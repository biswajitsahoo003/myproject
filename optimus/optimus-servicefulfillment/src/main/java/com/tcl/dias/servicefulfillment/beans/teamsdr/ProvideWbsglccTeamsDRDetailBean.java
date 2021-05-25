package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Provide WBSGLCC for teamsDR media gateway
 *
 * @author srraghav
 */
public class ProvideWbsglccTeamsDRDetailBean extends TaskDetailsBaseBean {
	private String level5Wbs;
	private String demandIdNo;
	private String glCode;
	private String costCenter;
	private String supportDemandIdNo;
	private String supportGlCode;
	private String supportCostCenter;
	private String licenceDemandIdNo;
	private String licenceGlCode;
	private String licenceCostCenter;
	private String endpointDeliveryRequired;
	private String endpointDeliveryRequiredRejectionReason;
	private String endpointRequiredDate;
	private String vendorPORaisedDate;

	public String getEndpointDeliveryRequired() {
		return endpointDeliveryRequired;
	}

	public void setEndpointDeliveryRequired(String endpointDeliveryRequired) {
		this.endpointDeliveryRequired = endpointDeliveryRequired;
	}

	public String getEndpointDeliveryRequiredRejectionReason() {
		return endpointDeliveryRequiredRejectionReason;
	}

	public void setEndpointDeliveryRequiredRejectionReason(String endpointDeliveryRequiredRejectionReason) {
		this.endpointDeliveryRequiredRejectionReason = endpointDeliveryRequiredRejectionReason;
	}

	public String getLevel5Wbs() {
		return level5Wbs;
	}

	public void setLevel5Wbs(String level5Wbs) {
		this.level5Wbs = level5Wbs;
	}

	public String getDemandIdNo() {
		return demandIdNo;
	}

	public void setDemandIdNo(String demandIdNo) {
		this.demandIdNo = demandIdNo;
	}

	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getSupportDemandIdNo() {
		return supportDemandIdNo;
	}

	public void setSupportDemandIdNo(String supportDemandIdNo) {
		this.supportDemandIdNo = supportDemandIdNo;
	}

	public String getSupportGlCode() {
		return supportGlCode;
	}

	public void setSupportGlCode(String supportGlCode) {
		this.supportGlCode = supportGlCode;
	}

	public String getSupportCostCenter() {
		return supportCostCenter;
	}

	public void setSupportCostCenter(String supportCostCenter) {
		this.supportCostCenter = supportCostCenter;
	}

	public String getLicenceDemandIdNo() {
		return licenceDemandIdNo;
	}

	public void setLicenceDemandIdNo(String licenceDemandIdNo) {
		this.licenceDemandIdNo = licenceDemandIdNo;
	}

	public String getLicenceGlCode() {
		return licenceGlCode;
	}

	public void setLicenceGlCode(String licenceGlCode) {
		this.licenceGlCode = licenceGlCode;
	}

	public String getLicenceCostCenter() {
		return licenceCostCenter;
	}

	public void setLicenceCostCenter(String licenceCostCenter) {
		this.licenceCostCenter = licenceCostCenter;
	}

	public String getEndpointRequiredDate() {
		return endpointRequiredDate;
	}

	public void setEndpointRequiredDate(String endpointRequiredDate) {
		this.endpointRequiredDate = endpointRequiredDate;
	}

	public String getVendorPORaisedDate() {
		return vendorPORaisedDate;
	}

	public void setVendorPORaisedDate(String vendorPORaisedDate) {
		this.vendorPORaisedDate = vendorPORaisedDate;
	}

	@Override
	public String toString() {
		return "ProvideWbsglccDetailBean{" + "level5Wbs='" + level5Wbs + '\'' + ", demandIdNo='" + demandIdNo + '\'' + ", glCode='" + glCode + '\'' + ", costCenter='" + costCenter + '\'' + ", supportDemandIdNo='" + supportDemandIdNo + '\'' + ", supportGlCode='" + supportGlCode + '\'' + ", supportCostCenter='" + supportCostCenter + '\'' + ", licenceDemandIdNo='" + licenceDemandIdNo + '\'' + ", licenceGlCode='" + licenceGlCode + '\'' + ", licenceCostCenter='" + licenceCostCenter + '\'' + '}';
	}
}
