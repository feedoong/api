package io.feedoong.api.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PAGE_NUMBER("페이지 번호는 0보다 작을 수 없습니다."),
    INVALID_PAGE_SIZE("페이지 크기는 1보다 작을 수 없습니다."),

    UNAUTHORIZED("인증에 실패하여 요청을 완료할 수 없습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("서버에 오류가 발생하여 요청을 완료할 수 없습니다.");

    private final String message;
}
