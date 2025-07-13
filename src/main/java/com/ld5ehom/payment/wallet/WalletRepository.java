package com.ld5ehom.payment.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for Wallet entity to handle database operations
// Wallet 엔티티에 대한 데이터베이스 접근을 처리하는 리포지토리 인터페이스입니다.
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // Retrieves the most recent Wallet entity by user ID
    // 사용자 ID로 가장 최근의 Wallet 엔티티를 조회합니다.
    Optional<Wallet> findTopByUserId(Long userId);

    // Retrieves all Wallet entities associated with the given user ID
    // 주어진 사용자 ID에 해당하는 모든 Wallet 엔티티를 조회합니다.
    List<Wallet> findAllByUserId(Long userId);
}
