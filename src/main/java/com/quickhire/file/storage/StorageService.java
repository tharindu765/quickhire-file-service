package com.quickhire.file.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadFile(MultipartFile file, String folder) throws IOException;
    void deleteFile(String fileUrl);
    Resource downloadFile(String filePath) throws IOException;
}