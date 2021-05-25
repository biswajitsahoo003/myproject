package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_asset_relation database table.
 * 
 */
@Entity
@Table(name="si_asset_relation")
public class SIAssetRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="business_relation_name")
	private String businessRelationName;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="is_active")
	private String isActive;

	@Column(name="relation_port")
	private Integer relationPort;

	@Column(name="relation_resiliency")
	private String relationResiliency;

	@Column(name="relation_type")
	private String relationType;

	private String remarks;

	@Column(name="SI_Asset_ID")
	private Integer siAssetId;

	@Column(name="SI_Related_Asset_ID")
	private Integer siRelatedAssetId;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

    public SIAssetRelation() {
    }

	public Integer getId() {
		return this.id;
	}

	

	public String getBusinessRelationName() {
		return this.businessRelationName;
	}

	public void setBusinessRelationName(String businessRelationName) {
		this.businessRelationName = businessRelationName;
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

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getRelationPort() {
		return this.relationPort;
	}

	public void setRelationPort(Integer relationPort) {
		this.relationPort = relationPort;
	}

	public String getRelationResiliency() {
		return this.relationResiliency;
	}

	public void setRelationResiliency(String relationResiliency) {
		this.relationResiliency = relationResiliency;
	}

	public String getRelationType() {
		return this.relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
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

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiAssetId() {
		return siAssetId;
	}

	public void setSiAssetId(Integer siAssetId) {
		this.siAssetId = siAssetId;
	}

	public Integer getSiRelatedAssetId() {
		return siRelatedAssetId;
	}

	public void setSiRelatedAssetId(Integer siRelatedAssetId) {
		this.siRelatedAssetId = siRelatedAssetId;
	}

}