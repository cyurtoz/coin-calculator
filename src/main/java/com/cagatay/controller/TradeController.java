package com.cagatay.controller;

import com.cagatay.model.common.*;
import com.cagatay.model.external.CoinPrice;
import com.cagatay.model.service.Result;
import com.cagatay.service.TradeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("v1")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("reserve")
    @ApiOperation(value = "Starts the process of buying coins. Returns a reservation id, unit price and total price of the trade.")
    public Result<CoinReserveResponse> reserve(@RequestParam CoinType coin,
                                               @RequestParam CurrencyType currency,
                                               @RequestParam BuyWith buyWith,
                                               @RequestParam Double amount) {
        return tradeService.reserve(coin, currency, amount, buyWith);
    }

    @PostMapping("buy")
    @ApiOperation(value = "Buys the amount of coins reserved. Returns HTTP 400 if the price is expired.")
    public Result<BuyResponse> buy(@RequestParam String reservationId) {
        return tradeService.buy(reservationId);
    }

    @GetMapping("prices")
    @ApiOperation(value = "Shows current prices.")
    public Map<String, CoinPrice> prices() {
        return tradeService.getCurrentPrices();
    }
}
