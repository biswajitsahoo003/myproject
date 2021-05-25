package com.tcl.dias.oms.teamsdr.service.v1;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.utils.*;
import com.tcl.dias.oms.beans.*;
import com.tcl.dias.oms.constants.*;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEDetailService;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiOrderLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRPricingBean;
import com.tcl.dias.oms.teamsdr.util.TeamsDRUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.teamsdr.beans.SolutionBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRCumulativePricesBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseAgreementType;
import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseRequestBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesRequest;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesResponse;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesResponseWrapper;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.gsc.beans.GscMultipleLESolutionBean;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEPricingFeasibilityService;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEQuoteService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.teamsdr.beans.ComponentBean;
import com.tcl.dias.oms.teamsdr.beans.MediaGatewayConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRAttributesBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRCityBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRConfigurationDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRConfigurationPricingBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRDeleteConfigRequestBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRDocumentBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRLicenseBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRLicenseComponentsBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRLicenseConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMediaGatewayBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiQuoteLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderServicesBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRQuoteDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRServicesBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRServicesResponseBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRSolutionBean;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.CommonConstants.USD;
import static com.tcl.dias.common.constants.LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY;
import static com.tcl.dias.common.constants.LeAttributesConstants.PAYMENT_CURRENCY;
import static com.tcl.dias.common.utils.Source.MANUAL_COF;
import static com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_ENRICHMENT;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_FORM;
import static com.tcl.dias.oms.gsc.util.GscConstants.*;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.STATUS_ACTIVE;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_OPTY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.SFDC;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ALL_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CONFIGURATION_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_AMC_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_OUTRIGHT_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_RENTAL_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CUSTOM_PLAN;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.EFFECTIVE_MSA_DATE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.IS_MEDIAGATEWAY;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.LICENSE_REQUIRED;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MANAGEMENT_AND_MONITORING_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MEDIA_GATEWAY;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_LICENSE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.NON_RECURRING;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.OVERAGE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.PLAN;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.RECURRING;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.SERVICE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TATA_VOICE_NEEDED_ATTRIBUTE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_CONFIG_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_LICENSE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_LICENSE_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_MEDIAGATEWAY_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_SERVICE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_SITE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TWELVE_MONTHS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.USAGE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRUtils.calculateNewValue;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRUtils.checkForNull;

/**
 * Service class for UCaaS Teams DR product quote
 *
 * @author Srinivasa Raghavan
 *
 */
@Service
public class TeamsDRQuoteService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TeamsDRQuoteService.class);

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Autowired
	QuoteProductComponentRepository productComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteTeamsDRRepository quoteTeamsDRRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	QuoteDirectRoutingRepository quoteDirectRoutingRepository;

	@Autowired
	QuoteDirectRoutingCityRepository quoteDirectRoutingCityRepository;

	@Autowired
	QuoteDirectRoutingMgRepository quoteDirectRoutingMgRepository;

	@Autowired
	TeamsDRPricingFeasibilityService teamsDRPricingFeasibilityService;

	@Autowired
	RestClientService restClientService;

	@Value("${rabbitmq.teamsdr.license.countries.details}")
	String licenseBasedOnCountries;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	QuoteTeamsLicenseRepository quoteTeamsLicenseRepository;

	@Autowired
	QuoteTeamsDRDetailsRepository quoteTeamsDRDetailsRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Value("${rabbitmq.rules.teamsdr.segregate}")
	String solutionSegregateQuote;


	@Value("${optimus.rules.test.queue}")
	String testQueue;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	GscMultiLEQuoteService gscMultiLEQuoteService;

	@Value("${rabbitmq.customerleattr.product.queue}")
	String customerLeAttrQueueProduct;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	GscMultiLEPricingFeasibilityService gscMultiLEPricingFeasibilityService;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeDetailsQueue;

	@Autowired
	TeamsDRSfdcService teamsDRSfdcService;

	@Autowired
	CreditCheckService creditCheckService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	TeamsDRPdfService teamsDRPdfService;

	@Autowired
	QuoteLeCreditCheckAuditRepository quoteLeCreditCheckAuditRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderTeamsDRRepository orderTeamsDRRepository;

	@Autowired
	OrderTeamsDRDetailsRepository orderTeamsDRDetailsRepository;

	@Autowired
	OrderTeamsLicenseRepository orderTeamsLicenseRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderDirectRoutingRepository orderDirectRoutingRepository;

	@Autowired
	OrderDirectRoutingCityRepository orderDirectRoutingCityRepository;

	@Autowired
	OrderDirectRoutingMgRepository orderDirectRoutingMgRepository;

	@Autowired
	TeamsDROrderService  teamsDROrderService;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	OmsUtilService omsUtilService;

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	GscMultiLEDetailService gscMultiLEDetailService;
	/**
	 * Create quote for Teams DR
	 *
	 * @param teamsDRQuoteDataBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRQuoteDataBean createQuote(TeamsDRQuoteDataBean teamsDRQuoteDataBean) throws TclCommonException {
		LOGGER.info("createQuote started : {}", teamsDRQuoteDataBean.getProductFamilyName());
		Objects.requireNonNull(teamsDRQuoteDataBean.getQuoteToLes());
		User user = getUserId(Utils.getSource());
		Quote quote = processQuote(teamsDRQuoteDataBean, user);
		MstProductFamily productFamily = getProductFamily(teamsDRQuoteDataBean.getProductFamilyName());
		teamsDRQuoteDataBean.getQuoteToLes().forEach(teamsDRMultiQuoteLeBean -> {
			QuoteToLe quoteToLe = processQuoteToLe(teamsDRQuoteDataBean,teamsDRMultiQuoteLeBean, quote);
			QuoteToLeProductFamily quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe);
			saveProductSolutions(teamsDRQuoteDataBean, teamsDRMultiQuoteLeBean, quoteToLe, productFamily, quoteToLeProductFamily);
			persistQuoteLeAttributes(user,quoteToLe);
			try {
				triggerTeamsDRPricingAllServices(teamsDRQuoteDataBean.getQuoteId(), teamsDRMultiQuoteLeBean);
			} catch (TclCommonException e) {
				LOGGER.info("Error occurred while triggering pricing {}", e.getMessage());
				throw new TclCommonRuntimeException(ExceptionConstants.PRICING_FAILURE_EXCEPTION);
			}
			updateQuoteToLeWithTeamsDRPrices(quoteToLe, teamsDRMultiQuoteLeBean);
			teamsDRSfdcService.createOpportunityInSfdc(quoteToLe,teamsDRQuoteDataBean.getEngagementOptyId(),
					teamsDRQuoteDataBean.getProductFamilyName());
		});
		// stages maintained at quote-level also, because stage is the same for all the
		// quote_to_le in l2o journey
		Optional<TeamsDRMultiQuoteLeBean> quoteLeBean = teamsDRQuoteDataBean.getQuoteToLes().stream()
				.filter(Objects::nonNull).findAny();
		quoteLeBean.ifPresent(quoteLe -> {
			teamsDRQuoteDataBean.setStage(quoteLe.getStage());
			teamsDRQuoteDataBean.setSubStage(quoteLe.getSubStage());
		});
		return teamsDRQuoteDataBean;
	}

	/**
	 * Method to save product solutions
	 *
	 * @param teamsDRQuoteDataBean
	 * @param quoteToLe
	 * @param productFamily
	 * @param quoteToLeProductFamily
	 */
	private void saveProductSolutions(TeamsDRQuoteDataBean teamsDRQuoteDataBean,
									  TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean, QuoteToLe quoteToLe,
									  MstProductFamily productFamily, QuoteToLeProductFamily quoteToLeProductFamily) {
		teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().forEach(teamsDRService -> {
			createSolutions(teamsDRQuoteDataBean, teamsDRService, quoteToLe, productFamily, quoteToLeProductFamily);
		});
	}

	/**
	 * Save prices in quote to le
	 *
	 * @param teamsDRServices
	 * @param quoteToLe
	 */
	private void savePrices(List<TeamsDRServicesBean> teamsDRServices, QuoteToLe quoteToLe) {
		teamsDRServices.stream()
				.filter(teamsDRService -> teamsDRService.getOfferingName().equals(teamsDRService.getPlan()))
				.forEach(teamsDRService -> {
					quoteToLe.setProposedMrc(
							checkForNull(quoteToLe.getProposedMrc()) + checkForNull(teamsDRService.getMrc()));
					quoteToLe.setProposedNrc(
							checkForNull(quoteToLe.getProposedNrc()) + checkForNull(teamsDRService.getNrc()));
					quoteToLe
							.setFinalMrc(checkForNull(quoteToLe.getFinalMrc()) + checkForNull(teamsDRService.getMrc()));
					quoteToLe
							.setFinalNrc(checkForNull(quoteToLe.getFinalNrc()) + checkForNull(teamsDRService.getNrc()));
				});
		quoteToLe.setProposedArc(12 * checkForNull(quoteToLe.getProposedMrc()));
		quoteToLe.setFinalArc(12 * checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setTotalTcv(quoteToLe.getFinalMrc() + quoteToLe.getFinalNrc());
	}

	/**
	 * Creating solutions
	 *
	 * @param teamsDRService
	 * @param quoteToLe
	 * @param productFamily
	 * @param quoteToLeProductFamily
	 */
	private ProductSolution createSolutions(TeamsDRQuoteDataBean teamsDRQuoteDataBean, TeamsDRServicesBean teamsDRService,
			QuoteToLe quoteToLe, MstProductFamily productFamily, QuoteToLeProductFamily quoteToLeProductFamily) {
		ProductSolution productSolution;
		try {
			String productOffering = teamsDRService.getOfferingName();
			MstProductOffering productOfferng = getProductOffering(productFamily, productOffering);
			productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
					Utils.convertObjectToJson(teamsDRService));
			teamsDRService.setSolutionId(productSolution.getId());
			teamsDRService.setSolutionCode(productSolution.getSolutionCode());
			createTeamsDRQuote(teamsDRQuoteDataBean, teamsDRService, quoteToLe, productSolution);
			LOGGER.info("Product Solution created with id {}", teamsDRService.getSolutionId());
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return productSolution;
	}

	/**
	 * Method to update quotetole with cummulative plan and mg price.
	 *
	 * @param quoteToLe
	 */
	private void updateQuoteToLeWithPlanAndMg(QuoteToLe quoteToLe, TeamsDRMultiQuoteLeBean quoteLeBean) {
		LOGGER.info("Inside updateQuoteToLeWithTeamsDRPrices");
		List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if (Objects.nonNull(quoteTeamsDRS) && !quoteTeamsDRS.isEmpty()) {
			quoteTeamsDRS.stream().filter(quoteTeamsDR -> (Objects.isNull(quoteTeamsDR.getServiceName()) && Objects
					.nonNull(quoteTeamsDR.getProfileName()) && quoteTeamsDR.getProfileName()
					.contains(PLAN)) || MEDIA_GATEWAY.equalsIgnoreCase(quoteTeamsDR.getServiceName()))
					.forEach(quoteTeamsDR -> {
						LOGGER.info("QuoteTeamsDR :: {}", quoteTeamsDR.getId());
						teamsDRCumulativePricesBean.setTotalMrc(
								teamsDRCumulativePricesBean.getTotalMrc() + checkForNull(quoteTeamsDR.getMrc()));
						teamsDRCumulativePricesBean.setTotalNrc(
								teamsDRCumulativePricesBean.getTotalNrc() + checkForNull(quoteTeamsDR.getNrc()));
						teamsDRCumulativePricesBean.setTotalArc(
								teamsDRCumulativePricesBean.getTotalArc() + checkForNull(quoteTeamsDR.getArc()));
						teamsDRCumulativePricesBean.setTotalTcv(
								teamsDRCumulativePricesBean.getTotalTcv() + checkForNull(quoteTeamsDR.getTcv()));
					});

			quoteToLe.setProposedMrc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedMrc()) + teamsDRCumulativePricesBean.getTotalMrc(), 2));
			quoteToLe.setProposedNrc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedNrc()) + teamsDRCumulativePricesBean.getTotalNrc(), 2));
			quoteToLe.setProposedArc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedArc()) + teamsDRCumulativePricesBean.getTotalArc(), 2));

			quoteToLe.setFinalMrc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalMrc()) + teamsDRCumulativePricesBean.getTotalMrc(), 2));
			quoteToLe.setFinalNrc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalNrc()) + teamsDRCumulativePricesBean.getTotalNrc(), 2));
			quoteToLe.setFinalArc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalArc()) + teamsDRCumulativePricesBean.getTotalArc(), 2));

			quoteToLe.setTotalTcv(Utils.setPrecision(
					checkForNull(quoteToLe.getTotalTcv()) + teamsDRCumulativePricesBean.getTotalTcv(), 2));
			quoteToLeRepository.save(quoteToLe);

			quoteLeBean.setProposedMrc(quoteToLe.getProposedMrc());
			quoteLeBean.setProposedNrc(quoteToLe.getProposedNrc());
			quoteLeBean.setProposedArc(quoteToLe.getProposedArc());
			quoteLeBean.setFinalMrc(quoteToLe.getFinalMrc());
			quoteLeBean.setFinalNrc(quoteToLe.getFinalNrc());
			quoteLeBean.setFinalArc(quoteToLe.getFinalArc());
			quoteLeBean.setTotalTcv(quoteToLe.getTotalTcv());
		}
	}

	/**
	 * Method to update quotetole with cummulative license price.
	 *
	 * @param quoteToLe
	 */
	private void updateQuoteToLeWithLicense(QuoteToLe quoteToLe, TeamsDRMultiQuoteLeBean quoteLeBean) {
		LOGGER.info("Inside updateQuoteToLeWithTeamsDRPrices");
		List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if (Objects.nonNull(quoteTeamsDRS) && !quoteTeamsDRS.isEmpty()) {
			quoteTeamsDRS.stream()
					.filter(quoteTeamsDR -> MICROSOFT_LICENSE.equalsIgnoreCase(quoteTeamsDR.getServiceName()))
					.forEach(quoteTeamsDR -> {
						LOGGER.info("QuoteTeamsDR :: {}", quoteTeamsDR.getId());
						teamsDRCumulativePricesBean.setTotalMrc(
								teamsDRCumulativePricesBean.getTotalMrc() + checkForNull(quoteTeamsDR.getMrc()));
						teamsDRCumulativePricesBean.setTotalNrc(
								teamsDRCumulativePricesBean.getTotalNrc() + checkForNull(quoteTeamsDR.getNrc()));
						teamsDRCumulativePricesBean.setTotalArc(
								teamsDRCumulativePricesBean.getTotalArc() + checkForNull(quoteTeamsDR.getArc()));
						teamsDRCumulativePricesBean.setTotalTcv(
								teamsDRCumulativePricesBean.getTotalTcv() + checkForNull(quoteTeamsDR.getTcv()));
					});

			quoteToLe.setProposedMrc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedMrc()) + teamsDRCumulativePricesBean.getTotalMrc(), 2));
			quoteToLe.setProposedNrc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedNrc()) + teamsDRCumulativePricesBean.getTotalNrc(), 2));
			quoteToLe.setProposedArc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedArc()) + teamsDRCumulativePricesBean.getTotalArc(), 2));

			quoteToLe.setFinalMrc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalMrc()) + teamsDRCumulativePricesBean.getTotalMrc(), 2));
			quoteToLe.setFinalNrc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalNrc()) + teamsDRCumulativePricesBean.getTotalNrc(), 2));
			quoteToLe.setFinalArc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalArc()) + teamsDRCumulativePricesBean.getTotalArc(), 2));

			quoteToLe.setTotalTcv(Utils.setPrecision(
					checkForNull(quoteToLe.getTotalTcv()) + teamsDRCumulativePricesBean.getTotalTcv(), 2));
			quoteToLeRepository.save(quoteToLe);

			quoteLeBean.setProposedMrc(quoteToLe.getProposedMrc());
			quoteLeBean.setProposedNrc(quoteToLe.getProposedNrc());
			quoteLeBean.setProposedArc(quoteToLe.getProposedArc());
			quoteLeBean.setFinalMrc(quoteToLe.getFinalMrc());
			quoteLeBean.setFinalNrc(quoteToLe.getFinalNrc());
			quoteLeBean.setFinalArc(quoteToLe.getFinalArc());
			quoteLeBean.setTotalTcv(quoteToLe.getTotalTcv());
		}
	}

	/**
	 * Method to update quotetole with cummulative teamsdrprice.
	 * @param quoteToLe
	 */
	private void updateQuoteToLeWithTeamsDRPrices(QuoteToLe quoteToLe, TeamsDRMultiQuoteLeBean quoteLeBean){
		LOGGER.info("Inside updateQuoteToLeWithTeamsDRPrices");
		List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if (Objects.nonNull(quoteTeamsDRS) && !quoteTeamsDRS.isEmpty()) {
			quoteTeamsDRS.stream().filter(quoteTeamsDR -> (Objects.isNull(quoteTeamsDR.getServiceName()) && Objects
					.nonNull(quoteTeamsDR.getProfileName()) && quoteTeamsDR.getProfileName()
					.contains(PLAN)) || MICROSOFT_LICENSE
					.equalsIgnoreCase(quoteTeamsDR.getServiceName()) || MEDIA_GATEWAY
					.equalsIgnoreCase(quoteTeamsDR.getServiceName())).forEach(quoteTeamsDR -> {
				LOGGER.info("QuoteTeamsDR :: {}", quoteTeamsDR.getId());
				teamsDRCumulativePricesBean
						.setTotalMrc(teamsDRCumulativePricesBean.getTotalMrc() + checkForNull(quoteTeamsDR.getMrc()));
				teamsDRCumulativePricesBean
						.setTotalNrc(teamsDRCumulativePricesBean.getTotalNrc() + checkForNull(quoteTeamsDR.getNrc()));
				teamsDRCumulativePricesBean
						.setTotalArc(teamsDRCumulativePricesBean.getTotalArc() + checkForNull(quoteTeamsDR.getArc()));
				teamsDRCumulativePricesBean
						.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv() + checkForNull(quoteTeamsDR.getTcv()));
			});

			quoteToLe.setProposedMrc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedMrc()) + teamsDRCumulativePricesBean.getTotalMrc(), 2));
			quoteToLe.setProposedNrc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedNrc()) + teamsDRCumulativePricesBean.getTotalNrc(), 2));
			quoteToLe.setProposedArc(Utils.setPrecision(
					checkForNull(quoteToLe.getProposedArc()) + teamsDRCumulativePricesBean.getTotalArc(), 2));

			quoteToLe.setFinalMrc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalMrc()) + teamsDRCumulativePricesBean.getTotalMrc(), 2));
			quoteToLe.setFinalNrc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalNrc()) + teamsDRCumulativePricesBean.getTotalNrc(), 2));
			quoteToLe.setFinalArc(Utils.setPrecision(
					checkForNull(quoteToLe.getFinalArc()) + teamsDRCumulativePricesBean.getTotalArc(), 2));

			quoteToLe.setTotalTcv(Utils.setPrecision(
					checkForNull(quoteToLe.getTotalTcv()) + teamsDRCumulativePricesBean.getTotalTcv(), 2));
			quoteToLeRepository.save(quoteToLe);

			quoteLeBean.setProposedMrc(quoteToLe.getProposedMrc());
			quoteLeBean.setProposedNrc(quoteToLe.getProposedNrc());
			quoteLeBean.setProposedArc(quoteToLe.getProposedArc());
			quoteLeBean.setFinalMrc(quoteToLe.getFinalMrc());
			quoteLeBean.setFinalNrc(quoteToLe.getFinalNrc());
			quoteLeBean.setFinalArc(quoteToLe.getFinalArc());
			quoteLeBean.setTotalTcv(quoteToLe.getTotalTcv());
		}
	}

	/**
	 * Method to update quotetole with cummulative quoteGsc prices..
	 * @param quoteToLe
	 */
	private void updateQuoteToLeWithGscPrices(QuoteToLe quoteToLe){
		LOGGER.info("Inside updateQuoteToLeWithGscPrices");
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if(Objects.nonNull(quoteGscs) && !quoteGscs.isEmpty()){
			quoteGscs.forEach(quoteGsc -> {
				LOGGER.info("QuoteGsc :: {}",quoteGsc.getId());
				teamsDRCumulativePricesBean.setTotalMrc(teamsDRCumulativePricesBean.getTotalMrc()+checkForNull(quoteGsc.getMrc()));
				teamsDRCumulativePricesBean.setTotalNrc(teamsDRCumulativePricesBean.getTotalNrc()+checkForNull(quoteGsc.getNrc()));
				teamsDRCumulativePricesBean.setTotalArc(teamsDRCumulativePricesBean.getTotalArc()+checkForNull(quoteGsc.getArc()));
				teamsDRCumulativePricesBean.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv()+checkForNull(quoteGsc.getTcv()));
			});

			quoteToLe.setProposedMrc(checkForNull(quoteToLe.getProposedMrc()) + teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setProposedNrc(checkForNull(quoteToLe.getProposedNrc()) + teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setProposedArc(checkForNull(quoteToLe.getProposedArc()) + teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setFinalMrc(checkForNull(quoteToLe.getFinalMrc()) + teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setFinalNrc(checkForNull(quoteToLe.getFinalNrc()) + teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setFinalArc(checkForNull(quoteToLe.getFinalArc()) + teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setTotalTcv(checkForNull(quoteToLe.getTotalTcv()) + teamsDRCumulativePricesBean.getTotalTcv());
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Method to update quotetole with quoteTeamsDR prices..
	 *
	 * @param quoteToLe
	 */
	protected void updateQuoteToLeWithQuoteTeamsDR(QuoteToLe quoteToLe, QuoteTeamsDR quoteTeamsDR) {
		LOGGER.info("Inside updateQuoteToLeWithGscPrices");
		if (Objects.nonNull(quoteTeamsDR)) {
			LOGGER.info("quoteTeamsDR :: {}", quoteTeamsDR.getId());

			quoteToLe.setProposedMrc(checkForNull(quoteToLe.getProposedMrc()) + quoteTeamsDR.getMrc());
			quoteToLe.setProposedNrc(checkForNull(quoteToLe.getProposedNrc()) + quoteTeamsDR.getNrc());
			quoteToLe.setProposedArc(12 * quoteToLe.getProposedMrc());

			quoteToLe.setFinalMrc(checkForNull(quoteToLe.getFinalMrc()) + quoteTeamsDR.getMrc());
			quoteToLe.setFinalNrc(checkForNull(quoteToLe.getFinalNrc()) + quoteTeamsDR.getNrc());
			quoteToLe.setFinalArc(12 * quoteToLe.getFinalMrc());

			quoteToLe.setTotalTcv((TeamsDRUtils.extractContractPeriodFromString(quoteToLe.getTermInMonths()) * quoteToLe
					.getFinalMrc()) + quoteToLe.getFinalNrc());
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Method to set quotetole with cummulative teamsdrprice.
	 * @param quoteToLe
	 */
	private void setQuoteToLeWithTeamsDRPrices(QuoteToLe quoteToLe){
		LOGGER.info("Inside updateQuoteToLeWithTeamsDRPrices");
		List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if(Objects.nonNull(quoteTeamsDRS) && !quoteTeamsDRS.isEmpty()){
			quoteTeamsDRS.forEach(quoteTeamsDR -> {
				LOGGER.info("QuoteTeamsDR :: {}",quoteTeamsDR.getId());
				teamsDRCumulativePricesBean.setTotalMrc(teamsDRCumulativePricesBean.getTotalMrc()+checkForNull(quoteTeamsDR.getMrc()));
				teamsDRCumulativePricesBean.setTotalNrc(teamsDRCumulativePricesBean.getTotalNrc()+checkForNull(quoteTeamsDR.getNrc()));
				teamsDRCumulativePricesBean.setTotalArc(teamsDRCumulativePricesBean.getTotalArc()+checkForNull(quoteTeamsDR.getArc()));
				teamsDRCumulativePricesBean.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv()+checkForNull(quoteTeamsDR.getTcv()));
			});

			quoteToLe.setProposedMrc(teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setProposedNrc(teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setProposedArc(teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setFinalMrc(teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setFinalNrc(teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setFinalArc(teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv());
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Method to set dummy price in quote price repo.
	 *
	 * @param teamsDRServicesBean
	 * @param quoteTeamsDR
	 * @throws TclCommonException
	 */
	private void setDummyPrice(TeamsDRServicesBean teamsDRServicesBean, QuoteTeamsDR quoteTeamsDR)
			throws TclCommonException {

		MstProductFamily mstProductFamily = getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);

		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();

		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quoteTeamsDR.getId(), TEAMSDR_CONFIG_ATTRIBUTES);
		List<ComponentBean> componentBeans = new ArrayList<>();
		quoteProductComponents.forEach(quoteProductComponent -> {
			List<QuoteProductComponentsAttributeValueBean> attributeValuesBeans = new ArrayList<>();
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
			attributeValues.forEach(quoteProductComponentsAttributeValue -> {
				String charge = quoteProductComponentsAttributeValue.getProductAttributeMaster().getName();
				if (ALL_CHARGES.contains(charge)) {
					Double dummyPrice = 0.0;
					String planName = quoteProductComponentsAttributeValue.getQuoteProductComponent()
							.getMstProductComponent().getName();
					LOGGER.info("Plan name for setting dummy price ::{}",planName);
					if (TeamsDRConstants.MANAGED_PLAN.equals(planName)) {
						dummyPrice = 5.0;
					} else if (TeamsDRConstants.CONNECTED_PLAN.equals(planName)) {
						dummyPrice = 9.0;
					} else {
						// For custom and underlying services.
						dummyPrice = 7.0;
					}

					LOGGER.info("Dummy value being set is :: {}",dummyPrice);

					List<QuotePrice> quotePrices = quotePriceRepository.findByReferenceNameAndReferenceId(ATTRIBUTES,
							quoteProductComponentsAttributeValue.getId().toString());
					if (quotePrices.isEmpty()) {
						QuotePrice quotePrice = new QuotePrice();
						if (NON_RECURRING.equals(charge)) {
							quotePrice.setMinimumNrc(dummyPrice);
							quotePrice.setEffectiveNrc(dummyPrice*quoteTeamsDR.getNoOfUsers());
						} else if (RECURRING.equals(charge)) {
							quotePrice.setMinimumMrc(dummyPrice);
							quotePrice.setEffectiveMrc(dummyPrice*quoteTeamsDR.getNoOfUsers());
						} else if (OVERAGE.equals(charge) || USAGE.equals(charge)) {
							if(quoteTeamsDR.getNoOfUsers()>500){
								Integer differenceInUsers = quoteTeamsDR.getNoOfUsers() - 500;
								quotePrice.setEffectiveUsagePrice(dummyPrice*differenceInUsers);
							}else{
								quotePrice.setEffectiveUsagePrice(dummyPrice);
							}
						}
						teamsDRCumulativePricesBean.setMrc(
								teamsDRCumulativePricesBean.getMrc() + checkForNull(quotePrice.getEffectiveMrc()));
						teamsDRCumulativePricesBean.setNrc(
								teamsDRCumulativePricesBean.getNrc() + checkForNull(quotePrice.getEffectiveNrc()));
						teamsDRCumulativePricesBean.setArc(
								teamsDRCumulativePricesBean.getArc() + checkForNull(quotePrice.getEffectiveArc()));
						teamsDRCumulativePricesBean.setTcv(teamsDRCumulativePricesBean.getTcv()
								+ checkForNull(quotePrice.getEffectiveUsagePrice()));

						quotePrice.setReferenceName(ATTRIBUTES);
						quotePrice.setReferenceId(quoteProductComponentsAttributeValue.getId().toString());
						quotePrice.setQuoteId(quoteTeamsDR.getProductSolution().getQuoteToLeProductFamily()
								.getQuoteToLe().getQuote().getId());
						quotePrice.setMstProductFamily(mstProductFamily);
						quotePriceRepository.save(quotePrice);
					}else{
						// if already values are present in quote price..
						Double finalDummyPrice = dummyPrice;
						quotePrices.forEach(quotePrice -> {
							Optional<QuoteProductComponentsAttributeValue> attributeValue =
									quoteProductComponentsAttributeValueRepository.findById(Integer.parseInt(quotePrice.getReferenceId()));
							if(attributeValue.isPresent()){
								String chargeName = attributeValue.get().getProductAttributeMaster().getName();
								if (NON_RECURRING.equals(chargeName)) {
									quotePrice.setMinimumNrc(finalDummyPrice);
									quotePrice.setEffectiveNrc(finalDummyPrice *quoteTeamsDR.getNoOfUsers());
								} else if (RECURRING.equals(chargeName)) {
									quotePrice.setMinimumMrc(finalDummyPrice);
									quotePrice.setEffectiveMrc(finalDummyPrice *quoteTeamsDR.getNoOfUsers());
								} else if (OVERAGE.equals(chargeName) || USAGE.equals(chargeName)) {
									if(quoteTeamsDR.getNoOfUsers()>500){
										Integer differenceInUsers = quoteTeamsDR.getNoOfUsers() - 500;
										quotePrice.setEffectiveUsagePrice(finalDummyPrice *differenceInUsers);
									}else{
										quotePrice.setEffectiveUsagePrice(finalDummyPrice);
									}
								}
								quotePriceRepository.save(quotePrice);
							}
							teamsDRCumulativePricesBean.setMrc(
									teamsDRCumulativePricesBean.getMrc() + checkForNull(quotePrice.getEffectiveMrc()));
							teamsDRCumulativePricesBean.setNrc(
									teamsDRCumulativePricesBean.getNrc() + checkForNull(quotePrice.getEffectiveNrc()));
							teamsDRCumulativePricesBean.setArc(
									teamsDRCumulativePricesBean.getArc() + checkForNull(quotePrice.getEffectiveArc()));
							teamsDRCumulativePricesBean.setTcv(teamsDRCumulativePricesBean.getTcv()
									+ checkForNull(quotePrice.getEffectiveUsagePrice()));
						});
					}
				}
			});
		});

		quoteTeamsDR.setMrc(teamsDRCumulativePricesBean.getMrc());
		quoteTeamsDR.setNrc(teamsDRCumulativePricesBean.getNrc());
		quoteTeamsDR.setArc(checkForNull(quoteTeamsDR.getMrc())*12);
		quoteTeamsDR.setTcv(checkForNull(quoteTeamsDR.getArc())+checkForNull(quoteTeamsDR.getNrc()));
		quoteTeamsDRRepository.save(quoteTeamsDR);

		teamsDRServicesBean.setMrc(teamsDRCumulativePricesBean.getMrc());
		teamsDRServicesBean.setNrc(teamsDRCumulativePricesBean.getNrc());
		teamsDRServicesBean.setArc(quoteTeamsDR.getArc());
		teamsDRServicesBean.setTcv(quoteTeamsDR.getTcv());
	}

	/**
	 * Create Teams DR quote
	 *
	 * @param teamsDRService
	 * @param quoteToLe
	 * @param productSolution
	 */
	private void createTeamsDRQuote(TeamsDRQuoteDataBean teamsDRQuoteDataBean, TeamsDRServicesBean teamsDRService,
									QuoteToLe quoteToLe, ProductSolution productSolution) throws TclCommonException {
		QuoteTeamsDR quoteTeamsDR = new QuoteTeamsDR();
		quoteTeamsDR.setQuoteToLe(quoteToLe);
		quoteTeamsDR.setProductSolution(productSolution);
		quoteTeamsDR.setProfileName(teamsDRService.getPlan());
		quoteTeamsDR.setNoOfUsers(teamsDRService.getNoOfUsers());
		quoteTeamsDR.setServiceName(teamsDRService.getOfferingName().equals(teamsDRService.getPlan()) ? null
				: teamsDRService.getOfferingName());
		if (Objects.isNull(quoteTeamsDR.getServiceName())) {
			quoteTeamsDR.setIsConfig((byte) 1);
		} else {
			quoteTeamsDR.setIsConfig((byte) 0);
		}
		quoteTeamsDR.setCreatedBy(Utils.getSource());
		quoteTeamsDR.setCreatedTime(new Date());
		quoteTeamsDR.setQuoteVersion(1);
		quoteTeamsDR.setMrc(teamsDRService.getMrc());
		quoteTeamsDR.setNrc(teamsDRService.getNrc());
		quoteTeamsDR.setArc(teamsDRService.getArc());
		quoteTeamsDR.setTcv(teamsDRService.getTcv());
		quoteTeamsDR.setStatus(CommonConstants.BACTIVE);
		quoteTeamsDR = quoteTeamsDRRepository.save(quoteTeamsDR);

		if(Objects.nonNull(teamsDRService.getPlan())){
			if(!CUSTOM_PLAN.equals(teamsDRService.getPlan())){
				if(Objects.isNull(quoteTeamsDR.getServiceName())){
					processProductComponent(quoteTeamsDR.getId(),teamsDRService.getComponents().get(0)
							, TEAMSDR_CONFIG_ATTRIBUTES);
				}
			}else{
				processProductComponent(quoteTeamsDR.getId(),teamsDRService.getComponents().get(0),
						TEAMSDR_CONFIG_ATTRIBUTES);
			}
		}
		teamsDRService.setComponents(getComponentDetail(quoteTeamsDR.getId(),TEAMSDR_CONFIG_ATTRIBUTES));
		teamsDRService.setQuoteTeamsDRId(quoteTeamsDR.getId());
	}

	/**
	 * Method to populate quote direct routing.
	 * @param quoteTeamsDR
	 * @param teamsDRServicesBean
	 */
	private void populateQuoteDirectRouting(QuoteTeamsDR quoteTeamsDR,TeamsDRServicesBean teamsDRServicesBean){
		QuoteDirectRouting quoteDirectRouting = new QuoteDirectRouting();

		// Spliting the string by delimeter '-' Eg. custom-configuratio-media-gateway-india
		List<String> words = Arrays.asList(teamsDRServicesBean.getOfferingName().split("-"));

		// Getting the last element in the list.. In this case country name.
		String country = words.get(words.size()-1);

		quoteDirectRouting.setCountry(country);
		quoteDirectRouting.setQuoteTeamsDR(quoteTeamsDR);
		quoteDirectRouting.setMrc(BigDecimal.ZERO);
		quoteDirectRouting.setNrc(BigDecimal.ZERO);
		quoteDirectRouting.setArc(BigDecimal.ZERO);
		quoteDirectRouting.setTcv(BigDecimal.ZERO)	;
		quoteDirectRouting.setCreatedTime(new Date());
		quoteDirectRouting.setQuoteVersion(1);
		quoteDirectRoutingRepository.save(quoteDirectRouting);
	}

	/**
	 * Constructing new product solution
	 *
	 * @param mstProductOffering
	 * @param quoteToLeProductFamily
	 * @param productProfileData
	 * @return
	 */
	private ProductSolution constructProductSolution(MstProductOffering mstProductOffering,
													 QuoteToLeProductFamily quoteToLeProductFamily, String productProfileData) {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setMstProductOffering(mstProductOffering);
		productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolution.setProductProfileData(productProfileData);
		productSolution.setSolutionCode(Utils.generateUid());
		productSolutionRepository.save(productSolution);
		return productSolution;
	}

	/**
	 * Get Master product offering
	 *
	 * @param mstProductFamily
	 * @param productOfferingName
	 * @return
	 */
	protected MstProductOffering getProductOffering(MstProductFamily mstProductFamily, String productOfferingName) {
		MstProductOffering productOffering;
		productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,
				productOfferingName, (byte) 1);

		if(productOffering == null){
			User user = getUserId(Utils.getSource());
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
	 * Construct quoteToLe Product family
	 *
	 * @param mstProductFamily
	 * @param quoteToLe
	 * @return
	 */
	private QuoteToLeProductFamily constructQuoteToLeProductFamily(MstProductFamily mstProductFamily,
																   QuoteToLe quoteToLe) {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
		quoteToLeProductFamily.setQuoteToLe(quoteToLe);
		quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
        if (Objects.isNull(quoteToLe.getQuoteToLeProductFamilies()))
            quoteToLe.setQuoteToLeProductFamilies(new HashSet<>());
		quoteToLe.getQuoteToLeProductFamilies().add(quoteToLeProductFamily);
		quoteToLeRepository.save(quoteToLe);
		return quoteToLeProductFamily;

	}

	/**
	 * Get product family by family name
	 *
	 * @param productFamilyName
	 * @return
	 * @throws TclCommonException
	 */
	protected MstProductFamily getProductFamily(String productFamilyName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productFamilyName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;

	}

	/**
	 * Process quote to Le
	 *
	 * @param teamsDRQuoteDataBean
	 * @param quote
	 * @return
	 */
	private QuoteToLe processQuoteToLe(TeamsDRQuoteDataBean teamsDRQuoteDataBean, TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean,
									   Quote quote) {
		QuoteToLe quoteToLe = null;
		String paymentCurrency = LeAttributesConstants.USD;
		quoteToLe = constructQuoteToLe(teamsDRQuoteDataBean, teamsDRMultiQuoteLeBean, quote, paymentCurrency);
		quoteToLeRepository.save(quoteToLe);
		teamsDRMultiQuoteLeBean.setQuoteleId(quoteToLe.getId());
		setLegalEntityAttributes(quoteToLe,teamsDRMultiQuoteLeBean);
		LOGGER.info("QuoteToLe created : id {}", teamsDRMultiQuoteLeBean.getQuoteleId());
		return quoteToLe;
	}

	/**
	 * Method to set legal entity attributes.
	 * @param quoteToLe
	 * @param teamsDRMultiQuoteLeBean
	 */
	private void setLegalEntityAttributes(QuoteToLe quoteToLe,TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean){
		if (quoteToLe != null) {
			teamsDRMultiQuoteLeBean.setQuoteleId(quoteToLe.getId());
			teamsDRMultiQuoteLeBean.setFinalMrc(quoteToLe.getFinalMrc());
			teamsDRMultiQuoteLeBean.setFinalNrc(quoteToLe.getFinalNrc());
			teamsDRMultiQuoteLeBean.setFinalArc(quoteToLe.getFinalArc());
			teamsDRMultiQuoteLeBean.setProposedMrc(quoteToLe.getProposedMrc());
			teamsDRMultiQuoteLeBean.setProposedNrc(quoteToLe.getProposedNrc());
			teamsDRMultiQuoteLeBean.setProposedArc(quoteToLe.getProposedArc());
			teamsDRMultiQuoteLeBean.setTotalTcv(quoteToLe.getTotalTcv());
			teamsDRMultiQuoteLeBean.setCurrencyId(quoteToLe.getCurrencyId());
			teamsDRMultiQuoteLeBean.setCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
			teamsDRMultiQuoteLeBean.setSupplierLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
			teamsDRMultiQuoteLeBean.setTpsSfdcOptyId( quoteToLe.getTpsSfdcOptyId());
			teamsDRMultiQuoteLeBean.setStage(quoteToLe.getStage());
			teamsDRMultiQuoteLeBean.setClassification(quoteToLe.getClassification());
			teamsDRMultiQuoteLeBean.setCreditLimit(quoteToLe.getTpsSfdcCreditLimit());
			teamsDRMultiQuoteLeBean.setSecurityDepositAmount( quoteToLe.getTpsSfdcSecurityDepositAmount());
			teamsDRMultiQuoteLeBean.setIsMultiCircuit(quoteToLe.getIsMultiCircuit());
			teamsDRMultiQuoteLeBean.setContractPeriod(quoteToLe.getTermInMonths());
			teamsDRMultiQuoteLeBean.setCurrency(quoteToLe.getCurrencyCode());
			teamsDRMultiQuoteLeBean.setQuoteLeCode(quoteToLe.getQuoteLeCode());
		}
	}

	/**
	 * Get legal entity details from quoteToLe
	 *
	 * @param quote
	 * @return
	 */
	private List<QuoteToLeBean> getLegalEntities(Quote quote) {
		return Optional.ofNullable(quoteToLeRepository.findByQuote(quote)).orElse(ImmutableList.of()).stream()
				.map(QuoteToLeBean::new).collect(Collectors.toList());
	}

	/**
	 * Construct Quote to Le
	 *
	 * @param teamsDRQuoteDataBean
	 * @param quote
	 * @param paymentCurrency
	 * @return
	 */
	private QuoteToLe constructQuoteToLe(TeamsDRQuoteDataBean teamsDRQuoteDataBean,
										 TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean, Quote quote,
										 String paymentCurrency) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setCurrencyCode(paymentCurrency);
		quoteToLe.setStage(QuoteStageConstants.SELECT_SERVICES.getConstantCode());
		quoteToLe.setTermInMonths(teamsDRMultiQuoteLeBean.getContractPeriod());
		quoteToLe.setQuoteType(teamsDRMultiQuoteLeBean.getQuoteType());
		quoteToLe.setQuoteCategory(Objects.isNull(teamsDRMultiQuoteLeBean.getQuoteCategory()) ? null
				: teamsDRMultiQuoteLeBean.getQuoteCategory());
		quoteToLe.setClassification(teamsDRMultiQuoteLeBean.getClassification());
		quoteToLe.setQuoteLeCode(Utils.generateUid(5));
		return quoteToLe;
	}

	/**
	 * Process quote during quote creation
	 *
	 * @param teamsDRQuoteDataBean
	 * @param user
	 * @return
	 * @throws TclCommonException
	 */
	private Quote processQuote(TeamsDRQuoteDataBean teamsDRQuoteDataBean, User user) throws TclCommonException {
		Customer customer = null;
		if (teamsDRQuoteDataBean.getCustomerId() != null)
			customer = getCustomerById(teamsDRQuoteDataBean.getCustomerId());
		else
			customer = user.getCustomer();
		Quote quote = null;
		if (teamsDRQuoteDataBean.getQuoteId() == null) {
			quote = constructQuote(customer, user, teamsDRQuoteDataBean);
			quoteRepository.save(quote);
		} else {
			quote = quoteRepository.findByIdAndStatus(teamsDRQuoteDataBean.getQuoteId(), CommonConstants.BACTIVE);
		}
		teamsDRQuoteDataBean.setQuoteId(quote.getId());
		teamsDRQuoteDataBean.setQuoteCode(quote.getQuoteCode());
		LOGGER.info("Quote created : id {}", teamsDRQuoteDataBean.getQuoteId());
		return quote;
	}

	/**
	 *
	 * get the customer details if already present
	 *
	 * @param customerId
	 * @return Customer
	 * @throws TclCommonException
	 */
	private Customer getCustomerById(Integer customerId) throws TclCommonException {
		Objects.requireNonNull(customerId);
		Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(customerId, (byte) 1);
		if (customer == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		return customer;

	}

	/**
	 * get user based on id
	 *
	 * @param username
	 * @return
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * Construct quote object for creating quote
	 *
	 * @param customer
	 * @param user
	 * @param teamsDRQuoteDataBean
	 * @return
	 */
	private Quote constructQuote(final Customer customer, final User user, TeamsDRQuoteDataBean teamsDRQuoteDataBean) {
		Quote quote = new Quote();
		quote.setCustomer(customer);
		quote.setCreatedBy(user.getId());
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setEngagementOptyId(teamsDRQuoteDataBean.getEngagementOptyId());
		quote.setQuoteCode(null != teamsDRQuoteDataBean.getEngagementOptyId() ? teamsDRQuoteDataBean.getQuoteCode()
				: Utils.generateRefId(TeamsDRConstants.UCAAS_TEAMSDR.toUpperCase()));
		return quote;
	}

	/**
	 * Get quote to le by ID
	 *
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	private QuoteToLe getQuoteToLeById(Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (!quoteToLeEntity.isPresent())
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		return quoteToLeEntity.get();
	}

	/**
	 * To update quotes if user has updated their choice
	 *
	 * @param teamsDRQuoteDataBean
	 * @return
	 */
	@Transactional
	public TeamsDRQuoteDataBean updateQuote(TeamsDRQuoteDataBean teamsDRQuoteDataBean) throws TclCommonException {
		MstProductFamily productFamily = getProductFamily(teamsDRQuoteDataBean.getProductFamilyName());
		List<TeamsDRMultiQuoteLeBean> tobeRemoved = new ArrayList<>();
		List<QuoteToLe> teamsDRQuoteToLes = new ArrayList<>();
		Set<Integer> initiallySavedIds = new HashSet<>();

		teamsDRQuoteDataBean.getQuoteToLes().forEach(teamsDRMultiQuoteLeBean -> {
			QuoteToLe quoteToLe;
			try {
				Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository
						.findById(teamsDRMultiQuoteLeBean.getQuoteleId());
				if (optionalQuoteToLe.isPresent()) {
					quoteToLe = optionalQuoteToLe.get();
					quoteToLe.setTermInMonths(teamsDRMultiQuoteLeBean.getContractPeriod());
					// Subtracting old teamsdr prices from quotetole...
 					subtractQuoteToLeWithTeamsDRPrice(quoteToLe);
					initiallySavedIds.add(quoteToLe.getId());
					LOGGER.info("QuoteToLeId :: {}", quoteToLe.getId());
					QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
							.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
					if (Objects.nonNull(quoteToLeProductFamily)) {
						teamsDRQuoteToLes.add(quoteToLe);
						checkAndProcessTeamsDRProductSolutions(teamsDRQuoteDataBean, teamsDRMultiQuoteLeBean, quoteToLe,
								productFamily, quoteToLeProductFamily);
						triggerTeamsDRPricingAllServices(teamsDRQuoteDataBean.getQuoteId(), teamsDRMultiQuoteLeBean);
					}
					// Updating new prices in quotetole...
					updateQuoteToLeWithTeamsDRPrices(quoteToLe, teamsDRMultiQuoteLeBean);
				} else {
					tobeRemoved.add(teamsDRMultiQuoteLeBean);
				}
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		});

		// Remove the quote to le bean .. if that quote to le is not present.
		teamsDRQuoteDataBean.getQuoteToLes().removeAll(tobeRemoved);

		// To find parent quote to le..
		QuoteToLe parentQuoteToLe = findParentQuoteToLe(teamsDRQuoteToLes);

		// check if new teamsdr solutions is present..
		createNewTeamsDRSolutions(teamsDRQuoteDataBean, productFamily, parentQuoteToLe);

		// Update quote data bean with newly created quote to le's
		updateQuoteDataBean(initiallySavedIds, teamsDRQuoteDataBean, productFamily);

		// stages maintained at quote-level also, because stage is the same for all the
		// quote_to_le in l2o journey
		Optional<TeamsDRMultiQuoteLeBean> quoteLeBean = teamsDRQuoteDataBean.getQuoteToLes().stream()
				.filter(Objects::nonNull).findAny();
		quoteLeBean.ifPresent(quoteLe -> {
			teamsDRQuoteDataBean.setStage(quoteLe.getStage());
			teamsDRQuoteDataBean.setSubStage(quoteLe.getSubStage());
		});
		return teamsDRQuoteDataBean;
	}

	/**
	 * Subtract quoteTole with teamsdr prices
	 * @param quoteToLe
	 */
	protected void subtractQuoteToLeWithTeamsDRPrice(QuoteToLe quoteToLe) {
		List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if (Objects.nonNull(quoteTeamsDRS) && !quoteTeamsDRS.isEmpty()) {
			quoteTeamsDRS.stream().filter(quoteTeamsDR -> (Objects.isNull(quoteTeamsDR.getServiceName()) && Objects
					.nonNull(quoteTeamsDR.getProfileName()) && quoteTeamsDR.getProfileName()
					.contains(PLAN)) || MICROSOFT_LICENSE
					.equalsIgnoreCase(quoteTeamsDR.getServiceName()) || MEDIA_GATEWAY
					.equalsIgnoreCase(quoteTeamsDR.getServiceName())).forEach(quoteTeamsDR -> {
				teamsDRCumulativePricesBean
						.setTotalMrc(teamsDRCumulativePricesBean.getTotalMrc() + checkForNull(quoteTeamsDR.getMrc()));
				teamsDRCumulativePricesBean
						.setTotalNrc(teamsDRCumulativePricesBean.getTotalNrc() + checkForNull(quoteTeamsDR.getNrc()));
				teamsDRCumulativePricesBean
						.setTotalArc(teamsDRCumulativePricesBean.getTotalArc() + checkForNull(quoteTeamsDR.getArc()));
				teamsDRCumulativePricesBean
						.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv() + checkForNull(quoteTeamsDR.getTcv()));
			});

			quoteToLe.setProposedMrc(
					checkForNull(quoteToLe.getProposedMrc()) - teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setProposedNrc(
					checkForNull(quoteToLe.getProposedNrc()) - teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setProposedArc(
					checkForNull(quoteToLe.getProposedArc()) - teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setFinalMrc(checkForNull(quoteToLe.getFinalMrc()) - teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setFinalNrc(checkForNull(quoteToLe.getFinalNrc()) - teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setFinalArc(checkForNull(quoteToLe.getFinalArc()) - teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setTotalTcv(checkForNull(quoteToLe.getTotalTcv()) - teamsDRCumulativePricesBean.getTotalTcv());
		}
		quoteToLeRepository.save(quoteToLe);
	}

	/**
	 * Subtract plan and media gateway prices from quotetoLe
	 *
	 */
	private void subtractPlanAndMgPriceFromQuoteLe(QuoteToLe quoteToLe) {
		List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if (Objects.nonNull(quoteTeamsDRS) && !quoteTeamsDRS.isEmpty()) {
			quoteTeamsDRS.stream().filter(quoteTeamsDR -> (Objects.isNull(quoteTeamsDR.getServiceName()) && Objects
					.nonNull(quoteTeamsDR.getProfileName()) && quoteTeamsDR.getProfileName()
					.contains(PLAN)) || MEDIA_GATEWAY.equalsIgnoreCase(quoteTeamsDR.getServiceName()))
					.forEach(quoteTeamsDR -> {
						teamsDRCumulativePricesBean.setTotalMrc(
								teamsDRCumulativePricesBean.getTotalMrc() + checkForNull(quoteTeamsDR.getMrc()));
						teamsDRCumulativePricesBean.setTotalNrc(
								teamsDRCumulativePricesBean.getTotalNrc() + checkForNull(quoteTeamsDR.getNrc()));
						teamsDRCumulativePricesBean.setTotalArc(
								teamsDRCumulativePricesBean.getTotalArc() + checkForNull(quoteTeamsDR.getArc()));
						teamsDRCumulativePricesBean.setTotalTcv(
								teamsDRCumulativePricesBean.getTotalTcv() + checkForNull(quoteTeamsDR.getTcv()));
					});

			quoteToLe.setProposedMrc(
					checkForNull(quoteToLe.getProposedMrc()) - teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setProposedNrc(
					checkForNull(quoteToLe.getProposedNrc()) - teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setProposedArc(
					checkForNull(quoteToLe.getProposedArc()) - teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setFinalMrc(checkForNull(quoteToLe.getFinalMrc()) - teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setFinalNrc(checkForNull(quoteToLe.getFinalNrc()) - teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setFinalArc(checkForNull(quoteToLe.getFinalArc()) - teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setTotalTcv(checkForNull(quoteToLe.getTotalTcv()) - teamsDRCumulativePricesBean.getTotalTcv());
		}
		quoteToLeRepository.save(quoteToLe);
	}

	/**
	 *
	 *
	 */
    private void subtractLicensePriceFromQuoteLe(QuoteToLe quoteToLe){
        Optional<QuoteTeamsDR> quoteTeamsDROp = quoteTeamsDRRepository.findByQuoteToLeIdAndServiceName(quoteToLe.getId(), MICROSOFT_LICENSE);
        TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
        quoteTeamsDROp.ifPresent(quoteTeamsDR -> {
                teamsDRCumulativePricesBean.setTotalMrc(teamsDRCumulativePricesBean.getTotalMrc()+checkForNull(quoteTeamsDR.getMrc()));
                teamsDRCumulativePricesBean.setTotalNrc(teamsDRCumulativePricesBean.getTotalNrc()+checkForNull(quoteTeamsDR.getNrc()));
                teamsDRCumulativePricesBean.setTotalArc(teamsDRCumulativePricesBean.getTotalArc()+checkForNull(quoteTeamsDR.getArc()));
                teamsDRCumulativePricesBean.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv()+checkForNull(quoteTeamsDR.getTcv()));

            quoteToLe.setProposedMrc(checkForNull(quoteToLe.getProposedMrc()) - teamsDRCumulativePricesBean.getTotalMrc());
            quoteToLe.setProposedNrc(checkForNull(quoteToLe.getProposedNrc()) - teamsDRCumulativePricesBean.getTotalNrc());
            quoteToLe.setProposedArc(checkForNull(quoteToLe.getProposedArc()) - teamsDRCumulativePricesBean.getTotalArc());

            quoteToLe.setFinalMrc(checkForNull(quoteToLe.getFinalMrc()) - teamsDRCumulativePricesBean.getTotalMrc());
            quoteToLe.setFinalNrc(checkForNull(quoteToLe.getFinalNrc()) - teamsDRCumulativePricesBean.getTotalNrc());
            quoteToLe.setFinalArc(checkForNull(quoteToLe.getFinalArc()) - teamsDRCumulativePricesBean.getTotalArc());

            quoteToLe.setTotalTcv(checkForNull(quoteToLe.getTotalTcv()) - teamsDRCumulativePricesBean.getTotalTcv());
            quoteToLeRepository.save(quoteToLe);
        });
    }


    /**
	 * Method to handle tata voice yes / No
	 * @param teamsDRQuoteDataBean
	 */
	private void handleTataVoiceYesOrNo(TeamsDRQuoteDataBean teamsDRQuoteDataBean){
		//to handle is tata voice - yes to no
		AtomicReference<String> isTataVoiceNeeded = new AtomicReference<>();
		teamsDRQuoteDataBean.getQuoteToLes().stream().filter(quoteToLe -> Objects.nonNull(quoteToLe.getTeamsDRSolution())
				&& Objects.nonNull(quoteToLe.getTeamsDRSolution().getTeamsDRServices()))
				.flatMap(quoteToLe -> quoteToLe.getTeamsDRSolution().getTeamsDRServices().stream())
				.filter(services -> Objects.nonNull(services.getComponents()) && !services.getComponents().isEmpty())
				.flatMap(services -> services.getComponents().stream())
				.filter(component -> Objects.nonNull(component) && Objects.nonNull(component.getAttributes()))
				.flatMap(component -> component.getAttributes().stream())
				.anyMatch(attribute-> {
					if(TATA_VOICE_NEEDED_ATTRIBUTE.equalsIgnoreCase(attribute.getName()))
					{
						isTataVoiceNeeded.set(attribute.getAttributeValues());
						return true;
					} else return false;
				});
		if(CommonConstants.NO.equalsIgnoreCase(isTataVoiceNeeded.get()))
			gscMultiLEQuoteService.checkForVoiceAndDeleteIfExists(teamsDRQuoteDataBean);
	}

	/**
	 * Method to update quote data bean for newly created solutions.
	 * @param initiallySavedIds
	 * @param teamsDRQuoteDataBean
	 */
	private void updateQuoteDataBean(Set<Integer> initiallySavedIds, TeamsDRQuoteDataBean teamsDRQuoteDataBean,MstProductFamily productFamily){
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(teamsDRQuoteDataBean.getQuoteId());
		Set<Integer> currentIds = quoteToLes.stream().map(QuoteToLe::getId).collect(Collectors.toSet());

		Set<Integer> toAddInBean = Sets.difference(currentIds,initiallySavedIds);
		if(!toAddInBean.isEmpty()){
			quoteToLes.forEach(quoteToLe -> {
				if(toAddInBean.contains(quoteToLe.getId())){
					LOGGER.info("QuoteToLe to be added in bean :: {}",quoteToLe.getId());
					TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean = new TeamsDRMultiQuoteLeBean();
					teamsDRMultiQuoteLeBean.setQuoteleId(quoteToLe.getId());
					updateMultiQuoteLeBean(teamsDRQuoteDataBean,teamsDRMultiQuoteLeBean,productFamily,
							null,MICROSOFT_CLOUD_SOLUTIONS);
					teamsDRQuoteDataBean.getQuoteToLes().add(teamsDRMultiQuoteLeBean);
				}
			});
		}
	}

	/**
	 * Method to handle new teamsdr solutions.
	 *
	 * @param teamsDRQuoteDataBean
	 * @param productFamily
	 */
	private void createNewTeamsDRSolutions(TeamsDRQuoteDataBean teamsDRQuoteDataBean, MstProductFamily productFamily,
			QuoteToLe parentQuoteTole) {
		try {
			MstProductFamily gscProductFamily = getProductFamily(GSIP_PRODUCT_NAME);
			boolean hitRuleEngineFlag = false;

			// Parent Quote to le pf
			Optional<QuoteToLeProductFamily> parentQtleProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLe(parentQuoteTole.getId()).stream()
					.filter(quoteToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS
							.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
					.findAny();

			// teamsdr new solutions..
			if (Objects.nonNull(teamsDRQuoteDataBean.getNewTeamsDRSolutions())
					&& !teamsDRQuoteDataBean.getNewTeamsDRSolutions().isEmpty()) {
				hitRuleEngineFlag = true;
				teamsDRQuoteDataBean.getNewTeamsDRSolutions()
						.forEach(teamsDRServicesBean -> createSolutions(teamsDRQuoteDataBean, teamsDRServicesBean,
								parentQuoteTole, productFamily, parentQtleProductFamily.get()));
			}

			// gsc new solutions..
			if (Objects.nonNull(teamsDRQuoteDataBean.getNewGscSolutions())
					&& !teamsDRQuoteDataBean.getNewGscSolutions().isEmpty()) {

				// Gsc quote to le pf
				Optional<QuoteToLeProductFamily> gscQtlePf = quoteToLeProductFamilyRepository
						.findByQuoteToLe(parentQuoteTole.getId()).stream()
						.filter(quoteToLeProductFamily -> GSIP_PRODUCT_NAME
								.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
						.findAny();

				// if gsc quote to le not present creating new one with parent qtle.
				QuoteToLeProductFamily newQuoteToLePf = gscQtlePf
						.orElseGet(() -> constructQuoteToLeProductFamily(gscProductFamily, parentQuoteTole));
				hitRuleEngineFlag = true;
				teamsDRQuoteDataBean.getNewGscSolutions().forEach(multipleLESolutionBean -> {
					multipleLESolutionBean.getGscSolutions().forEach(gscSolutionBean -> {
						// To be done later...
					});
				});
			}

			// hitting rule engine to handle new solutions.
			if (hitRuleEngineFlag)
				segregateBasedOnSolutions(teamsDRQuoteDataBean.getQuoteId());

		} catch (TclCommonException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to construct new quote to le for new solutions.
	 * @param teamsDRQuoteDataBean
	 * @param quote
	 * @return
	 */
	private QuoteToLe constructNewQuoteToLe(TeamsDRQuoteDataBean teamsDRQuoteDataBean,Quote quote){
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setCurrencyCode(LeAttributesConstants.USD);
		quoteToLe.setStage(QuoteStageConstants.SELECT_SERVICES.getConstantCode());
		quoteToLe.setTermInMonths(TWELVE_MONTHS);
//		quoteToLe.setQuoteType(teamsDRQuoteDataBean.getQuoteType());
//		quoteToLe.setQuoteCategory(Objects.isNull(teamsDRQuoteDataBean.getQuoteCategory()) ? null
//				: teamsDRQuoteDataBean.getQuoteCategory());
		// setting null for now..
		quoteToLe.setClassification(null);
		return quoteToLeRepository.save(quoteToLe);
	}

	/**
	 * Delete license attributes by missing ID
	 *
	 * @param quoteLeId
	 * @param licenseAttributes
	 */
	private void deleteLicenseAttributes(Integer quoteLeId, List<TeamsDRAttributesBean> licenseAttributes) {
		Set<Integer> requestIds = licenseAttributes.stream().map(TeamsDRAttributesBean::getAttributeId)
				.collect(Collectors.toSet());

		Set<Integer> savedIds = quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteLeId).stream()
				.filter(quoteLeAttributeValue -> TeamsDRConstants.EXISTING_LICENSE_ATTRIBUTES
						.contains(quoteLeAttributeValue.getMstOmsAttribute().getName()))
				.map(QuoteLeAttributeValue::getId).collect(Collectors.toSet());

		Sets.difference(savedIds, requestIds).forEach(id -> {
			quoteLeAttributeValueRepository.deleteById(id);
		});
	}

	/**
	 * Get quoteToLe Product family details for given product family
	 *
	 * @param quoteToLe
	 * @param productFamily
	 * @return
	 * @throws TclCommonException
	 */
	private QuoteToLeProductFamily getQuoteToLeProductFamily(QuoteToLe quoteToLe, MstProductFamily productFamily)
			throws TclCommonException {
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
		if (Objects.isNull(quoteToLeProductFamily))
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);
		return quoteToLeProductFamily;
	}

	/**
	 * Check for any change in product solutions and update them
	 *
	 * @param teamsDRQuoteDataBean
	 * @param quoteToLe
	 * @param productFamily
	 * @param quoteToLeProductFamily
	 * @throws TclCommonException
	 */
	private void checkAndProcessTeamsDRProductSolutions(TeamsDRQuoteDataBean teamsDRQuoteDataBean,
														TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean,
														QuoteToLe quoteToLe,
														MstProductFamily productFamily, QuoteToLeProductFamily quoteToLeProductFamily) throws TclCommonException {

		// finding difference between existing solutionIDs and current solutionIDs and
		// deleting them
		Set<Integer> savedSolutionIds = productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily)
				.stream().map(ProductSolution::getId).collect(Collectors.toSet());
		Set<Integer> requestSolutionIds = teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
				.map(TeamsDRServicesBean::getSolutionId).collect(Collectors.toSet());

		Set<Integer> solutionsToDelete = Sets.difference(savedSolutionIds, requestSolutionIds);

		updateQuoteTeamsDR(teamsDRQuoteDataBean,teamsDRMultiQuoteLeBean);

		LOGGER.info("Solution IDs to be deleted {}", Utils.convertObjectToJson(solutionsToDelete));
		deleteProductSolutions(solutionsToDelete,quoteToLe);

		// creating new Solutions for new services
		List<TeamsDRServicesBean> newSolutions = teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
				.filter(teamsDRService -> Objects.isNull(teamsDRService.getSolutionId())).collect(Collectors.toList());

		newSolutions.forEach(solution -> {
			if(!solution.getOfferingName().contains(PLAN)){
				createSolutions(teamsDRQuoteDataBean, solution, quoteToLe, productFamily, quoteToLeProductFamily);
			}else{
				quoteTeamsDRRepository.findByQuoteToLeIdAndServiceName(quoteToLe.getId(),null).ifPresent(quoteTeamsDR -> {
					ProductSolution planSolution = quoteTeamsDR.getProductSolution();
					solution.setSolutionId(planSolution.getId());
					solution.setSolutionCode(planSolution.getSolutionCode());
					User user = getUserId(Utils.getSource());
					MstProductComponent productComponent = getProductComponent(solution.getOfferingName(), user);
					quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteTeamsDR.getId(), quoteTeamsDR.getProfileName(),
							TEAMSDR_CONFIG_ATTRIBUTES)
							.stream().findFirst().ifPresent(quoteProductComponent -> {
						quoteProductComponent.setMstProductComponent(productComponent);
						quoteProductComponentRepository.save(quoteProductComponent);
					});
					quoteTeamsDR.setProfileName(solution.getPlan());
					quoteTeamsDRRepository.save(quoteTeamsDR);
					solution.setQuoteTeamsDRId(quoteTeamsDR.getId());
				});
			}
		});

		// calculating difference in prices and storing them in QuoteToLe
		//differenceInPrices(teamsDRMultiQuoteLeBean, quoteToLe);

		//Updating Config parameters
		updateConfigAttributesAndServiceAttributes(teamsDRMultiQuoteLeBean);

		// saving updated productSolutions in bean and sending to UI
		teamsDRMultiQuoteLeBean.getTeamsDRSolution()
				.setTeamsDRServices(productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily)
						.stream().map(productSolution -> {
							return createProductSolutionBean(teamsDRQuoteDataBean,productSolution);
						}).collect(Collectors.toList()));
		//teamsDRMultiQuoteLeBean.setLegalEntities(getLegalEntities(quoteToLe.getQuote()));
		setLegalEntityAttributes(quoteToLe,teamsDRMultiQuoteLeBean);
	}

	/**
	 * Method to update teamsdrservice
	 * @param teamsDRQuoteDataBean
	 * @param teamsDRMultiQuoteLeBean
	 */
	private void updateQuoteTeamsDR(TeamsDRQuoteDataBean teamsDRQuoteDataBean,TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean){
		if(Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution())){
			if(Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices()) &&
					!teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().isEmpty()){
				teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().forEach(teamsDRServicesBean -> {
					QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.
							findByProductSolutionIdAndStatus(teamsDRServicesBean.getSolutionId(),CommonConstants.BACTIVE);
					if(Objects.nonNull(quoteTeamsDR)){
						if(Objects.isNull(quoteTeamsDR.getServiceName()) ||
								(!MICROSOFT_LICENSE.equals(quoteTeamsDR.getServiceName()) &&
										!MEDIA_GATEWAY.equals(quoteTeamsDR.getServiceName()))){
							quoteTeamsDR.setNoOfUsers(teamsDRServicesBean.getNoOfUsers());
							quoteTeamsDRRepository.save(quoteTeamsDR);
						}
					}
				});
			}
		}
	}

	/**
	 * Method for updating the config attributes
	 *
	 * @param teamsDRMultiQuoteLeBean
	 */
	public void updateConfigAttributesAndServiceAttributes(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean) {
		teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
				.forEach(teamsDRServicesBean -> {
					if (teamsDRServicesBean.getOfferingName().equals(teamsDRServicesBean.getPlan())) {
						QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findByProductSolutionIdAndStatus(
								teamsDRServicesBean.getSolutionId(), CommonConstants.BACTIVE);
						if (Objects.nonNull(quoteTeamsDR)) {
							if (Objects.nonNull(teamsDRServicesBean.getConfigurations())) {
								teamsDRServicesBean.getConfigurations().forEach(teamsDRConfigurationBean -> {
									try {
										if (Objects.nonNull(teamsDRConfigurationBean.getComponents()) &&
												!teamsDRConfigurationBean.getComponents().isEmpty()) {
											processProductComponent(teamsDRConfigurationBean.getId(),
													teamsDRConfigurationBean.getComponents().get(0), TEAMSDR_SERVICE_ATTRIBUTES);
										}
									} catch (TclCommonException e) {
										e.printStackTrace();
									}
								});
							}
							try {
								processProductComponent(quoteTeamsDR.getId(), teamsDRServicesBean.getComponents().get(0),
										TEAMSDR_CONFIG_ATTRIBUTES);
							} catch (TclCommonException e) {
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
							}
						}
					} else if (CUSTOM_PLAN.equalsIgnoreCase(teamsDRServicesBean.getPlan())) {
						QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findByProductSolutionIdAndStatus(
								teamsDRServicesBean.getSolutionId(), CommonConstants.BACTIVE);
						try {
							processProductComponent(quoteTeamsDR.getId(), teamsDRServicesBean.getComponents().get(0),
									TEAMSDR_CONFIG_ATTRIBUTES);
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
						}
					}
				});
	}

	/**
	 * Find difference in price
	 * @param teamsDRMultiQuoteLeBean
	 * @param quoteToLe
	 */
	private void differenceInPrices(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean, QuoteToLe quoteToLe) {

		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		teamsDRCumulativePricesBean.setMrc(0.0);
		teamsDRCumulativePricesBean.setNrc(0.0);
		teamsDRCumulativePricesBean.setArc(0.0);
		teamsDRCumulativePricesBean.setTcv(0.0);

		teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
				.filter(teamsDRService -> teamsDRService.getOfferingName().equals(teamsDRService.getPlan()))
				.forEach(teamsDRService -> {
					Optional<ProductSolution> productSolution = productSolutionRepository
							.findById(teamsDRService.getSolutionId());
					List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository
							.findByProductSolutionAndStatus(productSolution.get(), CommonConstants.BACTIVE);
					quoteTeamsDRs.forEach(quoteTeamsDR -> {
						teamsDRCumulativePricesBean
								.setMrc(teamsDRCumulativePricesBean.getMrc() + checkForNull(quoteTeamsDR.getMrc()));
						teamsDRCumulativePricesBean
								.setNrc(teamsDRCumulativePricesBean.getNrc() + checkForNull(quoteTeamsDR.getNrc()));
						teamsDRCumulativePricesBean
								.setArc(teamsDRCumulativePricesBean.getArc() + checkForNull(quoteTeamsDR.getArc()));
						teamsDRCumulativePricesBean
								.setTcv(teamsDRCumulativePricesBean.getTcv() + checkForNull(quoteTeamsDR.getTcv()));
					});
				});

		double changeInMrc = checkForNull(quoteToLe.getFinalMrc()) - teamsDRCumulativePricesBean.getMrc();
		double changeInNrc = checkForNull(quoteToLe.getFinalNrc()) - teamsDRCumulativePricesBean.getNrc();
		LOGGER.info("Change in MRC and NRC values : {}, {}", changeInMrc, changeInNrc);

		quoteToLe.setProposedMrc(calculateNewValue(changeInMrc, checkForNull(quoteToLe.getProposedMrc())));
		quoteToLe.setProposedNrc(calculateNewValue(changeInNrc, checkForNull(quoteToLe.getProposedNrc())));
		quoteToLe.setFinalMrc(calculateNewValue(changeInMrc, checkForNull(quoteToLe.getFinalMrc())));
		quoteToLe.setFinalNrc(calculateNewValue(changeInNrc, checkForNull(quoteToLe.getFinalNrc())));

		quoteToLe.setProposedArc(12 * checkForNull(quoteToLe.getProposedMrc()));
		quoteToLe.setFinalArc(12 * checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setTotalTcv(quoteToLe.getFinalMrc() + quoteToLe.getFinalNrc());

		quoteToLeRepository.save(quoteToLe);
	}

	/**
	 * Check for change in product solutions
	 *
	 * @param teamsDRService
	 */
	private void changeInProductSolutions(TeamsDRServicesBean teamsDRService) {
		Optional<ProductSolution> productSolutionEntity = productSolutionRepository
				.findById(teamsDRService.getSolutionId());
		if (productSolutionEntity.isPresent()) {
			List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository
					.findByProductSolutionAndStatus(productSolutionEntity.get(), CommonConstants.BACTIVE);
			quoteTeamsDRs.forEach(quoteTeamsDR -> {
				if (Objects.nonNull(teamsDRService.getNoOfUsers())
						&& !teamsDRService.getNoOfUsers().equals(quoteTeamsDR.getNoOfUsers())) {
					LOGGER.info("Change in noOfUsers detected for {}", teamsDRService.getOfferingName());
					quoteTeamsDR.setNoOfUsers(teamsDRService.getNoOfUsers());
					quoteTeamsDR.setMrc(teamsDRService.getMrc());
					quoteTeamsDR.setNrc(teamsDRService.getNrc());
					quoteTeamsDR.setArc(teamsDRService.getArc());
					quoteTeamsDR.setTcv(teamsDRService.getTcv());
					quoteTeamsDRRepository.save(quoteTeamsDR);
					try {
						productSolutionEntity.get().setProductProfileData(Utils.convertObjectToJson(teamsDRService));
						productSolutionRepository.save(productSolutionEntity.get());
					} catch (TclCommonException e) {
						LOGGER.info(e.getMessage());
					}
				}
			});
		}
	}

	/**
	 * To delete removed product solutions
	 *
	 * @param solutionsToDelete
	 */
	private void deleteProductSolutions(Set<Integer> solutionsToDelete,QuoteToLe quoteToLe){

		List<ProductSolution> productSolutions = productSolutionRepository.findAllById(solutionsToDelete);
		productSolutions.forEach(productSolution -> {
			QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findByProductSolutionAndStatus(productSolution,
					CommonConstants.BACTIVE).stream().findAny().get();
			if(Objects.nonNull(quoteTeamsDR.getServiceName())){
				LOGGER.info("QuoteTeamsDr for delete :: {}",quoteTeamsDR.getServiceName());
				if(MICROSOFT_LICENSE.equals(quoteTeamsDR.getServiceName())){
					deleteQuoteProductComponentAndValues(quoteTeamsDR.getId(),TEAMSDR_LICENSE_ATTRIBUTES);
					List<QuoteTeamsLicense> quoteTeamsLicenses = quoteTeamsLicenseRepository.findByQuoteTeamsDR(quoteTeamsDR);
					quoteTeamsLicenses.forEach(quoteTeamsLicense -> {
						deleteQuoteProductComponentAndValues(quoteTeamsLicense.getId(),TEAMSDR_LICENSE_CHARGES);
						quoteTeamsLicenseRepository.delete(quoteTeamsLicense);
					});
				} else if(!MEDIA_GATEWAY.equals(quoteTeamsDR.getServiceName())){

					// Deleting the config attributes if present.
					deleteQuoteProductComponentAndValues(quoteTeamsDR.getId(),TEAMSDR_CONFIG_ATTRIBUTES);
				}
				quoteTeamsDRRepository.delete(quoteTeamsDR);

				if(Objects.nonNull(productSolution.getTpsSfdcProductId())){
					// to delete the product service in sfdc
					teamsDRSfdcService.deleteProductServiceInSfdc(quoteToLe,productSolution);
				}
				productSolutionRepository.delete(productSolution);
			}
		});
	}

	/**
	 * Method to delete quote product component ,
	 * Quote product component attribute values, and quoteprice
	 * @param referenceId
	 * @throws TclCommonException
	 */
	private void deleteQuoteProductComponentAndValues(Integer referenceId,String referenceName) {
		MstProductFamily mstProductFamily = null;
		try {
			mstProductFamily = getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
			List<QuoteProductComponent> components = quoteProductComponentRepository
					.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId,mstProductFamily,referenceName);
			components.forEach(component->{
				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList =
						quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(component);
				quoteProductComponentsAttributeValueList.forEach(quoteProductComponentsAttributeValue -> {
					if(ALL_CHARGES.contains(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName())){
						List<QuotePrice> quotePrices = quotePriceRepository.
								findByReferenceNameAndReferenceId(ATTRIBUTES,quoteProductComponentsAttributeValue.getId().toString());
						quotePriceRepository.deleteAll(quotePrices);
					}
					// to delete additional service param...
					if(Objects.nonNull(quoteProductComponentsAttributeValue.getIsAdditionalParam()) && Objects
							.nonNull(quoteProductComponentsAttributeValue.getAttributeValues())) {
						additionalServiceParamRepository
								.findById(Integer.valueOf(quoteProductComponentsAttributeValue.getAttributeValues()))
								.ifPresent(additionalServiceParams ->
										additionalServiceParamRepository.delete(additionalServiceParams));
					}
				});
				quoteProductComponentsAttributeValueRepository.deleteAll(quoteProductComponentsAttributeValueList);
			});
			quoteProductComponentRepository.deleteAll(components);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
	}

	/**
	 * Creating product solution bean for teams DR
	 *
	 * @param productSolution
	 * @return
	 */
	public TeamsDRServicesBean createProductSolutionBean(TeamsDRQuoteDataBean teamsDRQuoteDataBean,ProductSolution productSolution) {
		TeamsDRServicesBean teamsDRServicesBean = new TeamsDRServicesBean();
		teamsDRServicesBean.setSolutionId(productSolution.getId());
		teamsDRServicesBean.setSolutionCode(productSolution.getSolutionCode());
		teamsDRServicesBean.setOfferingName(productSolution.getMstProductOffering().getProductName());
		List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository.findByProductSolutionAndStatus(productSolution,
				CommonConstants.BACTIVE);
		if (Objects.nonNull(quoteTeamsDRs) && !quoteTeamsDRs.isEmpty()) {
			quoteTeamsDRs.forEach(quoteTeamsDR -> {
				teamsDRServicesBean.setQuoteTeamsDRId(quoteTeamsDR.getId());
				if (quoteTeamsDR.getServiceName() == null) {
					teamsDRServicesBean.setOfferingType(TeamsDRConstants.BUNDLED);
					teamsDRServicesBean.setOfferingName(quoteTeamsDR.getProfileName());
					teamsDRServicesBean.setConfigurations(getTeamsDRConfigurations(quoteTeamsDR));
				} else if (TeamsDRConstants.ADDON_SERVICES.equals(quoteTeamsDR.getProfileName())) {
					teamsDRServicesBean.setOfferingType(TeamsDRConstants.ADDON_SERVICES);
					teamsDRServicesBean.setOfferingName(quoteTeamsDR.getServiceName());
				} else {
					teamsDRServicesBean.setOfferingType(TeamsDRConstants.ATOMIC);
					teamsDRServicesBean.setOfferingName(quoteTeamsDR.getServiceName());
					if(quoteTeamsDR.getServiceName().equals(MEDIA_GATEWAY)){
						teamsDRServicesBean.setOfferingType(null);
						teamsDRServicesBean.setMgConfigurations(getMediaGatewayDetails(quoteTeamsDR));
					}else if(quoteTeamsDR.getServiceName().equals(MICROSOFT_LICENSE)){
						teamsDRServicesBean.setOfferingType(null);
						getQuoteTeamsLicense(quoteTeamsDR,teamsDRServicesBean);
					}
				}
				teamsDRServicesBean.setPlan(quoteTeamsDR.getProfileName());
				teamsDRServicesBean.setNoOfUsers(quoteTeamsDR.getNoOfUsers());
				teamsDRServicesBean.setMrc(quoteTeamsDR.getMrc());
				teamsDRServicesBean.setNrc(quoteTeamsDR.getNrc());
				teamsDRServicesBean.setArc(quoteTeamsDR.getArc());
				teamsDRServicesBean.setTcv(quoteTeamsDR.getTcv());
				try {
					teamsDRServicesBean.setComponents(getComponentDetail(quoteTeamsDR.getId(),TEAMSDR_CONFIG_ATTRIBUTES));


					// To handle back and forth operation for license...
					if(!teamsDRServicesBean.getComponents().isEmpty() && Objects.isNull(quoteTeamsDR.getServiceName())){
						teamsDRServicesBean.getComponents().get(0).getAttributes().forEach(attribute ->{
							if(LICENSE_REQUIRED.equals(attribute.getName())){
								if(CommonConstants.NO.equalsIgnoreCase(attribute.getAttributeValues())){
									deletelicenseSolutionAndComponents(teamsDRQuoteDataBean);
								}
							}

							if(TATA_VOICE_NEEDED_ATTRIBUTE.equals(attribute.getName())){
								if(CommonConstants.NO.equalsIgnoreCase(attribute.getAttributeValues())){
									gscMultiLEQuoteService.checkForVoiceAndDeleteIfExists(teamsDRQuoteDataBean);
								}
							}
						});
					}
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
		} else {
			if (productSolution.getMstProductOffering().getProductName().contains(MEDIA_GATEWAY)) {
				// To be uncommented when media gateaway comes into picture.
//				getQuoteDirectRoutings(productSolution, teamsDRServicesBean);
			} else if (productSolution.getMstProductOffering().getProductName().equals(TeamsDRConstants.MS_LICENSE)) {
//				getQuoteTeamsLicense(productSolution, teamsDRServicesBean);
			}
		}
		return teamsDRServicesBean;
	}

	/**
	 * Method to handle back and forth deletion operation of license..
	 * @param teamsDRQuoteDataBean
	 */
	private void deletelicenseSolutionAndComponents(TeamsDRQuoteDataBean teamsDRQuoteDataBean){

		boolean licenseFound = false;
		List<QuoteTeamsDR> quoteTeamsDRS = null;

		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(teamsDRQuoteDataBean.getQuoteId());

		for (QuoteToLe quoteToLe : quoteToLes) {
			quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
			if(!quoteTeamsDRS.isEmpty()){
				for (QuoteTeamsDR teamsDR : quoteTeamsDRS) {
					if(MICROSOFT_LICENSE.equals(teamsDR.getServiceName())){
						List<QuoteTeamsLicense> quoteTeamsLicenses = quoteTeamsLicenseRepository.findByQuoteTeamsDR(teamsDR);
						if(!quoteTeamsLicenses.isEmpty()){
							quoteTeamsLicenses.forEach(quoteTeamsLicense -> {
								deleteQuoteProductComponentAndValues(quoteTeamsLicense.getId(),TEAMSDR_LICENSE_CHARGES);
								quoteTeamsLicenseRepository.delete(quoteTeamsLicense);
							});
						}
						productSolutionRepository.findById(teamsDR.getProductSolution().getId()).ifPresent(productSolution -> {
							if(Objects.nonNull(productSolution.getTpsSfdcProductName())){
								teamsDRSfdcService.deleteProductServiceInSfdc(quoteToLe,productSolution);
							}
							productSolutionRepository.delete(productSolution);
						});
						quoteTeamsDRRepository.delete(teamsDR);
						licenseFound = true;
						break;
					}
				}
			}
			if(licenseFound){
				// If only license component was present.. deleting the quotetole and its components..
				if(quoteTeamsDRS.size() == 1){
					List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId());
					quoteToLeProductFamilies.forEach(quoteToLeProductFamily -> {
						List<ProductSolution> productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
						productSolutionRepository.deleteAll(productSolutions);
					});
					quoteToLeProductFamilyRepository.deleteAll(quoteToLeProductFamilies);
					List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
					quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValues);
					quoteLeCreditCheckAuditRepository.deleteAll(quoteLeCreditCheckAuditRepository.findByQuoteToLe_id(quoteToLe.getId()));
					teamsDRSfdcService.deleteOpportunity(quoteToLe);
					teamsDRSfdcService.deleteIncompleteRequests(quoteToLe);
					quoteToLeRepository.delete(quoteToLe);
				}
				break;
			}
		}
	}

	/**
	 * Method to get all the media gateway configurations
	 * @param quoteTeamsDR
	 * @return
	 */
	private List<MediaGatewayConfigurationBean> getMediaGatewayDetails(QuoteTeamsDR quoteTeamsDR){
		List<QuoteDirectRouting> quoteDirectRoutings = quoteDirectRoutingRepository.findByQuoteTeamsDR(quoteTeamsDR);
		List<MediaGatewayConfigurationBean> configurations = new ArrayList<>();
		quoteDirectRoutings.forEach(quoteDirectRouting -> {
			MediaGatewayConfigurationBean configurationBean = new MediaGatewayConfigurationBean();
			List<TeamsDRCityBean> sites = new ArrayList<>();
			List<QuoteDirectRoutingCity> quoteDirectRoutingCityList = quoteDirectRoutingCityRepository.
					findByQuoteDirectRoutingId(quoteDirectRouting.getId());
			quoteDirectRoutingCityList.forEach(quoteDirectRoutingCity -> {
				sites.add(toTeamsdrCityDetailsBean(quoteDirectRoutingCity));
			});
			configurationBean.setCities(sites);
			configurationBean.setCountry(quoteDirectRouting.getCountry());
			configurationBean.setId(quoteDirectRouting.getId());
			configurationBean.setArc(quoteDirectRouting.getArc());
			configurationBean.setNrc(quoteDirectRouting.getNrc());
			configurationBean.setTcv(quoteDirectRouting.getTcv());
			configurationBean.setMrc(quoteDirectRouting.getMrc());
			configurations.add(configurationBean);
		});
		return configurations;
	}

	/**
	 * Method to get all the quoteTeamsDrDetails
	 * @param quoteTeamsDR
	 * @return
	 */
	public List<TeamsDRConfigurationBean> getTeamsDRConfigurations(QuoteTeamsDR quoteTeamsDR){
		LOGGER.info("QuoteTeamsDR ID::{}",quoteTeamsDR.getId());
		return quoteTeamsDRDetailsRepository.findByQuoteTeamsDR(quoteTeamsDR).stream().
				map(quoteTeamsDRDetails -> {
					try {
						return toQuoteTeamsdrDetailsBean(quoteTeamsDRDetails,null);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
					}
				}).collect(Collectors.toList());
	}

	/**
	 * Method to add license configurations in get quote.
	 * @param quoteTeamsDR
	 * @param teamsDRServicesBean
	 * @return
	 */
	private TeamsDRServicesBean getQuoteTeamsLicense(QuoteTeamsDR quoteTeamsDR,
			TeamsDRServicesBean teamsDRServicesBean) {

		List<QuoteTeamsLicense> licenses = quoteTeamsLicenseRepository.findByQuoteTeamsDR(quoteTeamsDR);
		if (Objects.nonNull(licenses) && !licenses.isEmpty()) {
			TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
			AtomicReference<String> agreementType = new AtomicReference<>("");
			licenses.stream().findAny().ifPresent(license -> agreementType.set(license.getAgreementType()));
			TeamsDRLicenseComponentsBean teamsDRLicenseComponentsBean = new TeamsDRLicenseComponentsBean();
			teamsDRLicenseComponentsBean.setAgreementType(agreementType.get());
			Map<String, TeamsDRLicenseConfigurationBean> componentBeanMap = new HashMap<>();
			licenses.forEach(quoteTeamsLicense -> {
				if (componentBeanMap.containsKey(quoteTeamsLicense.getProvider())) {
					TeamsDRLicenseConfigurationBean componentBean = componentBeanMap
							.get(quoteTeamsLicense.getProvider());
					componentBean.getLicenseDetails().add(toTeamsDRLicenseBean(quoteTeamsLicense));
				} else {
					TeamsDRLicenseConfigurationBean componentBean = new TeamsDRLicenseConfigurationBean();
					componentBean.setProvider(quoteTeamsLicense.getProvider());
					try {
						componentBean.setLicenseComponents(getComponentDetail(quoteTeamsDR.getId(),
								TEAMSDR_LICENSE_ATTRIBUTES));
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					componentBean.setLicenseDetails(new ArrayList<>());
					componentBean.getLicenseDetails().add(toTeamsDRLicenseBean(quoteTeamsLicense));
					componentBeanMap.put(quoteTeamsLicense.getProvider(), componentBean);
				}

				teamsDRCumulativePricesBean
						.setMrc(checkForNull(quoteTeamsLicense.getMrc()) + teamsDRCumulativePricesBean.getMrc());
				teamsDRCumulativePricesBean
						.setNrc(checkForNull(quoteTeamsLicense.getNrc()) + teamsDRCumulativePricesBean.getNrc());
				teamsDRCumulativePricesBean
						.setArc(checkForNull(quoteTeamsLicense.getArc()) + teamsDRCumulativePricesBean.getArc());
				teamsDRCumulativePricesBean
						.setTcv(checkForNull(quoteTeamsLicense.getTcv()) + teamsDRCumulativePricesBean.getTcv());
			});
			teamsDRLicenseComponentsBean.setLicenseConfigurations(new ArrayList<>(componentBeanMap.values()));
			teamsDRServicesBean.setLicenseComponents(teamsDRLicenseComponentsBean);
			teamsDRServicesBean.setMrc(teamsDRCumulativePricesBean.getMrc());
			teamsDRServicesBean.setNrc(teamsDRCumulativePricesBean.getNrc());
			teamsDRServicesBean.setArc(teamsDRCumulativePricesBean.getArc());
			teamsDRServicesBean.setTcv(teamsDRCumulativePricesBean.getTcv());
			LOGGER.info("TeamsDRLicenseComponentsBean :" + teamsDRLicenseComponentsBean);
		}
		return teamsDRServicesBean;
	}

	/**
	 * Get teams DR quote data
	 *
	 * @param quoteId
	 * @param isFilterNeeded
	 * @param productFamily
	 * @return
	 * @throws TclCommonException
	 */
	public TeamsDRQuoteDataBean getTeamsDRQuote(Integer quoteId, Boolean isFilterNeeded, String productFamily)
			throws TclCommonException {
		TeamsDRQuoteDataBean teamsDRQuoteDataBean = getQuoteData(quoteId);
		getProductSolutions(teamsDRQuoteDataBean, isFilterNeeded, productFamily);
		return teamsDRQuoteDataBean;
	}

	/**
	 * Get quote and quote to le data
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private TeamsDRQuoteDataBean getQuoteData(Integer quoteId) throws TclCommonException {
		LOGGER.info("Inside getQuoteData()");
		TeamsDRQuoteDataBean teamsDRQuoteDataBean = new TeamsDRQuoteDataBean();
		teamsDRQuoteDataBean.setAccessType(GscConstants.PUBLIC_IP);
		Set<Integer> customerLeIds = new HashSet<>();
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (Objects.nonNull(quote)) {
			teamsDRQuoteDataBean.setQuoteId(quoteId);
			teamsDRQuoteDataBean.setQuoteCode(quote.getQuoteCode());
			teamsDRQuoteDataBean.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		} else {
			teamsDRQuoteDataBean.setEngagementOptyId(quote.getEngagementOptyId());
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		final List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		List<QuoteLeAttributeValue> adminChangedPrice = quoteLeAttributeValueRepository
				.findAttributesByQuoteIDAndMstOmsAttributeName(quoteId, LeAttributesConstants.ADMIN_CHANGED_PRICE);
		teamsDRQuoteDataBean.setIsAdminChangedPrice(Objects.nonNull(adminChangedPrice) && !adminChangedPrice.isEmpty());
		List<QuoteLeAttributeValue> effectiveMsaDate = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLes.stream().findAny().get(),
				EFFECTIVE_MSA_DATE);
		if(Objects.nonNull(effectiveMsaDate) && !effectiveMsaDate.isEmpty()){
			teamsDRQuoteDataBean.setEffectiveMSADate(effectiveMsaDate.get(0).getAttributeValue());
		}
		List<TeamsDRMultiQuoteLeBean> teamsDRMultiQuoteLeBeans = new ArrayList<>();
		if (Objects.nonNull(quoteToLes) && !quoteToLes.isEmpty()) {
			List<Integer> quoteLeIds = new ArrayList<>();
			quoteToLes.forEach(quoteToLe -> {
				TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean = new TeamsDRMultiQuoteLeBean(quoteToLe);
				if(Objects.nonNull(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId()))
					customerLeIds.add(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId());
				quoteLeIds.add(teamsDRMultiQuoteLeBean.getQuoteleId());
				teamsDRMultiQuoteLeBeans.add(teamsDRMultiQuoteLeBean);
			});
			//check if quoteToLe is signed through manual cof
			List<CofDetails> cofDetails = cofDetailsRepository.findByOrderUuidIs(quote.getQuoteCode());
			if (!cofDetails.isEmpty()) {
				cofDetails.forEach(cofDetail -> {
					teamsDRMultiQuoteLeBeans.stream().anyMatch(quoteLeBean -> {
						if (CommonConstants.ORDER_LE_CODE.equalsIgnoreCase(cofDetail.getReferenceType()) && quoteLeBean
								.getQuoteLeCode().equalsIgnoreCase(cofDetail.getReferenceId())) {
							if (MANUAL_COF.toString().equalsIgnoreCase(cofDetail.getSource()))
								quoteLeBean.setIsManualCofSigned(true);
							return true;
						} else
							return false;
					});
				});
			}
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		teamsDRQuoteDataBean.setQuoteToLes(teamsDRMultiQuoteLeBeans);

		// to set customer le details..
		updateCustomerLeDetails(teamsDRQuoteDataBean,customerLeIds);
		return teamsDRQuoteDataBean;
	}

	/**
	 * Method to fetch and store customer le details..
	 * @param teamsDRQuoteDataBean
	 * @param customerLeIds
	 */
	private void updateCustomerLeDetails(TeamsDRQuoteDataBean teamsDRQuoteDataBean, Set<Integer> customerLeIds) {

		Map<Integer, CustomerLeBean> leDetailsBeanMap = new HashMap<>();

		// to set legal entity details.
		if (Objects.nonNull(customerLeIds) && !customerLeIds.isEmpty()) {
			try {
				String request = customerLeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
				String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeDetailsQueue, request);
				CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = Utils
						.convertJsonToObject(customerLeAttributes, CustomerLegalEntityDetailsBean.class);

				customerLegalEntityDetailsBean.getCustomerLeDetails().forEach(customerLeBean -> {
					if (!leDetailsBeanMap.containsKey(customerLeBean.getLegalEntityId())) {
						leDetailsBeanMap.put(customerLeBean.getLegalEntityId(), customerLeBean);
					}
				});

				teamsDRQuoteDataBean.getQuoteToLes().forEach(teamsDRMultiQuoteLeBean -> {
					if (Objects.nonNull(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId())) {
						if (leDetailsBeanMap.containsKey(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId())) {
							teamsDRMultiQuoteLeBean.setCustomerlegalEntityName(leDetailsBeanMap
									.get(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId()).getLegalEntityName());
						}
					}
				});
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_EMPTY);
			}
		}
	}

	/**
	 * Get existing product solution
	 *
	 * @param teamsDRQuoteDataBean
	 * @param isFilterNeeded
	 * @param productFamily
	 * @return
	 * @throws TclCommonException
	 */
	private TeamsDRQuoteDataBean getProductSolutions(TeamsDRQuoteDataBean teamsDRQuoteDataBean, Boolean isFilterNeeded,
			String productFamily) throws TclCommonException {
		LOGGER.info("Inside getProductSolution()");
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(MICROSOFT_CLOUD_SOLUTIONS,
				CommonConstants.BACTIVE);
		teamsDRQuoteDataBean.getQuoteToLes().forEach(teamsDRMultiQuoteLeBean -> {
			updateMultiQuoteLeBean(teamsDRQuoteDataBean,teamsDRMultiQuoteLeBean,mstProductFamily,isFilterNeeded,productFamily);
		});
		// stages maintained at quote-level also, because stage is the same for all the
		// quote_to_le in l2o journey
		Optional<TeamsDRMultiQuoteLeBean> quoteLeBean = teamsDRQuoteDataBean.getQuoteToLes().stream()
				.filter(Objects::nonNull).findAny();
		quoteLeBean.ifPresent(quoteLe -> {
			teamsDRQuoteDataBean.setStage(quoteLe.getStage());
			teamsDRQuoteDataBean.setSubStage(quoteLe.getSubStage());
		});
		return teamsDRQuoteDataBean;
	}

	/**
	 * Method to update multiquotelebean
	 * @param teamsDRQuoteDataBean
	 * @param teamsDRMultiQuoteLeBean
	 * @param mstProductFamily
	 * @param isFilterNeeded
	 * @param productFamily
	 */
	private void updateMultiQuoteLeBean(TeamsDRQuoteDataBean teamsDRQuoteDataBean,TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean,MstProductFamily mstProductFamily,
			Boolean isFilterNeeded , String productFamily){
		teamsDRMultiQuoteLeBean.setVoiceSolutions(new ArrayList<>());
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeRepository
				.findById(teamsDRMultiQuoteLeBean.getQuoteleId()).map(quoteToLe -> {
					return quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId());
				}).orElse(null);
		if (Objects.nonNull(quoteToLeProductFamilies)) {
			teamsDRQuoteDataBean.setProductFamilyName(mstProductFamily.getName());

			quoteToLeProductFamilies.forEach(quoteToLeProductFamily -> {
				List<ProductSolution> productSolutions = productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily);
				if (Objects.isNull(isFilterNeeded) || !isFilterNeeded) {
					if (MICROSOFT_CLOUD_SOLUTIONS.equals(quoteToLeProductFamily.getMstProductFamily().getName())) {
						teamsDRMultiQuoteLeBean.setTeamsDRSolution(new TeamsDRSolutionBean());
						teamsDRMultiQuoteLeBean.getTeamsDRSolution()
								.setTeamsDRServices(
										productSolutions.stream()
												.map(productSolution -> createProductSolutionBean(
														teamsDRQuoteDataBean, productSolution))
												.collect(Collectors.toList()));
					} else if (GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase()
							.equals(quoteToLeProductFamily.getMstProductFamily().getName())) {
						teamsDRMultiQuoteLeBean.setVoiceSolutions(
								gscMultiLEQuoteService.createMultiLESolutionBean(productSolutions));
					}
				} else if (isFilterNeeded) {
					if (CommonConstants.TEAMSDR.equals(productFamily) && MICROSOFT_CLOUD_SOLUTIONS
							.equals(quoteToLeProductFamily.getMstProductFamily().getName())) {
						teamsDRMultiQuoteLeBean.setTeamsDRSolution(new TeamsDRSolutionBean());
						teamsDRMultiQuoteLeBean.getTeamsDRSolution()
								.setTeamsDRServices(
										productSolutions.stream()
												.map(productSolution -> createProductSolutionBean(
														teamsDRQuoteDataBean, productSolution))
												.collect(Collectors.toList()));
					} else if (GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase().equals(productFamily)
							&& GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase()
							.equals(quoteToLeProductFamily.getMstProductFamily().getName())) {
						teamsDRMultiQuoteLeBean.setVoiceSolutions(
								gscMultiLEQuoteService.createMultiLESolutionBean(productSolutions));
					}
				}
			});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.PRODUCT_EMPTY);
		}
	}

	/**
	 * Method to add configuration after selection of countries
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param countries
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public List<TeamsDRConfigurationBean> addOrUpdateTeamsDRConfiguration(Integer quoteId,Integer quoteToLeId,
																		  Integer solutionId,
																		  List<TeamsDRConfigurationBean> countries) throws TclCommonException {
		LOGGER.info("Inside add/update configurations");
		getQuote(quoteId);
		getQuoteToLeById(quoteToLeId);
		ProductSolution productSolution = populateSolution(solutionId);
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findByProductSolutionAndStatus(productSolution, (byte) 1)
				.stream().filter(teamsDR -> teamsDR.getServiceName() == null).findAny().get();
		List<TeamsDRConfigurationBean> teamsDRConfigurationBeans = saveQuoteTeamsDRDetails(countries, quoteTeamsDR);
		return teamsDRConfigurationBeans;
	}

	/**
	 * Method to get quote detail based on id
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private Quote getQuote(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, STATUS_ACTIVE);
		if (Objects.isNull(quote)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quote;
	}

	/**
	 * Method to populate quotetole based on id
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private QuoteToLe populateQuoteToLe(Integer quoteId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe;
		try {
			quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId).stream().findFirst();
			if (!quoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quoteToLe.get();
	}

	/**
	 * Method to populate ProductSolution based on id
	 *
	 * @param solutionId
	 * @return
	 * @throws TclCommonException
	 */
	private ProductSolution populateSolution(Integer solutionId) throws TclCommonException {
		ProductSolution productSolution;
		try {
			productSolution = getProductSolution(solutionId);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return productSolution;
	}

	/**
	 * Method to get product solution based on id
	 *
	 * @param solutionId
	 * @return
	 * @throws TclCommonException
	 */
	public ProductSolution getProductSolution(Integer solutionId) throws TclCommonException {
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Optional<ProductSolution> productSolution = productSolutionRepository.findById(solutionId);
		if (Objects.isNull(productSolution)) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return productSolution.get();
	}

	/**
	 * Method to save all the attributes of quoteteamsdrdetails.
	 * @param configurations
	 * @param quoteTeamsDR
	 * @return
	 */
	public List<TeamsDRConfigurationBean> saveQuoteTeamsDRDetails(List<TeamsDRConfigurationBean> configurations,
			QuoteTeamsDR quoteTeamsDR) throws TclCommonException {

		MstProductFamily mstProductFamily = getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);

		List<QuoteTeamsDRDetails> savedConfigurations = quoteTeamsDRDetailsRepository.
				findByQuoteTeamsDR(quoteTeamsDR);

		// Getting id of saved configurations.
		Set<Integer> savedConfigIds = savedConfigurations.stream().map(QuoteTeamsDRDetails::getId)
				.collect(Collectors.toSet());

		// Getting id of request configurations.
		Set<Integer> requestConfigIds = new HashSet<>();

		requestConfigIds.addAll(configurations.stream().filter(config->Objects.nonNull(config.getId()))
				.map(config->config.getId()).collect(Collectors.toList()));

		Set<Integer> toDelete = Sets.difference(savedConfigIds, requestConfigIds);

		toDelete.forEach(configId -> {
			LOGGER.info("Deleting config id :: {}",configId);
			Optional<QuoteTeamsDRDetails> quoteTeamsDRDetailsOptional = quoteTeamsDRDetailsRepository.findById(configId);
			quoteTeamsDRDetailsOptional.ifPresent(teamsdrDetail -> {
				// Deleting all the attributes
				List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.
						findByReferenceIdAndMstProductFamily(teamsdrDetail.getId(),mstProductFamily);
				if (!quoteProductComponentList.isEmpty()) {
					quoteProductComponentList.forEach(quoteProductComponent -> {
						quoteProductComponent.getQuoteProductComponentsAttributeValues()
								.forEach(attr -> {
									// Deleting quote direct routing is media gateway is present.
									if(IS_MEDIAGATEWAY.equals(attr.getProductAttributeMaster().getName())){
										deleteQuoteDirectRoutings(quoteTeamsDR.getQuoteToLe().getId(),teamsdrDetail.getCountry());
									}
									quoteProductComponentsAttributeValueRepository.delete(attr);
								});
						quoteProductComponentRepository.delete(quoteProductComponent);
					});
				}
				quoteTeamsDRDetailsRepository.delete(teamsdrDetail);
			});

		});

		configurations.forEach(configuration -> {
			QuoteTeamsDRDetails quoteTeamsDRDetail;
			if (Objects.isNull(configuration.getId())) {
				quoteTeamsDRDetail = new QuoteTeamsDRDetails();
				quoteTeamsDRDetail.setCountry(configuration.getCountry());
				quoteTeamsDRDetail.setQuoteTeamsDR(quoteTeamsDR);
				quoteTeamsDRDetail.setCreatedBy(Utils.getSource());
				quoteTeamsDRDetail.setCreatedTime(new Date());
			} else {
				quoteTeamsDRDetail = quoteTeamsDRDetailsRepository.findById(configuration.getId()).get();
			}
			QuoteTeamsDRDetails finalQuoteTeamsDrDetail = quoteTeamsDRDetailsRepository.save(quoteTeamsDRDetail);

			// Creating/deleting quote direct routing based on the isMediaGateway Attribute
			createOrDeleteQuoteDirectRouting(configuration,quoteTeamsDR,finalQuoteTeamsDrDetail);
		});


		return quoteTeamsDRDetailsRepository.findByQuoteTeamsDR(quoteTeamsDR).stream().
				map(quoteTeamsDRDetails1 -> {
					try {
						return toQuoteTeamsdrDetailsBean(quoteTeamsDRDetails1,null);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
					}
				}).collect(Collectors.toList());

	}

	/**
	 * Creating/deleting quote direct routing based on the isMediaGateway Attribute
	 * @param configuration
	 * @param quoteTeamsDR
	 * @param quoteTeamsDRDetails
	 */
	private void createOrDeleteQuoteDirectRouting(TeamsDRConfigurationBean configuration,
			QuoteTeamsDR quoteTeamsDR,QuoteTeamsDRDetails quoteTeamsDRDetails){
		if(Objects.nonNull(configuration.getComponents())){
			configuration.getComponents().forEach(componentBean -> {
				try {
					componentBean.getAttributes().stream().filter(attributeValueBean->
							IS_MEDIAGATEWAY.equals(attributeValueBean.getName())).findAny().ifPresent(attributeValueBean->{
						Optional<QuoteTeamsDR> quoteTeamsDRForMg = quoteTeamsDRRepository.
								findByQuoteToLeIdAndServiceName(quoteTeamsDR.getQuoteToLe().getId(),MEDIA_GATEWAY);
						LOGGER.info("QuoteTeamsDR for mediagateway is :: {}",quoteTeamsDRForMg.get().getId());
						if(CommonConstants.YES.equalsIgnoreCase(attributeValueBean.getAttributeValues())){
							if(quoteTeamsDRForMg.isPresent()){
								Optional<QuoteDirectRouting> quoteDirectRouting = quoteDirectRoutingRepository.
										findByQuoteTeamsDRAndCountry(quoteTeamsDRForMg.get(),configuration.getCountry());
								if(!quoteDirectRouting.isPresent()){
									// Create an entryy...
									LOGGER.info("Creating a new Quote Direct Routing Entry :: {}",configuration.getCountry());
									QuoteDirectRouting newQuoteDirectRouting = new QuoteDirectRouting();
									newQuoteDirectRouting.setCountry(configuration.getCountry());
									newQuoteDirectRouting.setQuoteTeamsDR(quoteTeamsDRForMg.get());
									newQuoteDirectRouting.setCreatedTime(new Date());
									newQuoteDirectRouting.setQuoteVersion(1);
									quoteDirectRoutingRepository.save(newQuoteDirectRouting);
								}
							}
						}else{
							// For null or No or NA
							Optional<QuoteDirectRouting> quoteDirectRouting = quoteDirectRoutingRepository.
									findByQuoteTeamsDRAndCountry(quoteTeamsDRForMg.get(),configuration.getCountry());
							if(quoteDirectRouting.isPresent()){
								// Delete entry..
								List<QuoteDirectRoutingCity> quoteDirectRoutingCities = quoteDirectRoutingCityRepository
										.findByQuoteDirectRoutingId(quoteDirectRouting.get().getId());
								quoteDirectRoutingCities.forEach(quoteDirectRoutingCity -> {
									List<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateways = quoteDirectRoutingMgRepository.
											findByQuoteDirectRoutingCityId(quoteDirectRoutingCity.getId());
									quoteDirectRoutingMgRepository.deleteAll(quoteDirectRoutingMediaGateways);
								});
								quoteDirectRoutingCityRepository.deleteAll(quoteDirectRoutingCities);
								quoteDirectRoutingRepository.delete(quoteDirectRouting.get());
							}
						}
					});

					if(SERVICE_ATTRIBUTES.equals(componentBean.getName())){
						processProductComponent(quoteTeamsDRDetails.getId(),componentBean,TEAMSDR_SERVICE_ATTRIBUTES);
					}

				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
		}
	}

	/**
	 * Method to delete quote direct routings.
	 * @param quoteToLeId
	 */
	private void deleteQuoteDirectRoutings(Integer quoteToLeId,String country){
		Optional<QuoteTeamsDR> quoteTeamsDRForMg = quoteTeamsDRRepository.findByQuoteToLeIdAndServiceName(quoteToLeId,MEDIA_GATEWAY);

		if(quoteTeamsDRForMg.isPresent()){
			Optional<QuoteDirectRouting> quoteDirectRouting = quoteDirectRoutingRepository.
					findByQuoteTeamsDRAndCountry(quoteTeamsDRForMg.get(),country);
			if(quoteDirectRouting.isPresent()){
				List<QuoteDirectRoutingCity> quoteDirectRoutingCities = quoteDirectRoutingCityRepository
						.findByQuoteDirectRoutingId(quoteDirectRouting.get().getId());
				quoteDirectRoutingCities.forEach(quoteDirectRoutingCity -> {
					List<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateways = quoteDirectRoutingMgRepository.
							findByQuoteDirectRoutingCityId(quoteDirectRoutingCity.getId());
					quoteDirectRoutingMgRepository.deleteAll(quoteDirectRoutingMediaGateways);
				});
				quoteDirectRoutingCityRepository.deleteAll(quoteDirectRoutingCities);
				quoteDirectRoutingRepository.delete(quoteDirectRouting.get());
			}
		}
	}

	/**
	 * Method to convert QuoteDirectRouting to DirectRoutingConfigurationBean
	 *
	 * @param quoteDirectRouting
	 * @return
	 */

	public MediaGatewayConfigurationBean toQuoteTeamsdrConfigurationBean(QuoteDirectRouting quoteDirectRouting,
			MediaGatewayConfigurationBean mediaGatewayConfigurationBean) {
		if (Objects.isNull(mediaGatewayConfigurationBean)) {
			mediaGatewayConfigurationBean = new MediaGatewayConfigurationBean();
		}
		mediaGatewayConfigurationBean.setId(quoteDirectRouting.getId());
		mediaGatewayConfigurationBean.setCountry(quoteDirectRouting.getCountry());
		mediaGatewayConfigurationBean.setMrc(quoteDirectRouting.getMrc());
		mediaGatewayConfigurationBean.setNrc(quoteDirectRouting.getNrc());
		mediaGatewayConfigurationBean.setArc(quoteDirectRouting.getArc());
		mediaGatewayConfigurationBean.setTcv(quoteDirectRouting.getTcv());

		return mediaGatewayConfigurationBean;
	}

	/**
	 * Method to convert QuoteDirectRouting to TeamsDRConfigurationBean
	 *
	 * @param quoteTeamsDRDetails
	 * @return
	 */

	public TeamsDRConfigurationBean toQuoteTeamsdrDetailsBean(QuoteTeamsDRDetails quoteTeamsDRDetails,
			TeamsDRConfigurationBean teamsDRConfigurationBean)
			throws TclCommonException {
		if (Objects.isNull(teamsDRConfigurationBean)) {
			teamsDRConfigurationBean = new TeamsDRConfigurationBean();
		}
		teamsDRConfigurationBean.setId(quoteTeamsDRDetails.getId());
		teamsDRConfigurationBean.setCountry(quoteTeamsDRDetails.getCountry());
		teamsDRConfigurationBean.setNoOfNamedUsers(quoteTeamsDRDetails.getNoOfNamedusers());
		teamsDRConfigurationBean.setNoOfCommonAreaDevices(quoteTeamsDRDetails.getNoOfCommonAreaDevices());
		teamsDRConfigurationBean.setTotalUsers(quoteTeamsDRDetails.getTotalUsers());
		teamsDRConfigurationBean.setMrc(Objects.nonNull(quoteTeamsDRDetails.getMrc())?
				quoteTeamsDRDetails.getMrc():BigDecimal.ZERO);
		teamsDRConfigurationBean.setNrc(Objects.nonNull(quoteTeamsDRDetails.getNrc())?
				quoteTeamsDRDetails.getNrc():BigDecimal.ZERO);
		teamsDRConfigurationBean.setArc(Objects.nonNull(quoteTeamsDRDetails.getArc())?
				quoteTeamsDRDetails.getArc():BigDecimal.ZERO);
		teamsDRConfigurationBean.setTcv(Objects.nonNull(quoteTeamsDRDetails.getTcv())?
				quoteTeamsDRDetails.getTcv():BigDecimal.ZERO);
		teamsDRConfigurationBean.setComponents(getComponentDetail(quoteTeamsDRDetails.getId(),TEAMSDR_SERVICE_ATTRIBUTES));
		return teamsDRConfigurationBean;
	}

	/**
	 * Method to get all the configurations based on product solution
	 *
	 * @param quoteId
	 * @param solutionId
	 * @return
	 * @throws TclCommonException
	 */
	public List<TeamsDRConfigurationBean> getConfigurations(Integer quoteId, Integer solutionId)
			throws TclCommonException {
		LOGGER.info("Inside Get Configurations");
		getQuote(quoteId);
		QuoteToLe quoteToLe = populateQuoteToLe(quoteId);
		ProductSolution productSolution = populateSolution(solutionId);
		QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId()).stream()
				.filter(quoteTeamsDR1 -> Objects.isNull(quoteTeamsDR1.getServiceName())).findAny().get();
		List<QuoteTeamsDRDetails> quoteTeamsDRDetails = quoteTeamsDRDetailsRepository.findByQuoteTeamsDR(quoteTeamsDR);
		return quoteTeamsDRDetails.stream()
				.map(quoteTeamsDRDetail -> {
					try {
						return toQuoteTeamsdrDetailsBean(quoteTeamsDRDetail, null);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
					}
				})
				.collect(Collectors.toList());
	}

	/**
	 * Method to get config attributes attributes.
	 * @param teamsDRQuoteDataBean
	 * @return
	 */
	private List<TeamsDRAttributesBean> getConfigAttributes(Integer quoteToLeId) {

		List<TeamsDRAttributesBean> teamsDRAttributesBeans = new ArrayList<>();
		quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteToLeId).stream()
				.filter(quoteLeAttributeValue -> CONFIGURATION_ATTRIBUTES
						.contains(quoteLeAttributeValue.getMstOmsAttribute().getName()))
				.forEach(quoteLeAttributeValue -> teamsDRAttributesBeans
						.add(TeamsDRAttributesBean.toTeamsDRAttributesBean(quoteLeAttributeValue)));
		return teamsDRAttributesBeans;
	}



	/**
	 * Method to update TeamsDR after pricing call
	 *
	 * @param offerings
	 * @param quoteTeamsDRS
	 */
	public void updateQuoteTeamsDRAfterPricing(QuoteToLe quoteToLe, List<TeamsDRServicesResponseBean> offerings,
			List<QuoteTeamsDR> quoteTeamsDRS) {
		for (TeamsDRServicesResponseBean offering : offerings) {
			for (QuoteTeamsDR quoteTeamsDR : quoteTeamsDRS) {
				if (Objects.nonNull(offering.getComponentName())
						&& !TeamsDRConstants.ADDON_SERVICES.equals(offering.getComponentName())) {
					if (Objects.nonNull(quoteTeamsDR.getServiceName())
							&& quoteTeamsDR.getServiceName().equals(offering.getComponentName())) {
						quoteTeamsDR.setMrc(offering.getMrc().doubleValue());
						quoteTeamsDR.setNrc(offering.getNrc().doubleValue());
						quoteTeamsDR.setArc(offering.getArc().doubleValue());
						quoteTeamsDR.setTcv(offering.getTcv().doubleValue());
						break;
					}
				} else if (!TeamsDRConstants.ADDON_SERVICES.equals(offering.getComponentName())) {
					if (Objects.isNull(quoteTeamsDR.getServiceName())) {
						quoteTeamsDR.setMrc(offering.getMrc().doubleValue());
						quoteTeamsDR.setNrc(offering.getNrc().doubleValue());
						quoteTeamsDR.setArc(offering.getArc().doubleValue());
						quoteTeamsDR.setTcv(offering.getTcv().doubleValue());
						break;
					}
				}
			}
		}
		quoteTeamsDRRepository.saveAll(quoteTeamsDRS);
		addTeamsDRPrices(quoteToLe, quoteTeamsDRS);
	}

	/**
	 * Updating pricing response in quote direct routing, quote direct routing city
	 * and quote direct routing media gateway
	 *
	 * @param response
	 * @param quoteToLe
	 */
	public MediaGatewayConfigurationBean updateQuoteDirectRouting(TeamsDRConfigurationPricingBean response,
			QuoteToLe quoteToLe, List<QuoteTeamsDR> quoteTeamsDRS) {
		LOGGER.info("Inside Update QuoteDirectRouting");
		LOGGER.info("Pricing response:" + response);
		Optional<QuoteDirectRouting> quoteDirectRoutingOptional = quoteDirectRoutingRepository
				.findById(response.getId());
		QuoteDirectRouting quoteDirectRouting = null;
		LOGGER.info("Updating quote direct routing");
		if (quoteDirectRoutingOptional.isPresent()) {
			quoteDirectRouting = quoteDirectRoutingOptional.get();
			quoteDirectRouting.setMrc(response.getMrc());
			quoteDirectRouting.setNrc(response.getNrc());
			quoteDirectRouting.setArc(response.getArc());
			quoteDirectRouting.setTcv(response.getTcv());
		}

		if (Objects.nonNull(response.getCities())) {
			response.getCities().forEach(teamsDRCityPricingBean -> {
				LOGGER.info("Updating Quote Direct Routing City Prices");
				Optional<QuoteDirectRoutingCity> quoteDirectRoutingCityOptional = quoteDirectRoutingCityRepository
						.findById(teamsDRCityPricingBean.getId());
				if (quoteDirectRoutingCityOptional.isPresent()) {
					QuoteDirectRoutingCity quoteDirectRoutingCity = quoteDirectRoutingCityOptional.get();
					quoteDirectRoutingCity.setMrc(teamsDRCityPricingBean.getMrc());
					quoteDirectRoutingCity.setNrc(teamsDRCityPricingBean.getNrc());
					quoteDirectRoutingCity.setArc(teamsDRCityPricingBean.getArc());
					quoteDirectRoutingCity.setTcv(teamsDRCityPricingBean.getTcv());
					quoteDirectRoutingCityRepository.save(quoteDirectRoutingCity);
				}
				if (Objects.nonNull(teamsDRCityPricingBean.getMediaGateway())) {
					teamsDRCityPricingBean.getMediaGateway().forEach(teamDRMediaGatewayPricingBean -> {
						LOGGER.info("Updating Media gateway prices");
						Optional<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGatewaysOptional = quoteDirectRoutingMgRepository
								.findById(teamDRMediaGatewayPricingBean.getId());
						if (quoteDirectRoutingMediaGatewaysOptional.isPresent()) {
							QuoteDirectRoutingMediaGateways quoteDirectRoutingMediaGateways = quoteDirectRoutingMediaGatewaysOptional
									.get();
							quoteDirectRoutingMediaGateways.setMrc(teamDRMediaGatewayPricingBean.getMrc());
							quoteDirectRoutingMediaGateways.setNrc(teamDRMediaGatewayPricingBean.getNrc());
							quoteDirectRoutingMediaGateways.setArc(teamDRMediaGatewayPricingBean.getArc());
							quoteDirectRoutingMediaGateways.setTcv(teamDRMediaGatewayPricingBean.getTcv());
							quoteDirectRoutingMgRepository.save(quoteDirectRoutingMediaGateways);
						}
					});
				}
			});
		}
		addQuoteToLe(quoteToLe, quoteDirectRouting);
		return toQuoteTeamsdrConfigurationBean(quoteDirectRouting, null);
	}



	/**
	 * Subtracting quote to le with old configuration prices
	 *
	 * @param quoteToLe
	 * @param quoteDirectRouting
	 * @return
	 */
	public QuoteToLe subtractPrices(QuoteToLe quoteToLe, QuoteDirectRouting quoteDirectRouting,
			List<QuoteTeamsDR> quoteTeamsDRs) {
		subtractQuoteDirectRoutingPrices(quoteToLe, quoteDirectRouting);
		subtractTeamsDRPrices(quoteToLe, quoteTeamsDRs);
		quoteToLeRepository.save(quoteToLe);
		return quoteToLe;
	}

	/**
	 * Method to subtract quote direct routing prices before pricing
	 *
	 * @param quoteToLe
	 * @param quoteDirectRouting
	 * @return
	 */
	private QuoteToLe subtractQuoteDirectRoutingPrices(QuoteToLe quoteToLe, QuoteDirectRouting quoteDirectRouting) {
		if (Objects.nonNull(quoteToLe.getFinalMrc()) && Objects.nonNull(quoteDirectRouting.getMrc())
				&& quoteToLe.getFinalMrc() > 0) {
			quoteToLe.setFinalMrc(quoteToLe.getFinalMrc() - quoteDirectRouting.getMrc().doubleValue());
		}
		if (Objects.nonNull(quoteToLe.getFinalNrc()) && Objects.nonNull(quoteDirectRouting.getNrc())
				&& quoteToLe.getFinalNrc() > 0) {
			quoteToLe.setFinalNrc(quoteToLe.getFinalNrc() - quoteDirectRouting.getNrc().doubleValue());
		}

		quoteToLe.setProposedMrc(checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setProposedNrc(checkForNull(quoteToLe.getFinalNrc()));

		quoteToLe.setProposedArc(12 * checkForNull(quoteToLe.getProposedMrc()));
		quoteToLe.setFinalArc(12 * checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setTotalTcv(quoteToLe.getFinalMrc() + quoteToLe.getFinalNrc());
		return quoteToLe;
	}

	/**
	 * Method to subtract teamsdrprices before pricing
	 *
	 * @param quoteToLe
	 * @param quoteTeamsDRS
	 * @return
	 */
	private QuoteToLe subtractTeamsDRPrices(QuoteToLe quoteToLe, List<QuoteTeamsDR> quoteTeamsDRS) {
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		quoteTeamsDRS.stream().filter(quoteTeamsDR -> Objects.isNull(quoteTeamsDR.getServiceName()))
				.forEach(quoteTeamsDRFinal -> {
					teamsDRCumulativePricesBean
							.setMrc(teamsDRCumulativePricesBean.getMrc() + checkForNull(quoteTeamsDRFinal.getMrc()));
					teamsDRCumulativePricesBean
							.setNrc(teamsDRCumulativePricesBean.getNrc() + checkForNull(quoteTeamsDRFinal.getNrc()));
					teamsDRCumulativePricesBean
							.setArc(teamsDRCumulativePricesBean.getArc() + checkForNull(quoteTeamsDRFinal.getArc()));
					teamsDRCumulativePricesBean
							.setTcv(teamsDRCumulativePricesBean.getTcv() + checkForNull(quoteTeamsDRFinal.getTcv()));
				});

		if (Objects.nonNull(quoteToLe.getFinalMrc()) && Objects.nonNull(teamsDRCumulativePricesBean.getMrc())
				&& quoteToLe.getFinalMrc() > 0) {
			quoteToLe.setFinalMrc(quoteToLe.getFinalMrc() - teamsDRCumulativePricesBean.getMrc());
		}

		if (Objects.nonNull(quoteToLe.getFinalNrc()) && Objects.nonNull(teamsDRCumulativePricesBean.getNrc())
				&& quoteToLe.getFinalMrc() > 0) {
			quoteToLe.setFinalMrc(quoteToLe.getFinalMrc() - teamsDRCumulativePricesBean.getNrc());
		}

		quoteToLe.setProposedMrc(checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setProposedNrc(checkForNull(quoteToLe.getFinalNrc()));

		quoteToLe.setProposedArc(12 * checkForNull(quoteToLe.getProposedMrc()));
		quoteToLe.setFinalArc(12 * checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setTotalTcv(quoteToLe.getFinalMrc() + quoteToLe.getFinalNrc());

		return quoteToLe;

	}

	/**
	 * Method to add teamsdrprices after pricing
	 *
	 * @param quoteToLe
	 * @param quoteTeamsDRS
	 * @return
	 */
	private QuoteToLe addTeamsDRPrices(QuoteToLe quoteToLe, List<QuoteTeamsDR> quoteTeamsDRS) {
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		quoteTeamsDRS.stream()
				.filter(quoteTeamsDR -> Objects.isNull(quoteTeamsDR.getServiceName())
						|| TeamsDRConstants.ADDON_SERVICES.equals(quoteTeamsDR.getServiceName()))
				.forEach(quoteTeamsDRFinal -> {
					teamsDRCumulativePricesBean
							.setMrc(teamsDRCumulativePricesBean.getMrc() + checkForNull(quoteTeamsDRFinal.getMrc()));
					teamsDRCumulativePricesBean
							.setNrc(teamsDRCumulativePricesBean.getNrc() + checkForNull(quoteTeamsDRFinal.getNrc()));
					teamsDRCumulativePricesBean
							.setArc(teamsDRCumulativePricesBean.getArc() + checkForNull(quoteTeamsDRFinal.getArc()));
					teamsDRCumulativePricesBean
							.setTcv(teamsDRCumulativePricesBean.getTcv() + checkForNull(quoteTeamsDRFinal.getTcv()));
				});

		if (Objects.nonNull(quoteToLe.getFinalMrc()) && Objects.nonNull(teamsDRCumulativePricesBean.getMrc())) {
			quoteToLe.setFinalMrc(quoteToLe.getFinalMrc() + teamsDRCumulativePricesBean.getMrc());
		}

		if (Objects.nonNull(quoteToLe.getFinalNrc()) && Objects.nonNull(teamsDRCumulativePricesBean.getNrc())) {
			quoteToLe.setFinalMrc(quoteToLe.getFinalMrc() + teamsDRCumulativePricesBean.getNrc());
		}

		quoteToLe.setProposedMrc(checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setProposedNrc(checkForNull(quoteToLe.getFinalNrc()));

		quoteToLe.setProposedArc(12 * checkForNull(quoteToLe.getProposedMrc()));
		quoteToLe.setFinalArc(12 * checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setTotalTcv(quoteToLe.getFinalMrc() + quoteToLe.getFinalNrc());
		quoteToLeRepository.save(quoteToLe);
		return quoteToLe;

	}

	/**
	 * For adding quote to le with new configuration prices
	 *
	 * @param quoteToLe
	 * @param quoteDirectRouting
	 * @return
	 */
	public QuoteToLe addQuoteToLe(QuoteToLe quoteToLe, QuoteDirectRouting quoteDirectRouting) {
		if (Objects.nonNull(quoteToLe.getFinalMrc()) && Objects.nonNull(quoteDirectRouting.getMrc())) {
			quoteToLe.setFinalMrc(quoteToLe.getFinalMrc() + quoteDirectRouting.getMrc().doubleValue());
		}
		if (Objects.nonNull(quoteToLe.getFinalNrc()) && Objects.nonNull(quoteDirectRouting.getNrc())) {
			quoteToLe.setFinalNrc(quoteToLe.getFinalNrc() + quoteDirectRouting.getNrc().doubleValue());
		}
		quoteToLe.setProposedMrc(checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setProposedNrc(checkForNull(quoteToLe.getFinalNrc()));

		quoteToLe.setProposedArc(12 * checkForNull(quoteToLe.getProposedMrc()));
		quoteToLe.setFinalArc(12 * checkForNull(quoteToLe.getFinalMrc()));
		quoteToLe.setTotalTcv(quoteToLe.getFinalMrc() + quoteToLe.getFinalNrc());
		quoteToLeRepository.save(quoteToLe);
		return quoteToLe;
	}

	/**
	 * Method to get quote direct routing based on id
	 *
	 * @param configId
	 * @return
	 * @throws TclCommonException
	 */
	private QuoteDirectRouting populateQuoteDirectRouting(Integer configId) throws TclCommonException {
		Objects.requireNonNull(configId);
		Optional<QuoteDirectRouting> quoteTeamsdrDetails = quoteDirectRoutingRepository.findById(configId);
		if (!quoteTeamsdrDetails.isPresent()) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quoteTeamsdrDetails.get();
	}

	/**
	 * Method to save City Details
	 *
	 * @param mediaGatewayConfigurationBean
	 * @param quoteToLe
	 * @param quoteDirectRouting
	 * @return
	 */
	public List<QuoteDirectRoutingCity> saveCityDetails(MediaGatewayConfigurationBean mediaGatewayConfigurationBean,
														QuoteToLe quoteToLe, QuoteDirectRouting quoteDirectRouting) {
		LOGGER.info("Inside save city details:");
		List<QuoteDirectRoutingCity> quoteDirectRoutingCityList;
		Set<Integer> savedCityIds = quoteDirectRoutingCityRepository
				.findByQuoteDirectRoutingId(quoteDirectRouting.getId()).stream().map(QuoteDirectRoutingCity::getId)
				.collect(Collectors.toSet());
		Set<Integer> requestCityIds = new HashSet<>();
		mediaGatewayConfigurationBean.getCities().forEach(teamsDRCityBean -> {
			QuoteDirectRoutingCity quoteDirectRoutingCity;
			if (Objects.nonNull(teamsDRCityBean.getId())) {
				quoteDirectRoutingCity = quoteDirectRoutingCityRepository.findById(teamsDRCityBean.getId()).get();
				quoteDirectRoutingMgRepository.deleteAll(
						quoteDirectRoutingMgRepository.findByQuoteDirectRoutingCityId(quoteDirectRoutingCity.getId()));
				requestCityIds.add(teamsDRCityBean.getId());
			} else {
				quoteDirectRoutingCity = new QuoteDirectRoutingCity();
			}
			quoteDirectRoutingCity.setQuoteDirectRouting(quoteDirectRouting);
			quoteDirectRoutingCity.setMrc(teamsDRCityBean.getMrc());
			quoteDirectRoutingCity.setName(teamsDRCityBean.getCity());
			quoteDirectRoutingCity.setNrc(teamsDRCityBean.getNrc());
			quoteDirectRoutingCity.setArc(teamsDRCityBean.getArc());
			if (!teamsDRCityBean.getMediaGatewayType().equals(TeamsDRConstants.NONE)) {
				if (Objects.nonNull(teamsDRCityBean.getMediaGateway())
						&& !teamsDRCityBean.getMediaGateway().isEmpty()) {
					quoteDirectRoutingCity.setQuoteDirectRoutingMediaGateways(teamsDRCityBean.getMediaGateway().stream()
							.map(teamsDRMediaGatewayBean -> TeamsDRMediaGatewayBean
									.toTeamsDrMediaGatewayBean(teamsDRMediaGatewayBean, quoteDirectRoutingCity))
							.collect(Collectors.toList()));
				} else {
					quoteDirectRoutingCity.setQuoteDirectRoutingMediaGateways(null);
				}
			} else {
				quoteDirectRoutingCity.setQuoteDirectRoutingMediaGateways(null);
			}
			quoteDirectRoutingCity.setMediaGatewayType(teamsDRCityBean.getMediaGatewayType());
			quoteDirectRoutingCity.setCreatedTime(new Date());
			quoteDirectRoutingCity.setQuoteVersion(1);
			quoteDirectRoutingCityRepository.save(quoteDirectRoutingCity);
		});

		Set<Integer> toDelete = Sets.difference(savedCityIds, requestCityIds);

		toDelete.forEach(cityId -> {
			Optional<QuoteDirectRoutingCity> directRoutingOptional = quoteDirectRoutingCityRepository.findById(cityId);
			if (directRoutingOptional.isPresent()) {
				quoteDirectRoutingMgRepository.deleteAll(quoteDirectRoutingMgRepository
						.findByQuoteDirectRoutingCityId(directRoutingOptional.get().getId()));
				quoteDirectRoutingCityRepository.delete(directRoutingOptional.get());
			}
		});
		quoteDirectRoutingCityList = quoteDirectRoutingCityRepository
				.findByQuoteDirectRoutingId(quoteDirectRouting.getId());
		return quoteDirectRoutingCityList;
	}



	/**
	 * Method for deleting all the configurations
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public String deleteConfigurations(Integer quoteId,Integer quoteLeId)
			throws TclCommonException {
		getQuote(quoteId);
		QuoteToLe quoteLe = getQuoteToLeById(quoteLeId);
		subtractPlanAndMgPriceFromQuoteLe(quoteLe);
		Optional<QuoteTeamsDR> quoteTeamsDROptional = quoteTeamsDRRepository.
				findByQuoteToLeIdAndIsConfig(quoteLe.getId(),CommonConstants.BACTIVE);
		if(quoteTeamsDROptional.isPresent()){
			QuoteTeamsDR quoteTeamsDR = quoteTeamsDROptional.get();
			List<QuoteTeamsDRDetails> quoteTeamsDRDetails = quoteTeamsDRDetailsRepository.findByQuoteTeamsDR(quoteTeamsDR);
			quoteTeamsDRDetailsRepository.deleteAll(quoteTeamsDRDetails);
			TeamsDRMultiQuoteLeBean quoteLeBean = createQuoteToLeBean(quoteLe);
			triggerPricingPlanAndMg(quoteId, quoteLeBean);
			updateQuoteToLeWithPlanAndMg(quoteLe, quoteLeBean);
		}else{
			throw new TclCommonException(ExceptionConstants.QUOTE_TEAMSDR_NOT_FOUND,ResponseResource.R_CODE_NOT_FOUND);
		}
		return CommonConstants.SUCCESS;
	}

	/**
	 * Delete Attributes For License
	 *
	 * @param quoteToLe
	 * @param deleteAll
	 */
	private void deleteAttributesForLicense(QuoteToLe quoteToLe, Boolean deleteAll) {
		LOGGER.info("Inside delete Attributes for license.");
		Predicate<QuoteLeAttributeValue> filter;
		if (deleteAll) {
			filter = quoteLeAttributeValue -> TeamsDRConstants.EXISTING_LICENSE_ATTRIBUTES
					.contains(quoteLeAttributeValue.getMstOmsAttribute().getName());
		} else {
			filter = quoteLeAttributeValue -> TeamsDRConstants.LICENSE_ATTRIBUTES
					.contains(quoteLeAttributeValue.getMstOmsAttribute().getName());
		}

		quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteToLe.getId()).stream().filter(filter)
				.forEach(quoteLeAttributeValue -> quoteLeAttributeValueRepository.delete(quoteLeAttributeValue));
	}


//	/**
//	 * Method to get quote direct routings
//	 *
//	 * @param productSolution
//	 * @param teamsDRServicesBean
//	 * @return
//	 */
//	private TeamsDRServicesBean getQuoteDirectRoutings(ProductSolution productSolution,
//													   TeamsDRServicesBean teamsDRServicesBean) {
//		List<QuoteDirectRouting> quoteDirectRoutings = quoteDirectRoutingRepository
//				.findByProductSolution(productSolution);
//		if (Objects.nonNull(quoteDirectRoutings) && !quoteDirectRoutings.isEmpty()) {
//			TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
//			List<DirectRoutingConfigurationBean> configurationBeans = new ArrayList<>();
//			quoteDirectRoutings.forEach(quoteDirectRouting -> {
//				configurationBeans.add(toQuoteTeamsdrConfigurationBean(quoteDirectRouting, null));
//				teamsDRCumulativePricesBean.setMrc(
//						checkForNull(quoteDirectRouting.getMrc()).doubleValue() + teamsDRCumulativePricesBean.getMrc());
//				teamsDRCumulativePricesBean.setNrc(
//						checkForNull(quoteDirectRouting.getNrc()).doubleValue() + teamsDRCumulativePricesBean.getNrc());
//				teamsDRCumulativePricesBean.setArc(
//						checkForNull(quoteDirectRouting.getArc()).doubleValue() + teamsDRCumulativePricesBean.getArc());
//				teamsDRCumulativePricesBean.setTcv(
//						checkForNull(quoteDirectRouting.getTcv()).doubleValue() + teamsDRCumulativePricesBean.getTcv());
//			});
//
//			teamsDRServicesBean.setMrc(teamsDRCumulativePricesBean.getMrc());
//			teamsDRServicesBean.setNrc(teamsDRCumulativePricesBean.getNrc());
//			teamsDRServicesBean.setArc(teamsDRCumulativePricesBean.getArc());
//			teamsDRServicesBean.setTcv(teamsDRCumulativePricesBean.getTcv());
//			teamsDRServicesBean.setConfigurations(configurationBeans);
//		}
//		return teamsDRServicesBean;
//	}

	/**
	 * Get quote TeamsDR by quoteToLeId
	 *
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	private List<QuoteTeamsDR> getQuoteTeamsDR(Integer quoteLeId) throws TclCommonException {
		List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository.findByQuoteToLeId(quoteLeId);
		if (Objects.isNull(quoteTeamsDRs) || quoteTeamsDRs.isEmpty())
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY);
		return quoteTeamsDRs;
	}

	/**
	 * Get quote TeamsDR by quoteToLeId
	 *
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	private List<QuoteTeamsDR> getQuoteTeamsDRByProductSolution(ProductSolution productSolution) throws TclCommonException {
		List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository.findByProductSolutionAndStatus(productSolution,CommonConstants.BACTIVE);
		if (Objects.isNull(quoteTeamsDRs) || quoteTeamsDRs.isEmpty())
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY);
		return quoteTeamsDRs;
	}

	/**
	 * Method to get licenses based on the countries and agreementType
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param agreementType
	 * @return
	 * @throws TclCommonException
	 */
	public TeamsDRLicenseAgreementType getLicenseByCountries(Integer quoteId, Integer quoteLeId, String agreementType)
			throws TclCommonException {
		Objects.requireNonNull(quoteId);
		Objects.requireNonNull(quoteLeId);
		QuoteToLe quoteToLe = getQuoteToLeById(quoteLeId);
		TeamsDRLicenseRequestBean teamsDRLicenseRequestBean = new TeamsDRLicenseRequestBean();
		QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.
				findByQuoteToLeIdAndIsConfig(quoteToLe.getId(),CommonConstants.BACTIVE).get();

		Set<String> countries = quoteTeamsDRDetailsRepository.findByQuoteTeamsDR(quoteTeamsDR).stream()
				.map(QuoteTeamsDRDetails::getCountry).collect(Collectors.toSet());
		teamsDRLicenseRequestBean.setCountries(countries);
		teamsDRLicenseRequestBean.setAgreementType(agreementType);

		String response = (String) mqUtils.sendAndReceive(licenseBasedOnCountries,
				Utils.convertObjectToJson(teamsDRLicenseRequestBean));
		return Utils.convertJsonToObject(response, TeamsDRLicenseAgreementType.class);
	}

	/**
	 * Save quote to le attributes
	 *
	 * @param quoteLeId
	 * @param teamsDRAttributesBean
	 * @return
	 */
	public TeamsDRAttributesBean saveAttribute(Integer quoteLeId, TeamsDRAttributesBean teamsDRAttributesBean) {
		List<MstOmsAttribute> mstOmsAttributes = createOrGetMstOmsAttribute(teamsDRAttributesBean.getAttributeName(),
				teamsDRAttributesBean.getDescription());
		mstOmsAttributes.forEach(mstOmsAttribute -> {
			try {
				createOrUpdateQuoteToLeAttributeValue(quoteLeId, mstOmsAttribute, teamsDRAttributesBean);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
			}
		});
		return teamsDRAttributesBean;
	}

	/**
	 * Creating or updating quote to le attribute values
	 *
	 * @param quoteLeId
	 * @param mstOmsAttribute
	 * @param attribute
	 * @return
	 * @throws TclCommonException
	 */private void createOrUpdateQuoteToLeAttributeValue(Integer quoteLeId, MstOmsAttribute mstOmsAttribute,
			TeamsDRAttributesBean attribute) throws TclCommonException {
		if (Objects.isNull(attribute.getAttributeId())) {
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setQuoteToLe(getQuoteToLeById(quoteLeId));
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setDisplayValue(attribute.getAttributeName());
			quoteLeAttributeValue.setAttributeValue(attribute.getAttributeValue());
			quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
			attribute.setAttributeId(quoteLeAttributeValue.getId());
		} else {
			quoteLeAttributeValueRepository.findById(attribute.getAttributeId()).ifPresent(quoteLeAttributeValue -> {
				if (!quoteLeAttributeValue.getAttributeValue().equals(attribute.getAttributeValue())) {
					quoteLeAttributeValue.setAttributeValue(attribute.getAttributeValue());
					quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
				}
			});
		}
	}

	/**
	 * Creating or getting mst oms attribute
	 *
	 * @param teamsDRAttributesBean
	 * @return
	 */
	protected List<MstOmsAttribute> createOrGetMstOmsAttribute(String attrName, String attrDescription) {
		List<MstOmsAttribute> mstOmsAttributes = new ArrayList<>();
		mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName, CommonConstants.BACTIVE);
		if (Objects.isNull(mstOmsAttributes) || mstOmsAttributes.isEmpty()) {
			MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setName(attrName);
			mstOmsAttribute.setDescription(attrDescription);
			mstOmsAttribute.setIsActive(CommonConstants.BACTIVE);
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setCreatedBy(Utils.getSource());
			mstOmsAttributeRepository.save(mstOmsAttribute);
			mstOmsAttributes.add(mstOmsAttribute);
		}
		return mstOmsAttributes;
	}

	/**
	 * Method to save license details
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param licenseComponents
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRLicenseComponentsBean saveLicenseDetails(Integer quoteId, Integer solutionId, Integer quoteToLeId,
			TeamsDRLicenseComponentsBean licenseComponents) throws TclCommonException {
		getQuote(quoteId);
		QuoteToLe quoteToLe = getQuoteToLeById(quoteToLeId);
		subtractLicensePriceFromQuoteLe(quoteToLe);
		ProductSolution productSolution = populateSolution(solutionId);
		QuoteTeamsDR quoteTeamsDR = getQuoteTeamsDRByProductSolution(productSolution).stream().findAny().get();
		List<QuoteTeamsLicense> quoteTeamsLicenses = saveQuoteTeamsLicense(licenseComponents, quoteToLe, quoteTeamsDR);
		licenseComponents = toTeamsDRLicenseComponentsBean(quoteTeamsLicenses, licenseComponents.getAgreementType(),
				quoteTeamsDR);
		TeamsDRMultiQuoteLeBean quoteLeBean = createQuoteToLeBean(quoteToLe);
		triggerPricingLicense(quoteId, quoteLeBean);
		updateQuoteToLeWithLicense(quoteToLe, quoteLeBean);
		// Creating product service in sfdc..
		if (Objects.isNull(productSolution.getTpsSfdcProductName())) {
			teamsDRSfdcService.createProductServiceInSfdc(quoteToLe, productSolution);
		} else {
			teamsDRSfdcService.updateProductServiceInSfdc(quoteToLe, productSolution);
		}
		return licenseComponents;
	}

	/**
	 * Method to convert list to toTeamsDRLicenseComponentsBean
	 *
	 * @param quoteTeamsLicenses
	 * @param agreementType
	 * @return
	 */
	public TeamsDRLicenseComponentsBean toTeamsDRLicenseComponentsBean(List<QuoteTeamsLicense> quoteTeamsLicenses,
			String agreementType,QuoteTeamsDR quoteTeamsDR) {
		TeamsDRLicenseComponentsBean teamsDRLicenseComponentsBean = new TeamsDRLicenseComponentsBean();
		teamsDRLicenseComponentsBean.setAgreementType(agreementType);
		Map<String, TeamsDRLicenseConfigurationBean> componentBeanMap = new HashMap<>();

		quoteTeamsLicenses.forEach(quoteTeamsLicense -> {
			if (componentBeanMap.containsKey(quoteTeamsLicense.getProvider())) {
				TeamsDRLicenseConfigurationBean componentBean = componentBeanMap.get(quoteTeamsLicense.getProvider());
				componentBean.getLicenseDetails().add(toTeamsDRLicenseBean(quoteTeamsLicense));
			} else {
				TeamsDRLicenseConfigurationBean componentBean = new TeamsDRLicenseConfigurationBean();
				componentBean.setProvider(quoteTeamsLicense.getProvider());
				try {
					componentBean.setLicenseComponents(getComponentDetail(quoteTeamsDR.getId(),
							TEAMSDR_LICENSE_ATTRIBUTES));
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
				componentBean.setLicenseDetails(new ArrayList<>());
				componentBean.getLicenseDetails().add(toTeamsDRLicenseBean(quoteTeamsLicense));
				componentBeanMap.put(quoteTeamsLicense.getProvider(), componentBean);
			}
		});
		teamsDRLicenseComponentsBean.setLicenseConfigurations(new ArrayList<>(componentBeanMap.values()));
		LOGGER.info("TeamsDRLicenseComponentsBean :" + teamsDRLicenseComponentsBean);
		return teamsDRLicenseComponentsBean;
	}

	/**
	 * Method to save quote teams license
	 *
	 * @param licenses
	 * @param quoteToLe
	 * @param productSolution
	 * @return
	 */
	public List<QuoteTeamsLicense> saveQuoteTeamsLicense(TeamsDRLicenseComponentsBean licenses,
			QuoteToLe quoteToLe, QuoteTeamsDR quoteTeamsDR) {
		LOGGER.info("Inside saveQuoteTeamsLicense");
		List<QuoteTeamsLicense> quoteTeamsLicenses = new ArrayList<>();

		// To get saved ids.
		Set<Integer> savedIds = quoteTeamsLicenseRepository.findByQuoteTeamsDR(quoteTeamsDR).stream()
				.map(QuoteTeamsLicense::getId).collect(Collectors.toSet());

		Set<Integer> requestIds = new HashSet<>();
		AtomicReference<Integer> totalLicenses = new AtomicReference<>(0);
		licenses.getLicenseConfigurations()
				.stream()
				.flatMap(teamsDRLicenseConfigurationBean -> teamsDRLicenseConfigurationBean.getLicenseDetails().stream())
				.forEach(teamsDRLicenseBean -> {
					totalLicenses.updateAndGet(v -> v + teamsDRLicenseBean.getNoOfLicenses());
				});

		LOGGER.info("Total Licenses :: {}",totalLicenses.get());
		deleteQuoteProductComponentAndValues(quoteTeamsDR.getId(),TEAMSDR_LICENSE_ATTRIBUTES);
		licenses.getLicenseConfigurations().forEach(teamsDRLicenseConfiguration -> {
			teamsDRLicenseConfiguration.getLicenseComponents().forEach(component ->{
				try {
					processProductComponent(quoteTeamsDR.getId(),component,TEAMSDR_LICENSE_ATTRIBUTES);
					List<QuoteProductComponentBean> components = getComponentDetail(quoteTeamsDR.getId(),
							TEAMSDR_LICENSE_ATTRIBUTES);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			});
			teamsDRLicenseConfiguration.getLicenseDetails().forEach(teamsDRLicenseBean -> {
				QuoteTeamsLicense quoteTeamsLicense;
				if (Objects.isNull(teamsDRLicenseBean.getId())) {
					quoteTeamsLicense = new QuoteTeamsLicense();
				} else {
					quoteTeamsLicense = quoteTeamsLicenseRepository.findById(teamsDRLicenseBean.getId()).get();
					requestIds.add(teamsDRLicenseBean.getId());
				}
				quoteTeamsLicense.setCreatedTime(new Date());
				quoteTeamsLicense.setQuoteVersion(1);
				quoteTeamsLicense.setLicenseName(teamsDRLicenseBean.getLicenseName());
				quoteTeamsLicense.setQuoteTeamsDR(quoteTeamsDR);
				quoteTeamsLicense.setAgreementType(licenses.getAgreementType());
				quoteTeamsLicense.setContractPeriod(teamsDRLicenseBean.getLicenseContractPeriod());
				quoteTeamsLicense.setProvider(teamsDRLicenseConfiguration.getProvider());
				quoteTeamsLicense.setNoOfLicenses(teamsDRLicenseBean.getNoOfLicenses());
				quoteTeamsLicense.setSfdcProductName(teamsDRLicenseBean.getSfdcProductName());
				quoteTeamsLicense = quoteTeamsLicenseRepository.save(quoteTeamsLicense);
				try {
					processProductComponent(quoteTeamsLicense.getId(),teamsDRLicenseBean.getLicenseSKUComponents().get(0),
							TEAMSDR_LICENSE_CHARGES);
					List<QuoteProductComponentBean> components = getComponentDetail(quoteTeamsLicense.getId(),
							TEAMSDR_LICENSE_CHARGES);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
				quoteTeamsLicenses.add(quoteTeamsLicense);
			});
		});

		Set<Integer> idsToDelete = Sets.difference(savedIds, requestIds);
		idsToDelete.forEach(id -> {
			LOGGER.info("QuoteTeamsLicense Id to delete :: {}",id);
			Optional<QuoteTeamsLicense> quoteTeamsLicense = quoteTeamsLicenseRepository.findById(id);
			if(quoteTeamsLicense.isPresent()){
				deleteQuoteProductComponentAndValues(quoteTeamsLicense.get().getId(),TEAMSDR_LICENSE_CHARGES);
				quoteTeamsLicenseRepository.delete(quoteTeamsLicense.get());
			}
		});
		Integer noOfUsers = licenses.getLicenseConfigurations().stream()
				.flatMap(config -> config.getLicenseDetails().stream())
				.filter(licenseDetail -> Objects.nonNull(licenseDetail) && Objects
						.nonNull(licenseDetail.getNoOfLicenses()))
				.mapToInt(licenseDetail -> licenseDetail.getNoOfLicenses()).sum();
		quoteTeamsDR.setNoOfUsers(noOfUsers);
		quoteTeamsDRRepository.save(quoteTeamsDR);
		return quoteTeamsLicenses;
	}

	/**
	 * To substract quote license prices from quote to le
	 *
	 * @param quoteTeamsLicenses
	 * @param quoteToLe
	 */
	public void subtractQuoteLicenseprices(List<QuoteTeamsLicense> quoteTeamsLicenses, QuoteToLe quoteToLe) {
		if (Objects.nonNull(quoteTeamsLicenses) && !quoteTeamsLicenses.isEmpty()) {
			TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();

			quoteTeamsLicenses.forEach(quoteTeamsLicense -> {
				teamsDRCumulativePricesBean
						.setMrc(checkForNull(quoteTeamsLicense.getMrc()) + teamsDRCumulativePricesBean.getMrc());
				teamsDRCumulativePricesBean
						.setNrc(checkForNull(quoteTeamsLicense.getNrc()) + teamsDRCumulativePricesBean.getNrc());
			});

			if (Objects.nonNull(quoteToLe.getFinalMrc()) && teamsDRCumulativePricesBean.getMrc() > 0) {
				quoteToLe.setFinalMrc(quoteToLe.getFinalMrc() - teamsDRCumulativePricesBean.getMrc());
			}

			if (Objects.nonNull(quoteToLe.getFinalNrc()) && teamsDRCumulativePricesBean.getNrc() > 0) {
				quoteToLe.setFinalNrc(quoteToLe.getFinalNrc() - teamsDRCumulativePricesBean.getNrc());
			}

			quoteToLe.setProposedMrc(checkForNull(quoteToLe.getFinalMrc()));
			quoteToLe.setProposedNrc(checkForNull(quoteToLe.getFinalNrc()));

			quoteToLe.setProposedArc(12 * checkForNull(quoteToLe.getProposedMrc()));
			quoteToLe.setFinalArc(12 * checkForNull(quoteToLe.getFinalMrc()));
			quoteToLe.setTotalTcv(quoteToLe.getFinalMrc() + quoteToLe.getFinalNrc());
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Updating TeamsDRConfiguration Based on ID.
	 * @param teamsDRConfigurationBean
	 * @param quoteId
	 * @param solutionId
	 * @param configId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRConfigurationBean updateTeamsDRConfigurationBasedOnId(TeamsDRConfigurationBean teamsDRConfigurationBean,Integer quoteId,
			Integer solutionId, Integer configId) throws TclCommonException {
		getQuote(quoteId);
		QuoteToLe quoteToLe = populateQuoteToLe(quoteId);
		getProductSolution(solutionId);
		QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.
				findByQuoteToLeIdAndIsConfig(quoteToLe.getId(), CommonConstants.BACTIVE).get();
		subtractPlanAndMgPriceFromQuoteLe(quoteToLe);
		Optional<QuoteTeamsDRDetails> quoteTeamsDRDetailsOptional = quoteTeamsDRDetailsRepository.findById(configId);
		if(quoteTeamsDRDetailsOptional.isPresent()){
			QuoteTeamsDRDetails quoteTeamsDRDetails = quoteTeamsDRDetailsOptional.get();
			quoteTeamsDRDetails.setNoOfCommonAreaDevices(teamsDRConfigurationBean.getNoOfCommonAreaDevices());
			quoteTeamsDRDetails.setNoOfNamedusers(teamsDRConfigurationBean.getNoOfNamedUsers());
			quoteTeamsDRDetails.setTotalUsers((Objects.nonNull(teamsDRConfigurationBean.getNoOfNamedUsers()) ?
					teamsDRConfigurationBean.getNoOfNamedUsers() :
					0) + (Objects.nonNull(teamsDRConfigurationBean.getNoOfCommonAreaDevices()) ?
					teamsDRConfigurationBean.getNoOfCommonAreaDevices() :
					0));
			quoteTeamsDRDetailsRepository.save(quoteTeamsDRDetails);
			List<QuoteTeamsDRDetails> quoteTeamsDRDetailsList = quoteTeamsDRDetailsRepository.
					findByQuoteTeamsDR(quoteTeamsDR);
			updateQuoteTeamsDR(quoteToLe, findTotalNoOfUsers(quoteTeamsDRDetailsList));
			teamsDRConfigurationBean = toQuoteTeamsdrDetailsBean(quoteTeamsDRDetails, null);
		}else{
			throw new TclCommonException(ExceptionConstants.QUOTE_TEAMSDR_DETAILS_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);
		}
		TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean = createQuoteToLeBean(quoteToLe);
		triggerPricingPlanAndMg(quoteId, teamsDRMultiQuoteLeBean);
		updateQuoteToLeWithPlanAndMg(quoteToLe, teamsDRMultiQuoteLeBean);
		return teamsDRConfigurationBean;
	}

	/**
	 * To find the cumulative total number of users from quoteTeamDrDetails.
	 * @param quoteTeamsDRDetails
	 * @return
	 */
	public Integer findTotalNoOfUsers(List<QuoteTeamsDRDetails> quoteTeamsDRDetails){
		AtomicReference<Integer> totalUsers = new AtomicReference<>(0);
		quoteTeamsDRDetails.forEach(quoteTeamsDRDetails1 -> totalUsers.updateAndGet(v ->
				v + (Objects.nonNull(quoteTeamsDRDetails1.getTotalUsers()) ? quoteTeamsDRDetails1.getTotalUsers():0)));
		return totalUsers.get();
	}


	/**
	 * Updating the totalUsers to all the teamsdr services
	 *
	 * @param quoteToLe
	 * @param totalusers
	 */
	private void updateQuoteTeamsDR(QuoteToLe quoteToLe, Integer totalusers) {
		List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
		quoteTeamsDRS.stream().filter(quoteTeamsDR -> Objects.isNull(quoteTeamsDR.getServiceName())
				|| (!MEDIA_GATEWAY.equals(quoteTeamsDR.getServiceName())
				&& !MICROSOFT_LICENSE.equals(quoteTeamsDR.getServiceName()))).forEach(quoteTeamsDR ->
				quoteTeamsDR.setNoOfUsers(totalusers));
		quoteTeamsDRRepository.saveAll(quoteTeamsDRS);
	}

	/**
	 * Add or update mg configuration.
	 * @param quoteId
	 * @param quoteLeId
	 * @param configId
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public List<TeamsDRCityBean> addOrUpdateMgConfiguration(Integer quoteId,Integer quoteLeId,Integer configId,
															List<TeamsDRCityBean> sites) throws TclCommonException {
		List<TeamsDRCityBean> updatedSites = null;
		Quote quote = getQuote(quoteId);
		QuoteToLe quoteToLe = getQuoteToLeById(quoteLeId);
		Optional<QuoteTeamsDRDetails> quoteTeamsDRDetails = quoteTeamsDRDetailsRepository.findById(configId);
		Optional<QuoteTeamsDR> quoteTeamsDR = quoteTeamsDRRepository.findByQuoteToLeIdAndServiceName(quoteLeId,MEDIA_GATEWAY);
		if(quoteTeamsDRDetails.isPresent() && quoteTeamsDR.isPresent()){
			subtractPlanAndMgPriceFromQuoteLe(quoteToLe);
			Optional<QuoteDirectRouting> quoteDirectRouting = quoteDirectRoutingRepository
					.findByQuoteTeamsDRAndCountry(quoteTeamsDR.get(),quoteTeamsDRDetails.get().getCountry());
			if(quoteDirectRouting.isPresent()){
				updatedSites = saveSiteDetails(quoteDirectRouting.get(),quoteToLe,sites);
			}
			triggerPricingPlanAndMg(quoteId, createQuoteToLeBean(quoteToLe));
			updateQuoteToLeWithPlanAndMg(quoteToLe, new TeamsDRMultiQuoteLeBean());
		}else{
			throw new TclCommonException(ExceptionConstants.QUOTE_DIRECT_ROUTING_NOT_FOUND,ResponseResource.R_CODE_NOT_FOUND);
		}
		return updatedSites;
	}

	/**
	 * Subtract quoteteamsdr from quotetole.
	 * @param quoteToLe
	 * @param quoteTeamsDR
	 */
	public void subtractQuoteTeamsDRFromQuoteToLe(QuoteToLe quoteToLe,QuoteTeamsDR quoteTeamsDR){
		quoteToLe.setProposedMrc(checkForNull(quoteToLe.getProposedMrc()) - checkForNull(quoteTeamsDR.getMrc()));
		quoteToLe.setProposedNrc(checkForNull(quoteToLe.getProposedNrc()) - checkForNull(quoteTeamsDR.getNrc()));
		quoteToLe.setProposedArc(checkForNull(quoteToLe.getProposedArc()) - checkForNull(quoteTeamsDR.getArc()));
		quoteToLe.setTotalTcv(checkForNull(quoteToLe.getTotalTcv()) -  checkForNull(quoteTeamsDR.getTcv()));
		quoteToLe.setFinalMrc(quoteToLe.getProposedMrc());
		quoteToLe.setFinalNrc(quoteToLe.getProposedNrc());
		quoteToLe.setFinalArc(quoteToLe.getProposedArc());
		quoteToLeRepository.save(quoteToLe);
	}

	/**
	 * Add quoteteamsdr price to quotetole.
	 * @param quoteToLe
	 * @param serviceName
	 */
	private void addQuoteTeamsDRToQuoteTole(QuoteToLe quoteToLe,String serviceName){
		quoteTeamsDRRepository.findByQuoteToLeIdAndServiceName(quoteToLe.getId(),serviceName).ifPresent(quoteTeamsDR -> {
			quoteToLe.setProposedMrc(checkForNull(quoteToLe.getProposedMrc()) + checkForNull(quoteTeamsDR.getMrc()));
			quoteToLe.setProposedNrc(checkForNull(quoteToLe.getProposedNrc()) + checkForNull(quoteTeamsDR.getNrc()));
			quoteToLe.setProposedArc(checkForNull(quoteToLe.getProposedArc()) + checkForNull(quoteTeamsDR.getArc()));
			quoteToLe.setTotalTcv(checkForNull(quoteToLe.getTotalTcv()) +  checkForNull(quoteTeamsDR.getTcv()));
			quoteToLe.setFinalMrc(quoteToLe.getProposedMrc());
			quoteToLe.setFinalNrc(quoteToLe.getProposedNrc());
			quoteToLe.setFinalArc(quoteToLe.getProposedArc());
			quoteToLeRepository.save(quoteToLe);
		});
	}

	/**
	 * Method to get cummulative price.
	 * @param quoteTeamsDR
	 */
	private void updateCummulativePriceForMg(QuoteTeamsDR quoteTeamsDR){
		TeamsDRCumulativePricesBean mgCumulative = new TeamsDRCumulativePricesBean();
		TeamsDRCumulativePricesBean cityCumulative = new TeamsDRCumulativePricesBean();
		List<QuoteDirectRouting> quoteDirectRoutings = quoteDirectRoutingRepository.findByQuoteTeamsDR(quoteTeamsDR);
		quoteDirectRoutings.forEach(quoteDirectRouting -> {
			List<QuoteDirectRoutingCity> cities = quoteDirectRoutingCityRepository.findByQuoteDirectRoutingId(quoteDirectRouting.getId());
			cities.forEach(city->{
				List<QuoteDirectRoutingMediaGateways> mgs = quoteDirectRoutingMgRepository.findByQuoteDirectRoutingCityId(city.getId());
				mgs.forEach(mg->{
					mgCumulative.setMrc(
							mgCumulative.getMrc() + checkForNull(mg.getMrc()));
					mgCumulative.setNrc(
							mgCumulative.getNrc() + checkForNull(mg.getNrc()));
					mgCumulative.setArc(
							mgCumulative.getArc() + checkForNull(mg.getArc()));
					mgCumulative.setTcv(mgCumulative.getTcv()
							+ checkForNull(mg.getTcv()));
				});
				city.setMrc(BigDecimal.valueOf(mgCumulative.getMrc()));
				city.setNrc(BigDecimal.valueOf(mgCumulative.getNrc()));
				city.setArc(BigDecimal.valueOf(mgCumulative.getArc()));
				city.setTcv(BigDecimal.valueOf(mgCumulative.getTcv()));
				quoteDirectRoutingCityRepository.save(city);
				cityCumulative.setMrc(
						cityCumulative.getMrc() + checkForNull(mgCumulative.getMrc()));
				cityCumulative.setNrc(
						cityCumulative.getNrc() + checkForNull(mgCumulative.getNrc()));
				cityCumulative.setArc(
						cityCumulative.getArc() + checkForNull(mgCumulative.getArc()));
				cityCumulative.setTcv(cityCumulative.getTcv()
						+ checkForNull(mgCumulative.getTcv()));
				resetPrice(mgCumulative);
			});
			quoteDirectRouting.setMrc(BigDecimal.valueOf(cityCumulative.getMrc()));
			quoteDirectRouting.setNrc(BigDecimal.valueOf(cityCumulative.getNrc()));
			quoteDirectRouting.setArc(BigDecimal.valueOf(cityCumulative.getArc()));
			quoteDirectRouting.setTcv(BigDecimal.valueOf(cityCumulative.getTcv()));
			quoteDirectRoutingRepository.save(quoteDirectRouting);
			cityCumulative.setTotalMrc(
					cityCumulative.getTotalMrc() + checkForNull(cityCumulative.getMrc()));
			cityCumulative.setTotalNrc(
					cityCumulative.getTotalNrc() + checkForNull(cityCumulative.getNrc()));
			cityCumulative.setTotalArc(
					cityCumulative.getTotalArc() + checkForNull(cityCumulative.getArc()));
			cityCumulative.setTotalTcv(
					cityCumulative.getTotalTcv() + checkForNull(cityCumulative.getTcv()));
			resetPrice(cityCumulative);
		});
		quoteTeamsDR.setMrc(cityCumulative.getTotalMrc());
		quoteTeamsDR.setNrc(cityCumulative.getTotalNrc());
		quoteTeamsDR.setArc(cityCumulative.getTotalArc());
		quoteTeamsDR.setTcv(cityCumulative.getTotalTcv());
		quoteTeamsDRRepository.save(quoteTeamsDR);
	}

	/**
	 * Method to reset teamsDRCumulativePricesBean
	 * @param teamsDRCumulativePricesBean
	 */
	private void resetPrice(TeamsDRCumulativePricesBean teamsDRCumulativePricesBean){
		teamsDRCumulativePricesBean.setMrc(0.0);
		teamsDRCumulativePricesBean.setNrc(0.0);
		teamsDRCumulativePricesBean.setArc(0.0);
		teamsDRCumulativePricesBean.setTcv(0.0);
	}

	/**
	 * Method to save site details.
	 * @param quoteDirectRouting
	 * @param quoteToLe
	 * @param sites
	 * @return
	 */
	private List<TeamsDRCityBean> saveSiteDetails(QuoteDirectRouting quoteDirectRouting,QuoteToLe quoteToLe, List<TeamsDRCityBean> sites){
		LOGGER.info("Inside save site details");
		List<QuoteDirectRoutingCity> quoteDirectRoutingCityList;
		Set<Integer> savedCityIds = quoteDirectRoutingCityRepository
				.findByQuoteDirectRoutingId(quoteDirectRouting.getId()).stream().map(QuoteDirectRoutingCity::getId)
				.collect(Collectors.toSet());
		Set<Integer> requestCityIds = new HashSet<>();
		sites.forEach(teamsDRCityBean -> {
			QuoteDirectRoutingCity quoteDirectRoutingCity;
			if (Objects.nonNull(teamsDRCityBean.getId())) {
				quoteDirectRoutingCity = quoteDirectRoutingCityRepository.findById(teamsDRCityBean.getId()).get();
				List<QuoteDirectRoutingMediaGateways> mgs = quoteDirectRoutingMgRepository
						.findByQuoteDirectRoutingCityId(quoteDirectRoutingCity.getId());
				mgs.forEach(mg->{
					deleteQuoteProductComponentAndValues(mg.getId(),TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);
					quoteDirectRoutingMgRepository.delete(mg);
				});
				requestCityIds.add(teamsDRCityBean.getId());
			} else {
				quoteDirectRoutingCity = new QuoteDirectRoutingCity();
			}
			quoteDirectRoutingCity.setQuoteDirectRouting(quoteDirectRouting);
			quoteDirectRoutingCity.setMrc(BigDecimal.ZERO);
			quoteDirectRoutingCity.setName(teamsDRCityBean.getCity());
			quoteDirectRoutingCity.setNrc(BigDecimal.ZERO);
			quoteDirectRoutingCity.setArc(BigDecimal.ZERO);
			quoteDirectRoutingCity.setTcv(BigDecimal.ZERO);
			quoteDirectRoutingCity.setMediaGatewayType(teamsDRCityBean.getMediaGatewayType());
			quoteDirectRoutingCity.setCreatedTime(new Date());
			quoteDirectRoutingCity.setQuoteVersion(1);
			if (!teamsDRCityBean.getMediaGatewayType().equals(TeamsDRConstants.NONE)) {
				if (Objects.nonNull(teamsDRCityBean.getMediaGateway())
						&& !teamsDRCityBean.getMediaGateway().isEmpty()) {
					List<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateways = new ArrayList<>();
					teamsDRCityBean.getMediaGateway().forEach(teamsDRMediaGatewayBean -> {
						QuoteDirectRoutingMediaGateways quoteDirectRoutingMediaGateway = TeamsDRMediaGatewayBean
								.toTeamsDrMediaGatewayBean(teamsDRMediaGatewayBean, quoteDirectRoutingCity);
						quoteDirectRoutingMediaGateway = quoteDirectRoutingMgRepository.save(quoteDirectRoutingMediaGateway);
						quoteDirectRoutingMediaGateways.add(quoteDirectRoutingMediaGateway);
						if(Objects.nonNull(teamsDRMediaGatewayBean.getMediaGatewayComponents()) && !teamsDRMediaGatewayBean.getMediaGatewayComponents().isEmpty()){
							LOGGER.info("Inside process product component");
							try {
								processProductComponent(quoteDirectRoutingMediaGateway.getId(),
										teamsDRMediaGatewayBean.getMediaGatewayComponents().get(0),TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);
								List<QuoteProductComponentBean> components = getComponentDetail(quoteDirectRoutingMediaGateway.getId(),
										TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
					});
					quoteDirectRoutingCity.setQuoteDirectRoutingMediaGateways(quoteDirectRoutingMediaGateways);

				} else {
					quoteDirectRoutingCity.setQuoteDirectRoutingMediaGateways(null);
				}
			} else {
				quoteDirectRoutingCity.setQuoteDirectRoutingMediaGateways(null);
			}
			QuoteDirectRoutingCity directRoutingCity = quoteDirectRoutingCityRepository.save(quoteDirectRoutingCity);



			// Saving the attributes
			teamsDRCityBean.getComponents().forEach(componentBean -> {
				try {
					processProductComponent(directRoutingCity.getId(),componentBean,TEAMSDR_SITE_ATTRIBUTES);
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
		});

		Set<Integer> toDelete = Sets.difference(savedCityIds, requestCityIds);
		deleteSiteDetails(toDelete);
		quoteDirectRoutingCityList = quoteDirectRoutingCityRepository
				.findByQuoteDirectRoutingId(quoteDirectRouting.getId());
		return quoteDirectRoutingCityList.stream().map(this::toTeamsdrCityDetailsBean)
				.collect(Collectors.toList());
	}

	/**
	 * Method to delete all the site details
	 * and underlying components.
	 * @param toDelete
	 */
	private void deleteSiteDetails(Set<Integer> toDelete){
		toDelete.forEach(cityId -> {
			LOGGER.info("Deleting site id :: {}",cityId);
			Optional<QuoteDirectRoutingCity> directRoutingOptional = quoteDirectRoutingCityRepository.findById(cityId);
			deleteSiteAndUnderLyingComponents(directRoutingOptional.get());
		});
	}

	/**
	 * Method to get site details.
	 * @param quoteDirectRouting
	 * @return
	 */
	public List<TeamsDRCityBean> getSiteDetails(QuoteDirectRouting quoteDirectRouting){
		List<QuoteDirectRoutingCity> quoteDirectRoutingCityList = quoteDirectRoutingCityRepository
				.findByQuoteDirectRoutingId(quoteDirectRouting.getId());
		return quoteDirectRoutingCityList.stream().map(this::toTeamsdrCityDetailsBean)
				.collect(Collectors.toList());
	}

	/**
	 *
	 * @param quoteDirectRoutingCity
	 * @return
	 */
	public TeamsDRCityBean toTeamsdrCityDetailsBean(QuoteDirectRoutingCity quoteDirectRoutingCity) {
		TeamsDRCityBean teamsDRCityBean = new TeamsDRCityBean();
		teamsDRCityBean.setId(quoteDirectRoutingCity.getId());
		LOGGER.info(String.valueOf(quoteDirectRoutingCity.getId()));
		teamsDRCityBean.setCity(quoteDirectRoutingCity.getName());
		if (Objects.nonNull(quoteDirectRoutingCity.getQuoteDirectRoutingMediaGateways())) {
			if (Objects.nonNull(quoteDirectRoutingCity.getQuoteDirectRoutingMediaGateways())
					&& !quoteDirectRoutingCity.getQuoteDirectRoutingMediaGateways().isEmpty()) {
				teamsDRCityBean.setMediaGateway(quoteDirectRoutingCity.getQuoteDirectRoutingMediaGateways().stream()
						.map(this::toTeamsDrMediaGatewayBean).collect(Collectors.toList()));
			}
		} else {
			teamsDRCityBean.setMediaGateway(null);
		}
		try {
			teamsDRCityBean.setComponents(getComponentDetail(quoteDirectRoutingCity.getId(),TEAMSDR_SITE_ATTRIBUTES));
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		teamsDRCityBean.setMediaGatewayType(quoteDirectRoutingCity.getMediaGatewayType());
		teamsDRCityBean.setArc(quoteDirectRoutingCity.getArc());
		teamsDRCityBean.setMrc(quoteDirectRoutingCity.getMrc());
		teamsDRCityBean.setNrc(quoteDirectRoutingCity.getNrc());
		teamsDRCityBean.setTcv(quoteDirectRoutingCity.getTcv());
		return teamsDRCityBean;
	}

	/**
	 *
	 * @param quoteDirectRoutingMediaGateways
	 * @return
	 */
	public TeamsDRMediaGatewayBean toTeamsDrMediaGatewayBean(
			QuoteDirectRoutingMediaGateways quoteDirectRoutingMediaGateways) {
		TeamsDRMediaGatewayBean teamsDRMediaGatewayBean = new TeamsDRMediaGatewayBean();
		teamsDRMediaGatewayBean.setId(quoteDirectRoutingMediaGateways.getId());
		teamsDRMediaGatewayBean.setName(quoteDirectRoutingMediaGateways.getName());
		teamsDRMediaGatewayBean.setMrc((quoteDirectRoutingMediaGateways.getMrc()));
		teamsDRMediaGatewayBean.setNrc((quoteDirectRoutingMediaGateways.getNrc()));
		teamsDRMediaGatewayBean.setArc((quoteDirectRoutingMediaGateways.getArc()));
		teamsDRMediaGatewayBean.setTcv((quoteDirectRoutingMediaGateways.getTcv()));
		teamsDRMediaGatewayBean.setQuantity(quoteDirectRoutingMediaGateways.getQuantity());
		try {
			teamsDRMediaGatewayBean.setMediaGatewayComponents(
					getComponentDetail(quoteDirectRoutingMediaGateways.getId(),TEAMSDR_MEDIAGATEWAY_ATTRIBUTES));
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
		return teamsDRMediaGatewayBean;
	}


	/**
	 * Method to get all the attributes
	 * @param quoteDirectRoutingCity
	 * @return
	 */
	public List<QuoteProductComponentBean> getComponentDetail(Integer referenceId,String referenceName) throws TclCommonException {
		MstProductFamily mstProductFamily = getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, mstProductFamily,referenceName);
		List<QuoteProductComponentBean> componentBeans = new ArrayList<>();
		quoteProductComponents.forEach(quoteProductComponent -> {
			QuoteProductComponentBean componentBean = new QuoteProductComponentBean();
			componentBean.setName(quoteProductComponent.getMstProductComponent().getName());
			List<QuoteProductComponentsAttributeValueBean> attributeDetailList = new ArrayList<>();
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository.
					findByQuoteProductComponent(quoteProductComponent);
			attributeValues.forEach(quoteProductComponentsAttributeValue -> {
				QuoteProductComponentsAttributeValueBean attributeDetail = new QuoteProductComponentsAttributeValueBean();
				attributeDetail.setId(quoteProductComponentsAttributeValue.getId());
				attributeDetail.setIsAdditionalParam(quoteProductComponentsAttributeValue.getIsAdditionalParam());
				attributeDetail.setName(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName());
				attributeDetail.setAttributeId(quoteProductComponentsAttributeValue.getId());
				attributeDetail.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
				attributeDetail.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
				if (ALL_CHARGES.contains(attributeDetail.getName()) || TeamsDRConstants.MEDIA_GATEWAY_COMMERCIALS
						.contains(attributeDetail.getDisplayValue()) || TeamsDRConstants.SIMPLE_SERVICES
						.equalsIgnoreCase(quoteProductComponent.getMstProductComponent()
								.getName()) || TeamsDRConstants.PROFESSIONAL_SERVICES
						.equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())) {
					Optional<QuotePrice> quotePrice = quotePriceRepository
							.findByReferenceNameAndReferenceId(ATTRIBUTES, attributeDetail.getAttributeId().toString())
							.stream().findAny();
					if (quotePrice.isPresent()) {
						attributeDetail.setPrice(new QuotePriceBean(quotePrice.get()));
					}
				}
				attributeDetailList.add(attributeDetail);
			});
			attributeDetailList.sort(Comparator.comparing(QuoteProductComponentsAttributeValueBean::getDisplayValue,
					Comparator.nullsFirst(Comparator.naturalOrder()))
					.thenComparing(QuoteProductComponentsAttributeValueBean::getAttributeId));
			componentBean.setAttributes(attributeDetailList);
			componentBeans.add(componentBean);
		});
		return componentBeans;
	}

	/**
	 *
	 * @param referenceId
	 * @param component
	 * @param referenceName
	 * @throws TclCommonException
	 */
	private void processProductComponent(Integer referenceId, QuoteProductComponentBean component, String referenceName)
			throws TclCommonException {
		try {
			LOGGER.info("ReferenceId :: {} ,  ReferenceName :: {}, ComponentName :: {}",referenceId,referenceName,component.getName());
			MstProductFamily mstProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS, CommonConstants.BACTIVE);
			User user = getUserId(Utils.getSource());
			MstProductComponent productComponent = getProductComponent(component.getName(), user);
			QuoteProductComponent quoteProductComponent = constructProductComponent(productComponent, mstProductFamily,
					referenceId, referenceName);
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
			Set<Integer> savedIds = attributeValues.stream().map(attribute -> attribute.getId())
					.collect(Collectors.toSet());
			Set<Integer> requestIds = new HashSet<>();
			component.getAttributes().forEach(attributeDetail -> {
				if (Objects.nonNull(attributeDetail.getAttributeId())) {
					requestIds.add(attributeDetail.getAttributeId());
				}
				processProductAttribute(quoteProductComponent, attributeDetail, user);
			});
			Set<Integer> attributesToDelete = Sets.difference(savedIds, requestIds);
			attributesToDelete.forEach(attributeId -> {
				Optional<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueRepository
						.findById(attributeId);
				if (quoteProductComponentsAttributeValue.isPresent()) {
					List<QuotePrice> quotePrices = quotePriceRepository.findByReferenceNameAndReferenceId(ATTRIBUTES,
							quoteProductComponentsAttributeValue.get().getId().toString());
					quotePriceRepository.deleteAll(quotePrices);
					quoteProductComponentsAttributeValueRepository.delete(quoteProductComponentsAttributeValue.get());
				}

			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Process product attribute
	 *
	 * @param quoteComponent
	 * @param attribute
	 * @param user
	 */
	private void processProductAttribute(QuoteProductComponent quoteComponent,
			QuoteProductComponentsAttributeValueBean attribute, User user) {
		ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = constructProductAttribute(
				quoteComponent, productAttribute, attribute);
		quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
	}

	/**
	 * Construct product Attribute
	 *
	 * @param quoteProductComponent
	 * @param productAttributeMaster
	 * @param attributeDetail
	 * @return
	 */
	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, QuoteProductComponentsAttributeValueBean attributeDetail) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = null;
		Optional<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueOptional = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, productAttributeMaster)
				.stream().findFirst();
		if (!quoteProductComponentsAttributeValueOptional.isPresent()) {
			quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
			quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
			quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getAttributeValues());
			quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		} else {
			quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueOptional.get();
			quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getAttributeValues());
		}
		return quoteProductComponentsAttributeValue;
	}

	/**
	 * get Product Attributes
	 *
	 * @param attributeDetail
	 * @param user
	 * @return
	 */
	public ProductAttributeMaster getProductAttributes(QuoteProductComponentsAttributeValueBean attributeDetail,
			User user) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
	}

	/**
	 * Get product attribute master.
	 * @param attrName
	 * @param user
	 * @return
	 */
	public ProductAttributeMaster getProductAttributes(String attrName) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attrName, (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			User user = getUserId(Utils.getSource());
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attrName);
			productAttributeMaster.setDescription(attrName);
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
	}

	/**
	 * Get or create mst product component
	 *
	 * @param component
	 * @return
	 */
	public MstProductComponent getProductComponent(String componentName, User user) {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(componentName, (byte) 1);
		if (Objects.nonNull(mstProductComponents) && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.stream().findFirst().get();

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setName(componentName);
			mstProductComponent.setDescription(componentName);
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}

		return mstProductComponent;
	}

	/**
	 * Construct Product component
	 *
	 * @param productComponent
	 * @param mstProductFamily
	 * @param orderUcaasId
	 * @return
	 */
	public QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer referenceId,
			String referenceName) {
		QuoteProductComponent quoteProductComponent = null;
		Optional<QuoteProductComponent> optionalQuoteProductComponent = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(referenceId, productComponent.getName(),
						referenceName)
				.stream().findFirst();
		if (!optionalQuoteProductComponent.isPresent()) {
			quoteProductComponent = new QuoteProductComponent();
			quoteProductComponent.setMstProductComponent(productComponent);
			quoteProductComponent.setMstProductFamily(mstProductFamily);
			quoteProductComponent.setReferenceId(referenceId);
			quoteProductComponent.setReferenceName(referenceName);
			quoteProductComponent.setType(TeamsDRConstants.PRIMARY);
			quoteProductComponentRepository.save(quoteProductComponent);
		} else {
			quoteProductComponent = optionalQuoteProductComponent.get();
		}
		return quoteProductComponent;

	}

	/**
	 * Method to update quotetole.
	 *
	 * @param solutionBeans
	 */
	public void updateLeInQuoteToLe(List<SolutionBean> solutionBeans) {
		for (SolutionBean bean : solutionBeans) {
			QuoteToLe quoteToLe = quoteToLeRepository.findById(bean.getQuoteToLeId()).get();
			quoteToLe.setErfCusCustomerLegalEntityId(bean.getLeId());
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Method to construct rules engine request.
	 *
	 * @param productSolutionList
	 * @param solutionBeans
	 * @param quoteToLes
	 */
	private void constructRulesEngineRequest(List<ProductSolution> productSolutionList,
											 List<SolutionBean> solutionBeans, List<QuoteToLe> quoteToLes, QuoteToLe parentQuoteTole) {
		for (ProductSolution productSolution : productSolutionList) {
			LOGGER.info("Product solution :: {}", productSolution.getId());
			QuoteToLe quoteToLe = productSolution.getQuoteToLeProductFamily().getQuoteToLe();
			SolutionBean solutionBean = new SolutionBean();
			solutionBean.setSolutionId(productSolution.getId());
			solutionBean.setLeId(quoteToLe.getErfCusCustomerLegalEntityId());

			List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository.findByProductSolutionAndStatus(productSolution,
					CommonConstants.BACTIVE);

			// For gsc solutions
			if (quoteTeamsDRs.isEmpty()) {
				QuoteGsc quoteGsc = quoteGscRepository
						.findByProductSolutionAndStatus(productSolution, GscConstants.STATUS_ACTIVE).get(0);
				solutionBean.setOfferingName(quoteGsc.getProductName());
				List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGsc(quoteGsc);
				quoteGscDetails.forEach(quoteGscDetail -> {
					if (Objects.nonNull(quoteGscDetail.getSrc())) {
						solutionBean.setCountry(quoteGscDetail.getSrc());
					} else {
						solutionBean.setCountry(quoteGscDetail.getDest());
					}
				});
				Integer solutionQuoteTole = productSolution.getQuoteToLeProductFamily().getQuoteToLe().getId();
				solutionBean.setQuoteToLeId(solutionQuoteTole.equals(parentQuoteTole.getId()) ? null : solutionQuoteTole);
				solutionBean.setProductName(GscConstants.GSIP_PRODUCT_NAME);
			} else {
				// for teamsdr solutions
				QuoteTeamsDR quoteTeamsDR = quoteTeamsDRs.stream().findAny().get();
				solutionBean.setProductName(TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS);
				solutionBean
						.setOfferingName(Objects.nonNull(quoteTeamsDR.getServiceName()) ? quoteTeamsDR.getServiceName()
								: quoteTeamsDR.getProfileName());
				if (!MICROSOFT_LICENSE.equals(quoteTeamsDR.getServiceName())) {
					solutionBean.setQuoteToLeId(parentQuoteTole.getId());
				} else {
					// For license
					Integer solutionQuoteTole = productSolution.getQuoteToLeProductFamily().getQuoteToLe().getId();
					solutionBean.setQuoteToLeId(solutionQuoteTole.equals(parentQuoteTole.getId()) ? null : solutionQuoteTole);
				}
			}
			solutionBeans.add(solutionBean);
		}
	}

	/**
	 * Method to find parent quote to le..
	 *
	 * @param quoteToLes
	 * @return
	 */
	public QuoteToLe findParentQuoteToLe(List<QuoteToLe> quoteToLes) {
		for (QuoteToLe quoteToLe : quoteToLes) {
			List<QuoteTeamsDR> quoteTeamsDRS = quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId());
			if (!quoteTeamsDRS.isEmpty()) {
				for (QuoteTeamsDR quoteTeamsDR : quoteTeamsDRS) {
					if(Objects.nonNull(quoteTeamsDR.getProfileName())){
						if (quoteTeamsDR.getProfileName().contains(PLAN)) {
							return quoteToLe;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Method to segrate based on product solutions
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public String segregateBasedOnSolutions(Integer quoteId) throws TclCommonException {
		Quote quote = getQuote(quoteId);
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		//subtract prices from quote to le
		quoteToLeProductFamilyRepository.findByQuoteToLeIn(quoteToLes).stream().filter(Objects::nonNull)
				.forEach(qpf -> {
					if (TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS
							.equalsIgnoreCase(qpf.getMstProductFamily().getName())) {
						subtractQuoteToLeWithTeamsDRPrice(qpf.getQuoteToLe());
					} else if (GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE
							.equalsIgnoreCase(qpf.getMstProductFamily().getName()))
						gscMultiLEQuoteService
								.subtractVoicePricesInQuoteToLe(quoteGscRepository.findByQuoteToLe(qpf.getQuoteToLe()));
				});
		QuoteToLe parentQuoteToLe = findParentQuoteToLe(quoteToLes);

		LOGGER.info("Parent QuoteToLe :: {}", parentQuoteToLe.getId());

		List<ProductSolution> productSolutionList = new ArrayList<>();
		TeamsDRRulesRequest teamsDRRulesRequest = new TeamsDRRulesRequest();
		List<SolutionBean> solutionBeans = new ArrayList<>();

		teamsDRRulesRequest.setSolutions(solutionBeans);

		MstProductFamily mstProductFamily = getProductFamily(TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS);

		// Adding all the solutions of all quote to le's
		for (QuoteToLe quoteToLe : quoteToLes) {
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			quoteToLeProductFamilies.forEach(quoteToLeProductFamily -> {
				productSolutionList
						.addAll(productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily));
			});
		}

		// To construct rules engine request.
		constructRulesEngineRequest(productSolutionList, solutionBeans, quoteToLes, parentQuoteToLe);

		String request = Utils.convertObjectToJson(teamsDRRulesRequest);

		LOGGER.info("The request for rules engine is :: {}", request);
		String response = (String) mqUtils.sendAndReceive(solutionSegregateQuote, request);

		LOGGER.info("Response from queue::{}", response);

		TeamsDRRulesResponseWrapper teamsDRProducts = (TeamsDRRulesResponseWrapper) Utils.convertJsonToObject(response,
				TeamsDRRulesResponseWrapper.class);

		LOGGER.info("Response after convertion :: {}", teamsDRProducts.toString());

		processRulesResponse(teamsDRProducts, quote, mstProductFamily, parentQuoteToLe);

		//update quote to le prices
		quoteToLes = quoteToLeRepository.findByQuote(quote);
		quoteToLeProductFamilyRepository.findByQuoteToLeIn(quoteToLes).stream().filter(Objects::nonNull)
				.forEach(qpf -> {
					if (TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS
							.equalsIgnoreCase(qpf.getMstProductFamily().getName())) {
						updateQuoteToLeWithTeamsDRPrices(qpf.getQuoteToLe(), new TeamsDRMultiQuoteLeBean());
					} else if (GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE
							.equalsIgnoreCase(qpf.getMstProductFamily().getName()))
						gscMultiLEQuoteService.updateQuoteToLeWithQuoteGscPrices(qpf.getQuoteToLe());
				});

		for (TeamsDRRulesResponse ucaasProduct : teamsDRProducts.getResponse()) {
			ucaasProduct.getSolutionBeans().forEach(solutionBean ->
					System.out.println(ucaasProduct.getKey() + " | " + solutionBean.getOfferingName() + " | "
							+ solutionBean.getCountry() + " | " + solutionBean.getLeId() + " | "
							+ solutionBean.getQuoteToLeId()));
		}
		return CommonConstants.SUCCESS;
	}

	/**
	 * Method to update cummulative price in overall quotetoles for a quoteid.
	 * @param quoteId
	 */
	private void updateCummulativePriceInQuoteToLe(Integer quoteId){
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);

		if(Objects.nonNull(quoteToLes) && !quoteToLes.isEmpty()){
			quoteToLes.forEach(quoteToLe -> {
				List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId());
				if(Objects.nonNull(quoteToLeProductFamilies) && !quoteToLeProductFamilies.isEmpty() && quoteToLeProductFamilies.size()<2){
					quoteToLeProductFamilies.forEach(quoteToLeProductFamily -> {
						String productFamily = quoteToLeProductFamily.getMstProductFamily().getName();
						if(MICROSOFT_CLOUD_SOLUTIONS.equals(productFamily)){
							setQuoteToLeWithTeamsDRPrices(quoteToLe);
						}else if(GSIP_PRODUCT_NAME.equals(productFamily)){
							setQuoteToLeWithQuoteGscPrices(quoteToLe);
						}
					});
				}else{
					// To update both teamsdr and quote gsc prices..
					setQuoteToLeWithTeamsDRPrices(quoteToLe);
					updateQuoteToLeWithGscPrices(quoteToLe);
				}
			});
		}
	}

	/**
	 * Method to update quotetole with quotegsc prices.
	 * @param quoteToLe
	 */
	private void setQuoteToLeWithQuoteGscPrices(QuoteToLe quoteToLe){
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if(Objects.nonNull(quoteGscs) && !quoteGscs.isEmpty()){
			quoteGscs.forEach(quoteGsc -> {
				LOGGER.info("QuoteGsc :: {}", quoteGsc.getId());
				teamsDRCumulativePricesBean.setTotalMrc(teamsDRCumulativePricesBean.getTotalMrc()+checkForNull(quoteGsc.getMrc()));
				teamsDRCumulativePricesBean.setTotalNrc(teamsDRCumulativePricesBean.getTotalNrc()+checkForNull(quoteGsc.getNrc()));
				teamsDRCumulativePricesBean.setTotalArc(teamsDRCumulativePricesBean.getTotalArc()+checkForNull(quoteGsc.getArc()));
				teamsDRCumulativePricesBean.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv()+checkForNull(quoteGsc.getTcv()));
			});

			quoteToLe.setProposedMrc(teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setProposedNrc(teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setProposedArc(teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setFinalMrc(teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setFinalNrc(teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setFinalArc(teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv());
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Method to construct quote to le from parent quote to le
	 *
	 * @param quote
	 * @param parentQuoteToLe
	 * @param offeringName
	 * @return
	 */
	private QuoteToLe constructNewQuoteToLe(Quote quote, QuoteToLe parentQuoteToLe, String offeringName) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setCurrencyCode(LeAttributesConstants.USD);
		quoteToLe.setStage(parentQuoteToLe.getStage());
		quoteToLe.setQuoteLeCode(Utils.generateUid(5));
		if (MICROSOFT_LICENSE.equals(offeringName)) {
			quoteToLe.setTermInMonths("12 Months");
		} else {
			quoteToLe.setTermInMonths(parentQuoteToLe.getTermInMonths());
		}
		quoteToLe.setQuoteType(parentQuoteToLe.getQuoteType());

		quoteToLe.setQuoteCategory(parentQuoteToLe.getQuoteCategory());
		quoteToLe.setClassification(parentQuoteToLe.getClassification());
		return quoteToLe;
	}

	/**
	 * Method to process rules response.
	 * @param teamsDRProducts
	 * @param quote
	 * @param teamsDRProductFamily
	 * @param parentQuoteTole
	 * @throws TclCommonException
	 */
	private void processRulesResponse(TeamsDRRulesResponseWrapper teamsDRProducts, Quote quote,
									  MstProductFamily teamsDRProductFamily, QuoteToLe parentQuoteTole) throws TclCommonException {

		User user = getUserId(Utils.getSource());

		MstProductFamily gscProductFamily = getProductFamily(GSIP_PRODUCT_NAME);

		for (TeamsDRRulesResponse teamsDRProduct : teamsDRProducts.getResponse()) {
			if (Objects.nonNull(teamsDRProduct.getSolutionBeans()) && !teamsDRProduct.getSolutionBeans().isEmpty()) {
				SolutionBean solutionBean = teamsDRProduct.getSolutionBeans().stream().findAny().get();
				boolean quoteToLePresent = teamsDRProduct.getSolutionBeans().stream().
						anyMatch(solBean -> Objects.nonNull(solBean.getQuoteToLeId()));

				// Construct new quotetole if quotetole id is null
				if (!quoteToLePresent) {

					// Ms License and Gsc Solutions
					QuoteToLe newQuoteToLe = constructNewQuoteToLe(quote, parentQuoteTole,
							solutionBean.getOfferingName());
					newQuoteToLe = quoteToLeRepository.save(newQuoteToLe);
					persistQuoteLeAttributes(user,newQuoteToLe);
					MstProductFamily mstProductFamily =
							GSIP_PRODUCT_NAME.equals(solutionBean.getProductName())? gscProductFamily : teamsDRProductFamily;

					// construct new quotetoleproduct family
					QuoteToLeProductFamily newQuoteToLeProductFamily = constructQuoteToLeProductFamily(mstProductFamily,
							newQuoteToLe);

					QuoteToLe finalNewQuoteToLe = newQuoteToLe;

					// Mapping the existing product solutions to new quotetoleproductfamily..
					mapExistingToNew(finalNewQuoteToLe, newQuoteToLeProductFamily, teamsDRProduct, solutionBean);

					// Deleting quote to le product family of gsc if no solutions is present.
					deleteGscQuoteToLeProductFamily(parentQuoteTole);

					// trigger create opportunity for new quotetole.
					// teamsDRSfdcService.createOpportunityInSfdc(newQuoteToLe,null,teamsDRProductFamily.getName());
				} else if (!MICROSOFT_LICENSE.equals(solutionBean.getOfferingName())) {

					// If quotetole is already present and If not microsoft license..
					List<SolutionBean> gscSolutions = teamsDRProduct.getSolutionBeans().stream()
							.filter(solutionBean1 -> GSIP_PRODUCT_NAME.equals(solutionBean1.getProductName()))
							.collect(Collectors.toList());
					if (!gscSolutions.isEmpty()) {
						// Getting the parent solution bean..
						Optional<SolutionBean> parentSolBean = teamsDRProduct.getSolutionBeans().stream()
								.filter(solutionBean1 -> solutionBean1.getOfferingName().contains(PLAN)
										|| solutionBean1.getOfferingName().equals(MEDIA_GATEWAY))
								.findAny();
						Integer parentQuoteToLeId = parentSolBean.map(SolutionBean::getQuoteToLeId).orElse(null);
						if (Objects.nonNull(parentQuoteToLeId)) {
							// to process gsc solutions in rules response
							processGscSolutionsInRulesResponse(parentQuoteTole, gscProductFamily, gscSolutions,
									parentQuoteToLeId);
						}
					}
				}
			}
		}
	}

	/**
	 * Method to process gsc solutions in rules response
	 *
	 * @param parentQuoteTole
	 * @param gscProductFamily
	 * @param gscSolutions
	 * @param parentQuoteToLeId
	 */
	private void processGscSolutionsInRulesResponse(QuoteToLe parentQuoteTole, MstProductFamily gscProductFamily,
													List<SolutionBean> gscSolutions, Integer parentQuoteToLeId) throws TclCommonException {
		Optional<QuoteToLeProductFamily> gscQtlePf = quoteToLeProductFamilyRepository
				.findByQuoteToLe(parentQuoteTole.getId()).stream().filter(quoteToLeProductFamily -> GSIP_PRODUCT_NAME
						.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
				.findAny();

		QuoteToLeProductFamily newQuoteToLePf = gscQtlePf.orElseGet(() ->
				constructQuoteToLeProductFamily(gscProductFamily, parentQuoteTole));

		List<ProductSolution> initialGscSolutions = productSolutionRepository.findByQuoteToLeProductFamily(newQuoteToLePf);

		CustomerLeDetailsBean customerLeDetailsBean = fetchCustomerLeAttributes(parentQuoteTole.getErfCusCustomerLegalEntityId(),MICROSOFT_CLOUD_SOLUTIONS);

		List<ThirdPartyServiceJob> createOptyJobs = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(parentQuoteTole.getQuote().getQuoteCode(),
						CREATE_OPTY, SFDC);

		for (SolutionBean bean : gscSolutions) {
			if (Objects.nonNull(bean.getQuoteToLeId()) && !bean.getQuoteToLeId().equals(parentQuoteToLeId)) {
				Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(bean.getQuoteToLeId());
				if (optionalQuoteToLe.isPresent()) {
					QuoteToLe childQle = optionalQuoteToLe.get();
					QuoteToLeProductFamily childQuoteToLeProductFamily = quoteToLeProductFamilyRepository
							.findByQuoteToLe(childQle.getId()).stream().findAny().get();
					// ..
					List<ProductSolution> productSolutions = productSolutionRepository
							.findByQuoteToLeProductFamily(childQuoteToLeProductFamily);
					Optional<ProductSolution> productSolution = productSolutions.stream()
							.filter(ps -> ps.getId().equals(bean.getSolutionId())).findAny();
					if (productSolution.isPresent()) {
						productSolution.get().setQuoteToLeProductFamily(newQuoteToLePf);
						productSolutionRepository.save(productSolution.get());

						List<QuoteGsc> quoteGscs = quoteGscRepository
								.findByProductSolutionAndStatus(productSolution.get(), CommonConstants.BACTIVE);
						quoteGscs.forEach(quoteGsc -> {
							quoteGsc.setQuoteToLe(parentQuoteTole);
							quoteGscRepository.save(quoteGsc);
						});

						// Creating new product service in sfdc

						if(Objects.nonNull(parentQuoteTole.getTpsSfdcOptyId())){
							if(Objects.isNull(initialGscSolutions) || initialGscSolutions.isEmpty()){
								if(createOptyJobs.size()>1)
								 teamsDRSfdcService.createProductServiceInSfdc(parentQuoteTole,productSolution.get());
							}else{
								// to update the existing sfdc product service..
								// update product will be triggered only if the solution has tpsSfdcProductName.
								//if(Objects.nonNull(parentQuoteTole.getTpsSfdcOptyId())){
								//	teamsDRSfdcService.updateProductServiceInSfdc(parentQuoteTole);
								//}
							}
						}

						//if(Objects.nonNull(productSolution.get().getTpsSfdcProductId())){
							// deleting the product service from sfdc before mapping to parent qtle.
							// teamsDRSfdcService.deleteProductServiceInSfdc(childQle,productSolution.get());
							//productSolution.get().setTpsSfdcProductId(null);
							//productSolution.get().setTpsSfdcProductName(null);
							//productSolutionRepository.save(productSolution.get());
						//}
						// Triggering update opportunity and credit check
						// if le is selected for parent quotetole..
						// To be enabled later..
//						if(Objects.nonNull(parentQuoteTole.getTpsSfdcOptyId())){
//							// Trigger credit check..
//							triggerCreditCheck(parentQuoteTole.getErfCusCustomerLegalEntityId(),parentQuoteTole,
//									customerLeDetailsBean,parentQuoteTole.getErfCusCustomerLegalEntityId());
//							teamsDRSfdcService.updateOpportunityInSfdc(parentQuoteTole);
//						}
					}

					// if only 1 solution is present in that quotetole
					// then deleting that quotetole and quotetoleproduct family
					if (productSolutions.size() <= 1) {
						quoteToLeProductFamilyRepository.delete(childQuoteToLeProductFamily);
						List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
								.findByQuoteToLe(childQle);
						quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValues);
						quoteLeCreditCheckAuditRepository.deleteAll(quoteLeCreditCheckAuditRepository.findByQuoteToLe_id(childQle.getId()));
						// to delete the opportunity in sfdc...
						teamsDRSfdcService.deleteOpportunity(childQle);
						teamsDRSfdcService.deleteIncompleteRequests(childQle);
						quoteToLeRepository.delete(childQle);
					}
				}
			}
		}

	}

	/**
	 * To construct new product solution with existing data.
	 * @param productSolution
	 * @param quoteToLeProductFamily
	 * @return
	 */
	public ProductSolution createNewSolution(ProductSolution productSolution, QuoteToLeProductFamily quoteToLeProductFamily){
		ProductSolution newProductSolution = new ProductSolution();
		newProductSolution.setTpsSfdcProductName(productSolution.getTpsSfdcProductName());
		newProductSolution.setTpsSfdcProductId(productSolution.getTpsSfdcProductId());
		newProductSolution.setSolutionCode(Utils.generateUid());
		newProductSolution.setMstProductOffering(productSolution.getMstProductOffering());
		newProductSolution.setProductProfileData(productSolution.getProductProfileData());
		newProductSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolutionRepository.save(newProductSolution);
		return newProductSolution;
	}

	/**
	 * Method to map the existing product solutions to new quotetoleproductfamily..
	 *
	 * @param finalNewQuoteToLe
	 * @param newQuoteToLeProductFamily
	 * @param teamsDRProduct
	 * @param solutionBean
	 */
	private void mapExistingToNew(QuoteToLe finalNewQuoteToLe, QuoteToLeProductFamily newQuoteToLeProductFamily,
			TeamsDRRulesResponse teamsDRProduct, SolutionBean solutionBean) {
		teamsDRProduct.getSolutionBeans().forEach(solutionBean1 -> {
			solutionBean1.setQuoteToLeId(finalNewQuoteToLe.getId());
			productSolutionRepository.findById(solutionBean1.getSolutionId()).ifPresent(productSolution -> {
				productSolution.setQuoteToLeProductFamily(newQuoteToLeProductFamily);
				productSolutionRepository.save(productSolution);
				if (Objects.isNull(newQuoteToLeProductFamily.getProductSolutions()))
					newQuoteToLeProductFamily.setProductSolutions(new HashSet<>());
				newQuoteToLeProductFamily.getProductSolutions().add(productSolution);
				quoteToLeProductFamilyRepository.save(newQuoteToLeProductFamily);
				if (MICROSOFT_CLOUD_SOLUTIONS.equals(solutionBean.getProductName())) {
					List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository
							.findByProductSolutionAndStatus(productSolution, CommonConstants.BACTIVE);
					quoteTeamsDRs.forEach(quoteTeamsDR -> {
						quoteTeamsDR.setQuoteToLe(finalNewQuoteToLe);
						quoteTeamsDR = quoteTeamsDRRepository.save(quoteTeamsDR);
					});
				} else if (GSIP_PRODUCT_NAME.equals(solutionBean.getProductName())) {
					List<QuoteGsc> quoteGscs = quoteGscRepository
							.findByProductSolutionAndStatus(productSolution, CommonConstants.BACTIVE);
					quoteGscs.forEach(quoteGsc -> {
						quoteGsc.setQuoteToLe(finalNewQuoteToLe);
						quoteGscRepository.save(quoteGsc);
					});
				}
			});
		});
	}

	/**
	 * Method to delete quote to le product family of gsc if no solutions is
	 * present.
	 *
	 * @param parentQuoteTole
	 */
	private void deleteGscQuoteToLeProductFamily(QuoteToLe parentQuoteTole) {
		quoteToLeProductFamilyRepository.findByQuoteToLe(parentQuoteTole.getId()).stream()
				.filter(quoteToLeProductFamily -> GSIP_PRODUCT_NAME
						.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
				.findAny().ifPresent(quoteToLeProductFamily -> {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if (productSolutions.isEmpty()) {
				quoteToLeProductFamilyRepository.delete(quoteToLeProductFamily);
			}
		});
	}

	/**
	 * Get teams solutions in a quote
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<ProductSolution> getTeamsSolutions(Integer quoteId) throws TclCommonException {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
		MstProductFamily mstProductFamily = getProductFamily(TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS);
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLeInAndMstProductFamily(quoteToLes, mstProductFamily);
		List<ProductSolution> productSolutions = productSolutionRepository
				.findByQuoteToLeProductFamilyIn(quoteToLeProductFamilies);
		return productSolutions;
	}

	/**
	 * Method to Delete Configuration/City based on iD
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRConfigurationDataBean deleteConfigurations(Integer quoteId, Integer quoteToLeId,
															 TeamsDRDeleteConfigRequestBean requestBean) throws TclCommonException {

		LOGGER.info("Inside Delete Configurations");
		TeamsDRConfigurationDataBean teamsDRConfigurationDataBean = new TeamsDRConfigurationDataBean();

		getQuote(quoteId);
		QuoteToLe quoteToLe = populateQuoteToLe(quoteId);
		subtractPlanAndMgPriceFromQuoteLe(quoteToLe);

		// If city id non null then deleting only that city details
		if (Objects.nonNull(requestBean.getCityId())) {
			LOGGER.info("Deleting City Details");
			Optional<QuoteDirectRoutingCity> teamsdrCityDetails = quoteDirectRoutingCityRepository
					.findById(requestBean.getCityId());
			teamsdrCityDetails.ifPresent(cityDetails -> {
				deleteSiteAndUnderLyingComponents(cityDetails);
			});
		} else {
			// else deleting the entire configuration.

			Optional<QuoteTeamsDRDetails> quoteTeamsDRDetails = quoteTeamsDRDetailsRepository
					.findById(requestBean.getConfigId());
			Optional<QuoteTeamsDR> quoteTeamsDR = quoteTeamsDRRepository
					.findByQuoteToLeIdAndServiceName(quoteToLe.getId(), MEDIA_GATEWAY);
			LOGGER.info("Deleting the entire configuration");
			if (quoteTeamsDRDetails.isPresent() && quoteTeamsDR.isPresent()) {
				Optional<QuoteDirectRouting> quoteDirectRouting = quoteDirectRoutingRepository
						.findByQuoteTeamsDRAndCountry(quoteTeamsDR.get(), quoteTeamsDRDetails.get().getCountry());
				List<QuoteDirectRoutingCity> quoteDirectRoutingCities = quoteDirectRoutingCityRepository
						.findByQuoteDirectRoutingId(quoteDirectRouting.get().getId());
				quoteDirectRoutingCities.forEach(quoteDirectRoutingCity -> {
					deleteSiteAndUnderLyingComponents(quoteDirectRoutingCity);
				});
				quoteDirectRouting.ifPresent(teamsdrDetail -> quoteDirectRoutingRepository.delete(teamsdrDetail));
			} else {
				throw new TclCommonException(ExceptionConstants.QUOTE_DIRECT_ROUTING_NOT_FOUND,
						ResponseResource.R_CODE_NOT_FOUND);
			}
		}
		TeamsDRMultiQuoteLeBean quoteLeBean = createQuoteToLeBean(quoteToLe);
		triggerPricingPlanAndMg(quoteId, quoteLeBean);
		updateQuoteToLeWithPlanAndMg(quoteToLe, quoteLeBean);
		return teamsDRConfigurationDataBean;
	}

	/**
	 * Method to delete site and underlying components
	 *
	 * @param quoteDirectRoutingCity
	 */
	private void deleteSiteAndUnderLyingComponents(QuoteDirectRoutingCity quoteDirectRoutingCity) {
		// Deleting all the media gateways.
		List<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateways = quoteDirectRoutingMgRepository
				.findByQuoteDirectRoutingCityId(quoteDirectRoutingCity.getId());
		quoteDirectRoutingMediaGateways.forEach(quoteDirectRoutingMediaGateway -> {
			deleteQuoteProductComponentAndValues(quoteDirectRoutingMediaGateway.getId(),
					TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);
			quoteDirectRoutingMgRepository.deleteAll(
					quoteDirectRoutingMgRepository.findByQuoteDirectRoutingCityId(quoteDirectRoutingCity.getId()));
		});
		deleteQuoteProductComponentAndValues(quoteDirectRoutingCity.getId(), TEAMSDR_SITE_ATTRIBUTES);
		// Deleting the site
		quoteDirectRoutingCityRepository.delete(quoteDirectRoutingCity);
	}

	/**
	 * Method to set dummy price for media gateway
	 *
	 * @param teamsDRMediaGatewayBean
	 */
	private void setDummyPriceForMg(TeamsDRMediaGatewayBean teamsDRMediaGatewayBean,
									QuoteDirectRoutingMediaGateways quoteDirectRoutingMediaGateways, QuoteToLe quoteToLe)
			throws TclCommonException {
		List<QuoteProductComponentBean> components = getComponentDetail(quoteDirectRoutingMediaGateways.getId(),
				TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);

		MstProductFamily mstProductFamily = getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);

		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		teamsDRMediaGatewayBean.setMediaGatewayComponents(components);
		components.forEach(component -> {
			component.getAttributes().forEach(attributeBean -> {
				if (ALL_CHARGES.contains(attributeBean.getName())) {
					Double dummyPrice = 4.0;
					List<QuotePrice> quotePrices = quotePriceRepository.findByReferenceNameAndReferenceId(ATTRIBUTES,
							attributeBean.getAttributeId().toString());
					if (quotePrices.isEmpty()) {
						QuotePrice quotePrice = new QuotePrice();
						quotePrice.setEffectiveNrc(dummyPrice);
						quotePrice.setEffectiveMrc(dummyPrice);
						quotePrice.setEffectiveArc(dummyPrice);

						teamsDRCumulativePricesBean.setMrc(
								teamsDRCumulativePricesBean.getMrc() + checkForNull(quotePrice.getEffectiveMrc()));
						teamsDRCumulativePricesBean.setNrc(
								teamsDRCumulativePricesBean.getNrc() + checkForNull(quotePrice.getEffectiveNrc()));
						teamsDRCumulativePricesBean.setArc(
								teamsDRCumulativePricesBean.getArc() + checkForNull(quotePrice.getEffectiveArc()));
						teamsDRCumulativePricesBean.setTcv(teamsDRCumulativePricesBean.getTcv()
								+ checkForNull(quotePrice.getEffectiveUsagePrice()));

						quotePrice.setReferenceName(ATTRIBUTES);
						quotePrice.setReferenceId(attributeBean.getAttributeId().toString());
						quotePrice.setQuoteId(quoteToLe.getQuote().getId());
						quotePrice.setMstProductFamily(mstProductFamily);
						quotePriceRepository.save(quotePrice);
					}
				}
			});
		});
	}

	/**
	 * Method to set dummy price for media gateway/license
	 *
	 * @param teamsDRMediaGatewayBean
	 */
	private List<QuoteProductComponentBean> setDummyPriceForComponents(List<QuoteProductComponentBean> components,
																	   QuoteToLe quoteToLe, String serviceName, Integer referenceId,
																	   String compName,
																	   Integer totalUsers) throws TclCommonException {
		MstProductFamily mstProductFamily = getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);

		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		components.forEach(component -> {
			component.getAttributes().forEach(attributeBean -> {
				if (ALL_CHARGES.contains(attributeBean.getName())) {
					Double dummyPrice = 0.0;
					if (MEDIA_GATEWAY.equals(serviceName)) {
						dummyPrice = 3.0;
					} else if (MICROSOFT_LICENSE.equals(serviceName)) {
						if(compName.contains("E1")){
							dummyPrice = 6.0;
						}else if(compName.contains("E3")){
							dummyPrice = 7.0;
						}else if(compName.contains("E5")){
							dummyPrice = 9.0;
						}else{
							dummyPrice = 8.0;
						}
					} else {
						// For management charge
						dummyPrice = 0.5;
					}
					List<QuotePrice> quotePrices = quotePriceRepository.findByReferenceNameAndReferenceId(ATTRIBUTES,
							attributeBean.getAttributeId().toString());
					if (quotePrices.isEmpty()) {
						QuotePrice quotePrice = new QuotePrice();
						if (NON_RECURRING.equals(attributeBean.getName())) {
							quotePrice.setMinimumNrc(dummyPrice);
							quotePrice.setEffectiveNrc(dummyPrice*totalUsers);
						} else if (RECURRING.equals(attributeBean.getName())) {
							quotePrice.setMinimumMrc(dummyPrice);
							quotePrice.setEffectiveMrc(dummyPrice*totalUsers);
						} else if (OVERAGE.equals(attributeBean.getName()) || USAGE.equals(attributeBean.getName())) {
							quotePrice.setEffectiveUsagePrice(dummyPrice);
						} else {
							// for mg..
							Double dummyRental = 0.0;
							Double amc = 0.0;
							Double monitoring = 0.0;
							if(Objects.nonNull(compName)){
								if(compName.contains("1E1")){
									dummyRental = 1000.0;
									amc = 100.0;
									monitoring = 50.0;
								}else if(compName.contains("2E1")){
									dummyRental = 2000.0;
									amc = 200.0;
									monitoring = 100.0;
								}else if(compName.contains("3E1")){
									dummyRental = 3000.0;
									amc = 300.0;
									monitoring = 150.0;
								}else if(compName.contains("4E1")){
									dummyRental = 4000.0;
									amc = 400.0;
									monitoring = 200.0;
								}else if(compName.contains("Dual")){
									dummyRental = 4000.0;
									amc = 400.0;
									monitoring = 200.0;
								}else if(compName.contains("Mediant")){
									dummyRental = 2000.0;
									amc = 200.0;
									monitoring = 100.0;
								}
							}
							if(CPE_RENTAL_CHARGES.equals(attributeBean.getName()) || CPE_OUTRIGHT_CHARGES.equals(attributeBean.getName())){
								quotePrice.setMinimumNrc(dummyRental);
								quotePrice.setEffectiveNrc(dummyRental);
							}else if(CPE_AMC_CHARGES.equals(attributeBean.getName())){
								quotePrice.setMinimumNrc(amc);
								quotePrice.setEffectiveNrc(amc);
							}else if(MANAGEMENT_AND_MONITORING_CHARGES.equals(attributeBean.getName())){
								quotePrice.setMinimumNrc(monitoring);
								quotePrice.setEffectiveNrc(monitoring);
							}
						}
						teamsDRCumulativePricesBean.setMrc(
								teamsDRCumulativePricesBean.getMrc() + checkForNull(quotePrice.getEffectiveMrc()));
						teamsDRCumulativePricesBean.setNrc(
								teamsDRCumulativePricesBean.getNrc() + checkForNull(quotePrice.getEffectiveNrc()));
						teamsDRCumulativePricesBean.setArc(
								teamsDRCumulativePricesBean.getArc() + checkForNull(quotePrice.getEffectiveArc()));
						teamsDRCumulativePricesBean.setTcv(teamsDRCumulativePricesBean.getTcv()
								+ checkForNull(quotePrice.getEffectiveUsagePrice()));

						quotePrice.setReferenceName(ATTRIBUTES);
						quotePrice.setReferenceId(attributeBean.getAttributeId().toString());
						quotePrice.setQuoteId(quoteToLe.getQuote().getId());
						quotePrice.setMstProductFamily(mstProductFamily);
						quotePriceRepository.save(quotePrice);
					} else {
						// if already values are present in quote price..
						Double finalDummyPrice = dummyPrice;
						quotePrices.forEach(quotePrice -> {
							Optional<QuoteProductComponentsAttributeValue> attributeValue =
									quoteProductComponentsAttributeValueRepository.findById(Integer.parseInt(quotePrice.getReferenceId()));
							if(attributeValue.isPresent()){
								String chargeName = attributeValue.get().getProductAttributeMaster().getName();
								if (NON_RECURRING.equals(chargeName)) {
									quotePrice.setMinimumNrc(finalDummyPrice);
									quotePrice.setEffectiveNrc(finalDummyPrice *totalUsers);
								} else if (RECURRING.equals(chargeName)) {
									quotePrice.setMinimumMrc(finalDummyPrice);
									quotePrice.setEffectiveMrc(finalDummyPrice *totalUsers);
								} else if (OVERAGE.equals(attributeBean.getName()) || USAGE.equals(attributeBean.getName())) {
									quotePrice.setEffectiveUsagePrice(finalDummyPrice);
								} else {
									// for mg..
									Double dummyRental = 0.0;
									Double amc = 0.0;
									Double monitoring = 0.0;
									if(Objects.nonNull(compName)){
										if(compName.contains("1E1")){
											dummyRental = 1000.0;
											amc = 100.0;
											monitoring = 50.0;
										}else if(compName.contains("2E1")){
											dummyRental = 2000.0;
											amc = 200.0;
											monitoring = 100.0;
										}else if(compName.contains("3E1")){
											dummyRental = 3000.0;
											amc = 300.0;
											monitoring = 150.0;
										}else if(compName.contains("4E1")){
											dummyRental = 4000.0;
											amc = 400.0;
											monitoring = 200.0;
										}else if(compName.contains("Dual")){
											dummyRental = 4000.0;
											amc = 400.0;
											monitoring = 200.0;
										}else if(compName.contains("Mediant")){
											dummyRental = 2000.0;
											amc = 200.0;
											monitoring = 100.0;
										}
									}
									if(CPE_RENTAL_CHARGES.equals(attributeBean.getName()) || CPE_OUTRIGHT_CHARGES.equals(attributeBean.getName())){
										quotePrice.setMinimumNrc(dummyRental);
										quotePrice.setEffectiveNrc(dummyRental);
									}else if(CPE_AMC_CHARGES.equals(attributeBean.getName())){
										quotePrice.setMinimumNrc(amc);
										quotePrice.setEffectiveNrc(amc);
									}else if(MANAGEMENT_AND_MONITORING_CHARGES.equals(attributeBean.getName())){
										quotePrice.setMinimumNrc(monitoring);
										quotePrice.setEffectiveNrc(monitoring);
									}
								}
								quotePriceRepository.save(quotePrice);
							}
							teamsDRCumulativePricesBean.setMrc(
									teamsDRCumulativePricesBean.getMrc() + checkForNull(quotePrice.getEffectiveMrc()));
							teamsDRCumulativePricesBean.setNrc(
									teamsDRCumulativePricesBean.getNrc() + checkForNull(quotePrice.getEffectiveNrc()));
							teamsDRCumulativePricesBean.setArc(
									teamsDRCumulativePricesBean.getArc() + checkForNull(quotePrice.getEffectiveArc()));
							teamsDRCumulativePricesBean.setTcv(teamsDRCumulativePricesBean.getTcv()
									+ checkForNull(quotePrice.getEffectiveUsagePrice()));
						});
					}
				}
			});
		});

		LOGGER.info(teamsDRCumulativePricesBean.toString());
		if (MEDIA_GATEWAY.equals(serviceName)) {
			QuoteDirectRoutingMediaGateways quoteDirectRoutingMediaGateways = quoteDirectRoutingMgRepository
					.findById(referenceId).get();
			quoteDirectRoutingMediaGateways.setMrc(teamsDRCumulativePricesBean.getMrc());
			quoteDirectRoutingMediaGateways.setNrc(teamsDRCumulativePricesBean.getNrc());
			quoteDirectRoutingMediaGateways.setArc(teamsDRCumulativePricesBean.getMrc()*12);
			quoteDirectRoutingMediaGateways.setTcv(quoteDirectRoutingMediaGateways.getArc()+quoteDirectRoutingMediaGateways.getNrc());
			quoteDirectRoutingMgRepository.save(quoteDirectRoutingMediaGateways);
		} else if (MICROSOFT_LICENSE.equals(serviceName)) {
			QuoteTeamsLicense quoteTeamsLicense = quoteTeamsLicenseRepository.findById(referenceId).get();
			quoteTeamsLicense.setMrc(teamsDRCumulativePricesBean.getMrc());
			quoteTeamsLicense.setNrc(teamsDRCumulativePricesBean.getNrc());
			quoteTeamsLicense.setArc(teamsDRCumulativePricesBean.getMrc()*12);
			quoteTeamsLicense.setTcv(quoteTeamsLicense.getArc()+quoteTeamsLicense.getNrc());
			quoteTeamsLicense = quoteTeamsLicenseRepository.save(quoteTeamsLicense);
		} else {
			QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findById(referenceId).get();
			quoteTeamsDR.setNoOfUsers(totalUsers);
			quoteTeamsDR.setMrc(teamsDRCumulativePricesBean.getMrc());
			quoteTeamsDR.setNrc(teamsDRCumulativePricesBean.getNrc());
			quoteTeamsDR.setArc(teamsDRCumulativePricesBean.getMrc()*12);
			quoteTeamsDR.setTcv(quoteTeamsDR.getArc()+quoteTeamsDR.getNrc());
			quoteTeamsDR = quoteTeamsDRRepository.save(quoteTeamsDR);
		}
		return components;
	}

	/**
	 * Method to convert quoteTeamsLicense to TeamsDRLicenseBean
	 *
	 * @param quoteTeamsLicense
	 * @return
	 */
	public TeamsDRLicenseBean toTeamsDRLicenseBean(QuoteTeamsLicense quoteTeamsLicense) {
		TeamsDRLicenseBean teamsDRLicenseBean = new TeamsDRLicenseBean();
		teamsDRLicenseBean.setId(quoteTeamsLicense.getId());
		teamsDRLicenseBean.setLicenseName(quoteTeamsLicense.getLicenseName());
		teamsDRLicenseBean.setMrc(quoteTeamsLicense.getMrc());
		teamsDRLicenseBean.setNrc(quoteTeamsLicense.getNrc());
		teamsDRLicenseBean.setArc(quoteTeamsLicense.getArc());
		teamsDRLicenseBean.setTcv(quoteTeamsLicense.getTcv());
		teamsDRLicenseBean.setLicenseContractPeriod(quoteTeamsLicense.getContractPeriod());
		teamsDRLicenseBean.setNoOfLicenses(quoteTeamsLicense.getNoOfLicenses());
		teamsDRLicenseBean.setSfdcProductName(quoteTeamsLicense.getSfdcProductName());
		try {
			teamsDRLicenseBean
					.setLicenseSKUComponents(getComponentDetail(quoteTeamsLicense.getId(), TEAMSDR_LICENSE_CHARGES));
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
		return teamsDRLicenseBean;
	}

	/**
	 * Update license price in quoteteamsdr.
	 *
	 * @param quoteTeamsLicenses
	 * @param quoteTeamsDR
	 */
	private void updateLicensePrice(List<QuoteTeamsLicense> quoteTeamsLicenses, QuoteTeamsDR quoteTeamsDR) {
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		quoteTeamsLicenses.forEach(quoteTeamsLicense -> {
			teamsDRCumulativePricesBean.setMrc(teamsDRCumulativePricesBean.getMrc() + quoteTeamsLicense.getMrc());
			teamsDRCumulativePricesBean.setNrc(teamsDRCumulativePricesBean.getNrc() + quoteTeamsLicense.getNrc());
			teamsDRCumulativePricesBean.setArc(teamsDRCumulativePricesBean.getArc() + quoteTeamsLicense.getArc());
			teamsDRCumulativePricesBean.setTcv(teamsDRCumulativePricesBean.getTcv() + quoteTeamsLicense.getTcv());
		});
		quoteTeamsDR.setMrc(checkForNull(quoteTeamsDR.getMrc()) + teamsDRCumulativePricesBean.getMrc());
		quoteTeamsDR.setNrc(checkForNull(quoteTeamsDR.getNrc()) + teamsDRCumulativePricesBean.getNrc());
		quoteTeamsDR.setArc(checkForNull(quoteTeamsDR.getMrc()) + teamsDRCumulativePricesBean.getArc());
		quoteTeamsDR.setTcv(checkForNull(quoteTeamsDR.getTcv()) + teamsDRCumulativePricesBean.getTcv());
		quoteTeamsDRRepository.save(quoteTeamsDR);
	}

	/**
	 * Method to get contact attribute details.
	 *
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public ContactAttributeInfo getContactAttributeDetails(Integer quoteLeId) throws TclCommonException {
		ContactAttributeInfo attributeInfo = null;
		try {
			attributeInfo = new ContactAttributeInfo();

			Optional<QuoteToLe> optionalQuoteLe = quoteToLeRepository.findById(quoteLeId);
			if (optionalQuoteLe.isPresent()) {
				QuoteToLe quToLe = optionalQuoteLe.get();
				if (quToLe.getQuoteLeAttributeValues() != null && !quToLe.getQuoteLeAttributeValues().isEmpty()) {
					constructAttributeInfo(quToLe, attributeInfo);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attributeInfo;
	}

	/**
	 * Method to construct attribute info.
	 *
	 * @param quoteToLe
	 * @param attributeInfo
	 */
	private void constructAttributeInfo(QuoteToLe quoteToLe, ContactAttributeInfo attributeInfo) {

		quoteToLe.getQuoteLeAttributeValues().forEach(attrval -> {
			if (attrval.getMstOmsAttribute() != null && attrval.getMstOmsAttribute().getName() != null) {
				switch (attrval.getMstOmsAttribute().getName()) {
					case LeAttributesConstants.CONTACT_ID:
						attributeInfo.setUserId(attrval.getAttributeValue());
						break;
					case LeAttributesConstants.CONTACT_NAME:
						attributeInfo.setFirstName(attrval.getAttributeValue());
						break;
					case LeAttributesConstants.CONTACT_EMAIL:
						attributeInfo.setEmailId(attrval.getAttributeValue());
						break;
					case LeAttributesConstants.DESIGNATION:
						attributeInfo.setDesignation(attrval.getAttributeValue());
						break;
					case LeAttributesConstants.CONTACT_NO:
						attributeInfo.setContactNo(attrval.getAttributeValue());
						break;
					default:
						break;
				}
			}
		});
	}

	/**
	 * Fetch details from customer MDM via queue call
	 *
	 * @param context
	 * @return
	 */
	public CustomerLeDetailsBean fetchCustomerLeAttributes(Integer customerLeId,String productName)
			throws TclCommonException {
		CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
		customerLeAttributeRequestBean.setCustomerLeId(customerLeId);
		customerLeAttributeRequestBean.setProductName(productName);
		String jsonPayload = GscUtils.toJson(customerLeAttributeRequestBean);
		String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct, jsonPayload);
		LOGGER.info("Response from customer queue :: {}",customerLeAttributes);
		return GscUtils.fromJson(customerLeAttributes, CustomerLeDetailsBean.class);
	}

	/**
	 * Construct supplier detail request bean
	 *
	 * @param supplierId
	 * @return
	 */
	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
	}

	/**
	 * Fetch service provider name
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	public String fetchServiceProviderName(TeamsDRDocumentBean document) throws TclCommonException {
		String provideName = (String) mqUtils.sendAndReceive(spQueue,
				Utils.convertObjectToJson(constructSupplierDetailsRequestBean(document.getSupplierLegalEntityId())));
		return provideName;
	}

	/**
	 * Get attribute master for billing
	 *
	 * @param attrName
	 * @return
	 */
	public MstOmsAttribute getMstAttributeMasterForBilling(String attrName) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName, (byte) 1);
		mstOmsAttribute = Objects.nonNull(mstOmsAttributes) && !mstOmsAttributes.isEmpty() ? mstOmsAttributes.get(0)
				: null;

		if (Objects.isNull(mstOmsAttributes)) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(Utils.getSource());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(attrName);
			mstOmsAttribute.setDescription(attrName);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	/**
	 * Process Account
	 *
	 * @param context
	 * @return
	 */
	private void processAccount(TeamsDRDocumentBean document, QuoteToLe quoteToLe, String providerName) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
		if (Objects.isNull(quoteLeAttributeValues) || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(providerName);
			attributeValue.setDisplayValue(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(
					LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValues.add(attributeValue);
		}
		quoteLeAttributeValues.forEach(attr -> {
			attr.setAttributeValue(providerName);
			quoteLeAttributeValueRepository.save(attr);
		});
	}

	/**
	 * This method update customer and supplier details against the quote and
	 * quoteToLe
	 *
	 * @param document
	 * @return GscDocumentBean
	 */
	@Transactional
	public TeamsDRDocumentBean createTeamsDRDocument(TeamsDRDocumentBean document) throws TclCommonException {
		Objects.requireNonNull(document);
		Quote quote = getQuote(document.getQuoteId());
		QuoteToLe quoteToLe = getQuoteToLeById(document.getQuoteLeId());
		Integer oldLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
		CustomerLeDetailsBean customerLeDetailsBean = fetchCustomerLeAttributes(document.getCustomerLegalEntityId(),document.getProductName());
		constructAndUpdateBillingInfoForSfdc(document, quoteToLe, customerLeDetailsBean);

		String providerName = fetchServiceProviderName(document);

		processAccount(document, quoteToLe, providerName);

		updateQuoteToLeAndQuote(document, quoteToLe, quote);

		// to call rule engine and segregate.
		segregateBasedOnSolutions(document.getQuoteId());

		Optional<QuoteToLe> updateQuoteToLe = quoteToLeRepository.findById(document.getQuoteLeId());

		if(updateQuoteToLe.isPresent() && Objects.nonNull(updateQuoteToLe.get().getTpsSfdcOptyId())){
			if (updateQuoteToLe.get().getQuoteToLeProductFamilies()
					.stream()
					.anyMatch(quoteToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS.equals(quoteToLeProductFamily.getMstProductFamily().getName()))){
				// to trigger credit check only if it contains teams solution.
				triggerCreditCheck(document.getCustomerLegalEntityId(),updateQuoteToLe.get(),customerLeDetailsBean,oldLegalEntityId);
			}
			// to trigger sfdc update..
			teamsDRSfdcService.updateOpportunityInSfdc(updateQuoteToLe.get(),SFDCConstants.VERBAL_AGREEMENT_STAGE);
		}
		return document;
	}

	/**
	 * Method to trigger creditcheck
	 * @param document
	 * @param quoteToLe
	 * @param customerLeDetailsBean
	 * @throws TclCommonException
	 */
	public void triggerCreditCheck(Integer customerLeId,QuoteToLe quoteToLe,
								   CustomerLeDetailsBean customerLeDetailsBean,Integer oldCustomerLegalEntityId) throws TclCommonException {
		LOGGER.info("Before triggering credit check");
		if (Objects.isNull(quoteToLe.getQuoteType()) ||
				Objects.nonNull(quoteToLe.getQuoteType()) &&
						quoteToLe.getQuoteType().equals(CommonConstants.NEW) ||
				MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			processAccount(quoteToLe, customerLeDetailsBean.getCreditCheckAccountType(),
					LeAttributesConstants.CREDIT_CHECK_ACCOUNT_TYPE);
			creditCheckService.triggerCreditCheck(customerLeId, Optional.of(quoteToLe),
					customerLeDetailsBean, oldCustomerLegalEntityId);
			LOGGER.info("Credit check status :: {}", quoteToLe.getTpsSfdcStatusCreditControl());
			LOGGER.info("Preapproved flag :: {}",
					CommonConstants.BACTIVE.equals(quoteToLe.getPreapprovedOpportunityFlag()));
		}
		LOGGER.info("After triggering credit check");
	}

	/**
	 * processAccountCuid used to process account details from customer mdm
	 *
	 * @param quoteToLe
	 * @param attrValue
	 * @param attributeName
	 */
	private void processAccount(QuoteToLe quoteToLe, String attrValue, String attributeName) {
		LOGGER.info("Inside processAccount method for attribute {}", attributeName);
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attributeName);

		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attr -> {
				attr.setAttributeValue(attrValue);
				quoteLeAttributeValueRepository.save(attr);

			});

		} else {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(attrValue);
			attributeValue.setDisplayValue(attributeName);
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);

		}
	}

	/**
	 * Save constructed billing attributes
	 *
	 * @param attribute
	 * @param quoteToLe
	 */
	private void saveConstructBillingAttribute(Attributes attribute, QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getAttributeName());
		if (Objects.nonNull(quoteLeAttributeValues) && !quoteLeAttributeValues.isEmpty()) {
			updateAttributes(attribute, quoteLeAttributeValues);
		} else {
			createAttribute(attribute, quoteToLe);
		}
	}

	/**
	 * Creates Quote Le Attribute Values
	 *
	 * @param attribute
	 * @param quoteToLe
	 */
	private void createAttribute(Attributes attribute, QuoteToLe quoteToLe) {
		QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
		attributeValue.setAttributeValue(attribute.getAttributeValue());
		attributeValue.setDisplayValue(attribute.getAttributeName());
		attributeValue.setQuoteToLe(quoteToLe);
		MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attribute.getAttributeName());
		attributeValue.setMstOmsAttribute(mstOmsAttribute);
		quoteLeAttributeValueRepository.save(attributeValue);
	}

	/**
	 * Update attributes
	 *
	 * @param attribute
	 * @param quoteLeAttributeValues
	 */
	private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues) {
		quoteLeAttributeValues.stream()
				.filter(attr -> !GscConstants.PAYMENT_CURRENCY.equalsIgnoreCase(attr.getMstOmsAttribute().getName()) &&
						!BILLING_CURRENCY.equalsIgnoreCase(attr.getMstOmsAttribute().getName()))
				.forEach(attr -> {
					if(CUSTOMER_CONTRACTING_ENTITY.equals(attr.getMstOmsAttribute().getName())){
						if(Objects.nonNull(attr.getAttributeValue()) && attr.getAttributeValue().equals("")){
							attr.setAttributeValue(attribute.getAttributeValue());
							quoteLeAttributeValueRepository.save(attr);
						}
					}else{
						attr.setAttributeValue(attribute.getAttributeValue());
						quoteLeAttributeValueRepository.save(attr);
					}
				});
	}

	/**
	 * Save quote to le attribute
	 *
	 * @param quoteToLe
	 * @param attrValue
	 * @param attributeName
	 */
	private void saveQuoteToLeAttribute(QuoteToLe quoteToLe, String attrValue, String attributeName) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attributeName);
		if (Objects.isNull(quoteLeAttributeValues) || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(attrValue);
			attributeValue.setDisplayValue(attributeName);
			attributeValue.setQuoteToLe(quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValues.add(attributeValue);
		}
		quoteLeAttributeValues.forEach(attr -> {
			attr.setAttributeValue(attrValue);
			quoteLeAttributeValueRepository.save(attr);
		});
	}

	/**
	 * Construct and update billing information for SFDC
	 * @param documentBean
	 * @param quoteToLe
	 * @param customerLeDetailsBean
	 */
	private void constructAndUpdateBillingInfoForSfdc(TeamsDRDocumentBean documentBean, QuoteToLe quoteToLe,
													  CustomerLeDetailsBean customerLeDetailsBean) {
		Optional.ofNullable(customerLeDetailsBean.getAttributes()).orElse(ImmutableList.of())
				.forEach(billAttr -> saveConstructBillingAttribute(billAttr, quoteToLe));
		saveQuoteToLeAttribute(quoteToLe, customerLeDetailsBean.getAccounCuId(), LeAttributesConstants.ACCOUNT_CUID);
		saveQuoteToLeAttribute(quoteToLe, customerLeDetailsBean.getAccountId(), LeAttributesConstants.ACCOUNT_NO18);
		/*
		 * Since Billing Contact Id saved by update attribute API of OMS,no need to save
		 * from customer queue saveQuoteToLeAttribute(context.quoteToLe,
		 * String.valueOf(context.customerLeDetailsBean.getBillingContactId()),
		 * LeAttributesConstants.BILLING_CONTACT_ID);
		 */
		saveQuoteToLeAttribute(quoteToLe, customerLeDetailsBean.getLegalEntityName(),
				LeAttributesConstants.LEGAL_ENTITY_NAME);
	}

	/**
	 * Save quote and quoteToLe with updated customer and supplier details
	 * @param documentBean
	 * @param quoteToLe
	 * @param quote
	 */
	private void updateQuoteToLeAndQuote(TeamsDRDocumentBean documentBean, QuoteToLe quoteToLe, Quote quote) {
		QuoteToLe qtle = quoteToLe;
		qtle.setErfCusCustomerLegalEntityId(documentBean.getCustomerLegalEntityId());
		qtle.setErfCusSpLegalEntityId(documentBean.getSupplierLegalEntityId());
		if (qtle.getStage().equals(QuoteStageConstants.CHECKOUT.getConstantCode())) {
			qtle.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
		}
		quoteToLeRepository.save(qtle);

		CustomerDetail customerDetail = null;
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			customerDetail = new CustomerDetail();
			Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(documentBean.getCustomerId(),
					(byte) 1);
			customerDetail.setCustomerId(customer.getId());
		} else {
			customerDetail = userInfoUtils.getCustomerByLeId(documentBean.getCustomerLegalEntityId());
		}

		if (Objects.nonNull(customerDetail) && !customerDetail.getCustomerId().equals(quote.getCustomer().getId())) {
			Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
			if (customerEntity.isPresent()) {
				Quote newQuote = quote;
				newQuote.setCustomer(customerEntity.get());
				quoteRepository.save(newQuote);
			}
		}
	}

	/**
	 * Method to get all the quote to le attibutes by id
	 *
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public Set<LegalAttributeBean> getAllAttributesByQuoteToLeId(Integer quoteToLeId) throws TclCommonException {
		Set<LegalAttributeBean> legalEntityAttributes = null;
		try {
			if (Objects.isNull(quoteToLeId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<QuoteToLe> optQuoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (optQuoteToLe.isPresent()) {
				legalEntityAttributes = constructLegalAttributes(optQuoteToLe.get());
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return legalEntityAttributes;
	}

	/**
	 * Method to construct legal attributes.
	 *
	 * @param quTle
	 * @return
	 */
	private Set<LegalAttributeBean> constructLegalAttributes(QuoteToLe quTle) {

		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		List<QuoteLeAttributeValue> attributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quTle);
		if (attributeValues != null) {

			attributeValues.stream().forEach(attrVal -> {
				LegalAttributeBean attributeBean = new LegalAttributeBean();
				attributeBean.setId(attrVal.getId());
				attributeBean.setAttributeValue(attrVal.getAttributeValue());
				attributeBean.setDisplayValue(attrVal.getDisplayValue());
				attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
				leAttributeBeans.add(attributeBean);

			});

		}
		return leAttributeBeans;
	}

	/**
	 * Method to construct mst attribute bean.
	 *
	 * @param mstOmsAttribute
	 * @return
	 */
	private MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setName(mstOmsAttribute.getName());
			mstOmsAttributeBean.setId(mstOmsAttribute.getId());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		}
		return mstOmsAttributeBean;
	}

	/**
	 * Method to validate update Request.
	 *
	 * @param request
	 */
	protected void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * Method to save list of quoteleattributes
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public Set<LegalAttributeBean> persistListOfQuoteLeAttributes(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		Set<LegalAttributeBean> legalAttributeBeans = new HashSet<>();
		try {
			List<QuoteLeAttributeValue> attributeValues = new ArrayList<>();
			LOGGER.info("Input Received {}", request);
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(request.getQuoteToLe());
			if (!optionalQuoteToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			List<AttributeDetail> attributeDetails = request.getAttributeDetails();
			for (AttributeDetail attribute : attributeDetails) {
				if (attribute.getName() != null) {
					LOGGER.info("Attribute Name {} ", attribute.getName());
					MstOmsAttribute mstOmsAttribute = null;
					List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
							.findByNameAndIsActive(attribute.getName(), (byte) 1);
					if (!mstOmsAttributeList.isEmpty()) {
						mstOmsAttribute = mstOmsAttributeList.get(0);
						LOGGER.info("Mst already there with id  {} ", mstOmsAttribute.getId());
					}
					if (mstOmsAttribute == null) {
						mstOmsAttribute = new MstOmsAttribute();
						mstOmsAttribute.setCreatedBy(user.getUsername());
						mstOmsAttribute.setCreatedTime(new Date());
						mstOmsAttribute.setIsActive((byte) 1);
						mstOmsAttribute.setName(attribute.getName());
						mstOmsAttribute.setDescription("");
						mstOmsAttributeRepository.save(mstOmsAttribute);
						LOGGER.info("Mst OMS Saved with id  {} ", mstOmsAttribute.getId());
					}

					attributeValues
							.addAll(saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute.getValue(),attribute.getName(), mstOmsAttribute));
				}

			}

			if (!attributeValues.isEmpty()) {
				if (attributeValues != null) {
					attributeValues.stream().forEach(attrVal -> {
						LegalAttributeBean attributeBean = new LegalAttributeBean();
						attributeBean.setId(attrVal.getId());
						attributeBean.setAttributeValue(attrVal.getAttributeValue());
						attributeBean.setDisplayValue(attrVal.getDisplayValue());
						attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
						legalAttributeBeans.add(attributeBean);
					});

				}

				List<QuoteLeAttributeValue> effectiveMsaDateAttribute = attributeValues.stream()
						.filter(attr -> attr.getMstOmsAttribute().getName().equals(EFFECTIVE_MSA_DATE))
						.collect(Collectors.toList());

				// To save effective msa date.
				if (!effectiveMsaDateAttribute.isEmpty()) {
					List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
							.findByNameAndIsActive(EFFECTIVE_MSA_DATE, (byte) 1);
					if (!mstOmsAttributeList.isEmpty()) {
						MstOmsAttribute effectiveDate = mstOmsAttributeList.get(0);
						StringBuilder effectiveMsaDate = new StringBuilder();
						for (QuoteLeAttributeValue quoteLeAttributeValue : effectiveMsaDateAttribute) {
							if (quoteLeAttributeValue.getAttributeValue() != null
									&& StringUtils.isNotBlank(quoteLeAttributeValue.getAttributeValue())) {
								effectiveMsaDate.append(quoteLeAttributeValue.getAttributeValue());
								break;
							}
						}

						LOGGER.info("Effective MSA Date :: {}", effectiveMsaDate);
						if (effectiveMsaDate.length() != 0) {
							for (QuoteToLe quoteToLe : quoteToLeRepository.findByQuote_Id(optionalQuoteToLe.get().getQuote().getId())) {
								saveLegalEntityAttributes(quoteToLe, effectiveMsaDate.toString(), EFFECTIVE_MSA_DATE,
										effectiveDate);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return legalAttributeBeans;
	}

	/**
	 * Method to save legalentity attributes
	 *
	 * @param quoteToLe
	 * @param attribute
	 * @param mstOmsAttribute
	 */
	private List<QuoteLeAttributeValue> saveLegalEntityAttributes(QuoteToLe quoteToLe, String value,String name,
																  MstOmsAttribute mstOmsAttribute) {
		List<QuoteLeAttributeValue> attributeValues = new ArrayList<>();
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, name);
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				LOGGER.info("Inside quote to le update");
				QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
				attrVal.setMstOmsAttribute(mstOmsAttribute);
				attrVal.setAttributeValue(value);
				attrVal.setDisplayValue(name);
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeValue = quoteLeAttributeValueRepository.save(attrVal);
				attributeValues.add(quoteLeAttributeValue);

			});
		} else {
			LOGGER.info("Inside quote to create");
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setAttributeValue(value);
			quoteLeAttributeValue.setDisplayValue(name);
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeValue = quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
			attributeValues.add(quoteLeAttributeValue);
		}
		return attributeValues;
	}

	/**
	 * Method to update quote to le stage
	 *
	 * @param quoteId
	 * @param status
	 * @param subStatus
	 * @return
	 * @throws TclCommonException
	 */
	public List<TeamsDRMultiQuoteLeBean> updateQuoteToLeStatus(Integer quoteId, String status, String subStatus)
			throws TclCommonException {
		TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean = null;
		List<TeamsDRMultiQuoteLeBean> quoteToLesToReturn = new ArrayList<>();
		try {
			if (Objects.isNull(quoteId) || (StringUtils.isEmpty(status))) {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
			if (quoteToLes.isEmpty())
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			if(status.equals("ORDER_FORM")){
				LOGGER.info("Triggering sfdc jobs...");
				teamsDRSfdcService.triggerSFDC(quoteToLes);
			}
			quoteToLes.forEach(quoteToLe -> {
				if(Objects.nonNull(status))
					quoteToLe.setStage(QuoteStageConstants.valueOf(status.toUpperCase()).toString());
				if(Objects.nonNull(subStatus))
					quoteToLe.setSubStage(QuoteSubStageConstants.valueOf(subStatus.toUpperCase()).toString());
			});
			quoteToLeRepository.saveAll(quoteToLes);
			quoteToLes.forEach(quoteToLe -> quoteToLesToReturn.add(new TeamsDRMultiQuoteLeBean(quoteToLe)));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteToLesToReturn;
	}

	/**
	 * Method to persist default QuoteLeAttributes
	 *
	 * @param user
	 * @param quoteTole
	 */
	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole) {
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_NAME, user.getFirstName());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_EMAIL, user.getEmailId());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_ID, user.getUsername());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.CONTACT_NO, user.getContactNo());
		updateLeAttribute(quoteTole, user, LeAttributesConstants.DESIGNATION, user.getDesignation());
	}

	/**
	 * Method to updateConstractInfo
	 *
	 * @param quoteTole
	 * @param user
	 */
	private void updateLeAttribute(QuoteToLe quoteTole, User user, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;

		List<MstOmsAttribute> mstOmsAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(name, (byte) 1);

		if (!mstOmsAttributesList.isEmpty()) {
			mstOmsAttribute = mstOmsAttributesList.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(user.getUsername());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(name);
			mstOmsAttribute.setDescription(value);
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		constructLegalAttribute(mstOmsAttribute, quoteTole, name, value);

	}

	/**
	 * Method to constructLegaAttribute
	 *
	 * @param mstOmsAttribute
	 * @param quoteTole
	 * @param name
	 * @param value
	 */
	private void constructLegalAttribute(MstOmsAttribute mstOmsAttribute, QuoteToLe quoteTole, String name,
										 String value) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteTole, name);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(value);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			attributeValue.setQuoteToLe(quoteTole);
			attributeValue.setDisplayValue(name);
			quoteLeAttributeValueRepository.save(attributeValue);
		} else {
			updateLeAttrbute(quoteLeAttributeValues, name, value);
		}

	}

	/**
	 * Method to updateLeAttrbute
	 *
	 * @param quoteLeAttributeValues
	 * @param name
	 * @param value
	 */
	private void updateLeAttrbute(List<QuoteLeAttributeValue> quoteLeAttributeValues, String name, String value) {
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setAttributeValue(value);
				attrVal.setDisplayValue(name);
				quoteLeAttributeValueRepository.save(attrVal);

			});
		}
	}

	/**
	 * Method to trigger pricing based on currency..
	 * @param quoteId
	 * @param quoteToLeId
	 * @param currency
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRMultiQuoteLeBean convertPricesToInputCurrency(Integer quoteId, Integer quoteToLeId, String currency)
			throws TclCommonException {
		TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean = new TeamsDRMultiQuoteLeBean();
		try {
			Quote quote = getQuote(quoteId);
			QuoteToLe quoteToLe = getQuoteToLeById(quoteToLeId);
			saveQuoteToLeAttribute(quoteToLe, currency, PAYMENT_CURRENCY);
			saveQuoteToLeAttribute(quoteToLe, currency, LeAttributesConstants.BILLING_CURRENCY);
			quoteToLe = getQuoteToLeById(quoteToLeId);
			teamsDRMultiQuoteLeBean = createQuoteToLeBean(quoteToLe);
			//updating input currency in quoteToLeBean for calculating new price
			teamsDRMultiQuoteLeBean.setCurrency(currency);
			//call price engine and convert price for the input currency
			if (!quoteToLe.getCurrencyCode().equalsIgnoreCase(currency) && (Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution()) && Objects.nonNull(
					teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices()) && teamsDRMultiQuoteLeBean
					.getTeamsDRSolution().getTeamsDRServices().stream()
					.anyMatch(service -> MEDIA_GATEWAY.equalsIgnoreCase(service.getOfferingName()) || PLAN.contains(service.getPlan())))) {
				LOGGER.info("To convert prices from {} to {}", quoteToLe.getCurrencyCode(), currency);
				subtractPlanAndMgPriceFromQuoteLe(quoteToLe);
				triggerPricingPlanAndMg(quoteId, teamsDRMultiQuoteLeBean);
				updateQuoteToLeWithPlanAndMg(quoteToLe, teamsDRMultiQuoteLeBean);
			}
			//updating license prices because of change in contract period of quoteToLe after segregation
			if (Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution()) && Objects.nonNull(
					teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices()) && teamsDRMultiQuoteLeBean
					.getTeamsDRSolution().getTeamsDRServices().stream()
					.anyMatch(service -> TeamsDRConstants.MICROSOFT_LICENSE.equalsIgnoreCase(service.getOfferingName()))) {
				subtractLicensePriceFromQuoteLe(quoteToLe);
				triggerPricingLicense(quoteId, teamsDRMultiQuoteLeBean);
				updateQuoteToLeWithLicense(quoteToLe, teamsDRMultiQuoteLeBean);
			}

			//  if voice is present and existing currency != inputCurrency
			if (!teamsDRMultiQuoteLeBean.getVoiceSolutions().isEmpty() && !quoteToLe.getCurrencyCode().equals(currency)) {
				List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
				gscMultiLEQuoteService.subtractVoicePricesInQuoteToLe(quoteGscs);
				gscMultiLEDetailService.updateCurrencyValueByCode(quoteId,quoteToLeId,currency);
				gscMultiLEQuoteService.updateQuoteToLeWithQuoteGscPrices(quoteToLe);
			}

			quoteToLe.setCurrencyCode(currency);
			quoteToLeRepository.save(quoteToLe);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return teamsDRMultiQuoteLeBean;
	}

	/**
	 * Trigger pricing for plan and media gateway
	 *
	 * @param quoteId
	 * @param quoteLeBean
	 * @throws TclCommonException
	 */
	void triggerPricingPlanAndMg(Integer quoteId, TeamsDRMultiQuoteLeBean quoteLeBean) throws TclCommonException {
		TeamsDRPricingBean request = teamsDRPricingFeasibilityService.constructPricingRequestPlanAndMg(quoteLeBean);
		TeamsDRPricingBean response = teamsDRPricingFeasibilityService
				.getTeamsDRPricingResponse(getQuote(quoteId).getQuoteCode(), quoteLeBean.getQuoteLeCode(), request,
						quoteLeBean.getCurrency());
		teamsDRPricingFeasibilityService.updatePlanAndMediaGatewayPrices(quoteId, quoteLeBean.getQuoteleId(), response);
	}

	/**
	 * Trigger pricing for plan and media gateway
	 *
	 * @param quoteId
	 * @param quoteLeBean
	 * @throws TclCommonException
	 */
	void triggerPricingLicense(Integer quoteId, TeamsDRMultiQuoteLeBean quoteLeBean) throws TclCommonException {
		TeamsDRPricingBean request = teamsDRPricingFeasibilityService.constructPricingRequestLicense(quoteLeBean);
		TeamsDRPricingBean response = teamsDRPricingFeasibilityService.getTeamsDRPricingResponse(
				getQuote(quoteId).getQuoteCode(), quoteLeBean.getQuoteLeCode(), request,
				quoteLeBean.getCurrency());
		teamsDRPricingFeasibilityService.updateLicensePrices(quoteId, quoteLeBean.getQuoteleId(), response);
	}

	/**
	 * Trigger pricing for all teams dr services
	 *
	 * @param quoteId
	 * @param quoteLeBean
	 * @throws TclCommonException
	 */
	void triggerTeamsDRPricingAllServices(Integer quoteId, TeamsDRMultiQuoteLeBean quoteLeBean)
			throws TclCommonException {
		TeamsDRPricingBean request = teamsDRPricingFeasibilityService.constructTeamsDRPricingRequest(quoteLeBean);
		TeamsDRPricingBean response = teamsDRPricingFeasibilityService.getTeamsDRPricingResponse(
				getQuote(quoteId).getQuoteCode(), quoteLeBean.getQuoteLeCode(), request, quoteLeBean.getCurrency());
		teamsDRPricingFeasibilityService.updateAllTeamsServicesPrices(quoteId, quoteLeBean.getQuoteleId(), response);
	}

	/**
	 * Get teams solutions by quote to le
	 *
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	public TeamsDRSolutionBean getTeamsSolutionsByQuoteLe(QuoteToLe quoteToLe) throws TclCommonException {
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.getId(),
						TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS);
		TeamsDRSolutionBean teamsDRSolutionBean = new TeamsDRSolutionBean();
		if (Objects.nonNull(quoteToLeProductFamily)) {
			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			teamsDRSolutionBean.setTeamsDRServices(new ArrayList<>());
			TeamsDRQuoteDataBean teamsDRQuoteDataBean = new TeamsDRQuoteDataBean();
			teamsDRQuoteDataBean.setQuoteId(quoteToLe.getQuote().getId());
			productSolutions.forEach(productSolution -> {
				teamsDRSolutionBean.getTeamsDRServices()
						.add(createProductSolutionBean(teamsDRQuoteDataBean, productSolution));
			});
		}
		return teamsDRSolutionBean;
	}

	/**
	 * Create quote to le Bean
	 *
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	private TeamsDRMultiQuoteLeBean createQuoteToLeBean(QuoteToLe quoteToLe) throws TclCommonException {
		TeamsDRSolutionBean teamsSolutions = getTeamsSolutionsByQuoteLe(quoteToLe);
		List<GscMultipleLESolutionBean> gscMultipleLESolutions = gscMultiLEQuoteService
				.getGscSolutionsByQuoteLe(quoteToLe);
		TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean = new TeamsDRMultiQuoteLeBean(quoteToLe);
		teamsDRMultiQuoteLeBean.setTeamsDRSolution(teamsSolutions);
		teamsDRMultiQuoteLeBean.setVoiceSolutions(gscMultipleLESolutions);
		return teamsDRMultiQuoteLeBean;
	}

	/**
	 * Get quote data by QuoteToLe
	 *
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	private TeamsDRQuoteDataBean getQuoteDataByQuoteLe(QuoteToLe quoteToLe) throws TclCommonException {
		LOGGER.info("Inside getQuoteData()");
		TeamsDRQuoteDataBean teamsDRQuoteDataBean = new TeamsDRQuoteDataBean();
		teamsDRQuoteDataBean.setAccessType(GscConstants.PUBLIC_IP);
		Quote quote = quoteToLe.getQuote();
		if (Objects.nonNull(quote)) {
			teamsDRQuoteDataBean.setQuoteId(quote.getId());
			teamsDRQuoteDataBean.setQuoteCode(quote.getQuoteCode());
			teamsDRQuoteDataBean.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		} else {
			teamsDRQuoteDataBean.setEngagementOptyId(quote.getEngagementOptyId());
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return teamsDRQuoteDataBean;
	}

	/**
	 * Get quote details by quote to le
	 *
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public TeamsDRQuoteDataBean getQuoteDetailsByQuoteToLe(Integer quoteLeId) throws TclCommonException {
		QuoteToLe quoteToLe = getQuoteToLeById(quoteLeId);
		TeamsDRQuoteDataBean teamsDRQuoteDataBean = new TeamsDRQuoteDataBean();
		teamsDRQuoteDataBean = getQuoteDataByQuoteLe(quoteToLe);
		teamsDRQuoteDataBean.setQuoteToLes(new ArrayList<>());
		teamsDRQuoteDataBean.getQuoteToLes().add(createQuoteToLeBean(quoteToLe));
		return teamsDRQuoteDataBean;
	}

	/**
	 * Method to share quote via email
	 *
	 * @param email
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public ServiceResponse shareQuoteViaEmail(String email, Integer quoteId, Integer quoteToLeId,
											  HttpServletResponse httpServletResponse) throws TclCommonException {
		Quote quote = getQuote(quoteId);
		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (Objects.isNull(email) || !Utils.isValidEmail(email)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String quoteHtml = teamsDRPdfService.processQuoteHtml(quoteId, quoteToLeId, httpServletResponse);
			String fileName = "Quote-" + quote.getQuoteCode() + "-" + quoteToLeId + ".pdf";
			LOGGER.info("File Name :: {}", fileName);
			notificationService.processShareQuoteNotification(email,
					java.util.Base64.getEncoder().encodeToString(quoteHtml.getBytes()),
					userInfoUtils.getUserFullName(),
					fileName, MICROSOFT_CLOUD_SOLUTIONS);
			fileUploadResponse.setFileName(fileName);
			fileUploadResponse.setStatus(Status.SUCCESS);
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * Create and save order to le attribute
	 *
	 * @param orderToLe
	 * @param quoteToLe
	 * @param quoteLeAttributeValues
	 * @return
	 */
	private Set<OrdersLeAttributeValue> createAndSaveOrderToLeAttribute(OrderToLe orderToLe, QuoteToLe quoteToLe,
			List<QuoteLeAttributeValue> quoteLeAttributeValues) {
		Set<OrdersLeAttributeValue> orderToLeAttributeValue = new HashSet<>();
		if (Objects.nonNull(quoteLeAttributeValues) && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.stream().filter(
					quoteLeAttributeValue -> quoteLeAttributeValue.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.forEach(quoteLeAttributeValue -> {
						OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
						ordersLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
						ordersLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
						ordersLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
						ordersLeAttributeValue.setOrderToLe(orderToLe);
						ordersLeAttributeValue = ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
						orderToLeAttributeValue.add(ordersLeAttributeValue);
					});
		}

		return orderToLeAttributeValue;
	}

	/**
	 * Create and save order teams dr details
	 *
	 * @param orderTeamsDR
	 * @param quoteTeamsDR
	 * @return
	 */
	private List<OrderTeamsDRDetails> createAndSaveOrderTeamsDRDetails(OrderTeamsDR orderTeamsDR,
			QuoteTeamsDR quoteTeamsDR) {
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		List<OrderTeamsDRDetails> orderTeamsDRDetails = new ArrayList<>();
		List<QuoteTeamsDRDetails> quoteTeamsDRDetails = quoteTeamsDRDetailsRepository.findByQuoteTeamsDR(quoteTeamsDR);
		if (!quoteTeamsDRDetails.isEmpty()) {
			quoteTeamsDRDetails.stream().forEach(quoteTeamsDRDetail -> {
				OrderTeamsDRDetails orderTeamsDRDetail = new OrderTeamsDRDetails();
				orderTeamsDRDetail.setArc(quoteTeamsDRDetail.getArc());
				orderTeamsDRDetail.setCountry(quoteTeamsDRDetail.getCountry());
				orderTeamsDRDetail.setCreatedBy(quoteTeamsDRDetail.getCreatedBy());
				orderTeamsDRDetail.setCreatedTime(quoteTeamsDRDetail.getCreatedTime());
				orderTeamsDRDetail.setMrc(quoteTeamsDRDetail.getMrc());
				orderTeamsDRDetail.setNoOfCommonAreaDevices(quoteTeamsDRDetail.getNoOfCommonAreaDevices());
				orderTeamsDRDetail.setNoOfNamedUsers(quoteTeamsDRDetail.getNoOfNamedusers());
				orderTeamsDRDetail.setNrc(quoteTeamsDRDetail.getNrc());
				orderTeamsDRDetail.setTcv(quoteTeamsDRDetail.getTcv());
				orderTeamsDRDetail.setTotalUsers(quoteTeamsDRDetail.getTotalUsers());
				orderTeamsDRDetail.setOrderTeamsDR(orderTeamsDR);

				orderTeamsDRDetail = orderTeamsDRDetailsRepository.save(orderTeamsDRDetail);
				orderTeamsDRDetails.add(orderTeamsDRDetail);
				orderProductComponentBeans.addAll(createAndSaveOrderProductComponent(orderTeamsDRDetail.getId(),
						quoteTeamsDRDetail.getId(), TEAMSDR_SERVICE_ATTRIBUTES).stream()
								.map(OrderProductComponentBean::fromOrderProductComponent)
								.collect(Collectors.toList()));
			});
		}
		orderProductComponentBeans.addAll(createAndSaveOrderProductComponent(orderTeamsDR.getId(), quoteTeamsDR.getId(),
				TEAMSDR_CONFIG_ATTRIBUTES).stream().map(OrderProductComponentBean::fromOrderProductComponent)
						.collect(Collectors.toList()));
		return orderTeamsDRDetails;
	}

	/**
	 * Create and save order product attribute values
	 *
	 * @param orderProductComponent
	 * @param quoteProductComponent
	 * @return
	 */
	private Set<OrderProductComponentsAttributeValue> createAndSaveOrderProductAttributeValues(
			OrderProductComponent orderProductComponent, QuoteProductComponent quoteProductComponent) {
		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent(quoteProductComponent);
		if (!quoteProductComponentsAttributeValues.isEmpty()) {
			quoteProductComponentsAttributeValues.stream()
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
						orderProductComponentsAttributeValue
								.setIsAdditionalParam(quoteProductComponentsAttributeValue.getIsAdditionalParam());
						orderProductComponentsAttributeValue = orderProductComponentsAttributeValueRepository
								.save(orderProductComponentsAttributeValue);
						if (Objects.nonNull(quoteProductComponentsAttributeValue.getIsAdditionalParam())) {
							Optional<AdditionalServiceParams> additionalServiceParams =
									additionalServiceParamRepository.findById(
											Integer.valueOf(quoteProductComponentsAttributeValue.getAttributeValues()));
							if (additionalServiceParams.isPresent()) {
								orderProductComponentsAttributeValue.setAttributeValues(String.valueOf(teamsDRPdfService
										.constructAdditionalServiceParams(orderProductComponentsAttributeValue.getId(),
												additionalServiceParams.get().getReferenceType(), null,
												additionalServiceParams.get().getAttribute(),
												additionalServiceParams.get().getValue()).getId()));
							}
						}
						List<QuotePrice> quotePrices = quotePriceRepository.findByReferenceNameAndReferenceId(ATTRIBUTES,
								quoteProductComponentsAttributeValue.getId().toString());
						OrderProductComponentsAttributeValue finalOPAV = orderProductComponentsAttributeValue;
						quotePrices.forEach(quotePrice -> {
							OrderPrice orderPrice = createOrderPrice(quotePrice, String.valueOf(finalOPAV.getId()));
						});
						orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);
					});
		}

		return orderProductComponentsAttributeValues;
	}

	/**
	 * Create order price
	 *
	 * @param quotePrice
	 * @return
	 */
	OrderPrice createOrderPrice(QuotePrice quotePrice, String referenceId) {
		OrderPrice orderPrice = new OrderPrice();
		orderPrice.setEffectiveUsagePrice(quotePrice.getEffectiveUsagePrice());
		orderPrice.setEffectiveNrc(quotePrice.getEffectiveNrc());
		orderPrice.setEffectiveMrc(quotePrice.getEffectiveMrc());
		orderPrice.setEffectiveArc(quotePrice.getEffectiveArc());
		orderPrice.setComputedNrc(quotePrice.getComputedNrc());
		orderPrice.setComputedMrc(quotePrice.getComputedMrc());
		orderPrice.setCatalogNrc(quotePrice.getCatalogNrc());
		orderPrice.setCatalogMrc(quotePrice.getCatalogMrc());
		orderPrice.setMinimumArc(quotePrice.getMinimumArc());
		orderPrice.setMinimumNrc(quotePrice.getMinimumNrc());
		orderPrice.setMinimumMrc(quotePrice.getMinimumMrc());
		orderPrice.setDiscountInPercent(quotePrice.getDiscountInPercent());
		orderPrice.setReferenceId(referenceId);
		orderPrice.setReferenceName(quotePrice.getReferenceName());
		orderPrice.setMstProductFamily(quotePrice.getMstProductFamily());
		orderPrice.setVersion(1);
		orderPrice.setQuoteId(quotePrice.getQuoteId());
		return orderPriceRepository.save(orderPrice);
	}

	/**
	 * Create and save order product component
	 *
	 * @param orderReferenceId
	 * @param quoteReferenceId
	 * @param referenceName
	 * @return
	 */
	private Set<OrderProductComponent> createAndSaveOrderProductComponent(Integer orderReferenceId,
			Integer quoteReferenceId, String referenceName) {
		Set<OrderProductComponent> orderProductComponents = new HashSet<>();
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductFamily_NameAndReferenceName(quoteReferenceId, MICROSOFT_CLOUD_SOLUTIONS,
						referenceName);
		if (!quoteProductComponents.isEmpty()) {
			quoteProductComponents.stream().forEach(quoteProductComponent -> {
				OrderProductComponent orderProductComponent = new OrderProductComponent();
				orderProductComponent.setReferenceId(orderReferenceId);
				orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
				orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
				orderProductComponent.setType(quoteProductComponent.getType());
				orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
				orderProductComponent = orderProductComponentRepository.save(orderProductComponent);
				orderProductComponent.setOrderProductComponentsAttributeValues(
						createAndSaveOrderProductAttributeValues(orderProductComponent, quoteProductComponent));
				orderProductComponents.add(orderProductComponent);
			});
		}

		return orderProductComponents;
	}

	/**
	 * Create and save order teams licenses
	 *
	 * @param orderTeamsDR
	 * @param quoteTeamsDR
	 * @return
	 */
	private List<OrderTeamsLicense> createAndSaveOrderTeamsLicenses(OrderTeamsDR orderTeamsDR,
			QuoteTeamsDR quoteTeamsDR) {
		List<OrderTeamsLicense> orderTeamsLicenses = new ArrayList<>();
		List<QuoteTeamsLicense> quoteTeamsLicenses = quoteTeamsLicenseRepository.findByQuoteTeamsDR(quoteTeamsDR);
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		if (!quoteTeamsLicenses.isEmpty()) {
			quoteTeamsLicenses.stream().forEach(quoteTeamsLicense -> {
				OrderTeamsLicense orderTeamsLicense = new OrderTeamsLicense();
				orderTeamsLicense.setArc(quoteTeamsLicense.getArc());
				orderTeamsLicense.setAgreementType(quoteTeamsLicense.getAgreementType());
				orderTeamsLicense.setCreatedTime(quoteTeamsLicense.getCreatedTime());
				orderTeamsLicense.setLicenseName(quoteTeamsLicense.getLicenseName());
				orderTeamsLicense.setMrc(quoteTeamsLicense.getMrc());
				orderTeamsLicense.setContractPeriod(quoteTeamsLicense.getContractPeriod());
				orderTeamsLicense.setNrc(quoteTeamsLicense.getNrc());
				orderTeamsLicense.setTcv(quoteTeamsLicense.getTcv());
				orderTeamsLicense.setProvider(quoteTeamsLicense.getProvider());
				orderTeamsLicense.setNoOfLicenses(quoteTeamsLicense.getNoOfLicenses());
				orderTeamsLicense.setSfdcProductName(quoteTeamsLicense.getSfdcProductName());
				orderTeamsLicense.setOrderVersion(quoteTeamsLicense.getQuoteVersion());
				orderTeamsLicense.setOrderTeamsDR(orderTeamsDR);

				orderTeamsLicense = orderTeamsLicenseRepository.save(orderTeamsLicense);
				orderProductComponentBeans.addAll(createAndSaveOrderProductComponent(orderTeamsLicense.getId(),
						quoteTeamsLicense.getId(), TEAMSDR_LICENSE_CHARGES).stream()
								.map(OrderProductComponentBean::fromOrderProductComponent)
								.collect(Collectors.toList()));
				orderTeamsLicense = orderTeamsLicenseRepository.save(orderTeamsLicense);
				orderTeamsLicenses.add(orderTeamsLicense);
			});
			orderProductComponentBeans.addAll(createAndSaveOrderProductComponent(orderTeamsDR.getId(),
					quoteTeamsDR.getId(), TEAMSDR_LICENSE_ATTRIBUTES).stream()
							.map(OrderProductComponentBean::fromOrderProductComponent).collect(Collectors.toList()));
		}
		return orderTeamsLicenses;
	}

	/**
	 * Create and save order direct routing
	 *
	 * @param orderTeamsDR
	 * @param quoteTeamsDR
	 * @return
	 */
	private List<OrderDirectRouting> createAndSaveOrderDirectRoutings(OrderTeamsDR orderTeamsDR,
			QuoteTeamsDR quoteTeamsDR) {
		List<OrderDirectRouting> orderDirectRoutings = new ArrayList<>();
		List<QuoteDirectRouting> quoteDirectRoutings = quoteDirectRoutingRepository
				.findByQuoteTeamsDR(quoteTeamsDR);
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		if (!quoteDirectRoutings.isEmpty()) {
			quoteDirectRoutings.stream().forEach(quoteDirectRouting -> {
				OrderDirectRouting orderDirectRouting = new OrderDirectRouting();
				orderDirectRouting.setCountry(quoteDirectRouting.getCountry());
				orderDirectRouting.setCreatedTime(quoteDirectRouting.getCreatedTime());
				orderDirectRouting.setMrc(quoteDirectRouting.getMrc());
				orderDirectRouting.setNrc(quoteDirectRouting.getNrc());
				orderDirectRouting.setArc(quoteDirectRouting.getArc());
				orderDirectRouting.setTcv(quoteDirectRouting.getTcv());
				orderDirectRouting.setOrderVersion(quoteDirectRouting.getQuoteVersion());
				orderDirectRouting.setOrderTeamsDR(orderTeamsDR);
				orderDirectRouting = orderDirectRoutingRepository.save(orderDirectRouting);
				orderDirectRouting.setOrderDRCities(createAndSaveOrderDRCities(orderDirectRouting, quoteDirectRouting));
				orderDirectRoutings.add(orderDirectRouting);
			});
			orderDirectRoutingRepository.saveAll(orderDirectRoutings);
		}
		return orderDirectRoutings;
	}

	/**
	 * Create and save order cities
	 *
	 * @param orderDirectRouting
	 * @param quoteDirectRouting
	 * @return
	 */
	private List<OrderDirectRoutingCity> createAndSaveOrderDRCities(OrderDirectRouting orderDirectRouting,
			QuoteDirectRouting quoteDirectRouting) {
		List<QuoteDirectRoutingCity> quoteDirectRoutingCities = quoteDirectRoutingCityRepository
				.findByQuoteDirectRoutingId(quoteDirectRouting.getId());
		List<OrderDirectRoutingCity> orderDRCities = new ArrayList<>();
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		quoteDirectRoutingCities.forEach(quoteDirectRoutingCity -> {
			OrderDirectRoutingCity orderDirectRoutingCity = new OrderDirectRoutingCity();
			orderDirectRoutingCity.setName(quoteDirectRoutingCity.getName());
			orderDirectRoutingCity.setMediaGatewayType(quoteDirectRoutingCity.getMediaGatewayType());
			orderDirectRoutingCity.setMrc(quoteDirectRoutingCity.getMrc());
			orderDirectRoutingCity.setNrc(quoteDirectRoutingCity.getNrc());
			orderDirectRoutingCity.setArc(quoteDirectRoutingCity.getArc());
			orderDirectRoutingCity.setOrderDirectRouting(orderDirectRouting);
			orderDirectRoutingCity.setCreatedTime(quoteDirectRoutingCity.getCreatedTime());
			orderDirectRoutingCity.setOrderVersion(quoteDirectRoutingCity.getQuoteVersion());
			orderDirectRoutingCityRepository.save(orderDirectRoutingCity);
			orderDirectRoutingCity.setOrderDirectRoutingMediagateways(
					createAndSaveOrderMediaGateways(orderDirectRoutingCity, quoteDirectRoutingCity));
			orderDirectRoutingCityRepository.save(orderDirectRoutingCity);
			orderProductComponentBeans.addAll(createAndSaveOrderProductComponent(orderDirectRoutingCity.getId(),
					quoteDirectRoutingCity.getId(), TEAMSDR_SITE_ATTRIBUTES).stream()
							.map(OrderProductComponentBean::fromOrderProductComponent).collect(Collectors.toList()));
			orderDRCities.add(orderDirectRoutingCity);
		});
		return orderDRCities;
	}

	/**
	 * Create and save order media gateways
	 *
	 * @param orderDirectRoutingCity
	 * @param quoteDirectRoutingCity
	 * @return
	 */
	private List<OrderDirectRoutingMediaGateways> createAndSaveOrderMediaGateways(
			OrderDirectRoutingCity orderDirectRoutingCity, QuoteDirectRoutingCity quoteDirectRoutingCity) {
		List<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateways = quoteDirectRoutingMgRepository
				.findByQuoteDirectRoutingCityId(quoteDirectRoutingCity.getId());
		List<OrderDirectRoutingMediaGateways> orderDirectRoutingMediaGateways = new ArrayList<>();
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		quoteDirectRoutingMediaGateways.forEach(quoteDRMediaGateway -> {
			OrderDirectRoutingMediaGateways orderDRMediaGateway = new OrderDirectRoutingMediaGateways();
			orderDRMediaGateway.setName(quoteDRMediaGateway.getName());
			orderDRMediaGateway.setQuantity(quoteDRMediaGateway.getQuantity());
			orderDRMediaGateway.setMrc(quoteDRMediaGateway.getMrc());
			orderDRMediaGateway.setNrc(quoteDRMediaGateway.getNrc());
			orderDRMediaGateway.setArc(quoteDRMediaGateway.getArc());
			orderDRMediaGateway.setOrderDirectRoutingCity(orderDirectRoutingCity);
			orderDRMediaGateway.setCreatedTime(quoteDRMediaGateway.getCreatedTime());
			orderDRMediaGateway.setOrderVersion(quoteDRMediaGateway.getQuoteVersion());
			orderDirectRoutingMgRepository.save(orderDRMediaGateway);
			orderProductComponentBeans.addAll(createAndSaveOrderProductComponent(orderDRMediaGateway.getId(),
					quoteDRMediaGateway.getId(), TEAMSDR_MEDIAGATEWAY_ATTRIBUTES).stream()
							.map(OrderProductComponentBean::fromOrderProductComponent).collect(Collectors.toList()));
			orderDirectRoutingMgRepository.save(orderDRMediaGateway);
			orderDirectRoutingMediaGateways.add(orderDRMediaGateway);
		});
		return orderDirectRoutingMediaGateways;
	}

	/**
	 * Create and save order teams DR
	 *
	 * @param orderToLe
	 * @param orderProductSolution
	 * @param quoteToLe
	 * @param quoteProductSolution
	 * @param quoteTeamsDRs
	 * @return
	 */
	private Set<OrderTeamsDR> createAndSaveOrderTeamsDR(OrderToLe orderToLe, OrderProductSolution orderProductSolution,
			QuoteToLe quoteToLe, ProductSolution quoteProductSolution, List<QuoteTeamsDR> quoteTeamsDRs) {
		Set<OrderTeamsDR> orderTeamsDRs = new HashSet<>();
		if (!quoteTeamsDRs.isEmpty()) {
			quoteTeamsDRs.stream().filter(quoteTeamsDR -> quoteTeamsDR.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.filter(quoteTeamsDR -> quoteTeamsDR.getProductSolution().getId()
							.equals(quoteProductSolution.getId()))
					.forEach(quoteTeamsDR -> {
						OrderTeamsDR orderTeamsDR = new OrderTeamsDR();
						orderTeamsDR.setIsConfig(quoteTeamsDR.getIsConfig());
						orderTeamsDR.setArc(quoteTeamsDR.getArc());
						orderTeamsDR.setCreatedBy(quoteTeamsDR.getCreatedBy());
						orderTeamsDR.setCreatedTime(quoteTeamsDR.getCreatedTime());
						orderTeamsDR.setProfileName(quoteTeamsDR.getProfileName());
						orderTeamsDR.setMrc(quoteTeamsDR.getMrc());
						orderTeamsDR.setNrc(quoteTeamsDR.getNrc());
						orderTeamsDR.setServiceName(quoteTeamsDR.getServiceName());
						orderTeamsDR.setStatus(quoteTeamsDR.getStatus());
						orderTeamsDR.setTcv(quoteTeamsDR.getTcv());
						orderTeamsDR.setNoOfUsers(quoteTeamsDR.getNoOfUsers());
						orderTeamsDR.setOrderVersion(quoteTeamsDR.getQuoteVersion());
						orderTeamsDR.setOrderToLe(orderToLe);
						orderTeamsDR.setOrderProductSolution(orderProductSolution);
						orderTeamsDR = orderTeamsDRRepository.save(orderTeamsDR);
						if (Objects.isNull(quoteTeamsDR.getServiceName())
								&& Objects.nonNull(quoteTeamsDR.getProfileName())
								&& quoteTeamsDR.getProfileName().contains(PLAN))
							orderTeamsDR.setOrderTeamsDRDetails(
									createAndSaveOrderTeamsDRDetails(orderTeamsDR, quoteTeamsDR));
						else if (TeamsDRConstants.MICROSOFT_LICENSE.equalsIgnoreCase(quoteTeamsDR.getServiceName()))
							orderTeamsDR
									.setOrderTeamsLicenses(createAndSaveOrderTeamsLicenses(orderTeamsDR, quoteTeamsDR));
						else if (MEDIA_GATEWAY.equalsIgnoreCase(quoteTeamsDR.getServiceName()))
							orderTeamsDR.setOrderDirectRoutings(
									createAndSaveOrderDirectRoutings(orderTeamsDR, quoteTeamsDR));

						// For saving commercials for underlying components of custom plan..
						if(Objects.nonNull(quoteTeamsDR.getProfileName()) && CUSTOM_PLAN.equals(quoteTeamsDR.getProfileName()) &&
                                Objects.nonNull(quoteTeamsDR.getServiceName())){
							createAndSaveOrderProductComponent(orderTeamsDR.getId(),quoteTeamsDR.getId(),TEAMSDR_CONFIG_ATTRIBUTES);
						}
						orderTeamsDRs.add(orderTeamsDR);
					});
			orderTeamsDRRepository.saveAll(orderTeamsDRs);
		}
		return orderTeamsDRs;
	}

	/**
	 * Create and save order product solution
	 *
	 * @param teamsDROrderDataBean
	 * @param orderToLeProductFamily
	 * @param orderToLe
	 * @param quoteToLeProductFamily
	 * @return
	 */
	private Set<OrderProductSolution> createAndSaveOrderProductSolution(TeamsDROrderDataBean teamsDROrderDataBean,
			OrderToLeProductFamily orderToLeProductFamily, OrderToLe orderToLe,
			QuoteToLeProductFamily quoteToLeProductFamily) {
		List<TeamsDROrderServicesBean> teamsDROrderServices = new ArrayList<>();
		List<ProductSolution> quoteProductSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		Set<OrderProductSolution> orderProductSolutions = new HashSet<>();
		List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository
				.findByQuoteToLeId(quoteToLeProductFamily.getQuoteToLe().getId());
		if (!(quoteProductSolutions.isEmpty())) {
			quoteProductSolutions.stream().forEach(quoteProductSolution -> {
				OrderProductSolution orderProductSolution = new OrderProductSolution();
				if (quoteProductSolution.getMstProductOffering() != null) {
					orderProductSolution.setMstProductOffering(quoteProductSolution.getMstProductOffering());
				}
				orderProductSolution.setOrderToLeProductFamily(orderToLeProductFamily);
				orderProductSolution.setSolutionCode(quoteProductSolution.getSolutionCode());
				orderProductSolution.setProductProfileData(quoteProductSolution.getProductProfileData());

				orderProductSolution = orderProductSolutionRepository.save(orderProductSolution);

				createAndSaveOrderTeamsDR(orderToLe, orderProductSolution,
						quoteToLeProductFamily.getQuoteToLe(), quoteProductSolution, quoteTeamsDRs);

				Map<String, Object> orderProductProfileData = Utils.fromJson(
						orderProductSolution.getProductProfileData(), new TypeReference<Map<String, Object>>() {
						});
				orderProductSolutions.add(orderProductSolution);
			});

		}

//		teamsDROrderDataBean.setAccessType(accessType);
		return orderProductSolutions;

	}

	/**
	 * Create and save order product family
	 *
	 * @param orderToLe
	 * @param quoteToLe
	 * @param teamsDRProductFamily
	 * @param teamsDROrderDataBean
	 * @return
	 */
	private Set<OrderToLeProductFamily> createAndSaveOrderProductFamily(OrderToLe orderToLe, QuoteToLe quoteToLe,
			MstProductFamily teamsDRProductFamily, TeamsDROrderDataBean teamsDROrderDataBean) {
		Set<OrderToLeProductFamily> orderToLeProductFamilies = new HashSet<>();
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, teamsDRProductFamily);
		if (Objects.nonNull(quoteToLeProductFamily)) {
			OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
			orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
			orderToLeProductFamily.setOrderToLe(orderToLe);
			orderToLeProductFamily = orderToLeProductFamilyRepository.save(orderToLeProductFamily);
			orderToLeProductFamily.setOrderProductSolutions(createAndSaveOrderProductSolution(teamsDROrderDataBean,
					orderToLeProductFamily, orderToLe, quoteToLeProductFamily));
			teamsDROrderDataBean.setProductFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
			orderToLeProductFamilies.add(orderToLeProductFamily);
//						if (!userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
//						}

		}
		return orderToLeProductFamilies;

	}

	/**
	 * Create and save order to le
	 *
	 * @param order
	 * @param quoteToLes
	 * @param teamsDROrderDataBean
	 */
	private void createAndSaveOrderToLe(Order order, List<QuoteToLe> quoteToLes,
			TeamsDROrderDataBean teamsDROrderDataBean) throws TclCommonException {
		Set<OrderToLe> orderToLes = new HashSet<>();
		MstProductFamily teamsDRProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(MICROSOFT_CLOUD_SOLUTIONS, CommonConstants.BACTIVE);
		Optional<OrderToLeProductFamily> teamsDRLeProductFamily = orderToLeRepository.findByOrder(order).stream()
				.findFirst().map(orderToLe -> {
					return orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(orderToLe,
							teamsDRProductFamily);
				});
		teamsDROrderDataBean.setOrderToLes(new ArrayList<>());
		if (!teamsDRLeProductFamily.isPresent()) {
			quoteToLes.forEach(quoteToLe -> {
				OrderToLe orderToLe = new OrderToLe();
				orderToLe.setOrderLeCode(quoteToLe.getQuoteLeCode());
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
				orderToLe.setOrder(order);
				orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
				orderToLe.setSourceSystem(quoteToLe.getSourceSystem());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
				orderToLe.setClassification(quoteToLe.getClassification());
				orderToLe.setIsWholesale(quoteToLe.getIsWholesale());
				orderToLe = orderToLeRepository.save(orderToLe);
				teamsDROrderDataBean.getOrderToLes().add(new TeamsDRMultiOrderLeBean(orderToLe));
				List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
						.findByQuoteToLe_Id(quoteToLe.getId());
				orderToLe.setOrdersLeAttributeValues(
						createAndSaveOrderToLeAttribute(orderToLe, quoteToLe, quoteLeAttributeValues));
				orderToLe.setOrderToLeProductFamilies(createAndSaveOrderProductFamily(orderToLe, quoteToLe,
						teamsDRProductFamily, teamsDROrderDataBean));
				orderToLes.add(orderToLe);
			});
			teamsDROrderService.getProductSolutions(teamsDROrderDataBean, false, null);
		}
	}

	/**
	 * Get order by quote or create new order
	 *
	 * @param quote
	 * @return
	 */
	private Order getOrder(Quote quote) {
		Order order = Optional.ofNullable(orderRepository.findByQuoteAndStatus(quote, GscConstants.STATUS_ACTIVE))
				.orElseGet(() -> {
					Order newOrder = new Order();
					newOrder.setCreatedBy(quote.getCreatedBy());
					newOrder.setCreatedTime(new Date());
					newOrder.setStatus(quote.getStatus());
					newOrder.setTermInMonths(quote.getTermInMonths());
					newOrder.setCustomer(quote.getCustomer());
					newOrder.setEffectiveDate(quote.getEffectiveDate());
					newOrder.setStatus(quote.getStatus());
					newOrder.setQuote(quote);
					newOrder.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
					newOrder.setStartDate(new Date());
					newOrder.setStatus(quote.getStatus());
					newOrder.setOrderCode(quote.getQuoteCode());
					newOrder.setQuoteCreatedBy(quote.getCreatedBy());
					newOrder.setEngagementOptyId(quote.getEngagementOptyId());
					return newOrder;
				});
		return order;
	}

	/**
	 * Save new order
	 *
	 * @param order
	 * @return
	 */
	private TeamsDROrderDataBean saveOrder(Order order) {
		if (Objects.isNull(order.getId())) {
			order = orderRepository.save(order);
		}
		TeamsDROrderDataBean teamsDROrderDataBean = new TeamsDROrderDataBean();
		teamsDROrderDataBean.setOrderId(order.getId());
		teamsDROrderDataBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
		teamsDROrderDataBean.setQuoteId(order.getQuote().getId());
		teamsDROrderDataBean.setOrderCode(order.getOrderCode());
		return teamsDROrderDataBean;
	}

	/**
	 * Get quote to le by quote id
	 *
	 * @param quoteId
	 * @return
	 */
	private List<QuoteToLe> getQuoteToLeByQuoteId(Integer quoteId) {
		return quoteToLeRepository.findByQuote_Id(quoteId).stream()
				.filter(quoteToLe -> quoteToLe.getQuoteToLeProductFamilies().stream()
						.anyMatch(quoteToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS
								.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())))
				.collect(Collectors.toList());
	}

	/**
	 * Process COF PDF
	 *
	 * @param quoteId
	 * @param quoteToLes
	 * @param response
	 */
	private void processCofPdf(Integer quoteId, List<QuoteToLe> quoteToLes, HttpServletResponse response) {
		quoteToLes.forEach(quoteToLe -> {
			try {
				teamsDRPdfService.processCofPdf(quoteId, quoteToLe.getId(), response, false, true);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
			}
		});
	}

	/**
	 * Construct service schedule bean
	 *
	 * @param customerLeId
	 * @return
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
	 * Upload Service schedule if not present
	 *
	 * @param quoteToLe
	 * @return
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
	 * Check MS and SS Documents
	 *
	 * @param quoteToLes
	 */
	public void checkMSAandSSDocuments(List<QuoteToLe> quoteToLes) {
		quoteToLes.stream().forEach(this::uploadSSIfNotPresent);
		/**
		 * commented due to requirement change for MSA mapping while optimus journey
		 */
		// forEach(this::uploadMSAIfNotPresent);
	}

	/**
	 * Method to update sfdc stage
	 * in approve quote.
	 * @param quoteToLes
	 */
	public void updateOpportunityInSfdc(Quote quote) {
		quoteToLeRepository.findByQuote_Id(quote.getId()).forEach(quoteToLe -> {
			teamsDRSfdcService.updateOpportunityInSfdc(quoteToLe,SFDCConstants.CLOSED_WON_COF_RECI);
		});
	}

	/**
	 * Method to check whether gsc is present.
	 * @param quoteToLe
	 * @return
	 */
	public Boolean containsGsc(QuoteToLe quoteToLe){
		return quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId()).stream().anyMatch(
				quoteToLeProductFamily -> GSIP_PRODUCT_NAME.equals(quoteToLeProductFamily.getMstProductFamily().getName())
		);
	}

	/**
	 * Approve quote to order for Teams DR
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDROrderDataBean approveQuotes(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		Quote quote = getQuote(quoteId);
		Order order = getOrder(quote);
		TeamsDROrderDataBean teamsDROrderDataBean = saveOrder(order);
		List<QuoteToLe> quoteToLes = getQuoteToLeByQuoteId(quoteId);
		createAndSaveOrderToLe(order, quoteToLes, teamsDROrderDataBean);
		processCofPdf(quoteId, quoteToLes, response);
		updateOpportunityInSfdc(quote);
		checkMSAandSSDocuments(quoteToLes);
		return teamsDROrderDataBean;
	}

	/**
	 * Get cof object mapper for manual approve
	 *
	 * @param orderToLe
	 * @return
	 */
	private Map<String, String> getCofObjectMapperForManualApprove(OrderToLe orderToLe) {
		Map<String, String> cofObjectMapper = new HashMap<>();
		CofDetails cofDetails = cofDetailsRepository
				.findByReferenceIdAndReferenceTypeAndSource(orderToLe.getOrderLeCode(), CommonConstants.ORDER_LE_CODE,
						MANUAL_COF.getSourceType());
		if (Objects.nonNull(cofDetails)) {
			cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
		}
		return cofObjectMapper;
	}

	/**
	 * Set quote status
	 *
	 * @param quote
	 */
	private void setQuoteStatus(Quote quote) {
		quote.getQuoteToLes().forEach(quoteToLe -> {
			if (ORDER_FORM.getConstantCode().equalsIgnoreCase(quoteToLe.getStage())) {
				quoteToLe.setStage(ORDER_ENRICHMENT.getConstantCode());
				quoteToLeRepository.save(quoteToLe);
			}
		});
	}

	/**
	 * Update attachment audit info
	 *
	 * @param order
	 * @param orderToLe
	 * @param quoteToLe
	 * @param isDocuSign
	 * @param cofObjectMapper
	 */
	private void updateAttachmentAuditInfo(Order order, OrderToLe orderToLe, QuoteToLe quoteToLe, boolean isDocuSign,
			Map<String, String> cofObjectMapper) {
		if (isDocuSign) {
			List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
					.findByQuoteToLeAndAttachmentType(quoteToLe, AttachmentTypeConstants.COF.toString());
			for (OmsAttachment omsAttachment : omsAttachmentList) {
				omsAttachment.setOrderToLe(orderToLe);
				omsAttachment.setReferenceName(CommonConstants.ORDERS);
				omsAttachment.setReferenceId(order.getId());
				omsAttachmentRepository.save(omsAttachment);
			}
		}
		try {
			teamsDRPdfService
					.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(), cofObjectMapper);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
		}
	}

	/**
	 * Get legal entity attributes
	 *
	 * @param orderToLe
	 * @param attr
	 * @return
	 */
	public String getLeAttributes(OrderToLe orderToLe, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(attr, CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		Set<OrdersLeAttributeValue> orderToLeAttribute = ordersLeAttributeValueRepository
				.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
		for (OrdersLeAttributeValue quoteLeAttributeValue : orderToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
		}
		return attrValue;
	}

	/**
	 * Populate partner classification
	 *
	 * @param orderToLe
	 * @param mailNotificationBean
	 * @return
	 */
	private MailNotificationBean populatePartnerClassification(OrderToLe orderToLe,
			MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils
					.sendAndReceive(getCustomerLeNameById, String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny()
					.get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(orderToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		return mailNotificationBean;
	}

	/**
	 * Populate mail notification sales order
	 *
	 * @param accountManagerEmail
	 * @param orderRefId
	 * @param customerEmail
	 * @param provisioningLink
	 * @param cofObjectMapper
	 * @param fileName
	 * @param productName
	 * @param orderToLe
	 * @return
	 */
	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId,
			String customerEmail, String provisioningLink, Map<String, String> cofObjectMapper, String fileName,
			String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setSubOrderId(orderToLe.getOrderLeCode());
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(provisioningLink);
		mailNotificationBean.setCofObjectMapper(cofObjectMapper);
		mailNotificationBean.setFileName(fileName);
		mailNotificationBean.setProductName(productName);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * Process order mail notification for manual and docusign
	 *
	 * @param order
	 * @param orderToLe
	 * @param cofObjectMapper
	 * @param user
	 * @throws TclCommonException
	 */
	private void processOrderMailNotificationForManualAndDocuSign(Order order, OrderToLe orderToLe,
			Map<String, String> cofObjectMapper, User user) throws TclCommonException {
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + CommonConstants.HYPHEN + orderToLe
				.getOrderLeCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				user.getEmailId(), appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName,
				CommonConstants.TEAMSDR, orderToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}

	/**
	 * Get quote delegate
	 *
	 * @param order
	 * @param orderToLe
	 * @param quoteToLe
	 * @param cofObjectMapper
	 * @throws TclCommonException
	 */
	private void getQuoteDelegate(Order order, OrderToLe orderToLe, QuoteToLe quoteToLe,
			Map<String, String> cofObjectMapper) throws TclCommonException {
		List<QuoteDelegation> quoteDelegates = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		quoteDelegates.forEach(quoteDelegation -> {
			quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
			quoteDelegationRepository.save(quoteDelegation);
		});
		User user = userRepository.findById(order.getCreatedBy()).get();
		processOrderMailNotificationForManualAndDocuSign(order, orderToLe, cofObjectMapper, user);
	}

	/**
	 * Update manual order confirmation audit
	 *
	 * @param order
	 * @param ipAddress
	 * @throws TclCommonException
	 */
	private void updateManualOrderConfirmationAudit(Order order, String ipAddress) throws TclCommonException {
		try {
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
					.findByOrderRefUuid(order.getOrderCode());
			if (Objects.isNull(orderConfirmationAudit)) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(Utils.getSource());
			orderConfirmationAudit.setMode(FPConstants.MANUAL.toString());
			orderConfirmationAudit.setUploadedBy(Utils.getSource());
			orderConfirmationAudit.setPublicIp(ipAddress);
			orderConfirmationAudit.setOrderRefUuid(order.getOrderCode());
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
		}
	}

	/**
	 * Approve manual quote for teams dr (multiLe)
	 *
	 * @param quoteId
	 * @param forwardedIp
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDROrderDataBean approveManualQuotes(Integer quoteId, String forwardedIp) throws TclCommonException {
		Quote quote = getQuote(quoteId);
		Order order = getOrder(quote);
		TeamsDROrderDataBean teamsDROrderDataBean = saveOrder(order);
		List<QuoteToLe> quoteToLes = getQuoteToLeByQuoteId(quoteId);
		createAndSaveOrderToLe(order, quoteToLes, teamsDROrderDataBean);
		updateOpportunityInSfdc(quote);
		setQuoteStatus(quote);
		orderToLeRepository.findByOrder(order).forEach(orderToLe -> {
			Map<String, String> cofObjectMapper = getCofObjectMapperForManualApprove(orderToLe);
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuoteLeCode(orderToLe.getOrderLeCode());
			updateAttachmentAuditInfo(order, orderToLe, quoteToLe, false, cofObjectMapper);
			try {
				getQuoteDelegate(order, orderToLe, quoteToLe, cofObjectMapper);
				updateManualOrderConfirmationAudit(order, forwardedIp);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.ORDER_ALREADY_EXISTS);
			}
		});
		return teamsDROrderDataBean;
	}
}
