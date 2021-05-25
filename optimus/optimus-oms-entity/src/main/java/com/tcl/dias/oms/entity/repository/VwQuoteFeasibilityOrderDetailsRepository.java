package com.tcl.dias.oms.entity.repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.VwQuoteFeasibilityOrderDetails;

@Repository
public interface VwQuoteFeasibilityOrderDetailsRepository extends JpaRepository<VwQuoteFeasibilityOrderDetails, String> {

	@Query(value = "select * from vw_quote_feasibility_order_details where quote_created_time >= :fromDate AND quote_created_time <= :toDate ;",nativeQuery=true)
	List<VwQuoteFeasibilityOrderDetails> findAllByQuoteCreatedTime(@Param("fromDate") String formatFromDate, @Param("toDate") String formatToDate);

}