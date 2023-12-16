package io.feedoong.api.service;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.service.dto.LikeItemDTO;
import io.feedoong.api.service.helper.ItemHelper;
import io.feedoong.api.service.helper.LikeHelper;
import io.feedoong.api.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final UserHelper userHelper;
    private final ItemHelper itemHelper;
    private final LikeHelper likeHelper;

    @Transactional(readOnly = true)
    public LikeItemDTO like(UserDetails requestUser, Long itemId) {
        User user = userHelper.findByEmail(requestUser.getUsername());
        Item item = itemHelper.findById(itemId);

        checkAlreadyLiked(user, item);

        Like newLike = Like.of(user, item);
        likeHelper.save(newLike);

        final boolean isLiked = true;
        return new LikeItemDTO(item.getId(), isLiked);
    }

    @Transactional
    public void unlike(UserDetails requestUser, Long itemId) {
        User user = userHelper.findByEmail(requestUser.getUsername());
        Item item = itemHelper.findById(itemId);
        Like like = likeHelper.findOne(user, item);

        likeHelper.delete(like);
    }

    private void checkAlreadyLiked(User user, Item item) {
        Optional<Like> like = likeHelper.findOpt(user, item);
        if (like.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }
}
