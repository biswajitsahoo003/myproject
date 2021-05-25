package com.tcl.dias.common.servicefulfillment.beans;

/**
 * This file contains the ScCommercialBean.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ScCommercialBean {
	private Integer id;
	private Integer serviceId;
	private String referenceName;
	private String referenceType;
	private String componentReferenceName;
	private Double mrc;
	private Double arc;
	private Double nrc;
	private String serviceType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
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

	public String getComponentReferenceName() {
		return componentReferenceName;
	}

	public void setComponentReferenceName(String componentReferenceName) {
		this.componentReferenceName = componentReferenceName;
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

	@Override
	public String toString() {
		return "ScCommercialBean [id=" + id + ", serviceId=" + serviceId + ", referenceName=" + referenceName
				+ ", referenceType=" + referenceType + ", componentReferenceName=" + componentReferenceName + ", mrc="
				+ mrc + ", arc=" + arc + ", nrc=" + nrc + ", serviceType=" + serviceType + "]";
	}

}
