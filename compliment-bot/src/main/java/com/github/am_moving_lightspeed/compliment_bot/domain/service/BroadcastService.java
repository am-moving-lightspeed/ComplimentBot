package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.DateTime.RIGHT_AFTER_MIDNIGHT;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.DateTime.RIGHT_BEFORE_MIDNIGHT;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_TIME;

import com.github.am_moving_lightspeed.compliment_bot.config.model.BroadcastProperties;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.BotService;
import com.github.am_moving_lightspeed.compliment_bot.persistence.service.StoragePersistenceService;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final BotService botService;
    private final BroadcastProperties properties;
    private final ContentManagerService contentManagerService;
    private final StoragePersistenceService storagePersistenceService;
    private final TaskExecutor broadcastTaskExecutor;
    private final TaskScheduler taskScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleBroadcast() {
        var nextBroadcastTime = getNextBroadcastTime();
        taskScheduler.schedule(this::performBroadcastAndReschedule,
                               nextBroadcastTime.toInstant());
    }

    private ZonedDateTime getNextBroadcastTime() {
        var interval = properties.getInterval();
        var broadcastDateTime = ZonedDateTime.now(UTC).plusSeconds(interval);

        while (isInBlackout(broadcastDateTime)) {
            broadcastDateTime = broadcastDateTime.plusSeconds(interval);
        }
        return broadcastDateTime;
    }

    private boolean isInBlackout(ZonedDateTime broadcastDateTime) {
        var blackoutStart = LocalTime.parse(properties.getBlackout().getStart(), ISO_TIME);
        var blackoutEnd = LocalTime.parse(properties.getBlackout().getEnd(), ISO_TIME);
        var broadcastTime = broadcastDateTime.toLocalTime();

        if (broadcastTime.isAfter(blackoutStart) && broadcastTime.isBefore(RIGHT_BEFORE_MIDNIGHT)) {
            return true;
        } else {
            return broadcastTime.isAfter(RIGHT_AFTER_MIDNIGHT) && broadcastTime.isBefore(blackoutEnd);
        }
    }

    private void performBroadcastAndReschedule() {
        broadcast();
        scheduleBroadcast();
    }

    private void broadcast() {
        var compliment = contentManagerService.fetchNextPendingCompliment();
        contentManagerService.saveComplimentAsUsed(compliment);

        var recipients = storagePersistenceService.getUsers();
        for (var recipient : recipients) {
            broadcastTaskExecutor.execute(() -> {
                var chatId = recipient.getId().toString();
                botService.sendMessage(chatId, compliment.getContent());
            });
        }
    }
}
