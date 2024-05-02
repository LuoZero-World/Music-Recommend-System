package com.llq.service;

import com.llq.dto.TagDTO;

import java.util.List;
import java.util.Map;

public interface TagService {
    List<TagDTO> getTagsByCategory(int category);

    List<Integer> getLikedTagIDList(int userId);

    Map<Integer, List<Integer>> getMusicTagIDMap();

    boolean updateLikedTagOfUser(int userId, Integer[] tagList);
}
