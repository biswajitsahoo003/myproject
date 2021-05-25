package com.tcl.dias.oms.entity.repository;

import com.tcl.dias.oms.entity.entities.GscAttachments;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Repository for GscAttachments entity
 *
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface GscAttachmentsRepository extends JpaRepository<GscAttachments, Integer> {
	GscAttachments findGscAttachmentByDocumentUId(String documentUid);

	List<GscAttachments> findAllByOmsAttachmentIn(List<OmsAttachment> omsAttachments);
	
	GscAttachments findAllByOmsAttachment(OmsAttachment omsAttachments);
}
