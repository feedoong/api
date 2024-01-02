package io.feedoong.api.global.util;

import io.feedoong.api.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.feedoong.api.shared.util.TestAssertionUtils.assertThrowsCustomException;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EmailParser 클래스")
class EmailParserTest {

    final EmailParser emailParser = new EmailParser();

    @Nested
    @DisplayName("getUsernameFromEmail 메소드")
    class GetUsernameFromEmail {

        @Nested
        @DisplayName("@이 존재하고, @ 앞뒤로 문자열이 존재하는 이메일이 주어지면")
        class WithValidEmail {

            private String validEmail;

            @BeforeEach
            void prepare() {
                validEmail = "bigfanoftim@gmail.com";
            }

            @Test
            @DisplayName("@ 기준으로 분리하여 첫 번째 캡쳐 그룹에 해당하는 문자열을 리턴한다.")
            public void shouldReturn_Username() throws Exception {
                String username = emailParser.getUsernameFromEmail(validEmail);
                assertThat(username).isEqualTo("bigfanoftim");
            }
        }

        @Nested
        @DisplayName("@이 없는 올바르지 않은 형식의 이메일이 주어지면")
        class EmailWithoutAtGiven {

            private String invalidEmail;

            @BeforeEach
            void prepare() {
                invalidEmail = "bigfanoftim";
            }

            @Test
            @DisplayName("INVALID_EMAIL_FORMAT CustomException을 던진다.")
            public void shouldThrow_INVALID_EMAIL_FORMAT_CustomException() throws Exception {
                assertThrowsCustomException(() -> emailParser.getUsernameFromEmail(invalidEmail))
                        .with(ErrorCode.INVALID_EMAIL_FORMAT);
            }
        }

        @Nested
        @DisplayName("@ 뒤에 문자열이 존재하지 않는 이메일이 주어지면")
        class WithInvalidEmail {

            private String invalidEmail;

            @BeforeEach
            void prepare() {
                invalidEmail = "bigfanoftim@";
            }

            @Test
            @DisplayName("INVALID_EMAIL_FORMAT CustomException을 던진다.")
            public void shouldThrow_INVALID_EMAIL_FORMAT_CustomException() throws Exception {
                assertThrowsCustomException(() -> emailParser.getUsernameFromEmail(invalidEmail))
                        .with(ErrorCode.INVALID_EMAIL_FORMAT);
            }
        }
    }
}