package com.llq.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

/**
 *  工具类，用于快速创建Rest风格的实体类
 *  成功返回 200 -> 前端通过此状态码进行判断
 */
public record RestBean<T>(int code, T data, String message) {

    public static <T> RestBean<T> success(T data, String msg){
        return new RestBean<>(200, data, msg);
    }

    public static <T> RestBean<T> success(String msg){
        return new RestBean<>(200, null, msg);
    }

    public static <T> RestBean<T> failure(int code, String message){
        return new RestBean<>(code, null, message);
    }

    public static <T> RestBean<T> failure(int code){
        return failure(code, "请求失败");
    }
    //将当前对象转换为JSON格式的字符串用于返回
    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
