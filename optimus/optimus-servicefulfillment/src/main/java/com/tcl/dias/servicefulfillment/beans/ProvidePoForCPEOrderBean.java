package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This bean is used to provide details related to CPE Order.
 * 
 * @author arjayapa
 */
public class ProvidePoForCPEOrderBean extends TaskDetailsBaseBean {
	
	private String cpeSupplyHardwarePoNumber;
	
	private String cpeSupplyHardwareVendorName;
	
	private List<AttachmentIdBean> documentIds;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeSupplyHardwarePoDate;

	private String cpeInstallationHardwarePoNumber;
	private String cpeInstallationHardwareVendorName;
	private List<AttachmentIdBean> cpeInstallationHardwarePoDocument;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeInstallationHardwarePoDate;
	
	private String cpeSupportPoNumber;
	
	private String cpeSupportVendorName;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeSupportPoDate;

	private String cpeLicencePoNumber;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeLicencePoDate;
	
	private Integer cpeComponentId;
	private Boolean isRouterExists;

	public String getCpeSupplyHardwarePoNumber() {
		return cpeSupplyHardwarePoNumber;
	}

	public void setCpeSupplyHardwarePoNumber(String cpeSupplyHardwarePoNumber) {
		this.cpeSupplyHardwarePoNumber = cpeSupplyHardwarePoNumber;
	}

	public String getCpeSupplyHardwareVendorName() {
		return cpeSupplyHardwareVendorName;
	}

	public void setCpeSupplyHardwareVendorName(String cpeSupplyHardwareVendorName) {
		this.cpeSupplyHardwareVendorName = cpeSupplyHardwareVendorName;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getCpeSupplyHardwarePoDate() {
		return cpeSupplyHardwarePoDate;
	}

	public void setCpeSupplyHardwarePoDate(String cpeSupplyHardwarePoDate) {
		this.cpeSupplyHardwarePoDate = cpeSupplyHardwarePoDate;
	}

	public String getCpeInstallationHardwarePoNumber() {
		return cpeInstallationHardwarePoNumber;
	}

	public void setCpeInstallationHardwarePoNumber(String cpeInstallationHardwarePoNumber) {
		this.cpeInstallationHardwarePoNumber = cpeInstallationHardwarePoNumber;
	}

	public String getCpeInstallationHardwareVendorName() {
		return cpeInstallationHardwareVendorName;
	}

	public void setCpeInstallationHardwareVendorName(String cpeInstallationHardwareVendorName) {
		this.cpeInstallationHardwareVendorName = cpeInstallationHardwareVendorName;
	}

	public List<AttachmentIdBean> getCpeInstallationHardwarePoDocument() {
		return cpeInstallationHardwarePoDocument;
	}

	public void setCpeInstallationHardwarePoDocument(List<AttachmentIdBean> cpeInstallationHardwarePoDocument) {
		this.cpeInstallationHardwarePoDocument = cpeInstallationHardwarePoDocument;
	}

	public String getCpeInstallationHardwarePoDate() {
		return cpeInstallationHardwarePoDate;
	}

	public void setCpeInstallationHardwarePoDate(String cpeInstallationHardwarePoDate) {
		this.cpeInstallationHardwarePoDate = cpeInstallationHardwarePoDate;
	}
	
	
	public String getCpeSupportPoNumber() {
		return cpeSupportPoNumber;
	}

	public void setCpeSupportPoNumber(String cpeSupportPoNumber) {
		this.cpeSupportPoNumber = cpeSupportPoNumber;
	}

	public String getCpeSupportVendorName() {
		return cpeSupportVendorName;
	}

	public void setCpeSupportVendorName(String cpeSupportVendorName) {
		this.cpeSupportVendorName = cpeSupportVendorName;
	}

	public String getCpeSupportPoDate() {
		return cpeSupportPoDate;
	}

	public void setCpeSupportPoDate(String cpeSupportPoDate) {
		this.cpeSupportPoDate = cpeSupportPoDate;
	}

	public String getCpeLicencePoNumber() {
		return cpeLicencePoNumber;
	}

	public void setCpeLicencePoNumber(String cpeLicencePoNumber) {
		this.cpeLicencePoNumber = cpeLicencePoNumber;
	}

	public String getCpeLicencePoDate() {
		return cpeLicencePoDate;
	}

	public void setCpeLicencePoDate(String cpeLicencePoDate) {
		this.cpeLicencePoDate = cpeLicencePoDate;
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

	@Override
	public String toString() {
		return "ProvidePoForCPEOrderBean{" +
				"cpeSupplyHardwarePoNumber='" + cpeSupplyHardwarePoNumber + '\'' +
				", cpeSupplyHardwareVendorName='" + cpeSupplyHardwareVendorName + '\'' +
				", documentIds=" + documentIds +
				", cpeSupplyHardwarePoDate='" + cpeSupplyHardwarePoDate + '\'' +
				", cpeInstallationHardwarePoNumber='" + cpeInstallationHardwarePoNumber + '\'' +
				", cpeInstallationHardwareVendorName='" + cpeInstallationHardwareVendorName + '\'' +
				", cpeInstallationHardwarePoDocument=" + cpeInstallationHardwarePoDocument +
				", cpeInstallationHardwarePoDate='" + cpeInstallationHardwarePoDate + '\'' +
				", cpeSupportPoNumber='" + cpeSupportPoNumber + '\'' +
				", cpeSupportVendorName='" + cpeSupportVendorName + '\'' +
				", cpeSupportPoDate='" + cpeSupportPoDate + '\'' +
				", cpeLicencePoNumber='" + cpeLicencePoNumber + '\'' +
				", cpeLicencePoDate='" + cpeLicencePoDate + '\'' +
				", cpeComponentId='" + cpeComponentId + '\'' +
				", isRouterExists='" + isRouterExists + '\'' +
				'}';
	}
}
