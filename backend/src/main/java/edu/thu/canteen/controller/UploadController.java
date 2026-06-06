package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {
    @PostMapping("/images")
    public ApiResponse<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("empty file");
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String suffix = original.contains(".") ? original.substring(original.lastIndexOf('.')) : ".jpg";
        String filename = UUID.randomUUID() + suffix;
        Path uploadRoot = Path.of("uploads");
        if (!Files.exists(uploadRoot) && Files.exists(Path.of("backend", "uploads"))) {
            uploadRoot = Path.of("backend", "uploads");
        }
        Path dir = uploadRoot.resolve("dishes").toAbsolutePath().normalize();
        Files.createDirectories(dir);
        file.transferTo(dir.resolve(filename));
        return ApiResponse.ok(Map.of("url", "/uploads/dishes/" + filename));
    }
}
