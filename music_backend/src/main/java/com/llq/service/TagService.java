package com.llq.service;

import com.llq.dto.TagDTO;

import java.util.List;

public interface TagService {
    List<TagDTO> getTagsByCategory(int category);

    List<Integer> getLikedTagIDList(int userId);

    boolean updateLikedTagOfUser(int userId, Integer[] tagList);
}
