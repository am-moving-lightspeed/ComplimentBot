package com.github.am_moving_lightspeed.compliment_bot.domain.service;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_CACHE;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_OBTAIN_CACHED_FILE_DESCRIPTOR;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_READ_FROM_CACHE;
import static java.util.stream.Collectors.toSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.am_moving_lightspeed.compliment_bot.config.model.StorageProperties;
import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Content;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.ComplimentCache;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.ContentCache;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.UserCache;
import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.ServiceException;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContentCacheService {

    private final Converter<Content, ContentCache> contentConverter;
    private final Converter<Compliment, ComplimentCache> complimentConverter;
    private final Converter<User, UserCache> userConverter;
    private final ObjectMapper objectMapper;
    private final StorageProperties storageProperties;

    private final Lock readLock;
    private final Lock writeLock;

    {
        var readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public ContentCacheService(Converter<Content, ContentCache> contentConverter,
                               Converter<Compliment, ComplimentCache> complimentConverter,
                               Converter<User, UserCache> userConverter,
                               ObjectMapper objectMapper,
                               StorageProperties storageProperties) {
        this.contentConverter = contentConverter;
        this.complimentConverter = complimentConverter;
        this.userConverter = userConverter;
        this.objectMapper = objectMapper;
        this.storageProperties = storageProperties;
    }

    public boolean hasCachedContent() {
        return !getCachedContent().getPendingCompliments().isEmpty();
    }

    public Set<Compliment> getCompliments() {
        return getCachedContent().getPendingCompliments().stream()
                                 .map(complimentConverter::convertBack)
                                 .collect(toSet());
    }

    public void cacheCompliments(Set<Compliment> compliments) {
        var cachedContent = getCachedContent();
        var complimentCaches = compliments.stream()
                                          .map(complimentConverter::convert)
                                          .collect(toSet());
        cachedContent.getPendingCompliments()
                     .addAll(complimentCaches);
        cache(cachedContent);
    }

    public Set<Integer> getUsedComplimentsHashes() {
        return getCachedContent().getUsedComplimentsHashes();
    }

    public void cacheUsedComplimentsHashes(Set<Integer> usedComplimentsHashes) {
        var cachedContent = getCachedContent();
        cachedContent.getUsedComplimentsHashes()
                     .addAll(usedComplimentsHashes);
        cache(cachedContent);
    }

    public void clearUsedComplimentsHashes() {
        var cachedContent = getCachedContent();
        cachedContent.getUsedComplimentsHashes().clear();
        cache(cachedContent);
    }

    public Set<User> getUsers() {
        return getCachedContent().getUsers().stream()
                                 .map(userConverter::convertBack)
                                 .collect(toSet());
    }

    public void cacheUser(User user) {
        var userCache = userConverter.convert(user);
        var cachedContent = getCachedContent();

        cachedContent.getUsers().add(userCache);
        cache(cachedContent);
    }

    public void removeUser(User user) {
        var userCache = userConverter.convert(user);
        var cachedContent = getCachedContent();

        cachedContent.getUsers().remove(userCache);
        cache(cachedContent);
    }

    public void cache(Content content) {
        var contentCache = contentConverter.convert(content);
        cache(contentCache);
    }

    // TODO remove
    public File getCachedFileDescriptor() {
        readLock.lock();
        try {
            return getCacheFile();
        } catch (IOException exception) {
            throw new ServiceException(FAILED_TO_OBTAIN_CACHED_FILE_DESCRIPTOR, exception);
        } finally {
            readLock.unlock();
        }
    }

    private ContentCache getCachedContent() {
        readLock.lock();
        try {
            var cacheFile = getCacheFile();
            return objectMapper.readValue(cacheFile, ContentCache.class);
        } catch (IOException exception) {
            throw new ServiceException(FAILED_TO_READ_FROM_CACHE, exception);
        } finally {
            readLock.unlock();
        }
    }

    private void cache(ContentCache storage) {
        writeLock.lock();
        try {
            var cacheFile = getCacheFile();
            objectMapper.writeValue(cacheFile, storage);
        } catch (IOException exception) {
            throw new ServiceException(FAILED_TO_CACHE, exception);
        } finally {
            writeLock.unlock();
        }
    }

    private File getCacheFile() throws IOException {
        var cacheFile = storageProperties.getCacheLocationPath().toFile();
        if (!cacheFile.exists()) {
            var parentFile = cacheFile.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            cacheFile.createNewFile();
            objectMapper.writeValue(cacheFile, new ContentCache());
        }
        return cacheFile;
    }
}
