package io.feedoong.api.domain.repository;

import io.feedoong.api.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
