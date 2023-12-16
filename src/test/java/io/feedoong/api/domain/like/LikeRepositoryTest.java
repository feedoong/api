package io.feedoong.api.domain.like;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.item.ItemRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.shared.base.BaseRepositoryTest;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.ItemFactory;
import io.feedoong.api.shared.factory.LikeFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LikeRepository 인터페이스")
class LikeRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Nested
    @DisplayName("findByUserAndItem 메소드")
    class FindByUserAndItem {
        private User user;
        private Item item;

        @BeforeEach
        void prepare() {
            user = UserFactory.create();
            userRepository.save(user);

            Channel channel = ChannelFactory.create();
            channelRepository.save(channel);

            item = ItemFactory.create(channel);
            itemRepository.save(item);
        }

        @Nested
        @DisplayName("좋아요가 이미 존재하면")
        class WithLike {
            @BeforeEach
            void prepare() {
                Like like = LikeFactory.create(user, item);
                likeRepository.save(like);
            }

            @Test
            @DisplayName("좋아요를 담은 Optional 객체를 리턴한다.")
            public void shouldReturn_OptionalLike() throws Exception {
                Optional<Like> result = likeRepository.findByUserAndItem(user, item);

                assertThat(result).isNotEmpty();
                assertThat(result.get().getItem()).usingRecursiveComparison().isEqualTo(item);
                assertThat(result.get().getUser()).usingRecursiveComparison().isEqualTo(user);
            }
        }

        @Nested
        @DisplayName("좋아요가 존재하지 않으면")
        class WithoutLike {
            @Test
            @DisplayName("빈 Optional 객체를 리턴한다.")
            public void shouldThrow_EmptyOptional() throws Exception {
                Optional<Like> result = likeRepository.findByUserAndItem(user, item);

                assertThat(result).isEmpty();
            }
        }
    }
}