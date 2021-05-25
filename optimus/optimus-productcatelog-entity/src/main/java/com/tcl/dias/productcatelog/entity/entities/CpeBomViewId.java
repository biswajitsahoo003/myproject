package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

/**
 * This file contains the composite Id of CpeBomView.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CpeBomViewId implements Serializable{
	
	private Integer lastMileBandwidthId;
	private Integer maxBandwidthId;
	private Integer minBandwidthId;
	private Integer portInterfaceId;
	private Integer routingProtocolId;
	private Integer resourceId;
	private Integer bomId;

	
	public CpeBomViewId () {
		
	}
	
	public CpeBomViewId(Integer lastMileBandwidthId, Integer maxBandwidthId, Integer minBandwidthId,
			Integer portInterfaceId, Integer routingProtocolId, Integer resourceId,Integer bomId) {
		super();
		this.lastMileBandwidthId = lastMileBandwidthId;
		this.maxBandwidthId = maxBandwidthId;
		this.minBandwidthId = minBandwidthId;
		this.portInterfaceId = portInterfaceId;
		this.routingProtocolId = routingProtocolId;
		this.resourceId = resourceId;
		this.bomId=bomId;
	}

	@Override
	public String toString() {
		return "CpeBomViewId [lastMileBandwidthId=" + lastMileBandwidthId + ", maxBandwidthId=" + maxBandwidthId
				+ ", minBandwidthId=" + minBandwidthId + ", portInterfaceId=" + portInterfaceId + ", routingProtocolId="
				+ routingProtocolId + ", resourceId=" + resourceId + ", bomId=" + bomId + "]";
	}


}
