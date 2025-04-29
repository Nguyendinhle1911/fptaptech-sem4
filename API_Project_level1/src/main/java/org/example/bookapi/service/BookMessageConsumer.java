package org.example.bookapi.service;

import lombok.extern.slf4j.Slf4j;
import org.example.bookapi.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookMessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        log.info("Received message from RabbitMQ: {}", message);
        // Xử lý thêm nếu cần, ví dụ: gửi email, lưu log, v.v.
    }
}