package com.tcl.dias.servicefulfillmentutils.beans.gsc;

public class SipTrunkDetails {
	
	private String sipInformation;
	private String trunkName;
	private String sipSignalingIp;
	private String mediaIp1;
	private String mediaIp2;
	private String concurrentChannels1;
	private String concurrentChannels2;
	private String voipProtocol;
	private String sipTransportProtocol;
	private String numberingFormat;
	private String codecsAllowed;
	private String dtmfType;
	private String customerSBCIp;
	
	
	public String getSipInformation() {
		return sipInformation;
	}
	
	public void setSipInformation(String sipInformation) {
		this.sipInformation = sipInformation;
	}
	
	public String getTrunkName() {
		return trunkName;
	}
	
	public void setTrunkName(String trunkName) {
		this.trunkName = trunkName;
	}
	
	public String getSipSignalingIp() {
		return sipSignalingIp;
	}
	
	public void setSipSignalingIp(String sipSignalingIp) {
		this.sipSignalingIp = sipSignalingIp;
	}
	
	public String getMediaIp1() {
		return mediaIp1;
	}
	
	public void setMediaIp1(String mediaIp1) {
		this.mediaIp1 = mediaIp1;
	}
	
	public String getMediaIp2() {
		return mediaIp2;
	}
	
	public void setMediaIp2(String mediaIp2) {
		this.mediaIp2 = mediaIp2;
	}
	
	public String getConcurrentChannels1() {
		return concurrentChannels1;
	}
	
	public void setConcurrentChannels1(String concurrentChannels1) {
		this.concurrentChannels1 = concurrentChannels1;
	}
	
	public String getConcurrentChannels2() {
		return concurrentChannels2;
	}
	
	public void setConcurrentChannels2(String concurrentChannels2) {
		this.concurrentChannels2 = concurrentChannels2;
	}
	
	public String getVoipProtocol() {
		return voipProtocol;
	}
	
	public void setVoipProtocol(String voipProtocol) {
		this.voipProtocol = voipProtocol;
	}
	
	public String getSipTransportProtocol() {
		return sipTransportProtocol;
	}
	
	public void setSipTransportProtocol(String sipTransportProtocol) {
		this.sipTransportProtocol = sipTransportProtocol;
	}
	
	public String getNumberingFormat() {
		return numberingFormat;
	}
	
	public void setNumberingFormat(String numberingFormat) {
		this.numberingFormat = numberingFormat;
	}
	
	public String getCodecsAllowed() {
		return codecsAllowed;
	}
	
	public void setCodecsAllowed(String codecsAllowed) {
		this.codecsAllowed = codecsAllowed;
	}
	
	public String getDtmfType() {
		return dtmfType;
	}
	
	public void setDtmfType(String dtmfType) {
		this.dtmfType = dtmfType;
	}
	
	public String getCustomerSBCIp() {
		return customerSBCIp;
	}
	
	public void setCustomerSBCIp(String customerSBCIp) {
		this.customerSBCIp = customerSBCIp;
	}
	
}
