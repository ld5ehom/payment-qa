package com.ld5ehom.payment;

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

/*
 * This test class verifies the wallet creation logic through an integration test using JUnit 5.
 * 이 테스트 클래스는 JUnit 5 기반 통합 테스트를 통해 지갑 생성 로직을 검증합니다.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WalletServiceIntgTest {

    @Autowired
    WalletService walletService;

    @Test
    @Transactional
    // Tests if a wallet is successfully created for a given user ID through the service layer.
    // 서비스 레이어를 통해 주어진 사용자 ID에 대해 지갑이 성공적으로 생성되는지 테스트합니다.(지갑을생성한다)
    public void shouldCreateWallet() {
        // given
        CreateWalletRequest request = new CreateWalletRequest(1001L);

        // when
        CreateWalletResponse response = walletService.createWallet(request);

        // then
        Assertions.assertNotNull(response);
        System.out.println(response);
    }
}
