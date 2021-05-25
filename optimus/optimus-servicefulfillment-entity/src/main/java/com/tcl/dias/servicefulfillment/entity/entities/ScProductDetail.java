package com.tcl.dias.servicefulfillment.entity.entities;

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
 * The persistent class for the sc_product_detail database table.
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 * 
 */
@Entity
@Table(name = "sc_product_detail")
@NamedQuery(name = "ScProductDetail.findAll", query = "SELECT s FROM ScProductDetail s")
public class ScProductDetail implements Serializable {

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

	@Column(name = "ppu_rate")
	private Double ppuRate;

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
	@JoinColumn(name = "sc_service_detail_id")
	private ScServiceDetail scServiceDetail;

	// bi-directional many-to-one association to ScComponentAttribute
	@OneToMany(mappedBy = "scProductDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ScProductDetailAttributes> scProductDetailAttributes;

	// bi-directional many-to-one association to odrServiceCommercialComponent
	@OneToMany(mappedBy = "scProductDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ScServiceCommericalComponent> scServiceCommercialComponent = new HashSet<>();
	
	@Column(name = "cloud_code")
	private String cloudCode;

	@Column(name = "parent_cloud_code")
	private String parentCloudCode;
	
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

	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

	public Set<ScProductDetailAttributes> getScProductDetailAttributes() {
		return scProductDetailAttributes;
	}

	public void setScProductDetailAttributes(Set<ScProductDetailAttributes> scProductDetailAttributes) {
		this.scProductDetailAttributes = scProductDetailAttributes;
	}

	public Set<ScServiceCommericalComponent> getScServiceCommercialComponent() {
		return scServiceCommercialComponent;
	}

	public void setScServiceCommercialComponent(Set<ScServiceCommericalComponent> scServiceCommercialComponent) {
		this.scServiceCommercialComponent = scServiceCommercialComponent;
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

	@Override
	public String toString() {
		return "ScProductDetail [id=" + id + ", type=" + type + ", solutionName=" + solutionName + ", mrc=" + mrc
				+ ", nrc=" + nrc + ", arc=" + arc + ", ppuRate=" + ppuRate + ", isActive=" + isActive + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate="
				+ updatedDate + ", scServiceDetail=" + scServiceDetail + ", scProductDetailAttributes="
				+ scProductDetailAttributes + ", scServiceCommercialComponent=" + scServiceCommercialComponent
				+ ", cloudCode=" + cloudCode + ", parentCloudCode=" + parentCloudCode + "]";
	}

}

