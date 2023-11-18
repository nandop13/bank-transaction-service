package com.services.banktransactionservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransactionRequestDTO {
    @JsonIgnore
    private Integer transactionRequestId;
    private UserDTO sourceUser;
    private UserDTO destinationUser;
    private BankAccountDTO sourceBankAccount;
    private BankAccountDTO destinationBankAccount;
    private BigDecimal amount;
}
