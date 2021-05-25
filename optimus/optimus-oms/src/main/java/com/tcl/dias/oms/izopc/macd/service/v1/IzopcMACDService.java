package com.tcl.dias.oms.izopc.macd.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_METHOD;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PAYMENT_TERM_VALUE;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.PARTNER;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.OrderSummaryBeanResponse;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.CompareQuotePrices;
import com.tcl.dias.oms.beans.CompareQuotes;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ComponentQuotePrices;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.TotalSolutionQuote;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteAccessPermission;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteAccessPermissionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteDetail;
import com.tcl.dias.oms.izopc.service.v1.IzoPcOrderService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.macd.beans.BandwidthResponse;
import com.tcl.dias.oms.macd.beans.IzopcMACDRequest;
import com.tcl.dias.oms.macd.beans.MACDAttributesBean;
import com.tcl.dias.oms.macd.beans.MACDAttributesComparisonBean;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequest;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequestResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponseBean;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.MulticircuitBandwidthResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the IzopcMACDService.java class. This class contains
 * IZOPCMACD related functionalities
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Service
@Transactional
public class IzopcMACDService extends IzoPcQuoteService{

	public static final Logger LOGGER = LoggerFactory.getLogger(IzopcMACDService.class);

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	IzoPcOrderService izoPcOrderService;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;


	@Value("${rabbitmq.si.ias.queue}")
	String siIASQueue;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;
	
	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	

	@Value("${rabbitmq.si.service.details.list.queue}")
	String siServiceDetailsListQueue;

	
	@Value("${rabbitmq.si.order.add.site.queue}")
	String siOrderDetailsAddSiteQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	PartnerService partnerService;
	
	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	



	/**
	 * @author ANNE NISHA editSiteComponent used to edit site component values
	 * @param request
	 * @return
	 */

	public QuoteDetail editSiteComponentDetails(Integer quoteId, Integer quoteLeId, Integer siteId,
			UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			QuoteToLe quoteToLe = null;
			quoteDetail = new QuoteDetail();
			validateRequest(request);
			// String[] additionalIPFlag = { null };
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLeOpt.isPresent()) {
				quoteToLe = quoteToLeOpt.get();

				request.getComponentDetails().stream().forEach(cmpDetail -> {

					updateAttributeValues(request, cmpDetail);

					// if (cmpDetail.getName() != null
					// && cmpDetail.getName().equalsIgnoreCase(FPConstants.ADD_ON.toString())) {
					// cmpDetail.getAttributes().forEach(attributeDetail -> {
					// if
					// (attributeDetail.getName().equalsIgnoreCase(FPConstants.ADDITIONAL_IP.toString()))
					// {
					// additionalIPFlag[0] = attributeDetail.getValue();
					// }
					//
					// });
					//
					// }
				});

				// if (additionalIPFlag.length > 0 &&
				// additionalIPFlag[0].equalsIgnoreCase(CommonConstants.NO)) {
				// List<QuoteProductComponent> prodComponent = quoteProductComponentRepository
				// .findByReferenceIdAndMstProductComponent_NameAndType(siteId,
				// FPConstants.ADDITIONAL_IP.toString(), MACDConstants.REFERENCE_TYPE_PRIMARY);
				// if (prodComponent != null && !prodComponent.isEmpty())
				// removeAdditionalIPComponent(prodComponent);
				//
				//
				// }
			}

			Optional<QuoteIllSite> quoteIllSiteOpt = illSiteRepository.findById(siteId);

			if (quoteIllSiteOpt.isPresent()) {
				if (request.getIsTaxExempted() != null) {
					quoteIllSiteOpt.get().setIsTaxExempted(request.getIsTaxExempted());

					if (StringUtils.isNotBlank(request.getPrimaryOrSecondaryOrBoth()))
						quoteIllSiteOpt.get().setMacdChangeBandwidthFlag(request.getPrimaryOrSecondaryOrBoth());
					illSiteRepository.save(quoteIllSiteOpt.get());
				}
				
				if(request.getBandwidthEditted() != null && request.getServiceId() != null ) {
					List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSite(quoteIllSiteOpt.get());
					quoteIllSiteToServiceList.stream().filter(quoteIllToService -> request.getServiceId().equals(quoteIllToService.getErfServiceInventoryTpsServiceId())).forEach(quoteIllSiteToService -> {
						quoteIllSiteToService.setBandwidthChanged((request.getBandwidthEditted() != null && request.getBandwidthEditted().equalsIgnoreCase("true")) ? 
										CommonConstants.BACTIVE : CommonConstants.BDEACTIVATE);
						quoteIllSiteToServiceRepository.save(quoteIllSiteToService);
					});
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	private void removeAdditionalIPComponent(List<QuoteProductComponent> prodComponent) {

		prodComponent.forEach(quoteProductComponent -> {

			quoteProductComponentsAttributeValueRepository.deleteAll(
					quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(quoteProductComponent));
		});

		quoteProductComponentRepository.deleteAll(prodComponent);

	}

	/**
	 * @author Thamizhselvi Perumal Method for handling macd request to create quote
	 *         with macd type
	 * 
	 * @param macdRequest
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */

	public MacdQuoteResponse handleMacdRequestToCreateQuote(IzopcMACDRequest macdRequest,Boolean nsval) throws TclCommonException {
		try {
			LOGGER.info("Entering handleMacdRequestToCreateQuote with nsVal {} and macd Request {}",nsval,macdRequest);
			validateMacdQuoteRequest(macdRequest);
			LOGGER.info("Starting to create Macd Quote");
			return createMacdQuote(macdRequest,nsval);
		} catch (Exception e) {
			LOGGER.error("Error in handling macd request",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * @author Thamizhselvi Perumal Method to validate macd quote request
	 * 
	 * @param macdRequest
	 * @throws TclCommonException
	 */

	private void validateMacdQuoteRequest(IzopcMACDRequest macdRequest) throws TclCommonException {
		LOGGER.info("Starting to validate the macd quote request");
		String[] quoteTypes = { MACDConstants.ADD_SITE_SERVICE, MACDConstants.SHIFT_SITE_SERVICE,
				MACDConstants.CHANGE_BANDWIDTH_SERVICE, MACDConstants.ADD_IP_SERVICE,
				MACDConstants.REQUEST_TERMINATION_SERVICE,MACDConstants.OTHERS, MACDConstants.ADD_SECONDARY , MACDConstants.DEMO_EXTENSION};
		List<String> quoteTypeList = Arrays.asList(quoteTypes);
		if (Objects.nonNull(macdRequest)) {
			if (Objects.nonNull(macdRequest.getRequestType()) && !quoteTypeList.contains(macdRequest.getRequestType())
					|| StringUtils.isBlank(macdRequest.getRequestType())) {
				String errorMessage = String.format("Business validation failed:Invalid MACD request type %s ",
						macdRequest.getRequestType());
				LOGGER.warn(errorMessage);
				throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			}

			/*
			 * if (StringUtils.isBlank(macdRequest.getDownstreamOrderId()) ||
			 * Objects.isNull(macdRequest.getServiceDetailId()) ||
			 * StringUtils.isBlank(macdRequest.getServiceId())) { throw new
			 * TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
			 * ResponseResource.R_CODE_BAD_REQUEST); }
			 */
			
			if (macdRequest.getServiceDetails() == null || macdRequest.getServiceDetails().isEmpty()) {
				throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			}
			validateQuoteDetail(macdRequest.getQuoteRequest());// validating the input for create Quote
		} else {
			throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		}

		/*
		 * if(!macdRequest.getRequestType().equalsIgnoreCase(MACDConstants.
		 * ADD_SITE_SERVICE)) { MacdDetail macdDetail = null; MacdDetail secMacdDetail =
		 * null; macdDetail =
		 * macdDetailRepository.findByTpsServiceId(macdRequest.getServiceId());
		 * SIOrderDataBean siOrderDataBean =
		 * macdUtils.getSiOrderData(macdRequest.getDownstreamOrderId());
		 * SIServiceDetailDataBean serviceDetailBean =
		 * macdUtils.getSiServiceDetailBeanBasedOnServiceId(siOrderDataBean,
		 * macdRequest.getServiceId()); if
		 * (Objects.nonNull(serviceDetailBean.getLinkId())) { secMacdDetail =
		 * macdDetailRepository.findByTpsServiceId(serviceDetailBean.getTpsServiceId());
		 * } if ((Objects.nonNull(macdDetail)&&!macdDetail.getStage().equalsIgnoreCase(
		 * MACDConstants.MACD_ORDER_IN_PROGRESS)&&!macdDetail.getStage().
		 * equalsIgnoreCase(MACDConstants.MACD_ORDER_COMMISSIONED)) ||
		 * (Objects.nonNull(secMacdDetail)&&!secMacdDetail.getStage().equalsIgnoreCase(
		 * MACDConstants.MACD_ORDER_IN_PROGRESS)&&!secMacdDetail.getStage().
		 * equalsIgnoreCase(MACDConstants.MACD_ORDER_COMMISSIONED))) { String
		 * errorMessage = String.format(
		 * "Business validation failed: Macd order is not allowed because it was already placed on SI order %s with serviceId %s"
		 * , macdRequest.getDownstreamOrderId(), macdRequest.getServiceId());
		 * LOGGER.warn(errorMessage);
		 *//* throw new RuntimeException(errorMessage); *//*
															 * throw new TclCommonException(ExceptionConstants.
															 * MACD_REQUEST_VALIDATION_ERROR,
															 * ResponseResource.R_CODE_BAD_REQUEST); } }
															 */
	}

	/**
	 * @author Thamizhselvi Perumal createQuote - This method is used to create a
	 *         quote The input validation is done and the corresponding tables are
	 *         populated with initial set of values
	 * 
	 * @param quoteDetail
	 * @param erfCustomerId
	 * @return QuoteDetail
	 * @throws TclCommonException
	 * @throws ParseException
	 */

	public MacdQuoteResponse createMacdQuote(IzopcMACDRequest request, Boolean nsVal)
			throws TclCommonException, ParseException {
		MacdQuoteResponse macdResponse = new MacdQuoteResponse();
		QuoteResponse response = null;
		Integer erfCustomerIdInt = null;
		Integer erfCustomerLeIdInt = null;
		String partnerCuid=null;
		String endCustomerName=null;
		User user = getUserId(Utils.getSource());
		LOGGER.info("Fetching user details based of username {}", Utils.getSource());
		List<SIServiceDetailsBean> serviceDetailsList = new ArrayList<>();
		if (Objects.nonNull(user)) {
			LOGGER.info("Fetching the serviceIds from the macQuoteRequest");
			String serviceIds = request.getServiceDetails().stream().map(i -> i.getServiceId().toString().trim())
					.collect(Collectors.joining(","));
			LOGGER.info("Service Ids with comma seperated {}", serviceIds);
			if (request.getRequestType() != null
					&& !MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(request.getRequestType())) {
				LOGGER.info("Entering condtion which is not a ADD SITE SERVICE and it is {}",request.getRequestType());
				String siOrderDetailsResponse = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceIds);
				LOGGER.info("Response from siOrderDetails {}",siOrderDetailsResponse);
				SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
						.convertJsonToObject(siOrderDetailsResponse, SIServiceDetailsBean[].class);
				if (serviceDetailBeanArray != null) {
					for (SIServiceDetailsBean siServiceDetailsBean : serviceDetailBeanArray) {
						serviceDetailsList.add(siServiceDetailsBean);
						if (StringUtils.isNotBlank(siServiceDetailsBean.getPriSecServLink())
								&& siServiceDetailsBean.getLinkType().equalsIgnoreCase("PRIMARY")) {

							String secOrderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsQueue,
									siServiceDetailsBean.getPriSecServLink());
							LOGGER.info("Response from siOrderDetails for secondary is {}",
									secOrderDetailsQueueResponse);
							SIServiceDetailsBean[] secServiceDetailBeanArray = (SIServiceDetailsBean[]) Utils
									.convertJsonToObject(secOrderDetailsQueueResponse, SIServiceDetailsBean[].class);
							if (secServiceDetailBeanArray != null) {
								for (SIServiceDetailsBean siServiceDetailsBean2 : secServiceDetailBeanArray) {
									serviceDetailsList.add(siServiceDetailsBean2);
								}
							}
						}

					}
				}
			} else {
				LOGGER.info("Entering condtion which is {}",request.getRequestType());
				String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsAddSiteQueue,
						serviceIds);
				LOGGER.info("Response from siOrderDetails for addSite is {}",orderDetailsQueueResponse);
				SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
						.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
				serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
			}
			response = new QuoteResponse();
			if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
				if (serviceDetailsList.stream().findFirst().isPresent()) {
					erfCustomerIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerId();
					erfCustomerLeIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerLeId();
					partnerCuid=serviceDetailsList.stream().findFirst().get().getPartnerCuid();
					endCustomerName=serviceDetailsList.stream().findFirst().get().getErfCustomerName();

				}
			}
			LOGGER.info("erfCustomer Id {} and erfCustomerLeId {}",erfCustomerIdInt,erfCustomerLeIdInt);
			if (Objects.isNull(nsVal)) {
				nsVal = false;
			}
			QuoteToLe quoteTole = processQuote(request.getQuoteRequest(), erfCustomerIdInt, user);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				createMACDSpecificQuoteToLe(quoteTole, serviceDetailsList, erfCustomerLeIdInt,
						request.getRequestType());
				if (Objects.nonNull(request.getQuoteRequest().getIsMulticircuit())
						&& CommonConstants.BACTIVE.equals(request.getQuoteRequest().getIsMulticircuit()))
					quoteTole.setIsMultiCircuit(request.getQuoteRequest().getIsMulticircuit());
				quoteToLeRepository.save(quoteTole);
				LOGGER.info("Term in months {}", quoteTole.getTermInMonths());
				if (serviceDetailsList != null && !serviceDetailsList.isEmpty() && request.getQuoteRequest().getQuoteId()==null) {
					serviceDetailsList.stream().forEach(serviceDetail -> {
						LOGGER.info("Service detail info id {} ", serviceDetail.getId());
						LOGGER.info("QuoteIllSiteToService creating with  properties {} ,{} , {}, {} {}",
								serviceDetail.getReferenceOrderId(), serviceDetail.getTpsServiceId(), quoteTole.getId(),
								serviceDetail.getParentOpportunityId(), serviceDetail.getLinkType());
						QuoteIllSiteToService siteToService = new QuoteIllSiteToService();
						siteToService.setAllowAmendment("NA");
						siteToService.setErfServiceInventoryParentOrderId(serviceDetail.getReferenceOrderId());
						siteToService.setErfServiceInventoryServiceDetailId(serviceDetail.getId());
						siteToService.setErfServiceInventoryTpsServiceId(serviceDetail.getTpsServiceId());
						siteToService.setQuoteToLe(quoteTole);
						siteToService.setQuoteIllSite(new QuoteIllSite(request.getQuoteRequest().getSolutions().get(0).getSiteDetail().get(0).getSiteId()));//TODO ;
						siteToService.setTpsSfdcParentOptyId(serviceDetail.getParentOpportunityId());
						if (serviceDetail.getLinkType() != null
								&& (PDFConstants.PRIMARY.equalsIgnoreCase(serviceDetail.getLinkType())
										|| MACDConstants.SINGLE.equalsIgnoreCase(serviceDetail.getLinkType())))
							siteToService.setType(PDFConstants.PRIMARY);
						else
							siteToService.setType(PDFConstants.SECONDARY);
						quoteIllSiteToServiceRepository.save(siteToService);
					});

				}
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteTole.getId());
				if (Objects.isNull(macdDetail)) {
					LOGGER.info("Create macd order detail for quoteToLe {},", quoteTole.getId());
					createMacdOrderDetail(quoteTole, request.getCancellationDate(), request.getCancellationReason());
				} else {
					macdDetail.setCreatedTime(new Date());
					macdDetailRepository.save(macdDetail);
				}
				macdResponse.setQuoteCategory(quoteTole.getQuoteCategory());
				macdResponse.setQuoteType(quoteTole.getQuoteType());
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());
				if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
					omsSfdcService.processCreatePartnerOpty(quoteTole, request.getQuoteRequest().getProductName(),partnerCuid,endCustomerName);
				} else if (request.getQuoteRequest().getQuoteId() == null
						&& Objects.isNull(request.getQuoteRequest().getEngagementOptyId())) {
					// Triggering Sfdc Creation
					omsSfdcService.processCreateOpty(quoteTole, request.getQuoteRequest().getProductName());
				}
				

				if (request.getRequestType().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
					customerLeAttributeRequestBean.setCustomerLeId(erfCustomerLeIdInt);
					customerLeAttributeRequestBean.setProductName("IAS");
					LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
							Utils.convertObjectToJson(customerLeAttributeRequestBean));

					if (StringUtils.isNotEmpty(customerLeAttributes)) {
						updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
								CustomerLeDetailsBean.class), quoteTole);
					}
				}
				//processQuoteAccessPermissions(user, quoteTole);

			}
		}
		macdResponse.setQuoteResponse(response);
		return macdResponse;
	}
	
	

	/**
	 * @author Thamizhselvi Perumal Method to create MACD specific quoteToLe
	 * @param quoteTole
	 * @param orderData
	 * @param quoteType
	 * @return
	 */
	private QuoteToLe createMACDSpecificQuoteToLe(QuoteToLe quoteTole,
			List<SIServiceDetailsBean> serviceDetailsBeanList, Integer erfCustomerLeIdInt, String quoteCategory) {
		Integer erfSpLeId = null;
		Integer erfCustCurrencyId = null;
		Integer maxContractTerm = null;
		Integer parentSfdcOptyId = null;
		SIServiceDetailsBean firstSiServiceDetailsBean = null ;
		if (serviceDetailsBeanList != null && !serviceDetailsBeanList.isEmpty()) {
			if (serviceDetailsBeanList.stream().findFirst().isPresent()) {
				firstSiServiceDetailsBean=serviceDetailsBeanList.stream().findFirst().get();
				LOGGER.info("service details bean is ---> {} " , firstSiServiceDetailsBean);

				erfSpLeId = firstSiServiceDetailsBean.getErfSpLeId();
				erfCustCurrencyId = firstSiServiceDetailsBean.getCustomerCurrencyId();
				parentSfdcOptyId = firstSiServiceDetailsBean.getParentOpportunityId();
			}

			Optional<SIServiceDetailsBean> siServiceDetail = serviceDetailsBeanList.stream()
					.min(Comparator.comparing(SIServiceDetailsBean::getContractTerm));
			if (siServiceDetail.isPresent())
				maxContractTerm = siServiceDetail.get().getContractTerm().intValue();

		}
		
		LOGGER.info("sp le id {}, currencyId {}, maxContract term {} from service inventory", erfSpLeId,
				erfCustCurrencyId, maxContractTerm);

		quoteTole.setQuoteCategory(quoteCategory);
		quoteTole.setQuoteType(MACDConstants.MACD_QUOTE_TYPE);
		quoteTole.setSourceSystem(MACDConstants.SOURCE_SYSTEM);
		quoteTole.setErfCusCustomerLegalEntityId(erfCustomerLeIdInt);
		quoteTole.setErfCusSpLegalEntityId(erfSpLeId);
		if (Objects.nonNull(quoteTole.getQuoteCategory())
				&& !quoteTole.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE))
			quoteTole.setStage(QuoteStageConstants.MODIFY.getConstantCode());
		quoteTole.setCurrencyId(erfCustCurrencyId);
		quoteTole.setTermInMonths(maxContractTerm + " months");
		quoteTole.setTpsSfdcParentOptyId(parentSfdcOptyId);
		if(MACDConstants.DEMO_EXTENSION.equalsIgnoreCase(quoteTole.getQuoteCategory())){

			LOGGER.info("Demo extension quote for quote code ---> {}  to set demo extension details.", quoteTole.getQuote().getQuoteCode());
			if(Objects.nonNull(firstSiServiceDetailsBean) && Objects.nonNull(firstSiServiceDetailsBean.getDemoFlag()) && "Yes".equalsIgnoreCase(firstSiServiceDetailsBean.getDemoFlag())){
				quoteTole.setIsDemo(BACTIVE);
				if(firstSiServiceDetailsBean.getDemoType().toLowerCase().contains("non-billable")){
					quoteTole.setDemoType("free");
				}
				if(firstSiServiceDetailsBean.getDemoType().toLowerCase().contains("billable") && !firstSiServiceDetailsBean.getDemoType().toLowerCase().contains("non")){
					quoteTole.setDemoType("paid");
				}

				LOGGER.info("Demo type and demo flag for demo extension quote ----> {} is ---> {} --- {} ", quoteTole.getQuote().getQuoteCode(),
						quoteTole.getIsDemo(),quoteTole.getDemoType());
			}
			else quoteTole.setIsDemo(BDEACTIVATE);
		}
		return quoteTole;
	}

	private void updateAttributeValues(UpdateRequest request, ComponentDetail cmpDetail) {
		cmpDetail.getAttributes().stream().forEach(attributeDetail -> {
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = null;
			if (attributeDetail.getAttributeId() == null) {
				LOGGER.info("Attribute Name:  {}", attributeDetail.getName());
				quoteProductComponentsAttributeValue = updateProductAttribute(request, cmpDetail, attributeDetail,
						quoteProductComponentsAttributeValue);

			}
		});
	}

	protected QuoteProductComponentsAttributeValue updateProductAttribute(UpdateRequest request,
			ComponentDetail cmpDetail, AttributeDetail attributeDetail,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		QuoteProductComponent quoteProductComponent = null;
		Optional<QuoteIllSite> siteEntity = illSiteRepository.findById(request.getSiteId());
		if (siteEntity.isPresent()) {
			List<ProductAttributeMaster> mstAttributeMaster = productAttributeMasterRepository
					.findByNameAndStatus(attributeDetail.getName(), CommonConstants.BACTIVE);
			ProductAttributeMaster productAttributeMaster = null;
			if (mstAttributeMaster.isEmpty()) {
				productAttributeMaster = new ProductAttributeMaster();
				productAttributeMaster.setName(attributeDetail.getName());
				productAttributeMaster.setStatus(CommonConstants.BACTIVE);
				productAttributeMaster.setDescription(attributeDetail.getName());
				productAttributeMaster.setCreatedBy(Utils.getSource());
				productAttributeMaster.setCreatedTime(new Date());
				productAttributeMasterRepository.save(productAttributeMaster);
			} else {
				productAttributeMaster = mstAttributeMaster.get(0);
			}
			List<QuoteProductComponent> prodComponent = null;
			if(IllSitePropertiesConstants.SITE_PROPERTIES.toString().equalsIgnoreCase(cmpDetail.getName())) {
			prodComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_Name(siteEntity.get().getId(), cmpDetail.getName());
			} else {
			prodComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(siteEntity.get().getId(), cmpDetail.getName(),
							cmpDetail.getType());
			}

			if (prodComponent.isEmpty()) {
				MstProductComponent productComponent = mstProductComponentRepository.findByName(cmpDetail.getName());
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(request.getFamilyName(), (byte) 1);

				quoteProductComponent = constructProductComponent(productComponent, mstProductFamily,
						request.getSiteId());
				quoteProductComponent.setType(cmpDetail.getType());
				quoteProductComponentRepository.save(quoteProductComponent);

			} else {
				quoteProductComponent = prodComponent.get(0);
				List<QuoteProductComponentsAttributeValue> quoteProductComponentAttributeValue = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent,
								productAttributeMaster);
				if (Objects.nonNull(quoteProductComponentAttributeValue)
						&& !quoteProductComponentAttributeValue.isEmpty())
					quoteProductComponentsAttributeValue = quoteProductComponentAttributeValue.get(0);
			}
			if (quoteProductComponent != null) {
				LOGGER.info("productAttribute" + productAttributeMaster.getName());
				if (quoteProductComponentsAttributeValue == null)
					quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
				quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
				quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
				quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
			}
		}
		return quoteProductComponentsAttributeValue;
	}

	/**
	 * 
	 * getOrderSummary - get all the quote details related to the given quote Id
	 * input
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */

	public MACDOrderSummaryResponseBean getOrderSummaryMulticircuit(Integer quoteId, Integer quoteLeId)
			throws TclCommonException {
		
		MACDOrderSummaryResponseBean macdOrderResponse = new MACDOrderSummaryResponseBean();
		List<MACDOrderSummaryResponse> macdOrderSummaryResponseList = new ArrayList<>();
		
		// 1. Location Id
		// 2. Port Bandwidth - Primary
		// 3. Local Loop Bandwidth - Primary
		// 4. CPE Model - Primary
		// 5. Interface - Primary
		// 6. IP Address Arrangement for Additional IPs
		// 7. IPv4 Address Pool Size for Additional IPs
		// 8. IPv6 Address Pool Size for Additional IPs
		// 9. Port Bandwidth - Secondary
		// 10.Local Loop Bandwidth - Secondary
		// 11. CPE Model - Secondary
		// 12. Interface - Secondary
		
		OrderSummaryBeanResponse siDetailedInfoResponseIAS = null;
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		String changeRequests = null;
	
		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId))
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);

		List<String> serviceIdsList = macdUtils.getServiceIds(quoteToLeOpt.get());
		String serviceIds = serviceIdsList.stream().map(i -> i.trim())
				.collect(Collectors.joining(","));
		
		// Queue call to get old attribute values from service details
		String iasQueueResponse = (String) mqUtils.sendAndReceive(siServiceDetailsListQueue, serviceIds);
		LOGGER.info(iasQueueResponse);
		if (StringUtils.isNotBlank(iasQueueResponse)) {
			siDetailedInfoResponseIAS = (OrderSummaryBeanResponse) Utils.convertJsonToObject(iasQueueResponse,
					OrderSummaryBeanResponse.class);
			// siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseIAS);
			// Logic to get new attribute values from oms
			
			siDetailedInfoResponseIAS.getServiceMap().entrySet().forEach(serviceId -> {
				
				MACDAttributesBean attributesBean = new MACDAttributesBean();

				MACDOrderSummaryResponse response = new MACDOrderSummaryResponse();
				Map<String, String> oldAttributesMapPrimary = new HashedMap<>();
				Map<String, String> oldAttributesMapSecondary = new HashedMap<>();
				Map<String, String> newAttributesMapPrimary = new HashedMap<>();
				Map<String, String> newAttributesMapSecondary = new HashedMap<>();
				List<MACDAttributesComparisonBean> primaryAttributesList = new ArrayList<>();
				List<MACDAttributesComparisonBean> secondaryAttributesList = new ArrayList<>();

				serviceId.getValue().stream().forEach(detailedInfo -> {
					LOGGER.info("Processing for circuit id {}",detailedInfo.getTpsServiceId());
				getLocationDetails(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
				getPortBandwidth(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
				getCPEDetails(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
				getInterfaceDetails(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
				getLocalLoopBandwidthDetails(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
				LOGGER.info("Old Attributes primary -{} secondary -{}",oldAttributesMapPrimary,oldAttributesMapSecondary);
				// getAdditionalIPDetails(oldAttributesMapPrimary, oldAttributesMapSecondary,
				// detailedInfo);

			});
                String type = null;


		// QuoteBean quoteBean = getQuoteDetails(quoteId, "");
		if (quoteToLeOpt.isPresent()) {
								try {
									List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId.getKey(), quoteToLeOpt.get());
									Optional<QuoteIllSiteToService> quoteIllSiteToServiceOpt = quoteIllSiteToService.stream().findFirst();
									if(quoteIllSiteToServiceOpt.isPresent()) {
                                        if(serviceId.getValue().get(0) != null && serviceId.getValue().get(0).getPrimaryOrSecondary() != null) {
                                            if(serviceId.getValue().get(0).getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE) || (serviceId.getValue().get(0).getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY))) {
                                                type = MACDConstants.PRIMARY_STRING.toLowerCase();
                                            }
                                            else {
                                            	 type = MACDConstants.SECONDARY_STRING.toLowerCase();
                                            }
                                        } else {
                                            type = MACDConstants.SECONDARY_STRING.toLowerCase();
                                        }

									List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndReferenceNameAndType(quoteIllSiteToServiceOpt.get().getQuoteIllSite().getId(),QuoteConstants.IZO_PC_SITES.toString(), type);
							if (type == null || type.equalsIgnoreCase(MACDConstants.PRIMARY_STRING.toLowerCase())) {
								getLatLongDetails(newAttributesMapPrimary, newAttributesMapSecondary,
										quoteIllSiteToServiceOpt.get().getQuoteIllSite());
							}

									getPrimaryPortDetails(newAttributesMapPrimary, newAttributesMapSecondary,
											quoteProductComponentList);

									getSecondaryPortDetails(newAttributesMapPrimary, newAttributesMapSecondary,
											quoteProductComponentList);
									getParallelBuildAndParallelRunDays(quoteProductComponentList, response);
									}
								} catch (Exception e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
											ResponseResource.R_CODE_ERROR);
								}

							

		}
		if (!newAttributesMapPrimary.equals(oldAttributesMapPrimary))

		{
			LOGGER.info("New Attri {}",newAttributesMapPrimary);
			newAttributesMapPrimary.entrySet().stream().filter(att -> !att.getKey()
					.equalsIgnoreCase(MACDConstants.LOCATION_ID)
					&& !((att.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
							|| att.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))
							&& Objects.nonNull(att.getValue()) && oldAttributesMapPrimary.containsKey(att.getKey())
							&& compareNewAndOldValues(oldAttributesMapPrimary.get(att.getKey()), att.getValue())))
					.forEach(attribute -> {
						if (Objects.nonNull(attribute.getValue()) && !attribute.getValue()
								.equalsIgnoreCase(oldAttributesMapPrimary.get(attribute.getKey()))) {
							LOGGER.info("Attribute name {}, attribute value {}, old attribute value {}", attribute.getKey(),
									attribute.getValue(),oldAttributesMapPrimary.get(attribute.getKey()));
							MACDAttributesComparisonBean bean = new MACDAttributesComparisonBean();
							bean.setAttributeName(attribute.getKey());
							if ((attribute.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
									|| attribute.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))) {
								bean.setNewAttributes(convertNewBandwidthValue(attribute.getValue()));
							} else
								bean.setNewAttributes(attribute.getValue());
							LOGGER.info("Attribute Key {}",attribute.getKey());
							bean.setOldAttributes(oldAttributesMapPrimary.get(attribute.getKey()));
							if (Objects.nonNull(attribute.getKey())
									&& attribute.getKey().equalsIgnoreCase(MACDConstants.LAT_LONG)) {
								if(quoteToLeOpt.isPresent() && Objects.nonNull(quoteToLeOpt.get().getQuoteCategory())) {
									LOGGER.info("Location id in quote - {} is different from service inventory - {} for category {}", 
											newAttributesMapPrimary.get(MACDConstants.LOCATION_ID), 
											oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID), quoteToLeOpt.get().getQuoteCategory());
									if(MACDConstants.SHIFT_SITE_SERVICE.equals(quoteToLeOpt.get().getQuoteCategory())) {
								bean.setAttributeName(MACDConstants.LOCATION_ID);
								bean.setNewAttributes(newAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
								bean.setOldAttributes(oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
								response.setChangeRequests(
										response.getChangeRequests() == null ? MACDConstants.SHIFT_SITE
												: response.getChangeRequests() + " + " + MACDConstants.SHIFT_SITE);
									}else {
										throw new TclCommonRuntimeException(ExceptionConstants.LOCATION_ID_MISMATCH, ResponseResource.R_CODE_ERROR);
									}
								}
							} else if (Objects.nonNull(attribute.getKey()) && (attribute.getKey()
									.equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
									|| attribute.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))) {
								response.setChangeRequests(
										response.getChangeRequests() == null ? MACDConstants.CHANGE_BANDWIDTH
												: response.getChangeRequests().contains(MACDConstants.CHANGE_BANDWIDTH)
														? response.getChangeRequests()
														: response.getChangeRequests() + " + "
																+ MACDConstants.CHANGE_BANDWIDTH);
							}
							primaryAttributesList.add(bean);
						}
					});
		}

		if (!newAttributesMapSecondary.equals(oldAttributesMapSecondary))

		{
			newAttributesMapSecondary.entrySet().stream()
					.filter(att -> !((att.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
							|| att.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))
							&& Objects.nonNull(att.getValue()) && oldAttributesMapSecondary.containsKey(att.getKey())
							&& compareNewAndOldValues(oldAttributesMapSecondary.get(att.getKey()), att.getValue())))
					.forEach(attribute -> {
						if (Objects.nonNull(attribute.getValue()) && !attribute.getValue()
								.equalsIgnoreCase(oldAttributesMapSecondary.get(attribute.getKey()))) {
							LOGGER.info("Attribute name {}, attribute value {}, old attribute value {}", attribute.getKey(),
									attribute.getValue(),oldAttributesMapSecondary.get(attribute.getKey()));
							MACDAttributesComparisonBean bean = new MACDAttributesComparisonBean();
							bean.setAttributeName(attribute.getKey());
							if ((attribute.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
									|| attribute.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString()))) {
								
								bean.setNewAttributes(convertNewBandwidthValue(attribute.getValue()));
							} else
								bean.setNewAttributes(attribute.getValue());
							bean.setOldAttributes(oldAttributesMapSecondary.get(attribute.getKey()));
							if (Objects.nonNull(attribute.getKey())
									&& attribute.getKey().equalsIgnoreCase(FPConstants.LOCAL_LOOP_BW.toString())
									|| attribute.getKey().equalsIgnoreCase(FPConstants.PORT_BANDWIDTH.toString())) {
								response.setChangeRequests(
										response.getChangeRequests() == null ? MACDConstants.CHANGE_BANDWIDTH
												: response.getChangeRequests().contains(MACDConstants.CHANGE_BANDWIDTH)
														? response.getChangeRequests()
														: response.getChangeRequests() + " + "
																+ MACDConstants.CHANGE_BANDWIDTH);
							}

							secondaryAttributesList.add(bean);
						}
					});
		}

		Optional<SIServiceInfoBean> siServiceInfoBean = serviceId.getValue().stream()
				.filter(service -> service.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
						|| service.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE))
				.findFirst();

		attributesBean.setPrimaryAttributesList(primaryAttributesList);
		attributesBean.setSecondaryAttributesList(secondaryAttributesList);
		response.setQuotesAttributes(attributesBean);
		response.setServiceId(serviceId.getKey());
		
		Optional<SIServiceInfoBean> serviceIdSelected = serviceId.getValue().stream().
				filter(serviceDetailSelected -> serviceDetailSelected.getTpsServiceId().equalsIgnoreCase(serviceId.getKey())).findFirst();
		

		response.setServiceType(serviceIdSelected.get().getPrimaryOrSecondary());
		if("PRIMARY".equalsIgnoreCase(response.getServiceType())) {
			response.setPrimaryServiceId(serviceIdSelected.get().getTpsServiceId());
			response.setSecondaryServiceId(serviceIdSelected.get().getPriSecServiceLink());
		}
		else if("SECONDARY".equalsIgnoreCase(response.getServiceType())) {
			response.setPrimaryServiceId(serviceIdSelected.get().getPriSecServiceLink());
			response.setSecondaryServiceId(serviceIdSelected.get().getTpsServiceId());
		}

		//end
		if (quoteToLeOpt.isPresent()) {
			quoteToLeOpt.get().setChangeRequestSummary(response.getChangeRequests());
			quoteToLeRepository.save(quoteToLeOpt.get());
			if (quoteToLeOpt.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.CHANGE_BANDWIDTH_SERVICE)) {
				if (siServiceInfoBean.isPresent())
					response.setSiteAddress(siServiceInfoBean.get().getSiteAddress());
			}
		}

		if (siServiceInfoBean.isPresent())
			try {
				response.setPricingsList(quoteCompare(quoteId, siServiceInfoBean.get().getSiOrderId(),
						siServiceInfoBean.get().getTpsServiceId()));
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
						ResponseResource.R_CODE_ERROR);
			}
		
		macdOrderSummaryResponseList.add(response);
			});
		}
		if (quoteToLeOpt.isPresent() && (Objects.nonNull(quoteToLeOpt.get().getQuoteCategory())
				&& !quoteToLeOpt.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE))) {
			if (quoteToLeOpt.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				quoteToLeOpt.get().setStage(QuoteStageConstants.CHANGE_ORDER.getConstantCode());
				quoteToLeRepository.save(quoteToLeOpt.get());
			}
		}

		macdOrderResponse.setMacdOrderSummaryResponseList(macdOrderSummaryResponseList);
		return macdOrderResponse;

	}

	private String convertNewBandwidthValue(String value) {
		Double newbandwidthVal = Double.valueOf(value.substring(0, (value.indexOf(MACDConstants.MBPS)) - 1));
		if (newbandwidthVal < 1)
			return String.valueOf((int) (newbandwidthVal * 1024) + CommonConstants.SPACE + MACDConstants.KBPS);
		else if (newbandwidthVal >= 1 && newbandwidthVal <= 999)
			return value;
		else if (newbandwidthVal > 999)
			return (String.valueOf((int) (newbandwidthVal / 1000)) + CommonConstants.SPACE + MACDConstants.GBPS);
		return null;
	}

	private void getParallelBuildAndParallelRunDays(List<QuoteProductComponent> quoteProductComponentList,
			MACDOrderSummaryResponse response) {
		quoteProductComponentList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(OrderDetailsExcelDownloadConstants.IZOPC_COMMON.toString())
						&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_BUILD.toString()))
								response.setParallelBuild(attribute.getAttributeValues());
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_RUN_DAYS.toString()))
								response.setParallelRunDays(attribute.getAttributeValues());
						}));

	}

	// private void getAdditionalIPDetails(Map<String, String>
	// oldAttributesMapPrimary,
	// Map<String, String> oldAttributesMapSecondary, SIServiceInfoBean
	// detailedInfo) {
	//
	// Optional<SIServiceAttributeBean> attValue =
	// detailedInfo.getAttributes().stream().filter(attribute -> attribute
	// .getAttributeName().equalsIgnoreCase(FPConstants.IP_ADDRESS_MANAGEMENT.toString())).findAny();
	// if (attValue.isPresent())
	// oldAttributesMapPrimary.put(FPConstants.IP_ADDRESS_MANAGEMENT.toString(),
	// attValue.get().getAttributeValue());
	//
	// }

	private void getLocalLoopBandwidthDetails(Map<String, String> oldAttributesMapPrimary,
			Map<String, String> oldAttributesMapSecondary, SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)) {
			oldAttributesMapPrimary.put(FPConstants.LOCAL_LOOP_BW.toString(), (detailedInfo.getLastMileBandwidth()
					+ CommonConstants.SPACE + detailedInfo.getLastMileBandwidthUnit()));
		} else if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_SECONDARY))
			oldAttributesMapSecondary.put(FPConstants.LOCAL_LOOP_BW.toString(), (detailedInfo.getLastMileBandwidth()
					+ CommonConstants.SPACE + detailedInfo.getLastMileBandwidthUnit()));

	}

	private void getInterfaceDetails(Map<String, String> oldAttributesMapPrimary,
			Map<String, String> oldAttributesMapSecondary, SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)) {
			oldAttributesMapPrimary.put(FPConstants.INTERFACE.toString(), detailedInfo.getSiteEndInterface());
		} else if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_SECONDARY))
			oldAttributesMapSecondary.put(FPConstants.INTERFACE.toString(), detailedInfo.getSiteEndInterface());
	}

	private void getCPEDetails(Map<String, String> oldAttributesMapPrimary,
			Map<String, String> oldAttributesMapSecondary, SIServiceInfoBean detailedInfo) {

		Optional<SIServiceAttributeBean> attValuePrimary = detailedInfo.getAttributes().stream()
				.filter(attribute -> attribute.getAttributeName()
						.equalsIgnoreCase(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue())
						&& (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
						|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)))
				.findAny();
		Optional<SIServiceAttributeBean> attValueSecondary = detailedInfo.getAttributes().stream()
				.filter(attribute -> attribute.getAttributeName()
						.equalsIgnoreCase(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue())
						&& detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_SECONDARY))
				.findAny();
		if (attValuePrimary.isPresent())
			oldAttributesMapPrimary.put(FPConstants.CPE.toString(), attValuePrimary.get().getAttributeValue());
		if (attValueSecondary.isPresent())
			oldAttributesMapSecondary.put(FPConstants.CPE.toString(), attValueSecondary.get().getAttributeValue());
	}

	private void getPortBandwidth(Map<String, String> oldAttributesMapPrimary,
			Map<String, String> oldAttributesMapSecondary, SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)) {
			oldAttributesMapPrimary.put(FPConstants.BANDWIDTH.toString(),
					(detailedInfo.getBandwidthPortSpeed() + CommonConstants.SPACE + detailedInfo.getBandwidthUnit()));
		} else if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_SECONDARY))
			oldAttributesMapSecondary.put(FPConstants.BANDWIDTH.toString(),
					(detailedInfo.getBandwidthPortSpeed() + CommonConstants.SPACE + detailedInfo.getBandwidthUnit()));

	}

	private void getLocationDetails(Map<String, String> oldAttributesMapPrimary,
			Map<String, String> oldAttributesMapSecondary, SIServiceInfoBean detailedInfo) {
		LOGGER.info("circuit Type {}",detailedInfo.getPrimaryOrSecondary());
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)) {
			oldAttributesMapPrimary.put(MACDConstants.LAT_LONG, detailedInfo.getLocationId().toString());
			LOGGER.info("setting location id as {}",detailedInfo.getSiteAddress());
			oldAttributesMapPrimary.put(MACDConstants.LOCATION_ID, detailedInfo.getSiteAddress());
		}else if(detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_SECONDARY)){
			oldAttributesMapSecondary.put(MACDConstants.LAT_LONG, detailedInfo.getLocationId().toString());
			LOGGER.info("setting secondary location id as {}",detailedInfo.getSiteAddress());
			oldAttributesMapSecondary.put(MACDConstants.LOCATION_ID, detailedInfo.getSiteAddress());
		}
	}

	// private void getAdditionalIPDetails(Map<String, String>
	// newAttributesMapPrimary,
	// Map<String, String> newAttributesMapSecondary, List<QuoteProductComponent>
	// componentsList) {
	// componentsList.stream()
	// .filter(quoteProdComponent ->
	// quoteProdComponent.getMstProductComponent().getName()
	// .equals(FPConstants.ADDITIONAL_IP.toString())
	// && quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
	// .findFirst().ifPresent(quoteProd ->
	// quoteProd.getQuoteProductComponentsAttributeValues().stream()
	// .forEach(attribute -> {
	// if (attribute.getProductAttributeMaster().getName()
	// .equals(FPConstants.IP_ADDRESS_MANAGEMENT.toString())) {
	// newAttributesMapPrimary.put(MACDConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS,
	// attribute.getAttributeValues());
	// }
	// if (attribute.getProductAttributeMaster().getName()
	// .equals(FPConstants.ADDITIONAL_IP_IPV6.toString())
	// && Objects.nonNull(newAttributesMapPrimary
	// .get(MACDConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS))
	// && (attribute.getAttributeValues().equalsIgnoreCase(MACDConstants.IPv6)
	// || ((newAttributesMapPrimary
	// .get(MACDConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS))
	// .equalsIgnoreCase(MACDConstants.IPv6)
	// || (newAttributesMapPrimary.get(
	// MACDConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS))
	// .equalsIgnoreCase(MACDConstants.DUAL)))) {
	//
	// newAttributesMapPrimary.put(MACDConstants.IPV4_ADDRESS_POOL_SIZE_FOR_ADDITIONAL_IPS,
	// attribute.getAttributeValues());
	// }
	// if (attribute.getProductAttributeMaster().getName()
	// .equals(FPConstants.ADDITIONAL_IP_IPV6.toString())
	// && Objects.nonNull(newAttributesMapPrimary
	// .get(MACDConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS))
	// && (attribute.getAttributeValues().equalsIgnoreCase(MACDConstants.IPv6)
	// || ((newAttributesMapPrimary
	// .get(MACDConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS))
	// .equalsIgnoreCase(MACDConstants.IPv6)
	// || (newAttributesMapPrimary.get(
	// MACDConstants.IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS))
	// .equalsIgnoreCase(MACDConstants.DUAL)))) {
	//
	// newAttributesMapPrimary.put(MACDConstants.IPV6_ADDRESS_POOL_SIZE_FOR_ADDITIONAL_IPS,
	// attribute.getAttributeValues());
	// }
	// }));
	//
	// }

	private void getSecondaryCPEModelDetails(Map<String, String> newAttributesMapPrimary,
			Map<String, String> newAttributesMapSecondary, List<QuoteProductComponent> componentsList) {
		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(FPConstants.CPE.toString())
						&& quoteProdComponent.getType().equals(FPConstants.SECONDARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(FPConstants.CPE_BASIC_CHASSIS.toString()))
								newAttributesMapSecondary.put(FPConstants.CPE.toString(),
										attribute.getAttributeValues());
						}));

	}

	private void getPrimaryCPEModelDetails(Map<String, String> newAttributesMapPrimary,
			Map<String, String> newAttributesMapSecondary, List<QuoteProductComponent> componentsList) {
		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(FPConstants.CPE.toString())
						&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(FPConstants.CPE_BASIC_CHASSIS.toString()))
								newAttributesMapPrimary.put(FPConstants.CPE.toString(), attribute.getAttributeValues());
						}));

		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(FPConstants.CPE.toString())
						&& quoteProdComponent.getType().equals(FPConstants.SECONDARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(FPConstants.CPE_BASIC_CHASSIS.toString()))
								newAttributesMapSecondary.put(FPConstants.CPE.toString(),
										attribute.getAttributeValues());
						}));

	}

	private void getSecondaryLastMileDetails(Map<String, String> newAttributesMapPrimary,
			Map<String, String> newAttributesMapSecondary, List<QuoteProductComponent> componentsList) {
		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(FPConstants.LAST_MILE.toString())
						&& quoteProdComponent.getType().equals(FPConstants.SECONDARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(FPConstants.LOCAL_LOOP_BW.toString()))
								newAttributesMapSecondary.put(FPConstants.LOCAL_LOOP_BW.toString(),
										(attribute.getAttributeValues() + CommonConstants.SPACE + MACDConstants.MBPS));

						}));

	}

	private void getPrimaryLastMileDetails(Map<String, String> newAttributesMapPrimary,
			Map<String, String> newAttributesMapSecondary, List<QuoteProductComponent> componentsList) {
		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(FPConstants.LAST_MILE.toString())
						&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(FPConstants.LOCAL_LOOP_BW.toString()))
								newAttributesMapPrimary.put(FPConstants.LOCAL_LOOP_BW.toString(),
										(attribute.getAttributeValues() + CommonConstants.SPACE + MACDConstants.MBPS));

						}));

	}

	private void getPrimaryPortDetails(Map<String, String> newAttributesMapPrimary,
			Map<String, String> newAttributesMapSecondary, List<QuoteProductComponent> componentsList) {
		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(FPConstants.IZO_PORT.toString())
						&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(FPConstants.BANDWIDTH.toString())) {
								newAttributesMapPrimary.put(FPConstants.BANDWIDTH.toString(),
										(attribute.getAttributeValues() + CommonConstants.SPACE + MACDConstants.MBPS));

							}

						}));
	}

	private void getSecondaryPortDetails(Map<String, String> newAttributesMapPrimary,
			Map<String, String> newAttributesMapSecondary, List<QuoteProductComponent> componentsList) {
		componentsList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(FPConstants.IZO_PORT.toString())
						&& quoteProdComponent.getType().equals(FPConstants.SECONDARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							if (attribute.getProductAttributeMaster().getName()
									.equals(FPConstants.BANDWIDTH.toString())) {
								newAttributesMapSecondary.put(FPConstants.BANDWIDTH.toString(),
										(attribute.getAttributeValues() + CommonConstants.SPACE + MACDConstants.MBPS));
							}

						}));
	}

	private void getLatLongDetails(Map<String, String> newAttributesMapPrimary,
			Map<String, String> newAttributesMapSecondary, QuoteIllSite illSite)
			throws TclCommonException, IllegalArgumentException {
		String locationResponse = (String) mqUtils.sendAndReceive(locationDetailQueue,
				illSite.getErfLocSitebLocationId().toString());
		LocationDetail[] locationDetailResponse = (LocationDetail[]) Utils.convertJsonToObject(locationResponse,
				LocationDetail[].class);
		if (locationDetailResponse.length > 0) {
			newAttributesMapPrimary.put(MACDConstants.LAT_LONG, locationDetailResponse[0].getLocationId().toString());
			String address = constructUserAddress(locationDetailResponse);
			newAttributesMapPrimary.put(MACDConstants.LOCATION_ID, address);
		}
	}

	private String constructUserAddress(LocationDetail[] locationDetailResponse) {
		String address = StringUtils.EMPTY;
		if (locationDetailResponse[0].getUserAddress().getAddressLineOne() != null)
			address += locationDetailResponse[0].getUserAddress().getAddressLineOne();
		// if (locationDetailResponse[0].getUserAddress().getAddressLineTwo() != null)
		// address += CommonConstants.SPACE +
		// locationDetailResponse[0].getUserAddress().getAddressLineTwo();
		if (locationDetailResponse[0].getUserAddress().getLocality() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getLocality();
		if (locationDetailResponse[0].getUserAddress().getCity() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getCity();
		if (locationDetailResponse[0].getUserAddress().getState() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getState();
		if (locationDetailResponse[0].getUserAddress().getPincode() != null)
			address += CommonConstants.SPACE + locationDetailResponse[0].getUserAddress().getPincode();
		return address;
	}

	/**
	 * Method for compare quotes API
	 * 
	 * @param quoteId
	 * @param orderid
	 * @param servicedetailid
	 * @return
	 * @throws TclCommonException
	 */

	public CompareQuotes quoteCompare(Integer quoteId, Integer orderid, String tpsId) throws TclCommonException {
		Objects.requireNonNull(quoteId, ExceptionConstants.QUOTE_ID_ERROR);
		Objects.requireNonNull(orderid, ExceptionConstants.SERVICE_ID_ERROR);
		Objects.requireNonNull(tpsId, ExceptionConstants.TPS_ID_ERROR);

		double finalArc = 0D;
		double finalNrc = 0D;
		double finalMrc = 0D;

		CompareQuotes compareQuotes = new CompareQuotes();
		Map<String, CompareQuotePrices> arcPrice = new HashMap<>();
		Map<String, CompareQuotePrices> nrcPrice = new HashMap<>();
		Map<String, CompareQuotePrices> mrcPrice = new HashMap<>();

		Optional<Quote> quote = quoteRepository.findById(quoteId);
		quote.get().getQuoteToLes().stream().forEach(quoteToLe -> {
			quoteToLe.getQuoteToLeProductFamilies().stream().forEach(productFamily -> {
				productFamily.getProductSolutions().stream().forEach(productSolution -> {
					productSolution.getQuoteIllSites().stream()
							.filter(illsite -> illsite.getStatus().equals(CommonConstants.BACTIVE))
							.forEach(illSites -> {
								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndReferenceName(illSites.getId(),QuoteConstants.IZO_PC_SITES.toString());
								quoteProductComponents.stream().forEach(quoteProductComponent -> {
									QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
											String.valueOf(quoteProductComponent.getId()), MACDConstants.COMPONENTS);
									getNewQuotesARCNRC(arcPrice, nrcPrice,mrcPrice, quoteProductComponent, quotePrice);
								});
							});
				});
			});
		});

		if (quote.get().getQuoteToLes().stream().findFirst().isPresent()
				&& Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getFinalArc()))
			finalArc = quote.get().getQuoteToLes().stream().findFirst().get().getFinalArc();
		if (quote.get().getQuoteToLes().stream().findFirst().isPresent()
				&& Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getFinalNrc()))
			finalNrc = quote.get().getQuoteToLes().stream().findFirst().get().getFinalNrc();
		if (quote.get().getQuoteToLes().stream().findFirst().isPresent()
				&& Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getFinalMrc()))
			finalMrc = quote.get().getQuoteToLes().stream().findFirst().get().getFinalMrc();

		List<CompareQuotePrices> quotePricesList = new ArrayList<>();
		/*SIServiceDetailDataBean sIServiceDetailDataBean = getOldQuotesNRCARC(orderid, tpsId);*/
		SIServiceDetailDataBean sIServiceDetailDataBean = macdUtils.getServiceDetail(tpsId, quote.get().getQuoteToLes().stream().findFirst().get().getQuoteCategory());
		getQuotePrices(arcPrice, quote, quotePricesList,
				Objects.isNull(sIServiceDetailDataBean.getArc()) ? 0d : sIServiceDetailDataBean.getArc(),
				MACDConstants.ARC, finalArc);
		getQuotePrices(nrcPrice, quote, quotePricesList,
				Objects.isNull(sIServiceDetailDataBean.getNrc()) ? 0d : sIServiceDetailDataBean.getNrc(),
				MACDConstants.NRC, finalNrc);
		//GVPN INTL MACD
		getQuotePrices(mrcPrice, quote, quotePricesList,
				Objects.isNull(sIServiceDetailDataBean.getMrc()) ? 0d : sIServiceDetailDataBean.getMrc(),
				MACDConstants.MRC, finalMrc);
		compareQuotes.setSolutionQuote(getTotalSolutionQuote(quote,sIServiceDetailDataBean));
		compareQuotes.setPrices(quotePricesList);
		return compareQuotes;

	}

	/**
	 * Method to get Total Solution Quotes in compare quotes API
	 * 
	 * @param quotePricesList
	 */
	private TotalSolutionQuote getTotalSolutionQuote(Optional<Quote> quote ,SIServiceDetailDataBean sIServiceDetailDataBean) {
		TotalSolutionQuote quote1 = new TotalSolutionQuote();
		double oldArc = Objects.isNull(sIServiceDetailDataBean.getArc()) ? 0d : sIServiceDetailDataBean.getArc();
		double oldNrc = Objects.isNull(sIServiceDetailDataBean.getNrc()) ? 0d : sIServiceDetailDataBean.getNrc();
		double newTcv = 0D;
		String termInMonths = null;
		if (quote.get().getQuoteToLes().stream().findFirst().isPresent() && Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getTotalTcv()))
			newTcv = quote.get().getQuoteToLes().stream().findFirst().get().getTotalTcv();
		if (quote.get().getQuoteToLes().stream().findFirst().isPresent() && Objects.nonNull(quote.get().getQuoteToLes().stream().findFirst().get().getTermInMonths()))
			termInMonths = quote.get().getQuoteToLes().stream().findFirst().get().getTermInMonths();
		 String intValue= termInMonths.replaceAll("[^0-9]", "");
		double oldTcv = (Integer.valueOf(intValue)/12)*oldArc + oldNrc;
			quote1.setName(MACDConstants.SOLUTION_QUOTE);
			quote1.setOldQuote(oldTcv);
			quote1.setNewQuote(newTcv);
			quote1.setDelta(newTcv - oldTcv);
			
			//GVPN INTL MACD 
			List<QuoteToLe> quoteToLe=quoteToLeRepository.findByQuote(quote.get());
			QuoteToLeProductFamily productfamily=quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLe.get(0).getId());
			final Boolean[] intl= {false};
			if(quoteToLe!=null && quoteToLe.get(0).getCurrencyCode()!=null) {
				if(quoteToLe.get(0).getCurrencyCode().equalsIgnoreCase("USD")) {
					intl[0]=true;
				}
			}
		   if (quoteToLe != null && productfamily != null) {
			LOGGER.info("flag : quotetype: product" + intl[0] + " " + quoteToLe.get(0).getQuoteType()
					+ productfamily.getMstProductFamily().getName().equalsIgnoreCase("IZOPC"));
			if (quoteToLe.get(0).getQuoteType().equalsIgnoreCase("MACD") && intl[0]
					&& productfamily.getMstProductFamily().getName().equalsIgnoreCase("IZOPC")) {
				quote1.setCurrencyType(MACDConstants.INTL_CURRENCY_TYPE);
			} else {
				quote1.setCurrencyType(MACDConstants.CURRENCY_TYPE);
			}
		}
		else {
				quote1.setCurrencyType(MACDConstants.CURRENCY_TYPE);
			}
			
		return quote1;
	}

	/**
	 * Method to get Old ARC and NRC prices
	 * 
	 * @param tpsId
	 * @param response1
	 * @throws TclCommonException
	 */
	/*public SIServiceDetailDataBean getOldQuotesNRCARC(Integer orderId, String tpsId) throws TclCommonException {
		SIServiceDetailDataBean sIServiceDetailDataBean = null;
		try {
			LOGGER.info("orderId"+orderId+"tps service ID"+tpsId);
			SIOrderDataBean sIOrderDataBean = macdUtils.getSiOrderData(String.valueOf(orderId));
			LOGGER.info("siorder data"+sIOrderDataBean.toString());
			Optional<SIServiceDetailDataBean> serviceDetailBean = sIOrderDataBean.getServiceDetails().stream()
					.filter(sId -> sId.getTpsServiceId().equals(tpsId)).findFirst();
			LOGGER.info("servicedetail"+serviceDetailBean);
			if(serviceDetailBean.isPresent())
			LOGGER.info("serviceDetail data"+serviceDetailBean.get().toString());
			if (Objects.nonNull(serviceDetailBean)) {
				sIServiceDetailDataBean = serviceDetailBean.get();
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return sIServiceDetailDataBean;
	}*/

	/**
	 * This method fetches and sets the component values/prices
	 * 
	 * @param nrcPrice
	 * @param quote
	 * @param compareQuotes
	 * @param quotePricesList
	 */

	private void getQuotePrices(Map<String, CompareQuotePrices> priceMap, Optional<Quote> quote,
			List<CompareQuotePrices> quotePricesList, double oldValue, String name, double finalPrice) {
		CompareQuotePrices compareQuotePrices = new CompareQuotePrices();
		compareQuotePrices.setName(name);
		compareQuotePrices.setOldQuotePrice(oldValue);
		compareQuotePrices.setNewQuotePrice(finalPrice);
		compareQuotePrices.setDelta(compareQuotePrices.getNewQuotePrice() - compareQuotePrices.getOldQuotePrice());
		List<ComponentQuotePrices> ComponentList = new ArrayList<>();

		setComponentValues(priceMap, ComponentList);
		compareQuotePrices.setComponents(ComponentList);
		quotePricesList.add(compareQuotePrices);
	}

	private void setComponentValues(Map<String, CompareQuotePrices> priceMap,
			List<ComponentQuotePrices> ComponentList) {
		priceMap.entrySet().forEach(value -> {
			ComponentQuotePrices componentQuotePrices = new ComponentQuotePrices();
			componentQuotePrices.setComponentName(value.getKey());
			componentQuotePrices.setNewQuote(value.getValue().getNewQuotePrice());
			componentQuotePrices.setOldQuote(0d);
			componentQuotePrices.setDelta(componentQuotePrices.getNewQuote() - componentQuotePrices.getOldQuote());
			ComponentList.add(componentQuotePrices);
		});
	}

	/**
	 * This method fetches the new quotes of both ARC and NRC components
	 * 
	 * @param arcPrice
	 * @param nrcPrice
	 * @param quoteProductComponent
	 * @param quotePrice
	 */
	private void getNewQuotesARCNRC(Map<String, CompareQuotePrices> arcPrice, Map<String, CompareQuotePrices> nrcPrice,Map<String, CompareQuotePrices> mrcPrice,
			QuoteProductComponent quoteProductComponent, QuotePrice quotePrice) {
		if (!Objects.isNull(quotePrice)) {
			if (Objects.nonNull(quotePrice.getEffectiveArc())) {
				CompareQuotePrices compareQuotePrices = new CompareQuotePrices();
				if (!arcPrice.containsKey(quoteProductComponent.getMstProductComponent().getName())) {
					compareQuotePrices.setNewQuotePrice(quotePrice.getEffectiveArc());
					arcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				} else {
					Double arcValue = arcPrice.get(quoteProductComponent.getMstProductComponent().getName())
							.getNewQuotePrice() + quotePrice.getEffectiveArc();
					compareQuotePrices.setNewQuotePrice(arcValue);
					arcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				}
				arcPrice.remove("Shifting Charges");
			}
			if (Objects.nonNull(quotePrice.getEffectiveNrc())) {
				CompareQuotePrices compareQuotePrices = new CompareQuotePrices();
				if (!nrcPrice.containsKey(quoteProductComponent.getMstProductComponent().getName())) {
					compareQuotePrices.setNewQuotePrice(quotePrice.getEffectiveNrc());
					nrcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				} else {
					Double nrcValue = nrcPrice.get(quoteProductComponent.getMstProductComponent().getName())
							.getNewQuotePrice() + quotePrice.getEffectiveNrc();
					compareQuotePrices.setNewQuotePrice(nrcValue);
					nrcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				}
			}
			//gvpn macd intl 
			if (Objects.nonNull(quotePrice.getEffectiveMrc())) {
				LOGGER.info("QUOTE COMPARE EFFECTIVE MRC AND ID"+quotePrice.getEffectiveMrc()+quotePrice.getId());
				CompareQuotePrices compareQuotePrices = new CompareQuotePrices();
				if (!mrcPrice.containsKey(quoteProductComponent.getMstProductComponent().getName())) {
					compareQuotePrices.setNewQuotePrice(quotePrice.getEffectiveMrc());
					mrcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				} else {
					Double mrcValue = mrcPrice.get(quoteProductComponent.getMstProductComponent().getName())
							.getNewQuotePrice() + quotePrice.getEffectiveMrc();
					compareQuotePrices.setNewQuotePrice(mrcValue);
					mrcPrice.put(quoteProductComponent.getMstProductComponent().getName(), compareQuotePrices);
				}
			}
		}
	}

	
	private void deactivateOtherSites(Optional<QuoteToLe> quoteToLe) {
		quoteToLe.get().getQuoteToLeProductFamilies().stream().forEach(quoteProdFamily -> {
			quoteProdFamily.getProductSolutions().stream().forEach(quoteProdSolution -> {
				quoteProdSolution.getQuoteIllSites().stream().forEach(site -> {
					try {
						processDeactivateSites(site.getId(), quoteToLe.get().getQuote().getId(), "delete");
					}
					catch(Exception e)
					{
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
					/*site.setStatus(CommonConstants.BDEACTIVATE);
					illSiteRepository.save(site);*/
				});
			});
		});

	}

	/**
	 * This method is used to request for termination of service
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param serviceId
	 * @param reason
	 * @param cancellationDate
	 * @param cpeRecovery
	 * @return
	 * @throws TclCommonException
	 */

	public MACDCancellationRequestResponse requestForCancellation(MACDCancellationRequest request)
			throws TclCommonException {
		MACDCancellationRequestResponse response = new MACDCancellationRequestResponse();
		try {
			validateRequest(request);
			response.setCpeCharges("300");
			response.setEarlyTerminationCharges("200");
			response.setLmPriceRevision("NA");
			response.setRequestId("8923458");

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	private void validateRequest(MACDCancellationRequest request) throws TclCommonException {
		if (request.getServiceId() == null || request.getCancellationDate() == null || request.getReason() == null)
			throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);

	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/ getQuoteDetails- This method is used
	 *       to get the quote details
	 * 
	 * @param quoteId
	 * @param version
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteBean getMacdQuoteDetails(Integer quoteId, String feasibleSites) throws TclCommonException {
		QuoteBean response = null;
		try {
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString()));
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites, true,null);
			List<QuoteToLe> quoteToLeList = getQuoteToLeBasenOnVersion(quote);
			if (!quoteToLeList.isEmpty()) {
				QuoteToLe quoteToLe = quoteToLeList.get(0);
				/*SIOrderDataBean sIOrderDataBean = macdUtils
						.getSiOrderData(String.valueOf(quoteToLe.getErfServiceInventoryParentOrderId()));
				SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean,
						quoteToLe.getErfServiceInventoryServiceDetailId());
				if (Objects.nonNull(serviceDetail))
					response.setSiteAlias(serviceDetail.getAlias());
				if (Objects.nonNull(serviceDetail)) {
					SIServiceDetailDataBean secServiceDetail = macdUtils
							.getSiServiceDetailBeanBasedOnServiceId(sIOrderDataBean, serviceDetail.getLinkId());
					if (Objects.nonNull(secServiceDetail))
						response.setSecondaryAlias(secServiceDetail.getAlias());
				}*/
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
						response.setServiceOrderId(quoteIllSiteToServiceList.stream().findFirst().get().getErfServiceInventoryParentOrderId());
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
						List<QuoteIllSiteToService> quoteIllSiteToServices = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
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

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	public QuoteDetail macdApprovedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {
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
				//TOBE REMOVED ONCE O2C deployed
				order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
				order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
				orderRepository.save(order);
				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());
				detail.setOrderCategory(order.getOrderToLes().stream().findFirst().get().getOrderCategory());
				detail.setOrderType(order.getOrderToLes().stream().findFirst().get().getOrderType());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC
				if (quote.getQuoteToLes().stream().findFirst().isPresent()) {
					QuoteToLe quoteLe = quote.getQuoteToLes().stream().findFirst().get();
					omsSfdcService.processSiteDetails(quoteLe);
					omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

					Boolean nat = !(request.getCheckList() == null
							|| request.getCheckList().equalsIgnoreCase(CommonConstants.NO));
					Map<String, String> cofObjectMapper = new HashMap<>();
					izoPcQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(),
							cofObjectMapper);
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
					if (cofDetails != null) {
						cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
					}

					for (OrderToLe orderToLe : order.getOrderToLes()) {
						izoPcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
								orderToLe.getId(), cofObjectMapper);
						break;
					}
					// Trigger orderMail
					Integer userId = order.getCreatedBy();
					String userEmail = null;
					if (userId != null) {
						Optional<User> userDetails = userRepository.findById(userId);
						if (userDetails.isPresent()) {
							userEmail = userDetails.get().getEmailId();
						}
					}
					processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
					List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
					quoteDelegate.stream().forEach(quoteDelegation -> {
						quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
						quoteDelegationRepository.save(quoteDelegation);
					});
				}

			}

			quote.getQuoteToLes().stream().forEach(quoteLe -> {

				if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					if (quoteLe.getStage().equals(OrderStagingConstants.ORDER_CONFIRMED.getStage())) {
						quoteLe.setStage(MACDConstants.TERMINATION_REQUEST_CREATED);
						quoteToLeRepository.save(quoteLe);
					}
				} else if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& !quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
					if (quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				} else if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
					if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				}

				List<MacdDetail> macdDetailList = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteLe.getId());
				if (Objects.nonNull(macdDetailList) && !macdDetailList.isEmpty()) {
					LOGGER.info("Setting MACD_ORDER_INITIATED for all quote to le {}", quoteLe.getId());
					macdDetailList.stream().forEach(macdDetail -> {
						macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
						macdDetailRepository.save(macdDetail);
					});

				}
			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order Method to get save Macd order type and
	 *            order Category
	 *
	 * @param order
	 * @param quote
	 */
	private void saveMacdOrderTypeAndOrderCategory(Order order, Quote quote) {
		order.getOrderToLes().stream().forEach(orderToLe -> {
			orderToLe.setOrderType(quote.getQuoteToLes().stream().findFirst().get().getQuoteType());
			if(Objects.nonNull(quote.getNsQuote()) && CommonConstants.Y.equalsIgnoreCase(quote.getNsQuote())) {
				quote.getQuoteToLes().stream().forEach(quoteToLe -> {
					Optional<QuoteLeAttributeValue> quoteLeAttributeValues = quoteToLe.getQuoteLeAttributeValues().stream().
							filter(attribVal -> AttributeConstants.ORDER_CATEGORY.toString().equalsIgnoreCase(attribVal.getMstOmsAttribute().getName())).findFirst();
					LOGGER.info("Order Category selected for NS orders GVPN");
					if(quoteLeAttributeValues.isPresent()) {
						LOGGER.info("Order Category selected for NS orders : {}", quoteLeAttributeValues.get().getAttributeValue());
                        if(Objects.nonNull(quoteLeAttributeValues.get().getAttributeValue()) && StringUtils.isNotBlank(quoteLeAttributeValues.get().getAttributeValue())) {
                            orderToLe.setOrderCategory(quoteLeAttributeValues.get().getAttributeValue());
                        } else {
                            orderToLe.setOrderCategory(quote.getQuoteToLes().stream().findFirst().get().getQuoteCategory());
                        }

					}
				});
			} 
			else {
			orderToLe.setOrderCategory(quote.getQuoteToLes().stream().findFirst().get().getQuoteCategory());
			}
			orderToLe.setErfServiceInventoryParentOrderId(
					quote.getQuoteToLes().stream().findFirst().get().getErfServiceInventoryParentOrderId());
			orderToLe.setTpsSfdcParentOptyId(quote.getQuoteToLes().stream().findFirst().get().getTpsSfdcParentOptyId());
			orderToLe.setSourceSystem(quote.getQuoteToLes().stream().findFirst().get().getSourceSystem());
			orderToLeRepository.save(orderToLe);
		});
	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/ getOrderDetails- This method is used
	 *       to get the order details
	 *
	 * @param orderId
	 * @return OrdersBean
	 * @throws TclCommonException
	 */
	public OrdersBean getOrderDetails(Integer orderId) throws TclCommonException {
		OrdersBean ordersBean = null;
		try {
			Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);

			if (order == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			ordersBean = izoPcOrderService.constructOrder(order);
			ordersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
			ordersBean.setOrderType(order.getOrderToLes().stream().findFirst().get().getOrderType());
			ordersBean.setOrderCategory(order.getOrderToLes().stream().findFirst().get().getOrderCategory());


			List<String> serviceIdsList=new ArrayList<>();
			//Get Service Id and order Id list
			serviceIdsList=macdUtils.getServiceIdListBasedOnOrderToLe(order.getOrderToLes().stream().findFirst().get());

			LOGGER.info("IsMultiCircuit: " +ordersBean.getIsMultiCircuit());
			if (ordersBean.getIsMultiCircuit().equals(false))
			{
				if (Objects.nonNull(serviceIdsList) && !serviceIdsList.isEmpty()) {
					LOGGER.info("Service Id: " + serviceIdsList.get(0));
					ordersBean.setServiceId(serviceIdsList.get(0));
					LOGGER.info("Response Service Id: " + ordersBean.getServiceId());
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

			}
			if(flagList.contains(true))
				ordersBean.setIsMacdInitiated(true);

			if(order.getOrderToLes().stream().findFirst().get().getIsMultiCircuit()==1)
			{
				ordersBean.setIsMultiCircuit(true);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBean;
	}

	/**
	 * Method to create Macd Order Detail
	 *
	 * @param quoteToLe
	 * @param cancellationDate
	 * @param cancellationReason
	 */
	public void createMacdOrderDetail(QuoteToLe quoteToLe, String cancellationDate, String cancellationReason)
			throws ParseException {
		
		if (Objects.nonNull(quoteToLe)) {
			List<String> serviceDetailsList = macdUtils.getAllServiceIdListBasedOnQuoteToLe(quoteToLe);
			LOGGER.info("Size of Service details list is {} ", serviceDetailsList.size());
			serviceDetailsList.stream().forEach(serviceId -> {
				MacdDetail macdDetail = new MacdDetail();
				LOGGER.info("Creating macddetail for service id {}", serviceId);
			macdDetail.setQuoteToLeId(quoteToLe.getId());
			if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
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
			}
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

	/**
	 * Method to find order ill site
	 * 
	 * @param order
	 * @return
	 */
	public Integer findOrderIllSite(Order order) {
		Integer orderIllSiteId = null;
		OrderToLe orderToLe = order.getOrderToLes().stream().findFirst().get();
		OrderToLeProductFamily orderToLeProductFamily = orderToLe.getOrderToLeProductFamilies().stream().findFirst()
				.get();
		List<OrderProductSolution> orderProductSolution = orderProductSolutionRepository
				.findByOrderToLeProductFamily(orderToLeProductFamily);
		if (!orderProductSolution.isEmpty() && orderProductSolution.size() > 0) {
			for (OrderProductSolution orderPrdtSol : orderProductSolution) {

				List<OrderIllSite> orderIllSite = orderIllSitesRepository
						.findByOrderProductSolutionAndStatus(orderPrdtSol, (byte) 1);
				if (!orderIllSite.isEmpty() && orderIllSite.size() > 0) {

					OrderIllSite ordIllSite = orderIllSite.stream().findFirst().get();
					orderIllSiteId = ordIllSite.getId();
					break;
				}
			}
		}
		return orderIllSiteId;
	}

	private String convertNewValuetoKbps(String newValue) {
		String bandwidthVal = newValue.substring(0, (newValue.indexOf(MACDConstants.MBPS)) - 1);
		Double newVal = Double.valueOf(bandwidthVal);
		return String.valueOf((int) (newVal * 1024));
	}

    private String convertNewValuetoGbps(String newValue) {
        String bandwidthVal = newValue.substring(0, (newValue.indexOf(MACDConstants.MBPS)) - 1);
        Double newVal = Double.valueOf(bandwidthVal);
        return String.valueOf((int) (newVal / 1000));
    }

	private Boolean compareNewAndOldValues(String oldBandwidth, String newBandwidth) {
		if (oldBandwidth.toLowerCase().contains(MACDConstants.KBPS_lOWER_CASE)) {
			String newBandwidthInKbps = convertNewValuetoKbps(newBandwidth);
			String oldbandwidthValInKbps = oldBandwidth.substring(0, (oldBandwidth.indexOf(MACDConstants.KBPS)) - 1);
			return (oldbandwidthValInKbps.equals(newBandwidthInKbps));

		}else if (oldBandwidth.toLowerCase().contains(MACDConstants.GBPS_lOWER_CASE)) {
            String newBandwidthInGbps = convertNewValuetoGbps(newBandwidth);
            String oldbandwidthValInGbps = oldBandwidth.substring(0, (oldBandwidth.indexOf(MACDConstants.GBPS)) - 1);
            LOGGER.info("newBandwidthInGbps-"+newBandwidthInGbps+"oldbandwidthInGbps-"+oldbandwidthValInGbps);
            return (oldbandwidthValInGbps.equals(newBandwidthInGbps));
        } else if (oldBandwidth.toLowerCase().contains(MACDConstants.MBPS_LOWER_CASE)) {
			return oldBandwidth.equals(newBandwidth);
		}

		return null;
	}

	/**
	 * Method to setMacdFlag
	 *
	 * @param macdFlag
	 * @param response
	 * @return
	 */
	public Boolean setMacdFlag(Map<String,Object> macdFlag,Boolean[] macdFlagValue)
	{
		macdFlag.entrySet().stream().forEach(entry->{
			Boolean flag=(Boolean)entry.getValue();
			if(flag)
				macdFlagValue[0]=flag;
		});
		return macdFlagValue[0];
	}

	/**
	 * Method to approve macd docusign quotes
	 *
	 * @param quoteuuId
	 * @return
	 * @throws TclCommonException
	 */
	public QuoteDetail approvedMacdDocusignQuotes(String quoteuuId) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			Quote quote = quoteRepository.findByQuoteCode(quoteuuId);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				order = constructOrder(quote, detail);
				if (Objects.nonNull(order.getOrderToLes()) && !order.getOrderToLes().isEmpty()) {
					Optional<OrderToLe> orderToLeOptional = order.getOrderToLes().stream().findFirst();
					if (orderToLeOptional.isPresent()) {
						OrderToLe orderToLe = orderToLeOptional.get();
						String classification = "";
						if (StringUtils.isNotBlank(orderToLe.getClassification())) {
							classification = orderToLe.getClassification();
							LOGGER.info("Classification type: {}", classification);
						}
						List<OrderIllSiteToService> orderIllSites = orderIllSiteToServiceRepository
								.findByOrderToLe_Id(orderToLe.getId());
						List<String> feasibilityModes = new ArrayList<>();
						if (Objects.nonNull(orderIllSites) && !orderIllSites.isEmpty()) {
							orderIllSites.stream().forEach(orderIllSiteToService -> {
								OrderIllSite site = orderIllSiteToService.getOrderIllSite();
								List<OrderSiteFeasibility> orderSiteFeasibility = orderSiteFeasibilityRepository
										.findByOrderIllSiteAndIsSelected(site, CommonConstants.BACTIVE);
								if (Objects.nonNull(orderSiteFeasibility) && !orderSiteFeasibility.isEmpty()) {
									orderSiteFeasibility.stream().forEach(orderSiteF -> {
										LOGGER.info("Feasibility mode for site {} is {} for {}",
												orderSiteF.getOrderIllSite(), orderSiteF.getFeasibilityMode(),
												orderSiteF.getType());
										feasibilityModes.add(orderSiteF.getFeasibilityMode());
									});
								}
							});
							/*
							 * List<String> orderSubTypeList=orderIllSites.stream().map(e ->
							 * e.getErfSfdcSubType()).collect(Collectors.toList()); //block o2c auto-trigger
							 * for INTL orders and Partner orders also if(processO2CBlockAutoTrigger(order))
							 * { LOGGER.info("not eligible  for O2C auto trigger for order code {}",
							 * order.getOrderCode());
							 * order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
							 * order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
							 * orderRepository.save(order); } else if(orderSubTypeList.isEmpty() ||
							 * orderSubTypeList.contains(null) || orderToLe.getOrderCategory()==null ||
							 * orderToLe.getOrderCategory().equalsIgnoreCase("others") ||
							 * orderSubTypeList.contains("Change Order - Upgrade") ||
							 * orderSubTypeList.contains("Change Order - Others") ||
							 * orderSubTypeList.contains("Change Order - Downgrade") ||
							 * orderSubTypeList.contains("Change Order - OTHERS") ||
							 * (Objects.nonNull(feasibilityModes) && !feasibilityModes.isEmpty() &&
							 * feasibilityModes.contains("INTL")) //block o2c auto-trigger for INTL orders
							 * || (order.getQuote() != null && order.getQuote().getNsQuote() != null &&
							 * CommonConstants.Y.equalsIgnoreCase(order.getQuote().getNsQuote()))) {
							 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
							 * orderRepository.save(order); } else {
							 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
							 * order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
							 * orderRepository.save(order); }
							 */
						}
					}
				}

				order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
				order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
				orderRepository.save(order);
				/*
				 * if(checkO2cEnabled(order) || checkO2cEnabledForOffnetWireless(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				 * orderRepository.save(order); }
				 */
				/*
				 * if(checkO2cEnabledForOffnetWireless(order)) {
				 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
				 * orderRepository.save(order); }
				 */
				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());

				// Trigger SFDC
				Order finalOrder = order;
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					try {
						omsSfdcService.processSiteDetails(quoteLe);
						Date cofSignedDate = new Date();
						DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
						if (docusignAudit != null && docusignAudit.getCustomerSignedDate() != null
								&& (docusignAudit.getStatus()
										.equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
										|| docusignAudit.getStatus()
												.equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
							cofSignedDate = docusignAudit.getCustomerSignedDate();
						}
						omsSfdcService.processUpdateOpportunity(cofSignedDate, quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
						List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
						Map<String, String> cofObjectMapper = new HashMap<>();
						CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(finalOrder.getOrderCode());
						if (cofDetails != null) {
							cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
						}

						Integer userId = finalOrder.getCreatedBy();
						String userEmail = null;
						if (userId != null) {
							Optional<User> userDetails = userRepository.findById(userId);
							if (userDetails.isPresent()) {
								userEmail = userDetails.get().getEmailId();
							}
						}

						for (OrderToLe orderToLe : finalOrder.getOrderToLes()) {
							List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
									.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES,
											quote.getId(), AttachmentTypeConstants.COF.toString());
							for (OmsAttachment omsAttachment : omsAttachmentList) {
								omsAttachment.setOrderToLe(orderToLe);
								omsAttachment.setReferenceName(CommonConstants.ORDERS);
								omsAttachment.setReferenceId(finalOrder.getId());
								omsAttachmentRepository.save(omsAttachment);
							}
							izoPcQuotePdfService.downloadCofFromStorageContainer(null, null, finalOrder.getId(),
									orderToLe.getId(), cofObjectMapper);
							break;
						}
						processOrderMailNotification(finalOrder, quoteLe, cofObjectMapper, userEmail);
						quoteDelegate.forEach(quoteDelegation -> {
							quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
							quoteDelegationRepository.save(quoteDelegation);
						});
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				}
			}
			quote.getQuoteToLes().stream().forEach(quoteLe -> {

				if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					if (quoteLe.getStage().equals(OrderStagingConstants.ORDER_CONFIRMED.getStage())) {
						quoteLe.setStage(MACDConstants.TERMINATION_REQUEST_CREATED);
						quoteToLeRepository.save(quoteLe);
					}
				} else if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& !quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
					if (quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				} else if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
					if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				}

				List<MacdDetail> macdDetailList = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteLe.getId());
				if (macdDetailList != null && !macdDetailList.isEmpty()) {
					LOGGER.info("Setting MACD_ORDER_INITIATED for all quote to le {}", quoteLe.getId());
					macdDetailList.stream().forEach(macdDetail -> {
						macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
						macdDetailRepository.save(macdDetail);
					});

				}
			});

			Integer orderIllSiteId = findOrderIllSite(order);
			detail.setOrderIllSiteId(orderIllSiteId);

			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}
	
	private Boolean checkO2cEnabled(Order order) {
		Boolean status = false;
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			if (orderToLe.getOrderCategory() != null && (orderToLe.getOrderCategory().equals("ADD_SITE")
					|| orderToLe.getOrderCategory().equals("CHANGE_BANDWIDTH")) || "SHIFT_SITE".equalsIgnoreCase(orderToLe.getOrderCategory())) {
				status = true;
			} else {
				LOGGER.info("order category is {} for {}", orderToLe.getOrderCategory(), orderToLe.getId());
				return status;
			}
			for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
				for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {

					for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {
						List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
										"primary");
						for (OrderSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
							String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
							if (!(feasibilityMode.equals("OnnetWL") || feasibilityMode.equals("Onnet Wireline") || feasibilityMode.equals("OnnetRF") || feasibilityMode.equals("Onnet Wireless"))) {
								LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
										orderIllSite.getId());
								return false;
							} else {
								status = true;
							}
						}
						List<OrderSiteFeasibility> secOrderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
										"secondary");
						if (!secOrderSiteFeasibilities.isEmpty()) {
							LOGGER.info("The prisec have secondary for site {}", orderIllSite.getId());
							return false;
						} else {
							status = true;
						}
					}

				}

			}

		}
		return status;
	}
	
	private Boolean checkO2cEnabledForOffnetWireless(Order order) {
		Boolean status = false;
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			if (orderToLe.getOrderCategory() != null && (orderToLe.getOrderCategory().equals("ADD_SITE")
					|| orderToLe.getOrderCategory().equals("CHANGE_BANDWIDTH")) || "SHIFT_SITE".equalsIgnoreCase(orderToLe.getOrderCategory())) {
				status = true;
			} else {
				LOGGER.info("order category is {} for {}", orderToLe.getOrderCategory(), orderToLe.getId());
				return status;
			}
			for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
				for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {

					for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {
						List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
										"primary");
						for (OrderSiteFeasibility orderSiteFeasibility : orderSiteFeasibilities) {
							String feasibilityMode = orderSiteFeasibility.getFeasibilityMode();
							if (!(feasibilityMode.equals("OffnetRF") || feasibilityMode.equals("Offnet Wireless"))) {
								LOGGER.info("The feasibility Mode is {} for site {}", feasibilityMode,
										orderIllSite.getId());
								return false;
							} else {
								status = true;
							}
						}
						List<OrderSiteFeasibility> secOrderSiteFeasibilities = orderSiteFeasibilityRepository
								.findByOrderIllSiteIdAndIsSelectedAndType(orderIllSite.getId(), CommonConstants.BACTIVE,
										"secondary");
						if (!secOrderSiteFeasibilities.isEmpty()) {
							LOGGER.info("The prisec have secondary for site {}", orderIllSite.getId());
							return false;
						} else {
							status = true;
						}
					}

				}

			}
		}

		return status;
	}


	/**
	 * Method to approve manual quotes
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public QuoteDetail approvedMacdManualQuotes(Integer quoteId) throws TclCommonException {
		QuoteDetail detail = null;
		try {
			detail = new QuoteDetail();
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			Map<String,String> cofObjectMapper=new HashMap<>();
			CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quote.getQuoteCode(),
					Source.MANUAL_COF.getSourceType());
			if (cofDetail != null) {
				cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetail.getUriPath());
			}
			Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);

			if (order != null) {
				detail.setOrderId(order.getId());
			} else {
				order = constructOrder(quote, detail);
				if(Objects.nonNull(order.getOrderToLes()) && !order.getOrderToLes().isEmpty()){
					Optional<OrderToLe> orderToLeOptional=order.getOrderToLes().stream().findFirst();
					if(orderToLeOptional.isPresent()){
						OrderToLe orderToLe=orderToLeOptional.get();
						String classification = "";
						if (StringUtils.isNotBlank(orderToLe.getClassification())) {
							classification = orderToLe.getClassification();
							LOGGER.info("Classification type: {}",classification);
						}
						List<String> feasibilityModes = new ArrayList<>();
						List<OrderIllSiteToService> orderIllSites=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
						if(Objects.nonNull(orderIllSites) && !orderIllSites.isEmpty()){
							orderIllSites.stream().forEach(orderIllSiteToService -> {
								OrderIllSite site = orderIllSiteToService.getOrderIllSite();
								List<OrderSiteFeasibility> orderSiteFeasibility = orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(site,CommonConstants.BACTIVE);
								if(Objects.nonNull(orderSiteFeasibility) && !orderSiteFeasibility.isEmpty()) {
									orderSiteFeasibility.stream().forEach(orderSiteF -> {
										LOGGER.info("Feasibility mode for site {} is {} for {}",orderSiteF.getOrderIllSite(),orderSiteF.getFeasibilityMode(),orderSiteF.getType());
										feasibilityModes.add(orderSiteF.getFeasibilityMode());
									});
								}
							});
							//String orderSubType=orderIllSites.get(0).getErfSfdcSubType();
							/*
							 * //block o2c auto-trigger for INTL orders and Partner orders also
							 * if(processO2CBlockAutoTrigger(order)) {
							 * LOGGER.info("deactivating auto trigger for order code {}",
							 * order.getOrderCode());
							 * order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
							 * order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
							 * orderRepository.save(order); } else if((Objects.nonNull(orderSubType) &&
							 * !orderSubType.isEmpty() && orderSubType.contains("Parallel")) ||
							 * (Objects.nonNull(feasibilityModes) && !feasibilityModes.isEmpty() &&
							 * feasibilityModes.contains("INTL")) //block o2c auto-trigger for INTL orders
							 * || (order.getQuote() != null && order.getQuote().getNsQuote() != null &&
							 * CommonConstants.Y.equalsIgnoreCase(order.getQuote().getNsQuote()))) {
							 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
							 * orderRepository.save(order); }else{
							 * order.setOrderToCashOrder(CommonConstants.BACTIVE);
							 * order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
							 * orderRepository.save(order); }
							 */
						}
					}
				}


				/*order.setOrderToCashOrder(CommonConstants.BACTIVE);
				order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				orderRepository.save(order);*/
				/*if(checkO2cEnabled(order) || checkO2cEnabledForOffnetWireless(order)) {
					order.setOrderToCashOrder(CommonConstants.BACTIVE);
					if(Objects.nonNull(order.getOrderToLes()) && !order.getOrderToLes().isEmpty()){
						Optional<OrderToLe> orderToLeOptional=order.getOrderToLes().stream().findFirst();
						if(orderToLeOptional.isPresent()){
							OrderToLe orderToLe=orderToLeOptional.get();
							List<OrderIllSiteToService> orderIllSites=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
							if(Objects.nonNull(orderIllSites) && !orderIllSites.isEmpty()){
								String orderSubType=orderIllSites.get(0).getErfSfdcSubType();
								if(Objects.nonNull(orderSubType) && !orderSubType.isEmpty() && orderSubType.contains("Parallel")){
									order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
								}
							}
						}
					}
					orderRepository.save(order);
				}*/
				/*if(checkO2cEnabledForOffnetWireless(order)) {
					order.setOrderToCashOrder(CommonConstants.BACTIVE);
					orderRepository.save(order);
				}*/
				order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
				order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
				orderRepository.save(order);
				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {

					if (!quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
						omsSfdcService.processSiteDetails(quoteLe);
						omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
						List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
						for (QuoteDelegation quoteDelegation : quoteDelegate) {
							quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
							quoteDelegationRepository.save(quoteDelegation);
						}
					}
				}
			}

			quote.getQuoteToLes().stream().forEach(quoteLe -> {

				if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					if (quoteLe.getStage().equals(OrderStagingConstants.ORDER_CONFIRMED.getStage())) {
						quoteLe.setStage(MACDConstants.TERMINATION_REQUEST_CREATED);
						quoteToLeRepository.save(quoteLe);
					}
				} else if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& !quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
					if (quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				} else if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
					if (quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				}
				
				List<MacdDetail> macdDetailList = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteLe.getId());
				if(macdDetailList != null && !macdDetailList.isEmpty()) {
					LOGGER.info("Setting MACD_ORDER_INITIATED for all quote to le {}", quoteLe.getId());
					macdDetailList.stream().forEach(macdDetail -> {
						macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
						macdDetailRepository.save(macdDetail);
					});
				}
			});

			if (detail.isManualFeasible()) {
				cloneQuoteForNonFeasibileSite(quote);
			}

			Integer orderIllSiteId = findOrderIllSite(order);
			detail.setOrderIllSiteId(orderIllSiteId);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	private boolean processO2CBlockAutoTrigger(Order order) {
		boolean status = false;
		LOGGER.info("Entering processO2CBlockAutoTrigger for order code {}", order.getOrderCode());
		for (OrderToLe orderToLe : order.getOrderToLes()) {
			
			// block partner orders from triggering to O2C
			if (Objects.nonNull(order.getEngagementOptyId())) {
				LOGGER.info("processO2CBlockAutoTrigger order code {} is partner order", order.getOrderCode());
				return true;
			}
			// block Demo orders from auto trigger to O2C
			if (orderToLe.getIsDemo() != null && CommonConstants.BACTIVE.equals(orderToLe.getIsDemo())) {
				LOGGER.info("order code {} is demo order", order.getOrderCode());
				return true;
			}
					

			if (orderToLe.getOrderCategory() != null
					&& MACDConstants.ADD_SECONDARY.equalsIgnoreCase(orderToLe.getOrderCategory())) {
				LOGGER.info("order code {} is add secondary order", order.getOrderCode());
				return true;
			}
			for (OrderToLeProductFamily leProductFamily : orderToLe.getOrderToLeProductFamilies()) {
				for (OrderProductSolution oProductSolution : leProductFamily.getOrderProductSolutions()) {
					for (OrderIllSite orderIllSite : oProductSolution.getOrderIllSites()) {

						// block colocated LM orders from O2C
						if(orderIllSite.getIsColo() != null && CommonConstants.BACTIVE.equals(orderIllSite.getIsColo())) {
							LOGGER.info("Inside processO2CBlockAutoTrigger is Colo for ordercode {}", order.getOrderCode());
							return true;
						}

					}
				}
			}
		}
		return status;
	}

	public Set<LegalAttributeBean> setBillingParameters(Integer quoteToLeId) throws TclCommonException {
		      Set<LegalAttributeBean> legalAttributeBean = null;

		      Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(quoteToLeId);
		      /*String erfServiceInventoryParentOrderId = optionalQuoteLe.get().getErfServiceInventoryParentOrderId()
		            .toString();
		*/
		      List<String> serviceIds=macdUtils.getServiceIds(optionalQuoteLe.get());
		      String serviceId=serviceIds.stream().findFirst().get();
		      if (StringUtils.isNotBlank(serviceId)) {

		         /*SIOrderDataBean orderData = macdUtils.getSiOrderData(erfServiceInventoryParentOrderId);*/
		         SIServiceDetailDataBean siServiceDetailDataBean=macdUtils.getServiceDetail(serviceId, optionalQuoteLe.get().getQuoteCategory());
		         List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
		               .findByQuoteToLe(optionalQuoteLe.get());
		         quoteLeAttributeValues.forEach(attr -> {
		            if (BILLING_METHOD.equalsIgnoreCase(attr.getMstOmsAttribute().getName())) {
		               /*orderData.getServiceDetails().stream().forEach(siServiceDetailDataBean -> {*/
		                  if (Objects.nonNull(siServiceDetailDataBean.getBillingMethod())) {
		                     attr.setAttributeValue(siServiceDetailDataBean.getBillingMethod());
		                  }
		            /* });*/
		            }
		            if (BILLING_FREQUENCY.equalsIgnoreCase(attr.getMstOmsAttribute().getName())) {
		            /* orderData.getServiceDetails().stream().forEach(siServiceDetailDataBean -> {*/
		                  if (Objects.nonNull(siServiceDetailDataBean.getBillingFrequency())) {
		                     attr.setAttributeValue(siServiceDetailDataBean.getBillingFrequency());
		                  }
		               /*});*/
		            }
		            if (PAYMENT_TERM_VALUE.equalsIgnoreCase(attr.getMstOmsAttribute().getName())) {
		               /*orderData.getServiceDetails().stream().forEach(siServiceDetailDataBean -> {*/
		                  if (Objects.nonNull(siServiceDetailDataBean.getPaymentTerm())) {
		                     attr.setAttributeValue(siServiceDetailDataBean.getPaymentTerm());
		                  }
		               /*});*/
		            }
		            quoteLeAttributeValueRepository.save(attr);
		         });
		      }
		      return legalAttributeBean;

		   }

	

	
	
	/**
	 * 
	 * getOrderSummary - get all the quote details related to the given quote Id
	 * input
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */

	public MACDOrderSummaryResponse getOrderSummary(Integer quoteId, Integer quoteLeId, String serviceId)
			throws TclCommonException {
		MACDOrderSummaryResponse response = new MACDOrderSummaryResponse();
		Map<String, String> oldAttributesMapPrimary = new HashedMap<>();
		Map<String, String> oldAttributesMapSecondary = new HashedMap<>();

		// 1. Location Id
		// 2. Port Bandwidth - Primary
		// 3. Local Loop Bandwidth - Primary
		// 4. CPE Model - Primary
		// 5. Interface - Primary
		// 6. IP Address Arrangement for Additional IPs
		// 7. IPv4 Address Pool Size for Additional IPs
		// 8. IPv6 Address Pool Size for Additional IPs
		// 9. Port Bandwidth - Secondary
		// 10.Local Loop Bandwidth - Secondary
		// 11. CPE Model - Secondary
		// 12. Interface - Secondary
		Map<String, String> newAttributesMapPrimary = new HashedMap<>();
		Map<String, String> newAttributesMapSecondary = new HashedMap<>();
		List<MACDAttributesComparisonBean> primaryAttributesList = new ArrayList<>();
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		String changeRequests = null;
		List<MACDAttributesComparisonBean> secondaryAttributesList = new ArrayList<>();
		MACDAttributesBean attributesBean = new MACDAttributesBean();

		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(serviceId))
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);

		// Queue call to get old attribute values from service details
		String iasQueueResponse = (String) mqUtils.sendAndReceive(siServiceDetailsListQueue, serviceId);

		if (StringUtils.isNotBlank(iasQueueResponse)) {
			OrderSummaryBeanResponse siDetailedInfoResponse = (OrderSummaryBeanResponse) Utils
					.convertJsonToObject(iasQueueResponse, OrderSummaryBeanResponse.class);

			// Logic to get new attribute values from oms
			siDetailedInfoResponse.getServiceMap().entrySet().forEach(siDetailedInfoResponseIAS -> {
				siDetailedInfoResponseIAS.getValue().stream().forEach(detailedInfo -> {
					getLocationDetails(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
					getPortBandwidth(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
					getInterfaceDetails(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
					// getAdditionalIPDetails(oldAttributesMapPrimary, oldAttributesMapSecondary,
					// detailedInfo);
					if (Objects.nonNull(detailedInfo.getDemoFlag())
							&& "Yes".equalsIgnoreCase(detailedInfo.getDemoFlag())) {
						getOldContractTermForDemo(oldAttributesMapPrimary, oldAttributesMapSecondary, detailedInfo);
					}
				});
			});
		

		// QuoteBean quoteBean = getQuoteDetails(quoteId, "");
		if (quoteToLeOpt.isPresent()) {
			quoteToLeOpt.get().getQuoteToLeProductFamilies().stream().forEach(prodFamily -> {
				prodFamily.getProductSolutions().stream().forEach(prodSolution -> {
					prodSolution.getQuoteIllSites().stream()
							.filter(site -> site.getStatus().equals(CommonConstants.BACTIVE)).forEach(illSite -> {
								try {
									List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndReferenceName(illSite.getId(),QuoteConstants.IZO_PC_SITES.toString());
									getLatLongDetails(newAttributesMapPrimary, newAttributesMapSecondary, illSite);

									getPrimaryPortDetails(newAttributesMapPrimary, newAttributesMapSecondary,
											quoteProductComponentList);

									getSecondaryPortDetails(newAttributesMapPrimary, newAttributesMapSecondary,
											quoteProductComponentList);

									// quoteProductComponentList);
									getParallelBuildAndParallelRunDays(quoteProductComponentList, response);

								} catch (Exception e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
											ResponseResource.R_CODE_ERROR);
								}

							});
				});
			});

		}
		if (!newAttributesMapPrimary.equals(oldAttributesMapPrimary)) {

			//setComparisonForContractTermDemoExt(oldAttributesMapPrimary, newAttributesMapPrimary, primaryAttributesList, quoteToLeOpt);
			removeContractTermIfNotDemo(oldAttributesMapPrimary, newAttributesMapPrimary, quoteToLeOpt);

			newAttributesMapPrimary.entrySet().stream().filter(att -> !att.getKey()
					.equalsIgnoreCase(MACDConstants.LOCATION_ID)
					&& !((att.getKey().equalsIgnoreCase(FPConstants.BANDWIDTH.toString())
							)
							&& Objects.nonNull(att.getValue()) && oldAttributesMapPrimary.containsKey(att.getKey())
							&& compareNewAndOldValues(oldAttributesMapPrimary.get(att.getKey()), att.getValue())))
					.forEach(attribute -> {
						if (Objects.nonNull(attribute.getValue()) && !attribute.getValue()
								.equalsIgnoreCase(oldAttributesMapPrimary.get(attribute.getKey()))) {
							LOGGER.info("Attribute name {}, attribute value {}, old attribute value {}", attribute.getKey(),
									attribute.getValue(),oldAttributesMapPrimary.get(attribute.getKey()));
							MACDAttributesComparisonBean bean = new MACDAttributesComparisonBean();
							bean.setAttributeName(attribute.getKey());
							if ((attribute.getKey().equalsIgnoreCase(FPConstants.BANDWIDTH.toString())
									)) {
								bean.setNewAttributes(convertNewBandwidthValue(attribute.getValue()));
							} else
								bean.setNewAttributes(attribute.getValue());
							bean.setOldAttributes(oldAttributesMapPrimary.get(attribute.getKey()));
							if (Objects.nonNull(attribute.getKey())
									&& attribute.getKey().equalsIgnoreCase(MACDConstants.LAT_LONG)) {
								if(quoteToLeOpt.isPresent() && Objects.nonNull(quoteToLeOpt.get().getQuoteCategory())) {
									LOGGER.info("Location id in quote - {} is different from service inventory - {} for category {}", 
											newAttributesMapPrimary.get(MACDConstants.LOCATION_ID), 
											oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID), quoteToLeOpt.get().getQuoteCategory());
									if(MACDConstants.SHIFT_SITE_SERVICE.equals(quoteToLeOpt.get().getQuoteCategory())) {
								bean.setAttributeName(MACDConstants.LOCATION_ID);
								bean.setNewAttributes(newAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
								bean.setOldAttributes(oldAttributesMapPrimary.get(MACDConstants.LOCATION_ID));
								response.setChangeRequests(
										response.getChangeRequests() == null ? MACDConstants.SHIFT_SITE
												: response.getChangeRequests() + " + " + MACDConstants.SHIFT_SITE);
									}else {
										throw new TclCommonRuntimeException(ExceptionConstants.LOCATION_ID_MISMATCH, ResponseResource.R_CODE_ERROR);
									}
								}
							} else if (Objects.nonNull(attribute.getKey()) && (attribute.getKey()
									.equalsIgnoreCase(FPConstants.BANDWIDTH.toString()))) {
								response.setChangeRequests(
										response.getChangeRequests() == null ? MACDConstants.CHANGE_BANDWIDTH
												: response.getChangeRequests().contains(MACDConstants.CHANGE_BANDWIDTH)
														? response.getChangeRequests()
														: response.getChangeRequests() + " + "
																+ MACDConstants.CHANGE_BANDWIDTH);
							}
							primaryAttributesList.add(bean);
						}
					});
		}

		/*
		 * if (!newAttributesMapSecondary.equals(oldAttributesMapSecondary))
		 * 
		 * { removeContractTermIfNotDemo(oldAttributesMapSecondary,
		 * newAttributesMapSecondary, quoteToLeOpt);
		 * //setComparisonForContractTermDemoExt(oldAttributesMapSecondary,
		 * newAttributesMapSecondary, secondaryAttributesList, quoteToLeOpt);
		 * newAttributesMapSecondary.entrySet().stream() .filter(att ->
		 * !((att.getKey().equalsIgnoreCase(FPConstants.BANDWIDTH.toString())) &&
		 * Objects.nonNull(att.getValue()) &&
		 * oldAttributesMapSecondary.containsKey(att.getKey()) &&
		 * compareNewAndOldValues(oldAttributesMapSecondary.get(att.getKey()),
		 * att.getValue()))) .forEach(attribute -> { if
		 * (Objects.nonNull(attribute.getValue()) && !attribute.getValue()
		 * .equalsIgnoreCase(oldAttributesMapSecondary.get(attribute.getKey()))) {
		 * LOGGER.info("Attribute name {}, attribute value {}, old attribute value {}",
		 * attribute.getKey(),
		 * attribute.getValue(),oldAttributesMapSecondary.get(attribute.getKey()));
		 * MACDAttributesComparisonBean bean = new MACDAttributesComparisonBean();
		 * bean.setAttributeName(attribute.getKey()); if
		 * ((attribute.getKey().equalsIgnoreCase(FPConstants.BANDWIDTH.toString()))) {
		 * 
		 * bean.setNewAttributes(convertNewBandwidthValue(attribute.getValue())); } else
		 * bean.setNewAttributes(attribute.getValue());
		 * bean.setOldAttributes(oldAttributesMapSecondary.get(attribute.getKey())); if
		 * (Objects.nonNull(attribute.getKey()) &&
		 * attribute.getKey().equalsIgnoreCase(FPConstants.BANDWIDTH.toString())) {
		 * response.setChangeRequests( response.getChangeRequests() == null ?
		 * MACDConstants.CHANGE_BANDWIDTH :
		 * response.getChangeRequests().contains(MACDConstants.CHANGE_BANDWIDTH) ?
		 * response.getChangeRequests() : response.getChangeRequests() + " + " +
		 * MACDConstants.CHANGE_BANDWIDTH); }
		 * 
		 * //secondaryAttributesList.add(bean); } }); }
		 */

		if(MACDConstants.ADD_SECONDARY.equalsIgnoreCase(quoteToLeOpt.get().getQuoteCategory())) {
			LOGGER.info("Changing the Change request to Add secondary to primary for quote category add sec");
			response.setChangeRequests(MACDConstants.ADD_SECONDARY_TO_PRIMARY);
		}

		if(MACDConstants.DEMO_EXTENSION.equalsIgnoreCase(quoteToLeOpt.get().getQuoteCategory())) {
			LOGGER.info("Changing the Change request to Demo Extension");
			response.setChangeRequests(MACDConstants.DEMO_EXTENSION_SFDC);
		}
		
		//siDetailedInfoResponse.getServiceMap().entrySet().forEach(siDetailedInfoResponseIAS -> {
			//siDetailedInfoResponseIAS.getValue().stream().forEach(detailedInfo -> {
		Optional<SIServiceInfoBean> siServiceInfoBean =null;
		for (Entry<String, List<SIServiceInfoBean>> serviceMap : siDetailedInfoResponse.getServiceMap().entrySet()) {
			siServiceInfoBean = serviceMap.getValue().stream()
					.filter(service -> service.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
							|| service.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE))
					.findFirst();
		}

		attributesBean.setPrimaryAttributesList(primaryAttributesList);
		attributesBean.setSecondaryAttributesList(secondaryAttributesList);
		response.setQuotesAttributes(attributesBean);
		response.setServiceId(serviceId);
		//Secondary Service Id impl
		/*SIOrderDataBean sIOrderDataBean = macdUtils
				.getSiOrderData(String.valueOf(quoteToLeOpt.get().getErfServiceInventoryParentOrderId()));*/
		SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(serviceId,quoteToLeOpt.get().getQuoteCategory() );
		response.setServiceType(serviceDetail.getLinkType());
		if("PRIMARY".equalsIgnoreCase(response.getServiceType())) {
			response.setPrimaryServiceId(serviceDetail.getTpsServiceId());
			response.setSecondaryServiceId(serviceDetail.getPriSecServLink());
		}
		else if("SECONDARY".equalsIgnoreCase(response.getServiceType())) {
			response.setPrimaryServiceId(serviceDetail.getPriSecServLink());
			response.setSecondaryServiceId(serviceDetail.getTpsServiceId());
		}

		//end
		if (quoteToLeOpt.isPresent()) {
			quoteToLeOpt.get().setChangeRequestSummary(response.getChangeRequests());
			quoteToLeRepository.save(quoteToLeOpt.get());
			if (quoteToLeOpt.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.CHANGE_BANDWIDTH_SERVICE)
					|| quoteToLeOpt.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)) {
				if (siServiceInfoBean.isPresent())
					response.setSiteAddress(siServiceInfoBean.get().getSiteAddress());
			}
		}

		if (siServiceInfoBean.isPresent())
			response.setPricingsList(quoteCompare(quoteId, siServiceInfoBean.get().getSiOrderId(),
					siServiceInfoBean.get().getTpsServiceId()));
		if (quoteToLeOpt.isPresent() && (Objects.nonNull(quoteToLeOpt.get().getQuoteCategory())
				&& !quoteToLeOpt.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE))) {
			if (quoteToLeOpt.get().getStage().equals(QuoteStageConstants.GET_QUOTE.getConstantCode())) {
				quoteToLeOpt.get().setStage(QuoteStageConstants.CHANGE_ORDER.getConstantCode());
				quoteToLeRepository.save(quoteToLeOpt.get());//
			}
		}
		}
		return response;

	}

	private void removeContractTermIfNotDemo(Map<String, String> oldAttributesMapSecondary, Map<String, String> newAttributesMapSecondary, Optional<QuoteToLe> quoteToLeOpt) {
		if (!quoteToLeOpt.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)) {
			if (newAttributesMapSecondary.containsKey("Demo Contract Term")) {
				newAttributesMapSecondary.remove("Demo Contract Term");
			}
			if (oldAttributesMapSecondary.containsKey("Demo Contract Term")) {
				oldAttributesMapSecondary.remove("Demo Contract Term");
			}
		}
	}


	public MulticircuitBandwidthResponse getBandwidthUpdatedFlag(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		MulticircuitBandwidthResponse response = new MulticircuitBandwidthResponse();
		List<BandwidthResponse> list = new ArrayList<>();
		if(quoteLeId == null)
			throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		
		List<QuoteIllSiteToService> serviceIdsList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteLeId);
		if(serviceIdsList != null && !serviceIdsList.isEmpty()) {
		serviceIdsList.stream().forEach(serviceId -> {
			BandwidthResponse bandwidthResponse = new BandwidthResponse();
			bandwidthResponse.setBandwidthEdited(serviceId.getBandwidthChanged());
			bandwidthResponse.setServiceId(serviceId.getErfServiceInventoryTpsServiceId());
			list.add(bandwidthResponse);
		});
		
		}
		
		response.setBandwidthResponseList(list);
		return response;
	}

	private void setNewContractTermDemo(Map<String, String> newAttributesMapPrimary, Map<String, String> newAttributesMapSecondary, Optional<QuoteToLe> quoteToLeOpt, List<QuoteProductComponent> quoteProductComponentList) {
		quoteProductComponentList.stream().filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
				.equals(ComponentConstants.PORT_CMP.getComponentsValue())
				&& quoteProdComponent.getType().equals(FPConstants.SECONDARY.toString()))
				.findFirst().ifPresent(
				prodComp->{
					newAttributesMapSecondary.put("Demo Contract Term", quoteToLeOpt.get().getTermInMonths());
				}
		);
		newAttributesMapPrimary.put("Demo Contract Term",quoteToLeOpt.get().getTermInMonths());
	}

	private void getOldContractTermForDemo(Map<String, String> oldAttributesMapPrimary, Map<String, String> oldAttributesMapSecondary, SIServiceInfoBean detailedInfo) {
		if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_PRIMARY)
				|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE)) {

			detailedInfo.getAttributes().stream().filter(attr-> attr.getAttributeName().equalsIgnoreCase("Demo Period in days"))
					.findFirst().ifPresent( attr1->{
						String term= attr1.getAttributeValue() + "days";
						oldAttributesMapPrimary.put("Demo Contract Term",term);
					}
			);
		}
		else if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.DUAL_SECONDARY)){
			detailedInfo.getAttributes().stream().filter(attr-> attr.getAttributeName().equalsIgnoreCase("Demo Period in days"))
					.findFirst().ifPresent( attr1->{
						String term= attr1.getAttributeValue() + "days";
						oldAttributesMapSecondary.put("Demo Contract Term",term);
					}
			);
		}
	}

	/*private void setComparisonForContractTermDemoExt(Map<String, String> oldAttributesMapPrimary, Map<String, String> newAttributesMapPrimary, List<MACDAttributesComparisonBean> primaryAttributesList, Optional<QuoteToLe> quoteToLeOpt) {
		if (quoteToLeOpt.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)) {
			MACDAttributesComparisonBean bean = new MACDAttributesComparisonBean();
			bean.setAttributeName("Demo Contract Term");
			bean.setOldAttributes(oldAttributesMapPrimary.get("Demo Contract"));
			bean.setNewAttributes(newAttributesMapPrimary.get("Demo Contract"));
			primaryAttributesList.add(bean);
		}
	}*/

}
