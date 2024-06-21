package com.github.am_moving_lightspeed.compliment_bot.config.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties("application.async")
public class AsyncTaskExecutorsProperties {

    private List<ExecutorProperties> executor;

    public ExecutorProperties get(String name) {
        return executor.stream()
                       .filter(properties -> properties.getName().equals(name))
                       .findFirst()
                       .orElse(null);
    }

    @Getter
    @Setter
    public static class ExecutorProperties {

        private String name;
        private int corePoolSize;
        private int maxPoolSize;
        private int queueCapacity;
    }
}
