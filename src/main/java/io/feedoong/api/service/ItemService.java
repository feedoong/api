package io.feedoong.api.service;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.service.helper.ChannelHelper;
import io.feedoong.api.service.helper.ItemHelper;
import io.feedoong.api.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final UserHelper userHelper;
    private final ItemHelper itemHelper;
    private final ChannelHelper channelHelper;

    public Page<ChannelItemDTO> getItems(Pageable pageable, UserDetails requestUser) {
        User user = userHelper.getByEmail(requestUser.getUsername());
        return itemHelper.getItems(pageable, user);
    }

    public Page<ChannelItemDTO> getItemsByChannel(Pageable pageable, UserDetails requestUser, Long channelId) {
        Optional<User> user = getOptionalUser(requestUser);
        Channel channel = channelHelper.getChannel(channelId);

        return itemHelper.getItemsByChannel(pageable, user, channel);
    }

    private Optional<User> getOptionalUser(UserDetails requestUser) {
        if (requestUser != null) {
            return userHelper.getOptionalByEmail(requestUser.getUsername());
        } else {
            return Optional.empty();
        }
    }
}
