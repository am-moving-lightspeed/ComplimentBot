package com.github.am_moving_lightspeed.compliment_bot.integration.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfoniacClientWrapper extends BaseClientWrapper {

    @Value("${application.integration.client.infoniac.url}")
    private String uri;
    @Value("${application.integration.client.infoniac.read-timeout}")
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
