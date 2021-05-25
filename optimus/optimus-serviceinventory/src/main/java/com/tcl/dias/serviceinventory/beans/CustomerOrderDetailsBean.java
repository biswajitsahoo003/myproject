package com.tcl.dias.serviceinventory.beans;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;

/**
 * This file contains the CustomerOrderDetailsDto.java class this class is used
 * for send response to dashboard of Customer portal .
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerOrderDetailsBean {
	private String oppertunityId;
	private String cuid;
	private String customerName;
	private String customerId;
	private Integer custmerLeId;
	private String customerLeName;
	private Set<CustomerServiceDetailBean> customerServiceDetailBeans;

	public Set<CustomerServiceDetailBean> getCustomerServiceDetailBeans() {
		return customerServiceDetailBeans;
	}

	public void setCustomerServiceDetailBeans(Set<CustomerServiceDetailBean> customerServiceDetailBeans) {
		this.customerServiceDetailBeans = customerServiceDetailBeans;
	}

	public Integer getCustmerLeId() {
		return custmerLeId;
	}

	public void setCustmerLeId(Integer custmerLeId) {
		this.custmerLeId = custmerLeId;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public String getOppertunityId() {
		return oppertunityId;
	}

	public void setOppertunityId(String oppertunityId) {
		this.oppertunityId = oppertunityId;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public CustomerOrderDetailsBean(SIOrder sIOrder) {
		this.cuid = sIOrder.getTpsSfdcCuid();
		this.customerName = sIOrder.getErfCustCustomerName();
		this.oppertunityId = sIOrder.getTpsCrmOptyId();
		this.custmerLeId = sIOrder.getErfCustLeId();
		this.customerLeName = sIOrder.getErfCustLeName();
		this.customerId=sIOrder.getErfCustCustomerId();
		//this.setCustomerServiceDetailBeans(sIOrder.getSiServiceDetails() != null
		//? sIOrder.getSiServiceDetails().stream().map(CustomerServiceDetailBean::new).collect(Collectors.toSet())
		//: null);
	}
}
