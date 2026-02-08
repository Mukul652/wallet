package com.wallet.wallet_service.controller;

import com.wallet.wallet_service.entity.*;
import com.wallet.wallet_service.repository.*;
import com.wallet.wallet_service.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final WalletTransactionRepository transactionRepository;

    public WalletController(WalletRepository walletRepository,
                            WalletService walletService,
                            WalletTransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{walletId}/transactions")
    public Object transactions(@PathVariable Long walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        return transactionRepository.findByWalletOrderByCreatedAtDesc(wallet);
    }


    @GetMapping("/{walletId}")
    public BigDecimal balance(@PathVariable Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow()
                .getBalance();
    }

    @PostMapping("/{walletId}/topup")
    public Wallet topup(@PathVariable Long walletId,
                        @RequestParam BigDecimal amount,
                        @RequestParam String ref) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        return walletService.topUp(walletId, amount, ref);

    }

    @PostMapping("/{walletId}/bonus")
    public Wallet bonus(@PathVariable Long walletId,
                        @RequestParam BigDecimal amount,
                        @RequestParam String ref) {
        return walletService.bonus(walletId, amount, ref);
    }

    @PostMapping("/{walletId}/spend")
    public Wallet spend(@PathVariable Long walletId,
                        @RequestParam BigDecimal amount,
                        @RequestParam String ref) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
        return walletService.topUp(walletId, amount, ref);

    }
}
