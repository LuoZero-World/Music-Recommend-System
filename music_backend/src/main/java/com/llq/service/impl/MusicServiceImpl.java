package com.llq.service.impl;

import com.llq.algorithm.CollaborativeUtil;
import com.llq.dto.MusicDTO;
import com.llq.entity.AccountRating;
import com.llq.entity.tempIDStore;
import com.llq.mapper.MusicMapper;
import com.llq.service.MusicService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author 李林麒
 * @Date 2024/4/11
 * @Description
 */
@Slf4j
@Service
public class MusicServiceImpl implements MusicService {

    @Resource
    MusicMapper musicMapper;

    @Resource @Lazy
    CollaborativeUtil collaborativeUtil;

    @Override
    public List<MusicDTO> getHot18Musics() {
        return musicMapper.getHot18Musics();
    }

    @Override
    public List<MusicDTO> getHotMusicsByTagId(int tagId) {
        return musicMapper.getHotMusicsByTagId(tagId);
    }

    @Override
    @Transactional
    public void addMusic(String name, String artist, String duration, int[] tag, tempIDStore store) {
        musicMapper.addMusic(name, artist, duration, store);
        for(int tagId : tag) musicMapper.addMusicType(store.getId(), tagId);
    }

    @Override
    public List<AccountRating> getAccountRatingList() {
        return musicMapper.getAllAccountRating();
    }

    @Override
    public List<MusicDTO> getRecommendMusicList(int userId) {
        return collaborativeUtil.getRecommendations(userId).keySet().stream()
                .map(musicId ->  musicMapper.getMusicByID(musicId))
                .toList();
    }

    @Override
    public boolean addMusicCollection(int userId, int musicId) {
        try{
            musicMapper.addMusicCollection(userId, musicId);
        } catch (RuntimeException e){
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeMusicCollection(int userId, int musicId) {
        try{
            musicMapper.removeMusicCollection(userId, musicId);
        } catch (RuntimeException e){
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Set<Integer> getAllMusicIDCollections(int userId) {
        return musicMapper.getAllMusicIDCollections(userId);
    }

    @Override
    public List<MusicDTO> getAllMusicCollections(int userId) {
        Set<Integer> musicIDCollections = getAllMusicIDCollections(userId);
        if(musicIDCollections == null || musicIDCollections.isEmpty()) return Collections.emptyList();

        return musicIDCollections.stream()
                .map(musicId ->  musicMapper.getMusicByID(musicId))
                .toList();
    }

    @Override
    @Transactional
    public void addMusicPlayNum(int userId, int musicId) {
        musicMapper.addMusicPlayNum(musicId);
        musicMapper.updatePlayingRecordElseInsert(userId, musicId);
    }
}
