package com.interswitch.api_demos.accept_payments.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.interswitch.api_demos.accept_payments.dto.InitialTransactionResponse;
import com.interswitch.api_demos.accept_payments.dto.Transaction;
import com.interswitch.api_demos.accept_payments.dto.TransactionStatusResponse;
import com.interswitch.api_demos.accept_payments.service.TransactionService;

@Controller
@RequestMapping("/accept-payments")
public class AcceptPaymentsController {

    public static final String ACCEPT_PAYMENTS = "accept-payments/";

    @Autowired
    private TransactionService transactionService;

    @Value("${interswitch.merchant-code}")
    public String merchantCode;

    @GetMapping("/inline-checkout")
    public String inlineCheckout(Model model) {
        model.addAttribute("merchantCode", merchantCode);
        model.addAttribute("trans", transactionService.createTransaction());
        return ACCEPT_PAYMENTS + "inline-checkout";
    }

    @GetMapping("/web-redirect")
    public String webRedirect(Model model) {
        model.addAttribute("merchantCode", merchantCode);
        model.addAttribute("trans", transactionService.createTransaction());
        return ACCEPT_PAYMENTS + "web-redirect";
    }

    @GetMapping("/pay-bill")
    public String payWithUssd(Model model) {
        Transaction transaction = transactionService.createTransaction();
        model.addAttribute("trans", transaction);
        model.addAttribute("payBillUrl", transactionService.createLink(transaction));
        return ACCEPT_PAYMENTS + "pay-bill";
    }

    // Called by the payment gateway during wed-redirect flow
    @PostMapping("/callback")
    public String processCallback(HttpServletRequest request, Model model) {
        InitialTransactionResponse initialTransResponse = new InitialTransactionResponse();
        initialTransResponse.setTxnref(request.getParameter("txnref"));
        TransactionStatusResponse transStatusResponse = transactionService.confirmPayment(initialTransResponse);

        model.addAttribute("trans", transactionService.getTransaction());
        model.addAttribute("transStatus", transStatusResponse);
        return ACCEPT_PAYMENTS + "status";
    }
}