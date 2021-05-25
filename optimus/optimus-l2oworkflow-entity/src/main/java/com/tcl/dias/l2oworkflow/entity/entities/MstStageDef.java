package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * This file contains the MstStageDef.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_stage_def")
@NamedQuery(name = "MstStageDef.findAll", query = "SELECT m FROM MstStageDef m")
public class MstStageDef implements Serializable {
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
	@OneToMany(mappedBy = "mstStageDef")
	private Set<MstProcessDef> mstProcessDefs;

	public MstStageDef() {
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

	public Set<MstProcessDef> getMstProcessDefs() {
		return this.mstProcessDefs;
	}

	public void setMstProcessDefs(Set<MstProcessDef> mstProcessDefs) {
		this.mstProcessDefs = mstProcessDefs;
	}

	public MstProcessDef addMstProcessDef(MstProcessDef mstProcessDef) {
		getMstProcessDefs().add(mstProcessDef);
		mstProcessDef.setMstStageDef(this);

		return mstProcessDef;
	}

	public MstProcessDef removeMstProcessDef(MstProcessDef mstProcessDef) {
		getMstProcessDefs().remove(mstProcessDef);
		mstProcessDef.setMstStageDef(null);

		return mstProcessDef;
	}	
}