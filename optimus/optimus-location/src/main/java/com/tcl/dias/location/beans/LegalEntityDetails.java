package com.tcl.dias.location.beans;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;

/**
 * LegalEntityDetails bean class
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LegalEntityDetails {

	private Integer legalEntityId;
	
	private List<LocationDetail> location;

	public List<LocationDetail> getLocation() {
		return location;
	}

	public void setLocation(List<LocationDetail> location) {
		this.location = location;
	}

	public LegalEntityDetails() {

	}

	public LegalEntityDetails(CustomerDetail customerDetail, CustomerLocation customerLocation,
			List<AddressDetail> addr) {
		List<LocationDetail> locationDetailsList = new ArrayList<>();
		this.setLegalEntityId(customerDetail.getCustomerLeId());
		addr.stream().forEach(eachAddr -> {
			if (eachAddr.getAddressId() == customerLocation.getLocation().getAddressId()) {
				LocationDetail locationTmp = new LocationDetail();
				locationTmp.setAddress(eachAddr);
				locationDetailsList.add(locationTmp);
			}
		});
		this.setLocation(locationDetailsList);
	}

	public LegalEntityDetails(CustomerDetail customerDetail, LocationLeCustomer locationLeCustomer,
			List<AddressDetail> addr) {
		List<LocationDetail> locationDetailsList = new ArrayList<>();
		this.setLegalEntityId(customerDetail.getCustomerLeId());
		addr.stream().forEach(eachAddr -> {
			if (eachAddr.getAddressId() == locationLeCustomer.getLocation().getAddressId()) {
				LocationDetail locationTmp = new LocationDetail();
				locationTmp.setAddress(eachAddr);
				locationDetailsList.add(locationTmp);
			}
		});
		this.setLocation(locationDetailsList);
	}


	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

}
