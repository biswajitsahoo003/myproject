package com.tcl.dias.networkaugment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.ScOrder;
import com.tcl.dias.networkaugment.entity.entities.ScOrderAttribute;

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

}
