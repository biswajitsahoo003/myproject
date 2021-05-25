
package com.tcl.dias.location.beans;

import java.util.List;
/**
 * This is the response bean class used in the NPL location download Template
 * @author chetan chaudhary
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
*/

public class LocationTemplateResponseBean {
	
	private List<String> profiles;
	
	private List<String> countries;
	
	private List<String> type;

	public List<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}
	
	
}
