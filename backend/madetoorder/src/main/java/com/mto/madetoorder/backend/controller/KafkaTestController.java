package com.mto.madetoorder.backend.controller;

import com.mto.madetoorder.backend.event.OrderEvent;
import com.mto.madetoorder.backend.service.producer.OrderProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class KafkaTestController {

    private final OrderProducer orderProducer;

    public KafkaTestController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/kafka")
    public String testKafka() {

        OrderEvent event = new OrderEvent();

        event.setOrderId("ORDER-123");
        event.setStatus("CREATED");

        orderProducer.sendOrderEvent(event);

        return "Event Sent";
    }
}