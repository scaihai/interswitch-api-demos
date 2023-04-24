package com.interswitch.api_demos.accept_payments.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interswitch.api_demos.accept_payments.dto.InitialTransactionResponse;
import com.interswitch.api_demos.accept_payments.dto.TransactionStatusResponse;
import com.interswitch.api_demos.accept_payments.service.TransactionService;
import com.interswitch.api_demos.misc.Utils;

@RestController
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private Utils utils;

    /**
     * The front-end calls this endpoint to confirm the status of the transaction
     * before giving value to the customer
     */
    @PostMapping("/confirm-payment")
    public TransactionStatusResponse confirmPayment(@RequestBody String json) {
        utils.log("Confirm Payment Request: \n" + json, logger);
        InitialTransactionResponse initialTransResponse = utils.jsonToObject(json, InitialTransactionResponse.class);
        return transactionService.confirmPayment(initialTransResponse);
    }
}
