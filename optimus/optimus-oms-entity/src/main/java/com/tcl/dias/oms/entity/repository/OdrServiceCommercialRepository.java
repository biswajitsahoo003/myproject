package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrServiceCommercial;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;

/**
 * 
 * This file contains the OdrServiceCommercialRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface OdrServiceCommercialRepository extends JpaRepository<OdrServiceCommercial, Integer> {
	
	List<OdrServiceCommercial> findByOdrServiceDetail(OdrServiceDetail odrServiceDetail);

	OdrServiceCommercial findFirstByOdrServiceDetailAndReferenceNameAndReferenceTypeAndServiceType(OdrServiceDetail odrServiceDetail,String refName,String refType,String type);

}
