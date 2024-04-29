package com.llq.mapper;

import com.llq.entity.Account;
import com.llq.entity.AdminAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select `id`,`admin_name`,`email`,`password`,`role` from admin_info where admin_name=#{adminName}")
    AdminAccount findAdminAccountByName(String adminName);

}
