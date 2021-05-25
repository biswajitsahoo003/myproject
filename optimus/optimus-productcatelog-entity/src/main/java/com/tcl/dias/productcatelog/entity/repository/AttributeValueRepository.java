package com.tcl.dias.productcatelog.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.productcatelog.entity.entities.AttributeValue;

/**
 * 
 * @author Biswajit Sahoo
 *
 */
@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer> {
	public List<AttributeValue> findByIdInAndIsActiveIsNullOrIdInAndIsActive(List<Integer> attributeId,
			List<Integer> attributeId1, String activeInd);
}
