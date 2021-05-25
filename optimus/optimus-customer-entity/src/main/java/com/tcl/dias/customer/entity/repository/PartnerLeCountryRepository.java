package com.tcl.dias.customer.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerLeCountry;

/**
 * Partner Le Country Entity Repository Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerLeCountryRepository extends JpaRepository<PartnerLeCountry, Integer> {
}
