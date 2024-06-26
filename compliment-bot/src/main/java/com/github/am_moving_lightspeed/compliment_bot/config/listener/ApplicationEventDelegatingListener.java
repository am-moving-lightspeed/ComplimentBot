package com.github.am_moving_lightspeed.compliment_bot.config.listener;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserSubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserUnsubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.BackupService;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.ContentLoaderService;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.session.BotSessionRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventDelegatingListener {

    private final BackupService backupService;
    private final BotSessionRunner botSessionRunner;
    private final ContentLoaderService contentLoaderService;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        contentLoaderService.loadContent();
        backupService.scheduleBackup();
        botSessionRunner.run();
    }

    @Async
    @EventListener({
        UserSubscribedEvent.class,
        UserUnsubscribedEvent.class
    })
    public void onUserEvent() {
        backupService.performBackup();
    }
}
