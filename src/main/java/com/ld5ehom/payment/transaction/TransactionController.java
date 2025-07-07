package com.ld5ehom.payment.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles endpoints related to transaction operations such as charge and payment.
 * 이 컨트롤러는 충전 및 결제와 같은 트랜잭션 작업과 관련된 엔드포인트를 처리합니다.
 */
@RequiredArgsConstructor
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Endpoint for charging the wallet balance.
     * 지갑 잔액을 충전하는 엔드포인트입니다.
     */
    @PostMapping("/api/balance/charge")
    public ChargeTransactionResponse charge(
            @RequestBody ChargeTransactionRequest request
    ) {
        return transactionService.charge(request);
    }

    /**
     * Endpoint for processing a payment from the wallet.
     * 지갑에서 결제를 처리하는 엔드포인트입니다.
     */
    @PostMapping("/api/balance/payment")
    public PaymentTransactionResponse payment(
            @RequestBody PaymentTransactionRequest request
    ) {
        return transactionService.payment(request);
    }
}
