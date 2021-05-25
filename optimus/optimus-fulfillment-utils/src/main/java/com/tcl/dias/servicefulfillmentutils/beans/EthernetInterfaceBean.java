package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * EthernetInterface Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class EthernetInterfaceBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ethernetInterfaceId;
	private String autonegotiationEnabled;
	private String bfdMultiplier;
	private String bfdreceiveInterval;
	private String bfdtransmitInterval;
	private String duplex;
	private String encapsulation;
	private boolean isEdited;
	private Timestamp endDate;
	private String framing;
	private String holdtimeDown;
	private String holdtimeUp;
	private Integer hsrpVrrpProtocolHsrpVrrpId;
	private String innerVlan;
	private String interfaceName;
	private String ipv4Address;
	private String ipv6Address;
	private Boolean isbfdEnabled;
	private Boolean ishsrpEnabled;
	private Boolean isvrrpEnabled;
	private Timestamp lastModifiedDate;
	private String mediaType;
	private String mode;
	private String modifiedBy;
	private String mtu;
	private String outerVlan;
	private String physicalPort;
	private String portType;
	private String qosLoopinPassthrough;
	private String secondaryIpv4Address;
	private String secondaryIpv6Address;
	private String speed;
	private Timestamp startDate;
	private String modifiedIpv4Address;
	private String modifiedIpv6Address;
	private String modifiedSecondaryIpv4Address;
	private String modifiedSecondaryIpv6Address;

	
	private String type;

	private Set<AclPolicyCriteriaBean> aclPolicyCriterias;

	private Set<HsrpVrrpProtocolBean> hsrpVrrpProtocols;

	private InterfaceDetailBean interfaceDetailBean;

	public Integer getEthernetInterfaceId() {
		return ethernetInterfaceId;
	}

	public void setEthernetInterfaceId(Integer ethernetInterfaceId) {
		this.ethernetInterfaceId = ethernetInterfaceId;
	}

	public String getAutonegotiationEnabled() {
		return autonegotiationEnabled;
	}

	public void setAutonegotiationEnabled(String autonegotiationEnabled) {
		this.autonegotiationEnabled = autonegotiationEnabled;
	}

	public String getBfdMultiplier() {
		return bfdMultiplier;
	}

	public void setBfdMultiplier(String bfdMultiplier) {
		this.bfdMultiplier = bfdMultiplier;
	}

	public String getBfdreceiveInterval() {
		return bfdreceiveInterval;
	}

	public void setBfdreceiveInterval(String bfdreceiveInterval) {
		this.bfdreceiveInterval = bfdreceiveInterval;
	}

	public String getBfdtransmitInterval() {
		return bfdtransmitInterval;
	}

	public void setBfdtransmitInterval(String bfdtransmitInterval) {
		this.bfdtransmitInterval = bfdtransmitInterval;
	}

	public String getDuplex() {
		return duplex;
	}

	public void setDuplex(String duplex) {
		this.duplex = duplex;
	}

	public String getEncapsulation() {
		return encapsulation;
	}

	public void setEncapsulation(String encapsulation) {
		this.encapsulation = encapsulation;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getFraming() {
		return framing;
	}

	public void setFraming(String framing) {
		this.framing = framing;
	}

	public String getHoldtimeDown() {
		return holdtimeDown;
	}

	public void setHoldtimeDown(String holdtimeDown) {
		this.holdtimeDown = holdtimeDown;
	}

	public String getHoldtimeUp() {
		return holdtimeUp;
	}

	public void setHoldtimeUp(String holdtimeUp) {
		this.holdtimeUp = holdtimeUp;
	}

	public Integer getHsrpVrrpProtocolHsrpVrrpId() {
		return hsrpVrrpProtocolHsrpVrrpId;
	}

	public void setHsrpVrrpProtocolHsrpVrrpId(Integer hsrpVrrpProtocolHsrpVrrpId) {
		this.hsrpVrrpProtocolHsrpVrrpId = hsrpVrrpProtocolHsrpVrrpId;
	}

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

	public String getIpv4Address() {
		return ipv4Address;
	}

	public void setIpv4Address(String ipv4Address) {
		this.ipv4Address = ipv4Address;
	}

	public String getIpv6Address() {
		return ipv6Address;
	}

	public void setIpv6Address(String ipv6Address) {
		this.ipv6Address = ipv6Address;
	}

	public Boolean getIsbfdEnabled() {
		return isbfdEnabled;
	}

	public void setIsbfdEnabled(Boolean isbfdEnabled) {
		this.isbfdEnabled = isbfdEnabled;
	}

	public Boolean getIshsrpEnabled() {
		return ishsrpEnabled;
	}

	public void setIshsrpEnabled(Boolean ishsrpEnabled) {
		this.ishsrpEnabled = ishsrpEnabled;
	}

	public Boolean getIsvrrpEnabled() {
		return isvrrpEnabled;
	}

	public void setIsvrrpEnabled(Boolean isvrrpEnabled) {
		this.isvrrpEnabled = isvrrpEnabled;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getMtu() {
		return mtu;
	}

	public void setMtu(String mtu) {
		this.mtu = mtu;
	}

	public String getOuterVlan() {
		return outerVlan;
	}

	public void setOuterVlan(String outerVlan) {
		this.outerVlan = outerVlan;
	}

	public String getPhysicalPort() {
		return physicalPort;
	}

	public void setPhysicalPort(String physicalPort) {
		this.physicalPort = physicalPort;
	}

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public String getQosLoopinPassthrough() {
		return qosLoopinPassthrough;
	}

	public void setQosLoopinPassthrough(String qosLoopinPassthrough) {
		this.qosLoopinPassthrough = qosLoopinPassthrough;
	}

	public String getSecondaryIpv4Address() {
		return secondaryIpv4Address;
	}

	public void setSecondaryIpv4Address(String secondaryIpv4Address) {
		this.secondaryIpv4Address = secondaryIpv4Address;
	}

	public String getSecondaryIpv6Address() {
		return secondaryIpv6Address;
	}

	public void setSecondaryIpv6Address(String secondaryIpv6Address) {
		this.secondaryIpv6Address = secondaryIpv6Address;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Set<AclPolicyCriteriaBean> getAclPolicyCriterias() {
		
		if(aclPolicyCriterias==null) {
			aclPolicyCriterias=new HashSet<>();
		}
		return aclPolicyCriterias;
	}

	public void setAclPolicyCriterias(Set<AclPolicyCriteriaBean> aclPolicyCriterias) {
		this.aclPolicyCriterias = aclPolicyCriterias;
	}

	public Set<HsrpVrrpProtocolBean> getHsrpVrrpProtocols() {
		if(hsrpVrrpProtocols==null){
			hsrpVrrpProtocols = new HashSet<>();
		}
		return hsrpVrrpProtocols;
	}

	public void setHsrpVrrpProtocols(Set<HsrpVrrpProtocolBean> hsrpVrrpProtocols) {
		this.hsrpVrrpProtocols = hsrpVrrpProtocols;
	}

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}
}