package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * 
 * This file contains the SapInterfaceMapping.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="sap_interface_mapping")
@NamedQuery(name="SapInterfaceMapping.findAll", query="SELECT s FROM SapInterfaceMapping s")
public class SapInterfaceMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="optimus_interface")
	private String optimusInterface;

	@Column(name="sap_interface")
	private String sapInterface;

	private byte status;
	
	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	private Date updatedTime;

	public SapInterfaceMapping() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOptimusInterface() {
		return this.optimusInterface;
	}

	public void setOptimusInterface(String optimusInterface) {
		this.optimusInterface = optimusInterface;
	}

	public String getSapInterface() {
		return this.sapInterface;
	}

	public void setSapInterface(String sapInterface) {
		this.sapInterface = sapInterface;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	

}