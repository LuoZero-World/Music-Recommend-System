package com.llq.service.impl;

import com.llq.dto.TagDTO;
import com.llq.mapper.TagMapper;
import com.llq.service.TagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author 李林麒
 * @Date 2024/4/11
 * @Description
 */
@Slf4j
@Service
public class TagServiceImpl implements TagService {

    @Resource
    TagMapper tagMapper;

    @Override
    public List<TagDTO> getTagsByCategory(int category) {
        return tagMapper.getTagsByCategory(category);
    }

    @Override
    public List<Integer> getLikedTagIDList(int userId) {
        return tagMapper.getLikedTagIDList(userId);
    }

    @Override
    @Transactional
    public boolean updateLikedTagOfUser(int userId, Integer[] tagList) {
        //先删除
        tagMapper.deleteAllLikedTagOfUser(userId);
        //再更新
        for(int tagId : tagList){
            tagMapper.insertLikedTagOfUser(userId, tagId);
        }
        return true;
    }
}
