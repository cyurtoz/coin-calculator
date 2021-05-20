package com.cagatay.service;

import com.cagatay.model.common.CoinType;
import com.cagatay.model.common.CurrencyType;
import com.cagatay.model.external.CoinPrice;

import java.util.Map;
import java.util.Optional;

public interface CoinPriceService {

    Optional<Double> getBuyingPriceForCurrency(CoinType coin, CurrencyType currencyType);

    Map<String, CoinPrice> getAllPrices();
}
