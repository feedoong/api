package io.feedoong.api.domain.item;

import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.dto.ChannelItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomItemRepository {
    Page<ChannelItemDTO> findChannelItemByUser(Pageable pageable, User user);

    Page<ChannelItemDTO> findChannelItemsByChannel(Pageable pageable, Optional<User> user, Channel channel);
}
