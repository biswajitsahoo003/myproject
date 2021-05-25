package com.tcl.dias.oms.gsc.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscConfigurationPriceBean;
import com.tcl.dias.oms.gsc.beans.GscManualPricing;
import com.tcl.dias.oms.gsc.beans.GscPricingRequest;
import com.tcl.dias.oms.gsc.beans.GscQuotePriceBean;
import com.tcl.dias.oms.gsc.beans.GscQuotePricingBean;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.pricing.beans.DomesticVoicePriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.DtfsAndAcnsPriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.GlobalOutboundPriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.ItfsAndUifnPriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.LnsPriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.PricingRequest;
import com.tcl.dias.oms.gsc.pricing.beans.PricingResponse;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscIsoCountries;
import com.tcl.dias.oms.gsc.util.GscUtils;
//import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.vavr.control.Try;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundError;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CUSTOMER_SECS_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_LE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.YES;

/**
 * Services to handle all price and feasibility related functionality
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscPricingFeasibilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscPricingFeasibilityService.class);
    @Autowired
    PricingDetailsRepository pricingDetailsRepository;
    @Autowired
    QuoteGscRepository quoteGscRepository;
    @Autowired
    QuoteGscDetailsRepository quoteGscDetailRepository;
    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;
    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;
    @Autowired
    QuotePriceRepository quotePriceRepository;
    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentAttributeValuesRepository;
    @Autowired
    QuoteToLeRepository quoteToLeRepository;
    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;
    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    RestClientService restClientService;

    @Value("${pricing.request.gsc.url}")
    String pricingUrl;

    @Value("${rabbitmq.gsc.outbound.pricing.queue}")
    String outboundPricingsQueue;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    OmsUtilService omsUtilService;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Value("${rabbitmq.customerle.queue}")
    String customerLeQueue;

    GlobalOutboundPriceBean globalOutboundPrice = null;

    @Autowired
    GscIsoCountries gscIsoCountries;

    @Value("${rabbitmq.customer.queue}")
    String customerDetailsQueue;

    @Value("${rabbitmq.gsc.outbound.queue}")
    String outboundDetailsQueue;

    @Value("${rabbitmq.customer.le.cuid}")
    String customerLeCuIdQueue;

    @Value("${rabbitmq.customer.le.secs.queue}")
    String customerLeSecsIdQueue;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    /**
     * Method to reset ContextPrice values
     *
     * @param context
     */
    private static void resetContextPrice(GscPricingFeasibilityContext context) {
        context.totalMRC = 0D;
        context.totalARC = 0D;
        context.totalNRC = 0D;
        context.termName = new ArrayList<>();
        context.rpmFixed = new ArrayList<>();
        context.rpmMobile = new ArrayList<>();
        context.rpmSpecial = new ArrayList<>();
        context.rpm = new ArrayList<>();
        context.gvpnTotalMrc = 0D;
        context.gvpnTotalNrc = 0D;
        context.gvpnTotalArc = 0D;
        context.gvpnTotalTcv = 0D;
    }

    /**
     * Create Context
     *
     * @param quoteLeId
     * @return
     */
    private GscPricingFeasibilityContext createContext(Integer quoteLeId, GscPricingRequest configData,
                                                       Boolean contractTermUpdate) {

        GscPricingFeasibilityContext context = new GscPricingFeasibilityContext();
        context.attributeName = FPConstants.IS_PRICING_DONE.toString();
        context.category = FPConstants.PRICING.toString();
        context.gscQuotePricingBean = new GscQuotePricingBean(quoteLeId);
        context.configurationData = configData;
        context.existingCurrency = LeAttributesConstants.INR;
        context.contractTermUpdate = contractTermUpdate;
        context.uifnRegCharge = 0.0;
        return context;
    }

    /**
     * Process price for given quote legal ID
     *
     * @param quoteLeId
     * @return {@link GscQuotePricingBean}
     */
    @Transactional
    public GscQuotePricingBean processPricing(Integer quoteLeId, GscPricingRequest configData) {
        Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);
        Objects.requireNonNull(configData, GscConstants.GSC_CONFIGURATION_DATA_NULL_MESSAGE);
        return Try.success(this.createContext(quoteLeId, configData, false))
                .flatMap(this::getQuoteLe)
                .flatMap(this::getGscs)
                //.map(this::getAdminChangedPriceValue)
                .mapTry(this::constructPricingRequest)
                .map(this::getCustomerLeCuId)
                .mapTry(this::getPricingResponse)
                .flatMap(this::validateResultantCurrency)
                .map(this::populateQuoteGscForPrice)
                .map(this::updateQuoteToLeWithPrice)
                .map(this::convertGscPrices)
                .map(this::enablePricingState)
                .map(context -> context.gscQuotePricingBean).get();
    }

    /**
     * @param context
     * @return
     */
    // TODO Need to rewrite this method; Just each object is not null
    private GscPricingFeasibilityContext getAdminChangedPriceValue(GscPricingFeasibilityContext context) {
        List<QuoteGsc> quoteGscs = context.quoteGscs;
        if (!CollectionUtils.isEmpty(quoteGscs)) {
            for (QuoteGsc quoteGsc : quoteGscs) {
                Integer quoteGscDetailId = quoteGsc.getQuoteGscDetails().stream().findFirst().get().getId();
                List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameAndStatus(quoteGsc.getAccessType(), GscConstants.STATUS_ACTIVE);
                if (!CollectionUtils.isEmpty(mstProductComponents)) {
                    Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(quoteGscDetailId,
                            mstProductComponents.stream().findFirst().get(), GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
                    if (Objects.nonNull(quoteProductComponent)) {
                        ProductAttributeMaster productAttributeMaster = productAttributeMasterRepository
                                .findByName(GscConstants.ADMIN_CHANGED_PRICE);
                        if (Objects.nonNull(productAttributeMaster)) {
                            List<QuoteProductComponentsAttributeValue> quoteProductComponentAndProductAttributeMaster = quoteProductComponentsAttributeValueRepository
                                    .findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent.get(), productAttributeMaster);
                            if (!CollectionUtils.isEmpty(quoteProductComponentAndProductAttributeMaster)) {
                                String value = quoteProductComponentAndProductAttributeMaster.stream().findFirst().get().getAttributeValues();
                                LOGGER.info("Admin Changed Price :: {}", value);
                                if (Objects.nonNull(value) && GscConstants.TRUE_FLAG.equalsIgnoreCase(value)) {
                                    //context.isAdminChangedPrice = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return context;
    }

    /**
     * Method to check for MACD new country
     *
     * @param context
     */
    private void checkMacdNewCountry(GscPricingFeasibilityContext context) {
        context.quoteGscs.stream().forEach(quoteGsc -> {
            quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
                if (Objects.isNull(quoteGscDetail.getType())
                        && GscConstants.ADD_COUNTRY.equalsIgnoreCase(context.quoteToLe.getQuoteCategory())) {
                    context.isMacdNewCountry = true;
                }
            });

        });
    }

    /**
     * Method to compute quoteLe prices and update in quoteLe table
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext updateQuoteToLeWithPrice(GscPricingFeasibilityContext context) {
        resetContextPrice(context);
        this.computeQuoteLePrices(context);
        context.gvpnAction = GscConstants.GET;
        this.setGvpnPriceAttributes(context);

        Double contractTerm = Double.parseDouble(context.pricingRequest.getOpportunityTerm());
        if (context.gvpnTotalArc == 0 && context.gvpnTotalMrc == 0 && context.gvpnTotalNrc == 0
                && context.gvpnTotalTcv == 0) {
            context.quoteToLe.setProposedMrc(context.totalMRC);
            context.quoteToLe.setProposedNrc(context.totalNRC);
            context.quoteToLe.setProposedArc(context.totalARC);
            context.quoteToLe.setFinalMrc(context.totalMRC);
            context.quoteToLe.setFinalNrc(context.totalNRC);
            context.quoteToLe.setFinalArc(context.totalARC);
            Double tcv = (contractTerm * this.setPrecision(context.quoteToLe.getFinalMrc(), 2))
                    + this.setPrecision(context.quoteToLe.getFinalNrc(), 2);
            context.quoteToLe.setTotalTcv(tcv);
        } else {
            context.quoteToLe.setProposedMrc(context.totalMRC + context.gvpnTotalMrc);
            context.quoteToLe.setProposedNrc(context.totalNRC + context.gvpnTotalNrc);
            context.quoteToLe.setProposedArc(context.totalARC + context.gvpnTotalArc);
            context.quoteToLe.setFinalMrc(context.totalMRC + context.gvpnTotalMrc);
            context.quoteToLe.setFinalNrc(context.totalNRC + context.gvpnTotalNrc);
            context.quoteToLe.setFinalArc(context.totalARC + context.gvpnTotalArc);
            Double tcv = (contractTerm * this.setPrecision(context.quoteToLe.getFinalMrc(), 2))
                    + this.setPrecision(context.quoteToLe.getFinalNrc(), 2);
            context.quoteToLe.setTotalTcv(tcv);

        }
        quoteToLeRepository.save(context.quoteToLe);

        context.gscQuotePricingBean.setTotalMRC(context.quoteToLe.getFinalMrc());
        context.gscQuotePricingBean.setTotalNRC(context.quoteToLe.getFinalNrc());
        context.gscQuotePricingBean.setTotalTCV(context.quoteToLe.getTotalTcv());
        return context;
    }

    /**
     * Method to set precision
     *
     * @param value
     * @param precision
     * @return
     */
    private Double setPrecision(Double value, Integer precision) {
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
     * Method to compute QuoteLe Prices
     *
     * @param context
     * @return
     */
    private GscPricingFeasibilityContext computeQuoteLePrices(GscPricingFeasibilityContext context) {
        context.gscQuotePricingBean.getGscQuotePrices().stream().forEach(gscQuotePrice -> {
            context.totalMRC = context.totalMRC
                    + (Objects.nonNull(gscQuotePrice.getMrc()) ? gscQuotePrice.getMrc() : 0D);
            context.totalNRC = context.totalNRC
                    + (Objects.nonNull(gscQuotePrice.getNrc()) ? gscQuotePrice.getNrc() : 0D);
            context.totalARC = context.totalARC
                    + (Objects.nonNull(gscQuotePrice.getArc()) ? gscQuotePrice.getArc() : 0D);
        });
        return context;
    }

    /**
     * Method to populate QuoteGsc for getting pricing details
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext populateQuoteGscForPrice(GscPricingFeasibilityContext context) {
        context.quoteGscs.stream().forEach(quoteGsc -> populateGscDetailForPrice(context, quoteGsc));
        return context;
    }

    /**
     * Method to validate resultant currency
     *
     * @param context
     * @return
     */
    private Try<GscPricingFeasibilityContext> validateResultantCurrency(GscPricingFeasibilityContext context) {
        /* this.findResultantCurrency(context); */
        context.resultantCurrency = context.quoteToLe.getCurrencyCode();
        return Objects.isNull(context.resultantCurrency)
                ? notFoundError(ExceptionConstants.RESULTANT_CURRENCY_VALIDATION_ERROR, "Payment currency is not found")
                : Try.success(context);
    }

    /**
     * Method to populate quoteGscDetail for getting pricing details
     *
     * @param context
     * @param quoteGsc
     */
    private void populateGscDetailForPrice(GscPricingFeasibilityContext context, QuoteGsc quoteGsc) {
        resetContextPrice(context);
        List<GscConfigurationPriceBean> gscConfigPrices = new ArrayList<>();
        if (context.configurationData.getProductName().equalsIgnoreCase(quoteGsc.getProductName())) {

            if (Objects.nonNull(quoteGsc.getQuoteGscDetails()) && !quoteGsc.getQuoteGscDetails().isEmpty()
                    && !context.configurationData.getGscConfigurationIds().isEmpty()) {

                filterQuoteGscDetailsBasedOnConfigIds(context, quoteGsc, gscConfigPrices);
            }
            saveQuoteGsc(context, quoteGsc);
            GscQuotePriceBean gscQuotePrice = createGscQuotePriceBean(context, quoteGsc, gscConfigPrices);
            context.gscQuotePricingBean.getGscQuotePrices().add(gscQuotePrice);
        } else if (context.configurationData.getProductName().equalsIgnoreCase("ALL")) {
            if (Objects.nonNull(quoteGsc.getQuoteGscDetails()) && !quoteGsc.getQuoteGscDetails().isEmpty()) {
                List<QuoteGscDetail> quoteGscDetails = new ArrayList<>();
                if (context.quoteToLe.getQuoteType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD)
                        && quoteGsc.getProductName().equalsIgnoreCase(GscConstants.UIFN)) {
                    quoteGscDetails.addAll(filterQuoteGscDetailsforMacd(quoteGsc));
                } else
                    quoteGscDetails.addAll(quoteGsc.getQuoteGscDetails());
                quoteGscDetails.sort((QuoteGscDetail o1, QuoteGscDetail o2) -> o1.getId() - o2.getId());

                processGscConfigPrices(context, quoteGsc, quoteGscDetails, gscConfigPrices);
            }
            saveQuoteGsc(context, quoteGsc);
            GscQuotePriceBean gscQuotePrice = createGscQuotePriceBean(context, quoteGsc, gscConfigPrices);
            context.gscQuotePricingBean.getGscQuotePrices().add(gscQuotePrice);
        }

    }

    /**
     * Method to filter quoteGscDetails based on type
     *
     * @param quoteGsc
     * @return
     */
    private List<QuoteGscDetail> filterQuoteGscDetailsforMacd(QuoteGsc quoteGsc) {
        List<QuoteGscDetail> quoteGscDetails = new ArrayList<>();
        quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
            if (Objects.isNull(quoteGscDetail.getType())) {
                quoteGscDetails.add(quoteGscDetail);
            }
        });
        return quoteGscDetails;
    }

    /**
     * Method to filter quoteGscDetails based on configIds
     *
     * @param context
     * @param quoteGsc
     * @param gscConfigPrices
     */
    private void filterQuoteGscDetailsBasedOnConfigIds(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                                       List<GscConfigurationPriceBean> gscConfigPrices) {
        List<QuoteGscDetail> quoteGscDetails = new ArrayList<>();
        context.configurationData.getGscConfigurationIds().stream().forEach(configId -> {
            quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
                if (configId.equals(quoteGscDetail.getId())) {
                    quoteGscDetails.add(quoteGscDetail);
                }
            });

        });
        if (!quoteGscDetails.isEmpty()) {
            processGscConfigPrices(context, quoteGsc, quoteGscDetails, gscConfigPrices);
        }
    }

    /**
     * Method to create GscQuotePriceBean based on quoteGsc
     *
     * @param quoteGsc
     * @param gscConfigPrices
     * @return
     */
    private GscQuotePriceBean createGscQuotePriceBean(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                                      List<GscConfigurationPriceBean> gscConfigPrices) {
        GscQuotePriceBean gscQuotePrice = new GscQuotePriceBean();
        gscQuotePrice.setProductName(quoteGsc.getProductName());
        gscQuotePrice.setAccessType(quoteGsc.getAccessType());
        gscQuotePrice.setGscConfigPrices(gscConfigPrices);
        gscQuotePrice.setId(quoteGsc.getId());
        gscQuotePrice.setName(quoteGsc.getName());
        gscQuotePrice.setMrc(quoteGsc.getMrc());
        gscQuotePrice.setNrc(quoteGsc.getNrc());
        gscQuotePrice.setArc(quoteGsc.getArc());
        gscQuotePrice.setTcv(quoteGsc.getTcv());
        return gscQuotePrice;
    }

    /**
     * Method to save QuoteGsc in db
     *
     * @param context
     * @param quoteGsc
     */
    private void saveQuoteGsc(GscPricingFeasibilityContext context, QuoteGsc quoteGsc) {

        if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.UIFN) && Objects.nonNull(context.uifnRegCharge)
                && context.uifnRegCharge != 0) {
            context.totalNRC = context.totalNRC + context.uifnRegCharge;
            context.totalNRC = Double.parseDouble(String.format("%.2f", context.totalNRC));
        }

        quoteGsc.setMrc(this.setPrecision(context.totalMRC, 2));
        quoteGsc.setArc(this.setPrecision(context.totalARC, 2));
        quoteGsc.setNrc(this.setPrecision(context.totalNRC, 2));
        Double contractTerm = Double.parseDouble(context.pricingRequest.getOpportunityTerm());
        Double tcv = (contractTerm * quoteGsc.getMrc()) + quoteGsc.getNrc();
        quoteGsc.setTcv(this.setPrecision(tcv, 2));
        quoteGscRepository.save(quoteGsc);
    }

    /**
     * Method to process GscConfig Prices based on quoteGsc
     *
     * @param context
     * @param quoteGsc
     * @param gscConfigPrices
     */
    private void processGscConfigPrices(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                        List<QuoteGscDetail> quoteGscDetails, List<GscConfigurationPriceBean> gscConfigPrices) {

        List<Integer> quoteGscDetailsId = quoteGsc.getQuoteGscDetails().stream().map(QuoteGscDetail::getId).collect(Collectors.toList());
        Collections.sort(quoteGscDetailsId);
        quoteGscDetails.stream()
                .forEach(gscConfig -> populateGscConfigDetails(context, gscConfigPrices, quoteGsc, gscConfig, quoteGscDetailsId));
        this.computeQuoteGscPrices(context, quoteGsc, gscConfigPrices);

    }

    /**
     * Method to compute QuoteGsc Prices based on gscConfig prices
     *
     * @param context
     * @param gscConfigPrices
     */
    private void computeQuoteGscPrices(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                       List<GscConfigurationPriceBean> gscConfigPrices) {
        quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
            context.totalMRC = context.totalMRC
                    + (Objects.nonNull(quoteGscDetail.getMrc()) ? quoteGscDetail.getMrc() : 0D);
            context.totalNRC = context.totalNRC
                    + (Objects.nonNull(quoteGscDetail.getNrc()) ? quoteGscDetail.getNrc() : 0D);
            context.totalARC = context.totalARC
                    + (Objects.nonNull(quoteGscDetail.getArc()) ? quoteGscDetail.getArc() : 0D);
        });
    }

    /**
     * Method to save QuoteGsc Detail
     *
     * @param context
     * @param quoteGscDetail
     */
    private void saveQuoteGscDetail(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                    QuoteGscDetail quoteGscDetail) {

        quoteGscDetail.setMrc(this.setPrecision(context.totalMRC, 2));
        quoteGscDetail.setNrc(this.setPrecision(context.totalNRC, 2));
        quoteGscDetail.setArc(this.setPrecision(context.totalARC, 2));
        quoteGscDetailRepository.save(quoteGscDetail);
    }

    /**
     * Method to get QuoteLe from db
     *
     * @param context
     * @return
     */
    private Try<GscPricingFeasibilityContext> getQuoteLe(GscPricingFeasibilityContext context) {
        Optional<QuoteToLe> quoteLeOptional = quoteToLeRepository.findById(context.gscQuotePricingBean.getQuoteLeId());
        context.quoteToLe = quoteLeOptional.isPresent() ? quoteLeOptional.get() : null;
        return Objects.isNull(context.quoteToLe)
                ? notFoundError(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR, "QuoteLe not found")
                : Try.success(context);
    }

    /**
     * Method to get MstOmsAttribute from db
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext getMstOmsAttribute(GscPricingFeasibilityContext context) {
        List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(context.attributeName,
                CommonConstants.BACTIVE);
        context.mstOmsAttribute = !mstOmsAttributes.isEmpty() ? mstOmsAttributes.get(0)
                : constructOmsAttribute(context);
        return context;
    }

    /**
     * Method to construct OmsAttribute and save in db
     *
     * @param context
     * @return
     */
    private MstOmsAttribute constructOmsAttribute(GscPricingFeasibilityContext context) {
        MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
        mstOmsAttribute.setName(context.attributeName);
        mstOmsAttribute.setCategory(context.category);
        mstOmsAttribute.setDescription(context.attributeName);
        mstOmsAttribute.setIsActive(GscConstants.STATUS_ACTIVE);
        mstOmsAttribute.setCreatedTime(new Date());
        return mstOmsAttributeRepository.save(mstOmsAttribute);
    }

    /**
     * Method to get QuoteLeAttributeValues
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext getQuoteLeAttributeValues(GscPricingFeasibilityContext context) {
        List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
                .findByQuoteToLeAndMstOmsAttribute(context.quoteToLe, context.mstOmsAttribute);
        context.quoteLeAttributeValue = !quoteLeAttributeValues.isEmpty() ? quoteLeAttributeValues.get(0)
                : constructQuoteLeAttributeValue(context);
        return context;
    }

    /**
     * Method to construct QuoteLeAttributeValue
     *
     * @param context
     * @return QuoteLeAttributeValue
     */
    private QuoteLeAttributeValue constructQuoteLeAttributeValue(GscPricingFeasibilityContext context) {
        QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
        attributeValue.setAttributeValue(context.state);
        attributeValue.setDisplayValue(context.attributeName);
        attributeValue.setQuoteToLe(context.quoteToLe);
        attributeValue.setMstOmsAttribute(context.mstOmsAttribute);
        return quoteLeAttributeValueRepository.save(attributeValue);
    }

    /**
     * Method to update QuoteLeAttributeValue in db
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext updateQuoteLeAttributeValue(GscPricingFeasibilityContext context) {
        context.quoteLeAttributeValue.setAttributeValue(context.state);
        quoteLeAttributeValueRepository.save(context.quoteLeAttributeValue);
        return context;
    }

    /**
     * Method to enable Pricing status based on state
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext enablePricingState(GscPricingFeasibilityContext context) {
        context.state = FPConstants.TRUE.toString();
        this.saveProcessState(context);
        return context;
    }

    /**
     * Method to save State in db
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext saveProcessState(GscPricingFeasibilityContext context) {
        this.getMstOmsAttribute(context);
        this.getQuoteLeAttributeValues(context);
        this.updateQuoteLeAttributeValue(context);
        return context;
    }

    /**
     * Method to filter QuoteGsc based on active status
     *
     * @param context
     * @param quoteGsc
     */
    private void processQuoteGsc(GscPricingFeasibilityContext context, QuoteGsc quoteGsc) {
        if (GscConstants.STATUS_ACTIVE == quoteGsc.getStatus()) {
            context.quoteGscs.add(quoteGsc);
        }
    }

    /**
     * Method to populate QuoteGsc based on product solution
     *
     * @param context
     * @param productSolution
     */
    private void populateQuoteGsc(GscPricingFeasibilityContext context, ProductSolution productSolution) {
        List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
                GscConstants.STATUS_ACTIVE);
        quoteGscs.stream().forEach(quoteGsc -> processQuoteGsc(context, quoteGsc));
    }

    /**
     * Method to populate ProductSolution based on quoteLeProductFamily
     *
     * @param context
     * @param quoLeFamily
     */
    private void populateProductSolution(GscPricingFeasibilityContext context, QuoteToLeProductFamily quoLeFamily) {
        quoLeFamily.getProductSolutions().stream()
                .forEach(productSolution -> populateQuoteGsc(context, productSolution));
    }

    /**
     * Method to populate QuoteLeFamily based on quoteLe
     *
     * @param context
     */
    private void populateQuoteLeFamily(GscPricingFeasibilityContext context) {
        context.quoteToLe.getQuoteToLeProductFamilies().stream()
                .forEach(quoLeFamily -> populateProductSolution(context, quoLeFamily));
    }

    /**
     * Method to get the list of active quoteGsc
     *
     * @param context
     * @return
     */
    private Try<GscPricingFeasibilityContext> getGscs(GscPricingFeasibilityContext context) {
        context.quoteGscs = new ArrayList<>();
        this.populateQuoteLeFamily(context);
        return context.quoteGscs.isEmpty()
                ? notFoundError(ExceptionConstants.QUOTE_GSC_EMPTY, "QuoteGsc list is not found")
                : Try.success(context);
    }

    private GscPricingFeasibilityContext getCustomerLeCuId(GscPricingFeasibilityContext context) {
        Integer erfCusCustomerLegalEntityId = context.quoteToLe.getErfCusCustomerLegalEntityId();
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeCuId {} {} :", erfCusCustomerLegalEntityId,
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        try {
            String cuId = "";
            if (Objects.nonNull(erfCusCustomerLegalEntityId)) {
                cuId = (String) mqUtils.sendAndReceive(customerLeCuIdQueue, String.valueOf(erfCusCustomerLegalEntityId));
            }
            context.pricingRequest.setCuLeId(cuId);
        } catch (TclCommonException e) {
            LOGGER.warn("Error Occured while getting CustomerLe CUID :: {}", e.getStackTrace());
        }

        return context;
    }

    /**
     * Method to construct Pricing Request
     *
     * @param context
     * @return
     */
    private GscPricingFeasibilityContext constructPricingRequest(GscPricingFeasibilityContext context) {
        context.pricingRequest = new PricingRequest();
        context.isMacdNewCountry = false;
        this.setAccountDetails(context);
        context.pricingRequest.setAccountRTMCust(GscConstants.DIRECT);
        context.pricingRequest
                .setOpportunityTerm(String.valueOf(getMonthsforOpportunityTerms(context.quoteToLe.getTermInMonths())));
        context.pricingRequest.setCuLeId(findCustomerLeId());
        context.pricingRequest.setVolumeProjected(GscConstants.DEFAULT_VOLUME.toString());
        context.pricingRequest.setQuoteTypePartner("None");

        getInboundAndOutboundCountries(context);
        checkMacdNewCountry(context);
        setNewAttributes(context);
        setMacdAttributes(context);
        this.setGscQuoteDetails(context);
        this.validatePricingRequest(context);

        return context;
    }

    private void setNewAttributes(GscPricingFeasibilityContext context) {
        if (GscConstants.NEW.equalsIgnoreCase(context.quoteToLe.getQuoteType())) {
            context.pricingRequest.setQuoteTypeQuote(GscConstants.QUOTE_TYPE_NEW_ORDER);
            context.pricingRequest.setMacdNewCountry("0");
            context.pricingRequest.setOrgId(getSECSId(context));
        }
    }

    /**
     * Method to set MacdAttributes
     *
     * @param context
     */
    private void setMacdAttributes(GscPricingFeasibilityContext context) {
        if (GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(context.quoteToLe.getQuoteType())) {
            context.pricingRequest.setQuoteTypeQuote(GscConstants.ORDER_TYPE_MACD);
            setMacdNewCountry(context);
            setOrgIdForMACD(context);
        }
    }

    private void setMacdNewCountry(GscPricingFeasibilityContext context) {
        if (context.isMacdNewCountry) {
            context.pricingRequest.setMacdNewCountry("1");
        } else {
            context.pricingRequest.setMacdNewCountry("0");
        }
    }

    private void setOrgIdForMACD(GscPricingFeasibilityContext context) {
        context.pricingRequest.setOrgId(getSECSId(context));
    }

    private String getSECSId(GscPricingFeasibilityContext context) {
        String secsId = "";
        List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(context.quoteToLe,
                ATTR_CUSTOMER_SECS_ID);
        if (!org.springframework.util.CollectionUtils.isEmpty(quoteLeAttributeValues)) {
            secsId = quoteLeAttributeValues.stream().findFirst().get().getAttributeValue();
        }
        return secsId;
    }

    private String getOrgId(GscPricingFeasibilityContext context) {
        String orgId = "";
        Integer erfCusCustomerLegalEntityId = context.quoteToLe.getErfCusCustomerLegalEntityId();
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeCuId {} {} :", erfCusCustomerLegalEntityId,
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        try {
            if (Objects.nonNull(erfCusCustomerLegalEntityId)) {
                orgId = (String) mqUtils.sendAndReceive(customerLeSecsIdQueue, String.valueOf(erfCusCustomerLegalEntityId));
            }
        } catch (TclCommonException e) {
            LOGGER.warn("Error Occured while getting CustomerLe SECS ID :: {}", e.getStackTrace());
        }
        return orgId;
    }

    /**
     * Method to validate pricing request
     *
     * @param context
     */
    private void validatePricingRequest(GscPricingFeasibilityContext context) {
        if (context.pricingRequest.getItfsRequested().equalsIgnoreCase("1")) {
            if (context.pricingRequest.getOriginItfs().isEmpty()
                    || context.pricingRequest.getNoRequestedItfs().isEmpty()
                    || context.pricingRequest.getNoPortedItfs().isEmpty() || context.pricingRequest.getOriginItfs()
                    .size() != context.pricingRequest.getNoRequestedItfs().size()) {
                context.pricingRequest.setItfsRequested("0");
            }
        }
        if (context.pricingRequest.getLnsRequested().equalsIgnoreCase("1")) {
            if (context.pricingRequest.getOriginLns().isEmpty() || context.pricingRequest.getNoRequestedLns().isEmpty()
                    || context.pricingRequest.getNoPortedLns().isEmpty() || context.pricingRequest.getOriginLns()
                    .size() != context.pricingRequest.getNoRequestedLns().size()) {
                context.pricingRequest.setLnsRequested("0");
            }
        }
        if (context.pricingRequest.getUifnRequested().equalsIgnoreCase("1")) {
            if (!context.pricingRequest.getOriginUifn().isEmpty()
                    && context.pricingRequest.getNoRequestedUifn().isEmpty()
                    && context.pricingRequest.getNoPortedUifn().isEmpty()) {
                context.pricingRequest.getNoRequestedUifn().add("0");
                context.pricingRequest.getNoPortedUifn().add("0");
            }
            if (context.pricingRequest.getOriginUifn().isEmpty()
                    || context.pricingRequest.getNoRequestedUifn().isEmpty()
                    || context.pricingRequest.getNoPortedUifn().isEmpty() || context.pricingRequest.getOriginUifn()
                    .size() != context.pricingRequest.getNoRequestedUifn().size()) {
                context.pricingRequest.setUifnRequested("0");
            }
            if (context.pricingRequest.getOriginUifn().size() > 1
                    && context.pricingRequest.getNoRequestedUifn().size() == 1
                    && context.pricingRequest.getNoPortedUifn().size() == 1) {
                String requestedNo = context.pricingRequest.getNoRequestedUifn().stream().findFirst().get();
                String portedNo = context.pricingRequest.getNoPortedUifn().stream().findFirst().get();
                if (requestedNo.equalsIgnoreCase("0") && portedNo.equalsIgnoreCase("0")) {
                    context.pricingRequest.setUifnRequested("1");
                }
            }
        }
        if (context.pricingRequest.getAcsAnsRequested().equalsIgnoreCase("1")) {
            if (context.pricingRequest.getOriginAcns().isEmpty()
                    || context.pricingRequest.getNoRequestedAcns().isEmpty() || context.pricingRequest.getOriginAcns()
                    .size() != context.pricingRequest.getNoRequestedAcns().size()) {
                context.pricingRequest.setAcsAnsRequested("0");
            }
        }
        if (context.pricingRequest.getAcsDtfRequested().equalsIgnoreCase("1")) {
            if (context.pricingRequest.getOriginDtfs().isEmpty()
                    || context.pricingRequest.getNoRequestedDtfs().isEmpty() || context.pricingRequest.getOriginDtfs()
                    .size() != context.pricingRequest.getNoRequestedDtfs().size()) {
                context.pricingRequest.setAcsDtfRequested("0");
            }
        }
        if (context.pricingRequest.getAcsSnsRequested().equalsIgnoreCase("1")) {
            if (context.pricingRequest.getOriginAcsns().isEmpty()
                    || context.pricingRequest.getNoRequestedAcsns().isEmpty() || context.pricingRequest.getOriginAcsns()
                    .size() != context.pricingRequest.getNoRequestedAcsns().size()) {
                context.pricingRequest.setAcsSnsRequested("0");
            }
        }
        if (context.pricingRequest.getDomesticVoiceRequested().equalsIgnoreCase("1")) {
            if (context.pricingRequest.getOriginDomesticVoice().isEmpty()
                    || context.pricingRequest.getNoRequestedDomesticVoice().isEmpty()
                    || context.pricingRequest.getNoPortedDomesticVoice().isEmpty()
                    || context.pricingRequest.getChannelsDomesticVoice().isEmpty()
                    || context.pricingRequest.getOriginDomesticVoice().size() != context.pricingRequest
                    .getNoRequestedDomesticVoice().size()) {
                context.pricingRequest.setDomesticVoiceRequested("0");
            }
        }

    }

    /**
     * Method to convert year into months
     *
     * @param termPeriod
     * @return
     */
    private Integer getMonthsforOpportunityTerms(String termPeriod) {
        String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
        Integer month = Integer.valueOf(reg[0]);
        if (reg.length > 0) {
            if (termPeriod.contains("year")) {
                return month * 12;
            }
        }
        return month;
    }

    /**
     * Method to set account details
     *
     * @param context
     */
    private void setAccountDetails(GscPricingFeasibilityContext context) {
        CustomerDetailsBean customerDetails = findCustomerDetails(context);
        if (customerDetails != null) {
            customerDetails.getCustomerAttributes().stream().forEach(attribute -> {
                if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
                    context.pricingRequest.setAccountIdWith18Digit(attribute.getValue());
                } else if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
                    context.pricingRequest.setCustomerSegment(attribute.getValue());
                } else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
                    context.pricingRequest.setSalesOrg(attribute.getValue());
                }
            });
        }
    }

    /**
     * Method to find customer details
     *
     * @param context
     * @return
     */
    private CustomerDetailsBean findCustomerDetails(GscPricingFeasibilityContext context) {
        CustomerDetailsBean customerDetails = null;
        try {
            customerDetails = processCustomerData(context.quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
        } catch (Exception e) {
            throw new TCLException(ExceptionConstants.CUSTOMER_DETAILS_EMPTY, e.getMessage());
        }
        return customerDetails;
    }

    /**
     * Method to find customerLe Id
     *
     * @return
     */
    private String findCustomerLeId() {
        String customerLeId = StringUtils.EMPTY;
        try {
            getCustomerLeDetails();
        } catch (Exception e) {
            throw new TCLException(ExceptionConstants.CUSTOMER_LE_DETAILS_EMPTY, e.getMessage());
        }
        return customerLeId;
    }

    /**
     * Method to get customerLe details
     *
     * @return {@link String}
     * @throws Exception
     */
    private String getCustomerLeDetails() throws TclCommonException {
        String customerLeId = StringUtils.EMPTY;
        List<String> customerLeIdsList = new ArrayList<>();
        List<CustomerDetail> cusLeIds = userInfoUtils.getCustomerDetails();
        if (cusLeIds != null && !cusLeIds.isEmpty()) {
            if (cusLeIds.get(0).getCustomerLeId() != null) {
                customerLeIdsList.add(cusLeIds.get(0).getCustomerLeId().toString());
                String customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
                if (customerLeIdsCommaSeparated != null) {
                    LOGGER.info("MDC Filter token value in before Queue call getCustomerLeDetails {} :",
                            MDC.get(CommonConstants.MDC_TOKEN_KEY));
                    String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
                    if (StringUtils.isNotBlank(response)) {
                        CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
                                .convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
                        if (null != cLeBean)
                            customerLeId = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
                    }
                }
            }
        }
        return customerLeId;
    }

    /**
     * Method to set quotegsc details
     *
     * @param context
     */
    private void setGscQuoteDetails(GscPricingFeasibilityContext context) {
        context.quoteGscs.stream().forEach(quoteGsc -> {
            context.pricingRequest.setAccessType(quoteGsc.getAccessType().toLowerCase());
            if (context.configurationData.getProductName().equalsIgnoreCase(quoteGsc.getProductName())) {
                this.setGscQuoteDetailsForSpecificProduct(context, quoteGsc);
            } else if (context.configurationData.getProductName().equalsIgnoreCase("ALL")) {
                this.setGscQuoteDetailsForAllProduct(context, quoteGsc);
            }
        });
    }

    /**
     * Method to set quotegsc details
     *
     * @param context
     */
    private void setGscQuoteDetailsForSpecificProduct(GscPricingFeasibilityContext context, QuoteGsc quoteGsc) {
        quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
            if (context.configurationData.getGscConfigurationIds().contains(quoteGscDetail.getId())) {
                this.setOriginAndDestination(context, quoteGsc, quoteGscDetail);
                this.setGscConfigAttributes(context, quoteGsc, quoteGscDetail);
            }
        });
    }

    /**
     * Method to set quotegsc details
     *
     * @param context
     */
    private void setGscQuoteDetailsForAllProduct(GscPricingFeasibilityContext context, QuoteGsc quoteGsc) {
        quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
            this.setOriginAndDestination(context, quoteGsc, quoteGscDetail);
            this.setGscConfigAttributes(context, quoteGsc, quoteGscDetail);
        });
    }

    /**
     * Method to set origin and destination
     *
     * @param context
     * @param quoteGsc
     * @param quoteGscDetail
     */
    private void setOriginAndDestination(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                         QuoteGscDetail quoteGscDetail) {
        context.inboundVolume = 0.0;
        context.outboundVolume = 0.0;

        setVolumeCommitmentAndRateColumnAttributes(context);

        switch (quoteGsc.getProductName()) {
            case GscConstants.ITFS: {
                context.pricingRequest.setItfsRequested("1");
                if (Objects.nonNull(quoteGscDetail.getSrc())) {
                    context.pricingRequest.getOriginItfs().add(quoteGscDetail.getSrc());
                }
                if (Objects.nonNull(quoteGscDetail.getDest())
                        && !context.pricingRequest.getDestinationItfs().contains(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationItfs().add(quoteGscDetail.getDest());
                } else if (Objects.isNull(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationItfs().add("NA");
                }
                setInboundVolumeCommitment(context);
            }
            break;
            case GscConstants.LNS: {
                context.pricingRequest.setLnsRequested("1");
                if (Objects.nonNull(quoteGscDetail.getSrc())) {
                    context.pricingRequest.getOriginLns().add(quoteGscDetail.getSrc());
                }
                if (Objects.nonNull(quoteGscDetail.getDest())
                        && !context.pricingRequest.getDestinationLns().contains(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationLns().add(quoteGscDetail.getDest());
                } else if (Objects.isNull(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationLns().add("NA");
                }
                setInboundVolumeCommitment(context);

            }
            break;
            case GscConstants.UIFN: {
                context.pricingRequest.setUifnRequested("1");
                if (Objects.nonNull(quoteGscDetail.getSrc())) {
                    context.pricingRequest.getOriginUifn().add(quoteGscDetail.getSrc());
                }
                if (Objects.nonNull(quoteGscDetail.getDest())
                        && !context.pricingRequest.getDestinationUifn().contains(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationUifn().add(quoteGscDetail.getDest());
                } else if (Objects.isNull(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationUifn().add("NA");
                }
                setInboundVolumeCommitment(context);

            }
            break;
            case GscConstants.ACANS: {
                context.pricingRequest.setAcsAnsRequested("1");
                if (Objects.nonNull(quoteGscDetail.getSrc())) {
                    context.pricingRequest.getOriginAcns().add(quoteGscDetail.getSrc());
                }
                if (Objects.nonNull(quoteGscDetail.getDest())
                        && !context.pricingRequest.getDestinationAcns().contains(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationAcns().add(quoteGscDetail.getDest());
                } else if (Objects.isNull(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationAcns().add("NA");
                }
                setInboundVolumeCommitment(context);

            }
            break;
            case GscConstants.ACSNS: {
                context.pricingRequest.setAcsSnsRequested("1");
                if (Objects.nonNull(quoteGscDetail.getSrc())) {
                    context.pricingRequest.getOriginAcsns().add(quoteGscDetail.getSrc());
                }
                if (Objects.nonNull(quoteGscDetail.getDest())
                        && !context.pricingRequest.getDestinationAcsns().contains(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationAcsns().add(quoteGscDetail.getDest());
                } else if (Objects.isNull(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationAcsns().add("NA");
                }
                setInboundVolumeCommitment(context);

            }
            break;
            case GscConstants.ACDTFS: {
                context.pricingRequest.setAcsDtfRequested("1");
                if (Objects.nonNull(quoteGscDetail.getSrc())) {
                    context.pricingRequest.getOriginDtfs().add(quoteGscDetail.getSrc());
                }
                if (Objects.nonNull(quoteGscDetail.getDest())
                        && !context.pricingRequest.getDestinationDtfs().contains(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationDtfs().add(quoteGscDetail.getDest());
                } else if (Objects.isNull(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationDtfs().add("NA");
                }
                setInboundVolumeCommitment(context);
            }
            break;
            case GscConstants.DOMESTIC_VOICE: {
                context.pricingRequest.setDomesticVoiceRequested("1");
                if (Objects.nonNull(quoteGscDetail.getSrc())) {
                    context.pricingRequest.getOriginDomesticVoice().add(quoteGscDetail.getSrc());
                }
                setInboundVolumeCommitment(context);
            }
            break;
            case GscConstants.GLOBAL_OUTBOUND: {
                context.pricingRequest.setGlobalOutboundRequested("1");
                if (Objects.nonNull(quoteGscDetail.getDest())
                        && !context.pricingRequest.getDestinationGlobalOutbound().contains(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationGlobalOutbound().add(quoteGscDetail.getDest());
                } else if (Objects.isNull(quoteGscDetail.getDest())) {
                    context.pricingRequest.getDestinationGlobalOutbound().add("NA");
                }

                setOutboundVolumeCommitment(context);

            }
            break;
            default:
                break;

        }

    }

    /**
     * inbound VolumeCommitment
     *
     * @param context
     */
    private void setInboundVolumeCommitment(GscPricingFeasibilityContext context) {
        if (context.inboundVolume != 0) {
            context.inboundVolume = context.inboundVolume * 1000000;
            context.pricingRequest.setInboundVolume(BigDecimal.valueOf(context.inboundVolume).toPlainString());
        } else {
            context.pricingRequest.setInboundVolume("NA");
        }

    }

    /**
     * outbound VolumeCommitment
     *
     * @param context
     */
    private void setOutboundVolumeCommitment(GscPricingFeasibilityContext context) {
        if (context.outboundVolume != 0) {
            context.outboundVolume = context.outboundVolume * 1000000;
            context.pricingRequest.setOutboundVolume(BigDecimal.valueOf(context.outboundVolume).toPlainString());
        } else {
            context.pricingRequest.setOutboundVolume("NA");
        }
    }

    /**
     * Method to set gsc configuration attributes
     *
     * @param context
     * @param quoteGsc
     * @param gscConfig
     */
    private void setGscConfigAttributes(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                        QuoteGscDetail gscConfig) {
        List<QuoteProductComponent> quoteProductComponents = getQuoteProductComponents(gscConfig);
        if (!quoteProductComponents.isEmpty()) {
            List<String> attributesList = Arrays.asList(GscConstants.COUNT_REQUESTED_NUMBERS,
                    GscConstants.COUNT_PORTED_NUMBERS, GscConstants.COUNT_CONCURRENT_CHANNELS);
            List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
            quoteProductComponents.stream().forEach(quoteComponent -> {
                attributes.stream().forEach(attribute -> {
                    this.setNumbersAttributes(context, quoteGsc, quoteComponent, attribute);
                });
            });

        }
    }

    /**
     * Method to set number attributes
     *
     * @param context
     * @param quoteGsc
     * @param quoteComponent
     * @param attribute
     */
    private void setNumbersAttributes(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                      QuoteProductComponent quoteComponent, ProductAttributeMaster attribute) {
        List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentAttributeValuesRepository
                .findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute);
        if (attributeValueList != null && !attributeValueList.isEmpty()) {
            QuoteProductComponentsAttributeValue attributeValue = attributeValueList.get(0);
            if (attributeValue.getAttributeValues().equals("")) {
                attributeValue.setAttributeValues("0");
                quoteProductComponentAttributeValuesRepository.save(attributeValue);
            }
            if (attributeValue.getProductAttributeMaster().getId() == attribute.getId()) {
                this.setCountOfNumbers(context, quoteGsc, attribute, attributeValue);
            }
        }

    }

    /**
     * Method to set count of numbers
     *
     * @param context
     * @param quoteGsc
     * @param attribute
     * @param attributeValue
     */
    private void setCountOfNumbers(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                   ProductAttributeMaster attribute, QuoteProductComponentsAttributeValue attributeValue) {
        switch (quoteGsc.getProductName()) {
            case GscConstants.ITFS: {
                if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_PORTED_NUMBERS)) {
                    context.pricingRequest.getNoPortedItfs().add(attributeValue.getAttributeValues());
                } else if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_REQUESTED_NUMBERS)) {
                    context.pricingRequest.getNoRequestedItfs().add(attributeValue.getAttributeValues());
                }
            }
            break;
            case GscConstants.LNS: {
                if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_PORTED_NUMBERS)) {
                    context.pricingRequest.getNoPortedLns().add(attributeValue.getAttributeValues());
                } else if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_REQUESTED_NUMBERS)) {
                    context.pricingRequest.getNoRequestedLns().add(attributeValue.getAttributeValues());
                }
            }
            break;
            case GscConstants.UIFN: {
                if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_PORTED_NUMBERS)) {
                    context.pricingRequest.getNoPortedUifn().add(attributeValue.getAttributeValues());
                } else if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_REQUESTED_NUMBERS)) {
                    context.pricingRequest.getNoRequestedUifn().add(attributeValue.getAttributeValues());
                }
            }
            break;
            case GscConstants.ACANS: {
                if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_REQUESTED_NUMBERS)) {
                    context.pricingRequest.getNoRequestedAcns().add(attributeValue.getAttributeValues());
                }
            }
            break;
            case GscConstants.ACDTFS: {
                if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_REQUESTED_NUMBERS)) {
                    context.pricingRequest.getNoRequestedDtfs().add(attributeValue.getAttributeValues());
                }
            }
            break;
            case GscConstants.DOMESTIC_VOICE: {
                if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_PORTED_NUMBERS)) {
                    context.pricingRequest.getNoPortedDomesticVoice().add(attributeValue.getAttributeValues());
                } else if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_REQUESTED_NUMBERS)) {
                    context.pricingRequest.getNoRequestedDomesticVoice().add(attributeValue.getAttributeValues());
                } else if (attribute.getName().equalsIgnoreCase(GscConstants.COUNT_CONCURRENT_CHANNELS)) {
                    context.pricingRequest.getChannelsDomesticVoice().add(attributeValue.getAttributeValues());
                }

                if (quoteGsc.getQuoteToLe().getQuoteType().equalsIgnoreCase(GscConstants.ORDER_TYPE_MACD)) {
                    context.pricingRequest.setChannelsDomesticVoice(Arrays.asList("1"));
                }
            }
            break;
            default:
                break;

        }

    }

    /**
     * Method to get quoteProduct components
     *
     * @param gscConfig
     * @return {@link List<QuoteProductComponent>}
     */
    private List<QuoteProductComponent> getQuoteProductComponents(QuoteGscDetail gscConfig) {
        return quoteProductComponentRepository.findByReferenceIdAndType(gscConfig.getId(),
                GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
    }

    /**
     * Method to get pricing response
     *
     * @param context
     * @return {@link GscPricingFeasibilityContext}
     */
    private GscPricingFeasibilityContext getPricingResponse(GscPricingFeasibilityContext context) {
        try {
            this.persistRequestPricingDetails(context.pricingRequest, GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE,
                    context.quoteToLe.getQuote());

            // TODO : Remove this function once Pricing Engine updated with Country Code
            // Do convert from Country Code to Country Name
            // context.pricingRequest = convertCountryCode(context.pricingRequest);

            String request = Utils.convertObjectToJson(context.pricingRequest);
            LOGGER.info("Pricing GSC input :: {}", request);
            RestResponse pricingResponse = restClientService.post(pricingUrl, request);// Call the URL with respect to
            LOGGER.info("Pricing GSC URL :: {}", pricingUrl);
            if (pricingResponse.getStatus() == Status.SUCCESS) {
                String response = pricingResponse.getData();
                LOGGER.info("Pricing GSC output :: {}", response);
                response = response.replaceAll("\"NA\"", "\"0\"");
                LOGGER.info("Pricing GSC output :: {}", response);
                context.pricingResponse = new PricingResponse();
                context.pricingResponse = (PricingResponse) Utils.convertJsonToObject(response, PricingResponse.class);
                this.persistResponsePricingDetails(context.pricingResponse,
                        GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, context.quoteToLe.getQuote());
            }
        } catch (Exception e) {
            LOGGER.warn("Error in getting pricing response" + ExceptionUtils.getStackTrace(e));
            throw new TCLException(ExceptionConstants.PRICING_VALIDATION_ERROR, e.getMessage());
        }
        return context;
    }

    // TODO : Remove this function once Pricing Engine updated with Country Code
    private PricingRequest convertCountryCode(PricingRequest request) {
        request.setOriginItfs(convertToCountryName(request.getOriginItfs()));
        request.setOriginLns(convertToCountryName(request.getOriginLns()));
        request.setOriginUifn(convertToCountryName(request.getOriginUifn()));
        request.setOriginDtfs(convertToCountryName(request.getOriginDtfs()));
        request.setOriginAcns(convertToCountryName(request.getOriginAcns()));
        request.setOriginDomesticVoice(convertToCountryName(request.getOriginDomesticVoice()));

        request.setDestinationItfs(convertToCountryName(request.getDestinationItfs()));
        request.setDestinationLns(convertToCountryName(request.getDestinationLns()));
        request.setDestinationUifn(convertToCountryName(request.getDestinationUifn()));
        request.setDestinationDtfs(convertToCountryName(request.getDestinationDtfs()));
        request.setDestinationAcns(convertToCountryName(request.getDestinationAcns()));
        return request;
    }

    // TODO : Remove this function once Pricing Engine updated with Country Code
    private List<String> convertToCountryName(List<String> countryNames) {
        return countryNames.stream().map(countryName -> {
            GscIsoCountries.GscCountry gscCountry = gscIsoCountries.forCode(countryName);
            Objects.requireNonNull(gscCountry, "No Country Available for this code" + countryName);
            return gscCountry.getName();
        }).collect(Collectors.toList());
    }

    /**
     * Method to populate gsc configuration details
     *
     * @param context
     * @param gscConfigPrices
     * @param quoteGsc
     * @param gscConfig
     */
    private void populateGscConfigDetails(GscPricingFeasibilityContext context,
                                          List<GscConfigurationPriceBean> gscConfigPrices, QuoteGsc quoteGsc, QuoteGscDetail gscConfig,
                                          List<Integer> quoteGscDetailsId) {
        GscConfigurationPriceBean gscConfigPriceBean = getPriceDetails(context, quoteGsc, gscConfig);
        if (quoteGscDetailsId.contains(gscConfig.getId())) {
            Integer index = quoteGscDetailsId.indexOf(gscConfig.getId());
            gscConfigPriceBean.setIndex(index);
        }
        gscConfigPrices.add(gscConfigPriceBean);
        resetContextPrice(context);
    }

    /**
     * Method to get price details
     *
     * @param context
     * @param quoteGsc
     * @param quoteGscDetail
     * @return {@link GscConfigurationPriceBean}
     */
    private GscConfigurationPriceBean getPriceDetails(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                                      QuoteGscDetail quoteGscDetail) {
        GscConfigurationPriceBean gscConfigPriceBean = new GscConfigurationPriceBean();
        this.extractServiceWisePriceDetails(context, quoteGsc, quoteGscDetail, gscConfigPriceBean);
        this.processConfigPrice(context, quoteGscDetail);
        this.setRatePerMinuteAttributes(quoteGsc, quoteGscDetail, context, gscConfigPriceBean);
        this.saveQuoteGscDetail(context, quoteGsc, quoteGscDetail);
        gscConfigPriceBean.setArc(quoteGscDetail.getArc());
        gscConfigPriceBean.setMrc(quoteGscDetail.getMrc());
        gscConfigPriceBean.setNrc(quoteGscDetail.getNrc());
        gscConfigPriceBean.setSrc(quoteGscDetail.getSrc());
        gscConfigPriceBean.setDest(quoteGscDetail.getDest());
        gscConfigPriceBean.setDestType(quoteGscDetail.getDestType());
        gscConfigPriceBean.setSrcType(quoteGscDetail.getSrcType());
        gscConfigPriceBean.setId(quoteGscDetail.getId());
        return gscConfigPriceBean;

    }

    /**
     * Method to extract service wise price details
     *
     * @param context
     * @param quoteGsc
     * @param quoteGscDetail
     */
    private void extractServiceWisePriceDetails(GscPricingFeasibilityContext context, QuoteGsc quoteGsc,
                                                QuoteGscDetail quoteGscDetail, GscConfigurationPriceBean gscConfigPriceBean) {
        context.termName = new ArrayList<>();
        context.phoneType = new ArrayList<>();
        context.termRate = new ArrayList<>();

        switch (quoteGsc.getProductName()) {
            case GscConstants.ITFS: {
                context.pricingResponse.getItfsPrice().stream().forEach(configPrice -> {
                    if (configPrice.getOrigin() != null
                            && configPrice.getOrigin().equalsIgnoreCase(quoteGscDetail.getSrc())) {
                        this.setItfsAndUifnPrices(context, configPrice);
                    }
                });
            }
            break;
            case GscConstants.LNS: {
                context.pricingResponse.getLnsPrice().stream().forEach(configPrice -> {
                    if (configPrice.getOrigin() != null
                            && configPrice.getOrigin().equalsIgnoreCase(quoteGscDetail.getSrc())) {
                        this.setLnsPrices(context, configPrice);
                    }
                });
            }
            break;
            case GscConstants.UIFN: {

                context.pricingResponse.getUifnPrice().stream().forEach(configPrice -> {
                    if (configPrice.getOrigin() != null
                            && configPrice.getOrigin().equalsIgnoreCase(quoteGscDetail.getSrc())) {
                        this.setItfsAndUifnPrices(context, configPrice);
                    }
                });
                if (StringUtils.isNotBlank(context.pricingResponse.getUifnRegCurr())
                        && StringUtils.isNotBlank(context.pricingResponse.getUifnRegCharge())) {
                    context.uifnRegCharge = this.setPrecision(this.convertCurrency(context.pricingResponse.getUifnRegCurr(),
                            context.resultantCurrency, Double.parseDouble(context.pricingResponse.getUifnRegCharge())), 2);
                }
            }
            break;
            case GscConstants.ACANS: {
                if (context.pricingResponse.getAcnsPrice().getOrigin() != null
                        && context.pricingResponse.getAcnsPrice().getOrigin().equalsIgnoreCase(quoteGscDetail.getSrc())) {
                    this.setAcnsAndDtfsPrices(context, context.pricingResponse.getAcnsPrice());
                }
            }
            break;
            case GscConstants.ACDTFS: {
                if (context.pricingResponse.getDtfsPrice().getOrigin() != null
                        && context.pricingResponse.getDtfsPrice().getOrigin().equalsIgnoreCase(quoteGscDetail.getSrc())) {
                    this.setAcnsAndDtfsPrices(context, context.pricingResponse.getDtfsPrice());
                }
            }
            break;
            case GscConstants.DOMESTIC_VOICE: {
                this.setDomesticVoicePrices(context, context.pricingResponse.getDomesticVoicePrice(), quoteGsc);
                DomesticVoicePriceBean domesticVoice = context.pricingResponse.getDomesticVoicePrice();
                domesticVoice.setPrice(context.termRate);
                domesticVoice.setTerminationName(context.termName);
                domesticVoice.setPhoneType(context.phoneType);
                if (domesticVoice.getCurrency().equalsIgnoreCase("0")) {
                    domesticVoice.setCurrency("NA");
                }
                gscConfigPriceBean.setDomesticVoicePrice(domesticVoice);

            }
            break;
            case GscConstants.GLOBAL_OUTBOUND: {
                for (GlobalOutboundPriceBean configPrice : context.pricingResponse.getGlobalOutboundPrice()) {

                    if (Objects.nonNull(configPrice.getDestination())
                            && configPrice.getDestination().equalsIgnoreCase(quoteGscDetail.getDest())) {
                        this.setGlobalOutboundPrices(context, configPrice);

                        configPrice.setTerminationName(context.termName);
                        configPrice.setPhoneType(context.phoneType);
                        if (context.termName.size() == context.termRate.size()) {
                            configPrice.setPrice(context.termRate);
                        }
                        gscConfigPriceBean.getGlobalOutboundPrices().add(configPrice);
                        break;
                    }
                }
                context.globalOutboundRateColumn = context.pricingResponse.getPriceSlab();
                setVolumeCommitmentAndRateColumnAttributes(context);
            }
            break;

            default:
                break;

        }

    }

    /**
     * Replace Name with Not Applicable
     *
     * @param context
     * @return
     */
    private GscPricingFeasibilityContext replaceNameWithNA(GscPricingFeasibilityContext context) {
        List<String> termName = new ArrayList<>();
        List<String> phoneType = new ArrayList<>();
        if (context.termName.contains("0")) {
            context.termName = termName;
        }
        if (context.phoneType.contains("0")) {
            context.phoneType = phoneType;
        }
        return context;
    }

    /**
     * Method to set prices
     *
     * @param context
     * @param configPrice
     */
    private void setItfsAndUifnPrices(GscPricingFeasibilityContext context, ItfsAndUifnPriceBean configPrice) {
        if (!configPrice.getCurrency().equalsIgnoreCase("0")) {
            context.totalARC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getArc()));
            context.totalMRC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getMrc()));
            context.totalNRC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getNrc()));
            context.termName = configPrice.getTerminationName();
            if (configPrice.getPriceFixed() != null && !configPrice.getPriceFixed().isEmpty()) {
                configPrice.getPriceFixed().stream().forEach(rpm -> {
                    context.rpmFixed.add(this.setPrecision(this.convertCurrency(configPrice.getCurrency(),
                            context.resultantCurrency, Double.parseDouble(rpm)), 4));
                });

            }
            if (configPrice.getPriceMobile() != null && !configPrice.getPriceMobile().isEmpty()) {
                configPrice.getPriceMobile().stream().forEach(rpm -> {
                    context.rpmMobile.add(this.setPrecision(this.convertCurrency(configPrice.getCurrency(),
                            context.resultantCurrency, Double.parseDouble(rpm)), 4));
                });
            }
            if (configPrice.getPricePayphone() != null && !configPrice.getPricePayphone().isEmpty()) {
                configPrice.getPricePayphone().stream().forEach(rpm -> {
                    context.rpmSpecial.add(this.setPrecision(this.convertCurrency(configPrice.getCurrency(),
                            context.resultantCurrency, Double.parseDouble(rpm)), 4));
                });
            }

            List<String> surchargeRates = new ArrayList<>();
            surchargeRates = computeSurchargeRates(configPrice.getSurchargeAmount(), configPrice.getSurchargeCurrency(),
                    surchargeRates, context);
            context.surchargeRate = surchargeRates;
            this.replaceNameWithNA(context);
        }
    }

    /**
     * Method to compute surcharge rates
     *
     * @param surchargeAmt
     * @param surchargeCurrency
     * @param surchargeRates
     * @param context
     * @return
     */
    public List<String> computeSurchargeRates(List<String> surchargeAmt, List<String> surchargeCurrency,
                                              List<String> surchargeRates, GscPricingFeasibilityContext context) {
        if (Objects.nonNull(surchargeAmt) && !surchargeAmt.isEmpty() && Objects.nonNull(surchargeCurrency)
                && !surchargeCurrency.isEmpty() && surchargeAmt.size() == surchargeCurrency.size()) {
            for (int i = 0; i < surchargeAmt.size(); i++) {
                if (!surchargeCurrency.get(i).equalsIgnoreCase("0")) {
                    String surchargeRate = surchargeAmt.get(i);
                    surchargeRates
                            .add(this
                                    .setPrecision(this.convertCurrency(surchargeCurrency.get(i),
                                            context.resultantCurrency, Double.parseDouble(surchargeRate)), 4)
                                    .toString());
                } else {
                    surchargeCurrency.set(i, "NA");
                    surchargeRates.add("0");
                }
            }
        }

        return surchargeRates;
    }

    /**
     * Method to set Lns prices
     *
     * @param context
     * @param configPrice
     */
    private void setLnsPrices(GscPricingFeasibilityContext context, LnsPriceBean configPrice) {
        if (!configPrice.getCurrency().equalsIgnoreCase("0")) {
            context.totalARC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getArc()));
            context.totalMRC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getMrc()));
            context.totalNRC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getNrc()));
            context.termName = configPrice.getTerminationName();

            if (configPrice.getPrice() != null && !configPrice.getPrice().isEmpty()) {
                configPrice.getPrice().stream().forEach(rpm -> {
                    rpm = this.setPrecision(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                            Double.parseDouble(rpm)), 4).toString();
                    context.rpmFixed.add(Double.parseDouble(rpm));
                    context.rpmSpecial.add(Double.parseDouble(rpm));
                    context.rpmMobile.add(Double.parseDouble(rpm));
                    context.rpm.add(Double.parseDouble(rpm));
                });
            }

            List<String> surchargeRates = new ArrayList<>();
            surchargeRates = computeSurchargeRates(configPrice.getSurchargeAmount(), configPrice.getSurchargeCurrency(),
                    surchargeRates, context);
            context.surchargeRate = surchargeRates;
            this.replaceNameWithNA(context);
        }
    }

    /**
     * Method to set Acns prices
     *
     * @param context
     * @param configPrice
     */
    private void setAcnsAndDtfsPrices(GscPricingFeasibilityContext context, DtfsAndAcnsPriceBean configPrice) {
        if (!configPrice.getCurrency().equalsIgnoreCase("0")) {
            context.totalARC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getArc()));
            context.totalMRC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getMrc()));
            context.totalNRC = this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getNrc()));
            if (configPrice.getPriceFixed() != null) {
                Double priceFixed = this.setPrecision(this.convertCurrency(configPrice.getCurrency(),
                        context.resultantCurrency, Double.parseDouble(configPrice.getPriceFixed())), 4);
                context.rpmFixed.add(priceFixed);
                if (!configPrice.getPriceFixed().equalsIgnoreCase("0")) {
                    context.rpm.add(priceFixed);
                }
            }
            if (configPrice.getPriceMobile() != null) {
                Double priceMobile = this.setPrecision(this.convertCurrency(configPrice.getCurrency(),
                        context.resultantCurrency, Double.parseDouble(configPrice.getPriceMobile())), 4);
                context.rpmMobile.add(priceMobile);
                if (!configPrice.getPriceMobile().equalsIgnoreCase("0")) {
                    context.rpm.add(priceMobile);
                }
            }
            if (configPrice.getPricePayphone() != null) {
                Double pricePayphone = this.setPrecision(this.convertCurrency(configPrice.getCurrency(),
                        context.resultantCurrency, Double.parseDouble(configPrice.getPricePayphone())), 4);
                context.rpmSpecial.add(pricePayphone);
                if (!configPrice.getPricePayphone().equalsIgnoreCase("0")) {
                    context.rpm.add(pricePayphone);
                }
            }
        }
    }

    /**
     * Method to set Domestic voice prices
     *
     * @param context
     * @param configPrice
     * @param quoteGsc
     */
    private void setDomesticVoicePrices(GscPricingFeasibilityContext context, DomesticVoicePriceBean configPrice, QuoteGsc quoteGsc) {
        if (!configPrice.getCurrency().equalsIgnoreCase("0")) {

            context.termName = configPrice.getTerminationName();
            context.phoneType = configPrice.getPhoneType();

            configPrice.setDidArc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getDidArc())).toString());
            configPrice.setDidMrc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getDidMrc())).toString());
            configPrice.setDidNrc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getDidNrc())).toString());
            configPrice.setChannelArc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getChannelArc())).toString());
            configPrice.setChannelMrc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getChannelMrc())).toString());
            configPrice.setChannelNrc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getChannelNrc())).toString());
            configPrice.setOrderSetupMrc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getOrderSetupMrc())).toString());
            configPrice.setOrderSetupNrc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getOrderSetupNrc())).toString());
            configPrice.setOrderSetupArc(this.convertCurrency(configPrice.getCurrency(), context.resultantCurrency,
                    Double.parseDouble(configPrice.getOrderSetupArc())).toString());

            context.totalARC = Double.parseDouble(configPrice.getDidArc())
                    + Double.parseDouble(configPrice.getChannelArc())
                    + Double.parseDouble(configPrice.getOrderSetupArc());
            context.totalMRC = Double.parseDouble(configPrice.getDidMrc())
                    + Double.parseDouble(configPrice.getChannelMrc())
                    + Double.parseDouble(configPrice.getOrderSetupMrc());
            context.totalNRC = Double.parseDouble(configPrice.getDidNrc())
                    + Double.parseDouble(configPrice.getChannelNrc())
                    + Double.parseDouble(configPrice.getOrderSetupNrc());

            int didConcurrentChannel = getDIDConcurrentChannel(quoteGsc);

            context.totalMRC = context.totalMRC * didConcurrentChannel;
            context.totalNRC = context.totalNRC * didConcurrentChannel;

            List<String> prices = new ArrayList<>();
            if (Objects.nonNull(configPrice.getPrice()) && !configPrice.getPrice().isEmpty()
                    && Objects.nonNull(configPrice.getUsageCurrency()) && !configPrice.getUsageCurrency().isEmpty()) {
                for (int i = 0; i < configPrice.getPrice().size(); i++) {
                    if (!configPrice.getUsageCurrency().get(i).equalsIgnoreCase("0")) {
                        String rpm = configPrice.getPrice().get(i);
                        prices.add(this.setPrecision(this.convertCurrency(configPrice.getUsageCurrency().get(i),
                                context.resultantCurrency, Double.parseDouble(rpm)), 4).toString());
                    } else {
                        configPrice.getUsageCurrency().set(i, "NA");
                    }
                }
            }
            context.termRate = prices;
            configPrice.setPrice(prices);
            this.replaceNameWithNA(context);
        }
    }

    private int getDIDConcurrentChannel(QuoteGsc quoteGsc) {
        Integer quoteGscDetailId = quoteGsc.getQuoteGscDetails().stream().findFirst().get().getId();
        List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameAndStatus(quoteGsc.getAccessType(), GscConstants.STATUS_ACTIVE);
        Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(quoteGscDetailId,
                mstProductComponents.stream().findFirst().get(), GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
                .findByNameAndStatus(GscConstants.COUNT_CONCURRENT_CHANNELS, GscConstants.STATUS_ACTIVE);
        if (!CollectionUtils.isEmpty(productAttributeMasters)) {
            List<QuoteProductComponentsAttributeValue> quoteProductComponentAndProductAttributeMaster = quoteProductComponentsAttributeValueRepository
                    .findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent.get(), productAttributeMasters.stream().findFirst().get());
            if (!CollectionUtils.isEmpty(quoteProductComponentAndProductAttributeMaster)) {
                String value = quoteProductComponentAndProductAttributeMaster.stream().findFirst().get().getAttributeValues();
                if (Objects.nonNull(value) && !value.isEmpty()) {
                    return Integer.valueOf(value);
                }
            }
        }
        return 1;
    }

    /**
     * Method to set global outbound prices
     *
     * @param context
     * @param configPrice
     */
    private void setGlobalOutboundPrices(GscPricingFeasibilityContext context, GlobalOutboundPriceBean configPrice) {

        context.termName = configPrice.getTerminationName();
        context.phoneType = configPrice.getPhoneType();

        List<String> prices = new ArrayList<>();
        if (Objects.nonNull(configPrice.getPrice()) && !configPrice.getPrice().isEmpty()
                && Objects.nonNull(configPrice.getCurrency()) && !configPrice.getCurrency().isEmpty()) {
            for (int i = 0; i < configPrice.getPrice().size(); i++) {
                if (!configPrice.getCurrency().get(i).equalsIgnoreCase("0")) {
                    String rpm = configPrice.getPrice().get(i);
                    prices.add(this.setPrecision(this.convertCurrency(configPrice.getCurrency().get(i),
                            context.resultantCurrency, Double.parseDouble(rpm)), 4).toString());
                } else {
                    configPrice.getCurrency().set(i, "NA");
                }
            }
        }
        context.termRate = prices;
        configPrice.setPrice(prices);
        this.replaceNameWithNA(context);

    }

    /**
     * Method to set rate per minute attributes
     *
     * @param quoteGsc
     * @param gscConfig
     * @param context
     * @param gscConfigurationBean
     */
    private void setRatePerMinuteAttributes(QuoteGsc quoteGsc, QuoteGscDetail gscConfig,
                                            GscPricingFeasibilityContext context, GscConfigurationPriceBean gscConfigurationBean) {
        List<QuoteProductComponent> quoteProductComponents = getQuoteProductComponents(gscConfig);
        if (!quoteProductComponents.isEmpty()) {
            List<String> attributesList = Arrays.asList(GscConstants.RATE_PER_MINUTE_FIXED,
                    GscConstants.RATE_PER_MINUTE_MOBILE, GscConstants.RATE_PER_MINUTE_SPECIAL, GscConstants.TERM_NAME,
                    GscConstants.DID_ARC, GscConstants.DID_MRC, GscConstants.DID_NRC, GscConstants.CHANNEL_ARC,
                    GscConstants.CHANNEL_MRC, GscConstants.CHANNEL_NRC, GscConstants.ORDER_SETUP_ARC,
                    GscConstants.ORDER_SETUP_MRC, GscConstants.ORDER_SETUP_NRC, GscConstants.TERM_RATE,
                    GscConstants.PHONE_TYPE, GscConstants.SURCHARGE_RATE, GscConstants.UIFN_REGISTRATION_CHARGE);
            List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
            quoteProductComponents.stream().forEach(quoteComponent -> {
                attributes.stream().forEach(attribute -> {
                    List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentAttributeValuesRepository
                            .findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute);
                    if (attributeValueList != null && !attributeValueList.isEmpty()) {
                        QuoteProductComponentsAttributeValue attributeValue = attributeValueList.get(0);
                        List<QuoteProductComponentsAttributeValue> attributeValues = processRatePerMinute(context,
                                attributeValue, attribute, gscConfigurationBean, quoteGsc);
                        if (!attributeValues.isEmpty()) {
                            quoteProductComponentAttributeValuesRepository.saveAll(attributeValues);
                        }

                        if (quoteGsc.getProductName().equals(GscConstants.ACANS) || quoteGsc.getProductName().equals(GscConstants.ACDTFS) || quoteGsc.getProductName().equals(GscConstants.LNS)) {
                            if (attributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.RATE_PER_MINUTE_FIXED)
                                    || attributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.RATE_PER_MINUTE_MOBILE)
                                    || attributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.RATE_PER_MINUTE_SPECIAL)) {
                                if (!attributeValues.isEmpty()) {
                                    setRpmFromRpmFixedMobilePayphone(quoteGsc, context, gscConfigurationBean, attribute, attributeValues.get(0), quoteComponent);
                                }
                            }
                        }
                    }
                });
            });

        }
    }

    /**
     *  @param quoteGsc
     * @param context
     * @param gscConfigurationBean
     * @param attribute
     * @param attributeValue
     * @param attributeValues
     * @param quoteComponent
     */
    private void setRpmFromRpmFixedMobilePayphone(QuoteGsc quoteGsc, GscPricingFeasibilityContext context, GscConfigurationPriceBean gscConfigurationBean, ProductAttributeMaster attribute, QuoteProductComponentsAttributeValue attributeValue, QuoteProductComponent quoteComponent) {
        LOGGER.info("Rate per minute Attribute name is {}", attributeValue.getProductAttributeMaster().getName());
        if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.ACDTFS)
                || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.ACANS)
                || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.LNS)) {
            LOGGER.info("Product name is {}", quoteGsc.getProductName());
            List<Double> rates = new ArrayList<>();
            if(attributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.RATE_PER_MINUTE_FIXED) && !(context.rpmFixed.contains(0.0) || context.rpmFixed.contains(0))){
                rates = addSurcharge(context.rpmFixed, context.surchargeRate);
                context.rpmFixed.clear();
                context.rpmFixed.add(0.0);
                attributeValue.setDisplayValue("0.0");
                attributeValue.setAttributeValues("0.0");
                quoteProductComponentsAttributeValueRepository.save(attributeValue);
                setRpm(context, gscConfigurationBean, attributeValue, quoteComponent, rates);
            }
            else if (attributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.RATE_PER_MINUTE_MOBILE) && !(context.rpmMobile.contains(0.0) || context.rpmMobile.contains(0))){
                rates = addSurcharge(context.rpmMobile, context.surchargeRate);
                context.rpmMobile.clear();
                context.rpmMobile.add(0.0);
                attributeValue.setDisplayValue("0.0");
                attributeValue.setAttributeValues("0.0");
                quoteProductComponentsAttributeValueRepository.save(attributeValue);
                setRpm(context, gscConfigurationBean, attributeValue, quoteComponent, rates);
            }
            else if (attributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(GscConstants.RATE_PER_MINUTE_SPECIAL) && !(context.rpmSpecial.contains(0.0) || context.rpmSpecial.contains(0))){
                rates = addSurcharge(context.rpmSpecial, context.surchargeRate);
                context.rpmSpecial.clear();
                context.rpmSpecial.add(0.0);
                attributeValue.setDisplayValue("0.0");
                attributeValue.setAttributeValues("0.0");
                quoteProductComponentsAttributeValueRepository.save(attributeValue);
                setRpm(context, gscConfigurationBean, attributeValue, quoteComponent, rates);
            }

        }
    }

    /**
     * Set rpm
     *
     * @param context
     * @param gscConfigurationBean
     * @param attributeValue
     * @param quoteComponent
     * @param rates
     */
    private void setRpm(GscPricingFeasibilityContext context, GscConfigurationPriceBean gscConfigurationBean, QuoteProductComponentsAttributeValue attributeValue, QuoteProductComponent quoteComponent, List<Double> rates) {
        context.rpm.clear();
        context.rpm.addAll(rates);
        gscConfigurationBean.setRpm(context.rpm);
        deleteAttributeByName(GscConstants.RATE_PER_MINUTE, attributeValue.getQuoteProductComponent());
        ProductAttributeMaster ratePerMinuteProduct = productAttributeMasterRepository.findByName(GscConstants.RATE_PER_MINUTE);
        List<QuoteProductComponentsAttributeValue> rpmList = context.rpm.stream()
                .map(rpm ->{
                    QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
                    quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteComponent);
                    quoteProductComponentsAttributeValue.setProductAttributeMaster(ratePerMinuteProduct);
                    quoteProductComponentsAttributeValue.setDisplayValue(Double.toString(rpm));
                    quoteProductComponentsAttributeValue.setAttributeValues(Double.toString(rpm));
                    return quoteProductComponentsAttributeValue;
                })
                .collect(Collectors.toList());
        if (!rpmList.isEmpty()) {
            quoteProductComponentAttributeValuesRepository.saveAll(rpmList);
        }
    }

    private QuoteProductComponentsAttributeValue copyAttribute(QuoteProductComponentsAttributeValue attribute,
                                                               String attributeValue) {
        QuoteProductComponentsAttributeValue copy = new QuoteProductComponentsAttributeValue();
        copy.setProductAttributeMaster(attribute.getProductAttributeMaster());
        copy.setQuoteProductComponent(attribute.getQuoteProductComponent());
        copy.setAttributeValues(attributeValue);
        copy.setDisplayValue(attributeValue);
        return copy;
    }

    private void deleteAttributeByName(String attributeName, QuoteProductComponent quoteProductComponent) {
        ProductAttributeMaster attributeMaster = productAttributeMasterRepository
                .findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream().findFirst()
                .orElseThrow(() -> new TCLException(ExceptionConstants.COMMON_ERROR,
                        String.format("Attribute with name %s not found", attributeName)));
        List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentAttributeValuesRepository
                .findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, attributeMaster);
        if (!CollectionUtils.isEmpty(attributes)) {
            LOGGER.info(String.format("Deleting pricing attribute name: %s, product component id: %s", attributeName,
                    quoteProductComponent.getId()));
            quoteProductComponentAttributeValuesRepository.deleteAll(attributes);
        } else {
            LOGGER.info(String.format(
                    "No pricing attributes exists for attribute name: %s, product component id: %s, not deleting",
                    attributeName, quoteProductComponent.getId()));
        }
    }

    /**
     * Method to process Rate Per Minute based on attribute name
     *
     * @param attributeValue
     * @param attribute
     * @param gscConfigurationBean
     */

    private List<QuoteProductComponentsAttributeValue> processRatePerMinute(GscPricingFeasibilityContext context,
                                                                            QuoteProductComponentsAttributeValue attributeValue, ProductAttributeMaster attribute,
                                                                            GscConfigurationPriceBean gscConfigurationBean, QuoteGsc quoteGsc) {
        if (attributeValue.getProductAttributeMaster().getId() == attribute.getId()) {
            switch (attribute.getName()) {
                case GscConstants.RATE_PER_MINUTE_FIXED:
                    if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.ITFS)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.UIFN)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.LNS)) {
                        List<Double> fixedRates = addSurcharge(context.rpmFixed, context.surchargeRate);
                        context.rpmFixed.clear();
                        context.rpmFixed.addAll(fixedRates);
                    }
                    gscConfigurationBean.setRpmFixed(context.rpmFixed);
                    if (!CollectionUtils.isEmpty(context.rpmFixed)) {
                        deleteAttributeByName(attribute.getName(), attributeValue.getQuoteProductComponent());
                    }
                    return context.rpmFixed.stream().map(rpm -> copyAttribute(attributeValue, Double.toString(rpm)))
                            .collect(Collectors.toList());
                case GscConstants.RATE_PER_MINUTE_SPECIAL:
                    if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.ITFS)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.UIFN)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.LNS)) {
                        List<Double> specialRates = addSurcharge(context.rpmSpecial, context.surchargeRate);
                        context.rpmSpecial.clear();
                        context.rpmSpecial.addAll(specialRates);
                    }
                    gscConfigurationBean.setRpmSpecial(context.rpmSpecial);
                    if (!CollectionUtils.isEmpty(context.rpmSpecial)) {
                        deleteAttributeByName(attribute.getName(), attributeValue.getQuoteProductComponent());
                    }
                    return context.rpmSpecial.stream().map(rpm -> copyAttribute(attributeValue, Double.toString(rpm)))
                            .collect(Collectors.toList());
                case GscConstants.RATE_PER_MINUTE_MOBILE:
                    if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.ITFS)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.UIFN)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.LNS)) {
                        List<Double> mobileRates = addSurcharge(context.rpmMobile, context.surchargeRate);
                        context.rpmMobile.clear();
                        context.rpmMobile.addAll(mobileRates);
                    }
                    gscConfigurationBean.setRpmMobile(context.rpmMobile);
                    if (!CollectionUtils.isEmpty(context.rpmMobile)) {
                        deleteAttributeByName(attribute.getName(), attributeValue.getQuoteProductComponent());
                    }
                    return context.rpmMobile.stream().map(rpm -> copyAttribute(attributeValue, Double.toString(rpm)))
                            .collect(Collectors.toList());
                case GscConstants.TERM_NAME:
                    gscConfigurationBean.setTermName(context.termName);
                    if (!CollectionUtils.isEmpty(context.termName)) {
                        deleteAttributeByName(attribute.getName(), attributeValue.getQuoteProductComponent());
                    }
                    return context.termName.stream().map(name -> copyAttribute(attributeValue, name))
                            .collect(Collectors.toList());
                case GscConstants.TERM_RATE:
                    if (!CollectionUtils.isEmpty(context.termRate)) {
                        deleteAttributeByName(attribute.getName(), attributeValue.getQuoteProductComponent());
                    }
                    return context.termRate.stream().map(rate -> copyAttribute(attributeValue, rate))
                            .collect(Collectors.toList());
                case GscConstants.PHONE_TYPE:
                    if (!CollectionUtils.isEmpty(context.phoneType)) {
                        deleteAttributeByName(attribute.getName(), attributeValue.getQuoteProductComponent());
                    }
                    return context.phoneType.stream().map(type -> copyAttribute(attributeValue, type))
                            .collect(Collectors.toList());
                case GscConstants.DID_ARC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getDidArc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.DID_MRC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getDidMrc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.DID_NRC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getDidNrc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.CHANNEL_ARC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getChannelArc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.CHANNEL_MRC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getChannelMrc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.CHANNEL_NRC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getChannelNrc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.ORDER_SETUP_ARC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getOrderSetupArc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.ORDER_SETUP_MRC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getOrderSetupMrc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.ORDER_SETUP_NRC:

                    attributeValue.setAttributeValues(gscConfigurationBean.getDomesticVoicePrice().getOrderSetupNrc());
                    return ImmutableList.of(attributeValue);
                case GscConstants.SURCHARGE_RATE:
                    if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.ITFS)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.UIFN)
                            || quoteGsc.getProductName().equalsIgnoreCase(GscConstants.LNS)) {
                        if (Objects.nonNull(context.surchargeRate)) {
                            if (!CollectionUtils.isEmpty(context.surchargeRate)) {
                                deleteAttributeByName(attribute.getName(), attributeValue.getQuoteProductComponent());
                            }
                            return context.surchargeRate.stream().map(name -> copyAttribute(attributeValue, name))
                                    .collect(Collectors.toList());
                        }
                    }
                    break;
                case GscConstants.UIFN_REGISTRATION_CHARGE:
                    if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.UIFN)) {
                        attributeValue.setAttributeValues(context.uifnRegCharge.toString());
                        return ImmutableList.of(attributeValue);
                    }
                    break;
                default:
                    LOGGER.info("other attribute Name-" + attribute.getName());
                    return ImmutableList.of(attributeValue);

            }
        }
        return ImmutableList.of(attributeValue);
    }

    /**
     * Method to add surcharge
     *
     * @param rates
     * @param surcharges
     * @return
     */
    private List<Double> addSurcharge(List<Double> rates, List<String> surcharges) {
        List<Double> finalRates = new ArrayList<>();

        if (Objects.nonNull(surcharges) && Objects.nonNull(rates) && rates.size() == surcharges.size()) {

            for (int i = 0; i < rates.size(); i++) {
                if (rates.get(i) != 0) {
                    Double value = rates.get(i) + Double.parseDouble(surcharges.get(i));
                    finalRates.add(setPrecision(value, 4));
                } else
                    finalRates.add(setPrecision(rates.get(i), 4));
            }
        } else {
            finalRates.addAll(rates);
        }

        return finalRates;
    }

    /**
     * Method to process configuration prices
     *
     * @param context
     * @param quoteGscDetail
     */
    private void processConfigPrice(GscPricingFeasibilityContext context, QuoteGscDetail quoteGscDetail) {
        List<QuotePrice> attributePrices = getConfigQuotePrice(quoteGscDetail);
        QuotePrice price = null;
        if (!attributePrices.isEmpty())
            price = attributePrices.get(0);
        saveQuotePrice(context, quoteGscDetail, price);
    }

    /**
     * Method to get configuration quote price
     *
     * @param quoteGscDetail
     * @return {@link QuotePrice}
     */
    private List<QuotePrice> getConfigQuotePrice(QuoteGscDetail quoteGscDetail) {

        return quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(),
                String.valueOf(quoteGscDetail.getId()));

    }

    /**
     * Method to save quote price
     *
     * @param context
     * @param quoteGscDetail
     * @param attributePrice
     */
    private void saveQuotePrice(GscPricingFeasibilityContext context, QuoteGscDetail quoteGscDetail,
                                QuotePrice attributePrice) {
        if (Objects.nonNull(attributePrice)) {
            attributePrice.setEffectiveMrc(context.totalMRC);
            attributePrice.setEffectiveNrc(context.totalNRC);
            attributePrice.setEffectiveArc(context.totalARC);
            quotePriceRepository.save(attributePrice);
        } else {
            processNewQuotePrice(context, quoteGscDetail);
        }

    }

    /**
     * Method to process new quote price
     *
     * @param context
     * @param quoteGscDetail
     */
    private void processNewQuotePrice(GscPricingFeasibilityContext context, QuoteGscDetail quoteGscDetail) {
        QuotePrice attributePrice = new QuotePrice();
        attributePrice.setQuoteId(context.quoteToLe.getQuote().getId());
        attributePrice.setReferenceId(String.valueOf(quoteGscDetail.getId()));
        attributePrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
        attributePrice.setEffectiveMrc(context.totalMRC);
        attributePrice.setEffectiveNrc(context.totalNRC);
        attributePrice.setEffectiveArc(context.totalARC);
        attributePrice.setMstProductFamily(
                quoteGscDetail.getQuoteGsc().getProductSolution().getQuoteToLeProductFamily().getMstProductFamily());
        quotePriceRepository.save(attributePrice);
    }

    /**
     * Method to process customer data
     *
     * @param customerId
     * @return
     * @throws TclCommonException
     */
    public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
        String customerResponse = null;
        CustomerDetailsBean detailsBean = null;
        if (customerId != 0) {
            LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
            if (StringUtils.isNotBlank(customerResponse)) {
                detailsBean = (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse,
                        CustomerDetailsBean.class);
            }
        }
        return detailsBean;

    }

    /**
     * persistPricingDetails
     *
     * @param response
     * @param type
     * @param quote
     * @throws TclCommonException
     */
    private void persistResponsePricingDetails(PricingResponse response, String type, Quote quote)
            throws TclCommonException {
        PricingEngineResponse pricingDetail = new PricingEngineResponse();
        pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
        pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
        pricingDetail.setPricingType(type);
        pricingDetail.setResponseData(Utils.convertObjectToJson(response));
        pricingDetail.setSiteCode(quote.getQuoteCode());
        pricingDetailsRepository.save(pricingDetail);
    }

    /**
     * persistRequestPricingDetails
     *
     * @param request
     * @param type
     * @param quote
     * @throws TclCommonException
     */
    private void persistRequestPricingDetails(PricingRequest request, String type, Quote quote)
            throws TclCommonException {
        PricingEngineResponse pricingDetail = new PricingEngineResponse();
        pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
        pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
        pricingDetail.setPricingType(type);
        pricingDetail.setRequestData(Utils.convertObjectToJson(request));
        pricingDetail.setSiteCode(quote.getQuoteCode());
        pricingDetailsRepository.save(pricingDetail);
    }

    /**
     * Method to convert currency
     *
     * @param existingCurrency
     * @param value
     * @return
     */
    private Double convertCurrency(String existingCurrency, String resultantCurrency, Double value) {
        return omsUtilService.convertCurrency(existingCurrency, resultantCurrency, value);
    }

    /**
     * Method to convert gsc prices
     *
     * @param context
     * @return {@link GscPricingFeasibilityContext}
     */
    private GscPricingFeasibilityContext convertGscPrices(GscPricingFeasibilityContext context) {
        List<GscQuotePriceBean> gscQuotePrices = new ArrayList<>();

        if (!context.existingCurrency.equalsIgnoreCase(context.resultantCurrency)) {
            context.gscQuotePricingBean.getGscQuotePrices().stream().forEach(gscQuotePrice -> {
                List<GscConfigurationPriceBean> gscConfigurationPrices = new ArrayList<>();
                gscQuotePrice.getGscConfigPrices().stream().forEach(gscConfigPrice -> {
                    if (gscConfigPrice.getDomesticVoicePrice() != null) {
                        this.convertDomesticVoicePrices(context, gscConfigPrice);
                    }
                    gscConfigurationPrices.add(gscConfigPrice);

                });
                gscQuotePrice.setGscConfigPrices(gscConfigurationPrices);
                gscQuotePrices.add(gscQuotePrice);
            });
            context.gscQuotePricingBean.setGscQuotePrices(gscQuotePrices);

        }
        context.gscQuotePricingBean.setUifnRegistrationCharge(context.uifnRegCharge);
        return context;
    }

    /**
     * Method to convert domestic voice prices
     *
     * @param context
     * @param gscConfigPrice
     */
    private void convertDomesticVoicePrices(GscPricingFeasibilityContext context,
                                            GscConfigurationPriceBean gscConfigPrice) {

        List<String> usageCurrencies = new ArrayList<>();
        gscConfigPrice.getDomesticVoicePrice().getUsageCurrency().stream().forEach(currency -> {
            usageCurrencies.add(context.resultantCurrency);
        });
        gscConfigPrice.getDomesticVoicePrice().setUsageCurrency(usageCurrencies);
    }

    /**
     * Method to convert global outbound prices
     *
     * @param context
     * @param gscConfigPrice
     */
    private void convertGlobalOutboundPrices(GscPricingFeasibilityContext context,
                                             GscConfigurationPriceBean gscConfigPrice) {

        List<GlobalOutboundPriceBean> globalPrices = new ArrayList<>();
        gscConfigPrice.getGlobalOutboundPrices().stream().forEach(globalpriceObj -> {
            List<String> prices = new ArrayList<>();
            globalpriceObj.getPrice().stream().forEach(price -> {
                if (Objects.isNull(price) || price.equalsIgnoreCase("NA"))
                    price = "0";
                Double value = this.setPrecision(Double.parseDouble(price), 4);
                prices.add(String.format("%.4f", value));
            });
            globalpriceObj.setPrice(prices);
            globalPrices.add(globalpriceObj);
        });
        gscConfigPrice.setGlobalOutboundPrices(globalPrices);

    }

    /**
     * saves new price values
     *
     * @param context
     * @return
     */
    private ManualPricingContext triggerManualPricing(ManualPricingContext context) {
        updatePriceForDomesticVoice(context);
        context.changeInMrc = context.quoteGscDetail.getMrc() - context.pricingRequest.getMrc();
        context.changeInNrc = context.quoteGscDetail.getNrc() - context.pricingRequest.getNrc();
        context.changeInArc = context.quoteGscDetail.getArc() - context.pricingRequest.getArc();
        saveQuoteGscDetail(context);
        saveQuoteGsc(context.quoteGsc.getArc(), context.quoteGsc.getMrc(), context.quoteGsc.getNrc(),
                context.quoteGsc.getTcv(), context);
        saveQuoteToLe(context.quoteToLe.getFinalArc(), context.quoteToLe.getFinalMrc(), context.quoteToLe.getFinalNrc(),
                context.quoteToLe.getTotalTcv(), context);
        return context;
    }

    private void updatePriceForDomesticVoice(ManualPricingContext context) {
        if (DOMESTIC_VOICE.equalsIgnoreCase(context.quoteGsc.getProductName())) {
            int didConcurrentChannel = getDIDConcurrentChannel(context.quoteGsc);
            context.pricingRequest.setMrc(didConcurrentChannel * context.pricingRequest.getMrc());
            context.pricingRequest.setNrc(didConcurrentChannel * context.pricingRequest.getNrc());
            context.pricingRequest.setArc(didConcurrentChannel * context.pricingRequest.getArc());
        }
    }

    /**
     * save quoteGsc
     *
     * @param arc
     * @param mrc
     * @param nrc
     * @param tcv
     * @param context
     */
    private void saveQuoteGsc(Double arc, Double mrc, Double nrc, Double tcv, ManualPricingContext context) {
        QuoteGsc quoteGsc = context.quoteGsc;
        double newArc = calculateNewValue(context.changeInArc, arc);
        double newMrc = calculateNewValue(context.changeInMrc, mrc);
        double newNrc = calculateNewValue(context.changeInNrc, nrc);
        quoteGsc.setArc(newArc);
        quoteGsc.setMrc(newMrc);
        quoteGsc.setNrc(newNrc);
        quoteGsc.setTcv((context.termInMonths * newMrc) + newNrc);
        LOGGER.info("Total values saved Quote Gsc: termMonths {}, arc {}, mrc {}, nrc {}", context.termInMonths, newArc, newMrc, newNrc);
        quoteGscRepository.save(quoteGsc);
    }

    private Double calculateNewValue(double change, Double value) {
        if (change < 0) {
            return (value + Math.abs(change));
        } else {
            return (value - Math.abs(change));
        }
    }

    /**
     * save quoteToLe
     *
     * @param arc
     * @param mrc
     * @param nrc
     * @param tcv
     * @param context
     */
    private void saveQuoteToLe(Double arc, Double mrc, Double nrc, Double tcv, ManualPricingContext context) {
        double newArc = calculateNewValue(context.changeInArc, arc);
        double newMrc = calculateNewValue(context.changeInMrc, mrc);
        double newNrc = calculateNewValue(context.changeInNrc, nrc);
        QuoteToLe quoteToLe = context.quoteToLe;
        quoteToLe.setProposedArc(newArc);
        quoteToLe.setProposedMrc(newMrc);
        quoteToLe.setProposedNrc(newNrc);
        quoteToLe.setFinalArc(newArc);
        quoteToLe.setFinalMrc(newMrc);
        quoteToLe.setFinalNrc(newNrc);
        quoteToLe.setTotalTcv((context.termInMonths * newMrc) + newNrc);
        LOGGER.info("Total values saved Quotetole Gsc: termMonths {}, arc {}, mrc {}, nrc {}", context.termInMonths, newArc, newMrc, newNrc);
        quoteToLeRepository.save(quoteToLe);
    }

    /**
     * save quoteGscDetail
     *
     * @param context
     */
    private void saveQuoteGscDetail(ManualPricingContext context) {
        QuoteGscDetail quoteGscDetail = context.quoteGscDetail;
        quoteGscDetail.setArc(context.pricingRequest.getArc());
        quoteGscDetail.setMrc(context.pricingRequest.getMrc());
        quoteGscDetail.setNrc(context.pricingRequest.getNrc());
        quoteGscDetailRepository.save(quoteGscDetail);
    }

    /**
     * update quote price manually
     *
     * @param quoteId
     * @param quoteLeId
     * @param configurationId
     * @param pricingRequest
     * @return
     * @author VISHESH AWASTHI
     */
    public GscManualPricing processManualPricing(Integer quoteId, Integer quoteLeId, Integer configurationId,
                                                 GscManualPricing pricingRequest) {
        Objects.requireNonNull(quoteId);
        Objects.requireNonNull(quoteLeId);
        Objects.requireNonNull(configurationId);
        Objects.requireNonNull(pricingRequest);
        createManualPricingContext(quoteId, quoteLeId, configurationId, pricingRequest)
                .map(this::triggerManualPricing);
        return pricingRequest;
    }

    /**
     * creates context for manual price update
     *
     * @param quoteId
     * @param quoteLeId
     * @param configurationId
     * @param pricingRequest
     * @return
     */
    private Try<ManualPricingContext> createManualPricingContext(Integer quoteId, Integer quoteLeId,
                                                                 Integer configurationId, GscManualPricing pricingRequest) {
        ManualPricingContext context = new ManualPricingContext();
        context.quoteToLe = getQuoteToLe(quoteLeId).get();
        context.quoteGscDetail = getQuoteGscDetail(configurationId).get();
        context.quoteGsc = getQuoteGsc(context.quoteGscDetail.getQuoteGsc().getId()).get();
        context.pricingRequest = pricingRequest;
        if (getQuoteToLe(quoteLeId).get().getTermInMonths() != null) {
            context.termInMonths = Integer.valueOf(getQuoteToLe(quoteLeId).get().getTermInMonths().split(" ")[0]);
        }
        LOGGER.info("Terms in months createManualPricingContext Gsc {}", context.termInMonths);
        return Try.success(context);
    }

    /**
     * return QuoteToLe
     *
     * @param quoteLeId
     * @return
     */
    private Try<QuoteToLe> getQuoteToLe(Integer quoteLeId) {
        return quoteToLeRepository.findById(quoteLeId).map(Try::success).orElse(notFoundError(
                ExceptionConstants.QUOTE_EMPTY, String.format("QuoteToLe with quoteToId:%s not found", quoteLeId)));
    }

    /**
     * return QuoteGscDetail
     *
     * @param configurationId
     * @return
     */
    private Try<QuoteGscDetail> getQuoteGscDetail(Integer configurationId) {
        return quoteGscDetailRepository.findById(configurationId).map(Try::success)
                .orElse(notFoundError(ExceptionConstants.QUOTE_GSC_EMPTY,
                        String.format("GscQuoteDetails not found with id:%s", configurationId)));
    }

    /**
     * return QuoteGsc
     *
     * @param quoteGscId
     * @return
     */
    private Try<QuoteGsc> getQuoteGsc(Integer quoteGscId) {
        return quoteGscRepository.findById(quoteGscId).map(Try::success).orElse(notFoundError(
                ExceptionConstants.QUOTE_GSC_EMPTY, String.format("GscQuote not found with id:%s", quoteGscId)));
    }

    /**
     * Process price for given quote legal ID
     *
     * @param quoteLeId
     * @return {@link GscQuotePricingBean}
     */
    @Transactional
    public String processPricingWithMpls(Integer quoteLeId, Boolean contractTermUpdate) {
        Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);
        return Try.success(this.createContext(quoteLeId, null, contractTermUpdate))
                .flatMap(this::getQuoteLe)
                .flatMap(this::getGscs)
                .map(this::updateQuoteToLeWithMplsPrice)
                .map(context -> context.message).get();
    }

    /**
     * Method to compute quoteLe prices and update in quoteLe table
     *
     * @param context
     * @return GscPricingFeasibilityContext
     */
    private GscPricingFeasibilityContext updateQuoteToLeWithMplsPrice(GscPricingFeasibilityContext context) {
        resetContextPrice(context);
        context.gvpnAction = GscConstants.GET;
        this.setGvpnPriceAttributes(context);
        context.pricingRequest = new PricingRequest();
        if ((context.gvpnTotalArc == 0 && context.gvpnTotalMrc == 0 && context.gvpnTotalNrc == 0
                && context.gvpnTotalTcv == 0) || context.contractTermUpdate == true) {
            this.computeGscQuoteLePrices(context);
            context.gvpnAction = GscConstants.SET;
            this.setGvpnPriceAttributes(context);
            /* this.setTermInMonths(context, context.quoteToLe); */
            context.pricingRequest.setOpportunityTerm(
                    String.valueOf(getMonthsforOpportunityTerms(context.quoteToLe.getTermInMonths())));
            context.quoteToLe.setProposedMrc(context.totalMRC + context.gvpnTotalMrc);
            context.quoteToLe.setProposedNrc(context.totalNRC + context.gvpnTotalNrc);
            context.quoteToLe.setProposedArc(context.totalARC + context.gvpnTotalArc);
            Double contractTerm = Double.parseDouble(context.pricingRequest.getOpportunityTerm());
            Double gscTcv = (contractTerm * context.totalMRC) + context.totalNRC;
            Double gvpnTcv = (contractTerm * context.gvpnTotalMrc) + context.gvpnTotalNrc;
            context.quoteToLe.setTotalTcv(gscTcv + gvpnTcv);
            context.quoteToLe.setFinalMrc(context.totalMRC + context.gvpnTotalMrc);
            context.quoteToLe.setFinalNrc(context.totalNRC + context.gvpnTotalNrc);
            context.quoteToLe.setFinalArc(context.totalARC + context.gvpnTotalArc);
            quoteToLeRepository.save(context.quoteToLe);
            context.gscQuotePricingBean.setTotalMRC(context.quoteToLe.getFinalMrc());
            context.gscQuotePricingBean.setTotalNRC(context.quoteToLe.getFinalNrc());
            context.gscQuotePricingBean.setTotalTCV(context.quoteToLe.getTotalTcv());
        }
        context.message = "Pricing is updated with Gsc Service and MPLS Service";
        return context;
    }

    /**
     * Method to persist Gvpn prices with GSC
     *
     * @param quoteToLe
     */
    public void persistGvpnPricesWithGsc(QuoteToLe quoteToLe) {
        try {
            GscPricingFeasibilityContext context = new GscPricingFeasibilityContext();
            context.quoteToLe = quoteToLe;
            context.gvpnAction = GscConstants.SET;
            setGvpnPriceAttributes(context);
            LOGGER.info("GVPN prices are updated in GVPN attributes for GSC");
        } catch (Exception e) {
            LOGGER.warn("Error in updating the GVPN attributes of GSC" + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Method to compute QuoteLe Prices
     *
     * @param context
     * @return
     */
    private GscPricingFeasibilityContext computeGscQuoteLePrices(GscPricingFeasibilityContext context) {
        context.quoteGscs.stream().forEach(quoteGsc -> {

            if (GscConstants.UIFN.equalsIgnoreCase(quoteGsc.getProductName())) {
                if (!quoteGsc.getQuoteGscDetails().isEmpty()) {
                    QuoteGscDetail quoteGscDetail = quoteGsc.getQuoteGscDetails().stream().findFirst().get();
                    List<String> uifnCharge = getOrSetComponentAttributeValue(quoteGscDetail.getId(),
                            GscConstants.UIFN_REGISTRATION_CHARGE, GscConstants.UIFN, true, new ArrayList<>());
                    if (uifnCharge.stream().findFirst().isPresent()) {
                        Double charge = Double.parseDouble(uifnCharge.stream().findFirst().get());
                        charge = Utils.setPrecision(
                                this.convertCurrency(context.existingCurrency, context.resultantCurrency, charge), 2);
                        List<String> chargeToBeSet = new ArrayList<>();
                        chargeToBeSet.add(charge.toString());
                        for (QuoteGscDetail gscDetail : quoteGsc.getQuoteGscDetails()) {
                            getOrSetComponentAttributeValue(gscDetail.getId(), GscConstants.UIFN_REGISTRATION_CHARGE,
                                    GscConstants.UIFN, false, chargeToBeSet);
                        }
                        Double nrc = quoteGsc.getNrc() + charge;
                        quoteGsc.setNrc(Utils.setPrecision(nrc, 2));
                        Double contractTerm = Double.parseDouble(context.pricingRequest.getOpportunityTerm());
                        Double tcv = (contractTerm * quoteGsc.getMrc()) + quoteGsc.getNrc();
                        quoteGsc.setTcv(this.setPrecision(tcv, 2));
                        quoteGscRepository.save(quoteGsc);
                    }
                }
            }

            context.totalMRC = context.totalMRC + (quoteGsc.getMrc() != null ? quoteGsc.getMrc() : 0D);
            context.totalNRC = context.totalNRC + (quoteGsc.getNrc() != null ? quoteGsc.getNrc() : 0D);
            context.totalARC = context.totalARC + (quoteGsc.getArc() != null ? quoteGsc.getArc() : 0D);

        });

        return context;
    }

    /**
     * Method to get quoteProduct components
     *
     * @param quoteToLe
     * @return {@link List<QuoteProductComponent>}
     */
    private List<QuoteProductComponent> getGsipCommonQuoteProductComponents(QuoteToLe quoteToLe) {
        return quoteProductComponentRepository.findByReferenceIdAndType(quoteToLe.getQuote().getId(),
                GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE);
    }

    /**
     * Method to set GVPN Prices attributes
     *
     * @param context
     */
    private void setGvpnPriceAttributes(GscPricingFeasibilityContext context) {
        List<QuoteProductComponent> quoteProductComponents = getGsipCommonQuoteProductComponents(context.quoteToLe);
        if (!quoteProductComponents.isEmpty()) {
            List<String> attributesList = Arrays.asList(GscConstants.GVPN_TOTAL_ARC, GscConstants.GVPN_TOTAL_MRC,
                    GscConstants.GVPN_TOTAL_NRC, GscConstants.GVPN_TOTAL_TCV);
            List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
            quoteProductComponents.forEach(quoteComponent -> {
                attributes.forEach(attribute -> {
                    List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentAttributeValuesRepository
                            .findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute);
                    if (attributeValueList != null && !attributeValueList.isEmpty()) {
                        QuoteProductComponentsAttributeValue attributeValue = attributeValueList.get(0);
                        processGvpnPricesAndVolumeCommitment(context, attributeValue, attribute);
                        quoteProductComponentAttributeValuesRepository.save(attributeValue);
                    }
                });
            });

        }
    }

    /**
     * Method to process Gvpn Prices
     *
     * @param attributeValue
     * @param attribute
     */

    private void processGvpnPricesAndVolumeCommitment(GscPricingFeasibilityContext context,
                                                      QuoteProductComponentsAttributeValue attributeValue, ProductAttributeMaster attribute) {
        if (attributeValue.getProductAttributeMaster().getId() == attribute.getId()) {
            switch (attribute.getName()) {
                case GscConstants.GVPN_TOTAL_ARC:
                    if (context.gvpnAction.equalsIgnoreCase(GscConstants.SET)) {

                        Double totalARC = 0.0;
                        if (context.quoteToLe.getFinalArc() != null) {
                            totalARC = context.quoteToLe.getFinalArc();
                        }

                        context.gvpnTotalArc = totalARC;
                        attributeValue.setAttributeValues(totalARC.toString());
                    } else
                        context.gvpnTotalArc = Double.parseDouble(attributeValue.getAttributeValues());
                    break;
                case GscConstants.GVPN_TOTAL_MRC:
                    if (context.gvpnAction.equalsIgnoreCase(GscConstants.SET)) {
                        Double totalMRC = 0.0;
                        if (context.quoteToLe.getFinalMrc() != null) {
                            totalMRC = context.quoteToLe.getFinalMrc();
                        }

                        context.gvpnTotalMrc = totalMRC;
                        attributeValue.setAttributeValues(totalMRC.toString());
                    } else
                        context.gvpnTotalMrc = Double.parseDouble(attributeValue.getAttributeValues());

                    break;
                case GscConstants.GVPN_TOTAL_NRC:
                    if (context.gvpnAction.equalsIgnoreCase(GscConstants.SET)) {
                        Double totalNRC = 0.0;
                        if (context.quoteToLe.getFinalNrc() != null) {
                            totalNRC = context.quoteToLe.getFinalNrc();
                        }
                        context.gvpnTotalNrc = totalNRC;
                        attributeValue.setAttributeValues(totalNRC.toString());
                    } else
                        context.gvpnTotalNrc = Double.parseDouble(attributeValue.getAttributeValues());

                    break;
                case GscConstants.GVPN_TOTAL_TCV:
                    if (context.gvpnAction.equalsIgnoreCase(GscConstants.SET)) {
                        Double totalTCV = 0.0;
                        if (context.quoteToLe.getTotalTcv() != null) {
                            totalTCV = context.quoteToLe.getTotalTcv();
                        }
                        context.gvpnTotalTcv = totalTCV;
                        attributeValue.setAttributeValues(totalTCV.toString());
                    } else
                        context.gvpnTotalTcv = Double.parseDouble(attributeValue.getAttributeValues());
                    break;
                case GscConstants.INBOUND_VOLUME:
                    if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
                        context.inboundVolume = Double.parseDouble(attributeValue.getAttributeValues());
                    }
                    break;
                case GscConstants.OUTBOUND_VOLUME:
                    if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
                        context.outboundVolume = Double.parseDouble(attributeValue.getAttributeValues());
                    }
                    break;
                case GscConstants.GLOBAL_OUTBOUND_DYNAMIC_COLUMN:
                    if (Objects.nonNull(context.globalOutboundRateColumn)) {
                        attributeValue.setAttributeValues(context.globalOutboundRateColumn);
                    }
                    break;
                default:
                    LOGGER.info("other attribute Name-" + attribute.getName());

            }
        }
    }

    /**
     * Method to get inbound and outbound countries
     *
     * @param context
     */
    private void getInboundAndOutboundCountries(GscPricingFeasibilityContext context) {
        List<QuoteProductComponent> quoteProductComponents = getGsipCommonQuoteProductComponents(context.quoteToLe);
        if (!quoteProductComponents.isEmpty()) {
            List<String> attributesList = Arrays.asList(GscConstants.INBOUND_COUNTRIES,
                    GscConstants.OUTBOUND_COUNTRIES);
            List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
            quoteProductComponents.stream().forEach(quoteComponent -> {
                attributes.stream().forEach(attribute -> {
                    List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentAttributeValuesRepository
                            .findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute);
                    if (attributeValueList != null) {
                        processInboundAndOutboundCountries(context, attribute, attributeValueList);
                    } else {
                        context.pricingRequest.getInboundSelectCust().add("NA");
                        context.pricingRequest.getOutboundSelectCust().add("NA");
                    }
                });
            });

        } else {
            context.pricingRequest.getInboundSelectCust().add("NA");
            context.pricingRequest.getOutboundSelectCust().add("NA");
        }
    }

    private void processInboundAndOutboundCountries(GscPricingFeasibilityContext context,
                                                    ProductAttributeMaster attribute, List<QuoteProductComponentsAttributeValue> attributes) {
        switch (attribute.getName()) {
            case GscConstants.INBOUND_COUNTRIES: {
                List<String> inboundCountries = new ArrayList<>();
                if (attributes.isEmpty()) {
                    inboundCountries.add("NA");
                } else {
                    attributes.stream().forEach(attributeVal -> {
                        inboundCountries.add(attributeVal.getAttributeValues());
                    });
                }
                context.pricingRequest.getInboundSelectCust().addAll(inboundCountries);
            }
            break;

            case GscConstants.OUTBOUND_COUNTRIES: {
                List<String> outboundCountries = new ArrayList<>();
                if (attributes.isEmpty()) {
                    outboundCountries.add("NA");
                } else {
                    attributes.stream().forEach(attributeVal -> {
                        outboundCountries.add(attributeVal.getAttributeValues());
                    });
                }
                context.pricingRequest.getOutboundSelectCust().addAll(outboundCountries);
            }
            break;
            default:
                LOGGER.info("Invalid attribute name");
        }
    }

    /**
     * Method to set volume commitment attributes
     *
     * @param context
     */
    private void setVolumeCommitmentAndRateColumnAttributes(GscPricingFeasibilityContext context) {
        List<QuoteProductComponent> quoteProductComponents = getGsipCommonQuoteProductComponents(context.quoteToLe);
        if (!quoteProductComponents.isEmpty()) {
            List<String> attributesList = Arrays.asList(GscConstants.INBOUND_VOLUME, GscConstants.OUTBOUND_VOLUME,
                    GscConstants.GLOBAL_OUTBOUND_DYNAMIC_COLUMN);
            List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);
            quoteProductComponents.stream().forEach(quoteComponent -> {
                attributes.stream().forEach(attribute -> {
                    List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentAttributeValuesRepository
                            .findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute);
                    if (attributeValueList != null && !attributeValueList.isEmpty()) {
                        QuoteProductComponentsAttributeValue attributeValue = attributeValueList.get(0);
                        processGvpnPricesAndVolumeCommitment(context, attributeValue, attribute);
                        quoteProductComponentAttributeValuesRepository.save(attributeValue);
                    }
                });
            });

        }
    }

    /**
     * Process price for given quote legal ID
     *
     * @param quoteLeId
     * @return {@link GscQuotePricingBean}
     */
    @Transactional
    public GscQuotePricingBean processPricingSummary(Integer quoteLeId) {
        Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);
        return Try.success(this.createContext(quoteLeId, null, false))
                .flatMap(this::getQuoteLe)
                .flatMap(this::getGscs)
                .map(this::getGscPricesFromDB)
                .map(context -> context.gscQuotePricingBean).get();
    }

    /**
     * Method to convert gsc prices
     *
     * @param context
     * @return {@link GscPricingFeasibilityContext}
     */
    private GscPricingFeasibilityContext getGscPricesFromDB(GscPricingFeasibilityContext context) {
        List<GscQuotePriceBean> gscQuotePrices = new ArrayList<>();
        context.uifnRegCharge = 0.0;
        resetContextPrice(context);
        context.gvpnAction = GscConstants.GET;
        setGvpnPriceAttributes(context);

        context.quoteGscs.forEach(quoteGsc -> {
            GscQuotePriceBean gscQuotePrice = new GscQuotePriceBean();
            gscQuotePrice = getGscPrice(context, gscQuotePrice, quoteGsc);
            context.totalMRC = context.totalMRC
                    + (Objects.nonNull(gscQuotePrice.getMrc()) ? gscQuotePrice.getMrc() : 0D);
            context.totalNRC = context.totalNRC
                    + (Objects.nonNull(gscQuotePrice.getNrc()) ? gscQuotePrice.getNrc() : 0D);
            context.totalARC = context.totalARC
                    + (Objects.nonNull(gscQuotePrice.getArc()) ? gscQuotePrice.getArc() : 0D);
            gscQuotePrices.add(gscQuotePrice);
        });


        Integer contractTerm = Integer.valueOf(context.quoteToLe.getTermInMonths().replace("months", "").trim());

        if (context.gvpnTotalArc == 0 && context.gvpnTotalMrc == 0 && context.gvpnTotalNrc == 0
                && context.gvpnTotalTcv == 0) {
            context.quoteToLe.setProposedMrc(context.totalMRC);
            context.quoteToLe.setProposedNrc(context.totalNRC);
            context.quoteToLe.setProposedArc(context.totalARC);
            context.quoteToLe.setFinalMrc(context.totalMRC);
            context.quoteToLe.setFinalNrc(context.totalNRC);
            context.quoteToLe.setFinalArc(context.totalARC);
            Double tcv = (contractTerm * this.setPrecision(context.quoteToLe.getFinalMrc(), 2))
                    + this.setPrecision(context.quoteToLe.getFinalNrc(), 2);
            context.quoteToLe.setTotalTcv(tcv);
        } else {
            context.quoteToLe.setProposedMrc(context.totalMRC + context.gvpnTotalMrc);
            context.quoteToLe.setProposedNrc(context.totalNRC + context.gvpnTotalNrc);
            context.quoteToLe.setProposedArc(context.totalARC + context.gvpnTotalArc);
            context.quoteToLe.setFinalMrc(context.totalMRC + context.gvpnTotalMrc);
            context.quoteToLe.setFinalNrc(context.totalNRC + context.gvpnTotalNrc);
            context.quoteToLe.setFinalArc(context.totalARC + context.gvpnTotalArc);
            Double tcv = (contractTerm * this.setPrecision(context.quoteToLe.getFinalMrc(), 2))
                    + this.setPrecision(context.quoteToLe.getFinalNrc(), 2);
            context.quoteToLe.setTotalTcv(tcv);

        }

        context.gscQuotePricingBean.setGscQuotePrices(gscQuotePrices);
        context.gscQuotePricingBean.setTotalMRC(context.quoteToLe.getFinalMrc());
        context.gscQuotePricingBean.setTotalNRC(context.quoteToLe.getFinalNrc());
        context.gscQuotePricingBean.setTotalTCV(context.quoteToLe.getTotalTcv());
        context.gscQuotePricingBean.setUifnRegistrationCharge(context.uifnRegCharge);
        quoteToLeRepository.save(context.quoteToLe);
        return context;

    }

    /**
     * Method to get gsc prices
     *
     * @param gscQuotePrice
     * @param quoteGsc
     * @return
     */
    private GscQuotePriceBean getGscPrice(GscPricingFeasibilityContext context, GscQuotePriceBean gscQuotePrice,
                                          QuoteGsc quoteGsc) {
        gscQuotePrice.setId(quoteGsc.getId());
        gscQuotePrice.setName(quoteGsc.getName());
        gscQuotePrice.setMrc(quoteGsc.getMrc());
        gscQuotePrice.setArc(quoteGsc.getArc());
        gscQuotePrice.setNrc(quoteGsc.getNrc());
        gscQuotePrice.setProductName(quoteGsc.getProductName());
        gscQuotePrice.setTcv(quoteGsc.getTcv());
        gscQuotePrice.setAccessType(quoteGsc.getAccessType());
        List<QuoteGscDetail> quoteGscDetails = new ArrayList<>();
        quoteGscDetails = quoteGsc.getQuoteGscDetails().stream().collect(Collectors.toList());
        quoteGscDetails.sort((QuoteGscDetail o1, QuoteGscDetail o2) -> o1.getId() - o2.getId());
        quoteGscDetails.stream().forEach(quoteGscDetail -> {
            GscConfigurationPriceBean gscConfigPrice = new GscConfigurationPriceBean();
            gscConfigPrice.setId(quoteGscDetail.getId());
            gscConfigPrice.setSrc(quoteGscDetail.getSrc());
            gscConfigPrice.setSrcType(quoteGscDetail.getSrcType());
            gscConfigPrice.setDest(quoteGscDetail.getDest());
            gscConfigPrice.setDestType(quoteGscDetail.getDestType());
            gscConfigPrice.setMrc(quoteGscDetail.getMrc());
            gscConfigPrice.setNrc(quoteGscDetail.getNrc());
            gscConfigPrice.setArc(quoteGscDetail.getArc());
            getAttributes(context, quoteGsc, quoteGscDetail, gscConfigPrice);
            gscQuotePrice.getGscConfigPrices().add(gscConfigPrice);
        });
        return gscQuotePrice;
    }

    /**
     * Method to get attributes
     *
     * @param quoteGsc
     * @param gscConfig
     * @param gscConfigurationBean
     */
    private void getAttributes(GscPricingFeasibilityContext context, QuoteGsc quoteGsc, QuoteGscDetail gscConfig,
                               GscConfigurationPriceBean gscConfigurationBean) {
        List<QuoteProductComponent> quoteProductComponents = getQuoteProductComponents(gscConfig);
        if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.GLOBAL_OUTBOUND)) {
            globalOutboundPrice = new GlobalOutboundPriceBean();
        } else if (quoteGsc.getProductName().equalsIgnoreCase(GscConstants.DOMESTIC_VOICE)) {
            gscConfigurationBean.setDomesticVoicePrice(new DomesticVoicePriceBean());
        }
        List<String> attributesList = Arrays.asList(GscConstants.RATE_PER_MINUTE_FIXED,
                GscConstants.RATE_PER_MINUTE_MOBILE, GscConstants.RATE_PER_MINUTE_SPECIAL, GscConstants.TERM_NAME,
                GscConstants.DID_ARC, GscConstants.DID_MRC, GscConstants.DID_NRC, GscConstants.CHANNEL_ARC,
                GscConstants.CHANNEL_MRC, GscConstants.CHANNEL_NRC, GscConstants.ORDER_SETUP_ARC,
                GscConstants.ORDER_SETUP_MRC, GscConstants.ORDER_SETUP_NRC, GscConstants.TERM_RATE,
                GscConstants.PHONE_TYPE, GscConstants.UIFN_REGISTRATION_CHARGE);
        List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameIn(attributesList);

        quoteProductComponents.stream().forEach(quoteComponent -> {
            attributes.stream().forEach(attribute -> {
                        List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentAttributeValuesRepository
                                .findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute);
                        if (!quoteProductComponents.isEmpty()) {

                            if (attributeValueList != null && !attributeValueList.isEmpty()) {

                                getAttributesValues(context, attributeValueList, attribute, gscConfigurationBean,
                                        globalOutboundPrice);
                            }

                        }
                    }

            );
        });

        if (Objects.nonNull(globalOutboundPrice)
                && quoteGsc.getProductName().equalsIgnoreCase(GscConstants.GLOBAL_OUTBOUND)) {
            gscConfigurationBean.getGlobalOutboundPrices().add(globalOutboundPrice);
            globalOutboundPrice = null;
        }

    }

    /**
     * Method to process Rate Per Minute based on attribute name
     *
     * @param attributeValues
     * @param attribute
     * @param gscConfigurationBean
     * @param globPrice
     */
    private void getAttributesValues(GscPricingFeasibilityContext context,
                                     List<QuoteProductComponentsAttributeValue> attributeValues, ProductAttributeMaster attribute,
                                     GscConfigurationPriceBean gscConfigurationBean, GlobalOutboundPriceBean globPrice) {
        if (!attributeValues.isEmpty()
                && attributeValues.get(0).getProductAttributeMaster().getId() == attribute.getId()) {
            switch (attribute.getName()) {
                case GscConstants.RATE_PER_MINUTE_FIXED:
                    gscConfigurationBean.setRpmFixed(getRpmValues(attributeValues));
                    break;
                case GscConstants.RATE_PER_MINUTE_SPECIAL:
                    gscConfigurationBean.setRpmSpecial(getRpmValues(attributeValues));
                    break;
                case GscConstants.RATE_PER_MINUTE_MOBILE:
                    gscConfigurationBean.setRpmMobile(getRpmValues(attributeValues));
                    break;
                case GscConstants.TERM_NAME:
                    List<String> termName = getStringValues(attributeValues);
                    gscConfigurationBean.setTermName(termName);
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice()))
                        gscConfigurationBean.getDomesticVoicePrice().setTerminationName(termName);
                    if (Objects.nonNull(globPrice))
                        globPrice.setTerminationName(termName);

                    break;
                case GscConstants.TERM_RATE:
                    List<String> termRate = getStringValues(attributeValues);
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice()))
                        gscConfigurationBean.getDomesticVoicePrice().setPrice(termRate);
                    if (Objects.nonNull(globPrice))
                        globPrice.setPrice(termRate);

                    break;
                case GscConstants.PHONE_TYPE:
                    List<String> phoneType = getStringValues(attributeValues);
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice()))
                        gscConfigurationBean.getDomesticVoicePrice().setPhoneType(phoneType);
                    if (Objects.nonNull(globPrice))
                        globPrice.setPhoneType(phoneType);
                    break;
                case GscConstants.DID_ARC: {
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setDidArc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setDidArc("0");
                        }
                    }
                    break;
                }
                case GscConstants.DID_MRC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setDidMrc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setDidMrc("0");
                        }
                    }

                    break;
                case GscConstants.DID_NRC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setDidNrc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setDidNrc("0");
                        }
                    }
                    break;
                case GscConstants.CHANNEL_ARC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setChannelArc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setChannelArc("0");
                        }
                    }
                    break;
                case GscConstants.CHANNEL_MRC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setChannelMrc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setChannelMrc("0");
                        }
                    }
                    break;
                case GscConstants.CHANNEL_NRC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setChannelNrc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setChannelNrc("0");
                        }
                    }
                    break;
                case GscConstants.ORDER_SETUP_ARC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setOrderSetupArc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setOrderSetupArc("0");
                        }
                    }
                    break;
                case GscConstants.ORDER_SETUP_MRC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setOrderSetupMrc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setOrderSetupMrc("0");
                        }
                    }
                    break;
                case GscConstants.ORDER_SETUP_NRC:
                    if (Objects.nonNull(gscConfigurationBean.getDomesticVoicePrice())) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            gscConfigurationBean.getDomesticVoicePrice().setOrderSetupNrc(value);
                        } else {
                            gscConfigurationBean.getDomesticVoicePrice().setOrderSetupNrc("0");
                        }
                    }
                    break;
                case GscConstants.UIFN_REGISTRATION_CHARGE:
                    if (context.uifnRegCharge == 0) {
                        String value = attributeValues.get(0).getAttributeValues();
                        if (StringUtils.isNotBlank(value)) {
                            context.uifnRegCharge = Double.parseDouble(value);
                        } else {
                            context.uifnRegCharge = Double.parseDouble("0");
                        }
                    }
                    break;
                default:
                    LOGGER.info("other attribute Name-" + attribute.getName());

            }
        }
    }

    /**
     * Method to get rpm values
     *
     * @param attributeValues
     * @return
     */
    private List<Double> getRpmValues(List<QuoteProductComponentsAttributeValue> attributeValues) {
        return attributeValues.stream().map(QuoteProductComponentsAttributeValue::getAttributeValues)
                .filter(StringUtils::isNotBlank).map(Double::parseDouble).collect(Collectors.toList());

    }

    /**
     * Method to get string values
     *
     * @param attributeValues
     * @return
     */
    private List<String> getStringValues(List<QuoteProductComponentsAttributeValue> attributeValues) {
        return attributeValues.stream().map(QuoteProductComponentsAttributeValue::getAttributeValues)
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());

    }

    /**
     * Method to process gsc outbound data
     *
     * @param customerId
     * @return
     * @throws TclCommonException
     */
    public List<GscOutboundPriceBean> processOutboundPriceData(List<String> destinations) throws TclCommonException {
        String outboundResponse = null;
        List<GscOutboundPriceBean> gscOutboundPriceBean = null;
        if (Objects.nonNull(destinations)) {
            LOGGER.info("MDC Filter token value in before Queue call processOutboundPriceData {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            outboundResponse = (String) mqUtils.sendAndReceive(outboundPricingsQueue, String.valueOf(destinations));

            if (!Strings.isNullOrEmpty(outboundResponse)) {
                gscOutboundPriceBean = GscUtils.fromJson(outboundResponse,
                        new TypeReference<List<GscOutboundPriceBean>>() {
                        });
            } else {
                throw new TclCommonException(ExceptionConstants.PRICING_VALIDATION_ERROR,
                        ResponseResource.R_CODE_ERROR);
            }
        }
        return gscOutboundPriceBean;
    }

    /**
     * Method to process gsc outbound data
     *
     * @param customerId
     * @return
     * @throws TclCommonException
     */
    public List<GscOutboundPriceBean> processOutboundData(String rateColumn) throws TclCommonException {
        String outboundResponse = null;
        List<GscOutboundPriceBean> gscOutboundPriceBean = null;
        if (Objects.nonNull(rateColumn)) {
            LOGGER.info("MDC Filter token value in before Queue call processOutboundPriceData {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            outboundResponse = (String) mqUtils.sendAndReceive(outboundDetailsQueue, String.valueOf(rateColumn));

            if (!Strings.isNullOrEmpty(outboundResponse)) {
                gscOutboundPriceBean = GscUtils.fromJson(outboundResponse,
                        new TypeReference<List<GscOutboundPriceBean>>() {
                        });
            } else {
                throw new TclCommonException(ExceptionConstants.PRICING_VALIDATION_ERROR,
                        ResponseResource.R_CODE_ERROR);
            }
        }
        return gscOutboundPriceBean;
    }

    /**
     * Method to update quote to le currency values
     *
     * @param context
     * @return
     */
    public void updateQuoteToLeCurrencyValues(QuoteToLe quoteToLe, List<QuoteGsc> quoteGscs, Double contractTerm, String existingCurrency, String resultantCurrency) {
        GscPricingFeasibilityContext context = new GscPricingFeasibilityContext();
        context.quoteToLe = quoteToLe;
        context.quoteGscs = quoteGscs;
        resetContextPrice(context);
        context.existingCurrency = existingCurrency;
        context.resultantCurrency = resultantCurrency;
        context.pricingRequest = new PricingRequest();
        context.pricingRequest.setOpportunityTerm(contractTerm.toString());
        this.computeGscQuoteLePrices(context);
        context.gvpnAction = GscConstants.GET;
        this.setGvpnPriceAttributes(context);
        context.quoteToLe.setProposedMrc(context.totalMRC + context.gvpnTotalMrc);
        context.quoteToLe.setProposedNrc(context.totalNRC + context.gvpnTotalNrc);
        context.quoteToLe.setProposedArc(context.totalARC + context.gvpnTotalArc);
        context.quoteToLe.setFinalMrc(context.totalMRC + context.gvpnTotalMrc);
        context.quoteToLe.setFinalNrc(context.totalNRC + context.gvpnTotalNrc);
        context.quoteToLe.setFinalArc(context.totalARC + context.gvpnTotalArc);
        Double tcv = (contractTerm * this.setPrecision(context.quoteToLe.getFinalMrc(), 2))
                + this.setPrecision(context.quoteToLe.getFinalNrc(), 2);
        context.quoteToLe.setTotalTcv(tcv);
        quoteToLeRepository.save(context.quoteToLe);
        this.convertGscDetailAttributes(context);

    }

    /**
     * Method to convert gsc detail attributes
     *
     * @param context
     */
    private void convertGscDetailAttributes(GscPricingFeasibilityContext context) {
        context.quoteGscs.stream().forEach(quoteGsc -> {
            quoteGsc.getQuoteGscDetails().stream().forEach(quoteGscDetail -> {
                convertMultipleAttributes(quoteGsc, quoteGscDetail, context, GscConstants.RATE_PER_MINUTE_FIXED);
                convertMultipleAttributes(quoteGsc, quoteGscDetail, context, GscConstants.RATE_PER_MINUTE_MOBILE);
                convertMultipleAttributes(quoteGsc, quoteGscDetail, context, GscConstants.RATE_PER_MINUTE_SPECIAL);
                convertMultipleAttributes(quoteGsc, quoteGscDetail, context, GscConstants.TERM_RATE);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.DID_ARC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.DID_MRC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.DID_NRC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.ORDER_SETUP_ARC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.ORDER_SETUP_MRC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.ORDER_SETUP_NRC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.CHANNEL_ARC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.CHANNEL_MRC);
                convertSingleAttribute(quoteGsc, quoteGscDetail, context, GscConstants.CHANNEL_NRC);
            });

        });
    }

    /**
     * Method to convert single attribute
     *
     * @param quoteGsc
     * @param quoteGscDetail
     * @param context
     * @param attributeName
     */
    private void convertSingleAttribute(QuoteGsc quoteGsc, QuoteGscDetail quoteGscDetail,
                                        GscPricingFeasibilityContext context, String attributeName) {
        List<String> chargeToBeSet = new ArrayList<>();
        List<String> attributeValue = getOrSetComponentAttributeValue(quoteGscDetail.getId(), attributeName,
                quoteGsc.getProductName(), true, new ArrayList<>());
        if (attributeValue.stream().findFirst().isPresent()) {
            String value = attributeValue.stream().findFirst().get();
            if (StringUtils.isNotBlank(value)) {
                Double charge = Double.parseDouble(value);
                charge = Utils
                        .setPrecision(this.convertCurrency(context.existingCurrency, context.resultantCurrency, charge), 2);
                chargeToBeSet.add(charge.toString());
            }
            getOrSetComponentAttributeValue(quoteGscDetail.getId(), attributeName, quoteGsc.getProductName(), false,
                    chargeToBeSet);
        }
    }

    /**
     * Method to convert Multiple Attributes
     *
     * @param quoteGsc
     * @param quoteGscDetail
     * @param context
     * @param attributeName
     */
    private void convertMultipleAttributes(QuoteGsc quoteGsc, QuoteGscDetail quoteGscDetail,
                                           GscPricingFeasibilityContext context, String attributeName) {
        List<String> chargeToBeSet = new ArrayList<>();
        List<String> attributeValues = getOrSetComponentAttributeValue(quoteGscDetail.getId(), attributeName,
                quoteGsc.getProductName(), true, new ArrayList<>());
        if (!CollectionUtils.isEmpty(attributeValues)) {
            attributeValues.stream().forEach(value -> {
                if (StringUtils.isNotBlank(value)) {
                    Double charge = Double.parseDouble(value);
                    charge = Utils.setPrecision(
                            this.convertCurrency(context.existingCurrency, context.resultantCurrency, charge), 4);
                    chargeToBeSet.add(charge.toString());
                }
            });
            getOrSetComponentAttributeValue(quoteGscDetail.getId(), attributeName, quoteGsc.getProductName(), false,
                    chargeToBeSet);
        }
    }

    /**
     * Method to get component attribute value
     *
     * @param referenceId
     * @param attributeName
     * @param componentName
     * @param isGet
     * @param valueToBeSet
     * @return
     */
    public List<String> getOrSetComponentAttributeValue(Integer referenceId, String attributeName, String componentName,
                                                        boolean isGet, List<String> valueToBeSet) {
        List<String> values = new ArrayList<>();
        List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
                .findByReferenceIdAndMstProductComponent_NameAndType(referenceId, componentName,
                        GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        List<ProductAttributeMaster> attributes = productAttributeMasterRepository.findByNameAndStatus(attributeName,
                (byte) 1);
        if (quoteProductComponents.stream().findFirst().isPresent() && attributes.stream().findFirst().isPresent()) {
            QuoteProductComponent quoteProductComponent = quoteProductComponents.stream().findFirst().get();
            ProductAttributeMaster attribute = attributes.stream().findFirst().get();
            List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentAttributeValuesRepository
                    .findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, attribute);
            if (attributeValues.stream().findFirst().isPresent()) {
                QuoteProductComponentsAttributeValue quoteComponentAttributeValue = attributeValues.stream().findFirst()
                        .get();
                if (!attributeValues.isEmpty() && attributeValues.size() > 1) {
                    if (isGet) {
                        values = getStringValues(attributeValues);
                    } else {
                        if (!CollectionUtils.isEmpty(valueToBeSet)) {
                            deleteAttributeByName(attribute.getName(), quoteProductComponent);

                            List<QuoteProductComponentsAttributeValue> componentAttributes = valueToBeSet.stream()
                                    .map(rpm -> copyAttribute(quoteComponentAttributeValue, rpm))
                                    .collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(componentAttributes)) {
                                quoteProductComponentAttributeValuesRepository.saveAll(componentAttributes);
                            }
                        }
                    }
                } else if (!attributeValues.isEmpty() && attributeValues.stream().findFirst().isPresent()) {
                    if (isGet) {
                        values.add(quoteComponentAttributeValue.getAttributeValues());
                    } else {
                        if (!CollectionUtils.isEmpty(valueToBeSet)) {
                            quoteComponentAttributeValue.setAttributeValues(valueToBeSet.stream().findFirst().get());
                            quoteProductComponentAttributeValuesRepository.save(quoteComponentAttributeValue);
                        }
                    }

                }
            }
        }

        return values;
    }

    /**
     * Set Partner Attributes in GscPricingRequest
     *
     * @param context
     */
//    private void setPartnerAttributes(GscPricingFeasibilityContext context) {
//        if (Objects.nonNull(context.quoteToLe.getClassification()) && context.quoteToLe.getClassification().equalsIgnoreCase(PartnerConstants.SELL_THROUGH_CLASSIFICATION)) {
//            context.pricingRequest.setQuoteTypePartner(context.quoteToLe.getClassification());
//        } else {
//            context.pricingRequest.setQuoteTypePartner("None");
//        }
//    }

    /**
     * static class for GscPricingFeasibilityContext
     */
    private static class GscPricingFeasibilityContext {
        QuoteToLe quoteToLe;
        MstOmsAttribute mstOmsAttribute;
        QuoteLeAttributeValue quoteLeAttributeValue;
        GscQuotePricingBean gscQuotePricingBean;
        List<QuoteGsc> quoteGscs;
        String attributeName;
        String category;
        String state;
        Double totalMRC;
        Double totalNRC;
        Double totalARC;
        List<String> termName;
        List<String> termRate;
        List<String> phoneType;
        List<String> surchargeRate;
        List<Double> rpmFixed;
        List<Double> rpmMobile;
        List<Double> rpmSpecial;
        List<Double> rpm;
        PricingRequest pricingRequest;
        PricingResponse pricingResponse;
        GscPricingRequest configurationData;
        String resultantCurrency;
        String existingCurrency;
        Double gvpnTotalMrc;
        Double gvpnTotalNrc;
        Double gvpnTotalArc;
        Double gvpnTotalTcv;
        Double uifnRegCharge;
        String gvpnAction;
        String message;
        Double inboundVolume;
        Double outboundVolume;
        Boolean contractTermUpdate;
        String globalOutboundRateColumn;
        Boolean isMacdNewCountry;
    }

    /**
     * Context class for manual price update
     *
     * @author VISHESH AWASTHI
     */
    private static class ManualPricingContext {
        QuoteToLe quoteToLe;
        QuoteGsc quoteGsc;
        QuoteGscDetail quoteGscDetail;
        GscManualPricing pricingRequest;
        double changeInMrc;
        double changeInNrc;
        double changeInArc;
        Integer termInMonths;
    }
}
