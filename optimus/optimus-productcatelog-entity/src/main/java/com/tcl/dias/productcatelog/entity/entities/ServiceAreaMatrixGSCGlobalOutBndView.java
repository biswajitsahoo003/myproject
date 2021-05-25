package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the
 * vw_service_area_matrix_GSC_GlobalOutBnd table
 * *
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Immutable
@Table(name = "vw_service_area_matrix_GSC_GlobalOutBnd")
@NamedQuery(name = "ServiceAreaMatrixGSCGlobalOutBndView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCGlobalOutBndView v")
public class ServiceAreaMatrixGSCGlobalOutBndView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer uid;

	@Column(name = "Ctry_Iso_3_Cd")
	private String iso3CountryCode;

	@Column(name = "Ctry_Name")
	private String isoCountryName;

	@Column(name = "phone_type")
	private String phoneType;

	@Column(name = "destination_name")
	private String destinationName;

	@Column(name = "service_level")
	private String serviceLevel;

	@Column(name = "comments")
	private String comments;

	@Column(name = "region")
	private String region;

	@Column(name = "Display_Order")
	private Integer displayOrder;

	@Column(name = "Interntional_Country_Dial_Codes")
	private String internationalCountryDialCodes;

	@Column(name = "internal_comments")
	private String internalComments;


	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
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

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public String getInternalComments() {
		return internalComments;
	}

	public void setInternalComments(String internalComments) {
		this.internalComments = internalComments;
	}
}
