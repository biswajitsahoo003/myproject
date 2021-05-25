package com.tcl.dias.serviceactivation.rule.engine.bean;

import java.sql.Timestamp;

public class ChannelizedSdhInterfaceBean extends RuleEngineBaseBean {
	
	
	private Integer sdhInterfaceId;

	private String _64kFirstTimeSlot;

	private String _64kLastTimeSlot;

	private String bfdMultiplier;

	private String bfdreceiveInterval;

	private String bfdtransmitInterval;

	private String channelGroupNumber;

	private String dlciValue;

	private String downcount;

	private String encapsulation;

	private Timestamp endDate;

	private String framing;

	private String holdtimeDown;

	private String holdtimeUp;

	private String interfaceName;

	private String ipv4Address;

	private String ipv6Address;

	private Byte isbfdEnabled;

	private Byte isframed;

	private Byte ishdlcConfig;

	private Byte ishsrpEnabled;

	private Byte isvrrpEnabled;

	private Integer j;

	private String keepalive;

	private String klm;

	private Timestamp lastModifiedDate;

	private String mode;

	private String modifiedBy;

	private String mtu;

	private String physicalPort;

	private String portType;

	private String posframing;

	private String secondaryIpv4Address;

	private String secondaryIpv6Address;

	private Timestamp startDate;

	private String upcount;

	public Integer getSdhInterfaceId() {
		return sdhInterfaceId;
	}

	public void setSdhInterfaceId(Integer sdhInterfaceId) {
		this.sdhInterfaceId = sdhInterfaceId;
	}

	public String get_64kFirstTimeSlot() {
		return _64kFirstTimeSlot;
	}

	public void set_64kFirstTimeSlot(String _64kFirstTimeSlot) {
		this._64kFirstTimeSlot = _64kFirstTimeSlot;
	}

	public String get_64kLastTimeSlot() {
		return _64kLastTimeSlot;
	}

	public void set_64kLastTimeSlot(String _64kLastTimeSlot) {
		this._64kLastTimeSlot = _64kLastTimeSlot;
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

	public Byte getIsbfdEnabled() {
		return isbfdEnabled;
	}

	public void setIsbfdEnabled(Byte isbfdEnabled) {
		this.isbfdEnabled = isbfdEnabled;
	}

	public Byte getIsframed() {
		return isframed;
	}

	public void setIsframed(Byte isframed) {
		this.isframed = isframed;
	}

	public Byte getIshdlcConfig() {
		return ishdlcConfig;
	}

	public void setIshdlcConfig(Byte ishdlcConfig) {
		this.ishdlcConfig = ishdlcConfig;
	}

	public Byte getIshsrpEnabled() {
		return ishsrpEnabled;
	}

	public void setIshsrpEnabled(Byte ishsrpEnabled) {
		this.ishsrpEnabled = ishsrpEnabled;
	}

	public Byte getIsvrrpEnabled() {
		return isvrrpEnabled;
	}

	public void setIsvrrpEnabled(Byte isvrrpEnabled) {
		this.isvrrpEnabled = isvrrpEnabled;
	}

	public Integer getJ() {
		return j;
	}

	public void setJ(Integer j) {
		this.j = j;
	}

	public String getKeepalive() {
		return keepalive;
	}

	public void setKeepalive(String keepalive) {
		this.keepalive = keepalive;
	}

	public String getKlm() {
		return klm;
	}

	public void setKlm(String klm) {
		this.klm = klm;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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

	public String getPosframing() {
		return posframing;
	}

	public void setPosframing(String posframing) {
		this.posframing = posframing;
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
	
	

}
