package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class ReserveVanityNumberBean {
    private String status;
    private String message;
    private String reservationId;
    private List<NumberDetailsBean> numberDetails;
    private String reservationExpiryDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public List<NumberDetailsBean> getNumberDetails() {
        return numberDetails;
    }

    public void setNumberDetails(List<NumberDetailsBean> numberDetails) {
        this.numberDetails = numberDetails;
    }

    public String getReservationExpiryDate() {
        return reservationExpiryDate;
    }

    public void setReservationExpiryDate(String reservationExpiryDate) {
        this.reservationExpiryDate = reservationExpiryDate;
    }
}
