package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * RouterUplinkport Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="router_uplinkport")
@NamedQuery(name="RouterUplinkport.findAll", query="SELECT r FROM RouterUplinkport r")
public class RouterUplinkport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="router_uplinkport_id")
	private Integer routerUplinkportId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="physical_port1_name")
	private String physicalPort1Name;

	@Column(name="physical_port2_name")
	private String physicalPort2Name;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to Topology
	@ManyToOne(fetch=FetchType.LAZY)
	private Topology topology;

	public RouterUplinkport() {
	}

	public Integer getRouterUplinkportId() {
		return this.routerUplinkportId;
	}

	public void setRouterUplinkportId(Integer routerUplinkportId) {
		this.routerUplinkportId = routerUplinkportId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPhysicalPort1Name() {
		return this.physicalPort1Name;
	}

	public void setPhysicalPort1Name(String physicalPort1Name) {
		this.physicalPort1Name = physicalPort1Name;
	}

	public String getPhysicalPort2Name() {
		return this.physicalPort2Name;
	}

	public void setPhysicalPort2Name(String physicalPort2Name) {
		this.physicalPort2Name = physicalPort2Name;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Topology getTopology() {
		return this.topology;
	}

	public void setTopology(Topology topology) {
		this.topology = topology;
	}

}