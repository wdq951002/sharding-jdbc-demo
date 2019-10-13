package com.wdq.shardingjdbcmasterslave.impl;

import com.wdq.shardingjdbcmasterslave.dao.OrderDao;
import com.wdq.shardingjdbcmasterslave.entity.OrderInfo;
import com.wdq.shardingjdbcmasterslave.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    final OrderDao orderDao;

    @Override
    public List<OrderInfo> queryOrderInfoList(OrderInfo orderInfo) {
        return orderDao.queryOrderInfoList(orderInfo);
    }

    @Override
    public OrderInfo queryOrderInfoByOrderId(OrderInfo orderInfo) {
        return orderDao.queryOrderInfoByOrderId(orderInfo);
    }

    @Override
    public int addOrder(OrderInfo orderInfo) {
        return orderDao.addOrder(orderInfo);
    }
}
