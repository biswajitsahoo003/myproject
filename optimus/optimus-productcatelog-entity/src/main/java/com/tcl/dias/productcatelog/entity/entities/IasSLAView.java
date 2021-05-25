package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Immutable;

/**
 * 
 * @author Dinahar Vivekanandan The persistent class for the vw_sla_pdt_with_id_IAS
 *         database table.
 * 
 */
@Entity
@Immutable
@Table(name = "vw_sla_pdt_with_id_IAS")
@IdClass(IasSlaViewId.class)

public class IasSLAView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "Pdt_Name")
	private String productName;

	@Column(name = "pdt_catalog_id")
	private Integer productCatalogId;

	@Column(name = "SLA_Name")
	private String slaName;

	@Id
	@Column(name = "SLA_Id")
	private Integer slaId;

	@Column(name = "sla_metric_id")
	private Integer slaMetricId;

	@Column(name = "slt_varient")
	private String sltVariant;

	@Column(name = "tier1_value")
	private String tier1Value;

	@Column(name = "tier2_value")
	private String tier2Value;

	@Column(name = "tier3_value")
	private String tier3Value;

	@Column(name = "default_value")
	private String defaultValue;

	@Column(name = "remarks_txt")
	private String remarksText;

	@Column(name = "Factor_Name")
	private String factorName;

	@Id
	@Column(name = "Factor_Name_Id")
	private Integer factorNameId;

	@Column(name = "Factor_Value")
	private String factorValue;

	@Id
	@Column(name = "Factor_Value_Id")
	private Integer factorValueId;

	@Column(name = "SubGrp_Id")
	private Integer subGroupId;

	@Id
	@Column(name = "SLAIdNo")
	private Integer slaIdNo;

	public IasSLAView() {
		// TO DO

	}

	public String getFactorName() {
		return factorName;
	}

	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}

	public String getFactorValue() {
		return factorValue;
	}

	public void setFactorValue(String factorValue) {
		this.factorValue = factorValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getSlaId() {
		return slaId;
	}

	public void setSlaId(Integer slaId) {
		this.slaId = slaId;
	}

	public Integer getFactorNameId() {
		return factorNameId;
	}

	public void setFactorNameId(Integer factorNameId) {
		this.factorNameId = factorNameId;
	}

	public Integer getFactorValueId() {
		return factorValueId;
	}

	public void setFactorValueId(Integer factorValueId) {
		this.factorValueId = factorValueId;
	}

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
	 * @return the productCatalogId
	 */
	public Integer getProductCatalogId() {
		return productCatalogId;
	}

	/**
	 * @param productCatalogId the productCatalogId to set
	 */
	public void setProductCatalogId(Integer productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	/**
	 * @return the slaName
	 */
	public String getSlaName() {
		return slaName;
	}

	/**
	 * @param slaName the slaName to set
	 */
	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	/**
	 * @return the slaMetricId
	 */
	public Integer getSlaMetricId() {
		return slaMetricId;
	}

	/**
	 * @param slaMetricId the slaMetricId to set
	 */
	public void setSlaMetricId(Integer slaMetricId) {
		this.slaMetricId = slaMetricId;
	}

	/**
	 * @return the sltVariant
	 */
	public String getSltVariant() {
		return sltVariant;
	}

	/**
	 * @param sltVariant the sltVariant to set
	 */
	public void setSltVariant(String sltVariant) {
		this.sltVariant = sltVariant;
	}

	/**
	 * @return the tier1Value
	 */
	public String getTier1Value() {
		return tier1Value;
	}

	/**
	 * @param tier1Value the tier1Value to set
	 */
	public void setTier1Value(String tier1Value) {
		this.tier1Value = tier1Value;
	}

	/**
	 * @return the tier2Value
	 */
	public String getTier2Value() {
		return tier2Value;
	}

	/**
	 * @param tier2Value the tier2Value to set
	 */
	public void setTier2Value(String tier2Value) {
		this.tier2Value = tier2Value;
	}

	/**
	 * @return the tier3Value
	 */
	public String getTier3Value() {
		return tier3Value;
	}

	/**
	 * @param tier3Value the tier3Value to set
	 */
	public void setTier3Value(String tier3Value) {
		this.tier3Value = tier3Value;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the remarksText
	 */
	public String getRemarksText() {
		return remarksText;
	}

	/**
	 * @param remarksText the remarksText to set
	 */
	public void setRemarksText(String remarksText) {
		this.remarksText = remarksText;
	}

	/**
	 * @return the subGroupId
	 */
	public Integer getSubGroupId() {
		return subGroupId;
	}

	/**
	 * @param subGroupId the subGroupId to set
	 */
	public void setSubGroupId(Integer subGroupId) {
		this.subGroupId = subGroupId;
	}

	/**
	 * @return the slaIdNo
	 */
	public Integer getSlaIdNo() {
		return slaIdNo;
	}

	/**
	 * @param slaIdNo the slaIdNo to set
	 */
	public void setSlaIdNo(Integer slaIdNo) {
		this.slaIdNo = slaIdNo;
	}
	
	

}