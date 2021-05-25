package com.tcl.dias.oms.gde.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class GdeFeasibilityInputBean
 * @author archchan
 *
 */
public class GdeFeasibilityInputBean {
	
	@JsonProperty("bw_profile")
	private GdeFeasibilityBwProfile bwProfile;
	private String start_time;
	private String end_time;
	private Integer exclusive_time;
//	private String callback_url;
	private String title;
	
	public GdeFeasibilityBwProfile getBwProfile() {
		return bwProfile;
	}
	public void setBwProfile(GdeFeasibilityBwProfile bwProfile) {
		this.bwProfile = bwProfile;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Integer getExclusive_time() {
		return exclusive_time;
	}
	public void setExclusive_time(Integer exclusive_time) {
		this.exclusive_time = exclusive_time;
	}
//	public String getCallback_url() {
//		return callback_url;
//	}
//	public void setCallback_url(String callback_url) {
//		this.callback_url = callback_url;
//	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
