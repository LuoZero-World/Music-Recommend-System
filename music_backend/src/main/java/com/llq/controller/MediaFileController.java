package com.llq.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Objects;

/**
 * @Author 李林麒
 * @Date 2024/5/3
 * @Description 用于返回给前端MP3、图片格式的数据
 */
@RestController
@RequestMapping("/api/media")
public class MediaFileController {
    @PostMapping("/music")
    public ResponseEntity<Resource> downloadMusic(@RequestParam String musicName) {
        InputStream inputStream = MediaFileController.class.getResourceAsStream("/music/" + musicName + ".mp3");
        Resource resource = new InputStreamResource(Objects.requireNonNull(inputStream));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }

    @PostMapping("/image")
    public ResponseEntity<Resource> downloadImage(@RequestParam String imageName) {
        InputStream inputStream = MediaFileController.class.getResourceAsStream("/music/" + imageName + ".mp3");
        Resource resource = new InputStreamResource(Objects.requireNonNull(inputStream));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                .body(resource);
    }
}
