package com.services.banktransactionservice.application.port.external;

import com.services.banktransactionservice.application.adapter.response.PaymentResponse;
import com.services.banktransactionservice.domain.dto.BankAccountDTO;
import com.services.banktransactionservice.domain.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Define the behavior for Port communication to execute the payment.
 */
@Service
public interface ExternalPaymentService {
    PaymentResponse executePayment(BigDecimal amount, UserDTO sourceUser, UserDTO destinationUser, BankAccountDTO sourceBankAccountDto, BankAccountDTO destinationBankAccountDto);
}
