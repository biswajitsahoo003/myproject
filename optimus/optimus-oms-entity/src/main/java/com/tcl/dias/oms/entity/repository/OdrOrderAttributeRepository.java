package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrOrderAttribute;
/**
 * 
 * This file contains repository class of OdrOrderAttribute entity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrOrderAttributeRepository extends JpaRepository<OdrOrderAttribute, Integer>{

	List<OdrOrderAttribute> findByOdrOrder(OdrOrder odrOrder);
}
