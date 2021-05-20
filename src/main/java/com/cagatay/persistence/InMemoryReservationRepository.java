package com.cagatay.persistence;

import com.cagatay.model.common.CoinReservationEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class InMemoryReservationRepository implements ReservationRepository {

    private Map<String, CoinReservationEntity> orders;

    public InMemoryReservationRepository() {
        this.orders = new HashMap<>();
    }

    @Override
    public CoinReservationEntity save(CoinReservationEntity body) {
        String reservationId = createReservationId();
        body.setId(reservationId);
        orders.put(reservationId, body);
        return body;
    }

    @Override
    public Optional<CoinReservationEntity> findById(String reservationId) {
        if (orders.containsKey(reservationId))
            return Optional.of(orders.get(reservationId));
        else return Optional.empty();
    }

    @Override
    public void delete(String reservationId) {
        orders.remove(reservationId);
    }

    private String createReservationId() {
        String s = UUID.randomUUID().toString();
        byte[] encode = Base64.getEncoder().encode(s.getBytes(StandardCharsets.UTF_8));
        return new String(encode);
    }
}
