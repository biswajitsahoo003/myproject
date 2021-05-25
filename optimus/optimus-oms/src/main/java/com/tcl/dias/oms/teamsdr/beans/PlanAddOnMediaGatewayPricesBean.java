package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Bean for Plan and Media Gateway prices response
 * 
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "componentName", "quantity", "plan", "mrc", "nrc", "arc", "tcv" })
public class PlanAddOnMediaGatewayPricesBean {

	@JsonProperty("componentName")
	String componentName;

	@JsonProperty("quantity")
	String quantity;

	@JsonProperty("plan")
	String plan;

	@JsonProperty("mrc")
	String mrc;

	@JsonProperty("nrc")
	String nrc;

	@JsonProperty("arc")
	String arc;

	@JsonProperty("tcv")
	String tcv;

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getMrc() {
		return mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}

	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getArc() {
		return arc;
	}

	public void setArc(String arc) {
		this.arc = arc;
	}

	public String getTcv() {
		return tcv;
	}

	public void setTcv(String tcv) {
		this.tcv = tcv;
	}
}
