package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author SanjeKum
 *
 */
@Entity
@Table(name = "vw_nde_cuid_ehs_dtls")
@NamedQuery(name = "SICustomerInfo.findAll", query = "SELECT q FROM SICustomerInfo q")
public class SICustomerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "cuid")
	private String cuId;

	@Column(name = "ehsid")
	private String ehsId;

	@Column(name = "ehs_address")
	private String ehsAddress;

	@Column(name = "ehs_loc_id")
	private String ehsLocId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCuId() {
		return cuId;
	}

	public void setCuId(String cuId) {
		this.cuId = cuId;
	}

	public String getEhsId() {
		return ehsId;
	}

	public void setEhsId(String ehsId) {
		this.ehsId = ehsId;
	}

	public String getEhsAddress() {
		return ehsAddress;
	}

	public void setEhsAddress(String ehsAddress) {
		this.ehsAddress = ehsAddress;
	}

	public String getEhsLocId() {
		return ehsLocId;
	}

	public void setEhsLocId(String ehsLocId) {
		this.ehsLocId = ehsLocId;
	}

}
