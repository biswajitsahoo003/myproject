package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the vw_service_area_matrix_GSC_LNS table
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_service_area_matrix_GSC_LNS")
@Immutable
@NamedQuery(name = "ServiceAreaMatrixGSCLNSView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCLNSView v")
public class ServiceAreaMatrixGSCLNSView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UID")
	private String uid;

	@Column(name = "Pdt_Name")
	private String productName;

	@Column(name = "Iso_3_Ctry_Cd")
	private String iso3CountryCode;

	@Column(name = "Iso_Ctry_Name")
	private String isoCountryName;
	
	@Column(name = "Internal_City_Code")
	private String internalCityCode;

	@Column(name = "LNS_City_Cd")
	private String cityCode;
	
	@Column(name = "City_Name")
	private String cityName;

	@Column(name = "Area_Code")
	private String areaCode;

	@Column(name = "portability_txt")
	private String portabilityText;

	@Column(name = "comments_txt")
	private String commentsText;

	@Column(name = "nbr_simultaneous_calls_per_nbr")
	private String numberSimultaneousCallsPerNumber;

	@Column(name = "onhold_indicator")
	private String onHoldIndicator;

	@Column(name = "estimated_standard_lead_time_days")
	private String estimatedStandardLeadTimeDays;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "Interntional_Country_Dial_Codes")
	private String internationalCountryDialCodes;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getIso3CountryCode() {
		return iso3CountryCode;
	}

	public void setIso3CountryCode(String iso3CountryCode) {
		this.iso3CountryCode = iso3CountryCode;
	}

	public String getIsoCountryName() {
		return isoCountryName;
	}

	public void setIsoCountryName(String isoCountryName) {
		this.isoCountryName = isoCountryName;
	}

	public String getInternalCityCode() {
		return internalCityCode;
	}

	public void setInternalCityCode(String internalCityCode) {
		this.internalCityCode = internalCityCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPortabilityText() {
		return portabilityText;
	}

	public void setPortabilityText(String portabilityText) {
		this.portabilityText = portabilityText;
	}

	public String getCommentsText() {
		return commentsText;
	}

	public void setCommentsText(String commentsText) {
		this.commentsText = commentsText;
	}

	public String getNumberSimultaneousCallsPerNumber() {
		return numberSimultaneousCallsPerNumber;
	}

	public void setNumberSimultaneousCallsPerNumber(String numberSimultaneousCallsPerNumber) {
		this.numberSimultaneousCallsPerNumber = numberSimultaneousCallsPerNumber;
	}

	public String getOnHoldIndicator() {
		return onHoldIndicator;
	}

	public void setOnHoldIndicator(String onHoldIndicator) {
		this.onHoldIndicator = onHoldIndicator;
	}

	public String getEstimatedStandardLeadTimeDays() {
		return estimatedStandardLeadTimeDays;
	}

	public void setEstimatedStandardLeadTimeDays(String estimatedStandardLeadTimeDays) {
		this.estimatedStandardLeadTimeDays = estimatedStandardLeadTimeDays;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getInternationalCountryDialCodes() {
		return internationalCountryDialCodes;
	}

	public void setInternationalCountryDialCodes(String internationalCountryDialCodes) {
		this.internationalCountryDialCodes = internationalCountryDialCodes;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
