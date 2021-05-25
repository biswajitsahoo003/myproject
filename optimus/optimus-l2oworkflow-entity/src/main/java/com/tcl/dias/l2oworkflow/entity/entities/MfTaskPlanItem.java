package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mf_task_plan_item")
@NamedQuery(name = "MfTaskPlanItem.findAll", query = "SELECT m FROM MfTaskPlanItem m")
public class MfTaskPlanItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "plan_item_inst_id")
    private String planItemInstId;
	
	@Column(name = "plan_item_def_id")
    private String planItemDefId;
	
	@Column(name = "case_inst_id")
    private String caseInstId;
	
	@Column(name = "status")
	private String status;

	public String getPlanItemInstId() {
		return planItemInstId;
	}

	public void setPlanItemInstId(String planItemInstId) {
		this.planItemInstId = planItemInstId;
	}

	public String getPlanItemDefId() {
		return planItemDefId;
	}

	public void setPlanItemDefId(String planItemDefId) {
		this.planItemDefId = planItemDefId;
	}

	public String getCaseInstId() {
		return caseInstId;
	}

	public void setCaseInstId(String caseInstId) {
		this.caseInstId = caseInstId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
