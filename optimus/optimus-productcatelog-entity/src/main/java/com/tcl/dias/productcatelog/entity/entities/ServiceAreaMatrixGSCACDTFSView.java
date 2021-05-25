package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the vw_service_area_matrix_GSC_ACDTFS table
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Immutable
@Table(name = "vw_service_area_matrix_GSC_ACDTFS")
@NamedQuery(name = "ServiceAreaMatrixGSCACDTFSView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCACDTFSView v")
public class ServiceAreaMatrixGSCACDTFSView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer uid;

	@Column(name = "Pdt_Name")
	private String productName;

	@Column(name = "Iso_3_Ctry_Cd")
	private String iso3CountryCode;

	@Column(name = "Iso_Ctry_Name")
	private String isoCountryName;

	@Column(name = "estimated_standard_lead_time_days")
	private String estimatedStandardLeadTimeDays;

	@Column(name = "Interntional_Country_Dial_Codes")
	private String internationalCountryDialCodes;

	@Column(name = "data_Ctry_Name")
	private String dataCountryName;

	@Column(name = "dialing_format_txt")
	private String dialingFormatText;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "fixed_ntw_limitation_txt")
	private String fixedNetworkLimitationText;

	@Column(name = "mobile_ntw_applicable_txt")
	private String mobileNetworkApplicableText;

	@Column(name = "nbr_simultaneous_calls_per_nbr")
	private String numberSimultaneousCallsPerNumber;

	@Column(name = "new_number_avbl_ind")
	private String newNumberAvailableIndicator;

	@Column(name = "payphone_applicable_txt")
	private String payphoneApplicableText;

	@Column(name = "portability_txt")
	private String portabilityText;

	@Column(name = "third_ctry_calling_restriction_txt")
	private String thirdCountryCallingRestrictionText;
	
	@Column(name = "comments_txt")
	private String commentsText;
	
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the iso3CountryCode
	 */
	public String getIso3CountryCode() {
		return iso3CountryCode;
	}

	/**
	 * @param iso3CountryCode the iso3CountryCode to set
	 */
	public void setIso3CountryCode(String iso3CountryCode) {
		this.iso3CountryCode = iso3CountryCode;
	}

	/**
	 * @return the isoCountryName
	 */
	public String getIsoCountryName() {
		return isoCountryName;
	}

	/**
	 * @param isoCountryName the isoCountryName to set
	 */
	public void setIsoCountryName(String isoCountryName) {
		this.isoCountryName = isoCountryName;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getEstimatedStandardLeadTimeDays() {
		return estimatedStandardLeadTimeDays;
	}

	public void setEstimatedStandardLeadTimeDays(String estimatedStandardLeadTimeDays) {
		this.estimatedStandardLeadTimeDays = estimatedStandardLeadTimeDays;
	}

	public String getInternationalCountryDialCodes() {
		return internationalCountryDialCodes;
	}

	public void setInternationalCountryDialCodes(String internationalCountryDialCodes) {
		this.internationalCountryDialCodes = internationalCountryDialCodes;
	}

	public String getCommentsText() {
		return commentsText;
	}

	public void setCommentsText(String commentsText) {
		this.commentsText = commentsText;
	}

	public String getDataCountryName() {
		return dataCountryName;
	}

	public void setDataCountryName(String dataCountryName) {
		this.dataCountryName = dataCountryName;
	}

	public String getDialingFormatText() {
		return dialingFormatText;
	}

	public void setDialingFormatText(String dialingFormatText) {
		this.dialingFormatText = dialingFormatText;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFixedNetworkLimitationText() {
		return fixedNetworkLimitationText;
	}

	public void setFixedNetworkLimitationText(String fixedNetworkLimitationText) {
		this.fixedNetworkLimitationText = fixedNetworkLimitationText;
	}

	public String getMobileNetworkApplicableText() {
		return mobileNetworkApplicableText;
	}

	public void setMobileNetworkApplicableText(String mobileNetworkApplicableText) {
		this.mobileNetworkApplicableText = mobileNetworkApplicableText;
	}

	public String getNumberSimultaneousCallsPerNumber() {
		return numberSimultaneousCallsPerNumber;
	}

	public void setNumberSimultaneousCallsPerNumber(String numberSimultaneousCallsPerNumber) {
		this.numberSimultaneousCallsPerNumber = numberSimultaneousCallsPerNumber;
	}

	public String getNewNumberAvailableIndicator() {
		return newNumberAvailableIndicator;
	}

	public void setNewNumberAvailableIndicator(String newNumberAvailableIndicator) {
		this.newNumberAvailableIndicator = newNumberAvailableIndicator;
	}

	public String getPayphoneApplicableText() {
		return payphoneApplicableText;
	}

	public void setPayphoneApplicableText(String payphoneApplicableText) {
		this.payphoneApplicableText = payphoneApplicableText;
	}

	public String getPortabilityText() {
		return portabilityText;
	}

	public void setPortabilityText(String portabilityText) {
		this.portabilityText = portabilityText;
	}

	public String getThirdCountryCallingRestrictionText() {
		return thirdCountryCallingRestrictionText;
	}

	public void setThirdCountryCallingRestrictionText(String thirdCountryCallingRestrictionText) {
		this.thirdCountryCallingRestrictionText = thirdCountryCallingRestrictionText;
	}
	
}
