package com.llq.algorithm;

import com.llq.entity.AccountRating;
import com.llq.service.MusicService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 李林麒
 * @Date 2024/4/17
 * @Description 协同过滤推荐算法-根据用户
 */
@Component
public class UserCFUtil implements RecommendationAlgorithm{

    @Resource
    MusicService musicService;

    //用户对于各首歌曲的评分表 - 即用户-音乐评分矩阵
    private static Map<Integer,Map<Integer,Double>> userRatings = new HashMap<>();


    public double calculateSimilarity(Map<Integer, Double> myRatings, Map<Integer, Double> otherRatings){
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2;

        for(Map.Entry<Integer, Double> entry : myRatings.entrySet()){
            double otherRate = otherRatings.getOrDefault(entry.getKey(), 0.0);
            dotProduct += otherRate*entry.getValue();
            norm1 += Math.pow(entry.getValue(),2);
        }
        norm2 = otherRatings.values().stream()
                .map(value -> Math.pow(value,2))
                .reduce(0.0, Double::sum);

        return  dotProduct / (Math.sqrt(norm1)*Math.sqrt(norm2));
    }

    @Scheduled(fixedDelay = 8000L)
    public void flushRatings(){
        /* 构建用户-音乐评分矩阵 */
        List<AccountRating> list = musicService.getAccountRatingList();
        userRatings = list.stream()
                .collect(Collectors.groupingBy(
                        AccountRating::getUserId,
                        Collectors.toMap(
                                AccountRating::getMusicId,
                                userRatingDTO -> {
                                    int k = userRatingDTO.getPlayNum();
                                    int isC = userRatingDTO.getCollected();
                                    return Math.min(k, 20)*0.1+isC*3;
                                }
                        , (oldV, newV) -> oldV,HashMap::new)
                ));
    }

    //为用户生成推荐，最大生成8个推荐
    public Map<Integer,Double> getRecommendations(int userId){
        //找到目标用户的评分列表
        Map<Integer ,Double> targetRatings = userRatings.get(userId);

        /*用户相似度矩阵*/
        //键值对列表存储用户列表中的用户与目标用户的相似度
        List<Map.Entry<Integer, Double>> similaritiesList = userRatings.keySet().stream()
                .filter(otherId -> !Objects.equals(otherId, userId))
                .map(otherId ->
                        new AbstractMap.SimpleEntry<>(otherId, calculateSimilarity(targetRatings, userRatings.get(otherId))))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(8)       //最多8个相似用户
                .collect(Collectors.toList());

        /*从邻居中获取音乐，舍去已经收藏的，对剩余预测评分，进行推荐*/
        Map<Integer, Double> preRecommendMap = new HashMap<>();
        for(Map.Entry<Integer, Double> entry : similaritiesList){
            int otherId = entry.getKey();
            double similarity = entry.getValue();
            //获取邻居的【音乐-评分】表
            Map<Integer, Double> otherRatingMap = userRatings.get(otherId);
            otherRatingMap.entrySet().stream()
                    .filter(rEntry -> !targetRatings.containsKey(rEntry.getKey()))
                    .collect(Collectors.toMap(      //生成【音乐-预测评分】表，但此时的预测评分只是一部分
                            Map.Entry::getKey,
                            rEntry -> rEntry.getValue() * similarity,
                            (newV, oldV) -> oldV,
                            HashMap::new
                    ))
                    .forEach((key, value)->{        //音乐预测评分=Σ 用户相似度*用户评分
                        preRecommendMap.merge(key, value, Double::sum);
                    });
        }

        LinkedHashMap<Integer, Double> sortedRecommendations = preRecommendMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(8)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, //物品为键
                        Map.Entry::getValue,//预测评分为值
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new//用LinkedHashMap保持插入顺序
                ));
        return sortedRecommendations;
    }
}

