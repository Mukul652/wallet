package com.wallet.wallet_service.repository;


import com.wallet.wallet_service.entity.WalletTransaction;
import com.wallet.wallet_service.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Optional<WalletTransaction> findByReferenceId(String referenceId);

    List<WalletTransaction> findByWalletOrderByCreatedAtDesc(Wallet wallet);
}
