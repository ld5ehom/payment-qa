package com.ld5ehom.payment;

import com.ld5ehom.payment.wallet.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * This test class verifies wallet creation and balance updates in concurrent environments.
 * 이 테스트 클래스는 지갑 생성 및 충전이 동시 환경에서도 올바르게 동작하는지 검증합니다.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WalletServiceIntgTest {

    @Autowired
    WalletService walletService;
    @Autowired
    WalletRepository walletRepository;

    // Tests if a wallet is successfully created for a given user ID through the service layer
    // 서비스 레이어를 통해 주어진 사용자 ID에 대해 지갑이 성공적으로 생성되는지 테스트합니다.
    @Test
    @Transactional
    public void shouldCreateWallet() {
        // given
        CreateWalletRequest request = new CreateWalletRequest(1001L);

        // when
        CreateWalletResponse response = walletService.createWallet(request);

        // then
        Assertions.assertNotNull(response);
        System.out.println(response);
    }

    // Tests if wallet creation logic handles concurrent requests properly
    // 지갑 생성 로직이 동시에 여러 요청이 들어오는 경우를 올바르게 처리하는지 테스트합니다.
    @Test
    public void shouldHandleConcurrentWalletCreation() throws InterruptedException {
        Long userId = 10L;
        CreateWalletRequest request = new CreateWalletRequest(userId);

        int numOfThread = 20;
        ExecutorService service = Executors.newFixedThreadPool(numOfThread);
        AtomicInteger completedTasks = new AtomicInteger(0);

        for (int i = 0; i < numOfThread; i++) {
            service.submit(() -> {
                try {
                    walletService.createWallet(request);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    completedTasks.incrementAndGet();
                }
            });
        }

        service.shutdown();
        boolean finished = service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(finished);
        System.out.println(walletRepository.findAllByUserId(userId));
    }

    // Tests concurrent charging to the same wallet using in-memory logic
    // 동일한 지갑에 대해 Java 인메모리 로직으로 동시 충전이 잘 처리되는지 테스트합니다.
    @Test
    public void shouldHandleConcurrentCharging() throws InterruptedException {
        Long walletId = 1L;
        AddBalanceWalletRequest request = new AddBalanceWalletRequest(
                walletId, BigDecimal.TEN);

        int numOfThread = 100;
        ExecutorService service = Executors.newFixedThreadPool(numOfThread);
        AtomicInteger completedTasks = new AtomicInteger(0);

        for (int i = 0; i < numOfThread; i++) {
            service.submit(() -> {
                try {
                    walletService.addBalanceJava(request);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    completedTasks.incrementAndGet();
                }
            });
        }

        service.shutdown();
        boolean finished = service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println(finished);
        System.out.println(walletService.findWalletJava(walletId));
        Assertions.assertEquals(1000L,
                walletService.findWalletJava(walletId).balance().longValue());
    }
}
