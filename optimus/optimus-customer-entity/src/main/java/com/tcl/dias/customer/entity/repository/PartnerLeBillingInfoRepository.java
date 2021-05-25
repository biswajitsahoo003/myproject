package com.tcl.dias.customer.entity.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcl.dias.customer.entity.entities.PartnerLeBillingInfo;

/**
 * Repository Class for Partner Billing Info
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Repository
public interface PartnerLeBillingInfoRepository extends JpaRepository<PartnerLeBillingInfo, Integer> {

    List<PartnerLeBillingInfo> findByPartnerLegalEntity_IdAndIsactive(Integer customerleid, String isActive);

    @Query(value = "select partner_le_id as partnerLeId ,fname as firstName, lname as lastName,email_id as emailId, phone_number as phoneNumber,mobile_number as mobileNumber from partner_le_billing_info where partner_le_id in (:partnerLeIds) and isactive=:isActive", nativeQuery = true)
    List<Map<String, Object>> findByPartnerLegalEntity_IdInAndIsactive(@Param("partnerLeIds") List<Integer> partnerLeIds, @Param("isActive") String isActive);

    @Query(value = "select bill_acc_no from partner_le_billing_info where partner_le_id in (:partnerLeIds) and isactive=:isActive", nativeQuery = true)
    List<Map<String, Object>> findBillingAccountForPartnerLegalEntity_IdInAndIsactive(@Param("partnerLeIds") List<Integer> partnerleids, @Param("isActive") String isActive);

    PartnerLeBillingInfo findByIdAndPartnerLegalEntity_Id(Integer billingInfoId, Integer partnerleId);

    PartnerLeBillingInfo findByIdAndPartnerLegalEntity(Integer billingInfoId, Integer partnerleId);

    Optional<PartnerLeBillingInfo> findById(Integer billingInfoId);


}
