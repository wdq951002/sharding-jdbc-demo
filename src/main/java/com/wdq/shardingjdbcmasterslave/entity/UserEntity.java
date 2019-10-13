package com.wdq.shardingjdbcmasterslave.entity;

import lombok.Data;

/**
 * @author BG405275
 */
@Data
public class UserEntity {
    private Integer id;
    private String username;
    private String password;
}
