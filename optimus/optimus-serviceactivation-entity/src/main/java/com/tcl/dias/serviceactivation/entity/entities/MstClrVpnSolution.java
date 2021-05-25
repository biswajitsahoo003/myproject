package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * VpnSolution Entity Class
 * 
 *
 * @author Dimple
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "mst_clr_vpn_solution")
@NamedQuery(name = "MstClrVpnSolution.findAll", query = "SELECT v FROM MstClrVpnSolution v")
public class MstClrVpnSolution implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "solution_id")
	private String solutionId;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "vpn_topology")
	private String vpnTopology;

	@Column(name = "site_topology")
	private String siteTopology;
	
	@Column(name = "updated_by")
	private String updatedBy;


	public MstClrVpnSolution() {
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getServiceCode() {
		return serviceCode;
	}


	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}


	public String getSolutionId() {
		return solutionId;
	}


	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getVpnTopology() {
		return vpnTopology;
	}


	public void setVpnTopology(String vpnTopology) {
		this.vpnTopology = vpnTopology;
	}


	public String getSiteTopology() {
		return siteTopology;
	}


	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}