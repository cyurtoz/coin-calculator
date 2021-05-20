package com.cagatay.validator;

import com.cagatay.factory.CoinEntityFactory;
import com.cagatay.model.common.CoinReservationEntity;
import com.cagatay.model.common.CoinReserveRequest;
import com.cagatay.model.common.CoinType;
import com.cagatay.model.common.CurrencyType;
import com.cagatay.model.service.Result;
import com.cagatay.model.service.ServiceError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PriceValidatorTest {

    private PriceValidator priceValidator;
    private final int expireSeconds = 1;
    private CoinEntityFactory coinEntityFactory;

    @BeforeEach
    void setUp() {
        priceValidator = new PriceValidator(expireSeconds);
        coinEntityFactory = new CoinEntityFactory();
    }

    @Test
    void PriceValidator_ShouldReturnError_IfPriceIsExpired() {
        long reservationTs = System.currentTimeMillis() - expireSeconds * 1000 - 1000;
        CoinReserveRequest req = CoinReserveRequest.create(CoinType.BTC, CurrencyType.EUR, 1.0D, 4.0D);
        CoinReservationEntity entity = coinEntityFactory.createNew(req);
        entity.setTimeStamp(reservationTs);
        Result<CoinReservationEntity> validate = priceValidator.validate(entity);
        assertFalse(validate.isSuccess());
        assertEquals(ServiceError.COIN_PRICE_EXPIRED, validate.getError());
    }

    @Test
    void PriceValidator_ShouldReturnSuccess_IfPriceIsNotExpired() {
        CoinReserveRequest req = CoinReserveRequest.create(CoinType.BTC, CurrencyType.EUR, 1.0D, 4.0D);
        CoinReservationEntity entity = coinEntityFactory.createNew(req);

        Result<CoinReservationEntity> validate = priceValidator.validate(entity);
        assertTrue(validate.isSuccess());
    }
}
