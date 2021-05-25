package com.tcl.dias.oms.gsc.service.v1;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
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
//import com.tcl.dias.oms.gsc.common.GscOmsSfdcComponent;
import com.tcl.dias.oms.gsc.controller.v1.GscQuoteController;
import com.tcl.dias.oms.gsc.exception.Exceptions;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY;
import static com.tcl.dias.common.constants.LeAttributesConstants.PAYMENT_CURRENCY;
import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundError;
import static com.tcl.dias.oms.gsc.util.GscConstants.ATTRIBUTES_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ATTRIBUTE_VALUE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.CONFIGURATION_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.NEW;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_COMPONENT_NUll_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_LE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_PRODUCT_COMPONENT_NUll_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.SOLUTION_ID_NULL_MESSAGE;

/**
 * Services to handle all quote attributes related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscQuoteAttributeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscQuoteAttributeService.class);

    @Autowired
    GscQuoteService gscQuoteService;
    @Autowired
    GscQuoteDetailService gscQuoteDetailService;
    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;
    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;
    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;
    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
    @Autowired
    MstOmsAttributeRepository mstOmsAttributeRepository;
    @Autowired
    UserInfoUtils userInfoUtils;
    @Autowired
    MQUtils mqUtils;
    @Value("${rabbitmq.service.provider.detail}")
    String spQueue;
    @Value("${rabbitmq.customerleattr.product.queue}")
    String customerLeAttrQueueProduct;
    @Autowired
    QuoteRepository quoteRepository;
    @Autowired
    QuoteToLeRepository quoteToLeRepository;
    @Autowired
    CustomerRepository customerRepository;

//    @Autowired
//    GscOmsSfdcComponent gscOmsSfdcComponent;

    private List<GscProductComponentBean> gscProductComponentBeansResponse;

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

    private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
        LOGGER.info("MDC Token in OMS before Rest call : {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
        supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
        supplierDetailRequestBean.setSupplierId(supplierId);
        return supplierDetailRequestBean;
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
     * Wrapper method for getQuote()
     *
     * @param context
     * @return
     */
    private Try<GscProductComponentAttributeValueContext> populateQuote(
            GscProductComponentAttributeValueContext context) {
        return gscQuoteService.getQuote(context.quoteId).map(quote -> {
            context.quote = quote;
            return context;
        });
    }

    /**
     * Update / delete product component attributes by given IDs
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
                                                                                  Integer quoteGscId, Integer configurationId, List<GscProductComponentBean> attributes) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(solutionId, SOLUTION_ID_NULL_MESSAGE);
        Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
        Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
        return gscQuoteDetailService.getProductSolution(solutionId)
                .flatMap(productSolution -> gscQuoteDetailService.getQuoteGsc(quoteGscId))
                .flatMap(quoteGsc -> gscQuoteDetailService.getQuoteGscDetail(configurationId))
                .map(productComponents -> toContext(quoteId, ImmutableList.of(), attributes, quoteGscId))
                .flatMap(this::populateQuote).map(this::updateQuoteProductComponentAttributes)
                .map(context -> context.componentBeans).get();
    }

    /**
     * To create product component attribute values
     *
     * @param attributeValueBean
     * @param quote
     * @param productComponent
     * @return {@link QuoteProductComponentsAttributeValue}
     */
    public QuoteProductComponentsAttributeValue toComponentAttributeValue(
            GscQuoteProductComponentsAttributeSimpleValueBean attributeValueBean, Quote quote,
            QuoteProductComponent productComponent) {
        Objects.requireNonNull(attributeValueBean, ATTRIBUTE_VALUE_NULL_MESSAGE);
        Objects.requireNonNull(quote, QUOTE_NULL_MESSAGE);
        Objects.requireNonNull(productComponent, QUOTE_PRODUCT_COMPONENT_NUll_MESSAGE);
        return productAttributeMasterRepository
                .findByNameAndStatus(attributeValueBean.getAttributeName(), GscConstants.STATUS_ACTIVE).stream()
                .findFirst().map(attributeMaster -> {
                    QuoteProductComponentsAttributeValue value = createQuoteProductComponentsAttributeValues(
                            attributeValueBean, quote, productComponent, attributeMaster,
                            ((GscQuoteProductComponentsAttributeSimpleValueBean) attributeValueBean)
                                    .getAttributeValue());
                    return value;
                }).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.PRODUCT_EMPTY,
                        String.format("Attribute with name: %s not found", attributeValueBean.getAttributeName())));

    }

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

    private List<QuoteProductComponentsAttributeValue> handleArrayAttribute(QuoteProductComponent productComponent,
                                                                            GscQuoteProductComponentsAttributeArrayValueBean arrayAttribute, Quote quote) {
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
                }).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY,
                        String.format("Attribute with name: %s not found", arrayAttribute.getAttributeName())));
    }

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

    private GscProductComponentBean processProductComponent(GscProductComponentBean componentBean,
                                                            QuoteProductComponent productComponent, Quote quote) {
        List<QuoteProductComponentsAttributeValue> savedSimpleAttributes = componentBean.getAttributes().stream()
                .filter(attributeValue -> attributeValue instanceof GscQuoteProductComponentsAttributeSimpleValueBean)
                .map(attribute -> (GscQuoteProductComponentsAttributeSimpleValueBean) attribute)
                .map(attribute -> saveQuoteComponentAttributeValue(productComponent, attribute))
                .collect(Collectors.toList());
        // save array attributes
        List<QuoteProductComponentsAttributeValue> savedArrayAttributeValues = componentBean.getAttributes().stream()
                .filter(attributeValue -> attributeValue instanceof GscQuoteProductComponentsAttributeArrayValueBean)
                .map(attribute -> (GscQuoteProductComponentsAttributeArrayValueBean) attribute)
                .map(bean -> handleArrayAttribute(productComponent, bean, quote)).flatMap(List::stream)
                .collect(Collectors.toList());
        savedSimpleAttributes.addAll(savedArrayAttributeValues);
        List<GscQuoteProductComponentsAttributeSimpleValueBean> attributes = savedSimpleAttributes.stream()
                .map(GscQuoteProductComponentsAttributeSimpleValueBean::fromProductComponentAttributeValue)
                .collect(Collectors.toList());
        componentBean.setAttributes(groupAndConvertToValueBeans(attributes));
        return componentBean;
    }

    /**
     * Process product component
     *
     * @param componentBean
     * @param quote
     * @return {@link GscProductComponentBean}
     */
    public GscProductComponentBean processProductComponent(GscProductComponentBean componentBean, Quote quote) {
        Objects.requireNonNull(componentBean, PRODUCT_COMPONENT_NUll_MESSAGE);
        Objects.requireNonNull(quote, QUOTE_NULL_MESSAGE);
        return quoteProductComponentRepository.findById(componentBean.getId())
                .map(productComponent -> processProductComponent(componentBean, productComponent, quote))
                .orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.PRODUCT_EMPTY,
                        String.format("Product component with id: %s not found", componentBean.getId())));

    }

    /**
     * action method to perform update/delete/create attribute values
     *
     * @param context
     * @return
     * @author VISHESH AWASTHI
     */
    private GscProductComponentAttributeValueContext updateQuoteProductComponentAttributes(
            GscProductComponentAttributeValueContext context) {
        context.componentBeans = context.componentBeans.stream()
                .map(componentBean -> processProductComponent(componentBean, context.quote))
                .collect(Collectors.toList());
        return context;
    }

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
     * fetch product component based on configuration id
     *
     * @param configurationId
     * @return
     */
    private Try<List<QuoteProductComponent>> fetchProductComponents(Integer configurationId) {
        List<QuoteProductComponent> components = quoteProductComponentRepository
                .findByReferenceIdAndType(configurationId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        if (components.isEmpty()) {
            return notFoundError(ExceptionConstants.PRODUCT_EMPTY,
                    String.format("Product components with reference id: %s not found", configurationId));
        } else {
            return Try.success(components);
        }
    }

    /**
     * Get product component attributes
     *
     * @param quoteId
     * @param quoteGscId
     * @param configurationId
     * @return {@link GscProductComponentBean}
     */
    public Try<List<GscProductComponentBean>> getProductComponentAttributes(Integer quoteId, Integer quoteGscId,
                                                                            Integer configurationId) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(quoteGscId, QUOTE_GSC_ID_NULL_MESSAGE);
        Objects.requireNonNull(configurationId, CONFIGURATION_ID_NULL_MESSAGE);
        return Optional.ofNullable(quoteGscId)
                .map(qtGscId -> gscQuoteDetailService.getQuoteGsc(quoteGscId)
                        .flatMap(quoteGsc -> gscQuoteDetailService.getQuoteGscDetail(configurationId))
                        .flatMap(quoteGscDetail -> fetchProductComponents(configurationId))
                        .map(productComponents -> toContext(quoteId, productComponents, null, quoteGscId))
                        .flatMap(this::populateQuote).map(this::fetchQuoteProductComponentAttributes)
                        .map(context -> context.componentBeans))
                .orElse(Try.success(ImmutableList.of()));
    }

    private Try<GscQuoteAttributeValuesContext> createQuoteAttributeContext(Integer quoteId,
                                                                            List<GscQuoteProductComponentsAttributeValueBean> attributes) {
        return gscQuoteService.getQuote(quoteId).map(quote -> {
            GscQuoteAttributeValuesContext context = new GscQuoteAttributeValuesContext();
            context.quote = quote;
            context.quoteId = quoteId;
            context.attributes = attributes;
            return context;
        });
    }

    private Try<GscQuoteAttributeValuesContext> populateQuoteToLe(Integer quoteToLeId,
                                                                  GscQuoteAttributeValuesContext context) {
        return gscQuoteService.getQuoteToLe(quoteToLeId).map(quoteToLe -> {
            context.quoteToLeId = quoteToLeId;
            context.quoteToLe = quoteToLe;
            return context;
        });
    }

    private GscQuoteAttributeValuesContext populateQuoteProductComponent(GscQuoteAttributeValuesContext context) {
        List<QuoteProductComponent> productComponents = quoteProductComponentRepository
                .findByReferenceIdAndType(context.quoteId, GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE);
        String productName = GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase();
        MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(
                GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), GscConstants.STATUS_ACTIVE);
        context.productComponent = productComponents.stream().findFirst()
                .orElseGet(() -> gscQuoteDetailService.saveProductComponent(context.quoteId, productName, context.quote,
                        mstProductFamily, GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase()));
        return context;
    }

    private GscQuoteAttributeValuesContext populateQuoteProductComponentAttributes(
            GscQuoteAttributeValuesContext context) {
        List<GscQuoteProductComponentsAttributeSimpleValueBean> attributes = Optional
                .ofNullable(context.productComponent.getQuoteProductComponentsAttributeValues())
                .orElse(ImmutableSet.of()).stream()
                .map(GscQuoteProductComponentsAttributeSimpleValueBean::fromProductComponentAttributeValue)
                .collect(Collectors.toList());
        context.attributes = groupAndConvertToValueBeans(attributes);
        return context;
    }

    private GscQuoteAttributeValuesContext saveQuoteAttributes(GscQuoteAttributeValuesContext context) {
        GscProductComponentBean componentBean = GscProductComponentBean
                .fromQuoteProductComponent(context.productComponent);
        componentBean.setAttributes(context.attributes);
        context.attributes = processProductComponent(componentBean, context.productComponent, context.quote)
                .getAttributes();
        return context;
    }

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

    private QuoteLeAttributeValue saveQuoteToLeAttributeValue(QuoteToLe quoteToLe,
                                                              GscQuoteProductComponentsAttributeSimpleValueBean bean) {
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
                }).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY,
                        String.format("Quote LE Attribute with name: %s not found", bean.getAttributeName())));
    }

    private List<QuoteLeAttributeValue> handleArrayAttribute(
            GscQuoteProductComponentsAttributeArrayValueBean arrayAttribute, Quote quote, QuoteToLe quoteToLe) {
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
                }).orElseThrow(() -> Exceptions.notFoundWithMessage(ExceptionConstants.ATTRIBUTE_EMPTY, String
                        .format("Quote LE Attribute with name: %s not found", arrayAttribute.getAttributeName())));
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
                .map(attribute -> (GscQuoteProductComponentsAttributeSimpleValueBean) attribute)
                .map(attribute -> saveQuoteToLeAttributeValue(quoteToLe, attribute))
                .map(GscQuoteProductComponentsAttributeSimpleValueBean::fromQuoteLeAttributeValue)
                .collect(Collectors.toList());
        List<GscQuoteProductComponentsAttributeSimpleValueBean> arrayAttributes = attributes.stream()
                .filter(attribute -> attribute instanceof GscQuoteProductComponentsAttributeArrayValueBean)
                .map(attribute -> (GscQuoteProductComponentsAttributeArrayValueBean) attribute)
                .map(attribute -> handleArrayAttribute(attribute, quote, quoteToLe)).flatMap(List::stream)
                .map(GscQuoteProductComponentsAttributeSimpleValueBean::fromQuoteLeAttributeValue)
                .collect(Collectors.toList());
        simpleAttributes.addAll(arrayAttributes);
        return groupAndConvertToValueBeans(simpleAttributes);
    }

    private GscQuoteAttributeValuesContext saveQuoteToLeAttributes(GscQuoteAttributeValuesContext context) {
        context.attributes = saveQuoteToLeAttributes(context.quote, context.quoteToLe, context.attributes);
        return context;
    }

    private GscQuoteAttributeValuesContext fetchQuoteToLeAttributeValues(GscQuoteAttributeValuesContext context) {
        List<GscQuoteProductComponentsAttributeSimpleValueBean> attributeValues = quoteLeAttributeValueRepository
                .findByQuoteToLe(context.quoteToLe).stream()
                .map(GscQuoteProductComponentsAttributeSimpleValueBean::fromQuoteLeAttributeValue)
                .collect(Collectors.toList());
        context.attributes = groupAndConvertToValueBeans(attributeValues);
        return context;
    }

    /**
     * Get quote attributes by ID
     *
     * @param quoteId
     * @return {@link GscQuoteAttributesBean}
     */
    @Transactional
    public GscQuoteAttributesBean getQuoteAttributes(Integer quoteId) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        return createQuoteAttributeContext(quoteId, ImmutableList.of()).map(this::populateQuoteProductComponent)
                .map(this::populateQuoteProductComponentAttributes).map(context -> {
                    GscQuoteAttributesBean bean = new GscQuoteAttributesBean();
                    bean.setAttributes(context.attributes);
                    bean.setQuoteId(context.quoteId);
                    return bean;
                }).get();
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
                                                                                 List<GscQuoteProductComponentsAttributeValueBean> attributes) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
        return createQuoteAttributeContext(quoteId, attributes).map(this::populateQuoteProductComponent)
                .map(this::saveQuoteAttributes).map(context -> context.attributes).get();
    }

    /**
     * Get quote legal attributes
     *
     * @param quoteId
     * @param quoteToLeId
     * @return {@link GscQuoteProductComponentsAttributeValueBean}
     */
    public Try<GscQuoteAttributesBean> getQuoteToLeAttributes(Integer quoteId, Integer quoteToLeId) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
        return createQuoteAttributeContext(quoteId, null).flatMap(context -> populateQuoteToLe(quoteToLeId, context))
                .map(this::fetchQuoteToLeAttributeValues).map(context -> {
                    GscQuoteAttributesBean bean = new GscQuoteAttributesBean();
                    bean.setAttributes(context.attributes);
                    bean.setQuoteId(quoteId);
                    return bean;
                });
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
                                                          List<GscQuoteProductComponentsAttributeValueBean> attributes) {
        Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(quoteToLeId, QUOTE_LE_ID_NULL_MESSAGE);
        Objects.requireNonNull(attributes, ATTRIBUTES_NULL_MESSAGE);
        return createQuoteAttributeContext(quoteId, attributes)
                .flatMap(context -> populateQuoteToLe(quoteToLeId, context))
                .map(this::saveQuoteToLeAttributes)
                .map(context -> {
                    GscQuoteAttributesBean bean = new GscQuoteAttributesBean();
                    bean.setQuoteId(context.quoteId);
                    bean.setAttributes(context.attributes);
                    return bean;
                }).get();
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
            Map<Integer, MstProductComponent> map = fetchProductComponents(configuration).get().stream().collect(
                    Collectors.toMap(QuoteProductComponent::getId, QuoteProductComponent::getMstProductComponent));

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

            gscProductComponentBeansResponse = updateOrDeleteProductComponentAttributes(quoteId, solutionId, gscQuoteId,
                    configuration, gscProductComponentBeansData);
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

    /**
     * This method update customer and supplier details against the quote and
     * quoteToLe
     *
     * @param document
     * @return GscDocumentBean
     * {@link GscQuoteController#createDocument(Integer, Integer, com.tcl.dias.oms.gsc.beans.GscApiRequest)}
     * @author VISHESH AWASTHI
     */

    @Transactional
    public GscDocumentBean createGscDocument(GscDocumentBean document) {
        Objects.requireNonNull(document);
        Try.success(createContext(document))
                .flatMap(this::populateQuote)
                .flatMap(this::populateQuoteToLe)
                .map(this::fetchCustomerLeAttributes)
                .map(this::removeCustomerContractingEntityAttributes)
                .map(this::constructAndUpdateBillingInfoForSfdc)
                .mapTry(this::fetchServiceProviderName)
                .map(this::processAccount)
                .mapTry(this::updateQuoteToLeAndQuote)
                .mapTry(this::updateOpportunityInSfdc).get();
        return document;
    }

    /**
     * save quote and quoteToLe with updated customer and supplier details
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
            Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(context.document.getCustomerId(), (byte) 1);
            customerDetail.setCustomerId(customer.getId());
        } else {
            customerDetail = userInfoUtils.getCustomerByLeId(context.document.getCustomerLegalEntityId());
        }
        if (customerDetail != null && !customerDetail.getCustomerId().equals(context.quote.getCustomer().getId())) {
            Optional<Customer> customerEntity = customerRepository.findById(customerDetail.getCustomerId());
            if (customerEntity.isPresent()) {
                Quote quote = context.quote;
                quote.setCustomer(customerEntity.get());
                quoteRepository.save(quote);
            }
        }
        return context;
    }

    private CreateDocumentContext processAccount(CreateDocumentContext context) {
        List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
                .findByQuoteToLeAndMstOmsAttribute_Name(context.quoteToLe,
                        LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
        if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
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

    public CreateDocumentContext fetchServiceProviderName(CreateDocumentContext context) throws TclCommonException {
        LOGGER.info("MDC Filter token value in before Queue call fetchServiceProviderName {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        context.provideName = (String) mqUtils.sendAndReceive(spQueue,
                Utils.convertObjectToJson(
                        constructSupplierDetailsRequestBean(context.document.getSupplierLegalEntityId())));
        return context;
    }

    private CreateDocumentContext constructAndUpdateBillingInfoForSfdc(CreateDocumentContext context) {
        Optional.ofNullable(context.customerLeDetailsBean.getAttributes())
                .orElse(ImmutableList.of())
                .forEach(billAttr -> saveConstructBillingAttribute(billAttr, context.quoteToLe));
        saveQuoteToLeAttribute(context.quoteToLe, context.customerLeDetailsBean.getAccounCuId(),
                LeAttributesConstants.ACCOUNT_CUID);
        saveQuoteToLeAttribute(context.quoteToLe, context.customerLeDetailsBean.getAccountId(),
                LeAttributesConstants.ACCOUNT_NO18);
		/* Since Billing Contact Id saved by update attribute API of OMS,no need to save from customer queue
		saveQuoteToLeAttribute(context.quoteToLe, String.valueOf(context.customerLeDetailsBean.getBillingContactId()),
				LeAttributesConstants.BILLING_CONTACT_ID);*/
        saveQuoteToLeAttribute(context.quoteToLe, context.customerLeDetailsBean.getLegalEntityName(),
                LeAttributesConstants.LEGAL_ENTITY_NAME);
        return context;
    }

    private void saveConstructBillingAttribute(Attributes attribute, QuoteToLe quoteToLe) {
        List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
                .findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getAttributeName());
        if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
            updateAttributes(attribute, quoteLeAttributeValues);
        } else {
            createAttribute(attribute, quoteToLe);
        }
    }

    private void saveQuoteToLeAttribute(QuoteToLe quoteToLe, String attrValue, String attributeName) {
        List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
                .findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attributeName);
        if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
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

    private void updateAttributes(Attributes attribute, List<QuoteLeAttributeValue> quoteLeAttributeValues) {
        quoteLeAttributeValues.stream()
                .filter(attr -> !PAYMENT_CURRENCY.equalsIgnoreCase(attr.getMstOmsAttribute().getName()))
                //.filter(attr -> !CUSTOMER_CONTRACTING_ENTITY.equalsIgnoreCase(attr.getMstOmsAttribute().getName()))
                .forEach(attr -> {
                    attr.setAttributeValue(attribute.getAttributeValue());
                    quoteLeAttributeValueRepository.save(attr);
                });
    }

    /**
     * creates QuoteLeAttributeValue
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
     * @param attrName
     * @return
     */
    public MstOmsAttribute getMstAttributeMasterForBilling(String attrName) {
        MstOmsAttribute mstOmsAttribute = null;
        List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName, (byte) 1);
        if (mstOmsAttributes != null && !mstOmsAttributes.isEmpty()) {
            mstOmsAttribute = mstOmsAttributes.get(0);
        }
        if (mstOmsAttribute == null) {
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
     * fetch details from customer MDM via queue call
     *
     * @param context
     * @return
     */

    private CreateDocumentContext fetchCustomerLeAttributes(CreateDocumentContext context) {
        return Try.of(() -> {
            String jsonPayload = GscUtils.toJson(context.customerLeAttributeRequestBean);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("fetchCustomerLeAttributes payload to customer service:", jsonPayload));
            }
            LOGGER.info("MDC Filter token value in before Queue call fetchCustomerLeAttributes {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct, jsonPayload);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                        String.format("fetchCustomerLeAttributes customer service response:", customerLeAttributes));
            }
            context.customerLeDetailsBean = GscUtils.fromJson(customerLeAttributes, CustomerLeDetailsBean.class);
            return context;
        }).getOrElseThrow(e -> Exceptions.wrapException(ExceptionConstants.COMMON_ERROR,
                "Error occurred while fetching LE attributes from customer service", e));
    }

    private CreateDocumentContext removeCustomerContractingEntityAttributes(CreateDocumentContext context) {
        if (NEW.equalsIgnoreCase(context.quoteToLe.getQuoteType())) {
            List<Attributes> requiredAttributesFromCustomer = context.customerLeDetailsBean.getAttributes().stream().filter(attributes ->
                    !CUSTOMER_CONTRACTING_ENTITY.equalsIgnoreCase(attributes.getAttributeName()))
                    .collect(Collectors.toList());
            context.customerLeDetailsBean.setAttributes(requiredAttributesFromCustomer);
        }
        return context;
    }

    /**
     * populateQuote
     *
     * @param context
     * @return
     */
    private Try<CreateDocumentContext> populateQuote(CreateDocumentContext context) {
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
    private Try<CreateDocumentContext> populateQuoteToLe(CreateDocumentContext context) {
        return gscQuoteService.getQuoteToLe(context.quoteToLeID).map(quoteToLe -> {
            context.quoteToLe = quoteToLe;
            return context;
        });
    }

    /**
     * returns quoteToLe contact attribute values
     *
     * @param quoteId
     * @param quoteToLeId
     * @return {@link GscContactAttributeInfo}
     * @author VISHESH AWASTHI
     */
    public GscContactAttributeInfo getContractInfo(Integer quoteId, Integer quoteToLeId) {
        GscContactAttributeInfo attributeInfo = new GscContactAttributeInfo();
        getQuoteToLeAttributes(quoteId, quoteToLeId).get().getAttributes().forEach(attrval -> {
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
     * Update opportunity in SFDC
     *
     * @param context
     * @return
     */
    private CreateDocumentContext updateOpportunityInSfdc(CreateDocumentContext context) {
//        quoteToLeRepository.findByQuote(context.quote).forEach(quoteLe -> {
//            try {
//                if (NEW.equalsIgnoreCase(quoteLe.getQuoteType())) {
//                    gscOmsSfdcComponent.getOmsSfdcService().processUpdateOpportunity(new Date(), quoteLe.getTpsSfdcOptyId(),
//                            SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteLe);
//                }
//            } catch (Exception e) {
//                Throwables.propagate(e);
//            }
//        });
        return context;

    }

    /**
     * GscProductComponentAttributeValueContext
     *
     * @author VISHESH AWASTHI
     */
    private static class GscProductComponentAttributeValueContext {
        List<QuoteProductComponent> productComponents;
        Integer quoteId;
        Quote quote;
        Integer quoteGscId;
        List<GscProductComponentBean> componentBeans;
    }

    /**
     * GscQuoteAttributeValuesContext
     *
     * @author Ramasubramanian Sankar
     */
    private static class GscQuoteAttributeValuesContext {
        Integer quoteId;
        Quote quote;
        Integer quoteToLeId;
        QuoteToLe quoteToLe;
        QuoteProductComponent productComponent;
        List<GscQuoteProductComponentsAttributeValueBean> attributes;
    }

    private static class CreateDocumentContext {
        Integer quoteId;
        Integer quoteToLeID;
        Quote quote;
        QuoteToLe quoteToLe;
        CustomerLeAttributeRequestBean customerLeAttributeRequestBean;
        CustomerLeDetailsBean customerLeDetailsBean;
        GscDocumentBean document;
        String provideName;
    }
}
