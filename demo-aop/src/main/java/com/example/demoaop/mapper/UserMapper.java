package com.example.demoaop.mapper;

import com.example.demoaop.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Author: zhouyq
 * @since: 2025/12/25
 */
@Mapper
public interface UserMapper {
    User selectById(Long id);

    int insert(User user);
}
