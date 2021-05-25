package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * License prices request
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LicensePricesBean {

	@JsonProperty("license_detail")
	private List<LicensePricesDetailBean> licenseDetail;

	@JsonProperty("total_mrc")
	private Double totalMrc;

	@JsonProperty("total_nrc")
	private Double totalNrc;

	@JsonProperty("total_arc")
	private Double totalArc;

	@JsonProperty("total_tcv")
	private Double totalTcv;

	@JsonProperty("management_charge_unit_nrc")
	private Double managementChargeUnitNrc;

	@JsonProperty("management_charge_nrc")
	private Double managementChargeNrc;

	@JsonProperty("contract_term")
	private Integer contractTerm;

	@JsonProperty("uom")
	private String UOM;

	public List<LicensePricesDetailBean> getLicenseDetail() {
		return licenseDetail;
	}

	public void setLicenseDetail(List<LicensePricesDetailBean> licenseDetail) {
		this.licenseDetail = licenseDetail;
	}

	public Double getTotalMrc() {
		return totalMrc;
	}

	public void setTotalMrc(Double totalMrc) {
		this.totalMrc = totalMrc;
	}

	public Double getTotalNrc() {
		return totalNrc;
	}

	public void setTotalNrc(Double totalNrc) {
		this.totalNrc = totalNrc;
	}

	public Double getTotalArc() {
		return totalArc;
	}

	public void setTotalArc(Double totalArc) {
		this.totalArc = totalArc;
	}

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}

	public Double getManagementChargeUnitNrc() {
		return managementChargeUnitNrc;
	}

	public void setManagementChargeUnitNrc(Double managementChargeUnitNrc) {
		this.managementChargeUnitNrc = managementChargeUnitNrc;
	}

	public Double getManagementChargeNrc() {
		return managementChargeNrc;
	}

	public void setManagementChargeNrc(Double managementChargeNrc) {
		this.managementChargeNrc = managementChargeNrc;
	}

	public Integer getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String UOM) {
		this.UOM = UOM;
	}

	@Override
	public String toString() {
		return "LicensePricesBean{" + "licenseDetail=" + licenseDetail + ", totalMrc=" + totalMrc + ", totalNrc="
				+ totalNrc + ", totalArc=" + totalArc + ", totalTcv=" + totalTcv + ", managementChargeUnitNrc="
				+ managementChargeUnitNrc + ", managementChargeNrc=" + managementChargeNrc + ", contractTerm="
				+ contractTerm + ", UOM='" + UOM + '\'' + '}';
	}
}