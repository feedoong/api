package io.feedoong.api.service.helper;

import io.feedoong.api.domain.user.User;
import io.feedoong.api.domain.user.UserRepository;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserHelper {

    private final UserRepository userRepository;

    public Optional<User> getOptionalByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Optional<User> findOptByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findOptionalByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findOptNotDeletedByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email);
    }
}
