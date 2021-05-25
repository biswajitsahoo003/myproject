package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;

/**
 * 
 * Repository class for ServicehandoverAudit - for Auditing entries like Account,Order,Invoice 
 * 
 *
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ServicehandoverAuditRepository extends JpaRepository<ServicehandoverAudit, Integer>{	
	
	@Query(value="select max(id) from service_handover_audit",nativeQuery=true)
	Integer findMaxId();
	
	@Query(value="SELECT * FROM service_handover_audit where geneva_group_id= :groupId",nativeQuery=true)
	ServicehandoverAudit findByGenevaGrpId(@Param("groupId") String groupID);

	ServicehandoverAudit findByOrderIdAndId(String OrderId, Integer id);
	
	@Query(value = "select * from service_handover_audit where order_id=:OrderId and request_type=:reqType  order by id desc limit 1",nativeQuery=true)
	ServicehandoverAudit findByRequestTypeAndOrderId(@Param("reqType") String reqType,@Param("OrderId") String OrderId);
	
	ServicehandoverAudit findByRequestTypeAndId(String reqType, Integer id);
	
	//ServicehandoverAudit findByRequestTypeAndServiceId(String reqType, String id);
	
	List<ServicehandoverAudit> findByGenevaGrpIdAndStatus(String groupID,String status);
	
	@Query(value = "select orderId from service_handover_audit where service_id=:serviceId and status=:status order by id desc limit 1",nativeQuery=true)
	String findByServiceIdAndStatus(String serviceId,String status);
		
	List<ServicehandoverAudit> findByCloudCode(String cloudCode);

	/**
	 * Native Query to fetch failure count for invoice
	 * 
	 * @param reqType
	 * @param OrderId
	 * @return
	 */
	@Query(value = "select count(status) as status_failure from service_handover_audit where order_id=:OrderId and request_type=:reqType and status='FAILURE'", nativeQuery = true)
	Integer findByRequestTypeAndOrderIdAndStatus(@Param("reqType") String reqType,@Param("OrderId") String OrderId);
	
	@Query(value = "select count(status) as status_failure from service_handover_audit where order_id=:OrderId and request_type=:reqType and service_type=:serviceType and status='FAILURE'", nativeQuery = true)
	Integer findByRequestTypeAndOrderIdAndStatusAndServiceType(@Param("reqType") String reqType,@Param("OrderId") String OrderId,@Param("serviceType") String serviceType);
	
	@Query(value = "select * from service_handover_audit where service_id=:serviceId and request_type=:reqType  order by id desc limit 1",nativeQuery=true)
	ServicehandoverAudit findByRequestTypeAndServiceIdForInvoice(@Param("reqType") String reqType,@Param("serviceId") String serviceId);
	
	@Query(value = "select * from service_handover_audit where order_id=:OrderId and service_type=:serviceType and request_type=:reqType order by id desc limit 1",nativeQuery=true)
	ServicehandoverAudit findByOrderId(@Param("OrderId")String OrderId,@Param("serviceType")String serviceType,@Param("reqType")String reqType);
	
	@Query(value = "select * from service_handover_audit  where service_id=:serviceId and request_type='ACCOUNT CREATION' and service_type in ('IAS','CPE','GVPN','NPL Intracity','NPLC') order by 1 desc limit 1",nativeQuery = true)
	ServicehandoverAudit findByServiceId(@Param("serviceId") String serviceId);
	
	@Query(value = "select * from service_handover_audit  where geneva_group_id=:groupID and status='SUCCESS'",nativeQuery = true)
	ServicehandoverAudit findByGrpId(@Param("groupID") String groupID);
	
	ServicehandoverAudit findBySourceProdSeq(String sourceProdSeq);
	
	ServicehandoverAudit findFirstBySourceProdSeq(String sourceProdSeq);
	
	@Query(value = "select * from service_handover_audit where order_id=:OrderId and request_type=:reqType and service_type=:serviceType order by id desc limit 1",nativeQuery=true)
	ServicehandoverAudit findByRequestTypeAndOrderIdAndServiceType(@Param("reqType") String reqType,@Param("OrderId") String OrderId,@Param("serviceType") String serviceType);

	@Query(value = "select * from service_handover_audit where service_id=:serviceId and request_type=:reqType and service_type=:serviceType order by id desc limit 1",nativeQuery=true)
	ServicehandoverAudit findByRequestTypeAndServiceIdAndServiceType(@Param("reqType") String reqType,@Param("serviceId") String serviceId,@Param("serviceType") String serviceType);
	
	@Query(value = "select count(status) as status_failure from service_handover_audit where service_id=:serviceId and request_type=:reqType and service_type=:serviceType and status='FAILURE'", nativeQuery = true)
	Integer findByRequestTypeAndServiceIdAndStatusAndServiceType(@Param("reqType") String reqType,@Param("serviceId") String serviceId,@Param("serviceType") String serviceType);
	
	@Query(value = "select * from service_handover_audit  where service_id=:serviceId and request_type=:requestType and service_type=:serviceType and status='SUCCESS'",nativeQuery = true)
	List<ServicehandoverAudit> findByServiceIdAndServiceType(@Param("serviceId") Integer serviceId ,@Param("requestType") String requestType,@Param("serviceType") String serviceType);
	
	@Query(value = "select * from service_handover_audit where service_id=:serviceId and request_type=:reqType order by id desc limit 1",nativeQuery=true)
	ServicehandoverAudit findByRequestTypeAndServiceCode(String reqType,String serviceId);

	List<ServicehandoverAudit> findByRequestTypeAndServiceCodeAndStatus(String prdComm, String serviceCode, String status);
	
	@Query(value = "select * from service_handover_audit where service_id=:serviceId and request_type=:reqType order by id desc limit 1",nativeQuery=true)
	ServicehandoverAudit findByRequestTypeAndServiceId(@Param("reqType") String reqType,@Param("serviceId") String serviceId);
	
	
}
