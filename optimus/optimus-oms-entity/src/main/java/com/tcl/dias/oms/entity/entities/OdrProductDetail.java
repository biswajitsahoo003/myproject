package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * This file holds the entity bean for the order product details flat table that
 * will be used in IPC to persist the IPC product.
 * 
 *
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Entity
@Table(name = "odr_product_detail")
@NamedQuery(name = "OdrProductDetail.findAll", query = "SELECT o FROM OdrProductDetail o")
public class OdrProductDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String type;

	@Column(name = "solution_name")
	private String solutionName;

	private Double mrc;

	private Double nrc;

	private Double arc;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail;

	// bi-directional many-to-one association to ScComponentAttribute
	@OneToMany(mappedBy = "odrProductDetail", cascade = CascadeType.ALL)
	private Set<OdrProductDetailAttributes> odrProductDetailAttributes = new HashSet<>();

	// bi-directional many-to-one association to odrServiceCommercialComponent
	@OneToMany(mappedBy = "odrProductDetail", cascade = CascadeType.ALL)
	private Set<OdrServiceCommercialComponent> odrServiceCommercialComponent = new HashSet<>();

	@Column(name = "cloud_code")
	private String cloudCode;

	@Column(name = "parent_cloud_code")
	private String parentCloudCode;

	@Column(name = "ppu_rate")
	private Double ppuRate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public OdrServiceDetail getOdrServiceDetail() {
		return odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
	}

	public Set<OdrProductDetailAttributes> getOdrProductDetailAttributes() {
		return odrProductDetailAttributes;
	}

	public void setOdrProductDetailAttributes(Set<OdrProductDetailAttributes> odrProductDetailAttributes) {
		this.odrProductDetailAttributes = odrProductDetailAttributes;
	}

	public Set<OdrServiceCommercialComponent> getOdrServiceCommercialComponent() {
		return odrServiceCommercialComponent;
	}

	public void setOdrServiceCommercialComponent(Set<OdrServiceCommercialComponent> odrServiceCommercialComponent) {
		this.odrServiceCommercialComponent = odrServiceCommercialComponent;
	}

	/*
	 * public OdrProductDetailAttributes
	 * addOdrDetailAttributes(OdrProductDetailAttributes
	 * odrProductDetailAttributes) {
	 * getOdrProductDetailAttributes().add(odrProductDetailAttributes);
	 * odrProductDetailAttributes.setOdrProductDetail(this); return
	 * odrProductDetailAttributes; }
	 * 
	 * public OdrProductDetailAttributes
	 * removeOdrDetailAttributes(OdrProductDetailAttributes
	 * odrProductDetailAttributes) {
	 * getOdrProductDetailAttributes().remove(odrProductDetailAttributes);
	 * odrProductDetailAttributes.setOdrProductDetail(null); return
	 * odrProductDetailAttributes; }
	 */

	public OdrServiceCommercialComponent addOdrServiceCommercialComponent(
			OdrServiceCommercialComponent odrServiceCommercialComponent) {
		getOdrServiceCommercialComponent().add(odrServiceCommercialComponent);
		odrServiceCommercialComponent.setOdrProductDetail(this);
		return odrServiceCommercialComponent;
	}

	public OdrServiceCommercialComponent removeOdrServiceCommercialComponent(
			OdrServiceCommercialComponent odrServiceCommercialComponent) {
		getOdrServiceCommercialComponent().remove(odrServiceCommercialComponent);
		odrServiceCommercialComponent.setOdrProductDetail(null);
		return odrServiceCommercialComponent;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}

	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

}
