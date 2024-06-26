package com.github.am_moving_lightspeed.compliment_bot.config.model;

import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("application.storage")
public class StorageProperties {

    private String backupTime;
    private String cacheLocation;
    private String cacheFileName;

    public Path getCacheLocationPath() {
        return Path.of(cacheLocation);
    }
}
