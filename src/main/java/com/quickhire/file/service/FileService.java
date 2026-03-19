package com.quickhire.file.service;

import com.quickhire.file.dto.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileUploadResponse uploadResume(MultipartFile file, String userId) throws IOException;
    FileUploadResponse uploadLogo(MultipartFile file, String companyId) throws IOException;
    void deleteFile(String fileUrl);
    Resource downloadFile(String filePath) throws IOException;
}