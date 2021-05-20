package com.cagatay.model.common;

import lombok.Getter;

import java.util.Date;

@Getter
public class BuyResponse {

    private final String reservationId;
    private final Date reservationDate;
    private final Date boughDate;

    public BuyResponse(String reservationId, Date reservationDate, Date boughDate) {
        this.reservationId = reservationId;
        this.reservationDate = reservationDate;
        this.boughDate = boughDate;
    }

}
