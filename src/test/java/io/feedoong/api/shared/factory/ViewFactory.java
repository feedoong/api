package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.view.View;

public class ViewFactory {
    public static View create(User user, Item item) {
        return View.builder()
                .user(user)
                .item(item)
                .build();
    }
}
