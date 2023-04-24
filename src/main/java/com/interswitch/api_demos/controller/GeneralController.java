package com.interswitch.api_demos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.interswitch.api_demos.accept_payments.controller.AcceptPaymentsController;

@Controller
public class GeneralController {

    @Autowired
    private AcceptPaymentsController acceptPaymentsController;
    
    @GetMapping("/")
    public String homePage(Model model) {
        return acceptPaymentsController.inlineCheckout(model);
    }
}