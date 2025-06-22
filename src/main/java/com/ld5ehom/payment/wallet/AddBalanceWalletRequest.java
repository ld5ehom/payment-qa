package com.ld5ehom.payment.wallet;

import java.math.BigDecimal;

public record AddBalanceWalletRequest(Long walletId, BigDecimal amount) {
}
