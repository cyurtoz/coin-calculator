package com.cagatay.validator;

import com.cagatay.model.common.CoinReserveRequest;
import com.cagatay.model.common.CoinType;
import com.cagatay.model.common.CurrencyType;
import com.cagatay.model.service.Result;
import com.cagatay.model.service.ServiceError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AmountValidatorTest {

    private AmountValidator amountValidator;
    private int minBuy = 10;
    private int maxBuy = 100;

    @BeforeEach
    void setUp() {
        amountValidator = new AmountValidator(maxBuy, minBuy);
    }

    @Test
    void AmountValidator_ShouldReturnError_IfAmountIsBelowLimit() {
        CoinReserveRequest req = CoinReserveRequest.create(CoinType.BTC, CurrencyType.EUR, 3D, 4.0D);
        Result<CoinReserveRequest> validate = amountValidator.validate(req);
        assertFalse(validate.isSuccess());
        assertEquals(ServiceError.BELOW_MIN_LIMIT, validate.getError());
    }

    @Test
    void AmountValidator_ShouldReturnError_IfAmountIsOverLimit() {
        CoinReserveRequest req = CoinReserveRequest.create(CoinType.BTC, CurrencyType.EUR, 105D, 4.0D);
        Result<CoinReserveRequest> validate = amountValidator.validate(req);
        assertFalse(validate.isSuccess());
        assertEquals(ServiceError.OVER_MAX_LIMIT, validate.getError());
    }

    @Test
    void AmountValidator_ShouldReturnOK_IfAmountIsValid() {
        CoinReserveRequest req = CoinReserveRequest.create(CoinType.BTC, CurrencyType.EUR, 20D, 4.0D);
        Result<CoinReserveRequest> validate = amountValidator.validate(req);
        assertTrue(validate.isSuccess());
    }
}
