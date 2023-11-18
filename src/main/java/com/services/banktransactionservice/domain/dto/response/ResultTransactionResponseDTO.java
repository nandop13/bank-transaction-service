package com.services.banktransactionservice.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.services.banktransactionservice.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResultTransactionResponseDTO {
    private String message;
    private TransactionStatus transactionStatus;
    private UUID transactionId;
    private BigDecimal fee;
    private BigDecimal totalAmountSent;
    private Integer walletTransactionId;
    private Integer userId;
}
