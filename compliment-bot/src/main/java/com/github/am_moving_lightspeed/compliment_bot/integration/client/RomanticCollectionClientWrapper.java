package com.github.am_moving_lightspeed.compliment_bot.integration.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RomanticCollectionClientWrapper extends BaseClientWrapper {

    @Value("${application.integration.client.romantic-collection.url}")
    private String uri;
    @Value("${application.integration.client.romantic-collection.read-timeout}")
    private int readTimeout;

    @Override
    protected String getUri() {
        return uri;
    }

    @Override
    protected int getReadTimeout() {
        return readTimeout;
    }
}
