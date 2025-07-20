package com.practice.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.notificationservice.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SendNotificationService {

    // Listen for the published message from the producer to the designated queue
    @RabbitListener(queues = {"${rabbitmq.queue}"})
    public void sendNotification(byte[] notificationData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NotificationDto dto = mapper.readValue(notificationData, NotificationDto.class);

        log.info("Hi, {}, email: {}. Your order for '{}' with total price: Rs. {}, and quantity: {} is successfully placed!", dto.getCustomerName(), dto.getEmail(), dto.getProductName(), dto.getTotalPrice(), dto.getProductQuantity());
    }
}
