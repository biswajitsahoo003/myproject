package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.CustomConfigBusinessRules;

/**
 * This file contains the CustomConfigBusinessRulesRepository.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface CustomConfigBusinessRulesRepository extends JpaRepository<CustomConfigBusinessRules, Integer>{
	
	List<CustomConfigBusinessRules> findByProduct_Id(Integer prodFamilyId);

}
