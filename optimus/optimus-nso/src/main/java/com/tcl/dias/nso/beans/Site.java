
package com.tcl.dias.nso.beans;

import java.util.List;

public class Site {

	private Integer siteId;
	private String offeringName;
	private List<Component> components = null;
	private String image;
	private String description;
	private Integer locationId;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
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

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	@Override
	public String toString() {
		return "Site [siteId=" + siteId + ", offeringName=" + offeringName + ", components=" + components + ", image="
				+ image + ", description=" + description + ", locationId=" + locationId + "]";
	}

}