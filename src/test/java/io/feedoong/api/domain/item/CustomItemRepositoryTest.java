package io.feedoong.api.domain.item;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.domain.like.LikeRepository;
import io.feedoong.api.domain.repository.ChannelRepository;
import io.feedoong.api.domain.repository.SubscriptionRepository;
import io.feedoong.api.domain.repository.UserRepository;
import io.feedoong.api.shared.base.BaseRepositoryTest;
import io.feedoong.api.shared.factory.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
}