package com.tcl.dias.customer.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;

/**
 * Customer DTO class
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerDto {

	private Integer customerId;

	private String customerName;

	private List<CustomerLegalEntityDto> legalEntity = new ArrayList<>();

	public CustomerDto() {

	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setLegalEntity(List<CustomerLegalEntityDto> legalEntity) {
		this.legalEntity = legalEntity;
	}

	public CustomerDto(Integer customerId, List<CustomerLegalEntity> customerLegalentityList,
			List<AttachmentDto> attachmentDto) {
		List<CustomerLegalEntityDto> customerLegalEntityDtoList = new ArrayList<>();
		this.setCustomerId(customerId);
		for (CustomerLegalEntity customerLegalEntityTmp : customerLegalentityList) {
			if (customerLegalEntityTmp.getCustomer().getId() == customerId) {
				this.setCustomerName(customerLegalEntityTmp.getCustomer().getCustomerName());
				break;
			}
		}

		for (CustomerLegalEntity customerLegalEntityTmp : customerLegalentityList) {
			customerLegalEntityDtoList.add(new CustomerLegalEntityDto(customerLegalEntityTmp, attachmentDto));
		}
		this.setLegalEntity(customerLegalEntityDtoList);
	}

	public CustomerDto(Integer customerId, List<CustomerLegalEntity> customerLegalentityList) {
		List<CustomerLegalEntityDto> customerLegalEntityDtoList = new ArrayList<>();
		this.setCustomerId(customerId);
		for (CustomerLegalEntity customerLegalEntityTmp : customerLegalentityList) {
			if (customerLegalEntityTmp.getCustomer().getId() == customerId) {
				this.setCustomerName(customerLegalEntityTmp.getCustomer().getCustomerName());
				break;
			}
		}

		for (CustomerLegalEntity customerLegalEntityTmp : customerLegalentityList) {
			customerLegalEntityDtoList.add(new CustomerLegalEntityDto(customerLegalEntityTmp));
		}
		this.setLegalEntity(customerLegalEntityDtoList);
	}

	public List<CustomerLegalEntityDto> getLegalEntity() {
		return legalEntity;
	}

	/*
	 * public CustomerDto() {
	 * 
	 * }
	 */
}
