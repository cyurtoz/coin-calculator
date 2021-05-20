package com.cagatay.persistence;

import com.cagatay.model.common.CoinReservationEntity;

import java.util.Optional;

public interface ReservationRepository {

    CoinReservationEntity save(CoinReservationEntity body);

    Optional<CoinReservationEntity> findById(String reservationId);

    void delete(String reservationId);
}
