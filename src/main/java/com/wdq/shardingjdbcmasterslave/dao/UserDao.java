package com.wdq.shardingjdbcmasterslave.dao;

import com.wdq.shardingjdbcmasterslave.po.UserPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    List<UserPo> queryList();

    int insert(UserPo user);
}
