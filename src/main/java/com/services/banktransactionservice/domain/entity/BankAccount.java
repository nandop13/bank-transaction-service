package com.services.banktransactionservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @Id
    private Integer userId;
    private String routingNumber;
    private String accountNumber;
    private String currency;
}
