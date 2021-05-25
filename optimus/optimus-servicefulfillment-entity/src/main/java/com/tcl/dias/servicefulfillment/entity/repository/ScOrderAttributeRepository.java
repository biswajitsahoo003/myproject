package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;

/**
 * 
 * @author Manojkumar R
 *
 */
@Repository
public interface ScOrderAttributeRepository extends JpaRepository<ScOrderAttribute, Integer> {

	List<ScOrderAttribute> findByScOrderAndIsActive(ScOrder scOrder, String isActive);
	
	List<ScOrderAttribute> findByScOrder_IdAndIsActive(Integer scOrderId, String isActive);
	
	ScOrderAttribute findFirstByAttributeNameAndScOrder(String attributeName, ScOrder scOrder);

	ScOrderAttribute findByScOrder_IdAndAttributeName(Integer scOrderId, String key);
	
	List<ScOrderAttribute> findByAttributeNameInAndScOrder(List<String> attrNames,ScOrder scOrder);
	
	public List<ScOrderAttribute> findByAttributeNameAndAttributeValue(String attributeName, String attributeValue);

	void deleteByScOrder_idAndAttributeName(Integer scOrderId, String key);
}
