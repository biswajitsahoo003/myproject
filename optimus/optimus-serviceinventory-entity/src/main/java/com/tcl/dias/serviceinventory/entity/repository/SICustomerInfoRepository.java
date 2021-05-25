package com.tcl.dias.serviceinventory.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.serviceinventory.entity.entities.SICustomerInfo;
@Repository
public interface SICustomerInfoRepository extends JpaRepository<SICustomerInfo, Integer>{
	
	List<SICustomerInfo> findByCuId(String cuId);

	

}
