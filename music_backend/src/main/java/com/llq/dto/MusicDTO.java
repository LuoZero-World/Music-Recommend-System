package com.llq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 李林麒
 * @Date 2024/4/11
 * @Description
 */
@Data
@AllArgsConstructor
public class MusicDTO {
    int id;
    String musicName;
    String author;
    String duration;
}
