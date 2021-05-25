package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.CpeBomDetails;

/**
	 * This file contains the CpeBomDetailsRepository.java class.
	 * 
	 *
	 * @author mpalanis
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	@Repository
	public abstract interface CpeBomDetailsRepository extends JpaRepository<CpeBomDetails, Integer> {

		List<CpeBomDetails> findAllByQuoteLeId(Integer quoteLeId);
		void deleteByParentId(Integer parentId);
		void deleteByLocationId(Integer locationId);
		
		List<CpeBomDetails> findAllByLocationIdIn(Set<Integer> locationId);
	}


