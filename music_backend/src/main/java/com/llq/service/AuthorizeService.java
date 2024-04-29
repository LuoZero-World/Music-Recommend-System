package com.llq.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author 李林麒
 * @date 2023/10/18 17:16
 * @Description     用于登录验证-数据库
 */
public interface AuthorizeService extends UserDetailsService {
    String sendValidateEmail(String email);
    String validateAndRegister(String username, String password, String email, String code);
    String sendResetEmail(String email);
    String validateOnly(String email, String code);
    boolean resetPassword(String email, String password);
    boolean isUsernameRepeat(String username);
    boolean validatePassword(String email, String password);
}
