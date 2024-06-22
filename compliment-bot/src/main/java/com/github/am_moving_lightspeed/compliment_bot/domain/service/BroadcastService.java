package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import static java.time.LocalTime.MIDNIGHT;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_TIME;

import com.github.am_moving_lightspeed.compliment_bot.config.model.BroadcastProperties;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.BotService;
import com.github.am_moving_lightspeed.compliment_bot.persistence.service.StoragePersistenceService;
import java.time.LocalDate;
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
        var initialTime = ZonedDateTime.of(LocalDate.now(),
                                           LocalTime.parse(properties.getInitialTime(), ISO_TIME),
                                           UTC);
        var calculatedBroadcastTime = ZonedDateTime.now(UTC).isAfter(initialTime)
            ? initialTime.plusSeconds(properties.getInterval())
            : initialTime;
        taskScheduler.schedule(() -> performBroadcastAndReschedule(calculatedBroadcastTime),
                               calculatedBroadcastTime.toInstant());
    }

    private void performBroadcastAndReschedule(ZonedDateTime broadcastDateTime) {
        broadcast();
        rescheduleBroadcast(broadcastDateTime);
    }

    private void rescheduleBroadcast(ZonedDateTime previousBroadcastDateTime) {
        var interval = properties.getInterval();
        var nextBroadcastTime = previousBroadcastDateTime.plusSeconds(interval);

        while (isInBlackout(nextBroadcastTime)) {
            nextBroadcastTime = nextBroadcastTime.plusSeconds(interval);
        }
        var calculatedBroadcastTime = nextBroadcastTime;
        taskScheduler.schedule(() -> performBroadcastAndReschedule(calculatedBroadcastTime),
                               calculatedBroadcastTime.toInstant());
    }

    private boolean isInBlackout(ZonedDateTime broadcastDateTime) {
        var blackoutStart = LocalTime.parse(properties.getBlackout().getStart(), ISO_TIME);
        var blackoutEnd = LocalTime.parse(properties.getBlackout().getEnd(), ISO_TIME);
        var broadcastTime = broadcastDateTime.toLocalTime();

        if (broadcastTime.isAfter(blackoutStart) && broadcastTime.isBefore(MIDNIGHT)) {
            return true;
        } else {
            return broadcastTime.isAfter(MIDNIGHT) && broadcastTime.isBefore(blackoutEnd);
        }
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
