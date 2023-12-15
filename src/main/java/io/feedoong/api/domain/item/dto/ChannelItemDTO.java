package io.feedoong.api.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelItemDTO {
    private Long id;
    private String title;
    private String description;
    private String link;
    private String guid;
    private LocalDateTime publishedAt;
    private String imageUrl;
    private Boolean isLiked;
    private Boolean isViewed;
    private Long channelId;
    private String channelTitle;
    private String channelImageUrl;
}
