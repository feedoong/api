package io.feedoong.api.service.helper;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.item.Item;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.item.dto.ChannelItemDTO;
import io.feedoong.api.domain.item.ItemRepository;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ItemHelper {
    private final ItemRepository itemRepository;

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
    }

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
