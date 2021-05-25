package com.tcl.dias.oms.gsc.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.gsc.beans.GscWholesaleInterconnectBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscProductLocationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteLeAttributeBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSipTrunkAttributeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gsc.common.GscOmsSfdcComponent;
import com.tcl.dias.oms.gsc.exception.ObjectNotFoundException;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.vavr.control.Try;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.COMMA;
import static com.tcl.dias.common.constants.CommonConstants.HYPHEN;
import static com.tcl.dias.common.constants.CommonConstants.INR;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.CommonConstants.USD;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_EMAIL;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_ID;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_NAME;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_NO;
import static com.tcl.dias.common.constants.LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY;
import static com.tcl.dias.common.constants.LeAttributesConstants.DESIGNATION;
import static com.tcl.dias.common.constants.LeAttributesConstants.IS_GSC_MULTI_MACD;
import static com.tcl.dias.common.constants.LeAttributesConstants.LE_EMAIL;
import static com.tcl.dias.common.constants.LeAttributesConstants.MULTI_MACD_GSC_SERVICE_DETAILS;
import static com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR;
import static com.tcl.dias.oms.constants.UserStatusConstants.OPEN;
import static com.tcl.dias.oms.constants.UserStatusConstants.OTHERS;
import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundError;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_INTERCONNECT_ATTRIBUTE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CUSTOMER_SECS_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.INTERCONNECT_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACSNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACTION_DELETE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACTION_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACTION_UPDATE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONTRACT_TERM_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.EMAIL_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_PRODUCT_NAME;
import static com.tcl.dias.oms.gsc.util.GscConstants.INDIA;
import static com.tcl.dias.oms.gsc.util.GscConstants.NEW;
import static com.tcl.dias.oms.gsc.util.GscConstants.NO;
import static com.tcl.dias.oms.gsc.util.GscConstants.OPT_WHOLESALE_NGP_CUSTOMER_PORTAL;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_LE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_TO_LE_STAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_FIXED;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_MOBILE;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_SPECIAL;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.PROPOSAL_SENT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.VERBAL_AGREEMENT_STAGE;

/**
 * Services to handle all quote related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscQuoteService {

    public static final Logger LOGGER = LoggerFactory.getLogger(GscQuoteService.class);

    @Autowired
    GscQuoteDetailService gscQuoteDetailService;
    @Autowired
    GscQuoteAttributeService gscQuoteAttributeService;
    @Autowired
    QuoteGscRepository quoteGscRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    QuoteToLeRepository quoteToLeRepository;
    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;
    @Autowired
    QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
    @Autowired
    ProductSolutionRepository productSolutionRepository;
    @Autowired
    MstProductOfferingRepository mstProductOfferingRepository;
    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
    @Autowired
    GscSlaService gscSlaService;
    @Autowired
    QuoteGscDetailsRepository quoteGscDetailsRepository;
    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserInfoUtils userInfoUtils;
    @Autowired
    UserRepository userRepository;

    @Value("${app.host}")
    String appHost;

    @Value("${notification.mail.admin}")
    String adminRelativeUrl;

    @Value("${rabbitmq.product.location.queue}")
    String productLocationQueue;

    @Value("${rabbitmq.customer.location.queue}")
    String customerLocationQueue;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    CofDetailsRepository cofDetailsRepository;

    @Autowired
    DocusignAuditRepository docusignAuditRepository;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;
    @Value("${customer.support.email}")
    String customerSupportEmail;
    @Value("${object.storage.gsc.country.files.move.queue}")
    String objectStorageMoveCountryFiles;
    @Autowired
    QuoteDelegationRepository quoteDelegationRepository;
    @Value("${pilot.team.email}")
    String[] pilotTeamMail;
    @Value("${notification.mail.quotedashboard}")
    String quoteDashBoardRelativeUrl;
    @Autowired
    OpportunityRepository opportunityRepository;
    @Value("${rabbitmq.get.partner.account.name.by.partner}")
    String partnerAccountNameMQ;
    @Autowired
    AdditionalServiceParamRepository additionalServiceParamRepository;
    @Value("${rabbitmq.supplier.currency.queue}")
    private String supplierCurrencyQueue;
    @Value("${rabbitmq.customerlename.queue}")
    private String getCustomerLeNameById;
    @Value("${rabbitmq.customer.currency.queue}")
    private String customerCurrencyQueue;
    @Value("${rabbitmq.wholesale.customer.interconnect}")
    private String interconnectQueue;

    @Autowired
    GscOmsSfdcComponent gscOmsSfdcComponent;

    private static boolean isNumeric(String pricingRate) {
        try {
            Double.parseDouble(pricingRate);
            Integer.parseInt(pricingRate);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Builds the CurrencyConvertor Context
     *
     * @param quoteToLeId
     * @param inputCurrency
     * @return CreateCurrencyConvertorContext
     */
    private static CreateCurrencyConvertorContext createCurrencyContext(Integer quoteToLeId, String inputCurrency) {
        CreateCurrencyConvertorContext context = new CreateCurrencyConvertorContext();
        context.quoteToLeId = quoteToLeId;
        context.inputCurrency = inputCurrency.toUpperCase();
        return context;
    }

    /**
     * createContext
     *
     * @param request
     * @return
     */
    private GscQuoteContext createContext(GscQuoteDataBean request) {
        Integer customerId = request.getCustomerId();
        GscQuoteContext context = new GscQuoteContext();
        context.user = getUserId(Utils.getSource());
        context.customer = !Objects.isNull(customerId)
                ? customerRepository.findByErfCusCustomerIdAndStatus(customerId, (byte) 1)
                : context.user.getCustomer();
        context.gscQuoteDataBean = request;
        // Only for MACD
        context.supplierLegalId = Objects.nonNull(request.getSupplierLegalId()) ? request.getSupplierLegalId() : 0;
        context.opportunity = opportunityRepository.findByUuid(request.getQuoteCode());
        context.customerLeId = request.getCustomerLeId();
        context.paymentCurrency = USD;
        context.isGscMultiMacd = Objects.nonNull(request.getIsGscMultiMacd()) ? request.getIsGscMultiMacd() : "No";
        context.secsId = request.getSecsId();
        context.quoteCategory = request.getQuoteCategory();
        return context;
    }

    private User getUserId(String username) {
        return userRepository.findByUsernameAndStatus(username, 1);
    }

    /**
     * constructQuote
     *
     * @param customer
     * @param user
     * @param context
     * @return
     */
    private Quote constructQuote(final Customer customer, final User user, GscQuoteContext context) {
        Quote quote = new Quote();
        quote.setCustomer(customer);
        quote.setCreatedBy(user.getId());
        quote.setCreatedTime(new Date());
        quote.setStatus((byte) 1);
        quote.setEngagementOptyId(context.gscQuoteDataBean.getEngagementOptyId());
        quote.setQuoteCode(null != context.gscQuoteDataBean.getEngagementOptyId() ? context.gscQuoteDataBean.getQuoteCode() :
                Utils.generateRefId(GscConstants.GSC_PRODUCT_NAME.toUpperCase()));
        return quote;
    }

    /**
     * saveQuote
     *
     * @param context
     * @return
     */
    private GscQuoteContext saveQuote(GscQuoteContext context) {
        Customer customer = context.customer;
        User user = context.user;
        context.quote = quoteRepository.save(constructQuote(customer, user, context));
        context.gscQuoteDataBean.setQuoteId(context.quote.getId());
        context.gscQuoteDataBean.setQuoteCode(context.quote.getQuoteCode());
        return context;
    }

    /**
     * constructQuoteToLe
     *
     * @param context
     * @return QuoteToLe
     */
    private QuoteToLe constructQuoteToLe(GscQuoteContext context) {
        QuoteToLe quoteToLe = new QuoteToLe();
        quoteToLe.setQuote(context.quote);
        quoteToLe.setStage(QuoteStageConstants.SELECT_SERVICES.getConstantCode());
        quoteToLe.setCurrencyCode(context.paymentCurrency);
        quoteToLe.setTermInMonths("12 months");
        quoteToLe.setQuote(context.quote);
        quoteToLe.setStage(QuoteStageConstants.SELECT_SERVICES.getConstantCode());
        quoteToLe.setQuoteType(context.gscQuoteDataBean.getQuoteType());
        quoteToLe.setQuoteCategory(Objects.isNull(context.gscQuoteDataBean.getQuoteCategory()) ? null
                : context.gscQuoteDataBean.getQuoteCategory());
        quoteToLe.setClassification(context.gscQuoteDataBean.getClassification());
        quoteToLe.setErfCusCustomerLegalEntityId(context.customerLeId);
        quoteToLe.setIsWholesale(BACTIVE);
        quoteToLe.setQuoteCategory(context.quoteCategory);
        return quoteToLe;
    }

    private GscQuoteContext updateQuoteToLeDetails(GscQuoteContext context) {
        context.quoteToLe.setClassification(context.gscQuoteDataBean.getClassification());
        context.quoteToLe.setQuoteType(context.gscQuoteDataBean.getQuoteType());
        if (context.gscQuoteDataBean.getQuoteType().equalsIgnoreCase("NEW")) {
            context.quoteToLe.setErfServiceInventoryTpsServiceId(null);
        }
        context.quoteToLe = quoteToLeRepository.save(context.quoteToLe);
        return context;
    }

    /**
     * TODO Need to rewrite this logic
     * @param context
     * @return
     */
    private GscQuoteContext setPaymentCurrency(GscQuoteContext context) {
        final String productName = context.gscQuoteDataBean.getSolutions().stream().findFirst().get().getProductName();
        if (ACANS.equalsIgnoreCase(productName) || ACDTFS.equalsIgnoreCase(productName) || ACSNS.equalsIgnoreCase(productName)) {
            context.paymentCurrency = INR;
        }
        if (ORDER_TYPE_MACD.equalsIgnoreCase(context.gscQuoteDataBean.getQuoteType()) &&
                Objects.nonNull(context.gscQuoteDataBean.getQuoteCategory())) {
            try {
                if (DOMESTIC_VOICE.equalsIgnoreCase(productName)) {
                    context.paymentCurrency = (String) mqUtils.sendAndReceive(customerCurrencyQueue, String.valueOf(context.customerLeId));
                } else if(REQUEST_TYPE_INTERCONNECT_ATTRIBUTE.equalsIgnoreCase(context.gscQuoteDataBean.getQuoteCategory())){
                    // This is multi macd code for wholesale
                    context.paymentCurrency = USD;
                } else {
                    context.paymentCurrency = (String) mqUtils.sendAndReceive(supplierCurrencyQueue, String.valueOf(context.supplierLegalId));
                }
            } catch (Exception e) {
                LOGGER.warn("Supplier {} Currency Not Found :: {}", context.supplierLegalId, e.getCause());
            }
        }
        return context;
    }

    /**
     * saveQuoteToLe
     *
     * @param context
     * @return
     */
    private GscQuoteContext saveQuoteToLe(GscQuoteContext context) {
        QuoteToLe quoteToLe = constructQuoteToLe(context);
        context.quoteToLe = quoteToLeRepository.save(quoteToLe);
        context.gscQuoteDataBean.setQuoteLeId(context.quoteToLe.getId());
        return context;
    }

    /**
     * create Opportunity in sfdc
     *
     * @param context
     * @return
     */
    private GscQuoteContext createOpportunityInSfdc(GscQuoteContext context) {
        try {
            if(userInfoUtils.getUserRoles().contains(OPT_WHOLESALE_NGP_CUSTOMER_PORTAL)) {
                // Triggering Sfdc Opportunity Creation
                if (Objects.nonNull(context.quoteToLe) && StringUtils.isEmpty(context.gscQuoteDataBean.getEngagementOptyId())) {
                    gscOmsSfdcComponent.getOmsSfdcService().processCreateOpty(context.quoteToLe,
                            context.gscQuoteDataBean.getProductFamilyName());
                }
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return context;
    }

    /**
     * createQuoteToLeProductFamily
     *
     * @param context
     * @return
     */
    private GscQuoteContext createQuoteToLeProductFamily(GscQuoteContext context) {
        QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
        quoteToLeProductFamily.setMstProductFamily(context.productFamily);
        quoteToLeProductFamily.setQuoteToLe(context.quoteToLe);
        context.quoteToLeProductFamily = quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
        return context;
    }

    /**
     * getProductOffering
     *
     * @param mstProductFamily
     * @param productOfferingName
     * @param user
     * @return
     */
    private MstProductOffering getProductOffering(final MstProductFamily mstProductFamily,
                                                  final String productOfferingName, final User user) {
        MstProductOffering productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(
                mstProductFamily, productOfferingName, GscConstants.STATUS_ACTIVE);
        if (Objects.isNull(productOffering)) {
            productOffering = new MstProductOffering();
            productOffering.setCreatedBy(user.getUsername());
            productOffering.setCreatedTime(new Date());
            productOffering.setMstProductFamily(mstProductFamily);
            productOffering.setProductName(productOfferingName);
            productOffering.setStatus((byte) 1);
            productOffering.setProductDescription(productOfferingName);
            mstProductOfferingRepository.save(productOffering);
        }
        return productOffering;
    }

    /**
     * createProductSolution
     *
     * @param mstProductOffering
     * @param quoteToLeProductFamily
     * @param productProfileData
     * @return
     */
    private ProductSolution createProductSolution(final MstProductOffering mstProductOffering,
                                                  final QuoteToLeProductFamily quoteToLeProductFamily, final String productProfileData) {
        final ProductSolution productSolution = new ProductSolution();
        productSolution.setMstProductOffering(mstProductOffering);
        productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
        productSolution.setProductProfileData(productProfileData);
        productSolution.setSolutionCode(Utils.generateUid());
        return productSolution;
    }

    /**
     * createGscQuote
     *
     * @param quoteDataBean
     * @param productSolution
     * @param quoteToLe
     * @param customer
     * @param solutionCode
     * @param user
     * @return
     */
    private QuoteGsc createGscQuote(GscQuoteDataBean quoteDataBean, ProductSolution productSolution,
                                    QuoteToLe quoteToLe, Customer customer, String solutionCode, User user) {
        QuoteGsc quoteGsc = new QuoteGsc();
        quoteGsc.setAccessType(quoteDataBean.getAccessType());
        quoteGsc.setCreatedBy(String.valueOf(user.getId()));
        quoteGsc.setProductSolution(productSolution);
        quoteGsc.setQuoteToLe(quoteToLe);
        quoteGsc.setStatus(GscConstants.STATUS_ACTIVE);
        String productName = solutionCode.replaceAll(quoteDataBean.getAccessType(), "");
        quoteGsc.setProductName(productName);
        quoteGsc.setName(String.format("%s_%s", quoteDataBean.getProductFamilyName(), solutionCode));
        quoteGsc.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        return quoteGsc;
    }

    /**
     * toGscQuoteBean
     *
     * @param quoteGsc
     * @return
     */
    private GscQuoteBean toGscQuoteBean(QuoteGsc quoteGsc) {
        GscQuoteBean quoteBean = new GscQuoteBean();
        quoteBean.setId(quoteGsc.getId());
        quoteBean.setAccessType(quoteGsc.getAccessType());
        quoteBean.setNrc(quoteGsc.getNrc());
        quoteBean.setMrc(quoteGsc.getMrc());
        quoteBean.setArc(quoteBean.getArc());
        quoteBean.setTcv(quoteBean.getTcv());
        return quoteBean;
    }

    /**
     * createProductSolution
     *
     * @param solution
     * @param context
     * @return
     * @throws IllegalArgumentException
     * @throws TclCommonException
     */
    private GscSolutionBean createProductSolution(GscSolutionBean solution, GscQuoteContext context)
            throws TclCommonException, IllegalArgumentException {
        MstProductOffering masterProductOffering = getProductOffering(context.productFamily, solution.getOfferingName(),
                context.user);
        ProductSolution productSolution = createProductSolution(masterProductOffering, context.quoteToLeProductFamily,
                GscUtils.toJson(solution));
        productSolutionRepository.save(productSolution);
        solution.setSolutionId(productSolution.getId());
        QuoteGsc quoteGsc = quoteGscRepository.save(createGscQuote(context.gscQuoteDataBean, productSolution,
                context.quoteToLe, context.customer, solution.getSolutionCode(), context.user));
        if (!GscConstants.PSTN.equals(quoteGsc.getAccessType())) {
            gscSlaService.processSla(quoteGsc);
        }
        solution.setGscQuotes(ImmutableList.of(GscQuoteBean.fromQuoteGsc(quoteGsc)));
        solution.setProductName(quoteGsc.getProductName());
        solution.setSolutionCode(productSolution.getSolutionCode());

        return solution;
    }

    /**
     * saveProductSolutions
     *
     * @param context
     * @return
     */
    private GscQuoteContext saveProductSolutions(GscQuoteContext context) {
        GscQuoteDataBean quoteDataBean = context.gscQuoteDataBean;
        context.gscQuoteDataBean.setSolutions(quoteDataBean.getSolutions().stream().map(solution -> {
            try {
                return createProductSolution(solution, context);
            } catch (Exception e) {
                LOGGER.info("Exception occured : {}", e.getMessage());
            }
            return null;
        }).collect(Collectors.toList()));
        return context;
    }

    /**
     * Get quote by id
     *
     * @param quoteId
     * @return {@link Quote}
     */
    public Try<Quote> getQuote(Integer quoteId) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
        return Optional.ofNullable(quote).map(Try::success).orElse(
                notFoundError(ExceptionConstants.QUOTE_EMPTY, String.format("Quote with id: %s not found", quoteId)));
    }

    /**
     * Get quote to le by ID
     *
     * @param quoteToLeId
     * @return {@link QuoteToLe}
     */
    public Try<QuoteToLe> getQuoteToLe(Integer quoteToLeId) {
        Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
        return quoteToLeRepository.findById(quoteToLeId).map(Try::success).orElse(notFoundError(
                ExceptionConstants.QUOTE_EMPTY, String.format("Quote To Le  with id: %s not found", quoteToLeId)));
    }

    /**
     * fetchMstProductFamily
     *
     * @param productName
     * @return
     */
    private Try<MstProductFamily> fetchMstProductFamily(String productName) {
        return Optional
                .ofNullable(mstProductFamilyRepository.findByNameAndStatus(productName, GscConstants.STATUS_ACTIVE))
                .map(Try::success).orElse(notFoundError(ExceptionConstants.PRODUCT_EMPTY,
                        String.format("Product with name: %s not found", productName)));

    }

    /**
     * deleteProductSolutions
     *
     * @param productSolutionIds
     */

    private void deleteProductSolutions(QuoteToLe quoteToLe, Set<Integer> productSolutionIds) {
        List<ProductSolution> productSolutions = productSolutionRepository.findAllById(productSolutionIds);
        productSolutions.forEach(productSolution -> {
            List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
                    GscConstants.STATUS_ACTIVE);
            quoteGscs.forEach(gscQuoteDetailService::deleteQuoteGscDetailsByQuoteGsc);
            quoteGscRepository.deleteByProductSolution(productSolution);

            productSolutionRepository.delete(productSolution);
        });
        if (!CollectionUtils.isEmpty(productSolutions)) {
            if(userInfoUtils.getUserRoles().contains(OPT_WHOLESALE_NGP_CUSTOMER_PORTAL)) {
                gscOmsSfdcComponent.getOmsSfdcService().processDeleteProduct(quoteToLe,
                        productSolutions.stream().findFirst().get());
            }
        }
    }

    /**
     * processProductSolutions
     *
     * @param context
     * @return
     */
    private GscQuoteContext processProductSolutions(GscQuoteContext context) {
        QuoteToLeProductFamily quoteToLeProductFamily = context.quoteToLeProductFamily;
        List<ProductSolution> savedProductSolutions = productSolutionRepository
                .findByQuoteToLeProductFamily(quoteToLeProductFamily);
        List<GscSolutionBean> requestSolutions = context.gscQuoteDataBean.getSolutions();
        Set<Integer> savedSolutionIds = savedProductSolutions.stream().map(ProductSolution::getId)
                .collect(Collectors.toSet());
        Set<Integer> requestSolutionIds = requestSolutions.stream().map(GscSolutionBean::getSolutionId)
                .filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Integer> solutionIdsToDelete = Sets.difference(savedSolutionIds, requestSolutionIds);
        List<GscSolutionBean> newSolutions = requestSolutions.stream()
                .filter(solution -> Objects.isNull(solution.getSolutionId())).collect(Collectors.toList());
        // delete all unwanted solutions
        deleteProductSolutions(context.quoteToLe, solutionIdsToDelete);
        // create new solutions
        newSolutions.forEach(solutionBean -> {
            try {
                createProductSolution(solutionBean, context);
            } catch (TclCommonException e) {
                LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
            }
        });
        context.gscQuoteDataBean
                .setSolutions(productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily).stream()
                        .map(this::createProductSolutionBean).collect(Collectors.toList()));
        return context;
    }

    /**
     * processAccessTypeChange
     *
     * @param context
     * @return
     */
    private GscQuoteContext processAccessTypeChange(GscQuoteContext context) {
        QuoteToLeProductFamily quoteToLeProductFamily = context.quoteToLeProductFamily;
        List<ProductSolution> savedProductSolutions = productSolutionRepository
                .findByQuoteToLeProductFamily(quoteToLeProductFamily);

        List<QuoteToLeProductFamily> families = quoteToLeProductFamilyRepository.findByQuoteToLe(context.quoteToLe.getId());
        if (families.size() > 1) {
            Integer quoteToLeProductFamilyId = families.stream()
                    .filter(family -> family.getMstProductFamily().getName().equals(GvpnConstants.GVPN))
                    .map(QuoteToLeProductFamily::getId).findFirst().get();
//            try {
//                gvpnQuoteService.deleteProductFamily(quoteToLeProductFamilyId);
//            } catch (TclCommonException e) {
//                Throwables.propagate(e);
//            }
        }
        Set<Integer> solutionIdsToDelete = savedProductSolutions.stream().map(ProductSolution::getId)
                .collect(Collectors.toSet());
        deleteProductSolutions(context.quoteToLe, solutionIdsToDelete);
        context.gscQuoteDataBean.getSolutions().forEach(solutionBean -> {
            try {
                createProductSolution(solutionBean, context);
            } catch (TclCommonException e) {
                LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
            }
        });
        context.gscQuoteDataBean
                .setSolutions(productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily).stream()
                        .map(this::createProductSolutionBean).collect(Collectors.toList()));
        return context;
    }

    /**
     * populateQuote
     *
     * @param context
     * @return
     */
    private Try<GscQuoteContext> populateQuote(GscQuoteContext context) {
        return getQuote(context.gscQuoteDataBean.getQuoteId()).map(quote -> {
            context.quote = quote;
            return context;
        });
    }

    /**
     * populateQuoteToLe
     *
     * @param context
     * @return
     */
    private Try<GscQuoteContext> populateQuoteToLe(GscQuoteContext context) {
        return getQuoteToLe(context.gscQuoteDataBean.getQuoteLeId()).map(quoteToLe -> {
            context.quoteToLe = quoteToLe;
            return context;
        });
    }

    /**
     * populateProductFamily
     *
     * @param context
     * @return
     */
    private Try<GscQuoteContext> populateProductFamily(GscQuoteContext context) {
        return fetchMstProductFamily(context.gscQuoteDataBean.getProductFamilyName()).map(productFamily -> {
            context.productFamily = productFamily;
            return context;
        });
    }

    /**
     * populateQuoteToLeProductFamily
     *
     * @param context
     * @return
     */
    private GscQuoteContext populateQuoteToLeProductFamily(GscQuoteContext context) {
        QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
                .findByQuoteToLeAndMstProductFamily(context.quoteToLe, context.productFamily);
        context.quoteToLeProductFamily = quoteToLeProductFamily;
        return context;
    }

    /**
     * checkAccessTypeAndProcessProductSolutions
     *
     * @param context
     * @return
     */
    private GscQuoteContext checkAccessTypeAndProcessProductSolutions(GscQuoteContext context) {
        QuoteToLeProductFamily quoteToLeProductFamily = context.quoteToLeProductFamily;
        List<ProductSolution> savedProductSolutions = productSolutionRepository
                .findByQuoteToLeProductFamily(quoteToLeProductFamily);
        String accessType = savedProductSolutions.stream().findFirst()
                .flatMap(solution -> quoteGscRepository
                        .findByProductSolutionAndStatus(solution, GscConstants.STATUS_ACTIVE).stream().findFirst())
                .map(QuoteGsc::getAccessType).orElse("");
        if (accessType.equals(context.gscQuoteDataBean.getAccessType())) {
            return processProductSolutions(context);
        } else {
            context.isAccessTypeChange = true;
            return processAccessTypeChange(context);
        }
    }

    /**
     * Create quote
     *
     * @param quoteBean
     * @return {@link GscQuoteDataBean}
     */
    @Transactional
    public GscQuoteDataBean createQuote(GscQuoteDataBean quoteBean) {
        Objects.requireNonNull(quoteBean, QUOTE_NULL_MESSAGE);
        return Try.success(createContext(quoteBean))
                .map(this::saveQuote)
                .map(this::setPaymentCurrency)
                .map(this::saveQuoteToLe)
                .map(this::persistDefaultQuoteLeAttributes)
                .flatMap(this::populateProductFamily)
                .map(this::createQuoteToLeProductFamily)
                .map(this::saveProductSolutions)
                .mapTry(this::createOpportunityInSfdc)
                .map(context -> context.gscQuoteDataBean).get();
    }

    /**
     * Update quote
     *
     * @param quoteBean
     * @return {@link GscQuoteDataBean}
     */
    @Transactional
    public GscQuoteDataBean updateQuote(GscQuoteDataBean quoteBean) {
        Objects.requireNonNull(quoteBean, QUOTE_NULL_MESSAGE);
        return Try.success(createContext(quoteBean))
                .flatMap(this::populateQuote)
                .flatMap(this::populateQuoteToLe)
                .map(this::updateQuoteToLeDetails)
                .map(this::persistDefaultQuoteLeAttributes)
                .flatMap(this::populateProductFamily)
                .map(this::populateQuoteToLeProductFamily)
                .map(this::checkAccessTypeAndProcessProductSolutions)
                .map(this::updateProductServiceInSfdc)
                .map(context -> context.gscQuoteDataBean).get();
    }

    /**
     * Get quote gsc by ID
     *
     * @param quoteId
     * @return {@link GscQuoteDataBean}
     */
    public Try<GscQuoteDataBean> getGscQuoteById(Integer quoteId) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        return getQuoteData(quoteId).flatMap(this::getProductFamily).flatMap(this::setRatesForConfigurations);
    }

    private Try<GscQuoteDataBean> setRatesForConfigurations(GscQuoteDataBean quoteBean) {

        quoteBean.getSolutions().forEach(gscSolutionBean -> gscSolutionBean.getGscQuotes()
                .forEach(gscQuoteBean -> gscQuoteBean.getConfigurations()
                        .forEach(gscQuoteConfigurationBean -> gscQuoteConfigurationBean.getProductComponents().stream()
                                .findFirst()
                                .ifPresent(gscProductComponentBean -> gscProductComponentBean.getAttributes().forEach(
                                        gscQuoteProductComponentsAttributeValueBean -> pickRatePerMinuteFromAttributes(
                                                gscQuoteConfigurationBean,
                                                gscQuoteProductComponentsAttributeValueBean))))));
        return Try.success(quoteBean);
    }

    private void pickRatePerMinuteFromAttributes(GscQuoteConfigurationBean gscQuoteConfigurationBean,
                                                 GscQuoteProductComponentsAttributeValueBean gscQuoteProductComponentsAttributeValueBean) {
        if ((RATE_PER_MINUTE_FIXED).equalsIgnoreCase(gscQuoteProductComponentsAttributeValueBean.getAttributeName())) {
            if (!StringUtils.isEmpty(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
                if (isNumeric(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
                    gscQuoteConfigurationBean.setRatePerMinFixed(Double.valueOf(gscQuoteProductComponentsAttributeValueBean.getValueString()));
                }
            }
        }
        if ((RATE_PER_MINUTE_SPECIAL)
                .equalsIgnoreCase(gscQuoteProductComponentsAttributeValueBean.getAttributeName())) {
            if (!StringUtils.isEmpty(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
                if (isNumeric(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
                    gscQuoteConfigurationBean.setRatePerMinSpecial(
                            Double.valueOf(gscQuoteProductComponentsAttributeValueBean.getValueString()));
                }
            }
        }
        if ((RATE_PER_MINUTE_MOBILE).equalsIgnoreCase(gscQuoteProductComponentsAttributeValueBean.getAttributeName())) {
            if (!StringUtils.isEmpty(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
                if (isNumeric(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
                    gscQuoteConfigurationBean.setRatePerMinMobile(
                            Double.valueOf(gscQuoteProductComponentsAttributeValueBean.getValueString()));
                }
            }
        }
    }

    /**
     * getQuoteData
     *
     * @param quoteId
     * @return
     */
    private Try<GscQuoteDataBean> getQuoteData(Integer quoteId) {
        GscQuoteDataBean quoteBean = new GscQuoteDataBean();

        Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
        if (!Objects.isNull(quote)) {
            quoteBean.setQuoteId(quoteId);
            quoteBean.setQuoteCode(quote.getQuoteCode());
            quoteBean.setCustomerId(quote.getCustomer().getErfCusCustomerId());
            quoteBean.setEngagementOptyId(quote.getEngagementOptyId());
        } else {
            return notFoundError(ExceptionConstants.QUOTE_EMPTY, String.format("Quote with id: %s not found", quoteId));
        }

        final List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
        if (!quoteToLes.isEmpty()) {
            quoteBean.setQuoteLeId(quoteToLes.get(0).getId());
            quoteBean.setQuoteType(quoteToLes.get(0).getQuoteType());
            quoteBean.setQuoteCategory(quoteToLes.get(0).getQuoteCategory());
            quoteBean.setClassification(quoteToLes.get(0).getClassification());
            quoteBean.setCustomerLeId(quoteToLes.get(0).getErfCusCustomerLegalEntityId());
            quoteBean.setWholesale(quoteToLes.get(0).getIsWholesale() == BACTIVE);

            getIsMultiMacdAttribute(quoteBean, quoteToLes);
            getSecsId(quoteBean, quoteToLes);
            getInterconnectId(quoteBean, quoteToLes);

        } else {
            return notFoundError(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
                    String.format("QuoteLeId not found for given quote id: %s", quoteId));
        }
        quoteBean.setLegalEntities(getLegalEntities(quote));

        CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quote.getQuoteCode(),
                Source.MANUAL_COF.getSourceType());
        quoteBean.setManualCofSigned(null != cofDetail);
        DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
        quoteBean.setDocusign(docusignAudit != null);
        return Try.success(quoteBean);
    }

    /**
     * Get Is Multi macd attribute against quote to le
     *
     * @param quoteBean
     * @param quoteToLes
     * @return
     */
    private void getIsMultiMacdAttribute(GscQuoteDataBean quoteBean, List<QuoteToLe> quoteToLes) {
        List<QuoteLeAttributeValue> gscMultiMacdAttributes = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLes.get(0),
                IS_GSC_MULTI_MACD);
        if (!CollectionUtils.isEmpty(gscMultiMacdAttributes)) {
            quoteBean.setIsGscMultiMacd(gscMultiMacdAttributes.stream().findFirst().get().getAttributeValue());
            LOGGER.info("Value of is multi macd attribute is {}", quoteBean.getIsGscMultiMacd());
        } else {
            quoteBean.setIsGscMultiMacd(NO);
        }
    }

    private void getSecsId(GscQuoteDataBean quoteBean, List<QuoteToLe> quoteToLes) {
        List<QuoteLeAttributeValue> secsId = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLes.get(0),
                ATTR_CUSTOMER_SECS_ID);
        if (!CollectionUtils.isEmpty(secsId)) {
            quoteBean.setSecsId(secsId.stream().findFirst().get().getAttributeValue());
            LOGGER.info("Value of is secs id attribute is {}", quoteBean.getSecsId());
        }
    }

    private void getInterconnectId(GscQuoteDataBean quoteBean, List<QuoteToLe> quoteToLes) {
        List<QuoteLeAttributeValue> interconnectId = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLes.get(0),
                INTERCONNECT_ID);
        if (!CollectionUtils.isEmpty(interconnectId)) {
            AdditionalServiceParams additionalServiceParams = additionalServiceParamRepository
                    .findById(Integer.valueOf(interconnectId.stream().findFirst().get().getAttributeValue()))
                    .get();
            quoteBean.setInterconnectId(additionalServiceParams.getValue());
            LOGGER.info("Value of is interconnect id attribute is {}", quoteBean.getInterconnectId());
        }
    }

    private List<GscQuoteToLeBean> getLegalEntities(Quote quote) {
        return Optional.ofNullable(quoteToLeRepository.findByQuote(quote)).orElse(ImmutableList.of()).stream()
                .map(GscQuoteToLeBean::fromQuoteToLe).collect(Collectors.toList());
    }

    /**
     * getProductFamily
     *
     * @param quoteBean
     * @return
     */
    private Try<GscQuoteDataBean> getProductFamily(GscQuoteDataBean quoteBean) {
        QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeRepository.findById(quoteBean.getQuoteLeId())
                .map(quoteToLe -> {
                    MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(
                            GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
                    return quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(quoteToLe,
                            mstProductFamily);
                }).orElse(null);
        if (Objects.nonNull(quoteToLeProductFamily)) {
            MstProductFamily mstProductFamily = quoteToLeProductFamily.getMstProductFamily();
            quoteBean.setProductFamilyName(mstProductFamily.getName());

            List<ProductSolution> productSolutions = productSolutionRepository
                    .findByQuoteToLeProductFamily(quoteToLeProductFamily);
            quoteBean.setSolutions(
                    productSolutions.stream().map(this::createProductSolutionBean).collect(Collectors.toList()));
            String accessType = quoteBean.getSolutions().stream().findFirst().map(GscSolutionBean::getAccessType)
                    .orElse(null);
            quoteBean.setAccessType(accessType);
            return Try.success(quoteBean);
        } else {
            return notFoundError(ExceptionConstants.PRODUCT_EMPTY,
                    String.format("Product with QuoteLeId: %s not found", quoteBean.getQuoteLeId()));
        }
    }

    /**
     * createProductSolution
     *
     * @param productSolution
     * @return
     */
    private GscSolutionBean createProductSolutionBean(ProductSolution productSolution) {
        GscSolutionBean gscSolutionBean = new GscSolutionBean();
        gscSolutionBean.setSolutionId(productSolution.getId());
        gscSolutionBean.setSolutionCode(productSolution.getSolutionCode());
        gscSolutionBean.setOfferingName(productSolution.getMstProductOffering().getProductName());
        List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
                GscConstants.STATUS_ACTIVE);
        quoteGscs.stream().findFirst().ifPresent(quoteGsc -> {
            gscSolutionBean.setAccessType(quoteGsc.getAccessType());
            gscSolutionBean.setProductName(quoteGsc.getProductName());
        });
        gscSolutionBean.setGscQuotes(quoteGscs.stream().map(this::fromQuoteGsc).collect(Collectors.toList()));
        return gscSolutionBean;
    }

    public GscQuoteBean fromQuoteGsc(QuoteGsc quoteGsc) {
        Objects.requireNonNull(quoteGsc, QUOTE_GSC_NULL_MESSAGE);
        GscQuoteBean gscQuoteBean = GscQuoteBean.fromQuoteGsc(quoteGsc);
        gscQuoteBean.setConfigurations(getGscQuoteConfigurationBean(quoteGsc));
        return gscQuoteBean;
    }

    private List<GscQuoteConfigurationBean> getGscQuoteConfigurationBean(QuoteGsc quoteGsc) {
        return quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream()
                .map(GscQuoteConfigurationBean::fromGscQuoteDetail)
                .map(gscQuoteConfigurationBean -> populateProductComponents(gscQuoteConfigurationBean,
                        quoteGsc.getQuoteToLe().getQuote().getId(), quoteGsc.getId()))
                .collect(Collectors.toList());
    }

    private GscQuoteConfigurationBean populateProductComponents(GscQuoteConfigurationBean configurationBean,
                                                                Integer quoteId, Integer quoteGscId) {
        List<GscProductComponentBean> components = gscQuoteAttributeService
                .getProductComponentAttributes(quoteId, quoteGscId, configurationBean.getId()).get();
        configurationBean.setProductComponents(components);
        return configurationBean;
    }

    private List<GscQuoteProductComponentsAttributeValueBean> getDefaultQuoteLeAttributes(GscQuoteContext context) {
        Map<String, String> attributes = ImmutableMap.<String, String>builder()
                .put(CONTACT_NAME, context.user.getFirstName())
                .put(CONTACT_EMAIL, context.user.getEmailId())
                .put(CONTACT_ID, context.user.getUsername())
                .put(CONTACT_NO, Optional.ofNullable(context.user.getContactNo()).orElse(""))
                .put(DESIGNATION, Optional.ofNullable(context.user.getDesignation()).orElse(""))
                .put(IS_GSC_MULTI_MACD, context.isGscMultiMacd)
                .put(ATTR_CUSTOMER_SECS_ID, context.secsId)
                .build();
        return attributes.entrySet().stream().map(entry -> {
            GscQuoteProductComponentsAttributeSimpleValueBean bean = new GscQuoteProductComponentsAttributeSimpleValueBean();
            bean.setAttributeName(entry.getKey());
            bean.setAttributeValue(entry.getValue());
            return bean;
        }).collect(Collectors.toList());
    }

    private GscQuoteContext persistDefaultQuoteLeAttributes(GscQuoteContext context) {
        gscQuoteAttributeService.saveQuoteToLeAttributes(context.quote, context.quoteToLe,
                getDefaultQuoteLeAttributes(context));
        return context;
    }

    /**
     * Trigger Mail Notification for Supplier Le Mismatch By using QuoteLeId
     *
     * @param quoteLeId
     * @return
     * @throws TclCommonException
     */
    public String triggerMailNotificationSupplierLeMisMatch(Integer quoteLeId) throws TclCommonException {
        QuoteToLe quoteToLe = quoteToLeRepository.findById(quoteLeId).get();
        String orderRefId = quoteToLe.getQuote().getQuoteCode();
        Integer customerId = userInfoUtils.getCustomerDetails().stream().findFirst().get().getCustomerId();
        User customerUser = userRepository.findByCustomerIdAndStatus(customerId, CommonConstants.ACTIVE).stream()
                .findFirst().get();
        String customerContractEntity = null != getLeAttributes(quoteToLe, CUSTOMER_CONTRACTING_ENTITY)
                ? getLeAttributes(quoteToLe, CUSTOMER_CONTRACTING_ENTITY)
                : customerUser.getCustomer().getCustomerName();
        try {
            notificationService.salesOrdeLeMismatchNotification(customerContractEntity,
                    customerUser.getCustomer().getCustomerName(), customerUser.getUsername(), customerUser.getEmailId(),
                    orderRefId, appHost + adminRelativeUrl);
        } catch (Exception e) {
            throw new TclCommonException(COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
        }
        return GscConstants.SUCCESS;
    }

    private String getLeAttributes(QuoteToLe quoteTole, String attribute) {
        MstOmsAttribute mstOmsAttribute = mstOmsAttributeRepository.findByNameAndIsActive(attribute, CommonConstants.BACTIVE).stream().findFirst().get();
        return quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(quoteTole, mstOmsAttribute).stream().map(QuoteLeAttributeValue::getAttributeValue).findFirst().get();
    }

    /**
     * Wrapper method to populate QuoteToLe
     *
     * @param context
     * @return
     */
    private Try<CreateCurrencyConvertorContext> populateQuoteToLe(CreateCurrencyConvertorContext context) {
        return getQuoteToLe(context.quoteToLeId).map(quoteToLe -> {
            context.quoteToLe = quoteToLe;
            return context;
        });
    }

    /**
     * update the stage status of QuoteToLe
     *
     * @return {@link GscQuoteDataBean}
     * @author VISHESH AWASTHI
     */
    @Transactional
    public GscQuoteToLeBean updateStageStatus(Integer quoteToLeId, String stage) {
        Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
        Objects.requireNonNull(stage, QUOTE_TO_LE_STAGE);
        return getQuoteToLe(quoteToLeId).mapTry(quoteToLe -> updateAllStages(quoteToLe, stage)).get();
    }

    /**
     * Update SFDC and QuoteToLe Stages
     *
     * @param quoteToLe
     * @param stage
     * @return {@link GscQuoteToLeBean}
     * @throws TclCommonException
     */
    private GscQuoteToLeBean updateAllStages(QuoteToLe quoteToLe, String stage) throws TclCommonException {
        if (PROPOSAL_SENT.equalsIgnoreCase(stage) || VERBAL_AGREEMENT_STAGE.equalsIgnoreCase(stage)) {
            updateSFDCStageWithOpportunity(quoteToLe, stage);
        } else if (QuoteStageConstants.valueOf(stage.toUpperCase()).name().equals(stage)) {
            updateQuoteToLeStage(quoteToLe, stage);
        } else {
            updateQuoteToLeStage(quoteToLe, stage);
        }
        return new GscQuoteToLeBean(quoteToLe);
    }

    /**
     * Update SFDC Stage
     *
     * @param quoteToLe
     * @param stage
     * @return {@link GscQuoteToLeBean}
     */
    private GscQuoteToLeBean updateSFDCStageWithOpportunity(QuoteToLe quoteToLe, String stage)
            throws TclCommonException {
        if(userInfoUtils.getUserRoles().contains(OPT_WHOLESALE_NGP_CUSTOMER_PORTAL)) {
            gscOmsSfdcComponent.getOmsSfdcService().processUpdateOpportunity(new Date(), quoteToLe.getTpsSfdcOptyId(),
                    stage, quoteToLe);
        }
        quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
        return new GscQuoteToLeBean(quoteToLe);
    }

    /**
     * saves the stage status
     *
     * @param quoteToLe
     * @param stage
     * @return
     */
    private GscQuoteToLeBean updateQuoteToLeStage(QuoteToLe quoteToLe, String stage) {
        quoteToLe.setStage(QuoteStageConstants.valueOf(stage.toUpperCase()).toString());
        quoteToLeRepository.save(quoteToLe);
        return new GscQuoteToLeBean(quoteToLe);
    }

    /**
     * Attach the Quote pdf in E-Mail
     *
     * @param email
     * @param quoteId
     * @return
     * @author VISHESH AWASTHI
     */
    public ServiceResponse sendQuoteViaEmail(Integer quoteId, String email, HttpServletResponse response) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(email, EMAIL_NULL_MESSAGE);
        String fileName = "Quote_" + quoteId + ".pdf";
        return null;
    }

    /**
     * Delete Gsc Quote and Order
     *
     * @param action
     * @param quoteId
     * @throws TclCommonException
     * @author VISHESH AWASTHI
     */
    @Transactional
    public void deleteQuote(String action, Integer quoteId) throws TclCommonException {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(action, ACTION_NULL_MESSAGE);
        if (!action.equalsIgnoreCase(ACTION_DELETE))
            throw new TclCommonException(COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
        deleteGscQuote(getQuote(quoteId).get());
    }

    /**
     * delete gsc quote details
     *
     * @param quote
     */
    private void deleteGscQuote(Quote quote) {
        Optional.ofNullable(quoteToLeRepository.findByQuote(quote))
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("No quote to le for GSIP found for quote id: %s", quote.getId())))
                .forEach(quoteToLe -> {
                    quoteToLe.getQuoteToLeProductFamilies().forEach(quoteToLeProductFamily -> {
                        List<ProductSolution> savedProductSolutions = productSolutionRepository
                                .findByQuoteToLeProductFamily(quoteToLeProductFamily);
                        deleteProductSolutions(savedProductSolutions);
                    });
                    quoteToLeProductFamilyRepository.deleteAllByQuoteToLe(quoteToLe);
                    quoteLeAttributeValueRepository.deleteAllByQuoteToLe(quoteToLe);
                    quoteToLeRepository.delete(quoteToLe);
                });
        quoteRepository.delete(quote);
    }

    /**
     * deleteProductSolutions
     *
     * @param productSolutions
     */
    private void deleteProductSolutions(List<ProductSolution> productSolutions) {
        productSolutions.forEach(productSolution -> {
            List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
                    GscConstants.STATUS_ACTIVE);
            quoteGscs.forEach(gscQuoteDetailService::deleteQuoteGscAndQuoteGscDetailsByQuoteGsc);
            quoteGscRepository.deleteByProductSolution(productSolution);
            productSolutionRepository.delete(productSolution);
        });
    }

    /**
     * Method to update contract terms of quote
     *
     * @param actionType
     * @param quoteId
     * @param termsInMonths
     * @throws TclCommonException
     */
    @Transactional
    public GscQuoteToLeBean updateTermsInMonths(String actionType, Integer quoteId, String termsInMonths)
            throws TclCommonException {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(actionType, ACTION_NULL_MESSAGE);
        Objects.requireNonNull(termsInMonths, CONTRACT_TERM_NULL_MESSAGE);
        if (!actionType.equalsIgnoreCase(ACTION_UPDATE))
            throw new TclCommonException(COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
        return updateContractTerm(getQuote(quoteId).get(), termsInMonths);
    }

    /**
     * Method to check for quoteToLe and update terms in months.
     *
     * @param quote
     * @param termsInMonths
     */
    private GscQuoteToLeBean updateContractTerm(Quote quote, String termsInMonths) {
        Integer quoteLeId = quote.getQuoteToLes().stream().findFirst().get().getId();
        QuoteToLe quoteToLe = getQuoteToLe(quoteLeId).get();
        quoteToLe.setTermInMonths(termsInMonths);
        quoteToLeRepository.save(quoteToLe);
        GscQuoteToLeBean gscQuoteToLeBean = new GscQuoteToLeBean(quoteToLe);
        return gscQuoteToLeBean;
    }

    public List<String> getMPLSLocations(Integer quoteId, Integer quoteLeId) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);

        return Try.success(createProductLocationContext(quoteLeId, 0))
                .map(this::populateProductLocation)
                .map(this::populateProductLocationDetail)
                .map(this::queueForProductLocationCall)
                .map(context -> context.productLocations).get();
    }

    private GscProductLocationContext createProductLocationContext(Integer quoteLeId, Integer customerId) {
        GscProductLocationContext context = new GscProductLocationContext();
        context.quoteToLeId = quoteLeId;
        context.productName = GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase();
        context.addressDetails = new ArrayList<AddressDetail>();
        context.customerId = customerId;
        return context;

    }

    /**
     * update product service in sfdc
     *
     * @param context
     */
    private GscQuoteContext updateProductServiceInSfdc(GscQuoteContext context) {
        try {
            if(userInfoUtils.getUserRoles().contains(OPT_WHOLESALE_NGP_CUSTOMER_PORTAL)) {
                if (context.isAccessTypeChange = true) {
                    return createProductServiceInSfdc(context);
                } else {
                    gscOmsSfdcComponent.getOmsSfdcService().processUpdateProductForWholesaleGSC(context.quoteToLe);
                }
            }
        } catch (Exception e) {
            throw new TCLException(ExceptionConstants.SFDC_VALIDATION_ERROR, e.getMessage());
        }
        return context;
    }

    /**
     * create product service in sfdc
     *
     * @param context
     */
    private GscQuoteContext createProductServiceInSfdc(GscQuoteContext context) {
        try {
            if(userInfoUtils.getUserRoles().contains(OPT_WHOLESALE_NGP_CUSTOMER_PORTAL)) {
                String productSolutionCode = context.gscQuoteDataBean.getSolutions().stream().findFirst().get().getSolutionCode();
                ProductSolution productSolution = productSolutionRepository.findBySolutionCode(productSolutionCode).stream().findFirst().get();
                gscOmsSfdcComponent.getOmsSfdcService().processProductServiceForSolution(
                        context.quoteToLe,
                        productSolution,
                        context.quoteToLe.getTpsSfdcOptyId());
            }
        } catch (Exception e) {
            throw new TCLException(ExceptionConstants.SFDC_VALIDATION_ERROR, e.getMessage());
        }
        return context;
    }

    private GscProductLocationContext populateProductLocation(GscProductLocationContext context) {
        context.productLocationBeans = GscUtils
                .mapRows(() -> quoteGscRepository.findProductLocations(context.quoteToLeId), this::toProductLocation);
        return context;
    }

    private GscProductLocationContext populateProductLocationDetail(GscProductLocationContext context) {
        for (GscProductLocationBean productLocationBean : context.productLocationBeans) {
            if (productLocationBean.getProductName().equalsIgnoreCase(DOMESTIC_VOICE)) {
                context.serviceName = productLocationBean.getProductName();
                context.country = productLocationBean.getOrigin();
                break;
            } else if (productLocationBean.getProductName().equalsIgnoreCase(GLOBAL_OUTBOUND)
                    && productLocationBean.getDestination().equalsIgnoreCase(INDIA)) {
                context.serviceName = productLocationBean.getProductName();
                context.country = productLocationBean.getDestination();
                break;
            } else {
                context.serviceName = productLocationBean.getProductName();
                context.country = "ALL";
            }
        }
        return context;
    }

    private GscProductLocationBean toProductLocation(Map<String, Object> row) {
        GscProductLocationBean bean = new GscProductLocationBean();
        bean.setOrigin((String) row.getOrDefault("origin", ""));
        bean.setDestination((String) row.getOrDefault("destination", ""));
        bean.setProductName((String) row.get("product_name"));
        return bean;
    }

    private GscProductLocationContext queueForProductLocationCall(GscProductLocationContext context) {
        try {
            String response = (String) mqUtils.sendAndReceive(productLocationQueue,
                    createProductLocationQueueRequest(context));
            context.productLocations = GscUtils.fromJson(response, List.class);
        } catch (Exception e) {
            LOGGER.info("Product Location Queue Exception :: {}", e.getMessage());
        }
        return context;
    }

    private String createProductLocationQueueRequest(GscProductLocationContext context) {
        return GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase() + CommonConstants.COMMA + context.serviceName
                + CommonConstants.COMMA + context.country;
    }

    public List<AddressDetail> getLocationDetailsByCustomer(Integer quoteId, Integer quoteLeId, Integer customerId) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);

        return Try.success(createProductLocationContext(quoteLeId, customerId))
                .map(this::populateProductLocation)
                .map(this::populateProductLocationDetail)
                .map(this::queueForCustomerLocationCall)
                .map(this::updateAddressBasedOnConfiguration)
                .map(context -> context.addressDetails).get();
    }

    private GscProductLocationContext queueForCustomerLocationCall(GscProductLocationContext context) {
        try {
            String response = (String) mqUtils.sendAndReceive(customerLocationQueue,
                    String.valueOf(context.customerId));

            LOGGER.info("Legal Entity Location Response :: {} ", response);

            context.addressDetails = GscUtils.fromJson(response, new TypeReference<List<AddressDetail>>() {
            });

            LOGGER.info("After conversation Legal Entity Location Response :: {}", context.addressDetails.toString());
        } catch (Exception e) {
            LOGGER.warn("Customer Location Queue Exception :: {}", e.getMessage());
        }
        return context;
    }

    private GscProductLocationContext updateAddressBasedOnConfiguration(GscProductLocationContext context) {
        if (context.serviceName.equalsIgnoreCase(DOMESTIC_VOICE)) {
            context.addressDetails = context.addressDetails.stream().filter(addressDetail ->
                    context.country.equalsIgnoreCase(addressDetail.getCountry())).collect(Collectors.toList());
        } else if (context.serviceName.equalsIgnoreCase(GLOBAL_OUTBOUND) && context.country.equalsIgnoreCase(INDIA)) {
            context.addressDetails = context.addressDetails.stream().filter(addressDetail ->
                    !context.country.equalsIgnoreCase(addressDetail.getCountry())).collect(Collectors.toList());
        }
        return context;
    }

    @Transactional
    public void moveCountryFilesToObjectStorage() throws TclCommonException {
        ObjectStorageListenerBean objectListenerBean = new ObjectStorageListenerBean();
        List<Integer> attachmentIdsList = new ArrayList<>();
        List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findCountryDocuments();
        if (!omsAttachmentList.isEmpty()) {
            omsAttachmentList.stream().forEach(omsAttachment -> {
                attachmentIdsList.add(omsAttachment.getErfCusAttachmentId());
                omsAttachment.setReferenceName(omsAttachment.getReferenceName() + "_OBJECT");
                omsAttachmentRepository.save(omsAttachment);
            });
        }
        objectListenerBean.setAttachmentIds(attachmentIdsList);
        String request = Utils.convertObjectToJson(objectListenerBean);
        mqUtils.sendAndReceive(objectStorageMoveCountryFiles, request);
    }

    public String updateExistingCurrencyBasedOnService(QuoteToLe quoteToLe, String existingCurrency) {
        List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe).stream()
                .filter(quoteGsc -> (ACANS.equalsIgnoreCase(quoteGsc.getProductName()) || ACDTFS.equalsIgnoreCase(quoteGsc.getProductName())))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(quoteGscs)) {
            existingCurrency = quoteToLe.getCurrencyCode();
        }
        return existingCurrency;
    }

    /**
     * Process Delegate notification
     *
     * @param triggerEmailRequest
     * @param initiatorIp
     * @return
     * @throws TclCommonException
     */
    public TriggerEmailResponse processTriggerMail(TriggerEmailRequest triggerEmailRequest, String initiatorIp)
            throws TclCommonException {
        TriggerEmailResponse triggerEmailResponse = new TriggerEmailResponse(Status.SUCCESS.toString());
        try {

            String userId = triggerEmailRequest.getEmailId();
            int quoteToLeId = triggerEmailRequest.getQuoteToLeId();
            validateTriggerInput(userId, quoteToLeId);
            User user = userRepository.findByEmailIdAndStatus(userId, 1);
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
            if (quoteToLe.isPresent()) {
                updateContactInfo(user, quoteToLe.get());

                Optional<QuoteDelegation> quoteDelExists = quoteDelegationRepository
                        .findByQuoteToLeAndAssignToAndStatus(quoteToLe.get(), user.getId(),
                                OPEN.toString());
                if (!quoteDelExists.isPresent()) {
                    QuoteDelegation quoteDelegation = new QuoteDelegation();
                    quoteDelegation.setAssignTo(user.getId());
                    quoteDelegation.setInitiatedBy(user.getCustomer().getId());
                    quoteDelegation.setParentId(0);
                    quoteDelegation.setStatus(OPEN.toString());
                    quoteDelegation.setType(OTHERS.toString());
                    quoteDelegation.setRemarks("");
                    quoteDelegation.setIpAddress(initiatorIp);
                    quoteDelegation.setIsActive((byte) 1);
                    quoteDelegation.setQuoteToLe(quoteToLe.get());
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    quoteDelegation.setCreatedTime(timestamp);
                    quoteDelegationRepository.save(quoteDelegation);
                }
                String orderRefId = quoteToLe.get().getQuote().getQuoteCode();
                User customerUser = userRepository.findByEmailIdAndStatus(triggerEmailRequest.getEmailId(), 1);
                String leMail = getLeAttributes(quoteToLe.get(), LE_EMAIL);
                String pilotMail = Objects.nonNull(pilotTeamMail) ? pilotTeamMail[0] : null;
                String accManager = (Objects.isNull(leMail) || leMail.equals(HYPHEN)) ? pilotMail : leMail;

                MailNotificationBean mailNotificationBean = populateMailNotificationBean(
                        getLeAttributes(quoteToLe.get(), CUSTOMER_CONTRACTING_ENTITY),
                        customerUser.getCustomer().getCustomerName(), customerUser.getUsername(),
                        customerUser.getContactNo(), triggerEmailRequest.getEmailId(), accManager, orderRefId,
                        appHost + adminRelativeUrl, quoteToLe.get());
                notificationService.cofDelegationNotification(mailNotificationBean);

                MailNotificationBean mailNotificationBeanCofDelegate = populateMailNotificationBeanCofDelegate(quoteToLe.get(), triggerEmailRequest, user, orderRefId, accManager);
                notificationService.cofCustomerDelegationNotification(mailNotificationBeanCofDelegate);
            }

        } catch (Exception e) {
            throw new TclCommonException(COMMON_ERROR, e, R_CODE_ERROR);
        }
        return triggerEmailResponse;
    }

    private void validateTriggerInput(String userId, Integer quoteId) throws TclCommonException {
        if (Objects.isNull(userId) || Objects.isNull(quoteId)) {
            throw new TclCommonException(COMMON_ERROR, R_CODE_ERROR);
        }
        Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteId);
        if (!quoteToLe.isPresent()) {
            throw new TclCommonException(COMMON_ERROR, R_CODE_ERROR);
        }
    }

    private void updateContactInfo(User user, QuoteToLe quoteToLe) {
        if (Objects.nonNull(user)) {
            if (quoteToLe.getQuoteLeAttributeValues() != null && !quoteToLe.getQuoteLeAttributeValues().isEmpty()) {
                quoteToLe.getQuoteLeAttributeValues().forEach(quoteLeAttributeValue -> {
                    if (CONTACT_ID.equals(quoteLeAttributeValue.getMstOmsAttribute().getName())) {
                        quoteLeAttributeValue.setAttributeValue(String.valueOf(user.getId()));
                        quoteLeAttributeValueRepository.save(quoteLeAttributeValue);

                    } else if (CONTACT_NAME.equals(quoteLeAttributeValue.getMstOmsAttribute().getName())) {
                        quoteLeAttributeValue.setAttributeValue(user.getFirstName());
                        quoteLeAttributeValueRepository.save(quoteLeAttributeValue);

                    } else if (CONTACT_EMAIL.equals(quoteLeAttributeValue.getMstOmsAttribute().getName())) {
                        quoteLeAttributeValue.setAttributeValue(user.getEmailId());
                        quoteLeAttributeValueRepository.save(quoteLeAttributeValue);

                    } else if (CONTACT_NO.equals(quoteLeAttributeValue.getMstOmsAttribute().getName())) {
                        quoteLeAttributeValue.setAttributeValue(user.getContactNo());
                        quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
                    } else if (DESIGNATION.equals(quoteLeAttributeValue.getMstOmsAttribute().getName())) {
                        quoteLeAttributeValue.setAttributeValue(user.getDesignation());
                        quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
                    }
                });
            }
        }
    }

    private MailNotificationBean populateMailNotificationBean(String customerAccountName, String customerName, String userName,
                                                              String userContactNumber, String userEmail, String accountManagerEmail,
                                                              String orderRefId, String quoteLink, QuoteToLe quoteToLe) throws TclCommonException {
        MailNotificationBean mailNotificationBean = new MailNotificationBean();

        mailNotificationBean.setCustomerAccountName(customerAccountName);
        mailNotificationBean.setCustomerName(customerName);
        mailNotificationBean.setUserName(userName);
        mailNotificationBean.setUserContactNumber(userContactNumber);
        mailNotificationBean.setUserEmail(userEmail);
        mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
        mailNotificationBean.setOrderId(orderRefId);
        mailNotificationBean.setQuoteLink(quoteLink);
        mailNotificationBean.setProductName(GSIP_PRODUCT_NAME);
        return mailNotificationBean;
    }

    private MailNotificationBean populatePartnerClassification(QuoteToLe quoteToLe, MailNotificationBean mailNotificationBean) {
        try {
            String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
                    String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
            CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
                    .convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

            String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails()
                    .stream().findAny().get().getLegalEntityName();

            LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

            mailNotificationBean.setClassification(quoteToLe.getClassification());
            mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
        } catch (Exception e) {
            LOGGER.warn("Error while reading end customer name :: {}", e.getStackTrace());
        }
        return mailNotificationBean;
    }

    private MailNotificationBean populateMailNotificationBeanCofDelegate(QuoteToLe quoteToLe, TriggerEmailRequest triggerEmailRequest, User user, String orderRefId, String leMail) {
        MailNotificationBean mailNotificationBeanCofDelegate = new MailNotificationBean();
        mailNotificationBeanCofDelegate.setCustomerName(user.getFirstName());
        mailNotificationBeanCofDelegate.setUserEmail(triggerEmailRequest.getEmailId());
        mailNotificationBeanCofDelegate.setAccountManagerEmail(leMail);
        mailNotificationBeanCofDelegate.setOrderId(orderRefId);
        mailNotificationBeanCofDelegate.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
        mailNotificationBeanCofDelegate.setProductName(GSIP_PRODUCT_NAME);
        if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
            mailNotificationBeanCofDelegate = populatePartnerClassification(quoteToLe, mailNotificationBeanCofDelegate);
        }
        return mailNotificationBeanCofDelegate;
    }

    /**
     * Cof Download Mail Notification
     *
     * @param quoteId
     * @param quoteToLeId
     * @return
     * @throws TclCommonException
     */
    public String cofDownloadAccountManagerNotification(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(quoteToLeId, QUOTE_ID_NULL_MESSAGE);
        return Try.success(createMailBeanContext(quoteId, quoteToLeId))
                .flatMap(this::populateQuote)
                .flatMap(this::populateQuoteToLe)
                .map(this::populateUserInfo)
                .mapTry(this::populateMailNotificationBeanForCofDownload)
                .map(this::triggerMailNotification)
                .get().response;
    }

    private GscQuoteContext createMailBeanContext(Integer quoteId, Integer quoteToLeId) {
        GscQuoteContext context = new GscQuoteContext();
        context.gscQuoteDataBean = new GscQuoteDataBean();
        context.gscQuoteDataBean.setQuoteId(quoteId);
        context.gscQuoteDataBean.setQuoteLeId(quoteToLeId);
        return context;
    }

    private GscQuoteContext populateUserInfo(GscQuoteContext context) {
        String userName = Utils.getSource();
        User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
        context.user = user;
        return context;
    }

    private GscQuoteContext populateMailNotificationBeanForCofDownload(GscQuoteContext context) throws TclCommonException {
        String leMail = getLeAttributes(context.quoteToLe, LE_EMAIL);
        String pilotMail = Objects.nonNull(pilotTeamMail) ? pilotTeamMail[0] : null;
        String accManager = (Objects.isNull(leMail) || leMail.equals(HYPHEN)) ? pilotMail : leMail;
        MailNotificationBean mailNotificationBean = populateMailNotificationBean(
                getLeAttributes(context.quoteToLe, LeAttributesConstants.LEGAL_ENTITY_NAME.toString()),
                context.quoteToLe.getQuote().getCustomer().getCustomerName(), context.user.getFirstName(), context.user.getContactNo(),
                getLeAttributes(context.quoteToLe, LeAttributesConstants.LE_EMAIL.toString()), accManager,
                context.quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl, context.quoteToLe);
        context.mailNotificationBean = mailNotificationBean;
        return context;
    }

    private GscQuoteContext triggerMailNotification(GscQuoteContext context) {
        try {
            boolean flag = notificationService.cofDownloadNotification(context.mailNotificationBean);
            if (flag) {
                LOGGER.info("Cof Download Notification Sent Sucessfully");
                context.response = "Success";
            } else {
                LOGGER.info("Cof Download Notification Failure");
            }
        } catch (TclCommonException e) {
            LOGGER.warn("Error Occured while sending notification :: {}", e.getStackTrace());
        }
        return context;
    }

    private String setAccountOwnerName(String partnerId) throws TclCommonException {
        String accountOwnerName = "";
        try {
            String accountOwnerNameAndEmail = (String) mqUtils.sendAndReceive(partnerAccountNameMQ, partnerId);
            accountOwnerName = accountOwnerNameAndEmail.split(COMMA)[0].trim();
        } catch (TclCommonException e) {
            LOGGER.warn("Error Occoured while fetching account owner name for partner id :: {} and error is :: {}", partnerId, e.getStackTrace());
        }
        return accountOwnerName;
    }

    /**
     * Update quote to le with service id's
     *
     * @param quoteId
     * @param quoteLeId
     * @param serviceIds
     */
    public GscQuoteToLeBean updateQuoteToLeWithServiceIds(Integer quoteId, Integer quoteLeId, List<String> serviceIds) {
        Objects.requireNonNull(quoteId, "QuoteId cannot be null");
        Objects.requireNonNull(quoteLeId, "QuoteLeId cannot be null");
        GscQuoteToLeBean quoteToLeBean = null;
        if (!CollectionUtils.isEmpty(serviceIds)) {
            String combinedServiceIds = String.join(",", serviceIds);
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
            if (quoteToLe.isPresent()) {
                quoteToLe.get().setErfServiceInventoryTpsServiceId(combinedServiceIds);
                quoteToLeRepository.save(quoteToLe.get());
                quoteToLeBean = new GscQuoteToLeBean(quoteToLe.get());
                LOGGER.info("Service ids saved against quoteLe {} are {}", quoteToLe, combinedServiceIds);
            }
        }
        return quoteToLeBean;
    }

    /**
     * Method to update quote le attribute values
     *
     * @param quoteId
     * @param quoteLeId
     * @param quoteLeAttributeBeans
     * @return {@link List<GscQuoteLeAttributeBean>}}
     */
    public List<GscQuoteLeAttributeBean> updateQuoteToLeAttributes(Integer quoteId, Integer quoteLeId, List<GscQuoteLeAttributeBean> quoteLeAttributeBeans) {
        if (!CollectionUtils.isEmpty(quoteLeAttributeBeans)) {
            quoteLeAttributeBeans.stream().forEach(quoteLeAttributeBean -> {
                QuoteLeAttributeValue quoteLeAttribute = quoteLeAttributeValueRepository.findByQuoteIDAndMstOmsAttributeName(quoteId,
                        quoteLeAttributeBean.getAttributeName());
                if (Objects.nonNull(quoteLeAttribute)) {
                    LOGGER.info("Updating quote le attribute of id {} ", quoteLeAttribute.getId());
                } else {
                    LOGGER.info("Creating quote le attribute with quotetoleid {} and mst oms attribute name {} ", quoteLeId, quoteLeAttributeBean.getAttributeName());
                    quoteLeAttribute = new QuoteLeAttributeValue();
                    quoteLeAttribute.setQuoteToLe(quoteToLeRepository.findById(quoteLeId).get());
                    quoteLeAttribute.setMstOmsAttribute(mstOmsAttributeRepository.findByNameAndIsActive(quoteLeAttributeBean.getAttributeName(), (byte) 1).stream().findFirst().get());
                }
                AdditionalServiceParams additionalServiceParams = saveAdditionalServiceParamsForMultiMacd(quoteId, quoteLeAttributeBean);
                LOGGER.info("AdditionalServiceParams ID :: {}", additionalServiceParams.getId());
                quoteLeAttribute.setAttributeValue(String.valueOf(additionalServiceParams.getId()));
                quoteLeAttributeValueRepository.save(quoteLeAttribute);
                quoteLeAttributeBean.setId(quoteLeAttribute.getId());
            });
        }
        return quoteLeAttributeBeans;
    }

    private AdditionalServiceParams saveAdditionalServiceParamsForMultiMacd(Integer quoteId, GscQuoteLeAttributeBean quoteLeAttributeBean) {
        AdditionalServiceParams additionalServiceParams = new AdditionalServiceParams();
        additionalServiceParams.setReferenceId(String.valueOf(quoteId));
        additionalServiceParams.setCategory("QuoteId");
        additionalServiceParams.setValue(quoteLeAttributeBean.getAttributeValue());
        additionalServiceParams.setCreatedBy(Utils.getSource());
        additionalServiceParams.setCreatedTime(new Date());
        additionalServiceParams.setIsActive(CommonConstants.Y);
        additionalServiceParams.setAttribute(mstOmsAttributeRepository.findByNameAndIsActive(quoteLeAttributeBean.getAttributeName(), (byte) 1)
                .stream().findFirst().get().getName());
        additionalServiceParams = additionalServiceParamRepository.save(additionalServiceParams);
        return additionalServiceParams;
    }

    /**
     * Method to get multi macd service details
     *
     * @param quoteId
     * @param quoteLeId
     * @return
     */
    public GscSipTrunkAttributeBean getMultiMacdServiceDetails(Integer quoteId, Integer quoteLeId) {
        GscSipTrunkAttributeBean gscSipTrunkAttributeBean = new GscSipTrunkAttributeBean();
        QuoteLeAttributeValue multiMacdGscServiceAttributeValue = quoteLeAttributeValueRepository.findByQuoteIDAndMstOmsAttributeName(quoteId, MULTI_MACD_GSC_SERVICE_DETAILS);
        if (Objects.nonNull(multiMacdGscServiceAttributeValue)) {
            LOGGER.info("Setting service Attributes from Additional Service Params :: {}", multiMacdGscServiceAttributeValue.getAttributeValue());
            AdditionalServiceParams additionalServiceParams = additionalServiceParamRepository.findById(Integer.valueOf(multiMacdGscServiceAttributeValue.getAttributeValue())).get();
            gscSipTrunkAttributeBean.setMultiMacdGscServiceDetails(additionalServiceParams.getValue());
        }
        return gscSipTrunkAttributeBean;
    }

    private static class GscQuoteContext {
        User user;
        Customer customer;
        Quote quote;
        QuoteToLe quoteToLe;
        GscQuoteDataBean gscQuoteDataBean;
        MstProductFamily productFamily;
        QuoteToLeProductFamily quoteToLeProductFamily;
        Integer supplierLegalId;
        Opportunity opportunity;
        Integer customerLeId;
        MailNotificationBean mailNotificationBean;
        String response;
        boolean isAccessTypeChange = false;
        String paymentCurrency;
        String isGscMultiMacd = "No";
        String secsId;
        String quoteCategory;
    }

    /**
     * Context class for convert currency
     *
     * @author VISHESH AWASTHI
     */
    private static class CreateCurrencyConvertorContext {
        String existingCurrency;
        String inputCurrency;
        Integer quoteToLeId;
        QuoteToLe quoteToLe;
        List<QuoteGsc> quoteGsc;
        MstOmsAttribute mstOmsAttribute;
        QuoteLeAttributeValue quoteToLeAttribute;
    }

    private static class GscProductLocationContext {
        Integer quoteToLeId;
        String productName = "";
        String country = "";
        String serviceName = "";
        List<String> productLocations;
        GscProductLocationBean productLocationBean;
        List<GscProductLocationBean> productLocationBeans;
        List<AddressDetail> addressDetails;
        Integer customerId;
    }

    /**
     * Get Interconnect details by quote id
     *
     * @param quoteId
     * @param quoteLeId
     * @return
     */
    public List<GscWholesaleInterconnectBean> getInterconnectDetails(Integer quoteId, Integer quoteLeId) {
        List<GscWholesaleInterconnectBean> gscWholesaleInterconnectBean = new ArrayList<>();
        QuoteLeAttributeValue customerSecsId = quoteLeAttributeValueRepository.findByQuoteIDAndMstOmsAttributeName(quoteId, ATTR_CUSTOMER_SECS_ID);
        if (Objects.nonNull(customerSecsId)) {
            LOGGER.info("Customer SECS ID :: {}", customerSecsId.getAttributeValue());
            try {
                String response = (String) mqUtils.sendAndReceive(interconnectQueue, customerSecsId.getAttributeValue());

                LOGGER.info("Interconnect Response :: {} ", response);

                gscWholesaleInterconnectBean = GscUtils.fromJson(response, new TypeReference<List<GscWholesaleInterconnectBean>>() {
                });

            } catch(Exception e) {
                LOGGER.warn("Interconnect Not Found for Secs ID :: {}",customerSecsId.getAttributeValue(), e.getCause());
            }
        }
        return gscWholesaleInterconnectBean;
    }

}
