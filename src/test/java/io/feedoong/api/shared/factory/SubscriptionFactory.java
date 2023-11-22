package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.User;

public class SubscriptionFactory {

    public static Subscription create(Channel channel, User user) {
        return Subscription.builder()
                .channel(channel)
                .user(user)
                .build();
    }
}
