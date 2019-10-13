package com.wdq.shardingjdbcmasterslave.controller;

import com.wdq.shardingjdbcmasterslave.entity.OrderInfo;
import com.wdq.shardingjdbcmasterslave.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data/order")
@RequiredArgsConstructor
public class OrderController {
    final OrderService orderService;

    @GetMapping
    public List<OrderInfo> orderInfoList(OrderInfo info) {
        return orderService.queryOrderInfoList(info);
    }
}
