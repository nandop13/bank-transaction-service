package com.services.banktransactionservice.domain.mapper;

import com.services.banktransactionservice.domain.dto.TransactionDTO;
import com.services.banktransactionservice.domain.entity.Transaction;

public class TransactionMapper {
    public static TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .amount(transaction.getAmount())
                .fee(transaction.getFee())
                .status(transaction.getStatus())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

}
