package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcl.dias.oms.entity.entities.GdeScheduleDetails;

@Repository
public interface GdeScheduleDetailsRepository extends JpaRepository<GdeScheduleDetails, Integer>{

	GdeScheduleDetails findByQuoteCodeAndLinkIdAndIsActive(String quoteCode, Integer linkId, byte status);
	
	GdeScheduleDetails findByMdsoResourceIdAndLinkId(String resourceId, Integer linkId);
	
	List<GdeScheduleDetails> findByServiceIdAndActivationStatus(String serviceId, String activationStatus);
	
	GdeScheduleDetails findByLinkIdAndIsActive(Integer linkId, byte status);
	
	GdeScheduleDetails findByMdsoResourceIdAndMdsoFeasibilityUuid(String resourceId, String operationId);
	
	GdeScheduleDetails findByQuoteCodeAndLinkId(String quoteCode, Integer linkId);
	
	GdeScheduleDetails findByLinkId(Integer linkId);
	
	List<GdeScheduleDetails> findByQuoteCodeAndIsActive(String quoteCode, byte status);
	
}
