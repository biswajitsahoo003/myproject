package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * EthernetInterface Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "ethernet_interface")
@NamedQuery(name = "EthernetInterface.findAll", query = "SELECT e FROM EthernetInterface e")
public class EthernetInterface implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ethernet_interface_id")
	private Integer ethernetInterfaceId;

	@Column(name = "autonegotiation_enabled")
	private String autonegotiationEnabled;

	@Column(name = "bfd_multiplier")
	private String bfdMultiplier;

	@Column(name = "bfdreceive_interval")
	private String bfdreceiveInterval;

	@Column(name = "bfdtransmit_interval")
	private String bfdtransmitInterval;

	private String duplex;

	private String encapsulation;

	@Column(name = "end_date")
	private Timestamp endDate;

	private String framing;

	@Column(name = "holdtime_down")
	private String holdtimeDown;

	@Column(name = "holdtime_up")
	private String holdtimeUp;

	@Column(name = "inner_vlan")
	private String innerVlan;

	@Column(name = "interface_name")
	private String interfaceName;

	@Column(name = "ipv4_address")
	private String ipv4Address;

	@Column(name = "modified_ipv4_address")
	private String modifiedIpv4Address;

	@Column(name = "modified_ipv6_address")
	private String modifiedIpv6Address;

	@Column(name = "ipv6_address")
	private String ipv6Address;

	@Column(name = "secondary_ipv4_address")
	private String secondaryIpv4Address;

	@Column(name = "secondary_ipv6_address")
	private String secondaryIpv6Address;

	@Column(name = "modified_secondary_ipv4_address")
	private String modifiedSecondaryIpv4Address;

	@Column(name = "modified_secondary_ipv6_address")
	private String modifiedSecondaryIpv6Address;

	@Column(name = "isbfd_enabled")
	private Byte isbfdEnabled;

	@Column(name = "ishsrp_enabled")
	private Byte ishsrpEnabled;

	@Column(name = "isvrrp_enabled")
	private Byte isvrrpEnabled;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "media_type")
	private String mediaType;

	private String mode;

	@Column(name = "modified_by")
	private String modifiedBy;

	private String mtu;

	@Column(name = "outer_vlan")
	private String outerVlan;

	@Column(name = "physical_port")
	private String physicalPort;

	@Column(name = "port_type")
	private String portType;

	@Column(name = "qos_loopin_passthrough")
	private String qosLoopinPassthrough;

	private String speed;

	@Column(name = "start_date")
	private Timestamp startDate;

	// bi-directional many-to-one association to AclPolicyCriteria
	@OneToMany(mappedBy = "ethernetInterface")
	private Set<AclPolicyCriteria> aclPolicyCriterias;

	// bi-directional many-to-one association to HsrpVrrpProtocol
	@OneToMany(mappedBy = "ethernetInterface")
	private Set<HsrpVrrpProtocol> hsrpVrrpProtocols;

	// bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy = "ethernetInterface")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	public EthernetInterface() {
	}

	public Integer getEthernetInterfaceId() {
		return this.ethernetInterfaceId;
	}

	public void setEthernetInterfaceId(Integer ethernetInterfaceId) {
		this.ethernetInterfaceId = ethernetInterfaceId;
	}

	public String getAutonegotiationEnabled() {
		return this.autonegotiationEnabled;
	}

	public void setAutonegotiationEnabled(String autonegotiationEnabled) {
		this.autonegotiationEnabled = autonegotiationEnabled;
	}

	public String getBfdMultiplier() {
		return this.bfdMultiplier;
	}

	public void setBfdMultiplier(String bfdMultiplier) {
		this.bfdMultiplier = bfdMultiplier;
	}

	public String getBfdreceiveInterval() {
		return this.bfdreceiveInterval;
	}

	public void setBfdreceiveInterval(String bfdreceiveInterval) {
		this.bfdreceiveInterval = bfdreceiveInterval;
	}

	public String getBfdtransmitInterval() {
		return this.bfdtransmitInterval;
	}

	public void setBfdtransmitInterval(String bfdtransmitInterval) {
		this.bfdtransmitInterval = bfdtransmitInterval;
	}

	public String getDuplex() {
		return this.duplex;
	}

	public void setDuplex(String duplex) {
		this.duplex = duplex;
	}

	public String getEncapsulation() {
		return this.encapsulation;
	}

	public void setEncapsulation(String encapsulation) {
		this.encapsulation = encapsulation;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getFraming() {
		return this.framing;
	}

	public void setFraming(String framing) {
		this.framing = framing;
	}

	public String getHoldtimeDown() {
		return this.holdtimeDown;
	}

	public void setHoldtimeDown(String holdtimeDown) {
		this.holdtimeDown = holdtimeDown;
	}

	public String getHoldtimeUp() {
		return this.holdtimeUp;
	}

	public void setHoldtimeUp(String holdtimeUp) {
		this.holdtimeUp = holdtimeUp;
	}

	public String getInnerVlan() {
		return this.innerVlan;
	}

	public void setInnerVlan(String innerVlan) {
		this.innerVlan = innerVlan;
	}

	public String getInterfaceName() {
		return this.interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getIpv4Address() {
		return this.ipv4Address;
	}

	public void setIpv4Address(String ipv4Address) {
		this.ipv4Address = ipv4Address;
	}

	public String getIpv6Address() {
		return this.ipv6Address;
	}

	public void setIpv6Address(String ipv6Address) {
		this.ipv6Address = ipv6Address;
	}

	public Byte getIsbfdEnabled() {
		return this.isbfdEnabled;
	}

	public void setIsbfdEnabled(Byte isbfdEnabled) {
		this.isbfdEnabled = isbfdEnabled;
	}

	public Byte getIshsrpEnabled() {
		return this.ishsrpEnabled;
	}

	public void setIshsrpEnabled(Byte ishsrpEnabled) {
		this.ishsrpEnabled = ishsrpEnabled;
	}

	public Byte getIsvrrpEnabled() {
		return this.isvrrpEnabled;
	}

	public void setIsvrrpEnabled(Byte isvrrpEnabled) {
		this.isvrrpEnabled = isvrrpEnabled;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMediaType() {
		return this.mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getMtu() {
		return this.mtu;
	}

	public void setMtu(String mtu) {
		this.mtu = mtu;
	}

	public String getOuterVlan() {
		return this.outerVlan;
	}

	public void setOuterVlan(String outerVlan) {
		this.outerVlan = outerVlan;
	}

	public String getPhysicalPort() {
		return this.physicalPort;
	}

	public void setPhysicalPort(String physicalPort) {
		this.physicalPort = physicalPort;
	}

	public String getPortType() {
		return this.portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public String getQosLoopinPassthrough() {
		return this.qosLoopinPassthrough;
	}

	public void setQosLoopinPassthrough(String qosLoopinPassthrough) {
		this.qosLoopinPassthrough = qosLoopinPassthrough;
	}

	public String getSecondaryIpv4Address() {
		return this.secondaryIpv4Address;
	}

	public void setSecondaryIpv4Address(String secondaryIpv4Address) {
		this.secondaryIpv4Address = secondaryIpv4Address;
	}

	public String getSecondaryIpv6Address() {
		return this.secondaryIpv6Address;
	}

	public void setSecondaryIpv6Address(String secondaryIpv6Address) {
		this.secondaryIpv6Address = secondaryIpv6Address;
	}

	public String getSpeed() {
		return this.speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Set<AclPolicyCriteria> getAclPolicyCriterias() {
		return this.aclPolicyCriterias;
	}

	public void setAclPolicyCriterias(Set<AclPolicyCriteria> aclPolicyCriterias) {
		this.aclPolicyCriterias = aclPolicyCriterias;
	}

	public AclPolicyCriteria addAclPolicyCriteria(AclPolicyCriteria aclPolicyCriteria) {
		getAclPolicyCriterias().add(aclPolicyCriteria);
		aclPolicyCriteria.setEthernetInterface(this);

		return aclPolicyCriteria;
	}

	public AclPolicyCriteria removeAclPolicyCriteria(AclPolicyCriteria aclPolicyCriteria) {
		getAclPolicyCriterias().remove(aclPolicyCriteria);
		aclPolicyCriteria.setEthernetInterface(null);

		return aclPolicyCriteria;
	}

	public Set<HsrpVrrpProtocol> getHsrpVrrpProtocols() {
		return this.hsrpVrrpProtocols;
	}

	public void setHsrpVrrpProtocols(Set<HsrpVrrpProtocol> hsrpVrrpProtocols) {
		this.hsrpVrrpProtocols = hsrpVrrpProtocols;
	}

	public HsrpVrrpProtocol addHsrpVrrpProtocol(HsrpVrrpProtocol hsrpVrrpProtocol) {
		getHsrpVrrpProtocols().add(hsrpVrrpProtocol);
		hsrpVrrpProtocol.setEthernetInterface(this);

		return hsrpVrrpProtocol;
	}

	public HsrpVrrpProtocol removeHsrpVrrpProtocol(HsrpVrrpProtocol hsrpVrrpProtocol) {
		getHsrpVrrpProtocols().remove(hsrpVrrpProtocol);
		hsrpVrrpProtocol.setEthernetInterface(null);

		return hsrpVrrpProtocol;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setEthernetInterface(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setEthernetInterface(null);

		return interfaceProtocolMapping;
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

}