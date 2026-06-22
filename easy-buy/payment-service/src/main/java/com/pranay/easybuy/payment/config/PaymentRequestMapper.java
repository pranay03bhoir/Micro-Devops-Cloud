package com.pranay.easybuy.payment.config;

import com.pranay.easybuy.payment.dto.PaymentRequest;
import com.pranay.easybuy.payment.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentRequestMapper {

    Transaction toTransaction(PaymentRequest paymentRequest);

    PaymentRequest toPaymentRequest(Transaction transaction);
}
