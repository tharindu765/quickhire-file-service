package com.quickhire.file.controller;

import com.quickhire.file.dto.FileUploadResponse;
import com.quickhire.file.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/resume/upload")
    public ResponseEntity<FileUploadResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) throws IOException {
        return ResponseEntity.ok(fileService.uploadResume(file, userId));
    }

    @PostMapping("/logo/upload")
    public ResponseEntity<FileUploadResponse> uploadLogo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("companyId") String companyId) throws IOException {
        return ResponseEntity.ok(fileService.uploadLogo(file, companyId));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath)
            throws IOException {
        Resource resource = fileService.downloadFile(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestParam("url") String fileUrl) {
        fileService.deleteFile(fileUrl);
        return ResponseEntity.noContent().build();
    }
}