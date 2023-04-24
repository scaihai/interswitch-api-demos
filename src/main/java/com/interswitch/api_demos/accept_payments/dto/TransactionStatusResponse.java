package com.interswitch.api_demos.accept_payments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionStatusResponse {

    @JsonProperty("Amount")
    private Long amount;

    @JsonProperty("CardNumber")
    private String cardNumber;

    @JsonProperty("MerchantReference")
    private String merchantReference;

    @JsonProperty("PaymentReference")
    private String paymentReference;

    @JsonProperty("RetrievalReferenceNumber")
    private String retrievalReferenceNumber;

    @JsonProperty("TransactionDate")
    private String transactionDate;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @JsonProperty("BankCode")
    private String bankCode;

    @JsonProperty("RemittanceAmount")
    private Long remittanceAmount;

    public TransactionStatusResponse setError(String errorMsg) {
        this.responseDescription = errorMsg;
        this.responseCode = "99";       
        return this;
    }
}
