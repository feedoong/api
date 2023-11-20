package io.feedoong.api.shared.util;

import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CustomExceptionAssert {

    private CustomException caughtException;

    public CustomExceptionAssert(ThrowingCallable throwingCallable) {

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex)
                .satisfies(ex -> this.caughtException = ex);
    }

    public CustomExceptionAssert with(ErrorCode errorCode) {
        assertThat(caughtException.getErrorCode()).isEqualTo(errorCode);
        return this;
    }
}
