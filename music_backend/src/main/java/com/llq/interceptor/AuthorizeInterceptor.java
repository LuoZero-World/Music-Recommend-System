package com.llq.interceptor;

import com.llq.dto.AccountDTO;
import com.llq.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author 李林麒
 * @date 2023/10/22 9:20
 * @Description 拦截器,拦截未经过权限校验的请求 注意要在Web配置类中配置
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Resource
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();

        Object principal_origin = context.getAuthentication().getPrincipal();
        if(principal_origin == null) return false;

        User principal = (User) principal_origin;
        AccountDTO account = userMapper.findAccountDTOByName(principal.getUsername());
        request.getSession().setAttribute("account", account);
        return true;
    }
}
