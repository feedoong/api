package io.feedoong.api.domain.repository;

import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s JOIN s.channel c GROUP BY c.id ORDER BY s.createdAt")
    Page<Subscription> findMostRecentSubscriptionsByChannel(Pageable pageable);

    @Query("SELECT s.channel.id FROM Subscription s WHERE s.user = :user AND s.channel.id IN :channelIds")
    List<Long> findSubscribedChannelIdsByUser(@Param("user") User user, @Param("channelIds") List<Long> channelIds);
}
