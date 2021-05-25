package com.tcl.dias.preparefulfillment.beans;

import java.util.List;

public class CaseDetails {

	String caseId;
	String caseName;
	List<PlanItemDetail> planItemDetails;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public List<PlanItemDetail> getPlanItemDetails() {
		return planItemDetails;
	}
	public void setPlanItemDetails(List<PlanItemDetail> planItemDetails) {
		this.planItemDetails = planItemDetails;
	}
	
	
}
