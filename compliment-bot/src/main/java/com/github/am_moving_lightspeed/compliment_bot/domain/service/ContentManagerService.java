package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentManagerService {

    private final ContentCacheService contentCacheService;

    public Compliment fetchNextPendingCompliment() {
        var nextPendingCompliment = getNextPendingCompliment();

        if (nextPendingCompliment == null) {
            contentCacheService.clearUsedComplimentsHashes();
            nextPendingCompliment = getNextPendingCompliment();
        }
        saveComplimentAsUsed(nextPendingCompliment);
        return nextPendingCompliment;
    }

    public void saveComplimentAsUsed(Compliment compliment) {
        var usedComplimentsHashes = contentCacheService.getUsedComplimentsHashes();
        usedComplimentsHashes.add(compliment.hashCode());
        contentCacheService.cacheUsedComplimentsHashes(usedComplimentsHashes);
    }

    private Compliment getNextPendingCompliment() {
        var usedComplimentsHashes = contentCacheService.getUsedComplimentsHashes();
        return contentCacheService.getCompliments().stream()
                                  .filter(compliment -> notUsed(compliment, usedComplimentsHashes))
                                  .findFirst()
                                  .orElse(null);
    }

    private boolean notUsed(Compliment compliment, Set<Integer> usedComplimentsHashes) {
        return !usedComplimentsHashes.contains(compliment.hashCode());
    }
}
