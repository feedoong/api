package io.feedoong.api.shared.base;

import io.feedoong.api.global.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public abstract class BaseRestDocsTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TokenProvider tokenProvider;
}
