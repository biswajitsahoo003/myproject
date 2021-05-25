package com.tcl.dias.l2oworkflow.entity.entities;

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
 * This file contains the MstProcessDef.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_process_def")
@NamedQuery(name = "MstProcessDef.findAll", query = "SELECT m FROM MstProcessDef m")
public class MstProcessDef implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String key;

	private String code;

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

	//bi-directional many-to-one association to MstActivityDef
	@OneToMany(mappedBy = "mstProcessDef")
	private Set<MstActivityDef> mstActivityDefs;

	//bi-directional many-to-one association to MstStageDef
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "stage_def_key")
	private MstStageDef mstStageDef;
	
	public MstProcessDef() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Set<MstActivityDef> getMstActivityDefs() {
		return this.mstActivityDefs;
	}

	public void setMstActivityDefs(Set<MstActivityDef> mstActivityDefs) {
		this.mstActivityDefs = mstActivityDefs;
	}

	public MstActivityDef addMstActivityDef(MstActivityDef mstActivityDef) {
		getMstActivityDefs().add(mstActivityDef);
		mstActivityDef.setMstProcessDef(this);

		return mstActivityDef;
	}

	public MstActivityDef removeMstActivityDef(MstActivityDef mstActivityDef) {
		getMstActivityDefs().remove(mstActivityDef);
		mstActivityDef.setMstProcessDef(null);

		return mstActivityDef;
	}

	public MstStageDef getMstStageDef() {
		return this.mstStageDef;
	}

	public void setMstStageDef(MstStageDef mstStageDef) {
		this.mstStageDef = mstStageDef;
	}}