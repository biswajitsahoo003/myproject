package com.tcl.dias.oms.gsc.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SapCodeRequest;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.gsc.beans.GscDocumentsByProductAndCountryRequest;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.GscAttachments;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.repository.GscAttachmentsRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.gsc.beans.GscAttachmentBean;
import com.tcl.dias.oms.gsc.beans.GscCityNumber;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscPorting;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.exception.Exceptions;
import com.tcl.dias.oms.gsc.exception.ObjectNotFoundException;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.tiger.TigerServiceHelper;
import com.tcl.dias.oms.gsc.tiger.beans.NumberDetails;
import com.tcl.dias.oms.gsc.tiger.beans.NumberReservationResponse;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscComponentAttributeValuesHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscIsoCountries;
import com.tcl.dias.oms.gsc.util.GscRepcCountries;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import io.vavr.control.Try;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundError;
import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundWithMessage;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CITY_WISE_AREA_CODE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.CITY_WISE_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATIONS_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.DID;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOCUMENT_STATUS_PENDING;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.LNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_GSC_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.REF_TYPE_ORDER_GSC_DETAIL;
import static com.tcl.dias.oms.gsc.util.GscConstants.SOLUTION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.UIFN;
import static java.util.Objects.requireNonNull;

/**
 * Services to handle order details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscOrderDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscOrderDetailService.class);

    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    @Autowired
    OrderGscTfnRepository orderGscTfnRepository;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Autowired
    GscAttachmentsRepository gscAttachmentRepository;

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    GscComponentAttributeValuesHelper attributeValuesPopulator;

    @Autowired
    GscIsoCountries gscIsoCountries;

    @Autowired
    GscRepcCountries gscRepcCountries;

    @Autowired
    TigerServiceHelper tigerServiceHelper;

    @Autowired
    GscAttachmentHelper gscAttachmentHelper;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Value("${rabbitmq.customerleattr.product.queue}")
    String customerLeAttrQueueProduct;

    @Value("${rabbitmq.country.specific.documents.queue}")
    String countrySpecificDocumentsQueueName;

    @Autowired
    MQUtils mqUtils;

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;

    @Value("${rabbitmq.le.sap.queue}")
    String secsCodeQueue;

    @Value("${rabbitmq.supplier.le.sap.queue}")
    String supplierSECSCodeQueue;

    /**
     * Get order product solution by ID
     *
     * @param solutionId
     * @return {@link OrderProductSolution}
     */
    public Try<OrderProductSolution> getOrderProductSolution(Integer solutionId) {
        requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
        return orderProductSolutionRepository.findById(solutionId).map(Try::success)
                .orElse(notFoundError(ExceptionConstants.ORDER_PRODUCT_EMPTY,
                        String.format("Order Product solution with id: %s not found", solutionId)));
    }

    /**
     * Get order which is related to gsc by ID
     *
     * @param orderGscId
     * @return {@link OrderGsc}
     */
    public Try<OrderGsc> getOrderGsc(Integer orderGscId) {
        requireNonNull(orderGscId, ORDER_GSC_ID_NULL_MESSAGE);
        return orderGscRepository.findById(orderGscId).map(Try::success).orElse(notFoundError(
                ExceptionConstants.ORDER_EMPTY, String.format("Order GSC with id: %s not found", orderGscId)));

    }

    /**
     * Get order details by configuration id
     *
     * @param configurationId
     * @return {@link OrderGscDetail}
     */
    public Try<OrderGscDetail> getOrderGscDetail(Integer configurationId) {
        requireNonNull(configurationId, CONFIGURATION_ID_NULL_MESSAGE);
        return orderGscDetailRepository.findById(configurationId).map(Try::success)
                .orElse(notFoundError(ExceptionConstants.ORDER_EMPTY,
                        String.format("Order GSC configuration with id: %s not found", configurationId)));
    }

    private GscOrderConfigurationBean updateOrderConfiguration(GscOrderConfigurationBean configuration) {
        return orderGscDetailRepository.findById(configuration.getId()).map(orderGscDetail -> {
            orderGscDetail.setDest(configuration.getDestination());
            orderGscDetail.setArc(configuration.getArc());
            orderGscDetail.setMrc(configuration.getMrc());
            orderGscDetail.setNrc(configuration.getNrc());
            orderGscDetail.setSrc(configuration.getSource());
            orderGscDetailRepository.save(orderGscDetail);
            return attributeValuesPopulator.handleComponentAttributeValues(configuration,
                    attributeMap -> gscOrderService.saveOrderProductComponentAttributeMap(attributeMap,
                            configuration.getId()));
        }).orElseThrow(() -> notFoundWithMessage(ExceptionConstants.GSC_ORDER_EMPTY,
                String.format("Order GSC configuration with id: %s not found", configuration.getId())));
    }

    /**
     * Update order configurations
     *
     * @param orderId
     * @param orderGscId
     * @param solutionId
     * @param configurations
     * @return
     */
    @Transactional
    public List<GscOrderConfigurationBean> updateOrderConfigurations(Integer orderId, Integer solutionId,
                                                                     Integer orderGscId, List<GscOrderConfigurationBean> configurations) {
        return Try.of(() -> {
            requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
            requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
            requireNonNull(orderGscId, ORDER_GSC_ID_NULL_MESSAGE);
            requireNonNull(configurations, CONFIGURATIONS_NULL_MESSAGE);
            return orderId;
        }).map(gscOrderService::fetchOrderById)
                .flatMap(order -> getOrderProductSolution(solutionId))
                .map(solution -> gscOrderService.fetchOrderGscById(orderGscId))
                .map(orderGsc -> configurations.stream()
                        .map(this::updateOrderConfiguration).collect(Collectors.toList()))
                .get();
    }

    /**
     * Get Order Configuration Details by configuration ID
     *
     * @param orderId
     * @param solutionId
     * @param orderGscId
     * @param configurationId
     * @param fetchAttributes
     * @return {@link GscOrderConfigurationBean}
     */
    public Try<GscOrderConfigurationBean> getOrderConfigurationDetails(Integer orderId, Integer solutionId,
                                                                       Integer orderGscId, Integer configurationId, Boolean fetchAttributes) {
        requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
        requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
        requireNonNull(orderGscId, ORDER_GSC_ID_NULL_MESSAGE);
        requireNonNull(configurationId, CONFIGURATION_ID_NULL_MESSAGE);
        OrderGscDetail orderGscDetail = getOrderGscDetail(configurationId).get();
        GscOrderConfigurationBean gscOrderConfigurationBean = fromOrderGscDetail(orderGscDetail, fetchAttributes);
        attributeValuesPopulator.populateComponentAttributeValues(gscOrderConfigurationBean,
                () -> gscOrderService.getOrderProductComponentAttributeMap(configurationId));
        return Try.success(gscOrderConfigurationBean);
    }

    /**
     * Set OrderGscDetail to GscOrderConfigurationBean
     *
     * @param orderGscDetail
     * @param fetchAttributes
     * @return {@link GscOrderConfigurationBean}
     */
    private GscOrderConfigurationBean fromOrderGscDetail(OrderGscDetail orderGscDetail, Boolean fetchAttributes) {
        GscOrderConfigurationBean gscOrderConfigurationBean = GscOrderConfigurationBean
                .fromOrderGscDetail(orderGscDetail);
        gscOrderConfigurationBean.setProductComponents(getOrderProductComponents(orderGscDetail, fetchAttributes));
        return gscOrderConfigurationBean;
    }

    /**
     * Get OrderProductComponents based on OrderGscDetail
     *
     * @param orderGscDetail
     * @param fetchAttributes
     * @return {@link List<GscOrderProductComponentBean>}
     */
    private List<GscOrderProductComponentBean> getOrderProductComponents(OrderGscDetail orderGscDetail,
                                                                         Boolean fetchAttributes) {
        List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                .findByReferenceIdAndType(orderGscDetail.getId(), GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);

        return orderProductComponents.stream().map(fetchAttributes ? orderProductComponent -> {
            GscOrderProductComponentBean gscOrderProductComponentBean = GscOrderProductComponentBean
                    .fromOrderProductComponentWithoutAttributes(orderProductComponent);
            List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
                    .findByOrderProductComponent(orderProductComponent);
            List<GscOrderProductComponentsAttributeSimpleValueBean> attributes = orderProductComponentsAttributeValues
                    .stream().map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute)
                    .collect(Collectors.toList());
            List<GscOrderProductComponentsAttributeValueBean> resultAttributes = groupAndConvertToValueBeans(
                    attributes);
            gscOrderProductComponentBean.setAttributes(resultAttributes);
            return gscOrderProductComponentBean;
        } : GscOrderProductComponentBean::fromOrderProductComponentWithoutAttributes).collect(Collectors.toList());
    }

    /**
     * Grouping GscOrderProductComponentsAttributeValueBeans
     *
     * @param simpleValueBeans
     * @return {@link List<GscOrderProductComponentsAttributeValueBean>}
     */
    private List<GscOrderProductComponentsAttributeValueBean> groupAndConvertToValueBeans(
            List<GscOrderProductComponentsAttributeSimpleValueBean> simpleValueBeans) {
        Map<String, List<GscOrderProductComponentsAttributeSimpleValueBean>> groupedAttributes = simpleValueBeans
                .stream()
                .collect(Collectors.groupingBy(GscOrderProductComponentsAttributeSimpleValueBean::getAttributeName));
        return groupedAttributes.values().stream().map(values -> {
            if (values.size() == 1) {
                return values.get(0);
            } else {
                GscOrderProductComponentsAttributeSimpleValueBean first = values.get(0);
                List<String> attributeValues = values.stream()
                        .map(GscOrderProductComponentsAttributeSimpleValueBean::getAttributeValue)
                        .collect(Collectors.toList());
                GscOrderProductComponentsAttributeArrayValueBean bean = new GscOrderProductComponentsAttributeArrayValueBean();
                bean.setAttributeId(first.getAttributeId());
                bean.setAttributeName(first.getAttributeName());
                bean.setDescription(first.getDescription());
                bean.setDisplayValue(first.getDisplayValue());
                bean.setAttributeValue(attributeValues);
                return bean;
            }
        }).collect(Collectors.toList());
    }

    private Try<OrderGscDetail> fetchOrderGscDetailById(Integer orderGscDetailId) {
        LOGGER.info("OrderGscDetail ID :: {}", orderGscDetailId);
        return orderGscDetailRepository.findById(orderGscDetailId).map(Try::success)
                .orElse(notFoundError(ExceptionConstants.GSC_ORDER_EMPTY,
                        String.format("GSC configuration with id: %s not found", orderGscDetailId)));
    }

    private CustomerLeDetailsBean getCustomerLeDetails(Integer customerLegalEntityId)
            throws TclCommonException, IllegalArgumentException {
        Objects.requireNonNull(customerLegalEntityId,
                "Error while getting customer LE details. Customer legal entity id cannot be null");
        CustomerLeAttributeRequestBean requestBean = new CustomerLeAttributeRequestBean();
        requestBean.setCustomerLeId(customerLegalEntityId);
        requestBean.setProductName(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
        String jsonPayload = GscUtils.toJson(requestBean);
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeDetails {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String mqResponse = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct, jsonPayload);
        Objects.requireNonNull(mqResponse, String
                .format("Null response from customer service for customerLegalEntityId: %s", customerLegalEntityId));
        return GscUtils.fromJson(mqResponse, CustomerLeDetailsBean.class);
    }

    private List<GscTfnBean> reserveAndSaveNumbers(List<GscTfnBean> tfns, OrderGscDetail orderGscDetail,
                                                   String originCountryCode, String serviceType) throws TclCommonException, IllegalArgumentException {
        String orgId = getOrganisationIdOfCustomer(orderGscDetail);
        LOGGER.info("orgId :: {}", orgId);
        LOGGER.info("originCountryCode :: {}", originCountryCode);
        if (Objects.isNull(orgId)) {
            // if org id is not found in Tiger return empty number list
            return new ArrayList<>();
        }
        int originCountryCallingCode = getOriginCountryCalllingCode(originCountryCode);
        LOGGER.info("originCountryCallingCode :: {}", originCountryCallingCode);
        List<GscTfnBean> filteredTfns = tfns.stream()
                .filter(gscTfnBean -> !"0".equalsIgnoreCase(gscTfnBean.getNumber())).collect(Collectors.toList());
        if (Objects.isNull(filteredTfns) || filteredTfns.isEmpty())
            return tfns;
        return tigerServiceHelper
                .reserveNumbersForOrganisation(orgId, String.valueOf(originCountryCallingCode), serviceType,
                        filteredTfns, orderGscDetail.getId(), REF_TYPE_ORDER_GSC_DETAIL, "")
                .flatMap(numberReservationResponse -> saveReservationIdAsAttribute(numberReservationResponse,
                        orderGscDetail, serviceType))
                .map(NumberReservationResponse::getNumberDetails).get().stream().map(GscTfnBean::fromNumberDetails)
                .map(gscTfnBean -> saveGscTfnBean(gscTfnBean, orderGscDetail, originCountryCode))
                .collect(Collectors.toList());
    }

    private int getOriginCountryCalllingCode(String originCountryCode) {
        String twoDigitCountryCode = gscIsoCountries.iso2ForISO3Code(originCountryCode);
        return PhoneNumberUtil.getInstance().getCountryCodeForRegion(twoDigitCountryCode);
    }

    private String getOrganisationIdOfCustomer(OrderGscDetail orderGscDetail) throws TclCommonException {
        Integer erfCustomerLeId = orderGscDetail.getOrderGsc().getOrderToLe().getErfCusCustomerLegalEntityId();
        LeSapCodeResponse leSapCodeResponse = getCustomerLeSapCodeDetails(erfCustomerLeId);
        LeSapCodeBean leSapCodeBean = leSapCodeResponse.getLeSapCodes().stream().findFirst().get();
        LOGGER.info("leSapCodeBean :: {}", leSapCodeBean.toString());
        // SECS Code and Org No both are same
        String secsCode = leSapCodeBean.getCodeValue();
        LOGGER.info("secsCode :: {}", secsCode);
		/*return tigerServiceHelper.getOrganisationByCuId(secsCode, orderGscDetail.getId(), REF_TYPE_ORDER_GSC_DETAIL)
				.map(Organisation::getOrgId).orElseGet(() -> {
					LOGGER.warn(
							String.format("Organisation with secsCode: %s not found in tiger service", secsCode));
					return null;
				});*/
        return secsCode;
    }


    /**
     * To get customerLeSapCodeDetails
     *
     * @param customerLegalEntityId
     * @return
     * @throws TclCommonException
     */
    public LeSapCodeResponse getCustomerLeSapCodeDetails(Integer customerLegalEntityId) throws TclCommonException {
        Objects.requireNonNull(customerLegalEntityId,
                "Error while getting customer LE details. Customer legal entity id cannot be null");
        SapCodeRequest requestBean = new SapCodeRequest();
        List<Integer> customerLeEntityIds = new ArrayList<>();
        customerLeEntityIds.add(customerLegalEntityId);
        requestBean.setCustomerLeIds(customerLeEntityIds);
        requestBean.setType(GscConstants.SECS_CODE);
        String jsonPayload = GscUtils.toJson(requestBean);
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeDetails {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String mqResponse = (String) mqUtils.sendAndReceive(secsCodeQueue, jsonPayload);
        Objects.requireNonNull(mqResponse, String
                .format("Null response from customer service for customerLegalEntityId: %s", customerLegalEntityId));
        LOGGER.info("Response from the customer service ::{}", mqResponse);
        return GscUtils.fromJson(mqResponse, LeSapCodeResponse.class);
    }

    public LeSapCodeResponse getSupplierLeSapCodeDetails(Integer supplierLegalEntityId) throws TclCommonException {
        Objects.requireNonNull(supplierLegalEntityId,
                "Error while getting supplier LE details. Supplier legal entity id cannot be null");
        SapCodeRequest requestBean = new SapCodeRequest();
        List<Integer> customerLeEntityIds = new ArrayList<>();
        customerLeEntityIds.add(supplierLegalEntityId);
        requestBean.setCustomerLeIds(customerLeEntityIds);
        requestBean.setType(GscConstants.SECS_CODE);
        // W stands for Wholesale
        requestBean.setLeType("W");
        String jsonPayload = GscUtils.toJson(requestBean);
        LOGGER.info("MDC Filter token value in before Queue call supplierSECSCodeQueue {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String mqResponse = (String) mqUtils.sendAndReceive(supplierSECSCodeQueue, jsonPayload);
        Objects.requireNonNull(mqResponse, String
                .format("Null response from customer service for supplierLegalEntityId: %s", supplierLegalEntityId));
        LOGGER.info("Response from the customer service ::{}", mqResponse);
        return GscUtils.fromJson(mqResponse, LeSapCodeResponse.class);
    }


    private Try<NumberReservationResponse> saveReservationIdAsAttribute(
            NumberReservationResponse numberReservationResponse, OrderGscDetail orderGscDetail, String serviceType) {
        String reservationId = numberReservationResponse.getReservationId();
        MstProductComponent mstProductComponent = mstProductComponentRepository
                .findByNameAndStatus(serviceType, GscConstants.STATUS_ACTIVE).stream().findFirst().get();
        OrderProductComponent orderProductComponent = orderProductComponentRepository
                .findByReferenceIdAndMstProductComponent(orderGscDetail.getId(), mstProductComponent).stream()
                .findFirst().get();
        gscOrderService.saveOrderComponentAttributeValue(orderProductComponent,
                buildReservationAttributeBean(reservationId));
        return Try.success(numberReservationResponse);
    }

    /**
     * @param reservationId
     * @return GscOrderProductComponentsAttributeSimpleValueBean
     */
    private GscOrderProductComponentsAttributeSimpleValueBean buildReservationAttributeBean(String reservationId) {
        GscOrderProductComponentsAttributeSimpleValueBean valueBean = new GscOrderProductComponentsAttributeSimpleValueBean();
        valueBean.setAttributeName(GscConstants.TFN_Reservation_ID);
        valueBean.setAttributeValue(reservationId);
        valueBean.setDescription("Resevation Id for tfn numbers");
        valueBean.setDisplayValue(reservationId);
        return valueBean;
    }

    public List<GscTfnBean> getNumbersForConfiguration(OrderGscDetail orderGscDetail, String city, Integer count,
                                                       Boolean autoReserve) throws TclCommonException, IllegalArgumentException {

        List<OrderGscTfn> inventoryNumbers = orderGscDetail.getOrderGscTfns().stream()
                .filter(orderGscTfn -> !GscConstants.STATUS_ACTIVE_BYTE.equals(orderGscTfn.getIsPorted()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(inventoryNumbers)) {
            LOGGER.info("Order Gsc Source :: {}", orderGscDetail.getSrc());
            String[] src = orderGscDetail.getSrc().split(":");
            String originIsoCountryCode = getCountryCodeForOrderGscDetail(orderGscDetail);
            String originRepcCountryCode = getRepcCountryCodeForOrderGscDetail(orderGscDetail);
            LOGGER.info("originIsoCountryCode :: {}", originIsoCountryCode);
            LOGGER.info("originRepcCountryCode :: {}", originRepcCountryCode);
            String serviceType = orderGscDetail.getOrderGsc().getProductName();
            String cityCode = null;
            if (Objects.nonNull(city)) {
                cityCode = city;
            } else {
                cityCode = src.length > 1 ? src[1] : null;
            }
            LOGGER.info("City Code :: {}", cityCode);
            String orgId = getOrganisationIdOfCustomer(orderGscDetail);
            LOGGER.info("orgId :: {}", orgId);
            if (Objects.isNull(orgId)) {
                // if org id is not found in Tiger return empty number list
                // return ImmutableList.of();
                return orderGscDetail.getOrderGscTfns().stream().map(GscTfnBean::fromOrderGscTfn)
                        .collect(Collectors.toList());
            }
            if (DOMESTIC_VOICE.equalsIgnoreCase(serviceType)) {
                serviceType = DID;
            }
            // TODO : Commenting below line to disable number bank
//            List<NumberDetails> numbers = tigerServiceHelper.getAvailableNumbersByServiceAndOriginCountryCity(
//                    serviceType, originRepcCountryCode, cityCode, count, orderGscDetail.getId(), REF_TYPE_ORDER_GSC_DETAIL,
//                    orgId);
            List<NumberDetails> numbers = null;
            numbers = Optional.ofNullable(numbers).orElse(ImmutableList.of());
            if (autoReserve && !CollectionUtils.isEmpty(numbers)) {
                List<GscTfnBean> tfns = numbers.stream().map(GscTfnBean::fromNumberDetails)
                        .collect(Collectors.toList());
                LOGGER.info("TFNS :: {}", tfns);
                LOGGER.info("serviceType :: {}", serviceType);
                reserveAndSaveNumbers(tfns, orderGscDetail, originIsoCountryCode, serviceType);
                LOGGER.info("After TFNS :: {}", tfns);
                return orderGscTfnRepository.findByOrderGscDetail(orderGscDetail).stream()
                        .map(GscTfnBean::fromOrderGscTfn).collect(Collectors.toList());
            } else {
                List<GscTfnBean> numbersFromInventory = numbers.stream().map(GscTfnBean::fromNumberDetails)
                        .collect(Collectors.toList());
                LOGGER.info("numbersFromInventory :: {}", numbersFromInventory);
                List<GscTfnBean> savedTfns = orderGscTfnRepository.findByOrderGscDetail(orderGscDetail).stream()
                        .map(GscTfnBean::fromOrderGscTfn).collect(Collectors.toList());
                LOGGER.info("savedTfns :: {}", savedTfns);
                savedTfns.addAll(numbersFromInventory);
                return savedTfns;
            }
        } else {
            return orderGscDetail.getOrderGscTfns().stream().map(GscTfnBean::fromOrderGscTfn)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Fetch available numbers for specified configuration
     *
     * @param configurationId
     * @param count
     * @return
     */
    @Transactional
    public List<GscTfnBean> getAvailableNumbers(Integer configurationId, String city, Integer count,
                                                Boolean autoReserve) {
        LOGGER.info("Configuration ID :: {}", configurationId);
        LOGGER.info("City :: {}", city);
        LOGGER.info("Count :: {}", count);
        LOGGER.info("AutoReserve :: {}", autoReserve);
        return Try.of(() -> {
            requireNonNull(configurationId, "Configuration Id cannot be null");
            requireNonNull(count, "Count cannot be null");
            requireNonNull(autoReserve, "Auto reserve must be specified as true/false");
            return configurationId;
        }).flatMap(this::fetchOrderGscDetailById)
                .mapTry(orderGscDetail -> getNumbersForConfiguration(orderGscDetail, city, count, autoReserve)).get();
    }

    private List<GscTfnBean> reserveNumbers(OrderGscDetail orderGscDetail, List<GscTfnBean> gscTfnBeans)
            throws TclCommonException, IllegalArgumentException {
        String originCountryCode = getCountryCodeForOrderGscDetail(orderGscDetail);
        String serviceType = orderGscDetail.getOrderGsc().getProductName();
        return reserveAndSaveNumbers(gscTfnBeans, orderGscDetail, originCountryCode, serviceType);
    }

    private Optional<OrderGscTfn> findNonUIFNTfnNumber(GscTfnBean gscTfnBean) {
        return Optional.ofNullable(orderGscTfnRepository.findAllByTfnNumber(gscTfnBean.getNumber()))
                .orElse(ImmutableList.of())
                .stream()
                .filter(orderGscTfn -> !UIFN.equalsIgnoreCase(orderGscTfn.getOrderGscDetail().getOrderGsc().getProductName()))
                .findFirst();
    }

    private GscTfnBean saveGscTfnBean(GscTfnBean gscTfnBean, OrderGscDetail orderGscDetail, String countryCode) {
        OrderGscTfn orderGscTfn = new OrderGscTfn();
        if (orderGscDetail.getOrderGsc().getProductName().equalsIgnoreCase(UIFN)) {
            orderGscTfn = (Objects
                    .nonNull(gscTfnBean.getId())
                    ? orderGscTfnRepository.findById(gscTfnBean.getId())
                    : Optional.ofNullable(orderGscTfnRepository
                    .findByTfnNumberAndOrderGscDetail(gscTfnBean.getNumber(), orderGscDetail)))
                    .orElse(new OrderGscTfn());
        } else {
            orderGscTfn = (Objects.nonNull(gscTfnBean.getId()) ? orderGscTfnRepository.findById(gscTfnBean.getId())
                    : findNonUIFNTfnNumber(gscTfnBean)).orElse(new OrderGscTfn());
        }
        orderGscTfn.setOrderGscDetail(orderGscDetail);
        orderGscTfn.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
        if (Objects.nonNull(gscTfnBean.getCity())) {
            orderGscTfn.setCountryCode(String.format("%s:%s", Optional.ofNullable(gscTfnBean.getCountry()).orElse(""),
                    gscTfnBean.getCity()));
        } else {
            orderGscTfn.setCountryCode(countryCode);
        }
        if (Objects.isNull(orderGscTfn.getCreatedTime())) {
            orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
        }
        if (Objects.isNull(orderGscTfn.getCreatedBy())) {
            orderGscTfn.setCreatedBy(Utils.getSource());
        }
        orderGscTfn.setIsPorted(gscTfnBean.getPorted());
        orderGscTfn.setUpdatedBy(Utils.getSource());
        orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
        orderGscTfn.setTfnNumber(gscTfnBean.getNumber());
        orderGscTfnRepository.save(orderGscTfn);
        gscTfnBean.setId(orderGscTfn.getId());
        return gscTfnBean;
    }

    private String getCountryCodeForOrderGscDetail(OrderGscDetail orderGscDetail) {
        String[] src = orderGscDetail.getSrc().split(":");
        String originCountryCode = Optional.ofNullable(gscIsoCountries.forName(src[0]))
                .map(GscIsoCountries.GscCountry::getCode)
                .orElseThrow(() -> notFoundWithMessage(ExceptionConstants.COMMON_ERROR,
                        String.format("Country with name: %s not found", orderGscDetail.getSrc())));
        return originCountryCode;
    }

    private String getRepcCountryCodeForOrderGscDetail(OrderGscDetail orderGscDetail) {
        String[] src = orderGscDetail.getSrc().split(":");
        String originCountryCode = Optional.ofNullable(gscRepcCountries.forName(src[0]))
                .map(GscRepcCountries.GscCountry::getCode)
                .orElseThrow(() -> notFoundWithMessage(ExceptionConstants.COMMON_ERROR,
                        String.format("Country with name: %s not found", orderGscDetail.getSrc())));
        return originCountryCode;
    }

    /**
     * Save TFN numbers for specified configuration
     *
     * @param configurationId
     * @param gscTfnBeans
     * @return
     */
    @Transactional
    public List<GscTfnBean> saveNumbers(Integer configurationId, List<GscTfnBean> gscTfnBeans) {
        requireNonNull(configurationId, "Configuration Id cannot be null");
        checkArgument(!CollectionUtils.isEmpty(gscTfnBeans), "TFN list cannot be empty");
        return this.fetchOrderGscDetailById(configurationId).mapTry(orderGscDetail -> {
            String originCountryCode = getCountryCodeForOrderGscDetail(orderGscDetail);
            List<GscTfnBean> toBeReservedNumbers = gscTfnBeans.stream()
                    .filter(gscTfnBean -> !GscConstants.STATUS_ACTIVE_BYTE.equals(gscTfnBean.getPorted()))
                    .collect(Collectors.toList());
            List<GscTfnBean> reservedNumbers = new ArrayList<>();
            if (!CollectionUtils.isEmpty(toBeReservedNumbers)) {
                reservedNumbers = reserveNumbers(orderGscDetail, toBeReservedNumbers);
            }
            if (orderGscDetail.getOrderGsc().getProductName().equalsIgnoreCase(UIFN)) {
                deleteTfnNumbeForUIFNConfig(orderGscDetail);
            }
            List<GscTfnBean> portedNumbers = gscTfnBeans.stream()
                    .filter(gscTfnBean -> GscConstants.STATUS_ACTIVE_BYTE.equals(gscTfnBean.getPorted()))
                    .map(gscTfnBean -> saveGscTfnBean(gscTfnBean, orderGscDetail, originCountryCode))
                    .collect(Collectors.toList());
            reservedNumbers.addAll(portedNumbers);
            return reservedNumbers;
        }).get();
    }

    private void deleteTfnNumbeForUIFNConfig(OrderGscDetail orderGscDetail) {
        Optional.ofNullable(orderGscTfnRepository.findByOrderGscDetail(orderGscDetail))
                .ifPresent(orderGscTfns -> {
                    orderGscTfns.forEach(orderGscTfn -> orderGscTfnRepository.delete(orderGscTfn));
                });
    }

    private List<GscCountrySpecificDocumentBean> getDocumentsForConfigurationFromProductCatalog(
            OrderGscDetail orderGscDetail) throws TclCommonException, IllegalArgumentException {
        GscIsoCountries.GscCountry gscCountry = gscIsoCountries.forName(orderGscDetail.getSrc());
        if (Objects.isNull(gscCountry)) {
            throw new TCLException("Unable to get document list from product catalogue service");
        }
        String iso3CountryCode = gscCountry.getCode();
        String productName = orderGscDetail.getOrderGsc().getProductName();
        GscDocumentsByProductAndCountryRequest request = new GscDocumentsByProductAndCountryRequest();
        request.setIso3CountryCode(iso3CountryCode);
        request.setProductName(productName);
        LOGGER.info("MDC Filter token value in before Queue call getDocumentsForConfigurationFromProductCatalog {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String mqResponse = (String) mqUtils.sendAndReceive(countrySpecificDocumentsQueueName, GscUtils.toJson(request));
        if (!Strings.isNullOrEmpty(mqResponse)) {
            return GscUtils.fromJson(mqResponse, new TypeReference<List<GscCountrySpecificDocumentBean>>() {
            });
        } else {
            throw new TCLException("Error occurred while fetching document list from product catalogue service");
        }
    }

    private GscAttachments toGscAttachment(GscCountrySpecificDocumentBean documentBean, OrderGscDetail orderGscDetail,
                                           OrderToLe orderToLe) {
        GscAttachments gscAttachment = new GscAttachments();
        gscAttachment.setDocumentName(documentBean.getDocumentName());
        gscAttachment.setDocumentCategory(documentBean.getCategory());
        gscAttachment.setDocumentType(documentBean.getType());
        gscAttachment.setStatus(DOCUMENT_STATUS_PENDING);
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            gscAttachment.setDocumentUId(documentBean.getuID() + "_OBJECT");
        } else {
            gscAttachment.setDocumentUId(documentBean.getuID());
        }
        gscAttachment.setTemplateName(documentBean.getTemplate());
        OmsAttachment omsAttachment = new OmsAttachment();
        omsAttachment.setAttachmentType(GscConstants.OTHERS);
        omsAttachment.setReferenceId(orderGscDetail.getId());
        omsAttachment.setOrderToLe(orderToLe);
        omsAttachment.setReferenceName(GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
        gscAttachment.setOmsAttachment(omsAttachment);
        return gscAttachment;
    }

    private List<GscAttachmentBean> getDocumentsForConfiguration(OrderGscDetail orderGscDetail)
            throws TclCommonException, IllegalArgumentException {
        List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(
                GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, orderGscDetail.getId(), GscConstants.OTHERS);
        if (CollectionUtils.isEmpty(omsAttachments)) {
            OrderToLe orderToLe = orderGscDetail.getOrderGsc().getOrderToLe();
            List<GscCountrySpecificDocumentBean> countrySpecificDocuments = getDocumentsForConfigurationFromProductCatalog(
                    orderGscDetail);
            List<GscAttachments> gscAttachments = countrySpecificDocuments.stream()
                    .map(documentBean -> toGscAttachment(documentBean, orderGscDetail, orderToLe))
                    .collect(Collectors.toList());
            omsAttachments = omsAttachmentRepository.saveAll(
                    gscAttachments.stream().map(GscAttachments::getOmsAttachment).collect(Collectors.toList()));
            gscAttachmentRepository.saveAll(gscAttachments);
        }
        List<GscAttachmentBean> gscAttachmentBeans = gscAttachmentRepository.findAllByOmsAttachmentIn(omsAttachments)
                .stream().map(GscAttachmentBean::fromGscAttachment).collect(Collectors.toList());

        gscAttachmentBeans.forEach(gscAttachment -> {
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                gscAttachment.setSwiftEnabled(true);
            } else {
                gscAttachment.setSwiftEnabled(false);
            }
            OmsAttachment attachment = omsAttachmentRepository.findByReferenceNameAndAttachmentType(gscAttachment.getDocumentUid(),
                    GscConstants.OTHERS);
            if (Objects.nonNull(attachment)) {
                gscAttachment.setTemplateAttachmentId(attachment.getErfCusAttachmentId());
            }
        });

        return gscAttachmentBeans;
    }

    @Transactional
    public List<GscAttachmentBean> getDocumentsForConfigurationId(Integer configurationId) {
        return fetchOrderGscDetailById(configurationId).mapTry(this::getDocumentsForConfiguration).get();
    }

    private Try<GscAttachments> fetchConfigurationAttachment(Integer documentId) {
        return gscAttachmentRepository.findById(documentId).map(Try::success)
                .orElse(Exceptions.notFoundError(ExceptionConstants.RESOURCE_NOT_EXIST,
                        String.format("Configuration document with id: %s not found", documentId)));
    }

    private GscAttachmentBean uploadDocumentForConfiguration(MultipartFile file, GscAttachments gscAttachments) {
        OmsAttachment omsAttachment = gscAttachments.getOmsAttachment();
        String configId = String.valueOf(omsAttachment.getReferenceId());
        String referenceType = GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE.replaceAll("\\.", "_");
        Integer attachmentId = gscAttachmentHelper.saveAttachment(file, configId, referenceType);
        omsAttachment.setErfCusAttachmentId(attachmentId);
        omsAttachmentRepository.save(omsAttachment);
        gscAttachments.setStatus(GscConstants.DOCUMENT_STATUS_UPLOADED);
        gscAttachments.setUploadedTime(Timestamp.valueOf(LocalDateTime.now()));
        gscAttachments.setUploadedBy(Utils.getSource());
        GscAttachments updated = gscAttachmentRepository.save(gscAttachments);
        return GscAttachmentBean.fromGscAttachment(updated);
    }

    private GscAttachmentBean uploadObjectDocumentForConfiguration(GscAttachments gscAttachments, String requestId,
                                                                   String url) {
        OmsAttachment omsAttachment = gscAttachments.getOmsAttachment();
        String configId = String.valueOf(omsAttachment.getReferenceId());
        String referenceType = GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE.replaceAll("\\.", "_");
        Integer attachmentId = gscAttachmentHelper.saveObjectAttachment(requestId, url);
        omsAttachment.setErfCusAttachmentId(attachmentId);
        omsAttachmentRepository.save(omsAttachment);
        gscAttachments.setStatus(GscConstants.DOCUMENT_STATUS_UPLOADED);
        gscAttachments.setUploadedTime(Timestamp.valueOf(LocalDateTime.now()));
        gscAttachments.setUploadedBy(Utils.getSource());
        GscAttachments updated = gscAttachmentRepository.save(gscAttachments);
        return GscAttachmentBean.fromGscAttachment(updated);
    }

    @Transactional
    public GscAttachmentBean uploadDocumentForConfiguration(MultipartFile file, Integer configurationId,
                                                            Integer documentId) {
        return fetchOrderGscDetailById(configurationId)
                .flatMap(orderGscDetail -> fetchConfigurationAttachment(documentId))
                .map(gscAttachment -> uploadDocumentForConfiguration(file, gscAttachment)).get();
    }

    private Resource fetchTemplateForConfiguration(GscAttachments gscAttachment)
            throws ObjectNotFoundException, TclCommonException, IllegalArgumentException {
        OmsAttachment omsAttachment = omsAttachmentRepository
                .findByReferenceNameAndAttachmentType(gscAttachment.getDocumentUId(), GscConstants.OTHERS);
        if (Objects.isNull(omsAttachment)) {
            throw notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                    String.format("Template not uploaded yet for attachment id: %s", gscAttachment.getId()));
        }
        return gscAttachmentHelper.fetchAttachmentResource(omsAttachment.getErfCusAttachmentId())
                .orElseThrow(() -> notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                        String.format("Template not found for attachment id: %s", gscAttachment.getId())));
    }

    private Resource fetchAttachmentForConfiguration(GscAttachments gscAttachment)
            throws ObjectNotFoundException, TclCommonException, IllegalArgumentException {
        Integer attachmentId = gscAttachment.getOmsAttachment().getErfCusAttachmentId();
        if (Objects.isNull(attachmentId)) {
            throw notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                    String.format("Document has not been uploaded yet for attachment id: %s", gscAttachment.getId()));
        }
        return gscAttachmentHelper.fetchAttachmentResource(attachmentId)
                .orElseThrow(() -> notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                        String.format("Document not found for attachment id: %s", gscAttachment.getId())));
    }

    public Try<Resource> fetchConfigurationAttachmentForId(Integer configurationId, Integer documentId,
                                                           Boolean downloadTemplate) {
        return fetchOrderGscDetailById(configurationId)
                .flatMap(orderGscDetail -> fetchConfigurationAttachment(documentId))
                .mapTry(downloadTemplate ? this::fetchTemplateForConfiguration : this::fetchAttachmentForConfiguration);
    }

    /**
     * Delete order gsc
     *
     * @param orderGsc
     */
    public void deleteOrderGscDetailsByOrderGsc(OrderGsc orderGsc) {
        Objects.requireNonNull(orderGsc, ORDER_GSC_ID_NULL_MESSAGE);
        orderGscDetailRepository.findByorderGsc(orderGsc).stream().map(OrderGscDetail::getId)
                .forEach(this::deleteOrderGscDetail);
        orderGscRepository.delete(orderGsc);
    }

    /**
     * deleteOrderGscDetail
     *
     * @param orderGscDetailId
     */
    private OrderGscDetail deleteOrderGscDetail(Integer orderGscDetailId) {
        List<OrderProductComponent> productComponents = orderProductComponentRepository
                .findByReferenceIdAndType(orderGscDetailId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        productComponents.forEach(productComponent -> orderProductComponentsAttributeValueRepository
                .deleteAll(productComponent.getOrderProductComponentsAttributeValues()));
        orderProductComponentRepository.deleteAll(productComponents);
        return orderGscDetailRepository.findById(orderGscDetailId).map(orderGscDetail -> {
            orderGscDetailRepository.delete(orderGscDetail);
            return orderGscDetail;
        }).orElseGet(null);
    }

    /**
     * This method will produce a list of TFN numbers for LNS and ACANS
     * configuration grouped by City name and portability
     *
     * @param orderGscDetailId
     * @return {@link GscCityNumber}
     * @author VISHESH AWASTHI
     */
    public List<GscCityNumber> getCityNumberConfiguration(Integer orderGscDetailId, String serviceType) {
        Objects.requireNonNull(orderGscDetailId);
        Objects.requireNonNull(serviceType);
        List<GscTfnBean> orderGscTfnsFromAttributes = new ArrayList<>();
        List<GscTfnBean> gscTfnBeans = new ArrayList<>();
        fetchComponentAttributes(orderGscDetailId, orderGscTfnsFromAttributes, serviceType);

        if (serviceType.equalsIgnoreCase(GscConstants.LNS)) {
            gscTfnBeans = Optional.ofNullable(orderGscTfnRepository.findByOrderGscDetailId(orderGscDetailId)).get()
                    .stream().filter(orderGscTfn -> StringUtils.isNotBlank(orderGscTfn.getCountryCode()) && orderGscTfn.getCountryCode().contains(":"))
                    .filter(orderGscTfn -> orderGscTfn.getIsPorted().equals((byte) 1)).map(GscTfnBean::fromOrderGscTfn)
                    .collect(Collectors.toList());
        }

        orderGscTfnsFromAttributes.addAll(gscTfnBeans);
        return getCityNumbersList(orderGscTfnsFromAttributes);
    }

    /**
     * @param orderGscDetailId
     * @param orderGscTfnsFromAttributes
     */
    private void fetchComponentAttributes(Integer orderGscDetailId, List<GscTfnBean> orderGscTfnsFromAttributes,
                                          String serviceType) {
        Optional.ofNullable(orderProductComponentRepository
                .findByReferenceIdAndType(orderGscDetailId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE)).get()
                .stream()
                .filter(orderProductComponent -> serviceType
                        .equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName()))
                .findFirst().ifPresent(filteredComponent -> {
            filterAttributeAndFetchCityNumbers(orderGscTfnsFromAttributes, filteredComponent);
        });
    }

    /**
     * @param orderGscTfnsFromAttributes
     * @param filteredComponent
     */
    private void filterAttributeAndFetchCityNumbers(List<GscTfnBean> orderGscTfnsFromAttributes,
                                                    OrderProductComponent filteredComponent) {
        List<OrderProductComponentsAttributeValue> attributeValues = Optional
                .ofNullable(
                        orderProductComponentsAttributeValueRepository.findByOrderProductComponent(filteredComponent))
                .get().stream()
                .filter(attribute -> GscConstants.CITY_WISE_QUANTITY_OF_NUMBERS
                        .equalsIgnoreCase(attribute.getProductAttributeMaster().getName()))
                .collect(Collectors.toList());

        if (attributeValues.size() > 0) {
            List<String> cityNumbers = attributeValues.stream()
                    .map(OrderProductComponentsAttributeValue::getAttributeValues).collect(Collectors.toList());

            cityNumbers.forEach(number -> {
                String[] cityAndNumbers = number.split(":");
                for (int i = 0; i < cityAndNumbers.length - 1; i++) {
                    GscTfnBean gscTfnBean = new GscTfnBean();
                    gscTfnBean.setCity(cityAndNumbers[0]);
                    gscTfnBean.setPorted((byte) 0);
                    gscTfnBean.setNumber(cityAndNumbers[i + 1]);
                    orderGscTfnsFromAttributes.add(gscTfnBean);
                }
            });
        }
    }

    /**
     * @param gscTfnBeans
     * @return
     */
    private List<GscCityNumber> getCityNumbersList(List<GscTfnBean> gscTfnBeans) {
        List<GscCityNumber> cityNumbers = new ArrayList<>();
        if (gscTfnBeans.size() == 0)
            return cityNumbers;
        Map<String, Map<Byte, List<GscTfnBean>>> map = gscTfnBeans.stream()
                .collect(Collectors.groupingBy(GscTfnBean::getCity, Collectors.groupingBy(GscTfnBean::getPorted)));

        map.entrySet().forEach(number -> {
            GscCityNumber cityNumber = new GscCityNumber();
            cityNumber.setOriginCity(number.getKey());
            List<GscPorting> gscPortings = new ArrayList<>();
            number.getValue().entrySet().forEach(porting -> {
                GscPorting gscPorting = new GscPorting();
                gscPorting.setIsPortingRequired(porting.getKey());
                gscPorting.setTfnBeans(
                        porting.getValue().stream().filter(numbers -> !(numbers.getNumber().equalsIgnoreCase("0")))
                                .map(GscTfnBean::mapToGscTfnBean).collect(Collectors.toList()));
                gscPorting.setTotalNumbers(porting.getValue().stream().count());
                gscPortings.add(gscPorting);
            });
            cityNumber.setLnsPortings(gscPortings);
            cityNumbers.add(cityNumber);
        });
        return cityNumbers;
    }

    /**
     * Method to return tempUpload url for country documents
     *
     * @param file
     * @param configurationId
     * @return {@link String}
     * @throws TclCommonException
     */
    @Transactional
    public ServiceResponse uploadObjectConfigurationDocument(MultipartFile file, Integer configurationId)
            throws TclCommonException {
        Objects.requireNonNull(file, "FILE SHOULD NOT BE NULL");
        Objects.requireNonNull(configurationId, "CONFIGURATION ID SHOULD NOT BE NULL");
        Optional<OrderGscDetail> orderGscDetail = orderGscDetailRepository.findById(configurationId);
        if (!orderGscDetail.isPresent()) {
            throw new TclCommonException("Order configuration id not present", ResponseResource.R_CODE_ERROR);
        }
        ServiceResponse fileUploadResponse = new ServiceResponse();
        TempUploadUrlInfo tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
                Long.parseLong(tempUploadUrlExpiryWindow), "ORDERS");
        fileUploadResponse.setFileName(tempUploadUrlInfo.getRequestId());
        fileUploadResponse.setUrlPath(tempUploadUrlInfo.getTemporaryUploadUrl());
        return fileUploadResponse;
    }

    /**
     * MEthod to update the uploaded document details in oms
     *
     * @param configurationId
     * @param documentId
     * @param requestId
     * @param url
     * @return {@link GscAttachmentBean}
     */
    @Transactional
    public GscAttachmentBean updateUploadObjectConfigurationDocument(Integer configurationId, Integer documentId,
                                                                     String requestId, String url) {
        Objects.requireNonNull(documentId, "Document ID SHOULD NOT BE NULL");
        Objects.requireNonNull(requestId, "Request ID SHOULD NOT BE NULL");
        Objects.requireNonNull(url, "url SHOULD NOT BE NULL");
        Objects.requireNonNull(configurationId, "CONFIGURATION ID SHOULD NOT BE NULL");
        return fetchOrderGscDetailById(configurationId)
                .flatMap(orderGscDetail -> fetchConfigurationAttachment(documentId))
                .map(gscAttachment -> uploadObjectDocumentForConfiguration(gscAttachment, requestId, url)).get();
    }

    /**
     * Delete porting and non-porting numbers based on specified city
     *
     * @param configurationId
     * @param gscTfnBeans
     * @param serviceType
     * @return List<GscTfnBean>
     */
    @Transactional
    public List<GscTfnBean> deleteNumbers(Integer configurationId, List<GscTfnBean> gscTfnBeans, String serviceType) {
        requireNonNull(configurationId, "Configuration Id cannot be null");
        requireNonNull(gscTfnBeans, "GscTfnBean cannot be null");

        if (LNS.equalsIgnoreCase(serviceType)) {
            Optional.ofNullable(orderGscTfnRepository.findByOrderGscDetailId(configurationId)).get().stream()
                    .filter(orderGscTfn -> orderGscTfn.getCountryCode().contains(getCityCode(gscTfnBeans)))
                    .forEach(orderGscTfn -> orderGscTfnRepository.delete(orderGscTfn));
        }

        Optional.ofNullable(orderProductComponentRepository
                .findByReferenceIdAndType(configurationId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE)).get()
                .stream()
                .filter(orderProductComponent -> serviceType
                        .equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName()))
                .findFirst().ifPresent(filteredComponent -> {
            List<OrderProductComponentsAttributeValue> attributeValues = filterAttributesCityWise(gscTfnBeans,
                    filteredComponent);
            String reservationId = getReservationIdForAttributes(filteredComponent);
            if (!attributeValues.isEmpty() && !serviceType.equalsIgnoreCase(ACANS)
                    && !StringUtils.isEmpty(reservationId)) {
                OrderGscDetail orderGscDetail = orderGscDetailRepository.findById(configurationId).get();
                String orgId = null;
                try {
                    orgId = getOrganisationIdOfCustomer(orderGscDetail);
                } catch (TclCommonException e) {
                    throw new TclCommonRuntimeException("Error in getting Organisation Id");
                }
                tigerServiceHelper.reserveNumbersForOrganisation(orgId,
                        String.valueOf(
                                getOriginCountryCalllingCode(getCountryCodeForOrderGscDetail(orderGscDetail))),
                        serviceType, gscTfnBeans, orderGscDetail.getId(), REF_TYPE_ORDER_GSC_DETAIL,
                        reservationId);
            }
            orderProductComponentsAttributeValueRepository.deleteAll(attributeValues);
        });
        return gscTfnBeans;
    }

    private String getReservationIdForAttributes(OrderProductComponent filteredComponent) {
        OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = Optional
                .ofNullable(
                        orderProductComponentsAttributeValueRepository.findByOrderProductComponent(filteredComponent))
                .get().stream()
                .filter(attribute -> GscConstants.TFN_Reservation_ID
                        .equalsIgnoreCase(attribute.getProductAttributeMaster().getName()))
                .findFirst().orElseGet(OrderProductComponentsAttributeValue::new);

        return Objects.isNull(orderProductComponentsAttributeValue.getAttributeValues()) ? ""
                : orderProductComponentsAttributeValue.getAttributeValues();
    }

    private String getCityCode(List<GscTfnBean> gscTfnBeans) {
        return gscTfnBeans.stream().map(GscTfnBean::getCity).findFirst().get();
    }

    private List<OrderProductComponentsAttributeValue> filterAttributesCityWise(List<GscTfnBean> gscTfnBeans,
                                                                                OrderProductComponent filteredComponent) {
        return Optional
                .ofNullable(
                        orderProductComponentsAttributeValueRepository.findByOrderProductComponent(filteredComponent))
                .get().stream()
                .filter(attribute -> GscConstants.CITY_WISE_QUANTITY_OF_NUMBERS
                        .equalsIgnoreCase(attribute.getProductAttributeMaster().getName())
                        || CITY_WISE_PORTING_SERVICE_NEEDED
                        .equalsIgnoreCase(attribute.getProductAttributeMaster().getName()))
                .filter(orderProductComponentsAttributeValue -> orderProductComponentsAttributeValue
                        .getAttributeValues().contains(getCityCode(gscTfnBeans)))
                .collect(Collectors.toList());
    }

    /**
     * Method to Retrieve the document status for the orderGscId
     *
     * @param orderGscId
     * @param portingRequired
     * @return {@link Map<Integer, List<GscAttachmentBean>>}
     */
    @Transactional
    public List<Integer> getDocumentStatus(Integer orderGscId, String portingRequired) {
        Objects.requireNonNull(orderGscId, "OrderGsc ID cannot be null");
        List<OrderGscDetail> orderGscDetails = orderGscDetailRepository.findByorderGsc(getOrderGsc(orderGscId).get());
        /*
         * Will return map of (configurationId,Pending document lists including the
         * document Name
         */
        /*
         * return orderGscDetails.stream().
         * collect(Collectors.toMap(orderGscDetail->orderGscDetail.getId(),
         * orderGscDetail ->
         * getDocumentsForConfigurationId(orderGscDetail.getId()).stream().filter(
         * gscAttachmentBean ->
         * gscAttachmentBean.getStatus().equalsIgnoreCase("PENDING")).collect(Collectors
         * .toList())));
         */
        List<Integer> configurationIds = new ArrayList<>();
        orderGscDetails.forEach(orderGscDetail -> {
            List<GscAttachmentBean> beans = getDocumentsForConfigurationId(orderGscDetail.getId()).stream()
                    .filter(gscAttachmentBean -> gscAttachmentBean.getStatus().equalsIgnoreCase("PENDING"))
                    .filter(gscAttachmentBean -> {
                        if (portingRequired.equalsIgnoreCase("no")) {
                            return gscAttachmentBean.getDocumentCategory()
                                    .equalsIgnoreCase("Country Specific Documents");
                        } else {
                            return true;
                        }
                    }).filter(gscAttachmentBean -> {
                        return gscAttachmentBean.getDocumentType().equalsIgnoreCase("BOTH")
                                || gscAttachmentBean.getDocumentType().equalsIgnoreCase("UPLOAD");
                    }).collect(Collectors.toList());

            if (beans.size() > 0) {
                configurationIds.add(orderGscDetail.getId());
            } else {
                Try<MstOrderSiteStatus> mstOrderSiteStatus = gscOrderService.getMstOrderSiteStatus(
                        GscConstants.ORDER_ENRICHMENT);
                if (Objects.nonNull(mstOrderSiteStatus)) {
                    orderGscDetail.setMstOrderSiteStatus(mstOrderSiteStatus.get());
                    orderGscDetailRepository.save(orderGscDetail);
                }
            }
        });
        return configurationIds;
    }

    /**
     * Get Object Storage Configuartion Attachments
     *
     * @param configurationId
     * @param documentId
     * @param downloadTemplate
     * @return
     */
    public Try<String> getObjectStorageConfigurationAttachmentForId(Integer configurationId, Integer documentId,
                                                                    Boolean downloadTemplate) {
        return fetchOrderGscDetailById(configurationId)
                .flatMap(orderGscDetail -> fetchConfigurationAttachment(documentId))
                .mapTry(downloadTemplate ? this::getObjectStorageTemplateForConfiguration : this::getObjectStorageAttachmentForConfiguration);
    }

    private String getObjectStorageTemplateForConfiguration(GscAttachments gscAttachment)
            throws ObjectNotFoundException, TclCommonException, IllegalArgumentException {
        OmsAttachment omsAttachment = omsAttachmentRepository
                .findByReferenceNameAndAttachmentType(gscAttachment.getDocumentUId(), GscConstants.OTHERS);
        if (Objects.isNull(omsAttachment)) {
            throw notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                    String.format("Template not uploaded yet for attachment id: %s", gscAttachment.getId()));
        }
        return gscAttachmentHelper.fetchObjectStorageAttachmentResource(omsAttachment.getErfCusAttachmentId())
                .orElseThrow(() -> notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                        String.format("Template not found for attachment id: %s", gscAttachment.getId())));
    }

    private String getObjectStorageAttachmentForConfiguration(GscAttachments gscAttachment)
            throws ObjectNotFoundException, TclCommonException, IllegalArgumentException {
        Integer attachmentId = gscAttachment.getOmsAttachment().getErfCusAttachmentId();
        if (Objects.isNull(attachmentId)) {
            throw notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                    String.format("Document has not been uploaded yet for attachment id: %s", gscAttachment.getId()));
        }
        return gscAttachmentHelper.fetchObjectStorageAttachmentResource(attachmentId)
                .orElseThrow(() -> notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                        String.format("Document not found for attachment id: %s", gscAttachment.getId())));
    }

    public List<GscTfnBean> getCityNpaConfigurationList(Integer configurationId, String serviceType) {
        Objects.requireNonNull(configurationId);
        Objects.requireNonNull(serviceType);
        List<GscTfnBean> gscTfnBeans = new ArrayList<>();

        List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new ArrayList<>();
        Optional<OrderProductComponent> orderProductComponent = Optional.ofNullable(orderProductComponentRepository
                .findByReferenceIdAndType(configurationId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE)).get()
                .stream()
                .findFirst();

        if(orderProductComponent.isPresent()){
            orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent.get(),ATTR_CITY_WISE_AREA_CODE);
        }
        if(!CollectionUtils.isEmpty(orderProductComponentsAttributeValues)){
            gscTfnBeans = orderProductComponentsAttributeValues.stream().map(OrderProductComponentsAttributeValue::getAttributeValues)
                    .map(attributeValue -> {
                        String[] cityWiseNpa = attributeValue.split(":");
                        GscTfnBean gscTfnBean = new GscTfnBean();
                        gscTfnBean.setCity(cityWiseNpa[0]);
                        gscTfnBean.setNpa(cityWiseNpa[1]);
                        return gscTfnBean;
                    }).collect(Collectors.toList());
        }
        return gscTfnBeans;
    }

}
