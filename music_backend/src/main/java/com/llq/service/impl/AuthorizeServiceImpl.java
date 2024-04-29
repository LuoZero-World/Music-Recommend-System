package com.llq.service.impl;

import com.llq.entity.Account;
import com.llq.entity.AdminAccount;
import com.llq.mapper.AdminMapper;
import com.llq.mapper.UserMapper;
import com.llq.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 李林麒
 * @date 2023/10/20 21:20
 * @Description
 */
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Resource
    UserMapper mapper;

    @Resource
    AdminMapper adminMapper;

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    @Resource
    StringRedisTemplate template;

    @Resource @Lazy
    BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if(name == null)
            throw new UsernameNotFoundException("用户名不能为空");
        if(name.startsWith("admin")){       //属于管理员登录
            AdminAccount adminAccount = adminMapper.findAdminAccountByName(name);
            if(adminAccount == null)
                throw new UsernameNotFoundException("管理员名或密码错误");
            return User
                    .withUsername(name)
                    .password(adminAccount.getPassword())
                    .roles(adminAccount.getRole() == 0 ? "admin" : "common")
                    .build();
        } else{
            Account account = mapper.findAccountByNameOrEmail(name);
            if(account == null)
                throw new UsernameNotFoundException("用户名或密码错误");
            return User
                    .withUsername(name)
                    .password(account.getPassword())
                    .roles("USER")
                    .build();
        }
    }

    @Override
    public String sendValidateEmail(String email) {

        if(mapper.findAccountByNameOrEmail(email) != null)
            return "该邮箱已经被注册";

        //实现功能: 1min后才能重新发送
        if(Boolean.TRUE.equals(template.hasKey(email))){
            long expire = Optional.ofNullable(template.getExpire(email)).orElse(0L);
            if(expire > 120) return "请求频繁，请稍后再试";
        }
        //生成验证码
        Random random = new Random();
        int code = random.nextInt(900000)+100000;
        //发送到指定邮箱
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【音乐推荐系统】验证码");
        message.setText("验证码是"+code);
        message.setTo(email);
        message.setFrom(from);

        try {
            //发送成功 -> 存redis（过期时间3min）;
            template.opsForValue().set(email, String.valueOf(code), 3, TimeUnit.MINUTES);
            mailSender.send(message);
            return null;
        } catch (MailException e){
            e.printStackTrace();
            return "邮件发送失败，请检查邮箱地址是否正确";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code) {
        if(Boolean.TRUE.equals(template.hasKey(email))){
            String s = template.opsForValue().get(email);
            if(s == null) return "验证码失效";
            if(s.equals(code)){
                //清除验证码
                template.delete(email);
                password = encoder.encode(password);
                if(mapper.createAccount(username, password, email) <= 0){
                    return "内部错误，请联系管理员";
                }
                return null;
            } else{
                return "验证码错误";
            }
        } else{
            return "当前邮箱未请求验证";
        }
    }

    @Override
    public String sendResetEmail(String email) {

        if(mapper.findAccountByNameOrEmail(email) == null)
            return "该邮箱未被注册，用户不存在";

        String key = email+"-reset";     //和注册时的邮件验证区别开
        //实现功能: 1min后才能重新发送
        if(Boolean.TRUE.equals(template.hasKey(key))){
            long expire = Optional.ofNullable(template.getExpire(key)).orElse(0L);
            if(expire > 120) return "请求频繁，请稍后再试";
        }

        //生成验证码
        Random random = new Random();
        int code = random.nextInt(9000)+1000;
        //发送到指定邮箱
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【音乐推荐系统】正在重置密码");
        message.setText("验证码是"+code);
        message.setTo(email);
        message.setFrom(from);

        //因为邮箱已经被注册，那么地址必然可靠
        template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);
        mailSender.send(message);
        return null;
    }

    @Override
    public String validateOnly(String email, String code) {
        String key = email+"-reset";
        if(Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if(s == null) return "验证码失效";
            if(s.equals(code)){
                //清除验证码
                template.delete(email);
                return null;
            } else{
                return "验证码错误";
            }
        } else{
            return "当前邮箱未请求验证";
        }
    }

    @Override
    public boolean resetPassword(String email, String password) {
        password = encoder.encode(password);
        return mapper.resetPassword(email, password) > 0;
    }

    @Override
    public boolean isUsernameRepeat(String username) {
        Account account = mapper.findAccountByNameOrEmail(username);
        return account != null;
    }

    @Override
    public boolean validatePassword(String email, String password) {
        Account account = mapper.findAccountByNameOrEmail(email);
        return account != null && encoder.matches(password, account.getPassword());
    }
}
