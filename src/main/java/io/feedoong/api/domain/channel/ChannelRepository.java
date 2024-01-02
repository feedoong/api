package io.feedoong.api.domain.channel;

import io.feedoong.api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long>, CustomChannelRepository {

    Optional<Channel> findChannelByUrl(String url);

    @Query("SELECT c FROM Subscription s JOIN s.channel c WHERE s.user = :user")
    Page<Channel> findSubscribedChannelsByUser(Pageable pageable, User user);
}
