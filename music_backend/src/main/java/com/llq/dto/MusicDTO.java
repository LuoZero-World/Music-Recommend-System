package com.llq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 李林麒
 * @Date 2024/4/11
 * @Description
 */
@Data
public class MusicDTO {
    int id;
    String musicName;
    String author;
    String duration;
    String musicURL = "";

    public MusicDTO(int id, String musicName, String author, String duration) {
        this.id = id;
        this.musicName = musicName;
        this.author = author;
        this.duration = duration;
    }
}
