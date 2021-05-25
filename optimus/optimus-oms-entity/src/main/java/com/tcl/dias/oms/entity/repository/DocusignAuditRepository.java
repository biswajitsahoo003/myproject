package com.tcl.dias.oms.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.DocusignAudit;

/**
 * 
 * This file contains the DocusignAuditRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface DocusignAuditRepository extends JpaRepository<DocusignAudit, Integer> {

	public DocusignAudit findByOrderRefUuid(String orderRefId);

	public DocusignAudit findByCustomerEnvelopeId(String customerEnvelopeId);

	public DocusignAudit findBySupplierEnvelopeId(String supplierEnvelopeId);
	
	public DocusignAudit findByOrderRefUuidAndStage(String orderRefId,String stage);

	public DocusignAudit findByApproverOneEnvelopeId(String approverOneEnvelopeId);

	public DocusignAudit findByApproverTwoEnvelopeId(String approverTwoEnvelopeId);

	DocusignAudit findByCustomerOneEnvelopeId(String envelopeId);

	DocusignAudit findByCustomerTwoEnvelopeId(String envelopeId);
	
	DocusignAudit findByCommercialEnvelopeId(String envelopeId);
}
