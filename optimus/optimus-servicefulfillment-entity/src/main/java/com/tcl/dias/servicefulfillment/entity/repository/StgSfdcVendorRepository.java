package com.tcl.dias.servicefulfillment.entity.repository;

import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SfdcVendorC;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Repository
public interface StgSfdcVendorRepository extends JpaRepository<Stg0SfdcVendorC, Integer> {

    List<Stg0SfdcVendorC> findAll(Specification<Stg0SfdcVendorC> specification);

    List<Stg0SfdcVendorC> findByVendorIdC(String vendorId);
    
    Stg0SfdcVendorC findByVendorIdCAndCompanyCodeC(String vendorId,String companyCode);

}
