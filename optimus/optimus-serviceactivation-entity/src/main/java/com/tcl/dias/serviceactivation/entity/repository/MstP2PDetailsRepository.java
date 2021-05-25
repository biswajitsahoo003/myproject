package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstP2PDetails;

@Repository
public interface MstP2PDetailsRepository extends JpaRepository<MstP2PDetails, Integer> {

	MstP2PDetails findFirstByServiceCodeAndIsActiveOrderByIdDesc(String serviceCode,String status);
	
}
