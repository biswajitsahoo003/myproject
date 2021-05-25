package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.QuoteIzosdwanVutmLocationDetail;

/**
 * 
 * This file contains the QuoteIzosdwanVutmLocationDetailRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface QuoteIzosdwanVutmLocationDetailRepository
		extends JpaRepository<QuoteIzosdwanVutmLocationDetail, Integer> {

	List<QuoteIzosdwanVutmLocationDetail> findByReferenceId(Integer referenceId);

	List<QuoteIzosdwanVutmLocationDetail> findByReferenceIdAndBreakupLocation(Integer referenceId, String locationName);

	List<QuoteIzosdwanVutmLocationDetail> findByReferenceIdAndBreakupLocationAndLocationIdNotIn(Integer referenceId,
			String locationName, List<Integer> locations);
	
	List<QuoteIzosdwanVutmLocationDetail> findByReferenceIdAndLocationIdNotIn(Integer referenceId, List<Integer> locations);

}
