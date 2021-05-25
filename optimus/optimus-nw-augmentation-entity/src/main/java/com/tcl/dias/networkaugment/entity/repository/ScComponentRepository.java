package com.tcl.dias.networkaugment.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.networkaugment.entity.entities.ScComponent;

/**
 * 
 * This file contains the ScComponent.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScComponentRepository extends JpaRepository<ScComponent, Integer> {

	@Query(value = "SELECT * FROM sc_component where sc_service_detail_id==:serviceId and component_type_id=:componentTypeId and site_type=:siteType", nativeQuery = true)
	Map<String, Object> findByScServiceDetail_idAndMstComponentType_id(@Param("serviceId") Integer serviceId,
			@Param("componentTypeId") Integer componentTypeId,@Param("siteType") String siteType);
	
	
	ScComponent findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(Integer serviceId,String componentName,String siteType);

	List<ScComponent> findByScServiceDetailIdAndComponentNameAndSiteTypeIn(Integer scServiceDetailId, String componentName, List<String> siteTypes);
}
