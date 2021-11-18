package com.myifeng.example.mapper;

import com.myifeng.example.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where id=#{id}")
    User findOne(String id);

    @Insert("insert into user(id, name) values(#{id}, #{name})")
    int save(User user);

    @Update("update user set name=#{name} where id=#{id}")
    int update(User user);

    @Delete("delete from user where id=#{id}")
    int delete(String id);
}
