package com.tcl.dias.products.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductProfileDto {
	private int profileId;
	private String profileName;
	private boolean isBackup;
	private String image;
	private String description;   
    private boolean showMore;
    private List<Integer> componentIds;
    private Map<String,String> networkInTime;
    
	public Map<String, String> getNetworkInTime() {
		return networkInTime;
	}
	public void setNetworkInTime(Map<String, String> networkInTime) {
		this.networkInTime = networkInTime;
	}
	public List<Integer> getComponentIds() {
		return componentIds;
	}
	public void setComponentIds(List<Integer> componentIds) {
		this.componentIds = componentIds;
	}
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public boolean isBackup() {
		return isBackup;
	}
	public void setBackup(boolean isBackup) {
		this.isBackup = isBackup;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isShowMore() {
		return showMore;
	}
	public void setShowMore(boolean showMore) {
		this.showMore = showMore;
	}
	
}
