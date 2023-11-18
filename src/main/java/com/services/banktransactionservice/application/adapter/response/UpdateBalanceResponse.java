package com.services.banktransactionservice.application.adapter.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateBalanceResponse {
    @JsonProperty(value = "wallet_transaction_id")
    private Integer walletTransactionId;
    private BigDecimal amount;
    @JsonProperty(value = "user_id")
    private Integer userId;
}
