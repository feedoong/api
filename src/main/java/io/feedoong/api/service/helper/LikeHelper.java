package io.feedoong.api.service.helper;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.like.LikeRepository;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
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

    public void delete(Like like) {
        likeRepository.delete(like);
    }

    public Like findOne(User user, Item item) {
        return likeRepository.findByUserAndItem(user, item)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));
    }

    public Optional<Like> findOpt(User user, Item item) {
        return likeRepository.findByUserAndItem(user, item);
    }
}
