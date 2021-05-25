package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.Partner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Jpa Repository class of Engagement to opportunity and its entity
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public interface EngagementOpportunityRepository extends JpaRepository<EngagementToOpportunity, Integer> {

    /**
     * Find By Opportunity
     * @param opportunity
     * @return {@link EngagementToOpportunity}
     */
    EngagementToOpportunity findByOpportunity(Opportunity opportunity);

    @Query(value = "select eto.* from engagement_to_opportunity eto inner join opportunity o on eto.opty_id = o.id where o.uuid =:optyId", nativeQuery = true)
    EngagementToOpportunity findByOpportunityCode(String optyId);

	Optional<EngagementToOpportunity> findById(Integer engagementOptyId);
	
	@Query(value = "select p.erf_cus_partner_id from engagement eng join partner p on eng.partner_id = p.id where eng.id =:engOptyId", nativeQuery = true)
	Integer findByEngagementOpty(Integer engOptyId);
}
