package com.practice.customerservice.service;

import com.practice.customerservice.dto.NotificationDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

// Service class to send notification to the notification service
@Component
public class NotifierService {
    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Autowired
    @Qualifier("rabbitJsonTemplate")
    private AmqpTemplate rabbitTemplate;

    // Send the message using a new thread
    public void notificationSender(NotificationDto notificationDto){
        Mono.fromRunnable(() -> {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, notificationDto);
        }).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
