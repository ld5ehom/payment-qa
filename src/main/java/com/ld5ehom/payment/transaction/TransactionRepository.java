package com.ld5ehom.payment.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Repository interface for performing CRUD operations on the Transaction entity.
 * 트랜잭션 엔티티에 대해 CRUD 작업을 수행하는 리포지토리 인터페이스입니다.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find a transaction by its order ID
    // 주문 ID를 기준으로 트랜잭션을 조회합니다.
    Optional<Transaction> findTransactionByOrderId(String orderId);
}
