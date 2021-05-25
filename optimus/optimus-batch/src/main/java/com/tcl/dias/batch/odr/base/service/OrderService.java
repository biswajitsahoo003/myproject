package com.tcl.dias.batch.odr.base.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import com.tcl.dias.common.constants.TeamsDROdrConstants;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import com.tcl.dias.batch.odr.gvpn.GvpnOdrService;
import com.tcl.dias.batch.odr.mapper.OdrMapper;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.IzopcDcRequestBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.ProductLocationBean;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderResponse;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.OrderSource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.entities.OdrAttachment;
import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrComponentAttribute;
import com.tcl.dias.oms.entity.entities.OdrContractInfo;
import com.tcl.dias.oms.entity.entities.OdrGstAddress;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrOrderAttribute;
import com.tcl.dias.oms.entity.entities.OdrProductDetail;
import com.tcl.dias.oms.entity.entities.OdrProductDetailAttributes;
import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercial;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercialComponent;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceSla;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.OrderSiteAddress;
import com.tcl.dias.oms.entity.entities.OrderSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrderVrfSites;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OdrAdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.OdrAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentRepository;
import com.tcl.dias.oms.entity.repository.OdrContractInfoRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceCommercialRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceDetailRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionSiLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteBillingDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderVrfSiteRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This class is used to freeze the order and move the order to the odr flat
 * tables
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public abstract class OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OdrOrderRepository odrOrderRepository;

	@Autowired
	OdrContractInfoRepository odrContractInfoRepository;

	@Autowired
	OdrOrderAttributeRepository odrOrderAttributeRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OdrServiceDetailRepository odrServiceDetailRepository;

	@Autowired
	OdrServiceCommercialRepository odrServiceCommercialRepository;

	@Autowired
	OdrServiceAttributeRepository odrServiceAttributeRepository;

	@Autowired
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository OrderProductComponentsAttributeValueRepository;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	OrderCloudRepository orderCloudRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.pricing.ipc.location}")
	String ipcPricingLocationQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String poplocationQueue;

	@Value("${rabbitmq.location.localitcontact}")
	String localItQueue;

	@Value("${odr.attachment.details}")
	String attachmentQueue;

	@Value("${rabbitmq.location.demarcation}")
	String demarcationQueue;

	@Value("${rabbitmq.o2c.fulfillmentdate}")
	String o2cFulfillmentQueue;

	@Value("${rabbitmq.o2c.oe.fulfillmentdate}")
	String orderEnrichmentFulfillQueue;

	@Value("${rabbitmq.cpe.bom.resource.queue}")
	String productBomQueue;

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.si.order.get.queue}")
	String getSIOrderQueue;
	
	@Value("${rabbitmq.site.gst.queue}")
	String siteGstQueue;
	
	@Value("${rabbitmq.get.partner.legal.entities}")
	String partnerLegalEntities;
	
	@Value("${rabbitmq.partner.details.queue}")
	String partner;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	OdrMapper odrMapper;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	OdrAttachmentRepository odrAttachmentRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	OdrAdditionalServiceParamRepository odrAdditionalServiceParamRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	OrderIllSitesRepository orderIllSiteRepository;
	
	@Autowired
	OdrComponentAttributeRepository odrComponentAttributeRepository;
	
	@Autowired
	OdrComponentRepository odrComponentRepository;
	
	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;
	
	@Value("${rabbitmq.cpe.bom.ntw.products.queue}")
	String cpeBomNtwProductsQueue;

	@Value("${queue.usage.eventsource}")
	String eventSourceQueue;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	EngagementOpportunityRepository engagementOpportunityRepository;
	
	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	OrderVrfSiteRepository orderVrfSiteRepository;
	
	@Autowired
	GvpnOdrService gvpnOdrService;
	
	@Autowired
	OrderSiteServiceTerminationDetailsRepository orderSiteServiceTerminationDetailsRepository;
	
	@Value("${rabbitmq.si.order.details.inactive.queue}")
	String siOrderDetailsInactiveQueue;
	
	@Value("${rabbitmq.product.location.county}")
	String productLocationQueue;
	
	@Value("${rabbitmq.product.izopc.dc}")
	String productDcInterconnectQueue;
	
	@Autowired
	OrderProductSolutionSiLinkRepository orderProductSolutionSiLinkRepository;
	private final String ILL_CODE = "0300";
	private final String GVPN_CODE = "6230";
	private final String IPC_CODE = "0000";

	private final String CLOUD = "CLOUD";
	private final String ACCESS = "ACCESS";
	private final String ADDON = "ADDON";

	protected abstract String getReferenceName();
	
	@Autowired
	OrderSiteBillingDetailsRepository orderSiteBillingInfoRepository;

	/**
	 * 
	 * processOrderFrost - This method is used to freeze the order by transforming
	 * the order to a new table odr
	 * 
	 * @param orderId
	 * @return -Boolean - Status whether the process is success or failure
	 */
	public synchronized Boolean processOrderFrost(Integer orderId, String username) {
		LOGGER.info("Process starting to freeze the order with id {}", orderId);
		Boolean status = true;
		Optional<Order> orderEntity = orderRepository.findById(orderId);
		if (orderEntity.isPresent()) {
			OdrOrder odrOrder = odrOrderRepository.findByOpOrderCode(orderEntity.get().getOrderCode());
			if (odrOrder != null) {
				LOGGER.warn("Order is already frozen with odr Id {}", odrOrder.getId());
				return false;
			} else {
				odrOrder = new OdrOrder();
				LOGGER.info("New odr orders");
			}
			try {
				processOrder(orderEntity.get(), odrOrder, username);
				String stage=orderEntity.get().getStage();
				LOGGER.info("processed process order");
				boolean flag = true;
				List<OdrOrderAttribute> odrAttributes = new ArrayList<>();
				List<OdrContractInfo> odrContractInfos = new ArrayList<>();
				List<OdrServiceDetail> odrServiceDetails = new ArrayList<>();
				List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();
				Map<String, Map<String, Integer>> prisecMapping = new HashMap<>();
				for (OrderToLe orderToLe : orderEntity.get().getOrderToLes()) {
					LOGGER.info("Processing order to le for {}", orderToLe.getId());
					Map<String, String> flowMapper = new HashMap<>();
					OdrContractInfo odrContractInfo = new OdrContractInfo();
					odrContractInfo.setOdrOrder(odrOrder);
					// ProcessOrderToLe
					processOrderToLe(orderToLe, odrOrder, flag, odrContractInfo, username);
					flag = false;
					List<Map<String, Object>> leAttributes = ordersLeAttributeValueRepository
							.findByLeId(orderToLe.getId());
					processOrderToLeAttr(leAttributes, odrOrder, odrAttributes, odrContractInfo, username, flowMapper);
					odrContractInfos.add(odrContractInfo);
					processProductFamily(orderToLe, flowMapper, odrServiceDetails, odrContractInfo, odrOrder, username,
							odrServiceCommercials, odrAttributes);
				}
				LOGGER.info("Saving all the Order Repository");
				odrOrderRepository.save(odrOrder);
				odrOrderAttributeRepository.saveAll(odrAttributes);
				odrContractInfoRepository.saveAll(odrContractInfos);
				/*
				 * for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
				 * LOGGER.info("SAVING {}",odrServiceDetail.getPrimarySecondary()); for
				 * (OdrComponent odrComponent : odrServiceDetail.getOdrComponents()) {
				 * odrComponent.setOdrServiceDetail(odrServiceDetail); for
				 * (OdrComponentAttribute odrComponentAttribute :
				 * odrComponent.getOdrComponentAttributes()) {
				 * odrComponentAttribute.setOdrServiceDetail(odrServiceDetail); } }
				 * odrServiceDetailRepository.save(odrServiceDetail); }
				 */
				/*
				 * for(OdrServiceDetail serviceDetail : odrServiceDetails) {
				 * for(OdrServiceAttribute attr : serviceDetail.getOdrServiceAttributes()) {
				 * LOGGER.info("Attribute attr :{}, val {}", attr.getAttributeName(),
				 * attr.getAttributeValue()); }
				 * 
				 * }
				 */
				odrServiceDetailRepository.saveAll(odrServiceDetails);
				for (OdrServiceCommercial odrServiceCommercial : odrServiceCommercials) {
					LOGGER.info("Type {}",odrServiceCommercial.getServiceType());
					LOGGER.info("Saving {}",odrServiceCommercial.getOdrServiceDetail().getId());
					odrServiceCommercialRepository.save(odrServiceCommercial);
				}
				//odrServiceCommercialRepository.saveAll(odrServiceCommercials);

				for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
					if (odrServiceDetail.getServiceRefId() != null
							&& odrServiceDetail.getServiceRefId().contains("-")) {
						String[] spliter = odrServiceDetail.getServiceRefId().split("-");
						if (prisecMapping.get(spliter[0]) == null) {
							Map<String, Integer> priMapper = new HashMap<String, Integer>();
							priMapper.put(spliter[1], odrServiceDetail.getId());
							prisecMapping.put(spliter[0], priMapper);
						} else {
							Map<String, Integer> priMapper = prisecMapping.get(spliter[0]);
							priMapper.put(spliter[1], odrServiceDetail.getId());
						}

					}
				}
				for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
					if (odrServiceDetail.getServiceRefId() != null
							&& odrServiceDetail.getServiceRefId().contains("-")) {
						String[] spliter = odrServiceDetail.getServiceRefId().split("-");
						if (prisecMapping.get(spliter[0]) != null) {
							if (spliter[1].contains("PRIM")) {
								odrServiceDetail.setErfPriSecServiceLinkId(prisecMapping.get(spliter[0]).get("SECO"));
							} else {
								odrServiceDetail.setErfPriSecServiceLinkId(prisecMapping.get(spliter[0]).get("PRIM"));
							}
							odrServiceDetailRepository.save(odrServiceDetail);
						}

					}
					if(odrServiceDetail.getOdrOrderUuid().startsWith("IPC")) {
						LOGGER.info("order Id {} and serviceDetail Id {}" , orderEntity.get().getId(), odrServiceDetail.getId());
						List<OdrAttachment> odrAttachments = new ArrayList<>();
						addToIpcAttachments(odrAttachments, orderEntity.get().getId(), CommonConstants.IPC_TAX);
						addToIpcAttachments(odrAttachments, orderEntity.get().getId(), CommonConstants.IPC_SOLUTION_DOCUMENT);
						addToIpcAttachments(odrAttachments, orderEntity.get().getId(), CommonConstants.IPC_LICENSE_QUOTE);
						addToIpcAttachments(odrAttachments, orderEntity.get().getId(), CommonConstants.IPC_SOLUTION_VALIDATION_EMAIL);
						
						for(OdrAttachment odrAttach : odrAttachments) {
							odrAttach.setOdrServiceDetail(odrServiceDetail);
							odrAttachmentRepository.save(odrAttach);
						}
					}
				}
				Map<Integer, Integer> masterServiceIdMap = new HashMap<>();
				for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
					if (odrServiceDetail.getMasterServiceId() != null) {
						Optional<OrderVrfSites> orderVrfSites = orderVrfSiteRepository
								.findById(odrServiceDetail.getMasterServiceId());
						if (orderVrfSites.isPresent() && orderVrfSites.get().getVrfType().equalsIgnoreCase("master")) {
							masterServiceIdMap.put(odrServiceDetail.getMasterServiceId(), odrServiceDetail.getId());
							odrServiceDetail.setMasterServiceId(null);
							odrServiceDetailRepository.save(odrServiceDetail);
						}
					}
				}

				for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
					if (odrServiceDetail.getMasterServiceId() != null) {
						Optional<OrderVrfSites> orderVrfSites = orderVrfSiteRepository
								.findById(odrServiceDetail.getMasterServiceId());
						if (orderVrfSites.isPresent() && !orderVrfSites.get().getVrfType().equalsIgnoreCase("master")) {
							List<OrderVrfSites> orderVrfSites2 = orderVrfSiteRepository
									.findByOrderIllSiteAndSiteTypeAndVrfType(orderVrfSites.get().getOrderIllSite(),
											odrServiceDetail.getPrimarySecondary().toLowerCase(), "master");
							if (orderVrfSites2 != null && !orderVrfSites2.isEmpty()
									&& masterServiceIdMap.containsKey(orderVrfSites2.get(0).getId())) {
								LOGGER.info("Got Master service ID details for {}",odrServiceDetail.getId());
								odrServiceDetail.setMasterServiceId(masterServiceIdMap.get(orderVrfSites2.get(0).getId()));
								odrServiceDetailRepository.save(odrServiceDetail);
							}

						}
					}
				}
				
				if (odrOrder.getOrderType().equalsIgnoreCase("RENEWALS")) {
					Set<OrderToLe> orderToLes = orderEntity.get().getOrderToLes();
					List<OrderToLe> aList = new ArrayList<OrderToLe>();
					for (OrderToLe x : orderToLes)
						aList.add(x);

					List<OmsAttachment> omsAttachments = omsAttachmentRepository
							.findByOrderToLeInAndAttachmentType(aList, "RENEWALS");
					if (!omsAttachments.isEmpty()) {

						OmsAttachment omsAttachment = omsAttachments.get(0);
						
						Integer erfAttachmentId = omsAttachment.getErfCusAttachmentId();
						String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
								String.valueOf(erfAttachmentId));
						
						Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse,
								Map.class);
						
						Attachment attachment = new Attachment();
						attachment.setCreatedBy(username);
						attachment.setCreatedDate(new Timestamp(new Date().getTime()));
						attachment.setIsActive(CommonConstants.Y);
						attachment.setName((String) attachmentMapper.get("NAME"));
						attachment.setType(omsAttachment.getAttachmentType());
						attachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
						attachmentRepository.save(attachment);
						
						
						List<OdrAttachment> odrServiceDetailList = new ArrayList<OdrAttachment>();
						for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
							OdrAttachment odrAttachment = new OdrAttachment();
							odrAttachment.setAttachmentId(attachment.getId());
							odrAttachment.setOrderId(orderEntity.get().getId());
							odrAttachment.setAttachmentType(omsAttachment.getAttachmentType());
							odrAttachment.setIsActive(CommonConstants.Y);
							//odrAttachment.setOfferingName(flowMapper.get("offeringName"));
							 odrAttachment.setProductName(odrOrder.getOpOrderCode().substring(0, 3));
							odrAttachment.setOdrServiceDetail(odrServiceDetail);
							odrServiceDetailList.add(odrAttachment);
							
						}
						odrAttachmentRepository.saveAll(odrServiceDetailList);
					}
				}

				OdrOrderBean odrOrderBean = odrMapper.mapOrderEntityToBean(odrOrder, odrContractInfos, odrAttributes,
						odrServiceDetails, odrServiceCommercials);
				odrOrderBean.setStage(stage);
				LOGGER.info("order froze completed");
				String resPayload = Utils.convertObjectToJson(odrOrderBean);
				LOGGER.info("Response to be transformed to sc is {}", resPayload);
				LOGGER.info("MDC Filter token value in before Queue call fulfillment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(o2cFulfillmentQueue, Utils.convertObjectToJson(odrOrderBean));

			} catch (Exception e) {
				LOGGER.error("Error in Order Frost {}", ExceptionUtils.getStackTrace(e));
				status = false;
			}
		} else {
			status = false;
			LOGGER.info("Order is Not found for id {}", orderId);
		}
		return status;
	}
	
	private void addToIpcAttachments(List<OdrAttachment> odrAttachments, Integer orderId, String attachmentType) {
		List<OdrAttachment> odrAttachment = odrAttachmentRepository.findByOrderIdAndAttachmentType(orderId, attachmentType);
		if(odrAttachment != null && odrAttachment.size() > 0) {
			odrAttachments.addAll(odrAttachment);
		}
	}

	/**
	 * processProductFamily
	 * 
	 * @param orderToLe
	 * @param flowMapper
	 * @param odrAttributes
	 * @throws ParseException
	 */
	public void processProductFamily(OrderToLe orderToLe, Map<String, String> flowMapper,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes) {
		for (OrderToLeProductFamily orderToLeProdFamily : orderToLe.getOrderToLeProductFamilies()) {
			flowMapper.put("productName", orderToLeProdFamily.getMstProductFamily().getName());
			flowMapper.put("productCode", orderToLeProdFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
			processProductSolution(orderToLe, flowMapper, orderToLeProdFamily, odrServiceDetails, odrContractInfo,
					odrOrder, username, odrServiceCommercials, odrAttributes);
		}
	}

	/**
	 * processProductSolution
	 * 
	 * @param flowMapper
	 * @param orderToLeProdFamily
	 * @throws ParseException
	 */
	public void processProductSolution(OrderToLe orderToLe, Map<String, String> flowMapper,
			OrderToLeProductFamily orderToLeProdFamily, List<OdrServiceDetail> odrServiceDetails,
			OdrContractInfo odrContractInfo, OdrOrder odrOrder, String username,
			List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes) {
		flowMapper.put("isAmended",
				(orderToLe.getIsAmended() != null
						? (orderToLe.getIsAmended() == CommonConstants.BACTIVE ? "Y" : "N")
						: "N"));
		for (OrderProductSolution orderProductSolution : orderToLeProdFamily.getOrderProductSolutions()) {
			if ("MACD".equals(orderToLe.getOrderType()) || "CANCELLATION".equalsIgnoreCase(orderToLe.getOrderType()) || "TERMINATION".equalsIgnoreCase(orderToLe.getOrderType())) {
				if(odrOrder.getOpOrderCode().contains("IPC")){
					LOGGER.info("MACD IPC Flow, setting uuid for ODR::" + orderToLe.getOrder().getQuote().getQuoteToLes()
							.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
					flowMapper.put("serviceCode", orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
							.getErfServiceInventoryTpsServiceId());
				}else if(odrOrder.getOpOrderCode().contains("IAS") || odrOrder.getOpOrderCode().contains("GVPN") || odrOrder.getOpOrderCode().contains("IZOPC")){
					//QuoteToLe quoteToLe=orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
					List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
					for (OrderIllSiteToService orderIllSiteToService : orderIllSiteToServices) {
						LOGGER.info(
								"Service Type --> {} , o2c service id {}, service inventory service id {} , isAmended {}",
								orderIllSiteToService.getType(), orderIllSiteToService.getO2cServiceId(),
								orderIllSiteToService.getErfServiceInventoryTpsServiceId(),
								flowMapper.get("isAmended"));
						if(orderIllSiteToService.getType() != null
								&& orderIllSiteToService.getType().equalsIgnoreCase("primary")
								&& flowMapper.get("isAmended") != null && flowMapper.get("isAmended").equals("Y")) {
							LOGGER.info("Primary amendment service ID --> {}",orderIllSiteToService.getO2cServiceId());
							flowMapper.put(
									"amended_o2c_serviceId-" + orderIllSiteToService.getOrderIllSite().getSiteCode(),
									orderIllSiteToService.getO2cServiceId());
						}
						if (orderIllSiteToService.getType() != null
								&& orderIllSiteToService.getType().equalsIgnoreCase("secondary")) {
							flowMapper.put(orderIllSiteToService.getOrderIllSite().getSiteCode() + "-SEC",
									orderIllSiteToService.getErfServiceInventoryTpsServiceId());
							if(flowMapper.get("isAmended")!=null && flowMapper.get("isAmended").equals("Y")) {
								LOGGER.info("Secondary amendment service ID --> {}",orderIllSiteToService.getO2cServiceId());
								flowMapper.put("amended_o2c_serviceId-sec-"+orderIllSiteToService.getOrderIllSite().getSiteCode(),orderIllSiteToService.getO2cServiceId());
							}
						} else {
							flowMapper.put(orderIllSiteToService.getOrderIllSite().getSiteCode(),
									orderIllSiteToService.getErfServiceInventoryTpsServiceId());
							/*
							 * if(flowMapper.get("isAmended")!=null &&
							 * flowMapper.get("isAmended").equals("Y")) {
							 * flowMapper.put("amended_o2c_serviceId-"+orderIllSiteToService.getOrderIllSite
							 * ().getSiteCode(),orderIllSiteToService.getO2cServiceId()); }
							 */
						}
						flowMapper.put(String.valueOf(orderIllSiteToService.getOrderIllSite().getId()), orderIllSiteToService.getErfSfdcSubType());
					}
					//LOGGER.info("MACD ILL or GVPN Flow, setting uuid for ODR::" + quoteIllSiteToServices.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
					//flowMapper.put("serviceCode", quoteIllSiteToServices.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
				}
			} else {
				if (odrOrder.getOpOrderCode().contains("IAS") || odrOrder.getOpOrderCode().contains("GVPN")) {
					List<OrderIllSiteToService> orderIllSiteToServices = orderIllSiteToServiceRepository
							.findByOrderToLe_Id(orderToLe.getId());
					for (OrderIllSiteToService orderIllSiteToService : orderIllSiteToServices) {
						if (orderIllSiteToService.getType() != null
								&& orderIllSiteToService.getType().equalsIgnoreCase("secondary")) {
							if (flowMapper.get("isAmended") != null && flowMapper.get("isAmended").equals("Y")) {
								flowMapper.put(
										"amended_o2c_serviceId-sec-"
												+ orderIllSiteToService.getOrderIllSite().getSiteCode(),
										orderIllSiteToService.getO2cServiceId());
							}
						} else {
							if (flowMapper.get("isAmended") != null && flowMapper.get("isAmended").equals("Y")) {
								flowMapper.put(
										"amended_o2c_serviceId-"
												+ orderIllSiteToService.getOrderIllSite().getSiteCode(),
										orderIllSiteToService.getO2cServiceId());
							}
						}
					}
				}
			}
			if (orderToLeProdFamily.getMstProductFamily().getName().equalsIgnoreCase(OdrConstants.IPC)) {
				flowMapper.put("offeringName", "IPC-CLOUD");
				flowMapper.put("quoteId", String.valueOf(orderToLe.getOrder().getQuote().getId()));
				processCloud(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username,
						odrAttributes);
			} else {
				if("CANCELLATION".equalsIgnoreCase(orderToLe.getOrderType())) {
					flowMapper.put("offeringName", orderProductSolution.getOrderToLeProductFamily().getMstProductFamily().getName());
				} 
				else {
				flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
				}
				processSite(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username,
						odrServiceCommercials);
				processLink(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username);
			}
		}
	}

	/**
	 * processIllSite
	 * 
	 * @param orderProductSolution
	 */
	public void processSite(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrServiceCommercial> odrServiceCommercials) {

		for (OrderIllSite orderIllSite : orderProductSolution.getOrderIllSites()) {
			List<OrderVrfSites>  orderVrfSites = orderVrfSiteRepository.findByOrderIllSite(orderIllSite);
			if(orderVrfSites!=null && !orderVrfSites.isEmpty()) {
				LOGGER.info("Got VRF details for site id {}",orderIllSite.getId());
				gvpnOdrService.processVRFSite(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username, odrServiceCommercials, orderVrfSites);
			}else {
			try {
				String type=null;
				if("RENEWALS".equals(odrOrder.getOrderType())){
					List<OrderProductComponent> orderProductComponentList=orderProductComponentRepository.findByReferenceId(orderIllSite.getId());
					for(OrderProductComponent orderProductComponent: orderProductComponentList) {
						if(orderProductComponent.getType()!=null && orderProductComponent.getType().equalsIgnoreCase("secondary")) {
							type = "secondary";
							flowMapper.put(orderIllSite.getSiteCode() + "-SEC",
									orderIllSite.getErfServiceInventoryTpsServiceId());
							break;
						}else {
							type = "primary";
							break;
						}
					}
					
				}
				if ("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType()) || "TERMINATION".equals(odrOrder.getOrderType())) {
					if (flowMapper.get(orderIllSite.getSiteCode()) != null
							&& flowMapper.get(orderIllSite.getSiteCode() + "-SEC") != null) {
						LOGGER.info("Into dual");
						type = "dual";
					} else if (flowMapper.get(orderIllSite.getSiteCode() + "-SEC") != null) {
						LOGGER.info("Into secondary");
						type = "secondary";
					} else if (flowMapper.get(orderIllSite.getSiteCode()) != null) {
						LOGGER.info("Into primary");
						type = "primary";
					}
				}
				
				OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();
				OdrComponent primaryOdrComponent = persistOdrComponent(primaryserviceDetail);
				Set<OdrComponentAttribute> odrComponentAttributes=primaryOdrComponent.getOdrComponentAttributes();
				primaryserviceDetail.setFlowStatus("NEW");
				primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "siteCode", orderIllSite.getSiteCode()));
				primaryserviceDetail.setServiceRefId(orderIllSite.getSiteCode());
				primaryserviceDetail.setOdrContractInfo(odrContractInfo);
				primaryserviceDetail.setOdrOrder(odrOrder);
				primaryserviceDetail.setServiceCommissionedDate(
						orderIllSite.getEffectiveDate() != null ? orderIllSite.getEffectiveDate()
								: orderIllSite.getRequestorDate());
				primaryserviceDetail.setRrfsDate(orderIllSite.getRequestorDate());
				primaryserviceDetail.setArc(orderIllSite.getArc());
				primaryserviceDetail.setMrc(orderIllSite.getMrc());
				primaryserviceDetail.setNrc(orderIllSite.getNrc());
				primaryserviceDetail.setErfPrdCatalogOfferingName(flowMapper.get("offeringName"));
				primaryserviceDetail.setCreatedBy(username);
				primaryserviceDetail.setCreatedDate(new Date());
				primaryserviceDetail.setUpdatedDate(new Date());
				primaryserviceDetail.setUpdatedBy(username);
				primaryserviceDetail.setIsActive(CommonConstants.Y);
				primaryserviceDetail.setIsIzo(CommonConstants.N);
				primaryserviceDetail.setOrderType(CommonConstants.NEW);
				primaryserviceDetail.setIsAmended(flowMapper.get("isAmended"));
				String absorbedOrPassedOn = null;
				List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
				if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty()) {
					absorbedOrPassedOn = orderIllSiteToServiceList.get(0).getAbsorbedOrPassedOn();
				LOGGER.info("Absorbed or passed on {} order site Id {}",absorbedOrPassedOn, orderIllSite.getId());
				if(absorbedOrPassedOn != null ) {
					if("Absorbed".equalsIgnoreCase(absorbedOrPassedOn)) {
						primaryserviceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						primaryserviceDetail.setCancellationCharges(0D);
					} else if ("Passed On".equalsIgnoreCase(absorbedOrPassedOn)) {
						primaryserviceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						primaryserviceDetail.setCancellationCharges(orderIllSite.getNrc());
					}
					LOGGER.info("cancellation charges for order site id {} is {}", orderIllSite.getId(), primaryserviceDetail.getCancellationCharges());
				}
				
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "cancellationReason", orderIllSiteToServiceList.get(0).getCancellationReason()));
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "leadToRFSDate", orderIllSiteToServiceList.get(0).getLeadToRFSDate()));
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "effectiveDateOfChange", String.valueOf(orderIllSiteToServiceList.get(0).getEffectiveDateOfChange())));
				}
				
				if ("RENEWALS".equalsIgnoreCase(odrOrder.getOrderType())) {
					List<OrdersLeAttributeValue> ordersLeAttributeValueList = ordersLeAttributeValueRepository
							.findByOrderToLe_Id(odrOrder.getErfOrderLeId());
					Optional<OrderToLe> orderTole = orderToLeRepository.findById(odrOrder.getErfOrderLeId());

					Optional<OrdersLeAttributeValue> ordersLeAttributeValue = ordersLeAttributeValueList.stream()
							.filter(x -> x.getDisplayValue().equalsIgnoreCase("Effective Date")).findFirst();
					if (ordersLeAttributeValue.isPresent()) {
						LOGGER.info("value present");

						Date formattedDate = DateUtils.parseDate(ordersLeAttributeValue.get().getAttributeValue(),
								new String[] { "dd-MMM-yyyy", "yyyy-MM-dd" });
						DateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
						String formattedDate1 = dateFormatter1.format(formattedDate);
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "serviceCommissionedDate", formattedDate1));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "effectiveDateOfChange", formattedDate1));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "billStartDate", formattedDate1));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "commissioningDate", formattedDate1));
//				if(orderTole.get().getTermInMonths()!=null && orderTole.get().getTermInMonths()!="") {
//					String month = orderTole.get().getTermInMonths().substring(0, 2); 
//					Integer value = Integer.valueOf(month);
//					
//					Date formattedDate = DateUtils.parseDate(ordersLeAttributeValue.get().getAttributeValue(), 
//							  new String[] { "dd-MMM-yyyy", "yyyy-MM-dd" });
//					formattedDate.setMonth(formattedDate.getMonth()+value);
//					DateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
//					String formattedDate1 = dateFormatter1.format(formattedDate);
//					
//					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "contractEndDate", formattedDate1));
//				}

					}
				}
				if("TERMINATION".equalsIgnoreCase(odrOrder.getOrderType())) {
					orderIllSiteToServiceList.stream().forEach(orderIllSiteToService -> {
						OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails =  orderSiteServiceTerminationDetailsRepository.findByOrderIllSiteToService(orderIllSiteToService);
						if(orderSiteServiceTerminationDetails != null){
							odrOrder.setTerminationParentOrderCode(orderSiteServiceTerminationDetails.getTerminatedParentOrderCode());
							primaryserviceDetail.setTerminationReason(orderSiteServiceTerminationDetails.getReasonForTermination());
							if("Send to CSM".equalsIgnoreCase(orderSiteServiceTerminationDetails.getHandoverTo())
									|| "Send to AM".equalsIgnoreCase(orderSiteServiceTerminationDetails.getHandoverTo())) {
								primaryserviceDetail.setNegotiationRequired(CommonConstants.YES);
							} else {
								primaryserviceDetail.setNegotiationRequired(CommonConstants.NO);
							}
							primaryserviceDetail.setTerminationEffectiveDate(orderSiteServiceTerminationDetails.getEffectiveDateOfChange());
							List<OrderProductComponent> orderProductComponentList= null;
							if(odrOrder.getOpOrderCode().startsWith("GVPN")) {
								orderProductComponentList =
										orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(orderSiteServiceTerminationDetails.getOrderIllSiteToService().getOrderIllSite().getId(),"ETC Charges", "GVPN_SITES");
							} else {
							orderProductComponentList =
									orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(orderSiteServiceTerminationDetails.getOrderIllSiteToService().getOrderIllSite().getId(),"ETC Charges", "ILL_SITES");
							}
							LOGGER.info("orderProductComponentList {}", orderProductComponentList.size());
							Double[] etcValue = { 0D };
							orderProductComponentList.stream().forEach(orderProdComp ->{
									
											OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(String.valueOf(orderProdComp.getId()), "COMPONENTS");
											if(orderPrice != null) {
											etcValue[0] = orderPrice.getEffectiveNrc();
											LOGGER.info("Etc value {}", etcValue[0]);
											}
								});
							primaryserviceDetail.setEtcValue(etcValue[0]);
							primaryserviceDetail.setEtcWaiver(CommonConstants.BACTIVE.equals(orderSiteServiceTerminationDetails.getEtcApplicable()) ? "Yes" : "No");
							primaryserviceDetail.setCustomerRequestorDate(orderSiteServiceTerminationDetails.getRequestedDateForTermination());
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "localItContactName",orderSiteServiceTerminationDetails.getLocalItContactName()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "localItContactMobile",orderSiteServiceTerminationDetails.getLocalItContactNumber()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "localItContactEmailId",orderSiteServiceTerminationDetails.getLocalItContactEmailId()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "customerMailReceiveDate",orderSiteServiceTerminationDetails.getCustomerMailReceivedDate() != null ? orderSiteServiceTerminationDetails.getCustomerMailReceivedDate().toString() : null));
							
							
							List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType( "Site", orderSiteServiceTerminationDetails.getOrderIllSiteToService().getOrderIllSite().getId(), AttachmentTypeConstants.APPRVEMAIL.toString());
							if(omsAttachmentList != null & !omsAttachmentList.isEmpty()) {
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "approvalMailAvailable", "Yes"));
							} else {
								primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "approvalMailAvailable", "No"));
							}
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "csmEmail", orderSiteServiceTerminationDetails.getCsmNonCsmEmail()));
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "csmUserName", orderSiteServiceTerminationDetails.getCsmNonCsmName()));
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "terminationSubType", orderSiteServiceTerminationDetails.getTerminationSubtype()));
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "terminationSubReason", orderSiteServiceTerminationDetails.getSubReason()));
							// get service details from service inventory to pass to O2C
							persistComponentAttrForTermination(primaryserviceDetail, primaryOdrComponent,
									odrComponentAttributes, orderIllSiteToService);
							
						}
					});
				}
				
				if(flowMapper.get("amended_o2c_serviceId-"+orderIllSite.getSiteCode()) !=null) {
					primaryserviceDetail.setAmendedServiceId(flowMapper.get("amended_o2c_serviceId-"+orderIllSite.getSiteCode()));
				}
				
				if("MACD".equals(odrOrder.getOrderType()) && flowMapper.containsKey(String.valueOf(orderIllSite.getId()))){
					LOGGER.info("Order Sub Category");
					primaryserviceDetail.setOrderSubCategory(flowMapper.get(String.valueOf(orderIllSite.getId())));
				}
				if (orderIllSite.getIsTaxExempted() != null
						&& orderIllSite.getIsTaxExempted().equals(CommonConstants.BACTIVE)) {
					primaryserviceDetail.setTaxExemptionFlag(CommonConstants.Y);
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "taxExemption", CommonConstants.Y));
				}else {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "taxExemption", CommonConstants.N));
				}
				primaryserviceDetail.setErfPrdCatalogProductName(flowMapper.get("productName"));
				if(flowMapper.get("productName")!=null && flowMapper.get("productName").equals(CommonConstants.IZOPC)) {
					primaryserviceDetail.setIsIzo(CommonConstants.Y);
					primaryserviceDetail.setServiceOption("Unmanaged");
				}else {
					primaryserviceDetail.setIsIzo(CommonConstants.N);
				}
				primaryserviceDetail.setErfPrdCatalogProductId(Objects.nonNull(flowMapper.get("productCode"))?Integer.valueOf(flowMapper.get("productCode").trim()):null);
				if(("MACD".equals(odrOrder.getOrderType()) && "ADD_SITE".equals(odrOrder.getOrderCategory()))
						|| ("MACD".equals(odrOrder.getOrderType()) && Objects.nonNull(primaryserviceDetail.getOrderSubCategory()) 
								&& primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))){
					LOGGER.info("Parent UUID {}",flowMapper.get(orderIllSite.getSiteCode()));
					LOGGER.info("Order Sub Category {}",primaryserviceDetail.getOrderSubCategory());
					primaryserviceDetail.setParentUuid(flowMapper.get(orderIllSite.getSiteCode()));
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
					primaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
				}else if("MACD".equals(odrOrder.getOrderType()) && !"ADD_SITE".equals(odrOrder.getOrderCategory())
						&& (Objects.isNull(primaryserviceDetail.getOrderSubCategory()) || (Objects.nonNull(primaryserviceDetail.getOrderSubCategory()) && !primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))){
					LOGGER.info("UUID {}",orderIllSite.getSiteCode());
					primaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()));
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
					primaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
				} else if("CANCELLATION".equalsIgnoreCase(odrOrder.getOrderType())) {
					LOGGER.info("CANCELLATION UUID {}",orderIllSite.getSiteCode());
					primaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()));
				} else if("TERMINATION".equalsIgnoreCase(odrOrder.getOrderType())) {
					LOGGER.info("TERMINATION UUID {}",orderIllSite.getSiteCode());
					primaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()));
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
				}else if("RENEWALS".equals(odrOrder.getOrderType())){
					LOGGER.info("UUID {}",orderIllSite.getSiteCode());
					primaryserviceDetail.setUuid(orderIllSite.getErfServiceInventoryTpsServiceId());
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
					OrderProductSolutionSiLink  orderProductSolutionSiLink =  orderProductSolutionSiLinkRepository.findFirstByServiceIdAndQuoteToLeId(orderIllSite.getErfServiceInventoryTpsServiceId(), 
							odrOrder.getErfOrderLeId());
					if(orderProductSolutionSiLink!=null) {
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "PO_DATE", orderProductSolutionSiLink.getPoDate()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "PO_NUMBER", orderProductSolutionSiLink.getPoNumber()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "taxExemption"
								,orderProductSolutionSiLink.getTaxException()));
						primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "taxExemption", orderProductSolutionSiLink.getTaxException()));

					}
					//primaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
				}
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("Order Site address for Reference Id {}",orderIllSite.getId());
				List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.findByQuoteSiteCodeAndServiceType(orderIllSite.getSiteCode(), "primary");
				if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) {
					LOGGER.info("quote site differential commercial order site code {} diff mrc {}, diff nrc {}", orderIllSite.getSiteCode(), quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc(), quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
					primaryserviceDetail.setDifferentialMrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc());
					primaryserviceDetail.setDifferentialNrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
				}
				
				OrderSiteAddress orderSiteAddress = orderSiteAddressRepository.findByReferenceIdAndReferenceNameAndSiteType(orderIllSite.getId().toString(), "ILL_SITES", "b");
				if(orderSiteAddress!=null) {
					LOGGER.info("Order Site Loaded with id {} ",orderSiteAddress.getId());
					String addr = StringUtils.trimToEmpty(orderSiteAddress.getAddressLineOne()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(orderSiteAddress.getAddressLineTwo()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(orderSiteAddress.getLocality()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(orderSiteAddress.getCity()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(orderSiteAddress.getState()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(orderSiteAddress.getCountry()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(orderSiteAddress.getPincode());
					primaryserviceDetail.setSiteAddress(addr);
					primaryserviceDetail.setSiteLatLang(orderSiteAddress.getLatLong());
					primaryserviceDetail.setLatLong(orderSiteAddress.getLatLong());
					primaryserviceDetail.setDestinationCountry(orderSiteAddress.getCountry());
					primaryserviceDetail.setDestinationCity(orderSiteAddress.getCity());
					primaryserviceDetail.setDestinationAddressLineOne(orderSiteAddress.getAddressLineOne());
					primaryserviceDetail.setDestinationAddressLineTwo(orderSiteAddress.getAddressLineTwo());
					primaryserviceDetail.setDestinationLocality(orderSiteAddress.getLocality());
					primaryserviceDetail.setDestinationState(orderSiteAddress.getState());
					primaryserviceDetail.setDestinationPincode(orderSiteAddress.getPincode());
					primaryserviceDetail
							.setErfLocSiteAddressId(String.valueOf(orderSiteAddress.getErfLocationLocId()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "siteAddressId", orderSiteAddress.getErfLocationLocId()+""));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "siteAddress", addr));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "latLong", orderSiteAddress.getLatLong()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationCountry", orderSiteAddress.getCountry()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationCity", orderSiteAddress.getCity()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationAddressLineOne", orderSiteAddress.getAddressLineOne()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationAddressLineTwo", orderSiteAddress.getAddressLineTwo()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationLocality", orderSiteAddress.getLocality()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationState", orderSiteAddress.getState()));
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationPincode", orderSiteAddress.getPincode()));
					try {
							LOGGER.info("Calling product queue for Intl Dial code {}", orderSiteAddress.getCountry());
							String response = (String) mqUtils.sendAndReceive(productLocationQueue,
									orderSiteAddress.getCountry());
							LOGGER.info("Response from product location queue {}", response);
							if (StringUtils.isNotBlank(response)) {
								ProductLocationBean productLocationBean = Utils.convertJsonToObject(response,
										ProductLocationBean.class);
								if (productLocationBean != null
										&& StringUtils.isNotBlank(productLocationBean.getIntlDialCode())) {
									LOGGER.info("Intl dial code for country --> {} is -->{}",
											orderSiteAddress.getCountry(), productLocationBean.getIntlDialCode());
									String intlDialCode = CommonConstants.EMPTY;
									if (productLocationBean.getIntlDialCode().startsWith("+1-")) {
										intlDialCode = "001";
									} else {
										intlDialCode = productLocationBean.getIntlDialCode().replace("+", "");
										if (intlDialCode.length() == 1) {
											intlDialCode = "00".concat(intlDialCode);
										} else if (intlDialCode.length() == 2) {
											intlDialCode = "0".concat(intlDialCode);
										}
									}
									LOGGER.info("Final Intl Dial code is {}", intlDialCode);
									primaryserviceDetail.setIntlDestinationDialCode(intlDialCode);
									odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
											primaryOdrComponent, "destinationCountryDialCode", intlDialCode));
								}
							}
						
					}catch(Exception e) {
						LOGGER.error("Error in product queue call for loaction",e);
					}
				}
				else {
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(orderIllSite.getErfLocSitebLocationId()));
					if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
							if (addressDetail != null) {
								String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne())
										+ CommonConstants.SPACE
										+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getLocality())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCountry())
										+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getPincode());
								primaryserviceDetail.setSiteAddress(addr);
								primaryserviceDetail.setSiteLatLang(addressDetail.getLatLong());
								primaryserviceDetail.setLatLong(addressDetail.getLatLong());
								primaryserviceDetail.setDestinationCountry(addressDetail.getCountry());
								primaryserviceDetail.setDestinationCity(addressDetail.getCity());
								primaryserviceDetail.setDestinationAddressLineOne(addressDetail.getAddressLineOne());
								primaryserviceDetail.setDestinationAddressLineTwo(addressDetail.getAddressLineTwo());
								primaryserviceDetail.setDestinationLocality(addressDetail.getLocality());
								primaryserviceDetail.setDestinationState(addressDetail.getState());
								primaryserviceDetail.setDestinationPincode(addressDetail.getPincode());
								primaryserviceDetail.setErfLocSiteAddressId(
										String.valueOf(orderIllSite.getErfLocSitebLocationId()));
								odrComponentAttributes
										.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
												"siteAddressId", orderIllSite.getErfLocSitebLocationId() + ""));
								odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
										primaryOdrComponent, "siteAddress", addr));
								odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
										primaryOdrComponent, "latLong", addressDetail.getLatLong()));
								odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
										primaryOdrComponent, "destinationCountry", addressDetail.getCountry()));
								odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
										primaryOdrComponent, "destinationCity", addressDetail.getCity()));
								odrComponentAttributes
										.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
												"destinationAddressLineOne", addressDetail.getAddressLineOne()));
								odrComponentAttributes
										.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
												"destinationAddressLineTwo", addressDetail.getAddressLineTwo()));
								odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
										primaryOdrComponent, "destinationLocality", addressDetail.getLocality()));
								odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
										primaryOdrComponent, "destinationState", addressDetail.getState()));
								odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
										primaryOdrComponent, "destinationPincode", addressDetail.getPincode()));
								try {
									if (StringUtils.isNotBlank(addressDetail.getCountry())) {
										LOGGER.info("Calling product queue for Intl Dial code {}",
												addressDetail.getCountry());
										String response = (String) mqUtils.sendAndReceive(productLocationQueue,
												addressDetail.getCountry());
										LOGGER.info("Response from product location queue {}", response);
										if (StringUtils.isNotBlank(response)) {
											ProductLocationBean productLocationBean = Utils
													.convertJsonToObject(response, ProductLocationBean.class);
											if (productLocationBean != null
													&& StringUtils.isNotBlank(productLocationBean.getIntlDialCode())) {
												LOGGER.info("Intl dial code for country --> {} is -->{}",
														addressDetail.getCountry(),
														productLocationBean.getIntlDialCode());
												String intlDialCode = CommonConstants.EMPTY;
												if (productLocationBean.getIntlDialCode().startsWith("+1-")) {
													intlDialCode = "001";
												} else {
													intlDialCode = productLocationBean.getIntlDialCode().replace("+",
															"");
													if (intlDialCode.length() == 1) {
														intlDialCode = "00".concat(intlDialCode);
													} else if (intlDialCode.length() == 2) {
														intlDialCode = "0".concat(intlDialCode);
													}
												}
												LOGGER.info("Final Intl Dial code is {}", intlDialCode);
												primaryserviceDetail.setIntlDestinationDialCode(intlDialCode);
												odrComponentAttributes.add(persistOdrComponentAttributes(
														primaryserviceDetail, primaryOdrComponent,
														"destinationCountryDialCode", intlDialCode));
											}
										}
									}
								} catch (Exception e) {
									LOGGER.error("Error in product queue call for loaction", e);
								}
								LOGGER.info("Pop Location ID is {}",addressDetail.getPopLocationId());
								if (addressDetail.getPopLocationId() != null) {
									if (flowMapper.get("productName") != null
											&& flowMapper.get("productName").equals(CommonConstants.IZOPC)) {
										String dcInterConnectPoint = CommonConstants.EMPTY;
										try {

											List<String> cloudProvider = OrderProductComponentsAttributeValueRepository
													.getAttributeValueByAttributeName(orderIllSite.getId(),
															"Cloud Provider", "primary", getReferenceName());
											LOGGER.info(
													"Cloud Provider for site ID {} is {} for fetching DC Interconnect Point",
													orderIllSite.getId(), cloudProvider);
											if (cloudProvider != null && !cloudProvider.isEmpty()) {
												IzopcDcRequestBean izopcDcRequestBean = new IzopcDcRequestBean();
												izopcDcRequestBean.setCloudProvider(cloudProvider.get(0));
												izopcDcRequestBean.setPopId(addressDetail.getPopLocationId());
												LOGGER.info("Calling product for DC Interconnect point");
												dcInterConnectPoint = (String) mqUtils.sendAndReceive(
														productDcInterconnectQueue,
														Utils.convertObjectToJson(izopcDcRequestBean));
												LOGGER.info("DC Interconnect Point is {}", dcInterConnectPoint);
											}
										} catch (Exception e) {
											LOGGER.error("Error while getting DC Interconnector Point for IZO PC", e);
										}
										odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
												primaryOdrComponent, "DC Interconnect Point", dcInterConnectPoint));
										primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(
												primaryserviceDetail, "DC Interconnect Point", dcInterConnectPoint));
									}
								}
							}
					}
				}
				/*
				 * String cityCode = CommonConstants.EMPTY; if
				 * (primaryserviceDetail.getDestinationCity() != null &&
				 * primaryserviceDetail.getDestinationCity().length() > 4) { cityCode =
				 * primaryserviceDetail.getDestinationCity().substring(0, 4); } else { cityCode
				 * = primaryserviceDetail.getDestinationCity(); } String serviceCode =
				 * getServiceCode(flowMapper, cityCode);
				 * primaryserviceDetail.setUuid(serviceCode);
				 */
				
				//added for order level sitewise billing info
				OrderSiteBillingDetails orderBillinginfo=orderSiteBillingInfoRepository.findByOrderIllSite(orderIllSite);
					if (orderBillinginfo != null) {
						LOGGER.info("inside if multisitebillinginfo id order level:::" + orderBillinginfo.getId());
						if (orderBillinginfo.getEtcCusBillingInfoId() != null) {
							LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							LOGGER.info("inside if billing contact id::" + orderBillinginfo.getEtcCusBillingInfoId());
							String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
									String.valueOf(orderBillinginfo.getEtcCusBillingInfoId()));
							if (StringUtils.isNotBlank(billingContactResponse)) {
								BillingContact billingContact = (BillingContact) Utils
										.convertJsonToObject(billingContactResponse, BillingContact.class);
								if (billingContact !=null  && billingContact.getErfLocationId() != null) {
									LOGGER.info("BILLING CONTACT location id" + billingContact.getErfLocationId());
									String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
											String.valueOf(billingContact.getErfLocationId()));
									if (StringUtils.isNotBlank(locationResponse)) {
										AddressDetail addressDetail = (AddressDetail) Utils
												.convertJsonToObject(locationResponse, AddressDetail.class);
										if (addressDetail != null) {
											odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "billingAddressLineOne", addressDetail.getAddressLineOne()));
											odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "billingAddressLineTwo", addressDetail.getAddressLineTwo()));
											odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "billingAddressLineThree",""));
											odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "billingAddressCity", addressDetail.getCity()));
											odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "billingAddressState", addressDetail.getState()));
											odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "billingAddressCountry", addressDetail.getCountry()));
											odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "billingAddressPincode", addressDetail.getPincode()));

										}
									}
								}
							}
						}
					}
				

				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String demarReponse = (String) mqUtils.sendAndReceive(demarcationQueue,
						String.valueOf(orderIllSite.getErfLocSitebLocationId()));
				if (demarReponse != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> demarDetails = (Map<String, Object>) Utils.convertJsonToObject(demarReponse,
							Map.class);
					if (demarDetails != null) {
						primaryserviceDetail.setDemarcationApartment(
								demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
										: null);
						primaryserviceDetail.setDemarcationFloor(
								demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null);
						primaryserviceDetail.setDemarcationRack(
								demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null);
						primaryserviceDetail.setDemarcationRoom(
								demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null);
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationBuildingName", 
								demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
										: null));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationFloor", 
								demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationWing", 
								demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationRoom", 
								demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null));
					}
				}

				if (orderIllSite.getErfLocSiteaLocationId() != null) {
					LOGGER.info("Site id {} ErfLocSiteaLocationId {}",orderIllSite.getId(), orderIllSite.getErfLocSiteaLocationId());
					LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String popResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(orderIllSite.getErfLocSiteaLocationId()));
					if (popResponse != null) {
						AddressDetail popAddressDetail = (AddressDetail) Utils.convertJsonToObject(popResponse,
								AddressDetail.class);
						if (popAddressDetail != null) {
							String popAddr = StringUtils.trimToEmpty(popAddressDetail.getAddressLineOne())
									+ CommonConstants.SPACE
									+ StringUtils.trimToEmpty(popAddressDetail.getAddressLineTwo())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getLocality())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getCity())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getState())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getCountry())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getPincode());
							primaryserviceDetail.setPopSiteAddress(popAddr);
							primaryserviceDetail.setPopSiteCode(orderIllSite.getErfLocSiteaSiteCode());
							primaryserviceDetail.setSourceCountry(popAddressDetail.getCountry());
							primaryserviceDetail.setSourceCity(popAddressDetail.getCity());
							primaryserviceDetail.setSourceAddressLineOne(popAddressDetail.getAddressLineOne());
							primaryserviceDetail.setSourceAddressLineTwo(popAddressDetail.getAddressLineTwo());
							primaryserviceDetail.setSourceLocality(popAddressDetail.getLocality());
							primaryserviceDetail.setSourcePincode(popAddressDetail.getPincode());
							primaryserviceDetail.setSourceState(popAddressDetail.getState());
							LOGGER.info("Source city in primaryserviceDetail {}",primaryserviceDetail.getSourceCity());
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "popSiteAddressId", orderIllSite.getErfLocSiteaLocationId()+""));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "popSiteAddress", popAddr));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "popSiteCode", orderIllSite.getErfLocSiteaSiteCode()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "sourceCountry", popAddressDetail.getCountry()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "sourceCity", popAddressDetail.getCity()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "sourceAddressLineOne", popAddressDetail.getAddressLineOne()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "sourceAddressLineTwo",popAddressDetail.getAddressLineTwo()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "sourceLocality", popAddressDetail.getLocality()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "sourcePincode", popAddressDetail.getPincode()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "sourceState",popAddressDetail.getState()));
						}
					}
				}
				if (("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType()) || "TERMINATION".equals(odrOrder.getOrderType()) || "RENEWALS".equals(odrOrder.getOrderType())) && !odrOrder.getOpOrderCode().contains("IPC")) {
					if (type != null && (type.equals("primary") || type.equals("dual"))) {
						odrServiceDetails.add(primaryserviceDetail);
					}
				}else {
					odrServiceDetails.add(primaryserviceDetail);
				}
				
				LOGGER.info("Saving secondary Attributes");
				OdrServiceDetail secondaryserviceDetail = new OdrServiceDetail();
				OdrComponent secondaryOdrComponent=new OdrComponent();
				BeanUtils.copyProperties(primaryserviceDetail, secondaryserviceDetail);
				BeanUtils.copyProperties(primaryOdrComponent, secondaryOdrComponent);
				primaryOdrComponent.setOdrServiceDetail(primaryserviceDetail);
				primaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));
				primaryOdrComponent.setSiteType("A");
				primaryserviceDetail.setOdrComponents(null);
				primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
				secondaryserviceDetail.setOdrServiceSlas(new HashSet<>());
				// persistAttachments(flowMapper, username, orderIllSite, primaryserviceDetail);
				processCommonComponentAttr(String.valueOf(orderIllSite.getId()), primaryserviceDetail, username,
						flowMapper,primaryOdrComponent);
				processProductComponentAttr(String.valueOf(orderIllSite.getId()), primaryserviceDetail, "primary",
						username, false, odrServiceCommercials,primaryOdrComponent);
				LOGGER.info("Total Number of primary Service attributes {}",primaryserviceDetail.getOdrServiceAttributes().size());
				processSiteSla(orderIllSite, primaryserviceDetail, username);
				secondaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));
				if(("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType()) || "TERMINATION".equals(odrOrder.getOrderType())|| "RENEWALS".equals(odrOrder.getOrderType())) && flowMapper.get(orderIllSite.getSiteCode() + "-SEC") ==null) {
					secondaryserviceDetail=null;
				}else {
					LOGGER.info("Getting into the Secondary Flow");
					secondaryserviceDetail = processProductComponentAttr(String.valueOf(orderIllSite.getId()),
							secondaryserviceDetail, "secondary", username, false, odrServiceCommercials,secondaryOdrComponent);
					if("MACD".equals(odrOrder.getOrderType()) && flowMapper.containsKey(String.valueOf(orderIllSite.getId()))){
						LOGGER.info("Order Sub Category");
						secondaryserviceDetail.setOrderSubCategory(flowMapper.get(String.valueOf(orderIllSite.getId())));
					}
						if (flowMapper.get("productName") != null
								&& flowMapper.get("productName").equals(CommonConstants.IZOPC)
								&& (odrOrder.getOrderType() == null || "NEW".equals(odrOrder.getOrderType()))) {
							List<String> cloudProvider = OrderProductComponentsAttributeValueRepository
									.getAttributeValueByAttributeName(orderIllSite.getId(), "Cloud Provider", "primary",
											getReferenceName());
							LOGGER.info("Cloud Provider for site ID {} is {}", orderIllSite.getId(), cloudProvider);
							if (cloudProvider != null && !cloudProvider.isEmpty()
									&& cloudProvider.get(0).equals("IBM Direct Link")) {
								LOGGER.info("Getting into the Secondary Flow");
								secondaryserviceDetail = new OdrServiceDetail();
								secondaryOdrComponent = new OdrComponent();
								BeanUtils.copyProperties(primaryserviceDetail, secondaryserviceDetail);
								BeanUtils.copyProperties(primaryOdrComponent, secondaryOdrComponent);
								secondaryserviceDetail.setOdrServiceSlas(new HashSet<>());
								secondaryserviceDetail = processProductComponentAttrWithoutCommercials(
										String.valueOf(orderIllSite.getId()), secondaryserviceDetail, "primary",
										username, false, odrServiceCommercials, secondaryOdrComponent);
								if (secondaryserviceDetail != null) {
									secondaryserviceDetail.setPrimarySecondary("secondary");
									secondaryserviceDetail.setMrc(0D);
									secondaryserviceDetail.setArc(0D);
									secondaryserviceDetail.setNrc(0D);
								}
								if (secondaryserviceDetail != null && "MACD".equals(odrOrder.getOrderType())
										&& flowMapper.containsKey(String.valueOf(orderIllSite.getId()))) {
									LOGGER.info("Order Sub Category");
									secondaryserviceDetail
											.setOrderSubCategory(flowMapper.get(String.valueOf(orderIllSite.getId())));
								}
							}
						}
				}
				if (secondaryserviceDetail != null) {
					if(flowMapper.get("productName")!=null && flowMapper.get("productName").equals(CommonConstants.IZOPC)) {
						secondaryserviceDetail.setIsIzo(CommonConstants.Y);
						secondaryserviceDetail.setServiceOption("Unmanaged");
					}else {
						secondaryserviceDetail.setIsIzo(CommonConstants.N);
					}
					if(("MACD".equals(odrOrder.getOrderType()) && "ADD_SITE".equals(odrOrder.getOrderCategory()))
							|| ("MACD".equals(odrOrder.getOrderType()) && Objects.nonNull(secondaryserviceDetail.getOrderSubCategory()) 
									&& secondaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))){
						LOGGER.info("Parent UUID {}",flowMapper.get(orderIllSite.getSiteCode()+"-SEC"));
						LOGGER.info("Order Sub Category {}",secondaryserviceDetail.getOrderSubCategory());
						secondaryserviceDetail.setParentUuid(flowMapper.get(orderIllSite.getSiteCode()+"-SEC"));
						secondaryserviceDetail.setOrderType(odrOrder.getOrderType());
						secondaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
					}else if("MACD".equals(odrOrder.getOrderType()) && !"ADD_SITE".equals(odrOrder.getOrderCategory())
							&& (Objects.isNull(secondaryserviceDetail.getOrderSubCategory()) || (Objects.nonNull(secondaryserviceDetail.getOrderSubCategory()) && !secondaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))){
						LOGGER.info("UUID {}",orderIllSite.getSiteCode());
						secondaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()+"-SEC"));
						secondaryserviceDetail.setOrderType(odrOrder.getOrderType());
						secondaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
					}else if("CANCELLATION".equalsIgnoreCase(odrOrder.getOrderType())) {
						LOGGER.info("CANCELLATION UUID {}",orderIllSite.getSiteCode());
						secondaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()+"-SEC"));

					} else if("TERMINATION".equalsIgnoreCase(odrOrder.getOrderType())) {
						LOGGER.info("TERMINATION UUID {}",orderIllSite.getSiteCode());
						secondaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()+"-SEC"));

					}else if("RENEWALS".equals(odrOrder.getOrderType())){
						LOGGER.info("UUID {}",orderIllSite.getSiteCode());
						secondaryserviceDetail.setUuid(orderIllSite.getErfServiceInventoryTpsServiceId());
						secondaryserviceDetail.setOrderType(odrOrder.getOrderType());
						//primaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
					} 
					if(flowMapper.get("amended_o2c_serviceId-sec-"+orderIllSite.getSiteCode()) !=null) {
						secondaryserviceDetail.setAmendedServiceId(flowMapper.get("amended_o2c_serviceId-sec-"+orderIllSite.getSiteCode()));
					}
					List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialListSecondary = quoteSiteDifferentialCommercialRepository.findByQuoteSiteCodeAndServiceType(orderIllSite.getSiteCode(), "secondary");
					if(quoteSiteDifferentialCommercialListSecondary != null && !quoteSiteDifferentialCommercialListSecondary.isEmpty()) {
						LOGGER.info("quote site differential commercial order site code {} diff mrc {}, diff nrc {}", orderIllSite.getSiteCode(), quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialMrc(), quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialNrc());
						secondaryserviceDetail.setDifferentialMrc(quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialMrc());
						secondaryserviceDetail.setDifferentialNrc(quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialNrc());
					}
					secondaryOdrComponent.setOdrServiceDetail(secondaryserviceDetail);
					secondaryOdrComponent.setSiteType("A");
					secondaryserviceDetail.setOdrComponents(null);
					secondaryserviceDetail.getOdrComponents().add(secondaryOdrComponent);
					primaryserviceDetail.setServiceRefId(orderIllSite.getSiteCode() + "-PRIM");
					secondaryserviceDetail.setServiceRefId(orderIllSite.getSiteCode() + "-SECO");
					// persistAttachments(flowMapper, username, orderIllSite,
					// secondaryserviceDetail);
					processCommonComponentAttr(String.valueOf(orderIllSite.getId()), secondaryserviceDetail, username,
							flowMapper,secondaryOdrComponent);
					processSiteSla(orderIllSite, secondaryserviceDetail, username);
					/*
					 * String secServiceCode = getServiceCode(flowMapper, cityCode);
					 * secondaryserviceDetail.setUuid(secServiceCode);
					 */
					if (("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType()) || "TERMINATION".equals(odrOrder.getOrderType()) ||
							"RENEWALS".equals(odrOrder.getOrderType())) && !odrOrder.getOpOrderCode().contains("IPC")) {
						if (type != null && (type.equals("secondary") || type.equals("dual"))) {
							LOGGER.info("Into secondary");
							odrServiceDetails.add(secondaryserviceDetail);
						}
					}else {
						odrServiceDetails.add(secondaryserviceDetail);
					}
					
					// primaryserviceDetail.setPriSecServiceLink(secondaryserviceDetail.getUuid());

					primaryserviceDetail.setErfPriSecServiceLinkId(secondaryserviceDetail.getId());
					LOGGER.info("Total Number of seondary Service attributes {}",secondaryserviceDetail.getOdrServiceAttributes().size());
					// secondaryserviceDetail.setUuid(primaryserviceDetail.getUuid());
					LOGGER.info("Secondary attributes is processed for id {}",secondaryserviceDetail.getUuid());
					for (OdrComponent odrComp : secondaryserviceDetail.getOdrComponents()) {
						odrComp.setOdrServiceDetail(secondaryserviceDetail);
						for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
							LOGGER.info("setting secondary reference");
							odrComponentAttribute.setOdrServiceDetail(secondaryserviceDetail);
						}
					}
					//for (OdrServiceAttribute odrComp : secondaryserviceDetail.getOdrServiceAttributes()) {
						//odrComp.setOdrServiceDetail(secondaryserviceDetail);
					//}
					LOGGER.info("Attributes {}",secondaryOdrComponent.getOdrComponentAttributes());
					
					//added for order level sitewise billing info
						if (orderBillinginfo != null) {
							LOGGER.info("inside if multisitebillinginfo id order level:::" + orderBillinginfo.getId());
							if (orderBillinginfo.getEtcCusBillingInfoId() != null) {
								LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								LOGGER.info("inside if billing contact id::" + orderBillinginfo.getEtcCusBillingInfoId());
								String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
										String.valueOf(orderBillinginfo.getEtcCusBillingInfoId()));
								if (StringUtils.isNotBlank(billingContactResponse)) {
									BillingContact billingContact = (BillingContact) Utils
											.convertJsonToObject(billingContactResponse, BillingContact.class);
									if (billingContact !=null  && billingContact.getErfLocationId() != null) {
										LOGGER.info("BILLING CONTACT location id" + billingContact.getErfLocationId());
										String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
												String.valueOf(billingContact.getErfLocationId()));
										if (StringUtils.isNotBlank(locationResponse)) {
											AddressDetail addressDetail = (AddressDetail) Utils
													.convertJsonToObject(locationResponse, AddressDetail.class);
											if (addressDetail != null) {
												odrComponentAttributes.add(persistOdrComponentAttributes(secondaryserviceDetail, secondaryOdrComponent, "billingAddressLineOne", addressDetail.getAddressLineOne()));
												odrComponentAttributes.add(persistOdrComponentAttributes(secondaryserviceDetail, secondaryOdrComponent, "billingAddressLineTwo", addressDetail.getAddressLineTwo()));
												odrComponentAttributes.add(persistOdrComponentAttributes(secondaryserviceDetail, secondaryOdrComponent, "billingAddressLineThree",""));
												odrComponentAttributes.add(persistOdrComponentAttributes(secondaryserviceDetail, secondaryOdrComponent, "billingAddressCity", addressDetail.getCity()));
												odrComponentAttributes.add(persistOdrComponentAttributes(secondaryserviceDetail, secondaryOdrComponent, "billingAddressState", addressDetail.getState()));
												odrComponentAttributes.add(persistOdrComponentAttributes(secondaryserviceDetail, secondaryOdrComponent, "billingAddressCountry", addressDetail.getCountry()));
												odrComponentAttributes.add(persistOdrComponentAttributes(secondaryserviceDetail, secondaryOdrComponent, "billingAddressPincode", addressDetail.getPincode()));

											}
										}
									}
								}
							}
						}
					
				}
				for (OdrComponent odrComp : primaryserviceDetail.getOdrComponents()) {
					odrComp.setOdrServiceDetail(primaryserviceDetail);
					for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
						LOGGER.info("setting primary reference");
						odrComponentAttribute.setOdrServiceDetail(primaryserviceDetail);
					}
				}
				//for (OdrServiceAttribute odrComp : primaryserviceDetail.getOdrServiceAttributes()) {
					//odrComp.setOdrServiceDetail(primaryserviceDetail);
				//}
				
			} catch (Exception e) {
				LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
			}
			}
		}
	}

	private void persistComponentAttrForTermination(OdrServiceDetail primaryserviceDetail,
			OdrComponent primaryOdrComponent, Set<OdrComponentAttribute> odrComponentAttributes,
			OrderIllSiteToService orderIllSiteToService) {
		List<SIServiceDetailsBean> serviceDetailsList = null;
		String orderDetailsQueueResponse = null;
		try {
			orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsInactiveQueue, orderIllSiteToService.getErfServiceInventoryTpsServiceId());
		} catch (Exception e) {
			LOGGER.info("Exception when trying to get service details from service inventory queue call {}", e);
		}
		if(Objects.nonNull(orderDetailsQueueResponse)){
			LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}", orderDetailsQueueResponse,orderIllSiteToService.getErfServiceInventoryTpsServiceId());
		}

		SIServiceDetailsBean[] serviceDetailBeanArray = null;
		try {
			serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
		} catch (TclCommonException e) {
			LOGGER.info("Exception when trying to get service details from service inventory queue call {}", e);
		}
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		if(serviceDetailsList != null) {
			serviceDetailsList.stream().forEach(serviceDetail -> {
				
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "contractEndDate",serviceDetail.getCircuitExpiryDate() != null ? DateUtil.convertDateToString(serviceDetail.getCircuitExpiryDate()) : null));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "contractStartDate",serviceDetail.getContractStartDate() != null ? DateUtil.convertDateToString(serviceDetail.getContractStartDate()) : null));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "orderTermInMonths",serviceDetail.getContractTerm().toString()));
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "billingType", serviceDetail.getBillingType()));

				serviceDetail.getComponentBean().stream().forEach(component -> {
						component.getSiComponentAttributes().stream().forEach(compAttr -> {
							if ("endMuxNodeIp".equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "endMuxNodeIp",
										compAttr.getAttributeValue()));
							} else if ("endMuxNodeName"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "endMuxNodeName",
										compAttr.getAttributeValue()));
							} else if ("endMuxNodePort"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "endMuxNodePort",
										compAttr.getAttributeValue()));
							} else if ("offnetSupplierBillStartDate"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "offnetSupplierBillStartDate",
										compAttr.getAttributeValue()));
							} else if ("mastPoNumber"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "mastPoNumber",
										compAttr.getAttributeValue()));
							} else if ("mastPoDate"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "mastPoDate",
										compAttr.getAttributeValue()));
							}  else if ("lastMileScenario"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "lastMileScenario",
										compAttr.getAttributeValue()));
							}  else if ("lmConnectionType"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "lmConnectionType",
										compAttr.getAttributeValue()));
							}  else if ("rfMake"
									.equalsIgnoreCase(compAttr.getAttributeName())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "rfMake",
										compAttr.getAttributeValue()));
							}

						});
					
					
				});
				
				if(serviceDetail.getAttributes() != null && !serviceDetail.getAttributes().isEmpty()) {
					serviceDetail.getAttributes().stream().forEach(attribute ->{
						if("A_END_BACKHAUL_PROVIDER".equalsIgnoreCase(attribute.getName()) || "B_END_BACKHAUL_PROVIDER".equalsIgnoreCase(attribute.getName())) {
							if(StringUtils.isNotBlank(attribute.getValue())) {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "offnetBackHaul",
										"Yes"));
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "backHaulProviderName",
										attribute.getValue()));									
								
							} else {
								odrComponentAttributes.add(persistOdrComponentAttributes(
										primaryserviceDetail, primaryOdrComponent, "offnetBackHaul","No"));
							}
						}
					});
					}
				
			});
			
		
		}
	}



	/**
	 * persistOdrComponent
	 * @param primaryserviceDetail
	 * @param odrComponentAttributes
	 * @return
	 */
	private OdrComponent persistOdrComponent(OdrServiceDetail primaryserviceDetail) {
		OdrComponent odrComponent=new OdrComponent();
		odrComponent.setComponentName("LM");
		odrComponent.setOdrServiceDetail(primaryserviceDetail);
		odrComponent.setCreatedBy(Utils.getSource());
		odrComponent.setCreatedDate(new Date());
		//odrComponent.setSiteType(siteType);
		odrComponent.setIsActive(CommonConstants.Y);
		odrComponent.setUuid(Utils.generateUid());
		odrComponent.setOdrComponentAttributes(new HashSet<>());
		return odrComponent;
	}

	/**
	 * 
	 * persistOdrComponentAttributes- persists the component Attr
	 * @param odrServiceDetail
	 * @param odrComponent
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	private OdrComponentAttribute persistOdrComponentAttributes(OdrServiceDetail odrServiceDetail,
			OdrComponent odrComponent, String attrName, String attrValue) {
		OdrComponentAttribute odrComponentAttribute = null;
		if (odrComponent.getId() != null) {
			odrComponentAttribute = odrComponentAttributeRepository.findByOdrComponentAndAttributeValue(odrComponent,
					attrName);
			if (odrComponentAttribute == null) {
				odrComponentAttribute = new OdrComponentAttribute();
				odrComponentAttribute.setCreatedBy(Utils.getSource());
				odrComponentAttribute.setCreatedDate(new Date());
			} else {
				odrComponentAttribute.setUpdatedBy(Utils.getSource());
				odrComponentAttribute.setUpdatedDate(new Date());
			}
		} else {
			odrComponentAttribute = new OdrComponentAttribute();
			odrComponentAttribute.setCreatedBy(Utils.getSource());
			odrComponentAttribute.setCreatedDate(new Date());
		}
		odrComponentAttribute.setOdrComponent(odrComponent);
		odrComponentAttribute.setAttributeName(attrName);
		odrComponentAttribute.setAttributeValue(attrValue);
		odrComponentAttribute.setIsActive(CommonConstants.Y);
		odrComponentAttribute.setOdrServiceDetail(odrServiceDetail);
		odrComponentAttribute.setUuid(Utils.generateUid());
		odrComponentAttribute.setAttributeAltValueLabel(attrValue);
		return odrComponentAttribute;
	}

	/**
	 * persistAttachments
	 * 
	 * @param flowMapper
	 * @param username
	 * @param orderIllSite
	 * @param primaryserviceDetail
	 * @throws TclCommonException
	 */
	private void persistAttachments(Map<String, String> flowMapper, String username, OrderIllSite orderIllSite,
			OdrServiceDetail primaryserviceDetail) throws TclCommonException {
		List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByReferenceNameAndReferenceId("Site",
				orderIllSite.getId());
		for (OmsAttachment omsAttachment : omsAttachments) {
			Integer erfAttachmentId = omsAttachment.getErfCusAttachmentId();
			LOGGER.info("MDC Filter token value in before Queue call getAttachment {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
					String.valueOf(erfAttachmentId));
			if (attachmentResponse != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);
				if (attachmentMapper != null) {
					Attachment attachment = new Attachment();
					attachment.setCreatedBy(username);
					attachment.setCreatedDate(new Timestamp(new Date().getTime()));
					attachment.setIsActive(CommonConstants.Y);
					attachment.setName((String) attachmentMapper.get("NAME"));
					attachment.setType(omsAttachment.getAttachmentType());
					attachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
					attachmentRepository.save(attachment);
					OdrAttachment odrAttachment = new OdrAttachment();
					odrAttachment.setAttachmentId(attachment.getId());
					odrAttachment.setAttachmentType(omsAttachment.getAttachmentType());
					odrAttachment.setIsActive(CommonConstants.Y);
					odrAttachment.setOfferingName(flowMapper.get("offeringName"));
					odrAttachment.setProductName(flowMapper.get("productName"));
					// odrAttachment.setServiceCode(primaryserviceDetail.getUuid());
					odrAttachment.setOdrServiceDetail(primaryserviceDetail);
					primaryserviceDetail.getOdrAttachments().add(odrAttachment);
					// odrAttachmentRepository.save(odrAttachment);
				}

			}

		}
	}

	/**
	 * getServiceCode
	 * 
	 * @param flowMapper
	 * @param cityCode
	 * @return
	 */
	private String getServiceCode(Map<String, String> flowMapper, String cityCode) {
		String prodCode = CommonConstants.EMPTY;
		if (flowMapper.get("productName").equals("IAS")) {
			prodCode = ILL_CODE;
		} else if (flowMapper.get("productName").equals("IPC")) {
			prodCode = IPC_CODE;
		} else {
			prodCode = GVPN_CODE;
		}
		if (cityCode == null)
			cityCode = "";
		String primaryKey = "";
		String serviceCode = "091" + cityCode.toUpperCase() + prodCode + "A";
		OdrServiceDetail odrServiceDetail = odrServiceDetailRepository.findTopByOrderByIdDesc();
		if (odrServiceDetail != null) {
			primaryKey = String.valueOf(odrServiceDetail.getId() + 1);
		} else {
			primaryKey = "1";
		}

		if (serviceCode.length() + primaryKey.length() < 19) {
			int length = 19 - (serviceCode.length() + primaryKey.length());

			StringBuilder stringBuilder = new StringBuilder();
			IntStream.range(0, length).forEach(value -> stringBuilder.append(0));
			serviceCode = serviceCode + stringBuilder + primaryKey;
		} else {
			serviceCode = serviceCode + primaryKey;

		}

		// serviceCode = serviceCode + Utils.generateUid(19 - serviceCode.length());
		return serviceCode;
	}

	/**
	 * processSiteSla
	 * 
	 * @param orderIllSite
	 * @param primaryserviceDetail
	 */
	private void processSiteSla(OrderIllSite orderIllSite, OdrServiceDetail primaryserviceDetail, String username) {
		List<Map<String, String>> siteSlas = orderIllSiteSlaRepository.findByOrderIllSiteId(orderIllSite.getId());
		Set<OdrServiceSla> odrServiceSlas = new HashSet<>();
		for (Map<String, String> siteSla : siteSlas) {
			String attrN = siteSla.get("sla_name");
			String attrVa = siteSla.get("sla_value");
			OdrServiceSla odrServiceSla = new OdrServiceSla();
			odrServiceSla.setCreatedBy(username);
			odrServiceSla.setCreatedTime(new Date());
			odrServiceSla.setIsActive(CommonConstants.Y);
			odrServiceSla.setOdrServiceDetail(primaryserviceDetail);
			odrServiceSla.setSlaComponent(attrN);
			odrServiceSla.setSlaValue(attrVa);
			odrServiceSla.setUpdatedBy(username);
			odrServiceSla.setUpdatedTime(new Date());
			odrServiceSlas.add(odrServiceSla);
		}
		primaryserviceDetail.setOdrServiceSlas(odrServiceSlas);
	}

	private void processCloudSla(OrderCloud orderCloud, OdrServiceDetail primaryserviceDetail, String username) {
		Set<OdrServiceSla> odrServiceSlas = new HashSet<>();
		// TO DO
		primaryserviceDetail.setOdrServiceSlas(odrServiceSlas);
	}

	public void processCommercialComponent(String refId, OdrProductDetail odrProductDetail, String username,
			Map<String, String> flowMapper) throws TclCommonException, IllegalArgumentException {
		int quoteId = Integer.parseInt(flowMapper.get("quoteId"));
		LOGGER.info("Processing Commercial Component for reference ID {} and reference Name {} and Quote ID {} ", refId,
				getReferenceName(), quoteId);
		List<Map<String, Object>> oderProdCompAttrs = orderProductComponentRepository.findByRefIdAndRefNameAndQuoteId(refId,
				getReferenceName(), quoteId); 
		for (Map<String, Object> oderProdCompAttr : oderProdCompAttrs) {
			String itemType = oderProdCompAttr.get("item_type") + "";
			String item = oderProdCompAttr.get("item") + "";
			if(CommonConstants.HYBRID_CONNECTION.equalsIgnoreCase(item)) {
				item += "_" + String.valueOf(oderProdCompAttr.get("id"));
			}
			Double nrc = (Double) oderProdCompAttr.get("nrc");
			Double mrc = (Double) oderProdCompAttr.get("mrc");
			Double arc = (Double) oderProdCompAttr.get("arc");

			OdrServiceCommercialComponent odrServiceCommercialComponent = new OdrServiceCommercialComponent();
			odrServiceCommercialComponent.setItemType(itemType);
			odrServiceCommercialComponent.setItem(item);
			odrServiceCommercialComponent.setNrc(nrc);
			odrServiceCommercialComponent.setMrc(mrc);
			odrServiceCommercialComponent.setArc(arc);
			odrServiceCommercialComponent.setCreatedBy(username);
			odrServiceCommercialComponent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			odrServiceCommercialComponent.setOdrProductDetail(odrProductDetail);
			odrProductDetail.addOdrServiceCommercialComponent(odrServiceCommercialComponent);
		}
	}

	public void processIpcCommonComponentAttr(OrderCloud orderCloud, OdrServiceDetail serviceDetail, String username,
			Map<String, String> flowMapper) throws TclCommonException, IllegalArgumentException {

		// LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference
		// Id {}", refId, getReferenceName());

		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefNameForIpc(orderCloud.getId().toString(), getReferenceName());

		String solution = orderCloud.getResourceDisplayName();
		String type = CLOUD;

		if (solution.toUpperCase().contains(ACCESS)) {
			type = ACCESS;
		} else if (solution.toUpperCase().contains(ADDON)) {
			type = ADDON;
		}

		OdrProductDetail odrProductDetail = new OdrProductDetail();
		odrProductDetail.setType(type);
		odrProductDetail.setSolutionName(solution);
		odrProductDetail.setMrc(orderCloud.getMrc());
		odrProductDetail.setNrc(orderCloud.getNrc());
		odrProductDetail.setArc(orderCloud.getArc());
		odrProductDetail.setPpuRate(orderCloud.getPpuRate());
		odrProductDetail.setCreatedBy(username);
		odrProductDetail.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		odrProductDetail.setIsActive(CommonConstants.Y);
		odrProductDetail.setCloudCode(orderCloud.getCloudCode());
		odrProductDetail.setParentCloudCode(orderCloud.getParentCloudCode());
		processCommercialComponent(String.valueOf(orderCloud.getId()), odrProductDetail, username, flowMapper);

		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			if(CommonConstants.HYBRID_CONNECTION.equalsIgnoreCase(category)) {
				category += "_" + String.valueOf(oderProdCompAttr.get("id"));
			}
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
				flowMapper.put("siteGstNumber", attrValue);
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
			} else if (attrName.equals("CPE Basic Chassis") && StringUtils.isNotBlank(attrValue)) {
				List<String> list = new ArrayList<String>();
				list.add(attrValue);
				LOGGER.info("MDC Filter token value in before Queue call product BOM Details {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("calling the product with request {}", list);
				String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue,
						Utils.convertObjectToJson(list));
				if (StringUtils.isNotBlank(productBomDetailsStr)) {
					LOGGER.info("response received product with response {}", productBomDetailsStr);
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("cpe-bom-resource");
					odrAdditionalServiceParam.setCategory("CPE-BOM");
					odrAdditionalServiceParam.setCreatedBy(username);
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(productBomDetailsStr);
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					OdrProductDetailAttributes odrProductDetailAttributes = new OdrProductDetailAttributes();
					odrProductDetailAttributes.setAttributeName(attrName);
					odrProductDetailAttributes
							.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					odrProductDetailAttributes.setCategory(category);
					odrProductDetailAttributes.setCreatedBy(username);
					odrProductDetailAttributes.setCreatedDate(new Date());
					odrProductDetailAttributes.setIsActive(CommonConstants.Y);
					odrProductDetailAttributes.setOdrProductDetail(odrProductDetail);
					odrProductDetailAttributes.setUpdatedBy(username);
					odrProductDetailAttributes.setUpdatedDate(new Date());
					odrProductDetail.getOdrProductDetailAttributes().add(odrProductDetailAttributes);
				}
			} else if (attrName.equals("TAX_EXCEMPTED_ATTACHMENTID") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("Entering the tax exception with attachmentId {}", attrValue);
				Integer erfAttachmentId = Integer.valueOf(attrValue);
				LOGGER.info("MDC Filter token value in before Queue call getAttachment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
						String.valueOf(erfAttachmentId));
				LOGGER.info("Response received from attachment queue {}", attachmentResponse);
				if (attachmentResponse != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);
					if (attachmentMapper != null) {
						Attachment attachment = new Attachment();
						attachment.setCreatedBy(username);
						attachment.setCreatedDate(new Timestamp(new Date().getTime()));
						attachment.setIsActive(CommonConstants.Y);
						attachment.setName((String) attachmentMapper.get("NAME"));
						attachment.setType("Tax");
						attachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
						attachmentRepository.save(attachment);
						OdrAttachment odrAttachment = new OdrAttachment();
						odrAttachment.setAttachmentId(attachment.getId());
						odrAttachment.setAttachmentType("Tax");
						odrAttachment.setIsActive(CommonConstants.Y);
						odrAttachment.setOfferingName(flowMapper.get("offeringName"));
						odrAttachment.setProductName(flowMapper.get("productName"));
						// odrAttachment.setServiceCode(serviceDetail.getUuid());
						odrAttachmentRepository.save(odrAttachment);
					}

				}
			} else if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
				if (localItResponse != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
							Map.class);
					serviceDetail.setLocalItContactEmail(
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
					serviceDetail.setLocalItContactMobile(
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
					serviceDetail.setLocalItContactName(
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
				}
			} else {
				OdrProductDetailAttributes odrProductDetailAttributes = new OdrProductDetailAttributes();
				odrProductDetailAttributes.setAttributeName(attrName);
				odrProductDetailAttributes.setAttributeValue(attrValue);
				odrProductDetailAttributes.setCategory(category);
				odrProductDetailAttributes.setCreatedBy(username);
				odrProductDetailAttributes.setCreatedDate(new Date());
				odrProductDetailAttributes.setIsActive(CommonConstants.Y);
				odrProductDetailAttributes.setOdrProductDetail(odrProductDetail);
				odrProductDetailAttributes.setUpdatedBy(username);
				odrProductDetailAttributes.setUpdatedDate(new Date());
				odrProductDetail.getOdrProductDetailAttributes().add(odrProductDetailAttributes);
			}
		}
		serviceDetail.addOdrProductDetail(odrProductDetail);
	}

	public void processCommonComponentAttr(String refId, OdrServiceDetail serviceDetail, String username,
			Map<String, String> flowMapper,OdrComponent odrComponent) throws TclCommonException, IllegalArgumentException {
		Utils.logMemory();
		LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference Id {}", refId,
				getReferenceName());
		Utils.logMemory();
		String referenceName=getReferenceName();
		if(serviceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN"))
			referenceName=OdrConstants.GVPN_SITES;
		if (serviceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(TeamsDROdrConstants.MICROSOFT_CLOUD_SOLUTIONS))
			referenceName = TeamsDROdrConstants.EQUIPMENT_REF_NAME;
		LOGGER.info("Check point 1");
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, referenceName);
		LOGGER.info("Check point size{}",oderProdCompAttrs.size());
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");
			if(category!=null && category.equals("IZO Private Connect Port")) {
				category="GVPN Common";
			}
			LOGGER.info("Check point 2");
			if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstNumber",attrValue.trim()));
				LOGGER.info("Check point 3");
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					String address = addServiceParam.get().getValue();
					if (StringUtils.isNotBlank(address)) {
						/*GstAddressBean gstAddress = Utils.convertJsonToObject(address, GstAddressBean.class);

						OdrGstAddress odrGstAddress = serviceDetail.getOdrGstAddress();
						if (serviceDetail.getOdrGstAddress() == null) {
							odrGstAddress = new OdrGstAddress();
						}
						odrGstAddress.setBuildingName(gstAddress.getBuildingName());
						odrGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
						odrGstAddress.setCreatedBy(serviceDetail.getCreatedBy());
						odrGstAddress.setCreatedTime(new Timestamp(new Date().getTime()));
						odrGstAddress.setDistrict(gstAddress.getDistrict());
						odrGstAddress.setFlatNumber(gstAddress.getFlatNumber());
						odrGstAddress.setLatitude(gstAddress.getLatitude());
						odrGstAddress.setLocality(gstAddress.getLocality());
						odrGstAddress.setLongitude(gstAddress.getLongitude());
						odrGstAddress.setPincode(gstAddress.getPinCode());
						odrGstAddress.setState(gstAddress.getState());
						odrGstAddress.setStreet(gstAddress.getStreet());
						serviceDetail.setOdrGstAddress(odrGstAddress);*/
						odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstAddress",address));
					} else {
						LOGGER.warn("Address is empty for id {}", attrValue);
					}
					
					//odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstAddressId",attrValue.trim()));
					LOGGER.info("Check point 4");
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstAddressId",attrValue.trim()));
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption",CommonConstants.Y));
			} else if (attrName.equals("CPE Basic Chassis") && StringUtils.isNotBlank(attrValue)) {
				OdrServiceAttribute odrEqupModelAttribute = new OdrServiceAttribute();
				odrEqupModelAttribute.setAttributeAltValueLabel(attrValue);
				odrEqupModelAttribute.setAttributeName("EQUIPMENT_MODEL");
				odrEqupModelAttribute.setAttributeValue(attrValue);
				odrEqupModelAttribute.setCategory(category);
				odrEqupModelAttribute.setCreatedBy(username);
				odrEqupModelAttribute.setCreatedDate(new Date());
				odrEqupModelAttribute.setIsActive(CommonConstants.Y);
				odrEqupModelAttribute.setOdrServiceDetail(serviceDetail);
				odrEqupModelAttribute.setUpdatedBy(username);
				odrEqupModelAttribute.setUpdatedDate(new Date());
				serviceDetail.getOdrServiceAttributes().add(odrEqupModelAttribute);
				List<String> list = new ArrayList<String>();
				list.add(attrValue);
				LOGGER.info("MDC Filter token value in before Queue call product BOM Details {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("calling the product with request {}", list);
				// String cpeString = list.stream().collect(Collectors.joining(","));
				String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue,Utils.convertObjectToJson(list));
				/*
				 * String productBomDetailsStr = (String)
				 * mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString); String
				 * modifiedProductBomDetailsStr=
				 * productBomDetailsStr.replaceFirst("cpeBomDetails", "resources");
				 * LOGGER.info("productBomDetailsStr {}", modifiedProductBomDetailsStr);
				 */
				if (StringUtils.isNotBlank(productBomDetailsStr)) {
					LOGGER.info("response received product with response {}", productBomDetailsStr);
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("cpe-bom-resource");
					odrAdditionalServiceParam.setCategory("CPE-BOM");
					odrAdditionalServiceParam.setCreatedBy(username);
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(productBomDetailsStr);
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					/*OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
							.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
									category);
					if (odrServiceAttribute == null) {*/
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
					/*
					 * }else { LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId()); }
					 */
					
				}
			} else if (attrName.equals("TAX_EXCEMPTED_ATTACHMENTID") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("Entering the tax exception with attachmentId {}", attrValue);
				Integer erfAttachmentId = Integer.valueOf(attrValue);
				LOGGER.info("MDC Filter token value in before Queue call getAttachment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
						String.valueOf(erfAttachmentId));
				LOGGER.info("Response received from attachment queue {}", attachmentResponse);
				if (attachmentResponse != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);
					if (attachmentMapper != null) {
						Attachment attachment = new Attachment();
						attachment.setCreatedBy(username);
						attachment.setCreatedDate(new Timestamp(new Date().getTime()));
						attachment.setIsActive(CommonConstants.Y);
						attachment.setName((String) attachmentMapper.get("NAME"));
						attachment.setType("Tax");
						attachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
						attachmentRepository.save(attachment);
						OdrAttachment odrAttachment = new OdrAttachment();
						odrAttachment.setAttachmentId(attachment.getId());
						odrAttachment.setAttachmentType("Tax");
						odrAttachment.setIsActive(CommonConstants.Y);
						odrAttachment.setOfferingName(flowMapper.get("offeringName"));
						odrAttachment.setProductName(flowMapper.get("productName"));
						// odrAttachment.setServiceCode(serviceDetail.getUuid());

						odrAttachment.setOdrServiceDetail(serviceDetail);
						serviceDetail.getOdrAttachments().add(odrAttachment);
						// odrAttachmentRepository.save(odrAttachment);
					}

				}
			} else if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
				if (localItResponse != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
							Map.class);
					serviceDetail.setLocalItContactEmail(
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
					serviceDetail.setLocalItContactMobile(
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
					serviceDetail.setLocalItContactName(
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
			} else {
				//OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
				//		.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
					//			category);
				//if (odrServiceAttribute == null) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(attrValue);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
			//	}else{
				//	LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
			//	}
				
			}

		}
	}

	public OdrServiceDetail processOEProductComponentAttr(String refId, OdrServiceDetail serviceDetail, String type,
			String userName) throws TclCommonException, IllegalArgumentException, ParseException {

		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, type, getReferenceName());
		if (oderProdCompAttrs.isEmpty()) {
			return null;
		}
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");
			//OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
			//		.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(), category);
			//if (odrServiceAttribute == null) {
			LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
				OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
				odrServiceAttribute.setAttributeAltValueLabel(attrValue);
				odrServiceAttribute.setAttributeName(attrName);
				odrServiceAttribute.setAttributeValue(attrValue);
				odrServiceAttribute.setCategory(category);
				odrServiceAttribute.setCreatedBy(userName);
				odrServiceAttribute.setCreatedDate(new Date());
				odrServiceAttribute.setIsActive(CommonConstants.Y);
				odrServiceAttribute.setOdrServiceDetail(serviceDetail);
				odrServiceAttribute.setUpdatedBy(userName);
				odrServiceAttribute.setUpdatedDate(new Date());
				serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
		//	}else{
		//		LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
		//	}
			
		}
		return serviceDetail;
	}

	@SuppressWarnings("unchecked")
	public OdrServiceDetail processProductComponentAttr(String refId, OdrServiceDetail serviceDetail, String type,
			String userName, boolean isUpdate, List<OdrServiceCommercial> odrServiceCommercials,OdrComponent odrComponent)
			throws TclCommonException, IllegalArgumentException, ParseException {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(Integer.valueOf(refId), type);
		for (OrderProductComponent orderProductComponent : orderProductComponents) {
			OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					orderProductComponent.getId() + CommonConstants.EMPTY, "COMPONENTS");
			if (orderPrice != null) {
				String categoryName = orderProductComponent.getMstProductComponent().getName();
				String arcAttrName = categoryName + "_Arc_Price";
				String nrcAttrName = categoryName + "_Nrc_Price";
				addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveArc(), categoryName, arcAttrName);
				addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveNrc(), categoryName, nrcAttrName);
				OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
				odrServiceCommercial.setArc(orderPrice.getEffectiveArc());
				odrServiceCommercial.setMrc(orderPrice.getEffectiveMrc());
				odrServiceCommercial.setNrc(orderPrice.getEffectiveNrc());
				odrServiceCommercial.setReferenceName(orderProductComponent.getMstProductComponent().getName());
				odrServiceCommercial.setReferenceType("COMPONENTS");
				odrServiceCommercial.setOdrServiceDetail(serviceDetail);
				odrServiceCommercial.setServiceType(type);
				odrServiceCommercials.add(odrServiceCommercial);
			}

			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponent
					.getOrderProductComponentsAttributeValues()) {
				OrderPrice attrOrderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
						orderProductComponentsAttributeValue.getId() + CommonConstants.EMPTY, "ATTRIBUTES");
				if (attrOrderPrice != null && (attrOrderPrice.getEffectiveArc() != null
						|| attrOrderPrice.getEffectiveMrc() != null || attrOrderPrice.getEffectiveNrc() != null)) {
					OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
					odrServiceCommercial.setArc(attrOrderPrice.getEffectiveArc());
					odrServiceCommercial.setMrc(attrOrderPrice.getEffectiveMrc());
					odrServiceCommercial.setNrc(attrOrderPrice.getEffectiveNrc());
					odrServiceCommercial
							.setComponentReferenceName(orderProductComponent.getMstProductComponent().getName());
					odrServiceCommercial.setReferenceName(
							orderProductComponentsAttributeValue.getProductAttributeMaster().getName());
					odrServiceCommercial.setReferenceType("ATTRIBUTES");
					odrServiceCommercial.setOdrServiceDetail(serviceDetail);
					odrServiceCommercial.setServiceType(type);
					odrServiceCommercials.add(odrServiceCommercial);
				}
				if (attrOrderPrice != null && attrOrderPrice.getEffectiveUsagePrice() != null && attrOrderPrice.getEffectiveUsagePrice() > 0 ) {
					addServiceAttr(serviceDetail, userName, attrOrderPrice.getEffectiveUsagePrice(),
							orderProductComponent.getMstProductComponent().getName(), "usage_price_per_mb");
					/*odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "eventSource", getEventSourceforBurstable(serviceDetail)));*/
				}

			}

		}

		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, type, getReferenceName());
		if (oderProdCompAttrs.isEmpty()) {
			LOGGER.info("Secondary not available for {} ,{} ,{}",refId, type, getReferenceName());
			return null;
		}
		serviceDetail.setPrimarySecondary(type);
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");
			if(category!=null && category.equals("IZO Private Connect Port")) {
				category="GVPN Common";
			}
			if (attrName.equals("Burstable Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBurstableBwPortspeed(attrValue.trim());
				serviceDetail.setBurstableBwPortspeedAltName(attrValue.trim());
				serviceDetail.setBurstableBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBandwidth",attrValue.trim()));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBwUnit","Mbps"));
			} else if ((attrName.equals("Port Bandwidth") && StringUtils.isNotBlank(attrValue))
					|| (attrName.equals("Bandwidth") && StringUtils.isNotBlank(attrValue))) {
				serviceDetail.setBwPortspeed(attrValue.trim());
				serviceDetail.setBwPortspeedAltName(attrValue.trim());
				serviceDetail.setBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "portBandwidth",attrValue.trim()));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "bwUnit","Mbps"));
			} else if (attrName.equals("Local Loop Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setLastmileBw(attrValue.trim());
				serviceDetail.setLastmileBwAltName(attrValue.trim());
				serviceDetail.setLastmileBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localLoopBandwidth",attrValue.trim()));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localLoopBandwidthUnit","Mbps"));
			} else if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue.trim());
				
			} else if (attrName.equals("CPE Basic Chassis") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call product BOM Details {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("calling the product with request {}", attrValue);
				/*
				 * List<String> list = new ArrayList<>(); list.add(attrValue);
				 */
				String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue, attrValue);
				/*
				 * String cpeString = list.stream().collect(Collectors.joining(","));
				 * 
				 * String productBomDetailsStr = (String)
				 * mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString); String
				 * modifiedProductBomDetailsStr=
				 * productBomDetailsStr.replaceFirst("cpeBomDetails", "resources");
				 * LOGGER.info("productBomDetailsStr {}", modifiedProductBomDetailsStr);
				 */
				if (StringUtils.isNotBlank(productBomDetailsStr)) {
					LOGGER.info("response received product with response {}", productBomDetailsStr);
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("cpe-bom-resource");
					odrAdditionalServiceParam.setCategory("CPE-BOM");
					odrAdditionalServiceParam.setCreatedBy(userName);
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(productBomDetailsStr);
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					//OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
					//		.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
							//		category);
					//if (odrServiceAttribute == null) {
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
						OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
						odrServiceAttribute.setAttributeAltValueLabel(attrValue);
						odrServiceAttribute.setAttributeName(attrName);
						odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
						odrServiceAttribute.setCategory(category);
						odrServiceAttribute.setCreatedBy(userName);
						odrServiceAttribute.setCreatedDate(new Date());
						odrServiceAttribute.setIsActive(CommonConstants.Y);
						odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
						odrServiceAttribute.setOdrServiceDetail(serviceDetail);
						odrServiceAttribute.setUpdatedBy(userName);
						odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				//	}else{
					//	LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
					//}
					
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption",CommonConstants.Y));
			} else if (attrName.equals("Interface") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setSiteEndInterface(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "interface",attrValue));
			} else if (attrName.equals("CPE Management Type") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setServiceOption(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeManagementType",attrValue));
			} else if (attrName.equals("Type of Peering") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, "Cloud Type", attrValue));
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, attrName, attrValue));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "Cloud Type",attrValue));
			} else if (attrName.equals("COS Profile") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributesWithCategory(serviceDetail, "Service Variant", attrValue,"GVPN Common"));
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, attrName, attrValue));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "Service Variant",attrValue));
			} else if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
				Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
						Map.class);
				if (localItDetail != null) {
					serviceDetail.setLocalItContactEmail(
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
					serviceDetail.setLocalItContactMobile(
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
					serviceDetail.setLocalItContactName(
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
					LOGGER.info("Local IT Contact Received {}", localItDetail);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
			} else {
				//OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
					//	.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
						//		category);
				//if (odrServiceAttribute == null) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(attrValue);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(userName);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(userName);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				//}else{
					//LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
				//}
			}

		}
		if (!isUpdate)
			processSiteFeasibility(refId, serviceDetail, type, userName,odrComponent);
		return serviceDetail;
	}

	/**
	 * addServiceAttr
	 * 
	 * @param serviceDetail
	 * @param userName
	 * @param orderPrice
	 * @param categoryName
	 * @param arcAttrName
	 */
	private void addServiceAttr(OdrServiceDetail serviceDetail, String userName, Double orderPrice, String categoryName,
			String arcAttrName) {
		OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
				.findByAttributeNameAndOdrServiceDetail_IdAndCategory(arcAttrName, serviceDetail.getId(), categoryName);
		if (odrServiceAttribute == null) {
			odrServiceAttribute = new OdrServiceAttribute();
			odrServiceAttribute.setAttributeAltValueLabel(arcAttrName);
			odrServiceAttribute.setAttributeName(arcAttrName);
			odrServiceAttribute.setAttributeValue(orderPrice + CommonConstants.EMPTY);
			odrServiceAttribute.setCategory(categoryName);
			odrServiceAttribute.setCreatedBy(userName);
			odrServiceAttribute.setCreatedDate(new Date());
			odrServiceAttribute.setIsActive(CommonConstants.Y);
			odrServiceAttribute.setOdrServiceDetail(serviceDetail);
			odrServiceAttribute.setUpdatedBy(userName);
			odrServiceAttribute.setUpdatedDate(new Date());
			serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
		}
		
	}

	@SuppressWarnings("unchecked")
	private OdrServiceDetail processLocalItContact(String refId, OdrServiceDetail serviceDetail, String username,OdrComponent odrComponent)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Starting to save the localItContact flow with refId {} refname {}", refId, getReferenceName());
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, getReferenceName());
		LOGGER.info("All the Attributes retrived {}", oderProdCompAttrs);
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");
			String category = oderProdCompAttr.get("category");
			if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
				Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
						Map.class);
				if (localItDetail != null) {
					serviceDetail.setLocalItContactEmail(
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
					serviceDetail.setLocalItContactMobile(
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
					serviceDetail.setLocalItContactName(
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
					LOGGER.info("Local IT Contact Received {}", localItDetail);
					
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
			} else if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstNumber",attrValue.trim()));
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					String address = addServiceParam.get().getValue();
					/*GstAddressBean gstAddress = Utils.convertJsonToObject(address, GstAddressBean.class);

					OdrGstAddress odrGstAddress = serviceDetail.getOdrGstAddress();
					if (serviceDetail.getOdrGstAddress() == null) {
						odrGstAddress = new OdrGstAddress();
					}
					odrGstAddress.setBuildingName(gstAddress.getBuildingName());
					odrGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
					odrGstAddress.setCreatedBy(serviceDetail.getCreatedBy());
					odrGstAddress.setCreatedTime(new Timestamp(new Date().getTime()));
					odrGstAddress.setDistrict(gstAddress.getDistrict());
					odrGstAddress.setFlatNumber(gstAddress.getFlatNumber());
					odrGstAddress.setLatitude(gstAddress.getLatitude());
					odrGstAddress.setLocality(gstAddress.getLocality());
					odrGstAddress.setLongitude(gstAddress.getLongitude());
					odrGstAddress.setPincode(gstAddress.getPinCode());
					odrGstAddress.setState(gstAddress.getState());
					odrGstAddress.setStreet(gstAddress.getStreet());
					serviceDetail.setOdrGstAddress(odrGstAddress);*/
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstAddress",address));
				//	odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstAddressId",attrValue.trim()));
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption",CommonConstants.Y));
			} else {
			//	OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
				//		.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
					//			category);
				//if (odrServiceAttribute == null) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(attrValue);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
			//	}else{
			//		LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
			//	}
				
			}

		}
		return serviceDetail;
	}

	/**
	 * processSiteFeasibility
	 * 
	 * @param refId
	 * @param serviceDetail
	 * @param type
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes" })
	public void processSiteFeasibility(String refId, OdrServiceDetail serviceDetail, String type, String userName,OdrComponent odrComponent)
			throws ParseException {
		List<OrderSiteFeasibility> orderSiteFeasibilitys = orderSiteFeasibilityRepository
				.findByOrderIllSiteIdAndIsSelectedAndType(Integer.valueOf(refId), CommonConstants.BACTIVE, type);
		for (OrderSiteFeasibility orderSiteFeasibility : orderSiteFeasibilitys) {
			
			String lmType = orderSiteFeasibility.getFeasibilityMode();
			
			if(lmType.equalsIgnoreCase("Onnet Wireless"))lmType="OnnetRF";
			else if(lmType.equalsIgnoreCase("Onnet Wireline"))lmType="OnnetWL";
			else if(lmType.equalsIgnoreCase("Offnet Wireless"))lmType="OffnetRF";
			else if(lmType.equalsIgnoreCase("Offnet Wireline"))lmType="OffnetWL";
			
			serviceDetail.setAccessType(lmType);
			serviceDetail.setLastmileType(lmType);
			serviceDetail.setFeasibilityId(orderSiteFeasibility.getFeasibilityCode());
			serviceDetail.setLastmileProvider(orderSiteFeasibility.getProvider());
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "accessType",lmType));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "lmType",lmType));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "feasibilityId",orderSiteFeasibility.getFeasibilityCode()));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "lastMileProvider",orderSiteFeasibility.getProvider()));
			
			
			if (orderSiteFeasibility.getResponseJson() != null) {
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(orderSiteFeasibility.getResponseJson());
				Iterator<?> iter = jsonObject.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String attrKey = String.valueOf(entry.getKey());

					if ((attrKey.equalsIgnoreCase("access_rings")) || (attrKey.equalsIgnoreCase("mux_details")) || "feasibility_remarks".equalsIgnoreCase(attrKey)) {
						LOGGER.info("access_rings", attrKey);
						OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
						odrAdditionalServiceParam.setAttribute(attrKey);
						odrAdditionalServiceParam.setCategory("FEASIBILITY");
						odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
						odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
						try {
							if (entry.getValue() != null) {
								odrAdditionalServiceParam.setValue(Utils.convertObjectToJson(entry.getValue()));
							} else {
								odrAdditionalServiceParam.setValue(null);
							}
						} catch (TclCommonException e) {
							LOGGER.error("error parsing access_rings:{}", e);
							odrAdditionalServiceParam.setValue(null);
						}
						odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

						OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();

						odrServiceAttribute.setAttributeAltValueLabel(attrKey);
						odrServiceAttribute.setAttributeName(attrKey);
						odrServiceAttribute
								.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
						odrServiceAttribute.setCategory("FEASIBILITY");
						odrServiceAttribute.setCreatedBy(userName);
						odrServiceAttribute.setCreatedDate(new Date());
						odrServiceAttribute.setIsActive(CommonConstants.Y);
						odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
						odrServiceAttribute.setOdrServiceDetail(serviceDetail);
						odrServiceAttribute.setUpdatedBy(userName);
						odrServiceAttribute.setUpdatedDate(new Date());
						serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
					} else {
						if ((attrKey.equalsIgnoreCase("hh_name") || attrKey.equalsIgnoreCase("min_hh_fatg")
								|| attrKey.equalsIgnoreCase("lm_nrc_inbldg_onwl")
								|| attrKey.equalsIgnoreCase("lm_nrc_ospcapex_onwl") || attrKey.equalsIgnoreCase("Type")
								|| attrKey.equalsIgnoreCase("closest_provider_bso_name")
								|| attrKey.equalsIgnoreCase("local_loop_interface")
								|| attrKey.equalsIgnoreCase("lm_nrc_mux_onwl") || attrKey.equalsIgnoreCase("HH_0_5km")
								|| attrKey.equalsIgnoreCase("bw_mbps") || attrKey.equalsIgnoreCase("Type")
								|| attrKey.equalsIgnoreCase("SECTOR_NAME") || attrKey.equalsIgnoreCase("bts_azimuth")
								|| attrKey.equalsIgnoreCase("bts_device_type")
								|| attrKey.equalsIgnoreCase("BHConnectivity") || attrKey.equalsIgnoreCase("BH_Type")
								|| attrKey.equalsIgnoreCase("BH_Capacity") || attrKey.equalsIgnoreCase("bts_long")
								|| attrKey.equalsIgnoreCase("bts_lat") || attrKey.equalsIgnoreCase("sector_id")
								|| attrKey.equalsIgnoreCase("bts_site_name")
								|| attrKey.equalsIgnoreCase("selected_access_ring")
								|| attrKey.equalsIgnoreCase("Avg_3KM_Mast_ht_cost")
								|| attrKey.equalsIgnoreCase("Mast_3KM_avg_mast_ht")
								|| attrKey.equalsIgnoreCase("solution_type") || attrKey.equalsIgnoreCase("bts_IP_PMP")
								|| attrKey.equalsIgnoreCase("Interface") || attrKey.equalsIgnoreCase("bts_num_BTS")
								|| attrKey.equalsIgnoreCase("Selected_solution_BW")
								|| attrKey.equalsIgnoreCase("connected_cust_tag")
								|| attrKey.equalsIgnoreCase("Local Loop Bandwidth")
								|| attrKey.equalsIgnoreCase("connected_building_tag")
								|| attrKey.equalsIgnoreCase("Network_Feasibility_Check")
								|| "primary_bts_ip_address".equalsIgnoreCase(attrKey)
								|| "primary_bts_name".equalsIgnoreCase(attrKey)
								|| "provider_name".equalsIgnoreCase(attrKey)
								|| "primary_first_sector_id".equalsIgnoreCase(attrKey)
								|| "delivery_timeLine".equalsIgnoreCase(attrKey)
								|| "feasibility_response_created_date".equalsIgnoreCase(attrKey)								
								|| "response_type".equalsIgnoreCase(attrKey)
								|| "validity_period".equalsIgnoreCase(attrKey)
								|| "feasibility_response_id".equalsIgnoreCase(attrKey)
								|| "provider_response_date".equalsIgnoreCase(attrKey)
								|| "avg_mast_ht".equalsIgnoreCase(attrKey)
								|| "lm_arc_bw_prov_ofrf".equalsIgnoreCase(attrKey)
								|| "lm_nrc_bw_prov_ofrf".equalsIgnoreCase(attrKey)
								|| "lm_nrc_mast_ofrf".equalsIgnoreCase(attrKey)
								|| "record_type".equalsIgnoreCase(attrKey)		
								|| "mast_type".equalsIgnoreCase(attrKey)
								|| "provider_reference_number".equalsIgnoreCase(attrKey)
								|| "cpe_variant".equalsIgnoreCase(attrKey)
								|| "cpe_chassis_changed".equalsIgnoreCase(attrKey)
								|| "cpe_variant".equalsIgnoreCase(attrKey)
								|| "lm_otc_nrc_installation_offwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_bw_offwl".equalsIgnoreCase(attrKey)
								|| "customer_segment".equalsIgnoreCase(attrKey)
								|| "vendor_name".equalsIgnoreCase(attrKey)
								|| "vendor_id".equalsIgnoreCase(attrKey)
								|| "tcl_pop_address".equalsIgnoreCase(attrKey)
								|| "pop_address".equalsIgnoreCase(attrKey)
								|| "pop_name".equalsIgnoreCase(attrKey)
								|| "pop_city".equalsIgnoreCase(attrKey)
								|| "pop_state".equalsIgnoreCase(attrKey)
								|| "pop_country".equalsIgnoreCase(attrKey)
								|| "local_loop_bw".equalsIgnoreCase(attrKey)
								|| "POP_DIST_KM_SERVICE_MOD".equalsIgnoreCase(attrKey)
								|| "mf_contract_term".equalsIgnoreCase(attrKey)
								|| "pop_network_loc_id".equalsIgnoreCase(attrKey)
								|| "ll_change".equalsIgnoreCase(attrKey)
								|| "old_Port_Bw".equalsIgnoreCase(attrKey)
								|| "other_provider_name".equalsIgnoreCase(attrKey)	
								|| "quotetype_quote".equalsIgnoreCase(attrKey)	
								|| "Type".equalsIgnoreCase(attrKey)	
								|| "service_id".equalsIgnoreCase(attrKey)	
								|| "ipv4_address_pool_size".equalsIgnoreCase(attrKey)	
								|| "Orch_LM_Type".equalsIgnoreCase(attrKey)	
								|| "old_contract_term".equalsIgnoreCase(attrKey)	
								|| "old_Ll_Bw".equalsIgnoreCase(attrKey)	
								|| "service_commissioned_date".equalsIgnoreCase(attrKey)	
								|| "Orch_Category".equalsIgnoreCase(attrKey)	
								|| "resp_city".equalsIgnoreCase(attrKey)	
								|| "opportunity_term".equalsIgnoreCase(attrKey)	
								|| "account_id_with_18_digit".equalsIgnoreCase(attrKey)	
								|| "sales_org".equalsIgnoreCase(attrKey)	
								|| "access_feasibility_category".equalsIgnoreCase(attrKey)	
								|| "provider_request_date".equalsIgnoreCase(attrKey)	
								|| "Orch_Connection".equalsIgnoreCase(attrKey)	
								|| "cpe_supply_type".equalsIgnoreCase(attrKey)	
								|| "lm_type".equalsIgnoreCase(attrKey)	
								|| "topology".equalsIgnoreCase(attrKey)
								|| "macd_option".equalsIgnoreCase(attrKey)
								|| "response_related_to".equalsIgnoreCase(attrKey)
								|| "burstable_bw".equalsIgnoreCase(attrKey)
								|| "cu_le_id".equalsIgnoreCase(attrKey)
								|| "last_mile_contract_term".equalsIgnoreCase(attrKey)
								|| "Predicted_Access_Feasibility".equalsIgnoreCase(attrKey)
								|| "sfdc_feasibility_id".equalsIgnoreCase(attrKey)
								|| "partner_profile".equalsIgnoreCase(attrKey)
								|| "lat_long".equalsIgnoreCase(attrKey)
								|| "parallel_run_days".equalsIgnoreCase(attrKey)
								|| "lm_interface_change".equalsIgnoreCase(attrKey)
								|| "backup_port_requested".equalsIgnoreCase(attrKey)
								|| "ipv6_address_pool_size".equalsIgnoreCase(attrKey)
								|| "vpn_Name".equalsIgnoreCase(attrKey)
								|| "connection_type".equalsIgnoreCase(attrKey)
								|| "lm_otc_modem_charges_offwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_modem_charges_offwl".equalsIgnoreCase(attrKey)
								|| "lm_nrc_bw_onwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_bw_onwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_bw_onrf".equalsIgnoreCase(attrKey)
								|| "lm_nrc_bw_onrf".equalsIgnoreCase(attrKey)
								|| "lm_nrc_mast_onrf".equalsIgnoreCase(attrKey)								
								|| "task_id".equalsIgnoreCase(attrKey)
								|| "upgrade_type".equalsIgnoreCase(attrKey)
								) && entry.getValue() != null) {
							
							String attrValue = String.valueOf(entry.getValue());
							if("provider_name".equals(attrKey)) {
								OdrServiceAttribute OdrServiceAttribute = new OdrServiceAttribute();
								OdrServiceAttribute.setAttributeAltValueLabel(attrValue);
								OdrServiceAttribute.setAttributeName("closest_provider_bso_name");
								OdrServiceAttribute.setAttributeValue(attrValue);
								OdrServiceAttribute.setCategory("FEASIBILITY");
								OdrServiceAttribute.setCreatedBy(userName);
								OdrServiceAttribute.setCreatedDate(new Date());
								OdrServiceAttribute.setIsActive(CommonConstants.Y);
								OdrServiceAttribute.setOdrServiceDetail(serviceDetail);
								OdrServiceAttribute.setUpdatedBy(userName);
								OdrServiceAttribute.setUpdatedDate(new Date());
								serviceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
							}else  if("pop_network_loc_id".equals(attrKey)) {
								odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "popSiteCode",attrValue));
							}
													
							
							switch(attrKey){
								case "primary_bts_ip_address":
									attrKey="bts_IP_PMP";
									break;
								case "primary_bts_name":
									attrKey="bts_site_name";
									break;
								case "provider_name":
									attrKey="solution_type";
									break;
								case "primary_first_sector_id":
									attrKey="sector_id";
									break;
								case "POP_DIST_KM_SERVICE_MOD":
									attrKey = "chargeable_distance";
									break;
							}
							
							OdrServiceAttribute OdrServiceAttribute = new OdrServiceAttribute();
							OdrServiceAttribute.setAttributeAltValueLabel(attrValue);
							OdrServiceAttribute.setAttributeName(attrKey);
							OdrServiceAttribute.setAttributeValue(attrValue);
							OdrServiceAttribute.setCategory("FEASIBILITY");
							OdrServiceAttribute.setCreatedBy(userName);
							OdrServiceAttribute.setCreatedDate(new Date());
							OdrServiceAttribute.setIsActive(CommonConstants.Y);
							OdrServiceAttribute.setOdrServiceDetail(serviceDetail);
							OdrServiceAttribute.setUpdatedBy(userName);
							OdrServiceAttribute.setUpdatedDate(new Date());
							serviceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);							
						}
					}
				}
			}

		}
	}

	public void processLink(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username) {
		// TODO
	}

	public void processCloud(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrOrderAttribute> odrAttributes) {
		final List<OrderCloud> orderClouds = orderCloudRepository.findByOrderProductSolution(orderProductSolution);

		orderClouds.stream().findFirst().ifPresent(cloud -> {
			try {
				OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();
				if (!odrServiceDetails.isEmpty()) {
					primaryserviceDetail = odrServiceDetails.get(0);
				}
				primaryserviceDetail.setServiceStatus("IN PROGRESS");
				primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
				primaryserviceDetail.setServiceRefId(odrOrder.getOpOrderCode());
				primaryserviceDetail.setOdrContractInfo(odrContractInfo);
				primaryserviceDetail.setOdrOrder(odrOrder);
				primaryserviceDetail.setServiceCommissionedDate(cloud.getCreatedTime());
				Optional<OrderToLe> optOrderToLe = orderToLeRepository.findById(cloud.getOrderToLeId());
				if (optOrderToLe.isPresent()) {
					primaryserviceDetail.setMrc(optOrderToLe.get().getFinalMrc());
					primaryserviceDetail.setNrc(optOrderToLe.get().getFinalNrc());
					primaryserviceDetail.setArc(optOrderToLe.get().getFinalArc());
				}
				primaryserviceDetail.setErfPrdCatalogOfferingName(flowMapper.get("offeringName"));
				primaryserviceDetail.setCreatedBy(username);
				primaryserviceDetail.setCreatedDate(new Date());
				primaryserviceDetail.setUpdatedDate(new Date());
				primaryserviceDetail.setUpdatedBy(username);
				primaryserviceDetail.setIsActive(CommonConstants.Y);
				primaryserviceDetail.setIsIzo(CommonConstants.N);
				primaryserviceDetail.setLocalItContactName(flowMapper.get(LeAttributesConstants.NAME));
				primaryserviceDetail.setLocalItContactEmail(flowMapper.get(LeAttributesConstants.EMAIL_ID));
				primaryserviceDetail.setLocalItContactMobile(flowMapper.get(LeAttributesConstants.NUMBER));
				primaryserviceDetail.setErfPrdCatalogProductName(flowMapper.get("productName"));

				String isTaxExept[] = { "N" };
				odrAttributes.stream().filter(odrOrderAttr -> "isTaxExemption".equals(odrOrderAttr.getAttributeName()))
						.forEach(odrOrderAttr -> {
							if ("yes".equals(odrOrderAttr.getAttributeValue().toLowerCase())) {
								isTaxExept[0] = "Y";
							}
						});
				primaryserviceDetail.setTaxExemptionFlag(isTaxExept[0]);

				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(flowMapper.get("productName"), CommonConstants.BACTIVE);
				if (Objects.nonNull(mstProductFamily)) {
					primaryserviceDetail.setErfPrdCatalogProductId(mstProductFamily.getId());
				}
				LOGGER.info("MDC Filter token value in before Queue call constructCloudDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));

				String cityCode = CommonConstants.EMPTY;
				if (primaryserviceDetail.getDestinationCity() != null
						&& primaryserviceDetail.getDestinationCity().length() > 4) {
					cityCode = primaryserviceDetail.getDestinationCity().replaceAll(" ", "").replaceAll(".", "").substring(0, 4);
				} else {
					cityCode = Objects.nonNull(primaryserviceDetail.getDestinationCity())
							? primaryserviceDetail.getDestinationCity().replaceAll(" ", "").replaceAll(".", "")
							: primaryserviceDetail.getDestinationCity();
				}
				if (flowMapper.get("serviceCode") == null) {
					flowMapper.put("serviceCode", getServiceCode(flowMapper, cityCode));
					LOGGER.info("Generated service code : {}", getServiceCode(flowMapper, cityCode));
				}
				primaryserviceDetail.setUuid(flowMapper.get("serviceCode"));

				LOGGER.info("Retrieving location details");
				if (null != cloud.getDcLocationId()) {
					LOGGER.info("MDC Filter token value in before Queue call for ipc pricing location details {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					Map<String, Object> localIPCPricingLocation = constructIPCPricingLocationDetails(
							cloud.getDcLocationId());
					if (null != localIPCPricingLocation && !localIPCPricingLocation.isEmpty()) {
						LOGGER.info("Location map exists");
						constructServiceLocationDetails(localIPCPricingLocation, cloud, primaryserviceDetail);
						String isIpcBillingInternational = "N";
						if (odrOrder.getErfCustSpLeName() != null
								&& !odrOrder.getErfCustSpLeName().equalsIgnoreCase(CommonConstants.TATA_CUST_SP_LE_NAME)) {
							isIpcBillingInternational = "Y";
						}
						if(!odrAttributes.stream().anyMatch(ele -> CommonConstants.IS_IPC_BILLING_INTL.equals(ele.getAttributeName()))) {
							addOdrOrderAttributes(odrOrder, odrAttributes, username,
									CommonConstants.IS_IPC_BILLING_INTL, isIpcBillingInternational);
						}
					}
				}
				processCloudSla(cloud, primaryserviceDetail, username);

				for (OrderCloud orderCloud : orderClouds) {
					processIpcCommonComponentAttr(orderCloud, primaryserviceDetail, username, flowMapper);
					List<OmsAttachment> omsAttachments = omsAttachmentRepository
							.findByReferenceNameAndReferenceId("Site", orderCloud.getId());
					for (OmsAttachment omsAttachment : omsAttachments) {
						Integer erfAttachmentId = omsAttachment.getErfCusAttachmentId();
						LOGGER.info("MDC Filter token value in before Queue call getAttachment {} :",
								MDC.get(CommonConstants.MDC_TOKEN_KEY));
						String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
								String.valueOf(erfAttachmentId));
						if (attachmentResponse != null) {
							@SuppressWarnings("unchecked")
							Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse,
									Map.class);
							if (attachmentMapper != null) {
								Attachment attachment = new Attachment();
								attachment.setCreatedBy(username);
								attachment.setCreatedDate(new Timestamp(new Date().getTime()));
								attachment.setIsActive(CommonConstants.Y);
								attachment.setName((String) attachmentMapper.get("NAME"));
								attachment.setType(omsAttachment.getAttachmentType());
								attachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
								attachmentRepository.save(attachment);
								OdrAttachment odrAttachment = new OdrAttachment();
								odrAttachment.setAttachmentId(attachment.getId());
								odrAttachment.setAttachmentType(omsAttachment.getAttachmentType());
								odrAttachment.setIsActive(CommonConstants.Y);
								odrAttachment.setOfferingName(flowMapper.get("offeringName"));
								odrAttachment.setProductName(flowMapper.get("productName"));
								odrAttachmentRepository.save(odrAttachment);
							}
						}
					}
				}
				if (odrServiceDetails.isEmpty())
					odrServiceDetails.add(primaryserviceDetail);
			} catch (Exception e) {
				LOGGER.warn("Error in Order Le Cloud : {}", ExceptionUtils.getStackTrace(e));
			}
		});
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> constructIPCPricingLocationDetails(String dcLocationId) throws TclCommonException {
		String ipcPricingLocationResponse = (String) mqUtils.sendAndReceive(ipcPricingLocationQueue, dcLocationId);
		Map<String, Object> localIPCPricingLocation = null;
		if (StringUtils.isNotBlank(ipcPricingLocationResponse)) {
			LOGGER.info("Response from ipc location queue {} :", ipcPricingLocationResponse);
			localIPCPricingLocation = (Map<String, Object>) Utils.convertJsonToObject(ipcPricingLocationResponse,
					Map.class);
		}
		return localIPCPricingLocation;
	}

	private void constructServiceLocationDetails(Map<String, Object> localIPCPricingLocation, OrderCloud cloud,
			OdrServiceDetail primaryserviceDetail) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call for location details {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String city = (String) localIPCPricingLocation.get("CITY");
		String country = (String) localIPCPricingLocation.get("COUNTRY_CODE");
		primaryserviceDetail.setSiteAddress(
				city + CommonConstants.COMMA + CommonConstants.SPACE + WordUtils.capitalizeFully(country));
		Integer locationId = (Integer) localIPCPricingLocation.get("LOCATION_ID");
		String popResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(locationId));
		if (StringUtils.isNotBlank(popResponse)) {
			AddressDetail popAddressDetail = Utils.convertJsonToObject(popResponse, AddressDetail.class);
			if (popAddressDetail != null) {
				LOGGER.info("Response from location queue");
				String addr = popAddressDetail.getAddressLineOne() + CommonConstants.SPACE
						+ popAddressDetail.getAddressLineTwo() + CommonConstants.SPACE + popAddressDetail.getLocality()
						+ CommonConstants.SPACE + popAddressDetail.getCity() + CommonConstants.SPACE
						+ popAddressDetail.getState() + CommonConstants.SPACE + popAddressDetail.getCountry()
						+ CommonConstants.SPACE + popAddressDetail.getPincode();
				primaryserviceDetail.setSiteAddress(addr);
				primaryserviceDetail.setPopSiteAddress(addr);
				primaryserviceDetail.setPopSiteCode(cloud.getDcLocationId());
				primaryserviceDetail.setSiteType(cloud.getDcCloudType());
				primaryserviceDetail.setSourceCountry(popAddressDetail.getCountry());
				primaryserviceDetail.setSourceCity(popAddressDetail.getCity());
				primaryserviceDetail.setSourceAddressLineOne(popAddressDetail.getAddressLineOne());
				primaryserviceDetail.setSourceAddressLineTwo(popAddressDetail.getAddressLineTwo());
				primaryserviceDetail.setSourceLocality(popAddressDetail.getLocality());
				primaryserviceDetail.setSourcePincode(popAddressDetail.getPincode());
				primaryserviceDetail.setSourceState(popAddressDetail.getState());
			}
		}
	}

	/**
	 * processOrder
	 * @throws TclCommonException 
	 */
	public void processOrder(Order order, OdrOrder odrOrder, String userName) throws TclCommonException {
		odrOrder.setCreatedBy(userName);
		odrOrder.setCreatedDate(new Date());
		//odrOrder.setDemoFlag(order.getIsDemo() == CommonConstants.BACTIVE ? CommonConstants.Y : CommonConstants.N);
		odrOrder.setOrderSource(OrderSource.OPTIMUS.toString());
		odrOrder.setErfCustCustomerId(order.getCustomer().getErfCusCustomerId());
		odrOrder.setErfCustCustomerName(order.getCustomer().getCustomerName());
		odrOrder.setErfUserInitiatorId(order.getCreatedBy());
		odrOrder.setIsActive(CommonConstants.Y);
		odrOrder.setOpOrderCode(order.getOrderCode());
		odrOrder.setUpdatedBy(userName);
		odrOrder.setUpdatedDate(new Date());
		odrOrder.setUuid(order.getOrderCode());
		odrOrder.setErfUserCustomerUserId(order.getCustomer().getId());
		odrOrder.setOrderStartDate(order.getCreatedTime());
		odrOrder.setErfOrderId(order.getId());
		if (NumberUtils.isNumber(order.getEngagementOptyId())) {
			Optional<EngagementToOpportunity> engagementToOpportunity = engagementOpportunityRepository
					.findById(Integer.parseInt(order.getEngagementOptyId()));
			LOGGER.info("Engagement to opportunity {}", engagementToOpportunity.get());
			if (engagementToOpportunity.isPresent()) {
				Optional<Engagement> engagement = engagementRepository
						.findById(engagementToOpportunity.get().getEngagement().getId());
				LOGGER.info("Engagement {}", engagement.get());
				if (engagement.isPresent()) {
					odrOrder.setErfCustPartnerLeId(engagement.get().getErfCusPartnerLeId());
					LOGGER.debug("Before partner legal entity queue {}", engagement.get().getErfCusPartnerLeId());
					String partnerLegalEntitiesResponse = (String) mqUtils.sendAndReceive(partnerLegalEntities,
							Utils.convertObjectToJson(Arrays.asList(engagement.get().getErfCusPartnerLeId())));
					LOGGER.debug("After response from partner legal entity queue {}", partnerLegalEntitiesResponse);
					if (StringUtils.isNotBlank(partnerLegalEntitiesResponse)) {
						List<PartnerLegalEntityBean> partnerLegalEnitiyBeans = Utils.fromJson(
								partnerLegalEntitiesResponse, new TypeReference<List<PartnerLegalEntityBean>>() {
								});
						if (!CollectionUtils.isEmpty(partnerLegalEnitiyBeans)) {
							PartnerLegalEntityBean partnerLegalEnitiyBean = partnerLegalEnitiyBeans.get(0);
							odrOrder.setErfCustPartnerId(partnerLegalEnitiyBean.getPartnerId());
							odrOrder.setPartnerCuid(partnerLegalEnitiyBean.getTpsSfdcCuid());
							odrOrder.setErfCustPartnerName(partnerLegalEnitiyBean.getEntityName());
						}
					}
				}
			}
		}
		LOGGER.info("Odr Order {}", odrOrder);
	}

	public void processOrderToLe(OrderToLe orderToLe, OdrOrder odrOrder, boolean flag, OdrContractInfo odrContractInfo,
			String userName) throws TclCommonException {
		if (flag) {
			odrOrder.setErfCustLeId(orderToLe.getErfCusCustomerLegalEntityId());
			odrOrder.setOrderCategory(orderToLe.getOrderCategory());
			if ("MACD".equals(orderToLe.getOrderType()) || CommonConstants.MIGRATION.equals(orderToLe.getOrderType()) || "CANCELLATION".equalsIgnoreCase(orderToLe.getOrderType()) || "TERMINATION".equalsIgnoreCase(orderToLe.getOrderType())
					|| "RENEWALS".equalsIgnoreCase(orderToLe.getOrderType())) {
				odrOrder.setOrderType(orderToLe.getOrderType());
			} else {
				odrOrder.setOrderType(orderToLe.getOrderCategory() == null ? "NEW" : orderToLe.getOrderCategory());
			}
			/*if ("MACD".equals(orderToLe.getOrderType())) {
				List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
				if(Objects.nonNull(orderIllSiteToServices) && !orderIllSiteToServices.isEmpty()){
					Optional<OrderIllSiteToService> orderIllSiteToServiceOptional=orderIllSiteToServices.stream().findFirst();
					if(orderIllSiteToServiceOptional.isPresent()){
						OrderIllSiteToService orderIllSiteToService=orderIllSiteToServiceOptional.get();
						if(Objects.nonNull(orderIllSiteToService.getErfSfdcSubType())){
							LOGGER.info("Update Sub Category for MACD::",orderIllSiteToService.getErfSfdcSubType());
							odrOrder.setOrderSubCategory(orderIllSiteToService.getErfSfdcSubType());
						}
					}
				}
			}*/
			odrOrder.setCancelledParentOrderCode(orderToLe.getCancelledParentOrderCode());
			odrOrder.setErfOrderLeId(orderToLe.getId());
			odrOrder.setOrderSource(OdrConstants.OPTIMUS);
			odrOrder.setErfCustSpLeId(orderToLe.getErfCusSpLegalEntityId());
			odrOrder.setIsBundleOrder(orderToLe.getIsBundle() != null ? String.valueOf(orderToLe.getIsBundle()) : null);
			odrOrder.setOpportunityClassification(orderToLe.getClassification());
			odrOrder.setTpsSfdcOptyId(orderToLe.getTpsSfdcCopfId());
			if (odrOrder.getOpOrderCode().startsWith("IPC") && "MACD".equals(odrOrder.getOrderType())) {
				LOGGER.info("IPC MACD");
				if (null != orderToLe.getErfServiceInventoryParentOrderId()) {
					LOGGER.info("Retrieve parentOrderCode");
					SIOrderDataBean siOrderData = getSiOrderData(
							String.valueOf(orderToLe.getErfServiceInventoryParentOrderId()));
					if (null != siOrderData) {
						LOGGER.info("Order exists in service inventory");
						odrOrder.setParentOpOrderCode(siOrderData.getUuid());
					}
				}
			}
		}
		odrOrder.setDemoFlag((orderToLe.getIsDemo()!=null && orderToLe.getIsDemo() == CommonConstants.BACTIVE) ? CommonConstants.Y : CommonConstants.N);
		// TO add sfdc opty and cof Id
		odrContractInfo.setErfCustLeId(orderToLe.getErfCusCustomerLegalEntityId());
		odrContractInfo.setErfCustSpLeId(orderToLe.getErfCusSpLegalEntityId());
		odrContractInfo.setMrc(BigDecimal.valueOf(orderToLe.getFinalMrc()));
		odrContractInfo.setNrc(BigDecimal.valueOf(orderToLe.getFinalNrc()));
		odrContractInfo.setArc(BigDecimal.valueOf(orderToLe.getFinalArc()));
		if (orderToLe.getTermInMonths() != null && orderToLe.getTermInMonths().contains("months")) {
			odrContractInfo
					.setOrderTermInMonths(Double.valueOf(orderToLe.getTermInMonths().replace("months", "").trim()));
		} else if (orderToLe.getTermInMonths() != null && orderToLe.getTermInMonths().contains("month")) {
			odrContractInfo
					.setOrderTermInMonths(Double.valueOf(orderToLe.getTermInMonths().replace("month", "").trim()));
		} else if (orderToLe.getTermInMonths() != null && orderToLe.getTermInMonths().contains("year")) {
			odrContractInfo
					.setOrderTermInMonths(Double.valueOf(orderToLe.getTermInMonths().replace("year", "").trim()));
		} else if(orderToLe.getTermInMonths() != null && orderToLe.getTermInMonths().contains("days")) {
			odrContractInfo
			.setOrderTermInMonths(Double.valueOf(orderToLe.getTermInMonths().replace("days", "").trim()));
		}
		odrContractInfo.setIsActive(CommonConstants.Y);
		odrContractInfo.setCreatedBy(userName);
		odrContractInfo.setCreatedDate(new Date());
		odrContractInfo.setUpdatedBy(userName);
		odrContractInfo.setUpdatedDate(new Date());
		
		//added for site wise billing
		LOGGER.info("Order level site wise billing flag"+orderToLe.getSiteLevelBilling());
		if(orderToLe.getSiteLevelBilling()!=null && orderToLe.getSiteLevelBilling().equalsIgnoreCase("1")) {
		       odrOrder.setSiteLevelBilling("Y");
		}
		else {
			odrOrder.setSiteLevelBilling("N");
		}
		
	}

	/**
	 * 
	 * processOrderToLeAttr - This method is used to save the ke attr
	 * 
	 * @param orderLeAttr
	 * @param odrOrder
	 * @param odrAttributes
	 */
	public void processOrderToLeAttr(List<Map<String, Object>> orderLeAttr, OdrOrder odrOrder,
			List<OdrOrderAttribute> odrAttributes, OdrContractInfo odrContractInfo, String userName,
			Map<String, String> flowMapper) {
		odrContractInfo.setPoMandatoryStatus(CommonConstants.N);
		for (Map<String, Object> ordersLeAttributeValue : orderLeAttr) {
			String attrName = (String) ordersLeAttributeValue.get("name");
			String attrValue = (String) ordersLeAttributeValue.get("attribute_value");
			// odrOrder.setCustomerSegment(customerSegment);
			LOGGER.info("attribute name :-:{} attribute value:-: {}",attrName,attrValue);
			if (attrName.equals(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)) {
				odrOrder.setErfCustSpLeName(attrValue);
				odrContractInfo.setErfCustSpLeName(attrValue);
			} else if (attrName.equals(LeAttributesConstants.LEGAL_ENTITY_NAME)) {
				odrOrder.setErfCustLeName(attrValue);
				odrContractInfo.setErfCustLeName(attrValue);
			} else if (attrName.equals(LeAttributesConstants.ACCOUNT_NO18)) {
				odrOrder.setSfdcAccountId(attrValue);
			} else if (attrName.equals(LeAttributesConstants.ACCOUNT_CUID)) {
				odrOrder.setTpsSfdcCuid(attrValue);
				odrContractInfo.setTpsSfdcCuid(attrValue);
			} else if (attrName.equals(LeAttributesConstants.LE_NAME)) {
				odrContractInfo.setAccountManager(attrValue);
			} else if (attrName.equals(LeAttributesConstants.LE_EMAIL)) {
				odrContractInfo.setAccountManagerEmail(attrValue);
			} else if (attrName.equals(LeAttributesConstants.PO_REQUIRED)) {
				if (attrValue != null && attrValue.toLowerCase().equals("yes")) {
					odrContractInfo.setPoMandatoryStatus(CommonConstants.Y);
				} else {
					odrContractInfo.setPoMandatoryStatus(CommonConstants.N);
				}
			} else if (attrName.equals(LeAttributesConstants.CONTACT_NAME)) {
				odrContractInfo.setCustomerContact(attrValue);
			} else if (attrName.equals(LeAttributesConstants.CONTACT_EMAIL)) {
				odrContractInfo.setCustomerContactEmail(attrValue);
			} else if (attrName.equals(LeAttributesConstants.BILLING_FREQUENCY)) {
				odrContractInfo.setBillingFrequency(attrValue);
			} else if (attrName.equals(LeAttributesConstants.BILLING_ADDRESS)) {
				odrContractInfo.setBillingAddress(attrValue);
			} else if (attrName.equals(LeAttributesConstants.BILLING_METHOD)) {
				odrContractInfo.setBillingMethod(attrValue);
			} else if (attrName.equals(LeAttributesConstants.BILLING_CONTACT_ID)) {
				if(StringUtils.isNotBlank(attrValue)){
				odrContractInfo.setBillingContactId(Integer.valueOf(attrValue));
				BillingContact billingContact = constructBillingInformations(attrValue, odrContractInfo);
				if (billingContact != null) {
					if (billingContact.getFname() != null)
						addOdrOrderAttributes(odrOrder, odrAttributes, userName, "BILLING_CONTACT_NAME",
								billingContact.getFname() + " " + billingContact.getLname());
					if (billingContact.getMobileNumber() != null)
						addOdrOrderAttributes(odrOrder, odrAttributes, userName, "BILLING_CONTACT_MOBILE",
								StringUtils.isNotBlank(billingContact.getPhoneNumber())
										? billingContact.getPhoneNumber()
										: billingContact.getMobileNumber());
					if (billingContact.getEmailId() != null)
						addOdrOrderAttributes(odrOrder, odrAttributes, userName, "BILLING_CONTACT_EMAIL",
								billingContact.getEmailId());
					if (billingContact.getTitle() != null)
						addOdrOrderAttributes(odrOrder, odrAttributes, userName, "BILLING_CONTACT_TITLE",
								billingContact.getTitle());
				}
				}
			} else if (attrName.equals(LeAttributesConstants.PAYMENT_TERM)) {
				odrContractInfo.setPaymentTerm(attrValue);
			}else if (attrName.equals(LeAttributesConstants.LE_STATE_GST_NO)
					|| (attrName.equals(LeAttributesConstants.GST_NUMBER)
							&& odrOrder.getOpOrderCode().startsWith("UCW"))) {
				getContractGstAddress(odrOrder,attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, LeAttributesConstants.LE_STATE_GST_NO,attrValue);

			} else if (attrName.equals(LeAttributesConstants.LE_STATE_GST_NO) || (attrName
					.equals(LeAttributesConstants.GST_NUMBER) && odrOrder.getOpOrderCode()
					.startsWith("UCW")) || (attrName.equals(LeAttributesConstants.GST_NUMBER) && odrOrder
					.getOpOrderCode().startsWith("UCDR"))) {
				getContractGstAddress(odrOrder,attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName,attrValue);
			} else if (attrName.equals(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY)) {
				getContractAddress(odrOrder,attrValue,odrAttributes,userName);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName,attrValue);
			} else if (odrOrder.getOpOrderCode().startsWith(CommonConstants.IPC)
					&& (attrName.equals(LeAttributesConstants.NAME) || attrName.equals(LeAttributesConstants.EMAIL_ID)
							|| attrName.equals(LeAttributesConstants.NUMBER))) {
				flowMapper.put(attrName, attrValue);
			}else if (attrName.equals(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)) {
				LOGGER.info("Supplier contracting address id is present with name {} and value {}", attrName,
						attrValue);
				flowMapper.put(attrName, attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName, attrValue);
				try {
					if (StringUtils.isNotBlank(
							StringUtils.trimToEmpty(flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)))) {
						LOGGER.info("MDC Filter token value in before Queue call contruct sp le address {} :",
								MDC.get(CommonConstants.MDC_TOKEN_KEY));
						String spLeResponse = (String) mqUtils.sendAndReceive(locationQueue,
								flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS));
						if (spLeResponse != null) {
							AddressDetail spAddressDetail = (AddressDetail) Utils.convertJsonToObject(spLeResponse,
									AddressDetail.class);
							String address = StringUtils.trimToEmpty(spAddressDetail.getAddressLineOne())
									+ CommonConstants.SPACE
									+ StringUtils.trimToEmpty(spAddressDetail.getAddressLineTwo())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getLocality())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getCity())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getState())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getCountry())
									+ CommonConstants.SPACE + StringUtils.trimToEmpty(spAddressDetail.getPincode());
							flowMapper.put(IzosdwanCommonConstants.SUPPLIER_ADDRESS, address);
							addOdrOrderAttributes(odrOrder, odrAttributes, userName,
									IzosdwanCommonConstants.SUPPLIER_ADDRESS, address);
							flowMapper.put(IzosdwanCommonConstants.SUPPLIER_COUNTRY, spAddressDetail.getCountry());
							addOdrOrderAttributes(odrOrder, odrAttributes, userName,
									IzosdwanCommonConstants.SUPPLIER_COUNTRY, spAddressDetail.getCountry());
							String isInternational = "N";
							if (spAddressDetail.getCountry() != null
									&& !"India".equalsIgnoreCase(spAddressDetail.getCountry())) {
								isInternational = "Y";
							}
							flowMapper.put(IzosdwanCommonConstants.IS_BILLING_INTL, isInternational);
							addOdrOrderAttributes(odrOrder, odrAttributes, userName,
									IzosdwanCommonConstants.IS_BILLING_INTL, isInternational);
						}
					}
				} catch (Exception e) {
					LOGGER.error("Error in getting sp address", e);
				}
			} else {
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName, attrValue);
			}
		}

	}

	/**
	 * addOdrOrderAttributes
	 * 
	 * @param odrOrder
	 * @param odrAttributes
	 * @param userName
	 * @param attrName
	 * @param attrValue
	 */
	private void addOdrOrderAttributes(OdrOrder odrOrder, List<OdrOrderAttribute> odrAttributes, String userName,
			String attrName, String attrValue) {
		OdrOrderAttribute orderAttributes = new OdrOrderAttribute();
		orderAttributes.setAttributeAltValueLabel(attrValue);
		orderAttributes.setAttributeName(attrName);
		orderAttributes.setAttributeValue(attrValue);
		orderAttributes.setCreatedBy(userName);
		orderAttributes.setCreatedDate(new Date());
		orderAttributes.setUpdatedBy(userName);
		orderAttributes.setUpdatedDate(new Date());
		orderAttributes.setOdrOrder(odrOrder);
		odrAttributes.add(orderAttributes);
	}

	private BillingContact constructBillingInformations(String billingContactId, OdrContractInfo odrContractInfo) {
		BillingContact billingContact = null;
		if (StringUtils.isNotBlank(billingContactId)) {
			try {
				LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
						String.valueOf(billingContactId));
				LOGGER.info("Billing Response {}", billingContactResponse);
				if (StringUtils.isNotBlank(billingContactResponse)) {
					billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
							BillingContact.class);
					String locationId = billingContact.getErfLocationId();
					String billingAddrr = constructCustomerLocationDetails(locationId, odrContractInfo);
					odrContractInfo.setErfLocBillingLocationId(locationId);
					odrContractInfo.setBillingAddress(billingAddrr);
				}

			} catch (Exception e) {
				LOGGER.error("Error in Billing Contact ", e);
			}
		}
		return billingContact;
	}

	private String constructCustomerLocationDetails(String locationId, OdrContractInfo odrContractInfo)
			throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, locationId);
		LOGGER.info("The response for the input location id {} is {}", locationId, locationResponse);
		if (StringUtils.isNotBlank(locationResponse)) {
			LOGGER.info("locationResponse" + locationResponse);
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			odrContractInfo.setBillingAddressLine1(addressDetail.getAddressLineOne());
			odrContractInfo.setBillingAddressLine2(addressDetail.getAddressLineTwo());
			odrContractInfo.setBillingAddressLine3(StringUtils.trimToEmpty(addressDetail.getLocality()));
			odrContractInfo.setBillingCity(addressDetail.getCity());
			odrContractInfo.setBillingCountry(addressDetail.getCountry());
			odrContractInfo.setBillingState(addressDetail.getState());
			odrContractInfo.setBillingPincode(addressDetail.getPincode());
			return StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getPincode());
		}
		return null;
	}

	/**
	 * processOrderEnrichment
	 */
	public Boolean processOrderEnrichment(Integer orderId, String username) {
		Boolean status = true;
		Optional<Order> orderEntity = orderRepository.findById(orderId);
		if (orderEntity.isPresent()) {
			OdrOrder odrOrder = odrOrderRepository.findByOpOrderCode(orderEntity.get().getOrderCode());
			if (odrOrder != null) {
				try {
					for (OrderToLe orderToLe : orderEntity.get().getOrderToLes()) {
						Map<String, String> flowMapper = new HashMap<>();
						for (OrderToLeProductFamily orderToLeProductFamily : orderToLe.getOrderToLeProductFamilies()) {
							flowMapper.put("productName", orderToLeProductFamily.getMstProductFamily().getName());
							flowMapper.put("productCode",
									orderToLeProductFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
							for (OrderProductSolution orderProductSolution : orderToLeProductFamily
									.getOrderProductSolutions()) {
								flowMapper.put("offeringName",
										orderProductSolution.getMstProductOffering().getProductName());
								for (OrderIllSite orderIllSite : orderProductSolution.getOrderIllSites()) {
									processOrderEnrichmentForSites(username, orderIllSite, flowMapper);
								}
							}
						}

					}
				} catch (Exception e) {
					LOGGER.error("Error in  Order Enrichment Order Frost {}", ExceptionUtils.getStackTrace(e));
					status = false;
				}
			} else {
				LOGGER.warn("Order enrichment will not be processed as odrOrder is not processed");
			}
		} else {
			LOGGER.warn(
					"Order is not yet frozen , so order enrichment also will be taken care at the time of order freeze");
		}
		return status;
	}

	public Boolean processOrderEnrichmentForIndividualSites(String username, OdrServiceDetail odrServiceDetail)
			throws TclCommonException, ParseException {
		Boolean status = true;
		Set<OdrServiceDetail> odrServiceDetails = new HashSet<>();
		odrServiceDetails.add(odrServiceDetail);
		List<OrderIllSite> illSite = orderIllSiteRepository.findBySiteCodeAndStatus(odrServiceDetail.getServiceRefId(),
				CommonConstants.BACTIVE);
		for (OrderIllSite orderIllSite : illSite) {
			Map<String, String> flowMapper = getOrderStatus(orderIllSite);
			if (flowMapper != null) {
				LOGGER.info("Order Enrichment is completed so proceeding the with the oe");
				processOeSite(username, orderIllSite, flowMapper, odrServiceDetails);
			} else {
				LOGGER.info("Order Enrichment is not yet completed so just updating the New Order to COMPLETED");
				odrServiceDetailRepository.save(odrServiceDetail);
			}
		}

		return status;
	}

	private Map<String, String> getOrderStatus(OrderIllSite illSite) {
		Map<String, String> flowMapper = new HashMap<>();
		OrderProductSolution prodSolution = illSite.getOrderProductSolution();
		flowMapper.put("offeringName", prodSolution.getMstProductOffering().getProductName());
		OrderToLeProductFamily orderFamily = prodSolution.getOrderToLeProductFamily();
		flowMapper.put("productName", orderFamily.getMstProductFamily().getName());
		flowMapper.put("productCode", orderFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
		Order order = orderFamily.getOrderToLe().getOrder();
		if (order.getStage().equals("ORDER_COMPLETED")) {
			return flowMapper;
		}
		return null;

	}

	public Boolean processStatus(Integer siteId, String username) {
		Boolean status = true;
		try {
			Optional<OdrServiceDetail> odrServiceDetailEntity = odrServiceDetailRepository.findById(siteId);
			if (odrServiceDetailEntity.isPresent()) {
				OdrServiceDetail odrServiceDetail = odrServiceDetailEntity.get();
				if (odrServiceDetail.getFlowStatus().equals("NEW")) {
					odrServiceDetail.setFlowStatus("COMPLETED");
					LOGGER.info("OdrService {} completed", siteId);
					processOrderEnrichmentForIndividualSites(username, odrServiceDetail);
				}
			} else {
				LOGGER.warn(
						"Order is not yet frozen , so order enrichment also will be taken care at the time of order freeze");
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing status ", e);
		}
		return status;
	}

	/**
	 * processOrderEnrichmentForSites
	 * 
	 * @param username
	 * @param orderIllSite
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public void processOrderEnrichmentForSites(String username, OrderIllSite orderIllSite,
			Map<String, String> flowMapper) throws TclCommonException, ParseException {
		Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository
				.findByServiceRefId(orderIllSite.getSiteCode());
		if (odrServiceDetails.isEmpty()) {
			String priCode = orderIllSite.getSiteCode() + "-" + "PRIM";
			odrServiceDetails = odrServiceDetailRepository.findByServiceRefId(priCode);
			String secCode = orderIllSite.getSiteCode() + "-" + "SECO";
			odrServiceDetails.addAll(odrServiceDetailRepository.findByServiceRefId(secCode));
		}

		if (!odrServiceDetails.isEmpty()) {
			processOeSite(username, orderIllSite, flowMapper, odrServiceDetails);
		}
	}

	private void processOeSite(String username, OrderIllSite orderIllSite, Map<String, String> flowMapper,
			Set<OdrServiceDetail> odrServiceDetails) throws TclCommonException, ParseException {
		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String demarReponse = (String) mqUtils.sendAndReceive(demarcationQueue,
				String.valueOf(orderIllSite.getErfLocSitebLocationId()));
		if (StringUtils.isNotBlank(demarReponse)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> demarDetails = (Map<String, Object>) Utils.convertJsonToObject(demarReponse, Map.class);
			if (demarDetails != null) {
				Set<OdrServiceDetailBean> odrServiceBeanDetails = new HashSet<>();
				for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
					if (odrServiceDetail.getFlowStatus().equals("NEW")) {
						continue;
					}
					List<OdrComponent> odrComponents=odrComponentRepository.findByOdrServiceDetailAndComponentNameAndSiteType(odrServiceDetail, "LM", "A");
					OdrComponent odrComponent=null;
					for (OdrComponent odrComponentEntity : odrComponents) {
						odrComponent=odrComponentEntity;
					}
					odrServiceDetail.setDemarcationApartment(
							demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
									: null);
					odrServiceDetail.setDemarcationFloor(
							demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null);
					odrServiceDetail.setDemarcationRack(
							demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null);
					odrServiceDetail.setDemarcationRoom(
							demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "demarcationBuildingName", 
							demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
									: null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "demarcationFloor", 
							demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "demarcationWing", 
							demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "demarcationRoom", 
							demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null));
					odrComponentRepository.save(odrComponent);
					processLocalItContact(String.valueOf(orderIllSite.getId()), odrServiceDetail, username,odrComponent);
					List<OmsAttachment> omsAttachments = omsAttachmentRepository
							.findByReferenceNameAndReferenceId("Site", orderIllSite.getId());
					for (OmsAttachment omsAttachment : omsAttachments) {
						Integer erfAttachmentId = omsAttachment.getErfCusAttachmentId();
						LOGGER.info("MDC Filter token value in before Queue call getAttachment {} :",
								MDC.get(CommonConstants.MDC_TOKEN_KEY));
						String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
								String.valueOf(erfAttachmentId));
						if (attachmentResponse != null) {
							@SuppressWarnings("unchecked")
							Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse,
									Map.class);
							if (attachmentMapper != null) {
								Attachment attachment = new Attachment();
								OdrAttachment odrAttachment = new OdrAttachment();
								Map<String, Object> attachmentObj = odrAttachmentRepository
										.findByErfOdrServiceIdAndType(odrServiceDetail.getId(),
												omsAttachment.getAttachmentType());
								if (!attachmentObj.isEmpty()) {
									LOGGER.info("Already Attachment and odrAttachment is found , so updating {}",
											attachmentObj);
									attachment.setId((Integer) attachmentObj.get("attachment_id"));
									odrAttachment.setId((Integer) attachmentObj.get("odr_attachment_id"));
								} else {
									LOGGER.info(
											"Creating a new attachment as already the attachment object is not found");
								}
								attachment.setCreatedBy(username);
								attachment.setCreatedDate(new Timestamp(new Date().getTime()));
								attachment.setIsActive(CommonConstants.Y);
								attachment.setName((String) attachmentMapper.get("NAME"));
								attachment.setType(omsAttachment.getAttachmentType());
								attachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
								attachmentRepository.save(attachment);

								odrAttachment.setAttachmentId(attachment.getId());
								odrAttachment.setAttachmentType(omsAttachment.getAttachmentType());
								odrAttachment.setIsActive(CommonConstants.Y);
								odrAttachment.setOfferingName(flowMapper.get("offeringName"));
								odrAttachment.setProductName(flowMapper.get("productName"));
								odrAttachment.setOdrServiceDetail(odrServiceDetail);
								// odrAttachment.setServiceCode(odrServiceDetail.getUuid());
								odrAttachmentRepository.save(odrAttachment);
							}

						}

					}
					processOEProductComponentAttr(String.valueOf(orderIllSite.getId()), odrServiceDetail,
							odrServiceDetail.getPrimarySecondary(), username);
					odrServiceDetailRepository.save(odrServiceDetail);
//Construct Odr
					OdrServiceDetailBean odrServiceDetailBean = new OdrServiceDetailBean();
					odrServiceDetailBean.setLocalItContactEmail(odrServiceDetail.getLocalItContactEmail());
					odrServiceDetailBean.setLocalItContactMobile(odrServiceDetail.getLocalItContactMobile());
					odrServiceDetailBean.setLocalItContactName(odrServiceDetail.getLocalItContactName());
					odrServiceDetailBean.setDemarcationApartment(odrServiceDetail.getDemarcationApartment());
					odrServiceDetailBean.setDemarcationFloor(odrServiceDetail.getDemarcationFloor());
					odrServiceDetailBean.setDemarcationRack(odrServiceDetail.getDemarcationRack());
					odrServiceDetailBean.setDemarcationRoom(odrServiceDetail.getDemarcationRoom());
					odrServiceDetailBean.setBillingGstNumber(odrServiceDetail.getBillingGstNumber());
					odrServiceDetailBean.setId(odrServiceDetail.getId());
					// odrServiceDetailBean.setUuid(odrServiceDetail.getUuid());
					odrServiceDetailBean.setTaxExemptionFlag(odrServiceDetail.getTaxExemptionFlag());
					List<OdrAttachment> odrAttachments = odrAttachmentRepository
							.findByOdrServiceDetailId(odrServiceDetail.getId());
					List<OdrAttachmentBean> odrAttachmentBeans = new ArrayList<OdrAttachmentBean>();
					for (OdrAttachment odrAttachment : odrAttachments) {
						Optional<Attachment> attachmentEntity = attachmentRepository
								.findById(odrAttachment.getAttachmentId());
						if (attachmentEntity.isPresent()) {
							OdrAttachmentBean odrAttachmentBean = new OdrAttachmentBean();
							odrAttachmentBean.setAttachmentId(odrAttachment.getAttachmentId());
							odrAttachmentBean.setCategory(attachmentEntity.get().getCategory());
							odrAttachmentBean.setContentTypeHeader(attachmentEntity.get().getContentTypeHeader());
							odrAttachmentBean.setCreatedBy(attachmentEntity.get().getCreatedBy());
							odrAttachmentBean.setCreatedDate(attachmentEntity.get().getCreatedDate());
							odrAttachmentBean.setName(attachmentEntity.get().getName());
							odrAttachmentBean.setOfferingName(odrAttachment.getOfferingName());
							odrAttachmentBean.setOrderId(odrAttachment.getOrderId());
							odrAttachmentBean.setProductName(odrAttachment.getProductName());
							odrAttachmentBean.setErfOdrServiceId(odrAttachment.getOdrServiceDetail().getId());
							// odrAttachmentBean.setServiceCode(odrAttachment.getServiceCode());
							odrAttachmentBean.setServiceId(odrAttachment.getServiceId());
							odrAttachmentBean.setSiteId(odrAttachment.getSiteId());
							odrAttachmentBean.setStoragePathUrl(attachmentEntity.get().getStoragePathUrl());
							odrAttachmentBean.setType(attachmentEntity.get().getType());
							odrAttachmentBeans.add(odrAttachmentBean);
						}
					}
					odrServiceDetailBean.setOdrAttachments(odrAttachmentBeans);
					odrServiceBeanDetails.add(odrServiceDetailBean);
					LOGGER.info("Updated the service Detail for {}", odrServiceDetail.getId());

				}
				if (!odrServiceBeanDetails.isEmpty()) {
					OdrOrderBean odrOrderBean = new OdrOrderBean();
					odrOrderBean.setOdrServiceDetails(odrServiceBeanDetails);
					LOGGER.info("MDC Filter token value in before Queue call fulfillment {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mqUtils.send(orderEnrichmentFulfillQueue, Utils.convertObjectToJson(odrOrderBean));
				}
			}
		}
	}

	public SIOrderDataBean getSiOrderData(String siOrderId) throws TclCommonException {
		SIOrderDataBean siOrderData = null;
		if (StringUtils.isNotBlank(siOrderId)) {
			SIGetOrderRequest getOrderRequest = new SIGetOrderRequest();
			getOrderRequest.setOrderId(siOrderId);
			String requestPayload = Utils.convertObjectToJson(getOrderRequest);
			LOGGER.info("Sending request to getSIOrder:: {}", requestPayload);
			String response = (String) mqUtils.sendAndReceive(getSIOrderQueue, requestPayload);
			LOGGER.info("Received response from getSIOrder:: {}", response);
			SIGetOrderResponse orderResponse = Utils.fromJson(response, SIGetOrderResponse.class);
			if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus())) {
				siOrderData = orderResponse.getOrder();
			} else {
				LOGGER.error("Error in retrieving getSIOrder:: {}", orderResponse.getMessage());
				throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		}
		return siOrderData;
	}
	
	private OdrOrder getContractGstAddress(OdrOrder odrOrder, String gstNo) {
		LOGGER.info("inside contract level gst:: {} for legal id:: {}",gstNo,odrOrder.getErfCustLeId());
		SiteGstDetail siteGstDetail = new SiteGstDetail();
		siteGstDetail.setGstNo(gstNo);
		siteGstDetail.setCustomerLeId(odrOrder.getErfCustLeId());
		try {
			String leGst = (String) mqUtils.sendAndReceive(siteGstQueue, Utils.convertObjectToJson(siteGstDetail));
			if (StringUtils.isNotBlank(leGst)) {
				LeStateInfo leStateInfo = (LeStateInfo) Utils.convertJsonToObject(leGst, LeStateInfo.class);
				if (leStateInfo != null) {
					LOGGER.info("Gst Response::{}", leStateInfo.toString());
					OdrGstAddress gstAddress = new OdrGstAddress();
					gstAddress.setBuildingName(StringUtils.trimToEmpty(leStateInfo.getAddresslineOne()));
					gstAddress.setStreet(StringUtils.trimToEmpty(leStateInfo.getAddresslineTwo()));
					gstAddress.setLocality(StringUtils.trimToEmpty(leStateInfo.getAddresslineThree()));
					gstAddress.setDistrict(StringUtils.trimToEmpty(leStateInfo.getCity()));
					gstAddress.setState(StringUtils.trimToEmpty(leStateInfo.getState()));
					gstAddress.setPincode(StringUtils.trimToEmpty(leStateInfo.getPincode()));
					odrOrder.setOdrGstAddress(gstAddress);
				}
			}
		} catch (TclCommonException | IllegalArgumentException e) {

			LOGGER.error("error in getting gst response");
		}
		return odrOrder;
	}
	
	private OdrOrder getContractAddress(OdrOrder odrOrder, String locationId, List<OdrOrderAttribute> odrAttributes, String userName) {
		try {
			LOGGER.info("MDC Filter token value in before Queue call construct Contract details {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			Map<String, String> contractAddressMap = new  HashMap<>();
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, locationId);
			if (StringUtils.isNotBlank(locationResponse)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
				
				contractAddressMap.put("ContractingAddressLineOne",StringUtils.trimToEmpty(addressDetail.getAddressLineOne()));
				contractAddressMap.put("ContractingAddressLineTwo",StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()));
				contractAddressMap.put("ContractingAddressLineThree",StringUtils.trimToEmpty(addressDetail.getLocality()));
				contractAddressMap.put("ContractingAddressCity",StringUtils.trimToEmpty(addressDetail.getCity()));
				contractAddressMap.put("ContractingAddressState",StringUtils.trimToEmpty(addressDetail.getState()));
				contractAddressMap.put("ContractingAddressCountry",StringUtils.trimToEmpty(addressDetail.getCountry()));				
				contractAddressMap.put("ContractingAddressPincode",StringUtils.trimToEmpty(addressDetail.getPincode()));
				
				contractAddressMap.forEach((attrName, attrValue) -> addOdrOrderAttributes(odrOrder, odrAttributes,
						userName, attrName, attrValue));

			}

		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return odrOrder;
	}
	
	protected OdrServiceAttribute persistOdrServiceAttributes(OdrServiceDetail odrServiceDetail, String attrName,
			String attrValue) {
		LOGGER.info("Inside persistOdrServiceAttributes, attribute name {}, attrib value {}", attrName, attrValue);
		OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
			odrServiceAttribute.setAttributeAltValueLabel(attrName);
			odrServiceAttribute.setAttributeName(attrName);
			odrServiceAttribute.setAttributeValue(attrValue);
			odrServiceAttribute.setCreatedBy(Utils.getSource());
			odrServiceAttribute.setCreatedDate(new Date());
			odrServiceAttribute.setIsActive(CommonConstants.Y);
			odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
			odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
			odrServiceAttribute.setUpdatedBy(Utils.getSource());
			odrServiceAttribute.setUpdatedDate(new Date());
		return odrServiceAttribute;
	}
	
	protected OdrServiceAttribute persistOdrServiceAttributesWithCategory(OdrServiceDetail odrServiceDetail, String attrName,
			String attrValue,String category) {
		LOGGER.info("Inside persistOdrServiceAttributes, attribute name {}, attrib value {}", attrName, attrValue);
		OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
			odrServiceAttribute.setAttributeAltValueLabel(attrName);
			odrServiceAttribute.setAttributeName(attrName);
			odrServiceAttribute.setAttributeValue(attrValue);
			odrServiceAttribute.setCreatedBy(Utils.getSource());
			odrServiceAttribute.setCreatedDate(new Date());
			odrServiceAttribute.setIsActive(CommonConstants.Y);
			odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
			odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
			odrServiceAttribute.setUpdatedBy(Utils.getSource());
			odrServiceAttribute.setUpdatedDate(new Date());
			odrServiceAttribute.setCategory(category);
		return odrServiceAttribute;
	}
	
	public String getEventSourceforBurstable(OdrServiceDetail scServiceDetail) {
		String eventSource="";
		LOGGER.info("MDC Filter token value in before Queue call getEventSoruceforBurstable {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		try {
			eventSource = (String) mqUtils.sendAndReceive(eventSourceQueue,String.valueOf(scServiceDetail.getUuid()));
			if (StringUtils.isEmpty(eventSource) || "Not Available".equals(eventSource)) {
				String custName = scServiceDetail.getOdrOrder().getErfCustLeName().replaceAll("[^a-zA-Z0-9]", "")
						.substring(0, 6);
				eventSource = custName.concat("-").concat(scServiceDetail.getUuid());
			}
		} catch (TclCommonException e) {
						e.printStackTrace();
		}
		LOGGER.info("eventSource Response {}", eventSource);
		
		return eventSource;
	}
	
	@SuppressWarnings("unchecked")
	public OdrServiceDetail processProductComponentAttrWithoutCommercials(String refId, OdrServiceDetail serviceDetail, String type,
			String userName, boolean isUpdate, List<OdrServiceCommercial> odrServiceCommercials,OdrComponent odrComponent)
			throws TclCommonException, IllegalArgumentException, ParseException {

		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, type, getReferenceName());
		if (oderProdCompAttrs.isEmpty()) {
			LOGGER.info("Secondary not available for {} ,{} ,{}",refId, type, getReferenceName());
			return null;
		}
		serviceDetail.setPrimarySecondary(type);
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");
			if(category!=null && category.equals("IZO Private Connect Port")) {
				category="GVPN Common";
			}
			if (attrName.equals("Burstable Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBurstableBwPortspeed(attrValue.trim());
				serviceDetail.setBurstableBwPortspeedAltName(attrValue.trim());
				serviceDetail.setBurstableBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBandwidth",attrValue.trim()));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBwUnit","Mbps"));
			} else if ((attrName.equals("Port Bandwidth") && StringUtils.isNotBlank(attrValue))
					|| (attrName.equals("Bandwidth") && StringUtils.isNotBlank(attrValue))) {
				serviceDetail.setBwPortspeed(attrValue.trim());
				serviceDetail.setBwPortspeedAltName(attrValue.trim());
				serviceDetail.setBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "portBandwidth",attrValue.trim()));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "bwUnit","Mbps"));
			} else if (attrName.equals("Local Loop Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setLastmileBw(attrValue.trim());
				serviceDetail.setLastmileBwAltName(attrValue.trim());
				serviceDetail.setLastmileBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localLoopBandwidth",attrValue.trim()));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localLoopBandwidthUnit","Mbps"));
			} else if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue.trim());
				
			} else if (attrName.equals("CPE Basic Chassis") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call product BOM Details {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("calling the product with request {}", attrValue);
				/*
				 * List<String> list = new ArrayList<>(); list.add(attrValue);
				 */
				String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue, attrValue);
				/*
				 * String cpeString = list.stream().collect(Collectors.joining(","));
				 * 
				 * String productBomDetailsStr = (String)
				 * mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString); String
				 * modifiedProductBomDetailsStr=
				 * productBomDetailsStr.replaceFirst("cpeBomDetails", "resources");
				 * LOGGER.info("productBomDetailsStr {}", modifiedProductBomDetailsStr);
				 */
				if (StringUtils.isNotBlank(productBomDetailsStr)) {
					LOGGER.info("response received product with response {}", productBomDetailsStr);
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("cpe-bom-resource");
					odrAdditionalServiceParam.setCategory("CPE-BOM");
					odrAdditionalServiceParam.setCreatedBy(userName);
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(productBomDetailsStr);
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					//OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
					//		.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
							//		category);
					//if (odrServiceAttribute == null) {
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
						OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
						odrServiceAttribute.setAttributeAltValueLabel(attrValue);
						odrServiceAttribute.setAttributeName(attrName);
						odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
						odrServiceAttribute.setCategory(category);
						odrServiceAttribute.setCreatedBy(userName);
						odrServiceAttribute.setCreatedDate(new Date());
						odrServiceAttribute.setIsActive(CommonConstants.Y);
						odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
						odrServiceAttribute.setOdrServiceDetail(serviceDetail);
						odrServiceAttribute.setUpdatedBy(userName);
						odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				//	}else{
					//	LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
					//}
					
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption",CommonConstants.Y));
			} else if (attrName.equals("Interface") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setSiteEndInterface(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "interface",attrValue));
			} else if (attrName.equals("CPE Management Type") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setServiceOption(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeManagementType",attrValue));
			} else if (attrName.equals("Type of Peering") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, "Cloud Type", attrValue));
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, attrName, attrValue));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "Cloud Type",attrValue));
			} else if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
				Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
						Map.class);
				if (localItDetail != null) {
					serviceDetail.setLocalItContactEmail(
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
					serviceDetail.setLocalItContactMobile(
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
					serviceDetail.setLocalItContactName(
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
					LOGGER.info("Local IT Contact Received {}", localItDetail);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
			} else {
				//OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
					//	.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
						//		category);
				//if (odrServiceAttribute == null) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}",attrName,attrValue,serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(attrValue);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(userName);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(userName);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				//}else{
					//LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
				//}
			}

		}
		if (!isUpdate)
			processSiteFeasibility(refId, serviceDetail, type, userName,odrComponent);
		return serviceDetail;
	}

}
