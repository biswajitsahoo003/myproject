package com.tcl.dias.serviceinventory.beans;

/**
 * Bean class to hold asset details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIAssetBean {

	private Integer orderId;
	private Integer assetId;
	private String assetType;
	private String number;
	private String outpulse;
	private String origin;
	private String destination;
	private String originNetwork;
	private String tigerOrderId;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOutpulse() {
		return outpulse;
	}

	public void setOutpulse(String outpulse) {
		this.outpulse = outpulse;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOriginNetwork() {
		return originNetwork;
	}

	public void setOriginNetwork(String originNetwork) {
		this.originNetwork = originNetwork;
	}

	public String getTigerOrderId() {
		return tigerOrderId;
	}

	public void setTigerOrderId(String tigerOrderId) {
		this.tigerOrderId = tigerOrderId;
	}

	@Override
	public String toString() {
		return "SIAssetBean{" +
				"orderId=" + orderId +
				", assetId=" + assetId +
				", assetType='" + assetType + '\'' +
				", number='" + number + '\'' +
				", outpulse='" + outpulse + '\'' +
				", origin='" + origin + '\'' +
				", destination='" + destination + '\'' +
				", originNetwork='" + originNetwork + '\'' +
				", tigerOrderId='" + tigerOrderId + '\'' +
				'}';
	}
}
