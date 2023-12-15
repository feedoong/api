package io.feedoong.api.domain.item;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.domain.like.LikeRepository;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.domain.subscription.SubscriptionRepository;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.domain.view.View;
import io.feedoong.api.domain.view.ViewRepository;
import io.feedoong.api.shared.base.BaseRepositoryTest;
import io.feedoong.api.shared.factory.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CustomItemRepository 인터페이스")
class CustomItemRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Nested
    @DisplayName("findChannelItemByUser 메소드")
    class FindChannelItemByUser {
        private User user;
        private Pageable pageable;

        @BeforeEach
        void prepare() {
            user = UserFactory.create();
            userRepository.save(user);

            pageable = PageRequest.of(0, 10, Sort.by("publishedAt").ascending());
        }

        @Nested
        @DisplayName("구독한 채널이 존재하며")
        class WithSubscribedChannels {
            private Channel channel;

            @BeforeEach
            void prepare() {
                channel = ChannelFactory.create();
                channelRepository.save(channel);

                Subscription subscription = SubscriptionFactory.create(channel, user);
                subscriptionRepository.save(subscription);
            }

            @Nested
            @DisplayName("채널에 아이템이 존재하지 않으면")
            class WithoutItems {
                @Test
                @DisplayName("빈 Page 객체를 리턴한다.")
                public void should() throws Exception {
                    Page<ChannelItemDTO> result = itemRepository.findChannelItemByUser(pageable, user);
                    assertThat(result).isEmpty();
                }
            }

            @Nested
            @DisplayName("채널에 아이템이 존재하면")
            class WithItems {
                @BeforeEach
                void prepare() {
                    Item item = ItemFactory.create(channel);
                    itemRepository.save(item);
                }

                @Test
                @DisplayName("아이템 정보를 담은 Page 객체를 리턴한다")
                public void should() throws Exception {
                    Page<ChannelItemDTO> result = itemRepository.findChannelItemByUser(pageable, user);

                    assertAll("Page 객체 검증",
                            () -> assertThat(result).as("결과는 비어있지 않아야 한다").isNotEmpty(),
                            () -> assertThat(result.getTotalPages()).as("총 페이지 수는 1이어야 한다").isEqualTo(1),
                            () -> assertThat(result.getTotalElements()).as("총 요소 수는 1이어야 한다").isEqualTo(1),
                            () -> assertThat(result.getNumber()).as("페이지 번호는 0이어야 한다").isEqualTo(0),
                            () -> assertThat(result.getSize()).as("페이지 크기는 10이어야 한다").isEqualTo(10)
                    );
                }
            }

            @Nested
            @DisplayName("채널의 아이템에 좋아요를 했으면")
            class WithLikeItems {
                @BeforeEach
                void prepare() {
                    Item item = ItemFactory.create(channel);
                    itemRepository.save(item);

                    Like like = LikeFactory.create(user, item);
                    likeRepository.save(like);
                }

                @Test
                @DisplayName("좋아요 했음을 알 수 있는 데이터와 함께 아이템 정보를 담은 Page 객체를 리턴한다.")
                public void should() throws Exception {
                    Page<ChannelItemDTO> result = itemRepository.findChannelItemByUser(pageable, user);

                    assertAll("Page 객체 검증",
                            () -> assertThat(result).isNotEmpty().as("결과는 비어있지 않아야 한다"),
                            () -> assertThat(result.getTotalPages()).as("총 페이지 수는 1이어야 한다").isEqualTo(1),
                            () -> assertThat(result.getTotalElements()).as("총 요소 수는 1이어야 한다").isEqualTo(1),
                            () -> assertThat(result.getNumber()).as("페이지 번호는 0이어야 한다").isEqualTo(0),
                            () -> assertThat(result.getSize()).as("페이지 크기는 10이어야 한다").isEqualTo(10)
                    );
                    assertAll("Content 검증",
                            () -> assertThat(result.getContent().get(0).getIsLiked()).isTrue().as("좋아요 데이터가 참이어야 한다")
                    );
                }
            }
        }

        @Nested
        @DisplayName("구독한 채널이 존재하지 않으면")
        class WithoutSubscribedChannels {
            @Test
            @DisplayName("빈 Page 객체를 리턴한다.")
            public void shouldReturn_EmptyPage() throws Exception {
                Page<ChannelItemDTO> result = itemRepository.findChannelItemByUser(pageable, user);
                assertThat(result).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("findChannelItemsByChannel 메소드")
    class FindChannelItemsByChannel {
        private Pageable pageable;
        private Channel channel;
        private Item item;

        @BeforeEach
        void prepare() {
            pageable = PageRequest.of(0, 10, Sort.by("publishedAt").ascending());

            channel = ChannelFactory.create();
            channelRepository.save(channel);

            item = ItemFactory.create(channel);
            itemRepository.save(item);
        }

        @Nested
        @DisplayName("로그인하지 않은 사용자가 조회하면")
        class AnonymousUser {
            private Optional<User> anonymousUser;

            @BeforeEach
            void prepare() {
                anonymousUser = Optional.empty();
            }

            @Test
            @DisplayName("좋아요, 조회 여부가 전부 false인 아이템을 포함한 Page 객체를 리턴한다.")
            public void should() throws Exception {
                Page<ChannelItemDTO> result = itemRepository.findChannelItemsByChannel(pageable, anonymousUser, channel);

                assertAll("Page 객체 검증",
                        () -> assertThat(result).isNotEmpty().as("결과는 비어있지 않아야 한다"),
                        () -> assertThat(result.getTotalPages()).as("총 페이지 수는 1이어야 한다").isEqualTo(1),
                        () -> assertThat(result.getTotalElements()).as("총 요소 수는 1이어야 한다").isEqualTo(1),
                        () -> assertThat(result.getNumber()).as("페이지 번호는 0이어야 한다").isEqualTo(0),
                        () -> assertThat(result.getSize()).as("페이지 크기는 10이어야 한다").isEqualTo(10)
                );
                assertAll("Content 검증",
                        () -> assertThat(result.getContent().get(0).getIsLiked()).isFalse().as("좋아요 데이터가 거짓이어야 한다"),
                        () -> assertThat(result.getContent().get(0).getIsViewed()).isFalse().as("조회 데이터가 거짓이어야 한다")
                );
            }
        }

        @Nested
        @DisplayName("로그인한 사용자가 조회하면")
        class LoggedInUser {
            private Optional<User> loggedInUser;

            @BeforeEach
            void prepare() {
                User user = UserFactory.create();
                userRepository.save(user);
                loggedInUser = Optional.of(user);

                Like like = LikeFactory.create(user, item);
                likeRepository.save(like);

                View view = ViewFactory.create(user, item);
                viewRepository.save(view);
            }

            @Test
            @DisplayName("정상적으로 좋아요, 조회 여부가 담긴 아이템을 포함한 Page 객체를 리턴한다.")
            public void should() throws Exception {
                Page<ChannelItemDTO> result = itemRepository.findChannelItemsByChannel(pageable, loggedInUser, channel);

                assertAll("Page 객체 검증",
                        () -> assertThat(result).isNotEmpty().as("결과는 비어있지 않아야 한다"),
                        () -> assertThat(result.getTotalPages()).as("총 페이지 수는 1이어야 한다").isEqualTo(1),
                        () -> assertThat(result.getTotalElements()).as("총 요소 수는 1이어야 한다").isEqualTo(1),
                        () -> assertThat(result.getNumber()).as("페이지 번호는 0이어야 한다").isEqualTo(0),
                        () -> assertThat(result.getSize()).as("페이지 크기는 10이어야 한다").isEqualTo(10)
                );
                assertAll("Content 검증",
                        () -> assertThat(result.getContent().get(0).getIsLiked()).isTrue().as("좋아요 데이터가 거짓이어야 한다"),
                        () -> assertThat(result.getContent().get(0).getIsViewed()).isTrue().as("조회 데이터가 거짓이어야 한다")
                );
            }
        }
    }

    @Nested
    @DisplayName("findLikedChannelItems 메소드")
    class FindLikedChannelItems {
        @Nested
        @DisplayName("좋아요하지 않은 아이템만 존재하면")
        class WithNotLikedItems {
            private User user;
            private Pageable pageable;

            @BeforeEach
            void prepare() {
                Channel channel = ChannelFactory.create();
                channelRepository.save(channel);

                Item item = ItemFactory.create(channel);
                itemRepository.save(item);

                user = UserFactory.create();
                userRepository.save(user);

                pageable = PageRequest.of(0, 10, Sort.by("publishedAt").ascending());
            }

            @Test
            @DisplayName("빈 Page 객체를 리턴한다.")
            public void shouldReturn_Empty_PageObject() throws Exception {
                Page<ChannelItemDTO> result = itemRepository.findLikedChannelItems(pageable, user);

                assertThat(result.getContent()).isEmpty();
            }
        }

        @Nested
        @DisplayName("좋아요한 아이템이 존재하면")
        class WithLikedItem {
            private User user;
            private Pageable pageable;

            @BeforeEach
            void prepare() {
                Channel channel = ChannelFactory.create();
                channelRepository.save(channel);

                Item item = ItemFactory.create(channel);
                itemRepository.save(item);

                user = UserFactory.create();
                userRepository.save(user);

                Like like = LikeFactory.create(user, item);
                likeRepository.save(like);

                pageable = PageRequest.of(0, 10, Sort.by("publishedAt").ascending());
            }

            @Test
            @DisplayName("아이템을 content로 담고 있는 Page 객체를 성공적으로 리턴한다.")
            public void shouldReturn_PageObject_Successfully() throws Exception {
                Page<ChannelItemDTO> result = itemRepository.findLikedChannelItems(pageable, user);

                assertAll("Page 객체 검증",
                        () -> assertThat(result).isNotEmpty().as("결과는 비어있지 않아야 한다"),
                        () -> assertThat(result.getTotalPages()).as("총 페이지 수는 1이어야 한다").isEqualTo(1),
                        () -> assertThat(result.getTotalElements()).as("총 요소 수는 1이어야 한다").isEqualTo(1),
                        () -> assertThat(result.getNumber()).as("페이지 번호는 0이어야 한다").isEqualTo(0),
                        () -> assertThat(result.getSize()).as("페이지 크기는 10이어야 한다").isEqualTo(10),
                        () -> assertThat(result.isFirst() && result.isLast()).as("해당 페이지가 처음이자 마지막이어야 한다").isTrue()
                );
                assertAll("Content 검증",
                        () -> assertThat(result.getContent().get(0).getIsLiked()).isTrue().as("좋아요 데이터가 참이어야 한다"),
                        () -> assertThat(result.getContent().get(0).getIsViewed()).isFalse().as("조회 데이터가 거짓이어야 한다")
                );
            }
        }

        @Nested
        @DisplayName("좋아요한 아이템이 10개 존재하며")
        class WithLikedItems {
            private User user;

            @BeforeEach
            void prepare() {
                Channel channel = ChannelFactory.create();
                channelRepository.save(channel);

                user = UserFactory.create();
                userRepository.save(user);

                List<Item> items = ItemFactory.createItems(channel);
                itemRepository.saveAll(items);

                List<Like> likes = LikeFactory.createLikes(user, items);
                likeRepository.saveAll(likes);

            }

            @Nested
            @DisplayName("pageNumber 0, pageSize 5로 좋아요한 아이템을 조회하면")
            class WithPageNumber0AndPageSize5 {
                private Pageable pageable;

                @BeforeEach
                void prepare() {
                    pageable = PageRequest.of(0, 5, Sort.by("publishedAt").ascending());
                }

                @Test
                @DisplayName("총 요소 수는 10개, 조회된 아이템은 5개, 첫 페이지가 성공적으로 조회된다.")
                public void shouldReturn_PageObject_With_10_TotalElements() throws Exception {
                    Page<ChannelItemDTO> result = itemRepository.findLikedChannelItems(pageable, user);

                    assertAll("Page 객체 검증",
                            () -> assertThat(result).isNotEmpty().as("결과는 비어있지 않아야 한다"),
                            () -> assertThat(result.getTotalPages()).as("총 페이지 수는 2이어야 한다").isEqualTo(2),
                            () -> assertThat(result.getTotalElements()).as("총 요소 수는 10이어야 한다").isEqualTo(10),
                            () -> assertThat(result.getNumber()).as("페이지 번호는 0이어야 한다").isEqualTo(0),
                            () -> assertThat(result.getSize()).as("페이지 크기는 5이어야 한다").isEqualTo(5),
                            () -> assertThat(result.isFirst()).as("해당 페이지가 처음이어야 한다").isTrue(),
                            () -> assertThat(result.isLast()).as("해당 페이지가 마지막이 아니어야 한다").isFalse()
                    );
                    assertAll("Content 검증",
                            () -> result.getContent().stream()
                                    .map(item -> assertThat(item.getIsLiked()).isTrue().as("좋아요 데이터가 참이어야 한다.")),
                            () -> result.getContent().stream()
                                    .map(item -> assertThat(item.getIsViewed()).isFalse().as("조회 데이터가 거짓이어야 한다."))
                    );
                }
            }
        }
    }
}