package com.services.banktransactionservice.infrastructure.controller;

import com.services.banktransactionservice.domain.dto.TransactionRequestDTO;
import com.services.banktransactionservice.domain.dto.response.ResultTransactionResponseDTO;
import com.services.banktransactionservice.domain.service.WalletServiceImpl;
import com.services.banktransactionservice.enums.TransactionStatus;
import com.services.banktransactionservice.exception.InsufficientBalanceException;
import com.services.banktransactionservice.exception.PaymentFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WalletControllerTest {
    @Mock
    private WalletServiceImpl walletService;

    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void withdraw_withSuccessfulTransaction_shouldReturnOkResponse() {
        TransactionRequestDTO request = new TransactionRequestDTO();
        ResultTransactionResponseDTO expectedResult = new ResultTransactionResponseDTO();
        when(walletService.withdraw(any(), any(), any(), any(), any())).thenReturn(expectedResult);

        // Act
        ResponseEntity<ResultTransactionResponseDTO> response = walletController.withdraw(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());

        // Verify that the withdraw method on WalletService was called with the expected parameters
        verify(walletService).withdraw(request.getSourceUser(), request.getDestinationUser(),
                request.getSourceBankAccount(), request.getDestinationBankAccount(), request.getAmount());
    }

    @Test
    void withdraw_withInsufficientBalanceException_shouldReturnBadRequestResponse() {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        when(walletService.withdraw(any(), any(), any(), any(), any())).thenThrow(new InsufficientBalanceException("Insufficient funds"));

        // Act
        ResponseEntity<ResultTransactionResponseDTO> response = walletController.withdraw(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TransactionStatus.FAILED, response.getBody().getTransactionStatus());
        assertEquals("Insufficient funds", response.getBody().getMessage());

        // Verify that the withdraw method on WalletService was called with the expected parameters
        verify(walletService).withdraw(request.getSourceUser(), request.getDestinationUser(),
                request.getSourceBankAccount(), request.getDestinationBankAccount(), request.getAmount());
    }

    @Test
    void withdraw_withPaymentFailedException_shouldReturnInternalServerErrorResponse() {
        // Arrange
        TransactionRequestDTO request = new TransactionRequestDTO();
        when(walletService.withdraw(any(), any(), any(), any(), any())).thenThrow(new PaymentFailedException("Payment failed"));

        // Act
        ResponseEntity<ResultTransactionResponseDTO> response = walletController.withdraw(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(TransactionStatus.FAILED, response.getBody().getTransactionStatus());
        assertEquals("Payment failed due to internal error", response.getBody().getMessage());

        // Verify that the withdraw method on WalletService was called with the expected parameters
        verify(walletService).withdraw(request.getSourceUser(), request.getDestinationUser(),
                request.getSourceBankAccount(), request.getDestinationBankAccount(), request.getAmount());
    }


}