package com.ld5ehom.payment.transaction;

import java.math.BigDecimal;

public record PaymentTransactionRequest(
        Long walletId, String courseId, BigDecimal amount) {
}