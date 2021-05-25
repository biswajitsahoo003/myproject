package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrContractInfo;
import com.tcl.dias.oms.entity.entities.OdrOrder;
/**
 * 
 *  This file contains repository class of OdrContractInfo entity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrContractInfoRepository extends JpaRepository<OdrContractInfo, Integer>{
	
	List<OdrContractInfo> findByOdrOrder(OdrOrder odrOrder);
}
