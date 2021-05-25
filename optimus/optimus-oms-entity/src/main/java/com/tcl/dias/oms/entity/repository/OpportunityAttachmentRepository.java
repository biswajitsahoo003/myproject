package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.OpportunityToAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Jpa Repository class of OpportunityToAttachment and its entity
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public interface OpportunityAttachmentRepository extends JpaRepository<OpportunityToAttachment, Integer> {

    List<OpportunityToAttachment> findByOptyId(Integer opportunityId);
}
