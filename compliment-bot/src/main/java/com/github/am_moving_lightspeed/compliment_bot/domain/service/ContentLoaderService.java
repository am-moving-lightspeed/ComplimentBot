package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toSet;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.integration.service.ContentSupplierService;
import com.github.am_moving_lightspeed.compliment_bot.persistence.service.StoragePersistenceService;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentLoaderService {

    private final List<ContentSupplierService> contentSupplierServices;
    private final StoragePersistenceService storagePersistenceService;
    private final TaskExecutor integrationClientTaskExecutor;

    @EventListener(ApplicationReadyEvent.class)
    public void loadContent() {
        if (storagePersistenceService.hasContent()) {
            return;
        }

        log.info("Started content loading at {}", LocalDateTime.now());

        var content = contentSupplierServices.stream()
                                             .map(service -> performAsync(service::getContent))
                                             .map(CompletableFuture::join)
                                             .flatMap(Collection::stream)
                                             .collect(toSet());
        storagePersistenceService.saveCompliments(content);

        log.info("Content loaded at {}. Total entries: {}", LocalDateTime.now(), content.size());
    }

    private CompletableFuture<Set<Compliment>> performAsync(Supplier<Set<Compliment>> supplier) {
        return supplyAsync(supplier, integrationClientTaskExecutor).exceptionally(this::handleError);
    }

    private Set<Compliment> handleError(Throwable throwable) {
        log.error("Failed to fetch content. Nested exception:", throwable);
        return Set.of();
    }
}
