package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This file contains the MstTaskRegion.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="mst_task_region")
@NamedQuery(name="MstTaskRegion.findAll", query="SELECT m FROM MstTaskRegion m")
public class MstTaskRegion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String email;

	private String group;

	private String region;

	public MstTaskRegion() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup() {
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}