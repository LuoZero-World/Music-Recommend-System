package com.llq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 李林麒
 * @date 2023/10/22 9:5
 * @Description     用于留存在前端
 */
@Data
@AllArgsConstructor
public class AccountDTO {
    int id;
    String userName;
    String email;
}
