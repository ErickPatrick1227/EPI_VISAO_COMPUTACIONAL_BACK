package tech.safevision.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {

    @GetMapping("/frame/{cameraId}")
    public ResponseEntity<Resource> frameAtual(
            @PathVariable String cameraId) {

        if (!cameraId.matches("camera\\d+")) {
            return ResponseEntity.badRequest().build();
        }


        File file = new File(
                "C:/Users/Sarah/Desktop/EPI-Visão Computacional/data/models/src/evidencias/"
                        + cameraId + ".jpg"
        );

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}