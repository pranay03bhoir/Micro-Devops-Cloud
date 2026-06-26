package com.pranay.easybuy.payment.services.serviceImpl;

import com.pranay.easybuy.payment.dto.PaymentRequest;
import com.pranay.easybuy.payment.dto.PaymentResponse;
import com.pranay.easybuy.payment.services.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        return null;
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return List.of();
    }

    @Override
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        return null;
    }
}
