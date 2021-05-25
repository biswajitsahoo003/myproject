package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the OdrServiceCommercial.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "sc_service_commercial")
@NamedQuery(name = "ScServiceCommercial.findAll", query = "SELECT o FROM ScServiceCommercial o")
public class ScServiceCommercial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "sc_service_detail_id")
	private String serviceId;

	@Column(name = "sc_service_id")
	private Integer scServiceId;

	@Column(name = "reference_name")
	private String referenceName;

	@Column(name = "reference_type")
	private String referenceType;

	@Column(name = "component_reference_name")
	private String componentReferenceName;

	@Column(name = "mrc")
	private Double mrc;

	@Column(name = "arc")
	private Double arc;

	@Column(name = "nrc")
	private Double nrc;

	@Column(name = "service_type")
	private String serviceType;
	
	@Column(name = "component_id")
	private Integer componentId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getComponentReferenceName() {
		return componentReferenceName;
	}

	public void setComponentReferenceName(String componentReferenceName) {
		this.componentReferenceName = componentReferenceName;
	}

	public Integer getScServiceId() {
		return scServiceId;
	}

	public void setScServiceId(Integer scServiceId) {
		this.scServiceId = scServiceId;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

}