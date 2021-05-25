package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NumbersBean {

	private String correlationId;
	private Integer invoiceGroupId;
	private String vasNumberId;
	private String acBridgePlatform;
	@JsonIgnore
	private String cityCode;

	private List<DetailsBySupplierBean> detailsBySupplier;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String inServiceDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String endServiceDate;
	
	private String status;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAcBridgePlatform() {
		return acBridgePlatform;
	}

	public void setAcBridgePlatform(String acBridgePlatform) {
		this.acBridgePlatform = acBridgePlatform;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public Integer getInvoiceGroupId() {
		return invoiceGroupId;
	}

	public void setInvoiceGroupId(Integer invoiceGroupId) {
		this.invoiceGroupId = invoiceGroupId;
	}

	public String getVasNumberId() {
		return vasNumberId;
	}

	public void setVasNumberId(String vasNumberId) {
		this.vasNumberId = vasNumberId;
	}

	public List<DetailsBySupplierBean> getDetailsBySupplier() {
		return detailsBySupplier;
	}

	public void setDetailsBySupplier(List<DetailsBySupplierBean> detailsBySupplier) {
		this.detailsBySupplier = detailsBySupplier;
	}

	public String getInServiceDate() {
		return inServiceDate;
	}

	public void setInServiceDate(String inServiceDate) {
		this.inServiceDate = inServiceDate;
	}

	public String getEndServiceDate() {
		return endServiceDate;
	}

	public void setEndServiceDate(String endServiceDate) {
		this.endServiceDate = endServiceDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
