package com.tcl.dias.oms.teamsdr.beans;

import java.math.BigDecimal;
import java.util.List;

import com.tcl.dias.oms.beans.OrderProductComponentBean;

/**
 * Teams DR Order City bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderCityBean {
	private Integer id;
	private String city;
	private String mediaGatewayType;
	private BigDecimal mrc;
	private BigDecimal nrc;
	private BigDecimal arc;
	private BigDecimal tcv;
	private List<OrderProductComponentBean> components;
	private List<TeamsDROrderMediaGatewayBean> mediaGateway;

	public TeamsDROrderCityBean() {

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

	public List<TeamsDROrderMediaGatewayBean> getMediaGateway() {
		return mediaGateway;
	}

	public void setMediaGateway(List<TeamsDROrderMediaGatewayBean> mediaGateway) {
		this.mediaGateway = mediaGateway;

	}

	public String getMediaGatewayType() {
		return mediaGatewayType;
	}

	public void setMediaGatewayType(String mediaGatewayType) {
		this.mediaGatewayType = mediaGatewayType;
	}

	public List<OrderProductComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<OrderProductComponentBean> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "TeamsDRCityBean [id=" + id + ", city=" + city + ", site=" + ", mediaGatewayType=" + mediaGatewayType
				+ ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + "]";
	}

}
