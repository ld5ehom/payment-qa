package com.ld5ehom.payment;

import com.ld5ehom.payment.transaction.*;
import com.ld5ehom.payment.wallet.CreateWalletRequest;
import com.ld5ehom.payment.wallet.CreateWalletResponse;
import com.ld5ehom.payment.wallet.WalletService;
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

/**
 * This class tests the payment and charge transaction flows via TransactionService.
 * 이 클래스는 TransactionService를 통해 결제 및 충전 트랜잭션 흐름을 테스트합니다.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceIntgTest {

    @Autowired
    TransactionService transactionService;

    // Creates a new payment transaction and checks for successful response
    // 결제 트랜잭션을 생성하고 정상 응답을 확인합니다.
    @Test
    @Transactional
    public void createPaymentTransaction() {
        // given
        PaymentTransactionRequest request = new PaymentTransactionRequest(
                1L, "course-1", new BigDecimal(10)
        );

        // when
        PaymentTransactionResponse response = transactionService.payment(request);

        // then
        Assertions.assertNotNull(response);
        System.out.println(response);
    }

    // Creates a new charge transaction and prints the result
    // 충전 트랜잭션을 생성하고 결과를 출력합니다.
    @Test
    public void performCharge() throws InterruptedException {
        Long userId = 2L;
        String orderId = "orderId-1";

        // given
        ChargeTransactionRequest chargeTransactionRequest = new ChargeTransactionRequest(
                userId, orderId, BigDecimal.TEN
        );

        // when
        ChargeTransactionResponse response = transactionService.charge(chargeTransactionRequest);

        // then
        System.out.println(response);
    }

    // Simulates concurrent charge requests with the same orderId
    // 동일한 orderId로 동시에 충전 요청을 시뮬레이션합니다.
    @Test
    public void executeConcurrentCharges() throws InterruptedException {
        Long userId = 2L;
        String orderId = "orderId-2";

        // Request
        ChargeTransactionRequest chargeTransactionRequest = new ChargeTransactionRequest(
                userId, orderId, BigDecimal.TEN
        );

        int numOfThread = 20;
        ExecutorService service = Executors.newFixedThreadPool(numOfThread);
        AtomicInteger completedTasks = new AtomicInteger(0);

        // Submit multiple concurrent charge requests using the same orderId
        // 동일한 orderId를 사용하여 여러 개의 충전 요청을 동시에 전송합니다.
        for (int i = 0; i < numOfThread; i++) {
            service.submit(() -> {
                try {
                    ChargeTransactionResponse response = transactionService.charge(chargeTransactionRequest);
                    System.out.println(response);
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
    }
}
