package com.tcl.dias.location.beans;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;

/**
 * CustomerDetails bean
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerDetails {
	private Integer customerId;

	private String customerName;

	private List<LegalEntityDetails> legalEntities;

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

	public List<LegalEntityDetails> getLegalEntities() {
		return legalEntities;
	}

	public void setLegalEntities(List<LegalEntityDetails> legalEntities) {
		this.legalEntities = legalEntities;
	}

	public CustomerDetails() {

	}

	public CustomerDetails(CustomerDetail customerDetail, List<LocationLeCustomer> locationLeCustomer,
			List<AddressDetail> addr) {
		List<LegalEntityDetails> legalEntityList = new ArrayList<>();
		this.setCustomerId(customerDetail.getErfCustomerId());
		this.setCustomerName(customerDetail.getCustomerName());
		locationLeCustomer.stream().forEach(eachLocationLe -> {
			LegalEntityDetails legalEntityDetails = new LegalEntityDetails(customerDetail, eachLocationLe, addr);
			legalEntityList.add(legalEntityDetails);
		});
		this.setLegalEntities(legalEntityList);
	}

	public CustomerDetails(List<CustomerLocation> customerLocation, CustomerDetail customerDetail, List<AddressDetail> addr) {
		List<LegalEntityDetails> legalEntityList = new ArrayList<>();
		this.setCustomerId(customerDetail.getErfCustomerId());
		this.setCustomerName(customerDetail.getCustomerName());
		customerLocation.stream().forEach(eachCustomerLocation -> {
			LegalEntityDetails legalEntityDetails = new LegalEntityDetails(customerDetail, eachCustomerLocation, addr);
			legalEntityList.add(legalEntityDetails);
		});
		this.setLegalEntities(legalEntityList);
	}

}
