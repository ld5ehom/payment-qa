package com.ld5ehom.payment.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
 * This class handles wallet creation, retrieval, and balance modification logic.
 * 이 클래스는 지갑 생성, 조회, 잔액 변경 로직을 처리합니다.
 */
@RequiredArgsConstructor
@Service
public class WalletService {
    // balance limit
    private static final BigDecimal BALANCE_LIMIT = new BigDecimal(100_000);

    private final WalletRepository walletRepository;

    // In-memory wallet map for Java unit tests
    // Java 단위 테스트용 인메모리 지갑 맵입니다.
    private Map<Long, Wallet> walletMap;

    {
        walletMap = new HashMap<>();
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setId(1L);
        walletMap.put(1L, wallet);
    }

    // CRUD : Create Read Update Delete

    /// Creates a new wallet for the given user if one does not already exist.
    // 사용자의 지갑이 존재하지 않을 경우 새 지갑을 생성합니다.
    @Transactional
    public CreateWalletResponse createWallet(CreateWalletRequest request) {
        boolean isWalletExist = walletRepository.findTopByUserId(request.userId())
                .isPresent();
        if (isWalletExist) {
            throw new RuntimeException("Wallet already exists.");
        }

        final Wallet wallet = walletRepository.save(new Wallet(request.userId()));

        return new CreateWalletResponse(
                wallet.getId(), wallet.getUserId(), wallet.getBalance());
    }

    /// Finds the wallet associated with the given user ID.
    // 주어진 사용자 ID에 해당하는 지갑을 조회합니다.
    public FindWalletResponse findWalletByUserId(Long userId) {
        return walletRepository.findTopByUserId(userId)
                .map(wallet -> new FindWalletResponse(
                        wallet.getId(),
                        wallet.getUserId(),
                        wallet.getBalance(),
                        wallet.getCreatedAt(),
                        wallet.getUpdatedAt()
                ))
                .orElse(null);
    }

    /// Finds the wallet by wallet ID.
    // 지갑 ID로 지갑을 조회합니다.
    public FindWalletResponse findWalletByWalletId(Long walletId) {
        return walletRepository.findById(walletId)
                .map(wallet -> new FindWalletResponse(
                        wallet.getId(),
                        wallet.getUserId(),
                        wallet.getBalance(),
                        wallet.getCreatedAt(),
                        wallet.getUpdatedAt()
                ))
                .orElse(null);
    }

    /// Adds the specified amount to the wallet balance with validation rules:
    /// - Balance must not go below zero.
    /// - Balance must not exceed the limit (100,000).
    // 지갑에 금액을 추가하며 다음 조건을 검사합니다:
    // - 잔액이 0보다 작아지면 안됩니다.
    // - 잔액이 최대 한도(10만원)를 초과하면 안됩니다.
    @Transactional
    public AddBalanceWalletResponse addBalance(AddBalanceWalletRequest request) {
        final Wallet wallet = walletRepository.findById(request.walletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found."));
        BigDecimal balance = wallet.getBalance();
        balance = balance.add(request.amount());

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance.");
        }

        if (BALANCE_LIMIT.compareTo(balance) < 0) {
            throw new RuntimeException("Balance limit exceeded.");
        }

        wallet.setBalance(balance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        return new AddBalanceWalletResponse(
                wallet.getId(), wallet.getUserId(), wallet.getBalance(),
                wallet.getCreatedAt(), wallet.getUpdatedAt()
        );
    }

    /// Adds balance to an in-memory wallet instance for Java-based tests
    // Java 기반 테스트를 위한 인메모리 지갑에 잔액을 추가합니다.
    public AddBalanceWalletResponse addBalanceJava(AddBalanceWalletRequest request) {
        final Wallet wallet = walletMap.get(request.walletId());
        BigDecimal balance = wallet.getBalance();
        balance = balance.add(request.amount());

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance.");
        }

        if (BALANCE_LIMIT.compareTo(balance) < 0) {
            throw new RuntimeException("Balance limit exceeded.");
        }

        wallet.setBalance(balance);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletMap.put(wallet.getId(), wallet);

        return new AddBalanceWalletResponse(
                wallet.getId(), wallet.getUserId(), wallet.getBalance(),
                wallet.getCreatedAt(), wallet.getUpdatedAt()
        );
    }

    /// Retrieves a wallet from in-memory storage (for Java unit test usage)
    // 인메모리 저장소에서 지갑을 조회합니다 (Java 유닛 테스트용).
    public FindWalletResponse findWalletJava(Long id) {
        Wallet wallet = walletMap.get(id);
        return new FindWalletResponse(
                wallet.getId(), wallet.getUserId(), wallet.getBalance(),
                LocalDateTime.now(), LocalDateTime.now()
        );
    }
}
