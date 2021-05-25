package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the TeamDRMediaGatewayPricingBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "id", "name", "quantity", "mrc", "nrc", "arc", "tcv" })
public class TeamDRMediaGatewayPricingBean {
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("quantity")
	private Integer quantity;
	@JsonProperty("mrc")
	private Double mrc;
	@JsonProperty("nrc")
	private Double nrc;
	@JsonProperty("arc")
	private Double arc;
	@JsonProperty("tcv")
	private Double tcv;

	public TeamDRMediaGatewayPricingBean() {

	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("quantity")
	public Integer getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("mrc")
	public Double getMrc() {
		return mrc;
	}

	@JsonProperty("mrc")
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	@JsonProperty("nrc")
	public Double getNrc() {
		return nrc;
	}

	@JsonProperty("nrc")
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	@JsonProperty("arc")
	public Double getArc() {
		return arc;
	}

	@JsonProperty("arc")
	public void setArc(Double arc) {
		this.arc = arc;
	}

	@JsonProperty("tcv")
	public Double getTcv() {
		return tcv;
	}

	@JsonProperty("tcv")
	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public static TeamDRMediaGatewayPricingBean toTeamsDrMediaGatewayPricingBean(
			TeamsDRMediaGatewayBean teamsDRMediaGatewayBean) {
		TeamDRMediaGatewayPricingBean teamDRMediaGatewayPricingBean = new TeamDRMediaGatewayPricingBean();
		teamDRMediaGatewayPricingBean.setId(teamsDRMediaGatewayBean.getId());
		teamDRMediaGatewayPricingBean.setName(teamsDRMediaGatewayBean.getName());
		teamDRMediaGatewayPricingBean.setMrc((teamsDRMediaGatewayBean.getMrc()));
		teamDRMediaGatewayPricingBean.setNrc((teamsDRMediaGatewayBean.getNrc()));
		teamDRMediaGatewayPricingBean.setArc((teamsDRMediaGatewayBean.getArc()));
		teamDRMediaGatewayPricingBean.setTcv((teamsDRMediaGatewayBean.getTcv()));
		teamDRMediaGatewayPricingBean.setQuantity(teamsDRMediaGatewayBean.getQuantity());
		return teamDRMediaGatewayPricingBean;
	}

	@Override
	public String toString() {
		return "TeamDRMediaGatewayPricingBean{" + "id=" + id + ", name='" + name + '\'' + ", quantity=" + quantity
				+ ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + '}';
	}
}
