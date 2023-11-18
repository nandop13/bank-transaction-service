package com.services.banktransactionservice.infrastructure.controller;

import com.services.banktransactionservice.application.port.WalletService;
import com.services.banktransactionservice.domain.dto.TransactionRequestDTO;
import com.services.banktransactionservice.domain.dto.response.ResultTransactionResponseDTO;
import com.services.banktransactionservice.domain.service.WalletServiceImpl;
import com.services.banktransactionservice.enums.TransactionStatus;
import com.services.banktransactionservice.exception.InsufficientBalanceException;
import com.services.banktransactionservice.exception.PaymentFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to perform withdraw from a wallet to a bank account.
 */
@RestController
@Slf4j
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletServiceImpl walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ResultTransactionResponseDTO> withdraw(@RequestBody TransactionRequestDTO request) {
        try {
            ResultTransactionResponseDTO resultTransaction = walletService.withdraw(request.getSourceUser(), request.getDestinationUser(), request.getSourceBankAccount(), request.getDestinationBankAccount(), request.getAmount());
            resultTransaction.setMessage("Withdrawal successful from Wallet to Bank Account");

            return new ResponseEntity<>(resultTransaction, HttpStatus.OK);

        } catch (InsufficientBalanceException e) {
            return new ResponseEntity<>(ResultTransactionResponseDTO.builder().transactionStatus(TransactionStatus.FAILED).message("Insufficient funds").build(), HttpStatus.BAD_REQUEST);
        } catch (PaymentFailedException e) {
            return new ResponseEntity<>(ResultTransactionResponseDTO.builder().transactionStatus(TransactionStatus.FAILED).message("Payment failed due to internal error").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
