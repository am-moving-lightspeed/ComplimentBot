package com.github.am_moving_lightspeed.compliment_bot.config.async;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Executors.BROADCAST;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Executors.INTEGRATION_CLIENT_EXECUTOR;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Executors.SCHEDULER;

import com.github.am_moving_lightspeed.compliment_bot.config.model.AsyncTaskExecutorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration(proxyBeanMethods = false)
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

    @Bean
    public TaskExecutor broadcastTaskExecutor() {
        var executorProperties = executorsProperties.get(BROADCAST);
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorProperties.getCorePoolSize());
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize());
        executor.setQueueCapacity(executorProperties.getQueueCapacity());
        return executor;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        var schedulerProperties = executorsProperties.get(SCHEDULER);
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(schedulerProperties.getCorePoolSize());
        return scheduler;
    }
}
