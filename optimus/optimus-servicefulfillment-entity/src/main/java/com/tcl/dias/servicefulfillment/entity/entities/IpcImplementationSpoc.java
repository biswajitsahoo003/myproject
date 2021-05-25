package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ipc_implementation_spoc")
@NamedQuery(name = "IpcImplementationSpoc.findAll" , query = "SELECT ipc FROM IpcImplementationSpoc ipc")
public class IpcImplementationSpoc implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "team")
	private String team;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "mobile_phone")
	private String mobilePhone;
	
	@Column(name = "focus_skill")
	private String focusSkill;
	
	@Column(name = "associated_skill")
	private String associatedSkill;

	public IpcImplementationSpoc() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getFocusSkill() {
		return focusSkill;
	}

	public void setFocusSkill(String focusSkill) {
		this.focusSkill = focusSkill;
	}

	public String getAssociatedSkill() {
		return associatedSkill;
	}

	public void setAssociatedSkill(String associatedSkill) {
		this.associatedSkill = associatedSkill;
	}

	@Override
	public String toString() {
		return "IpcImplementationSpoc [id=" + id + ", name=" + name + ", team=" + team + ", emailId=" + emailId
				+ ", mobilePhone=" + mobilePhone + ", focusSkill=" + focusSkill + ", associatedSkill=" + associatedSkill
				+ "]";
	}
	
}
