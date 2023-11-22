package io.feedoong.api.service;

import io.feedoong.api.domain.User;
import io.feedoong.api.domain.repository.helper.ChannelHelper;
import io.feedoong.api.domain.repository.helper.UserHelper;
import io.feedoong.api.service.dto.ChannelDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final ChannelHelper channelHelper;
    private final UserHelper userHelper;

    public Page<ChannelDetailsDTO> getSubscribedChannels(Pageable pageable, UserDetails requestUser) {
        User user = userHelper.getByEmail(requestUser.getUsername());
        return channelHelper.getSubscribedChannels(pageable, user);
    }
}
