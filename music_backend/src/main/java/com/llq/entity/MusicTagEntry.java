package com.llq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 李林麒
 * @Date 2024/5/1
 * @Description
 */
@Data
@AllArgsConstructor
public class MusicTagEntry {
    int musicId;
    String tagIds;
}
