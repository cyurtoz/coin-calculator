package com.cagatay.model.common;

import lombok.Getter;


@Getter
public class CoinReserveRequest {

    private CoinType coinType;
    private CurrencyType currency;
    private Double totalPrice;
    private Double coinAmount;

    public static CoinReserveRequest create(CoinType coinType,
                                            CurrencyType currency,
                                            Double totalPrice,
                                            Double coinAmount) {
        CoinReserveRequest coinReserveRequest = new CoinReserveRequest();
        coinReserveRequest.totalPrice = totalPrice;
        coinReserveRequest.coinType = coinType;
        coinReserveRequest.currency = currency;
        coinReserveRequest.coinAmount = coinAmount;
        return coinReserveRequest;
    }
}
