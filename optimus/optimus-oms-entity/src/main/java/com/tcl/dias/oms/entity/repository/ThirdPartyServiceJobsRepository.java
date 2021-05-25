package com.tcl.dias.oms.entity.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;

/**
 * JPA Repository class for all Third Party Services
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface ThirdPartyServiceJobsRepository extends JpaRepository<ThirdPartyServiceJob, Integer> {

	/**
	 * Find SFDC Service Status By Sequenece ID
	 *
	 * @param serviceStatus
	 * @return
	 */
	@Query(value = "select t.*  from thirdparty_service_jobs t where TRIM(t.service_status)=:serviceStatus and third_party_source='SFDC'  and is_active=1 and t.seq_num in (select min(seq_num) "
			+ "from thirdparty_service_jobs where t.ref_id=ref_id and service_status=:serviceStatus and third_party_source='SFDC' and is_active=1 order by created_time asc)  order by t.id asc", nativeQuery = true)
	List<ThirdPartyServiceJob> findSfdcByServiceStatusBySeq(@Param("serviceStatus") String serviceStatus);

	List<ThirdPartyServiceJob> findByServiceStatusInAndThirdPartySourceAndIsActive(List<String> serviceStatuses, String source,Byte isActive);
	
	List<ThirdPartyServiceJob> findByServiceStatusInAndThirdPartySource(List<String> serviceStatuses, String source);

	List<ThirdPartyServiceJob> findByServiceStatusAndThirdPartySource(String serviceStatuse, String source);

	List<ThirdPartyServiceJob> findByRefIdAndThirdPartySource(String refId, String source);

	List<ThirdPartyServiceJob> findByThirdPartySourceAndServiceTypeAndTpsId(String source,
			String serviceType, String tpsId);

	List<ThirdPartyServiceJob> findByRefIdAndServiceStatusAndThirdPartySource(String refId, String status,
			String source);
	
	List<ThirdPartyServiceJob> findByRefIdAndIsDroppedAndThirdPartySource(String refId, Byte isDropped,
			String source);

	List<ThirdPartyServiceJob> findByRefIdAndServiceStatusInAndThirdPartySource(String refId, List<String> statuses,
			String source);

	List<ThirdPartyServiceJob> findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(String serviceStatus,
			String refId, String serviceType, String source);

	List<ThirdPartyServiceJob> findByRefIdAndServiceTypeAndThirdPartySource(String refId, String serviceType,
			String source);

	List<ThirdPartyServiceJob> findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatus(String refId,
			String serviceType, String source, String Status);

	List<ThirdPartyServiceJob> findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
			String refId, String serviceType, String source, String Status);

	List<ThirdPartyServiceJob> findAllByRefIdAndServiceTypeInAndThirdPartySourceIn(String refId,
			List<String> serviceTypes, List<String> thirdPartySource);

	List<ThirdPartyServiceJob> findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(String refId, String status,
			String source);

	List<ThirdPartyServiceJob> findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdAsc(String refId, String status,
			String source);

	/**
	 * Update Service Status By Ref ID and Third Party Source
	 *
	 * @param refId
	 * @param serviceStatus
	 * @param source
	 * @return
	 */
	@Modifying
	@Query(value = "update thirdparty_service_jobs t set t.service_status=:serviceStatus  where t.ref_id= :refId and t.service_status='NEW' and third_party_source=:source", nativeQuery = true)
	int updateServiceStatusByRefIdAndThirdPartySource(@Param("refId") String refId,
			@Param("serviceStatus") String serviceStatus, @Param("source") String source);
	
	
	@Modifying
	@Transactional
	@Query(value = "update thirdparty_service_jobs t set t.service_status=:serviceStatus, t.response_payload=:comments  where t.ref_id= :refId and t.service_status not in ('SUCCESS') and (t.is_dropped!=1 or t.is_dropped is null) and third_party_source=:source", nativeQuery = true)
	int updateServiceStatusByRefIdAndThirdPartySourceAndServiceStatus(@Param("refId") String refId,
			@Param("serviceStatus") String serviceStatus, @Param("source") String source,@Param("comments") String comments);

	/**
	 * Update Service Status By Ref ID and Third Party Source and Service Type
	 *
	 * @param refId
	 * @param serviceStatus
	 * @param source
	 * @param serviceType
	 * @return
	 */
	@Modifying
	@Query(value = "update thirdparty_service_jobs t set t.service_status=:serviceStatus  where t.ref_id= :refId and third_party_source=:source and service_type = :serviceType", nativeQuery = true)
	int updateServiceStatusByRefIdAndThirdPartySourceAndServiceType(@Param("refId") String refId,
			@Param("serviceStatus") String serviceStatus, @Param("source") String source,
			@Param("serviceType") String serviceType);

	/**
	 * Find Enterprise Tiger services by status and sequence number
	 *
	 * @param serviceStatus
	 * @return
	 */
	@Query(value = "select t.* from thirdparty_service_jobs t where TRIM(t.service_status) = :serviceStatus and third_party_source = 'ENTERPRISE_TIGER_ORDER' " +
			"and is_active = 1 and t.seq_num in (select min(seq_num) from thirdparty_service_jobs where t.ref_id = ref_id " +
			"and service_status = :serviceStatus and third_party_source = 'ENTERPRISE_TIGER_ORDER' and is_active = 1 order by created_time asc) order by t.id asc", nativeQuery = true)
	List<ThirdPartyServiceJob> findEnterpriseTigerByServiceStatusBySeq(@Param("serviceStatus") String serviceStatus);

	/**
	 * Find Wholesale Tiger services by status and sequence number
	 *
	 * @param serviceStatus
	 * @return
	 */
	@Query(value = "select t.* from thirdparty_service_jobs t where TRIM(t.service_status) = :serviceStatus and third_party_source = 'WHOLESALE_TIGER_ORDER' " +
			"and is_active = 1 and t.seq_num in (select min(seq_num) from thirdparty_service_jobs where t.ref_id = ref_id " +
			"and service_status = :serviceStatus and third_party_source = 'WHOLESALE_TIGER_ORDER' and is_active = 1 order by created_time asc) order by t.id asc", nativeQuery = true)
	List<ThirdPartyServiceJob> findWholesaleTigerByServiceStatusBySeq(@Param("serviceStatus") String serviceStatus);

	/**
	 * Find CHANGE_OUTPULSE services by status and sequence number
	 *
	 * @param serviceStatus
	 * @return
	 */
	@Query(value = "select t.* from thirdparty_service_jobs t where TRIM(t.service_status) = :serviceStatus and third_party_source = 'CHANGE_OUTPULSE' " +
			"and is_active = 1 and t.seq_num in (select min(seq_num) from thirdparty_service_jobs where t.ref_id = ref_id " +
			"and service_status = :serviceStatus and third_party_source = 'CHANGE_OUTPULSE' and is_active = 1 order by created_time asc) order by t.id asc", nativeQuery = true)
	List<ThirdPartyServiceJob>  findChangeOutpulseByServiceStatusBySeq(@Param("serviceStatus") String serviceStatus);

	@Query(value = "select * from thirdparty_service_jobs where service_status  in (:serviceStatuses) and (retry_count is null or retry_count <= :retryCount ) and third_party_source = 'SFDC' order by seq_num asc", nativeQuery = true)
	List<ThirdPartyServiceJob> findByServiceStatusAndRetryCount(@Param("serviceStatuses") List<String> serviceStatuses,
			@Param("retryCount") Integer retryCount);
	
	@Query(value = "select * from thirdparty_service_jobs where service_status  in (:serviceStatuses) and (retry_count is null or retry_count <= :retryCount ) and third_party_source = 'SFDC' and is_active=1 order by seq_num asc", nativeQuery = true)
	List<ThirdPartyServiceJob> findByServiceStatusAndRetryCountAndIsActive(@Param("serviceStatuses") List<String> serviceStatuses,
			@Param("retryCount") Integer retryCount);

	@Modifying
	@Query(value = "update thirdparty_service_jobs t set t.service_status = 'NEW'  where t.ref_id= :refId and third_party_source = 'SFDC' and t.service_status in (:serviceStatuses) ", nativeQuery = true)
	int updateServiceStatusByRefId(@Param("refId") String refId,
			@Param("serviceStatuses") List<String> serviceStatuses);

	@Query(value = "select * from thirdparty_service_jobs where service_status  = :serviceStatus and third_party_source = 'SFDC' and updated_time <= :updatedTime", nativeQuery = true)
	List<ThirdPartyServiceJob> findByServiceStatusAndUpdatedTime(@Param("serviceStatus") String serviceStatus,
			@Param("updatedTime") Date updatedTime);
	
	List<ThirdPartyServiceJob> findAllByServiceStatusInAndServiceTypeInAndRefId(List<String> statusList,List<String> serviceType,String refId);
	
	List<ThirdPartyServiceJob> findByServiceRefIdAndServiceTypeAndRefIdAndThirdPartySourceAndServiceStatus(String serviceRefId, String serviceType, String refId, String source, String serviceStatus);
	
	List<ThirdPartyServiceJob> findByServiceRefIdAndServiceTypeAndRefIdAndThirdPartySource(String serviceRefId, String serviceType, String refId, String source);

	@Query(value = "select * from thirdparty_service_jobs where ref_id = :refId and service_status != 'SUCCESS' and third_party_source = 'SFDC'", nativeQuery = true)
	List<ThirdPartyServiceJob> findSfdcAllSuccessJobs(@Param("refId") String refId);

	@Query(value = "select * from thirdparty_service_jobs where ref_id = :refId and service_status = 'NEW' and third_party_source = 'ENTERPRISE_TIGER_ORDER'", nativeQuery = true)
	List<ThirdPartyServiceJob> findNewEnterpriseTigerJobs(@Param("refId") String refId);
	
	@Query(value = "select * from thirdparty_service_jobs where ref_id =?1 and third_party_source ='SFDC' and is_active =1 and service_status in ('FAILURE' ,'INPROGRESS') order by id asc",nativeQuery = true)
	List<ThirdPartyServiceJob> findByRefIdAndServiceStatus(String refId);
	
    @Query(value = "SELECT * FROM thirdparty_service_jobs WHERE ref_id=?1 and service_status in ('FAILURE' ,'INPROGRESS') and service_type = ?2 order by id desc",nativeQuery = true)
    List<ThirdPartyServiceJob> findAllByRefIdAndServiceType(String refId,String serviceType);

}
