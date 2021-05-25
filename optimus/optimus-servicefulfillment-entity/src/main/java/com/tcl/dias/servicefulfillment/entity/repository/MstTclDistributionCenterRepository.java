package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.MasterTclDistributionCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MstTclDistributionCenterRepository extends JpaRepository<MasterTclDistributionCenter, Integer> {

}
