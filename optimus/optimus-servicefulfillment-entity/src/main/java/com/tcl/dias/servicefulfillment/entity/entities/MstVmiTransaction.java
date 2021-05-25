package com.tcl.dias.servicefulfillment.entity.entities;

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
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "mst_vmi_transaction")
@NamedQuery(name = "MstVmiTransaction.findAll", query = "SELECT m FROM MstVmiTransaction m")
public class MstVmiTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer quantity;

	@Column(name = "sub_po_number")
	private String subPoNumber;

	// bi-directional many-to-one association to MstVmi
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_vmi_id")
	private MstVmi mstVmi;

	public MstVmiTransaction() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSubPoNumber() {
		return subPoNumber;
	}

	public void setSubPoNumber(String subPoNumber) {
		this.subPoNumber = subPoNumber;
	}

	public MstVmi getMstVmi() {
		return this.mstVmi;
	}

	public void setMstVmi(MstVmi mstVmi) {
		this.mstVmi = mstVmi;
	}

}