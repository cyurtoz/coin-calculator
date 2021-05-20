package com.cagatay.service;

import com.cagatay.model.common.*;
import com.cagatay.model.external.CoinPrice;
import com.cagatay.model.service.Result;

import java.util.Map;


public interface TradeService {

    Result<CoinReserveResponse> reserve(CoinType coin, CurrencyType currency,
                                        Double buyAmount, BuyWith buyWith);

    Result<BuyResponse> buy(String reservationId);

    Map<String, CoinPrice> getCurrentPrices();
}
