package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class SupplierValidationBean {
	private Integer id;
	private String serviceId;
	private String originCountry;
	private String destCountry;
	private String originCity;
	private String accessType;
	private String servAbbr;
	private String status;
	private String cmsID;
	private List<OutpulseDetailsBean> outpulseDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getServAbbr() {
		return servAbbr;
	}

	public void setServAbbr(String servAbbr) {
		this.servAbbr = servAbbr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCmsID() {
		return cmsID;
	}

	public void setCmsID(String cmsID) {
		this.cmsID = cmsID;
	}

	public List<OutpulseDetailsBean> getOutpulseDetails() {
		return outpulseDetails;
	}

	public void setOutpulseDetails(List<OutpulseDetailsBean> outpulseDetails) {
		this.outpulseDetails = outpulseDetails;
	}

}
