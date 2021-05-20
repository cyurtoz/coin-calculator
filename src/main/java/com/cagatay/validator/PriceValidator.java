package com.cagatay.validator;

import com.cagatay.model.common.CoinReservationEntity;
import com.cagatay.model.service.Result;
import com.cagatay.model.service.ServiceError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PriceValidator implements Validator<CoinReservationEntity> {

    private final int priceExpireInSeconds;

    public PriceValidator(@Value("${coin-calculator.priceExpireInSeconds}") int priceExpireInSeconds) {
        this.priceExpireInSeconds = priceExpireInSeconds;
    }

    @Override
    public Result<CoinReservationEntity> validate(CoinReservationEntity argument) {
        boolean expired = (System.currentTimeMillis() - argument.getTimeStamp()) / 1000 > priceExpireInSeconds;
        if (expired) {
            return Result.failedWithError(ServiceError.COIN_PRICE_EXPIRED);
        } else return Result.success(argument);
    }
}
