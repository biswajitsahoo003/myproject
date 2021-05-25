package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

public class CpeBomGvpnIntlViewId implements Serializable {

	private Integer minBw;
	private String minBwUomCd;
	private String portInterface;
	private String routingProtocol;
	private String cpeManagementOption;
	private String licenseType;
	private Integer maxBw;
	private String cpeServiceConfig;
	
	public CpeBomGvpnIntlViewId() {

	}

	public CpeBomGvpnIntlViewId(Integer minBw, String minBwUomCd, String portInterface, String routingProtocol,
			String cpeManagementOption, String licenseType,Integer maxBw, String cpeServiceConfig) {
		super();
		this.minBw = minBw;
		this.minBwUomCd = minBwUomCd;
		this.portInterface = portInterface;
		this.routingProtocol = routingProtocol;
		this.cpeManagementOption = cpeManagementOption;
		this.licenseType = licenseType;
		this.maxBw = maxBw;
		this.cpeServiceConfig = cpeServiceConfig;
	}

	@Override
	public String toString() {
		return "CpeBomGvpnIntlViewId{" +
				"minBw=" + minBw +
				", minBwUomCd='" + minBwUomCd + '\'' +
				", portInterface='" + portInterface + '\'' +
				", routingProtocol='" + routingProtocol + '\'' +
				", cpeManagementOption='" + cpeManagementOption + '\'' +
				", licenseType='" + licenseType + '\'' +
				", maxBw=" + maxBw +
				", cpeServiceConfig='" + cpeServiceConfig + '\'' +
				'}';
	}
}
