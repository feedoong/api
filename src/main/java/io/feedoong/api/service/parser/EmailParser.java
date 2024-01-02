package io.feedoong.api.service.parser;

import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailParser {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@.+");
    private static final int USERNAME_GROUP_NUM = 1;

    public String getUsernameFromEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);

        if (!matcher.matches()) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        return matcher.group(USERNAME_GROUP_NUM);
    }
}
