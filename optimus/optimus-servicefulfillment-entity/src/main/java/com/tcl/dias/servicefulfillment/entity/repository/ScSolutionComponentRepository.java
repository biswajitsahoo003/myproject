package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;

/**
 * 
 * This file contains the ScSolutionComponent.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface ScSolutionComponentRepository extends JpaRepository<ScSolutionComponent, Integer> {

	List<ScSolutionComponent> findAllByOrderCodeAndIsActive(String orderCode, String isActive);
	
	List<ScSolutionComponent> findAllByOrderCodeAndComponentGroupAndIsActive(String orderCode,String componentGroup, String isActive);
	
	List<ScSolutionComponent> findAllByScServiceDetail3_idAndComponentGroupAndIsActive(Integer solutionId,String componentGroup, String isActive);
	
	List<ScSolutionComponent> findAllBySolutionCodeAndIsActive(String solutionCode, String isActive);
	
	@Query(value = "select distinct order_code from sc_solution_components where solution_code=:solutionCode and is_active=:isActive", nativeQuery = true)
	List<String> findDistinctOrderBySolutionCodeAndIsActive(@Param("solutionCode") String solutionCode, @Param("isActive") String isActive);
	
	@Query(value = "select ssc.sc_service_detail_id as overlayIds,group_concat(sscc.sc_service_detail_id) as underlayIds from sc_solution_components ssc join sc_solution_components sscc on ssc.sc_service_detail_id=sscc.parent_sc_service_detail_id where sscc.order_code=:orderCode and sscc.component_group in(:componentGroups) and sscc.is_active=:isActive and ssc.sc_service_detail_id=:serviceId", nativeQuery = true)
	List<Map<String,Integer>> findScServiceDetailByComponentType(String orderCode, List<String> componentGroups,String isActive, Integer serviceId);
	

	@Query(value = "select ssc.sc_service_detail_id as overlayIds,group_concat(sscc.sc_service_detail_id) as underlayIds from sc_solution_components ssc join sc_solution_components sscc on ssc.sc_service_detail_id=sscc.parent_sc_service_detail_id where sscc.order_code=:orderCode and sscc.component_group in(:componentGroups) and sscc.is_active=:isActive group by ssc.sc_service_detail_id", nativeQuery = true)
	List<Map<String,Integer>> findScServiceDetailByComponentType(String orderCode, List<String> componentGroups,String isActive);
	
	@Query(value = "select count(distinct(cpe_component_id)) from sc_solution_components where parent_sc_service_detail_id=:overlayId and cpe_component_id is not null", nativeQuery = true)
	int findUnderlayBasedOnOverlayAndCpe(Integer overlayId);
	
	@Query(value = "select * FROM sc_solution_components where parent_sc_service_detail_id= (select parent_sc_service_detail_id from sc_solution_components where sc_service_detail_id=:serviceId and is_active=:isActive)", nativeQuery = true)
	List<ScSolutionComponent> findAllByServiceIdAndIsActive(Integer serviceId, String isActive);
	
	ScSolutionComponent findByScServiceDetail1_idAndScServiceDetail2_idAndIsActive(Integer serviceId, Integer overlayId,String isActive);
	
	ScSolutionComponent findByScServiceDetail1_idAndOrderCodeAndIsActive(Integer serviceId, String orderCode,String isActive);
	
	List<ScSolutionComponent> findByScServiceDetail1_idInAndOrderCodeAndIsActive(List<Integer> serviceId, String orderCode,String isActive);
	
	List<ScSolutionComponent> findByScServiceDetail3_idAndScServiceDetail2_idOrScServiceDetail1_idAndComponentGroupInAndIsActive(Integer solutionId, Integer overlayId,Integer serviceId,List<String> componentGroups,String isActive);
	
	List<ScSolutionComponent> findByScServiceDetail3_idAndScServiceDetail2_idAndComponentGroupAndIsActive(Integer solutionId, Integer serviceId,String componentGroup,String isActive);
	
	ScSolutionComponent findByScServiceDetail3_idAndScServiceDetail1_idAndIsActive(Integer solutionId, Integer overlayId,String isActive);
	
	@Query(value = "select sc_service_detail_id from sc_solution_components where solution_service_detail_id=:solutionId and (sc_service_detail_id=:serviceId or parent_sc_service_detail_id=:overlayId) and is_active=:isActive", nativeQuery = true)
	List<Integer> getServiceDetailsBySolutionIdAndOverlayId(Integer solutionId, Integer serviceId,Integer overlayId,String isActive);
	
	@Query(value = "select sc_service_detail_id from sc_solution_components where parent_sc_service_detail_id=(select parent_sc_service_detail_id from sc_solution_components where order_code=:orderCode and sc_service_detail_id=:serviceId and is_active=:isActive)", nativeQuery = true)
	List<Integer> getServiceDetailsByOrderCodeAndUnderlayId(String orderCode, Integer serviceId,String isActive);
	
	@Query(value = "select sc_service_detail_id from sc_solution_components where parent_sc_service_detail_id=(select parent_sc_service_detail_id from sc_solution_components where order_code=:orderCode and sc_service_detail_id=:serviceId and is_active=:isActive) and sc_service_detail_id!=:serviceId", nativeQuery = true)
	List<Integer> getServiceDetailsByOrderCodeAndUnderlayIdNot(String orderCode, Integer serviceId,String isActive);
	
	@Query(value = "select solution_service_detail_id as solutionId,order_code as orderCode,solution_code as solutionCode FROM sc_solution_components where sc_service_detail_id=:serviceId and service_code=:serviceCode and is_active=:isActive", nativeQuery = true)
	List<Map<Object,Object>> getSolutionDetails(Integer serviceId, String serviceCode,String isActive);

	ScSolutionComponent findByScServiceDetail1(ScServiceDetail scServiceDetail);

	@Query(value = "SELECT * FROM sc_solution_components where parent_sc_service_detail_id=:overlayIdServiceId", nativeQuery = true)
	List<ScSolutionComponent> findByParentServiceId(Integer overlayIdServiceId);
	
	@Query(value = "SELECT sc_service_detail_id FROM sc_solution_components where parent_sc_service_detail_id=:overlayIdServiceId and is_active=:isActive", nativeQuery = true)
	List<Integer> findUnderlayServiceIdByParentServiceId(Integer overlayIdServiceId,String isActive);

	List<ScSolutionComponent> findByCpeComponentId(Integer cpeComponentId);
	
	@Query(value = "select service_code from sc_solution_components ssc join  sc_service_detail ssd on ssc.sc_service_detail_id=ssd.id where ssc.cpe_component_id=:cpeComponentId and ssc.order_code=:orderCode and ssc.sc_service_detail_id!=:serviceId and ssd.erf_prd_catalog_product_name not in(:productNameList) and ssd.destination_country!='India'", nativeQuery = true)
	List<String> getServiceCodeBasedOnCpeComponentId(Integer cpeComponentId,String orderCode,Integer serviceId,List<String> productNameList);
	
	@Query(value = "select sc_service_detail_id from sc_solution_components where solution_service_detail_id=:solutionId and component_group=:componentGroup and priority=:priority and is_active=:isActive", nativeQuery = true)
	List<Integer> findAllByScServiceDetail3_idAndComponentGroupAndPriorityAndIsActive(Integer solutionId,String componentGroup, String priority,String isActive);
	
	@Query(value = "select sc_service_detail_id from sc_solution_components where solution_service_detail_id=:solutionId and sc_service_detail_id!=:serviceId and component_group=:componentGroup and is_active=:isActive", nativeQuery = true)
	Integer findAllByScServiceDetail3_idAndComponentGroupAndServiceIdAndIsActive(Integer solutionId,Integer serviceId,String componentGroup,String isActive);
	
	@Query(value = "select sc_service_detail_id from sc_solution_components where order_code=:orderCode and component_group=:componentGroup and is_active=:isActive", nativeQuery = true)
	List<Integer> getServiceDetailIdByOrderCodeAndComponentGroupAndIsActive(String orderCode,String componentGroup,String isActive);
	
	@Query(value = "select sc_service_detail_id from sc_solution_components where order_code=:orderCode and component_group=:componentGroup and is_active=:isActive and sc_service_detail_id!=:serviceId", nativeQuery = true)
	List<Integer> findByOrderCodeAndComponentGroupAndIsActiveAndNotInServiceId(String orderCode,String componentGroup,String isActive, Integer serviceId);

	@Modifying
	@Query(value = "update sc_solution_components ssc SET ssc.o2c_triggered_status = :status WHERE ssc.solution_service_detail_id=:solutionServiceDetailId and ssc.sc_service_detail_id =:serviceDetailId", nativeQuery = true)
	@Transactional
	void updateStatus(@Param ("status")String status,@Param("solutionServiceDetailId") Integer solutionServiceDetailId,@Param("serviceDetailId") Integer serviceDetailId);
	
	List<ScSolutionComponent> findByScServiceDetail1_IdNotInAndCpeComponentId(List<Integer> scServiceIds,Integer cpeComponentId);
	
	@Query(value = "SELECT ssc.* FROM sc_solution_components ssc JOIN sc_service_detail ssd ON ssc.sc_service_detail_id=ssd.id WHERE ssc.solution_code=:solutionCode and ssc.is_active=:isActive ORDER BY ssd.rrfs_date asc", nativeQuery = true)
	List<ScSolutionComponent> findAllBySolutionCodeAndIsActiveOrderByRRFSDate(String solutionCode, String isActive);
	
	List<ScSolutionComponent> findByOrderCodeAndCpeComponentIdIsNotNull(String orderCode);
	
	List<ScSolutionComponent> findByOrderCodeAndComponentGroupAndIsActive(String orderCode,String componentGroup, String isActive);

	@Query(value = "select * FROM sc_solution_components where parent_sc_service_detail_id = :parentServiceDetailId and is_active=:isActive", nativeQuery = true)
	List<ScSolutionComponent> findDistinctOrdersAndIsActive(Integer parentServiceDetailId, String isActive);

	List<ScSolutionComponent> findByParentServiceCode(String serviceCode);

    ScSolutionComponent findByServiceCode(String serviceCode);
    
    @Query(value = "select sc_service_detail_id from sc_solution_components where order_code=:orderCode and is_active=:isActive and sc_service_detail_id!=:serviceId", nativeQuery = true)
	List<Integer> findByOrderCodeAndIsActiveAndNotInServiceId(String orderCode,String isActive, Integer serviceId);
}
