package com.interswitch.api_demos.accept_payments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    
    private String cardNum, expiryMonth, expiryYear, cvv, pin;
}
