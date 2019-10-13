package com.wdq.shardingjdbcmasterslave.controller;

import com.wdq.shardingjdbcmasterslave.dao.UserDao;
import com.wdq.shardingjdbcmasterslave.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data/user")
@RequiredArgsConstructor
public class UserController {
    final UserDao userDao;


    @GetMapping
    public List<UserEntity> getUserList() {
        return userDao.queryList();
    }

    @PostMapping
    public void insertUser(UserEntity user) {
        userDao.insert(user);
    }
}
