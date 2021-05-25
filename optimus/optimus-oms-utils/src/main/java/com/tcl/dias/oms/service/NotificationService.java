package com.tcl.dias.oms.service;


import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER_CREATE_ENTITY_NOTIFICATION;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


import com.tcl.dias.oms.beans.SalesSupportMailNotificationBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.PartnerNotificationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * This file contains the NotificationService.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	@Value("${notification.mail.from}")
	String fromAddress;
	
	@Value("${pilot.team.email}")
	String[] pilotTeamMail;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${notification.manual.feasibility.template}")
	String manualFeasibilityTemplateId;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.feasibility.pricing.change.template}")
	String feasibilityPricingChangeTemplateId;

	@Value("${notification.quote.download.template}")
	String quoteDownloadTemplateId;

	@Value("${notification.sales.order.multiplele.template}")
	String salesOrderMultipleLeTemplateId;

	@Value("${notification.sales.order.lemismatch.template}")
	String salesOrderLeMismatchTemplateId;

	@Value("${notification.cof.delegation.template}")
	String cofDelegationTemplateId;

	@Value("${notification.cof.download.template}")
	String cofDownloadTemplateId;

	@Value("${notification.new.order.template}")
	String newOrderTemplateId;

	@Value("${notification.new.site.entered.template}")
	String newSiteEnteredTemplateId;

	@Value("${notification.site.deleted.template}")
	String siteDeletedTemplateId;

	@Value("${notification.quote.expiry.template}")
	String quoteExpiryTemplateId;

	@Value("${notification.feasibility.expiry.template}")
	String feasibilityExpiryTemplateId;

	@Value("${notification.help.ticket.template}")
	String helpTicketTemplateId;

	@Value("${notification.pending.provisioning.template}")
	String pendingProvisioningTemplateId;

	@Value("${notification.provisioning.update.template}")
	String provisioningOrderUpdateTemplateId;

	@Value("${notification.provisioning.new.order.template}")
	String provisioningNewOrderTemplateId;
	
	@Value("${notification.service.acceptance.template}")
	String serviceAcceptanceTemplateId;

	@Value("${notification.order.status.change.template}")
	String orderStatusChangeTemplateId;

	@Value("${notification.order.delivery.complete.template}")
	String orderDeliveryCompleteTemplateId;

	@Value("${notification.issue.resolved.template}")
	String issueResolvedTemplateId;

	@Value("${notification.order.missing.information.template}")
	String orderMissingInformationTemplateId;

	@Value("${notification.new.user.added.template}")
	String newUserAddedTemplateId;

	@Value("${notification.user.deleted.template}")
	String userDeletedTemplateId;

	@Value("${customer.site}")
	String customerSite;

	@Value("${notification.rrfs.date.change.template}")
	String rrfsDateChangeTemplateId;

	@Value("${notification.order.mail.bcc}")
	String[] orderConfirmationBccMail;

	@Value("${notification.cof.cus.delegation.template}")
	String cusCofDelegateTemplate;

	@Value("${notification.order.welcome.letter}")
	String welcomerLetter;

	@Value("${notification.quote.update.online}")
	String quoteUpdateOnline;

	@Value("${notification.sales.order.supplier.unavailable.template}")
	String supplierLeTemplateId;

	@Value("${notification.mail.template}")
	String attachmentTemplateId;

	@Value("${notification.sharequote.template}")
	String shareQuoteTemplate;

	@Value("${notification.manual.feasibility.pricing.template}")
	String manualFeasibilityPricingTemplateId;
	
	@Value("${notification.tax.exemption.us.sites.template}")
	String taxExemptionNotificationTemplateId;
	
	@Value("${notification.creditCheck.template}")
	String creditCheckNotificationTemplateId;
	
	
	@Value("${notification.customerportal.initial.template}")
	String notifyCustomerPortalInitialTemplate;
	
	@Value("${notification.customerportal.mid.template}")
	String notifyCustomerPortalMidTemplate;
	
	@Value("${notification.customerportal.creditcheck.template}")
	String notifyCustomerPortalCreditCheckTemplate;
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${notification.docusign.sent.confirmation}")
	String notificationDocusignSentTemplateId;
	
	@Value("${notification.docusign.signed.confirmation}")
	String notificationDocusignSignedTemplateId;

	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	
	@Value("${notification.blacklist.account.template}")
	String accountBlacklistedNotificationTemplateId;
	
	@Value("${credit.check.team.email}")
	String creditCheckTeamEmail;

	@Value("${notification.partner.create.entity}")
	String notificationPartnerCreateEntityTemplateId;

	@Value("rabbitmq.partner.details.notification.queue")
	String partnerNotificationQueue;

	@Value("${gsc.sell.through.vebal.aggreemnt}")
	private String gscSellThroughVerbalAgreement;

	@Value("${gsc.sell.through.verbal.aggrement.email}")
	private String gscSellThroughVerbalAgreementEmails;

	@Value("${notification.ordernalite.feasibility.template}")
	private String notificationOrderNaLiteFeasibilityTemplate;

	@Value("${notification.termination.acknowledgement.template}")
	private String notifyTerminationAcknowledgmentTemplate;

	@Value("${notification.termination.initiation.template}")
	private String notifyTerminationInitiationTemplate;

	@Value("${notification.etc.applicable.template}")
	private String notifyEtcApplicableTemplate;

	@Value("${notification.etc.revision.template}")
	private String notifyEtcRevisionTemplate;

	@Value("${rabbitmq.customer.contact.details.queue}")
	String customerLeContactQueueName;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;

	@Value("${partner.gsc.sales.support.email}")
	private String partnerGscSalesSupportEmail;

	@Value("${notification.partner.sales.support.template}")
	private String partnerSalesSupportTemplate;

	@Value("${notification.ngp.sales.support.template}")
	private String ngpSalesSupportTemplate;

	@Autowired
	MACDUtils macdUtils;

	public boolean manualFeasibilityNotification(MailNotificationBean mailNotificationBean) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getCustomerEmail());
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(mailNotificationBean.getAccountManagerEmail()) && !mailNotificationBean.getAccountManagerEmail().equals(CommonConstants.HYPHEN))
				ccAddresses.add(mailNotificationBean.getAccountManagerEmail());
			ccAddresses.add(customerSupportEmail);
			map.put("orderid", mailNotificationBean.getOrderId());
			map.put("statuslink", mailNotificationBean.getQuoteLink());
			map.put("estimateddate", mailNotificationBean.getEstimatedReadinessDate());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + mailNotificationBean.getOrderId() + ", Manual Feasibility for Product "
					+ mailNotificationBean.getProductName());
			mailNotificationRequest.setTemplateId(manualFeasibilityTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(mailNotificationBean.getCustomerEmail());
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setNotificationAction(CommonConstants.MANUAL_FEASIBILITY_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call manualFeasibilityNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;
	}

	public boolean processShareQuoteNotification(String toAddress, String quoteHtml, String userName, String fileName,String productName)
			throws TclCommonException {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("customername", userName);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject(userName + " shared a quote for you");
			mailNotificationRequest.setTemplateId(shareQuoteTemplate);
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(toAddress);
			mailNotificationRequest.setAttachementHtml(quoteHtml);
			mailNotificationRequest.setAttachmentName(fileName);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setProductName(productName);
			mailNotificationRequest.setUserEmailId(toAddress);
			mailNotificationRequest.setNotificationAction(CommonConstants.SHARE_QUOTE_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call processShareQuoteNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return isSuccess;
	}

	public boolean feasibilityPricingChangeNotification(String customerEmail, String accountManagerEmail,
			Integer orderId, String quoteLink,String productName) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("orderid", orderId);
			map.put("statuslink", quoteLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + orderId + ", pricing and feasibility update");
			mailNotificationRequest.setTemplateId(feasibilityPricingChangeTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(customerEmail);
			mailNotificationRequest.setProductName(productName);
			mailNotificationRequest.setNotificationAction(CommonConstants.FEASIBILITY_PRICING_CHANGE_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call feasibilityPricingChangeNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}
	
	/**
	 * 
	 * @param customerName
	 * @param customerEmail
	 * @param quoteRef
	 * @param profileName
	 * @param productName
	 * @param portBw
	 * @return
	 * @throws TclCommonException
	 */
	public boolean customerPortalInitialNotify(String customerName, String customerEmail,
			String quoteRef,String productName) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("quoteRefId", quoteRef);
			map.put("customerName", customerName);
			map.put("productName", productName);
			map.put("customerSupport", customerSupportEmail);
			map.put("tat", "6 days");
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + quoteRef + ", pricing and feasibility for "+ productName);
			mailNotificationRequest.setTemplateId(notifyCustomerPortalInitialTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(customerEmail);
			mailNotificationRequest.setProductName(productName);
			mailNotificationRequest.setNotificationAction("Your order #" + quoteRef + ", pricing and feasibility for "+ productName);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}
	
	/**
	 * 
	 * @param customerName
	 * @param customerEmail
	 * @param quoteRef
	 * @param productName
	 * @param portalLink
	 * @return
	 * @throws TclCommonException
	 */
	public boolean customerPortalMidNotify(String customerName, String customerEmail,
			String quoteRef,String productName,String portalLink) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("quoteRefId", quoteRef);
			map.put("customerName", customerName);
			map.put("customerSupport", customerSupportEmail);
			map.put("portalLink", portalLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + quoteRef + ", pricing and feasibility update for "+ productName);
			mailNotificationRequest.setTemplateId(notifyCustomerPortalMidTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(customerEmail);
			mailNotificationRequest.setProductName(productName);
			mailNotificationRequest.setNotificationAction("Your order #" + quoteRef + ", pricing and feasibility update for "+ productName);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;
	}
	
	/**
	 * 
	 * @param customerName
	 * @param customerEmail
	 * @param quoteRef
	 * @param customerLeName
	 * @return
	 * @throws TclCommonException
	 */
	public boolean customerPortalCreditNotify(String customerName, String customerEmail,
			String quoteRef,String customerLeName) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("quoteRefId", quoteRef);
			map.put("customerName", customerName);
			map.put("customerSupport", customerSupportEmail);
			map.put("leName", customerLeName);
			map.put("tat", "6 days");
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + quoteRef + ", Credit Check update");
			mailNotificationRequest.setTemplateId(notifyCustomerPortalCreditCheckTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(customerEmail);
			mailNotificationRequest.setNotificationAction("Your order #" + quoteRef + ", Credit Check update");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;
	}

	public boolean quoteDownloadNotification(String accountName, String accountManagerEmail, String customerName,
			String userName, Integer orderId, String quoteLink) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(accountManagerEmail);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("orderid", orderId);
			map.put("statuslink", quoteLink);
			map.put("customername", customerName);
			map.put("username", userName);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Customer " + accountName + ", Quote download notification");
			mailNotificationRequest.setTemplateId(quoteDownloadTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call quoteDownloadNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	/**
	 * Notification for selecting muliple legal entities
	 *
	 * @param customerEmail
	 * @param orderId
	 * @param quoteLink
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	public boolean salesOrderMultipleleNotification(MailNotificationBean mailNotificationBean)
			throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getCustomerEmail());
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("orderid", mailNotificationBean.getOrderId());
			map.put("statuslink", mailNotificationBean.getQuoteLink());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + mailNotificationBean.getOrderId()
					+ ", Multiple Legal Entity Support request for Product " + mailNotificationBean.getProductName());
			mailNotificationRequest.setTemplateId(salesOrderMultipleLeTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setUserEmailId(mailNotificationBean.getCustomerEmail());
			mailNotificationRequest.setNotificationAction(CommonConstants.SALES_ORDER_MULTIPLE_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call salesOrderMultipleleNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	/**
	 * @author VIVEK KUMAR K salesOrdeSupplierUnavailableNotification
	 * @param customerEmail
	 * @param orderId
	 * @param quoteLink
	 * @return
	 * @throws TclCommonException
	 */
	public boolean salesOrdeSupplierUnavailableNotification(String customerEmail, String orderId, String quoteLink,String productName)
			throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("orderid", orderId);
			map.put("statuslink", quoteLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + orderId + ", Supplier Legal Entity Support request");
			mailNotificationRequest.setTemplateId(supplierLeTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setProductName(productName);
			mailNotificationRequest.setUserEmailId(customerEmail);
			mailNotificationRequest.setNotificationAction(CommonConstants.SALES_ORDER_SUPPLIER_UNAVAILABLE_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call salesOrdeSupplierUnavailableNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean salesOrdeLeMismatchNotification(String accountName, String customerName, String userName,
			String accountManagerEmail, String orderId, String orderViewLink) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerSupportEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			map.put("orderrefid", orderId);
			map.put("omsorderviewlink", orderViewLink);
			map.put("customername", customerName);
			map.put("username", userName);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(
					"Customer " + accountName + ", Order #" + orderId + " Legal Entity Mismatch notification");
			mailNotificationRequest.setTemplateId(salesOrderLeMismatchTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call salesOrdeLeMismatchNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	/**
	 * Cof delegation notification
	 *
	 * @param mailNotificationBean
	 * @return
	 * @throws TclCommonException
	 */
	public boolean cofDelegationNotification(MailNotificationBean mailNotificationBean)
			throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(mailNotificationBean.getAccountManagerEmail()) &&
					!mailNotificationBean.getAccountManagerEmail().equals(CommonConstants.HYPHEN)) {
				toAddresses.add(mailNotificationBean.getAccountManagerEmail());
			}
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("orderrefid", mailNotificationBean.getOrderId());
			map.put("quotaionstatuslink", mailNotificationBean.getQuoteLink());
			map.put("customername", mailNotificationBean.getCustomerName());
			map.put("username", mailNotificationBean.getUserName());
			map.put("mobilenumber", mailNotificationBean.getUserContactNumber());
			map.put("email", mailNotificationBean.getUserEmail());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject(map.get("userType") + mailNotificationBean.getCustomerAccountName() + ", Order #"
					+ mailNotificationBean.getOrderId() + " COF signature delegation notification");
			mailNotificationRequest.setTemplateId(cofDelegationTemplateId);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call cofDelegationNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean cofCustomerDelegationNotification(MailNotificationBean mailNotificationBean) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getUserEmail());
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			if (StringUtils.isNotBlank(mailNotificationBean.getAccountManagerEmail()) && !mailNotificationBean.getAccountManagerEmail().equals(CommonConstants.HYPHEN))
				ccAddresses.add(mailNotificationBean.getAccountManagerEmail());
			map.put("orderrefid", mailNotificationBean.getOrderId());
			map.put("coflink", mailNotificationBean.getQuoteLink());
			map.put("customername", mailNotificationBean.getCustomerName());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("A COF delegated to you is pending action for Product " + mailNotificationBean.getProductName());
			mailNotificationRequest.setTemplateId(cusCofDelegateTemplate);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setUserEmailId(mailNotificationBean.getUserEmail());
			mailNotificationRequest.setNotificationAction(CommonConstants.COF_CUSTOMER_DELEGATION_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call cofCustomerDelegationNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean cofDownloadNotification(MailNotificationBean mailNotificationBean) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getAccountManagerEmail());
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			map.put("orderrefid", mailNotificationBean.getOrderId());
			map.put("quotationstatuslink", mailNotificationBean.getQuoteLink());
			map.put("customername", mailNotificationBean.getCustomerName());
			map.put("username", mailNotificationBean.getUserName());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject(map.get("userType") + " " + mailNotificationBean.getCustomerAccountName()
					+ ", Order #" + mailNotificationBean.getOrderId()
							+ " for Product " + mailNotificationBean.getProductName()
							+ " - COF Download notification");
			mailNotificationRequest.setTemplateId(cofDownloadTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call cofDownloadNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean newOrderSubmittedNotification(MailNotificationBean mailNotificationBean) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getCustomerEmail());
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(mailNotificationBean.getAccountManagerEmail()) &&
					!mailNotificationBean.getAccountManagerEmail().equals(CommonConstants.HYPHEN))
				ccAddresses.add(mailNotificationBean.getAccountManagerEmail());
			ccAddresses.add(customerSupportEmail);
			map.put("orderrefid", mailNotificationBean.getOrderId());
			map.put("orderenrichmentUrl", mailNotificationBean.getQuoteLink());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			
			if(mailNotificationBean.getOrderId().startsWith("IPC")){
				mailNotificationRequest.setSubject("Your order #" + mailNotificationBean.getOrderId() + " for " +
						mailNotificationBean.getProductName() + " has been placed");
				map.put("order_enrichment_msg", CommonConstants.IPC_ORDER_ENRICHMENT_MSG);
			}else{
				mailNotificationRequest.setSubject("Your order #" + mailNotificationBean.getOrderId() + " for Product " +
						mailNotificationBean.getProductName() + " has been placed");
				map.put("order_enrichment_msg", CommonConstants.ORDER_ENRICHMENT_MSG);
			}
			mailNotificationRequest.setTemplateId(newOrderTemplateId);
			mailNotificationRequest.setCofObjectMapper(mailNotificationBean.getCofObjectMapper());
			mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setAttachmentName(mailNotificationBean.getFileName());
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setUserEmailId(mailNotificationBean.getCustomerEmail());
			mailNotificationRequest.setNotificationAction(CommonConstants.NEW_ORDER_SUBMISSION_NOTIFICATION);
			
			LOGGER.info("MDC Filter token value in before Queue call newOrderSubmittedNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean welcomeLetterNotification(MailNotificationBean mailNotificationBean) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getCustomerEmail());
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			if (StringUtils.isNotBlank(mailNotificationBean.getAccountManagerEmail()) && !mailNotificationBean.getAccountManagerEmail().equals(CommonConstants.HYPHEN))
				ccAddresses.add(mailNotificationBean.getAccountManagerEmail());
			map.put("user_name", mailNotificationBean.getUserName());
			map.put("today_date", DateUtil.convertDateToSlashString(new Date()));
			map.put("contracting_entity", mailNotificationBean.getContactEntityName());
			map.put("supplier_entity", mailNotificationBean.getSupllierEntityName());
			map.put("order_refid", mailNotificationBean.getOrderId());
			map.put("le_Name", mailNotificationBean.getCustomerAccountName());
			map.put("le_contact", mailNotificationBean.getAccountManagerContact());
			map.put("le_email", mailNotificationBean.getAccountManagerEmail());
			map.put("order_track", mailNotificationBean.getQuoteLink());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			
			if(mailNotificationBean.getOrderId().startsWith("IPC")){
				mailNotificationRequest.setSubject("Your order #" + mailNotificationBean.getOrderId() + " for " + mailNotificationBean.getProductName()
				+ " has been placed");
				map.put("thank_you_msg", CommonConstants.IPC_THANK_YOU_MSG);
				map.put("order_type", mailNotificationBean.getOrderType());
			}else{
				mailNotificationRequest.setSubject("Your order #" + mailNotificationBean.getOrderId() + " for Product " + mailNotificationBean.getProductName()
				+ " has been placed");
				map.put("thank_you_msg", CommonConstants.THANK_YOU_MSG);
				map.put("order_type", "New Order");
			}
			mailNotificationRequest.setTemplateId(welcomerLetter);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(mailNotificationBean.getCustomerEmail());
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setNotificationAction(CommonConstants.WELCOME_LETTER_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call welcomeLetterNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean salesSupportMailNotification(SalesSupportMailNotificationBean salesSupportMailNotificationBean) throws TclCommonException {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			List<String> bccAddresses = new ArrayList<>();
			List<String> ccAddresses = new ArrayList<>();
			toAddresses.add(partnerGscSalesSupportEmail);
			map.put("opportunity_id", salesSupportMailNotificationBean.getOpportunityId());
			map.put("quote_ref_id", salesSupportMailNotificationBean.getQuoteRefId());
			map.put("partner_le", salesSupportMailNotificationBean.getPartnerLe());
			map.put("secs", salesSupportMailNotificationBean.getSecsId());
			map.put("end_customer_name", salesSupportMailNotificationBean.getEndCustomerName());
			map.put("status", salesSupportMailNotificationBean.getStatus());
			map.put("accountName", salesSupportMailNotificationBean.getAccountName());
			StringBuilder subject = new StringBuilder();
			subject.append("Partner Order ").append(salesSupportMailNotificationBean.getAccountName())
					.append(" created GSC ").append(salesSupportMailNotificationBean.getOptyClassification())
					.append(" Opportunity ").append(salesSupportMailNotificationBean.getOpportunityId())
					.append(" has reached ").append(salesSupportMailNotificationBean.getCompletePercentage()).append("%");
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setTemplateId(partnerSalesSupportTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setNotificationAction(CommonConstants.OPTIMUS_NOTIFICATION);
			mailNotificationRequest.setSubject(subject.toString());
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			LOGGER.info(">> Sales Support Email Notification sent");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return isSuccess;
	}

	public boolean ngpSalesSupportMailNotification(SalesSupportMailNotificationBean salesSupportMailNotificationBean) throws TclCommonException {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			List<String> bccAddresses = new ArrayList<>();
			List<String> ccAddresses = new ArrayList<>();
			// same tiger team email
			toAddresses.add(partnerGscSalesSupportEmail);
			map.put("opportunity_id", salesSupportMailNotificationBean.getOpportunityId());
			map.put("quote_ref_id", salesSupportMailNotificationBean.getQuoteRefId());
			//map.put("partner_le", salesSupportMailNotificationBean.getPartnerLe());
			map.put("secs", salesSupportMailNotificationBean.getSecsId());
			//map.put("end_customer_name", salesSupportMailNotificationBean.getEndCustomerName());
			map.put("status", salesSupportMailNotificationBean.getStatus());
			//map.put("accountName", salesSupportMailNotificationBean.getAccountName());
			StringBuilder subject = new StringBuilder();
			subject.append("NGP order created on Optimus portal");
//			subject.append("Partner Order ").append(salesSupportMailNotificationBean.getAccountName())
//					.append(" created GSC ").append(salesSupportMailNotificationBean.getOptyClassification())
//					.append(" Opportunity ").append(salesSupportMailNotificationBean.getOpportunityId())
//					.append(" has reached ").append(salesSupportMailNotificationBean.getCompletePercentage()).append("%");
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setTemplateId(ngpSalesSupportTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setNotificationAction(CommonConstants.OPTIMUS_NOTIFICATION);
			mailNotificationRequest.setSubject(subject.toString());
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			LOGGER.info(">> Sales Support Email Notification sent");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return isSuccess;
	}

	public boolean newSiteEnteredNotification(String accountManagerEmail, String customerAccountName,
			String customerName, String userName, Integer orderId, String orderViewLink) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerSupportEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			map.put("orderrefid", orderId);
			map.put("customername", customerName);
			map.put("username", userName);
			map.put("orderViewLink", orderViewLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Customer " + customerAccountName + " - new site notification");
			mailNotificationRequest.setTemplateId(newSiteEnteredTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call newSiteEnteredNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean siteDeletedPermanentlyNotification(String accountManagerEmail, String customerAccountName,
			String customerName, String userName, Integer orderId, String orderViewLink) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerSupportEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			map.put("orderrefid", orderId);
			map.put("customername", customerName);
			map.put("username", userName);
			map.put("orderViewLink", orderViewLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Customer " + customerAccountName + " - site deletion notification");
			mailNotificationRequest.setTemplateId(siteDeletedTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call siteDeletedPermanentlyNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean quoteExpiryNotification(String customerEmail, String accountManagerEmail, Integer orderId,
			String quotePageLink, Integer quoteId) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("orderrefid", orderId);
			map.put("quoteLink", quotePageLink);
			map.put("quoteId", quoteId);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Your order #" + orderId + " quotation has expired");
			mailNotificationRequest.setTemplateId(quoteExpiryTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call quoteExpiryNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean feasibilityExpiryNotification(String customerEmail, String accountManagerEmail, Integer orderId,
			String quotePageLink, Integer quoteId) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("orderrefid", orderId);
			map.put("quoteLink", quotePageLink);
			map.put("quoteId", quoteId);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Your order #" + orderId + " feasibility has expired");
			mailNotificationRequest.setTemplateId(feasibilityExpiryTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call feasibilityExpiryNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean helpTicketNotification(String customerEmail, String accountManagerEmail, String subject,
			String message,String productName) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("reason", subject);
			map.put("freetext", message);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Support request acknowledgement");
			mailNotificationRequest.setTemplateId(helpTicketTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(customerEmail);
			mailNotificationRequest.setProductName(productName);
			mailNotificationRequest.setNotificationAction(CommonConstants.HELP_TICKET_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call helpTicketNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean pendingProvisioningNotification(String customerEmail, String accountManagerEmail,
			String projectManagerEmail, Integer orderId, String orderEnrichmentFormLink) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			ccAddresses.add(projectManagerEmail);
			map.put("orderid", orderId);
			map.put("orderEnrichmentFormLink", orderEnrichmentFormLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest
					.setSubject("Your order #" + orderId + " delivery is pending completion of your inputs");
			mailNotificationRequest.setTemplateId(pendingProvisioningTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call pendingProvisioningNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean provisioningOrderUpdateNotification(String customerEmail, String accountManagerEmail,
			String projectManagerEmail, Integer orderId, Integer subOrderId, String orderTrackingLink)
			throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			ccAddresses.add(projectManagerEmail);
			map.put("orderid", orderId);
			map.put("subOrderId", subOrderId);
			map.put("orderTrackingLink", orderTrackingLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject(
					"Your order #" + orderId + ", sub order #" + subOrderId + " - Order Enrichment form now updated");
			mailNotificationRequest.setTemplateId(provisioningOrderUpdateTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call provisioningOrderUpdateNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean provisioningOrderNewOrderNotification(String accountManagerEmail, String orderRefId,
			String customerAccountName, String orderTrackingLink) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerSupportEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			map.put("orderrefid", orderRefId);
			map.put("omsorderviewlink", orderTrackingLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest
					.setSubject("Customer " + customerAccountName + ", order #" + orderRefId + " - ready for delivery");
			mailNotificationRequest.setTemplateId(provisioningNewOrderTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call provisioningOrderNewOrderNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean orderStatusChangeNotification(String accountManagerEmail, String customerEmail,
			String projectManagerEmail, Integer orderId, Integer subOrderId, String orderTrackingLink,
			String orderTrackingView) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(projectManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("orderid", orderId);
			map.put("subOrderId", subOrderId);
			map.put("orderTrackingLink", orderTrackingLink);
			map.put("orderTrackingView", orderTrackingView);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject(
					" Your order #" + orderId + ", sub order #" + subOrderId + " - order tracking has been updated");
			mailNotificationRequest.setTemplateId(orderStatusChangeTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call orderStatusChangeNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean orderDeliveryCompleteNotification(MailNotificationBean mailNotificationBean)
			throws TclCommonException {
		//String accountManagerEmail, String customerEmail,
		//			String projectManagerEmail, String orderId, String subOrderId, String effectiveDeliveryDate,String productName
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getCustomerEmail());
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(mailNotificationBean.getAccountManagerEmail()) &&
					!mailNotificationBean.getAccountManagerEmail().equals(CommonConstants.HYPHEN)) {
				ccAddresses.add(mailNotificationBean.getAccountManagerEmail());
			}
			if (StringUtils.isNotBlank(mailNotificationBean.getProjectManagerEmail()) &&
					!mailNotificationBean.getProjectManagerEmail().equals(CommonConstants.HYPHEN)) {
				ccAddresses.add(mailNotificationBean.getProjectManagerEmail());
			}
			ccAddresses.add(customerSupportEmail);
			map.put("orderid", mailNotificationBean.getOrderId());
			map.put("suborderref", mailNotificationBean.getSubOrderId());
			map.put("delivery_date", mailNotificationBean.getEffectiveDeliveryDate());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest
					.setSubject("Your order #" + mailNotificationBean.getOrderId() +
							", for Product " + mailNotificationBean.getProductName() + " sub order #" + mailNotificationBean.getSubOrderId()
							+ " has been delivered");
			mailNotificationRequest.setTemplateId(orderDeliveryCompleteTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(mailNotificationBean.getCustomerEmail());
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setNotificationAction(CommonConstants.ORDER_DELIVERY_COMPLETE_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call orderDeliveryCompleteNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean issueResolvedNotification(String accountManagerEmail, String customerEmail, String dropdownReason,
			String freeTextReason) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("dropdownReason", dropdownReason);
			map.put("freeTextReason", freeTextReason);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Support request closure");
			mailNotificationRequest.setTemplateId(issueResolvedTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call issueResolvedNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean orderMissingInformationNotification(String accountManagerEmail, String customerEmail,
			Integer orderId, Integer subOrderId, List<String> missingInformation) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("orderId", orderId);
			map.put("subOrderId", subOrderId);
			map.put("missingInformation", missingInformation);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest
					.setSubject("Your order #" + orderId + ", sub order #" + subOrderId + " - missing information");
			mailNotificationRequest.setTemplateId(orderMissingInformationTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call orderMissingInformationNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean newUserAddedNotification(String accountManagerEmail, String userEmail, String userName)
			throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(userEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("userName", userName);
			map.put("customerSite", customerSite);
			map.put("userEmail", userEmail);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Tata Communications Portal - new user addition");
			mailNotificationRequest.setTemplateId(newUserAddedTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call newUserAddedNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean userDeletedNotification(String accountManagerEmail, String userEmail, String userName)
			throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(userEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("userName", userName);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Tata Communications Portal - user deletion");
			mailNotificationRequest.setTemplateId(userDeletedTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call userDeletedNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	public boolean rrfsDateChangeNotification(String accountManagerEmail, String projectManagerEmail,
			String customerEmail, Integer orderId, Integer subOrderId, String orderTrackingLink,
			String orderTrackingView) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			ccAddresses.add(projectManagerEmail);
			ccAddresses.add(customerSupportEmail);
			map.put("orderId", orderId);
			map.put("subOrderId", subOrderId);
			map.put("orderTrackingLink", orderTrackingLink);
			map.put("orderTrackingView", orderTrackingView);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Your order #" + orderId + ", sub order #" + subOrderId
					+ " - Requested Ready for Service Date update");
			mailNotificationRequest.setTemplateId(rrfsDateChangeTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call rrfsDateChangeNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	/**
	 * Quote Update Online Mail Notification
	 *
	 * @param mailNotificationBean
	 * @return
	 * @throws TclCommonException
	 */
	public boolean quoteUpdateOnline(MailNotificationBean mailNotificationBean) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailNotificationBean.getCustomerEmail());
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(mailNotificationBean.getAccountManagerEmail()) &&
					!mailNotificationBean.getAccountManagerEmail().equals(CommonConstants.HYPHEN))
				ccAddresses.add(mailNotificationBean.getAccountManagerEmail());
			ccAddresses.add(customerSupportEmail);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			map.put("orderid", mailNotificationBean.getOrderId());
			map.put("statuslink", mailNotificationBean.getQuoteLink());

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);

			mailNotificationRequest.setSubject("Your order #" + mailNotificationBean.getOrderId() + ", pricing and feasibility update");
			mailNotificationRequest.setTemplateId(quoteUpdateOnline);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setUserEmailId(mailNotificationBean.getUserEmail());
			mailNotificationRequest.setNotificationAction(CommonConstants.QUOTE_UPDATE_ONLINE_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call quoteUpdateOnline {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	/**
	 * This method is used to trigger the Manual Feasibility Pricing notification
	 *
	 * @param orderId
	 * @param customerName
	 * @param subjectMsg
	 * @param quoteLink
	 * @param productName
	 * @return boolean
	 */
	public boolean manualFeasibilityPricingNotification(MailNotificationBean mailNotificationBean) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerSupportEmail);
			List<String> ccAddresses = new ArrayList<>();
			/*if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);*/
			//ccAddresses.add(customerSupportEmail);
			map.put("orderid", mailNotificationBean.getOrderId());
			map.put("statuslink", mailNotificationBean.getQuoteLink());
			//map.put("estimateddate", estimatedReadinessDate);

			Map<String, Object> userBasedValues = updateUserBasedValues(mailNotificationBean);
			map.putAll(userBasedValues);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				/*for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}*/
				bccAddresses.addAll(Arrays.asList(orderConfirmationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(populateMailSubject(mailNotificationBean, map));
			mailNotificationRequest.setTemplateId(manualFeasibilityPricingTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			//mailNotificationRequest.setUserEmailId(customerEmail);
			mailNotificationRequest.setProductName(mailNotificationBean.getProductName());
			mailNotificationRequest.setNotificationAction(CommonConstants.MANUAL_FEASIBILITY_PRICING_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call manualFeasibilitypricingNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;
	}

	private String populateMailSubject(MailNotificationBean mailNotificationBean, Map<String, Object> userMapValue) {
		String subjectLine = String.format("%s %s, order # %s, %s For Product %s", userMapValue.get("userType"), mailNotificationBean.getCustomerName(),
				mailNotificationBean.getOrderId(), mailNotificationBean.getSubjectMsg(), mailNotificationBean.getProductName());
		return subjectLine;
	}
	
	/**
	 * 
	 * This method is used to trigger mail to pilot/AM for docusign sent acknowledgement
	 * @param accountManagerEmail
	 * @param customerAccountName
	 * @param customerName
	 * @param userName
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	public boolean docusignSentNOtification(String accountManagerEmail, String customerAccountName,
			String customerName, String userName, String orderId,String userEmailId) throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				toAddresses.add(accountManagerEmail);
			if (StringUtils.isNotBlank(userEmailId) && !userEmailId.equals(CommonConstants.HYPHEN))
				toAddresses.add(userEmailId);
			List<String> ccAddresses = new ArrayList<>();
			map.put("customername", customerName);
			map.put("username", userName);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Customer " + customerAccountName +" , Order # "+orderId+ " -  Docusign sent to customer");
			mailNotificationRequest.setTemplateId(notificationDocusignSentTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call docusignSentNOtification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}
	
	
	/**
	 * 
	 * This method is used to trigger mail to pilot/AM for docusign Signed acknowledgement
	 * @param accountManagerEmail
	 * @param customerAccountName
	 * @param customerName
	 * @param userName
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	public boolean docusignSignedNOtification(String accountManagerEmail, String customerAccountName,
			String customerName, String userName, String orderId) throws TclCommonException {
		LOGGER.info("docusignSignedNOtification method");
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				toAddresses.add(accountManagerEmail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("customername", customerName);
			map.put("username", userName);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Customer " + customerAccountName +" , Order # "+orderId+ " -  Docusign signed by customer");
			mailNotificationRequest.setTemplateId(notificationDocusignSignedTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call docusignSignedNOtification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}
	
	/**
	 * 
	 * This function is used to trigger the tax exemption mail notification
	 * @param customerEmail
	 * @param accountManagerEmail
	 * @param accountManagerName
	 * @param orderId
	 * @param quoteLink
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	public boolean taxExemptionNotificationforUSSites(String customerEmail,String accountManagerEmail,String accountManagerName, String orderId, String quoteLink,String productName)
			throws TclCommonException {
		boolean isSuccess = false;
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(accountManagerEmail);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			if(!StringUtils.isAllBlank(customerEmail)) {
				ccAddresses.add(customerEmail);
			}
			
			map.put("orderid", orderId);
			map.put("statuslink", quoteLink);
			map.put("accountmanager", accountManagerName);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Tax exemption selected by customer for USA site");
			mailNotificationRequest.setTemplateId(taxExemptionNotificationTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call taxExemptionNotificationforUSSites {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;

	}

	/**
	 * Update User Based Values
	 *
	 * @param variableMap
	 * @param mailNotificationBean
	 * @return {@link HashMap<String, Object>}
	 */
	private Map<String, Object> updateUserBasedValues(MailNotificationBean mailNotificationBean) {
		Map<String, Object> variableMap = new HashMap<>();
		LOGGER.info("userInfoUtils.getUserType() :: {}", userInfoUtils.getUserType());
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			variableMap.put("userType", "Partner");
			variableMap.put("classification", mailNotificationBean.getClassification());
			variableMap.put("endCustomerName", mailNotificationBean.getEndCustomerLegalEntityName());
		} else {
			variableMap.put("userType", "Customer");
		}
		LOGGER.info("variableMap :: {}", variableMap);
		return variableMap;
	}
	/**
	 * mail notification for sfdc 
	 * @param customerEmail
	 * @param quoteid
	 * @param accountName
	 * @param opportunityName
	 * @param opportunityId
	 * @param creditControlStatus
	 * @param creditRemarks
	 * @return
	 * @throws TclCommonException
	 */
    public boolean mailNotificationForSfdcCreditCheck(String customerEmail, String accountName, 
            String opportunityName, String opportunityId, String creditControlStatus, String creditRemarks, String securityDepositAmount, String link)
			throws TclCommonException {
    	
		boolean isSuccess = false;
		String message = null;
		String status = null;
		try {

				HashMap<String, Object> map=new HashMap<>();
				List<String> toAddresses = new ArrayList<>();
				toAddresses.add(customerEmail);
				List<String> ccAddresses = new ArrayList<>();
				ccAddresses.add(Objects.nonNull(pilotTeamMail[0])?pilotTeamMail[0]:null);
				ccAddresses.add(Objects.nonNull(creditCheckTeamEmail)?creditCheckTeamEmail : null);
	
				
				  if(creditControlStatus.equalsIgnoreCase(CommonConstants.POSITIVE)) {
		            	message = "Credit Evaluation is Completed. Please Place the Order.";
		            	status = "Complete";
		            	}
		            else if(creditControlStatus.equalsIgnoreCase(CommonConstants.NEGATIVE)) {
		            	message = "Credit Evaluation is Completed. Please get in touch with your respective AGMR."; 
		            	status = "Complete";
		            	}
		            else  {
		            	message = "Credit Evaluation is in Process.";
		            	status = "Reserved";
		            	}
				  
				  
			
			map.put("account",accountName);
			map.put("opportunity",opportunityName);
			map.put("id", opportunityId );
			map.put("creditControlStatus", status );
            map.put("creditCheckDocument", link);
            map.put("creditMessage", message);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Change in credit control status");
			mailNotificationRequest.setTemplateId(creditCheckNotificationTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setNotificationAction(CommonConstants.CREDIT_CHECK_STATUS_CHANGE_NOTIFICATION);
			LOGGER.info("mailNotificationForSfdcCreditCheck - Notification Object before sending tot queue {} ", mailNotificationRequest.toString());
			LOGGER.info("MDC Filter token value in before Queue call mailNotificationForSfdcCreditCheck {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;
		

	}

	public boolean mailNotificationForBlacklistedAccount(String legalentity, String accountName, String leOwnerEmail) throws TclCommonException {
		
		boolean isSuccess = false;
		try {

				HashMap<String, Object> map=new HashMap<>();
				List<String> toAddresses = new ArrayList<>();
				toAddresses.add(leOwnerEmail);
				List<String> ccAddresses = new ArrayList<>();
				ccAddresses.add(Objects.nonNull(pilotTeamMail[0])?pilotTeamMail[0]:null);
				ccAddresses.add(Objects.nonNull(creditCheckTeamEmail)?creditCheckTeamEmail : null);
			
			map.put("account",accountName);
			map.put("legalentity",legalentity);
			
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Legal Entity " + legalentity + " blacklisted for Account " + accountName );
			mailNotificationRequest.setTemplateId(accountBlacklistedNotificationTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setNotificationAction(CommonConstants.CREDIT_CHECK_ACCOUNT_BLACKLIST_NOTIFICATION);
			LOGGER.info("MDC Filter token value in before Queue call mailNotificationForBlacklistedAccount {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;
		
	}

	/**
	 * Sending mail notification when partner creates new entity
	 *
	 * @param partnerTempCustomerDetails
	 * @throws TclCommonException
	 */
	public void mailNotificationForPartnerCreateEntity(PartnerTempCustomerDetails partnerTempCustomerDetails) throws TclCommonException {
		try {
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Creation of new Legal entity for GSC Sell Through Order");
			mailNotificationRequest.setTemplateId(notificationPartnerCreateEntityTemplateId);
			mailNotificationRequest.setTo(Arrays.asList(customerSupportEmail));
			mailNotificationRequest.setNotificationAction(PARTNER_CREATE_ENTITY_NOTIFICATION);
//			mailNotificationRequest.setCc(ccAddresses);

			Map<String, Object> map = new HashMap<>();
			map.put("customerName",partnerTempCustomerDetails.getCustomerName());
			map.put("industry",partnerTempCustomerDetails.getIndustry());
			map.put("subIndustry",partnerTempCustomerDetails.getSubIndustry());
			map.put("industrySubtype",partnerTempCustomerDetails.getIndustrySubtype());
			map.put("customerWebsite",partnerTempCustomerDetails.getCustomerWebsite());
			map.put("registrationNo",partnerTempCustomerDetails.getRegistrationNo());
			map.put("businessType",partnerTempCustomerDetails.getBusinessType());
			map.put("erfPartnerId", partnerTempCustomerDetails.getErfPartnerId());
			map.put("secsId", partnerTempCustomerDetails.getSecsId());
			map.put("orgId", partnerTempCustomerDetails.getOrgId());
			map.put("street", partnerTempCustomerDetails.getStreet());
			map.put("city", partnerTempCustomerDetails.getCity());
			map.put("state", partnerTempCustomerDetails.getState());
			map.put("postalCode", partnerTempCustomerDetails.getPostalCode());
			map.put("country", partnerTempCustomerDetails.getCountry());
			map.put("customerContactName",partnerTempCustomerDetails.getCustomerContactName());
			map.put("customerContactEmail",partnerTempCustomerDetails.getCustomerContactEmail());
			map.put("recordId",partnerTempCustomerDetails.getRecordId());

			// Get below information from Partner Table
			String partnerDetailsResponse = (String) mqUtils.sendAndReceive(partnerNotificationQueue, partnerTempCustomerDetails.getErfPartnerLegalEntityId());
			PartnerNotificationDetail partnerNotificationDetail = Utils.fromJson(partnerDetailsResponse,
					new TypeReference<PartnerNotificationDetail>() {
					});

			map.put("partnerAccountName", partnerNotificationDetail.getPartnerAccountName());
			map.put("partnerAccountId", partnerNotificationDetail.getPartnerAccountId());
			map.put("partnerOrgId", partnerNotificationDetail.getPartnerOrgId());
			map.put("partnerLeName", partnerNotificationDetail.getPartnerLeName());
			map.put("partnerLeCUID", partnerNotificationDetail.getPartnerLeCUID());

			mailNotificationRequest.setVariable(map);

			LOGGER.info("MDC Filter token value in before Queue call mailNotificationForBlacklistedAccount {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public boolean serviceAcceptanceNotification(String accountManagerEmail, String orderRefId, String customerAccountName, User user, String isDeemedAcceptance, User orderPlacedUser, User orderEnrichmentUser) throws TclCommonException {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> ccAddresses = new ArrayList<>();
			if (StringUtils.isNotBlank(accountManagerEmail) && !accountManagerEmail.equals(CommonConstants.HYPHEN))
				ccAddresses.add(accountManagerEmail);
			map.put("date",new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			Set<String> uniqueMail = new HashSet<>();
			LOGGER.info("Order placed user mailid :{} ",orderPlacedUser.getEmailId());
			uniqueMail.add(orderPlacedUser.getEmailId());
			uniqueMail.add(orderEnrichmentUser.getEmailId());
			if(null!=isDeemedAcceptance && isDeemedAcceptance.equals("Y")){
				map.put("acceptancemsg", " deemed acceptance has been completed");			
			}else if(null==isDeemedAcceptance || isDeemedAcceptance.equals("N")){
				map.put("acceptancemsg", " and confirming your acceptance of service delivery");
				LOGGER.info("Logger in user mail id :{} ",user.getEmailId());
				uniqueMail.add(user.getEmailId());
			}
			List<String> toAddresses=uniqueMail.stream().collect(Collectors.toList());
			LOGGER.info("To Address mail size :{} ",toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest
					.setSubject("Customer " + customerAccountName + ", order #" + orderRefId + " - has been accepted");
			mailNotificationRequest.setTemplateId(serviceAcceptanceTemplateId);
			mailNotificationRequest.setTo(toAddresses);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call serviceAcceptanceNotification {} :");
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return isSuccess;
	}
	/**
	 * Sending mail notification when partner opportunity reach 80%
	 *
	 * @param orderCode
	 * @param opportunityId
	 *
	 * @throws TclCommonException
	 */
	public void gscSellThroughVerbalAgreementNotification(String orderCode, String opportunityId) throws TclCommonException {
		try {
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			LOGGER.info("GSC sell through verbal agreemnet mail Info  {} :" + orderCode + " " + opportunityId);
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Partner Order created for GSC Sell through Opportunity has reached 80%");
			mailNotificationRequest.setTemplateId(gscSellThroughVerbalAgreement);
			mailNotificationRequest.setTo(Arrays.asList(gscSellThroughVerbalAgreementEmails));
			mailNotificationRequest.setNotificationAction("Verbal Agreement stage for GSC Sell Through Order");
			//mailNotificationRequest.setCc(ccdAdresses);
			LOGGER.info("GSC sell through verbal agreemnet To address {} :" + gscSellThroughVerbalAgreementEmails + " " + gscSellThroughVerbalAgreement);
			Map<String, Object> map = new HashMap<>();
			//map.put("partnerName", partnerName);
			map.put("orderCode", orderCode);
			map.put("opportunityId", opportunityId);

			mailNotificationRequest.setVariable(map);

			LOGGER.info("MDC Filter token value in before Queue call gscSellThroughVerbalAgreementNotification {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in gscSellThroughVerbalAgreementNotification " + e.getMessage(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public boolean orderNALiteNotify(HashMap<String, Object> map,String psamEmail,
											   String quoteRef,String productName) throws TclCommonException {
		boolean isSuccess = false;
		try {

			//HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(psamEmail);
			List<String> ccAddresses = new ArrayList<>();
			//ccAddresses.add(customerSupportEmail);
			map.put("quoteRefId", quoteRef);
			map.put("productDescription", productName);
			map.put("psamEmail", psamEmail);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (orderConfirmationBccMail != null) {
				for (String bcc : orderConfirmationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your order #" + quoteRef + ", pricing and feasibility for "+ productName);
			mailNotificationRequest.setTemplateId(notificationOrderNaLiteFeasibilityTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mailNotificationRequest.setUserEmailId(psamEmail);
			mailNotificationRequest.setProductName(productName);
			mailNotificationRequest.setNotificationAction(CommonConstants.PARTNER_ORDER_NA_LITE_NOTIFICATION);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return isSuccess;
	}
	
	/**
	 * To send email notification to customers with attached document
	 * 
	 * @param toEmailIds
	 * @param filePath
	 * @param fileName
	 * @param subject
	 * @throws TclCommonException
	 */
	public void sendLrNotification(List<String> toEmailIds, String filePath, String fileName, String subject)
			throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setTemplateId("ANT");
		mailNotificationRequest.setTo(toEmailIds);
		mailNotificationRequest.setSubject(subject);
		mailNotificationRequest.setIsAttachment(true);
		mailNotificationRequest.setAttachmentName(fileName);
		mailNotificationRequest.setAttachmentPath(filePath);
		mqUtils.sendAndReceive(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
	}

	/**
	 * Sending mail notification when termination quote is created
	 *
	 * @param quoteCode
	 * @param quoteToLe
	 *
	 * @throws TclCommonException
	 */
	public void notifyTerminationAcknowlegment(Map<String, Object> map) throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		LOGGER.info("Termination acknowledgment mail Info : "+ map.toString());
		mailNotificationRequest.setFrom(fromAddress);
		if(Boolean.TRUE.equals(Boolean.valueOf(map.get("isMulticircuit").toString()))) {
			mailNotificationRequest.setSubject("Bulk Termination Acknowlegment Letter - "+map.get("serviceId").toString()+" Circuits");
		} else {
			mailNotificationRequest.setSubject("Termination Acknowlegment Letter - "+map.get("serviceId").toString());
		}
		mailNotificationRequest.setTemplateId(notifyTerminationAcknowledgmentTemplate);
		List<String> toEmailIds = Arrays.asList(map.get("communicationRecipient").toString());
		Object csmEmail=map.get("customerSuccessManagersEmail");
		Object amEmail=map.get("accountManagersEmail");
		List<String> ccEmailIds = new ArrayList<String>(); 
		//List<String> ccEmailIds = Arrays.asList((amEmail!=null?amEmail.toString():""), (csmEmail!=null?csmEmail.toString():""), MACDConstants.TD_SUPPORT_EMAIL);
		if (amEmail!=null) 
			ccEmailIds.add(amEmail.toString());
		if (csmEmail!=null)
			ccEmailIds.add(csmEmail.toString());
		ccEmailIds.add(MACDConstants.TD_SUPPORT_EMAIL);
		mailNotificationRequest.setTo(toEmailIds);
		mailNotificationRequest.setCc(ccEmailIds);
		LOGGER.info("Termination notification email To & Cc addresses:" + mailNotificationRequest.getTo() + "-" + mailNotificationRequest.getCc());
		mailNotificationRequest.setVariable(map);
		mailNotificationRequest.setTemplateId(notifyTerminationAcknowledgmentTemplate);
		mailNotificationRequest.setReferenceName("Orders");
		mailNotificationRequest.setReferenceValue(map.get("tataCommRefId").toString());

		try {
			LOGGER.info("MDC Filter token value in before Queue call Termination Acknowledgement {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in Termination Acknowledgement email notifications " + e.getMessage(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * Sending mail notification when termination order is accepted
	 *
	 * @param quoteCode
	 * @param quoteToLe
	 *
	 * @throws TclCommonException
	 */
	public void notifyTerminationInitiation(Map<String, Object> map ) throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		LOGGER.info("Termination initiation mail Info : "+ map.toString());
		mailNotificationRequest.setFrom(fromAddress);
		if(Boolean.TRUE.equals(Boolean.valueOf(map.get("isMulticircuit").toString()))) {
			mailNotificationRequest.setSubject("Bulk Termination Initiation Letter - "+map.get("serviceId").toString()+" Circuits");
		} else {
			mailNotificationRequest.setSubject("Termination Initiation Letter - "+map.get("serviceId").toString());
		}
		mailNotificationRequest.setTemplateId(notifyTerminationInitiationTemplate);
		List<String> toEmailIds = Arrays.asList(map.get("communicationRecipient").toString());
		Object csmEmail=map.get("customerSuccessManagersEmail");
		Object amEmail=map.get("accountManagersEmail");
		List<String> ccEmailIds = new ArrayList<String>(); 
		//List<String> ccEmailIds = Arrays.asList((amEmail!=null?amEmail.toString():""), (csmEmail!=null?csmEmail.toString():""), MACDConstants.TD_SUPPORT_EMAIL);
		if (amEmail!=null) 
			ccEmailIds.add(amEmail.toString());
		if (csmEmail!=null)
			ccEmailIds.add(csmEmail.toString());
		ccEmailIds.add(MACDConstants.TD_SUPPORT_EMAIL);
		mailNotificationRequest.setTo(toEmailIds);
		mailNotificationRequest.setCc(ccEmailIds);
		LOGGER.info("Termination Initiation email To & CC addresses:" + mailNotificationRequest.getTo() + " - " + mailNotificationRequest.getCc());
		mailNotificationRequest.setVariable(map);
        mailNotificationRequest.setTemplateId(notifyTerminationInitiationTemplate);
        mailNotificationRequest.setReferenceName("Orders");
        mailNotificationRequest.setReferenceValue(map.get("tataCommRefId").toString());


		try {
			LOGGER.info("MDC Filter token value in before Queue call Termination Initiation {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in Termination Initiation email notifications " + e.getMessage(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Sending mail notification when etc is revised
	 *
	 * @param map
	 *
	 * @throws TclCommonException
	 */
	public void notifyEtcRevision(Map<String, Object> map ) throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		LOGGER.info("Termination etc revision mail Info : "+ map.toString());
		mailNotificationRequest.setFrom(fromAddress);

		mailNotificationRequest.setSubject("Notification : ETC Revision for - "+map.get("serviceId").toString());

		List<String> toEmailIds = new ArrayList<String>();
		Object amEmail=map.get("accountManagersEmail");
		if (amEmail!=null)
			toEmailIds.add(amEmail.toString());

		List<String> ccEmailIds = new ArrayList<String>();
		ccEmailIds.add(MACDConstants.TD_SUPPORT_EMAIL);

		mailNotificationRequest.setTo(toEmailIds);
		mailNotificationRequest.setCc(ccEmailIds);
		LOGGER.info("Termination Request Form Revision email To & CC addresses:" + mailNotificationRequest.getTo() + " - " + mailNotificationRequest.getCc());
		mailNotificationRequest.setVariable(map);
		mailNotificationRequest.setTemplateId(notifyEtcRevisionTemplate);
		mailNotificationRequest.setReferenceName("Orders");
		mailNotificationRequest.setReferenceValue(map.get("tataCommRefId").toString());

		try {
			LOGGER.info("MDC Filter token value in before Queue call ETC Revision {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in ETC Revision email notifications " + e.getMessage(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
	/**
	 * Sending mail notification when etc is applicable
	 *
	 * @param map
	 *
	 * @throws TclCommonException
	 */
	public void notifyEtcApplicability(Map<String, Object> map ) throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		LOGGER.info("Termination etc applicable Info : "+ map.toString());
		mailNotificationRequest.setFrom(fromAddress);

		mailNotificationRequest.setSubject("Notification: ETC Applicability for - "+map.get("serviceId").toString());

		List<String> toEmailIds = new ArrayList<String>();
		Object amEmail=map.get("accountManagersEmail");
		if (amEmail!=null)
			toEmailIds.add(amEmail.toString());

		List<String> ccEmailIds = new ArrayList<String>();
		ccEmailIds.add(MACDConstants.TD_SUPPORT_EMAIL);

		mailNotificationRequest.setTo(toEmailIds);
		mailNotificationRequest.setCc(ccEmailIds);
		LOGGER.info("Termination Request Form Revision email To & CC addresses:" + mailNotificationRequest.getTo() + " - " + mailNotificationRequest.getCc());
		mailNotificationRequest.setVariable(map);
		mailNotificationRequest.setTemplateId(notifyEtcApplicableTemplate);
		mailNotificationRequest.setReferenceName("Orders");
		mailNotificationRequest.setReferenceValue(map.get("tataCommRefId").toString());

		try {
			LOGGER.info("MDC Filter token value in before Queue call ETC Applicability email notification {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in ETC Applicability email notification" + e.getMessage(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
}
