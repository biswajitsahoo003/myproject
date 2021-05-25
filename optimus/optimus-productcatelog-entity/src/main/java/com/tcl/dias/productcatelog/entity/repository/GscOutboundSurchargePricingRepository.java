package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.GscOutboundSurchargePrices;

/**
 * Repository class for  GscOutboundSurchargePrices entity
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface GscOutboundSurchargePricingRepository extends JpaRepository<GscOutboundSurchargePrices, Integer> {

}
