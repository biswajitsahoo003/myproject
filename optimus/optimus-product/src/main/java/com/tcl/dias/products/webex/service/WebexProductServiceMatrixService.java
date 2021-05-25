package com.tcl.dias.products.webex.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.OutboundPriceConversionBean;
import com.tcl.dias.common.webex.beans.WebexEndpointCatalogBean;
import com.tcl.dias.common.webex.beans.WebexPriceConversionBean;
import com.tcl.dias.productcatelog.entity.entities.WebexEndpointHsnCodeView;
import com.tcl.dias.productcatelog.entity.entities.WebexLnsAllCountryPricing;
import com.tcl.dias.productcatelog.entity.repository.WebexEndpointHsnCodeViewRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexItfsSharedPayPerUsePricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexLnsSharedPayPerUsePricingRepository;
import com.tcl.dias.products.gsc.beans.GscOutboundPricingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.webex.beans.SkuDetailsRequestBean;
import com.tcl.dias.common.webex.beans.SkuDetailsResponseBean;
import com.tcl.dias.common.webex.beans.WebexProductPricesBean;
import com.tcl.dias.common.webex.beans.WebexProductPricesRequest;
import com.tcl.dias.common.webex.beans.WebexProductPricesResponse;
import com.tcl.dias.productcatelog.entity.entities.WebexSkuDetails;
import com.tcl.dias.productcatelog.entity.repository.GscItfsDedicatedPriceViewRepository;
import com.tcl.dias.productcatelog.entity.repository.GscLnsDedicatedPriceViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCGlobalOutBndViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCITFSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCLNSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXALLProductsRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXBridgeCountryRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXGlobalOutboundRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXLNSRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexItfsDedicatedAllCountryPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexLnsAllCountryPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexLnsSharedPayPerSeatPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexOutboundAllCountryPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexOutboundSharedPayPerSeatPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexSkuDetailsRepository;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.gsc.service.v1.GscOutboundPricingService;
import com.tcl.dias.products.webex.beans.WebexPricingBean;
import com.tcl.dias.products.webex.beans.WebexProductCountriesBean;
import com.tcl.dias.products.webex.beans.WebexProductLocationBean;
import com.tcl.dias.products.webex.beans.WebexProductLocationDetailBean;
import com.tcl.dias.products.webex.constants.WebexProductServiceMatrixConstant;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service for Getting country List.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class WebexProductServiceMatrixService {

	@Autowired
	ServiceAreaMatrixWEBEXLNSRepository serviceAreaMatrixWeBEXLNSRepository;

	@Autowired
	ServiceAreaMatrixWEBEXGlobalOutboundRepository serviceAreaMatrixWEBEXGlobalOutboundRepository;

	@Autowired
	ServiceAreaMatrixWEBEXBridgeCountryRepository serviceAreaMatrixWEBEXBridgeCountryRepository;

	@Autowired
	ServiceAreaMatrixWEBEXALLProductsRepository serviceAreaMatrixWEBEXALLProductsRepository;

	@Autowired
	ServiceAreaMatrixGSCLNSViewRepository serviceAreaMatrixGSCLNSViewRepository;

	@Autowired
	ServiceAreaMatrixGSCITFSViewRepository serviceAreaMatrixGSCITFSViewRepository;

	@Autowired
	ServiceAreaMatrixGSCGlobalOutBndViewRepository serviceAreaMatrixGSCGlobalOutBndViewRepository;

	@Autowired
	GscOutboundPricingService gscOutboundPricingService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	GscLnsDedicatedPriceViewRepository gscLnsDedicatedPriceViewRepository;

	@Autowired
	GscItfsDedicatedPriceViewRepository gscItfsDedicatedPriceViewRepository;

	@Autowired
	WebexLnsSharedPayPerSeatPricingRepository webexLnsSharedPayPerSeatPricingRepository;

	@Autowired
	WebexOutboundSharedPayPerSeatPricingRepository webexOutboundSharedPayPerSeatPricingRepository;

	@Autowired
	WebexLnsAllCountryPricingRepository webexLnsAllCountryPricingRepository;

	@Autowired
	WebexOutboundAllCountryPricingRepository webexOutboundAllCountryPricingRepository;

	@Autowired
	WebexItfsDedicatedAllCountryPricingRepository webexItfsDedicatedAllCountryPricingRepository;

	@Autowired
	WebexSkuDetailsRepository webexSkuDetailsRepository;

	@Autowired
	WebexItfsSharedPayPerUsePricingRepository webexItfsSharedPayPerUsePricingRepository;

	@Autowired
	WebexLnsSharedPayPerUsePricingRepository webexLnsSharedPayPerUsePricingRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.webex.convert.price.currency}")
	String convertWebexPrices;

	@Value("${rabbitmq.outbound.convert.price.currency}")
	String convertOutboundPrices;

	@Autowired
	WebexEndpointHsnCodeViewRepository webexEndpointHsnCodeViewRepository;

	/**
	 * Get all countries
	 *
	 * @param productName
	 * @return {@link WebexProductLocationBean}
	 * @throws TclCommonException
	 */
	public WebexProductLocationBean getCountries(String type, final String productName, final String paymentModel,
			String bridgeRegion, String accessType, Boolean isBridgeCountryDialOut) throws TclCommonException {
		Objects.requireNonNull(productName, "Product name cannot be null");
		Objects.requireNonNull(bridgeRegion, "BridgeRegion cannot be null");
		WebexProductLocationBean webexProductLocationBean = new WebexProductLocationBean();
		Set<Map<String, Object>> sourceList;
		Set<Map<String, Object>> destinationList;
		try {
			if (WebexProductServiceMatrixConstant.PAYPERSEAT.equals(paymentModel)) {
				switch (productName) {
				case WebexProductServiceMatrixConstant.LNS:
					sourceList = serviceAreaMatrixWeBEXLNSRepository.findPackagedCountries();
					if (bridgeRegion.equals(WebexProductServiceMatrixConstant.BRIDGE_USA)) {
						destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName();
					} else {
						destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
					}
					webexProductLocationBean.setSources(createWebexCountryBean(sourceList));
					webexProductLocationBean.setDestinations(createWebexCountryBean(destinationList));
					break;
				case WebexProductServiceMatrixConstant.GLOBAL_OUTBOUND:
					if (bridgeRegion.equals(WebexProductServiceMatrixConstant.BRIDGE_USA)) {
						sourceList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName();
					} else {
						sourceList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
					}
					if (Objects.nonNull(isBridgeCountryDialOut) && isBridgeCountryDialOut) {
						destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
					} else {
						destinationList = serviceAreaMatrixWEBEXGlobalOutboundRepository.findPackagedCountries();
					}
					webexProductLocationBean.setSources(createWebexCountryBean(sourceList));
					webexProductLocationBean.setDestinations(createWebexCountryBean(destinationList));
					break;
				}
			} else if (WebexProductServiceMatrixConstant.PAYPERUSE.equals(paymentModel)) {
				if (WebexProductServiceMatrixConstant.SHARED.equals(type)) {
					if (WebexProductServiceMatrixConstant.LNS.equals(productName)
							|| WebexProductServiceMatrixConstant.ITFS.equals(productName)) {
						sourceList = serviceAreaMatrixWEBEXALLProductsRepository.findByDistinctCountry();
						if (bridgeRegion.equals(WebexProductServiceMatrixConstant.BRIDGE_USA)) {
							destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName();
						} else {
							destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
						}
						webexProductLocationBean.setSources(createWebexCountryBean(sourceList));
						webexProductLocationBean.setDestinations(createWebexCountryBean(destinationList));
					} else {
						if (bridgeRegion.equals(WebexProductServiceMatrixConstant.BRIDGE_USA)) {
							sourceList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName();
						} else {
							sourceList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
						}
						if (Objects.nonNull(isBridgeCountryDialOut) && isBridgeCountryDialOut) {
							destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
						} else {
							destinationList = serviceAreaMatrixWEBEXGlobalOutboundRepository.findPackagedCountries();
						}
						webexProductLocationBean.setSources(createWebexCountryBean(sourceList));
						webexProductLocationBean.setDestinations(createWebexCountryBean(destinationList));
					}
				} else if (WebexProductServiceMatrixConstant.DEDICATED.equals(type)) {
					switch (productName) {
					case WebexProductServiceMatrixConstant.LNS:
						sourceList = serviceAreaMatrixGSCLNSViewRepository
								.findDistinctByIso3CountryCodeAndAndIsoCountryName();
						if (bridgeRegion.equals(WebexProductServiceMatrixConstant.BRIDGE_USA)) {
							destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName();
						} else {
							destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
						}
						webexProductLocationBean.setSources(createWebexCountryBeanForGsc(sourceList));
						webexProductLocationBean.setDestinations(createWebexCountryBeanForGsc(destinationList));
						break;
					case WebexProductServiceMatrixConstant.ITFS:
						sourceList = serviceAreaMatrixGSCITFSViewRepository
								.findDistinctByIso3CountryCodeAndAndIsoCountryNameWhereIndicatorPresent();
						if (bridgeRegion.equals(WebexProductServiceMatrixConstant.BRIDGE_USA)) {
							destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName();
						} else {
							destinationList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
						}
						webexProductLocationBean.setSources(createWebexCountryBeanForGsc(sourceList));
						webexProductLocationBean.setDestinations(createWebexCountryBeanForGsc(destinationList));
						break;
					case WebexProductServiceMatrixConstant.GLOBAL_OUTBOUND:
						destinationList = serviceAreaMatrixGSCGlobalOutBndViewRepository
								.findDistinctByIso3CountryCodeAndAndIsoCountryName();
						if (bridgeRegion.equals(WebexProductServiceMatrixConstant.BRIDGE_USA)) {
							sourceList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName();
						} else {
							sourceList = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridgeRegion);
						}
						webexProductLocationBean.setSources(createWebexCountryBeanForGsc(sourceList));
						webexProductLocationBean.setDestinations(createWebexCountryBeanForGsc(destinationList));
						break;
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return webexProductLocationBean;
	}

	/**
	 * Get all countries
	 *
	 * @param productName
	 * @return {@link WebexProductCountriesBean}
	 * @throws TclCommonException
	 */
	public WebexProductCountriesBean getAllCountries(String productName) {
		Objects.requireNonNull(productName, "Product name cannot be null");
		WebexProductCountriesBean webexProductCountriesBean = new WebexProductCountriesBean();
		Set<Map<String, Object>> countries = serviceAreaMatrixWEBEXALLProductsRepository.findByDistinctCountry();
		webexProductCountriesBean.setCountries(createWebexCountryBean(countries));
		return webexProductCountriesBean;
	}

	/**
	 * create webexcountrybean
	 *
	 * @param serviceAreaMatrixWEBEXList
	 * @return {@link WebexProductLocationBean}
	 * @throws TclCommonException
	 */
	private Set<WebexProductLocationDetailBean> createWebexCountryBean(
			final Set<Map<String, Object>> serviceAreaMatrixWEBEXList) {
		return serviceAreaMatrixWEBEXList.stream().map(this::createWebexProductLocationDetailBean)
				.collect(Collectors.toSet());
	}

	/**
	 * create webexcountrybean
	 *
	 * @param serviceAreaMatrixWEBEXList
	 * @return {@link WebexProductLocationBean}
	 * @throws TclCommonException
	 */
	private Set<WebexProductLocationDetailBean> createWebexCountryBeanForGsc(
			final Set<Map<String, Object>> serviceAreaMatrixWEBEXList) {
		return serviceAreaMatrixWEBEXList.stream().map(this::createGscProductLocationDetailBean)
				.collect(Collectors.toSet());
	}

	private WebexProductLocationDetailBean createGscProductLocationDetailBean(
			final Map<String, Object> serviceAreaMatrixGSCView) {
		return new WebexProductLocationDetailBean(
				String.valueOf(serviceAreaMatrixGSCView.get(WebexProductServiceMatrixConstant.NAME)),
				String.valueOf(serviceAreaMatrixGSCView.get(WebexProductServiceMatrixConstant.CODE)),
				String.valueOf(serviceAreaMatrixGSCView.get(WebexProductServiceMatrixConstant.ISD_CODE)));
	}

	/**
	 * create createWebexProductLocationDetailBean
	 *
	 * @param serviceAreaMatrixWEBEXView
	 * @return {@link WebexProductLocationBean}
	 * @throws TclCommonException
	 */
	private WebexProductLocationDetailBean createWebexProductLocationDetailBean(
			final Map<String, Object> serviceAreaMatrixWEBEXView) {
		return new WebexProductLocationDetailBean(
				String.valueOf(serviceAreaMatrixWEBEXView.get(WebexProductServiceMatrixConstant.NAME)),
				String.valueOf(serviceAreaMatrixWEBEXView.get(WebexProductServiceMatrixConstant.CODE)),
				String.valueOf(serviceAreaMatrixWEBEXView.get(WebexProductServiceMatrixConstant.ISD_CODE)));
	}

	/**
	 * Method to download webex prices (LNS/ITFS/Global Outbound)
	 *
	 * @param response
	 * @param fileType
	 * @return ({ @ link HttpServletResponse }
	 * @throws IOException
	 * @throws DocumentException
	 * @throws TclCommonException
	 */
	public HttpServletResponse downloadAllPrices(HttpServletResponse response, String audioGreeting, String productName,
			String paymentModel, String fileType, Boolean isBridgeCountryDialOut, String bridge)
			throws IOException, DocumentException, TclCommonException {
		Objects.requireNonNull(audioGreeting, "Audio Greeting cannot be null");
		Objects.requireNonNull(productName, "Product Name cannot be null");
		Objects.requireNonNull(paymentModel, "Payment Model cannot be null");
		Objects.requireNonNull(fileType, "File type cannot be null");

		List<?> webexPrices = new ArrayList<>();
		String fileName = audioGreeting + "_" + productName + "_" + paymentModel + "_Prices";
		if (Objects.nonNull(isBridgeCountryDialOut) && isBridgeCountryDialOut)
			fileName = audioGreeting + "_" + productName + "_" + bridge + "_" + paymentModel + "_Prices";
		String template = "";
		switch (productName) {
		case WebexProductServiceMatrixConstant.LNS:
			webexPrices = getLnsPrices(audioGreeting, paymentModel);
			template = "lnsitfspricing_template";
			break;
		case WebexProductServiceMatrixConstant.GLOBAL_OUTBOUND:
			webexPrices = getOutboundPrices(audioGreeting, paymentModel, isBridgeCountryDialOut, bridge);
			template = "ucaasoutboundpricing_template";
			break;
		case WebexProductServiceMatrixConstant.ITFS:
			webexPrices = getItfsPrices(audioGreeting, paymentModel);
			template = "lnsitfspricing_template";
			break;
		}
		if (fileType.equals("pdf")) {
			generatePdf(response, webexPrices, productName, fileName, template);
		} else
			throw new TclCommonException("Unsupported file type");
		return response;
	}

	/**
	 * Get itfs prices
	 *
	 * @param audioGreeting
	 * @param paymentModel
	 * @return
	 */
	public List<WebexPricingBean> getItfsPrices(String audioGreeting, String paymentModel) throws TclCommonException {

		Set<Map<String, Object>> priceTable;
		List<WebexPricingBean> prices = new ArrayList<>();

		switch (paymentModel) {
		case WebexProductServiceMatrixConstant.PAYPERUSE:
			switch (audioGreeting) {
			case WebexProductServiceMatrixConstant.SHARED:
				priceTable = webexItfsSharedPayPerUsePricingRepository.findByOriginCountry();
				if (Objects.nonNull(priceTable))
					prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean).collect(Collectors.toList());
				else
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				break;
			case WebexProductServiceMatrixConstant.DEDICATED:
				priceTable = gscItfsDedicatedPriceViewRepository.findByOriginCountry();
				if (Objects.nonNull(priceTable))
					prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean).collect(Collectors.toList());
				else
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);

				break;
			}
			break;
		}
		return prices;
	}

	/**
	 * Get global outbound prices
	 *
	 * @param audioGreeting
	 * @param paymentModel
	 * @param bridge
	 * @param isBridgeCountryDialOut
	 * @return
	 */
	public List<?> getOutboundPrices(String audioGreeting, String paymentModel, Boolean isBridgeCountryDialOut,
			String bridge) throws TclCommonException {

		Set<Map<String, Object>> priceTable;
		List<?> prices = new ArrayList<>();
		switch (paymentModel) {
		case WebexProductServiceMatrixConstant.PAYPERSEAT:
			switch (audioGreeting) {
			case WebexProductServiceMatrixConstant.SHARED:
				if (Objects.nonNull(isBridgeCountryDialOut) && isBridgeCountryDialOut) {
					return getOutboundBridgeCountryDialOutPrices(bridge);
				} else {
					priceTable = webexOutboundSharedPayPerSeatPricingRepository.findByCountry();
					if (Objects.nonNull(priceTable))
						prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean)
								.collect(Collectors.toList());
					else
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_NOT_FOUND);

				}
				break;
			case WebexProductServiceMatrixConstant.DEDICATED:
				if (Objects.nonNull(isBridgeCountryDialOut) && isBridgeCountryDialOut) {
					prices = getOutboundBridgeCountryDialOutPrices(bridge);
				} else {
					priceTable = webexOutboundAllCountryPricingRepository.findByCountry();
					if (Objects.nonNull(priceTable))
						prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean)
								.collect(Collectors.toList());
					else
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_NOT_FOUND);

				}
				break;
			}
			break;
		case WebexProductServiceMatrixConstant.PAYPERUSE:
			switch (audioGreeting) {
			case WebexProductServiceMatrixConstant.SHARED:
				priceTable = webexOutboundAllCountryPricingRepository.findByCountry();
				if (Objects.nonNull(priceTable))
					prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean).collect(Collectors.toList());
				else
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);

				break;
			case WebexProductServiceMatrixConstant.DEDICATED:
				prices = gscOutboundPricingService.getOutboundPrices();
				break;
			}
			break;
		}
		return prices;
	}

	/**
	 * Get outbound prices if bridge country dial out is true
	 *
	 * @param bridge
	 * @return
	 */
	private List<?> getOutboundBridgeCountryDialOutPrices(String bridge) throws TclCommonException {
		Function<WebexPricingBean, Object> pricingFunction = Objects::nonNull;
		List<String> countries = serviceAreaMatrixWEBEXBridgeCountryRepository.findCountriesByRegion(bridge);

		pricingFunction = webexPricingBean -> {
			if (countries.contains(webexPricingBean.getCountry())){
				webexPricingBean.setHighRate(new BigDecimal(0.0));
			}
			return webexPricingBean;
		};
		
		Set<Map<String, Object>> bridgeCountryPrices = webexOutboundAllCountryPricingRepository.findByCountry();
		if (Objects.nonNull(bridgeCountryPrices))
			return bridgeCountryPrices.stream().map(WebexPricingBean::toWebexPricingBean).map(pricingFunction)
					.collect(Collectors.toList());
		else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
	}

	/**
	 * Get all lns prices
	 *
	 * @param audioGreeting
	 * @param paymentModel
	 * @return
	 */
	public List<WebexPricingBean> getLnsPrices(String audioGreeting, String paymentModel) throws TclCommonException {

		Set<Map<String, Object>> priceTable;
		List<WebexPricingBean> prices = new ArrayList<>();
		switch (paymentModel) {
		case WebexProductServiceMatrixConstant.PAYPERSEAT:
			switch (audioGreeting) {
			case WebexProductServiceMatrixConstant.SHARED:
				priceTable = webexLnsSharedPayPerSeatPricingRepository.findByCountry();
				if (Objects.nonNull(priceTable))
					prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean).collect(Collectors.toList());
				else
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				break;
			case WebexProductServiceMatrixConstant.DEDICATED:
				priceTable = webexLnsAllCountryPricingRepository.findByCountry();
				if (Objects.nonNull(priceTable))
					prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean).collect(Collectors.toList());
				else
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				break;
			}
			break;
		case WebexProductServiceMatrixConstant.PAYPERUSE:
			switch (audioGreeting) {
			case WebexProductServiceMatrixConstant.SHARED:
				priceTable = webexLnsSharedPayPerUsePricingRepository.findByCountry();
				if (Objects.nonNull(priceTable))
					prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean).collect(Collectors.toList());
				else
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				break;

			case WebexProductServiceMatrixConstant.DEDICATED:
				priceTable = gscLnsDedicatedPriceViewRepository.findByCountry();
				if (Objects.nonNull(priceTable))
					prices = priceTable.stream().map(WebexPricingBean::toWebexPricingBean).collect(Collectors.toList());
				else
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
				break;
			}
			break;
		}
		return prices;
	}

	/**
	 * Generate LNS/ITFS/Global outbound price list PDF
	 *
	 * @param response
	 * @param webexPricingBeans
	 * @param fileName
	 * @param template
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	private HttpServletResponse generatePdf(HttpServletResponse response, List<?> webexPricingBeans, String productName,
			String fileName, String template) throws DocumentException, IOException, TclCommonException {
		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		@SuppressWarnings("unchecked")
		Map<String, Object> pricingVariable = objectMapper
				.convertValue(ImmutableMap.of("webexPricingBeans", webexPricingBeans), Map.class);
		Context context = new Context();
		context.setVariables(pricingVariable);
		if (Objects.nonNull(productName))
			context.setVariable("productName", productName.toUpperCase());
		String html = templateEngine.process(template, context);
		PDFGenerator.createPdf(html, outByteStream);
		outArray = outByteStream.toByteArray();
		response.reset();
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf" + "\"");
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		outByteStream.flush();
		outByteStream.close();

		return response;
	}

	/**
	 * Generate LNS/ITFS/Global outbound price list.
	 *
	 * @param request
	 * @throws TclCommonException
	 */
	public WebexProductPricesResponse getProductPrices(WebexProductPricesRequest request) throws TclCommonException {
		WebexProductPricesResponse response = new WebexProductPricesResponse();
		List<WebexProductPricesBean> responseList = new ArrayList<>();
		response.setPricesRequest(request);

		// For LNS
		WebexProductPricesBean lnsPrices = new WebexProductPricesBean();
		lnsPrices.setProductName(WebexProductServiceMatrixConstant.LNS);
		lnsPrices.setPaymentModel(request.getPaymentModel());
		lnsPrices.setProductPrices(getLnsPrices(request.getAudioModel(), lnsPrices.getPaymentModel()));
		if (CommonConstants.INDIA_SITES.equalsIgnoreCase(request.getLeCountry())) {
			List<WebexPriceConversionBean> priceConversionBeans = new ArrayList<>();
			lnsPrices.getProductPrices().forEach(lnsPrice -> {
				WebexPricingBean prices = (WebexPricingBean) lnsPrice;
				priceConversionBeans.add(WebexPricingBean.toWebexPriceConversionBean(prices,
						request.getExistingCurrency(), request.getInputCurrency()));
			});

			String convertedResponse = (String) mqUtils.sendAndReceive(convertWebexPrices,
					Utils.convertObjectToJson(priceConversionBeans));
			List<WebexPriceConversionBean> convertedList = Utils.fromJson(convertedResponse,
					new TypeReference<List<WebexPriceConversionBean>>() {
					});
			lnsPrices.setProductPrices(convertedList.stream()
					.map(convertedPrice -> WebexPricingBean.fromWebexPriceConversionBean(convertedPrice))
					.collect(Collectors.toList()));
		}
		responseList.add(lnsPrices);

		// For Global Outbound
		WebexProductPricesBean outboundPrices = new WebexProductPricesBean();
		outboundPrices.setProductName(WebexProductServiceMatrixConstant.GLOBAL_OUTBOUND);
		if (Objects.nonNull(request.getAudioType())
				&& WebexProductServiceMatrixConstant.TOLL_DIAL_IN.equals(request.getAudioType())
				&& WebexProductServiceMatrixConstant.SHARED.equals(request.getAudioModel())) {
			outboundPrices.setPaymentModel(WebexProductServiceMatrixConstant.PAYPERUSE);
		} else {
			outboundPrices.setPaymentModel(request.getPaymentModel());
		}

		// For checking shared toll dial + bridge dial out.
		Boolean isBridgeCountryDialOut = WebexProductServiceMatrixConstant.SHARED.equals(request.getAudioModel())
				&& WebexProductServiceMatrixConstant.TOLL_DIAL_IN_BRIDGE_DIAL_OUT.equals(request.getAudioType());

		outboundPrices.setProductPrices(getOutboundPrices(request.getAudioModel(), outboundPrices.getPaymentModel(),
				isBridgeCountryDialOut, request.getBridge()));

		if (CommonConstants.INDIA_SITES.equalsIgnoreCase(request.getLeCountry())) {
			List<WebexPriceConversionBean> priceConversionBeans = new ArrayList<>();
			List<OutboundPriceConversionBean> outboundPriceConversion = new ArrayList<>();
			outboundPrices.getProductPrices().forEach(outboundPrice -> {
				if (outboundPrice instanceof WebexPricingBean) {
					WebexPricingBean prices = (WebexPricingBean) outboundPrice;
					priceConversionBeans.add(WebexPricingBean.toWebexPriceConversionBean(prices,
							request.getExistingCurrency(), request.getInputCurrency()));
				} else if (outboundPrice instanceof GscOutboundPricingBean) {
					GscOutboundPricingBean prices = (GscOutboundPricingBean) outboundPrice;
					outboundPriceConversion.add(GscOutboundPricingBean.toOutboundPriceConversionBean(prices,
							request.getExistingCurrency(), request.getInputCurrency()));
				}
			});
			if (!priceConversionBeans.isEmpty()) {
				String convertedResponse = (String) mqUtils.sendAndReceive(convertWebexPrices,
						Utils.convertObjectToJson(priceConversionBeans));
				List<WebexPriceConversionBean> convertedList = Utils.fromJson(convertedResponse,
						new TypeReference<List<WebexPriceConversionBean>>() {
						});
				outboundPrices.setProductPrices(convertedList.stream()
						.map(convertedPrice -> WebexPricingBean.fromWebexPriceConversionBean(convertedPrice))
						.collect(Collectors.toList()));
			} else if (!outboundPriceConversion.isEmpty()) {
				String convertedResponse = (String) mqUtils.sendAndReceive(convertOutboundPrices,
						Utils.convertObjectToJson(outboundPriceConversion));
				List<OutboundPriceConversionBean> convertedList = Utils.fromJson(convertedResponse,
						new TypeReference<List<OutboundPriceConversionBean>>() {
						});
				outboundPrices.setProductPrices(convertedList.stream()
						.map(convertedPrice -> GscOutboundPricingBean.fromOutboundPriceConversionBean(convertedPrice))
						.collect(Collectors.toList()));
			}
		}
		responseList.add(outboundPrices);

		// For ITFS
		WebexProductPricesBean itfsPrices = new WebexProductPricesBean();
		itfsPrices.setProductName(WebexProductServiceMatrixConstant.ITFS);
		itfsPrices.setPaymentModel(WebexProductServiceMatrixConstant.PAYPERUSE);
		itfsPrices.setProductPrices(getItfsPrices(request.getAudioModel(), itfsPrices.getPaymentModel()));
		if (CommonConstants.INDIA_SITES.equalsIgnoreCase(request.getLeCountry())) {
			List<WebexPriceConversionBean> priceConversionBeans = new ArrayList<>();
			itfsPrices.getProductPrices().forEach(lnsPrice -> {
				WebexPricingBean prices = (WebexPricingBean) lnsPrice;
				priceConversionBeans.add(WebexPricingBean.toWebexPriceConversionBean(prices,
						request.getExistingCurrency(), request.getInputCurrency()));
			});

			String convertedResponse = (String) mqUtils.sendAndReceive(convertWebexPrices,
					Utils.convertObjectToJson(priceConversionBeans));
			List<WebexPriceConversionBean> convertedList = Utils.fromJson(convertedResponse,
					new TypeReference<List<WebexPriceConversionBean>>() {
					});
			itfsPrices.setProductPrices(convertedList.stream()
					.map(convertedPrice -> WebexPricingBean.fromWebexPriceConversionBean(convertedPrice))
					.collect(Collectors.toList()));
		}
		responseList.add(itfsPrices);

		response.setProductPricesList(responseList);
		return response;
	}

	/**
	 * Method for getting skuID and Description.
	 *
	 * @param request
	 *
	 */
	public SkuDetailsResponseBean getSkuDetails(SkuDetailsRequestBean request) {
		SkuDetailsResponseBean responseBean = new SkuDetailsResponseBean();
		WebexSkuDetails skuDetails = webexSkuDetailsRepository
				.findSku(request.getLicenseType(), request.getAudioPlan(), request.getBridgeRegion()).stream()
				.findFirst().get();
		responseBean.setSkuName(skuDetails.getSku());
		responseBean.setDescription(skuDetails.getDescription());
		responseBean.setMrc(skuDetails.getListPriceMrc());
		return responseBean;
	}

	/**
	 * Method for getting bridge countries
	 *
	 * @param bridge
	 *
	 */
	public String getBridgeCountries(String bridge) {
		Set<Map<String, Object>> countries = serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(bridge);
		if (!countries.isEmpty()) {
			return (String) countries.stream().findFirst().get().get(WebexProductServiceMatrixConstant.NAME);
		}
		return null;
	}

	/**
	 * To check whether a LNS country is part of package
	 *
	 * @param request
	 * @return
	 */
	public String getIsPackagedCountry(String request) {
		WebexLnsAllCountryPricing webexLnsAllCountryPricing = webexLnsAllCountryPricingRepository.findByCountry(request);
		return String.valueOf(Objects.nonNull(webexLnsAllCountryPricing) && (webexLnsAllCountryPricing.getIsPackagedInd().equals(CommonConstants.Y)));
	}

	/**
	 * Return HSN Code for given endpoint
	 *
	 * @param request
	 * @return
	 */
	public Integer getHsnCodeForEndpoint(String request) {
		WebexEndpointHsnCodeView webexEndpointHsnCodeView = webexEndpointHsnCodeViewRepository.findBySku(request);
		return Objects.nonNull(webexEndpointHsnCodeView) ? webexEndpointHsnCodeView.getHsnCode() : null;
	}

	/**
	 * To convert WebexEndpointHsnCodeView to WebexEndpointCatalogBean
	 * 
	 * @param webexEndpointHsnCodeView
	 * @return
	 */
	WebexEndpointCatalogBean toWebexEndpointCatalogBean(WebexEndpointHsnCodeView webexEndpointHsnCodeView) {
		WebexEndpointCatalogBean webexEndpointCatalogBean = new WebexEndpointCatalogBean();
		webexEndpointCatalogBean.setSku(webexEndpointHsnCodeView.getSku());
		webexEndpointCatalogBean.setCpeMakeVendor(webexEndpointHsnCodeView.getCpeMakeVendor());
		webexEndpointCatalogBean.setEquipmentCategory(webexEndpointHsnCodeView.getEquipmentCategory());
		webexEndpointCatalogBean.setHsnCode(webexEndpointHsnCodeView.getHsnCode());
		webexEndpointCatalogBean.setTaxPercent(webexEndpointHsnCodeView.getTaxPercent());
		return webexEndpointCatalogBean;
	}

	/**
	 * To fetch endpoint list from database
	 */
	public List<WebexEndpointCatalogBean> getEndpointList() {
		return webexEndpointHsnCodeViewRepository.findAll().stream().map(view -> toWebexEndpointCatalogBean(view))
				.collect(Collectors.toList());
	}
}
