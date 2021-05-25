package com.tcl.dias.serviceactivation.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceactivation.entity.entities.VpnMetatData;

@Repository
public interface VpnMetatDataRepository extends JpaRepository<VpnMetatData, Integer>{
	
	List<VpnMetatData> findByServiceDetail_IdAndEndDateIsNull(Integer serviceDetailId);

}
