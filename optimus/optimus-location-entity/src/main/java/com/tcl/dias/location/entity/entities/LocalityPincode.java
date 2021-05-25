package com.tcl.dias.location.entity.entities;

import java.io.Serializable;

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
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "locality_pincode")
@NamedQuery(name = "LocalityPincode.findAll", query = "SELECT l FROM LocalityPincode l")
public class LocalityPincode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer pincode;

	private String source;

	// bi-directional many-to-one association to MstLocality
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locality_id")
	private MstLocality mstLocality;

	public LocalityPincode() {
		// Do NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPincode() {
		return this.pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public MstLocality getMstLocality() {
		return this.mstLocality;
	}

	public void setMstLocality(MstLocality mstLocality) {
		this.mstLocality = mstLocality;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}