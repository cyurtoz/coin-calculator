package com.cagatay.model.common;

import lombok.Data;

@Data
public class CoinReservationEntity {

    private String id;
    private CoinType coinType;
    private CurrencyType currency;
    private Double totalPrice;
    private Double coinAmount;
    private long timeStamp;

}
