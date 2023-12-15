package io.feedoong.api.domain.view;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.channel.ChannelRepository;
import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.item.ItemRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.shared.base.BaseRepositoryTest;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.ItemFactory;
import io.feedoong.api.shared.factory.UserFactory;
import io.feedoong.api.shared.factory.ViewFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ViewRepository 인터페이스")
class ViewRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Nested
    @DisplayName("findViewByUserAndItemAndDeletedAtIsNull 메소드")
    class FindViewByUserAndItemAndDeletedAtIsNull {
        private User user;
        private Item item;

        @Nested
        @DisplayName("이미 조회(View)가 존재하면")
        class WithView {
            @BeforeEach
            void prepare() {
                user = UserFactory.create();
                userRepository.save(user);

                Channel channel = ChannelFactory.create();
                channelRepository.save(channel);

                item = ItemFactory.create(channel);
                itemRepository.save(item);

                View view = ViewFactory.create(user, item);
                viewRepository.save(view);
            }

            @Test
            @DisplayName("조회(View)를 담은 Optional 객체를 리턴한다.")
            public void shouldReturn_OptionalView() throws Exception {
                Optional<View> result = viewRepository.findViewByUserAndItemAndDeletedAtIsNull(user, item);

                assertThat(result).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("조회(View)가 존재하지 않으면")
        class WithoutView {
            @Test
            @DisplayName("빈 Optional 객체를 리턴한다.")
            public void shouldReturn_EmptyOptional() throws Exception {
                Optional<View> result = viewRepository.findViewByUserAndItemAndDeletedAtIsNull(user, item);

                assertThat(result).isEmpty();
            }
        }
    }
}