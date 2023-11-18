package com.services.banktransactionservice.application.port.external;

import com.services.banktransactionservice.application.adapter.response.GetBalanceResponse;
import com.services.banktransactionservice.application.adapter.response.UpdateBalanceResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Define the behavior for Port communication to get and update the balance.
 */
@Service
public interface WalletTransactionsService {
    UpdateBalanceResponse updateBalance(Integer userId, BigDecimal amount);
    GetBalanceResponse getBalance(Integer userId);
}

