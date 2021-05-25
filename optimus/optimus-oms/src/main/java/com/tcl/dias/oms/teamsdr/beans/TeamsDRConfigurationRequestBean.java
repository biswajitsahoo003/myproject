package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

/**
 * This file contains the TeamsDRConfigurationRequestBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRConfigurationRequestBean {
	List<TeamsDRPricingRequestBean> offerings;
	MediaGatewayConfigurationBean configuration;

	public TeamsDRConfigurationRequestBean() {
	}

	public List<TeamsDRPricingRequestBean> getOfferings() {
		return offerings;
	}

	public void setOfferings(List<TeamsDRPricingRequestBean> offerings) {
		this.offerings = offerings;
	}

	public MediaGatewayConfigurationBean getConfiguration() {
		return configuration;
	}

	public void setConfiguration(MediaGatewayConfigurationBean configuration) {
		this.configuration = configuration;
	}

	@Override
	public String toString() {
		return "TeamsDRConfigurationRequestBean{" + "offerings=" + offerings + ", configuration=" + configuration + '}';
	}
}
