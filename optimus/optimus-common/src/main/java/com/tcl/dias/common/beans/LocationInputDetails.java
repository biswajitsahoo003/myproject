package com.tcl.dias.common.beans;

import java.util.List;

public class LocationInputDetails {
	
	private String textToSearch;
	
	private List<Integer> locationIds;


	public String getTextToSearch() {
		return textToSearch;
	}

	public void setTextToSearch(String textToSearch) {
		this.textToSearch = textToSearch;
	}

	public List<Integer> getLocationIds() {
		return locationIds;
	}

	public void setLocationIds(List<Integer> locationIds) {
		this.locationIds = locationIds;
	}
	
	

}
