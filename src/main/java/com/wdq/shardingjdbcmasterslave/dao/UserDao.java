package com.wdq.shardingjdbcmasterslave.dao;

import com.wdq.shardingjdbcmasterslave.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    List<UserEntity> queryList();

    int insert(UserEntity user);
}
