package com.wallet.wallet_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
@Table(name = "ledger_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = "referenceId"))
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private Wallet wallet;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String entryType;

    @Setter
    @Column(nullable = false)
    private String referenceId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public WalletTransaction() {}

    public WalletTransaction(Wallet wallet, BigDecimal amount,
                             String entryType, String referenceId) {
        this.wallet = wallet;
        this.amount = amount;
        this.entryType = entryType;
        this.referenceId = referenceId;
    }

}
