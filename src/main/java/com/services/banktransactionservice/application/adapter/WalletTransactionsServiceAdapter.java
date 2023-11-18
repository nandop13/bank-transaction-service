package com.services.banktransactionservice.application.adapter;

import com.services.banktransactionservice.application.adapter.request.UpdateBalanceRequest;
import com.services.banktransactionservice.application.adapter.response.GetBalanceResponse;
import com.services.banktransactionservice.application.adapter.response.UpdateBalanceResponse;
import com.services.banktransactionservice.application.port.external.WalletTransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Internal implementation of the behavior to get and update the balance.
 */
@Service
@Slf4j
public class WalletTransactionsServiceAdapter implements WalletTransactionsService {

    @Value("${external.transaction.api.url}")
    private String transactionApiUrl;

    @Value("${external.balance.api.url}")
    private String getBalanceApiUrl;

    private final RestTemplate restTemplate;

    public WalletTransactionsServiceAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UpdateBalanceResponse updateBalance(Integer userId, BigDecimal amount) {
        // Prepare the request payload
        UpdateBalanceRequest walletUpdateRequest = new UpdateBalanceRequest();
        walletUpdateRequest.setUserId(userId);
        walletUpdateRequest.setAmount(amount);

        log.info("Calling update balance external service..");
        // Send the update request to the external wallet API
        UpdateBalanceResponse updateBalanceResponse = restTemplate.postForObject(transactionApiUrl, walletUpdateRequest, UpdateBalanceResponse.class);
        log.info(updateBalanceResponse.toString());

        return updateBalanceResponse;
    }

    @Override
    public GetBalanceResponse getBalance(Integer userId) {
        log.info("Calling get balance external service..");
        return restTemplate.getForObject(getBalanceApiUrl + "user_id=" + userId, GetBalanceResponse.class);
    }
}
