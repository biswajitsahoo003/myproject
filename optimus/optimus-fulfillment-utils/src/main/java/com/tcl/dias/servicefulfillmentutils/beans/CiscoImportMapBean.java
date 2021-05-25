package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * CiscoImportMap Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class CiscoImportMapBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ciscoImportId;
	private Integer aclPolicyCriteriaServiceCosCriteriaServiceCosId;
	private String description;
	private Timestamp endDate;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Timestamp startDate;
	private boolean isEdited;
	private List<PolicyTypeCriteriaMappingBean> policyTypeCriteriaMappingBeans;

	public List<PolicyTypeCriteriaMappingBean> getPolicyTypeCriteriaMappingBeans() {
		if(policyTypeCriteriaMappingBeans!=null){
			policyTypeCriteriaMappingBeans = new ArrayList<>();
		}
		return policyTypeCriteriaMappingBeans;
	}

	public void setPolicyTypeCriteriaMappingBeans(List<PolicyTypeCriteriaMappingBean> policyTypeCriteriaMappingBeans) {
		this.policyTypeCriteriaMappingBeans = policyTypeCriteriaMappingBeans;
	}

	public Integer getCiscoImportId() {
		return ciscoImportId;
	}
	public void setCiscoImportId(Integer ciscoImportId) {
		this.ciscoImportId = ciscoImportId;
	}
	public Integer getAclPolicyCriteriaServiceCosCriteriaServiceCosId() {
		return aclPolicyCriteriaServiceCosCriteriaServiceCosId;
	}
	public void setAclPolicyCriteriaServiceCosCriteriaServiceCosId(
			Integer aclPolicyCriteriaServiceCosCriteriaServiceCosId) {
		this.aclPolicyCriteriaServiceCosCriteriaServiceCosId = aclPolicyCriteriaServiceCosCriteriaServiceCosId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}
}