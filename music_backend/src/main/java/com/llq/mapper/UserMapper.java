package com.llq.mapper;

import com.llq.entity.Account;
import com.llq.dto.AccountDTO;
import org.apache.ibatis.annotations.*;

/**
 * @author 李林麒
 * @date 2023/10/18 17:20
 * @Description
 */
@Mapper
public interface UserMapper {

    @Select("select `id`, `user_name`,`email` from user_info where user_name=#{username}")
    AccountDTO findAccountDTOByName(String username);

    @Select("select * from user_info where user_name=#{text} or email=#{text}")
    Account findAccountByNameOrEmail(String text);

    @Insert("insert into user_info (user_name, email, password) values(#{username}, #{email}, #{password})")
    int createAccount(@Param("username") String username, @Param("password") String password, @Param("email") String email);

    @Update("update user_info set password=#{password} where email=#{email}")
    int resetPassword(@Param("email") String email, @Param("password") String password);
}
