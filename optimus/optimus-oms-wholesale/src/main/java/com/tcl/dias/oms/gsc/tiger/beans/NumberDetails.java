package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Bean class to store Vanity number details from Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberDetails {

	private String originCountry;
	private String originState;
	private String originCity;
	private String e164;
	private String serviceType;
	private String orgId;
	private String supplier;
	private String status;
	private String reservationId;
	private Date reservationExpiryDate;
	private String originCountryCode;
	private String npa;
	private String nxx;
	private String number;
	private String remarks;
	private String vanityNumStatus;

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getOriginState() {
		return originState;
	}

	public void setOriginState(String originState) {
		this.originState = originState;
	}

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getE164() {
		return e164;
	}

	public void setE164(String e164) {
		this.e164 = e164;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public Date getReservationExpiryDate() {
		return reservationExpiryDate;
	}

	public void setReservationExpiryDate(Date reservationExpiryDate) {
		this.reservationExpiryDate = reservationExpiryDate;
	}

	public String getOriginCountryCode() {
		return originCountryCode;
	}

	public void setOriginCountryCode(String originCountryCode) {
		this.originCountryCode = originCountryCode;
	}

	public String getNpa() {
		return npa;
	}

	public void setNpa(String npa) {
		this.npa = npa;
	}

	public String getNxx() {
		return nxx;
	}

	public void setNxx(String nxx) {
		this.nxx = nxx;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getVanityNumStatus() {
		return vanityNumStatus;
	}

	public void setVanityNumStatus(String vanityNumStatus) {
		this.vanityNumStatus = vanityNumStatus;
	}

	@Override
	public String toString() {
		return "NumberDetails{" +
				"originCountry='" + originCountry + '\'' +
				", originState='" + originState + '\'' +
				", originCity='" + originCity + '\'' +
				", e164='" + e164 + '\'' +
				", serviceType='" + serviceType + '\'' +
				", orgId='" + orgId + '\'' +
				", supplier='" + supplier + '\'' +
				", status='" + status + '\'' +
				", reservationId='" + reservationId + '\'' +
				", reservationExpiryDate=" + reservationExpiryDate +
				", originCountryCode='" + originCountryCode + '\'' +
				", npa='" + npa + '\'' +
				", nxx='" + nxx + '\'' +
				", number='" + number + '\'' +
				", remarks='" + remarks + '\'' +
				", vanityNumStatus='" + vanityNumStatus + '\'' +
				'}';
	}
}
