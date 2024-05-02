package com.llq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 李林麒
 * @Date 2024/5/1
 * @Description 存储用户对某首音乐的评分、收藏信息，用于协同过滤的计算
 */
@Data
@AllArgsConstructor
public class AccountRating {
    int userId;
    int musicId;
    int playNum;
    int collected;
}
