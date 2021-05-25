package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.CofDetails;

import java.util.List;

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
public interface CofDetailsRepository extends JpaRepository<CofDetails, Integer> {

	CofDetails findByOrderUuid(String orderuuid);

	CofDetails findByOrderUuidAndSource(String orderuuid, String source);

	CofDetails findByReferenceIdAndReferenceType(String referenceId, String referenceType);

	CofDetails findByReferenceIdAndReferenceTypeAndSource(String referenceId, String referenceType, String source);

	List<CofDetails> findByOrderUuidIs(String orderUuid);
}
