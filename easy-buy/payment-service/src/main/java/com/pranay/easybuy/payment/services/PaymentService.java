package com.pranay.easybuy.payment.services;

import com.pranay.easybuy.payment.dto.PaymentRequest;
import com.pranay.easybuy.payment.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);

    List<PaymentResponse> getPaymentsByOrderId(Long orderId);

    PaymentResponse getPaymentByTransactionId(String transactionId);
}
