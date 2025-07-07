package com.ld5ehom.payment.transaction;

import java.math.BigDecimal;

public record PaymentTransactionResponse(Long walletId, BigDecimal balance) {
}