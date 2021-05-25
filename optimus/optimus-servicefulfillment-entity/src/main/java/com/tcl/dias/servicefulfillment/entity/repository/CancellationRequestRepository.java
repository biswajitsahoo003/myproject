package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.CancellationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancellationRequestRepository extends JpaRepository<CancellationRequest, Integer> {
	
	
	CancellationRequest findByCancellationOrderCode(String orderCode);
	
	CancellationRequest findByOrderCode(String orderCode);

	CancellationRequest findByCancellationServiceCodeAndCancellationOrderCode(String serviceCode,String orderCode);

}
