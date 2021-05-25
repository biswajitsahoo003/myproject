package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * 
 * PolicyTypeCriteriaMapping Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class PolicyTypeCriteriaMappingBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer policyTypeCriteriaId;
	private Timestamp endDate;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Integer policyCriteriaId;
	private Timestamp startDate;
	private Integer version;
	private boolean isEdited;
	public Integer getPolicyTypeCriteriaId() {
		return policyTypeCriteriaId;
	}
	public void setPolicyTypeCriteriaId(Integer policyTypeCriteriaId) {
		this.policyTypeCriteriaId = policyTypeCriteriaId;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Integer getPolicyCriteriaId() {
		return policyCriteriaId;
	}
	public void setPolicyCriteriaId(Integer policyCriteriaId) {
		this.policyCriteriaId = policyCriteriaId;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}