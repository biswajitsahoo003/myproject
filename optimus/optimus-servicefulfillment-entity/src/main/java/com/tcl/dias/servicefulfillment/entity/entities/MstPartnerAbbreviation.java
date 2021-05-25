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
@Table(name = "mst_partner_abbreviation")
@NamedQuery(name = "MstPartnerAbbreviation.findAll", query = "SELECT m FROM MstPartnerAbbreviation m")
public class MstPartnerAbbreviation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	@Column(name="repc_name")
	private String repcName;
	
	@Column(name="repc_abbreviation_name")
	private String repcAbbreviationName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRepcName() {
		return repcName;
	}

	public void setRepcName(String repcName) {
		this.repcName = repcName;
	}

	public String getRepcAbbreviationName() {
		return repcAbbreviationName;
	}

	public void setRepcAbbreviationName(String repcAbbreviationName) {
		this.repcAbbreviationName = repcAbbreviationName;
	}

}
