package com.example.demoaop.controller;

import com.example.demoaop.annotation.MethodDescription;
import com.example.demoaop.entity.User;
import com.example.demoaop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zhouyq
 * @since: 2025/12/23
 */

@RestController
@RequestMapping("/v1/users")
public class TestController {
    @Autowired
    UserService userService;
    /**
     * 查询用户详情
     */
    @MethodDescription(
            value = "查询用户详情",
            enabled = true,
            logParams = true,
            logResult = true
    )
    @GetMapping("/ids")
    public User getUser(@RequestParam String userID) {
        Long id = Long.valueOf(userID);
        return userService.getUser(id);
    }

}
