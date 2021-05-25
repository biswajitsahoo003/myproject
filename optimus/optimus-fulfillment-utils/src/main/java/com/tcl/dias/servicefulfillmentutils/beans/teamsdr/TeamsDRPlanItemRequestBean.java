package com.tcl.dias.servicefulfillmentutils.beans.teamsdr;

import java.io.Serializable;

/**
 * @author Syed Ali.
 * @createdAt 01/03/2021, Monday, 18:19
 */
public class TeamsDRPlanItemRequestBean implements Serializable {
	private static final long serialVersionUID = 330225977982696645L;

	Integer serviceId;

	Integer componentId;

	String caseInstanceId;

	String planItem;

	String planItemDefinitionId;

	private Integer flowGroupId;

	public TeamsDRPlanItemRequestBean() {
	}

	public Integer getFlowGroupId() {
		return flowGroupId;
	}

	public void setFlowGroupId(Integer flowGroupId) {
		this.flowGroupId = flowGroupId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
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
		return "TeamsDRPlanItemRequestBean{" + "serviceId=" + serviceId + ", caseInstanceId='" + caseInstanceId + '\'' + ", planItem='" + planItem + '\'' + ", planItemDefinitionId='" + planItemDefinitionId + '\'' + '}';
	}
}
