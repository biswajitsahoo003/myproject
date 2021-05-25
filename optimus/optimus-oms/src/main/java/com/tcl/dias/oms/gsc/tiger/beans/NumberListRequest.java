package com.tcl.dias.oms.gsc.tiger.beans;

/**
 * Bean class to store number list request to Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NumberListRequest {
	private String sortBy;
	private String sortOrder;
	private Integer offset;
	private Integer limit;
	private String serviceType;
	private String status;
	private String orgId;
	private String reservationId;
	private String originCountry;
	private String originState;
	private String originCity;
	private Integer quantity;
	private String sequence;
	private String rangeFrom;
	private String rangeTo;
	private String contains;
	private String e164;
	private String originCountryCode;
	private String npa;
	private String nxx;
	private String number;

	NumberListRequest(String sortBy, String sortOrder, Integer offset, Integer limit, String serviceType, String status,
			String orgId, String reservationId, String originCountry, String originState, String originCity,
			Integer quantity, String sequence, String rangeFrom, String rangeTo, String contains, String e164,
			String originCountryCode, String npa, String nxx, String number) {
		this.sortBy = sortBy;
		this.sortOrder = sortOrder;
		this.offset = offset;
		this.limit = limit;
		this.serviceType = serviceType;
		this.status = status;
		this.orgId = orgId;
		this.reservationId = reservationId;
		this.originCountry = originCountry;
		this.originState = originState;
		this.originCity = originCity;
		this.quantity = quantity;
		this.sequence = sequence;
		this.rangeFrom = rangeFrom;
		this.rangeTo = rangeTo;
		this.contains = contains;
		this.e164 = e164;
		this.originCountryCode = originCountryCode;
		this.npa = npa;
		this.nxx = nxx;
		this.number = number;
	}

	public String getSortBy() {
		return sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public String getServiceType() {
		return serviceType;
	}

	public String getStatus() {
		return status;
	}

	public String getOrgId() {
		return orgId;
	}

	public String getReservationId() {
		return reservationId;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public String getOriginState() {
		return originState;
	}

	public String getOriginCity() {
		return originCity;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getSequence() {
		return sequence;
	}

	public String getRangeFrom() {
		return rangeFrom;
	}

	public String getRangeTo() {
		return rangeTo;
	}

	public String getContains() {
		return contains;
	}

	public String getE164() {
		return e164;
	}

	public String getOriginCountryCode() {
		return originCountryCode;
	}

	public String getNpa() {
		return npa;
	}

	public String getNxx() {
		return nxx;
	}

	public String getNumber() {
		return number;
	}
}
