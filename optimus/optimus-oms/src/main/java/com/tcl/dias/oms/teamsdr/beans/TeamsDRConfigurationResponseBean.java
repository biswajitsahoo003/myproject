package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

/**
 * This file contains the TeamsDRConfigurationRequestBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRConfigurationResponseBean {
	List<TeamsDRServicesResponseBean> offerings;
	TeamsDRConfigurationPricingBean teamsDRConfigurationPricingBean;

	public TeamsDRConfigurationResponseBean() {
	}

	public List<TeamsDRServicesResponseBean> getOfferings() {
		return offerings;
	}

	public void setOfferings(List<TeamsDRServicesResponseBean> offerings) {
		this.offerings = offerings;
	}

	public TeamsDRConfigurationPricingBean getTeamsDRConfigurationPricingBean() {
		return teamsDRConfigurationPricingBean;
	}

	public void setTeamsDRConfigurationPricingBean(TeamsDRConfigurationPricingBean teamsDRConfigurationPricingBean) {
		this.teamsDRConfigurationPricingBean = teamsDRConfigurationPricingBean;
	}

	@Override
	public String toString() {
		return "TeamsDRConfigurationResponseBean{" + "offerings=" + offerings + ", teamsDRConfigurationPricingBean="
				+ teamsDRConfigurationPricingBean + '}';
	}
}
