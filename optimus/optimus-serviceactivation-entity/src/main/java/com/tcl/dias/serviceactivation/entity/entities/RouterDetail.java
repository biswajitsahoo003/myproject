package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * RouterDetail Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="router_details")
@NamedQuery(name="RouterDetail.findAll", query="SELECT r FROM RouterDetail r")
public class RouterDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="router_id")
	private Integer routerId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="ipv4_mgmt_address")
	private String ipv4MgmtAddress;

	@Column(name="ipv6_mgmt_address")
	private String ipv6MgmtAddress;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="router_hostname")
	private String routerHostname;

	@Column(name="router_make")
	private String routerMake;

	@Column(name="router_model")
	private String routerModel;

	@Column(name="router_role")
	private String routerRole;

	@Column(name="router_type")
	private String routerType;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy="routerDetail")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	public RouterDetail() {
	}

	public Integer getRouterId() {
		return this.routerId;
	}

	public void setRouterId(Integer routerId) {
		this.routerId = routerId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getIpv4MgmtAddress() {
		return this.ipv4MgmtAddress;
	}

	public void setIpv4MgmtAddress(String ipv4MgmtAddress) {
		this.ipv4MgmtAddress = ipv4MgmtAddress;
	}

	public String getIpv6MgmtAddress() {
		return this.ipv6MgmtAddress;
	}

	public void setIpv6MgmtAddress(String ipv6MgmtAddress) {
		this.ipv6MgmtAddress = ipv6MgmtAddress;
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

	public String getRouterHostname() {
		return this.routerHostname;
	}

	public void setRouterHostname(String routerHostname) {
		this.routerHostname = routerHostname;
	}

	public String getRouterMake() {
		return this.routerMake;
	}

	public void setRouterMake(String routerMake) {
		this.routerMake = routerMake;
	}

	public String getRouterModel() {
		return this.routerModel;
	}

	public void setRouterModel(String routerModel) {
		this.routerModel = routerModel;
	}

	public String getRouterRole() {
		return this.routerRole;
	}

	public void setRouterRole(String routerRole) {
		this.routerRole = routerRole;
	}

	public String getRouterType() {
		return this.routerType;
	}

	public void setRouterType(String routerType) {
		this.routerType = routerType;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setRouterDetail(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setRouterDetail(null);

		return interfaceProtocolMapping;
	}

}