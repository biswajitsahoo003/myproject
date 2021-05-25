package com.tcl.dias.serviceactivation.rule.engine.bean;

public class IpAddressDetailsRuleBean {

	private String wanIpV4Address;

	private String wanIpV6Address;

	private String lanIpV4Address;

	private String lanIpV6Address;

	public String getWanIpV4Address() {
		return wanIpV4Address;
	}

	public void setWanIpV4Address(String wanIpV4Address) {
		this.wanIpV4Address = wanIpV4Address;
	}

	public String getWanIpV6Address() {
		return wanIpV6Address;
	}

	public void setWanIpV6Address(String wanIpV6Address) {
		this.wanIpV6Address = wanIpV6Address;
	}

	public String getLanIpV4Address() {
		return lanIpV4Address;
	}

	public void setLanIpV4Address(String lanIpV4Address) {
		this.lanIpV4Address = lanIpV4Address;
	}

	public String getLanIpV6Address() {
		return lanIpV6Address;
	}

	public void setLanIpV6Address(String lanIpV6Address) {
		this.lanIpV6Address = lanIpV6Address;
	}

}
