package com.ld5ehom.payment.wallet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Represents a wallet entity for tracking user balance and timestamps
// 사용자의 잔액 및 생성/수정 시점을 관리하는 지갑 엔티티 클래스입니다.

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal balance;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor for initializing a new wallet with default balance and timestamps
    // 초기 잔액 및 생성/수정 시간을 자동으로 설정하는 생성자입니다.
    public Wallet(Long userId) {
        this.userId = userId;
        this.balance = new BigDecimal(0);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
