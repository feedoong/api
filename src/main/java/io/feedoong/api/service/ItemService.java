package io.feedoong.api.service;

import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.service.helper.ItemHelper;
import io.feedoong.api.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final UserHelper userHelper;
    private final ItemHelper itemHelper;

    public Page<ChannelItemDTO> getItems(Pageable pageable, UserDetails requestUser) {
        User user = userHelper.getByEmail(requestUser.getUsername());
        return itemHelper.getItems(pageable, user);
    }
}
