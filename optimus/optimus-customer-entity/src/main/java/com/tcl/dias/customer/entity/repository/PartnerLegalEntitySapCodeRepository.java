package com.tcl.dias.customer.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerLegalEntitySapCode;

/**
 * Repository class Partner Legal Sap Code
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerLegalEntitySapCodeRepository extends JpaRepository<PartnerLegalEntitySapCode, Integer> {

    List<PartnerLegalEntitySapCode> findBypartnerLeIdInAndCodeType(List<Integer> partnerLeIDs, String spaCode);

    @Query(value = "SELECT concat(company_code.code_value, sap_code.code_value) from partner_legal_entity_sap_code sap_code, partner_legal_entity_company_code company_code where company_code.partner_le_sap_code_id = sap_code.id and sap_code.partner_le_id in (:partnerLeId) and sap_code.code_type = :sapCodeType", nativeQuery = true)
    List<String> getPartnerLeCompanyCode(@Param("partnerLeId") List<Integer> partnerLeId,@Param("sapCodeType") String sapCodeType);

    @Query(value = "SELECT sap_code.code_value from partner_legal_entity_sap_code sap_code where sap_code.partner_le_id = :partnerLeId " +
            "and sap_code.code_type = 'SECS Code'", nativeQuery = true)
    List<String> getPartnerLeSecsCode(@Param("partnerLeId") Integer partnerLeId);
}
