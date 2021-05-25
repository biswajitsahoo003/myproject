package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Managed services price request
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagedServicesPricesBean {

	@JsonProperty("comp_variant")
	private String compVariant;

	@JsonProperty("no_of_users")
	private Integer noOfUsers;

	@JsonProperty("custom_components")
	private List<CustomComponentPricesBean> customComponents;

	@JsonProperty("unit_mrc")
	private Double unitMrc;

	@JsonProperty("unit_nrc")
	private Double unitNrc;

	@JsonProperty("total_mrc")
	private Double totalMrc;

	@JsonProperty("total_nrc")
	private Double totalNrc;

	@JsonProperty("total_arc")
	private Double totalArc;

	@JsonProperty("total_tcv")
	private Double totalTcv;

	@JsonProperty("usage")
	private Double usage;

	@JsonProperty("overage")
	private Double overage;

	@JsonProperty("service_detail")
	private List<TeamsDRServiceDetailPricesBean> serviceDetail;

	@JsonProperty("contract_term")
	private Integer contractTerm;

	@JsonProperty("plan")
	private String plan;

	@JsonProperty("uom")
	private String UOM;

	public String getCompVariant() {
		return compVariant;
	}

	public void setCompVariant(String compVariant) {
		this.compVariant = compVariant;
	}

	public Integer getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(Integer noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public List<CustomComponentPricesBean> getCustomComponents() {
		return customComponents;
	}

	public void setCustomComponents(List<CustomComponentPricesBean> customComponents) {
		this.customComponents = customComponents;
	}

	public Double getUnitMrc() {
		return unitMrc;
	}

	public void setUnitMrc(Double unitMrc) {
		this.unitMrc = unitMrc;
	}

	public Double getUnitNrc() {
		return unitNrc;
	}

	public void setUnitNrc(Double unitNrc) {
		this.unitNrc = unitNrc;
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

	public Double getUsage() {
		return usage;
	}

	public void setUsage(Double usage) {
		this.usage = usage;
	}

	public Double getOverage() {
		return overage;
	}

	public void setOverage(Double overage) {
		this.overage = overage;
	}

	public List<TeamsDRServiceDetailPricesBean> getServiceDetail() {
		return serviceDetail;
	}

	public void setServiceDetail(List<TeamsDRServiceDetailPricesBean> serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public Integer getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String UOM) {
		this.UOM = UOM;
	}

	@Override
	public String toString() {
		return "ManagedServicesPricesBean{" + "compVariant='" + compVariant + '\'' + ", noOfUsers=" + noOfUsers
				+ ", customComponents=" + customComponents + ", unitMrc=" + unitMrc + ", unitNrc=" + unitNrc
				+ ", totalMrc=" + totalMrc + ", totalNrc=" + totalNrc + ", totalArc=" + totalArc + ", totalTcv="
				+ totalTcv + ", usage=" + usage + ", overage=" + overage + ", serviceDetail=" + serviceDetail
				+ ", contractTerm=" + contractTerm + ", plan='" + plan + '\'' + ", UOM='" + UOM + '\'' + '}';
	}
}
