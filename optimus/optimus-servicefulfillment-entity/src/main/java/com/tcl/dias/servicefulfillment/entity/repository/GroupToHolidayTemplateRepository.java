package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.GroupToHolidayTemplate;

/**
 * This file contains the GroupToHolidayTemplateRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface GroupToHolidayTemplateRepository extends JpaRepository<GroupToHolidayTemplate, Integer> {

    GroupToHolidayTemplate findFirstByGroupName(String groupName);
}
