package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * This is the entity class for SDWAN Bandwidth Interface Mapping
 * 
 *
 * @author mpalanis
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_bw_intf_mapping")
public class VwSdwanBwIntfMapping implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="min_bw")
	private Integer minBw;
	
	@Column(name="min_bw_unit")
	private String minBwUnit;
	
	@Column(name="interface_cable_type")
	private String interfaceCableType;
	
	@Column(name="vendor")
	private String vendor;
	
	@Column(name="interface")
	private String interfaceType;

	public Integer getMinBw() {
		return minBw;
	}

	public void setMinBw(Integer minBw) {
		this.minBw = minBw;
	}

	public String getMinBwUnit() {
		return minBwUnit;
	}

	public void setMinBwUnit(String minBwUnit) {
		this.minBwUnit = minBwUnit;
	}

	public String getInterfaceCableType() {
		return interfaceCableType;
	}

	public void setInterfaceCableType(String interfaceCableType) {
		this.interfaceCableType = interfaceCableType;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
}
