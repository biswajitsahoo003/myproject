package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the vw_service_area_matrix_GSC_ITFS database table.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_service_area_matrix_GSC_ITFS")
@Immutable
@NamedQuery(name = "ServiceAreaMatrixGSCITFSView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCITFSView v")
public class ServiceAreaMatrixGSCITFSView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "comments_txt")
	private String commentsText;

	@Column(name = "data_Ctry_Name")
	private String dataCountryName;

	@Column(name = "dialing_format_txt")
	private String dialingFormatText;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "estimated_standard_lead_time_days")
	private String estimatedStandardLeadTimeDays;

	@Column(name = "fixed_ntw_limitation_txt")
	private String fixedNetworkLimitationText;

	@Column(name = "iso_3_Ctry_Cd")
	private String iso3CountryCode;

	@Column(name = "iso_Ctry_Name")
	private String isoCountryName;

	@Column(name = "mobile_ntw_applicable_txt")
	private String mobileNetworkApplicableText;

	@Column(name = "nbr_simultaneous_calls_per_nbr")
	private String numberSimultaneousCallsPerNumber;

	@Column(name = "new_number_avbl_ind")
	private String newNumberAvailableIndicator;

	@Column(name = "payphone_applicable_txt")
	private String payphoneApplicableText;

	//	@Id
	@Column(name = "pdt_Name")
	private String productName;

	@Column(name = "portability_txt")
	private String portabilityText;

	@Column(name = "third_ctry_calling_restriction_txt")
	private String thirdCountryCallingRestrictionText;

	@Id
	private Integer uid;

	@Column(name = "Interntional_Country_Dial_Codes")
	private String internationalCountryDialCodes;

	public ServiceAreaMatrixGSCITFSView() {
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

	public String getEstimatedStandardLeadTimeDays() {
		return estimatedStandardLeadTimeDays;
	}

	public void setEstimatedStandardLeadTimeDays(String estimatedStandardLeadTimeDays) {
		this.estimatedStandardLeadTimeDays = estimatedStandardLeadTimeDays;
	}

	public String getFixedNetworkLimitationText() {
		return fixedNetworkLimitationText;
	}

	public void setFixedNetworkLimitationText(String fixedNetworkLimitationText) {
		this.fixedNetworkLimitationText = fixedNetworkLimitationText;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getInternationalCountryDialCodes() {
		return internationalCountryDialCodes;
	}

	public void setInternationalCountryDialCodes(String internationalCountryDialCodes) {
		this.internationalCountryDialCodes = internationalCountryDialCodes;
	}
}
