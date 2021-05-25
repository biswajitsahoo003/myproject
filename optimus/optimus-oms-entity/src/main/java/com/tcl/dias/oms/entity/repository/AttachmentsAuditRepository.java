package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tcl.dias.oms.entity.entities.AttachmentsAudit;

@Repository
public interface AttachmentsAuditRepository extends JpaRepository<AttachmentsAudit, Integer> {
	
	@Query(value="select * from attachments_audit a where a.quote_to_le_id = ?1 and a.attachment_type in ('CUSTEMAIL','ADDLDOC')",nativeQuery = true)
	List<AttachmentsAudit> findByQuoteToLeIdAndAttachmentType(Integer quoteToLeId);
	
	
	
	List<AttachmentsAudit> findByQuoteToLeIdAndAttachmentType(Integer quoteToLeId, String attachmentType);
}
