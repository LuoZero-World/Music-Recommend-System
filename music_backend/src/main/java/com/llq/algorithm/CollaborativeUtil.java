package com.llq.algorithm;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 李林麒
 * @Date 2024/4/17
 * @Description 混合推荐算法 加权混合
 */
@Component
public class CollaborativeUtil implements RecommendationAlgorithm{

    @Resource
    ContentUtil contentUtil;

    @Resource
    UserCFUtil userCFUtil;

    //生成5个推荐
    public Map<Integer,Double> getRecommendations(int userId){
        Map<Integer, Double> recommendations1 = normalizeToZeroOne(userCFUtil.getRecommendations(userId));
        Map<Integer, Double> recommendations2 = normalizeToZeroOne(contentUtil.getRecommendations(userId));

        recommendations1.forEach((key, value)->{
            recommendations2.merge(key, value, (oldV, newV) -> oldV*0.3 + newV*0.7);
        });
        LinkedHashMap<Integer, Double> sortedRecommendations = recommendations2.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        return sortedRecommendations;
    }
}
