package com.tcl.dias.productcatelog.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcPartnerCommission;


/**
 * This file contains the PricingIpcPartnerCommission.java class.
 *  
 * @author Savanya
 *
 */
@Repository
public interface PricingIpcPartnerCommissionRespository extends JpaRepository<PricingIpcPartnerCommission, Integer>{

	PricingIpcPartnerCommission findByProfileId(Integer profileId);
}
