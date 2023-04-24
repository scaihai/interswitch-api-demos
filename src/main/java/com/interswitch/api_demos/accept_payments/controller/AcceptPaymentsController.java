package com.interswitch.api_demos.accept_payments.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.interswitch.api_demos.accept_payments.dto.Card;
import com.interswitch.api_demos.accept_payments.dto.InitialTransactionResponse;
import com.interswitch.api_demos.accept_payments.dto.TransactionStatusResponse;
import com.interswitch.api_demos.accept_payments.service.CardService;
import com.interswitch.api_demos.accept_payments.service.TransactionService;

@Controller
@RequestMapping("/accept-payments")
public class AcceptPaymentsController {

    public static final String ACCEPT_PAYMENTS = "accept-payments/";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

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

    @GetMapping("/card-payments-api")
    public String cardPaymentsApi(Model model) {
        model.addAttribute("trans", transactionService.createTransaction());
        return ACCEPT_PAYMENTS + "card-payments-api";
    }

    @GetMapping("/card-details")
    public String cardDetails(Model model) {
        model.addAttribute("card", new Card());
        return ACCEPT_PAYMENTS + "card-details";
    }

    @PostMapping("/callback") // Called by the payment gateway
    public String processCallback(HttpServletRequest request, Model model) {
        InitialTransactionResponse initialTransResponse = new InitialTransactionResponse();
        initialTransResponse.setTxnref(request.getParameter("txnref"));
        TransactionStatusResponse transStatusResponse = transactionService.confirmPayment(initialTransResponse);

        model.addAttribute("trans", transactionService.getTransaction());
        model.addAttribute("transStatus", transStatusResponse);
        return ACCEPT_PAYMENTS + "status";
    }

    @PostMapping("/process-card")
    public String processCard(@ModelAttribute("card") Card card, Model model) {
        System.out.println(">>> Card Pan: " + card.getCardNum());
        cardService.processCard(card);
        model.addAttribute("card", card);
        return ACCEPT_PAYMENTS + "card-details";
        // return ACCEPT_PAYMENTS + "status";
    }
}