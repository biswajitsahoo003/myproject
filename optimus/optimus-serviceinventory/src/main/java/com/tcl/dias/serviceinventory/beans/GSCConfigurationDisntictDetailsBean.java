package com.tcl.dias.serviceinventory.beans;

import java.util.List;
import java.util.Map;

/**
 * Bean class to hold all distinct details of configurations
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GSCConfigurationDisntictDetailsBean {

	private Map<Integer,String> customerLes;
	private List<String> secsIds;
	private List<String> accessTypes;
	private List<SIConfigurationCountryBean> siConfigurations;

	public Map<Integer, String> getCustomerLes() {
		return customerLes;
	}

	public void setCustomerLes(Map<Integer, String> customerLes) {
		this.customerLes = customerLes;
	}

	public List<String> getSecsIds() {
		return secsIds;
	}

	public void setSecsIds(List<String> secsIds) {
		this.secsIds = secsIds;
	}

	public List<String> getAccessTypes() {
		return accessTypes;
	}

	public void setAccessTypes(List<String> accessTypes) {
		this.accessTypes = accessTypes;
	}

	public List<SIConfigurationCountryBean> getSiConfigurations() {
		return siConfigurations;
	}

	public void setSiConfigurations(List<SIConfigurationCountryBean> siConfigurations) {
		this.siConfigurations = siConfigurations;
	}
}
