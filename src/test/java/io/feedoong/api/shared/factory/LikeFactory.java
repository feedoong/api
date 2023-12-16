package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.like.Like;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.item.Item;
import io.feedoong.api.service.dto.LikeItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LikeFactory {
    public static Like create(User user, Item item) {
        return Like.builder()
                .user(user)
                .item(item)
                .build();
    }

    public static List<Like> createLikes(User user, List<Item> items) {
        return items.stream()
                .map(item -> Like.builder()
                        .user(user)
                        .item(item)
                        .build())
                .toList();
    }

    public static LikeItemDTO createLikeItemDTO() {
        return LikeItemDTO.builder()
                .itemId(1L)
                .isLiked(true)
                .build();
    }
}
