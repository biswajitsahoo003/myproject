package com.tcl.dias.oms.gsc.service.v2;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_NOT_FOUND;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.oms.gsc.util.GscConstants.ATTRIBUTES_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_COMPONENT_NUll_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_LE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.SOLUTION_ID_NULL_MESSAGE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscContactAttributeInfo;
import com.tcl.dias.oms.gsc.beans.GscDocumentBean;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteAttributesBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.common.GscOmsSfdcComponent;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.webex.common.UcaasOmsSfdcComponent;
import com.tcl.dias.oms.webex.service.WebexQuoteService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Services to handle all quote attributes related functionality
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class GscQuoteAttributeService2 {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscQuoteAttributeService2.class);

	@Value("${rabbitmq.customerleattr.product.queue}")
	String customerLeAttrQueueProduct;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

	@Autowired
	GscQuoteDetailService2 gscQuoteDetailService2;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	GscQuoteService2 gscQuoteService;

	@Autowired
	GscQuoteDetailService2 gscQuoteDetailService;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	GscOmsSfdcComponent gscOmsSfdcComponent;

	@Autowired
	WebexQuoteService webexQuoteService;

	@Autowired
	UcaasOmsSfdcComponent ucaasOmsSfdcComponent;

	@Value("${info.customer_le_location_queue}")
	String customerLeLocationQueue;

	@Autowired
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	CreditCheckService creditCheckService;

	private List<GscProductComponentBean> gscProductComponentBeansResponse;

	/**
	 * Populate quote product component attributes for a given product component
	 *
	 * @param productComponent
	 * @param quote
	 * @return
	 */
	public GscProductComponentBean populateQuoteProductComponentAttributes(QuoteProductComponent productComponent,
																		   Quote quote) {
		GscProductComponentBean componentBean = GscProductComponentBean.fromQuoteProductComponent(productComponent);
		List<QuoteProductComponentsAttributeValue> productComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent(productComponent);
		List<GscQuoteProductComponentsAttributeSimpleValueBean> attributes = productComponentsAttributeValues.stream()
				.map(GscQuoteProductComponentsAttributeSimpleValueBean::fromProductComponentAttributeValue)
				.collect(Collectors.toList());
		componentBean.setAttributes(groupAndConvertToValueBeans(attributes));
		return componentBean;
	}

	/**
	 * Group and convert to value beans.
	 *
	 * @param simpleValueBeans
	 * @return
	 */
	private List<GscQuoteProductComponentsAttributeValueBean> groupAndConvertToValueBeans(
			List<GscQuoteProductComponentsAttributeSimpleValueBean> simpleValueBeans) {
		Map<String, List<GscQuoteProductComponentsAttributeSimpleValueBean>> groupedAttributes = simpleValueBeans
				.stream()
				.collect(Collectors.groupingBy(GscQuoteProductComponentsAttributeSimpleValueBean::getAttributeName));
		return groupedAttributes.values().stream().map(values -> {
			if (values.size() == 1) {
				return values.get(0);
			} else {
				GscQuoteProductComponentsAttributeSimpleValueBean first = values.get(0);
				List<String> attributeValues = values.stream()
						.map(GscQuoteProductComponentsAttributeSimpleValueBean::getAttributeValue)
						.collect(Collectors.toList());
				GscQuoteProductComponentsAttributeArrayValueBean bean = new GscQuoteProductComponentsAttributeArrayValueBean();
				bean.setAttributeId(first.getAttributeId());
				bean.setAttributeName(first.getAttributeName());
				bean.setDescription(first.getDescription());
				bean.setDisplayValue(Joiner.on(",").join(attributeValues));
				bean.setAttributeValue(attributeValues);
				return bean;
			}
		}).collect(Collectors.toList());
	}

	/**
	 * Process product component
	 *
	 * @param componentBean
	 * @param quote
	 * @return {@link GscProductComponentBean}
	 */
	public GscProductComponentBean processProductComponent(GscProductComponentBean componentBean, Quote quote)
			throws TclCommonException {
		Objects.requireNonNull(componentBean, PRODUCT_COMPONENT_NUll_MESSAGE);
		Objects.requireNonNull(quote, QUOTE_NULL_MESSAGE);
		return quoteProductComponentRepository.findById(componentBean.getId())
				.map(productComponent -> processProductComponent(componentBean, productComponent, quote))
				.orElseThrow(() -> new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, R_CODE_NOT_FOUND));

	}

	/**
	 * processProductComponent
	 *
	 * @param componentBean
	 * @param productComponent
	 * @param quote
	 * @return {@link GscProductComponentBean}
	 *
	 */
	private GscProductComponentBean processProductComponent(GscProductComponentBean componentBean,
															QuoteProductComponent productComponent, Quote quote) {
		List<QuoteProductComponentsAttributeValue> savedSimpleAttributes = componentBean.getAttributes().stream()
				.filter(attributeValue -> attributeValue instanceof GscQuoteProductComponentsAttributeSimpleValueBean)
				.map(attribute -> (GscQuoteProductComponentsAttributeSimpleValueBean) attribute).map(attribute -> {
					try {
						return saveQuoteComponentAttributeValue(productComponent, attribute);
					} catch (TclCommonException e) {
						LOGGER.warn("Exception occured : {}", e.getMessage());
					}
					return null;
				}).collect(Collectors.toList());
		// save array attributes
		List<QuoteProductComponentsAttributeValue> savedArrayAttributeValues = componentBean.getAttributes().stream()
				.filter(attributeValue -> attributeValue instanceof GscQuoteProductComponentsAttributeArrayValueBean)
				.map(attribute -> (GscQuoteProductComponentsAttributeArrayValueBean) attribute).map(bean -> {
					try {
						return handleArrayAttribute(productComponent, bean, quote);
					} catch (TclCommonException e) {
						LOGGER.warn("Exception occured : {}", e.getMessage());
					}
					return null;
				}).flatMap(List::stream).collect(Collectors.toList());
		savedSimpleAttributes.addAll(savedArrayAttributeValues);
		List<GscQuoteProductComponentsAttributeSimpleValueBean> attributes = savedSimpleAttributes.stream()
				.map(GscQuoteProductComponentsAttributeSimpleValueBean::fromProductComponentAttributeValue)
				.collect(Collectors.toList());
		componentBean.setAttributes(groupAndConvertToValueBeans(attributes));
		return componentBean;
	}

	/**
	 * saveQuoteComponentAttributeValue
	 *
	 * @param quoteProductComponent
	 * @param bean
	 * @return {@link QuoteProductComponentsAttributeValue}
	 * @throws TclCommonException
	 *
	 */
	private QuoteProductComponentsAttributeValue saveQuoteComponentAttributeValue(
			QuoteProductComponent quoteProductComponent, GscQuoteProductComponentsAttributeSimpleValueBean bean)
			throws TclCommonException {
		return productAttributeMasterRepository.findByNameAndStatus(bean.getAttributeName(), GscConstants.STATUS_ACTIVE)
				.stream().findFirst().map(productAttributeMaster -> {
					QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent,
									productAttributeMaster)
							.stream().findFirst().orElse(new QuoteProductComponentsAttributeValue());
					attributeValue.setAttributeValues(bean.getAttributeValue());
					attributeValue.setDisplayValue(bean.getAttributeValue());
					attributeValue.setQuoteProductComponent(quoteProductComponent);
					attributeValue.setProductAttributeMaster(productAttributeMaster);
					return quoteProductComponentsAttributeValueRepository.save(attributeValue);
				}).orElseThrow(() -> new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, R_CODE_NOT_FOUND));
	}

	/**
	 * handleArrayAttribute
	 *
	 * @param productComponent
	 * @param arrayAttribute
	 * @param quote
	 * @return {@link List<QuoteProductComponentsAttributeValue>}
	 *
	 */
	private List<QuoteProductComponentsAttributeValue> handleArrayAttribute(QuoteProductComponent productComponent,
																			GscQuoteProductComponentsAttributeArrayValueBean arrayAttribute, Quote quote) throws TclCommonException {
		return productAttributeMasterRepository
				.findByNameAndStatus(arrayAttribute.getAttributeName(), GscConstants.STATUS_ACTIVE).stream().findFirst()
				.map(productAttributeMaster -> {
					List<QuoteProductComponentsAttributeValue> values = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponentAndProductAttributeMaster(productComponent,
									productAttributeMaster);
					// delete all existing attributes for that name
					quoteProductComponentsAttributeValueRepository.deleteAll(values);
					List<QuoteProductComponentsAttributeValue> attributeValues = arrayAttribute.getAttributeValue()
							.stream()
							.map(attributeValue -> createQuoteProductComponentsAttributeValues(arrayAttribute, quote,
									productComponent, productAttributeMaster, attributeValue))
							.collect(Collectors.toList());
					// save all new attributes
					return quoteProductComponentsAttributeValueRepository.saveAll(attributeValues);
				}).orElseThrow(() -> new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, R_CODE_NOT_FOUND));
	}

	/**
	 * createQuoteProductComponentsAttributeValues
	 *
	 * @param attributeValueBean
	 * @param quote
	 * @param productComponent
	 * @param attributeMaster
	 * @param attributeValue
	 * @return {@link QuoteProductComponentsAttributeValue}
	 *
	 */
	private QuoteProductComponentsAttributeValue createQuoteProductComponentsAttributeValues(
			GscQuoteProductComponentsAttributeValueBean attributeValueBean, Quote quote,
			QuoteProductComponent productComponent, ProductAttributeMaster attributeMaster, String attributeValue) {
		QuoteProductComponentsAttributeValue value = new QuoteProductComponentsAttributeValue();
		value.setId(attributeValueBean.getAttributeId());
		value.setAttributeValues(attributeValue);
		value.setDisplayValue(attributeValue);
		value.setQuoteProductComponent(productComponent);
		value.setProductAttributeMaster(attributeMaster);
		return value;
	}

	/**
	 * Create/ Update / delete product component attributes by given IDs
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param quoteGscId
	 * @param configurationId
	 * @param attributes
	 * @return {@link GscProductComponentBean}
	 */
	@Transactional
	public List<GscProductComponentBean> updateOrDeleteProductComponentAttributes(Integer quoteId, Integer solutionId,
																				  Integer quoteGscId, Integer configurationId, List<GscProductComponentBean> attributes)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);

		// validate whether solution/quote_gsc/quote_gsc_detail exists or not.
		gscQuoteDetailService2.getProductSolution(solutionId);
		gscQuoteDetailService2.getQuoteGsc(quoteGscId);
		gscQuoteDetailService2.getQuoteGscDetail(configurationId);

		GscProductComponentAttributeValueContext context = toContext(quoteId, ImmutableList.of(), attributes,
				quoteGscId);
		populateQuote(context);
		updateQuoteProductComponentAttributes(context);
		return context.componentBeans;
	}

	/**
	 * @param quoteId
	 * @param productComponents
	 * @param components
	 * @return
	 */
	private GscProductComponentAttributeValueContext toContext(Integer quoteId,
															   List<QuoteProductComponent> productComponents, List<GscProductComponentBean> components,
															   Integer quoteGscId) {
		GscProductComponentAttributeValueContext context = new GscProductComponentAttributeValueContext();
		context.productComponents = productComponents;
		context.componentBeans = components;
		context.quoteId = quoteId;
		context.quoteGscId = quoteGscId;
		return context;
	}

	/**
	 * GscProductComponentAttributeValueContext
	 *
	 */
	private static class GscProductComponentAttributeValueContext {
		List<QuoteProductComponent> productComponents;
		Integer quoteId;
		Quote quote;
		Integer quoteGscId;
		List<GscProductComponentBean> componentBeans;
	}

	/**
	 * Wrapper method for getQuote()
	 *
	 * @param context
	 * @return
	 */
	private GscProductComponentAttributeValueContext populateQuote(GscProductComponentAttributeValueContext context)
			throws TclCommonException {
		context.quote = gscQuoteDetailService2.getQuote(context.quoteId);
		return context;
	}

	/**
	 * action method to perform update/delete/create attribute values
	 *
	 * @param context
	 * @return
	 */
	private GscProductComponentAttributeValueContext updateQuoteProductComponentAttributes(
			GscProductComponentAttributeValueContext context) {
		context.componentBeans = context.componentBeans.stream().map(componentBean -> {
			GscProductComponentBean gscProductComponentBean = null;
			try {
				gscProductComponentBean = processProductComponent(componentBean, context.quote);
			} catch (TclCommonException e) {
				LOGGER.warn("Exception occured : {}", e.getMessage());
			}
			return gscProductComponentBean;
		}).collect(Collectors.toList());
		return context;
	}

	/**
	 * fetch product component based on configuration id
	 *
	 * @param configurationId
	 * @return
	 */
	private List<QuoteProductComponent> fetchProductComponents(Integer configurationId) throws TclCommonException {
		List<QuoteProductComponent> components = quoteProductComponentRepository
				.findByReferenceIdAndType(configurationId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		if (components.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		} else {
			return components;
		}
	}

	/**
	 * This fetches the component attributes based on product component
	 *
	 * @param context
	 * @return
	 */
	private GscProductComponentAttributeValueContext fetchQuoteProductComponentAttributes(
			GscProductComponentAttributeValueContext context) {
		context.componentBeans = context.productComponents.stream()
				.map(productComponent -> populateQuoteProductComponentAttributes(productComponent, context.quote))
				.collect(Collectors.toList());

		return context;
	}

	/**
	 * Get product component attributes
	 *
	 * @param quoteId
	 * @param quoteGscId
	 * @param configurationId
	 * @return {@link GscProductComponentBean}
	 */
	public List<GscProductComponentBean> getProductComponentAttributes(Integer quoteId, Integer quoteGscId,
																	   Integer configurationId) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(configurationId, CONFIGURATION_ID_NULL_MESSAGE);
		gscQuoteDetailService2.getQuoteGsc(quoteGscId);
		gscQuoteDetailService2.getQuoteGscDetail(configurationId);
		GscProductComponentAttributeValueContext context = toContext(quoteId, fetchProductComponents(configurationId),
				null, quoteGscId);
		populateQuote(context);
		fetchQuoteProductComponentAttributes(context);
		return context.componentBeans;

	}

	/**
	 * Quote legal entity attribute values
	 *
	 * @param quoteToLe
	 * @param bean
	 * @return
	 */
	private QuoteLeAttributeValue saveQuoteToLeAttributeValue(QuoteToLe quoteToLe,
															  GscQuoteProductComponentsAttributeSimpleValueBean bean) throws TclCommonException {
		return mstOmsAttributeRepository.findByNameAndIsActive(bean.getAttributeName(), GscConstants.STATUS_ACTIVE)
				.stream().findFirst().map(mstOmsAttribute -> {
					QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute).stream().findFirst()
							.orElse(new QuoteLeAttributeValue());
					quoteLeAttributeValue.setAttributeValue(bean.getAttributeValue());
					if (Objects.nonNull(bean.getDisplayValue())) {
						quoteLeAttributeValue.setDisplayValue(bean.getDisplayValue());
					}
					quoteLeAttributeValue.setQuoteToLe(quoteToLe);
					quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
					return quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
				}).orElseThrow(() -> new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, R_CODE_NOT_FOUND));
	}

	/**
	 * Creates quote to le attribute value
	 *
	 * @param bean
	 * @param quote
	 * @param quoteToLe
	 * @param mstOmsAttribute
	 * @param attributeValue
	 * @return
	 */
	private QuoteLeAttributeValue createQuoteLeAttributeValue(GscQuoteProductComponentsAttributeValueBean bean,
															  Quote quote, QuoteToLe quoteToLe, MstOmsAttribute mstOmsAttribute, String attributeValue) {
		QuoteLeAttributeValue value = new QuoteLeAttributeValue();
		value.setMstOmsAttribute(mstOmsAttribute);
		value.setAttributeValue(attributeValue);
		value.setDisplayValue(bean.getDisplayValue());
		value.setQuoteToLe(quoteToLe);
		value.setId(bean.getAttributeId());
		return value;
	}

	/**
	 * Handle Array Attribute
	 *
	 * @param arrayAttribute
	 * @param quote
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	private List<QuoteLeAttributeValue> handleArrayAttribute(
			GscQuoteProductComponentsAttributeArrayValueBean arrayAttribute, Quote quote, QuoteToLe quoteToLe)
			throws TclCommonException {
		return mstOmsAttributeRepository
				.findByNameAndIsActive(arrayAttribute.getAttributeName(), GscConstants.STATUS_ACTIVE).stream()
				.findFirst().map(mstOmsAttribute -> {
					List<QuoteLeAttributeValue> values = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
					// delete all existing attributes for that name
					quoteLeAttributeValueRepository.deleteAll(values);
					List<QuoteLeAttributeValue> attributeValues = arrayAttribute.getAttributeValue().stream()
							.map(attributeValue -> createQuoteLeAttributeValue(arrayAttribute, quote, quoteToLe,
									mstOmsAttribute, attributeValue))
							.collect(Collectors.toList());
					// save all new attributes
					return quoteLeAttributeValueRepository.saveAll(attributeValues);
				}).orElseThrow(() -> new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND));
	}

	/**
	 * GscQuoteAttributeValuesContext
	 *
	 */
	private static class GscQuoteAttributeValuesContext {
		Integer quoteId;
		Quote quote;
		Integer quoteToLeId;
		QuoteToLe quoteToLe;
		QuoteProductComponent productComponent;
		List<GscQuoteProductComponentsAttributeValueBean> attributes;
	}

	/**
	 * Create quote attributes context
	 *
	 * @param quoteId
	 * @param attributes
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuoteAttributeValuesContext createQuoteAttributeContext(Integer quoteId,
																	   List<GscQuoteProductComponentsAttributeValueBean> attributes) throws TclCommonException {
		Quote quote = gscQuoteService.getQuote(quoteId);
		GscQuoteAttributeValuesContext context = new GscQuoteAttributeValuesContext();
		context.quote = quote;
		context.quoteId = quoteId;
		context.attributes = attributes;
		return context;
	}

	/**
	 * create document context class
	 */
	private static class CreateDocumentContext {
		Integer quoteId;
		Integer quoteToLeID;
		Quote quote;
		QuoteToLe quoteToLe;
		CustomerLeAttributeRequestBean customerLeAttributeRequestBean;
		CustomerLeDetailsBean customerLeDetailsBean;
		GscDocumentBean document;
		String provideName;
		Integer oldCustomerLegalEntityId;
	}

	/**
	 * Create GSC Document context class
	 *
	 * @param document
	 * @return
	 */
	private static CreateDocumentContext createContext(GscDocumentBean document) {
		CreateDocumentContext context = new CreateDocumentContext();
		context.quoteId = document.getQuoteId();
		context.quoteToLeID = document.getQuoteLeId();
		CustomerLeAttributeRequestBean customerLeAttributeRequestBean = new CustomerLeAttributeRequestBean();
		customerLeAttributeRequestBean.setCustomerLeId(document.getCustomerLegalEntityId());
		customerLeAttributeRequestBean.setProductName(document.getProductName());
		context.customerLeAttributeRequestBean = customerLeAttributeRequestBean;
		context.document = document;
		return context;
	}

	/**
	 * Save quote legal attributes
	 *
	 * @param quote
	 * @param quoteToLe
	 * @param attributes
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	public List<GscQuoteProductComponentsAttributeValueBean> saveQuoteToLeAttributes(Quote quote, QuoteToLe quoteToLe,
																					 List<GscQuoteProductComponentsAttributeValueBean> attributes) {
		Objects.requireNonNull(quote, QUOTE_NULL_MESSAGE);
		Objects.requireNonNull(quoteToLe, QUOTE_LE_ID_NULL_MESSAGE);
		Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
		List<GscQuoteProductComponentsAttributeSimpleValueBean> simpleAttributes = attributes.stream()
				.filter(attribute -> attribute instanceof GscQuoteProductComponentsAttributeSimpleValueBean)
				.map(attribute -> (GscQuoteProductComponentsAttributeSimpleValueBean) attribute).map(attribute -> {
					try {
						return saveQuoteToLeAttributeValue(quoteToLe, attribute);
					} catch (TclCommonException e) {
						LOGGER.warn(ExceptionConstants.ATTRIBUTE_EMPTY);
						return new QuoteLeAttributeValue();
					}
				}).map(GscQuoteProductComponentsAttributeSimpleValueBean::fromQuoteLeAttributeValue)
				.collect(Collectors.toList());
		List<GscQuoteProductComponentsAttributeSimpleValueBean> arrayAttributes = attributes.stream()
				.filter(attribute -> attribute instanceof GscQuoteProductComponentsAttributeArrayValueBean)
				.map(attribute -> (GscQuoteProductComponentsAttributeArrayValueBean) attribute).map(attribute -> {
					try {
						return handleArrayAttribute(attribute, quote, quoteToLe);
					} catch (TclCommonException e) {
						LOGGER.warn("Exception occurred : {}", e.getMessage());
					}
					return new ArrayList<QuoteLeAttributeValue>();
				}).flatMap(List::stream)
				.map(GscQuoteProductComponentsAttributeSimpleValueBean::fromQuoteLeAttributeValue)
				.collect(Collectors.toList());
		simpleAttributes.addAll(arrayAttributes);
		return groupAndConvertToValueBeans(simpleAttributes);
	}

	/**
	 * Fetch quote to le attribute values
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteAttributeValuesContext fetchQuoteToLeAttributeValues(GscQuoteAttributeValuesContext context) {
		List<GscQuoteProductComponentsAttributeSimpleValueBean> attributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLe(context.quoteToLe).stream()
				.map(GscQuoteProductComponentsAttributeSimpleValueBean::fromQuoteLeAttributeValue)
				.collect(Collectors.toList());
		context.attributes = groupAndConvertToValueBeans(attributeValues);
		return context;
	}

	/**
	 * Populate product components in context
	 *
	 * @param context
	 */
	private GscQuoteAttributeValuesContext populateQuoteProductComponent(GscQuoteAttributeValuesContext context) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(context.quoteId, GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE);

		String productName = GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase();
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(
				GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), GscConstants.STATUS_ACTIVE);
		context.productComponent = productComponents.stream().findFirst().orElseGet(() -> {
			try {
				LOGGER.info("Returning context {}", context.quoteId + "" + context.quote + mstProductFamily);
				return gscQuoteDetailService.saveProductComponent(context.quoteId, productName, context.quote,
						mstProductFamily, GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase());
			} catch (TclCommonException e) {
				LOGGER.warn("Inside populateQuoteProductComponent {}", e.getLocalizedMessage());
			}
			return null;
		});
		return context;
	}

	/**
	 * Wrapper method for getQuoteToLe
	 *
	 * @param quoteToLeId
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuoteAttributeValuesContext populateQuoteToLe(Integer quoteToLeId,
															 GscQuoteAttributeValuesContext context) throws TclCommonException {
		QuoteToLe quoteToLe = gscQuoteService.getQuoteToLe(quoteToLeId);
		context.quoteToLeId = quoteToLeId;
		context.quoteToLe = quoteToLe;
		return context;
	}

	/**
	 * Get quote attributes by ID
	 *
	 * @param quoteId
	 * @return {@link GscQuoteAttributesBean}
	 * @throws TclCommonException
	 */
	@Transactional
	public GscQuoteAttributesBean getQuoteAttributes(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		GscQuoteAttributeValuesContext context = createQuoteAttributeContext(quoteId, ImmutableList.of());
		populateQuoteProductComponent(context);
		GscProductComponentBean bean = populateQuoteProductComponentAttributes(context.productComponent, context.quote);
		GscQuoteAttributesBean quoteAttribute = new GscQuoteAttributesBean();
		quoteAttribute.setAttributes(bean.getAttributes());
		quoteAttribute.setQuoteId(context.quoteId);
		return quoteAttribute;
	}

	/**
	 * Save quote legal attributes to context
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteAttributeValuesContext saveQuoteToLeAttributes(GscQuoteAttributeValuesContext context) {
		context.attributes = saveQuoteToLeAttributes(context.quote, context.quoteToLe, context.attributes);
		return context;
	}

	/**
	 * Save quote legal attributes
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param attributes
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	@Transactional
	public GscQuoteAttributesBean saveQuoteToLeAttributes(Integer quoteId, Integer quoteToLeId,
														  List<GscQuoteProductComponentsAttributeValueBean> attributes) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
		Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
		GscQuoteAttributeValuesContext context = createQuoteAttributeContext(quoteId, attributes);
		populateQuoteToLe(quoteToLeId, context);
		saveQuoteToLeAttributes(context);
		GscQuoteAttributesBean bean = new GscQuoteAttributesBean();
		bean.setQuoteId(context.quoteId);
		bean.setAttributes(context.attributes);
		return bean;
	}

	/**
	 * returns quoteToLe contact attribute values
	 *
	 * @author
	 * @param quoteId
	 * @param quoteToLeId
	 * @return {@link GscContactAttributeInfo}
	 */
	public GscContactAttributeInfo getContactInfo(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		GscContactAttributeInfo attributeInfo = new GscContactAttributeInfo();
		getQuoteToLeAttributes(quoteId, quoteToLeId).getAttributes().forEach(attrval -> {
			if (attrval.getAttributeName().equals(LeAttributesConstants.CONTACT_ID.toString())) {
				attributeInfo.setUserId(attrval.getValueString());
			} else if (attrval.getAttributeName().equals(LeAttributesConstants.CONTACT_NAME.toString())) {
				attributeInfo.setFirstName(attrval.getValueString());
			} else if (attrval.getAttributeName().equals(LeAttributesConstants.CONTACT_EMAIL.toString())) {
				attributeInfo.setEmailId(attrval.getValueString());
			} else if (attrval.getAttributeName().equals(LeAttributesConstants.DESIGNATION.toString())) {
				attributeInfo.setDesignation(attrval.getValueString());
			} else if (attrval.getAttributeName().equals(LeAttributesConstants.CONTACT_NO.toString())) {
				attributeInfo.setContactNo(attrval.getValueString());
			}
		});
		return attributeInfo;
	}

	/**
	 * Populate Quote in context
	 *
	 * @param context
	 * @return
	 */
	private CreateDocumentContext populateQuote(CreateDocumentContext context) throws TclCommonException {
		context.quote = gscQuoteService.getQuote(context.quoteId);
		return context;
	}

	/**
	 * Populate Quote To Le in context
	 *
	 * @param context
	 * @return
	 */
	private CreateDocumentContext populateQuoteToLe(CreateDocumentContext context) throws TclCommonException {
		context.quoteToLe = gscQuoteService.getQuoteToLe(context.quoteToLeID);
		context.oldCustomerLegalEntityId = context.quoteToLe.getErfCusCustomerLegalEntityId();
		return context;
	}

	/**
	 * Fetch details from customer MDM via queue call
	 *
	 * @param context
	 * @return
	 */

	private CreateDocumentContext fetchCustomerLeAttributes(CreateDocumentContext context) throws TclCommonException {
		String jsonPayload = GscUtils.toJson(context.customerLeAttributeRequestBean);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("fetchCustomerLeAttributes payload to customer service:", jsonPayload));
		}
		String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct, jsonPayload);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("fetchCustomerLeAttributes customer service response:", customerLeAttributes));
		}
		context.customerLeDetailsBean = GscUtils.fromJson(customerLeAttributes, CustomerLeDetailsBean.class);
		return context;
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
	 * Update attributes
	 *
	 * @param attribute
	 * @param quoteLeAttributeValues
	 */
	private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues) {
		quoteLeAttributeValues.stream()
				.filter(attr -> !GscConstants.PAYMENT_CURRENCY.equalsIgnoreCase(attr.getMstOmsAttribute().getName()))
				.forEach(attr -> {
					attr.setAttributeValue(attribute.getAttributeValue());
					quoteLeAttributeValueRepository.save(attr);
				});
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
	 * Construct and update billing information for SFDC
	 *
	 * @param context
	 * @return
	 */
	private CreateDocumentContext constructAndUpdateBillingInfoForSfdc(CreateDocumentContext context) {
		Optional.ofNullable(context.customerLeDetailsBean.getAttributes()).orElse(ImmutableList.of())
				.forEach(billAttr -> saveConstructBillingAttribute(billAttr, context.quoteToLe));
		saveQuoteToLeAttribute(context.quoteToLe, context.customerLeDetailsBean.getAccounCuId(),
				LeAttributesConstants.ACCOUNT_CUID);
		saveQuoteToLeAttribute(context.quoteToLe, context.customerLeDetailsBean.getAccountId(),
				LeAttributesConstants.ACCOUNT_NO18);
		/*
		 * Since Billing Contact Id saved by update attribute API of OMS,no need to save
		 * from customer queue saveQuoteToLeAttribute(context.quoteToLe,
		 * String.valueOf(context.customerLeDetailsBean.getBillingContactId()),
		 * LeAttributesConstants.BILLING_CONTACT_ID);
		 */
		saveQuoteToLeAttribute(context.quoteToLe, context.customerLeDetailsBean.getLegalEntityName(),
				LeAttributesConstants.LEGAL_ENTITY_NAME);
		return context;
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
	public CreateDocumentContext fetchServiceProviderName(CreateDocumentContext context) throws TclCommonException {
		context.provideName = (String) mqUtils.sendAndReceive(spQueue, Utils
				.convertObjectToJson(constructSupplierDetailsRequestBean(context.document.getSupplierLegalEntityId())));
		return context;
	}

	/**
	 * Process Account
	 *
	 * @param context
	 * @return
	 */
	private CreateDocumentContext processAccount(CreateDocumentContext context) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(context.quoteToLe,
						LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
		if (Objects.isNull(quoteLeAttributeValues) || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(context.provideName);
			attributeValue.setDisplayValue(context.provideName);
			attributeValue.setQuoteToLe(context.quoteToLe);
			MstOmsAttribute mstOmsAttribute = getMstAttributeMasterForBilling(
					LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValues.add(attributeValue);
		}
		quoteLeAttributeValues.forEach(attr -> {
			attr.setAttributeValue(context.provideName);
			quoteLeAttributeValueRepository.save(attr);
		});
		return context;
	}

	/**
	 * Save quote and quoteToLe with updated customer and supplier details
	 *
	 * @param context
	 * @return
	 */
	private CreateDocumentContext updateQuoteToLeAndQuote(CreateDocumentContext context) {
		QuoteToLe quoteToLe = context.quoteToLe;
		quoteToLe.setErfCusCustomerLegalEntityId(context.document.getCustomerLegalEntityId());
		quoteToLe.setErfCusSpLegalEntityId(context.document.getSupplierLegalEntityId());
		if (quoteToLe.getStage().equals(QuoteStageConstants.CHECKOUT.getConstantCode())) {
			quoteToLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			quoteToLeRepository.save(quoteToLe);
		}

		CustomerDetail customerDetail = null;
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			customerDetail = new CustomerDetail();
			Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(context.document.getCustomerId(),
					(byte) 1);
			customerDetail.setCustomerId(customer.getId());
		} else {
			customerDetail = userInfoUtils.getCustomerByLeId(context.document.getCustomerLegalEntityId());
		}

		if (Objects.nonNull(customerDetail)
				&& !customerDetail.getCustomerId().equals(context.quote.getCustomer().getId())) {
			Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
			if (customerEntity.isPresent()) {
				Quote quote = context.quote;
				quote.setCustomer(customerEntity.get());
				quoteRepository.save(quote);
			}
		}
		return context;
	}

	/**
	 * This method update customer and supplier details against the quote and
	 * quoteToLe
	 *
	 * @param document
	 * @return GscDocumentBean
	 */
	@Transactional
	public GscDocumentBean createGscDocument(GscDocumentBean document) throws TclCommonException {
		Objects.requireNonNull(document);
		CreateDocumentContext documentContext = createContext(document);
		populateQuote(documentContext);
		populateQuoteToLe(documentContext);
		fetchCustomerLeAttributes(documentContext);
		constructAndUpdateBillingInfoForSfdc(documentContext);
		fetchServiceProviderName(documentContext);
		processAccount(documentContext);
		updateQuoteToLeAndQuote(documentContext);
		if (WebexConstants.UCAAS_FAMILY_NAME.equals(webexQuoteService.getProductFamily(documentContext.quoteToLe))) {
			// Unique endpoint location details to be persisted in
			// locationLeCustomerRepository
			Set<Integer> locationIds = quoteUcaasRepository
					.findByQuoteToLeIdAndLocationNotNull(documentContext.quoteToLe.getId()).stream()
					.map(QuoteUcaas::getEndpointLocationId).collect(Collectors.toSet());
			if (!locationIds.isEmpty()) {
				processLocationDetailsAndSendToQueue(documentContext.quoteToLe,
						documentContext.quote.getCustomer().getErfCusCustomerId(), locationIds);
			}
			// Trigger credit check for ucaas.
			triggerCreditCheck(documentContext);
			updateOpportunityInSfdcForUcaas(documentContext);
		} else {
			updateOpportunityInSfdc(documentContext);
		}
		return documentContext.document;
	}

	/**
	 * Method to trigger credit check.
	 * @param context
	 * @throws TclCommonException
	 */
	public void triggerCreditCheck(CreateDocumentContext context) throws TclCommonException {
		LOGGER.info("Before triggering credit check");
		if(Objects.isNull(context.quoteToLe.getQuoteType()) ||
				(Objects.nonNull(context.quoteToLe.getQuoteType()) && context.quoteToLe.getQuoteType().equals(CommonConstants.NEW))
				|| (Objects.nonNull(context.quoteToLe) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(context.quoteToLe.getQuoteType())) ) {
			CustomerLeDetailsBean lePreapprovedValuesBean = context.customerLeDetailsBean;
			processAccount(context.quoteToLe, lePreapprovedValuesBean.getCreditCheckAccountType(), LeAttributesConstants.CREDIT_CHECK_ACCOUNT_TYPE.toString());
			creditCheckService.triggerCreditCheck(context.document.getCustomerLegalEntityId(),  Optional.of(context.quoteToLe), lePreapprovedValuesBean, context.oldCustomerLegalEntityId);
			LOGGER.info("Credit check status :: {}",context.quoteToLe.getTpsSfdcStatusCreditControl());
			LOGGER.info("Preapproved flag :: {}", CommonConstants.BACTIVE.equals(context.quoteToLe.getPreapprovedOpportunityFlag()));
		}
		LOGGER.info("After triggering credit check");
	}

	/**
	 * processAccountCuid used to process account details from customer mdm
	 * @param quoteToLe
	 * @param attrValue
	 * @param attributeName
	 */
	private void processAccount(QuoteToLe quoteToLe, String attrValue, String attributeName) {
		LOGGER.info("Inside processAccount method for attribute {}", attributeName );
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
	 * Method to process request for location queue And to persist unique endpoint
	 * details.
	 *
	 * @param quoteToLe
	 * @param erfCustomerId
	 */
	private void processLocationDetailsAndSendToQueue(QuoteToLe quoteToLe, Integer erfCustomerId,
													  Set<Integer> locationIds) {
		try {
			CustomerLeLocationBean bean = constructCustomerLeAndLocation(quoteToLe, erfCustomerId, locationIds);
			String request = Utils.convertObjectToJson(bean);
			LOGGER.info("Customer id to be send {} , request {}", erfCustomerId, request);
			LOGGER.info("MDC Filter token value in before Queue call processLocationDetailsAndSendToQueue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(customerLeLocationQueue, request);
		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}

	}

	/**
	 * Method to contruct request bean for location queue.
	 *
	 * @param quoteToLe
	 * @param erfCustomerId
	 * @return
	 */
	public CustomerLeLocationBean constructCustomerLeAndLocation(QuoteToLe quoteToLe, Integer erfCustomerId,
																 Set<Integer> locationIds) {
		CustomerLeLocationBean customerLeLocationBean = new CustomerLeLocationBean();

		try {
			customerLeLocationBean.setErfCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
			customerLeLocationBean.setCustomerId(erfCustomerId);
			customerLeLocationBean.getLocationIds().addAll(locationIds);
		} catch (Exception e) {
			// since it is and internal queue call so we are logging it only
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}
		return customerLeLocationBean;
	}

	/**
	 * Update opportunity in SFDC
	 *
	 * @param context
	 * @return
	 */
	private CreateDocumentContext updateOpportunityInSfdc(CreateDocumentContext context) {
		quoteToLeRepository.findByQuote(context.quote).forEach(quoteLe -> {
			try {
				gscOmsSfdcComponent.getOmsSfdcService().processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
						SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteLe);

			} catch (Exception e) {
				Throwables.propagate(e);
			}
		});
		return context;
	}

	/**
	 * Update opportunity in SFDC for UCAAS
	 *
	 * @param context
	 * @return
	 */
	private CreateDocumentContext updateOpportunityInSfdcForUcaas(CreateDocumentContext context) {
		quoteToLeRepository.findByQuote(context.quote).forEach(quoteLe -> {
			try {
				ucaasOmsSfdcComponent.getOmsSfdcService().processUpdateOpportunity(new Date(),
						quoteLe.getTpsSfdcOptyId(), SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteLe);

			} catch (Exception e) {
				Throwables.propagate(e);
			}
		});
		return context;
	}

	/**
	 * Save quote attributes by quote id
	 *
	 * @param quoteId
	 * @param attributes
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	@Transactional
	public List<GscQuoteProductComponentsAttributeValueBean> saveQuoteAttributes(Integer quoteId,
																				 List<GscQuoteProductComponentsAttributeValueBean> attributes) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
		GscQuoteAttributeValuesContext context = createQuoteAttributeContext(quoteId, attributes);
		populateQuoteProductComponent(context);
		saveQuoteAttributes(context);
		return context.attributes;
	}

	/**
	 * Method to save quote attributes.
	 *
	 * @param context
	 * @return
	 */
	private GscQuoteAttributeValuesContext saveQuoteAttributes(GscQuoteAttributeValuesContext context) {
		GscProductComponentBean componentBean = GscProductComponentBean
				.fromQuoteProductComponent(context.productComponent);
		componentBean.setAttributes(context.attributes);
		context.attributes = processProductComponent(componentBean, context.productComponent, context.quote)
				.getAttributes();
		return context;
	}

	/**
	 * Get quote legal attributes
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	public GscQuoteAttributesBean getQuoteToLeAttributes(Integer quoteId, Integer quoteToLeId)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
		GscQuoteAttributeValuesContext context = createQuoteAttributeContext(quoteId, null);
		populateQuoteToLe(quoteToLeId, context);
		fetchQuoteToLeAttributeValues(context);
		GscQuoteAttributesBean bean = new GscQuoteAttributesBean();
		bean.setAttributes(context.attributes);
		bean.setQuoteId(quoteId);
		return bean;
	}

	/**
	 * Handle updating multiple configurations for UIFN
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @param configurations
	 * @param gscProductComponentBeansData
	 * @return {@link List<GscProductComponentBean>}
	 */
	@Transactional
	public List<GscProductComponentBean> updateMultipleConfigurationProductComponentAttr(Integer quoteId,
																						 Integer solutionId, Integer gscQuoteId, List<Integer> configurations,
																						 List<GscProductComponentBean> gscProductComponentBeansData) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(gscQuoteId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(gscProductComponentBeansData, ATTRIBUTES_NULL_MESSAGE);
		Objects.requireNonNull(configurations, "Configurations ID cannot be null");

		configurations.forEach(configuration -> {
			Map<Integer, MstProductComponent> map = null;
			try {
				map = fetchProductComponents(configuration).stream().collect(
						Collectors.toMap(QuoteProductComponent::getId, QuoteProductComponent::getMstProductComponent));
			} catch (TclCommonException e) {
				e.printStackTrace();
			}

			// delete all attribute for product components
			quoteProductComponentsAttributeValueRepository.deleteAllByQuoteProductComponentIdIn(map.keySet());

			map.entrySet().forEach(value -> {
				gscProductComponentBeansData.forEach(gscProductComponentBean -> {
					if (value.getValue().getName().equals(gscProductComponentBean.getProductComponentName())) {
						gscProductComponentBean.setId(value.getKey());
						this.resetAttributeIds(gscProductComponentBean.getAttributes());
					}
				});
			});

			try {
				gscProductComponentBeansResponse = updateOrDeleteProductComponentAttributes(quoteId, solutionId,
						gscQuoteId, configuration, gscProductComponentBeansData);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
			// gscProductComponentBeansResponse.addAll(gscProductComponentBeansResponse);
		});
		return gscProductComponentBeansResponse;
	}

	private void resetAttributeIds(
			List<GscQuoteProductComponentsAttributeValueBean> gscQuoteProductComponentsAttributeValueBeanList) {
		gscQuoteProductComponentsAttributeValueBeanList
				.forEach(gscQuoteProductComponentsAttributeValueBean -> gscQuoteProductComponentsAttributeValueBean
						.setAttributeId(null));
	}
}
