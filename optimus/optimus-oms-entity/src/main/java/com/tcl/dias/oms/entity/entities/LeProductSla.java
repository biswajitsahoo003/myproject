package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * This file contains the LeProductSla.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "le_product_sla")
@NamedQuery(name = "LeProductSla.findAll", query = "SELECT l FROM LeProductSla l")
public class LeProductSla {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "erf_cus_customer_id")
	private Integer erfCustomerId;

	@Column(name = "erf_cus_customer_le_id")
	private Integer erfCustomerLeId;

	@Column(name = "sla_value")
	private String slaValue;

	@Column(name = "sla_tier")
	private String slaTier;

	@ManyToOne
	@JoinColumn(name = "mst_product_family_id")
	private MstProductFamily mstProductFamily;

	@ManyToOne
	@JoinColumn(name = "sla_master_id")
	private SlaMaster slaMaster;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the erfCustomerId
	 */
	public Integer getErfCustomerId() {
		return erfCustomerId;
	}

	/**
	 * @param erfCustomerId
	 *            the erfCustomerId to set
	 */
	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}

	/**
	 * @return the erfCustomerLeId
	 */
	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}

	/**
	 * @param erfCustomerLeId
	 *            the erfCustomerLeId to set
	 */
	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}



	/**
	 * @return the mstProductFamily
	 */
	public MstProductFamily getMstProductFamily() {
		return mstProductFamily;
	}

	/**
	 * @param mstProductFamily
	 *            the mstProductFamily to set
	 */
	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	/**
	 * @return the slaMaster
	 */
	public SlaMaster getSlaMaster() {
		return slaMaster;
	}

	/**
	 * @param slaMaster
	 *            the slaMaster to set
	 */
	public void setSlaMaster(SlaMaster slaMaster) {
		this.slaMaster = slaMaster;
	}

	/**
	 * @return the slaValue
	 */
	public String getSlaValue() {
		return slaValue;
	}

	/**
	 * @param slaValue the slaValue to set
	 */
	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	/**
	 * @return the slaTier
	 */
	public String getSlaTier() {
		return slaTier;
	}

	/**
	 * @param slaTier the slaTier to set
	 */
	public void setSlaTier(String slaTier) {
		this.slaTier = slaTier;
	}
	
	

}
