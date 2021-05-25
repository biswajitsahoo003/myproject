package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstRadwinDetails;

@Repository
public interface MstRadwinDetailsRepository extends JpaRepository<MstRadwinDetails, Integer> {

	MstRadwinDetails findFirstByServiceCodeAndIsActiveOrderByIdDesc(String serviceCode,String status);
	
}
