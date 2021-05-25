package com.tcl.dias.oms.gsc.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.GscAttachmentTypeConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscOutboundSurchargePricingBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscOutboundPricesDownloadBean;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.pdf.beans.GscCofOutboundPricesPdf;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundWithMessage;

/**
 * Service related to product catalog views
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscProductCatalogService {

    private static final String OUTBOUND_MQ_ERROR = "Error in getting outbound prices";
    private static final Logger LOGGER = LoggerFactory.getLogger(GscProductCatalogService.class);
    @Autowired
    MQUtils mqUtils;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SpringTemplateEngine templateEngine;
    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    QuoteToLeRepository quoteToLeRepository;
    @Autowired
    GscAttachmentHelper gscAttachmentHelper;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Autowired
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Value(("${swift.api.container}"))
    String swiftApiContainer;

    @Value("${attachment.queue}")
    String attachmentQueue;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;
    @Value("${rabbitmq.product.outbound.surcharge.pricing}")
    private String outboundSurchargeQueue;
    @Value("${rabbitmq.product.outbound.pricing}")
    private String outboundQueue;

    /**
     * Download dynamic outbound surcharge prices
     *
     * @param response2
     * @return {@link List<GscOutboundSurchargePricingBean>}
     * @throws TclCommonException
     */
    @Transactional
    public Try<HttpServletResponse> downloadOutboundSurchargePrices(HttpServletResponse response)
            throws TclCommonException {
        return getOutboundSurchargePrices()
                .flatMapTry(prices -> downloadOutboundSurchargePdf(prices, response));
    }

    /**
     * Get outboung surcharge prices
     *
     * @return {@link List<GscOutboundSurchargePricingBean>}
     * @throws TclCommonException
     * @throws IllegalArgumentException
     */
    private Try<List<GscOutboundSurchargePricingBean>> getOutboundSurchargePrices()
            throws TclCommonException, IllegalArgumentException {
        List<GscOutboundSurchargePricingBean> gscOutboundSurchargePricingBeans;
        LOGGER.info("MDC Filter token value in before Queue call getOutboundSurchargePrices {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        final String response = (String) mqUtils.sendAndReceive(outboundSurchargeQueue, null);
        LOGGER.info("MDC Filter token value in before Queue call getOutboundSurchargePrices {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        if (StringUtils.isNotBlank(response)) {
            gscOutboundSurchargePricingBeans = GscUtils.fromJson(response,
                    new TypeReference<List<GscOutboundSurchargePricingBean>>() {
                    });
        } else {
            throw new TclCommonException(OUTBOUND_MQ_ERROR);
        }
        return Try.success(gscOutboundSurchargePricingBeans);
    }

    /**
     * download outbound surcharge as PDF
     *
     * @param gscOutboundSurchargePricingBeans
     * @param response
     * @return
     * @throws DocumentException
     * @throws IOException
     * @throws TclCommonException
     */
    private Try<HttpServletResponse> downloadOutboundSurchargePdf(
            List<GscOutboundSurchargePricingBean> gscOutboundSurchargePricingBeans, HttpServletResponse response)
            throws DocumentException, IOException, TclCommonException {
        byte[] outArray = null;
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        Map<String, Object> variable = objectMapper.convertValue(
                ImmutableMap.of("gscOutboundSurchargePricingBean", gscOutboundSurchargePricingBeans), Map.class);
        Context context = new Context();
        context.setVariables(variable);
        String html = templateEngine.process("outboundSurchargePrices_template", context);
        PDFGenerator.createPdf(html, outByteStream);
        outArray = outByteStream.toByteArray();
        response.reset();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + GscAttachmentTypeConstants.GSC_SURCHARGE_PRICES_PDF + "\"");
        try {
            FileCopyUtils.copy(outArray, response.getOutputStream());
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        outByteStream.flush();
        outByteStream.close();
        return Try.success(response);
    }

    /**
     * Method to generate and save outbound prices via file storage
     *
     * @param quoteCode
     * @param response
     * @return {@link GscOutboundPricesDownloadBean}
     */
    @Transactional
    public GscOutboundPricesDownloadBean generateAndSaveOutboundPrices(String quoteCode, HttpServletResponse response) {
        return Try.success(createOutboundContext(quoteCode, response))
                .map(this::getQuoteForOutbound)
                .flatMapTry(this::saveOutboundPrices)
                .mapTry(this::createOutboundPDFFile)
                .mapTry(this::saveGeneratedFile)
                .map(context -> context.downloadBean).get();
    }

    /**
     * Method to download outbound price lievle
     *
     * @param attachmentID
     * @return {@link Resource}
     * @throws TclCommonException
     */
    @Transactional
    public Resource downloadOutboundPricesFile(Integer attachmentID) throws TclCommonException {
        return gscAttachmentHelper.fetchAttachmentResource(attachmentID)
                .orElseThrow(() -> notFoundWithMessage(ExceptionConstants.RESOURCE_NOT_EXIST,
                        String.format("Template not found for attachment id: %s", attachmentID)));
    }

    /**
     * Method to update the oms details for file uploaded via object storage
     *
     * @param quoteToLeId
     * @param requestId
     * @param url
     * @return {@link GscOutboundPricesDownloadBean}
     */
    @Transactional
    public GscOutboundPricesDownloadBean updateGeneratedOutboundPrices(Integer quoteToLeId, String requestId,
                                                                       String url) {
        GscOutboundPricesDownloadBean bean = new GscOutboundPricesDownloadBean();
        Optional.ofNullable(quoteToLeRepository.findById(quoteToLeId)).map(quoteToLe -> {
            OmsAttachment omsAttachment = new OmsAttachment();
            Integer attachmentId = gscAttachmentHelper.saveObjectAttachment(requestId, url);
            omsAttachment.setErfCusAttachmentId(attachmentId);
            omsAttachment.setAttachmentType("Others");
            omsAttachmentRepository.save(omsAttachment);
            bean.setAttachmentId(attachmentId);
            return omsAttachment;
        }).orElseGet(() -> {
            throw new TCLException(String.format("Quote with quoteleId  %d not found", quoteToLeId));
        });
        return bean;
    }

    /**
     * Create outbound context
     *
     * @param quoteCode
     * @param response
     * @return {@link OutboundPriceContext}
     */
    private PdfOutboundContext createOutboundContext(String quoteCode, HttpServletResponse response) {
        PdfOutboundContext context = new PdfOutboundContext();
        context.quoteCode = quoteCode;
        context.response = response;
        context.downloadBean = new GscOutboundPricesDownloadBean();
        context.outBoundPdfRequest = new GscCofOutboundPricesPdf();
        context.fileName = GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF;
        return context;
    }

    /**
     * create context for surcharge outbound price list
     *
     * @param quoteCode
     * @param response
     * @return {@link SurchargeOutboundPriceContext}
     */
    private PdfOutboundContext createSurchargeOutboundContext(String quoteCode, HttpServletResponse response) {
        PdfOutboundContext context = new PdfOutboundContext();
        context.quoteCode = quoteCode;
        context.response = response;
        context.downloadBean = new GscOutboundPricesDownloadBean();
        context.referenceName = GscConstants.GSIP_SURCHARGE_PDF;
        context.fileName = GscAttachmentTypeConstants.GSC_SURCHARGE_PRICES_PDF;
        return context;
    }

    /**
     * Create outbound context
     *
     * @param quoteCode
     * @param response
     * @return {@link OutboundPriceContext}
     */
    private PdfOutboundContext createOutboundContextExcel(String quoteCode, HttpServletResponse response) {
        PdfOutboundContext context = new PdfOutboundContext();
        context.quoteCode = quoteCode;
        context.response = response;
        context.downloadBean = new GscOutboundPricesDownloadBean();
        context.outBoundPdfRequest = new GscCofOutboundPricesPdf();
        context.fileName = GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_EXCEL;
        return context;
    }

    /**
     * Get quote by quoteCode
     *
     * @param context
     * @return {@link OutboundPriceContext}
     */
    private PdfOutboundContext getQuoteForOutbound(PdfOutboundContext context) {
        context.quoteToLe = getQuoteToLe(context.quoteCode);
        context.quote = quoteRepository.findByQuoteCode(context.quoteCode);
        context.dynamicColoumnName = quoteProductComponentsAttributeValueRepository
                .findByAttributeName(context.quoteCode);
        return context;
    }

    /**
     * Save outbound prices in context
     *
     * @param context
     * @return {@link OutboundPriceContext}
     * @throws TclCommonException
     */
    private Try<PdfOutboundContext> saveOutboundPrices(PdfOutboundContext context) throws TclCommonException {
        List<GscOutboundPriceBean> gscOutboundPriceBeans = getOutboundPrices(context.dynamicColoumnName);
        context.outBoundPdfRequest.setGscOutboundPriceBean(
                createListWithDynamicColoumn(context.dynamicColoumnName, gscOutboundPriceBeans));
        context.outBoundPdfRequest.setCofRefernceNumber(context.quoteCode);
        context.outBoundPdfRequest.setOrderDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        context.outBoundPdfRequest.setOrderType(context.quoteToLe.getQuoteType());
        if (!GscConstants.HIGH_RATE.equalsIgnoreCase(context.dynamicColoumnName)) {
            context.outBoundPdfRequest.setDynamicColoumnName(context.dynamicColoumnName);
        }
        return Try.success(context);
    }

    /**
     * Get outbound prices by dynamic coloumn
     *
     * @param dynamicColoumnName
     * @return {@link List<GscOutboundPriceBean>}
     * @throws TclCommonException
     */
    private List<GscOutboundPriceBean> getOutboundPrices(String dynamicColoumnName) throws TclCommonException {
        List<GscOutboundPriceBean> gscOutboundPriceBeans;
        LOGGER.info("MDC Filter token value in before Queue call getOutboundPrices {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        final String response = (String) mqUtils.sendAndReceive(outboundQueue, dynamicColoumnName);
        LOGGER.info("MDC Filter token value in before Queue call getOutboundPrices {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        if (StringUtils.isNotBlank(response)) {
            gscOutboundPriceBeans = GscUtils.fromJson(response, new TypeReference<List<GscOutboundPriceBean>>() {
            });
        } else {
            throw new TCLException(OUTBOUND_MQ_ERROR);
        }
        return gscOutboundPriceBeans;
    }

    /**
     * create oubound prices list by dynamic coloumn
     *
     * @param dynamicColoumn
     * @param gscOutboundPriceBeans
     * @return {@link List<GscOutboundPriceBean>}
     */
    private List<GscOutboundPriceBean> createListWithDynamicColoumn(String dynamicColoumn,
                                                                    List<GscOutboundPriceBean> gscOutboundPriceBeans) {
        return gscOutboundPriceBeans.stream().map(inputbean -> createNewOutboundList(inputbean, dynamicColoumn))
                .collect(Collectors.toList());
    }

    /**
     * create outbound list
     *
     * @param inputbean
     * @param dynamicColoumn
     * @return {@link GscOutboundPriceBean}
     */
    private GscOutboundPriceBean createNewOutboundList(GscOutboundPriceBean inputbean, String dynamicColoumn) {
        GscOutboundPriceBean outputBean = new GscOutboundPriceBean();
        outputBean.setCountry(inputbean.getCountry());
        outputBean.setPhoneType(inputbean.getPhoneType());
        outputBean.setDestId(inputbean.getDestId());
        outputBean.setDestinationName(inputbean.getDestinationName());
        outputBean.setHighRate(inputbean.getHighRate());
        outputBean.setComments(inputbean.getComments());
        if (Objects.nonNull(dynamicColoumn) && !dynamicColoumn.equalsIgnoreCase(GscConstants.HIGH_RATE)) {
            setDynamicColoumn(inputbean, outputBean, dynamicColoumn);
        }
        return outputBean;
    }

    /**
     * Set dynamic coloumn
     *
     * @param inputbean
     * @param outputBean
     * @param dynamicColoumn
     */
    private void setDynamicColoumn(GscOutboundPriceBean inputbean, GscOutboundPriceBean outputBean,
                                   String dynamicColoumn) {
        switch (dynamicColoumn) {
            case "ctry_cd_iso_3":
                outputBean.setCountryCode(inputbean.getCountryCode());
                break;
            case "service_level":
                outputBean.setServiceLevel(inputbean.getServiceLevel());
                break;
            case "region":
                outputBean.setRegion(inputbean.getRegion());
                break;
            case "currency_cd":
                outputBean.setCurrencyCode(inputbean.getCurrencyCode());
                break;
            case "cda_floor":
                outputBean.setCdaFloor(inputbean.getCdaFloor());
                break;
            case "sp_region_discount":
                outputBean.setSpRegionDiscount(inputbean.getSpRegionDiscount());
                break;
            case "sp_discount_3":
                outputBean.setSpDiscount3(inputbean.getSpDiscount3());
                break;
            case "sp_discount_2":
                outputBean.setSpDiscount2(inputbean.getSpDiscount2());
                break;
            case "sp_discount_1":
                outputBean.setSpDiscount1(inputbean.getSpDiscount1());
                break;
            case "service_provider_floor":
                outputBean.setServiceProviderFloor(inputbean.getServiceProviderFloor());
                break;
            case "ep_region_discount":
                outputBean.setEpRegionDiscount(inputbean.getEpRegionDiscount());
                break;
            case "enterprise_discount_3":
                outputBean.setEnterpriseDiscount3(inputbean.getEnterpriseDiscount3());
                break;
            case "enterprise_discount_2":
                outputBean.setEnterpriseDiscount2(inputbean.getEnterpriseDiscount2());
                break;
            case "enterprise_discount_1":
                outputBean.setEnterpriseDiscount1(inputbean.getEnterpriseDiscount1());
                break;
            case "enterprise_sales_floor":
                outputBean.setEnterpriseSalesFloor(inputbean.getEnterpriseSalesFloor());
                break;
            case "highest_possible_obc_surcharge":
                outputBean.setHighestPossibleObcSurcharge(inputbean.getHighestPossibleObcSurcharge());
                break;
            case "internal_comments":
                outputBean.setInternalComments(inputbean.getInternalComments());
                break;
            case "is_active_ind":
                outputBean.setIsActiveInd(inputbean.getIsActiveInd());
                break;
            case "reason_txt":
                outputBean.setReasonText(inputbean.getReasonText());
                break;
            default:
                break;
        }
    }

    /**
     * create outbound file as pdf
     *
     * @param outboundContext
     * @return {@link PdfOutboundContext}
     * @throws DocumentException
     * @throws IOException
     * @throws TclCommonException
     */
    private PdfOutboundContext createOutboundPDFFile(PdfOutboundContext outboundContext)
            throws DocumentException, IOException, TclCommonException {
        outboundContext.referenceName = GscConstants.GSIP_OUTBOUND_PDF;
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        Map<String, Object> variable = objectMapper
                .convertValue(ImmutableMap.of("request", outboundContext.outBoundPdfRequest), Map.class);
        Context context = new Context();
        context.setVariables(variable);
        String html = templateEngine.process("outboundpricing_template", context);
        PDFGenerator.createPdf(html, outByteStream);
        InputStream inputStream = new ByteArrayInputStream(outByteStream.toByteArray());
        outboundContext.targetStream = inputStream;
        inputStream.close();
        outByteStream.flush();
        outByteStream.close();
        return outboundContext;
    }

    /**
     * Save Generated file (Global)
     *
     * @param context
     * @return {@link PdfOutboundContext}
     */
    private PdfOutboundContext saveGeneratedFile(PdfOutboundContext context) throws IOException, TclCommonException {

        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            StoredObject storedObject = fileStorageService.uploadFiles(context.fileName, "GSIP_OUTBOUND_PRICE_FILES", context.targetStream);
            if (storedObject != null && storedObject.getURL() != null && !storedObject.getURL().isEmpty()) {
                String objectStorageUrl = storedObject.getURL().substring(storedObject.getURL().indexOf(swiftApiContainer), storedObject.getURL().lastIndexOf("/"));
                objectStorageUrl = objectStorageUrl.replaceAll("%2F", "/").replaceAll("%20", " ");
                String updatedFileName = storedObject.getName();
                if (Objects.nonNull(objectStorageUrl)) {
                    setAttachmentForObjectStorage(context, objectStorageUrl, updatedFileName);
                }
            }
        } else {
            Integer attachmentId = gscAttachmentHelper.saveFileAttachment(context.targetStream, context.fileName);
            if (Objects.nonNull(attachmentId)) {
                List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByQuoteToLeAndAttachmentTypeAndReferenceName(context.quoteToLe, GscConstants.OTHERS, context.referenceName);
                omsAttachments.stream().findFirst().map(omsAttachment -> {
                    omsAttachment.setErfCusAttachmentId(attachmentId);
                    omsAttachmentRepository.save(omsAttachment);
                    return omsAttachment;
                }).orElse(createOmsAttachment(attachmentId, context.quoteToLe, context.referenceName));

                context.downloadBean.setAttachmentId(attachmentId);
            }
        }
        return context;
    }

    private void setAttachmentForObjectStorage(PdfOutboundContext context, String objectStorageUrl, String updatedFileName) throws TclCommonException {
        AttachmentBean attachmentBean = new AttachmentBean();
        attachmentBean.setPath(objectStorageUrl);
        attachmentBean.setFileName(updatedFileName);
        String attachmentRequest = GscUtils.toJson(attachmentBean);
        LOGGER.info("MDC Filter token value in before Queue call saveAttachment {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        Integer attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentRequest);
        if (attachmentId != null && attachmentId != -1) {
            List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByQuoteToLeAndAttachmentTypeAndReferenceName(context.quoteToLe, GscConstants.OTHERS, context.referenceName);
            omsAttachments.stream().findFirst().map(omsAttachment -> {
                omsAttachment.setErfCusAttachmentId(attachmentId);
                omsAttachmentRepository.save(omsAttachment);
                return omsAttachment;
            }).orElse(createOmsAttachment(attachmentId, context.quoteToLe, context.referenceName));

            context.downloadBean.setAttachmentId(attachmentId);
        }
    }

    /**
     * Create a oms attachment
     *
     * @param attachmentId
     * @param quoteToLe
     * @return {@link OmsAttachment}
     */
    private OmsAttachment createOmsAttachment(Integer attachmentId, QuoteToLe quoteToLe, String referenceName) {
        OmsAttachment omsAttachment = new OmsAttachment();
        omsAttachment.setErfCusAttachmentId(attachmentId);
        omsAttachment.setQuoteToLe(quoteToLe);
        omsAttachment.setAttachmentType("Others");
        omsAttachment.setReferenceName(referenceName);
        omsAttachmentRepository.save(omsAttachment);
        return omsAttachment;
    }

    /**
     * Method to generate and save surcharge prices via file storage
     *
     * @param quoteCode
     * @param response
     * @return {@link GscOutboundPricesDownloadBean}
     */
    @Transactional
    public GscOutboundPricesDownloadBean generateAndSaveSurchargeOutboundPrices(String quoteCode, HttpServletResponse response) {
        return Try.success(createSurchargeOutboundContext(quoteCode, response))
                .map(this::getQuoteForSurcharge)
                .mapTry(this::saveOutboundSurchargePrices)
                .mapTry(this::createSurchargePDFFile)
                .mapTry(this::saveGeneratedFile)
                .map(context -> context.downloadBean).get();
    }

    /**
     * Get Quote Details
     *
     * @param context
     * @return {@link SurchargeOutboundPriceContext}
     */
    private PdfOutboundContext getQuoteForSurcharge(PdfOutboundContext context) {
        context.quoteToLe = getQuoteToLe(context.quoteCode);
        return context;
    }

    /**
     * Get QuoteToLe Details by QuoteCode
     *
     * @param quoteCode
     * @return {@link QuoteToLe}
     */
    private QuoteToLe getQuoteToLe(String quoteCode) {
        return Optional.ofNullable(quoteToLeRepository.findByQuote_QuoteCode(quoteCode)).map(quoteToLes -> {
            QuoteToLe quoteToLe = quoteToLes.stream().findFirst().get();
            return quoteToLe;
        }).orElseGet(() -> {
            throw new TCLException(String.format("Quote with code  %s not found", quoteCode));
        });
    }

    /**
     * Save outbound Surcharge Prices in context
     *
     * @param context
     * @return {@link SurchargeOutboundPriceContext}
     * @throws TclCommonException
     * @throws IllegalArgumentException
     */
    private PdfOutboundContext saveOutboundSurchargePrices(PdfOutboundContext context)
            throws TclCommonException, IllegalArgumentException {
        context.surchargePricingBean = getOutboundSurchargePrices().get();
        return context;
    }

    /**
     * Create Surcharge PDF
     *
     * @param surchargeContext
     * @return
     * @throws DocumentException
     * @throws IOException
     * @throws TclCommonException
     */
    private PdfOutboundContext createSurchargePDFFile(PdfOutboundContext surchargeContext)
            throws DocumentException, IOException, TclCommonException {
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        Map<String, Object> variable = objectMapper
                .convertValue(ImmutableMap.of("gscOutboundSurchargePricingBean", surchargeContext.surchargePricingBean), Map.class);
        Context context = new Context();
        context.setVariables(variable);
        String html = templateEngine.process("outboundSurchargePrices_template", context);
        PDFGenerator.createPdf(html, outByteStream);
        InputStream inputStream = new ByteArrayInputStream(outByteStream.toByteArray());
        surchargeContext.targetStream = inputStream;
        outByteStream.flush();
        outByteStream.close();
        return surchargeContext;
    }

    /**
     * download Outbound Prices as PDF
     *
     * @param response
     * @param quoteCode
     * @return {@link Try<HttpServletResponse>}
     * @throws TclCommonException
     */
    @Transactional
    public Try<HttpServletResponse> downloadOutboundPrices(HttpServletResponse response, String quoteCode)
            throws TclCommonException {
        String columnName = quoteProductComponentsAttributeValueRepository.findByAttributeName(quoteCode);
        return getOutboundPricesFile(columnName, quoteCode)
                .flatMapTry(prices -> downloadOutboundPricesPDF(prices, response));
    }

    /**
     * get outbound prices File
     *
     * @return {@link Try<List<GscOutboundPriceBean>>}
     * @throws TclCommonException
     */
    private Try<GscCofOutboundPricesPdf> getOutboundPricesFile(String value, String quoteCode) throws TclCommonException {
        List<GscOutboundPriceBean> gscOutboundPriceBeans = getOutboundPrices(value);
        GscCofOutboundPricesPdf outBoundPdfRequest = new GscCofOutboundPricesPdf();
        outBoundPdfRequest.setGscOutboundPriceBean(createListWithDynamicColoumn(value, gscOutboundPriceBeans));
        Quote quote = quoteRepository.findByQuoteCode(quoteCode);
        outBoundPdfRequest.setCofRefernceNumber(quoteCode);
        outBoundPdfRequest.setOrderDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        outBoundPdfRequest.setOrderType(quote.getQuoteToLes().stream().findFirst().get().getQuoteType());
        if (!value.equalsIgnoreCase(GscConstants.HIGH_RATE)) {
            outBoundPdfRequest.setDynamicColoumnName(value);
        }
        return Try.success(outBoundPdfRequest);
    }

    /**
     * Download the gsoutboundPriceBean data in PDF
     *
     * @param prices
     * @param response
     * @return {@link Try<HttpServletResponse>}
     * @throws DocumentException
     * @throws IOException
     * @throws TclCommonException
     */
    private Try<HttpServletResponse> downloadOutboundPricesPDF(GscCofOutboundPricesPdf request, HttpServletResponse response)
            throws DocumentException, IOException, TclCommonException {
        byte[] outArray = null;
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        Map<String, Object> variable = objectMapper.convertValue(ImmutableMap.of("request", request), Map.class);
        Context context = new Context();
        context.setVariables(variable);
        String html = templateEngine.process("outboundpricing_template", context);
        PDFGenerator.createPdf(html, outByteStream);
        outArray = outByteStream.toByteArray();
        response.reset();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_PDF + "\"");
        try {
            FileCopyUtils.copy(outArray, response.getOutputStream());
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        outByteStream.flush();
        outByteStream.close();
        return Try.success(response);
    }

    /**
     * Download outbound Prices in Excel
     *
     * @param response
     * @param quoteCode
     * @return
     * @throws IOException
     * @throws DocumentException
     * @throws TclCommonException
     */
    public void downloadOutboundPricesInExcel(HttpServletResponse response, String quoteCode)
            throws IOException, DocumentException, TclCommonException {
        Objects.requireNonNull("FileType cannot be null");
        String coloumnName = quoteProductComponentsAttributeValueRepository.findByAttributeName(quoteCode);
        List<GscOutboundPriceBean> gscOutboundPricingBeans = getOutboundPrices(coloumnName);
        generateExcel(response, gscOutboundPricingBeans, coloumnName);
    }

    /**
     * Generate data in Excel
     *
     * @param response
     * @param gscOutboundPricingBeans
     * @param coloumnName
     * @return
     * @throws IOException
     * @throws TclCommonException
     */
    private void generateExcel(HttpServletResponse response,
                               List<GscOutboundPriceBean> gscOutboundPricingBeans, String coloumnName)
            throws IOException, TclCommonException {

        byte[] outArray = null;
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        Workbook workbook = new HSSFWorkbook();
        createOutboundWorkbook(workbook, gscOutboundPricingBeans, coloumnName);
        workbook.write(outByteStream);
        outArray = outByteStream.toByteArray();
        String fileName = GscAttachmentTypeConstants.GSC_OUTBOUND_PRICES_EXCEL;
        response.reset();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        workbook.close();
        try {
            FileCopyUtils.copy(outArray, response.getOutputStream());
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        outByteStream.flush();
        outByteStream.close();
    }

    /**
     * Create outbound workbook for excel
     *
     * @param workbook
     * @param gscOutboundPricingBeans
     * @param coloumnName
     * @return
     */
    private Workbook createOutboundWorkbook(Workbook workbook, List<GscOutboundPriceBean> gscOutboundPricingBeans, String coloumnName) {

        boolean dynamicColumnRequired = false;
        Sheet sheet = workbook.createSheet("Global_Outbound_Price_List");
        int rowId = 1;

        sheet.setColumnWidth(50000, 50000);
        Row headerRow = sheet.createRow(0);
        Cell headerCellValue;

        headerCellValue = headerRow.createCell(0);
        headerCellValue.setCellValue(GscConstants.COUNTRY);
        headerCellValue = headerRow.createCell(1);
        headerCellValue.setCellValue(GscConstants.PHONE_TYPE);
        headerCellValue = headerRow.createCell(2);
        headerCellValue.setCellValue(GscConstants.DEST_ID);
        headerCellValue = headerRow.createCell(3);
        headerCellValue.setCellValue(GscConstants.DESTINATION_NAME);
        headerCellValue = headerRow.createCell(4);
        headerCellValue.setCellValue(GscConstants.RATE_MIN);
        headerCellValue = headerRow.createCell(5);
        headerCellValue.setCellValue(GscConstants.COMMENTS);
        if (coloumnName != null && !coloumnName.equalsIgnoreCase(GscConstants.HIGH_RATE)) {
            headerCellValue = headerRow.createCell(6);
            headerCellValue.setCellValue(coloumnName);
            dynamicColumnRequired = true;
        }

        for (GscOutboundPriceBean gscOutboundPriceBean : gscOutboundPricingBeans) {
            int cellId = 0;
            Row row = sheet.createRow(rowId++);
            Cell cell;

            cell = row.createCell(cellId++);
            cell.setCellValue(gscOutboundPriceBean.getCountry());
            cell = row.createCell(cellId++);
            cell.setCellValue(gscOutboundPriceBean.getPhoneType());
            cell = row.createCell(cellId++);
            cell.setCellValue(gscOutboundPriceBean.getDestId());
            cell = row.createCell(cellId++);
            cell.setCellValue(gscOutboundPriceBean.getDestinationName());
            cell = row.createCell(cellId++);
            cell.setCellValue(gscOutboundPriceBean.getHighRate());
            cell = row.createCell(cellId++);
            cell.setCellValue(gscOutboundPriceBean.getComments());
            if (dynamicColumnRequired) {
                cell = row.createCell(cellId++);
                cell.setCellValue(gscOutboundPriceBean.getComments());
            }

        }
        return workbook;
    }

    /**
     * Create outbound in excel
     *
     * @param context
     * @return {@link PdfOutboundContext}
     * @throws IOException
     */
    private PdfOutboundContext createOutboundExcelFile(PdfOutboundContext context) throws IOException {
        context.referenceName = GscConstants.GSIP_OUTBOUND_EXCEL;
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        Workbook workbook = new HSSFWorkbook();
        createOutboundWorkbook(workbook, context.outBoundPdfRequest.getGscOutboundPriceBean(), context.dynamicColoumnName);
        workbook.write(outByteStream);
        InputStream inputStream = new ByteArrayInputStream(outByteStream.toByteArray());
        context.targetStream = inputStream;
        workbook.close();
        outByteStream.close();
        return context;
    }

    /**
     * Method to generate and save outbound prices via file storage
     *
     * @param quoteCode
     * @param response
     * @return {@link GscOutboundPricesDownloadBean}
     */
    @Transactional
    public GscOutboundPricesDownloadBean generateAndSaveOutboundPricesExcel(String quoteCode, HttpServletResponse response) {
        return Try.success(createOutboundContextExcel(quoteCode, response))
                .map(this::getQuoteForOutbound)
                .flatMapTry(this::saveOutboundPrices)
                .mapTry(this::createOutboundExcelFile)
                .mapTry(this::saveGeneratedFile)
                .map(context -> context.downloadBean).get();
    }

    private static class PdfOutboundContext {
        GscOutboundPricesDownloadBean downloadBean;
        List<GscOutboundSurchargePricingBean> surchargePricingBean;
        String quoteCode;
        Quote quote;
        QuoteToLe quoteToLe;
        String dynamicColoumnName;
        GscCofOutboundPricesPdf outBoundPdfRequest;
        HttpServletResponse response;
        String referenceName;
        InputStream targetStream;
        String fileName;
    }
}
