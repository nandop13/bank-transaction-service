package com.services.banktransactionservice.application.adapter.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.services.banktransactionservice.domain.dto.response.DestinationDTO;
import com.services.banktransactionservice.domain.dto.response.SourceDTO;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentRequest {
    private SourceDTO source;
    private DestinationDTO destination;
    private BigDecimal amount;
}
