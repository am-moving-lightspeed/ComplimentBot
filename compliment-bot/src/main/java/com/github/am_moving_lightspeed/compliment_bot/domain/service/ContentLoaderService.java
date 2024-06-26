package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toSet;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.integration.service.ContentSupplierService;
import com.github.am_moving_lightspeed.compliment_bot.integration.service.DropboxService;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentLoaderService {

    private final ContentCacheService contentCacheService;
    private final DropboxService dropboxService;
    private final List<ContentSupplierService> contentSupplierServices;
    private final TaskExecutor integrationClientTaskExecutor;

    public void loadContent() {
        var cacheFile = contentCacheService.getCachedFileDescriptor();
        if (dropboxService.downloadFile(cacheFile)) {
            return;
        }

        log.info("Started content loading at {}", LocalDateTime.now());

        var content = contentSupplierServices.stream()
                                             .map(service -> performAsync(service::getContent))
                                             .map(CompletableFuture::join)
                                             .flatMap(Collection::stream)
                                             .collect(toSet());
        contentCacheService.cacheCompliments(content);

        log.info("Content loaded at {}. Total entries: {}", LocalDateTime.now(), content.size());
    }

    private CompletableFuture<Set<Compliment>> performAsync(Supplier<Set<Compliment>> supplier) {
        return supplyAsync(supplier, integrationClientTaskExecutor).exceptionally(this::handleError);
    }

    private Set<Compliment> handleError(Throwable throwable) {
        log.error("Failed to fetch content", throwable);
        return Set.of();
    }
}
