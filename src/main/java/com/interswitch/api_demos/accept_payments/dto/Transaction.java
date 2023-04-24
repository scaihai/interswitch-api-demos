package com.interswitch.api_demos.accept_payments.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transaction {

    public enum Status { PENDING, SUCCESSFUL, FAILED }
    
    private String reference;
    private Long amount;
    private String currency;
    private String displayAmount;
    private String paymentItemId;
    private String paymentItemName;
    private Status status;
}
