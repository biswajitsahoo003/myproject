package com.tcl.dias.common.fulfillment.beans;

import java.util.Date;

/**
 * 
 * This is the bean class to bind all the component attribute details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OdrComponentAttributeBean {
	
	private Integer id;
	private String name;
	private String value;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date updatedDate;
	private String isAdditionalParam;
	private OdrAdditionalServiceParamBean odrAdditionalServiceParam;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public String getIsAdditionalParam() {
		return isAdditionalParam;
	}
	public void setIsAdditionalParam(String isAdditionalParam) {
		this.isAdditionalParam = isAdditionalParam;
	}
	public OdrAdditionalServiceParamBean getOdrAdditionalServiceParam() {
		return odrAdditionalServiceParam;
	}
	public void setOdrAdditionalServiceParam(OdrAdditionalServiceParamBean odrAdditionalServiceParam) {
		this.odrAdditionalServiceParam = odrAdditionalServiceParam;
	}

}
