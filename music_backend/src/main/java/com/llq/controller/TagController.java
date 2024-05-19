package com.llq.controller;

import com.llq.dto.AccountDTO;
import com.llq.dto.TagDTO;
import com.llq.entity.RestBean;
import com.llq.service.TagService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 李林麒
 * @Date 2024/4/11
 * @Description tag
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Resource
    TagService tagService;

    @GetMapping("/{category}")
    public RestBean<List<TagDTO>> getTagList(@PathVariable("category") int category){
        List<TagDTO> tagsByCategory = tagService.getTagsByCategory(category);
        if(tagsByCategory == null || tagsByCategory.isEmpty()) return RestBean.failure(404, "未找到相关数据");
        return RestBean.success(tagsByCategory, "信息获取成功");
    }

    @GetMapping("/all-likedTag-id")
    public RestBean<List<Integer>> getLikedTagIDList(@SessionAttribute("account") AccountDTO account){
        int userId = account.getId();
        List<Integer> likedTagList = tagService.getLikedTagIDList(userId);
        if(likedTagList == null || likedTagList.isEmpty()) return RestBean.success("用户不存在喜好标签");
        return RestBean.success(likedTagList, "用户喜好标签获取成功");
    }

    @GetMapping("/has-likedTag-id")
    public RestBean<Boolean> hasLikedTag(@SessionAttribute("account") AccountDTO account){
        int userId = account.getId();
        List<Integer> likedTagList = tagService.getLikedTagIDList(userId);
        if(likedTagList == null || likedTagList.isEmpty()) return RestBean.success(Boolean.FALSE, "用户无喜好标签");
        return RestBean.success(Boolean.TRUE, "");
    }

    @PostMapping("/update-all-likedTag")
    public RestBean<String> updateLikedTagOfUser(@SessionAttribute("account") AccountDTO account,
                                                 @RequestParam(value = "tagList[]", required = false)Integer[] tagList){
        int userId = account.getId();
        if(tagService.updateLikedTagOfUser(userId, tagList)) return RestBean.success("修改成功");
        return RestBean.failure(500, "修改失败");
    }
}
