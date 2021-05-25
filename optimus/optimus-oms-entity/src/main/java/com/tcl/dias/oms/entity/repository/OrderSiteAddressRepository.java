package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OrderSiteAddress;

/**
 * 
 * Repository class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OrderSiteAddressRepository extends JpaRepository<OrderSiteAddress, Integer> {

	OrderSiteAddress findByReferenceIdAndReferenceNameAndSiteType(String referenceId,String referenceName,String siteType);

}
