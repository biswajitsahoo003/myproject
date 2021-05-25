package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * This class is used for task - Get Supplier Acceptance.
 *
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SupplierAcceptance extends TaskDetailsBaseBean {

	private String provideOrderreferenceId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String providerOrderLogDate;
	private String delayReason;
	private String cafNumber;
	private String supplierOrderId;
	private String raNumber;
	private String otherId;

	public String getProvideOrderreferenceId() {
		return provideOrderreferenceId;
	}

	public void setProvideOrderreferenceId(String provideOrderreferenceId) {
		this.provideOrderreferenceId = provideOrderreferenceId;
	}

	public String getProviderOrderLogDate() {
		return providerOrderLogDate;
	}

	public void setProviderOrderLogDate(String providerOrderLogDate) {
		this.providerOrderLogDate = providerOrderLogDate;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getCafNumber() {
		return cafNumber;
	}

	public void setCafNumber(String cafNumber) {
		this.cafNumber = cafNumber;
	}

	public String getSupplierOrderId() {
		return supplierOrderId;
	}

	public void setSupplierOrderId(String supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}

	public String getRaNumber() {
		return raNumber;
	}

	public void setRaNumber(String raNumber) {
		this.raNumber = raNumber;
	}

	public String getOtherId() {
		return otherId;
	}

	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

}
