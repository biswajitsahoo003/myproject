/**
 * 
 */
package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.io.Serializable;

/**
 * @author ASyed
 *
 */
public class PlanItemRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 330225977982696645L;

	Integer serviceId;

	String caseInstanceId;

	String planItem;
	
	String planItemDefinitionId;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getCaseInstanceId() {
		return caseInstanceId;
	}

	public void setCaseInstanceId(String caseInstanceId) {
		this.caseInstanceId = caseInstanceId;
	}

	public String getPlanItem() {
		return planItem;
	}

	public void setPlanItem(String planItem) {
		this.planItem = planItem;
	}

	
	public String getPlanItemDefinitionId() {
		return planItemDefinitionId;
	}

	public void setPlanItemDefinitionId(String planItemDefinitionId) {
		this.planItemDefinitionId = planItemDefinitionId;
	}

	@Override
	public String toString() {
		return "PlanItemRequestBean [serviceId=" + serviceId + ", caseInstanceId=" + caseInstanceId + ", planItem="
				+ planItem + ", planItemDefinitionId=" + planItemDefinitionId + "]";
	}

}
