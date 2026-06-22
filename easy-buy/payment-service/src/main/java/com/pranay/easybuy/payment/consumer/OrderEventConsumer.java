package com.pranay.easybuy.payment.consumer;

import com.pranay.easy_buy.events.OrderEvent;
import com.pranay.easy_buy.events.PaymentEvent;
import com.pranay.easybuy.payment.dto.PaymentRequest;
import com.pranay.easybuy.payment.dto.PaymentResponse;
import com.pranay.easybuy.payment.entity.PaymentMethod;
import com.pranay.easybuy.payment.producer.PaymentEvetPublisher;
import com.pranay.easybuy.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final PaymentService paymentService;
    private final PaymentEvetPublisher paymentEvetPublisher;
    private final String ORDER_TOPIC = "order-topic";

    @KafkaListener(topics = ORDER_TOPIC, groupId = "payment-group")
    public void consumeOrderCreatedEvent(OrderEvent orderEvent) {
        log.info("Received OrderEvent from Kafka: {}", orderEvent);

        if (orderEvent.getOrderId() == null) {
            log.error("Received OrderEvent with null orderId");
            return;
        }

        try {
            PaymentRequest paymentRequest = new PaymentRequest(
                    orderEvent.getOrderId(),
                    orderEvent.getTotalAmount() != null ? orderEvent.getTotalAmount() : BigDecimal.ZERO,
                    PaymentMethod.ONLINE,
                    orderEvent.getMessage() != null ? orderEvent.getMessage() : "Kafka Order Event"
            );
            log.info("Processing payment via Kafka consumer for Order ID: {}", orderEvent.getOrderId());
            PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

            PaymentEvent paymentEvent = new PaymentEvent(
                    paymentResponse.orderId(),
                    paymentResponse.transactionId(),
                    paymentResponse.amount(),
                    paymentResponse.status().name(),
                    "Payment processed successfully via Kafka consumer."
            );
            paymentEvetPublisher.pushPaymentEvent(paymentEvent);
        } catch (Exception e) {
            log.error("Error processing payment via Kafka consumer for Order ID: {}", orderEvent.getOrderId(), e);
            PaymentEvent paymentEvent = new PaymentEvent(
                    orderEvent.getOrderId(),
                    null,
                    orderEvent.getTotalAmount() != null ? orderEvent.getTotalAmount() : BigDecimal.ZERO,
                    "FAILED",
                    e.getMessage()
            );
            paymentEvetPublisher.pushPaymentEvent(paymentEvent);
        }
    }
}
