package com.tcl.dias.oms.beans;

import java.util.List;

public class LocationTemplateRequest {
	
	private List<String> profiles;
	
	private List<String> countries;
	
	public LocationTemplateRequest() {
		/**
		 * 
		 * Default Constructor
		 */
	}

	public List<String> getProfiles() {
		return profiles;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	
	
}