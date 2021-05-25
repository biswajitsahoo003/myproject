package com.tcl.dias.batch.odr.npl;

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

import com.tcl.dias.batch.odr.npl.bean.NplCrossConnectPricingBean;
import com.tcl.dias.common.beans.CrossConnectDemarcations;
import com.tcl.dias.common.beans.CrossConnectLocalITContacts;
import com.tcl.dias.common.beans.CrossConnectLocalITDemarcationBean;
import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;

import org.apache.commons.lang.time.DateUtils;
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
import com.tcl.dias.common.constants.AttachmentTypeConstants;
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
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.utils.DateUtil;
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
import com.tcl.dias.oms.entity.entities.OrderProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.OrderSiteAddress;
import com.tcl.dias.oms.entity.entities.OrderSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.OrderSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
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
import com.tcl.dias.oms.entity.repository.OrderProductSolutionSiLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteBillingDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class NplOdrService extends OrderService {
	
	/**
	 * 
	 * getReferenceName
	 * 
	 * @return
	 */
	protected String getReferenceName() {
		return OdrConstants.NPL_LINK;

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(NplOdrService.class);

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

	@Value("${rabbitmq.crossconnect.localit.demarcation.detail}")
	String getCrossConnectLocalITandDemarcation;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	OdrMapper odrMapper;

	@Autowired
	OdrNplMapper odrNplMapper;

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

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;
	
	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	

	@Autowired
	OrderSiteServiceTerminationDetailsRepository orderSiteServiceTerminationDetailsRepository;
	
	@Value("${rabbitmq.si.npl.details.inactive.queue}")
	String siNplServiceDetailsInactiveQueue;
	
	@Autowired
	OrderSiteBillingDetailsRepository orderSiteBillingInfoRepository;

	@Autowired
	OrderProductSolutionSiLinkRepository orderProductSolutionSiLinkRepository;

	/**
	 * 
	 * processOrderFrost - This method is used to freeze the order by transforming
	 * the order to a new table odr
	 * 
	 * @param orderId
	 * @return -Boolean - Status whether the process is success or failure
	 */
	public synchronized Boolean processOrderFrost(Integer orderId, String username) {
		Utils.logMemory();
		LOGGER.info("Inside Npl Odr Service Process starting to freeze the order with id {}", orderId);
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
				List<OdrComponent> odrComponents = new ArrayList<>();
				List<OdrComponentAttribute> odrComponentAttributes = new ArrayList<>();
				List<OdrAttachment> odrAttachments = new ArrayList<>();
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
							odrServiceCommercials, odrAttributes, odrComponents, odrComponentAttributes,odrAttachments);
				}
				odrOrderRepository.save(odrOrder);
				odrOrderAttributeRepository.saveAll(odrAttributes);
				odrContractInfoRepository.saveAll(odrContractInfos);
				odrServiceDetailRepository.saveAll(odrServiceDetails);
				odrServiceCommercialRepository.saveAll(odrServiceCommercials);
				odrComponents = odrComponentRepository.saveAll(odrComponents);
				odrComponentAttributes = odrComponentAttributeRepository.saveAll(odrComponentAttributes);
				if(odrAttachments!=null && !odrAttachments.isEmpty()) {
					odrAttachmentRepository.saveAll(odrAttachments);
				}
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

						Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);

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
							// odrAttachment.setOfferingName(flowMapper.get("offeringName"));
							odrAttachment.setProductName(odrOrder.getOpOrderCode().substring(0, 3));
							odrAttachment.setOdrServiceDetail(odrServiceDetail);
							odrServiceDetailList.add(odrAttachment);

						}
						odrAttachmentRepository.saveAll(odrServiceDetailList);
					}
				}
				OdrOrderBean odrOrderBean = odrNplMapper.mapOrderEntityToBean(odrOrder, odrContractInfos, odrAttributes,
						odrServiceDetails, odrServiceCommercials, odrComponents, odrComponentAttributes);
				LOGGER.info("order froze completed");
				odrOrderBean.setStage(stage);
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
			LOGGER.info(" order product solution id {}, product Solution {}",orderProductSolution.getId(),  orderProductSolution.getMstProductOffering().getProductName());
			if (CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(orderProductSolution.getMstProductOffering().getProductName())) {
				LOGGER.info("Inside Npl CrossConnect service processProductSolution {} :",orderProductSolution.getMstProductOffering().getProductName());
				flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
				processCrossConnectSite(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo,
						odrOrder, username, odrServiceCommercials, odrComponents, odrComponentAttributes, odrAttachments);
			} else {
				flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
				if ("MACD".equals(orderToLe.getOrderType()) || "CANCELLATION".equalsIgnoreCase(orderToLe.getOrderType()) || "TERMINATION".equalsIgnoreCase(orderToLe.getOrderType())) {
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
						flowMapper.put(String.valueOf(orderIllSiteToService.getOrderNplLink().getId()), orderIllSiteToService.getErfSfdcSubType());
					}
				}
				processLink(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username,
						odrServiceCommercials, odrComponents, odrComponentAttributes, odrAttachments);
			}

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
		LOGGER.info("Inside Npl odr service processSite");
		// TODO
		try {
			OrderIllSite orderIllSiteA = orderIllSiteRepository.findByIdAndStatus(orderNplLink.getSiteAId(),
					CommonConstants.BACTIVE);
			OrderIllSite orderIllSiteB = orderIllSiteRepository.findByIdAndStatus(orderNplLink.getSiteBId(),
					CommonConstants.BACTIVE);
			processProductComponentAttrForSite(orderNplLink.getSiteAId(), primaryserviceDetail,
					"Site-A", username, false, flowMapper, odrComponents, odrComponentAttributes,
					orderIllSiteA.getErfLocSitebLocationId(), orderIllSiteA.getErfLocSiteaLocationId(),
					orderIllSiteA.getErfLocSiteaSiteCode(),orderNplLink,odrAttachments,orderIllSiteA);
			processProductComponentAttrForSite(orderNplLink.getSiteBId(), primaryserviceDetail,
					"Site-B", username, false, flowMapper, odrComponents, odrComponentAttributes,
					orderIllSiteB.getErfLocSitebLocationId(), orderIllSiteB.getErfLocSiteaLocationId(),
					orderIllSiteB.getErfLocSiteaSiteCode(),orderNplLink,odrAttachments,orderIllSiteB);
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
		LOGGER.info("Inside Npl ODR service processCommonComponentAttr ");
		LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference Id {}", refId,
				OdrConstants.NPL_LINK);
		Utils.logMemory();
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId,"Link",OdrConstants.NPL_LINK );
		Utils.logMemory();
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");
			Utils.logMemory();
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
		LOGGER.info("Inside Npl odr service processProductComponentAttr");
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndTypeAndReferenceName(Integer.valueOf(refId), type,OdrConstants.NPL_LINK);
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
				.findByRefidAndRefName(refId, type, OdrConstants.NPL_LINK);
		if (oderProdCompAttrs.isEmpty()) {
			return null;
		}
		serviceDetail.setPrimarySecondary("Single");
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
	@SuppressWarnings({ "rawtypes" })
	public void processLinkFeasibility(OrderNplLink orderNplLink, OdrServiceDetail serviceDetail, String type,
			String userName,Map<String, String> flowMapper) throws ParseException {
		LOGGER.info("Inside npl odr service processLinkFeasibility");
		List<OrderLinkFeasibility> orderLinkFeasibilities = orderLinkFeasibilityRepository
				.findByOrderNplLinkAndIsSelected(orderNplLink, CommonConstants.BACTIVE);
		for (OrderLinkFeasibility orderLinkFeasibility : orderLinkFeasibilities) {
			LOGGER.info("Inside npl odr service processLinkFeasibility after getting link feasibility data");
			serviceDetail.setAccessType(orderLinkFeasibility.getFeasibilityMode());
			serviceDetail.setLastmileType(orderLinkFeasibility.getFeasibilityMode());
			serviceDetail.setFeasibilityId(orderLinkFeasibility.getFeasibilityCode());
			flowMapper.put("feasibilityId", orderLinkFeasibility.getFeasibilityCode());
			serviceDetail.setLastmileProvider(orderLinkFeasibility.getProvider());
			flowMapper.put("lastMileProvider", orderLinkFeasibility.getProvider());
			flowMapper.put("serviceSubType", orderLinkFeasibility.getType());
			if (orderLinkFeasibility.getResponseJson() != null) {
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(orderLinkFeasibility.getResponseJson());
				Iterator<?> iter = jsonObject.entrySet().iterator();
				String lastMileTypeA = "";
				String popIDA = "";
				String popIDB = "";
				String lastMileConnectionA = "";
				String lastMileTypeB = "";
				String lastMileConnectionB = "";
				String connectedCustomerA = "";
				String connectedBuildingA = "";
				String connectedCustomerB = "";
				String connectedBuildingB = "";
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String attrKey = String.valueOf(entry.getKey());

					if ((attrKey.equalsIgnoreCase("a_access_rings")) || (attrKey.equalsIgnoreCase("a_mux_details"))||
							(attrKey.equalsIgnoreCase("b_access_rings")) || (attrKey.equalsIgnoreCase("b_mux_details") 
									|| "feasibility_remarks".equalsIgnoreCase(attrKey) || "a_feasibility_remarks".equalsIgnoreCase(attrKey) || "b_feasibility_remarks".equalsIgnoreCase(attrKey))) {
						LOGGER.info("key={}", attrKey);
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
						if ((attrKey.equalsIgnoreCase("a_hh_name") || attrKey.equalsIgnoreCase("a_min_hh_fatg")
								|| attrKey.equalsIgnoreCase("a_lm_nrc_inbldg_onwl")
								|| attrKey.equalsIgnoreCase("a_lm_nrc_ospcapex_onwl")
								|| attrKey.equalsIgnoreCase("Type")
								|| attrKey.equalsIgnoreCase("a_local_loop_interface")
								|| attrKey.equalsIgnoreCase("a_lm_nrc_mux_onwl")
								|| attrKey.equalsIgnoreCase("a_HH_0_5km") || attrKey.equalsIgnoreCase("bw_mbps")
								|| attrKey.equalsIgnoreCase("Type") || attrKey.equalsIgnoreCase("a_connected_cust_tag")
								|| attrKey.equalsIgnoreCase("a_connected_building_tag")
								|| attrKey.equalsIgnoreCase("a_Network_Feasibility_Check")
								|| attrKey.equalsIgnoreCase("b_hh_name") || attrKey.equalsIgnoreCase("b_min_hh_fatg")
								|| attrKey.equalsIgnoreCase("b_lm_nrc_inbldg_onwl")
								|| attrKey.equalsIgnoreCase("b_lm_nrc_ospcapex_onwl")
								|| attrKey.equalsIgnoreCase("b_local_loop_interface")
								|| attrKey.equalsIgnoreCase("b_lm_nrc_mux_onwl")
								|| attrKey.equalsIgnoreCase("b_HH_0_5km")
								|| attrKey.equalsIgnoreCase("b_connected_cust_tag")
								|| attrKey.equalsIgnoreCase("b_connected_building_tag")
								|| attrKey.equalsIgnoreCase("b_Network_Feasibility_Check")
								|| attrKey.equalsIgnoreCase("b_Orch_Connection")
								|| attrKey.equalsIgnoreCase("a_Orch_Connection")
								|| attrKey.equalsIgnoreCase("b_Orch_Category")
								|| attrKey.equalsIgnoreCase("a_Orch_Category")
								|| attrKey.equalsIgnoreCase("b_Orch_LM_Type")
								|| attrKey.equalsIgnoreCase("a_Orch_LM_Type")
								|| attrKey.equalsIgnoreCase("b_pop_network_loc_id")
								|| attrKey.equalsIgnoreCase("a_pop_network_loc_id")
								|| "chargeable_distance".equalsIgnoreCase(attrKey)) && entry.getValue() != null) {

							String attrValue = String.valueOf(entry.getValue());
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
							if (attrKey.equalsIgnoreCase("b_Orch_LM_Type")) {
								lastMileTypeB = attrValue;
							}
							if (attrKey.equalsIgnoreCase("a_Orch_LM_Type")) {
								lastMileTypeA = attrValue;
							}
							if (attrKey.equalsIgnoreCase("a_Orch_Connection")) {
								lastMileConnectionA = attrValue;
							}
							if (attrKey.equalsIgnoreCase("b_Orch_Connection")) {
								lastMileConnectionB = attrValue;
							}
							if (attrKey.equalsIgnoreCase("a_connected_cust_tag")) {
								connectedCustomerA = attrValue;
							}
							if (attrKey.equalsIgnoreCase("a_connected_building_tag")) {
								connectedBuildingA = attrValue;
							}
							if (attrKey.equalsIgnoreCase("b_connected_cust_tag")) {
								connectedCustomerB = attrValue;
							}
							if (attrKey.equalsIgnoreCase("b_connected_building_tag")) {
								connectedBuildingB = attrValue;
							}
							if (attrKey.equalsIgnoreCase("a_pop_network_loc_id")) {
								popIDA = attrValue;
							}
							if (attrKey.equalsIgnoreCase("b_pop_network_loc_id")) {
								popIDB = attrValue;
							}
						}
					}
				}
				// LM Type derivation
				String lmTypeA = lastMileTypeA;
				String lmTypeB = lastMileTypeB;
				if (lastMileConnectionA.contains("Wireline")) {
					lmTypeA = lmTypeA.concat("WL");
				} else {
					lmTypeA = lmTypeA.concat("RF");
				}

				if (lastMileConnectionB.contains("Wireline")) {
					lmTypeB = lmTypeB.concat("WL");
				} else {
					lmTypeB = lmTypeB.concat("RF");
				}
				flowMapper.put("lmTypeA", lmTypeA);
				flowMapper.put("lmTypeB", lmTypeB);
				String lmScenarioTypeA = "";
				String lmConnectionTypeA ="";
				String lmScenarioTypeB = "";
				String lmConnectionTypeB ="";
				if (lmTypeA.toLowerCase().contains("onnetwl")) {
					lmScenarioTypeA = "Onnet Wireline";
					lmConnectionTypeA = "Wireline";
					LOGGER.info("connectedBuilding A={},connectedCustomer A={}", connectedBuildingA,
							connectedCustomerA);
					if (connectedCustomerA.contains("1")) {
						lmScenarioTypeA = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingA.contains("1")) {
						lmScenarioTypeA = "Onnet Wireline - Connected Building";
					} else {
						lmScenarioTypeA = "Onnet Wireline - Near Connect";
					}
				} else if (lmTypeA.toLowerCase().contains("offnetwl")) {
					lmScenarioTypeA = "Offnet Wireline";
					lmConnectionTypeA = "Wireline";
				}else if (lmTypeB.toLowerCase().contains("offnetwl")) {
					lmScenarioTypeB = "Offnet Wireline";
					lmConnectionTypeB = "Wireline";
				}else {
					lmScenarioTypeB = "Onnet Wireline";
					lmConnectionTypeB = "Wireline";
					LOGGER.info("connectedBuilding B={},connectedCustomer B={}", connectedBuildingB,
							connectedCustomerA);
					if (connectedCustomerB.contains("1")) {
						lmScenarioTypeB = "Onnet Wireline - Connected Customer";
					} else if (connectedBuildingB.contains("1")) {
						lmScenarioTypeB = "Onnet Wireline - Connected Building";
					} else {
						lmScenarioTypeB = "Onnet Wireline - Near Connect";
					}
				} 
				flowMapper.put("lmScenarioTypeA", lmScenarioTypeA);
				flowMapper.put("lmConnectionTypeA", lmConnectionTypeA);
				flowMapper.put("lmScenarioTypeB", lmScenarioTypeB);
				flowMapper.put("lmConnectionTypeB", lmConnectionTypeB);
				flowMapper.put("popSiteCodeA", popIDA);
				flowMapper.put("popSiteCodeB", popIDB);
				OdrServiceAttribute OdrServiceAttribute = new OdrServiceAttribute();
				OdrServiceAttribute.setAttributeAltValueLabel(lmTypeA);
				OdrServiceAttribute.setAttributeName("lm_type_a");
				OdrServiceAttribute.setAttributeValue(lmTypeA);
				OdrServiceAttribute.setCategory("FEASIBILITY");
				OdrServiceAttribute.setCreatedBy(userName);
				OdrServiceAttribute.setCreatedDate(new Date());
				OdrServiceAttribute.setIsActive(CommonConstants.Y);
				OdrServiceAttribute.setOdrServiceDetail(serviceDetail);
				OdrServiceAttribute.setUpdatedBy(userName);
				OdrServiceAttribute.setUpdatedDate(new Date());
				serviceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
				OdrServiceAttribute = new OdrServiceAttribute();
				OdrServiceAttribute.setAttributeAltValueLabel(lmTypeB);
				OdrServiceAttribute.setAttributeName("lm_type_b");
				OdrServiceAttribute.setAttributeValue(lmTypeB);
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

	public void processLink(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrComponent> odrComponents,
			List<OdrComponentAttribute> odrComponentAttributes,List<OdrAttachment> odrAttachments) {
		// TODO
		
		LOGGER.info("Inside Npl ODR service processLink ");
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
						link.getEffectiveDate() != null ? link.getEffectiveDate() : link.getRequestorDate());
				primaryserviceDetail.setRrfsDate(link.getRequestorDate());
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
				primaryserviceDetail.setOrderType(CommonConstants.NEW);
				
				String absorbedOrPassedOn = null;
				List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderNplLink_Id(link.getId());
				if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty()) {
					absorbedOrPassedOn = orderIllSiteToServiceList.get(0).getAbsorbedOrPassedOn();
				LOGGER.info("Absorbed or passed on {} order link Id {}",absorbedOrPassedOn, link.getId());
				if(absorbedOrPassedOn != null ) {
					if("Absorbed".equalsIgnoreCase(absorbedOrPassedOn)) {
						primaryserviceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						primaryserviceDetail.setCancellationCharges(0D);
					} else if ("Passed On".equalsIgnoreCase(absorbedOrPassedOn)) {
						primaryserviceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						primaryserviceDetail.setCancellationCharges(link.getNrc());
					}
					LOGGER.info("cancellation charges for order link id {} is {}", link.getId(), primaryserviceDetail.getCancellationCharges());
				}
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "cancellationReason", orderIllSiteToServiceList.get(0).getCancellationReason()));
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "leadToRFSDate", orderIllSiteToServiceList.get(0).getLeadToRFSDate()));
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "effectiveDateOfChange", String.valueOf(orderIllSiteToServiceList.get(0).getEffectiveDateOfChange())));				
				}
				if("TERMINATION".equalsIgnoreCase(odrOrder.getOrderType())) {
					primaryserviceDetail.setUuid(orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
					if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty()) {
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
							List<OrderProductComponent> orderProductComponentList =
									orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(orderSiteServiceTerminationDetails.getOrderIllSiteToService().getOrderNplLink().getId(),"ETC Charges", "NPL_LINK");
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
							primaryserviceDetail.setLocalItContactEmail(orderSiteServiceTerminationDetails.getLocalItContactEmailId());
							primaryserviceDetail.setLocalItContactMobile(orderSiteServiceTerminationDetails.getLocalItContactNumber());
							primaryserviceDetail.setLocalItContactName(orderSiteServiceTerminationDetails.getLocalItContactName());
							List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType( "Link", orderSiteServiceTerminationDetails.getOrderIllSiteToService().getOrderNplLink().getId(), AttachmentTypeConstants.APPRVEMAIL.toString());
							if(omsAttachmentList != null & !omsAttachmentList.isEmpty()) {
								primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "approvalMailAvailable", "Yes"));
							} else {
								primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "approvalMailAvailable", "No"));
							}
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "csmEmail", orderSiteServiceTerminationDetails.getCsmNonCsmEmail()));
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "csmUserName", orderSiteServiceTerminationDetails.getCsmNonCsmName()));
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "terminationSubType", orderSiteServiceTerminationDetails.getTerminationSubtype()));
							primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "terminationSubReason", orderSiteServiceTerminationDetails.getSubReason()));
						}
					});
					}
				}
				
				if("MACD".equals(odrOrder.getOrderType()) && flowMapper.containsKey(String.valueOf(link.getId()))){
					LOGGER.info("Order Sub Category");
					primaryserviceDetail.setOrderSubCategory(flowMapper.get(String.valueOf(link.getId())));
				}
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
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
					primaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
				}else if("MACD".equals(odrOrder.getOrderType()) && !"ADD_SITE".equals(odrOrder.getOrderCategory())
						&& (Objects.isNull(primaryserviceDetail.getOrderSubCategory()) || (Objects.nonNull(primaryserviceDetail.getOrderSubCategory()) && !primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))){
					LOGGER.info("UUID {}",link.getLinkCode());
					primaryserviceDetail.setUuid(flowMapper.get(link.getLinkCode()));
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
					primaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
				} else if("CANCELLATION".equalsIgnoreCase(odrOrder.getOrderType())) {
					LOGGER.info("CANCELLATION UUID {}",link.getLinkCode());
					primaryserviceDetail.setUuid(flowMapper.get(link.getLinkCode()));
				} else if("TERMINATION".equalsIgnoreCase(odrOrder.getOrderType())) {
					LOGGER.info("TERMINATION UUID {}",link.getLinkCode());
					primaryserviceDetail.setUuid(flowMapper.get(link.getLinkCode()));
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
				}
				else if ("RENEWALS".equals(odrOrder.getOrderType())) {
					LOGGER.info("UUID {}", link.getLinkCode());

					Optional<OrderIllSite> orderIllSite = orderProductSolution.getOrderIllSites().stream()
							.filter(x -> x.getErfServiceInventoryTpsServiceId() != null).findFirst();
					if (orderIllSite.isPresent()) {
						primaryserviceDetail.setUuid(orderIllSite.get().getErfServiceInventoryTpsServiceId());
					
					primaryserviceDetail.setOrderType(odrOrder.getOrderType());
					OrderProductSolutionSiLink  orderProductSolutionSiLink =  orderProductSolutionSiLinkRepository.findFirstByServiceIdAndQuoteToLeId(orderIllSite.get().getErfServiceInventoryTpsServiceId(), 
							odrOrder.getErfOrderLeId());
					if(orderProductSolutionSiLink!=null) {
						primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail, "taxExemption", orderProductSolutionSiLink.getTaxException()));

					}
					}
					// primaryserviceDetail.setOrderCategory(odrOrder.getOrderCategory());
				}
				List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.findByQuoteLinkCodeAndServiceType(link.getLinkCode(), "Link");
				if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) {
					LOGGER.info("quote site differential commercial order site code {} diff mrc {}, diff nrc {}", link.getLinkCode(), quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc(), quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
					primaryserviceDetail.setDifferentialMrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc());
					primaryserviceDetail.setDifferentialNrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
				}
				odrServiceDetails.add(primaryserviceDetail);
				processCommonComponentAttr(String.valueOf(link.getId()), primaryserviceDetail, username, flowMapper);
				processProductComponentAttr(String.valueOf(link.getId()), primaryserviceDetail, "Link", username, false,
						odrServiceCommercials, flowMapper, link);
				processLinkSla(link, primaryserviceDetail, username);
				processSite(flowMapper, username, link, primaryserviceDetail, odrComponents, odrComponentAttributes,odrAttachments);
			} catch (Exception e) {
				LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
			}

		}

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
		LOGGER.info("Inside Npl odr service processOrderToLe"+ orderToLe.getId());
		if (flag) {
			odrOrder.setErfCustLeId(orderToLe.getErfCusCustomerLegalEntityId());
			odrOrder.setOrderCategory(orderToLe.getOrderCategory());
			if ("MACD".equals(orderToLe.getOrderType()) || "CANCELLATION".equalsIgnoreCase(orderToLe.getOrderType()) || "TERMINATION".equalsIgnoreCase(orderToLe.getOrderType())|| "RENEWALS".equalsIgnoreCase(orderToLe.getOrderType())) {
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
			odrOrder.setOpportunityClassification(OdrConstants.SELL_TO);
			odrOrder.setTpsSfdcOptyId(orderToLe.getTpsSfdcCopfId());
		}
		// TO add sfdc opty and cof Id
		odrOrder.setDemoFlag((orderToLe.getIsDemo()!=null && orderToLe.getIsDemo() == CommonConstants.BACTIVE) ? CommonConstants.Y : CommonConstants.N);
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
		LOGGER.info("Inside NPL Odr Service processOrderToLeAttr"+ odrOrder.getId());
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
			} else if (odrOrder.getOpOrderCode().startsWith(CommonConstants.IPC)
					&& (attrName.equals(LeAttributesConstants.NAME) || attrName.equals(LeAttributesConstants.EMAIL_ID)
							|| attrName.equals(LeAttributesConstants.NUMBER))) {
				flowMapper.put(attrName, attrValue);
				// addOdrOrderAttributes(odrOrder, odrAttributes, userName, attrName,
				// attrValue); //NOSONAR
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
		if(CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(odrServiceDetail.getErfPrdCatalogOfferingName())){
			List<OrderIllSite> orderIllSites = orderIllSiteRepository.findBySiteCodeAndStatus(odrServiceDetail.getServiceRefId(),
					CommonConstants.BACTIVE);
			LOGGER.info("Inside cross connect processOrderEnrichmentForIndividualSites {} :",orderIllSites);
			for (OrderIllSite orderIllSite : orderIllSites) {
				Map<String, String> flowMapper = getCrossConnectOrderStatus(orderIllSite);
				if (flowMapper != null) {
					LOGGER.info("Cross connect Order Enrichment is completed so proceeding the with the oe");
					processOeCrossConnectSite(username, orderIllSite, flowMapper, odrServiceDetail);
				} else {
					LOGGER.info("Cross connect Order Enrichment is not yet completed so just updating the New Order to COMPLETED");
					odrServiceDetailRepository.save(odrServiceDetail);
				}
			}

		}else{
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
			Integer popLocationId, String popSiteCode, OrderNplLink orderNplLink,List<OdrAttachment> odrAttachments, OrderIllSite orderIllSite)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Inside npl odr service processProductComponentAttrForSite for siteId {}",refId);
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(String.valueOf(refId), type, OdrConstants.NPL_SITES);
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
					
					serviceDetail.setLastmileBw(bw[0]);
					serviceDetail.setLastmileBwAltName(bw[0]);
					serviceDetail.setLastmileBwUnit("Mbps");
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
	if ("RENEWALS".equalsIgnoreCase(serviceDetail.getOdrOrder().getOrderType())) {
			List<Map<String, String>> oderProdCompAttrsRenewals = OrderProductComponentsAttributeValueRepository
					.findByRefidAndRefNameRenewals(String.valueOf(refId), type);
			if(!oderProdCompAttrsRenewals.isEmpty()) {
			for (Map<String, String> oderProdCompAttr : oderProdCompAttrsRenewals) {
				String attrName = oderProdCompAttr.get("attrName");
				String attrValue = oderProdCompAttr.get("attrValue");
			if(attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)){
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
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
							"siteGstAddress", addServiceParam.get().getValue(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
							"siteGstAddressId", attrValue, userName));
				}
			}
			}	
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
		if (flowMapper.containsKey("lmTypeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmType",
					flowMapper.get("lmTypeA"), userName));
			serviceDetail.setLastmileType(flowMapper.get("lmTypeA"));
			serviceDetail.setAccessType(flowMapper.get("lmTypeA"));
		}
		if (flowMapper.containsKey("lmTypeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmType",
					flowMapper.get("lmTypeB"), userName));
		}
		
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
		if (flowMapper.containsKey("lmScenarioTypeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileScenario",
					flowMapper.get("lmScenarioTypeA"), userName));
			
		}
		if (flowMapper.containsKey("lmConnectionTypeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmConnectionType",
					flowMapper.get("lmConnectionTypeA"), userName));
		}
		if (flowMapper.containsKey("lmScenarioTypeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileScenario",
					flowMapper.get("lmScenarioTypeB"), userName));
		}
		if (flowMapper.containsKey("lmConnectionTypeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmConnectionType",
					flowMapper.get("lmConnectionTypeB"), userName));
		}
		if (flowMapper.containsKey("serviceSubType")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "serviceSubType",
					flowMapper.get("serviceSubType"), userName));
		}
		if("TERMINATION".equalsIgnoreCase(serviceDetail.getOdrOrder().getOrderType())) {
			List<OrderIllSiteToService> orderSiteToServiceList = orderIllSiteToServiceRepository.findByOrderNplLink_Id(orderNplLink.getId());
			if(orderSiteToServiceList != null && !orderSiteToServiceList.isEmpty()) {
			OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails = orderSiteServiceTerminationDetailsRepository.findByOrderIllSiteToService(orderSiteToServiceList.get(0));
			
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactMobile",
					orderSiteServiceTerminationDetails.getLocalItContactNumber(),
					userName));
			
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactEmailId",
					orderSiteServiceTerminationDetails.getLocalItContactEmailId(),
					userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactName",
					orderSiteServiceTerminationDetails.getLocalItContactName(),
					userName));
			
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"customerMailReceiveDate",
					(orderSiteServiceTerminationDetails.getCustomerMailReceivedDate() != null ?
							orderSiteServiceTerminationDetails.getCustomerMailReceivedDate().toString() : null) ,
					userName));
			
			}
			String srvSiteType = "Site-A".equalsIgnoreCase(type) ? "SiteA" : "SiteB";
			persistComponentAttrForTermination(serviceDetail, odrComponent, odrComponentAttributes, orderSiteToServiceList.get(0), srvSiteType);
		}
		if ("RENEWALS".equalsIgnoreCase(serviceDetail.getOdrOrder().getOrderType())) {
			List<OrdersLeAttributeValue> ordersLeAttributeValueList = ordersLeAttributeValueRepository
					.findByOrderToLe_Id(serviceDetail.getOdrOrder().getErfOrderLeId());
			Optional<OrderToLe> orderTole = orderToLeRepository.findById(serviceDetail.getOdrOrder().getErfOrderLeId());

			Optional<OrdersLeAttributeValue> ordersLeAttributeValue = ordersLeAttributeValueList.stream()
					.filter(x -> x.getDisplayValue().equalsIgnoreCase("Effective Date")).findFirst();
			if (ordersLeAttributeValue.isPresent()) {
				LOGGER.info("value present");

				Date formattedDate;
				try {
					formattedDate = DateUtils.parseDate(ordersLeAttributeValue.get().getAttributeValue(),
							new String[] { "dd-MMM-yyyy", "yyyy-MM-dd" });
			
				DateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
				String formattedDate1 = dateFormatter1.format(formattedDate);

				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
						"serviceCommissionedDate", formattedDate1, userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
						"effectiveDateOfChange", formattedDate1, userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
						"billStartDate", formattedDate1, userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
						"commissioningDate", formattedDate1, userName));
				
				
				OrderProductSolutionSiLink  orderProductSolutionSiLink =  orderProductSolutionSiLinkRepository.findFirstByServiceIdAndQuoteToLeId(orderIllSite.getErfServiceInventoryTpsServiceId(), 
						serviceDetail.getOdrOrder().getErfOrderLeId());
				if(orderProductSolutionSiLink!=null) {
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent,
							serviceDetail, "PO_DATE", orderProductSolutionSiLink.getPoDate(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent,
							serviceDetail, "PO_NUMBER", orderProductSolutionSiLink.getPoNumber(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
							"taxExemption", orderProductSolutionSiLink.getTaxException(), userName));
				}
				
				
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "linkCode",
				orderNplLink.getLinkCode(), userName));
		persistAddressDetailsOfTheSite(locationId, odrComponent, odrComponentAttributes, serviceDetail, userName,
				siteType,orderIllSite);
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
			List<OdrComponentAttribute> odrComponentAttributes, OdrServiceDetail odrServiceDetail, String userName,String siteType, OrderIllSite orderIllSite) throws TclCommonException {
		LOGGER.info("Order Site address for Reference Id {}",orderIllSite.getId());
		OrderSiteAddress orderSiteAddress = orderSiteAddressRepository.findByReferenceIdAndReferenceNameAndSiteType(orderIllSite.getId().toString(), "NPL_SITES", siteType.toLowerCase());
		if (orderSiteAddress != null) {
			LOGGER.info("Order Site Loaded with id {}", orderSiteAddress.getId());
			String addr = StringUtils.trimToEmpty(orderSiteAddress.getAddressLineOne()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(orderSiteAddress.getAddressLineTwo()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(orderSiteAddress.getLocality()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(orderSiteAddress.getCity()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(orderSiteAddress.getState()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(orderSiteAddress.getCountry()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(orderSiteAddress.getPincode());
			odrComponentAttributes
					.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "siteAddress", addr, userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "latLong",
					orderSiteAddress.getLatLong(), userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
					"destinationCountry", orderSiteAddress.getCountry(), userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "destinationCity",
					orderSiteAddress.getCity(), userName));
			if (siteType != null && siteType.equals("A")) {
				odrServiceDetail.setDestinationCity(orderSiteAddress.getCity());
			}
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
					"destinationAddressLineOne", orderSiteAddress.getAddressLineOne(), userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
					"destinationAddressLineTwo", orderSiteAddress.getAddressLineTwo(), userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
					"destinationLocality", orderSiteAddress.getLocality(), userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
					"destinationState", orderSiteAddress.getState(), userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
					"destinationPincode", orderSiteAddress.getPincode(), userName));
		} else {
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
						"siteAddressId", String.valueOf(locationId), userName));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"siteAddress", addr, userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "latLong",
							addressDetail.getLatLong(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"destinationCountry", addressDetail.getCountry(), userName));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail,
							"destinationCity", addressDetail.getCity(), userName));
					if (siteType != null && siteType.equals("A")) {
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
		
		//added for order level sitewise billing info
		OrderSiteBillingDetails orderBillinginfo=orderSiteBillingInfoRepository.findByOrderIllSite(orderIllSite);
			if (orderBillinginfo != null) {
				LOGGER.info("inside if multisitebillinginfo id order level NPL:::" + orderBillinginfo.getId());
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
									odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "billingAddressLineOne", addressDetail.getAddressLineOne(),userName));
									odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "billingAddressLineTwo", addressDetail.getAddressLineTwo(),userName));
									odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "billingAddressLineThree","",userName));
									odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "billingAddressCity", addressDetail.getCity(),userName));
									odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "billingAddressState", addressDetail.getState(),userName));
									odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "billingAddressCountry", addressDetail.getCountry(),userName));
									odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "billingAddressPincode", addressDetail.getPincode(),userName));

								}
							}
						}
					}
				}
			}
	}


	private void persistAddressDetailsOfThePopLocationOfTheSite(Integer locationId, OdrComponent odrComponent,
			List<OdrComponentAttribute> odrComponentAttributes, OdrServiceDetail odrServiceDetail, String userName,String popSiteCode) throws TclCommonException {
		if (locationId != null) {
			LOGGER.info("Updating source city using location id {}",locationId);
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

					odrServiceDetail.setPopSiteAddress(addr);
					odrServiceDetail.setPopSiteCode(popSiteCode);
					odrServiceDetail.setSourceCountry(addressDetail.getCountry());
					odrServiceDetail.setSourceCity(addressDetail.getCity());
					odrServiceDetail.setSourceAddressLineOne(addressDetail.getAddressLineOne());
					odrServiceDetail.setSourceAddressLineTwo(addressDetail.getAddressLineTwo());
					odrServiceDetail.setSourceLocality(addressDetail.getLocality());
					odrServiceDetail.setSourcePincode(addressDetail.getPincode());
					odrServiceDetail.setSourceState(addressDetail.getState());

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

	public void processCrossConnectSite(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
										List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
										String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrComponent> odrComponents,
										List<OdrComponentAttribute> odrComponentAttributes, List<OdrAttachment> odrAttachments) {
		LOGGER.info("processCrossConnectSite method invoked");
		for (OrderIllSite orderIllSite : orderProductSolution.getOrderIllSites()) {
			try {
				List<Map<String, String>> crossConnectAttributes = OrderProductComponentsAttributeValueRepository.findAttributeValueBySiteIdAsReferenceId(String.valueOf(orderIllSite.getId()));
				Integer cableCount=0;
				String  crossConnectType="";
				String  fiberEntryNeeded="";
				if (crossConnectAttributes.stream()
						.anyMatch(attrVal -> attrVal.get("attributeName").equalsIgnoreCase("Cross Connect Type"))) {
					crossConnectType = crossConnectAttributes.stream()
							.filter(attrVal -> attrVal.get("attributeName").equalsIgnoreCase("Cross Connect Type"))
							.findFirst().get().get("attributeValue");
					fiberEntryNeeded = crossConnectAttributes.stream()
							.filter(attrVal -> attrVal.get("attributeName").equalsIgnoreCase("Do you want fiber entry"))
							.findFirst().get().get("attributeValue");
					LOGGER.info("Cross connect Do you want fiber entry : {}", fiberEntryNeeded);

					LOGGER.info("Cross connect type value  : {}", crossConnectType);
					if ("Passive".equalsIgnoreCase(crossConnectType) && crossConnectAttributes.stream()
							.anyMatch(attrVal -> attrVal.get("attributeName").equalsIgnoreCase("Media Type"))) {
						String mediaType = crossConnectAttributes.stream()
								.filter(attrVal -> attrVal.get("attributeName").equalsIgnoreCase("Media Type"))
								.findFirst().get().get("attributeValue");
						LOGGER.info("Cross connect media type  : {}", mediaType);
						if ("Fiber pair".equalsIgnoreCase(mediaType)) {
							cableCount = Integer.parseInt(crossConnectAttributes.stream()
									.filter(attrVal -> attrVal.get("attributeName")
											.equalsIgnoreCase("No. of Fiber pairs"))
									.findFirst().get().get("attributeValue"));
							LOGGER.info("Cross connect no. of fiber pair  : {}", cableCount);
						} else {
							cableCount = Integer.parseInt(crossConnectAttributes.stream()
									.filter(attrVal -> attrVal.get("attributeName")
											.equalsIgnoreCase("No. of Cable pairs"))
									.findFirst().get().get("attributeValue"));
							LOGGER.info("Cross connect no. of cable pair  : {}", cableCount);
						}
					}
				}
				if ("Passive".equalsIgnoreCase(crossConnectType)){
					LOGGER.info("Cross connect Type:{},{}",crossConnectType,cableCount);
					for(int serviceCount=0;serviceCount<cableCount;serviceCount++){
						constructCrossConnectServiceDetails(flowMapper,  orderProductSolution,
								odrServiceDetails,  odrContractInfo,  odrOrder,
								username,odrServiceCommercials,  odrComponents,
								odrComponentAttributes,  odrAttachments, orderIllSite, crossConnectType,
								fiberEntryNeeded);
					}
				}else if("Active".equalsIgnoreCase(crossConnectType)){
					LOGGER.info("Cross connect Type:{}",crossConnectType);
					constructCrossConnectServiceDetails(flowMapper,  orderProductSolution,
							odrServiceDetails,  odrContractInfo,  odrOrder,
							username,odrServiceCommercials,  odrComponents,
							odrComponentAttributes,  odrAttachments, orderIllSite, crossConnectType,
							fiberEntryNeeded);
				}
			} catch (Exception e) {
				LOGGER.error("Error in processCrossConnectSite : {}", ExceptionUtils.getStackTrace(e));
			}

		}
	}

	private void constructCrossConnectServiceDetails(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
													 List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
													 String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrComponent> odrComponents,
													 List<OdrComponentAttribute> odrComponentAttributes, List<OdrAttachment> odrAttachments,OrderIllSite orderIllSite,String crossConnectType,
													 String fiberEntryNeeded) {
		LOGGER.info("constructCrossConnectServiceDetails method invoked");
		try{
			OdrServiceDetail odrServiceDetail = new OdrServiceDetail();
			odrServiceDetail.setFlowStatus("NEW");
			odrServiceDetail.setOrderType("NEW");
			odrServiceDetail.setAccessType("OnnetWL");
			odrServiceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
			odrServiceDetail.setServiceRefId(orderIllSite.getSiteCode());
			odrServiceDetail.setOdrContractInfo(odrContractInfo);
			odrServiceDetail.setOdrOrder(odrOrder);
			odrServiceDetail.setServiceCommissionedDate(
					orderIllSite.getRequestorDate() != null ? orderIllSite.getRequestorDate()
							: orderIllSite.getEffectiveDate());
			odrServiceDetail.setArc(orderIllSite.getArc());
			odrServiceDetail.setMrc(orderIllSite.getMrc());
			odrServiceDetail.setNrc(orderIllSite.getNrc());
			odrServiceDetail.setErfPrdCatalogOfferingName(flowMapper.get("offeringName"));
			odrServiceDetail.setErfPrdCatalogProductName(flowMapper.get("productName"));
			odrServiceDetail.setErfPrdCatalogProductId(Objects.nonNull(flowMapper.get("productCode")) ? Integer.valueOf(flowMapper.get("productCode").trim()) : null);
			odrServiceDetail.setCreatedBy(username);
			odrServiceDetail.setCreatedDate(new Date());
			odrServiceDetail.setUpdatedDate(new Date());
			odrServiceDetail.setUpdatedBy(username);
			odrServiceDetail.setIsActive(CommonConstants.Y);
			odrServiceDetail.setIsIzo(CommonConstants.N);
			odrServiceDetail.setPrimarySecondary("CrossConnect");
			if("MACD".equals(odrOrder.getOrderType())){
				List<OrderIllSiteToService> orderIllSiteToServices = orderIllSiteToServiceRepository.findByOrderToLe_Id(odrOrder.getErfOrderLeId());
				LOGGER.info("UUID {}",orderIllSiteToServices.get(0).getErfServiceInventoryTpsServiceId());
				odrServiceDetail.setUuid(orderIllSiteToServices.get(0).getErfServiceInventoryTpsServiceId());
				odrServiceDetail.setOrderType(odrOrder.getOrderType());
				odrServiceDetail.setOrderCategory(odrOrder.getOrderCategory());
				odrServiceDetail.setOrderSubCategory(orderIllSiteToServices.get(0).getErfSfdcOrderType());
			}
			
			List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository.findByQuoteSiteCodeAndServiceType(orderIllSite.getSiteCode(), "primary");
			if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) {
				LOGGER.info("quote site differential commercial order site code {} diff mrc {}, diff nrc {}", orderIllSite.getSiteCode(), quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc(), quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
				odrServiceDetail.setDifferentialMrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc());
				odrServiceDetail.setDifferentialNrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
			}
			
			if("CANCELLATION".equalsIgnoreCase(odrOrder.getOrderType())) {
				String absorbedOrPassedOn = null;
				List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
				odrServiceDetail.setUuid(orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
				if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty()) {
					absorbedOrPassedOn = orderIllSiteToServiceList.get(0).getAbsorbedOrPassedOn();
				LOGGER.info("Absorbed or passed on {} order site Id {}",absorbedOrPassedOn, orderIllSite.getId());
				if(absorbedOrPassedOn != null ) {
					if("Absorbed".equalsIgnoreCase(absorbedOrPassedOn)) {
						odrServiceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						odrServiceDetail.setCancellationCharges(0D);
					} else if ("Passed On".equalsIgnoreCase(absorbedOrPassedOn)) {
						odrServiceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						odrServiceDetail.setCancellationCharges(orderIllSite.getNrc());
					}
					LOGGER.info("cancellation charges for order site id {} is {}", orderIllSite.getId(), odrServiceDetail.getCancellationCharges());
				}
				odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "cancellationReason", orderIllSiteToServiceList.get(0).getCancellationReason()));
				odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "leadToRFSDate", orderIllSiteToServiceList.get(0).getLeadToRFSDate()));
				odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "effectiveDateOfChange", String.valueOf(orderIllSiteToServiceList.get(0).getEffectiveDateOfChange())));				
				}
			}
			if("TERMINATION".equalsIgnoreCase(odrOrder.getOrderType())) {
				odrServiceDetail.setOrderType(odrOrder.getOrderType());
				List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
				odrServiceDetail.setUuid(orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
				if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty()) {
				orderIllSiteToServiceList.stream().forEach(orderIllSiteToService -> {
					OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails =  orderSiteServiceTerminationDetailsRepository.findByOrderIllSiteToService(orderIllSiteToService);
					if(orderSiteServiceTerminationDetails != null){
						odrOrder.setTerminationParentOrderCode(orderSiteServiceTerminationDetails.getTerminatedParentOrderCode());
						odrServiceDetail.setTerminationReason(orderSiteServiceTerminationDetails.getReasonForTermination());
						if("Send to CSM".equalsIgnoreCase(orderSiteServiceTerminationDetails.getHandoverTo())
								|| "Send to AM".equalsIgnoreCase(orderSiteServiceTerminationDetails.getHandoverTo())) {
							odrServiceDetail.setNegotiationRequired(CommonConstants.YES);
						} else {
							odrServiceDetail.setNegotiationRequired(CommonConstants.NO);
						}
						odrServiceDetail.setTerminationEffectiveDate(orderSiteServiceTerminationDetails.getEffectiveDateOfChange());
						List<OrderProductComponent> orderProductComponentList =
								orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(orderSiteServiceTerminationDetails.getOrderIllSiteToService().getOrderIllSite().getId(),"ETC Charges", "ILL_SITES");
						LOGGER.info("orderProductComponentList {}", orderProductComponentList.size());
						Double[] etcValue = { 0D };
						orderProductComponentList.stream().forEach(orderProdComp ->{
								
										OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(String.valueOf(orderProdComp.getId()), "COMPONENTS");
										if(orderPrice != null) {
										etcValue[0] = orderPrice.getEffectiveNrc();
										LOGGER.info("Etc value {}", etcValue[0]);
										}
							});
						odrServiceDetail.setEtcValue(etcValue[0]);
						odrServiceDetail.setEtcWaiver(CommonConstants.BACTIVE.equals(orderSiteServiceTerminationDetails.getEtcApplicable()) ? "Yes" : "No");
						odrServiceDetail.setCustomerRequestorDate(orderSiteServiceTerminationDetails.getRequestedDateForTermination());
						odrServiceDetail.setLocalItContactEmail(orderSiteServiceTerminationDetails.getLocalItContactEmailId());
						odrServiceDetail.setLocalItContactMobile(orderSiteServiceTerminationDetails.getLocalItContactNumber());
						odrServiceDetail.setLocalItContactName(orderSiteServiceTerminationDetails.getLocalItContactName());
						List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType( "Site", orderSiteServiceTerminationDetails.getOrderIllSiteToService().getOrderIllSite().getId(), AttachmentTypeConstants.APPRVEMAIL.toString());
						if(omsAttachmentList != null & !omsAttachmentList.isEmpty()) {
							odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "approvalMailAvailable", "Yes"));
						} else {
							odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "approvalMailAvailable", "No"));
						}
						odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "csmEmail", orderSiteServiceTerminationDetails.getCsmNonCsmEmail()));
						odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "csmUserName", orderSiteServiceTerminationDetails.getCsmNonCsmName()));
						odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "terminationSubType", orderSiteServiceTerminationDetails.getTerminationSubtype()));
						odrServiceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(odrServiceDetail, "terminationSubReason", orderSiteServiceTerminationDetails.getSubReason()));
						
					}
				});
				}
			}
			odrServiceDetails.add(odrServiceDetail);
			LOGGER.info("Cross connect odrServiceDetail value : {}", odrServiceDetail);
			processCrossConnectProductServiceAttr(orderIllSite, odrServiceDetail,
					username, false, odrServiceCommercials, flowMapper, crossConnectType,fiberEntryNeeded);
			LOGGER.info("Total Number of Cross Connect Service attributes : {}", odrServiceDetail.getOdrServiceAttributes().size());
			processCrossConnectSiteSla(orderIllSite, odrServiceDetail, username);
			processCrossConnectProductComponentAttr(flowMapper, username, orderIllSite,
					odrServiceDetail, odrComponents, odrComponentAttributes, odrAttachments);
		}catch (Exception e) {
			LOGGER.error("Error in constructCrossConnectServiceDetails : {}", ExceptionUtils.getStackTrace(e));
		}

	}


	private void processCrossConnectProductComponentAttr(Map<String, String> flowMapper, String username, OrderIllSite orderIllSite,
														 OdrServiceDetail serviceDetail, List<OdrComponent> odrComponents, List<OdrComponentAttribute> odrComponentAttributes, List<OdrAttachment> odrAttachments) {
		LOGGER.info("Inside Npl odr service processSite");
		try {
			processCrossConnectProductComponentAttrForSite(orderIllSite, serviceDetail,
					"Site-A", username, flowMapper, odrComponents, odrComponentAttributes,
					orderIllSite.getErfLocSitebLocationId(), orderIllSite.getErfLocSiteaLocationId(),
					orderIllSite.getErfLocSiteaSiteCode(), odrAttachments);
			Integer siteBLocationId=null;
			if(orderIllSite.getErfLocSiteaLocationId()!=null){
				siteBLocationId=orderIllSite.getErfLocSiteaLocationId();
			}
			else{
				siteBLocationId=orderIllSite.getErfLocSitebLocationId();
			}

			processCrossConnectProductComponentAttrForSite(orderIllSite, serviceDetail,
					"Site-B", username, flowMapper, odrComponents, odrComponentAttributes,
					siteBLocationId, orderIllSite.getErfLocSiteaLocationId(),
					orderIllSite.getErfLocSiteaSiteCode(), odrAttachments);
		} catch (Exception e) {
			LOGGER.error("Error in processCrossConnectProductComponentAttr : {}", ExceptionUtils.getStackTrace(e));
		}
	}

	private void processCrossConnectProductComponentAttrForSite(OrderIllSite orderIllSite, OdrServiceDetail serviceDetail,
																String type, String userName, Map<String, String> flowMapper, List<OdrComponent> odrComponents, List<OdrComponentAttribute> odrComponentAttributes,
																Integer locationId,
																Integer popLocationId, String popSiteCode, List<OdrAttachment> odrAttachments) throws TclCommonException {

		LOGGER.info("Inside npl odr service processCrossConnectProductComponentAttrForSite");


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
		String crossConnectType="";
		if(flowMapper.containsKey("crossConnectType") && Objects.nonNull(flowMapper.containsKey("crossConnectType"))) {
			crossConnectType =flowMapper.get("crossConnectType");
		}
		if (flowMapper.containsKey("Bandwidth") && (type.equals("Site-A") || type.equals("Site-B")) && "Active".equalsIgnoreCase(crossConnectType)) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localLoopBandwidth", flowMapper.get("Bandwidth"), userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"bwUnit", "Mbps", userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localLoopBandwidthUnit", "Mbps", userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"portBandwidth", flowMapper.get("Bandwidth"), userName));
		}
		if (flowMapper.containsKey("lastMileProvider") && (type.equals("Site-A") || type.equals("Site-B"))) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileProvider",
					flowMapper.get("lastMileProvider"), userName));
		}
		if (flowMapper.containsKey("Interface Type - A end") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "interface",
					flowMapper.get("Interface Type - A end"), userName));
		}
		if (flowMapper.containsKey("Interface Type - B end") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "interface",
					flowMapper.get("Interface Type - B end"), userName));
		}
		if (flowMapper.containsKey("A end Address Type") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "addressType",
					flowMapper.get("A end Address Type"), userName));
		}
		if (flowMapper.containsKey("B end Address Type") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "addressType",
					flowMapper.get("B end Address Type"), userName));
		}
		if (flowMapper.containsKey("GSTNO_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstNumber",
					flowMapper.get("GSTNO_A"), userName));
		}
		if (flowMapper.containsKey("GSTNO_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstNumber",
					flowMapper.get("GSTNO_B"), userName));
		}
		//Need to check for GST_ADDRESS
		if (flowMapper.containsKey("EMAIL_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactEmailId",
					flowMapper.get("EMAIL_A"), userName));
		}
		if (flowMapper.containsKey("CONTACT_NO_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactMobile",
					flowMapper.get("CONTACT_NO_A"), userName));
		}
		if (flowMapper.containsKey("NAME_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactName",
					flowMapper.get("NAME_A"), userName));
		}

		if (flowMapper.containsKey("EMAIL_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactEmailId",
					flowMapper.get("EMAIL_B"), userName));
		}
		if (flowMapper.containsKey("CONTACT_NO_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactMobile",
					flowMapper.get("CONTACT_NO_B"), userName));
		}
		if (flowMapper.containsKey("NAME_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "localItContactName",
					flowMapper.get("NAME_B"), userName));
		}

		if (flowMapper.containsKey("BUILDING_NAME_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationBuildingName",
					flowMapper.get("BUILDING_NAME_A"), userName));
		}
		if (flowMapper.containsKey("FLOOR_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationFloor",
					flowMapper.get("FLOOR_A"), userName));
		}
		if (flowMapper.containsKey("WING_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationWing",
					flowMapper.get("WING_A"), userName));
		}
		if (flowMapper.containsKey("ROOM_A") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationRoom",
					flowMapper.get("ROOM_A"), userName));
		}

		if (flowMapper.containsKey("BUILDING_NAME_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationBuildingName",
					flowMapper.get("BUILDING_NAME_B"), userName));
		}
		if (flowMapper.containsKey("FLOOR_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationFloor",
					flowMapper.get("FLOOR_B"), userName));
		}
		if (flowMapper.containsKey("WING_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationWing",
					flowMapper.get("WING_B"), userName));
		}
		if (flowMapper.containsKey("ROOM_B") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "demarcationRoom",
					flowMapper.get("ROOM_B"), userName));
		}

		if (flowMapper.containsKey("lmTypeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmType",
					flowMapper.get("lmTypeA"), userName));
			serviceDetail.setLastmileType(flowMapper.get("lmTypeA"));
			serviceDetail.setAccessType(flowMapper.get("lmTypeA"));
		}
		if (flowMapper.containsKey("lmTypeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmType",
					flowMapper.get("lmTypeB"), userName));
		}

		if (flowMapper.containsKey("popSiteCodeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "popSiteCode",
					flowMapper.get("popSiteCodeA"), userName));
		}
		if (flowMapper.containsKey("popSiteCodeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "popSiteCode",
					flowMapper.get("popSiteCodeB"), userName));
		}
		if (flowMapper.containsKey("lmScenarioTypeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileScenario",
					flowMapper.get("lmScenarioTypeA"), userName));

		}
		if (flowMapper.containsKey("lmScenarioTypeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lastMileScenario",
					flowMapper.get("lmScenarioTypeB"), userName));
		}
		if (flowMapper.containsKey("lmConnectionTypeA") && type.equals("Site-A")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmConnectionType",
					flowMapper.get("lmConnectionTypeA"), userName));
		}
		if (flowMapper.containsKey("lmConnectionTypeB") && type.equals("Site-B")) {
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "lmConnectionType",
					flowMapper.get("lmConnectionTypeB"), userName));
		}
		if (flowMapper.containsKey("GST_ADDRESS")  && type.equals("Site-A")) {
			Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
					.findById(Integer.valueOf(flowMapper.get("GST_ADDRESS")));
			LOGGER.info("Site GST Address ID {}",Integer.valueOf(flowMapper.get("GST_ADDRESS")));
			if (addServiceParam.isPresent()) {
				LOGGER.info("Additional Service Param present and the value is {}",addServiceParam.get().getValue());
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstAddress",
						addServiceParam.get().getValue(), userName));
				//odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					//	"siteGstAddressId", addServiceParam.get().getValue(), userName));
			}
		}
		if (flowMapper.containsKey("GST_ADDRESS")  && type.equals("Site-B")) {
			Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
					.findById(Integer.valueOf(flowMapper.get("GST_ADDRESS")));
			LOGGER.info("Site GST Address ID {}",Integer.valueOf(flowMapper.get("GST_ADDRESS")));
			if (addServiceParam.isPresent()) {
				LOGGER.info("Additional Service Param present and the value is {}",addServiceParam.get().getValue());
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstAddress",
						addServiceParam.get().getValue(), userName));
				//odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					//	"siteGstAddressId", addServiceParam.get().getValue(), userName));
			}
		}
		if("TERMINATION".equalsIgnoreCase(serviceDetail.getOdrOrder().getOrderType())) {
			List<OrderIllSiteToService> orderSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(orderIllSite);
			if(orderSiteToServiceList != null && !orderSiteToServiceList.isEmpty()) {
			OrderSiteServiceTerminationDetails orderSiteServiceTerminationDetails = orderSiteServiceTerminationDetailsRepository.findByOrderIllSiteToService(orderSiteToServiceList.get(0));
			
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactMobile",
					orderSiteServiceTerminationDetails.getLocalItContactNumber(),
					userName));
			
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactEmailId",
					orderSiteServiceTerminationDetails.getLocalItContactEmailId(),
					userName));
			odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail,
					"localItContactName",
					orderSiteServiceTerminationDetails.getLocalItContactName(),
					userName));
			}
		}
		odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteCode",
				orderIllSite.getSiteCode(), userName));

		persistAddressDetailsOfTheSite(locationId, odrComponent, odrComponentAttributes, serviceDetail, userName,
				siteType,orderIllSite);
		persistAddressDetailsOfThePopLocationOfTheSite(popLocationId, odrComponent, odrComponentAttributes,
				serviceDetail, userName, popSiteCode);
		LOGGER.info("Before getting demar details for {}", type);


		List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByReferenceNameAndReferenceId("Site",
				orderIllSite.getId());
		for (OmsAttachment omsAttachment : omsAttachments) {
			Integer erfAttachmentId = omsAttachment.getErfCusAttachmentId();
			LOGGER.info("MDC Filter token value in before Queue call getAttachment {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
					String.valueOf(erfAttachmentId));
			LOGGER.info("Queue call getAttachment values {} :", attachmentResponse);
			if (attachmentResponse != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);
				if (attachmentMapper != null) {
					Attachment attachment = new Attachment();
					OdrAttachment odrAttachment = new OdrAttachment();

					attachment.setCreatedBy(userName);
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
					odrAttachment.setOdrServiceDetail(serviceDetail);
					// odrAttachment.setServiceCode(odrServiceDetail.getUuid());
					odrAttachments.add(odrAttachment);
				}

			}

		}
	}


	/**
	 * persistOdrComponent
	 * @param primaryserviceDetail
	 * @param odrComponentAttributes
	 * @return
	 */
	private OdrComponent constructOdrComponent(OdrServiceDetail primaryserviceDetail) {
		OdrComponent odrComponent=new OdrComponent();
		odrComponent.setComponentName("LM");
		odrComponent.setOdrServiceDetail(primaryserviceDetail);
		odrComponent.setCreatedBy(Utils.getSource());
		odrComponent.setCreatedDate(new Date());
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
	 * processSiteSla
	 *
	 * @param orderIllSite
	 * @param primaryserviceDetail
	 */
	private void processCrossConnectSiteSla( OrderIllSite orderIllSite,OdrServiceDetail primaryserviceDetail, String username) {
		LOGGER.info("processCrossConnectSiteSla");
		Set<OdrServiceSla> odrServiceSlas = new HashSet<>();
		OdrServiceSla odrServiceSla = new OdrServiceSla();
		odrServiceSla.setCreatedBy(username);
		odrServiceSla.setCreatedTime(new Date());
		odrServiceSla.setIsActive(CommonConstants.Y);
		odrServiceSla.setOdrServiceDetail(primaryserviceDetail);
		odrServiceSla.setSlaComponent("Service Availability %");
		odrServiceSla.setSlaValue("99.5%");
		odrServiceSla.setUpdatedBy(username);
		odrServiceSla.setUpdatedTime(new Date());
		odrServiceSlas.add(odrServiceSla);
		primaryserviceDetail.setOdrServiceSlas(odrServiceSlas);
	}
	public CrossConnectLocalITDemarcationBean getCrossConnectDemarcationAndLocalITContact(String ccLocMappingId) {
		CrossConnectLocalITDemarcationBean crossConnectLocalITDemarcationBean = null;
		LOGGER.info("Info for cross connect location {} :" + ccLocMappingId);
		try {
			String response = (String) mqUtils.sendAndReceive(getCrossConnectLocalITandDemarcation,
					ccLocMappingId);
			crossConnectLocalITDemarcationBean = (CrossConnectLocalITDemarcationBean)
					Utils.convertJsonToObject(response, CrossConnectLocalITDemarcationBean.class);

		} catch (Exception ex) {
			LOGGER.error("Error in getCrossConnectDemarcation queue call method", ex.getMessage());
		}
		return crossConnectLocalITDemarcationBean;
	}
	public OdrServiceDetail processCrossConnectProductServiceAttr(OrderIllSite orderIllSite, OdrServiceDetail odrServiceDetail,
																  String userName, boolean isUpdate, List<OdrServiceCommercial> odrServiceCommercials,
																  Map<String, String> flowMapper,String crossConnectType,String fiberPairNeeded)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Inside Npl cross connect odr service processCrossConnectProductComponentAttr");
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdInAndReferenceName(orderIllSite.getId(),OdrConstants.ILLSITES);
		for (OrderProductComponent orderProductComponent : orderProductComponents) {
			OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					orderProductComponent.getId() + CommonConstants.EMPTY, "COMPONENTS");
			LOGGER.info("Inside Npl cross connect odr service componenet commercial value : {}",orderPrice);
			if (orderPrice != null){
				LOGGER.info("Inside cross connect price block : {}",orderPrice.getId());
				if (!"Passive".equalsIgnoreCase(crossConnectType)) {
					LOGGER.info("Inside cross connect Active block : {}",orderProductComponent.getId());
					String categoryName = orderProductComponent.getMstProductComponent().getName();
					String arcAttrName = categoryName + "_Arc_Price";
					String nrcAttrName = categoryName + "_Nrc_Price";
					addServiceAttr(odrServiceDetail, userName, orderPrice.getEffectiveArc(), categoryName, arcAttrName);
					addServiceAttr(odrServiceDetail, userName, orderPrice.getEffectiveNrc(), categoryName, nrcAttrName);
					OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
					odrServiceCommercial.setArc(orderPrice.getEffectiveArc());
					odrServiceCommercial.setMrc(orderPrice.getEffectiveMrc());
					odrServiceCommercial.setNrc(orderPrice.getEffectiveNrc());
					odrServiceCommercial.setReferenceName(orderProductComponent.getMstProductComponent().getName());
					odrServiceCommercial.setReferenceType("COMPONENTS");
					odrServiceCommercial.setOdrServiceDetail(odrServiceDetail);
					odrServiceCommercial.setServiceType("CrossConnect");
					odrServiceCommercials.add(odrServiceCommercial);
				}else{
					LOGGER.info("Inside cross connect Passive block {} :",orderProductComponent.getId());
					List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
							.findBySiteCodeAndPricingType(orderIllSite.getSiteCode(), "primary");
					OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
					if(pricingDetails != null && !pricingDetails.isEmpty()) {
					NplCrossConnectPricingBean crossConnectPricingResponse =
							Utils.convertJsonToObject(pricingDetails.stream().findFirst().get().getResponseData(), NplCrossConnectPricingBean.class);
					String categoryName = orderProductComponent.getMstProductComponent().getName();
					String arcAttrName = categoryName + "_Arc_Price";
					String nrcAttrName = categoryName + "_Nrc_Price";
					addServiceAttr(odrServiceDetail, userName, orderPrice.getEffectiveArc(), categoryName, arcAttrName);
					addServiceAttr(odrServiceDetail, userName, orderPrice.getEffectiveNrc(), categoryName, nrcAttrName);
					
					LOGGER.info("Cross connect Fiber entry needed : {}",fiberPairNeeded);
					if("Fiber Entry".equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())) {
						LOGGER.info("Fiber entry commerical component : {}",orderProductComponent.getMstProductComponent().getName());
						odrServiceCommercial.setArc(crossConnectPricingResponse.getUnitFiberEntryArc());
						odrServiceCommercial.setNrc(crossConnectPricingResponse.getUnitFiberEntryNrc());
					}else if("Cross Connect".equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())){
						LOGGER.info("Cross connect commerical component : {}",orderProductComponent.getMstProductComponent().getName());
						odrServiceCommercial.setArc(crossConnectPricingResponse.getUnitCrossConnectArc());
						odrServiceCommercial.setNrc(crossConnectPricingResponse.getUnitCrossConnectNrc());
					}
				}
					/*if("Yes".equalsIgnoreCase(fiberPairNeeded)) {
						LOGGER.info("Cross connect Fiber entry needed : {}",fiberPairNeeded);
						if("Fiber Entry".equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())) {
							LOGGER.info("Fiber entry commerical component : {}",orderProductComponent.getMstProductComponent().getName());
							odrServiceCommercial.setArc(crossConnectPricingResponse.getUnitFiberEntryArc());
							odrServiceCommercial.setNrc(crossConnectPricingResponse.getUnitFiberEntryNrc());
						}else if("Cross Connect".equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())){
							LOGGER.info("Cross connect commerical component : {}",orderProductComponent.getMstProductComponent().getName());
							odrServiceCommercial.setArc(crossConnectPricingResponse.getUnitCrossConnectArc());
							odrServiceCommercial.setNrc(crossConnectPricingResponse.getUnitCrossConnectNrc());
						}
					}else {
						LOGGER.info("Cross connect Fiber entry needed : {}",fiberPairNeeded);
						if("Fiber Entry".equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())) {
							LOGGER.info("Fiber entry commerical component : {}",orderProductComponent.getMstProductComponent().getName());
							odrServiceCommercial.setArc(crossConnectPricingResponse.getUnitFiberEntryArc());
							odrServiceCommercial.setNrc(crossConnectPricingResponse.getUnitFiberEntryNrc());
						}else if("Cross Connect".equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())){
							LOGGER.info("Cross connect commerical component : {}",orderProductComponent.getMstProductComponent().getName());
							odrServiceCommercial.setArc(crossConnectPricingResponse.getUnitCrossConnectArc());
							odrServiceCommercial.setNrc(crossConnectPricingResponse.getUnitCrossConnectNrc());
						}
					}*/
					odrServiceCommercial.setReferenceName(orderProductComponent.getMstProductComponent().getName());
					odrServiceCommercial.setReferenceType("COMPONENTS");
					odrServiceCommercial.setOdrServiceDetail(odrServiceDetail);
					odrServiceCommercial.setServiceType("CrossConnect");
					odrServiceCommercials.add(odrServiceCommercial);
				}
			}
			    LOGGER.info("Oms cross connect type attribute value : {}",crossConnectType);
				flowMapper.put("crossConnectType",crossConnectType);


			for (OrderProductComponentsAttributeValue orderProductComponentsAttribute : orderProductComponent
					.getOrderProductComponentsAttributeValues()) {
				OrderPrice attrOrderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
						orderProductComponentsAttribute.getId() + CommonConstants.EMPTY, "ATTRIBUTES");
				LOGGER.info("Inside Npl cross connect odr service attibute commercial value {} :",attrOrderPrice);
				if (attrOrderPrice != null && (attrOrderPrice.getEffectiveArc() != null
						|| attrOrderPrice.getEffectiveMrc() != null || attrOrderPrice.getEffectiveNrc() != null)) {
					OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
					odrServiceCommercial.setArc(attrOrderPrice.getEffectiveArc());
					odrServiceCommercial.setMrc(attrOrderPrice.getEffectiveMrc());
					odrServiceCommercial.setNrc(attrOrderPrice.getEffectiveNrc());
					odrServiceCommercial
							.setComponentReferenceName(orderProductComponent.getMstProductComponent().getName());
					odrServiceCommercial.setReferenceName(
							orderProductComponentsAttribute.getProductAttributeMaster().getName());
					odrServiceCommercial.setReferenceType("ATTRIBUTES");
					odrServiceCommercial.setOdrServiceDetail(odrServiceDetail);
					odrServiceCommercial.setServiceType("CrossConnect");
					odrServiceCommercials.add(odrServiceCommercial);
				}
				String category = orderProductComponentsAttribute.getOrderProductComponent().getMstProductComponent().getName();
				String attrName = orderProductComponentsAttribute.getProductAttributeMaster().getName();
				String attrValue = orderProductComponentsAttribute.getAttributeValues();
				LOGGER.info("Npl cross connect odr service attibute name value and catogory {} :"+attrName+","+attrValue+" ,"+category);
				if (attrName.equals("Bandwidth") && StringUtils.isNotBlank(attrValue)) {
					odrServiceDetail.setBwPortspeed(attrValue);
					odrServiceDetail.setBwPortspeedAltName(attrValue);
					odrServiceDetail.setBwUnit("Mbps");
					String[] bw = attrValue.split(" ");
					if(bw!=null) {
						flowMapper.put("Bandwidth", bw[0]);
					}
				}else if(attrName.equals("CROSS_CONNECT_LOCAL_DEMARCATION_ID") && StringUtils.isNotBlank(attrValue)) {
					processDemarcationAndLocalITContacctForServiceDetail(attrValue,odrServiceDetail,flowMapper);
				}else if (attrName.equals("GSTNO_A") && StringUtils.isNotBlank(attrValue)) {
					flowMapper.put("GSTNO_A", attrValue);
					odrServiceDetail.setBillingGstNumber(attrValue);
				}else if (attrName.equals("GSTNO_Z") && StringUtils.isNotBlank(attrValue)) {
					flowMapper.put("GSTNO_B", attrValue);
				}else if (attrName.equals("Interface") && StringUtils.isNotBlank(attrValue)) {
					flowMapper.put("Interface Type - A end", attrValue);
				} else if (attrName.equals("Interface B") && StringUtils.isNotBlank(attrValue)) {
					flowMapper.put("Interface Type - B end", attrValue);
				} else if (attrName.equals("A end Address Type") && StringUtils.isNotBlank(attrValue)) {
					flowMapper.put("A end Address Type", attrValue);
				}else if (attrName.equals("Z end Address Type") && StringUtils.isNotBlank(attrValue)) {
					flowMapper.put("B end Address Type", attrValue);
				}else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
					flowMapper.put("GST_ADDRESS", attrValue);
					Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
							.findById(Integer.valueOf(attrValue));
					if (addServiceParam.isPresent()) {
						OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
						odrAdditionalServiceParam.setAttribute("GST_ADDRESS");
						odrAdditionalServiceParam.setCategory(null);
						odrAdditionalServiceParam.setCreatedBy(userName);
						odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
						odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
						odrAdditionalServiceParam.setValue(addServiceParam.get().getValue());
						odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

						OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
								.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, odrServiceDetail.getId(),
										category);
						if (odrServiceAttribute == null) {
							odrServiceAttribute = new OdrServiceAttribute();
						}
						odrServiceAttribute.setAttributeAltValueLabel(attrValue);
						odrServiceAttribute.setAttributeName(attrName);
						odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
						odrServiceAttribute.setCategory(category);
						odrServiceAttribute.setCreatedBy(userName);
						odrServiceAttribute.setCreatedDate(new Date());
						odrServiceAttribute.setIsActive(CommonConstants.Y);
						odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
						odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
						odrServiceAttribute.setUpdatedBy(userName);
						odrServiceAttribute.setUpdatedDate(new Date());
						odrServiceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
					}
				}/*else if(attrName.equalsIgnoreCase("Cross Connect Type") || attrName.equalsIgnoreCase("Media Type")||
						attrName.equalsIgnoreCase("Do you want fiber entry") || attrName.equalsIgnoreCase("Type of Fibre Entry")||
						attrName.equalsIgnoreCase("Fiber Type") || attrName.equalsIgnoreCase("Service Credits - uptime")||
						attrName.equalsIgnoreCase("No. of Fiber pairs") || attrName.equalsIgnoreCase("No. of Cable pairs")) {
					saveCrossConnectAttributesInScService(odrServiceDetail,attrName,attrValue);
				}*/else {
					OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
							.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, odrServiceDetail.getId(),
									category);
					if (odrServiceAttribute == null) {
						odrServiceAttribute = new OdrServiceAttribute();
					}
				LOGGER.info("Other Npl cross connect odr service attibute name value and catogory {} :"+attrName+","+attrValue+" ,"+category);
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(attrValue);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(userName);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(userName);
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				}
			}
		}
		if (!isUpdate)
			processCrossConnectFeasibility(odrServiceDetail, userName,flowMapper);
		return odrServiceDetail;
	}
	/*private void saveCrossConnectAttributes(OdrServiceDetail odrServiceDetail,String attrName,String attrValue){

		if(attrName.equalsIgnoreCase("Cross Connect Type") && StringUtils.isNotBlank(attrValue)) {
			updateOdrServiceAttribute(odrServiceDetail);
		}else if (odrServiceDetail.getOdrOrder().getCrossConnectType()!=null && "Passive".equalsIgnoreCase(odrServiceDetail.getOdrOrder().getCrossConnectType())
				&& attrName.equals("Media Type") && StringUtils.isNotBlank(attrValue)) {
			odrServiceDetail.setMediaType(attrValue!=null?attrValue:null);
		}else if (attrName.equalsIgnoreCase("Do you want fiber entry") && StringUtils.isNotBlank(attrValue)) {
			odrServiceDetail.setFiberEntryNeeded(attrValue!=null?attrValue:null);
		}else if (attrName.equalsIgnoreCase("Type of Fibre Entry") && StringUtils.isNotBlank(attrValue)) {
			odrServiceDetail.setFiberEntryType(attrValue!=null?attrValue:null);
		}else if (attrName.equalsIgnoreCase("Fiber Type") && StringUtils.isNotBlank(attrValue)) {
			odrServiceDetail.setFiberType(attrValue!=null?attrValue:null);
		}else if (attrName.equalsIgnoreCase("Service Credits - uptime") && StringUtils.isNotBlank(attrValue)) {
			odrServiceDetail.setServiceCreditUptime(attrValue!=null?attrValue:null);
		}else if (odrServiceDetail.getOdrOrder().getCrossConnectType()!=null && "Passive".equalsIgnoreCase(odrServiceDetail.getOdrOrder().getCrossConnectType())
				&& attrName.equalsIgnoreCase("No. of Fiber pairs") && StringUtils.isNotBlank(attrValue)) {
			odrServiceDetail.setFiberPairNumber(attrValue!=null?attrValue:null);
		}else if (attrName.equalsIgnoreCase("No. of Cable pairs") && StringUtils.isNotBlank(attrValue)) {
			odrServiceDetail.setCablePairNumber(attrValue!=null?attrValue:null);
		}
	}*/
	public void processCrossConnectFeasibility(OdrServiceDetail odrServiceDetail,
											   String userName,Map<String, String> flowMapper) throws ParseException {
		LOGGER.info("Inside npl odr service processCrossConnectFeasibility");
		flowMapper.put("lmTypeA", "Onnet Wireline");
		flowMapper.put("lmTypeB", "Onnet Wireline");
		flowMapper.put("lmScenarioTypeA", "Onnet Wireline - Connected Customer");
		flowMapper.put("lmConnectionTypeA", "Wireline");
		flowMapper.put("lmScenarioTypeB", "Onnet Wireline - Connected Customer");
		flowMapper.put("lmConnectionTypeB", "Wireline");
		flowMapper.put("popSiteCodeA", "");
		flowMapper.put("popSiteCodeB", "");
		flowMapper.put("lastMileProvider", "");


		OdrServiceAttribute OdrServiceAttribute = new OdrServiceAttribute();
		OdrServiceAttribute.setAttributeAltValueLabel("Onnet Wireline");
		OdrServiceAttribute.setAttributeName("lm_type_a");
		OdrServiceAttribute.setAttributeValue("Onnet Wireline");
		OdrServiceAttribute.setCategory("FEASIBILITY");
		OdrServiceAttribute.setCreatedBy(userName);
		OdrServiceAttribute.setCreatedDate(new Date());
		OdrServiceAttribute.setIsActive(CommonConstants.Y);
		OdrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
		OdrServiceAttribute.setUpdatedBy(userName);
		OdrServiceAttribute.setUpdatedDate(new Date());
		odrServiceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
		OdrServiceAttribute = new OdrServiceAttribute();
		OdrServiceAttribute.setAttributeAltValueLabel("Onnet Wireline");
		OdrServiceAttribute.setAttributeName("lm_type_b");
		OdrServiceAttribute.setAttributeValue("Onnet Wireline");
		OdrServiceAttribute.setCategory("FEASIBILITY");
		OdrServiceAttribute.setCreatedBy(userName);
		OdrServiceAttribute.setCreatedDate(new Date());
		OdrServiceAttribute.setIsActive(CommonConstants.Y);
		OdrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
		OdrServiceAttribute.setUpdatedBy(userName);
		OdrServiceAttribute.setUpdatedDate(new Date());
		odrServiceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
		OdrServiceAttribute = new OdrServiceAttribute();
		OdrServiceAttribute.setAttributeAltValueLabel("1");
		OdrServiceAttribute.setAttributeName("a_connected_cust_tag");
		OdrServiceAttribute.setAttributeValue("1");
		OdrServiceAttribute.setCategory("FEASIBILITY");
		OdrServiceAttribute.setCreatedBy(userName);
		OdrServiceAttribute.setCreatedDate(new Date());
		OdrServiceAttribute.setIsActive(CommonConstants.Y);
		OdrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
		OdrServiceAttribute.setUpdatedBy(userName);
		OdrServiceAttribute.setUpdatedDate(new Date());
		odrServiceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
		OdrServiceAttribute = new OdrServiceAttribute();
		OdrServiceAttribute.setAttributeAltValueLabel("1");
		OdrServiceAttribute.setAttributeName("a_connected_building_tag");
		OdrServiceAttribute.setAttributeValue("1");
		OdrServiceAttribute.setCategory("FEASIBILITY");
		OdrServiceAttribute.setCreatedBy(userName);
		OdrServiceAttribute.setCreatedDate(new Date());
		OdrServiceAttribute.setIsActive(CommonConstants.Y);
		OdrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
		OdrServiceAttribute.setUpdatedBy(userName);
		OdrServiceAttribute.setUpdatedDate(new Date());
		odrServiceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
		OdrServiceAttribute = new OdrServiceAttribute();
		OdrServiceAttribute.setAttributeAltValueLabel("1");
		OdrServiceAttribute.setAttributeName("b_connected_cust_tag");
		OdrServiceAttribute.setAttributeValue("1");
		OdrServiceAttribute.setCategory("FEASIBILITY");
		OdrServiceAttribute.setCreatedBy(userName);
		OdrServiceAttribute.setCreatedDate(new Date());
		OdrServiceAttribute.setIsActive(CommonConstants.Y);
		OdrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
		OdrServiceAttribute.setUpdatedBy(userName);
		OdrServiceAttribute.setUpdatedDate(new Date());
		odrServiceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
		OdrServiceAttribute = new OdrServiceAttribute();
		OdrServiceAttribute.setAttributeAltValueLabel("1");
		OdrServiceAttribute.setAttributeName("b_connected_building_tag");
		OdrServiceAttribute.setAttributeValue("1");
		OdrServiceAttribute.setCategory("FEASIBILITY");
		OdrServiceAttribute.setCreatedBy(userName);
		OdrServiceAttribute.setCreatedDate(new Date());
		OdrServiceAttribute.setIsActive(CommonConstants.Y);
		OdrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
		OdrServiceAttribute.setUpdatedBy(userName);
		OdrServiceAttribute.setUpdatedDate(new Date());
		odrServiceDetail.getOdrServiceAttributes().add(OdrServiceAttribute);
		LOGGER.info("After npl odr service processCrossConnectFeasibility method {} :",odrServiceDetail.getOdrServiceAttributes());
	}

	private void processDemarcationAndLocalITContacctForServiceDetail(String ccLocMappingId, OdrServiceDetail serviceDetail, Map<String,String> flowMapper) {
		LOGGER.info("MDC Filter token value in before Queue call processDemarcationAndLocalITContacctForServiceDetail {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		CrossConnectLocalITDemarcationBean crossConnectLocalITDemarcation = getCrossConnectDemarcationAndLocalITContact(ccLocMappingId);
		LOGGER.info("Cross connect demarcation and localIt contact {} :",crossConnectLocalITDemarcation);
		if (crossConnectLocalITDemarcation != null) {
			if (crossConnectLocalITDemarcation.getDemarcations() != null && !crossConnectLocalITDemarcation.getDemarcations().isEmpty()) {
				for (CrossConnectDemarcations crossConnectDemarcations : crossConnectLocalITDemarcation.getDemarcations()) {
					crossConnectDemarcations.getSiteA().stream().forEach(demarcationBean -> {
						LOGGER.info("CrossConnectDemarcations details for site A {} :",
								demarcationBean.toString());

						addCrossConnectDemarcationInServiceDetail(demarcationBean, serviceDetail, flowMapper,"A");
					});
					crossConnectDemarcations.getSiteZ().stream().forEach(demarcationBean -> {
						LOGGER.info("CrossConnectDemarcations details for site B {} :",
								demarcationBean.toString());
						addCrossConnectDemarcationInServiceDetail(demarcationBean, serviceDetail,flowMapper,"B");
					});
					if (crossConnectLocalITDemarcation.getContacts() != null && !crossConnectLocalITDemarcation.getContacts().isEmpty()) {
						for (CrossConnectLocalITContacts crossConnectLocalItContacts : crossConnectLocalITDemarcation.getContacts()) {
							crossConnectLocalItContacts.getSiteA().stream().forEach(locationItContact -> {
								LOGGER.info("CrossConnectLocalIT details for site A {} :",
										locationItContact.toString());
								addCrossConnectLocalITContactInServiceDetal(locationItContact, serviceDetail, flowMapper,"A");
							});
							crossConnectLocalItContacts.getSiteZ().stream().forEach(locationItContact -> {
								LOGGER.info("CrossConnectLocalIT details for site B {} :",
										locationItContact.toString());
								addCrossConnectLocalITContactInServiceDetal(locationItContact, serviceDetail, flowMapper,"B");
							});
						}
					}
				}
			}
		}
	}
	private void addCrossConnectDemarcationInServiceDetail(DemarcationBean demarcationBean, OdrServiceDetail odrServiceDetail, Map<String,String> flowMapper, String siteType){
		{
			LOGGER.info("Inside addCrossConnectDemarcationInServiceDetail details for site type "+siteType);
			if(siteType.equalsIgnoreCase("A")) {
				odrServiceDetail.setDemarcationApartment(
						demarcationBean.getBuildingName() != null ? (String) demarcationBean.getBuildingName() : null);
				odrServiceDetail.setDemarcationFloor(
						demarcationBean.getFloor() != null ? (String) demarcationBean.getFloor() : null);
				odrServiceDetail.setDemarcationRack(
						demarcationBean.getWing() != null ? (String) demarcationBean.getWing() : null);
				odrServiceDetail.setDemarcationRoom(
						demarcationBean.getRoom() != null ? (String) demarcationBean.getRoom() : null);
			}
			flowMapper.put("BUILDING_NAME_"+siteType,demarcationBean.getBuildingName() != null ? (String) demarcationBean.getBuildingName() : null);
			flowMapper.put("FLOOR_"+siteType,demarcationBean.getFloor() != null ? (String) demarcationBean.getFloor() : null);
			flowMapper.put("WING_"+siteType,demarcationBean.getWing() != null ? (String) demarcationBean.getWing() : null);
			flowMapper.put("ROOM_"+siteType,demarcationBean.getRoom() != null ? (String) demarcationBean.getRoom() : null);

		}
	}
	private void addCrossConnectLocalITContactInServiceDetal(LocationItContact locationItContact, OdrServiceDetail odrServiceDetail, Map<String,String> flowMapper, String siteType){
		LOGGER.info("Inside addCrossConnectLocalITContactInServiceDetal details for site type "+siteType);
		if(siteType.equalsIgnoreCase("A")) {
			odrServiceDetail.setLocalItContactEmail(locationItContact.getEmail() != null ? (String) locationItContact.getEmail() : null);
			odrServiceDetail.setLocalItContactMobile(locationItContact.getContactNo() != null ? (String) locationItContact.getContactNo() : null);
			odrServiceDetail.setLocalItContactName(locationItContact.getName() != null ? (String) locationItContact.getName() : null);
		}

		flowMapper.put("EMAIL_"+siteType,locationItContact.getEmail() != null ? (String) locationItContact.getEmail() : null);
		flowMapper.put("CONTACT_NO_"+siteType,locationItContact.getContactNo() != null ? (String) locationItContact.getContactNo() : null);
		flowMapper.put("NAME_"+siteType,locationItContact.getName() != null ? (String) locationItContact.getName() : null);

	}

	private Map<String, String> getCrossConnectOrderStatus(OrderIllSite orderIllSite) {
		Map<String, String> flowMapper = new HashMap<>();
		Optional<OrderProductSolution> prodSolutionOpt = orderProductSolutionRepository
				.findById(orderIllSite.getOrderProductSolution().getId());
		LOGGER.info("Inside getCrossConnectOrderStatus {} :",orderIllSite);
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
	private void processOeCrossConnectSite(String username, OrderIllSite orderIllSite, Map<String, String> flowMapper,
							   OdrServiceDetail odrServiceDetail) throws TclCommonException, ParseException {
		if (Objects.nonNull(orderIllSite)) {
			LOGGER.info("Entering into processOeCrossConnectSite for  Site-A");
			persistCrossConnectSiteDemarcationDetails(username, flowMapper, odrServiceDetail,
					orderIllSite.getErfLocSitebLocationId(), orderIllSite.getId(), "Site-A");

			LOGGER.info("Entering into processOeCrossConnectSite for  Site-B");
			persistCrossConnectSiteDemarcationDetails(username, flowMapper, odrServiceDetail,
					orderIllSite.getErfLocSitebLocationId(), orderIllSite.getId(), "Site-B");
		}

	}
	private void persistCrossConnectSiteDemarcationDetails(String username, Map<String, String> flowMapper,
						OdrServiceDetail odrServiceDetail, Integer locationId, Integer siteId, String type) throws TclCommonException, IllegalArgumentException, ParseException {

		LOGGER.info("Entering into persistCrossConnectSiteDemarcationDetails with site type"+type);
		String siteType = "A";
		if (type.equals("Site-B")) {
			siteType = "B";
		}
				Set<OdrServiceDetailBean> odrServiceBeanDetails = new HashSet<>();
				if (!odrServiceDetail.getFlowStatus().equals("NEW")) {
					List<OdrComponentAttribute> odrComponentAttributes = new ArrayList<>();
					OdrComponent odrComponent = new OdrComponent();
					List<OdrComponent> odrComponents = odrComponentRepository
							.findByOdrServiceDetailAndComponentNameAndSiteType(odrServiceDetail, "LM", siteType);
					LOGGER.info("Getting findByOdrServiceDetailAndComponentNameAndSiteType values {} :",odrComponents);
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
					processCrossConnectDemarcationLocalItContact(siteId, odrServiceDetail, type, odrComponent,
							odrComponentAttributes);
					odrComponentAttributeRepository.saveAll(odrComponentAttributes);
					List<OmsAttachment> omsAttachments = omsAttachmentRepository
							.findByReferenceNameAndReferenceId("Site", siteId);
					for (OmsAttachment omsAttachment : omsAttachments) {
						Integer erfAttachmentId = omsAttachment.getErfCusAttachmentId();
						LOGGER.info("MDC Filter token value in before Queue call crossConnect getAttachment {} :",
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
									LOGGER.info("Already crossConnect Attachment and odrAttachment is found , so updating {}",
											attachmentObj);
									attachment.setId((Integer) attachmentObj.get("attachment_id"));
									odrAttachment.setId((Integer) attachmentObj.get("odr_attachment_id"));
								} else {
									LOGGER.info(
											"Creating a new crossConnect attachment as already the attachment object is not found");
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


					LOGGER.info("Updated the service Detail for crossConnect {}", odrServiceDetail.getId());

					if (!odrServiceBeanDetails.isEmpty()) {
						OdrOrderBean odrOrderBean = new OdrOrderBean();
						odrOrderBean.setOdrServiceDetails(odrServiceBeanDetails);
						LOGGER.info("MDC Filter token value in before Queue call crossConnect fulfillment {} :",
								MDC.get(CommonConstants.MDC_TOKEN_KEY));
						mqUtils.send(orderEnrichmentFulfillQueue, Utils.convertObjectToJson(odrOrderBean));
					}
				}

	}

	private OdrServiceDetail processCrossConnectDemarcationLocalItContact(Integer refId, OdrServiceDetail odrServiceDetail, String type,
												   OdrComponent odrComponent, List<OdrComponentAttribute> odrComponentAttributes)
			throws TclCommonException, IllegalArgumentException, ParseException {
		LOGGER.info("Cross connect starting to save the localItContact flow with refId {} refname {}", refId, OdrConstants.ILLSITES);
		List<OrderProductComponent> oderProdCompomponents = orderProductComponentRepository
				.findByReferenceIdInAndReferenceName(refId, OdrConstants.ILLSITES);
		LOGGER.info("All the Attributes retrived {}", oderProdCompomponents);
		for (OrderProductComponent orderProductComponent : oderProdCompomponents) {
			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue:orderProductComponent.getOrderProductComponentsAttributeValues()){
				String attrName = orderProductComponentsAttributeValue.getProductAttributeMaster().getName();
			    String attrValue = orderProductComponentsAttributeValue.getAttributeValues();
			    String category = orderProductComponentsAttributeValue.getOrderProductComponent().getMstProductComponent().getName();

				LOGGER.info("Cross connect attributeName attributeValue category"+attrName+" ,"+attrValue+", "+category);
			if (attrName.equals("CROSS_CONNECT_LOCAL_DEMARCATION_ID") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("Cross connect cc location mapping primary key "+attrValue);
				processDemarcationAndLocalITContacctForOdrComponet(attrValue,odrServiceDetail,odrComponent,odrComponentAttributes);
			} else if (attrName.equals("GSTNO_A") && type.equalsIgnoreCase("A") && StringUtils.isNotBlank(attrValue)) {
				odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "siteGstNumber",
						attrValue));
			}
			else if (attrName.equals("GSTNO_Z") && type.equalsIgnoreCase("B") && StringUtils.isNotBlank(attrValue)) {
				odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "siteGstNumber",
						attrValue));
			}/*  Commented since GST_ADDRESS attribute is not availbale in Crossconnect
			else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("GST_ADDRESS");
					odrAdditionalServiceParam.setCategory(null);
					odrAdditionalServiceParam.setCreatedBy(odrServiceDetail.getCreatedBy());
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(addServiceParam.get().getValue());
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
							.findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName, odrServiceDetail.getId(),
									category);
					if (odrServiceAttribute == null) {
						odrServiceAttribute = new OdrServiceAttribute();
					}
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(odrServiceDetail.getCreatedBy());
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(odrServiceDetail);
					odrServiceAttribute.setUpdatedBy(odrServiceDetail.getCreatedBy());
					odrServiceAttribute.setUpdatedDate(new Date());
					odrServiceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
//					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, serviceDetail, "siteGstAddress",
//							addServiceParam.get().getValue(), serviceDetail.getCreatedBy()));
					odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "siteGstAddressId",
							attrValue, odrServiceDetail.getCreatedBy()));
					odrServiceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				}
			}*/ else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, attrName,
						CommonConstants.Y, odrServiceDetail.getCreatedBy()));
				odrComponentAttributes.add(constructOdrComponentAttribute(odrComponent, odrServiceDetail, "taxExemption",
						CommonConstants.Y, odrServiceDetail.getCreatedBy()));
			}
		  }
		}
		return odrServiceDetail;
	}
	private void processDemarcationAndLocalITContacctForOdrComponet(String ccLocMappingId, OdrServiceDetail odrServiceDetail,OdrComponent odrComponent, List<OdrComponentAttribute> odrComponentAttributes ) {
		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		CrossConnectLocalITDemarcationBean crossConnectLocalITDemarcation = getCrossConnectDemarcationAndLocalITContact(ccLocMappingId);
		if (crossConnectLocalITDemarcation != null) {
			if (crossConnectLocalITDemarcation.getDemarcations() != null && !crossConnectLocalITDemarcation.getDemarcations().isEmpty()) {
				for (CrossConnectDemarcations crossConnectDemarcations : crossConnectLocalITDemarcation.getDemarcations()) {
					crossConnectDemarcations.getSiteA().stream().forEach(demarcationBean -> {
						LOGGER.info("CrossConnectDemarcations details for site A {} :",
								demarcationBean.toString());
						addCrossConnectDemarcationInOdrComponentAttr(demarcationBean, odrServiceDetail,odrComponent,odrComponentAttributes,"A");
					});
					crossConnectDemarcations.getSiteZ().stream().forEach(demarcationBean -> {
						LOGGER.info("CrossConnectDemarcations details for site B {} :",
								demarcationBean.toString());
						addCrossConnectDemarcationInOdrComponentAttr(demarcationBean, odrServiceDetail,odrComponent,odrComponentAttributes,"B");
					});
					if (crossConnectLocalITDemarcation.getContacts() != null && !crossConnectLocalITDemarcation.getContacts().isEmpty()) {
						for (CrossConnectLocalITContacts crossConnectLocalItContacts : crossConnectLocalITDemarcation.getContacts()) {
							crossConnectLocalItContacts.getSiteA().stream().forEach(locationItContact -> {
								LOGGER.info("CrossConnectLocalIT details for site A {} :",
										locationItContact.toString());
								addCrossConnectLocalITContactInOdrComponentAttr(locationItContact, odrServiceDetail,odrComponent,odrComponentAttributes,"A");
							});
							crossConnectLocalItContacts.getSiteZ().stream().forEach(locationItContact -> {
								LOGGER.info("CrossConnectLocalIT details for site B {} :",
										locationItContact.toString());
								addCrossConnectLocalITContactInOdrComponentAttr(locationItContact, odrServiceDetail,odrComponent,odrComponentAttributes,"B");
							});
						}
					}
				}
			}
		}
	}
	private void addCrossConnectDemarcationInOdrComponentAttr(DemarcationBean demarcationBean,
					OdrServiceDetail odrServiceDetail,OdrComponent odrComponent, List<OdrComponentAttribute> odrComponentAttributes, String siteType){ {

	        LOGGER.info("CrossConnectLocalIT details for site type {} :",siteType);
			if(siteType.equalsIgnoreCase("A")) {
				odrServiceDetail.setDemarcationApartment(
						demarcationBean.getBuildingName() != null ? (String) demarcationBean.getBuildingName() : null);
				odrServiceDetail.setDemarcationFloor(
						demarcationBean.getFloor() != null ? (String) demarcationBean.getFloor() : null);
				odrServiceDetail.setDemarcationRack(
						demarcationBean.getWing() != null ? (String) demarcationBean.getWing() : null);
				odrServiceDetail.setDemarcationRoom(
						demarcationBean.getRoom() != null ? (String) demarcationBean.getRoom() : null);
			}
			odrComponentAttributes
					.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "demarcationBuildingName",
							demarcationBean.getBuildingName() != null ? (String) demarcationBean.getBuildingName() : null));

			odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "demarcationFloor",
					demarcationBean.getFloor() != null ? (String) demarcationBean.getFloor() : null));
			odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "demarcationWing",
					demarcationBean.getWing() != null ? (String) demarcationBean.getWing() : null));
			odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "demarcationRoom",
					demarcationBean.getRoom() != null ? (String) demarcationBean.getRoom() : null));
		}
	}
	private void addCrossConnectLocalITContactInOdrComponentAttr(LocationItContact locationItContact,
							OdrServiceDetail odrServiceDetail,OdrComponent odrComponent, List<OdrComponentAttribute> odrComponentAttributes, String siteType){
		LOGGER.info("CrossConnectLocalIT details for site type {} :",siteType);
		if(siteType.equalsIgnoreCase("A")) {
			odrServiceDetail.setLocalItContactEmail(locationItContact.getEmail() != null ? (String) locationItContact.getEmail() : null);
			odrServiceDetail.setLocalItContactMobile(locationItContact.getContactNo() != null ? (String) locationItContact.getContactNo() : null);
			odrServiceDetail.setLocalItContactName(locationItContact.getName() != null ? (String) locationItContact.getName() : null);
		}
		odrComponentAttributes
				.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent,"localItContactEmailId",
						locationItContact.getEmail() != null ? (String) locationItContact.getEmail() : null));
		odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "localItContactMobile",
				locationItContact.getContactNo() != null ? (String) locationItContact.getContactNo() : null));
		odrComponentAttributes
				.add(persistOdrComponentAttributes(odrServiceDetail,odrComponent, "localItContactName",
						locationItContact.getName() != null ? (String) locationItContact.getName(): null));
	}
	
	
	private void persistComponentAttrForTermination(OdrServiceDetail primaryserviceDetail,
			OdrComponent primaryOdrComponent, List<OdrComponentAttribute> odrComponentAttributes,
			OrderIllSiteToService orderIllSiteToService, String siteType) {
		LOGGER.info("NPL Private Line srvSiteType {}", siteType);
		List<SIServiceDetailsBean> serviceDetailsList = null;
		String orderDetailsQueueResponse = null;
		try {
			orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siNplServiceDetailsInactiveQueue, orderIllSiteToService.getErfServiceInventoryTpsServiceId());

			LOGGER.info("Response received in OMS getServiceDetailNPL: {}",orderDetailsQueueResponse);
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
			serviceDetailsList.stream().filter(serDet -> "SiteA".equalsIgnoreCase(serDet.getSrvSiteType())).forEach(serviceDetail -> {
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "contractEndDate",serviceDetail.getCircuitExpiryDate() != null ? DateUtil.convertDateToString(serviceDetail.getCircuitExpiryDate()) : null));
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
				
			});}
	}


}
