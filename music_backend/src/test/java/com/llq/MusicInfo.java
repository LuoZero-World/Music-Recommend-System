package com.llq;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 李林麒
 * @Date 2024/4/14
 * @Description
 */
@Data
@AllArgsConstructor
public class MusicInfo {
    String name;
    String artist;
    long rid;
    String pic;
}
