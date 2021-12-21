package pl.java.homebudget.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.java.homebudget.service.uploader.UploadService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    private void uploadFile(@RequestParam("file") MultipartFile file) {
        uploadService.uploadFile(file);
    }
}
