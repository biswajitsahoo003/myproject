package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.FeasibilityPricingPayloadAudit;
import com.tcl.dias.oms.entity.entities.OrderProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.UtilityAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for  FeasibilityPricingPayload audit
 * @author archchan
 *
 */
@Repository
public interface FeasibilityPricingPayloadAuditRepository extends JpaRepository<FeasibilityPricingPayloadAudit, Integer> {
    List<FeasibilityPricingPayloadAudit> findByMdcTokenAndAuditType(String token,String type);
    List<FeasibilityPricingPayloadAudit> findByQuoteCode(String quoteCode);
}
