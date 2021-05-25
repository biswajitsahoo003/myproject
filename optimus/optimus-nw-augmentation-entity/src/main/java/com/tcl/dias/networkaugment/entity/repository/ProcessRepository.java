package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * This file contains the ProcessRepository.java class.
 * 
 *
 * @author MAYANSHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ProcessRepository extends JpaRepository<com.tcl.dias.networkaugment.entity.entities.Process, Integer> {
	
	Page<com.tcl.dias.networkaugment.entity.entities.Process> findAll(Specification<Process> specification, Pageable pageable);

	com.tcl.dias.networkaugment.entity.entities.Process findFirstByStage_idAndMstProcessDef_keyOrderByIdDesc(Integer stageId, String processKey);

}
