package com.interswitch.api_demos.misc;

import java.text.NumberFormat;
import java.util.Base64;
import java.util.Locale;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Utils {

    public static final String LOG_PREFIX = ":::::";

    @Autowired
    private ObjectMapper objectMapper;
    
    public <T> T jsonToObject(String json, Class<T> c) {
        try {
            return objectMapper.readValue(json, c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String formatAmount(Long amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(amount/100.0);
    }

    public void log(String message, Logger logger, boolean isError) {
        String msg = String.format("%s %s", LOG_PREFIX, message);
        if (isError) logger.error(msg);
        else logger.info(msg);
    }

    public void log(String message, Logger logger) {
        log(message, logger, false);
    }

    public String base64Encode(byte[] bytes) {
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return new String(encodedBytes);
    }

    public byte[] base64Decode(String encoded) {
        return Base64.getDecoder().decode(encoded.getBytes());
    }
}
