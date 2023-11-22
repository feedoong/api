package io.feedoong.api.domain.repository;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.User;
import io.feedoong.api.shared.base.BaseRepositoryTest;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.SubscriptionFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChannelRepository 인터페이스")
class ChannelRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Nested
    @DisplayName("findSubscribedChannelsByUser 메소드")
    class FindSubscribedChannelsByUser {

        @Nested
        @DisplayName("유저가 구독한 채널이 있으면")
        class WithSubscribedChannels {

            private Channel channel;
            private User john;
            private User andy;
            private Subscription johnSubscription;
            private Subscription andySubscription;

            @BeforeEach
            void prepare() {
                john = UserFactory.createWithEmailUsername("john@gmail.com", "john");
                andy = UserFactory.createWithEmailUsername("andy@gmail.com", "andy");
                userRepository.saveAll(List.of(john, andy));

                channel = ChannelFactory.create();
                channelRepository.save(channel);

                johnSubscription = SubscriptionFactory.create(channel, john);
                andySubscription = SubscriptionFactory.create(channel, andy);
                subscriptionRepository.saveAll(List.of(johnSubscription, andySubscription));
            }

            @Test
            @DisplayName("해당 유저가 구독한 채널만 리턴한다.")
            public void shouldReturn_SubscribedChannels_ForUser() throws Exception {
                PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                Page<Channel> result = channelRepository.findSubscribedChannelsByUser(pageable, john);

                assertThat(result.getTotalElements()).isEqualTo(1);

                assertThat(result.getNumber()).isEqualTo(pageable.getPageNumber());
                assertThat(result.getSize()).isEqualTo(pageable.getPageSize());
                assertThat(result.getTotalPages()).isEqualTo(1);
                assertThat(result.getContent()).isNotEmpty();

                assertThat(result.hasNext()).isFalse();
                assertThat(result.isFirst()).isTrue();
                assertThat(result.isLast()).isTrue();
            }
        }

        @Nested
        @DisplayName("유저가 구독한 채널이 없으면")
        class WithoutSubscribedChannels {

            private User user;

            @BeforeEach
            void prepare() {
                user = UserFactory.create();
                userRepository.save(user);
            }

            @Test
            @DisplayName("아무 채널도 리턴하지 않는다.")
            public void shouldReturn_EmptyPage() throws Exception {
                PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
                Page<Channel> result = channelRepository.findSubscribedChannelsByUser(pageable, user);

                assertThat(result.getTotalElements()).isEqualTo(0);

                assertThat(result.getNumber()).isEqualTo(pageable.getPageNumber());
                assertThat(result.getSize()).isEqualTo(pageable.getPageSize());
                assertThat(result.getTotalPages()).isEqualTo(0);
                assertThat(result.getContent()).isEmpty();

                assertThat(result.hasNext()).isFalse();
                assertThat(result.isFirst()).isTrue();
                assertThat(result.isLast()).isTrue();

            }
        }
    }
}