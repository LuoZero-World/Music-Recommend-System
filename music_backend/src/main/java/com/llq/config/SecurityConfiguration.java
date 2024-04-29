package com.llq.config;

import com.llq.dto.AdminAuthenticationDTO;
import com.llq.entity.RestBean;
import com.llq.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 李林麒
 * @date 2023/10/18 16:34
 * @Description
 */
@Configuration
public class SecurityConfiguration {

    @Resource
    DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //【AuthenticationManager】
    //有什么用-> 通过向实现类ProviderManager传入各种AuthenticationProvider从而进行登录校验
    //如何配置 -> 见下述函数内部注释处
    //如果没有配置,那么AuthenticationManagerBuilder是从工厂里拿现有的bean 然后再去构建,就生成了AuthenticationManager
    //因此我们常用自定义bean的方式进行配置

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security
                .securityMatcher("/api/**")
                .authorizeHttpRequests(conf->{
                    conf.requestMatchers("/api/auth/**").permitAll();
                    conf.anyRequest().authenticated();
                })
                .formLogin(conf->{
                    conf.loginPage("http://localhost:5173/");
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler(this::onAuthenticationSuccess);
                    conf.failureHandler(this::onAuthenticationFailure);
                    conf.permitAll();
                })
                .cors(conf->{
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.addAllowedOriginPattern("*");
                    cors.setAllowCredentials(true);
                    cors.addAllowedHeader("*");
                    cors.addAllowedMethod("*");
                    cors.addExposedHeader("*");

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);
                    conf.configurationSource(source);
                })
                .rememberMe(conf ->{
                    conf.rememberMeParameter("remember");
                    conf.tokenRepository(jdbcRepository());
                    conf.tokenValiditySeconds(3600*24);
                })
                .logout(conf->{
                    conf.logoutUrl("/api/auth/logout");
                    conf.logoutSuccessHandler(this::onAuthenticationSuccess);
                })
//                .userDetailsService(new JdbcUserDetailsManager())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain_admin(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(conf->{
                    conf.requestMatchers("/admin/auth/**").permitAll();
                    conf.anyRequest().authenticated();
                })
                .formLogin(conf->{
                    conf.loginPage("http://localhost:8848/");
                    conf.loginProcessingUrl("/admin/auth/login");
                    conf.successHandler(this::onAuthenticationSuccess_admin);
                    conf.failureHandler(this::onAuthenticationFailure_admin);
                    conf.permitAll();
                })
                .cors(conf->{
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.addAllowedOriginPattern("*");
                    cors.setAllowCredentials(true);
                    cors.addAllowedHeader("*");
                    cors.addAllowedMethod("*");
                    cors.addExposedHeader("*");

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);
                    conf.configurationSource(source);
                })
                .logout(conf->{
                    conf.logoutUrl("/admin/auth/logout");
                    conf.logoutSuccessHandler(this::onAuthenticationSuccess);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PersistentTokenRepository jdbcRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();  //使用基于JDBC的实现
        repository.setDataSource(dataSource);   //配置数据源
//        repository.setCreateTableOnStartup(true);   //启动时自动创建用于存储Token的表
        return repository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthorizeService authorizeService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authorizeService);

        return new ProviderManager(provider);
    }

    void onAuthenticationSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        //login请求
        if(request.getRequestURI().endsWith("login")){
            writer.write(RestBean.success(authentication.getName(), "登录成功").asJsonString());
        }
        //logout请求
        else if(request.getRequestURI().endsWith("logout"))
            writer.write(RestBean.success("退出成功").asJsonString());
    }

    void onAuthenticationFailure(HttpServletRequest request,
                                 HttpServletResponse response,
                                 AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(RestBean.failure(401, exception.getMessage()).asJsonString());
    }

    void onAuthenticationSuccess_admin(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();

        //login请求
        if(request.getRequestURI().endsWith("login")){
            String name = authentication.getName();
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority).toList();
            AdminAuthenticationDTO adminAuthenticationDTO = new AdminAuthenticationDTO(true, new AdminAuthenticationDTO.AdminData(name, roles));
            writer.write(RestBean.success(adminAuthenticationDTO, "登录成功").asJsonString());
        }
        //logout请求
        else if(request.getRequestURI().endsWith("logout"))
            writer.write(RestBean.success("退出成功").asJsonString());
    }

    void onAuthenticationFailure_admin(HttpServletRequest request,
                                 HttpServletResponse response,
                                 AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(RestBean.failure(401, exception.getMessage()).asJsonString());
    }
}
