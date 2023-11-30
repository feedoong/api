package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.user.User;

public class SubscriptionFactory {
    public static Subscription create(Channel channel, User user) {
        return Subscription.builder()
                .channel(channel)
                .user(user)
                .build();
    }
}
