package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.AttributeGroupAttrAssoc;
import com.tcl.dias.productcatelog.entity.entities.ComponentAttributeGroupAssoc;

/**
 * 
 * @author Thamizhselvi Perumal
 *
 */
@Repository
public interface AttributeGroupAttrAssocRepository extends JpaRepository<AttributeGroupAttrAssoc, Integer>
{	
	public List<AttributeGroupAttrAssoc> findByAttributeGroupMaster_Id(Integer groupId);
	public List<AttributeGroupAttrAssoc> findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Integer groupId,Integer groupId2,String isActive);

}
