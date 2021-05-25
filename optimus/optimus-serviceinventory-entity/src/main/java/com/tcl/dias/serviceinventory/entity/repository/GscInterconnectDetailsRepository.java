package com.tcl.dias.serviceinventory.entity.repository;

import com.tcl.dias.serviceinventory.entity.entities.GscInterconnectDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * This file contains the repository class for GscInterconnectDetailsRepository entity
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface GscInterconnectDetailsRepository extends JpaRepository<GscInterconnectDetails, Integer> {

    List<GscInterconnectDetails> findByOrgNo(Integer orgId);

    @Query(value = "select interconnect_name from gsc_interconnect_dtls where interconnect_id = :interconnectId", nativeQuery = true)
    String getInterconnectName(@Param("interconnectId") String interconnectId);
    
    /**
	 * Query to get all interconnect details based on org id
	 * @param orgId
	 * @param page
	 * @param size
	 * @return
	 */
	@Query(value = "select * from gsc_interconnect_dtls where org_no =  :orgId limit :page, :size", nativeQuery = true)
	List<Map<String, Object>> getInterconnectDetailsByOrgId(@Param("orgId") Integer orgId,@Param("page") Integer page,@Param("size") Integer size);
	
	/**
	 * Query to get count of interconnect details
	 *
	 * @param orgId
	 * @return
	 */
	@Query(value = "select count(*) as count from gsc_interconnect_dtls where org_no =  :orgId", nativeQuery = true)
	Integer getCountOfInterconnectDetails(@Param("orgId") Integer orgId);

}
