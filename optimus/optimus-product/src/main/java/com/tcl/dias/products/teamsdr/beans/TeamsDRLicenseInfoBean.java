package com.tcl.dias.products.teamsdr.beans;

/**
 * Bean for sending license information
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDRLicenseInfoBean {

	private String licenseName;
	private String description;
	private String category;
	private String offerId;
	private Integer minSeats;
	private Integer maxSeats;
	private String sfdcProductName;

	public TeamsDRLicenseInfoBean() {
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getMinSeats() {
		return minSeats;
	}

	public void setMinSeats(Integer minSeats) {
		this.minSeats = minSeats;
	}

	public Integer getMaxSeats() {
		return maxSeats;
	}

	public void setMaxSeats(Integer maxSeats) {
		this.maxSeats = maxSeats;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getSfdcProductName() {
		return sfdcProductName;
	}

	public void setSfdcProductName(String sfdcProductName) {
		this.sfdcProductName = sfdcProductName;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseInfoBean{" +
				"licenseName='" + licenseName + '\'' +
				", description='" + description + '\'' +
				", category='" + category + '\'' +
				", offerId='" + offerId + '\'' +
				", minSeats=" + minSeats +
				", maxSeats=" + maxSeats +
				", sfdcProductName=" + sfdcProductName +
				'}';
	}
}
