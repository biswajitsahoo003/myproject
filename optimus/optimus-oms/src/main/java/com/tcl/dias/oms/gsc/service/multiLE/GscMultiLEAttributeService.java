package com.tcl.dias.oms.gsc.service.multiLE;

import java.util.*;
import java.util.stream.Collectors;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gsc.beans.GscMultiLEProductComponentRequest;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.exception.Exceptions;
import com.tcl.dias.oms.gsc.util.GscConstants;
import org.springframework.transaction.annotation.Transactional;

import static com.tcl.dias.oms.gsc.util.GscConstants.*;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_ID_NULL_MESSAGE;

/**
 * GscMultiLEAttributeService for handling multi LE scenario
 * 
 */
@Service
public class GscMultiLEAttributeService {

	@Autowired
	GscMultiLEQuoteService gscMultiLEQuoteService;

	@Autowired
	GscMultiLEDetailService gscMultiLEDetailService;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	GscMultiLEPricingFeasibilityService gscMultiLEPricingFeasibilityService;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	/**
	 * GscProductComponentAttributeValue context class
	 */
	private static class GscProductComponentAttributeValueContext {
		List<QuoteProductComponent> productComponents;
		Integer quoteId;
		Quote quote;
		Integer quoteGscId;
		List<GscProductComponentBean> componentBeans;
		Boolean isManualFP;
	}

	/**
	 * Create context
	 *
	 * @param quoteId
	 * @param productComponents
	 * @param components
	 * @return
	 */
	private GscProductComponentAttributeValueContext createContextMultipleLE(Integer quoteId, Quote quote,
			List<QuoteProductComponent> productComponents, List<GscProductComponentBean> components,
			Integer quoteGscId) {
		GscProductComponentAttributeValueContext context = new GscProductComponentAttributeValueContext();
		context.productComponents = productComponents;
		context.componentBeans = components;
		context.quoteId = quoteId;
		context.quoteGscId = quoteGscId;
		context.quote = quote;
		return context;
	}

	/**
	 * Handle array attributes
	 *
	 * @param productComponent
	 * @param arrayAttribute
	 * @param quote
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValue> handleArrayAttribute(QuoteProductComponent productComponent,
			GscQuoteProductComponentsAttributeArrayValueBean arrayAttribute, Quote quote, Boolean isManualFP) {
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
					if (TERM_NAME.contains(productAttributeMaster.getName())) {
						List<String> attributeIds = values.stream().map(attribute -> String.valueOf(attribute.getId()))
								.collect(Collectors.toList());
						List<QuotePrice> quotePricesToDelete = quotePriceRepository
								.findByReferenceNameAndReferenceIdIn(QuoteConstants.ATTRIBUTES.toString(),
										attributeIds);
						quotePriceRepository.deleteAll(quotePricesToDelete);

						List<QuotePrice> quotePrices = gscMultiLEPricingFeasibilityService
								.createOrUpdateQuotePricesForAttributes(quote.getId(),
										productComponent.getMstProductFamily().getName(), attributeValues);
						if (Objects.nonNull(isManualFP) && isManualFP)
							gscMultiLEPricingFeasibilityService
									.updateQuotePriceAuditForUsagePrice(quotePricesToDelete, quotePrices, quote);
					}
					// save all new attributes
					return quoteProductComponentsAttributeValueRepository.saveAll(attributeValues);
				}).orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY));
	}

	/**
	 * Create quote product components attribute values
	 *
	 * @param attributeValueBean
	 * @param quote
	 * @param productComponent
	 * @param attributeMaster
	 * @param attributeValue
	 * @return
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
	 * Save quote component attribute value
	 *
	 * @param quoteProductComponent
	 * @param bean
	 * @return
	 */
	private QuoteProductComponentsAttributeValue saveQuoteComponentAttributeValue(
			QuoteProductComponent quoteProductComponent, GscQuoteProductComponentsAttributeSimpleValueBean bean) {
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
				}).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY, String
						.format("Quote Product component Attribute with name: %s not found", bean.getAttributeName())));
	}

	/**
	 * Process product component
	 *
	 * @param componentBean
	 * @param quote
	 * @return
	 */
	public GscProductComponentBean processProductComponent(GscProductComponentBean componentBean, Quote quote, Boolean isManualFP) {
		Objects.requireNonNull(componentBean, PRODUCT_COMPONENT_NUll_MESSAGE);
		Objects.requireNonNull(quote, QUOTE_NULL_MESSAGE);
		return quoteProductComponentRepository.findById(componentBean.getId())
				.map(productComponent -> processProductComponent(componentBean, productComponent, quote, isManualFP))
				.orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.PRODUCT_EMPTY,
						String.format("Product component with id: %s not found", componentBean.getId())));

	}

	/**
	 * Process product component
	 *
	 * @param componentBean
	 * @param productComponent
	 * @param quote
	 * @return
	 */
	private GscProductComponentBean processProductComponent(GscProductComponentBean componentBean,
			QuoteProductComponent productComponent, Quote quote, Boolean isManualFP) {
		List<String> attributeIds = componentBean.getAttributes().stream()
				.filter(attributeValue -> attributeValue instanceof GscQuoteProductComponentsAttributeSimpleValueBean)
				.map(attribute -> String.valueOf(attribute.getAttributeId())).collect(Collectors.toList());
		List<QuoteProductComponentsAttributeValue> savedSimpleAttributes = componentBean.getAttributes().stream()
				.filter(attributeValue -> attributeValue instanceof GscQuoteProductComponentsAttributeSimpleValueBean)
				.map(attribute -> (GscQuoteProductComponentsAttributeSimpleValueBean) attribute)
				.map(attribute -> saveQuoteComponentAttributeValue(productComponent, attribute))
				.collect(Collectors.toList());

		List<QuotePrice> oldQuotePrices = quotePriceRepository
				.findByReferenceNameAndReferenceIdIn(QuoteConstants.ATTRIBUTES.toString(), attributeIds);
		List<QuotePrice> updatedQuotePrices = gscMultiLEPricingFeasibilityService
				.createOrUpdateQuotePricesForAttributes(quote.getId(), productComponent.getMstProductFamily().getName(),
						savedSimpleAttributes);
		if (Objects.nonNull(isManualFP) && isManualFP && Objects.nonNull(oldQuotePrices) && !oldQuotePrices
				.isEmpty() && Objects.nonNull(updatedQuotePrices) && !updatedQuotePrices.isEmpty())
			gscMultiLEPricingFeasibilityService.createQuotePriceAudit(oldQuotePrices, updatedQuotePrices, quote);
		// save array attributes
		List<QuoteProductComponentsAttributeValue> savedArrayAttributeValues = componentBean.getAttributes().stream()
				.filter(attributeValue -> attributeValue instanceof GscQuoteProductComponentsAttributeArrayValueBean)
				.map(attribute -> (GscQuoteProductComponentsAttributeArrayValueBean) attribute)
				.map(bean -> handleArrayAttribute(productComponent, bean, quote, isManualFP)).flatMap(List::stream)
				.collect(Collectors.toList());
		savedSimpleAttributes.addAll(savedArrayAttributeValues);
		List<GscQuoteProductComponentsAttributeSimpleValueBean> attributes = savedSimpleAttributes.stream()
				.map(GscQuoteProductComponentsAttributeSimpleValueBean::fromProductComponentAttributeValue)
				.collect(Collectors.toList());
		componentBean.setAttributes(groupAndConvertToValueBeans(attributes));
		return componentBean;
	}

	/**
	 * Grouped and convert to value beans
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
				if (!attributeValues.contains(null)) {
					bean.setDisplayValue(Joiner.on(",").join(attributeValues));
				} else {
					bean.setDisplayValue(first.getAttributeValue());
				}
				bean.setAttributeValue(attributeValues);
				return bean;
			}
		}).collect(Collectors.toList());
	}

	/**
	 * Update quote product component attributes
	 *
	 * @param context
	 * @return
	 */
	private GscProductComponentAttributeValueContext updateQuoteProductComponentAttributes(
			GscProductComponentAttributeValueContext context) {
		context.componentBeans = context.componentBeans.stream()
				.map(componentBean -> processProductComponent(componentBean, context.quote, context.isManualFP))
				.collect(Collectors.toList());
		return context;
	}

	/**
	 * Update product component attributes
	 *
	 * @param quoteId
	 * @param attributes
	 * @return
	 */
	public List<GscMultiLEProductComponentRequest> updateProductComponentAttributes(Integer quoteId,
			List<GscMultiLEProductComponentRequest> attributes) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
		Set<Integer> productSolutionIds = attributes.stream().map(GscMultiLEProductComponentRequest::getSolutionId)
				.collect(Collectors.toSet());
		Set<Integer> quoteGscIds = attributes.stream().map(GscMultiLEProductComponentRequest::getGscQuoteId)
				.collect(Collectors.toSet());
		Objects.requireNonNull(gscMultiLEDetailService.getProductSolutionByIds(productSolutionIds),
				ExceptionConstants.PRODUCT_SOLUTION_EMPTY);
		List<QuoteGsc> quoteGscs = gscMultiLEDetailService.getQuoteGscsByQuoteGscsIds(quoteGscIds);
		List<QuoteGscDetail> quoteGscDetails = gscMultiLEDetailService.getQuoteGscDetailsByQuoteGscs(quoteGscs);
		Quote quote = gscMultiLEQuoteService.getQuote(quoteId).get();
		gscMultiLEQuoteService.subtractVoicePricesInQuoteToLe(quoteGscs);
		attributes.forEach(attribute -> {
			GscProductComponentAttributeValueContext context = createContextMultipleLE(quoteId, quote,
					ImmutableList.of(), attribute.getGscProductComponents(), attribute.getGscQuoteId());
			context = updateQuoteProductComponentAttributes(context);
			attribute.setGscProductComponents(context.componentBeans);
		});
		quoteGscs = gscMultiLEDetailService.getQuoteGscsByQuoteGscsIds(quoteGscIds);
		List<QuoteToLe> quoteToLes = quoteGscs.stream().map(quoteGsc -> quoteGsc.getQuoteToLe())
				.collect(Collectors.toList());
		Set<Integer> quoteLeIds = new HashSet<>();
		quoteToLes = quoteToLes.stream().filter(quoteToLe -> !quoteLeIds.contains(quoteToLe.getId()))
				.peek(quoteToLe -> quoteLeIds.add(quoteToLe.getId())).collect(Collectors.toList());
		gscMultiLEQuoteService.updateGscPrices(quoteToLes);
		gscMultiLEQuoteService.addVoicePricesInQuoteToLe(quoteGscs);
		List<QuoteToLe> allQuoteToLes = quoteToLeRepository.findByQuote(quote);
		QuoteToLe parentQuotetole = teamsDRQuoteService.findParentQuoteToLe(allQuoteToLes);
		gscMultiLEQuoteService.updateOpportunityForTeamsDR(quote, parentQuotetole);
		return attributes;
	}

	/**
	 * Populate quote product component attributes
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
	 * Set the values in context
	 *
	 * @param quoteId
	 * @param productComponents
	 * @param components
	 * @param quoteGscId
	 * @param isManualFP
	 * @return
	 */
	private GscProductComponentAttributeValueContext toContext(Integer quoteId,
			List<QuoteProductComponent> productComponents, List<GscProductComponentBean> components, Integer quoteGscId,
			Boolean isManualFP) {
		GscProductComponentAttributeValueContext context = new GscProductComponentAttributeValueContext();
		context.productComponents = productComponents;
		context.componentBeans = components;
		context.quoteId = quoteId;
		context.quoteGscId = quoteGscId;
		context.isManualFP = isManualFP;
		return context;
	}

	/**
	 * Wrapper method for getQuote()
	 *
	 * @param context
	 * @return
	 */
	private GscProductComponentAttributeValueContext populateQuote(GscProductComponentAttributeValueContext context)
			throws TclCommonException {
		context.quote = gscMultiLEDetailService.getQuote(context.quoteId);
		return context;
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
			Integer quoteGscId, Integer configurationId, List<GscProductComponentBean> attributes, Boolean isManualFP)
			throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
		Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
		Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);

		// validate whether solution/quote_gsc/quote_gsc_detail exists or not.
		gscMultiLEDetailService.getProductSolution(solutionId);
		gscMultiLEDetailService.getQuoteGsc(quoteGscId);
		gscMultiLEDetailService.getQuoteGscDetail(configurationId);

		GscProductComponentAttributeValueContext context = toContext(quoteId, ImmutableList.of(), attributes,
				quoteGscId, isManualFP);
		populateQuote(context);
		updateQuoteProductComponentAttributes(context);
		updateAdminChangedPriceAttributeInQuoteLe(context);
		return context.componentBeans;
	}

	/**
	 * Update admin changed price attribute for manual FP in quote le attribute
	 *
	 * @param context
	 * @throws TclCommonException
	 */
	private void updateAdminChangedPriceAttributeInQuoteLe(GscProductComponentAttributeValueContext context)
			throws TclCommonException {
		if (Objects.nonNull(context.isManualFP) && context.isManualFP) {
			QuoteToLe quoteToLe = gscMultiLEDetailService.getQuoteGsc(context.quoteGscId).getQuoteToLe();
			List<QuoteLeAttributeValue> quoteToLeAttributeValueDto = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, LeAttributesConstants.ADMIN_CHANGED_PRICE);
			if (quoteToLeAttributeValueDto.isEmpty()) {
				QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
				quoteLeAttributeValue.setMstOmsAttribute(gscMultiLEQuoteService
						.createOrGetMstOmsAttribute(LeAttributesConstants.ADMIN_CHANGED_PRICE,
								LeAttributesConstants.ADMIN_CHANGED_PRICE).stream().findAny().get());
				quoteLeAttributeValue.setQuoteToLe(quoteToLe);
				quoteLeAttributeValue.setAttributeValue(context.isManualFP.toString());
				quoteLeAttributeValue.setDisplayValue(LeAttributesConstants.ADMIN_CHANGED_PRICE);
				quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
			}
		}
	}
}
