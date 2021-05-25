package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * EthernetInterface Bean - To push Ethernet Interface attributes to Service Inventory
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class EthernetInterfaceBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String innerVlan;
	private String interfaceName;
	private String physicalPort;
	private String outerVlan;
	private String modifiedIpv4Address;
	private String modifiedSecondaryIpv4Address;
	private String modifiedIpv6Address;
	private String modifiedSecondaryIpv6Address;
	private String ceModifiedIpv4Address;
	private String ceModifiedSecondaryIpv4Address;
	private String ceModifiedIpv6Address;
	private String ceModifiedSecondaryIpv6Address;

	public String getInnerVlan() {
		return innerVlan;
	}

	public void setInnerVlan(String innerVlan) {
		this.innerVlan = innerVlan;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getPhysicalPort() {
		return physicalPort;
	}

	public void setPhysicalPort(String physicalPort) {
		this.physicalPort = physicalPort;
	}

	public String getOuterVlan() {
		return outerVlan;
	}

	public void setOuterVlan(String outerVlan) {
		this.outerVlan = outerVlan;
	}

	public String getModifiedIpv4Address() {
		return modifiedIpv4Address;
	}

	public void setModifiedIpv4Address(String modifiedIpv4Address) {
		this.modifiedIpv4Address = modifiedIpv4Address;
	}

	public String getModifiedIpv6Address() {
		return modifiedIpv6Address;
	}

	public void setModifiedIpv6Address(String modifiedIpv6Address) {
		this.modifiedIpv6Address = modifiedIpv6Address;
	}

	public String getModifiedSecondaryIpv4Address() {
		return modifiedSecondaryIpv4Address;
	}

	public void setModifiedSecondaryIpv4Address(String modifiedSecondaryIpv4Address) {
		this.modifiedSecondaryIpv4Address = modifiedSecondaryIpv4Address;
	}

	public String getModifiedSecondaryIpv6Address() {
		return modifiedSecondaryIpv6Address;
	}

	public void setModifiedSecondaryIpv6Address(String modifiedSecondaryIpv6Address) {
		this.modifiedSecondaryIpv6Address = modifiedSecondaryIpv6Address;
	}

	public String getCeModifiedIpv4Address() {
		return ceModifiedIpv4Address;
	}

	public void setCeModifiedIpv4Address(String ceModifiedIpv4Address) {
		this.ceModifiedIpv4Address = ceModifiedIpv4Address;
	}

	public String getCeModifiedSecondaryIpv4Address() {
		return ceModifiedSecondaryIpv4Address;
	}

	public void setCeModifiedSecondaryIpv4Address(String ceModifiedSecondaryIpv4Address) {
		this.ceModifiedSecondaryIpv4Address = ceModifiedSecondaryIpv4Address;
	}

	public String getCeModifiedIpv6Address() {
		return ceModifiedIpv6Address;
	}

	public void setCeModifiedIpv6Address(String ceModifiedIpv6Address) {
		this.ceModifiedIpv6Address = ceModifiedIpv6Address;
	}

	public String getCeModifiedSecondaryIpv6Address() {
		return ceModifiedSecondaryIpv6Address;
	}

	public void setCeModifiedSecondaryIpv6Address(String ceModifiedSecondaryIpv6Address) {
		this.ceModifiedSecondaryIpv6Address = ceModifiedSecondaryIpv6Address;
	}
}