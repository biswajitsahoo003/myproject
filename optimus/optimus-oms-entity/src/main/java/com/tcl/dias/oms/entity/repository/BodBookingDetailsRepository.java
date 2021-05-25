package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.BodBookingDetails;

/**
 * Repository class of booking_schedule_Details entity
 * @author archchan
 *
 */
@Repository
public interface BodBookingDetailsRepository extends JpaRepository<BodBookingDetails, Integer>{
	
	public BodBookingDetails findByBodScheduleIdAndMdsoResourceId(String scheduleId, String resourceId);
	
	public BodBookingDetails findFirstByBodScheduleIdAndMdsoResourceIdOrderByCreatedTimeAsc(String scheduleId, String resourceId);
	
}
