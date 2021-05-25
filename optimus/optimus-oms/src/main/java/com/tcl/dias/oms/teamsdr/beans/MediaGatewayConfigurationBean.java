package com.tcl.dias.oms.teamsdr.beans;


import java.math.BigDecimal;
import java.util.List;

/**
 * This file contains the DirectRoutingConfigurationBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class MediaGatewayConfigurationBean {
	private Integer id;
	private String country;
	private List<TeamsDRCityBean> cities;
	private BigDecimal mrc;
	private BigDecimal nrc;
	private BigDecimal arc;
	private BigDecimal tcv;

	public MediaGatewayConfigurationBean() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<TeamsDRCityBean> getCities() {
		return cities;
	}

	public void setCities(List<TeamsDRCityBean> cities) {
		this.cities = cities;
	}

	public BigDecimal getMrc() {
		return mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public BigDecimal getNrc() {
		return nrc;
	}

	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	public BigDecimal getArc() {
		return arc;
	}

	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	public BigDecimal getTcv() {
		return tcv;
	}

	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}

}

