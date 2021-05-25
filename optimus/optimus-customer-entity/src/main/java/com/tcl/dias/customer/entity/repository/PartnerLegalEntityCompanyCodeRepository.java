package com.tcl.dias.customer.entity.repository;

import com.tcl.dias.customer.entity.entities.PartnerLegalEntityCompanyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This file contains the Partner Legal Entity Company Code Details
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Repository
public interface PartnerLegalEntityCompanyCodeRepository extends JpaRepository<PartnerLegalEntityCompanyCode, Integer> {

    @Query(value = "select distinct company.supplier_name_gbs from partner_legal_entity_sap_code sap " +
            "left join partner_legal_entity_company_code company on sap.id = company.partner_le_sap_code_id " +
            "where sap.partner_le_id =:partnerLeId and sap.code_value =:secsId and company.supplier_name_gbs is not null", nativeQuery = true)
    List<String> findSupplierNameByLeIdAndSecsId(@Param("partnerLeId") Integer partnerLeId, @Param("secsId") Integer secsId);

}
