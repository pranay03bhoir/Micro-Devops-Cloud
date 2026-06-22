package com.pranay.easybuy.payment.producer;

import com.pranay.easy_buy.events.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentEvetPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String PAYMENT_TOPIC = "payment-topic";

    public void pushPaymentEvent(PaymentEvent paymentEvent) {
        try {
            log.info("Publishing payment event to Kafka: {}", paymentEvent);
            this.kafkaTemplate.send(PAYMENT_TOPIC, paymentEvent);
            log.info("PaymentEvent published successfully for Order Id: {}", paymentEvent.getOrderId());
        } catch (Exception e) {
            log.error("Failed to publish PaymentEvent for Order ID: {}", paymentEvent.getOrderId(), e);
        }
    }
}
