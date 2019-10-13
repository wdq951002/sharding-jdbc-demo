package com.wdq.shardingjdbcmasterslave;

import com.wdq.shardingjdbcmasterslave.entity.OrderInfo;
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

    @Test
    public void contextLoads() {
        for (int i = 0; i < 1000; i++) {
            long userId = i;
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUserName("snowalker");
            orderInfo.setUserId(userId);
            for(int j = 0; j < 2; j++) {
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

}
