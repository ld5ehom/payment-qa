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

    // Test wallet creation when no wallet exists
    // 지갑이 없는 경우 지갑 생성 테스트
    def "Creates a wallet if the user does not already have one"() {
        given:
        CreateWalletRequest request = new CreateWalletRequest(1L)
        walletRepository.findWalletByUserId(1L) >> Optional.empty()

        when:
        def createdWallet = walletService.createWallet(request)

        then:
        1 * walletRepository.save(_) >> new Wallet(1L)
        createdWallet != null
        createdWallet.balance() == BigDecimal.ZERO
        println createdWallet
    }

    // Test error when wallet already exists for user
    // 사용자가 이미 지갑을 가진 경우 예외 발생 테스트
    def "Throws an exception if the user already has a wallet"() {
        given:
        CreateWalletRequest request = new CreateWalletRequest(1L)
        walletRepository.findWalletByUserId(1L) >>
                Optional.of(new Wallet(1L))

        when:
        def createdWallet = walletService.createWallet(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    // Test successful wallet retrieval
    // 지갑 조회 성공 테스트 (생성된 경우)
    def "Retrieves a wallet if it exists"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        wallet.balance = new BigDecimal(1000)
        walletRepository.findWalletByUserId(userId) >> Optional.of(wallet)

        when:
        def result = walletService.findWalletByUserId(userId)

        then:
        result != null
        result.balance() == new BigDecimal(1000)
        println result
    }

    // Test retrieval when wallet does not exist
    // 지갑이 존재하지 않을 때 조회 결과 테스트
    def "Returns null if the wallet does not exist"() {
        given:
        def userId = 1L
        walletRepository.findWalletByUserId(userId) >> Optional.empty()

        when:
        def result = walletService.findWalletByUserId(1L)
        then:
        result == null
    }

    // Test successful balance addition
    // 잔액 충전 성공 테스트
    def "Updates the balance when wallet exists and sufficient funds"() {
        given:
        def walletId = 1L
        def initialBalance = new BigDecimal("200.00")
        def addAmount = new BigDecimal("100.00")
        def wallet = new Wallet(walletId, walletId, initialBalance, LocalDateTime.now(), LocalDateTime.now())

        walletRepository.findById(walletId) >> Optional.of(wallet)

        when:
        def result = walletService.addBalance(new AddBalanceWalletRequest(1L, addAmount))

        then:
        result.balance() == new BigDecimal("300.00")
    }

    // Test exception when wallet does not exist
    // 지갑이 없을 경우 예외 테스트
    def "Throws an exception if the wallet does not exist"() {
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

    // Test exception when resulting balance would be negative
    // 충전 후 잔액이 마이너스가 될 경우 예외 테스트
    def "Throws an exception if balance goes below zero"() {
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

    // Test exception when balance exceeds the maximum limit
    // 잔액이 최대 한도를 초과할 경우 예외 테스트
    def "Throws an exception if balance exceeds the limit"() {
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
