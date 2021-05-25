package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface ProcessRepository extends JpaRepository<com.tcl.dias.servicefulfillment.entity.entities.Process, Integer> {
	
	Page<com.tcl.dias.servicefulfillment.entity.entities.Process> findAll(Specification<Process> specification, Pageable pageable);

	com.tcl.dias.servicefulfillment.entity.entities.Process findFirstByStage_idAndMstProcessDef_keyOrderByIdDesc(Integer stageId,String processKey);

	
	@Query(value = "SELECT * FROM process where service_id=:serviceId and process_def_key=:defkey and site_type=:siteType", nativeQuery = true)
	com.tcl.dias.servicefulfillment.entity.entities.Process findByServiceIdAndMstProcessDefKeyAndSiteTye(@Param("serviceId") Integer serviceId,@Param("defkey") String defkey, String siteType);
	
}
