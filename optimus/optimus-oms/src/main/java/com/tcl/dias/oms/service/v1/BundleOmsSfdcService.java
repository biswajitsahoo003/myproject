package com.tcl.dias.oms.service.v1;


import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_DOMESTIC_ORDER;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.CgwServiceAreaMatricBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.sfdc.bean.BundledSfdcOppurtunityRequest;
import com.tcl.dias.common.sfdc.bean.CreateRequestV1Sfdc;
import com.tcl.dias.common.sfdc.bean.Opportunity;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteOpportunityLocation;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.response.bean.BundledOpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductServicesResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductsserviceResponse;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanMssPricingRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.factory.OmsSfdcBundleInputMapperFactory;
import com.tcl.dias.oms.izosdwan.beans.ProductPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanQuoteService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.OmsSfdcBundleInputHandler;
import com.tcl.dias.oms.sfdc.service.OmsSfdcUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * 
 * @author vpachava
 *
 */
@Service
@Transactional
public class BundleOmsSfdcService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BundleOmsSfdcService.class);

	@Value("${application.env}")
	String appEnv;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${rabbitmq.opportunity.create}")
	String sfdcCreateOpty;

	@Value("${rabbitmq.opportunity.productservices}")
	String sfdcCreatePrdService;

	@Value("${rabbitmq.opportunity.updateproductservices}")
	String sfdcUpdatedService;

	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${sfdc.test.accountcuid}")
	String accountCuid;

	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	IllSiteRepository illSiteRepository;
	
	@Autowired
	QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository; 

	@Autowired
	OmsSfdcBundleInputMapperFactory factory;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Value("${rabbitmq.customer.account.manager.email}")
	String customerAccountManagerName;

	@Autowired
	QuoteIzoSdwanMssPricingRepository  quoteIzoSdwanMssPricingRepository;
	
	@Autowired
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;
	
	@Value("${rabbitmq.opportunity.deleteproductservices}")
	String sfdcProductDeleteService;
	
	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;
	
	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;
	
	@Value("${rabbitmq.location.detail}")
	String locationQueue;
	
	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	QuoteIzosdwanCgwDetailRepository quoteIzosdwanCgwDetailRepository;
	
	@Value("${rabbitmq.product.cgw.sam.city}")
	String cgwSamByCityQueue;
	
	@Value("${rabbitmq.opportunity.site}")
	String sfdcUpdateSite;
	
	@Autowired
	OmsSfdcUtilService omsSfdcUtilService;
	
	@Value("${application.test.emails}")
	String[] applicationTestMails;
	
	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;
	
	
	
	@Transactional
	public void processCreateOpty(QuoteToLe quoteToLe, String productName) throws TclCommonException {
		LOGGER.info("OmsSfdcService.processCreateOpty method invoked");
		if (StringUtils.isBlank(quoteToLe.getTpsSfdcOptyId())) {
			// Get the OrderLeAttributes
			LOGGER.info("SfdcOptyId not exists");
			Opportunity opportunity = new Opportunity();
			BundledSfdcOppurtunityRequest request = new BundledSfdcOppurtunityRequest();
			CreateRequestV1Sfdc requestV1 = new CreateRequestV1Sfdc();
			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				LOGGER.info("Quote exists");
				String orderCode = quote.getQuoteCode();
				LOGGER.info("orderCode::" + orderCode);
				LOGGER.info("quote::" + quote.getId());
				opportunity.setName("Optimus Opportunity -" + quote.getId());
				opportunity.setDescription("Creating opportunity for the order " + quote.getId() + " on " + new Date());
				opportunity.setReferralToPartnerC(SFDCConstants.NO);
				opportunity.setIllAutoCreationC(CommonConstants.EMPTY);
				opportunity.setOrderCategoryC(SFDCConstants.CAT_3);
				opportunity.setType(SFDCConstants.NEWBUNDLE);
				opportunity.setSubTypeC(CommonConstants.EMPTY);
				opportunity.setPortalTransactionIdC(SFDCConstants.OPTIMUS + orderCode);
				opportunity.setOpportunityClassificationC(SFDCConstants.SELL_TO);
				opportunity.setTataBillingEntityC(SFDCConstants.TATACOMLMT);
				String termsAndMonths = "12";
				opportunity.setTermsOfMonthsC(termsAndMonths);
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				opportunity.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
				String currency = SFDCConstants.INR;
				if (quoteToLe.getCurrencyCode() != null) {
					currency = quoteToLe.getCurrencyCode();
				}
				opportunity.setCurrencyIsoCode(currency);
				opportunity.setStageName(SFDCConstants.IDENTIFIED_OPTY_STAGE);
				opportunity.setLeadTimeToRfsc(termsAndMonths);
				opportunity.setCustomerChurnedC(SFDCConstants.NO);
				opportunity.setBillingFrequencyC(SFDCConstants.MONTHLY);
				opportunity.setBillingMethodC(SFDCConstants.ADVANCE);
				opportunity.setPaymentTermC("7 days from generation of Invoice");
				opportunity.setSelectProductTypeC("IZO SDWAN");
				opportunity.setCofTypeC(SFDCConstants.OPTIMUS_COF);
				opportunity.setMigrationSourceSystemC(SFDCConstants.OPTIMUS);
				if (quoteToLe.getErfServiceInventoryTpsServiceId() != null) {
					opportunity.setCurrentCircuitServiceIdC(quoteToLe.getErfServiceInventoryTpsServiceId());
				}
				opportunity.setEffectiveDateOfChangeC(cal.getTime());
				opportunity.setPreviousMrcC("0.0");
				opportunity.setPreviousNrcC("0.0");
				opportunity.setBundledOrderTypeOneC(CommonConstants.NEW);
				opportunity.setBundledProductOneC("IZO SDWAN");
				opportunity.setBundledSubOrderTypeOneC(SFDCConstants.NEW_ORDER);
				opportunity.setOpportunitySpecificationC(IzosdwanCommonConstants.HYBRID_OPPORTUNITY);
				LOGGER.info("Product name for create opportunity method {} ", productName);
				if (productName != null) {
					OmsSfdcBundleInputHandler handler = factory.getInstance(productName);
					if (handler != null) {
						handler.getOpportunityBean(quoteToLe, opportunity);
					}

					LOGGER.info("Opportunity bean for type field is set as ----->  {} ", opportunity.getType());

					LOGGER.info("opportunitybean" + opportunity.getType() + " " + opportunity.getSubTypeC());

					if (appEnv.equals(SFDCConstants.PROD) || Utils.isTestAccount(applicationTestMails)) {
						String cuid=getCustomerLeCuid(quoteToLe.getErfCusCustomerLegalEntityId());
						requestV1.setAccountCuid(cuid);
						requestV1.setCustomerContractEntity(cuid);
					} else {
						requestV1.setAccountCuid(accountCuid);
						requestV1.setCustomerContractEntity(accountCuid);
					}
					
					if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
						if (appEnv.equals(SFDCConstants.PROD)) {
							LOGGER.info("OPPORTUNITY USER EMAIL ID {}", Utils.getSource());
							requestV1.setOwnerName(Utils.getSource());
						} else {
							requestV1.setOwnerName(null);
						}
					} else {
						if (appEnv.equals(SFDCConstants.PROD)) {
							String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
							String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
							requestV1.setOwnerName(name);
						} else {
							requestV1.setOwnerName(null);
						}
					}
					requestV1.setProductBundleName("SDWAN with MSS");
					requestV1.setOpportunity(opportunity);
					request.setCreateRequestV1(requestV1);
					quoteToLeRepository.save(quoteToLe);
					persistSfdcServiceJob(quote.getQuoteCode(), sfdcCreateOpty, Utils.convertObjectToJson(request),
							CommonConstants.BACTIVE, SFDCConstants.CREATE_OPTY, getSequenceNumber(SFDCConstants.CREATE_OPTY));
				}
			}
			// }
		}
		LOGGER.info("OmsSfdcService.processCreateOpty method exited");
	}

	/**
	 * persistSfdcServiceJob
	 *
	 * @param opportunityBean
	 * @param quote
	 * @throws TclCommonException
	 */
	public void persistSfdcServiceJob(String refId, String queueName, String payload, Byte isComplete,
			String serviceType, Integer seqNum) {
		ThirdPartyServiceJob thirdServiceJob = new ThirdPartyServiceJob();
		thirdServiceJob.setCreatedBy(Utils.getSource());
		thirdServiceJob.setCreatedTime(new Date());
		thirdServiceJob.setQueueName(queueName);
		thirdServiceJob.setRefId(refId);
		thirdServiceJob.setRequestPayload(payload);
		thirdServiceJob.setSeqNum(seqNum);
		thirdServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
		thirdServiceJob.setThirdPartySource(ThirdPartySource.SFDC.toString());
		thirdServiceJob.setServiceType(serviceType);
		thirdServiceJob.setIsComplete(isComplete);
		thirdPartyServiceJobsRepository.save(thirdServiceJob);
	}

	/**
	 * getSequenceNumber
	 */

	public Integer getSequenceNumber(String stage) {
		switch (stage) {
		case SFDCConstants.CREATE_OPTY:
			return 1;
		case SFDCConstants.CREATE_PRODUCT:
			return 2;
		case SFDCConstants.UPDATE_PRODUCT:
			return 3;
		case SFDCConstants.DELETE_PRODUCT:
			return 4;
		case SFDCConstants.PROPOSAL_SENT:
			return 5;
		case SFDCConstants.VERBAL_AGREEMENT_STAGE:
			return 6;
		case SFDCConstants.UPDATE_SITE:
			return 7;
		case SFDCConstants.CLOSED_WON_COF_RECI:
			return 8;
		case SFDCConstants.COF_WON_PROCESS_STAGE:
			return 9;
		case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES:
			return 10;
		case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND:
			return 11;
		case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT:
			return 12;
		case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS:
			return 13;
		case TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER:
			return 14;
		case TIGER_SERVICE_TYPE_DOMESTIC_ORDER:
			return 15;
		case SFDCConstants.CREATE_ENTITY:
			return 16;
		case SFDCConstants.CREATE_FEASIBILITY:
			return 4;
		case SFDCConstants.UPDATE_FEASIBILITY:
			return 6;
		case SFDCConstants.CLOSED_DROPPED:
			return 17;
		case SFDCConstants.OPEN_CLOSED_BCR:
			return 3;
		case SFDCConstants.UPDATE_INPROGRESS_BCR:
			return 4;
		default:
			return 0;
		}
	}

	public void createOpportunityResponse(BundledOpportunityResponseBean response) throws TclCommonException {
		LOGGER.info("inside createOpportunityResponse:: {}", response.toString());
		String sfdcOpId = response.getCustomOptyId();
		if (response.getOpportunity() != null) {
			LOGGER.info("Inside processSfd create response - opportunity id {}", sfdcOpId);
			String quoteCode = response.getOpportunity().getPortalTransactionIdC();
			if (StringUtils.isNotBlank(quoteCode)) {
				quoteCode = quoteCode.replace(SFDCConstants.OPTIMUS, CommonConstants.EMPTY);
			}
			boolean isCancellation = false;
			if (quoteCode.contains("-C")) {
				LOGGER.info("This opty is a response for the Cancellation");
				isCancellation = true;
				quoteCode = quoteCode.replace("-C", CommonConstants.EMPTY);
			}
			LOGGER.info("inside createOpportunityResponse - replaced orderCode {}", quoteCode);
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
			if (!isCancellation) {
				MstOmsAttribute mstOmsAttribute = getMstAttributeMaster(SFDCConstants.SFDC_STAGE, Utils.getSource());
				for (QuoteToLe quoteLe : quoteToLes) {
					LOGGER.info("inside processSfdcOpportunityCreateResponse - in for loop {}",
							quoteLe.getTpsSfdcOptyId());
					quoteLe.setTpsSfdcOptyId(sfdcOpId);
					LOGGER.info("Saved the Opty Id {} for orderCode {}", sfdcOpId, quoteCode);
					List<QuoteLeAttributeValue> quoteLeAttrvalues = quoteLeAttributeValueRepository
							.findByQuoteToLe_IdAndMstOmsAttribute_Id(quoteLe.getId(), mstOmsAttribute.getId());
					if (quoteLeAttrvalues.isEmpty()) {
						QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
						quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
						quoteLeAttributeValue.setAttributeValue(response.getOpportunity().getStageName());
						quoteLeAttributeValue.setDisplayValue(response.getOpportunity().getStageName());
						quoteLeAttributeValue.setQuoteToLe(quoteLe);
						quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
					}
					LOGGER.info("SFDC Termination request update in macd detail");
					if (Objects.nonNull(response.getOpportunity().getType())
							&& response.getOpportunity().getType().equalsIgnoreCase(SFDCConstants.TERMINATION)) {
						LOGGER.info("Termination - inside if loop");
						quoteLe.setStage(MACDConstants.TERMINATION_REQUEST_RECEIVED);
						quoteToLeRepository.save(quoteLe);
					}

					LOGGER.info("inside processSfdcOpportunityCreateResponse - after saving quoteToLe {}",
							quoteLe.getTpsSfdcOptyId());
					LOGGER.info("inside processSfdcOpportunityCreateResponse - before product service create call");
					// processProductServices(quoteLe, sfdcOpId);
					quoteLe.setTpsSfdcOptyId(sfdcOpId);// Saving the optyId
					quoteToLeRepository.save(quoteLe);
				}
				// Saving to Order if it exists
//				List<OrderToLe> orderToLes = orderToLeRepository.findByOrder_OrderCode(quoteCode);
//				orderToLes.forEach(orderToLe -> {
//					orderToLe.setTpsSfdcCopfId(sfdcOpId);
//					orderToLeRepository.save(orderToLe);
//				});
				com.tcl.dias.oms.entity.entities.Opportunity opportunity = opportunityRepository.findByUuid(quoteCode);
				if (Objects.nonNull(opportunity)) {
					opportunity.setTpsOptyId(sfdcOpId);
					opportunityRepository.save(opportunity);
				}
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteCode, SFDCConstants.CREATE_OPTY,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(response), sfdcOpId);
				LOGGER.info("Persisting the status of opty as success");
			} else {
				MstOmsAttribute mstOmsAttrubute = getMstAttributeMaster(SFDCConstants.C_SFDC_STAGE, Utils.getSource());
				for (QuoteToLe quoteLe : quoteToLes) {
					LOGGER.info("inside processSfdcOpportunityCreateResponse for cancellation - in for loop {}",
							quoteLe.getTpsSfdcOptyId());
					List<QuoteLeAttributeValue> quoteLeAttrvalues = quoteLeAttributeValueRepository
							.findByQuoteToLe_IdAndMstOmsAttribute_Id(quoteLe.getId(), mstOmsAttrubute.getId());
					if (quoteLeAttrvalues.isEmpty()) {
						QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
						quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttrubute);
						quoteLeAttributeValue.setAttributeValue(sfdcOpId);
						quoteLeAttributeValue.setDisplayValue(sfdcOpId);
						quoteLeAttributeValue.setQuoteToLe(quoteLe);
						quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
					}
			//		processProductServicesForCancellation(quoteLe, sfdcOpId);
				}
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteCode + "-C", SFDCConstants.CREATE_OPTY,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(response), sfdcOpId);
				LOGGER.info("Persisting the status of opty as success");
			}
		}

	}

	/**
	 * getMstAttributeMaster
	 *
	 * @param propName
	 * @param username
	 * @return
	 */

	private MstOmsAttribute getMstAttributeMaster(String propName, String username) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(propName,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}

		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(username);
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(propName);
			mstOmsAttribute.setDescription(propName);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	/**
	 * processProductServiceForSolution
	 * <p>
	 * for product service call
	 *
	 * @param quoteToLe
	 * @param productSolution
	 * @throws TclCommonException
	 */
	public void processProductServiceForSolution(QuoteToLe quoteToLe, ProductSolution productSolution,
			String sfdcOpportunityId,String ProductName,boolean isNew) throws TclCommonException {
		ProductServiceBean productServiceBean = new ProductServiceBean();
		// Get the QuoteLeAttributes
		productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
		// productServiceBean.setProductType(SFDCConstants.INTERNET_ACCESS_SERVICE.toString());//have
		// moved to product specific data
		String currency = SFDCConstants.INR;
		if (quoteToLe.getCurrencyCode() != null) {
			currency = quoteToLe.getCurrencyCode();
		}
		productServiceBean.setProductMRC(0.0);
		productServiceBean.setProductNRC(0.0);
		productServiceBean.setBigMachinesArc(0D);
		productServiceBean.setBigMachinesMrc(0D);
		productServiceBean.setBigMachinesNrc(0D);
		productServiceBean.setBigMachinesTcv(0D);
		productServiceBean.setProductFlavourC("Prime");
		productServiceBean.setCurrencyIsoCode(currency);
		
		if(ProductName.equals("GVPN") || ProductName.equals(IzosdwanCommonConstants.BYON_MPLS)) {
			productServiceBean.setProductLineOfBusiness("IPVPN");
			productServiceBean.setRecordTypeName("Global VPN");
			productServiceBean.setProductCategoryC("GVPN");
			productServiceBean.setOrderType(SFDCConstants.CHANGE_ORDER);
			if (quoteToLe.getQuote().getNsQuote() != null && quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {
				//productServiceBean.setOrderType(isNewOrder(ProductName,productSolution)?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
				productServiceBean.setOrderType(isNew?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
			}
			if(ProductName.equals(IzosdwanCommonConstants.BYON_MPLS)) {
				productServiceBean.setType("GVPN BYON");
			}
		}
		if(ProductName.equals("IAS") || ProductName.equals(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT)) {
			productServiceBean.setProductLineOfBusiness("IPVPN");
			productServiceBean.setRecordTypeName("Internet Access Service");
			productServiceBean.setProductCategoryC("IAS");
			
			if (quoteToLe.getQuote().getNsQuote() != null && quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {
				productServiceBean.setOrderType(isNew?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
				//productServiceBean.setOrderType(isNewOrder(ProductName,productSolution)?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
			}else {
				productServiceBean.setOrderType(SFDCConstants.CHANGE_ORDER);
			}
			if(ProductName.equals(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT)) {
				productServiceBean.setType("IAS BYON");
			}
		}
		if(ProductName.equals("Vproxy") ||ProductName.equals(IzosdwanCommonConstants.VUTM)) {
			productServiceBean.setProductLineOfBusiness("Voice");
			productServiceBean.setRecordTypeName("Managed Security Services");
			productServiceBean.setProductCategoryC("Managed Services");
			productServiceBean.setMssTypeC("Managed Firewalls (mFW)");
			productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
		}
		if(ProductName.equals("SDWAN")) {
			productServiceBean.setProductLineOfBusiness("IPVPN");
			productServiceBean.setRecordTypeName("IZO SDWAN");
			productServiceBean.setProductCategoryC("IZO SDWAN");
			productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
		}
		if(ProductName.equals("IP Transit")) {
			productServiceBean.setProductLineOfBusiness("IPVPN");
			productServiceBean.setRecordTypeName("IP Transit");
			productServiceBean.setProductCategoryC("IP Transit");
			productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
			if (quoteToLe.getQuote().getNsQuote() != null && quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {
				//productServiceBean.setOrderType(isNewOrder(ProductName,productSolution)?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
				productServiceBean.setOrderType(isNew?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
			}

		}
		if(ProductName.equals("IZO Internet WAN")) {
			productServiceBean.setProductLineOfBusiness("IPVPN");
			productServiceBean.setRecordTypeName("IZO Internet WAN");
			productServiceBean.setProductCategoryC("IZO Internet WAN");
			productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
			if (quoteToLe.getQuote().getNsQuote() != null && quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {
				//productServiceBean.setOrderType(isNewOrder(ProductName,productSolution)?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
				productServiceBean.setOrderType(isNew?SFDCConstants.NEW_ORDER:SFDCConstants.CHANGE_ORDER);
			}

		}
		productServiceBean.setQuoteToLeId(quoteToLe.getId());
		productServiceBean.setOpportunityId(sfdcOpportunityId);
		productServiceBean.setIsCancel(false);
		productServiceBean.setTermInMonthsC(quoteToLe.getTermInMonths().substring(0,2));
		productServiceBean.setSdwanProductName(ProductName);
		String familyName = getFamilyName(quoteToLe);
		LOGGER.info("Family Name for Create Product {}", familyName);
		if (familyName != null) {
			OmsSfdcBundleInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.getProductServiceInput(quoteToLe, productServiceBean, productSolution);

			}
		}
		productSolutionRepository.save(productSolution);
		LOGGER.info("product creation request:{}",Utils.convertObjectToJson(productServiceBean));
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcCreatePrdService,
				Utils.convertObjectToJson(productServiceBean),
				StringUtils.isNotBlank(sfdcOpportunityId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.CREATE_PRODUCT, getSequenceNumber(SFDCConstants.CREATE_PRODUCT));
	}

	/*
	 * private boolean isNewOrder(String productName, ProductSolution
	 * productSolution) { List<QuoteIzosdwanSite> sites =
	 * quoteIzosdwanSiteRepository.findByProductSolutionAndIzosdwanSiteProduct(
	 * productSolution,productName); boolean isNewOrder=true; for (QuoteIzosdwanSite
	 * quoteIzosdwanSite : sites) {
	 * LOGGER.info("Site Id : {}",quoteIzosdwanSite.getId());
	 * if(quoteIzosdwanSite.getErfServiceInventoryTpsServiceId()!=null) {
	 * isNewOrder=false; break; } } return isNewOrder; }
	 */

	protected String getFamilyName(QuoteToLe quoteToLe) {
		return quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
	}
	
	protected String getFamilyNameByProductName(String productName) {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName,
				CommonConstants.BACTIVE);
		if (mstProductFamily != null) {
			return mstProductFamily.getName();
		} else {
			return null;
		}
	}
	
	/**
	 * processProductServices- used to process the process service with sfdc end
	 * point
	 *
	 * @param quoteToLe
	 * @param optyId
	 * @throws TclCommonException
	 */
	public void processProductServices(QuoteToLe quoteToLe, String sfdcOpertunityId,String productName) throws TclCommonException {
		LOGGER.info("inside processSfdcOpportunityCreateResponse - after saving quoteToLe {}",
				quoteToLe.getTpsSfdcOptyId());
		List<QuoteToLeProductFamily> quoteToLeProdFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProdFamily) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			for (ProductSolution productSolution : productSolutions) {
				processProductServiceForSolution(quoteToLe, productSolution, sfdcOpertunityId,productName, false);
			}
		}
	}

	/**
	* processProductServices- used to process the process service with sfdc end
	* point
	*
	* @param quoteToLe
	* @param optyId
	* @throws TclCommonException
	*/
	public void processProductServices(Integer quoteToLeId ,String productName,boolean isNew) throws TclCommonException {
	Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
	if(quoteToLe.get()!=null) {
	String sfdcOpertunityId= quoteToLe.get().getTpsSfdcOptyId();
	LOGGER.info("inside processSfdcOpportunityCreateResponse - after saving quoteToLe {}",
	sfdcOpertunityId);
	List<QuoteToLeProductFamily> quoteToLeProdFamily = quoteToLeProductFamilyRepository
	.findByQuoteToLe(quoteToLeId);
	for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProdFamily) {
	List<ProductSolution> productSolutions = productSolutionRepository
	.findByQuoteToLeProductFamily(quoteToLeProductFamily);
	for (ProductSolution productSolution : productSolutions) {
	processProductServiceForSolution(quoteToLe.get(), productSolution, sfdcOpertunityId,productName,isNew);
	}
	}
	}
	}
	/**
	 * persistSfdcJobResponse
	 *
	 * @param opBean
	 * @param sfdcServiceJobs
	 */
	public void persistSfdcJobResponse(List<ThirdPartyServiceJob> sfdcServiceJobs, String serviceStatus,
			String responsePayload, String optyId) {
		for (ThirdPartyServiceJob sfdcServiceJob : sfdcServiceJobs) {
			sfdcServiceJob.setServiceStatus(serviceStatus);
			sfdcServiceJob.setResponsePayload(responsePayload);
			sfdcServiceJob.setUpdatedBy("admin");
			sfdcServiceJob.setUpdatedTime(new Date());
			sfdcServiceJob.setIsComplete(CommonConstants.BACTIVE);
			sfdcServiceJob.setTpsId(optyId);
			thirdPartyServiceJobsRepository.save(sfdcServiceJob);
		}
	}

	public void processProductServicesForCancella1tion(QuoteToLe quoteToLe, String sfdcOpertunityId)
			throws TclCommonException {
		LOGGER.info("inside processProductServicesForCancellation - after saving quoteToLe {}", sfdcOpertunityId);
		List<QuoteToLeProductFamily> quoteToLeProdFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteToLe.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProdFamily) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			for (ProductSolution productSolution : productSolutions) {
				processProductServiceForSolutionForCancel(quoteToLe, productSolution, sfdcOpertunityId);
			}
		}
	}

	public void processProductServiceForSolutionForCancel(QuoteToLe quoteToLe, ProductSolution productSolution,
			String sfdcOpportunityId) throws TclCommonException {
		ProductServiceBean productServiceBean = new ProductServiceBean();
// Get the QuoteLeAttributes
		productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
// productServiceBean.setProductType(SFDCConstants.INTERNET_ACCESS_SERVICE.toString());//have
// moved to product specific data
		String currency = SFDCConstants.INR;
		if (quoteToLe.getCurrencyCode() != null) {
			currency = quoteToLe.getCurrencyCode();
		}
		productServiceBean.setCurrencyIsoCode(currency);
		productServiceBean.setIsTrainingRequire(SFDCConstants.NO);
		productServiceBean.setIdcBandwidth(SFDCConstants.NO);
		productServiceBean.setMultiVRFSolution(SFDCConstants.NO);
		productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
		productServiceBean.setOpportunityId(sfdcOpportunityId);
		productServiceBean.setIsCancel(true);
		productServiceBean.setQuoteToLeId(quoteToLe.getId());
		String familyName = getFamilyName(quoteToLe);
		LOGGER.info("Family Name for Create Product {}", familyName);
		if (familyName != null) {
			OmsSfdcBundleInputHandler handler = factory.getInstance(familyName);
			if (handler != null) {
				handler.getProductServiceInput(quoteToLe, productServiceBean, productSolution);

			}
		}
		productSolutionRepository.save(productSolution);
		persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode() + "-C", sfdcCreatePrdService,
				Utils.convertObjectToJson(productServiceBean),
				StringUtils.isNotBlank(sfdcOpportunityId) ? CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE,
				SFDCConstants.CREATE_PRODUCT, getSequenceNumber(SFDCConstants.CREATE_PRODUCT));
	}

	/**
	 * processUpdateProduct
	 *
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processUpdateProduct(QuoteToLe quoteToLe)
			throws TclCommonException {
		LOGGER.info("Inside Process Update Product function of Bundle oms sfdc service !!!");
		QuotePricingDetailsBean quotePricingDetailsBean = null;
	
		if(quoteToLe.getQuote().getNsQuote()!=null && quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("Y")) {
			LOGGER.info("Check NS-Quote Condition : ---" +quoteToLe.getQuote().getNsQuote());
			quotePricingDetailsBean = izosdwanQuoteService.getPriceInformationForTheQuoteSfdcSdwan(quoteToLe.getQuote().getId());
		}else {
			quotePricingDetailsBean = izosdwanQuoteService.getPriceInformationForTheQuote(quoteToLe.getQuote().getId());
		}
		
		if (quoteToLe != null && quotePricingDetailsBean != null) {
			List<ProductPricingDetailsBean> productPricingDetailsBeans = quotePricingDetailsBean.getIzosdwan()
					.getProductPricingDetailsBeans();
			List<QuoteToLeProductFamily> quoteLeProdFamilyRepo = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			String productName = "";
			String sfdcProductAttribute = "";
			String type = "";
			String recordType = "";
			Double nrc;
			Double arc;
			
					
			String currencyIsoCode = quotePricingDetailsBean.getCurrency();// to update input currency in sfdc
			for (ProductPricingDetailsBean productPricingDetail : productPricingDetailsBeans) {
				productName = productPricingDetail.getProductName();
				LOGGER.info("Product Name {}", productName);
				 productPricingDetail.getServiceId();
		
				
                boolean isNew = false;
                if(quoteToLe.getQuote().getNsQuote()!=null && quoteToLe.getQuote().getNsQuote().equalsIgnoreCase("Y") && productPricingDetail.getOrderType() != null) {
                	isNew =	productPricingDetail.getOrderType().equals(MACDConstants.NEW)?true:false;
                }
                LOGGER.info("Boolean Check :"+isNew);
         
				String displayValue = CommonConstants.EMPTY;
				String displayValueProdName = CommonConstants.EMPTY;
				
				switch (productName) {
				case IzosdwanCommonConstants.IZOSDWAN_NAME:
					displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
					displayValueProdName = IzosdwanCommonConstants.SDWANSFDCProductName;
					type = IzosdwanCommonConstants.PRDT_SOLUTION;
					recordType = type;
					break;
				case "IAS":
					if(isNew) {
						displayValue = IzosdwanCommonConstants.IASSFDCNewProductId;
						displayValueProdName = IzosdwanCommonConstants.IASSFDCNewProductName;
					}else {
						displayValue = IzosdwanCommonConstants.IASSFDCProductId;
						displayValueProdName = IzosdwanCommonConstants.IASSFDCProductName;
					}
						type = IzosdwanCommonConstants.IAS;
						recordType = type;
					break;
				case "GVPN":
					if(isNew) {
						displayValue = IzosdwanCommonConstants.GVPNSFDCNewProductId;
						displayValueProdName = IzosdwanCommonConstants.GVPNSFDCNewProductName;
					}else {
						displayValue = IzosdwanCommonConstants.GVPNSFDCProductId;
						displayValueProdName = IzosdwanCommonConstants.GVPNSFDCProductName;
					}	
					type = IzosdwanCommonConstants.GVPN;
					recordType = type;
					break;
				case IzosdwanCommonConstants.IP_TRANSIT_PRODUCT: case IzosdwanCommonConstants.DIA_PRODUCT:
					if(isNew) {
						displayValue = IzosdwanCommonConstants.IPTRANSITSFDCNewProductId;
						displayValueProdName = IzosdwanCommonConstants.IPTRANSITSFDCNewProductName;
					}else {
						displayValue = IzosdwanCommonConstants.IPTRANSITSFDCProductId;
						displayValueProdName = IzosdwanCommonConstants.IPTRANSITSFDCProductName;
					}
					type = IzosdwanCommonConstants.IP_TRANSIT_PRODUCT;
					recordType = type;
					break;
				case IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT:
					if(isNew) {
						displayValue = IzosdwanCommonConstants.IWANSFDCNewProductId;
						displayValueProdName = IzosdwanCommonConstants.IWANSFDCNewProductName;
					}else {
						displayValue = IzosdwanCommonConstants.IWANSFDCProductId;
						displayValueProdName = IzosdwanCommonConstants.IWANSFDCProductName;
					}
					type = IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT;
					recordType = type;
					break;
				case IzosdwanCommonConstants.BYON_INTERNET_PRODUCT:
					displayValue = IzosdwanCommonConstants.ILLBYONSFDCProductId;
					displayValueProdName = IzosdwanCommonConstants.ILLBYONSFDCProductName;
					type = IzosdwanCommonConstants.IAS;
					recordType = type;
					if(isNew) {
						type = IzosdwanCommonConstants.SFDC_IAS_BYON;
					}

					break;
				case IzosdwanCommonConstants.BYON_MPLS_PRODUCT:
					displayValue = IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTID;
					displayValueProdName = IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTNAME;
					type = IzosdwanCommonConstants.GVPN;
					recordType = type;
					if(isNew) {
						type = IzosdwanCommonConstants.SFDC_GVPN_BYON;
					}
					break;
				case IzosdwanCommonConstants.VPROXY:
					displayValue = IzosdwanCommonConstants.VproxySFDCProductId;
					displayValueProdName = IzosdwanCommonConstants.VproxySFDCProductName;
					type = IzosdwanCommonConstants.MSS;
					recordType = type;
					break;
				case IzosdwanCommonConstants.VUTM:
					displayValue = IzosdwanCommonConstants.VutmSFDCProductId;
					displayValueProdName = IzosdwanCommonConstants.VutmSFDCProductName;
					type = IzosdwanCommonConstants.MSS;
					recordType = type;
					break;
				default:
					break;
				}
				ProductServiceBean productServiceBean = new ProductServiceBean();
				List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
						.findByDisplayValueAndQuote_id(displayValue, quoteToLe.getQuote().getId());
				if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()) {
					productServiceBean.setProductId(quoteIzoSdwanAttributeValues.get(0).getAttributeValue());
				}
				List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues1 = quoteIzoSdwanAttributeValuesRepository
						.findByDisplayValueAndQuote_id(displayValueProdName, quoteToLe.getQuote().getId());
				if (quoteIzoSdwanAttributeValues1 != null && !quoteIzoSdwanAttributeValues1.isEmpty()) {
					productServiceBean.setProductName(quoteIzoSdwanAttributeValues1.get(0).getAttributeValue());
				}
				productServiceBean.setSdwanProductName(productName);
				productServiceBean.setType(type);
				if(isNew) {
					productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
				}else {
					productServiceBean.setOrderType(SFDCConstants.CHANGE_ORDER);
				}
				productServiceBean.setRecordTypeName(recordType);
				productServiceBean.setCurrencyIsoCode(currencyIsoCode != null ? currencyIsoCode : SFDCConstants.INR);
				productServiceBean.setType(type);
				productServiceBean.setIdcBandwidth(SFDCConstants.NO);
				productServiceBean.setBigMachinesArc(0D);
				productServiceBean.setBigMachinesMrc(0D);
				productServiceBean.setBigMachinesNrc(0D);
				productServiceBean.setBigMachinesTcv(0D);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				productServiceBean.setRecordTypeName(recordType);
				nrc = productPricingDetail.getNrcDetailsBean().getNrcTcv().doubleValue();
				arc = productPricingDetail.getArcDetailsBean().getArcTcv().doubleValue();
				productServiceBean.setProductNRC(nrc != null ? nrc : 0D);
				productServiceBean.setProductMRC(arc != null ? arc / 12 : 0D);
				productServiceBean.setProductARC(arc != null ? arc : 0D);
				
				if(productPricingDetail.getPreviousMrcTotal() != null) {
					productServiceBean.setPreviousMrc(productPricingDetail.getPreviousMrcTotal().doubleValue());
				}else {
					productServiceBean.setPreviousMrc(0.0);
				}
				if(productPricingDetail.getPreviousNrcTotal() != null) {
					productServiceBean.setPreviousNrc(productPricingDetail.getPreviousNrcTotal().doubleValue());
				}else {
					productServiceBean.setPreviousNrc(0.0);
				}
				//productServiceBean.setPreviousArc(productPricingDetail.getPreviousArcTotal().doubleValue());
				
				
				LOGGER.info("Product NRC {} and Product MRC {}", productServiceBean.getProductNRC(),
						productServiceBean.getProductMRC());
				if (quoteToLe.getTermInMonths() != null) {
					productServiceBean.setTermInMonthsC(quoteToLe.getTermInMonths().substring(0, 2));
				}
				productServiceBean.setProductSolutionCode(quoteToLe.getQuote().getQuoteCode());
				String request = Utils.convertObjectToJson(productServiceBean);
				LOGGER.info("Input for updating the product Details {}", request);
				Byte isComplete = CommonConstants.BDEACTIVATE;
				if (StringUtils.isNotBlank(productServiceBean.getProductName())
						&& StringUtils.isNotBlank(productServiceBean.getProductName())) {
					isComplete = CommonConstants.BACTIVE;
				}
				LOGGER.info("Is Complete --> {} product id --> {} product name --> {} request --> {}", isComplete,
						productServiceBean.getProductId(), productServiceBean.getProductName(), request);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request, isComplete,
						SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT));

			}

		}
	}

 	/**
	 * getOrderLeAttributes- This method is used for getting the orderLeAttributtes
	 *
	 * @param orderToLe
	 * @param version
	 * @return
	 */
	private Map<String, String> getQuoteLeAttributes(QuoteToLe quoteToLe) {
		Map<String, String> attributeMap = new HashMap<>();
		List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
			Optional<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
					.findById(quoteLeAttributeValue.getMstOmsAttribute().getId());
			if (mstOmsAttributes.isPresent())
				attributeMap.put(mstOmsAttributes.get().getName(), quoteLeAttributeValue.getAttributeValue());
		}
		return attributeMap;
	}
	
	/**
	 * used to process the product service from sfdc reponse
	 * processSfdcProductService
	 *
	 * @param productServicesResponseBean
	 * @throws TclCommonException
	 */
	@Transactional
	public void processSfdcProductService(ProductServicesResponseBean productServicesResponseBean)
			throws TclCommonException {
		if (productServicesResponseBean.getProductsservices() != null
				&& !productServicesResponseBean.getProductsservices().isEmpty()
				&& !productServicesResponseBean.isError()) {
			String prodServiceId = null;
			String productServiceName = productServicesResponseBean.getProductId();
			for (ProductsserviceResponse prodService : productServicesResponseBean.getProductsservices()) {
				prodServiceId = prodService.getId();
			}
			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			
			if (quoteLes!=null && !quoteLes.isEmpty()) {
				List<ProductSolution> productSolutions = null;
				if (productServicesResponseBean.getType().equals("create")) {
					LOGGER.info("Got Quote To Le ");
					Quote quote = quoteLes.get(0).getQuote();
					List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesList = quoteIzoSdwanAttributeValuesRepository
							.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.IS_CREATE_PRODUCT_BYON,
									quote.getId());
					 
					List<ProductsserviceResponse> productsserviceResponses=productServicesResponseBean.getProductsservices();
					Boolean isOrderNew = (productsserviceResponses.get(0).getOrderType().equals("New Order")) ? true:false;
					if (!productsserviceResponses.isEmpty()&&productsserviceResponses.get(0).getProductCategoryC()!=null&&productServicesResponseBean.getProductsservices().get(0).getProductCategoryC()
							.equals(CommonConstants.IAS)) {
						LOGGER.info("Product category IAS or BYON");
						if (quoteIzoSdwanAttributeValuesList != null && !quoteIzoSdwanAttributeValuesList.isEmpty()
								&& quoteIzoSdwanAttributeValuesList.get(0).getAttributeValue()
										.equalsIgnoreCase("true")) {
							LOGGER.info("Product category BYON");
							List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
							QuoteIzoSdwanAttributeValues iasVal = new QuoteIzoSdwanAttributeValues();
							iasVal.setDisplayValue(IzosdwanCommonConstants.ILLBYONSFDCProductId);
							iasVal.setAttributeValue(prodServiceId);
							iasVal.setQuote(quote);
							QuoteIzoSdwanAttributeValues iasNameVal = new QuoteIzoSdwanAttributeValues();
							iasNameVal.setDisplayValue(IzosdwanCommonConstants.ILLBYONSFDCProductName);
							iasNameVal.setAttributeValue(productServiceName);
							iasNameVal.setQuote(quote);
							vals.add(iasNameVal);
							vals.add(iasVal);
							quoteIzoSdwanAttributeValuesRepository.saveAll(vals);
						} else {
							LOGGER.info("Product category IAS");
							List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
							QuoteIzoSdwanAttributeValues iasVal = new QuoteIzoSdwanAttributeValues();
							//iasVal.setDisplayValue(IzosdwanCommonConstants.IASSFDCProductId);
							iasVal.setAttributeValue(prodServiceId);
							iasVal.setQuote(quote);
							QuoteIzoSdwanAttributeValues iasNameVal = new QuoteIzoSdwanAttributeValues();
							//iasNameVal.setDisplayValue(IzosdwanCommonConstants.IASSFDCProductName);
							iasNameVal.setAttributeValue(productServiceName);
							iasNameVal.setQuote(quote);
							if(isOrderNew) {
								iasVal.setDisplayValue(IzosdwanCommonConstants.IASSFDCNewProductId);
								iasNameVal.setDisplayValue(IzosdwanCommonConstants.IASSFDCNewProductName);
							}else {
								iasVal.setDisplayValue(IzosdwanCommonConstants.IASSFDCProductId);
								iasNameVal.setDisplayValue(IzosdwanCommonConstants.IASSFDCProductName);
							}
							vals.add(iasNameVal);
							vals.add(iasVal);
							quoteIzoSdwanAttributeValuesRepository.saveAll(vals);
						}
					}
					
					// For custom journey
					if (quote.getNsQuote() != null && quote.getNsQuote().equalsIgnoreCase("Y")) {
						LOGGER.info("BYON in custom journey ");
						if (!productsserviceResponses.isEmpty()
								&& productsserviceResponses.get(0).getProductCategoryC() != null
								&& productServicesResponseBean.getProductsservices().get(0).getProductCategoryC().equals(CommonConstants.IAS)
								&& productServicesResponseBean.getProductsservices().get(0).getType() != null
								&& productServicesResponseBean.getProductsservices().get(0).getType().equals(IzosdwanCommonConstants.SFDC_IAS_BYON)) {
							LOGGER.info("IAS in custom journey" );
								LOGGER.info("Product category BYON Internet");
								List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
								QuoteIzoSdwanAttributeValues iasVal = new QuoteIzoSdwanAttributeValues();
								iasVal.setDisplayValue(IzosdwanCommonConstants.ILLBYONSFDCProductId);
								iasVal.setAttributeValue(prodServiceId);
								iasVal.setQuote(quote);
								QuoteIzoSdwanAttributeValues iasNameVal = new QuoteIzoSdwanAttributeValues();
								iasNameVal.setDisplayValue(IzosdwanCommonConstants.ILLBYONSFDCProductName);
								iasNameVal.setAttributeValue(productServiceName);
								iasNameVal.setQuote(quote);
								vals.add(iasNameVal);
								vals.add(iasVal);
								quoteIzoSdwanAttributeValuesRepository.saveAll(vals);
						}
						if (!productsserviceResponses.isEmpty()
								&& productsserviceResponses.get(0).getProductCategoryC() != null
								&& productServicesResponseBean.getProductsservices().get(0).getProductCategoryC().equals(CommonConstants.GVPN)
								&& productServicesResponseBean.getProductsservices().get(0).getType() != null
								&& productServicesResponseBean.getProductsservices().get(0).getType().equals(IzosdwanCommonConstants.SFDC_GVPN_BYON)) {
								LOGGER.info("Product category BYON MPLS ");
								List<QuoteIzoSdwanAttributeValues> valss = new ArrayList<>();
								QuoteIzoSdwanAttributeValues iasVall = new QuoteIzoSdwanAttributeValues();
								iasVall.setDisplayValue(IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTID);
								iasVall.setAttributeValue(prodServiceId);
								iasVall.setQuote(quote);
								QuoteIzoSdwanAttributeValues iasNameVall = new QuoteIzoSdwanAttributeValues();
								iasNameVall.setDisplayValue(IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTNAME);
								iasNameVall.setAttributeValue(productServiceName);
								iasNameVall.setQuote(quote);
								valss.add(iasNameVall);
								valss.add(iasVall);
								quoteIzoSdwanAttributeValuesRepository.saveAll(valss);
							
						}
					}
					if (!productsserviceResponses.isEmpty()&&productsserviceResponses.get(0).getProductCategoryC()!=null
							&& productServicesResponseBean.getProductsservices().get(0).getProductCategoryC().equals(CommonConstants.GVPN)) {
						LOGGER.info("Product category GVPN");
						List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
						QuoteIzoSdwanAttributeValues gvpnVal = new QuoteIzoSdwanAttributeValues();
						//gvpnVal.setDisplayValue(IzosdwanCommonConstants.GVPNSFDCProductId);
						gvpnVal.setAttributeValue(prodServiceId);
						gvpnVal.setQuote(quote);
						QuoteIzoSdwanAttributeValues gvpnNameVal = new QuoteIzoSdwanAttributeValues();
						//gvpnNameVal.setDisplayValue(IzosdwanCommonConstants.GVPNSFDCProductName);
						gvpnNameVal.setAttributeValue(productServiceName);
						gvpnNameVal.setQuote(quote);
						if(isOrderNew) {
							gvpnVal.setDisplayValue(IzosdwanCommonConstants.GVPNSFDCNewProductId);
							gvpnNameVal.setDisplayValue(IzosdwanCommonConstants.GVPNSFDCNewProductName);
						}else {
							gvpnVal.setDisplayValue(IzosdwanCommonConstants.GVPNSFDCProductId);
							gvpnNameVal.setDisplayValue(IzosdwanCommonConstants.GVPNSFDCProductName);
						}
						vals.add(gvpnNameVal);
						vals.add(gvpnVal);
						quoteIzoSdwanAttributeValuesRepository.saveAll(vals);
					}
					if (!productsserviceResponses.isEmpty()&&productsserviceResponses.get(0).getProductCategoryC()!=null&&productServicesResponseBean.getProductsservices().get(0).getProductCategoryC()
							.equals(IzosdwanCommonConstants.ManagedServices)) {
						LOGGER.info("Product category MSS");
						List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
						QuoteIzoSdwanAttributeValues vproxyVal = new QuoteIzoSdwanAttributeValues();
						vproxyVal.setDisplayValue(IzosdwanCommonConstants.VproxySFDCProductId);
						vproxyVal.setAttributeValue(prodServiceId);
						vproxyVal.setQuote(quote);
						QuoteIzoSdwanAttributeValues vproxyNameVal = new QuoteIzoSdwanAttributeValues();
						vproxyNameVal.setDisplayValue(IzosdwanCommonConstants.VproxySFDCProductName);
						vproxyNameVal.setAttributeValue(productServiceName);
						vproxyNameVal.setQuote(quote);
						vals.add(vproxyNameVal);
						vals.add(vproxyVal);
						quoteIzoSdwanAttributeValuesRepository.saveAll(vals);
					}
					if (!productsserviceResponses.isEmpty()&&productsserviceResponses.get(0).getProductCategoryC()!=null&&productServicesResponseBean.getProductsservices().get(0).getProductCategoryC()
							.equals("IZO SDWAN")) {
						LOGGER.info("Product category IZO SDWAN");
						List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
						QuoteIzoSdwanAttributeValues sdwanVal = new QuoteIzoSdwanAttributeValues();
						sdwanVal.setDisplayValue(IzosdwanCommonConstants.SDWANSFDCProductId);
						sdwanVal.setAttributeValue(prodServiceId);
						sdwanVal.setQuote(quote);
						QuoteIzoSdwanAttributeValues sdwanNameVal = new QuoteIzoSdwanAttributeValues();
						sdwanNameVal.setDisplayValue(IzosdwanCommonConstants.SDWANSFDCProductName);
						sdwanNameVal.setAttributeValue(productServiceName);
						sdwanNameVal.setQuote(quote);
						vals.add(sdwanNameVal);
						vals.add(sdwanVal);
						quoteIzoSdwanAttributeValuesRepository.saveAll(vals);

					}
					if (!productsserviceResponses.isEmpty()&&productsserviceResponses.get(0).getProductCategoryC()!=null&&productServicesResponseBean.getProductsservices().get(0).getProductCategoryC()
							.equals("IZO Internet WAN")) {
						LOGGER.info("Product category IZO Internet WAN");
						List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
						QuoteIzoSdwanAttributeValues iwanVal = new QuoteIzoSdwanAttributeValues();
						//iwanVal.setDisplayValue(IzosdwanCommonConstants.IWANSFDCProductId);
						iwanVal.setAttributeValue(prodServiceId);
						iwanVal.setQuote(quote);
						QuoteIzoSdwanAttributeValues iwanNameVal = new QuoteIzoSdwanAttributeValues();
						//iwanNameVal.setDisplayValue(IzosdwanCommonConstants.IWANSFDCProductName);
						iwanNameVal.setAttributeValue(productServiceName);
						iwanNameVal.setQuote(quote);
						if(isOrderNew) {
							iwanVal.setDisplayValue(IzosdwanCommonConstants.IWANSFDCNewProductId);
							iwanNameVal.setDisplayValue(IzosdwanCommonConstants.IWANSFDCNewProductName);
						}else {
							iwanVal.setDisplayValue(IzosdwanCommonConstants.IWANSFDCProductId);
							iwanNameVal.setDisplayValue(IzosdwanCommonConstants.IWANSFDCProductName);
						}
						vals.add(iwanNameVal);
						vals.add(iwanVal);
						quoteIzoSdwanAttributeValuesRepository.saveAll(vals);

					}
					
					if (!productsserviceResponses.isEmpty()&&productsserviceResponses.get(0).getProductCategoryC()!=null&&productServicesResponseBean.getProductsservices().get(0).getProductCategoryC()
							.equals("IP Transit")) {
						LOGGER.info("Product category IP Transit");
						List<QuoteIzoSdwanAttributeValues> vals = new ArrayList<>();
						QuoteIzoSdwanAttributeValues ipTransitVal = new QuoteIzoSdwanAttributeValues();
						//ipTransitVal.setDisplayValue(IzosdwanCommonConstants.IPTRANSITSFDCProductId);
						ipTransitVal.setAttributeValue(prodServiceId);
						ipTransitVal.setQuote(quote);
						QuoteIzoSdwanAttributeValues ipTransitNameVal = new QuoteIzoSdwanAttributeValues();
						//ipTransitNameVal.setDisplayValue(IzosdwanCommonConstants.IPTRANSITSFDCProductName);
						ipTransitNameVal.setAttributeValue(productServiceName);
						ipTransitNameVal.setQuote(quote);
						if(isOrderNew) {
							ipTransitVal.setDisplayValue(IzosdwanCommonConstants.IPTRANSITSFDCNewProductId);
							ipTransitNameVal.setDisplayValue(IzosdwanCommonConstants.IPTRANSITSFDCNewProductName);
						}else {
							ipTransitVal.setDisplayValue(IzosdwanCommonConstants.IPTRANSITSFDCProductId);
							ipTransitNameVal.setDisplayValue(IzosdwanCommonConstants.IPTRANSITSFDCProductName);
						}
						vals.add(ipTransitNameVal);
						vals.add(ipTransitVal);
						quoteIzoSdwanAttributeValuesRepository.saveAll(vals);

					}


				}
			

			}
			
			LOGGER.info("Entering - product service response when not in error ");
			List<QuoteToLe> quoteLes1= quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
					: SFDCConstants.CREATE_PRODUCT;
			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
			for (QuoteToLe quoteToLe2 : quoteLes1) {
				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.INPROGRESS.toString(), quoteToLe2.getQuote().getQuoteCode(), status,
								ThirdPartySource.SFDC.toString());
				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
						Utils.convertObjectToJson(productServicesResponseBean), productServicesResponseBean.getSfdcid());
			}
		}
//		} else {
//			String status = productServicesResponseBean.getType().equals("update") ? SFDCConstants.UPDATE_PRODUCT
//					: SFDCConstants.CREATE_PRODUCT;
//			status = productServicesResponseBean.getType().equals("delete") ? SFDCConstants.DELETE_PRODUCT : status;
//			List<QuoteToLe> quoteLes = quoteToLeRepository.findByTpsSfdcOptyId(productServicesResponseBean.getSfdcid());
//			for (QuoteToLe quoteToLe : quoteLes) {
//				String orderCode = quoteToLe.getQuote().getQuoteCode();
//				updateStruckStatus(orderCode);
//				List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
//						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
//								SfdcServiceStatus.INPROGRESS.toString(), orderCode, status,
//								ThirdPartySource.SFDC.toString());
//				persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
//						productServicesResponseBean.getErrorMessage(), productServicesResponseBean.getSfdcid());
//			}
		
	}
	/**
	 * updateStruckStatus
	 */
	public void updateStruckStatus(String refId) {
		thirdPartyServiceJobsRepository.updateServiceStatusByRefIdAndThirdPartySource(refId,
				SfdcServiceStatus.STRUCK.toString(), ThirdPartySource.SFDC.toString());
	}
	
	public void processDeleteProduct(QuoteToLe quoteToLe, QuoteToLeProductFamily quoteToLeProductFamily,ProductSolution productSolution) {
		try {
			LOGGER.info("Inside process delete product for the product {} for quote {}",quoteToLeProductFamily.getMstProductFamily().getName(),quoteToLe.getQuote().getQuoteCode());
			List<String> productNames = getProductNameBasedOnProductCategory(
					quoteToLeProductFamily.getMstProductFamily().getName());
			LOGGER.info("Product names are {}",Utils.convertObjectToJson(productNames));
			List<String> productIds = new ArrayList<>();
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = new ArrayList<>();
			if (productNames != null && !productNames.isEmpty()) {
				productNames.stream().forEach(name -> {
					
					List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesProductName = quoteIzoSdwanAttributeValuesRepository
							.findByDisplayValueAndQuote(name.concat(IzosdwanCommonConstants.SFDC)
									.concat(IzosdwanCommonConstants.PRODUCT_NAME), quoteToLe.getQuote());
					List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValuesProductId = quoteIzoSdwanAttributeValuesRepository
							.findByDisplayValueAndQuote(name.concat(IzosdwanCommonConstants.SFDC)
									.concat(IzosdwanCommonConstants.PRODUCT_ID), quoteToLe.getQuote());
					if (quoteIzoSdwanAttributeValuesProductId != null
							&& !quoteIzoSdwanAttributeValuesProductId.isEmpty()) {
						LOGGER.info("Product id for product {} is {}",name,quoteIzoSdwanAttributeValuesProductId.get(0).getAttributeValue());
						productIds.add(quoteIzoSdwanAttributeValuesProductId.get(0).getAttributeValue());
						quoteIzoSdwanAttributeValues.addAll(quoteIzoSdwanAttributeValuesProductId);
					}
					if (quoteIzoSdwanAttributeValuesProductName != null
							&& !quoteIzoSdwanAttributeValuesProductName.isEmpty()) {
						quoteIzoSdwanAttributeValues.addAll(quoteIzoSdwanAttributeValuesProductName);
					}
					

				});
				if (productIds != null && !productIds.isEmpty()) {
					LOGGER.info("Product ids are {}",Utils.convertObjectToJson(productIds));
					ProductServiceBean productServiceBean = new ProductServiceBean();

					productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
					productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
					productServiceBean.setProductIds(productIds);
					productServiceBean.setSourceSystem(SFDCConstants.OPTIMUS.toString());
					String request = Utils.convertObjectToJson(productServiceBean);
					LOGGER.info("Input for updating the product Details {}", request);

					persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcProductDeleteService, request,
							StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
									: CommonConstants.BDEACTIVATE,
							SFDCConstants.DELETE_PRODUCT, getSequenceNumber(SFDCConstants.DELETE_PRODUCT));
				}
				if(productNames!=null && !productNames.isEmpty()) {
					LOGGER.info("Before calling deleting the create product entries !!");
					deleteProductRelatedEntires(productNames, quoteToLe.getQuote().getQuoteCode());
				}
				if(quoteIzoSdwanAttributeValues!=null && !quoteIzoSdwanAttributeValues.isEmpty()) {
					izosdwanQuoteService.deleteTheQuoteIzosdwanAttributeValues(quoteIzoSdwanAttributeValues);
				}
			}

		} catch (Exception e) {
			LOGGER.info("Exception in deleting sfdc product", e);
		}
	}
	
	private List<String> getProductNameBasedOnProductCategory(String productCategory) {
		List<String> productName = new ArrayList<>();
		switch(productCategory){
		case IzosdwanCommonConstants.IZOSDWAN_NAME:
			productName.add(IzosdwanCommonConstants.SDWAN.toUpperCase());
			productName.add(IzosdwanCommonConstants.IAS_CODE.toUpperCase());
			productName.add(IzosdwanCommonConstants.GVPN_CODE.toUpperCase());
			break;
		case IzosdwanCommonConstants.VPROXY:
			productName.add(IzosdwanCommonConstants.VPROXY.toUpperCase());
			break;
		default:
			break;
		}
		
		return productName;
	}
	
	private void deleteProductRelatedEntires(List<String> productIds, String quoteCode) {
		try {
		List<ThirdPartyServiceJob> thirdPartyServiceJobs = new ArrayList<>();
		List<String> status = new ArrayList<>();
		status.add(IzosdwanCommonConstants.INPROGRESS);
		status.add(IzosdwanCommonConstants.FAILURE);
		status.add(IzosdwanCommonConstants.NEW);
		status.add(IzosdwanCommonConstants.STRUCK);
		List<String> serviceTypes = new ArrayList<>();
		serviceTypes.add(SFDCConstants.UPDATE_PRODUCT);
		serviceTypes.add(SFDCConstants.CREATE_PRODUCT);
		List<ThirdPartyServiceJob> alreadyPresentJobs = thirdPartyServiceJobsRepository
				.findAllByServiceStatusInAndServiceTypeInAndRefId(status, serviceTypes, quoteCode);
		if (alreadyPresentJobs != null && !alreadyPresentJobs.isEmpty()) {
			for (ThirdPartyServiceJob jobs : alreadyPresentJobs) {
				for (String id : productIds) {

					if (jobs.getRequestPayload().contains(id)) {
						thirdPartyServiceJobs.add(jobs);
						break;
					}
				}
			}
		}
		if(thirdPartyServiceJobs!=null && !thirdPartyServiceJobs.isEmpty()) {
			LOGGER.info("Got records to delete for update product!!");
			thirdPartyServiceJobsRepository.deleteAll(thirdPartyServiceJobs);
		}
		}catch(Exception e) {
			LOGGER.error("Error on deleting existing records");
		}
	}
	
	public void processCreateSiteLocationInSdwanSDFC(QuoteToLe quoteToLe, String prodName,Boolean isNew) throws TclCommonException{

		ProductSolution productSolution = productSolutionRepository
				.findByReferenceIdForIzoSdwan(quoteToLe.getQuote().getId());
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		MDMServiceInventoryBean serviceDetail = null;
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
		
		Byte isComplete = CommonConstants.BDEACTIVATE;
		if (productSolution != null) {
			List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
					.findByProductSolution(productSolution);
			if (prodName.equalsIgnoreCase(IzosdwanCommonConstants.IZOSDWAN_NAME)) {
				String productname = getFamilyNameByProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
				List<Integer> locationIds = quoteIzosdwanSites.stream().map(site -> site.getErfLocSitebLocationId())
						.distinct().collect(Collectors.toList());
				for (Integer locationId : locationIds) {
					List<QuoteIzosdwanSite> sites = quoteIzosdwanSites.stream()
							.filter(site -> site.getErfLocSitebLocationId().equals(locationId))
							.collect(Collectors.toList());

					LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(locationId));

					SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
					if (StringUtils.isNotBlank(locationResponse)) {

						AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
								AddressDetail.class);
						addressDetail = validateAddressDetail(addressDetail);
						siteOpportunityLocation.setCity(addressDetail.getCity());
						siteOpportunityLocation.setCountry(addressDetail.getCountry());
						siteOpportunityLocation.setLocation(addressDetail.getCity());
						siteOpportunityLocation.setState(addressDetail.getState());

					}
					siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + sites.get(0).getSiteCode());
					Double mrc = 0D;
					Double arc = 0D;
					Double nrc = 0D;
					for (QuoteIzosdwanSite quoteIzosdwanSite : sites) {
						List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
								.findByReferenceIdAndReferenceName(quoteIzosdwanSite.getId(),
										getReferenceName(prodName));
						Map<String, Double> map = getFinalPriceForTheSiteProductWise(prodName, quoteProductComponents,
								quoteToLe.getQuote().getId());
						mrc += map.get("mrc");
						nrc += map.get("nrc");
						arc += map.get("arc");
					}
					siteOpportunityLocation.setSiteMRC(mrc);
					siteOpportunityLocation.setSiteNRC(nrc);
					siteOpportunityLocation.setSiteARC(arc);
					siteOpportunityLocations.add(siteOpportunityLocation);

				}
			} else if (prodName.equalsIgnoreCase(IzosdwanCommonConstants.CGW)) {

				List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetail = quoteIzosdwanCgwDetailRepository
						.findByQuote_Id(quoteToLe.getQuote().getId());
				String productname = getFamilyNameByProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
				if (quoteIzosdwanCgwDetail != null && !quoteIzosdwanCgwDetail.isEmpty()) {
					for (QuoteIzosdwanCgwDetail cgw : quoteIzosdwanCgwDetail) {
						if (cgw.getPrimaryLocation() != null) {
							String productResponse = (String) mqUtils.sendAndReceive(cgwSamByCityQueue,
									cgw.getPrimaryLocation());
							if (StringUtils.isNotEmpty(productResponse)) {
								CgwServiceAreaMatricBean cgwServiceAreaMatricBean = Utils
										.convertJsonToObject(productResponse, CgwServiceAreaMatricBean.class);
								LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(cgwServiceAreaMatricBean.getLocationId()));

								SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
								if (StringUtils.isNotBlank(locationResponse)) {

									AddressDetail addressDetail = (AddressDetail) Utils
											.convertJsonToObject(locationResponse, AddressDetail.class);
									addressDetail = validateAddressDetail(addressDetail);
									siteOpportunityLocation.setCity(addressDetail.getCity());
									siteOpportunityLocation.setCountry(addressDetail.getCountry());
									siteOpportunityLocation.setLocation(addressDetail.getCity());
									siteOpportunityLocation.setState(addressDetail.getState());

								}
								siteOpportunityLocation.setSiteLocationID(
										SFDCConstants.OPTIMUS + " CGW PRIMARY" + quoteToLe.getQuote().getQuoteCode());
								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndReferenceName(cgw.getId(),
												getReferenceName(IzosdwanCommonConstants.CGW));
								Map<String, Double> map = getFinalPriceForTheSiteProductWise(prodName,
										quoteProductComponents, quoteToLe.getQuote().getId());
								siteOpportunityLocation.setSiteMRC(map.get("mrc"));
								siteOpportunityLocation.setSiteNRC(map.get("nrc"));
								siteOpportunityLocation.setSiteARC(map.get("arc"));
								siteOpportunityLocations.add(siteOpportunityLocation);
							}
						}
						if (cgw.getSecondaryLocation() != null) {
							String productResponse = (String) mqUtils.sendAndReceive(cgwSamByCityQueue,
									cgw.getSecondaryLocation());
							if (StringUtils.isNotEmpty(productResponse)) {
								CgwServiceAreaMatricBean cgwServiceAreaMatricBean = Utils
										.convertJsonToObject(productResponse, CgwServiceAreaMatricBean.class);
								LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(cgwServiceAreaMatricBean.getLocationId()));

								SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
								if (StringUtils.isNotBlank(locationResponse)) {

									AddressDetail addressDetail = (AddressDetail) Utils
											.convertJsonToObject(locationResponse, AddressDetail.class);
									addressDetail = validateAddressDetail(addressDetail);
									siteOpportunityLocation.setCity(addressDetail.getCity());
									siteOpportunityLocation.setCountry(addressDetail.getCountry());
									siteOpportunityLocation.setLocation(addressDetail.getCity());
									siteOpportunityLocation.setState(addressDetail.getState());

								}
								siteOpportunityLocation.setSiteLocationID(
										SFDCConstants.OPTIMUS + " CGW SECONDARY" + quoteToLe.getQuote().getQuoteCode());
								siteOpportunityLocation.setSiteMRC(0D);
								siteOpportunityLocation.setSiteNRC(0D);
								siteOpportunityLocation.setSiteARC(0D);
								siteOpportunityLocations.add(siteOpportunityLocation);
							}
						}
					}
				}

			} else {

				if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
					for (QuoteIzosdwanSite quoteIzosdwanSite : quoteIzosdwanSites) {
						if (quoteIzosdwanSite.getIzosdwanSiteProduct() != null
								&& quoteIzosdwanSite.getIzosdwanSiteProduct().equals(prodName) && checkSitesBasedOnIsNewFlag(isNew, quoteIzosdwanSite)) {
							String productname = getFamilyNameByProductName(quoteIzosdwanSite.getIzosdwanSiteProduct());
							LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
									String.valueOf(quoteIzosdwanSite.getErfLocSitebLocationId()));

							SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
							if (StringUtils.isNotBlank(locationResponse)) {

								AddressDetail addressDetail = (AddressDetail) Utils
										.convertJsonToObject(locationResponse, AddressDetail.class);
								addressDetail = validateAddressDetail(addressDetail);
								siteOpportunityLocation.setCity(addressDetail.getCity());
								siteOpportunityLocation.setCountry(addressDetail.getCountry());
								siteOpportunityLocation.setLocation(addressDetail.getCity());
								siteOpportunityLocation.setState(addressDetail.getState());

							}
							siteOpportunityLocation
									.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIzosdwanSite.getSiteCode());
							List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
									.findByReferenceIdAndReferenceName(quoteIzosdwanSite.getId(),
											getReferenceName(prodName));
							Map<String, Double> map = getFinalPriceForTheSiteProductWise(prodName,
									quoteProductComponents, quoteToLe.getQuote().getId());
							siteOpportunityLocation.setSiteMRC(map.get("mrc"));
							siteOpportunityLocation.setSiteNRC(map.get("nrc"));
							siteOpportunityLocation.setSiteARC(map.get("arc"));
							siteOpportunityLocations.add(siteOpportunityLocation);

						}
					}
				}

			}
			siteSolutionOpportunityBean.setSiteProduct(prodName);
			siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
			siteSolutionOpportunityBean.setProductSolutionCode(productSolution.getSolutionCode());
			siteSolutionOpportunityBean
					.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + productSolution.getSolutionCode());

			siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
			if (siteOpportunityLocations != null && !siteOpportunityLocations.isEmpty()) {
				String sfdcProductId = getProductServiceIdByProductNameBasedOnFlag(quoteToLe.getQuote().getId(), prodName,isNew);
				if (sfdcProductId != null && quoteToLe.getTpsSfdcOptyId() != null) {
					isComplete = CommonConstants.BACTIVE;
				}
				siteSolutionOpportunityBean.setProductServiceId(sfdcProductId);
				String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
				LOGGER.info("Request for create site location {}", request);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
						SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE));
			}
		}

	
		
	}
	
	
	private Boolean checkSitesBasedOnIsNewFlag(Boolean isNew,QuoteIzosdwanSite quoteIzosdwanSite) {
		if(isNew) {
			return quoteIzosdwanSite.getErfServiceInventoryTpsServiceId()==null;
		}else {
			return quoteIzosdwanSite.getErfServiceInventoryTpsServiceId()!=null;
		}
		
	}
	public void processCreateSiteLocationInSFDC(QuoteToLe quoteToLe, String prodName) throws TclCommonException {
		ProductSolution productSolution = productSolutionRepository
				.findByReferenceIdForIzoSdwan(quoteToLe.getQuote().getId());
		SiteSolutionOpportunityBean siteSolutionOpportunityBean = new SiteSolutionOpportunityBean();
		MDMServiceInventoryBean serviceDetail = null;
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();
		siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
		
		Byte isComplete = CommonConstants.BDEACTIVATE;
		if (productSolution != null) {
			List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
					.findByProductSolution(productSolution);
			if (prodName.equalsIgnoreCase(IzosdwanCommonConstants.IZOSDWAN_NAME)) {
				String productname = getFamilyNameByProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
				List<Integer> locationIds = quoteIzosdwanSites.stream().map(site -> site.getErfLocSitebLocationId())
						.distinct().collect(Collectors.toList());
				for (Integer locationId : locationIds) {
					List<QuoteIzosdwanSite> sites = quoteIzosdwanSites.stream()
							.filter(site -> site.getErfLocSitebLocationId().equals(locationId))
							.collect(Collectors.toList());

					LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(locationId));

					SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
					if (StringUtils.isNotBlank(locationResponse)) {

						AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
								AddressDetail.class);
						addressDetail = validateAddressDetail(addressDetail);
						siteOpportunityLocation.setCity(addressDetail.getCity());
						siteOpportunityLocation.setCountry(addressDetail.getCountry());
						siteOpportunityLocation.setLocation(addressDetail.getCity());
						siteOpportunityLocation.setState(addressDetail.getState());

					}
					siteOpportunityLocation.setSiteLocationID(SFDCConstants.OPTIMUS + sites.get(0).getSiteCode());
					Double mrc = 0D;
					Double arc = 0D;
					Double nrc = 0D;
					for (QuoteIzosdwanSite quoteIzosdwanSite : sites) {
						List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
								.findByReferenceIdAndReferenceName(quoteIzosdwanSite.getId(),
										getReferenceName(prodName));
						Map<String, Double> map = getFinalPriceForTheSiteProductWise(prodName, quoteProductComponents,
								quoteToLe.getQuote().getId());
						mrc += map.get("mrc");
						nrc += map.get("nrc");
						arc += map.get("arc");
					}
					siteOpportunityLocation.setSiteMRC(mrc);
					siteOpportunityLocation.setSiteNRC(nrc);
					siteOpportunityLocation.setSiteARC(arc);
					siteOpportunityLocations.add(siteOpportunityLocation);

				}
			} else if (prodName.equalsIgnoreCase(IzosdwanCommonConstants.CGW)) {

				List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetail = quoteIzosdwanCgwDetailRepository
						.findByQuote_Id(quoteToLe.getQuote().getId());
				String productname = getFamilyNameByProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
				if (quoteIzosdwanCgwDetail != null && !quoteIzosdwanCgwDetail.isEmpty()) {
					for (QuoteIzosdwanCgwDetail cgw : quoteIzosdwanCgwDetail) {
						if (cgw.getPrimaryLocation() != null) {
							String productResponse = (String) mqUtils.sendAndReceive(cgwSamByCityQueue,
									cgw.getPrimaryLocation());
							if (StringUtils.isNotEmpty(productResponse)) {
								CgwServiceAreaMatricBean cgwServiceAreaMatricBean = Utils
										.convertJsonToObject(productResponse, CgwServiceAreaMatricBean.class);
								LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(cgwServiceAreaMatricBean.getLocationId()));

								SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
								if (StringUtils.isNotBlank(locationResponse)) {

									AddressDetail addressDetail = (AddressDetail) Utils
											.convertJsonToObject(locationResponse, AddressDetail.class);
									addressDetail = validateAddressDetail(addressDetail);
									siteOpportunityLocation.setCity(addressDetail.getCity());
									siteOpportunityLocation.setCountry(addressDetail.getCountry());
									siteOpportunityLocation.setLocation(addressDetail.getCity());
									siteOpportunityLocation.setState(addressDetail.getState());

								}
								siteOpportunityLocation.setSiteLocationID(
										SFDCConstants.OPTIMUS + " CGW PRIMARY" + quoteToLe.getQuote().getQuoteCode());
								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndReferenceName(cgw.getId(),
												getReferenceName(IzosdwanCommonConstants.CGW));
								Map<String, Double> map = getFinalPriceForTheSiteProductWise(prodName,
										quoteProductComponents, quoteToLe.getQuote().getId());
								siteOpportunityLocation.setSiteMRC(map.get("mrc"));
								siteOpportunityLocation.setSiteNRC(map.get("nrc"));
								siteOpportunityLocation.setSiteARC(map.get("arc"));
								siteOpportunityLocations.add(siteOpportunityLocation);
							}
						}
						if (cgw.getSecondaryLocation() != null) {
							String productResponse = (String) mqUtils.sendAndReceive(cgwSamByCityQueue,
									cgw.getSecondaryLocation());
							if (StringUtils.isNotEmpty(productResponse)) {
								CgwServiceAreaMatricBean cgwServiceAreaMatricBean = Utils
										.convertJsonToObject(productResponse, CgwServiceAreaMatricBean.class);
								LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
										String.valueOf(cgwServiceAreaMatricBean.getLocationId()));

								SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
								if (StringUtils.isNotBlank(locationResponse)) {

									AddressDetail addressDetail = (AddressDetail) Utils
											.convertJsonToObject(locationResponse, AddressDetail.class);
									addressDetail = validateAddressDetail(addressDetail);
									siteOpportunityLocation.setCity(addressDetail.getCity());
									siteOpportunityLocation.setCountry(addressDetail.getCountry());
									siteOpportunityLocation.setLocation(addressDetail.getCity());
									siteOpportunityLocation.setState(addressDetail.getState());

								}
								siteOpportunityLocation.setSiteLocationID(
										SFDCConstants.OPTIMUS + " CGW SECONDARY" + quoteToLe.getQuote().getQuoteCode());
								siteOpportunityLocation.setSiteMRC(0D);
								siteOpportunityLocation.setSiteNRC(0D);
								siteOpportunityLocation.setSiteARC(0D);
								siteOpportunityLocations.add(siteOpportunityLocation);
							}
						}
					}
				}

			} else {

				if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
					for (QuoteIzosdwanSite quoteIzosdwanSite : quoteIzosdwanSites) {
						if (quoteIzosdwanSite.getIzosdwanSiteProduct() != null
								&& quoteIzosdwanSite.getIzosdwanSiteProduct().equals(prodName)) {
							String productname = getFamilyNameByProductName(quoteIzosdwanSite.getIzosdwanSiteProduct());
							LOGGER.info("MDC Filter token value in before Queue call processSiteDetails {} :",
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
									String.valueOf(quoteIzosdwanSite.getErfLocSitebLocationId()));

							SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
							if (StringUtils.isNotBlank(locationResponse)) {

								AddressDetail addressDetail = (AddressDetail) Utils
										.convertJsonToObject(locationResponse, AddressDetail.class);
								addressDetail = validateAddressDetail(addressDetail);
								siteOpportunityLocation.setCity(addressDetail.getCity());
								siteOpportunityLocation.setCountry(addressDetail.getCountry());
								siteOpportunityLocation.setLocation(addressDetail.getCity());
								siteOpportunityLocation.setState(addressDetail.getState());

							}
							siteOpportunityLocation
									.setSiteLocationID(SFDCConstants.OPTIMUS + quoteIzosdwanSite.getSiteCode());
							List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
									.findByReferenceIdAndReferenceName(quoteIzosdwanSite.getId(),
											getReferenceName(prodName));
							Map<String, Double> map = getFinalPriceForTheSiteProductWise(prodName,
									quoteProductComponents, quoteToLe.getQuote().getId());
							siteOpportunityLocation.setSiteMRC(map.get("mrc"));
							siteOpportunityLocation.setSiteNRC(map.get("nrc"));
							siteOpportunityLocation.setSiteARC(map.get("arc"));
							siteOpportunityLocations.add(siteOpportunityLocation);

						}
					}
				}

			}
			siteSolutionOpportunityBean.setSiteProduct(prodName);
			siteSolutionOpportunityBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			siteSolutionOpportunityBean.setSourceSystem(SFDCConstants.OPTIMUS);
			siteSolutionOpportunityBean.setProductSolutionCode(productSolution.getSolutionCode());
			siteSolutionOpportunityBean
					.setSourceSytemTransactionId(SFDCConstants.OPTIMUS + productSolution.getSolutionCode());

			siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);
			if (siteOpportunityLocations != null && !siteOpportunityLocations.isEmpty()) {
				String sfdcProductId = getProductServiceIdByProductName(quoteToLe.getQuote().getId(), prodName);
				if (sfdcProductId != null && quoteToLe.getTpsSfdcOptyId() != null) {
					isComplete = CommonConstants.BACTIVE;
				}
				siteSolutionOpportunityBean.setProductServiceId(sfdcProductId);
				String request = Utils.convertObjectToJson(siteSolutionOpportunityBean);
				LOGGER.info("Request for create site location {}", request);
				persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdateSite, request, isComplete,
						SFDCConstants.UPDATE_SITE, getSequenceNumber(SFDCConstants.UPDATE_SITE));
			}
		}

	}
	
	/**
	 * Method to validate addressdetail
	 * 
	 * @param addressDetail
	 * @return
	 */
	public AddressDetail validateAddressDetail(AddressDetail addressDetail) {
		if (Objects.isNull(addressDetail.getAddressLineOne()))
			addressDetail.setAddressLineOne("");
		if (Objects.isNull(addressDetail.getAddressLineTwo()))
			addressDetail.setAddressLineTwo("");
		if (Objects.isNull(addressDetail.getCity()))
			addressDetail.setCity("");
		if (Objects.isNull(addressDetail.getCountry()))
			addressDetail.setCountry("");
		if (Objects.isNull(addressDetail.getPincode()))
			addressDetail.setPincode("");
		if (Objects.isNull(addressDetail.getLocality()))
			addressDetail.setLocality("");
		if (Objects.isNull(addressDetail.getState()))
			addressDetail.setState("");
		return addressDetail;
	}
	
	private Map<String,Double> getFinalPriceForTheSiteProductWise(String productName, List<QuoteProductComponent> quoteProductComponents,Integer quoteId) {
		Double finalMrc = 0D;
		Double finalNrc = 0D;
		Double finalArc = 0D;
		Map<String,Double> map = new HashMap<>();
		List<QuoteProductComponent> qpcs = new ArrayList<>();
		if (productName != null && productName.replace(" ", "").equalsIgnoreCase(IzosdwanCommonConstants.IZOSDWAN)) {
			qpcs = quoteProductComponents.stream()
					.filter(qpc -> (qpc.getMstProductComponent().getName().equals(FPConstants.CPE.toString())
							|| qpc.getMstProductComponent().getName().equals(FPConstants.LICENSE_COST.toString())))
					.collect(Collectors.toList());
			
		} else {
			qpcs = quoteProductComponents.stream()
					.filter(qpc -> !(qpc.getMstProductComponent().getName().equals(FPConstants.CPE.toString())
							|| qpc.getMstProductComponent().getName().equals(FPConstants.LICENSE_COST.toString())))
					.collect(Collectors.toList());
		}
		if(qpcs!=null && !qpcs.isEmpty()) {
			for(QuoteProductComponent qpc : qpcs) {
				List<QuotePrice> price = quotePriceRepository.findByReferenceIdAndQuoteIdOrderByIdDesc(qpc.getId().toString(), quoteId);
				if (price != null && !price.isEmpty()) {
					QuotePrice quotePrice = price.get(0);
					finalMrc += quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D;
					finalArc += quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D;
					finalNrc += quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D;
				}
			}
		}
		map.put("arc", finalArc);
		map.put("nrc", finalNrc);
		map.put("mrc", finalMrc);
		return map;
	}
	
	private String getReferenceName(String product) {
		if (product.equals(IzosdwanCommonConstants.IZOSDWAN_NAME)) {
			return IzosdwanCommonConstants.IZOSDWAN_SITES;
		}
		if (product.equals(IzosdwanCommonConstants.CGW)) {
			return IzosdwanCommonConstants.IZOSDWAN_CGW;
		}
		//Need to add for MSS
		return IzosdwanCommonConstants.IZOSDWAN_SITES;
	}
	
	public String getProductServiceIdByProductNameBasedOnFlag(Integer quoteId, String productName,Boolean isNew) {
	
		String displayValue = CommonConstants.EMPTY;
		switch (productName) {
		case IzosdwanCommonConstants.IZOSDWAN_NAME:
			displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
			break;
		case IzosdwanCommonConstants.CGW:
			displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
			break;
		case "IAS":
			if(isNew) {
				displayValue = IzosdwanCommonConstants.IASSFDCNewProductId;
			}else {
				displayValue = IzosdwanCommonConstants.IASSFDCProductId;
			}
		//	displayValue = IzosdwanCommonConstants.IASSFDCProductId;
			break;
		case "GVPN":
			if(isNew) {
				displayValue = IzosdwanCommonConstants.GVPNSFDCNewProductId;
			}else {
				displayValue = IzosdwanCommonConstants.GVPNSFDCProductId;
			}
		//	displayValue = IzosdwanCommonConstants.GVPNSFDCProductId;
			break;
		case IzosdwanCommonConstants.BYON_INTERNET_PRODUCT:
			displayValue = IzosdwanCommonConstants.ILLBYONSFDCProductId;
			break;
		case IzosdwanCommonConstants.BYON_MPLS_PRODUCT:
			displayValue = IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTID;
			break;
		case IzosdwanCommonConstants.IP_TRANSIT_PRODUCT:
		case IzosdwanCommonConstants.DIA_PRODUCT:
			if(isNew) {
				displayValue = IzosdwanCommonConstants.IPTRANSITSFDCNewProductId;
			}else {
				displayValue = IzosdwanCommonConstants.IPTRANSITSFDCProductId;
			}
		//	displayValue = IzosdwanCommonConstants.IPTRANSITSFDCProductId;
			break;
		case IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT:
			if(isNew) {
				displayValue = IzosdwanCommonConstants.IPTRANSITSFDCNewProductId;
			}else {
				displayValue = IzosdwanCommonConstants.IPTRANSITSFDCProductId;
			}
		//	displayValue = IzosdwanCommonConstants.IWANSFDCProductId;
			break;
		case IzosdwanCommonConstants.VUTM:
			displayValue = IzosdwanCommonConstants.VutmSFDCProductId;
			break;
		case IzosdwanCommonConstants.VPROXY:
			displayValue = IzosdwanCommonConstants.VproxySFDCProductId;
			break;
		default:
			break;
		}
		List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
				.findByDisplayValueAndQuote_id(displayValue, quoteId);
		if(quoteIzoSdwanAttributeValues!=null && !quoteIzoSdwanAttributeValues.isEmpty()) {
			return quoteIzoSdwanAttributeValues.get(0).getAttributeValue();
		}
		return null;
	
	}
	public String getProductServiceIdByProductName(Integer quoteId, String productName) {
		String displayValue = CommonConstants.EMPTY;
		switch (productName) {
		case IzosdwanCommonConstants.IZOSDWAN_NAME:
			displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
			break;
		case IzosdwanCommonConstants.CGW:
			displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
			break;
		case "IAS":
			displayValue = IzosdwanCommonConstants.IASSFDCProductId;
			break;
		case "GVPN":
			displayValue = IzosdwanCommonConstants.GVPNSFDCProductId;
			break;
		case IzosdwanCommonConstants.BYON_INTERNET_PRODUCT:
			displayValue = IzosdwanCommonConstants.ILLBYONSFDCProductId;
			break;
		case IzosdwanCommonConstants.BYON_MPLS_PRODUCT:
			displayValue = IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTID;
			break;
		case IzosdwanCommonConstants.IP_TRANSIT_PRODUCT:
			displayValue = IzosdwanCommonConstants.IPTRANSITSFDCProductId;
			break;
		case IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT:
			displayValue = IzosdwanCommonConstants.IWANSFDCProductId;
			break;
		case IzosdwanCommonConstants.VUTM:
			displayValue = IzosdwanCommonConstants.VutmSFDCProductId;
			break;
		case IzosdwanCommonConstants.VPROXY:
			displayValue = IzosdwanCommonConstants.VproxySFDCProductId;
			break;
		default:
			break;
		}
		List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
				.findByDisplayValueAndQuote_id(displayValue, quoteId);
		if(quoteIzoSdwanAttributeValues!=null && !quoteIzoSdwanAttributeValues.isEmpty()) {
			return quoteIzoSdwanAttributeValues.get(0).getAttributeValue();
		}
		return null;
	}
	
	protected void setSfdcAccId(CreateRequestV1Sfdc opportunityBean, CustomerDetailsBean customerDetails) {
        for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
            if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
                opportunityBean.setAccountCuid(attribute.getValue());
                break;
            }
        }
    }
	
	private String getCustomerLeCuid(Integer customerLeId) {
		List<String> customerLeIdsList = new ArrayList<>();
		String cuid = accountCuid;
		try {

			String customerLeIdsCommaSeparated = StringUtils.EMPTY;
			customerLeIdsList.add(customerLeId.toString());
			customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
			LOGGER.debug("MDC Filter token value in before Queue call processFeasibility {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
			CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
			if (null != cLeBean) {
				cuid = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
			}

		} catch (Exception e) {
			LOGGER.error("error on getting cuid", e);
		}
		return cuid;

	}
}
