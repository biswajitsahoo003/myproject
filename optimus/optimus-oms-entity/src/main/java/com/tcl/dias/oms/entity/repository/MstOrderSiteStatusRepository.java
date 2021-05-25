package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;

@Repository
public interface MstOrderSiteStatusRepository extends JpaRepository<MstOrderSiteStatus, Integer> {
	
	MstOrderSiteStatus findByName(String name);

}
