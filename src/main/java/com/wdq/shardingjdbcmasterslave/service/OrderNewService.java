package com.wdq.shardingjdbcmasterslave.service;

import com.wdq.shardingjdbcmasterslave.dao.OrderNewDao;
import com.wdq.shardingjdbcmasterslave.entity.OrderNewInfoEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author BG405275
 */
@Log4j2
@Service
public class OrderNewService {
    @Autowired
    OrderNewDao orderNewDao;

    public List<OrderNewInfoEntity> queryOrderInfoList(OrderNewInfoEntity orderInfo) {
        return orderNewDao.queryOrderInfoList(orderInfo);
    }

    public OrderNewInfoEntity queryOrderInfoByOrderId(OrderNewInfoEntity orderInfo) {
        return orderNewDao.queryOrderInfoByOrderId(orderInfo);
    }

    public int addOrder(OrderNewInfoEntity orderInfo) {
        log.info("订单入库开始，orderinfo={}", orderInfo.toString());
        return orderNewDao.addOrder(orderInfo);
    }
}
