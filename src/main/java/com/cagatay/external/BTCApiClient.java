package com.cagatay.external;

import com.cagatay.model.external.CoinPrice;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class BTCApiClient {

    private final RestTemplate restTemplate;

    public BTCApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<Map<String, CoinPrice>> getCurrentPrices() {
        ParameterizedTypeReference<Map<String, CoinPrice>> responseType = new ParameterizedTypeReference<>() {
        };
        return restTemplate.exchange("https://blockchain.info/ticker", HttpMethod.GET, HttpEntity.EMPTY, responseType);
    }
}
