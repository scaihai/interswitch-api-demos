package com.interswitch.api_demos.accept_payments.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.interswitch.api_demos.accept_payments.dto.AccessTokenResponse;
import com.interswitch.api_demos.accept_payments.dto.Card;
import com.interswitch.api_demos.misc.Utils;

import kong.unirest.Unirest;

@Service
public class CardService {

    private Logger logger = LoggerFactory.getLogger(CardService.class);

    @Value("${interswitch.client-id}")
    private String clientId;

    @Value("${interswitch.secret-key}")
    private String secretKey;

    @Value("${interswitch.access-token-url}")
    private String accessTokenUrl;

    @Autowired
    private Utils utils;

    public void processCard(Card card) {
        String accessToken = getAccessToken();
    }

    private String getAccessToken() {
        byte[] credentials = (clientId + ":" + secretKey).getBytes();
        String base64Encoded = utils.base64Encode(credentials);
        String authorization = "Basic " + base64Encoded;
        String response = Unirest.post(accessTokenUrl)
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .field("grant_type", "client_credentials")
                .asString().getBody();
        utils.log("Access Token Response: \n" + response, logger);
        return utils.jsonToObject(response, AccessTokenResponse.class).getAccessToken();
    }
}
