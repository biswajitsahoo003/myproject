package com.tcl.dias.oms.gsc.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.GscAttachmentTypeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.entity.entities.GlobalOutboundPricingEngineResponse;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.GlobalOutboundPricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscOutboundPricesDownloadBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.pdf.beans.GlobalOutboundDisplayRatesBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscCofOutboundPricesPdfBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscTerminationBean;
import com.tcl.dias.oms.gsc.pricing.beans.GlobalOutboundDisplayRate;
import com.tcl.dias.oms.gsc.pricing.beans.GlobalOutboundPricingResponse;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.PricingRequest;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRCofPdfBean;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_OUTBOUND_EXCEL;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_OUTBOUND_PDF;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_SURCHARGE_PDF;
import static com.tcl.dias.oms.gsc.util.GscConstants.OTHERS;

/**
 * Services to handle all quote related functionality
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class GlobalOutboundRateCardService {

    public static final String PERMANENTLY_BLOCKED_DESTINATIONS = "Permanently Blocked Destinations";
    public static final String NO = "No";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalOutboundRateCardService.class);

    private static final String YES = "YES";
    public static final String CORPORATE = "Corporate";
    public static final String ACTIVE = "Active";
    public static final String PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST_60_60_BILLING_INCREMENTS = "Permanently Blocked Destination.  May be unblocked upon request. + 60/60 Billing Increments";
    public static final String PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST = "Permanently Blocked Destination.  May be unblocked upon request";
    public static final String NA = "NA";
    public static final String BLOCKED = "Blocked";

    @Autowired
    MQUtils mqUtils;

    @Value("${rabbitmq.customer.le.cuid}")
    String customerLeCuIdQueue;

    @Value("${rabbitmq.customer.queue}")
    String customerDetailsQueue;

    @Value("${rabbitmq.customer.le.secs.queue}")
    String customerLeSecsIdQueue;

    @Autowired
    RestClientService restClientService;

    @Value("${pricing.request.gsc.url}")
    String pricingUrl;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    QuoteGscRepository quoteGscRepository;

    @Autowired
    GlobalOutboundPricingDetailsRepository globalOutboundPricingDetailsRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;


    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Value(("${swift.api.container}"))
    String swiftApiContainer;

    @Value("${attachment.queue}")
    String attachmentQueue;

    @Value("${odr.attachment.details}")
    String attachmentDetails;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;

    @Autowired
    GscAttachmentHelper gscAttachmentHelper;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Value("${rabbitmq.gsc.distinct.countries}")
    String distinctCountriesQueue;

    @Autowired
    QuoteGscDetailsRepository quoteGscDetailsRepository;

    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;

    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;

    @Value("${file.download.queue}")
    String fileDownloadQueue;

    @Value("${temp.download.url.expiryWindow}")
    String tempDownloadUrlExpiryWindow;

    @Autowired
    GscQuoteService gscQuoteService;

    @Value("${app.host}")
    String baseUrl;

    @Autowired
    GscPricingFeasibilityService gscPricingFeasibilityService;
    
    @Autowired
    RateFileGenerationService excelGeneratorService;

    /**
     * To get Outbound prices file
     *
     * @param httpServletResponse
     * @param quoteCode
     * @param quoteGscId
     * @param hideSelected
     * @throws TclCommonException
     * @throws IOException
     * @throws DocumentException
     */
    public void getOutboundPricesFile(HttpServletResponse httpServletResponse, String quoteCode, Integer quoteGscId, Byte isAllActive) throws TclCommonException, IOException, DocumentException {
        Objects.requireNonNull(quoteCode, GscConstants.QUOTE_CODE_NULL_MESSAGE);
        if (Objects.isNull(isAllActive)) {
            LOGGER.info("Is All Active is set to 0");
            isAllActive = (byte) 0;
        }
        List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
        LOGGER.info("Quote to le id for generating outbound prices is {} ", quoteToLe.size() == 1 ? quoteToLe.get(0).getId() : "multiple le ids");
        List<QuoteGsc> quoteGscs = new ArrayList<>();
		if (Objects.isNull(quoteGscId)) {
			quoteGscs = quoteGscRepository.findByQuoteToLeIn(quoteToLe);
		} else if(Objects.nonNull(quoteGscId) && Objects.nonNull(quoteCode) && quoteCode.startsWith(TeamsDRConstants.UCAAS_TEAMSDR))
		    //querying quoteGsc based on id for teamsDR as the particular country to be hidden in rate card
			quoteGscs = quoteGscRepository.findAllById(Collections.singletonList(quoteGscId));
        LOGGER.info("No of quote gsc for the given quote to le are {} ", quoteGscs.size());
        if (!CollectionUtils.isEmpty(quoteGscs)) {
            QuoteGsc globalOutBoundQuoteGsc = quoteGscs.stream().filter(quoteGsc -> quoteGsc.getProductName().equalsIgnoreCase("Global Outbound")).findFirst().get();
            GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean = getPdfBean(quoteCode, globalOutBoundQuoteGsc.getQuoteToLe(), globalOutBoundQuoteGsc, isAllActive, quoteGscId);
            gscCofOutboundPricesPdfBean.setApproved(false);
            try {
                String sourceFeed = quoteCode + "---" + Utils.getSource();
                String ikey = EncryptionUtil.encrypt(sourceFeed);
                ikey = URLEncoder.encode(ikey, "UTF-8");
                gscCofOutboundPricesPdfBean.setIkey(ikey);
            } catch (Exception e) {
                LOGGER.error("Suppressing the Outbound document ", e);
            }
            downloadOutboundPricesFile(gscCofOutboundPricesPdfBean, httpServletResponse);
        }
    }

    /**
     * Get pdf bean either from pricing engine response or negotiated response, both responses are in different format
     *
     * @param quoteCode
     * @param quoteToLe
     * @param quoteGscs
     * @param quoteGscId
     * @return
     * @throws TclCommonException
     */
    private GscCofOutboundPricesPdfBean getPdfBean(String quoteCode, QuoteToLe quoteToLe, QuoteGsc quoteGsc, Byte isAllActive, Integer quoteGscId) throws TclCommonException {
        //added for multiple LE scenario
        GlobalOutboundPricingEngineResponse negotiatedPrices;
        if (Objects.isNull(quoteToLe.getQuoteLeCode()))
            negotiatedPrices = globalOutboundPricingDetailsRepository
                    .findByQuoteCodeAndIsNegotiatedPrices(quoteCode, (byte) 1);
        else
            negotiatedPrices = globalOutboundPricingDetailsRepository
                    .findByQuoteLeCodeAndIsNegotiatedPrices(quoteToLe.getQuoteLeCode(), (byte) 1);
        GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean = new GscCofOutboundPricesPdfBean();
        GscCofOutboundPricesPdfBean finalPdfBean = new GscCofOutboundPricesPdfBean();

        if (Objects.nonNull(negotiatedPrices) && Objects.nonNull(negotiatedPrices.getResponseData())) {
            LOGGER.info("Negotiated price response is present");
            setCommonDetails(quoteToLe, finalPdfBean);
            List<GlobalOutboundDisplayRatesBean> displayRatesBeans = GscUtils.fromJson(negotiatedPrices.getResponseData(),
                    new TypeReference<List<GlobalOutboundDisplayRatesBean>>() {
                    });
            List<GlobalOutboundDisplayRatesBean> displayRatesBeansWithStatusAndComments = setStatusAndCommentsOfCountries(quoteToLe, quoteGsc, displayRatesBeans, isAllActive);
            List<GlobalOutboundDisplayRatesBean> finalDisplayRateBean = displayRatesBeansWithStatusAndComments.stream().map(globalOutboundDisplayRatesBean -> {
                if (Objects.isNull(globalOutboundDisplayRatesBean.getComments())) {
                    LOGGER.info("Comments is null");
                    globalOutboundDisplayRatesBean.setComments(NA);
                }
                return globalOutboundDisplayRatesBean;
            }).collect(Collectors.toList());
            finalPdfBean.setGlobalOutboundDisplayRatesBeans(finalDisplayRateBean);
        } else {
            LOGGER.info("Negotiated price response not present, thus generating response from pricing engine");
            GlobalOutboundPricingResponse pricingResponse = generateOutboundPricingResponse(quoteToLe, quoteGsc, null, new ArrayList<>(), quoteGscId);
            gscCofOutboundPricesPdfBean = generateGBPricesBeanFromResponse(quoteToLe, quoteGsc, pricingResponse, isAllActive);
            finalPdfBean = checkForVolumeCommitmentPresent(quoteToLe, quoteGsc, gscCofOutboundPricesPdfBean, quoteGscId);

        }
        return finalPdfBean;
    }

    /**
     * Check volume for commitment
     *
     * @param quoteToLe
     * @param quoteGsc
     * @param gscCofOutboundPricesPdfBean
     * @param quoteGscId
     * @return
     * @throws TclCommonException
     */
    private GscCofOutboundPricesPdfBean checkForVolumeCommitmentPresent(QuoteToLe quoteToLe, QuoteGsc quoteGsc, GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean, Integer quoteGscId) throws TclCommonException {
        LOGGER.info("Checking if Volume Estimate present or not");
        List<QuoteProductComponent> estimateVolumeQuoteProductComponents = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(quoteToLe.getQuote().getId(), GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase(), GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase());
        if (!CollectionUtils.isEmpty(estimateVolumeQuoteProductComponents)) {
            LOGGER.info("estimateVolumeQuoteProductComponents present");
            List<QuoteProductComponentsAttributeValue> countriesQuoteProductComponentAttributeValues = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(estimateVolumeQuoteProductComponents.get(0).getId(), GscConstants.OUTBOUND_COUNTRIES);
            LOGGER.info("no. of countries included in estimate volume is {}", countriesQuoteProductComponentAttributeValues.size());
            List<QuoteProductComponentsAttributeValue> volumeQuoteProductComponentAttributeValues = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(estimateVolumeQuoteProductComponents.get(0).getId(), GscConstants.OUTBOUND_VOLUME);
            LOGGER.info("Volume estimate is {} ", volumeQuoteProductComponentAttributeValues.size());


            if (!CollectionUtils.isEmpty(estimateVolumeQuoteProductComponents) && !CollectionUtils.isEmpty(volumeQuoteProductComponentAttributeValues)) {
                List<String> volumeCountries = countriesQuoteProductComponentAttributeValues.stream().map(a -> a.getAttributeValues()).collect(Collectors.toList());
                LOGGER.info("Pricing response for countries of estimated volume");
                GlobalOutboundPricingResponse volumeEstimatePricingResponse = generateOutboundPricingResponse(quoteToLe, quoteGsc, volumeQuoteProductComponentAttributeValues.get(0).getAttributeValues(), volumeCountries, quoteGscId);
                GscCofOutboundPricesPdfBean volumePricesPdfBean = generateGBPricesBeanFromResponse(quoteToLe, quoteGsc, volumeEstimatePricingResponse, (byte) 0);

                List<String> volumeCountriesInLowerCase = volumeCountries.stream().map(String::toLowerCase).collect(Collectors.toList());
                LOGGER.info("Removing the volume estimate countries in all countries pricing pdf bean");
                List<GlobalOutboundDisplayRatesBean> nonVolumeEstimateCountriesDisplayRates = gscCofOutboundPricesPdfBean.getGlobalOutboundDisplayRatesBeans().stream().filter(globalOutboundDisplayRatesBean -> !volumeCountriesInLowerCase.contains(globalOutboundDisplayRatesBean.getDestinationCountry().toLowerCase())).collect(Collectors.toList());
                LOGGER.info("Adding volume estimate countries pricing response to main response");
                nonVolumeEstimateCountriesDisplayRates.addAll(volumePricesPdfBean.getGlobalOutboundDisplayRatesBeans());
                gscCofOutboundPricesPdfBean.setGlobalOutboundDisplayRatesBeans(nonVolumeEstimateCountriesDisplayRates);
            }
        }
        return gscCofOutboundPricesPdfBean;
    }

    /**
     * Generating bean from response of GB pricing engine response
     *
     * @param quoteToLe
     * @param quoteGscs
     * @param pricingResponse
     * @return
     */
    private GscCofOutboundPricesPdfBean generateGBPricesBeanFromResponse(QuoteToLe quoteToLe, QuoteGsc quoteGsc, GlobalOutboundPricingResponse pricingResponse, Byte isAllActive) {
        GlobalOutboundDisplayRate globalOutboundDisplayRate = pricingResponse.getGlobalOutboundDisplayRate();

        List<GlobalOutboundDisplayRatesBean> globalOutboundDisplayRatesBeans = globalOutboundDisplayRate.getDestinationCountry().stream().map(destCountry -> {
            GlobalOutboundDisplayRatesBean globalOutboundDisplayRatesBean = new GlobalOutboundDisplayRatesBean();
            globalOutboundDisplayRatesBean.setDestinationCountry(destCountry);
            return globalOutboundDisplayRatesBean;
        }).collect(Collectors.toList());

        //Converting list of strings in a bean from pricing engine response to list of beans with single string
        GlobalOutboundDisplayRatesBean[] globalOutboundDisplayRatesBeansArray = new GlobalOutboundDisplayRatesBean[globalOutboundDisplayRatesBeans.size()];
        globalOutboundDisplayRatesBeans.toArray(globalOutboundDisplayRatesBeansArray);

        String[] destinationNames = new String[globalOutboundDisplayRate.getDestinationName().size()];
        globalOutboundDisplayRate.getDestinationName().toArray(destinationNames);

        String[] displayCurrencies = new String[globalOutboundDisplayRate.getDisplayCurrency().size()];
        globalOutboundDisplayRate.getDisplayCurrency().toArray(displayCurrencies);

        String[] phoneTypes = new String[globalOutboundDisplayRate.getPhoneType().size()];
        globalOutboundDisplayRate.getPhoneType().toArray(phoneTypes);

        String[] prices = new String[globalOutboundDisplayRate.getPrice().size()];
        globalOutboundDisplayRate.getPrice().toArray(prices);

        String[] destIds = new String[globalOutboundDisplayRate.getDestId().size()];
        globalOutboundDisplayRate.getDestId().toArray(destIds);

        String[] comments = new String[globalOutboundDisplayRate.getComments().size()];
        globalOutboundDisplayRate.getComments().toArray(comments);

        String[] status = new String[globalOutboundDisplayRate.getServiceLevel().size()];
        globalOutboundDisplayRate.getServiceLevel().toArray(status);

        LOGGER.info("Setting Destination Names");
        IntStream.range(0, destinationNames.length).forEach(index -> globalOutboundDisplayRatesBeansArray[index].setDestinationName(StringUtils.capitalize(destinationNames[index])));
        LOGGER.info("Setting Display Currencies");
        IntStream.range(0, displayCurrencies.length).forEach(index -> globalOutboundDisplayRatesBeansArray[index].setDisplayCurrency(displayCurrencies[index]));
        LOGGER.info("Setting Phone types");
        IntStream.range(0, phoneTypes.length).forEach(index -> globalOutboundDisplayRatesBeansArray[index].setPhoneType(StringUtils.capitalize(phoneTypes[index])));
        LOGGER.info("Setting Prices");
        IntStream.range(0, prices.length).forEach(index -> globalOutboundDisplayRatesBeansArray[index].setPrice(prices[index]));
        LOGGER.info("Setting destIds");
        IntStream.range(0, destIds.length).forEach(index -> globalOutboundDisplayRatesBeansArray[index].setDestId(destIds[index]));
        LOGGER.info("Setting status");
        IntStream.range(0, status.length).forEach(index -> globalOutboundDisplayRatesBeansArray[index].setStatus(status[index]));
        LOGGER.info("Setting comments");
        IntStream.range(0, comments.length).forEach(index -> globalOutboundDisplayRatesBeansArray[index].setComments(comments[index]));

        GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean = new GscCofOutboundPricesPdfBean();
        List<GlobalOutboundDisplayRatesBean> displayRatesBeans = setStatusAndCommentsOfCountries(quoteToLe, quoteGsc, globalOutboundDisplayRatesBeans, isAllActive);
        List<GlobalOutboundDisplayRatesBean> finalDisplayRateBean = displayRatesBeans.stream().map(globalOutboundDisplayRatesBean -> {
            if (Objects.isNull(globalOutboundDisplayRatesBean.getComments())) {
                LOGGER.info("Comments is null");
                globalOutboundDisplayRatesBean.setComments(NA);
            }
            return globalOutboundDisplayRatesBean;
        }).collect(Collectors.toList());
        gscCofOutboundPricesPdfBean.setGlobalOutboundDisplayRatesBeans(finalDisplayRateBean);
        setCommonDetails(quoteToLe, gscCofOutboundPricesPdfBean);
        return gscCofOutboundPricesPdfBean;
    }

    /**
     * Set precision for prices
     *
     * @param finalDisplayRateBean
     */
    private void setPrecisionForPrices(List<GlobalOutboundDisplayRatesBean> finalDisplayRateBean) {
       finalDisplayRateBean.stream().forEach(globalOutboundDisplayRatesBean -> {
           if(Double.valueOf(globalOutboundDisplayRatesBean.getPrice()) < 0.00095){
               globalOutboundDisplayRatesBean.setPrice(getPrecisionForLessValues(Double.valueOf(globalOutboundDisplayRatesBean.getPrice()), 4));
           }
           else{
               globalOutboundDisplayRatesBean.setPrice(getPrecision(Double.valueOf(globalOutboundDisplayRatesBean.getPrice()), 4).toString());
           }
        });
    }

    /**
     * Set status of countries selected as blocked in pdf bean
     *  @param quoteGscs
     * @param quoteToLe
     * @param globalOutboundDisplayRatesBeans
     */
    private List<GlobalOutboundDisplayRatesBean> setStatusAndCommentsOfCountries(QuoteToLe quoteToLe, QuoteGsc quoteGsc, List<GlobalOutboundDisplayRatesBean> globalOutboundDisplayRatesBeans, Byte isAllActive) {
        List<GlobalOutboundDisplayRatesBean> displayRatesBeansWithStatus = new ArrayList<>();
        if (isAllActive.equals((byte) 1)) {
            LOGGER.info("All countries are set to active since pdf is being downloaded in select services page");
            displayRatesBeansWithStatus = globalOutboundDisplayRatesBeans.stream()
                    .map(globalOutboundDisplayRatesBean -> {
                        if (Objects.nonNull(globalOutboundDisplayRatesBean.getStatus()) && globalOutboundDisplayRatesBean.getStatus().contains(CORPORATE)) {
                            //From pricing engine we get status as corporate for active
                            globalOutboundDisplayRatesBean.setStatus(ACTIVE);
                        }
                        return globalOutboundDisplayRatesBean;
                    }).collect(Collectors.toList());
            setPrecisionForPrices(displayRatesBeansWithStatus);
            return displayRatesBeansWithStatus;
        } else {
            LOGGER.info("Global outbound display rates beans from excel size is {}", globalOutboundDisplayRatesBeans.size());
            List<String> selectedCountries = getSelectedCountries(quoteGsc);

            String addPermanentlyBlockedDestination = getAddPermanentlyBlockedDestinationsAttribute(quoteToLe);
            String isSelectAllOption = getIsSelectAllOptionsAttribute(quoteToLe);

            if (!CollectionUtils.isEmpty(selectedCountries) && YES.equalsIgnoreCase(isSelectAllOption) && YES.equalsIgnoreCase(addPermanentlyBlockedDestination)) {
                LOGGER.info("All countries are set to active, since customer has chosen permanently bloced destinations to be added");
                displayRatesBeansWithStatus = globalOutboundDisplayRatesBeans.stream()
                        .map(globalOutboundDisplayRatesBean -> {
                            globalOutboundDisplayRatesBean.setStatus(ACTIVE);
                            if (PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST_60_60_BILLING_INCREMENTS.equalsIgnoreCase(globalOutboundDisplayRatesBean.getComments())) {
                                globalOutboundDisplayRatesBean.setComments(NA);
                            } else if (PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST.equalsIgnoreCase(globalOutboundDisplayRatesBean.getComments())) {
                                globalOutboundDisplayRatesBean.setComments(NA);
                            } else if (Objects.nonNull(globalOutboundDisplayRatesBean.getComments()) && globalOutboundDisplayRatesBean.getComments().contains(PERMANENTLY_BLOCKED_DESTINATIONS)) {
                                String replacedComment = globalOutboundDisplayRatesBean.getComments().replaceAll(PERMANENTLY_BLOCKED_DESTINATIONS, "");
                                globalOutboundDisplayRatesBean.setComments(replacedComment);
                            }
                            return globalOutboundDisplayRatesBean;
                        }).collect(Collectors.toList());
            } else if(!CollectionUtils.isEmpty(selectedCountries) && YES.equalsIgnoreCase(isSelectAllOption) && NO.equalsIgnoreCase(addPermanentlyBlockedDestination)){
                LOGGER.info("User hasnt chosen permanently blocked destinations to be added in select all option");
                displayRatesBeansWithStatus = globalOutboundDisplayRatesBeans.stream()
                        .map(globalOutboundDisplayRatesBean -> {
                            if (Objects.nonNull(globalOutboundDisplayRatesBean.getStatus()) && globalOutboundDisplayRatesBean.getStatus().contains(CORPORATE)) {
                                //From pricing engine we get status as corporate for active
                                globalOutboundDisplayRatesBean.setStatus(ACTIVE);
                            }
                            return globalOutboundDisplayRatesBean;
                        }).collect(Collectors.toList());
            }
            else if(!CollectionUtils.isEmpty(selectedCountries) && NO.equalsIgnoreCase(isSelectAllOption)){
                LOGGER.info("User hasnt chosen select all option");
                List<String> finalSelectedCountries = selectedCountries;
                displayRatesBeansWithStatus = globalOutboundDisplayRatesBeans.stream()
                        .map(globalOutboundDisplayRatesBean -> {
                            if (finalSelectedCountries.contains(globalOutboundDisplayRatesBean.getDestinationCountry().toLowerCase())) {
                                globalOutboundDisplayRatesBean.setStatus(ACTIVE);
                                if (PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST_60_60_BILLING_INCREMENTS.equalsIgnoreCase(globalOutboundDisplayRatesBean.getComments())) {
                                    globalOutboundDisplayRatesBean.setComments(NA);
                                } else if (PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST.equalsIgnoreCase(globalOutboundDisplayRatesBean.getComments())) {
                                    globalOutboundDisplayRatesBean.setComments(NA);
                                } else if (Objects.nonNull(globalOutboundDisplayRatesBean.getComments()) && globalOutboundDisplayRatesBean.getComments().contains(PERMANENTLY_BLOCKED_DESTINATIONS)) {
                                    String replacedComment = globalOutboundDisplayRatesBean.getComments().replaceAll(PERMANENTLY_BLOCKED_DESTINATIONS, "");
                                    globalOutboundDisplayRatesBean.setComments(replacedComment);
                                }
                            } else {
                                globalOutboundDisplayRatesBean.setStatus(BLOCKED);
                            }
                            return globalOutboundDisplayRatesBean;
                        }).collect(Collectors.toList());
            }

            setPrecisionForPrices(displayRatesBeansWithStatus);
            return displayRatesBeansWithStatus;
        }
    }

//    private void setComments(GlobalOutboundDisplayRatesBean globalOutboundDisplayRatesBean) {
//        if (PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST_60_60_BILLING_INCREMENTS.equalsIgnoreCase(globalOutboundDisplayRatesBean.getComments())) {
//            globalOutboundDisplayRatesBean.setComments(NA);
//        } else if (PERMANENTLY_BLOCKED_DESTINATION_MAY_BE_UNBLOCKED_UPON_REQUEST.equalsIgnoreCase(globalOutboundDisplayRatesBean.getComments())) {
//            globalOutboundDisplayRatesBean.setComments(NA);
//        } else if (Objects.nonNull(globalOutboundDisplayRatesBean.getComments()) && globalOutboundDisplayRatesBean.getComments().contains(PERMANENTLY_BLOCKED_DESTINATIONS)) {
//            String replacedComment = globalOutboundDisplayRatesBean.getComments().replaceAll(PERMANENTLY_BLOCKED_DESTINATIONS, "");
//            globalOutboundDisplayRatesBean.setComments(replacedComment);
//        }else if(Objects.isNull(globalOutboundDisplayRatesBean.getComments())){
//            globalOutboundDisplayRatesBean.setComments(NA);
//        }
//    }

    /**
     *  get is select all options attribute
     *
     * @param quoteToLe
     * @return
     */
    private String getIsSelectAllOptionsAttribute(QuoteToLe quoteToLe) {
        String isSelectAllOption = NO;
        List<QuoteLeAttributeValue> isSelectAllOptions = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.SELECT_ALL_OF_GLOBAL_OUTBOUND);
        if (!CollectionUtils.isEmpty(isSelectAllOptions)) {
            isSelectAllOption = isSelectAllOptions.stream().findFirst().get().getAttributeValue();
            LOGGER.info("Value of select all option is {}", isSelectAllOption);
        }
        return isSelectAllOption;
    }

    /**
     * get Add permanently blocked destinations attribute
     *
     * @param quoteToLe
     * @return
     */
    private String getAddPermanentlyBlockedDestinationsAttribute(QuoteToLe quoteToLe) {
        String addPermanentlyBlockedDestination = NO;
        List<QuoteLeAttributeValue> addPermanentlyBlockedDestinations = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.ADD_PERMANENTLY_BLOCKED_DESTINATIONS_OF_GLOBAL_OUTBOUND);
        if (!CollectionUtils.isEmpty(addPermanentlyBlockedDestinations)) {
            addPermanentlyBlockedDestination = addPermanentlyBlockedDestinations.stream().findFirst().get().getAttributeValue();
            LOGGER.info("Value of add permanently blocked destination is {}", addPermanentlyBlockedDestination);
        }
        return addPermanentlyBlockedDestination;
    }

    /**
     * get selected countries
     *
     * @param quoteGsc
     * @return
     */
    private List<String> getSelectedCountries(QuoteGsc quoteGsc) {
        List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGsc(quoteGsc);
        List<String> selectedCountries = new ArrayList<>();
        if (!CollectionUtils.isEmpty(quoteGscDetails)) {
            selectedCountries = quoteGscDetails.stream().map(QuoteGscDetail::getDest).map(String::toLowerCase).collect(Collectors.toList());
        }
        return selectedCountries;
    }

    /**
     * Common details for cof
     *
     * @param quoteToLe
     * @param gscCofOutboundPricesPdfBean
     */
    private void setCommonDetails(QuoteToLe quoteToLe, GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean) {
        gscCofOutboundPricesPdfBean.setCofRefernceNumber(quoteToLe.getQuote().getQuoteCode());
        gscCofOutboundPricesPdfBean.setOrderType(quoteToLe.getQuoteType());
        Date date = new Date();
        gscCofOutboundPricesPdfBean.setOrderDate(DateUtil.convertDateToMMMString(date));
        gscCofOutboundPricesPdfBean.setPaymentCurrency(quoteToLe.getCurrencyCode());
    }

    /**
     * Generate outbound pricing response
     *
     * @param quoteToLe
     * @param quoteGscs
     * @param quoteGscId
     * @return
     * @throws TclCommonException
     */
    private GlobalOutboundPricingResponse generateOutboundPricingResponse(QuoteToLe quoteToLe, QuoteGsc quoteGsc, String volume, List<String> volumeCountries, Integer quoteGscId) throws TclCommonException {
        LOGGER.info("Generate outbound pricing response from pricing engine");
        PricingRequest pricingRequest = constructPricingRequest(quoteToLe, quoteGsc, volume, volumeCountries, quoteGscId);
        return generatePricingResponse(quoteToLe, pricingRequest);
    }

    /**
     * construct pricing request
     *
     * @param quoteToLe
     * @param quoteGscs
     * @param volume
     * @param volumeCountries
	 * @param quoteGscId
     * @return
     * @throws TclCommonException
     */
    private PricingRequest constructPricingRequest(QuoteToLe quoteToLe, QuoteGsc quoteGsc, String volume, List<String> volumeCountries, Integer quoteGscId) throws TclCommonException {
        LOGGER.info("Construct pricing request for global outbound");
        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setAccountRTMCust(GscConstants.DIRECT);
        pricingRequest.setOpportunityTerm(String.valueOf(getMonthsforOpportunityTerms(quoteToLe.getTermInMonths())));
        if (Objects.nonNull(volume)) {
            Double volumeinMillions = Double.valueOf(volume) * 1000000;
            pricingRequest.setVolumeProjected(volumeinMillions.toString());
        } else {
            pricingRequest.setVolumeProjected(GscConstants.DEFAULT_VOLUME.toString());
        }
        pricingRequest.setCuLeId(getCustomerLeCuId(quoteToLe));
        setAccountDetails(pricingRequest, quoteToLe);
        setNewAttributes(pricingRequest, quoteToLe);
        setMacdAttributes(pricingRequest, quoteToLe, quoteGsc);
        setPartnerAttributes(pricingRequest, quoteToLe);
        setGlobalOutboundAttributes(pricingRequest, quoteToLe, quoteGsc, volumeCountries, quoteGscId);
        return pricingRequest;
    }

    /**
     * Set global out bound details in pricing
     *
     * @param pricingRequest
     * @param volumeCountries
     * @param quoteToLe
     * @param quoteGsc
     * @param quoteGscId
     */
    private void setGlobalOutboundAttributes(PricingRequest pricingRequest, QuoteToLe quoteToLe, QuoteGsc quoteGsc, List<String> volumeCountries, Integer quoteGscId) {
        pricingRequest.setAccessType(quoteGsc.getAccessType().toLowerCase());
        pricingRequest.setGlobalOutboundRequested("1");
        pricingRequest.setGlobalOutboundCatalogFlag("1");
        if (CollectionUtils.isEmpty(volumeCountries)) {
            List<String> globalOutboundCountries = distinctCountriesQueue("Global Outbound");
            // hiding selected countries from list (requirement for TeamsDR - multiLE)
            if (Objects.nonNull(quoteGscId) && quoteToLe.getQuote().getQuoteCode().startsWith(TeamsDRConstants.UCAAS_TEAMSDR)) {
                List<String> selectedCountries = quoteGsc.getQuoteGscDetails().stream().filter(quoteGscDetail -> Objects.nonNull(quoteGscDetail.getDest()))
                        .map(QuoteGscDetail::getDest).collect(Collectors.toList());
                globalOutboundCountries.removeAll(selectedCountries);
            }
            pricingRequest.setDestinationGlobalOutbound(globalOutboundCountries);
        } else {
            pricingRequest.setDestinationGlobalOutbound(volumeCountries);
        }

        // List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.PAYMENT_CURRENCY);
        // setting default currency value as USD (TEAMS DR - Multiple LE) else quoteToLe
        // wise currency
        if (Objects.isNull(quoteGscId) && quoteToLe.getQuote().getQuoteCode().startsWith(TeamsDRConstants.UCAAS_TEAMSDR)) {
            LOGGER.info("Currency code for quotetole is {}", CommonConstants.USD);
            pricingRequest.setGlobalOutboundCatalogCurrency(CommonConstants.USD);
        } else {
            LOGGER.info("Currency code for quotetole is {}", quoteToLe.getCurrencyCode());
            pricingRequest.setGlobalOutboundCatalogCurrency(quoteToLe.getCurrencyCode());
        }

    }

    /**
     * Generate pricing response from pricing engine
     *
     * @param quoteToLe
     * @param pricingRequest
     * @return
     * @throws TclCommonException
     */
    private GlobalOutboundPricingResponse generatePricingResponse(QuoteToLe quoteToLe, PricingRequest pricingRequest) throws TclCommonException {
        GlobalOutboundPricingResponse pricingResponse = new GlobalOutboundPricingResponse();
        try {
            GlobalOutboundPricingEngineResponse pricingEngineResponseTable = persistRequestPricingDetails(pricingRequest, GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, quoteToLe, (byte) 0);

            String request = Utils.convertObjectToJson(pricingRequest);
            LOGGER.info("Pricing GSC input :: {}", request);
            RestResponse restResponse = restClientService.post(pricingUrl, request);
            LOGGER.info("Pricing GSC URL :: {}", pricingUrl);
            if (restResponse.getStatus() == Status.SUCCESS) {
                String response = restResponse.getData();
                LOGGER.info("Pricing GSC output :: {}", response);
                response = response.replaceAll("\"NA\"", "\"0\"");
                LOGGER.info("Pricing GSC output :: {}", response);
                pricingResponse = Utils.convertJsonToObject(response, GlobalOutboundPricingResponse.class);
                persistResponsePricingDetails(pricingEngineResponseTable, pricingResponse, GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, quoteToLe);
            } else {
                LOGGER.warn("Pricing Response Status is not Success");
                throw new TclCommonException(ExceptionConstants.PRICING_VALIDATION_ERROR);
            }
        } catch (Exception e) {
            LOGGER.warn("Error in getting pricing response" + ExceptionUtils.getStackTrace(e));
            throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
        }
        return pricingResponse;
    }

    /**
     * get months of opportunity terms from terms period
     *
     * @param termPeriod
     * @return
     */
    private Integer getMonthsforOpportunityTerms(String termPeriod) {
        LOGGER.info("Term period is {} " + termPeriod);
        String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
        Integer month = Integer.valueOf(reg[0]);
        if (reg.length > 0) {
            if (termPeriod.contains("year")) {
                return month * 12;
            }
        }
        LOGGER.info("Months calculated is {} " + month);
        return month;
    }

    /**
     * get customer le cuid
     *
     * @param quoteToLe
     * @return
     * @throws TclCommonException
     */
    private String getCustomerLeCuId(QuoteToLe quoteToLe) throws TclCommonException {
        Integer erfCusCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeCuId {} {} :", erfCusCustomerLegalEntityId,
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String cuId = "";
        try {
            if (Objects.nonNull(erfCusCustomerLegalEntityId)) {
                cuId = (String) mqUtils.sendAndReceive(customerLeCuIdQueue, String.valueOf(erfCusCustomerLegalEntityId));
                LOGGER.info("CUID is {} " + cuId);
            }
            return cuId;
        } catch (TclCommonException e) {
            LOGGER.warn("Error Occured while getting CustomerLe CUID :: {}", e.getStackTrace());
        }
        return cuId;
    }

    /**
     * set account details of customer in pricing request
     *
     * @param pricingRequest
     * @param quoteToLe
     * @throws TclCommonException
     */
    private void setAccountDetails(PricingRequest pricingRequest, QuoteToLe quoteToLe) throws TclCommonException {
        LOGGER.info("Set account details for pricing request");
        CustomerDetailsBean customerDetails = getCustomerDetails(quoteToLe);
        if (Objects.nonNull(customerDetails)) {
            customerDetails.getCustomerAttributes().stream().forEach(attribute -> {
                if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
                    pricingRequest.setAccountIdWith18Digit(attribute.getValue());
                } else if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
                    pricingRequest.setCustomerSegment(attribute.getValue());
                } else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
                    pricingRequest.setSalesOrg(attribute.getValue());
                }
            });
        }
    }

    /**
     * get customer details
     *
     * @param quoteToLe
     * @return
     * @throws TclCommonException
     */
    private CustomerDetailsBean getCustomerDetails(QuoteToLe quoteToLe) throws TclCommonException {
        String customerResponse = null;
        CustomerDetailsBean detailsBean = null;
        Integer customerId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId();
        if (customerId != 0) {
            try {
                LOGGER.info("MDC Filter token value in before Queue call for customer id getCustomerDetails {} {} :", customerId, MDC.get(CommonConstants.MDC_TOKEN_KEY));
                customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
                if (StringUtils.isNotBlank(customerResponse)) {
                    detailsBean = Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);
                }
            } catch (Exception e) {
                throw new TclCommonException(ExceptionConstants.CUSTOMER_DETAILS_EMPTY, e);
            }
        } else {
            throw new TclCommonException(ExceptionConstants.CUSTOMER_LE_ID_EMPTY);
        }
        return detailsBean;
    }

    /**
     * Set attributes of new order
     *
     * @param pricingRequest
     * @param quoteToLe
     */
    private void setNewAttributes(PricingRequest pricingRequest, QuoteToLe quoteToLe) {
        if (GscConstants.NEW.equalsIgnoreCase(quoteToLe.getQuoteType())) {
            LOGGER.info("Setting attributes of new order");
            pricingRequest.setQuoteTypeQuote(GscConstants.QUOTE_TYPE_NEW_ORDER);
            pricingRequest.setMacdNewCountry("0");
            pricingRequest.setOrgId(getOrgId(quoteToLe));
        }
    }

    /**
     * set attributes of macd order
     *
     * @param pricingRequest
     * @param quoteToLe
     * @param quoteGscs
     */
    private void setMacdAttributes(PricingRequest pricingRequest, QuoteToLe quoteToLe, QuoteGsc quoteGsc) {
        if (GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
            LOGGER.info("Setting attributes of macd order");
            pricingRequest.setQuoteTypeQuote(GscConstants.ORDER_TYPE_MACD);
            setMacdNewCountry(pricingRequest, quoteToLe, quoteGsc);
            setOrgIdForMACD(pricingRequest, quoteToLe);
        }
    }

    /**
     * set if macd new country in pricing request
     *
     * @param pricingRequest
     * @param quoteToLe
     * @param quoteGscs
     */
    private void setMacdNewCountry(PricingRequest pricingRequest, QuoteToLe quoteToLe, QuoteGsc quoteGsc) {
        pricingRequest.setMacdNewCountry("0");
        quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
            if (Objects.isNull(quoteGscDetail.getType()) && GscConstants.ADD_COUNTRY.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
                pricingRequest.setMacdNewCountry("1");
            }
        });
        LOGGER.info("macd new country in pricing request {} " + pricingRequest.getMacdNewCountry());
    }

    /**
     * set org id for macd
     *
     * @param pricingRequest
     * @param quoteToLe
     */
    private void setOrgIdForMACD(PricingRequest pricingRequest, QuoteToLe quoteToLe) {
        QuoteLeAttributeValue gscMultiMacdAttribute = quoteLeAttributeValueRepository.findByQuoteIDAndMstOmsAttributeName(quoteToLe.getQuote().getId(), LeAttributesConstants.IS_GSC_MULTI_MACD);
        if (Objects.nonNull(gscMultiMacdAttribute) && YES.equalsIgnoreCase(gscMultiMacdAttribute.getAttributeValue())) {
            LOGGER.info("Updating orgid for gsc multi macd order");
            pricingRequest.setOrgId(getOrgId(quoteToLe));
            LOGGER.info("Org id for pricing request is {} ", pricingRequest.getOrgId());
        } else {
            LOGGER.info("Updating orgid for non gsc multi macd order");
            getSECSId(pricingRequest, quoteToLe);
        }
    }

    /**
     * set secs id as org id
     *
     * @param pricingRequest
     * @param quoteToLe
     */
    private void getSECSId(PricingRequest pricingRequest, QuoteToLe quoteToLe) {
        Set<QuoteLeAttributeValue> quoteToLeAttributeValues = quoteToLe.getQuoteLeAttributeValues();
        if (Objects.nonNull(quoteToLeAttributeValues) && !CollectionUtils.isEmpty(quoteToLeAttributeValues)) {
            Optional<QuoteLeAttributeValue> quoteLeAttributeValue = quoteToLeAttributeValues.stream().filter(attr -> (GscAttributeConstants.ATTR_CUSTOMER_SECS_ID.equalsIgnoreCase(attr.getDisplayValue()))).findAny();
            quoteLeAttributeValue.ifPresent(attr -> {
                LOGGER.info("org id is {} ", attr.getAttributeValue());
                pricingRequest.setOrgId(attr.getAttributeValue());
            });
        }
    }

    /**
     * get org id
     *
     * @param quoteToLe
     * @return
     */
    private String getOrgId(QuoteToLe quoteToLe) {
        String orgId = "";
        Integer erfCusCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeCuId {} {} :", erfCusCustomerLegalEntityId, MDC.get(CommonConstants.MDC_TOKEN_KEY));
        try {
            if (Objects.nonNull(erfCusCustomerLegalEntityId)) {
                orgId = (String) mqUtils.sendAndReceive(customerLeSecsIdQueue, String.valueOf(erfCusCustomerLegalEntityId));
                LOGGER.info("org is is {} ", orgId);
            }
        } catch (TclCommonException e) {
            LOGGER.warn("Error Occured while getting CustomerLe SECS ID :: {}", e.getStackTrace());
        }
        return orgId;
    }


    /**
     * set partner attributes in pricing request
     *
     * @param pricingRequest
     * @param quoteToLe
     */
    private void setPartnerAttributes(PricingRequest pricingRequest, QuoteToLe quoteToLe) {
        if (Objects.nonNull(quoteToLe.getClassification()) && quoteToLe.getClassification().equalsIgnoreCase(PartnerConstants.SELL_THROUGH_CLASSIFICATION)) {
            pricingRequest.setQuoteTypePartner(quoteToLe.getClassification());
        } else {
            pricingRequest.setQuoteTypePartner("None");
        }
    }

    /**
     * create or update pricing request in table from pricing engine
     *
     * @param request
     * @param type
     * @param quote
     * @throws TclCommonException
     */
    private GlobalOutboundPricingEngineResponse persistRequestPricingDetails(PricingRequest request, String type,
            QuoteToLe quoteToLe, Byte isNegoitatedPrices) throws TclCommonException {
        //added for multiple LE scenario
        GlobalOutboundPricingEngineResponse pricingDetail;
        if (Objects.isNull(quoteToLe.getQuoteLeCode()))
            pricingDetail = globalOutboundPricingDetailsRepository
                    .findByQuoteCodeAndIsNegotiatedPrices(quoteToLe.getQuote().getQuoteCode(), isNegoitatedPrices);
        else
            pricingDetail = globalOutboundPricingDetailsRepository
                    .findByQuoteLeCodeAndIsNegotiatedPrices(quoteToLe.getQuoteLeCode(), isNegoitatedPrices);
        if (Objects.nonNull(pricingDetail)) {
            pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
            pricingDetail.setRequestData(Utils.convertObjectToJson(request));
        } else {
            LOGGER.info("New pricing response generated for the quote");
            pricingDetail = new GlobalOutboundPricingEngineResponse();
            pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
            pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
            pricingDetail.setPricingType(type);
            pricingDetail.setRequestData(Utils.convertObjectToJson(request));
            //added for multiple LE scenario
            pricingDetail.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
            pricingDetail.setIsNegotiatedPrices((byte) 0);
        }
        globalOutboundPricingDetailsRepository.save(pricingDetail);
        return pricingDetail;
    }

    /**
     * create or update pricing response in table from pricing engine
     *
     * @param pricingEngineResponseTable
     * @param response
     * @param type
     * @param quote
     * @throws TclCommonException
     */
    private void persistResponsePricingDetails(GlobalOutboundPricingEngineResponse pricingEngineResponseTable, GlobalOutboundPricingResponse response, String type, QuoteToLe quoteToLe) throws TclCommonException {
        GlobalOutboundPricingEngineResponse pricingDetail = globalOutboundPricingDetailsRepository.findById(pricingEngineResponseTable.getId()).get();
        pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
        pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
        pricingDetail.setPricingType(type);
        pricingDetail.setResponseData(Utils.convertObjectToJson(response));
        pricingDetail.setIsNegotiatedPrices((byte) 0);
        pricingDetail.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
        //added for multiple LE scenario
        pricingDetail.setQuoteLeCode(quoteToLe.getQuoteLeCode());
        globalOutboundPricingDetailsRepository.save(pricingDetail);
    }

    /**
     * downoad outbound prices file
     *
     * @param gscCofOutboundPricesPdfBean
     * @param response
     * @return
     * @throws DocumentException
     * @throws IOException
     * @throws TclCommonException
     */
    private HttpServletResponse downloadOutboundPricesFile(GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean, HttpServletResponse response)
            throws DocumentException, IOException, TclCommonException {
        LOGGER.info("Downloading Outbound prices File");
        byte[] outArray = null;
        String fileName = GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF;
        if (!CollectionUtils.isEmpty(gscCofOutboundPricesPdfBean.getGscQuoteConfigurationBeans())) {
            LOGGER.info("Domestic outbound file name");
            fileName = GscAttachmentTypeConstants.GSC_DOMESTIC_OUTBOUND_PRICES_PDF;
        }
        outArray = generatePdfByteArrayFromBean(gscCofOutboundPricesPdfBean);
        setHttpServletResponse(response, outArray, fileName, MediaType.APPLICATION_PDF_VALUE);
        try {
            FileCopyUtils.copy(outArray, response.getOutputStream());
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return response;
    }

    /**
     * Set http servlet response details
     *
     * @param response
     * @param outArray
     * @param fileName
     */
    private void setHttpServletResponse(HttpServletResponse response, byte[] outArray, String fileName, String contentType) {
        response.reset();
        response.setContentType(contentType);
        response.setContentLength(outArray.length);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires:", "0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }

    /**
     * Generate and save outbound prices file
     *
     * @param quoteCode
     * @param quoteGscId
     * @return
     * @throws TclCommonException
     * @throws DocumentException
     * @throws IOException
     */
    public GscOutboundPricesDownloadBean generateAndSaveOutboundPricesFile(String quoteCode, Integer quoteLeId) throws TclCommonException, DocumentException, IOException {
        GscOutboundPricesDownloadBean gscOutboundPricesDownloadBean = new GscOutboundPricesDownloadBean();
        Objects.requireNonNull(quoteCode, GscConstants.QUOTE_CODE_NULL_MESSAGE);
        QuoteToLe quoteToLe = null;
        if (Objects.isNull(quoteLeId))
            quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(quoteCode).stream().findAny().get();
        else
            quoteToLe = quoteToLeRepository.findById(quoteLeId).orElse(null);
        LOGGER.info("Quote to le id for generating outbound prices is {} ", quoteLeId);
        List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
        LOGGER.info("No of quote gsc for the given quote to le are {} ", quoteGscs.size());
        if (!CollectionUtils.isEmpty(quoteGscs)) {
            QuoteGsc globalOutBoundQuoteGsc = quoteGscs.stream().filter(quoteGsc -> quoteGsc.getProductName().equalsIgnoreCase("Global Outbound")).findFirst().get();
            GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean = getPdfBean(quoteCode, quoteToLe, globalOutBoundQuoteGsc, (byte) 0, null);
            gscCofOutboundPricesPdfBean.setApproved(true);
            gscCofOutboundPricesPdfBean.setIsObjectStorage(swiftApiEnabled);
            try {
                String sourceFeed = quoteToLe.getQuote().getQuoteCode() + "---" + Utils.getSource();
                String ikey = EncryptionUtil.encrypt(sourceFeed);
                ikey = URLEncoder.encode(ikey, "UTF-8");
                gscCofOutboundPricesPdfBean.setIkey(ikey);
            } catch (Exception e) {
                LOGGER.error("Suppressing the Outbound document ", e);
            }
            InputStream inputStream = new ByteArrayInputStream(generatePdfByteArrayFromBean(gscCofOutboundPricesPdfBean));
            Integer erfAttachmentId = saveGeneratedFile(quoteToLe, inputStream, GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF, GSIP_OUTBOUND_PDF);
            gscOutboundPricesDownloadBean.setAttachmentId(erfAttachmentId);
            generateAndSaveDomesticVoiceOutboundPrices(gscOutboundPricesDownloadBean, quoteToLe, quoteGscs);

            //newly added for Rate file generation in O2C
            InputStream excelInputStream = new ByteArrayInputStream(excelGeneratorService.generateExcelByteArrayFromBean(gscCofOutboundPricesPdfBean, null));
            Integer erfAttachmentId1 = saveGeneratedFile(quoteToLe, excelInputStream, GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_EXCL, GSIP_OUTBOUND_EXCEL);

        }
        return gscOutboundPricesDownloadBean;
    }

    /**
     * generate and save domestic voice outbound prices file
     *
     * @param gscOutboundPricesDownloadBean
     * @param quoteToLe
     * @param quoteGscs
     * @throws DocumentException
     * @throws IOException
     * @throws TclCommonException
     */
    private void generateAndSaveDomesticVoiceOutboundPrices(GscOutboundPricesDownloadBean gscOutboundPricesDownloadBean, QuoteToLe quoteToLe, List<QuoteGsc> quoteGscs) throws DocumentException, IOException, TclCommonException {
        LOGGER.info("Checking domestic voice to generate prices file");
        List<QuoteGsc> domesticVoicQuoteGscs = quoteGscs.stream().filter(quoteGsc -> quoteGsc.getProductName().equalsIgnoreCase("Domestic Voice")).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(domesticVoicQuoteGscs)) {
            QuoteGsc domesticVoiceQuoteGsc = domesticVoicQuoteGscs.stream().findFirst().get();
            LOGGER.info("For domestic voice quote gsc generating outbound prices");
            GscCofOutboundPricesPdfBean domesticVoicePdfBean = new GscCofOutboundPricesPdfBean();
            domesticVoicePdfBean.setApproved(true);
            domesticVoicePdfBean.setIsObjectStorage(swiftApiEnabled);
            setCommonDetails(quoteToLe, domesticVoicePdfBean);

            List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGsc(domesticVoiceQuoteGsc);
            List<GscQuoteConfigurationBean> gscQuoteConfigurationBeans = new ArrayList<>();
            getDomesticVoiceBeans(quoteGscDetails, gscQuoteConfigurationBeans);
            setCommonDetails(quoteToLe, domesticVoicePdfBean);
            domesticVoicePdfBean.setGscQuoteConfigurationBeans(gscQuoteConfigurationBeans);
//            List<GscQuoteConfigurationBean> gscQuoteConfigurationBeans = gscQuoteService.getGscQuoteConfigurationBean(domesticVoiceQuoteGsc);
//            setDomesticVoiceTerminationDetails(gscQuoteConfigurationBeans);
//            domesticVoicePdfBean.setGscQuoteConfigurationBeans(gscQuoteConfigurationBeans);
            try {
                String sourceFeed = quoteToLe.getQuote().getQuoteCode() + "---" + Utils.getSource();
                String ikey = EncryptionUtil.encrypt(sourceFeed);
                ikey = URLEncoder.encode(ikey, "UTF-8");
                domesticVoicePdfBean.setIkey(ikey);
            } catch (Exception e) {
                LOGGER.error("Suppressing the Domestic outbound document ", e);
            }
            InputStream domesticInputStream = new ByteArrayInputStream(generatePdfByteArrayFromBean(domesticVoicePdfBean));
            Integer domesticVoiceAttachmentId = saveGeneratedFile(quoteToLe, domesticInputStream, GscAttachmentTypeConstants.GSC_DOMESTIC_OUTBOUND_PRICES_PDF, GscConstants.GSIP_DOMESTIC_OUTBOUND_PDF);
            gscOutboundPricesDownloadBean.setDomesticVoiceOutboundAttachmentId(domesticVoiceAttachmentId);
            
            //newly added for Rate file generation in O2C
            InputStream domesticExcelInputStream = new ByteArrayInputStream(excelGeneratorService.generateExcelByteArrayFromBean(domesticVoicePdfBean, null));
            Integer domesticVoiceAttachmentId1 = saveGeneratedFile(quoteToLe, domesticExcelInputStream, GscAttachmentTypeConstants.GSC_DOMESTIC_OUTBOUND_PRICES_EXCEL, GscConstants.GSIP_DOMESTIC_OUTBOUND_EXCEL);
        }
    }

    /**
     * Generate pdf byte array from bean
     *
     * @param gscCofOutboundPricesPdfBean
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    private byte[] generatePdfByteArrayFromBean(GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean) throws DocumentException, IOException {
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        Map<String, Object> variable = objectMapper.convertValue(ImmutableMap.of("request", gscCofOutboundPricesPdfBean), Map.class);
        Context context = new Context();
        context.setVariables(variable);
        String html;
        if (!CollectionUtils.isEmpty(gscCofOutboundPricesPdfBean.getGscQuoteConfigurationBeans())) {
            LOGGER.info("domestic outbound html");
            html = templateEngine.process("domesticoutboundpricing_template", context);
        } else {
            LOGGER.info("global outbound html");
            html = templateEngine.process("outboundpricing_template", context);
        }
        PDFGenerator.createPdf(html, outByteStream);
        byte[] toByteArray = outByteStream.toByteArray();
        outByteStream.flush();
        outByteStream.close();
        return toByteArray;
    }

    /**
     * Save generated file
     *
     * @param quoteToLe
     * @param inputStream
     * @throws TclCommonException
     */
    private Integer saveGeneratedFile(QuoteToLe quoteToLe, InputStream inputStream, String fileName, String referenceName) throws TclCommonException {
        LOGGER.info("Saving the generated file by object storage or file storage");
        Integer erfAttachmentId = null;
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            StoredObject storedObject = fileStorageService.uploadFiles(fileName, "GSIP_OUTBOUND_PRICE_FILES", inputStream);
            if (Objects.nonNull(storedObject) && !StringUtils.isEmpty(storedObject.getURL())) {
                String objectStorageUrl = storedObject.getURL().substring(storedObject.getURL().indexOf(swiftApiContainer), storedObject.getURL().lastIndexOf("/"));
                objectStorageUrl = objectStorageUrl.replaceAll("%2F", "/").replaceAll("%20", " ");
                String updatedFileName = storedObject.getName();
                if (Objects.nonNull(objectStorageUrl)) {
                    erfAttachmentId = setAttachmentDetails(quoteToLe, objectStorageUrl, updatedFileName, referenceName);
                }
            }
        } else {
            erfAttachmentId = gscAttachmentHelper.saveFileAttachment(inputStream, fileName);
            if (Objects.nonNull(erfAttachmentId)) {
                createOrUpdateOmsAttachment(quoteToLe, erfAttachmentId, referenceName);
            }
        }
        return erfAttachmentId;
    }

    /**
     * Save object storage uploaded file in customer attachment table
     *
     * @param quoteToLe
     * @param objectStorageUrl
     * @param updatedFileName
     * @return
     * @throws TclCommonException
     */
    private Integer setAttachmentDetails(QuoteToLe quoteToLe, String objectStorageUrl, String updatedFileName, String referenceName) throws TclCommonException {
        AttachmentBean attachmentBean = new AttachmentBean();
        attachmentBean.setPath(objectStorageUrl);
        attachmentBean.setFileName(updatedFileName);
        String attachmentRequest = GscUtils.toJson(attachmentBean);
        LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        Integer erfAttachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
        LOGGER.info("Attachment id is {}", erfAttachmentId);
        createOrUpdateOmsAttachment(quoteToLe, erfAttachmentId, referenceName);
        return erfAttachmentId;
    }

    /**
     * create or update oms attachment based on erf attachment id
     *
     * @param quoteToLe
     * @param erfAttachmentId
     */
    private void createOrUpdateOmsAttachment(QuoteToLe quoteToLe, Integer erfAttachmentId, String referenceName) {
        LOGGER.info("Create or update oms attachment");
        if (Objects.nonNull(erfAttachmentId)) {
            List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByQuoteToLeAndAttachmentTypeAndReferenceName(quoteToLe, GscConstants.OTHERS, referenceName);
            if (!CollectionUtils.isEmpty(omsAttachments)) {
                OmsAttachment omsAttachment = omsAttachments.stream().findFirst().get();
                omsAttachment.setErfCusAttachmentId(erfAttachmentId);
                omsAttachmentRepository.save(omsAttachment);
            } else {
                createOmsAttachment(erfAttachmentId, quoteToLe, referenceName);
            }
            ;
        }
    }

    /**
     * create oms Attachment
     *
     * @param erfAttachmentId
     * @param quoteToLe
     * @param referenceName
     * @return
     */
    private OmsAttachment createOmsAttachment(Integer erfAttachmentId, QuoteToLe quoteToLe, String referenceName) {
        LOGGER.info("Creating oms attachment");
        OmsAttachment omsAttachment = new OmsAttachment();
        omsAttachment.setErfCusAttachmentId(erfAttachmentId);
        omsAttachment.setQuoteToLe(quoteToLe);
        omsAttachment.setAttachmentType("Others");
        omsAttachment.setReferenceName(referenceName);
        omsAttachmentRepository.save(omsAttachment);
        return omsAttachment;
    }

    /**
     * Extract the prices from excel as response and save
     *
     * @param quoteToLe
     * @param file
     * @throws TclCommonException
     */
    public void extractPricesFromExcelAndSave(QuoteToLe quoteToLe, MultipartFile file) throws TclCommonException {
        LOGGER.info("Extracting the prices from excel and saving in the table");
        List<GlobalOutboundDisplayRatesBean> globalOutboundDisplayRatesBeans = extractNegotiablePriceResponseFromExcel(file);
        List<GlobalOutboundDisplayRatesBean> nonNullDisplayRates = globalOutboundDisplayRatesBeans.stream().filter(globalOutboundDisplayRatesBean -> Objects.nonNull(globalOutboundDisplayRatesBean.getDestinationCountry())).collect(Collectors.toList());
        nonNullDisplayRates.stream().forEach(globalOutboundDisplayRatesBean -> {
            if(Double.valueOf(globalOutboundDisplayRatesBean.getPrice()) < 0.00095){
                globalOutboundDisplayRatesBean.setPrice(getPrecisionForLessValues(Double.valueOf(globalOutboundDisplayRatesBean.getPrice()), 4));
            }
            else{
                globalOutboundDisplayRatesBean.setPrice(getPrecision(Double.valueOf(globalOutboundDisplayRatesBean.getPrice()), 4).toString());
            }
        });
        persistNegotiablePrices(nonNullDisplayRates, GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, quoteToLe, (byte) 1);
        persistRatesInGscConfigurations(quoteToLe, nonNullDisplayRates);
    }

    /**
     * Persist rates in gsc configurations
     *
     * @param quoteToLe
     * @param globalOutboundDisplayRatesBeans
     */
    private void persistRatesInGscConfigurations(QuoteToLe quoteToLe, List<GlobalOutboundDisplayRatesBean> globalOutboundDisplayRatesBeans) {
        List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
        LOGGER.info("No of quote gsc for the given quote to le are {} ", quoteGscs.size());
        List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGscIn(quoteGscs);
        LOGGER.info("No of quote gsc details for the given quote gsc are {} ", quoteGscDetails.size());
        quoteGscDetails.stream().forEach(quoteGscDetail -> {
            List<QuoteProductComponent> globalOutboundConfigurationsProductComponents = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(quoteGscDetail.getId(), GscConstants.GLOBAL_OUTBOUND, GscConstants.GSIP_PRODUCT_NAME.toLowerCase());
            if (!CollectionUtils.isEmpty(globalOutboundConfigurationsProductComponents)) {
                LOGGER.info("Global outbound quote gsc details configuration in quote product component is not empty");
                QuoteProductComponent quoteProductComponent = globalOutboundConfigurationsProductComponents.stream().findFirst().get();
                List<QuoteProductComponentsAttributeValue> goPricesAttributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(quoteProductComponent);
                if (!CollectionUtils.isEmpty(goPricesAttributes)) {
                    LOGGER.info("Delete already exisiting prices of go");
                    quoteProductComponentsAttributeValueRepository.deleteAllByIdIn(goPricesAttributes.stream().map(QuoteProductComponentsAttributeValue::getId).collect(Collectors.toList()));
                }

                List<GlobalOutboundDisplayRatesBean> countryPricesInExcel = globalOutboundDisplayRatesBeans.stream().filter(globalOutboundDisplayRatesBean -> Objects.nonNull(globalOutboundDisplayRatesBean.getDestinationCountry())).filter(globalOutboundDisplayRatesBean -> globalOutboundDisplayRatesBean.getDestinationCountry().equalsIgnoreCase(quoteGscDetail.getDest())).collect(Collectors.toList());

                String[] destinationNames = countryPricesInExcel.stream().map(GlobalOutboundDisplayRatesBean::getDestinationName).collect(Collectors.toList()).stream().toArray(String[]::new);
                String[] phoneTypes = countryPricesInExcel.stream().map(GlobalOutboundDisplayRatesBean::getPhoneType).collect(Collectors.toList()).stream().toArray(String[]::new);
                String[] prices = countryPricesInExcel.stream().map(GlobalOutboundDisplayRatesBean::getPrice).collect(Collectors.toList()).stream().toArray(String[]::new);

                ProductAttributeMaster terminationRateProductAttributeMaster = productAttributeMasterRepository.findByName(GscConstants.TERM_RATE);
                ProductAttributeMaster terminationNameProductAttributeMaster = productAttributeMasterRepository.findByName(GscConstants.TERM_NAME);
                ProductAttributeMaster phoneTypeProductAttributeMaster = productAttributeMasterRepository.findByName(GscConstants.PHONE_TYPE);

                LOGGER.info("Destination names and phonetypes and prices sizes are {}, {}, {} ", destinationNames.length, phoneTypes.length, prices.length);
                Arrays.asList(destinationNames).stream().forEach(destinationName -> {
                    QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
                    quoteProductComponentsAttributeValue.setAttributeValues(destinationName);
                    quoteProductComponentsAttributeValue.setDisplayValue(destinationName);
                    quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
                    quoteProductComponentsAttributeValue.setProductAttributeMaster(terminationNameProductAttributeMaster);
                    quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
                });

                Arrays.asList(phoneTypes).stream().forEach(phoneType -> {
                    QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
                    quoteProductComponentsAttributeValue.setAttributeValues(phoneType);
                    quoteProductComponentsAttributeValue.setDisplayValue(phoneType);
                    quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
                    quoteProductComponentsAttributeValue.setProductAttributeMaster(phoneTypeProductAttributeMaster);
                    quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
                });

                Arrays.asList(prices).stream().forEach(price -> {
                    QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
                    quoteProductComponentsAttributeValue.setAttributeValues(price);
                    quoteProductComponentsAttributeValue.setDisplayValue(price);
                    quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
                    quoteProductComponentsAttributeValue.setProductAttributeMaster(terminationRateProductAttributeMaster);
                    quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
                });

            }
        });
    }

    /**
     * Extract prices from excel as response
     *
     * @param file
     * @return
     * @throws TclCommonException
     */
    private List<GlobalOutboundDisplayRatesBean> extractNegotiablePriceResponseFromExcel(MultipartFile file) throws TclCommonException {
        List<GlobalOutboundDisplayRatesBean> globalOutboundDisplayRatesBeans = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8")) {
            for (Sheet sheet : workbook) {
                LOGGER.info("Total no of rows => {}", sheet.getPhysicalNumberOfRows());
                IntStream.range(1, sheet.getPhysicalNumberOfRows()).forEach(index -> globalOutboundDisplayRatesBeans.add(extractExcelData(sheet, index)));
            }
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.EXCEL_VALIDATION_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return globalOutboundDisplayRatesBeans;
    }

    /**
     * Extract excel data
     *
     * @param sheet
     * @param i
     * @return
     */
    private GlobalOutboundDisplayRatesBean extractExcelData(Sheet sheet, int i) {
        DataFormatter dataFormatter = new DataFormatter();
        GlobalOutboundDisplayRatesBean globalOutboundDisplayRatesBean = new GlobalOutboundDisplayRatesBean();
        globalOutboundDisplayRatesBean
                .setDestId(sheet.getRow(i).getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : null);
        globalOutboundDisplayRatesBean
                .setDestinationName(sheet.getRow(i).getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : null);
        globalOutboundDisplayRatesBean
                .setDestinationCountry(sheet.getRow(i).getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : null);
        globalOutboundDisplayRatesBean
                .setPhoneType(sheet.getRow(i).getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : null);
        globalOutboundDisplayRatesBean
                .setStatus(sheet.getRow(i).getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : null);
        globalOutboundDisplayRatesBean
                .setDisplayCurrency(sheet.getRow(i).getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : null);
        globalOutboundDisplayRatesBean
                .setPrice(sheet.getRow(i).getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : null);
        globalOutboundDisplayRatesBean
                .setComments(sheet.getRow(i).getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : NA);
        globalOutboundDisplayRatesBean
                .setRemarks(sheet.getRow(i).getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
                        ? dataFormatter.formatCellValue(sheet.getRow(i).getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL))
                        : NA);
        return globalOutboundDisplayRatesBean;
    }

    /**
     * Perist negotiable prices
     *
     * @param response
     * @param type
     * @param quote
     * @param isNegoitatedPrices
     * @return
     * @throws TclCommonException
     */
    private GlobalOutboundPricingEngineResponse persistNegotiablePrices(List<GlobalOutboundDisplayRatesBean> response, String type, QuoteToLe quoteToLe, Byte isNegoitatedPrices) throws TclCommonException {
        LOGGER.info("Save the response in pricing table");
        //added for multiple LE scenario
        GlobalOutboundPricingEngineResponse pricingDetail;
        if (Objects.isNull(quoteToLe.getQuoteLeCode()))
            pricingDetail = globalOutboundPricingDetailsRepository
                    .findByQuoteCodeAndIsNegotiatedPrices(quoteToLe.getQuote().getQuoteCode(), isNegoitatedPrices);
        else
            pricingDetail = globalOutboundPricingDetailsRepository
                    .findByQuoteLeCodeAndIsNegotiatedPrices(quoteToLe.getQuoteLeCode(), isNegoitatedPrices);
        if (Objects.nonNull(pricingDetail)) {
            pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
            pricingDetail.setResponseData(Utils.convertObjectToJson(response));
        } else {
            LOGGER.info("New pricing response generated for the quote");
            pricingDetail = new GlobalOutboundPricingEngineResponse();
            pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
            pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
            pricingDetail.setPricingType(type);
            pricingDetail.setResponseData(Utils.convertObjectToJson(response));
            pricingDetail.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
            pricingDetail.setQuoteLeCode(quoteToLe.getQuoteLeCode());
            pricingDetail.setIsNegotiatedPrices((byte) 1);
        }
        globalOutboundPricingDetailsRepository.save(pricingDetail);
        return pricingDetail;
    }

    /**
     * Upload negotiated excel and save the response as json in table
     *
     * @param quoteCode
     * @param file
     * @return
     * @throws TclCommonException
     */
    public String uploadNegotiatedOutboundPricesInExcel(String quoteCode, MultipartFile file, Integer quoteLeId) throws TclCommonException {
        Objects.requireNonNull(quoteCode, GscConstants.QUOTE_CODE_NULL_MESSAGE);
        QuoteToLe quoteToLe = null;
        if (Objects.isNull(quoteLeId))
            quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(quoteCode).stream().findAny().get();
        else
            quoteToLe = quoteToLeRepository.findById(quoteLeId).get();
        LOGGER.info("Quote to le id for generating outbound prices is {} ", quoteToLe.getId());
        Integer erfAttachmentId = uploadFilesByObjectOrFileStorage(file, quoteToLe);
        extractPricesFromExcelAndSave(quoteToLe, file);

        List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
        LOGGER.info("No of quote gsc for the given quote to le are {} ", quoteGscs.size());

        if (!CollectionUtils.isEmpty(quoteGscs)) {
            QuoteGsc globalOutBoundQuoteGsc = quoteGscs.stream().filter(quoteGsc -> quoteGsc.getProductName().equalsIgnoreCase("Global Outbound")).findFirst().get();

        }
        return erfAttachmentId.toString();
    }

    /**
     * Upload files by object or file storage
     *
     * @param file
     * @param quoteToLe
     * @return
     * @throws TclCommonException
     */
    private Integer uploadFilesByObjectOrFileStorage(MultipartFile file, QuoteToLe quoteToLe) throws TclCommonException {
        LOGGER.info("Saving the uploaded file by object or file storage");
        Integer erfAttachmentId = null;
        try {
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                StoredObject storedObject = fileStorageService.uploadFiles(GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_EXCEL, "GSIP_OUTBOUND_PRICE_FILES", file.getInputStream());
                if (Objects.nonNull(storedObject) && !StringUtils.isEmpty(storedObject.getURL())) {
                    String objectStorageUrl = storedObject.getURL().substring(storedObject.getURL().indexOf(swiftApiContainer), storedObject.getURL().lastIndexOf("/"));
                    objectStorageUrl = objectStorageUrl.replaceAll("%2F", "/").replaceAll("%20", " ");
                    String updatedFileName = storedObject.getName();
                    if (Objects.nonNull(objectStorageUrl)) {
                        erfAttachmentId = setAttachmentDetails(quoteToLe, objectStorageUrl, updatedFileName, GscConstants.GB_NEGOTIATED_PRICES_EXCEL);
                    }
                }
            } else {
                erfAttachmentId = gscAttachmentHelper.saveFileAttachment(file.getInputStream(), GscConstants.GB_NEGOTIATED_PRICES_EXCEL);
                if (Objects.nonNull(erfAttachmentId)) {
                    createOrUpdateOmsAttachment(quoteToLe, erfAttachmentId, GscConstants.GB_NEGOTIATED_PRICES_EXCEL);
                }
            }
        } catch (IOException e) {
            throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
                    ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return erfAttachmentId;
    }

    /**
     * Check the Excel File Upload Status
     *
     * @param quoteCode
     * @param quoteLeId
     * @return
     * @throws TclCommonException
     */
    public String uploadExcelStatus(String quoteCode, Integer quoteLeId) throws TclCommonException {
        Objects.requireNonNull(quoteCode, GscConstants.QUOTE_CODE_NULL_MESSAGE);
        List<QuoteToLe> quoteToLeList = null;
        if (Objects.isNull(quoteLeId))
            quoteToLeList = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
        else
            quoteToLeList = quoteToLeRepository.findAllById(Collections.singletonList(quoteLeId));
        if (Objects.nonNull(quoteToLeList)) {
            QuoteToLe quoteToLe = quoteToLeList.stream().findAny().get();
            List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findByQuoteToLe(quoteToLe);
            LOGGER.info("OmsAttachment List are:", omsAttachmentList);
            List<OmsAttachment> filteredOmsAttachmentList = omsAttachmentList.stream().filter(p -> p.getErfCusAttachmentId() != null && p.getReferenceName().equalsIgnoreCase("GB NEGOTIATED PRICES EXCEL")).collect(Collectors.toList());
            if ((Objects.nonNull(filteredOmsAttachmentList)) && !filteredOmsAttachmentList.isEmpty()) {
                LOGGER.info("Checking the filteed OMSAttachent list{} ", filteredOmsAttachmentList);
                OmsAttachment omsAttachment = filteredOmsAttachmentList.get(0);

                if (Objects.nonNull(omsAttachment)) {
                    LOGGER.info("Attachment is: ", omsAttachment.getErfCusAttachmentId());
                    return omsAttachment.getErfCusAttachmentId().toString();

                }
            }
        }
        return null;
    }

    /**
     * Get distinct countries queue by product name
     *
     * @param productName
     * @return
     */
    private List<String> distinctCountriesQueue(String productName) {
        List<String> distinctOutboundCountries = new ArrayList<>();
        LOGGER.info("MDC Filter token value in before Queue call get distinct outbound countries {}:", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        try {
            if (Objects.nonNull(productName)) {
                String response = (String) mqUtils.sendAndReceive(distinctCountriesQueue, productName);
                distinctOutboundCountries = GscUtils.fromJson(response,
                        new TypeReference<List<String>>() {
                        });
                LOGGER.info("No of Distinct countries are {} ", distinctOutboundCountries.size());
            }
        } catch (TclCommonException e) {
            LOGGER.warn("Error Occured while getting distinct countries :: {}", e.getStackTrace());
        }
        return distinctOutboundCountries;
    }


    /**
     * Upload files template by object or file storage
     *
     * @param file
     * @return
     * @throws TclCommonException
     */
    public String uploadExcelByObjectOrFileStorageTemplate(MultipartFile file) throws TclCommonException {
        LOGGER.info("Saving the uploaded file by object or file storage");
        Integer erfAttachmentId = null;
        try {
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                StoredObject storedObject = fileStorageService.uploadFiles(GscAttachmentTypeConstants.GLOBAL_OUTBOUND_NEGOTIATED_PRICE_TEMPLATE_EXCEL, "GSIP_OUTBOUND_PRICE_FILES", file.getInputStream());
                if (Objects.nonNull(storedObject) && !StringUtils.isEmpty(storedObject.getURL())) {
                    String objectStorageUrl = storedObject.getURL().substring(storedObject.getURL().indexOf(swiftApiContainer), storedObject.getURL().lastIndexOf("/"));
                    objectStorageUrl = objectStorageUrl.replaceAll("%2F", "/").replaceAll("%20", " ");
                    String updatedFileName = storedObject.getName();
                    if (Objects.nonNull(objectStorageUrl)) {
                        erfAttachmentId = setAttachmentDetailsTemplate(objectStorageUrl, updatedFileName, GscAttachmentTypeConstants.GLOBAL_OUTBOUND_NEGOTIATED_PRICE_TEMPLATE);
                    }
                }
            } else {
                erfAttachmentId = gscAttachmentHelper.saveFileAttachment(file.getInputStream(), GscAttachmentTypeConstants.GLOBAL_OUTBOUND_NEGOTIATED_PRICE_TEMPLATE);
                if (Objects.nonNull(erfAttachmentId)) {
                    createOrUpdateOmsAttachmentTemplate(erfAttachmentId, GscAttachmentTypeConstants.GLOBAL_OUTBOUND_NEGOTIATED_PRICE_TEMPLATE);
                }
            }
        } catch (IOException e) {
            throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
                    ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return erfAttachmentId.toString();
    }


    /**
     * Save object storage uploaded file in customer attachment table
     *
     * @param objectStorageUrl
     * @param updatedFileName
     * @return
     * @throws TclCommonException
     */
    private Integer setAttachmentDetailsTemplate(String objectStorageUrl, String updatedFileName, String referenceName) throws TclCommonException {
        AttachmentBean attachmentBean = new AttachmentBean();
        attachmentBean.setPath(objectStorageUrl);
        attachmentBean.setFileName(updatedFileName);
        String attachmentRequest = GscUtils.toJson(attachmentBean);
        LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        Integer erfAttachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
        LOGGER.info("Attachment id is {}", erfAttachmentId);
        createOrUpdateOmsAttachmentTemplate(erfAttachmentId, referenceName);
        return erfAttachmentId;
    }

    /**
     * create or update oms attachment based on erf attachment id
     *
     * @param quoteToLe
     * @param erfAttachmentId
     */
    private void createOrUpdateOmsAttachmentTemplate(Integer erfAttachmentId, String referenceName) {
        LOGGER.info("Create or update oms attachment");
        if (Objects.nonNull(erfAttachmentId)) {
            List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByAttachmentTypeAndReferenceName(GscConstants.OTHERS, referenceName);
            if (!CollectionUtils.isEmpty(omsAttachments)) {
                OmsAttachment omsAttachment = omsAttachments.stream().findFirst().get();
                omsAttachment.setErfCusAttachmentId(erfAttachmentId);
                omsAttachmentRepository.save(omsAttachment);
            } else {
                createOmsAttachmentTemplate(erfAttachmentId, referenceName);
            }
            ;
        }
    }

    /**
     * create oms Attachment Template
     *
     * @param erfAttachmentId
     * @param referenceName
     * @return
     */
    private OmsAttachment createOmsAttachmentTemplate(Integer erfAttachmentId, String referenceName) {
        LOGGER.info("Creating oms attachment");
        OmsAttachment omsAttachment = new OmsAttachment();
        omsAttachment.setErfCusAttachmentId(erfAttachmentId);
        omsAttachment.setAttachmentType("Others");
        omsAttachment.setReferenceName(referenceName);
        omsAttachmentRepository.save(omsAttachment);
        return omsAttachment;
    }

    /**
     * Method to get attachment id of GB template
     *
     * @return
     */
    public String getAttachmentIdOfNegotiatedPricesTemplate() {
        OmsAttachment omsAttachment = omsAttachmentRepository.findByReferenceName(GscAttachmentTypeConstants.GLOBAL_OUTBOUND_NEGOTIATED_PRICE_TEMPLATE);
        if (Objects.nonNull(omsAttachment)) {
            LOGGER.info("OmsAttachment id present {} ", omsAttachment.getId());
            return omsAttachment.getErfCusAttachmentId().toString();
        }
        return null;
    }

    /**
     * Method to get temp url of saved outbound prices file
     *
     * @param response
     * @param quoteCode
     * @param fileType
     * @param fileName
     * @return
     * @throws TclCommonException
     */
    public HttpServletResponse getSavedOutboundPricesFileFromObjectStorage(HttpServletResponse response, String quoteCode, String fileType, String fileName) throws TclCommonException {
        QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(quoteCode).stream().findFirst().get();
        LOGGER.info("Quote to le id for generating outbound prices is {} ", quoteToLe.getId());
        List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByQuoteToLeAndAttachmentType(quoteToLe, OTHERS);
        LOGGER.info("No of Oms Attachments with  quote to le and type others is {} ", omsAttachments.size());

        Integer erfCusAttachmentId = omsAttachments.stream().filter(omsAttachment -> {
            return omsAttachment.getReferenceName().equalsIgnoreCase(getReferenceName(fileName, fileType));
        }).findFirst().get().getErfCusAttachmentId();


        AttachmentBean attachmentBean = getAttachmentDetails(erfCusAttachmentId);
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            LOGGER.info("file path is {} ", attachmentBean.getPath());
            LOGGER.info("file name is {} ", attachmentBean.getFileName());
            String tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
                    Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
            LOGGER.info("Temporary url is {} ", tempDownloadUrl);
            if(fileName.equalsIgnoreCase("outbound")){
                downloadFileFromObjectUrl(response, GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF, tempDownloadUrl);
            }
            else{
                downloadFileFromObjectUrl(response, GscAttachmentTypeConstants.GSC_SURCHARGE_PRICES_PDF, tempDownloadUrl);
            }


        }
        return response;
    }

    /**
     * Download file from object url
     *
     * @param response
     * @param fileName
     * @param tempDownloadUrl
     * @throws TclCommonException
     */
    private void downloadFileFromObjectUrl(HttpServletResponse response, String fileName, String tempDownloadUrl) throws TclCommonException {
        LOGGER.info("Download file from object url");
        String url = baseUrl + "/" + tempDownloadUrl;
        LOGGER.info("Complete Url is {} ", url);
        try {
            RestResponse restResponse = restClientService.get(url);
            if(restResponse.getStatus() == Status.SUCCESS) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                String data = restResponse.getData();
                for (int i = 0; i < data.length(); ++i)
                    outStream.write(data.charAt(i));

                byte[] fileDataInBytes = outStream.toByteArray();
                setHttpServletResponse(response, fileDataInBytes, fileName, "binary/octet-stream");
                FileCopyUtils.copy(fileDataInBytes, response.getOutputStream());
            }
        } catch (IOException e) {
            throw new TclCommonException(ExceptionConstants.QUEUE_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get file name for saved file
     *
     * @param fileName
     * @return
     */
    private String getFileNameForSavedFile(String fileName) {
        String fileNameInResponse;
        if (fileName.equalsIgnoreCase("outbound")) {
            fileNameInResponse = GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF;
        } else if (fileName.equalsIgnoreCase("surcharge")) {
            fileNameInResponse = GscAttachmentTypeConstants.GSC_SURCHARGE_PRICES_PDF;
        } else {
            fileNameInResponse = GscAttachmentTypeConstants.GSC_DOMESTIC_OUTBOUND_PRICES_PDF;
        }
        return fileNameInResponse;
    }

    /**
     * Get referencename
     *
     * @param fileName
     * @param fileType
     * @return
     */
    private String getReferenceName(final String fileName, final String fileType) {
        if (GSIP_SURCHARGE_PDF.toLowerCase().contains(fileName.toLowerCase())) {
            return GSIP_SURCHARGE_PDF;
        } else if (GSIP_OUTBOUND_PDF.toLowerCase().contains(fileName.toLowerCase())) {
            if (GscAttachmentTypeConstants.PDF.toLowerCase().contains(fileType.toLowerCase())) {
                return GSIP_OUTBOUND_PDF;
            } else {
                return GSIP_OUTBOUND_EXCEL;
            }
        }
        return "";
    }

    /**
     * Method to get attachment Path
     *
     * @param attachmentId
     * @return {@link String}
     * @throws TclCommonException
     */
    private AttachmentBean getAttachmentDetails(Integer attachmentId) throws TclCommonException {
        LOGGER.info("MDC Filter token value in before Queue call fetchAttachmentResource {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        AttachmentBean attachmentBean = new AttachmentBean();
        try {
            String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentDetails,
                    String.valueOf(attachmentId));
            if (attachmentResponse != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);

                if (attachmentMapper != null) {
                    attachmentBean.setFileName((String) attachmentMapper.get("DISPLAY_NAME"));
                    attachmentBean.setPath((String) attachmentMapper.get("URL_PATH"));
                }

            }
        } catch (TclCommonException e) {
            throw new TclCommonException(ExceptionConstants.QUEUE_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return attachmentBean;
    }

    /**
     * To get Outbound prices file
     *
     * @param httpServletResponse
     * @param quoteCode
     * @throws TclCommonException
     * @throws IOException
     * @throws DocumentException
     */
    public void downloadDomesticOutboundPrices(HttpServletResponse httpServletResponse, String quoteCode) throws TclCommonException, IOException, DocumentException {
        QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(quoteCode).stream().findAny().get();
        LOGGER.info("Quote to le id for generating outbound prices is {} ", quoteToLe.getId());
        List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
        LOGGER.info("No of quote gsc for the given quote to le are {} ", quoteGscs.size());
        if (!CollectionUtils.isEmpty(quoteGscs)) {
            QuoteGsc domesticVoiceQuoteGscs = quoteGscs.stream().filter(quoteGsc -> quoteGsc.getProductName().equalsIgnoreCase("Domestic Voice")).findFirst().get();
            List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGsc(domesticVoiceQuoteGscs);
            List<GscQuoteConfigurationBean> gscQuoteConfigurationBeans = new ArrayList<>();
            getDomesticVoiceBeans(quoteGscDetails, gscQuoteConfigurationBeans);
            GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean = new GscCofOutboundPricesPdfBean();
            setCommonDetails(quoteToLe, gscCofOutboundPricesPdfBean);
            gscCofOutboundPricesPdfBean.setGscQuoteConfigurationBeans(gscQuoteConfigurationBeans);
            try {
                String sourceFeed = quoteToLe.getQuote().getQuoteCode() + "---" + Utils.getSource();
                String ikey = EncryptionUtil.encrypt(sourceFeed);
                ikey = URLEncoder.encode(ikey, "UTF-8");
                gscCofOutboundPricesPdfBean.setIkey(ikey);
            } catch (Exception e) {
                LOGGER.error("Suppressing the domestic outbound document ", e);
            }
            downloadOutboundPricesFile(gscCofOutboundPricesPdfBean, httpServletResponse);
        }
    }

    private void getDomesticVoiceBeans(List<QuoteGscDetail> quoteGscDetails, List<GscQuoteConfigurationBean> gscQuoteConfigurationBeans)  throws TclCommonException{
        quoteGscDetails.stream().forEach(quoteGscDetail -> {
                List<QuoteProductComponent> domesticVoiceConfigurations = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(quoteGscDetail.getId(), GscConstants.DOMESTIC_VOICE, GscConstants.GSIP_PRODUCT_NAME.toLowerCase());
                if (!CollectionUtils.isEmpty(domesticVoiceConfigurations)) {
                    GscQuoteConfigurationBean gscQuoteConfigurationBean = new GscQuoteConfigurationBean();
                    gscQuoteConfigurationBean.setSource(quoteGscDetail.getSrc());
                    LOGGER.info("Domestic Voice quote gsc details configuration in quote product component is not empty");
                    QuoteProductComponent quoteProductComponent = domesticVoiceConfigurations.stream().findFirst().get();
                    List<QuoteProductComponentsAttributeValue> domesticVoicePrices = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(quoteProductComponent);

                    List<String> terminationRates = domesticVoicePrices.stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.TERM_RATE))
                            .map(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getAttributeValues()).collect(Collectors.toList());
                    List<String> terminationNames = domesticVoicePrices.stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.TERM_NAME))
                            .map(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getAttributeValues()).collect(Collectors.toList());
                    List<String> phoneTypes = domesticVoicePrices.stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.PHONE_TYPE))
                            .map(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getAttributeValues()).collect(Collectors.toList());

                    List<GscTerminationBean> gscTerminationBeans = terminationNames.stream().map(s -> {
                        GscTerminationBean gscTerminationBean = new GscTerminationBean();
                        gscTerminationBean.setTerminationName(s);
                        return gscTerminationBean;
                    }).collect(Collectors.toList());

                    GscTerminationBean[] gscTerminationBeansArray = new GscTerminationBean[gscTerminationBeans.size()];
                    gscTerminationBeans.toArray(gscTerminationBeansArray);

                    String[] terminationRatesArray = new String[terminationRates.size()];
                    terminationRates.toArray(terminationRatesArray);

                    String[] phoneTypesArray = new String[phoneTypes.size()];
                    phoneTypes.toArray(phoneTypesArray);



                    LOGGER.info("Setting Termination Rate");
                    IntStream.range(0, terminationRatesArray.length).forEach(index -> gscTerminationBeansArray[index].setTerminationRate(terminationRatesArray[index]));
                    LOGGER.info("Setting Phone Type");
                    IntStream.range(0, phoneTypesArray.length).forEach(index -> gscTerminationBeansArray[index].setPhoneType(phoneTypesArray[index]));

                    try {
                        List<String> allTerminationNames = gscTerminationBeans.stream().map(GscTerminationBean::getTerminationName).collect(Collectors.toList());
                        List<GscOutboundPriceBean>  gscOutboundPriceBeans = gscPricingFeasibilityService.processOutboundPriceData(allTerminationNames);
                        if(!CollectionUtils.isEmpty(gscOutboundPriceBeans)){
                            LOGGER.info("Size of gscOutboundPriceBeans {}", gscOutboundPriceBeans.size());
                            gscTerminationBeans.stream().forEach(gscTerminationBean -> {
                                GscOutboundPriceBean matchingOutboundPrice = new GscOutboundPriceBean();
                                List<GscOutboundPriceBean> matchingOutboundPrices = gscOutboundPriceBeans.stream().filter(gscOutboundPriceBean -> gscOutboundPriceBean.getDestinationName().equalsIgnoreCase(gscTerminationBean.getTerminationName()))
                                        .collect(Collectors.toList());
                                if(!CollectionUtils.isEmpty(matchingOutboundPrices)){
                                    gscTerminationBean.setTerminationId(matchingOutboundPrices.stream().findFirst().get().getDestId());
                                }
                                else{
                                    gscTerminationBean.setTerminationId(0);
                                }

                            });
                        }
                    } catch (TclCommonException e) {
                        LOGGER.warn("Error Occured while getting CustomerLe CUID :: {}", e.getStackTrace());
                    }


                    gscQuoteConfigurationBean.setTerminations(gscTerminationBeans);
                    gscQuoteConfigurationBeans.add(gscQuoteConfigurationBean);
                }
        });
    }

    /**
     * Get saved domestic outbound prices from object
     *
     * @param response
     * @param quoteCode
     * @return
     * @throws TclCommonException
     */
    public HttpServletResponse getSavedDomesticOutboundPricesFileFromObjectStorage(HttpServletResponse response, String quoteCode) throws TclCommonException {
        QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(quoteCode).stream().findFirst().get();
        LOGGER.info("Quote to le id for generating outbound prices is {} ", quoteToLe.getId());
        List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByQuoteToLeAndAttachmentType(quoteToLe, OTHERS);
        LOGGER.info("No of Oms Attachments with  quote to le and type others is {} ", omsAttachments.size());

        Integer erfCusAttachmentId = omsAttachments.stream().filter(omsAttachment -> omsAttachment.getReferenceName().equalsIgnoreCase(GscConstants.GSIP_DOMESTIC_OUTBOUND_PDF)).findFirst().get().getErfCusAttachmentId();

        LOGGER.info("ErfcusAttachment Id is {} ", erfCusAttachmentId);
        AttachmentBean attachmentBean = getAttachmentDetails(erfCusAttachmentId);
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            LOGGER.info("file path is {} ", attachmentBean.getPath());
            LOGGER.info("file name is {} ", attachmentBean.getFileName());
            String tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
                    Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
            LOGGER.info("Temporary url is {} ", tempDownloadUrl);
            downloadFileFromObjectUrl(response, GscAttachmentTypeConstants.GSC_DOMESTIC_OUTBOUND_PRICES_PDF, tempDownloadUrl);
        }
        return response;
    }

    /**
     * Method to set precision
     *
     * @param value
     * @param precision
     * @return
     */
    private Double getPrecision(Double value, Integer precision) {
        Double result = 0.0;
        if (Objects.nonNull(value)) {
            if (precision == 2) {
                result = Math.round(value * 100.0) / 100.0;
                DecimalFormat df1 = new DecimalFormat(".##");
                result = Double.parseDouble(df1.format(result));
            } else if (precision == 4) {
                result = Math.round(value * 10000.0) / 10000.0;
                DecimalFormat df2 = new DecimalFormat(".####");
                result = Double.parseDouble(df2.format(result));
            }
        }
        return result;
    }

    /**
     * Values less tan 0.00094 are converting to exponential for when doing Double.parse of (Math.round), thus using this method
     *
     * @param value
     * @param precision
     * @return
     */
    private String getPrecisionForLessValues(Double value, Integer precision) {
        Double result = 0.0;
        if (Objects.nonNull(value)) {
            if (precision == 2) {
                result = Math.round(value * 100.0) / 100.0;
                DecimalFormat df1 = new DecimalFormat(".##");
                return "0".concat(df1.format(result));
            } else if (precision == 4) {
                result = Math.round(value * 10000.0) / 10000.0;
                DecimalFormat df2 = new DecimalFormat(".####");
                return "0".concat(df2.format(result));
            }
        }
        else{
            return result.toString();
        }
        return null;
    }

	/**
	 * Set Gsc Outbound Rate card variables for quote/COF PDF
	 *
	 * @param cofPdfRequest
	 * @param quoteCode
	 * @param isAllActive
	 * @param quoteGscId
	 * @throws TclCommonException
	 */
	public void setGscOutboundRateCardVariables(TeamsDRCofPdfBean cofPdfRequest, String quoteCode, Integer quoteLeId, Byte isAllActive,
			Integer quoteGscId) throws TclCommonException {
		Objects.requireNonNull(quoteCode, GscConstants.QUOTE_CODE_NULL_MESSAGE);
		if (Objects.isNull(isAllActive)) {
			LOGGER.info("Is All Active is set to 0");
			isAllActive = (byte) 0;
		}
        List<QuoteToLe> quoteToLe;
        if (Objects.isNull(quoteLeId))
            quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
        else {
            quoteToLe = quoteToLeRepository.findAllById(Collections.singletonList(quoteLeId));
        }
		LOGGER.info("Quote to le id for generating outbound prices is {} ",
				quoteToLe.size() == 1 ? quoteToLe.get(0).getId() : "multiple le ids");
		List<QuoteGsc> quoteGscs = new ArrayList<>();
		if (Objects.isNull(quoteGscId)) {
			quoteGscs = quoteGscRepository.findByQuoteToLeIn(quoteToLe);
		} else
			quoteGscs = quoteGscRepository.findAllById(Collections.singletonList(quoteGscId));
		LOGGER.info("No of quote gsc for the given quote to le are {} ", quoteGscs.size());
		if (!CollectionUtils.isEmpty(quoteGscs)) {
			QuoteGsc globalOutBoundQuoteGsc = quoteGscs.stream()
					.filter(quoteGsc -> quoteGsc.getProductName().equalsIgnoreCase("Global Outbound")).findFirst()
					.get();
			GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean = getPdfBean(quoteCode,
					globalOutBoundQuoteGsc.getQuoteToLe(), globalOutBoundQuoteGsc, isAllActive, quoteGscId);
			gscCofOutboundPricesPdfBean.setApproved(false);
			cofPdfRequest.setGscCofOutboundPricesPdfBean(gscCofOutboundPricesPdfBean);
		}
	}
}

