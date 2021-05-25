package com.tcl.dias.oms.gsc.service.v1;

import static com.tcl.dias.oms.constants.QuoteConstants.GVPN_SITES;
import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundError;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_NUMBER_COUNT;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.BILLING_CURRENCY;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CFG_TYPE_REFERENCE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.NEW;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;
import static com.tcl.dias.oms.gsc.util.GscConstants.PAYMENT_CURRENCY;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_NAME_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_DETAIL_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_LE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.SOLUTION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;
import static com.tcl.dias.oms.gsc.util.GscConstants.UIFN;

import java.io.Reader;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;

/**
 * Services to handle all quote detail related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscQuoteDetailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscQuoteDetailService.class);

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	@Autowired
	GscQuoteService gscQuoteService;
	@Autowired
	GscQuoteAttributeService gscQuoteAttributeService;
	@Autowired
	ProductSolutionRepository productSolutionRepository;
	@Autowired
	QuoteGscRepository quoteGscRepository;
	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	@Autowired
	UserService userService;
	@Autowired
	OmsUtilService omsUtilService;
	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	@Autowired
	QuotePriceRepository quotePriceRepository;
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentAttributeValuesRepository;
	@Autowired
	IllSiteRepository illSiteRepository;
	@Autowired
	MQUtils mqUtils;
	@Value("${rabbitmq.country.currency.queue}")
	String currencyQueue;
	@Autowired
	private GscPricingFeasibilityService gscPricingFeasibilityService;
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	/**
	 * populateQuote
	 *
	 * @param context
	 * @return
	 */
	private Try<GscQuoteDetailContext> populateQuote(GscQuoteDetailContext context) {
		return gscQuoteService.getQuote(context.quoteId).map(quote -> {
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
	private Try<GscQuoteDetailContext> populateQuoteToLe(GscQuoteDetailContext context) {
		return quoteToLeRepository.findByQuote(context.quote).stream().findFirst().map(quoteToLe -> {
			context.quoteToLe = quoteToLe;
			return Try.success(context);
		}).orElse(Try.success(context));
	}

	/**
	 * Get product solution by id
	 *
	 * @param solutionId
	 * @return {@link ProductSolution}
	 */
	public Try<ProductSolution> getProductSolution(Integer solutionId) {
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		return productSolutionRepository.findById(solutionId).map(Try::success).orElse(notFoundError(
				ExceptionConstants.PRODUCT_EMPTY, String.format("Product solution with id: %s not found", solutionId)));
	}

	/**
	 * Get quote related to Gsc by ID
	 *
	 * @param quoteGscId
	 * @return {@link QuoteGsc}
	 */
	public Try<QuoteGsc> getQuoteGsc(Integer quoteGscId) {
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		return quoteGscRepository.findById(quoteGscId).map(Try::success).orElse(notFoundError(
				ExceptionConstants.QUOTE_EMPTY, String.format("Quote GSC with id: %s not found", quoteGscId)));
	}

	/**
	 * Get quote gsc details by ID
	 *
	 * @param quoteGscDetailId
	 * @return {@link QuoteGscDetail}
	 */
	public Try<QuoteGscDetail> getQuoteGscDetail(Integer quoteGscDetailId) {
		Objects.requireNonNull(quoteGscDetailId, QUOTE_GSC_DETAIL_ID_NULL_MESSAGE);
		return quoteGscDetailsRepository.findById(quoteGscDetailId).map(Try::success)
				.orElse(notFoundError(ExceptionConstants.QUOTE_EMPTY,
						String.format("Quote GSC configuration with id: %s not found", quoteGscDetailId)));
	}

	/**
	 * populateSolution
	 *
	 * @param context
	 * @return
	 */
	private Try<GscQuoteDetailContext> populateSolution(GscQuoteDetailContext context) {
		return getProductSolution(context.solutionId).map(solution -> {
			context.productSolution = solution;
			return context;
		});
	}

	/**
	 * populateQuoteGsc
	 *
	 * @param context
	 * @return
	 */
	private Try<GscQuoteDetailContext> populateQuoteGsc(GscQuoteDetailContext context) {
		return getQuoteGsc(context.quoteGscId).map(quoteGsc -> {
			context.quoteGsc = quoteGsc;
			return context;
		});
	}

	/**
	 * populateQuoteGsc
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext populateQuoteGscByQuoteLe(GscQuoteDetailContext context) {
		context.quoteGscs = quoteGscRepository.findByQuoteToLeId(context.quoteLeId);
		return context;
	}

	/**
	 * populateQuoteGscDetail
	 *
	 * @param context
	 * @return
	 */
	private Try<GscQuoteDetailContext> populateQuoteGscDetail(GscQuoteDetailContext context) {
		return getQuoteGscDetail(context.quoteGscDetailId).map(quoteGscDetail -> {
			context.quoteGscDetail = quoteGscDetail;
			return context;
		});
	}

	/**
	 * toContext
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @param configurations
	 * @return
	 */
	private Try<GscQuoteDetailContext> toContext(Integer quoteId, Integer solutionId, Integer quoteGscId,
			List<GscQuoteConfigurationBean> configurations, Boolean fetchAttributes) {
		GscQuoteDetailContext context = new GscQuoteDetailContext();
		context.quoteId = quoteId;
		context.solutionId = solutionId;
		context.quoteGscId = quoteGscId;
		context.configurations = configurations;
		context.fetchAttributes = fetchAttributes;
		context.customer = userService.getUserId(Utils.getSource()).getCustomer();
		context.user = userService.getUserId(Utils.getSource());
		return Try.success(context);
	}

	private Try<GscQuoteDetailContext> toCurrencyContext(Integer quoteId, Integer quoteLeId, String paymentCurrency) {
		GscQuoteDetailContext context = new GscQuoteDetailContext();
		context.quoteId = quoteId;
		context.quoteLeId = quoteLeId;
		context.inputCurrency = paymentCurrency;
		context.customer = userService.getUserId(Utils.getSource()).getCustomer();
		context.user = userService.getUserId(Utils.getSource());
		return Try.success(context);
	}

	/**
	 * saveQuoteGscDetail
	 *
	 * @param quoteGsc
	 * @param customer
	 * @param gscQuoteConfigurationBean
	 * @param user
	 * @return
	 */
	private Tuple2<GscQuoteConfigurationBean, QuoteGscDetail> saveQuoteGscDetail(QuoteGsc quoteGsc, Customer customer,
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
		return Tuple.of(gscQuoteConfigurationBean, quoteGscDetail);
	}

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
	 * @return {@link QuoteProductComponent}
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
				gscQuoteAttributeService.processProductComponent(componentBean, quote);
				return savedComponent;
			}
		}
		return savedComponent;
	}

	private Optional<QuoteProductComponentsAttributeValue> cloneIfPresent(
			List<QuoteProductComponentsAttributeValue> attributeValues, String attributeName,
			QuoteProductComponent newComponent) {
		return Optional.ofNullable(attributeValues).orElse(ImmutableList.of()).stream()
				.filter(value -> value.getProductAttributeMaster().getName().equalsIgnoreCase(attributeName))
				.findFirst().map(value -> {
					List<QuoteProductComponentsAttributeValue> existingValues = quoteProductComponentsAttributeValueRepository
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
	 * createOrUpdateProductComponent
	 *
	 * @param bean
	 * @param quote
	 * @param mstProductFamily
	 * @param quoteGsc
	 * @param quoteGscDetail
	 * @param solution
	 * @return
	 */
	private GscQuoteConfigurationBean createOrUpdateProductComponent(GscQuoteConfigurationBean bean, Quote quote,
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

	/**
	 * saveQuoteGscDetails
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext saveQuoteGscDetails(GscQuoteDetailContext context) {
		MstProductFamily mstProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(context.quoteToLe,
						context.productSolution.getQuoteToLeProductFamily().getMstProductFamily())
				.getMstProductFamily();

		List<QuoteGscDetail> savedQuoteGscDetails = quoteGscDetailsRepository.findByQuoteGsc(context.quoteGsc);

		Map<String, QuoteGscDetail> gscDetailMap = savedQuoteGscDetails.stream().collect(Collectors.toMap(detail -> {
			// treat empty string in dest also as null since pricing sets this to "" when
			// null during normal flow
			String destination = Strings.isNullOrEmpty(detail.getDest()) ? null : detail.getDest();
			return detail.getSrc() + ":" + destination;
		}, Function.identity()));
		Map<String, GscQuoteConfigurationBean> configurationBeanMap = context.configurations.stream()
				.collect(Collectors.toMap(
						detail -> detail.getSource() + ":"
								+ (Strings.isNullOrEmpty(detail.getDestination()) ? null : detail.getDestination()),
						Function.identity()));
		Set<String> toDelete = Sets.difference(gscDetailMap.keySet(), configurationBeanMap.keySet());
		// delete configurations
		toDelete.stream().map(gscDetailMap::get).map(QuoteGscDetail::getId).forEach(this::deleteQuoteGscDetail);
		context.configurations = configurationBeanMap.entrySet().stream()
				.map(entry -> this.saveQuoteGscDetail(context.quoteGsc, context.customer,
						gscDetailMap.get(entry.getKey()), entry.getValue(), context.user))
				.map(beanPair -> this.createOrUpdateProductComponent(beanPair._1, context.quote, mstProductFamily,
						context.quoteGsc, beanPair._2, context.productSolution))
				.collect(Collectors.toList());
		return context;
	}

	private GscQuoteConfigurationBean populateProductComponents(GscQuoteConfigurationBean configurationBean,
			Quote quote, boolean fetchAttributes) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(configurationBean.getId(), GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		List<GscProductComponentBean> components = productComponents.stream()
				.map(fetchAttributes
						? productComponent -> gscQuoteAttributeService
								.populateQuoteProductComponentAttributes(productComponent, quote)
						: GscProductComponentBean::fromQuoteProductComponent)
				.collect(Collectors.toList());
		configurationBean.setProductComponents(components);
		return configurationBean;
	}

	/**
	 * fetchQuoteDetails
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext fetchQuoteDetails(GscQuoteDetailContext context) {
		context.configurations = quoteGscDetailsRepository.findByQuoteGsc(context.quoteGsc).stream()
				.map(GscQuoteConfigurationBean::fromGscQuoteDetail).map(configurationBean -> this
						.populateProductComponents(configurationBean, context.quote, context.fetchAttributes))
				.collect(Collectors.toList());
		return context;
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
	 * Delete quote gsc
	 *
	 * @param quoteGsc
	 */
	public void deleteQuoteGscAndQuoteGscDetailsByQuoteGsc(QuoteGsc quoteGsc) {
		Objects.requireNonNull(quoteGsc, QUOTE_GSC_NULL_MESSAGE);
		quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream().map(QuoteGscDetail::getId)
				.forEach(this::deleteQuoteGscDetail);
		quoteGscRepository.delete(quoteGsc);
	}

	/**
	 * deleteQuoteGscDetail
	 *
	 * @param quoteGscDetailId
	 */
	private QuoteGscDetail deleteQuoteGscDetail(Integer quoteGscDetailId) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteGscDetailId, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		productComponents.forEach(productComponent -> {
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentAttributeValuesRepository
					.findByQuoteProductComponent(productComponent);
			if (!CollectionUtils.isEmpty(attributeValues)) {
				quoteProductComponentsAttributeValueRepository.deleteAll(attributeValues);
			}
		});
		quoteProductComponentRepository.deleteAll(productComponents);
		return quoteGscDetailsRepository.findById(quoteGscDetailId).map(quoteGscDetail -> {
			quoteGscDetailsRepository.delete(quoteGscDetail);
			return quoteGscDetail;
		}).orElseGet(null);
	}

	/**
	 * deleteGscQuoteDetails
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext deleteGscQuoteDetails(GscQuoteDetailContext context) {
		context.configurations = context.configurations.stream().map(GscQuoteConfigurationBean::getId)
				.map(this::deleteQuoteGscDetail).filter(Objects::nonNull)
				.map(GscQuoteConfigurationBean::fromGscQuoteDetail).collect(Collectors.toList());
		return context;
	}

	/**
	 * Delete quote related configurations
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @param data
	 * @return {@link GscQuoteConfigurationBean}
	 */
	@Transactional
	public List<GscQuoteConfigurationBean> deleteConfigurations(Integer quoteId, Integer solutionId, Integer quoteGscId,
			List<GscQuoteConfigurationBean> data) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(data, CONFIGURATION_ID_NULL_MESSAGE);
		return toContext(quoteId, solutionId, quoteGscId, data, false).flatMap(this::populateQuote)
				.flatMap(this::populateQuoteToLe).flatMap(this::populateSolution).flatMap(this::populateQuoteGsc)
				.map(this::deleteGscQuoteDetails).map(context -> context.configurations).get();
	}

	/**
	 * Create quote related configuration
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @param data
	 * @return {@link GscQuoteConfigurationBean}
	 */
	@Transactional
	public List<GscQuoteConfigurationBean> createConfiguration(Integer quoteId, Integer solutionId, Integer quoteGscId,
			List<GscQuoteConfigurationBean> data) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(data, CONFIGURATION_ID_NULL_MESSAGE);
		return toContext(quoteId, solutionId, quoteGscId, data, false)
				.flatMap(this::populateQuote)
				.flatMap(this::populateQuoteToLe)
				.flatMap(this::populateSolution)
				.flatMap(this::populateQuoteGsc)
				.map(this::saveQuoteGscDetails)
				.map(this::convertCurrency)
				.map(context -> context.configurations).get();
	}

	/**
	 * Convert Currency By given payment currency
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 */
	@Transactional
	public String updateCurrencyValueByCode(Integer quoteId, Integer quoteLeId, String paymentCurrency) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);
		return toCurrencyContext(quoteId, quoteLeId, paymentCurrency)
				.flatMap(this::populateQuote)
				.flatMap(this::populateQuoteToLe)
				.map(this::populateQuoteGscByQuoteLe)
				.map(this::convertCurrencyByCode)
				.map(context -> context.inputCurrency).get();
	}

	/**
	 * Update quote related configuration
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @param quoteGscDetailId
	 * @param data
	 * @return {@link GscQuoteConfigurationBean}
	 */
	@Transactional
	public List<GscQuoteConfigurationBean> updateConfiguration(Integer quoteId, Integer solutionId, Integer quoteGscId,
			Integer quoteGscDetailId, GscQuoteConfigurationBean data) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscDetailId, QUOTE_GSC_DETAIL_ID_NULL_MESSAGE);
		Objects.requireNonNull(data, CONFIGURATION_ID_NULL_MESSAGE);
		return toContext(quoteId, solutionId, quoteGscId, ImmutableList.of(data), false).map(context -> {
			context.quoteGscDetailId = quoteGscDetailId;
			return context;
		}).flatMap(this::populateQuote).flatMap(this::populateQuoteToLe).flatMap(this::populateSolution)
				.flatMap(this::populateQuoteGsc).flatMap(this::populateQuoteGscDetail).map(this::saveQuoteGscDetails)
				.map(context -> context.configurations).get();
	}

	/**
	 * Get gsc quote details
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @return {@link GscQuoteConfigurationBean}
	 */
	public Try<List<GscQuoteConfigurationBean>> getGscQuoteDetails(Integer quoteId, Integer solutionId,
			Integer quoteGscId, Boolean fetchAttributes) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		return toContext(quoteId, solutionId, quoteGscId, ImmutableList.of(),
				Optional.ofNullable(fetchAttributes).orElse(Boolean.FALSE)).flatMap(this::populateQuote)
						.flatMap(this::populateQuoteToLe).flatMap(this::populateSolution)
						.flatMap(this::populateQuoteGsc).map(this::fetchQuoteDetails)
						.map(context -> context.configurations);
	}

	/**
	 * Convert Currency
	 *
	 * @param context
	 * @return
	 */
	@Transactional
	public GscQuoteDetailContext convertCurrency(GscQuoteDetailContext context) {
		if ((DOMESTIC_VOICE.equalsIgnoreCase(context.quoteGsc.getProductName()) || ACDTFS.equalsIgnoreCase(context.quoteGsc.getProductName()))
				&& NEW.equalsIgnoreCase(context.quoteToLe.getQuoteType())) {
			return Try.success(createCurrencyContext(context))
					.map(this::getCurrencyValue)
					.map(this::findExistingCurrency)
					.map(this::updateQuoteToLeCurrencyValues)
					.map(this::updateQuoteGscCurrencyValues)
					.map(this::updateQuoteGscDetailCurrencyValues)
					.map(this::updateQuotePriceCurrencyValues)
					.map(this::updateGvpnPriceAttributes)
					.map(this::updateQuoteIllSitesCurrencyValues)
					.map(this::updatePaymentCurrencyValueInQuoteLe).get();
		}

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
			return Try.success(createCurrencyContext(context))
					.map(this::findExistingCurrency)
					.map(this::convertPricesBasedOnCurrency)
					.map(this::updatePaymentCurrencyValueInQuoteLe)
					.map(this::updatePaymentCurrencyValueInQuoteLeAttributes)
					.get();
	}
	
	/**
	 * Method to convert prices into another currency values
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext convertPricesBasedOnCurrency(GscQuoteDetailContext context) {
		if(!context.inputCurrency.equalsIgnoreCase(context.existingCurrency))
		{
			updateQuotePriceCurrencyValues(context);
			QuoteToLe quoteToLe = updateQuoteIllSitesCurrencyValues(context.quoteToLe, context.inputCurrency,
					context.existingCurrency);
			gscPricingFeasibilityService.persistGvpnPricesWithGsc(quoteToLe);
			updateQuoteGscDetailCurrencyValues(context);
			computeGscCurrencyValues(context);
			Double contractTerm = Double.parseDouble(this.setTermInMonths(context.quoteToLe));
			gscPricingFeasibilityService.updateQuoteToLeCurrencyValues(context.quoteToLe, context.quoteGscs, contractTerm,context.existingCurrency,context.inputCurrency);
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
	 * Builds the CurrencyConvertor Context
	 *
	 * @param context
	 * @return GscQuoteDetailContext
	 */
	private static GscQuoteDetailContext createCurrencyContext(GscQuoteDetailContext context) {
		return context;
	}

	private GscQuoteDetailContext getCurrencyValue(GscQuoteDetailContext context) {
		String currency = "";
		try {
			if (ACDTFS.equalsIgnoreCase(context.quoteGsc.getProductName())) {
				if(!context.configurations.stream().findFirst().get().getSource().equalsIgnoreCase("India")){
					currency = "USD";
				}
				else{
					currency = "INR";
				}
			} else {
				currency = (String) mqUtils.sendAndReceive(currencyQueue, context.configurations.stream().findFirst().get().getSource());

			}
		} catch (Exception e) {
			LOGGER.error("Currency Queue Exception :: {}", e);
		}
		context.inputCurrency = currency.toUpperCase();
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
	 * update {@link QuoteToLe} with converted price
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext updateQuoteToLeCurrencyValues(GscQuoteDetailContext context) {
		QuoteToLe quoteToLe = context.quoteToLe;
		quoteToLe.setFinalArc(omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
				quoteToLe.getFinalArc()));
		quoteToLe.setFinalMrc(omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
				quoteToLe.getFinalMrc()));
		quoteToLe.setFinalNrc(omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
				quoteToLe.getFinalNrc()));
		quoteToLe.setProposedArc(omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
				quoteToLe.getProposedArc()));
		quoteToLe.setProposedMrc(omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
				quoteToLe.getProposedMrc()));
		quoteToLe.setProposedNrc(omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
				quoteToLe.getProposedNrc()));
		quoteToLe.setTotalTcv(omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
				quoteToLe.getTotalTcv()));
		quoteToLeRepository.save(quoteToLe);
		return context;
	}

	/**
	 * Update {@link QuoteGsc} with converted price
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext updateQuoteGscCurrencyValues(GscQuoteDetailContext context) {
		String existingCurrency = context.existingCurrency;
		String inputCurrency = context.inputCurrency;
		List<QuoteGsc> quoteGscList = quoteGscRepository.findByQuoteToLe(context.quoteToLe);
		quoteGscList.forEach(quoteGsc -> {
			quoteGsc.setMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteGsc.getMrc()));
			quoteGsc.setNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteGsc.getNrc()));
			quoteGsc.setArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteGsc.getArc()));
			quoteGsc.setTcv(omsUtilService.convertCurrency(existingCurrency, inputCurrency, quoteGsc.getTcv()));
			quoteGscRepository.save(quoteGsc);
		});
		context.quoteGscs = quoteGscList;
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
		quoteGscDetail.forEach(priceDetail -> {
			priceDetail.setMrc(Utils.setPrecision(omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getMrc()),2));
			priceDetail.setNrc(Utils.setPrecision(omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getNrc()),2));
			priceDetail.setArc(Utils.setPrecision(omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getArc()),2));
			quoteGscDetailsRepository.save(priceDetail);
		});
		return context;
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
	 * Method to get quoteProduct components
	 *
	 * @param quoteToLe
	 * @return {@link List<QuoteProductComponent>}
	 */
	public List<QuoteProductComponent> getGsipCommonQuoteProductComponents(QuoteToLe quoteToLe) {
		return quoteProductComponentRepository.findByReferenceIdAndType(quoteToLe.getQuote().getId(),
				GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE);
	}

	/**
	 * Method to set GVPN Prices attributes
	 *
	 * @param context
	 */
	private GscQuoteDetailContext updateGvpnPriceAttributes(GscQuoteDetailContext context) {
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
						QuoteProductComponentsAttributeValue attributeValue = attributeValueList.stream().findFirst()
								.get();
						processGvpnPrices(context, attributeValue, attribute);
						quoteProductComponentAttributeValuesRepository.save(attributeValue);
					}
				});
			});

		}
		return context;
	}

	/**
	 * Method to process Gvpn Prices
	 *
	 * @param attributeValue
	 * @param attribute
	 */

	private void processGvpnPrices(GscQuoteDetailContext context, QuoteProductComponentsAttributeValue attributeValue,
			ProductAttributeMaster attribute) {
		if (attributeValue.getProductAttributeMaster().getId() == attribute.getId()) {
			switch (attribute.getName()) {
			case GscConstants.GVPN_TOTAL_ARC:
				if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
					Double totalArc = Double.parseDouble(attributeValue.getAttributeValues());
					totalArc = omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
							totalArc);
					attributeValue.setAttributeValues(totalArc.toString());
				}
				break;
			case GscConstants.GVPN_TOTAL_MRC:
				if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
					Double totalMrc = Double.parseDouble(attributeValue.getAttributeValues());
					totalMrc = omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
							totalMrc);
					attributeValue.setAttributeValues(totalMrc.toString());
				}
				break;
			case GscConstants.GVPN_TOTAL_NRC:
				if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
					Double totalNrc = Double.parseDouble(attributeValue.getAttributeValues());
					totalNrc = omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
							totalNrc);
					attributeValue.setAttributeValues(totalNrc.toString());
				}
				break;
			case GscConstants.GVPN_TOTAL_TCV:
				if (StringUtils.isNotBlank(attributeValue.getAttributeValues())) {
					Double totalTcv = Double.parseDouble(attributeValue.getAttributeValues());
					totalTcv = omsUtilService.convertCurrency(context.existingCurrency, context.inputCurrency,
							totalTcv);
					attributeValue.setAttributeValues(totalTcv.toString());
				}
				break;
			default:
				LOGGER.info("other attribute Name- " + attribute.getName());

			}
		}
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
	 * @author Thamizhselvi Perumal Method to update quoteIllSites currency values
	 * @param quote
	 * @param inputCurrency
	 * @param existingCurrency
	 * @throws TclCommonException
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

}
