package com.tcl.dias.location.entity.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.CustomerLocation;

/**
 * This file contains the CustomerLocationRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerLocationRepository extends JpaRepository<CustomerLocation, Integer> {

	public Optional<CustomerLocation> findByLocation_Id(Integer locationId);
	
	public Optional<CustomerLocation> findByLocation_IdAndErfCusCustomerId(Integer locationId,Integer customerId);

	List<CustomerLocation> findByErfCusCustomerIdIn(List<Integer> erfCustomerId);
	
	public List<CustomerLocation> findByLocation_IdAndErfCusCustomerIdIn(Integer locationId,Set<Integer> customerId);

}
