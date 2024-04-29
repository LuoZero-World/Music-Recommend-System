package com.llq.controller;

import com.llq.dto.AccountDTO;
import com.llq.dto.MusicDTO;
import com.llq.entity.RestBean;
import com.llq.service.MusicService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Author 李林麒
 * @Date 2024/4/11
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Resource
    MusicService musicService;

    @GetMapping("/hot/{id}")
    public RestBean<List<MusicDTO>> getHotMusicListByTagId(@PathVariable("id")int tagId){
        List<MusicDTO> hotMusics = musicService.getHotMusicsByTagId(tagId);
        if(hotMusics == null || hotMusics.isEmpty()) return RestBean.failure(404, "未找到相关数据");
        return RestBean.success( hotMusics, "信息获取成功");
    }

    @GetMapping("/hot18")
    public RestBean<List<MusicDTO>> getHot18MusicList(){
        List<MusicDTO> hot18Musics = musicService.getHot18Musics();
        if(hot18Musics == null || hot18Musics.isEmpty()) return RestBean.failure(404, "未找到相关数据");
        return RestBean.success( hot18Musics, "信息获取成功");
    }

    @PostMapping("/collectIt")
    public RestBean<String> collectMusic(@RequestParam int userId, @RequestParam int musicId){
        //存入收藏信息
        boolean flag = musicService.addMusicCollection(userId, musicId);
        if(flag) return RestBean.success("收藏成功");
        else return RestBean.failure(500, "收藏失败");
    }

    @PostMapping("/disCollectIt")
    public RestBean<String> disCollectMusic(@RequestParam int userId, @RequestParam int musicId){
        //存入收藏信息
        boolean flag = musicService.removeMusicCollection(userId, musicId);
        if(flag) return RestBean.success("取消收藏成功");
        else return RestBean.failure(500, "取消收藏失败");
    }

    @GetMapping("/all-collect-id")
    public RestBean<Set<Integer>> getCollectedMusicIDSet(@SessionAttribute("account") AccountDTO account){
        int userId = account.getId();
        Set<Integer> musicCollections = musicService.getAllMusicIDCollections(userId);
        if(musicCollections == null || musicCollections.isEmpty()) return RestBean.success("用户不存在收藏数据");
        return RestBean.success(musicCollections, "获取成功");
    }

    @GetMapping("/all-collect-number")
    public RestBean<Integer> getNumberOfCollectedMusic(@SessionAttribute("account") AccountDTO account){
        int userId = account.getId();
        Set<Integer> musicCollections = musicService.getAllMusicIDCollections(userId);
        return RestBean.success(musicCollections.size(), "获取成功");
    }

    @GetMapping("/all-collect-detail")
    public RestBean<List<MusicDTO>> getCollectedMusicList(@SessionAttribute("account") AccountDTO account){
        int userId = account.getId();
        List<MusicDTO> musicCollections = musicService.getAllMusicCollections(userId);
        if(musicCollections.isEmpty()) return RestBean.success("用户数据不存在收藏数据");
        return RestBean.success(musicCollections, "获取成功");
    }

    @GetMapping("/playIt/{id}")
    public RestBean<String> playMusic(@PathVariable("id") int musicId, @SessionAttribute("account") AccountDTO account){
        try {
            musicService.addMusicPlayNum(account.getId(), musicId);
        } catch (RuntimeException e){
            log.error(e.toString());
            return RestBean.failure(500, "+1失败");
        }
        return RestBean.success("+1成功");
    }

}
