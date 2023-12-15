package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.view.View;
import io.feedoong.api.service.dto.ViewedItemDTO;

public class ViewFactory {
    public static View create(User user, Item item) {
        return View.builder()
                .user(user)
                .item(item)
                .build();
    }

    public static ViewedItemDTO viewedItemDTO() {
        return ViewedItemDTO.builder()
                .itemId(1L)
                .isViewed(true)
                .build();
    }
}
