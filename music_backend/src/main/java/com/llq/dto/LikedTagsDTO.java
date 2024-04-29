package com.llq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author 李林麒
 * @Date 2024/4/13
 * @Description 用户喜好标签列表
 */
@Data
@AllArgsConstructor
public class LikedTagsDTO {
    int userId;
    List<Integer> likedTagsId;
}
