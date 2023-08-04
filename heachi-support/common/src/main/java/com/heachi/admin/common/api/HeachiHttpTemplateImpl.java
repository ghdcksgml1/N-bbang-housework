package com.heachi.admin.common.api;

import com.heachi.admin.common.response.JsonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 내부 통신을 위한 WebClient Generator
 */
@Service
@RequiredArgsConstructor
public class HeachiHttpTemplateImpl implements HeachiHttpTemplate {

    private final WebClient webClient;

    // SERVER_URL CONFIG
    @Value("${heachi.admin.server.url}") private static String ADMIN_SERVER;
    @Value("${heachi.notify.server.url}") private static String NOTIFY_SERVER;

    private final static String MEDIA_TYPE = MediaType.APPLICATION_JSON_VALUE;

    /**
     * 단순 [GET] 조회 시
     */
    public <T> JsonResult get(final String URI) {
        return webClient.get()
                .uri(URI)
                .retrieve()
                .bodyToMono(JsonResult.class)
                .block();
    }
}
