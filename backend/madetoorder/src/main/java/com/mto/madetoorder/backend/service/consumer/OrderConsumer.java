package com.mto.madetoorder.backend.service.consumer;

import com.mto.madetoorder.backend.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

  //  @KafkaListener(topics = "order-created", groupId = "mto-group")
    public void consume(OrderEvent event) {
        System.out.println(
                "Received Order Event: "
                        + event.getOrderId());
    }
}