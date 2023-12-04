package io.feedoong.api.shared.base;

import io.feedoong.api.domain.user.User;
import io.feedoong.api.global.config.SecurityConfig;
import io.feedoong.api.global.security.CustomUserDetails;
import io.feedoong.api.global.security.CustomUserDetailsService;
import io.feedoong.api.global.security.jwt.TokenProvider;
import io.feedoong.api.shared.factory.UserFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public abstract class BaseRestDocsTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    protected String bearerToken;

    @PostConstruct
    private void set() {
        User user = UserFactory.create();
        bearerToken = tokenProvider.create(user);

        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new CustomUserDetails(user));
    }
}
