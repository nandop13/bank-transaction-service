package com.services.banktransactionservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    @Id
    private Integer walletId;
    private BigDecimal balance;
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserTransaction userTransaction;
}
