package com.pranay.easybuy.payment.dto;

import com.pranay.easybuy.payment.entity.PaymentMethod;
import com.pranay.easybuy.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(Long id,
                              String transactionId,
                              Long orderId,
                              BigDecimal amount,
                              PaymentMethod paymentMethod,
                              PaymentStatus status,
                              String paymentGatewayTxnId,
                              Instant createdAt,
                              Instant updatedAt) {
}
