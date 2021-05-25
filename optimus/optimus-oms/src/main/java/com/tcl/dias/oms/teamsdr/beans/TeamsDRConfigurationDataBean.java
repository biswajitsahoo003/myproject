package com.tcl.dias.oms.teamsdr.beans;


import java.math.BigDecimal;

/**
 * This file contains the TeamsDRConfigurationDataBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRConfigurationDataBean {
	private BigDecimal totalMrc;
	private BigDecimal totalNrc;
	private BigDecimal totalArc;
	private BigDecimal totalTcv;
	private MediaGatewayConfigurationBean configurations;

	public TeamsDRConfigurationDataBean() {

	}

	public BigDecimal getTotalMrc() {
		return totalMrc;
	}

	public void setTotalMrc(BigDecimal totalMrc) {
		this.totalMrc = totalMrc;
	}


	public BigDecimal getTotalNrc() {
		return totalNrc;
	}

	public void setTotalNrc(BigDecimal totalNrc) {
		this.totalNrc = totalNrc;
	}

	public BigDecimal getTotalArc() {
		return totalArc;
	}

	public void setTotalArc(BigDecimal totalArc) {
		this.totalArc = totalArc;
	}

	public BigDecimal getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(BigDecimal totalTcv) {
		this.totalTcv = totalTcv;
	}


	public MediaGatewayConfigurationBean getConfigurations() {
		return configurations;
	}

	public void setConfigurations(MediaGatewayConfigurationBean configurations) {
		this.configurations = configurations;
	}

	@Override
	public String toString() {
		return "TeamsDRConfigurationDataBean{" +
				"totalMrc=" + totalMrc +
				", totalNrc=" + totalNrc +
				", totalArc=" + totalArc +
				", totalTcv=" + totalTcv +
				", configurations=" + configurations +
				'}';
	}
}
