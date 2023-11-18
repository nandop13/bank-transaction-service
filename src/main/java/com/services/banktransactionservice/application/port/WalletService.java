package com.services.banktransactionservice.application.port;

import com.services.banktransactionservice.domain.dto.BankAccountDTO;
import com.services.banktransactionservice.domain.dto.UserDTO;
import com.services.banktransactionservice.domain.dto.response.ResultTransactionResponseDTO;

import java.math.BigDecimal;

/**
 * Define the behavior for Port internal communication to perform the withdraw.
 */
public interface WalletService {
    ResultTransactionResponseDTO withdraw(UserDTO sourceUser, UserDTO destinationUser , BankAccountDTO sourceBankAccount, BankAccountDTO destinationBankAccount, BigDecimal amount);

}
