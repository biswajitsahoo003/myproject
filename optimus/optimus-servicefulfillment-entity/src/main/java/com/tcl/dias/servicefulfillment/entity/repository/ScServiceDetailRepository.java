package com.tcl.dias.servicefulfillment.entity.repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;


import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;

/**
 * 
 * This file contains the ScServiceDetailRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ScServiceDetailRepository extends JpaRepository<ScServiceDetail, Integer>,PagingAndSortingRepository<ScServiceDetail, Integer>,JpaSpecificationExecutor<ScServiceDetail> {
	
	//@Query(value = "select sc.* from sc_service_detail sc, mst_status ms where sc.uuid=:serviceUuid and sc.status=ms.id and ms.code=:code and is_migrated_order='N' order by id desc limit 1", nativeQuery = true)
		
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByUuidAndMstStatus_code(String serviceUuid, String code);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByUuidAndScOrder_id(String serviceUuid,Integer scOrderId);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByUuidAndScOrderUuid(String serviceUuid,String scOrderId);
	
	@Lock(LockModeType.NONE)
	public List<ScServiceDetail> findByScOrderUuid(String scOrderId);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findFirstByErfOdrServiceIdOrderByIdDesc(Integer erfOdrServiceId);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByParentUuidAndMstStatus_code(String serviceUuid, String code);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByScOrderUuidAndUuidAndId(String orderCode,String serviceCode,Integer id);
	
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByScOrderUuidAndId(String orderCode,Integer id);

	
	public List<ScServiceDetail> findByScOrderUuidAndErfPrdCatalogProductName(String orderCode, String erfCatalogProductName);
	
	
	@Query(value = "select distinct sc_order_uuid from sc_service_detail where uuid=:uuid and erf_prd_catalog_product_name=:productName", nativeQuery = true)
	public List<String> findByUuidAndErfPrdCatalogProductName(@Param("uuid") String uuid, @Param("productName") String productName);
	
	
	@Query(value = "select * from sc_service_detail where sc_order_id=:scOrderId", nativeQuery = true)
	public List<ScServiceDetail> findByScOrderId(@Param("scOrderId") Integer scOrderId);
	
	@Query(value = "select * from sc_service_detail scd join sc_order sc on scd.sc_order_id=sc.id where sc.op_order_code=:scOrderCode and erf_prd_catalog_product_name=:erfCatalogProductName order by scd.id", nativeQuery = true)
	public List<ScServiceDetail> findByScOrderIdAndErfCatalogProductName(@Param("scOrderCode") String scOrderCode, @Param("erfCatalogProductName") String erfCatalogProductName);

	@Query(value = "select * from sc_service_detail where id=:scServiceId", nativeQuery = true)
	public Map<String, Object> findByScServiceId(@Param("scServiceId") Integer scServiceId);
	
	@Query(value = "SELECT sc.erf_prd_catalog_product_name as productName,sco.order_type as orderType,count(sco.order_type) as count FROM sc_service_detail sc, mst_status ms, sc_order sco where sc.status=ms.id and ms.code in ('INPROGRESS','HOLD','CANCELLED','ACTIVE','DEFERRED-DELIVERY','RESOURCE-RELEASED-INITIATED','RESOURCE-RELEASED','CANCELLATION-INPROGRESS','JEOPARDY-INITIATED') and sc.sc_order_id = sco.id and sco.is_migrated_order='N' GROUP BY sc.erf_prd_catalog_product_name, sco.order_type HAVING count >= 1 order by sc.id asc;", nativeQuery = true)
	List<Map<String, String>> groupByProductAndType();

	@Query(value = "SELECT distinct(sc.sc_order_id) as scOrderId, sc.id as serviceId, sc.uuid as serviceCode, n.op_order_code as orderCode, sc.erf_prd_catalog_product_name, n.order_type as orderType, n.erf_cust_customer_name as customerName FROM sc_order n, sc_service_detail sc where sc.status=4 and n.id=sc.sc_order_id and sc.erf_prd_catalog_product_name=:productName and n.order_type=:orderType", nativeQuery = true)
	List<Map<String, String>> 	groupByProductNameAndOrderType(String productName, String orderType);
	
	List<ScServiceDetail> findByIdAndMstStatus_code(Integer serviceId,String status);
	
	List<ScServiceDetail> findByMstStatus_codeOrderByIdDesc(String status);
	
	public ScServiceDetail findByUuidAndMstStatus_codeOrderByIdDesc(String serviceUuid, String code);
	
	@Query(value = "select id from sc_service_detail where uuid=:serviceUuid and status=:status order by id desc limit 1", nativeQuery = true)
	public Integer findByUuidAndStatus(String serviceUuid, Integer status);
	
	@Query(value = "select * from sc_service_detail where uuid=:serviceUuid and is_migrated_order='N' order by id desc limit 1", nativeQuery = true)
	public ScServiceDetail findFirstByUuidOrderByIdDesc(String serviceUuid);

	Page<ScServiceDetail> findByMstStatus_codeOrderByIdDesc(String status,Pageable pageable);

	@Query(value = "select * from sc_service_detail where local_it_contact_name like (%:input%)", nativeQuery = true)
	List<ScServiceDetail> findByOrdersWithLocalContactName(String input);

	@Query(value = "select scd.lastmile_scenario as LMScenarioType, count(scd.id) as Count from  " +
			"sc_service_detail scd where scd.is_migrated_order='N' and scd.status in (1,4,5,6,10,8,14,15,16,17,18) group by scd.lastmile_scenario;", nativeQuery = true)
	List<Map<String, String>> groupbytLMScenarioTypeCount();

    List<ScServiceDetail> findByIdInOrderByIdDesc(List<Integer> integer);
    
    public List<ScServiceDetail> findLastByUuidOrderByIdDesc(String serviceUuid);

	public Collection<ScServiceDetail> findAllByIdIn(List<Integer> serviceDetailId);

	public List<ScServiceDetail> findByTpsServiceIdOrUuid(String tpsServiceId, String uuid);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findFirstByUuidAndMstStatus_codeOrderByIdDesc(String serviceUuid, String code);


	@Query(value = "SELECT count(sc.id) as COUNT FROM sc_service_detail sc, mst_status ms where sc.status=ms.id and sc.is_migrated_order='N' and sc.is_active='Y'and ms.code in ('INPROGRESS','HOLD','CANCELLED','ACTIVE','INACTIVE','AMENDED','DEFERRED-DELIVERY','RESOURCE-RELEASED-INITIATED','RESOURCE-RELEASED','CANCELLATION-INPROGRESS','JEOPARDY-INITIATED');", nativeQuery = true)
	List<Map<String, String>> groupbyActiveServiceRecords();

	@Query(value = "SELECT count(sc.id) as COUNT FROM sc_service_detail sc  where sc.is_migrated_order='N' and sc.is_active='Y'and sc.service_config_status ='ACTIVE';", nativeQuery = true)
	public Map<String, String> getAllActiveRecords();

    ScServiceDetail findFirstByUuidAndScOrderUuidIsNotNullOrderByIdDesc(String serviceId);

	ScServiceDetail findFirstByScOrderUuidOrderByIdDesc(String orderCode);

	List<ScServiceDetail> findByMstStatus_codeIn(List<String> statuses);

	List<ScServiceDetail> findByMstStatus_codeInAndIsMigratedOrder(List<String> asList, String migrated);
	
	@Query(value = "SELECT * FROM sc_service_detail  where is_migrated_order='N' and is_active='Y'and service_config_status ='ACTIVE';", nativeQuery = true)
	List<ScServiceDetail> getServiceConfiguredList();

	List<ScServiceDetail> findByMstStatus_codeAndIsMigratedOrder(String active, String n);

	List<ScServiceDetail> findByMstStatus_codeInAndIsMigratedOrderAndUuidIn(List<String> asList, String migrated, List<String> serviceCodes);
	
	List<ScServiceDetail> findByMstStatus_codeInAndIsMigratedOrderAndParentUuidIn(List<String> asList, String migrated, List<String> serviceCodes);
	
	List<ScServiceDetail> findByMstStatus_codeInAndIsMigratedOrderAndTpsServiceIdIn(List<String> asList, String migrated, List<String> serviceCodes);
	
	ScServiceDetail findByMstStatus_codeAndIsMigratedOrderAndTpsServiceId(String status, String migrated, String serviceCode);
	
	@Query(value = "select sc_order_uuid as scOrderCode from sc_service_detail where uuid=:serviceUuid and is_migrated_order='N' order by id desc", nativeQuery = true)
	public List<Map<String,String>> findByUuidAndOrderByIdDesc(String serviceUuid);
	
	@Modifying
	@Query(value = "update sc_service_detail ssd SET ssd.status = :status WHERE ssd.id =:serviceDetailId", nativeQuery = true)
	@Transactional
	void updateStatus(@Param ("status")Integer status,@Param("serviceDetailId") Integer serviceDetailId);

	@Modifying
	@Query(value = "update sc_service_detail ssd SET ssd.status = :status,ssd.is_delivered=:deliveredStatus,delivered_date=:deliveredDate WHERE ssd.id =:serviceDetailId", nativeQuery = true)
	@Transactional
	void updateActiveDeliveredStatus(@Param ("status")Integer status,@Param ("deliveredStatus")String deliveredStatus,@Param ("deliveredDate")Timestamp deliveredDate,@Param("serviceDetailId") Integer serviceDetailId);
	
	@Query(value = "select * from sc_service_detail where id=(select service_link_id from sc_service_detail ssd WHERE ssd.uuid=:serviceUuid and ssd.status=4)", nativeQuery = true)
	public ScServiceDetail findFirstByServiceLinkIdOrderByIdDesc(String serviceUuid);

	List<ScServiceDetail> findByUuidAndMstStatus_codeAndIsMigratedOrder(String uuid,String status, String migrated);

	ScServiceDetail findByIdAndUuidAndScOrderUuid(Integer serviceId, String serviceCode, String orderCode);
	
	@Query(value = "select count(distinct(id)) from sc_service_detail where is_migrated_order = 'N'  and billing_status = 'ACTIVE';",nativeQuery=true)
	Integer findTotalBilledCircuits();
	
	@Query(value = "select ssd.targeted_delivery_date as targetedDeliveryDate from sc_solution_components ssc join sc_service_detail ssd on ssc.sc_service_detail_id=ssd.id where ssc.parent_sc_service_detail_id=:serviceId", nativeQuery = true)
	public List<Map<String,Timestamp>> findByIdAndTargetedDeliveryDate(Integer serviceId);
	
	@Query(value = "select ssd.erf_prd_catalog_product_name as productName,ssc.component_group as componentGroup from sc_service_detail ssd join sc_solution_components ssc on ssd.id=ssc.sc_service_detail_id where ssd.id=:serviceId", nativeQuery = true)
	public Map<String,String> findByProductNameAndComponent(Integer serviceId);
	
	@Query(value = "select * from sc_service_detail sc where sc.uuid=:serviceCode order by id desc limit 1",nativeQuery=true)
	ScServiceDetail findLatestServiceDetailbyServiceCode(String serviceCode);
	
	@Query(value = "select * from sc_service_detail where sc_order_id=:scOrderId and erf_prd_catalog_product_name=:productName", nativeQuery = true)
	public List<ScServiceDetail> findByScOrderIdAndProductName(@Param("scOrderId") Integer scOrderId,@Param("productName") String productName);

	@Query(value = "SELECT distinct(destination_country) from sc_service_detail where sc_order_id=:scOrderId and erf_prd_catalog_product_name=:productName", nativeQuery = true)
	public List<String> findDistinctDestinationCountryByScOrder_IdAndErfPrdCatalogProductName(@Param("scOrderId") Integer scOrderId,@Param("productName") String productName);
	
	@Modifying
	@Query(value = "update sc_service_detail ssd SET ssd.service_aceptance_status=:serviceAcceptanceStatus,service_aceptance_date=:serviceAceptanceDate WHERE ssd.id in(:serviceDetailIds)", nativeQuery = true)
	@Transactional
	void updateServiceAcceptanceStatus(@Param ("serviceAcceptanceStatus")String serviceAcceptanceStatus,@Param ("serviceAceptanceDate")Timestamp serviceAceptanceDate,@Param("serviceDetailIds") List<Integer> serviceDetailIds);
	
	@Modifying
	@Query(value = "update sc_service_detail ssd SET ssd.assurance_completion_status=:assuranceCompletionStatus,assurance_completion_date=:assuranceCompletionDate WHERE ssd.id in(:serviceDetailIds)", nativeQuery = true)
	@Transactional
	void updateServiceAssuranceStatus(@Param ("assuranceCompletionStatus")String assuranceCompletionStatus,@Param ("assuranceCompletionDate")Timestamp assuranceCompletionDate,@Param("serviceDetailIds") List<Integer> serviceDetailIds);

	@Query(value = "select * from sc_service_detail where status=4 and is_migrated_order='N' and targeted_delivery_date<CURRENT_DATE+1", nativeQuery = true)
	public List<ScServiceDetail> findTargetedDateLessThanCurrentDate();

	public ScServiceDetail findByUuidAndScOrder_OpOrderCode(String serviceCode, String opOrderCode);
	
	@Query(value = "select count(distinct(id)) from sc_service_detail where is_migrated_order = 'N'  and is_delivered = 'ACTIVE'",nativeQuery=true)
	Integer findTotalDeliveredCircuits();

	@Query(value = "select * from sc_service_detail where uuid = :serviceCode and sc_order_uuid = :opOrderCode and is_migrated_order = 'N' and status !=11 order by id desc limit 1", nativeQuery = true)
	public ScServiceDetail findByUuidAndScOrder_OpOrderCodeAndIsMigratedOrder(String serviceCode, String opOrderCode);
	
	List<ScServiceDetail> findAllByScOrderUuidIn(List<String> scOrderList);
	
	ScServiceDetail findByScOrderAndErfPrdCatalogOfferingName(ScOrder scOrder, String erfPrdCatalogOfferingName);
	
	List<ScServiceDetail> findByIdInAndMstStatus_code(List<Integer> serviceIds,String status);
	
	public ScServiceDetail findFirstByScOrderUuidAndErfPrdCatalogProductNameOrderByIdDesc(String orderCode,String productName);
	
	public ScServiceDetail findFirstByUuidAndMstStatus_codeAndIsMigratedOrder(String uuid,String status, String migrated);
	
	public ScServiceDetail findFirstByUuidAndIsMigratedOrder(String uuid, String migrated);

	public List<ScServiceDetail> findByMasterVrfServiceId(Integer masterVrfServiceId);
	
	public ScServiceDetail findFirstByUuidAndMstStatus_codeAndIsMigratedOrderAndIsActive(String uuid,String status, String migrated, String isActive);
	
	public ScServiceDetail findFirstByUuidAndIsMigratedOrderAndIsActiveOrderByIdDesc(String uuid, String migrated, String isActive);
	
	@Query(value = "select erf_prd_catalog_offering_name from sc_service_detail sc where sc.parent_id=:serviceId group by sc.erf_prd_catalog_offering_name", nativeQuery = true)
	public List<String> findProductNamesByParentId(String serviceId);

	@Query(value = "select * from sc_service_detail sc where sc.parent_id=:serviceId and sc.erf_prd_catalog_offering_name=:productName", nativeQuery = true)
	public List<ScServiceDetail> findByProductNameAndParentId(String productName, String serviceId);

	@Query(value = "select cust_org_no from sc_service_detail where id=:serviceId", nativeQuery = true)
	public String findOrgIdbyServiceId(Integer serviceId);
	
	public List<ScServiceDetail> findByParentId(Integer parentServiceId);

	public List<ScServiceDetail> findByUuidOrderByIdDesc(String uuid);
	
	List<ScServiceDetail> findByScOrderUuidAndErfPrdCatalogProductNameInAndDestinationCountryNotAndMstStatus_code(String orderCode,List<String> productNameList,String destinationCountry,String status);

	@Query(value = "SELECT distinct(id) FROM sc_service_detail where id<:serviceId and uuid=:serviceCode order by id desc limit 1", nativeQuery = true)
	Integer findPrevServiceDetailIdsByServiceCodeAndServiceId(String serviceCode, Integer serviceId);
	
	@Query(value = "select * from sc_service_detail where is_delivered = 'ACTIVE' and delivered_date>='2021-01-01' and status=5 and is_migrated_order='N'", nativeQuery = true)
	public List<ScServiceDetail> findByDeliveryDate();

	@Query(value = "select sc.* from sc_service_detail sc, sc_service_attributes ssa where sc.id = ssa.sc_service_detail_id and sc.parent_id=:serviceId and ssa.attribute_name='serviceType' AND ssa.attribute_value=:productType", nativeQuery = true)
	public List<ScServiceDetail> findByServiceTypeAndParentId(String productType, String serviceId);
	
	@Query(value = "select id from sc_service_detail where sc_order_id=:scOrderId and uuid!=:uuid", nativeQuery = true)
	public List<Integer> getIdByScOrderIdAndUuid(@Param("scOrderId") Integer scOrderId,@Param("uuid") String uuid);

	@Query(value = "select id from sc_service_detail where sc_order_uuid=:scOrderUuid and uuid!=:scOrderUuid", nativeQuery = true)
	public List<Integer> getIdByScOrderUuidAndUuid(@Param("scOrderUuid") String scOrderUuid);
	
	@Query(value = "select id from sc_service_detail where sc_order_uuid=:scOrderUuid", nativeQuery = true)
	public List<Integer> getIdByScOrderUuid(@Param("scOrderUuid") String scOrderUuid);
	
	@Query(value = "select * from sc_service_detail where sc_order_uuid=:scOrderUuid and uuid!=:scOrderUuid", nativeQuery = true)
	List<ScServiceDetail> getServiceDetailsByScOrderUuidAndUuid(@Param("scOrderUuid") String scOrderUuid);
	
	@Query(value = "select * from sc_service_detail where sc_order_uuid=:scOrderUuid and uuid not in(:uuIds) and status=4 and is_migrated_order='N'", nativeQuery = true)
	List<ScServiceDetail> getServiceDetailsByUuidAndScOrderUuidAndUuids(@Param("scOrderUuid") String scOrderUuid,@Param("uuIds") List<String> uuIds);
	
	public List<ScServiceDetail> findByTpsServiceIdAndMstStatus_codeOrUuidAndMstStatus_code(String tpsServiceId, String tpsStatus, String uuid,String status);
	
    public List<ScServiceDetail> findByScOrder_idAndUuidNot(@Param("scOrderId") Integer scOrderId,@Param("uuid") String uuid);
    
    @Query(value = "select id from sc_service_detail where sc_order_id=:scOrderId and uuid in (:uuids)", nativeQuery = true)
    public List<ScServiceDetail> findByScOrder_idAndUuidIn(@Param("scOrderId") Integer scOrderId,@Param("uuids") List<String> uuids);

	
}
