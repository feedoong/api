package io.feedoong.api.service;

import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.view.View;
import io.feedoong.api.service.dto.ViewedItemDTO;
import io.feedoong.api.service.helper.ItemHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.service.helper.ViewHelper;
import io.feedoong.api.service.mapper.ViewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ViewService {
    private final UserHelper userHelper;
    private final ItemHelper itemHelper;
    private final ViewHelper viewHelper;

    public ViewedItemDTO viewItem(UserDetails requestUser, Long itemId) {
        User user = userHelper.findByEmail(requestUser.getUsername());
        Item item = itemHelper.findById(itemId);

        Optional<View> view = viewHelper.findOptionalItemView(user, item);
        if (view.isEmpty()) {
            View newView = View.builder()
                    .user(user)
                    .item(item)
                    .build();
            viewHelper.save(newView);
        }

        final boolean isViewed = true;
        return ViewMapper.toViewedItemDTO(item, isViewed);
    }
}
