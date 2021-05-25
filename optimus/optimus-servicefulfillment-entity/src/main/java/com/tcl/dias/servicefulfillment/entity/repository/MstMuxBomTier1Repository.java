package com.tcl.dias.servicefulfillment.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstMuxBomTier1;

@Repository
public interface MstMuxBomTier1Repository extends JpaRepository<MstMuxBomTier1, String> {
	MstMuxBomTier1 findByCity(String city);
}
