package com.mto.madetoorder.backend.service.producer;

import com.mto.madetoorder.backend.event.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String,OrderEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(OrderEvent event) {

        System.out.println("Sending Event: " + event.getOrderId());

        kafkaTemplate.send("order-created", event);
    }
}