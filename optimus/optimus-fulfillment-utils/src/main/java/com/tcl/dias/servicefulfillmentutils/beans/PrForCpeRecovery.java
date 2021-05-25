package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class PrForCpeRecovery extends BaseRequest {

	private String cpeSupplyHardwarePrNumber;
	private String cpeSupplyHardwarePrVendorName;
	private String cpeSupplyHardwarePrDate;
	private String cpeLicencePrNumber;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeLicencePrDate;
	private String cpeLicenseVendorName;
	private Integer cpeComponentId;
	private Boolean isRouterExists;

	private List<AttachmentIdBean> documentIds;

	public String getCpeSupplyHardwarePrNumber() {
		return cpeSupplyHardwarePrNumber;
	}

	public void setCpeSupplyHardwarePrNumber(String cpeSupplyHardwarePrNumber) {
		this.cpeSupplyHardwarePrNumber = cpeSupplyHardwarePrNumber;
	}

	public String getCpeSupplyHardwarePrVendorName() {
		return cpeSupplyHardwarePrVendorName;
	}

	public void setCpeSupplyHardwarePrVendorName(String cpeSupplyHardwarePrVendorName) {
		this.cpeSupplyHardwarePrVendorName = cpeSupplyHardwarePrVendorName;
	}

	public String getCpeSupplyHardwarePrDate() {
		return cpeSupplyHardwarePrDate;
	}

	public void setCpeSupplyHardwarePrDate(String cpeSupplyHardwarePrDate) {
		this.cpeSupplyHardwarePrDate = cpeSupplyHardwarePrDate;
	}

	public String getCpeLicencePrNumber() {
		return cpeLicencePrNumber;
	}

	public void setCpeLicencePrNumber(String cpeLicencePrNumber) {
		this.cpeLicencePrNumber = cpeLicencePrNumber;
	}

	public String getCpeLicencePrDate() {
		return cpeLicencePrDate;
	}

	public void setCpeLicencePrDate(String cpeLicencePrDate) {
		this.cpeLicencePrDate = cpeLicencePrDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getCpeLicenseVendorName() {
		return cpeLicenseVendorName;
	}

	public void setCpeLicenseVendorName(String cpeLicenseVendorName) {
		this.cpeLicenseVendorName = cpeLicenseVendorName;
	}

	public Integer getCpeComponentId() {
		return cpeComponentId;
	}

	public void setCpeComponentId(Integer cpeComponentId) {
		this.cpeComponentId = cpeComponentId;
	}

	public Boolean getIsRouterExists() {
		return isRouterExists;
	}

	public void setIsRouterExists(Boolean isRouterExists) {
		this.isRouterExists = isRouterExists;
	}

}
