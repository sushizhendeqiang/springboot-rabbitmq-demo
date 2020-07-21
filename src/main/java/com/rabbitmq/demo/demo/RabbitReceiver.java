package com.rabbitmq.demo.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author: sush4
 * @Description:
 * @Date: 2020/7/18
 */

@Component
@Slf4j
public class RabbitReceiver {
    @RabbitHandler
    //@RabbitListener(queues = "rabbitmq-demo")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "rabbitmq-demo", durable = "true"),
            exchange = @Exchange(name = "rabbit-springboot-exchange", durable = "true", type = "topic"),
            key = "rabbitmq-demo-routingkey"
    ))
    public void receiveMessage(Message message, Channel channel) throws UnsupportedEncodingException {
        String encoding = message.getMessageProperties().getContentEncoding();
        log.info("接收到string消息:[{}]", new String(message.getBody(), "UTF-8"));
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @RabbitHandler
    //@RabbitListener(queues = "rabbitmq-demo-bean")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "rabbitmq-demo-bean", durable = "true"),
            exchange = @Exchange(name = "rabbit-springboot-exchange", durable = "true", type = "topic"),
            key = "rabbitmq-demo-routingkey-bean"
    ))
    public void receiveMessage(User user, Message message, Channel channel) {
        log.info("接收到bean消息:[{}]", user);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
