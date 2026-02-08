package com.wallet.wallet_service.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private AssetType assetType;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    public Wallet() {}

    public Wallet(User user, AssetType assetType, BigDecimal balance) {
        this.user = user;
        this.assetType = assetType;
        this.balance = balance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
