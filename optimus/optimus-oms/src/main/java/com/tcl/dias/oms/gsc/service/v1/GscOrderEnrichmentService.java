package com.tcl.dias.oms.gsc.service.v1;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.DomesticVoiceAddressDetail;
import com.tcl.dias.common.beans.GscAddressDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.gsc.beans.GscLocationBean;
import com.tcl.dias.oms.gsc.beans.GscProductAddressResponse;
import com.tcl.dias.oms.gsc.beans.excel.ACANSSheetBean;
import com.tcl.dias.oms.gsc.beans.excel.ACDTFSSheetBean;
import com.tcl.dias.oms.gsc.beans.excel.DomesticVoiceSheetBean;
import com.tcl.dias.oms.gsc.beans.excel.ITFSSheetBean;
import com.tcl.dias.oms.gsc.beans.excel.LNSSheetBean;
import com.tcl.dias.oms.gsc.beans.excel.OrderEnrichmentExcel;
import com.tcl.dias.oms.gsc.beans.excel.UIFNSheetBean;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscIsoCountries;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import io.vavr.Tuple;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tcl.dias.common.constants.CommonConstants.COLON;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CITY_WISE_AREA_CODE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CITY_WISE_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CITY_WISE_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_NUMBER_COUNT;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_REGISTRATION_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.EMERGENCY_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ITFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.LNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.REQUIRED_PORTING_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;
import static com.tcl.dias.oms.gsc.util.GscConstants.UIFN;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.ACANS_HIDDEN_SHEET;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.CITY;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.DESTINATION_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.LNS_CITY_NPA_SHEET;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.LNS_HIDDEN_SHEET;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.LNS_NPA_HIDDEN_SHEET;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.LOCALITY;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.NOT_APPLICABLE;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.NPA;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.ORIGIN_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.OUTPULSE_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.PORTED_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.PORTED_STATUS;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.POSTAL_CODE;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.QUANTITY_REQUIRED;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.REGISTRATION_NUMBER;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.STATE;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.STREET_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscOrderEnrichmentExcelConstants.SUIT;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Service class for GSC Order Enrichment Bulk Upload
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class GscOrderEnrichmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscOrderEnrichmentService.class);

    private static Row.MissingCellPolicy xRow;

    private DataFormatter dataFormatter = null;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderGscRepository orderGscRepository;

    @Autowired
    OrderGscDetailRepository orderGscDetailRepository;

    @Autowired
    OrderProductComponentRepository orderProductComponentRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;

    @Value("${gsc.product.country.city.queue}")
    String getCitiesQueue;

    @Value("${gsc.product.address.queue}")
    String getAddressQueue;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    GscIsoCountries gscIsoCountries;

    @Autowired
    OrderGscTfnRepository orderGscTfnRepository;


    /**
     * Excel Upload
     *
     * @param file
     * @return CommonValidationResponse
     * @throws Exception
     */
    @Transactional
    public CommonValidationResponse excelBulkUpload(Integer orderId, MultipartFile file) throws Exception {
        CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
        try {
            Order order = orderRepository.findById(orderId).get();
            XSSFWorkbook workbook = basicValidation(order, file, commonValidationResponse);
            Map<String, List<OrderEnrichmentExcel>> orderEnrichmentData = loadExcelDataToBean(workbook);
            deepValidation(orderEnrichmentData, commonValidationResponse);
            deleteOrderEnrichmentAttribute(order);
            processOrderEnrichmentAttribute(order, orderEnrichmentData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(commonValidationResponse.getValidationMessage());
        }
        commonValidationResponse.setStatus(true);
        commonValidationResponse.setValidationMessage("Uploaded Successfully");
        return commonValidationResponse;
    }

    private void deleteOrderEnrichmentAttribute(Order order) {
        // delete all the existing attributes and order_gsc_tfn data
        List<OrderGsc> orderGscs = orderGscRepository.findByOrderToLe(order.getOrderToLes().stream().findFirst().get());
        Map<String, ProductAttributeMaster> productAttributeMasters = collectProductAttributeMaster();
        orderGscs.stream().forEach(orderGsc -> orderGsc.getOrderGscDetails().stream().forEach(orderGscDetail -> {
            // 1. order_gsc_tfn table delete
            orderGscTfnRepository.deleteByOrderGscDetail(orderGscDetail);
            // 2. delete accessType component attribute entry
            OrderProductComponent accessTypecomponent = getOrderProductComponent(orderGscDetail, orderGsc.getAccessType());
            productAttributeMasters.entrySet().stream().forEach(attribute -> {
                ProductAttributeMaster productAttributeMaster = attribute.getValue();
                List<OrderProductComponentsAttributeValue> list = orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(accessTypecomponent, productAttributeMaster);
                orderProductComponentsAttributeValueRepository.deleteAll(list);
            });
            // 3. delete productName component attribute entry
            OrderProductComponent productComponent = getOrderProductComponent(orderGscDetail, orderGsc.getProductName());
            productAttributeMasters.entrySet().stream().forEach(attribute -> {
                ProductAttributeMaster productAttributeMaster = attribute.getValue();
                List<OrderProductComponentsAttributeValue> list = orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(productComponent, productAttributeMaster);
                orderProductComponentsAttributeValueRepository.deleteAll(list);
            });
        }));
    }

    private void processOrderEnrichmentAttribute(Order order, Map<String, List<OrderEnrichmentExcel>> orderEnrichmentData) {
        List<OrderGsc> orderGscs = orderGscRepository.findByOrderToLe(order.getOrderToLes().stream().findFirst().get());
        Map<String, ProductAttributeMaster> productAttributeMasters = collectProductAttributeMaster();
        orderGscs.stream().forEach(orderGsc -> {
            orderGscDetailRepository.findByorderGsc(orderGsc).stream().forEach(orderGscDetail -> {
                String productName = orderGsc.getProductName();
                List<OrderEnrichmentExcel> orderEnrichmentExcels = orderEnrichmentData.get(productName);
                if (ITFS.equalsIgnoreCase(productName)) {
                    List<ITFSSheetBean> itfsSheetBeans = convertToClass(ITFSSheetBean.class, orderEnrichmentExcels);
                    Map<String, List<ITFSSheetBean>> itfsMap = convertITFSByOriginCountry(itfsSheetBeans);
                    saveOrderProductComponentAttributeValuesForITFS(orderGscDetail, orderGsc.getAccessType(), itfsMap, productAttributeMasters);
                } else if (LNS.equalsIgnoreCase(productName)) {
                    List<LNSSheetBean> lnsSheetBeans = convertToClass(LNSSheetBean.class, orderEnrichmentExcels);
                    Map<String, List<LNSSheetBean>> lnsMap = convertLNSSByOriginCountry(lnsSheetBeans);
                    saveOrderProductComponentAttributeValuesForLNS(orderGscDetail, orderGsc.getAccessType(), orderGsc.getProductName(), lnsMap, productAttributeMasters);
                } else if (UIFN.equalsIgnoreCase(productName)) {
                    List<UIFNSheetBean> uifnSheetBeans = convertToClass(UIFNSheetBean.class, orderEnrichmentExcels);
                    Map<String, List<UIFNSheetBean>> uifnMap = convertUIFNByOriginCountry(uifnSheetBeans);
                    // TODO Single Save for UIFN alone
                    saveOrderProductComponentAttributeValuesForUIFN(orderGscDetail, orderGsc.getAccessType(), uifnMap, productAttributeMasters);
                } else if (ACDTFS.equalsIgnoreCase(productName)) {
                    List<ACDTFSSheetBean> acdtfsSheetBeans = convertToClass(ACDTFSSheetBean.class, orderEnrichmentExcels);
                    Map<String, List<ACDTFSSheetBean>> acdtfsMap = convertACDTFSByOriginCountry(acdtfsSheetBeans);
                    saveOrderProductComponentAttributeValuesForACDTFS(orderGscDetail, orderGsc.getAccessType(), acdtfsMap, productAttributeMasters);
                } else if (ACANS.equalsIgnoreCase(productName)) {
                    List<ACANSSheetBean> acansSheetBeans = convertToClass(ACANSSheetBean.class, orderEnrichmentExcels);
                    Map<String, List<ACANSSheetBean>> acansMap = convertACANSByOriginCountry(acansSheetBeans);
                    saveOrderProductComponentAttributeValuesForACANS(orderGscDetail, orderGsc.getAccessType(), orderGsc.getProductName(), acansMap, productAttributeMasters);
                } else if (DOMESTIC_VOICE.equalsIgnoreCase(productName)) {
                    List<DomesticVoiceSheetBean> domesticVoiceSheetBeans = convertToClass(DomesticVoiceSheetBean.class, orderEnrichmentExcels);
                    Map<String, List<DomesticVoiceSheetBean>> domesticVoiceMap = convertDomesticVoiceByOriginCountry(domesticVoiceSheetBeans);
                    saveOrderProductComponentAttributeValuesForDomesticVoice(orderGscDetail, orderGsc.getAccessType(), orderGsc.getProductName(), domesticVoiceMap, productAttributeMasters);
                }
            });
        });
    }

    private Map<String, List<ITFSSheetBean>> convertITFSByOriginCountry(List<ITFSSheetBean> itfsSheetBeans) {
        return itfsSheetBeans.stream().collect(Collectors.groupingBy(ITFSSheetBean::getOriginCountry));
    }

    private Map<String, List<LNSSheetBean>> convertLNSSByOriginCountry(List<LNSSheetBean> lnsSheetBeans) {
        return lnsSheetBeans.stream().collect(Collectors.groupingBy(LNSSheetBean::getOriginCountry));
    }

    private Map<String, List<UIFNSheetBean>> convertUIFNByOriginCountry(List<UIFNSheetBean> uifnSheetBeans) {
        return uifnSheetBeans.stream().collect(Collectors.groupingBy(UIFNSheetBean::getOriginCountry));
    }

    private Map<String, List<ACDTFSSheetBean>> convertACDTFSByOriginCountry(List<ACDTFSSheetBean> acdtfsSheetBeans) {
        return acdtfsSheetBeans.stream().collect(Collectors.groupingBy(ACDTFSSheetBean::getOriginCountry));
    }

    private Map<String, List<ACANSSheetBean>> convertACANSByOriginCountry(List<ACANSSheetBean> acansSheetBeans) {
        return acansSheetBeans.stream().collect(Collectors.groupingBy(ACANSSheetBean::getOriginCountry));
    }

    private Map<String, List<DomesticVoiceSheetBean>> convertDomesticVoiceByOriginCountry(List<DomesticVoiceSheetBean> domesticVoiceSheetBeans) {
        return domesticVoiceSheetBeans.stream().collect(Collectors.groupingBy(DomesticVoiceSheetBean::getOriginCountry));
    }

    private void saveOrderProductComponentAttributeValuesForITFS(OrderGscDetail orderGscDetail, String accessType, Map<String,
            List<ITFSSheetBean>> itfsMapByOriginCountry, Map<String, ProductAttributeMaster> productAttributeMasters) {
        OrderProductComponent component = getOrderProductComponent(orderGscDetail, accessType);
        List<ITFSSheetBean> itfsSheetBeans = itfsMapByOriginCountry.get(orderGscDetail.getSrc());
        itfsSheetBeans.stream().forEach(itfsSheetBean -> {
            // termination number
            OrderProductComponentsAttributeValue terminationNumberAttribute = new OrderProductComponentsAttributeValue();
            terminationNumberAttribute.setOrderProductComponent(component);
            terminationNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_TERMINATION_NUMBER_OUTPULSE));
            terminationNumberAttribute.setAttributeValues(itfsSheetBean.getOutPulseNumber());
            terminationNumberAttribute.setDisplayValue(itfsSheetBean.getOutPulseNumber());
            orderProductComponentsAttributeValueRepository.save(terminationNumberAttribute);

            // porting number
            if (!NOT_APPLICABLE.equalsIgnoreCase(itfsSheetBean.getPortedNumber())) {
                OrderProductComponentsAttributeValue portingNumberAttribute = new OrderProductComponentsAttributeValue();
                portingNumberAttribute.setOrderProductComponent(component);
                portingNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(REQUIRED_PORTING_NUMBERS));
                portingNumberAttribute.setAttributeValues(itfsSheetBean.getPortedNumber());
                portingNumberAttribute.setDisplayValue(itfsSheetBean.getPortedNumber());
                orderProductComponentsAttributeValueRepository.save(portingNumberAttribute);

                OrderGscTfn orderGscTfn = new OrderGscTfn();
                orderGscTfn.setOrderGscDetail(orderGscDetail);
                orderGscTfn.setTfnNumber(itfsSheetBean.getPortedNumber());
                orderGscTfn.setIsPorted(GscConstants.STATUS_ACTIVE);
                orderGscTfn.setCreatedBy(Utils.getSource());
                orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
                orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
                orderGscTfn.setAction("RESERVE");
                orderGscTfn.setCountryCode(gscIsoCountries.forName(orderGscDetail.getSrc()).getCode());

                orderGscTfnRepository.save(orderGscTfn);
            }
        });
    }

    private void saveOrderProductComponentAttributeValuesForLNS(OrderGscDetail orderGscDetail, String accessType, String productName, Map<String,
            List<LNSSheetBean>> lnsMapByOriginCountry, Map<String, ProductAttributeMaster> productAttributeMasters) {
        OrderProductComponent accessTypecomponent = getOrderProductComponent(orderGscDetail, accessType);
        OrderProductComponent productTypeComponent = getOrderProductComponent(orderGscDetail, productName);
        Map<String, String> allCityCode = getAllCityCode(orderGscDetail.getSrc(), LNS);
        List<LNSSheetBean> lnsSheetBeans = lnsMapByOriginCountry.get(orderGscDetail.getSrc());
        lnsSheetBeans.stream().forEach(lnsSheetBean -> {
            // termination number
            OrderProductComponentsAttributeValue terminationNumberAttribute = new OrderProductComponentsAttributeValue();
            terminationNumberAttribute.setOrderProductComponent(accessTypecomponent);
            terminationNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_TERMINATION_NUMBER_OUTPULSE));
            terminationNumberAttribute.setAttributeValues(lnsSheetBean.getOutPulseNumber());
            terminationNumberAttribute.setDisplayValue(lnsSheetBean.getOutPulseNumber());
            orderProductComponentsAttributeValueRepository.save(terminationNumberAttribute);
            // porting number
            if (!NOT_APPLICABLE.equalsIgnoreCase(lnsSheetBean.getPortedNumber())) {
                //Porting number
                OrderGscTfn orderGscTfn = new OrderGscTfn();
                orderGscTfn.setOrderGscDetail(orderGscDetail);
                orderGscTfn.setTfnNumber(lnsSheetBean.getPortedNumber());
                orderGscTfn.setIsPorted(GscConstants.STATUS_ACTIVE);
                orderGscTfn.setCreatedBy(Utils.getSource());
                orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
                orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
                orderGscTfn.setAction("RESERVE");
                orderGscTfn.setCountryCode(":" + Objects.requireNonNull(allCityCode.get(lnsSheetBean.getCity()), "Error City not matching with country"));
                orderGscTfnRepository.save(orderGscTfn);
                //City wise Quantity Of Numbers
                OrderProductComponentsAttributeValue cityWiseQuantityAttribute = new OrderProductComponentsAttributeValue();
                cityWiseQuantityAttribute.setOrderProductComponent(productTypeComponent);
                cityWiseQuantityAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_CITY_WISE_QUANTITY_OF_NUMBERS));
                cityWiseQuantityAttribute.setAttributeValues(Objects.requireNonNull(allCityCode.get(lnsSheetBean.getCity()), "Error City not matching with country") + ":");
                orderProductComponentsAttributeValueRepository.save(cityWiseQuantityAttribute);
            } else {
                //City wise Quantity Of Numbers
                OrderProductComponentsAttributeValue cityWiseQuantityAttribute = new OrderProductComponentsAttributeValue();
                cityWiseQuantityAttribute.setOrderProductComponent(productTypeComponent);
                cityWiseQuantityAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_CITY_WISE_QUANTITY_OF_NUMBERS));
                cityWiseQuantityAttribute.setAttributeValues(Objects.requireNonNull(allCityCode.get(lnsSheetBean.getCity()), "Error City not matching with country") + ":0:");
                orderProductComponentsAttributeValueRepository.save(cityWiseQuantityAttribute);
            }
            //City wise Area Code
            OrderProductComponentsAttributeValue cityWiseAreaCodeAttribute = new OrderProductComponentsAttributeValue();
            cityWiseAreaCodeAttribute.setOrderProductComponent(productTypeComponent);
            cityWiseAreaCodeAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_CITY_WISE_AREA_CODE));
            String cityWiseAreaCode = Objects.requireNonNull(allCityCode.get(lnsSheetBean.getCity()), "Error City not matching with country") + ":" + lnsSheetBean.getNpaAreaCode();
            cityWiseAreaCodeAttribute.setAttributeValues(cityWiseAreaCode);
            orderProductComponentsAttributeValueRepository.save(cityWiseAreaCodeAttribute);

            OrderProductComponentsAttributeValue cityWisePortingServiceNeededAttribute = new OrderProductComponentsAttributeValue();
            cityWisePortingServiceNeededAttribute.setOrderProductComponent(productTypeComponent);
            cityWisePortingServiceNeededAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_CITY_WISE_PORTING_SERVICE_NEEDED));
            String cityWisePortingServiceNeeded = Objects.requireNonNull(allCityCode.get(lnsSheetBean.getCity()), "Error City not matching with country") + ":Yes";
            cityWisePortingServiceNeededAttribute.setAttributeValues(cityWisePortingServiceNeeded);
            orderProductComponentsAttributeValueRepository.save(cityWisePortingServiceNeededAttribute);
        });
    }

    private Map<String, String> getAllCityCode(String country, String type) {
        Map<String, String> countryCitycodeMap = new HashMap<>();
        try {
            String response = (String) mqUtils.sendAndReceive(getCitiesQueue, type.concat(COLON).concat(country));
            GscLocationBean gscProductLocationBean = GscUtils.fromJson(response, GscLocationBean.class);
            Set<Set<String>> npaCodes = new HashSet<>();
            gscProductLocationBean.getSources().stream().forEach(gscProductLocationDetailBean -> {
                String city = gscProductLocationDetailBean.getName();
                String cityCode = gscProductLocationDetailBean.getCode();
                if (Objects.nonNull(city) && StringUtils.isNoneEmpty(city) && Objects.nonNull(cityCode)
                        && StringUtils.isNoneEmpty(cityCode)) {
                    countryCitycodeMap.put(city, cityCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new TclCommonRuntimeException("Error in retriving city code");
        }
        return countryCitycodeMap;
    }

    private void saveOrderProductComponentAttributeValuesForUIFN(OrderGscDetail orderGscDetail, String accessType, Map<String,
            List<UIFNSheetBean>> uifnMapByOriginCountry, Map<String, ProductAttributeMaster> productAttributeMasters) {
        OrderProductComponent component = getOrderProductComponent(orderGscDetail, accessType);
        List<UIFNSheetBean> uifnSheetBeans = uifnMapByOriginCountry.get(orderGscDetail.getSrc());
        uifnSheetBeans.stream().forEach(uifnSheetBean -> {
            // termination number
            OrderProductComponentsAttributeValue terminationNumberAttribute = new OrderProductComponentsAttributeValue();
            terminationNumberAttribute.setOrderProductComponent(component);
            terminationNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_TERMINATION_NUMBER_OUTPULSE));
            terminationNumberAttribute.setAttributeValues(uifnSheetBean.getOutPulseNumber());
            terminationNumberAttribute.setDisplayValue(uifnSheetBean.getOutPulseNumber());
            orderProductComponentsAttributeValueRepository.save(terminationNumberAttribute);

            // porting number
            if (!NOT_APPLICABLE.equalsIgnoreCase(uifnSheetBean.getPortedNumber())) {
                OrderProductComponentsAttributeValue portingNumberAttribute = new OrderProductComponentsAttributeValue();
                portingNumberAttribute.setOrderProductComponent(component);
                portingNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(REQUIRED_PORTING_NUMBERS));
                portingNumberAttribute.setAttributeValues(uifnSheetBean.getPortedNumber());
                portingNumberAttribute.setDisplayValue(uifnSheetBean.getPortedNumber());
                orderProductComponentsAttributeValueRepository.save(portingNumberAttribute);

                OrderGscTfn orderGscTfn = new OrderGscTfn();
                orderGscTfn.setOrderGscDetail(orderGscDetail);
                orderGscTfn.setTfnNumber(uifnSheetBean.getPortedNumber());
                orderGscTfn.setIsPorted(GscConstants.STATUS_ACTIVE);
                orderGscTfn.setCreatedBy(Utils.getSource());
                orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
                orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
                orderGscTfn.setAction("RESERVE");
                orderGscTfn.setCountryCode(gscIsoCountries.forName(orderGscDetail.getSrc()).getCode());
                orderGscTfnRepository.save(orderGscTfn);
            }
        });
    }

    private void saveOrderProductComponentAttributeValuesForACDTFS(OrderGscDetail orderGscDetail, String accessType, Map<String,
            List<ACDTFSSheetBean>> acdtfsMapByOriginCountry, Map<String, ProductAttributeMaster> productAttributeMasters) {
        OrderProductComponent component = getOrderProductComponent(orderGscDetail, accessType);
        List<ACDTFSSheetBean> acdtfsSheetBeans = acdtfsMapByOriginCountry.get(orderGscDetail.getSrc());
        acdtfsSheetBeans.stream().forEach(acdtfsSheetBean -> {
            OrderProductComponentsAttributeValue terminationNumberAttribute = new OrderProductComponentsAttributeValue();
            terminationNumberAttribute.setOrderProductComponent(component);
            terminationNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_TERMINATION_NUMBER_OUTPULSE));
            terminationNumberAttribute.setAttributeValues(acdtfsSheetBean.getOutPulseNumber());
            terminationNumberAttribute.setDisplayValue(acdtfsSheetBean.getOutPulseNumber());
            orderProductComponentsAttributeValueRepository.save(terminationNumberAttribute);
        });
    }

    private void saveOrderProductComponentAttributeValuesForACANS(OrderGscDetail orderGscDetail, String accessType, String productName, Map<String,
            List<ACANSSheetBean>> acansMapByOriginCountry, Map<String, ProductAttributeMaster> productAttributeMasters) {
        OrderProductComponent accessTypecomponent = getOrderProductComponent(orderGscDetail, accessType);
        OrderProductComponent productTypeComponent = getOrderProductComponent(orderGscDetail, productName);
        Map<String, String> allCityCode = getAllCityCode(orderGscDetail.getSrc(), ACANS);
        List<ACANSSheetBean> acansSheetBeans = acansMapByOriginCountry.get(orderGscDetail.getSrc());
        acansSheetBeans.stream().forEach(acansSheetBean -> {
            // termination number
            OrderProductComponentsAttributeValue terminationNumberAttribute = new OrderProductComponentsAttributeValue();
            terminationNumberAttribute.setOrderProductComponent(accessTypecomponent);
            terminationNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_TERMINATION_NUMBER_OUTPULSE));
            terminationNumberAttribute.setAttributeValues(acansSheetBean.getOutPulseNumber());
            terminationNumberAttribute.setDisplayValue(acansSheetBean.getOutPulseNumber());
            orderProductComponentsAttributeValueRepository.save(terminationNumberAttribute);
            //City wise Quantity Of Numbers
            OrderProductComponentsAttributeValue cityWiseQuantityAttribute = new OrderProductComponentsAttributeValue();
            cityWiseQuantityAttribute.setOrderProductComponent(productTypeComponent);
            cityWiseQuantityAttribute.setProductAttributeMaster(productAttributeMasters.get(ATTR_CITY_WISE_QUANTITY_OF_NUMBERS));
            cityWiseQuantityAttribute.setAttributeValues(Objects.requireNonNull(allCityCode.get(acansSheetBean.getCity()), "Error city not matched with cournty") + ":0:");
            orderProductComponentsAttributeValueRepository.save(cityWiseQuantityAttribute);
        });
    }

    private void saveOrderProductComponentAttributeValuesForDomesticVoice(OrderGscDetail orderGscDetail, String accessType, String productName, Map<String,
            List<DomesticVoiceSheetBean>> domesticVoiceMapByOriginCountry, Map<String, ProductAttributeMaster> productAttributeMasters) {
        try {
            OrderProductComponent accessTypecomponent = getOrderProductComponent(orderGscDetail, accessType);
            OrderProductComponent productTypeComponent = getOrderProductComponent(orderGscDetail, productName);
            List<DomesticVoiceSheetBean> domesticVoiceSheetBeans = domesticVoiceMapByOriginCountry.get(orderGscDetail.getSrc());
            domesticVoiceSheetBeans.stream().forEach(domesticVoiceSheetBean -> {
                // porting number
                if (!NOT_APPLICABLE.equalsIgnoreCase(domesticVoiceSheetBean.getPortedNumber())) {
                    OrderProductComponentsAttributeValue portingNumberAttribute = new OrderProductComponentsAttributeValue();
                    portingNumberAttribute.setOrderProductComponent(accessTypecomponent);
                    portingNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(REQUIRED_PORTING_NUMBERS));
                    portingNumberAttribute.setAttributeValues(domesticVoiceSheetBean.getPortedNumber());
                    portingNumberAttribute.setDisplayValue(domesticVoiceSheetBean.getPortedNumber());
                    orderProductComponentsAttributeValueRepository.save(portingNumberAttribute);

                    OrderGscTfn orderGscTfn = new OrderGscTfn();
                    orderGscTfn.setOrderGscDetail(orderGscDetail);
                    orderGscTfn.setTfnNumber(domesticVoiceSheetBean.getPortedNumber());
                    orderGscTfn.setIsPorted(GscConstants.STATUS_ACTIVE);
                    orderGscTfn.setCreatedBy(Utils.getSource());
                    orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
                    orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
                    orderGscTfn.setAction("RESERVE");
                    orderGscTfn.setCountryCode(gscIsoCountries.forName(orderGscDetail.getSrc()).getCode());
                    orderGscTfnRepository.save(orderGscTfn);
                }

            });
            //Location Service MQ call - Save address details
            GscAddressDetailBean gscAddressDetailBean = convertToGscAddressDetailBeans(domesticVoiceSheetBeans);
            String requestData = Utils.convertObjectToJson(gscAddressDetailBean);
            String response = (String) mqUtils.sendAndReceive(getAddressQueue, requestData);
            LOGGER.info("Location service responded >> " + response);
            GscProductAddressResponse gscProductAddressResponse = Utils.convertJsonToObject(response, GscProductAddressResponse.class);
            List<Integer> addressIds = gscProductAddressResponse.getAddressIds();
            List<Long> uniqueAddressCount = gscProductAddressResponse.getUniqueAddressCount();

            for (int i = 0; i < addressIds.size(); i++) {
                //Address
                OrderProductComponentsAttributeValue domesticVoiceAddressDetail = new OrderProductComponentsAttributeValue();
                domesticVoiceAddressDetail.setOrderProductComponent(productTypeComponent);
                domesticVoiceAddressDetail.setProductAttributeMaster(productAttributeMasters.get(EMERGENCY_ADDRESS));
                domesticVoiceAddressDetail.setAttributeValues(addressIds.get(i) + ":" + uniqueAddressCount.get(i));
                orderProductComponentsAttributeValueRepository.save(domesticVoiceAddressDetail);

                //registration number
                DomesticVoiceSheetBean domesticVoiceSheetBean = domesticVoiceSheetBeans.get(i);
                OrderProductComponentsAttributeValue registrationNumberAttribute = new OrderProductComponentsAttributeValue();
                registrationNumberAttribute.setOrderProductComponent(productTypeComponent);
                registrationNumberAttribute.setProductAttributeMaster(productAttributeMasters.get(REGISTRATION_NUMBER));
                registrationNumberAttribute.setAttributeValues(addressIds.get(i) + ":" + domesticVoiceSheetBean.getRegistrationNumber());
                registrationNumberAttribute.setDisplayValue(domesticVoiceSheetBean.getRegistrationNumber());
                orderProductComponentsAttributeValueRepository.save(registrationNumberAttribute);
            }
        } catch (TclCommonException e) {
            LOGGER.error("Error in saving Domestic voice data" + ExceptionUtils.getStackTrace(e));
            throw new TclCommonRuntimeException("Error in saving Domestic voice data");
        }
    }

    private GscAddressDetailBean convertToGscAddressDetailBeans(List<DomesticVoiceSheetBean> domesticVoiceSheetBeans) {
        GscAddressDetailBean gscAddressDetailBean = new GscAddressDetailBean();
        List<DomesticVoiceAddressDetail> domesticVoiceAddressDetails = new ArrayList<>();
        domesticVoiceSheetBeans.stream().forEach(domesticVoiceSheetBean -> {
            DomesticVoiceAddressDetail domesticVoiceAddressDetail = new DomesticVoiceAddressDetail();
            domesticVoiceAddressDetail.setCountry(domesticVoiceSheetBean.getOriginCountry());
            domesticVoiceAddressDetail.setCity(domesticVoiceSheetBean.getCity());
            domesticVoiceAddressDetail.setAddress(domesticVoiceSheetBean.getStreetAddress());
            domesticVoiceAddressDetail.setLocality(domesticVoiceSheetBean.getLocality());
            domesticVoiceAddressDetail.setFloor(domesticVoiceSheetBean.getSuit());
            domesticVoiceAddressDetail.setState(domesticVoiceSheetBean.getState());
            domesticVoiceAddressDetail.setPostalCode(domesticVoiceSheetBean.getPostalCode());
            domesticVoiceAddressDetails.add(domesticVoiceAddressDetail);
        });
        gscAddressDetailBean.setDomesticVoiceAddressDetailsList(domesticVoiceAddressDetails);
        return gscAddressDetailBean;
    }

    private Map<String, ProductAttributeMaster> collectProductAttributeMaster() {
        Map<String, ProductAttributeMaster> productAttributeMasters = new HashMap<>();
        productAttributeMasters.put(ATTR_TERMINATION_NUMBER_OUTPULSE, getProductAttributeMaster(ATTR_TERMINATION_NUMBER_OUTPULSE));
        productAttributeMasters.put(REQUIRED_PORTING_NUMBERS, getProductAttributeMaster(REQUIRED_PORTING_NUMBERS));
        productAttributeMasters.put(ATTR_CITY_WISE_AREA_CODE, getProductAttributeMaster(ATTR_CITY_WISE_AREA_CODE));
        productAttributeMasters.put(ATTR_REGISTRATION_NUMBER, getProductAttributeMaster(ATTR_REGISTRATION_NUMBER));
        productAttributeMasters.put(EMERGENCY_ADDRESS, getProductAttributeMaster(EMERGENCY_ADDRESS));
        productAttributeMasters.put(ATTR_PORTING_SERVICE_NEEDED, getProductAttributeMaster(ATTR_PORTING_SERVICE_NEEDED));
        productAttributeMasters.put(ATTR_CITY_WISE_QUANTITY_OF_NUMBERS, getProductAttributeMaster(ATTR_CITY_WISE_QUANTITY_OF_NUMBERS));
        productAttributeMasters.put(ATTR_CITY_WISE_PORTING_SERVICE_NEEDED, getProductAttributeMaster(ATTR_CITY_WISE_PORTING_SERVICE_NEEDED));
        return productAttributeMasters;
    }

    private ProductAttributeMaster getProductAttributeMaster(String attributeName) {
        ProductAttributeMaster gscReferenceOrderAttribute = Optional
                .ofNullable(productAttributeMasterRepository.findByNameAndStatus(attributeName,
                        GscConstants.STATUS_ACTIVE))
                .orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
                    LOGGER.warn("Unable to find attribute master with name: {}", attributeName);
                    return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
                });
        return gscReferenceOrderAttribute;
    }

    private OrderProductComponent getOrderProductComponent(OrderGscDetail orderGscDetail, String accessType) {
        MstProductComponent mstProductComponent = mstProductComponentRepository.findByNameAndStatus(accessType, GscConstants.STATUS_ACTIVE).get(0);

        OrderProductComponent component = orderProductComponentRepository
                .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                        GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE).stream().findFirst().get();
        return component;
    }

    static <T> List<T> convertToClass(Class<T> clazz, List<?> items) {
        return items.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    private XSSFWorkbook basicValidation(Order order, MultipartFile file, CommonValidationResponse commonValidationResponse) throws TclCommonException, IOException, InvalidFormatException {
        // TODO if basic validation pass,
        // then save the uploaded file in directory (attachment)
        // then read the file from there as - { new File("path") }
        //Order order = orderRepository.findById(orderId).get();
        String fileName = file.getOriginalFilename();
        if (!fileName.contains(order.getOrderCode())) {
            LOGGER.info("Template File Name Changed!!, Please do not change the file name.");
            commonValidationResponse.setStatus(false);
            commonValidationResponse.setValidationMessage("Template File Name Changed!!, Please do not change the file name.");
            throw new TclCommonException("Template File Name Changed!!, Please do not change the file name.", ResponseResource.R_CODE_ERROR);
        }
        // TODO Using InputStream increase memory - need to remove
        OPCPackage pkg = OPCPackage.open(file.getInputStream());
        XSSFWorkbook workbook = new XSSFWorkbook(pkg);

        List<String> gscServices = Arrays.asList(ITFS, LNS, UIFN, ACANS, ACDTFS, DOMESTIC_VOICE,
                ACANS_HIDDEN_SHEET, LNS_HIDDEN_SHEET, LNS_NPA_HIDDEN_SHEET, LNS_CITY_NPA_SHEET);
        for (Sheet sheet : workbook) {
            if (!gscServices.contains(sheet.getSheetName())) {
                LOGGER.info("Template Sheet Name Not Matching!! Please do not change the sheet name or do not add new sheet.");
                commonValidationResponse.setStatus(false);
                commonValidationResponse.setValidationMessage("Template Sheet Name Not Matching!! Please do not change the sheet name or do not add new sheet.");
                //throw new TclCommonException("Template Sheet Name Not Matching!! Please do not change the sheet name or do not add new sheet.", ResponseResource.R_CODE_ERROR);
            }
        }
        pkg.close();
        return workbook;
    }

    /**
     * loadExcelDataToBean - this method reads the excel value and pass for data
     * validations.
     */
    public Map<String, List<OrderEnrichmentExcel>> loadExcelDataToBean(XSSFWorkbook workbook) throws InvalidFormatException, IOException, TclCommonException {
        final Map<String, List<OrderEnrichmentExcel>> orderEnrichmentData = new LinkedHashMap<>();
        try {
            for (Sheet sheet : workbook) {
                switch (sheet.getSheetName()) {
                    case ITFS:
                        final List<OrderEnrichmentExcel> itfsBean = extractITFSSheet((XSSFSheet) sheet);
                        orderEnrichmentData.put(ITFS, itfsBean);
                        break;
                    case LNS:
                        final List<OrderEnrichmentExcel> lnsBean = extractLNSSheet((XSSFSheet) sheet);
                        orderEnrichmentData.put(LNS, lnsBean);
                        break;
                    case UIFN:
                        final List<OrderEnrichmentExcel> uifnBean = extractUIFNSheet((XSSFSheet) sheet);
                        orderEnrichmentData.put(UIFN, uifnBean);
                        break;
                    case ACANS:
                        final List<OrderEnrichmentExcel> acansBean = extractACANSSheet((XSSFSheet) sheet);
                        orderEnrichmentData.put(ACANS, acansBean);
                        break;
                    case ACDTFS:
                        final List<OrderEnrichmentExcel> acdtfaBean = extractACDTFSSheet((XSSFSheet) sheet);
                        orderEnrichmentData.put(ACDTFS, acdtfaBean);
                        break;
                    case DOMESTIC_VOICE:
                        final List<OrderEnrichmentExcel> domesticVoiceBean = extractDomesticVoiceSheet((XSSFSheet) sheet);
                        orderEnrichmentData.put(DOMESTIC_VOICE, domesticVoiceBean);
                        break;
                    default:
                        break;
                }
            }
            workbook.close();
        } catch (Exception e) {
            LOGGER.error("Error in Bulk upload :: {} ", e);
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return orderEnrichmentData;
    }

    private List<OrderEnrichmentExcel> extractITFSSheet(XSSFSheet sheet) {

        LOGGER.info("Loading Sheet name :: >> {} and last row num with data => {} ", sheet.getSheetName(), getLastRowWithData(sheet));

        List<OrderEnrichmentExcel> itfsSheetBeans = new ArrayList<>();

        for (int i = 1; i <= getLastRowWithData(sheet); i++) {

            ITFSSheetBean itfsSheetBean = (ITFSSheetBean) getInstance(ITFS);
            Cell originCountry = null;
            Cell destinationCountry = null;
            Cell quantityRequired = null;
            Cell portedStatus = null;
            Cell outPulseNumber = null;
            Cell portedNumber = null;

            // find destination country based on the cell count
            if (Objects.isNull(sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL))) {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                portedStatus = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
                portedNumber = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
            } else {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                destinationCountry = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                portedStatus = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
                portedNumber = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
            }

            if (Objects.nonNull(originCountry))
                itfsSheetBean.setOriginCountry(originCountry.getStringCellValue());

            if (Objects.nonNull(destinationCountry))
                itfsSheetBean.setDestinationCountry(destinationCountry.getStringCellValue());

            if (Objects.nonNull(quantityRequired))
                itfsSheetBean.setQuantityRequired(getDataFormatter().formatCellValue(quantityRequired));

            if (Objects.nonNull(portedStatus))
                itfsSheetBean.setPortedStatus(getDataFormatter().formatCellValue(portedStatus));

            if (Objects.nonNull(outPulseNumber))
                itfsSheetBean.setOutPulseNumber(getDataFormatter().formatCellValue(outPulseNumber));

            if (Objects.nonNull(portedNumber))
                itfsSheetBean.setPortedNumber(getDataFormatter().formatCellValue(portedNumber));

            itfsSheetBeans.add(itfsSheetBean);
        }

        LOGGER.info(itfsSheetBeans.toString());

        return itfsSheetBeans;
    }

    private List<OrderEnrichmentExcel> extractUIFNSheet(XSSFSheet sheet) {

        LOGGER.info("Loading Sheet name :: >> " + sheet.getSheetName() + ">> last row num with data => {} " + getLastRowWithData(sheet));

        List<OrderEnrichmentExcel> uifnSheetBeans = new ArrayList<>();
        for (int i = 1; i <= getLastRowWithData(sheet); i++) {

            UIFNSheetBean uifnSheetBean = (UIFNSheetBean) getInstance(UIFN);

            Cell originCountry = null;
            Cell destinationCountry = null;
            Cell quantityRequired = null;
            Cell portedStatus = null;
            Cell outPulseNumber = null;
            Cell portedNumber = null;

            // find destination country based on the cell count
            if (Objects.isNull(sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL))) {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                portedStatus = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
                portedNumber = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
            } else {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                destinationCountry = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                portedStatus = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
                portedNumber = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
            }

            if (Objects.nonNull(originCountry))
                uifnSheetBean.setOriginCountry(originCountry.getStringCellValue());

            if (Objects.nonNull(destinationCountry))
                uifnSheetBean.setDestinationCountry(destinationCountry.getStringCellValue());

            if (Objects.nonNull(quantityRequired))
                uifnSheetBean.setQuantityRequired(getDataFormatter().formatCellValue(quantityRequired));

            if (Objects.nonNull(portedStatus))
                uifnSheetBean.setPortedStatus(getDataFormatter().formatCellValue(portedStatus));

            if (Objects.nonNull(outPulseNumber))
                uifnSheetBean.setOutPulseNumber(getDataFormatter().formatCellValue(outPulseNumber));

            if (Objects.nonNull(portedNumber))
                uifnSheetBean.setPortedNumber(getDataFormatter().formatCellValue(portedNumber));

            uifnSheetBeans.add(uifnSheetBean);
        }
        LOGGER.info(uifnSheetBeans.toString());

        return uifnSheetBeans;
    }

    private List<OrderEnrichmentExcel> extractACANSSheet(XSSFSheet sheet) {

        LOGGER.info("Loading Sheet name :: >> " + sheet.getSheetName() + ">> last row num with data => {} " + getLastRowWithData(sheet));

        List<OrderEnrichmentExcel> acansSheetBeans = new ArrayList<>();

        for (int i = 1; i <= getLastRowWithData(sheet); i++) {

            ACANSSheetBean acansSheetBean = (ACANSSheetBean) getInstance(ACANS);
            Cell originCountry = null;
            Cell destinationCountry = null;
            Cell city = null;
            Cell quantityRequired = null;
            Cell outPulseNumber = null;

            if (Objects.isNull(sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL))) {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                city = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
            } else {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                destinationCountry = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                city = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
            }

            if (Objects.nonNull(originCountry))
                acansSheetBean.setOriginCountry(originCountry.getStringCellValue());

            if (Objects.nonNull(destinationCountry))
                acansSheetBean.setDestinationCountry(destinationCountry.getStringCellValue());

            if (Objects.nonNull(quantityRequired))
                acansSheetBean.setQuantityRequired(getDataFormatter().formatCellValue(quantityRequired));

            if (Objects.nonNull(outPulseNumber))
                acansSheetBean.setOutPulseNumber(getDataFormatter().formatCellValue(outPulseNumber));

            if (Objects.nonNull(city))
                acansSheetBean.setCity(city.getStringCellValue());

            acansSheetBeans.add(acansSheetBean);
        }
        LOGGER.info(acansSheetBeans.toString());

        return acansSheetBeans;
    }

    private List<OrderEnrichmentExcel> extractLNSSheet(XSSFSheet sheet) {

        LOGGER.info("Loading Sheet name :: >> " + sheet.getSheetName() + ">> last row num with data => {} " + getLastRowWithData(sheet));

        List<OrderEnrichmentExcel> lnsSheetBeans = new ArrayList<>();

        for (int i = 1; i <= getLastRowWithData(sheet); i++) {

            LNSSheetBean lnsSheetBean = (LNSSheetBean) getInstance(LNS);
            Cell originCountry = null;
            Cell destinationCountry = null;
            Cell city = null;
            Cell npaAreaCode = null;
            Cell quantityRequired = null;
            Cell portedStatus = null;
            Cell outPulseNumber = null;
            Cell portedNumber = null;

            if (Objects.isNull(sheet.getRow(i).getCell(7, xRow.RETURN_BLANK_AS_NULL))) {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                city = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                npaAreaCode = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
                portedStatus = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
                portedNumber = sheet.getRow(i).getCell(6, xRow.RETURN_BLANK_AS_NULL);
            } else {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                destinationCountry = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                city = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                npaAreaCode = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
                quantityRequired = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
                portedStatus = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(6, xRow.RETURN_BLANK_AS_NULL);
                portedNumber = sheet.getRow(i).getCell(7, xRow.RETURN_BLANK_AS_NULL);
            }

            if (Objects.nonNull(originCountry))
                lnsSheetBean.setOriginCountry(originCountry.getStringCellValue());

            if (Objects.nonNull(destinationCountry))
                lnsSheetBean.setDestinationCountry(destinationCountry.getStringCellValue());

            if (Objects.nonNull(city))
                lnsSheetBean.setCity(city.getStringCellValue());

            if (Objects.nonNull(npaAreaCode))
                lnsSheetBean.setNpaAreaCode(getDataFormatter().formatCellValue(npaAreaCode));

            if (Objects.nonNull(quantityRequired))
                lnsSheetBean.setQuantityRequired(getDataFormatter().formatCellValue(quantityRequired));

            if (Objects.nonNull(portedStatus))
                lnsSheetBean.setPortedStatus(getDataFormatter().formatCellValue(portedStatus));

            if (Objects.nonNull(outPulseNumber))
                lnsSheetBean.setOutPulseNumber(getDataFormatter().formatCellValue(outPulseNumber));

            if (Objects.nonNull(portedNumber))
                lnsSheetBean.setPortedNumber(getDataFormatter().formatCellValue(portedNumber));

            lnsSheetBeans.add(lnsSheetBean);
        }

        LOGGER.info(lnsSheetBeans.toString());

        return lnsSheetBeans;
    }

    private List<OrderEnrichmentExcel> extractACDTFSSheet(XSSFSheet sheet) {

        LOGGER.info("Loading Sheet name :: >> " + sheet.getSheetName() + ">> last row num with data => {} " + getLastRowWithData(sheet));

        List<OrderEnrichmentExcel> acdtfsSheetBeans = new ArrayList<>();

        for (int i = 1; i <= getLastRowWithData(sheet); i++) {

            ACDTFSSheetBean acdtfsSheetBean = (ACDTFSSheetBean) getInstance(ACDTFS);
            Cell originCountry = null;
            Cell destinationCountry = null;
            Cell qtyRequired = null;
            Cell outPulseNumber = null;

            if (Objects.isNull(sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL))) {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                qtyRequired = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
            } else {
                originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
                destinationCountry = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
                qtyRequired = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
                outPulseNumber = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
            }

            if (Objects.nonNull(originCountry))
                acdtfsSheetBean.setOriginCountry(originCountry.getStringCellValue());

            if (Objects.nonNull(destinationCountry))
                acdtfsSheetBean.setDestinationCountry(destinationCountry.getStringCellValue());

            if (Objects.nonNull(qtyRequired))
                acdtfsSheetBean.setQuantityRequired(getDataFormatter().formatCellValue(qtyRequired));

            if (Objects.nonNull(outPulseNumber))
                acdtfsSheetBean.setOutPulseNumber(getDataFormatter().formatCellValue(outPulseNumber));

            acdtfsSheetBeans.add(acdtfsSheetBean);
        }
        LOGGER.info(acdtfsSheetBeans.toString());

        return acdtfsSheetBeans;
    }

    private List<OrderEnrichmentExcel> extractDomesticVoiceSheet(XSSFSheet sheet) {

        LOGGER.info("Loading Sheet name :: >> " + sheet.getSheetName() + ">> last row num with data => {} " + getLastRowWithData(sheet));

        List<OrderEnrichmentExcel> domesticVoiceSheetBeans = new ArrayList<>();

        for (int i = 1; i <= getLastRowWithData(sheet); i++) {

            DomesticVoiceSheetBean domesticVoiceSheetBean = (DomesticVoiceSheetBean) getInstance(DOMESTIC_VOICE);

            Cell originCountry = sheet.getRow(i).getCell(0, xRow.RETURN_BLANK_AS_NULL);
            Cell city = sheet.getRow(i).getCell(1, xRow.RETURN_BLANK_AS_NULL);
            Cell address = sheet.getRow(i).getCell(2, xRow.RETURN_BLANK_AS_NULL);
            Cell locality = sheet.getRow(i).getCell(3, xRow.RETURN_BLANK_AS_NULL);
            Cell suit = sheet.getRow(i).getCell(4, xRow.RETURN_BLANK_AS_NULL);
            Cell state = sheet.getRow(i).getCell(5, xRow.RETURN_BLANK_AS_NULL);
            Cell postCode = sheet.getRow(i).getCell(6, xRow.RETURN_BLANK_AS_NULL);
            Cell registrationNumber = sheet.getRow(i).getCell(7, xRow.RETURN_BLANK_AS_NULL);
            Cell quantityRequired = sheet.getRow(i).getCell(8, xRow.RETURN_BLANK_AS_NULL);
            Cell portedStatus = sheet.getRow(i).getCell(9, xRow.RETURN_BLANK_AS_NULL);
            Cell portedNumber = sheet.getRow(i).getCell(10, xRow.RETURN_BLANK_AS_NULL);

            if (Objects.nonNull(originCountry))
                domesticVoiceSheetBean.setOriginCountry(getDataFormatter().formatCellValue(originCountry));

            if (Objects.nonNull(city))
                domesticVoiceSheetBean.setCity(getDataFormatter().formatCellValue(city));

            if (Objects.nonNull(address))
                domesticVoiceSheetBean.setStreetAddress(getDataFormatter().formatCellValue(address));

            if (Objects.nonNull(locality))
                domesticVoiceSheetBean.setLocality(getDataFormatter().formatCellValue(locality));

            if (Objects.nonNull(suit))
                domesticVoiceSheetBean.setSuit(getDataFormatter().formatCellValue(suit));

            if (Objects.nonNull(state))
                domesticVoiceSheetBean.setState(getDataFormatter().formatCellValue(state));

            if (Objects.nonNull(postCode))
                domesticVoiceSheetBean.setPostalCode(getDataFormatter().formatCellValue(postCode));

            if (Objects.nonNull(registrationNumber))
                domesticVoiceSheetBean.setRegistrationNumber(getDataFormatter().formatCellValue(registrationNumber));

            if (Objects.nonNull(quantityRequired))
                domesticVoiceSheetBean.setQuantityRequired(getDataFormatter().formatCellValue(quantityRequired));

            if (Objects.nonNull(portedStatus))
                domesticVoiceSheetBean.setPortedStatus(getDataFormatter().formatCellValue(portedStatus));

            if (Objects.nonNull(portedNumber))
                domesticVoiceSheetBean.setPortedNumber(getDataFormatter().formatCellValue(portedNumber));

            domesticVoiceSheetBeans.add(domesticVoiceSheetBean);
        }
        LOGGER.info(domesticVoiceSheetBeans.toString());

        return domesticVoiceSheetBeans;
    }

    private void deepValidation(Map<String, List<OrderEnrichmentExcel>> orderEnrichmentData, CommonValidationResponse commonValidationResponse) throws TclCommonException {
        // if validation fails throw an error and delete the attachment entry in DB
        for (Map.Entry<String, List<OrderEnrichmentExcel>> entry : orderEnrichmentData.entrySet()) {
            String sheetName = entry.getKey();
            List<OrderEnrichmentExcel> orderEnrichment = entry.getValue();
            switch (sheetName) {
                case ITFS:
                    validateITFSBean(orderEnrichment, commonValidationResponse);
                    break;
                case LNS:
                    validateLNSBean(orderEnrichment, commonValidationResponse);
                    break;
                case UIFN:
                    validateUIFNBean(orderEnrichment, commonValidationResponse);
                    break;
                case ACANS:
                    validateACANSBean(orderEnrichment, commonValidationResponse);
                    break;
                case ACDTFS:
                    validateACDTFSBean(orderEnrichment, commonValidationResponse);
                    break;
                case DOMESTIC_VOICE:
                    validateDomesticVoiceBean(orderEnrichment, commonValidationResponse);
                    break;
                default:
                    break;
            }
        }
    }

    private void validateITFSBean(List<OrderEnrichmentExcel> orderEnrichments, CommonValidationResponse commonValidationResponse) throws TclCommonException {
        for (OrderEnrichmentExcel orderEnrichment : orderEnrichments) {
            ITFSSheetBean itfsSheetBean = (ITFSSheetBean) orderEnrichment;
            if (isBlank(itfsSheetBean.getOutPulseNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("ITFS Outpulse Number is missing for Origin Country :: %s", itfsSheetBean.getOriginCountry()));
            if (isBlank(itfsSheetBean.getPortedNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("ITFS Ported Number is missing for Origin Country :: %s", itfsSheetBean.getOriginCountry()));
        }

    }

    private void validateLNSBean(List<OrderEnrichmentExcel> orderEnrichments, CommonValidationResponse commonValidationResponse) throws TclCommonException {
        for (OrderEnrichmentExcel orderEnrichment : orderEnrichments) {
            LNSSheetBean lnsSheetBean = (LNSSheetBean) orderEnrichment;
            if (isBlank(lnsSheetBean.getOutPulseNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("LNS Outpulse Number is missing for Origin Country :: %s", lnsSheetBean.getOriginCountry()));
            if (isBlank(lnsSheetBean.getPortedNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("LNS Ported Number is missing for Origin Country :: %s", lnsSheetBean.getOriginCountry()));
            if (isBlank(lnsSheetBean.getCity()))
                throwErrorMessage(commonValidationResponse,
                        String.format("LNS City is missing for Origin Country :: %s", lnsSheetBean.getOriginCountry()));
            if (isBlank(lnsSheetBean.getNpaAreaCode()))
                throwErrorMessage(commonValidationResponse,
                        String.format("LNS NPA is missing for Origin Country :: %s", lnsSheetBean.getOriginCountry()));
        }
    }

    private void validateUIFNBean(List<OrderEnrichmentExcel> orderEnrichments, CommonValidationResponse commonValidationResponse) throws TclCommonException {
        for (OrderEnrichmentExcel orderEnrichment : orderEnrichments) {
            UIFNSheetBean uifnSheetBean = (UIFNSheetBean) orderEnrichment;
            if (isBlank(uifnSheetBean.getOutPulseNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("LNS Outpulse Number is missing for Origin Country :: %s", uifnSheetBean.getOriginCountry()));
            if (isBlank(uifnSheetBean.getPortedNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("UIFN Ported Number is missing for Origin Country :: %s", uifnSheetBean.getOriginCountry()));
        }
    }

    private void validateACDTFSBean(List<OrderEnrichmentExcel> orderEnrichments, CommonValidationResponse commonValidationResponse) throws TclCommonException {
        for (OrderEnrichmentExcel orderEnrichment : orderEnrichments) {
            ACDTFSSheetBean acdtfsSheetBean = (ACDTFSSheetBean) orderEnrichment;
            if (isBlank(acdtfsSheetBean.getOutPulseNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("ACDTFS Outpulse Number is missing for Origin Country :: %s", acdtfsSheetBean.getOriginCountry()));
        }
    }

    private void validateACANSBean(List<OrderEnrichmentExcel> orderEnrichments, CommonValidationResponse commonValidationResponse) throws TclCommonException {
        for (OrderEnrichmentExcel orderEnrichment : orderEnrichments) {
            ACANSSheetBean acansSheetBean = (ACANSSheetBean) orderEnrichment;
            if (isBlank(acansSheetBean.getOutPulseNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("ACDTFS Outpulse Number is missing for Origin Country :: %s", acansSheetBean.getOriginCountry()));
            if (isBlank(acansSheetBean.getCity()))
                throwErrorMessage(commonValidationResponse,
                        String.format("ACANS City is missing for Origin Country :: %s", acansSheetBean.getOriginCountry()));
        }
    }

    private void validateDomesticVoiceBean(List<OrderEnrichmentExcel> orderEnrichments, CommonValidationResponse commonValidationResponse) throws TclCommonException {
        for (OrderEnrichmentExcel orderEnrichment : orderEnrichments) {
            DomesticVoiceSheetBean dvSheetBean = (DomesticVoiceSheetBean) orderEnrichment;
            if (isBlank(dvSheetBean.getCity()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice City is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
            if (isBlank(dvSheetBean.getStreetAddress()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice Street Address is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
            if (isBlank(dvSheetBean.getLocality()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice Street Locality is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
            if (isBlank(dvSheetBean.getSuit()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice Suit / Floor is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
            if (isBlank(dvSheetBean.getState()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice State / Province is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
            if (isBlank(dvSheetBean.getPostalCode()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice Postal Code is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
            if (isBlank(dvSheetBean.getRegistrationNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice Registration Number is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
            if (isBlank(dvSheetBean.getPortedNumber()))
                throwErrorMessage(commonValidationResponse,
                        String.format("Domestic Voice Ported Number is missing for Origin Country :: %s", dvSheetBean.getOriginCountry()));
        }
    }

    public void throwErrorMessage(CommonValidationResponse commonValidationResponse, String errorMessage) throws TclCommonException {
        LOGGER.info(errorMessage);
        commonValidationResponse.setStatus(false);
        commonValidationResponse.setValidationMessage(errorMessage);
        throw new TclCommonException(errorMessage, ResponseResource.R_CODE_ERROR);
    }

    /**
     * Download Order Enrichment Template With Order Enrichment Details
     *
     * @param orderId
     * @param response
     * @throws IOException
     * @throws TclCommonException
     */
    public void downloadTemplateExcel(Integer orderId, HttpServletResponse response) throws IOException, TclCommonException {
        Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
        Order order = orderRepository.findById(orderId).get();
        String orderCode = order.getOrderCode();
        XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        byte[] outArray;
        try {
            Map<String, List<OrderEnrichmentExcel>> excelMap = getExcelData(order);
            for (Map.Entry<String, List<OrderEnrichmentExcel>> entry : excelMap.entrySet()) {
                String sheetName = entry.getKey();
                List<OrderEnrichmentExcel> sheetData = entry.getValue();
                createSheet(workbook, sheetData, sheetName);
            }
            workbook.lockStructure();
            workbook.setWorkbookPassword("test", null);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.write(outByteStream);
            outArray = outByteStream.toByteArray();
            String fileName = orderCode + ".xlsx";
            response.reset();
            response.setContentType("application/ms-excel");
            response.setContentLength(outArray.length);
            response.setHeader("Expires:", "0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            FileCopyUtils.copy(outArray, response.getOutputStream());
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        } finally {
            workbook.close();
            outByteStream.flush();
            outByteStream.close();
        }
    }

    /**
     * getExcelList - converts order into list of excel bean
     *
     * @param order
     * @return
     * @throws TclCommonException
     */
    private Map<String, List<OrderEnrichmentExcel>> getExcelData(Order order) {
        Map<String, List<OrderEnrichmentExcel>> mapData = new LinkedHashMap<>();
        order.getOrderToLes().stream().forEach(orderToLe -> {
            List<OrderGsc> orderGscs = orderGscRepository.findByOrderToLe(orderToLe);
            orderGscs.stream().forEach(orderGsc -> {
                String productName = orderGsc.getProductName();
                mapData.put(productName, getTemplateData(productName, orderGscs));
            });
        });
        return mapData;
    }

    private List<OrderEnrichmentExcel> getTemplateData(String productName, List<OrderGsc> orderGscs) {
        switch (productName) {
            case ITFS:
                return getITFSTemplateData(orderGscs);
            case LNS:
                return getLNSTemplateData(orderGscs);
            case UIFN:
                return getUIFNTemplateData(orderGscs);
            case ACANS:
                return getACANSTemplateData(orderGscs);
            case ACDTFS:
                return getACDTFSTemplateData(orderGscs);
            case DOMESTIC_VOICE:
                return getDomesticVoiceTemplateData(orderGscs);
            default:
                break;
        }
        return new ArrayList<>();
    }

    private List<OrderEnrichmentExcel> getITFSTemplateData(List<OrderGsc> orderGscs) {
        List<OrderEnrichmentExcel> itfsSheetBeans = new ArrayList<>();

        ITFSSheetBean itfsSheetHeader = new ITFSSheetBean(ORIGIN_COUNTRY, QUANTITY_REQUIRED, PORTED_STATUS, OUTPULSE_NUMBER, PORTED_NUMBER);
        itfsSheetBeans.add(itfsSheetHeader);

        orderGscs.stream().filter(orderGsc -> orderGsc.getProductName().equals(ITFS))
                .forEach(orderGsc -> orderGscDetailRepository.findByorderGsc(orderGsc)
                        .stream().forEach(orderGscDetail -> {
                            itfsSheetHeader.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? DESTINATION_COUNTRY : null);
                            Integer totalNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_QUANTITY_OF_NUMBERS));
                            Integer portingNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_PORTING_NUMBER_COUNT));
                            numberOfLineItemsForITFS(itfsSheetBeans, orderGscDetail, totalNumbers, portingNumbers);
                        }));

        return itfsSheetBeans;
    }

    private List<OrderEnrichmentExcel> numberOfLineItemsForITFS(List<OrderEnrichmentExcel> itfsSheetBeans, OrderGscDetail orderGscDetail, Integer totalNumber, Integer portingNumber) {
        Integer actualNumber = totalNumber - portingNumber;
        for (int i = 1; i <= actualNumber; i++) {
            ITFSSheetBean itfsSheetBean = (ITFSSheetBean) getInstance(ITFS);
            itfsSheetBean.setOriginCountry(orderGscDetail.getSrc());
            itfsSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            itfsSheetBean.setQuantityRequired("1");
            itfsSheetBean.setPortedStatus("New");
            itfsSheetBean.setPortedNumber(NOT_APPLICABLE);
            itfsSheetBeans.add(itfsSheetBean);
        }
        for (int j = 1; j <= portingNumber; j++) {
            ITFSSheetBean itfsSheetBean = (ITFSSheetBean) getInstance(ITFS);
            itfsSheetBean.setOriginCountry(orderGscDetail.getSrc());
            itfsSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            itfsSheetBean.setQuantityRequired("1");
            itfsSheetBean.setPortedStatus("Ported");
            itfsSheetBeans.add(itfsSheetBean);
        }
        return itfsSheetBeans;
    }

    private List<OrderEnrichmentExcel> getLNSTemplateData(List<OrderGsc> orderGscs) {
        List<OrderEnrichmentExcel> lnsSheetBeans = new ArrayList<>();

        LNSSheetBean lnsSheetHeader = new LNSSheetBean(ORIGIN_COUNTRY, CITY, NPA, QUANTITY_REQUIRED, PORTED_STATUS, OUTPULSE_NUMBER, PORTED_NUMBER);
        lnsSheetBeans.add(lnsSheetHeader);

        orderGscs.stream().filter(orderGsc -> orderGsc.getProductName().equals(LNS))
                .forEach(orderGsc -> orderGscDetailRepository.findByorderGsc(orderGsc).stream().forEach(orderGscDetail -> {
                    lnsSheetHeader.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? DESTINATION_COUNTRY : null);
                    Integer totalNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_QUANTITY_OF_NUMBERS));
                    Integer portingNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_PORTING_NUMBER_COUNT));
                    numberOfLineItemsForLNS(lnsSheetBeans, orderGscDetail, totalNumbers, portingNumbers);
                }));

        return lnsSheetBeans;
    }

    private List<OrderEnrichmentExcel> numberOfLineItemsForLNS(List<OrderEnrichmentExcel> lnsSheetBeans, OrderGscDetail orderGscDetail, Integer totalNumber, Integer portingNumber) {
        Integer actualNumber = totalNumber - portingNumber;
        for (int i = 1; i <= actualNumber; i++) {
            LNSSheetBean lnsSheetBean = (LNSSheetBean) getInstance(LNS);
            lnsSheetBean.setOriginCountry(orderGscDetail.getSrc());
            lnsSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            lnsSheetBean.setQuantityRequired("1");
            lnsSheetBean.setPortedStatus("New");
            lnsSheetBean.setPortedNumber(NOT_APPLICABLE);
            lnsSheetBeans.add(lnsSheetBean);
        }
        for (int j = 1; j <= portingNumber; j++) {
            LNSSheetBean lnsSheetBean = (LNSSheetBean) getInstance(LNS);
            lnsSheetBean.setOriginCountry(orderGscDetail.getSrc());
            lnsSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            lnsSheetBean.setQuantityRequired("1");
            lnsSheetBean.setPortedStatus("Ported");
            lnsSheetBeans.add(lnsSheetBean);
        }
        return lnsSheetBeans;
    }

    private List<OrderEnrichmentExcel> getUIFNTemplateData(List<OrderGsc> orderGscs) {
        List<OrderEnrichmentExcel> uifnSheetBeans = new ArrayList<>();

        UIFNSheetBean uifnSheetHeader = new UIFNSheetBean(ORIGIN_COUNTRY, QUANTITY_REQUIRED, PORTED_STATUS, OUTPULSE_NUMBER, PORTED_NUMBER);
        uifnSheetBeans.add(uifnSheetHeader);

        orderGscs.stream().filter(orderGsc -> orderGsc.getProductName().equals(UIFN))
                .forEach(orderGsc -> orderGscDetailRepository.findByorderGsc(orderGsc).stream().forEach(orderGscDetail -> {
                    uifnSheetHeader.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? DESTINATION_COUNTRY : null);
                    Integer totalNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_QUANTITY_OF_NUMBERS));
                    Integer portingNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_PORTING_NUMBER_COUNT));
                    numberOfLineItemsForUIFN(uifnSheetBeans, orderGscDetail, totalNumbers, portingNumbers);
                }));

        return uifnSheetBeans;
    }

    private List<OrderEnrichmentExcel> numberOfLineItemsForUIFN(List<OrderEnrichmentExcel> uifnSheetBeans, OrderGscDetail orderGscDetail, Integer totalNumber, Integer portingNumber) {
        Integer actualNumber = totalNumber - portingNumber;
        for (int i = 1; i <= actualNumber; i++) {
            UIFNSheetBean uifnSheetBean = (UIFNSheetBean) getInstance(UIFN);
            uifnSheetBean.setOriginCountry(orderGscDetail.getSrc());
            uifnSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            uifnSheetBean.setQuantityRequired("1");
            uifnSheetBean.setPortedStatus("New");
            uifnSheetBean.setPortedNumber(NOT_APPLICABLE);
            uifnSheetBeans.add(uifnSheetBean);
        }
        for (int j = 1; j <= portingNumber; j++) {
            UIFNSheetBean uifnSheetBean = (UIFNSheetBean) getInstance(UIFN);
            uifnSheetBean.setOriginCountry(orderGscDetail.getSrc());
            uifnSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            uifnSheetBean.setQuantityRequired("1");
            uifnSheetBean.setPortedStatus("Ported");
            uifnSheetBeans.add(uifnSheetBean);
        }
        return uifnSheetBeans;
    }

    private List<OrderEnrichmentExcel> getACANSTemplateData(List<OrderGsc> orderGscs) {
        List<OrderEnrichmentExcel> acansSheetBeans = new ArrayList<>();

        ACANSSheetBean acansSheetHeader = new ACANSSheetBean(ORIGIN_COUNTRY, CITY, QUANTITY_REQUIRED, OUTPULSE_NUMBER);
        acansSheetBeans.add(acansSheetHeader);

        orderGscs.stream().filter(orderGsc -> orderGsc.getProductName().equals(ACANS))
                .forEach(orderGsc -> orderGscDetailRepository.findByorderGsc(orderGsc).stream().forEach(orderGscDetail -> {
                    acansSheetHeader.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? DESTINATION_COUNTRY : null);
                    Integer totalNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_QUANTITY_OF_NUMBERS));
                    numberOfLineItemsForACANS(acansSheetBeans, orderGscDetail, totalNumbers);
                }));

        return acansSheetBeans;
    }

    private List<OrderEnrichmentExcel> numberOfLineItemsForACANS(List<OrderEnrichmentExcel> acansSheetBeans, OrderGscDetail orderGscDetail, Integer totalNumber) {
        for (int i = 1; i <= totalNumber; i++) {
            ACANSSheetBean acansSheetBean = (ACANSSheetBean) getInstance(ACANS);
            acansSheetBean.setOriginCountry(orderGscDetail.getSrc());
            acansSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            acansSheetBean.setQuantityRequired("1");
            acansSheetBeans.add(acansSheetBean);
        }
        return acansSheetBeans;
    }

    private List<OrderEnrichmentExcel> getACDTFSTemplateData(List<OrderGsc> orderGscs) {
        List<OrderEnrichmentExcel> acdtfsSheetBeans = new ArrayList<>();

        ACDTFSSheetBean acdtfsSheetHeader = new ACDTFSSheetBean(ORIGIN_COUNTRY, QUANTITY_REQUIRED, OUTPULSE_NUMBER);
        acdtfsSheetBeans.add(acdtfsSheetHeader);

        orderGscs.stream().filter(orderGsc -> orderGsc.getProductName().equals(ACDTFS))
                .forEach(orderGsc -> orderGscDetailRepository.findByorderGsc(orderGsc).stream().forEach(orderGscDetail -> {
                    acdtfsSheetHeader.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? DESTINATION_COUNTRY : null);
                    Integer totalNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_QUANTITY_OF_NUMBERS));
                    noOfLineItemsForACDTFS(acdtfsSheetBeans, orderGscDetail, totalNumbers);
                }));

        return acdtfsSheetBeans;
    }

    private List<OrderEnrichmentExcel> noOfLineItemsForACDTFS(List<OrderEnrichmentExcel> acdtfsSheetBeans, OrderGscDetail orderGscDetail, Integer totalNumber) {
        for (int i = 1; i <= totalNumber; i++) {
            ACDTFSSheetBean acdtfsSheetBean = (ACDTFSSheetBean) getInstance(ACDTFS);
            acdtfsSheetBean.setOriginCountry(orderGscDetail.getSrc());
            acdtfsSheetBean.setDestinationCountry(isNotBlank(orderGscDetail.getDest()) == true ? orderGscDetail.getDest() : null);
            acdtfsSheetBean.setQuantityRequired("1");
            acdtfsSheetBeans.add(acdtfsSheetBean);
        }
        return acdtfsSheetBeans;
    }

    private List<OrderEnrichmentExcel> getDomesticVoiceTemplateData(List<OrderGsc> orderGscs) {
        List<OrderEnrichmentExcel> domesticVoiceSheetBeans = new ArrayList<>();

        DomesticVoiceSheetBean domesticVoiceSheetHeader = new DomesticVoiceSheetBean(ORIGIN_COUNTRY, CITY, STREET_ADDRESS,
                LOCALITY ,SUIT, STATE, POSTAL_CODE, REGISTRATION_NUMBER, QUANTITY_REQUIRED, PORTED_STATUS, PORTED_NUMBER);
        domesticVoiceSheetBeans.add(domesticVoiceSheetHeader);

        orderGscs.stream().filter(orderGsc -> orderGsc.getProductName().equals(DOMESTIC_VOICE))
                .forEach(orderGsc -> orderGscDetailRepository.findByorderGsc(orderGsc).stream().forEach(orderGscDetail -> {
                    Integer totalNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_QUANTITY_OF_NUMBERS));
                    Integer portingNumbers = Integer.valueOf(getProductMasterAttribute(orderGsc, orderGscDetail, ATTR_PORTING_NUMBER_COUNT));
                    numberOfLineItemsForDomesticVoice(domesticVoiceSheetBeans, orderGscDetail, totalNumbers, portingNumbers);
                }));
        return domesticVoiceSheetBeans;
    }

    private List<OrderEnrichmentExcel> numberOfLineItemsForDomesticVoice(List<OrderEnrichmentExcel> domesticVoiceSheetBeans, OrderGscDetail orderGscDetail, Integer totalNumber, Integer portingNumber) {
        Integer actualNumber = totalNumber - portingNumber;
        for (int i = 1; i <= actualNumber; i++) {
            DomesticVoiceSheetBean domesticVoiceSheetBean = (DomesticVoiceSheetBean) getInstance(DOMESTIC_VOICE);
            domesticVoiceSheetBean.setOriginCountry(orderGscDetail.getSrc());
            domesticVoiceSheetBean.setQuantityRequired("1");
            domesticVoiceSheetBean.setPortedStatus("New");
            domesticVoiceSheetBean.setPortedNumber(NOT_APPLICABLE);
            domesticVoiceSheetBeans.add(domesticVoiceSheetBean);
        }
        for (int j = 1; j <= portingNumber; j++) {
            DomesticVoiceSheetBean domesticVoiceSheetBean = (DomesticVoiceSheetBean) getInstance(DOMESTIC_VOICE);
            domesticVoiceSheetBean.setOriginCountry(orderGscDetail.getSrc());
            domesticVoiceSheetBean.setQuantityRequired("1");
            domesticVoiceSheetBean.setPortedStatus("Ported");
            domesticVoiceSheetBeans.add(domesticVoiceSheetBean);
        }
        return domesticVoiceSheetBeans;
    }

    //Create OrderEnrichment Excel Sheet
    private void createSheet(XSSFWorkbook workbook, List<OrderEnrichmentExcel> orderEnrichmentExcels, String sheetName) throws TclCommonException {
        if (0 < orderEnrichmentExcels.size()) {
            XSSFSheet sheet = workbook.createSheet(sheetName);
            // Password to protect the sheet
            // TODO Change to app prop
            sheet.protectSheet("test");
            //workbook.setSheetName(workbook.getSheetIndex(sheet), sheetName);

            int rowCount = 0;
            for (OrderEnrichmentExcel orderEnrichmentExcel : orderEnrichmentExcels) {
                Row row = sheet.createRow(rowCount);
                writeSheet(orderEnrichmentExcel, row, sheetName, workbook);
                rowCount++;
            }
            appandCityAndNPA(workbook, sheetName, sheet, orderEnrichmentExcels);
            styleHeader(workbook, sheet);
        }
    }

    private void appandCityAndNPA(XSSFWorkbook workbook, String sheetName, XSSFSheet sheet, List<OrderEnrichmentExcel> orderEnrichmentExcels) throws TclCommonException {
        switch (sheetName) {
            case LNS:
                appendLNSCity(workbook, sheet, orderEnrichmentExcels);
                appendLnsNpaCode(workbook, sheet, orderEnrichmentExcels);
                break;
            case ACANS:
                appendACANSCity(workbook, sheet, orderEnrichmentExcels);
                break;
            case DOMESTIC_VOICE:
                appendDomesticVoiceCity(workbook, sheet, orderEnrichmentExcels);
                break;
            default:
                break;
        }

    }

    /**
     * writeBook - writes data into Order Enrichment Excel
     *
     * @param orderEnrichmentExcel
     * @param row
     * @param row
     * @param workbook
     * @throws TclCommonException
     */
    private void writeSheet(OrderEnrichmentExcel orderEnrichmentExcel, Row row, String sheetName, XSSFWorkbook workbook) throws TclCommonException {
        switch (sheetName) {
            case ITFS:
                writeITFSSheet(orderEnrichmentExcel, row, workbook);
                break;
            case LNS:
                writeLNSSheet(orderEnrichmentExcel, row, workbook);
                break;
            case UIFN:
                writeUIFNSheet(orderEnrichmentExcel, row, workbook);
                break;
            case ACANS:
                writeACANSSheet(orderEnrichmentExcel, row, workbook);
                break;
            case ACDTFS:
                writeACDTFSSheet(orderEnrichmentExcel, row, workbook);
                break;
            case DOMESTIC_VOICE:
                writeDomesticVoiceSheet(orderEnrichmentExcel, row, workbook);
                break;
            default:
                break;
        }
    }

    /**
     * writeITFSSheet - writes data into excel
     *
     * @param orderEnrichmentExcel
     * @param row
     * @throws TclCommonException
     */
    private void writeITFSSheet(OrderEnrichmentExcel orderEnrichmentExcel, Row row, XSSFWorkbook workbook) throws TclCommonException {
        ITFSSheetBean itfsSheetBean = (ITFSSheetBean) orderEnrichmentExcel;

        if (Objects.isNull(itfsSheetBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }

        int i = 0;
        Cell cell = row.createCell(i);
        cell.setCellValue(itfsSheetBean.getOriginCountry());

        if (Objects.nonNull(itfsSheetBean.getDestinationCountry())) {
            cell = row.createCell(++i);
            cell.setCellValue(itfsSheetBean.getDestinationCountry());
        }

        if (row.getRowNum() == 0) {
            cell = row.createCell(++i);
            cell.setCellValue(itfsSheetBean.getQuantityRequired());
        } else {
            cell = row.createCell(++i);
            cell.setCellValue(Double.valueOf(itfsSheetBean.getQuantityRequired()));
        }

        cell = row.createCell(++i);
        cell.setCellValue(itfsSheetBean.getPortedStatus());

        cell = row.createCell(++i);
        cell.setCellValue(itfsSheetBean.getOutPulseNumber());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        if (isBlank(itfsSheetBean.getPortedNumber())) {
            cell.setCellStyle(unlockCellStyle(workbook));
        }
        cell.setCellValue(itfsSheetBean.getPortedNumber());

    }

    /**
     * writeLNSSheet - writes data into excel
     *
     * @param orderEnrichmentExcel
     * @param row
     * @throws TclCommonException
     */
    private void writeLNSSheet(OrderEnrichmentExcel orderEnrichmentExcel, Row row, XSSFWorkbook workbook) throws TclCommonException {
        LNSSheetBean lnsSheetBean = (LNSSheetBean) orderEnrichmentExcel;

        if (Objects.isNull(lnsSheetBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }

        int i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue(lnsSheetBean.getOriginCountry());

        if (Objects.nonNull(lnsSheetBean.getDestinationCountry())) {
            cell = row.createCell(++i);
            cell.setCellValue(lnsSheetBean.getDestinationCountry());
        }

        cell = row.createCell(++i);
        cell.setCellValue(lnsSheetBean.getCity());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        cell.setCellValue(lnsSheetBean.getNpaAreaCode());
        cell.setCellStyle(unlockCellStyle(workbook));

        if (row.getRowNum() == 0) {
            cell = row.createCell(++i);
            cell.setCellValue(lnsSheetBean.getQuantityRequired());
        } else {
            cell = row.createCell(++i);
            cell.setCellValue(Double.valueOf(lnsSheetBean.getQuantityRequired()));
        }

        cell = row.createCell(++i);
        cell.setCellValue(lnsSheetBean.getPortedStatus());

        cell = row.createCell(++i);
        cell.setCellValue(lnsSheetBean.getOutPulseNumber());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        if (isBlank(lnsSheetBean.getPortedNumber())) {
            cell.setCellStyle(unlockCellStyle(workbook));
        }
        cell.setCellValue(lnsSheetBean.getPortedNumber());
    }

    private void appendLnsCityNpaSheet(XSSFWorkbook workbook, Map<String, Map<String, List<String>>> countryCityNPAData) {
        XSSFSheet lnsCityNpaSheet = workbook.createSheet(LNS_CITY_NPA_SHEET);
        lnsCityNpaSheet.protectSheet("test");
        XSSFRow headerRow = lnsCityNpaSheet.createRow(0);
        XSSFCell headerCountryCell = headerRow.createCell(0);
        headerCountryCell.setCellValue(COUNTRY);
        XSSFCell headerCityCell = headerRow.createCell(1);
        headerCityCell.setCellValue(CITY);
        XSSFCell headerNpaCell = headerRow.createCell(2);
        headerNpaCell.setCellValue(NPA);
        styleHeader(workbook, lnsCityNpaSheet);
        int rowIncrement = 1;
        for (Map.Entry<String, Map<String, List<String>>> countrtCityNpaData : countryCityNPAData.entrySet()) {
            String country = countrtCityNpaData.getKey();
            Map<String, List<String>> cityNpaMap = countrtCityNpaData.getValue();
            for (Map.Entry<String, List<String>> cityNpa : cityNpaMap.entrySet()) {
                String city = cityNpa.getKey();
                List<String> npaCodeList = cityNpa.getValue();
                if (Objects.nonNull(npaCodeList) && !npaCodeList.isEmpty()) {
                    String npaCode = npaCodeList.stream().filter(s -> Objects.nonNull(s) && !s.isEmpty()).findFirst().get();
                    List<String> convertedNpaCode = Stream.of(npaCode.split(",")).map(String::trim).collect(Collectors.toList());
                    for (int i = 0; i < convertedNpaCode.size(); i++) {
                        XSSFRow row = lnsCityNpaSheet.createRow(rowIncrement);
                        XSSFCell countryCell = row.createCell(0);
                        countryCell.setCellValue(country);
                        XSSFCell cityCell = row.createCell(1);
                        cityCell.setCellValue(city);
                        XSSFCell npaCell = row.createCell(2);
                        npaCell.setCellValue(convertedNpaCode.get(i));
                        rowIncrement++;
                    }
                }
            }
        }
    }

    private void appendLNSCity(XSSFWorkbook workbook, XSSFSheet sheet, List<OrderEnrichmentExcel> orderEnrichmentExcels) throws TclCommonException {
        try {
            List<LNSSheetBean> lnsSheetBeans = convertToClass(LNSSheetBean.class, orderEnrichmentExcels);
            Map<String, List<OrderEnrichmentExcel>> lnsOriginCountryWiseData = orderEnrichmentExcels.stream().collect(Collectors.groupingBy(orderEnrichmentExcel -> {
                return ((LNSSheetBean) orderEnrichmentExcel).getOriginCountry();
            }));

            //create hiddensheet to load lns cities and npa code for dropdown
            XSSFSheet lnsHiddenSheet = workbook.createSheet(LNS_HIDDEN_SHEET);
            Map<String, Map<String, List<String>>> countryCityNPAData = createCountryWiseCityNpaCodeData(lnsOriginCountryWiseData, LNS);
            appendLnsCityNpaSheet(workbook, countryCityNPAData);
            loadDataToLnsHiddensheet(lnsHiddenSheet, countryCityNPAData, lnsSheetBeans);

            Integer numberOfRowStartValues = 1;
            Integer numberOfRowEndValues = 0;
            Integer countryCount = 1;
            Integer startValue = 1;
            Integer endValue = 0;
            String countryTrace = "";
            for (LNSSheetBean lnsSheetBean : lnsSheetBeans) {
                String originCountry = lnsSheetBean.getOriginCountry();
                if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                    continue;
                Integer numberOfCountry = lnsOriginCountryWiseData.get(originCountry).size();
                Integer numberOfcity = countryCityNPAData.get(originCountry).size();
                numberOfRowEndValues = numberOfRowEndValues + numberOfCountry;
                endValue = endValue + numberOfcity;

                XSSFName namedCell = workbook.createName();
                namedCell.setNameName("dropdownFormula" + countryCount);
                namedCell.setRefersToFormula("lnsHiddenSheet!$B$" + startValue + ":$B$" + endValue);
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);

                Integer cityColumnValue = 0;
                if (isLNSDestinationCountryPresent(orderEnrichmentExcels))
                    cityColumnValue = 2;
                else
                    cityColumnValue = 1;

                XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("dropdownFormula" + countryCount);
                CellRangeAddressList cityCellRange = new CellRangeAddressList(numberOfRowStartValues, numberOfRowEndValues, cityColumnValue, cityColumnValue);
                XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, cityCellRange);
                workbook.setSheetHidden(workbook.getSheetIndex(LNS_HIDDEN_SHEET), true);
                validation.setShowErrorBox(true);
                sheet.addValidationData(validation);

                startValue = endValue + 1;
                countryCount++;
                countryTrace = originCountry;
                numberOfRowStartValues = numberOfRowEndValues + 1;
            }
        } catch (Exception e) {
            LOGGER.error("Error in appending LNS city :: {}", e);
            throw new TclCommonException("Error in appending LNS city");
        }
    }

    //load data to lnsHiddenSheet
    private void loadDataToLnsHiddensheet(XSSFSheet lnsHiddenSheet, Map<String, Map<String,
            List<String>>> countryCityNPAData, List<LNSSheetBean> lnsSheetBeans) {
        String countryTrace = "";
        int hiddenSheetRow = 0;
        for (LNSSheetBean lnsSheetBean : lnsSheetBeans) {
            String originCountry = lnsSheetBean.getOriginCountry();
            if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                continue;
            Map<String, List<String>> cityNpaMap = countryCityNPAData.get(originCountry);
            for (Map.Entry<String, List<String>> cityNpaData : cityNpaMap.entrySet()) {
                String city = cityNpaData.getKey();
                List<String> npaCode = cityNpaData.getValue();
                XSSFRow row = lnsHiddenSheet.createRow(hiddenSheetRow);
                XSSFCell countryCell = row.createCell(0);
                countryCell.setCellValue(originCountry);
                XSSFCell cityCell = row.createCell(1);
                cityCell.setCellValue(city);
                npaCode.forEach(code -> {
                    XSSFCell npaCell = row.createCell(2);
                    npaCell.setCellValue(code);
                });
                hiddenSheetRow++;
            }
            countryTrace = originCountry;
        }
    }

    private void appendDomesticVoiceCity(XSSFWorkbook workbook, XSSFSheet sheet, List<OrderEnrichmentExcel> orderEnrichmentExcels) throws TclCommonException {
        try {
            List<DomesticVoiceSheetBean> domesticVoiceSheetBeans = convertToClass(DomesticVoiceSheetBean.class, orderEnrichmentExcels);
            Map<String, List<OrderEnrichmentExcel>> domesticVoiceOriginCountryWiseData = orderEnrichmentExcels.stream().collect(Collectors.groupingBy(orderEnrichmentExcel -> {
                return ((DomesticVoiceSheetBean) orderEnrichmentExcel).getOriginCountry();
            }));

            //create hiddensheet to load cities and npa code for dropdown
            XSSFSheet dvHiddenSheet = workbook.createSheet("dvHiddenSheet");
            Map<String, Map<String, List<String>>> countryCityNPAData = createCountryWiseCityNpaCodeData(domesticVoiceOriginCountryWiseData, DOMESTIC_VOICE);
            loadDataToDomesticVoiceHiddensheet(dvHiddenSheet, countryCityNPAData, domesticVoiceSheetBeans);

            Integer numberOfRowStartValues = 1;
            Integer numberOfRowEndValues = 0;
            Integer countryCount = 1;
            Integer startValue = 1;
            Integer endValue = 0;
            String countryTrace = "";
            for (DomesticVoiceSheetBean domesticVoiceSheetBean : domesticVoiceSheetBeans) {
                String originCountry = domesticVoiceSheetBean.getOriginCountry();
                if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                    continue;
                Integer numberOfCountry = domesticVoiceOriginCountryWiseData.get(originCountry).size();
                Integer numberOfcity = countryCityNPAData.get(originCountry).size();
                numberOfRowEndValues = numberOfRowEndValues + numberOfCountry;
                endValue = endValue + numberOfcity;

                XSSFName namedCell = workbook.createName();
                namedCell.setNameName("dvdropdownFormula" + countryCount);
                namedCell.setRefersToFormula("dvHiddenSheet!$B$" + startValue + ":$B$" + endValue);
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);

                XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("dvdropdownFormula" + countryCount);
                CellRangeAddressList cityCellRange = new CellRangeAddressList(numberOfRowStartValues, numberOfRowEndValues, 1, 1);
                XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, cityCellRange);
                workbook.setSheetHidden(workbook.getSheetIndex("dvHiddenSheet"), true);
                validation.setShowErrorBox(true);
                sheet.addValidationData(validation);

                startValue = endValue + 1;
                countryCount++;
                countryTrace = originCountry;
                numberOfRowStartValues = numberOfRowEndValues + 1;
            }
        } catch (Exception e) {
            LOGGER.error("Error in appending DomesticVoice City and npa :: {}", e);
            throw new TclCommonException("Error in appending DomesticVoice City and npa");
        }
    }

    //load data to domesticHiddenSheet
    private void loadDataToDomesticVoiceHiddensheet(XSSFSheet lnsHiddenSheet, Map<String, Map<String,
            List<String>>> countryCityNPAData, List<DomesticVoiceSheetBean> domesticVoiceSheetBeans) {
        String countryTrace = "";
        int hiddenSheetRow = 0;
        for (DomesticVoiceSheetBean domesticVoiceSheetBean : domesticVoiceSheetBeans) {
            String originCountry = domesticVoiceSheetBean.getOriginCountry();
            if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                continue;
            Map<String, List<String>> cityNpaMap = countryCityNPAData.get(originCountry);
            for (Map.Entry<String, List<String>> cityNpaData : cityNpaMap.entrySet()) {
                String city = cityNpaData.getKey();
                List<String> npaCode = cityNpaData.getValue();
                XSSFRow row = lnsHiddenSheet.createRow(hiddenSheetRow);
                XSSFCell countryCell = row.createCell(0);
                countryCell.setCellValue(originCountry);
                XSSFCell cityCell = row.createCell(1);
                cityCell.setCellValue(city);
                npaCode.forEach(code -> {
                    XSSFCell npaCell = row.createCell(2);
                    npaCell.setCellValue(code);
                });
                hiddenSheetRow++;
            }
            countryTrace = originCountry;
        }
    }

    private void appendACANSCity(XSSFWorkbook workbook, XSSFSheet sheet, List<OrderEnrichmentExcel> orderEnrichmentExcels) throws TclCommonException {
        try {
            List<ACANSSheetBean> acansSheetBeans = convertToClass(ACANSSheetBean.class, orderEnrichmentExcels);
            Map<String, List<OrderEnrichmentExcel>> acansOriginCountryWiseData = orderEnrichmentExcels.stream().collect(Collectors.groupingBy(orderEnrichmentExcel -> {
                return ((ACANSSheetBean) orderEnrichmentExcel).getOriginCountry();
            }));

            //create hiddensheet to load cities and npa code for dropdown
            XSSFSheet dvHiddenSheet = workbook.createSheet(ACANS_HIDDEN_SHEET);
            Map<String, Map<String, List<String>>> countryCityNPAData = createCountryWiseCityNpaCodeData(acansOriginCountryWiseData, ACANS);
            loadDataToACANSHiddensheet(dvHiddenSheet, countryCityNPAData, acansSheetBeans);

            Integer numberOfRowStartValues = 1;
            Integer numberOfRowEndValues = 0;
            Integer countryCount = 1;
            Integer startValue = 1;
            Integer endValue = 0;
            String countryTrace = "";
            for (ACANSSheetBean acansSheetBean : acansSheetBeans) {
                String originCountry = acansSheetBean.getOriginCountry();
                if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                    continue;
                Integer numberOfCountry = acansOriginCountryWiseData.get(originCountry).size();
                Integer numberOfcity = countryCityNPAData.get(originCountry).size();
                numberOfRowEndValues = numberOfRowEndValues + numberOfCountry;
                endValue = endValue + numberOfcity;

                XSSFName namedCell = workbook.createName();
                namedCell.setNameName("acansdropdownFormula" + countryCount);
                namedCell.setRefersToFormula("acansHiddenSheet!$B$" + startValue + ":$B$" + endValue);
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);

                Integer cityColumnValue = 0;
                if (isACANSDestinationCountryPresent(orderEnrichmentExcels))
                    cityColumnValue = 2;
                else
                    cityColumnValue = 1;

                XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("acansdropdownFormula" + countryCount);
                CellRangeAddressList cityCellRange = new CellRangeAddressList(numberOfRowStartValues, numberOfRowEndValues, cityColumnValue, cityColumnValue);
                XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, cityCellRange);
                workbook.setSheetHidden(workbook.getSheetIndex(ACANS_HIDDEN_SHEET), true);
                validation.setShowErrorBox(true);
                sheet.addValidationData(validation);

                startValue = endValue + 1;
                countryCount++;
                countryTrace = originCountry;
                numberOfRowStartValues = numberOfRowEndValues + 1;
            }
        } catch (Exception e) {
            LOGGER.error("Error in appending ACANS City and npa :: {}", e);
            throw new TclCommonException("Error in appending ACANS City and npa");
        }
    }

    //load data to domesticHiddenSheet
    private void loadDataToACANSHiddensheet(XSSFSheet acansHiddenSheet, Map<String, Map<String,
            List<String>>> countryCityNPAData, List<ACANSSheetBean> acansSheetBeans) {
        String countryTrace = "";
        int hiddenSheetRow = 0;
        for (ACANSSheetBean acansSheetBean : acansSheetBeans) {
            String originCountry = acansSheetBean.getOriginCountry();
            if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                continue;
            Map<String, List<String>> cityNpaMap = countryCityNPAData.get(originCountry);
            for (Map.Entry<String, List<String>> cityNpaData : cityNpaMap.entrySet()) {
                String city = cityNpaData.getKey();
                List<String> npaCode = cityNpaData.getValue();
                XSSFRow row = acansHiddenSheet.createRow(hiddenSheetRow);
                XSSFCell countryCell = row.createCell(0);
                countryCell.setCellValue(originCountry);
                XSSFCell cityCell = row.createCell(1);
                cityCell.setCellValue(city);
                npaCode.forEach(code -> {
                    XSSFCell npaCell = row.createCell(2);
                    npaCell.setCellValue(code);
                });
                hiddenSheetRow++;
            }
            countryTrace = originCountry;
        }
    }

    private boolean isLNSDestinationCountryPresent(List<OrderEnrichmentExcel> value) {
        LNSSheetBean lnsSheetBean = (LNSSheetBean) value.stream().findFirst().get();
        if (isNotBlank(lnsSheetBean.getDestinationCountry())) {
            return true;
        }
        return false;
    }

    private boolean isACANSDestinationCountryPresent(List<OrderEnrichmentExcel> value) {
        ACANSSheetBean acansSheetBean = (ACANSSheetBean) value.stream().findFirst().get();
        if (isNotBlank(acansSheetBean.getDestinationCountry())) {
            return true;
        }
        return false;
    }

    private Map<String, Map<String, List<String>>> createCountryWiseCityNpaCodeData(Map<String,
            List<OrderEnrichmentExcel>> originCountryWiseData, String productType) {
        Map<String, Map<String, List<String>>> countryCityNPAData = new HashMap<>();
        originCountryWiseData.forEach((originCountry, orderEnrichmentExcels) -> {
            String response = null;
            try {
                if (!ORIGIN_COUNTRY.equalsIgnoreCase(originCountry)) {
                    response = (String) mqUtils.sendAndReceive(getCitiesQueue, productType.concat(COLON).concat(originCountry));
                    GscLocationBean gscProductLocationBean = GscUtils.fromJson(response, GscLocationBean.class);
                    Map<String, List<String>> cityNpaData = new TreeMap<>();
                    gscProductLocationBean.getSources().stream().forEach(gscProductLocationDetailBean -> {
                        String city = gscProductLocationDetailBean.getName();
                        String cityCode = gscProductLocationDetailBean.getCode();
                        List<String> areaCode = gscProductLocationDetailBean.getAreaCode();
                        areaCode = Objects.nonNull(areaCode) ? areaCode : new ArrayList<String>();
                        if (Objects.nonNull(city) && StringUtils.isNoneEmpty(city) && Objects.nonNull(cityCode)
                                && StringUtils.isNoneEmpty(cityCode)) {
                            cityNpaData.put(city, areaCode);
                        }
                    });
                    countryCityNPAData.put(originCountry, cityNpaData);
                }
            } catch (TclCommonException e) {
                e.printStackTrace();
                throw new TclCommonRuntimeException("Error in appending city and npa");
            }
        });
        return countryCityNPAData;
    }

    /**
     * writeUIFNSheet - writes data into excel
     *
     * @param orderEnrichmentExcel
     * @param row
     * @param workbook
     * @throws TclCommonException
     */
    private void writeUIFNSheet(OrderEnrichmentExcel orderEnrichmentExcel, Row row, XSSFWorkbook workbook) throws TclCommonException {
        UIFNSheetBean uifnSheetBean = (UIFNSheetBean) orderEnrichmentExcel;

        if (Objects.isNull(uifnSheetBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }

        int i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue(uifnSheetBean.getOriginCountry());

        if (Objects.nonNull(uifnSheetBean.getDestinationCountry())) {
            cell = row.createCell(++i);
            cell.setCellValue(uifnSheetBean.getDestinationCountry());
        }

        if (row.getRowNum() == 0) {
            cell = row.createCell(++i);
            cell.setCellValue(uifnSheetBean.getQuantityRequired());

        } else {
            cell = row.createCell(++i);
            cell.setCellValue(Double.valueOf(uifnSheetBean.getQuantityRequired()));
        }

        cell = row.createCell(++i);
        cell.setCellValue(uifnSheetBean.getPortedStatus());

        cell = row.createCell(++i);
        cell.setCellValue(uifnSheetBean.getOutPulseNumber());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        if (isBlank(uifnSheetBean.getPortedNumber())) {
            cell.setCellStyle(unlockCellStyle(workbook));
        }
        cell.setCellValue(uifnSheetBean.getPortedNumber());
    }

    /**
     * writeACANSSheet - writes data into excel
     *
     * @param orderEnrichmentExcel
     * @param row
     * @param workbook
     * @throws TclCommonException
     */
    private void writeACANSSheet(OrderEnrichmentExcel orderEnrichmentExcel, Row row, XSSFWorkbook workbook) throws TclCommonException {
        ACANSSheetBean acansSheetBean = (ACANSSheetBean) orderEnrichmentExcel;

        if (Objects.isNull(acansSheetBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }

        int i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue(acansSheetBean.getOriginCountry());

        if (Objects.nonNull(acansSheetBean.getDestinationCountry())) {
            cell = row.createCell(++i);
            cell.setCellValue(acansSheetBean.getDestinationCountry());
        }

        cell = row.createCell(++i);
        cell.setCellValue(acansSheetBean.getCity());
        cell.setCellStyle(unlockCellStyle(workbook));

        if (row.getRowNum() == 0) {
            cell = row.createCell(++i);
            cell.setCellValue(acansSheetBean.getQuantityRequired());
        } else {
            cell = row.createCell(++i);
            cell.setCellValue(Double.valueOf(acansSheetBean.getQuantityRequired()));
        }

        cell = row.createCell(++i);
        cell.setCellValue(acansSheetBean.getOutPulseNumber());
        cell.setCellStyle(unlockCellStyle(workbook));
    }

    /**
     * writeACDTFSSheet - writes data into excel
     *
     * @param orderEnrichmentExcel
     * @param row
     * @param workbook
     * @throws TclCommonException
     */
    private void writeACDTFSSheet(OrderEnrichmentExcel orderEnrichmentExcel, Row row, XSSFWorkbook workbook) throws TclCommonException {
        ACDTFSSheetBean acdtfsSheetBean = (ACDTFSSheetBean) orderEnrichmentExcel;

        if (Objects.isNull(acdtfsSheetBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }

        int i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue(acdtfsSheetBean.getOriginCountry());

        if (Objects.nonNull(acdtfsSheetBean.getDestinationCountry())) {
            cell = row.createCell(++i);
            cell.setCellValue(acdtfsSheetBean.getDestinationCountry());
        }

        if (row.getRowNum() == 0) {
            cell = row.createCell(++i);
            cell.setCellValue(acdtfsSheetBean.getQuantityRequired());
        } else {
            cell = row.createCell(++i);
            cell.setCellValue(Double.valueOf(acdtfsSheetBean.getQuantityRequired()));
        }

        cell = row.createCell(++i);
        cell.setCellValue(acdtfsSheetBean.getOutPulseNumber());
        cell.setCellStyle(unlockCellStyle(workbook));
    }

    /**
     * writeDomesticVoiceSheet - writes data into excel
     *
     * @param orderEnrichmentExcel
     * @param row
     * @param workbook
     * @throws TclCommonException
     */
    private void writeDomesticVoiceSheet(OrderEnrichmentExcel orderEnrichmentExcel, Row row, XSSFWorkbook workbook) throws TclCommonException {
        DomesticVoiceSheetBean domesticVoiceSheetBean = (DomesticVoiceSheetBean) orderEnrichmentExcel;

        if (Objects.isNull(domesticVoiceSheetBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }

        int i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue(domesticVoiceSheetBean.getOriginCountry());

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getCity());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getStreetAddress());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getLocality());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getSuit());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getState());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getPostalCode());
        cell.setCellStyle(unlockCellStyle(workbook));

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getRegistrationNumber());
        cell.setCellStyle(unlockCellStyle(workbook));

        if (row.getRowNum() == 0) {
            cell = row.createCell(++i);
            cell.setCellValue(domesticVoiceSheetBean.getQuantityRequired());
        } else {
            cell = row.createCell(++i);
            cell.setCellValue(Double.valueOf(domesticVoiceSheetBean.getQuantityRequired()));
        }

        cell = row.createCell(++i);
        cell.setCellValue(domesticVoiceSheetBean.getPortedStatus());

        cell = row.createCell(++i);
        if (isBlank(domesticVoiceSheetBean.getPortedNumber())) {
            cell.setCellStyle(unlockCellStyle(workbook));
        }
        cell.setCellValue(domesticVoiceSheetBean.getPortedNumber());

    }

    private int getLastRowWithData(Sheet currentSheet) {
        int rowCount = 0;
        Iterator<Row> iter = currentSheet.rowIterator();

        while (iter.hasNext()) {
            Row r = iter.next();
            if (!this.isRowBlank(r)) {
                rowCount = r.getRowNum();
            }
        }

        return rowCount;
    }

    private boolean isRowBlank(Row r) {
        boolean ret = true;

        /*
         * If a row is null, it must be blank.
         */
        if (r != null) {
            Iterator<Cell> cellIter = r.cellIterator();
            /*
             * Iterate through all cells in a row.
             */
            while (cellIter.hasNext()) {
                /*
                 * If one of the cells in given row contains data, the row is considered not
                 * blank.
                 */
                if (!this.isCellBlank(cellIter.next())) {
                    ret = false;
                    break;
                }
            }
        }

        return ret;
    }

    private boolean isCellBlank(Cell c) {
        return (c == null || c.getCellTypeEnum() == CellType.BLANK);
    }

    private DataFormatter getDataFormatter() {
        if (null == dataFormatter) {
            return new DataFormatter();
        }
        return dataFormatter;
    }

    private OrderEnrichmentExcel getInstance(String instance) {
        switch (instance) {
            case ITFS:
                return new ITFSSheetBean();
            case LNS:
                return new LNSSheetBean();
            case UIFN:
                return new UIFNSheetBean();
            case ACANS:
                return new ACANSSheetBean();
            case ACDTFS:
                return new ACDTFSSheetBean();
            case DOMESTIC_VOICE:
                return new DomesticVoiceSheetBean();
            default:
                break;
        }
        return null;
    }

    private CellStyle unlockCellStyle(XSSFWorkbook workbook) {
        CellStyle unlockedCellStyle = workbook.createCellStyle();
        //unlock cell
        unlockedCellStyle.setLocked(false);
        //make cell as text
        DataFormat dataFormat = workbook.createDataFormat();
        unlockedCellStyle.setDataFormat(dataFormat.getFormat("@"));
        return unlockedCellStyle;
    }

    private CellStyle convertCellStyleToText(XSSFWorkbook workbook) {
        CellStyle cellToText = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        cellToText.setDataFormat(dataFormat.getFormat("@"));
        return cellToText;
    }

    private void styleHeader(XSSFWorkbook workbook, XSSFSheet sheet) {
        //style heading
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row heading = sheet.getRow(0);

        for (int i = 0; i < heading.getLastCellNum(); i++) {
            sheet.setColumnWidth(i, 7000);
            heading.getCell(i).setCellStyle(style);
        }
        // Freeze header row
        sheet.createFreezePane(0, 1);
    }

    // we have common method in GSC Builder class
    private String getProductMasterAttribute(OrderGsc orderGsc, OrderGscDetail orderGscDetail, String attributeName) {
        return mstProductComponentRepository
                .findByNameAndStatus(orderGsc.getProductName(),
                        STATUS_ACTIVE)
                .stream().findFirst()
                .flatMap(mstProductComponent -> orderProductComponentRepository
                        .findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
                                GSC_ORDER_PRODUCT_COMPONENT_TYPE)
                        .stream().findFirst())
                .map(orderProductComponent -> Tuple.of(orderProductComponent,
                        productAttributeMasterRepository
                                .findByNameAndStatus(attributeName, STATUS_ACTIVE).stream()
                                .findFirst().orElse(new ProductAttributeMaster())))
                .map(componentMasterAttributePair -> orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponentAndProductAttributeMaster(componentMasterAttributePair._1,
                                componentMasterAttributePair._2))
                .flatMap(orderProductComponentsAttributeValues -> orderProductComponentsAttributeValues.stream()
                        .findFirst().map(OrderProductComponentsAttributeValue::getAttributeValues))
                .orElse("");
    }

    // append lsn npa code
    private void appendLnsNpaCode(XSSFWorkbook workbook, XSSFSheet sheet, List<OrderEnrichmentExcel> orderEnrichmentExcels) throws TclCommonException {
        try {
            List<LNSSheetBean> lnsSheetBeans = convertToClass(LNSSheetBean.class, orderEnrichmentExcels);
            Map<String, List<OrderEnrichmentExcel>> lnsOriginCountryWiseData = orderEnrichmentExcels.stream().collect(Collectors.groupingBy(orderEnrichmentExcel -> {
                return ((LNSSheetBean) orderEnrichmentExcel).getOriginCountry();
            }));

            //create hiddensheet to load lns cities and npa code for dropdown
            XSSFSheet lnsHiddenSheet = workbook.createSheet(LNS_NPA_HIDDEN_SHEET);
            Map<String, List<String>> countryWiseNPAData = createCountryWiseNpaCodeData(lnsOriginCountryWiseData);
            loadNpaDataToLnsHiddensheet(workbook, lnsHiddenSheet, countryWiseNPAData, lnsSheetBeans);

            Integer numberOfRowStartValues = 1;
            Integer numberOfRowEndValues = 0;
            Integer countryCount = 1;
            Integer startValue = 1;
            Integer endValue = 0;
            String countryTrace = "";
            for (LNSSheetBean lnsSheetBean : lnsSheetBeans) {
                String originCountry = lnsSheetBean.getOriginCountry();
                if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                    continue;
                Integer numberOfCountry = lnsOriginCountryWiseData.get(originCountry).size();
                Integer numberOfNpa = countryWiseNPAData.get(originCountry).size();
                numberOfRowEndValues = numberOfRowEndValues + numberOfCountry;
                endValue = endValue + numberOfNpa;

                XSSFName namedCell = workbook.createName();
                namedCell.setNameName("lnsNpaDropdownFormula" + countryCount);
                namedCell.setRefersToFormula("lnsNpaHiddenSheet!$B$" + startValue + ":$B$" + endValue);
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);

                Integer cityColumnValue = 0;
                if (isLNSDestinationCountryPresent(orderEnrichmentExcels))
                    cityColumnValue = 3;
                else
                    cityColumnValue = 2;

                XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint("lnsNpaDropdownFormula" + countryCount);
                CellRangeAddressList cityCellRange = new CellRangeAddressList(numberOfRowStartValues, numberOfRowEndValues, cityColumnValue, cityColumnValue);
                XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, cityCellRange);
                workbook.setSheetHidden(workbook.getSheetIndex(LNS_NPA_HIDDEN_SHEET), true);
                validation.setShowErrorBox(true);
                sheet.addValidationData(validation);

                startValue = endValue + 1;
                countryCount++;
                countryTrace = originCountry;
                numberOfRowStartValues = numberOfRowEndValues + 1;
            }
        } catch (Exception e) {
            LOGGER.error("Error in appending LNS city and npa :: {}", e);
            throw new TclCommonException("Error in appending LNS city and npa");
        }
    }

    //load data to npalnsHiddenSheet
    private void loadNpaDataToLnsHiddensheet(XSSFWorkbook workbook, XSSFSheet lnsHiddenSheet, Map<String, List<String>>
            countryNPAData, List<LNSSheetBean> lnsSheetBeans) {
        String countryTrace = "";
        int hiddenSheetRow = 0;
        for (LNSSheetBean lnsSheetBean : lnsSheetBeans) {
            String originCountry = lnsSheetBean.getOriginCountry();
            if (ORIGIN_COUNTRY.equalsIgnoreCase(originCountry) || countryTrace.equals(originCountry))
                continue;
            List<String> npaCode = countryNPAData.get(originCountry);
            for (String npa : npaCode) {
                XSSFRow row = lnsHiddenSheet.createRow(hiddenSheetRow);
                XSSFCell countryCell = row.createCell(0);
                countryCell.setCellValue(originCountry);
                XSSFCell npaCell = row.createCell(1);
                npaCell.setCellValue(npa);
                npaCell.setCellStyle(convertCellStyleToText(workbook));
                hiddenSheetRow++;
            }
            countryTrace = originCountry;
        }
    }

    private Map<String, List<String>> createCountryWiseNpaCodeData(Map<String,
            List<OrderEnrichmentExcel>> lnsOriginCountryWiseData) {
        Map<String, List<String>> countryWiseNPAData = new HashMap<>();
        lnsOriginCountryWiseData.forEach((originCountry, orderEnrichmentExcels) -> {
            String response = null;
            try {
                if (!ORIGIN_COUNTRY.equalsIgnoreCase(originCountry)) {
                    response = (String) mqUtils.sendAndReceive(getCitiesQueue, LNS.concat(COLON).concat(originCountry));
                    GscLocationBean gscProductLocationBean = GscUtils.fromJson(response, GscLocationBean.class);
                    Set<Set<String>> npaCodes = new HashSet<>();
                    gscProductLocationBean.getSources().stream().forEach(gscProductLocationDetailBean -> {
                        List<String> areaCode = gscProductLocationDetailBean.getAreaCode();
                        if (Objects.nonNull(areaCode) && !areaCode.isEmpty()) {
                            String npaCode = areaCode.stream().filter(s -> Objects.nonNull(s) && !s.isEmpty())
                                    .findFirst().get();
                            Set<String> convertedNpaCode = Stream.of(npaCode.split(","))
                                    .map(String::trim)
                                    .collect(Collectors.toSet());
                            npaCodes.add(convertedNpaCode);
                        }
                    });
                    // Sample Input : "33", "012", "NA", "22", "012", "000", "012", "000","000", "012", "22", "12"
                    // sample Output: [000, 012, 12, 22, 33, NA]
                    List<String> collect = npaCodes.stream().flatMap(s -> s.stream())
                            //.map(s -> Integer.parseInt(s))
                            .sorted().distinct()
                            .map(s -> String.valueOf(s)).collect(Collectors.toList());

                    countryWiseNPAData.put(originCountry, collect);
                }
            } catch (TclCommonException e) {
                e.printStackTrace();
                throw new TclCommonRuntimeException("Error in appending LNS npa");
            }
        });
        return countryWiseNPAData;
    }
}
