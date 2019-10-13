package com.wdq.shardingjdbcmasterslave.service;

import com.wdq.shardingjdbcmasterslave.entity.OrderInfo;

import java.util.List;

public interface OrderService {
    List<OrderInfo> queryOrderInfoList(OrderInfo orderInfo);

    OrderInfo queryOrderInfoByOrderId(OrderInfo orderInfo);

    int addOrder(OrderInfo orderInfo);
}
