package com.wallet.wallet_service.repository;

import com.wallet.wallet_service.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {
    Optional<AssetType> findByName(String name);
}
