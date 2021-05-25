package com.tcl.dias.location.entity.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
@Component
public interface LocationLeCustomerDao{
	
	public List<LocationLeCustomer> getAllLocationsBasedOnSearch(String query);
	
	public List<Location> getPopLocationsBasedOnSearch(String query);
}
