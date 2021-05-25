package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

/**
 * Bean class to store number reservation request to Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NumberReservationRequest {
	private String originCountryCode;
	private String serviceType;
	private List<String> e164;
	private String reservationId;

	public String getOriginCountryCode() {
		return originCountryCode;
	}

	public void setOriginCountryCode(String originCountryCode) {
		this.originCountryCode = originCountryCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<String> getE164() {
		return e164;
	}

	public void setE164(List<String> e164) {
		this.e164 = e164;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	@Override
	public String toString() {
		return "NumberReservationRequest{" +
				"originCountryCode='" + originCountryCode + '\'' +
				", serviceType='" + serviceType + '\'' +
				", e164=" + e164 +
				", reservationId='" + reservationId + '\'' +
				'}';
	}
}
