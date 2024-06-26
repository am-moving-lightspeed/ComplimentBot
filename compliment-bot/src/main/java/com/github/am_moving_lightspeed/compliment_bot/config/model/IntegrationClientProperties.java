package com.github.am_moving_lightspeed.compliment_bot.config.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationClientProperties {

    private String name;
    private String refreshToken;
    private OAuth2 oAuth2;

    @Getter
    @Setter
    public static class OAuth2 {

        private String clientId;
        private String clientSecret;
    }
}
