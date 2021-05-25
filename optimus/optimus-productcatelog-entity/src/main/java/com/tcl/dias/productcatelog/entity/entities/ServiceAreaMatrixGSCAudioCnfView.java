package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the vw_service_area_matrix_GSC_AudioCnf table
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Immutable
@Table(name = "vw_service_area_matrix_GSC_AudioCnf")
@NamedQuery(name = "ServiceAreaMatrixGSCAudioCnfView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCAudioCnfView v")
public class ServiceAreaMatrixGSCAudioCnfView implements Serializable {

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
}
