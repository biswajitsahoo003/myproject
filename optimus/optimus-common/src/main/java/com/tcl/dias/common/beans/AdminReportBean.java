package com.tcl.dias.common.beans;

import java.util.HashMap;

/**
 * @author krutsrin
 *
 */
public class AdminReportBean {
	
	String region;
	HashMap<String, HashMap<String,Double>> stat;
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public HashMap<String, HashMap<String, Double>> getStat() {
		return stat;
	}
	public void setStat(HashMap<String, HashMap<String, Double>> stat) {
		this.stat = stat;
	}

}
