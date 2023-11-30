package io.feedoong.api.domain.subscription;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserAndChannel(User user, Channel channel);
}
