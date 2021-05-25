package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstVendorCpeInternational;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MstVendorCpeInternationalRepository extends JpaRepository<MstVendorCpeInternational, Integer> {

    MstVendorCpeInternational findByCountry(String country);
}
