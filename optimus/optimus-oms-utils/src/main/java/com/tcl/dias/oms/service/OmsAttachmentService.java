package com.tcl.dias.oms.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.AttachmentsAudit;
import com.tcl.dias.oms.entity.entities.OdrAttachment;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.AttachmentsAuditRepository;
import com.tcl.dias.oms.entity.repository.OdrAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class contains service to store the attachment information
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class OmsAttachmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsAttachmentService.class);
	
	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	QuoteRepository quoterepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;
	
	@Autowired
	AttachmentRepository attachmentRepository;
	
	@Autowired
	OdrAttachmentRepository odrAttachmentRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AttachmentsAuditRepository attachmentsAuditRepository;
	
	
	public static final String OTHERS = "Others";
	public static final String ATTACHMENT_IS_NULL = "Attachment is null";
	
	
	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;
	

	/**
	 * This Method is for processing the attachment Information
	 * 
	 * @param attachmentRequest
	 * @return 
	 * @throws TclCommonException
	 */
	public Integer processOmsAttachment(OmsListenerBean omsAttachmentRequest) throws TclCommonException {
		OmsAttachment omsAttachment = new OmsAttachment();
		try {
			if (Objects.isNull(omsAttachmentRequest)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			List<OmsAttachBean> omsAttachBean = omsAttachmentRequest.getOmsAttachBean();
			LOGGER.info("omsAttachmentRequest.getOmsAttachBean {}" , omsAttachmentRequest.getOmsAttachBean());
			for (OmsAttachBean bean : omsAttachBean) {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository
						.findByReferenceNameAndReferenceIdAndAttachmentType(bean.getReferenceName(),
								bean.getReferenceId(), bean.getAttachmentType());
				if (omsAttachments.isEmpty()) {
					omsAttachment = persistOmsAttachment(bean, omsAttachment);
				} else {
					for (OmsAttachment attachment : omsAttachments) {
						omsAttachment = persistOmsAttachment(bean, attachment);
					}
				}
				//IPC - Changes
				try {
					if (bean.getOrderLeId() != null) {
						Optional<OrderToLe> orderToLe = orderToLeRepository.findById(bean.getOrderLeId());
						Order orderEntity = orderRepository.findByOrderLeId(orderToLe.get().getId());
						LOGGER.info("orderCode {}", orderEntity.getOrderCode());
						if (orderToLe.isPresent() && orderEntity.getOrderCode().startsWith("IPC")) {
							persistAttachmentAndOdrttachmentForIPC(bean, orderEntity,
									orderToLe.get().getErfServiceInventoryTpsServiceId());
						}
					}
					if (bean.getQouteLeId() != null) {
						Optional<QuoteToLe> le = quoteToLeRepository.findById(bean.getQouteLeId());
						if (le.isPresent()) {
							Integer quoteId = quoteToLeRepository.getQuoteId(bean.getQouteLeId());
							LOGGER.info("quoteId---{}", quoteId);
							Optional<Quote> quote = quoterepository.findById(quoteId);
							if (quote.isPresent()) {
								LOGGER.info("getQuote from Quote{}", quote.get().getQuoteCode());
								LOGGER.info("le.get().getQuoteType()---{}", le.get().getQuoteType());
								if (le.get().getQuoteType().equals("TERMINATION")) {
									persistAttachmentAudit(bean, quote.get());
								}
								if (quote.get().getQuoteCode().startsWith("IPC")
										&& ("Quotes").equals(bean.getReferenceName())) {
									persistAttachmentAndOdrttachmentForIPC(bean, null,
											le.get().getErfServiceInventoryTpsServiceId());
								}
							}
						}
					}
				} catch (Exception ex) {
					LOGGER.info("Exception thrown when mapping Tax in Attachement");
				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED,ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return omsAttachment.getId();
	}
	
	public void persistAttachmentAudit(OmsAttachBean bean,Quote quote){
			User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
			AttachmentsAudit audit = new AttachmentsAudit();
			LOGGER.info("user---{}",user.getId());
			LOGGER.info("user---{}",user.getUsername());
			LOGGER.info("bean---{}",bean.toString());
			LOGGER.info("QuoteCode  in persistAttachmentAudit---{}",quote.getQuoteCode());
			audit.setQuoteCode(quote.getQuoteCode());
			audit.setFileName(bean.getFileName());
			audit.setQuoteToLeId(bean.getQouteLeId());
			if(!(CommonConstants.QUOTES.equalsIgnoreCase(audit.getReferenceName()) || CommonConstants.ORDERS.equalsIgnoreCase(audit.getReferenceName())) ) {
				if (quote.getQuoteCode().startsWith("IAS") || quote.getQuoteCode().startsWith("GVPN")) {
					audit.setQuoteSiteId(bean.getReferenceId());
				} else if (quote.getQuoteCode().startsWith("NPL") || quote.getQuoteCode().startsWith("NDE")) {
					audit.setQuoteLinkId(bean.getReferenceId());
				}
			}
			audit.setAttachmentType(bean.getAttachmentType());
			audit.setCustomerAttachmentId(bean.getAttachmentId());
			audit.setReferenceName(bean.getReferenceName());
			audit.setReferenceId(Integer.toString(bean.getReferenceId()));
			audit.setCreatedTime(new Date());
			audit.setCreatedBy(user.getId());
			LOGGER.info("audit{}",audit.toString());
			audit = attachmentsAuditRepository.save(audit);	
	}
	
	private void persistAttachmentAndOdrttachmentForIPC(OmsAttachBean bean, Order order, String serviceCode) {
		String attachmentType = null;
		if ("TAX".equalsIgnoreCase(bean.getAttachmentType())) {
			attachmentType = CommonConstants.IPC_TAX;
		} else if ("SOLUTION".equalsIgnoreCase(bean.getAttachmentType())) {
			attachmentType = CommonConstants.IPC_SOLUTION_DOCUMENT;
		} else if ("OTHERS".equalsIgnoreCase(bean.getAttachmentType())) {
			if ("Orders_IPC_License_Quote".equalsIgnoreCase(bean.getReferenceName())) {
				attachmentType = CommonConstants.IPC_LICENSE_QUOTE;
			} else if ("Orders_IPC_Solution_Validation_Email".equalsIgnoreCase(bean.getReferenceName())) {
				attachmentType = CommonConstants.IPC_SOLUTION_VALIDATION_EMAIL;
			}
		}

		if (attachmentType != null) {
			Attachment attachment = new Attachment();
			attachment.setName(bean.getFileName());
			attachment.setType(attachmentType);
			attachment.setStoragePathUrl(bean.getPath());
			attachment.setIsActive(CommonConstants.Y);
			attachment.setCreatedBy(Utils.getSource());
			attachment.setCreatedDate(new Timestamp(new Date().getTime()));
			attachmentRepository.save(attachment);
			
			OdrAttachment odrAttachment = new OdrAttachment();
			odrAttachment.setAttachmentId(attachment.getId());
			if(order != null) {
				odrAttachment.setOrderId(order.getId());
			}
			odrAttachment.setServiceCode(serviceCode);
			odrAttachment.setAttachmentType(attachmentType);
			odrAttachment.setProductName(CommonConstants.IPC);
			odrAttachment.setOfferingName("IPC-CLOUD");
			odrAttachment.setIsActive(CommonConstants.Y);
			odrAttachmentRepository.save(odrAttachment);
		}
	}

	/**
	 * persistOmsAttachment
	 * 
	 * @param bean
	 * @param omsAttachment
	 * @return 
	 */
	private OmsAttachment persistOmsAttachment(OmsAttachBean bean, OmsAttachment omsAttachment) {
		omsAttachment.setErfCusAttachmentId(bean.getAttachmentId());
		omsAttachment.setAttachmentType(bean.getAttachmentType());
		omsAttachment.setReferenceName(bean.getReferenceName());
		omsAttachment.setReferenceId(bean.getReferenceId());
		if(null != bean.getQouteLeId()) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(bean.getQouteLeId());
			if (quoteToLe.isPresent()) {
				omsAttachment.setQuoteToLe(quoteToLe.get());
			}	
		}
		if(null != bean.getOrderLeId()) {
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(bean.getOrderLeId());
			if (orderToLe.isPresent()) {
				omsAttachment.setOrderToLe(orderToLe.get());
			}
		}
		return omsAttachmentRepository.save(omsAttachment);
	}
	
	/**
	 * Persist in Oms Attachment
	 * 
	 * @param bean
	 * @param attachment
	 * @return {@link OmsAttachment}
	 */
	private OmsAttachment persistGscOmsAttachment(OmsAttachBean bean) {
		OmsAttachment omsAttachment=new OmsAttachment();
		omsAttachment.setErfCusAttachmentId(bean.getAttachmentId());
		omsAttachment.setAttachmentType(OTHERS);
		omsAttachment.setReferenceName(bean.getReferenceName());
		omsAttachmentRepository.save(omsAttachment);
		return omsAttachment;
	}

	/**
	 * Process Attachment and save to Attachment table
	 * 
	 * @param responseBody
	 * @return {@link OmsAttachment}
	 * @throws TclCommonException
	 */
	public OmsAttachment processOmsGscAttachment(String responseBody) throws TclCommonException{
		Objects.requireNonNull(responseBody,ATTACHMENT_IS_NULL);
		OmsListenerBean omsAttachmentRequest = (OmsListenerBean) Utils.convertJsonToObject(responseBody,
				OmsListenerBean.class);
		OmsAttachBean omsAttachBean = omsAttachmentRequest.getOmsAttachBean().get(0);
			OmsAttachment omsAttachment = omsAttachmentRepository
					.findByReferenceNameAndAttachmentType(omsAttachBean.getReferenceName(),
							omsAttachBean.getAttachmentType());
		if(Objects.nonNull(omsAttachBean.getAttachmentId())){
			return persistGscOmsAttachment(omsAttachBean);
		}
		else{
			//checking if omsAttachment for country combination present, if not present return null
			if(Objects.isNull(omsAttachment.getId())) {
				return null;
			}
			//checking if omsAttachment for country combination present, if present returning that omsAttachment
			else{
				return omsAttachment;
			}
		}
	}
	
	

	/**
	 * This Method is for processing the attachment Information
	 * 
	 * @param attachmentRequest
	 * @throws TclCommonException
	 */
	public void processOmsAttachmentForMF(OmsListenerBean omsAttachmentRequest) throws TclCommonException {

		try {
			if (Objects.isNull(omsAttachmentRequest)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			List<OmsAttachBean> omsAttachBean = omsAttachmentRequest.getOmsAttachBean();

			for (OmsAttachBean bean : omsAttachBean) {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository
						.findByAttachmentTypeAndErfCusAttachmentId(bean.getAttachmentType(),bean.getAttachmentId());
				if (omsAttachments.isEmpty()) {
					OmsAttachment omsAttachment = new OmsAttachment();
					persistOmsAttachment(bean, omsAttachment);
				} else {
					for (OmsAttachment omsAttachment : omsAttachments) {
						persistOmsAttachment(bean, omsAttachment);
					}
				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED,ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}
	

	/**
	 * This Method is for processing the attachment Information
	 * 
	 * @param attachmentRequest
	 * @throws TclCommonException
	 */
	public void deleteOmsAttachmentReferenceForMF(OmsListenerBean omsAttachmentRequest) throws TclCommonException {

		try {
			if (Objects.isNull(omsAttachmentRequest)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}

			List<OmsAttachBean> omsAttachBean = omsAttachmentRequest.getOmsAttachBean();

			for (OmsAttachBean bean : omsAttachBean) {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository
						.findByAttachmentTypeAndErfCusAttachmentIdAndReferenceName(bean.getAttachmentType(),
								bean.getAttachmentId(), bean.getReferenceName());
				if (!omsAttachments.isEmpty()) {
					for (OmsAttachment omsAttachment : omsAttachments) {
						omsAttachment.setErfCusAttachmentId(null);
						omsAttachmentRepository.save(omsAttachment);
					}
				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED,ex,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}
	
	

	
	/**
	 * persistOmsAttachmentSDD
	 * 
	 * @param bean
	 * @param omsAttachment
	 */
	public void persistOmsAttachmentSDD(OmsAttachBean bean, OmsAttachment omsAttachment) {
		omsAttachment.setErfCusAttachmentId(bean.getAttachmentId());
		omsAttachment.setAttachmentType(bean.getAttachmentType());
		omsAttachment.setReferenceName(bean.getReferenceName());
		omsAttachment.setReferenceId(bean.getReferenceId());
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(bean.getQouteLeId());
		if (quoteToLe.isPresent()) {
			omsAttachment.setQuoteToLe(quoteToLe.get());
		}
		if(!Objects.isNull(bean.getOrderLeId()))
		{
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(bean.getOrderLeId());
			if (orderToLe.isPresent()) {
				omsAttachment.setOrderToLe(orderToLe.get());
			} 
		}
		else
		{
			omsAttachment.setOrderToLe(null);
		}
		omsAttachmentRepository.save(omsAttachment);
	}


}
