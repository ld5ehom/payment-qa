package com.ld5ehom.payment

import com.ld5ehom.payment.wallet.AddBalanceWalletRequest
import com.ld5ehom.payment.wallet.CreateWalletRequest
import com.ld5ehom.payment.wallet.Wallet
import com.ld5ehom.payment.wallet.WalletRepository
import com.ld5ehom.payment.wallet.WalletService
import spock.lang.Specification

import java.time.LocalDateTime

/*
 * This test class verifies WalletService behavior using unit tests with mocked repository.
 * 이 테스트 클래스는 WalletService의 동작을 모킹된 저장소를 이용해 단위 테스트합니다.
 */
class WalletServiceSpockTest extends Specification {
    WalletService walletService
    WalletRepository walletRepository = Mock()

    def setup() {
        walletService = new WalletService(walletRepository)
    }

    // Creates a wallet if user does not already have one
    // 지갑이 없는 경우 지갑을 생성합니다.
    def "creates wallet when user has none"() {
        given:
        CreateWalletRequest request = new CreateWalletRequest(1L)
        walletRepository.findTopByUserId(1L) >> Optional.empty()

        when:
        def createdWallet = walletService.createWallet(request)

        then:
        1 * walletRepository.save(_) >> new Wallet(1L)
        createdWallet != null
        createdWallet.balance() == BigDecimal.ZERO
        println createdWallet
    }

    // Throws exception if wallet already exists
    // 지갑이 이미 존재하는 경우 예외를 발생시킵니다.
    def "throws exception when wallet already exists"() {
        given:
        CreateWalletRequest request = new CreateWalletRequest(1L)
        walletRepository.findTopByUserId(1L) >> Optional.of(new Wallet(1L))

        when:
        walletService.createWallet(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    // Retrieves wallet if it exists
    // 지갑이 존재할 경우 해당 지갑을 조회합니다.
    def "retrieves wallet when it exists"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        wallet.balance = new BigDecimal(1000)
        walletRepository.findTopByUserId(userId) >> Optional.of(wallet)

        when:
        def result = walletService.findWalletByUserId(userId)

        then:
        result != null
        result.balance() == new BigDecimal(1000)
        println result
    }

    // Returns null if wallet does not exist
    // 지갑이 존재하지 않으면 null을 반환합니다.
    def "returns null when wallet does not exist"() {
        given:
        def userId = 1L
        walletRepository.findTopByUserId(userId) >> Optional.empty()

        when:
        def result = walletService.findWalletByUserId(userId)

        then:
        result == null
    }

    // Updates balance when wallet exists and add amount is valid
    // 지갑이 존재하고 잔액 추가가 유효한 경우 잔액을 업데이트합니다.
    def "updates balance with valid addition"() {
        given:
        def walletId = 1L
        def initialBalance = new BigDecimal("200.00")
        def addAmount = new BigDecimal("100.00")
        def wallet = new Wallet(walletId, walletId, initialBalance, LocalDateTime.now(), LocalDateTime.now())

        walletRepository.findById(walletId) >> Optional.of(wallet)

        when:
        def result = walletService.addBalance(new AddBalanceWalletRequest(walletId, addAmount))

        then:
        result.balance() == new BigDecimal("300.00")
    }

    // Throws exception if wallet does not exist
    // 지갑이 존재하지 않으면 예외를 발생시킵니다.
    def "throws exception when wallet not found"() {
        given:
        def walletId = 999L
        def addAmount = new BigDecimal("100.00")
        walletRepository.findById(walletId) >> Optional.empty()

        when:
        walletService.addBalance(new AddBalanceWalletRequest(walletId, addAmount))

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    // Throws exception if balance goes below zero
    // 잔액이 음수가 되는 경우 예외를 발생시킵니다.
    def "throws exception when balance becomes negative"() {
        given:
        def walletId = 1L
        def addAmount = new BigDecimal("-101.00")
        def initialBalance = new BigDecimal("100.00")
        def wallet = new Wallet(walletId, walletId, initialBalance, LocalDateTime.now(), LocalDateTime.now())
        walletRepository.findById(walletId) >> Optional.of(wallet)

        when:
        walletService.addBalance(new AddBalanceWalletRequest(walletId, addAmount))

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    // Throws exception if balance exceeds maximum limit
    // 잔액이 최대 한도를 초과하는 경우 예외를 발생시킵니다.
    def "throws exception when balance exceeds limit"() {
        given:
        def walletId = 1L
        def addAmount = new BigDecimal(100_000)
        def initialBalance = new BigDecimal(1)
        def wallet = new Wallet(walletId, walletId, initialBalance, LocalDateTime.now(), LocalDateTime.now())
        walletRepository.findById(walletId) >> Optional.of(wallet)

        when:
        walletService.addBalance(new AddBalanceWalletRequest(walletId, addAmount))

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }
}
