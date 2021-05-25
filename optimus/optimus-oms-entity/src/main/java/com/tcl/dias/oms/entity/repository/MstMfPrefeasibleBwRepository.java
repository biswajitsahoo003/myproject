package com.tcl.dias.oms.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.MstMfPrefeasibleBw;

@Repository
public interface MstMfPrefeasibleBwRepository extends JpaRepository<MstMfPrefeasibleBw, Integer>{
	
	public MstMfPrefeasibleBw findByLocationAndProduct(String location, String product);

}
