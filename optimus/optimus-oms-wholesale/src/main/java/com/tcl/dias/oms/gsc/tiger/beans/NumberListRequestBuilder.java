package com.tcl.dias.oms.gsc.tiger.beans;

/**
 * Builder class to create number list request to Tiger service API
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NumberListRequestBuilder {
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

	public NumberListRequestBuilder setSortBy(String sortBy) {
		this.sortBy = sortBy;
		return this;
	}

	public NumberListRequestBuilder setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}

	public NumberListRequestBuilder setOffset(Integer offset) {
		this.offset = offset;
		return this;
	}

	public NumberListRequestBuilder setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	public NumberListRequestBuilder setServiceType(String serviceType) {
		this.serviceType = serviceType;
		return this;
	}

	public NumberListRequestBuilder setStatus(String status) {
		this.status = status;
		return this;
	}

	public NumberListRequestBuilder setOrgId(String orgId) {
		this.orgId = orgId;
		return this;
	}

	public NumberListRequestBuilder setReservationId(String reservationId) {
		this.reservationId = reservationId;
		return this;
	}

	public NumberListRequestBuilder setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
		return this;
	}

	public NumberListRequestBuilder setOriginState(String originState) {
		this.originState = originState;
		return this;
	}

	public NumberListRequestBuilder setOriginCity(String originCity) {
		this.originCity = originCity;
		return this;
	}

	public NumberListRequestBuilder setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public NumberListRequestBuilder setSequence(String sequence) {
		this.sequence = sequence;
		return this;
	}

	public NumberListRequestBuilder setRangeFrom(String rangeFrom) {
		this.rangeFrom = rangeFrom;
		return this;
	}

	public NumberListRequestBuilder setRangeTo(String rangeTo) {
		this.rangeTo = rangeTo;
		return this;
	}

	public NumberListRequestBuilder setContains(String contains) {
		this.contains = contains;
		return this;
	}

	public NumberListRequestBuilder setE164(String e164) {
		this.e164 = e164;
		return this;
	}

	public NumberListRequestBuilder setOriginCountryCode(String originCountryCode) {
		this.originCountryCode = originCountryCode;
		return this;
	}

	public NumberListRequestBuilder setNpa(String npa) {
		this.npa = npa;
		return this;
	}

	public NumberListRequestBuilder setNxx(String nxx) {
		this.nxx = nxx;
		return this;
	}

	public NumberListRequestBuilder setNumber(String number) {
		this.number = number;
		return this;
	}

	public NumberListRequest create() {
		return new NumberListRequest(sortBy, sortOrder, offset, limit, serviceType, status, orgId, reservationId,
				originCountry, originState, originCity, quantity, sequence, rangeFrom, rangeTo, contains, e164,
				originCountryCode, npa, nxx, number);
	}
}