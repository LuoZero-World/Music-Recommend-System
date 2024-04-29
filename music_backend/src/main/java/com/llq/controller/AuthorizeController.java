package com.llq.controller;

import com.llq.entity.RestBean;
import com.llq.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李林麒
 * @date 2023/10/20 21:27
 * @Description 用于处理验证的接口
 */
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String USERNAME_REGEX = "^[0-9a-zA-Z-_]+$";

    @Resource
    AuthorizeService authorizeService;

    @PostMapping("/valid-repeat-username")
    public RestBean<String> validateRepeatUsername(@Pattern (regexp = USERNAME_REGEX) @Length(min = 6, max = 8)@RequestParam String username){
        if(authorizeService.isUsernameRepeat(username)){
            return RestBean.failure(400, "用户名已经存在");
        } else{
            return RestBean.success("");
        }
    }

    @PostMapping("/valid-email")
    public RestBean<String> validateRegisterEmail(@RequestParam @Email String email)
    {
        String msg = authorizeService.sendValidateEmail(email);
        if(msg == null)
            return RestBean.success("邮件已发送，请注意查收");
        else return RestBean.failure(400, msg);
    }

    @PostMapping("/register")
    public RestBean<String> register(@Pattern (regexp = USERNAME_REGEX) @Length(min = 6, max = 8)@RequestParam String username,
                                     @Length(min = 6, max = 16) @RequestParam String password,
                                     @Email @RequestParam String email,
                                     @Pattern(regexp = "^\\d+$")@Length(min = 6, max = 6)@RequestParam String code){
        String msg = authorizeService.validateAndRegister(username, password, email, code);
        if(msg == null)
            return RestBean.success("注册成功");
        else
            return RestBean.failure(400, msg);
    }

    @PostMapping("/validate-reset-email")
    public RestBean<String> validateResetEmail(@Email @RequestParam String email){
        String msg = authorizeService.sendResetEmail(email);
        if(msg == null){
            return RestBean.success("邮件已发送，请注意查收");
        } else{
            return RestBean.failure(400, msg);
        }
    }

    @PostMapping("/reset-first")
    public RestBean<String> resetFirst(@Email @RequestParam String email,
                                       @Pattern(regexp = "^\\d+$")@Length(min = 4, max = 4)@RequestParam String code,
                                       HttpSession session){
        String msg = authorizeService.validateOnly(email, code);
        if(msg == null) {
            session.setAttribute("reset-email", email);
            return RestBean.success("验证成功");
        }
        else
            return RestBean.failure(400, msg);
    }

    @PostMapping("/reset-second")
    public RestBean<String> resetSecond(@Length(min = 6, max = 16) @RequestParam String password,
                                        HttpSession session){
        String email = (String) session.getAttribute("reset-email");
        if(email == null){
            return RestBean.failure(401, "请先完成邮件验证");       //401未授权
        } else if(authorizeService.resetPassword(email, password)){
            session.removeAttribute("reset-email");
            return RestBean.success("密码重置成功");
        } else{
            return RestBean.failure(500, "内部错误，请联系管理员");
        }
    }

    @PostMapping("/internal-reset")
    public RestBean<String> internalReset(@Email @RequestParam String email,
                                          @Length(min = 6, max = 16) @RequestParam String password_old,
                                          @Length(min = 6, max = 16) @RequestParam String password_new){
        if(authorizeService.validatePassword(email, password_old)){
            //旧密码正确
            boolean flag = authorizeService.resetPassword(email, password_new);
            return flag? RestBean.success("修改成功") : RestBean.failure(500, "内部错误，修改失败");
        }
        return RestBean.failure(401, "旧密码错误");
    }
}
