package com.example.demoaop.service;

import com.example.demoaop.entity.User;
import com.example.demoaop.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: zhouyq
 * @since: 2025/12/25
 */
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public User getUser(Long id) {
        return userMapper.selectById(id);
    }
}
