package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * This file contains the MstComponentType.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_component_type")
@NamedQuery(name = "MstComponentType.findAll", query = "SELECT m FROM MstComponentType m")
public class MstComponentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "component_type_name")
	private String componentTypeName;

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

	private String uuid;

	// bi-directional many-to-one association to ScComponent
	@OneToMany(mappedBy = "mstComponentType")
	private Set<ScComponent> scComponents;

	public MstComponentType() {
		// DO NOTHING
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

	public Set<ScComponent> getScComponents() {
		return this.scComponents;
	}

	public void setScComponents(Set<ScComponent> scComponents) {
		this.scComponents = scComponents;
	}

	public ScComponent addScComponent(ScComponent scComponent) {
		getScComponents().add(scComponent);
		scComponent.setMstComponentType(this);

		return scComponent;
	}

	public ScComponent removeScComponent(ScComponent scComponent) {
		getScComponents().remove(scComponent);
		scComponent.setMstComponentType(null);

		return scComponent;
	}

}