package com.tcl.dias.batch.odr.gsip;

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
import java.util.stream.IntStream;

import org.apache.commons.lang.WordUtils;
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
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.GscCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
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
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.entities.OdrAsset;
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
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OdrAdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.OdrAssetRepository;
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
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class WebexGscOdrService extends OrderService{
    
	private static final Logger LOGGER = LoggerFactory.getLogger(WebexGscOdrService.class);
    
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
    com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository OrderProductComponentsAttributeValueRepository;

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

    @Value("${oms.gsc.iso.country.code}")
    String countryCodeQueue;

    @Autowired
    MstProductOfferingRepository mstProductOfferingRepository;

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderGscTfnRepository orderGscTfnRepository;

    @Autowired
    OdrAssetRepository odrAssetRepository;

    private final String ILL_CODE = "0300";
    private final String GVPN_CODE = "6230";
    private final String IPC_CODE = "0000";
    private final String GSC_CODE = "0002";
    private final String CLOUD = "CLOUD";
    private final String ACCESS = "ACCESS";
    private final String ADDON = "ADDON";

    protected String getReferenceName() {
        return OdrConstants.GSC;
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
                List<OdrAsset> odrAssets=new ArrayList<>();
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
                            odrServiceCommercials, odrAttributes,odrAssets);
                    LOGGER.info("End of Order Repository");
                }
                LOGGER.info("Saving all the Order Repository");
                LOGGER.info("OdrServiceDetails Size Count "+odrServiceDetails.size());
                odrOrderRepository.save(odrOrder);
                odrOrderAttributeRepository.saveAll(odrAttributes);
                odrContractInfoRepository.saveAll(odrContractInfos);

                odrServiceDetailRepository.saveAll(odrServiceDetails);
                odrAssetRepository.saveAll(odrAssets);
                
                processGscParentService(odrOrder, username, odrContractInfos.get(0), odrServiceDetails);

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
                /*odrServiceDetailRepository.saveAll(odrServiceDetails);*/
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
                                     String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes,List<OdrAsset> odrAssets) {
        for (OrderToLeProductFamily orderToLeProdFamily : orderToLe.getOrderToLeProductFamilies()) {
            LOGGER.info("Product Family Count:"+orderToLe.getOrderToLeProductFamilies().size());
           LOGGER.info("Product Family:"+orderToLeProdFamily.getMstProductFamily().getName());
            flowMapper.put("productName", orderToLeProdFamily.getMstProductFamily().getName());
            flowMapper.put("productCode", orderToLeProdFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
            processProductSolution(orderToLe, flowMapper, orderToLeProdFamily, odrServiceDetails, odrContractInfo,
                    odrOrder, username, odrServiceCommercials, odrAttributes,odrAssets);
            LOGGER.info("End of Product Family"+orderToLeProdFamily.getMstProductFamily().getName());
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
                                       List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes, List<OdrAsset> odrAssets) {

        for (OrderProductSolution orderProductSolution : orderToLeProdFamily.getOrderProductSolutions()) {
            LOGGER.info("Product Solution :"+orderProductSolution.getMstProductOffering().getProductName());
            if ("MACD".equals(orderToLe.getOrderType())) {
                if(odrOrder.getOpOrderCode().contains(OdrConstants.GSC)){
                    LOGGER.info("Processing MACD GSC :"+orderToLeProdFamily.getMstProductFamily().getName());
                    processGsc(flowMapper,orderProductSolution,odrServiceDetails,odrContractInfo,odrOrder,username,odrServiceCommercials,odrAttributes,odrAssets);
					/* Needs to be implemented
					LOGGER.info("MACD IPC Flow, setting uuid for ODR::" + orderToLe.getOrder().getQuote().getQuoteToLes()
							.stream().findFirst().get().getErfServiceInventoryTpsServiceId());*/
					/*flowMapper.put("serviceCode", orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
							.getErfServiceInventoryTpsServiceId());*/
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
            if (orderToLeProdFamily.getMstProductFamily().getName().equalsIgnoreCase(OdrConstants.GSIP)) {
                /* Needs to be implemented*/
                LOGGER.info("Processing GSC :"+orderToLeProdFamily.getMstProductFamily().getName());

                processGsc(flowMapper,orderProductSolution,odrServiceDetails,odrContractInfo,odrOrder,username,odrServiceCommercials,odrAttributes,odrAssets);
            } else {
                flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
                processSite(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username, odrServiceCommercials);
               /* processLink(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username);*/
            }
        }
        LOGGER.info("end of product solution");
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
    private String getServiceCode(Map<String, String> flowMapper, String cityCode,OrderGscDetail gscDetail) {
        String prodCode = CommonConstants.EMPTY;
        if (flowMapper.get("productName").equals("IAS")) {
            prodCode = ILL_CODE;
        } else if (flowMapper.get("productName").equals("IPC")) {
            prodCode = IPC_CODE;
        } else if(flowMapper.get("productName").equals("GSIP")) {
            prodCode = GSC_CODE;
        }
        else {
            prodCode = GVPN_CODE;
        }
        if (cityCode == null)
            cityCode = "";
        String primaryKey = "";
        String serviceCode = "091" + cityCode.toUpperCase() + prodCode + "A";


        if(!flowMapper.get("productName").equals("GSIP")) {
            OdrServiceDetail odrServiceDetail= odrServiceDetailRepository.findTopByOrderByIdDesc();
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

    public void processIpcCommonComponentAttr(OrderCloud orderCloud, OdrServiceDetail serviceDetail, String username,
                                              Map<String, String> flowMapper) throws TclCommonException, IllegalArgumentException {

        // LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference
        // Id {}", refId, getReferenceName());

        List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
                .findByRefidAndRefNameForGsc(orderCloud.getId().toString(), getReferenceName());

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
        odrProductDetail.setCreatedBy(username);
        odrProductDetail.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        odrProductDetail.setIsActive(CommonConstants.Y);
        odrProductDetail.setCloudCode(orderCloud.getCloudCode());
        odrProductDetail.setParentCloudCode(orderCloud.getParentCloudCode());
        processCommercialComponent(String.valueOf(orderCloud.getId()), odrProductDetail, username, flowMapper);

        for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
            String category = oderProdCompAttr.get("category");
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

    public void processCommonComponentAttr(String refId, OdrServiceDetail serviceDetail, String username,OdrComponent odrComponent) throws TclCommonException, IllegalArgumentException {
        LOGGER.info("Querying for Common Component for reference Name {} and reference Id {}",  getReferenceName(),refId
               );
        List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
                .findByRefidAndRefNameForGsc(refId, getReferenceName());
        String terminationNumber = null;
        String terminationAttr = null;
        for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
            String category = oderProdCompAttr.get("category");
            String attrName = oderProdCompAttr.get("attrName");
            String attrValue = oderProdCompAttr.get("attrValue");
            LOGGER.info("attributeName:"+attrName);
            LOGGER.info("attributeValue:"+attrValue);
            if(Objects.nonNull(attrValue))
                attrValue=attrValue.trim();
            if (attrName.contains("Termination Number") && StringUtils.isNotBlank(attrValue)) {
            	terminationAttr = attrName;
            	if(terminationNumber == null) {
            		terminationNumber = attrValue;
            	} else {
            		terminationNumber = terminationNumber + "," + attrValue;
            	}
            } else {
            	odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,attrName,attrValue));
            }
        }
        if(terminationNumber != null) {
        	odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,terminationAttr,terminationNumber));
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
                if (attrOrderPrice != null && attrOrderPrice.getEffectiveUsagePrice() != null) {
                    addServiceAttr(serviceDetail, userName, attrOrderPrice.getEffectiveUsagePrice(),
                            orderProductComponent.getMstProductComponent().getName(), "usage_price_per_mb");
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

            if (attrName.equals("Burstable Bandwidth") && StringUtils.isNotBlank(attrValue)) {
                serviceDetail.setBurstableBwPortspeed(attrValue.trim());
                serviceDetail.setBurstableBwPortspeedAltName(attrValue.trim());
                serviceDetail.setBurstableBwUnit("Mbps");
                odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBandwidth",attrValue.trim()));
                odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBwUnit","Mbps"));
            } else if (attrName.equals("Port Bandwidth") && StringUtils.isNotBlank(attrValue)) {
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
                String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue, attrValue);
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
                .findByRefidAndRefNameForGsc(refId, getReferenceName());
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
                    GstAddressBean gstAddress = Utils.convertJsonToObject(address, GstAddressBean.class);

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
                    serviceDetail.setOdrGstAddress(odrGstAddress);
                    //odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstAddress",address));
                    odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstAddressId",attrValue.trim()));
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

                String isTaxExept[] = { "" };
                odrAttributes.stream().filter(odrOrderAttr -> "isTaxExemption".equals(odrOrderAttr.getAttributeName()))
                        .forEach(odrOrderAttr -> {
                            if ("Yes".equals(odrOrderAttr.getAttributeValue())) {
                                isTaxExept[0] = "Y";
                            } else if ("no".equals(odrOrderAttr.getAttributeValue().toLowerCase())) {
                                isTaxExept[0] = "N";
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
                    flowMapper.put("serviceCode", getServiceCode(flowMapper, cityCode,null));
                    LOGGER.info("Generated service code : {}", getServiceCode(flowMapper, cityCode,null));
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
     */
    public void processOrder(Order order, OdrOrder odrOrder, String userName) {
        odrOrder.setCreatedBy(userName);
        odrOrder.setCreatedDate(new Date());
        odrOrder.setDemoFlag(order.getIsDemo() == CommonConstants.BACTIVE ? CommonConstants.Y : CommonConstants.N);
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

    /**
     * Method to process gsc product solution
     * @param flowMapper
     * @param orderProductSolution
     * @param odrServiceDetails
     * @param odrContractInfo
     * @param odrOrder
     * @param username
     * @param odrServiceCommercials
     * @param odrAttributes
     */
    public void processGsc(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
                            List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
                            String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes, List<OdrAsset> odrAssets){
        LOGGER.info("Entering Process GSC ");
        final List<OrderGsc> orderGsc = orderGscRepository.findByorderProductSolution(orderProductSolution);
            for(OrderGsc orderGscData:orderGsc)
            {
                String offeringName=null;
                offeringName= orderGscData.getOrderProductSolution().getMstProductOffering().getProductName();
                LOGGER.info("OfferingName"+offeringName);
            for(OrderGscDetail gscDetail:orderGscData.getOrderGscDetails())
            {
                LOGGER.info("OrderGscDetail"+gscDetail.getId());
                try{
                    OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();
                    primaryserviceDetail.setOdrComponents(new HashSet<>());
                    primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
                    primaryserviceDetail.setServiceRefId(odrOrder.getOpOrderCode());
                    primaryserviceDetail.setOdrContractInfo(odrContractInfo);
                    primaryserviceDetail.setOdrOrder(odrOrder);
                    primaryserviceDetail.setServiceCommissionedDate(gscDetail.getCreatedTime());
                    primaryserviceDetail.setMrc(gscDetail.getMrc());
                    primaryserviceDetail.setNrc(gscDetail.getNrc());
                    primaryserviceDetail.setArc(gscDetail.getArc());
                    primaryserviceDetail.setErfPrdCatalogOfferingName(offeringName);
                    primaryserviceDetail.setCreatedBy(username);
                    primaryserviceDetail.setCreatedDate(new Date());
                    primaryserviceDetail.setUpdatedDate(new Date());
                    primaryserviceDetail.setUpdatedBy(username);
                    primaryserviceDetail.setIsActive(CommonConstants.Y);
                    primaryserviceDetail.setIsIzo(CommonConstants.N);
                    primaryserviceDetail.setErfPrdCatalogProductName("GSIP");
                    MstProductFamily mstProductFamily = mstProductFamilyRepository
                            .findByNameAndStatus("GSIP", CommonConstants.BACTIVE);

                    String isTaxExept[] = { "" };
                    odrAttributes.stream().filter(odrOrderAttr -> "isTaxExemption".equals(odrOrderAttr.getAttributeName()))
                            .forEach(odrOrderAttr -> {
                                if ("Yes".equals(odrOrderAttr.getAttributeValue())) {
                                    isTaxExept[0] = "Y";
                                } else if ("no".equals(odrOrderAttr.getAttributeValue().toLowerCase())) {
                                    isTaxExept[0] = "N";
                                }
                            });

                    primaryserviceDetail.setTaxExemptionFlag(isTaxExept[0]);
                    if (Objects.nonNull(mstProductFamily)) {
                        primaryserviceDetail.setErfPrdCatalogProductId(mstProductFamily.getId());
                    }
                    LOGGER.info("MDC Filter token value in before Queue call constructGscDetails {} :",
                            MDC.get(CommonConstants.MDC_TOKEN_KEY));
                    primaryserviceDetail.setAccessType(orderGscData.getAccessType());
                    MstProductOffering productOffering=null;
                    if (Objects.nonNull(mstProductFamily))
                    productOffering=mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,primaryserviceDetail.getErfPrdCatalogOfferingName(),(byte)1);
                    if(Objects.nonNull((productOffering)))
                        primaryserviceDetail.setErfPrdCatalogOfferingId(productOffering.getId());

                    primaryserviceDetail.setSourceCountry(gscDetail.getSrc());
                    primaryserviceDetail.setDestinationCountry(gscDetail.getDest());


                    LOGGER.info("MDC Filter token value in before Queue call getSourceCountryCode {} :",
                            MDC.get(CommonConstants.MDC_TOKEN_KEY));
                    String sourceCountryCode = (String) mqUtils.sendAndReceive(countryCodeQueue,
                            String.valueOf(gscDetail.getSrc()));
                    LOGGER.info("MDC Filter token value in before Queue call getDestinationCountryCode {} :",
                            MDC.get(CommonConstants.MDC_TOKEN_KEY));
                    String destinationCountryCode = (String) mqUtils.sendAndReceive(countryCodeQueue,
                            String.valueOf(gscDetail.getDest()));

                    primaryserviceDetail.setSourceCountryCode(sourceCountryCode);
                    primaryserviceDetail.setSourceCountryCodeRepc(sourceCountryCode);
                    primaryserviceDetail.setDestinationCountryCode(destinationCountryCode);
                    primaryserviceDetail.setDestinationCountryCodeRepc(destinationCountryCode);

                    /*String cityCode = CommonConstants.EMPTY;
                    if (flowMapper.get("serviceCode") == null) {
                        flowMapper.put("serviceCode", getServiceCode(flowMapper, cityCode,gscDetail));
                        LOGGER.info("Generated service code : {}", getServiceCode(flowMapper, cityCode,gscDetail));
                    }
                    primaryserviceDetail.setUuid(flowMapper.get("serviceCode"));*/
                    primaryserviceDetail.setParentUuid("");
                    Order order=orderRepository.findByOrderCode(odrOrder.getOpOrderCode());
                    primaryserviceDetail.setCustOrgNo(getCustomerOrgId(order.getQuote().getQuoteToLes().stream().findFirst().get()));
                    primaryserviceDetail.setSupplOrgNo("");
                    primaryserviceDetail.setGscOrderSequenceId("");
                    primaryserviceDetail.setTpsServiceId("");
                    primaryserviceDetail.setCallType("");
                    primaryserviceDetail.setSourceCity("");
                    primaryserviceDetail.setProductReferenceId(null);

                    OdrComponent primaryOdrComponent = persistOdrComponent(primaryserviceDetail, "LM");
                    primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
                    processCommonComponentAttr(String.valueOf(gscDetail.getId()), primaryserviceDetail, username, primaryOdrComponent);

                    //yet to implement
                    //service Varient, IpAddressProvidedby and additionalIPRequired

                    /*List<OmsAttachment> omsAttachments = omsAttachmentRepository
                            .findByReferenceNameAndReferenceId("Site", gscDetail.getId());*/
                    List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByOrderToLe(orderProductSolution.getOrderToLeProductFamily().getOrderToLe());
                 LOGGER.info("omsAttachments"+omsAttachments.size());

                   /* for (OmsAttachment omsAttachment : omsAttachments) {
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
                    }*/
                   /**/

                  /*  OdrServiceDetail serviceDetail= odrServiceDetailRepository.save(primaryserviceDetail);*/
                    odrAssets.addAll(generateOdrAssets(primaryserviceDetail,gscDetail));

                    /*odrAssetRepository.saveAll(assets);*/
                    odrServiceDetails.add(primaryserviceDetail);
                }
                catch (Exception e) {
                    LOGGER.warn("Error in Process GSC : {}", ExceptionUtils.getStackTrace(e));
                }
            }
            }
            LOGGER.info("OdrServiceDetails Size"+odrServiceDetails.size());

    }

    /**
     * Method to generate odr assets
     *
     * @param odrServiceDetail
     * @param gscDetail
     * @return
     */
    public List<OdrAsset> generateOdrAssets(OdrServiceDetail odrServiceDetail,OrderGscDetail gscDetail)
    {
        LOGGER.info("OdrServiceDetail-"+odrServiceDetail.getId()+"orderGscDetail-"+gscDetail.getId());
        List<OrderGscTfn> orderGscTfnList=orderGscTfnRepository.findByOrderGscDetailId(gscDetail.getId());

        List<OdrAsset> assets=new ArrayList<>();
        if(Objects.nonNull(orderGscTfnList)&&!orderGscTfnList.isEmpty()) {
            LOGGER.info("orderGscTfnList size-"+orderGscTfnList.size());
            for (OrderGscTfn tfn : orderGscTfnList) {
                OdrAsset asset = new OdrAsset();
                asset.setOdrServiceDetail(odrServiceDetail);
                asset.setName(tfn.getTfnNumber());
                asset.setFqdn(tfn.getTfnNumber());
                asset.setPublicIp(tfn.getTfnNumber());
                asset.setIsActive(CommonConstants.Y);
                asset.setCreatedBy(tfn.getCreatedBy());
                asset.setUpdatedBy(tfn.getUpdatedBy());
                asset.setIsSharedInd(CommonConstants.N);
                asset.setOriginnetwork("Fixed");
                asset.setCreatedDate(new Timestamp(new Date().getTime()));
                asset.setUpdatedDate(new Timestamp(new Date().getTime()));
                asset.setType("TollFree");
                //Type
                assets.add(asset);
            }
        }

        return assets;
    }

    /**
     * Method to get ord Id
     * @param quoteToLe
     * @return
     */
    private String getCustomerOrgId(QuoteToLe quoteToLe) {
        String organizationId = "";
        Set<QuoteLeAttributeValue> quoteToLeAttributeValues = quoteToLe.getQuoteLeAttributeValues();
        if (Objects.nonNull(quoteToLeAttributeValues)) {
            for (QuoteLeAttributeValue attr : quoteToLeAttributeValues) {
                if (Objects.nonNull(attr) && Objects.nonNull(attr.getDisplayValue())
                        && attr.getDisplayValue().equalsIgnoreCase("ORG_NO")) {
                    organizationId = attr.getAttributeValue();
                    break;
                }
            }
        }
        return organizationId;
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

    public void processGscParentService(OdrOrder odrOrder, String username, OdrContractInfo odrContractInfo, List<OdrServiceDetail> odrServiceDetails) {
		LOGGER.info("process gsc parent service method invoked");
		OdrServiceDetail parentOdrServiceDetail = new OdrServiceDetail();
		//parentOdrServiceDetail.setServiceRefId(Utils.generateUid());
		//parentOdrServiceDetail.setErfPrdCatalogOfferingName(
				//orderProductSolution.getMstProductOffering().getProductName());
		parentOdrServiceDetail.setCreatedBy(username);
		parentOdrServiceDetail.setCreatedDate(new Date());
		parentOdrServiceDetail.setUpdatedDate(new Date());
		parentOdrServiceDetail.setUpdatedBy(username);
		parentOdrServiceDetail.setIsActive(CommonConstants.Y);
		parentOdrServiceDetail.setIsIzo(CommonConstants.N);
		//parentOdrServiceDetail.setErfPrdCatalogProductName(GscCommonConstants.GSC_SOLUTION);
		parentOdrServiceDetail.setErfPrdCatalogOfferingName(GscCommonConstants.GSC_SOLUTION);
		parentOdrServiceDetail.setErfPrdCatalogProductName("GSIP");
		/*parentOdrServiceDetail
				.setErfPrdCatalogProductId(orderProductSolution.getMstProductOffering()
						.getMstProductFamily().getProductCatalogFamilyId());*/
		parentOdrServiceDetail.setOdrOrder(odrOrder);
		parentOdrServiceDetail.setOdrOrderUuid(odrOrder.getUuid());
		parentOdrServiceDetail.setServiceRefId(odrOrder.getOpOrderCode());
		parentOdrServiceDetail.setOdrContractInfo(odrContractInfo);
		//parentOdrServiceDetail.setOrderType(CommonConstants.NEW);
		OdrComponent solutionOdrComponent=persistOdrComponent(parentOdrServiceDetail);
		solutionOdrComponent.setSiteType("A");
		solutionOdrComponent.setOdrServiceDetail(parentOdrServiceDetail);
		parentOdrServiceDetail.getOdrComponents().add(solutionOdrComponent);
		parentOdrServiceDetail = odrServiceDetailRepository.saveAndFlush(parentOdrServiceDetail);
		
		for(OdrServiceDetail odrServiceDetail : odrServiceDetails) {
			if(odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GSIP"))
				odrServiceDetail.setParentId(parentOdrServiceDetail.getId());
		}
		odrServiceDetailRepository.saveAll(odrServiceDetails);
		odrServiceDetails.add(parentOdrServiceDetail);
	}
}
