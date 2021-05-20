package com.cagatay.model.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class CoinReserveResponse {
    private final String reservationId;
    private final Double unitPrice;
    private final Double coinAmount;
    private final Double totalPrice;
    private final CoinType coin;
    private final CurrencyType currency;
}
