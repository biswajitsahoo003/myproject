package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.SupplierLegalEntityCompanyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class Supplier Legal Sap Code
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface SupplierLegalEntityCompanyCodeRepository extends JpaRepository<SupplierLegalEntityCompanyCode, Integer> {

    @Query(value = "select code_value from service_provider_legal_entity_company_code where sp_le_id =:spLeId and code_type =:codeType " +
            "and sp_le_type =:spLeType and is_active = 'Yes'", nativeQuery = true)
    List<String> findCompanyCodeBySupplierLeId(@Param("spLeId") Integer spLeId, @Param("codeType") String codeType, @Param("spLeType") String spLeType);
}
