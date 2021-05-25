package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.ComponentAttributeGroupAssoc;


/**
 * 
 * @author Thamizhselvi Perumal
 *
 */
@Repository
public interface ComponentAttrGroupAssocRepository extends JpaRepository<ComponentAttributeGroupAssoc, Integer>{

	
	public List<ComponentAttributeGroupAssoc> findByComponent_Id(Integer componentId);
	public List<ComponentAttributeGroupAssoc> findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Integer componentId,Integer componentId2,String isActive);
}
