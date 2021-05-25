package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the gsc_flow_group_to_asset database table.
 * 
 */

@Entity
@Table(name="gsc_flow_group_to_asset")
@NamedQuery(name="GscFlowGroupToAsset.findAll", query="SELECT g FROM GscFlowGroupToAsset g")
public class GscFlowGroupToAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="gsc_flow_group_id")
	private Integer gscFlowGroupId;

	@Column(name="is_active")
	private byte isActive;

	@Column(name="sc_asset_id")
	private Integer scAssetId;

	public GscFlowGroupToAsset() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getGscFlowGroupId() {
		return this.gscFlowGroupId;
	}

	public void setGscFlowGroupId(Integer gscFlowGroupId) {
		this.gscFlowGroupId = gscFlowGroupId;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public Integer getScAssetId() {
		return this.scAssetId;
	}

	public void setScAssetId(Integer scAssetId) {
		this.scAssetId = scAssetId;
	}

}