package com.tcl.dias.oms.gsc.service.multiLE;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.NEW;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_EMAIL;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_ID;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_NAME;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_NO;
import static com.tcl.dias.common.constants.LeAttributesConstants.USD;
import static com.tcl.dias.oms.gsc.util.GscConstants.GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_PRODUCT_NAME;
import static com.tcl.dias.oms.gsc.util.GscConstants.NO;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_FIXED;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_MOBILE;
import static com.tcl.dias.oms.gsc.util.GscConstants.RATE_PER_MINUTE_SPECIAL;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GSIP;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.UCAAS_TEAMSDR;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRUtils.checkForNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.teamsdr.beans.TeamsDRCumulativePricesBean;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.gsc.beans.*;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiQuoteLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRQuoteDataBean;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRSfdcService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.teamsdr.beans.SolutionBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesRequest;
import com.tcl.dias.common.teamsdr.beans.TeamsDRRulesResponseWrapper;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.gsc.common.GscOmsSfdcComponent;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.gsc.service.v1.GscSlaService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRQuoteService;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service class for covering multi LE scenario of GSC + TeamsDR
 *
 * @author Srinivasa Raghavan
 */
@Service
@Transactional
public class GscMultiLEQuoteService {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscMultiLEQuoteService.class);

	@Autowired
	UserService userService;
	@Autowired
	GscMultiLEDetailService gscMultiLEDetailService;
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
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	DocusignAuditRepository docusignAuditRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Value("${rabbitmq.rules.teamsdr.segregate}")
	String solutionSegregateQuote;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	GscOmsSfdcComponent gscOmsSfdcComponent;

	@Autowired
	TeamsDRSfdcService teamsDRSfdcService;

	@Autowired
	QuoteLeCreditCheckAuditRepository quoteLeCreditCheckAuditRepository;

	@Autowired
	GscMultiLEPricingFeasibilityService gscMultiLEPricingFeasibilityService;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteTncRepository quoteTncRepository;

	/**
	 * GscQuoteContext static class for reusing most frequently used objects
	 */
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
		String billingCurrency;
		String isGscMultiMacd = "No";
		GscMultiLEQuoteDataBean gscMultiLEQuoteData;
		List<QuoteToLe> quoteToLes;
		List<QuoteToLeProductFamily> quoteToLeProductFamilies;
		List<ProductSolution> productSolutions;
		List<GscQuoteProductComponentsAttributeValueBean> defaultAttributes;
	}

	/**
	 * Get product offering
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
	 * Create product solution
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
	 * Create GscQuoteContext for multiple LE scenario
	 *
	 * @param request
	 * @return
	 */
	private GscQuoteContext createContextMultipleLE(GscMultiLEQuoteDataBean request) {
		Integer customerId = request.getCustomerId();
		GscQuoteContext context = new GscQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		context.customer = !Objects.isNull(customerId)
				? customerRepository.findByErfCusCustomerIdAndStatus(customerId, (byte) 1)
				: context.user.getCustomer();
		context.gscMultiLEQuoteData = request;
		// Only for MACD
		context.opportunity = opportunityRepository.findByUuid(request.getQuoteCode());
		context.billingCurrency = USD;
		return context;
	}

	/**
	 * Populate quote to le's in context
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	public GscQuoteContext populateQuoteToLes(GscQuoteContext context) throws TclCommonException {
		context.quoteToLes = getOrCreateQuoteToLesByQuote(context);
		if (context.quoteToLes.size() == 1)
			context.quoteToLe = context.quoteToLes.get(0);
		return context;
	}

	/**
	 * Get multiple LE quote to le by quote
	 *
	 * @param quote
	 * @return
	 * @throws TclCommonException
	 */
	public List<QuoteToLe> getOrCreateQuoteToLesByQuote(GscQuoteContext context) throws TclCommonException {
		Objects.requireNonNull(context.quote, QUOTE_NULL_MESSAGE);
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(context.quote);
		if (quoteToLes.isEmpty()) {
			GscMultiQuoteLeBean quoteLeBean = context.gscMultiLEQuoteData.getQuoteToLes().stream()
					.filter(Objects::nonNull).findAny().get();
			createQuoteToLe(context.quote, quoteLeBean.getStage(), quoteLeBean.getSubStage(),
					quoteLeBean.getTermsInMonths());
		}
		return quoteToLes;
	}

	/**
	 * Update multipe LE quote to le details
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext updateMultiQuoteToLeDetails(GscQuoteContext context) {
		context.quoteToLes.forEach(quoteToLe -> {
			context.gscMultiLEQuoteData.getQuoteToLes().stream()
					.filter(quoteLeBean -> quoteLeBean.getQuoteleId().equals(quoteToLe.getId())).peek(quoteToLeBean -> {
						quoteToLe.setClassification(quoteToLeBean.getClassification());
						quoteToLe.setQuoteType(quoteToLeBean.getQuoteType());
						if (quoteToLeBean.getQuoteType().equalsIgnoreCase("NEW")) {
							quoteToLe.setErfServiceInventoryTpsServiceId(null);
						}
					}).anyMatch(quoteLeBean -> quoteLeBean.getQuoteleId().equals(quoteToLe.getId()));
		});
		context.quoteToLes = quoteToLeRepository.saveAll(context.quoteToLes);
		return context;
	}

	/**
	 * Get default quote to le attributes
	 *
	 * @param context
	 * @return
	 */
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

	/**
	 * Persist default quote le attributes
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext persistDefaultQuoteLeAttributes(GscQuoteContext context) {
		// persist default quote to le attributes of gsc
		if (context.quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
			List<GscQuoteProductComponentsAttributeValueBean> defaultAttributes = getDefaultQuoteLeAttributes(context);
			context.defaultAttributes = defaultAttributes;
			context.quoteToLes.stream()
					.filter(quoteToLe -> quoteToLe.getQuoteToLeProductFamilies().stream()
							.anyMatch(quoteToLeProductFamily -> GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE
									.toUpperCase().equals(quoteToLeProductFamily.getMstProductFamily().getName())))
					.forEach(quoteToLe -> {
						gscQuoteAttributeService.saveQuoteToLeAttributes(context.quote, quoteToLe, defaultAttributes);
					});
		}
		return context;
	}

	/**
	 * Fetch mst product family for given name
	 *
	 * @param productName
	 * @return
	 */
	private Optional<MstProductFamily> fetchMstProductFamily(String productName) {
		return Optional
				.ofNullable(mstProductFamilyRepository.findByNameAndStatus(productName, GscConstants.STATUS_ACTIVE));

	}

	/**
	 * Populate product family in context (quotetole==1)
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext populateProductFamilyMultiLE(GscQuoteContext context) {
		// set product family in context
		return fetchMstProductFamily(context.gscMultiLEQuoteData.getProductFamilyName()).map(productFamily -> {
			context.productFamily = productFamily;
			return context;
		}).get();
	}

	/**
	 * Populate quote to le product families for multiple LE in context
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext populateMultiQuoteToLeProductFamily(GscQuoteContext context) {
		// set quoteToLeProduct family if quoteToLe == 1
		if (Objects.nonNull(context.quoteToLe)) {
			QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(context.quoteToLe, context.productFamily);
			context.quoteToLeProductFamily = quoteToLeProductFamily;
			// create new quoteToLeProductFamily if it does not exist
			if (Objects.isNull(quoteToLeProductFamily))
				createQuoteToLeProductFamily(context);
		}
		// set quoteToLeProductFamilies if quoteToLe > 1
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLeInAndMstProductFamily(context.quoteToLes, context.productFamily);
		if (Objects.isNull(quoteToLeProductFamilies) || quoteToLeProductFamilies.isEmpty())
			quoteToLeProductFamilies.add(context.quoteToLeProductFamily);
		context.quoteToLeProductFamilies = quoteToLeProductFamilies;
		return context;
	}

	/**
	 * Create or update product solutions
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext createOrUpdateProductSolutionsMultiLE(GscQuoteContext context) {
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = context.quoteToLeProductFamilies;
		List<ProductSolution> savedProductSolutions = productSolutionRepository
				.findByQuoteToLeProductFamilyIn(quoteToLeProductFamilies);
		context.productSolutions = savedProductSolutions;
		/*
		 * Considering access type to be one temporarily, have to handle multiple access
		 * types for multiple solutions in future
		 */
		String accessType = savedProductSolutions.stream().findFirst()
				.flatMap(solution -> quoteGscRepository
						.findByProductSolutionAndStatus(solution, GscConstants.STATUS_ACTIVE).stream().findFirst())
				.map(QuoteGsc::getAccessType).orElse("");
		if (!accessType.isEmpty() && accessType.equals(context.gscMultiLEQuoteData.getAccessType())) {
			return processProductSolutionsMultiLE(context);
		} else {
			context.isAccessTypeChange = true;
			return processAccessTypeChangeMultiLE(context);
		}
	}

	/**
	 * Process change in product solutions for multiple LE
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext processProductSolutionsMultiLE(GscQuoteContext context) {
		List<GscMultipleLESolutionBean> requestSolutions = context.gscMultiLEQuoteData.getQuoteToLes().stream()
				.map(GscMultiQuoteLeBean::getVoiceSolutions).flatMap(Collection::stream).collect(Collectors.toList());
		Set<Integer> savedSolutionIds = context.productSolutions.stream().map(ProductSolution::getId)
				.collect(Collectors.toSet());
		Set<Integer> requestSolutionIds = requestSolutions.stream()
				.flatMap(multiLe -> multiLe.getGscSolutions().stream()).map(GscSolutionBean::getSolutionId)
				.filter(Objects::nonNull).collect(Collectors.toSet());
		Set<Integer> solutionIdsToDelete = Sets.difference(savedSolutionIds, requestSolutionIds);
		// delete removed solutions
		deleteProductSolutionsMultiLE(context, solutionIdsToDelete);
		List<GscMultipleLESolutionBean> newSolutions = new ArrayList<>();
		// add new solutions created under a country already having a solution
		context.gscMultiLEQuoteData.getQuoteToLes().stream().forEach(quoteToLe -> {
			context.quoteToLe = quoteToLeRepository.findByQuoteAndId(context.quote, quoteToLe.getQuoteleId());
			context.quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(context.quoteToLe, context.productFamily);
			quoteToLe.getVoiceSolutions().stream().forEach(multiLe -> {
				try {
					createProductSolutionMultiLE(multiLe, context);
				} catch (TclCommonException e) {
					LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
				}
			});
		});
		// creating new solutions that are added after legal entities classification
		if (Objects.nonNull(context.gscMultiLEQuoteData.getSolutionsToBeAdded()))
			context.gscMultiLEQuoteData.getSolutionsToBeAdded().forEach(solutionBean -> {
				try {
					context.quoteToLe = null;
					context.quoteToLeProductFamily = null;
					createProductSolutionMultiLE(solutionBean, context);
				} catch (TclCommonException e) {
					LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
				}
			});
		// update quote to product families
		context.quoteToLeProductFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLeInAndMstProductFamily(context.quoteToLes, context.productFamily);
		if (context.quoteToLes.size() == 1)
			context.quoteToLeProductFamily = context.quoteToLeProductFamilies.get(0);
		return context;
	}

	/**
	 * Delete product solutions multiple LE scenario
	 *
	 * @param context
	 * @param productSolutionIds
	 */
	private void deleteProductSolutionsMultiLE(GscQuoteContext context, Set<Integer> productSolutionIds) {
		LOGGER.info("Is Access type change : {}", context.isAccessTypeChange);
		LOGGER.info("Solutions IDs to delete : {}", productSolutionIds.toString());
		List<ProductSolution> productSolutions = productSolutionRepository.findAllById(productSolutionIds);
		productSolutions.forEach(productSolution -> {
			List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
					GscConstants.STATUS_ACTIVE);
			quoteGscs.forEach(gscMultiLEDetailService::deleteQuoteGscDetailsByQuoteGsc);
			quoteGscRepository.deleteByProductSolution(productSolution);

			QuoteToLeProductFamily quoteToLeProductFamily = productSolution.getQuoteToLeProductFamily();
			List<ProductSolution> productSolutionsList = productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			if(context.quote.getQuoteCode().startsWith(UCAAS_TEAMSDR)){
				if(productSolutionsList.size() == 1){
					if(Objects.nonNull(productSolution.getTpsSfdcProductName())){
						// deleting product service from sfdc.
						teamsDRSfdcService.deleteProductServiceInSfdc(quoteToLeProductFamily.getQuoteToLe(),productSolution);
					}
				}
			}
			productSolution.getQuoteToLeProductFamily().removeProductSolution(productSolution);
			productSolutionRepository.delete(productSolution);
		});
		if (context.quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())
				&& !CollectionUtils.isEmpty(productSolutions)) {
			gscOmsSfdcComponent.getOmsSfdcService().processDeleteProduct(context.quoteToLe,
					productSolutions.stream().findFirst().get());
		}

	}

	/**
	 * To process access type change
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext processAccessTypeChangeMultiLE(GscQuoteContext context) {
		List<ProductSolution> savedProductSolutions = context.productSolutions;
		List<QuoteToLeProductFamily> families = context.quoteToLeProductFamilies;
		// if family size is > 1 and has GVPN then delete the gvpn solution
		if (families.size() > 1 && families.stream()
				.anyMatch(family -> family.getMstProductFamily().getName().equals(GvpnConstants.GVPN))) {
			Integer quoteToLeProductFamilyId = families.stream()
					.filter(family -> family.getMstProductFamily().getName().equals(GvpnConstants.GVPN))
					.map(QuoteToLeProductFamily::getId).findFirst().get();
			try {
				gvpnQuoteService.deleteProductFamily(quoteToLeProductFamilyId);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.PRODUCT_EMPTY);
			}
		}
		// delete all created solutions
		Set<Integer> solutionIdsToDelete = savedProductSolutions.stream().map(ProductSolution::getId)
				.collect(Collectors.toSet());
		deleteProductSolutionsMultiLE(context, solutionIdsToDelete);
		List<GscMultipleLESolutionBean> newSolutions = new ArrayList<>();
		// make existing id as null and create new solutions
		context.gscMultiLEQuoteData.getQuoteToLes().stream().flatMap(quoteLe -> quoteLe.getVoiceSolutions().stream())
				.flatMap(voiceSolution -> voiceSolution.getGscSolutions().stream())
				.forEach(gscSolution -> gscSolution.setSolutionId(null));
		context.gscMultiLEQuoteData.getQuoteToLes().stream().forEach(quoteToLe -> {
			context.quoteToLe = quoteToLeRepository.findByQuoteAndId(context.quote, quoteToLe.getQuoteleId());
			context.quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(context.quoteToLe, context.productFamily);
			quoteToLe.getVoiceSolutions().stream().forEach(multiLe -> {
				try {
					createProductSolutionMultiLE(multiLe, context);
				} catch (TclCommonException e) {
					LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
				}
			});
		});
		// create new solutions if solutions to be added is present
		if (Objects.nonNull(context.gscMultiLEQuoteData.getSolutionsToBeAdded()))
			newSolutions.addAll(context.gscMultiLEQuoteData.getSolutionsToBeAdded());
		newSolutions.stream().forEach(solutionBean -> {
			try {
				createProductSolutionMultiLE(solutionBean, context);
			} catch (TclCommonException e) {
				LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
			} catch (IllegalArgumentException e) {
				LOGGER.info("Exception occured in create product solution: {}", e.getMessage());
			}
		});
		return context;
	}

	/**
	 * Get multiple LE Solutions
	 *
	 * @param quoteToLeProductFamilies
	 * @return
	 */
	public List<GscMultiQuoteLeBean> getMultiLESolutions(Quote quote,
			List<QuoteToLeProductFamily> quoteToLeProductFamilies) {
		List<GscMultiQuoteLeBean> gscMultiQuoteLes = new ArrayList<>();
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
		CofDetails cofDetail = cofDetailsRepository.findByOrderUuidAndSource(quote.getQuoteCode(),
				Source.MANUAL_COF.getSourceType());
		quoteToLeProductFamilies.forEach(quoteToLeProductFamily -> {
			QuoteToLe quoteToLe = quoteToLeProductFamily.getQuoteToLe();
			Opportunity opportunity = null;
			if (Objects.nonNull(quoteToLe.getClassification())
					&& (PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification())
							|| (PartnerConstants.SELL_THROUGH_CLASSIFICATION
									.equalsIgnoreCase(quoteToLe.getClassification())))) {
				opportunity = opportunityRepository.findByUuid(quote.getQuoteCode());
			}
			GscMultiQuoteLeBean gscMultiQuoteLeBean = GscMultiQuoteLeBean.fromQuoteToLe(quoteToLe,
					docusignAudit != null, cofDetail != null, getIsMultiMacdAttribute(quoteToLe), opportunity);
			gscMultiQuoteLeBean
					.setVoiceSolutions(createMultiLESolutionBean(quoteToLeProductFamily.getProductSolutions()));
			gscMultiQuoteLes.add(gscMultiQuoteLeBean);
		});
		return gscMultiQuoteLes;
	}

	/**
	 * Populate product components in configuration
	 *
	 * @param configurationBean
	 * @param quoteId
	 * @param quoteGscId
	 * @return
	 */
	private GscQuoteConfigurationBean populateProductComponents(GscQuoteConfigurationBean configurationBean,
			Integer quoteId, Integer quoteGscId) {
		List<GscProductComponentBean> components = gscQuoteAttributeService
				.getProductComponentAttributes(quoteId, quoteGscId, configurationBean.getId()).get();
		configurationBean.setProductComponents(components);
		return configurationBean;
	}

	/**
	 * Get GSC Configuration object
	 *
	 * @param quoteGsc
	 * @return
	 */
	public List<GscQuoteConfigurationBean> getGscQuoteConfigurationBean(QuoteGsc quoteGsc) {
		return quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream()
				.map(GscQuoteConfigurationBean::fromGscQuoteDetail)
				.map(gscQuoteConfigurationBean -> populateProductComponents(gscQuoteConfigurationBean,
						quoteGsc.getQuoteToLe().getQuote().getId(), quoteGsc.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * Conversion method from quoteGsc to GscQuoteBean
	 *
	 * @param quoteGsc
	 * @return
	 */
	public GscQuoteBean fromQuoteGsc(QuoteGsc quoteGsc) {
		Objects.requireNonNull(quoteGsc, QUOTE_GSC_NULL_MESSAGE);
		GscQuoteBean gscQuoteBean = GscQuoteBean.fromQuoteGsc(quoteGsc);
		gscQuoteBean.setConfigurations(new ArrayList<>());
		gscQuoteBean.setConfigurations(getGscQuoteConfigurationBean(quoteGsc));
		return gscQuoteBean;
	}

	/**
	 * Create multiple LE solution object
	 *
	 * @param productSolutions
	 * @return
	 */
	public List<GscMultipleLESolutionBean> createMultiLESolutionBean(Iterable<ProductSolution> productSolutions) {
		List<GscMultipleLESolutionBean> gscMultiLESolution = new ArrayList<>();
		if(Objects.nonNull(productSolutions)) {
			List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionInAndStatus(productSolutions,
					GscConstants.STATUS_ACTIVE);
			List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGscIn(quoteGscs);
			Set<String> srcDestPair = new HashSet<>();
			quoteGscDetails.forEach(quoteGscDetail -> {
				if (!srcDestPair.contains(quoteGscDetail.getSrc() + quoteGscDetail.getDest())) {
					GscMultipleLESolutionBean gscMultipleLESolutionBean = new GscMultipleLESolutionBean();
					gscMultipleLESolutionBean.setSource(quoteGscDetail.getSrc());
					gscMultipleLESolutionBean.setDestination(quoteGscDetail.getDest());
					gscMultipleLESolutionBean.setGscSolutions(new ArrayList<>());
					GscSolutionBean gscSolutionBean = getGscSolutionBean(quoteGscDetail);
					gscMultipleLESolutionBean.getGscSolutions().add(gscSolutionBean);
					gscMultiLESolution.add(gscMultipleLESolutionBean);
					srcDestPair.add(quoteGscDetail.getSrc() + quoteGscDetail.getDest());
				} else {
					gscMultiLESolution.stream().filter(gscMultiLE -> (quoteGscDetail.getSrc() + quoteGscDetail.getDest())
							.equals(gscMultiLE.getSource() + gscMultiLE.getDestination())).peek(gscMultiLE -> {
						GscSolutionBean gscSolutionBean = getGscSolutionBean(quoteGscDetail);
						gscMultiLE.getGscSolutions().add(gscSolutionBean);
					}).anyMatch(gscMultiLE -> (quoteGscDetail.getSrc() + quoteGscDetail.getDest())
							.equals(gscMultiLE.getSource() + gscMultiLE.getDestination()));
				}
			});
		}
		return gscMultiLESolution;
	}

	/**
	 * Get Gsc Solutions from gsc quote detail
	 *
	 * @param quoteGscDetail
	 * @return
	 */
	private GscSolutionBean getGscSolutionBean(QuoteGscDetail quoteGscDetail) {
		QuoteGsc quoteGsc = quoteGscDetail.getQuoteGsc();
		ProductSolution productSolution = quoteGsc.getProductSolution();
		GscSolutionBean gscSolutionBean = new GscSolutionBean();
		gscSolutionBean.setAccessType(quoteGsc.getAccessType());
		gscSolutionBean.setProductName(quoteGsc.getProductName());
		gscSolutionBean.setSolutionId(productSolution.getId());
		gscSolutionBean.setSolutionCode(productSolution.getSolutionCode());
		gscSolutionBean.setOfferingName(productSolution.getMstProductOffering().getProductName());
		gscSolutionBean.setGscQuotes(new ArrayList<>());
		gscSolutionBean.getGscQuotes().add(fromQuoteGsc(quoteGsc));
		return gscSolutionBean;
	}

	/**
	 * Create gsc quote multiple LE
	 *
	 * @param quoteDataBean
	 * @param productSolution
	 * @param quoteToLe
	 * @param customer
	 * @param solutionCode
	 * @param user
	 * @return
	 */
	private QuoteGsc createGscQuoteMultiLE(GscMultiLEQuoteDataBean quoteDataBean, ProductSolution productSolution,
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
		quoteGsc.setQuoteGscDetails(new HashSet<>());
		return quoteGsc;
	}

	/**
	 * Create product solution for multiple LE
	 *
	 * @param solution
	 * @param context
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private GscMultipleLESolutionBean createProductSolutionMultiLE(GscMultipleLESolutionBean solution,
			GscQuoteContext context) throws TclCommonException {
		solution.getGscSolutions().stream().filter(gscSolution -> Objects.isNull(gscSolution.getSolutionId()))
				.forEach(gscSolution -> {
					MstProductOffering masterProductOffering = getProductOffering(context.productFamily,
							gscSolution.getOfferingName(), context.user);
					ProductSolution productSolution = createProductSolution(masterProductOffering,
							context.quoteToLeProductFamily, GscUtils.toJson(gscSolution));
					productSolution = productSolutionRepository.save(productSolution);
					QuoteGsc quoteGsc = quoteGscRepository
							.save(createGscQuoteMultiLE(context.gscMultiLEQuoteData, productSolution, context.quoteToLe,
									context.customer, gscSolution.getSolutionCode(), context.user));
					QuoteGscDetail quoteGscDetail = quoteGscDetailsRepository.save(createQuoteGscDetails(
							solution.getSource(), solution.getDestination(), quoteGsc, context.user));
					// until pricing integration (temporary)
					setMockPrices(quoteGsc, quoteGscDetail);
					quoteGscDetailsRepository.save(quoteGscDetail);
					quoteGsc.getQuoteGscDetails().add(quoteGscDetail);
					quoteGsc = quoteGscRepository.save(quoteGsc);
					gscMultiLEDetailService.createOrUpdateProductComponents(context.quote, context.productFamily,
							quoteGsc, quoteGscDetail, productSolution);
					if (context.quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())
							&& !GscConstants.PSTN.equals(quoteGsc.getAccessType())) {
						try {
							gscSlaService.processSla(quoteGsc);
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(ExceptionConstants.SAVE_SLA_ERROR);
						}
					}
					gscSolution.setGscQuotes(ImmutableList.of(GscQuoteBean.fromQuoteGsc(quoteGsc)));
					gscSolution.setProductName(quoteGsc.getProductName());
					gscSolution.setSolutionCode(productSolution.getSolutionCode());
					gscSolution.setSolutionId(productSolution.getId());
					if (Objects.nonNull(context.quoteToLeProductFamily)) {
						if (Objects.isNull(context.quoteToLeProductFamily.getProductSolutions()))
							context.quoteToLeProductFamily.setProductSolutions(new HashSet<>());
						context.quoteToLeProductFamily.getProductSolutions().add(productSolution);
					}
				});
		return solution;
	}

	/**
	 * To Set mock prices until pricing integration is in place
	 * 
	 * @param quoteGsc
	 * @param quoteGscDetail
	 */
	private void setMockPrices(QuoteGsc quoteGsc, QuoteGscDetail quoteGscDetail) {
		quoteGscDetail.setMrc(10.0);
		quoteGscDetail.setNrc(20.0);
		quoteGscDetail.setArc(30.0);
		quoteGsc.setMrc(quoteGscDetail.getMrc());
		quoteGsc.setArc(quoteGscDetail.getArc());
		quoteGsc.setNrc(quoteGscDetail.getNrc());
		quoteGsc.setTcv(quoteGscDetail.getArc() + quoteGscDetail.getNrc());
	}

	/**
	 * Create gsc quote details object
	 *
	 * @param source
	 * @param destination
	 * @param quoteGsc
	 * @param user
	 * @return
	 */
	private QuoteGscDetail createQuoteGscDetails(String source, String destination, QuoteGsc quoteGsc, User user) {
		QuoteGscDetail quoteGscDetail = new QuoteGscDetail();
		quoteGscDetail.setSrc(source);
		quoteGscDetail.setDest(destination);
		quoteGscDetail.setQuoteGsc(quoteGsc);
		quoteGscDetail.setCreatedBy(String.valueOf(user.getId()));
		quoteGscDetail.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		return quoteGscDetail;
	}

	/**
	 * Get quote by quote id
	 *
	 * @param quoteId
	 * @return
	 */
	public Optional<Quote> getQuote(Integer quoteId) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
		return Optional.ofNullable(quote);
	}

	/**
	 * Populate quote in context
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext populateQuoteMultiLE(GscQuoteContext context) {
		return getQuote(context.gscMultiLEQuoteData.getQuoteId()).map(quote -> {
			context.quote = quote;
			return context;
		}).get();
	}

	/**
	 * Create quote to le product family object and save
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
	 * Create product solution for new solutions
	 *
	 * @param context
	 */
	private void createProductSolutions(GscQuoteContext context) {
		context.gscMultiLEQuoteData.getQuoteToLes().stream()
				.flatMap(quoteToLe -> quoteToLe.getVoiceSolutions().stream()).forEach(solutions -> {
					try {
						createProductSolutionMultiLE(solutions, context);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY);
					}
				});
		if (Objects.nonNull(context.gscMultiLEQuoteData.getSolutionsToBeAdded())
				&& !context.gscMultiLEQuoteData.getSolutionsToBeAdded().isEmpty()) {
			context.gscMultiLEQuoteData.getSolutionsToBeAdded().stream().forEach(solutions -> {
				try {
					createProductSolutionMultiLE(solutions, context);
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY);
				}
			});
		}
	}

	/**
	 * Construct quote object
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
		quote.setEngagementOptyId(context.gscMultiLEQuoteData.getEngagementOptyId());
		quote.setQuoteCode(
				null != context.gscMultiLEQuoteData.getEngagementOptyId() ? context.gscMultiLEQuoteData.getQuoteCode()
						: Utils.generateRefId(GscConstants.GSC_PRODUCT_NAME.toUpperCase()));
		return quote;
	}

	/**
	 * Get if exists or create quote if not
	 *
	 * @param context
	 * @return
	 */
	GscQuoteContext getOrCreateQuote(GscQuoteContext context) {
		if (Objects.nonNull(context.gscMultiLEQuoteData.getQuoteId()))
			context = populateQuoteMultiLE(context);
		else
			context.quote = quoteRepository.save(constructQuote(context.customer, context.user, context));
		return context;
	}

	/**
	 * Create multiple LE quote
	 *
	 * @param quoteBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public GscMultiLEQuoteDataBean createQuoteMultipleLE(GscMultiLEQuoteDataBean quoteBean) throws TclCommonException {
		Objects.requireNonNull(quoteBean, QUOTE_NULL_MESSAGE);
		GscQuoteContext context = createContextMultipleLE(quoteBean);
		getOrCreateQuote(context);
		populateQuoteToLes(context);
		updateMultiQuoteToLeDetails(context);
		persistDefaultQuoteLeAttributes(context);
		populateProductFamilyMultiLE(context);
		populateMultiQuoteToLeProductFamily(context);
		createProductSolutions(context);
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeIn(context.quoteToLes);
		updateGscPrices(context.quoteToLes);
		addVoicePricesInQuoteToLe(quoteGscs);
		// updateOpportunityForTeamsDR(context);
		if (context.quoteToLes.size() > 1 || (Objects.nonNull(quoteBean.getSolutionsToBeAdded())
				&& !quoteBean.getSolutionsToBeAdded().isEmpty())) {
			segregateSolutions(quoteBean.getSolutionsToBeAdded(), quoteBean.getQuoteToLes(), context);
		}
		deleteQuoteToLeWithNoSolution(context);
		populateProductSolutionsMultiLE(context);
		if (context.quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase()))
			updateProductServiceInSfdc(context);
		return context.gscMultiLEQuoteData;
	}

	/**
	 * Update Gsc Prices
	 * 
	 * @param quoteToLes
	 */
	public void updateGscPrices(List<QuoteToLe> quoteToLes) {
		quoteToLes.forEach(quoteToLe -> {
			constructGscPriceRequestAndTriggerPricing(quoteToLe);
		});
	}

	/**
	 * Method to create product service
	 * @param quote
	 * @param quoteToLe
	 */
	public void updateOpportunityForTeamsDR(Quote quote,QuoteToLe quoteToLe){
		if(quote.getQuoteCode().startsWith(UCAAS_TEAMSDR)){
			List<ProductSolution> gscSolutions = new ArrayList<>();
			AtomicReference<QuoteToLeProductFamily> gscQtlePf = new AtomicReference<>();
			List<QuoteToLeProductFamily> quoteToLeProdFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			// For voice solutions.
			Optional.ofNullable(quoteToLeProdFamilies).orElse(Collections.emptyList())
					.stream()
					.filter(quoteToLeProductFamily -> GSIP.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
					.map(quoteToLeProductFamily -> {
						gscQtlePf.set(quoteToLeProductFamily);
						return productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily);
					})
					.filter(Objects::nonNull)
					.flatMap(Collection::stream)
					.filter(productSolution -> Objects.nonNull(productSolution.getTpsSfdcProductName()))
					.findAny()
					.ifPresent(gscSolutions::add);

			if(gscSolutions.isEmpty() && Objects.nonNull(gscQtlePf.get())){
				productSolutionRepository.findByQuoteToLeProductFamily(gscQtlePf.get())
						.stream()
						.findAny()
						.ifPresent(gscSolutions::add);
			}
			gscSolutions.stream().findAny().ifPresent(productSolution -> {
				if(Objects.isNull(productSolution.getTpsSfdcProductName())){
					teamsDRSfdcService.createProductServiceInSfdc(quoteToLe,productSolution);
				}else{
					teamsDRSfdcService.updateProductServiceInSfdc(quoteToLe,productSolution);
				}
			});
		}
	}

	/**
	 * Update GSC Multiple LE quote
	 *
	 * @param quoteBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public GscMultiLEQuoteDataBean updateQuoteMultipleLE(GscMultiLEQuoteDataBean quoteBean) throws TclCommonException {
		Objects.requireNonNull(quoteBean, QUOTE_NULL_MESSAGE);
		GscQuoteContext context = createContextMultipleLE(quoteBean);
		populateQuoteMultiLE(context);
		populateQuoteToLes(context);
		subtractVoicePricesInQuoteToLe(context);
		updateMultiQuoteToLeDetails(context);
		persistDefaultQuoteLeAttributes(context);
		populateProductFamilyMultiLE(context);
		populateMultiQuoteToLeProductFamily(context);
		createOrUpdateProductSolutionsMultiLE(context);
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeIn(context.quoteToLes);
		updateGscPrices(context.quoteToLes);
		addVoicePricesInQuoteToLe(quoteGscs);
		// Calling rule engine if solutions to be added is not empty
		if ((Objects.nonNull(context.gscMultiLEQuoteData.getSolutionsToBeAdded())
				&& !context.gscMultiLEQuoteData.getSolutionsToBeAdded().isEmpty())) {
			segregateSolutions(quoteBean.getSolutionsToBeAdded(), quoteBean.getQuoteToLes(), context);
		}
		deleteQuoteToLeWithNoSolution(context);
		populateProductSolutionsMultiLE(context);
		// update in SFDC if GSC quote
		if (context.quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase()))
			updateProductServiceInSfdc(context);
		return context.gscMultiLEQuoteData;
	}

	/**
	 * Subtract mock prices from quote to le if present
	 *
	 * @param context
	 */
	public void subtractVoicePricesInQuoteToLe(GscQuoteContext context) {
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeIn(context.quoteToLes);
		if (Objects.nonNull(quoteGscs) && !quoteGscs.isEmpty()) {
			quoteGscs.forEach(quoteGsc -> {
				QuoteToLe quoteToLe = quoteGsc.getQuoteToLe();
				if (Objects.nonNull(quoteToLe)) {
					quoteToLe.setProposedMrc(
							(Objects.isNull(quoteToLe.getProposedMrc()) ? 0.0 : quoteToLe.getProposedMrc()) - quoteGsc
									.getMrc());
					quoteToLe.setProposedNrc(
							(Objects.isNull(quoteToLe.getProposedNrc()) ? 0.0 : quoteToLe.getProposedNrc()) - quoteGsc
									.getNrc());
					quoteToLe.setProposedArc(
							(Objects.isNull(quoteToLe.getProposedArc()) ? 0.0 : quoteToLe.getProposedArc()) - quoteGsc
									.getArc());
					quoteToLe.setFinalMrc(
							(Objects.isNull(quoteToLe.getFinalMrc()) ? 0.0 : quoteToLe.getProposedMrc()) - quoteGsc
									.getMrc());
					quoteToLe.setFinalArc(
							(Objects.isNull(quoteToLe.getFinalArc()) ? 0.0 : quoteToLe.getFinalArc()) - quoteGsc
									.getArc());
					quoteToLe.setFinalNrc(
							(Objects.isNull(quoteToLe.getFinalNrc()) ? 0.0 : quoteToLe.getFinalNrc()) - quoteGsc
									.getNrc());
					quoteToLe.setTotalTcv(
							(Objects.isNull(quoteToLe.getTotalTcv()) ? 0.0 : quoteToLe.getTotalTcv()) - quoteGsc
									.getTcv());
					quoteToLeRepository.save(quoteToLe);
				}
			});
		}
	}

	/**
	 * Subtract mock prices from quote to le if present
	 *
	 * @param context
	 */
	public void subtractVoicePricesInQuoteToLe(List<QuoteGsc> quoteGscs) {
		if (Objects.nonNull(quoteGscs) && !quoteGscs.isEmpty()) {
			quoteGscs.forEach(quoteGsc -> {
				QuoteToLe quoteToLe = quoteGsc.getQuoteToLe();
				if (Objects.nonNull(quoteToLe)) {
					quoteToLe.setProposedMrc((Objects.isNull(quoteToLe.getProposedMrc()) ? 0.0 :
							quoteToLe.getProposedMrc()) - checkForNull(quoteGsc.getMrc()));
					quoteToLe.setProposedNrc((Objects.isNull(quoteToLe.getProposedNrc()) ? 0.0 :
							quoteToLe.getProposedNrc()) - checkForNull(quoteGsc.getNrc()));
					quoteToLe.setProposedArc((Objects.isNull(quoteToLe.getProposedArc()) ? 0.0 :
							quoteToLe.getProposedArc()) - checkForNull(quoteGsc.getArc()));
					quoteToLe.setFinalMrc(
							(Objects.isNull(quoteToLe.getFinalMrc()) ? 0.0 : quoteToLe.getFinalMrc()) - checkForNull(
									quoteGsc.getMrc()));
					quoteToLe.setFinalArc(
							(Objects.isNull(quoteToLe.getFinalArc()) ? 0.0 : quoteToLe.getFinalArc()) - checkForNull(
									quoteGsc.getArc()));
					quoteToLe.setFinalNrc(
							(Objects.isNull(quoteToLe.getFinalNrc()) ? 0.0 : quoteToLe.getFinalNrc()) - checkForNull(
									quoteGsc.getNrc()));
					quoteToLe.setTotalTcv(
							(Objects.isNull(quoteToLe.getTotalTcv()) ? 0.0 : quoteToLe.getTotalTcv()) - checkForNull(
									quoteGsc.getTcv()));
						quoteToLeRepository.save(quoteToLe);
				}
			});
		}
	}

	/**
	 * Add quote gsc price to quote to le
	 *
	 * @param quoteGscs
	 */
	public void addVoicePricesInQuoteToLe(List<QuoteGsc> quoteGscs) {
		if (Objects.nonNull(quoteGscs) && !quoteGscs.isEmpty()) {
			quoteGscs.forEach(quoteGsc -> {
				QuoteToLe quoteToLe = quoteGsc.getQuoteToLe();
				if (Objects.nonNull(quoteToLe)) {
					quoteToLe.setProposedMrc((Objects.isNull(quoteToLe.getProposedMrc()) ? 0.0 :
							quoteToLe.getProposedMrc()) + checkForNull(quoteGsc.getMrc()));
					quoteToLe.setProposedNrc((Objects.isNull(quoteToLe.getProposedNrc()) ? 0.0 :
							quoteToLe.getProposedNrc()) + checkForNull(quoteGsc.getNrc()));
					quoteToLe.setProposedArc((Objects.isNull(quoteToLe.getProposedArc()) ? 0.0 :
							quoteToLe.getProposedArc()) + checkForNull(quoteGsc.getArc()));
					quoteToLe.setFinalMrc(
							(Objects.isNull(quoteToLe.getFinalMrc()) ? 0.0 : quoteToLe.getFinalMrc()) + checkForNull(
									quoteGsc.getMrc()));
					quoteToLe.setFinalArc(
							(Objects.isNull(quoteToLe.getFinalArc()) ? 0.0 : quoteToLe.getFinalArc()) + checkForNull(
									quoteGsc.getArc()));
					quoteToLe.setFinalNrc(
							(Objects.isNull(quoteToLe.getFinalNrc()) ? 0.0 : quoteToLe.getFinalNrc()) + checkForNull(
									quoteGsc.getNrc()));
					quoteToLe.setTotalTcv(
							(Objects.isNull(quoteToLe.getTotalTcv()) ? 0.0 : quoteToLe.getTotalTcv()) + checkForNull(
									quoteGsc.getTcv()));
					quoteToLeRepository.save(quoteToLe);
				}
			});
		}
	}

	/**
	 * Subtract mock prices from quote to le if present
	 *
	 * @param context
	 */
	public void subtractVoicePricesInQuoteToLe(QuoteToLe quoteToLe, QuoteGsc quoteGsc) {
		if (Objects.nonNull(quoteGsc) && Objects.nonNull(quoteToLe)) {
				quoteToLe.setProposedMrc((Objects.isNull(quoteToLe.getProposedMrc()) ? 0.0 : quoteToLe.getProposedMrc())
						- quoteGsc.getMrc());
				quoteToLe.setProposedNrc((Objects.isNull(quoteToLe.getProposedNrc()) ? 0.0 : quoteToLe.getProposedNrc())
						- quoteGsc.getNrc());
				quoteToLe.setProposedArc((Objects.isNull(quoteToLe.getProposedArc()) ? 0.0 : quoteToLe.getProposedArc())
						- quoteGsc.getArc());
				quoteToLe.setFinalMrc((Objects.isNull(quoteToLe.getFinalMrc()) ? 0.0 : quoteToLe.getProposedMrc())
						- quoteGsc.getMrc());
				quoteToLe.setFinalArc(
						(Objects.isNull(quoteToLe.getFinalArc()) ? 0.0 : quoteToLe.getFinalArc()) - quoteGsc.getArc());
				quoteToLe.setFinalNrc(
						(Objects.isNull(quoteToLe.getFinalNrc()) ? 0.0 : quoteToLe.getFinalNrc()) - quoteGsc.getNrc());
				quoteToLe.setTotalTcv(
						(Objects.isNull(quoteToLe.getTotalTcv()) ? 0.0 : quoteToLe.getTotalTcv()) - quoteGsc.getTcv());
				quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Saving mock quoteToLe prices until pricing integration
	 * 
	 * @param context
	 */
	private void saveMockPricesInQuoteToLe(GscQuoteContext context) {
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeIn(context.quoteToLes);
		quoteGscs.forEach(quoteGsc -> {
			QuoteToLe quoteToLe = quoteGsc.getQuoteToLe();
			quoteToLe.setProposedMrc(quoteToLe.getProposedMrc() + quoteGsc.getMrc());
			quoteToLe.setProposedNrc(quoteToLe.getProposedNrc() + quoteGsc.getNrc());
			quoteToLe.setProposedArc(quoteToLe.getProposedArc() + quoteGsc.getArc());
			quoteToLe.setFinalMrc(quoteToLe.getProposedMrc() + quoteGsc.getMrc());
			quoteToLe.setFinalArc(quoteToLe.getFinalArc() + quoteGsc.getArc());
			quoteToLe.setFinalNrc(quoteToLe.getFinalNrc() + quoteGsc.getNrc());
			quoteToLe.setTotalTcv(quoteToLe.getTotalTcv() + quoteGsc.getTcv());
			quoteToLeRepository.save(quoteToLe);
		});
	}

	/**
	 * Delete quote to le related objects if no product solution
	 *
	 * @param context
	 */
	private void deleteQuoteToLeWithNoSolution(GscQuoteContext context) {
		context.quoteToLes = quoteToLeRepository.findByQuote(context.quote);
		context.quoteToLeProductFamilies = quoteToLeProductFamilyRepository
				.findByQuoteToLeInAndMstProductFamily(context.quoteToLes, context.productFamily);
		// collecting qpf to be deleted that does not have a product solution
		List<QuoteToLeProductFamily> qpfToBeDeleted = context.quoteToLeProductFamilies.stream()
				.filter(quoteToLeProductFamily -> Objects.isNull(quoteToLeProductFamily.getProductSolutions())
						|| quoteToLeProductFamily.getProductSolutions().isEmpty())
				.collect(Collectors.toList());
		LOGGER.info("Number of qpf to delete : {}", qpfToBeDeleted.size());
		if (!qpfToBeDeleted.isEmpty()) {
			qpfToBeDeleted.forEach(quoteToLeProductFamily -> {
				// disassociate qpf from it's quoteToLe
				quoteToLeProductFamily.getQuoteToLe().removeQuoteToLeProductFamily(quoteToLeProductFamily);
			});
			// remove the qpf's
			context.quoteToLeProductFamilies.removeAll(qpfToBeDeleted);
			quoteToLeProductFamilyRepository.deleteAll(qpfToBeDeleted);
		}
		// collect qtl that dies not have any qpf
		List<QuoteToLe> qtlToBeDeleted = context.quoteToLes.stream()
				.filter(quoteToLe -> Objects.isNull(quoteToLe.getQuoteToLeProductFamilies())
						|| quoteToLe.getQuoteToLeProductFamilies().isEmpty())
				.collect(Collectors.toList());
		LOGGER.info("Number of qtl to delete : {}", qtlToBeDeleted.size());
		qtlToBeDeleted.forEach(quoteToLe -> {
			// to delete opportunity
			quoteLeCreditCheckAuditRepository.deleteAll(quoteLeCreditCheckAuditRepository.findByQuoteToLe_id(quoteToLe.getId()));
			teamsDRSfdcService.deleteOpportunity(quoteToLe);
			teamsDRSfdcService.deleteIncompleteRequests(quoteToLe);
		});
		// remove all qtl attributes and then remove the qtl
		if (!qtlToBeDeleted.isEmpty()) {
			context.quoteToLes.removeAll(qtlToBeDeleted);
			quoteLeAttributeValueRepository.deleteAllByQuoteToLeIn(qtlToBeDeleted);
			quoteToLeRepository.deleteAll(qtlToBeDeleted);
		}
	}

	/**
	 * Create or update quote to le product family
	 *
	 * @param quoteToLe
	 * @param existingQuoteToLe
	 * @param mstProductFamily
	 * @param productSolution
	 * @return
	 */
	private QuoteToLeProductFamily createOrUpdateQuoteToLeProductFamily(QuoteToLe quoteToLe, Integer existingQuoteToLe,
																		MstProductFamily mstProductFamily, ProductSolution productSolution) {
		QuoteToLeProductFamily quoteToLeProductFamily = null;
		//
		if (Objects.nonNull(existingQuoteToLe))
			quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLe_IdAndMstProductFamily_Name(existingQuoteToLe, mstProductFamily.getName());
		else if (Objects.nonNull(quoteToLe))
			quoteToLeProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.getId(), mstProductFamily.getName());
		if (Objects.isNull(quoteToLeProductFamily)) {
			quoteToLeProductFamily = new QuoteToLeProductFamily();
			quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
			quoteToLeProductFamily.setQuoteToLe(quoteToLe);
		} else{
			if(Objects.nonNull(productSolution.getTpsSfdcProductName())){
				// deleting the product service in sfdc.
				teamsDRSfdcService.deleteProductServiceInSfdc(quoteToLeProductFamily.getQuoteToLe(),productSolution);
			}
			quoteToLeProductFamily.setQuoteToLe(quoteToLe);
			//Creating new product service ..
			// teamsDRSfdcService.createProductServiceInSfdc(quoteToLe,productSolution);
		}
		// Relationship for quoteToLe and product family are formed
		if (Objects.isNull(quoteToLe.getQuoteToLeProductFamilies()))
			quoteToLe.setQuoteToLeProductFamilies(new HashSet<>());
		if (!quoteToLe.getQuoteToLeProductFamilies().contains(quoteToLeProductFamily))
			quoteToLe.getQuoteToLeProductFamilies().add(quoteToLeProductFamily);
		if (Objects.isNull(quoteToLeProductFamily.getProductSolutions()))
			quoteToLeProductFamily.setProductSolutions(new HashSet<>());
		quoteToLeProductFamily.getProductSolutions().add(productSolution);
		quoteToLeProductFamily = quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		return quoteToLeProductFamily;
	}

	/**
	 * Segregate GSC solutions according to rules
	 *
	 * @param solutionsToBeAdded
	 * @param gscMultiQuoteLes
	 * @param context
	 * @throws TclCommonException
	 */
	private void segregateSolutions(List<GscMultipleLESolutionBean> solutionsToBeAdded,
			List<GscMultiQuoteLeBean> gscMultiQuoteLes, GscQuoteContext context) throws TclCommonException {
		LOGGER.info("Inside segregate solutions");
		TeamsDRRulesRequest teamsDRRulesRequest = new TeamsDRRulesRequest();
		List<SolutionBean> solutionBeans = new ArrayList<>();
		teamsDRRulesRequest.setSolutions(solutionBeans);
		// construct rule request for new gsc solutions added
		if (Objects.nonNull(solutionsToBeAdded)) {
			solutionsToBeAdded.forEach(gscMultipleLESolution -> {
				gscMultipleLESolution.getGscSolutions().forEach(gscSolution -> {
					solutionBeans.add(constructGscRuleRequest(context, null, gscMultipleLESolution, gscSolution));
				});
			});
		}

		// construct rule request for existing gsc solutions
		gscMultiQuoteLes.forEach(gscQuoteToLe -> {
			gscQuoteToLe.getVoiceSolutions().forEach(gscMultipleLESolution -> {
				gscMultipleLESolution.getGscSolutions().forEach(gscSolution -> {
					solutionBeans
							.add(constructGscRuleRequest(context, gscQuoteToLe, gscMultipleLESolution, gscSolution));
				});
			});
		});
		// fetch and populate teams dr solutions
		List<ProductSolution> teamsDRSolutions = teamsDRQuoteService.getTeamsSolutions(context.quote.getId());
		AtomicReference<String> termsInMonths = new AtomicReference<>();
		AtomicReference<String> stage = new AtomicReference<>();
		AtomicReference<String> subStage = new AtomicReference<>();
		teamsDRSolutions.forEach(solution -> {
			SolutionBean solutionBean = constructTeamsRuleRequest(solution);
			solutionBeans.add(solutionBean);
			if (solutionBean.getOfferingName().contains(TeamsDRConstants.PLAN))
				termsInMonths.set(solution.getQuoteToLeProductFamily().getQuoteToLe().getTermInMonths());
			stage.set(solution.getQuoteToLeProductFamily().getQuoteToLe().getStage());
			subStage.set(solution.getQuoteToLeProductFamily().getQuoteToLe().getSubStage());
		});
		// send request to rule engine queue
		String response = (String) mqUtils.sendAndReceive(solutionSegregateQuote,
				Utils.convertObjectToJson(teamsDRRulesRequest));
		LOGGER.info("Response from queue call of rule engine (GSC) : {}", response);
		TeamsDRRulesResponseWrapper rulesResponse = (TeamsDRRulesResponseWrapper) Utils.convertJsonToObject(response,
				TeamsDRRulesResponseWrapper.class);
		// process response from rule engine
		processRuleResponse(context, termsInMonths, stage, subStage, rulesResponse);
	}

	/**
	 * To process solutions according to response from rule engine
	 *  @param context
	 * @param termsInMonths
	 * @param stage
	 * @param subStage
	 * @param rulesResponse
	 */
	private void processRuleResponse(GscQuoteContext context, AtomicReference<String> termsInMonths,
			AtomicReference<String> stage, AtomicReference<String> subStage,
			TeamsDRRulesResponseWrapper rulesResponse) {
		List<Integer> productSolutionIds = rulesResponse.getResponse().stream()
				.flatMap(ruleResponse -> ruleResponse.getSolutionBeans().stream()).map(SolutionBean::getSolutionId)
				.collect(Collectors.toList());
		List<ProductSolution> productSolutions = productSolutionRepository.findAllById(productSolutionIds);
		Map<Integer, List<ProductSolution>> productSolutionMap = productSolutions.stream()
				.collect(Collectors.groupingBy(ProductSolution::getId));
		List<QuoteToLe> quoteToLes = new ArrayList<>(context.quote.getQuoteToLes());
		Map<Integer, List<QuoteToLe>> quoteToLeMap = quoteToLes.stream()
				.collect(Collectors.groupingBy(QuoteToLe::getId));
		MstProductFamily mstProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), BACTIVE);
		AtomicInteger quoteToLeId = new AtomicInteger();
		// if solution size is 1 and id is null create new quoteToLe
		rulesResponse.getResponse().forEach(ruleResponse -> {
			if (ruleResponse.getSolutionBeans().size() == 1) {
				if (Objects.isNull(ruleResponse.getSolutionBeans().get(0).getQuoteToLeId())) {
					QuoteToLe newQuoteToLe = createQuoteToLe(context.quote, stage.get(), subStage.get(),
							termsInMonths.get());
					quoteToLeRepository.save(newQuoteToLe);
					updateSolutions(productSolutionMap, newQuoteToLe, mstProductFamily, null,
							ruleResponse.getSolutionBeans().get(0).getSolutionId());

					// create opportunity for new quotetole.
					// teamsDRSfdcService.createOpportunityInSfdc(newQuoteToLe,null,MICROSOFT_CLOUD_SOLUTIONS);
				}
			}
			// if gsc is present alongwith teams, then update gsc solutions with teamsDR
			// QuoteToLe
			else if (ruleResponse.getSolutionBeans().stream().peek(solution -> {
				if (Objects.nonNull(solution.getQuoteToLeId()))
					quoteToLeId.set(solution.getQuoteToLeId());
				else
					quoteToLeId.set(0);
			}).anyMatch(solution -> !TeamsDRConstants.MS_LICENSE.equals(solution.getOfferingName())
					&& TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS.equals(solution.getProductName()))) {
				ruleResponse.getSolutionBeans().stream().filter(solution -> Objects.isNull(solution.getQuoteToLeId())
						|| (Objects.nonNull(quoteToLeId.get()) && !solution.getQuoteToLeId().equals(quoteToLeId.get())))
						.forEach(solution -> {
							if (solution.getProductName().equals(GSIP_PRODUCT_NAME)) {
								updateSolutions(productSolutionMap, quoteToLeMap.get(quoteToLeId.get()).get(0),
										mstProductFamily, quoteToLeId.get(), solution.getSolutionId());
							}
						});
			}
			// if GSC solution is already present with quoteToLe update other new solutions
			// with this
			else if (ruleResponse.getSolutionBeans().stream().peek(solutionBean -> {
				if (Objects.nonNull(solutionBean.getQuoteToLeId()))
					quoteToLeId.set(solutionBean.getQuoteToLeId());
			}).anyMatch(solutionBean -> Objects.nonNull(solutionBean.getQuoteToLeId()))) {
				ruleResponse.getSolutionBeans().stream().filter(solution -> Objects.isNull(solution.getQuoteToLeId()))
						.forEach(solutionBean -> {
							if (solutionBean.getProductName().equals(GSIP_PRODUCT_NAME)) {
								updateSolutions(productSolutionMap, quoteToLeMap.get(quoteToLeId.get()).get(0),
										mstProductFamily, quoteToLeId.get(), solutionBean.getSolutionId());
							}
						});
			}
			// if all Gsc solutions are new then create new QuoteToLe and update in
			// solutions
			else {
				AtomicReference<QuoteToLe> newQuoteToLe = new AtomicReference<>();
				newQuoteToLe.set(createQuoteToLe(context.quote, stage.get(), subStage.get(), termsInMonths.get()));
				ruleResponse.getSolutionBeans().stream().filter(solution -> Objects.isNull(solution.getQuoteToLeId()))
						.forEach(solutionBean -> {
							if (solutionBean.getProductName().equals(GSIP_PRODUCT_NAME)) {
								quoteToLeRepository.save(newQuoteToLe.get());
								updateSolutions(productSolutionMap, newQuoteToLe.get(), mstProductFamily, null,
										solutionBean.getSolutionId());
							}
						});
				// create opportunity for new quotetole.
				// teamsDRSfdcService.createOpportunityInSfdc(newQuoteToLe.get(),null,MICROSOFT_CLOUD_SOLUTIONS);
			}
		});
	}

	/**
	 * Update solutions
	 *
	 * @param productSolutionMap
	 * @param quoteToLeMap
	 * @param mstProductFamily
	 * @param existingQuoteToLeId
	 * @param solutionId
	 */
	private void updateSolutions(Map<Integer, List<ProductSolution>> productSolutionMap, QuoteToLe newQuoteToLe,
			MstProductFamily mstProductFamily, Integer existingQuoteToLeId, Integer solutionId) {
		productSolutionMap.get(solutionId).forEach(productSolution -> {
			QuoteToLeProductFamily quoteToLeProductFamily = createOrUpdateQuoteToLeProductFamily(newQuoteToLe,
					existingQuoteToLeId, mstProductFamily, productSolution);
			productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		});
		List<QuoteGsc> quoteGscs = quoteGscRepository
				.findByProductSolutionAndStatus(productSolutionMap.get(solutionId).get(0), BACTIVE);
		subtractVoicePricesInQuoteToLe(quoteGscs);
		quoteGscs.forEach(quoteGsc -> quoteGsc.setQuoteToLe(newQuoteToLe));
		productSolutionRepository.saveAll(productSolutionMap.get(solutionId));
		quoteGscRepository.saveAll(quoteGscs);
		updateQuoteToLeWithQuoteGscPrices(newQuoteToLe);
	}

	/**
	 * To construct rule request object for teamsDR solutions
	 *
	 * @param solution
	 * @return
	 */
	private SolutionBean constructTeamsRuleRequest(ProductSolution solution) {
		SolutionBean solutionBean = new SolutionBean();
		solutionBean.setSolutionId(solution.getId());
		solutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
		solutionBean.setQuoteToLeId(solution.getQuoteToLeProductFamily().getQuoteToLe().getId());
		solutionBean.setLeId(solution.getQuoteToLeProductFamily().getQuoteToLe().getErfCusCustomerLegalEntityId());
		solutionBean.setProductName(TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS);
		return solutionBean;
	}

	/**
	 * To construct rule request for GSC solutions
	 *
	 * @param context
	 * @param gscQuoteToLe
	 * @param gscMultipleLESolution
	 * @param gscSolution
	 * @return
	 */
	private SolutionBean constructGscRuleRequest(GscQuoteContext context, GscMultiQuoteLeBean gscQuoteToLe,
			GscMultipleLESolutionBean gscMultipleLESolution, GscSolutionBean gscSolution) {
		SolutionBean solutionBean = new SolutionBean();
		if (Objects.nonNull(gscQuoteToLe)) {
			solutionBean.setQuoteToLeId(gscQuoteToLe.getQuoteleId());
			solutionBean.setLeId(gscQuoteToLe.getCustomerLegalEntityId());
		}
		solutionBean.setSolutionId(gscSolution.getSolutionId());
		solutionBean.setProductName(context.gscMultiLEQuoteData.getProductFamilyName());
		solutionBean.setOfferingName(gscSolution.getOfferingName());
		if (Objects.nonNull(gscMultipleLESolution.getSource()))
			solutionBean.setCountry(gscMultipleLESolution.getSource());
		else
			solutionBean.setCountry(gscMultipleLESolution.getDestination());
		return solutionBean;
	}

	/**
	 * Create quote to le
	 *
	 * @param quote
	 * @param stage
	 * @param termsInMonths
	 * @return
	 */
	private QuoteToLe createQuoteToLe(Quote quote, String stage, String subStage, String termsInMonths) {
		QuoteToLe newQuoteToLe = new QuoteToLe();
		newQuoteToLe.setQuote(quote);
		newQuoteToLe.setQuoteLeCode(Utils.generateUid(5));
		newQuoteToLe.setQuoteType(NEW);
		newQuoteToLe.setStage(stage);
		newQuoteToLe.setSubStage(subStage);
		newQuoteToLe.setProposedMrc(0.0);
		newQuoteToLe.setProposedArc(0.0);
		newQuoteToLe.setProposedNrc(0.0);
		newQuoteToLe.setFinalArc(0.0);
		newQuoteToLe.setFinalMrc(0.0);
		newQuoteToLe.setFinalNrc(0.0);
		newQuoteToLe.setTotalTcv(0.0);
		newQuoteToLe.setTermInMonths(termsInMonths);
		newQuoteToLe = quoteToLeRepository.save(newQuoteToLe);
		return newQuoteToLe;
	}

	/**
	 * Populate products for multiple LE
	 *
	 * @param context
	 */
	private void populateProductSolutionsMultiLE(GscQuoteContext context) {
		context.gscMultiLEQuoteData.setSolutionsToBeAdded(new ArrayList<>());
		if (Objects.nonNull(context.quoteToLeProductFamilies))
			context.gscMultiLEQuoteData
					.setQuoteToLes(getMultiLESolutions(context.quote, context.quoteToLeProductFamilies));
		else
			context.gscMultiLEQuoteData.setQuoteToLes(
					getMultiLESolutions(context.quote, Collections.singletonList(context.quoteToLeProductFamily)));
	}

	/**
	 * Get Multi Macd attribute
	 *
	 * @param quoteBean
	 * @param quoteToLe
	 */
	public String getIsMultiMacdAttribute(QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> gscMultiMacdAttributes = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.IS_GSC_MULTI_MACD);
		String isMultiMacd;
		if (!CollectionUtils.isEmpty(gscMultiMacdAttributes)) {
			isMultiMacd = (gscMultiMacdAttributes.stream().findFirst().get().getAttributeValue());
			LOGGER.info("Value of is multi macd attribute is {}", isMultiMacd);
		} else {
			isMultiMacd = NO;
		}
		return isMultiMacd;
	}

	/**
	 * Get quote data for multiple LE
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public GscMultiLEQuoteDataBean getMultiLEQuoteData(Integer quoteId) throws TclCommonException {
		GscMultiLEQuoteDataBean quoteBean = new GscMultiLEQuoteDataBean();

		Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
		if (!Objects.isNull(quote)) {
			quoteBean.setQuoteId(quoteId);
			quoteBean.setQuoteCode(quote.getQuoteCode());
			quoteBean.setCustomerId(quote.getCustomer().getErfCusCustomerId());
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY);
		}

		final List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
		quoteBean.setQuoteToLes(new ArrayList<>());
		if (quoteToLes.isEmpty())
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY);
		return quoteBean;
	}

	/**
	 * Get product family
	 *
	 * @param quoteBean
	 * @param quoteToLes
	 * @param isFilterNeeded
	 * @param productFamily
	 * @return
	 * @throws TclCommonException
	 */
	private GscMultiLEQuoteDataBean getProductFamily(GscMultiLEQuoteDataBean quoteBean, List<QuoteToLe> quoteToLes,
			Boolean isFilterNeeded, String productFamily) throws TclCommonException {
		MstProductFamily gscMstProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
		List<MstProductFamily> mstProductFamilies = new ArrayList<>();
		if (Objects.isNull(isFilterNeeded) || !isFilterNeeded) {
			mstProductFamilies = mstProductFamilyRepository.findByNameInAndStatus(
					Arrays.asList(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), GvpnConstants.GVPN),
					BACTIVE);
		} else if (isFilterNeeded) {
			mstProductFamilies = mstProductFamilyRepository.findByNameInAndStatus(Arrays.asList(productFamily),
					BACTIVE);
		}
		quoteBean.setProductFamilyName(gscMstProductFamily.getName());
		if (Objects.nonNull(mstProductFamilies) && !mstProductFamilies.isEmpty()) {
			mstProductFamilies.forEach(mstProductFamily -> {
				if (GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase().equals(mstProductFamily.getName())) {
					List<QuoteToLeProductFamily> quoteToLeProductFamily = quoteToLeProductFamilyRepository
							.findByQuoteToLeInAndMstProductFamily(quoteToLes, mstProductFamily);
					quoteBean.setQuoteToLes(getMultiLESolutions(
							quoteToLeProductFamily.stream().findAny().get().getQuoteToLe().getQuote(),
							quoteToLeProductFamily));
				}
			});
			String accessType = quoteBean.getQuoteToLes().stream()
					.flatMap(quoteLe -> quoteLe.getVoiceSolutions().stream()).findFirst()
					.flatMap(gscMultipleLESolution -> gscMultipleLESolution.getGscSolutions().stream().findAny())
					.map(GscSolutionBean::getAccessType).orElse(null);
			quoteBean.setAccessType(accessType);
			// added stage and sub-stage fields at quote level
			Optional<GscMultiQuoteLeBean> quoteLeBean = quoteBean.getQuoteToLes().stream().filter(Objects::nonNull)
					.findAny();
			quoteLeBean.ifPresent(quoteLe -> {
				quoteBean.setStage(quoteLe.getStage());
				quoteBean.setSubStage(quoteLe.getSubStage());
			});
			return quoteBean;
		} else {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY);
		}
	}

	/**
	 * Is numeric
	 *
	 * @param pricingRate
	 * @return
	 */
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
	 * Pick rate per minute attributes
	 *
	 * @param gscQuoteConfigurationBean
	 * @param gscQuoteProductComponentsAttributeValueBean
	 */
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
	 * Set rates for configurations
	 *
	 * @param quoteBean
	 * @return
	 */
	private GscMultiLEQuoteDataBean setRatesForConfigurations(GscMultiLEQuoteDataBean quoteBean) {

		quoteBean.getQuoteToLes().stream().flatMap(quoteLe -> quoteLe.getVoiceSolutions().stream())
				.flatMap(gscMultipleLESolution -> gscMultipleLESolution.getGscSolutions().stream())
				.flatMap(gscSolutionBean -> gscSolutionBean.getGscQuotes().stream())
				.forEach(gscQuoteBean -> gscQuoteBean.getConfigurations()
						.forEach(gscQuoteConfigurationBean -> gscQuoteConfigurationBean.getProductComponents().stream()
								.findFirst()
								.ifPresent(gscProductComponentBean -> gscProductComponentBean.getAttributes().forEach(
										gscQuoteProductComponentsAttributeValueBean -> pickRatePerMinuteFromAttributes(
												gscQuoteConfigurationBean,
												gscQuoteProductComponentsAttributeValueBean)))));
		return quoteBean;
	}

	/**
	 * Get GSC multiple LE quote
	 *
	 * @param quoteId
	 * @param isFilterNeeded
	 * @param productFamily
	 * @return
	 * @throws TclCommonException
	 */
	public GscMultiLEQuoteDataBean getQuoteMultipleLE(Integer quoteId, Boolean isFilterNeeded, String productFamily)
			throws TclCommonException {
		GscMultiLEQuoteDataBean quoteDataBean = getMultiLEQuoteData(quoteId);
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
		quoteDataBean = getProductFamily(quoteDataBean, quoteToLes, isFilterNeeded, productFamily);
		quoteDataBean = setRatesForConfigurations(quoteDataBean);
		return quoteDataBean;
	}

	/**
	 * Update product service in SFDC
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteContext updateProductServiceInSfdc(GscQuoteContext context) {
		try {
			if (context.isAccessTypeChange = true) {
				return createProductServiceInSfdc(context);
			} else {
				gscOmsSfdcComponent.getOmsSfdcService().processUpdateProductForGSC(context.quoteToLe);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR);
		}
		return context;
	}

	/**
	 * Create product service in sfdc
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
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR);
		}
		return context;
	}

	/**
	 * Check for tata voice and remove them
	 *
	 * @param teamsDRQuoteDataBean
	 */
	public void checkForVoiceAndDeleteIfExists(TeamsDRQuoteDataBean teamsDRQuoteDataBean) {
		if (Objects.nonNull(teamsDRQuoteDataBean) && Objects.nonNull(teamsDRQuoteDataBean.getQuoteToLes())) {
			List<Integer> quoteLeIds = teamsDRQuoteDataBean.getQuoteToLes().stream()
					.map(TeamsDRMultiQuoteLeBean::getQuoteleId).collect(Collectors.toList());
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findAllById(quoteLeIds);
			if (Objects.nonNull(quoteToLes) && !quoteToLes.isEmpty()) {
				MstProductFamily gscProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), BACTIVE);
				List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
						.findByQuoteToLeInAndMstProductFamily(quoteToLes, gscProductFamily);
				if (Objects.nonNull(quoteToLeProductFamilies) && !quoteToLeProductFamilies.isEmpty()) {
					LOGGER.info("Deleting GSC related data as tata voice is not required");
					List<ProductSolution> solutionsToDelete = productSolutionRepository
							.findByQuoteToLeProductFamilyIn(quoteToLeProductFamilies);
					List<QuoteGsc> quoteGscs = quoteGscRepository.findByProductSolutionInAndStatus(solutionsToDelete,
							BACTIVE);
					List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGscIn(quoteGscs);
					quoteGscDetailsRepository.deleteAll(quoteGscDetails);
					quoteGscRepository.deleteAll(quoteGscs);
					teamsDRQuoteDataBean.getQuoteToLes()
							.forEach(quoteToLe -> quoteToLe.setVoiceSolutions(new ArrayList<>()));
					solutionsToDelete.forEach(solution -> {
						// QuoteToLeProductFamily quoteToLeProductFamily =
						// solution.getQuoteToLeProductFamily();
						if (Objects.nonNull(solution.getTpsSfdcProductName())) {
							teamsDRSfdcService.deleteProductServiceInSfdc(
									solution.getQuoteToLeProductFamily().getQuoteToLe(), solution);
						}
						productSolutionRepository.delete(solution);
					});
					quoteToLeProductFamilies.forEach(quoteToLeProductFamily -> quoteToLeProductFamily.getQuoteToLe()
							.removeQuoteToLeProductFamily(quoteToLeProductFamily));
					quoteToLeProductFamilyRepository.deleteAll(quoteToLeProductFamilies);
					quoteToLes = quoteToLeRepository.findAllById(quoteLeIds).stream()
							.filter(quoteToLe -> Objects.isNull(quoteToLe.getQuoteToLeProductFamilies())
									|| quoteToLe.getQuoteToLeProductFamilies().isEmpty())
							.collect(Collectors.toList());
					LOGGER.info("Number of qtl to delete : {}", quoteToLes.size());
					// remove all qtl attributes and then remove the qtl
					if (!quoteToLes.isEmpty()) {
						quoteLeAttributeValueRepository.deleteAllByQuoteToLeIn(quoteToLes);
						quoteToLes.forEach(quoteToLe -> {
							quoteLeCreditCheckAuditRepository
									.deleteAll(quoteLeCreditCheckAuditRepository.findByQuoteToLe_id(quoteToLe.getId()));
							teamsDRSfdcService.deleteOpportunity(quoteToLe);
							teamsDRSfdcService.deleteIncompleteRequests(quoteToLe);
							quoteToLeRepository.delete(quoteToLe);
						});
					}
				}
			}
		}
	}

	/**
	 * Get Gsc Solutions by Quote to Le
	 *
	 * @param quoteToLe
	 * @return
	 */
	public List<GscMultipleLESolutionBean> getGscSolutionsByQuoteLe(QuoteToLe quoteToLe) {
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLe.getId(), GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toString());
		if(Objects.nonNull(quoteToLeProductFamily))
			return createMultiLESolutionBean(productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily));
		else return new ArrayList<>();
	}

	/**
	 * Method to construct API request and get response.
	 *  @param services
	 * @param quoteToLe
	 */
	public void constructGscPriceRequestAndTriggerPricing(QuoteToLe quoteToLe) {
		if (Objects.nonNull(quoteToLe)) {
			List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
			if (Objects.nonNull(quoteGscs) && !quoteGscs.isEmpty()) {
				quoteGscs.forEach(quoteGsc -> {
					GscPricingRequest gscPricingRequest = new GscPricingRequest();
					LOGGER.info("Service Name :: {}", quoteGsc.getProductName());
					if (!GLOBAL_OUTBOUND.equals(quoteGsc.getProductName())) {
						List<Integer> configId = new ArrayList<>();
						configId.addAll(
								quoteGsc.getQuoteGscDetails().stream().map(quoteGscDetail -> quoteGscDetail.getId())
										.collect(Collectors.toList()));
						gscPricingRequest.setProductName(quoteGsc.getProductName());
						gscPricingRequest.setGscConfigurationIds(configId);
					} else {
						gscPricingRequest.setProductName("ALL");
						gscPricingRequest.setGscConfigurationIds(Collections.EMPTY_LIST);
					}
					// to be enabled when pricing integration is done.
					try {
						gscMultiLEPricingFeasibilityService.processPricing(quoteToLe.getId(), gscPricingRequest,
								TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS, quoteGsc.getId());
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}

	/**
	 * Update quote_to_le with quote gsc prices
	 *
	 * @param quoteToLe
	 */
	public void updateQuoteToLeWithQuoteGscPrices(QuoteToLe quoteToLe) {
		LOGGER.info("Inside updateQuoteToLeWithTeamsDRPrices");
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeId(quoteToLe.getId());
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		if (Objects.nonNull(quoteGscs) && !quoteGscs.isEmpty()) {
			quoteGscs.forEach(quoteGsc -> {
				LOGGER.info("QuoteTeamsDR :: {}", quoteGsc.getId());
				teamsDRCumulativePricesBean
						.setTotalMrc(teamsDRCumulativePricesBean.getTotalMrc() + checkForNull(quoteGsc.getMrc()));
				teamsDRCumulativePricesBean
						.setTotalNrc(teamsDRCumulativePricesBean.getTotalNrc() + checkForNull(quoteGsc.getNrc()));
				teamsDRCumulativePricesBean
						.setTotalArc(teamsDRCumulativePricesBean.getTotalArc() + checkForNull(quoteGsc.getArc()));
				teamsDRCumulativePricesBean
						.setTotalTcv(teamsDRCumulativePricesBean.getTotalTcv() + checkForNull(quoteGsc.getTcv()));
			});

			quoteToLe.setProposedMrc(
					checkForNull(quoteToLe.getProposedMrc()) + teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setProposedNrc(
					checkForNull(quoteToLe.getProposedNrc()) + teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setProposedArc(
					checkForNull(quoteToLe.getProposedArc()) + teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setFinalMrc(checkForNull(quoteToLe.getFinalMrc()) + teamsDRCumulativePricesBean.getTotalMrc());
			quoteToLe.setFinalNrc(checkForNull(quoteToLe.getFinalNrc()) + teamsDRCumulativePricesBean.getTotalNrc());
			quoteToLe.setFinalArc(checkForNull(quoteToLe.getFinalArc()) + teamsDRCumulativePricesBean.getTotalArc());

			quoteToLe.setTotalTcv(checkForNull(quoteToLe.getTotalTcv()) + teamsDRCumulativePricesBean.getTotalTcv());
			quoteToLeRepository.save(quoteToLe);
		}
	}

	/**
	 * Creating or getting mst oms attribute
	 *
	 * @param attrName
	 * @param attrDescription
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
	 * Update special terms and conditions
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param gscSpecialTermsConditionsBean
	 * @return
	 */
	public GscSpecialTermsConditionsBean updateSpecialTermsAndConditions(Integer quoteId, Integer quoteLeId,
			GscSpecialTermsConditionsBean gscSpecialTermsConditionsBean) {
		Quote quote = quoteRepository.findById(quoteId).get();
		QuoteToLe quoteToLe = quoteToLeRepository.findById(quoteLeId).get();
		QuoteTnc quoteTnc = quoteTncRepository.findByQuoteToLe(quoteToLe);
		if (Objects.nonNull(quoteTnc)) {
			quoteTnc.setTnc(gscSpecialTermsConditionsBean.getTermsAndConditions());
			quoteTnc.setUpdatedBy(Utils.getSource());
			quoteTnc.setUpdatedTime(new Date());
		} else {
			quoteTnc = new QuoteTnc();
			quoteTnc.setQuote(quote);
			quoteTnc.setQuoteToLe(quoteToLe);
			quoteTnc.setCreatedBy(Utils.getSource());
			quoteTnc.setCreatedTime(new Date());
			quoteTnc.setTnc(gscSpecialTermsConditionsBean.getTermsAndConditions());
		}
		quoteTncRepository.save(quoteTnc);
		return gscSpecialTermsConditionsBean;
	}

	/**
	 * Get special terms and conditions
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 */
	public GscSpecialTermsConditionsBean getSpecialTermsAndConditions(Integer quoteId, Integer quoteLeId) {
		QuoteToLe quoteToLe = quoteToLeRepository.findById(quoteLeId).get();
		QuoteTnc quoteTnc = quoteTncRepository.findByQuoteToLe(quoteToLe);
		GscSpecialTermsConditionsBean gscSpecialTermsConditionsBean = new GscSpecialTermsConditionsBean();
		if (Objects.nonNull(quoteTnc)) {
			gscSpecialTermsConditionsBean.setTermsAndConditions(quoteTnc.getTnc());
		}
		gscSpecialTermsConditionsBean.setQuoteId(quoteId);
		return gscSpecialTermsConditionsBean;
	}
}
