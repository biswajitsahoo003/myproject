package com.tcl.dias.batch.odr.gde;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import com.tcl.dias.batch.odr.mapper.OdrMapper;
import com.tcl.dias.batch.odr.mapper.OdrNplMapper;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderResponse;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.OrderSource;
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
import com.tcl.dias.oms.entity.entities.OdrProductDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercial;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercialComponent;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceSla;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
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
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class GdeOdrService extends OrderService {
	/**
	 * 
	 * getReferenceName
	 * 
	 * @return
	 */
	protected String getReferenceName() {
		return OdrConstants.GDE_LINK;

	}

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
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository OrderProductComponentsAttributeValueRepository;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

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

	@Value("${rabbitmq.gde.sc.fulfillmentdate}")
	String gdeScFulfillmentQueue;

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

//	@Autowired
//	OdrMapper odrMapper;

	@Autowired
	OdrGdeMapper odrGdeMapper;

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
	OrderNplLinkRepository orderNplLinkRepository;

	@Autowired
	OdrComponentRepository odrComponentRepository;

	@Autowired
	OdrComponentAttributeRepository odrComponentAttributeRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;
	
	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	
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
				if(stage.equalsIgnoreCase("BOOKING_SUCCESS"))
					stage = "ORDER_COMPLETED";
				LOGGER.info("processed process order");
				boolean flag = true;
				List<OdrOrderAttribute> odrAttributes = new ArrayList<>();
				List<OdrContractInfo> odrContractInfos = new ArrayList<>();
				List<OdrServiceDetail> odrServiceDetails = new ArrayList<>();
				List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();
				Map<String, Map<String, Integer>> prisecMapping = new HashMap<>();
				List<OdrComponent> odrComponents = new ArrayList<>();
				List<OdrComponentAttribute> odrComponentAttributes = new ArrayList<>();
				List<OdrAttachment> odrAttachments = new ArrayList<>();
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
							odrServiceCommercials, odrAttributes, odrComponents, odrComponentAttributes,odrAttachments);
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
				}

				OdrOrderBean odrOrderBean = odrGdeMapper.mapOrderEntityToBean(odrOrder, odrContractInfos, odrAttributes,
						odrServiceDetails, odrServiceCommercials, odrComponents, odrComponentAttributes);
				odrOrderBean.setStage(stage);
				LOGGER.info("order froze completed");
				String resPayload = Utils.convertObjectToJson(odrOrderBean);
				LOGGER.info("Response to be transformed to sc is {}", resPayload);
				LOGGER.info("MDC Filter token value in before Queue call fulfillment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(gdeScFulfillmentQueue, Utils.convertObjectToJson(odrOrderBean));

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
			String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes,
			List<OdrComponent> odrComponents, List<OdrComponentAttribute> odrComponentAttributes,List<OdrAttachment> odrAttachments) {
		LOGGER.info("Inside Npl ODR service processProduct Family ");
		for (OrderToLeProductFamily orderToLeProdFamily : orderToLe.getOrderToLeProductFamilies()) {
			flowMapper.put("productName", orderToLeProdFamily.getMstProductFamily().getName());
			flowMapper.put("productCode", orderToLeProdFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
			processProductSolution(orderToLe, flowMapper, orderToLeProdFamily, odrServiceDetails, odrContractInfo,
					odrOrder, username, odrServiceCommercials, odrAttributes, odrComponents, odrComponentAttributes,odrAttachments);
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
			List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes,
			List<OdrComponent> odrComponents, List<OdrComponentAttribute> odrComponentAttributes,List<OdrAttachment> odrAttachments) {
		LOGGER.info("Inside Npl ODR service processProductSolution ");
		for (OrderProductSolution orderProductSolution : orderToLeProdFamily.getOrderProductSolutions()) {

			flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
			if ("MACD".equals(orderToLe.getOrderType())) {
				// QuoteToLe
				// quoteToLe=orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get();
				List<OrderIllSiteToService> orderIllSiteToServices = orderIllSiteToServiceRepository
						.findByOrderToLe_Id(orderToLe.getId());
				for (OrderIllSiteToService orderIllSiteToService : orderIllSiteToServices) {
					if (orderIllSiteToService.getType() != null
							&& orderIllSiteToService.getType().equalsIgnoreCase("secondary")) {
						flowMapper.put(orderIllSiteToService.getOrderNplLink().getLinkCode() + "-SEC",
								orderIllSiteToService.getErfServiceInventoryTpsServiceId());
					} else {
						flowMapper.put(orderIllSiteToService.getOrderNplLink().getLinkCode(),
								orderIllSiteToService.getErfServiceInventoryTpsServiceId());
					}
				}
			}
			processLink(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username,
					odrServiceCommercials, odrComponents, odrComponentAttributes, odrAttachments);

		}
	}

	/**
	 * processIllSite
	 * 
	 * @param orderProductSolution
	 */
	public void processSite(Map<String, String> flowMapper, String username, OrderNplLink orderNplLink,
			OdrServiceDetail primaryserviceDetail, List<OdrComponent> odrComponents,
			List<OdrComponentAttribute> odrComponentAttributes,List<OdrAttachment> odrAttachments) {
		LOGGER.info("Inside Gde odr service processSite");
		// TODO
		try {
			OrderIllSite orderIllSiteA = orderIllSiteRepository.findByIdAndStatus(orderNplLink.getSiteAId(),
					CommonConstants.BACTIVE);
			OrderIllSite orderIllSiteB = orderIllSiteRepository.findByIdAndStatus(orderNplLink.getSiteBId(),
					CommonConstants.BACTIVE);
			processProductComponentAttrForSite(orderNplLink.getSiteAId(), primaryserviceDetail,
					"Site-A", username, false, flowMapper, odrComponents, odrComponentAttributes,
					orderIllSiteA.getErfLocSitebLocationId(), orderIllSiteA.getErfLocSiteaLocationId(),
					orderIllSiteA.getErfLocSiteaSiteCode(),orderNplLink,odrAttachments);
			processProductComponentAttrForSite(orderNplLink.getSiteBId(), primaryserviceDetail,
					"Site-B", username, false, flowMapper, odrComponents, odrComponentAttributes,
					orderIllSiteB.getErfLocSitebLocationId(), orderIllSiteB.getErfLocSiteaLocationId(),
					orderIllSiteB.getErfLocSiteaSiteCode(),orderNplLink,odrAttachments);
		} catch (Exception e) {
			LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
		}

	}

	/**
	 * processSiteSla
	 * 
	 * @param orderIllSite
	 * @param primaryserviceDetail
	 */
	private void processLinkSla(OrderNplLink link, OdrServiceDetail primaryserviceDetail, String username) {
		LOGGER.info("Inside Npl Odr service processLinkSla");
		List<Map<String, String>> siteSlas = orderNplLinkSlaRepository.findByLinkId(link.getId());
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

	public void processCommercialComponent(String refId, OdrProductDetail odrProductDetail, String username,
			Map<String, String> flowMapper) throws TclCommonException, IllegalArgumentException {
		LOGGER.info("Processing Commercial Component for reference Name {} and reference Id {}", refId,
				getReferenceName());
		List<Map<String, Object>> oderProdCompAttrs = orderProductComponentRepository.findByRefidAndRefName(refId,
				getReferenceName());
		for (Map<String, Object> oderProdCompAttr : oderProdCompAttrs) {
			String itemType = oderProdCompAttr.get("item_type") + "";
			String item = oderProdCompAttr.get("item") + "";
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


	public void processCommonComponentAttr(String refId, OdrServiceDetail serviceDetail, String username,
			Map<String, String> flowMapper) throws TclCommonException, IllegalArgumentException {
		LOGGER.info("Inside GDE ODR service processCommonComponentAttr ");
		LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference Id {}", refId,
				OdrConstants.GDE_LINK);
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId,"Link",OdrConstants.GDE_LINK );
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("GST_ADDRESS");
					odrAdditionalServiceParam.setCategory(null);
					odrAdditionalServiceParam.setCreatedBy(username);
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(addServiceParam.get().getValue());
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
							.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
									category);
					if (odrServiceAttribute == null) {
						odrServiceAttribute = new OdrServiceAttribute();
					}
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
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
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
			} else {
				OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
						.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
								category);
				if (odrServiceAttribute == null) {
					odrServiceAttribute = new OdrServiceAttribute();
				}
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
			}

		}
	}

	@SuppressWarnings("unchecked")
	public OdrServiceDetail processProductComponentAttr(String refId, OdrServiceDetail serviceDetail, String type,
			String userName, boolean isUpdate, List<OdrServiceCommercial> odrServiceCommercials,
			Map<String, String> flowMapper, OrderNplLink link)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Inside Gde odr service processProductComponentAttr");
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndTypeAndReferenceName(Integer.valueOf(refId), type,OdrConstants.GDE_LINK);
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

			}

		}

		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, type, OdrConstants.GDE_LINK);
		if (oderProdCompAttrs.isEmpty()) {
			return null;
		}
		serviceDetail.setPrimarySecondary("Link");
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("Port Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBwPortspeed(attrValue);
				serviceDetail.setBwPortspeedAltName(attrValue);
				serviceDetail.setBwUnit("Mbps");
				String[] bw = attrValue.split(" ");
				if(bw!=null) {
					flowMapper.put("Port Bandwidth", bw[0]);
				}
			} else if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
			}  else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
			} else if (attrName.equals("Interface") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setSiteEndInterface(attrValue);
			} else if (attrName.equals("CPE Management Type") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setServiceOption(attrValue);
			} else if (attrName.equals("Interface Type - A end") && StringUtils.isNotBlank(attrValue)) {
				flowMapper.put("Interface Type - A end", attrValue);
				// serviceDetail.setSiteEndInterface(attrValue);
			} else if (attrName.equals("Interface Type - B end") && StringUtils.isNotBlank(attrValue)) {
				flowMapper.put("Interface Type - B end", attrValue);
				// serviceDetail.setSiteEndInterface(attrValue);
			} else if (attrName.equals("Connector Type - A end") && StringUtils.isNotBlank(attrValue)) {
				flowMapper.put("Connector Type - A end", attrValue);
				// serviceDetail.setSiteEndInterface(attrValue);
			}else if (attrName.equals("Connector Type - B end") && StringUtils.isNotBlank(attrValue)) {
				flowMapper.put("Connector Type - B end", attrValue);
				// serviceDetail.setSiteEndInterface(attrValue);
			} else {
				OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
						.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
								category);
				if (odrServiceAttribute == null) {
					odrServiceAttribute = new OdrServiceAttribute();
				}
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
			}

		}
		if (!isUpdate)
			processLinkFeasibility(link, serviceDetail, type, userName,flowMapper);
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
		}
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

	@SuppressWarnings("unchecked")
	private OdrServiceDetail processLocalItContact(String refId, OdrServiceDetail serviceDetail, String type,
			OdrComponent odrComponent, List<OdrComponentAttribute> odrComponentAttributes)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Starting to save the localItContact flow with refId {} refname {}", refId, OdrConstants.NPL_LINK);
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, type, OdrConstants.NPL_SITES);
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
					odrComponentAttributes
							.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactEmailId",
									localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null,
									serviceDetail.getCreatedBy()));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
							"localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null,
							serviceDetail.getCreatedBy()));
					odrComponentAttributes
							.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactName",
									localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null,
									serviceDetail.getCreatedBy()));
					LOGGER.info("Local IT Contact Received {}", localItDetail);
				}
			} else if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstNumber",
						attrValue, serviceDetail.getCreatedBy()));
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("GST_ADDRESS");
					odrAdditionalServiceParam.setCategory(null);
					odrAdditionalServiceParam.setCreatedBy(serviceDetail.getCreatedBy());
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(addServiceParam.get().getValue());
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
							.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, serviceDetail.getId(),
									category);
					if (odrServiceAttribute == null) {
						odrServiceAttribute = new OdrServiceAttribute();
					}
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(serviceDetail.getCreatedBy());
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(serviceDetail.getCreatedBy());
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
//					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstAddress",
//							addServiceParam.get().getValue(), serviceDetail.getCreatedBy()));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstAddressId",
							attrValue, serviceDetail.getCreatedBy()));
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, attrName,
						CommonConstants.Y, serviceDetail.getCreatedBy()));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "taxExemption",
						CommonConstants.Y, serviceDetail.getCreatedBy()));
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
	public void processLinkFeasibility(OrderNplLink orderNplLink, OdrServiceDetail serviceDetail, String type,
			String userName,Map<String, String> flowMapper) throws ParseException {
		LOGGER.info("Inside Gde odr service processLinkFeasibility");
		List<OrderLinkFeasibility> orderLinkFeasibilities = orderLinkFeasibilityRepository
				.findByOrderNplLinkAndIsSelected(orderNplLink, CommonConstants.BACTIVE);
		for (OrderLinkFeasibility orderLinkFeasibility : orderLinkFeasibilities) {
			LOGGER.info("Inside Gde odr service processLinkFeasibility after getting link feasibility data");
			serviceDetail.setAccessType(orderLinkFeasibility.getFeasibilityMode());
			serviceDetail.setLastmileType(orderLinkFeasibility.getFeasibilityMode());
			serviceDetail.setFeasibilityId(orderLinkFeasibility.getFeasibilityCode());
			flowMapper.put("feasibilityId", orderLinkFeasibility.getFeasibilityCode());
			serviceDetail.setLastmileProvider(orderLinkFeasibility.getProvider());
			flowMapper.put("lastMileProvider", orderLinkFeasibility.getProvider());
			flowMapper.put("serviceSubType", orderLinkFeasibility.getType());
		}
	}

	public void processLink(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrComponent> odrComponents,
			List<OdrComponentAttribute> odrComponentAttributes,List<OdrAttachment> odrAttachments) {
		// TODO
		
		LOGGER.info("Inside GDE ODR service processLink ");
		List<OrderNplLink> links = orderNplLinkRepository.findByProductSolutionIdAndStatus(orderProductSolution.getId(),
				CommonConstants.BACTIVE);
		for (OrderNplLink link : links) {
			try {
				OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();
				primaryserviceDetail.setFlowStatus("NEW");
				primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
				primaryserviceDetail.setServiceRefId(link.getLinkCode());
				primaryserviceDetail.setOdrContractInfo(odrContractInfo);
				primaryserviceDetail.setOdrOrder(odrOrder);
				primaryserviceDetail.setServiceCommissionedDate(
						link.getRequestorDate() != null ? link.getRequestorDate() : link.getEffectiveDate());
				primaryserviceDetail.setArc(link.getArc());
				primaryserviceDetail.setMrc(link.getMrc());
				primaryserviceDetail.setNrc(link.getNrc());
				primaryserviceDetail.setErfPrdCatalogOfferingName(flowMapper.get("offeringName"));
				primaryserviceDetail.setCreatedBy(username);
				primaryserviceDetail.setCreatedDate(new Date());
				primaryserviceDetail.setUpdatedDate(new Date());
				primaryserviceDetail.setUpdatedBy(username);
				primaryserviceDetail.setIsActive(CommonConstants.Y);
				primaryserviceDetail.setIsIzo(CommonConstants.N);
				primaryserviceDetail.setErfPrdCatalogParentProductName(flowMapper.get("productName"));
				primaryserviceDetail.setErfPrdCatalogProductName(flowMapper.get("productName"));
				
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(flowMapper.get("productName"), CommonConstants.BACTIVE);
				if (Objects.nonNull(mstProductFamily)) {
					primaryserviceDetail.setErfPrdCatalogProductId(mstProductFamily.getId());
				}
				if(("MACD".equals(odrOrder.getOrderType()) && "ADD_SITE".equals(odrOrder.getOrderCategory()))
						|| ("MACD".equals(odrOrder.getOrderType()) && Objects.nonNull(primaryserviceDetail.getOrderSubCategory()) 
								&& primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))){
					LOGGER.info("Parent UUID {}",flowMapper.get(link.getLinkCode()));
					LOGGER.info("Order Sub Category {}",primaryserviceDetail.getOrderSubCategory());
					primaryserviceDetail.setParentUuid(flowMapper.get(link.getLinkCode()));
				}else if("MACD".equals(odrOrder.getOrderType()) && !"ADD_SITE".equals(odrOrder.getOrderCategory())
						&& (Objects.isNull(primaryserviceDetail.getOrderSubCategory()) || (Objects.nonNull(primaryserviceDetail.getOrderSubCategory()) && !primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))){
					LOGGER.info("UUID {}",link.getLinkCode());
					primaryserviceDetail.setUuid(flowMapper.get(link.getLinkCode()));
				}
				odrServiceDetails.add(primaryserviceDetail);
				processCommonComponentAttr(String.valueOf(link.getId()), primaryserviceDetail, username, flowMapper);
				processProductComponentAttr(String.valueOf(link.getId()), primaryserviceDetail, "Link", username, false,
						odrServiceCommercials, flowMapper, link);
//				No SLA available for GDE
//				processLinkSla(link, primaryserviceDetail, username);
				processSite(flowMapper, username, link, primaryserviceDetail, odrComponents, odrComponentAttributes,odrAttachments);
			} catch (Exception e) {
				LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
			}

		}

	}

	/**
	 * Method to processOrder
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
		LOGGER.info("Inside Npl odr service processOrderToLe");
		if (flag) {
			odrOrder.setErfCustLeId(orderToLe.getErfCusCustomerLegalEntityId());
			odrOrder.setOrderCategory(orderToLe.getOrderCategory());
			if ("MACD".equals(orderToLe.getOrderType())) {
				odrOrder.setOrderType(orderToLe.getOrderType());
			} else {
				odrOrder.setOrderType(orderToLe.getOrderCategory() == null ? "NEW" : orderToLe.getOrderCategory());
			}
			
//			Commenting as GDE don't have SFDC as of now
//			if ("MACD".equals(orderToLe.getOrderType())) {
//				List<OrderIllSiteToService> orderIllSiteToServices=orderIllSiteToServiceRepository.findByOrderToLe_Id(orderToLe.getId());
//				if(Objects.nonNull(orderIllSiteToServices) && !orderIllSiteToServices.isEmpty()){
//					Optional<OrderIllSiteToService> orderIllSiteToServiceOptional=orderIllSiteToServices.stream().findFirst();
//					if(orderIllSiteToServiceOptional.isPresent()){
//						OrderIllSiteToService orderIllSiteToService=orderIllSiteToServiceOptional.get();
//						if(Objects.nonNull(orderIllSiteToService.getErfSfdcSubType())){
//							LOGGER.info("Update Sub Category for MACD::",orderIllSiteToService.getErfSfdcSubType());
//							odrOrder.setOrderSubCategory(orderIllSiteToService.getErfSfdcSubType());
//						}
//					}
//				}
//			}
			odrOrder.setErfOrderLeId(orderToLe.getId());
			odrOrder.setOrderSource(OdrConstants.OPTIMUS);
			odrOrder.setErfCustSpLeId(orderToLe.getErfCusSpLegalEntityId());
			odrOrder.setIsBundleOrder(orderToLe.getIsBundle() != null ? String.valueOf(orderToLe.getIsBundle()) : null);
			odrOrder.setOpportunityClassification(OdrConstants.SELL_TO);
			odrOrder.setTpsSfdcOptyId(orderToLe.getTpsSfdcCopfId());
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
		LOGGER.info("Inside GDE Odr Service processOrderToLeAttr");
		odrContractInfo.setPoMandatoryStatus(CommonConstants.N);
		for (Map<String, Object> ordersLeAttributeValue : orderLeAttr) {
			String attrName = (String) ordersLeAttributeValue.get("name");
			String attrValue = (String) ordersLeAttributeValue.get("attribute_value");
			// odrOrder.setCustomerSegment(customerSegment);
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
			}else if (attrName.equals(LeAttributesConstants.LE_STATE_GST_NO)) {
				getContractGstAddress(odrOrder,attrValue);
				addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName,attrValue);
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
								List<OrderNplLink> orderNplLinks = orderNplLinkRepository
										.findByProductSolutionId(orderProductSolution.getId());
								for (OrderNplLink orderNplLink : orderNplLinks) {
									processOrderEnrichmentForSites(username, orderNplLink, flowMapper);
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
		List<OrderNplLink> nplLinks = orderNplLinkRepository.findByLinkCodeAndStatus(odrServiceDetail.getServiceRefId(),
				CommonConstants.BACTIVE);
		for (OrderNplLink orderNplLink : nplLinks) {
			Map<String, String> flowMapper = getOrderStatus(orderNplLink);
			if (flowMapper != null) {
				LOGGER.info("Order Enrichment is completed so proceeding the with the oe");
				processOeLink(username, orderNplLink, flowMapper, odrServiceDetail);
			} else {
				LOGGER.info("Order Enrichment is not yet completed so just updating the New Order to COMPLETED");
				odrServiceDetailRepository.save(odrServiceDetail);
			}
		}

		return status;
	}

	private Map<String, String> getOrderStatus(OrderNplLink orderNplLink) {
		Map<String, String> flowMapper = new HashMap<>();
		Optional<OrderProductSolution> prodSolutionOpt = orderProductSolutionRepository
				.findById(orderNplLink.getProductSolutionId());
		if (prodSolutionOpt.isPresent()) {
			flowMapper.put("offeringName", prodSolutionOpt.get().getMstProductOffering().getProductName());
			OrderToLeProductFamily orderFamily = prodSolutionOpt.get().getOrderToLeProductFamily();
			flowMapper.put("productName", orderFamily.getMstProductFamily().getName());
			flowMapper.put("productCode", orderFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
			Order order = orderFamily.getOrderToLe().getOrder();
			if (order.getStage().equals("ORDER_COMPLETED")) {
				return flowMapper;
			}
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
	public void processOrderEnrichmentForSites(String username, OrderNplLink orderNplLink,
			Map<String, String> flowMapper) throws TclCommonException, ParseException {
		Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository
				.findByServiceRefId(orderNplLink.getLinkCode());
		if (!odrServiceDetails.isEmpty()) {
			processOeLink(username, orderNplLink, flowMapper, odrServiceDetails.stream().findFirst().get());
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

	@SuppressWarnings("unchecked")
	public OdrServiceDetail processProductComponentAttrForSite(Integer refId, OdrServiceDetail serviceDetail,
			String type, String userName, boolean isUpdate, Map<String, String> flowMapper,
			List<OdrComponent> odrComponents, List<OdrComponentAttribute> odrComponentAttributes, Integer locationId,
			Integer popLocationId, String popSiteCode, OrderNplLink orderNplLink,List<OdrAttachment> odrAttachments)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Inside GDE odr service processProductComponentAttrForSite");
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(String.valueOf(refId), type, OdrConstants.GDE_SITES);
		if (oderProdCompAttrs.isEmpty()) {
			return null;
		}
		String siteType = "A";
		if (type.equals("Site-B")) {
			siteType = "B";
		}
		OdrComponent odrComponent = new OdrComponent();
		odrComponent.setComponentName("LM");
		odrComponent.setCreatedBy(userName);
		odrComponent.setCreatedDate(new Date());
		odrComponent.setIsActive("Y");
		odrComponent.setUpdatedBy(userName);
		odrComponent.setSiteType(siteType);
		odrComponent.setUpdatedDate(new Date());
		odrComponent.setOdrServiceDetail(serviceDetail);

		odrComponents.add(odrComponent);

		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				updateLocalItContactSite(serviceDetail, odrComponent, attrValue, userName, odrComponents,
						odrComponentAttributes);
			} else if (attrName.equals("Local Loop Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				String[] bw = attrValue.split(" ");
				if (bw != null) {
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
							"localLoopBandwidth", bw[0], userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
							"localLoopBandwidthUnit", "Mbps", userName));
				}
			}else if(attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)){
				if(type.equals("Site-A")) {
					serviceDetail.setBillingGstNumber(attrValue);
				}
				odrComponentAttributes.add(
						constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstNumber", attrValue, userName));
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				LOGGER.info("Site GST Address ID {}",Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					LOGGER.info("Additional Service Param present and the value is {}",addServiceParam.get().getValue());
//					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
//							"siteGstAddress", addServiceParam.get().getValue(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
							"siteGstAddressId", attrValue, userName));
				}
			}else {
				odrComponentAttributes.add(
						constructOdrComponentAttribute(odrComponent, serviceDetail, attrName, attrValue, userName));
			}
		}
		if (flowMapper.containsKey("Interface Type - A end") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "interface",
					flowMapper.get("Interface Type - A end"), userName));
		}
		if (flowMapper.containsKey("Interface Type - B end") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "interface",
					flowMapper.get("Interface Type - B end"), userName));
		}
		if (flowMapper.containsKey("Connector Type - A end") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "connectorType",
					flowMapper.get("Interface Type - B end"), userName));
		}
		if (flowMapper.containsKey("Connector Type - B end") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "connectorType",
					flowMapper.get("Interface Type - B end"), userName));
		}
		if (flowMapper.containsKey("Port Bandwidth")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "portBandwidth",
					flowMapper.get("Port Bandwidth"), userName));
			odrComponentAttributes
					.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "bwUnit", "Mbps", userName));
		}
		if (flowMapper.containsKey("feasibilityId")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "feasibilityId",
					flowMapper.get("feasibilityId"), userName));
		}
//		if (flowMapper.containsKey("lmTypeA") && type.equals("Site-A")) {
//			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmType",
//					flowMapper.get("lmTypeA"), userName));
//			serviceDetail.setLastmileType(flowMapper.get("lmTypeA"));
//			serviceDetail.setAccessType(flowMapper.get("lmTypeA"));
//		}
//		if (flowMapper.containsKey("lmTypeB") && type.equals("Site-B")) {
//			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmType",
//					flowMapper.get("lmTypeB"), userName));
//		}
		
		if (flowMapper.containsKey("popSiteCodeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "popSiteCode",
					flowMapper.get("popSiteCodeA"), userName));
		}
		if (flowMapper.containsKey("popSiteCodeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "popSiteCode",
					flowMapper.get("popSiteCodeB"), userName));
		}
		
		if (flowMapper.containsKey("lastMileProvider")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileProvider",
					flowMapper.get("lastMileProvider"), userName));
		}
//		if (flowMapper.containsKey("lmScenarioTypeA") && type.equals("Site-A")) {
//			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileScenario",
//					flowMapper.get("lmScenarioTypeA"), userName));
//			
//		}
//		if (flowMapper.containsKey("lmConnectionTypeA") && type.equals("Site-A")) {
//			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmConnectionType",
//					flowMapper.get("lmConnectionTypeA"), userName));
//		}
//		if (flowMapper.containsKey("lmScenarioTypeB") && type.equals("Site-B")) {
//			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileScenario",
//					flowMapper.get("lmScenarioTypeB"), userName));
//		}
//		if (flowMapper.containsKey("lmConnectionTypeB") && type.equals("Site-B")) {
//			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmConnectionType",
//					flowMapper.get("lmConnectionTypeB"), userName));
//		}
		if (flowMapper.containsKey("serviceSubType")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "serviceSubType",
					flowMapper.get("serviceSubType"), userName));
		}
		
		odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "linkCode",
				orderNplLink.getLinkCode(), userName));
		persistAddressDetailsOfTheSite(locationId, odrComponent, odrComponentAttributes, serviceDetail, userName,
				siteType);
		persistAddressDetailsOfThePopLocationOfTheSite(popLocationId, odrComponent, odrComponentAttributes,
				serviceDetail, userName, popSiteCode);
		LOGGER.info("Before getting demar details for {}",type);
		persistSiteDemarcationDetailsInOrderFrost(userName, orderNplLink, flowMapper, serviceDetail, locationId,
				refId, type, odrComponent, odrComponentAttributes,odrAttachments);
		return serviceDetail;
	}

	@SuppressWarnings("unchecked")
	private void updateLocalItContactSite(OdrServiceDetail serviceDetail, OdrComponent odrComponent, String attrValue,
			String userName, List<OdrComponent> odrComponents, List<OdrComponentAttribute> odrComponentAttributes) throws TclCommonException {

		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
		Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse, Map.class);
		if (localItDetail != null) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactEmailId",
					localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null, userName));
			serviceDetail.setLocalItContactEmail(localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactMobile",
					localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null,
					userName));
			serviceDetail.setLocalItContactMobile(localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactName",
					localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null, userName));
			serviceDetail.setLocalItContactName(localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
			LOGGER.info("Local IT Contact Received {}", localItDetail);
		}

	}

	private void persistAddressDetailsOfTheSite(Integer locationId, OdrComponent odrComponent,
			List<OdrComponentAttribute> odrComponentAttributes, OdrServiceDetail odrServiceDetail, String userName,String siteType) throws TclCommonException {
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(locationId));
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
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"siteAddress", addr, userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"latLong", addressDetail.getLatLong(), userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"destinationCountry", addressDetail.getCountry(), userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"destinationCity", addressDetail.getCity(), userName));
				if(siteType!=null && siteType.equals("A")) {
					odrServiceDetail.setDestinationCity(addressDetail.getCity());
				}
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"destinationAddressLineOne", addressDetail.getAddressLineOne(), userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"destinationAddressLineTwo", addressDetail.getAddressLineTwo(), userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"destinationLocality", addressDetail.getLocality(), userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"destinationState", addressDetail.getState(), userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
						"destinationPincode", addressDetail.getPincode(), userName));

			}
		}
	}

	private void persistAddressDetailsOfThePopLocationOfTheSite(Integer locationId, OdrComponent odrComponent,
			List<OdrComponentAttribute> odrComponentAttributes, OdrServiceDetail odrServiceDetail, String userName,String popSiteCode) throws TclCommonException {
		if (locationId != null) {
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(locationId));
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
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"popSiteAddress", addr, userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"popSiteCode", popSiteCode, userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"popLatLang", addressDetail.getLatLong(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"sourceCountry", addressDetail.getCountry(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"sourceCity", addressDetail.getCity(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"sourceAddressLineOne", addressDetail.getAddressLineOne(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"sourceAddressLineTwo", addressDetail.getAddressLineTwo(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"sourceLocality", addressDetail.getLocality(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"sourceState", addressDetail.getState(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"sourcePincode", addressDetail.getPincode(), userName));

				}
			}
		}
	}

	private OdrComponentAttribute constructOdrComponentAttribute(OdrComponent odrComponent,
			OdrServiceDetail odrServiceDetail, String name, String value, String userName) {
		OdrComponentAttribute odrComponentAttribute = new OdrComponentAttribute();
		odrComponentAttribute.setAttributeName(name);
		odrComponentAttribute.setAttributeValue(value);
		odrComponentAttribute.setCreatedBy(userName);
		odrComponentAttribute.setCreatedDate(new Date());
		odrComponentAttribute.setIsActive("Y");
		odrComponentAttribute.setOdrComponent(odrComponent);
		odrComponentAttribute.setOdrServiceDetail(odrServiceDetail);
		odrComponentAttribute.setUpdatedBy(userName);
		odrComponentAttribute.setUpdatedDate(new Date());
		return odrComponentAttribute;

	}

	private void processOeLink(String username, OrderNplLink orderNplLink, Map<String, String> flowMapper,
			OdrServiceDetail odrServiceDetail) throws TclCommonException, ParseException {
		if (orderNplLink.getSiteAId() != null) {
			OrderIllSite orderIllSite = orderIllSiteRepository.findByIdAndStatus(orderNplLink.getSiteAId(),
					CommonConstants.BACTIVE);
			persistSiteDemarcationDetails(username, orderNplLink, flowMapper, odrServiceDetail,
					orderIllSite.getErfLocSitebLocationId(), orderIllSite.getId(), "Site-A");
		}
		if (orderNplLink.getSiteBId() != null) {
			OrderIllSite orderIllSite = orderIllSiteRepository.findByIdAndStatus(orderNplLink.getSiteBId(),
					CommonConstants.BACTIVE);
			persistSiteDemarcationDetails(username, orderNplLink, flowMapper, odrServiceDetail,
					orderIllSite.getErfLocSitebLocationId(), orderIllSite.getId(), "Site-B");
		}

	}

	private void persistSiteDemarcationDetails(String username, OrderNplLink orderNplLink,
			Map<String, String> flowMapper, OdrServiceDetail odrServiceDetail, Integer locationId, Integer siteId,
			String type) throws TclCommonException, IllegalArgumentException, ParseException {
		String siteType = "A";
		if (type.equals("Site-B")) {
			siteType = "B";
		}
		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String demarReponse = (String) mqUtils.sendAndReceive(demarcationQueue, String.valueOf(locationId));
		if (StringUtils.isNotBlank(demarReponse)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> demarDetails = (Map<String, Object>) Utils.convertJsonToObject(demarReponse, Map.class);
			if (demarDetails != null) {
				Set<OdrServiceDetailBean> odrServiceBeanDetails = new HashSet<>();
				if (!odrServiceDetail.getFlowStatus().equals("NEW")) {
					List<OdrComponentAttribute> odrComponentAttributes = new ArrayList<>();
					OdrComponent odrComponent = new OdrComponent();
					List<OdrComponent> odrComponents = odrComponentRepository
							.findByOdrServiceDetailAndComponentNameAndSiteType(odrServiceDetail, "LM", siteType);
					if (odrComponents != null && !odrComponents.isEmpty()) {
						odrComponent = odrComponents.get(0);
					} else {
						odrComponent.setComponentName("LM");
						odrComponent.setCreatedBy(odrServiceDetail.getCreatedBy());
						odrComponent.setCreatedDate(new Date());
						odrComponent.setIsActive("Y");
						odrComponent.setSiteType(type);
						odrComponent.setUpdatedBy(odrServiceDetail.getCreatedBy());
						odrComponent.setSiteType(siteType);
						odrComponent.setUpdatedDate(new Date());
						odrComponent.setOdrServiceDetail(odrServiceDetail);
						odrComponent = odrComponentRepository.saveAndFlush(odrComponent);
					}
					odrComponentAttributes
							.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "demarcationBuildingName",
									demarDetails.get("BUILDING_NAME") != null
											? (String) demarDetails.get("BUILDING_NAME")
											: null,
									odrServiceDetail.getCreatedBy()));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "demarcationFloor",
							demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null,
							odrServiceDetail.getCreatedBy()));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "demarcationWing",
							demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null,
							odrServiceDetail.getCreatedBy()));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "demarcationRoom",
							demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null,
							odrServiceDetail.getCreatedBy()));

					processLocalItContact(String.valueOf(siteId), odrServiceDetail, type, odrComponent,
							odrComponentAttributes);
					odrComponentAttributeRepository.saveAll(odrComponentAttributes);
					List<OmsAttachment> omsAttachments = omsAttachmentRepository
							.findByReferenceNameAndReferenceId("Site", siteId);
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
					odrServiceDetailRepository.save(odrServiceDetail);
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
					List<OdrComponent> odrComponentsList = odrComponentRepository.findByOdrServiceDetail(odrServiceDetail);
					if(odrComponentsList!=null && !odrComponentsList.isEmpty()) {
						Set<OdrComponentBean> odrComponentBeans = new HashSet<>();
						odrComponentsList.stream().forEach(comp->{
							odrComponentBeans.add(mapOdrComponentEntityToBean(comp));
						});
						odrServiceDetailBean.setOdrComponentBeans(odrComponentBeans);
					}
					odrServiceBeanDetails.add(odrServiceDetailBean);
					
					
					LOGGER.info("Updated the service Detail for {}", odrServiceDetail.getId());

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

	}
	
	private void persistSiteDemarcationDetailsInOrderFrost(String username, OrderNplLink orderNplLink,
			Map<String, String> flowMapper, OdrServiceDetail odrServiceDetail, Integer locationId, Integer siteId,
			String type, OdrComponent odrComponent, List<OdrComponentAttribute> odrComponentAttributes,List<OdrAttachment> odrAttachments)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Inside persistSiteDemarcationDetailsInOrderFrost location id {}",locationId);
		
		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails inside persistSiteDemarcationDetailsInOrderFrost {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String demarReponse = (String) mqUtils.sendAndReceive(demarcationQueue, String.valueOf(locationId));
		if (StringUtils.isNotBlank(demarReponse)) {
			LOGGER.info("Got the response from demarcation queue call {}",demarReponse);
			@SuppressWarnings("unchecked")
			Map<String, Object> demarDetails = (Map<String, Object>) Utils.convertJsonToObject(demarReponse, Map.class);
			if (demarDetails != null) {
				LOGGER.info("Demarcation details from the queue call {}",demarDetails);
				if (demarDetails.containsKey("BUILDING_NAME") && demarDetails.get("BUILDING_NAME")!=null && StringUtils.isNotBlank(demarDetails.get("BUILDING_NAME").toString())) {
					odrComponentAttributes.add(
							constructOdrComponentAttribute(odrComponent, odrServiceDetail, "demarcationBuildingName",
									demarDetails.get("BUILDING_NAME").toString(), odrServiceDetail.getCreatedBy()));
				}
				
				if (demarDetails.containsKey("FLOOR") && demarDetails.get("FLOOR")!=null && StringUtils.isNotBlank(demarDetails.get("FLOOR").toString())) {
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"demarcationFloor", demarDetails.get("FLOOR").toString(), odrServiceDetail.getCreatedBy()));
				}
				if (demarDetails.containsKey("WING") && demarDetails.get("WING")!=null && StringUtils.isNotBlank(demarDetails.get("WING").toString())) {
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"demarcationWing", demarDetails.get("WING").toString(), odrServiceDetail.getCreatedBy()));
				}
				if (demarDetails.containsKey("ROOM") && demarDetails.get("ROOM")!=null && StringUtils.isNotBlank(demarDetails.get("ROOM").toString())) {
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"demarcationRoom", demarDetails.get("ROOM").toString(), odrServiceDetail.getCreatedBy()));
				}

				List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByReferenceNameAndReferenceId("Site",
						siteId);
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
							OdrAttachment odrAttachment = new OdrAttachment();

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
							odrAttachments.add(odrAttachment);
						}

					}

				}

			}
		}

	}
	public OdrComponentBean mapOdrComponentEntityToBean(OdrComponent odrComponent) {
		OdrComponentBean odrComponentBean = new OdrComponentBean();
		odrComponentBean.setComponentName(odrComponent.getComponentName());
		odrComponentBean.setSiteType(odrComponent.getSiteType());
		Set<OdrComponentAttributeBean> odrComponentAttributesBeans = new HashSet<>();
		List<OdrComponentAttribute> odrComponentAttributes = odrComponentAttributeRepository.findByOdrComponent(odrComponent);
		if(odrComponentAttributes!=null && !odrComponentAttributes.isEmpty()) {
			odrComponentAttributes.stream().forEach(odrComponentAttr->{
				odrComponentAttributesBeans.add(mapOdrComponentAttributeToBen(odrComponentAttr));
			});
			
		}
		odrComponentBean.setOdrComponentAttributeBeans(odrComponentAttributesBeans);
		return odrComponentBean;
	}
	
	public OdrComponentAttributeBean mapOdrComponentAttributeToBen(OdrComponentAttribute odrComponentAttribute) {
		OdrComponentAttributeBean odrComponentAttributeBean = new OdrComponentAttributeBean();
		odrComponentAttributeBean.setId(odrComponentAttribute.getId());
		odrComponentAttributeBean.setName(odrComponentAttribute.getAttributeName());
		odrComponentAttributeBean.setValue(odrComponentAttribute.getAttributeValue());
		return odrComponentAttributeBean;
	}
	
	private OdrOrder getContractGstAddress(OdrOrder odrOrder, String gstNo) {
		LOGGER.error("inside contract level gst:: {} for legal id:: {}",gstNo,odrOrder.getErfCustLeId());
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
}
