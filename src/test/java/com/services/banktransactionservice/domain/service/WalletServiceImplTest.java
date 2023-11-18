package com.services.banktransactionservice.domain.service;

import com.services.banktransactionservice.application.adapter.response.GetBalanceResponse;
import com.services.banktransactionservice.application.adapter.response.PaymentResponse;
import com.services.banktransactionservice.application.adapter.response.UpdateBalanceResponse;
import com.services.banktransactionservice.application.port.external.ExternalPaymentService;
import com.services.banktransactionservice.application.port.external.WalletTransactionsService;
import com.services.banktransactionservice.domain.dto.BankAccountDTO;
import com.services.banktransactionservice.domain.dto.UserDTO;
import com.services.banktransactionservice.domain.dto.response.PaymentInfoDTO;
import com.services.banktransactionservice.domain.dto.response.RequestInfoDTO;
import com.services.banktransactionservice.domain.dto.response.ResultTransactionResponseDTO;
import com.services.banktransactionservice.enums.SourceType;
import com.services.banktransactionservice.enums.TransactionStatus;
import com.services.banktransactionservice.exception.InsufficientBalanceException;
import com.services.banktransactionservice.exception.PaymentFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WalletServiceImplTest {
    @Mock
    private ExternalPaymentService externalPaymentService;

    @Mock
    private WalletTransactionsService walletTransactionsService;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void withdraw_withSufficientBalance_shouldReturnTransactionResponse() {
        // Arrange
        UserDTO sourceUser = new UserDTO(123, "Frank", "Lampard", "ABC987", SourceType.PERSON);
        UserDTO destinationUser = new UserDTO(456, "Joe", "Cole", "ABC654", SourceType.PERSON);
        BankAccountDTO sourceBankAccountDto = new BankAccountDTO(123, "456", "789", "USD");
        BankAccountDTO destinationBankAccountDto = new BankAccountDTO(456, "789", "012", "USD");
        BigDecimal amount = new BigDecimal("50");

        GetBalanceResponse getBalanceResponse = new GetBalanceResponse(BigDecimal.valueOf(100), 456);
        when(walletTransactionsService.getBalance(destinationUser.getUserId())).thenReturn(getBalanceResponse);

        PaymentResponse paymentResponse = new PaymentResponse(RequestInfoDTO.builder().status("COMPLETED").build(), PaymentInfoDTO.builder().amount(amount).build());
        when(externalPaymentService.executePayment(any(), any(), any(), any(), any())).thenReturn(paymentResponse);

        UpdateBalanceResponse updateBalanceResponse = new UpdateBalanceResponse(852, amount, 456);
        when(walletTransactionsService.updateBalance(any(), any())).thenReturn(updateBalanceResponse);

        // Act
        ResultTransactionResponseDTO resultTransactionResponse = walletService.withdraw(
                sourceUser, destinationUser, sourceBankAccountDto, destinationBankAccountDto, amount);

        // Assert
        assertNotNull(resultTransactionResponse);
        assertEquals(TransactionStatus.COMPLETED, resultTransactionResponse.getTransactionStatus());
        assertEquals(BigDecimal.valueOf(55.0), resultTransactionResponse.getTotalAmountSent());
        assertEquals(BigDecimal.valueOf(5.0), resultTransactionResponse.getFee());
        assertEquals(852, resultTransactionResponse.getWalletTransactionId());
        assertEquals(456, resultTransactionResponse.getUserId());

        // Verify that the required methods were called
        verify(walletTransactionsService).getBalance(destinationUser.getUserId());
        verify(externalPaymentService).executePayment(amount, sourceUser, destinationUser, sourceBankAccountDto, destinationBankAccountDto);
        verify(walletTransactionsService).updateBalance(destinationUser.getUserId(), amount.multiply(BigDecimal.valueOf(2)));
    }

    @Test
    void withdraw_withInsufficientBalance_shouldThrowException() {
        UserDTO sourceUser = new UserDTO(123, "Frank", "Lampard", "ABC987", SourceType.PERSON);
        UserDTO destinationUser = new UserDTO(456, "Joe", "Cole", "ABC654", SourceType.PERSON);
        BankAccountDTO sourceBankAccountDto = new BankAccountDTO(123, "456", "789", "USD");
        BankAccountDTO destinationBankAccountDto = new BankAccountDTO(456, "789", "012", "USD");
        BigDecimal amount = new BigDecimal("150.00");

        GetBalanceResponse getBalanceResponse = new GetBalanceResponse(BigDecimal.valueOf(100.00), 456);
        when(walletTransactionsService.getBalance(destinationUser.getUserId())).thenReturn(getBalanceResponse);

        assertThrows(InsufficientBalanceException.class,
                () -> walletService.withdraw(sourceUser, destinationUser, sourceBankAccountDto, destinationBankAccountDto, amount));

        // Verify that the required methods were called
        verify(walletTransactionsService).getBalance(destinationUser.getUserId());
    }

    @Test
    void withdraw_withPaymentFailure_shouldThrowException() {
        UserDTO destinationUser = new UserDTO(456, "Joe", "Cole", "ABC654", SourceType.PERSON);
        BankAccountDTO sourceBankAccountDto = new BankAccountDTO(123, "456", "789", "USD");
        BankAccountDTO destinationBankAccountDto = new BankAccountDTO(456, "789", "012", "USD");
        BigDecimal amount = new BigDecimal("50.00");

        GetBalanceResponse getBalanceResponse = new GetBalanceResponse(BigDecimal.valueOf(100.00), 456);
        when(walletTransactionsService.getBalance(destinationUser.getUserId())).thenReturn(getBalanceResponse);

        when(externalPaymentService.executePayment(any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Payment execution failed"));

        assertThrows(PaymentFailedException.class,
                () -> walletService.withdraw(null, destinationUser, sourceBankAccountDto, destinationBankAccountDto, amount));

        // Verify that the required methods were called
        verify(walletTransactionsService).getBalance(destinationUser.getUserId());
        verify(externalPaymentService).executePayment(amount, null, destinationUser, sourceBankAccountDto, destinationBankAccountDto);
    }
}