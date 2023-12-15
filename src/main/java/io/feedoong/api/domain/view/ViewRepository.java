package io.feedoong.api.domain.view;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View, Long> {

    Optional<View> findViewByUserAndItemAndDeletedAtIsNull(User user, Item item);
}
