package io.feedoong.api.service.helper;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.subscription.SubscriptionRepository;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SubscriptionHelper {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription getSubscription(User user, Channel channel) {
        return subscriptionRepository.findByUserAndChannel(user, channel)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    public void delete(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }
}
