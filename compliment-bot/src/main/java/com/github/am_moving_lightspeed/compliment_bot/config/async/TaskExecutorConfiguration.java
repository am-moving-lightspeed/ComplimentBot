package com.github.am_moving_lightspeed.compliment_bot.config.async;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Executors.INTEGRATION_CLIENT_EXECUTOR;

import com.github.am_moving_lightspeed.compliment_bot.config.model.AsyncTaskExecutorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class TaskExecutorConfiguration {

    private final AsyncTaskExecutorsProperties executorsProperties;

    @Bean
    public TaskExecutor integrationClientTaskExecutor() {
        var executorProperties = executorsProperties.get(INTEGRATION_CLIENT_EXECUTOR);
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorProperties.getCorePoolSize());
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        executor.setQueueCapacity(executorProperties.getQueueCapacity());
        return executor;
    }
}
