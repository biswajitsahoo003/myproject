package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;

@Repository
public interface MstOrderSiteStageRepository extends JpaRepository<MstOrderSiteStage, Integer>{
	
	MstOrderSiteStage findByName(String name);

}
