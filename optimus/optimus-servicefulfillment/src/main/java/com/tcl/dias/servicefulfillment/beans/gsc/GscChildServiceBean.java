package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GscChildServiceBean {

	private Integer id;
	private String accessType;
	private String serviceCode;
	private String originCountry;
	private String destCountry;
	private String status;
	private String remarks;
	private List<DocumentBean> documents;
	private Map<String, Object> commonData = new HashMap<String, Object>();

	public Integer getId() {
		return id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public List<DocumentBean> getDocuments() {
		return documents;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public void setDocuments(List<DocumentBean> documents) {
		this.documents = documents;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Map<String, Object> getCommonData() {
		return commonData;
	}

	public void setCommonData(Map<String, Object> commonData) {
		this.commonData = commonData;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
}
