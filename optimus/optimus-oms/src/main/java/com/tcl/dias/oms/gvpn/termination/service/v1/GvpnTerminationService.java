package com.tcl.dias.oms.gvpn.termination.service.v1;

import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.serviceinventory.beans.SIAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.Site;
import com.tcl.dias.oms.beans.TerminatedServicesBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteAudit;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteAuditRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.ServiceDetailBean;
import com.tcl.dias.oms.macd.beans.TerminationRequest;
import com.tcl.dias.oms.macd.beans.TerminationResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pdf.beans.TRFBean;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.termination.service.v1.TerminationETCPricingService;
import com.tcl.dias.oms.termination.service.v1.TerminationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class GvpnTerminationService extends GvpnQuoteService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(GvpnTerminationService.class);

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;
	
	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;
	
	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;
	

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Value("${attachment.requestId.queue}")
	String attachmentRequestIdQueue;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	SpringTemplateEngine templateEngine;


	@Value("${cof.auto.upload.path}")
	String cofAutoUploadPath;
	
	 @Value("${rabbitmq.customer.contact.details.queue}")
	 String customerLeContactQueueName;
	 
	 @Autowired
	 ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;
	 
	 private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";
	 
	 @Value("${process.terminate.ten.percent}")
	 String o2cTenPercentQueue;
	 
	 @Value("${rabbitmq.customercode.customerlecode.queue}")
		String customerCodeCustomerLeCodeQueue;

	 @Autowired
	 protected GvpnQuotePdfService gvpnQuotePdfService;
	 
	 @Value("${odr.attachment.details}")
	    String attachmentDetails;
	 
	 @Value("${rabbitmq.odr.process.queue}")
		String odrProcessQueue;

	 @Autowired
	 TerminationService terminationService;

	 @Autowired
	 TerminationETCPricingService terminationETCPricingService;
	 
		@Autowired
		QuoteAuditRepository quoteAuditRepository;
		
		@Value("${rabbitmq.quote.arch.request}")
		String taskArchivalRequest;
		
	@Autowired
	OmsUtilService omsUtilService;
	

	@Value("${rabbitmq.si.order.details.inactive.queue}")
	String serviceDetailsInactiveQueue;
	
	/**
	 *
	 * * @author Mansi Bedi Method for handling termination request to create quote
	 * 	 *         with termination type
	 * @param terminationRequest
	 * @param nsVal
	 * @return
	 * @throws TclCommonException
	 */
	public MacdQuoteResponse createTerminationQuoteRequest(MacdQuoteRequest terminationRequest, Boolean nsVal) throws TclCommonException, Exception {
		try {
			validateTerminationQuoteRequest(terminationRequest);
			return createTerminationQuote(terminationRequest,nsVal);
		}  catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 *
	 * @param terminationRequest
	 * @throws TclCommonException
	 */
	private void validateTerminationQuoteRequest(MacdQuoteRequest terminationRequest) throws Exception {

		List<TerminatedServicesBean> terminatedServices = null;
		Map<String, List<String>> existingQuotes = new HashMap<>();
		try {
			if (Objects.nonNull(terminationRequest)) {
				if (terminationRequest.getServiceDetails() == null || terminationRequest.getServiceDetails().isEmpty()) {
					LOGGER.info("Validity failed : Service details empty");
					throw new TclCommonException(ExceptionConstants.TERMINATION_REQUEST_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);
				} else {
					List<String> serviceIdList = terminationRequest.getServiceDetails().stream().filter(Objects::nonNull).map(ServiceDetailBean::getServiceId).collect(Collectors.toList());
					LOGGER.info("Termination Quote creation request recieved for service Ids : {}", serviceIdList);
					terminatedServices = terminationService.checkForExistingQuotesByServiceIds(serviceIdList, "GVPN");
					if (Objects.nonNull(terminatedServices) && !terminatedServices.isEmpty()) {
						terminatedServices.stream().filter(Objects::nonNull).forEach(service -> {
							List<String> quoteCodes = new ArrayList<>();
							if(Objects.nonNull(existingQuotes.get(service.getServiceId()))) {
								List<String> currentCodes = existingQuotes.get(service.getServiceId());
								quoteCodes.addAll(currentCodes);
								quoteCodes.add(service.getQuoteCode());
								existingQuotes.put(service.getServiceId(), quoteCodes);
							} else {
								existingQuotes.put(service.getServiceId(), Arrays.asList(service.getQuoteCode()));
							}
						});
						LOGGER.info("Termination Quote already exists : {}", existingQuotes.values());
						throw new TclCommonException(ExceptionConstants.DUPLICATE_TERMINATION_INITIATED,
								ResponseResource.R_CODE_ERROR);
					}
				}
				
				validateQuoteDetail(terminationRequest.getQuoteRequest());// validating the input for create Quote
			} else {
				throw new TclCommonException(ExceptionConstants.TERMINATION_REQUEST_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			} 
		} catch (Exception e) {
			  if(e.getMessage() != null && e.getMessage().startsWith("Termination Quote already exists for selected service(s)")) { 
				List<String> errorMsg = new ArrayList<>();
				existingQuotes.entrySet().forEach(map->{
					errorMsg.add(map.getKey()+ " -- "+map.getValue().toString());
				});
				  String errorMessage = "Termination Quote already exists for selected service(s) : ".concat(errorMsg.toString());
				  throw new Exception(errorMessage);
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public MacdQuoteResponse createTerminationQuote(MacdQuoteRequest request,Boolean nsVal) throws TclCommonException, ParseException {
		LOGGER.info("Inside method createTerminationQuote");
		MacdQuoteResponse terminationResponse = new MacdQuoteResponse();
		QuoteResponse response = null;
		Integer erfCustomerIdInt = null;
		Integer erfCustomerLeIdInt = null;
		String endCustomerName=null;
		User user = getUserId(Utils.getSource());

		if (Objects.nonNull(user)) {
			LOGGER.info("MDC Filter token value in before Queue call createTerminationQuote {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String serviceIds = request.getServiceDetails().stream().map(i -> i.getServiceId().toString().trim())
					.collect(Collectors.joining(","));
			LOGGER.info("service Ids passed {}", serviceIds);
			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(serviceDetailsInactiveQueue, serviceIds);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
			List<SIServiceDetailsBean> serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
			response = new QuoteResponse();
			if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
				if (serviceDetailsList.stream().findFirst().isPresent()) {
					erfCustomerIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerId();
					erfCustomerLeIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerLeId();
					endCustomerName=serviceDetailsList.stream().findFirst().get().getErfCustomerName();
				}


			}
			if(Objects.isNull(nsVal)) {
				nsVal = false;
			}
			LOGGER.info("Processing quote =======");
			QuoteToLe quoteTole = processQuote(request.getQuoteRequest(), erfCustomerIdInt, user,nsVal);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				createTerminationSpecificQuoteToLe(quoteTole, serviceDetailsList, erfCustomerLeIdInt);
				if(Objects.nonNull(request.getQuoteRequest().getIsMulticircuit())&&CommonConstants.BACTIVE.equals(request.getQuoteRequest().getIsMulticircuit()))
					quoteTole.setIsMultiCircuit(request.getQuoteRequest().getIsMulticircuit());
				quoteTole.setFinalArc(quoteTole.getFinalArc() != null ? quoteTole.getFinalArc() : 0D);
				quoteTole.setFinalMrc(quoteTole.getFinalMrc() != null ? quoteTole.getFinalMrc() : 0D);
				quoteTole.setFinalNrc(quoteTole.getFinalNrc() != null ? quoteTole.getFinalNrc() : 0D);
				quoteToLeRepository.save(quoteTole);
				LOGGER.info("QuoteToLe term " + quoteTole.getTermInMonths());

				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
					serviceDetailsList.stream().forEach(serviceDetail -> {
						LOGGER.info("serviceDetail :: {}", serviceDetail);
						QuoteIllSiteToService siteToService = new QuoteIllSiteToService();
						siteToService.setAllowAmendment("NA");
						siteToService.setErfServiceInventoryParentOrderId(serviceDetail.getReferenceOrderId());
						siteToService.setErfServiceInventoryServiceDetailId(serviceDetail.getId());
						siteToService.setErfServiceInventoryTpsServiceId(serviceDetail.getTpsServiceId());
						siteToService.setQuoteToLe(quoteTole);
						siteToService.setTpsSfdcParentOptyId(serviceDetail.getParentOpportunityId());
						if(serviceDetail.getLinkType() != null && (PDFConstants.PRIMARY.equalsIgnoreCase(serviceDetail.getLinkType()) || MACDConstants.SINGLE.equalsIgnoreCase(serviceDetail.getLinkType()) ))
							siteToService.setType(PDFConstants.PRIMARY);
						else
							siteToService.setType(PDFConstants.SECONDARY);
						quoteIllSiteToServiceRepository.save(siteToService);
						
						
						QuoteSiteServiceTerminationDetails terminationDetail = new QuoteSiteServiceTerminationDetails();
						terminationDetail.setQuoteIllSiteToService(siteToService);
						terminationDetail.setTerminatedParentOrderCode(serviceDetail.getOrderCode());
						terminationDetail.setQuoteToLeId(quoteTole.getId());
						terminationDetail.setCreatedBy(user.getId());
						terminationDetail.setCreatedTime(new Date());
						quoteSiteServiceTerminationDetailsRepository.save(terminationDetail);
					});

				}

				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteTole.getId());
				if (Objects.isNull(macdDetail)) {
					LOGGER.info("Create termination order detail for quoteToLe {},", quoteTole.getId());
					createTerminationEntryInMacdDetail(quoteTole, request.getCancellationDate(),
							request.getCancellationReason());
				} else {
					macdDetail.setCreatedTime(new Date());
					macdDetailRepository.save(macdDetail);
				}
				terminationResponse.setQuoteCategory(quoteTole.getQuoteCategory());
				terminationResponse.setQuoteType(quoteTole.getQuoteType());

				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());

				if (request.getQuoteRequest().getQuoteId() == null && Objects.isNull(request.getQuoteRequest().getEngagementOptyId())) {
					// Triggering Sfdc Creation for termination
					//	omsSfdcService.processCreateOpty(quoteTole, request.getQuoteRequest().getProductName());
				}

					LOGGER.info("Persisting le related attributes for termination");
					CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
					customerLeAttributeRequestBean.setCustomerLeId(erfCustomerLeIdInt);
					customerLeAttributeRequestBean.setProductName("IAS");
					LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
							Utils.convertObjectToJson(customerLeAttributeRequestBean));
					if (StringUtils.isNotEmpty(customerLeAttributes)) {
						updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils
								.convertJsonToObject(customerLeAttributes, CustomerLeDetailsBean.class), quoteTole);
					}

					String spName = returnServiceProviderName(quoteTole.getErfCusSpLegalEntityId());
					if (StringUtils.isNotEmpty(spName)) {
						processAccount(quoteTole, spName,
								LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
					}

				// processQuoteAccessPermissions(user, quoteTole);  --- ??
					
					List<String> customerLeIdList = new ArrayList<>();
					customerLeIdList.add(Objects.nonNull(quoteTole.getErfCusCustomerLegalEntityId()) ? quoteTole.getErfCusCustomerLegalEntityId().toString() : null);
					String customerLeIdsStringList = customerLeIdList.stream().collect(Collectors.joining(","));
					LOGGER.info("customer le id string list{}", customerLeIdsStringList);
					String codeLeCoderesponse = (String) mqUtils.sendAndReceive(customerCodeCustomerLeCodeQueue, customerLeIdsStringList);
					LOGGER.info("Response from Customer {}", codeLeCoderesponse);
					ObjectStorageListenerBean[] objStorageListenerBeanArray = (ObjectStorageListenerBean[]) Utils
							.convertJsonToObject(codeLeCoderesponse, ObjectStorageListenerBean[].class);
					List<ObjectStorageListenerBean> objStorageListenerBeanList = Arrays.asList(objStorageListenerBeanArray);
					
					if (Objects.nonNull(objStorageListenerBeanList) && !objStorageListenerBeanList.isEmpty()) {
						processAccount(quoteTole, objStorageListenerBeanList.get(0).getCustomerCode(),
								LeAttributesConstants.CUSTOMER_CODE.toString());
						processAccount(quoteTole, objStorageListenerBeanList.get(0).getCustomerLeCode(),
								LeAttributesConstants.CUSTOMER_LE_CODE.toString());
					}

			}

		}
		terminationResponse.setQuoteResponse(response);
		return terminationResponse;


	}

	/**
	 *
	 * @param quoteTole
	 * @param serviceDetailsBeanList
	 * @param erfCustomerLeIdInt
	 * @return
	 */
	private QuoteToLe createTerminationSpecificQuoteToLe(QuoteToLe quoteTole,
														 List<SIServiceDetailsBean> serviceDetailsBeanList, Integer erfCustomerLeIdInt) {
		LOGGER.info("Inside method createTerminationSpecificQuoteToLe");
		Integer erfSpLeId = null;
		Integer erfCustCurrencyId = null;
		Double maxContractTerm = null;
		Integer parentSfdcOptyId = null;
		String currencyCode = null;
		SIServiceDetailsBean firstSiServiceDetailsBean = null;
		if (serviceDetailsBeanList != null && !serviceDetailsBeanList.isEmpty()) {
			if (serviceDetailsBeanList.stream().findFirst().isPresent()) {
				firstSiServiceDetailsBean=serviceDetailsBeanList.stream().findFirst().get();
				LOGGER.info("service details bean is ---> {} " , firstSiServiceDetailsBean);

				erfSpLeId = firstSiServiceDetailsBean.getErfSpLeId();
				erfCustCurrencyId = firstSiServiceDetailsBean.getCustomerCurrencyId();
				parentSfdcOptyId = firstSiServiceDetailsBean.getParentOpportunityId();
				if(firstSiServiceDetailsBean.getBillingCurrency() != null)
					currencyCode = firstSiServiceDetailsBean.getBillingCurrency();
			}

			Optional<SIServiceDetailsBean> siServiceDetail = serviceDetailsBeanList.stream()
					.min(Comparator.comparing(SIServiceDetailsBean::getContractTerm));
			if (siServiceDetail.isPresent())
				maxContractTerm = siServiceDetail.get().getContractTerm();

		}

		LOGGER.info("sp le id {}, currencyId {}, maxContract term {}, parent opty id {} from service inventory", erfSpLeId,
				erfCustCurrencyId, maxContractTerm, parentSfdcOptyId);

	//	quoteTole.setQuoteCategory(quoteCategory);
		quoteTole.setQuoteType(MACDConstants.TERMINATION_SERVICE);
		quoteTole.setSourceSystem(MACDConstants.SOURCE_SYSTEM);
		quoteTole.setErfCusCustomerLegalEntityId(erfCustomerLeIdInt);
		quoteTole.setErfCusSpLegalEntityId(erfSpLeId);
		quoteTole.setStage(QuoteStageConstants.TERMINATION_CREATED.getConstantCode());
		quoteTole.setCurrencyId(erfCustCurrencyId);
		quoteTole.setTermInMonths(maxContractTerm + " months");
		quoteTole.setTpsSfdcParentOptyId(parentSfdcOptyId);
		if(currencyCode != null)
			quoteTole.setCurrencyCode(currencyCode);
		return quoteTole;
	}



	/**
	 * Method to persist termination details in macd details table
	 *
	 * @param quoteToLe
	 * @param cancellationDate
	 * @param cancellationReason
	 * @throws ParseException
	 */
	public void createTerminationEntryInMacdDetail(QuoteToLe quoteToLe, String cancellationDate, String cancellationReason)
			throws ParseException {

		if (Objects.nonNull(quoteToLe)) {
			List<String> serviceDetailsList = macdUtils.getAllServiceIdListBasedOnQuoteToLe(quoteToLe);
			serviceDetailsList.stream().forEach(serviceId -> {
				MacdDetail macdDetail = new MacdDetail();
				macdDetail.setQuoteToLeId(quoteToLe.getId());

			/*	if (quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.TERMINATION_SERVICE)) {

					if (Objects.nonNull(cancellationDate)) {
						Date date;
						try {
							date = new SimpleDateFormat("dd/MM/yyyy").parse(cancellationDate);
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
						}
						macdDetail.setCancellationDate(date);
					}
					macdDetail.setCancellationReason(cancellationReason);
				}*/
				macdDetail.setTpsServiceId(serviceId);
				macdDetail.setCreatedBy(quoteToLe.getQuote().getCreatedBy().toString());
				macdDetail.setCreatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
				macdDetail.setOrderCategory(quoteToLe.getQuoteCategory());
				macdDetail.setOrderType(quoteToLe.getQuoteType());
				macdDetail.setIsActive(quoteToLe.getQuote().getStatus());
				macdDetail.setStage(MACDConstants.MACD_ORDER_IN_PROGRESS);
				if (Objects.nonNull(quoteToLe.getTpsSfdcParentOptyId()))
					macdDetail.setTpsSfdcParentOptyId(quoteToLe.getTpsSfdcParentOptyId().toString());
				macdDetail.setUpdatedBy(quoteToLe.getQuote().getCreatedBy().toString());
				macdDetail.setUpdatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
				macdDetailRepository.save(macdDetail);
			});
		}

	}

	public TerminationResponse updateTerminatingServiceDetails(TerminationRequest request) throws TclCommonException {
		LOGGER.info("updateTerminatingServiceDetails request {}", request);
		
		TerminationResponse response = new TerminationResponse();
		
		validateTerminationRequest(request);
		
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(request.getQuoteToLeId());
		if(!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		List<String> etcApplicableServiceIds=new ArrayList<>();

		request.getTerminatingServiceDetails().stream().forEach(service -> {
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(service.getServiceId(), quoteToLe.get());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {

				QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetail = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToServiceList.get(0));
				if(quoteSiteServiceTerminationDetail == null)
					quoteSiteServiceTerminationDetail = new QuoteSiteServiceTerminationDetails();
				else {
					LOGGER.info("EDC from db {} ,from request payload {}", (quoteSiteServiceTerminationDetail.getEffectiveDateOfChange()), service.getEffectiveDateOfChange());
					if (quoteSiteServiceTerminationDetail.getEffectiveDateOfChange() != null) {
						if(quoteSiteServiceTerminationDetail.getEffectiveDateOfChange().compareTo(service.getEffectiveDateOfChange()) != 0){
							quoteSiteServiceTerminationDetail.setEffectiveDateofChangeChanged(CommonConstants.ACTIVE);
							LOGGER.info("Setting isEffectiveDateChanged as 1");
						}
					}
					else {
						quoteSiteServiceTerminationDetail.setEffectiveDateofChangeChanged(CommonConstants.ACTIVE);
						LOGGER.info("Setting isEffectiveDateChanged as 1 , EDC from DB is null");
					}
				}

				Double etCharges=0D;
				LOGGER.info("service id {} sitetoserviceid {}", service.getServiceId(), quoteIllSiteToServiceList.get(0).getId());
				quoteSiteServiceTerminationDetail.setQuoteIllSiteToService(quoteIllSiteToServiceList.get(0));
				quoteSiteServiceTerminationDetail.setCommunicationReceipient(service.getCommunicationReceipient());
				quoteSiteServiceTerminationDetail.setCsmNonCsmContactNumber(service.getCsmNonCsmContactNumber());
				quoteSiteServiceTerminationDetail.setCsmNonCsmEmail(service.getCsmNonCsmEmail());
				quoteSiteServiceTerminationDetail.setCsmNonCsmName(service.getCsmNonCsmName());
				quoteSiteServiceTerminationDetail.setCustomerMailReceivedDate(service.getCustomerMailReceivedDate());
				quoteSiteServiceTerminationDetail.setEffectiveDateOfChange(service.getEffectiveDateOfChange());
				quoteSiteServiceTerminationDetail.setEtcApplicable(service.getEtcApplicable());
				quoteSiteServiceTerminationDetail.setHandoverTo(service.getHandoverTo());
				quoteSiteServiceTerminationDetail.setInternalCustomer(service.getInternalCustomer());
				quoteSiteServiceTerminationDetail.setLocalItContactEmailId(service.getLocalItContactEmailId());
				quoteSiteServiceTerminationDetail.setLocalItContactName(service.getLocalItContactName());
				quoteSiteServiceTerminationDetail.setLocalItContactNumber(service.getLocalItContactNumber());
				quoteSiteServiceTerminationDetail.setReasonForTermination(service.getReasonForTermination());
				quoteSiteServiceTerminationDetail.setRequestedDateForTermination(service.getRequestedDateForTermination());
				quoteSiteServiceTerminationDetail.setSubReason(service.getSubReason());
				quoteSiteServiceTerminationDetail.setTermInMonths(service.getTermInMonths());
				quoteSiteServiceTerminationDetail.setTerminationSubtype(service.getTerminationSubtype());
				quoteSiteServiceTerminationDetail.setTerminationSendToTdDate(service.getTerminationSendToTdDate());
				quoteSiteServiceTerminationDetail.setRegrettedNonRegrettedTermination(service.getRegrettedNonRegrettedTermination());
				quoteSiteServiceTerminationDetail.setTerminationRemarks(service.getTerminationRemarks());

				User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
				quoteSiteServiceTerminationDetail.setUpdatedBy(user.getId());
				quoteSiteServiceTerminationDetail.setUpdatedTime(new Date());
				//Trigger pricing engine when ETC is applicable for this service
				if(CommonConstants.BACTIVE.equals(service.getEtcApplicable())) {
					try {
						etCharges = terminationETCPricingService.retrieveETCChargesFromPricing(request.getQuoteToLeId(),service.getServiceId(), DateUtil.convertDateToString(service.getEffectiveDateOfChange()));
						if(quoteSiteServiceTerminationDetail.getActualEtc() != null){
							LOGGER.info("ETC from DB {} ,from pricing engine {} for serviceId {}",quoteSiteServiceTerminationDetail.getActualEtc(), etCharges,service.getServiceId());

							if(quoteSiteServiceTerminationDetail.getActualEtc().equals(etCharges)) {
								LOGGER.info("Before method sendEtcRevisionMailForTermination for serviceId {}",service.getServiceId());
								terminationService.sendEtcRevisionMailForTermination(request.getQuoteId(),request.getQuoteToLeId(),service.getServiceId());
								quoteSiteServiceTerminationDetail.setIsEtcValueChanged(CommonConstants.ACTIVE);
								LOGGER.info("Setting isEtcValueChanged as 1");
							}
						}
						else {
							quoteSiteServiceTerminationDetail.setIsEtcValueChanged(CommonConstants.ACTIVE);
							LOGGER.info("Setting isEtcValueChanged as 1 , ETC from DB is null for serviceId {}",service.getServiceId());
							etcApplicableServiceIds.add(service.getServiceId());
						}

						quoteSiteServiceTerminationDetail.setActualEtc(etCharges);
						LOGGER.info("BigDecimal Value {}", new BigDecimal(etCharges));
						BigDecimal etcInUSD = omsUtilService.convertCurrencyBigDecimal(quoteToLe.get().getCurrencyCode(), "USD", new BigDecimal(etCharges));

						if(etcInUSD.compareTo(new BigDecimal(250)) < 0) {
							LOGGER.info("etc value less than 250 USD. Overriding final etc as 0, full waiver and etc remarks for service id-quoteLeId {}-{}", 
									quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId(), quoteSiteServiceTerminationDetail.getQuoteToLeId());
							quoteSiteServiceTerminationDetail.setFinalEtc(0D);
							quoteSiteServiceTerminationDetail.setWaiverType("Full Waiver");
							quoteSiteServiceTerminationDetail.setWaiverApprovalRemarks("ETC Auto waived based on the Clause 6.1.1");
						} else {
							LOGGER.info("etc value more than 250 USD. resetting final etc, waiver type and etc remarks for service id-quoteLeId {}-{}", 
									quoteSiteServiceTerminationDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId(), quoteSiteServiceTerminationDetail.getQuoteToLeId());
							quoteSiteServiceTerminationDetail.setFinalEtc(null);
							quoteSiteServiceTerminationDetail.setWaiverType(null);
							quoteSiteServiceTerminationDetail.setWaiverApprovalRemarks(null);
						}
						
						terminationService.updateETCProductComponentAndPricingBean(quoteIllSiteToServiceList.get(0).getQuoteIllSite().getId(), 
								(quoteSiteServiceTerminationDetail.getFinalEtc() != null && quoteSiteServiceTerminationDetail.getFinalEtc() == 0D) ? 0D : etCharges, QuoteConstants.GVPN_SITES.toString(),quoteIllSiteToServiceList.get(0).getType());
					} catch (TclCommonException e) {
						LOGGER.error("Error occurred during pricing engine call - {} - {}",e.getMessage(),e);
					}
				} else {
					quoteSiteServiceTerminationDetail.setActualEtc(etCharges);
					terminationService.updateETCProductComponentAndPricingBean(quoteIllSiteToServiceList.get(0).getQuoteIllSite().getId(), etCharges, QuoteConstants.GVPN_SITES.toString(),quoteIllSiteToServiceList.get(0).getType());
					quoteSiteServiceTerminationDetail.setIsEtcValueChanged(CommonConstants.INACTIVE);
				}
				
			
				quoteSiteServiceTerminationDetailsRepository.save(quoteSiteServiceTerminationDetail);


			}
		});

		//Triggering Mail for serviceIds with ETC Applicable as 1
		if(etcApplicableServiceIds != null && !etcApplicableServiceIds.isEmpty() )
		{ LOGGER.info("Service ids for which ETC Applicability mail is to be sent  :: {}",etcApplicableServiceIds);
			etcApplicableServiceIds.stream().forEach(serviceId -> {
				try {
					LOGGER.info("Before method sendEtcApplicableMailForTermination for service id {}",serviceId);
					terminationService.sendEtcApplicableMailForTermination(request.getQuoteId(),request.getQuoteToLeId(),serviceId);
				} catch (TclCommonException e) {
					LOGGER.info("Error occured while triggering etc applicable mail {}", e);
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				}
			});
		}
		LOGGER.info("Triggering Sfdc Creation for termination =========");
		List<ThirdPartyServiceJob> tpresponse =thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(quoteToLe.get().getQuote().getQuoteCode(), SFDCConstants.CREATE_OPTY, SFDCConstants.SFDC);
		if(tpresponse == null || tpresponse.isEmpty()) {
			LOGGER.info("create entry in tps jobs if new opty is to be created -- not an update case");
			String productName = getProductFamilyNameForQuoteToLe(quoteToLe.get());
			if (quoteToLe.isPresent() && productName != null && !productName.isEmpty()  && (quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit()))) {
				omsSfdcService.processCreateOpty(quoteToLe.get(), productName);
			} else {
				
				if(request.getTerminatingServiceDetails() != null && !request.getTerminatingServiceDetails().isEmpty()) {
					// Create parent dummy opportunity
					omsSfdcService.processCreateOptyDummy(quoteToLe.get(), productName,request.getTerminatingServiceDetails().stream().findFirst().get().getServiceId(), String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()));
					/*
					 * request.getTerminatingServiceDetails().stream().forEach(terminatingService ->
					 * { // Creating child opportunities for each service try {
					 * omsSfdcService.processCreateOptyChildOpty(quoteToLe.get(), productName,
					 * terminatingService.getServiceId()); } catch (TclCommonException e) {
					 * LOGGER.info("Exception when trying to create child opportunity {}", e); } });
					 */

				
				}
			}
		}

		response.setQuoteId(request.getQuoteId());
		response.setQuoteToLeId(quoteToLe.get().getId());
		
		
		return response;
	}

	private void validateTerminationRequest(TerminationRequest request) throws TclCommonException {

		if(request.getQuoteId() == null || request.getQuoteToLeId() == null || request.getTerminatingServiceDetails() == null) {
			LOGGER.info("Validation error for the termination request");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,  ResponseResource.R_CODE_ERROR);
		}
	}


	/**
	 *
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public QuoteBean updateSitesForTermination(QuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId)
			throws TclCommonException {
		QuoteBean quoteBean = null;

		try{
			validateSiteInformation(quoteDetail);
			User user = getUserId(Utils.getSource());
			List<Site> sites = quoteDetail.getSite();
			String productOfferingName = null;
			if (sites.stream().findFirst().isPresent()) {
				productOfferingName = sites.stream().findFirst().get().getOfferingName();
			}
			MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);

			if (quoteToLe.isPresent()) {
				sites.forEach(site -> {
					String productOfferingName1 = site.getOfferingName();
					try {
						processSiteDetail(user, productFamily, quoteToLeProductFamily, site, productOfferingName1);
					} catch (TclCommonException e) {

						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				});
				quoteDetail.setQuoteId(quoteId);
			}

			for(Site siteObj:  sites) {
			MstProductOffering productOfferng = getProductOffering(productFamily, siteObj.getOfferingName(), user);
			ProductSolution productSolution = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(quoteToLeProductFamily, productOfferng);
			List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);
			illSites.stream().forEach(site -> {
				site.setMacdChangeBandwidthFlag(quoteDetail.getPrimaryOrSecondaryOrBoth());
				site.setFeasibility(CommonConstants.BACTIVE);
				site.setArc(site.getArc() != null ? site.getArc() : 0D);
				site.setMrc(site.getMrc() != null ? site.getMrc() : 0D);
				site.setNrc(site.getNrc() != null ? site.getNrc() : 0D);
				site.setFpStatus(FPStatus.MF.toString());
				illSiteRepository.save(site);
				
				//Update Quote Product Component & Quote Price for ETC Charges
				terminationService.updateETCProductComponentAndPricingBean(site, null, user,QuoteConstants.GVPN_SITES.toString());
				
			});
			}

			quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), true, null);
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if(quote.isPresent()) {
				if(Boolean.TRUE.equals(checkO2cEnabled(quote.get()))) {
					quote.get().setOrderToCashOrder(CommonConstants.BACTIVE);
				LOGGER.info(" checkO2cEnabled method IllTerminationService true for quote {}", quote.get().getId());
				} else {
					quote.get().setOrderToCashOrder(CommonConstants.BDEACTIVATE);
					LOGGER.info(" checkO2cEnabled method IllTerminationService false for quote {}", quote.get().getId());
				}
				
				quoteRepository.save(quote.get());
				
			}

		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteBean;

	}
	
	public String generateTRFForm(Integer quoteId, Integer quoteToLeId,Boolean triggero2cCall, HttpServletResponse response) throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = null;
		try {
			LOGGER.info("triggero2cCall {}", triggero2cCall);
			LOGGER.info("Processing cof PDF for quote id {}", quoteId);
			if(triggero2cCall == null)
				triggero2cCall= false;
			QuoteBean quoteDetail = getQuoteDetails(quoteId, null, false,null);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			boolean[] offnetBackHaulProvider = { false };
			boolean[] sendToCsmOrAM = { false };
			boolean[] effectiveDateChanged = { false };
			boolean[] sendToTD = { false };
			boolean[] etcValueChanged = { false };
			List<String> lmTypeList = new ArrayList<>();
			boolean[] o2cCallAlreadyInitiatedOnce = {false};
			//validateOpportunityCreated(quoteToLe.get());
			/*
			 * if(StringUtils.isEmpty(quoteToLe.get().getTpsSfdcOptyId())) throw new
			 * TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,
			 * ResponseResource.R_CODE_ERROR);
			 */
			quoteDetail.getLegalEntities().stream().forEach(quoteToLeBean->{
				quoteToLeBean.getProductFamilies().stream().forEach(prodFamilyBean->{
					prodFamilyBean.getSolutions().stream().forEach(solutionBean->{
						solutionBean.getSites().stream().forEach(siteBean-> {
							siteBean.getQuoteSiteServiceTerminationsBean().stream().forEach(terminationDetailBean->{
								SIServiceDetailDataBean sIServiceDetailDataBean = null;
								try {
									sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(terminationDetailBean.getServiceId());
								} catch (Exception e) {
									LOGGER.info("Error when fetching service details {}", e);
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
								}
								if(sIServiceDetailDataBean != null) {
								
								if(sIServiceDetailDataBean.getLmType() != null) {
									lmTypeList.add(sIServiceDetailDataBean.getLmType());
								} else {
									lmTypeList.add(sIServiceDetailDataBean.getAccessType());
									}
								
								if(sIServiceDetailDataBean.getAttributes() != null && !sIServiceDetailDataBean.getAttributes().isEmpty()) {
								sIServiceDetailDataBean.getAttributes().stream().forEach(attribute ->{
									if("A_END_BACKHAUL_PROVIDER".equalsIgnoreCase(attribute.getName()) || "B_END_BACKHAUL_PROVIDER".equalsIgnoreCase(attribute.getName())) {
										if(StringUtils.isNotBlank(attribute.getValue())) {
											LOGGER.info("Offnet Backhaul provider value {}", attribute.getValue());
											offnetBackHaulProvider[0] = true;	
										} 
									}
								});
								}
								}
						
							if(MACDConstants.SEND_TO_CSM.equalsIgnoreCase(terminationDetailBean.getHandoverTo()) 
									|| MACDConstants.SEND_TO_AM.equalsIgnoreCase(terminationDetailBean.getHandoverTo())){
								sendToCsmOrAM[0] = true;
							}

							if(MACDConstants.SEND_TO_TD.equalsIgnoreCase(terminationDetailBean.getHandoverTo())){
								sendToTD[0] = true;
							}

							if(terminationDetailBean.getO2cCallInitiatedDate() != null)
								o2cCallAlreadyInitiatedOnce[0] = true;

							LOGGER.info("Setting effectiveDateofChangeChanged and etcValueChanged as 0");
							Optional<QuoteSiteServiceTerminationDetails> quoteSiteServiceTerminationDetail = quoteSiteServiceTerminationDetailsRepository.findById(terminationDetailBean.getId());

							if(CommonConstants.ACTIVE.equals(quoteSiteServiceTerminationDetail.get().getEffectiveDateofChangeChanged()))
								effectiveDateChanged[0] = true;

							if(CommonConstants.ACTIVE.equals(quoteSiteServiceTerminationDetail.get().getIsEtcValueChanged()))
								etcValueChanged[0] = true;

							quoteSiteServiceTerminationDetail.get().setEffectiveDateofChangeChanged(0);
							quoteSiteServiceTerminationDetail.get().setIsEtcValueChanged(0);
							quoteSiteServiceTerminationDetailsRepository.save(quoteSiteServiceTerminationDetail.get());

							});
						});
					});
				});
				
			});
			
			//Generate & upload TRF Form - both Single & Multi Circuit
			omsAttachmentBean = terminationService.generateAndUploadTRFToStorage(quoteId, quoteToLeId);
			
			// Conditions on which o2c call needs to be triggered TODO: needs to be handled for multicircuit cases
			// 	1. If the lm type is offnet wireline or offnet wireless or offnetbackhaulprovider is present
			//	2. If user selects 'Send to CSM / Send to AM' in Handover to dropdown, sales task needs to be opened in o2c for negotiation
		//  3. If the effective date of change/Handover to is changed after it is selected for the first time
				
			
				Optional<String> offnetLmType = null;
				if(lmTypeList != null && !lmTypeList.isEmpty()) {
					
					offnetLmType = lmTypeList.stream().filter(lmType -> ("OFFNET WIRELINE".equalsIgnoreCase(lmType) || "OFFNET WIRELESS".equalsIgnoreCase(lmType))).findAny();
					
				}
LOGGER.info("offnetLM Type {}, offnetBackHaulProvider {}, sendToCsmOrAM {}, 	effectiveDateChanged {}, etcValueChanged {}, o2cCallAlreadyInitiatedOnce {}, sendToTD {}", offnetLmType, offnetBackHaulProvider[0], sendToCsmOrAM[0], effectiveDateChanged[0], etcValueChanged[0], o2cCallAlreadyInitiatedOnce[0] ,sendToTD[0] );

            if(!sendToTD[0])
            {
				if( (!o2cCallAlreadyInitiatedOnce[0] &&  ((offnetLmType != null  && offnetLmType.isPresent()) || offnetBackHaulProvider[0] || sendToCsmOrAM[0] || effectiveDateChanged[0] || etcValueChanged[0]))
						|| (o2cCallAlreadyInitiatedOnce[0] && (effectiveDateChanged[0] || etcValueChanged[0])) ){
			 //o2C Call 
			   TerminationNegotiationResponse o2cResponse =  o2cTenPercentCall(quoteId, quoteToLeId, omsAttachmentBean);
			  if(!o2cResponse.getStatus().equalsIgnoreCase("SUCCESS")) 
				  throw new TclCommonException(ExceptionConstants.O2C_TEN_PERCENT_CALL_ERROR,
			  ResponseResource.R_CODE_ERROR);
					}
            }
			// setting quote stage
			if (quoteToLe.get().getStage().equals(QuoteStageConstants.TERMINATION_CREATED.getConstantCode())) {
				quoteToLe.get().setStage(MACDConstants.TERMINATION_REQUEST_RECEIVED);
				quoteToLeRepository.save(quoteToLe.get());
			}
			
			String productName = getProductFamilyNameForQuoteToLe(quoteToLe.get());
			String sfdcStage = null;
			if(quoteToLe.get().getStage() != null && MACDConstants.TERMINATION_REQUEST_RECEIVED.equalsIgnoreCase(quoteToLe.get().getStage()) && !sendToTD[0]) {
				sfdcStage = SFDCConstants.IDENTIFIED_OPTY_STAGE;
			} else {
				sfdcStage = SFDCConstants.VERBAL_AGREEMENT_STAGE;
			}
			if (quoteToLe.isPresent() && productName != null && !productName.isEmpty() && (quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit()))) {
				omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.get().getTpsSfdcOptyId(), sfdcStage, quoteToLe.get());
			} else {
				
				List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteToLe.get().getTpsSfdcOptyId());
				if(optyList != null && !optyList.isEmpty()) {
					// Create parent dummy opportunity
					omsSfdcService.processUpdateOptyDummy(quoteToLe.get(), optyList.get(0).getServiceRefId(),  String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()), new Date(), quoteToLe.get().getTpsSfdcOptyId(), sfdcStage); 
		
				}
			}
			  
			  
		} catch (TclCommonException e) {
			LOGGER.warn("Error in Generate Cof {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return (Objects.nonNull(omsAttachmentBean)?omsAttachmentBean.toString():null);
	}	
	
	
	private CustomerLeContactDetailBean getCustomerLeContact(QuoteToLeBean quoteToLe)
			throws TclCommonException, IllegalArgumentException {
		if (quoteToLe.getCustomerLegalEntityId() != null) {
			LOGGER.info("Customer LE Contact called {}", quoteToLe.getCustomerLegalEntityId());
			String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
					String.valueOf(quoteToLe.getCustomerLegalEntityId()));
			CustomerLeContactDetailBean[] customerLeContacts = (CustomerLeContactDetailBean[]) Utils
					.convertJsonToObject(response, CustomerLeContactDetailBean[].class);
			return customerLeContacts[0];
		} else {
			return null;
		}

	}
	
	protected String getProductFamilyNameForQuoteToLe(QuoteToLe quoteToLe) {
		return quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
	}
	
	
	public TerminationNegotiationResponse o2cTenPercentCall(Integer quoteId, Integer quoteToLeId, OmsAttachmentBean trfAttachmentBean) {
		TerminationNegotiationResponse response = new TerminationNegotiationResponse();
		
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if(quoteToLeOpt.isPresent()) {
			QuoteToLe quoteToLe = quoteToLeOpt.get();
			
			OdrOrderBean odrOrderBean = new OdrOrderBean();
			Optional<User> user = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
			if(user.isPresent())
				odrOrderBean.setCreatedBy(user.get().getUsername());
			List<QuoteSiteServiceTerminationDetails> quoteSiteServiceTerminationList = quoteSiteServiceTerminationDetailsRepository.findByQuoteToLeIdAndIsDeleted(quoteToLe.getId());
			List<QuoteLeAttributeValue> quoteToLeAttributeValuesList = quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteToLe.getId());
			
			odrOrderBean.setOpOrderCode(quoteToLe.getQuote().getQuoteCode());
			odrOrderBean.setErfCustCustomerName(quoteToLe.getQuote().getCustomer().getCustomerName());
			odrOrderBean.setErfCustCustomerId(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
			odrOrderBean.setErfCustSpLeId(quoteToLe.getErfCusSpLegalEntityId());
			odrOrderBean.setErfCustLeId(quoteToLe.getErfCusCustomerLegalEntityId());
			Optional<QuoteLeAttributeValue> leNameOpt = quoteToLeAttributeValuesList.stream().filter(attribute -> LeAttributesConstants.LEGAL_ENTITY_NAME.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())).findFirst();
			if(leNameOpt.isPresent()) {
				odrOrderBean.setErfCustLeName(leNameOpt.get().getAttributeValue());
			} 
			Optional<QuoteLeAttributeValue> spleNameOpt = quoteToLeAttributeValuesList.stream().filter(attribute -> LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())).findFirst();
			if(spleNameOpt.isPresent()) {
				odrOrderBean.setErfCustSpLeName(spleNameOpt.get().getAttributeValue());
			}
			odrOrderBean.setSfdcOptyId(quoteToLe.getTpsSfdcOptyId());
			Optional<QuoteLeAttributeValue> accountId18Opt = quoteToLeAttributeValuesList.stream().filter(attribute -> LeAttributesConstants.ACCOUNT_NO18.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())).findFirst();
			if(accountId18Opt.isPresent()) {
				odrOrderBean.setSfdcAccountId(accountId18Opt.get().getAttributeValue());
			}
			odrOrderBean.setOrderType(MACDConstants.TERMINATION_SERVICE);
			Set<OdrServiceDetailBean> odrServiceDetailSet = new HashSet<>();
			odrOrderBean.setOdrServiceDetails(odrServiceDetailSet);
			quoteSiteServiceTerminationList.stream().forEach(terminatingServiceDetail -> {
				
				OdrServiceDetailBean odrServiceDetailBean = new OdrServiceDetailBean();
				List<OdrAttachmentBean> odrAttachmentBeanList = new ArrayList<>();
				odrServiceDetailBean.setOdrAttachments(odrAttachmentBeanList);
				LOGGER.info("o2cTenPercentCall service id passed {}", terminatingServiceDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
				SIServiceDetailDataBean sIServiceDetailDataBean;
				try {
					sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(terminatingServiceDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
				} catch (TclCommonException e) {
					LOGGER.info("Error when o2cTenPercentCall {}", e);
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				}
				Optional<SIAttributeBean> llArrangeByOpt = Optional.empty();
				Optional<SIAttributeBean> shardLmReqOpt = Optional.empty();
				Optional<SIAttributeBean> siAttributeBeanOpt = Optional.empty();
				if(sIServiceDetailDataBean.getAttributes() != null && !sIServiceDetailDataBean.getAttributes().isEmpty()) {
					// siAttributeBeanOpt is applicable only for M6/POS circuits, not there currently for o2c circuits
					siAttributeBeanOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.PRODUCT_FLAVOUR.equalsIgnoreCase(attribute.getName())).findAny();
					llArrangeByOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.LL_ARRANGE_BY.equalsIgnoreCase(attribute.getName())).findAny();
					shardLmReqOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.SHARED_LM_REQUIRED.equalsIgnoreCase(attribute.getName())).findAny();
				}
				if(siAttributeBeanOpt.isPresent()) {
					LOGGER.info("siAttributeBeanOpt : value {}", siAttributeBeanOpt.get().getValue());
				}
				if(llArrangeByOpt.isPresent())
					LOGGER.info("llArrangeByOpt {}", llArrangeByOpt.get().getValue());
				
				if(shardLmReqOpt.isPresent())
					LOGGER.info("shardLmReqOpt {}", shardLmReqOpt.get().getValue());

				if(sIServiceDetailDataBean.getUuid() != null) {  //LM provider and LM type check done only for O2C ckts.
					List<String> mandatoryComponentAttributeList = Arrays.asList("onnet wireless", "offnet wireless", "offnet wireline", "onnet wireline", "man", "onnetrf", "offnetrf", "offnetwl", "onnetwl");
					if (!MACDConstants.COLOCATED.equalsIgnoreCase(sIServiceDetailDataBean.getLmType())
							&& ((siAttributeBeanOpt.isPresent()
							&& CommonConstants.GVPN.equalsIgnoreCase(siAttributeBeanOpt.get().getValue())) || !siAttributeBeanOpt.isPresent())
							&& ((llArrangeByOpt.isPresent() && !MACDConstants.CUSTOMER.equalsIgnoreCase(llArrangeByOpt.get().getValue())) || !llArrangeByOpt.isPresent())
							&& ((shardLmReqOpt.isPresent() && !CommonConstants.YES.equalsIgnoreCase(shardLmReqOpt.get().getValue())) || !shardLmReqOpt.isPresent())) {
						if (sIServiceDetailDataBean.getLmType() == null && sIServiceDetailDataBean.getAccessType() == null) {
							LOGGER.info("LM TYpe is null from service inventory for service id {}", sIServiceDetailDataBean.getTpsServiceId());
							throw new TclCommonRuntimeException(ExceptionConstants.INVALID_LM_TYPE_DATA_SERVICE_INVENTORY_ERROR, ResponseResource.R_CODE_ERROR);
						}
						if (sIServiceDetailDataBean.getLmType() != null && "offnet".equalsIgnoreCase(sIServiceDetailDataBean.getLmType().toLowerCase())
								&& sIServiceDetailDataBean.getAccessType() != null && !mandatoryComponentAttributeList.contains(sIServiceDetailDataBean.getAccessType().toLowerCase())) {
							LOGGER.info("LM TYpe is OFFNET from service inventory and access type is not a valid value for service id {}", sIServiceDetailDataBean.getTpsServiceId());
							throw new TclCommonRuntimeException(ExceptionConstants.INVALID_LM_TYPE_DATA_SERVICE_INVENTORY_ERROR, ResponseResource.R_CODE_ERROR);
						}
						if (sIServiceDetailDataBean.getAccessProvider() == null) {
							LOGGER.info("LM mile provider is null from service inventory for service id {}", sIServiceDetailDataBean.getTpsServiceId());
							throw new TclCommonRuntimeException(ExceptionConstants.INVALID_LM_PROVIDER_DATA_SERVICE_INVENTORY_ERROR, ResponseResource.R_CODE_ERROR);
						}
					}
				}
				
				/*
					 * if (sIServiceDetailDataBean.getComponents() != null &&
					 * !sIServiceDetailDataBean.getComponents().containsAll(
					 * mandatoryComponentAttributeList)) { throw new
					 * TclCommonRuntimeException(ExceptionConstants.
					 * INVALID_COMPONENT_ATTRIBUTES_DATA_SERVICE_INVENTORY_ERROR,
					 * ResponseResource.R_CODE_ERROR);
					 * 
					 * }
					 */
				odrOrderBean.setTerminationOrderCode(sIServiceDetailDataBean.getOrderCode());
				odrOrderBean.setTpsSfdcCuid(String.valueOf(sIServiceDetailDataBean.getTpsSfdcCuId()));
				odrServiceDetailBean.setTerminationReason(terminatingServiceDetail.getReasonForTermination());
				if(sIServiceDetailDataBean.getLmType() != null) {
					odrServiceDetailBean.setLmType(sIServiceDetailDataBean.getLmType());
					} else {
					odrServiceDetailBean.setLmType(sIServiceDetailDataBean.getAccessType());
					}
			//	odrServiceDetailBean.setOffnetBackhaul(offnetBackhaul); -- Not needed to be sent as confirmed by Rashmi
				if(MACDConstants.SEND_TO_AM.equalsIgnoreCase(terminatingServiceDetail.getHandoverTo())
						|| MACDConstants.SEND_TO_CSM.equalsIgnoreCase(terminatingServiceDetail.getHandoverTo())) {
					odrServiceDetailBean.setNegotiationRequired(CommonConstants.YES);
				} else {
				odrServiceDetailBean.setNegotiationRequired(CommonConstants.NO);
				}
				odrServiceDetailBean.setUuid(sIServiceDetailDataBean.getTpsServiceId());
				odrServiceDetailBean.setTerminationEffectiveDate(Objects.nonNull(terminatingServiceDetail.getEffectiveDateOfChange()) ? terminatingServiceDetail.getEffectiveDateOfChange().toString() : null);
				odrServiceDetailBean.setCustomerRequestorDate(Objects.nonNull(terminatingServiceDetail.getRequestedDateForTermination()) ? terminatingServiceDetail.getRequestedDateForTermination().toString() : null);
				LOGGER.info("contract end date {}", sIServiceDetailDataBean.getContractEndDate());
				if(Objects.nonNull(sIServiceDetailDataBean.getContractEndDate())) {
				DateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					date = originalFormat.parse(sIServiceDetailDataBean.getContractEndDate().toString());
				} catch (ParseException e) {
					LOGGER.info("Error in parse exception {}", e);
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				}
				
				String formattedDate = targetFormat.format(date); 
				LOGGER.info("formatted date {}", formattedDate);
				odrServiceDetailBean.setContractEndDate(Objects.nonNull(formattedDate) ? formattedDate : null);
			}
			
				odrServiceDetailBean.setCsmEmail(terminatingServiceDetail.getCsmNonCsmEmail());
				odrServiceDetailBean.setCsmUserName(terminatingServiceDetail.getCsmNonCsmName());
				odrServiceDetailBean.setUuid(terminatingServiceDetail.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
				odrServiceDetailBean.setAccessType(sIServiceDetailDataBean.getAccessType());
				odrServiceDetailBean.setLastmileType(sIServiceDetailDataBean.getLmType());
				odrServiceDetailBean.setErfPrdCatalogProductName(sIServiceDetailDataBean.getErfPrdCatalogProductName());
				odrServiceDetailBean.setErfPrdCatalogParentProductOfferingName(sIServiceDetailDataBean.getErfPrdCatalogParentProductOfferingName());
				odrServiceDetailBean.setErfPrdCatalogOfferingName(sIServiceDetailDataBean.getErfPrdCatalogOfferingName());
				odrServiceDetailBean.setLocalItContactEmail(terminatingServiceDetail.getLocalItContactEmailId());
				odrServiceDetailBean.setLocalItContactMobile(terminatingServiceDetail.getLocalItContactNumber());
				odrServiceDetailBean.setLocalItContactName(terminatingServiceDetail.getLocalItContactName());
				// odrServiceDetailBean.setOrderSubCategory(sIServiceDetailDataBean.get)			
				odrOrderBean.setOrderCategory(sIServiceDetailDataBean.getOrderCategory()); // -- M6 services data?
				odrOrderBean.setParentOrderType(sIServiceDetailDataBean.getOrderCategory());
				
				List<QuoteProductComponent> quoteProductComponentList =
						quoteProductComponentRepository.findByReferenceIdAndReferenceNameAndType(terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getId(), QuoteConstants.GVPN_SITES.toString(), PDFConstants.PRIMARY);
				
				Double[] etcValue = { 0D };
				quoteProductComponentList.stream().filter(quoteProd -> ChargeableItemConstants.ETC_CHARGES.equalsIgnoreCase(quoteProd.getMstProductComponent().getName())).forEach(quoteProdComp ->{
						
								QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(quoteProdComp.getId()), QuoteConstants.COMPONENTS.toString());
								if(quotePrice != null) {
								etcValue[0] = quotePrice.getEffectiveNrc();
								LOGGER.info("Etc value {}", etcValue[0]);
								}
					});
				odrServiceDetailBean.setEtcValue(String.valueOf(etcValue[0]));
				odrServiceDetailBean.setEtcWaiver(CommonConstants.BACTIVE.equals(terminatingServiceDetail.getEtcApplicable()) ? "Yes" : "No");
				
				Set<OdrServiceAttributeBean> serviceAttributeSet = new HashSet<>();
				odrServiceDetailBean.setOdrServiceAttributes(serviceAttributeSet);
				OdrServiceAttributeBean odrServiceAttributeBurstable = new OdrServiceAttributeBean();
				Optional<QuoteProductComponent> burstableComponent = quoteProductComponentList.stream().filter(component->(FPConstants.VPN_PORT.toString().equalsIgnoreCase(component.getMstProductComponent().getName()))).findFirst();
				if(burstableComponent.isPresent()) {
					Optional<QuoteProductComponentsAttributeValue> burstableAttribute = burstableComponent.get().getQuoteProductComponentsAttributeValues().stream().filter(attribute -> (FPConstants.BURSTABLE_BANDWIDTH.toString().equalsIgnoreCase(attribute.getProductAttributeMaster().getName()))).findFirst();
					if(burstableAttribute.isPresent()) {
						odrServiceAttributeBurstable.setAttributeName(FPConstants.BURSTABLE_BANDWIDTH.toString());
						odrServiceAttributeBurstable.setAttributeValue(burstableAttribute.get().getAttributeValues());
						odrServiceAttributeBurstable.setCategory(FPConstants.VPN_PORT.toString());
						odrServiceAttributeBurstable.setIsAdditionalParam(CommonConstants.N);
						serviceAttributeSet.add(odrServiceAttributeBurstable);
					}
				}
				
				
				OdrServiceAttributeBean odrServiceAttributeServiceVariant = new OdrServiceAttributeBean();
				Optional<QuoteProductComponent> iasCommonComponent = quoteProductComponentList.stream().filter(component->(PDFConstants.GVPN_COMMON.toString().equalsIgnoreCase(component.getMstProductComponent().getName()))).findFirst();
				if(iasCommonComponent.isPresent()) {
					Optional<QuoteProductComponentsAttributeValue> serviceVariantAttribute = iasCommonComponent.get().getQuoteProductComponentsAttributeValues().stream().filter(attribute -> (FPConstants.SERVICE_VARIANT.toString().equalsIgnoreCase(attribute.getProductAttributeMaster().getName()))).findFirst();
					if(serviceVariantAttribute.isPresent()) {
						odrServiceAttributeServiceVariant.setAttributeName(FPConstants.SERVICE_VARIANT.toString());
						odrServiceAttributeServiceVariant.setAttributeValue(serviceVariantAttribute.get().getAttributeValues());
						odrServiceAttributeServiceVariant.setCategory(PDFConstants.GVPN_COMMON);
						odrServiceAttributeServiceVariant.setIsAdditionalParam(CommonConstants.N);
						serviceAttributeSet.add(odrServiceAttributeServiceVariant);
						
					}
					
					OdrServiceAttributeBean odrServiceAttributeCPE = new OdrServiceAttributeBean();
					
					Optional<QuoteProductComponentsAttributeValue> cpeAttribute = iasCommonComponent.get().getQuoteProductComponentsAttributeValues().stream().filter(attribute -> (FPConstants.CPE.toString().equalsIgnoreCase(attribute.getProductAttributeMaster().getName()))).findFirst();
					if(cpeAttribute.isPresent()) {
						odrServiceAttributeCPE.setAttributeName(FPConstants.CPE.toString());
						odrServiceAttributeCPE.setAttributeValue(cpeAttribute.get().getAttributeValues());
						odrServiceAttributeCPE.setCategory(PDFConstants.GVPN_COMMON);
						odrServiceAttributeCPE.setIsAdditionalParam(CommonConstants.N);
						serviceAttributeSet.add(odrServiceAttributeCPE);
						
					}
				}
				
				if(sIServiceDetailDataBean.getAttributes() != null && !sIServiceDetailDataBean.getAttributes().isEmpty()) {
				sIServiceDetailDataBean.getAttributes().stream().forEach(attribute -> {
					
					
					if(MACDConstants.LOCAL_LOOP_BW.equalsIgnoreCase(attribute.getName())) {
						OdrServiceAttributeBean odrServiceAttributeLocalLoop = new OdrServiceAttributeBean();
						odrServiceAttributeLocalLoop.setAttributeName(attribute.getName());
						odrServiceAttributeLocalLoop.setAttributeValue(attribute.getValue());
						odrServiceAttributeLocalLoop.setCategory("FEASIBILITY");
						odrServiceAttributeLocalLoop.setIsAdditionalParam(CommonConstants.N);	
						serviceAttributeSet.add(odrServiceAttributeLocalLoop);
					} else if(MACDConstants.BURSTABLE_BW.equalsIgnoreCase(attribute.getName())) {
						OdrServiceAttributeBean odrServiceAttributeBurstableBw = new OdrServiceAttributeBean();
					odrServiceAttributeBurstableBw.setAttributeName(attribute.getName());
					odrServiceAttributeBurstableBw.setAttributeValue(attribute.getValue());
					odrServiceAttributeBurstableBw.setCategory("FEASIBILITY");
					odrServiceAttributeBurstableBw.setIsAdditionalParam(CommonConstants.N);	
					serviceAttributeSet.add(odrServiceAttributeBurstableBw);
					} else if (MACDConstants.SITE_TYPE.equalsIgnoreCase(attribute.getName())) {					
						OdrServiceAttributeBean odrServiceAttributeSiteType = new OdrServiceAttributeBean();
					odrServiceAttributeSiteType.setAttributeName(attribute.getName());
					odrServiceAttributeSiteType.setAttributeValue(attribute.getValue());
					odrServiceAttributeSiteType.setCategory("FEASIBILITY");
					odrServiceAttributeSiteType.setIsAdditionalParam(CommonConstants.N);	
					serviceAttributeSet.add(odrServiceAttributeSiteType);
					} else if(MACDConstants.CPE_BASIC_CHASSIS.equalsIgnoreCase(attribute.getName())) {
						OdrServiceAttributeBean odrServiceAttributeCPEBasicChassis = new OdrServiceAttributeBean();
					odrServiceAttributeCPEBasicChassis.setAttributeName(attribute.getName());
					odrServiceAttributeCPEBasicChassis.setAttributeValue(attribute.getValue());
					odrServiceAttributeCPEBasicChassis.setCategory("FEASIBILITY");
					odrServiceAttributeCPEBasicChassis.setIsAdditionalParam(CommonConstants.N);	
					serviceAttributeSet.add(odrServiceAttributeCPEBasicChassis);
					} else if(MACDConstants.OEM_VENDOR.equalsIgnoreCase(attribute.getName())) {
						OdrServiceAttributeBean odrServiceAttributeoemVendor = new OdrServiceAttributeBean();
						odrServiceAttributeoemVendor.setAttributeName(attribute.getName());
						odrServiceAttributeoemVendor.setAttributeValue(attribute.getValue());
						odrServiceAttributeoemVendor.setCategory("");
						odrServiceAttributeoemVendor.setIsAdditionalParam(CommonConstants.N);	
					serviceAttributeSet.add(odrServiceAttributeoemVendor);
					} else if(MACDConstants.CPE_SERIAL_NO.equalsIgnoreCase(attribute.getName())) {
						OdrServiceAttributeBean odrServiceAttributecpeSerialNo = new OdrServiceAttributeBean();
						odrServiceAttributecpeSerialNo.setAttributeName(attribute.getName());
						odrServiceAttributecpeSerialNo.setAttributeValue(attribute.getValue());
						odrServiceAttributecpeSerialNo.setCategory("");
						odrServiceAttributecpeSerialNo.setIsAdditionalParam(CommonConstants.N);	
						serviceAttributeSet.add(odrServiceAttributecpeSerialNo);
					}
					
				});
				}
				
				if(terminatingServiceDetail.getTerminationSubtype() != null ) {
					OdrServiceAttributeBean odrServiceAttributeSubType = new OdrServiceAttributeBean();
					odrServiceAttributeSubType.setAttributeName("terminationSubType");
					odrServiceAttributeSubType.setAttributeValue(terminatingServiceDetail.getTerminationSubtype());
					odrServiceAttributeSubType.setCategory("");
					odrServiceAttributeSubType.setIsAdditionalParam(CommonConstants.N);	
					serviceAttributeSet.add(odrServiceAttributeSubType);
				}
				if(terminatingServiceDetail.getSubReason() != null) {
					OdrServiceAttributeBean odrServiceAttributeSubReason = new OdrServiceAttributeBean();
					odrServiceAttributeSubReason.setAttributeName("terminationSubReason");
					odrServiceAttributeSubReason.setAttributeValue(terminatingServiceDetail.getSubReason());
					odrServiceAttributeSubReason.setCategory("");
					odrServiceAttributeSubReason.setIsAdditionalParam(CommonConstants.N);	
					serviceAttributeSet.add(odrServiceAttributeSubReason);
				}

				OdrServiceAttributeBean odrServiceAttributeBillingType = new OdrServiceAttributeBean();
				odrServiceAttributeBillingType.setAttributeName("billingType");
				odrServiceAttributeBillingType.setAttributeValue(sIServiceDetailDataBean.getBillingType());
				odrServiceAttributeBillingType.setCategory("");
				odrServiceAttributeBillingType.setIsAdditionalParam(CommonConstants.N);
				serviceAttributeSet.add(odrServiceAttributeBillingType);

				OdrAttachmentBean customerEmailAttachment = new OdrAttachmentBean();
				List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType( "Site", terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getId(), AttachmentTypeConstants.CUSTEMAIL.toString());
					String attachmentResponse;
					try {
						if(omsAttachmentList != null && !omsAttachmentList.isEmpty()) {
						attachmentResponse = (String) mqUtils.sendAndReceive(attachmentDetails,
								String.valueOf(omsAttachmentList.get(0).getErfCusAttachmentId()));
					
					if (attachmentResponse != null) {

						@SuppressWarnings("unchecked")
						Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);

						if (attachmentMapper != null) {
							String[] fileNameSplitter =  ((String) attachmentMapper.get("DISPLAY_NAME")).split("\\.");
							LOGGER.info("customer email split string {}, 1-{}", fileNameSplitter[0], fileNameSplitter[1]);
							customerEmailAttachment.setType(fileNameSplitter[1]);
							customerEmailAttachment.setCategory("TERREQDOC");
							customerEmailAttachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
							customerEmailAttachment.setName((String) attachmentMapper.get("DISPLAY_NAME"));
							odrAttachmentBeanList.add(customerEmailAttachment);
						}
						
					}
						}} catch (TclCommonException e) {
						LOGGER.info("Error when making queue call attachmentDetails CUSTEMAIL{}", e );
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}

				
				OdrAttachmentBean trfAttachment = new OdrAttachmentBean();
				//List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType( "Quotes", terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getId(), AttachmentTypeConstants.TRF.toString());
				String trfAttachmentResponse;
				if(trfAttachmentBean != null) {
					try {
						trfAttachmentResponse = (String) mqUtils.sendAndReceive(attachmentDetails,
								String.valueOf(trfAttachmentBean.getErfCusAttachmentId()));
					
					if (trfAttachmentResponse != null) {

						@SuppressWarnings("unchecked")
						Map<String, Object> attachmentMapper = Utils.convertJsonToObject(trfAttachmentResponse, Map.class);

						if (attachmentMapper != null) {
							String[] fileNameSplitter =  ((String) attachmentMapper.get("DISPLAY_NAME")).split("\\.");
							LOGGER.info("customer email split string {}, 1-{}", fileNameSplitter[0], fileNameSplitter[1]);
							trfAttachment.setType(fileNameSplitter[1]);
							trfAttachment.setCategory("TRF");
							trfAttachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
							trfAttachment.setName((String) attachmentMapper.get("DISPLAY_NAME"));
							odrAttachmentBeanList.add(trfAttachment);
							
						}
						
					}} catch (TclCommonException e) {
						LOGGER.info("Error when making queue call attachmentDetails trf {}", e );
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}
				}
				
				OdrAttachmentBean approvalEmailAttachment = new OdrAttachmentBean();
				List<OmsAttachment> omsAttachmentListAppr = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType( "Site", terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getId(), AttachmentTypeConstants.APPRVEMAIL.toString());
				if(omsAttachmentListAppr != null && !omsAttachmentListAppr.isEmpty()) {
					odrServiceDetailBean.setApprovalMailAvailable(CommonConstants.YES);
				}
					String apprvAttachmentResponse;
					try {
						if(omsAttachmentListAppr != null && !omsAttachmentListAppr.isEmpty()) {
							apprvAttachmentResponse = (String) mqUtils.sendAndReceive(attachmentDetails,
								String.valueOf(omsAttachmentListAppr.get(0).getErfCusAttachmentId()));
					
					if (apprvAttachmentResponse != null) {

						@SuppressWarnings("unchecked")
						Map<String, Object> attachmentMapper = Utils.convertJsonToObject(apprvAttachmentResponse, Map.class);

						if (attachmentMapper != null) {
							LOGGER.info("name {}", (String) attachmentMapper.get("DISPLAY_NAME"));
							String[] fileNameSplitter =  ((String) attachmentMapper.get("DISPLAY_NAME")).split("\\.");
								LOGGER.info("approval email split string {}, 1-{}", fileNameSplitter[0], fileNameSplitter[1]);
								approvalEmailAttachment.setType(fileNameSplitter[1]);
							
								approvalEmailAttachment.setCategory("ETC_WAIVER");
								approvalEmailAttachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
								approvalEmailAttachment.setName((String) attachmentMapper.get("DISPLAY_NAME"));
								odrAttachmentBeanList.add(approvalEmailAttachment);
						}
						
					}
						}} catch (TclCommonException e) {
						LOGGER.info("Error when making queue call attachmentDetails APPRVEMAIL{}", e );
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}


				Set<OdrComponentBean> odrComponentBeans = new HashSet<>();
				OdrComponentBean odrComponentBean = new OdrComponentBean();
				odrComponentBean.setComponentName("LM");
				odrComponentBean.setSiteType("A");
				Set<OdrComponentAttributeBean> odrComponentAttributesBeans = new HashSet<>();
				odrComponentAttributesBeans = mapOdrComponentAttributeToBean(odrComponentAttributesBeans, quoteProductComponentList, terminatingServiceDetail, sIServiceDetailDataBean);
				odrComponentBean.setOdrComponentAttributeBeans(odrComponentAttributesBeans);
				odrComponentBeans.add(odrComponentBean);
				odrServiceDetailBean.setOdrComponentBeans(odrComponentBeans);
				
			    odrServiceDetailSet.add(odrServiceDetailBean);
				
				
			
			});

			LOGGER.info("O2C json constructed ---- {}", odrOrderBean.toString());
			try {
				String queueResponse = (String) mqUtils.sendAndReceive(o2cTenPercentQueue, Utils.convertObjectToJson(odrOrderBean));
				LOGGER.info("ten percent queue response- {}", queueResponse);
				if (StringUtils.isNotBlank(queueResponse)) {
					response = (TerminationNegotiationResponse) Utils.convertJsonToObject(queueResponse, TerminationNegotiationResponse.class);
					 quoteSiteServiceTerminationList.stream().forEach(terminatingServiceDetail -> {
						 terminatingServiceDetail.setO2cCallInitiatedDate(new Date());
						 quoteSiteServiceTerminationDetailsRepository.save(terminatingServiceDetail);
					 });
				}
			} catch (TclCommonException e) {
				LOGGER.info("Exception while fetching o2c ten percent response ");
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}
			
		}
		
		return response;
		
		
		
		
	}
	
	private void validateOpportunityCreated(QuoteToLe quoteToLe) throws TclCommonException {
		
		if(quoteToLe != null) {
			if(quoteToLe.getTpsSfdcOptyId() == null) {
				LOGGER.info("Opportunity not yet created, throwing exception");
				throw new TclCommonException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE, ResponseResource.R_CODE_ERROR);
			}
		}
		
		
			
		}

	
	public QuoteBean getTerminationQuoteDetails(Integer quoteId, String feasibleSites) throws TclCommonException {
		QuoteBean response = null;
		try {
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString()));
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites, true, null);
			List<QuoteToLe> quoteToLeList = getQuoteToLeBasenOnVersion(quote);
			if (!quoteToLeList.isEmpty()) {
				QuoteToLe quoteToLe = quoteToLeList.get(0);
				if (quoteToLe != null) {
					response.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				}
				response.setQuoteType(quoteToLe.getQuoteType());
				response.setQuoteCategory(quoteToLe.getQuoteCategory());
				List<String> serviceIdsList=new ArrayList<>();
				//Get Service Id and order Id list
				serviceIdsList=macdUtils.getServiceIdListBasedOnQuoteToLe(quoteToLe);

				if (CommonConstants.BDEACTIVATE.equals(quoteToLe.getIsMultiCircuit()))
				{
					List<String> tpsServiceId = macdUtils.getServiceIds(quoteToLe);

					if (Objects.nonNull(tpsServiceId) && !tpsServiceId.isEmpty()) {
						LOGGER.info("Service Id: " + tpsServiceId.get(0));
						response.setServiceId(tpsServiceId.get(0));
						List<QuoteIllSiteToService> quoteIllSiteToServiceList=quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(tpsServiceId.get(0),quoteToLe);
						response.setServiceOrderId(quoteIllSiteToServiceList.stream().filter(service -> (!CommonConstants.ACTIVE.equals(service.getIsDeleted())))
								.findFirst().get().getErfServiceInventoryParentOrderId());
					}
				}

				List<Boolean> flagList=new ArrayList<>();
				Boolean[] macdFlagValue={false};
				if(Objects.nonNull(serviceIdsList)&&!serviceIdsList.isEmpty()) {
					serviceIdsList.stream().forEach(serviceId -> {

						try {
							Map<String, String> serviceIds = macdUtils.getRelatedServiceIds(serviceId);
							LOGGER.info("Service ID" + serviceIds);

							Map<String, Object> macdFlag = macdUtils.getMacdInitiatedStatus(serviceIds);
							LOGGER.info("macdFlag" + macdFlag);
							if (Objects.nonNull(macdFlag) && !macdFlag.isEmpty()) {
								if (macdFlag.size() == 1)
									macdFlagValue[0] = (Boolean) macdFlag.get(serviceId);
								else
									macdFlagValue[0] = setMacdFlag(macdFlag, macdFlagValue);
							}
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
						flagList.add(macdFlagValue[0]);
					});


					if (flagList.contains(true))
						response.setIsMacdInitiated(true);
					if (Objects.nonNull(quoteToLe.getIsMultiCircuit())&&quoteToLe.getIsMultiCircuit() == 1)
						response.setIsMultiCircuit(true);
					if (Objects.nonNull(quoteToLe.getIsMultiCircuit())&&quoteToLe.getIsMultiCircuit() == 1) {
						List<String> multiCircuitChangeBandwidthFlag = new ArrayList<>();
						List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByQuoteToLe_IdAndIsDeletedIsNullOrIsDeleted(quoteToLe.getId(),CommonConstants.INACTIVE);
						quoteIllSiteToServices.stream().forEach(quoteIllSiteToService -> {
							if (Objects.nonNull(quoteIllSiteToService) && CommonConstants.BACTIVE.equals(quoteIllSiteToService.getBandwidthChanged()))
								multiCircuitChangeBandwidthFlag.add("true");
							else
								multiCircuitChangeBandwidthFlag.add("false");
						});
						if (multiCircuitChangeBandwidthFlag.contains("false")) {
							response.setIsMulticircuitBandwidthChangeFlag(false);
						} else
							response.setIsMulticircuitBandwidthChangeFlag(true);
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	public Boolean setMacdFlag(Map<String,Object> macdFlag,Boolean[] macdFlagValue)
	{
		macdFlag.entrySet().stream().forEach(entry->{
			Boolean flag=(Boolean)entry.getValue();
			if(flag)
				macdFlagValue[0]=flag;

		});
		return macdFlagValue[0];
	
}

	public String downloadParentCofFromStorageContainer(Integer quoteId, Integer quoteLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = null;
		if (quoteId == null || quoteLeId == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_ID_ERROR, ResponseResource.R_CODE_ERROR);
		}
		try {
			List<QuoteSiteServiceTerminationDetails> quoteSiteServiceTerminationList = quoteSiteServiceTerminationDetailsRepository.findByQuoteToLeIdAndIsDeleted(quoteLeId);
			String parentOrderCode = quoteSiteServiceTerminationList.stream().findFirst().map(QuoteSiteServiceTerminationDetails::getTerminatedParentOrderCode).orElse(StringUtils.EMPTY);
			LOGGER.info("parent order code - {}", parentOrderCode);

			Order orderEntity = orderRepository.findByOrderCode(parentOrderCode);
			if (Objects.nonNull(orderEntity)) {
				LOGGER.info("order is passed --------", orderEntity.getId());
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository
							.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, orderEntity.getId(),
									AttachmentTypeConstants.COF.toString());
					LOGGER.info("oms attachment is present and has value -- {} ", omsAttachmentsList.toString());
					if(omsAttachmentsList.isEmpty()) {
						Quote quoteEntity = orderEntity.getQuote();
						if(quoteEntity != null) {
							omsAttachmentsList = omsAttachmentRepository
									.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteEntity.getId(),
											AttachmentTypeConstants.COF.toString());
						}
					}
					if (!omsAttachmentsList.isEmpty()) {
						tempDownloadUrl = gvpnQuotePdfService.downloadCofFromStorageContainer(null, null, orderEntity.getId(),
								orderEntity.getId(), null);

					}
				} else {
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(orderEntity.getOrderCode());
					LOGGER.info("cof details present and has value --- {}", cofDetails.toString());
					if (cofDetails != null) {
						processDownloadCof(response, cofDetails);
					}
				}
			}


		} catch (Exception e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	/**
	 * processDownloadCof
	 *
	 * @param response
	 * @param cofDetails
	 * @throws IOException
	 */
	private void processDownloadCof(HttpServletResponse response, CofDetails cofDetails) throws IOException {
		Path path = Paths.get(cofDetails.getUriPath());
		String fileName = "Customer-Order-Form - " + cofDetails.getOrderUuid() + ".pdf";
		response.reset();
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
		response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
		Files.copy(path, response.getOutputStream());
		// flushes output stream
		response.getOutputStream().flush();
	}


	public Set<OdrComponentAttributeBean> mapOdrComponentAttributeToBean(Set<OdrComponentAttributeBean> odrComponentAttributeBean, List<QuoteProductComponent> quoteProductComponentList,
																		 QuoteSiteServiceTerminationDetails terminatingServiceDetail, SIServiceDetailDataBean sIServiceDetailDataBean) {
		LOGGER.info("inside method mapOdrComponentAttributeToBean to map attributes ------");
		List<String> listOfAttrs = Arrays.asList(FPConstants.PORT_BANDWIDTH.toString(), FPConstants.LOCAL_LOOP_BW.toString(), FPConstants.CPE_MANAGEMENT_TYPE.toString());
		//LOGGER.info("quoteProductComponentList {}", quoteProductComponentList.size());
		if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
			LOGGER.info("quoteProductComponentList presnt");
			quoteProductComponentList.forEach(component -> {
				listOfAttrs.forEach(attrListValue -> {
					LOGGER.info("attrListValue {}", attrListValue);
					List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
									component.getId(), attrListValue);

					attributes.forEach(attr -> {
						if (attr.getProductAttributeMaster().getName().equals(FPConstants.PORT_BANDWIDTH.toString())) {
							OdrComponentAttributeBean odrComponentAttribute = new OdrComponentAttributeBean();
							odrComponentAttribute.setName("portBandwidth");
							odrComponentAttribute.setValue(attr.getAttributeValues());
							odrComponentAttributeBean.add(odrComponentAttribute);
						}

						if (attr.getProductAttributeMaster().getName().equals(FPConstants.LOCAL_LOOP_BW.toString())) {
							OdrComponentAttributeBean odrComponentAttribute = new OdrComponentAttributeBean();
							odrComponentAttribute.setName("localLoopBandwidth");
							odrComponentAttribute.setValue(attr.getAttributeValues());
							odrComponentAttributeBean.add(odrComponentAttribute);
						}

						if (attr.getProductAttributeMaster().getName().equals(FPConstants.CPE_MANAGEMENT_TYPE.toString())) {
							OdrComponentAttributeBean odrComponentAttribute = new OdrComponentAttributeBean();
							odrComponentAttribute.setName("cpeManagementType");
							odrComponentAttribute.setValue(attr.getAttributeValues());
							odrComponentAttributeBean.add(odrComponentAttribute);
						}


					});

				});
			});

		}
		
				//Adding customerMailReceiveDate in odrComponentAttributes
		
				LOGGER.info("setting customerMailReceiveDate in  odr component attributes ==========");
				OdrComponentAttributeBean odrComponentAttributeCustomerMailDate = new OdrComponentAttributeBean();
				odrComponentAttributeCustomerMailDate.setName("customerMailReceiveDate");
				odrComponentAttributeCustomerMailDate.setValue(terminatingServiceDetail.getCustomerMailReceivedDate() != null ? terminatingServiceDetail.getCustomerMailReceivedDate().toString() : null);
				odrComponentAttributeBean.add(odrComponentAttributeCustomerMailDate);
				
				LOGGER.info("setting regrettedNonRegrettedTermination in  odr component attributes ==========");
				OdrComponentAttributeBean odrComponentAttributeRegretted = new OdrComponentAttributeBean();
				odrComponentAttributeRegretted.setName("regrettedNonRegrettedTermination");
				odrComponentAttributeRegretted.setValue(terminatingServiceDetail.getRegrettedNonRegrettedTermination() != null ? terminatingServiceDetail.getRegrettedNonRegrettedTermination() : null);
				odrComponentAttributeBean.add(odrComponentAttributeRegretted);

		LOGGER.info("setting service related attributes ===========");
		OdrComponentAttributeBean odrComponentAttribute1 = new OdrComponentAttributeBean();
		odrComponentAttribute1.setName("localItContactEmailId");
		odrComponentAttribute1.setValue(terminatingServiceDetail.getLocalItContactEmailId());
		odrComponentAttributeBean.add(odrComponentAttribute1);

		OdrComponentAttributeBean odrComponentAttribute2 = new OdrComponentAttributeBean();
		odrComponentAttribute2.setName(MACDConstants.LASTMILE_PROVIDER);
		odrComponentAttribute2.setValue(sIServiceDetailDataBean.getAccessProvider());
		odrComponentAttributeBean.add(odrComponentAttribute2);

		OdrComponentAttributeBean odrComponentAttribute3 = new OdrComponentAttributeBean();
		odrComponentAttribute3.setName("localItContactName");
		odrComponentAttribute3.setValue(terminatingServiceDetail.getLocalItContactName());
		odrComponentAttributeBean.add(odrComponentAttribute3);

		OdrComponentAttributeBean odrComponentAttribute4 = new OdrComponentAttributeBean();
		odrComponentAttribute4.setName(MACDConstants.LM_TYPE);
		if(sIServiceDetailDataBean.getLmType() != null) {
			odrComponentAttribute4.setValue(sIServiceDetailDataBean.getLmType());
			} else {
			odrComponentAttribute4.setValue(sIServiceDetailDataBean.getAccessType());
			}
		odrComponentAttributeBean.add(odrComponentAttribute4);

		OdrComponentAttributeBean odrComponentAttribute5 = new OdrComponentAttributeBean();
		odrComponentAttribute5.setName(MACDConstants.BW_UNIT);
		odrComponentAttribute5.setValue(sIServiceDetailDataBean.getPortBwUnit());
		odrComponentAttributeBean.add(odrComponentAttribute5);

		OdrComponentAttributeBean odrComponentAttribute6 = new OdrComponentAttributeBean();
		odrComponentAttribute6.setName(MACDConstants.LL_BW_UNIT);
		odrComponentAttribute6.setValue(sIServiceDetailDataBean.getLastmileBwUnit());
		odrComponentAttributeBean.add(odrComponentAttribute6);

		OdrComponentAttributeBean odrComponentAttribute7 = new OdrComponentAttributeBean();
		odrComponentAttribute7.setName("localItContactMobile");
		odrComponentAttribute7.setValue(terminatingServiceDetail.getLocalItContactNumber());
		odrComponentAttributeBean.add(odrComponentAttribute7);

		OdrComponentAttributeBean odrComponentAttribute8 = new OdrComponentAttributeBean();
		odrComponentAttribute8.setName(MACDConstants.BURSTABLE_BW_UNIT);
		odrComponentAttribute8.setValue(sIServiceDetailDataBean.getBurstableBwUnit());
		odrComponentAttributeBean.add(odrComponentAttribute8);
		

		OdrComponentAttributeBean odrComponentAttribute9 = new OdrComponentAttributeBean();
		odrComponentAttribute9.setName("interface");
		odrComponentAttribute9.setValue(sIServiceDetailDataBean.getSiteEndInterface());
		odrComponentAttributeBean.add(odrComponentAttribute9);

		OdrComponentAttributeBean odrComponentAttribute10 = new OdrComponentAttributeBean();
		odrComponentAttribute10.setName("contractStartDate");
		odrComponentAttribute10.setValue(sIServiceDetailDataBean.getContractStartDate() != null ? DateUtil.convertDateToString(sIServiceDetailDataBean.getContractStartDate()) : null);
		odrComponentAttributeBean.add(odrComponentAttribute10);

		OdrComponentAttributeBean odrComponentAttribute11 =  new OdrComponentAttributeBean();
		odrComponentAttribute11.setName("contractEndDate");
		odrComponentAttribute11.setValue(sIServiceDetailDataBean.getCircuitExpiryDate() != null ? DateUtil.convertDateToString(sIServiceDetailDataBean.getCircuitExpiryDate()) : null);
		odrComponentAttributeBean.add(odrComponentAttribute11);

		OdrComponentAttributeBean odrComponentAttribute12 = new OdrComponentAttributeBean();
		odrComponentAttribute12.setName("orderTermInMonths");
		odrComponentAttribute12.setValue(sIServiceDetailDataBean.getContractTerm().toString());
		odrComponentAttributeBean.add(odrComponentAttribute12);

		if(sIServiceDetailDataBean.getComponents() != null && !sIServiceDetailDataBean.getComponents().isEmpty()) {

		sIServiceDetailDataBean.getComponents().stream().forEach(compo -> {
			compo.getSiComponentAttributes().stream().forEach(siAttr -> {
				if("offnetSupplierBillStartDate".equalsIgnoreCase(siAttr.getAttributeName()) || "endMuxNodeIp".equalsIgnoreCase(siAttr.getAttributeName()) 
						|| "endMuxNodeName".equalsIgnoreCase(siAttr.getAttributeName()) || "endMuxNodePort".equalsIgnoreCase(siAttr.getAttributeName()) 
						|| "mastPoNumber".equalsIgnoreCase(siAttr.getAttributeName()) || "mastPoDate".equalsIgnoreCase(siAttr.getAttributeName())
						|| "lastMileScenario".equalsIgnoreCase(siAttr.getAttributeName()) || "lmConnectionType".equalsIgnoreCase(siAttr.getAttributeName())
						|| "rfMake".equalsIgnoreCase(siAttr.getAttributeName())){
					OdrComponentAttributeBean siComponentAttr = new OdrComponentAttributeBean();
					siComponentAttr.setName(siAttr.getAttributeName());
					siComponentAttr.setValue(siAttr.getAttributeValue());
					odrComponentAttributeBean.add(siComponentAttr);
				}
			});
		});  
		}
		
		if(sIServiceDetailDataBean.getAttributes() != null && !sIServiceDetailDataBean.getAttributes().isEmpty()) {
		
		sIServiceDetailDataBean.getAttributes().stream().forEach(attribute ->{
			if("A_END_BACKHAUL_PROVIDER".equalsIgnoreCase(attribute.getName()) || "B_END_BACKHAUL_PROVIDER".equalsIgnoreCase(attribute.getName())) {
				if(StringUtils.isNotBlank(attribute.getValue())) {
					OdrComponentAttributeBean siComponentAttr = new OdrComponentAttributeBean();
					siComponentAttr.setName(attribute.getName());
					siComponentAttr.setValue(attribute.getValue());
					odrComponentAttributeBean.add(siComponentAttr);
					
					OdrComponentAttributeBean siComponentAttrBackHaul = new OdrComponentAttributeBean();
					siComponentAttrBackHaul.setName("offnetBackHaul");
					siComponentAttrBackHaul.setValue("Yes");
					odrComponentAttributeBean.add(siComponentAttrBackHaul);
					
					OdrComponentAttributeBean siComponentAttrBackHaulProviderName = new OdrComponentAttributeBean();
					siComponentAttrBackHaulProviderName.setName("backHaulProviderName");
					siComponentAttrBackHaulProviderName.setValue(attribute.getValue());
					odrComponentAttributeBean.add(siComponentAttrBackHaulProviderName);
				} else {
					OdrComponentAttributeBean siComponentAttrBackHaul = new OdrComponentAttributeBean();
					siComponentAttrBackHaul.setName("offnetBackHaul");
					siComponentAttrBackHaul.setValue("No");
					odrComponentAttributeBean.add(siComponentAttrBackHaul);
				}
			}
		});
		
		}

		if (terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getErfLocSitebLocationId() != null) {
			Integer erfLocationLocId = terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getErfLocSitebLocationId();
			String locationResponse;
			try {
				locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(erfLocationLocId));
				LOGGER.info("address queue response for destination- {}", locationResponse);

				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						// Set destination related data.
						OdrComponentAttributeBean destinationCity = new OdrComponentAttributeBean();
						destinationCity.setName("destinationCity");
						destinationCity.setValue(addressDetail.getCity());
						odrComponentAttributeBean.add(destinationCity);

						OdrComponentAttributeBean destinationAddressLineOne = new OdrComponentAttributeBean();
						destinationAddressLineOne.setName("destinationAddressLineOne");
						destinationAddressLineOne.setValue(addressDetail.getAddressLineOne());
						odrComponentAttributeBean.add(destinationAddressLineOne);

						OdrComponentAttributeBean destinationAddressLineTwo = new OdrComponentAttributeBean();
						destinationAddressLineTwo.setName("destinationAddressLineTwo");
						destinationAddressLineTwo.setValue(addressDetail.getAddressLineTwo());
						odrComponentAttributeBean.add(destinationAddressLineTwo);

						OdrComponentAttributeBean destinationPincode = new OdrComponentAttributeBean();
						destinationPincode.setName("destinationPincode");
						destinationPincode.setValue(addressDetail.getPincode());
						odrComponentAttributeBean.add(destinationPincode);

						OdrComponentAttributeBean destinationState = new OdrComponentAttributeBean();
						destinationState.setName("destinationState");
						destinationState.setValue(addressDetail.getState());
						odrComponentAttributeBean.add(destinationState);

						OdrComponentAttributeBean destinationCountry = new OdrComponentAttributeBean();
						destinationCountry.setName("destinationCountry");
						destinationCountry.setValue(addressDetail.getCountry());
						odrComponentAttributeBean.add(destinationCountry);

						String siteAddressVal = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
								+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getPincode());
						LOGGER.info("destination Site address. {}", siteAddressVal);

						OdrComponentAttributeBean siteAddress = new OdrComponentAttributeBean();
						siteAddress.setName("siteAddress");
						siteAddress.setValue(siteAddressVal);
						odrComponentAttributeBean.add(siteAddress);
					}
				} } catch (TclCommonException e) {
				LOGGER.info("Exception when trying to fetch location details for site  {}", erfLocationLocId);
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}
		}

		if (terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getErfLocSiteaLocationId() != null) {
			Integer erfLocationLocId = terminatingServiceDetail.getQuoteIllSiteToService().getQuoteIllSite().getErfLocSiteaLocationId();
			String locationResponse;
			try {
				locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(erfLocationLocId));
				LOGGER.info("address queue response for source- {}", locationResponse);

				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						// Set source related data.
						OdrComponentAttributeBean sourceCity = new OdrComponentAttributeBean();
						sourceCity.setName("sourceCity");
						sourceCity.setValue(addressDetail.getCity());
						odrComponentAttributeBean.add(sourceCity);

						OdrComponentAttributeBean sourceAddressLineOne = new OdrComponentAttributeBean();
						sourceAddressLineOne.setName("sourceAddressLineOne");
						sourceAddressLineOne.setValue(addressDetail.getAddressLineOne());
						odrComponentAttributeBean.add(sourceAddressLineOne);

						OdrComponentAttributeBean sourceAddressLineTwo = new OdrComponentAttributeBean();
						sourceAddressLineTwo.setName("sourceAddressLineTwo");
						sourceAddressLineTwo.setValue(addressDetail.getAddressLineTwo());
						odrComponentAttributeBean.add(sourceAddressLineTwo);

						OdrComponentAttributeBean sourcePincode = new OdrComponentAttributeBean();
						sourcePincode.setName("sourcePincode");
						sourcePincode.setValue(addressDetail.getPincode());
						odrComponentAttributeBean.add(sourcePincode);

						OdrComponentAttributeBean sourceState = new OdrComponentAttributeBean();
						sourceState.setName("sourceState");
						sourceState.setValue(addressDetail.getState());
						odrComponentAttributeBean.add(sourceState);

						OdrComponentAttributeBean sourceCountry = new OdrComponentAttributeBean();
						sourceCountry.setName("sourceCountry");
						sourceCountry.setValue(addressDetail.getCountry());
						odrComponentAttributeBean.add(sourceCountry);
					}
				} } catch (TclCommonException e) {
				LOGGER.info("Exception when trying to fetch location details for site  {}", erfLocationLocId);
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}
		}

		return odrComponentAttributeBean;
	}

	/**
	 *
	 * @param request
	 * @param ipAddress
	 * @return
	 * @throws TclCommonException
	 */
	public QuoteDetail terminationApprovedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {
		QuoteDetail detail = null;
		try {

			detail = new QuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				order = constructOrder(quote, detail);
				// Updating stage to ORDER_CONFIRMED as there is no order enrichment for terminations
				order.setStage(OrderStagingConstants.ORDER_COMPLETED.getStage());
				order.getOrderToLes().stream().forEach(orderToLe -> {
					orderToLe.setStage(OrderStagingConstants.ORDER_COMPLETED.getStage());
					orderToLeRepository.save(orderToLe);
				});
				orderRepository.save(order);
				if(checkO2cEnabledForTermination(order)) {
					LOGGER.info("Inside checkO2cEnabledForTermination o2corder {}, enabled {}", order.getOrderToCashOrder(), order.getIsOrderToCashEnabled());
					order.setOrderToCashOrder(CommonConstants.BACTIVE);
					order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
					orderRepository.save(order);
				} else {
					LOGGER.info("Inside checkO2cEnabledForTermination else condition");
					order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
					order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
					orderRepository.save(order);
				}

				detail.setOrderId(order.getId());
				detail.setOrderType(order.getOrderToLes().stream().findFirst().get().getOrderType());
				List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(request.getQuoteId());
				if (quoteToLe.get(0).getIsMultiCircuit().equals(false)) {
					List<String> serviceIds = macdUtils.getServiceIdListBasedOnOrderToLe(order.getOrderToLes().stream().findFirst().get());
					detail.setServiceId(serviceIds.get(0));
				}
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());

				//Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					if(quoteLe.getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteLe.getIsMultiCircuit())) {
					omsSfdcService.processSiteDetails(quoteLe);
					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
					} else {
						
						
						List<QuoteSiteServiceTerminationDetails> terminationServicesList = quoteSiteServiceTerminationDetailsRepository.findByQuoteToLeId(quoteLe.getId());
						if(terminationServicesList != null && !terminationServicesList.isEmpty()){
							for(QuoteSiteServiceTerminationDetails serviceDetails : terminationServicesList) {
								if(serviceDetails.getSalesTaskResponse() == null || (serviceDetails.getSalesTaskResponse() != null && serviceDetails.getSalesTaskResponse().toLowerCase().contains("not retained"))) {
						omsSfdcService.processCreateSiteSolutionTermination(quoteLe, new HashMap<>(), serviceDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
						}
							}
							
						}
						
						//Move parent opty to 95% first
						List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteLe.getTpsSfdcOptyId());
						if(optyList != null && !optyList.isEmpty()) {
						omsSfdcService.processUpdateOptyDummy(quoteLe, optyList.get(0).getServiceRefId(), String.valueOf(quoteLe.getTpsSfdcParentOptyId()), new Date(), quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI);
						}
					}
			/*		Boolean nat = (request.getCheckList() == null
							|| request.getCheckList().equalsIgnoreCase(CommonConstants.NO)) ? Boolean.FALSE
							: Boolean.TRUE;
					Map<String, String> cofObjectMapper = new HashMap<>();
					illQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(), cofObjectMapper);
					User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
					String userEmail = null;
					if (userRepo != null) {
						userEmail = userRepo.getEmailId();
					}
					// TODO- Trigger orderMail ??????????
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail); */

				}
			}
			// updating quote stage
			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					quoteLe.setStage(QuoteStageConstants.TERMINATION_ACCEPTED.getConstantCode());
					quoteToLeRepository.save(quoteLe);

				List<MacdDetail> macdDetailList = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteLe.getId());
				if (Objects.nonNull(macdDetailList) && !macdDetailList.isEmpty()) {
					LOGGER.info("Setting MACD_ORDER_INITIATED for all quote to le {}", quoteLe.getId());
					macdDetailList.stream().forEach(macdDetail -> {
						macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
						macdDetailRepository.save(macdDetail);
					});
				}
			}
			
			// Trigger to O2C
			if (order.getIsOrderToCashEnabled() != null
					&& order.getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
				LOGGER.info("Inside the order to flat table freeze");
				Map<String, Object> requestparam = new HashMap<>();
				requestparam.put("orderId", order.getId());
				Optional<OrderToLe> orderToLeOpt =order.getOrderToLes().stream().findFirst();
				if (orderToLeOpt.isPresent() && (orderToLeOpt.get().getOrderType() == null || orderToLeOpt.get().getOrderType().equals("NEW"))) {
					requestparam.put("productName", "GVPN");
				} else {
					requestparam.put("productName", "GVPN_MACD");
				}
				requestparam.put("userName", Utils.getSource());
				mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
			}

			Integer quoteToLeId = null;
			if(quote != null) {
				Optional<QuoteToLe> quoteToLe = quote.getQuoteToLes().stream().findFirst();
				if(quoteToLe.isPresent())
					quoteToLeId = quoteToLe.get().getId();
				LOGGER.info("quotetoLeId {}", quoteToLeId);
			}
			//Trigger Email notification on Termination Initiation
			terminationService.sendInitiationMailforTermination(request.getQuoteId(),quoteToLeId);			

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	private Boolean checkO2cEnabledForTermination(Order order) {
		LOGGER.info("checkO2cEnabled for order code {}", order.getOrderCode());
		Boolean status = false;
		//Disable O2C auto-trigger for Partner orders
		if(Objects.nonNull(order.getEngagementOptyId())) {
			return status;
		}
		
		

		
		String productName = getProductFamilyNameForQuoteToLe(order.getQuote().getQuoteToLes().stream().findFirst().get());
		if (CommonConstants.NPL.equalsIgnoreCase(productName) || CommonConstants.IAS.equalsIgnoreCase(productName) || CommonConstants.GVPN.equalsIgnoreCase(productName)) {
			// o2c disabled for NDE
			// TODO- check if o2c has to be restricted if only secondary service id is terminated in pri-sec case
			status = true;
		}
		
		
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
				for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
					for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {
						
						//Check if the service is colocated from inventory details
						List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
						for(OrderIllSiteToService qSiteToService : orderIllSiteToServiceList) {
							SIServiceDetailDataBean sIServiceDetailDataBean = null;
							try {
								sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(qSiteToService.getErfServiceInventoryTpsServiceId());
							} catch (TclCommonException e) {
								LOGGER.info("Error when o2cTenPercentCall {}", e);
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
							}

							if(sIServiceDetailDataBean.getUuid()==null){  //PFN-299: o2c not to be triggered for non o2c circuits at 100%
								return false;
							} else {
								return true;
								// PFN5-338
							/*
								Optional<SIAttributeBean> siAttributeBeanOpt = Optional.empty();
								Optional<SIAttributeBean> llArrangeByOpt = Optional.empty();
								Optional<SIAttributeBean> shardLmReqOpt = Optional.empty();
								if (sIServiceDetailDataBean.getAttributes() != null && !sIServiceDetailDataBean.getAttributes().isEmpty()) {
									siAttributeBeanOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.PRODUCT_FLAVOUR.equalsIgnoreCase(attribute.getName())).findAny();
									llArrangeByOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.LL_ARRANGE_BY.equalsIgnoreCase(attribute.getName())).findAny();
									shardLmReqOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.SHARED_LM_REQUIRED.equalsIgnoreCase(attribute.getName())).findAny();
								}
								if (llArrangeByOpt.isPresent())
									LOGGER.info("llArrangeByOpt {}", llArrangeByOpt.get().getValue());

								if (shardLmReqOpt.isPresent())
									LOGGER.info("shardLmReqOpt {}", shardLmReqOpt.get().getValue());
								if (sIServiceDetailDataBean.getLmType() != null && MACDConstants.COLOCATED.equalsIgnoreCase(sIServiceDetailDataBean.getLmType())) {
									return false;
								} else if (siAttributeBeanOpt.isPresent() && !(CommonConstants.GVPN.equalsIgnoreCase(siAttributeBeanOpt.get().getValue()))) {
									return false;
								} else if ((llArrangeByOpt.isPresent() && MACDConstants.CUSTOMER.equalsIgnoreCase(llArrangeByOpt.get().getValue()))
										|| (shardLmReqOpt.isPresent() && CommonConstants.YES.equalsIgnoreCase(shardLmReqOpt.get().getValue()))) {
									return false;
								}

							 */
							}
						}
					}
				}
			}
		}
		

		
		return status;
	}
	
	
	public String downloadTrfFromStorageContainer(Integer quoteId, Integer quoteLeId, Integer orderId,
			Integer orderLeId, Map<String, String> cofObjectMapper) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		Order order = null;
		try {
			LOGGER.info(
					"Inside Download TRF From Object Storage Container with input with quoteId {} ,quoteLe {} , cofObjMapper {}",
					quoteId, quoteLeId, cofObjectMapper);
			OmsAttachment omsAttachment = null;
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if ((Objects.isNull(quoteId) && Objects.isNull(quoteLeId))
						&& (Objects.isNull(orderId) && Objects.isNull(orderLeId)))
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

				if (!Objects.isNull(quoteLeId)) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);

					if (quoteToLe.isPresent()) {
						order = orderRepository.findByQuoteAndStatus(quoteToLe.get().getQuote(),
								quoteToLe.get().getQuote().getStatus());
						if (order != null) {
							LOGGER.info("Getting oms Attachment Using order {}", order.getId());
							omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
							if (omsAttachment == null) {
								omsAttachment = getOmsAttachmentByQuote(quoteId, omsAttachment);
							}

						} else {
							LOGGER.info("Getting oms Attachment Using quote {}", quoteId);
							omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
							LOGGER.info("Output omsAttachment {}", omsAttachment.getId());
						}
					}
				} else if (!Objects.isNull(orderId) && !Objects.isNull(orderLeId)) {
					LOGGER.info("Getting oms Attachment Using order {}", orderLeId);
					Optional<Order> orderOpt = orderRepository.findById(orderId);
					if (orderOpt.isPresent()) {
						order = orderOpt.get();
						omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
						if (omsAttachment == null) {
							quoteId = order.getQuote().getId();
							omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
							Optional<OrderToLe> orderTole = orderToLeRepository.findById(orderLeId);
							if (orderTole.isPresent()) {
								omsAttachment.setOrderToLe(orderTole.get());
								omsAttachment.setReferenceId(orderId);
								omsAttachment.setReferenceName(CommonConstants.ORDERS);
								omsAttachmentRepository.save(omsAttachment);
							}
						}
					}

				}
				if (omsAttachment != null) {
					String response = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
							String.valueOf(omsAttachment.getErfCusAttachmentId())));
					if (StringUtils.isNotBlank(response)) {
						LOGGER.info("Output Received while getting the attachment table {}", response);
						AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response,
								AttachmentBean.class);
						if (cofObjectMapper != null) {
							cofObjectMapper.put("FILENAME", attachmentBean.getFileName());
							cofObjectMapper.put("OBJECT_STORAGE_PATH", attachmentBean.getPath());
							String tempUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(), 60000,
									attachmentBean.getPath(), false);
							cofObjectMapper.put("TEMP_URL", tempUrl);
							LOGGER.info("CofObject Mapper {}", cofObjectMapper);
						} else {
							tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
									Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
						}
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;

	}
	
	private OmsAttachment getOmsAttachmentBasedOnOrder(Order order, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, order.getId(),
						AttachmentTypeConstants.TRF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		return omsAttachment;
	}
	
	private OmsAttachment getOmsAttachmentBasedOnQuote(Integer quoteId, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteId,
						AttachmentTypeConstants.TRF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		LOGGER.info("Oms Attachment is -----> for quote ----> {} ",
				Optional.of(omsAttachment), quoteRepository.findById(quoteId).get().getQuoteCode());
		return omsAttachment;
	}
	
	private OmsAttachment getOmsAttachmentByQuote(Integer quoteId, OmsAttachment omsAttachment) {
		LOGGER.info("Trying with oms Attachment Using quote {}", quoteId);
		omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
		LOGGER.info("Output omsAttachment {}", omsAttachment.getId());
		return omsAttachment;
	}

	public String gvpnPersistTerminationNegotiationResponse(TerminationNegotiationResponse terminationNegotiationResponse) {
		String response="";
		try {
			LOGGER.info("terminaitonNegotiationResponse {}", terminationNegotiationResponse.toString());
			Quote quote = quoteRepository.findByQuoteCode(terminationNegotiationResponse.getOrderCode()); 
			Optional<QuoteToLe> quoteToLeOpt = quote.getQuoteToLes().stream().findFirst();
			if(quoteToLeOpt.isPresent()) {
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(terminationNegotiationResponse.getServiceCode(), quoteToLeOpt.get());
				if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					QuoteSiteServiceTerminationDetails terminationDetails = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToServiceList.get(0));
		    terminationDetails.setSalesTaskResponse(Utils.convertObjectToJson(terminationNegotiationResponse));
		    
		    if(terminationNegotiationResponse.getTerminationSubType() != null)
		    	terminationDetails.setTerminationSubtype(terminationNegotiationResponse.getTerminationSubType());
		    if(terminationNegotiationResponse.getReason() != null)
		    	terminationDetails.setRetentionReason(terminationNegotiationResponse.getReason());
		    if(terminationNegotiationResponse.getTerminationReason() != null)
		    	terminationDetails.setReasonForTermination(terminationNegotiationResponse.getTerminationReason());
		    if(terminationNegotiationResponse.getTerminationSubReason() != null)
		    	terminationDetails.setSubReason(terminationNegotiationResponse.getTerminationSubReason());
		    if(terminationNegotiationResponse.getTerminationRemarks() != null)
		    	terminationDetails.setTerminationRemarks(terminationNegotiationResponse.getTerminationRemarks());
		    if(terminationNegotiationResponse.getRegrettedNonRegrettedTermination() != null)
		    	terminationDetails.setRegrettedNonRegrettedTermination(terminationNegotiationResponse.getRegrettedNonRegrettedTermination());

		    terminationDetails = quoteSiteServiceTerminationDetailsRepository.save(terminationDetails);
		    if(quoteToLeOpt.get().getStage() != null && (MACDConstants.TERMINATION_REQUEST_RECEIVED.equalsIgnoreCase(quoteToLeOpt.get().getStage()) 
		    		|| MACDConstants.TERMINATION_CONFIRMED.equalsIgnoreCase(quoteToLeOpt.get().getStage()))) {
		    if(MACDConstants.RETAINED.equalsIgnoreCase(terminationNegotiationResponse.getNegotiationResponse())) {
		    	LOGGER.info("RETAINED, dropping opportunity" );
		    	if(quoteToLeOpt.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLeOpt.get().getIsMultiCircuit() )) {
				omsSfdcService.processUpdateOpportunity(null, quoteToLeOpt.get().getTpsSfdcOptyId(),
						SFDCConstants.CLOSED_DROPPED, quoteToLeOpt.get());
				deactivateQuoteIfCustomerRetained(terminationNegotiationResponse, quote);
		    	}  else {
		    		//TODO: Need to check if any child opportunity can be closed/dropped
		    		omsSfdcService.processUpdateOpportunityTermination(new Date(), SFDCConstants.CLOSED_DROPPED, quoteToLeOpt.get(),
		    				terminationDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());

					deactivateQuoteIllSiteToService(quoteToLeOpt.get(),terminationDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
					deactivateSites(quoteToLeOpt.get(),terminationDetails.getQuoteIllSiteToService().getQuoteIllSite().getId());

					//Delete quote if All sites are deactivated
					List<QuoteIllSite> activeSites  = illSiteRepository.findIllSites(quoteToLeOpt.get().getQuote().getQuoteCode()).stream()
							.filter(site -> (CommonConstants.BACTIVE.equals(site.getStatus()))).collect(Collectors.toList());
					LOGGER.info("No of Active Sites in the quote  : {}", activeSites.size());
					if(activeSites.size()==0) {
						LOGGER.info("Dropping Quote since all the sites have been deactivated");
						omsSfdcService.processUpdateOpportunityCloseDropped(quoteToLeOpt.get().getTpsSfdcOptyId(),SFDCConstants.CLOSED_DROPPED,
								quoteToLeOpt.get(),SFDCConstants.CLOSED_DROPPED,SFDCConstants.WIN_LOSS_DROP_KEY_REASON,SFDCConstants.DROP_REASONS);
						deactivateQuoteIfCustomerRetained(terminationNegotiationResponse, quote);
					}
				}
				
		    } else {
		    	LOGGER.info("Not RETAINED, Updating SFDC");
				if (quoteToLeOpt.isPresent() && (quoteToLeOpt.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLeOpt.get().getIsMultiCircuit()))) {
					omsSfdcService.processUpdateOpportunity(new Date(), quoteToLeOpt.get().getTpsSfdcOptyId(),SFDCConstants.IDENTIFIED_OPTY_STAGE, quoteToLeOpt.get());
				} else {
					LOGGER.info("Updating SFDC for service id {}",terminationDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
					omsSfdcService.processUpdateOpportunityTermination(new Date(), SFDCConstants.IDENTIFIED_OPTY_STAGE, quoteToLeOpt.get(),
							terminationDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
				}
			}
		    }
			response = "Success";
			}
		}
		}catch(Exception e) {
			response = "Failure";
			LOGGER.info("Exception when trying to persists the termination negotiation response");
		}
		return response;
	}

	private void deactivateQuoteIllSiteToService(QuoteToLe quoteToLe, String serviceId) {
		LOGGER.info("ServiceId to be deactivated {}",serviceId);
		List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
		if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
			for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServiceList) {
				quoteIllSiteToService.setIsDeleted(CommonConstants.ACTIVE);
				quoteIllSiteToServiceRepository.save(quoteIllSiteToService);
			}
		}
	}

	private void deactivateSites(QuoteToLe quoteToLe, Integer siteId) throws TclCommonException{
		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(siteId);
		if (quoteIllSite.get() == null) {
			throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		} else {
			Map<String,String> activeServiceIds=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite.get(),quoteToLe);
			LOGGER.info("No of ServiceIds linked to site and are not deleted : {}",activeServiceIds.size());
			if(activeServiceIds.size()==0)
			{ // Skipping for Dual Cases when the other service id is linked to the Site
				quoteIllSite.get().setStatus((byte) 0);
				illSiteRepository.save(quoteIllSite.get());
			}
		}

	}

	public String getO2CCallStatus(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		String response = "true";
		if(quoteId == null || quoteLeId == null) 
			throw new TclCommonException(ExceptionConstants.TERMINATION_REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
		
		if(quoteToLe.isPresent()) {
			if(quoteToLe.get().getStage() != null && MACDConstants.TERMINATION_CONFIRMED.equalsIgnoreCase(quoteToLe.get().getStage())) {
				response = "true";
			} else {
			LocalDate termWaitPeriodDate = LocalDate.now().minusDays(MACDConstants.QUOTE_WAIT_PERIOD_IN_DAYS);
			Date reqDate = Date.from(termWaitPeriodDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			LOGGER.info("Requested Date {}", reqDate);
			List<QuoteSiteServiceTerminationDetails> quoteSiteServiceTerminationDetails = quoteSiteServiceTerminationDetailsRepository.findByQuoteToLeIdAndIsDeleted(quoteLeId);
			for(QuoteSiteServiceTerminationDetails terminationDetails : quoteSiteServiceTerminationDetails) {
				//
			LOGGER.info("Service id  : {}",terminationDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
			 if( (terminationDetails.getSalesTaskResponse() == null && (terminationDetails.getO2cCallInitiatedDate() != null && terminationDetails.getO2cCallInitiatedDate().before(reqDate))) || MACDConstants.SEND_TO_TD.equalsIgnoreCase(terminationDetails.getHandoverTo()) ) {
					LOGGER.info("o2c call inititated date {}, req date {}, handoverTo {} ", terminationDetails.getO2cCallInitiatedDate(), reqDate, terminationDetails.getHandoverTo());
					response = "true";
					
					List<ThirdPartyServiceJob> thirdPartyServiceJoblist = thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.get().getQuote().getQuoteCode(), SFDCConstants.UPDATE_OPTY, SFDCConstants.SFDC);
					// Make 80%  o2c call
					if(thirdPartyServiceJoblist != null && !thirdPartyServiceJoblist.isEmpty()) {
						thirdPartyServiceJoblist.stream().forEach(thirdpartyServiceJob -> {
							if(thirdpartyServiceJob.getRequestPayload().toLowerCase().contains("verbal agreement")) {
								LOGGER.info("Verbal Agreement stage already present");
							} else {
								LOGGER.info("Verbal agreemnt call not there but 7 days already passed");
								try {
									omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.get().getTpsSfdcOptyId(),
											SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe.get());
								} catch (TclCommonException e) {
									LOGGER.info("Exception when trying to place verbal agreement call to SFDC {}", e);
									
								}
							}
						});
					}
				
				} else	if (terminationDetails.getSalesTaskResponse() == null  && !MACDConstants.SEND_TO_TD.equalsIgnoreCase(terminationDetails.getHandoverTo()) ) {
					LOGGER.info("sales task response  null {}", terminationDetails.getSalesTaskResponse());
					response = "false";
					break;
				}
				else if(terminationDetails.getSalesTaskResponse() != null){
					LOGGER.info(" received sales task response, not null {}", terminationDetails.getSalesTaskResponse());
					response = "true";
				} 
				
			}
			}
					
			
			LOGGER.info("response evaluated {}", response);
		 
		}
		return response;
		
		
	}
	
	private void deactivateQuoteIfCustomerRetained(TerminationNegotiationResponse terminationNegotiationResponse,
			Quote quote) throws TclCommonException {
		processQuoteAudit(terminationNegotiationResponse.getNegotiationResponse(), quote.getQuoteCode(), "DEACTIVATE");
		quote.setStatus(CommonConstants.BDEACTIVATE);
		quoteRepository.save(quote);
		Map<String,String> archRequest=new HashMap<>();
		archRequest.put("quoteCode", quote.getQuoteCode());
		archRequest.put("action", "ACTIVATE");
		mqUtils.send(taskArchivalRequest, Utils.convertObjectToJson(archRequest));
	}
	
	private void processQuoteAudit(String comments, String quoteCode,String action) {
		QuoteAudit quoteAudit=new QuoteAudit();
		quoteAudit.setAction(action);
		quoteAudit.setComments(comments);
		quoteAudit.setCreatedBy(Utils.getSource());
		quoteAudit.setCreatedTime(new Date());
		quoteAudit.setQuoteCode(quoteCode);
		quoteAuditRepository.save(quoteAudit);
	}
	
	private Boolean checkO2cEnabled(Quote quote) {
		LOGGER.info("checkO2cEnabled for order code {}", quote.getQuoteCode());
		Boolean status = true;
		//Disable O2C auto-trigger for Partner orders
		if(Objects.nonNull(quote.getEngagementOptyId())) {
			return false;
		}
		for (QuoteToLe quoteToLe : quote.getQuoteToLes()) {
			
			if(quoteToLe.getQuoteCategory() != null && MACDConstants.ADD_SECONDARY.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				return false;
			}

			//Disable O2C auto-trigger for Partner orders
			if(StringUtils.isNotBlank(quoteToLe.getClassification()) && SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification())) {
				return false;
			}
			else if (StringUtils.isNotBlank(quoteToLe.getClassification()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification()))
			{
				return false;
			} else {

				for (QuoteToLeProductFamily leProductFamily : quoteToLe.getQuoteToLeProductFamilies()) {
					for (ProductSolution oProductSolution : leProductFamily.getProductSolutions()) {
						for (QuoteIllSite quoteIllSite : oProductSolution.getQuoteIllSites()) {
							
							//Check if the service is colocated from inventory details
							List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite(quoteIllSite);
							for(QuoteIllSiteToService qSiteToService : quoteIllSiteToServiceList) {
								SIServiceDetailDataBean sIServiceDetailDataBean = null;
								try {
									sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(qSiteToService.getErfServiceInventoryTpsServiceId());
								} catch (TclCommonException e) {
									LOGGER.info("Error when o2cTenPercentCall {}", e);
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
								}
								Optional<SIAttributeBean> llArrangeByOpt = Optional.empty();
								Optional<SIAttributeBean> shardLmReqOpt = Optional.empty();
								Optional<SIAttributeBean> siAttributeBeanOpt = Optional.empty();
								if(sIServiceDetailDataBean.getAttributes() != null && !sIServiceDetailDataBean.getAttributes().isEmpty()) {
									siAttributeBeanOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.PRODUCT_FLAVOUR.equalsIgnoreCase(attribute.getName())).findAny();
									llArrangeByOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.LL_ARRANGE_BY.equalsIgnoreCase(attribute.getName())).findAny();
									shardLmReqOpt = sIServiceDetailDataBean.getAttributes().stream().filter(attribute -> MACDConstants.SHARED_LM_REQUIRED.equalsIgnoreCase(attribute.getName())).findAny();
								}
								if(llArrangeByOpt.isPresent())
									LOGGER.info("llArrangeByOpt {}", llArrangeByOpt.get().getValue());
								
								if(shardLmReqOpt.isPresent())
									LOGGER.info("shardLmReqOpt {}", shardLmReqOpt.get().getValue());
								if(sIServiceDetailDataBean.getLmType() != null && MACDConstants.COLOCATED.equalsIgnoreCase(sIServiceDetailDataBean.getLmType())) {
									return false;
								}  else if(siAttributeBeanOpt.isPresent() && !(CommonConstants.GVPN.equalsIgnoreCase(siAttributeBeanOpt.get().getValue()))) {
									return false;
								}else if((llArrangeByOpt.isPresent() && MACDConstants.CUSTOMER.equalsIgnoreCase(llArrangeByOpt.get().getValue())) 
										|| (shardLmReqOpt.isPresent() && CommonConstants.YES.equalsIgnoreCase(shardLmReqOpt.get().getValue()))) {
									return false;
								}
							}
							
						}
					}
				}
			
			}
		}
		return status;
	}
}

