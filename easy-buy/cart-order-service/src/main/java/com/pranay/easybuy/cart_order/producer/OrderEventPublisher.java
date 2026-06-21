package com.pranay.easybuy.cart_order.producer;

import com.pranay.easy_buy.events.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String ORDER_TOPIC = "order-topic";

    public void publishOrderCreatedEvent(OrderEvent orderEvent) {
        try {
            this.kafkaTemplate.send(ORDER_TOPIC, orderEvent);
            log.info("Order Event Published Successfully: {}", orderEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
