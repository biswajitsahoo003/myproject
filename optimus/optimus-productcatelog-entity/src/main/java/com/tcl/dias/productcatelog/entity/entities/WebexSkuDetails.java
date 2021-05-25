package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the vw_ucaas_webex_cca_audio_plans_per_seat_sku_dtl
 * database table.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_ucaas_webex_cca_audio_plans_per_seat_sku_dtl")
@NamedQuery(name = "WebexSkuDetails.findAll", query = "SELECT v FROM WebexSkuDetails v")
public class WebexSkuDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "audio_Plan_Abbr")
	private String audioPlanAbbr;

	@Column(name = "audio_Plan_bridge")
	private String audioPlanBridge;

	@Column(name = "base_Name")
	private String baseName;

	@Column(name = "description")
	private String description;

	private String feature;

	@Column(name = "feature_Number")
	private Integer featureNumber;

	@Column(name = "full_Name")
	private String fullName;

	@Id
	private Integer id;

	@Column(name = "license_Type")
	private String licenseType;

	@Column(name = "license_Type_Abbr")
	private String licenseTypeAbbr;

	@Column(name = "list_Price_Mrc")
	private BigDecimal listPriceMrc;

	@Column(name = "list_price_mrc_curr")
	private String listPriceMrcCurr;

	@Column(name = "shortened_Feature_Description")
	private String shortenedFeatureDescription;

	private String sku;

	@Column(name = "topline_SKU")
	private String topLineSku;

	@Column(name = "webex_Offer")
	private String webexOffer;

	public WebexSkuDetails() {
	}

	public String getAudioPlanAbbr() {
		return audioPlanAbbr;
	}

	public void setAudioPlanAbbr(String audioPlanAbbr) {
		this.audioPlanAbbr = audioPlanAbbr;
	}

	public String getAudioPlanBridge() {
		return audioPlanBridge;
	}

	public void setAudioPlanBridge(String audioPlanBridge) {
		this.audioPlanBridge = audioPlanBridge;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Integer getFeatureNumber() {
		return featureNumber;
	}

	public void setFeatureNumber(Integer featureNumber) {
		this.featureNumber = featureNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getLicenseTypeAbbr() {
		return licenseTypeAbbr;
	}

	public void setLicenseTypeAbbr(String licenseTypeAbbr) {
		this.licenseTypeAbbr = licenseTypeAbbr;
	}

	public BigDecimal getListPriceMrc() {
		return listPriceMrc;
	}

	public void setListPriceMrc(BigDecimal listPriceMrc) {
		this.listPriceMrc = listPriceMrc;
	}

	public String getListPriceMrcCurr() {
		return listPriceMrcCurr;
	}

	public void setListPriceMrcCurr(String listPriceMrcCurr) {
		this.listPriceMrcCurr = listPriceMrcCurr;
	}

	public String getShortenedFeatureDescription() {
		return shortenedFeatureDescription;
	}

	public void setShortenedFeatureDescription(String shortenedFeatureDescription) {
		this.shortenedFeatureDescription = shortenedFeatureDescription;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getTopLineSku() {
		return topLineSku;
	}

	public void setTopLineSku(String topLineSku) {
		this.topLineSku = topLineSku;
	}

	public String getWebexOffer() {
		return webexOffer;
	}

	public void setWebexOffer(String webexOffer) {
		this.webexOffer = webexOffer;
	}

}