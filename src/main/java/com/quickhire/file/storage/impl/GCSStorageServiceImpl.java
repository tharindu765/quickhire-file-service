package com.quickhire.file.storage.impl;

import com.quickhire.file.storage.StorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@ConditionalOnProperty(name = "storage.provider", havingValue = "gcs")
public class GCSStorageServiceImpl implements StorageService {

    // TODO: inject Storage bean when GCS is enabled
    // @Value("${spring.cloud.gcp.storage.bucket}")
    // private String bucketName;
    // private final Storage storage;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        throw new UnsupportedOperationException("GCS not yet configured");
    }

    @Override
    public void deleteFile(String fileUrl) {
        throw new UnsupportedOperationException("GCS not yet configured");
    }

    @Override
    public Resource downloadFile(String filePath) throws IOException {
        throw new UnsupportedOperationException("GCS not yet configured");
    }
}