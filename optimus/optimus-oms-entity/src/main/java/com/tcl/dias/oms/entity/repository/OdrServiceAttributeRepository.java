package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
/**
 * 
 * This file contains repository class of OdrServiceAttribute entity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrServiceAttributeRepository extends JpaRepository<OdrServiceAttribute, Integer>{
	
	OdrServiceAttribute findByAttributeNameAndOdrServiceDetail_IdAndCategory(String attributeName,Integer odrServiceDetailId,String category);

	List<OdrServiceAttribute> findByOdrServiceDetail_Id(Integer odrServiceDetailId);
	
	OdrServiceAttribute findFirstByAttributeNameAndOdrServiceDetail_IdAndCategory(String attributeName,Integer odrServiceDetailId,String category);

}
