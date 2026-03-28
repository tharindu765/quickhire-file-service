package com.quickhire.file.service.impl;

import com.quickhire.file.client.UserServiceClient;
import com.quickhire.file.dto.FileUploadResponse;
import com.quickhire.file.exception.FileStorageException;
import com.quickhire.file.service.FileService;
import com.quickhire.file.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final StorageService storageService;
    private final UserServiceClient userServiceClient;

    public FileServiceImpl(StorageService storageService, UserServiceClient userServiceClient) {
        this.storageService = storageService;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public FileUploadResponse uploadResume(MultipartFile file, String userId) throws IOException {
        // Verify user exists
        try {
            userServiceClient.getUserById(Long.parseLong(userId));
        } catch (Exception e) {
            throw new FileStorageException("User not found with id: " + userId);
        }

        validateFile(file, List.of(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        ));
        String url = storageService.uploadFile(file, "resumes/" + userId);
        return FileUploadResponse.builder()
                .url(url)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
    }

    @Override
    public FileUploadResponse uploadLogo(MultipartFile file, String companyId) throws IOException {
        // verify user exists
        try {
            userServiceClient.getUserById(Long.parseLong(companyId));
        } catch (Exception e) {
            throw new FileStorageException("User not found with id: " + companyId);
        }

        validateFile(file, List.of("image/jpeg", "image/png", "image/webp"));
        String url = storageService.uploadFile(file, "logos/" + companyId);
        return FileUploadResponse.builder()
                .url(url)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
    }

    @Override
    public void deleteFile(String fileUrl) {
        storageService.deleteFile(fileUrl);
    }

    @Override
    public Resource downloadFile(String filePath) throws IOException {
        return storageService.downloadFile(filePath);
    }

    private void validateFile(MultipartFile file, List<String> allowedTypes) {
        if (file.isEmpty())
            throw new FileStorageException("File is empty");
        if (!allowedTypes.contains(file.getContentType()))
            throw new FileStorageException("File type not allowed: " + file.getContentType());
        if (file.getSize() > 10 * 1024 * 1024)
            throw new FileStorageException("File exceeds 10MB limit");
    }
}