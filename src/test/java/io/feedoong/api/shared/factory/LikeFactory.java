package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.item.Item;

public class LikeFactory {
    public static Like create(User user, Item item) {
        return Like.builder()
                .user(user)
                .item(item)
                .build();
    }
}
