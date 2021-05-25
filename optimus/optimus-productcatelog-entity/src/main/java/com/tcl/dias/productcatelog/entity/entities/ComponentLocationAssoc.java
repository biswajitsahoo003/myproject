package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "component_location_assoc")
@NamedQuery(name = "ComponentLocationAssoc.findAll", query = "SELECT c FROM ComponentLocationAssoc c")
public class ComponentLocationAssoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ComponentLocationAssocPK id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "long_desc")
	private String longDesc;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_dt")
	private Date updatedDt;

	// bi-directional many-to-one association to Component
	@ManyToOne
	@JoinColumn(name = "comp_id", insertable = false, updatable = false)
	private Component component;

	// bi-directional many-to-one association to Location
	@ManyToOne
	@JoinColumn(name = "loc_id", insertable = false, updatable = false)
	private Location location;

	public ComponentLocationAssocPK getId() {
		return this.id;
	}

	public void setId(ComponentLocationAssocPK id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public Component getComponent() {
		return this.component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}