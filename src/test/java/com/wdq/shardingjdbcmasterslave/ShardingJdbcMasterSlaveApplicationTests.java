package com.wdq.shardingjdbcmasterslave;

import com.wdq.shardingjdbcmasterslave.constant.DbAndTableEnum;
import com.wdq.shardingjdbcmasterslave.entity.OrderInfo;
import com.wdq.shardingjdbcmasterslave.entity.OrderNewInfoEntity;
import com.wdq.shardingjdbcmasterslave.sequence.KeyGenerator;
import com.wdq.shardingjdbcmasterslave.service.OrderNewService;
import com.wdq.shardingjdbcmasterslave.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShardingJdbcMasterSlaveApplicationTests {
    @Autowired
    OrderService orderService;
    @Autowired
    KeyGenerator keyGenerator;
    @Autowired
    OrderNewService orderNewSerivce;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 1000; i++) {
            long userId = i;
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUserName("snowalker");
            orderInfo.setUserId(userId);
            for (int j = 0; j < 2; j++) {
                long orderId = i + j;
                orderInfo.setOrderId(orderId);
                log.info("订单入库开始，orderinfo={}", orderInfo.toString());
                int result = orderService.addOrder(orderInfo);
                if (1 == result) {
                    log.info("入库成功,orderInfo={}", orderInfo);
                } else {
                    log.info("入库失败,orderInfo={}", orderInfo);
                }
            }
        }
    }

    /**
     * 测试订单入库
     */
    @Test
    public void testNewOrderInsert() {
        for (int i = 0; i < 10; i++) {
            // 支付宝或者微信uid
            String outId = "1232132131241241243126";
            log.info("获取id开始");
            String innerUserId = keyGenerator.generateKey(DbAndTableEnum.T_USER, outId);
            log.info("外部id={},内部用户={}", outId, innerUserId);
            String orderId = keyGenerator.generateKey(DbAndTableEnum.T_NEW_ORDER, innerUserId);
            log.info("外部id={},内部用户={},订单={}", outId, innerUserId, orderId);
            OrderNewInfoEntity orderInfo = new OrderNewInfoEntity();
            orderInfo.setUserName("snowalker");
            orderInfo.setUserId(innerUserId);
            orderInfo.setOrderId(orderId);
            orderNewSerivce.addOrder(orderInfo);
        }
    }

    /**
     * 测试订单明细查询
     */
    @Test
    public void testQueryNewOrderById() {
        String orderId = "OD000001011910161531036990000002";
        String userId = "UD030002011910161545322340000034";
        OrderNewInfoEntity orderInfo = new OrderNewInfoEntity();
        orderInfo.setOrderId(orderId);
        orderInfo.setUserId(userId);
        System.out.println(orderNewSerivce.queryOrderInfoList(orderInfo));
    }

}
