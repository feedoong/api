package io.feedoong.api.shared.factory;

import io.feedoong.api.domain.User;

public class UserFactory {

    public static User create() {
        return User.builder()
                .name("jun")
                .email("bigfanoftim@gmail.com")
                .username("bigfanoftim")
                .build();
    }
}
