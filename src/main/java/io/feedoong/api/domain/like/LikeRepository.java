package io.feedoong.api.domain.like;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndItem(User user, Item item);
}
