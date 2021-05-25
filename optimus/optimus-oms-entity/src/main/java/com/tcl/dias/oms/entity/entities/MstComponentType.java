package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * This file contains the MstComponentType.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="mst_component_type")
@NamedQuery(name="MstComponentType.findAll", query="SELECT m FROM MstComponentType m")
public class MstComponentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="component_type_name")
	private String componentTypeName;

	@Column(name="created_by")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	@Column(name="is_active")
	private String isActive;

	@Column(name="updated_by")
	private String updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	private String uuid;

	//bi-directional many-to-one association to OdrComponent
	@OneToMany(mappedBy="mstComponentType")
	private Set<OdrComponent> odrComponents;

	public MstComponentType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComponentTypeName() {
		return this.componentTypeName;
	}

	public void setComponentTypeName(String componentTypeName) {
		this.componentTypeName = componentTypeName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
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

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Set<OdrComponent> getOdrComponents() {
		return this.odrComponents;
	}

	public void setOdrComponents(Set<OdrComponent> odrComponents) {
		this.odrComponents = odrComponents;
	}

	public OdrComponent addOdrComponent(OdrComponent odrComponent) {
		getOdrComponents().add(odrComponent);
		odrComponent.setMstComponentType(this);

		return odrComponent;
	}

	public OdrComponent removeOdrComponent(OdrComponent odrComponent) {
		getOdrComponents().remove(odrComponent);
		odrComponent.setMstComponentType(null);

		return odrComponent;
	}

}