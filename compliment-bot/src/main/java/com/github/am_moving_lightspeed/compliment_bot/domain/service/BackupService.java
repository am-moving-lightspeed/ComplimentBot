package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_TIME;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserSubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.event.UserUnsubscribedEvent;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.BotService;
import com.github.am_moving_lightspeed.compliment_bot.persistence.service.StoragePersistenceService;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService {

    @Value("${application.storage.backup-time}")
    private final String backupTime;

    private final BotService botService;
    private final StoragePersistenceService storagePersistenceService;
    private final TaskScheduler taskScheduler;

    @PreDestroy
    public void onShutdown() {
        var backupFile = storagePersistenceService.getStorageFile(false);
        if (backupFile != null) {
            botService.sendDocument(backupFile);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleBackup() {
        var backupDateTime = ZonedDateTime.of(LocalDate.now(),
                                              LocalTime.parse(backupTime, ISO_TIME),
                                              UTC);
        if (ZonedDateTime.now(UTC).isAfter(backupDateTime)) {
            backupDateTime = backupDateTime.plusDays(1);
        }

        var backupFile = storagePersistenceService.getStorageFile(true);
        taskScheduler.scheduleAtFixedRate(() -> botService.sendDocument(backupFile),
                                          backupDateTime.toInstant(),
                                          Duration.ofDays(1));
    }

    @EventListener({UserSubscribedEvent.class, UserUnsubscribedEvent.class})
    public void sendBackupFile() {
        var backupFile = storagePersistenceService.getStorageFile(true);
        botService.sendDocument(backupFile);
    }
}
