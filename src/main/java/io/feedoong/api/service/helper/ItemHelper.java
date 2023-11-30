package io.feedoong.api.service.helper;

import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import io.feedoong.api.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemHelper {
    private final ItemRepository itemRepository;

    public Page<ChannelItemDTO> getItems(Pageable pageable, User user) {
        return itemRepository.findChannelItemByUser(pageable, user);
    }
}
