package io.feedoong.api.service.helper;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.like.LikeRepository;
import io.feedoong.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LikeHelper {
    private final LikeRepository likeRepository;

    public Like save(Like like) {
        return likeRepository.save(like);
    }

    public Optional<Like> findByUserAndItem(User user, Item item) {
        return likeRepository.findByUserAndItem(user, item);
    }
}
