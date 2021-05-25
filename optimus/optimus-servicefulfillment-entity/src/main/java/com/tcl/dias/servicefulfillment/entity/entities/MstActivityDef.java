package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * This file contains the MstActivityDef.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_activity_def")
@NamedQuery(name = "MstActivityDef.findAll", query = "SELECT m FROM MstActivityDef m")
public class MstActivityDef implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String key;

	@Column(name = "customer_view")
	private String customerView;

	private String name;

	private Byte sequence;
	
	@Column(name="admin_view")
	private String adminView;
	
	


	public String getAdminView() {
		return adminView;
	}

	public void setAdminView(String adminView) {
		this.adminView = adminView;
	}

	//bi-directional many-to-one association to MstProcessDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "process_def_key")
	private MstProcessDef mstProcessDef;

	
	//bi-directional many-to-one association to MstTaskDef
	@OneToMany(mappedBy = "mstActivityDef")
	private Set<MstTaskDef> mstTaskDefs;

	public MstActivityDef() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCustomerView() {
		return this.customerView;
	}

	public void setCustomerView(String customerView) {
		this.customerView = customerView;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getSequence() {
		return this.sequence;
	}

	public void setSequence(Byte sequence) {
		this.sequence = sequence;
	}

	public MstProcessDef getMstProcessDef() {
		return this.mstProcessDef;
	}

	public void setMstProcessDef(MstProcessDef mstProcessDef) {
		this.mstProcessDef = mstProcessDef;
	}


	public Set<MstTaskDef> getMstTaskDefs() {
		return this.mstTaskDefs;
	}

	public void setMstTaskDefs(Set<MstTaskDef> mstTaskDefs) {
		this.mstTaskDefs = mstTaskDefs;
	}

	public MstTaskDef addMstTaskDef(MstTaskDef mstTaskDef) {
		getMstTaskDefs().add(mstTaskDef);
		mstTaskDef.setMstActivityDef(this);

		return mstTaskDef;
	}

	public MstTaskDef removeMstTaskDef(MstTaskDef mstTaskDef) {
		getMstTaskDefs().remove(mstTaskDef);
		mstTaskDef.setMstActivityDef(null);

		return mstTaskDef;
	}

}