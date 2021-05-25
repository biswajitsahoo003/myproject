package com.tcl.dias.productcatelog.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.productcatelog.entity.entities.AttributeGroupAttrAssoc;
/**
 * 
 * @author Biswajit Sahoo
 *
 */
@Repository
public interface AttributeGroupAssocRepository extends JpaRepository<AttributeGroupAttrAssoc, Integer> {
	public Optional<AttributeGroupAttrAssoc> findByAttributeMaster_IdAndIsActiveIsNullOrAttributeMaster_IdAndIsActive(
			Integer attributeId1, Integer attributeId2, String isActiveInd);
}
