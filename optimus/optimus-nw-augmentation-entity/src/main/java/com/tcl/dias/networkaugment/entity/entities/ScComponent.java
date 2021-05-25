package com.tcl.dias.networkaugment.entity.entities;

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
 * This file contains the ScComponent.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "sc_component")
@NamedQuery(name = "ScComponent.findAll", query = "SELECT s FROM ScComponent s")
public class ScComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "component_name")
	private String componentName;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "site_type")
	private String siteType;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	private String uuid;

	@Column(name = "sc_service_detail_id")
	private Integer scServiceDetailId;

	// bi-directional many-to-one association to MstComponentType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_type_id")
	private MstComponentType mstComponentType;

	// bi-directional many-to-one association to ScComponentAttribute
	@OneToMany(mappedBy = "scComponent",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<ScComponentAttribute> scComponentAttributes=new HashSet<>();

	public ScComponent() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComponentName() {
		return this.componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
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

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public MstComponentType getMstComponentType() {
		return this.mstComponentType;
	}

	public void setMstComponentType(MstComponentType mstComponentType) {
		this.mstComponentType = mstComponentType;
	}

	public Integer getScServiceDetailId() {
		return scServiceDetailId;
	}

	public void setScServiceDetailId(Integer scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
	}

	public Set<ScComponentAttribute> getScComponentAttributes() {
		return this.scComponentAttributes;
	}

	public void setScComponentAttributes(Set<ScComponentAttribute> scComponentAttributes) {
		this.scComponentAttributes = scComponentAttributes;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public ScComponentAttribute addScComponentAttribute(ScComponentAttribute scComponentAttribute) {
		getScComponentAttributes().add(scComponentAttribute);
		scComponentAttribute.setScComponent(this);

		return scComponentAttribute;
	}

	public ScComponentAttribute removeScComponentAttribute(ScComponentAttribute scComponentAttribute) {
		getScComponentAttributes().remove(scComponentAttribute);
		scComponentAttribute.setScComponent(null);

		return scComponentAttribute;
	}

}