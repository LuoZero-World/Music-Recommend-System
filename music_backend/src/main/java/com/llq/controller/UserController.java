package com.llq.controller;

import com.llq.dto.AccountDTO;
import com.llq.entity.RestBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * @author 李林麒
 * @date 2023/10/22 9:25
 * @Description 先走security过滤器 -> 再走拦截器，因此如果未登录，是无法调用此页面的接口
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public RestBean<AccountDTO> me(@SessionAttribute("account") AccountDTO account){
        //用户登录后，session中便存有用户相关性息，将这些信息返回给前端
        return RestBean.success(account, "用户信息获取成功");
    }
}
