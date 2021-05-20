package com.cagatay.model.external;

import lombok.Data;

import java.util.Map;

@Data
public class CoinPriceResponse {
    Map<String, CoinPrice> priceMap;
}
