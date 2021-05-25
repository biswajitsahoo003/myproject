package com.tcl.dias.oms.gsc.macd;

import static com.tcl.dias.oms.gsc.macd.MACDConstants.ASSET_TYPE_OUTPULSE;
import static com.tcl.dias.oms.gsc.macd.MACDConstants.ASSET_TYPE_TOLL_FREE_NUMBER;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_ACCESS_TYPE;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_ASSET_ID;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_CUSTOMER_ID;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_CUSTOMER_LE_ID;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_DOWNSTREAM_ORDER_ID;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_OUTPULSE;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.CFG_DATA_KEY_PRODUCT_NAME;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_ADD_SITE;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_CHANGE_OUTPULSE;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_NUMBER_ADD;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_NUMBER_REMOVE;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.SITE_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_OLD_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_NUMBER_COUNT;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.DOMESTIC_VOICE_SITE_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_MACD_PRODUCT_REFERENCE_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACANS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ACDTFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ITFS;
import static com.tcl.dias.oms.gsc.util.GscConstants.LNS;
import static com.tcl.dias.oms.gsc.util.GscConstants.UIFN;
import static com.tcl.dias.oms.gsc.util.GscUtils.fromJson;
import static com.tcl.dias.oms.gsc.util.GscUtils.toJson;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.VERBAL_AGREEMENT_STAGE;

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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.CustomerLeCountryCurrencyBean;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tcl.dias.common.beans.CustomerLeAttributeRequestBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.serviceinventory.beans.SIAssetBean;
import com.tcl.dias.common.serviceinventory.beans.SIAssetRelationBean;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderRequest;
import com.tcl.dias.common.serviceinventory.beans.SIGetOrderResponse;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.macd.helpers.GscGlobalOutboundMACDHelper;
import com.tcl.dias.oms.gsc.macd.helpers.GscITFSMACDHelper;
import com.tcl.dias.oms.gsc.macd.helpers.GscUIFNMACDHelper;
import com.tcl.dias.oms.gsc.macd.helpers.IGscMACDHelper;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscIsoCountries;
import com.tcl.dias.oms.gsc.util.GscRepcCountries;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.vavr.control.Try;

/**
 * Helper class to handle GSC MACD requests
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@Service
public class GscMACDUtils implements ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscMACDUtils.class);

	@Value("${rabbitmq.si.order.get.queue}")
	String getSIOrderQueue;

	@Value("${process.macd.orders.with.downstream}")
	String processMACDOrdersWithDownstreamSystem;

	@Value("${rabbitmq.customerleattr.product.queue}")
	String customerLeAttrQueueProduct;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	OrderGscTfnRepository orderGscTfnRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderGscDetailRepository orderGscDetailRepository;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	GscOrderService gscOrderService;

	@Autowired
	GscQuoteService gscQuoteService;

	@Autowired
	GscQuoteDetailService gscQuoteDetailService;

	@Autowired
	GscQuoteAttributeService gscQuoteAttributeService;

	@Autowired
	GscIsoCountries gscIsoCountries;
	
	@Autowired
	GscRepcCountries gscRepcCountries;

	@Autowired
	private OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Value("${rabbitmq.country.currency.by.customer.le.queue}")
	String countryCurrencyByLeQueue;

	@Value("${rabbitmq.customer.le.secs.queue}")
	String customerLeSecsIdQueue;

	private ApplicationContext applicationContext;

	private Map<String, GscMACDRule> requestTypeRules;

	Map<String, IGscMACDHelper> macdHelpers;

	@PostConstruct
	public void initMACDHelpers() {
		macdHelpers = ImmutableMap.<String, IGscMACDHelper>builder()
				.put(ITFS, applicationContext.getBean(GscITFSMACDHelper.class))
				.put(ACDTFS, applicationContext.getBean(GscITFSMACDHelper.class))
				.put(ACANS, applicationContext.getBean(GscITFSMACDHelper.class))
				.put(LNS, applicationContext.getBean(GscITFSMACDHelper.class))
				.put(DOMESTIC_VOICE, applicationContext.getBean(GscITFSMACDHelper.class))
				.put(UIFN, applicationContext.getBean(GscUIFNMACDHelper.class))
				.put(GLOBAL_OUTBOUND, applicationContext.getBean(GscGlobalOutboundMACDHelper.class)).build();
	}

	@PostConstruct
	public void initRequestTypeRules() {
		requestTypeRules = new HashMap<>();
		requestTypeRules.put(REQUEST_TYPE_ADD_COUNTRY,
				new GscMACDRule(REQUEST_TYPE_ADD_COUNTRY, ImmutableSet.of(REQUEST_TYPE_NUMBER_REMOVE)));
		requestTypeRules.put(REQUEST_TYPE_NUMBER_REMOVE, new GscMACDRule(REQUEST_TYPE_NUMBER_REMOVE,
				ImmutableSet.of(REQUEST_TYPE_NUMBER_REMOVE, REQUEST_TYPE_ADD_COUNTRY)));
		requestTypeRules.put(REQUEST_TYPE_NUMBER_ADD, new GscMACDRule(REQUEST_TYPE_NUMBER_ADD, ImmutableSet.of()));
		requestTypeRules.put(REQUEST_TYPE_CHANGE_OUTPULSE,
				new GscMACDRule(REQUEST_TYPE_CHANGE_OUTPULSE, ImmutableSet.of()));
	}

	private boolean containsAsset(SIServiceDetailDataBean detailDataBean, Integer assetId) {
		return Optional.ofNullable(detailDataBean.getAssets()).orElse(ImmutableList.of()).stream()
				.filter(siAssetBean -> siAssetBean.getId().equals(assetId)).findFirst().isPresent();
	}

	private SIServiceDetailDataBean findServiceDetailForAsset(Integer assetId, SIOrderDataBean orderDataBean) {
		return orderDataBean.getServiceDetails().stream()
				.filter(detailDataBean -> containsAsset(detailDataBean, assetId)).findFirst().orElseThrow(() -> {
					LOGGER.warn(String.format("Asset with id: %s, not found in SI order: %s", assetId,
							orderDataBean.getId()));
					return new TclCommonRuntimeException(ExceptionConstants.ASSET_NOT_FOUND);
				});
	}

	private Try<MACDRequestContext> getServiceInventoryOrder(MACDRequestContext context) {
		if(!context.requestType.equalsIgnoreCase(REQUEST_TYPE_ADD_SITE)){
			return Try.success(context.downstreamOrderId).flatMapTry(oId -> {
				SIGetOrderRequest getOrderRequest = new SIGetOrderRequest();
				getOrderRequest.setOrderId(context.downstreamOrderId);
				getOrderRequest.setAssets(Boolean.TRUE);
				getOrderRequest.setAssetTypes(ImmutableList.of(ASSET_TYPE_TOLL_FREE_NUMBER));
				getOrderRequest.setAssetRelationTypes(ImmutableList.of(ASSET_TYPE_OUTPULSE));
				String requestPayload = toJson(getOrderRequest);
				LOGGER.info("Sending request to getSIOrder:: {}", requestPayload);
				String response = (String) mqUtils.sendAndReceive(getSIOrderQueue, requestPayload);
				LOGGER.info("Received response from getSIOrder:: {}", response);
				SIGetOrderResponse orderResponse = fromJson(response, SIGetOrderResponse.class);
				if (CommonConstants.SUCCESS.equalsIgnoreCase(orderResponse.getStatus())) {
					context.siOrderData = orderResponse.getOrder();
					return Try.success(context);
				} else {
					return Try.failure(new RuntimeException(orderResponse.getMessage()));
				}
			});
		}
		else{
			context.siOrderData = new SIOrderDataBean();
			return Try.success(context);
		}

	}

	private static class MACDRequestContext {
		String downstreamOrderId;
		SIOrderDataBean siOrderData;
		String requestType;
		List<Integer> assetIds;
	}

	private MACDRequestContext toContext(String orderId, String requestType, List<Map<String, Object>> data, String productName) {
        MACDRequestContext context = new MACDRequestContext();
        context.requestType = requestType;
        String downstreamOrderId = orderId;
        if (Objects.isNull(downstreamOrderId) && !requestType.equalsIgnoreCase(REQUEST_TYPE_ADD_SITE)) {
            LOGGER.warn("Downstream order Id should not be empty");
            throw new TclCommonRuntimeException(ExceptionConstants.MACD_DOWNSTREAM_ORDER_ID_EMPTY,
                    ResponseResource.R_CODE_BAD_REQUEST);
        }
        List<Integer> assetIds;
        assetIds = data.stream().map(map -> {
            if (Objects.isNull(map.get(CFG_DATA_KEY_ASSET_ID)) && Objects.nonNull(productName) && !DOMESTIC_VOICE.equalsIgnoreCase(productName)) {
                LOGGER.warn("Asset Id should not be empty");
                throw new TclCommonRuntimeException(ExceptionConstants.MACD_ASSET_ID_EMPTY,
                        ResponseResource.R_CODE_BAD_REQUEST);
            } else if (Objects.nonNull(map.get(CFG_DATA_KEY_ASSET_ID))) {
                return ((Number) map.get(CFG_DATA_KEY_ASSET_ID)).intValue();
            }
            LOGGER.info("asset id is null and product is domestic voice");
            return null;
        }).collect(Collectors.toList());
        context.assetIds = assetIds;

        context.downstreamOrderId = downstreamOrderId;
        return context;
    }

	private String getExistingMACDTypeOnOrder(String downstreamOrderId) {
		return orderToLeRepository.findLatestMACDTypeByDownstreamOrder(downstreamOrderId).orElse("");
	}

	private MACDRequestContext validateBusinessRules(MACDRequestContext context) {
		String existingMACDType = getExistingMACDTypeOnOrder(context.downstreamOrderId);
		if(!context.requestType.equalsIgnoreCase(REQUEST_TYPE_ADD_SITE)) {
			if (!requestTypeRules.get(context.requestType).isThisRequestTypeAllowed(existingMACDType)) {
				String errorMessage = String.format(
						"Business validation failed: New MACD request type %s not allowed with existing MACD request type %s on SI order %s",
						context.requestType, existingMACDType, context.downstreamOrderId);
				LOGGER.warn(errorMessage);
				throw new RuntimeException(errorMessage);
			}
		}
		return context;
	}

	private List<MACDOrderResponse> handleMACDRequest(String requestType, String orderId,
                                                      List<Map<String, Object>> data, HttpServletResponse httpServletResponse, String productName) {
			return Try.success(toContext(orderId, requestType, data, productName))
					.map(this::validateBusinessRules)
					.flatMap(this::getServiceInventoryOrder).map(context -> {
						SIOrderDataBean orderDataBean = context.siOrderData;
						QuoteToLe quoteToLe=null;
						IGscMACDHelper macdHelper;
						SIServiceDetailDataBean serviceDetailDataBean = new SIServiceDetailDataBean();
						if(Objects.nonNull(productName) && DOMESTIC_VOICE.equalsIgnoreCase(productName)){
							macdHelper = macdHelpers.get(DOMESTIC_VOICE);
							serviceDetailDataBean = createServiceDetailDataBeanForDomesticVoice(productName, requestType, data, orderDataBean, serviceDetailDataBean);
						}
						else{
							serviceDetailDataBean = findServiceDetailForAsset(context.assetIds.get(0),
									orderDataBean);
							macdHelper = macdHelpers.get(serviceDetailDataBean.getGscProductName());
						}
						Quote newQuote;
						switch (requestType) {
							case REQUEST_TYPE_ADD_COUNTRY:
								MACDOrderResponse macdOrderResponse= macdHelper.handleAddCountryRequest(requestType, serviceDetailDataBean,
										orderDataBean, data.get(0));
								quoteToLe=quoteToLeRepository.findByQuote_Id(macdOrderResponse.getQuoteId()).get(0);
								setGscServiceAttributes(serviceDetailDataBean,quoteToLe);
								return ImmutableList.of(macdOrderResponse);
							case REQUEST_TYPE_CHANGE_OUTPULSE:
								serviceDetailDataBean = findSIAssetBean(orderDataBean, serviceDetailDataBean, context.assetIds);
								return handleChangeOutpulseRequest(requestType, data, orderDataBean, serviceDetailDataBean,
										macdHelper);
							case REQUEST_TYPE_NUMBER_ADD:
								 newQuote = macdHelper.cloneQuoteFromConfiguration(requestType, serviceDetailDataBean,
										orderDataBean, data.get(0));
								quoteToLe = quoteToLeRepository.findByQuote(newQuote).get(0);
								setGscReferenceOrderId(serviceDetailDataBean, quoteToLe);
								setGscServiceAttributes(serviceDetailDataBean,quoteToLe);
								setSiteAddressForDomesticVoice(serviceDetailDataBean,quoteToLe, data.get(0));
								return ImmutableList.of(MACDOrderResponse.successQuote(newQuote.getId(), quoteToLe.getId(),
										"Quote created successfully"));
							case REQUEST_TYPE_ADD_SITE:
								newQuote = macdHelper.cloneQuoteFromConfiguration(requestType, serviceDetailDataBean, orderDataBean, data.get(0));
								quoteToLe = quoteToLeRepository.findByQuote(newQuote).get(0);
								return ImmutableList.of(MACDOrderResponse.successQuote(newQuote.getId(), quoteToLe.getId(),
										"Quote created successfully"));
							case REQUEST_TYPE_NUMBER_REMOVE:
								return handleRemoveNumberRequest(requestType, data, orderDataBean, serviceDetailDataBean,
										macdHelper, httpServletResponse);
							default:
								return ImmutableList.of(MACDOrderResponse.failure("request type not supported"));
						}
					}).toEither().fold(error -> {
						LOGGER.error("Error occurred while handling MACD response", error);
						return ImmutableList.of(MACDOrderResponse.failure(error.getMessage()));
					}, Function.identity());

	}

	/**
	 * Create service details data bean for DV based on request type
	 *
	 *
	 * @param productName
	 * @param requestType
	 * @param data
	 * @param orderDataBean
	 * @param serviceDetailDataBean
	 * @return
	 */
	private SIServiceDetailDataBean createServiceDetailDataBeanForDomesticVoice(String productName, String requestType, List<Map<String, Object>> data, SIOrderDataBean orderDataBean, SIServiceDetailDataBean serviceDetailDataBean) {
		if(requestType.equalsIgnoreCase(REQUEST_TYPE_ADD_SITE)){
			CustomerLeCountryCurrencyBean countryCurrencyByLeId = getCountryCurrencyByLeId((Integer)data.get(0).get(CFG_DATA_KEY_CUSTOMER_LE_ID));
			createOrderDataBean(data, orderDataBean, countryCurrencyByLeId);
			createServiceDetailDataBean(productName, data,serviceDetailDataBean, countryCurrencyByLeId);
		}
		else{
			serviceDetailDataBean = orderDataBean.getServiceDetails().stream().findFirst().get();
		}
		return serviceDetailDataBean;
	}

	/**
	 * create service detail data bean for add site request type
	 *
	 * @param productName
	 * @param data
	 * @param serviceDetailDataBean
	 * @param countryCurrencyByLeBean
	 */
	private void createServiceDetailDataBean(String productName, List<Map<String, Object>> data, SIServiceDetailDataBean serviceDetailDataBean, CustomerLeCountryCurrencyBean countryCurrencyByLeBean) {
		serviceDetailDataBean.setAccessType((String)data.get(0).get(CFG_DATA_KEY_ACCESS_TYPE));
		serviceDetailDataBean.setGscProductName(productName);
		serviceDetailDataBean.setContractTerm(12.0);
		serviceDetailDataBean.setSourceCountryCode(getCountryCode(countryCurrencyByLeBean.getCountryName()));
	}

	/**
	 * create order data bean for add site request type
	 *
	 * @param data
	 * @param orderDataBean
	 * @param countryCurrencyByLeBean
	 */
	private void createOrderDataBean(List<Map<String, Object>> data, SIOrderDataBean orderDataBean, CustomerLeCountryCurrencyBean countryCurrencyByLeBean) {
		orderDataBean.setErfCustLeId((Integer)data.get(0).get(CFG_DATA_KEY_CUSTOMER_LE_ID));
		orderDataBean.setErfCustSpLeId(null);
		orderDataBean.setId(null);
		orderDataBean.setErfCustCurrencyId(countryCurrencyByLeBean.getCurrencyId());
		orderDataBean.setTpsSecsId(getOrgId(orderDataBean.getErfCustLeId()));
		orderDataBean.setErfCustCustomerId((String)data.get(0).get(CFG_DATA_KEY_CUSTOMER_ID));
	}

	private void setGscReferenceOrderId(SIServiceDetailDataBean serviceDetailDataBean, QuoteToLe quoteToLe) {
		quoteGscRepository.findByQuoteToLe(quoteToLe).stream().forEach(quoteGsc -> {
			quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream().forEach(quoteGscDetail -> {
				saveGscMacdReferenceOrderId(quoteGscDetail, serviceDetailDataBean);
			});
		});
	}

	public String getCountryNameForCode(String countryCode) {
		GscIsoCountries.GscCountry country = gscIsoCountries.forCode(countryCode);
		if (Objects.nonNull(country)) {
			return country.getName();
		} else {
			throw new RuntimeException(String.format("Country with code: %s not found", countryCode));
		}
	}

	private List<MACDOrderResponse> handleRemoveNumberRequest(String requestType, List<Map<String, Object>> dataList,
			SIOrderDataBean orderDataBean, SIServiceDetailDataBean serviceDetailDataBean, IGscMACDHelper macdHelper,
			HttpServletResponse httpServletResponse) {
		Quote newQuote = macdHelper.cloneQuoteFromConfiguration(requestType, serviceDetailDataBean, orderDataBean,
				dataList.get(0));
		Integer newQuoteId = newQuote.getId();
		gscQuoteService.getGscQuoteById(newQuoteId).onSuccess(quoteDataBean -> {
			Integer newSolutionId = quoteDataBean.getSolutions().get(0).getSolutionId();
			Integer newQuoteGscId = quoteDataBean.getSolutions().get(0).getGscQuotes().get(0).getId();
			List<GscQuoteConfigurationBean> newConfigurations = quoteDataBean.getSolutions().get(0).getGscQuotes().get(0)
					.getConfigurations();
			// add cancellation number count to quote configuration attributes
			newConfigurations.stream()
					.map(GscQuoteConfigurationBean::getProductComponents)
					.flatMap(List::stream)
					.filter(productComponentBean -> serviceDetailDataBean.getAccessType()
							.equalsIgnoreCase(productComponentBean.getProductComponentName()))
					.forEach(productComponentBean -> {
						String numberCount = String.valueOf(dataList.size());
						GscQuoteProductComponentsAttributeSimpleValueBean totalNumbersAttribute = new GscQuoteProductComponentsAttributeSimpleValueBean();
						totalNumbersAttribute.setAttributeName(ATTR_QUANTITY_OF_NUMBERS);
						totalNumbersAttribute.setDisplayValue(numberCount);
						totalNumbersAttribute.setDescription(ATTR_QUANTITY_OF_NUMBERS);
						totalNumbersAttribute.setAttributeValue(numberCount);
						GscQuoteProductComponentsAttributeSimpleValueBean portingRequired = new GscQuoteProductComponentsAttributeSimpleValueBean();
						portingRequired.setAttributeName(ATTR_PORTING_SERVICE_NEEDED);
						portingRequired.setDisplayValue("No");
						portingRequired.setDescription(ATTR_PORTING_SERVICE_NEEDED);
						portingRequired.setAttributeValue("No");
						GscQuoteProductComponentsAttributeSimpleValueBean portingNumbers = new GscQuoteProductComponentsAttributeSimpleValueBean();
						portingNumbers.setAttributeName(ATTR_PORTING_NUMBER_COUNT);
						portingNumbers.setDisplayValue("0");
						portingNumbers.setDescription(ATTR_PORTING_NUMBER_COUNT);
						portingNumbers.setAttributeValue("0");
						productComponentBean.setAttributes(
								ImmutableList.of(totalNumbersAttribute, portingRequired, portingNumbers));
						gscQuoteAttributeService.processProductComponent(productComponentBean, newQuote);
					});
			LOGGER.info("MACD successfully created quote: {}, solution: {}, quoteGsc: {},configurations: {}", newQuoteId,
					newSolutionId, newQuoteGscId, newConfigurations.stream()
							.map(configurationBean -> String.valueOf(configurationBean.getId()))
							.collect(Collectors.joining(",")));
		}).onFailure(Throwables::propagate);

		// create order by approving quote instead of direct creation
		GscOrderDataBean gscOrderDataBean = gscOrderService.approveQuotes(newQuoteId, httpServletResponse);
		Order newOrder = orderRepository.findById(gscOrderDataBean.getOrderId()).get();
		List<OrderGscDetail> newOrderGscDetails = orderGscDetailRepository.findAllById((gscOrderDataBean.getSolutions()
				.get(0)
				.getGscOrders()
				.get(0)
				.getConfigurations()
				.stream()
				.map(GscOrderConfigurationBean::getId)
				.collect(Collectors.toList())));
		OrderToLe orderToLe = orderToLeRepository.findByOrder(newOrder).get(0);
		orderToLe.setOrderCategory(requestType);
		orderToLeRepository.save(orderToLe);

		newOrderGscDetails.forEach(orderGscDetail -> {
			GscOrderStatusStageUpdate updateRequest = new GscOrderStatusStageUpdate();
			updateRequest.setConfigurationStatusName(QuoteStageConstants.MACD_ORDER_IN_PROGRESS.toString());
			gscOrderService.updateOrderConfigurationStageStatus(orderGscDetail.getId(), updateRequest);

			MstProductComponent mstProductComponent = mstProductComponentRepository.findByNameAndStatus(
					serviceDetailDataBean.getAccessType().toUpperCase(), GscConstants.STATUS_ACTIVE).get(0);

			OrderProductComponent component = orderProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndType(orderGscDetail.getId(), mstProductComponent,
							GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE)
					.get(0);

			ProductAttributeMaster gscReferenceOrderAttribute = Optional
					.ofNullable(productAttributeMasterRepository.findByNameAndStatus(GSC_MACD_PRODUCT_REFERENCE_ORDER_ID,
							GscConstants.STATUS_ACTIVE))
					.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
						LOGGER.warn("Unable to find attribute master with name: {}", GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
						return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
					});

			// save gsc macd product reference order id
			OrderProductComponentsAttributeValue gscReferenceOrder = new OrderProductComponentsAttributeValue();
			gscReferenceOrder.setOrderProductComponent(component);
			gscReferenceOrder.setProductAttributeMaster(gscReferenceOrderAttribute);
			gscReferenceOrder.setAttributeValues(serviceDetailDataBean.getReferenceOrderId());
			gscReferenceOrder.setDisplayValue(serviceDetailDataBean.getReferenceOrderId());
			orderProductComponentsAttributeValueRepository.save(gscReferenceOrder);

            // save gsc macd service inventory attribute
			Map<String,String> attribute=getGscServiceAttribute(serviceDetailDataBean);
			if(!attribute.isEmpty()){
				attribute.forEach((key,value)->{
					saveOrderProductComponentsAttributeValue(component,key,value);
				});
			}

		});

		List<MACDOrderResponse> responses = dataList.stream().map(data -> {
			newOrderGscDetails.forEach(orderGscDetail -> {
				OrderGscTfn orderGscTfn = new OrderGscTfn();
				orderGscTfn.setOrderGscDetail(orderGscDetail);
				orderGscTfn.setTfnNumber(serviceDetailDataBean.getAssets().get(0).getFqdn());
				orderGscTfn.setIsPorted(GscConstants.STATUS_INACTIVE);
				orderGscTfn.setCreatedBy(Utils.getSource());
				orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
				orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
				orderGscTfn.setAction(GscConstants.TFN_ACTION_CANCEL);
				orderGscTfn.setCountryCode(gscIsoCountries.forName(orderGscDetail.getSrc()).getCode());
				String comment = (String) data.get(MACDOrderRequest.CFG_DATA_KEY_COMMENT);
				if (!Strings.isNullOrEmpty(comment)) {
					orderGscTfn.setPortedFrom(String.format("COMMENT:%s", StringUtils.substring(comment, 0, 140)));
				}
				orderGscTfnRepository.save(orderGscTfn);
			});
			return MACDOrderResponse.successOrder(newOrder.getId(), newOrder.getOrderCode(),
					"Order created successfully");
		}).collect(Collectors.toList());
		completeOrder(newOrder);
		sfdcUpdateForRemoveNumber(newQuote);
		return responses;
	}

	private void completeOrder(Order order) {
		if (GscConstants.TRUE_FLAG.equalsIgnoreCase(processMACDOrdersWithDownstreamSystem)) {
			Integer orderToLeId = orderToLeRepository.findByOrder(order).stream().findFirst().get().getId();
			try {
				//gscOrderService.updateOrderToLeStatus(orderToLeId, OrderStagingConstants.ORDER_IN_PROGRESS.getStage());
			} catch (Exception e) {
				Throwables.propagate(e);
			}
		} else {
			order.setStage(OrderStagingConstants.ORDER_IN_PROGRESS.getStage().toUpperCase());
			OrderToLe orderToLe = orderToLeRepository.findByOrder(order).get(0);
			orderToLe.setStage(OrderStagingConstants.ORDER_IN_PROGRESS.getStage().toUpperCase());
			orderToLeRepository.save(orderToLe);
			orderRepository.save(order);
		}

		saveSFDCOpportunityID(order);
	}

	private void saveSFDCOpportunityID(Order order) {
		if (Objects.isNull(orderToLeRepository.findByOrder(order).stream().findFirst().get().getTpsSfdcCopfId())) {
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_QuoteCode(order.getOrderCode());
			quoteToLes.stream().findFirst().map(QuoteToLe::getTpsSfdcOptyId).ifPresent(sfdcOpportunityId -> {
				List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(order);
				orderToLes.forEach(orderToLe -> {
					orderToLe.setTpsSfdcCopfId(sfdcOpportunityId);
					orderToLeRepository.save(orderToLe);
				});
			});
		}
	}

	private void sfdcUpdateForRemoveNumber(Quote quote) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_QuoteCode(quote.getQuoteCode());
		quoteToLes.forEach(quoteToLe -> {
			try {
				gscQuoteService.updateAllStages(quoteToLe, VERBAL_AGREEMENT_STAGE);
			} catch (TclCommonException e) {
				LOGGER.warn("SFDC stage update for remove number failed :: {}", e);
			}
		});
	}

	private CustomerLeDetailsBean getCustomerLeDetails(Integer customerLeId, String productName) {
		return Try.of(() -> {
			CustomerLeAttributeRequestBean requestBean = new CustomerLeAttributeRequestBean();
			requestBean.setCustomerLeId(customerLeId);
			requestBean.setProductName(productName);
			String jsonPayload = GscUtils.toJson(requestBean);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("getCustomerLeDetails payload to customer service:", jsonPayload));
			}
			LOGGER.info("MDC Filter token value in before Queue call getCustomerLeDetails {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeAttrQueueProduct, jsonPayload);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("getCustomerLeDetails customer service response:", customerLeAttributes));
			}
			return GscUtils.fromJson(customerLeAttributes, CustomerLeDetailsBean.class);
		}).getOrElseThrow(e -> {
			LOGGER.warn("Error occurred while fetching LE attributes from customer service");
			return new TclCommonRuntimeException(e);
		});
	}

	private void saveOrderToLeAttribute(OrderToLe orderToLe, String attrValue, String attributeName) {
		Set<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository
				.findByMstOmsAttribute_NameAndOrderToLe(attributeName, orderToLe);
		if (ordersLeAttributeValues == null || ordersLeAttributeValues.isEmpty()) {
			OrdersLeAttributeValue attributeValue = new OrdersLeAttributeValue();
			attributeValue.setAttributeValue(attrValue);
			attributeValue.setDisplayValue(attributeName);
			attributeValue.setOrderToLe(orderToLe);
			MstOmsAttribute mstOmsAttribute = gscQuoteAttributeService.getMstAttributeMasterForBilling(attributeName);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			ordersLeAttributeValues.add(attributeValue);
		}
		ordersLeAttributeValues.forEach(attr -> {
			attr.setAttributeValue(attrValue);
			attr.setDisplayValue(attributeName);
			ordersLeAttributeValueRepository.save(attr);
		});
	}

	private void saveCustomerLeAttributes(OrderToLe orderToLe, CustomerLeDetailsBean customerLeDetailsBean) {
		saveOrderToLeAttribute(orderToLe, customerLeDetailsBean.getAccounCuId(),
				LeAttributesConstants.ACCOUNT_CUID);
		saveOrderToLeAttribute(orderToLe, customerLeDetailsBean.getAccountId(),
				LeAttributesConstants.ACCOUNT_NO18);
		saveOrderToLeAttribute(orderToLe, String.valueOf(customerLeDetailsBean.getBillingContactId()),
				LeAttributesConstants.BILLING_CONTACT_ID);
		saveOrderToLeAttribute(orderToLe, customerLeDetailsBean.getLegalEntityName(),
				LeAttributesConstants.LEGAL_ENTITY_NAME);

		customerLeDetailsBean.getAttributes().stream().forEach(attributes -> {
			saveOrderToLeAttribute(orderToLe, attributes.getAttributeValue(),
					attributes.getAttributeName());
		});
	}

	private List<MACDOrderResponse> handleChangeOutpulseRequest(String requestType, List<Map<String, Object>> dataList,
			SIOrderDataBean orderDataBean, SIServiceDetailDataBean serviceDetailDataBean, IGscMACDHelper macdHelper) {
		Order newOrder = macdHelper.cloneOrderFromConfiguration(serviceDetailDataBean, orderDataBean, dataList.get(0));
		List<OrderGscDetail> newOrderGscDetails = macdHelper.cloneConfiguration(serviceDetailDataBean, orderDataBean, newOrder,
				dataList.get(0));
		OrderToLe orderToLe = orderToLeRepository.findByOrder(newOrder).get(0);
		orderToLe.setOrderCategory(requestType);
		if(Objects.nonNull(serviceDetailDataBean.getContractTerm())) {
			//PT-14232 Temp fix - Convert Double to Int
			orderToLe.setTermInMonths(serviceDetailDataBean.getContractTerm().intValue() + " months");
		} else {
			orderToLe.setTermInMonths("12 months");
		}

		orderToLe = orderToLeRepository.save(orderToLe);

		//save customer LE attributes
		CustomerLeDetailsBean customerLeDetailsBean = getCustomerLeDetails(orderDataBean.getErfCustLeId(), GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase());
		saveCustomerLeAttributes(orderToLe, customerLeDetailsBean);

		newOrderGscDetails.forEach(newOrderGscDetail -> {
			GscOrderStatusStageUpdate updateRequest = new GscOrderStatusStageUpdate();
			updateRequest.setConfigurationStatusName(QuoteStageConstants.MACD_ORDER_IN_PROGRESS.toString());
			gscOrderService.updateOrderConfigurationStageStatus(newOrderGscDetail.getId(), updateRequest);
		});

		List<MACDOrderResponse> macdOrderResponses = dataList.stream().map(data -> {
			newOrderGscDetails.forEach(newOrderGscDetail -> {
				OrderGscTfn orderGscTfn = new OrderGscTfn();
				orderGscTfn.setOrderGscDetail(newOrderGscDetail);
				orderGscTfn.setTfnNumber(serviceDetailDataBean.getAssets().get(0).getFqdn());
				orderGscTfn.setIsPorted(GscConstants.STATUS_INACTIVE);
				orderGscTfn.setCreatedBy(Utils.getSource());
				orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
				orderGscTfn.setStatus(GscConstants.STATUS_ACTIVE);
				orderGscTfn.setCountryCode(newOrderGscDetail.getSrc());
				orderGscTfnRepository.save(orderGscTfn);

				MstProductComponent mstProductComponent = mstProductComponentRepository.findByNameAndStatus(
						serviceDetailDataBean.getAccessType().toUpperCase(), GscConstants.STATUS_ACTIVE).get(0);

				OrderProductComponent component = orderProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndType(newOrderGscDetail.getId(), mstProductComponent,
								GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE)
						.get(0);
				ProductAttributeMaster attributeMaster = Optional
						.ofNullable(productAttributeMasterRepository.findByNameAndStatus(ATTR_TERMINATION_NUMBER_OUTPULSE,
								GscConstants.STATUS_ACTIVE))
						.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
							LOGGER.warn("Unable to find attribute master with name: {}", ATTR_TERMINATION_NUMBER_OUTPULSE);
							return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
						});

				ProductAttributeMaster gscReferenceOrderAttribute = Optional
						.ofNullable(productAttributeMasterRepository.findByNameAndStatus(GSC_MACD_PRODUCT_REFERENCE_ORDER_ID,
								GscConstants.STATUS_ACTIVE))
						.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
							LOGGER.warn("Unable to find attribute master with name: {}", GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
							return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
						});

				String outpulse = (String) data.get(CFG_DATA_KEY_OUTPULSE);
				if (Objects.isNull(outpulse)) {
					LOGGER.warn("terminating outpulse is mandatory for change outpulse request: {}", data);
					throw new TclCommonRuntimeException(ExceptionConstants.MACD_OUTPULSE_EMPTY,
							ResponseResource.R_CODE_BAD_REQUEST);
				}
				//save existing outpulse under old outpulse attribute
				Integer assetId = ((Number) data.get(CFG_DATA_KEY_ASSET_ID)).intValue();
				serviceDetailDataBean.getAssetRelations().stream()
						.filter(siAssetRelationBean -> assetId.equals(siAssetRelationBean.getAssetId())
								&& MACDConstants.ASSET_TYPE_OUTPULSE.equalsIgnoreCase(
								siAssetRelationBean.getRelationType()))
						.findFirst()
						.map(SIAssetRelationBean::getSiRelatedAssetId)
						.flatMap(relatedAssetId -> serviceDetailDataBean.getAssets().stream()
								.filter(siAssetBean -> siAssetBean.getId().equals(relatedAssetId))
								.findFirst())
						.ifPresent(outpulseAsset -> {
							ProductAttributeMaster oldOutpulseMaster = Optional.ofNullable(
									productAttributeMasterRepository.findByNameAndStatus(
											ATTR_OLD_TERMINATION_NUMBER_OUTPULSE, GscConstants.STATUS_ACTIVE))
									.orElse(ImmutableList.of()).stream()
									.findFirst()
									.orElseThrow(() -> {
										LOGGER.warn("Unable to find attribute master with name: {}",
												ATTR_OLD_TERMINATION_NUMBER_OUTPULSE);
										return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY,
												ResponseResource.R_CODE_ERROR);
									});
							OrderProductComponentsAttributeValue outpulseAttribute = new OrderProductComponentsAttributeValue();
							outpulseAttribute.setOrderProductComponent(component);
							outpulseAttribute.setProductAttributeMaster(oldOutpulseMaster);
							outpulseAttribute.setAttributeValues(outpulseAsset.getFqdn());
							outpulseAttribute.setDisplayValue(outpulseAsset.getFqdn());
							orderProductComponentsAttributeValueRepository.save(outpulseAttribute);
						});
				//save new outpulse in attribute
				OrderProductComponentsAttributeValue attributeValue = new OrderProductComponentsAttributeValue();
				attributeValue.setOrderProductComponent(component);
				attributeValue.setProductAttributeMaster(attributeMaster);
				attributeValue.setAttributeValues(outpulse);
				attributeValue.setDisplayValue(outpulse);
				orderProductComponentsAttributeValueRepository.save(attributeValue);

				// save gsc macd product reference order id
				OrderProductComponentsAttributeValue gscReferenceOrder = new OrderProductComponentsAttributeValue();
				gscReferenceOrder.setOrderProductComponent(component);
				gscReferenceOrder.setProductAttributeMaster(gscReferenceOrderAttribute);
				gscReferenceOrder.setAttributeValues(serviceDetailDataBean.getReferenceOrderId());
				gscReferenceOrder.setDisplayValue(serviceDetailDataBean.getReferenceOrderId());
				orderProductComponentsAttributeValueRepository.save(gscReferenceOrder);

				// save gsc macd service inventory attribute
				Map<String,String> attribute=getGscServiceAttribute(serviceDetailDataBean);
				if(!attribute.isEmpty()){
					attribute.forEach((key,value)->{
						saveOrderProductComponentsAttributeValue(component,key,value);
					});
				}
			});

			return MACDOrderResponse.successOrder(newOrder.getId(), newOrder.getOrderCode(),
					"Order created successfully");
		}).collect(Collectors.toList());

		// set order status to complete and process downstream requests
		completeOrder(newOrder);
		return macdOrderResponses;
	}

	public List<MACDOrderResponse> handleMACDRequest(MACDOrderRequest request,
			HttpServletResponse httpServletResponse) {
		Objects.requireNonNull(request, "Request cannot be null");
		String requestType = request.getRequestType();
		String productName = request.getProductName();
		List<Map<String, Object>> dataList = request.getData();
		if(!requestType.equalsIgnoreCase(REQUEST_TYPE_ADD_SITE)){
			Map<String, List<Map<String, Object>>> orderIdWiseRequests = dataList.stream()
					.collect(Collectors.groupingBy(data -> (String) data.get(CFG_DATA_KEY_DOWNSTREAM_ORDER_ID)));
			return orderIdWiseRequests.entrySet().stream()
					.map(e -> handleMACDRequest(requestType, e.getKey(), e.getValue(), httpServletResponse, productName))
					.flatMap(List::stream).collect(Collectors.toList());
		}
		else {
			return handleMACDRequest(requestType, null, request.getData(), httpServletResponse, productName);
		}
	}

	private void saveSiteAddressForDomesticVoice(QuoteGscDetail quoteGscDetail, SIServiceDetailDataBean serviceDetailDataBean, List<String> siteAddresses) {
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByNameAndStatus(
				serviceDetailDataBean.getAccessType().toUpperCase(), GscConstants.STATUS_ACTIVE).get(0);

		QuoteProductComponent component = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(quoteGscDetail.getId(), mstProductComponent,
						GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE).get();

		ProductAttributeMaster gscDidSiteAddressAttribute = Optional
				.ofNullable(productAttributeMasterRepository.findByNameAndStatus(DOMESTIC_VOICE_SITE_ADDRESS,
						GscConstants.STATUS_ACTIVE))
				.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
					LOGGER.warn("Unable to find attribute master with name: {}", DOMESTIC_VOICE_SITE_ADDRESS);
					return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
				});

		// save gsc macd product reference order id
		QuoteProductComponentsAttributeValue gscReferenceOrder = new QuoteProductComponentsAttributeValue();
		gscReferenceOrder.setQuoteProductComponent(component);
		gscReferenceOrder.setProductAttributeMaster(gscDidSiteAddressAttribute);
		String didSiteAddress = siteAddresses.stream().map(String::valueOf).collect(Collectors.joining("|"));
		LOGGER.info("DID Site Address :: {}", didSiteAddress);
		gscReferenceOrder.setAttributeValues(didSiteAddress);
		gscReferenceOrder.setDisplayValue(didSiteAddress);
		quoteProductComponentsAttributeValueRepository.save(gscReferenceOrder);
	}

	private void saveGscMacdReferenceOrderId(QuoteGscDetail quoteGscDetail, SIServiceDetailDataBean serviceDetailDataBean) {
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByNameAndStatus(
				serviceDetailDataBean.getAccessType().toUpperCase(), GscConstants.STATUS_ACTIVE).get(0);

		QuoteProductComponent component = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(quoteGscDetail.getId(), mstProductComponent,
						GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE).get();

		ProductAttributeMaster gscReferenceOrderAttribute = Optional
				.ofNullable(productAttributeMasterRepository.findByNameAndStatus(GSC_MACD_PRODUCT_REFERENCE_ORDER_ID,
						GscConstants.STATUS_ACTIVE))
				.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
					LOGGER.warn("Unable to find attribute master with name: {}", GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
					return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
				});

		// save gsc macd product reference order id
		QuoteProductComponentsAttributeValue gscReferenceOrder = new QuoteProductComponentsAttributeValue();
		gscReferenceOrder.setQuoteProductComponent(component);
		gscReferenceOrder.setProductAttributeMaster(gscReferenceOrderAttribute);
		gscReferenceOrder.setAttributeValues(serviceDetailDataBean.getReferenceOrderId());
		gscReferenceOrder.setDisplayValue(serviceDetailDataBean.getReferenceOrderId());
		quoteProductComponentsAttributeValueRepository.save(gscReferenceOrder);
	}

	private void saveDomesticVoiceSiteAddress(QuoteGscDetail quoteGscDetail, SIServiceDetailDataBean serviceDetailDataBean) {
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByNameAndStatus(
				serviceDetailDataBean.getAccessType().toUpperCase(), GscConstants.STATUS_ACTIVE).get(0);

		QuoteProductComponent component = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(quoteGscDetail.getId(), mstProductComponent,
						GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE).get();

		ProductAttributeMaster gscReferenceOrderAttribute = Optional
				.ofNullable(productAttributeMasterRepository.findByNameAndStatus(GSC_MACD_PRODUCT_REFERENCE_ORDER_ID,
						GscConstants.STATUS_ACTIVE))
				.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
					LOGGER.warn("Unable to find attribute master with name: {}", GSC_MACD_PRODUCT_REFERENCE_ORDER_ID);
					return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
				});

		// save gsc macd product reference order id
		QuoteProductComponentsAttributeValue gscReferenceOrder = new QuoteProductComponentsAttributeValue();
		gscReferenceOrder.setQuoteProductComponent(component);
		gscReferenceOrder.setProductAttributeMaster(gscReferenceOrderAttribute);
		gscReferenceOrder.setAttributeValues(serviceDetailDataBean.getReferenceOrderId());
		gscReferenceOrder.setDisplayValue(serviceDetailDataBean.getReferenceOrderId());
		quoteProductComponentsAttributeValueRepository.save(gscReferenceOrder);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private SIServiceDetailDataBean findSIAssetBean(SIOrderDataBean orderDataBean,
			SIServiceDetailDataBean siServiceDetailDataBean, List<Integer> assetIds) {
		List<SIAssetBean> siAssetBeans = new ArrayList<>();
		List<SIAssetRelationBean> assetRelations = new ArrayList<>();
		assetIds.stream().forEach(assetId -> {
			orderDataBean.getServiceDetails().stream().forEach(detailDataBean -> {
				detailDataBean.getAssets().stream().forEach(siAssetBean -> {
					if (assetId.equals(siAssetBean.getId())) {
						siAssetBeans.addAll(detailDataBean.getAssets());
						assetRelations.addAll(detailDataBean.getAssetRelations());
					}
				});
			});
		});
		siServiceDetailDataBean.setAssets(siAssetBeans);
		siServiceDetailDataBean.setAssetRelations(assetRelations);
		return siServiceDetailDataBean;
	}

	private void setGscServiceAttributes(SIServiceDetailDataBean serviceDetailDataBean, QuoteToLe quoteToLe) {
		quoteGscRepository.findByQuoteToLe(quoteToLe).stream().forEach(quoteGsc -> {
			quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream().forEach(quoteGscDetail -> {
				saveSIServiceAttributes(quoteGscDetail,serviceDetailDataBean );
			});
		});
	}

	private void setSiteAddressForDomesticVoice(SIServiceDetailDataBean serviceDetailDataBean, QuoteToLe quoteToLe, Map<String, Object> requestData) {
		List<String> siteAddresses = (List<String>) requestData.get(SITE_ADDRESS);
		if(!CollectionUtils.isEmpty(siteAddresses)) {
			quoteGscRepository.findByQuoteToLe(quoteToLe).stream().forEach(quoteGsc -> {
				quoteGscDetailsRepository.findByQuoteGsc(quoteGsc).stream().forEach(quoteGscDetail -> {
					saveSiteAddressForDomesticVoice(quoteGscDetail, serviceDetailDataBean, siteAddresses);
				});
			});
		}
	}

	private void saveQuoteProductComponentsAttributeValue(QuoteProductComponent component,
														  String productAttributeMasterName,String attributeValue){
		ProductAttributeMaster gscProductMstAttribute = Optional
				.ofNullable(productAttributeMasterRepository.findByNameAndStatus(productAttributeMasterName,
						GscConstants.STATUS_ACTIVE))
				.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
					LOGGER.warn("Unable to find attribute master with name: {}", productAttributeMasterName);
					return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
				});

		// save gsc macd si service attributs
		QuoteProductComponentsAttributeValue componentsAttributeValue = new QuoteProductComponentsAttributeValue();
		componentsAttributeValue.setQuoteProductComponent(component);
		componentsAttributeValue.setProductAttributeMaster(gscProductMstAttribute);
		componentsAttributeValue.setAttributeValues(attributeValue);
		componentsAttributeValue.setDisplayValue(productAttributeMasterName);
		quoteProductComponentsAttributeValueRepository.save(componentsAttributeValue);
	}
    private void saveOrderProductComponentsAttributeValue(OrderProductComponent component,
														  String productAttributeMasterName,String attributeValue){

		ProductAttributeMaster gscProductMstAttribute = Optional
				.ofNullable(productAttributeMasterRepository.findByNameAndStatus(productAttributeMasterName,
						GscConstants.STATUS_ACTIVE))
				.orElse(ImmutableList.of()).stream().findFirst().orElseThrow(() -> {
					LOGGER.warn("Unable to find attribute master with name: {}", productAttributeMasterName);
					return new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_ERROR);
				});
		OrderProductComponentsAttributeValue componentsAttributeValue = new OrderProductComponentsAttributeValue();
		componentsAttributeValue.setOrderProductComponent(component);
		componentsAttributeValue.setProductAttributeMaster(gscProductMstAttribute);
		componentsAttributeValue.setAttributeValues(attributeValue);
		componentsAttributeValue.setDisplayValue(productAttributeMasterName);
		orderProductComponentsAttributeValueRepository.save(componentsAttributeValue);
	}
	private void saveSIServiceAttributes(QuoteGscDetail quoteGscDetail,SIServiceDetailDataBean siServiceDetailDataBean) {
		{
			MstProductComponent mstProductComponent = mstProductComponentRepository.findByNameAndStatus(
					siServiceDetailDataBean.getAccessType().toUpperCase(), GscConstants.STATUS_ACTIVE).get(0);

			QuoteProductComponent component = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndType(quoteGscDetail.getId(), mstProductComponent,
							GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE).get();
			Map<String,String> attribute=getGscServiceAttribute(siServiceDetailDataBean);
			if(!attribute.isEmpty()){
				attribute.forEach((key,value)->{
					saveQuoteProductComponentsAttributeValue(component,key,value);
				});
			}
		}
	}

	public Map<String,String> getGscServiceAttribute(SIServiceDetailDataBean serviceDetailDataBean) {
		Map<String,String> attribute=new HashMap<>();
		List<String> switchUnits = new ArrayList<>();
		List<String> circuitUnits = new ArrayList();
		List<String> circuitIds = new ArrayList();
			serviceDetailDataBean.getAttributes().stream().forEach(siAttribute -> {
				if(Objects.nonNull(siAttribute.getValue()) && GscConstants.SWTCH_UNIT_CD_RERT.equalsIgnoreCase(siAttribute.getName())) {
					switchUnits.add(siAttribute.getValue());
				}
				if(Objects.nonNull(siAttribute.getValue()) && GscConstants.CIRCT_GR_CD_RERT.equalsIgnoreCase(siAttribute.getName())) {
					circuitUnits.add(siAttribute.getValue());
				}
				if(Objects.nonNull(siAttribute.getValue()) && GscConstants.CIRCUIT_ID.equalsIgnoreCase(siAttribute.getName())) {
					circuitIds.add(siAttribute.getValue());
				}
			});
        if(!switchUnits.isEmpty())
 		attribute.put(GscConstants.SWTCH_UNIT_CD_RERT,String.join(",", switchUnits));
        if(!circuitUnits.isEmpty())
		attribute.put(GscConstants.CIRCT_GR_CD_RERT,String.join(",", circuitUnits));
		if(!circuitIds.isEmpty())
		attribute.put(GscConstants.CIRCUIT_ID,String.join(",", circuitIds));

		return attribute;

	}

	//Method to get country code based on country name
	public String getCountryCodeForCountry(String countryName) {
		GscIsoCountries.GscCountry country = gscIsoCountries.forName(countryName);
		if (Objects.nonNull(country)) {
			return country.getCode();
		} else {
			throw new RuntimeException(String.format("Country Name: %s not found", countryName));
		}
	}
	/**
	 * Method to get country and currency  from le
	 * @param customerLeId
	 * @return
	 */
	public CustomerLeCountryCurrencyBean getCountryCurrencyByLeId(Integer customerLeId) {
		String response;
		CustomerLeCountryCurrencyBean customerLeCountryCurrencyBean;
		try {
			LOGGER.info("MDC Filter token value in before Queue call country and currency id by customer le {} :");
			response = (String) mqUtils.sendAndReceive(countryCurrencyByLeQueue, customerLeId.toString());
			customerLeCountryCurrencyBean = Utils.convertJsonToObject(response, CustomerLeCountryCurrencyBean.class);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COUNTRY_CURRENCY_ID_BY_LE_MQ_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		if (Objects.isNull(response)) {
			throw new TclCommonRuntimeException(ExceptionConstants.COUNTRY_CURRENCY_ID_BY_LE_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		return customerLeCountryCurrencyBean;
	}

	/**
	 * get org id
	 *
	 * @param erfCusCustomerLegalEntityId
	 * @return
	 */
	private String getOrgId(Integer erfCusCustomerLegalEntityId) {
		String orgId = "";
		LOGGER.info("MDC Filter token value in before Queue call getCustomerLeCuId {} {} :", erfCusCustomerLegalEntityId, MDC.get(CommonConstants.MDC_TOKEN_KEY));
		try {
			if (Objects.nonNull(erfCusCustomerLegalEntityId)) {
				orgId = (String) mqUtils.sendAndReceive(customerLeSecsIdQueue, String.valueOf(erfCusCustomerLegalEntityId));
				LOGGER.info("org is is {} ", orgId);
			}
		} catch (TclCommonException e) {
			LOGGER.warn("Error Occured while getting CustomerLe SECS ID :: {}", e.getStackTrace());
		}
		return orgId;
	}

	private String getCountryCode(String countryName){
		List<GscIsoCountries.GscCountry> filteredCountryList = GscUtils.fromJsonFile("gsc/gsc_countries_iso.json", new TypeReference<List<GscIsoCountries.GscCountry>>() {
		}).stream().filter(gscCountry -> gscCountry.getName().equalsIgnoreCase(countryName)).collect(Collectors.toList());
		if(!CollectionUtils.isEmpty(filteredCountryList)){
			return filteredCountryList.stream().findFirst().get().getCode();
		}
		return null;
	}
	
	
	//Method to get repc country code based on country name
	public String getRepcCountryCodeForCountry(String countryName) {
		GscRepcCountries.GscCountry country = gscRepcCountries.forName(countryName);
		if (Objects.nonNull(country)) {
			return country.getCode();
		} else {
			throw new RuntimeException(String.format("Repc Country Name: %s not found", countryName));
		}
	}
}
