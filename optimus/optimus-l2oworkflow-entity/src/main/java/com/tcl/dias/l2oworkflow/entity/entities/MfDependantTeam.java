package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mf_dependant_team")
@NamedQuery(name = "MfDependantTeam.findAll", query = "SELECT m FROM MfDependantTeam m")
public class MfDependantTeam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "team_name")
	private String teamName;
	
	@Column(name = "team_region")
	private String teamRegion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamRegion() {
		return teamRegion;
	}

	public void setTeamRegion(String teamRegion) {
		this.teamRegion = teamRegion;
	}
	
	
	
}
