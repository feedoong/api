package io.feedoong.api.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDetailsDTO {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String feedUrl;
    private String imageUrl;
    private Boolean isSubscribed;
}

