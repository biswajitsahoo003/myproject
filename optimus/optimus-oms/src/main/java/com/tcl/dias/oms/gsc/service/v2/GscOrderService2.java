package com.tcl.dias.oms.gsc.service.v2;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CFG_TYPE_REFERENCE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_DOMESTIC_OUTBOUND_EXCEL;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_OUTBOUND_EXCEL;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_SURCHARGE_EXCEL;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_COMPONENT_TYPE_ORDER_GSC;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tcl.dias.common.beans.ExpectedDeliveryDateBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscSla;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteGscSla;
import com.tcl.dias.oms.entity.entities.QuoteGscTfn;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscTfnRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscOrderBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOrderLeBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.exception.Exceptions;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscComponentAttributeValuesHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/*
 *
 *  @author Syed Ali
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 *
 */
@Service
public class GscOrderService2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscOrderService.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    MstOrderSiteStageRepository mstOrderSiteStageRepository;

    @Autowired
    UserService userService;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;

    @Autowired
    QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

    @Autowired
    ProductSolutionRepository productSolutionRepository;

    @Autowired
    QuoteGscRepository quoteGscRepository;

    @Autowired
    QuoteGscDetailsRepository quoteGscDetailsRepository;

    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;

    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Autowired
    QuoteGscSlaRepository quoteGscSlaRepository;

    @Autowired
    OrderToLeRepository orderToLeRepository;

    @Autowired
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

    @Autowired
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

    @Autowired
    EngagementRepository engagementRepository;

    @Autowired
    PartnerService partnerService;

    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderGscSlaRepository orderGscSlaRepository;

    @Autowired
    QuoteGscTfnRepository quoteGscTfnRepository;

    @Autowired
    GscComponentAttributeValuesHelper attributeValuesPopulator;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    @Autowired
    OrderGscTfnRepository orderGscTfnRepository;

    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${expected.delivery.date.queue}")
    String expectedDeliveryDateQueue;

    @Autowired
    OmsSfdcService omsSfdcService;

    @Autowired
    GscQuotePdfService gscQuotePdfService;

    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;

    @Value("${rabbitmq.customer.le.update.ss}")
    String updateSSQueue;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    /**
     * Bulk update configuration attributes for multiple solutions and product
     * components
     *
     * @param orderId
     * @param solutions
     * @return
     */
    @Transactional
    public List<GscOrderSolutionBean> updateProductComponentAttributesForSolutions(Integer orderId,
                                                                                   List<GscOrderSolutionBean> solutions) throws TclCommonException {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        if (solutions.isEmpty()) {
            throw new TclCommonException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
        }
        Order order = fetchOrderById(orderId);
        return updateProductComponentAttributesForSolutions(solutions, order);
    }

    /**
     * Method to fetch order based on id.
     *
     * @param orderId
     * @return
     * @throws TclCommonException
     */
    public Order fetchOrderById(Integer orderId) throws TclCommonException {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Order order = orderRepository.findByIdAndStatus(orderId, STATUS_ACTIVE);
        if (Objects.isNull(order)) {
            throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
        }
        return order;
    }

    /**
     * Method to update product component attributes.
     *
     * @param solutionBeans
     * @param order
     * @return
     */
    public List<GscOrderSolutionBean> updateProductComponentAttributesForSolutions(
            List<GscOrderSolutionBean> solutionBeans, Order order) {
        solutionBeans.stream()
                .map(gscOrderSolutionBean -> Optional
                        .ofNullable(gscOrderSolutionBean.getGscOrders()).orElse(ImmutableList.of()))
                .flatMap(List::stream)
                .map(gscOrderSolutionBean -> Optional.ofNullable(gscOrderSolutionBean.getConfigurations())
                        .orElse(ImmutableList.of()))
                .flatMap(List::stream).map(gscOrderSolutionBean -> Optional
                .ofNullable(gscOrderSolutionBean.getProductComponents()).orElse(ImmutableList.of()))
                .flatMap(List::stream).forEach(gscOrderProductComponentBean -> {
            try {
                processProductComponent(gscOrderProductComponentBean, order);
            } catch (TclCommonException e) {
                e.printStackTrace();
            }
        });
        return solutionBeans;
    }

    /**
     * Method to process attributes.
     *
     * @param gscOrderProductComponentBean
     * @param order
     * @return
     * @throws TclCommonException
     */
    private GscOrderProductComponentBean processProductComponent(
            GscOrderProductComponentBean gscOrderProductComponentBean, Order order) throws TclCommonException {

        Optional<OrderProductComponent> optionalOrderProductComponent = orderProductComponentRepository
                .findById(gscOrderProductComponentBean.getId());
        if (!optionalOrderProductComponent.isPresent()) {
            throw new TclCommonException(ExceptionConstants.ORDER_PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
        }
        return processProductComponent(optionalOrderProductComponent.get(), gscOrderProductComponentBean, order);
    }

    /**
     * Method to update configuration stage status.
     *
     * @param configurationId
     * @param request
     * @return
     * @throws TclCommonException
     */
    @Transactional
    public GscOrderStatusStageUpdate updateOrderConfigurationStageStatus(Integer configurationId,
                                                                         GscOrderStatusStageUpdate request) throws TclCommonException {
        Objects.requireNonNull(configurationId);
        Objects.requireNonNull(request);
        OrderStatusStageUpdateContext context = createOrderStatusStageUpdateContext(configurationId, request);
        populateMstOrderSiteStatus(context);
        populateMstOrderSiteStage(context);
        updateConfigurationStatusStage(context);
        return context.gscOrderStatusStageUpdate;
    }

    /**
     * Context class for OrderStatusStage
     */
    public static class OrderStatusStageUpdateContext {
        Integer configurationId;
        MstOrderSiteStage mstOrderSiteStage;
        MstOrderSiteStatus mstOrderSiteStatus;
        GscOrderStatusStageUpdate gscOrderStatusStageUpdate;
    }

    /**
     * creates context for OrderStatusStageUpdateContext
     *
     * @param configurationId
     * @param request
     * @return
     */
    private OrderStatusStageUpdateContext createOrderStatusStageUpdateContext(Integer configurationId,
                                                                              GscOrderStatusStageUpdate request) {
        OrderStatusStageUpdateContext context = new OrderStatusStageUpdateContext();
        context.configurationId = configurationId;
        context.gscOrderStatusStageUpdate = request;
        return context;
    }

    /**
     * Method to populate mstordersitestatus into context.
     *
     * @param context
     * @return
     * @throws TclCommonException
     */
    private OrderStatusStageUpdateContext populateMstOrderSiteStatus(OrderStatusStageUpdateContext context)
            throws TclCommonException {
        String configurationStatus = context.gscOrderStatusStageUpdate.getConfigurationStatusName();
        context.mstOrderSiteStatus = getMstOrderSiteStatus(configurationStatus);
        return context;
    }

    /**
     * Method to get mstordersitestatus
     *
     * @param status
     * @return
     * @throws TclCommonException
     */
    public MstOrderSiteStatus getMstOrderSiteStatus(String status) throws TclCommonException {
        MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository.findByName(status);
        if (Objects.isNull(mstOrderSiteStatus)) {
            throw new TclCommonException(ExceptionConstants.MST_ORDER_SITE_STATUS_EMPTY,
                    ResponseResource.R_CODE_NOT_FOUND);
        }
        return mstOrderSiteStatus;
    }

    /**
     * Method to update configuration status stage.
     *
     * @param context
     * @return
     */
    private OrderStatusStageUpdateContext updateConfigurationStatusStage(OrderStatusStageUpdateContext context) {
        Optional<OrderGscDetail> gscDetail = orderGscDetailRepository.findById(context.configurationId);
        gscDetail.ifPresent(gscOrderdetail -> {
            gscOrderdetail.setMstOrderSiteStatus(context.mstOrderSiteStatus);
            gscOrderdetail.setMstOrderSiteStage(context.mstOrderSiteStage);
            orderGscDetailRepository.save(gscOrderdetail);
        });
        return context;
    }

    /**
     * Method to populate mstordersitestage.
     *
     * @param context
     * @return
     * @throws TclCommonException
     */
    private OrderStatusStageUpdateContext populateMstOrderSiteStage(OrderStatusStageUpdateContext context)
            throws TclCommonException {
        if (Objects.nonNull(context.gscOrderStatusStageUpdate.getConfigurationStageName())) {
            context.mstOrderSiteStage = getMstOrderSiteStage(
                    context.gscOrderStatusStageUpdate.getConfigurationStageName());
        }
        return context;
    }

    /**
     * Method to get mstordersitestage.
     *
     * @param stage
     * @return
     * @throws TclCommonException
     */
    private MstOrderSiteStage getMstOrderSiteStage(String stage) throws TclCommonException {
        MstOrderSiteStage mstOrderSiteStage = mstOrderSiteStageRepository.findByName(stage);
        if (Objects.isNull(mstOrderSiteStage)) {
            throw new TclCommonException(ExceptionConstants.MST_ORDER_SITE_STAGE_EMPTY,
                    ResponseResource.R_CODE_NOT_FOUND);
        }
        return mstOrderSiteStage;
    }

    /**
     * Method to get order
     *
     * @param context
     * @return
     */
    private GscApproveQuoteContext getOrder(GscApproveQuoteContext context) {
        Quote quote = context.quote;
        context.order = Optional.ofNullable(orderRepository.findByQuoteAndStatus(quote, GscConstants.STATUS_ACTIVE))
                .orElseGet(() -> {
                    Order order = new Order();
                    order.setCreatedBy(quote.getCreatedBy());
                    order.setCreatedTime(new Date());
                    order.setStatus(quote.getStatus());
                    order.setTermInMonths(quote.getTermInMonths());
                    order.setCustomer(quote.getCustomer());
                    order.setEffectiveDate(quote.getEffectiveDate());
                    order.setStatus(quote.getStatus());
                    order.setQuote(quote);
                    order.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
                    order.setStartDate(new Date());
                    order.setStatus(quote.getStatus());
                    order.setOrderCode(context.quote.getQuoteCode());
                    order.setQuoteCreatedBy(quote.getCreatedBy());
                    order.setEngagementOptyId(quote.getEngagementOptyId());
                    context.gscNewOrder = true;
                    return order;
                });
        return context;
    }

    /**
     * Create order after quote approved by customer
     *
     * @param quoteId
     * @return {@link GscOrderDataBean}
     */
    @Transactional
    public GscOrderDataBean approveQuotes(Integer quoteId, HttpServletResponse response) throws TclCommonException {
        GscApproveQuoteContext context = createApproveQuoteContext(quoteId, response);
        getQuote(context);
        getOrder(context);
        saveOrder(context);
        getQuoteToLe(context);
        getQuoteToLeAttribute(context);
        getProductFamily(context);
        getQuoteProductSolution(context);
        getQuoteGsc(context);
        getQuoteGscDetails(context);
        getQuoteProductComponent(context);
        getQuoteProductComponentAttributeValues(context);
        getQuoteGscSla(context);
        createAndSaveOrderToLe(context);
        updateOpportunityInSfdc(context);
        processCofPdf(context);
        checkMSAandSSDocuments(context);
        return context.gscOrderDataBean;
    }

    /**
     * This file contains the GscApproveQuoteContext.java class.
     */
    private static class GscApproveQuoteContext {
        User user;
        Quote quote;
        Order order;
        boolean gscNewOrder;
        List<QuoteToLe> quoteToLe;
        Set<OrderToLe> orderToLe;
        List<QuoteLeAttributeValue> quoteLeAttributeValue;
        Set<OrdersLeAttributeValue> orderToLeAttributeValue;
        List<QuoteToLeProductFamily> quoteToLeProductFamily;
        Set<OrderToLeProductFamily> orderToLeProductFamily;
        List<ProductSolution> quoteProductSolutions;
        Set<OrderProductSolution> orderProductSolution;
        List<QuoteGsc> quoteGsc;
        Set<OrderGsc> orderGsc;
        List<QuoteGscDetail> quoteGscDetail;
        Set<OrderGscDetail> orderGscDetail;
        List<QuoteGscSla> quoteGscSla;
        Set<OrderGscSla> orderGscSla;
        List<QuoteProductComponent> quoteProductComponents;
        Set<OrderProductComponent> orderProductComponents;
        List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues;
        Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues;
        GscOrderDataBean gscOrderDataBean;
        List<GscOrderProductComponentBean> gscOrderProductComponentBean = new ArrayList<>();
        HttpServletResponse response;
        Date cofSignedDate;
        List<QuoteDelegation> quoteDelegate;
        String ipAddress;
        Map<String, String> cofObjectMapper;
        boolean isDocuSign;
    }

    /**
     * createApproveQuoteContext
     *
     * @param quoteId
     * @param response
     * @return
     */
    private GscApproveQuoteContext createApproveQuoteContext(Integer quoteId, HttpServletResponse response) {
        GscApproveQuoteContext context = new GscApproveQuoteContext();
        context.user = userService.getUserId(Utils.getSource());
        Quote quote = new Quote();
        quote.setId(quoteId);
        context.quote = quote;
        context.gscNewOrder = false;
        context.response = response;
        context.cofSignedDate = new Timestamp(System.currentTimeMillis());
        return context;
    }

    /**
     * Get Quote By ID
     *
     * @param quoteId
     * @return
     */
    private Quote getQuoteById(Integer quoteId) throws TclCommonException {
        Optional<Quote> optionalQuote = quoteRepository.findById(quoteId);
        if (!optionalQuote.isPresent()) {
            throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
        }
        return optionalQuote.get();
    }

    /**
     * Method to get quote.
     *
     * @param context
     * @return
     * @throws TclCommonException
     */
    private GscApproveQuoteContext getQuote(GscApproveQuoteContext context) throws TclCommonException {
        context.quote = getQuoteById(context.quote.getId());
        return context;
    }

    /**
     * Method to save order
     *
     * @param context
     * @return
     */
    private GscApproveQuoteContext saveOrder(GscApproveQuoteContext context) {
        if ((context.gscNewOrder) && Objects.isNull(context.order.getId())) {
            context.order = orderRepository.save(context.order);
        }
        context.gscOrderDataBean = new GscOrderDataBean();
        context.gscOrderDataBean.setOrderId(context.order.getId());
        context.gscOrderDataBean.setCustomerId(context.order.getCustomer().getErfCusCustomerId());
        context.gscOrderDataBean.setQuoteId(context.order.getQuote().getId());
        context.gscOrderDataBean.setOrderCode(context.quote.getQuoteCode());
        context.gscOrderDataBean.setCreatedTime(context.order.getCreatedTime());
        context.gscOrderDataBean.setCreatedBy(context.order.getCreatedBy());
        return context;
    }

    /**
     * Method to get quotetole
     *
     * @param context
     * @return
     */
    private GscApproveQuoteContext getQuoteToLe(GscApproveQuoteContext context) {
        List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(context.quote);
        context.quoteToLe = quoteToLes;
        return context;
    }

    /**
     * Method to get quotetoleattribute.
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getQuoteToLeAttribute(GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteLeAttributeValue = new ArrayList<>();
        gscApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
            List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
                    .findByQuoteToLe(quoteToLe);
            quoteToLe.setQuoteLeAttributeValues(new HashSet<>(quoteLeAttributeValues));
            if (!quoteLeAttributeValues.isEmpty()) {
                gscApproveQuoteContext.quoteLeAttributeValue.addAll(quoteLeAttributeValues);
            }
        });
        return gscApproveQuoteContext;
    }

    /**
     * Method to get product family
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getProductFamily(GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteToLeProductFamily = new ArrayList<>();
        gscApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
            MstProductFamily gsipProductFamily = mstProductFamilyRepository
                    .findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
            QuoteToLeProductFamily quoteToLeProductFamilies = quoteToLeProductFamilyRepository
                    .findByQuoteToLeAndMstProductFamily(quoteToLe, gsipProductFamily);
            gscApproveQuoteContext.quoteToLeProductFamily.add(quoteToLeProductFamilies);
        });
        return gscApproveQuoteContext;
    }

    /**
     * Method to get quoteproductsolution
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getQuoteProductSolution(GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteProductSolutions = new ArrayList<>();
        gscApproveQuoteContext.quoteToLeProductFamily
                .forEach(productFamily -> gscApproveQuoteContext.quoteProductSolutions
                        .addAll(productSolutionRepository.findByQuoteToLeProductFamily(productFamily)));

        return gscApproveQuoteContext;
    }

    /**
     * Method to get quotegsc.
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getQuoteGsc(GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteGsc = new ArrayList<>();
        gscApproveQuoteContext.quoteProductSolutions.forEach(productSolution -> {
            List<QuoteGsc> quoteGscList = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
                    GscConstants.STATUS_ACTIVE);
            gscApproveQuoteContext.quoteGsc.addAll(quoteGscList);
        });
        return gscApproveQuoteContext;
    }

    /**
     * Method to get quotegscdetails.
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getQuoteGscDetails(GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteGscDetail = gscApproveQuoteContext.quoteGsc.stream()
                .map(quoteGscDetailsRepository::findByQuoteGsc).flatMap(List::stream)
                .filter(quoteGscDetail -> !GSC_CFG_TYPE_REFERENCE.equalsIgnoreCase(quoteGscDetail.getType()))
                .collect(Collectors.toList());
        return gscApproveQuoteContext;
    }

    /**
     * Method to getquoteproductcomponent.
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getQuoteProductComponent(GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteProductComponents = new ArrayList<>();
        gscApproveQuoteContext.quoteGscDetail.forEach(quoteGscDetail -> {
            List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
                    .findByReferenceIdAndReferenceName(quoteGscDetail.getId(), QuoteConstants.GSC.toString());
            gscApproveQuoteContext.quoteProductComponents.addAll(quoteProductComponents);
        });
        return gscApproveQuoteContext;
    }

    /**
     * Method to getQuoteProductComponentAttributeValues
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getQuoteProductComponentAttributeValues(
            GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteProductComponentsAttributeValues = new ArrayList<>();
        gscApproveQuoteContext.quoteProductComponents.forEach(quoteProductComponent -> {
            List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
                    .findByQuoteProductComponent_Id(quoteProductComponent.getId());
            gscApproveQuoteContext.quoteProductComponentsAttributeValues.addAll(quoteProductComponentsAttributeValues);
        });
        return gscApproveQuoteContext;
    }

    /**
     * Method to get quotegscsla
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext getQuoteGscSla(GscApproveQuoteContext gscApproveQuoteContext) {
        gscApproveQuoteContext.quoteGscSla = new ArrayList<>();
        gscApproveQuoteContext.quoteGsc.forEach(quoteGsc -> {
            List<QuoteGscSla> quoteGscSla = quoteGscSlaRepository.findByQuoteGsc(quoteGsc);
            gscApproveQuoteContext.quoteGscSla.addAll(quoteGscSla);
        });
        return gscApproveQuoteContext;
    }

    /**
     * Method to createAndSaveOrderToLe
     *
     * @param gscApproveQuoteContext
     * @return
     */
    private GscApproveQuoteContext createAndSaveOrderToLe(GscApproveQuoteContext gscApproveQuoteContext) {
        Set<OrderToLe> orderToLes = new HashSet<>();
        Optional<OrderToLeProductFamily> gsipLeProductFamily = orderToLeRepository
                .findByOrder(gscApproveQuoteContext.order).stream().findFirst().map(orderToLe -> {
                    MstProductFamily gsipProductFamily = mstProductFamilyRepository.findByNameAndStatus(
                            GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
                    return orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(orderToLe,
                            gsipProductFamily);
                });
        if (!gsipLeProductFamily.isPresent()) {
            gscApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
                OrderToLe orderToLe = new OrderToLe();
                orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
                orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
                orderToLe.setFinalArc(quoteToLe.getFinalArc());
                orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
                orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
                orderToLe.setProposedArc(quoteToLe.getProposedArc());
                orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
                orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
                orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
                orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
                orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
                orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
                orderToLe.setOrder(gscApproveQuoteContext.order);
                orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
                orderToLe.setSourceSystem(quoteToLe.getSourceSystem());
                orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
                orderToLe.setOrderType(quoteToLe.getQuoteType());
                orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
                orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
                orderToLe.setClassification(quoteToLe.getClassification());
                orderToLe = orderToLeRepository.save(orderToLe);
                orderToLe.setOrdersLeAttributeValues(
                        createAndSaveOrderToLeAttribute(gscApproveQuoteContext, orderToLe, quoteToLe));
                orderToLe.setOrderToLeProductFamilies(
                        createAndSaveOrderProductFamily(gscApproveQuoteContext, orderToLe, quoteToLe));
                orderToLes.add(orderToLe);
                gscApproveQuoteContext.gscOrderDataBean.setOrderLeId(orderToLe.getId());
            });
            gscApproveQuoteContext.orderToLe = orderToLes;
            gscApproveQuoteContext.gscOrderDataBean.setLegalEntities(getLegalEntities(gscApproveQuoteContext.order));
        }
        return gscApproveQuoteContext;
    }

    /**
     * Method to createAndSaveOrderToLeAttribute
     *
     * @param gscApproveQuoteContext
     * @param orderToLe
     * @param quoteToLe
     * @return
     */
    private Set<OrdersLeAttributeValue> createAndSaveOrderToLeAttribute(GscApproveQuoteContext gscApproveQuoteContext,
                                                                        OrderToLe orderToLe, QuoteToLe quoteToLe) {
        gscApproveQuoteContext.orderToLeAttributeValue = new HashSet<>();
        if (Objects.nonNull(gscApproveQuoteContext.quoteLeAttributeValue)
                && !gscApproveQuoteContext.quoteLeAttributeValue.isEmpty()) {
            gscApproveQuoteContext.quoteLeAttributeValue.stream().filter(
                    quoteLeAttributeValue -> quoteLeAttributeValue.getQuoteToLe().getId().equals(quoteToLe.getId()))
                    .forEach(quoteLeAttributeValue -> {
                        OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
                        ordersLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
                        ordersLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
                        ordersLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
                        ordersLeAttributeValue.setOrderToLe(orderToLe);
                        ordersLeAttributeValue = ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
                        gscApproveQuoteContext.orderToLeAttributeValue.add(ordersLeAttributeValue);
                    });
        }

        return gscApproveQuoteContext.orderToLeAttributeValue;
    }

    /**
     * Method to createAndSaveOrderProductFamily
     *
     * @param gscApproveQuoteContext
     * @param orderToLe
     * @param quoteToLe
     * @return
     */
    private Set<OrderToLeProductFamily> createAndSaveOrderProductFamily(GscApproveQuoteContext gscApproveQuoteContext,
                                                                        OrderToLe orderToLe, QuoteToLe quoteToLe) {
        gscApproveQuoteContext.orderToLeProductFamily = new HashSet<>();
        if (!(gscApproveQuoteContext.quoteToLeProductFamily.isEmpty())) {
            gscApproveQuoteContext.quoteToLeProductFamily.stream().filter(
                    quoteToLeProductFamily -> quoteToLeProductFamily.getQuoteToLe().getId().equals(quoteToLe.getId()))
                    .forEach(quoteToLeProductFamily -> {
                        OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
                        orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
                        orderToLeProductFamily.setOrderToLe(orderToLe);
                        orderToLeProductFamily = orderToLeProductFamilyRepository.save(orderToLeProductFamily);
                        orderToLeProductFamily.setOrderProductSolutions(createAndSaveOrderProductSolution(
                                gscApproveQuoteContext, orderToLeProductFamily, orderToLe, quoteToLeProductFamily));
                        gscApproveQuoteContext.gscOrderDataBean
                                .setProductFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
                        gscApproveQuoteContext.orderToLeProductFamily.add(orderToLeProductFamily);
//						if (!userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
                        if (!partnerService.quoteCreatedByPartner(quoteToLe.getQuote().getId())) {
                            processEngagement(quoteToLe, quoteToLeProductFamily);
                        }
//						}
                    });

        }
        return gscApproveQuoteContext.orderToLeProductFamily;

    }

    /**
     * Method to createAndSaveOrderProductSolution
     *
     * @param gscApproveQuoteContext
     * @param orderToLeProductFamily
     * @param orderToLe
     * @param quoteToLeProductFamily
     * @return
     */
    private Set<OrderProductSolution> createAndSaveOrderProductSolution(GscApproveQuoteContext gscApproveQuoteContext,
                                                                        OrderToLeProductFamily orderToLeProductFamily, OrderToLe orderToLe,
                                                                        QuoteToLeProductFamily quoteToLeProductFamily) {
        List<GscOrderSolutionBean> gscOrderSolutionBeanList = new ArrayList<>();
        gscApproveQuoteContext.orderProductSolution = new HashSet<>();
        if (!(gscApproveQuoteContext.quoteProductSolutions.isEmpty())) {
            gscApproveQuoteContext.quoteProductSolutions.stream().filter(quoteProductSolution -> quoteProductSolution
                    .getQuoteToLeProductFamily().getId().equals(quoteToLeProductFamily.getId()))
                    .forEach(quoteProductSolution -> {
                        OrderProductSolution orderProductSolution = new OrderProductSolution();
                        if (quoteProductSolution.getMstProductOffering() != null) {
                            orderProductSolution.setMstProductOffering(quoteProductSolution.getMstProductOffering());
                        }
                        orderProductSolution.setOrderToLeProductFamily(orderToLeProductFamily);
                        orderProductSolution.setSolutionCode(quoteProductSolution.getSolutionCode());
                        orderProductSolution.setProductProfileData(quoteProductSolution.getProductProfileData());

                        orderProductSolution = orderProductSolutionRepository.save(orderProductSolution);

                        Set<OrderGsc> orderGscSet = createAndSaveOrderGsc(gscApproveQuoteContext, orderToLe,
                                orderProductSolution, quoteToLeProductFamily.getQuoteToLe(), quoteProductSolution);

                        Map<String, Object> orderProductProfileData = GscUtils.fromJson(
                                orderProductSolution.getProductProfileData(), new TypeReference<Map<String, Object>>() {
                                });

                        GscOrderSolutionBean gscOrderSolutionBean = createGscOrderSolutionBean(orderProductSolution,
                                orderProductProfileData, gscApproveQuoteContext, orderGscSet);
                        gscOrderSolutionBeanList.add(gscOrderSolutionBean);
                        gscApproveQuoteContext.orderProductSolution.add(orderProductSolution);
                    });

        }
        gscApproveQuoteContext.gscOrderDataBean.setSolutions(gscOrderSolutionBeanList);
        String accessType = gscApproveQuoteContext.gscOrderDataBean.getSolutions().stream().findFirst()
                .map(GscOrderSolutionBean::getAccessType).orElse(null);
        gscApproveQuoteContext.gscOrderDataBean.setAccessType(accessType);
        return gscApproveQuoteContext.orderProductSolution;

    }

    /**
     * get Legal Entities by order
     *
     * @param order
     * @return {@link List< GscOrderLeBean >}
     */
    private List<GscOrderLeBean> getLegalEntities(Order order) {
        return Optional.ofNullable(orderToLeRepository.findByOrder(order)).orElse(ImmutableList.of()).stream()
                .map(orderToLe -> populateProductFamily(GscOrderLeBean.fromOrderToLe(orderToLe), orderToLe))
                .collect(Collectors.toList());
    }

    private GscOrderLeBean populateProductFamily(GscOrderLeBean orderLeBean, OrderToLe orderToLe) {
        List<OrderToLeProductFamily> productFamilies = Optional.ofNullable(orderToLe.getOrderToLeProductFamilies())
                .map(orderToLeProductFamilies -> (List) new ArrayList<>(orderToLeProductFamilies))
                .orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe));
        if (!CollectionUtils.isEmpty(productFamilies)) {
            orderLeBean.setProductFamily(productFamilies.get(0).getMstProductFamily().getName());
        }
        return orderLeBean;
    }

    /**
     * Method to have an entry in Engagement table based on customer and
     * productFamily
     *
     * @param quoteToLe
     * @param quoteToLeProductFamily
     */
    private void processEngagement(QuoteToLe quoteToLe, QuoteToLeProductFamily quoteToLeProductFamily) {
        List<Engagement> engagements = engagementRepository
                .findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(quoteToLe.getQuote().getCustomer(),
                        quoteToLe.getErfCusCustomerLegalEntityId(), quoteToLeProductFamily.getMstProductFamily(),
                        CommonConstants.BACTIVE);
        if (engagements == null || engagements.isEmpty()) {
            Engagement engagement = new Engagement();
            engagement.setCustomer(quoteToLe.getQuote().getCustomer());
            engagement.setEngagementName(quoteToLeProductFamily.getMstProductFamily().getName() + CommonConstants.HYPHEN
                    + quoteToLe.getErfCusCustomerLegalEntityId());
            engagement.setErfCusCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
            engagement.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
            engagement.setStatus(CommonConstants.BACTIVE);
            engagement.setCreatedTime(new Date());
            engagementRepository.save(engagement);
        }
    }

    /**
     * Method to createAndSaveOrderGsc
     *
     * @param gscApproveQuoteContext
     * @param orderToLe
     * @param orderProductSolution
     * @param quoteToLe
     * @param quoteProductSolution
     * @return
     */
    private Set<OrderGsc> createAndSaveOrderGsc(GscApproveQuoteContext gscApproveQuoteContext, OrderToLe orderToLe,
                                                OrderProductSolution orderProductSolution, QuoteToLe quoteToLe, ProductSolution quoteProductSolution) {
        gscApproveQuoteContext.orderGsc = new HashSet<>();
        if (!gscApproveQuoteContext.quoteGsc.isEmpty()) {
            gscApproveQuoteContext.quoteGsc.stream()
                    .filter(quoteGsc -> quoteGsc.getQuoteToLe().getId().equals(quoteToLe.getId()))
                    .filter(quoteGsc -> quoteGsc.getProductSolution().getId().equals(quoteProductSolution.getId()))
                    .forEach(quoteGsc -> {
                        OrderGsc orderGsc = new OrderGsc();
                        orderGsc.setAccessType(quoteGsc.getAccessType());
                        orderGsc.setArc(quoteGsc.getArc());
                        orderGsc.setCreatedBy(quoteGsc.getCreatedBy());
                        orderGsc.setCreatedTime(quoteGsc.getCreatedTime());
                        orderGsc.setImageUrl(quoteGsc.getImageUrl());
                        orderGsc.setMrc(quoteGsc.getMrc());
                        orderGsc.setNrc(quoteGsc.getNrc());
                        orderGsc.setName(quoteGsc.getName());
                        orderGsc.setStatus(quoteGsc.getStatus());
                        orderGsc.setTcv(quoteGsc.getTcv());
                        orderGsc.setProductName(quoteGsc.getProductName());
                        orderGsc.setOrderToLe(orderToLe);
                        orderGsc.setOrderProductSolution(orderProductSolution);
                        orderGsc = orderGscRepository.save(orderGsc);
                        orderGsc.setOrderGscSlas(createAndSaveOrderGscSla(gscApproveQuoteContext, orderGsc, quoteGsc));
                        try {
                            orderGsc.setOrderGscDetails(
                                    createAndSaveOrderGscDetails(gscApproveQuoteContext, orderGsc, quoteGsc));
                        } catch (TclCommonException e) {
                            e.printStackTrace();
                        }
                        gscApproveQuoteContext.orderGsc.add(orderGsc);

                    });
        }
        return gscApproveQuoteContext.orderGsc;
    }

    /**
     * Method to createGscOrderSolutionBean
     *
     * @param orderProductSolution
     * @param orderProductProfileData
     * @param gscApproveQuoteContext
     * @param orderGscSet
     * @return
     */
    private GscOrderSolutionBean createGscOrderSolutionBean(OrderProductSolution orderProductSolution,
                                                            Map<String, Object> orderProductProfileData, GscApproveQuoteContext gscApproveQuoteContext,
                                                            Set<OrderGsc> orderGscSet) {
        GscOrderSolutionBean gscOrderSolutionBean = new GscOrderSolutionBean();
        gscOrderSolutionBean.setSolutionId(orderProductSolution.getId());
        gscOrderSolutionBean.setSolutionCode(orderProductSolution.getSolutionCode());
        gscOrderSolutionBean.setOfferingName(orderProductSolution.getMstProductOffering().getProductName());
        gscOrderSolutionBean.setGscOrders(orderGscSet.stream().map(orderGsc -> {
            GscOrderBean gscOrderBean = GscOrderBean.fromOrderGsc(orderGsc);
            attributeValuesPopulator.populateComponentAttributeValues(gscOrderBean,
                    () -> getOrderGscAttributes(orderGsc));
            return gscOrderBean;
        }).collect(Collectors.toList()));
        gscOrderSolutionBean.getGscOrders().stream().findFirst().ifPresent(ordergsc -> {
            gscOrderSolutionBean.setAccessType(ordergsc.getAccessType());
            gscOrderSolutionBean.setProductName(ordergsc.getProductName());
        });
        setOrderProductionBean(gscOrderSolutionBean.getGscOrders(),
                gscApproveQuoteContext.gscOrderProductComponentBean);
        return gscOrderSolutionBean;
    }

    /**
     * Method to createAndSaveOrderGscSla
     *
     * @param gscApproveQuoteContext
     * @param orderGsc
     * @param quoteGsc
     * @return
     */
    private Set<OrderGscSla> createAndSaveOrderGscSla(GscApproveQuoteContext gscApproveQuoteContext, OrderGsc orderGsc,
                                                      QuoteGsc quoteGsc) {
        gscApproveQuoteContext.orderGscSla = new HashSet<>();
        if (!gscApproveQuoteContext.quoteGscSla.isEmpty()) {
            gscApproveQuoteContext.quoteGscSla.stream()
                    .filter(quoteGscSla -> quoteGscSla.getQuoteGsc().getId().equals(quoteGsc.getId()))
                    .forEach(quoteGscSla -> {
                        OrderGscSla orderGscSla = new OrderGscSla();
                        orderGscSla.setAttributeName(quoteGscSla.getAttributeName());
                        orderGscSla.setAttributeValue(quoteGscSla.getAttributeValue());
                        orderGscSla.setSlaMaster(quoteGscSla.getSlaMaster());
                        orderGscSla.setOrderGsc(orderGsc);
                        orderGscSla = orderGscSlaRepository.save(orderGscSla);
                        gscApproveQuoteContext.orderGscSla.add(orderGscSla);

                    });
        }
        return gscApproveQuoteContext.orderGscSla;
    }

    /**
     * Method to createAndSaveOrderGscDetails
     *
     * @param gscApproveQuoteContext
     * @param orderGsc
     * @param quoteGsc
     * @return
     */
    private Set<OrderGscDetail> createAndSaveOrderGscDetails(GscApproveQuoteContext gscApproveQuoteContext,
                                                             OrderGsc orderGsc, QuoteGsc quoteGsc) throws TclCommonException {
        gscApproveQuoteContext.orderGscDetail = new HashSet<>();
        MstOrderSiteStage mstOrderSiteStage = getMstOrderSiteStage(GscConstants.INTIAL_ORDER_CONFIGURATION_STAGE);
        MstOrderSiteStatus mstOrderSiteStatus = getMstOrderSiteStatus(GscConstants.INTIAL_ORDER_CONFIGURATION_STATUS);
        if (!gscApproveQuoteContext.quoteGscDetail.isEmpty()) {
            gscApproveQuoteContext.quoteGscDetail.stream()
                    .filter(quoteGscDetail -> quoteGscDetail.getQuoteGsc().getId().equals(quoteGsc.getId()))
                    .forEach(quoteGscDetail -> {
                        OrderGscDetail orderGscDetail = new OrderGscDetail();
                        orderGscDetail.setArc(quoteGscDetail.getArc());
                        orderGscDetail.setCreatedBy(quoteGscDetail.getCreatedBy());
                        orderGscDetail.setCreatedTime(quoteGscDetail.getCreatedTime());
                        orderGscDetail.setDestType(quoteGscDetail.getDestType());
                        orderGscDetail.setSrcType(quoteGscDetail.getSrcType());
                        orderGscDetail.setDest(quoteGscDetail.getDest());
                        orderGscDetail.setSrc(quoteGscDetail.getSrc());
                        orderGscDetail.setMrc(quoteGscDetail.getMrc());
                        orderGscDetail.setNrc(quoteGscDetail.getNrc());
                        orderGscDetail.setOrderGsc(orderGsc);
                        if (Objects.nonNull(mstOrderSiteStatus)) {
                            orderGscDetail.setMstOrderSiteStatus(mstOrderSiteStatus);
                        }
                        if (Objects.nonNull(mstOrderSiteStage)) {
                            orderGscDetail.setMstOrderSiteStage(mstOrderSiteStage);
                        }
                        orderGscDetail = orderGscDetailRepository.save(orderGscDetail);
                        createOrderGscTfns(quoteGscDetail, orderGscDetail);
                        createConfigurationProductComponent(orderGscDetail, gscApproveQuoteContext.order);
                        gscApproveQuoteContext.orderGscDetail.add(orderGscDetail);
                        gscApproveQuoteContext.gscOrderProductComponentBean
                                .addAll(createAndSaveOrderProductComponent(gscApproveQuoteContext, orderGscDetail,
                                        quoteGscDetail).stream()
                                        .map(GscOrderProductComponentBean::fromOrderProductComponent)
                                        .collect(Collectors.toList()));
                    });
        }
        return gscApproveQuoteContext.orderGscDetail;
    }

    /**
     * Method to createOrderGscTfns
     *
     * @param quoteGscDetail
     * @param orderGscDetail
     */
    private void createOrderGscTfns(QuoteGscDetail quoteGscDetail, OrderGscDetail orderGscDetail) {
        List<QuoteGscTfn> quoteGscTfns = quoteGscTfnRepository.findByQuoteGscDetail(quoteGscDetail);
        if (!CollectionUtils.isEmpty(quoteGscTfns)) {
            List<OrderGscTfn> orderGscTfns = quoteGscTfns.stream().map(quoteGscTfn -> {
                OrderGscTfn orderGscTfn = new OrderGscTfn();
                orderGscTfn.setOrderGscDetail(orderGscDetail);
                orderGscTfn.setPortedFrom(quoteGscTfn.getPortedFrom());
                orderGscTfn.setStatus(quoteGscTfn.getStatus());
                orderGscTfn.setIsPorted(quoteGscTfn.getIsPorted());
                orderGscTfn.setTfnNumber(quoteGscTfn.getTfnNumber());
                orderGscTfn.setCountryCode(quoteGscTfn.getCountryCode());
                orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
                orderGscTfn.setCreatedBy(Utils.getSource());
                return orderGscTfn;
            }).collect(Collectors.toList());
            orderGscTfnRepository.saveAll(orderGscTfns);
        }
    }

    /**
     * Method to createConfigurationProductComponent
     *
     * @param orderGscDetail
     * @param order
     */
    private void createConfigurationProductComponent(OrderGscDetail orderGscDetail, Order order) {
        List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                .findByReferenceIdAndType(orderGscDetail.getId(), GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
        if (orderProductComponents.isEmpty()) {
            OrderProductComponent component = new OrderProductComponent();
            component.setType(GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
            component.setReferenceId(orderGscDetail.getId());
            List<MstProductComponent> mstProductComponents = mstProductComponentRepository
                    .findByNameAndStatus(GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
            component.setMstProductComponent(mstProductComponents.get(0));
            MstProductFamily productFamily = mstProductFamilyRepository
                    .findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
            component.setMstProductFamily(productFamily);
            component = orderProductComponentRepository.save(component);
            createDefaultConfigurationAttributes(component, orderGscDetail);
        }
    }

    /**
     * Method to getOrderGscAttributes
     *
     * @param orderGsc
     * @return
     */
    private Map<String, String> getOrderGscAttributes(OrderGsc orderGsc) {
        OrderProductComponent gscOrderProductComponent = getGscOrderProductComponent(orderGsc);
        return Optional.ofNullable(gscOrderProductComponent.getOrderProductComponentsAttributeValues())
                .orElse(ImmutableSet.of()).stream()
                .collect(Collectors.toMap(value -> value.getProductAttributeMaster().getName(),
                        OrderProductComponentsAttributeValue::getAttributeValues));
    }

    /**
     * Method to get getGscOrderProductComponent
     *
     * @param orderGsc
     * @return
     */
    private OrderProductComponent getGscOrderProductComponent(OrderGsc orderGsc) {
        MstProductComponent gscOrderMasterComponent = mstProductComponentRepository
                .findByName(PRODUCT_COMPONENT_TYPE_ORDER_GSC);
        if (Objects.isNull(gscOrderMasterComponent)) {
            throw new RuntimeException(
                    String.format("Master product component of type: %s not found", PRODUCT_COMPONENT_TYPE_ORDER_GSC));
        }
        return orderProductComponentRepository
                .findByReferenceIdAndMstProductComponent(orderGsc.getId(), gscOrderMasterComponent).stream().findFirst()
                .orElseGet(() -> {
                    OrderProductComponent orderProductComponent = new OrderProductComponent();
                    orderProductComponent.setMstProductComponent(gscOrderMasterComponent);
                    orderProductComponent.setReferenceId(orderGsc.getId());
                    orderProductComponent.setType(PRODUCT_COMPONENT_TYPE_ORDER_GSC);
                    MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(
                            GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), GscConstants.STATUS_ACTIVE);
                    orderProductComponent.setMstProductFamily(mstProductFamily);
                    return orderProductComponentRepository.save(orderProductComponent);
                });
    }

    /**
     * Setting OrderProduction To Configuration Bean
     *
     * @param gscOrders
     * @param gscOrderProductComponentBean
     */
    private void setOrderProductionBean(List<GscOrderBean> gscOrders,
                                        List<GscOrderProductComponentBean> gscOrderProductComponentBean) {
        gscOrders.stream().forEach(gscOrder -> gscOrder.getConfigurations().stream()
                .forEach(gscConfiguration -> gscConfiguration.setProductComponents(gscOrderProductComponentBean.stream()
                        .filter(gscProductBean -> gscProductBean.getReferenceId().equals(gscConfiguration.getId()))
                        .collect(Collectors.toList()))));

    }

    /**
     * Method to createAndSaveOrderProductComponent
     *
     * @param gscApproveQuoteContext
     * @param orderGscDetail
     * @param quoteGscDetail
     * @return
     */
    private Set<OrderProductComponent> createAndSaveOrderProductComponent(GscApproveQuoteContext gscApproveQuoteContext,
                                                                          OrderGscDetail orderGscDetail, QuoteGscDetail quoteGscDetail) {
        gscApproveQuoteContext.orderProductComponents = new HashSet<>();
        if (!gscApproveQuoteContext.quoteProductComponents.isEmpty()) {
            gscApproveQuoteContext.quoteProductComponents.stream().filter(
                    quoteProductComponents -> quoteProductComponents.getReferenceId().equals(quoteGscDetail.getId()))
                    .forEach(quoteProductComponent -> {
                        OrderProductComponent orderProductComponent = new OrderProductComponent();
                        orderProductComponent.setReferenceId(orderGscDetail.getId());
                        orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
                        orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
                        orderProductComponent.setType(quoteProductComponent.getType());
                        orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
                        orderProductComponent = orderProductComponentRepository.save(orderProductComponent);
                        orderProductComponent.setOrderProductComponentsAttributeValues(
                                createAndSaveOrderProductAttributeValues(gscApproveQuoteContext, orderProductComponent,
                                        quoteProductComponent));
                        gscApproveQuoteContext.orderProductComponents.add(orderProductComponent);
                    });
        }
        return gscApproveQuoteContext.orderProductComponents;
    }

    /**
     * Method to create default configuration attribtues
     *
     * @param productComponent
     * @param orderGscDetail
     */
    private void createDefaultConfigurationAttributes(OrderProductComponent productComponent,
                                                      OrderGscDetail orderGscDetail) {
        ProductAttributeMaster requestorDateAttributeMaster = getMasterAttribute(
                GscAttributeConstants.ATTR_REQUESTOR_DATE_FOR_SERVICE);
        OrderProductComponentsAttributeValue requestorDateAttribute = new OrderProductComponentsAttributeValue();
        requestorDateAttribute.setOrderProductComponent(productComponent);
        requestorDateAttribute.setProductAttributeMaster(requestorDateAttributeMaster);
        String requestorDateForServiceValue = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        requestorDateAttribute.setAttributeValues(requestorDateForServiceValue);
        requestorDateAttribute.setDisplayValue(requestorDateForServiceValue);
        orderProductComponentsAttributeValueRepository.save(requestorDateAttribute);

        ProductAttributeMaster expectedDeliveryDateAttributeMaster = getMasterAttribute(
                GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE);
        OrderProductComponentsAttributeValue expectedDeliveryDateAttribute = new OrderProductComponentsAttributeValue();
        expectedDeliveryDateAttribute.setOrderProductComponent(productComponent);
        expectedDeliveryDateAttribute.setProductAttributeMaster(expectedDeliveryDateAttributeMaster);
        String expectedDeliveryDateForValue = getGscExpectedDateForDelivery(orderGscDetail);
        if (!expectedDeliveryDateForValue.equalsIgnoreCase(GscConstants.BEST_EFFORT))
            expectedDeliveryDateForValue = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()
                    .plusDays(Integer.valueOf(Optional.ofNullable(expectedDeliveryDateForValue).orElse("0"))));
        expectedDeliveryDateAttribute.setAttributeValues(expectedDeliveryDateForValue);
        expectedDeliveryDateAttribute.setDisplayValue(expectedDeliveryDateForValue);
        orderProductComponentsAttributeValueRepository.save(expectedDeliveryDateAttribute);
    }

    /**
     * createAndSaveOrderProductAttributeValues
     *
     * @param gscApproveQuoteContext
     * @param orderProductComponent
     * @param quoteProductComponent
     * @param quoteProductComponent
     * @return
     */
    private Set<OrderProductComponentsAttributeValue> createAndSaveOrderProductAttributeValues(
            GscApproveQuoteContext gscApproveQuoteContext, OrderProductComponent orderProductComponent,
            QuoteProductComponent quoteProductComponent) {
        gscApproveQuoteContext.orderProductComponentsAttributeValues = new HashSet<>();
        if (!gscApproveQuoteContext.quoteProductComponentsAttributeValues.isEmpty()) {
            gscApproveQuoteContext.quoteProductComponentsAttributeValues.stream()
                    .filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue
                            .getQuoteProductComponent().getId().equals(quoteProductComponent.getId()))
                    .forEach(quoteProductComponentsAttributeValue -> {
                        OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
                        orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
                        orderProductComponentsAttributeValue
                                .setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
                        orderProductComponentsAttributeValue
                                .setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
                        orderProductComponentsAttributeValue.setProductAttributeMaster(
                                quoteProductComponentsAttributeValue.getProductAttributeMaster());
                        orderProductComponentsAttributeValue = orderProductComponentsAttributeValueRepository
                                .save(orderProductComponentsAttributeValue);
                        gscApproveQuoteContext.orderProductComponentsAttributeValues
                                .add(orderProductComponentsAttributeValue);
                    });
        }

        return gscApproveQuoteContext.orderProductComponentsAttributeValues;
    }

    private ProductAttributeMaster getMasterAttribute(String attributeName) {
        return productAttributeMasterRepository.findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream()
                .findFirst().orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY,
                        String.format("Attribute with name: %s not found", attributeName)));
    }

    /**
     * fetch rfs via MQ call
     *
     * @param orderGscDetail
     * @return
     */
    private String getGscExpectedDateForDelivery(OrderGscDetail orderGscDetail) {
        String expectedDeliveryDate = null;
        String country = null;
        if (orderGscDetail.getOrderGsc().getProductName().equals(GscConstants.GLOBAL_OUTBOUND)) {
            country = orderGscDetail.getDest();
        } else {
            country = orderGscDetail.getSrc();
        }
        ExpectedDeliveryDateBean expectedDeliveryDateBean = new ExpectedDeliveryDateBean(
                orderGscDetail.getOrderGsc().getProductName(), orderGscDetail.getOrderGsc().getAccessType(), country);
        try {
            LOGGER.info("MDC Filter token value in before Queue call getGscRequestorDateForServiceDays {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            expectedDeliveryDate = mqUtils
                    .sendAndReceive(expectedDeliveryDateQueue, Utils.convertObjectToJson(expectedDeliveryDateBean))
                    .toString();
        } catch (Exception e) {
            LOGGER.warn("Error in estimaiting the Delivery Date ", e);
        }
        return expectedDeliveryDate;
    }

    /**
     * Method to updateOpportunityInSfdc
     *
     * @param context
     * @return
     */
    private GscApproveQuoteContext updateOpportunityInSfdc(GscApproveQuoteContext context) {
        quoteToLeRepository.findByQuote(context.quote).forEach(quoteToLe -> {
            callUpdateOpportunityInSfdc(quoteToLe, context);
        });
        return context;
    }

    /**
     * Method to call UpdateOpportunity In Sfdc
     *
     * @param context
     * @return
     */
    private void callUpdateOpportunityInSfdc(QuoteToLe quoteLe, GscApproveQuoteContext context) {
        try {
            omsSfdcService.processUpdateOpportunity(context.cofSignedDate, quoteLe.getTpsSfdcOptyId(),
                    SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);
        } catch (Exception e) {
            throw new TCLException("", e.getMessage());
        }
    }

    /**
     * Method to processCofPdf.
     *
     * @param context
     * @return
     */
    private GscApproveQuoteContext processCofPdf(GscApproveQuoteContext context) {
        gscQuotePdfService.processCofPdf(context.quote.getId(), context.response, false, true);
        return context;
    }

    /**
     * Method to check MSA and SS documents
     *
     * @param context
     * @return {@link GscApproveQuoteContext}
     */
    public GscApproveQuoteContext checkMSAandSSDocuments(GscApproveQuoteContext context) {
        context.quoteToLe.stream().forEach(this::uploadSSIfNotPresent);
        return context;
    }

    /**
     * Method to upload Service schedule document If Not Present
     *
     * @param quoteToLe
     * @return {@link QuoteToLe}
     */
    private QuoteToLe uploadSSIfNotPresent(QuoteToLe quoteToLe) {
        Optional<List<MstOmsAttribute>> mstOmsAttributes = Optional.ofNullable(mstOmsAttributeRepository
                .findByNameAndIsActive(LeAttributesConstants.SERVICE_SCHEDULE, CommonConstants.BACTIVE));
        mstOmsAttributes.ifPresent(attribute -> {
            attribute.forEach(mstOmsAttribute -> {
                List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
                        .findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
                if (quoteLeAttributeValues.isEmpty()) {
                    ServiceScheduleBean serviceScheduleBean = constructServiceScheduleBean(
                            quoteToLe.getErfCusCustomerLegalEntityId());
                    LOGGER.info("MDC Filter token value in before Queue call uploadSSIfNotPresent {} :",
                            MDC.get(CommonConstants.MDC_TOKEN_KEY));
                    try {
                        mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
                    } catch (TclCommonException | IllegalArgumentException e) {
                        LOGGER.info("Exception in uploading SS document: {}", e.getMessage());
                    }
                }
            });
        });
        return quoteToLe;
    }

    /**
     * construct Service Schedule Bean
     *
     * @param customerLeId
     * @return {@link ServiceScheduleBean}
     */
    public ServiceScheduleBean constructServiceScheduleBean(Integer customerLeId) {
        ServiceScheduleBean serviceScheduleBean = new ServiceScheduleBean();
        serviceScheduleBean.setCustomerLeId(customerLeId);
        serviceScheduleBean.setDisplayName(LeAttributesConstants.SERVICE_SCHEDULE);
        serviceScheduleBean.setIsSSUploaded(true);
        serviceScheduleBean.setName(LeAttributesConstants.SERVICE_SCHEDULE);
        serviceScheduleBean.setProductName(SFDCConstants.GSIP);
        return serviceScheduleBean;
    }

    /**
     * Create Product Attribute values for each attributes
     *
     * @param orderProductComponent
     * @param gscOrderProductComponentBean
     * @param order
     * @return GscOrderProductComponentBean
     */
    public GscOrderProductComponentBean processProductComponent(OrderProductComponent orderProductComponent,
                                                                GscOrderProductComponentBean gscOrderProductComponentBean, Order order) {
        List<GscOrderProductComponentsAttributeValueBean> normalAttributes = gscOrderProductComponentBean
                .getAttributes();
        Stream<OrderProductComponentsAttributeValue> simpleAttributes = normalAttributes.stream()
                .filter(attributeValue -> attributeValue instanceof GscOrderProductComponentsAttributeSimpleValueBean)
                .map(attribute -> (GscOrderProductComponentsAttributeSimpleValueBean) attribute)
                .map(attribute -> saveOrderComponentAttributeValue(orderProductComponent, attribute));
        Stream<OrderProductComponentsAttributeValue> arrayAttributes = normalAttributes.stream()
                .filter(attributeValue -> attributeValue instanceof GscOrderProductComponentsAttributeArrayValueBean)
                .map(attribute -> (GscOrderProductComponentsAttributeArrayValueBean) attribute)
                .map(attribute -> handleArrayAttribute(orderProductComponent, attribute, order)).flatMap(List::stream);
        List<GscOrderProductComponentsAttributeSimpleValueBean> savedAttributes = Stream
                .concat(simpleAttributes, arrayAttributes)
                .map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute).collect(Collectors.toList());
        List<GscOrderProductComponentsAttributeValueBean> resultAttributes = groupAndConvertToValueBeans(
                savedAttributes);
        // handle tfn attribute values
//        gscOrderProductComponentBean
//                .getAttributes()
//                .stream()
//                .filter(bean -> GscConstants.TFN_ATTRIBUTE_NAME.equalsIgnoreCase(bean.getAttributeName()))
//                .findFirst()
//                .ifPresent(valueBean -> resultAttributes.add(
//                        handleTfnAttribute(orderProductComponent.getReferenceId(), valueBean)));
        gscOrderProductComponentBean.setAttributes(resultAttributes);
        return gscOrderProductComponentBean;
    }

    /**
     *
     * Method to get the user details
     *
     * @return User
     */
    private User getUserId(String username) {
        return userRepository.findByUsernameAndStatus(username, 1);
    }

    /**
     * Method to save order component Attribute value
     *
     * @param orderProductComponent
     * @param bean
     * @return
     */
    public OrderProductComponentsAttributeValue saveOrderComponentAttributeValue(
            OrderProductComponent orderProductComponent, GscOrderProductComponentsAttributeSimpleValueBean bean) {
        Objects.requireNonNull(orderProductComponent);
        Objects.requireNonNull(bean);
        List<ProductAttributeMaster> productAttributeMasterList = productAttributeMasterRepository
                .findByNameAndStatus(bean.getAttributeName(), GscConstants.STATUS_ACTIVE);

        ProductAttributeMaster productAttributeMaster = null;

        if (productAttributeMasterList != null && !productAttributeMasterList.isEmpty()) {
            productAttributeMaster = productAttributeMasterList.get(0);
        }

        if (Objects.isNull(productAttributeMasterList) || productAttributeMasterList.isEmpty()) {
            LOGGER.info("Creating new attribute...");
            productAttributeMaster = new ProductAttributeMaster();
            User user = getUserId(Utils.getSource());
            productAttributeMaster.setCreatedBy(user.getUsername());
            productAttributeMaster.setCreatedTime(new Date());
            productAttributeMaster.setDescription(bean.getDescription());
            productAttributeMaster.setName(bean.getAttributeName());
            productAttributeMaster.setStatus((byte) 1);
            productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
        }
        OrderProductComponentsAttributeValue attributeValue = orderProductComponentsAttributeValueRepository
                .findByOrderProductComponentAndProductAttributeMaster(orderProductComponent, productAttributeMaster)
                .stream().findFirst().orElse(new OrderProductComponentsAttributeValue());
        attributeValue.setAttributeValues(bean.getAttributeValue());
        attributeValue.setDisplayValue(bean.getAttributeValue());
        attributeValue.setOrderProductComponent(orderProductComponent);
        attributeValue.setProductAttributeMaster(productAttributeMaster);

        return orderProductComponentsAttributeValueRepository.save(attributeValue);
    }

    /**
     * Method to handle array attributes
     *
     * @param productComponent
     * @param arrayAttribute
     * @param quote
     * @return
     */
    private List<OrderProductComponentsAttributeValue> handleArrayAttribute(OrderProductComponent productComponent,
                                                                            GscOrderProductComponentsAttributeArrayValueBean arrayAttribute, Order quote) {

        List<ProductAttributeMaster> productAttributeMasterList = productAttributeMasterRepository
                .findByNameAndStatus(arrayAttribute.getAttributeName(), GscConstants.STATUS_ACTIVE);

        ProductAttributeMaster productAttributeMaster = null;

        if (productAttributeMasterList != null && !productAttributeMasterList.isEmpty()) {
            productAttributeMaster = productAttributeMasterList.get(0);
        }

        if (Objects.isNull(productAttributeMasterList) || productAttributeMasterList.isEmpty()) {
            productAttributeMaster = new ProductAttributeMaster();
            User user = getUserId(Utils.getSource());
            productAttributeMaster.setCreatedBy(user.getUsername());
            productAttributeMaster.setCreatedTime(new Date());
            productAttributeMaster.setDescription(arrayAttribute.getDescription());
            productAttributeMaster.setName(arrayAttribute.getAttributeName());
            productAttributeMaster.setStatus((byte) 1);
            productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
        }

        List<OrderProductComponentsAttributeValue> values = orderProductComponentsAttributeValueRepository
                .findByOrderProductComponentAndProductAttributeMaster(productComponent, productAttributeMaster);
        // delete all existing attributes for that name
        orderProductComponentsAttributeValueRepository.deleteAll(values);

        ProductAttributeMaster finalProductAttributeMaster = productAttributeMaster;
        List<OrderProductComponentsAttributeValue> attributeValues = arrayAttribute.getAttributeValue().stream()
                .map(attributeValue -> createOrderProductComponentsAttributeValues(arrayAttribute, quote,
                        productComponent, finalProductAttributeMaster, attributeValue))
                .collect(Collectors.toList());
        // save all new attributes
        return orderProductComponentsAttributeValueRepository.saveAll(attributeValues);
    }

    /**
     * create Order Product Components Attribute Values
     *
     * @param attributeValueBean
     * @param order
     * @param productComponent
     * @param attributeMaster
     * @return OrderProductComponentsAttributeValue
     */
    private OrderProductComponentsAttributeValue createOrderProductComponentsAttributeValues(
            GscOrderProductComponentsAttributeValueBean attributeValueBean, Order order,
            OrderProductComponent productComponent, ProductAttributeMaster attributeMaster, String attributeValue) {
        OrderProductComponentsAttributeValue value = new OrderProductComponentsAttributeValue();
        value.setAttributeValues(attributeValue);
        value.setId(attributeValueBean.getAttributeId());
        value.setDisplayValue(attributeValueBean.getDisplayValue());
        value.setOrderProductComponent(productComponent);
        value.setProductAttributeMaster(attributeMaster);
        return value;
    }

    /**
     * group And Convert To Value Beans
     *
     * @param simpleValueBeans
     * @return List of GscOrderProductComponentsAttributeValueBean>
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

	 /**
	 * Update Order le in oms attachments for Ratefile generation
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @return {@link Boolean}
	 * @throws TclCommonException
	 */
    
	@Transactional
	public Boolean updateOrderToLeInAttachment(Integer orderId, Integer orderToLeId) throws TclCommonException {
		Boolean isUpdated = false;
		if (Objects.isNull(orderId) || Objects.isNull(orderToLeId)) {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY,R_CODE_ERROR);
		}
		
		Optional<Order> order = orderRepository.findById(orderId);
		if(!order.isPresent()) {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY,R_CODE_ERROR);
		}
		
		Order odr = order.get();
		List<QuoteToLe> quoteToLes = new ArrayList<>(odr.getQuote().getQuoteToLes()); 
		QuoteToLe quoteToLe = null;
		
		if(!CollectionUtils.isEmpty(quoteToLes)) {
			if (quoteToLes.size() > 1) {
				quoteToLe = quoteToLes.get(1);
			} else {
				quoteToLe = quoteToLes.get(0);
			}
			List<String> referenceNames = Arrays.asList(GSIP_SURCHARGE_EXCEL, GSIP_DOMESTIC_OUTBOUND_EXCEL, GSIP_OUTBOUND_EXCEL);
			List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByQuoteToLeAndReferenceNameIn(quoteToLe, referenceNames);
			
			if(!CollectionUtils.isEmpty(omsAttachments)) {
				omsAttachments.stream().forEach(omsAttachment -> {
					Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
					omsAttachment.setOrderToLe(orderToLe.get());
					omsAttachmentRepository.save(omsAttachment);
				});
				isUpdated = true;
			}
		}
		return isUpdated;
	}
}
