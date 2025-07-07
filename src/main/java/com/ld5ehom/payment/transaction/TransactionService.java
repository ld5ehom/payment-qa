package com.ld5ehom.payment.transaction;

import com.ld5ehom.payment.wallet.AddBalanceWalletRequest;
import com.ld5ehom.payment.wallet.AddBalanceWalletResponse;
import com.ld5ehom.payment.wallet.FindWalletResponse;
import com.ld5ehom.payment.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/*
 * This service handles transaction logic for both charging and payment operations.
 * 이 서비스는 충전 및 결제 작업에 대한 트랜잭션 로직을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    /**
     * Charge
     * Processes a charge transaction by verifying order ID and updating wallet balance.
     * 주문 ID 중복을 확인하고 지갑 잔액을 업데이트하여 충전 트랜잭션을 처리합니다.
     */
    @Transactional
    public ChargeTransactionResponse charge(ChargeTransactionRequest request) {
        // Check if a transaction with the same order ID already exists
        // 동일한 주문 ID의 트랜잭션이 이미 존재하는지 확인합니다.
        if (transactionRepository.findTransactionByOrderId(request.orderId()).isPresent()) {
            throw new RuntimeException("Charge transaction already exists.");
        }

        // Retrieve the user's wallet
        // 사용자 지갑을 조회합니다.
        final FindWalletResponse findWalletResponse = walletService.findWalletByUserId(request.userId());

        if (findWalletResponse == null) {
            throw new RuntimeException("User wallet not found.");
        }

        // Add the charge amount to the wallet
        // 지갑에 충전 금액을 추가합니다.
        final AddBalanceWalletResponse wallet = walletService.addBalance(
                new AddBalanceWalletRequest(findWalletResponse.id(), request.amount()));

        // Create and save the charge transaction
        // 충전 트랜잭션을 생성하고 저장합니다.
        final Transaction transaction = Transaction.createChargeTransaction(
                request.userId(),
                wallet.id(),
                request.orderId(),
                request.amount());

        // Save
        transactionRepository.save(transaction);

        return new ChargeTransactionResponse(wallet.id(), wallet.balance());
    }

    /**
     * Payment
     * Processes a payment transaction by checking duplicates and deducting balance.
     * 중복 결제를 확인하고 잔액을 차감하여 결제 트랜잭션을 처리합니다.
     */
    @Transactional
    public PaymentTransactionResponse payment(PaymentTransactionRequest request) {
        // Check if a payment for the same course already exists
        // 동일한 강좌에 대한 결제가 이미 존재하는지 확인합니다.
        if (transactionRepository.findTransactionByOrderId(request.courseId()).isPresent()) {
            throw new RuntimeException("Course has already been paid for.");
        }

        // Retrieve the wallet using wallet ID
        // 지갑 ID를 사용하여 지갑을 조회합니다.
        final FindWalletResponse findWalletResponse = walletService.findWalletByWalletId(request.walletId());

        // Deduct the payment amount by adding a negative value
        // 음수 값을 추가하여 결제 금액만큼 차감합니다.
        final AddBalanceWalletResponse wallet = walletService.addBalance(
                new AddBalanceWalletRequest(findWalletResponse.id(), request.amount().negate()));

        // Create and save the payment transaction
        // 결제 트랜잭션을 생성하고 저장합니다.
        final Transaction transaction = Transaction.createPaymentTransaction(
                wallet.userId(), wallet.id(), request.courseId(), request.amount());
        transactionRepository.save(transaction);

        return new PaymentTransactionResponse(wallet.id(), wallet.balance());
    }
}
