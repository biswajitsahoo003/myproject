package com.tcl.dias.l2oworkflow.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.l2oworkflow.entity.entities.StateRegionMapping;

@Repository
public interface StageRegionMappingRepository extends JpaRepository<StateRegionMapping, Integer> {
	
	StateRegionMapping findByState(String state);

}
