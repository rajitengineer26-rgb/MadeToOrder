package com.mto.notificationservice.consumer;

import com.mto.notificationservice.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @KafkaListener(
            topics = "order-created",
            groupId = "notification-group"
    )
    public void consume(OrderEvent event) {

        System.out.println(
                "=================================="
        );

        System.out.println(
                "Notification Service Received Event"
        );

        System.out.println(
                "Order Id : " + event.getOrderId()
        );

        System.out.println(
                "Status   : " + event.getStatus()
        );

        System.out.println(
                "SMS Sent Successfully"
        );

        System.out.println(
                "=================================="
        );
    }
}