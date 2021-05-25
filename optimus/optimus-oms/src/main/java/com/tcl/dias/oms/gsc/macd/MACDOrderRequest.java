package com.tcl.dias.oms.gsc.macd;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class MACDOrderRequest {

	public static final String REQUEST_TYPE_NUMBER_ADD = "ADD_NUMBER";
	public static final String REQUEST_TYPE_NUMBER_REMOVE = "REMOVE_NUMBER";
	public static final String REQUEST_TYPE_CHANGE_OUTPULSE = "CHANGE_OUTPULSE";
	public static final String REQUEST_TYPE_ADD_COUNTRY = "ADD_COUNTRY";
	public static final String REQUEST_TYPE_ADD_SITE = "ADD_SITE";
	public static final String REQUEST_TYPE_INTERCONNET_ATTRIBUTE = "INTERCONNET_ATTRIBUTE";
	public static final String REQUEST_TYPE_AGGREGATE_SIP_TRUNK = "AGGREGATE_SIP_TRUNK";

	public static final String CFG_DATA_KEY_PRODUCT_NAME = "productName";
	public static final String CFG_DATA_KEY_ACCESS_TYPE = "accessType";
	public static final String CFG_DATA_KEY_SOURCE = "source";
	public static final String CFG_DATA_KEY_DESTINATION = "destination";
	public static final String CFG_DATA_KEY_CUSTOMER_ID = "customerId";
	public static final String CFG_DATA_KEY_DOWNSTREAM_ORDER_ID = "downstreamOrderId";
	public static final String CFG_DATA_KEY_ASSET_ID = "assetId";
	public static final String CFG_DATA_KEY_OUTPULSE = "outpulse";
	public static final String CFG_DATA_KEY_COMMENT = "comment";
	public static final String SITE_ADDRESS = "SITE_ADDRESS";
	public static final String CFG_DATA_KEY_CUSTOMER_LE_ID = "customerLeId";

	private String requestType;
	@JsonIgnore
	private String productName;
	private List<Map<String, Object>> data;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
