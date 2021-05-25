package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the MstTaskDef.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_task_def")
@NamedQuery(name = "MstTaskDef.findAll", query = "SELECT m FROM MstTaskDef m")
public class MstTaskDef implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String key;

	@Column(name = "assigned_group")
	private String assignedGroup;

	@Column(name = "button_label")
	private String buttonLabel;

	@Column(name = "form_key")
	private String formKey;

	@Column(name = "is_customer_task")
	private String isCustomerTask;

	@Column(name = "is_manual_task")
	private String isManualTask;
	
	@Column(name="is_dependent_task")
	private String isDependentTask;

	private String name;

	@Column(name = "owner_group")
	private String ownerGroup;

	@Column(name = "reminder_cycle")
	private String reminderCycle;

	@Column(name = "description")
	private String description;

	private Integer tat;
		
	@Column(name="title")
	private String title;

	@Column(name = "wait_time")
	private String waitTime;
	
	@Column(name = "dynamic_assignment")
	private String dynamicAssignment;
	
	@Column(name = "dependent_task_key")
	private String dependentTaskKey;
	
	@Column(name="admin_view")
	private String adminView;
	
//	@Column(name="fe_engineer")
//	private String feEngineer;
//
//	@Column(name="fe_type")
//	private String feType;

	//bi-directional many-to-one association to MstActivityDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "activity_def_key")
	private MstActivityDef mstActivityDef;


	public MstTaskDef() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAssignedGroup() {
		return this.assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup) {
		this.assignedGroup = assignedGroup;
	}

	public String getButtonLabel() {
		return this.buttonLabel;
	}

	public void setButtonLabel(String buttonLabel) {
		this.buttonLabel = buttonLabel;
	}

	public String getFormKey() {
		return this.formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getIsCustomerTask() {
		return this.isCustomerTask;
	}

	public void setIsCustomerTask(String isCustomerTask) {
		this.isCustomerTask = isCustomerTask;
	}

	public String getIsManualTask() {
		return this.isManualTask;
	}

	public void setIsManualTask(String isManualTask) {
		this.isManualTask = isManualTask;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerGroup() {
		return this.ownerGroup;
	}

	public void setOwnerGroup(String ownerGroup) {
		this.ownerGroup = ownerGroup;
	}

	public String getReminderCycle() {
		return this.reminderCycle;
	}

	public void setReminderCycle(String reminderCycle) {
		this.reminderCycle = reminderCycle;
	}


	public Integer getTat() {
		return this.tat;
	}

	public void setTat(Integer tat) {
		this.tat = tat;
	}

	public String getWaitTime() {
		return this.waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public MstActivityDef getMstActivityDef() {
		return this.mstActivityDef;
	}

	public void setMstActivityDef(MstActivityDef mstActivityDef) {
		this.mstActivityDef = mstActivityDef;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsDependentTask() {
		return isDependentTask;
	}

	public void setIsDependentTask(String isDependentTask) {
		this.isDependentTask = isDependentTask;
	}
	
	public String getDynamicAssignment() {
		return dynamicAssignment;
	}

	public void setDynamicAssignment(String dynamicAssignment) {
		this.dynamicAssignment = dynamicAssignment;
	}

	public String getDependentTaskKey() {
		return dependentTaskKey;
	}

	public void setDependentTaskKey(String dependentTaskKey) {
		this.dependentTaskKey = dependentTaskKey;
	}

	public String getAdminView() {
		return adminView;
	}

	public void setAdminView(String adminView) {
		this.adminView = adminView;
	}

//	public String getFeEngineer() {
//		return feEngineer;
//	}
//
//	public void setFeEngineer(String feEngineer) {
//		this.feEngineer = feEngineer;
//	}
//
//	public String getFeType() {
//		return feType;
//	}
//
//	public void setFeType(String feType) {
//		this.feType = feType;
//	}		
//	

}