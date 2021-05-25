package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstActivityDef;

/**
 * This file contains the MstActivityDefRepository.java class.
 * 
 *
 * @author MAayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface MstActivityDefRepository extends JpaRepository<MstActivityDef, Integer> {

	MstActivityDef findByKey(String string);

	/**
	 * Find Mst Activity def by process key and activity key
	 *
	 * @param processKey
	 * @param activityKey
	 * @return {@link MstActivityDef >}
	 */
	@Query(value = "SELECT * FROM mst_activity_def where process_def_key=:processKey and `key`=:activityKey limit 1", nativeQuery = true)
	MstActivityDef findByProcessKeyAndActivityKey(@Param("processKey") String processKey, @Param("activityKey") String activityKey);

}
