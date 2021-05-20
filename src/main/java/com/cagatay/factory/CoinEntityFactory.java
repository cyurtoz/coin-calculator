package com.cagatay.factory;

import com.cagatay.model.common.CoinReservationEntity;
import com.cagatay.model.common.CoinReserveRequest;
import org.springframework.stereotype.Component;

@Component
public class CoinEntityFactory {

    public CoinReservationEntity createNew(CoinReserveRequest request) {
        CoinReservationEntity entity = new CoinReservationEntity();
        entity.setCoinType(request.getCoinType());
        entity.setCoinAmount(request.getCoinAmount());
        entity.setCurrency(request.getCurrency());
        entity.setTimeStamp(System.currentTimeMillis());
        entity.setTotalPrice(request.getTotalPrice());
        return entity;
    }
}
