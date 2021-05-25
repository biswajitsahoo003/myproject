package com.tcl.dias.l2oworkflow.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.HolidayTemplate;

/**
 * This file contains the HolidayTemplateRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface HolidayTemplateRepository extends JpaRepository<HolidayTemplate, Integer> {

	List<HolidayTemplate> findAllByTemplateCode(String templateCode);
}
