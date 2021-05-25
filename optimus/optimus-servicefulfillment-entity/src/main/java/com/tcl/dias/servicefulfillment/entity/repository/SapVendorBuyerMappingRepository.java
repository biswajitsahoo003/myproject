package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.SapVendorBuyerMapping;

/**
 * 
 * This file contains the SapVendorBuyerMappingRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface SapVendorBuyerMappingRepository extends JpaRepository<SapVendorBuyerMapping, Integer>{
	List<SapVendorBuyerMapping> findByStatus(Byte status);
}
