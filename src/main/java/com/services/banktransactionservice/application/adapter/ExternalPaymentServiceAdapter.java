package com.services.banktransactionservice.application.adapter;

import com.services.banktransactionservice.application.adapter.request.PaymentRequest;
import com.services.banktransactionservice.application.adapter.response.PaymentResponse;
import com.services.banktransactionservice.application.port.external.ExternalPaymentService;
import com.services.banktransactionservice.domain.dto.BankAccountDTO;
import com.services.banktransactionservice.domain.dto.UserDTO;
import com.services.banktransactionservice.domain.dto.response.DestinationDTO;
import com.services.banktransactionservice.domain.dto.response.SourceDTO;
import com.services.banktransactionservice.domain.dto.response.SourceInformationDTO;
import com.services.banktransactionservice.enums.SourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Internal implementation of the behavior for Port communication to execute the payment.
 */
@Service
@Slf4j
public class ExternalPaymentServiceAdapter implements ExternalPaymentService {
    @Value("${external.payment.api.url}")
    private String paymentApiUrl;
    private final RestTemplate restTemplate;

    public ExternalPaymentServiceAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentResponse executePayment(BigDecimal amount, UserDTO sourceUserDto, UserDTO destinationUserDto , BankAccountDTO sourceBankAccountDto, BankAccountDTO destinationBankAccountDto) {
        SourceInformationDTO sourceInformation = SourceInformationDTO.builder().name(sourceUserDto.getName()).build();

        PaymentRequest paymentRequest = PaymentRequest.builder().
                source(SourceDTO.builder().type(SourceType.COMPANY.getDescription()).
                        sourceInformation(sourceInformation).
                        bankAccountDTO(sourceBankAccountDto).build()).
                destination(DestinationDTO.builder().
                        name(destinationUserDto.getName()).
                        bankAccountDTO(destinationBankAccountDto)
                        .build()).
                amount(amount)
                .build();

        log.info("Calling payment external service..");

        // Send the payment request to the external payment API
        PaymentResponse paymentResponse = restTemplate.postForObject(paymentApiUrl, paymentRequest, PaymentResponse.class);
        log.info(paymentResponse.toString());

        return paymentResponse;
    }
}
