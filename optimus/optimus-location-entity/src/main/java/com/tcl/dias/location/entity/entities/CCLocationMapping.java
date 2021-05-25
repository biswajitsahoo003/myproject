package com.tcl.dias.location.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cc_loc_mapping")
@NamedQuery(name = "CCLocationMapping.findAll", query = "SELECT c FROM CCLocationMapping c")
public class CCLocationMapping implements Serializable {

	private static final long serialVersionUID = -1248206168133852304L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "a_end_loc_id")
	private Integer aEndLocId;
	
	@Column(name = "z_end_loc_id")
	private Integer zEndLocId;

	@Column(name = "a_end_demarcation_id")
	private Integer aEndDemarcationId;

	@Column(name = "z_end_demarcation_id")
	private Integer zEndDemarcationId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getaEndLocId() {
		return aEndLocId;
	}

	public void setaEndLocId(Integer aEndLocId) {
		this.aEndLocId = aEndLocId;
	}

	public Integer getzEndLocId() {
		return zEndLocId;
	}

	public void setzEndLocId(Integer zEndLocId) {
		this.zEndLocId = zEndLocId;
	}

	public Integer getaEndDemarcationId() {
		return aEndDemarcationId;
	}

	public void setaEndDemarcationId(Integer aEndDemarcationId) {
		this.aEndDemarcationId = aEndDemarcationId;
	}

	public Integer getzEndDemarcationId() {
		return zEndDemarcationId;
	}

	public void setzEndDemarcationId(Integer zEndDemarcationId) {
		this.zEndDemarcationId = zEndDemarcationId;
	}
}
