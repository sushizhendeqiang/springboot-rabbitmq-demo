package com.rabbitmq.demo;

import com.rabbitmq.demo.demo.RabbitProducer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {
    @Resource
    private RabbitProducer rabbitProducer;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        rabbitProducer.sendMessage();
    }
}
