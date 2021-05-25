package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GscOutboundPricing;

/**
 * Repository for GscOutboundPricing
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface GscOutboundPricingRepository extends JpaRepository<GscOutboundPricing, Integer> {

	public List<GscOutboundPricing> findByDestinationNameIn(List<String> destinationName);

}
