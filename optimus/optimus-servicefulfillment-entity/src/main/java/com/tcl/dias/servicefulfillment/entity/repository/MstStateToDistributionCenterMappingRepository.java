package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstStateToDistributionCenterMapping;

@Repository
public interface MstStateToDistributionCenterMappingRepository extends JpaRepository<MstStateToDistributionCenterMapping, Integer> {
	
	List<MstStateToDistributionCenterMapping> findByState(String state);
	
}
