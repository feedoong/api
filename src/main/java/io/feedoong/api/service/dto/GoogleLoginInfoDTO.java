package io.feedoong.api.service.dto;

import io.feedoong.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginInfoDTO {
    private String accessToken;
    private String refreshToken;
    private String name;
    private String email;
    private String username;
    private String profileImageUrl;

    public static GoogleLoginInfoDTO of(String accessToken, String refreshToken, User user) {
        return GoogleLoginInfoDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
