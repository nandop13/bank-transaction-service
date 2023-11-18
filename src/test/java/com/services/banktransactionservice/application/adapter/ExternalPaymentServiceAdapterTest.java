package com.services.banktransactionservice.application.adapter;

import com.services.banktransactionservice.application.adapter.request.PaymentRequest;
import com.services.banktransactionservice.application.adapter.response.PaymentResponse;
import com.services.banktransactionservice.domain.dto.BankAccountDTO;
import com.services.banktransactionservice.domain.dto.UserDTO;
import com.services.banktransactionservice.domain.dto.response.*;
import com.services.banktransactionservice.enums.SourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ExternalPaymentServiceAdapterTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalPaymentServiceAdapter externalPaymentServiceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(externalPaymentServiceAdapter, "paymentApiUrl", "mock.url.com");
    }

    @Test
    void testExecutePayment_Success() {
        BigDecimal amount = BigDecimal.TEN;
        UserDTO sourceUser = new UserDTO(123, "Frank", "Lampard", "ABC987", SourceType.PERSON);
        UserDTO destinationUser = new UserDTO(456, "Joe", "Cole", "ABC654", SourceType.PERSON);
        BankAccountDTO sourceBankAccountDto = new BankAccountDTO(1, "123", "456", "USD");
        BankAccountDTO destinationBankAccountDto = new BankAccountDTO(2, "789", "012", "EUR");

        SourceInformationDTO sourceInformation = SourceInformationDTO.builder().name(sourceUser.getName()).build();
        PaymentRequest expectedPaymentRequest = PaymentRequest.builder()
                .source(SourceDTO.builder().type(SourceType.COMPANY.getDescription())
                        .sourceInformation(sourceInformation)
                        .bankAccountDTO(sourceBankAccountDto).build())
                .destination(DestinationDTO.builder()
                        .name(destinationUser.getName())
                        .bankAccountDTO(destinationBankAccountDto)
                        .build())
                .amount(amount)
                .build();

        PaymentResponse expectedPaymentResponse = new PaymentResponse(RequestInfoDTO.builder().status("COMPLETED").build(), PaymentInfoDTO.builder().id(UUID.randomUUID()).amount(amount).build());

        when(restTemplate.postForObject(anyString(), any(), eq(PaymentResponse.class)))
                .thenReturn(expectedPaymentResponse);

        // Act
        PaymentResponse actualPaymentResponse = externalPaymentServiceAdapter.executePayment(amount, sourceUser, destinationUser,
                sourceBankAccountDto, destinationBankAccountDto);

        // Assert
        verify(restTemplate, times(1)).postForObject(eq("mock.url.com"), any(), eq(PaymentResponse.class));
        assertEquals(expectedPaymentResponse, actualPaymentResponse);
    }

}