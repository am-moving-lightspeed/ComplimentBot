package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_TIME;

import com.github.am_moving_lightspeed.compliment_bot.config.model.StorageProperties;
import com.github.am_moving_lightspeed.compliment_bot.integration.service.DropboxService;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService {

    private final ContentCacheService contentCacheService;
    private final DropboxService dropboxService;
    private final StorageProperties storageProperties;
    private final TaskScheduler taskScheduler;

    @PreDestroy
    public void onShutdown() {
        try {
            performBackup();
        } catch (Exception exception) {
            log.error("Failed to perform back-up on application shutdown", exception);
            // Swallowing the exception not to prevent normal application shutdown
        }
    }

    public void scheduleBackup() {
        var backupTime = LocalTime.parse(storageProperties.getBackupTime(), ISO_TIME);
        var backupDateTime = ZonedDateTime.of(LocalDate.now(), backupTime, UTC);

        if (ZonedDateTime.now(UTC).isAfter(backupDateTime)) {
            backupDateTime = backupDateTime.plusDays(1);
        }

        taskScheduler.scheduleAtFixedRate(this::performBackup,
                                          backupDateTime.toInstant(),
                                          Duration.ofDays(1));
    }

    public void performBackup() {
        var backupFile = contentCacheService.getCachedFileDescriptor();
        dropboxService.uploadFile(backupFile);
    }
}
