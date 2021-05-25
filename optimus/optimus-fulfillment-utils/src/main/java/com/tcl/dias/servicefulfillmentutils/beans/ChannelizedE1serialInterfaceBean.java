package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * ChannelizedE1serialInterface Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class ChannelizedE1serialInterfaceBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer e1serialInterfaceId;
	private String bfdMultiplier;
	private String bfdreceiveInterval;
	private String bfdtransmitInterval;
	private String channelGroupNumber;
	private Integer crcsize;
	private String dlciValue;
	private String downcount;
	private String encapsulation;
	private boolean isEdited;
	private Timestamp endDate;
	private String firsttimeSlot;
	private String framing;
	private String holdtimeDown;
	private String holdtimeUp;
	private String interfaceName;
	private String ipv4Address;
	private String ipv6Address;
	private Boolean isbfdEnabled;
	private Boolean iscrcforenabled;
	private Boolean isframed;
	private Boolean ishdlcConfig;
	private Boolean ishsrpEnabled;
	private Boolean isvrrpEnabled;
	private String keepalive;
	private Timestamp lastModifiedDate;
	private String lasttimeSlot;
	private String mode;
	private String modifiedBy;
	private String mtu;
	private String physicalPort;
	private String portType;
	private String secondaryIpv4Address;
	private String secondaryIpv6Address;
	private Timestamp startDate;
	private String upcount;
	private String modifiedIpv4Address;
	private String modifiedIpv6Address;
	private String modifiedSecondaryIpv4Address;
	private String modifiedSecondaryIpv6Address;
	
	private String type;
	
	private Set<HsrpVrrpProtocolBean> hsrpVrrpProtocols;

	private InterfaceDetailBean interfaceDetailBean;

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	public Set<HsrpVrrpProtocolBean> getHsrpVrrpProtocols() {
		
		if(hsrpVrrpProtocols==null) {
			hsrpVrrpProtocols=new HashSet<>();
		}
		return hsrpVrrpProtocols;
	}

	public void setHsrpVrrpProtocols(Set<HsrpVrrpProtocolBean> hsrpVrrpProtocols) {
		this.hsrpVrrpProtocols = hsrpVrrpProtocols;
	}

	private Set<AclPolicyCriteriaBean> aclPolicyCriterias;

	public Integer getE1serialInterfaceId() {
		return e1serialInterfaceId;
	}

	public void setE1serialInterfaceId(Integer e1serialInterfaceId) {
		this.e1serialInterfaceId = e1serialInterfaceId;
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

	public String getChannelGroupNumber() {
		return channelGroupNumber;
	}

	public void setChannelGroupNumber(String channelGroupNumber) {
		this.channelGroupNumber = channelGroupNumber;
	}

	public Integer getCrcsize() {
		return crcsize;
	}

	public void setCrcsize(Integer crcsize) {
		this.crcsize = crcsize;
	}

	public String getDlciValue() {
		return dlciValue;
	}

	public void setDlciValue(String dlciValue) {
		this.dlciValue = dlciValue;
	}

	public String getDowncount() {
		return downcount;
	}

	public void setDowncount(String downcount) {
		this.downcount = downcount;
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

	public String getFirsttimeSlot() {
		return firsttimeSlot;
	}

	public void setFirsttimeSlot(String firsttimeSlot) {
		this.firsttimeSlot = firsttimeSlot;
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

	public Boolean getIscrcforenabled() {
		return iscrcforenabled;
	}

	public void setIscrcforenabled(Boolean iscrcforenabled) {
		this.iscrcforenabled = iscrcforenabled;
	}

	public Boolean getIsframed() {
		return isframed;
	}

	public void setIsframed(Boolean isframed) {
		this.isframed = isframed;
	}

	public Boolean getIshdlcConfig() {
		return ishdlcConfig;
	}

	public void setIshdlcConfig(Boolean ishdlcConfig) {
		this.ishdlcConfig = ishdlcConfig;
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

	public String getKeepalive() {
		return keepalive;
	}

	public void setKeepalive(String keepalive) {
		this.keepalive = keepalive;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLasttimeSlot() {
		return lasttimeSlot;
	}

	public void setLasttimeSlot(String lasttimeSlot) {
		this.lasttimeSlot = lasttimeSlot;
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

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getUpcount() {
		return upcount;
	}

	public void setUpcount(String upcount) {
		this.upcount = upcount;
	}

	public Set<AclPolicyCriteriaBean> getAclPolicyCriterias() {
		return aclPolicyCriterias;
	}

	public void setAclPolicyCriterias(Set<AclPolicyCriteriaBean> aclPolicyCriterias) {
		this.aclPolicyCriterias = aclPolicyCriterias;
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