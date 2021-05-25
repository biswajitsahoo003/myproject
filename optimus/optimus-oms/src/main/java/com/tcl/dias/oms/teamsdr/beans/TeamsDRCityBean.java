package com.tcl.dias.oms.teamsdr.beans;

import com.tcl.dias.oms.beans.QuoteProductComponentBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * This file contains the TeamsDRCityBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRCityBean {
	private Integer id;
	private String city;
	private String mediaGatewayType;
	private BigDecimal mrc;
	private BigDecimal nrc;
	private BigDecimal arc;
	private BigDecimal tcv;
	private List<QuoteProductComponentBean> components;
	private List<TeamsDRMediaGatewayBean> mediaGateway;

	public TeamsDRCityBean() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<TeamsDRMediaGatewayBean> getMediaGateway() {
		return mediaGateway;
	}

	public void setMediaGateway(List<TeamsDRMediaGatewayBean> mediaGateway) {
		this.mediaGateway = mediaGateway;

	}

	public String getMediaGatewayType() {
		return mediaGatewayType;
	}

	public void setMediaGatewayType(String mediaGatewayType) {
		this.mediaGatewayType = mediaGatewayType;
	}

	public List<QuoteProductComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<QuoteProductComponentBean> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "TeamsDRCityBean [id=" + id + ", city=" + city + ", site=" + ", mediaGatewayType="
				+ mediaGatewayType + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + "]";
	}

}
