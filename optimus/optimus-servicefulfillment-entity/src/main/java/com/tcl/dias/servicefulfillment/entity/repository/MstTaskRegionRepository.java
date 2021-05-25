package com.tcl.dias.servicefulfillment.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
/**
 * 
 * This file contains the MstTaskRegionRepository.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface MstTaskRegionRepository extends JpaRepository<MstTaskRegion, Integer>{
	
	public List<MstTaskRegion> findByRegionAndGroup(String region,String group);
	public List<MstTaskRegion> findByGroup(String group);
	public List<MstTaskRegion> findByGroupIn(List<String> group);
	public List<MstTaskRegion> findByGroupAndLocation(String group,String location);

}
