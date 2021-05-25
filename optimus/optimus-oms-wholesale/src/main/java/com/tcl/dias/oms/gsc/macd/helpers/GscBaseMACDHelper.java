package com.tcl.dias.oms.gsc.macd.helpers;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscDocumentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gsc.macd.GscMACDUtils;
import com.tcl.dias.oms.gsc.macd.MACDOrderRequest;
import com.tcl.dias.oms.gsc.macd.MACDOrderResponse;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_METHOD;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PAYMENT_TERM;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_CUSTOMER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CUSTOMER_SECS_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;
import static com.tcl.dias.oms.gsc.util.GscConstants.SOURCE_SYSTEM_LEGACY;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;

@Component
public class GscBaseMACDHelper implements IGscMACDHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscBaseMACDHelper.class);

    @Autowired
    GscQuoteService gscQuoteService;

    @Autowired
    GscQuoteDetailService gscQuoteDetailService;

    @Autowired
    GscQuoteAttributeService gscQuoteAttributeService;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderToLeRepository orderToLeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;

    @Autowired
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;

    @Autowired
    MstProductOfferingRepository mstProductOfferingRepository;

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

    @Autowired
    GscMACDUtils gscMACDUtils;

    private MstOmsAttribute getMstAttributeByName(String attributeName) {
        return Optional.ofNullable(
                mstOmsAttributeRepository.findByNameAndIsActive(attributeName, GscConstants.STATUS_ACTIVE))
                .orElse(ImmutableList.of())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Mst Oms Attribute with name %s not found", attributeName)));
    }

    @Override
    public Quote cloneQuoteFromConfiguration(String requestType, SIServiceDetailDataBean serviceDetail,
                                             SIOrderDataBean orderDataBean, Map<String, Object> data) {
        Integer customerId = Ints.tryParse((String) data.get(CFG_DATA_KEY_CUSTOMER_ID));
        Preconditions.checkArgument(!Objects.isNull(customerId), "Customer Id is mandatory");
        // create quote with solutions
        GscQuoteDataBean quoteDataBean = new GscQuoteDataBean();
        quoteDataBean.setCustomerId(customerId);
        quoteDataBean.setProductFamilyName(GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
        quoteDataBean.setAccessType(serviceDetail.getAccessType());
        GscSolutionBean solutionBean = new GscSolutionBean();
        String accessType = serviceDetail.getAccessType();
        String productName = serviceDetail.getGscProductName();
        solutionBean.setSolutionCode(String.format("%s%s", productName, accessType));
        solutionBean.setOfferingName(String.format("%s on %s", productName, accessType));
        solutionBean.setProductName(productName);
        quoteDataBean.setSolutions(ImmutableList.of(solutionBean));
        quoteDataBean.setQuoteType(ORDER_TYPE_MACD);
        quoteDataBean.setQuoteCategory(requestType);
        quoteDataBean.setSupplierLegalId(orderDataBean.getErfCustSpLeId());
        quoteDataBean.setCustomerLeId(orderDataBean.getErfCustLeId());
        quoteDataBean.setSecsId(orderDataBean.getTpsSecsId());
        quoteDataBean = gscQuoteService.createQuote(quoteDataBean);
        Integer newQuoteId = quoteDataBean.getQuoteId();
        Integer newSolutionId = quoteDataBean.getSolutions().get(0).getSolutionId();
        Integer newQuoteGscId = quoteDataBean.getSolutions().get(0).getGscQuotes().get(0).getId();
        Quote newQuote = quoteRepository.findById(newQuoteId).get();

        QuoteToLe newQuoteToLe = quoteToLeRepository.findByQuote(newQuote).get(0);
        newQuoteToLe.setSourceSystem(SOURCE_SYSTEM_LEGACY);
        newQuoteToLe.setErfServiceInventoryParentOrderId(orderDataBean.getId());
        newQuoteToLe.setErfCusCustomerLegalEntityId(orderDataBean.getErfCustLeId());
        newQuoteToLe.setErfCusSpLegalEntityId(orderDataBean.getErfCustSpLeId());
        newQuoteToLe.setCurrencyId(orderDataBean.getErfCustCurrencyId());
        if (Objects.nonNull(serviceDetail.getContractTerm())) {
            //PT-14232 Temp fix - Convert Double to Int
            newQuoteToLe.setTermInMonths(serviceDetail.getContractTerm().intValue() + " months");
        }

        //save SECS ID in quote to le attributes
//        QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
//        quoteLeAttributeValue.setAttributeValue(orderDataBean.getTpsSecsId());
//        quoteLeAttributeValue.setDisplayValue(ATTR_CUSTOMER_SECS_ID);
//        quoteLeAttributeValue.setQuoteToLe(newQuoteToLe);
//        quoteLeAttributeValue.setMstOmsAttribute(getMstAttributeByName(ATTR_CUSTOMER_SECS_ID));
//        quoteLeAttributeValueRepository.save(quoteLeAttributeValue);

        GscDocumentBean gscDocumentBean = new GscDocumentBean();
        gscDocumentBean.setCustomerId(Integer.valueOf(orderDataBean.getErfCustCustomerId()));
        gscDocumentBean.setCustomerLegalEntityId(orderDataBean.getErfCustLeId());
        gscDocumentBean.setProductName(GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
        gscDocumentBean.setQuoteId(newQuoteId);
        gscDocumentBean.setQuoteLeId(newQuoteToLe.getId());
        gscDocumentBean.setSupplierLegalEntityId(orderDataBean.getErfCustSpLeId());
        gscQuoteAttributeService.createGscDocument(gscDocumentBean);

        //saveAdditionalQuoteToLeAttributes(orderDataBean, newQuoteToLe);

        LOGGER.info("MACD successfully created quote: {}, solution: {}, quoteGsc: {}", newQuoteId, newSolutionId,
                newQuoteGscId);
        return newQuote;
    }


    private void saveAdditionalQuoteToLeAttributes(SIOrderDataBean orderDataBean, QuoteToLe newQuoteToLe) {
        List<QuoteLeAttributeValue> quoteLeAttributeValues = new ArrayList<>();

        //save SECS ID in quote to le attributes
        QuoteLeAttributeValue tpsSecsId = new QuoteLeAttributeValue();
        tpsSecsId.setAttributeValue(orderDataBean.getTpsSecsId());
        tpsSecsId.setDisplayValue(ATTR_CUSTOMER_SECS_ID);
        tpsSecsId.setQuoteToLe(newQuoteToLe);
        tpsSecsId.setMstOmsAttribute(getMstAttributeByName(ATTR_CUSTOMER_SECS_ID));

        //save BILLING FREQUENCY in quote to le attributes
        QuoteLeAttributeValue billingFrequency = new QuoteLeAttributeValue();

        orderDataBean.getServiceDetails().stream().forEach(siServiceDetailDataBean -> {
            if (Objects.nonNull(siServiceDetailDataBean.getBillingFrequency())) {
                billingFrequency.setAttributeValue(siServiceDetailDataBean.getBillingFrequency());
            }
        });
        billingFrequency.setDisplayValue(BILLING_FREQUENCY);
        billingFrequency.setQuoteToLe(newQuoteToLe);
        billingFrequency.setMstOmsAttribute(getMstAttributeByName(BILLING_FREQUENCY));

        //save BILLING METHOD in quote to le attributes
        QuoteLeAttributeValue billingMethod = new QuoteLeAttributeValue();
        orderDataBean.getServiceDetails().stream().forEach(siServiceDetailDataBean -> {
            if (Objects.nonNull(siServiceDetailDataBean.getBillingMethod())) {
                billingMethod.setAttributeValue(siServiceDetailDataBean.getBillingMethod());
            }
        });
        billingMethod.setDisplayValue(BILLING_METHOD);
        billingMethod.setQuoteToLe(newQuoteToLe);
        billingMethod.setMstOmsAttribute(getMstAttributeByName(BILLING_METHOD));

        //save PAYMENT TERM in quote to le attributes
        QuoteLeAttributeValue paymentTerm = new QuoteLeAttributeValue();
        orderDataBean.getServiceDetails().stream().forEach(siServiceDetailDataBean -> {
            if (Objects.nonNull(siServiceDetailDataBean.getPaymentTerm())) {
                paymentTerm.setAttributeValue(siServiceDetailDataBean.getPaymentTerm());
            }
        });
        paymentTerm.setDisplayValue(PAYMENT_TERM);
        paymentTerm.setQuoteToLe(newQuoteToLe);
        paymentTerm.setMstOmsAttribute(getMstAttributeByName(PAYMENT_TERM));

        quoteLeAttributeValues.addAll(Arrays.asList(tpsSecsId, billingFrequency, billingMethod, paymentTerm));
        quoteLeAttributeValueRepository.saveAll(quoteLeAttributeValues);
    }

    private Order createOrder(SIOrderDataBean orderDataBean) {
        Order order = new Order();
        Optional<User> userOpt = Optional
                .ofNullable(userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE));
        Integer userId = userOpt.map(User::getId).orElse(0);
        order.setQuoteCreatedBy(userId);
        order.setCreatedBy(userId);
        order.setCreatedTime(new Date());
        order.setCustomer(userOpt.map(User::getCustomer)
                .orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_NOT_FOUND)));
        order.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
        order.setIsDemo(CommonConstants.BDEACTIVATE);
        order.setStatus(CommonConstants.BACTIVE);
        order.setOrderCode(Utils.generateRefId(GscConstants.GSC_PRODUCT_NAME.toUpperCase()));
        return orderRepository.save(order);
    }

    private OrderToLe createOrderToLe(Order newOrder, SIOrderDataBean orderData) {
        OrderToLe orderToLe = new OrderToLe();
        orderToLe.setOrder(newOrder);
        orderToLe.setErfCusCustomerLegalEntityId(orderData.getErfCustLeId());
        orderToLe.setErfCusSpLegalEntityId(orderData.getErfCustSpLeId());
        orderToLe.setCurrencyId(orderData.getErfCustCurrencyId());
        orderToLe.setSourceSystem(SOURCE_SYSTEM_LEGACY);
        orderToLe.setErfServiceInventoryParentOrderId(orderData.getId());
        orderToLe.setOrderType(ORDER_TYPE_MACD);
        OrderToLe newOrderToLe = orderToLeRepository.save(orderToLe);

        //save SECS ID in orders to le attribute value
        OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
        ordersLeAttributeValue.setAttributeValue(orderData.getTpsSecsId());
        ordersLeAttributeValue.setDisplayValue(ATTR_CUSTOMER_SECS_ID);
        ordersLeAttributeValue.setOrderToLe(newOrderToLe);
        ordersLeAttributeValue.setMstOmsAttribute(getMstAttributeByName(ATTR_CUSTOMER_SECS_ID));
        ordersLeAttributeValueRepository.save(ordersLeAttributeValue);

        return newOrderToLe;
    }

    private OrderToLeProductFamily createProductFamily(OrderToLe newOrderToLe, SIOrderDataBean orderDataBean) {
        OrderToLeProductFamily productFamily = new OrderToLeProductFamily();
        productFamily.setOrderToLe(newOrderToLe);
        productFamily.setMstProductFamily(mstProductFamilyRepository
                .findByNameAndStatus(GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), CommonConstants.BACTIVE));
        return orderToLeProductFamilyRepository.save(productFamily);
    }

    private OrderProductSolution createSolution(OrderToLeProductFamily newProductFamily,
                                                SIServiceDetailDataBean detailDataBean) {
        OrderProductSolution solution = new OrderProductSolution();
        MstProductOffering mstProductOffering = Optional
                .ofNullable(mstProductOfferingRepository
                        .findByProductNameAndStatus(detailDataBean.getErfPrdCatalogOfferingName(), STATUS_ACTIVE))
                .orElseThrow(() -> {
                    LOGGER.warn(String.format("Master product offering with name : %s not found",
                            detailDataBean.getErfPrdCatalogOfferingName()));
                    return new TclCommonRuntimeException(ExceptionConstants.MST_PRODUCT_OFFERING_EMPTY);
                });
        solution.setMstProductOffering(mstProductOffering);
        solution.setOrderToLeProductFamily(newProductFamily);
        solution.setSolutionCode(Utils.generateUid());
        GscSolutionBean solutionBean = new GscSolutionBean();
        solutionBean.setProductName(detailDataBean.getGscProductName());
        solutionBean.setAccessType(detailDataBean.getAccessType());
        solutionBean.setOfferingName(detailDataBean.getErfPrdCatalogOfferingName());
        solutionBean.setSolutionCode(solution.getSolutionCode());
        solution.setProductProfileData(GscUtils.toJson(solutionBean));
        return orderProductSolutionRepository.save(solution);
    }

    private OrderGsc createOrderGsc(SIServiceDetailDataBean detailDataBean, OrderProductSolution newSolution,
                                    OrderToLe newOrderToLe) {
        OrderGsc orderGsc = new OrderGsc();
        orderGsc.setOrderToLe(newOrderToLe);
        orderGsc.setOrderProductSolution(newSolution);
        orderGsc.setProductName(detailDataBean.getGscProductName());
        orderGsc.setAccessType(detailDataBean.getAccessType());
        orderGsc.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
        orderGsc.setCreatedBy(Utils.getSource());
        orderGsc.setStatus(CommonConstants.BACTIVE);
        orderGsc.setName(String.format("%s_%s%s", GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(),
                detailDataBean.getGscProductName(), detailDataBean.getAccessType()));
        return orderGscRepository.save(orderGsc);
    }

    @Override
    public Order cloneOrderFromConfiguration(SIServiceDetailDataBean configuration, SIOrderDataBean orderData,
                                             Map<String, Object> data) {
        Order newOrder = createOrder(orderData);
        OrderToLe newOrderToLe = createOrderToLe(newOrder, orderData);
        OrderToLeProductFamily newProductFamily = createProductFamily(newOrderToLe, orderData);
        OrderProductSolution newSolution = createSolution(newProductFamily, configuration);
        createOrderGsc(configuration, newSolution, newOrderToLe);
        return newOrder;
    }

    protected OrderGscDetail createOrderGscDetail(SIServiceDetailDataBean configuration, OrderGsc newOrderGsc) {
        OrderGscDetail detail = new OrderGscDetail();
        detail.setOrderGsc(newOrderGsc);
        if (!Strings.isNullOrEmpty(configuration.getSourceCountryCode())) {
            String srcCountry = gscMACDUtils.getCountryNameForCode(configuration.getSourceCountryCode());
            String src = Joiner.on(":").skipNulls().join(new String[]{srcCountry, configuration.getGscSourceCity()});
            detail.setSrc(src);
        }
        if (!Strings.isNullOrEmpty(configuration.getDestinationCountryCode())) {
            String destCountry = gscMACDUtils.getCountryNameForCode(configuration.getDestinationCountryCode());
            String dest = Joiner.on(":").skipNulls()
                    .join(new String[]{destCountry, configuration.getGscDestinationCity()});
            detail.setDest(dest);
        }
        detail.setCreatedBy(Utils.getSource());
        detail.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
        return orderGscDetailRepository.save(detail);
    }

    protected OrderProductComponent createProductComponent(SIServiceDetailDataBean detailDataBean,
                                                           OrderGscDetail orderGscDetail, OrderToLe orderToLe) {
        // create component with type 'gsip.config'
        OrderProductComponent orderProductComponent = new OrderProductComponent();
        MstProductFamily mstProductFamily = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe).stream()
                .findFirst().get().getMstProductFamily();
        orderProductComponent.setMstProductFamily(mstProductFamily);
        MstProductComponent mstProductComponent = Optional
                .ofNullable(mstProductComponentRepository.findByName(detailDataBean.getGscProductName()))
                .orElseThrow(() -> {
                    LOGGER.warn(String.format("Master product component not found for name: %s",
                            detailDataBean.getGscProductName()));
                    return new TclCommonRuntimeException(ExceptionConstants.MST_PRODUCT_EMPTY);
                });
        orderProductComponent.setMstProductComponent(mstProductComponent);
        orderProductComponent.setReferenceId(orderGscDetail.getId());
        orderProductComponent.setType(GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
        orderProductComponentRepository.save(orderProductComponent);
        // create component with type 'gsip' for product name
        orderProductComponent = new OrderProductComponent();
        orderProductComponent.setMstProductFamily(mstProductFamily);
        mstProductComponent = Optional
                .ofNullable(mstProductComponentRepository.findByName(detailDataBean.getGscProductName()))
                .orElseThrow(() -> {
                    LOGGER.warn(String.format("Master product component not found for name: %s",
                            detailDataBean.getGscProductName()));
                    return new TclCommonRuntimeException(ExceptionConstants.MST_PRODUCT_EMPTY);
                });
        orderProductComponent.setMstProductComponent(mstProductComponent);
        orderProductComponent.setReferenceId(orderGscDetail.getId());
        orderProductComponent.setType(GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        orderProductComponentRepository.save(orderProductComponent);
        // create component with type 'gsip' for access type
        orderProductComponent = new OrderProductComponent();
        orderProductComponent.setMstProductFamily(mstProductFamily);
        mstProductComponent = Optional
                .ofNullable(mstProductComponentRepository.findByName(detailDataBean.getAccessType()))
                .orElseThrow(() -> {
                    LOGGER.warn(String.format("Master product component not found for name: %s",
                            detailDataBean.getAccessType()));
                    return new TclCommonRuntimeException(ExceptionConstants.MST_PRODUCT_EMPTY);
                });
        orderProductComponent.setMstProductComponent(mstProductComponent);
        orderProductComponent.setReferenceId(orderGscDetail.getId());
        orderProductComponent.setType(GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        return orderProductComponentRepository.save(orderProductComponent);
    }

    @Override
    public List<OrderGscDetail> cloneConfiguration(SIServiceDetailDataBean configuration, SIOrderDataBean orderDataBean, Order newOrder,
                                                   Map<String, Object> data) {
        OrderGsc orderGsc = orderToLeRepository.findByOrder(newOrder).stream().findFirst()
                .map(orderGscRepository::findByOrderToLe).flatMap(orderGscs -> orderGscs.stream().findFirst()).get();
        OrderGscDetail newOrderGscDetail = createOrderGscDetail(configuration, orderGsc);
        createProductComponent(configuration, newOrderGscDetail, orderToLeRepository.findByOrder(newOrder).get(0));
        return ImmutableList.of(newOrderGscDetail);
    }

    private boolean checkSourceAndDestinationCountry(SIServiceDetailDataBean serviceDetails, String sourceCountry,
                                                     String destinationCountry) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(destinationCountry), "Destination country cannot be empty");
        return sourceCountry.equalsIgnoreCase(serviceDetails.getGscSourceCountry())
                && destinationCountry.equalsIgnoreCase(serviceDetails.getGscDestinationCountry());
    }

    private boolean checkSourceCountry(SIServiceDetailDataBean serviceDetails, String sourceCountry) {
        return sourceCountry.equalsIgnoreCase(serviceDetails.getGscSourceCountry());
    }

    @Override
    public MACDOrderResponse handleAddCountryRequest(String requestType, SIServiceDetailDataBean detailDataBean,
                                                     SIOrderDataBean orderDataBean, Map<String, Object> data) {
        return MACDOrderResponse.failure(String.format("MACD request type: %s not supported for specified GSC product",
                MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY));
    }

}
