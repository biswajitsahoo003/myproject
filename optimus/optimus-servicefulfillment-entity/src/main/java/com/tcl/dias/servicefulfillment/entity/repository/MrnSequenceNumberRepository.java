package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MrnSequenceNumber;

@Repository
public interface MrnSequenceNumberRepository extends JpaRepository<MrnSequenceNumber, Integer> {
	
	MrnSequenceNumber findByServiceCodeAndType(String serviceCode,String type);

}
