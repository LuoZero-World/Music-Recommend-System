package com.llq.service;

import com.llq.dto.MusicDTO;
import com.llq.entity.AccountRating;
import com.llq.entity.tempIDStore;

import java.util.List;
import java.util.Set;

public interface MusicService {
    boolean addMusicCollection(int userId, int musicId);

    boolean removeMusicCollection(int userId, int musicId);

    Set<Integer> getAllMusicIDCollections(int userId);

    List<MusicDTO> getAllMusicCollections(int userId);

    void addMusicPlayNum(int userId, int musicId);

    List<MusicDTO> getHot18Musics();

    List<MusicDTO> getHotMusicsByTagId(int tagId);

    void addMusic(String name, String artist, String duration, int[] tag, tempIDStore store);

    List<AccountRating> getAccountRatingList();

    List<MusicDTO> getRecommendMusicList(int userId);
}
