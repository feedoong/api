package io.feedoong.api.domain.channel;

import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.subscription.SubscriptionRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.shared.base.BaseRepositoryTest;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.SubscriptionFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CustomChannelRepository 인터페이스")
public class CustomChannelRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Nested
    @DisplayName("getChannelDetails 메소드")
    class GetChannelDetails {
        @Nested
        @DisplayName("채널이 존재하고")
        class WithChannel {
            private Channel channel;

            @BeforeEach
            void prepare() {
                channel = ChannelFactory.create();
                channelRepository.save(channel);
            }

            @Nested
            @DisplayName("구독이 존재하고, 유저가 주어지면")
            class WithUserSubscription {
                private Optional<User> loggedInUser;

                @BeforeEach
                void prepare() {
                    User user = UserFactory.create();
                    userRepository.save(user);
                    loggedInUser = Optional.of(user);

                    Subscription subscription = SubscriptionFactory.create(channel, user);
                    subscriptionRepository.save(subscription);
                }

                @Test
                @DisplayName("구독 여부(true)와 함께 채널 정보를 리턴한다.")
                public void shouldReturn_ChannelDetails_With_SubscribedTrue() throws Exception {
                    Optional<ChannelDetailsDTO> channelDetailsOpt = channelRepository.getChannelDetails(loggedInUser, channel.getId());

                    assertThat(channelDetailsOpt).isNotEmpty();
                    ChannelDetailsDTO channelDetails = channelDetailsOpt.get();
                    assertThat(channelDetails.getId()).isEqualTo(channel.getId());
                    assertThat(channelDetails.getTitle()).isEqualTo(channel.getTitle());
                    assertThat(channelDetails.getDescription()).isEqualTo(channel.getDescription());
                    assertThat(channelDetails.getUrl()).isEqualTo(channel.getUrl());
                    assertThat(channelDetails.getFeedUrl()).isEqualTo(channel.getFeedUrl());
                    assertThat(channelDetails.getImageUrl()).isEqualTo(channel.getImageUrl());
                    assertThat(channelDetails.getIsSubscribed()).isTrue();
                }
            }

            @Nested
            @DisplayName("유저가 주어지지 않으면")
            class WithEmptyUser {
                private Optional<User> anonymousUser;

                @BeforeEach
                void prepare() {
                    anonymousUser = Optional.empty();
                }

                @Test
                @DisplayName("구독 여부(false)와 함께 채널 정보를 리턴한다.")
                public void shouldReturn_ChannelDetails_With_SubscribedFalse() throws Exception {
                    Optional<ChannelDetailsDTO> channelDetailsOpt = channelRepository.getChannelDetails(anonymousUser, channel.getId());

                    assertThat(channelDetailsOpt).isNotEmpty();
                    ChannelDetailsDTO channelDetails = channelDetailsOpt.get();
                    assertThat(channelDetails.getId()).isEqualTo(channel.getId());
                    assertThat(channelDetails.getTitle()).isEqualTo(channel.getTitle());
                    assertThat(channelDetails.getDescription()).isEqualTo(channel.getDescription());
                    assertThat(channelDetails.getUrl()).isEqualTo(channel.getUrl());
                    assertThat(channelDetails.getFeedUrl()).isEqualTo(channel.getFeedUrl());
                    assertThat(channelDetails.getImageUrl()).isEqualTo(channel.getImageUrl());
                    assertThat(channelDetails.getIsSubscribed()).isFalse();
                }
            }
        }

        @Nested
        @DisplayName("채널이 존재하지 않으면")
        class WithoutChannel {
            private Optional<User> user;

            @BeforeEach
            void prepare() {
                user = Optional.empty();
            }

            @Test
            @DisplayName("빈 Optional을 리턴한다.")
            public void shouldReturn_EmptyOptional() throws Exception {
                Optional<ChannelDetailsDTO> channelDetailsOpt = channelRepository.getChannelDetails(user, 0L);
                assertThat(channelDetailsOpt).isEmpty();
            }
        }
    }
}
