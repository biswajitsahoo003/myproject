package com.tcl.dias.serviceinventory.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author vpachava
 *
 */
@Entity
@Table(name="vw_sdwan_service_clsfn_port_dtls")
public class ViewSdwanServiceSiteDetails {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="site_type_name")
	private String siteTypeName;
	
	@Column(name="no_cpe_ports")
	private Integer noOfCpePorts;
	
	@Column(name="no_L2_ports")
	private Integer noOfL2Ports;
	
	@Column(name="no_L3_ports")
	private Integer noOfL3Ports;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiteTypeName() {
		return siteTypeName;
	}

	public void setSiteTypeName(String siteTypeName) {
		this.siteTypeName = siteTypeName;
	}

	public Integer getNoOfCpePorts() {
		return noOfCpePorts;
	}

	public void setNoOfCpePorts(Integer noOfCpePorts) {
		this.noOfCpePorts = noOfCpePorts;
	}

	public Integer getNoOfL2Ports() {
		return noOfL2Ports;
	}

	public void setNoOfL2Ports(Integer noOfL2Ports) {
		this.noOfL2Ports = noOfL2Ports;
	}

	public Integer getNoOfL3Ports() {
		return noOfL3Ports;
	}

	public void setNoOfL3Ports(Integer noOfL3Ports) {
		this.noOfL3Ports = noOfL3Ports;
	}
	
	
	
	

}
