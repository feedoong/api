package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.channel.Channel;

public class ChannelFactory {
    public static Channel create() {
        return Channel.builder()
                .title("feedoong.log")
                .description("")
                .url("https://velog.io/@feedoong")
                .feedUrl("https://v2.velog.io/rss/feedoong")
                .imageUrl("https://velog.velcdn.com/images/feedoong/profile/1c8a2eb9-dbd9-499d-b846-3d3855c94bc0/social_profile.png")
                .build();
    }
}
