package com.llq.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @Author 李林麒
 * @Date 2024/5/3
 * @Description 用于返回给前端MP3、图片格式的数据
 */
@RestController
@RequestMapping("/api/media")
public class MediaFileController {
    @PostMapping("/music")
    public ResponseEntity<Resource> downloadMusic(@RequestParam String musicName) throws IOException {
        String path = this.getClass().getResource("/music").getPath() + "/"+musicName+".mp3";
        Resource resource = new InputStreamResource(new FileInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }

    @PostMapping("/image")
    public ResponseEntity<Resource> downloadImage(@RequestParam String imageName) throws FileNotFoundException {
        String path = this.getClass().getResource("/image").getPath() + "/"+imageName+".mp3";
        Resource resource = new InputStreamResource(new FileInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                .body(resource);
    }
}
