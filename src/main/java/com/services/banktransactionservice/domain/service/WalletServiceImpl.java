package com.services.banktransactionservice.domain.service;

import com.services.banktransactionservice.application.adapter.response.GetBalanceResponse;
import com.services.banktransactionservice.application.adapter.response.PaymentResponse;
import com.services.banktransactionservice.application.adapter.response.UpdateBalanceResponse;
import com.services.banktransactionservice.application.port.WalletService;
import com.services.banktransactionservice.application.port.external.ExternalPaymentService;
import com.services.banktransactionservice.application.port.external.WalletTransactionsService;
import com.services.banktransactionservice.domain.dto.BankAccountDTO;
import com.services.banktransactionservice.domain.dto.UserDTO;
import com.services.banktransactionservice.domain.dto.response.ResultTransactionResponseDTO;
import com.services.banktransactionservice.enums.TransactionStatus;
import com.services.banktransactionservice.exception.InsufficientBalanceException;
import com.services.banktransactionservice.exception.PaymentFailedException;
import com.services.banktransactionservice.infrastructure.persistence.UserRepository;
import com.services.banktransactionservice.infrastructure.persistence.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service to centralize all the logic to perform a withdraw, including getting the balance, executePayment and updateBalance.
 */
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ExternalPaymentService externalPaymentService;
    private final WalletTransactionsService walletTransactionsService;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository,
                             UserRepository userRepository,
                             ExternalPaymentService externalPaymentService,
                             WalletTransactionsService walletTransactionsService) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.externalPaymentService = externalPaymentService;
        this.walletTransactionsService = walletTransactionsService;
    }

    @Override
    public ResultTransactionResponseDTO withdraw(UserDTO sourceUser, UserDTO destinationUser, BankAccountDTO sourceBankAccountDto, BankAccountDTO destinationBankAccountDto, BigDecimal amount) {
        GetBalanceResponse balanceResponse = walletTransactionsService.getBalance(destinationUser.getUserId());
        BigDecimal currentBalance = balanceResponse.getBalance();
        BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.1));
        BigDecimal totalAmount = amount.add(fee);
        PaymentResponse paymentResponse;
        UpdateBalanceResponse updateBalanceResponse;

        if (currentBalance.compareTo(totalAmount) <= 0) {
            throw new InsufficientBalanceException("Insufficient balance in the wallet.");
        }

        try {
            // Execute payment to recipient's bank
            paymentResponse = externalPaymentService.executePayment(amount, sourceUser, destinationUser, sourceBankAccountDto, destinationBankAccountDto);
        } catch (RuntimeException exception) {
            throw new PaymentFailedException("Error trying to execute the payment", exception.getCause());
        }

        try {
            // Update wallet balance
            updateBalanceResponse = walletTransactionsService.updateBalance(destinationUser.getUserId(), amount.multiply(BigDecimal.valueOf(2)));
        } catch (RuntimeException exception) {
            throw new PaymentFailedException("Error trying to update the wallet balance", exception.getCause());
        }

        return ResultTransactionResponseDTO.builder().transactionId(paymentResponse.getPaymentInfo().getId()).
                transactionStatus(TransactionStatus.COMPLETED).
                totalAmountSent(totalAmount).
                fee(fee).
                walletTransactionId(updateBalanceResponse.getWalletTransactionId()).
                userId(updateBalanceResponse.getUserId()).
                build();
    }
}
