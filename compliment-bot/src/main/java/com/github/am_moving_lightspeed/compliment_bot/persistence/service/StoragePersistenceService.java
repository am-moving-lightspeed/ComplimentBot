package com.github.am_moving_lightspeed.compliment_bot.persistence.service;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_FLUSH_TO_STORAGE;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_OBTAIN_STORAGE_FILE_DESCRIPTOR;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_READ_FROM_STORAGE;
import static java.util.stream.Collectors.toSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.ServiceException;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.ComplimentDao;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.StorageDao;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.UserDao;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StoragePersistenceService {

    private final Converter<Compliment, ComplimentDao> complimentConverter;
    private final Converter<User, UserDao> userConverter;
    private final Path storageLocation;
    private final ObjectMapper objectMapper;

    public StoragePersistenceService(@Value("${application.storage.location}") String storageLocation,
                                     Converter<Compliment, ComplimentDao> complimentConverter,
                                     Converter<User, UserDao> userConverter,
                                     ObjectMapper objectMapper) {
        this.complimentConverter = complimentConverter;
        this.userConverter = userConverter;
        this.objectMapper = objectMapper;
        this.storageLocation = Path.of(storageLocation);
    }

    public boolean hasContent() {
        var storage = getStorage();
        return !storage.getPendingCompliments().isEmpty()
            || !storage.getUsedComplimentsHashes().isEmpty();
    }

    public Set<Compliment> getCompliments() {
        var storage = getStorage();
        return storage.getPendingCompliments().stream()
                      .map(complimentConverter::reverseConvert)
                      .collect(toSet());
    }

    public void saveCompliments(Set<Compliment> compliments) {
        var storage = getStorage();
        var used = storage.getUsedComplimentsHashes();
        var newContent = compliments.stream()
                                    .map(complimentConverter::convert)
                                    .filter(complimentDao -> !used.contains(complimentDao.getHash()))
                                    .collect(toSet());
        storage.getPendingCompliments().addAll(newContent);
        flushStorage(storage);
    }

    public Set<Integer> getUsedComplimentsHashes() {
        return getStorage().getUsedComplimentsHashes();
    }

    public void saveUsedComplimentsHashes(Set<Integer> usedComplimentsHashes) {
        var storage = getStorage();
        storage.setUsedComplimentsHashes(usedComplimentsHashes);
        flushStorage(storage);
    }

    public void clearUsedComplimentsHashes() {
        var storage = getStorage();
        storage.getUsedComplimentsHashes().clear();
        flushStorage(storage);
    }

    public Set<User> getUsers() {
        return getStorage().getUsers().stream()
                           .map(userConverter::reverseConvert)
                           .collect(toSet());
    }

    public void saveUser(User user) {
        var userDao = userConverter.convert(user);
        var storage = getStorage();
        storage.getUsers().add(userDao);
        flushStorage(storage);
    }

    public void removeUser(User user) {
        var userDao = userConverter.convert(user);
        var storage = getStorage();
        storage.getUsers().remove(userDao);
        flushStorage(storage);
    }

    public File getStorageFile(boolean rethrowException) {
        try {
            return getStorageFile();
        } catch (IOException exception) {
            log.error("Failed to obtain storage file descriptor. Nested exception", exception);

            if (rethrowException) {
                throw new ServiceException(FAILED_TO_OBTAIN_STORAGE_FILE_DESCRIPTOR, exception);
            } else {
                return null;
            }
        }
    }

    private StorageDao getStorage() {
        try {
            var storageFile = getStorageFile();
            return objectMapper.readValue(storageFile, StorageDao.class);
        } catch (IOException exception) {
            log.error("Failed to read from storage. Nested exception:", exception);
            throw new ServiceException(FAILED_TO_READ_FROM_STORAGE, exception);
        }
    }

    private void flushStorage(StorageDao storage) {
        try {
            var storageFile = getStorageFile();
            objectMapper.writeValue(storageFile, storage);
        } catch (IOException exception) {
            log.error("Failed to flush content to storage. Nested exception:", exception);
            throw new ServiceException(FAILED_TO_FLUSH_TO_STORAGE, exception);
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
            objectMapper.writeValue(storageFile, new StorageDao());
        }
        return storageFile;
    }
}
