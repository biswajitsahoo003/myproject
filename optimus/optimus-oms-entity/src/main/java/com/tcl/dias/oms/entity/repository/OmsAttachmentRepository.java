package com.tcl.dias.oms.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * 
 * This file contains the OmsAttachmentRepository.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Repository
public interface OmsAttachmentRepository extends JpaRepository<OmsAttachment, Integer> {

	public List<OmsAttachment> findByOrderToLe(OrderToLe orderToLe);
	
	public List<OmsAttachment> findByOrderToLeAndReferenceId(OrderToLe orderToLe, Integer referenceId);
	
	public List<OmsAttachment> findByOrderToLeAndReferenceName(OrderToLe orderToLe, String referenceName);
	
	public List<OmsAttachment> findByOrderToLeAndReferenceNameIn(OrderToLe orderToLe, List<String> referenceName);

	public List<OmsAttachment> findByReferenceNameAndReferenceIdAndAttachmentType(String referenceName,
                                                                                  Integer referenceId, String type);

	public OmsAttachment findByReferenceNameAndAttachmentType(String referenceName, String attachmentType);
	
	public List<OmsAttachment> findByQuoteToLeAndAttachmentType(QuoteToLe quoteToLe, String attachmentType);


    public List<OmsAttachment> findByQuoteToLeAndAttachmentTypeAndReferenceName(QuoteToLe quoteToLe, String attachmentType, String referenceName);
    
    public List<OmsAttachment> findByOrderToLeAndAttachmentTypeAndReferenceName(OrderToLe orderToLe, String attachmentType, String referenceName);
    
	public List<OmsAttachment> findByAttachmentTypeAndReferenceName(String referenceName, String attachmentType);
	

	public List<OmsAttachment> findByAttachmentTypeAndErfCusAttachmentId( String type,Integer erfCustId);
	
	public List<OmsAttachment> findByAttachmentTypeAndErfCusAttachmentIdAndReferenceName( String type,Integer erfCustId,String refName);
	
	public List<OmsAttachment> findByQuoteToLeAndAttachmentTypeOrderByIdDesc(QuoteToLe quoteToLe, String attachmentType);




	/**
	 * Find Country Documents
	 *
	 * @return
	 */
	@Query(value = "select a.id,a.attachment_type,a.erf_cus_attachment_id,a.reference_id,a.reference_name,a.order_le_id,a.quote_le_id from oms_attachments a where a.quote_le_id is null and a.order_le_id is null and a.reference_id is null", nativeQuery = true)
	List<OmsAttachment> findCountryDocuments();
    
	
	public List<OmsAttachment> findByQuoteToLe(QuoteToLe quoteToLe);

	public OmsAttachment findByErfCusAttachmentId(Integer documentID);
	
	public List<OmsAttachment>  findByReferenceNameAndReferenceId(String refName,Integer refId);
	
	public List<OmsAttachment> findByReferenceNameAndReferenceIdAndAttachmentTypeOrderByIdDesc(String referenceName,
            Integer referenceId, String type);

    OmsAttachment findByReferenceName(String globalOutboundNegotiatedPriceTemplate);
    
    /* newly added for Rate-file generation */
    public List<OmsAttachment> findByQuoteToLeAndReferenceNameIn(QuoteToLe quoteToLe, List<String> refNames);

	List<OmsAttachment> findByOrderToLeAndAttachmentType(OrderToLe orderToLe, String type);
	
	List<OmsAttachment> findByOrderToLeInAndAttachmentType(List<OrderToLe> orderToLes, String type);
}
