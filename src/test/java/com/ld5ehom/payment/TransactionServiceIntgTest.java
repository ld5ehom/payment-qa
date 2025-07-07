package com.ld5ehom.payment;

import com.ld5ehom.payment.transaction.PaymentTransactionRequest;
import com.ld5ehom.payment.transaction.PaymentTransactionResponse;
import com.ld5ehom.payment.transaction.TransactionService;
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

/**
 * This class tests the payment transaction flow via TransactionService.
 * 이 클래스는 TransactionService를 통해 결제 트랜잭션 흐름을 테스트합니다.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceIntgTest {

    @Autowired
    TransactionService transactionService;

    /**
     * Tests the creation of a payment transaction and asserts the result is not null.
     * 결제 트랜잭션을 생성하고 결과가 null이 아님을 확인합니다.
     */
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
}
