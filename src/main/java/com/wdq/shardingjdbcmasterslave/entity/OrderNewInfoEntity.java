package com.wdq.shardingjdbcmasterslave.entity;

import lombok.Data;

/**
 * @description:
 * @author: wdq-bg405275
 * @data: 2019-10-16 15:01
 **/
@Data
public class OrderNewInfoEntity {
    private Integer id;
    private String userId;
    private String orderId;
    private String userName;
}
