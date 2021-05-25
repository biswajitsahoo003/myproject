package com.tcl.dias.location.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.location.entity.entities.CustomerSiteLocationItContact;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import java.util.List;
import java.util.Optional;

/**
 * CustomerLocationItInfoRepository.java.
 * 
 *
 * @author Kusuma K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface CustomerLocationItInfoRepository extends JpaRepository<CustomerSiteLocationItContact, Integer> {

	List<CustomerSiteLocationItContact> findByCustomerLocation(CustomerLocation customerlocation);

	List<CustomerSiteLocationItContact> findByCustomerLocationAndIsActive(CustomerLocation customerlocatiBon,
			Byte isActive);

	Optional<CustomerSiteLocationItContact> findByIdAndIsActive(Integer cusLocid, Byte isActive);
	
	CustomerSiteLocationItContact findByCustomerLeLocationAndIsActive(LocationLeCustomer locationLeCustomer, Byte isActive);
	
	

}
