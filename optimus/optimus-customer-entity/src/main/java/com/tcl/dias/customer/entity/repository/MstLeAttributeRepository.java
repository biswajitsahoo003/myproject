package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.MstLeAttribute;

/**
 * This file contains the MstLe Attribute Repository Details
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstLeAttributeRepository extends JpaRepository<MstLeAttribute, Integer> {
	
	Optional<MstLeAttribute> findByName(String name);

	/**
	 * Find By Master Le Attribute Name
	 *
	 * @param mstLeAttributeName
	 * @return {@link List<MstLeAttribute>}
	 */
	@Query(value="select mst.id,mst.description,mst.name,mst.status,mst.type from mst_le_attributes mst where mst.name like :mstLeAttributeName% ", nativeQuery = true)
    List<MstLeAttribute> findByMstLeAttributeName(@Param("mstLeAttributeName") String mstLeAttributeName);
}
