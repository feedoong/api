package io.feedoong.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.feedoong.api.domain.refreshtoken.RefreshToken;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.feedoong.api.service.dto.GoogleLoginInfoDTO;
import io.feedoong.api.service.dto.GoogleUser;
import io.feedoong.api.service.helper.RefreshTokenHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.service.parser.EmailParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final UserHelper userHelper;
    private final RefreshTokenHelper refreshTokenHelper;
    private final EmailParser emailParser;

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

    private final List<String> fruitNames = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "grape", "kiwi", "lemon", "mango", "orange", "peach", "pear", "pineapple", "strawberry"));

    @Transactional
    public GoogleLoginInfoDTO googleLogin(String googleAccessToken) {
        ResponseEntity<String> userInfoResponse = requestUserInfo(googleAccessToken);
        GoogleUser googleUser = createGoogleUser(userInfoResponse);

        String parsedUsername = emailParser.getUsernameFromEmail(googleUser.getEmail());
        String username = generateUniqueUsername(parsedUsername);

        User user = processUserLogin(googleUser, username);

        String accessTokenValue = tokenProvider.create(user);
        String refreshTokenValue = tokenProvider.createRefreshToken(user);
        RefreshToken refreshToken = new RefreshToken(refreshTokenValue, user);
        refreshTokenHelper.save(refreshToken);

        return GoogleLoginInfoDTO.of(accessTokenValue, refreshTokenValue, user);
    }

    private ResponseEntity<String> requestUserInfo(String googleAccessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, tokenProvider.getBearerPrefix() + googleAccessToken);

        return restTemplate.exchange(GOOGLE_USER_INFO_URL, HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class);
    }

    private GoogleUser createGoogleUser(ResponseEntity<String> userInfoResponse) {
        try {
            return objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.GOOGLE_USER_MAPPING_FAILED);
        }
    }

    private String generateUniqueUsername(String username) {
        List<String> shuffledFruitNames = new ArrayList<>(fruitNames);
        Collections.shuffle(shuffledFruitNames);

        for (String fruitName : shuffledFruitNames) {
            String uniqueUsername = username + "-" + fruitName;
            Optional<User> optionalUser = userHelper.findOptionalByUsername(uniqueUsername);

            if (optionalUser.isEmpty()) {
                return uniqueUsername;
            }
        }

        throw new CustomException(ErrorCode.UNIQUE_USERNAME_CREATION_FAILED);
    }

    private User processUserLogin(GoogleUser googleUser, String username) {
        boolean existsByEmail = userHelper.existsByEmail(googleUser.getEmail());

        if (!existsByEmail) {
            return createNewUser(googleUser, username);
        } else {
            return handleExistingUser(googleUser);
        }
    }

    private User createNewUser(GoogleUser googleUser, String username) {
        User newUser = User.of(
                googleUser.getName(),
                googleUser.getEmail(),
                googleUser.getPicture(),
                username
        );
        userHelper.save(newUser);
        // TODO: add private method to subscribe feedoong channel
        return newUser;
    }

    private User handleExistingUser(GoogleUser googleUser) {
        Optional<User> optNotDeletedUser = userHelper.findOptNotDeletedByEmail(googleUser.getEmail());

        if (optNotDeletedUser.isEmpty()) {
            User user = userHelper.findByEmail(googleUser.getEmail());
            user.reactivate();
            // TODO: reactivate all subscriptions & views
            return user;
        } else {
            return optNotDeletedUser.get();
        }
    }
}
