package com.wdq.shardingjdbcmasterslave.dao;

import com.wdq.shardingjdbcmasterslave.entity.OrderNewInfoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderNewDao {
    List<OrderNewInfoEntity> queryOrderInfoList(OrderNewInfoEntity orderInfo);

    OrderNewInfoEntity queryOrderInfoByOrderId(OrderNewInfoEntity orderInfo);

    int addOrder(OrderNewInfoEntity orderInfo);
}
