package com.tcl.dias.oms.entity.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceSla;
/**
 * 
 * This file contains repository class of OdrServiceSla entity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrServiceSlaRepository extends JpaRepository<OdrServiceSla, Integer>{
	
	Set<OdrServiceSla> findByOdrServiceDetail(OdrServiceDetail odrServiceDetail);

}
