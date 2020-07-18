package com.rabbitmq.demo.demo;

import com.rabbitmq.demo.entity.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @Author: sush4
 * @Description:
 * @Date: 2020/7/18
 */

@Component
public class RabbitProducer {
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     */
    @SendTo
    public void sendMessage() {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //发送简单消息
            IntStream.rangeClosed(1, 5).forEach(num -> {
                String body = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " : " + num;
                MessageProperties properties = new MessageProperties();
                //消息内容的编码格式
                properties.setContentEncoding("UTF-8");
                //Delivery mode: 是否持久化，1 - Non-persistent，2 - Persistent
                properties.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
                Message message = new Message(body.getBytes(Charset.forName(properties.getContentEncoding())), properties);
                amqpTemplate.convertAndSend("rabbit-springboot-exchange", "rabbitmq-demo-routingkey", message);
            });
            // 发送java bean 消息
            // 实体要序列化 否则 会发送失败
            IntStream.rangeClosed(1, 5).forEach(num -> {
                //这里的builder是lombok快速构建实体的一个方法
                User user = User.builder().userId(num).username("zhangsan:" + num).password("666666").build();
                //convertAndSend方法参数说明：
                //参数1：exchange     交换机名称
                //参数2：routingKey   绑定关系，通过绑定关系，将exchage交换机绑定到queue队列
                amqpTemplate.convertAndSend("rabbit-springboot-exchange", "rabbitmq-demo-routingkey-bean", user);
            });
        }).start();
    }
}
