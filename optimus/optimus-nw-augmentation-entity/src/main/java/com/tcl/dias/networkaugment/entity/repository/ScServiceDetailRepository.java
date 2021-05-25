package com.tcl.dias.networkaugment.entity.repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;


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

import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;

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
	public ScServiceDetail findFirstByErfOdrServiceIdOrderByIdDesc(Integer erfOdrServiceId);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByParentUuidAndMstStatus_code(String serviceUuid, String code);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findByScOrderUuidAndUuidAndId(String orderCode,String serviceCode,Integer id);

	@Query(value = "select * from sc_service_detail where sc_order_id=:scOrderId", nativeQuery = true)
	public List<ScServiceDetail> findByScOrderId(@Param("scOrderId") Integer scOrderId);

	@Query(value = "select * from sc_service_detail where id=:scServiceId", nativeQuery = true)
	public Map<String, Object> findByScServiceId(@Param("scServiceId") Integer scServiceId);
	
	@Query(value = "SELECT sc.erf_prd_catalog_product_name as productName,sco.order_type as orderType,count(sco.order_type) as count FROM sc_service_detail sc, mst_status ms, sc_order sco where sc.status=ms.id and ms.code in ('INPROGRESS','HOLD','CANCELLED','ACTIVE') and sc.sc_order_id = sco.id and sco.is_migrated_order='N' GROUP BY sc.erf_prd_catalog_product_name, sco.order_type HAVING count >= 1 order by sc.id asc;", nativeQuery = true)
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
			"sc_service_detail scd where scd.is_migrated_order='N' and scd.status in (1,4,5,6,10,8) group by scd.lastmile_scenario;", nativeQuery = true)
	List<Map<String, String>> groupbytLMScenarioTypeCount();

    List<ScServiceDetail> findByIdInOrderByIdDesc(List<Integer> integer);
    
    public List<ScServiceDetail> findLastByUuidOrderByIdDesc(String serviceUuid);

	public Collection<ScServiceDetail> findAllByIdIn(List<Integer> serviceDetailId);

	public List<ScServiceDetail> findByTpsServiceIdOrUuid(String tpsServiceId, String uuid);
	
	@Lock(LockModeType.NONE)
	public ScServiceDetail findFirstByUuidAndMstStatus_codeOrderByIdDesc(String serviceUuid, String code);

	@Query(value = "SELECT count(sc.id) as COUNT FROM sc_service_detail sc, mst_status ms where sc.status=ms.id and sc.is_migrated_order='N' and sc.is_active='Y'and ms.code in ('INPROGRESS','HOLD','CANCELLED','ACTIVE','INACTIVE');", nativeQuery = true)
	List<Map<String, String>> groupbyActiveServiceRecords();

	@Query(value = "SELECT count(sc.id) as COUNT FROM sc_service_detail sc  where sc.is_migrated_order='N' and sc.is_active='Y'and sc.service_config_status ='ACTIVE';", nativeQuery = true)
	public Map<String, String> getAllActiveRecords();

    ScServiceDetail findFirstByUuidAndScOrderUuidIsNotNull(String serviceId);

	ScServiceDetail findFirstByScOrderUuidOrderByIdDesc(String orderCode);

	List<ScServiceDetail> findByMstStatus_codeIn(List<String> statuses);

	List<ScServiceDetail> findByMstStatus_codeInAndIsMigratedOrder(List<String> asList, String migrated);
	
	@Query(value = "SELECT * FROM sc_service_detail  where is_migrated_order='N' and is_active='Y'and service_config_status ='ACTIVE';", nativeQuery = true)
	List<ScServiceDetail> getServiceConfiguredList();

	List<ScServiceDetail> findByMstStatus_codeAndIsMigratedOrder(String active, String n);

	List<ScServiceDetail> findByMstStatus_codeInAndIsMigratedOrderAndUuidIn(List<String> asList, String migrated, List<String> serviceCodes);
	
	@Query(value = "select sc_order_uuid as scOrderCode from sc_service_detail where uuid=:serviceUuid and is_migrated_order='N' order by id desc", nativeQuery = true)
	public List<Map<String,String>> findByUuidOrderByIdDesc(String serviceUuid);
	
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
}
