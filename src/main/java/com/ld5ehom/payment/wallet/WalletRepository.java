package com.ld5ehom.payment.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository interface for Wallet entity to handle database operations
// Wallet 엔티티에 대한 데이터베이스 접근을 처리하는 리포지토리 인터페이스입니다.

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // Retrieves a Wallet entity by user ID
    // 사용자 ID로 Wallet 엔티티를 조회합니다.
    Optional<Wallet> findWalletByUserId(Long userId);
}
