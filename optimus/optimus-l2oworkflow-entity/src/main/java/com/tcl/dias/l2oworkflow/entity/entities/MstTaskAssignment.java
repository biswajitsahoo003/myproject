package com.tcl.dias.l2oworkflow.entity.entities;

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



@Entity
@Table(name = "mst_task_assignment")
@NamedQuery(name = "MstTaskAssignment.findAll", query = "SELECT m FROM MstTaskAssignment m")
public class MstTaskAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Column(name = "assigned_user")
	private String assignedUser;
	
	@Column(name = "next_assigned_user")
	private String nextAssignedUser;

	@Column(name = "backup_user1")
	private String backupUser1;
	
	@Column(name = "backup_user2")
	private String backupUser2;


	//bi-directional many-to-one association to MstTaskDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "task_def_key")
	private MstTaskDef mstTaskDef;


	public MstTaskAssignment() {
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAssignedUser() {
		return assignedUser;
	}


	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}


	public String getBackupUser1() {
		return backupUser1;
	}

	public void setBackupUser1(String backupUser1) {
		this.backupUser1 = backupUser1;
	}
	
	public String getBackupUser2() {
		return backupUser2;
	}

	public void setBackupUser2(String backupUser2) {
		this.backupUser2 = backupUser2;
	}


	public String getNextAssignedUser() {
		return nextAssignedUser;
	}


	public void setNextAssignedUser(String nextAssignedUser) {
		this.nextAssignedUser = nextAssignedUser;
	}



	public MstTaskDef getMstTaskDef() {
		return mstTaskDef;
	}


	public void setMstTaskDef(MstTaskDef mstTaskDef) {
		this.mstTaskDef = mstTaskDef;
	}


}