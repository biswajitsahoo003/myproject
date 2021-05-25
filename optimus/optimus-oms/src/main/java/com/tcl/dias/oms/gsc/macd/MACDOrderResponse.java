package com.tcl.dias.oms.gsc.macd;

import com.tcl.dias.common.constants.CommonConstants;

public class MACDOrderResponse {
	private String status;
	private String message;
	private Integer orderId;
	private String orderCode;
	private Integer quoteId;
	private Integer quoteToLeId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public static MACDOrderResponse failure(String message) {
		MACDOrderResponse response = new MACDOrderResponse();
		response.message = message;
		response.status = CommonConstants.ERROR;
		return response;
	}

	public static MACDOrderResponse successOrder(Integer orderId, String orderCode, String message) {
		MACDOrderResponse response = new MACDOrderResponse();
		response.message = message;
		response.status = CommonConstants.SUCCESS;
		response.orderId = orderId;
		response.orderCode = orderCode;
		return response;
	}

	public static MACDOrderResponse successQuote(Integer quoteId, Integer quoteToLeId, String message) {
		MACDOrderResponse response = new MACDOrderResponse();
		response.message = message;
		response.status = CommonConstants.SUCCESS;
		response.quoteId = quoteId;
		response.quoteToLeId = quoteToLeId;
		return response;
	}
}
