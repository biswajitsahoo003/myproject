package com.tcl.dias.common.serviceinventory.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean class to contain Service Inventory get order request
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class SIGetOrderRequest {
	private String orderId;
	private String originCountry;
	private Boolean orderAttributes = Boolean.FALSE;
	private Boolean serviceDetailAttributes = Boolean.FALSE;
	private Boolean assets = Boolean.FALSE;
	private Boolean assetAttributes = Boolean.FALSE;
	private List<String> assetTypes = new ArrayList<>();
	private List<String> assetRelationTypes = new ArrayList<>();

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Boolean getOrderAttributes() {
		return orderAttributes;
	}

	public void setOrderAttributes(Boolean orderAttributes) {
		this.orderAttributes = orderAttributes;
	}

	public Boolean getServiceDetailAttributes() {
		return serviceDetailAttributes;
	}

	public void setServiceDetailAttributes(Boolean serviceDetailAttributes) {
		this.serviceDetailAttributes = serviceDetailAttributes;
	}

	public Boolean getAssets() {
		return assets;
	}

	public void setAssets(Boolean assets) {
		this.assets = assets;
	}

	public Boolean getAssetAttributes() {
		return assetAttributes;
	}

	public void setAssetAttributes(Boolean assetAttributes) {
		this.assetAttributes = assetAttributes;
	}

	public List<String> getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(List<String> assetTypes) {
		this.assetTypes = assetTypes;
	}

	public List<String> getAssetRelationTypes() {
		return assetRelationTypes;
	}

	public void setAssetRelationTypes(List<String> assetRelationTypes) {
		this.assetRelationTypes = assetRelationTypes;
	}
}
