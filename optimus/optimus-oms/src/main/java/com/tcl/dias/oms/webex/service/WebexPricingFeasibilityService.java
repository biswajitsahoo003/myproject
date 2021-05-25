package com.tcl.dias.oms.webex.service;

import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_LE_ID_NULL_MESSAGE;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.entity.entities.QuoteUcaasDetail;
import com.tcl.dias.oms.entity.repository.QuoteUcaasDetailsRepository;
import com.tcl.dias.oms.webex.beans.PricingUcaasRequestBean;
import com.tcl.dias.oms.webex.beans.QuoteUcaasBean;
import com.tcl.dias.oms.webex.beans.WebexPricingResponseWrapper;
import com.tcl.dias.oms.webex.beans.WebexQuotePricingRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.cisco.beans.AddressType;
import com.tcl.dias.oms.cisco.beans.LocationABIEType;
import com.tcl.dias.oms.cisco.beans.LocationType;
import com.tcl.dias.oms.cisco.beans.PartyBaseType;
import com.tcl.dias.oms.cisco.beans.PartyType;
import com.tcl.dias.oms.cisco.beans.QuoteLineType;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.webex.beans.CumulativePricesBean;
import com.tcl.dias.oms.webex.beans.WebexLicenseManualPricingBean;
import com.tcl.dias.oms.webex.beans.WebexPricingResponse;
import com.tcl.dias.oms.webex.beans.WebexQuotePricingBean;
import com.tcl.dias.oms.webex.beans.WebexQuotePricingRequest;
import com.tcl.dias.oms.webex.beans.WebexUcaasQuotePriceBean;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class contains pricing related functions.
 *
 * @author ssyed ali.
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class WebexPricingFeasibilityService {

	public static final Logger LOGGER = LoggerFactory.getLogger(WebexPricingFeasibilityService.class);

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	RestClientService restClientService;

	@Value("${ucaas.pricing.request.url}")
	String pricingUrl;

	@Autowired
	QuoteUcaasDetailsRepository quoteUcaasDetailsRepository;

	/*
	 * Static class For WebexPricingFeasibilityContext
	 *
	 */
	private static class WebexPricingFeasibilityContext {
		QuoteToLe quoteToLe;
		WebexQuotePricingRequest webexQuotePricingRequest;
		WebexQuotePricingBean webexQuotePricingBean;
		WebexPricingResponse webexPricingResponse;
		List<QuoteUcaasBean> ucaasQuotes;
	}

	/**
	 * Create WebexPricingFeasibilityContext
	 * 
	 * @param requestBean
	 * @return
	 */
	public WebexPricingFeasibilityContext createContext(PricingUcaasRequestBean requestBean) {
		WebexPricingFeasibilityContext context = new WebexPricingFeasibilityContext();
		context.webexQuotePricingBean = new WebexQuotePricingBean();
		context.webexQuotePricingRequest = new WebexQuotePricingRequest();
		context.ucaasQuotes = requestBean.getUcaasQuotes();
		return context;
	}

	/**
	 * Method to get QuoteLe
	 *
	 * @param context
	 * @return
	 */
	private WebexPricingFeasibilityContext getQuoteLe(WebexPricingFeasibilityContext context, Integer quoteToLeId)
			throws TclCommonException {
		Optional<QuoteToLe> quoteLeOptional = quoteToLeRepository.findById(quoteToLeId);
		QuoteToLe quoteToLe = quoteLeOptional.isPresent() ? quoteLeOptional.get() : null;
		if (Objects.isNull(quoteToLe)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		context.quoteToLe = quoteToLe;
		return context;
	}

	/**
	 * persistRequestPricingDetails
	 *
	 * @param request
	 * @param type
	 * @param quote
	 * @throws TclCommonException
	 */
	private void persistRequestPricingDetails(WebexQuotePricingRequest request, String type, Quote quote)
			throws TclCommonException {
		PricingEngineResponse pricingDetail = new PricingEngineResponse();
		pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
		pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
		pricingDetail.setPricingType(type);
		pricingDetail.setRequestData(Utils.convertObjectToJson(request));
		pricingDetail.setSiteCode(quote.getQuoteCode());
		pricingDetailsRepository.save(pricingDetail);
	}

	/**
	 * persistPricingDetails
	 *
	 * @param response
	 * @param type
	 * @param quote
	 * @throws TclCommonException
	 */
	private void persistResponsePricingDetails(WebexPricingResponse response, String type, Quote quote)
			throws TclCommonException {
		PricingEngineResponse pricingDetail = new PricingEngineResponse();
		pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
		pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
		pricingDetail.setPricingType(type);
		pricingDetail.setResponseData(Utils.convertObjectToJson(response));
		pricingDetail.setSiteCode(quote.getQuoteCode());
		pricingDetailsRepository.save(pricingDetail);
	}

	/**
	 * Method to get pricing response
	 *
	 * @param context
	 * @return {@link WebexPricingFeasibilityContext}
	 */
	private WebexPricingFeasibilityContext getPricingResponse(WebexPricingFeasibilityContext context)
			throws TclCommonException {
		try {
			this.persistRequestPricingDetails(context.webexQuotePricingRequest, WebexConstants.QUOTE_UCAAS,
					context.quoteToLe.getQuote());
			context.webexQuotePricingRequest.setContractTerm(
					Integer.parseInt(context.quoteToLe.getTermInMonths().replaceAll("[^0-9]", CommonConstants.EMPTY)));
			WebexQuotePricingRequestWrapper requestBean = new WebexQuotePricingRequestWrapper();
			requestBean.setInputData(Collections.singletonList(context.webexQuotePricingRequest));
			String request = Utils.convertObjectToJson(requestBean);
			LOGGER.info("Pricing UCAAS input :: {}", request);
			RestResponse pricingResponse = restClientService.post(pricingUrl, request);
			LOGGER.info("Pricing Response :: {}", pricingResponse);
			LOGGER.info("Pricing UCAAS URL :: {}", pricingUrl);
			if (pricingResponse.getStatus() == Status.SUCCESS) {
				String response = pricingResponse.getData();
				LOGGER.info("Pricing UCAAS output :: {}", response);
				response = response.replaceAll("\"NA\"", "\"0\"");
				LOGGER.info("Pricing UCAAS output :: {}", response);
				WebexPricingResponseWrapper responseBean = Utils.convertJsonToObject(response, WebexPricingResponseWrapper.class);
				context.webexPricingResponse = responseBean.getResults().stream().findFirst().get();
				this.persistResponsePricingDetails(context.webexPricingResponse, WebexConstants.QUOTE_UCAAS,
						context.quoteToLe.getQuote());
			} else {
				throw new TclCommonException(ExceptionConstants.PRICING_FAILURE,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in getting pricing response");
			throw new TclCommonException(ExceptionConstants.PRICING_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return context;
	}

	/**
	 * Method to get Ucaas Quotes.
	 *
	 * @param context
	 * @return
	 */
	private WebexPricingFeasibilityContext getUcaasQuotes(WebexPricingFeasibilityContext context) {
		Set<String> skuNames = Sets.newHashSet(WebexConstants.A_FLEX_EACM, WebexConstants.A_FLEX_AUCM,
				WebexConstants.A_FLEX_NUCM);

		List<WebexUcaasQuotePriceBean> ucaasQuotes = context.ucaasQuotes.stream().map(quoteUcaas -> {
			WebexUcaasQuotePriceBean webexUcaasQuotePriceBean = WebexUcaasQuotePriceBean
					.toWebexUcaasQuotePriceBean(quoteUcaas);
			webexUcaasQuotePriceBean.setIsLicenseComponent(
					skuNames.stream().anyMatch(skuName -> quoteUcaas.getSkuId().contains(skuName)));
			webexUcaasQuotePriceBean.setIsEndpoint(quoteUcaas.getIsEndpoint());
			webexUcaasQuotePriceBean.setIsSkuID(quoteUcaas.getSkuId().contains(WebexConstants.TAAP));
			if (webexUcaasQuotePriceBean.getIsEndpoint()) {
				Optional<QuoteUcaas> endPointUcaasQuote = quoteUcaasRepository.findById(quoteUcaas.getId());
				if (endPointUcaasQuote.isPresent()) {
					QuoteUcaasDetail quoteUcaasDetail = quoteUcaasDetailsRepository.findByQuoteUcaasId(endPointUcaasQuote.get().getId()).get();
					List<PartyType> partyType = null;
					try {
						partyType = Utils
								.convertJsonToObject(quoteUcaasDetail.getResponse(), QuoteLineType.class)
								.getUserArea().getCiscoExtensions().getCiscoLine().getParty();
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					Optional<List<LocationType>> locationType = partyType.stream()
							.filter(partyType1 -> WebexConstants.INSTALL_ADDRESS.equals(partyType1.getCategory()))
							.map(PartyBaseType::getLocation).findAny();

					if (locationType.isPresent()) {
						Optional<List<AddressType>> addressTypes = locationType.get().stream()
								.map(LocationABIEType::getAddress).findAny();
						addressTypes.ifPresent(types -> types.stream().anyMatch(addressType -> {
							if (WebexConstants.INDIA_COUNTRY_CODE.equals(addressType.getCountryCode().getValue()))
								webexUcaasQuotePriceBean.setIsEndpointIntl(false);
							else
								webexUcaasQuotePriceBean.setIsEndpointIntl(true);
							return false;
						}));
					}
				}
			}
			return webexUcaasQuotePriceBean;
		}).collect(Collectors.toList());
		context.webexQuotePricingRequest.setUcaasQuotes(ucaasQuotes);
		context.webexQuotePricingBean.setUcaasQuotes(ucaasQuotes);
		return context;
	}

	/**
	 * Method to populate Ucaas price from pricing response.
	 *
	 * @param context
	 * @return
	 */
	private WebexPricingFeasibilityContext populateUcaasQuotesForPrice(WebexPricingFeasibilityContext context) {

		QuoteUcaas configUcaas = quoteUcaasRepository.findByQuoteToLeAndIsConfig(context.quoteToLe, (byte) 1).stream()
				.findFirst().get();

		// Populating Pricing Bean
		context.webexPricingResponse.getUcaasQuotes().forEach(pricingUcaasQuotesBean -> context.webexQuotePricingBean
				.getUcaasQuotes().stream().anyMatch(webexUcaasQuotePriceBean -> {
					if (webexUcaasQuotePriceBean.getId().equals(pricingUcaasQuotesBean.getId())) {
						webexUcaasQuotePriceBean.setUnitMrc(pricingUcaasQuotesBean.getUnitMrc().doubleValue());
						webexUcaasQuotePriceBean.setMrc(pricingUcaasQuotesBean.getMrc().doubleValue());
						webexUcaasQuotePriceBean.setNrc(pricingUcaasQuotesBean.getNrc().doubleValue());
						webexUcaasQuotePriceBean.setUnitNrc(pricingUcaasQuotesBean.getUnitNrc().doubleValue());
						webexUcaasQuotePriceBean.setTcv(pricingUcaasQuotesBean.getTcv().doubleValue());
						webexUcaasQuotePriceBean.setArc(pricingUcaasQuotesBean.getArc().doubleValue());
						QuoteUcaas quoteUcaas = quoteUcaasRepository.findById(webexUcaasQuotePriceBean.getId()).get();
						quoteUcaas.setUnitMrc(pricingUcaasQuotesBean.getUnitMrc().doubleValue());
						quoteUcaas.setMrc(pricingUcaasQuotesBean.getMrc().doubleValue());
						quoteUcaas.setUnitNrc(pricingUcaasQuotesBean.getUnitNrc().doubleValue());
						quoteUcaas.setNrc(pricingUcaasQuotesBean.getNrc().doubleValue());
						quoteUcaas.setTcv(pricingUcaasQuotesBean.getTcv().doubleValue());
						quoteUcaas.setArc(pricingUcaasQuotesBean.getArc().doubleValue());
						quoteUcaasRepository.save(quoteUcaas);
						return true;
					}
					return false;
				}));

		// Populating Overall Gross Value for License
		context.webexQuotePricingBean.setErrorFlag(context.webexPricingResponse.getErrorFlag().equals("1"));
		context.webexQuotePricingBean.setErrorMsg(context.webexPricingResponse.getErrorMessages());
		context.webexQuotePricingBean.setTotalTcv(context.webexPricingResponse.getTotalTcv());
		context.webexQuotePricingBean.setTotalMrc(context.webexPricingResponse.getTotalMrc());
		context.webexQuotePricingBean.setTotalNrc(context.webexPricingResponse.getTotalNrc());
		context.webexQuotePricingBean.setTotalArc(context.webexPricingResponse.getTotalArc());

		configUcaas.setMrc(context.webexPricingResponse.getTotalMrc().doubleValue());
		configUcaas.setTcv(context.webexPricingResponse.getTotalTcv().doubleValue());
		configUcaas.setNrc(context.webexPricingResponse.getTotalNrc().doubleValue());
		configUcaas.setArc(context.webexPricingResponse.getTotalArc().doubleValue());
		quoteUcaasRepository.save(configUcaas);
		return context;
	}

	/**
	 * Process pricing.
	 *
	 * @param requestBean
	 * @return
	 */
	public WebexQuotePricingBean processPricing(PricingUcaasRequestBean requestBean) throws TclCommonException {
		Objects.requireNonNull(requestBean.getQuoteToLeId(), QUOTE_LE_ID_NULL_MESSAGE);
		WebexPricingFeasibilityContext context = createContext(requestBean);
		getQuoteLe(context, requestBean.getQuoteToLeId());
		getUcaasQuotes(context);
		getPricingResponse(context);
		populateUcaasQuotesForPrice(context);
		return context.webexQuotePricingBean;
	}

	/**
	 * Get quoteToLe
	 *
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public QuoteToLe getQuoteToLe(Integer quoteToLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR);
		return quoteToLe.get();
	}

	/**
	 * Calculate new value
	 *
	 * @param change
	 * @param value
	 * @return
	 */
	private double calculateNewValue(double change, double value) {
		if (change < 0) {
			return (value + Math.abs(change));
		} else {
			return (value - Math.abs(change));
		}
	}

	/**
	 * Save new prices in quoteToLe
	 *
	 * @param changeInArc
	 * @param changeInMrc
	 * @param changeInNrc
	 * @param changeInTcv
	 * @param pricingBean
	 */
	private void saveQuoteToLe(double changeInArc, double changeInMrc, double changeInNrc, double changeInTcv,
			WebexLicenseManualPricingBean pricingBean) throws TclCommonException {

		QuoteToLe quoteToLe = getQuoteToLe(pricingBean.getQuoteLeId());

		double currentMrc = Objects.nonNull(quoteToLe.getFinalMrc()) ? quoteToLe.getFinalMrc() : 0.0;
		double currentNrc = Objects.nonNull(quoteToLe.getFinalNrc()) ? quoteToLe.getFinalNrc() : 0.0;
		double currentArc = Objects.nonNull(quoteToLe.getFinalArc()) ? quoteToLe.getFinalArc() : 0.0;
		double currentTcv = Objects.nonNull(quoteToLe.getTotalTcv()) ? quoteToLe.getTotalTcv() : 0.0;

		double newArc = calculateNewValue(changeInArc, currentArc);
		double newMrc = calculateNewValue(changeInMrc, currentMrc);
		double newNrc = calculateNewValue(changeInNrc, currentNrc);
		double newTcv = calculateNewValue(changeInTcv, currentTcv);

		quoteToLe.setProposedArc(BigDecimal.valueOf(newArc).doubleValue());
		quoteToLe.setProposedMrc(BigDecimal.valueOf(newMrc).doubleValue());
		quoteToLe.setProposedNrc(BigDecimal.valueOf(newNrc).doubleValue());
		quoteToLe.setFinalArc(BigDecimal.valueOf(newArc).doubleValue());
		quoteToLe.setFinalMrc(BigDecimal.valueOf(newMrc).doubleValue());
		quoteToLe.setFinalNrc(BigDecimal.valueOf(newNrc).doubleValue());
		quoteToLe.setTotalTcv(BigDecimal.valueOf(newTcv).doubleValue());
		quoteToLeRepository.save(quoteToLe);
	}

	/**
	 * Save changed price quote ucaas configuration
	 *
	 * @param priceUpdateBean
	 */
	private void saveQuoteUcaasAndQuoteToLePrices(WebexLicenseManualPricingBean priceUpdateBean)
			throws TclCommonException {

		QuoteUcaas ucaasConfiguration = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(
				priceUpdateBean.getQuoteLeId(), WebexConstants.CONFIGURATION, CommonConstants.BACTIVE);

		double currentMrc = Objects.nonNull(ucaasConfiguration.getMrc()) ? ucaasConfiguration.getMrc() : 0.0;
		double currentNrc = Objects.nonNull(ucaasConfiguration.getNrc()) ? ucaasConfiguration.getNrc() : 0.0;
		double currentArc = Objects.nonNull(ucaasConfiguration.getArc()) ? ucaasConfiguration.getArc() : 0.0;
		double currentTcv = Objects.nonNull(ucaasConfiguration.getTcv()) ? ucaasConfiguration.getTcv() : 0.0;

		// calculating change in mrc/nrc/arc/tcv for updating quoteToLe prices
		double changeInMrc = currentMrc - priceUpdateBean.getTotalMrc().doubleValue();
		double changeInNrc = currentNrc - priceUpdateBean.getTotalNrc().doubleValue();
		double changeInArc = currentArc - priceUpdateBean.getTotalArc().doubleValue();
		double changeInTcv = currentTcv - priceUpdateBean.getTotalTcv().doubleValue();

		// calculate new mrc/nrc/tcv values
		double newMrc = calculateNewValue(changeInMrc, currentMrc);
		double newNrc = calculateNewValue(changeInNrc, currentNrc);
		double newArc = calculateNewValue(changeInArc, currentArc);
		double newTcv = calculateNewValue(changeInTcv, currentTcv);

		ucaasConfiguration.setMrc(BigDecimal.valueOf(newMrc).doubleValue());
		ucaasConfiguration.setNrc(BigDecimal.valueOf(newNrc).doubleValue());
		ucaasConfiguration.setArc(BigDecimal.valueOf(newArc).doubleValue());
		ucaasConfiguration.setTcv(BigDecimal.valueOf(newTcv).doubleValue());
		quoteUcaasRepository.save(ucaasConfiguration);
		saveQuoteToLe(changeInArc, changeInMrc, changeInNrc, changeInTcv, priceUpdateBean);
	}

	/**
	 * Update unitMrc, unitNrc, mrc, nrc, tcv prices in UCaaS quotes
	 *
	 * @param priceUpdateBean
	 * @throws TclCommonException
	 */
	private void updateLicenseComponentPrices(WebexLicenseManualPricingBean priceUpdateBean) throws TclCommonException {
		List<QuoteUcaas> allQuotes = quoteUcaasRepository.findByQuoteToLeIdAndIsConfig(priceUpdateBean.getQuoteLeId(),
				(byte) 0);

		CumulativePricesBean cumulativePricesBean = new CumulativePricesBean();
		if (Objects.nonNull(allQuotes) && !allQuotes.isEmpty()) {
			// update/save mrc,nrc prices from bean
			allQuotes.forEach(quoteUcaas -> {
				priceUpdateBean.getQuoteUcaasBeans().stream().anyMatch(quoteUcaasBean -> {
					if (Objects.equals(quoteUcaas.getId(), quoteUcaasBean.getId())) {
						quoteUcaas.setUnitMrc(quoteUcaasBean.getUnitMrc());
						quoteUcaas.setUnitNrc(quoteUcaasBean.getUnitNrc());
						quoteUcaas.setMrc(quoteUcaasBean.getMrc());
						quoteUcaas.setNrc(quoteUcaasBean.getNrc());
						quoteUcaas.setArc(quoteUcaasBean.getArc());
						quoteUcaas.setTcv(quoteUcaasBean.getTcv());
						return true;
					} else
						return false;
				});
				// calculate total mrc/nrc/arc prices cumulatively
				cumulativePricesBean.setMrc(quoteUcaas.getMrc());
				cumulativePricesBean.setNrc(quoteUcaas.getNrc());
				cumulativePricesBean.setArc(quoteUcaas.getArc());
				cumulativePricesBean.setTcv(quoteUcaas.getTcv());
				cumulativePrices(cumulativePricesBean);
				quoteUcaasRepository.save(quoteUcaas);
			});
			// save calculated total price in bean for sending to UI
			priceUpdateBean.setTotalMrc(BigDecimal.valueOf(cumulativePricesBean.getTotalMrc()));
			priceUpdateBean.setTotalNrc(BigDecimal.valueOf(cumulativePricesBean.getTotalNrc()));
			priceUpdateBean.setTotalArc(BigDecimal.valueOf(cumulativePricesBean.getTotalArc()));
			priceUpdateBean.setTotalTcv(BigDecimal.valueOf(cumulativePricesBean.getTotalTcv()));
		} else
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY);
	}

	/**
	 * Calculating prices cumulatively
	 *
	 * @param cumulativePricesBean
	 */
	private void cumulativePrices(CumulativePricesBean cumulativePricesBean) {
		cumulativePricesBean.setTotalMrc(
				(Objects.nonNull(cumulativePricesBean.getTotalMrc()) ? cumulativePricesBean.getTotalMrc() : 0.0)
						+ cumulativePricesBean.getMrc());
		cumulativePricesBean.setTotalNrc(
				(Objects.nonNull(cumulativePricesBean.getTotalNrc()) ? cumulativePricesBean.getTotalNrc() : 0.0)
						+ cumulativePricesBean.getNrc());
		cumulativePricesBean.setTotalArc(
				(Objects.nonNull(cumulativePricesBean.getTotalArc()) ? cumulativePricesBean.getTotalArc() : 0.0)
						+ cumulativePricesBean.getArc());
		cumulativePricesBean.setTotalTcv(
				(Objects.nonNull(cumulativePricesBean.getTotalTcv()) ? cumulativePricesBean.getTotalTcv() : 0.0)
						+ cumulativePricesBean.getTcv());
	}

	/**
	 * Updating SKU ID prices if isSku is true
	 *
	 * @param priceUpdateBean
	 */
	private void updateSkuPrices(WebexLicenseManualPricingBean priceUpdateBean) {
		QuoteUcaas ucaasConfiguration = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(
				priceUpdateBean.getQuoteLeId(), WebexConstants.CONFIGURATION, CommonConstants.BACTIVE);

		priceUpdateBean.getQuoteUcaasBeans().forEach(quoteUcaasBean -> {
			Optional<QuoteUcaas> skuLineItem = quoteUcaasRepository.findById(quoteUcaasBean.getId());
			if (skuLineItem.isPresent()) {
				QuoteUcaas sku = skuLineItem.get();

				// calculate change in sku price
				double changeInMrc = sku.getMrc() - quoteUcaasBean.getMrc();
				double changeInNrc = sku.getNrc() - quoteUcaasBean.getNrc();
				double changeInArc = sku.getArc() - quoteUcaasBean.getArc();
				double changeInTcv = sku.getTcv() - quoteUcaasBean.getTcv();

				// update table with new values
				sku.setUnitMrc(quoteUcaasBean.getUnitMrc());
				sku.setUnitNrc(quoteUcaasBean.getUnitNrc());
				sku.setMrc(quoteUcaasBean.getMrc());
				sku.setNrc(quoteUcaasBean.getNrc());
				sku.setArc(quoteUcaasBean.getArc());
				sku.setTcv(quoteUcaasBean.getTcv());
				quoteUcaasRepository.save(sku);

				// calculate new total price after finding
				double newMrc = calculateNewValue(changeInMrc, ucaasConfiguration.getMrc());
				double newNrc = calculateNewValue(changeInNrc, ucaasConfiguration.getNrc());
				double newArc = calculateNewValue(changeInArc, ucaasConfiguration.getArc());
				double newTcv = calculateNewValue(changeInTcv, ucaasConfiguration.getTcv());

				// set total mrc/nrc/arc/tcv prices in bean for UI
				priceUpdateBean.setTotalMrc(BigDecimal.valueOf(newMrc));
				priceUpdateBean.setTotalArc(BigDecimal.valueOf(newArc));
				priceUpdateBean.setTotalNrc(BigDecimal.valueOf(newNrc));
				priceUpdateBean.setTotalTcv(BigDecimal.valueOf(newTcv));
			}
		});

	} 

	/**
	 * Process manual prices of license components
	 *
	 * @param priceUpdateBean
	 * @param isSku
	 * @return
	 */
	public WebexLicenseManualPricingBean processManualLicenseComponentPrices(
			WebexLicenseManualPricingBean priceUpdateBean, Boolean isSku) throws TclCommonException {
		Objects.requireNonNull(priceUpdateBean);
		if (Objects.nonNull(isSku) && isSku)
			updateSkuPrices(priceUpdateBean);
		else
			updateLicenseComponentPrices(priceUpdateBean);
		saveQuoteUcaasAndQuoteToLePrices(priceUpdateBean);
		return priceUpdateBean;
	}
}
