package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.BusinessHourTemplate;

import java.util.List;

/**
 * This file contains the BusinessHourTemplateRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface BusinessHourTemplateRepository extends JpaRepository<BusinessHourTemplate, Integer> {
	
	List<BusinessHourTemplate> findAllByTemplateCode(String templateCode);
}
