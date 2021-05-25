package com.tcl.dias.serviceactivation.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.MstCambiumDetails;

@Repository
public interface MstCambiumDetailsRepository extends JpaRepository<MstCambiumDetails, Integer> {

	MstCambiumDetails findFirstByServiceCodeAndIsActiveOrderByIdDesc(String serviceCode,String status);
	
}
