package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.AceIPMapping;

@Repository
public interface AceIPMappingRepository extends JpaRepository<AceIPMapping, Integer> {

	List<AceIPMapping> findAllByScServiceDetail_Id(Integer serviceId);
	
	List<AceIPMapping> findByScServiceDetail_IdAndAceIp(Integer serviceId,String aceIp);
	
	void deleteByScServiceDetail_Id(Integer serviceId);
	
}
