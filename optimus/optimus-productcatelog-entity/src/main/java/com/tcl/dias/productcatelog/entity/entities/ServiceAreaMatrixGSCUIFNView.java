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
@Table(name = "vw_service_area_matrix_GSC_UIFN")
@Immutable
@NamedQuery(name = "ServiceAreaMatrixGSCUIFNView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCUIFNView v")
public class ServiceAreaMatrixGSCUIFNView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer UID;

	@Column(name = "Pdt_Name")
	private String productName;

	@Column(name = "Iso_3_Ctry_Cd")
	private String iso3CountryCode;

	@Column(name = "Iso_Ctry_Name")
	private String isoCountryName;

	@Column(name = "Data_Ctry_Name")
	private String dataCountryName;

	@Column(name = "comments_txt")
	private String commentsText;

	@Column(name = "fixed_ntw_limitation_txt")
	private String fixedNetworkLimitationText;

	@Column(name = "mobile_ntw_applicable_txt")
	private String mobileNetworkApplicableText;

	@Column(name = "payphone_applicable_txt")
	private String payphoneApplicableText;

	@Column(name = "third_ctry_calling_restriction_txt")
	private String thirdCountryCallingRestrictionText;

	@Column(name = "new_number_avbl_ind")
	private String newNumberAvailableInicator;

	@Column(name = "International_access_code")
	private String InternationalAccessCode;

	@Column(name = "nbr_simultaneous_calls_per_nbr")
	private String numberSimultaneousCallsPerNumber;

	@Column(name = "estimated_standard_lead_time_days")
	private String estimatedStandardLeadTimeDays;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "Interntional_Country_Dial_Codes")
	private String internationalCountryDialCodes;

	@Column(name = "country_type_src_dest")
	private String countryTypeSrcDest;

	public ServiceAreaMatrixGSCUIFNView() {

	}

	public Integer getUID() {
		return UID;
	}

	public void setUID(Integer UID) {
		this.UID = UID;
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

	public String getDataCountryName() {
		return dataCountryName;
	}

	public void setDataCountryName(String dataCountryName) {
		this.dataCountryName = dataCountryName;
	}

	public String getCommentsText() {
		return commentsText;
	}

	public void setCommentsText(String commentsText) {
		this.commentsText = commentsText;
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

	public String getPayphoneApplicableText() {
		return payphoneApplicableText;
	}

	public void setPayphoneApplicableText(String payphoneApplicableText) {
		this.payphoneApplicableText = payphoneApplicableText;
	}

	public String getThirdCountryCallingRestrictionText() {
		return thirdCountryCallingRestrictionText;
	}

	public void setThirdCountryCallingRestrictionText(String thirdCountryCallingRestrictionText) {
		this.thirdCountryCallingRestrictionText = thirdCountryCallingRestrictionText;
	}

	public String getNewNumberAvailableInicator() {
		return newNumberAvailableInicator;
	}

	public void setNewNumberAvailableInicator(String newNumberAvailableInicator) {
		this.newNumberAvailableInicator = newNumberAvailableInicator;
	}

	public String getInternationalAccessCode() {
		return InternationalAccessCode;
	}

	public void setInternationalAccessCode(String internationalAccessCode) {
		InternationalAccessCode = internationalAccessCode;
	}

	public String getNumberSimultaneousCallsPerNumber() {
		return numberSimultaneousCallsPerNumber;
	}

	public void setNumberSimultaneousCallsPerNumber(String numberSimultaneousCallsPerNumber) {
		this.numberSimultaneousCallsPerNumber = numberSimultaneousCallsPerNumber;
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

	public String getCountryTypeSrcDest() {
		return countryTypeSrcDest;
	}

	public void setCountryTypeSrcDest(String countryTypeSrcDest) {
		this.countryTypeSrcDest = countryTypeSrcDest;
	}
}
