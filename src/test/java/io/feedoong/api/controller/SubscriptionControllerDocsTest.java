package io.feedoong.api.controller;

import io.feedoong.api.domain.Channel;
import io.feedoong.api.domain.Subscription;
import io.feedoong.api.domain.User;
import io.feedoong.api.domain.repository.ChannelRepository;
import io.feedoong.api.domain.repository.SubscriptionRepository;
import io.feedoong.api.domain.repository.UserRepository;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.feedoong.api.shared.factory.ChannelFactory;
import io.feedoong.api.shared.factory.SubscriptionFactory;
import io.feedoong.api.shared.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("SubscriptionController 클래스 RestDocs")
class SubscriptionControllerDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Nested
    @DisplayName("GET /v2/subscriptions - getSubscriptions 메소드")
    class GetSubscriptions {
        private Channel channel;
        private User user;
        private Subscription subscription;
        private String token;

        @BeforeEach
        void prepare() {
            user = UserFactory.create();
            userRepository.save(user);

            channel = ChannelFactory.create();
            channelRepository.save(channel);

            subscription = SubscriptionFactory.create(channel, user);
            subscriptionRepository.save(subscription);

            token = tokenProvider.create(user);
        }

        @Test
        @DisplayName("성공 테스트")
        public void should() throws Exception {
            mockMvc.perform(get("/v2/subscriptions")
                            .contentType("application/json")
                            .header("Authorization", token)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "createdAt")
                            .param("direction", "desc"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10));
        }
    }
}