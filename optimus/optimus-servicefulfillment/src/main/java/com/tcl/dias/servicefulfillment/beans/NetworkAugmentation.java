package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * This bean used for LM Jeopardy
 * @author yomagesh
 *
 */
public class NetworkAugmentation extends BaseRequest{

	private String accessRingName;
	private String accessRingBandwidth;
	private String isExistingRing;
	private String ringType;
	private String accessRingAggregationBandwidth;
	private String typeOfParenting;
	private String popId;
	private String secondaryPopId;
	private String chamberDetails;
	private String action;
	public String getAccessRingName() {
		return accessRingName;
	}
	public void setAccessRingName(String accessRingName) {
		this.accessRingName = accessRingName;
	}
	public String getAccessRingBandwidth() {
		return accessRingBandwidth;
	}
	public void setAccessRingBandwidth(String accessRingBandwidth) {
		this.accessRingBandwidth = accessRingBandwidth;
	}
	public String getIsExistingRing() {
		return isExistingRing;
	}
	public void setIsExistingRing(String isExistingRing) {
		this.isExistingRing = isExistingRing;
	}
	public String getRingType() {
		return ringType;
	}
	public void setRingType(String ringType) {
		this.ringType = ringType;
	}
	public String getAccessRingAggregationBandwidth() {
		return accessRingAggregationBandwidth;
	}
	public void setAccessRingAggregationBandwidth(String accessRingAggregationBandwidth) {
		this.accessRingAggregationBandwidth = accessRingAggregationBandwidth;
	}
	public String getTypeOfParenting() {
		return typeOfParenting;
	}
	public void setTypeOfParenting(String typeOfParenting) {
		this.typeOfParenting = typeOfParenting;
	}
	public String getPopId() {
		return popId;
	}
	public void setPopId(String popId) {
		this.popId = popId;
	}
	public String getSecondaryPopId() {
		return secondaryPopId;
	}
	public void setSecondaryPopId(String secondaryPopId) {
		this.secondaryPopId = secondaryPopId;
	}
	public String getChamberDetails() {
		return chamberDetails;
	}
	public void setChamberDetails(String chamberDetails) {
		this.chamberDetails = chamberDetails;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
	
	

}
