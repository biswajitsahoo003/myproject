package com.tcl.dias.servicefulfillment.entity.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the flow_group_attributes database table.
 * 
 */
@Entity
@Table(name="flow_group_attributes")
@NamedQuery(name="FlowGroupAttribute.findAll", query="SELECT f FROM FlowGroupAttribute f")
public class FlowGroupAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="attribute_alt_value_label")
	private String attributeAltValueLabel;

	@Column(name="attribute_name")
	private String attributeName;

	@Lob
	@Column(name="attribute_value")
	private String attributeValue;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "flow_group_id")
	private GscFlowGroup gscFlowGroup;

	@Column(name="is_active")
	private String isActive;

	@Column(name="is_additional_param")
	private String isAdditionalParam;

	@Column(name="sc_service_detail_id")
	private int scServiceDetailId;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	private String uuid;

	public FlowGroupAttribute() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public GscFlowGroup getGscFlowGroup() {
		return gscFlowGroup;
	}

	public void setGscFlowGroup(GscFlowGroup gscFlowGroup) {
		this.gscFlowGroup = gscFlowGroup;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsAdditionalParam() {
		return this.isAdditionalParam;
	}

	public void setIsAdditionalParam(String isAdditionalParam) {
		this.isAdditionalParam = isAdditionalParam;
	}

	public int getScServiceDetailId() {
		return this.scServiceDetailId;
	}

	public void setScServiceDetailId(int scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
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

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}