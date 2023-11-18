package com.services.banktransactionservice.domain.mapper;

import com.services.banktransactionservice.domain.dto.BankAccountDTO;
import com.services.banktransactionservice.domain.entity.BankAccount;

public class BankAccountMapper {
    public static BankAccountDTO toDTO(BankAccount userBankDetails) {
        return BankAccountDTO.builder()
                .routingNumber(userBankDetails.getRoutingNumber())
                .accountNumber(userBankDetails.getAccountNumber())
                .build();
    }

    public static BankAccount toEntity(BankAccount userBankDetailsDTO) {
        return BankAccount.builder()
                .routingNumber(userBankDetailsDTO.getRoutingNumber())
                .accountNumber(userBankDetailsDTO.getAccountNumber())
                .build();
    }
}
