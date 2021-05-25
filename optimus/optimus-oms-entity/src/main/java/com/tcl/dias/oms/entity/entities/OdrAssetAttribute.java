package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited The persistent class for the
 *  odr_asset_attributes database table.
 * 
 */

@Entity
@Table(name = "odr_asset_attributes")
public class OdrAssetAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_alt_value_label")
	private String attributeAltValueLabel;

	@Column(name = "attribute_name")
	private String attributeName;

	@Column(name = "attribute_value")
	private String attributeValue;

	private String category;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	// bi-directional many-to-one association to ScAsset
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "odr_asset_id")
	private OdrAsset odrAsset;

	public OdrAssetAttribute() {
	}

	public String getAttributeAltValueLabel() {
		return this.attributeAltValueLabel;
	}

	public void setAttributeAltValueLabel(String attributeAltValueLabel) {
		this.attributeAltValueLabel = attributeAltValueLabel;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getId() {
		return id;
	}

	public OdrAsset getOdrAsset() {
		return odrAsset;
	}

	public void setOdrAsset(OdrAsset odrAsset) {
		this.odrAsset = odrAsset;
	}
}