package com.wdq.shardingjdbcmasterslave.dao;

import com.wdq.shardingjdbcmasterslave.entity.OrderInfo;

import java.util.List;

public interface OrderDao {
    // 查询某个用户订单列表
    List<OrderInfo> queryOrderInfoList(OrderInfo orderInfo);

    // 通过订单号查询订单信息
    OrderInfo queryOrderInfoByOrderId(OrderInfo orderInfo);

    // 插入订单信息
    int addOrder(OrderInfo orderInfo);
}
