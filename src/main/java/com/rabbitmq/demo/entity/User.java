package com.rabbitmq.demo.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: sush4
 * @Description:
 * @Date: 2020/7/18
 */
@Data
@Builder
public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
}
