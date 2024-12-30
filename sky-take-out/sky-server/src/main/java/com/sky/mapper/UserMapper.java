package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据openId查询用户
     *
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openId}")
    User selectByOpenId(String openId);

    /**
     * 新增用户
     *
     * @param user
     */
    void addUser(User user);
}
