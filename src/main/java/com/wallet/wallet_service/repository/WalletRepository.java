package com.wallet.wallet_service.repository;


import com.wallet.wallet_service.entity.Wallet;
import com.wallet.wallet_service.entity.User;
import com.wallet.wallet_service.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserAndAssetType(User user, AssetType assetType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findWithLockById(Long id);
}
