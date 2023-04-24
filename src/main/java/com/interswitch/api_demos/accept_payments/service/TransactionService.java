package com.interswitch.api_demos.accept_payments.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.interswitch.api_demos.accept_payments.dto.InitialTransactionResponse;
import com.interswitch.api_demos.accept_payments.dto.Transaction;
import com.interswitch.api_demos.accept_payments.dto.TransactionStatusResponse;
import com.interswitch.api_demos.misc.Utils;

import kong.unirest.Unirest;

@Service
public class TransactionService {

    @Value("${interswitch.transaction-status-url}")
    private String transactionStatusUrl;

    @Value("${interswitch.merchant-code}")
    public String merchantCode;

    private Transaction transaction;

    private Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private Utils utils;

    public Transaction createTransaction() {
        this.transaction = new Transaction();
        transaction.setReference(UUID.randomUUID().toString());
        transaction.setAmount(10000L); // 100 naira
        transaction.setDisplayAmount(utils.formatAmount(transaction.getAmount()));
        transaction.setCurrency("566");
        transaction.setPaymentItemId("Default_Payable_MX114253");
        transaction.setPaymentItemName("Perfume");
        transaction.setStatus(Transaction.Status.PENDING);
        return transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public TransactionStatusResponse confirmPayment(InitialTransactionResponse initialTransResponse) {
        TransactionStatusResponse transStatusResponse = new TransactionStatusResponse();
        try {
            if (!transaction.getReference().equals(initialTransResponse.getTxnref())) {
                transStatusResponse.setError("Invalid transaction reference");
                return transStatusResponse;
            }
            
            String json = Unirest.get(transactionStatusUrl)
                    .queryString("merchantcode", merchantCode)
                    .queryString("transactionreference", transaction.getReference())
                    .queryString("amount", transaction.getAmount())
                    .asString().getBody();
            utils.log("Transaction Status Response:\n" + json, logger);
            transStatusResponse = utils.jsonToObject(json, TransactionStatusResponse.class);

            // Check if the transaction was successful
            if (!"00".equals(transStatusResponse.getResponseCode())) {
                return transStatusResponse;
            }

            // Check that the transaction amount matches the original transaction amount
            if (transaction.getAmount().equals(transStatusResponse.getAmount())) {
                giveValueToCustomer();
                return transStatusResponse;
            } else {
                transStatusResponse.setError("Invalid transaction amount");
                return transStatusResponse;
            }
        } catch (Exception e) {
            transStatusResponse.setError("An error occurred while confirming transaction: " + e.getMessage());
            return transStatusResponse;
        }
    }

    private void giveValueToCustomer() {
        // implement logic to give the customer value for their payment
    }
}
