package com.tcl.dias.oms.entity.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OdrScheduleDetails;

/**
 * Repository class of odr_schedule_Details entity
 * @author archchan
 *
 */
@Repository
public interface OdrScheduleDetailsRepository extends JpaRepository<OdrScheduleDetails, Integer>{

	OdrScheduleDetails findByOrderLinkId(Integer orderNplLinkId);
	
	List<OdrScheduleDetails> findByServiceIdAndActivationStatusIn(String serviceId, List<String> activationStatus);
	
	List<OdrScheduleDetails> findByServiceIdAndActivationStatusInAndScheduleStartDate(String serviceId, List<String> activationStatus,OffsetDateTime scheduleStartDate);
	
	@Query(value = "SELECT * FROM odr_schedule_details sd WHERE sd.service_id = :serviceId AND sd.activation_status IN (:activationStatus) AND ((ADDTIME(sd.schedule_start_date,\"1\") <= :startDate AND sd.schedule_end_date >= :startDate) OR (ADDTIME(sd.schedule_start_date,\"1\") <= :endDate AND sd.schedule_end_date >= :endDate) OR (:startDate <= ADDTIME(sd.schedule_start_date,\"1\") AND :endDate >= ADDTIME(sd.schedule_start_date,\"1\")))",nativeQuery = true)
	List<OdrScheduleDetails> findByServiceIdAndActivationStatusInAndScheduleStartDateAndEndDate(@Param("serviceId") String serviceId, @Param("activationStatus") List<String> activationStatus,@Param("startDate") OffsetDateTime scheduleStartDate,@Param("endDate") OffsetDateTime scheduleEndDate);
	
	OdrScheduleDetails findByScheduleIdAndMdsoResourceId(String scheduleId, String mdsoResoureId);
	
	OdrScheduleDetails findByMdsoResourceIdAndMdsoFeasibilityUuid(String resourceId, String operationId);
	
	OdrScheduleDetails findByOrderCodeAndOrderLinkIdAndServiceId(String orderCode, Integer linkId, String serviceId);
	
	OdrScheduleDetails findByOrderCodeAndOrderLinkId(String orderCode, Integer linkId);
	
	OdrScheduleDetails findByScheduleOperationIdAndMdsoResourceId(String scheduleOperationId, String mdsoResoureId);
	
	
}
