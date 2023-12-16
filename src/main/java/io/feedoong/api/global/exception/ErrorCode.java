package io.feedoong.api.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNAUTHORIZED("인증에 실패하여 요청을 완료할 수 없습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    SUBSCRIPTION_NOT_FOUND("구독 정보를 찾을 수 없습니다."),
    ITEM_NOT_FOUND("아이템을 찾을 수 없습니다."),
    ALREADY_LIKED("이미 좋아요한 게시글입니다."),
    INTERNAL_SERVER_ERROR("서버에 오류가 발생하여 요청을 완료할 수 없습니다.");

    private final String message;
}
