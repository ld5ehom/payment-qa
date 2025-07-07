package com.ld5ehom.payment.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * This class represents a transaction record, including charge and payment entries.
 * 이 클래스는 충전 및 결제 내역을 포함한 거래 기록을 나타냅니다.
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long walletId;
    private String orderId;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Creates a new transaction representing a balance charge
    // 잔액 충전을 나타내는 새로운 거래를 생성합니다.
    public static Transaction createChargeTransaction(
            Long userId,
            Long walletId,
            String orderId,
            BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.userId = userId;
        transaction.walletId = walletId;
        transaction.orderId = orderId;
        transaction.transactionType = TransactionType.CHARGE;
        transaction.amount = amount;
        transaction.description = "Charge";
        transaction.createdAt = LocalDateTime.now();
        transaction.updatedAt = LocalDateTime.now();

        return transaction;
    }

    // Creates a new transaction representing a payment
    // 결제를 나타내는 새로운 거래를 생성합니다.
    public static Transaction createPaymentTransaction(
            Long userId, Long walletId, String courseId,
            BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.userId = userId;
        transaction.walletId = walletId;
        transaction.orderId = courseId;
        transaction.transactionType = TransactionType.PAYMENT;
        transaction.amount = amount;
        transaction.description = "Payment for course " + courseId;
        transaction.updatedAt = LocalDateTime.now();
        transaction.createdAt = LocalDateTime.now();
        return transaction;
    }
}
