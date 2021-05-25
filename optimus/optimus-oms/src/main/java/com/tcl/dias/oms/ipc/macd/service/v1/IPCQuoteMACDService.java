package com.tcl.dias.oms.ipc.macd.service.v1;

import static com.tcl.dias.oms.partner.constants.PartnerConstants.PARTNER;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIAssetComponentBean;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAssetComponentBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.UpdateIpcComponentBean;
import com.tcl.dias.oms.beans.IPCMACDAttributeSummaryResponse;
import com.tcl.dias.oms.beans.IPCMACDOrderSummaryResponse;
import com.tcl.dias.oms.beans.IPCMACDPricingBeanResponse;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.TotalSolutionQuote;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ipc.beans.QuoteBean;
import com.tcl.dias.oms.ipc.beans.SolutionDetail;
import com.tcl.dias.oms.ipc.constants.IPCQuoteConstants;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.dias.oms.ipc.service.v1.IPCPricingService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuotePdfService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuoteService;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.beans.PartnerOpportunityBean;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the IPCQuoteMACDService.java class. This class contains IPCMACD
 * related functionalities
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@Service
@Transactional
public class IPCQuoteMACDService extends IPCQuoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPCQuoteMACDService.class);

    @Autowired
    MACDUtils macdUtils;

    @Autowired
    MQUtils mqUtils;
    
    @Autowired
	UserInfoUtils userInfoUtils;
    
    @Autowired
	PartnerService partnerService;

    @Autowired
    MacdDetailRepository macdDetailRepository;
    
    @Autowired
	IPCQuotePdfService ipcQuotePdfService;
    
    @Autowired
	UserRepository userRepository;
    
    @Autowired
    QuoteCloudRepository quoteCloudRepository;
    
    @Autowired
    QuotePriceRepository quotePriceRepository;
    
    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;
    
    @Autowired
    ProductSolutionRepository productSolutionRepository;
    
    @Autowired
    MstProductOfferingRepository mstProductOfferingRepository;
    
    @Autowired
    QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
    QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
    
    @Autowired
	IPCPricingService ipcPricingService;
    
    @Autowired
	OmsAttachmentRepository omsAttachmentRepository;
    
    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;
    
    @Autowired
	IPCOrderService ipcOrderService;
    
    @Autowired
    IPCQuoteService ipcQuoteService;
    
    @Value("${rabbitmq.ipc.si.related.details.queue}")
	String ipcSIRelatedDetailsQueue;

	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;

    public MacdQuoteResponse handleMacdRequestToCreateQuote(MacdQuoteRequest macdRequest) throws TclCommonException {
        try {
            validateMacdQuoteRequest(macdRequest);
            return createMacdQuote(macdRequest);
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
    }

    private void validateMacdQuoteRequest(MacdQuoteRequest macdRequest) throws TclCommonException {
        String[] quoteTypes = {MACDConstants.ADD_CLOUDVM_SERVICE,MACDConstants.CONNECTIVITY_UPGRADE_SERVICE,MACDConstants.ADDITIONAL_SERVICE_UPGRADE,MACDConstants.REQUEST_FOR_TERMINATION_SERVICE,MACDConstants.UPGRADE_VM_SERVICE,MACDConstants.DELETE_VM_SERVICE};
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
    }

	public MacdQuoteResponse createMacdQuote(MacdQuoteRequest request) throws TclCommonException, ParseException {
		MacdQuoteResponse macdResponse = new MacdQuoteResponse();
		QuoteResponse response = null;
		Integer erfCustomerIdInt = null;
		Integer erfCustomerLeIdInt = null;
		String partnerCuid = null;
		String endCustomerName = null;
		User user = getUserId(Utils.getSource());
		if (Objects.nonNull(user)) {
			SIOrderDataBean orderData = macdUtils
					.getSiOrderData(String.valueOf(request.getServiceDetails().get(0).getDownstreamOrderId()));
			LOGGER.info("MDC Filter token value in before Queue call createMacdQuote {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String serviceIds = request.getServiceDetails().stream().map(i -> i.getServiceId().toString().trim())
					.collect(Collectors.joining(","));
			LOGGER.info("service Ids passed {}", serviceIds);
			String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceIds);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
			List<SIServiceDetailsBean> serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
			response = new QuoteResponse();
			if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
				if (serviceDetailsList.stream().findFirst().isPresent()) {
					erfCustomerIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerId();
					partnerCuid = serviceDetailsList.stream().findFirst().get().getPartnerCuid();
					endCustomerName = serviceDetailsList.stream().findFirst().get().getErfCustomerName();
				}
			}
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				PartnerOpportunityBean partnerOpportunityBean = partnerService.createPartnerOpportunityBeanForMACD(
						request, serviceDetailsList, request.getQuoteRequest().getProductName());
				erfCustomerIdInt = partnerOpportunityBean.getCustomerId();
				erfCustomerLeIdInt = partnerOpportunityBean.getCustomerLeId();
				LOGGER.info("erfCustomerIdInt :: {} , erfCustomerLeIdInt :: {}", erfCustomerIdInt, erfCustomerLeIdInt);
			}
			QuoteToLe quoteTole = processQuote(request.getRequestType(), request.getQuoteRequest(), erfCustomerIdInt,
					user);
			persistQuoteLeAttributes(user, quoteTole, request.getQuoteRequest());
			if (quoteTole != null) {
				createMACDSpecificQuoteToLe(quoteTole, orderData, request.getRequestType(),
						request.getServiceDetails().get(0).getServiceDetailId(),
						request.getServiceDetails().get(0).getServiceId(), orderData.getTpsSfdcId());
				quoteToLeRepository.save(quoteTole);
				LOGGER.info("QuoteToLe term" + quoteTole.getTermInMonths());

				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteTole.getId());
				if (Objects.isNull(macdDetail)) {
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
					omsSfdcService.processCreatePartnerOpty(quoteTole, request.getQuoteRequest().getProductName(),
							partnerCuid, endCustomerName);
				} else if (request.getQuoteRequest().getQuoteId() == null
						&& Objects.isNull(request.getQuoteRequest().getEngagementOptyId())) {
					// Triggering Sfdc Creation
					omsSfdcService.processCreateOpty(quoteTole, request.getQuoteRequest().getProductName());
				}
				ipcPricingService.processPricingRequest(quoteTole.getQuote().getId(), quoteTole.getId(),
						request.getServiceDetails().get(0).getServiceId());
				if(MACDConstants.DELETE_VM_SERVICE.equals(quoteTole.getQuoteCategory())) {
					ipcPricingService.processEarlyTerminationChargesForDeleteVM(quoteTole, serviceDetailsList, request.getCancellationDate());
				}
				if (request.getRequestType().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
					customerLeAttributeRequestBean.setCustomerLeId(orderData.getErfCustLeId());
					customerLeAttributeRequestBean.setProductName("IPC");
					LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct,
							Utils.convertObjectToJson(customerLeAttributeRequestBean));
					if (StringUtils.isNotEmpty(customerLeAttributes)) {
						updateBillingInfoForSfdc((CustomerLeDetailsBean) Utils.convertJsonToObject(customerLeAttributes,
								CustomerLeDetailsBean.class), quoteTole);
					}
				}
			}
		}
		macdResponse.setQuoteResponse(response);
		return macdResponse;
	}

    private QuoteToLe createMACDSpecificQuoteToLe(QuoteToLe quoteTole, SIOrderDataBean orderData, String quoteCategory,
                                                  Integer serviceDetailId, String serviceId, String parentSfdcId) {
        quoteTole.setQuoteCategory(quoteCategory);
        quoteTole.setQuoteType(MACDConstants.MACD_QUOTE_TYPE);
        quoteTole.setSourceSystem(MACDConstants.SOURCE_SYSTEM);
        quoteTole.setErfServiceInventoryParentOrderId(orderData.getId());
        quoteTole.setErfCusCustomerLegalEntityId(orderData.getErfCustLeId());
        quoteTole.setErfCusSpLegalEntityId(orderData.getErfCustSpLeId());
        quoteTole.setErfServiceInventoryServiceDetailId(serviceDetailId);
        quoteTole.setErfServiceInventoryTpsServiceId(serviceId);
        quoteTole.setStage(QuoteStageConstants.MODIFY.getConstantCode());
        quoteTole.setCurrencyId(orderData.getErfCustCurrencyId());
        SIServiceDetailDataBean serviceDetail=macdUtils.getSiServiceDetailBeanBasedOnServiceId(orderData,serviceId);
        if(Objects.nonNull(serviceDetail)) {
            LOGGER.info("Term In Months" + serviceDetail.getContractTerm());
            if (Objects.nonNull(serviceDetail.getContractTerm())) {
                quoteTole.setTermInMonths(serviceDetail.getContractTerm().intValue() + " months");
            }
        }
        if (Objects.nonNull(parentSfdcId)) {
            quoteTole.setTpsSfdcParentOptyId(Integer.parseInt(parentSfdcId));
        }
        return quoteTole;
    }

    public void createMacdOrderDetail(QuoteToLe quoteToLe, String cancellationDate, String cancellationReason) throws ParseException {
        MacdDetail macdDetail = new MacdDetail();
        if (Objects.nonNull(quoteToLe)) {
            macdDetail.setQuoteToLeId(quoteToLe.getId());

            if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
                if (Objects.nonNull(cancellationDate)) {
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(cancellationDate);
                    macdDetail.setCancellationDate(date);
                }
                macdDetail.setCancellationReason(cancellationReason);
            }
            macdDetail.setTpsServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
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
        }
    }
    
    /**
	 * @return ResponseResource
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 */
	@Transactional
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
				DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
				if (docusignAudit != null) {
					throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
				}
				order = constructOrder(quote, detail);
				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());
				updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
				// Trigger SFDC
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					try {
						if (Objects.nonNull(quoteLe.getQuoteCategory()) && !quoteLe.getQuoteCategory()
								.equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {

							omsSfdcService.processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
									SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

							Boolean nat = !(request.getCheckList() == null
									|| request.getCheckList().equalsIgnoreCase(CommonConstants.NO));
							Map<String, String> cofObjectMapper = new HashMap<>();
							ipcQuotePdfService.processCofPdf(quote.getId(), null, nat, true, quoteLe.getId(),
									cofObjectMapper);
							CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
							if (cofDetails != null) {
								cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
							}
							// Trigger orderMail
							User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
							String userEmail = null;
							if (userRepo != null) {
								userEmail = userRepo.getEmailId();
							}

							Optional<OrderToLe> orderToLe = order.getOrderToLes().stream().findFirst();
							if (orderToLe.isPresent()) {
								try {
									ipcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
											orderToLe.get().getId(), cofObjectMapper);
								} catch (TclCommonException e) {
									throw new TclCommonRuntimeException(e);
								}
							}
							// Trigger orderMail
							processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
							List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
							quoteDelegate.stream().forEach(quoteDelegation -> {
								quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
								quoteDelegationRepository.save(quoteDelegation);
							});
						}
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(e);
					}
				}
			}

			quote.getQuoteToLes().forEach(quoteLe -> {
				if (Objects.nonNull(quoteLe.getQuoteCategory())
						&& quoteLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					if (quoteLe.getStage().equals(OrderStagingConstants.ORDER_CONFIRMED.getStage())) {
						quoteLe.setStage(MACDConstants.TERMINATION_REQUEST_CREATED);
						quoteToLeRepository.save(quoteLe);
					}
				} else {
					if (Objects.nonNull(quoteLe.getQuoteCategory())
							&& quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}
				}
			});
			Optional<QuoteToLe> quoteToLeOpt = order.getQuote().getQuoteToLes().stream().findFirst();
			if (quoteToLeOpt.isPresent()) {
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLeOpt.get().getId());
				macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
				macdDetailRepository.save(macdDetail);
			}

			for (OrderToLe orderToLe : order.getOrderToLes()) {
				LOGGER.info("Order Type: {}, Order Category: {}", orderToLe.getOrderType(),
						orderToLe.getOrderCategory());
				if (MACDConstants.MACD_QUOTE_TYPE.equals(orderToLe.getOrderType())
						&& MACDConstants.DELETE_VM.equals(orderToLe.getOrderCategory())) {
					processAttachmentsFromQuote(quote.getId(), orderToLe);
					ipcOrderService.launchCloud(order.getId(), orderToLe.getId(), false);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}
	
	private void saveMacdOrderTypeAndOrderCategory(Order order, Quote quote) {
		order.getOrderToLes().stream().forEach(orderToLe -> {
			orderToLe.setOrderType(quote.getQuoteToLes().stream().findFirst().get().getQuoteType());
			orderToLe.setOrderCategory(quote.getQuoteToLes().stream().findFirst().get().getQuoteCategory());
			orderToLe.setErfServiceInventoryParentOrderId(
					quote.getQuoteToLes().stream().findFirst().get().getErfServiceInventoryParentOrderId());
			orderToLe.setTpsSfdcParentOptyId(quote.getQuoteToLes().stream().findFirst().get().getTpsSfdcParentOptyId());
			orderToLe.setSourceSystem(quote.getQuoteToLes().stream().findFirst().get().getSourceSystem());
			orderToLeRepository.save(orderToLe);
		});
	}	
	
	public QuoteBean getMACDQuoteDetails(Integer quoteId) throws TclCommonException {
		try {	
			final QuoteBean response = getQuoteDetails(quoteId);
			Quote quote = getQuote(quoteId);
			if (Objects.nonNull(quote) && Objects.nonNull(quote.getQuoteToLes())){
				quote.getQuoteToLes().stream().findFirst().ifPresent(quoteToLe ->{
					if (Objects.nonNull(quoteToLe)
							&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
						response.setQuoteCategory(quoteToLe.getQuoteCategory());
						response.setQuoteType(quoteToLe.getQuoteType());
						response.setTermInMonths(quoteToLe.getTermInMonths());
						response.setServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
						response.setServiceOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
						if(Objects.nonNull(response.getServiceId())){
							response.setIsMacdInitiated(checkMacdInitiatedBasedOnServiceId(quoteToLe.getErfServiceInventoryTpsServiceId(),MACDConstants.IPC_MACD_SERVICE_LIST,MACDConstants.MACD_ORDER_INITIATED));
						}
					}
				});
			}
			return response;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	
	
	public boolean checkMacdInitiatedBasedOnQuoteToLe(QuoteToLe quoteToLe){
		MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
		return macdDetail.getStage().equals(MACDConstants.MACD_ORDER_INITIATED);
	}
	
	public boolean checkMacdInitiatedBasedOnServiceId(String serviceId, List<String> orderCategory,String stage ){
		MacdDetail macdDetail = macdDetailRepository.findByTpsServiceIdAndOrderCategoryInAndStage(serviceId,orderCategory,stage);
		return Objects.nonNull(macdDetail);
	}

	public IPCMACDOrderSummaryResponse getOrderSummary(Integer quoteId, Integer quoteLeId, String serviceId) throws TclCommonException{
		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(serviceId))
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		IPCMACDOrderSummaryResponse ipcMACDOrderSummaryResponse = new IPCMACDOrderSummaryResponse();
		List<IPCMACDAttributeSummaryResponse> ipcMacdAttrList = new ArrayList<>();
		TotalSolutionQuote totalSolutionQuote = new TotalSolutionQuote();
		SIServiceAssetComponentBean[] siServiceAssetBeanArray = null;
		List<SIServiceAssetComponentBean> previousServiceAssetList = new ArrayList<>();

		// Queue call to get old attribute values from service details
		String ipcQueueResponse = (String) mqUtils.sendAndReceive(ipcSIRelatedDetailsQueue, serviceId);
		LOGGER.info("IPC SI Queue Response::"+ipcQueueResponse);
		if (StringUtils.isNotBlank(ipcQueueResponse)) {
			siServiceAssetBeanArray = (SIServiceAssetComponentBean[]) Utils.convertJsonToObject(ipcQueueResponse,
					SIServiceAssetComponentBean[].class);
			previousServiceAssetList = Arrays.asList(siServiceAssetBeanArray);
		}
		
		Optional<QuoteToLe> quoteToLe=quoteToLeRepository.findById(quoteLeId);
		if(quoteToLe.isPresent()){
			if(quoteToLe.get().getQuoteCategory().equals("CONNECTIVITY_UPGRADE")){
				LOGGER.info("CONNECTIVITY UPGRADE");
				//Get current asset details from quote
				List<String> resourceNameList = ImmutableList.of("IPC addon","Access");
				List<QuoteCloud> quoteCloudList = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameInAndStatus(quoteLeId,resourceNameList,CommonConstants.BACTIVE);
				Optional<QuoteToLe> quoteToLeOpt=quoteToLeRepository.findById(quoteLeId);
				totalSolutionQuote.setName(MACDConstants.SOLUTION_QUOTE);
				totalSolutionQuote.setCurrencyType(quoteToLeOpt.get().getCurrencyCode());
				int termInMonths=Integer.valueOf(quoteToLeOpt.get().getTermInMonths().replaceAll("([a-zA-Z])", "").trim());
				LOGGER.info("TermInMonths::"+termInMonths);
				if(Objects.nonNull(quoteCloudList) && !quoteCloudList.isEmpty()){
					Map<String,Integer> assetIDTypeMap = new HashMap<>();
					Map<String,SIServiceAssetComponentBean> assetTypeComponentBeanMap= new HashMap<>();
					List<SIServiceAssetComponentBean> currentServiceAssetList= new ArrayList<>();
					// Get QuoteCloud details
					getQuoteCloudDetails(quoteCloudList,assetIDTypeMap,assetTypeComponentBeanMap,currentServiceAssetList);
					//Get current access details
					getAccessDetails(assetIDTypeMap,assetTypeComponentBeanMap);
					//Get addOn details
					getAddOnDetails(assetIDTypeMap,assetTypeComponentBeanMap,false);
					LOGGER.info("IPC Current Queue Response::"+Utils.convertObjectToJson(currentServiceAssetList));
					boolean isCurrentAdditionalIpExists = false;
					boolean isPrevAdditionalIpExists=false;
					isCurrentAdditionalIpExists=checkCategoryDetailsExistsInCurrentAsset("Additional Ip",currentServiceAssetList,isCurrentAdditionalIpExists);
					isPrevAdditionalIpExists=checkCategoryDetailsExistsInPreviousAsset("Additional Ip",previousServiceAssetList,isPrevAdditionalIpExists);
					//Get old and new quote price for categories
					getQuotePrices(currentServiceAssetList,previousServiceAssetList,totalSolutionQuote,ipcMacdAttrList,termInMonths);
					// Get currently Additional Ip price if it exists and previous Additional Ip doesn't exists
					getCategoryPriceForCurrentAsset(isCurrentAdditionalIpExists,isPrevAdditionalIpExists,currentServiceAssetList,totalSolutionQuote,"Additional Ip",ipcMacdAttrList,termInMonths);
				}
				ipcMACDOrderSummaryResponse.setIpcMACDAttributeSummaryList(ipcMacdAttrList);
				ipcMACDOrderSummaryResponse.setTotalSolQuote(totalSolutionQuote);
				
			}else if(quoteToLe.get().getQuoteCategory().equals("ADDITIONAL_SERVICE_UPGRADE")){
				LOGGER.info("ADD ON SERVICE");
				//Get current asset details from quote
				List<String> resourceNameList = ImmutableList.of("IPC addon");
				List<QuoteCloud> quoteCloudList = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameInAndStatus(quoteLeId,resourceNameList,CommonConstants.BACTIVE);
				Optional<QuoteToLe> quoteToLeOpt=quoteToLeRepository.findById(quoteLeId);
				totalSolutionQuote.setName(MACDConstants.SOLUTION_QUOTE);
				totalSolutionQuote.setCurrencyType(quoteToLeOpt.get().getCurrencyCode());
				int termInMonths=Integer.valueOf(quoteToLeOpt.get().getTermInMonths().replaceAll("([a-zA-Z])", "").trim());
				LOGGER.info("TermInMonths::"+termInMonths);
				if(Objects.nonNull(quoteCloudList) && !quoteCloudList.isEmpty()){
					Map<String,Integer> assetIDTypeMap = new HashMap<>();
					Map<String,SIServiceAssetComponentBean> assetTypeComponentBeanMap= new HashMap<>();
					List<SIServiceAssetComponentBean> currentServiceAssetList= new ArrayList<>();
					// Get QuoteCloud details
					getQuoteCloudDetails(quoteCloudList,assetIDTypeMap,assetTypeComponentBeanMap,currentServiceAssetList);
					//Get addOn details
					getAddOnDetails(assetIDTypeMap,assetTypeComponentBeanMap,true);
					LOGGER.info("IPC Current Queue Response::"+Utils.convertObjectToJson(currentServiceAssetList));
					boolean isCurrentBackupExists = false;
					boolean isPrevBackupExists=false;
					boolean isCurrentVPNExists=false;
					boolean isPrevVPNExists=false;
					boolean isCurrentManagedExists=false;
					boolean isPrevManagedExists=false;
					
					isCurrentBackupExists=checkCategoryDetailsExistsInCurrentAsset("Backup",currentServiceAssetList,isCurrentBackupExists);
					isPrevBackupExists=checkCategoryDetailsExistsInPreviousAsset("Backup",previousServiceAssetList,isPrevBackupExists);
					
					isCurrentVPNExists=checkCategoryDetailsExistsInCurrentAsset("VPN Connection",currentServiceAssetList,isCurrentVPNExists);
					isPrevVPNExists=checkCategoryDetailsExistsInPreviousAsset("VPN Connection",previousServiceAssetList,isPrevVPNExists);
					
					isCurrentManagedExists=checkCategoryDetailsExistsInCurrentAsset("managed",currentServiceAssetList,isCurrentManagedExists);
					isPrevManagedExists=checkCategoryDetailsExistsInPreviousAsset("managed",previousServiceAssetList,isPrevManagedExists);
					//Get old and new quote price for categories
					getQuotePrices(currentServiceAssetList,previousServiceAssetList,totalSolutionQuote,ipcMacdAttrList,termInMonths);
					// Get currently Backup price if it exists and previous Backup doesn't exists
					getCategoryPriceForCurrentAsset(isCurrentBackupExists,isPrevBackupExists,currentServiceAssetList,totalSolutionQuote,"Backup",ipcMacdAttrList,termInMonths);
					getCategoryPriceForCurrentAsset(isCurrentVPNExists,isPrevVPNExists,currentServiceAssetList,totalSolutionQuote,"VPN Connection",ipcMacdAttrList,termInMonths);
					getCategoryPriceForCurrentAsset(isCurrentManagedExists,isPrevManagedExists,currentServiceAssetList,totalSolutionQuote,"managed",ipcMacdAttrList,termInMonths);
				}
				ipcMACDOrderSummaryResponse.setIpcMACDAttributeSummaryList(ipcMacdAttrList);
				ipcMACDOrderSummaryResponse.setTotalSolQuote(totalSolutionQuote);
			}
		}
		return ipcMACDOrderSummaryResponse;
	}
	
	private void getQuoteCloudDetails(List<QuoteCloud> quoteCloudList, Map<String, Integer> assetIDTypeMap,
			Map<String, SIServiceAssetComponentBean> assetTypeComponentBeanMap,List<SIServiceAssetComponentBean> currentServiceAssetList) {
		LOGGER.info("IPCQuoteMACDService.getQuoteCloudDetails method invoked");
		quoteCloudList.stream().forEach(quoteCloud->{
			SIServiceAssetComponentBean siServiceAssetComponentBean = new SIServiceAssetComponentBean();
			siServiceAssetComponentBean.setType(quoteCloud.getResourceDisplayName());
			siServiceAssetComponentBean.setArc(quoteCloud.getArc());
			siServiceAssetComponentBean.setMrc(quoteCloud.getMrc());
			siServiceAssetComponentBean.setNrc(quoteCloud.getNrc());
			siServiceAssetComponentBean.setAssetId(quoteCloud.getId());
			assetIDTypeMap.put(siServiceAssetComponentBean.getType(), siServiceAssetComponentBean.getAssetId());
			assetTypeComponentBeanMap.put(quoteCloud.getResourceDisplayName(),siServiceAssetComponentBean);
			currentServiceAssetList.add(siServiceAssetComponentBean);
		});
		LOGGER.info("IPCQuoteMACDService.getQuoteCloudDetails method ends");
	}

	private void getQuotePrices(List<SIServiceAssetComponentBean> currentServiceAssetList,
			List<SIServiceAssetComponentBean> previousServiceAssetList, TotalSolutionQuote totalSolutionQuote,
			List<IPCMACDAttributeSummaryResponse> ipcMacdAttrList,int termInMonths) {
		LOGGER.info("IPCQuoteMACDService.getQuotePrices method invoked");
		currentServiceAssetList.stream().forEach(currentAsset->{
			previousServiceAssetList.stream().forEach(previousAsset ->{
				//Checks if type and name are same
				if(currentAsset.getType().equals(previousAsset.getName())){
					getQuoteDetailsBasedOnCategory(currentAsset,previousAsset,totalSolutionQuote,ipcMacdAttrList,termInMonths);
				}
			});
		});
		LOGGER.info("IPCQuoteMACDService.getQuotePrices method ends");
		
	}

	private void getQuoteDetailsBasedOnCategory(SIServiceAssetComponentBean currentAsset,
			SIServiceAssetComponentBean previousAsset, TotalSolutionQuote totalSolutionQuote,
			List<IPCMACDAttributeSummaryResponse> ipcMacdAttrList,int termInMonths) {
		LOGGER.info("IPCQuoteMACDService.getQuoteDetailsBasedOnCategory method invoked");
		currentAsset.getSiAssetComponentList().stream().forEach(currentAssetComponentBean ->{
			previousAsset.getSiAssetComponentList().stream().forEach(prevAssetComponentBean ->{
				//Checks if category of prev and current are same
				if(currentAssetComponentBean.getCategory().equals(prevAssetComponentBean.getCategory())){
					IPCMACDAttributeSummaryResponse ipcMACDAttrSummaryResponse = new IPCMACDAttributeSummaryResponse();
					ipcMACDAttrSummaryResponse.setType(currentAssetComponentBean.getCategory());
					ipcMACDAttrSummaryResponse.setOldAttrName((prevAssetComponentBean.getName().equals("Data Transfer") 
							|| prevAssetComponentBean.getCategory().equals("Backup")
							|| prevAssetComponentBean.getCategory().equals("VPN Connection")
							|| prevAssetComponentBean.getCategory().equals("managed"))?prevAssetComponentBean.getName():prevAssetComponentBean.getCategory());
					ipcMACDAttrSummaryResponse.setOldAttrVal(prevAssetComponentBean.getValue());
					ipcMACDAttrSummaryResponse.setNewAttrName((currentAssetComponentBean.getName().equals("Data Transfer") 
							|| currentAssetComponentBean.getCategory().equals("Backup")
							|| currentAssetComponentBean.getCategory().equals("VPN Connection") 
							|| currentAssetComponentBean.getCategory().equals("managed"))?currentAssetComponentBean.getName():currentAssetComponentBean.getCategory());
					ipcMACDAttrSummaryResponse.setNewAttrVal(currentAssetComponentBean.getValue());
					LOGGER.info("Old Attr::"+ipcMACDAttrSummaryResponse.getOldAttrName());
					LOGGER.info("New Attr::"+ipcMACDAttrSummaryResponse.getNewAttrName());
					IPCMACDPricingBeanResponse ipcMACDPricingBeanResponse = new IPCMACDPricingBeanResponse();
					ipcMACDPricingBeanResponse.setOldArc(prevAssetComponentBean.getArc() != null ? prevAssetComponentBean.getArc():0);
					ipcMACDPricingBeanResponse.setOldMrc(prevAssetComponentBean.getMrc() != null ? prevAssetComponentBean.getMrc():0);
					ipcMACDPricingBeanResponse.setOldNrc(prevAssetComponentBean.getNrc() != null ? prevAssetComponentBean.getNrc():0);
					ipcMACDPricingBeanResponse.setNewArc(currentAssetComponentBean.getArc() != null ? currentAssetComponentBean.getArc():0);
					ipcMACDPricingBeanResponse.setNewMrc(currentAssetComponentBean.getMrc() != null ? currentAssetComponentBean.getMrc():0);
					ipcMACDPricingBeanResponse.setNewNrc(currentAssetComponentBean.getNrc() != null ? currentAssetComponentBean.getNrc():0);
					ipcMACDAttrSummaryResponse.setIpcMacdPricingBeanResponse(ipcMACDPricingBeanResponse);
					ipcMacdAttrList.add(ipcMACDAttrSummaryResponse);
					totalSolutionQuote.setNewQuote((totalSolutionQuote.getNewQuote()!=null?totalSolutionQuote.getNewQuote():0)+((termInMonths*ipcMACDPricingBeanResponse.getNewMrc())+ipcMACDPricingBeanResponse.getNewNrc()));
					totalSolutionQuote.setOldQuote((totalSolutionQuote.getOldQuote()!=null?totalSolutionQuote.getOldQuote():0)+((termInMonths*ipcMACDPricingBeanResponse.getOldMrc())+ipcMACDPricingBeanResponse.getOldNrc()));
					LOGGER.info("New Quote::"+totalSolutionQuote.getNewQuote());
					LOGGER.info("Old Quote::"+totalSolutionQuote.getOldQuote());
				}
			});
		});
		LOGGER.info("IPCQuoteMACDService.getQuoteDetailsBasedOnCategory method ends");
	}

	private void getCategoryPriceForCurrentAsset(boolean isCurrentCategoryExists, boolean isPrevCategoryExists,
			List<SIServiceAssetComponentBean> currentServiceAssetList, TotalSolutionQuote totalSolutionQuote,
			String category,List<IPCMACDAttributeSummaryResponse> ipcMacdAttrList,int termInMonths) {
		LOGGER.info("IPCQuoteMACDService.getCategoryPriceForCurrentAsset method invoked");
		if(isCurrentCategoryExists && !isPrevCategoryExists){
			currentServiceAssetList.stream().forEach(currentAsset ->{
				currentAsset.getSiAssetComponentList().stream().forEach(currentAssetComponentBean ->{
					if(currentAssetComponentBean.getCategory().equals(category)){
						IPCMACDAttributeSummaryResponse ipcMACDAttrSummaryResponse = new IPCMACDAttributeSummaryResponse();
						ipcMACDAttrSummaryResponse.setType(currentAssetComponentBean.getCategory());
						ipcMACDAttrSummaryResponse.setNewAttrName((currentAssetComponentBean.getName().equals("Data Transfer") 
								|| currentAssetComponentBean.getCategory().equals("Backup")
								|| currentAssetComponentBean.getCategory().equals("VPN Connection")
								|| currentAssetComponentBean.getCategory().equals("managed"))?
								currentAssetComponentBean.getName():currentAssetComponentBean.getCategory());
						ipcMACDAttrSummaryResponse.setNewAttrVal(currentAssetComponentBean.getValue());
						LOGGER.info("New Attr::"+ipcMACDAttrSummaryResponse.getNewAttrName());
						IPCMACDPricingBeanResponse ipcMACDPricingBeanResponse = new IPCMACDPricingBeanResponse();
						ipcMACDPricingBeanResponse.setNewArc(currentAssetComponentBean.getArc() != null ? currentAssetComponentBean.getArc():0);
						ipcMACDPricingBeanResponse.setNewMrc(currentAssetComponentBean.getMrc() != null ? currentAssetComponentBean.getMrc():0);
						ipcMACDPricingBeanResponse.setNewNrc(currentAssetComponentBean.getNrc() != null ? currentAssetComponentBean.getNrc():0);
						ipcMACDAttrSummaryResponse.setIpcMacdPricingBeanResponse(ipcMACDPricingBeanResponse);
						ipcMacdAttrList.add(ipcMACDAttrSummaryResponse);
						totalSolutionQuote.setNewQuote((totalSolutionQuote.getNewQuote()!=null?totalSolutionQuote.getNewQuote():0)+((termInMonths*ipcMACDPricingBeanResponse.getNewMrc())+ipcMACDPricingBeanResponse.getNewNrc()));
						totalSolutionQuote.setOldQuote((totalSolutionQuote.getOldQuote()!=null?totalSolutionQuote.getOldQuote():0)+((termInMonths*ipcMACDPricingBeanResponse.getOldMrc())+ipcMACDPricingBeanResponse.getOldNrc()));
						LOGGER.info("New Quote::"+totalSolutionQuote.getNewQuote());
						LOGGER.info("Old Quote::"+totalSolutionQuote.getOldQuote());
					}
				});
			});
		}
		LOGGER.info("IPCQuoteMACDService.getCategoryPriceForCurrentAsset method ends");
	}

	private boolean checkCategoryDetailsExistsInPreviousAsset(String category,
			List<SIServiceAssetComponentBean> previousServiceAssetList, boolean isPrevCategoryExists) {
		for(SIServiceAssetComponentBean previousAsset:previousServiceAssetList){
			for(SIAssetComponentBean prevAssetComponentBean:previousAsset.getSiAssetComponentList()){
				if(prevAssetComponentBean.getCategory().equals(category)){
					isPrevCategoryExists=true;
				}
			}
		}
		return isPrevCategoryExists;
	}

	private boolean checkCategoryDetailsExistsInCurrentAsset(String category,
			List<SIServiceAssetComponentBean> currentServiceAssetList, boolean isCurrentCategoryExists) {
		for(SIServiceAssetComponentBean currentAsset:currentServiceAssetList){
			for(SIAssetComponentBean currentAssetComponentBean:currentAsset.getSiAssetComponentList()){
				if(currentAssetComponentBean.getCategory().equals(category)){
					isCurrentCategoryExists=true;
				}
			}
		}
		return isCurrentCategoryExists;
	}

	private void getAddOnDetails(Map<String, Integer> assetIDTypeMap,Map<String, SIServiceAssetComponentBean> assetTypeComponentBeanMap, boolean addOn) {
		LOGGER.info("IPCQuoteMACDService.getAddOnDetails method invoked");
		Integer quoteCloudId=assetIDTypeMap.get("IPC addon");
		List<String> names=null;
		if(addOn){
			names= ImmutableList.of("Backup","managed","VPN Connection");
		}else{
			names= ImmutableList.of("Additional Ip","VDOM");
		}
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameIn(names);
		List<QuoteProductComponent> quoteProductComponentList=quoteProductComponentRepository.findByReferenceIdAndMstProductComponentIn(quoteCloudId, mstProductComponents);
		List<SIAssetComponentBean> addOnList= new ArrayList<>();
		if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()){
			Map<String,SIAssetComponentBean> siAssetComponentMap= new HashMap<>();
			Map<String,SIAssetComponentBean> siAssetComponentTypeMap= new HashMap<>();
			List<String> quoteProductComponentIds= new ArrayList<>();
			quoteProductComponentList.stream().forEach(quoteProductComponent ->{
				SIAssetComponentBean siAssetComponentBean = new SIAssetComponentBean();
				siAssetComponentBean.setCategory(quoteProductComponent.getMstProductComponent().getName());
				quoteProductComponent.getQuoteProductComponentsAttributeValues().stream().forEach(quoteProductComponentAttr->{
					siAssetComponentBean.setName(siAssetComponentBean.getName() !=null? siAssetComponentBean.getName().concat(",").concat(quoteProductComponentAttr.getProductAttributeMaster().getName()):quoteProductComponentAttr.getProductAttributeMaster().getName());
					if(quoteProductComponentAttr.getAttributeValues() != null) {
						siAssetComponentBean.setValue(siAssetComponentBean.getValue()!=null?siAssetComponentBean.getValue().concat(",").concat(quoteProductComponentAttr.getAttributeValues()):quoteProductComponentAttr.getAttributeValues());
					}
				});
				siAssetComponentMap.put(String.valueOf(quoteProductComponent.getId()), siAssetComponentBean);
				siAssetComponentTypeMap.put(quoteProductComponent.getMstProductComponent().getName(), siAssetComponentBean);
				quoteProductComponentIds.add(String.valueOf(quoteProductComponent.getId()));
			});
			List<QuotePrice> quotePriceList=quotePriceRepository.findByReferenceNameAndReferenceIdIn("COMPONENTS",quoteProductComponentIds);
			if(Objects.nonNull(quotePriceList) && !quotePriceList.isEmpty()){
				quotePriceList.stream().forEach(quotePrice ->{
					SIAssetComponentBean siAssetComponentBean =siAssetComponentMap.get(quotePrice.getReferenceId());
					siAssetComponentBean.setArc(quotePrice.getEffectiveArc());
					siAssetComponentBean.setMrc(quotePrice.getEffectiveMrc());
					siAssetComponentBean.setNrc(quotePrice.getEffectiveNrc());
					addOnList.add(siAssetComponentBean);
				
				});
			}
			
		}
		//Set AddOn details
		assetTypeComponentBeanMap.get("IPC addon").setSiAssetComponentList(addOnList);
		LOGGER.info("IPCQuoteMACDService.getAddOnDetails method ends");
	}

	private void getAccessDetails(Map<String,Integer> assetIDTypeMap,Map<String,SIServiceAssetComponentBean> assetTypeComponentBeanMap) {
		LOGGER.info("IPCQuoteMACDService.getAccessDetails method invoked");
		Integer quoteCloudId=assetIDTypeMap.get("Access");
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByName("Access Type");
		LOGGER.info("QCloud ID::"+quoteCloudId);
		LOGGER.info("MSTPC ID::"+mstProductComponent.getId());
		List<QuoteProductComponent> quoteProductComponentList=quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(quoteCloudId, mstProductComponent);
		List<SIAssetComponentBean> accessList= new ArrayList<>();
		SIServiceAssetComponentBean siServiceAssetComponentBean=assetTypeComponentBeanMap.get("Access");
		LOGGER.info("siServiceAssetComponentBean {}" , siServiceAssetComponentBean);
		if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()){
			LOGGER.info("QPC::"+quoteProductComponentList.size());
			Set<QuoteProductComponentsAttributeValue> quoteProductComponentSet=quoteProductComponentList.stream().findFirst().get().getQuoteProductComponentsAttributeValues();
			SIAssetComponentBean siAssetComponentBean = new SIAssetComponentBean();
			for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue:quoteProductComponentSet) {
				if(("accessOption").equals(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName())) {
					siAssetComponentBean.setName(quoteProductComponentsAttributeValue.getAttributeValues());
				} else if(quoteProductComponentsAttributeValue.getAttributeValues() != null && !quoteProductComponentsAttributeValue.getAttributeValues().isEmpty()){
					siAssetComponentBean.setCategory("Access Type");
					siAssetComponentBean.setValue(quoteProductComponentsAttributeValue.getAttributeValues());
				}
				siAssetComponentBean.setArc(siServiceAssetComponentBean.getArc());
				siAssetComponentBean.setMrc(siServiceAssetComponentBean.getMrc());
				siAssetComponentBean.setNrc(siServiceAssetComponentBean.getNrc());
			}
			accessList.add(siAssetComponentBean);
		}
		//Set Access details
		if(null!=siServiceAssetComponentBean) {
			siServiceAssetComponentBean.setSiAssetComponentList(accessList);
		}
		LOGGER.info("IPCQuoteMACDService.getAccessDetails method ends");
	}

	public void updateSolutionName(Integer quoteCloudId, String solutionName,SolutionDetail solution) throws TclCommonException {
		LOGGER.info("IPCQuoteMACDService.updateSolutionName method invoked");
	 try {
			Optional<QuoteCloud> quoteCloudOpt = quoteCloudRepository.findById(quoteCloudId);
			User user = getUserId(Utils.getSource());
			MstProductFamily productFamily = getProductFamily(CommonConstants.IPC);
			if(!quoteCloudOpt.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_CLOUD_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
			} else {
				QuoteCloud quoteCloud = quoteCloudOpt.get();
				ProductSolution productSolution = quoteCloud.getProductSolution();
				String tpsSfdcProductId = productSolution.getTpsSfdcProductId();
				String tpsSfdcProductName = productSolution.getTpsSfdcProductName();
				List<String> productNames = quoteCloudRepository.getProductNameBasedOnQuoteCloudId(quoteCloud.getId());
				MstProductOffering mstProductOffering = mstProductOfferingRepository.findByProductNameAndStatus(solutionName, CommonConstants.BACTIVE);
				if(null == mstProductOffering) {
					mstProductOffering = getProductOffering(productFamily, solutionName, user);
				}
				QuoteToLeProductFamily quoteToLeProductFamily = getQuoteToLeProductDetails(quoteToLeRepository.findById(quoteCloud.getQuoteToLeId()).get(), getProductFamily("IPC"));
				if(Objects.nonNull(productNames) && !productNames.isEmpty() && !productNames.contains(solutionName)) {
					LOGGER.info("Solution Name doesn't exists");
					productSolution = new ProductSolution();
					productSolution.setMstProductOffering(mstProductOffering);
					productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
					productSolution.setSolutionCode(Utils.generateUid());
					productSolution.setTpsSfdcProductId(tpsSfdcProductId);
					productSolution.setTpsSfdcProductName(tpsSfdcProductName);
					productSolution.setProductProfileData(Utils.convertObjectToJson(solution));
					productSolutionRepository.save(productSolution);
					quoteCloud.setProductSolution(productSolution);
				} else {
					LOGGER.info("Solution Name already exists");
					Integer psId = quoteCloudRepository.findByQuoteCloudIdAndResourceDisplayNameAndStatus(quoteCloud.getId(),solutionName,(byte) 1);
					quoteCloud.setProductSolution(productSolutionRepository.findById(psId).get());
				}
				quoteCloud.setResourceDisplayName(solutionName);
				quoteCloudRepository.save(quoteCloud);
				
				//Delete unused productSolution
				List<Integer> psIds = quoteCloudRepository.findByQuoteCloudIdAndStatus(quoteCloud.getId(), (byte) 1);
				List<ProductSolution> psUnusedList = productSolutionRepository.findByQuoteToLeProductFamilyAndIdNotIn(quoteToLeProductFamily, psIds);
				if(Objects.nonNull(psUnusedList) && !psUnusedList.isEmpty()){
					LOGGER.info("Deleting unused product solutions");
					productSolutionRepository.deleteAll(psUnusedList);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("IPCQuoteMACDService.updateSolutionName method ends");
	}
	
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
				LOGGER.info("Order is not available. Creating new Order entity");
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
					}
				}

				LOGGER.info("saveMacdOrderTypeAndOrderCategory");
				saveMacdOrderTypeAndOrderCategory(order, quote);
				detail.setOrderId(order.getId());

				LOGGER.info("Get Details from QuoteToLe");
				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					if (Objects.nonNull(quoteLe.getQuoteCategory()) && !quoteLe.getQuoteCategory()
							.equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
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
						CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
						if (cofDetails != null) {
							cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
						}
						Integer userId = order.getCreatedBy();
						String userEmail = null;
						if (userId != null) {
							Optional<User> userDetails = userRepository.findById(userId);
							if (userDetails.isPresent()) {
								userEmail = userDetails.get().getEmailId();
							}
						}
						for (OrderToLe orderToLe : order.getOrderToLes()) {
							List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
									.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES,
											quote.getId(), AttachmentTypeConstants.COF.toString());
							for (OmsAttachment omsAttachment : omsAttachmentList) {
								omsAttachment.setOrderToLe(orderToLe);
								omsAttachment.setReferenceName(CommonConstants.ORDERS);
								omsAttachment.setReferenceId(order.getId());
								omsAttachmentRepository.save(omsAttachment);
							}
							ipcQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(),
									orderToLe.getId(), cofObjectMapper);
							break;
						}
						processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
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
				} else {
					if (Objects.nonNull(quoteLe.getQuoteCategory())
							&& quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
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

			for (OrderToLe orderToLe : order.getOrderToLes()) {
				if (MACDConstants.MACD_QUOTE_TYPE.equals(orderToLe.getOrderType())
						&& MACDConstants.DELETE_VM.equals(orderToLe.getOrderCategory())) {
					processAttachmentsFromQuote(quote.getId(), orderToLe);
					ipcOrderService.launchCloud(order.getId(), orderToLe.getId(), false);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}

	public String addOrUpdateComponentAttr(Integer quoteId, List<UpdateIpcComponentBean> ipcComponentLst) throws TclCommonException {
		LOGGER.info("Processing addOrUpdateComponentAttr..");
		User user = getUserId(Utils.getSource());
		ipcComponentLst.forEach(compLst ->{
			compLst.getComponents().forEach(component -> {
				MstProductComponent mstPrdCompt = ipcQuoteService.getProductComponent(component, user);
				QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
						.findByCloudCodeAndMstProductComponent(compLst.getCloudCode(), mstPrdCompt.getId());
				for (AttributeDetail attribute : component.getAttributes()) {
					try {
						if(quoteProductComponent == null) {
							quoteProductComponent = new QuoteProductComponent();
							quoteProductComponent.setMstProductComponent(mstPrdCompt);
						    MstProductFamily productFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.IPC, (byte) 1);
							quoteProductComponent.setMstProductFamily(productFamily);
							QuoteCloud quoteCloud = quoteCloudRepository.findFirstByCloudCodeOrderByIdDesc(compLst.getCloudCode());
							quoteProductComponent.setReferenceId(quoteCloud.getId());
							quoteProductComponent.setReferenceName(QuoteConstants.IPCCLOUD.toString());
							quoteProductComponentRepository.save(quoteProductComponent);
						}
						processProductAttribute(quoteProductComponent, attribute, user);
					} catch (TclCommonException e) {
						LOGGER.error("Exception thrown {}", e.getMessage());
					}
				}
			});
		});
		return "SUCCESS";
	}
}
