package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.HolidayTemplate;

import java.util.List;

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
