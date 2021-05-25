package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcl.dias.oms.entity.entities.QuoteLeCreditCheckAudit;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
/**
 * 
 * Repository for QuoteLe credit check audit
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public interface QuoteLeCreditCheckAuditRepository extends JpaRepository<QuoteLeCreditCheckAudit, Integer>{
	
	List<QuoteLeCreditCheckAudit> findByQuoteToLe_id(Integer quoteToLeId);
	
	List<QuoteLeCreditCheckAudit> findByQuoteToLeAndTpsSfdcCuIdAndTpsSfdcCreditCheckStatus(QuoteToLe quoteToLeId, String tpsSfdcCuId, String creditCheckStatus);

	public void deleteByQuoteToLeId(Integer quoteToLeId);
}
