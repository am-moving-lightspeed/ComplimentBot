package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.persistence.service.StoragePersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentManagerService {

    private final StoragePersistenceService storagePersistenceService;

    public Compliment fetchNextPendingCompliment() {
        var nextPendingCompliment = getNextPendingCompliment();

        if (nextPendingCompliment == null) {
            storagePersistenceService.clearUsedComplimentsHashes();
            nextPendingCompliment = getNextPendingCompliment();
        }
        return nextPendingCompliment;
    }

    public void saveComplimentAsUsed(Compliment compliment) {
        var usedComplimentsHashes = storagePersistenceService.getUsedComplimentsHashes();
        usedComplimentsHashes.add(compliment.hashCode());
        storagePersistenceService.saveUsedComplimentsHashes(usedComplimentsHashes);
    }

    private Compliment getNextPendingCompliment() {
        var usedComplimentsHashes = storagePersistenceService.getUsedComplimentsHashes();
        return storagePersistenceService
            .getCompliments().stream()
            .filter(pendingCompliment -> !usedComplimentsHashes.contains(pendingCompliment.hashCode()))
            .findFirst()
            .orElse(null);
    }
}
