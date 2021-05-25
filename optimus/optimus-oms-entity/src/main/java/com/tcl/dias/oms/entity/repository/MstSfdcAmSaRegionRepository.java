package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstSfdcAmSaRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MstSfdcAmSaRegionRepository extends JpaRepository<MstSfdcAmSaRegion, Integer> {

    List<MstSfdcAmSaRegion> findByAmEmail(String email);

}
