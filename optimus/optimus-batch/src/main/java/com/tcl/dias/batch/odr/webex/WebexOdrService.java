package com.tcl.dias.batch.odr.webex;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import com.tcl.dias.oms.entity.entities.OdrAsset;
import com.tcl.dias.oms.entity.repository.OdrAssetRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import com.tcl.dias.batch.odr.gsip.WebexGscOdrService;
import com.tcl.dias.batch.odr.gvpn.GvpnOdrService;
import com.tcl.dias.batch.odr.mapper.OdrWebexMapper;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.constants.WebexCommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
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
import com.tcl.dias.oms.entity.entities.OdrSolutionComponent;
import com.tcl.dias.oms.entity.entities.OdrWebexServiceCommercial;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrderUcaas;
import com.tcl.dias.oms.entity.entities.OrderUcaasDetail;
import com.tcl.dias.oms.entity.entities.OrderUcaasSiteDetails;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
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
import com.tcl.dias.oms.entity.repository.OdrSolutionComponentRepository;
import com.tcl.dias.oms.entity.repository.OdrWebexServiceCommercialRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasSiteDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class WebexOdrService extends OrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebexOdrService.class);
	
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
	OrderUcaasRepository orderUcaasRepository;
	
	@Autowired
	OrderUcaasDetailsRepository orderUcaasDetailsRepository;
	
	@Autowired
	OrderUcaasSiteDetailsRepository orderUcaasSiteDetailsRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	OdrAssetRepository odrAssetRepository;

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
	OdrWebexMapper odrMapper;

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
	
	@Autowired
	OdrWebexServiceCommercialRepository odrWebexServiceCommercialRepository;
	
	@Autowired
	OdrSolutionComponentRepository odrSolutionComponentRepository;
	
	@Autowired
	GvpnOdrService gvpnOdrService;

	@Autowired
	WebexGscOdrService gscOdrService;

	private final String GVPN_CODE = "6230";
	private final String WEBEX_CODE = "UCAAS";
	private final String GSC_CODE = "0002";

	@Override
	protected String getReferenceName() {
		return OdrConstants.UCAAS_WEBEX_SITE;
	}
	
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
		Map<String, String> flowMapper = new HashMap<>();
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
				List<OdrWebexServiceCommercial> odrWebexServiceCommercials = new ArrayList<>();
				List<OdrAsset> odrAssets=new ArrayList<>();
				//Map<String, Map<String, Integer>> prisecMapping = new HashMap<>();
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
							odrServiceCommercials, odrAttributes, odrWebexServiceCommercials,odrAssets);
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
				odrServiceDetailRepository.saveAll(odrServiceDetails);
				odrAssetRepository.saveAll(odrAssets);
				
				processWebexSolution(odrOrder, orderEntity.get(), username, odrContractInfos.get(0), flowMapper, odrServiceDetails);
				gscOdrService.processGscParentService(odrOrder, username, odrContractInfos.get(0), odrServiceDetails);

				for (OdrServiceCommercial odrServiceCommercial : odrServiceCommercials) {
					LOGGER.info("Type {}",odrServiceCommercial.getServiceType());
					LOGGER.info("Saving {}",odrServiceCommercial.getOdrServiceDetail().getId());
					odrServiceCommercialRepository.save(odrServiceCommercial);
				}
				//odrServiceCommercialRepository.saveAll(odrServiceCommercials);
				odrWebexServiceCommercialRepository.saveAll(odrWebexServiceCommercials);

				/*
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
				}*/

				List<OdrSolutionComponent> odrSolutionComponents = odrSolutionComponentRepository
						.findByOdrOrder(odrOrder);
				LOGGER.info("Odr contract infos :",odrContractInfos);
				OdrOrderBean odrOrderBean = odrMapper.mapOrderEntityToBean(odrOrder, odrContractInfos, odrAttributes,
						odrServiceDetails, odrServiceCommercials, odrSolutionComponents, odrWebexServiceCommercials);
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
			String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes, List<OdrWebexServiceCommercial> odrWebexServiceCommercials, List<OdrAsset> odrAssets) {
		for (OrderToLeProductFamily orderToLeProdFamily : orderToLe.getOrderToLeProductFamilies()) {
			flowMapper.put("productName", orderToLeProdFamily.getMstProductFamily().getName());
			flowMapper.put("productCode", orderToLeProdFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
			processProductSolution(orderToLe, flowMapper, orderToLeProdFamily, odrServiceDetails, odrContractInfo,
					odrOrder, username, odrServiceCommercials, odrAttributes, odrWebexServiceCommercials,odrAssets);
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
			List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes, List<OdrWebexServiceCommercial> odrWebexServiceCommercials, List<OdrAsset> odrAssets) {
		for (OrderProductSolution orderProductSolution : orderToLeProdFamily.getOrderProductSolutions()) {
			if ("MACD".equals(orderToLe.getOrderType())) {
				if(odrOrder.getOpOrderCode().contains(OdrConstants.UCAAS_WEBEX)){
					/* Needs to be implemented
					LOGGER.info("MACD IPC Flow, setting uuid for ODR::" + orderToLe.getOrder().getQuote().getQuoteToLes()
							.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
					flowMapper.put("serviceCode", orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
							.getErfServiceInventoryTpsServiceId()); */
				}else if(odrOrder.getOpOrderCode().contains("IAS") || odrOrder.getOpOrderCode().contains("GVPN")){
					//QuoteToLe quoteToLe=orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
					List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
					for (OrderIllSiteToService orderIllSiteToService : orderIllSiteToServices) {
						if (orderIllSiteToService.getType() != null
								&& orderIllSiteToService.getType().equalsIgnoreCase("secondary")) {
							flowMapper.put(orderIllSiteToService.getOrderIllSite().getSiteCode() + "-SEC",
									orderIllSiteToService.getErfServiceInventoryTpsServiceId());
						} else {
							flowMapper.put(orderIllSiteToService.getOrderIllSite().getSiteCode(),
									orderIllSiteToService.getErfServiceInventoryTpsServiceId());
						}
						flowMapper.put(String.valueOf(orderIllSiteToService.getOrderIllSite().getId()), orderIllSiteToService.getErfSfdcSubType());
					}
					//LOGGER.info("MACD ILL or GVPN Flow, setting uuid for ODR::" + quoteIllSiteToServices.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
					//flowMapper.put("serviceCode", quoteIllSiteToServices.stream().findFirst().get().getErfServiceInventoryTpsServiceId());
				}
			}
			if (orderToLeProdFamily.getMstProductFamily().getName().equalsIgnoreCase(OdrConstants.UCAAS)) {
				flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
				Map<String, String> ucaasVoiceOfferingMap = ucaasVoiceOfferingsData();
				LOGGER.info("Entering Ucaas with offeringName"+orderProductSolution.getMstProductOffering().getProductName());
				if(ucaasVoiceOfferingMap.get(orderProductSolution.getMstProductOffering().getProductName())!=null) {
					LOGGER.info("Entering Process Gsc with offeringName"+ucaasVoiceOfferingMap.get(orderProductSolution.getMstProductOffering().getProductName()));
					gscOdrService.processGsc(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username, odrServiceCommercials, odrAttributes,odrAssets);
				}
				else {
					LOGGER.info("Entering Process Webex with offeringName"+orderProductSolution.getMstProductOffering().getProductName());
					processWebEx(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username, odrAttributes, odrWebexServiceCommercials);
					processWebexEndpointSite(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username, odrWebexServiceCommercials);
				}
			} else if (orderToLeProdFamily.getMstProductFamily().getName().equalsIgnoreCase(OdrConstants.GSIP)) {
				/* Needs to be implemented
				flowMapper.put("offeringName", "IPC-CLOUD");
				processCloud(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username, odrAttributes); */

			} else {
				flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
				gvpnOdrService.processSite(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username, odrServiceCommercials);
				gvpnOdrService.processLink(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username);
			}
		}
	}
	
	public void processWebEx(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrOrderAttribute> odrAttributes, List<OdrWebexServiceCommercial> odrWebexServiceCommercials) {
		LOGGER.info("processWebEx method Invoked: {}", orderProductSolution.getId());
		List<OrderUcaas> OrderUcaas = orderUcaasRepository.findByOrderProductSolution(orderProductSolution);
		LOGGER.info("OrderUcaas List: {}", OrderUcaas.size());
		OrderUcaas.stream().findFirst().ifPresent(orderUcaas -> {
			try {
				OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();
				primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
				primaryserviceDetail.setServiceRefId(odrOrder.getOpOrderCode());
				primaryserviceDetail.setOdrContractInfo(odrContractInfo);
				primaryserviceDetail.setOdrOrder(odrOrder);
				primaryserviceDetail.setServiceCommissionedDate(orderUcaas.getCreatedTime());
				Optional<OrderToLe> optOrderToLe = Optional.ofNullable(orderUcaas.getOrderToLe());
				if (optOrderToLe.isPresent()) {
					primaryserviceDetail.setMrc(optOrderToLe.get().getFinalMrc());
					primaryserviceDetail.setNrc(optOrderToLe.get().getFinalNrc());
					primaryserviceDetail.setArc(optOrderToLe.get().getFinalArc());
				}
//				primaryserviceDetail.setErfPrdCatalogOfferingName(flowMapper.get("offeringName")); 
				primaryserviceDetail.setErfPrdCatalogOfferingName("Cisco WebEx CCA"); //hardcoded based on Agasthi's Input
				primaryserviceDetail.setErfPrdCatalogFlavourName("Cisco WebEx CCA");
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
				primaryserviceDetail.setDestinationCity("");
				
				LOGGER.info("OFFERING_NAME ID:{}, OFFERING: {}", orderUcaas.getId(), primaryserviceDetail.getErfPrdCatalogOfferingName());

				/* Need to check tax information
				String isTaxExept[] = { "" };
				odrAttributes.stream().filter(odrOrderAttr -> "isTaxExemption".equals(odrOrderAttr.getAttributeName()))
						.forEach(odrOrderAttr -> {
							if ("Yes".equals(odrOrderAttr.getAttributeValue())) {
								isTaxExept[0] = "Y";
							} else if ("no".equals(odrOrderAttr.getAttributeValue().toLowerCase())) {
								isTaxExept[0] = "N";
							}
						});
				primaryserviceDetail.setTaxExemptionFlag(isTaxExept[0]); */
				primaryserviceDetail.setTaxExemptionFlag("N");

				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(flowMapper.get("productName"), CommonConstants.BACTIVE);
				if (Objects.nonNull(mstProductFamily)) {
					primaryserviceDetail.setErfPrdCatalogProductId(mstProductFamily.getId());
				}
				LOGGER.info("MDC Filter token value in before Queue call constructCloudDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));

				/*String cityCode = CommonConstants.EMPTY;
				if (primaryserviceDetail.getDestinationCity() != null
						&& primaryserviceDetail.getDestinationCity().length() > 4) {
					cityCode = primaryserviceDetail.getDestinationCity().replaceAll(" ", "").replaceAll(".", "").substring(0, 4);
				} else {
					cityCode = Objects.nonNull(primaryserviceDetail.getDestinationCity())
							? primaryserviceDetail.getDestinationCity().replaceAll(" ", "").replaceAll(".", "")
							: primaryserviceDetail.getDestinationCity();
				}
				if (flowMapper.get("serviceCode") == null) {
					flowMapper.put("serviceCode", getServiceCode(flowMapper, cityCode,null));
					LOGGER.info("Generated service code : {}", getServiceCode(flowMapper, cityCode,null));
				}
				primaryserviceDetail.setUuid(flowMapper.get("serviceCode"));*/
				
				boolean isdealAdded = false;
				for (OrderUcaas orderUcas : OrderUcaas) {
					LOGGER.info("Processing orderUcas {}", orderUcas.getName());
					if(orderUcas.getName().equalsIgnoreCase("Configuration")) {
						processWebexServiceAttr(primaryserviceDetail, "license_provider", orderUcas.getLicenseProvider(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "primary_region", orderUcas.getPrimaryRegion(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "cug_required", orderUcas.getCugRequired().toString(), "Configuration", username, false);
						if(orderUcas.getCugRequired() == 1) {
							processWebexServiceAttr(primaryserviceDetail, "access_type", "GVPN", "Configuration", username, false);
						} else {
							processWebexServiceAttr(primaryserviceDetail, "access_type", "PSTN", "Configuration", username, false);
						}
						processWebexServiceAttr(primaryserviceDetail, "audio_model", orderUcas.getAudioModel(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "payment_model", orderUcas.getPaymentModel(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "audio_type", orderUcas.getAudioType(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "gvpn_mode", orderUcas.getGvpnMode(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "dial_in", orderUcas.getDialIn().toString(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "dial_out", orderUcas.getDialOut().toString(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "dial_back", orderUcas.getDialBack().toString(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "is_lns", orderUcas.getIsLns().toString(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "is_itfs", orderUcas.getIsItfs().toString(), "Configuration", username, false);
						processWebexServiceAttr(primaryserviceDetail, "is_outbound", orderUcas.getIsOutbound().toString(), "Configuration", username, false);
						List<OrderUcaasDetail> orderUcaasDetails = orderUcaasDetailsRepository.findByOrderUcaas(orderUcas);
						if(orderUcaasDetails != null && !orderUcaasDetails.isEmpty()) {
							OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
							odrAdditionalServiceParam.setAttribute("ucaas-bom-resource");
							odrAdditionalServiceParam.setCategory("UCAAS-BOM");
							odrAdditionalServiceParam.setCreatedBy(username);
							odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
							odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
							odrAdditionalServiceParam.setValue(orderUcaasDetails.get(0).getResponse());
							odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);
							processWebexServiceAttr(primaryserviceDetail, "ucaas_bom", odrAdditionalServiceParam.getId().toString(), "BOM", username, true);
						}
					} else if(!orderUcas.getUom().equalsIgnoreCase("Endpoint")) {
						processWebexServiceCommercial(orderUcas, primaryserviceDetail, odrWebexServiceCommercials, username);
						if(!isdealAdded && orderUcas.getDealId() != null) {
							processWebexServiceAttr(primaryserviceDetail, "deal_id", orderUcas.getDealId().toString(), "Commercial", username, false);
							isdealAdded = true;
						}
						/*if(orderUcas.getUom().equalsIgnoreCase("Endpoint")) {
							for(int i=0;i<orderUcas.getQuantity();i++) {
								OdrComponent primaryOdrComponent = persistOdrComponent(primaryserviceDetail, orderUcas.getName());
								primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
							}
						}*/
					}
				}
				
				//OdrComponent licenseOdrComponent = persistOdrComponent(primaryserviceDetail, "WebEx-License");
				//primaryserviceDetail.getOdrComponents().add(licenseOdrComponent);
				OdrComponent lmOdrComponent = persistOdrComponent(primaryserviceDetail, "LM");
				primaryserviceDetail.getOdrComponents().add(lmOdrComponent);
				/*
				LOGGER.info("Retrieving location details");
				if (null != cloud.getDcLocationId()) {
					LOGGER.info("MDC Filter token value in before Queue call for ipc pricing location details {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					Map<String, Object> localIPCPricingLocation = constructIPCPricingLocationDetails(
							cloud.getDcLocationId());
					if (null != localIPCPricingLocation && !localIPCPricingLocation.isEmpty()) {
						LOGGER.info("Location map exists");
						constructServiceLocationDetails(localIPCPricingLocation, cloud, primaryserviceDetail);
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
				*/
				odrServiceDetails.add(primaryserviceDetail);
			} catch (Exception e) {
				LOGGER.warn("Error in Order Le Cloud : {}", ExceptionUtils.getStackTrace(e));
			}
		});
	}
	
	public void processWebexEndpointSite(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrWebexServiceCommercial> odrWebexServiceCommercials) {
		List<OrderUcaasSiteDetails> orderUcaasSiteDetails = orderUcaasSiteDetailsRepository.findByOrderProductSolutionId(orderProductSolution.getId());
		for (OrderUcaasSiteDetails orderUcaasSiteDetail : orderUcaasSiteDetails) {
			try {
				OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();
				OdrComponent primaryOdrComponent = persistOdrComponent(primaryserviceDetail, "LM");
				primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
				Set<OdrComponentAttribute> odrComponentAttributes=primaryOdrComponent.getOdrComponentAttributes();
				primaryserviceDetail.setFlowStatus("NEW");
				primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "siteCode", orderUcaasSiteDetail.getSiteCode()));
				primaryserviceDetail.setServiceRefId(orderUcaasSiteDetail.getSiteCode());
				primaryserviceDetail.setOdrContractInfo(odrContractInfo);
				primaryserviceDetail.setOdrOrder(odrOrder);
				/*primaryserviceDetail.setServiceCommissionedDate(
						orderIllSite.getEffectiveDate() != null ? orderIllSite.getEffectiveDate()
								: orderIllSite.getRequestorDate());
				primaryserviceDetail.setRrfsDate(orderIllSite.getRequestorDate());
				primaryserviceDetail.setArc(orderIllSite.getArc());
				primaryserviceDetail.setMrc(orderIllSite.getMrc());
				primaryserviceDetail.setNrc(orderIllSite.getNrc());*/
				//primaryserviceDetail.setErfPrdCatalogOfferingName(flowMapper.get("offeringName"));
				primaryserviceDetail.setErfPrdCatalogOfferingName("UCCEndpoint"); //hardcoded based on Agasthi & Sam discussion
				primaryserviceDetail.setCreatedBy(username);
				primaryserviceDetail.setCreatedDate(new Date());
				primaryserviceDetail.setUpdatedDate(new Date());
				primaryserviceDetail.setUpdatedBy(username);
				primaryserviceDetail.setIsActive(CommonConstants.Y);
				primaryserviceDetail.setIsIzo(CommonConstants.N);
				
				/*if("MACD".equals(odrOrder.getOrderType()) && flowMapper.containsKey(String.valueOf(orderIllSite.getId()))){
					LOGGER.info("Order Sub Category");
					primaryserviceDetail.setOrderSubCategory(flowMapper.get(String.valueOf(orderIllSite.getId())));
				}*/
				/*if (orderUcaasSiteDetail.getIsTaxExempted() != null
						&& orderUcaasSiteDetail.getIsTaxExempted().equals(CommonConstants.BACTIVE)) {
					primaryserviceDetail.setTaxExemptionFlag(CommonConstants.Y);
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "taxExemption", CommonConstants.Y));
				}else {*/
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "taxExemption", CommonConstants.N));
				//}
				primaryserviceDetail.setErfPrdCatalogProductName(flowMapper.get("productName"));
				//primaryserviceDetail.setErfPrdCatalogProductName("CPE"); //hardcoded based on Agasthi & Sam discussion
				primaryserviceDetail.setErfPrdCatalogProductId(Objects.nonNull(flowMapper.get("productCode"))?Integer.valueOf(flowMapper.get("productCode").trim()):null);
				/*if(("MACD".equals(odrOrder.getOrderType()) && "ADD_SITE".equals(odrOrder.getOrderCategory()))
						|| ("MACD".equals(odrOrder.getOrderType()) && Objects.nonNull(primaryserviceDetail.getOrderSubCategory()) 
								&& primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))){
					LOGGER.info("Parent UUID {}",flowMapper.get(orderIllSite.getSiteCode()));
					LOGGER.info("Order Sub Category {}",primaryserviceDetail.getOrderSubCategory());
					primaryserviceDetail.setParentUuid(flowMapper.get(orderIllSite.getSiteCode()));
				}else if("MACD".equals(odrOrder.getOrderType()) && !"ADD_SITE".equals(odrOrder.getOrderCategory())
						&& (Objects.isNull(primaryserviceDetail.getOrderSubCategory()) || (Objects.nonNull(primaryserviceDetail.getOrderSubCategory()) && !primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))){
					LOGGER.info("UUID {}",orderIllSite.getSiteCode());
					primaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()));
				}*/
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("Order Site address for Reference Id {}",orderUcaasSiteDetail.getId());
				/*OrderSiteAddress orderSiteAddress = orderSiteAddressRepository.findByReferenceIdAndReferenceNameAndSiteType(orderIllSite.getId().toString(), "ILL_SITES", "b");
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
				}
				else {*/
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(orderUcaasSiteDetail.getEndpointLocationId()));
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
								.setErfLocSiteAddressId(String.valueOf(orderUcaasSiteDetail.getEndpointLocationId()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "siteAddressId", orderUcaasSiteDetail.getEndpointLocationId()+""));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "siteAddress", addr));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "latLong", addressDetail.getLatLong()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationCountry", addressDetail.getCountry()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationCity", addressDetail.getCity()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationAddressLineOne", addressDetail.getAddressLineOne()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationAddressLineTwo", addressDetail.getAddressLineTwo()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationLocality", addressDetail.getLocality()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationState", addressDetail.getState()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "destinationPincode", addressDetail.getPincode()));
						}
					}
				//}
				/*
				 String cityCode = CommonConstants.EMPTY;
				 if (primaryserviceDetail.getDestinationCity() != null && primaryserviceDetail.getDestinationCity().length() > 4) {
					 cityCode = primaryserviceDetail.getDestinationCity().substring(0, 4);
				 } else {
					 cityCode = primaryserviceDetail.getDestinationCity();
				 }
				 String serviceCode = getServiceCode(flowMapper, cityCode, null);
				 primaryserviceDetail.setUuid(serviceCode);*/

				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String demarReponse = (String) mqUtils.sendAndReceive(demarcationQueue,
						String.valueOf(orderUcaasSiteDetail.getEndpointLocationId()));
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

				if (orderUcaasSiteDetail.getEndpointLocationId() != null) {
					LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String popResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(orderUcaasSiteDetail.getEndpointLocationId()));
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
							primaryserviceDetail.setPopSiteCode(orderUcaasSiteDetail.getSiteCode());
							primaryserviceDetail.setSourceCountry(popAddressDetail.getCountry());
							primaryserviceDetail.setSourceCity(popAddressDetail.getCity());
							primaryserviceDetail.setSourceAddressLineOne(popAddressDetail.getAddressLineOne());
							primaryserviceDetail.setSourceAddressLineTwo(popAddressDetail.getAddressLineTwo());
							primaryserviceDetail.setSourceLocality(popAddressDetail.getLocality());
							primaryserviceDetail.setSourcePincode(popAddressDetail.getPincode());
							primaryserviceDetail.setSourceState(popAddressDetail.getState());
							
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "popSiteAddressId", orderUcaasSiteDetail.getEndpointLocationId()+""));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "popSiteAddress", popAddr));
							odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "popSiteCode", orderUcaasSiteDetail.getSiteCode()));
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
				
				processCommonComponentAttr(String.valueOf(orderUcaasSiteDetail.getId()), primaryserviceDetail, username, flowMapper,primaryOdrComponent);
				
				List<OrderUcaas> OrderUcaas = orderUcaasRepository.findByOrderProductSolutionAndUomAndEndpointLocationId(orderProductSolution, "Endpoint", orderUcaasSiteDetail.getEndpointLocationId());
				boolean isdealAdded = false;
				for (OrderUcaas orderUcas : OrderUcaas) {
					processWebexServiceCommercial(orderUcas, primaryserviceDetail, odrWebexServiceCommercials, username);
					if(!isdealAdded && orderUcas.getDealId() != null) {
						processWebexServiceAttr(primaryserviceDetail, "deal_id", orderUcas.getDealId().toString(), "Commercial", username, false);
						isdealAdded = true;
					}
					for(int i=0;i<orderUcas.getQuantity();i++) {
						OdrComponent OdrComponent = persistOdrComponent(primaryserviceDetail, orderUcas.getName());
						primaryserviceDetail.getOdrComponents().add(OdrComponent);
					}
				}
				
				odrServiceDetails.add(primaryserviceDetail);
				/*for (OdrComponent odrComp : primaryserviceDetail.getOdrComponents()) {
					odrComp.setOdrServiceDetail(primaryserviceDetail);
					for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
						LOGGER.info("setting primary reference");
						odrComponentAttribute.setOdrServiceDetail(primaryserviceDetail);
					}
				}*/
				//for (OdrServiceAttribute odrComp : primaryserviceDetail.getOdrServiceAttributes()) {
					//odrComp.setOdrServiceDetail(primaryserviceDetail);
				//}
				
			} catch (Exception e) {
				LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	public void processCommonComponentAttr(String refId, OdrServiceDetail serviceDetail, String username,
			Map<String, String> flowMapper,OdrComponent odrComponent) throws TclCommonException, IllegalArgumentException {
		LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference Id {}", getReferenceName(), refId);
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, "WEBEX_SITE", getReferenceName());
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstNumber",attrValue.trim()));
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
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption",CommonConstants.Y));
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
	
	private void processWebexServiceAttr(OdrServiceDetail serviceDetail, String attrName, String attrValue, String category,
			String userName, boolean isAddParam) throws TclCommonException, IllegalArgumentException, ParseException {
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
		odrServiceAttribute.setIsAdditionalParam(isAddParam ? CommonConstants.Y : CommonConstants.N);
		serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
	}
	
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
	
	private void processWebexServiceCommercial(OrderUcaas orderUcaas, OdrServiceDetail odrServiceDetail, List<OdrWebexServiceCommercial> odrWebexServiceCommercials, String username) {
		OdrWebexServiceCommercial odrWebexServiceCommercial = new OdrWebexServiceCommercial();
		odrWebexServiceCommercial.setOdrServiceDetail(odrServiceDetail);
		odrWebexServiceCommercial.setComponentName(orderUcaas.getName());
		odrWebexServiceCommercial.setComponentDesc(orderUcaas.getDescription());
		odrWebexServiceCommercial.setComponentType(orderUcaas.getUom());
		if(orderUcaas.getUom().equalsIgnoreCase("License")) {
			odrWebexServiceCommercial.setShortText("Subscription License");
		}
		odrWebexServiceCommercial.setHsnCode(orderUcaas.getHsnCode());
		odrWebexServiceCommercial.setQuantity(orderUcaas.getQuantity());
		odrWebexServiceCommercial.setUnitMrc(orderUcaas.getUnitMrc());
		odrWebexServiceCommercial.setUnitNrc(orderUcaas.getUnitNrc());
		odrWebexServiceCommercial.setMrc(orderUcaas.getMrc());
		odrWebexServiceCommercial.setNrc(orderUcaas.getNrc());
		odrWebexServiceCommercial.setArc(orderUcaas.getArc());
		odrWebexServiceCommercial.setTcv(orderUcaas.getTcv());
		odrWebexServiceCommercial.setCiscoUnitListPrice(orderUcaas.getCiscoUnitListPrice());
		odrWebexServiceCommercial.setCiscoDiscountPercent(orderUcaas.getCiscoDiscountPercent());
		odrWebexServiceCommercial.setCiscoUnitNetPrice(orderUcaas.getCiscoUnitNetPrice());
		odrWebexServiceCommercial.setContractType(orderUcaas.getContractType());
		odrWebexServiceCommercial.setEndpointManagementType(orderUcaas.getEndpointManagementType());
		odrWebexServiceCommercial.setCreatedBy(username);
		odrWebexServiceCommercial.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		odrWebexServiceCommercials.add(odrWebexServiceCommercial);
	}
	
	/**
	 * persistOdrComponent
	 * @param primaryserviceDetail
	 * @param odrComponentAttributes
	 * @return
	 */
	private OdrComponent persistOdrComponent(OdrServiceDetail odrServiceDetail, String componentName) {
		OdrComponent odrComponent=new OdrComponent();
		odrComponent.setComponentName(componentName);
		odrComponent.setOdrServiceDetail(odrServiceDetail);
		odrComponent.setCreatedBy(Utils.getSource());
		odrComponent.setCreatedDate(new Date());
		odrComponent.setSiteType("A");
		odrComponent.setIsActive(CommonConstants.Y);
		odrComponent.setUuid(Utils.generateUid());
		odrComponent.setOdrComponentAttributes(new HashSet<>());
		return odrComponent;
	}
	
	/**
	 * getServiceCode
	 * 
	 * @param flowMapper
	 * @param cityCode
	 * @return
	 */
	private String getServiceCode(Map<String, String> flowMapper, String cityCode, OrderGscDetail gscDetail) {
		String prodCode = CommonConstants.EMPTY;
		if (flowMapper.get("productName").equals("UCAAS")) {
			prodCode = WEBEX_CODE;
		} else if (flowMapper.get("productName").equals(OdrConstants.GSIP)) {
			prodCode = GSC_CODE;
		} else {
			prodCode = GVPN_CODE;
		}
		if (cityCode == null)
			cityCode = "";
		String primaryKey = "";
		String serviceCode = "091" + cityCode.toUpperCase() + prodCode + "A";


		if(!flowMapper.get("productName").equals(OdrConstants.GSIP)) {
			OdrServiceDetail odrServiceDetail = odrServiceDetailRepository.findTopByOrderByIdDesc();
			if (odrServiceDetail != null) {
				primaryKey = String.valueOf(odrServiceDetail.getId() + 1);
			} else {
				primaryKey = "1";
			}
		}
		else
		{
			if (gscDetail != null) {
				primaryKey = String.valueOf(gscDetail.getId() + 1);
			} else {
				primaryKey = "1";
			}
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
		odrSolutionComponent = odrSolutionComponentRepository.save(odrSolutionComponent);
		return odrSolutionComponent;
	}

	/**
	 *
	 * This function return optimus order sub category and corresponding Sap Service type
	 * @author AnandhiV
	 * @return
	 */
	public Map<String,String> ucaasVoiceOfferingsData(){
		Map<String,String> offeringsMap = new HashMap<>();
		offeringsMap.put("LNS on PSTN", "LNS on PSTN");
		offeringsMap.put("ITFS on PSTN", "ITFS on PSTN");
		offeringsMap.put("Global Outbound on PSTN", "Global Outbound on PSTN");
		offeringsMap.put("ITFS on MPLS", "ITFS on MPLS");
		offeringsMap.put("LNS on MPLS", "LNS on MPLS");
		offeringsMap.put("Global Outbound on MPLS", "Global Outbound on MPLS");
		return offeringsMap;

	}
	
	private void processWebexSolution(OdrOrder odrOrder, Order order, String username, OdrContractInfo odrContractInfo,Map<String, String> flowMapper, List<OdrServiceDetail> odrServiceDetails) {
		LOGGER.info("process webex solution method invoked");
		OdrServiceDetail solutionOdrServiceDetail = new OdrServiceDetail();
		solutionOdrServiceDetail.setServiceRefId(Utils.generateUid());
		//solutionOdrServiceDetail.setErfPrdCatalogOfferingName(
				//orderProductSolution.getMstProductOffering().getProductName());
		solutionOdrServiceDetail.setCreatedBy(username);
		solutionOdrServiceDetail.setCreatedDate(new Date());
		solutionOdrServiceDetail.setUpdatedDate(new Date());
		solutionOdrServiceDetail.setUpdatedBy(username);
		solutionOdrServiceDetail.setIsActive(CommonConstants.Y);
		solutionOdrServiceDetail.setIsIzo(CommonConstants.N);
		solutionOdrServiceDetail.setErfPrdCatalogProductName(
				WebexCommonConstants.WEBEX_SOLUTION);
		/*solutionOdrServiceDetail
				.setErfPrdCatalogProductId(orderProductSolution.getMstProductOffering()
						.getMstProductFamily().getProductCatalogFamilyId());*/
		solutionOdrServiceDetail.setOdrOrder(odrOrder);
		solutionOdrServiceDetail.setOdrOrderUuid(odrOrder.getUuid());
		solutionOdrServiceDetail.setOrderType(CommonConstants.NEW);
		OdrComponent solutionOdrComponent=persistOdrComponent(solutionOdrServiceDetail);
		solutionOdrComponent.setSiteType("A");
		solutionOdrComponent.setOdrServiceDetail(solutionOdrServiceDetail);
		solutionOdrServiceDetail.getOdrComponents().add(solutionOdrComponent);
		solutionOdrServiceDetail = odrServiceDetailRepository.saveAndFlush(solutionOdrServiceDetail);
		
		for(OdrServiceDetail odrServiceDetail : odrServiceDetails) {
			constructOdrSolutionComponents(solutionOdrServiceDetail, odrServiceDetail, solutionOdrServiceDetail,
					null, odrOrder, WebexCommonConstants.COMMON);
		}
		
		odrServiceDetails.add(solutionOdrServiceDetail);
	}
	
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
}
