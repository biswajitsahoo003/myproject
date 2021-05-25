package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrComponentAttribute;
/**
 * 
 * This file contains the OdrComponentAttributeRepository.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OdrComponentAttributeRepository extends JpaRepository<OdrComponentAttribute, Integer>{
	
	List<OdrComponentAttribute> findByOdrComponent(OdrComponent odrComponent);
	
	OdrComponentAttribute findByOdrComponentAndAttributeValue(OdrComponent odrComponent,String attributeValue);
	
	OdrComponentAttribute findFirstByOdrComponentAndAttributeName(OdrComponent odrComponent,String attributeName);

}
