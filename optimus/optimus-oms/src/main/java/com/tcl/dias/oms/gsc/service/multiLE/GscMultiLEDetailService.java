package com.tcl.dias.oms.gsc.service.multiLE;

import static com.tcl.dias.oms.constants.QuoteConstants.GVPN_SITES;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_NUMBER_COUNT;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.*;

import java.io.Reader;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.gsc.beans.GscMultiLEConfigRequestBean;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.service.v1.UserService;

/**
 * Detail Service for handling multiple LE solutions
 * 
 */
@Service
public class GscMultiLEDetailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GscMultiLEDetailService.class);

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	GscMultiLEQuoteService gscMultiQuoteService;

	@Autowired
	GscMultiLEAttributeService gscMultiLEAttributeService;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentAttributeValuesRepository;

	@Autowired
	UserService userService;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	private QuoteRepository quoteRepository;

	@Autowired
	GscMultiLEPricingFeasibilityService gscMultiLEPricingFeasibilityService;

	/**
	 * Get quote gsc by id
	 *
	 * @param gscQuoteIds
	 * @return
	 */
	public List<QuoteGsc> getQuoteGscsByQuoteGscsIds(Set<Integer> gscQuoteIds) {
		List<QuoteGsc> quoteGscs = quoteGscRepository.findAllById(gscQuoteIds);
		if (Objects.isNull(quoteGscs))
			quoteGscs = new ArrayList<>();
		return quoteGscs;
	}

	/**
	 * Get quote gsc details by quote gsc
	 *
	 * @param quoteGscs
	 * @return
	 */
	public List<QuoteGscDetail> getQuoteGscDetailsByQuoteGscs(List<QuoteGsc> quoteGscs) {
		List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGscIn(quoteGscs);
		if (Objects.isNull(quoteGscDetails))
			quoteGscDetails = new ArrayList<>();
		return quoteGscDetails;
	}

	/**
	 * Get product solution by id
	 *
	 * @param productSolIds
	 * @return
	 */
	public List<ProductSolution> getProductSolutionByIds(Set<Integer> productSolIds) {
		List<ProductSolution> productSolutions = productSolutionRepository.findAllById(productSolIds);
		if (Objects.isNull(productSolutions))
			productSolutions = new ArrayList<>();
		return productSolutions;
	}

	private static class GscQuoteDetailContext {
		Integer quoteId;
		Integer quoteLeId;
		Integer solutionId;
		Integer quoteGscId;
		Integer quoteGscDetailId;
		Customer customer;
		Quote quote;
		QuoteToLe quoteToLe;
		QuoteGsc quoteGsc;
		List<QuoteGsc> quoteGscs;
		boolean fetchAttributes;
		ProductSolution productSolution;
		List<GscQuoteConfigurationBean> configurations;
		QuoteGscDetail quoteGscDetail;
		User user;
		String existingCurrency;
		String inputCurrency;
		MstOmsAttribute mstOmsAttribute;
		QuoteLeAttributeValue quoteToLeAttribute;
		List<GscMultiLEConfigRequestBean> requestBean;
	}

	/**
	 * Create and load context for quote detail
	 *
	 * @param quoteId
	 * @param configurations
	 * @param fetchAttributes
	 * @return
	 */
	private GscQuoteDetailContext createContext(Integer quoteId, List<GscMultiLEConfigRequestBean> configurations,
			Boolean fetchAttributes) {
		GscQuoteDetailContext context = new GscQuoteDetailContext();
		context.quoteId = quoteId;
		context.requestBean = configurations;
		context.fetchAttributes = fetchAttributes;
		context.customer = userService.getUserId(Utils.getSource()).getCustomer();
		context.user = userService.getUserId(Utils.getSource());
		return context;
	}

	private Map<GscQuoteConfigurationBean, QuoteGscDetail> saveQuoteGscDetail(QuoteGsc quoteGsc, Customer customer,
			QuoteGscDetail quoteGscDetail, GscQuoteConfigurationBean gscQuoteConfigurationBean, User user) {
		if (quoteGscDetail == null) {
			quoteGscDetail = new QuoteGscDetail();
		}
		quoteGscDetail.setSrc(gscQuoteConfigurationBean.getSource());
		quoteGscDetail.setDest(Optional.ofNullable(gscQuoteConfigurationBean.getDestination()).orElse(""));
		quoteGscDetail.setMrc(gscQuoteConfigurationBean.getMrc());
		quoteGscDetail.setNrc(gscQuoteConfigurationBean.getNrc());
		quoteGscDetail.setArc(gscQuoteConfigurationBean.getArc());
		quoteGscDetail.setType(gscQuoteConfigurationBean.getType());
		if (Objects.isNull(quoteGscDetail.getId())) {
			quoteGscDetail.setCreatedBy(String.valueOf(user.getId()));
			quoteGscDetail.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
		}
		quoteGscDetail.setQuoteGsc(quoteGsc);
		quoteGscDetail = quoteGscDetailsRepository.save(quoteGscDetail);
		gscQuoteConfigurationBean.setId(quoteGscDetail.getId());
		Map<GscQuoteConfigurationBean, QuoteGscDetail> response = new HashMap<>();
		response.put(gscQuoteConfigurationBean, quoteGscDetail);
		return response;
	}

	private QuoteGscDetail deleteQuoteGscDetail(Integer quoteGscDetailId) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteGscDetailId, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		productComponents.forEach(productComponent -> {
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentAttributeValuesRepository
					.findByQuoteProductComponent(productComponent);
			if (!CollectionUtils.isEmpty(attributeValues)) {
				quoteProductComponentAttributeValuesRepository.deleteAll(attributeValues);
			}
		});
		quoteProductComponentRepository.deleteAll(productComponents);
		return quoteGscDetailsRepository.findById(quoteGscDetailId).map(quoteGscDetail -> {
			quoteGscDetailsRepository.delete(quoteGscDetail);
			return quoteGscDetail;
		}).orElseGet(null);
	}

	/**
	 * Get default attributes
	 *
	 * @param productComponentName
	 * @return
	 */
	private List<GscQuoteProductComponentsAttributeValueBean> getDefaultAttributes(String productComponentName) {
		URL url = Resources.getResource("attributes/product_component_default_attributes.json");
		CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
		try (Reader jsonReader = charSource.openStream()) {
			Map<String, List<GscQuoteProductComponentsAttributeValueBean>> defaultAttributesMap = new ObjectMapper()
					.readValue(jsonReader,
							new TypeReference<Map<String, List<GscQuoteProductComponentsAttributeValueBean>>>() {
							});
			return defaultAttributesMap.getOrDefault(productComponentName, ImmutableList.of());
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return ImmutableList.of();
	}

	/**
	 * Save product component
	 *
	 * @param referenceId
	 * @param productName
	 * @param quote
	 * @param mstProductFamily
	 * @param componentType
	 * @return
	 */
	public QuoteProductComponent saveProductComponent(Integer referenceId, String productName, Quote quote,
			MstProductFamily mstProductFamily, String componentType) {
		Objects.requireNonNull(referenceId, "Reference ID cannot be null");
		Objects.requireNonNull(productName, PRODUCT_NAME_NULL_MESSAGE);
		Objects.requireNonNull(quote, QUOTE_NULL_MESSAGE);
		Objects.requireNonNull(mstProductFamily, "Master product family cannot be null");
		Objects.requireNonNull(componentType, "Component Type cannot be null");
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(productName);
		QuoteProductComponent productComponent = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(referenceId, mstProductComponent, componentType)
				.orElse(new QuoteProductComponent());
		productComponent.setType(componentType);
		productComponent.setMstProductComponent(mstProductComponent);
		productComponent.setMstProductFamily(mstProductFamily);
		productComponent.setReferenceId(referenceId);
		productComponent.setReferenceName(QuoteConstants.GSC.toString());
		boolean isCreate = Objects.isNull(productComponent.getId());
		QuoteProductComponent savedComponent = quoteProductComponentRepository.save(productComponent);
		// persist default attributes corresponding to component if any during creation
		if (isCreate) {
			List<GscQuoteProductComponentsAttributeValueBean> defaultAttributes = getDefaultAttributes(
					mstProductComponent.getName());
			if (!defaultAttributes.isEmpty()) {
				GscProductComponentBean componentBean = GscProductComponentBean
						.fromQuoteProductComponent(savedComponent);
				componentBean.setAttributes(defaultAttributes);
				gscMultiLEAttributeService.processProductComponent(componentBean, quote, false);
				return savedComponent;
			}
		}
		return savedComponent;
	}

	public GscQuoteConfigurationBean createOrUpdateProductComponent(GscQuoteConfigurationBean bean, Quote quote,
			MstProductFamily mstProductFamily, QuoteGsc quoteGsc, QuoteGscDetail quoteGscDetail,
			ProductSolution solution) {
		Map<String, Object> productProfileData = GscUtils.fromJson(solution.getProductProfileData(),
				new TypeReference<Map<String, Object>>() {
				});
		String accessType = quoteGsc.getAccessType();
		String solutionCode = (String) productProfileData.get("solutionCode");
		String productName = solutionCode.replaceAll(accessType, "");
		// translate GVPN access type to MPLS in optimus oms since SI does not know MPLS
		if (CommonConstants.GVPN.equalsIgnoreCase(accessType)) {
			accessType = GscConstants.MPLS;
		}
		QuoteProductComponent quoteProductComponent = saveProductComponent(quoteGscDetail.getId(), productName, quote,
				mstProductFamily, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		QuoteProductComponent accessTypeProductComponent = saveProductComponent(quoteGscDetail.getId(), accessType,
				quote, mstProductFamily, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		// if MACD and UIFN then clone applicable attributes from existing REFERENCE
		// type configuration
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(quote).get(0);
		if (UIFN.equalsIgnoreCase(quoteGsc.getProductName())
				&& REQUEST_TYPE_ADD_COUNTRY.equalsIgnoreCase(quoteToLe.getQuoteCategory())
				&& ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			copyUIFNMACDAttributes(quoteGsc, quoteProductComponent);
		}
		List<GscProductComponentBean> productComponents = ImmutableList
				.of(quoteProductComponent, accessTypeProductComponent).stream()
				.map(GscProductComponentBean::fromQuoteProductComponent).collect(Collectors.toList());
		bean.setProductComponents(productComponents);
		return bean;
	}

	private Optional<QuoteProductComponentsAttributeValue> cloneIfPresent(
			List<QuoteProductComponentsAttributeValue> attributeValues, String attributeName,
			QuoteProductComponent newComponent) {
		return Optional.ofNullable(attributeValues).orElse(ImmutableList.of()).stream()
				.filter(value -> value.getProductAttributeMaster().getName().equalsIgnoreCase(attributeName))
				.findFirst().map(value -> {
					List<QuoteProductComponentsAttributeValue> existingValues = quoteProductComponentAttributeValuesRepository
							.findByQuoteProductComponent_IdInAndProductAttributeMaster_Name(
									ImmutableList.of(newComponent.getId()), attributeName);
					QuoteProductComponentsAttributeValue newValue = CollectionUtils.isEmpty(existingValues)
							? new QuoteProductComponentsAttributeValue()
							: existingValues.get(0);
					newValue.setProductAttributeMaster(value.getProductAttributeMaster());
					newValue.setAttributeValues(value.getAttributeValues());
					newValue.setQuoteProductComponent(newComponent);
					newValue.setDisplayValue(value.getDisplayValue());
					return newValue;
				});
	}

	private void copyUIFNMACDAttributes(QuoteGsc quoteGsc, QuoteProductComponent productComponent) {
		quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream()
				.filter(quoteGscDetail -> GSC_CFG_TYPE_REFERENCE.equalsIgnoreCase(quoteGscDetail.getType())).findFirst()
				.flatMap(quoteGscDetail -> {
					MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(UIFN);
					return quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(
							quoteGscDetail.getId(), mstProductComponent, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
				}).ifPresent(referenceComponent -> {
					List<QuoteProductComponentsAttributeValue> values = quoteProductComponentAttributeValuesRepository
							.findByQuoteProductComponent(referenceComponent);
					cloneIfPresent(values, ATTR_QUANTITY_OF_NUMBERS, productComponent)
							.ifPresent(quoteProductComponentAttributeValuesRepository::save);
					cloneIfPresent(values, ATTR_PORTING_SERVICE_NEEDED, productComponent)
							.ifPresent(quoteProductComponentAttributeValuesRepository::save);
					cloneIfPresent(values, ATTR_PORTING_NUMBER_COUNT, productComponent)
							.ifPresent(quoteProductComponentAttributeValuesRepository::save);
				});
	}

	/**
	 * To create or update product components
	 *
	 * @param quote
	 * @param mstProductFamily
	 * @param quoteGsc
	 * @param quoteGscDetail
	 * @param solution
	 */
	public void createOrUpdateProductComponents(Quote quote, MstProductFamily mstProductFamily, QuoteGsc quoteGsc,
			QuoteGscDetail quoteGscDetail, ProductSolution solution) {
		Map<String, Object> productProfileData = GscUtils.fromJson(solution.getProductProfileData(),
				new TypeReference<Map<String, Object>>() {
				});
		String accessType = quoteGsc.getAccessType();
		String solutionCode = (String) productProfileData.get("solutionCode");
		String productName = solutionCode.replaceAll(accessType, "");
		// translate GVPN access type to MPLS in optimus oms since SI does not know MPLS
		if (CommonConstants.GVPN.equalsIgnoreCase(accessType)) {
			accessType = GscConstants.MPLS;
		}
		QuoteProductComponent quoteProductComponent = saveProductComponent(quoteGscDetail.getId(), productName, quote,
				mstProductFamily, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		QuoteProductComponent accessTypeProductComponent = saveProductComponent(quoteGscDetail.getId(), accessType,
				quote, mstProductFamily, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		// if MACD and UIFN then clone applicable attributes from existing REFERENCE
		// type configuration
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(quote).get(0);
		if (UIFN.equalsIgnoreCase(quoteGsc.getProductName())
				&& REQUEST_TYPE_ADD_COUNTRY.equalsIgnoreCase(quoteToLe.getQuoteCategory())
				&& ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			copyUIFNMACDAttributes(quoteGsc, quoteProductComponent);
		}
		List<GscProductComponentBean> productComponents = ImmutableList
				.of(quoteProductComponent, accessTypeProductComponent).stream()
				.map(GscProductComponentBean::fromQuoteProductComponent).collect(Collectors.toList());
	}

	/**
	 * Delete quote gsc
	 *
	 * @param quoteGsc
	 */
	public void deleteQuoteGscDetailsByQuoteGsc(QuoteGsc quoteGsc) {
		Objects.requireNonNull(quoteGsc, QUOTE_GSC_NULL_MESSAGE);
		quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream().map(QuoteGscDetail::getId)
				.forEach(this::deleteQuoteGscDetail);
	}

	/**
	 * populateQuote
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext populateQuote(GscQuoteDetailContext context) {
		return gscMultiQuoteService.getQuote(context.quoteId).map(quote -> {
			context.quote = quote;
			return context;
		}).get();
	}

	/**
	 * Populate product components
	 *
	 * @param configurationBean
	 * @param quote
	 * @param fetchAttributes
	 * @return
	 */
	private GscQuoteConfigurationBean populateProductComponents(GscQuoteConfigurationBean configurationBean,
			Quote quote, boolean fetchAttributes) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(configurationBean.getId(), GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		List<GscProductComponentBean> components = productComponents.stream()
				.map(fetchAttributes
						? productComponent -> gscMultiLEAttributeService
								.populateQuoteProductComponentAttributes(productComponent, quote)
						: GscProductComponentBean::fromQuoteProductComponent)
				.collect(Collectors.toList());
		configurationBean.setProductComponents(components);
		return configurationBean;
	}

	/**
	 * Get configurations
	 *
	 * @param quoteId
	 * @param request
	 * @param fetchAttributes
	 * @return
	 */
	public List<GscMultiLEConfigRequestBean> getConfigurations(Integer quoteId,
			List<GscMultiLEConfigRequestBean> request, Boolean fetchAttributes) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(request, GSC_CONFIGURATION_DATA_NULL_MESSAGE);
		GscQuoteDetailContext context = createContext(quoteId, request,
				Optional.ofNullable(fetchAttributes).orElse(Boolean.FALSE));
		populateQuote(context);
		Set<Integer> gscQuoteIds = request.stream().map(GscMultiLEConfigRequestBean::getGscQuoteId)
				.collect(Collectors.toSet());
		List<QuoteGsc> quoteGscs = quoteGscRepository.findAllById(gscQuoteIds);
		List<QuoteGscDetail> quoteGscDetails = quoteGscDetailsRepository.findByQuoteGscIn(quoteGscs);
		Map<Integer, List<QuoteGscDetail>> quoteGscDetailMap = quoteGscDetails.stream()
				.collect(Collectors.groupingBy(quoteGscDetail -> quoteGscDetail.getQuoteGsc().getId()));
		request.forEach(configurationRequest -> {
			configurationRequest.setConfigurations(quoteGscDetailMap.get(configurationRequest.getGscQuoteId()).stream()
					.map(GscQuoteConfigurationBean::fromGscQuoteDetail).map(configurationBean -> this
							.populateProductComponents(configurationBean, context.quote, context.fetchAttributes))
					.collect(Collectors.toList()));
		});
		return request;
	}

	/**
	 * Method to update currency value by code.
	 * @param quoteId
	 * @param quoteLeId
	 * @param paymentCurrency
	 * @return
	 */
	public String updateCurrencyValueByCode(Integer quoteId, Integer quoteLeId, String paymentCurrency) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);
		GscQuoteDetailContext context  = toCurrencyContext(quoteId, quoteLeId, paymentCurrency);
		populateQuoteToLe(context);
		populateQuoteGscByQuoteLe(context);
		convertCurrencyByCode(context);
		return context.inputCurrency;
	}

	/**
	 * Method to create context..
	 * @param quoteId
	 * @param quoteLeId
	 * @param paymentCurrency
	 * @return
	 */
	private GscQuoteDetailContext toCurrencyContext(Integer quoteId, Integer quoteLeId, String paymentCurrency) {
		GscQuoteDetailContext context = new GscQuoteDetailContext();
		context.quoteId = quoteId;
		context.quoteLeId = quoteLeId;
		context.inputCurrency = paymentCurrency;
		context.customer = userService.getUserId(Utils.getSource()).getCustomer();
		context.user = userService.getUserId(Utils.getSource());
		return context;
	}

	/**
	 * populateQuoteToLe
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext populateQuoteToLe(GscQuoteDetailContext context) {
		quoteToLeRepository.findById(context.quoteLeId).ifPresent(quoteToLe -> context.quoteToLe = quoteToLe);
		return context;
	}

	/**
	 * Method to populate quotegsc by quotetole
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext populateQuoteGscByQuoteLe(GscQuoteDetailContext context) {
		context.quoteGscs = quoteGscRepository.findByQuoteToLeId(context.quoteLeId);
		return context;
	}

	/**
	 * Convert Currency
	 *
	 * @param context
	 * @return
	 */
	@Transactional
	private GscQuoteDetailContext convertCurrencyByCode(GscQuoteDetailContext context) {
		findExistingCurrency(context);
		convertPricesBasedOnCurrency(context);
		if(!context.quoteToLe.getQuote().getQuoteCode().startsWith(TeamsDRConstants.UCAAS_TEAMSDR)){
			updatePaymentCurrencyValueInQuoteLe(context);
			updatePaymentCurrencyValueInQuoteLeAttributes(context);
		}
		return context;
	}

	/**
	 * update the quoteToLeAttributes for payment and billing currency code
	 *
	 * @param context
	 * @return GscQuoteDetailContext
	 */
	private GscQuoteDetailContext updatePaymentCurrencyValueInQuoteLeAttributes(GscQuoteDetailContext context) {
		mstOmsAttributeRepository.findByNameAndIsActive(PAYMENT_CURRENCY, STATUS_ACTIVE)
				.stream().findFirst().map(mstOmsAttribute -> {
			QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(context.quoteToLe, mstOmsAttribute).stream().findFirst().get();
			quoteLeAttributeValue.setAttributeValue(context.inputCurrency);
			quoteLeAttributeValue.setQuoteToLe(context.quoteToLe);
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			return quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		});

		mstOmsAttributeRepository.findByNameAndIsActive(BILLING_CURRENCY, STATUS_ACTIVE)
				.stream().findFirst().map(mstOmsAttribute -> {
			QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(context.quoteToLe, mstOmsAttribute).stream().findFirst().get();
			quoteLeAttributeValue.setAttributeValue(context.inputCurrency);
			quoteLeAttributeValue.setQuoteToLe(context.quoteToLe);
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			return quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
		});

		return context;
	}

	/**
	 * update the quoteToLe for currency code
	 *
	 * @param context
	 * @return GscQuoteDetailContext
	 */
	private GscQuoteDetailContext updatePaymentCurrencyValueInQuoteLe(GscQuoteDetailContext context) {
		QuoteToLe quoteToLe = context.quoteToLe;
		quoteToLe.setCurrencyCode(context.inputCurrency);
		quoteToLeRepository.save(quoteToLe);
		return context;
	}

	/**
	 * finds existing currency and set it to context class
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext findExistingCurrency(GscQuoteDetailContext context) {
		context.existingCurrency = context.quoteToLe.getCurrencyCode();
		return context;
	}

	/**
	 * Method to convert prices into another currency values
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext convertPricesBasedOnCurrency(GscQuoteDetailContext context) {
		if(!context.inputCurrency.equalsIgnoreCase(context.existingCurrency))
		{
			// updateQuotePriceCurrencyValues(context);
			QuoteToLe quoteToLe = updateQuoteIllSitesCurrencyValues(context.quoteToLe, context.inputCurrency,
					context.existingCurrency);
			gscMultiLEPricingFeasibilityService.persistGvpnPricesWithGsc(quoteToLe);
			updateQuoteGscDetailCurrencyValues(context);
			computeGscCurrencyValues(context);
		}
		return context;
	}

	/**
	 * Method to compute gsc currency values
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext computeGscCurrencyValues(GscQuoteDetailContext context) {
		List<QuoteGsc> quoteGscList = quoteGscRepository.findByQuoteToLe(context.quoteToLe);
		context.quoteGscs.clear();
		context.quoteGscs.addAll(quoteGscList);
		quoteGscList.stream().forEach(quoteGsc -> {
			Double totalMrc = 0.0;
			Double totalNrc = 0.0;
			Double totalArc = 0.0;
			Double totalTcv = 0.0;
			for (QuoteGscDetail quoteGscDetail : quoteGsc.getQuoteGscDetails()) {
				totalMrc += quoteGscDetail.getMrc();
				totalNrc += quoteGscDetail.getNrc();
				totalArc += quoteGscDetail.getArc();
			}
			Double contractTerm = Double.parseDouble(this.setTermInMonths(context.quoteToLe));
			Double tcv = (contractTerm * Utils.setPrecision(totalMrc, 2)) + Utils.setPrecision(totalNrc, 2);

			quoteGsc.setMrc(totalMrc);
			quoteGsc.setNrc(totalNrc);
			quoteGsc.setArc(totalArc);
			quoteGsc.setTcv(tcv);
			quoteGscRepository.save(quoteGsc);
		});
		return context;
	}

	/**
	 * update {@link QuoteGscDetail} with converted price
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext updateQuoteGscDetailCurrencyValues(GscQuoteDetailContext context) {
		String existingCurrency = context.existingCurrency;
		String inputCurrency = context.inputCurrency;
		List<QuoteGscDetail> quoteGscDetail = quoteGscDetailsRepository.findByQuoteGscIn(context.quoteGscs);
		List<String> quoteGscDetailIds = new ArrayList<>();
		quoteGscDetail.forEach(priceDetail -> {
			quoteGscDetailIds.add(String.valueOf(priceDetail.getId()));
			priceDetail.setMrc(Utils.setPrecision(omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getMrc()),2));
			priceDetail.setNrc(Utils.setPrecision(omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getNrc()),2));
			priceDetail.setArc(Utils.setPrecision(omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getArc()),2));
			quoteGscDetailsRepository.save(priceDetail);

			List<String> attributeIds = quoteProductComponentRepository.findByReferenceIdAndReferenceName(priceDetail.getId(), GSC_PRODUCT_NAME.toUpperCase())
					.stream().flatMap(quoteProductComponent -> quoteProductComponentAttributeValuesRepository.findByQuoteProductComponent(quoteProductComponent)
					.stream()).filter(quoteProductComponentsAttributeValue -> PRICE_ATTRIBUTES.contains(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()))
					.map(attr-> String.valueOf(attr.getId())).collect(Collectors.toList());

			List<QuotePrice> quotePricesOfAttributes = quotePriceRepository.findByReferenceNameAndReferenceIdIn(QuoteConstants.ATTRIBUTES.toString(),attributeIds);
			updateQuotePrice(quotePricesOfAttributes,existingCurrency,inputCurrency);

			List<Integer> attributeIdsInInt =
					attributeIds.stream().map(Integer::parseInt).collect(Collectors.toList());
			quoteProductComponentAttributeValuesRepository.findAllById(attributeIdsInInt).forEach(
					attrValue -> {
						if (Objects.nonNull(attrValue) && Objects.nonNull(attrValue.getAttributeValues()) && StringUtils.isNotBlank(attrValue.getAttributeValues())) {
							attrValue.setAttributeValues(String.valueOf(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
									Utils.setPrecision(Double.parseDouble(attrValue.getAttributeValues()), 2))));
							quoteProductComponentAttributeValuesRepository.save(attrValue);
						}
					}
			);
		});
		List<QuotePrice> quotePricesOfQuoteGscDetail = quotePriceRepository.findByReferenceNameAndReferenceIdIn(QuoteConstants.COMPONENTS.toString(),quoteGscDetailIds);
		updateQuotePrice(quotePricesOfQuoteGscDetail,existingCurrency,inputCurrency);
		return context;
	}


	/**
	 * Method to update quote price
	 * @param quotePrices
	 * @param existingCurrency
	 * @param inputCurrency
	 */
	private void updateQuotePrice(List<QuotePrice> quotePrices,String existingCurrency,String inputCurrency){
		quotePrices.forEach(quotePrice -> {
			if (Objects.nonNull(quotePrice.getCatalogArc()))
				quotePrice.setCatalogArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getCatalogArc(), 2)));
			if (Objects.nonNull(quotePrice.getCatalogMrc()))
				quotePrice.setCatalogMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getCatalogMrc(), 2)));
			if (Objects.nonNull(quotePrice.getCatalogNrc()))
				quotePrice.setCatalogNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getCatalogNrc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedArc()))
				quotePrice.setComputedArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getComputedArc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedMrc()))
				quotePrice.setComputedMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getComputedMrc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedNrc()))
				quotePrice.setComputedNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getComputedNrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveArc()))
				quotePrice.setEffectiveArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getEffectiveArc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveMrc()))
				quotePrice.setEffectiveMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getEffectiveMrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveNrc()))
				quotePrice.setEffectiveNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getEffectiveNrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveUsagePrice()))
				quotePrice.setEffectiveUsagePrice(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getEffectiveUsagePrice(), 2)));
			if (Objects.nonNull(quotePrice.getMinimumArc()))
				quotePrice.setMinimumArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getMinimumArc(),2)));
			if (Objects.nonNull(quotePrice.getMinimumMrc()))
				quotePrice.setMinimumMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getMinimumMrc(),2)));
			if (Objects.nonNull(quotePrice.getMinimumNrc()))
				quotePrice.setMinimumNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getMinimumNrc(), 2)));
			quotePriceRepository.save(quotePrice);
		});
	}

	/**
	 * update {@link QuotePrice} with converted price
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext updateQuotePriceCurrencyValues(GscQuoteDetailContext context) {
		Integer quoteId = context.quoteToLe.getQuote().getId();
		String existingCurrency = context.existingCurrency;
		String inputCurrency = context.inputCurrency;
		quotePriceRepository.findByQuoteId(quoteId).forEach(quotePrice -> {
			if (Objects.nonNull(quotePrice.getCatalogArc()))
				quotePrice.setCatalogArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getCatalogArc(), 2)));
			if (Objects.nonNull(quotePrice.getCatalogMrc()))
				quotePrice.setCatalogMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getCatalogMrc(), 2)));
			if (Objects.nonNull(quotePrice.getCatalogNrc()))
				quotePrice.setCatalogNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getCatalogNrc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedArc()))
				quotePrice.setComputedArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getComputedArc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedMrc()))
				quotePrice.setComputedMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getComputedMrc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedNrc()))
				quotePrice.setComputedNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getComputedNrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveArc()))
				quotePrice.setEffectiveArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getEffectiveArc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveMrc()))
				quotePrice.setEffectiveMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getEffectiveMrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveNrc()))
				quotePrice.setEffectiveNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getEffectiveNrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveUsagePrice()))
				quotePrice.setEffectiveUsagePrice(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getEffectiveUsagePrice(), 2)));
			if (Objects.nonNull(quotePrice.getMinimumArc()))
				quotePrice.setMinimumArc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getMinimumArc(),2)));
			if (Objects.nonNull(quotePrice.getMinimumMrc()))
				quotePrice.setMinimumMrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getMinimumMrc(),2)));
			if (Objects.nonNull(quotePrice.getMinimumNrc()))
				quotePrice.setMinimumNrc(
						omsUtilService.convertCurrency(existingCurrency, inputCurrency, Utils.setPrecision(quotePrice.getMinimumNrc(), 2)));
			quotePriceRepository.save(quotePrice);
		});
		return context;
	}

	/**
	 * Method to update quoteIllSites currency values
	 *
	 * @param context
	 * @return GscQuoteDetailContext
	 */
	private GscQuoteDetailContext updateQuoteIllSitesCurrencyValues(GscQuoteDetailContext context) {
		String existingCurrency = context.existingCurrency;
		String inputCurrency = context.inputCurrency;

		LOGGER.info("Updating quote ill sites currency value on domestic voice");
		context.quoteToLe.getQuoteToLeProductFamilies().stream().forEach(quoteLeProdFamily -> {
			quoteLeProdFamily.getProductSolutions().stream().forEach(prodSolution -> {
				prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
					List<QuoteProductComponent> components = getSiteComponents(illSite);


					Double totalMrc = 0.0;
					Double totalNrc = 0.0;
					Double totalArc = 0.0;

					for (QuoteProductComponent component : components) {
						LOGGER.info("Quote product component for site id {} is {} ",component.getId(), illSite.getId());
						QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(component.getId().toString(), QuoteConstants.COMPONENTS.toString());
						LOGGER.info("Quote price for reference id (product component) {} is  {} ", component.getId(), attrPrice.getId());
						totalMrc = totalMrc + attrPrice.getEffectiveMrc();
						totalNrc = totalNrc + attrPrice.getEffectiveNrc();
						totalArc = totalArc + attrPrice.getEffectiveArc();
					}

					illSite.setArc(Utils.setPrecision(totalArc,2));
					illSite.setMrc(Utils.setPrecision(totalMrc,2));
					illSite.setNrc(Utils.setPrecision(totalNrc,2));
					Double contractTerm = Double.parseDouble(this.setTermInMonths(context.quoteToLe));
					Double tcv = (contractTerm * Utils.setPrecision(illSite.getMrc(), 2))
							+ Utils.setPrecision(illSite.getNrc(), 2);
					illSite.setTcv(tcv);
					illSiteRepository.save(illSite);
					LOGGER.info("Prices for site id {} is mrc : {}, nrc : {}, arc :{}, tcv :{}", illSite.getId(), totalMrc, totalNrc, totalArc, tcv);
				});
			});
		});

		return context;
	}

	/**
	 * Method to get site components
	 *
	 * @param illSite
	 * @return
	 */
	public List<QuoteProductComponent> getSiteComponents(QuoteIllSite illSite) {
		List<QuoteProductComponent> components = new ArrayList<>();
		/*List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(illSite.getId(),QuoteConstants.GSC.toString());*/

		/* Changing the reference name to gvpn_sites since it is dealing with illsite*/
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(illSite.getId(), QuoteConstants.GVPN_SITES.toString());

		quoteProductComponents.stream().forEach(quoteProductComponent -> {
			if (quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.LAST_MILE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.CPE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.VPN_PORT.toString())) {
				components.add(quoteProductComponent);
			}
		});
		return components;
	}

	/**
	 * Method to update quote ill sites currency values
	 * @param quoteToLe
	 * @param inputCurrency
	 * @param existingCurrency
	 * @return
	 */
	public QuoteToLe updateQuoteIllSitesCurrencyValues(QuoteToLe quoteToLe, String inputCurrency,
													   String existingCurrency) {
		Double totalMrc = 0.0;
		Double totalNrc = 0.0;
		Double totalArc = 0.0;

		for (QuoteToLeProductFamily quoteLeProdFamily : quoteToLe.getQuoteToLeProductFamilies()) {
			for (ProductSolution prodSolution : quoteLeProdFamily.getProductSolutions()) {
				for (QuoteIllSite illSite : prodSolution.getQuoteIllSites()) {
					List<QuoteProductComponent> components = getSiteComponentsForGVPN(illSite);

					Double siteTotalMrc = 0.0;
					Double siteTotalNrc = 0.0;
					Double siteTotalArc = 0.0;

					for (QuoteProductComponent component : components) {
						QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(component.getId().toString(), QuoteConstants.COMPONENTS.toString());
						siteTotalMrc = Utils.setPrecision(siteTotalMrc + attrPrice.getEffectiveMrc(),2);
						siteTotalNrc = Utils.setPrecision(siteTotalNrc + attrPrice.getEffectiveNrc(), 2);
						siteTotalArc = Utils.setPrecision(siteTotalArc + attrPrice.getEffectiveArc(), 2);
					}

					illSite.setArc(siteTotalArc);
					illSite.setMrc(siteTotalMrc);
					illSite.setNrc(siteTotalNrc);

					Double contractTerm = Double.parseDouble(this.setTermInMonths(quoteToLe));
					Double tcv = (contractTerm * Utils.setPrecision(illSite.getMrc(), 2))
							+ Utils.setPrecision(illSite.getNrc(), 2);
					illSite.setTcv(tcv);
					LOGGER.info("ILLSITE1" + siteTotalMrc + "ILLSITE GETMRC1" + illSite.getMrc());
					illSiteRepository.save(illSite);
					totalMrc = totalMrc + illSite.getMrc();
					totalNrc = totalNrc + illSite.getNrc();
					totalArc = totalArc + illSite.getArc();
				}

			}

		}

		if (Objects.nonNull(quoteToLe.getQuote()) && Objects.nonNull(quoteToLe.getQuote().getQuoteCode())) {
			if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
				quoteToLe.setFinalMrc(totalMrc);
				quoteToLe.setFinalNrc(totalNrc);
				quoteToLe.setFinalArc(totalArc);
				quoteToLe.setProposedArc(totalArc);
				quoteToLe.setProposedMrc(totalMrc);
				quoteToLe.setProposedNrc(totalNrc);
				Double contractTerm = Double.parseDouble(this.setTermInMonths(quoteToLe));
				Double totalTcv = (contractTerm * Utils.setPrecision(quoteToLe.getFinalMrc(), 2))
						+ Utils.setPrecision(quoteToLe.getFinalNrc(), 2);
				quoteToLe.setTotalTcv(totalTcv);
				quoteToLeRepository.save(quoteToLe);
			}
		}
		return quoteToLe;
	}

	/**
	 * Method to get site components for gsc with gvpn
	 *
	 * @param illSite
	 * @return
	 */
	public List<QuoteProductComponent> getSiteComponentsForGVPN(QuoteIllSite illSite) {
		List<QuoteProductComponent> components = new ArrayList<>();
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(illSite.getId(), GVPN_SITES.toString());
		;

		quoteProductComponents.stream().forEach(quoteProductComponent -> {
			if (quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.LAST_MILE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.CPE.toString())
					|| quoteProductComponent.getMstProductComponent().getName()
					.equalsIgnoreCase(FPConstants.VPN_PORT.toString())) {
				components.add(quoteProductComponent);
			}
		});
		return components;
	}

	/**
	 * Method to set termInMonths
	 *
	 * @param quoteToLe
	 * @param quoteToLe
	 */
	private String setTermInMonths(QuoteToLe quoteToLe) {
		if (quoteToLe.getTermInMonths() != null) {
			return String.valueOf(getMonthsforOpportunityTerms(quoteToLe.getTermInMonths()));
		}
		return "0";
	}

	/**
	 * Method to convert year into months
	 *
	 * @param termPeriod
	 * @return
	 */
	private Integer getMonthsforOpportunityTerms(String termPeriod) {
		Integer month = 12;
		if(termPeriod.contains("month")){
			return month;
		}else{
			String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
			if (reg.length > 0) {
				if (StringUtils.isNumeric(reg[0])) {
					month = Integer.valueOf(reg[0]);
					month=month*12;
				}
			}
		}
		return month;
	}

	/**
	 * Get quote gsc details by ID
	 *
	 * @param quoteGscDetailId
	 * @return {@link QuoteGscDetail}
	 */
	public QuoteGscDetail getQuoteGscDetail(Integer quoteGscDetailId) throws TclCommonException {
		Objects.requireNonNull(quoteGscDetailId, QUOTE_GSC_DETAIL_ID_NULL_MESSAGE);
		Optional<QuoteGscDetail> quoteGscDetail = quoteGscDetailsRepository.findById(quoteGscDetailId);
		if (Objects.isNull(quoteGscDetail)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quoteGscDetail.get();

	}

	/**
	 * Get quote related to Gsc by ID
	 *
	 * @param quoteGscId
	 * @return {@link QuoteGsc}
	 * @throws TclCommonException
	 */
	public QuoteGsc getQuoteGsc(Integer quoteGscId) throws TclCommonException {
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Optional<QuoteGsc> quoteGsc = quoteGscRepository.findById(quoteGscId);
		if (Objects.isNull(quoteGsc)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_GSC_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quoteGsc.get();
	}

	/**
	 * Get product solution by id
	 *
	 * @param solutionId
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public ProductSolution getProductSolution(Integer solutionId) throws TclCommonException {
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Optional<ProductSolution> productSolution = productSolutionRepository.findById(solutionId);
		if (Objects.isNull(productSolution)) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return productSolution.get();
	}

	/**
	 * Get quote by id
	 *
	 * @param quoteId
	 * @return {@link Quote}
	 * @throws TclCommonException
	 */
	public Quote getQuote(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, STATUS_ACTIVE);
		if (Objects.isNull(quote)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quote;
	}

}
