package com.cagatay.service;

import com.cagatay.factory.CoinEntityFactory;
import com.cagatay.model.common.*;
import com.cagatay.model.service.Result;
import com.cagatay.model.service.ServiceError;
import com.cagatay.persistence.ReservationRepository;
import com.cagatay.validator.AmountValidator;
import com.cagatay.validator.PriceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TradeServiceTest {

    private TradeService tradeService;
    private CoinPriceService coinPriceService;
    private AmountValidator amountValidator;
    private PriceValidator priceValidator;
    private ReservationRepository repository;
    private CoinEntityFactory entityFactory;

    @BeforeEach
    void setUp() {
        coinPriceService = mock(CoinPriceService.class);
        amountValidator = mock(AmountValidator.class);
        priceValidator = mock(PriceValidator.class);
        repository = mock(ReservationRepository.class);
        entityFactory = new CoinEntityFactory();
        tradeService = new TradeServiceImpl(coinPriceService, repository, amountValidator, priceValidator, entityFactory);
    }

    @Test
    void TradeService_ShouldReserveByCoinAmount_IfValidatorsReturnOK() {
        when(coinPriceService.getBuyingPriceForCurrency(any(), any())).thenReturn(Optional.of(2.0D));
        when(amountValidator.validate(any())).thenReturn(Result.success(new CoinReserveRequest()));
        when(repository.save(any())).thenAnswer((Answer<CoinReservationEntity>) invocationOnMock -> {
            CoinReservationEntity argument = invocationOnMock.getArgument(0);
            argument.setId("id");
            return argument;
        });

        Result<CoinReserveResponse> reserve = tradeService.reserve(CoinType.BTC, CurrencyType.EUR,
                2.0D, BuyWith.COIN_AMOUNT);

        assertTrue(reserve.isSuccess());
        assertEquals(4.0D, reserve.getBody().getTotalPrice());
        assertEquals("You wanted to buy 2.0 BTC with 4.0 EUR. Current unit price is 2.0", reserve.getMessage());
    }

    @Test
    void TradeService_ShouldReserveByCurrencyAmount_IfValidatorsReturnOK() {
        when(coinPriceService.getBuyingPriceForCurrency(any(), any())).thenReturn(Optional.of(2.0D));
        when(amountValidator.validate(any())).thenReturn(Result.success(new CoinReserveRequest()));
        when(repository.save(any())).thenAnswer((Answer<CoinReservationEntity>) invocationOnMock -> {
            CoinReservationEntity argument = invocationOnMock.getArgument(0);
            argument.setId("id");
            return argument;
        });

        Result<CoinReserveResponse> reserve = tradeService.reserve(CoinType.BTC, CurrencyType.EUR,
                2.0D, BuyWith.CURRENCY_AMOUNT);

        assertTrue(reserve.isSuccess());
        assertEquals(2.0D, reserve.getBody().getTotalPrice());
        assertEquals("You wanted to buy 1.0 BTC with 2.0 EUR. Current unit price is 2.0", reserve.getMessage());
    }

    @Test
    void TradeService_ShouldFailReserve_IfOneValidatorFails() {
        when(coinPriceService.getBuyingPriceForCurrency(any(), any())).thenReturn(Optional.of(2.0D));
        when(amountValidator.validate(any())).thenReturn(Result.failWithMessageArgs(ServiceError.BELOW_MIN_LIMIT, new Object[]{10}));

        Result<CoinReserveResponse> reserve = tradeService.reserve(CoinType.BTC, CurrencyType.EUR,
                2.0D, BuyWith.CURRENCY_AMOUNT);

        assertFalse(reserve.isSuccess());
        assertEquals("The amount you want to buy is below the price limit. Current min. limit is 10.", reserve.getMessage());
    }

    @Test
    void TradeService_ShouldBuyCoinAmount_IfReservationIsOK() {
        when(coinPriceService.getBuyingPriceForCurrency(any(), any())).thenReturn(Optional.of(2.0D));
        when(priceValidator.validate(any())).thenReturn(Result.success(new CoinReservationEntity()));
        CoinReservationEntity ent = entityFactory.createNew(
                CoinReserveRequest.create(CoinType.BTC, CurrencyType.EUR, 1.0D, 4.0D));
        when(repository.findById(any())).thenReturn(Optional.of(ent));
        Result<BuyResponse> reserve = tradeService.buy("1");
        assertTrue(reserve.isSuccess());
        assertEquals("1", reserve.getBody().getReservationId());
        assertEquals("You bought 4.0 BTC with 1.0 EUR !", reserve.getMessage());
    }

    @Test
    void TradeService_ShouldNotBuy_IfReservationIdIsInvalid() {
        when(coinPriceService.getBuyingPriceForCurrency(any(), any())).thenReturn(Optional.of(2.0D));
        when(priceValidator.validate(any())).thenReturn(Result.success(new CoinReservationEntity()));
        when(repository.findById(any())).thenReturn(Optional.empty());
        Result<BuyResponse> reserve = tradeService.buy("1");
        assertFalse(reserve.isSuccess());
        assertEquals("Invalid reservation.", reserve.getMessage());
    }

    @Test
    void TradeService_ShouldNotBuy_IfValidationFails() {
        when(coinPriceService.getBuyingPriceForCurrency(any(), any())).thenReturn(Optional.of(2.0D));
        when(priceValidator.validate(any())).thenReturn(Result.failedWithError(ServiceError.COIN_PRICE_EXPIRED));
        CoinReservationEntity ent = entityFactory.createNew(
                CoinReserveRequest.create(CoinType.BTC, CurrencyType.EUR, 1.0D, 4.0D));
        when(repository.findById(any())).thenReturn(Optional.of(ent));
        Result<BuyResponse> reserve = tradeService.buy("1");
        assertFalse(reserve.isSuccess());
        assertEquals("Coin price is expired. Please create a new reservation.", reserve.getMessage());
    }
}
