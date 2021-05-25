package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class RepcDBCreationBean extends TaskDetailsBaseBean {
	private Integer serviceId;
	private String productType;
	private String sourceCountry;
	private String destCountry;
	private CircuitCreationBean circuitBean;
	private String status;
	private Integer noOfTrunks;
	
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSourceCountry() {
		return sourceCountry;
	}

	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public CircuitCreationBean getCircuitBean() {
		return circuitBean;
	}

	public void setCircuitBean(CircuitCreationBean circuitBean) {
		this.circuitBean = circuitBean;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNoOfTrunks() {
		return noOfTrunks;
	}

	public void setNoOfTrunks(Integer noOfTrunks) {
		this.noOfTrunks = noOfTrunks;
	}
}
