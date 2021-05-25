package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

/**
 * Wrapper class to hold list of service level charges
 *
 */
public class ServiceLevelChargesWrapper {

	private List<ServiceLevelChargesBean> serviceLevelCharges;

	public List<ServiceLevelChargesBean> getServiceLevelCharges() {
		return serviceLevelCharges;
	}

	public void setServiceLevelCharges(List<ServiceLevelChargesBean> serviceLevelCharges) {
		this.serviceLevelCharges = serviceLevelCharges;
	}
}
