package com.tcl.dias.oms.gsc.service.v2;

import static com.tcl.dias.oms.constants.QuoteConstants.GVPN_SITES;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_NUMBER_COUNT;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.BILLING_CURRENCY;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CFG_TYPE_REFERENCE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;
import static com.tcl.dias.oms.gsc.util.GscConstants.PAYMENT_CURRENCY;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_NAME_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_DETAIL_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_ID_NULL_MESSAGE;
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
import java.util.HashMap;
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
import com.tcl.dias.common.beans.ResponseResource;
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
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
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
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.webex.beans.DeleteConfigurationBean;
import com.tcl.dias.oms.webex.beans.QuoteUcaasBean;
import com.tcl.dias.oms.webex.service.WebexQuoteService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Services to handle all quote detail related functionality
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class GscQuoteDetailService2 {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscQuoteDetailService2.class);

	@Autowired
	UserService userService;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValuesRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	GscQuoteAttributeService2 gscQuoteAttributeService2;

	@Autowired
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	GscPricingFeasibilityService2 gscPricingFeasibilityService;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Value("${rabbitmq.product.webex.bridge.queue}")
	private String bridgeCountryQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	QuoteUcaasDetailsRepository quoteUcaasDetailsRepository;

	@Autowired
	WebexQuoteService webexQuoteService;

	@Autowired
	OrderGscDetailRepository orderGscDetailRepository;

	@Autowired
	GscOrderDetailService gscOrderDetailService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	GscOrderService gscOrderService;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	/**
	 * Get gsc quote details
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @return {@link GscQuoteConfigurationBean}
	 * @throws TclCommonException
	 */
	public List<GscQuoteConfigurationBean> getGscQuoteDetails(Integer quoteId, Integer solutionId, Integer quoteGscId,
															  Boolean fetchAttributes) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);

		GscQuoteDetailContext context = toContext(quoteId, solutionId, quoteGscId, ImmutableList.of(),
				Optional.ofNullable(fetchAttributes).orElse(Boolean.FALSE));
		populateQuote(context);
		populateQuoteToLe(context);
		populateSolution(context);
		populateQuoteGsc(context);
		fetchQuoteDetails(context);
		return context.configurations;
	}

	/**
	 * fetchQuoteDetails
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext fetchQuoteDetails(GscQuoteDetailContext context) {
		List<QuoteGscDetail> gscDetails = quoteGscDetailsRepository.findByQuoteGsc(context.quoteGsc);
		List<GscQuoteConfigurationBean> configurationBeans = gscDetails.stream()
				.map(GscQuoteConfigurationBean::fromGscQuoteDetail).map(configurationBean -> this
						.populateProductComponents(configurationBean, context.quote, context.fetchAttributes))
				.collect(Collectors.toList());
		context.configurations = configurationBeans;
		return context;
	}

	/**
	 * Populate Product Components.
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
						? productComponent -> gscQuoteAttributeService2
						.populateQuoteProductComponentAttributes(productComponent, quote)
						: GscProductComponentBean::fromQuoteProductComponent)
				.collect(Collectors.toList());
		configurationBean.setProductComponents(components);
		return configurationBean;
	}

	/**
	 * populateQuoteGsc
	 *
	 * @param context
	 * @return context
	 * @throws TclCommonException
	 */
	private GscQuoteDetailContext populateQuoteGsc(GscQuoteDetailContext context) throws TclCommonException {
		try {
			context.quoteGsc = getQuoteGsc(context.quoteGscId);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.QUOTE_GSC_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return context;
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
	 * populateSolution
	 *
	 * @param context
	 * @return context
	 * @throws TclCommonException
	 */
	private GscQuoteDetailContext populateSolution(GscQuoteDetailContext context) throws TclCommonException {
		try {
			context.productSolution = getProductSolution(context.solutionId);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return context;
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
	 * populateQuoteToLe
	 *
	 * @param context
	 * @return context
	 * @throws TclCommonException
	 */
	private GscQuoteDetailContext populateQuoteToLe(GscQuoteDetailContext context) throws TclCommonException {
		try {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(context.quote).stream().findFirst();
			if (Objects.isNull(quoteToLe.get())) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			context.quoteLeId = quoteToLe.get().getId();
			context.quoteToLe = quoteToLe.get();
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return context;
	}

	/**
	 * populateQuote
	 *
	 * @param context
	 * @return context
	 * @throws TclCommonException
	 */
	private GscQuoteDetailContext populateQuote(GscQuoteDetailContext context) throws TclCommonException {
		try {
			context.quote = getQuote(context.quoteId);

		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return context;
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

	/**
	 * toContext
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @param configurations
	 * @return context
	 */
	private GscQuoteDetailContext toContext(Integer quoteId, Integer solutionId, Integer quoteGscId,
											List<GscQuoteConfigurationBean> configurations, Boolean fetchAttributes) {
		GscQuoteDetailContext context = new GscQuoteDetailContext();
		context.quoteId = quoteId;
		context.solutionId = solutionId;
		context.quoteGscId = quoteGscId;
		context.configurations = configurations;
		context.fetchAttributes = fetchAttributes;
		context.customer = userService.getUserId(Utils.getSource()).getCustomer();
		context.user = userService.getUserId(Utils.getSource());
		return context;
	}

	/**
	 * createConfiguration.
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @param data
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public List<GscQuoteConfigurationBean> createConfiguration(Integer quoteId, Integer solutionId, Integer gscQuoteId,
															   List<GscQuoteConfigurationBean> data) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(gscQuoteId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(data, CONFIGURATION_ID_NULL_MESSAGE);
		GscQuoteDetailContext context = toContext(quoteId, solutionId, gscQuoteId, data, false);
		checkAudioModel(context);
		populateQuote(context);
		populateQuoteToLe(context);
		populateSolution(context);
		populateQuoteGsc(context);
		saveQuoteGscDetails(context);
		return context.configurations;
	}

	/**
	 * checkAudioModel
	 *
	 * @param context
	 * @return context
	 *
	 */
	private GscQuoteDetailContext checkAudioModel(GscQuoteDetailContext context) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(context.quoteId).stream().findFirst();
		if (quoteToLe.isPresent()) {
			Optional<QuoteUcaas> quoteUcaas = quoteUcaasRepository.findByQuoteToLeAndIsConfig(quoteToLe.get(), (byte) 1)
					.stream().findAny();

			if (quoteUcaas.isPresent()) {
				if (WebexConstants.SHARED.equals(quoteUcaas.get().getAudioModel())) {
					// Queue call to get bridge country based on bridge.
					String bridgeCountry = (String) mqUtils.sendAndReceive(bridgeCountryQueue,
							quoteUcaas.get().getPrimaryRegion() + "");

					QuoteGsc quoteGsc = quoteGscRepository.findById(context.quoteGscId).get();

					context.configurations.stream().forEach(gscQuoteConfigurationBean -> {
						if (GscConstants.LNS.equals(quoteGsc.getProductName())) {
							gscQuoteConfigurationBean.setSource(WebexConstants.LNS_COUNTRIES);
							gscQuoteConfigurationBean.setDestination(bridgeCountry);
						} else if (GscConstants.GLOBAL_OUTBOUND.equals(quoteGsc.getProductName())) {
							gscQuoteConfigurationBean.setSource(bridgeCountry);
							gscQuoteConfigurationBean.setDestination(
									WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT.equals(quoteUcaas.get().getAudioType())
											? bridgeCountry
											: WebexConstants.GLOBAL_OUTBOUND_COUNTRIES);
						} else {
							gscQuoteConfigurationBean.setSource(WebexConstants.ITFS_COUNTRIES);
							gscQuoteConfigurationBean.setDestination(bridgeCountry);
						}
					});
				}
			}
		}
		return context;
	}

	/**
	 * saveQuoteGscDetails
	 *
	 * @param context
	 * @return context
	 *
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
		context.configurations = configurationBeanMap.entrySet().stream().map(entry -> this
				.saveQuoteGscDetail(context.quoteGsc, gscDetailMap.get(entry.getKey()), entry.getValue(), context.user))
				.map(beanPair -> {
					try {
						return this.createOrUpdateProductComponent(beanPair.keySet().stream().findFirst().get(),
								context.quote, mstProductFamily, context.quoteGsc,
								beanPair.get(beanPair.keySet().stream().findFirst().get()), context.productSolution);
					} catch (TclCommonException e) {
						LOGGER.warn("Exception occured : {}", e.getMessage());
					}
					return null;
				}).collect(Collectors.toList());
		return context;
	}

	/**
	 * saveQuoteGscDetail
	 *
	 * @param quoteGsc
	 * @param quoteGscDetail
	 * @param gscQuoteConfigurationBean
	 * @param user
	 * @return
	 *
	 */
	private Map<GscQuoteConfigurationBean, QuoteGscDetail> saveQuoteGscDetail(QuoteGsc quoteGsc,
																			  QuoteGscDetail quoteGscDetail, GscQuoteConfigurationBean gscQuoteConfigurationBean, User user) {
		Map<GscQuoteConfigurationBean, QuoteGscDetail> beanpair = new HashMap<GscQuoteConfigurationBean, QuoteGscDetail>();
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
		beanpair.put(gscQuoteConfigurationBean, quoteGscDetail);
		return beanpair;
	}

	/**
	 * deleteQuoteGscDetail
	 *
	 * @param quoteGscDetailId
	 * @return QuoteGscDetail
	 *
	 */
	private QuoteGscDetail deleteQuoteGscDetail(Integer quoteGscDetailId) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteGscDetailId, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		productComponents.forEach(productComponent -> {
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValuesRepository
					.findByQuoteProductComponent(productComponent);
			if (!CollectionUtils.isEmpty(attributeValues)) {
				quoteProductComponentsAttributeValuesRepository.deleteAll(attributeValues);
			}
		});
		quoteProductComponentRepository.deleteAll(productComponents);
		return quoteGscDetailsRepository.findById(quoteGscDetailId).map(quoteGscDetail -> {
			quoteGscDetailsRepository.delete(quoteGscDetail);
			return quoteGscDetail;
		}).orElse(null);
	}

	/**
	 * createOrUpdataProductComponent
	 *
	 * @param bean
	 * @param quote
	 * @param mstProductFamily
	 * @param quoteGsc
	 * @param quoteGscDetail
	 * @param solution
	 * @return GscQuoteConfigurationBean
	 * @throws TclCommonException
	 *
	 */
	private GscQuoteConfigurationBean createOrUpdateProductComponent(GscQuoteConfigurationBean bean, Quote quote,
																	 MstProductFamily mstProductFamily, QuoteGsc quoteGsc, QuoteGscDetail quoteGscDetail,
																	 ProductSolution solution) throws TclCommonException {
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
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(quote).stream().findFirst().get();
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
	 * saveProductComponent
	 *
	 * @param referenceId
	 * @return productName
	 * @return quote
	 * @return mstProductFamily
	 * @param componentType
	 * @throws TclCommonException
	 *
	 */
	public QuoteProductComponent saveProductComponent(Integer referenceId, String productName, Quote quote,
													  MstProductFamily mstProductFamily, String componentType) throws TclCommonException {
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
				gscQuoteAttributeService2.processProductComponent(componentBean, quote);
				return savedComponent;
			}
		}
		return savedComponent;
	}

	/**
	 * copyUIFNMACDAttributes
	 *
	 * @param quoteGsc
	 * @param productComponent
	 *
	 *
	 */
	private void copyUIFNMACDAttributes(QuoteGsc quoteGsc, QuoteProductComponent productComponent) {
		quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream()
				.filter(quoteGscDetail -> GSC_CFG_TYPE_REFERENCE.equalsIgnoreCase(quoteGscDetail.getType())).findFirst()
				.flatMap(quoteGscDetail -> {
					MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(UIFN);
					return quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(
							quoteGscDetail.getId(), mstProductComponent, GSC_ORDER_PRODUCT_COMPONENT_TYPE);
				}).ifPresent(referenceComponent -> {
			List<QuoteProductComponentsAttributeValue> values = quoteProductComponentsAttributeValuesRepository
					.findByQuoteProductComponent(referenceComponent);
			cloneIfPresent(values, ATTR_QUANTITY_OF_NUMBERS, productComponent)
					.ifPresent(quoteProductComponentsAttributeValuesRepository::save);
			cloneIfPresent(values, ATTR_PORTING_SERVICE_NEEDED, productComponent)
					.ifPresent(quoteProductComponentsAttributeValuesRepository::save);
			cloneIfPresent(values, ATTR_PORTING_NUMBER_COUNT, productComponent)
					.ifPresent(quoteProductComponentsAttributeValuesRepository::save);
		});
	}

	/**
	 * cloneIfPresent
	 *
	 * @param attributeValues
	 * @param attributeName
	 * @param newComponent
	 * @return
	 *
	 */
	private Optional<QuoteProductComponentsAttributeValue> cloneIfPresent(
			List<QuoteProductComponentsAttributeValue> attributeValues, String attributeName,
			QuoteProductComponent newComponent) {
		return Optional.ofNullable(attributeValues).orElse(ImmutableList.of()).stream()
				.filter(value -> value.getProductAttributeMaster().getName().equalsIgnoreCase(attributeName))
				.findFirst().map(value -> {
					List<QuoteProductComponentsAttributeValue> existingValues = quoteProductComponentsAttributeValuesRepository
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

	/**
	 * getDefaultAttributes
	 *
	 * @param productComponentName
	 * @return List<GscQuoteProductComponentsAttributeValueBean>
	 *
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
		List<Integer> configurationIds;
		MstOmsAttribute mstOmsAttribute;
		QuoteLeAttributeValue quoteToLeAttribute;
		DeleteConfigurationBean deleteConfigurations;
		String dealId;
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
	public List<GscQuoteConfigurationBean> deleteConfiguration(Integer quoteId, Integer solutionId, Integer quoteGscId,
															   List<GscQuoteConfigurationBean> data) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(data, CONFIGURATION_ID_NULL_MESSAGE);
		GscQuoteDetailContext context = toContext(quoteId, solutionId, quoteGscId, data, false);
		populateQuote(context);
		populateQuoteToLe(context);
		populateSolution(context);
		populateQuoteGsc(context);
		deleteGscQuoteDetails(context);
		return context.configurations;
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
	 * create Context
	 *
	 * @param quoteId
	 * @param quoteLeId
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
	 * Convert Currency
	 *
	 * @param context
	 * @return
	 */
	@Transactional
	public GscQuoteDetailContext convertCurrencyByCode(GscQuoteDetailContext context) {
//		Method removed as it returns redundant context
//		createCurrencyContext(context);
		findExistingCurrency(context);
		convertPricesBasedOnCurrency(context);
		updatePaymentCurrencyValue(context);
		updatePaymentCurrencyValueInQuoteLeAttributes(context);
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
				quotePrice.setCatalogArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getCatalogArc(), 2)));
			if (Objects.nonNull(quotePrice.getCatalogMrc()))
				quotePrice.setCatalogMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getCatalogMrc(), 2)));
			if (Objects.nonNull(quotePrice.getCatalogNrc()))
				quotePrice.setCatalogNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getCatalogNrc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedArc()))
				quotePrice.setComputedArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getComputedArc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedMrc()))
				quotePrice.setComputedMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getComputedMrc(), 2)));
			if (Objects.nonNull(quotePrice.getComputedNrc()))
				quotePrice.setComputedNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getComputedNrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveArc()))
				quotePrice.setEffectiveArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getEffectiveArc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveMrc()))
				quotePrice.setEffectiveMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getEffectiveMrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveNrc()))
				quotePrice.setEffectiveNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getEffectiveNrc(), 2)));
			if (Objects.nonNull(quotePrice.getEffectiveUsagePrice()))
				quotePrice.setEffectiveUsagePrice(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getEffectiveUsagePrice(), 2)));
			if (Objects.nonNull(quotePrice.getMinimumArc()))
				quotePrice.setMinimumArc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getMinimumArc(), 2)));
			if (Objects.nonNull(quotePrice.getMinimumMrc()))
				quotePrice.setMinimumMrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getMinimumMrc(), 2)));
			if (Objects.nonNull(quotePrice.getMinimumNrc()))
				quotePrice.setMinimumNrc(omsUtilService.convertCurrency(existingCurrency, inputCurrency,
						Utils.setPrecision(quotePrice.getMinimumNrc(), 2)));
			quotePriceRepository.save(quotePrice);
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
		/*
		 * List<QuoteProductComponent> quoteProductComponents =
		 * quoteProductComponentRepository
		 * .findByReferenceIdAndReferenceName(illSite.getId(),QuoteConstants.GSC.
		 * toString());
		 */

		/* Changing the reference name to gvpn_sites since it is dealing with illsite */
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(illSite.getId(), GVPN_SITES.toString());

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
						LOGGER.info("Quote product component for site id {} is {} ", component.getId(),
								illSite.getId());
						QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
								component.getId().toString(), QuoteConstants.COMPONENTS.toString());
						LOGGER.info("Quote price for reference id (product component) {} is  {} ", component.getId(),
								attrPrice.getId());
						totalMrc = totalMrc + attrPrice.getEffectiveMrc();
						totalNrc = totalNrc + attrPrice.getEffectiveNrc();
						totalArc = totalArc + attrPrice.getEffectiveArc();
					}

					illSite.setArc(Utils.setPrecision(totalArc, 2));
					illSite.setMrc(Utils.setPrecision(totalMrc, 2));
					illSite.setNrc(Utils.setPrecision(totalNrc, 2));
					Double contractTerm = Double.parseDouble(this.setTermInMonths(context.quoteToLe));
					Double tcv = (contractTerm * Utils.setPrecision(illSite.getMrc(), 2))
							+ Utils.setPrecision(illSite.getNrc(), 2);
					illSite.setTcv(tcv);
					illSiteRepository.save(illSite);
					LOGGER.info("Prices for site id {} is mrc : {}, nrc : {}, arc :{}, tcv :{}", illSite.getId(),
							totalMrc, totalNrc, totalArc, tcv);
				});
			});
		});

		return context;
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
		if (termPeriod.contains("month")) {
			return month;
		} else {
			String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
			if (reg.length > 0) {
				if (StringUtils.isNumeric(reg[0])) {
					month = Integer.valueOf(reg[0]);
					month = month * 12;
				}
			}
		}
		return month;
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
	 * Method to update quoteIllSites currency values
	 *
	 * @param
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
						QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
								component.getId().toString(), QuoteConstants.COMPONENTS.toString());
						siteTotalMrc = Utils.setPrecision(siteTotalMrc + attrPrice.getEffectiveMrc(), 2);
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
			if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())
					|| quoteToLe.getQuote().getQuoteCode().startsWith(WebexConstants.UCAAS_WEBEX)) {
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
	 * Method to convert prices into another currency values
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext convertPricesBasedOnCurrency(GscQuoteDetailContext context) {
		if (!context.inputCurrency.equalsIgnoreCase(context.existingCurrency)) {
			updateQuotePriceCurrencyValues(context);
			QuoteToLe quoteToLe = updateQuoteIllSitesCurrencyValues(context.quoteToLe, context.inputCurrency,
					context.existingCurrency);
			gscPricingFeasibilityService.persistGvpnPricesWithGsc(quoteToLe);
			updateQuoteGscDetailCurrencyValues(context);
			computeGscCurrencyValues(context);
			updateQuoteUcaasDetailCurrencyValues(context);
			Double contractTerm = Double.parseDouble(this.setTermInMonths(context.quoteToLe));
			gscPricingFeasibilityService.updateQuoteToLeCurrencyValues(context.quoteToLe, context.quoteGscs,
					contractTerm, context.existingCurrency, context.inputCurrency);
		}
		return context;
	}

	/**
	 * Method to compute ucaas currency values
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext updateQuoteUcaasDetailCurrencyValues(GscQuoteDetailContext context) {
		List<QuoteUcaas> quoteUcaas = quoteUcaasRepository.findByQuoteToLeId(context.quoteToLe.getId());
		if (!quoteUcaas.isEmpty()) {
			quoteUcaas.forEach(ucaasQuote -> {
				if (Objects.nonNull(ucaasQuote.getUnitMrc())) {
					ucaasQuote.setUnitMrc(Utils.setPrecision(omsUtilService.convertCurrency(context.existingCurrency,
							context.inputCurrency, ucaasQuote.getUnitMrc()), 2));
				}

				if (Objects.nonNull(ucaasQuote.getUnitNrc())) {
					ucaasQuote.setUnitNrc(Utils.setPrecision(omsUtilService.convertCurrency(context.existingCurrency,
							context.inputCurrency, ucaasQuote.getUnitNrc()), 2));
				}

				if (Objects.nonNull(ucaasQuote.getMrc())) {
					ucaasQuote.setMrc(Utils.setPrecision(omsUtilService.convertCurrency(context.existingCurrency,
							context.inputCurrency, ucaasQuote.getMrc()), 2));
				}
				if (Objects.nonNull(ucaasQuote.getNrc())) {
					ucaasQuote.setNrc(Utils.setPrecision(omsUtilService.convertCurrency(context.existingCurrency,
							context.inputCurrency, ucaasQuote.getNrc()), 2));
				}
				if (Objects.nonNull(ucaasQuote.getTcv())) {
					ucaasQuote.setTcv(Utils.setPrecision(omsUtilService.convertCurrency(context.existingCurrency,
							context.inputCurrency, ucaasQuote.getTcv()), 2));
				}
				if (Objects.nonNull(ucaasQuote.getArc())) {
					ucaasQuote.setArc(Utils.setPrecision(omsUtilService.convertCurrency(context.existingCurrency,
							context.inputCurrency, ucaasQuote.getArc()), 2));
				}
				quoteUcaasRepository.save(ucaasQuote);
			});
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
		quoteGscDetail.forEach(priceDetail -> {
			priceDetail.setMrc(Utils.setPrecision(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getMrc()), 2));
			priceDetail.setNrc(Utils.setPrecision(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getNrc()), 2));
			priceDetail.setArc(Utils.setPrecision(
					omsUtilService.convertCurrency(existingCurrency, inputCurrency, priceDetail.getArc()), 2));
			quoteGscDetailsRepository.save(priceDetail);
		});
		return context;
	}

	/**
	 * update the quoteToLe for currency code
	 *
	 * @param context
	 * @return GscQuoteDetailContext
	 */
	private GscQuoteDetailContext updatePaymentCurrencyValue(GscQuoteDetailContext context) {
		QuoteToLe quoteToLe = context.quoteToLe;
		quoteToLe.setCurrencyCode(context.inputCurrency);
		quoteToLeRepository.save(quoteToLe);
		return context;
	}

	/**
	 * Convert Currency By given payment currency
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 */
	@Transactional
	public String updateCurrencyValueByCode(Integer quoteId, Integer quoteLeId, String paymentCurrency)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteLeId, QUOTE_LE_ID_NULL_MESSAGE);
		GscQuoteDetailContext context = toCurrencyContext(quoteId, quoteLeId, paymentCurrency);
		populateQuote(context);
		populateQuoteToLe(context);
		populateQuoteGscByQuoteLe(context);
		convertCurrencyByCode(context);
		return context.inputCurrency;
	}

	/**
	 * toContext
	 *
	 * @param quoteId
	 * @param configurations
	 * @return context
	 */
	private GscQuoteDetailContext configurationContext(Integer quoteId, DeleteConfigurationBean configurations,
													   Boolean fetchAttributes) {
		GscQuoteDetailContext context = new GscQuoteDetailContext();
		context.quoteId = quoteId;
		context.deleteConfigurations = configurations;
		context.configurationIds = configurations.getConfigurationIds();
		context.dealId = configurations.getDealId();
		context.fetchAttributes = fetchAttributes;
		context.customer = userService.getUserId(Utils.getSource()).getCustomer();
		context.user = userService.getUserId(Utils.getSource());
		return context;
	}

	/**
	 * deleteGscQuoteDetails
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext deleteGscQuoteDetailsById(GscQuoteDetailContext context) {
		List<GscQuoteConfigurationBean> deletedConfigurations = context.configurationIds.stream()
				.map(this::deleteQuoteGscDetail).filter(Objects::nonNull)
				.map(GscQuoteConfigurationBean::fromGscQuoteDetail).collect(Collectors.toList());
		if (Objects.nonNull(deletedConfigurations) && !deletedConfigurations.isEmpty())
			context.deleteConfigurations.setGscQuoteConfigurations(deletedConfigurations);
		return context;
	}

	/**
	 * Delete quote related configurations
	 *
	 * @param quoteId
	 * @param configurationIds
	 * @return {@link DeleteConfigurationBean}
	 */
	@Transactional
	public DeleteConfigurationBean deleteConfigurations(Integer quoteId, DeleteConfigurationBean configurationIds)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(configurationIds, CONFIGURATION_ID_NULL_MESSAGE);
		GscQuoteDetailContext context = configurationContext(quoteId, configurationIds, false);
		populateQuote(context);
		populateQuoteToLe(context);
		deleteGscQuoteDetailsById(context);
		deleteUcaasQuotes(context);
		return context.deleteConfigurations;
	}

	/**
	 * Method to update the quoteToLeAttributes
	 *
	 * @param context
	 * @return GscQuoteDetailContext
	 */
	private GscQuoteDetailContext updatePaymentCurrencyValueInQuoteLeAttributes(GscQuoteDetailContext context) {
		mstOmsAttributeRepository.findByNameAndIsActive(PAYMENT_CURRENCY, STATUS_ACTIVE).stream().findFirst()
				.map(mstOmsAttribute -> {
					QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(context.quoteToLe, mstOmsAttribute).stream().findFirst()
							.get();
					quoteLeAttributeValue.setAttributeValue(context.inputCurrency);
					quoteLeAttributeValue.setQuoteToLe(context.quoteToLe);
					quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
					return quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
				});

		mstOmsAttributeRepository.findByNameAndIsActive(BILLING_CURRENCY, STATUS_ACTIVE).stream().findFirst()
				.map(mstOmsAttribute -> {
					QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(context.quoteToLe, mstOmsAttribute).stream().findFirst()
							.get();
					quoteLeAttributeValue.setAttributeValue(context.inputCurrency);
					quoteLeAttributeValue.setQuoteToLe(context.quoteToLe);
					quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
					return quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
				});

		return context;
	}

	/**
	 * Get UCaaS quotes from repository
	 *
	 * @param context
	 * @param dealId
	 * @return
	 */
	public List<QuoteUcaas> getUcaasQuotes(GscQuoteDetailContext context, Integer dealId) {
		List<QuoteUcaas> ucaasQuotes = quoteUcaasRepository
				.findByQuoteToLeIdAndIsConfig(context.quoteToLe.getId(), (byte) 0).stream()
				.filter(quoteUcaas -> dealId.equals(quoteUcaas.getDealId())).collect(Collectors.toList());
		return ucaasQuotes;
	}

	/**
	 * Get UCaaS Configuration from repository
	 *
	 * @param context
	 * @param dealId
	 * @return
	 */
	public QuoteUcaas getUcaasConfiguration(GscQuoteDetailContext context, Integer dealId) {
		QuoteUcaas ucaasConfiguration = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(
				context.quoteToLe.getId(), WebexConstants.CONFIGURATION, CommonConstants.BACTIVE);
		return ucaasConfiguration;
	}

	/**
	 * Delete UCaaS Quotes from repository
	 *
	 * @param context
	 * @return
	 */
	public GscQuoteDetailContext deleteUcaasQuotes(GscQuoteDetailContext context) {
		if (Objects.nonNull(context.dealId) && StringUtils.isNotBlank(context.dealId)) {
			QuoteUcaas ucaasConfiguration = getUcaasConfiguration(context, Integer.parseInt(context.dealId));
			List<QuoteUcaas> ucaasQuotes = getUcaasQuotes(context, Integer.parseInt(context.dealId));
			webexQuoteService.deleteAllQuoteUcaasDetailsAndSiteDetails(context.quoteToLe);
			if (Objects.nonNull(ucaasConfiguration) && Objects.nonNull(ucaasQuotes)) {
				// making dealid, status, message, mrc, nrc, tcv to null as all license
				// components are deleted
				context.deleteConfigurations.setUcaasQuotes(
						ucaasQuotes.stream().map(QuoteUcaasBean::toQuoteUcaasBean).collect(Collectors.toList()));

				ucaasConfiguration.setQuantity(
						Objects.nonNull(ucaasConfiguration.getQuantity()) ? null : ucaasConfiguration.getQuantity());
				ucaasConfiguration.setDealId(
						Objects.nonNull(ucaasConfiguration.getDealId()) ? null : ucaasConfiguration.getDealId());
				ucaasConfiguration.setDeal_status(Objects.nonNull(ucaasConfiguration.getDeal_status()) ? null
						: ucaasConfiguration.getDeal_status());
				ucaasConfiguration.setDeal_message(Objects.nonNull(ucaasConfiguration.getDeal_message()) ? null
						: ucaasConfiguration.getDeal_message());
				ucaasConfiguration.setDealAttributes(Objects.nonNull(ucaasConfiguration.getDealAttributes()) ? null
						: ucaasConfiguration.getDealAttributes());
				ucaasConfiguration
						.setMrc(Objects.nonNull(ucaasConfiguration.getMrc()) ? null : ucaasConfiguration.getMrc());
				ucaasConfiguration
						.setNrc(Objects.nonNull(ucaasConfiguration.getNrc()) ? null : ucaasConfiguration.getNrc());
				ucaasConfiguration
						.setArc(Objects.nonNull(ucaasConfiguration.getArc()) ? null : ucaasConfiguration.getArc());
				ucaasConfiguration
						.setTcv(Objects.nonNull(ucaasConfiguration.getTcv()) ? null : ucaasConfiguration.getTcv());

				quoteUcaasRepository.save(ucaasConfiguration);

				// delete all line items of license component
				quoteUcaasRepository.deleteAll(ucaasQuotes);
			}
		}
		return context;
	}

	/**
	 * Method to get all the available numbers by hitting tiger.
	 *
	 * @param configurationId
	 * @param city
	 * @param count
	 * @param autoReserve
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public List<GscTfnBean> getAvailableNumbers(Integer configurationId, String city, Integer count,
												Boolean autoReserve) throws TclCommonException {
		LOGGER.info("Configuration ID :: {}", configurationId);
		LOGGER.info("City :: {}", city);
		LOGGER.info("Count :: {}", count);
		LOGGER.info("AutoReserve :: {}", autoReserve);
		Objects.requireNonNull(configurationId, "Configuration Id cannot be null");
		Objects.requireNonNull(count, "Count cannot be null");
		Objects.requireNonNull(autoReserve, "Auto reserve must be specified as true/false");
		OrderGscDetail orderGscDetail = fetchOrderGscDetailById(configurationId);
		List<GscTfnBean> gscTfnBeans = null;
		try {
			gscTfnBeans = gscOrderDetailService.getNumbersForConfiguration(orderGscDetail, city, count, autoReserve);
		} catch (Exception e) {
			LOGGER.error("Error in getting numbers ", e);
		}
		return gscTfnBeans;
	}

	/**
	 * Method to fetch orderGscDetail by id.
	 *
	 * @param orderGscDetailId
	 * @return
	 * @throws TclCommonException
	 */
	private OrderGscDetail fetchOrderGscDetailById(Integer orderGscDetailId) throws TclCommonException {
		LOGGER.info("OrderGscDetail ID :: {}", orderGscDetailId);
		Optional<OrderGscDetail> optionalOrderGscDetail = orderGscDetailRepository.findById(orderGscDetailId);
		if (!optionalOrderGscDetail.isPresent()) {
			throw new TclCommonException(ExceptionConstants.GSC_ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return optionalOrderGscDetail.get();
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
															   Integer quoteGscDetailId, GscQuoteConfigurationBean data) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscDetailId, QUOTE_GSC_DETAIL_ID_NULL_MESSAGE);
		Objects.requireNonNull(data, CONFIGURATION_ID_NULL_MESSAGE);
		GscQuoteDetailContext context = toContext(quoteId, solutionId, quoteGscId, ImmutableList.of(data), false);
		context.quoteGscDetailId = quoteGscDetailId;
		populateQuote(context);
		populateQuoteToLe(context);
		populateSolution(context);
		populateQuoteGsc(context);
		populateQuoteGscDetail(context);
		saveQuoteGscDetails(context);
		return context.configurations;
	}

	/**
	 * populateQuoteGscDetail
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteDetailContext populateQuoteGscDetail(GscQuoteDetailContext context) throws TclCommonException {
		QuoteGscDetail quoteGscDetail = getQuoteGscDetail(context.quoteGscDetailId);
		context.quoteGscDetail = quoteGscDetail;
		return context;
	}
}
