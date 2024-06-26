package com.github.am_moving_lightspeed.compliment_bot.integration.service;

import static com.dropbox.core.v2.files.WriteMode.OVERWRITE;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_DOWNLOAD_SAVED_CONTENT;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_UPLOAD_BACKUP_CONTENT;
import static java.lang.String.format;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.github.am_moving_lightspeed.compliment_bot.config.model.StorageProperties;
import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.ServiceException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DropboxService {

    private final DbxClientV2 dropboxClient;
    private final StorageProperties storageProperties;

    private final String contentPath;
    private final String rootDir;

    public DropboxService(@Value("${application.integration.dropbox.root-dir}") String rootDir,
                          DbxClientV2 dropboxClient,
                          StorageProperties storageProperties) {
        this.dropboxClient = dropboxClient;
        this.storageProperties = storageProperties;
        this.rootDir = rootDir;

        this.contentPath = format("%s/%s", rootDir, storageProperties.getCacheFileName());
    }

    public boolean downloadFile(File file) {
        try (var outputStream = new FileOutputStream(file)) {
            var files = dropboxClient.files();
            var folderContent = files.listFolder(rootDir);
            var fileExists = folderContent.getEntries().stream()
                                          .map(Metadata::getName)
                                          .anyMatch(name -> storageProperties.getCacheFileName().equals(name));
            if (!fileExists) {
                return false;
            }
            files.downloadBuilder(contentPath)
                 .download(outputStream);
            return true;
        } catch (DbxException | IOException exception) {
            throw new ServiceException(FAILED_TO_DOWNLOAD_SAVED_CONTENT, exception);
        }
    }

    public void uploadFile(File file) {
        try (var inputStream = new FileInputStream(file)) {
            dropboxClient.files()
                         .uploadBuilder(contentPath)
                         .withMode(OVERWRITE)
                         .uploadAndFinish(inputStream);
        } catch (DbxException | IOException exception) {
            throw new ServiceException(FAILED_TO_UPLOAD_BACKUP_CONTENT, exception);
        }
    }
}
