package io.feedoong.api.domain.channel;

import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.domain.user.User;

import java.util.Optional;

public interface CustomChannelRepository {

    Optional<ChannelDetailsDTO> getChannelDetails(Optional<User> user, Long channelId);
}
