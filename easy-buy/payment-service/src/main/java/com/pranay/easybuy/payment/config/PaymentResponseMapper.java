package com.pranay.easybuy.payment.config;

import com.pranay.easybuy.payment.dto.PaymentResponse;
import com.pranay.easybuy.payment.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentResponseMapper {

    Transaction toTransaction(PaymentResponse paymentResponse);

    PaymentResponse toPaymentResponse(Transaction transaction);
}
