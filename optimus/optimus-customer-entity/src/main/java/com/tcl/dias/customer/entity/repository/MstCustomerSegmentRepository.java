package com.tcl.dias.customer.entity.repository;


import com.tcl.dias.customer.entity.entities.MstCustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This file contains the MstCustomerSegmentRepository.java class.
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstCustomerSegmentRepository extends JpaRepository<MstCustomerSegment, Integer> {

	Optional<MstCustomerSegment> findByName(String name);

}
