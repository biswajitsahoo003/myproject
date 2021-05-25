package com.tcl.dias.location.beans;

import java.util.List;

public class CrossConnectLocationResponse {

	private List<Integer> locationIdList;
	
	private List<Integer> localItContactIdList;

	public List<Integer> getLocationIdList() {
		return locationIdList;
	}

	public void setLocationIdList(List<Integer> locationIdList) {
		this.locationIdList = locationIdList;
	}

	public List<Integer> getLocalItContactIdList() {
		return localItContactIdList;
	}

	public void setLocalItContactIdList(List<Integer> localItContactIdList) {
		this.localItContactIdList = localItContactIdList;
	}
	
	

}
