package com.tcl.dias.oms.gsc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.COMMA;
import static com.tcl.dias.common.constants.CommonConstants.INDIA_INTERNATIONAL_SITES;
import static com.tcl.dias.common.constants.CommonConstants.INTERNATIONAL_SITES;
import static com.tcl.dias.common.constants.CommonConstants.QUOTE_SITE_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.ACCESS_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_COMPONENT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_CURRENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_INCREMENT;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_METHOD;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.BILLING_TYPE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.DESTINATION_COUNTRY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_CONFIGURATION_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_DETAILS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_DETAIL_MRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_DETAIL_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_MRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_NRC;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.GSC_QUOTE_TCV;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.HAND_OFF;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_ADDRESS;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_CITY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_CONTACT_EMAIL;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_CONTACT_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_COUNTRY;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_POSTAL_CODE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_STATE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PARTNER_END_CUSTOMER_WEBSITE;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.PRODUCT_NAME;
import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SOURCE_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscConstants.CHANNEL_NRC;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.MPLS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.PUBLIC_IP;
import static com.tcl.dias.oms.gsc.util.GscConstants.TERM_NAME;
import static com.tcl.dias.oms.gsc.util.GscConstants.YES;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.customexception.TCLException;
import com.tcl.dias.common.gsc.beans.GscWholesaleInterconnectBean;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.partner.beans.PartnerEndCustomerMappingBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.gsc.beans.GscMultiMacdServiceBean;
import com.tcl.dias.common.gsc.beans.GscSlaBean;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.CustomFeasibilityRequest;
import com.tcl.dias.oms.beans.ExcelBean;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.PortBandwithConstants;
import com.tcl.dias.oms.gsc.beans.GscCityNumber;
import com.tcl.dias.oms.gsc.beans.GscOrderBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gvpn.pricing.bean.Feasible;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlFeasible;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlNotFeasible;
import com.tcl.dias.oms.gvpn.pricing.bean.NotFeasible;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.vavr.control.Try;

/**
 * Services to handle export to LR  functionality
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscExportLRService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscExportLRService.class);
    public static final String INTL = "INTL";

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;
    @Autowired
    OrderProductSolutionRepository orderProductSolutionRepository;
    @Autowired
    OrderGscRepository orderGscRepository;
    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;
    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;
    @Autowired
    GscOrderDetailService gscOrderDetailService;
    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;
    @Autowired
    MstProductComponentRepository mstProductComponentRepository;
    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;
    @Autowired
    OrderIllSitesRepository orderIllSitesRepository;
    @Autowired
    OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;
    @Autowired
    OrderPriceRepository orderPriceRepository;
    @Autowired
    private OpportunityRepository opportunityRepository;
    @Autowired
    private PartnerTempCustomerDetailsRepository partnerTempCustomerDetailsRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${rabbitmq.product.city.location.queue}")
    private String cityDetailsQueue;

    @Value("${rabbitmq.product.acans.city.location.queue}")
    private String acansCityDetailsQueue;

    @Autowired
    GscSlaService gscSlaService;

    @Value("${rabbitmq.location.address.request}")
    String apiAddressQueue;

    @Value("${rabbitmq.location.itcontact.request}")
    String locationItContactQueue;

    @Value("${rabbitmq.poplocation.detail}")
    String popQueue;
    
    @Value("${rabbitmq.mstaddress.detail}")
    String addressDetail;

    private String portingNumbers = "";

    @Autowired
    GscOrderService gscOrderService;

    @Value("${rabbitmq.site.gst.queue}")
    String siteGstQueue;

    @Value("${rabbitmq.partner.endCustomer.mapping}")
    String partnerEndCustomerMappingQueue;

    @Autowired
    PartnerService partnerService;

    @Autowired
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

    @Autowired
    AdditionalServiceParamRepository additionalServiceParamRepository;

    @Value("${rabbitmq.location.detail}")
    String locationQueue;


    /**
     * returnExcel - method holding functionality to export order details as LR
     * Excel
     *
     * @param orderId
     * @param response
     * @throws IOException
     * @throws TclCommonException
     */
    public void returnExcel(Integer orderId, HttpServletResponse response) throws IOException, TclCommonException {

        Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
        Objects.requireNonNull(response, HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
        List<ExcelBean> listBook = getExcelList(orderId);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        sheet.setColumnWidth(50000, 50000);
        int rowCount = 0;

        for (ExcelBean aBook : listBook) {
            Row row = sheet.createRow(rowCount);
            writeBook(aBook, row);
            rowCount++;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        workbook.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();
        String fileName = "OrderDetails-" + orderId + ".xls";
        response.reset();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        workbook.close();
        try {
            FileCopyUtils.copy(outArray, response.getOutputStream());
        } catch (Exception e) {

        }

        outByteStream.flush();
        outByteStream.close();
    }

    /**
     * getExcelList - converts order into list of excel bean
     *
     * @param orderId
     * @return
     * @throws TclCommonException
     */
    public List<ExcelBean> getExcelList(Integer orderId) throws TclCommonException {

        Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
        List<ExcelBean> listBook = new ArrayList<>();
        Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
        Objects.requireNonNull(order, ORDER_NULL_MESSAGE);
        MstProductFamily gscProductFamily = mstProductFamilyRepository
                .findByNameAndStatus(OrderDetailsExcelDownloadConstants.GSIP, (byte) 1);
        OrderToLe gscOrderToLe = getOrderToLeBasedOnProductFamily(order, gscProductFamily);
        Map<String, String> attributeValues = getAttributeValues(gscOrderToLe);

        //Show end customer details only for Sell through
        if(PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(gscOrderToLe.getClassification()))
            attributeValues = addEndCustomerAttributeValues(order, attributeValues);

        ExcelBean info = new ExcelBean(OrderDetailsExcelDownloadConstants.LR_SECTION,
                OrderDetailsExcelDownloadConstants.ATTRIBUTE_REQUIRED, OrderDetailsExcelDownloadConstants.REMARKS);
        info.setOrder(0);
        info.setGscQuoteId(0);
        listBook.add(info);
        boolean isIntl = false;
        boolean isGscMultiMacd = false;
        String multiMacdTrunkDataType = null;

        if (attributeValues.containsKey(QUOTE_SITE_TYPE)
                && (attributeValues.get(QUOTE_SITE_TYPE).equalsIgnoreCase(INTERNATIONAL_SITES) || attributeValues.get(QUOTE_SITE_TYPE).equalsIgnoreCase(INDIA_INTERNATIONAL_SITES)))
        {
            isIntl = true;
        }

        if(attributeValues.containsKey(LeAttributesConstants.IS_GSC_MULTI_MACD) && attributeValues.get(LeAttributesConstants.IS_GSC_MULTI_MACD).equalsIgnoreCase("Yes")){
            isGscMultiMacd = true;
            if(attributeValues.containsKey(LeAttributesConstants.MULTI_MACD_TRUNK_DATA_TYPE)){
                multiMacdTrunkDataType = attributeValues.get(LeAttributesConstants.MULTI_MACD_TRUNK_DATA_TYPE);
            }
        }

        createOrderDetails(listBook, attributeValues, gscOrderToLe, isIntl, isGscMultiMacd);
        List<OrderGsc> orderGscList = getGscQuotes(gscOrderToLe);

        boolean finalIsGscMultiMacd = isGscMultiMacd;
        final String[] accessType = new String[1];
        orderGscList.stream().forEach(orderGsc -> {
            createQuoteGscDetails(listBook, orderGsc);

            List<OrderGscDetail> orderGscDetails = orderGscDetailRepository.findByorderGsc(orderGsc);
            orderGscDetails.stream().forEach(orderGscDetail -> {

                createQuoteGscConfigurationDetails(listBook, orderGscDetail);
                MstProductComponent component = mstProductComponentRepository.findByName(orderGsc.getProductName());
                MstProductComponent gscConfigComponent = mstProductComponentRepository.findByName(GSC_CONFIG_PRODUCT_COMPONENT_TYPE.toUpperCase());
                List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), component, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
                Optional<OrderProductComponent> orderProductComponent = orderProductComponents.stream().findFirst();
                List<OrderProductComponent> orderProductCongigComponents = orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), gscConfigComponent, GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
                Optional<OrderProductComponent> orderProductConfigComponent = orderProductCongigComponents.stream().findFirst();
                if (orderProductComponent.isPresent()) {
                    createQuoteGscConfigurationAttributesDetails(listBook, orderGscDetail, gscOrderToLe,
                            orderProductComponent.get(),orderProductConfigComponent, finalIsGscMultiMacd, orderGsc.getAccessType());
                }

                MstProductComponent accessTypeComponent = mstProductComponentRepository
                        .findByName(orderGsc.getAccessType());
                List<OrderProductComponent> accessTypeorderProductComponents = orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), accessTypeComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE);
                Optional<OrderProductComponent> accessTypeOrderProductComponent = accessTypeorderProductComponents
                        .stream().findFirst();
                if (accessTypeOrderProductComponent.isPresent()) {
                    accessType[0] = String.valueOf(accessTypeOrderProductComponent.get());
                    getSipAttributes(listBook, accessTypeOrderProductComponent.get(), gscOrderToLe, finalIsGscMultiMacd);
                }

            });
        });

        if (isGscMultiMacd) {
            getSIPTrunkAttributes(listBook, gscOrderToLe, accessType[0], multiMacdTrunkDataType);
        }

        MstProductFamily gvpnProductFamily = mstProductFamilyRepository
                .findByNameAndStatus(OrderDetailsExcelDownloadConstants.GVPN, (byte) 1);
        OrderToLe gvpnOrderToLe = getOrderToLeBasedOnProductFamily(order, gvpnProductFamily);
        List<OrderSiteFeasibility> orderSitFeasibilityList = null;
        OrderSiteFeasibility orderSiteFeasibility = null;
        Feasible feasible = null;
        NotFeasible notFeasible = null;
        IntlFeasible intlFeasible = null;
        IntlNotFeasible intlNotFeasible = null;
        CustomFeasibilityRequest customerFeasible = null;
        List<OrderIllSite> orderIllSitesList = getSites(gvpnOrderToLe);
        for (OrderIllSite site : orderIllSitesList) {
            Map<String, Map<String, String>> excelMap = new HashMap<>();
            Map<String, Map<String, String>> secondaryExcelMap = new HashMap<>();

            orderSitFeasibilityList = orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(site, (byte) 1);

            Map<String, String> dependentAttributesOfGVPN = new HashMap<>();
            if (orderSitFeasibilityList != null && !orderSitFeasibilityList.isEmpty()) {
                orderSiteFeasibility = orderSitFeasibilityList.get(0);
                if (orderSiteFeasibility.getFeasibilityType() != null
                        && orderSiteFeasibility.getFeasibilityType().equals(FPConstants.CUSTOM.toString())) {
                    customerFeasible = (CustomFeasibilityRequest) Utils.convertJsonToObject(
                            orderSiteFeasibility.getResponseJson(), CustomFeasibilityRequest.class);
                    createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, customerFeasible, dependentAttributesOfGVPN);
                }
                else if(Objects.nonNull(orderSiteFeasibility) && Objects.nonNull(orderSiteFeasibility.getResponseJson())) {
                    if (orderSiteFeasibility.getFeasibilityMode().equalsIgnoreCase(INTL)) {
                        if (Objects.nonNull(orderSiteFeasibility.getRank())) {
                            LOGGER.info("Feasible International site");
                            intlFeasible = (IntlFeasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
                                    IntlFeasible.class);
                            createIntlSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, intlFeasible, dependentAttributesOfGVPN);
                            dependentAttributesOfGVPN.put(HAND_OFF, intlFeasible.getProviderLocalLoopInterface());
                        } else {
                            LOGGER.info("Non Feasible International site");
                            intlNotFeasible = (IntlNotFeasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
                                    IntlNotFeasible.class);
                            createIntlSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, intlNotFeasible, dependentAttributesOfGVPN);
                            dependentAttributesOfGVPN.put(HAND_OFF, intlNotFeasible.getProviderLocalLoopInterface());
                        }
                    } else {
                        if (orderSiteFeasibility.getRank() != null) {
                            LOGGER.info("Feasible site");
                            feasible = (Feasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
                                    Feasible.class);
                            if (feasible != null) {
                                createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, feasible, dependentAttributesOfGVPN);
                            }
                        } else {
                            LOGGER.info("Non Feasible site");
                            notFeasible = (NotFeasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(), NotFeasible.class);
                            if (notFeasible != null) {
                                createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, notFeasible, dependentAttributesOfGVPN);
                            }
                        }

                    }
                }

            }
            createSiteLocationForExcel(site, listBook);
            createDefaultSiteDetails(listBook, site);

            List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                    .findByReferenceId(site.getId());
            Map<String, OrderProductComponent> componentPriceMap = new HashMap<>();
            for (OrderProductComponent orderProductComponent : orderProductComponents) {
                Map<String, String> attributesMap = new HashMap<>();
                Map<String, String> secondaryAttributesMap = new HashMap<>();

                componentPriceMap.put(orderProductComponent.getMstProductComponent().getName(), orderProductComponent);
                for (OrderProductComponentsAttributeValue attributeValue : orderProductComponent
                        .getOrderProductComponentsAttributeValues()) {

                    if (orderProductComponent.getType() == null || orderProductComponent.getType().equals("primary")) {
                        attributesMap.put(attributeValue.getProductAttributeMaster().getName(),
                                attributeValue.getAttributeValues());
                    } else {
                        secondaryAttributesMap.put(attributeValue.getProductAttributeMaster().getName(),
                                attributeValue.getAttributeValues());
                    }

                }

                if (orderProductComponent.getType() == null || orderProductComponent.getType().equals("primary")) {
                    excelMap.put(orderProductComponent.getMstProductComponent().getName(), attributesMap);

                }
                if (orderProductComponent.getType() != null && orderProductComponent.getType().equals("secondary")) {
                    secondaryExcelMap.put(orderProductComponent.getMstProductComponent().getName(),
                            secondaryAttributesMap);
                }

            }
            createSitePropAndNetworDetailsForExcel(excelMap, listBook, site, gvpnOrderToLe,componentPriceMap, secondaryExcelMap, dependentAttributesOfGVPN);
            createNetworkComponentBasedOnfeasibility(orderSiteFeasibility, listBook, site, feasible, notFeasible,
                    customerFeasible, dependentAttributesOfGVPN);
            createSlaDetails(orderGscList.stream().findFirst().get(), site, listBook);
            createDefaultNetworkComponent(site, listBook);
            createLmPopValue(listBook, site, feasible);
            createBillingComponentPrice(listBook, site);

        }

        createBillingDetails(listBook, attributeValues, gscOrderToLe);
        return listBook;
    }

    /**
     * Method to get orderToLe based on product family
     *
     * @param order
     * @param mstProductFamily
     * @return
     */
    private OrderToLe getOrderToLeBasedOnProductFamily(Order order, MstProductFamily mstProductFamily) {
        OrderToLe orderToLeResult = null;
        for (OrderToLe orderToLe : order.getOrderToLes()) {
            if (mstProductFamily != null) {
                OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
                        .findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
                if (Objects.nonNull(orderToLeProductFamily)) {
                    orderToLeResult = orderToLeProductFamily.getOrderToLe();
                    break;
                }
            }

        }
        return orderToLeResult;
    }

    /**
     * createBillingDetails
     *
     * @param listBook
     * @param attributeValues
     * @param orderToLe
     */
    private void createBillingDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
                                      OrderToLe orderToLe) {
        ExcelBean billingMethod = new ExcelBean(BILLING_COMPONENT, BILLING_METHOD,
                attributeValues.containsKey(BILLING_METHOD) ? attributeValues.get(BILLING_METHOD) : "");
        billingMethod.setOrder(7);
        billingMethod.setGscQuoteId(0);
        listBook.add(billingMethod);

        ExcelBean billingCurrency = new ExcelBean(BILLING_COMPONENT, BILLING_CURRENCY,
                attributeValues.containsKey(BILLING_CURRENCY) ? attributeValues.get(BILLING_CURRENCY) : "");
        billingCurrency.setOrder(7);
        billingCurrency.setGscQuoteId(0);
        listBook.add(billingCurrency);

        ExcelBean billingIncrement = new ExcelBean(BILLING_COMPONENT,
                BILLING_INCREMENT,
                attributeValues.containsKey(BILLING_INCREMENT) ? attributeValues.get(BILLING_INCREMENT) : "");
        billingIncrement.setOrder(7);
        billingIncrement.setGscQuoteId(0);
        listBook.add(billingIncrement);

        ExcelBean billingType = new ExcelBean(BILLING_COMPONENT,
                BILLING_TYPE,
                attributeValues.containsKey(BILLING_TYPE) ? attributeValues.get(BILLING_TYPE) : "");
        billingType.setOrder(7);
        billingType.setGscQuoteId(0);
        listBook.add(billingType);

        ExcelBean billingFrequency = new ExcelBean(BILLING_COMPONENT,
                BILLING_FREQUENCY,
                attributeValues.containsKey(BILLING_FREQUENCY) ? attributeValues.get(BILLING_FREQUENCY) : "");
        billingFrequency.setOrder(1);
        billingFrequency.setGscQuoteId(0);
        listBook.add(billingFrequency);

    }

    /**
     * Method to create quote gsc details
     *
     * @param listBook
     * @param gscQuote
     */
    private void createQuoteGscDetails(List<ExcelBean> listBook, OrderGsc gscQuote) {
        ExcelBean book17 = new ExcelBean(GSC_QUOTE_DETAILS, PRODUCT_NAME, gscQuote.getProductName());
        book17.setOrder(1);
        book17.setGscQuoteId(gscQuote.getId());
        listBook.add(book17);

        ExcelBean book18 = new ExcelBean(GSC_QUOTE_DETAILS, ACCESS_TYPE, gscQuote.getAccessType());
        book18.setOrder(1);
        book18.setGscQuoteId(gscQuote.getId());
        listBook.add(book18);

        String mrc = "0.0";
        if (Objects.nonNull(gscQuote.getMrc())) {
            mrc = String.format("%.2f", gscQuote.getMrc());
        }

        ExcelBean book19 = new ExcelBean(GSC_QUOTE_DETAILS, GSC_QUOTE_MRC, mrc);
        book19.setOrder(1);
        book19.setGscQuoteId(gscQuote.getId());
        listBook.add(book19);

        String nrc = "0.0";
        if (Objects.nonNull(gscQuote.getNrc())) {
            nrc = String.format("%.2f", gscQuote.getNrc());
        }
        ExcelBean book20 = new ExcelBean(GSC_QUOTE_DETAILS, GSC_QUOTE_NRC, nrc);
        book20.setOrder(1);
        book20.setGscQuoteId(gscQuote.getId());
        listBook.add(book20);

        String tcv = "0.0";
        if (Objects.nonNull(gscQuote.getTcv())) {
            tcv = String.format("%.2f", gscQuote.getTcv());
        }
        ExcelBean book21 = new ExcelBean(GSC_QUOTE_DETAILS, GSC_QUOTE_TCV, tcv);
        book21.setOrder(1);
        book21.setGscQuoteId(gscQuote.getId());
        listBook.add(book21);
    }

    private void createQuoteGscConfigurationDetails(List<ExcelBean> listBook, OrderGscDetail orderGscDetail) {
        ExcelBean book22 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, SOURCE_COUNTRY, orderGscDetail.getSrc());
        book22.setOrder(2);
        book22.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book22);

        ExcelBean book23 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, DESTINATION_COUNTRY, orderGscDetail.getDest());
        book23.setOrder(2);
        book23.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book23);

        String mrc = "0.0";
        if (Objects.nonNull(orderGscDetail.getMrc())) {
            mrc = String.format("%.2f", orderGscDetail.getMrc());
        }
        ExcelBean book24 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, GSC_QUOTE_DETAIL_MRC, mrc);
        book24.setOrder(2);
        book24.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book24);

        String nrc = "0.0";
        if (Objects.nonNull(orderGscDetail.getNrc())) {
            nrc = String.format("%.2f", orderGscDetail.getNrc());
        }
        ExcelBean book25 = new ExcelBean(GSC_QUOTE_CONFIGURATION_DETAILS, GSC_QUOTE_DETAIL_NRC, nrc);
        book25.setOrder(2);
        book25.setGscQuoteDetailId(orderGscDetail.getId());
        listBook.add(book25);
    }

    /**
     * Method to create quotegsc configuration details
     *  @param listBook
     * @param orderProductComponent
     * @param accessType
     */
    private void createQuoteGscConfigurationAttributesDetails(List<ExcelBean> listBook, OrderGscDetail orderGscDetail, OrderToLe orderToLe, OrderProductComponent orderProductComponent, Optional<OrderProductComponent> orderProductGscConfigComponent, boolean isGscMultiMacd, String accessType) {

        Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes = orderProductComponent.getOrderProductComponentsAttributeValues();
        List<String> attributesList = Arrays.asList(GscConstants.RATE_PER_MINUTE_FIXED, GscConstants.RATE_PER_MINUTE_MOBILE, GscConstants.RATE_PER_MINUTE_SPECIAL,GscConstants.RATE_PER_MINUTE,
                TERM_NAME, GscConstants.DID_ARC, GscConstants.DID_MRC, GscConstants.DID_NRC, GscConstants.CHANNEL_ARC, GscConstants.CHANNEL_MRC,
                CHANNEL_NRC, GscConstants.ORDER_SETUP_ARC, GscConstants.ORDER_SETUP_MRC, GscConstants.ORDER_SETUP_NRC, GscConstants.TERM_RATE,
                GscConstants.PHONE_TYPE, GscConstants.SURCHARGE_RATE, GscConstants.UIFN_REGISTRATION_CHARGE, GscConstants.GLOBAL_OUTBOUND_DYNAMIC_COLUMN,
                GscConstants.LIST_OF_NUMBERS_TO_BE_PORTED, GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, GscConstants.CITY_SELECTION, GscConstants.EMERGENCY_ADDRESS,
                GscConstants.CALLING_SERVICE_TYPE, GscConstants.QUANTITY_OF_NUMBERS,GscConstants.CITY_WISE_QUANTITY_OF_NUMBERS, GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE,
                GscConstants.REGISTRATION_NUMBER,GscConstants.SOLUTION_TYPE,GscConstants.WORKING_TEMPORARY_TERMINATION_NUMBER);

        List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
        Map<String, Integer> attributesMap = getAttributesMap(attributes);
        LOGGER.info("Access Type:" + orderGscDetail.getOrderGsc().getAccessType());
        Try<GscOrderDataBean> gscOrderDataBean=gscOrderService.getGscOrderById(orderToLe.getOrder().getId());
        Set<OrderProductComponentsAttributeValue> orderProductGscConfigComponentAttributes=null;
        if(orderProductGscConfigComponent.isPresent()) {
            orderProductGscConfigComponentAttributes = orderProductGscConfigComponent.get().getOrderProductComponentsAttributeValues();
        }
        switch (orderProductComponent.getMstProductComponent().getName()) {

            case GscConstants.ITFS: {

                if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && !isGscMultiMacd && (orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.DELETE_NUMBER) || orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                Integer rpmFixedId = attributesMap.get(GscConstants.RATE_PER_MINUTE_FIXED);
                ExcelBean book26;
                if(Objects.nonNull(rpmFixedId) && rpmFixedId > 0) {
                	book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_FIXED,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_FIXED,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book26.setOrder(3);
                book26.setGscQuoteDetailId(0);
                listBook.add(book26);

                Integer rpmMobileId = attributesMap.get(GscConstants.RATE_PER_MINUTE_MOBILE);
                ExcelBean book27;
                if(Objects.nonNull(rpmMobileId) && rpmMobileId> 0) {
                	book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_MOBILE,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_MOBILE,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book27.setOrder(3);
                book27.setGscQuoteDetailId(0);
                listBook.add(book27);

                Integer rpmSpecialId = attributesMap.get(GscConstants.RATE_PER_MINUTE_SPECIAL);
                ExcelBean book28;
                if(Objects.nonNull(rpmSpecialId) && rpmSpecialId> 0) {
                	book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_SPECIAL,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_SPECIAL,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book28.setOrder(3);
                book28.setGscQuoteDetailId(0);
                listBook.add(book28);

                Integer qnId = attributesMap.get(GscConstants.QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);

                Integer portedId = attributesMap.get(GscConstants.LIST_OF_NUMBERS_TO_BE_PORTED);
                ExcelBean portBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.LIST_OF_NUMBERS_TO_BE_PORTED, getAttributeValue(portedId, orderProductComponentAttributes));
                portBook.setOrder(3);
                portBook.setGscQuoteDetailId(0);
                listBook.add(portBook);
                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> GscConstants.ITFS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if(Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !GscConstants.BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                if(PUBLIC_IP.equalsIgnoreCase(accessType) || MPLS.equalsIgnoreCase(accessType)) {
                    Integer workingTemporaryTerminationNumberId = attributesMap.get(GscConstants.WORKING_TEMPORARY_TERMINATION_NUMBER);
                    String workingTemporaryTerminationNumber = getAttributeValue(workingTemporaryTerminationNumberId, orderProductComponentAttributes);
                    if (!StringUtils.isAllBlank(workingTemporaryTerminationNumber)) {
                        ExcelBean workingTemporaryTerminationNumberBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.WORKING_TEMPORARY_TERMINATION_NUMBER,
                                workingTemporaryTerminationNumber);
                        workingTemporaryTerminationNumberBook.setOrder(3);
                        workingTemporaryTerminationNumberBook.setGscQuoteDetailId(0);
                        listBook.add(workingTemporaryTerminationNumberBook);
                    }
                }

                break;
            }
            case GscConstants.LNS: {

                if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && !isGscMultiMacd
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                List<GscCityNumber> cityDetails = gscOrderDetailService.getCityNumberConfiguration(orderGscDetail.getId(),
                        GscConstants.LNS);
                Map<String, String> citiesWithCode = queueForLNSCityNamesCall();

                List<GscTfnBean> cityWithNpa = gscOrderDetailService.getCityNpaConfigurationList(orderGscDetail.getId(),
                        GscConstants.LNS);
                LOGGER.info("No of npa cities is {} ", cityWithNpa.size());

                cityDetails.stream().forEach(gscCityNumber -> {
                    final boolean[] portingFlag = {false};
                    ExcelBean cityBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CITY_SELECTION, citiesWithCode.get(gscCityNumber.getOriginCity()));
                    cityBook.setOrder(3);
                    cityBook.setGscQuoteDetailId(0);
                    listBook.add(cityBook);
                            gscCityNumber.getLnsPortings().forEach(gscLNSPortingNumber -> {

                                String isPortingRequired=getIsPortingRequired(gscLNSPortingNumber.getIsPortingRequired());

                                if(YES.equalsIgnoreCase(isPortingRequired)) {
                                    ExcelBean portingRequiredBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                            "Porting Required", isPortingRequired);
                                    portingRequiredBook.setOrder(3);
                                    portingRequiredBook.setGscQuoteDetailId(0);
                                    listBook.add(portingRequiredBook);
                                    portingFlag[0] =true;

                                    ExcelBean totalPortingNumbersBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                            "Total Porting Numbers", gscLNSPortingNumber.getTotalNumbers().toString());
                                    totalPortingNumbersBook.setOrder(3);
                                    totalPortingNumbersBook.setGscQuoteDetailId(0);
                                    listBook.add(totalPortingNumbersBook);

                                    gscLNSPortingNumber.getTfnBeans().stream().forEach(gscTfnBean -> {
                                        portingNumbers = gscTfnBean.getNumber() + ", " + portingNumbers;
                                    });

                                    ExcelBean portingNumbersBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                            "All Porting Numbers", portingNumbers);
                                    portingNumbersBook.setOrder(3);
                                    portingNumbersBook.setGscQuoteDetailId(0);
                                    listBook.add(portingNumbersBook);
                                }else{
                                    ExcelBean howManyPortingNumbers = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                            "New Numbers", gscLNSPortingNumber.getTotalNumbers().toString());
                                    howManyPortingNumbers.setOrder(3);
                                    howManyPortingNumbers.setGscQuoteDetailId(0);
                                    listBook.add(howManyPortingNumbers);
                                }
                                portingNumbers = "";

                                cityWithNpa.stream().map(gscTfnBean -> {
                                    LOGGER.info("city for npa {}", gscTfnBean.getCity());
                                    LOGGER.info("origin city {}", gscCityNumber.getOriginCity());
                                    LOGGER.info("origin city with code {}", citiesWithCode.get(gscCityNumber.getOriginCity()));
                                    return gscTfnBean;
                                }).filter(gscTfnBean -> gscTfnBean.getCity().equalsIgnoreCase(gscCityNumber.getOriginCity())).findFirst().ifPresent(gscTfnBean -> {
                                    LOGGER.info("gsctfn city code {} and npa {} ", gscTfnBean.getCity(), gscTfnBean.getNpa());
                                    ExcelBean howManyPortingNumbers = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                            "NPA", gscTfnBean.getNpa());
                                    howManyPortingNumbers.setOrder(3);
                                    howManyPortingNumbers.setGscQuoteDetailId(0);
                                    listBook.add(howManyPortingNumbers);
                                    cityWithNpa.remove(gscTfnBean);
                                });

                            });
                            if(!portingFlag[0]){
                                ExcelBean portingRequiredBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                        "Porting Required", GscConstants.NO);
                                portingRequiredBook.setOrder(3);
                                portingRequiredBook.setGscQuoteDetailId(0);
                                listBook.add(portingRequiredBook);
                            }
                        });

                Integer rpm = attributesMap.get(GscConstants.RATE_PER_MINUTE);
                ExcelBean book32;
                if(Objects.nonNull(rpm) && rpm> 0) {
                	book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book32.setOrder(3);
                book32.setGscQuoteDetailId(0);
                listBook.add(book32);

                Integer qnId = attributesMap.get(GscConstants.QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);

                Integer portedId = attributesMap.get(GscConstants.LIST_OF_NUMBERS_TO_BE_PORTED);
                ExcelBean portBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.LIST_OF_NUMBERS_TO_BE_PORTED, getAttributeValue(portedId, orderProductComponentAttributes));
                portBook.setOrder(3);
                portBook.setGscQuoteDetailId(0);
                listBook.add(portBook);

                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> GscConstants.LNS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if(Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !GscConstants.BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                if(PUBLIC_IP.equalsIgnoreCase(accessType) || MPLS.equalsIgnoreCase(accessType)) {
                    Integer workingTemporaryTerminationNumberId = attributesMap.get(GscConstants.WORKING_TEMPORARY_TERMINATION_NUMBER);
                    String workingTemporaryTerminationNumber = getAttributeValue(workingTemporaryTerminationNumberId, orderProductComponentAttributes);
                    if (!StringUtils.isAllBlank(workingTemporaryTerminationNumber)) {
                        ExcelBean workingTemporaryTerminationNumberBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.WORKING_TEMPORARY_TERMINATION_NUMBER,
                                workingTemporaryTerminationNumber);
                        workingTemporaryTerminationNumberBook.setOrder(3);
                        workingTemporaryTerminationNumberBook.setGscQuoteDetailId(0);
                        listBook.add(workingTemporaryTerminationNumberBook);
                    }
                }
                break;
            }

            case GscConstants.UIFN: {

                if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && !isGscMultiMacd
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);

                }

                Integer rpmFixedId = attributesMap.get(GscConstants.RATE_PER_MINUTE_FIXED);
                ExcelBean book32;
                if(Objects.nonNull(rpmFixedId) && rpmFixedId > 0) {
                	book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_FIXED,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_FIXED,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book32.setOrder(3);
                book32.setGscQuoteDetailId(0);
                listBook.add(book32);

                Integer rpmMobileId = attributesMap.get(GscConstants.RATE_PER_MINUTE_MOBILE);
                ExcelBean book33;
                if(Objects.nonNull(rpmMobileId) && rpmMobileId> 0) {
                	book33 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_MOBILE,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book33 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_MOBILE,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book33.setOrder(3);
                book33.setGscQuoteDetailId(0);
                listBook.add(book33);

                Integer rpmSpecialId = attributesMap.get(GscConstants.RATE_PER_MINUTE_SPECIAL);
                ExcelBean book34;
                if(Objects.nonNull(rpmSpecialId) && rpmSpecialId> 0) {
                	 book34 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                             OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_SPECIAL,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	 book34 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                             OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE_SPECIAL,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book34.setOrder(3);
                book34.setGscQuoteDetailId(0);
                listBook.add(book34);

                Integer uifnRegChargeId = attributesMap.get(GscConstants.UIFN_REGISTRATION_CHARGE);
                String charge = getAttributeValue(uifnRegChargeId, orderProductComponentAttributes);
                if (charge.isEmpty())
                    charge = "0";
                ExcelBean uifnRegChargeBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.UIFN_REGISTRATION_CHARGE,
                        String.format("%.2f", Double.parseDouble(charge)));
                uifnRegChargeBook.setOrder(3);
                uifnRegChargeBook.setGscQuoteDetailId(0);
                listBook.add(uifnRegChargeBook);

                Integer qnId = attributesMap.get(GscConstants.QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);

                Integer portedId = attributesMap.get(GscConstants.LIST_OF_NUMBERS_TO_BE_PORTED);
                ExcelBean portBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.LIST_OF_NUMBERS_TO_BE_PORTED, getAttributeValue(portedId, orderProductComponentAttributes));
                portBook.setOrder(3);
                portBook.setGscQuoteDetailId(0);
                listBook.add(portBook);


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> GscConstants.UIFN.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if(Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !GscConstants.BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                if(PUBLIC_IP.equalsIgnoreCase(accessType) || MPLS.equalsIgnoreCase(accessType)) {
                    Integer workingTemporaryTerminationNumberId = attributesMap.get(GscConstants.WORKING_TEMPORARY_TERMINATION_NUMBER);
                    String workingTemporaryTerminationNumber = getAttributeValue(workingTemporaryTerminationNumberId, orderProductComponentAttributes);
                    if (!StringUtils.isAllBlank(workingTemporaryTerminationNumber)) {
                        ExcelBean workingTemporaryTerminationNumberBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.WORKING_TEMPORARY_TERMINATION_NUMBER,
                                workingTemporaryTerminationNumber);
                        workingTemporaryTerminationNumberBook.setOrder(3);
                        workingTemporaryTerminationNumberBook.setGscQuoteDetailId(0);
                        listBook.add(workingTemporaryTerminationNumberBook);
                    }
                }

                break;
            }
            case GscConstants.ACANS: {

                List<OrderProductComponentsAttributeValue> cityWiseNumberAttributes = getComponentAttributesBasedOnSpecificAttribute(
                        attributesMap.get(GscConstants.CITY_WISE_QUANTITY_OF_NUMBERS), orderProductComponentAttributes);

                cityWiseNumberAttributes.stream().forEach(orderProductComponentsAttributeValue -> {

                    List<String> numberValues = new LinkedList<>(Arrays.asList(orderProductComponentsAttributeValue.getAttributeValues().split(":")));

                    ExcelBean cityNameValue = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CITY_SELECTION, numberValues.get(0).split("_")[1]);
                    cityNameValue.setOrder(3);
                    cityNameValue.setGscQuoteDetailId(0);
                    listBook.add(cityNameValue);

                    ExcelBean cityQuantityOfNumbers = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            GscConstants.CITY_WISE_QUANTITY_OF_NUMBERS, String.valueOf(numberValues.size() - 1));
                    cityQuantityOfNumbers.setOrder(3);
                    cityQuantityOfNumbers.setGscQuoteDetailId(0);
                    listBook.add(cityQuantityOfNumbers);
                });

                if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD)
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                Integer rpm = attributesMap.get(GscConstants.RATE_PER_MINUTE);
                ExcelBean book38;
                if(Objects.nonNull(rpm) && rpm> 0) {
                	book38 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book38 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book38.setOrder(3);
                book38.setGscQuoteDetailId(0);
                listBook.add(book38);

                Integer qnId = attributesMap.get(GscConstants.QUANTITY_OF_NUMBERS);
                ExcelBean qnBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.QUANTITY_OF_NUMBERS,
                        getAttributeValue(qnId, orderProductComponentAttributes));
                qnBook.setOrder(3);
                qnBook.setGscQuoteDetailId(0);
                listBook.add(qnBook);


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> GscConstants.ACANS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if(Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !GscConstants.BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                if(PUBLIC_IP.equalsIgnoreCase(accessType) || MPLS.equalsIgnoreCase(accessType)) {
                    Integer workingTemporaryTerminationNumberId = attributesMap.get(GscConstants.WORKING_TEMPORARY_TERMINATION_NUMBER);
                    String workingTemporaryTerminationNumber = getAttributeValue(workingTemporaryTerminationNumberId, orderProductComponentAttributes);
                    if (!StringUtils.isAllBlank(workingTemporaryTerminationNumber)) {
                        ExcelBean workingTemporaryTerminationNumberBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.WORKING_TEMPORARY_TERMINATION_NUMBER,
                                workingTemporaryTerminationNumber);
                        workingTemporaryTerminationNumberBook.setOrder(3);
                        workingTemporaryTerminationNumberBook.setGscQuoteDetailId(0);
                        listBook.add(workingTemporaryTerminationNumberBook);
                    }
                }

                break;
            }
            case GscConstants.ACDTFS: {

                if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && !isGscMultiMacd
                        && (orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.DELETE_NUMBER)
                        || orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE))) {
                    ExcelBean tfnNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TOLL_FREE_NUMBERS, getTfnNumbers(orderGscDetail));
                    tfnNumBook.setOrder(3);
                    tfnNumBook.setGscQuoteDetailId(0);
                    listBook.add(tfnNumBook);
                }

                Integer rpm = attributesMap.get(GscConstants.RATE_PER_MINUTE);
                ExcelBean book41;
                if(Objects.nonNull(rpm) && rpm> 0) {
                	book41 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE,OrderDetailsExcelDownloadConstants.SHOW_COMMERCIAL_BREAK);
                }else {
                	book41 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.RATE_PER_MINUTE,OrderDetailsExcelDownloadConstants.HIDE_COMMERCIAL_BREAK);
                }
                book41.setOrder(3);
                book41.setGscQuoteDetailId(0);
                listBook.add(book41);

                if (orderGscDetail.getOrderGsc().getAccessType().equalsIgnoreCase(GscConstants.PSTN) || orderGscDetail.getOrderGsc().getAccessType().equalsIgnoreCase(GscConstants.PUBLIC_IP)) {
                    Integer qnId = attributesMap.get(GscConstants.QUANTITY_OF_NUMBERS);
                    ExcelBean qnBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.QUANTITY_OF_NUMBERS,
                            getAttributeValue(qnId, orderProductComponentAttributes));
                    qnBook.setOrder(3);
                    qnBook.setGscQuoteDetailId(0);
                    listBook.add(qnBook);
                }


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> GscConstants.ACDTFS.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if(Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !GscConstants.BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                if(PUBLIC_IP.equalsIgnoreCase(accessType) || MPLS.equalsIgnoreCase(accessType)) {
                    Integer workingTemporaryTerminationNumberId = attributesMap.get(GscConstants.WORKING_TEMPORARY_TERMINATION_NUMBER);
                    String workingTemporaryTerminationNumber = getAttributeValue(workingTemporaryTerminationNumberId, orderProductComponentAttributes);
                    if (!StringUtils.isAllBlank(workingTemporaryTerminationNumber)) {
                        ExcelBean workingTemporaryTerminationNumberBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.WORKING_TEMPORARY_TERMINATION_NUMBER,
                                workingTemporaryTerminationNumber);
                        workingTemporaryTerminationNumberBook.setOrder(3);
                        workingTemporaryTerminationNumberBook.setGscQuoteDetailId(0);
                        listBook.add(workingTemporaryTerminationNumberBook);
                    }
                }

                break;
            }
            case GscConstants.DOMESTIC_VOICE: {
                if((orderToLe.getOrderType().equalsIgnoreCase(GscConstants.NEW) && !isGscMultiMacd) || orderToLe.getOrderCategory().equalsIgnoreCase("ADD_SITE")) {
                    String emergencyAddressValue = getAttributeValue(attributesMap.get(GscConstants.EMERGENCY_ADDRESS), orderProductComponentAttributes);
                    /*Since emergency value save with ':' below we are spliting it*/
                    LOGGER.info("Emergency Address Value::{}",emergencyAddressValue);
                    Map<String, String> siteWiseRegistrationNumberMap = getSiteWiseRegistrationNumbers(orderProductComponentAttributes, attributesMap);

                    if(Objects.nonNull(emergencyAddressValue) && !emergencyAddressValue.isEmpty()){
                        String[] emergencyAddress = emergencyAddressValue.split(",");
                        int count = 1;
                        for (String emergencyAddressId : emergencyAddress) {
                            emergencyAddressValue = getEmergencyAddress(emergencyAddressId.split(":")[0]);

                            ExcelBean emergencyBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                    OrderDetailsExcelDownloadConstants.EMERGENCY_ADDRESS + " " + String.valueOf(count),
                                    emergencyAddressValue);
                            emergencyBook.setOrder(3);
                            emergencyBook.setGscQuoteDetailId(0);
                            listBook.add(emergencyBook);

                            ExcelBean quantityOfNumbers = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                    GscConstants.QUANTITY_OF_NUMBERS + " " + String.valueOf(count),
                                    emergencyAddressId.split(":")[1]);
                            emergencyBook.setOrder(3);
                            emergencyBook.setGscQuoteDetailId(0);
                            listBook.add(quantityOfNumbers);
                            count++;
                             if(!CollectionUtils.isEmpty(siteWiseRegistrationNumberMap) && siteWiseRegistrationNumberMap.containsKey(emergencyAddressId.split(":")[0])) {
                                 ExcelBean registrationNumberBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                                         OrderDetailsExcelDownloadConstants.REGISTRATION_NUMBER,
                                         siteWiseRegistrationNumberMap.get(emergencyAddressId.split(":")[0]));
                                 registrationNumberBook.setOrder(3);
                                 registrationNumberBook.setGscQuoteDetailId(0);
                                 listBook.add(registrationNumberBook);
                             }
                        }
                    }
                }

                Integer didMrcId = attributesMap.get(GscConstants.DID_MRC);
                String didMrccharge = getAttributeValue(didMrcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(didMrccharge))
                    didMrccharge = "0.0";
                ExcelBean domVoiceDidMrcBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.DID_MRC,
                        String.format("%.2f", Double.parseDouble(didMrccharge)));
                domVoiceDidMrcBook.setOrder(3);
                domVoiceDidMrcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceDidMrcBook);

                Integer didNrcId = attributesMap.get(GscConstants.DID_NRC);
                String didNrccharge = getAttributeValue(didNrcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(didNrccharge))
                    didNrccharge = "0.0";
                ExcelBean domVoiceDidNrcBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.DID_NRC,
                        String.format("%.2f", Double.parseDouble(didNrccharge)));
                domVoiceDidNrcBook.setOrder(3);
                domVoiceDidNrcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceDidNrcBook);

                Integer didArcId = attributesMap.get(GscConstants.DID_ARC);
                String didArcCharge = getAttributeValue(didArcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(didArcCharge))
                    didArcCharge = "0.0";
                ExcelBean domVoiceDidArcBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.DID_ARC,
                        String.format("%.2f", Double.parseDouble(didArcCharge)));
                domVoiceDidArcBook.setOrder(3);
                domVoiceDidArcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceDidArcBook);

                Integer channelMrcId = attributesMap.get(GscConstants.CHANNEL_MRC);
                String channelMrccharge = getAttributeValue(channelMrcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(channelMrccharge))
                    channelMrccharge = "0.0";
                ExcelBean domVoiceChannelMrcBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.CHANNEL_MRC,
                        String.format("%.2f", Double.parseDouble(channelMrccharge)));
                domVoiceChannelMrcBook.setOrder(3);
                domVoiceChannelMrcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceChannelMrcBook);

                Integer channelNrcId = attributesMap.get(CHANNEL_NRC);
                String channelNrccharge = getAttributeValue(channelNrcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(channelNrccharge))
                    channelNrccharge = "0.0";
                ExcelBean domVoiceChannelNrcBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.CHANNEL_NRC,
                        String.format("%.2f", Double.parseDouble(channelNrccharge)));
                domVoiceChannelNrcBook.setOrder(3);
                domVoiceChannelNrcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceChannelNrcBook);

                Integer channelArcId = attributesMap.get(GscConstants.CHANNEL_ARC);
                String channelArccharge = getAttributeValue(channelArcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(channelArccharge))
                    channelArccharge = "0.0";
                ExcelBean domVoiceChannelArcBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.CHANNEL_ARC,
                        String.format("%.2f", Double.parseDouble(channelArccharge)));
                domVoiceChannelArcBook.setOrder(3);
                domVoiceChannelArcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceChannelArcBook);

                Integer orderSetupMrcId = attributesMap.get(GscConstants.ORDER_SETUP_MRC);
                String orderSetupMrccharge = getAttributeValue(orderSetupMrcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(orderSetupMrccharge))
                    orderSetupMrccharge = "0.0";
                ExcelBean domVoiceOrderSetupMrcBook = new ExcelBean(
                        OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.ORDER_SETUP_MRC,
                        String.format("%.2f", Double.parseDouble(orderSetupMrccharge)));
                domVoiceOrderSetupMrcBook.setOrder(3);
                domVoiceOrderSetupMrcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceOrderSetupMrcBook);

                Integer orderSetupNrcId = attributesMap.get(GscConstants.ORDER_SETUP_NRC);
                String orderSetupNrccharge = getAttributeValue(orderSetupNrcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(orderSetupNrccharge))
                    orderSetupNrccharge = "0.0";
                ExcelBean domVoiceOrderSetupNrcBook = new ExcelBean(
                        OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.ORDER_SETUP_NRC,
                        String.format("%.2f", Double.parseDouble(orderSetupNrccharge)));
                domVoiceOrderSetupNrcBook.setOrder(3);
                domVoiceOrderSetupNrcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceOrderSetupNrcBook);

                Integer orderSetupArcId = attributesMap.get(GscConstants.ORDER_SETUP_ARC);
                String orderSetupArccharge = getAttributeValue(orderSetupArcId, orderProductComponentAttributes);
                if (StringUtils.isBlank(orderSetupArccharge))
                    orderSetupArccharge = "0.0";
                ExcelBean domVoiceOrderSetupArcBook = new ExcelBean(
                        OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.ORDER_SETUP_ARC,
                        String.format("%.2f", Double.parseDouble(orderSetupArccharge)));
                domVoiceOrderSetupArcBook.setOrder(3);
                domVoiceOrderSetupArcBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceOrderSetupArcBook);

                Integer terminationNameId = attributesMap.get(TERM_NAME);
                ExcelBean domVoiceTermNameBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.TERMINATION_NAME,
                        getAttributeValue(terminationNameId, orderProductComponentAttributes));
                domVoiceTermNameBook.setOrder(3);
                domVoiceTermNameBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceTermNameBook);

                Integer terminationRateId = attributesMap.get(GscConstants.TERM_RATE);
                ExcelBean domVoiceTermRateBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.TERMINATION_RATE,
                        getAttributeValue(terminationRateId, orderProductComponentAttributes));
                domVoiceTermRateBook.setOrder(3);
                domVoiceTermRateBook.setGscQuoteDetailId(0);
                listBook.add(domVoiceTermRateBook);

                Integer phoneTypeId = attributesMap.get(GscConstants.PHONE_TYPE);
                ExcelBean globalPhoneTypeBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.PHONE_TYPE,
                        getAttributeValue(phoneTypeId, orderProductComponentAttributes));
                globalPhoneTypeBook.setOrder(3);
                globalPhoneTypeBook.setGscQuoteDetailId(0);
                listBook.add(globalPhoneTypeBook);

                Integer solutionTypeId = attributesMap.get(GscConstants.SOLUTION_TYPE);
                String solutionType = getAttributeValue(solutionTypeId, orderProductComponentAttributes);
                if (!StringUtils.isAllBlank(solutionType)) {
                    ExcelBean solutionTypeBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.SOLUTION_TYPE,
                            solutionType);
                    solutionTypeBook.setOrder(3);
                    solutionTypeBook.setGscQuoteDetailId(0);
                    listBook.add(solutionTypeBook);
                }

                if (orderGscDetail.getOrderGsc().getAccessType().equalsIgnoreCase(GscConstants.PSTN) || orderGscDetail.getOrderGsc().getAccessType().equalsIgnoreCase(GscConstants.PUBLIC_IP)) {
                    Integer qnId = attributesMap.get(GscConstants.QUANTITY_OF_NUMBERS);
                    ExcelBean qnBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.QUANTITY_OF_NUMBERS,
                            getAttributeValue(qnId, orderProductComponentAttributes));
                    qnBook.setOrder(3);
                    qnBook.setGscQuoteDetailId(0);
                    listBook.add(qnBook);

                    Integer portedId = attributesMap.get(GscConstants.LIST_OF_NUMBERS_TO_BE_PORTED);
                    ExcelBean portBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.LIST_OF_NUMBERS_TO_BE_PORTED, getAttributeValue(portedId, orderProductComponentAttributes));
                    portBook.setOrder(3);
                    portBook.setGscQuoteDetailId(0);
                    listBook.add(portBook);
                }


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> GscConstants.DOMESTIC_VOICE.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if(Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !GscConstants.BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }

                break;
            }
            case GscConstants.GLOBAL_OUTBOUND: {

                Integer callId = attributesMap.get(GscConstants.CALLING_SERVICE_TYPE);
                ExcelBean callServiceBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.CALLING_SERVICE_TYPE,
                        getAttributeValue(callId, orderProductComponentAttributes));
                callServiceBook.setOrder(3);
                callServiceBook.setGscQuoteDetailId(0);
                listBook.add(callServiceBook);

                Integer terminationNameId = attributesMap.get(TERM_NAME);
                ExcelBean globalOutboundTermNameBook = new ExcelBean(
                        OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.TERMINATION_NAME,
                        getAttributeValue(terminationNameId, orderProductComponentAttributes));
                globalOutboundTermNameBook.setOrder(3);
                globalOutboundTermNameBook.setGscQuoteDetailId(0);
                listBook.add(globalOutboundTermNameBook);

                Integer terminationRateId = attributesMap.get(GscConstants.TERM_RATE);
                ExcelBean globalTermRateBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.TERMINATION_RATE,
                        getAttributeValue(terminationRateId, orderProductComponentAttributes));
                globalTermRateBook.setOrder(3);
                globalTermRateBook.setGscQuoteDetailId(0);
                listBook.add(globalTermRateBook);

                Integer phoneTypeId = attributesMap.get(GscConstants.PHONE_TYPE);
                ExcelBean globalPhoneTypeBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.PHONE_TYPE,
                        getAttributeValue(phoneTypeId, orderProductComponentAttributes));
                globalPhoneTypeBook.setOrder(3);
                globalPhoneTypeBook.setGscQuoteDetailId(0);
                listBook.add(globalPhoneTypeBook);


                /* Getting GSC downstream(Tiger attribute) attribute values*/
                gscOrderDataBean.get().getSolutions().stream()
                        .flatMap(solution -> solution.getGscOrders().stream())
                        .filter(gscOrderBean -> GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(gscOrderBean.getServiceName()))
                        .forEach(gscOrderBean -> {
                            getDownStreamAttributeValues(listBook, gscOrderBean);
                        });
                if(Objects.nonNull(orderProductGscConfigComponentAttributes)) {
                    String expDeliveryTime = getAttributeValue(attributesMap.get(GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE), orderProductGscConfigComponentAttributes);
                    if (Objects.nonNull(expDeliveryTime) && !expDeliveryTime.isEmpty() && !GscConstants.BEST_EFFORT.equalsIgnoreCase(expDeliveryTime)) {
                        ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.LEAD_TIME,
                                getLeadTime(orderGscDetail.getCreatedTime(), expDeliveryTime));
                        leadTime.setOrder(3);
                        leadTime.setGscQuoteDetailId(0);
                        listBook.add(leadTime);
                    }
                }
                break;
            }

            default:

                LOGGER.info("Invalid master product component Name");

        }

    }

    /**
     * Get site wise registration Numbers map
     *
     * @param orderProductComponentAttributes
     * @param attributesMap
     * @return
     */
    private Map<String, String> getSiteWiseRegistrationNumbers(Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes, Map<String, Integer> attributesMap) {
        Map<String, String> siteWiseRegistrationNumberMap = new HashMap<>();
        Integer registrationNumberId = attributesMap.get(GscConstants.REGISTRATION_NUMBER);
        LOGGER.info("Registration Number id {}", registrationNumberId);
        if(Objects.nonNull(registrationNumberId)) {
            String registrationNumber = getAttributeValue(registrationNumberId, orderProductComponentAttributes);
            List<String> commaSeparatedRegistrationNumbers = Arrays.asList(registrationNumber.split(","));
            commaSeparatedRegistrationNumbers.stream().forEach(commaSeparatedRegistrationNumber -> {
                LOGGER.info("Each Separated Registration Number {}", commaSeparatedRegistrationNumber);
                if(isNotBlank(commaSeparatedRegistrationNumber)) {
                    String[] siteWiseRegistrationNumber = commaSeparatedRegistrationNumber.split(":");
                    if (Objects.nonNull(siteWiseRegistrationNumber) && 0 < siteWiseRegistrationNumber.length) {
                        LOGGER.info("Site wise registration site {} and number {} ",siteWiseRegistrationNumber[0], siteWiseRegistrationNumber[1]);
                        siteWiseRegistrationNumberMap.put(siteWiseRegistrationNumber[0], siteWiseRegistrationNumber[1]);
                    }
                }
            });
        }
        return siteWiseRegistrationNumberMap;
    }

    private String getIsPortingRequired(Byte value) {
        if(1 == value.intValue()) {
            return GscConstants.YES;
        }
        return GscConstants.NO;
    }

    /**
     * Method to get tfn Numbers
     *
     * @param orderGscDetail
     * @return
     */
    private String getTfnNumbers(OrderGscDetail orderGscDetail) {
        StringBuilder tfnNumbersList = new StringBuilder();
        try {
            List<GscTfnBean> tfnNumbers = gscOrderDetailService.getNumbersForConfiguration(orderGscDetail, null, 10,
                    false);
            if (Objects.nonNull(tfnNumbers)) {
                tfnNumbers.stream().forEach(tfnNumber -> {
                    tfnNumbersList.append(tfnNumber.getNumber()).append(CommonConstants.COMMA);

                });
            }
        } catch (Exception e) {
            LOGGER.error("error in processing tfnNumbers", e);
        }
        String numbers = tfnNumbersList.toString();
        if (numbers.length() > 1) {
            numbers = tfnNumbersList.toString().substring(0, tfnNumbersList.toString().length() - 1);

        } else
            numbers = "";
        return numbers;

    }

    /**
     * Method to get SIP attributes
     * @param listBook
     * @param orderProductComponent
     * @param isGscMultiMacd
     */
    private void getSipAttributes(List<ExcelBean> listBook, OrderProductComponent orderProductComponent, OrderToLe orderToLe, boolean isGscMultiMacd) {

        Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes = orderProductComponent
                .getOrderProductComponentsAttributeValues();
        List<String> attributesList = Arrays.asList(GscConstants.REQUIRED_ON_A_AND_B_NUMBER,
                GscConstants.DTMF_RELAY_SUPPORT, GscConstants.SUPPORTED_SIP_PRIVACY_HEADERS,
                GscConstants.SESSION_KEEP_ALIVE_TIMER, GscConstants.PREFIX_ADDITION, GscConstants.CUSTOMER_PUBLIC_IP,
                GscConstants.TRANSPORT_PROTOCOL, GscConstants.CODEC, GscConstants.NO_OF_CONCURRENT_CHANNEL,
                GscConstants.EQUIPMENT_ADDRESS, GscConstants.ROUTING_TOPOLOGY, GscConstants.DIAL_PLAN_LOGIC,
                GscConstants.CALLS_PER_SECOND, GscConstants.CERTIFICATE_AUTHORITY_SUPPORT, GscConstants.FQDN,
                GscConstants.IP_ADDRESS_SPACE, GscConstants.CUSTOMER_DEVICE_IP, GscConstants.ADDITIONAL_INFORMATION,
                GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, GscConstants.QUANTITY_OF_NUMBERS,
                GscConstants.LIST_OF_NUMBERS_TO_BE_PORTED, GscConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE,
                GscConstants.TERMINATION_NUMBER_ISD_CODE, GscConstants.REQUIRED_PORTING_NUMBERS,
                GscAttributeConstants.SELECTED_TERMINATION_NUMBER_OUTPULSE,
                GscConstants.SWTCH_UNIT_CD_RERT,GscConstants.CIRCT_GR_CD_RERT,GscConstants.CIRCUIT_ID,GscConstants.NNI_ID,
                GscAttributeConstants.DOMESTIC_VOICE_SITE_ADDRESS);
        List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
        Map<String, Integer> attributesMap = getAttributesMap(attributes);

        switch (orderProductComponent.getMstProductComponent().getName()) {
            case GscConstants.PSTN: {

                if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE)) {
                    Integer oldTermId = attributesMap.get(GscConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE);

                    ExcelBean oldTermNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE, appendTerminationCountryISDCode(oldTermId, orderProductComponentAttributes, attributesMap));
                    oldTermNumBook.setOrder(3);
                    oldTermNumBook.setGscQuoteDetailId(0);
                    listBook.add(oldTermNumBook);
                }
                Integer termId = attributesMap.get(GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE);
                ExcelBean termNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, appendTerminationCountryISDCode(termId, orderProductComponentAttributes, attributesMap));
                termNumBook.setOrder(3);
                termNumBook.setGscQuoteDetailId(0);
                listBook.add(termNumBook);

                //showing MACD Add country selected outpulse number in LR export
                getSelectedOutpulseNumber(listBook, attributesMap, orderProductComponentAttributes);

                String requiredPortingNumbers=getAttributeValue(attributesMap.get(GscConstants.REQUIRED_PORTING_NUMBERS), orderProductComponentAttributes);
                if(StringUtils.isNoneBlank(requiredPortingNumbers) && (!Pattern.compile("^[,]").matcher(requiredPortingNumbers).find())) {
                    ExcelBean portingBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.REQUIRED_PORTING_NUMBERS,requiredPortingNumbers);
                    portingBook.setOrder(3);
                    portingBook.setGscQuoteDetailId(0);
                    listBook.add(portingBook);
                }

            }
            break;
            case GscConstants.PUBLIC_IP: {

                if(!isGscMultiMacd) {
                    if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE)) {
                        Integer oldTermId = attributesMap.get(GscConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE);
                        ExcelBean oldTermNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE, getAttributeValue(oldTermId, orderProductComponentAttributes));
                        oldTermNumBook.setOrder(3);
                        oldTermNumBook.setGscQuoteDetailId(0);
                        listBook.add(oldTermNumBook);
                    }
               /* //show MACD service attribute
                if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD)) {
                    getMACDServiceAttributes(listBook,attributesMap,orderProductComponentAttributes);
                }*/
                    Integer termId = attributesMap.get(GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE);
                    ExcelBean termNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, getAttributeValue(termId, orderProductComponentAttributes));
                    termNumBook.setOrder(3);
                    termNumBook.setGscQuoteDetailId(0);
                    listBook.add(termNumBook);

                    //showing MACD Add country selected outpulse number in LR export
                    getSelectedOutpulseNumber(listBook, attributesMap, orderProductComponentAttributes);

                    getDIDSiteAddress(listBook, attributesMap, orderProductComponentAttributes);
               /* if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && isGscMultiMacd) {
                    getSIPTrunkAttributes(listBook, orderToLe, orderProductComponent.getMstProductComponent().getName());
                }*/

                    Integer requiredId = attributesMap.get(GscConstants.REQUIRED_ON_A_AND_B_NUMBER);
                    ExcelBean book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.REQUIRED_ON_A_AND_B_NUMBER,
                            getAttributeValue(requiredId, orderProductComponentAttributes));
                    book26.setOrder(3);
                    book26.setGscQuoteDetailId(0);
                    listBook.add(book26);

                    Integer dtmfId = attributesMap.get(GscConstants.DTMF_RELAY_SUPPORT);
                    ExcelBean book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.DTMF_RELAY_SUPPORT,
                            getAttributeValue(dtmfId, orderProductComponentAttributes));
                    book27.setOrder(3);
                    book27.setGscQuoteDetailId(0);
                    listBook.add(book27);

                    Integer privacyHeaderId = attributesMap.get(GscConstants.SUPPORTED_SIP_PRIVACY_HEADERS);
                    ExcelBean book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.SUPPORTED_SIP_PRIVACY_HEADERS,
                            getAttributeValue(privacyHeaderId, orderProductComponentAttributes));
                    book28.setOrder(3);
                    book28.setGscQuoteDetailId(0);
                    listBook.add(book28);

                    Integer sessionAliveId = attributesMap.get(GscConstants.SESSION_KEEP_ALIVE_TIMER);
                    ExcelBean book29 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.SESSION_KEEP_ALIVE_TIMER,
                            getAttributeValue(sessionAliveId, orderProductComponentAttributes));
                    book29.setOrder(3);
                    book29.setGscQuoteDetailId(0);
                    listBook.add(book29);

                    Integer prefixAdId = attributesMap.get(GscConstants.PREFIX_ADDITION);
                    ExcelBean book30 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.PREFIX_ADDITION,
                            getAttributeValue(prefixAdId, orderProductComponentAttributes));
                    book30.setOrder(3);
                    book30.setGscQuoteDetailId(0);
                    listBook.add(book30);

                    Integer customerPublicId = attributesMap.get(GscConstants.CUSTOMER_PUBLIC_IP);
                    ExcelBean book31 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CUSTOMER_PUBLIC_IP,
                            getAttributeValue(customerPublicId, orderProductComponentAttributes));
                    book31.setOrder(3);
                    book31.setGscQuoteDetailId(0);
                    listBook.add(book31);

                    Integer transportId = attributesMap.get(GscConstants.TRANSPORT_PROTOCOL);
                    ExcelBean book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TRANSPORT_PROTOCOL,
                            getAttributeValue(transportId, orderProductComponentAttributes));
                    book32.setOrder(3);
                    book32.setGscQuoteDetailId(0);
                    listBook.add(book32);

                    Integer codecId = attributesMap.get(GscConstants.CODEC);
                    String codecValue = getAttributeValue(codecId, orderProductComponentAttributes);
                    if (!StringUtils.isAllBlank(codecValue)) {
                        ExcelBean book33 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.CODEC,
                                codecValue);
                        book33.setOrder(3);
                        book33.setGscQuoteDetailId(0);
                        listBook.add(book33);
                    }

                    Integer channelId = attributesMap.get(GscConstants.NO_OF_CONCURRENT_CHANNEL);
                    ExcelBean book34 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL,
                            getAttributeValue(channelId, orderProductComponentAttributes));
                    book34.setOrder(3);
                    book34.setGscQuoteDetailId(0);
                    listBook.add(book34);

                    Integer equipmentId = attributesMap.get(GscConstants.EQUIPMENT_ADDRESS);
                    ExcelBean book35 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.EQUIPMENT_ADDRESS,
                            getAttributeValue(equipmentId, orderProductComponentAttributes));
                    book35.setOrder(3);
                    book35.setGscQuoteDetailId(0);
                    listBook.add(book35);

                    Integer routingId = attributesMap.get(GscConstants.ROUTING_TOPOLOGY);
                    ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.ROUTING_TOPOLOGY,
                            getAttributeValue(routingId, orderProductComponentAttributes));
                    book36.setOrder(3);
                    book36.setGscQuoteDetailId(0);
                    listBook.add(book36);

                    Integer dialPlanId = attributesMap.get(GscConstants.DIAL_PLAN_LOGIC);
                    ExcelBean book37 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.DIAL_PLAN_LOGIC,
                            getAttributeValue(dialPlanId, orderProductComponentAttributes));
                    book37.setOrder(3);
                    book37.setGscQuoteDetailId(0);
                    listBook.add(book37);

                    Integer callsId = attributesMap.get(GscConstants.CALLS_PER_SECOND);
                    ExcelBean book38 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CALLS_PER_SECOND,
                            getAttributeValue(callsId, orderProductComponentAttributes));
                    book38.setOrder(3);
                    book38.setGscQuoteDetailId(0);
                    listBook.add(book38);

                    Integer certificateId = attributesMap.get(GscConstants.CERTIFICATE_AUTHORITY_SUPPORT);
                    ExcelBean book39 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CERTIFICATE_AUTHORITY_SUPPORT,
                            getAttributeValue(certificateId, orderProductComponentAttributes));
                    book39.setOrder(3);
                    book39.setGscQuoteDetailId(0);
                    listBook.add(book39);

                    Integer fqdnId = attributesMap.get(GscConstants.FQDN);
                    ExcelBean book40 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.FQDN,
                            getAttributeValue(fqdnId, orderProductComponentAttributes));
                    book40.setOrder(3);
                    book40.setGscQuoteDetailId(0);
                    listBook.add(book40);

                    Integer adInfoId = attributesMap.get(GscConstants.ADDITIONAL_INFORMATION);
                    ExcelBean book43 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.ADDITIONAL_INFORMATION,
                            getAttributeValue(adInfoId, orderProductComponentAttributes));
                    book43.setOrder(3);
                    book43.setGscQuoteDetailId(0);
                    listBook.add(book43);

                    String requiredPortingNumbers = getAttributeValue(attributesMap.get(GscConstants.REQUIRED_PORTING_NUMBERS), orderProductComponentAttributes);
                    if (StringUtils.isNoneBlank(requiredPortingNumbers) && (!Pattern.compile("^[,]").matcher(requiredPortingNumbers).find())) {
                        ExcelBean portingBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.REQUIRED_PORTING_NUMBERS, requiredPortingNumbers);
                        portingBook.setOrder(3);
                        portingBook.setGscQuoteDetailId(0);
                        listBook.add(portingBook);
                    }

                }

                break;
            }
            case GscConstants.MPLS: {

                if(!isGscMultiMacd) {
                    if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && orderToLe.getOrderCategory().equalsIgnoreCase(GscConstants.CHANGING_OUTPULSE)) {
                        Integer oldTermId = attributesMap.get(GscConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE);
                        ExcelBean oldTermNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.OLD_TERMINATION_NUMBER_WORKING_OUTPULSE, getAttributeValue(oldTermId, orderProductComponentAttributes));
                        oldTermNumBook.setOrder(3);
                        oldTermNumBook.setGscQuoteDetailId(0);
                        listBook.add(oldTermNumBook);
                    }

                    //show MACD service attribute
                /*if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD)) {
                    getMACDServiceAttributes(listBook,attributesMap,orderProductComponentAttributes);

                    Integer circuitId = attributesMap.get(GscConstants.CIRCUIT_ID);
                    ExcelBean circuitIdBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.GVPN_CIRCUIT_ID, StringUtils.isEmpty(getAttributeValue(circuitId, orderProductComponentAttributes))? getAttributeValue(circuitId, orderProductComponentAttributes) :"");
                    circuitIdBook.setOrder(3);
                    circuitIdBook.setGscQuoteDetailId(0);
                    listBook.add(circuitIdBook);
                }*/
                    Integer termId = attributesMap.get(GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE);
                    ExcelBean termNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, getAttributeValue(termId, orderProductComponentAttributes));
                    termNumBook.setOrder(3);
                    termNumBook.setGscQuoteDetailId(0);
                    listBook.add(termNumBook);

                    //showing MACD Add country selected outpulse number in LR export
                    getSelectedOutpulseNumber(listBook, attributesMap, orderProductComponentAttributes);

                    getDIDSiteAddress(listBook, attributesMap, orderProductComponentAttributes);

               /* if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && isGscMultiMacd) {
                    getSIPTrunkAttributes(listBook, orderToLe, orderProductComponent.getMstProductComponent().getName());
                }*/

                    Integer requiredId = attributesMap.get(GscConstants.REQUIRED_ON_A_AND_B_NUMBER);
                    ExcelBean book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.REQUIRED_ON_A_AND_B_NUMBER,
                            getAttributeValue(requiredId, orderProductComponentAttributes));
                    book26.setOrder(3);
                    book26.setGscQuoteDetailId(0);
                    listBook.add(book26);

                    Integer dtmfId = attributesMap.get(GscConstants.DTMF_RELAY_SUPPORT);
                    ExcelBean book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.DTMF_RELAY_SUPPORT,
                            getAttributeValue(dtmfId, orderProductComponentAttributes));
                    book27.setOrder(3);
                    book27.setGscQuoteDetailId(0);
                    listBook.add(book27);

                    Integer privacyHeaderId = attributesMap.get(GscConstants.SUPPORTED_SIP_PRIVACY_HEADERS);
                    ExcelBean book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.SUPPORTED_SIP_PRIVACY_HEADERS,
                            getAttributeValue(privacyHeaderId, orderProductComponentAttributes));
                    book28.setOrder(3);
                    book28.setGscQuoteDetailId(0);
                    listBook.add(book28);
                    /* GscConstants.ADDITIONAL_INFORMATION */
                    Integer sessionAliveId = attributesMap.get(GscConstants.SESSION_KEEP_ALIVE_TIMER);
                    ExcelBean book29 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.SESSION_KEEP_ALIVE_TIMER,
                            getAttributeValue(sessionAliveId, orderProductComponentAttributes));
                    book29.setOrder(3);
                    book29.setGscQuoteDetailId(0);
                    listBook.add(book29);

                    Integer prefixAdId = attributesMap.get(GscConstants.PREFIX_ADDITION);
                    ExcelBean book30 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.PREFIX_ADDITION,
                            getAttributeValue(prefixAdId, orderProductComponentAttributes));
                    book30.setOrder(3);
                    book30.setGscQuoteDetailId(0);
                    listBook.add(book30);

                    Integer transportId = attributesMap.get(GscConstants.TRANSPORT_PROTOCOL);
                    ExcelBean book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.TRANSPORT_PROTOCOL,
                            getAttributeValue(transportId, orderProductComponentAttributes));
                    book32.setOrder(3);
                    book32.setGscQuoteDetailId(0);
                    listBook.add(book32);

                    Integer codecId = attributesMap.get(GscConstants.CODEC);
                    ExcelBean book33 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CODEC,
                            getAttributeValue(codecId, orderProductComponentAttributes));
                    book33.setOrder(3);
                    book33.setGscQuoteDetailId(0);
                    listBook.add(book33);

                    Integer channelId = attributesMap.get(GscConstants.NO_OF_CONCURRENT_CHANNEL);
                    ExcelBean book34 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL,
                            getAttributeValue(channelId, orderProductComponentAttributes));
                    book34.setOrder(3);
                    book34.setGscQuoteDetailId(0);
                    listBook.add(book34);

                    Integer equipmentId = attributesMap.get(GscConstants.EQUIPMENT_ADDRESS);
                    ExcelBean book35 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.EQUIPMENT_ADDRESS,
                            getAttributeValue(equipmentId, orderProductComponentAttributes));
                    book35.setOrder(3);
                    book35.setGscQuoteDetailId(0);
                    listBook.add(book35);

                    Integer routingId = attributesMap.get(GscConstants.ROUTING_TOPOLOGY);
                    ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.ROUTING_TOPOLOGY,
                            getAttributeValue(routingId, orderProductComponentAttributes));
                    book36.setOrder(3);
                    book36.setGscQuoteDetailId(0);
                    listBook.add(book36);

                    Integer dialPlanId = attributesMap.get(GscConstants.DIAL_PLAN_LOGIC);
                    ExcelBean book37 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.DIAL_PLAN_LOGIC,
                            getAttributeValue(dialPlanId, orderProductComponentAttributes));
                    book37.setOrder(3);
                    book37.setGscQuoteDetailId(0);
                    listBook.add(book37);

                    Integer callsId = attributesMap.get(GscConstants.CALLS_PER_SECOND);
                    ExcelBean book38 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CALLS_PER_SECOND,
                            getAttributeValue(callsId, orderProductComponentAttributes));
                    book38.setOrder(3);
                    book38.setGscQuoteDetailId(0);
                    listBook.add(book38);

                    Integer certificateId = attributesMap.get(GscConstants.CERTIFICATE_AUTHORITY_SUPPORT);
                    ExcelBean book39 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CERTIFICATE_AUTHORITY_SUPPORT,
                            getAttributeValue(certificateId, orderProductComponentAttributes));
                    book39.setOrder(3);
                    book39.setGscQuoteDetailId(0);
                    listBook.add(book39);

                    Integer ipAddressId = attributesMap.get(GscConstants.IP_ADDRESS_SPACE);
                    ExcelBean book41 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.IP_ADDRESS_SPACE,
                            getAttributeValue(ipAddressId, orderProductComponentAttributes));
                    book41.setOrder(3);
                    book41.setGscQuoteDetailId(0);
                    listBook.add(book41);

                    Integer cusDeviceId = attributesMap.get(GscConstants.CUSTOMER_DEVICE_IP);
                    ExcelBean book42 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.CUSTOMER_DEVICE_IP,
                            getAttributeValue(cusDeviceId, orderProductComponentAttributes));
                    book42.setOrder(3);
                    book42.setGscQuoteDetailId(0);
                    listBook.add(book42);

                    Integer adInfoId = attributesMap.get(GscConstants.ADDITIONAL_INFORMATION);
                    ExcelBean book43 = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.ADDITIONAL_INFORMATION,
                            getAttributeValue(adInfoId, orderProductComponentAttributes));
                    book43.setOrder(3);
                    book43.setGscQuoteDetailId(0);
                    listBook.add(book43);

                    String requiredPortingNumbers = getAttributeValue(attributesMap.get(GscConstants.REQUIRED_PORTING_NUMBERS), orderProductComponentAttributes);
                    if (StringUtils.isNoneBlank(requiredPortingNumbers) && (!Pattern.compile("^[,]").matcher(requiredPortingNumbers).find())) {
                        ExcelBean portingBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                                OrderDetailsExcelDownloadConstants.REQUIRED_PORTING_NUMBERS, requiredPortingNumbers);
                        portingBook.setOrder(3);
                        portingBook.setGscQuoteDetailId(0);
                        listBook.add(portingBook);
                    }
                }

                break;
            }
            case GscConstants.NNI: {
                Integer nniId = attributesMap.get(GscConstants.NNI_ID);
                ExcelBean nniIdBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        GscConstants.NNI_ID, getAttributeValue(nniId, orderProductComponentAttributes));
                nniIdBook.setOrder(3);
                nniIdBook.setGscQuoteDetailId(0);
                listBook.add(nniIdBook);

                Integer termId = attributesMap.get(GscConstants.TERMINATION_NUMBER_WORKING_OUTPULSE);
                ExcelBean termNumBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                        OrderDetailsExcelDownloadConstants.TERMINATION_NUMBER_WORKING_OUTPULSE, getAttributeValue(termId, orderProductComponentAttributes));
                termNumBook.setOrder(3);
                termNumBook.setGscQuoteDetailId(0);
                listBook.add(termNumBook);

                String requiredPortingNumbers = getAttributeValue(attributesMap.get(GscConstants.REQUIRED_PORTING_NUMBERS), orderProductComponentAttributes);
                if (StringUtils.isNoneBlank(requiredPortingNumbers) && (!Pattern.compile("^[,]").matcher(requiredPortingNumbers).find())) {
                    ExcelBean portingBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                            OrderDetailsExcelDownloadConstants.REQUIRED_PORTING_NUMBERS, requiredPortingNumbers);
                    portingBook.setOrder(3);
                    portingBook.setGscQuoteDetailId(0);
                    listBook.add(portingBook);
                }

                break;
            }
            default:

                LOGGER.info("Invalid master product component Name");

        }

    }

    private String appendTerminationCountryISDCode(Integer id, Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes, Map<String, Integer> attributesMap) {
        String[] terminationNumbersArr = getAttributeValue(id, orderProductComponentAttributes).split(CommonConstants.COMMA);
        List<String> terminationNumbers = Arrays.stream(terminationNumbersArr).map(terminationNumber -> getAttributeValue(attributesMap.get(GscConstants.TERMINATION_NUMBER_ISD_CODE),
                orderProductComponentAttributes) + terminationNumber).collect(Collectors.toList());

        return String.join(CommonConstants.COMMA, terminationNumbers);
    }

    /**
     * Method to concat values
     *
     * @param attributes
     * @return
     */
    private String concatValues(List<OrderProductComponentsAttributeValue> attributes) {
        StringBuilder values = new StringBuilder();
        if (attributes.size() == 1) {
            Optional<OrderProductComponentsAttributeValue> attribute = attributes.stream().findFirst();
            values.append(attribute.get().getAttributeValues());
        } else {
            attributes.stream().forEach(attribute -> {
                values.append(attribute.getAttributeValues()).append(CommonConstants.COMMA);
            });
        }

        String value = values.toString();
        if (value.endsWith(",")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    /**
     * Method to get attributes map
     *
     * @param productAttributes
     * @return
     */
    private Map<String, Integer> getAttributesMap(List<ProductAttributeMaster> productAttributes) {
        Map<String, Integer> attributesMap = new HashMap<>();
        productAttributes.stream().forEach(attribute -> {
            attributesMap.put(attribute.getName(), attribute.getId());
        });
        return attributesMap;
    }

    /**
     * Method to get component attributes based on specific attribute
     *
     * @param attributeId
     * @param componentAttributeList
     * @return
     */
    private List<OrderProductComponentsAttributeValue> getComponentAttributesBasedOnSpecificAttribute(
            Integer attributeId, Set<OrderProductComponentsAttributeValue> componentAttributeList) {
        List<OrderProductComponentsAttributeValue> attributeValuesList = new ArrayList<>();
        componentAttributeList.stream().forEach(componentAttribute -> {
            if (componentAttribute.getProductAttributeMaster().getId() == attributeId) {
                attributeValuesList.add(componentAttribute);
            }
        });
        return attributeValuesList;
    }

    /**
     * Method to get attribute Value
     *
     * @param attributeId
     * @return
     */
    private String getAttributeValue(Integer attributeId,
                                     Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes) {
        String value = "";
        if (Objects.nonNull(orderProductComponentAttributes) && Objects.nonNull(attributeId)) {
            List<OrderProductComponentsAttributeValue> attributeValues = getComponentAttributesBasedOnSpecificAttribute(
                    attributeId, orderProductComponentAttributes);

            if (!attributeValues.isEmpty()) {
                value = concatValues(attributeValues);
            }
        }
        return value;
    }

    /**
     * writeBook - writes data into excel
     *
     * @param aBook
     * @param row
     * @throws TclCommonException
     */
    public void writeBook(ExcelBean aBook, Row row) throws TclCommonException {
        if (Objects.isNull(aBook) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
        Cell cell = row.createCell(1);
        if (aBook.getGscQuoteId() != null) {
            if (aBook.getGscQuoteId() == 0) {
                cell.setCellValue(aBook.getLrSection());
            } else if (aBook.getGscQuoteId() != 0) {
                cell.setCellValue(aBook.getLrSection() + ":" + aBook.getGscQuoteId());
            }

        } else {
            if (aBook.getGscQuoteDetailId() != null) {
                if (aBook.getGscQuoteDetailId() == 0) {
                    cell.setCellValue(aBook.getLrSection());
                } else if (aBook.getGscQuoteDetailId() != 0) {
                    cell.setCellValue(aBook.getLrSection() + ":" + aBook.getGscQuoteDetailId());
                }
            }
        }
        cell = row.createCell(2);
        cell.setCellValue(aBook.getAttributeName());

        cell = row.createCell(3);
        cell.setCellValue(aBook.getAttributeValue());

    }

    /**
     * getLinks - retrieves gscQuotes under an orderToLe
     *
     * @param orderToLe
     * @param version
     * @return
     */
    private List<OrderGsc> getGscQuotes(OrderToLe orderToLe) {

        List<OrderGsc> orderGscList = new ArrayList<>();
        MstProductFamily mstProductFamily = mstProductFamilyRepository
                .findByNameAndStatus(OrderDetailsExcelDownloadConstants.GSIP, (byte) 1);
        if (mstProductFamily != null) {
            OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
                    .findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
            List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
                    .findByOrderToLeProductFamily(orderToLeProductFamily);

            orderProductSolutionList.stream().forEach(orderProductSolution -> {
                orderGscList
                        .addAll(orderGscRepository.findByorderProductSolutionAndStatus(orderProductSolution, (byte) 1));
            });
        }

        return orderGscList;
    }

    /**
     * getAttributeValues - retrieves attribute values for an OrderToLe
     *
     * @param orderToLe
     * @return
     * @throws TclCommonException
     */
    public Map<String, String> getAttributeValues(OrderToLe orderToLe) throws TclCommonException {
        Map<String, String> attributeMap = new HashMap<>();
        try {

            Objects.requireNonNull(orderToLe, ORDER_NULL_MESSAGE);

            orderToLe.getOrdersLeAttributeValues().forEach(attr -> {
                attributeMap.put(attr.getMstOmsAttribute().getName(), attr.getAttributeValue());

            });

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
        }
        return attributeMap;
    }

    /**
     * addEndCustomerAttributeValues - add end customer details
     *
     * @param order
     * @param attributeValues
     * @return
     * @throws TclCommonException
     */
    public Map<String, String> addEndCustomerAttributeValues(Order order, Map<String, String> attributeValues) throws TclCommonException {
        try {
            Opportunity opportunity= opportunityRepository.findByUuid(order.getOrderCode());
            if(Objects.nonNull(opportunity) && Objects.nonNull(opportunity.getTempCustomerLeId())) {
                LOGGER.info("Temp end customer  : {}", opportunity.getTempCustomerLeId());
                Optional<PartnerTempCustomerDetails> partnerTempCustomerDetails = partnerTempCustomerDetailsRepository.findById(opportunity.getTempCustomerLeId());
                if (partnerTempCustomerDetails.isPresent()) {
                    attributeValues.put(PARTNER_END_CUSTOMER_NAME, Objects.nonNull(
                            partnerTempCustomerDetails.get().getCustomerName()) ? partnerTempCustomerDetails.get().getCustomerName() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_ADDRESS, Objects.nonNull(
                            partnerTempCustomerDetails.get().getStreet()) ? partnerTempCustomerDetails.get().getStreet() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_CITY, Objects.nonNull(
                            partnerTempCustomerDetails.get().getCity()) ? partnerTempCustomerDetails.get().getCity() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_STATE, Objects.nonNull(
                            partnerTempCustomerDetails.get().getState()) ? partnerTempCustomerDetails.get().getState() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_COUNTRY, Objects.nonNull(
                            partnerTempCustomerDetails.get().getCountry()) ? partnerTempCustomerDetails.get().getCountry() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_POSTAL_CODE, Objects.nonNull(
                            partnerTempCustomerDetails.get().getPostalCode()) ? partnerTempCustomerDetails.get().getPostalCode() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_WEBSITE, Objects.nonNull(
                            partnerTempCustomerDetails.get().getCustomerWebsite()) ? partnerTempCustomerDetails.get().getCustomerWebsite() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_CONTACT_EMAIL, Objects.nonNull(
                            partnerTempCustomerDetails.get().getCustomerContactEmail()) ? partnerTempCustomerDetails.get().getCustomerContactEmail() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_CONTACT_NAME, Objects.nonNull(
                            partnerTempCustomerDetails.get().getCustomerContactName()) ? partnerTempCustomerDetails.get().getCustomerContactName() : "NA");
                }
            } else if (Objects.nonNull(opportunity) && Objects.isNull(opportunity.getTempCustomerLeId())){
                String endCustomerCuid = opportunity.getEndCustomerCuid();
                LOGGER.info("Queue call to get end customer details by endCustomerCuid : {}", endCustomerCuid);
                String response = (String) mqUtils.sendAndReceive(partnerEndCustomerMappingQueue, endCustomerCuid);
                if (Objects.nonNull(response)) {
                    PartnerEndCustomerMappingBean partnerEndCustomerMappingBean = Utils.fromJson(response, PartnerEndCustomerMappingBean.class);
                    attributeValues.put(PARTNER_END_CUSTOMER_NAME, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerLeName())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerLeName() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_ADDRESS, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerAddress())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerAddress() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_CITY, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerLeCity())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerLeCity() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_STATE, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerLeState())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerLeState() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_COUNTRY, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerLeCountry())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerLeCountry() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_POSTAL_CODE, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerLeZip())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerLeZip() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_WEBSITE, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerLeWebsite())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerLeWebsite() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_CONTACT_EMAIL, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerContactEmail())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerContactEmail() : "NA");
                    attributeValues.put(PARTNER_END_CUSTOMER_CONTACT_NAME, Objects.nonNull(partnerEndCustomerMappingBean.getPartnerCustomerContactName())
                            ? partnerEndCustomerMappingBean.getPartnerCustomerContactName() : "NA");
                }
            }
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
        }
        return attributeValues;
    }

    /**
     * Method to construct Customer Location Details
     *
     * @param context
     * @param quoteLeAttrbutes
     * @throws IllegalArgumentException
     * @throws TclCommonException
     */
    private String getCustomerAddress(String quoteLeAttributeCustomerContactEntity) throws TclCommonException, IllegalArgumentException {
        String customerAddress = null;
        LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, quoteLeAttributeCustomerContactEntity);
        if (Objects.nonNull(locationResponse) && StringUtils.isNotBlank(locationResponse)) {
            try {
                com.tcl.dias.oms.beans.AddressDetail addressDetail = (com.tcl.dias.oms.beans.AddressDetail) Utils.convertJsonToObject(locationResponse, com.tcl.dias.oms.beans.AddressDetail.class);
                if (Objects.nonNull(addressDetail)) {
                    addressDetail=validateAddressDetail(addressDetail);
                    customerAddress = addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo()+" "+addressDetail.getLocality();
                }
            } catch (Exception e) {
                throw new TCLException("", e.getMessage());
            }
        }
        LOGGER.info("Customer address is {}", customerAddress);
        return  customerAddress;
    }

    /**
     * Method to validate addressdetail
     * @param addressDetail
     * @return
     */
    private com.tcl.dias.oms.beans.AddressDetail validateAddressDetail(com.tcl.dias.oms.beans.AddressDetail addressDetail) {
        LOGGER.info("validating address detail");
        if(Objects.isNull(addressDetail.getAddressLineOne()))
            addressDetail.setAddressLineOne("");
        if(Objects.isNull(addressDetail.getAddressLineTwo()))
            addressDetail.setAddressLineTwo("");
        if(Objects.isNull(addressDetail.getCity()))
            addressDetail.setCity("");
        if(Objects.isNull(addressDetail.getCountry()))
            addressDetail.setCountry("");
        if(Objects.isNull(addressDetail.getPincode()))
            addressDetail.setPincode("");
        if(Objects.isNull(addressDetail.getLocality()))
            addressDetail.setLocality("");
        if(Objects.isNull(addressDetail.getState()))
            addressDetail.setState("");
        return addressDetail;
    }
    /**
     * createOrderDetails - extracts order related details as excel bean
     * @param listBook
     * @param attributeValues
     * @param isIntl
     * @param isGscMultiMacd
     */
    private void createOrderDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
                                    OrderToLe orderToLe, boolean isIntl, boolean isGscMultiMacd) throws TclCommonException {
        ExcelBean orderDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.ORDER_REF_ID, orderToLe.getOrder().getOrderCode());
        orderDetails.setOrder(1);
        orderDetails.setGscQuoteId(0);
        listBook.add(orderDetails);

//        if (orderToLe.getOrderType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD) && !isGscMultiMacd) {
            ExcelBean orderSecIdDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.ORDER_SECS_ID, attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUSTOMER_SECS_ID)
                    ? attributeValues.get(OrderDetailsExcelDownloadConstants.CUSTOMER_SECS_ID)
                    : OrderDetailsExcelDownloadConstants.NOT_AVAILABLE);
            orderSecIdDetails.setOrder(1);
            orderSecIdDetails.setGscQuoteId(0);
            listBook.add(orderSecIdDetails);
//        }
//        else {
//        	ExcelBean orderSecIdDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
//                    OrderDetailsExcelDownloadConstants.ORDER_SEC_ID, attributeValues.containsKey(OrderDetailsExcelDownloadConstants.ORG_NO)
//                    ? attributeValues.get(OrderDetailsExcelDownloadConstants.ORG_NO)
//                    : OrderDetailsExcelDownloadConstants.NOT_AVAILABLE);
//            orderSecIdDetails.setOrder(1);
//            orderSecIdDetails.setGscQuoteId(0);
//            listBook.add(orderSecIdDetails);
//        }

        ExcelBean supplierInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.GSC_SUPPLIER_NAME,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.GSC_SUPPLIER_NAME)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.GSC_SUPPLIER_NAME)
                        : OrderDetailsExcelDownloadConstants.TCL);
        supplierInfo.setOrder(1);
        supplierInfo.setGscQuoteId(0);
        listBook.add(supplierInfo);

        ExcelBean ownerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.Le_Owner,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.Le_Owner)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.Le_Owner)
                        : OrderDetailsExcelDownloadConstants.NOT_APPLICABLE);
        ownerInfo.setOrder(1);
        ownerInfo.setGscQuoteId(0);
        listBook.add(ownerInfo);
        if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) && (PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())
                || (PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())))) {
            List<PartnerLegalEntityBean> partnerLegalEntites = partnerService.getPartnerLegalEntiy(Integer.valueOf(orderToLe.getOrder().getEngagementOptyId()));
            ExcelBean partnerLeName = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.PARTNER_LE_NAME,
                    partnerLegalEntites.isEmpty()
                            ? ""
                            : partnerLegalEntites.stream().findFirst().get().getEntityName());
            partnerLeName.setOrder(1);
            partnerLeName.setGscQuoteId(0);
            listBook.add(partnerLeName);

            ExcelBean partnerCuid = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.PARTNER_CUID,
                    partnerLegalEntites.isEmpty()
                            ? ""
                            : partnerLegalEntites.stream().findFirst().get().getTpsSfdcCuid());
            partnerCuid.setOrder(1);
            partnerCuid.setGscQuoteId(0);
            listBook.add(partnerCuid);
            /*If GSC having Sell With scenario in future this if block will work */
            if (PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
                CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = partnerService.getCustomerLeDetailsMQCall(String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));

                ExcelBean customerLeName = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                        OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
                        customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getLegalEntityName());
                customerLeName.setOrder(1);
                customerLeName.setGscQuoteId(0);
                listBook.add(customerLeName);

                ExcelBean customerCuid = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                        OrderDetailsExcelDownloadConstants.CUID,
                        customerLegalEntityDetailsBean.getCustomerLeDetails().isEmpty()
                                ? ""
                                : customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getSfdcId());
                customerCuid.setOrder(1);
                customerCuid.setGscQuoteId(0);
                listBook.add(customerCuid);
            }else{
                EngagementToOpportunity engagementToOpportunity = partnerService.getEngagementToOpportunity(Integer.valueOf(orderToLe.getOrder().getEngagementOptyId()));
                ExcelBean customerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                        OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
                        engagementToOpportunity.getOpportunity().getCustomerLeName());
                customerInfo.setOrder(1);
                customerInfo.setGscQuoteId(0);
                listBook.add(customerInfo);
            }

            }else {

            ExcelBean customerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
                    attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_NAME)
                            ? attributeValues.get(OrderDetailsExcelDownloadConstants.LE_NAME)
                            : OrderDetailsExcelDownloadConstants.NOT_APPLICABLE);
            customerInfo.setOrder(1);
            customerInfo.setGscQuoteId(0);
            listBook.add(customerInfo);

            ExcelBean cuId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.CUID,
                    attributeValues.getOrDefault(OrderDetailsExcelDownloadConstants.CUID_VALUE, CommonConstants.EMPTY));
            cuId.setOrder(1);
            cuId.setGscQuoteId(0);
            listBook.add(cuId);
        }

        ExcelBean orderType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.ORDER_TYPE, orderToLe.getOrderType());
        orderType.setOrder(1);
        orderType.setGscQuoteId(0);
        listBook.add(orderType);
        if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) &&
                PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
            ExcelBean billingAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.PARTNER_CONTRACTING_BILLING_ADDRESS,
                    attributeValues.containsKey("Customer Contracting Entity") ? getCustomerAddress(attributeValues.get("Customer Contracting Entity")) : "NA");
            billingAddress.setOrder(1);
            billingAddress.setGscQuoteId(0);
            listBook.add(billingAddress);
        }else {
            ExcelBean billingAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.CUSTOMER_CONTRACTING_BILLING_ADDRESS,
                    attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
                            ? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
                            : "NA");
            billingAddress.setOrder(1);
            billingAddress.setGscQuoteId(0);
            listBook.add(billingAddress);
        }

        ExcelBean paymentCurrency = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY, orderToLe.getCurrencyCode());
        paymentCurrency.setOrder(1);
        paymentCurrency.setGscQuoteId(0);
        listBook.add(paymentCurrency);

        ExcelBean paymentMethod = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.PAYMENT_METHOD,
                attributeValues.get(OrderDetailsExcelDownloadConstants.INVOICE_METHOD));
        paymentMethod.setOrder(1);
        paymentMethod.setGscQuoteId(0);
        listBook.add(paymentMethod);

        ExcelBean paymentOptions = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.PAYMENT_OPTIONS,
                attributeValues.getOrDefault(OrderDetailsExcelDownloadConstants.PAYMENT_OPTIONS, OrderDetailsExcelDownloadConstants.NA));
        paymentOptions.setOrder(1);
        paymentOptions.setGscQuoteId(0);
        listBook.add(paymentOptions);

        ExcelBean paymentTerm = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.PAYMENT_TERM_VALUE,
                attributeValues.getOrDefault(OrderDetailsExcelDownloadConstants.PAYMENT_OPTIONS, OrderDetailsExcelDownloadConstants.NA));
        paymentTerm.setOrder(1);
        paymentTerm.setGscQuoteId(0);
        listBook.add(paymentTerm);

        ExcelBean autoCofNumber = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.AUTO_COF_NUMBER, orderToLe.getOrder().getOrderCode());
        autoCofNumber.setOrder(1);
        autoCofNumber.setGscQuoteId(0);
        listBook.add(autoCofNumber);

        ExcelBean sfdc = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.SFDC_ID, orderToLe.getTpsSfdcCopfId());
        sfdc.setOrder(1);
        sfdc.setGscQuoteId(0);
        listBook.add(sfdc);

        if(isIntl == false){
            ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.PROGRAM_MANAGER, "");
            book5.setOrder(1);
            book5.setGscQuoteId(0);
            listBook.add(book5);
        }

        Integer months = 0;
        //Since old orders had saved as 1 Year Contract term
        if(orderToLe.getTermInMonths().contains("Year")){
            months=Integer.valueOf(orderToLe.getTermInMonths()
                    .replace("Year", "")
                    .trim()) * 12;
        }else{
            months=Integer.valueOf(orderToLe.getTermInMonths()
                    .replace("months", "")
                    .trim());
        }

        ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.CONTRACT_TERMS, months +" months");
        book6.setOrder(1);
        book6.setGscQuoteId(0);
        listBook.add(book6);
        if(Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) && Objects.nonNull(orderToLe.getClassification())) {
            ExcelBean book8 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.OPPORTUNITY_CLASSIFICATION,
                    orderToLe.getClassification());
            book8.setOrder(1);
            book8.setGscQuoteId(0);
            listBook.add(book8);
        }else {

            ExcelBean book8 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.OPPORTUNITY_CLASSIFICATION,
                    OrderDetailsExcelDownloadConstants.SELL_TO);
            book8.setOrder(1);
            book8.setGscQuoteId(0);
            listBook.add(book8);
        }
        StringBuilder gstDetail = new StringBuilder();

        String gstAddress = "";
        String gstNo = "";


        if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_STATE_GST_NUMBER)) {
            gstNo= attributeValues.get(OrderDetailsExcelDownloadConstants.LE_STATE_GST_NUMBER);

        }else if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.GST_NUM)) {
            gstNo= attributeValues.get(OrderDetailsExcelDownloadConstants.GST_NUM);

        }
        if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_STATE_GST_ADDRESS)) {
            gstAddress= attributeValues.get(OrderDetailsExcelDownloadConstants.LE_STATE_GST_ADDRESS);
        }else if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.GST_ADDR)) {
            gstAddress= attributeValues.get(OrderDetailsExcelDownloadConstants.GST_ADDR);
        }

        if (gstDetail == null || gstDetail.toString().equalsIgnoreCase(CommonConstants.EMPTY)) {
            gstDetail.append(Objects.nonNull(gstNo) ? gstNo : CommonConstants.EMPTY);
            gstDetail.append(",")
                    .append(Objects.nonNull(gstAddress) ? gstAddress : "NA");
        } else {
            if (gstDetail.toString().endsWith(CommonConstants.RIGHT_SLASH)) {
                gstDetail.setLength(gstDetail.length() - 1);
            }
        }

        if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) &&
                PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
            ExcelBean book9 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.PARTNER_LE_GST,
                    gstDetail.toString().matches("^[a-zA-Z0-9]+$")?gstDetail.toString():CommonConstants.EMPTY);
            book9.setOrder(1);
            book9.setGscQuoteId(0);
            listBook.add(book9);
        }else {

            ExcelBean book9 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                    OrderDetailsExcelDownloadConstants.CUSTOMER_LE_GST,
                    gstDetail.toString().matches("^[a-zA-Z0-9]+$")?gstDetail.toString():CommonConstants.EMPTY);
            book9.setOrder(1);
            book9.setGscQuoteId(0);
            listBook.add(book9);
        }

        ExcelBean book10 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER)
                        : "");
        book10.setOrder(1);
        book10.setGscQuoteId(0);
        listBook.add(book10);

        ExcelBean book12 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.CREDIT_LIMIT,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CREDIT_LIMIT)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.CREDIT_LIMIT)
                        : "NA");
        book12.setOrder(1);
        book12.setGscQuoteId(0);
        listBook.add(book12);

        ExcelBean book13 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT)
                        : "NA");
        book13.setOrder(1);
        book13.setGscQuoteId(0);
        listBook.add(book13);

        ExcelBean book14 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.BILLING_RATIO,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.MST_BILLING_RATIO)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.MST_BILLING_RATIO)
                        : "NA");
        book14.setOrder(1);
        book14.setGscQuoteId(0);
        listBook.add(book14);

        ExcelBean book15 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.PO_NUMBER,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PO_NUMBER_KEY)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.PO_NUMBER_KEY)
                        : "NA");
        book15.setOrder(1);
        book15.setGscQuoteId(0);
        listBook.add(book15);

        ExcelBean book16 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.PO_DATE, attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PO_DATE_KEY)
                ? attributeValues.get(OrderDetailsExcelDownloadConstants.PO_DATE_KEY)
                : "NA");
        book16.setOrder(1);
        book16.setGscQuoteId(0);
        listBook.add(book16);

        ExcelBean book17 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.DEPARTMENT_BILLING,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.DEPARTMENT_BILLING)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.DEPARTMENT_BILLING)
                        : CommonConstants.NO);
        book17.setOrder(1);
        book17.setGscQuoteId(0);
        listBook.add(book17);

        ExcelBean book18 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                OrderDetailsExcelDownloadConstants.DEPARTMENT_NAME,
                attributeValues.containsKey(OrderDetailsExcelDownloadConstants.DEPARTMENT_NAME)
                        ? attributeValues.get(OrderDetailsExcelDownloadConstants.DEPARTMENT_NAME)
                        : "NA");
        book18.setOrder(1);
        book18.setGscQuoteId(0);
        listBook.add(book18);

        //End Customer Details
        ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_NAME, attributeValues.containsKey(PARTNER_END_CUSTOMER_NAME) ?
                        attributeValues.get(PARTNER_END_CUSTOMER_NAME) : "NA");
        book19.setOrder(1);
        book19.setGscQuoteId(0);
        listBook.add(book19);

        ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_ADDRESS, attributeValues.containsKey(PARTNER_END_CUSTOMER_ADDRESS) ?
                attributeValues.get(PARTNER_END_CUSTOMER_ADDRESS) : "NA");
        book20.setOrder(1);
        book20.setGscQuoteId(0);
        listBook.add(book20);

        ExcelBean book21 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_CITY, attributeValues.containsKey(PARTNER_END_CUSTOMER_CITY)
                        ? attributeValues.get(PARTNER_END_CUSTOMER_CITY) : "NA");
        book21.setOrder(1);
        book21.setGscQuoteId(0);
        listBook.add(book21);

        ExcelBean book22 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_STATE, attributeValues.containsKey(PARTNER_END_CUSTOMER_STATE)
                ? attributeValues.get(PARTNER_END_CUSTOMER_STATE) : "NA");
        book22.setOrder(1);
        book22.setGscQuoteId(0);
        listBook.add(book22);

        ExcelBean book23 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_COUNTRY, attributeValues.containsKey(PARTNER_END_CUSTOMER_COUNTRY)
                ? attributeValues.get(PARTNER_END_CUSTOMER_COUNTRY) : "NA");
        book23.setOrder(1);
        book23.setGscQuoteId(0);
        listBook.add(book23);

        ExcelBean book24 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_POSTAL_CODE, attributeValues.containsKey(PARTNER_END_CUSTOMER_POSTAL_CODE)
                ? attributeValues.get(PARTNER_END_CUSTOMER_POSTAL_CODE) : "NA");
        book24.setOrder(1);
        book24.setGscQuoteId(0);
        listBook.add(book24);

        // Commented as it is redundant and that data of no value - Ref OD-1914
        /*ExcelBean book25 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_CONTACT_NAME, attributeValues.containsKey(PARTNER_END_CUSTOMER_CONTACT_NAME)
                ? attributeValues.get(PARTNER_END_CUSTOMER_CONTACT_NAME) : "NA");
        book25.setOrder(1);
        book25.setGscQuoteId(0);
        listBook.add(book25);

        ExcelBean book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_CONTACT_EMAIL, attributeValues.containsKey(PARTNER_END_CUSTOMER_CONTACT_EMAIL)
                ? attributeValues.get(PARTNER_END_CUSTOMER_CONTACT_EMAIL) : "NA");
        book26.setOrder(1);
        book26.setGscQuoteId(0);
        listBook.add(book26);

        ExcelBean book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
                PARTNER_END_CUSTOMER_WEBSITE, attributeValues.containsKey(PARTNER_END_CUSTOMER_WEBSITE)
                ? attributeValues.get(PARTNER_END_CUSTOMER_WEBSITE) : "NA");
        book27.setOrder(1);
        book27.setGscQuoteId(0);
        listBook.add(book27);*/

    }

    /**
     * Method to get list of sites
     *
     * @param orderToLe
     * @param version
     * @return
     */
    public List<OrderIllSite> getSites(OrderToLe orderToLe) {

        List<OrderIllSite> orderIllSitesList = new ArrayList<>();
        MstProductFamily mstProductFamily = mstProductFamilyRepository
                .findByNameAndStatus(OrderDetailsExcelDownloadConstants.GVPN, (byte) 1);
        if (mstProductFamily != null) {
            OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
                    .findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
            List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
                    .findByOrderToLeProductFamily(orderToLeProductFamily);

            orderProductSolutionList.stream().forEach(orderProductSolution -> orderIllSitesList.addAll(
                    orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution, (byte) 1)));
        }

        return orderIllSitesList;
    }

    /**
     * Method to create site details based on feasibility
     *
     * @param orderSiteFeasibility
     * @param listBook
     * @param site
     * @param feasible
     * @throws TclCommonException
     */
    private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
                                                     List<ExcelBean> listBook, OrderIllSite site, CustomFeasibilityRequest feasible,
                                                     Map<String, String> dependentAttributesOfGVPN) throws TclCommonException {
        ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID, orderSiteFeasibility.getFeasibilityCode());
        book19.setOrder(4);
        book19.setGscQuoteId(site.getId());
        listBook.add(book19);
        ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID, orderSiteFeasibility.getFeasibilityCode());
        book20.setOrder(4);
        book20.setGscQuoteId(site.getId());
        listBook.add(book20);

        ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT, "");
        book3.setOrder(4);
        book3.setGscQuoteId(site.getId());
        listBook.add(book3);

        ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.LM_TYPE,
                feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
        book4.setOrder(4);
        book4.setGscQuoteId(site.getId());
        listBook.add(book4);
        dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

        ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
                orderSiteFeasibility.getSfdcFeasibilityId() == null ? "" : orderSiteFeasibility.getSfdcFeasibilityId());
        book5.setOrder(4);
        book5.setGscQuoteId(site.getId());
        listBook.add(book5);
    }

    /**
     * Method to create default site details
     *
     * @param listBook
     * @param site
     */
    private void createDefaultSiteDetails(List<ExcelBean> listBook, OrderIllSite site) {
        ExcelBean book17 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.BILLING_CATEGORY, OrderDetailsExcelDownloadConstants.BILLABLE);
        book17.setOrder(4);
        book17.setGscQuoteId(site.getId());
        listBook.add(book17);

        ExcelBean rfsDate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.REQUESTED_DATE,
                site.getRequestorDate() != null ? DateUtil.convertDateToSlashString(site.getRequestorDate()) : "NA");
        rfsDate.setOrder(4);
        rfsDate.setGscQuoteId(site.getId());
        listBook.add(rfsDate);

        ExcelBean layerMedium = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.LAYER_MEDIUM, "Layer 3");
        layerMedium.setOrder(4);
        layerMedium.setGscQuoteId(site.getId());
        listBook.add(layerMedium);

        ExcelBean unfr = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.UNIFR, OrderDetailsExcelDownloadConstants.NO);
        unfr.setOrder(4);
        unfr.setGscQuoteId(site.getId());
        listBook.add(unfr);

        ExcelBean reporting = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.REPORTING, "Standard Reporting");
        reporting.setOrder(4);
        reporting.setGscQuoteId(site.getId());
        listBook.add(reporting);

        ExcelBean gvpnType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.GVPNTYPE, "Fixed");
        gvpnType.setOrder(4);
        gvpnType.setGscQuoteId(site.getId());
        listBook.add(gvpnType);

        ExcelBean izoPrivate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.IZOPrivate, OrderDetailsExcelDownloadConstants.NO);
        izoPrivate.setOrder(4);
        izoPrivate.setGscQuoteId(site.getId());
        listBook.add(izoPrivate);

        ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.LINKTYPE, "Customer Facing");
        linkType.setOrder(4);
        linkType.setGscQuoteId(site.getId());
        listBook.add(linkType);

        ExcelBean gvpnFlavour = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.GVPN_FLAVOR, "GVPN");
        gvpnFlavour.setOrder(4);
        gvpnFlavour.setGscQuoteId(site.getId());
        listBook.add(gvpnFlavour);

    }

    /**
     * Method to create site details based on non feasible sites
     *
     * @param orderSiteFeasibility
     * @param listBook
     * @param site
     * @param feasible
     */
    private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
                                                     List<ExcelBean> listBook, OrderIllSite site, NotFeasible feasible, Map<String, String> dependentAttributesOfGVPN) {
        if (orderSiteFeasibility != null) {
            ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book19.setOrder(4);
            book19.setGscQuoteId(site.getId());
            listBook.add(book19);

            ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book20.setOrder(4);
            book20.setGscQuoteId(site.getId());
            listBook.add(book20);
        }
        ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
                feasible == null ? "" : feasible.getCustomerSegment());
        book3.setOrder(4);
        book3.setGscQuoteId(site.getId());
        listBook.add(book3);

        if (orderSiteFeasibility != null) {
            ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LM_TYPE,
                    feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
            book4.setOrder(4);
            book4.setGscQuoteId(site.getId());
            listBook.add(book4);
            dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

            ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
                    orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
                            : orderSiteFeasibility.getSfdcFeasibilityId());
            book5.setOrder(4);
            book5.setGscQuoteId(site.getId());
            listBook.add(book5);

            ExcelBean siteCode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SITE_CODE,
                    Optional.ofNullable(orderSiteFeasibility.getOrderIllSite().getSiteCode()).orElse(CommonConstants.EMPTY));
            siteCode.setOrder(4);
            siteCode.setGscQuoteId(site.getId());
            listBook.add(siteCode);

            Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
            Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

            LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);

            ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LEAD_TIME,
                    lead.toString());
            leadTime.setOrder(4);
            leadTime.setGscQuoteId(site.getId());
            listBook.add(leadTime);
        }
    }

    /**
     * create Intl site details based on feasibility
     *
     * @param orderSiteFeasibility
     * @param listBook
     * @param site
     * @param feasible
     * @param dependentAttributesOfGVPN
     */
    private void createIntlSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
                                                     List<ExcelBean> listBook, OrderIllSite site, IntlNotFeasible feasible, Map<String, String> dependentAttributesOfGVPN) {
        if (orderSiteFeasibility != null) {
            ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book19.setOrder(4);
            book19.setGscQuoteId(site.getId());
            listBook.add(book19);

            ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book20.setOrder(4);
            book20.setGscQuoteId(site.getId());
            listBook.add(book20);
        }
        ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
                feasible == null ? "" : feasible.getCustomerSegment());
        book3.setOrder(4);
        book3.setGscQuoteId(site.getId());
        listBook.add(book3);

        if (orderSiteFeasibility != null) {
            ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LM_TYPE,
                    feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
            book4.setOrder(4);
            book4.setGscQuoteId(site.getId());
            listBook.add(book4);
            dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

            ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
                    orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
                            : orderSiteFeasibility.getSfdcFeasibilityId());
            book5.setOrder(4);
            book5.setGscQuoteId(site.getId());
            listBook.add(book5);

            ExcelBean siteCode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SITE_CODE,
                    Optional.ofNullable(orderSiteFeasibility.getOrderIllSite().getSiteCode()).orElse(CommonConstants.EMPTY));
            siteCode.setOrder(4);
            siteCode.setGscQuoteId(site.getId());
            listBook.add(siteCode);

            Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
            Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

            LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);

            ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LEAD_TIME,
                    lead.toString());
            leadTime.setOrder(4);
            leadTime.setGscQuoteId(site.getId());
            listBook.add(leadTime);
        }
    }

    /**
     * Method to create site details based on feasible sites
     *
     * @param orderSiteFeasibility
     * @param listBook
     * @param site
     * @param feasible
     */
    private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
                                                     List<ExcelBean> listBook, OrderIllSite site, Feasible feasible,
                                                     Map<String, String> dependentAttributesOfGVPN) {
        if (orderSiteFeasibility != null) {
            ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book19.setOrder(4);
            book19.setGscQuoteId(site.getId());
            listBook.add(book19);

            ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book20.setOrder(4);
            book20.setGscQuoteId(site.getId());
            listBook.add(book20);
        }
        ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
                feasible == null ? "" : feasible.getCustomerSegment());
        book3.setOrder(4);
        book3.setGscQuoteId(site.getId());
        listBook.add(book3);

        if (orderSiteFeasibility != null) {
            ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LM_TYPE,
                    feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
            book4.setOrder(4);
            book4.setGscQuoteId(site.getId());
            listBook.add(book4);
            dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

            ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
                    orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
                            : orderSiteFeasibility.getSfdcFeasibilityId());
            book5.setOrder(4);
            book5.setGscQuoteId(site.getId());
            listBook.add(book5);

            ExcelBean siteCode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SITE_CODE,
                    Optional.ofNullable(orderSiteFeasibility.getOrderIllSite().getSiteCode()).orElse(CommonConstants.EMPTY));
            siteCode.setOrder(4);
            siteCode.setGscQuoteId(site.getId());
            listBook.add(siteCode);

            Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
            Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

            LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);

            ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LEAD_TIME,
                    lead.toString());
            leadTime.setOrder(4);
            leadTime.setGscQuoteId(site.getId());
            listBook.add(leadTime);
        }
    }

    /**
     * create intl site details based on feasibility
     *
     * @param orderSiteFeasibility
     * @param listBook
     * @param site
     * @param feasible
     * @param dependentAttributesOfGVPN
     */
    private void createIntlSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
                                                     List<ExcelBean> listBook, OrderIllSite site, IntlFeasible feasible,
                                                     Map<String, String> dependentAttributesOfGVPN) {
        if (orderSiteFeasibility != null) {
            ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book19.setOrder(4);
            book19.setGscQuoteId(site.getId());
            listBook.add(book19);

            ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
                    orderSiteFeasibility.getFeasibilityCode());
            book20.setOrder(4);
            book20.setGscQuoteId(site.getId());
            listBook.add(book20);
        }
        ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
                feasible == null ? "" : feasible.getCustomerSegment());
        book3.setOrder(4);
        book3.setGscQuoteId(site.getId());
        listBook.add(book3);

        if (orderSiteFeasibility != null) {
            ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LM_TYPE,
                    feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
            book4.setOrder(4);
            book4.setGscQuoteId(site.getId());
            listBook.add(book4);
            dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

            ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
                    orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
                            : orderSiteFeasibility.getSfdcFeasibilityId());
            book5.setOrder(4);
            book5.setGscQuoteId(site.getId());
            listBook.add(book5);

            ExcelBean siteCode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SITE_CODE,
                    Optional.ofNullable(orderSiteFeasibility.getOrderIllSite().getSiteCode()).orElse(CommonConstants.EMPTY));
            siteCode.setOrder(4);
            siteCode.setGscQuoteId(site.getId());
            listBook.add(siteCode);

            Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
            Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

            LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);

            ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.LEAD_TIME,
                    lead.toString());
            leadTime.setOrder(4);
            leadTime.setGscQuoteId(site.getId());
            listBook.add(leadTime);
        }
    }

    /**
     * Method to create site location for excel
     *
     * @param site
     * @param listBook
     * @throws TclCommonException
     */
    private void createSiteLocationForExcel(OrderIllSite site, List<ExcelBean> listBook) throws TclCommonException {
        String response = returnApiAddressForSites(site.getErfLocSitebLocationId());
        AddressDetail adDetail = null;
        if (response != null) {
            adDetail = (AddressDetail) Utils.convertJsonToObject(response, AddressDetail.class);
        }
        String address = "";
        if (adDetail != null) {
            address = adDetail.getAddressLineOne() + " " + adDetail.getLocality()+ " " + adDetail.getCity() + " " + adDetail.getState() + ""
                    + adDetail.getPincode();
        }
        ExcelBean siteLocation = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.SITE_ADDRESS, address);
        siteLocation.setOrder(4);
        siteLocation.setGscQuoteId(site.getId());
        listBook.add(siteLocation);
    }

    /**
     * Method to return API adresss for sites
     *
     * @param sites
     * @return
     */
    public String returnApiAddressForSites(Integer sites) {
        try {
            return (String) mqUtils.sendAndReceive(apiAddressQueue, String.valueOf(sites));
        } catch (Exception e) {
            LOGGER.error("error in process location", e);
        }
        return null;
    }

    /**
     * Method to create site properties and network details for excel
     *  @param excelMap
     * @param listBook
     * @param site
     * @param componentPriceMap
     * @param secondaryExcelMap
     * @param dependentAttributesOfGVPN
     */
    private void createSitePropAndNetworDetailsForExcel(Map<String, Map<String, String>> excelMap,
                                                        List<ExcelBean> listBook, OrderIllSite site, OrderToLe orderToLe, Map<String, OrderProductComponent> componentPriceMap,
                                                        Map<String, Map<String, String>> secondaryExcelMap, Map<String, String> dependentAttributesOfGVPN) {

        createSitePropertiesForExcel(excelMap, listBook, site,orderToLe);
        createNetworkComponentForExcel(excelMap, listBook, site, secondaryExcelMap, dependentAttributesOfGVPN);

    }

    /**
     * Method to create site properties for excel
     *
     * @param excelMap
     * @param listBook
     * @param site
     */
    private void createSitePropertiesForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
                                              OrderIllSite site,OrderToLe orderToLe) {

        Map<String, String> siteMap = excelMap.get("SITE_PROPERTIES");
        if (siteMap == null) {
            siteMap = new HashMap<>();
        }

        if (excelMap.containsKey(PDFConstants.GVPN_COMMON)) {
            Map<String, String> gvpnCommon = excelMap.get(PDFConstants.GVPN_COMMON);

            ExcelBean cosprofile = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_PROFILE,
                    gvpnCommon.containsKey(PDFConstants.SERVICE_VARIANT) ? gvpnCommon.get(PDFConstants.SERVICE_VARIANT)
                            : "");
            cosprofile.setGscQuoteId(site.getId());
            cosprofile.setOrder(4);
            listBook.add(cosprofile);
            String outCon = "";
            if (cosprofile.getAttributeValue().equals("Premium")) {
                outCon = "Premium";
            } else {
                outCon = "Basic";
            }

            ExcelBean portmode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.PORT_MODE,
                    gvpnCommon.containsKey(PDFConstants.PORT_MODE) ? gvpnCommon.get(PDFConstants.PORT_MODE)
                            : "");
            portmode.setSiteId(site.getId());
            portmode.setOrder(2);
            listBook.add(portmode);

            ExcelBean outOfContract = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS, "Out of Contract",
                    outCon);
            outOfContract.setOrder(4);
            outOfContract.setGscQuoteId(site.getId());
            listBook.add(outOfContract);

            ExcelBean vpnTopology = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.VPN_TOPOLOGY,
                    gvpnCommon.containsKey("VPN Topology") ? gvpnCommon.get("VPN Topology") : "");
            vpnTopology.setGscQuoteId(site.getId());
            vpnTopology.setOrder(4);
            listBook.add(vpnTopology);

            ExcelBean siteType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SITE_TYPE,
                    gvpnCommon.containsKey("Site Type") ? gvpnCommon.get("Site Type") : "");
            siteType.setGscQuoteId(site.getId());
            siteType.setOrder(4);
            listBook.add(siteType);

            ExcelBean cosModel = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_MODEL,

                    "6 Cos Model");
            cosModel.setGscQuoteId(site.getId());
            cosModel.setOrder(4);
            listBook.add(cosModel);

            ExcelBean cos1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_1,
                    gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS1)
                            ? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS1)
                            : "");
            cos1.setGscQuoteId(site.getId());
            cos1.setOrder(4);
            listBook.add(cos1);

            ExcelBean cos2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_2,
                    gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS2)
                            ? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS2)
                            : "");
            cos2.setGscQuoteId(site.getId());
            cos2.setOrder(4);
            listBook.add(cos2);

            ExcelBean cos4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_3,
                    gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS3)
                            ? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS3)
                            : "");
            cos4.setGscQuoteId(site.getId());
            cos4.setOrder(4);
            listBook.add(cos4);

            ExcelBean cos5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_4,
                    gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS4)
                            ? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS4)
                            : "");
            cos5.setGscQuoteId(site.getId());
            cos5.setOrder(4);
            listBook.add(cos5);

            ExcelBean cos6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_5,
                    gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS5)
                            ? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS5)
                            : "");
            cos6.setGscQuoteId(site.getId());
            cos6.setOrder(4);
            listBook.add(cos6);

            ExcelBean cos7 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.COS_6,
                    gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS6)
                            ? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS6)
                            : "");
            cos7.setGscQuoteId(site.getId());
            cos7.setOrder(4);
            listBook.add(cos7);

        }

        if (site.getIsTaxExempted() == CommonConstants.BACTIVE) {
            ExcelBean taxExcemption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.TAX_EXEMPTION,
                    siteMap.containsKey("TAX_EXCEMPTED_REASON") ? siteMap.get("TAX_EXCEMPTED_REASON") : "");
            taxExcemption.setGscQuoteId(site.getId());
            taxExcemption.setOrder(4);
            listBook.add(taxExcemption);
        }

        String contactDetails = "";
        try {
            if (siteMap.containsKey(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)) {
                String respone = returnLocationItContactName(Integer
                        .valueOf(siteMap.get(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)));
                if (respone != null) {
                    LocationItContact locationItContact = (LocationItContact) Utils.convertJsonToObject(respone,
                            LocationItContact.class);
                    contactDetails = locationItContact.getName() + "," + locationItContact.getEmail() + ","
                            + locationItContact.getContactNo();
                }
            }
        } catch (Exception e) {
            LOGGER.error("error in getting local It Contact info", e);
        }

        ExcelBean localItContact = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, contactDetails);
        localItContact.setGscQuoteId(site.getId());
        localItContact.setOrder(4);
        listBook.add(localItContact);

        String[] gstDetails = {""};

        if(StringUtils.isBlank(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO))) {

            gstDetails[0]=getGstDetails(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO),
                    orderToLe.getErfCusCustomerLegalEntityId());

        }

        ExcelBean siteGst = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.SITE_GST,
                Optional.ofNullable(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO)).map(siteMapVal -> siteMapVal + " " + gstDetails[0]).orElse(OrderDetailsExcelDownloadConstants.NA));

        siteGst.setGscQuoteId(site.getId());
        siteGst.setOrder(4);
        listBook.add(siteGst);

        Map<String, String> internePortMap = excelMap.get(OrderDetailsExcelDownloadConstants.INTERNET_PORT);

        if (internePortMap == null) {
            internePortMap = new HashMap<>();
        }
        // Removing duplicate entry of gvpn type
        /*String serviceType = internePortMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_TYPE)
                ? internePortMap.get(OrderDetailsExcelDownloadConstants.SERVICE_TYPE)
                : " ";

        ExcelBean illType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.GVPN_Type, serviceType == null ? "" : serviceType);
        illType.setOrder(4);
        illType.setGscQuoteId(site.getId());
        if (!listBook.contains(illType)) {
            listBook.add(illType);

        }*/

        Map<String, String> cpeManagementMap = excelMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGED);

        if (cpeManagementMap == null) {
            cpeManagementMap = new HashMap<>();
        }

        ExcelBean serviceOption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,

                OrderDetailsExcelDownloadConstants.SERVICE_OPTION,
                cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
                        ? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
                        : "NA");
        serviceOption.setOrder(4);
        serviceOption.setGscQuoteId(site.getId());
        if (!listBook.contains(serviceOption)) {
            listBook.add(serviceOption);
        }

        ExcelBean scopeOfManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,

                OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT,
                cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGED_TYPE)
                        ? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGED_TYPE)
                        : "NA");
        scopeOfManagement.setOrder(4);
        scopeOfManagement.setGscQuoteId(site.getId());
        if (!listBook.contains(scopeOfManagement)) {
            listBook.add(scopeOfManagement);
        }

    }

    /**
     * Method to return location It Contact Name
     *
     * @param id
     * @return
     */
    public String returnLocationItContactName(Integer id) {
        try {
            LOGGER.info("MDC Filter token value in before Queue call returnLocationItContactName {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            return (String) mqUtils.sendAndReceive(locationItContactQueue, String.valueOf(id));
        } catch (Exception e) {
            LOGGER.error("error in process location", e);
        }
        return null;
    }

    /**
     * Method to create network component based on feasibility
     *
     * @param orderSiteFeasibility
     * @param listBook
     * @param site
     * @param feasible
     * @param notFeasible
     * @param customerFeasible
     */
    private void createNetworkComponentBasedOnfeasibility(OrderSiteFeasibility orderSiteFeasibility,
                                                          List<ExcelBean> listBook, OrderIllSite site, Feasible feasible, NotFeasible notFeasible,
                                                          CustomFeasibilityRequest customerFeasible, Map<String, String> dependentAttributesOfGVPN) {
        String chargableDistance = "";
        if (feasible != null && feasible.getPOPDISTKMSERVICE() != null) {
            chargableDistance = String.valueOf(feasible.getPOPDISTKMSERVICE());

        }
        ExcelBean book29 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.CHARGABLE_DISTANCE, chargableDistance);
        book29.setOrder(5);
        book29.setGscQuoteId(site.getId());
        listBook.add(book29);

        String lmComponent;
        if (!dependentAttributesOfGVPN.isEmpty() && Objects.nonNull(dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.LM_TYPE))
                && dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.LM_TYPE).equalsIgnoreCase(INTL)) {
            lmComponent = "LM International offnet";
        } else {
            lmComponent = "LAST MILE";
        }

        ExcelBean book291 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.LM_COMPONENT, lmComponent);
        book291.setOrder(5);
        book291.setGscQuoteId(site.getId());
        listBook.add(book291);
        String prov = "";
        if (orderSiteFeasibility != null && orderSiteFeasibility.getProvider() != null) {
            prov = orderSiteFeasibility.getProvider();
            String feasMode = orderSiteFeasibility.getFeasibilityMode();
            if (prov.toLowerCase().contains(OrderDetailsExcelDownloadConstants.TATA_COMMUNICATIONS.toLowerCase())
                    && feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETWL)) {
                prov = OrderDetailsExcelDownloadConstants.MAN;
            } else if (notFeasible != null && feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETRF)) {
                prov = notFeasible.getSolutionType();

            } else if (feasible != null && feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETRF)) {
                prov = feasible.getSolutionType();

            } else if (customerFeasible != null
                    && feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETRF)) {
                prov = "";

            }
        }

        ExcelBean book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.PROVIDER, prov);
        book26.setOrder(5);
        book26.setGscQuoteId(site.getId());
        listBook.add(book26);
        ExcelBean book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.MANAGED, OrderDetailsExcelDownloadConstants.TCL);
        book27.setOrder(5);
        book27.setGscQuoteId(site.getId());
        listBook.add(book27);

        ExcelBean book25 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
        book25.setOrder(5);
        book25.setGscQuoteId(site.getId());
        listBook.add(book25);

    }

    /**
     * Method to create sla details
     *
     * @param site
     * @param listBook
     * @throws TclCommonException
     */
    private void createSlaDetails(OrderGsc orderGsc, OrderIllSite site, List<ExcelBean> listBook)
            throws TclCommonException {
        GscSlaBeanListener slaBeansListener = gscSlaService.getSlaDetails(orderGsc.getAccessType());
        List<GscSlaBean> slaBeans = slaBeansListener.getGscSlaBeans();
        if (Objects.nonNull(slaBeans)) {
            slaBeans.stream().forEach(sla -> {

                switch (sla.getSlaName()) {
                    case OrderDetailsExcelDownloadConstants.SERVICE_AVAILABILITY:
                        ExcelBean ntwUpTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
                                OrderDetailsExcelDownloadConstants.SERVICE_AVAILABILITY, sla.getDefaultValue());
                        ntwUpTime.setOrder(5);
                        ntwUpTime.setGscQuoteId(site.getId());
                        if (!listBook.contains(ntwUpTime)) {
                            listBook.add(ntwUpTime);
                        }
                        break;
                    case OrderDetailsExcelDownloadConstants.PACKET_DROP_GSC:

                        ExcelBean pckdrp = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
                                OrderDetailsExcelDownloadConstants.PACKET_DROP_GSC, sla.getDefaultValue());
                        pckdrp.setOrder(5);
                        pckdrp.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp)) {
                            listBook.add(pckdrp);
                        }
                        break;
                    case OrderDetailsExcelDownloadConstants.JITTER_SERVICE_LEVEL_GSC:
                        ExcelBean pckdrp2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
                                OrderDetailsExcelDownloadConstants.JITTER_SERVICE_LEVEL_GSC, sla.getDefaultValue());
                        pckdrp2.setOrder(5);
                        pckdrp2.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp2)) {
                            listBook.add(pckdrp2);
                        }
                        break;
                    case OrderDetailsExcelDownloadConstants.POST_DIAL_DELAY:
                        ExcelBean pckdrp3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
                                OrderDetailsExcelDownloadConstants.POST_DIAL_DELAY, sla.getDefaultValue());
                        pckdrp3.setOrder(5);
                        pckdrp3.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp3)) {
                            listBook.add(pckdrp3);
                        }
                        break;
                    case OrderDetailsExcelDownloadConstants.LATENCY:
                        ExcelBean pckdrp4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
                                OrderDetailsExcelDownloadConstants.LATENCY, sla.getDefaultValue());
                        pckdrp4.setOrder(5);
                        pckdrp4.setGscQuoteId(site.getId());
                        if (!listBook.contains(pckdrp4)) {
                            listBook.add(pckdrp4);
                        }
                        break;
                    default:
                        LOGGER.info("Invalid SLA name" + sla.getSlaName());

                }
            });
        }
    }

    /**
     * Method to create default network component
     *
     * @param site
     * @param listBook
     */
    private void createDefaultNetworkComponent(OrderIllSite site, List<ExcelBean> listBook) {
        ExcelBean book23 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
                OrderDetailsExcelDownloadConstants.LOCATION, "Non-CableHead");
        book23.setGscQuoteId(site.getId());
        book23.setOrder(5);
        listBook.add(book23);

    }

    /**
     * Method to create Lm Pop Value
     *
     * @param listBook
     * @param site
     * @param feasible
     */
    private void createLmPopValue(List<ExcelBean> listBook, OrderIllSite site, Feasible feasible) {

        String popLmDetails = null;
        try {
            LOGGER.info("MDC Filter token value in before Queue call createLmPopValue {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String locationResponse = (String) mqUtils.sendAndReceive(popQueue, site.getErfLocSiteaSiteCode());
			if (isNotBlank(locationResponse)) {
				LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
						LocationDetail.class);
				if (locationDetails.getApiAddress() != null) {
					popLmDetails = locationDetails.getAddress() + "" + locationDetails.getPopId() + ""
							+ locationDetails.getTier();

				} else if (locationDetails.getUserAddress() != null) {
					popLmDetails = locationDetails.getUserAddress() + "" + locationDetails.getPopId() + ""
							+ locationDetails.getTier();

				}
			}

        } catch (Exception e) {
            LOGGER.error("pop lm details errot", e);
        }
        String popAddress = "";
        if (feasible != null && feasible.getPopAddress() != null) {
            popAddress = feasible.getPopAddress();
        }

        if (popLmDetails != null) {
            popLmDetails = site.getErfLocSiteaSiteCode() + "," + popAddress;
        }

        ExcelBean book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.LM_POP_DETAILS, popLmDetails);
        book28.setOrder(5);
        book28.setGscQuoteId(site.getId());
        listBook.add(book28);

    }

    /**
     * Method to create billing component price
     *
     * @param listBook
     * @param site
     */
    private void createBillingComponentPrice(List<ExcelBean> listBook, OrderIllSite site) {

        MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
                .findByName(OrderDetailsExcelDownloadConstants.VPN_PORT);
        extractComponentPrices(site, internetPortmstProductComponent, listBook,
                OrderDetailsExcelDownloadConstants.PORT_MRC_NRC);

        MstProductComponent lastMilePortmstProductComponent = mstProductComponentRepository
                .findByName(FPConstants.LAST_MILE.toString());
        extractComponentPrices(site, lastMilePortmstProductComponent, listBook,
                OrderDetailsExcelDownloadConstants.LASTMILE_MRC_NRC);

        MstProductComponent addIpsPortmstProductComponent = mstProductComponentRepository
                .findByName(FPConstants.ADDITIONAL_IP.toString());
        extractComponentPrices(site, addIpsPortmstProductComponent, listBook,
                OrderDetailsExcelDownloadConstants.ADDITIONAL_IPS_MRC_NRC);

        MstProductComponent cpemstProductComponent = mstProductComponentRepository
                .findByName(FPConstants.CPE.toString());
        extractComponentPrices(site, cpemstProductComponent, listBook, OrderDetailsExcelDownloadConstants.CPE_MRC_NRC);

    }

    /**
     * Method to extract component prices
     *
     * @param site
     * @param mstProductComponent
     * @param listBook
     * @param attrName
     */
    private void extractComponentPrices(OrderIllSite site, MstProductComponent mstProductComponent,
                                        List<ExcelBean> listBook, String attrName) {
        List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                .findByReferenceIdAndMstProductComponent(site.getId(), mstProductComponent);
        if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
            for (OrderProductComponent orderProductComponent : orderProductComponents) {
                OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
                        String.valueOf(orderProductComponent.getId()), OrderDetailsExcelDownloadConstants.COMPONENTS);

                if (price != null) {
                    ExcelBean book36 = new ExcelBean(
                            BILLING_COMPONENT + "("
                                    + orderProductComponent.getType().toUpperCase() + ")",
                            attrName, price.getEffectiveMrc() + "," + price.getEffectiveNrc());
                    book36.setOrder(6);
                    book36.setGscQuoteId(site.getId());
                    listBook.add(book36);
                }
            }

        }
    }

    /**
     * Method to create network component for excel
     *  @param excelMap
     * @param listBook
     * @param site
     * @param secondaryExcelMap
     * @param dependentAttributesOfGVPN
     */
    private void createNetworkComponentForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
                                                OrderIllSite site, Map<String, Map<String, String>> secondaryExcelMap, Map<String, String> dependentAttributesOfGVPN) {
        Map<String, String> secondaryInternePortMap = null;
        Map<String, String> secondaryLatsMileMap = null;
        Map<String, String> secondaryCpeMap = null;
        Map<String, String> secondaryIasCommonMap = null;
        Map<String, String> secondaryCpeManagementMap = null;

        Map<String, String> internePortMap = excelMap.get(OrderDetailsExcelDownloadConstants.VPN_PORT);
        Map<String, String> latsMileMap = excelMap.get(OrderDetailsExcelDownloadConstants.LAST_MILE);
        Map<String, String> cpeMap = excelMap.get(OrderDetailsExcelDownloadConstants.CPE);
        Map<String, String> iasCommonMap = excelMap.get(OrderDetailsExcelDownloadConstants.GVPN_COMMON);
        Map<String, String> cpeManagementMap = excelMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT);
        createPrimaySecMap(listBook, site, OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE);
        secondaryInternePortMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.VPN_PORT);
        secondaryLatsMileMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.LAST_MILE);
        secondaryCpeMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.CPE);
        secondaryIasCommonMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.GVPN_COMMON);
        secondaryCpeManagementMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT);

        createInternetPortComponentForExcel(internePortMap, listBook, site, secondaryInternePortMap, dependentAttributesOfGVPN);
        createLastMileComponentForExcel(latsMileMap, listBook, site, secondaryLatsMileMap);
//        boolean isCpe = createCpeComponentForExcel(cpeMap, listBook, site);
//        boolean isSecondaryCpe = createCpeComponentForExcel(secondaryCpeMap, listBook, site);
        boolean isCpe = createCpeComponentForExcel(cpeMap, listBook, site);
        boolean isSecondaryCpe = false;
        if (secondaryCpeMap != null) {
            isSecondaryCpe = createCpeComponentForExcel(secondaryCpeMap, listBook, site);
        }

        createIasCommonForExcel(iasCommonMap, listBook, site, isCpe, secondaryIasCommonMap, isSecondaryCpe);
        createCpeManagementForExcel(cpeManagementMap, listBook, site, isCpe, secondaryCpeManagementMap, isSecondaryCpe, cpeMap, secondaryCpeMap);

    }

    /**
     * Method to create CPE management for excel
     *  @param cpeManagementMap
     * @param listBook
     * @param site
     * @param isCpe
     * @param secondaryCpeManagementMap
     * @param isSecondaryCpe
     * @param cpeMap
     * @param secondaryCpeMap
     */
    private void createCpeManagementForExcel(Map<String, String> cpeManagementMap, List<ExcelBean> listBook,
                                             OrderIllSite site, boolean isCpe, Map<String, String> secondaryCpeManagementMap,
                                             boolean isSecondaryCpe, Map<String, String> cpeMap, Map<String, String> secondaryCpeMap) {

        if (cpeManagementMap == null) {
            cpeManagementMap = new HashMap<>();
        }
        if (isCpe) {
            ExcelBean router = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
                    "CPE Component/Particulars", "Router");
            router.setOrder(4);
            router.setGscQuoteId(site.getId());
            if (!listBook.contains(router)) {
                listBook.add(router);
            }

            ExcelBean typeCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
                    "Type Of Equipment", "CPE");
            typeCpe.setOrder(4);
            typeCpe.setGscQuoteId(site.getId());
            if (!listBook.contains(typeCpe)) {
                listBook.add(typeCpe);
            }

            ExcelBean book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
                    OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
            book32.setOrder(4);
            book32.setGscQuoteId(site.getId());
            listBook.add(book32);

            ExcelBean serviceOption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SERVICE_OPTION,
                    cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
                            ? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
                            : "");
            serviceOption.setOrder(4);
            serviceOption.setGscQuoteId(site.getId());
            if (!listBook.contains(serviceOption)) {
                listBook.add(serviceOption);
            }

            ExcelBean scopeOfManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT,
                    cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            ? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            : "");
            scopeOfManagement.setOrder(4);
            scopeOfManagement.setGscQuoteId(site.getId());
            if (!listBook.contains(scopeOfManagement)) {
                listBook.add(scopeOfManagement);
            }

            ExcelBean cpeManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
                    OrderDetailsExcelDownloadConstants.CPE_MANAGED,
                    cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            ? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            : "");
            cpeManagement.setOrder(4);
            cpeManagement.setGscQuoteId(site.getId());
            if (!listBook.contains(cpeManagement)) {
                listBook.add(cpeManagement);
            }

            ExcelBean noOfConcurrentChannel = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL) ? cpeMap.get(OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL) : "");
            noOfConcurrentChannel.setOrder(4);
            noOfConcurrentChannel.setGscQuoteId(site.getId());
            if (!listBook.contains(noOfConcurrentChannel)) {
                listBook.add(noOfConcurrentChannel);
            }

            ExcelBean concurrentSessions = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.CONCURRENT_SESSIONS,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.CONCURRENT_SESSIONS) ? cpeMap.get(OrderDetailsExcelDownloadConstants.CONCURRENT_SESSIONS) : "");
            concurrentSessions.setOrder(4);
            concurrentSessions.setGscQuoteId(site.getId());
            if (!listBook.contains(concurrentSessions)) {
                listBook.add(concurrentSessions);
            }

            ExcelBean typeOfCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.TYPE_OF_CPE,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.TYPE_OF_CPE) ? cpeMap.get(OrderDetailsExcelDownloadConstants.TYPE_OF_CPE) : "");
            typeOfCpe.setOrder(4);
            typeOfCpe.setGscQuoteId(site.getId());
            if (!listBook.contains(typeOfCpe)) {
                listBook.add(typeOfCpe);
            }

            ExcelBean cubeLicenses = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.CUBE_LICENSES,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.CUBE_LICENSES) ? cpeMap.get(OrderDetailsExcelDownloadConstants.CUBE_LICENSES) : "");
            cubeLicenses.setOrder(4);
            cubeLicenses.setGscQuoteId(site.getId());
            if (!listBook.contains(cubeLicenses)) {
                listBook.add(cubeLicenses);
            }

            ExcelBean typeOfConnectivity = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.TYPE_OF_CONNECTIVITY,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.TYPE_OF_CONNECTIVITY) ? cpeMap.get(OrderDetailsExcelDownloadConstants.TYPE_OF_CONNECTIVITY) : "");
            typeOfConnectivity.setOrder(4);
            typeOfConnectivity.setGscQuoteId(site.getId());
            if (!listBook.contains(typeOfConnectivity)) {
                listBook.add(typeOfConnectivity);
            }

            ExcelBean pvdmAndMftQuantitites = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.PVDM_QUANTITIES,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.PVDM_QUANTITIES) ? cpeMap.get(OrderDetailsExcelDownloadConstants.PVDM_QUANTITIES) : "");
            pvdmAndMftQuantitites.setOrder(4);
            pvdmAndMftQuantitites.setGscQuoteId(site.getId());
            if (!listBook.contains(pvdmAndMftQuantitites)) {
                listBook.add(pvdmAndMftQuantitites);
            }

            ExcelBean codec = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.CODEC,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.CODEC) ? cpeMap.get(OrderDetailsExcelDownloadConstants.CODEC) : "");
            codec.setOrder(4);
            codec.setGscQuoteId(site.getId());
//            if (!listBook.contains(codec)) {
                listBook.add(codec);
//            }

            if(cpeMap.containsKey(OrderDetailsExcelDownloadConstants.SFP) && Objects.nonNull(cpeMap.get(OrderDetailsExcelDownloadConstants.SFP))) {
                ExcelBean sfp = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.POWER_CABLE,
                        cpeMap.get(OrderDetailsExcelDownloadConstants.SFP));
                sfp.setOrder(4);
                sfp.setGscQuoteId(site.getId());
                listBook.add(sfp);
            }

            if(cpeMap.containsKey(OrderDetailsExcelDownloadConstants.NIM) && Objects.nonNull(cpeMap.get(OrderDetailsExcelDownloadConstants.NIM))) {
                ExcelBean nim = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.POWER_CABLE,
                        cpeMap.get(OrderDetailsExcelDownloadConstants.NIM));
                nim.setOrder(4);
                nim.setGscQuoteId(site.getId());
                listBook.add(nim);

            }

            ExcelBean powerCable = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.POWER_CABLE,
                    cpeMap.containsKey(OrderDetailsExcelDownloadConstants.POWER_CABLE) ? cpeMap.get(OrderDetailsExcelDownloadConstants.POWER_CABLE) : "");
            powerCable.setOrder(4);
            powerCable.setGscQuoteId(site.getId());
            listBook.add(powerCable);

        }
        if (secondaryCpeManagementMap != null && !secondaryCpeManagementMap.isEmpty()) {
            createSecondaryCpeManagement(secondaryCpeManagementMap, site, isSecondaryCpe, listBook);
        }
        if(secondaryCpeMap != null && !secondaryCpeMap.isEmpty()) {
            createSecondaryCpe(site, listBook, secondaryCpeMap);
        }
    }

    /**
     * Method to create secondary CPE management
     *  @param secondaryCpeManagementMap
     * @param site
     * @param isSecondaryCpe
     * @param listBook
     * @param secondaryCpeMap
     */
    private void createSecondaryCpeManagement(Map<String, String> secondaryCpeManagementMap, OrderIllSite site,
                                              boolean isSecondaryCpe, List<ExcelBean> listBook) {

        if (isSecondaryCpe) {

            ExcelBean router = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
                    + OrderDetailsExcelDownloadConstants.SECONDARY_C, "CPE Component/Particulars", "Router");
            router.setOrder(4);
            router.setGscQuoteId(site.getId());
            listBook.add(router);

            ExcelBean typeCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
                    + OrderDetailsExcelDownloadConstants.SECONDARY_C, "Type Of Equipment", "CPE");
            typeCpe.setOrder(4);
            typeCpe.setGscQuoteId(site.getId());
            listBook.add(typeCpe);

            ExcelBean book32 = new ExcelBean(
                    OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
                            + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                    OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
            book32.setOrder(4);
            book32.setGscQuoteId(site.getId());
            listBook.add(book32);

            ExcelBean serviceOption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SERVICE_OPTION + ":"
                            + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                    secondaryCpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
                            ? secondaryCpeManagementMap.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
                            : "");
            serviceOption.setOrder(4);
            serviceOption.setGscQuoteId(site.getId());
            listBook.add(serviceOption);

            ExcelBean scopeOfManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                    OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT + ":"
                            + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                    secondaryCpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            ? secondaryCpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            : "");
            scopeOfManagement.setOrder(4);
            scopeOfManagement.setGscQuoteId(site.getId());
            if (!listBook.contains(scopeOfManagement)) {
                listBook.add(scopeOfManagement);
            }

            ExcelBean cpeManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
                    OrderDetailsExcelDownloadConstants.CPE_MANAGED + ":"
                            + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                    secondaryCpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            ? secondaryCpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
                            : "");
            cpeManagement.setOrder(4);
            cpeManagement.setGscQuoteId(site.getId());
            listBook.add(cpeManagement);
        }

    }

    private void createSecondaryCpe(OrderIllSite site, List<ExcelBean> listBook, Map<String, String> secondaryCpeMap) {
        ExcelBean noOfConcurrentChannel = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
                + OrderDetailsExcelDownloadConstants.SECONDARY_C, OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.NO_OF_CONCURRENT_CHANNEL) : "");
        noOfConcurrentChannel.setOrder(4);
        noOfConcurrentChannel.setGscQuoteId(site.getId());
        if (!listBook.contains(noOfConcurrentChannel)) {
            listBook.add(noOfConcurrentChannel);
        }

        ExcelBean secondaryConcurrentSessions = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
                + OrderDetailsExcelDownloadConstants.SECONDARY_C, OrderDetailsExcelDownloadConstants.CONCURRENT_SESSIONS,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.CONCURRENT_SESSIONS) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.CONCURRENT_SESSIONS) : "");
        secondaryConcurrentSessions.setOrder(4);
        secondaryConcurrentSessions.setGscQuoteId(site.getId());
//        if (!listBook.contains(secondaryConcurrentSessions)) {
            listBook.add(secondaryConcurrentSessions);
//        }

        ExcelBean secondaryTypeOfCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE
                + ":"
                + OrderDetailsExcelDownloadConstants.SECONDARY_C, OrderDetailsExcelDownloadConstants.TYPE_OF_CPE,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.TYPE_OF_CPE) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.TYPE_OF_CPE) : "");
        secondaryTypeOfCpe.setOrder(4);
        secondaryTypeOfCpe.setGscQuoteId(site.getId());
//        if (!listBook.contains(secondaryPbxType)) {
            listBook.add(secondaryTypeOfCpe);
//        }

        ExcelBean secondaryCubeLicenses = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE
                + ":"
                + OrderDetailsExcelDownloadConstants.SECONDARY_C, OrderDetailsExcelDownloadConstants.CUBE_LICENSES,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.CUBE_LICENSES) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.CUBE_LICENSES) : "");
        secondaryCubeLicenses.setOrder(4);
        secondaryCubeLicenses.setGscQuoteId(site.getId());
//        if (!listBook.contains(secondaryCubeLicenses)) {
            listBook.add(secondaryCubeLicenses);
//        }

        ExcelBean secondaryTypeOfConnectivity = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE
                + ":"
                + OrderDetailsExcelDownloadConstants.SECONDARY_C, OrderDetailsExcelDownloadConstants.TYPE_OF_CONNECTIVITY,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.TYPE_OF_CONNECTIVITY) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.TYPE_OF_CONNECTIVITY) : "");
        secondaryTypeOfConnectivity.setOrder(4);
        secondaryTypeOfConnectivity.setGscQuoteId(site.getId());
//        if (!listBook.contains(secondaryTypeOfConnectivity)) {
            listBook.add(secondaryTypeOfConnectivity);
//        }

        ExcelBean secondaryPvdmAndMftQuantitites = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE
                + ":"
                + OrderDetailsExcelDownloadConstants.SECONDARY_C, OrderDetailsExcelDownloadConstants.PVDM_QUANTITIES,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.PVDM_QUANTITIES) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.PVDM_QUANTITIES) : "");
        secondaryPvdmAndMftQuantitites.setOrder(4);
        secondaryPvdmAndMftQuantitites.setGscQuoteId(site.getId());
//        if (!listBook.contains(secondaryPvdmAndMftQuantitites)) {
            listBook.add(secondaryPvdmAndMftQuantitites);
//        }

        ExcelBean secondaryCodec = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE
                + ":"
                + OrderDetailsExcelDownloadConstants.SECONDARY_C, OrderDetailsExcelDownloadConstants.CODEC,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.CODEC) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.CODEC) : "");
        secondaryCodec.setOrder(4);
        secondaryCodec.setGscQuoteId(site.getId());
//        if (!listBook.contains(secondaryPvdmAndMftQuantitites)) {
        listBook.add(secondaryCodec);
//        }

        if(secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.SFP) && Objects.nonNull(secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.SFP))) {
            ExcelBean sfp = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.POWER_CABLE,
                    secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.SFP));
            sfp.setOrder(4);
            sfp.setGscQuoteId(site.getId());
            listBook.add(sfp);
        }

        if(secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.NIM) && Objects.nonNull(secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.NIM))) {
            ExcelBean nim = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.POWER_CABLE,
                    secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.NIM));
            nim.setOrder(4);
            nim.setGscQuoteId(site.getId());
            listBook.add(nim);
        }

        ExcelBean powerCable = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE, OrderDetailsExcelDownloadConstants.POWER_CABLE,
                secondaryCpeMap.containsKey(OrderDetailsExcelDownloadConstants.POWER_CABLE) ? secondaryCpeMap.get(OrderDetailsExcelDownloadConstants.POWER_CABLE) : "");
        powerCable.setOrder(4);
        powerCable.setGscQuoteId(site.getId());
        listBook.add(powerCable);

    }

    /**
     * Method to create IAS common for excel
     *
     * @param iasCommonMap
     * @param listBook
     * @param site
     * @param isCpe
     * @param secondaryIasCommonMap
     * @param isSecondaryCpe
     */
    private void createIasCommonForExcel(Map<String, String> iasCommonMap, List<ExcelBean> listBook, OrderIllSite site,
                                         boolean isCpe, Map<String, String> secondaryIasCommonMap, boolean isSecondaryCpe) {

        if (iasCommonMap == null) {
            iasCommonMap = new HashMap<>();
        }

        ExcelBean connectorType = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE,
                iasCommonMap.containsKey(ComponentConstants.CONNECTOR_TYPE.getComponentsValue())
                        ? iasCommonMap.get(ComponentConstants.CONNECTOR_TYPE.getComponentsValue())
                        : "");
        connectorType.setOrder(4);
        connectorType.setGscQuoteId(site.getId());
        if (!listBook.contains(connectorType)) {
            listBook.add(connectorType);
        }
        String portComp = "";

        if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equalsIgnoreCase(PDFConstants.BUY_SINGLE_UNMANAGED_GVPN)) {
            portComp = "VPN Port";
        } else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equalsIgnoreCase(PDFConstants.BUY_DUAL_UNMANAGED_GVPN)) {
            portComp = "VPN Port/VPN Backup Port";
        } else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equalsIgnoreCase(PDFConstants.BUY_SINGLE_MANAGED_GVPN)) {
            portComp = "VPN Port";
        } else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equalsIgnoreCase(PDFConstants.BUY_DUAL_MANAGED_GVPN)) {
            portComp = "VPN Port/VPN Backup Port";
        }

        ExcelBean serviceVariant = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
                "Port Component/Particulars", portComp);
        serviceVariant.setOrder(4);
        serviceVariant.setGscQuoteId(site.getId());
        listBook.add(serviceVariant);

        if (isCpe) {
            ExcelBean iasCommonCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
                    OrderDetailsExcelDownloadConstants.CPE_CONTRACT_TYPE,
                    iasCommonMap.containsKey("CPE") ? iasCommonMap.get("CPE") : "");
            iasCommonCpe.setOrder(4);
            iasCommonCpe.setGscQuoteId(site.getId());
            if (!listBook.contains(iasCommonCpe)) {
                listBook.add(iasCommonCpe);
            }
        }

        if (secondaryIasCommonMap != null && !secondaryIasCommonMap.isEmpty()) {
            createSecondaryIasCommonMap(site, listBook, secondaryIasCommonMap, isSecondaryCpe);
        }

    }

    /**
     * Method to create secondary IAS common map
     *
     * @param site
     * @param listBook
     * @param secondaryIasCommonMap
     * @param isCpe
     */
    private void createSecondaryIasCommonMap(OrderIllSite site, List<ExcelBean> listBook,
                                             Map<String, String> secondaryIasCommonMap, boolean isCpe) {
        ExcelBean connectorType = new ExcelBean(
                OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE + ":"
                        + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE,
                secondaryIasCommonMap.containsKey("Connector Type") ? secondaryIasCommonMap.get("Connector Type") : "");
        connectorType.setOrder(4);
        connectorType.setGscQuoteId(site.getId());
        if (!listBook.contains(connectorType)) {
            listBook.add(connectorType);
        }

        ExcelBean serviceVariant = new ExcelBean(
                OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT + ":"
                        + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                "Port Component/Particulars",
                secondaryIasCommonMap.containsKey("Service Variant") ? secondaryIasCommonMap.get("Service Variant")
                        : "");
        serviceVariant.setOrder(4);
        serviceVariant.setGscQuoteId(site.getId());
        listBook.add(serviceVariant);
        if (isCpe) {
            ExcelBean iasCommonCpe = new ExcelBean(
                    OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
                            + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                    OrderDetailsExcelDownloadConstants.CPE_CONTRACT_TYPE,
                    secondaryIasCommonMap.containsKey("CPE") ? secondaryIasCommonMap.get("CPE") : "");
            iasCommonCpe.setOrder(4);
            iasCommonCpe.setGscQuoteId(site.getId());
            listBook.add(iasCommonCpe);
        }

        ExcelBean portmode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.PORT_MODE,
                secondaryIasCommonMap.containsKey(PDFConstants.PORT_MODE) ? secondaryIasCommonMap.get(PDFConstants.PORT_MODE)
                        : "");
        portmode.setSiteId(site.getId());
        portmode.setOrder(2);
        listBook.add(portmode);

    }

    /**
     * Method to create CPE component for excel
     *
     * @param cpeMap
     * @param listBook
     * @param site
     * @return
     */
    private boolean createCpeComponentForExcel(Map<String, String> cpeMap, List<ExcelBean> listBook,
                                               OrderIllSite site) {
        boolean isCpe = false;
        if (cpeMap == null) {
            cpeMap = new HashMap<>();
        }
        String cpeChassis = cpeMap.containsKey(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue())
                ? cpeMap.get(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue())
                : null;
        if (cpeChassis != null) {
            ExcelBean cpeChasis = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
                    "CPE Basic Chassis", cpeChassis);
            cpeChasis.setOrder(5);
            cpeChasis.setGscQuoteId(site.getId());
            if (!listBook.contains(cpeChasis)) {
                listBook.add(cpeChasis);
                isCpe = true;
            }
        }
        return isCpe;
    }

    /**
     * Method to create last mile component for excel
     *
     * @param latsMileMap
     * @param listBook
     * @param site
     * @param secondaryLatsMileMap
     */
    private void createLastMileComponentForExcel(Map<String, String> latsMileMap, List<ExcelBean> listBook,
                                                 OrderIllSite site, Map<String, String> secondaryLatsMileMap) {
        if (latsMileMap == null) {
            latsMileMap = new HashMap<>();
        }
        String localLoopBandwidth = "";
        if (PortBandwithConstants.getBandwidthMap()
                .containsKey(latsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()))) {
            localLoopBandwidth = PortBandwithConstants
                    .getPortBandWidth(latsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()));

        } else {
            localLoopBandwidth = latsMileMap.containsKey(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue())
                    ? latsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()) + "mbps"
                    : "NA";
        }

        ExcelBean localLoopBandWidth = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD, localLoopBandwidth);
        localLoopBandWidth.setOrder(5);
        localLoopBandWidth.setGscQuoteId(site.getId());
        if (!listBook.contains(localLoopBandWidth)) {
            listBook.add(localLoopBandWidth);
        }

        if (secondaryLatsMileMap != null && !secondaryLatsMileMap.isEmpty()) {
            createSecondaryLastMileComponent(listBook, secondaryLatsMileMap, site);
        }

    }

    /**
     * Method to create secondary last mile component
     *
     * @param listBook
     * @param secondaryLatsMileMap
     * @param site
     */
    private void createSecondaryLastMileComponent(List<ExcelBean> listBook, Map<String, String> secondaryLatsMileMap,
                                                  OrderIllSite site) {

        String localLoopBandwidth = "";
        if (PortBandwithConstants.getBandwidthMap()
                .containsKey(secondaryLatsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()))) {
            localLoopBandwidth = PortBandwithConstants.getPortBandWidth(
                    secondaryLatsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()));

        } else {
            localLoopBandwidth = secondaryLatsMileMap
                    .containsKey(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue())
                    ? secondaryLatsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue())
                    + "mbps"
                    : "NA";
        }
        ExcelBean localLoopBandWidth = new ExcelBean(
                OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE + ":"
                        + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD, localLoopBandwidth);
        localLoopBandWidth.setOrder(4);
        localLoopBandWidth.setGscQuoteId(site.getId());
        listBook.add(localLoopBandWidth);

    }

    /**
     * Method to create internetport component for excel
     *  @param internePortMap
     * @param listBook
     * @param site
     * @param secondaryInternePortMap
     * @param dependentAttributesOfGVPN
     */
    private void createInternetPortComponentForExcel(Map<String, String> internePortMap, List<ExcelBean> listBook,
                                                     OrderIllSite site, Map<String, String> secondaryInternePortMap, Map<String, String> dependentAttributesOfGVPN) {
        if (internePortMap == null) {
            internePortMap = new HashMap<>();
        }
        // Removing duplicate entry of gvpn type
        /*ExcelBean illType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.GVPN_Type, "Fixed");
        illType.setOrder(4);
        illType.setGscQuoteId(site.getId());
        if (!listBook.contains(illType)) {
            listBook.add(illType);

        }*/
        String wanIp = "";
        if (internePortMap.containsKey(ComponentConstants.WAN_IP_PROVIDED_BY.getComponentsValue())) {
            wanIp = internePortMap.get(ComponentConstants.WAN_IP_PROVIDED_BY.getComponentsValue());
        }

        ExcelBean wanIP = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                ComponentConstants.WAN_IP_PROVIDED_BY.getComponentsValue(), wanIp);
        wanIP.setOrder(4);
        wanIP.setGscQuoteId(site.getId());
        listBook.add(wanIP);

        ExcelBean portBandwd = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
                ComponentConstants.PORT_BANDWIDTH.getComponentsValue(),
                PortBandwithConstants.handleBwVales(
                internePortMap.containsKey(ComponentConstants.PORT_BANDWIDTH.getComponentsValue())
                        ? internePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue())
                        : ""));
        portBandwd.setOrder(4);
        portBandwd.setGscQuoteId(site.getId());
        if (!listBook.contains(portBandwd)) {
            listBook.add(portBandwd);
        }
        String interf = internePortMap.containsKey(ComponentConstants.INTERFACE.getComponentsValue())
                ? internePortMap.get(ComponentConstants.INTERFACE.getComponentsValue())
                : "Non-Ethernet";

        ExcelBean port = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                OrderDetailsExcelDownloadConstants.PORT, "Ethernet");
        port.setOrder(4);
        port.setGscQuoteId(site.getId());
        if (!listBook.contains(port)) {
            listBook.add(port);
        }

        /*if(dependentAttributesOfGVPN.containsKey(OrderDetailsExcelDownloadConstants.HAND_OFF)){
            interf = interf + "," + dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.HAND_OFF);
        }*/

        ExcelBean iFace = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
                OrderDetailsExcelDownloadConstants.INTERFACE, interf);
        iFace.setOrder(4);
        iFace.setGscQuoteId(site.getId());
        if (!listBook.contains(iFace)) {
            listBook.add(iFace);
        }
        if (secondaryInternePortMap != null && !secondaryInternePortMap.isEmpty()) {

            createSecondaryInternetPortDetails(secondaryInternePortMap, listBook, site, dependentAttributesOfGVPN);

        }

        String handOff = "";
        if(dependentAttributesOfGVPN.containsKey(HAND_OFF)){
            handOff = dependentAttributesOfGVPN.get(HAND_OFF);
        }

        ExcelBean handOffBean = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
                OrderDetailsExcelDownloadConstants.HAND_OFF_TYPE, handOff);
        handOffBean.setOrder(4);
        handOffBean.setSiteId(site.getId());
        if (!listBook.contains(handOffBean)) {
            listBook.add(handOffBean);
        }

    }

    /**
     * Method to create secondary internet port details
     *  @param secondaryInternePortMap
     * @param listBook
     * @param site
     * @param dependentAttributesOfGVPN
     */
    private void createSecondaryInternetPortDetails(Map<String, String> secondaryInternePortMap,
                                                    List<ExcelBean> listBook, OrderIllSite site, Map<String, String> dependentAttributesOfGVPN) {
        if (secondaryInternePortMap.containsKey(ComponentConstants.PORT_BANDWIDTH.getComponentsValue())) {
            String portBandwidth = "";

            if (PortBandwithConstants.getBandwidthMap()
                    .containsKey(secondaryInternePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue()))) {
                portBandwidth = PortBandwithConstants.handleBwVales(
                        secondaryInternePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue()));
            } else {
                portBandwidth = secondaryInternePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue());
            }
            ExcelBean secondaryPortBandwidth = new ExcelBean(
                    OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT + ":"
                            + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                    ComponentConstants.PORT_BANDWIDTH.getComponentsValue(), portBandwidth);
            secondaryPortBandwidth.setOrder(4);
            secondaryPortBandwidth.setGscQuoteId(site.getId());
            listBook.add(secondaryPortBandwidth);
        }

        String secondaryInterf = secondaryInternePortMap.containsKey(ComponentConstants.INTERFACE.getComponentsValue())
                ? secondaryInternePortMap.get(ComponentConstants.INTERFACE.getComponentsValue())
                : " ";

        ExcelBean secondaryPort = new ExcelBean(
                OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE + ":"
                        + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                OrderDetailsExcelDownloadConstants.PORT, "Ethernet");
        secondaryPort.setOrder(4);
        secondaryPort.setGscQuoteId(site.getId());
        listBook.add(secondaryPort);

        /*if(dependentAttributesOfGVPN.containsKey(OrderDetailsExcelDownloadConstants.HAND_OFF)) {
            secondaryInterf = secondaryInterf + dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.HAND_OFF);
        }*/

        ExcelBean secondaryiFace = new ExcelBean(
                OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT + ":"
                        + OrderDetailsExcelDownloadConstants.SECONDARY_C,
                OrderDetailsExcelDownloadConstants.INTERFACE, secondaryInterf);
        secondaryiFace.setOrder(4);
        secondaryiFace.setGscQuoteId(site.getId());
        listBook.add(secondaryiFace);

        String handOff = "";
        if(dependentAttributesOfGVPN.containsKey(HAND_OFF)){
            handOff = dependentAttributesOfGVPN.get(HAND_OFF);
        }

        ExcelBean handOffBean = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
                OrderDetailsExcelDownloadConstants.HAND_OFF_TYPE, handOff);
        handOffBean.setOrder(4);
        handOffBean.setSiteId(site.getId());
        if (!listBook.contains(handOffBean)) {
            listBook.add(handOffBean);
        }
    }

    /**
     * Method to create primary sec map
     *
     * @param listBook
     * @param site
     * @param details
     */
    private void createPrimaySecMap(List<ExcelBean> listBook, OrderIllSite site, String details) {
        String presecMap = "";
        if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equals(OrderDetailsExcelDownloadConstants.BUY_SINGLE_UNMANAGED_GVPN)) {
            presecMap = OrderDetailsExcelDownloadConstants.SINGLE;
        } else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equals(OrderDetailsExcelDownloadConstants.BUY_DUAL_MANAGED_GVPN)) {
            presecMap = OrderDetailsExcelDownloadConstants.SECONDARY;
            ExcelBean book30 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                    OrderDetailsExcelDownloadConstants.GVPN_BACKUP, OrderDetailsExcelDownloadConstants.YES);
            book30.setOrder(5);
            book30.setGscQuoteId(site.getId());
            listBook.add(book30);
        } else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equals(OrderDetailsExcelDownloadConstants.BUY_SINGLE_MANAGED_GVPN)) {
            presecMap = OrderDetailsExcelDownloadConstants.SINGLE;
            ExcelBean book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                    OrderDetailsExcelDownloadConstants.GVPN_BACKUP, OrderDetailsExcelDownloadConstants.NO);
            book32.setOrder(5);
            book32.setGscQuoteId(site.getId());
            listBook.add(book32);
        } else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
                .equals(OrderDetailsExcelDownloadConstants.BUY_DUAL_UNMANAGED_GVPN)) {
            presecMap = OrderDetailsExcelDownloadConstants.SECONDARY;
            ExcelBean book31 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
                    OrderDetailsExcelDownloadConstants.GVPN_BACKUP, OrderDetailsExcelDownloadConstants.YES);
            book31.setOrder(5);
            book31.setGscQuoteId(site.getId());
            listBook.add(book31);
        } else {
            presecMap = site.getOrderProductSolution().getMstProductOffering().getProductName();
        }
        ExcelBean book18 = new ExcelBean(details, OrderDetailsExcelDownloadConstants.PRI_SEC_MAPPING, presecMap);
        if (details.equals(OrderDetailsExcelDownloadConstants.SITE_DETAILS)) {
            book18.setOrder(5);
        } else {
            book18.setOrder(5);

        }
        book18.setGscQuoteId(site.getId());

        if (!listBook.contains(book18)) {
            listBook.add(book18);
        }
    }

    /**
     * Method to get city selection
     *
     * @return
     */
    private String getLNSCitySelection(Integer orderGscDetailId, String serviceType) {
        List<GscCityNumber> cityDetails = gscOrderDetailService.getCityNumberConfiguration(orderGscDetailId,
                serviceType);

        List<String> cityCodes = cityDetails.stream().map(GscCityNumber::getOriginCity)
                .collect(Collectors.toList());
        LOGGER.info("City Codes" + cityCodes.toString());
        Map<String, String> citiesWithCode = queueForLNSCityNamesCall();
        LOGGER.info("All cities Codes" + citiesWithCode.toString());
        return cityCodes.stream().map(city -> {
            return citiesWithCode.get(city);

        }).collect(Collectors.joining(","));
    }

    /**
     * Method to call queue for city names
     *
     * @return
     */
    private Map<String, String> queueForLNSCityNamesCall() {
        Map<String, String> cityCodeAndNameList = new HashMap<>();
        try {
            String response = (String) mqUtils.sendAndReceive(cityDetailsQueue, "");
            LOGGER.info("Response received from queue:" + response);
            cityCodeAndNameList = (Map<String, String>) GscUtils.fromJson(response, Map.class);
            LOGGER.info("queue response" + cityCodeAndNameList.toString());
        } catch (Exception e) {
            LOGGER.info("City Queue Exception :: ", e.getMessage());
        }
        return cityCodeAndNameList;
    }

    /**
     * Method to get city selection
     *
     * @return
     */
    private String getACANSCitySelection(Integer orderGscDetailId, String serviceType) {
        List<GscCityNumber> cityDetails = gscOrderDetailService.getCityNumberConfiguration(orderGscDetailId,
                serviceType);

        List<String> cityCodes = cityDetails.stream().map(GscCityNumber::getOriginCity)
                .collect(Collectors.toList());
        LOGGER.info("City Codes" + cityCodes.toString());
        Map<String, String> citiesWithCode = queueForACANSCityNamesCall();
        LOGGER.info("All cities Codes" + citiesWithCode.toString());
        return cityCodes.stream().map(city -> {
            return citiesWithCode.get(city);

        }).collect(Collectors.joining(","));
    }

    /**
     * Method to call queue for city names
     *
     * @return
     */
    private Map<String, String> queueForACANSCityNamesCall() {
        Map<String, String> cityCodeAndNameList = new HashMap<>();
        try {
            String response = (String) mqUtils.sendAndReceive(acansCityDetailsQueue, "");
            LOGGER.info("Response received from queue:" + response);
            cityCodeAndNameList = (Map<String, String>) GscUtils.fromJson(response, Map.class);
            LOGGER.info("queue response" + cityCodeAndNameList.toString());
        } catch (Exception e) {
            LOGGER.info("City Queue Exception :: ", e.getMessage());
        }
        return cityCodeAndNameList;
    }
    
    public String getEmergencyAddress(String emergencyAddressId) {
    	String emergencyAddressValue = "";
        try {
            String response= (String) mqUtils.sendAndReceive(addressDetail, emergencyAddressId);
			if (isNotBlank(response)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(response, AddressDetail.class);

				emergencyAddressValue = Optional.ofNullable(addressDetail.getAddressLineOne()).orElse("") + ","
						+ Optional.ofNullable(addressDetail.getAddressLineTwo()).orElse("") + ","
						+ Optional.ofNullable(addressDetail.getLocality()).orElse("") + ","
						+ Optional.ofNullable(addressDetail.getCity()).orElse("") + ","
						+ Optional.ofNullable(addressDetail.getState()).orElse("") + ","
						+ Optional.ofNullable(addressDetail.getCountry()).orElse("") + ","
						+ Optional.ofNullable(addressDetail.getPincode()).orElse("");
			}
        } catch (Exception e) {
            LOGGER.warn("Error in process location {}", e.getMessage());
        }
        return emergencyAddressValue;
    }
    private void getDownStreamAttributeValues(List<ExcelBean> listBook, GscOrderBean gscOrderBean){

        if(gscOrderBean.getInternationalDownStreamOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getInternationalDownStreamOrderId())) {
            ExcelBean bookInternatinalDownStreamOrder = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID,gscOrderBean.getInternationalDownStreamOrderId());
            bookInternatinalDownStreamOrder.setOrder(3);
            bookInternatinalDownStreamOrder.setGscQuoteDetailId(0);
            listBook.add(bookInternatinalDownStreamOrder);
        }

        if(gscOrderBean.getInternationalDownStreamSubOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getInternationalDownStreamSubOrderId())) {
            ExcelBean bookInternatinalDownStreamSubOrder = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID,gscOrderBean.getInternationalDownStreamSubOrderId());
            bookInternatinalDownStreamSubOrder.setOrder(3);
            bookInternatinalDownStreamSubOrder.setGscQuoteDetailId(0);
            listBook.add(bookInternatinalDownStreamSubOrder);
        }

        if(gscOrderBean.getDomesticDownStreamOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getDomesticDownStreamOrderId())) {
            ExcelBean bookDomesticDownStreamOrder = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_DOMESTIC_DOWNSTREAM_ORDER_ID,gscOrderBean.getDomesticDownStreamOrderId());
            bookDomesticDownStreamOrder.setOrder(3);
            bookDomesticDownStreamOrder.setGscQuoteDetailId(0);
            listBook.add(bookDomesticDownStreamOrder);
        }

        if(gscOrderBean.getDomesticDownStreamSubOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getDomesticDownStreamSubOrderId())) {
            ExcelBean bookDomesticDownStreamSubOrder = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID,gscOrderBean.getDomesticDownStreamSubOrderId());
            bookDomesticDownStreamSubOrder.setOrder(3);
            bookDomesticDownStreamSubOrder.setGscQuoteDetailId(0);
            listBook.add(bookDomesticDownStreamSubOrder);
        }

        if(gscOrderBean.getInterConnectDownStreamOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamOrderId())) {
            ExcelBean bookInterconectDownStreamOrder = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID,gscOrderBean.getInterConnectDownStreamOrderId());
            bookInterconectDownStreamOrder.setOrder(3);
            bookInterconectDownStreamOrder.setGscQuoteDetailId(0);
            listBook.add(bookInterconectDownStreamOrder);
        }


        if(gscOrderBean.getInterConnectDownStreamSubOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamSubOrderId())) {
            ExcelBean bookInterconnectDownStreamSubOrder = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID,gscOrderBean.getInterConnectDownStreamSubOrderId());
            bookInterconnectDownStreamSubOrder.setOrder(3);
            bookInterconnectDownStreamSubOrder.setGscQuoteDetailId(0);
            listBook.add(bookInterconnectDownStreamSubOrder);
        }

        if(gscOrderBean.getInterConnectDownStreamDomesticVTSSubOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamDomesticVTSSubOrderId())) {
            ExcelBean bookInterconectDownStreamOrderVts = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS,gscOrderBean.getInterConnectDownStreamDomesticVTSSubOrderId());
            bookInterconectDownStreamOrderVts.setOrder(3);
            bookInterconectDownStreamOrderVts.setGscQuoteDetailId(0);
            listBook.add(bookInterconectDownStreamOrderVts);
        }

        if(gscOrderBean.getInterConnectDownStreamDomesticNVTSubOrderId()!=null && !StringUtils.isBlank(gscOrderBean.getInterConnectDownStreamDomesticNVTSubOrderId())) {
            ExcelBean bookInterconectDownStreamOrderNvt = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT,gscOrderBean.getInterConnectDownStreamDomesticNVTSubOrderId());
            bookInterconectDownStreamOrderNvt.setOrder(3);
            bookInterconectDownStreamOrderNvt.setGscQuoteDetailId(0);
            listBook.add(bookInterconectDownStreamOrderNvt);
        }
    }
    /**
     * getGstDetails
     *
     * @param gstNo
     * @param customerLeID
     * @return
     */
    private String getGstDetails(String gstNo,Integer customerLeID) {

        SiteGstDetail siteGstDetail = new SiteGstDetail();
        siteGstDetail.setGstNo(gstNo);
        siteGstDetail.setCustomerLeId(customerLeID);

        if (Objects.nonNull(gstNo) && Objects.nonNull(customerLeID)) {
            try {
                String leGst = (String) mqUtils.sendAndReceive(siteGstQueue, Utils.convertObjectToJson(siteGstDetail));

                if (isNotBlank(leGst)) {
                    LeStateInfo leStateInfo = (LeStateInfo) Utils.convertJsonToObject(leGst, LeStateInfo.class);
                    if (Objects.nonNull(leStateInfo.getAddress())) {
                        return leStateInfo.getAddress();
                    } else {
                        LOGGER.info("Address is empty for legal entity {} and site gst {}", customerLeID, gstNo);
                    }
                }
            } catch (TclCommonException | IllegalArgumentException e) {

                LOGGER.error("error in getting gst response");
            }
        }

        return "";
    }

    private static String getLeadTime(Timestamp confCreatedDate, String exptDeliveryDate) {
        String leadTime = null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        try {
            Date dateExpected = dateFormatter.parse(exptDeliveryDate);
            Date dateCreated = new Date(confCreatedDate.getTime());
            LocalDate dateBefore = dateCreated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dateAfter = dateExpected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            leadTime=String.valueOf(ChronoUnit.DAYS.between(dateBefore, dateAfter));
        } catch (Exception ex) {
            LOGGER.info("Error in calculating Lead Time for GSC" + ex.getMessage());
            throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ex,
                    ResponseResource.R_CODE_ERROR);
        }
        return leadTime;
    }

    private void getSelectedOutpulseNumber(List<ExcelBean> listBook, Map<String, Integer> attributesMap,
                                           Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes) {
        Integer selectedOutpulseNumberId = attributesMap.get(GscAttributeConstants.SELECTED_TERMINATION_NUMBER_OUTPULSE);
        String selectedOutpulseNumber = getAttributeValue(selectedOutpulseNumberId, orderProductComponentAttributes);
        if (Objects.nonNull(selectedOutpulseNumber) && !selectedOutpulseNumber.isEmpty()) {
            ExcelBean selectedOutpulseNumberBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.UIFN_NUMBER,
                    getAttributeValue(selectedOutpulseNumberId, orderProductComponentAttributes));
            selectedOutpulseNumberBook.setOrder(3);
            selectedOutpulseNumberBook.setGscQuoteDetailId(0);
            listBook.add(selectedOutpulseNumberBook);
        }
    }

    private void getDIDSiteAddress(List<ExcelBean> listBook, Map<String, Integer> attributesMap,
                                           Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes) {
        Integer didSiteAddressValues = attributesMap.get(GscAttributeConstants.DOMESTIC_VOICE_SITE_ADDRESS);
        String didSiteAddressNumber = getAttributeValue(didSiteAddressValues, orderProductComponentAttributes);
        if (Objects.nonNull(didSiteAddressNumber) && !didSiteAddressNumber.isEmpty()) {
            ExcelBean didSiteAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                    OrderDetailsExcelDownloadConstants.DID_SITE_ADDRESS,
                    getAttributeValue(didSiteAddressValues, orderProductComponentAttributes));
            didSiteAddress.setOrder(3);
            didSiteAddress.setGscQuoteDetailId(0);
            listBook.add(didSiteAddress);
        }
    }

    /**
     * getMACDServiceAttributes to show MACD service attribute
     *
     * @param List<ExcelBean>
     * @param Map<String, Integer>
     * Set<OrderProductComponentsAttributeValue>
     *
     *
     */
    private void getMACDServiceAttributes(List<ExcelBean> listBook, Map<String, Integer> attributesMap,
                                          Set<OrderProductComponentsAttributeValue> orderProductComponentAttributes){
        Integer switchUnit = attributesMap.get(GscConstants.SWTCH_UNIT_CD_RERT);
        ExcelBean switchUnitBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                OrderDetailsExcelDownloadConstants.SWITCH_UNIT, getAttributeValue(switchUnit, orderProductComponentAttributes));
        switchUnitBook.setOrder(3);
        switchUnitBook.setGscQuoteDetailId(0);
        listBook.add(switchUnitBook);

        Integer circuitUnit = attributesMap.get(GscConstants.CIRCT_GR_CD_RERT);
        ExcelBean circuitUnitBook = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_ATTRIBUTES_DETAILS,
                OrderDetailsExcelDownloadConstants.CIRCUIT_UNIT, getAttributeValue(circuitUnit, orderProductComponentAttributes));

        circuitUnitBook.setOrder(3);
        circuitUnitBook.setGscQuoteDetailId(0);
        listBook.add(circuitUnitBook);
    }


    /**
     * Get sip trunk attributes in lr export
     *  @param listBook
     * @param orderToLe
     * @param multiMacdTrunkDataType
     */
    private void getSIPTrunkAttributes(List<ExcelBean> listBook, OrderToLe orderToLe, String accessType, String multiMacdTrunkDataType) {
        OrdersLeAttributeValue ordersLeAttributeValue = null;
        
        List<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository.findByOrderIDAndMstOmsAttributeName(orderToLe.getOrder().getId(), LeAttributesConstants.MULTI_MACD_GSC_SERVICE_DETAILS);
        if (Objects.nonNull(ordersLeAttributeValues) && !ordersLeAttributeValues.isEmpty()){

            ordersLeAttributeValue = ordersLeAttributeValues.stream().findFirst().get();
        }
        if (Objects.nonNull(ordersLeAttributeValue) && Objects.nonNull(ordersLeAttributeValue.getAttributeValue()) && Objects.nonNull(accessType)) {
            AdditionalServiceParams additionalServiceParams = additionalServiceParamRepository.findById(Integer.valueOf(ordersLeAttributeValue.getAttributeValue())).get();
            LOGGER.info("additionalServiceParams ID :: {}", additionalServiceParams.getId());
            if(Objects.nonNull(multiMacdTrunkDataType) && "Interconnect".equalsIgnoreCase(multiMacdTrunkDataType)){
                List<GscWholesaleInterconnectBean> gscWholesaleInterconnectBeans = Utils.fromJson(additionalServiceParams.getValue(), new TypeReference<List<GscWholesaleInterconnectBean>>() {
                });
                AtomicInteger count = new AtomicInteger(0);
                if (!CollectionUtils.isEmpty(gscWholesaleInterconnectBeans)) {
                    gscWholesaleInterconnectBeans.stream().forEach(gscWholesaleInterconnectBean -> {
                        count.getAndIncrement();
                        ExcelBean interConnectId = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS + ":" + OrderDetailsExcelDownloadConstants.INTERCONNECT_ATTRIBUTES + ":" + count.toString(),
                                OrderDetailsExcelDownloadConstants.INTERCONNECT_ID,
                                gscWholesaleInterconnectBean.getInterconnectId());
                        interConnectId.setOrder(3);
                        interConnectId.setGscQuoteDetailId(0);
                        listBook.add(interConnectId);
                        ExcelBean interConnectName = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS + ":" + OrderDetailsExcelDownloadConstants.INTERCONNECT_ATTRIBUTES + ":" + count.toString(),
                                OrderDetailsExcelDownloadConstants.INTERCONNECT_NAME,
                                gscWholesaleInterconnectBean.getInterconnectName());
                        interConnectName.setOrder(3);
                        interConnectName.setGscQuoteDetailId(0);
                        listBook.add(interConnectName);
                    });
                }
            }
            else{
                List<GscMultiMacdServiceBean> gscMultiMacdServiceBeans = Utils.fromJson(additionalServiceParams.getValue(), new TypeReference<List<GscMultiMacdServiceBean>>() {
                });
                AtomicInteger count = new AtomicInteger(0);
                if (!CollectionUtils.isEmpty(gscMultiMacdServiceBeans)) {
                    gscMultiMacdServiceBeans.stream().forEach(gscMultiMacdServiceBean -> {
                        count.getAndIncrement();
                        ExcelBean circuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS + ":" + OrderDetailsExcelDownloadConstants.SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                                OrderDetailsExcelDownloadConstants.CIRCUIT_ID,
                                gscMultiMacdServiceBean.getCircuitID());
                        circuitId.setOrder(3);
                        circuitId.setGscQuoteDetailId(0);
                        listBook.add(circuitId);
                        ExcelBean customerIP = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS + ":" + OrderDetailsExcelDownloadConstants.SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                                OrderDetailsExcelDownloadConstants.CUST_IP_ADDRESS,
                                gscMultiMacdServiceBean.getIpAddress());
                        customerIP.setOrder(3);
                        customerIP.setGscQuoteDetailId(0);
                        listBook.add(customerIP);
                        ExcelBean circuitUnit = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS + ":" + OrderDetailsExcelDownloadConstants.SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                                OrderDetailsExcelDownloadConstants.SIP_CIRCUIT_UNIT,
                                gscMultiMacdServiceBean.getSipTrunkGroup());
                        circuitUnit.setOrder(3);
                        circuitUnit.setGscQuoteDetailId(0);
                        listBook.add(circuitUnit);
                        ExcelBean switchingUnit = new ExcelBean(OrderDetailsExcelDownloadConstants.GSC_SIP_ATTRIBUTES_DETAILS + ":" + OrderDetailsExcelDownloadConstants.SIP_TRUNK_ATTRIBUTES + ":" + count.toString(),
                                OrderDetailsExcelDownloadConstants.SWITCHING_UNIT,
                                gscMultiMacdServiceBean.getTclSwitch());
                        switchingUnit.setOrder(3);
                        switchingUnit.setGscQuoteDetailId(0);
                        listBook.add(switchingUnit);
                    });
                }
            }
        }
    }
}
