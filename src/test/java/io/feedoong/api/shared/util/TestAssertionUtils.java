package io.feedoong.api.shared.util;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

public class TestAssertionUtils {

    public static CustomExceptionAssert assertThrowsCustomException(ThrowingCallable throwingCallable) {
        return new CustomExceptionAssert(throwingCallable);
    }
}
