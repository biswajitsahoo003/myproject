package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the mst_gvpn_alu_cust_id database table.
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name="mst_gvpn_alu_cust_id")
@NamedQuery(name="MstGvpnAluCustId.findAll", query="SELECT m FROM MstGvpnAluCustId m")
public class MstGvpnAluCustId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="alu_cust_id", length=45)
	private String aluCustId;

	@Column(name="crn_id", length=45)
	private String crnId;

	@Column(name="customer_name", length=45)
	private String customerName;

	public MstGvpnAluCustId() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAluCustId() {
		return this.aluCustId;
	}

	public void setAluCustId(String aluCustId) {
		this.aluCustId = aluCustId;
	}

	public String getCrnId() {
		return this.crnId;
	}

	public void setCrnId(String crnId) {
		this.crnId = crnId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}