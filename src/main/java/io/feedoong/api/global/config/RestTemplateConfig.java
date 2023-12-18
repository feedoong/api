package io.feedoong.api.global.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    private static final int SQ_TIMEOUT = 3;
    private static final int REQ_TIMEOUT = 3;
    private static final int RES_TIMEOUT = 3;

    private static final int MAX_CONN = 100;
    private static final int MAX_CONN_PER_ROUTE = 20;

    @Bean
    public RestTemplate buildRestTemplate() {
        Timeout socketTimeout = Timeout.ofSeconds(SQ_TIMEOUT);
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(socketTimeout)
                .build();

        Timeout requestTimeout = Timeout.ofSeconds(REQ_TIMEOUT);
        Timeout responseTimeout = Timeout.ofSeconds(RES_TIMEOUT);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(requestTimeout)
                .setResponseTimeout(responseTimeout)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_CONN);
        cm.setDefaultMaxPerRoute(MAX_CONN_PER_ROUTE);
        cm.setDefaultSocketConfig(socketConfig);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }
}
