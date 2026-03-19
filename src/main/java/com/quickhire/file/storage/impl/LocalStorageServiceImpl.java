package com.quickhire.file.storage.impl;

import com.quickhire.file.exception.FileNotFoundException;
import com.quickhire.file.exception.FileStorageException;
import com.quickhire.file.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "storage.provider", havingValue = "local", matchIfMissing = true)
public class LocalStorageServiceImpl implements StorageService {

    @Value("${storage.local.base-path:uploads}")
    private String basePath;

    @Value("${storage.local.base-url:http://localhost:8083/files}")
    private String baseUrl;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        try {
            Path uploadDir = Paths.get(basePath, folder);
            Files.createDirectories(uploadDir);

            String fileName = UUID.randomUUID() + "_" +
                    StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/" + folder + "/" + fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String relativePath = fileUrl.replace(baseUrl + "/", "");
            Path path = Paths.get(basePath, relativePath);
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to delete file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Resource downloadFile(String filePath) throws IOException {
        String relativePath = filePath.replace(baseUrl + "/", "");
        Path path = Paths.get(basePath).resolve(relativePath).normalize();
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        return resource;
    }
}