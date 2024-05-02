package com.llq.algorithm;

import com.llq.service.MusicService;
import com.llq.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 李林麒
 * @Date 2024/4/17
 * @Description 基于内容的推荐算法
 */
@Component
public class ContentUtil implements RecommendationAlgorithm{

    private final int TAG_NUMBER = 6;

    @Resource
    TagService tagService;

    @Resource
    MusicService musicService;

    //固定生成最大8个推荐
    public Map<Integer,Double> getRecommendations(int userId) {

        /*构建用户画像*/
        int[] portrait = getUserPortrait(userId);

        /*构建音乐特征向量*/
        Map<Integer, int[]> music_tagVector_map = generateMap();

        //获取收藏的歌曲ID
        Set<Integer> collectedMusic = musicService.getAllMusicIDCollections(userId);
        /*用户-音乐 相似度计算*/
        /*舍去用户收藏的音乐，剩余音乐直接排序*/
        LinkedHashMap<Integer, Double> collect = music_tagVector_map.entrySet().stream()
                .filter(entry -> !collectedMusic.contains(entry.getKey()))
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), calculateSimilarity(portrait, entry.getValue())))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(8)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldV, newV) -> oldV,
                        LinkedHashMap::new
                ));

        return collect;
    }

    private int[] getUserPortrait(int userId){
        //TODO 根据用户打的标签，计算用户与这些标签的近似度【通过卡方检验】
        List<Integer> likedTagIDList = tagService.getLikedTagIDList(userId);
        int[] userPortrait = new int[TAG_NUMBER];
        for(int tagId : likedTagIDList){
            userPortrait[tagId-1] = 1;
        }
        return userPortrait;
    }

    private HashMap<Integer, int[]> generateMap(){

        HashMap<Integer, int[]> collect = tagService.getMusicTagIDMap().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            int[] res = new int[TAG_NUMBER];
                            for (int idx : entry.getValue()) {
                                res[idx - 1] = 1;
                            }
                            return res;
                        },
                        (oldV, newV) -> newV,
                        HashMap::new
                ));

        return collect;
    }

    //计算两个用户之间的相似度（基于余弦相似度）
    private double calculateSimilarity(int[] user, int[] music){

        double dotProduct = 0.0;//两向量的点积
        double norm1 = 0.0;//用户1向量的范数
        double norm2 = 0.0;//用户2向量的范数

        int len = user.length;
        for(int i = 0; i < len; i++){
            dotProduct += user[i] * music[i];
            norm1 += Math.pow(user[i],2);
            norm2 += Math.pow(music[i],2);
        }

        //计算余弦相似度，点积除以两个用户范数的乘积
        return dotProduct / (Math.sqrt(norm1)*Math.sqrt(norm2));
    }
}
