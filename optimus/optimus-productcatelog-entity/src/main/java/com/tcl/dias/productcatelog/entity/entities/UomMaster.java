package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "uom_master")
@NamedQuery(name = "UomMaster.findAll", query = "SELECT u FROM UomMaster u")
public class UomMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cd")
	private String cd;

	@Column(name="created_by")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="is_active_ind")
	private String isActiveInd;

	@Column(name="long_desc")
	private String longDesc;

	private String nm;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	//bi-directional many-to-one association to AttributeMaster
	@OneToMany(mappedBy="uomMaster")
	private Set<AttributeMaster> attributeMasters;

	//bi-directional many-to-one association to AttributeValue
	@OneToMany(mappedBy="uomMaster")
	private Set<AttributeValue> attributeValues;

	//bi-directional many-to-one association to SlaComponentMaster
	@OneToMany(mappedBy="uomMaster")
	private Set<SlaComponentMaster> slaComponentMasters;

    public UomMaster() {
    	// TO DO

    }

	public String getCd() {
		return this.cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
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

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getNm() {
		return this.nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
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

	public Set<AttributeMaster> getAttributeMasters() {
		return this.attributeMasters;
	}

	public void setAttributeMasters(Set<AttributeMaster> attributeMasters) {
		this.attributeMasters = attributeMasters;
	}
	
	public Set<AttributeValue> getAttributeValues() {
		return this.attributeValues;
	}

	public void setAttributeValues(Set<AttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	public Set<SlaComponentMaster> getSlaComponentMasters() {
		return this.slaComponentMasters;
	}

	public void setSlaComponentMasters(Set<SlaComponentMaster> slaComponentMasters) {
		this.slaComponentMasters = slaComponentMasters;
	}
	
}