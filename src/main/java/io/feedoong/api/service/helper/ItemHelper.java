package io.feedoong.api.service.helper;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ItemHelper {
    private final ItemRepository itemRepository;

    public Page<ChannelItemDTO> getItems(Pageable pageable, User user) {
        return itemRepository.findChannelItemByUser(pageable, user);
    }

    public Page<ChannelItemDTO> getItemsByChannel(Pageable pageable, Optional<User> user, Channel channel) {
        return itemRepository.findChannelItemsByChannel(pageable, user, channel);
    }

    public Page<ChannelItemDTO> findLikedItems(Pageable pageable, User user) {
        return itemRepository.findLikedChannelItems(pageable, user);
    }
}
