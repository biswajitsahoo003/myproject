package com.tcl.dias.oms.credit.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLeVO;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.CreditCheckStatusResponse;
import com.tcl.dias.oms.constants.CreditCheckConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteLeCreditCheckAudit;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeCreditCheckAuditRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.macd.constants.MACDConstants;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the services related to credit check
 *
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CreditCheckService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreditCheckService.class);

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;

	@Autowired
	protected OmsSfdcUtilService omsSfdcService;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	protected MQUtils mqUtils;

	@Value("${rabbitmq.customerle.credit.queue}")
	String customerLeCreditCheckQueue;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	UserRepository userRepository;

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Autowired
	QuoteLeCreditCheckAuditRepository quoteLeCreditCheckAuditRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@Value("${application.env}")
	String appEnv;
	
	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	MACDUtils macdUtils;

	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;

	@Value("${rabbitmq.si.order.add.site.queue}")
	String siOrderDetailsAddSiteQueue;


	/**
	 * @author MBEDI
	 * @param quoteId
	 * @param quoteLeId
	 * @param customerLeId
	 * @throws TclCommonException
	 */

	public CustomerLeVO creditCheckBasedOnPreapprovedValue(Integer quoteId, Integer quoteLeId,
			CustomerLeDetailsBean customerLeDetails) throws TclCommonException {
		CustomerLeVO customerLeVO = new CustomerLeVO();
		Double preapprovedMrc = 0D;
		Double preapprovedNrc = 0D;

		if (quoteLeId == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}

		Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(quoteLeId);
		boolean sfdcCheck = false;
		try {
			if (optionalQuoteLe.isPresent()) {

				String billingMethodAttrValue = customerLeDetails.getAttributes().stream()
						.filter(attributes -> attributes.getAttributeName()
								.equalsIgnoreCase(LeAttributesConstants.BILLING_METHOD))
						.findFirst().map(Attributes::getAttributeValue).get();

				String paymentTermAttrValue = customerLeDetails.getAttributes().stream()
						.filter(attributes -> attributes.getAttributeName()
								.equalsIgnoreCase(LeAttributesConstants.PAYMENT_TERM))
						.findFirst().map(Attributes::getAttributeValue).get();

				//String split[] = paymentTermAttrValue.split("\\s+");
				Integer paymentTerm = Integer.parseInt(paymentTermAttrValue.replaceAll("[\\D]", ""));
				Integer preapprovedPaymentTerm = null;
				if (Objects.nonNull(customerLeDetails.getPreapprovedPaymentTerm())) {
					String preapprovedPaymentTermValue = customerLeDetails.getPreapprovedPaymentTerm();
					//String split1[] = preapprovedPaymentTermValue.split("\\s+");
					preapprovedPaymentTerm = Integer.parseInt(preapprovedPaymentTermValue.replaceAll("[\\D]", ""));
				}
				if(!optionalQuoteLe.get().getCurrencyCode().equalsIgnoreCase("USD")) {
					preapprovedMrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), LeAttributesConstants.INR.toString(),
							customerLeDetails.getPreapprovedMrc());
					preapprovedNrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), LeAttributesConstants.INR.toString(),
							customerLeDetails.getPreapprovedNrc());
					LOGGER.info("Value before conversion in usd mrc {}, nrc {}, value after conversion in inr mrc {}, nrc {}",
							customerLeDetails.getPreapprovedMrc(), customerLeDetails.getPreapprovedNrc(), preapprovedMrc, preapprovedNrc);
				}else{
					preapprovedMrc=customerLeDetails.getPreapprovedMrc();
					preapprovedNrc=customerLeDetails.getPreapprovedNrc();
					LOGGER.info("Value of mrc {}, nrc {}",
							customerLeDetails.getPreapprovedMrc(), customerLeDetails.getPreapprovedNrc(), preapprovedMrc, preapprovedNrc);
				}
//
//				if (Objects.isNull(customerLeDetails.getPreapprovedMrc())
//						|| (Objects.isNull(customerLeDetails.getPreapprovedNrc()))
//						|| (Objects.isNull(customerLeDetails.getPreapprovedPaymentTerm()))
//						|| (Objects.isNull(customerLeDetails.getPreapprovedBillingMethod()))) {
//					LOGGER.info("Some data is null from cuLe, sending for manual check by default");
//					sfdcCheck = true;
//				} else {
				if (Objects.nonNull(optionalQuoteLe.get()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(optionalQuoteLe.get().getQuoteType())
							&& !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(optionalQuoteLe.get().getQuoteCategory())) {
					LOGGER.info("quote type,quote category"+optionalQuoteLe.get().getQuoteType()+""+optionalQuoteLe.get().getQuoteCategory());

						List<String> serviceIdsList = macdUtils.getServiceIdListBasedOnQuoteToLe(optionalQuoteLe.get());
						LOGGER.info("service id list : {}", serviceIdsList);
					String serviceIdsCommaSeparated = serviceIdsList.stream().map(i -> i.trim())
							.collect(Collectors.joining(","));
					LOGGER.info("si order detail request : {}", serviceIdsCommaSeparated);

					List<SIServiceDetailsBean> serviceDetailsList = null;
					Double mrc = 0.0D;
				/*	if(optionalQuoteLe.get().getQuoteCategory() != null && !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(optionalQuoteLe.get().getQuoteCategory())) {
						String SiOrderDetailsQueue = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceIdsCommaSeparated);
						LOGGER.info("Response from si order detail queue for add site : {}", SiOrderDetailsQueue);
						SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
								.convertJsonToObject(SiOrderDetailsQueue, SIServiceDetailsBean[].class);
						serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
						LOGGER.info("Service inventory detail list : {}", serviceDetailsList);
					//	mrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getMrc()!=null?serviceDetail.getMrc():0)).sum();
						mrc = serviceDetailsList.stream().filter(Utils.distinctByKey(SIServiceDetailsBean::getTpsServiceId))
								.mapToDouble(serviceDetail -> new Double(serviceDetail.getMrc()!=null?serviceDetail.getMrc():0)).sum();
					} else {*/
						String SiOrderDetailsQueue = (String) mqUtils.sendAndReceive(siOrderDetailsAddSiteQueue, serviceIdsCommaSeparated);
						LOGGER.info("Response from si order detail queue : {}", SiOrderDetailsQueue);
						SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
								.convertJsonToObject(SiOrderDetailsQueue, SIServiceDetailsBean[].class);
						serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
						LOGGER.info("Service inventory detail list : {}", serviceDetailsList);
					//	mrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getMrc())).sum();
						mrc = serviceDetailsList.stream().filter(Utils.distinctByKey(SIServiceDetailsBean::getTpsServiceId))
								.mapToDouble(serviceDetail -> new Double(serviceDetail.getMrc()!=null?serviceDetail.getMrc():0)).sum();

				//	}

						Double differentialMRC =(optionalQuoteLe.get().getFinalMrc()!= null ?optionalQuoteLe.get().getFinalMrc():0) - (mrc);
//						Double differentialNRC = optionalQuoteLe.get().getFinalNrc()!= null ?optionalQuoteLe.get().getFinalNrc():0 - (siServiceDetailDataBean.getNrc()!= null?siServiceDetailDataBean.getNrc():0);
						constructLegaAttributeMACD(MACDConstants.DIFFERENTIAL_MRC, optionalQuoteLe.get(),
								differentialMRC);
						LOGGER.info("Checking if pre approved opportunity for MACD");
						if(Objects.nonNull(customerLeDetails.getPreapprovedBillingMethod()) && Objects.nonNull(customerLeDetails.getPreapprovedPaymentTerm())) {
							sfdcCheck = differentialMRC > preapprovedMrc
									|| optionalQuoteLe.get().getFinalNrc() > preapprovedNrc
									|| paymentTerm > preapprovedPaymentTerm
									|| !billingMethodAttrValue
									.equalsIgnoreCase(customerLeDetails.getPreapprovedBillingMethod()); }
						else {
							LOGGER.info("Comparing only mrc and nrc values, skipping preapproved billing method, preapproved payment term for MACD");
							sfdcCheck = differentialMRC > preapprovedMrc
									|| optionalQuoteLe.get().getFinalNrc() > preapprovedNrc;
						}
				}  else {

					LOGGER.info("Checking if pre approved opportunity -- NEW order/ Add site loop");
					if(Objects.nonNull(customerLeDetails.getPreapprovedBillingMethod()) && Objects.nonNull(customerLeDetails.getPreapprovedPaymentTerm())) {
					sfdcCheck = optionalQuoteLe.get().getFinalMrc() > preapprovedMrc
							|| optionalQuoteLe.get().getFinalNrc() > preapprovedNrc
							|| paymentTerm > preapprovedPaymentTerm
							|| !billingMethodAttrValue
									.equalsIgnoreCase(customerLeDetails.getPreapprovedBillingMethod()); }
					else {
						LOGGER.info("Comparing only mrc and nrc values, skipping preapproved billing method, preapproved payment term");
						sfdcCheck = optionalQuoteLe.get().getFinalMrc() > preapprovedMrc
								|| optionalQuoteLe.get().getFinalNrc() > preapprovedNrc;
					}
				}
				LOGGER.info("sfdc credit check flag {}:", sfdcCheck);
				
				// Logic added to bypass credit check in dev environments. Credit check will trigger correctly only in UAT/PROD

				if(!(appEnv.equalsIgnoreCase(CommonConstants.PROD) || appEnv.equalsIgnoreCase(CommonConstants.UAT)))
					sfdcCheck = false;

				customerLeVO.setPreapprovedOpportunityFlag(!sfdcCheck);

				optionalQuoteLe.get().setPreapprovedOpportunityFlag((byte) (sfdcCheck ? 0 : 1));
				optionalQuoteLe.get().setTpsSfdcApprovedMrc(optionalQuoteLe.get().getFinalMrc());
				optionalQuoteLe.get().setTpsSfdcApprovedNrc(optionalQuoteLe.get().getFinalNrc());
				optionalQuoteLe.get().setTpsSfdcStatusCreditControl(
						sfdcCheck ? CommonConstants.PENDING_CREDIT_ACTION : CommonConstants.POSITIVE);
				optionalQuoteLe.get().setCreditCheckTriggered((byte) (sfdcCheck ? 1 : 0));
				quoteToLeRepository.save(optionalQuoteLe.get());

				customerLeVO.setTpsSfdcStatusCreditControl(optionalQuoteLe.get().getTpsSfdcStatusCreditControl());

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerLeVO;

	}

	/**
	 * @author chetan chaudhary
	 * @param quoteId
	 * @param quoteLeId
	 * @return boolean value
	 * @throws TclCommonException
	 * @throws RuntimeException
	 */
	public boolean creditCheckWithVariationMatrix(Integer quoteId, Integer quoteLeId, String sfdcCuId)
			throws TclCommonException, RuntimeException {
		String creditCheckAccountType = null;
		boolean quotetoLeVariationMatrixCheckMrc = false;
		boolean quotetoLeVariationMatrixCheckNrc = false;
		try {
			Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(quoteLeId);
			if (optionalQuoteLe.isPresent()) {
				List<QuoteLeAttributeValue> quoteLeAttributeValueList = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute_Name(optionalQuoteLe.get(),
								LeAttributesConstants.CREDIT_CHECK_ACCOUNT_TYPE);
				if (!quoteLeAttributeValueList.isEmpty()) {
					creditCheckAccountType = quoteLeAttributeValueList.get(0).getAttributeValue();

					QuoteLeCreditCheckAudit auditRecord = checkAuditForAlreadyApprovedValues(optionalQuoteLe.get(),
							sfdcCuId, CommonConstants.POSITIVE);
					if (auditRecord != null) {
						quotetoLeVariationMatrixCheckMrc = (creditCheckAccountType
								.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_1A)
								&& optionalQuoteLe.get().getFinalMrc() > (CreditCheckConstants.VARIATION_MATRIX_1A) * (auditRecord.getTpsSfdcApprovedMrc()))
								|| (creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_2A)
										&& optionalQuoteLe.get().getFinalMrc() > (CreditCheckConstants.VARIATION_MATRIX_2A)
												* (auditRecord.getTpsSfdcApprovedMrc()))
								|| (creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_3A)
										&& optionalQuoteLe.get().getFinalMrc() > (CreditCheckConstants.VARIATION_MATRIX_3A)
												* (auditRecord.getTpsSfdcApprovedMrc()))
								|| (creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_4A)
										&& optionalQuoteLe.get().getFinalMrc() > (CreditCheckConstants.VARIATION_MATRIX_4A)
												* (auditRecord.getTpsSfdcApprovedMrc()))
								|| (creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_3B)
										&& !optionalQuoteLe.get().getFinalMrc()
												.equals(auditRecord.getTpsSfdcApprovedMrc()));

						quotetoLeVariationMatrixCheckNrc = creditCheckAccountType
								.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_1A)
								&& optionalQuoteLe.get().getFinalNrc() > (CreditCheckConstants.VARIATION_MATRIX_1A) * (auditRecord.getTpsSfdcApprovedNrc())
								|| creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_2A)
										&& optionalQuoteLe.get().getFinalNrc() > (CreditCheckConstants.VARIATION_MATRIX_2A)
												* (auditRecord.getTpsSfdcApprovedNrc())
								|| creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_3A)
										&& optionalQuoteLe.get().getFinalNrc() > (CreditCheckConstants.VARIATION_MATRIX_3A)
												* (auditRecord.getTpsSfdcApprovedNrc())
								|| creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_4A)
										&& optionalQuoteLe.get().getFinalNrc() > (CreditCheckConstants.VARIATION_MATRIX_4A)
												* (auditRecord.getTpsSfdcApprovedNrc())
								|| creditCheckAccountType.equals(CreditCheckConstants.CC_ACCOUNT_TYPE_3B)
										&& !optionalQuoteLe.get().getFinalNrc()
												.equals(auditRecord.getTpsSfdcApprovedNrc());
					}
				}
			} else {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return (quotetoLeVariationMatrixCheckMrc || quotetoLeVariationMatrixCheckNrc);

	}

	/**
	 * This method is used to return the status of credit check based on which the
	 * generate order form button needs to be enabled/disabled
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean getCreditCheckStatus(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		Boolean enable = false;
		Boolean variationMatrixCheck = false;
		CustomerLeVO customerLeVO = null;

		if (quoteLeId == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}

		Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(quoteLeId);
		if (optionalQuoteToLe.isPresent()) {
			LOGGER.info("customer Le id in request {}", optionalQuoteToLe.get().getErfCusCustomerLegalEntityId());

			CustomerLeVO customerLeAndBillingDetails = getCustomerLeDetailsForCreditCheck(
					optionalQuoteToLe.get().getQuote().getId(), optionalQuoteToLe.get().getId(),
					optionalQuoteToLe.get().getErfCusCustomerLegalEntityId());
			
			
			CustomerLeDetailsBean customerLeDetailsBean = new CustomerLeDetailsBean();
			customerLeDetailsBean.setBlacklistStatus(customerLeAndBillingDetails.getBlacklistStatus());
			customerLeDetailsBean.setCreditCheckAccountType(customerLeAndBillingDetails.getCreditCheckAccountType());
			customerLeDetailsBean.setCreditPreapprovedFlag(customerLeAndBillingDetails.getCreditPreapprovedFlag());
			customerLeDetailsBean.setPreapprovedBillingMethod(customerLeAndBillingDetails.getPreapprovedBillingMethod());
			customerLeDetailsBean.setPreapprovedMrc(customerLeAndBillingDetails.getPreapprovedMrc());
			customerLeDetailsBean.setPreapprovedNrc(customerLeAndBillingDetails.getPreapprovedNrc());
			customerLeDetailsBean.setPreapprovedPaymentTerm(customerLeAndBillingDetails.getPreapprovedPaymentTerm());
			
			
			if (customerLeAndBillingDetails.getCreditPreapprovedFlag() != null
					&& CommonConstants.Y.equalsIgnoreCase(customerLeAndBillingDetails.getCreditPreapprovedFlag())) {

				if (customerLeAndBillingDetails.getBlacklistStatus() != null
						&& CommonConstants.N.equalsIgnoreCase(customerLeAndBillingDetails.getBlacklistStatus())) {

					if (Objects.isNull(optionalQuoteToLe.get().getErfCusCustomerLegalEntityId())
							|| (Objects.nonNull(optionalQuoteToLe.get().getErfCusCustomerLegalEntityId())
									&& Objects.nonNull(optionalQuoteToLe.get().getErfCusCustomerLegalEntityId())
									&& optionalQuoteToLe.get().getErfCusCustomerLegalEntityId()
											.equals(optionalQuoteToLe.get().getErfCusCustomerLegalEntityId()))) {
						if (Objects.isNull(optionalQuoteToLe.get().getPreapprovedOpportunityFlag())
								|| (Objects.nonNull(optionalQuoteToLe.get().getPreapprovedOpportunityFlag())
										&& optionalQuoteToLe.get().getPreapprovedOpportunityFlag()
												.equals(CommonConstants.BACTIVE))) {
							LOGGER.info("same legal entity, pre approved flag value {}",
									optionalQuoteToLe.get().getPreapprovedOpportunityFlag());

							
							
							customerLeVO = creditCheckBasedOnPreapprovedValue(quoteId, quoteLeId, customerLeDetailsBean);
							omsSfdcService.processUpdateOpportunity(new Date(),
									optionalQuoteToLe.get().getTpsSfdcOptyId(), SFDCConstants.PROPOSAL_SENT,
									optionalQuoteToLe.get());
						} else if ((Objects.nonNull(optionalQuoteToLe.get().getPreapprovedOpportunityFlag())
								&& optionalQuoteToLe.get().getPreapprovedOpportunityFlag()
										.equals(CommonConstants.BDEACTIVATE))) {
							LOGGER.info("same legal entity, pre approved flag value in else {}, credit check status {}",
									optionalQuoteToLe.get().getPreapprovedOpportunityFlag(),
									optionalQuoteToLe.get().getTpsSfdcStatusCreditControl());
							if (Objects.nonNull(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl())
									&& (CommonConstants.POSITIVE.equalsIgnoreCase(
											optionalQuoteToLe.get().getTpsSfdcStatusCreditControl()))) {
								if ((!optionalQuoteToLe.get().getTpsSfdcApprovedMrc()
										.equals(optionalQuoteToLe.get().getFinalMrc()))
										|| (!optionalQuoteToLe.get().getTpsSfdcApprovedNrc()
												.equals(optionalQuoteToLe.get().getTpsSfdcApprovedNrc()))) {
									// Variation Matrix
									LOGGER.info("Triggering variation matrix check");
									variationMatrixCheck = creditCheckWithVariationMatrix(quoteId, quoteLeId,
											customerLeVO.getSfdcCuId());
									LOGGER.info("variationMatrixCheck evaluated value {}", variationMatrixCheck);
									if (variationMatrixCheck) {
										optionalQuoteToLe.get().setVariationApprovedFlag(CommonConstants.BDEACTIVATE);
										optionalQuoteToLe.get()
												.setTpsSfdcStatusCreditControl(CommonConstants.PENDING_CREDIT_ACTION);
										quoteToLeRepository.save(optionalQuoteToLe.get());
										omsSfdcService.processUpdateOpportunity(new Date(),
												optionalQuoteToLe.get().getTpsSfdcOptyId(), SFDCConstants.PROPOSAL_SENT,
												optionalQuoteToLe.get());
									} else {
										optionalQuoteToLe.get().setVariationApprovedFlag(CommonConstants.BACTIVE);
										quoteToLeRepository.save(optionalQuoteToLe.get());

									}
								}

							} else if (Objects.nonNull(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl())
									&& (CommonConstants.PENDING_CREDIT_ACTION.equalsIgnoreCase(
											optionalQuoteToLe.get().getTpsSfdcStatusCreditControl()))) {
								creditCheckBasedOnPreapprovedValue(quoteId, quoteLeId, customerLeDetailsBean);
								omsSfdcService.processUpdateOpportunity(new Date(),
										optionalQuoteToLe.get().getTpsSfdcOptyId(), SFDCConstants.PROPOSAL_SENT,
										optionalQuoteToLe.get());

							}

						}
					} else {
						creditCheckBasedOnPreapprovedValue(quoteId, quoteLeId, customerLeDetailsBean);
						omsSfdcService.processUpdateOpportunity(new Date(), optionalQuoteToLe.get().getTpsSfdcOptyId(),
								SFDCConstants.PROPOSAL_SENT, optionalQuoteToLe.get());
					}

					if (CommonConstants.POSITIVE
							.equalsIgnoreCase(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl()))
						enable = true;
					else
						enable = false;

				} else {
					triggerAccountBlackListedMail(optionalQuoteToLe.get());
					throw new TclCommonException(ExceptionConstants.BLACKLISTED_ACCOUNT_ERROR,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}

			} else {
				throw new TclCommonException(ExceptionConstants.CREDITCHECK_LE_PREAPPROVED_FLAG_ERROR,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
		}
		return enable;

	}

	public void triggerCreditCheckStatusChangeMail(String portalTransactionId) throws TclCommonException {
		String orderCode = null;
		String accountName = null;
		String customerEmail = null;
		String opportunityName = null;
		String securityDepositAmount = null;
		if (StringUtils.isNotBlank(portalTransactionId)) {
			orderCode = portalTransactionId.replace(SFDCConstants.OPTIMUS, CommonConstants.EMPTY);
		}

		List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_QuoteCode(orderCode);
		if (quoteToLeList != null && !quoteToLeList.isEmpty()) {

			List<QuoteLeAttributeValue> quoteLeAttributeValueLegalEntity = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLeList.get(0),
							LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
			if (quoteLeAttributeValueLegalEntity != null && !quoteLeAttributeValueLegalEntity.isEmpty())
				accountName = quoteLeAttributeValueLegalEntity.get(0).getAttributeValue();

			User users = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
			if (users != null) {
				LOGGER.info("Logged in username :: {} ", users.getUsername());
				customerEmail = users.getEmailId();
			}
			
			

			if (Objects.nonNull(quoteToLeList.get(0).getTpsSfdcSecurityDepositAmount())) {

				securityDepositAmount = quoteToLeList.get(0).getTpsSfdcSecurityDepositAmount().toString();
			} else
				securityDepositAmount = "0";

			opportunityName = "Optimus Opportunity -" + quoteToLeList.get(0).getQuote().getId();
			LOGGER.info("customer email - {}, accountName - {}, status {}", customerEmail, accountName, quoteToLeList.get(0).getTpsSfdcStatusCreditControl());
			
			//Testing purpose - remove after testing
			if(appEnv.equalsIgnoreCase("DEV")) {
				customerEmail = "anne.fernando@tatacommunications.com";
				LOGGER.info("Resetting to address for testing purpose");
			}
				
			
			notificationService.mailNotificationForSfdcCreditCheck(customerEmail, accountName, opportunityName,
					quoteToLeList.get(0).getTpsSfdcOptyId(), quoteToLeList.get(0).getTpsSfdcStatusCreditControl(),
					quoteToLeList.get(0).getTpsSfdcCreditRemarks(), securityDepositAmount,
					appHost + quoteDashBoardRelativeUrl);
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}

	}

	public void triggerAccountBlackListedMail(QuoteToLe quoteToLe) throws TclCommonException {
		String legalentity = null;
		String accountName = null;
		String leOwnerEmail = null;

		List<QuoteToLe> quoteToLeList = quoteToLeRepository.findByQuote_QuoteCode(quoteToLe.getQuote().getQuoteCode());
		if (quoteToLeList != null && !quoteToLeList.isEmpty()) {

			List<QuoteLeAttributeValue> quoteLeAttributeValueLegalEntity = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLeList.get(0),
							LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
			if (quoteLeAttributeValueLegalEntity != null && !quoteLeAttributeValueLegalEntity.isEmpty())
				legalentity = quoteLeAttributeValueLegalEntity.get(0).getAttributeValue();

			List<QuoteLeAttributeValue> quoteLeAttributeValueLeOwnerEmail = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLeList.get(0),
							LeAttributesConstants.LE_EMAIL.toString());
			if (quoteLeAttributeValueLeOwnerEmail != null && !quoteLeAttributeValueLeOwnerEmail.isEmpty())
				leOwnerEmail = quoteLeAttributeValueLeOwnerEmail.get(0).getAttributeValue();

			accountName = quoteToLe.getQuote().getCustomer().getCustomerName();
			
			if(appEnv.equalsIgnoreCase("DEV")) {
				leOwnerEmail = "anne.fernando@tatacommunications.com";
				LOGGER.info("Resetting the le owner email for testing purpose");
			}

			notificationService.mailNotificationForBlacklistedAccount(legalentity, accountName, leOwnerEmail);
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * @author MBEDI
	 * @param quoteId
	 * @param quoteLeId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public CustomerLeVO getCustomerLeDetailsForCreditCheck(Integer quoteId, Integer quoteLeId, Integer customerLeId)
			throws TclCommonException {

		if (quoteLeId == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}

		Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(quoteLeId);
		CustomerLeVO customerLeDetails = new CustomerLeVO();
		try {
			if (optionalQuoteLe.isPresent()) {

				QuoteToLeProductFamily quoteLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_Id(optionalQuoteLe.get().getId());
				String productName = quoteLeProductFamily.getMstProductFamily().getName() != null
						? quoteLeProductFamily.getMstProductFamily().getName()
						: "";

				LOGGER.info("Sending the customerLeIds as {}", customerLeId);
				LOGGER.info("MDC Filter token value in before Queue call creditCheckBasedOnPreapprovedValue {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils.sendAndReceive(customerLeCreditCheckQueue,
						String.valueOf(customerLeId).concat(",").concat(productName));
				LOGGER.info("Response : " + response);
				if (response != null) {

					customerLeDetails = (CustomerLeVO) Utils.convertJsonToObject(response, CustomerLeVO.class);
				}

			} else {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customerLeDetails;

	}

	public QuoteLeCreditCheckAudit checkAuditForAlreadyApprovedValues(QuoteToLe quoteLe, String sfdcCuId, String status)
			throws TclCommonException {
		QuoteLeCreditCheckAudit quoteLeCreditCheckAudit = null;
		try {
			LOGGER.info("Input values QuoteLeId {}, sfdcCuId {}, status {}", quoteLe, sfdcCuId, status);
			List<QuoteLeCreditCheckAudit> quoteLeCreditCheckAuditList = quoteLeCreditCheckAuditRepository
					.findByQuoteToLeAndTpsSfdcCuIdAndTpsSfdcCreditCheckStatus(quoteLe, sfdcCuId, status);
			if (Objects.nonNull(quoteLeCreditCheckAuditList) && !quoteLeCreditCheckAuditList.isEmpty()) {
				quoteLeCreditCheckAudit = Collections.max(quoteLeCreditCheckAuditList,
						Comparator.comparing(audit -> audit.getTpsSfdcApprovedMrc()));
				LOGGER.info("Approved MRC {}, approved NRC {}, status {}, Id {}",
						quoteLeCreditCheckAudit.getTpsSfdcApprovedMrc(),
						quoteLeCreditCheckAudit.getTpsSfdcApprovedNrc(),
						quoteLeCreditCheckAudit.getTpsSfdcCreditCheckStatus(), quoteLeCreditCheckAudit.getId());
			}

		} catch (Exception e) {
			LOGGER.info("Exception in checkAuditForAlreadyApprovedValues {}", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteLeCreditCheckAudit;
	}

	@Transactional
	public String triggerCreditCheck(Integer customerLegalEntityId,
			Optional<QuoteToLe> optionalQuoteToLe, CustomerLeDetailsBean customerLeAndBillingDetails, Integer oldCustomerLegalEntityId) throws TclCommonException {
		Boolean variationMatrixCheck;
		LOGGER.info("customer Le id in request {}", customerLegalEntityId);
		
		if (customerLeAndBillingDetails.getBlacklistStatus() == null || (customerLeAndBillingDetails.getBlacklistStatus() != null
				&& CommonConstants.Y.equalsIgnoreCase(customerLeAndBillingDetails.getBlacklistStatus()))) {
					LOGGER.info("Account has been blacklisted, triggering blacklisted account email");
					triggerAccountBlackListedMail(optionalQuoteToLe.get());
					throw new TclCommonException(ExceptionConstants.BLACKLISTED_ACCOUNT_ERROR,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}

		if (customerLeAndBillingDetails.getCreditPreapprovedFlag() != null
				&& CommonConstants.Y.equalsIgnoreCase(customerLeAndBillingDetails.getCreditPreapprovedFlag())) {

		
				if((Objects.nonNull(customerLeAndBillingDetails.getPreapprovedPaymentTerm()))
						&& (Objects.nonNull(customerLeAndBillingDetails.getPreapprovedBillingMethod()))) {
				if (Objects.isNull(customerLeAndBillingDetails.getPreapprovedMrc())
						|| (Objects.isNull(customerLeAndBillingDetails.getPreapprovedNrc()))
						|| (Objects.isNull(customerLeAndBillingDetails.getPreapprovedPaymentTerm()))
						|| (Objects.isNull(customerLeAndBillingDetails.getPreapprovedBillingMethod()))) {
					LOGGER.info("Some data is null for cuLe sfdc id - {}, sending for manual check by default", customerLeAndBillingDetails.getAccounCuId());
					throw  new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID, ResponseResource.R_CODE_ERROR);
				}
				} else if(Objects.isNull(customerLeAndBillingDetails.getPreapprovedMrc())
						|| (Objects.isNull(customerLeAndBillingDetails.getPreapprovedNrc()))) {
					LOGGER.info("Preapproved mrc/nrc is null for cuLe sfdc id - {}, sending for manual check by default", customerLeAndBillingDetails.getAccounCuId());
					throw  new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID, ResponseResource.R_CODE_ERROR);
					
				} 

				if (Objects.isNull(oldCustomerLegalEntityId) || (Objects
						.nonNull(customerLegalEntityId)
						&& Objects.nonNull(oldCustomerLegalEntityId)
						&& customerLegalEntityId.equals(oldCustomerLegalEntityId))) {
					if (Objects.isNull(optionalQuoteToLe.get().getPreapprovedOpportunityFlag())
							|| (Objects.nonNull(optionalQuoteToLe.get().getPreapprovedOpportunityFlag())
									&& optionalQuoteToLe.get().getPreapprovedOpportunityFlag()
											.equals(CommonConstants.BACTIVE))) {
						LOGGER.info("same legal entity, pre approved flag value {}",
								optionalQuoteToLe.get().getPreapprovedOpportunityFlag());

						initiatePreapprovedCheck(optionalQuoteToLe, customerLeAndBillingDetails);

					} else if ((Objects.nonNull(optionalQuoteToLe.get().getPreapprovedOpportunityFlag())
							&& optionalQuoteToLe.get().getPreapprovedOpportunityFlag()
									.equals(CommonConstants.BDEACTIVATE))) {

						LOGGER.info("same legal entity, pre approved flag value in else {}, credit check status {}",
								optionalQuoteToLe.get().getPreapprovedOpportunityFlag(),
								optionalQuoteToLe.get().getTpsSfdcStatusCreditControl());
						if (Objects.nonNull(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl())
								&& (CommonConstants.POSITIVE
										.equalsIgnoreCase(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl()))) {
							if ((!optionalQuoteToLe.get().getTpsSfdcApprovedMrc()
									.equals(optionalQuoteToLe.get().getFinalMrc()))
									|| (!optionalQuoteToLe.get().getTpsSfdcApprovedNrc()
											.equals(optionalQuoteToLe.get().getFinalNrc()))) { 
								// Variation Matrix
								LOGGER.info("Triggering variation matrix check");
								variationMatrixCheck = creditCheckWithVariationMatrix(
										optionalQuoteToLe.get().getQuote().getId(), optionalQuoteToLe.get().getId(),
										customerLeAndBillingDetails.getAccounCuId());
								LOGGER.info("variationMatrixCheck evaluated value {}", variationMatrixCheck);
								if (variationMatrixCheck) {
									optionalQuoteToLe.get().setVariationApprovedFlag(CommonConstants.BDEACTIVATE);
									optionalQuoteToLe.get()
											.setTpsSfdcStatusCreditControl(CommonConstants.PENDING_CREDIT_ACTION);
									optionalQuoteToLe.get().setCreditCheckTriggered(CommonConstants.BACTIVE);
									quoteToLeRepository.save(optionalQuoteToLe.get());
								} else {
									optionalQuoteToLe.get().setVariationApprovedFlag(CommonConstants.BACTIVE);
									optionalQuoteToLe.get().setCreditCheckTriggered(CommonConstants.BDEACTIVATE);
									optionalQuoteToLe.get()
											.setTpsSfdcApprovedMrc(optionalQuoteToLe.get().getFinalMrc());
									optionalQuoteToLe.get()
											.setTpsSfdcApprovedNrc(optionalQuoteToLe.get().getFinalNrc());
									quoteToLeRepository.save(optionalQuoteToLe.get());

								}
							} 

						} else if (Objects.nonNull(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl())
								&& (CommonConstants.PENDING_CREDIT_ACTION
										.equalsIgnoreCase(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl()))) {

							initiatePreapprovedCheck(optionalQuoteToLe, customerLeAndBillingDetails);
						}

					}
				} else if (Objects.nonNull(customerLegalEntityId)
						&& Objects.nonNull(oldCustomerLegalEntityId)
						&& !customerLegalEntityId.equals(oldCustomerLegalEntityId)) {

					initiatePreapprovedCheck(optionalQuoteToLe, customerLeAndBillingDetails);
					if ((Objects.nonNull(optionalQuoteToLe.get().getPreapprovedOpportunityFlag())
									&& optionalQuoteToLe.get().getPreapprovedOpportunityFlag()
											.equals(CommonConstants.BACTIVE))) {
						LOGGER.info("different legal entity, pre approved flag value {}",
								optionalQuoteToLe.get().getPreapprovedOpportunityFlag());

						initiatePreapprovedCheck(optionalQuoteToLe, customerLeAndBillingDetails);

					} else if (optionalQuoteToLe.get().getPreapprovedOpportunityFlag().equals(CommonConstants.BDEACTIVATE)) {
						
						LOGGER.info("different legal entity, pre approved flag value in else {}, credit check status {}",
								optionalQuoteToLe.get().getPreapprovedOpportunityFlag(),
								optionalQuoteToLe.get().getTpsSfdcStatusCreditControl());
						if (Objects.nonNull(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl())
								&& (CommonConstants.POSITIVE
										.equalsIgnoreCase(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl()))) {
							if ((!optionalQuoteToLe.get().getTpsSfdcApprovedMrc()
									.equals(optionalQuoteToLe.get().getFinalMrc()))
									|| (!optionalQuoteToLe.get().getTpsSfdcApprovedNrc()
											.equals(optionalQuoteToLe.get().getFinalNrc()))) { // Variation Matrix
								LOGGER.info("Triggering variation matrix check");
						variationMatrixCheck = creditCheckWithVariationMatrix(
								optionalQuoteToLe.get().getQuote().getId(), optionalQuoteToLe.get().getId(),
								customerLeAndBillingDetails.getAccounCuId());
						LOGGER.info("Legal entity changed, variationMatrix check value {}", variationMatrixCheck);
						if (variationMatrixCheck) {
							optionalQuoteToLe.get().setVariationApprovedFlag(CommonConstants.BDEACTIVATE);
							optionalQuoteToLe.get()
									.setTpsSfdcStatusCreditControl(CommonConstants.PENDING_CREDIT_ACTION);
							optionalQuoteToLe.get().setCreditCheckTriggered(CommonConstants.BACTIVE);
							quoteToLeRepository.save(optionalQuoteToLe.get());
						} else {
							optionalQuoteToLe.get().setTpsSfdcStatusCreditControl(CommonConstants.POSITIVE);
							optionalQuoteToLe.get().setVariationApprovedFlag(CommonConstants.BACTIVE);
							optionalQuoteToLe.get().setCreditCheckTriggered(CommonConstants.BDEACTIVATE);
							optionalQuoteToLe.get().setTpsSfdcApprovedMrc(optionalQuoteToLe.get().getFinalMrc());
							optionalQuoteToLe.get().setTpsSfdcApprovedNrc(optionalQuoteToLe.get().getFinalNrc());
							quoteToLeRepository.save(optionalQuoteToLe.get());

						}
					}

				}  else if (Objects.nonNull(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl())
						&& (CommonConstants.PENDING_CREDIT_ACTION
								.equalsIgnoreCase(optionalQuoteToLe.get().getTpsSfdcStatusCreditControl()))) {

					initiatePreapprovedCheck(optionalQuoteToLe, customerLeAndBillingDetails);
				}
					}
				}
			

			

		} else if (customerLeAndBillingDetails.getCreditPreapprovedFlag() != null
				&& CommonConstants.N.equalsIgnoreCase(customerLeAndBillingDetails.getCreditPreapprovedFlag())) {
			LOGGER.info("In else - credit preapproved flag is N");		

			LOGGER.info(
					"Credit check triggered flag {}, status of credit control {}, customer legal entity id {}, quote le mrc {}, quote le nrc {}, sfdc mrc {}, sfdc nrc {}",
					optionalQuoteToLe.get().getCreditCheckTriggered(),
					optionalQuoteToLe.get().getTpsSfdcStatusCreditControl(),
					oldCustomerLegalEntityId, optionalQuoteToLe.get().getFinalMrc(),
					optionalQuoteToLe.get().getFinalNrc(), optionalQuoteToLe.get().getTpsSfdcApprovedMrc(),
					optionalQuoteToLe.get().getTpsSfdcApprovedNrc());

			if ((Objects.nonNull(optionalQuoteToLe.get().getCreditCheckTriggered())
					&& optionalQuoteToLe.get().getCreditCheckTriggered().equals(CommonConstants.BACTIVE))) {

				if (Objects.isNull(oldCustomerLegalEntityId)
						|| (Objects.nonNull(customerLegalEntityId)
								&& Objects.nonNull(oldCustomerLegalEntityId)
								&& customerLegalEntityId
										.equals(oldCustomerLegalEntityId))) {

					if ((!optionalQuoteToLe.get().getTpsSfdcApprovedMrc().equals(optionalQuoteToLe.get().getFinalMrc()))
							|| (!optionalQuoteToLe.get().getTpsSfdcApprovedNrc()
									.equals(optionalQuoteToLe.get().getFinalNrc()))) {
						LOGGER.info(
								"same legal entity , commercials changed , final mrc {}, final nrc {}, sfdc mrc {}, sfdc nrc {}",
								optionalQuoteToLe.get().getFinalMrc(), optionalQuoteToLe.get().getFinalNrc(),
								optionalQuoteToLe.get().getTpsSfdcApprovedMrc(),
								optionalQuoteToLe.get().getTpsSfdcApprovedNrc());
						updateQuoteToLeForNonPrepapproved(optionalQuoteToLe);

					}

				} else if ((Objects.nonNull(customerLegalEntityId)
						&& Objects.nonNull(oldCustomerLegalEntityId) && 
						!customerLegalEntityId.equals(oldCustomerLegalEntityId))) {
					LOGGER.info("legal entity changed, new le {}, quoteLe le {}", customerLegalEntityId,
							oldCustomerLegalEntityId);
					updateQuoteToLeForNonPrepapproved(optionalQuoteToLe);

				}
			} else if (Objects.nonNull(optionalQuoteToLe.get().getCreditCheckTriggered())
							&& optionalQuoteToLe.get().getCreditCheckTriggered().equals(CommonConstants.BDEACTIVATE)) {
					processCreditCheckLegalEntityChange(customerLegalEntityId, optionalQuoteToLe, oldCustomerLegalEntityId);
			} else if(Objects.isNull(optionalQuoteToLe.get().getCreditCheckTriggered())) {
				updateQuoteToLeForNonPrepapproved(optionalQuoteToLe);
			}

		} else {
			throw new TclCommonException(ExceptionConstants.CREDITCHECK_LE_PREAPPROVED_FLAG_ERROR,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
		
		return optionalQuoteToLe.get().getTpsSfdcStatusCreditControl();
	}

	private void processCreditCheckLegalEntityChange(Integer customerLegalEntityId,
			Optional<QuoteToLe> optionalQuoteToLe, Integer oldCustomerLegalEntityId) throws TclCommonException {
		if (Objects.nonNull(customerLegalEntityId)
				&& Objects.nonNull(oldCustomerLegalEntityId)
				&& customerLegalEntityId
						.equals(oldCustomerLegalEntityId)) {
			if ((!optionalQuoteToLe.get().getTpsSfdcApprovedMrc()
					.equals(optionalQuoteToLe.get().getFinalMrc()))
					|| (!optionalQuoteToLe.get().getTpsSfdcApprovedNrc()
							.equals(optionalQuoteToLe.get().getFinalNrc()))) {
				LOGGER.info(
						"same legal entity , commercials changed , final mrc {}, final nrc {}, sfdc mrc {}, sfdc nrc {}, credit check triggered {}",
						optionalQuoteToLe.get().getFinalMrc(), optionalQuoteToLe.get().getFinalNrc(),
						optionalQuoteToLe.get().getTpsSfdcApprovedMrc(),
						optionalQuoteToLe.get().getTpsSfdcApprovedNrc(),
						optionalQuoteToLe.get().getCreditCheckTriggered());
				updateQuoteToLeForNonPrepapproved(optionalQuoteToLe);

			}

		} else if (Objects.isNull(oldCustomerLegalEntityId)
				|| (Objects.nonNull(customerLegalEntityId)
						&& Objects.nonNull(oldCustomerLegalEntityId)
						&& !customerLegalEntityId
								.equals(oldCustomerLegalEntityId))) {
			LOGGER.info("legal entity changed, new le {}, quoteLe le {}, credit check triggerd flag {}",
					customerLegalEntityId, oldCustomerLegalEntityId,
					optionalQuoteToLe.get().getCreditCheckTriggered());
			updateQuoteToLeForNonPrepapproved(optionalQuoteToLe);

		}
	}

	private void updateQuoteToLeForNonPrepapproved(Optional<QuoteToLe> optionalQuoteToLe)
			throws TclCommonException {
		if (optionalQuoteToLe.isPresent()) {
			optionalQuoteToLe.get().setTpsSfdcApprovedMrc(optionalQuoteToLe.get().getFinalMrc());
			optionalQuoteToLe.get().setTpsSfdcApprovedNrc(optionalQuoteToLe.get().getFinalNrc());
			optionalQuoteToLe.get().setPreapprovedOpportunityFlag(CommonConstants.BDEACTIVATE);
			optionalQuoteToLe.get().setTpsSfdcStatusCreditControl(CommonConstants.PENDING_CREDIT_ACTION);
			optionalQuoteToLe.get().setCreditCheckTriggered(CommonConstants.BACTIVE);
			optionalQuoteToLe.get().setTpsSfdcCreditLimit(2*optionalQuoteToLe.get().getFinalMrc());
			quoteToLeRepository.save(optionalQuoteToLe.get());

		}
	}

	private String initiatePreapprovedCheck(Optional<QuoteToLe> optionalQuoteToLe,
			CustomerLeDetailsBean customerLeAndBillingDetail) throws TclCommonException {
		// Preapproved -check
		CustomerLeVO customerLeAndBillingDetails = creditCheckBasedOnPreapprovedValue(
				optionalQuoteToLe.get().getQuote().getId(), optionalQuoteToLe.get().getId(),
				customerLeAndBillingDetail);

		optionalQuoteToLe.get().setTpsSfdcApprovedMrc(optionalQuoteToLe.get().getFinalMrc());
		optionalQuoteToLe.get().setTpsSfdcApprovedNrc(optionalQuoteToLe.get().getFinalNrc());
		optionalQuoteToLe.get().setTpsSfdcCreditLimit(2 * optionalQuoteToLe.get().getFinalMrc());
		quoteToLeRepository.save(optionalQuoteToLe.get());

		return customerLeAndBillingDetails.getTpsSfdcStatusCreditControl();
	}

	public CreditCheckStatusResponse getCreditCheckStatusFromQuoteLe(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		CreditCheckStatusResponse creditCheckStatusResponse = new CreditCheckStatusResponse();
		String status = null;
		String productName = null;
		if(Objects.isNull(quoteLeId))
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		
		
		
		Optional<QuoteToLe> quoteToLeOptional = quoteToLeRepository.findById(quoteLeId);
		if(quoteToLeOptional.isPresent()) {
			if(quoteToLeOptional.get().getQuote().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
				productName = IzosdwanCommonConstants.IZOSDWAN_NAME;
			}else if(quoteToLeOptional.get().getQuote().getQuoteCode().startsWith(CommonConstants.UCAAS_WEBEX)){
				QuoteToLeProductFamily quoteLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLeOptional.get().getId()).stream()
						.filter(quoteToLeProductFamily -> quoteToLeProductFamily.getMstProductFamily().getName().equals(CommonConstants.UCAAS)).findAny().get();
				productName = quoteLeProductFamily.getMstProductFamily().getName();
			}else {
				QuoteToLeProductFamily quoteLeProductFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLe_Id(quoteToLeOptional.get().getId());
				productName = quoteLeProductFamily.getMstProductFamily().getName() != null
						? quoteLeProductFamily.getMstProductFamily().getName()
						: "";
			}
			LOGGER.info("Sending the customerLeIds as {}", quoteToLeOptional.get().getErfCusCustomerLegalEntityId());
			LOGGER.info("MDC Filter token value in before Queue call creditCheckBasedOnPreapprovedValue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeCreditCheckQueue, String.valueOf(quoteToLeOptional.get().getErfCusCustomerLegalEntityId()).concat(",").concat(productName));
			LOGGER.info("Response from customerLeCreditCheckQueue: " +response);
			if(response != null ) {
				CustomerLeVO customerLeDetails = (CustomerLeVO) Utils.convertJsonToObject(response,
						CustomerLeVO.class);
				if(customerLeDetails.getCreditPreapprovedFlag() == null)
					throw new TclCommonException(ExceptionConstants.CREDITCHECK_LE_PREAPPROVED_FLAG_ERROR,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				if(CommonConstants.Y.equalsIgnoreCase(customerLeDetails.getBlacklistStatus())) {
					throw new TclCommonException(ExceptionConstants.BLACKLISTED_ACCOUNT_ERROR,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}
				
				if(Objects.nonNull(customerLeDetails.getCreditPreapprovedFlag()) && CommonConstants.Y.equalsIgnoreCase(customerLeDetails.getCreditPreapprovedFlag())) {
				
				if((Objects.nonNull(customerLeDetails.getPreapprovedPaymentTerm()))
						&& (Objects.nonNull(customerLeDetails.getPreapprovedBillingMethod()))) {
				if (Objects.isNull(customerLeDetails.getPreapprovedMrc())
						|| (Objects.isNull(customerLeDetails.getPreapprovedNrc()))
						|| (Objects.isNull(customerLeDetails.getPreapprovedPaymentTerm()))
						|| (Objects.isNull(customerLeDetails.getPreapprovedBillingMethod()))) {
					LOGGER.info("Some data is null for cuLe sfdc id - {}, sending for manual check by default", customerLeDetails.getAccountId());
					throw  new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID, ResponseResource.R_CODE_ERROR);
				}
				} else if(Objects.isNull(customerLeDetails.getPreapprovedMrc())
						|| (Objects.isNull(customerLeDetails.getPreapprovedNrc()))) {
					LOGGER.info("Preapproved mrc/nrc is null for cuLe sfdc id - {}, sending for manual check by default", customerLeDetails.getAccountId());
					throw  new TclCommonException(ExceptionConstants.CREDITCHECK_DATA_INVALID, ResponseResource.R_CODE_ERROR);
					
				} 
				}
				
				
			}
			creditCheckStatusResponse.setCreditCheckStatus(quoteToLeOptional.get().getTpsSfdcStatusCreditControl());
			creditCheckStatusResponse.setPreapprovedOpportunityFlag(CommonConstants.BACTIVE.equals(quoteToLeOptional.get().getPreapprovedOpportunityFlag())?true:false);
		}
		return creditCheckStatusResponse;
	}
	
	
	public void resetCreditCheckFields(QuoteToLe quoteToLe) {
		LOGGER.info("resetting credit check fields");
		List<QuoteLeCreditCheckAudit> auditList = quoteLeCreditCheckAuditRepository.findByQuoteToLe_id(quoteToLe.getId());
		if(auditList != null && !auditList.isEmpty()) {
			quoteLeCreditCheckAuditRepository.deleteAll(auditList);
		}
		quoteToLe.setCreditCheckTriggered(null);
		quoteToLe.setPreapprovedOpportunityFlag(null);
		quoteToLe.setTpsSfdcApprovedBy(null);
		quoteToLe.setTpsSfdcApprovedMrc(null);
		quoteToLe.setTpsSfdcApprovedNrc(null);
		quoteToLe.setTpsSfdcCreditApprovalDate(null);
		quoteToLe.setTpsSfdcCreditRemarks(null);
		quoteToLe.setTpsSfdcDifferentialMrc(null);
		quoteToLe.setTpsSfdcReservedBy(null);
		quoteToLe.setTpsSfdcSecurityDepositAmount(null);
		quoteToLe.setTpsSfdcStatusCreditControl(null);
		quoteToLe.setVariationApprovedFlag(null);
		quoteToLe.setTpsSfdcCreditLimit(null);
		quoteToLeRepository.save(quoteToLe);
		
		List<String> statuses = new ArrayList<>();
		statuses.add(SfdcServiceStatus.NEW.toString());
		statuses.add(SfdcServiceStatus.INPROGRESS.toString());
		List<ThirdPartyServiceJob> jobsList = thirdPartyServiceJobsRepository.findByRefIdAndServiceStatusInAndThirdPartySource(quoteToLe.getQuote().getQuoteCode(),statuses,
				ThirdPartySource.CREDITCHECK.toString());
		if (!jobsList.isEmpty()) {
			jobsList.stream()
					.forEach(inProgressJob -> {
						inProgressJob.setServiceStatus(SfdcServiceStatus.CANCELLED.toString());
						inProgressJob.setIsComplete(CommonConstants.BACTIVE);
						thirdPartyServiceJobsRepository.save(inProgressJob);						
					});
		}
	}

	private void updateLeAttrbuteMACD(List<QuoteLeAttributeValue> quoteLeAttributeValues, double attributeValueDMRC, String attributeName) {
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setAttributeValue(String.valueOf(attributeValueDMRC));
				attrVal.setDisplayValue(attributeName);
				quoteLeAttributeValueRepository.save(attrVal);

			});
		}


	}
	private void constructLegaAttributeMACD(String attributeName, QuoteToLe quoteTole,
											Double attributeValueDMRC) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteTole, attributeName);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(String.valueOf(attributeValueDMRC));
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForCreditCheck(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			attributeValue.setQuoteToLe(quoteTole);
			attributeValue.setDisplayValue(attributeName);
			quoteLeAttributeValueRepository.save(attributeValue);
		} else {
			updateLeAttrbuteMACD(quoteLeAttributeValues, attributeValueDMRC, attributeName);
		}

	}

	private MstOmsAttribute getMstAttributeMasterForCreditCheck(String attrName) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName, (byte) 1);
		if (mstOmsAttributes != null && !mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(Utils.getSource());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(attrName);
			mstOmsAttribute.setDescription(attrName);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}



}
