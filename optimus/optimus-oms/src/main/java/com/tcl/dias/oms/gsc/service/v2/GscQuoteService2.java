package com.tcl.dias.oms.gsc.service.v2;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_EMAIL;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_ID;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_NAME;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_NO;
import static com.tcl.dias.common.constants.LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY;
import static com.tcl.dias.common.constants.LeAttributesConstants.INR;
import static com.tcl.dias.common.constants.LeAttributesConstants.USD;
import static com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACTION_DELETE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACTION_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACTION_UPDATE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONTRACT_TERM_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.EMAIL_NULL_MESSAGE;
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
import static org.jsoup.helper.StringUtil.isNumeric;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gsc.common.GscOmsSfdcComponent;
import com.tcl.dias.oms.gsc.exception.ObjectNotFoundException;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscSlaService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.webex.common.UcaasOmsSfdcComponent;
import com.tcl.dias.oms.webex.service.WebexQuoteService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Services to handle all quote related functionality
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class GscQuoteService2 {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscQuoteService2.class);

	@Autowired
	NotificationService notificationService;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	GscOmsSfdcComponent gscOmsSfdcComponent;

	@Autowired
	GscQuotePdfService2 gscQuotePdfService2;

	@Autowired
	GscQuoteAttributeService2 gscQuoteAttributeService2;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	DocusignAuditRepository docusignAuditRepository;

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
	WebexQuoteService webexQuoteService;

	@Autowired
	UcaasOmsSfdcComponent ucaasOmsSfdcComponent;

	@Autowired
	UserService userService;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customer.currency.queue}")
	private String customerCurrencyQueue;

	@Value("${rabbitmq.supplier.currency.queue}")
	private String supplierCurrencyQueue;

	@Autowired
	PartnerService partnerService;

	@Autowired
	GscQuoteAttributeService gscQuoteAttributeService;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	GscSlaService gscSlaService;

	@Autowired
	GscQuoteDetailService gscQuoteDetailService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	UserRepository userRepository;

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	/**
	 * update the stage status of QuoteToLe
	 *
	 * @param quoteToLeId
	 * @param stage
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public GscQuoteToLeBean updateStageStatus(Integer quoteToLeId, String stage) throws TclCommonException {
		Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
		Objects.requireNonNull(stage, QUOTE_TO_LE_STAGE);
		QuoteToLe quoteToLe = getQuoteToLe(quoteToLeId);
		return updateAllStages(quoteToLe, stage);
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
		if (PROPOSAL_SENT.equalsIgnoreCase(stage)) {
			if (WebexConstants.UCAAS_FAMILY_NAME.equals(webexQuoteService.getProductFamily(quoteToLe))) {
				updateSFDCStageWithOpportunityForUcaas(quoteToLe, stage);
			} else {
				updateSFDCStageWithOpportunity(quoteToLe, stage);
			}
		} else if (QuoteStageConstants.valueOf(stage.toUpperCase()).name().equals(stage)) {
			updateQuoteToLeStage(quoteToLe, stage);
		} else {
			updateQuoteToLeStage(quoteToLe, stage);
		}
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
	 * Update SFDC Stage
	 *
	 * @param quoteToLe
	 * @param stage
	 * @return {@link GscQuoteToLeBean}
	 */
	private GscQuoteToLeBean updateSFDCStageWithOpportunity(QuoteToLe quoteToLe, String stage)
			throws TclCommonException {
		gscOmsSfdcComponent.getOmsSfdcService().processUpdateOpportunity(new Date(), quoteToLe.getTpsSfdcOptyId(),
				stage, quoteToLe);
		quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
		return new GscQuoteToLeBean(quoteToLe);
	}

	/**
	 * Update SFDC Stage
	 *
	 * @param quoteToLe
	 * @param stage
	 * @return {@link GscQuoteToLeBean}
	 */
	private GscQuoteToLeBean updateSFDCStageWithOpportunityForUcaas(QuoteToLe quoteToLe, String stage)
			throws TclCommonException {
		ucaasOmsSfdcComponent.getOmsSfdcService().processUpdateOpportunity(new Date(), quoteToLe.getTpsSfdcOptyId(),
				stage, quoteToLe);
		quoteToLe.setStage(QuoteStageConstants.CHECKOUT.getConstantCode());
		quoteToLeRepository.save(quoteToLe);
		return new GscQuoteToLeBean(quoteToLe);
	}

	/**
	 * Get quote to le by ID
	 *
	 * @param quoteToLeId
	 * @return {@link QuoteToLe}
	 * @throws TclCommonException
	 */
	public QuoteToLe getQuoteToLe(Integer quoteToLeId) throws TclCommonException {
		Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (Objects.isNull(quoteToLe.get())) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quoteToLe.get();
	}

	/**
	 * getQuoteData
	 *
	 * @param quoteId
	 * @return
	 */
	public GscQuoteDataBean getQuoteData(Integer quoteId) throws TclCommonException {
		GscQuoteDataBean quoteBean = new GscQuoteDataBean();

		Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
		if (!Objects.isNull(quote)) {
			quoteBean.setQuoteId(quoteId);
			quoteBean.setQuoteCode(quote.getQuoteCode());
			quoteBean.setCustomerId(quote.getCustomer().getErfCusCustomerId());
			quoteBean.setEngagementOptyId(quote.getEngagementOptyId());
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}

		final List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		if (!quoteToLes.isEmpty()) {
			quoteBean.setQuoteLeId(quoteToLes.get(0).getId());
			quoteBean.setQuoteType(quoteToLes.get(0).getQuoteType());
			quoteBean.setClassification(quoteToLes.get(0).getClassification());
			if (Objects.nonNull(quoteBean.getClassification())
					&& (PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteBean.getClassification())
					|| (PartnerConstants.SELL_THROUGH_CLASSIFICATION
					.equalsIgnoreCase(quoteBean.getClassification())))) {
				Opportunity opportunity = opportunityRepository.findByUuid(quote.getQuoteCode());
				if (Objects.nonNull(opportunity)) {
					quoteBean.setPartnerOptyExpectedArc(opportunity.getExpectedMrc());
					quoteBean.setPartnerOptyExpectedNrc(opportunity.getExpectedNrc());
					quoteBean.setPartnerOptyExpectedCurrency(opportunity.getExpectedCurrency());
				}
			}
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		quoteBean.setLegalEntities(getLegalEntities(quote));

		CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quote.getQuoteCode(),
				Source.MANUAL_COF.getSourceType());
		quoteBean.setManualCofSigned(null != cofDetail);
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
		quoteBean.setDocusign(docusignAudit != null);
		return quoteBean;
	}

	public List<GscQuoteToLeBean> getLegalEntities(Quote quote) {
		return Optional.ofNullable(quoteToLeRepository.findByQuote(quote)).orElse(ImmutableList.of()).stream()
				.map(GscQuoteToLeBean::fromQuoteToLe).collect(Collectors.toList());
	}

	public GscQuoteBean fromQuoteGsc(QuoteGsc quoteGsc) {
		Objects.requireNonNull(quoteGsc, QUOTE_GSC_NULL_MESSAGE);
		GscQuoteBean gscQuoteBean = GscQuoteBean.fromQuoteGsc(quoteGsc);
		gscQuoteBean.setConfigurations(getGscQuoteConfigurationBean(quoteGsc));
		return gscQuoteBean;
	}

	private GscQuoteConfigurationBean populateProductComponents(GscQuoteConfigurationBean configurationBean,
																Integer quoteId, Integer quoteGscId) throws TclCommonException {
		List<GscProductComponentBean> components = gscQuoteAttributeService2.getProductComponentAttributes(quoteId,
				quoteGscId, configurationBean.getId());
		configurationBean.setProductComponents(components);
		return configurationBean;
	}

	private List<GscQuoteConfigurationBean> getGscQuoteConfigurationBean(QuoteGsc quoteGsc) {
		return quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream()
				.map(GscQuoteConfigurationBean::fromGscQuoteDetail).map(gscQuoteConfigurationBean -> {
					try {
						return populateProductComponents(gscQuoteConfigurationBean,
								quoteGsc.getQuoteToLe().getQuote().getId(), quoteGsc.getId());
					} catch (TclCommonException e) {
						LOGGER.warn("Exception occured : {}", e.getMessage());
					}
					return null;
				}).collect(Collectors.toList());
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

	/**
	 * getProductFamily
	 *
	 * @param quoteBean
	 * @return
	 */
	private GscQuoteDataBean getProductFamily(GscQuoteDataBean quoteBean) throws TclCommonException {
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
			return quoteBean;
		} else {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	public GscQuoteDataBean setRatesForConfigurations(GscQuoteDataBean quoteBean) {

		quoteBean.getSolutions().forEach(gscSolutionBean -> gscSolutionBean.getGscQuotes()
				.forEach(gscQuoteBean -> gscQuoteBean.getConfigurations()
						.forEach(gscQuoteConfigurationBean -> gscQuoteConfigurationBean.getProductComponents().stream()
								.findFirst()
								.ifPresent(gscProductComponentBean -> gscProductComponentBean.getAttributes().forEach(
										gscQuoteProductComponentsAttributeValueBean -> pickRatePerMinuteFromAttributes(
												gscQuoteConfigurationBean,
												gscQuoteProductComponentsAttributeValueBean))))));
		return quoteBean;
	}

	private void pickRatePerMinuteFromAttributes(GscQuoteConfigurationBean gscQuoteConfigurationBean,
												 GscQuoteProductComponentsAttributeValueBean gscQuoteProductComponentsAttributeValueBean) {
		if ((RATE_PER_MINUTE_FIXED).equalsIgnoreCase(gscQuoteProductComponentsAttributeValueBean.getAttributeName())) {
			if (!StringUtils.isEmpty(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
				if (isNumeric(gscQuoteProductComponentsAttributeValueBean.getValueString())) {
					gscQuoteConfigurationBean.setRatePerMinFixed(
							Double.valueOf(gscQuoteProductComponentsAttributeValueBean.getValueString()));
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
	 * Get quote gsc by ID
	 *
	 * @param quoteId
	 * @return {@link GscQuoteDataBean}
	 */
	public GscQuoteDataBean getGscQuoteById(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		GscQuoteDataBean gscQuoteDataBean = getQuoteData(quoteId);
		getProductFamily(gscQuoteDataBean);
		setRatesForConfigurations(gscQuoteDataBean);
		return gscQuoteDataBean;
	}

	/**
	 * Attach the Quote pdf in E-Mail
	 *
	 * @param email
	 * @param quoteId
	 * @return
	 *
	 */
	public ServiceResponse sendQuoteViaEmail(Integer quoteId, String email, HttpServletResponse response)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(email, EMAIL_NULL_MESSAGE);
		String fileName = "Quote_" + quoteId + ".pdf";
		String quotePdf = gscQuotePdfService2.processQuoteHtml(quoteId, response);
		notificationService.processShareQuoteNotification(email,
				java.util.Base64.getEncoder().encodeToString(quotePdf.getBytes()), userInfoUtils.getUserFullName(),
				fileName, CommonConstants.GSC);
		ServiceResponse serviceResponse = new ServiceResponse();
		serviceResponse.setFileName(fileName);
		serviceResponse.setStatus(Status.SUCCESS);
		return serviceResponse;
	}

	/**
	 * Get quote by id
	 *
	 * @param quoteId
	 * @return {@link Quote}
	 */
	public Quote getQuote(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
		if (Objects.isNull(quote)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quote;
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
		return updateContractTerm(getQuote(quoteId), termsInMonths);
	}

	/**
	 * Method to check for quoteToLe and update terms in months.
	 *
	 * @param quote
	 * @param termsInMonths
	 */
	private GscQuoteToLeBean updateContractTerm(Quote quote, String termsInMonths) throws TclCommonException {
		Integer quoteLeId = quote.getQuoteToLes().stream().findFirst().get().getId();
		QuoteToLe quoteToLe = getQuoteToLe(quoteLeId);
		quoteToLe.setTermInMonths(termsInMonths);
		quoteToLeRepository.save(quoteToLe);
		GscQuoteToLeBean gscQuoteToLeBean = new GscQuoteToLeBean(quoteToLe);
		return gscQuoteToLeBean;
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
	}

	/**
	 * Method to create context.
	 *
	 * @param request
	 * @return
	 */
	private GscQuoteContext createContext(GscQuoteDataBean request) {
		Integer customerId = request.getCustomerId();
		GscQuoteContext context = new GscQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
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
		return context;
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
	 * Method to construct quote.
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
		quote.setQuoteCode(
				null != context.gscQuoteDataBean.getEngagementOptyId() ? context.gscQuoteDataBean.getQuoteCode()
						: Utils.generateRefId(GscConstants.GSC_PRODUCT_NAME.toUpperCase()));
		return quote;
	}

	private GscQuoteContext setPaymentCurrency(GscQuoteContext context) {
		final String productName = context.gscQuoteDataBean.getSolutions().stream().findFirst().get().getProductName();
		if (ACANS.equalsIgnoreCase(productName) || ACDTFS.equalsIgnoreCase(productName)) {
			context.paymentCurrency = INR;
		}

		if (ORDER_TYPE_MACD.equalsIgnoreCase(context.gscQuoteDataBean.getQuoteType())
				&& Objects.nonNull(context.gscQuoteDataBean.getQuoteCategory())) {
			try {
				if (DOMESTIC_VOICE.equalsIgnoreCase(productName)) {
					context.paymentCurrency = (String) mqUtils.sendAndReceive(customerCurrencyQueue,
							String.valueOf(context.customerLeId));
				} else {
					context.paymentCurrency = (String) mqUtils.sendAndReceive(supplierCurrencyQueue,
							String.valueOf(context.supplierLegalId));
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
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			quoteToLe.setTpsSfdcOptyId(context.opportunity.getTpsOptyId());
			quoteToLe.setErfCusCustomerLegalEntityId(partnerService
					.getCustomerLeIdFromEngagementOpportunityId(Integer.valueOf(context.quote.getEngagementOptyId())));
		}
		return quoteToLe;
	}

	private List<GscQuoteProductComponentsAttributeValueBean> getDefaultQuoteLeAttributes(GscQuoteContext context) {
		Map<String, String> attributes = ImmutableMap.<String, String>builder()
				.put(CONTACT_NAME, context.user.getFirstName()).put(CONTACT_EMAIL, context.user.getEmailId())
				.put(CONTACT_ID, context.user.getUsername())
				.put(CONTACT_NO, Optional.ofNullable(context.user.getContactNo()).orElse(""))
				.put(LeAttributesConstants.DESIGNATION, Optional.ofNullable(context.user.getDesignation()).orElse(""))
				.put(LeAttributesConstants.IS_GSC_MULTI_MACD, context.isGscMultiMacd).build();
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
	 * Method to fetch mstproductssfamily based on product name.
	 *
	 * @param productName
	 * @return
	 */
	private MstProductFamily fetchMstProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName,
				GscConstants.STATUS_ACTIVE);
		if (Objects.isNull(mstProductFamily)) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;
	}

	/**
	 * Method to populate product family
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext populateProductFamily(GscQuoteContext context) throws TclCommonException {
		context.productFamily = fetchMstProductFamily(context.gscQuoteDataBean.getProductFamilyName());
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
	 * create Opportunity in sfdc
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext createOpportunityInSfdc(GscQuoteContext context) {
		try {
			// Triggering Sfdc Opportunity Creation
			if (Objects.nonNull(context.quoteToLe)
					&& StringUtils.isEmpty(context.gscQuoteDataBean.getEngagementOptyId())) {
				gscOmsSfdcComponent.getOmsSfdcService().processCreateOpty(context.quoteToLe,
						context.gscQuoteDataBean.getProductFamilyName());
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return context;
	}

	/**
	 * Method to create quote.
	 *
	 * @param quoteBean
	 * @return {@link GscQuoteDataBean}
	 */
	@Transactional
	public GscQuoteDataBean createQuote(GscQuoteDataBean quoteBean) throws TclCommonException {
		Objects.requireNonNull(quoteBean, QUOTE_NULL_MESSAGE);

		GscQuoteContext context = createContext(quoteBean);
		saveQuote(context);
		setPaymentCurrency(context);
		saveQuoteToLe(context);
		persistDefaultQuoteLeAttributes(context);
		populateProductFamily(context);
		createQuoteToLeProductFamily(context);
		saveProductSolutions(context);
		createOpportunityInSfdc(context);
		return context.gscQuoteDataBean;
	}

	/**
	 * populateQuote
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext populateQuote(GscQuoteContext context) throws TclCommonException {
		context.quote = getQuote(context.gscQuoteDataBean.getQuoteId());
		return context;
	}

	/**
	 * populateQuoteToLe
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext populateQuoteToLe(GscQuoteContext context) throws TclCommonException {
		context.quoteToLe = getQuoteToLe(context.gscQuoteDataBean.getQuoteLeId());
		return context;
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
	 * Method to populate quotetoleproductfamily
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
			gscOmsSfdcComponent.getOmsSfdcService().processDeleteProduct(quoteToLe,
					productSolutions.stream().findFirst().get());
		}
	}

	/**
	 * update product service in sfdc
	 *
	 * @param context
	 */
	private GscQuoteContext updateProductServiceInSfdc(GscQuoteContext context) throws TclCommonException {
		try {
			if (context.isAccessTypeChange = true) {
				return createProductServiceInSfdc(context);
			} else {
				gscOmsSfdcComponent.getOmsSfdcService().processUpdateProductForGSC(context.quoteToLe);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.SFDC_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
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
			String productSolutionCode = context.gscQuoteDataBean.getSolutions().stream().findFirst().get()
					.getSolutionCode();
			ProductSolution productSolution = productSolutionRepository.findBySolutionCode(productSolutionCode).stream()
					.findFirst().get();
			gscOmsSfdcComponent.getOmsSfdcService().processProductServiceForSolution(context.quoteToLe, productSolution,
					context.quoteToLe.getTpsSfdcOptyId());
		} catch (Exception e) {
			throw new TCLException(ExceptionConstants.SFDC_VALIDATION_ERROR, e.getMessage());
		}
		return context;
	}

	private GscQuoteContext processAccessTypeChange(GscQuoteContext context) {
		QuoteToLeProductFamily quoteToLeProductFamily = context.quoteToLeProductFamily;
		List<ProductSolution> savedProductSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);

		List<QuoteToLeProductFamily> families = quoteToLeProductFamilyRepository
				.findByQuoteToLe(context.quoteToLe.getId());
		if (families.size() > 1) {
			Integer quoteToLeProductFamilyId = families.stream()
					.filter(family -> family.getMstProductFamily().getName().equals(GvpnConstants.GVPN))
					.map(QuoteToLeProductFamily::getId).findFirst().get();
			try {
				gvpnQuoteService.deleteProductFamily(quoteToLeProductFamilyId);
			} catch (TclCommonException e) {
				Throwables.propagate(e);
			}
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
	 * Method for update quote.
	 *
	 * @param quoteBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public GscQuoteDataBean updateQuote(GscQuoteDataBean quoteBean) throws TclCommonException {
		Objects.requireNonNull(quoteBean, QUOTE_NULL_MESSAGE);
		GscQuoteContext context = createContext(quoteBean);
		populateQuote(context);
		populateQuoteToLe(context);
		updateQuoteToLeDetails(context);
		persistDefaultQuoteLeAttributes(context);
		populateProductFamily(context);
		populateQuoteToLeProductFamily(context);
		checkAccessTypeAndProcessProductSolutions(context);
		updateProductServiceInSfdc(context);
		return context.gscQuoteDataBean;
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

	/**
	 * Method to get le attributes.
	 *
	 * @param quoteTole
	 * @param attribute
	 * @return
	 */
	private String getLeAttributes(QuoteToLe quoteTole, String attribute) {
		MstOmsAttribute mstOmsAttribute = mstOmsAttributeRepository
				.findByNameAndIsActive(attribute, CommonConstants.BACTIVE).stream().findFirst().get();
		return quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(quoteTole, mstOmsAttribute).stream()
				.map(QuoteLeAttributeValue::getAttributeValue).findFirst().get();
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
		deleteGscQuote(getQuote(quoteId));
	}

	/**
	 * Method delete gsc quote details
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
	 * Method to delete product solutions
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
}
