package com.llq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author 李林麒
 * @Date 2024/4/16
 * @Description 后台登录验证 返回
 */
@Data
@AllArgsConstructor
public class AdminAuthenticationDTO {
    boolean success;
    AdminData data;

    @Data
    public static class AdminData{
        String username;
        List<String> roles;
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.admin";
        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.adminRefresh";
        String expires = "2030/10/30 00:00:00";

        public AdminData(String username, List<String> roles) {
            this.username = username;
            this.roles = roles;
        }
    }
}


