package com.cagatay.service;

import com.cagatay.external.BTCApiClient;
import com.cagatay.model.common.CoinType;
import com.cagatay.model.common.CurrencyType;
import com.cagatay.model.external.CoinPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinPriceServiceImpl implements CoinPriceService {

    private final BTCApiClient btcApiClient;

    @Override
    public Optional<Double> getBuyingPriceForCurrency(CoinType coin, CurrencyType currencyType) {
        ResponseEntity<Map<String, CoinPrice>> currentPrices = btcApiClient.getCurrentPrices();
        if (currentPrices.getStatusCode().is2xxSuccessful() && !Objects.isNull(currentPrices.getBody())) {
            return Optional.ofNullable(currentPrices.getBody().get(currencyType.name()).getBuy());
        } else {
            log.warn("Could not retrieve prices");
            return Optional.empty();
        }
    }

    @Override
    public Map<String, CoinPrice> getAllPrices() {
        ResponseEntity<Map<String, CoinPrice>> currentPrices = btcApiClient.getCurrentPrices();
        Map<String, CoinPrice> body = currentPrices.getBody();
        if (currentPrices.getStatusCode().is2xxSuccessful() && !Objects.isNull(body)) {
            Map<String, CoinPrice> supportedPrices = new HashMap<>();
            Arrays.stream(CurrencyType.values()).forEach(k -> {
                if (body.containsKey(k.name())) {
                    supportedPrices.put(k.name(), body.get(k.name()));
                }
            });
            return supportedPrices;
        } else {
            log.warn("Could not retrieve prices");
            return Collections.emptyMap();
        }
    }
}
