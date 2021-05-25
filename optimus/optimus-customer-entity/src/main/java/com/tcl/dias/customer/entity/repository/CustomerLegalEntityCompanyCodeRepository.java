package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.CustomerLegalEntityCompanyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file contains the Customer Legal Entity Company Code Details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface CustomerLegalEntityCompanyCodeRepository extends JpaRepository<CustomerLegalEntityCompanyCode, Integer> {

    @Query(value = "select distinct company.supplier_name_gbs from customer_legal_entity_sap_code sap " +
            "left join customer_legal_entity_company_code company on sap.id = company.customer_le_sap_code_id " +
            "where sap.customer_le_id =:customerLeId and sap.code_value =:secsId and company.supplier_name_gbs is not null", nativeQuery = true)
    List<String> findSupplierNameByLeIdAndSecsId(@Param("customerLeId") Integer customerLeId, @Param("secsId") Integer secsId);

}
