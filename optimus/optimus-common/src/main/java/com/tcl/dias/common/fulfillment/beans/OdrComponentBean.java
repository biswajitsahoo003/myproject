package com.tcl.dias.common.fulfillment.beans;

import java.util.Date;
import java.util.Set;
/**
 * 
 * This bean is to bind all the component details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OdrComponentBean {
	
	private Integer id;
	private String componentName;
	private String siteType;
	private Set<OdrComponentAttributeBean> odrComponentAttributeBeans;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public Set<OdrComponentAttributeBean> getOdrComponentAttributeBeans() {
		return odrComponentAttributeBeans;
	}
	public void setOdrComponentAttributeBeans(Set<OdrComponentAttributeBean> odrComponentAttributeBeans) {
		this.odrComponentAttributeBeans = odrComponentAttributeBeans;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
