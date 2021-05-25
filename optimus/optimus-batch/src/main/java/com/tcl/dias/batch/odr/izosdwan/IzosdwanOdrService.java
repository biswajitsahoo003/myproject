package com.tcl.dias.batch.odr.izosdwan;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.tcl.dias.oms.entity.entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import com.tcl.dias.batch.odr.mapper.OdrIzosdwanMapper;
import com.tcl.dias.common.beans.AddonsBean;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CgwServiceAreaMatricBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.ProductLocationBean;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomDto;
import com.tcl.dias.common.servicefulfillment.beans.ResourceDto;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderResponse;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.OrderSource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstServiceClassificationDetail;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.entities.OdrAttachment;
import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrComponentAttribute;
import com.tcl.dias.oms.entity.entities.OdrContractInfo;
import com.tcl.dias.oms.entity.entities.OdrGstAddress;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrOrderAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercial;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceSla;
import com.tcl.dias.oms.entity.entities.OdrSolutionComponent;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstServiceClassificationDetailRepository;
import com.tcl.dias.oms.entity.repository.OdrAdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.OdrAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentRepository;
import com.tcl.dias.oms.entity.repository.OdrContractInfoRepository;
import com.tcl.dias.oms.entity.repository.OdrGstAddressRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceCommercialRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceDetailRepository;
import com.tcl.dias.oms.entity.repository.OdrSolutionComponentRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanCpeConfigDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.springframework.util.CollectionUtils;

@Service
public class IzosdwanOdrService extends OrderService {

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
	OrderIzosdwanSiteFeasibilityRepository orderIzosdwanSiteFeasibilityRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository OrderProductComponentsAttributeValueRepository;

	@Autowired
	OrderIzosdwanSiteSlaRepository orderIzosdwanSiteSlaRepository;

	@Autowired
	OrderCloudRepository orderCloudRepository;

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

	@Autowired
	MQUtils mqUtils;

	@Autowired
	OdrIzosdwanMapper odrMapper;

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
	OdrComponentAttributeRepository odrComponentAttributeRepository;

	@Autowired
	OdrComponentRepository odrComponentRepository;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Autowired
	OrderIzosdwanSiteRepository orderIzosdwanSiteRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OdrSolutionComponentRepository odrSolutionComponentRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;
	
	@Autowired
	MstServiceClassificationDetailRepository mstServiceClassificationDetailRepository;
	
	@Autowired
	OrderIzosdwanCgwDetailRepository orderIzosdwanCgwDetailRepository;
	
	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
	
	@Autowired
	OdrGstAddressRepository odrGstAddressRepository;

	@Autowired
	OrderIzosdwanCpeConfigDetailsRepository orderIzosdwanCpeConfigDetailsRepository;
	
	@Value("${rabbitmq.product.cgw.sam.city}")
	String cgwSamByCityQueue;
	
	@Value("${rabbitmq.product.location.county}")
	String productLocationQueue;
	
	private final String ILL_CODE = "0300";
	private final String GVPN_CODE = "6230";
	private final String IPC_CODE = "0000";

	private final String CLOUD = "CLOUD";
	private final String ACCESS = "ACCESS";
	private final String ADDON = "ADDON";
	private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

	protected String getReferenceName(String product) {
		if (product.equals(IzosdwanCommonConstants.IZOSDWAN_NAME)) {
			return OdrConstants.IZOSDWAN_SITES;
		}
		if (product.equals(IzosdwanCommonConstants.CGW)) {
			return IzosdwanCommonConstants.IZOSDWAN_CGW;
		}
		return OdrConstants.IZOSDWAN_SITES;
	}

	/**
	 * 
	 * processOrderFrost - This method is used to freeze the order by transforming
	 * the order to a new table odr
	 * 
	 * @param orderId
	 * @return -Boolean - Status whether the process is success or failure
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public synchronized Boolean processOrderFrost(Integer orderId, String username) {
		LOGGER.info("Process starting to freeze the order with id {}", orderId);
		Boolean status = true;
		Optional<Order> orderEntity = orderRepository.findById(orderId);
		Map<String, String> flowMapper = new HashMap<>(); // Need to change for multiple le
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
				String stage = orderEntity.get().getStage();
				LOGGER.info("processed process order");
				boolean flag = true;
				List<OdrOrderAttribute> odrAttributes = new ArrayList<>();
				List<OdrContractInfo> odrContractInfos = new ArrayList<>();
				List<OdrServiceDetail> odrServiceDetails = new ArrayList<>();
				List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();
				Map<String, Map<String, Integer>> prisecMapping = new HashMap<>();
				for (OrderToLe orderToLe : orderEntity.get().getOrderToLes()) {
					LOGGER.info("Processing order to le for {}", orderToLe.getId());
					
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
				odrOrder = odrOrderRepository.save(odrOrder);
				odrOrderAttributeRepository.saveAll(odrAttributes);
				odrContractInfos = odrContractInfoRepository.saveAll(odrContractInfos);
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
				odrServiceDetailRepository.saveAll(odrServiceDetails);
				for (OdrServiceCommercial odrServiceCommercial : odrServiceCommercials) {
					LOGGER.info("Type {}", odrServiceCommercial.getServiceType());
					LOGGER.info("Saving {}", odrServiceCommercial.getOdrServiceDetail().getId());
					odrServiceCommercialRepository.save(odrServiceCommercial);
				}
				// odrServiceCommercialRepository.saveAll(odrServiceCommercials);

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
				}

				processOverLaydetails(odrOrder, orderEntity.get(), username, odrContractInfos.get(0),flowMapper);
				persistCgwDetails(odrOrder, orderEntity.get(), odrContractInfos.get(0), username,flowMapper);
				
				List<String> productNameList= new ArrayList<>();
				productNameList.add(IzosdwanCommonConstants.IZOSDWAN_NAME);
				productNameList.add(IzosdwanCommonConstants.IZOSDWAN_SOLUTION);
				List<OdrServiceDetail> odrServiceDetailsOverlay = odrServiceDetailRepository
						.findByOdrOrderAndErfPrdCatalogProductNameIn(odrOrder,productNameList);
				if (odrServiceDetailsOverlay != null && !odrServiceDetailsOverlay.isEmpty()) {
					odrServiceDetails.addAll(odrServiceDetailsOverlay);
					odrServiceDetailsOverlay.stream().forEach(service -> {
						List<OdrServiceCommercial> odrServiceCommercialsService = odrServiceCommercialRepository
								.findByOdrServiceDetail(service);
						if (odrServiceCommercialsService != null && !odrServiceCommercialsService.isEmpty()) {
							odrServiceCommercials.addAll(odrServiceCommercialsService);
						}
					});
				}
				
				List<OdrServiceDetail> odrServiceDetailsCgw = odrServiceDetailRepository
						.findByOdrOrderAndErfPrdCatalogProductName(odrOrder, IzosdwanCommonConstants.IZOSDWAN_CGW);
				if (odrServiceDetailsCgw != null && !odrServiceDetailsCgw.isEmpty()) {
					odrServiceDetails.addAll(odrServiceDetailsCgw);
					odrServiceDetailsCgw.stream().forEach(service -> {
						List<OdrServiceCommercial> odrServiceCommercialsService = odrServiceCommercialRepository
								.findByOdrServiceDetail(service);
						if (odrServiceCommercialsService != null && !odrServiceCommercialsService.isEmpty()) {
							odrServiceCommercials.addAll(odrServiceCommercialsService);
						}
					});
				}
				List<OdrSolutionComponent> odrSolutionComponents = odrSolutionComponentRepository
						.findByOdrOrder(odrOrder);
				OdrOrderBean odrOrderBean = odrMapper.mapOrderEntityToBean(odrOrder, odrContractInfos, odrAttributes,
						odrServiceDetails, odrServiceCommercials, odrSolutionComponents);
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
		for (OrderProductSolution orderProductSolution : orderToLeProdFamily.getOrderProductSolutions()) {
			if ("MACD".equals(orderToLe.getOrderType())) {
				if (odrOrder.getOpOrderCode().contains("IPC")) {
					LOGGER.info("MACD IPC Flow, setting uuid for ODR::" + orderToLe.getOrder().getQuote()
							.getQuoteToLes().stream().findFirst().get().getErfServiceInventoryTpsServiceId());
					flowMapper.put("serviceCode", orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst()
							.get().getErfServiceInventoryTpsServiceId());
				} else if (odrOrder.getOpOrderCode().contains("IAS") || odrOrder.getOpOrderCode().contains("GVPN")) {
					// QuoteToLe
					// quoteToLe=orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
					List<OrderIllSiteToService> orderIllSiteToServices = orderIllSiteToServiceRepository
							.findByOrderToLe_Id(orderToLe.getId());
					for (OrderIllSiteToService orderIllSiteToService : orderIllSiteToServices) {
						if (orderIllSiteToService.getType() != null
								&& orderIllSiteToService.getType().equalsIgnoreCase("secondary")) {
							flowMapper.put(orderIllSiteToService.getOrderIllSite().getSiteCode() + "-SEC",
									orderIllSiteToService.getErfServiceInventoryTpsServiceId());
						} else {
							flowMapper.put(orderIllSiteToService.getOrderIllSite().getSiteCode(),
									orderIllSiteToService.getErfServiceInventoryTpsServiceId());
						}
						flowMapper.put(String.valueOf(orderIllSiteToService.getOrderIllSite().getId()),
								orderIllSiteToService.getErfSfdcSubType());
					}
					// LOGGER.info("MACD ILL or GVPN Flow, setting uuid for ODR::" +
					// quoteIllSiteToServices.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
					// flowMapper.put("serviceCode",
					// quoteIllSiteToServices.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
				}
			}
			if (orderToLeProdFamily.getMstProductFamily().getName().equalsIgnoreCase(OdrConstants.IPC)) {
				flowMapper.put("offeringName", "IPC-CLOUD");
				processCloud(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username,
						odrAttributes);
			} else {
				flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
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
		for (OrderIzosdwanSite orderIllSite : orderProductSolution.getOrderIzosdwanSites()) {
			try {
				String type = null;
				if ("MACD".equals(odrOrder.getOrderType())) {
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
				HashSet<OdrComponentAttribute> odrComponentAttributes = new HashSet<>(primaryOdrComponent.getOdrComponentAttributes());
				primaryserviceDetail.setFlowStatus("NEW");
				primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"siteCode", orderIllSite.getSiteCode()));
				primaryserviceDetail.setServiceRefId(orderIllSite.getSiteCode());
				primaryserviceDetail.setOdrContractInfo(odrContractInfo);
				primaryserviceDetail.setOdrOrder(odrOrder);
				primaryserviceDetail.setServiceCommissionedDate(
						orderIllSite.getRequestorDate() != null ? orderIllSite.getRequestorDate()
								: orderIllSite.getEffectiveDate());
				primaryserviceDetail.setArc(orderIllSite.getArc());
				primaryserviceDetail.setMrc(orderIllSite.getMrc());
				primaryserviceDetail.setNrc(orderIllSite.getNrc());
				primaryserviceDetail.setErfPrdCatalogOfferingName(orderIllSite.getIzosdwanSiteOffering());
				primaryserviceDetail.setCreatedBy(username);
				primaryserviceDetail.setCreatedDate(new Date());
				primaryserviceDetail.setUpdatedDate(new Date());
				primaryserviceDetail.setUpdatedBy(username);
				primaryserviceDetail.setIsActive(CommonConstants.Y);
				primaryserviceDetail.setIsIzo(CommonConstants.N);
				primaryserviceDetail.setOrderType(CommonConstants.NEW);
				primaryserviceDetail.setRrfsDate(orderIllSite.getRequestorDate());
				primaryserviceDetail.setServiceCommissionedDate(orderIllSite.getEffectiveDate());
				if ("MACD".equals(odrOrder.getOrderType())
						&& flowMapper.containsKey(String.valueOf(orderIllSite.getId()))) {
					LOGGER.info("Order Sub Category");
					primaryserviceDetail.setOrderSubCategory(flowMapper.get(String.valueOf(orderIllSite.getId())));
				}
				if (orderIllSite.getIsTaxExempted() != null
						&& orderIllSite.getIsTaxExempted().equals(CommonConstants.BACTIVE)) {
					primaryserviceDetail.setTaxExemptionFlag(CommonConstants.Y);
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"taxExemption", CommonConstants.Y));
				} else {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"taxExemption", CommonConstants.N));
				}
				primaryserviceDetail.setErfPrdCatalogProductName(orderIllSite.getIzosdwanSiteProduct());
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(orderIllSite.getIzosdwanSiteProduct(), CommonConstants.BACTIVE);
				primaryserviceDetail.setErfPrdCatalogProductId(
						Objects.nonNull(mstProductFamily) ? mstProductFamily.getProductCatalogFamilyId() : null);

				if (orderIllSite.getErfServiceInventoryTpsServiceId() != null && !orderIllSite.getIzosdwanSiteProduct()
						.equals(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT)) {
					List<OrderIllSiteToService> orderIllSiteToServices = orderIllSiteToServiceRepository
							.findByOrderIzosdwanSite(orderIllSite);
					primaryserviceDetail.setUuid(orderIllSite.getErfServiceInventoryTpsServiceId());
					if (orderIllSiteToServices != null && !orderIllSiteToServices.isEmpty()) {
						primaryserviceDetail.setOrderType(IzosdwanCommonConstants.MACD_QUOTE_TYPE);
						primaryserviceDetail.setOrderCategory(orderIllSiteToServices.get(0).getErfSfdcOrderType());
						primaryserviceDetail.setOrderSubCategory(orderIllSiteToServices.get(0).getErfSfdcSubType());
					}
				} else {
					primaryserviceDetail.setOrderType(CommonConstants.NEW);
				}
				if (orderIllSite.getIzosdwanSiteProduct().equals(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT)) {
					primaryserviceDetail.setErfPrdCatalogOfferingName("HYBRID");
				}
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));

				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(orderIllSite.getErfLocSitebLocationId()));
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getPincode());
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
						primaryserviceDetail
								.setErfLocSiteAddressId(String.valueOf(orderIllSite.getErfLocSitebLocationId()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddressId", orderIllSite.getErfLocSitebLocationId() + ""));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddress", addr));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "latLong", addressDetail.getLatLong()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCountry", addressDetail.getCountry()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCity", addressDetail.getCity()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationAddressLineOne", addressDetail.getAddressLineOne()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationAddressLineTwo", addressDetail.getAddressLineTwo()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationLocality", addressDetail.getLocality()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationState", addressDetail.getState()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationPincode", addressDetail.getPincode()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCountryCode", addressDetail.getCountryCode()));
						try {
							if (StringUtils.isNotBlank(addressDetail.getCountry())) {
								LOGGER.info("Calling product queue for Intl Dial code {}", addressDetail.getCountry());
								String response = (String) mqUtils.sendAndReceive(productLocationQueue,
										addressDetail.getCountry());
								LOGGER.info("Response from product location queue {}", response);
								if (StringUtils.isNotBlank(response)) {
									ProductLocationBean productLocationBean = Utils.convertJsonToObject(response,
											ProductLocationBean.class);
									if (productLocationBean != null
											&& StringUtils.isNotBlank(productLocationBean.getIntlDialCode())) {
										LOGGER.info("Intl dial code for country --> {} is -->{}",
												addressDetail.getCountry(), productLocationBean.getIntlDialCode());
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
							}
						}catch(Exception e) {
							LOGGER.error("Error in product queue call for loaction",e);
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
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "demarcationBuildingName",
								demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
										: null));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "demarcationFloor",
								demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "demarcationWing",
								demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "demarcationRoom",
								demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null));
					}
				}

				if (orderIllSite.getErfLocSiteaLocationId() != null) {
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

							odrComponentAttributes
									.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
											"popSiteAddressId", orderIllSite.getErfLocSiteaLocationId() + ""));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "popSiteAddress", popAddr));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "popSiteCode", orderIllSite.getErfLocSiteaSiteCode()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "sourceCountry", popAddressDetail.getCountry()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "sourceCity", popAddressDetail.getCity()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "sourceAddressLineOne", popAddressDetail.getAddressLineOne()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "sourceAddressLineTwo", popAddressDetail.getAddressLineTwo()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "sourceLocality", popAddressDetail.getLocality()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "sourcePincode", popAddressDetail.getPincode()));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
									primaryOdrComponent, "sourceState", popAddressDetail.getState()));
						}
					}
				}
				if ("MACD".equals(odrOrder.getOrderType()) && !odrOrder.getOpOrderCode().contains("IPC")) {
					if (type != null && (type.equals("primary") || type.equals("dual"))) {
						odrServiceDetails.add(primaryserviceDetail);
					}
				} else {
					odrServiceDetails.add(primaryserviceDetail);
				}
				LOGGER.info("Saving secondary Attributes");
				if (flowMapper.containsKey(LeAttributesConstants.PO_DATE)) {
					primaryserviceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("PO DATE", flowMapper.get(LeAttributesConstants.PO_DATE), "IZOSDWAN_COMMON",
									primaryserviceDetail, CommonConstants.N, username));
				}
				if (flowMapper.containsKey(LeAttributesConstants.PO_NUMBER)) {
					primaryserviceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("PO NUMBER", flowMapper.get(LeAttributesConstants.PO_NUMBER),
									"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.OWNER_REGION)) {
					primaryserviceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute(IzosdwanCommonConstants.OWNER_REGION,
									flowMapper.get(IzosdwanCommonConstants.OWNER_REGION), "IZOSDWAN_COMMON",
									primaryserviceDetail, CommonConstants.N, username));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_COUNTRY)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_COUNTRY,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_COUNTRY)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.IS_BILLING_INTL)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.IS_BILLING_INTL,
							flowMapper.get(IzosdwanCommonConstants.IS_BILLING_INTL)));
				}
				Map<String,MstServiceClassificationDetail> serviceClassificationMap = getAllSiteClassificationDetails();
				processServiceAttributesBySiteTypeUnderlay(primaryserviceDetail, orderIllSite.getIzosdwanSiteType(), serviceClassificationMap, username);
				primaryOdrComponent.setOdrServiceDetail(primaryserviceDetail);
				primaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));
				primaryOdrComponent.setSiteType("A");
				primaryserviceDetail.setOdrComponents(null);
				primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
				processCommonComponentAttr(String.valueOf(orderIllSite.getId()), primaryserviceDetail, username,
						flowMapper, primaryOdrComponent, false, orderIllSite);
				LOGGER.info("Pri sec for site id {} is {}", orderIllSite.getId(), orderIllSite.getPriSec());
				Order order = orderIllSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrder();
				Optional<OrderIzosdwanAttributeValue> optOrderAttribute = order.getOrderIzosdwanAttributeValue().stream().filter(attribute -> IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE.equalsIgnoreCase(attribute.getDisplayValue())).findFirst();
				boolean isNsQuote = optOrderAttribute.isPresent() ? CommonConstants.Y.equals(optOrderAttribute.get().getAttributeValue()) : false;
				processProductComponentAttr(String.valueOf(orderIllSite.getId()), primaryserviceDetail,
						(orderIllSite.getPriSec() != null ? (orderIllSite.getPriSec().toLowerCase()) : "primary"),
						username, false, odrServiceCommercials, primaryOdrComponent, false,
						IzosdwanCommonConstants.IZOSDWAN_NAME, isNsQuote);
				LOGGER.info("Total Number of primary Service attributes {}",
						primaryserviceDetail.getOdrServiceAttributes().size());
				processSiteSla(orderIllSite, primaryserviceDetail, username);
				if (orderIllSite.getManagementType() == null
						|| orderIllSite.getManagementType().toLowerCase().contains("unmanaged")) {
					primaryOdrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(
							primaryserviceDetail, primaryOdrComponent, "isCPEManagedAlready", "false"));
				} else {
					primaryOdrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(
							primaryserviceDetail, primaryOdrComponent, "isCPEManagedAlready", "true"));
				}
				if (orderIllSite.getIsShared() == null || orderIllSite.getIsShared().equals(CommonConstants.N)) {
					primaryOdrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
									IzosdwanCommonConstants.CPE_SHARED_OR_NOT, CommonConstants.N));
				} else {
					primaryOdrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
									IzosdwanCommonConstants.CPE_SHARED_OR_NOT, CommonConstants.Y));
				}

				for (OdrComponent odrComp : primaryserviceDetail.getOdrComponents()) {
					odrComp.setOdrServiceDetail(primaryserviceDetail);
					for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
						LOGGER.info("setting primary reference");
						odrComponentAttribute.setOdrServiceDetail(primaryserviceDetail);
					}
				}
				LOGGER.info("Removing duplicates from service attributes, size of attributes list is {}",
						primaryserviceDetail.getOdrServiceAttributes().size());
				HashMap<String, OdrServiceAttribute> serviceAttrMap = new HashMap<>();
				primaryserviceDetail.getOdrServiceAttributes().stream().forEach(attr -> {
					serviceAttrMap.put(attr.getAttributeName(), attr);
				});

				primaryserviceDetail.setOdrServiceAttributes(new HashSet<>(serviceAttrMap.values()));
				LOGGER.info(
						"After removing duplicates from service attributes, size of attributes list is {} and map is {}",
						primaryserviceDetail.getOdrServiceAttributes().size(), serviceAttrMap.toString());
				// for (OdrServiceAttribute odrComp :
				// primaryserviceDetail.getOdrServiceAttributes()) {
				// odrComp.setOdrServiceDetail(primaryserviceDetail);
				// }

			} catch (Exception e) {
				LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
			}

		}
	}

	/**
	 * persistOdrComponent
	 * 
	 * @param primaryserviceDetail
	 * @param odrComponentAttributes
	 * @return
	 */
	private OdrComponent persistOdrComponent(OdrServiceDetail primaryserviceDetail) {
		OdrComponent odrComponent = new OdrComponent();
		odrComponent.setComponentName("LM");
		odrComponent.setOdrServiceDetail(primaryserviceDetail);
		odrComponent.setCreatedBy(Utils.getSource());
		odrComponent.setCreatedDate(new Date());
		// odrComponent.setSiteType(siteType);
		odrComponent.setIsActive(CommonConstants.Y);
		odrComponent.setUuid(Utils.generateUid());
		odrComponent.setOdrComponentAttributes(new HashSet<>());
		return odrComponent;
	}

	/**
	 * 
	 * persistOdrComponentAttributes- persists the component Attr
	 * 
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
			odrComponentAttribute = odrComponentAttributeRepository
					.findFirstByOdrComponentAndAttributeName(odrComponent, attrName);
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
	private void persistAttachments(Map<String, String> flowMapper, String username, OrderIzosdwanSite orderIllSite,
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
					odrAttachment.setOfferingName(orderIllSite.getIzosdwanSiteOffering());
					odrAttachment.setProductName(orderIllSite.getIzosdwanSiteProduct());
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
	private void processSiteSla(OrderIzosdwanSite orderIllSite, OdrServiceDetail primaryserviceDetail,
			String username) {
		List<Map<String, String>> siteSlas = orderIzosdwanSiteSlaRepository
				.findByOrderIzosdwanSiteId(orderIllSite.getId());
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


	public void processCommonComponentAttr(String refId, OdrServiceDetail serviceDetail, String username,
			Map<String, String> flowMapper, OdrComponent odrComponent, Boolean isOverlay, OrderIzosdwanSite orderIllSite)
			throws TclCommonException, IllegalArgumentException {
		LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference Id {}", refId,
				getReferenceName());
		List<OrderIzosdwanSiteFeasibility> orderSiteFeasibilitys = orderIzosdwanSiteFeasibilityRepository
				.findByOrderIzosdwanSiteIdAndIsSelected(Integer.valueOf(refId), CommonConstants.BACTIVE);
		List<String> components = new ArrayList<>();
		components.add(IzosdwanCommonConstants.SITE_PROPERTIES);
		List<Map<String, String>> oderProdCompAttrs = new ArrayList<>();
		if (isOverlay != null && isOverlay) {
			List<String> attributes = IzosdwanUtils.getCpeAttributesForSdwanOverlay();
			attributes.add("GSTNO");
			attributes.add("GST_ADDRESS");
			attributes.add("LOCAL_IT_CONTACT");
			attributes.add("TAX_EXCEMPTED_FILENAME");
			attributes.add("TAX_EXCEMPTED_ATTACHMENTID");
			oderProdCompAttrs = OrderProductComponentsAttributeValueRepository.findByRefidAndRefNameOverlay(refId,
					getReferenceName(), components, attributes);
			odrComponent.getOdrComponentAttributes().add(
					persistOdrComponentAttributes(serviceDetail, odrComponent, "templateName", CommonConstants.EMPTY));
			
		} else {
			oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
					.findByRefidAndRefNameUnderlay(refId, getReferenceName(), components, IzosdwanUtils.getCpeAttributesForSdwanOverlay());
			odrComponent.getOdrComponentAttributes()
			.add(persistOdrComponentAttributes(serviceDetail, odrComponent, IzosdwanCommonConstants.CPE_MANGEMENT_TYPE, IzosdwanCommonConstants.FULLY_MANAGED));
			
		}
		Boolean isPowerCordPresent = false;
		Boolean isSfpPresent = false;
		Boolean isSfpPlusPresent = false;
		Boolean isNMCPresent = false;
		Boolean isRackPresent = false;
		Double routerCost = 0D;
		Double rackmountCost = 0D;
		Double sfpCost = 0D;
		Double sfpPlusCost = 0D;
		Double nmcCost = 0D;
		Double powerCordCost = 0D;
		Boolean isRouterCost = false;
		Boolean isRackmountCost = false;
		Boolean isSfpCost = false;
		Boolean isSfpPlusCost = false;
		Boolean isNmcCost = false;
		Boolean isPowerCordCost = false;
		
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.RACKMOUNT)) {
				isRackPresent = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.SFP)) {
				isSfpPresent = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.SFP_PLUS)) {
				isSfpPlusPresent = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.NMC)) {
				isNMCPresent = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.POWER_CORD)) {
				isPowerCordPresent = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.ROUTER_COST)) {
				if (StringUtils.isNotBlank(attrValue)) {
					routerCost = Double.valueOf(attrValue);
				}
				isRouterCost = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.RACKMOUNT_COST)) {
				if (StringUtils.isNotBlank(attrValue)) {
					rackmountCost = Double.valueOf(attrValue);
				}
				isRackmountCost = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.SFP_COST)) {
				if (StringUtils.isNotBlank(attrValue)) {
					sfpCost = Double.valueOf(attrValue);
				}
				isSfpCost = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.SFP_PLUS_COST)) {
				if (StringUtils.isNotBlank(attrValue)) {
					sfpPlusCost = Double.valueOf(attrValue);
				}
				isSfpPlusCost = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.NMC_COST)) {
				if (StringUtils.isNotBlank(attrValue)) {
					nmcCost = Double.valueOf(attrValue);
				}
				isNmcCost = true;
			}
			if (attrName.equalsIgnoreCase(IzosdwanCommonConstants.POWER_CORD_COST)) {
				if (StringUtils.isNotBlank(attrValue)) {
					powerCordCost = Double.valueOf(attrValue);
				}
				isPowerCordCost = true;
			}
		}
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstNumber", attrValue.trim()));
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				OdrGstAddress odrGstAddress = null;
				if (addServiceParam.isPresent()) {
					String address = addServiceParam.get().getValue();
					if (StringUtils.isNotBlank(address)) {
						/*GstAddressBean gstAddress = Utils.convertJsonToObject(address, GstAddressBean.class);
						LOGGER.info("Site Gst address is {} ", gstAddress.toString());
						odrGstAddress = serviceDetail.getOdrGstAddress();
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
						odrGstAddressRepository.save(odrGstAddress);
						//serviceDetail.setOdrGstAddress(odrGstAddress);
						// odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
						// odrComponent, "siteGstAddress",address));
						LOGGER.info("Site Gst address id {} for service id {} " ,odrGstAddress.getId(),serviceDetail.getId());*/
						odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
								odrComponent, "siteGstAddress", address));
					} else {
						LOGGER.warn("Address is empty for id {}", attrValue);
					}

					/*odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "siteGstAddressId", odrGstAddress.getId().toString()));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "siteGstAddress", address));*/
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption", CommonConstants.Y));
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
				odrComponent.getOdrComponentAttributes()
				.add(persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
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
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
									localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
									localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
				odrComponent.getOdrComponentAttributes()
				.add(persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
			} else if (attrName.equals(IzosdwanCommonConstants.ACCESS_TYPE) && StringUtils.isNotBlank(attrValue)) {
				if(orderSiteFeasibilitys==null || orderSiteFeasibilitys.isEmpty()) {

					if (attrValue.equalsIgnoreCase("Onnet Wireless"))
						attrValue = "OnnetRF";
					else if (attrValue.equalsIgnoreCase("Onnet Wireline"))
						attrValue = "OnnetWL";
					else if (attrValue.equalsIgnoreCase("Offnet Wireless"))
						attrValue = "OffnetRF";
					else if (attrValue.equalsIgnoreCase("Offnet Wireline"))
						attrValue = "OffnetWL";
				serviceDetail.setAccessType(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						IzosdwanCommonConstants.LM_TYPE, attrValue));
				}
				else {
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
							"accessType", attrValue));
				}
			} else if (attrName.equals(IzosdwanCommonConstants.PORT_MODE) && StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "portMode", attrValue));
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("portMode",
						attrValue, category, serviceDetail, CommonConstants.N, username));
			} else if (attrName.equals(IzosdwanCommonConstants.INTERNET_QUALITY) && StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "internetQuality", attrValue));
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("internetQuality",
						attrValue, category, serviceDetail, CommonConstants.N, username));
			} else if (attrName.equals(IzosdwanCommonConstants.THIRDPARTY_SERVICE_ID)
					&& StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "thirdPartyServiceID", attrValue));
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("thirdPartyServiceID",
						attrValue, category, serviceDetail, CommonConstants.N, username));
			} else if (attrName.equals(IzosdwanCommonConstants.THIRDPARTY_IP_ADDRESS)
					&& StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "thirdPartyIPAddress", attrValue));
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("thirdPartyIPAddress",
						attrValue, category, serviceDetail, CommonConstants.N, username));
			} else if (attrName.equals(IzosdwanCommonConstants.THIRDPARTY_PROVIDER_NAME)
					&& StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						"thirdPartyProviderName", attrValue));
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("thirdPartyProviderName",
						attrValue, category, serviceDetail, CommonConstants.N, username));
			} else if (attrName.equals(IzosdwanCommonConstants.THIRDPARTY_LINK_UPTIME)
					&& StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						"thirdPartylinkUptimeAgreement", attrValue));
				serviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("thirdPartylinkUptimeAgreement", attrValue,
								category, serviceDetail, CommonConstants.N, username));
			} else if (attrName.equals(IzosdwanCommonConstants.LASTMILE_PROVIDER)
					&& StringUtils.isNotBlank(attrValue)) {
				
				if (orderSiteFeasibilitys == null || orderSiteFeasibilitys.isEmpty()) {
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
							serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
							.findFirstByAttributeNameAndOdrServiceDetail_IdAndCategory("closest_provider_bso_name", serviceDetail.getId(),"FEASIBILITY");
					if (odrServiceAttribute == null) {
						odrServiceAttribute = new OdrServiceAttribute();
						odrServiceAttribute.setCreatedBy(username);
						odrServiceAttribute.setCreatedDate(new Date());
					}
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName("closest_provider_bso_name");
					odrServiceAttribute.setAttributeValue(attrValue);
					odrServiceAttribute.setCategory("FEASIBILITY");
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
					
					OdrServiceAttribute odrServiceAttribute1 = odrServiceAttributeRepository
							.findFirstByAttributeNameAndOdrServiceDetail_IdAndCategory("BHConnectivity", serviceDetail.getId(),"FEASIBILITY");
					if (odrServiceAttribute1 == null) {
						odrServiceAttribute1 = new OdrServiceAttribute();
						odrServiceAttribute1.setCreatedBy(username);
						odrServiceAttribute1.setCreatedDate(new Date());
					}
					odrServiceAttribute1.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute1.setAttributeName("BHConnectivity");
					odrServiceAttribute1.setAttributeValue(attrValue);
					odrServiceAttribute1.setCategory("FEASIBILITY");
					odrServiceAttribute1.setCreatedBy(username);
					odrServiceAttribute1.setCreatedDate(new Date());
					odrServiceAttribute1.setIsActive(CommonConstants.Y);
					odrServiceAttribute1.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute1.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute1.setUpdatedBy(username);
					odrServiceAttribute1.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute1);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
							"lastMileProvider", attrValue));
					OdrServiceAttribute odrServiceAttribute2 = odrServiceAttributeRepository
							.findFirstByAttributeNameAndOdrServiceDetail_IdAndCategory("solution_type", serviceDetail.getId(),"FEASIBILITY");
					if (odrServiceAttribute2 == null) {
						odrServiceAttribute2 = new OdrServiceAttribute();
						odrServiceAttribute2.setCreatedBy(username);
						odrServiceAttribute2.setCreatedDate(new Date());
					}
					odrServiceAttribute2.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute2.setAttributeName("solution_type");
					odrServiceAttribute2.setAttributeValue(attrValue);
					odrServiceAttribute2.setCategory("FEASIBILITY");
					odrServiceAttribute2.setCreatedBy(username);
					odrServiceAttribute2.setCreatedDate(new Date());
					odrServiceAttribute2.setIsActive(CommonConstants.Y);
					odrServiceAttribute2.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute2.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute2.setUpdatedBy(username);
					odrServiceAttribute2.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute2);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
							"solution_type", attrValue));
				}
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
				

			} else if (attrName.equals("Underlay Profile Selection") && StringUtils.isNotBlank(attrValue) && attrValue.equalsIgnoreCase("SINGLE")) {
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("PRI_SEC", attrValue, category, serviceDetail, CommonConstants.N, username));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "PRI_SEC", attrValue.trim()));
			}else if (attrName.equals("Burstable Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBurstableBwPortspeed(attrValue.trim());
				serviceDetail.setBurstableBwPortspeedAltName(attrValue.trim());
				serviceDetail.setBurstableBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						"burstableBandwidth", attrValue.trim()));
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBwUnit", "Mbps"));
			} else if (attrName.equals("Port Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				Integer roundUpValue = new Double(attrValue.trim()).intValue();
				serviceDetail.setBwPortspeed(String.valueOf(roundUpValue));
				serviceDetail.setBwPortspeedAltName(String.valueOf(roundUpValue));
				serviceDetail.setBwUnit("Mbps");
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("portBandwidth",
						String.valueOf(roundUpValue), category, serviceDetail, CommonConstants.N, username));
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "portBandwidth", String.valueOf(roundUpValue)));
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "bwUnit", "Mbps"));
			} else if (attrName.equals("Local Loop Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				Integer roundUpValue = new Double(attrValue.trim()).intValue();
				serviceDetail.setLastmileBw(String.valueOf(roundUpValue));
				serviceDetail.setLastmileBwAltName(String.valueOf(roundUpValue));
				serviceDetail.setLastmileBwUnit("Mbps");
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("localLoopBandwidth",
						String.valueOf(roundUpValue), category, serviceDetail, CommonConstants.N, username));
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						"localLoopBandwidth", String.valueOf(roundUpValue)));
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "localLoopBandwidthUnit", "Mbps"));
			} else if (attrName.equals("CPE") && StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeType", attrValue));
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute(attrName, attrValue, category,
						serviceDetail, CommonConstants.N, username));
			} else if (attrName.equals("CPE Basic Chassis") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("Underlay or overlay {}",isOverlay);
				LOGGER.info("MDC Filter token value in before Queue call product BOM Details {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("calling the product with request {}", attrValue);
				LOGGER.info("Rack --> {}, SFP --> {}, SFP+ --> {}, NMC --> {}, Power --> {}",isRackPresent,isSfpPresent,isSfpPlusPresent,isNMCPresent,isPowerCordPresent);
				String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue, attrValue);
				try {
					List<CpeBomDto> cpeBomResourcesFinal = new ArrayList<>();
					List<CpeBomDto> cpeBomResources = IzosdwanUtils.fromJson(productBomDetailsStr, new TypeReference<List<CpeBomDto>>() {
					});
					if(cpeBomResources!=null && !cpeBomResources.isEmpty()) {
						LOGGER.info("After converting the response from the product to bean {}",Utils.convertObjectToJson(cpeBomResources));
						for(CpeBomDto resource: cpeBomResources) {
							LOGGER.info("Looping inside the boms");
							CpeBomDto cpeBomResource = new CpeBomDto();
							cpeBomResource.setBomName(resource.getBomName());
							cpeBomResource.setId(resource.getId());
							cpeBomResource.setUniCode(resource.getUniCode());
							List<ResourceDto> bomResources = new ArrayList<>();
							ResourceDto routerRes = new ResourceDto();
							for(ResourceDto res : resource.getResources()) {
								LOGGER.info("Resource category --> {}",res.getProductCategory());
								if(res.getProductCategory().startsWith("CPE -")) {
									//bomResources.add(res);
									res.setQuantity(1);
									if(isRouterCost) {res.setListPrice(routerCost);
									LOGGER.info("RouterCost present in static BOM:{}",res.getListPrice());}
									routerRes = res;
									odrComponent.getOdrComponentAttributes()
									.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "cpePartCode", res.getProductCode()));
								}else {
									if(res.getProductCategory().contains("Rack") && isRackPresent) {
										LOGGER.info("Rack present in static BOM");
										res.setQuantity(1);
										if(isRackmountCost) {res.setListPrice(rackmountCost);
										LOGGER.info("RackCost present in static BOM:{}",res.getListPrice());}
										bomResources.add(res);
									}
									if(res.getProductCategory().contains("NMC") && isNMCPresent) {
										LOGGER.info("NMC present in static BOM");
										res.setQuantity(1);
										if(isNmcCost) {res.setListPrice(nmcCost);
										LOGGER.info("nmcCost present in static BOM:{}",res.getListPrice());}
										bomResources.add(res);
									}
									if(res.getProductCategory().contains("SFP") && !res.getProductCategory().contains("SFP+") && isSfpPresent) {
										LOGGER.info("SFP present in static BOM");
										res.setQuantity(1);
										if(isSfpCost) {res.setListPrice(sfpCost);
										LOGGER.info("sfpCost present in static BOM:{}",res.getListPrice());}
										bomResources.add(res);
									}
									if(!res.getProductCategory().contains("SFP") && res.getProductCategory().contains("SFP+") && isSfpPlusPresent) {
										LOGGER.info("SFP+ present in static BOM");
										res.setQuantity(1);
										if(isSfpPlusCost) {res.setListPrice(sfpPlusCost);
										LOGGER.info("sfpPlusCost present in static BOM:{}",res.getListPrice());}
										bomResources.add(res);
									}
									if(res.getProductCategory().contains("Power") && isPowerCordPresent) {
										LOGGER.info("PowerCord present in static BOM");
										res.setQuantity(1);
										if(isPowerCordCost) {res.setListPrice(powerCordCost);
										LOGGER.info("powerCordCost present in static BOM:{}",res.getListPrice());}
										bomResources.add(res);
									}
								}
							}
							bomResources.add(0,routerRes);
							cpeBomResource.setResources(bomResources);
							cpeBomResourcesFinal.add(cpeBomResource);
						
						}
					}
					if(cpeBomResourcesFinal!=null && !cpeBomResourcesFinal.isEmpty()) {
						LOGGER.info("Got final dynamic bom resources for {}",serviceDetail.getUuid());
						productBomDetailsStr = Utils.convertObjectToJson(cpeBomResourcesFinal);
						LOGGER.info("Final Dynamic bom resources for {}",productBomDetailsStr);
					}
				}catch(Exception e) {
					
				}
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
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {} --> addition service param id {} --> addition service param value {}", attrName, attrValue,
							serviceDetail.getPrimarySecondary(),odrAdditionalServiceParam.getId(),odrAdditionalServiceParam.getValue());
					
					
					OdrComponentAttribute odrComponentAttributeCpe = persistOdrComponentAttributes(serviceDetail,
							odrComponent, attrName, odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					odrComponentAttributeCpe.setIsAdditionalParam(CommonConstants.Y);
					odrComponent.getOdrComponentAttributes().add(odrComponentAttributeCpe);
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, "EQUIPMENT_MODEL", attrValue));
					// TO:DO device name mapping
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeDeviceName", attrValue));
					
				}
				
			} else if (attrName.equals(IzosdwanCommonConstants.BYON_4G_LTE)
					&& StringUtils.isNotBlank(attrValue)) {
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("byonNetworkType",
						attrValue, category, serviceDetail, CommonConstants.N, username));
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "byonNetworkType", attrValue.trim()));
			} else if (attrName.equals(IzosdwanCommonConstants.OLD_LOCAL_LOOP_BANDWIDTH)
					&& StringUtils.isNotBlank(attrValue)) {

				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
						serviceDetail.getPrimarySecondary());
				OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
						.findFirstByAttributeNameAndOdrServiceDetail_IdAndCategory("old_Ll_Bw", serviceDetail.getId(),"FEASIBILITY");
				if (odrServiceAttribute == null) {
					odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
				}
				odrServiceAttribute.setAttributeAltValueLabel(attrValue);
				odrServiceAttribute.setAttributeName("old_Ll_Bw");
				odrServiceAttribute.setAttributeValue(attrValue);
				odrServiceAttribute.setCategory("FEASIBILITY");
				odrServiceAttribute.setIsActive(CommonConstants.Y);
				odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
				odrServiceAttribute.setOdrServiceDetail(serviceDetail);
				odrServiceAttribute.setUpdatedBy(username);
				odrServiceAttribute.setUpdatedDate(new Date());
				serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				// }else{
				// LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
				// }

				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
			} else if (attrName.equals("Interface") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setSiteEndInterface(attrValue);
				serviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("interface",
						attrValue, category, serviceDetail, CommonConstants.N, username));
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "interface", attrValue));
			} else if (attrName.equals("CPE Management Type") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setServiceOption(attrValue);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeManagementType", attrValue));
			} else if (attrName.equals(IzosdwanCommonConstants.CPE_MODEL_END_OF_LIFE) && StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeModelEndOfLife", attrValue));
			} else if (attrName.equals(IzosdwanCommonConstants.CPE_MODEL_END_OF_SALE) && StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeModelEndOfSale", attrValue));
			} else if (attrName.equals(IzosdwanCommonConstants.POP_SITE_ADDRESS) && StringUtils.isNotBlank(attrValue)) {
				if (orderSiteFeasibilitys == null || orderSiteFeasibilitys.isEmpty()) {
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, "popSiteAddress", attrValue));
					serviceDetail.setPopSiteAddress(attrValue);
				}
			} else if (attrName.equals(IzosdwanCommonConstants.POP_SITE_CODE) && StringUtils.isNotBlank(attrValue)) {
				if (!orderSiteFeasibilitys.isEmpty() ||orderSiteFeasibilitys != null) {
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, "popSiteCode", attrValue));
					serviceDetail.setPopSiteCode(attrValue);
				}
			} else if (attrName.equals(IzosdwanCommonConstants.POP_CITY) && StringUtils.isNotBlank(attrValue)) {
				if (!orderSiteFeasibilitys.isEmpty() ||orderSiteFeasibilitys != null) {
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, "sourceCity", attrValue));
					serviceDetail.setSourceCity(attrValue);;
				}
			} else if (attrName.equals(IzosdwanCommonConstants.POP_COUNTRY) && StringUtils.isNotBlank(attrValue)) {
				if (orderSiteFeasibilitys == null || orderSiteFeasibilitys.isEmpty()) {
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, "sourceCountry", attrValue));
					serviceDetail.setSourceCountry(attrValue);;
				}
			} else if (attrName.equals(IzosdwanCommonConstants.POP_STATE) && StringUtils.isNotBlank(attrValue)) {
				if (orderSiteFeasibilitys == null || orderSiteFeasibilitys.isEmpty()) {
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, "sourceState", attrValue));
					serviceDetail.setSourceState(attrValue);;
				}
			} else if (StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
						serviceDetail.getPrimarySecondary());
				OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
				odrServiceAttribute.setCreatedBy(username);
				odrServiceAttribute.setCreatedDate(new Date());

				odrServiceAttribute.setAttributeAltValueLabel(attrValue);
				odrServiceAttribute.setAttributeName(attrName);
				odrServiceAttribute.setAttributeValue(attrValue);
				odrServiceAttribute.setCategory(category);
				odrServiceAttribute.setIsActive(CommonConstants.Y);
				odrServiceAttribute.setOdrServiceDetail(serviceDetail);
				odrServiceAttribute.setUpdatedBy(username);
				odrServiceAttribute.setUpdatedDate(new Date());
				serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
			}

		}
		Order order = orderIllSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrder();
		Optional<OrderIzosdwanAttributeValue> optOrderAttribute = order.getOrderIzosdwanAttributeValue().stream().filter(attribute -> IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE.equalsIgnoreCase(attribute.getDisplayValue())).findFirst();
		String nsQuote = optOrderAttribute.isPresent() ? optOrderAttribute.get().getAttributeValue() : null;
		if (CommonConstants.Y.equals(nsQuote)) {
			if (StringUtils.isBlank(serviceDetail.getServiceTopology()) && IzosdwanCommonConstants.GVPN_PRODUCT.equals(serviceDetail.getErfPrdCatalogProductName())) {
				LOGGER.info("Setting default service topology: {} for custom GVPN underlay orderSiteId: {}", IzosdwanCommonConstants.FULL_MESH, orderIllSite.getId());
				serviceDetail.setServiceTopology(IzosdwanCommonConstants.FULL_MESH);
			}
			if (StringUtils.isBlank(serviceDetail.getServiceOption()) && IzosdwanCommonConstants.GVPN_PRODUCT.equals(serviceDetail.getErfPrdCatalogProductName())) {
				LOGGER.info("Setting default service option: {} for custom GVPN underlay orderSiteId: {}", IzosdwanCommonConstants.FULLY_MANAGED, orderIllSite.getId());
				serviceDetail.setServiceOption(IzosdwanCommonConstants.FULLY_MANAGED);
			}

		}
		odrComponent.getOdrComponentAttributes()
		.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeSiScope", "Supply, Installation, Support"));
		if(orderSiteFeasibilitys==null || orderSiteFeasibilitys.isEmpty()) {
			OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
					.findFirstByAttributeNameAndOdrServiceDetail_IdAndCategory("connected_cust_tag", serviceDetail.getId(),"FEASIBILITY");
			if (odrServiceAttribute == null) {
				odrServiceAttribute = new OdrServiceAttribute();
				odrServiceAttribute.setCreatedBy(username);
				odrServiceAttribute.setCreatedDate(new Date());
			}
			odrServiceAttribute.setAttributeAltValueLabel("1");
			odrServiceAttribute.setAttributeName("connected_cust_tag");
			odrServiceAttribute.setAttributeValue("1");
			odrServiceAttribute.setCategory("FEASIBILITY");
			odrServiceAttribute.setIsActive(CommonConstants.Y);
			odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
			odrServiceAttribute.setOdrServiceDetail(serviceDetail);
			odrServiceAttribute.setUpdatedBy(username);
			odrServiceAttribute.setUpdatedDate(new Date());
			serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
			OdrServiceAttribute odrServiceAttribute1 = odrServiceAttributeRepository
					.findFirstByAttributeNameAndOdrServiceDetail_IdAndCategory("connected_building_tag", serviceDetail.getId(),"FEASIBILITY");
			if (odrServiceAttribute1 == null) {
				odrServiceAttribute1 = new OdrServiceAttribute();
				odrServiceAttribute1.setCreatedBy(username);
				odrServiceAttribute1.setCreatedDate(new Date());
			}
			odrServiceAttribute1.setAttributeAltValueLabel("0");
			odrServiceAttribute1.setAttributeName("connected_building_tag");
			odrServiceAttribute1.setAttributeValue("0");
			odrServiceAttribute1.setCategory("FEASIBILITY");
			odrServiceAttribute1.setCreatedBy(username);
			odrServiceAttribute1.setCreatedDate(new Date());
			odrServiceAttribute1.setIsActive(CommonConstants.Y);
			odrServiceAttribute1.setIsAdditionalParam(CommonConstants.N);
			odrServiceAttribute1.setOdrServiceDetail(serviceDetail);
			odrServiceAttribute1.setUpdatedBy(username);
			odrServiceAttribute1.setUpdatedDate(new Date());
			serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute1);
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
			// OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
			// .findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName,
			// serviceDetail.getId(), category);
			// if (odrServiceAttribute == null) {
			LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
					serviceDetail.getPrimarySecondary());
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
			// }else{
			// LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
			// }

		}
		return serviceDetail;
	}

	/**
	 * Get nrc price from order price based on reference id
	 *
	 * @param referenceId
	 * @return
	 */
	private Double getOrderPriceNrc(Integer referenceId) {
		OrderPrice attrPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
				String.valueOf(referenceId), "ATTRIBUTES");
		if (attrPrice != null) {
			LOGGER.info("Attribute found in order price - id: {}", attrPrice.getId());
			return Double.parseDouble(decimalFormat.format(attrPrice.getEffectiveNrc()));
		}
		LOGGER.error("Order price not found for reference id:: {}", referenceId);
		return 0D;
	}

	/**
	 * Get CPE installation charges from order price using product component attribute reference.
	 *
	 * @param orderProductComponents
	 * @return
	 */
	private Double getNrcCpeInstallationCharges(List<OrderProductComponent> orderProductComponents) {
		Optional<OrderProductComponentsAttributeValue> attributeValueOpt = orderProductComponents.stream().filter(odrComponent -> odrComponent.getMstProductComponent().getName().equals(IzosdwanCommonConstants.CPE))
				.flatMap(odrComponent -> odrComponent.getOrderProductComponentsAttributeValues().stream())
				.filter(attr -> IzosdwanCommonConstants.CPE_INSTALLATION.equalsIgnoreCase(attr.getProductAttributeMaster().getName()))
				.findFirst();
		LOGGER.info("CPE Installation attribute found: {}", attributeValueOpt.isPresent());
		return attributeValueOpt.map(attributeValue -> getOrderPriceNrc(attributeValue.getId())).orElse(0D);
	}

	@SuppressWarnings("unchecked")
	public OdrServiceDetail processProductComponentAttr(String refId, OdrServiceDetail serviceDetail, String type,
			String userName, boolean isUpdate, List<OdrServiceCommercial> odrServiceCommercials,
			OdrComponent odrComponent, Boolean isOverlay,String productName, boolean isNsQuote)
			throws TclCommonException, IllegalArgumentException, ParseException {
		Double arc = 0D;
		Double nrc = 0D;
		Double mrc = 0D;
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndReferenceName(Integer.valueOf(refId), getReferenceName(productName));
		String cpeId = null;
		if (isNsQuote && isOverlay) {
			cpeId = getCpeId(refId);
			LOGGER.info("Got CPE id: {} for OrderIzosdwansiteId: {}", cpeId, refId);
		}
		for (OrderProductComponent orderProductComponent : orderProductComponents) {
			if ((isOverlay
					&& (orderProductComponent.getMstProductComponent().getName().equals(IzosdwanCommonConstants.CPE)
							|| orderProductComponent.getMstProductComponent().getName()
									.equalsIgnoreCase("IZO SDWAN service charges")))
					|| (!isOverlay && !(orderProductComponent.getMstProductComponent().getName()
							.equals(IzosdwanCommonConstants.CPE)
							|| orderProductComponent.getMstProductComponent().getName()
									.equalsIgnoreCase("IZO SDWAN service charges")))) {
				LOGGER.info("Incoming component for commercial {}",orderProductComponent.getMstProductComponent().getName());
				OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
						orderProductComponent.getId() + CommonConstants.EMPTY, "COMPONENTS");
				if (orderPrice != null) {
					Double orderPriceNrc;
					if (orderProductComponent.getMstProductComponent().getName().equalsIgnoreCase("IZO SDWAN service charges") && isNsQuote) {
						// Add cpe installation changes in nrc prices for IZO SDWAN service changes component.
						Double cpeInstallationCharges = getNrcCpeInstallationCharges(orderProductComponents);
						orderPriceNrc = cpeInstallationCharges + (Objects.nonNull(orderPrice.getEffectiveNrc()) ? orderPrice.getEffectiveNrc() : 0);
					} else {
						orderPriceNrc = orderPrice.getEffectiveNrc();
					}
					String categoryName = orderProductComponent.getMstProductComponent().getName();
					String arcAttrName = categoryName + "_Arc_Price";
					String nrcAttrName = categoryName + "_Nrc_Price";
					String mrcAttrName = categoryName + "_Mrc_Price";
					arc = arc + (Objects.nonNull(orderPrice.getEffectiveArc()) ? orderPrice.getEffectiveArc() : 0);
					nrc = nrc + (Objects.nonNull(orderPriceNrc) ? orderPriceNrc : 0);
					mrc = mrc + (Objects.nonNull(orderPrice.getEffectiveMrc()) ? orderPrice.getEffectiveMrc() : 0);
					addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveArc(), categoryName, arcAttrName);
					addServiceAttr(serviceDetail, userName, orderPriceNrc, categoryName, nrcAttrName);
					addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveMrc(), categoryName, mrcAttrName);
					OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
					odrServiceCommercial.setArc(orderPrice.getEffectiveArc());
					odrServiceCommercial.setMrc(orderPrice.getEffectiveMrc());
					odrServiceCommercial.setNrc(orderPriceNrc);
					odrServiceCommercial.setReferenceName(orderProductComponent.getMstProductComponent().getName());
					odrServiceCommercial.setReferenceType("COMPONENTS");
					odrServiceCommercial.setOdrServiceDetail(serviceDetail);
					odrServiceCommercial.setServiceType(type);
					odrServiceCommercial.setOdrComponent(odrComponent);
					if (isNsQuote && isOverlay && isCpeComponent(odrServiceCommercial)) {
						odrServiceCommercial.setComponentReferenceName(cpeId);
					}
					odrServiceCommercials.add(odrServiceCommercial);
				}

				for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponent
						.getOrderProductComponentsAttributeValues()) {
					OrderPrice attrOrderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
							orderProductComponentsAttributeValue.getId() + CommonConstants.EMPTY, "ATTRIBUTES");
					if (attrOrderPrice != null && (attrOrderPrice.getEffectiveArc() != null
							|| attrOrderPrice.getEffectiveMrc() != null || attrOrderPrice.getEffectiveNrc() != null)) {
						OdrServiceCommercial  odrServiceCommercial = new OdrServiceCommercial();
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
						odrServiceCommercial.setOdrComponent(odrComponent);
						odrServiceCommercials.add(odrServiceCommercial);
					}
					if (attrOrderPrice != null && attrOrderPrice.getEffectiveUsagePrice() != null) {
						addServiceAttr(serviceDetail, userName, attrOrderPrice.getEffectiveUsagePrice(),
								orderProductComponent.getMstProductComponent().getName(), "usage_price_per_mb");
					}

				}
			}
		}
		serviceDetail.setArc(arc);
		serviceDetail.setMrc(mrc);
		serviceDetail.setNrc(nrc);
		List<String> components = new ArrayList<>();
		components.add(IzosdwanCommonConstants.SITE_PROPERTIES);
		List<Map<String, String>> oderProdCompAttrs = new ArrayList<>();
		if (isOverlay) {
			List<String> attributes = IzosdwanUtils.getCpeAttributesForSdwanOverlay();
			attributes.add("GSTNO");
			attributes.add("GST_ADDRESS");
			attributes.add("LOCAL_IT_CONTACT");
			LOGGER.info("Attributes list to be fetched {}",Utils.convertObjectToJson(attributes));
			oderProdCompAttrs = OrderProductComponentsAttributeValueRepository.findByRefidAndRefNameOverlay(refId,
					getReferenceName(), components, attributes);
		} else {
			serviceDetail.setPrimarySecondary(type);
			oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
					.findByRefidAndRefNameUnderlay(refId, getReferenceName(), components, IzosdwanUtils.getCpeAttributesForSdwanOverlay());
		}
		if (oderProdCompAttrs.isEmpty()) {
			LOGGER.info("Secondary not available for {} ,{} ,{}", refId, type, getReferenceName());
			return null;
		}
		LOGGER.info("Attributes list fetched {}",Utils.convertObjectToJson(oderProdCompAttrs));
		
		if (!isUpdate)
			processSiteFeasibility(refId, serviceDetail, type, userName, odrComponent);
		return serviceDetail;
	}

	private boolean isCpeComponent(OdrServiceCommercial odrServiceCommercial) {
		return (IzosdwanCommonConstants.CPE.equals(odrServiceCommercial.getReferenceName()) ||
				"IZO SDWAN service charges".equalsIgnoreCase(odrServiceCommercial.getReferenceName()));
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
				.findByAttributeNameAndOdrServiceDetail_IdAndCategory(arcAttrName, serviceDetail.getId(),categoryName);
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
	private OdrServiceDetail processLocalItContact(String refId, OdrServiceDetail serviceDetail, String username,
			OdrComponent odrComponent) throws TclCommonException, IllegalArgumentException, ParseException {
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

					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
									localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
									localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
			} else if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstNumber", attrValue.trim()));
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					String address = addServiceParam.get().getValue();
					/*GstAddressBean gstAddress = Utils.convertJsonToObject(address, GstAddressBean.class);
					LOGGER.info("Site Gst address is {} ", gstAddress.toString());
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
					odrGstAddressRepository.save(odrGstAddress);
					//serviceDetail.setOdrGstAddress(odrGstAddress);
					// odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
					// odrComponent, "siteGstAddress",address));
					LOGGER.info("Site Gst address id {} for service id {} " ,odrGstAddress.getId(),serviceDetail.getId());
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "siteGstAddressId", odrGstAddress.getId().toString()));*/
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "siteGstAddress", address));
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption", CommonConstants.Y));
			} else {
				// OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
				// .findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName,
				// serviceDetail.getId(),
				// category);
				// if (odrServiceAttribute == null) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
						serviceDetail.getPrimarySecondary());
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
				// }else{
				// LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
				// }

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
	public void processSiteFeasibility(String refId, OdrServiceDetail serviceDetail, String type, String userName,
			OdrComponent odrComponent) throws ParseException {
		List<OrderIzosdwanSiteFeasibility> orderSiteFeasibilitys = orderIzosdwanSiteFeasibilityRepository
				.findByOrderIzosdwanSiteIdAndIsSelectedAndType(Integer.valueOf(refId), CommonConstants.BACTIVE, type);
		for (OrderIzosdwanSiteFeasibility orderSiteFeasibility : orderSiteFeasibilitys) {

			String lmType = orderSiteFeasibility.getFeasibilityMode();

			if (lmType.equalsIgnoreCase("Onnet Wireless"))
				lmType = "OnnetRF";
			else if (lmType.equalsIgnoreCase("Onnet Wireline"))
				lmType = "OnnetWL";
			else if (lmType.equalsIgnoreCase("Offnet Wireless"))
				lmType = "OffnetRF";
			else if (lmType.equalsIgnoreCase("Offnet Wireline"))
				lmType = "OffnetWL";

			serviceDetail.setAccessType(lmType);
			serviceDetail.setLastmileType(lmType);
			serviceDetail.setFeasibilityId(orderSiteFeasibility.getFeasibilityCode());
			serviceDetail.setLastmileProvider(orderSiteFeasibility.getProvider());
			odrComponent.getOdrComponentAttributes()
					.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "accessType", lmType));
			odrComponent.getOdrComponentAttributes()
					.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "lmType", lmType));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
					"feasibilityId", orderSiteFeasibility.getFeasibilityCode()));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
					"lastMileProvider", orderSiteFeasibility.getProvider()));

			if (orderSiteFeasibility.getResponseJson() != null) {
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(orderSiteFeasibility.getResponseJson());
				Iterator<?> iter = jsonObject.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String attrKey = String.valueOf(entry.getKey());

					if ((attrKey.equalsIgnoreCase("access_rings")) || (attrKey.equalsIgnoreCase("mux_details") || "feasibility_remarks".equalsIgnoreCase(attrKey))) {
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
								|| "record_type".equalsIgnoreCase(attrKey) || "mast_type".equalsIgnoreCase(attrKey)
								|| "provider_reference_number".equalsIgnoreCase(attrKey)
								|| "cpe_variant".equalsIgnoreCase(attrKey)
								|| "cpe_chassis_changed".equalsIgnoreCase(attrKey)
								|| "cpe_variant".equalsIgnoreCase(attrKey)
								|| "lm_otc_nrc_installation_offwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_bw_offwl".equalsIgnoreCase(attrKey)
								|| "customer_segment".equalsIgnoreCase(attrKey)
								|| "vendor_name".equalsIgnoreCase(attrKey) || "vendor_id".equalsIgnoreCase(attrKey)
								|| "tcl_pop_address".equalsIgnoreCase(attrKey)
								|| "pop_address".equalsIgnoreCase(attrKey) || "pop_name".equalsIgnoreCase(attrKey)
								|| "pop_city".equalsIgnoreCase(attrKey) || "pop_state".equalsIgnoreCase(attrKey)
								|| "pop_country".equalsIgnoreCase(attrKey) || "local_loop_bw".equalsIgnoreCase(attrKey)
								|| "POP_DIST_KM_SERVICE_MOD".equalsIgnoreCase(attrKey)
								|| "mf_contract_term".equalsIgnoreCase(attrKey)
								|| "pop_network_loc_id".equalsIgnoreCase(attrKey)
								|| "ll_change".equalsIgnoreCase(attrKey) || "old_Port_Bw".equalsIgnoreCase(attrKey)
								|| "other_provider_name".equalsIgnoreCase(attrKey)
								|| "quotetype_quote".equalsIgnoreCase(attrKey) || "Type".equalsIgnoreCase(attrKey)
								|| "service_id".equalsIgnoreCase(attrKey)
								|| "ipv4_address_pool_size".equalsIgnoreCase(attrKey)
								|| "Orch_LM_Type".equalsIgnoreCase(attrKey)
								|| "old_contract_term".equalsIgnoreCase(attrKey)
								|| "old_Ll_Bw".equalsIgnoreCase(attrKey)
								|| "service_commissioned_date".equalsIgnoreCase(attrKey)
								|| "Orch_Category".equalsIgnoreCase(attrKey) || "resp_city".equalsIgnoreCase(attrKey)
								|| "opportunity_term".equalsIgnoreCase(attrKey)
								|| "account_id_with_18_digit".equalsIgnoreCase(attrKey)
								|| "sales_org".equalsIgnoreCase(attrKey)
								|| "access_feasibility_category".equalsIgnoreCase(attrKey)
								|| "provider_request_date".equalsIgnoreCase(attrKey)
								|| "Orch_Connection".equalsIgnoreCase(attrKey)
								|| "cpe_supply_type".equalsIgnoreCase(attrKey) || "lm_type".equalsIgnoreCase(attrKey)
								|| "topology".equalsIgnoreCase(attrKey) || "macd_option".equalsIgnoreCase(attrKey)
								|| "response_related_to".equalsIgnoreCase(attrKey)
								|| "burstable_bw".equalsIgnoreCase(attrKey) || "cu_le_id".equalsIgnoreCase(attrKey)
								|| "last_mile_contract_term".equalsIgnoreCase(attrKey)
								|| "Predicted_Access_Feasibility".equalsIgnoreCase(attrKey)
								|| "sfdc_feasibility_id".equalsIgnoreCase(attrKey)
								|| "partner_profile".equalsIgnoreCase(attrKey) || "lat_long".equalsIgnoreCase(attrKey)
								|| "parallel_run_days".equalsIgnoreCase(attrKey)
								|| "lm_interface_change".equalsIgnoreCase(attrKey)
								|| "backup_port_requested".equalsIgnoreCase(attrKey)
								|| "ipv6_address_pool_size".equalsIgnoreCase(attrKey)
								|| "vpn_Name".equalsIgnoreCase(attrKey) || "connection_type".equalsIgnoreCase(attrKey)
								|| "lm_otc_modem_charges_offwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_modem_charges_offwl".equalsIgnoreCase(attrKey)
								|| "lm_nrc_bw_onwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_bw_onwl".equalsIgnoreCase(attrKey)
								|| "lm_arc_bw_onrf".equalsIgnoreCase(attrKey)
								|| "lm_nrc_bw_onrf".equalsIgnoreCase(attrKey)
								|| "lm_nrc_mast_onrf".equalsIgnoreCase(attrKey) || "task_id".equalsIgnoreCase(attrKey))
								&& entry.getValue() != null) {

							String attrValue = String.valueOf(entry.getValue());
							if ("provider_name".equals(attrKey)) {
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
							} else if ("pop_network_loc_id".equals(attrKey)) {
								odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(
										serviceDetail, odrComponent, "popSiteCode", attrValue));
							}

							switch (attrKey) {
							case "primary_bts_ip_address":
								attrKey = "bts_IP_PMP";
								break;
							case "primary_bts_name":
								attrKey = "bts_site_name";
								break;
							case "provider_name":
								attrKey = "solution_type";
								break;
							case "primary_first_sector_id":
								attrKey = "sector_id";
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
	
	
	/**
	 * processOrder
	 */
	public void processOrder(Order order, OdrOrder odrOrder, String userName) {
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
	}

	public void processOrderToLe(OrderToLe orderToLe, OdrOrder odrOrder, boolean flag, OdrContractInfo odrContractInfo,
			String userName) throws TclCommonException {
		if (flag) {
			odrOrder.setErfCustLeId(orderToLe.getErfCusCustomerLegalEntityId());
			odrOrder.setOrderCategory(orderToLe.getOrderCategory());
			if ("MACD".equals(orderToLe.getOrderType())) {
				odrOrder.setOrderType(orderToLe.getOrderType());
			} else {
				odrOrder.setOrderType(orderToLe.getOrderCategory() == null ? "NEW" : orderToLe.getOrderCategory());
			}
			/*
			 * if ("MACD".equals(orderToLe.getOrderType())) { List<OrderIllSiteToService>
			 * orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderToLe_Id(
			 * orderToLe.getId()); if(Objects.nonNull(orderIllSiteToServices) &&
			 * !orderIllSiteToServices.isEmpty()){ Optional<OrderIllSiteToService>
			 * orderIllSiteToServiceOptional=orderIllSiteToServices.stream().findFirst();
			 * if(orderIllSiteToServiceOptional.isPresent()){ OrderIllSiteToService
			 * orderIllSiteToService=orderIllSiteToServiceOptional.get();
			 * if(Objects.nonNull(orderIllSiteToService.getErfSfdcSubType())){
			 * LOGGER.info("Update Sub Category for MACD::",orderIllSiteToService.
			 * getErfSfdcSubType());
			 * odrOrder.setOrderSubCategory(orderIllSiteToService.getErfSfdcSubType()); } }
			 * } }
			 */
			odrOrder.setErfOrderLeId(orderToLe.getId());
			odrOrder.setOrderSource(OdrConstants.OPTIMUS);
			odrOrder.setErfCustSpLeId(orderToLe.getErfCusSpLegalEntityId());
			odrOrder.setIsBundleOrder(orderToLe.getIsBundle() != null ? String.valueOf(orderToLe.getIsBundle()) : null);
			odrOrder.setOpportunityClassification(OdrConstants.SELL_TO);
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
			LOGGER.info("attribute name :-:{} attribute value:-: {}", attrName, attrValue);
			if (attrName.equals(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)) {
				odrOrder.setErfCustSpLeName(attrValue);
				odrContractInfo.setErfCustSpLeName(attrValue);
			} else if (attrName.equals(LeAttributesConstants.LEGAL_ENTITY_NAME)) {
				odrOrder.setErfCustLeName(attrValue);
				odrContractInfo.setErfCustLeName(attrValue);
			} else if (attrName.equals(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)) {
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
			} /*
				 * else if (attrName.equals(LeAttributesConstants.BILLING_ADDRESS)) {
				 * odrContractInfo.setBillingAddress(attrValue); }
				 */ else if (attrName.equals(LeAttributesConstants.BILLING_METHOD)) {
				odrContractInfo.setBillingMethod(attrValue);
			} else if (attrName.equals(LeAttributesConstants.BILLING_CONTACT_ID)) {
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
				if (odrOrder.getOpOrderCode().startsWith(CommonConstants.IPC)) {
					odrContractInfo.setBillingAddressLine3(
							odrContractInfo.getBillingCity() + "," + odrContractInfo.getBillingCountry());
				}
			} else if (attrName.equals(LeAttributesConstants.PAYMENT_TERM)) {
				odrContractInfo.setPaymentTerm(attrValue);
			} else if (attrName.equals(LeAttributesConstants.LE_STATE_GST_NO)) {
				getContractGstAddress(odrOrder, attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName, attrValue);
			}else if (attrName.equals(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY)) {
				getContractAddress(odrOrder,attrValue,odrAttributes,userName);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName,attrValue);
			} else if (odrOrder.getOpOrderCode().startsWith(CommonConstants.IPC)
					&& (attrName.equals(LeAttributesConstants.NAME) || attrName.equals(LeAttributesConstants.EMAIL_ID)
							|| attrName.equals(LeAttributesConstants.NUMBER))) {
				flowMapper.put(attrName, attrValue);
				// addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName,
				// attrValue); //NOSONAR
			}else if (attrName.equals(LeAttributesConstants.PO_NUMBER)) {
				flowMapper.put(attrName, attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName, attrValue);
			}else if (attrName.equals(LeAttributesConstants.PO_DATE)) {
				flowMapper.put(attrName, attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName, attrValue);
			}else if (attrName.equals(IzosdwanCommonConstants.OWNER_REGION)) {
				flowMapper.put(attrName, attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName, attrValue);
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
								for (OrderIzosdwanSite orderIllSite : orderProductSolution.getOrderIzosdwanSites()) {
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
		List<OrderIzosdwanSite> illSite = orderIzosdwanSiteRepository
				.findBySiteCodeAndStatus(odrServiceDetail.getServiceRefId(), CommonConstants.BACTIVE);
		for (OrderIzosdwanSite orderIllSite : illSite) {
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

	private Map<String, String> getOrderStatus(OrderIzosdwanSite illSite) {
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
	public void processOrderEnrichmentForSites(String username, OrderIzosdwanSite orderIllSite,
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

	private void processOeSite(String username, OrderIzosdwanSite orderIllSite, Map<String, String> flowMapper,
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
					List<OdrComponent> odrComponents = odrComponentRepository
							.findByOdrServiceDetailAndComponentNameAndSiteType(odrServiceDetail, "LM", "A");
					OdrComponent odrComponent = null;
					for (OdrComponent odrComponentEntity : odrComponents) {
						odrComponent = odrComponentEntity;
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
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
									"demarcationBuildingName",
									demarDetails.get("BUILDING_NAME") != null
											? (String) demarDetails.get("BUILDING_NAME")
											: null));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "demarcationFloor",
									demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "demarcationWing",
									demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "demarcationRoom",
									demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null));
					odrComponentRepository.save(odrComponent);
					processLocalItContact(String.valueOf(orderIllSite.getId()), odrServiceDetail, username,
							odrComponent);
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
								odrAttachment.setOfferingName(orderIllSite.getIzosdwanSiteOffering());
								odrAttachment.setProductName(orderIllSite.getIzosdwanSiteProduct());
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
		LOGGER.error("inside contract level gst:: {} for legal id:: {}", gstNo, odrOrder.getErfCustLeId());
		SiteGstDetail siteGstDetail = new SiteGstDetail();
		siteGstDetail.setGstNo(gstNo);
		siteGstDetail.setCustomerLeId(odrOrder.getErfCustLeId());
		try {
			String leGst = (String) mqUtils.sendAndReceive(siteGstQueue, Utils.convertObjectToJson(siteGstDetail));
			if (StringUtils.isNotBlank(leGst)) {
				LeStateInfo leStateInfo = (LeStateInfo) Utils.convertJsonToObject(leGst, LeStateInfo.class);
				if (leStateInfo != null) {
					LOGGER.error("Gst Response::{}", leStateInfo.toString());
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

	private void processOverLaydetails(OdrOrder odrOrder, Order order, String username,
			OdrContractInfo odrContractInfo,Map<String, String> flowMapper) {
		LOGGER.info("processOverLaydetails method invoked");
		if (order != null && order.getOrderToLes() != null) {
			for (OrderToLe orderToLe : order.getOrderToLes()) {
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(IzosdwanCommonConstants.IZOSDWAN_NAME, CommonConstants.BACTIVE);
				if (mstProductFamily != null) {
					OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
							.findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
					if (orderToLeProductFamily != null) {
						List<OrderProductSolution> orderProductSolutions = orderProductSolutionRepository
								.findByOrderToLeProductFamily(orderToLeProductFamily);
						if (orderProductSolutions != null && !orderProductSolutions.isEmpty()) {

							for (OrderProductSolution orderProductSolution : orderProductSolutions) {
								OdrServiceDetail solutionOdrServiceDetail = new OdrServiceDetail();
								solutionOdrServiceDetail.setServiceRefId(Utils.generateUid());
								solutionOdrServiceDetail.setErfPrdCatalogOfferingName(
										orderProductSolution.getMstProductOffering().getProductName());
								solutionOdrServiceDetail.setCreatedBy(username);
								solutionOdrServiceDetail.setCreatedDate(new Date());
								solutionOdrServiceDetail.setUpdatedDate(new Date());
								solutionOdrServiceDetail.setUpdatedBy(username);
								solutionOdrServiceDetail.setIsActive(CommonConstants.Y);
								solutionOdrServiceDetail.setIsIzo(CommonConstants.N);
								solutionOdrServiceDetail.setErfPrdCatalogProductName(
										IzosdwanCommonConstants.IZOSDWAN_SOLUTION);
								solutionOdrServiceDetail
										.setErfPrdCatalogProductId(orderProductSolution.getMstProductOffering()
												.getMstProductFamily().getProductCatalogFamilyId());
								solutionOdrServiceDetail.setOdrOrder(odrOrder);
								solutionOdrServiceDetail.setOdrOrderUuid(odrOrder.getUuid());
								solutionOdrServiceDetail.setOrderType(CommonConstants.NEW);
								List<OrderIzosdwanSite> orderIzosdwanSites = orderIzosdwanSiteRepository
										.findByOrderProductSolutionAndStatus(orderProductSolution,
												CommonConstants.BACTIVE);
								OdrComponent solutionOdrComponent=persistOdrComponent(solutionOdrServiceDetail);
								Set<OdrComponentAttribute> odrComponentAttributes = new HashSet<>();
								if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)) {
									odrComponentAttributes.add(persistOdrComponentAttributes(solutionOdrServiceDetail,
											solutionOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS,
											flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)));
								}
								if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS)) {
									odrComponentAttributes.add(persistOdrComponentAttributes(solutionOdrServiceDetail,
											solutionOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS,
											flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS)));
								}
								if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_COUNTRY)) {
									odrComponentAttributes.add(persistOdrComponentAttributes(solutionOdrServiceDetail,
											solutionOdrComponent, IzosdwanCommonConstants.SUPPLIER_COUNTRY,
											flowMapper.get(IzosdwanCommonConstants.SUPPLIER_COUNTRY)));
								}
								if (flowMapper.containsKey(IzosdwanCommonConstants.IS_BILLING_INTL)) {
									odrComponentAttributes.add(persistOdrComponentAttributes(solutionOdrServiceDetail,
											solutionOdrComponent, IzosdwanCommonConstants.IS_BILLING_INTL,
											flowMapper.get(IzosdwanCommonConstants.IS_BILLING_INTL)));
								}
								solutionOdrComponent.setOdrComponentAttributes(odrComponentAttributes);
								solutionOdrComponent.setSiteType("A");
								solutionOdrComponent.setOdrServiceDetail(solutionOdrServiceDetail);
								solutionOdrServiceDetail.getOdrComponents().add(solutionOdrComponent);
								solutionOdrServiceDetail = odrServiceDetailRepository.saveAndFlush(solutionOdrServiceDetail);
								Map<String,MstServiceClassificationDetail> serviceClassificationMap = getAllSiteClassificationDetails();
								Optional<OrderIzosdwanAttributeValue> optOrderAttribute = order.getOrderIzosdwanAttributeValue().stream().filter(attribute -> IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE.equalsIgnoreCase(attribute.getDisplayValue())).findFirst();
								boolean isNsQuote = optOrderAttribute.isPresent() ? CommonConstants.Y.equals(optOrderAttribute.get().getAttributeValue()) : false;
								if (orderIzosdwanSites != null && !orderIzosdwanSites.isEmpty()) {
									List<Integer> locationIds = orderIzosdwanSites.stream()
											.map(site -> site.getErfLocSitebLocationId()).distinct()
											.collect(Collectors.toList());
									for (Integer locationId : locationIds) {
										if (isNsQuote) {
											processServiceEntriesForSdwanOverlaysForCustomOrder(locationId, orderIzosdwanSites.stream()
															.filter(site -> site.getErfLocSitebLocationId().equals(locationId))
															.collect(Collectors.toList()), odrOrder, odrContractInfo,
													orderProductSolution, username, solutionOdrServiceDetail,
													serviceClassificationMap,flowMapper);
										} else {
											processServiceEntriesForSdwanOverlays(locationId, orderIzosdwanSites.stream()
															.filter(site -> site.getErfLocSitebLocationId().equals(locationId))
															.collect(Collectors.toList()), odrOrder, odrContractInfo,
													orderProductSolution, username, solutionOdrServiceDetail,
													serviceClassificationMap,flowMapper);
										}

									}
								}
							}
						}
					}
				}
			}
		}

	}

	private void processServiceEntriesForSdwanOverlaysForCustomOrder(Integer locationId, List<OrderIzosdwanSite> orderIzosdwanSites,
													   OdrOrder odrOrder, OdrContractInfo odrContractInfo, OrderProductSolution orderProductSolution,
													   String username, OdrServiceDetail solutionOdrServiceDetail,Map<String,MstServiceClassificationDetail> serviceClassificationMap,Map<String, String> flowMapper) {
		try {
			Map<OdrServiceDetail, OdrComponent> cpeComponetsMap = new HashMap<>();
			String countryCode = CommonConstants.EMPTY;
			OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();

			primaryserviceDetail.setFlowStatus("NEW");
			primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
			String serviceRefId = Utils.generateUid();

			primaryserviceDetail.setServiceRefId(serviceRefId);
			primaryserviceDetail.setOdrContractInfo(odrContractInfo);
			primaryserviceDetail.setOdrOrder(odrOrder);
			primaryserviceDetail.setServiceCommissionedDate(
					orderIzosdwanSites.get(0).getRequestorDate() != null ? orderIzosdwanSites.get(0).getRequestorDate()
							: orderIzosdwanSites.get(0).getEffectiveDate());
			// primaryserviceDetail.setArc(orderIllSite.getArc());
			// primaryserviceDetail.setMrc(orderIllSite.getMrc());
			// primaryserviceDetail.setNrc(orderIllSite.getNrc());
			primaryserviceDetail
					.setErfPrdCatalogOfferingName(orderProductSolution.getMstProductOffering().getProductName());
			primaryserviceDetail.setCreatedBy(username);
			primaryserviceDetail.setCreatedDate(new Date());
			primaryserviceDetail.setUpdatedDate(new Date());
			primaryserviceDetail.setUpdatedBy(username);
			primaryserviceDetail.setIsActive(CommonConstants.Y);
			primaryserviceDetail.setIsIzo(CommonConstants.N);
			primaryserviceDetail.setOrderType(CommonConstants.NEW);
			primaryserviceDetail.setErfPrdCatalogProductName(orderProductSolution.getMstProductOffering().getMstProductFamily().getName());
			primaryserviceDetail.setErfPrdCatalogProductId(orderProductSolution.getMstProductOffering()
					.getMstProductFamily().getProductCatalogFamilyId());

			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(orderIzosdwanSites.get(0).getErfLocSitebLocationId()));
			if (StringUtils.isNotBlank(locationResponse)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				if (addressDetail != null) {
					String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getPincode());
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
							String.valueOf(orderIzosdwanSites.get(0).getErfLocSitebLocationId()));

				}
			}
			primaryserviceDetail = odrServiceDetailRepository.save(primaryserviceDetail);

			List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();

			flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
			flowMapper.put("productName", orderProductSolution.getMstProductOffering().getMstProductFamily().getName());
			
			String vpnName = CommonConstants.EMPTY;
			String directorInstanceMapping = CommonConstants.EMPTY;
			
			String cpeFailoverType = CommonConstants.EMPTY;
			List<String> cpeFailoverTypeList=new ArrayList<>();
			for (OrderIzosdwanSite orderIzosdwanSite : orderIzosdwanSites) {
				/*if (cpeFailoverType.equals(CommonConstants.EMPTY)) {
					cpeFailoverType = cpeFailoverType.concat(getAttributeValue(orderIzosdwanSite.getId(),
							IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.PORT_MODE));
				} else {
					cpeFailoverType = cpeFailoverType.concat("-").concat(getAttributeValue(orderIzosdwanSite.getId(),
							IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.PORT_MODE));
				}*/
				vpnName = getAttributeValue(orderIzosdwanSite.getId(),IzosdwanCommonConstants.SITE_PROPERTIES,"Overlay VPN");
				directorInstanceMapping = getAttributeValue(orderIzosdwanSite.getId(),IzosdwanCommonConstants.SITE_PROPERTIES,"Director Instance Mapping");
				
				primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("vpnName",
						vpnName, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
				primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("directorInstanceMapping",
						directorInstanceMapping, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
				
				cpeFailoverType = getAttributeValue(orderIzosdwanSite.getId(),IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.PORT_MODE);
				cpeFailoverTypeList.add(cpeFailoverType);
				LOGGER.info("failover types {} list for the location id {}", cpeFailoverTypeList, locationId);
			}
			cpeFailoverType = CommonConstants.EMPTY;
			if (!cpeFailoverTypeList.isEmpty()) {
				Collections.sort(cpeFailoverTypeList);
				LOGGER.info("failover types after sorting : {}", cpeFailoverTypeList);
				cpeFailoverType = StringUtils.join(cpeFailoverTypeList,"-");
				LOGGER.info("failover types after joining : {}", cpeFailoverType);

			}
			LOGGER.info("failover type {}", cpeFailoverType);
			primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("CPE_FAILOVER",
					cpeFailoverType, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
			
			
//			primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("templateName",
//					CommonConstants.EMPTY, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
			if (flowMapper.containsKey(LeAttributesConstants.PO_DATE)) {
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("PO DATE", flowMapper.get(LeAttributesConstants.PO_DATE), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));
			}
			if (flowMapper.containsKey(LeAttributesConstants.PO_NUMBER)) {
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("PO NUMBER", flowMapper.get(LeAttributesConstants.PO_NUMBER), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));
			}
			if (flowMapper.containsKey(IzosdwanCommonConstants.OWNER_REGION)) {
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute(IzosdwanCommonConstants.OWNER_REGION,
								flowMapper.get(IzosdwanCommonConstants.OWNER_REGION), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));
			}

			if (orderProductSolution.getProductProfileData() != null) {
				String offeringData = orderProductSolution.getProductProfileData();
				LOGGER.info("Offering data {}", offeringData);

				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("offeringName",
								orderProductSolution.getMstProductOffering().getProductName(), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));

				OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
				odrAdditionalServiceParam.setAttribute("offeringData");
				odrAdditionalServiceParam.setCategory("OFFERING_DATA");
				odrAdditionalServiceParam.setCreatedBy(username);
				odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
				odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
				odrAdditionalServiceParam.setValue(offeringData);
				odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("offeringData",
								odrAdditionalServiceParam.getId() + CommonConstants.EMPTY, "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.Y, username));

				if (orderProductSolution.getMstProductOffering().getMstProductFamily().getName().startsWith("IZO")) {
					ProductOfferingsBean productOfferingsBean = Utils.convertJsonToObject(
							orderProductSolution.getProductProfileData(), ProductOfferingsBean.class);
					Boolean additionalSecurityAddon = false;
					if (productOfferingsBean != null && productOfferingsBean.getAddons() != null) {

						AddonsBean addon = productOfferingsBean.getAddons().stream()
								.filter(detail -> detail.getName().equalsIgnoreCase("Advanced Security")).findFirst()
								.orElse(null);
						if (addon != null) {
							additionalSecurityAddon = true;
							primaryserviceDetail.getOdrServiceAttributes()
									.add(constructOdrServiceAttribute("securityAddon", addon.getName(),
											"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
							if (addon.getDescription() != null && !addon.getDescription().isEmpty()) {
								OdrAdditionalServiceParam odrAdditionalServiceParam1 = new OdrAdditionalServiceParam();
								odrAdditionalServiceParam1.setAttribute("securityAddonDesc");
								odrAdditionalServiceParam1.setCategory("OFFERING_DATA");
								odrAdditionalServiceParam1.setCreatedBy(username);
								odrAdditionalServiceParam1.setCreatedTime(new Timestamp(new Date().getTime()));
								odrAdditionalServiceParam1.setIsActive(CommonConstants.Y);
								odrAdditionalServiceParam1.setValue(Utils.convertObjectToJson(addon.getDescription()));
								odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam1);
								primaryserviceDetail.getOdrServiceAttributes()
										.add(constructOdrServiceAttribute("securityAddonDesc",
												odrAdditionalServiceParam1.getId() + CommonConstants.EMPTY,
												"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.Y, username));
							}
						} else {
							addon = productOfferingsBean.getAddons().stream()
									.filter(detail -> detail.getName().equalsIgnoreCase("Basic Security")).findFirst()
									.orElse(null);
							if (addon != null) {
								additionalSecurityAddon = true;
								primaryserviceDetail.getOdrServiceAttributes()
										.add(constructOdrServiceAttribute("securityAddon", addon.getName(),
												"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
								if (addon.getDescription() != null && !addon.getDescription().isEmpty()) {
									OdrAdditionalServiceParam odrAdditionalServiceParam1 = new OdrAdditionalServiceParam();
									odrAdditionalServiceParam1.setAttribute("securityAddonDesc");
									odrAdditionalServiceParam1.setCategory("OFFERING_DATA");
									odrAdditionalServiceParam1.setCreatedBy(username);
									odrAdditionalServiceParam1.setCreatedTime(new Timestamp(new Date().getTime()));
									odrAdditionalServiceParam1.setIsActive(CommonConstants.Y);
									odrAdditionalServiceParam1
											.setValue(Utils.convertObjectToJson(addon.getDescription()));
									odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam1);
									primaryserviceDetail.getOdrServiceAttributes()
											.add(constructOdrServiceAttribute("securityAddonDesc",
													odrAdditionalServiceParam1.getId() + CommonConstants.EMPTY,
													"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.Y,
													username));
								}
							}
						}

					}
					primaryserviceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("isSecurityAddonApplicable",
									additionalSecurityAddon ? CommonConstants.Y : CommonConstants.N, "IZOSDWAN_COMMON",
									primaryserviceDetail, CommonConstants.N, username));
				}
			}
			List<Integer> llBwList = orderIzosdwanSites.stream().map(site->Integer.parseInt(site.getNewLastmileBandwidth())).collect(Collectors.toList());
			primaryserviceDetail.getOdrServiceAttributes()
					.add(constructOdrServiceAttribute("siteSize", Collections.max(llBwList).toString(),
							"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
			Integer count = 1;
			Map<String,OdrComponent> primaryServiceIdComponentMap = new HashMap<>();
			Map<String, OdrComponent> cpeIzosdwanSitesMap = new HashMap<>();
			for (OrderIzosdwanSite orderIzosdwanSite : orderIzosdwanSites) {
				OdrComponent primaryOdrComponent = null;
				String izosdwanCpeId = getCpeId(orderIzosdwanSite);

				LOGGER.info("Izosdwan site: {}, cpe Id: {}, isOdrComponentExist: {}", orderIzosdwanSite.getId(), izosdwanCpeId, cpeIzosdwanSitesMap.containsKey(izosdwanCpeId));

				if (cpeIzosdwanSitesMap.containsKey(izosdwanCpeId)) {
					LOGGER.info("Got shared Underlay count --> {} and shared component --> {}", count, cpeIzosdwanSitesMap.get(izosdwanCpeId).getId());
					primaryOdrComponent = cpeIzosdwanSitesMap.get(izosdwanCpeId);
					primaryOdrComponent.setSiteType("A");
				} else {
					primaryOdrComponent = persistOdrComponent(primaryserviceDetail);
					cpeIzosdwanSitesMap.put(izosdwanCpeId, primaryOdrComponent);
					if (count == 1) {
						primaryOdrComponent.setSiteType("A");
					} else if(count == 2){
						primaryOdrComponent.setSiteType("B");
					}else if(count == 3){
						primaryOdrComponent.setSiteType("C");
					}else if(count == 4){
						primaryOdrComponent.setSiteType("D");
					}else if(count == 5){
						primaryOdrComponent.setSiteType("E");
					}
				}
				primaryOdrComponent.setOdrServiceDetail(primaryserviceDetail);


				// primaryserviceDetail.setOdrComponents(null);
				Set<OdrComponentAttribute> odrComponentAttributes = primaryOdrComponent.getOdrComponentAttributes();
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						countryCode = addressDetail.getCountryCode();
						LOGGER.info("Country code--> {} for Country -->{}", addressDetail.getCountryCode(),
								addressDetail.getCountry());
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCountryCode", addressDetail.getCountryCode()));
					}
				}
				if (flowMapper.containsKey(LeAttributesConstants.PO_DATE)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							LeAttributesConstants.PO_DATE, flowMapper.get(LeAttributesConstants.PO_DATE)));
				}
				if (flowMapper.containsKey(LeAttributesConstants.PO_NUMBER)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							LeAttributesConstants.PO_NUMBER, flowMapper.get(LeAttributesConstants.PO_NUMBER)));
				}
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"siteCode", serviceRefId));
				primaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));

				if (orderIzosdwanSite.getIsTaxExempted() != null
						&& orderIzosdwanSite.getIsTaxExempted().equals(CommonConstants.BACTIVE)) {
					LOGGER.info("Tax exemption underlay present");
					primaryserviceDetail.setTaxExemptionFlag(CommonConstants.Y);
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"taxExemption", CommonConstants.Y));
				} else {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"taxExemption", CommonConstants.N));
				}
				processCommonComponentAttr(String.valueOf(orderIzosdwanSite.getId()), primaryserviceDetail,
						username, flowMapper, primaryOdrComponent, true, orderIzosdwanSite);
				LOGGER.info("Pri sec for site id {} is {}", orderIzosdwanSite.getId(),
						orderIzosdwanSite.getPriSec());
				processProductComponentAttr(String.valueOf(orderIzosdwanSite.getId()), primaryserviceDetail,
						(orderIzosdwanSite.getPriSec() != null ? (orderIzosdwanSite.getPriSec().toLowerCase())
								: "primary"),
						username, false, odrServiceCommercials, primaryOdrComponent, true,IzosdwanCommonConstants.IZOSDWAN_NAME, true);
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_COUNTRY)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_COUNTRY,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_COUNTRY)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.IS_BILLING_INTL)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.IS_BILLING_INTL,
							flowMapper.get(IzosdwanCommonConstants.IS_BILLING_INTL)));
				}
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getPincode());
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
								"destinationCountryCode", addressDetail.getCountryCode()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddressId",
								primaryserviceDetail.getErfLocSiteAddressId()));
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
								LOGGER.info("Calling product queue for Intl Dial code {}", addressDetail.getCountry());
								String response = (String) mqUtils.sendAndReceive(productLocationQueue,
										addressDetail.getCountry());
								LOGGER.info("Response from product location queue {}", response);
								if (StringUtils.isNotBlank(response)) {
									ProductLocationBean productLocationBean = Utils.convertJsonToObject(response,
											ProductLocationBean.class);
									if (productLocationBean != null
											&& StringUtils.isNotBlank(productLocationBean.getIntlDialCode())) {
										LOGGER.info("Intl dial code for country --> {} is -->{}",
												addressDetail.getCountry(), productLocationBean.getIntlDialCode());
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
							}
						}catch(Exception e) {
							LOGGER.error("Error in product queue call for loaction",e);
						}
					}

				}


				if (orderIzosdwanSite.getManagementType() == null
						|| orderIzosdwanSite.getManagementType().toLowerCase().contains("unmanaged")) {
					primaryOdrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(
							primaryserviceDetail, primaryOdrComponent, "isCPEManagedAlready", "false"));
				} else {
					primaryOdrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(
							primaryserviceDetail, primaryOdrComponent, "isCPEManagedAlready", "true"));
				}
				primaryOdrComponent.getOdrComponentAttributes().addAll(odrComponentAttributes);
				primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
				primaryOdrComponent = odrComponentRepository.save(primaryOdrComponent);
				primaryserviceDetail.setPrimarySecondary(IzosdwanCommonConstants.PRIMARY);
				primaryserviceDetail = odrServiceDetailRepository.save(primaryserviceDetail);

				Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository
						.findByServiceRefId(orderIzosdwanSite.getSiteCode());
				if (odrServiceDetails != null && !odrServiceDetails.isEmpty()) {
					cpeComponetsMap.put(odrServiceDetails.stream().findFirst().get(), primaryOdrComponent);

				}
				LOGGER.info("CPE is izosdwan site id: {}, adding odr component id: {} and service details id: {}", orderIzosdwanSite.getId(), primaryOdrComponent.getId(), odrServiceDetails.stream().findFirst().get().getId());

				if(orderIzosdwanSite.getIsShared()==null || orderIzosdwanSite.getIsShared().equals(CommonConstants.Y)) {
					primaryServiceIdComponentMap.put(orderIzosdwanSite.getPrimaryServiceId(), primaryOdrComponent);
					LOGGER.info("primaryServiceIdComponentMap: added site: {}", orderIzosdwanSite.getId());
				}

				count++;

			}

			Map<String, List<OdrServiceCommercial>> cpeAndIzosdwanChargesServiceCommercials = getCpeAndIzosdwanChargesComponents(odrServiceCommercials);
			odrServiceCommercials = excludeCpeAndIzosdwanChargeOverlayComponents(odrServiceCommercials);
			List<OdrServiceCommercial> cpeAndIzosdwanSumUpCharges = sumUpCpeAndIzosdwanChargesAndPersist(cpeAndIzosdwanChargesServiceCommercials);
			odrServiceCommercials.addAll(cpeAndIzosdwanSumUpCharges);

			odrServiceCommercialRepository.saveAll(odrServiceCommercials);

			LOGGER.info("Total Number of primary Service attributes {}",
					primaryserviceDetail.getOdrServiceAttributes().size());

			for (OdrComponent odrComp : primaryserviceDetail.getOdrComponents()) {
				odrComp.setOdrServiceDetail(primaryserviceDetail);
				for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
					LOGGER.info("setting primary reference");
					odrComponentAttribute.setOdrServiceDetail(primaryserviceDetail);
				}
			}

			LOGGER.info("Overlay Service id {}", primaryserviceDetail.getId());
			constructOdrSolutionComponents(solutionOdrServiceDetail, primaryserviceDetail, solutionOdrServiceDetail,
					null, odrOrder, IzosdwanCommonConstants.OVERLAY);

			cpeComponetsMap.forEach((odrServiceDetail, odrComponent) -> {
				LOGGER.info("cpeComponetsMap: {}, {}", odrServiceDetail.getId(), odrComponent.getId());
			});
			for (OrderIzosdwanSite orderIzosdwanSite : orderIzosdwanSites) {
				Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository
						.findByServiceRefId(orderIzosdwanSite.getSiteCode());
				if (odrServiceDetails != null && !odrServiceDetails.isEmpty()) {
					if (cpeComponetsMap.containsKey(odrServiceDetails.stream().findFirst().get())) {
						LOGGER.info("Constructing odr solution component for site: {}, Service details id: {}, and odr component: {}", orderIzosdwanSite.getId(), odrServiceDetails.stream().findAny().get().getId(), cpeComponetsMap.get(odrServiceDetails.stream().findFirst().get()).getId());
						constructOdrSolutionComponents(solutionOdrServiceDetail,
								odrServiceDetails.stream().findAny().get(), primaryserviceDetail,
								cpeComponetsMap.get(odrServiceDetails.stream().findFirst().get()), odrOrder,
								IzosdwanCommonConstants.UNDERLAY);
					}
				}

			}
			odrServiceCommercialRepository.saveAll(odrServiceCommercials);
			if (orderIzosdwanSites != null && !orderIzosdwanSites.isEmpty()) {
				Order order = orderIzosdwanSites.get(0).getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrder();
				LOGGER.info("Non Standard Order: {} IZOSDWAN site type {} for service id {} map is {}",
						CommonConstants.Y, orderIzosdwanSites.get(0).getIzosdwanSiteType(), primaryserviceDetail.getUuid(),
						serviceClassificationMap);
				// Custom journey service attributes for overlay by custom custom site type.
				Optional<OrderToLe> orderToLe = order.getOrderToLes().stream().findFirst();
				Integer orderToLeId = orderToLe.map(OrderToLe::getId).get();
				processServiceAttributesBySiteTypeForCustomJourney(primaryserviceDetail,
						IzosdwanCommonConstants.CUSTOM, serviceClassificationMap, username, orderIzosdwanSites.size(), locationId, orderToLeId);
			}

		} catch (Exception e) {
			LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
		}

	}

	private List<OdrServiceCommercial> excludeCpeAndIzosdwanChargeOverlayComponents(List<OdrServiceCommercial> serviceCommercials) {
		return serviceCommercials.stream()
				.filter(serviceCommercial -> !(IzosdwanCommonConstants.COMPONENTS.equalsIgnoreCase(serviceCommercial.getReferenceType()) && IzosdwanCommonConstants.CPE.equals(serviceCommercial.getReferenceName())))
				.filter(serviceCommercial -> !(IzosdwanCommonConstants.COMPONENTS.equalsIgnoreCase(serviceCommercial.getReferenceType()) && "IZO SDWAN service charges".equals(serviceCommercial.getReferenceName())))
				.collect(Collectors.toList());
	}

	private List<OdrServiceCommercial> sumUpCpeAndIzosdwanChargesAndPersist(Map<String, List<OdrServiceCommercial>> cpeAndIzosdwanChargesServiceCommercials) {

		List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();

		for (Map.Entry<String, List<OdrServiceCommercial>> mapEntry : cpeAndIzosdwanChargesServiceCommercials.entrySet()) {
			Double totalCpeNrc = 0D;
			Double totalCpeArc = 0D;
			Double totalCpeMrc = 0D;
			Double totalIzosdwanChargesNrc = 0D;
			Double totalIzosdwanChargesArc = 0D;
			Double totalIzosdwanChargesMrc = 0D;
			List<OdrServiceCommercial> serviceCommercials = mapEntry.getValue();
			LOGGER.info("Calculating total CPE and Izosdwan charges for CPE id: {}", mapEntry.getKey());
			if (!CollectionUtils.isEmpty(serviceCommercials)) {
				for (OdrServiceCommercial serviceCommercial: serviceCommercials) {
					if (IzosdwanCommonConstants.CPE.equals(serviceCommercial.getReferenceName())) {
						totalCpeNrc += serviceCommercial.getNrc();
						totalCpeArc += serviceCommercial.getArc();
						totalCpeMrc += serviceCommercial.getMrc();
					} else if ("IZO SDWAN service charges".equalsIgnoreCase(serviceCommercial.getReferenceName())) {
						totalIzosdwanChargesNrc += serviceCommercial.getNrc();
						totalIzosdwanChargesArc += serviceCommercial.getArc();
						totalIzosdwanChargesMrc += serviceCommercial.getMrc();
					}
				}

				OdrServiceCommercial odrServiceCommercialCpe = new OdrServiceCommercial();
				odrServiceCommercialCpe.setArc(totalCpeArc);
				odrServiceCommercialCpe.setMrc(totalCpeMrc);
				odrServiceCommercialCpe.setNrc(totalCpeNrc);
				odrServiceCommercialCpe.setReferenceName(IzosdwanCommonConstants.CPE);
				odrServiceCommercialCpe.setReferenceType("COMPONENTS");
				odrServiceCommercialCpe.setOdrServiceDetail(serviceCommercials.get(0).getOdrServiceDetail());
				odrServiceCommercialCpe.setServiceType(serviceCommercials.get(0).getServiceType());
				odrServiceCommercialCpe.setOdrComponent(serviceCommercials.get(0).getOdrComponent());
				odrServiceCommercials.add(odrServiceCommercialCpe);
				LOGGER.info("Calculated CPE component total arc: {}, nrc: {} and mrc: {} for CPE id: {}", totalCpeArc, totalCpeNrc, totalCpeMrc, mapEntry.getKey());

				OdrServiceCommercial odrServiceCommercialIzosdwanCharges = new OdrServiceCommercial();
				odrServiceCommercialIzosdwanCharges.setArc(totalIzosdwanChargesArc);
				odrServiceCommercialIzosdwanCharges.setMrc(totalIzosdwanChargesMrc);
				odrServiceCommercialIzosdwanCharges.setNrc(totalIzosdwanChargesNrc);
				odrServiceCommercialIzosdwanCharges.setReferenceName("IZO SDWAN service charges");
				odrServiceCommercialIzosdwanCharges.setReferenceType("COMPONENTS");
				odrServiceCommercialIzosdwanCharges.setOdrServiceDetail(serviceCommercials.get(0).getOdrServiceDetail());
				odrServiceCommercialIzosdwanCharges.setServiceType(serviceCommercials.get(0).getServiceType());
				odrServiceCommercialIzosdwanCharges.setOdrComponent(serviceCommercials.get(0).getOdrComponent());
				odrServiceCommercials.add(odrServiceCommercialIzosdwanCharges);
				LOGGER.info("Calculated IZO SDWAN service charges total arc: {}, nrc: {} and mrc: {} for CPE id: {}", totalIzosdwanChargesArc, totalIzosdwanChargesNrc, totalIzosdwanChargesMrc, mapEntry.getKey());
			}
		}

		return odrServiceCommercials;

	}

	private String getCpeId(OrderIzosdwanSite orderIzosdwanSite) {
		List<OrderProductComponent> components = orderProductComponentRepository.findByReferenceIdAndMstProductFamily_NameAndReferenceName(orderIzosdwanSite.getId(),
				orderIzosdwanSite.getOrderProductSolution().getOrderToLeProductFamily().getMstProductFamily().getName(), IzosdwanCommonConstants.IZOSDWAN_SITES);
		if (!CollectionUtils.isEmpty(components)) {
			LOGGER.info("Product components found for order izosdwan site: {} ", orderIzosdwanSite.getId());
			Optional<OrderProductComponentsAttributeValue> optCpeIdAttribute = components.stream()
					.filter(component -> !CollectionUtils.isEmpty(component.getOrderProductComponentsAttributeValues()))
					.flatMap(component -> component.getOrderProductComponentsAttributeValues().stream())
					.filter(attribute -> IzosdwanCommonConstants.SDWAN_CPE_ID.equalsIgnoreCase(attribute.getProductAttributeMaster().getName()))
					.findFirst();
			LOGGER.info("CPE ID attribute found: {} for order izosdwan site: {} ", optCpeIdAttribute.isPresent(), orderIzosdwanSite.getId());
			return optCpeIdAttribute.map(OrderProductComponentsAttributeValue::getAttributeValues).orElse(null);
		}
		LOGGER.info("Product components not found for order izosdwan site: {} ", orderIzosdwanSite.getId());
		return null;
	}

	private String getCpeId(String orderIzosdwanSiteId) {
		return orderIzosdwanSiteRepository.findById(Integer.valueOf(orderIzosdwanSiteId)).map(this::getCpeId).orElse(null);
	}

	private Map<String, List<OdrServiceCommercial>> getCpeAndIzosdwanChargesComponents(List<OdrServiceCommercial> serviceCommercials) {
		if (!CollectionUtils.isEmpty(serviceCommercials)) {

			Map<String, List<OdrServiceCommercial>> cpeIzosdwanServiceCommercialMap = serviceCommercials.stream()
					.filter(serviceCommercial -> (IzosdwanCommonConstants.CPE.equals(serviceCommercial.getReferenceName()) || "IZO SDWAN service charges".equalsIgnoreCase(serviceCommercial.getReferenceName())))
					.filter(serviceCommercial -> IzosdwanCommonConstants.COMPONENTS.equalsIgnoreCase(serviceCommercial.getReferenceType()))
					.collect(Collectors.groupingBy(OdrServiceCommercial::getComponentReferenceName, Collectors.toList()));

			cpeIzosdwanServiceCommercialMap.forEach((cpeId, odrServiceCommercials) -> {
				LOGGER.info("CPE and Izosdwan charges are filtered for CPE ids : {}, total odr service commercials: {}", cpeId, odrServiceCommercials.size());
			});

			return cpeIzosdwanServiceCommercialMap;
		}
		return Collections.emptyMap();
	}

	private void processServiceEntriesForSdwanOverlays(Integer locationId, List<OrderIzosdwanSite> orderIzosdwanSites,
			OdrOrder odrOrder, OdrContractInfo odrContractInfo, OrderProductSolution orderProductSolution,
			String username, OdrServiceDetail solutionOdrServiceDetail,Map<String,MstServiceClassificationDetail> serviceClassificationMap,Map<String, String> flowMapper) {
		try {
			Map<OdrServiceDetail, OdrComponent> cpeComponetsMap = new HashMap<>();
			String countryCode = CommonConstants.EMPTY;
			OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();

			primaryserviceDetail.setFlowStatus("NEW");
			primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
			String serviceRefId = Utils.generateUid();

			primaryserviceDetail.setServiceRefId(serviceRefId);
			primaryserviceDetail.setOdrContractInfo(odrContractInfo);
			primaryserviceDetail.setOdrOrder(odrOrder);
			primaryserviceDetail.setServiceCommissionedDate(
					orderIzosdwanSites.get(0).getRequestorDate() != null ? orderIzosdwanSites.get(0).getRequestorDate()
							: orderIzosdwanSites.get(0).getEffectiveDate());
			// primaryserviceDetail.setArc(orderIllSite.getArc());
			// primaryserviceDetail.setMrc(orderIllSite.getMrc());
			// primaryserviceDetail.setNrc(orderIllSite.getNrc());
			primaryserviceDetail
					.setErfPrdCatalogOfferingName(orderProductSolution.getMstProductOffering().getProductName());
			primaryserviceDetail.setCreatedBy(username);
			primaryserviceDetail.setCreatedDate(new Date());
			primaryserviceDetail.setUpdatedDate(new Date());
			primaryserviceDetail.setUpdatedBy(username);
			primaryserviceDetail.setIsActive(CommonConstants.Y);
			primaryserviceDetail.setIsIzo(CommonConstants.N);
			primaryserviceDetail.setOrderType(CommonConstants.NEW);
			primaryserviceDetail.setErfPrdCatalogProductName(orderProductSolution.getMstProductOffering().getMstProductFamily().getName());
			primaryserviceDetail.setErfPrdCatalogProductId(orderProductSolution.getMstProductOffering()
					.getMstProductFamily().getProductCatalogFamilyId());

			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(orderIzosdwanSites.get(0).getErfLocSitebLocationId()));
			if (StringUtils.isNotBlank(locationResponse)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				if (addressDetail != null) {
					String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getPincode());
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
							String.valueOf(orderIzosdwanSites.get(0).getErfLocSitebLocationId()));

				}
			}
			primaryserviceDetail = odrServiceDetailRepository.save(primaryserviceDetail);

			List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();

			flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
			flowMapper.put("productName", orderProductSolution.getMstProductOffering().getMstProductFamily().getName());
//			primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("vpnName",
//					CommonConstants.EMPTY, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
//			primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("directorInstanceMapping",
//					CommonConstants.EMPTY, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
			String cpeFailoverType = CommonConstants.EMPTY;
			List<String> cpeFailoverTypeList=new ArrayList<>();
			for (OrderIzosdwanSite orderIzosdwanSite : orderIzosdwanSites) {
				/*if (cpeFailoverType.equals(CommonConstants.EMPTY)) {
					cpeFailoverType = cpeFailoverType.concat(getAttributeValue(orderIzosdwanSite.getId(),
							IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.PORT_MODE));
				} else {
					cpeFailoverType = cpeFailoverType.concat("-").concat(getAttributeValue(orderIzosdwanSite.getId(),
							IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.PORT_MODE));
				}*/
				String vpnName = getAttributeValue(orderIzosdwanSite.getId(),IzosdwanCommonConstants.SITE_PROPERTIES,"Overlay VPN");
				String directorInstanceMapping = getAttributeValue(orderIzosdwanSite.getId(),IzosdwanCommonConstants.SITE_PROPERTIES,"Director Instance Mapping");
				
				primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("vpnName",
						vpnName, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
				primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("directorInstanceMapping",
						directorInstanceMapping, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
				cpeFailoverType = getAttributeValue(orderIzosdwanSite.getId(),IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.PORT_MODE);
				cpeFailoverTypeList.add(cpeFailoverType);
				LOGGER.info("failover types {} list for the location id {}", cpeFailoverTypeList, locationId);
			}
			cpeFailoverType = CommonConstants.EMPTY;
			if (!cpeFailoverTypeList.isEmpty()) {
				Collections.sort(cpeFailoverTypeList);
				LOGGER.info("failover types after sorting : {}", cpeFailoverTypeList);
				cpeFailoverType = StringUtils.join(cpeFailoverTypeList,"-");
				LOGGER.info("failover types after joining : {}", cpeFailoverType);
				
			}
			LOGGER.info("failover type {}", cpeFailoverType);
			primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("CPE_FAILOVER",
					cpeFailoverType, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
//			primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("templateName",
//					CommonConstants.EMPTY, "IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
			if (flowMapper.containsKey(LeAttributesConstants.PO_DATE)) {
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("PO DATE", flowMapper.get(LeAttributesConstants.PO_DATE), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));
			}
			if (flowMapper.containsKey(LeAttributesConstants.PO_NUMBER)) {
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("PO NUMBER", flowMapper.get(LeAttributesConstants.PO_NUMBER), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));
			}
			if (flowMapper.containsKey(IzosdwanCommonConstants.OWNER_REGION)) {
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute(IzosdwanCommonConstants.OWNER_REGION,
								flowMapper.get(IzosdwanCommonConstants.OWNER_REGION), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));
			}
			
			if (orderProductSolution.getProductProfileData() != null) {
				String offeringData = orderProductSolution.getProductProfileData();
				LOGGER.info("Offering data {}", offeringData);
				
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("offeringName",
								orderProductSolution.getMstProductOffering().getProductName(), "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.N, username));
				
				OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
				odrAdditionalServiceParam.setAttribute("offeringData");
				odrAdditionalServiceParam.setCategory("OFFERING_DATA");
				odrAdditionalServiceParam.setCreatedBy(username);
				odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
				odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
				odrAdditionalServiceParam.setValue(offeringData);
				odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);
				primaryserviceDetail.getOdrServiceAttributes()
						.add(constructOdrServiceAttribute("offeringData",
								odrAdditionalServiceParam.getId() + CommonConstants.EMPTY, "IZOSDWAN_COMMON",
								primaryserviceDetail, CommonConstants.Y, username));
				
				if (orderProductSolution.getMstProductOffering().getMstProductFamily().getName().startsWith("IZO")) {
					ProductOfferingsBean productOfferingsBean = Utils.convertJsonToObject(
							orderProductSolution.getProductProfileData(), ProductOfferingsBean.class);
					Boolean additionalSecurityAddon = false;
					if (productOfferingsBean != null && productOfferingsBean.getAddons() != null) {

						AddonsBean addon = productOfferingsBean.getAddons().stream()
								.filter(detail -> detail.getName().equalsIgnoreCase("Advanced Security")).findFirst()
								.orElse(null);
						if (addon != null) {
							additionalSecurityAddon = true;
							primaryserviceDetail.getOdrServiceAttributes()
									.add(constructOdrServiceAttribute("securityAddon", addon.getName(),
											"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
							if (addon.getDescription() != null && !addon.getDescription().isEmpty()) {
								OdrAdditionalServiceParam odrAdditionalServiceParam1 = new OdrAdditionalServiceParam();
								odrAdditionalServiceParam1.setAttribute("securityAddonDesc");
								odrAdditionalServiceParam1.setCategory("OFFERING_DATA");
								odrAdditionalServiceParam1.setCreatedBy(username);
								odrAdditionalServiceParam1.setCreatedTime(new Timestamp(new Date().getTime()));
								odrAdditionalServiceParam1.setIsActive(CommonConstants.Y);
								odrAdditionalServiceParam1.setValue(Utils.convertObjectToJson(addon.getDescription()));
								odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam1);
								primaryserviceDetail.getOdrServiceAttributes()
										.add(constructOdrServiceAttribute("securityAddonDesc",
												odrAdditionalServiceParam1.getId() + CommonConstants.EMPTY,
												"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.Y, username));
							}
						} else {
							addon = productOfferingsBean.getAddons().stream()
									.filter(detail -> detail.getName().equalsIgnoreCase("Basic Security")).findFirst()
									.orElse(null);
							if (addon != null) {
								additionalSecurityAddon = true;
								primaryserviceDetail.getOdrServiceAttributes()
										.add(constructOdrServiceAttribute("securityAddon", addon.getName(),
												"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
								if (addon.getDescription() != null && !addon.getDescription().isEmpty()) {
									OdrAdditionalServiceParam odrAdditionalServiceParam1 = new OdrAdditionalServiceParam();
									odrAdditionalServiceParam1.setAttribute("securityAddonDesc");
									odrAdditionalServiceParam1.setCategory("OFFERING_DATA");
									odrAdditionalServiceParam1.setCreatedBy(username);
									odrAdditionalServiceParam1.setCreatedTime(new Timestamp(new Date().getTime()));
									odrAdditionalServiceParam1.setIsActive(CommonConstants.Y);
									odrAdditionalServiceParam1
											.setValue(Utils.convertObjectToJson(addon.getDescription()));
									odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam1);
									primaryserviceDetail.getOdrServiceAttributes()
											.add(constructOdrServiceAttribute("securityAddonDesc",
													odrAdditionalServiceParam1.getId() + CommonConstants.EMPTY,
													"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.Y,
													username));
								}
							}
						}

					}
					primaryserviceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("isSecurityAddonApplicable",
									additionalSecurityAddon ? CommonConstants.Y : CommonConstants.N, "IZOSDWAN_COMMON",
									primaryserviceDetail, CommonConstants.N, username));
				}
			}
			List<Integer> llBwList = orderIzosdwanSites.stream().map(site->Integer.parseInt(site.getNewLastmileBandwidth())).collect(Collectors.toList());
			primaryserviceDetail.getOdrServiceAttributes()
					.add(constructOdrServiceAttribute("siteSize", Collections.max(llBwList).toString(),
							"IZOSDWAN_COMMON", primaryserviceDetail, CommonConstants.N, username));
			Integer count = 1;
			OdrComponent sharedOdrComponent = persistOdrComponent(primaryserviceDetail);
			Map<String,OdrComponent> primaryServiceIdComponentMap = new HashMap<>();
			for (OrderIzosdwanSite orderIzosdwanSite : orderIzosdwanSites) {
				OdrComponent primaryOdrComponent = new OdrComponent();
				if (orderIzosdwanSite.getIsShared() != null
						&& orderIzosdwanSite.getIsShared().equals(CommonConstants.Y)) {
					LOGGER.info("Got shared Underlay count --> {} and shared component --> {}",count,sharedOdrComponent.getId());
					primaryOdrComponent = sharedOdrComponent;
					primaryOdrComponent.setSiteType("A");
				} else {
					primaryOdrComponent = persistOdrComponent(primaryserviceDetail);
					if (count == 1) {
						primaryOdrComponent.setSiteType("A");
					} else if(count == 2){
						primaryOdrComponent.setSiteType("B");
					}else if(count == 3){
						primaryOdrComponent.setSiteType("C");
					}else if(count == 4){
						primaryOdrComponent.setSiteType("D");
					}else if(count == 5){
						primaryOdrComponent.setSiteType("E");
					}
				}
				primaryOdrComponent.setOdrServiceDetail(primaryserviceDetail);
				

				// primaryserviceDetail.setOdrComponents(null);
				Set<OdrComponentAttribute> odrComponentAttributes = primaryOdrComponent.getOdrComponentAttributes();
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						countryCode = addressDetail.getCountryCode();
						LOGGER.info("Country code--> {} for Country -->{}", addressDetail.getCountryCode(),
								addressDetail.getCountry());
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCountryCode", addressDetail.getCountryCode()));
					}
				}
				if (flowMapper.containsKey(LeAttributesConstants.PO_DATE)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							LeAttributesConstants.PO_DATE, flowMapper.get(LeAttributesConstants.PO_DATE)));
				}
				if (flowMapper.containsKey(LeAttributesConstants.PO_NUMBER)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							LeAttributesConstants.PO_NUMBER, flowMapper.get(LeAttributesConstants.PO_NUMBER)));
				}
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"siteCode", serviceRefId));
				primaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));
				
				if (orderIzosdwanSite.getIsTaxExempted() != null
						&& orderIzosdwanSite.getIsTaxExempted().equals(CommonConstants.BACTIVE)) {
					LOGGER.info("Tax exemption underlay present");
					primaryserviceDetail.setTaxExemptionFlag(CommonConstants.Y);
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"taxExemption", CommonConstants.Y));
				} else {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"taxExemption", CommonConstants.N));
				}
				processCommonComponentAttr(String.valueOf(orderIzosdwanSite.getId()), primaryserviceDetail,
						username, flowMapper, primaryOdrComponent, true, orderIzosdwanSite);
				LOGGER.info("Pri sec for site id {} is {}", orderIzosdwanSite.getId(),
						orderIzosdwanSite.getPriSec());
				Order order = orderIzosdwanSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrder();
				Optional<OrderIzosdwanAttributeValue> optOrderAttribute = order.getOrderIzosdwanAttributeValue().stream().filter(attribute -> IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE.equalsIgnoreCase(attribute.getDisplayValue())).findFirst();
				boolean isNsQuote = optOrderAttribute.isPresent() ? CommonConstants.Y.equals(optOrderAttribute.get().getAttributeValue()) : false;
				processProductComponentAttr(String.valueOf(orderIzosdwanSite.getId()), primaryserviceDetail,
						(orderIzosdwanSite.getPriSec() != null ? (orderIzosdwanSite.getPriSec().toLowerCase())
								: "primary"),
						username, false, odrServiceCommercials, primaryOdrComponent, true,IzosdwanCommonConstants.IZOSDWAN_NAME, isNsQuote);
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_ADDRESS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_COUNTRY)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.SUPPLIER_COUNTRY,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_COUNTRY)));
				}
				if (flowMapper.containsKey(IzosdwanCommonConstants.IS_BILLING_INTL)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
							primaryOdrComponent, IzosdwanCommonConstants.IS_BILLING_INTL,
							flowMapper.get(IzosdwanCommonConstants.IS_BILLING_INTL)));
				}
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getPincode());
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
								"destinationCountryCode", addressDetail.getCountryCode()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddressId",
								primaryserviceDetail.getErfLocSiteAddressId()));
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
								LOGGER.info("Calling product queue for Intl Dial code {}", addressDetail.getCountry());
								String response = (String) mqUtils.sendAndReceive(productLocationQueue,
										addressDetail.getCountry());
								LOGGER.info("Response from product location queue {}", response);
								if (StringUtils.isNotBlank(response)) {
									ProductLocationBean productLocationBean = Utils.convertJsonToObject(response,
											ProductLocationBean.class);
									if (productLocationBean != null
											&& StringUtils.isNotBlank(productLocationBean.getIntlDialCode())) {
										LOGGER.info("Intl dial code for country --> {} is -->{}",
												addressDetail.getCountry(), productLocationBean.getIntlDialCode());
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
							}
						}catch(Exception e) {
							LOGGER.error("Error in product queue call for loaction",e);
						}
					}
					
				}
				
				
				if (orderIzosdwanSite.getManagementType() == null
						|| orderIzosdwanSite.getManagementType().toLowerCase().contains("unmanaged")) {
					primaryOdrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(
							primaryserviceDetail, primaryOdrComponent, "isCPEManagedAlready", "false"));
				} else {
					primaryOdrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(
							primaryserviceDetail, primaryOdrComponent, "isCPEManagedAlready", "true"));
				}
				primaryOdrComponent.getOdrComponentAttributes().addAll(odrComponentAttributes);
				primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
				primaryOdrComponent = odrComponentRepository.save(primaryOdrComponent);
				primaryserviceDetail.setPrimarySecondary(IzosdwanCommonConstants.PRIMARY);
				primaryserviceDetail = odrServiceDetailRepository.save(primaryserviceDetail);
				odrServiceCommercialRepository.saveAll(odrServiceCommercials);
				if(orderIzosdwanSite.getIsShared()==null || orderIzosdwanSite.getIsShared().equals(CommonConstants.N)) {
				Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository
						.findByServiceRefId(orderIzosdwanSite.getSiteCode());
				if (odrServiceDetails != null && !odrServiceDetails.isEmpty()) {
					cpeComponetsMap.put(odrServiceDetails.stream().findFirst().get(), primaryOdrComponent);

				}
				}else {
					List<OrderIzosdwanSite> orderIzosdwanSitesShared = orderIzosdwanSites.stream().filter(site->StringUtils.isNotBlank(site.getPrimaryServiceId()) && site.getPrimaryServiceId().equals(orderIzosdwanSite.getPrimaryServiceId())).collect(Collectors.toList());
					if(orderIzosdwanSitesShared!=null && !orderIzosdwanSitesShared.isEmpty()) {
						for(OrderIzosdwanSite siteDetail : orderIzosdwanSitesShared) {
							Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository
									.findByServiceRefId(siteDetail.getSiteCode());
							if (odrServiceDetails != null && !odrServiceDetails.isEmpty()) {
								cpeComponetsMap.put(odrServiceDetails.stream().findFirst().get(), primaryOdrComponent);
								
							}
						}
					}
					primaryServiceIdComponentMap.put(orderIzosdwanSite.getPrimaryServiceId(), primaryOdrComponent);
				}

				count++;
				
			}

			LOGGER.info("Total Number of primary Service attributes {}",
					primaryserviceDetail.getOdrServiceAttributes().size());

			for (OdrComponent odrComp : primaryserviceDetail.getOdrComponents()) {
				odrComp.setOdrServiceDetail(primaryserviceDetail);
				for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
					LOGGER.info("setting primary reference");
					odrComponentAttribute.setOdrServiceDetail(primaryserviceDetail);
				}
			}

			LOGGER.info("Overlay Service id {}", primaryserviceDetail.getId());
			constructOdrSolutionComponents(solutionOdrServiceDetail, primaryserviceDetail, solutionOdrServiceDetail,
					null, odrOrder, IzosdwanCommonConstants.OVERLAY);
			
			for (OrderIzosdwanSite orderIzosdwanSite : orderIzosdwanSites) {
				Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository
						.findByServiceRefId(orderIzosdwanSite.getSiteCode());
				if (odrServiceDetails != null && !odrServiceDetails.isEmpty()) {
					if (cpeComponetsMap.containsKey(odrServiceDetails.stream().findFirst().get())) {
						constructOdrSolutionComponents(solutionOdrServiceDetail,
								odrServiceDetails.stream().findAny().get(), primaryserviceDetail,
								cpeComponetsMap.get(odrServiceDetails.stream().findFirst().get()), odrOrder,
								IzosdwanCommonConstants.UNDERLAY);
					}
				}

			}
			odrServiceCommercialRepository.saveAll(odrServiceCommercials);
			if (orderIzosdwanSites != null && !orderIzosdwanSites.isEmpty()) {
				Order order = orderIzosdwanSites.get(0).getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrder();
				Optional<OrderIzosdwanAttributeValue> optOrderAttribute = order.getOrderIzosdwanAttributeValue().stream().filter(attribute -> IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE.equalsIgnoreCase(attribute.getDisplayValue())).findFirst();
				String nsQuote = optOrderAttribute.map(OrderIzosdwanAttributeValue::getAttributeValue).orElse(null);
				LOGGER.info("Non Standard Order: {} IZOSDWAN site type {} for service id {} map is {}",
						nsQuote, orderIzosdwanSites.get(0).getIzosdwanSiteType(), primaryserviceDetail.getUuid(),
						serviceClassificationMap);
				// Custom journey service attributes for overlay by custom custom site type.
				if (CommonConstants.Y.equals(nsQuote)) {
					Optional<OrderToLe> orderToLe = order.getOrderToLes().stream().findFirst();
					Integer orderToLeId = orderToLe.map(OrderToLe::getId).get();
					processServiceAttributesBySiteTypeForCustomJourney(primaryserviceDetail,
							IzosdwanCommonConstants.CUSTOM, serviceClassificationMap, username, orderIzosdwanSites.size(), locationId, orderToLeId);
				} else {
					processServiceAttributesBySiteType(primaryserviceDetail,
							orderIzosdwanSites.get(0).getIzosdwanSiteType(), serviceClassificationMap, username);
				}

			}

		} catch (Exception e) {
			LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
		}

	}
	
	private OdrSolutionComponent constructOdrSolutionComponents(OdrServiceDetail solutionOdrServiceDetail,
			OdrServiceDetail odrServiceDetail, OdrServiceDetail parenOdrServiceDetail, OdrComponent cpeComponent,
			OdrOrder odrOrder, String componentGroup) {
		OdrSolutionComponent odrSolutionComponent = new OdrSolutionComponent();
		odrSolutionComponent.setComponentGroup(componentGroup);
		if (cpeComponent != null && cpeComponent.getId() != null) {
			odrSolutionComponent.setCpeComponentId(cpeComponent.getId());
		}
		odrSolutionComponent.setIsActive(CommonConstants.Y);
		odrSolutionComponent.setOdrOrder(odrOrder);
		if (!Objects.isNull(odrServiceDetail)) {
			odrSolutionComponent.setOdrServiceDetail1(odrServiceDetail);
			odrSolutionComponent.setServiceCode(odrServiceDetail.getUuid());
		}
		if (!Objects.isNull(parenOdrServiceDetail)) {
			odrSolutionComponent.setOdrServiceDetail2(parenOdrServiceDetail);
			odrSolutionComponent.setParentServiceCode(parenOdrServiceDetail.getServiceRefId());
		}
		if (!Objects.isNull(solutionOdrServiceDetail)) {
			odrSolutionComponent.setOdrServiceDetail3(solutionOdrServiceDetail);
			odrSolutionComponent.setSolutionCode(solutionOdrServiceDetail.getServiceRefId());
		}
		odrSolutionComponent.setOdrOrder(odrOrder);
		odrSolutionComponent.setOrderCode(odrOrder.getUuid());
		odrSolutionComponent = odrSolutionComponentRepository.saveAndFlush(odrSolutionComponent);
		return odrSolutionComponent;
	}

	private void processServiceAttributesBySiteTypeForCustomJourney(OdrServiceDetail odrServiceDetail,String serviceClasiification,Map<String, MstServiceClassificationDetail> serviceClassificationMap,String username, Integer noOfUnderlays, Integer locationId, Integer orderToLeId) {
		LOGGER.info("Inside processServiceAttributesBySiteType for {}",odrServiceDetail.getUuid());
		List<OdrServiceAttribute> odrServiceAttributes = new ArrayList<>();
		if(serviceClassificationMap.containsKey(serviceClasiification)) {
			LOGGER.info("Got service classification details for {}",serviceClasiification);
			MstServiceClassificationDetail mstServiceClassificationDetail = serviceClassificationMap.get(serviceClasiification);
			if (mstServiceClassificationDetail != null) {

				// Calculate and add no. of wan links and no. of CPEs attributes.
				odrServiceAttributes.add(getNoOfCpeServiceAttribute(odrServiceDetail, username, locationId, orderToLeId));
				odrServiceAttributes.add(getNoOfWanLinkServiceAttribute(odrServiceDetail, username, noOfUnderlays));

				if (mstServiceClassificationDetail.getServiceClassification() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getServiceClassification());
					odrServiceAttribute.setAttributeName("serviceClassification");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getServiceClassification());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getFirstWanLinkType() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getFirstWanLinkType());
					odrServiceAttribute.setAttributeName("firstWanLinkType");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getFirstWanLinkType());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getInternetBreakout() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getInternetBreakout());
					odrServiceAttribute.setAttributeName("internetBreakOut");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getInternetBreakout());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getSecondWanLinkType() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getSecondWanLinkType());
					odrServiceAttribute.setAttributeName("secondWanLinkType");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getSecondWanLinkType());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getSiteType() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getSiteType());
					odrServiceAttribute.setAttributeName("serviceClassificationType");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getSiteType());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
			}

		}
		if(odrServiceAttributes!=null && !odrServiceAttributes.isEmpty()) {
			odrServiceAttributeRepository.saveAll(odrServiceAttributes);
		}
	}

	private OdrServiceAttribute getNoOfWanLinkServiceAttribute(OdrServiceDetail odrServiceDetail, String username, Integer noOfUnderlays) {
		OdrServiceAttribute odrServiceAttributeNoOfWanLinks = new OdrServiceAttribute();
		odrServiceAttributeNoOfWanLinks.setAttributeAltValueLabel(noOfUnderlays.toString());
		odrServiceAttributeNoOfWanLinks.setAttributeName("noOfWanLinks");
		odrServiceAttributeNoOfWanLinks.setAttributeValue(noOfUnderlays.toString());
		odrServiceAttributeNoOfWanLinks.setCategory("IZOSDWAN_COMMON");
		odrServiceAttributeNoOfWanLinks.setCreatedBy(username);
		odrServiceAttributeNoOfWanLinks.setCreatedDate(new Date());
		odrServiceAttributeNoOfWanLinks.setIsActive(CommonConstants.Y);
		odrServiceAttributeNoOfWanLinks.setIsAdditionalParam(CommonConstants.N);
		odrServiceAttributeNoOfWanLinks.setOdrServiceDetail(odrServiceDetail);
		odrServiceAttributeNoOfWanLinks.setUpdatedBy(username);
		odrServiceAttributeNoOfWanLinks.setUpdatedDate(new Date());
		return odrServiceAttributeNoOfWanLinks;
	}

	private OdrServiceAttribute getNoOfCpeServiceAttribute(OdrServiceDetail odrServiceDetail, String username, Integer locationId, Integer orderToLeId) {
		List<OrderIzosdwanCpeConfigDetails> orderCpeConfigDetails = orderIzosdwanCpeConfigDetailsRepository.findAllByOrderLeIdAndLocationId(orderToLeId, locationId);
		long noOfCpes = orderCpeConfigDetails.stream().filter(cpeDetails -> IzosdwanCommonConstants.CPE_BASIC_CHASSIS.equals(cpeDetails.getAttributeName())).count();
		OdrServiceAttribute odrServiceAttributeNoOfCpe = new OdrServiceAttribute();
		odrServiceAttributeNoOfCpe.setAttributeAltValueLabel(String.valueOf(noOfCpes));
		odrServiceAttributeNoOfCpe.setAttributeName("noOfCpe");
		odrServiceAttributeNoOfCpe.setAttributeValue(String.valueOf(noOfCpes));
		odrServiceAttributeNoOfCpe.setCategory("IZOSDWAN_COMMON");
		odrServiceAttributeNoOfCpe.setCreatedBy(username);
		odrServiceAttributeNoOfCpe.setCreatedDate(new Date());
		odrServiceAttributeNoOfCpe.setIsActive(CommonConstants.Y);
		odrServiceAttributeNoOfCpe.setIsAdditionalParam(CommonConstants.N);
		odrServiceAttributeNoOfCpe.setOdrServiceDetail(odrServiceDetail);
		odrServiceAttributeNoOfCpe.setUpdatedBy(username);
		odrServiceAttributeNoOfCpe.setUpdatedDate(new Date());
		return odrServiceAttributeNoOfCpe;
	}

	private void processServiceAttributesBySiteType(OdrServiceDetail odrServiceDetail,String serviceClasiification,Map<String, MstServiceClassificationDetail> serviceClassificationMap,String username) {
		LOGGER.info("Inside processServiceAttributesBySiteType for {}",odrServiceDetail.getUuid());
		List<OdrServiceAttribute> odrServiceAttributes = new ArrayList<>();
		if(serviceClassificationMap.containsKey(serviceClasiification)) {
			LOGGER.info("Got service classification details for {}",serviceClasiification);
			MstServiceClassificationDetail mstServiceClassificationDetail = serviceClassificationMap.get(serviceClasiification);
			if (mstServiceClassificationDetail != null) {
				if (mstServiceClassificationDetail.getServiceClassification() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getServiceClassification());
					odrServiceAttribute.setAttributeName("serviceClassification");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getServiceClassification());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getFirstWanLinkType() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getFirstWanLinkType());
					odrServiceAttribute.setAttributeName("firstWanLinkType");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getFirstWanLinkType());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getInternetBreakout() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getInternetBreakout());
					odrServiceAttribute.setAttributeName("internetBreakOut");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getInternetBreakout());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getNoOfCpe() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getNoOfCpe());
					odrServiceAttribute.setAttributeName("noOfCpe");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getNoOfCpe());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
					String byonCpeType = CommonConstants.EMPTY;
					if (mstServiceClassificationDetail.getNoOfCpe().equalsIgnoreCase("One") || mstServiceClassificationDetail.getNoOfCpe().equalsIgnoreCase("1")) {
						byonCpeType= "Single CPE";
					} else if (mstServiceClassificationDetail.getNoOfCpe().equalsIgnoreCase("Two") || mstServiceClassificationDetail.getNoOfCpe().equalsIgnoreCase("2")) {
						byonCpeType = "Dual CPE";
					}
					OdrServiceAttribute odrServiceAttribute1 = new OdrServiceAttribute();
					odrServiceAttribute1.setAttributeAltValueLabel(byonCpeType);
					odrServiceAttribute1.setAttributeName("byonCpeType");
					odrServiceAttribute1.setAttributeValue(byonCpeType);
					odrServiceAttribute1.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute1.setCreatedBy(username);
					odrServiceAttribute1.setCreatedDate(new Date());
					odrServiceAttribute1.setIsActive(CommonConstants.Y);
					odrServiceAttribute1.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute1.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute1.setUpdatedBy(username);
					odrServiceAttribute1.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute1);
				}
				if (mstServiceClassificationDetail.getNoOfWanLinks() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getNoOfWanLinks());
					odrServiceAttribute.setAttributeName("noOfWanLinks");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getNoOfWanLinks());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getSecondWanLinkType() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getSecondWanLinkType());
					odrServiceAttribute.setAttributeName("secondWanLinkType");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getSecondWanLinkType());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
				if (mstServiceClassificationDetail.getSiteType() != null) {
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(mstServiceClassificationDetail.getSiteType());
					odrServiceAttribute.setAttributeName("serviceClassificationType");
					odrServiceAttribute.setAttributeValue(mstServiceClassificationDetail.getSiteType());
					odrServiceAttribute.setCategory("IZOSDWAN_COMMON");
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.N);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceAttributes.add(odrServiceAttribute);
				}
			}
			
		}
		if(odrServiceAttributes!=null && !odrServiceAttributes.isEmpty()) {
			odrServiceAttributeRepository.saveAll(odrServiceAttributes);
		}
		
	}
	
	private Map<String, MstServiceClassificationDetail> getAllSiteClassificationDetails() {
		LOGGER.info("Inside getAllSiteClassificationDetails!!");
		Map<String, MstServiceClassificationDetail> map = new HashMap<>();
		List<MstServiceClassificationDetail> mstServiceClassificationDetails = mstServiceClassificationDetailRepository
				.findAll();
		if (mstServiceClassificationDetails != null && !mstServiceClassificationDetails.isEmpty()) {
			mstServiceClassificationDetails.stream().forEach(detail -> {
				map.put(detail.getServiceClassification(), detail);
			});
		}
		try {
		LOGGER.info("Map details getAllSiteClassificationDetails {}",map);
		}catch(Exception e) {
			
		}
		return map;
	}
	
	private OdrServiceAttribute constructOdrServiceAttribute(String attrName, String attrValue, String category,
			OdrServiceDetail odrServiceDetail, String isAdditionalParam, String username) {
		OdrServiceAttribute odrServiceAttribute1 = odrServiceAttributeRepository
				.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, odrServiceDetail.getId(), category);
		if (odrServiceAttribute1 == null) {
			odrServiceAttribute1 = new OdrServiceAttribute();
			odrServiceAttribute1.setCreatedBy(username);
			odrServiceAttribute1.setCreatedDate(new Date());
		}

		odrServiceAttribute1.setAttributeAltValueLabel(attrValue);
		odrServiceAttribute1.setAttributeName(attrName);
		odrServiceAttribute1.setAttributeValue(attrValue);
		odrServiceAttribute1.setCategory(category);
		odrServiceAttribute1.setIsActive(CommonConstants.Y);
		odrServiceAttribute1.setIsAdditionalParam(isAdditionalParam);
		odrServiceAttribute1.setOdrServiceDetail(odrServiceDetail);
		odrServiceAttribute1.setUpdatedBy(username);
		odrServiceAttribute1.setUpdatedDate(new Date());
		return odrServiceAttribute1;
	}
	
	public void persistCgwDetails(OdrOrder odrOrder,Order order,OdrContractInfo odrContractInfo,String username,Map<String, String> flowMapper) {
		LOGGER.info("Persisting CGW details!!");
		try {
			List<OrderIzosdwanCgwDetail> orderIzosdwanCgwDetails = orderIzosdwanCgwDetailRepository.findByOrderId(order.getId());
			for(OrderIzosdwanCgwDetail cgw : orderIzosdwanCgwDetails) {
				OdrServiceDetail primaryServiceId = persistCGWServices(odrOrder, order, odrContractInfo, username, cgw.getPrimaryLocation(), true,cgw,flowMapper);
				OdrServiceDetail secondaryServiceId = persistCGWServices(odrOrder, order, odrContractInfo, username, cgw.getSecondaryLocation(), false,cgw,flowMapper);
				primaryServiceId.setErfPriSecServiceLinkId(secondaryServiceId.getId());
				secondaryServiceId.setErfPriSecServiceLinkId(primaryServiceId.getId());
				odrServiceDetailRepository.saveAndFlush(primaryServiceId);
				odrServiceDetailRepository.saveAndFlush(secondaryServiceId);
			}
			
			

		} catch (Exception e) {
			LOGGER.warn("Error CGW : {}", ExceptionUtils.getStackTrace(e));
		}

	
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	private OdrServiceDetail persistCGWServices(OdrOrder odrOrder, Order order, OdrContractInfo odrContractInfo, String username,
			String cgwLocation, Boolean isPrimary, OrderIzosdwanCgwDetail orderIzosdwanCgwDetail,Map<String, String> flowMapper)
			throws TclCommonException, IllegalArgumentException, ParseException {
		OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();

		primaryserviceDetail.setFlowStatus("NEW");
		primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
		String serviceRefId = Utils.generateUid();

		primaryserviceDetail.setServiceRefId(serviceRefId);
		primaryserviceDetail.setOdrContractInfo(odrContractInfo);
		primaryserviceDetail.setOdrOrder(odrOrder);
		primaryserviceDetail.setErfPrdCatalogOfferingName(IzosdwanCommonConstants.CGW);
		primaryserviceDetail.setCreatedBy(username);
		primaryserviceDetail.setCreatedDate(new Date());
		primaryserviceDetail.setUpdatedDate(new Date());
		primaryserviceDetail.setUpdatedBy(username);
		primaryserviceDetail.setIsActive(CommonConstants.Y);
		primaryserviceDetail.setIsIzo(CommonConstants.N);
		primaryserviceDetail.setOrderType(CommonConstants.NEW);
		primaryserviceDetail.setBwUnit(IzosdwanCommonConstants.MBPS);
		primaryserviceDetail.setErfPrdCatalogProductName(IzosdwanCommonConstants.IZOSDWAN_CGW);
		primaryserviceDetail.setPrimarySecondary(isPrimary ? "Primary" : "Secondary");
		primaryserviceDetail.setOdrComponents(new HashSet<>());
		//primaryserviceDetail = odrServiceDetailRepository.save(primaryserviceDetail);
		OdrComponent primaryOdrComponent = persistOdrComponent(primaryserviceDetail);
		primaryOdrComponent.setSiteType("A");
		Set<OdrComponentAttribute> odrComponentAttributes = primaryOdrComponent.getOdrComponentAttributes();
		odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "bwUnit", "Mbps"));
		if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS)) {
			primaryserviceDetail.getOdrServiceAttributes()
					.add(constructOdrServiceAttribute(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS_ID_OMS), "IZOSDWAN_COMMON",
							primaryserviceDetail, CommonConstants.N, username));
		}
		if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_ADDRESS)) {
			primaryserviceDetail.getOdrServiceAttributes()
					.add(constructOdrServiceAttribute(IzosdwanCommonConstants.SUPPLIER_ADDRESS,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_ADDRESS), "IZOSDWAN_COMMON",
							primaryserviceDetail, CommonConstants.N, username));
		}
		if (flowMapper.containsKey(IzosdwanCommonConstants.SUPPLIER_COUNTRY)) {
			primaryserviceDetail.getOdrServiceAttributes()
					.add(constructOdrServiceAttribute(IzosdwanCommonConstants.SUPPLIER_COUNTRY,
							flowMapper.get(IzosdwanCommonConstants.SUPPLIER_COUNTRY), "IZOSDWAN_COMMON",
							primaryserviceDetail, CommonConstants.N, username));
		}
		if (flowMapper.containsKey(IzosdwanCommonConstants.IS_BILLING_INTL)) {
			primaryserviceDetail.getOdrServiceAttributes()
					.add(constructOdrServiceAttribute(IzosdwanCommonConstants.IS_BILLING_INTL,
							flowMapper.get(IzosdwanCommonConstants.IS_BILLING_INTL), "IZOSDWAN_COMMON",
							primaryserviceDetail, CommonConstants.N, username));
		}
		//primaryOdrComponent.setOdrComponentAttributes(odrComponentAttributes);
		String productResponse = (String) mqUtils.sendAndReceive(cgwSamByCityQueue, cgwLocation);
		if (StringUtils.isNotEmpty(productResponse)) {
			CgwServiceAreaMatricBean cgwServiceAreaMatricBean = Utils.convertJsonToObject(productResponse,
					CgwServiceAreaMatricBean.class);

			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(cgwServiceAreaMatricBean.getLocationId()));
			if (StringUtils.isNotBlank(locationResponse)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				if (addressDetail != null) {
					String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
							+ StringUtils.trimToEmpty(addressDetail.getPincode());
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
					primaryserviceDetail
							.setErfLocSiteAddressId(String.valueOf(cgwServiceAreaMatricBean.getLocationId()));

				}

			}

			LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String demarReponse = (String) mqUtils.sendAndReceive(demarcationQueue,
					String.valueOf(cgwServiceAreaMatricBean.getLocationId()));
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
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"demarcationBuildingName",
							demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
									: null));
					odrComponentAttributes.add(
							persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationFloor",
									demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null));
					odrComponentAttributes.add(
							persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationWing",
									demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null));
					odrComponentAttributes.add(
							persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationRoom",
									demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null));
				}
			}

			if (cgwServiceAreaMatricBean.getLocationId() != null) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String popResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(cgwServiceAreaMatricBean.getLocationId()));
				if (popResponse != null) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(popResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getPincode());
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
						primaryserviceDetail
								.setErfLocSiteAddressId(String.valueOf(cgwServiceAreaMatricBean.getLocationId()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddressId", cgwServiceAreaMatricBean.getLocationId() + ""));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddress", addr));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "latLong", addressDetail.getLatLong()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCountry", addressDetail.getCountry()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCity", addressDetail.getCity()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationAddressLineOne", addressDetail.getAddressLineOne()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationAddressLineTwo", addressDetail.getAddressLineTwo()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationLocality", addressDetail.getLocality()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationState", addressDetail.getState()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationPincode", addressDetail.getPincode()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCountryCode", addressDetail.getCountryCode()));
						try {
							if (StringUtils.isNotBlank(addressDetail.getCountry())) {
								LOGGER.info("Calling product queue for Intl Dial code {}", addressDetail.getCountry());
								String response = (String) mqUtils.sendAndReceive(productLocationQueue,
										addressDetail.getCountry());
								LOGGER.info("Response from product location queue {}", response);
								if (StringUtils.isNotBlank(response)) {
									ProductLocationBean productLocationBean = Utils.convertJsonToObject(response,
											ProductLocationBean.class);
									if (productLocationBean != null
											&& StringUtils.isNotBlank(productLocationBean.getIntlDialCode())) {
										LOGGER.info("Intl dial code for country --> {} is -->{}",
												addressDetail.getCountry(), productLocationBean.getIntlDialCode());
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
							}
						}catch(Exception e) {
							LOGGER.error("Error in product queue call for loaction",e);
						}
					}
				}
			}
			Random rng = new Random();
			Integer s =  rng.nextInt(1000000);
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"feasibilityId", Integer.toString(Math.abs(s))));
			String migrationBw = null;
			if (StringUtils.isNotBlank(orderIzosdwanCgwDetail.getMigrationUserBw())) {
				migrationBw = orderIzosdwanCgwDetail.getMigrationUserBw();
			} else if (StringUtils.isNotBlank(orderIzosdwanCgwDetail.getMigrationSystemBw())) {
				migrationBw = orderIzosdwanCgwDetail.getMigrationSystemBw();
			}
			Integer aggerBw = 0;
			if (migrationBw != null) {
				aggerBw = aggerBw + Integer.parseInt(migrationBw);
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"migrationBw", orderIzosdwanCgwDetail.getMigrationSystemBw()));
			}

			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"heteroBw", orderIzosdwanCgwDetail.getHetroBw()));

			if (StringUtils.isNotEmpty(orderIzosdwanCgwDetail.getHetroBw())) {
				aggerBw = aggerBw + Integer.parseInt(orderIzosdwanCgwDetail.getHetroBw());
			}
			String useCase = CommonConstants.EMPTY;
			/*
			 * if (StringUtils.isNotEmpty(orderIzosdwanCgwDetail.getUseCase1())) { useCase =
			 * useCase.concat("Use Case 1:").concat(orderIzosdwanCgwDetail.getUseCase1()).
			 * concat(";"); odrComponentAttributes.add(persistOdrComponentAttributes(
			 * primaryserviceDetail, primaryOdrComponent, "useCase1",
			 * orderIzosdwanCgwDetail.getUseCase1())); }
			 */
			if (StringUtils.isNotEmpty(orderIzosdwanCgwDetail.getUseCase1a())) {
				useCase = useCase.concat("Use Case 1a:").concat(orderIzosdwanCgwDetail.getUseCase1()).concat(";");
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"useCase1a", orderIzosdwanCgwDetail.getUseCase1()));
			}
			
			if (StringUtils.isNotEmpty(orderIzosdwanCgwDetail.getUseCase1b())) {
				useCase = useCase.concat("Use Case 1b:").concat(orderIzosdwanCgwDetail.getUseCase1()).concat(";");
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"useCase1b", orderIzosdwanCgwDetail.getUseCase1()));
			}
			if (StringUtils.isNotEmpty(orderIzosdwanCgwDetail.getUseCase2())) {
				useCase = useCase.concat("Use Case 2:").concat(orderIzosdwanCgwDetail.getUseCase2()).concat(";");
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"useCase2", orderIzosdwanCgwDetail.getUseCase2()));
			}
			if (StringUtils.isNotEmpty(orderIzosdwanCgwDetail.getUseCase3())) {
				useCase = useCase.concat("Use Case 3:").concat(orderIzosdwanCgwDetail.getUseCase3()).concat(";");
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"useCase3", orderIzosdwanCgwDetail.getUseCase3()));
			}
			if (StringUtils.isNotEmpty(orderIzosdwanCgwDetail.getUseCase4())) {
				useCase = useCase.concat("Use Case 4:").concat(orderIzosdwanCgwDetail.getUseCase4());
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"useCase4", orderIzosdwanCgwDetail.getUseCase4()));
			}
			if(!useCase.isEmpty() && useCase.charAt(useCase.length() - 1) == ';'){
				useCase= useCase.substring(0, useCase.length() - 1);
			}
			odrComponentAttributes
					.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "useCase", useCase));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "nniId",
					cgwServiceAreaMatricBean.getPrimaryIor()));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"aggregationBandwidth", aggerBw.toString()));
			odrComponentAttributes.add(
					persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "routingProtocol", "BGP"));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"sourceCity", cgwServiceAreaMatricBean.getPopAddress()));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"popSiteCode", cgwServiceAreaMatricBean.getPopAddress()));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "asno",
					cgwServiceAreaMatricBean.getAsno()));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"hostName", cgwServiceAreaMatricBean.getHostName()));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"serialNumber", cgwServiceAreaMatricBean.getSerialNumber()));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"primaryIor", cgwServiceAreaMatricBean.getPrimaryIor()));
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"secondaryIor", cgwServiceAreaMatricBean.getSecondaryIor()));
		}
		List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();
		for(OdrComponentAttribute compAttr : odrComponentAttributes) {
			compAttr.setOdrServiceDetail(primaryserviceDetail);
		}
		primaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));
		primaryOdrComponent.setOdrServiceDetail(primaryserviceDetail);
		primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
		primaryserviceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("COS Model", "6 COS",
				"CGW Common", primaryserviceDetail, CommonConstants.N, username));
		primaryserviceDetail = odrServiceDetailRepository.saveAndFlush(primaryserviceDetail);
		LOGGER.info("Service detail saved {} and components {}",primaryserviceDetail,primaryserviceDetail.getOdrComponents());
		if (isPrimary) {
			Optional<OrderIzosdwanAttributeValue> optOrderAttribute = order.getOrderIzosdwanAttributeValue().stream().filter(attribute -> IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE.equalsIgnoreCase(attribute.getDisplayValue())).findFirst();
			boolean isNsQuote = optOrderAttribute.isPresent() ? CommonConstants.Y.equals(optOrderAttribute.get().getAttributeValue()) : false;
			processProductComponentAttr(Integer.toString(orderIzosdwanCgwDetail.getId()), primaryserviceDetail,
					isPrimary ? "Primary" : "Secondary", username, false, odrServiceCommercials, primaryOdrComponent,
					false, IzosdwanCommonConstants.CGW, isNsQuote);

		}
		persistCgwAttribute(isPrimary, primaryserviceDetail, username, primaryOdrComponent,
				orderIzosdwanCgwDetail.getId());
		odrServiceCommercialRepository.saveAll(odrServiceCommercials);
		odrServiceCommercialRepository.flush();
		List<OdrSolutionComponent> odrSolutionComponents = odrSolutionComponentRepository.findByOdrOrder(odrOrder);
		if(odrSolutionComponents!=null && !odrSolutionComponents.isEmpty()) {
			OdrServiceDetail odrServiceDetail = odrSolutionComponents.get(0).getOdrServiceDetail3();
			constructOdrSolutionComponents(odrServiceDetail, primaryserviceDetail, odrServiceDetail, null, odrOrder, "CGW");
		}
		return primaryserviceDetail;
	}
	
	private void persistCgwAttribute(Boolean isPrimary, OdrServiceDetail odrServiceDetail, String username,
			OdrComponent odrComponent, Integer refId) throws TclCommonException {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndReferenceName(refId, IzosdwanCommonConstants.IZOSDWAN_CGW);
		OrderProductComponent orderProductComponent = null;
		if (isPrimary) {
			orderProductComponent = orderProductComponents.stream()
					.filter(opc -> opc.getMstProductComponent().getName().equals(IzosdwanCommonConstants.PRIMARY_CGW))
					.findFirst().orElse(null);
		} else {
			orderProductComponent = orderProductComponents.stream()
					.filter(opc -> opc.getMstProductComponent().getName().equals(IzosdwanCommonConstants.SECONDARY_CGW))
					.findFirst().orElse(null);
		}
		if (orderProductComponent != null) {
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = OrderProductComponentsAttributeValueRepository
					.findByOrderProductComponent(orderProductComponent);
			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
				String attrName = orderProductComponentsAttributeValue.getProductAttributeMaster().getName();
				String attrValue = orderProductComponentsAttributeValue.getAttributeValues();
				if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
					odrServiceDetail.setBillingGstNumber(attrValue);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail,
							odrComponent, "siteGstNumber", attrValue.trim()));
				} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
					Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
							.findById(Integer.valueOf(attrValue));
					OdrGstAddress odrGstAddress = null;
					if (addServiceParam.isPresent()) {
						String address = addServiceParam.get().getValue();
						if (StringUtils.isNotBlank(address)) {
							/*GstAddressBean gstAddress = Utils.convertJsonToObject(address, GstAddressBean.class);
							LOGGER.info("Site Gst address is {} ", gstAddress.toString());
							odrGstAddress = odrServiceDetail.getOdrGstAddress();
							if (odrServiceDetail.getOdrGstAddress() == null) {
								odrGstAddress = new OdrGstAddress();
							}
							odrGstAddress.setBuildingName(gstAddress.getBuildingName());
							odrGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
							odrGstAddress.setCreatedBy(odrServiceDetail.getCreatedBy());
							odrGstAddress.setCreatedTime(new Timestamp(new Date().getTime()));
							odrGstAddress.setDistrict(gstAddress.getDistrict());
							odrGstAddress.setFlatNumber(gstAddress.getFlatNumber());
							odrGstAddress.setLatitude(gstAddress.getLatitude());
							odrGstAddress.setLocality(gstAddress.getLocality());
							odrGstAddress.setLongitude(gstAddress.getLongitude());
							odrGstAddress.setPincode(gstAddress.getPinCode());
							odrGstAddress.setState(gstAddress.getState());
							odrGstAddress.setStreet(gstAddress.getStreet());
							//odrServiceDetail.setOdrGstAddress(odrGstAddress);
							odrGstAddressRepository.save(odrGstAddress);
							LOGGER.info("Site Gst address id {} for service id {} " ,odrGstAddress.getId(),odrServiceDetail.getId());*/
							odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail,
									odrComponent, "siteGstAddress", address));
						} else {
							LOGGER.warn("Address is empty for id {}", attrValue);
						}

						/*odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail,
								odrComponent, "siteGstAddressId", odrGstAddress.getId().toString()));
						odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail,
								odrComponent, "siteGstAddress", address));*/
					}
				} else if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
					LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
					if (localItResponse != null) {
						@SuppressWarnings("unchecked")
						Map<String, Object> localItDetail = (Map<String, Object>) Utils
								.convertJsonToObject(localItResponse, Map.class);
						odrServiceDetail.setLocalItContactEmail(
								localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
						odrServiceDetail.setLocalItContactMobile(
								localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO")
										: null);
						odrServiceDetail.setLocalItContactName(
								localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
						odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail,
								odrComponent, "localItContactEmailId",
								localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
						odrComponent.getOdrComponentAttributes()
								.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
										"localItContactMobile",
										localItDetail.get("CONTACT_NO") != null
												? (String) localItDetail.get("CONTACT_NO")
												: null));
						odrComponent.getOdrComponentAttributes()
								.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, "localItContactName",
										localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
					}
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, attrName, attrValue));
				} else if (attrName.equals("Effective Date") && StringUtils.isNotBlank(attrValue)) {
					try {
						odrServiceDetail
								.setServiceCommissionedDate(new SimpleDateFormat("yyyy-MM-dd").parse(attrValue));
					} catch (Exception e) {
						LOGGER.error("Error while parsing the commited delivery date for CGW {}", isPrimary);
					}
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(odrServiceDetail, odrComponent, attrName, attrValue.trim()));
				} else {
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
							odrServiceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(attrValue);
					odrServiceAttribute.setCategory(orderProductComponent.getMstProductComponent().getName());
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(odrServiceDetail, odrComponent, attrName, attrValue.trim()));
				}

			}
		}

	}

	@Override
	protected String getReferenceName() {
		// TODO Auto-generated method stub
		return OdrConstants.IZOSDWAN_SITES;
	}
	
	private String getAttributeValue(Integer siteId, String componentName, String attributeName) {
		OrderProductComponent qpc = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId, componentName,
						IzosdwanCommonConstants.IZOSDWAN_SITES)
				.stream().findFirst().orElse(null);
		if (qpc != null) {
			OrderProductComponentsAttributeValue qpcav = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent_IdAndProductAttributeMaster_Name(qpc.getId(), attributeName).stream()
					.findFirst().orElse(null);
			if (qpcav != null) {
				LOGGER.info("Got Attribute value for component {} and attribute name {} as {}", componentName,
						attributeName, qpcav.getAttributeValues());
				return qpcav.getAttributeValues();
			}
		}
		return CommonConstants.EMPTY;
	}
	
	private void processServiceAttributesBySiteTypeUnderlay(OdrServiceDetail odrServiceDetail,String serviceClasiification,Map<String, MstServiceClassificationDetail> serviceClassificationMap,String username) {
		LOGGER.info("Inside processServiceAttributesBySiteType for {}",odrServiceDetail.getUuid());
		if (serviceClassificationMap.containsKey(serviceClasiification)) {
			LOGGER.info("Got service classification details for {}", serviceClasiification);
			MstServiceClassificationDetail mstServiceClassificationDetail = serviceClassificationMap
					.get(serviceClasiification);
			if (mstServiceClassificationDetail != null) {
				if (mstServiceClassificationDetail.getServiceClassification() != null) {
					odrServiceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("serviceClassification",
									mstServiceClassificationDetail.getServiceClassification(), "IZOSDWAN_COMMON",
									odrServiceDetail, CommonConstants.N, username));
				}
				if (mstServiceClassificationDetail.getFirstWanLinkType() != null) {
					odrServiceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("firstWanLinkType",
									mstServiceClassificationDetail.getFirstWanLinkType(), "IZOSDWAN_COMMON",
									odrServiceDetail, CommonConstants.N, username));

				}
				if (mstServiceClassificationDetail.getInternetBreakout() != null) {
					odrServiceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("internetBreakOut",
									mstServiceClassificationDetail.getInternetBreakout(), "IZOSDWAN_COMMON",
									odrServiceDetail, CommonConstants.N, username));
				}
				if (mstServiceClassificationDetail.getNoOfCpe() != null) {
					odrServiceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("noOfCpe", mstServiceClassificationDetail.getNoOfCpe(),
									"IZOSDWAN_COMMON", odrServiceDetail, CommonConstants.N, username));
					String byonCpeType = CommonConstants.EMPTY;
					if (mstServiceClassificationDetail.getNoOfCpe().equalsIgnoreCase("One")) {
						byonCpeType = "Single CPE";
					} else if (mstServiceClassificationDetail.getNoOfCpe().equalsIgnoreCase("Two")) {
						byonCpeType = "Dual CPE";
					}
					odrServiceDetail.getOdrServiceAttributes().add(constructOdrServiceAttribute("byonCpeType",
							byonCpeType, "IZOSDWAN_COMMON", odrServiceDetail, CommonConstants.N, username));
				}
				if (mstServiceClassificationDetail.getNoOfWanLinks() != null) {
					odrServiceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("noOfWanLinks",
									mstServiceClassificationDetail.getNoOfWanLinks(), "IZOSDWAN_COMMON",
									odrServiceDetail, CommonConstants.N, username));
				}
				if (mstServiceClassificationDetail.getSecondWanLinkType() != null) {
					odrServiceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("secondWanLinkType",
									mstServiceClassificationDetail.getSecondWanLinkType(), "IZOSDWAN_COMMON",
									odrServiceDetail, CommonConstants.N, username));
				}
				if (mstServiceClassificationDetail.getSiteType() != null) {
					odrServiceDetail.getOdrServiceAttributes()
							.add(constructOdrServiceAttribute("serviceClassificationType",
									mstServiceClassificationDetail.getSiteType(), "IZOSDWAN_COMMON", odrServiceDetail,
									CommonConstants.N, username));
				}
			}

		}
		
	}

}
