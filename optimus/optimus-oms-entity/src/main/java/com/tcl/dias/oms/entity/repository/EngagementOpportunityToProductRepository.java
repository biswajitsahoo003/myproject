package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.EngagementOpportunityToProduct;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa Repository class of Engagement opportunity to product and its entity
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public interface EngagementOpportunityToProductRepository extends JpaRepository<EngagementOpportunityToProduct, Integer> {

    /**
     * Find By Engagement To Opportunity
     * @param engagementToOpportunity
     * @return {@link EngagementOpportunityToProduct}
     */
    EngagementOpportunityToProduct findByEngagementToOpportunity(EngagementToOpportunity engagementToOpportunity);
}
