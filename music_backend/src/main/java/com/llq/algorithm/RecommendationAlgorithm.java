package com.llq.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface RecommendationAlgorithm {
    Map<Integer,Double> getRecommendations(int userId);
    default Map<Integer, Double> normalizeToZeroOne(Map<Integer, Double> map) {
        if (map == null || map.size() == 0) {
            return map;
        }

        Double reduce = map.values().stream().reduce(0.0, Double::sum);
        // 归一化
        HashMap<Integer, Double> collect = map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()/reduce,
                        (oldV, newV) -> oldV,
                        HashMap::new
                ));

        return collect;
    }
}
