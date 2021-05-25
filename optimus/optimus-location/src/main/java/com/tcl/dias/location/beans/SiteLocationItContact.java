package com.tcl.dias.location.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.location.entity.entities.CustomerSiteLocationItContact;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;

/**
 * SiteLocationItContact Bean class
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class SiteLocationItContact {

	private Integer customerId;

	private String customerName;

	private Integer legalEntityId;

	private String legalEntityName;

	private List<AddressDetail> address = new ArrayList<>();

	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public List<AddressDetail> getAddress() {
		return address;
	}

	public void setAddress(List<AddressDetail> address) {
		this.address = address;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public SiteLocationItContact() {

	}

	public Integer getCustomerId() {
		return customerId;
	}

	public SiteLocationItContact(LocationLeCustomer eachLocationLeCustomer, List<AddressDetail> addressList,
			List<CustomerDetail> customerDetailsList) {
		for (CustomerDetail customerDetail : customerDetailsList) {
			if (customerDetail.getCustomerLeId().intValue() == eachLocationLeCustomer.getErfCusCustomerLeId()
					.intValue()) {
				this.setLegalEntityId(eachLocationLeCustomer.getErfCusCustomerLeId());
				this.setCustomerId(customerDetail.getCustomerId());
				this.setCustomerName(customerDetail.getCustomerName());
				List<LocationItContact> iTContact = new ArrayList<>();
				for (CustomerSiteLocationItContact customerSiteLocItContact : eachLocationLeCustomer
						.getCustomerSiteLocationItContacts()) {
					if (customerSiteLocItContact.getIsActive() == 1) {
						LocationItContact locationItContact = new LocationItContact();
						locationItContact.setName(customerSiteLocItContact.getName());
						locationItContact.setEmail(customerSiteLocItContact.getEmailId());
						locationItContact.setContactNo(customerSiteLocItContact.getContactNumber());
						iTContact.add(locationItContact);
						for (AddressDetail addrDet : addressList) {
							if (addrDet.getAddressId() == customerSiteLocItContact.getCustomerLeLocation().getLocation()
									.getAddressId()) {
								AddressDetail addr = new AddressDetail();
								if (!Objects.isNull(addr)) {
									addr.setAddressId(addrDet.getAddressId());
									addr.setAddressLineOne(addrDet.getAddressLineOne());
									addr.setAddressLineTwo(addrDet.getAddressLineTwo());
									addr.setCity(addrDet.getCity());
									addr.setState(addrDet.getState());
									addr.setCountry(addrDet.getCountry());
									addr.setPincode(addrDet.getPincode());
									addr.setLocality(addrDet.getLocality());
									addr.setLocationItContact(locationItContact);
									addr.setLatLong(addrDet.getLatLong());
									addr.setSource(addrDet.getSource());
									addr.setPlotBuilding(addrDet.getPlotBuilding());
									address.add(addr);
								}
							}
						}
					}
				}
				this.setAddress(address);
				break;
			}
		}
	}
}
