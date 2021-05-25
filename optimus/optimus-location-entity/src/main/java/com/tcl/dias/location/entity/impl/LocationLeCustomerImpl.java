package com.tcl.dias.location.entity.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
@Repository
@Transactional
public class LocationLeCustomerImpl implements LocationLeCustomerDao {
	
	@PersistenceContext
    private EntityManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public List<LocationLeCustomer> getAllLocationsBasedOnSearch(String query) {
		List<LocationLeCustomer> employees = manager.createNativeQuery(query,LocationLeCustomer.class).getResultList();
        return employees;
	}

	@Override
	public List<Location> getPopLocationsBasedOnSearch(String query) {
		List<Location> employees = manager.createNativeQuery(query,Location.class).getResultList();
        return employees;
	}


}
