package com.cagatay.model.service;

public enum ServiceError {

    COIN_PRICE_EXPIRED("Coin price is expired. Please create a new reservation."),
    INVALID_RESERVATION("Invalid reservation."),
    ZERO_AMOUNT("You can bot buy with zero amount."),
    BELOW_MIN_LIMIT("The amount you want to buy is below the price limit. Current min. limit is %s."),
    OVER_MAX_LIMIT("The amount you want to buy is over the price limit. Current max. limit is %s."),
    SERVICE_DOWN("Price service is down. Please try again later");

    private final String messageTemplate;

    ServiceError(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }
}
