package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.Date;
import java.util.List;

/**
 * Bean class to store number reservation response from Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NumberReservationResponse extends TigerServiceResponse {
	private String reservationId;
	private List<NumberDetails> numberDetails;
	private Date reservationExpiryDate;

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public List<NumberDetails> getNumberDetails() {
		return numberDetails;
	}

	public void setNumberDetails(List<NumberDetails> numberDetails) {
		this.numberDetails = numberDetails;
	}

	public Date getReservationExpiryDate() {
		return reservationExpiryDate;
	}

	public void setReservationExpiryDate(Date reservationExpiryDate) {
		this.reservationExpiryDate = reservationExpiryDate;
	}
}
