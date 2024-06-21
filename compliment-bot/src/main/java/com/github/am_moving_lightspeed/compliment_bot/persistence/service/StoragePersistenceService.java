package com.github.am_moving_lightspeed.compliment_bot.persistence.service;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_READ_FROM_STORAGE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.ServiceException;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.StorageDao;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StoragePersistenceService {

    private final Path storageLocation;
    private final ObjectMapper objectMapper;

    public StoragePersistenceService(@Value("${application.storage.location}") String storageLocation,
                                     ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.storageLocation = Path.of(storageLocation);
    }

    private StorageDao getStorage() {
        try {
            var storageFile = getStorageFile();
            return objectMapper.readValue(storageFile, StorageDao.class);
        } catch (IOException exception) {
            log.error("Failed to from storage. Nested exception:", exception);
            throw new ServiceException(FAILED_TO_READ_FROM_STORAGE, exception);
        }
    }

    private File getStorageFile() throws IOException {
        var storageFile = storageLocation.toFile();
        if (!storageFile.exists()) {
            var parentFile = storageFile.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            storageFile.createNewFile();
        }
        objectMapper.writeValue(storageFile, new StorageDao());
        return storageFile;
    }
}
