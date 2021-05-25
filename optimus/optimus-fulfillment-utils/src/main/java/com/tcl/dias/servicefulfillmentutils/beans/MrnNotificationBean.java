package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

/**
 * Mrn Notification bean class
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MrnNotificationBean {
	
	private String circuitId;
	private String productName;
	private String customerNameOrProjectName;
	private String rentalOrSale;
	private String distributionCenter;
	private String mrnDate;
	private String mrnNumber;
	private String cpeOptimusId;
	
	private String issueTo;
	private String customerName;
	private String deliveryAddress;
	private String deliveryLocationCity;
	private String deliveryState;
	private String pincode;
	private String contactName;
	private String contactNumber;
	private String cpeModel;
	private String partnerPONumber;
	private String cpeExpectedDeliveryDate;
	private String cpeInvoiceNumber;

	private List<MrnListBean> mrnList;

	public String getCircuitId() {
		return circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerNameOrProjectName() {
		return customerNameOrProjectName;
	}

	public void setCustomerNameOrProjectName(String customerNameOrProjectName) {
		this.customerNameOrProjectName = customerNameOrProjectName;
	}

	public String getRentalOrSale() {
		return rentalOrSale;
	}

	public void setRentalOrSale(String rentalOrSale) {
		this.rentalOrSale = rentalOrSale;
	}

	public String getDistributionCenter() {
		return distributionCenter;
	}

	public void setDistributionCenter(String distributionCenter) {
		this.distributionCenter = distributionCenter;
	}

	public String getMrnDate() {
		return mrnDate;
	}

	public void setMrnDate(String mrnDate) {
		this.mrnDate = mrnDate;
	}

	public String getIssueTo() {
		return issueTo;
	}

	public void setIssueTo(String issueTo) {
		this.issueTo = issueTo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryLocationCity() {
		return deliveryLocationCity;
	}

	public void setDeliveryLocationCity(String deliveryLocationCity) {
		this.deliveryLocationCity = deliveryLocationCity;
	}

	public String getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public List<MrnListBean> getMrnList() {
		return mrnList;
	}

	public void setMrnList(List<MrnListBean> mrnList) {
		this.mrnList = mrnList;
	}

	public String getMrnNumber() {
		return mrnNumber;
	}

	public void setMrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
	}

	public String getCpeOptimusId() {
		return cpeOptimusId;
	}

	public void setCpeOptimusId(String cpeOptimusId) {
		this.cpeOptimusId = cpeOptimusId;
	}

	public String getCpeModel() {
		return cpeModel;
	}

	public void setCpeModel(String cpeModel) {
		this.cpeModel = cpeModel;
	}

	public String getPartnerPONumber() {
		return partnerPONumber;
	}

	public void setPartnerPONumber(String partnerPONumber) {
		this.partnerPONumber = partnerPONumber;
	}

	public String getCpeExpectedDeliveryDate() {
		return cpeExpectedDeliveryDate;
	}

	public void setCpeExpectedDeliveryDate(String cpeExpectedDeliveryDate) {
		this.cpeExpectedDeliveryDate = cpeExpectedDeliveryDate;
	}

	public String getCpeInvoiceNumber() {
		return cpeInvoiceNumber;
	}

	public void setCpeInvoiceNumber(String cpeInvoiceNumber) {
		this.cpeInvoiceNumber = cpeInvoiceNumber;
	}	
}
