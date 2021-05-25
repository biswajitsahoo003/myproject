package com.tcl.dias.oms.gsc.macd.helpers;

import static com.tcl.dias.oms.gsc.macd.MACDConstants.ASSET_TYPE_OUTPULSE;
import static com.tcl.dias.oms.gsc.macd.MACDConstants.ASSET_TYPE_TOLL_FREE_NUMBER;
import static com.tcl.dias.oms.gsc.macd.MACDConstants.OUTPULSE_NUMBER;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_ADD_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_NUMBER_COUNT;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_PORTING_SERVICE_NEEDED;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_QUANTITY_OF_NUMBERS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.GSC_MACD_PRODUCT_REFERENCE_ORDER_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.SELECTED_TERMINATION_NUMBER_OUTPULSE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_CFG_TYPE_REFERENCE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_ACTIVE;
import static com.tcl.dias.oms.gsc.util.GscConstants.STATUS_INACTIVE;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.serviceinventory.beans.SIAssetBean;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteGscTfn;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscTfnRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.macd.MACDOrderResponse;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscIsoCountries;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.vavr.Tuple;
import io.vavr.Tuple2;

@Component
public class GscUIFNMACDHelper extends GscBaseMACDHelper {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscUIFNMACDHelper.class);

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Autowired
	GscIsoCountries gscIsoCountries;

	@Autowired
	QuoteGscTfnRepository quoteGscTfnRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Override
	public MACDOrderResponse handleAddCountryRequest(String requestType, SIServiceDetailDataBean detailDataBean,
			SIOrderDataBean orderDataBean, Map<String, Object> data) {
		Quote newQuote = cloneQuoteFromConfiguration(requestType, detailDataBean, orderDataBean, data);
		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(newQuote).get(0);
		return MACDOrderResponse.successQuote(newQuote.getId(), quoteToLe.getId(), "Quote created successfully");
	}

	private void appendProductComponentAttributes(GscProductComponentBean productComponentBean, Integer totalNumbers) {
		GscQuoteProductComponentsAttributeSimpleValueBean totalNumbersAttribute = new GscQuoteProductComponentsAttributeSimpleValueBean();
		totalNumbersAttribute.setAttributeName(ATTR_QUANTITY_OF_NUMBERS);
		totalNumbersAttribute.setDisplayValue(ATTR_QUANTITY_OF_NUMBERS);
		totalNumbersAttribute.setDescription(ATTR_QUANTITY_OF_NUMBERS);
		totalNumbersAttribute.setAttributeValue(String.valueOf(totalNumbers));
		GscQuoteProductComponentsAttributeSimpleValueBean portingRequired = new GscQuoteProductComponentsAttributeSimpleValueBean();
		portingRequired.setAttributeName(ATTR_PORTING_SERVICE_NEEDED);
		portingRequired.setDisplayValue(ATTR_PORTING_SERVICE_NEEDED);
		portingRequired.setDescription(ATTR_PORTING_SERVICE_NEEDED);
		portingRequired.setAttributeValue("No");
		GscQuoteProductComponentsAttributeSimpleValueBean portingNumbers = new GscQuoteProductComponentsAttributeSimpleValueBean();
		portingNumbers.setAttributeName(ATTR_PORTING_NUMBER_COUNT);
		portingNumbers.setDisplayValue(ATTR_PORTING_NUMBER_COUNT);
		portingNumbers.setDescription(ATTR_PORTING_NUMBER_COUNT);
		portingNumbers.setAttributeValue("0");
		productComponentBean.setAttributes(ImmutableList.of(totalNumbersAttribute, portingRequired, portingNumbers));
	}

	@Override
	public Quote cloneQuoteFromConfiguration(String requestType, SIServiceDetailDataBean serviceDetail,
			SIOrderDataBean orderDataBean, Map<String, Object> data) {
		Quote newQuote = super.cloneQuoteFromConfiguration(requestType, serviceDetail, orderDataBean, data);
		Integer newQuoteId = newQuote.getId();
		return gscQuoteService.getGscQuoteById(newQuoteId).map(quoteDataBean -> {
			Integer newSolutionId = quoteDataBean.getSolutions().get(0).getSolutionId();
			Integer newQuoteGscId = quoteDataBean.getSolutions().get(0).getGscQuotes().get(0).getId();
			Map<Tuple2<String, String>, List<SIServiceDetailDataBean>> grouped = orderDataBean.getServiceDetails()
					.stream()
					.collect(Collectors.groupingBy(bean -> Tuple.of(
							Strings.isNullOrEmpty(bean.getSourceCountryCode()) ? null : bean.getSourceCountryCode(),
							Strings.isNullOrEmpty(bean.getDestinationCountryCode()) ? null
									: bean.getDestinationCountryCode())));

			List<GscQuoteConfigurationBean> gscQuoteConfigurationBeans = grouped.entrySet().stream().map(e -> {
				Tuple2<String, String> countries = e.getKey();
				List<SIServiceDetailDataBean> siServiceDetailDataBeans = e.getValue();
				GscQuoteConfigurationBean configurationBean = new GscQuoteConfigurationBean();
				if (!Strings.isNullOrEmpty(countries._1)) {
					configurationBean.setSource(gscMACDUtils.getCountryNameForCode(countries._1));
				}
				if (!Strings.isNullOrEmpty(countries._2)) {
					configurationBean.setDestination(gscMACDUtils.getCountryNameForCode(countries._2));
				}
				// always set config type as REFERENCE for cloned country data for UIFN add
				// country request
				if (REQUEST_TYPE_ADD_COUNTRY.equalsIgnoreCase(requestType)) {
					configurationBean.setType(GSC_CFG_TYPE_REFERENCE);
				}

				return configurationBean;
			}).collect(Collectors.toList());

			gscQuoteConfigurationBeans = gscQuoteDetailService.createConfiguration(newQuoteId, newSolutionId,
					newQuoteGscId, gscQuoteConfigurationBeans);

			// clone configuration level attributes only for add country request
			if (REQUEST_TYPE_ADD_COUNTRY.equalsIgnoreCase(requestType)) {
				gscQuoteConfigurationBeans.forEach(configurationBean -> {
					String sourceCountry = Strings.isNullOrEmpty(configurationBean.getSource()) ? null
							: gscIsoCountries.forName(configurationBean.getSource()).getCode();
					String destCountry = Strings.isNullOrEmpty(configurationBean.getDestination()) ? null
							: gscIsoCountries.forName(configurationBean.getDestination()).getCode();
					List<SIServiceDetailDataBean> details = grouped.getOrDefault(Tuple.of(sourceCountry, destCountry),
							ImmutableList.of());
					List<SIAssetBean> tfnAssets = details.stream().map(SIServiceDetailDataBean::getAssets)
							.flatMap(List::stream)
							.filter(siAssetBean -> ASSET_TYPE_TOLL_FREE_NUMBER.equalsIgnoreCase(siAssetBean.getType()))
							.collect(Collectors.toList());
					// save numbers to TFN
					quoteGscDetailsRepository.findById(configurationBean.getId())
							.ifPresent(quoteGscDetail -> saveTfnAssets(tfnAssets, quoteGscDetail));

					quoteGscDetailsRepository.findById(configurationBean.getId()).ifPresent(quoteGscDetail -> {
						saveGscMacdReferenceOrderId(quoteGscDetail, serviceDetail);
					});

					Integer totalNumbers = tfnAssets.size();
					configurationBean.getProductComponents().stream()
							.filter(productComponentBean -> serviceDetail.getAccessType()
									.equalsIgnoreCase(productComponentBean.getProductComponentName()))
							.findFirst().ifPresent(productComponentBean -> {
								appendProductComponentAttributes(productComponentBean, totalNumbers);
						       if (Objects.nonNull(data.get(OUTPULSE_NUMBER))) {
							    appendSelectedOutpulseAttributes(productComponentBean, (String) data.get(OUTPULSE_NUMBER));
							    data.remove(OUTPULSE_NUMBER);
						       }
								List<SIAssetBean> outpulseAssets = details.stream()
										.map(SIServiceDetailDataBean::getAssets).flatMap(List::stream)
										.filter(siAssetBean -> ASSET_TYPE_OUTPULSE
												.equalsIgnoreCase(siAssetBean.getType()))
										.collect(Collectors.toList());
								// append termination outpulse attribute
								appendOutpulseAttributes(productComponentBean, outpulseAssets);
								gscQuoteAttributeService.processProductComponent(productComponentBean, newQuote);
							});
				});
			}

			LOGGER.info("MACD successfully created UIFN quote: {}, solution: {}, quoteGsc: {}", newQuoteId,
					newSolutionId, newQuoteGscId);
			return newQuote;
		}).get();
	}

	private void appendOutpulseAttributes(GscProductComponentBean productComponentBean,
			List<SIAssetBean> outpulseAssets) {
		if (!CollectionUtils.isEmpty(outpulseAssets)) {
			List<String> attributeValues = outpulseAssets.stream().map(SIAssetBean::getFqdn)
					.collect(Collectors.toList());
			GscQuoteProductComponentsAttributeArrayValueBean outpulseAttribute = new GscQuoteProductComponentsAttributeArrayValueBean();
			outpulseAttribute.setAttributeName(ATTR_TERMINATION_NUMBER_OUTPULSE);
			outpulseAttribute.setDescription(ATTR_TERMINATION_NUMBER_OUTPULSE);
			outpulseAttribute.setAttributeValue(attributeValues);
			List<GscQuoteProductComponentsAttributeValueBean> attributes = ImmutableList
					.<GscQuoteProductComponentsAttributeValueBean>builder().addAll(productComponentBean.getAttributes())
					.add(outpulseAttribute).build();
			productComponentBean.setAttributes(attributes);
		}

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

	private void saveTfnAssets(List<SIAssetBean> tfnAssets, QuoteGscDetail quoteGscDetail) {
		if (!CollectionUtils.isEmpty(tfnAssets)) {
			List<QuoteGscTfn> tfns = tfnAssets.stream().map(siAssetBean -> {
				QuoteGscTfn quoteGscTfn = new QuoteGscTfn();
				quoteGscTfn.setQuoteGscDetail(quoteGscDetail);
				quoteGscTfn.setTfnNumber(siAssetBean.getFqdn());
				quoteGscTfn.setIsPorted(STATUS_INACTIVE);
				quoteGscTfn.setCreatedBy(Utils.getSource());
				quoteGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
				quoteGscTfn.setStatus(STATUS_ACTIVE);
				return quoteGscTfn;
			}).collect(Collectors.toList());
			quoteGscTfnRepository.saveAll(tfns);
		}
	}

	@Override
	public List<OrderGscDetail> cloneConfiguration(SIServiceDetailDataBean configuration, SIOrderDataBean orderDataBean,
			Order newOrder, Map<String, Object> data) {
		OrderGsc orderGsc = orderToLeRepository.findByOrder(newOrder).stream()
				.findFirst()
				.map(orderGscRepository::findByOrderToLe)
				.flatMap(orderGscs -> orderGscs.stream().findFirst())
				.get();
		List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(newOrder);
		return orderDataBean.getServiceDetails()
				.stream()
				.filter(detailDataBean -> Objects.equals(detailDataBean.getGscDestinationCountry(),
						configuration.getGscDestinationCountry()))
				.map(baseConfiguration -> {
					OrderGscDetail newOrderGscDetail = createOrderGscDetail(baseConfiguration, orderGsc);
					createProductComponent(configuration, newOrderGscDetail, orderToLes.get(0));
					return newOrderGscDetail;
				})
				.collect(Collectors.toList());
	}
	private void appendSelectedOutpulseAttributes(GscProductComponentBean productComponentBean,
												  String selectedOutpulseNumber ){
		if (Objects.nonNull(selectedOutpulseNumber)) {
			GscQuoteProductComponentsAttributeSimpleValueBean outpulseNumberAttribute = new GscQuoteProductComponentsAttributeSimpleValueBean();
			outpulseNumberAttribute.setAttributeName(SELECTED_TERMINATION_NUMBER_OUTPULSE);
			outpulseNumberAttribute.setDescription(SELECTED_TERMINATION_NUMBER_OUTPULSE);
			outpulseNumberAttribute.setAttributeValue(selectedOutpulseNumber);
			List<GscQuoteProductComponentsAttributeValueBean> attributes = ImmutableList
					.<GscQuoteProductComponentsAttributeValueBean>builder().addAll(productComponentBean.getAttributes())
					.add(outpulseNumberAttribute).build();
			productComponentBean.setAttributes(attributes);
		}
	}
}
