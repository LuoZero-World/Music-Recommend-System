package com.llq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/10/18 17:18
 * @Description   普通用户
 */
@Data
@AllArgsConstructor
public class Account{
    int id;
    String username;
    String email;
    String password;
}
