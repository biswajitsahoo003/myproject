package com.tcl.dias.location.beans;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.location.entity.entities.CustomerSiteLocationItContact;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;

/**
 * List of location Ids
 * 
 *
 * @author RSriramo
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LocationDetails {

	private Integer customerId;

	private String customerName;

	private Integer legalEntityId;

	private String legalEntityName;

	private List<AddressDetail> address = new ArrayList<>();

	private List<Integer> locationIds;

	/**
	 * 
	 * getLocationIds
	 * 
	 * @return locationIds
	 */
	public List<Integer> getLocationIds() {
		return locationIds;
	}

	/**
	 * 
	 * setLocationIds
	 * 
	 * @param locationIds
	 */
	public void setLocationIds(List<Integer> locationIds) {
		this.locationIds = locationIds;
	}

	@Override
	public String toString() {
		return "LocationDetails [locationIds=" + locationIds + "]";
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public List<AddressDetail> getAddress() {
		return address;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public void setAddress(List<AddressDetail> address) {
		this.address = address;
	}

	public LocationDetails() {

	}

	public LocationDetails(LocationLeCustomer eachLocationLeCustomer, List<AddressDetail> addressList,
			List<CustomerDetail> customerDetailsList) {
		for (CustomerDetail customerDetail : customerDetailsList) {
			if (customerDetail.getCustomerLeId().intValue() == eachLocationLeCustomer.getErfCusCustomerLeId()
					.intValue()) {
				this.setLegalEntityId(eachLocationLeCustomer.getErfCusCustomerLeId());
				this.setCustomerId(customerDetail.getCustomerId());
				this.setCustomerName(customerDetail.getCustomerName());
				setAddressDetails(eachLocationLeCustomer, addressList);
				this.setAddress(address);
			}
		}
	}
	
	public void setAddressDetail(AddressDetail addr,AddressDetail addrDet) {
		addr.setAddressId(addrDet.getAddressId());
		addr.setAddressLineOne(addrDet.getAddressLineOne());
		addr.setAddressLineTwo(addrDet.getAddressLineTwo());
		addr.setCity(addrDet.getCity());
		addr.setState(addrDet.getState());
		addr.setCountry(addrDet.getCountry());
		addr.setPincode(addrDet.getPincode());
		addr.setLocality(addrDet.getLocality());
		addr.setLatLong(addrDet.getLatLong());
		addr.setSource(addrDet.getSource());
		addr.setPlotBuilding(addrDet.getPlotBuilding());
		address.add(addr);
	}
	
	public void setAddressDetails(LocationLeCustomer eachLocationLeCustomer,List<AddressDetail> addressList) {
		for (CustomerSiteLocationItContact customerSiteLocItContact : eachLocationLeCustomer
				.getCustomerSiteLocationItContacts()) {
			for (AddressDetail addrDet : addressList) {
				if (addrDet.getAddressId() == customerSiteLocItContact.getCustomerLeLocation().getLocation()
						.getAddressId()) {
					AddressDetail addr = new AddressDetail();
					setAddressDetail(addr, addrDet);
				}
			}
		}
	}
}
