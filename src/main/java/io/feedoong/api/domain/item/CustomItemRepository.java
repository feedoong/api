package io.feedoong.api.domain.item;

import io.feedoong.api.domain.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomItemRepository {
    Page<ChannelItemDTO> findChannelItemByUser(Pageable pageable, User user);
}
