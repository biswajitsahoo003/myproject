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
 * The persistent class for the mst_vpn_alias database table.
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name="mst_vpn_alias")
@NamedQuery(name="MstVpnAlias.findAll", query="SELECT m FROM MstVpnAlias m")
public class MstVpnAlias implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="alias_name", length=45)
	private String aliasName;

	@Column(name="created_by", length=45)
	private String createdBy;

	@Column(name="created_date", length=45)
	private String createdDate;

	@Column(name="vpn_name", length=45)
	private String vpnName;

	public MstVpnAlias() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAliasName() {
		return this.aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getVpnName() {
		return this.vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

}