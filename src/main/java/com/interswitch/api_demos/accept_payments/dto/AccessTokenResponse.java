package com.interswitch.api_demos.accept_payments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenResponse {
    
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("merchant_code")
    private String merchantCode;

    @JsonProperty("client_name")
    private String clientName;

    @JsonProperty("payable_id")
    private String payableId;

    @JsonProperty("jti")
    private String jti;
}
