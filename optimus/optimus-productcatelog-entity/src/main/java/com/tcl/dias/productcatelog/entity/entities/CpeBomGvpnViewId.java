package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

public class CpeBomGvpnViewId implements Serializable {

	private Integer minBw;
	private String minBwUomCd;
	private String portInterface;
	private String routingProtocol;
	private String cpeManagementOption;
	private String licenseType;
	private Integer maxBw;
	
	public CpeBomGvpnViewId() {

	}

	public CpeBomGvpnViewId(Integer minBw, String minBwUomCd, String portInterface, String routingProtocol,
			String cpeManagementOption, String licenseType,Integer maxBw) {
		super();
		this.minBw = minBw;
		this.minBwUomCd = minBwUomCd;
		this.portInterface = portInterface;
		this.routingProtocol = routingProtocol;
		this.cpeManagementOption = cpeManagementOption;
		this.licenseType = licenseType;
		this.maxBw = maxBw;
	}

	@Override
	public String toString() {
		return "CpeBomGvpnViewId [minBw=" + minBw + ", minBwUomCd=" + minBwUomCd + ", portInterface=" + portInterface
				+ ", routingProtocol=" + routingProtocol + ", cpeManagementOption=" + cpeManagementOption
				+ ", licenseType=" + licenseType + ", maxBw=" + maxBw +"]";
	}

}
