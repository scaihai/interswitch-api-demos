package com.interswitch.api_demos.accept_payments.service;

import java.util.TreeSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.interswitch.api_demos.accept_payments.dto.AccessTokenResponse;
import com.interswitch.api_demos.accept_payments.dto.CreateBillResponse;
import com.interswitch.api_demos.accept_payments.dto.InitialTransactionResponse;
import com.interswitch.api_demos.accept_payments.dto.Transaction;
import com.interswitch.api_demos.accept_payments.dto.TransactionStatusResponse;
import com.interswitch.api_demos.misc.Utils;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

@Service
public class TransactionService {

    @Value("${interswitch.transaction-status-url}")
    private String transactionStatusUrl;

    @Value("${interswitch.pay-bill-url}")
    private String payBillUrl;

    @Value("${interswitch.access-token-url}")
    private String accessTokenUrl;

    @Value("${interswitch.merchant-code}")
    public String merchantCode;

    @Value("${interswitch.payment-item-id}")
    private String paymentItemId;

    @Value("${interswitch.client-id}")
    private String clientId;

    @Value("${interswitch.secret-key}")
    private String secretKey;

    @Autowired
    private Utils utils;

    private Transaction transaction;

    private Logger logger = LoggerFactory.getLogger(TransactionService.class);

//    static {
//        Unirest.config().proxy("172.16.10.20", 8080);
//        Unirest.config().verifySsl(false);
//    }

    public Transaction createTransaction() {
        transaction = new Transaction();
        transaction.setReference(UUID.randomUUID().toString());
        transaction.setAmount(10000L); // 100 naira
        transaction.setDisplayAmount(utils.formatAmount(transaction.getAmount()));
        transaction.setCurrency("566");
        transaction.setPaymentItemId(paymentItemId);
        transaction.setPaymentItemName("Perfume");
        transaction.setStatus(Transaction.Status.PENDING);
        return transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public String createLink(Transaction transaction) {
        JSONObject json = new JSONObject();
        json.put("merchantCode", merchantCode);
        json.put("payableCode", paymentItemId);
        json.put("amount", transaction.getAmount());
        json.put("currencyCode", transaction.getCurrency());
        json.put("redirectUrl", "http://localhost:8080/callback");
        json.put("customerId", "johndoe@gmail.com");
        json.put("customerEmail", "johndoe@gmail.com");

        String response = Unirest.post(payBillUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(json).asString().getBody();
        return utils.jsonToObject(response, CreateBillResponse.class).getPaymentUrl();
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

    private String getAccessToken() {
        byte[] credentials = (clientId + ":" + secretKey).getBytes();
        String base64Encoded = utils.base64Encode(credentials);
        String authorization = "Basic " + base64Encoded;
        System.out.println(">>> About to get access token: " + authorization);
        System.out.println(">>> URL: " + accessTokenUrl);
        String response = Unirest.post(accessTokenUrl)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .field("grant_type", "client_credentials")
                .asString().getBody();
        System.out.println("Access token: " + response);
        return utils.jsonToObject(response, AccessTokenResponse.class).getAccessToken();
    }
}
