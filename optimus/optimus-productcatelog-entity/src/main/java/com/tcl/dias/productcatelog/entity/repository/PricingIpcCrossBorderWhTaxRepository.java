package com.tcl.dias.productcatelog.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.PricingIpcCrossBorderWhTax;

/**
 * 
 * This file holds the repository class for the IPC Cross border with holding tax component.
 * 
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 TATA Communications Limited
 * 
 */
@Repository
public interface PricingIpcCrossBorderWhTaxRepository extends JpaRepository<PricingIpcCrossBorderWhTax, Integer> {

	public Optional<PricingIpcCrossBorderWhTax> findByCustomerLeCountryAndDcLocationCountry(String customerLeCountry, String dcLocationCountry);

}
