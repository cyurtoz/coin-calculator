package com.cagatay.validator;

import com.cagatay.model.common.CoinReserveRequest;
import com.cagatay.model.service.Result;
import com.cagatay.model.service.ServiceError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmountValidator implements Validator<CoinReserveRequest> {

    private final Integer maxBuyAmount;
    private final Integer minBuyAmount;

    public AmountValidator(@Value("${coin-calculator.limit.maxBuy}") int maxBuyAmount,
                           @Value("${coin-calculator.limit.minBuy}") int minBuyAmount) {
        this.maxBuyAmount = maxBuyAmount;
        this.minBuyAmount = minBuyAmount;
    }

    @Override
    public Result<CoinReserveRequest> validate(CoinReserveRequest argument) {
        if (isInvalidAmount(argument)) {
            return Result.failedWithError(ServiceError.ZERO_AMOUNT);
        }

        if (isMaxLimitExceeded(argument)) {
            return Result.failWithMessageArgs(ServiceError.OVER_MAX_LIMIT, new Integer[]{maxBuyAmount});
        }

        if (isMinLimit(argument)) {
            return Result.failWithMessageArgs(ServiceError.BELOW_MIN_LIMIT, new Integer[]{minBuyAmount});
        }
        return Result.success(argument);
    }

    private boolean isMaxLimitExceeded(CoinReserveRequest argument) {
        return argument.getTotalPrice() > maxBuyAmount;
    }

    private boolean isMinLimit(CoinReserveRequest argument) {
        return argument.getTotalPrice() < minBuyAmount;
    }

    private boolean isInvalidAmount(CoinReserveRequest argument) {
        return argument.getCoinAmount() <= 0;
    }
}
