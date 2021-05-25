package com.tcl.dias.oms.teamsdr.beans;

import java.math.BigDecimal;
import java.util.List;

import com.tcl.dias.oms.beans.OrderProductComponentBean;

/**
 * Teams DR Order configuration bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderConfigurationBean {
	private Integer id;
	private String country;
	private Integer noOfNamedUsers;
	private Integer noOfCommonAreaDevices;
	private Integer totalUsers;
	private List<OrderProductComponentBean> components;
	private BigDecimal mrc;
	private BigDecimal nrc;
	private BigDecimal arc;
	private BigDecimal tcv;

	public TeamsDROrderConfigurationBean() {
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

	public Integer getNoOfNamedUsers() {
		return noOfNamedUsers;
	}

	public void setNoOfNamedUsers(Integer noOfNamedUsers) {
		this.noOfNamedUsers = noOfNamedUsers;
	}

	public Integer getNoOfCommonAreaDevices() {
		return noOfCommonAreaDevices;
	}

	public void setNoOfCommonAreaDevices(Integer noOfCommonAreaDevices) {
		this.noOfCommonAreaDevices = noOfCommonAreaDevices;
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

	public Integer getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}

	public List<OrderProductComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<OrderProductComponentBean> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "TeamsDRConfigurationBean{" + "id=" + id + ", country='" + country + '\'' + ", noOfNamedUsers="
				+ noOfNamedUsers + ", noOfCommonAreaDevices=" + noOfCommonAreaDevices + ", totalUsers=" + totalUsers
				+ ", components=" + components + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv
				+ '}';
	}
}
