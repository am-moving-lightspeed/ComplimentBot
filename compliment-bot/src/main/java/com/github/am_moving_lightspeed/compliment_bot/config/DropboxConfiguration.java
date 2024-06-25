package com.github.am_moving_lightspeed.compliment_bot.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.github.am_moving_lightspeed.compliment_bot.config.model.IntegrationClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "application.integration.client.dropbox")
    public IntegrationClientProperties dropboxClientProperties() {
        return new IntegrationClientProperties();
    }

    @Bean
    public DbxClientV2 dropboxClient(IntegrationClientProperties dropboxClientProperties) {
        var config = DbxRequestConfig.newBuilder(dropboxClientProperties.getName())
                                     .build();
        return new DbxClientV2(config, dropboxClientProperties.getAccessToken());
    }
}
