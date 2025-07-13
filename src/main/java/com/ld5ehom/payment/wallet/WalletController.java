package com.ld5ehom.payment.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/*
 * This controller exposes wallet-related HTTP endpoints for creating, retrieving, and updating wallet balance.
 * 이 컨트롤러는 지갑 생성, 조회, 잔액 변경을 위한 HTTP 엔드포인트를 제공합니다.
 */
@RequiredArgsConstructor
@RestController
public class WalletController {
    private final WalletService walletService;

    // Creates a new wallet for a user
    // 사용자를 위한 새 지갑을 생성합니다.
    @PostMapping("/api/wallets")
    public CreateWalletResponse createWallet(@RequestBody CreateWalletRequest request) {
        return walletService.createWallet(request);
    }

    // Retrieves the wallet of a specific user by userId
    // 특정 사용자의 지갑 정보를 userId로 조회합니다.
    @GetMapping("/api/users/{userId}/wallets")
    public FindWalletResponse findWalletByUserId(
            @PathVariable("userId") Long userId) {
        return walletService.findWalletByUserId(userId);
    }
}
