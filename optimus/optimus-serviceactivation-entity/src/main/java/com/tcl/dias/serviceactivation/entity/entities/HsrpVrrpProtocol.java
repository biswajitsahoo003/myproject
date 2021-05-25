package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * HsrpVrrpProtocol Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="hsrp_vrrp_protocol")
@NamedQuery(name="HsrpVrrpProtocol.findAll", query="SELECT h FROM HsrpVrrpProtocol h")
public class HsrpVrrpProtocol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="hsrp_vrrp_id")
	private Integer hsrpVrrpId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="hello_value")
	private String helloValue;

	@Column(name="hold_value")
	private String holdValue;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	private String priority;

	private String role;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="timer_unit")
	private String timerUnit;

	@Column(name="virtual_ipv4_address")
	private String virtualIpv4Address;

	@Column(name="virtual_ipv6_address")
	private String virtualIpv6Address;

	//bi-directional many-to-one association to EthernetInterface
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ethernet_interface_ethernet_interface_id")
	private EthernetInterface ethernetInterface;

	public HsrpVrrpProtocol() {
	}

	public Integer getHsrpVrrpId() {
		return this.hsrpVrrpId;
	}

	public void setHsrpVrrpId(Integer hsrpVrrpId) {
		this.hsrpVrrpId = hsrpVrrpId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getHelloValue() {
		return this.helloValue;
	}

	public void setHelloValue(String helloValue) {
		this.helloValue = helloValue;
	}

	public String getHoldValue() {
		return this.holdValue;
	}

	public void setHoldValue(String holdValue) {
		this.holdValue = holdValue;
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

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getTimerUnit() {
		return this.timerUnit;
	}

	public void setTimerUnit(String timerUnit) {
		this.timerUnit = timerUnit;
	}

	public String getVirtualIpv4Address() {
		return this.virtualIpv4Address;
	}

	public void setVirtualIpv4Address(String virtualIpv4Address) {
		this.virtualIpv4Address = virtualIpv4Address;
	}

	public String getVirtualIpv6Address() {
		return this.virtualIpv6Address;
	}

	public void setVirtualIpv6Address(String virtualIpv6Address) {
		this.virtualIpv6Address = virtualIpv6Address;
	}

	public EthernetInterface getEthernetInterface() {
		return this.ethernetInterface;
	}

	public void setEthernetInterface(EthernetInterface ethernetInterface) {
		this.ethernetInterface = ethernetInterface;
	}

}