package io.feedoong.api.domain.repository;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserAndChannel(User user, Channel channel);
}
