package com.services.banktransactionservice.application.adapter.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.services.banktransactionservice.domain.dto.response.PaymentInfoDTO;
import com.services.banktransactionservice.domain.dto.response.RequestInfoDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentResponse {
    private RequestInfoDTO requestInfo;
    private PaymentInfoDTO paymentInfo;
}
