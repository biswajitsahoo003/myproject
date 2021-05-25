package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcLocation;

/**
 * 
 * This file contains the PricingIpcLocationRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PricingIpcLocationRepository extends JpaRepository<PricingIpcLocation, Integer> {
	
	List<PricingIpcLocation> findByCityCode(String cityCode);

}
