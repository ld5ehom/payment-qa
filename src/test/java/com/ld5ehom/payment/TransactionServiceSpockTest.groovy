package com.ld5ehom.payment

import com.ld5ehom.payment.transaction.ChargeTransactionRequest
import com.ld5ehom.payment.transaction.PaymentTransactionRequest
import com.ld5ehom.payment.transaction.Transaction
import com.ld5ehom.payment.transaction.TransactionRepository
import com.ld5ehom.payment.transaction.TransactionService
import com.ld5ehom.payment.wallet.*
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * This test class verifies the behavior of TransactionService
 * by mocking dependencies and testing charging/payment scenarios.
 * 이 테스트 클래스는 TransactionService의 동작을 검증하며,
 * 의존성을 목(mock) 처리하고 충전/결제 시나리오를 테스트합니다.
 */
class TransactionServiceSpockTest extends Specification {
    TransactionService transactionService
    WalletService walletService = Mock()
    TransactionRepository transactionRepository = Mock()

    def setup() {
        transactionService = new TransactionService(walletService, transactionRepository)
    }

    /**
     * Successfully processes a charge transaction if the order is new and wallet exists.
     * 주문이 새롭고 지갑이 존재하는 경우 충전 트랜잭션이 성공적으로 처리됩니다.
     */
    def "charge transaction succeeds when wallet exists and no duplicate order"() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId()) >> Optional.empty()

        def findWalletResponse = new FindWalletResponse(
                1L, 1L, BigDecimal.ZERO, LocalDateTime.now(), LocalDateTime.now())
        walletService.findWalletByUserId(1L) >> findWalletResponse

        def addBalanceWalletResponse = new AddBalanceWalletResponse(
                1L, 1L, findWalletResponse.balance().add(request.amount()), LocalDateTime.now(), LocalDateTime.now())
        walletService.addBalance(_) >> addBalanceWalletResponse

        when:
        def createdWallet = transactionService.charge(request)

        then:
        1 * transactionRepository.save(_)
        createdWallet != null
        println createdWallet
    }

    /**
     * Throws exception when wallet is not found during charge transaction.
     * 지갑이 존재하지 않으면 충전 트랜잭션에서 예외가 발생합니다.
     */
    def "charge transaction fails if wallet does not exist"() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId()) >> Optional.empty()
        walletService.findWalletByUserId(1L) >> null

        when:
        transactionService.charge(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
//        ex.message == "User wallet not found."
    }

    /**
     * Throws exception when duplicate order ID is used in charge transaction.
     * 중복된 주문 ID로 충전 트랜잭션을 요청하면 예외가 발생합니다.
     */
    def "charge transaction fails if orderId already exists"() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId()) >> Optional.of(new Transaction())

        when:
        transactionService.charge(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    /**
     * Successfully processes a payment transaction when course is not already paid.
     * 강좌가 이미 결제되지 않은 경우 결제 트랜잭션이 성공적으로 처리됩니다.
     */
    def "payment transaction succeeds if course is not already paid"() {
        given:
        PaymentTransactionRequest request = new PaymentTransactionRequest(1L, "100", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.courseId()) >> Optional.empty()

        def findWalletResponse = new FindWalletResponse(
                1L, 1L, BigDecimal.TEN, LocalDateTime.now(), LocalDateTime.now())
        walletService.findWalletByWalletId(1L) >> findWalletResponse

        def addBalanceWalletResponse = new AddBalanceWalletResponse(
                1L, 1L, findWalletResponse.balance().add(request.amount().negate()),
                LocalDateTime.now(), LocalDateTime.now())
        walletService.addBalance(_) >> addBalanceWalletResponse

        when:
        def createdWallet = transactionService.payment(request)

        then:
        1 * transactionRepository.save(_)
        createdWallet != null
        println createdWallet
    }

    // TODO: Add more failure cases for payment
}
