package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * This file contains the OdrComponent.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="odr_component")
@NamedQuery(name="OdrComponent.findAll", query="SELECT o FROM OdrComponent o")
public class OdrComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="component_name")
	private String componentName;

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

	//bi-directional many-to-one association to MstComponentType
	@ManyToOne
	@JoinColumn(name="component_type_id")
	private MstComponentType mstComponentType;

	//bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name="odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail;

	//bi-directional many-to-one association to OdrComponentAttribute
	@OneToMany(mappedBy="odrComponent",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<OdrComponentAttribute> odrComponentAttributes;
	
	@OneToMany(mappedBy="odrComponent",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<OdrServiceCommercial> odrServiceCommercials;
	
	@Column(name ="site_type")
	private String siteType;

	public OdrComponent() {
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

	public MstComponentType getMstComponentType() {
		return this.mstComponentType;
	}

	public void setMstComponentType(MstComponentType mstComponentType) {
		this.mstComponentType = mstComponentType;
	}

	public OdrServiceDetail getOdrServiceDetail() {
		return this.odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
	}

	public Set<OdrComponentAttribute> getOdrComponentAttributes() {
		return this.odrComponentAttributes;
	}

	public void setOdrComponentAttributes(Set<OdrComponentAttribute> odrComponentAttributes) {
		this.odrComponentAttributes = odrComponentAttributes;
	}

	public OdrComponentAttribute addOdrComponentAttribute(OdrComponentAttribute odrComponentAttribute) {
		getOdrComponentAttributes().add(odrComponentAttribute);
		odrComponentAttribute.setOdrComponent(this);

		return odrComponentAttribute;
	}

	public OdrComponentAttribute removeOdrComponentAttribute(OdrComponentAttribute odrComponentAttribute) {
		getOdrComponentAttributes().remove(odrComponentAttribute);
		odrComponentAttribute.setOdrComponent(null);

		return odrComponentAttribute;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	@Override
	public String toString() {
		return "OdrComponent [id=" + id + ", componentName=" + componentName + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", isActive=" + isActive + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", uuid=" + uuid + ", mstComponentType=" + mstComponentType
				+ ", odrServiceDetail=" + odrServiceDetail + ", odrComponentAttributes=" + odrComponentAttributes
				+ ", siteType=" + siteType + "]";
	}

	public Set<OdrServiceCommercial> getOdrServiceCommercials() {
		return odrServiceCommercials;
	}

	public void setOdrServiceCommercials(Set<OdrServiceCommercial> odrServiceCommercials) {
		this.odrServiceCommercials = odrServiceCommercials;
	}
	
	
	
}