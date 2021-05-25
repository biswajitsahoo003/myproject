package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;

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

	@Query(value = "SELECT * FROM sc_component where sc_service_detail_id=:serviceId and component_type_id=:componentTypeId and site_type=:siteType", nativeQuery = true)
	Map<String, Object> findByScServiceDetail_idAndMstComponentType_id(@Param("serviceId") Integer serviceId,
			@Param("componentTypeId") Integer componentTypeId,@Param("siteType") String siteType);
	
	
	ScComponent findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(Integer serviceId,String componentName,String siteType);

	List<ScComponent> findByScServiceDetailIdAndComponentNameAndSiteTypeIn(Integer scServiceDetailId, String componentName, List<String> siteTypes);
	
	@Query(value = "select id from sc_component where sc_service_detail_id=:scServiceDetailId", nativeQuery = true)
	List<Integer> findComponentIdScServiceDetailId(Integer scServiceDetailId);
	
	List<ScComponent> findByScServiceDetailId(Integer scServiceDetailId);
	
	List<ScComponent> findByScServiceDetailIdOrderByIdDesc(Integer scServiceDetailId);

	ScComponent findFirstByUuidOrderByIdDesc(String uuid);

	List<ScComponent> findByScServiceDetailIdAndSiteType(Integer serviceId, String siteType);
	
	List<ScComponent> findByScServiceDetailIdAndComponentName(Integer serviceId,String componentName);
	
	@Query(value = "select site_type from sc_component where sc_service_detail_id=:scServiceDetailId", nativeQuery = true)
	List<String> getSiteTypeByScServiceDetailId(Integer scServiceDetailId);
	/*
	 * by dushyanth 
	 */
	//List<ScSolutionComponent> findBySolutionServiceDetailId(Integer solutionId);
	
	
	
	//@Query(value = "SELECT sc.id AS `id`,sc.component_name AS `component_name`,sca.attribute_name AS `attribute_name`,sca.attribute_value AS `attribute_value` FROM sc_component sc LEFT JOIN sc_component_attributes sca ON sca.sc_component_id=sc.id AND sca.attribute_name=:attrName WHERE sc.sc_service_detail_id=:serviceId AND sc.component_name=:componentName", nativeQuery = true)
	//Map<String, Object> findByScServiceDetailIdAndComponentName(@Param("serviceId") Integer serviceId, @Param("componentName") String componentName, @Param("attrName") String attrName);
}

