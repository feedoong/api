package io.feedoong.api.service;

import io.feedoong.api.domain.channel.dto.ChannelDetailsDTO;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.service.helper.ChannelHelper;
import io.feedoong.api.service.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChannelService {
    private final ChannelHelper channelHelper;
    private final UserHelper userHelper;

    @Transactional(readOnly = true)
    public ChannelDetailsDTO getChannelDetails(Long channelId, UserDetails requestUser) {
        Optional<User> user = getOptionalUser(requestUser);
        return channelHelper.getChannelDetails(user, channelId);
    }

    private Optional<User> getOptionalUser(UserDetails requestUser) {
        if (requestUser != null) {
            return userHelper.getOptionalByEmail(requestUser.getUsername());
        } else {
            return Optional.empty();
        }
    }
}
