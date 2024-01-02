package io.feedoong.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.feedoong.api.domain.channel.Channel;
import io.feedoong.api.domain.refreshtoken.RefreshToken;
import io.feedoong.api.domain.subscription.Subscription;
import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.feedoong.api.global.util.AuthenticationHeaderHandler;
import io.feedoong.api.service.dto.GoogleLoginInfoDTO;
import io.feedoong.api.service.dto.GoogleUser;
import io.feedoong.api.service.helper.ChannelHelper;
import io.feedoong.api.service.helper.RefreshTokenHelper;
import io.feedoong.api.service.helper.SubscriptionHelper;
import io.feedoong.api.service.helper.UserHelper;
import io.feedoong.api.global.util.EmailParser;
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
    private final AuthenticationHeaderHandler authenticationHeaderHandler;
    private final TokenProvider tokenProvider;
    private final UserHelper userHelper;
    private final ChannelHelper channelHelper;
    private final SubscriptionHelper subscriptionHelper;
    private final RefreshTokenHelper refreshTokenHelper;
    private final EmailParser emailParser;

    private final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

    private final List<String> fruitNames = Arrays.asList("apple", "banana", "cherry", "grape", "kiwi", "lemon", "mango", "orange", "peach", "pear", "pineapple", "strawberry");

    @Transactional
    public GoogleLoginInfoDTO googleLogin(String googleAccessToken) {
        ResponseEntity<String> userInfoResponse = requestUserInfo(googleAccessToken);
        GoogleUser googleUser = createGoogleUser(userInfoResponse);

        String parsedUsername = emailParser.getUsernameFromEmail(googleUser.getEmail());
        String username = generateUniqueUsername(parsedUsername);

        boolean existsByEmail = userHelper.existsByEmail(googleUser.getEmail());
        if (!existsByEmail) {
            User user = createNewUser(googleUser, username);
            subscribeFeedoongChannel(user);
        } else {
            if (isDeletedUser(googleUser)) {
                reactivateUser(googleUser);
                // TODO: reactivate all subscriptions & views
            }
        }

        User user = userHelper.findByEmail(googleUser.getEmail());
        String accessTokenValue = tokenProvider.create(user);
        String refreshTokenValue = tokenProvider.createRefreshToken(user);
        RefreshToken refreshToken = new RefreshToken(refreshTokenValue, user);
        refreshTokenHelper.save(refreshToken);

        return GoogleLoginInfoDTO.of(accessTokenValue, refreshTokenValue, user);
    }

    private ResponseEntity<String> requestUserInfo(String googleAccessToken) {
        HttpHeaders httpHeaders = authenticationHeaderHandler.createAuthorizationHeader(googleAccessToken);
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
            Optional<User> optionalUser = userHelper.findOptByUsername(uniqueUsername);

            if (optionalUser.isEmpty()) {
                return uniqueUsername;
            }
        }

        throw new CustomException(ErrorCode.UNIQUE_USERNAME_CREATION_FAILED);
    }

    private User createNewUser(GoogleUser googleUser, String username) {
        User newUser = User.of(
                googleUser.getName(),
                googleUser.getEmail(),
                googleUser.getPicture(),
                username
        );
        return userHelper.save(newUser);
    }

    /**
     * 기존에 ID 고정 조회 방식 대신 url 조회로 변경
     * 개발 환경 혹은 대표 url 변경과 같은 이유로 조회가 안될 수 있음을 고려하여
     * Optional 하게 처리
     *
     * isEmpty() 인 경우 대응을 위한 알림 처리 필요
     */
    private void subscribeFeedoongChannel(User user) {
        Optional<Channel> optFeedoongChannel = channelHelper.findOptFeedoongChannel();

        if (optFeedoongChannel.isPresent()) {
            Subscription subscription = Subscription.of(user, optFeedoongChannel.get());
            subscriptionHelper.save(subscription);
        }
    }

    private boolean isDeletedUser(GoogleUser googleUser) {
        Optional<User> optNotDeletedUser = userHelper.findOptNotDeletedByEmail(googleUser.getEmail());
        return optNotDeletedUser.isEmpty();
    }

    private void reactivateUser(GoogleUser googleUser) {
        User user = userHelper.findByEmail(googleUser.getEmail());
        user.reactivate();
    }
}
