package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerLegalEntity;

/**
 * Repository class Partner Legal Entity
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerLegalEntityRepository extends JpaRepository<PartnerLegalEntity, Integer> {

    List<PartnerLegalEntity> findByPartnerId(Integer partnerId);

    /**
     * Find All Partner Legal Entity By Sap Code
     *
     * @param sapCodes
     * @return
     */
    @Query(value = "select sap.code_value, ple.entity_name, ple.id as le_id from partner_legal_entity_sap_code sap inner join partner_legal_entity ple on sap.partner_le_id = ple.id where sap.code_value in (:sapCodes)", nativeQuery = true)
    List<Map<String, Object>> findAllPartnerLegalEntityBySapCode(@Param("sapCodes") List<String> sapCodes);

    /**
     * Find All Partner Legal Entity By Billing Account Ids
     *
     * @param billingAccountIds
     * @return
     */
    @Query(value = "select bill.bill_acc_no as code_value, ple.entity_name, ple.id as le_id from partner_le_billing_info bill inner join partner_legal_entity ple on bill.partner_le_id = ple.id where bill.bill_acc_no in (:billingAccountIds)", nativeQuery = true)
    List<Map<String, Object>> findAllPartnerLegalEntityByBillingAccountIds(@Param("billingAccountIds") List<String> billingAccountIds);
    
    /**
	 * 
	 * Find all partner le with pagination
	 * @param specification
	 * @param pageable
	 * @return
	 */
	Page<PartnerLegalEntity> findAll(Specification<PartnerLegalEntity> specification, Pageable pageable);
}
