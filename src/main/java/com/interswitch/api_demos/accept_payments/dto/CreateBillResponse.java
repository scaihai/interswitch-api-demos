package com.interswitch.api_demos.accept_payments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBillResponse {

    private Long id;
    private Double amount;

    private String merchantCode, payableCode, code, redirectUrl, customerId,
            reference, customerEmail, currencyCode, paymentUrl;
}
