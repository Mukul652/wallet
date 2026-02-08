package com.wallet.wallet_service.service;

import com.wallet.wallet_service.entity.*;
import com.wallet.wallet_service.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import com.wallet.wallet_service.entity.Wallet;
import com.wallet.wallet_service.entity.WalletTransaction;
import com.wallet.wallet_service.repository.WalletRepository;
import com.wallet.wallet_service.repository.WalletTransactionRepository;




@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository,
                         WalletTransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Wallet topUp(Long walletId, BigDecimal amount, String referenceId) {
        int retries = 3;

        while (retries-- > 0) {
            try {
                return performTopUp(walletId, amount, referenceId);
            } catch (org.springframework.dao.DeadlockLoserDataAccessException e) {
                if (retries == 0) throw e;
            }
        }
        throw new RuntimeException("Deadlock retry failed");
    }
    @Transactional
    public Wallet performTopUp(Long walletId, BigDecimal amount, String referenceId) {

        Wallet[] wallets = lockWalletsInOrder(walletId, 999L);

        Wallet userWallet = wallets[0].getId().equals(walletId)
                ? wallets[0]
                : wallets[1];

        Wallet systemWallet = wallets[0].getId().equals(999L)
                ? wallets[0]
                : wallets[1];

        try {
            transactionRepository.save(new WalletTransaction(systemWallet,
                    amount.negate(), "DEBIT", referenceId));

            transactionRepository.save(new WalletTransaction(userWallet,
                    amount, "CREDIT", referenceId));

            userWallet.setBalance(userWallet.getBalance().add(amount));
            systemWallet.setBalance(systemWallet.getBalance().subtract(amount));

            walletRepository.save(userWallet);
            walletRepository.save(systemWallet);
            BigDecimal ledgerBalance = calculateBalanceFromLedger(userWallet);
            userWallet.setBalance(ledgerBalance);
            walletRepository.save(userWallet);

            return userWallet;

        } catch (DataIntegrityViolationException e) {
            return userWallet;
        }
    }


//    @Transactional
//    public Wallet topUp(Long walletId, BigDecimal amount, String referenceId) {
//        Wallet[] wallets = lockWalletsInOrder(walletId, 999L);
//
//        Wallet userWallet = wallets[0].getId().equals(walletId)
//                ? wallets[0]
//                : wallets[1];
//
//        Wallet systemWallet = wallets[0].getId().equals(999L)
//                ? wallets[0]
//                : wallets[1];
//
//        try {
//            transactionRepository.save(new WalletTransaction(systemWallet,
//                    amount.negate(), "DEBIT", referenceId));
//
//            transactionRepository.save(new WalletTransaction(userWallet,
//                    amount, "CREDIT", referenceId));
//
//            userWallet.setBalance(userWallet.getBalance().add(amount));
//            systemWallet.setBalance(systemWallet.getBalance().subtract(amount));
//
//            walletRepository.save(userWallet);
//            walletRepository.save(systemWallet);
//
//            return userWallet;
//        } catch (DataIntegrityViolationException e) {
//            return userWallet;
//        }
//    }

    @Transactional
    public Wallet spend(Long walletId, BigDecimal amount, String referenceId) {
        int retries = 3;

        while (retries-- > 0) {
            try {
                return performSpend(walletId, amount, referenceId);
            } catch (org.springframework.dao.DeadlockLoserDataAccessException e) {
                if (retries == 0) throw e;
            }
        }
        throw new RuntimeException("Deadlock retry failed");
    }

    @Transactional
    public Wallet performSpend(Long walletId, BigDecimal amount, String referenceId) {

        Wallet[] wallets = lockWalletsInOrder(walletId, 999L);

        Wallet userWallet = wallets[0].getId().equals(walletId)
                ? wallets[0]
                : wallets[1];

        Wallet systemWallet = wallets[0].getId().equals(999L)
                ? wallets[0]
                : wallets[1];

        if (userWallet.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");

        try {
            transactionRepository.save(new WalletTransaction(userWallet,
                    amount.negate(), "DEBIT", referenceId));

            transactionRepository.save(new WalletTransaction(systemWallet,
                    amount, "CREDIT", referenceId));

            userWallet.setBalance(userWallet.getBalance().subtract(amount));
            systemWallet.setBalance(systemWallet.getBalance().add(amount));

            walletRepository.save(userWallet);
            walletRepository.save(systemWallet);
            BigDecimal ledgerBalance = calculateBalanceFromLedger(userWallet);
            userWallet.setBalance(ledgerBalance);
            walletRepository.save(userWallet);

            return userWallet;

        } catch (DataIntegrityViolationException e) {
            return userWallet;
        }
    }


    private Wallet[] lockWalletsInOrder(Long walletAId, Long walletBId) {
        if (walletAId < walletBId) {
            Wallet first = walletRepository.findWithLockById(walletAId).orElseThrow();
            Wallet second = walletRepository.findWithLockById(walletBId).orElseThrow();
            return new Wallet[]{first, second};
        } else {
            Wallet first = walletRepository.findWithLockById(walletBId).orElseThrow();
            Wallet second = walletRepository.findWithLockById(walletAId).orElseThrow();
            return new Wallet[]{first, second};
        }
    }

    public BigDecimal calculateBalanceFromLedger(Wallet wallet) {
        return transactionRepository
                .findByWalletOrderByCreatedAtDesc(wallet)
                .stream()
                .map(WalletTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
