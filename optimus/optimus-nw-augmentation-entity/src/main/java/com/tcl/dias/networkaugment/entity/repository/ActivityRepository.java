package com.tcl.dias.networkaugment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.Activity;

/**
 * 
 * This file contains the ActivityRepository.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query(value = "SELECT * FROM activity where service_id=:serviceId and activity_def_key=:defkey and site_type=:siteType", nativeQuery = true)
    Activity findByServiceIdAndMstActivityDefKeyAndSiteType(@Param("serviceId") Integer serviceId, @Param("defkey") String defkey, String siteType);
	
}
