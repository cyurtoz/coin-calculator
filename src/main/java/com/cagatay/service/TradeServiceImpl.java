package com.cagatay.service;

import com.cagatay.factory.CoinEntityFactory;
import com.cagatay.model.common.*;
import com.cagatay.model.external.CoinPrice;
import com.cagatay.model.service.Result;
import com.cagatay.model.service.ServiceError;
import com.cagatay.persistence.ReservationRepository;
import com.cagatay.validator.AmountValidator;
import com.cagatay.validator.PriceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class TradeServiceImpl implements TradeService {

    private final CoinPriceService coinPriceService;
    private final ReservationRepository reservationRepository;
    private final AmountValidator amountValidator;
    private final PriceValidator priceValidator;
    private final CoinEntityFactory coinEntityFactory;

    @Override
    public Result<CoinReserveResponse> reserve(CoinType coin, CurrencyType currency,
                                               Double buyAmount, BuyWith buyWith) {
        Optional<Double> currentPriceOpt = coinPriceService.getBuyingPriceForCurrency(coin, currency);
        return currentPriceOpt.map(currentPrice -> {
            final Result<CoinReserveResponse> res;
            CoinReserveRequest request = processCoinRequest(coin, currency, buyAmount, buyWith, currentPrice);
            Result<CoinReserveRequest> amountValidation = amountValidator.validate(request);
            if (amountValidation.isSuccess()) {
                CoinReservationEntity newEntity = coinEntityFactory.createNew(request);
                CoinReservationEntity saved = reservationRepository.save(newEntity);
                CoinReserveResponse response = new CoinReserveResponse(saved.getId(), currentPrice, saved.getCoinAmount(),
                        saved.getTotalPrice(), coin, currency);
                res = Result.successWithMessage(response, createReservationSummary(response));
            } else {
                res = Result.failedWithResult(amountValidation);
            }
            return res;
        }).orElse(Result.failedWithError(ServiceError.SERVICE_DOWN));
    }

    @Override
    public Result<BuyResponse> buy(String reservationId) {
        Optional<CoinReservationEntity> reservationEntityOpt = reservationRepository.findById(reservationId);
        if (reservationEntityOpt.isPresent()) {
            CoinReservationEntity entity = reservationEntityOpt.get();
            Result<CoinReservationEntity> priceValidation = priceValidator.validate(entity);
            if (priceValidation.isSuccess()) {
                BuyResponse buyResponse = new BuyResponse(reservationId, new Date(entity.getTimeStamp()), new Date());
                reservationRepository.delete(reservationId);
                return Result.successWithMessage(buyResponse, createTradeSummary(entity));
            } else return Result.failedWithResult(priceValidation);
        } else return Result.failedWithError(ServiceError.INVALID_RESERVATION);

    }

    @Override
    public Map<String, CoinPrice> getCurrentPrices() {
        return coinPriceService.getAllPrices();
    }

    private CoinReserveRequest processCoinRequest(CoinType coin, CurrencyType currency,
                                                  Double buyAmount, BuyWith buyWith, Double currentPrice) {
        if (buyWith.equals(BuyWith.COIN_AMOUNT)) {
            double totalPrice = buyAmount * currentPrice;
            return CoinReserveRequest.create(coin, currency, totalPrice, buyAmount);
        } else {
            double coinAmount = buyAmount / currentPrice;
            return CoinReserveRequest.create(coin, currency, buyAmount, coinAmount);
        }
    }

    private String createReservationSummary(CoinReserveResponse response) {
        return String.format("You wanted to buy %s %s with %s %s. Current unit price is %s",
                response.getCoinAmount(), response.getCoin(), response.getTotalPrice(), response.getCurrency(),
                response.getUnitPrice());
    }

    private String createTradeSummary(CoinReservationEntity response) {
        return String.format("You bought %s %s with %s %s !",
                response.getCoinAmount(), response.getCoinType(), response.getTotalPrice(), response.getCurrency());
    }
}
