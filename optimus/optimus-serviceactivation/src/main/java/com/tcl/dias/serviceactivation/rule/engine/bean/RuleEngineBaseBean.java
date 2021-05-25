package com.tcl.dias.serviceactivation.rule.engine.bean;

public class RuleEngineBaseBean {

	private String vrf;

	private String serviceSubType;

	private String serviceType;

	private String routingProtocal;

	private String routerName;

	private IpAddressDetailsRuleBean ipAddressDetails;

	public IpAddressDetailsRuleBean getIpAddressDetails() {
		return ipAddressDetails;
	}

	public void setIpAddressDetails(IpAddressDetailsRuleBean ipAddressDetails) {
		this.ipAddressDetails = ipAddressDetails;
	}

	public String getRouterName() {
		return routerName;
	}

	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	public String getRoutingProtocal() {
		return routingProtocal;
	}

	public void setRoutingProtocal(String routingProtocal) {
		this.routingProtocal = routingProtocal;
	}

	private boolean lANIPV4isCustomerProvided;

	public String getVrf() {
		return vrf;
	}

	public void setVrf(String vrf) {
		this.vrf = vrf;
	}

	public String getServiceSubType() {
		return serviceSubType;
	}

	public void setServiceSubType(String serviceSubType) {
		this.serviceSubType = serviceSubType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public boolean islANIPV4isCustomerProvided() {
		return lANIPV4isCustomerProvided;
	}

	public void setlANIPV4isCustomerProvided(boolean lANIPV4isCustomerProvided) {
		this.lANIPV4isCustomerProvided = lANIPV4isCustomerProvided;
	}


}
