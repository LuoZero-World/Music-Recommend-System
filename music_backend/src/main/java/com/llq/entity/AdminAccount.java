package com.llq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 李林麒
 * @Date 2024/4/16
 * @Description 管理员
 */
@Data
@AllArgsConstructor
public class AdminAccount {
    int id;
    String adminName;
    String email;
    String password;
    int role;
}
