package com.tcl.dias.oms.teamsdr.service.v1;

import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ALL_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.COMMERCIAL_COMPONENTS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_AMC_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_BOM_RESPONSE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_DELIVERY_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_OUTRIGHT_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_RENTAL_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CUSTOM_PLAN;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MANAGEMENT_AND_MONITORING_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MEDIA_GATEWAY;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MEDIA_GATEWAY_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_LICENSE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.NON_RECURRING;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.OVERAGE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.RECURRING;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_LICENSE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_MEDIAGATEWAY_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.USAGE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRUtils.checkForNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.teamsdr.beans.CpeChargesBean;
import com.tcl.dias.common.teamsdr.beans.CpeSubchargesBean;
import com.tcl.dias.common.teamsdr.beans.MediaGatewayListPricesBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRCumulativePricesBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRServiceQuoteBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteDirectRouting;
import com.tcl.dias.oms.entity.entities.QuoteDirectRoutingCity;
import com.tcl.dias.oms.entity.entities.QuoteDirectRoutingMediaGateways;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTeamsDR;
import com.tcl.dias.oms.entity.entities.QuoteTeamsLicense;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.TeamsDRPricingEngine;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteDirectRoutingCityRepository;
import com.tcl.dias.oms.entity.repository.QuoteDirectRoutingMgRepository;
import com.tcl.dias.oms.entity.repository.QuoteDirectRoutingRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteTeamsDRRepository;
import com.tcl.dias.oms.entity.repository.QuoteTeamsLicenseRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.TeamsDRPricingEngineRepository;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.teamsdr.beans.CustomComponentPricesBean;
import com.tcl.dias.oms.teamsdr.beans.LicenseListPricesBean;
import com.tcl.dias.oms.teamsdr.beans.LicensePricesBean;
import com.tcl.dias.oms.teamsdr.beans.LicensePricesDetailBean;
import com.tcl.dias.oms.teamsdr.beans.ManagedServicesPricesBean;
import com.tcl.dias.oms.teamsdr.beans.ManagedServicesPricesRequestBean;
import com.tcl.dias.oms.teamsdr.beans.MediaGatewayPriceDetailsBean;
import com.tcl.dias.oms.teamsdr.beans.MediaGatewayPricesBean;
import com.tcl.dias.oms.teamsdr.beans.MgLocationWiseDetailBean;
import com.tcl.dias.oms.teamsdr.beans.MgSiteAddressBean;
import com.tcl.dias.oms.teamsdr.beans.QuoteTeamsDRManualPriceBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRManualPriceBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiQuoteLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRPriceInputDatum;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRPriceResultsDatum;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRPricingBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRServiceDetailPricesBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRServicesBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRUnitPriceBean;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.dias.oms.teamsdr.util.TeamsDRUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;


/**
 * Service for pricing and feasibility related
 *
 * @author Srinivasa Raghavan
 */
@Service
public class TeamsDRPricingFeasibilityService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TeamsDRPricingFeasibilityService.class);

	@Autowired
	QuoteTeamsDRRepository quoteTeamsDRRepository;

	@Autowired
	RestClientService restClientService;

	@Value("${pricing.request.teamsdr.url}")
	String pricingUrl;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteDirectRoutingRepository quoteDirectRoutingRepository;

	@Autowired
	QuoteDirectRoutingCityRepository quoteDirectRoutingCityRepository;

	@Autowired
	QuoteDirectRoutingMgRepository quoteDirectRoutingMgRepository;

	@Autowired
	QuoteTeamsLicenseRepository quoteTeamsLicenseRepository;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Value("${rabbitmq.teamsdr.service.level.charges}")
	String fetchServiceLevelCharges;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Value("${teamsdr.mg.get.vendor.name}")
	String getTeamsDrVendorName;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	TeamsDRPdfService teamsDRPdfService;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	TeamsDRPricingEngineRepository teamsDRPricingEngineRepository;


	/**
	 * Get prices for Plan/Add-Ons selected by the user
	 *
	 * @param teamsDRServiceQuoteBean
	 * @return
	 * @throws TclCommonException
	 */
	public TeamsDRServiceQuoteBean getPricesForPlanAndAddOn(TeamsDRServiceQuoteBean teamsDRServiceQuoteBean)
			throws TclCommonException {

		// construct request payload for pricing URL
//		List<TeamsDRPricingBean> teamsDRPricingRequestBean = constructPricingRequest(teamsDRServiceQuoteBean);
//		String request = Utils.convertObjectToJson(teamsDRPricingRequestBean);
//		LOGGER.info("Request payload for pricing API : {}", request);

		// will be enabled once price integration sprint has started
//		RestResponse restResponse = restClientService.post(pricingUrl, request);
//		if(restResponse.getStatus() == Status.SUCCESS)
//		{
//			String response  = restResponse.getData();
//			TeamsDRPricingResponseBean pricingResponseBean = Utils.convertJsonToObject(response, TeamsDRPricingResponseBean.class);
//			mapPrices(pricingResponseBean, teamsDRServiceQuoteBean);
//		}

		// setting mock prices until pricing integration is completed
		setMockPrices(teamsDRServiceQuoteBean);

		return teamsDRServiceQuoteBean;
	}

	/**
	 * Set mock prices for response
	 *
	 * @param teamsDRServiceQuoteBean
	 */
	private void setMockPrices(TeamsDRServiceQuoteBean teamsDRServiceQuoteBean) {
		teamsDRServiceQuoteBean.getTeamsDRServicesQuoteOfferings().forEach(teamsDRServicesQuoteOffering -> {
			teamsDRServicesQuoteOffering.setMrc(0.0);
			teamsDRServicesQuoteOffering.setNrc(0.0);
			teamsDRServicesQuoteOffering.setArc(0.0);
			teamsDRServicesQuoteOffering.setTcv(0.0);
		});
	}

	/**
	 * Check if the service is an Add-On
	 *
	 * @param plan
	 * @return
	 */
	private String checkIfAddOn(String plan) {
		if (TeamsDRConstants.ADDON_SERVICES.equals(plan))
			return "1";
		else
			return "0";
	}

	/**
	 * Check if an service is a Plan
	 *
	 * @param offeringName
	 * @param plan
	 * @return
	 */
	private String checkIfBundle(String offeringName, String plan) {
		if (Objects.nonNull(offeringName) && offeringName.equals(plan))
			return "1";
		else
			return "0";
	}

	/**
	 * To construct teams DR services price request
	 *
	 * @param teamsDRMultiQuoteLeBean
	 * @return
	 */
	public TeamsDRPricingBean constructTeamsDRPricingRequest(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean)
			throws TclCommonException {
		TeamsDRPricingBean teamsDRPricingBean = new TeamsDRPricingBean();
		TeamsDRPriceInputDatum teamsDRPriceInputDatum = new TeamsDRPriceInputDatum();
		final String plan = checkForPlan(teamsDRMultiQuoteLeBean);
		constructManagedServicesPriceRequest(plan, teamsDRMultiQuoteLeBean, teamsDRPriceInputDatum);
		constructLicensePriceRequest(teamsDRMultiQuoteLeBean, teamsDRPriceInputDatum);
		constructMediaGatewayPricesRequest(teamsDRMultiQuoteLeBean, teamsDRPriceInputDatum);
		teamsDRPricingBean.setInputDatum(new ArrayList<>());
		teamsDRPricingBean.getInputDatum().add(teamsDRPriceInputDatum);
		LOGGER.info("TeamsDR Pricing Request bean : {}", Utils.convertObjectToJson(teamsDRPricingBean));
		return teamsDRPricingBean;
	}

	/**
	 * To construct teams DR services price request
	 *
	 * @param teamsDRMultiQuoteLeBean
	 * @return
	 */
	public TeamsDRPricingBean constructPricingRequestPlanAndMg(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean)
			throws TclCommonException {
		TeamsDRPricingBean teamsDRPricingBean = new TeamsDRPricingBean();
		TeamsDRPriceInputDatum teamsDRPriceInputDatum = new TeamsDRPriceInputDatum();
		final String plan = checkForPlan(teamsDRMultiQuoteLeBean);
		constructManagedServicesPriceRequest(plan, teamsDRMultiQuoteLeBean, teamsDRPriceInputDatum);
		// setting an empty license prices object, to prevent pricing engine error of
		// null object
		teamsDRPriceInputDatum.setLicense(new LicensePricesBean());
		constructMediaGatewayPricesRequest(teamsDRMultiQuoteLeBean, teamsDRPriceInputDatum);
		teamsDRPricingBean.setInputDatum(new ArrayList<>());
		teamsDRPricingBean.getInputDatum().add(teamsDRPriceInputDatum);
		LOGGER.info("TeamsDR Pricing Request bean : {}", Utils.convertObjectToJson(teamsDRPricingBean));
		return teamsDRPricingBean;
	}

	/**
	 * To construct teams DR services price request
	 *
	 * @param teamsDRMultiQuoteLeBean
	 * @return
	 */
	public TeamsDRPricingBean constructPricingRequestLicense(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean)
			throws TclCommonException {
		TeamsDRPricingBean teamsDRPricingBean = new TeamsDRPricingBean();
		TeamsDRPriceInputDatum teamsDRPriceInputDatum = new TeamsDRPriceInputDatum();
		final String plan = checkForPlan(teamsDRMultiQuoteLeBean);
		constructLicensePriceRequest(teamsDRMultiQuoteLeBean, teamsDRPriceInputDatum);
		// setting an empty prices object, to prevent pricing engine error of null
		// object
		teamsDRPriceInputDatum.setManagedServices(new ManagedServicesPricesRequestBean());
		teamsDRPriceInputDatum.setMediaGateway(new MediaGatewayPricesBean());

		teamsDRPricingBean.setInputDatum(new ArrayList<>());
		teamsDRPricingBean.getInputDatum().add(teamsDRPriceInputDatum);
		LOGGER.info("TeamsDR Pricing Request bean : {}", Utils.convertObjectToJson(teamsDRPricingBean));
		return teamsDRPricingBean;
	}

	/**
	 * Construct request mg
	 *
	 * @param teamsDRMultiQuoteLeBean
	 * @param teamsDRPriceInputDatum
	 * @throws TclCommonException
	 */
	private void constructMediaGatewayPricesRequest(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean,
			TeamsDRPriceInputDatum teamsDRPriceInputDatum) throws TclCommonException {
		MediaGatewayPricesBean mediaGatewayPricesBean = new MediaGatewayPricesBean();
		teamsDRPriceInputDatum.setMediaGateway(mediaGatewayPricesBean);
		Optional<TeamsDRServicesBean> mediaGatewayService = teamsDRMultiQuoteLeBean.getTeamsDRSolution()
				.getTeamsDRServices().stream()
				.filter(services -> TeamsDRConstants.MEDIA_GATEWAY.equalsIgnoreCase(services.getOfferingName()))
				.findAny();
		if (mediaGatewayService.isPresent()) {
			boolean mgPresent = false;
			if (Objects.nonNull(mediaGatewayService.get().getMgConfigurations()))
				mgPresent = mediaGatewayService.get().getMgConfigurations().stream()
						.flatMap(mgConfig -> mgConfig.getCities().stream()).filter(Objects::nonNull)
						.anyMatch(mgCity -> Objects.nonNull(mgCity.getMediaGateway())
								&& !mgCity.getMediaGateway().isEmpty());
			if (mgPresent) {
				mediaGatewayPricesBean.setContractTerm(
						TeamsDRUtils.extractContractPeriodFromString(teamsDRMultiQuoteLeBean.getContractPeriod()));
				mediaGatewayPricesBean.setMediaGatewayPriceDetails(new ArrayList<>());
				mediaGatewayService.get().getMgConfigurations().forEach(mgConfig -> {
					mgConfig.getCities().forEach(city -> {
						MediaGatewayPriceDetailsBean mediaGatewayPriceDetailsBean = new MediaGatewayPriceDetailsBean();
						mediaGatewayPricesBean.getMediaGatewayPriceDetails().add(mediaGatewayPriceDetailsBean);

						mediaGatewayPriceDetailsBean.setCountry(mgConfig.getCountry());
						mediaGatewayPriceDetailsBean.setId(mgConfig.getId());
						mediaGatewayPriceDetailsBean.setMediaGatewayType(city.getMediaGatewayType());
						mediaGatewayPriceDetailsBean.setUom(teamsDRMultiQuoteLeBean.getCurrency());
						if (Objects.nonNull(city.getComponents())) {
							Optional<String> vendorName = city.getComponents().stream()
									.filter(component -> MEDIA_GATEWAY_ATTRIBUTES.equalsIgnoreCase(component.getName()))
									.flatMap(component -> component.getAttributes().stream())
									.filter(attribute -> TeamsDRConstants.VENDOR_NAME
											.equalsIgnoreCase(attribute.getName()))
									.map(attribute -> attribute.getAttributeValues()).findAny();
							vendorName
									.ifPresent(vendor -> mediaGatewayPriceDetailsBean.setMediaGatewayProvider(vendor));
						}
						MgLocationWiseDetailBean mgLocationWiseDetailBean = new MgLocationWiseDetailBean();
						mgLocationWiseDetailBean.setMediaGatewayType(city.getMediaGatewayType());
						mediaGatewayPriceDetailsBean.setLocationWiseDetails(new ArrayList<>());
						mediaGatewayPriceDetailsBean.getLocationWiseDetails().add(mgLocationWiseDetailBean);
						MgSiteAddressBean mgSiteAddressBean = new MgSiteAddressBean();
						city.getComponents().stream()
								.filter(component -> TeamsDRConstants.MEDIA_GATEWAY_ATTRIBUTES
										.equalsIgnoreCase(component.getName()))
								.findAny().ifPresent(component -> component.getAttributes().forEach(attribute -> {
									if (TeamsDRConstants.SIP
											.equalsIgnoreCase(mediaGatewayPriceDetailsBean.getMediaGatewayType())) {
										if (TeamsDRConstants.NUMBER_OF_SESSIONS.equalsIgnoreCase(attribute.getName())) {
											mediaGatewayPriceDetailsBean.setNoOfPri(TeamsDRConstants.NOT_APPLICABLE);
											mediaGatewayPriceDetailsBean.setNoOfSession(attribute.getAttributeValues());
										}
									} else if (TeamsDRConstants.TDM
											.equalsIgnoreCase(mediaGatewayPriceDetailsBean.getMediaGatewayType())) {
										if (TeamsDRConstants.NUMBER_OF_PRI.equalsIgnoreCase(attribute.getName())) {
											mediaGatewayPriceDetailsBean
													.setNoOfSession(TeamsDRConstants.NOT_APPLICABLE);
											mediaGatewayPriceDetailsBean.setNoOfPri(attribute.getAttributeValues());
										}
									}
									if (TeamsDRConstants.REDUNDANT_GATEWAY_ATTRIBUTE_NAME
											.equalsIgnoreCase(attribute.getName()))
										mediaGatewayPriceDetailsBean.setRedundancy(attribute.getAttributeValues());
									if (TeamsDRConstants.MEDIA_GATEWAY_PURCHASE_TYPE
											.equalsIgnoreCase(attribute.getName()))
										mediaGatewayPriceDetailsBean.setPurchaseType(attribute.getAttributeValues());
									if (TeamsDRConstants.AMC_SUPPORT_PLAN_ATTRIBUTE_NAME
											.equalsIgnoreCase(attribute.getName()))
										mediaGatewayPriceDetailsBean.setAmc(attribute.getAttributeValues());
									if (TeamsDRConstants.AHR_ATTRIBUTE_NAME.equalsIgnoreCase(attribute.getName()))
										mediaGatewayPriceDetailsBean.setAhr(attribute.getAttributeValues());
									mgSiteAddressBean.setCityId(city.getId());
									mgSiteAddressBean.setCity(city.getCity());
									if (TeamsDRConstants.SITE_ADDRESS_1.equalsIgnoreCase(attribute.getName()))
										mgSiteAddressBean.setSiteAddress1(attribute.getAttributeValues());
									if (TeamsDRConstants.SITE_ADDRESS_2.equalsIgnoreCase(attribute.getName()))
										mgSiteAddressBean.setSiteAddress2(attribute.getAttributeValues());
									if (TeamsDRConstants.SITE_ADDRESS_3.equalsIgnoreCase(attribute.getName()))
										mgSiteAddressBean.setSiteAddress3(attribute.getAttributeValues());
									if (TeamsDRConstants.STATE.equalsIgnoreCase(attribute.getName()))
										mgSiteAddressBean.setState(attribute.getAttributeValues());
									if (TeamsDRConstants.PINCODE.equalsIgnoreCase(attribute.getName()))
										mgSiteAddressBean.setPinCode(attribute.getAttributeValues());
								}));
						mgLocationWiseDetailBean.setMgSiteAddress(mgSiteAddressBean);
						mgLocationWiseDetailBean.setMediaGatewayListPrices(new ArrayList<>());
						city.getMediaGateway().forEach(mg -> {
							MediaGatewayListPricesBean mediaGatewayListPricesBean = new MediaGatewayListPricesBean();
							mediaGatewayListPricesBean.setMgId(mg.getId());
							mediaGatewayListPricesBean.setMediaGatewayName(mg.getName());
							mediaGatewayListPricesBean.setQty(mg.getQuantity());
							mgLocationWiseDetailBean.getMediaGatewayListPrices().add(mediaGatewayListPricesBean);
						});
					});
				});
				LOGGER.info("Media gateway pricing request : {}", Utils.convertObjectToJson(mediaGatewayPricesBean));
			}
		}
	}

	/**
	 * To construct MS license price teamsDRPriceInputDatum
	 *
	 * @param teamsDRMultiQuoteLeBean
	 * @param teamsDRPriceInputDatum
	 */
	private void constructLicensePriceRequest(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean,
			TeamsDRPriceInputDatum teamsDRPriceInputDatum) {
		LicensePricesBean licensePricesBean = new LicensePricesBean();
		teamsDRPriceInputDatum.setLicense(licensePricesBean);
		if (Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution())
				&& Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices())) {
			Optional<TeamsDRServicesBean> licenseService = teamsDRMultiQuoteLeBean.getTeamsDRSolution()
					.getTeamsDRServices().stream()
					.filter(services -> TeamsDRConstants.MICROSOFT_LICENSE.equalsIgnoreCase(services.getOfferingName()))
					.findAny();
			if (licenseService.isPresent() && Objects.nonNull(licenseService.get().getLicenseComponents())) {
				licensePricesBean.setContractTerm(
						TeamsDRUtils.extractContractPeriodFromString(teamsDRMultiQuoteLeBean.getContractPeriod()));
				licensePricesBean.setUOM(teamsDRMultiQuoteLeBean.getCurrency());
				licensePricesBean.setLicenseDetail(new ArrayList<>());
				licenseService.ifPresent(license -> {
					LicensePricesDetailBean licensePricesDetailBean = new LicensePricesDetailBean();
					if (Objects.nonNull(license.getLicenseComponents())) {
						licensePricesDetailBean.setAgreementType(license.getLicenseComponents().getAgreementType());
						licensePricesDetailBean.setListOfLicense(new ArrayList<>());
						license.getLicenseComponents().getLicenseConfigurations().stream()
								.peek(licenseConfig -> licensePricesDetailBean.setProvider(licenseConfig.getProvider()))
								.flatMap(licenseConfig -> licenseConfig.getLicenseDetails().stream())
								.forEach(licenseDetail -> {
									LicenseListPricesBean licenseListPricesBean = new LicenseListPricesBean();
									licenseListPricesBean.setOfferDisplayName(licenseDetail.getLicenseName());
									licenseListPricesBean.setNoOfLicense(licenseDetail.getNoOfLicenses());
									licensePricesDetailBean.getListOfLicense().add(licenseListPricesBean);
								});
						licensePricesBean.getLicenseDetail().add(licensePricesDetailBean);
					}
				});
				LOGGER.info("License prices teamsDRPriceInputDatum bean : {}", licensePricesBean.toString());
			}
		}
	}

	/**
	 * To construct managed services of Teams DR price request
	 *
	 * @param plan
	 * @param teamsDRMultiQuoteLeBean
	 * @param teamsDRPriceInputDatum
	 */
	private void constructManagedServicesPriceRequest(String plan, TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean,
			TeamsDRPriceInputDatum teamsDRPriceInputDatum) {
		ManagedServicesPricesRequestBean request = new ManagedServicesPricesRequestBean();
		teamsDRPriceInputDatum.setManagedServices(request);
		if (!StringUtils.isBlank(plan)) {
			teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream().anyMatch(services -> {
				if (services.getOfferingName().contains(TeamsDRConstants.PLAN)) {
					request.setCompVariant(services.getOfferingName());
					Optional<String> paymentType = services.getComponents().stream()
							.flatMap(component -> component.getAttributes().stream())
							.filter(attribute -> TeamsDRConstants.SELECT_YOUR_PAYMENT_MODEL
									.equalsIgnoreCase(attribute.getName()))
							.map(QuoteProductComponentsAttributeValueBean::getAttributeValues).findAny();
					paymentType.ifPresent(request::setPlan);
					request.setNoOfUsers(services.getNoOfUsers());
					request.setContractTerm(
							TeamsDRUtils.extractContractPeriodFromString(teamsDRMultiQuoteLeBean.getContractPeriod()));
					request.setUOM(teamsDRMultiQuoteLeBean.getCurrency());
					return true;
				} else
					return false;
			});
			request.setCustomComponents(new ArrayList<>());
			if (TeamsDRConstants.CUSTOM_PLAN.equalsIgnoreCase(plan))
				teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
						.filter(services -> TeamsDRConstants.CUSTOM_PLAN.equalsIgnoreCase(services.getPlan())
								&& !TeamsDRConstants.CUSTOM_PLAN.equalsIgnoreCase(services.getOfferingName()))
						.forEach(customServices -> {
							request.getCustomComponents().add(customServices.getOfferingName());
						});

			LOGGER.info("Managed Services pricing request : {}", request.toString());
		}
	}

	/**
	 * To check for presence of Plan line item in solutions
	 *
	 * @param teamsDRMultiQuoteLeBean
	 * @return
	 */
	private String checkForPlan(TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean) {
		Optional<String> plan = Optional.of(new String());
		if (Objects.nonNull(teamsDRMultiQuoteLeBean) && Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution()))
			plan = teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream().filter(Objects::nonNull)
					.filter(services -> Objects.nonNull(services.getPlan())).map(TeamsDRServicesBean::getPlan)
					.findAny();
		return plan.orElse("");
	}

	/**
	 * Get teamsDR Pricing response
	 *
	 *
	 * @param quoteCode
	 * @param quoteLeCode
	 * @param priceRequest
	 * @param currency
	 * @return
	 * @throws TclCommonException
	 */
	public TeamsDRPricingBean getTeamsDRPricingResponse(String quoteCode, String quoteLeCode, TeamsDRPricingBean priceRequest,
			String currency) throws TclCommonException {
		LOGGER.info("Inside getTeamsDRPricingResponse :");
		String request = Utils.convertObjectToJson(priceRequest);
		RestResponse response = restClientService.postWithUtf8Support(pricingUrl, request);
		if (response.getStatus() == Status.SUCCESS) {
			String removedNA = response.getData().replaceAll("NA", "0");
			TeamsDRPricingBean priceResponseBean = Utils.convertJsonToObject(removedNA, TeamsDRPricingBean.class);
			// To save pricing response for mg.
			savePriceResponse(quoteCode, quoteLeCode, priceRequest, priceResponseBean);
			if (!currency.equalsIgnoreCase(CommonConstants.USD))
				convertTeamsDRPrices(priceResponseBean, currency);
			saveMgPriceResponse(priceResponseBean);
			return priceResponseBean;
		} else
			throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION);
	}

	/**
	 * Save price response
	 *
	 * @param quoteCode
	 * @param quoteLeCode
	 * @param priceRequest
	 * @param priceResponseBean
	 * @throws TclCommonException
	 */
	private void savePriceResponse(String quoteCode, String quoteLeCode, TeamsDRPricingBean priceRequest,
			TeamsDRPricingBean priceResponseBean) throws TclCommonException {
		TeamsDRPricingEngine teamsdrPricingEngineResponse = new TeamsDRPricingEngine();
		teamsdrPricingEngineResponse.setSiteCode(quoteCode + CommonConstants.HYPHEN + quoteLeCode);
		teamsdrPricingEngineResponse.setPriceMode(FPConstants.SYSTEM.toString());
		teamsdrPricingEngineResponse.setPricingType(findPricingType(priceResponseBean));
		teamsdrPricingEngineResponse.setRequestData(Utils.convertObjectToJson(priceRequest));
		teamsdrPricingEngineResponse.setResponseData(Utils.convertObjectToJson(priceResponseBean));
		teamsdrPricingEngineResponse.setDateTime(new Timestamp(new Date().getTime()));
		teamsDRPricingEngineRepository.save(teamsdrPricingEngineResponse);
	}

	/**
	 * Find pricing type
	 *
	 * @param priceResponse
	 * @return
	 */
	private String findPricingType(TeamsDRPricingBean priceResponse) {
		String pricingType = "";
		if (Objects.nonNull(priceResponse) && Objects.nonNull(priceResponse.getResults())) {
			List<TeamsDRPriceResultsDatum> results = priceResponse.getResults();
			if(!results.isEmpty()){
				//pricing type to be decided based on the number of services in the price request/response
				//appending multiple charge types if present and separating them using comma(,)
				for (TeamsDRPriceResultsDatum result : results) {
					if(Objects.nonNull(result.getManagedServices()) && Objects.nonNull(result.getManagedServices().getServiceDetail())){
						pricingType = pricingType.concat(TeamsDRConstants.TEAMSDR_CONFIG_ATTRIBUTES);
					}
					if (Objects.nonNull(result.getMediaGateway()) && Objects.nonNull(result.getMediaGateway().getMediaGatewayPriceDetails())) {
						pricingType = (StringUtils.isNotEmpty(pricingType)) ? pricingType.concat(CommonConstants.COMMA): pricingType;
						pricingType = pricingType.concat(TeamsDRConstants.TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);
					}
					if (Objects.nonNull(result.getLicense()) && Objects.nonNull(result.getLicense().getLicenseDetail())) {
						pricingType = (StringUtils.isNotEmpty(pricingType)) ? pricingType.concat(CommonConstants.COMMA): pricingType;
						pricingType = pricingType.concat(TeamsDRConstants.TEAMSDR_LICENSE_CHARGES);
					}
				}
			}
		}
		LOGGER.info("PricingType : {} ", pricingType);
		return pricingType;
	}

	/**
	 * Convert teamsDR prices
	 *
	 * @param priceResponseBean
	 * @param currentCurrency
	 */
	private void convertTeamsDRPrices(TeamsDRPricingBean priceResponseBean, String currentCurrency) {
		LOGGER.info("Converting prices from {} to {}", CommonConstants.USD, currentCurrency);
		priceResponseBean.getResults().forEach(result -> {
			if (Objects.nonNull(result.getManagedServices()) && Objects
					.nonNull(result.getManagedServices().getServiceDetail()))
				convertManagedServicesPrices(CommonConstants.USD, currentCurrency, result.getManagedServices());

			if (Objects.nonNull(result.getMediaGateway()) && Objects
					.nonNull(result.getMediaGateway().getMediaGatewayPriceDetails()) && !result.getMediaGateway()
					.getMediaGatewayPriceDetails().isEmpty())
				convertMediagatewayPrices(CommonConstants.USD, currentCurrency, result.getMediaGateway());

			if (Objects.nonNull(result.getLicense()) && Objects.nonNull(result.getLicense().getLicenseDetail()))
				convertLicensePrices(CommonConstants.USD, currentCurrency, result.getLicense());
		});
	}

	/**
	 * Convert license prices
	 *
	 * @param existingCurrency
	 * @param currentCurrency
	 * @param license
	 */
	private void convertLicensePrices(String existingCurrency, String currentCurrency, LicensePricesBean license) {

		license.setManagementChargeNrc(omsUtilService
				.convertCurrencyWithPrecision(existingCurrency, currentCurrency, license.getManagementChargeNrc(), 2));
		license.setManagementChargeUnitNrc(omsUtilService
				.convertCurrencyWithPrecision(existingCurrency, currentCurrency, license.getManagementChargeUnitNrc(),
						2));
		license.setTotalTcv(omsUtilService
				.convertCurrencyWithPrecision(existingCurrency, currentCurrency, license.getTotalTcv(), 2));
		license.setTotalNrc(omsUtilService
				.convertCurrencyWithPrecision(existingCurrency, currentCurrency, license.getTotalNrc(), 2));
		license.setTotalMrc(omsUtilService
				.convertCurrencyWithPrecision(existingCurrency, currentCurrency, license.getTotalMrc(), 2));
		license.setTotalArc(omsUtilService
				.convertCurrencyWithPrecision(existingCurrency, currentCurrency, license.getTotalArc(), 2));

		convertLicenseListPrice(existingCurrency, currentCurrency, license.getLicenseDetail());
	}

	/**
	 * Convert license items prices
	 *
	 * @param existingCurrency
	 * @param currentCurrency
	 * @param licenseDetail
	 */
	private void convertLicenseListPrice(String existingCurrency, String currentCurrency,
			List<LicensePricesDetailBean> licenseDetail) {
		licenseDetail.forEach(licensePriceDetail -> {
			licensePriceDetail.getListOfLicense().forEach(licensePrice -> {
				licensePrice.setAfterDiscountArc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getAfterDiscountArc(), 2));
				licensePrice.setAfterDiscountNrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getAfterDiscountNrc(), 2));
				licensePrice.setAfterDiscountMrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getAfterDiscountMrc(), 2));
				licensePrice.setAfterDiscountTcv(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getAfterDiscountTcv(), 2));

				licensePrice.setLicenseCostArc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getLicenseCostArc(), 2));
				licensePrice.setLicenseCostMrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getLicenseCostMrc(), 2));
				licensePrice.setLicenseCostNrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getLicenseCostNrc(), 2));
				licensePrice.setLicenseCostTcv(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getLicenseCostTcv(), 2));

				licensePrice.setUnitArcSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getUnitArcSp(), 2));
				licensePrice.setUnitMrcSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getUnitMrcSp(), 2));
				licensePrice.setUnitNrcSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getUnitNrcSp(), 2));
				licensePrice.setUnitTcvSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getUnitTcvSp(), 2));

				licensePrice.setMrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getMrc(), 2));
				licensePrice.setNrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getNrc(), 2));
				licensePrice.setArc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getArc(), 2));
				licensePrice.setTcv(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, licensePrice.getTcv(), 2));
			});
		});
	}

	/**
	 * Convert media gateway prices
	 *
	 * @param existingCurrency
	 * @param currentCurrency
	 * @param mediaGateway
	 */
	private void convertMediagatewayPrices(String existingCurrency, String currentCurrency,
			MediaGatewayPricesBean mediaGateway) {
		mediaGateway.setTotalTcv(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGateway.getTotalTcv(), 2));
		mediaGateway.setTotalNrc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGateway.getTotalNrc(), 2));
		mediaGateway.setTotalMrc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGateway.getTotalMrc(), 2));
		mediaGateway.setTotalArc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGateway.getTotalArc(), 2));
		mediaGateway.getMediaGatewayPriceDetails().forEach(mediaGatewayPriceDetails -> {
			mediaGatewayPriceDetails.setCountryWiseArc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGatewayPriceDetails.getCountryWiseArc(), 2));
			mediaGatewayPriceDetails.setCountryWiseMrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGatewayPriceDetails.getCountryWiseMrc(), 2));
			mediaGatewayPriceDetails.setCountryWiseNrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGatewayPriceDetails.getCountryWiseNrc(), 2));
			mediaGatewayPriceDetails.setCountryWiseTcv(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mediaGatewayPriceDetails.getCountryWiseTcv(), 2));
			if (Objects.nonNull(mediaGatewayPriceDetails.getLocationWiseDetails())) {
				mediaGatewayPriceDetails.getLocationWiseDetails().forEach(mgLocationWiseDetail -> {
					mgLocationWiseDetail.setCityWiseArc(omsUtilService
							.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgLocationWiseDetail.getCityWiseArc(), 2));
					mgLocationWiseDetail.setCityWiseMrc(omsUtilService
							.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgLocationWiseDetail.getCityWiseMrc(), 2));
					mgLocationWiseDetail.setCityWiseNrc(omsUtilService
							.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgLocationWiseDetail.getCityWiseNrc(), 2));
					mgLocationWiseDetail.setCityWiseTcv(omsUtilService
							.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgLocationWiseDetail.getCityWiseTcv(), 2));

					if (Objects.nonNull(mgLocationWiseDetail.getMediaGatewayListPrices())) {
						mgLocationWiseDetail.getMediaGatewayListPrices().forEach(mgListPrice -> {
							mgListPrice.setArc(omsUtilService
									.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgListPrice.getArc(), 2));
							mgListPrice.setMrc(omsUtilService
									.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgListPrice.getMrc(), 2));
							mgListPrice.setNrc(omsUtilService
									.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgListPrice.getNrc(), 2));
							mgListPrice.setTcv(omsUtilService
									.convertCurrencyWithPrecision(existingCurrency, currentCurrency, mgListPrice.getTcv(), 2));
							mgListPrice.setNrcWithDelivery(omsUtilService
									.convertCurrencyWithPrecision(existingCurrency, currentCurrency,
											mgListPrice.getNrcWithDelivery(), 2));
							mgListPrice.setUnitNrcWithDelivery(omsUtilService
									.convertCurrencyWithPrecision(existingCurrency, currentCurrency,
											mgListPrice.getUnitNrcWithDelivery(), 2));
							//convert AMC charges
							convertCpeCharges(existingCurrency, currentCurrency, mgListPrice.getCpeAmcCharges());

							//convert Management charges
							convertCpeCharges(existingCurrency, currentCurrency, mgListPrice.getCpeManagement());

							//convert outright/rental charges
							convertCpeCharges(existingCurrency, currentCurrency,
									mgListPrice.getCpeOutrightRentalCharges());
						});
					}
				});
			}
		});
	}

	/**
	 * Convert cpe charges
	 *
	 * @param existingCurrency
	 * @param currentCurrency
	 * @param cpeCharges
	 */
	private void convertCpeCharges(String existingCurrency, String currentCurrency, CpeChargesBean cpeCharges) {
		if (Objects.nonNull(cpeCharges)) {
			cpeCharges.setUnitNrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeCharges.getUnitNrc(), 2));
			cpeCharges.setUnitMrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeCharges.getUnitMrc(), 2));
			cpeCharges.setMrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeCharges.getMrc(), 2));
			cpeCharges.setNrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeCharges.getNrc(), 2));
			cpeCharges.setArc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeCharges.getArc(), 2));
			cpeCharges.setTcv(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeCharges.getTcv(), 2));
			convertSubChargePrices(existingCurrency, currentCurrency, cpeCharges.getCpeSubcharges());
		}
	}

	/**
	 * Convert subcharges prices
	 *
	 * @param existingCurrency
	 * @param currentCurrency
	 * @param cpeSubcharges
	 */
	private void convertSubChargePrices(String existingCurrency, String currentCurrency,
			List<CpeSubchargesBean> cpeSubcharges) {
		if (Objects.nonNull(cpeSubcharges)) {
			cpeSubcharges.forEach(cpeSubcharge -> {
				cpeSubcharge.setArc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getArc(), 2));
				cpeSubcharge.setMrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getMrc(), 2));
				cpeSubcharge.setNrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getNrc(), 2));
				cpeSubcharge.setTcv(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getTcv(), 2));
				cpeSubcharge.setAfterDiscountArc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getAfterDiscountArc(), 2));
				cpeSubcharge.setAfterDiscountMrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getAfterDiscountMrc(), 2));
				cpeSubcharge.setAfterDiscountNrc(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getAfterDiscountNrc(), 2));
				cpeSubcharge.setAfterDiscountTcv(omsUtilService
						.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getAfterDiscountTcv(), 2));
				cpeSubcharge.setCostArc(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getCostArc(), 2));
				cpeSubcharge.setCostMrc(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getCostMrc(), 2));
				cpeSubcharge.setCostNrc(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getCostNrc(), 2));
				cpeSubcharge.setCostTcv(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getCostTcv(), 2));
				cpeSubcharge.setUnitArcSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getUnitArcSp(), 2));
				cpeSubcharge.setUnitMrcSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getUnitMrcSp(), 2));
				cpeSubcharge.setUnitNrcSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getUnitNrcSp(), 2));
				cpeSubcharge.setUnitTcvSp(
						omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, cpeSubcharge.getUnitTcvSp(), 2));
			});
		}
	}

	/**
	 * Convert Managed Services prices
	 *
	 * @param existingCurrency
	 * @param currentCurrency
	 * @param managedServices
	 */
	private void convertManagedServicesPrices(String existingCurrency, String currentCurrency,
			ManagedServicesPricesBean managedServices) {
		managedServices.setUnitMrc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getUnitMrc(), 2));
		managedServices.setUnitNrc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getUnitNrc(), 2));
		managedServices.setTotalTcv(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getTotalTcv(), 2));
		managedServices.setTotalNrc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getTotalNrc(), 2));
		managedServices.setTotalMrc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getTotalMrc(), 2));
		managedServices.setTotalArc(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getTotalArc(), 2));
		managedServices.setUsage(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getUsage(), 2));
		managedServices.setOverage(
				omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, managedServices.getOverage(), 2));

		managedServices.getServiceDetail().forEach(service -> {
			service.setUsage(omsUtilService.convertCurrencyWithPrecision(CommonConstants.USD, currentCurrency, service.getUsage(), 2));
		});
		managedServices.getCustomComponents().forEach(customComponents -> {
			customComponents.setUnitMrc(
					omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, customComponents.getUnitMrc(), 2));
			customComponents.setUnitNrc(
					omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, customComponents.getUnitNrc(), 2));
			customComponents.setMrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, customComponents.getMrc(), 2));
			customComponents.setNrc(omsUtilService
					.convertCurrencyWithPrecision(existingCurrency, currentCurrency, customComponents.getNrc(), 2));
			customComponents.setOverage(
					omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, customComponents.getOverage(), 2));
			customComponents.setUsage(
					omsUtilService.convertCurrencyWithPrecision(existingCurrency, currentCurrency, customComponents.getUsage(), 2));
		});
	}

	/**
	 * Method to save teamsdr mg pricing response.
	 *
	 * @param teamsDRPricingBean
	 */
	private void saveMgPriceResponse(TeamsDRPricingBean teamsDRPricingBean) {
		if (Objects.nonNull(teamsDRPricingBean) && Objects.nonNull(teamsDRPricingBean.getResults())
				&& Objects.nonNull(teamsDRPricingBean.getResults().get(0).getMediaGateway())) {
			MstProductFamily mstProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS, CommonConstants.BACTIVE);
			teamsDRPricingBean.getResults().forEach(result -> {
				if (Objects.nonNull(result) && Objects.nonNull(result.getMediaGateway())
						&& Objects.nonNull(result.getMediaGateway().getMediaGatewayPriceDetails())) {
					result.getMediaGateway().getMediaGatewayPriceDetails().forEach(mediaGatewayPriceDetailsBean -> {
						mediaGatewayPriceDetailsBean.getLocationWiseDetails().forEach(mgLocationWiseDetailBean -> {
							mgLocationWiseDetailBean.getMediaGatewayListPrices()
									.forEach(mediaGatewayListPricesBean -> quoteDirectRoutingMgRepository
											.findById(mediaGatewayListPricesBean.getMgId()).ifPresent(mediaGateway -> {
												try {
													updateBomResponse(mstProductFamily, mediaGateway.getId(),
															Utils.convertObjectToJson(mediaGatewayListPricesBean),
															TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);
												} catch (TclCommonException e) {
													e.printStackTrace();
												}
											}));
						});
					});
				}
			});
		}
	}

	/**
	 * Method to update bom response..
	 *
	 * @param mstProductFamily
	 * @param referenceId
	 * @param response
	 */
	private void updateBomResponse(MstProductFamily mstProductFamily, Integer referenceId, String response,
			String referenceName) {
		AtomicReference<Boolean> isPriceAttributePresent = new AtomicReference<>(false);
		quoteProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, mstProductFamily, referenceName)
				.forEach(quoteProductComponent -> {
					quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(quoteProductComponent)
							.forEach(attrValue -> {
								String attrName = attrValue.getProductAttributeMaster().getName();
								if (CPE_BOM_RESPONSE.equals(attrName)) {
									isPriceAttributePresent.set(true);
								}
							});
					if (!isPriceAttributePresent.get()) {
						QuoteProductComponentsAttributeValue newAttributeValue = new QuoteProductComponentsAttributeValue();
						newAttributeValue.setQuoteProductComponent(quoteProductComponent);
						ProductAttributeMaster productAttributeMaster = teamsDRQuoteService
								.getProductAttributes(CPE_BOM_RESPONSE);
						newAttributeValue.setProductAttributeMaster(productAttributeMaster);
						newAttributeValue.setIsAdditionalParam("Y");
						newAttributeValue = quoteProductComponentsAttributeValueRepository.save(newAttributeValue);
						newAttributeValue.setAttributeValues(String
								.valueOf(teamsDRPdfService.constructAdditionalServiceParams(newAttributeValue.getId(),
										TEAMSDR, null, CPE_BOM_RESPONSE, response).getId()));
					}
				});
	}

	/**
	 * @param noOfUsers
	 * @param rate
	 * @return
	 */
	private Double checkAndReturnUsageRate(Integer noOfUsers, Double rate) {
		if (noOfUsers > 500)
			return rate * (noOfUsers - 500);
		else
			return rate;
	}

	/**
	 * Save price attributes of plan
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param referenceId
	 * @param referenceName
	 * @param response
	 * @throws TclCommonException
	 */
	private void savePriceAttributesOfPlan(Integer quoteId, MstProductFamily teamsPF, Integer referenceId,
			String referenceName, TeamsDRPriceResultsDatum response) throws TclCommonException {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, teamsPF, referenceName);
		List<QuoteProductComponentBean> componentBeans = new ArrayList<>();
		if (!quoteProductComponents.isEmpty())
			quoteProductComponents.forEach(quoteProductComponent -> {
				QuoteProductComponentBean componentBean = new QuoteProductComponentBean();
				componentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				List<QuoteProductComponentsAttributeValueBean> attributeDetailList = new ArrayList<>();
				List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent(quoteProductComponent);
				attributeValues.forEach(quoteProductComponentsAttributeValue -> {
					QuoteProductComponentsAttributeValueBean attributeDetail = new QuoteProductComponentsAttributeValueBean();
					attributeDetail.setName(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName());
					attributeDetail.setAttributeId(quoteProductComponentsAttributeValue.getId());
					attributeDetail.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
					if (ALL_CHARGES.contains(attributeDetail.getName())) {
						Optional<QuotePrice> quotePrice = quotePriceRepository
								.findByReferenceNameAndReferenceId(ATTRIBUTES,
										attributeDetail.getAttributeId().toString())
								.stream().findAny();
						QuotePrice price = new QuotePrice();
						if (NON_RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
							if (quotePrice.isPresent()) {
								price = quotePrice.get();
								price.setMinimumNrc(response.getManagedServices().getTotalNrc());
								price.setEffectiveNrc(response.getManagedServices().getTotalNrc());
							} else {
								price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null,
										response.getManagedServices().getTotalNrc(),
										response.getManagedServices().getTotalNrc(), null, null, null, null, null, null,
										null, null);
							}
							attributeDetail.setPrice(new QuotePriceBean(price));
						} else if (RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
							if (quotePrice.isPresent()) {
								price = quotePrice.get();
								price.setMinimumMrc(response.getManagedServices().getUnitMrc());
								price.setEffectiveMrc(response.getManagedServices().getTotalMrc());
							} else {
								price = createQuotePrice(quoteId, teamsPF, attributeDetail,
										response.getManagedServices().getUnitMrc(),
										response.getManagedServices().getTotalMrc(), null, null, null, null, null, null,
										null, null, null, null);
							}
							attributeDetail.setPrice(new QuotePriceBean(price));
						} else if (OVERAGE.equalsIgnoreCase(attributeDetail.getName())) {
							if (quotePrice.isPresent()) {
								price = quotePrice.get();
								price.setEffectiveUsagePrice(response.getManagedServices().getOverage());
							} else {
								price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null, null, null,
										response.getManagedServices().getOverage(), null, null, null, null, null, null,
										null);
							}
							attributeDetail.setPrice(new QuotePriceBean(price));
						} else if (USAGE.equalsIgnoreCase(attributeDetail.getName())) {
							if (quotePrice.isPresent()) {
								price = quotePrice.get();
								price.setEffectiveUsagePrice(response.getManagedServices().getUsage());
							} else {
								price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null, null, null,
										response.getManagedServices().getUsage(), null, null, null, null, null, null,
										null);
							}
							attributeDetail.setPrice(new QuotePriceBean(price));
						}
					}
					attributeDetailList.add(attributeDetail);
				});
				componentBean.setAttributes(attributeDetailList);
				componentBeans.add(componentBean);
			});
	}

	/**
	 * Save price attributes of custom
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param referenceId
	 * @param referenceName
	 * @param response
	 * @throws TclCommonException
	 */
	void savePriceAttributesOfCustom(Integer quoteId, MstProductFamily teamsPF, Integer referenceId,
			String referenceName, CustomComponentPricesBean response) throws TclCommonException {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, teamsPF, referenceName);
		List<QuoteProductComponentBean> componentBeans = new ArrayList<>();
		quoteProductComponents.forEach(quoteProductComponent -> {
			QuoteProductComponentBean componentBean = new QuoteProductComponentBean();
			componentBean.setName(quoteProductComponent.getMstProductComponent().getName());
			List<QuoteProductComponentsAttributeValueBean> attributeDetailList = new ArrayList<>();
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
			attributeValues.forEach(quoteProductComponentsAttributeValue -> {
				QuoteProductComponentsAttributeValueBean attributeDetail = new QuoteProductComponentsAttributeValueBean();
				attributeDetail.setName(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName());
				attributeDetail.setAttributeId(quoteProductComponentsAttributeValue.getId());
				attributeDetail.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
				if (ALL_CHARGES.contains(attributeDetail.getName())) {
					Optional<QuotePrice> quotePrice = quotePriceRepository
							.findByReferenceNameAndReferenceId(ATTRIBUTES, attributeDetail.getAttributeId().toString())
							.stream().findAny();
					QuotePrice price = new QuotePrice();
					//setting total nrc for minimum nrc as quantity for non recurring is 1
					if (NON_RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumNrc(response.getNrc());
							price.setEffectiveNrc(response.getNrc());
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null,
									response.getNrc(), response.getNrc(), null, null, null, null, null, null, null,
									null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
					} else if (RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumMrc(response.getUnitMrc());
							price.setEffectiveMrc(response.getMrc());
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, response.getUnitMrc(),
									response.getMrc(), null, null, null, null, null, null, null, null, null, null);
						}
					} else if (OVERAGE.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setEffectiveUsagePrice(response.getOverage());
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null, null, null,
									response.getOverage(), null, null, null, null, null, null, null);
						}
					} else if (USAGE.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setEffectiveUsagePrice(response.getUsage());
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null, null, null,
									response.getUsage(), null, null, null, null, null, null, null);
						}
					}
					attributeDetail.setPrice(new QuotePriceBean(price));
				}
				attributeDetailList.add(attributeDetail);
			});
			componentBean.setAttributes(attributeDetailList);
			componentBeans.add(componentBean);
		});
	}

	/**
	 * Create quote price
	 *
	 * @param quoteId
	 * @param mstProductFamily
	 * @param attributeDetail
	 * @param minimumMrc
	 * @param totalMrc
	 * @param minimumNrc
	 * @param totalNrc
	 * @param totalUsage
	 * @param discount
	 * @return
	 */
	private QuotePrice createQuotePrice(Integer quoteId, MstProductFamily mstProductFamily,
			QuoteProductComponentsAttributeValueBean attributeDetail, Double minimumMrc, Double totalMrc,
			Double minimumNrc, Double totalNrc, Double totalUsage, Double discount, Double costMrc, Double costNrc,
			Double costArc, Double computedMrc, Double computedNrc, Double computedArc) {
		QuotePrice quotePrice = new QuotePrice();
		quotePrice.setQuoteId(quoteId);
		quotePrice.setMstProductFamily(mstProductFamily);
		quotePrice.setReferenceId(String.valueOf(attributeDetail.getAttributeId()));
		quotePrice.setReferenceName(ATTRIBUTES);
		quotePrice.setMinimumMrc(minimumMrc);
		quotePrice.setMinimumNrc(minimumNrc);
		quotePrice.setEffectiveMrc(totalMrc);
		quotePrice.setEffectiveNrc(totalNrc);
		quotePrice.setEffectiveUsagePrice(totalUsage);
		quotePrice.setDiscountInPercent(discount);
		quotePrice.setCatalogMrc(costMrc);
		quotePrice.setCatalogNrc(costNrc);
		quotePrice.setCatalogArc(costArc);
		quotePrice.setComputedMrc(computedMrc);
		quotePrice.setComputedNrc(computedNrc);
		quotePrice.setComputedArc(computedArc);
		return quotePriceRepository.save(quotePrice);
	}

	/**
	 * Save price attributes of license
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param referenceId
	 * @param referenceName
	 * @param response
	 * @throws TclCommonException
	 */
	private void savePriceAttributesOfLicense(Integer quoteId, MstProductFamily teamsPF, Integer referenceId,
			String referenceName, LicenseListPricesBean response) throws TclCommonException {
		MstProductFamily mstProductFamily = teamsDRQuoteService.getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, mstProductFamily, referenceName);
		List<QuoteProductComponentBean> componentBeans = new ArrayList<>();
		quoteProductComponents.forEach(quoteProductComponent -> {
			QuoteProductComponentBean componentBean = new QuoteProductComponentBean();
			componentBean.setName(quoteProductComponent.getMstProductComponent().getName());
			List<QuoteProductComponentsAttributeValueBean> attributeDetailList = new ArrayList<>();
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
			attributeValues.forEach(quoteProductComponentsAttributeValue -> {
				QuoteProductComponentsAttributeValueBean attributeDetail = new QuoteProductComponentsAttributeValueBean();
				attributeDetail.setName(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName());
				attributeDetail.setAttributeId(quoteProductComponentsAttributeValue.getId());
				attributeDetail.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
				if (ALL_CHARGES.contains(attributeDetail.getName())) {
					Optional<QuotePrice> quotePrice = quotePriceRepository
							.findByReferenceNameAndReferenceId(ATTRIBUTES, attributeDetail.getAttributeId().toString())
							.stream().findAny();
					QuotePrice price = new QuotePrice();
					if (NON_RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumNrc(response.getUnitNrcSp());
							price.setEffectiveNrc(response.getNrc());
							price.setDiscountInPercent(response.getDiscountToTata());
							price.setComputedNrc(response.getAfterDiscountNrc());
							price.setCatalogNrc(response.getLicenseCostNrc());
							quotePriceRepository.save(price);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null,
									response.getUnitNrcSp(), response.getNrc(), null, response.getDiscountToTata(),
									null, response.getLicenseCostNrc(), null, null,
									response.getAfterDiscountNrc(), null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
					} else if (RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumMrc(response.getUnitMrcSp());
							price.setEffectiveMrc(response.getMrc());
							price.setDiscountInPercent(response.getDiscountToTata());
							price.setComputedMrc(response.getAfterDiscountMrc());
							price.setComputedArc(response.getAfterDiscountArc());
							price.setCatalogMrc(response.getLicenseCostMrc());
							price.setCatalogArc(response.getLicenseCostArc());
							quotePriceRepository.save(price);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, response.getUnitMrcSp(),
									response.getMrc(), null, null, null, response.getDiscountToTata(),
									response.getLicenseCostMrc(), null, response.getLicenseCostArc(),
									response.getAfterDiscountMrc(), null, response.getAfterDiscountArc());
						}
					}
					attributeDetail.setPrice(new QuotePriceBean(price));
				}
				attributeDetailList.add(attributeDetail);
			});
			componentBean.setAttributes(attributeDetailList);
			componentBeans.add(componentBean);
		});
	}

	/**
	 * Save attributes of management
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param referenceId
	 * @param referenceName
	 * @param response
	 * @throws TclCommonException
	 */
	void savePriceAttributesOfLicense(Integer quoteId, MstProductFamily teamsPF, Integer referenceId,
			String referenceName, LicensePricesBean response) throws TclCommonException {
		MstProductFamily mstProductFamily = teamsDRQuoteService.getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, mstProductFamily, referenceName);
		List<QuoteProductComponentBean> componentBeans = new ArrayList<>();
		quoteProductComponents.forEach(quoteProductComponent -> {
			QuoteProductComponentBean componentBean = new QuoteProductComponentBean();
			componentBean.setName(quoteProductComponent.getMstProductComponent().getName());
			List<QuoteProductComponentsAttributeValueBean> attributeDetailList = new ArrayList<>();
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
			attributeValues.forEach(quoteProductComponentsAttributeValue -> {
				QuoteProductComponentsAttributeValueBean attributeDetail = new QuoteProductComponentsAttributeValueBean();
				attributeDetail.setName(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName());
				attributeDetail.setAttributeId(quoteProductComponentsAttributeValue.getId());
				attributeDetail.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
				if (ALL_CHARGES.contains(attributeDetail.getName())) {
					Optional<QuotePrice> quotePrice = quotePriceRepository
							.findByReferenceNameAndReferenceId(ATTRIBUTES, attributeDetail.getAttributeId().toString())
							.stream().findAny();
					QuotePrice price = new QuotePrice();
					if (NON_RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumNrc(response.getManagementChargeUnitNrc());
							price.setEffectiveNrc(response.getManagementChargeNrc());
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null,
									response.getManagementChargeUnitNrc(), response.getManagementChargeNrc(), null,
									null, null, null, null, null, null, null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
					} else if (RECURRING.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumMrc(0.0);
							price.setEffectiveMrc(0.0);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, 0.0, 0.0, null, null, null,
									null, null, null, null, null, null, null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
					}
					attributeDetail.setPrice(new QuotePriceBean(price));
				}
				attributeDetailList.add(attributeDetail);
			});
			componentBean.setAttributes(attributeDetailList);
			componentBeans.add(componentBean);
		});
	}

	/**
	 * Save media gateway subcharges
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param serviceDetailPrice
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValueBean> saveServiceDetailPrices(Integer quoteId,
			MstProductFamily teamsPF, User user, Integer referenceId, String referenceName,
			TeamsDRServiceDetailPricesBean serviceDetailPrice) {
		MstProductComponent mstProductComponent = teamsDRQuoteService
				.getProductComponent(serviceDetailPrice.getCompVariant(), user);
		QuoteProductComponent quoteProductComponent = teamsDRQuoteService.constructProductComponent(mstProductComponent,
				teamsPF, referenceId, referenceName);
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
						serviceDetailPrice.getSubVariant());
		List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = new ArrayList<>();
		if (!quoteProductComponentsAttributeValues.isEmpty())
			quoteProductComponentsAttributeValues.forEach(quoteProductComponentsAttributeValue -> {
				QuoteProductComponentsAttributeValueBean attributeValueBean = new QuoteProductComponentsAttributeValueBean(
						quoteProductComponentsAttributeValue);
				QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attributeValueBean.getAttributeId()), ATTRIBUTES);
				if (Objects.nonNull(quotePrice)) {
					quotePrice.setEffectiveUsagePrice(serviceDetailPrice.getUsage());
					quotePriceRepository.save(quotePrice);
				} else
					quotePrice = createQuotePrice(quoteId, teamsPF, attributeValueBean, null, null, null, null,
							serviceDetailPrice.getUsage(), null, null, null, null, null, null, null);
				attributeValueBean.setPrice(new QuotePriceBean(quotePrice));
				attributeValueBeans.add(attributeValueBean);
			});
		else {
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
			quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
			// check for 0 or NA, set the value if false
			if (!CommonConstants.ZERO.equalsIgnoreCase(serviceDetailPrice.getSubVariant())
					|| !TeamsDRConstants.NOT_APPLICABLE.equalsIgnoreCase(serviceDetailPrice.getSubVariant()))
				quoteProductComponentsAttributeValue.setAttributeValues(serviceDetailPrice.getBilledBy());
			QuoteProductComponentsAttributeValueBean attributeValueBean = new QuoteProductComponentsAttributeValueBean(
					quoteProductComponentsAttributeValue);
			attributeValueBean.setName(serviceDetailPrice.getSubVariant());
			quoteProductComponentsAttributeValue.setProductAttributeMaster(teamsDRQuoteService
					.getProductAttributes(attributeValueBean, teamsDRQuoteService.getUserId(Utils.getSource())));
			quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
			attributeValueBean.setAttributeId(quoteProductComponentsAttributeValue.getId());
			QuotePrice quotePrice = createQuotePrice(quoteId, teamsPF, attributeValueBean, null, null, null, null,
					serviceDetailPrice.getUsage(), null, null, null, null, null, null, null);
			attributeValueBean.setPrice(new QuotePriceBean(quotePrice));
			attributeValueBeans.add(attributeValueBean);
		}
		return attributeValueBeans;
	}

	/**
	 * Update managed services
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param teamsPF
	 * @param response
	 * @throws TclCommonException
	 */
	public void updateManagedServicesPrices(Integer quoteId, Integer quoteToLeId, MstProductFamily teamsPF,
			TeamsDRPriceResultsDatum response) throws TclCommonException {
		if (Objects.nonNull(response.getManagedServices())) {
			QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findPlanByQuoteToLeAndProfile(quoteToLeId,
					response.getManagedServices().getCompVariant());
			if (Objects.nonNull(quoteTeamsDR)) {
				savePriceAttributesOfPlan(quoteId, teamsPF, quoteTeamsDR.getId(),
						TeamsDRConstants.TEAMSDR_CONFIG_ATTRIBUTES, response);
				quoteTeamsDR.setMrc(response.getManagedServices().getTotalMrc());
				quoteTeamsDR.setNrc(response.getManagedServices().getTotalNrc());
				quoteTeamsDR.setArc(response.getManagedServices().getTotalArc());
				quoteTeamsDR.setTcv(response.getManagedServices().getTotalTcv());
				quoteTeamsDRRepository.save(quoteTeamsDR);
			}
			if (TeamsDRConstants.CUSTOM_PLAN.equalsIgnoreCase(response.getManagedServices().getCompVariant())) {
				List<String> customServices = response.getManagedServices().getCustomComponents().stream()
						.map(CustomComponentPricesBean::getName).collect(Collectors.toList());
				List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository.findByQuoteToLeIdAndServiceNameIn(quoteToLeId,
						customServices);
				quoteTeamsDRs.forEach(customQuote -> {
					response.getManagedServices().getCustomComponents().stream().anyMatch(customComponentPrices -> {
						if (customComponentPrices.getName().equalsIgnoreCase(customQuote.getServiceName())) {
							try {
								savePriceAttributesOfCustom(quoteId, teamsPF, customQuote.getId(),
										TeamsDRConstants.TEAMSDR_CONFIG_ATTRIBUTES, customComponentPrices);
								customQuote.setMrc(customComponentPrices.getMrc());
								customQuote.setNrc(customComponentPrices.getNrc());
								customQuote.setArc(customComponentPrices.getMrc() * 12);
								customQuote
										.setTcv((customQuote.getMrc() * response.getManagedServices().getContractTerm())
												+ customQuote.getNrc());
								quoteTeamsDRRepository.save(customQuote);
							} catch (TclCommonException e) {
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
							}
							return true;
						}
						return false;
					});
				});
			}
			if (Objects.nonNull(response.getManagedServices().getServiceDetail())) {
				User user = teamsDRQuoteService.getUserId(Utils.getSource());
				response.getManagedServices().getServiceDetail()
						.forEach(serviceDetailPrices -> saveServiceDetailPrices(quoteId, teamsPF, user,
								quoteTeamsDR.getId(), TeamsDRConstants.TEAMSDR_CONFIG_ATTRIBUTES, serviceDetailPrices));
			}
		}
	}

	/**
	 * Save price attributes of media gateway
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param referenceId
	 * @param referenceName
	 * @param response
	 * @throws TclCommonException
	 */
	private void savePriceAttributesOfMg(Integer quoteId, MstProductFamily teamsPF, Integer referenceId,
			String referenceName, MediaGatewayListPricesBean response) throws TclCommonException {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, teamsPF, referenceName);
		List<QuoteProductComponentBean> componentBeans = new ArrayList<>();
		quoteProductComponents.forEach(quoteProductComponent -> {
			QuoteProductComponentBean componentBean = new QuoteProductComponentBean();
			componentBean.setName(quoteProductComponent.getMstProductComponent().getName());
			List<QuoteProductComponentsAttributeValueBean> attributeDetailList = new ArrayList<>();
			List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
			attributeValues.forEach(quoteProductComponentsAttributeValue -> {
				QuoteProductComponentsAttributeValueBean attributeDetail = new QuoteProductComponentsAttributeValueBean();
				attributeDetail.setName(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName());
				attributeDetail.setAttributeId(quoteProductComponentsAttributeValue.getId());
				attributeDetail.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
				if (ALL_CHARGES.contains(attributeDetail.getName())) {
					Optional<QuotePrice> quotePrice = quotePriceRepository
							.findByReferenceNameAndReferenceId(ATTRIBUTES, attributeDetail.getAttributeId().toString())
							.stream().findAny();
					QuotePrice price = new QuotePrice();
					if (CPE_RENTAL_CHARGES.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumMrc(response.getCpeOutrightRentalCharges().getUnitMrc());
							price.setEffectiveMrc(response.getCpeOutrightRentalCharges().getMrc());
							price.setMinimumNrc(response.getCpeOutrightRentalCharges().getUnitNrc());
							price.setEffectiveNrc(response.getCpeOutrightRentalCharges().getNrc());
							quotePriceRepository.save(price);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail,
									response.getCpeOutrightRentalCharges().getUnitMrc(),
									response.getCpeOutrightRentalCharges().getMrc(),
									response.getCpeOutrightRentalCharges().getUnitNrc(),
									response.getCpeOutrightRentalCharges().getNrc(), null, null, null, null, null, null,
									null, null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
						response.getCpeOutrightRentalCharges().getCpeSubcharges().forEach(
								cpeSubchargesBean -> attributeDetailList
										.addAll(saveMgSubcharges(quoteId, teamsPF, quoteProductComponent,
												cpeSubchargesBean, CPE_RENTAL_CHARGES)));
					} else if (CPE_OUTRIGHT_CHARGES.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumMrc(response.getCpeOutrightRentalCharges().getUnitMrc());
							price.setEffectiveMrc(response.getCpeOutrightRentalCharges().getMrc());
							price.setMinimumNrc(response.getCpeOutrightRentalCharges().getUnitNrc());
							price.setEffectiveNrc(response.getCpeOutrightRentalCharges().getNrc());
							quotePriceRepository.save(price);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail,
									response.getCpeOutrightRentalCharges().getUnitMrc(),
									response.getCpeOutrightRentalCharges().getMrc(),
									response.getCpeOutrightRentalCharges().getUnitNrc(),
									response.getCpeOutrightRentalCharges().getNrc(), null, null, null, null, null, null,
									null, null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
						response.getCpeOutrightRentalCharges().getCpeSubcharges().forEach(
								cpeSubchargesBean -> attributeDetailList
										.addAll(saveMgSubcharges(quoteId, teamsPF, quoteProductComponent,
												cpeSubchargesBean, CPE_OUTRIGHT_CHARGES)));
					} else if (MANAGEMENT_AND_MONITORING_CHARGES.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumMrc(response.getCpeManagement().getUnitMrc());
							price.setEffectiveMrc(response.getCpeManagement().getMrc());
							price.setMinimumNrc(response.getCpeManagement().getUnitNrc());
							price.setEffectiveNrc(response.getCpeManagement().getNrc());
							quotePriceRepository.save(price);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail,
									response.getCpeManagement().getUnitMrc(), response.getCpeManagement().getMrc(),
									response.getCpeManagement().getUnitNrc(), response.getCpeManagement().getNrc(),
									null, null, null, null, null, null, null, null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
					} else if (CPE_AMC_CHARGES.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumMrc(response.getCpeAmcCharges().getUnitMrc());
							price.setEffectiveMrc(response.getCpeAmcCharges().getMrc());
							price.setMinimumNrc(response.getCpeAmcCharges().getUnitNrc());
							price.setEffectiveNrc(response.getCpeAmcCharges().getNrc());
							quotePriceRepository.save(price);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail,
									response.getCpeAmcCharges().getUnitMrc(), response.getCpeAmcCharges().getMrc(),
									response.getCpeAmcCharges().getUnitNrc(), response.getCpeAmcCharges().getNrc(),
									null, null, null, null, null, null, null, null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
						response.getCpeAmcCharges().getCpeSubcharges().forEach(cpeSubchargesBean -> attributeDetailList
								.addAll(saveMgSubcharges(quoteId, teamsPF, quoteProductComponent, cpeSubchargesBean,
										CPE_AMC_CHARGES)));
					} else if (CPE_DELIVERY_CHARGES.equalsIgnoreCase(attributeDetail.getName())) {
						if (quotePrice.isPresent()) {
							price = quotePrice.get();
							price.setMinimumNrc(response.getUnitNrcWithDelivery());
							price.setEffectiveNrc(response.getUnitNrcWithDelivery());
							quotePriceRepository.save(price);
						} else {
							price = createQuotePrice(quoteId, teamsPF, attributeDetail, null, null,
									response.getUnitNrcWithDelivery(), response.getUnitNrcWithDelivery(), null, null,
									null, null, null, null, null, null);
						}
						attributeDetail.setPrice(new QuotePriceBean(price));
					}
				}
				attributeDetailList.add(attributeDetail);
			});
			componentBean.setAttributes(attributeDetailList);
			componentBeans.add(componentBean);
		});
	}

	/**
	 * Save media gateway subcharges
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param quoteProductComponent
	 * @param cpeSubchargesBean
	 * @return
	 */
	private List<QuoteProductComponentsAttributeValueBean> saveMgSubcharges(Integer quoteId, MstProductFamily teamsPF,
			QuoteProductComponent quoteProductComponent, CpeSubchargesBean cpeSubchargesBean, String displayName) {
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
						cpeSubchargesBean.getComponentType());
		List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = new ArrayList<>();
		if (!quoteProductComponentsAttributeValues.isEmpty())
			quoteProductComponentsAttributeValues.forEach(quoteProductComponentsAttributeValue -> {
				QuoteProductComponentsAttributeValueBean attributeValueBean = new QuoteProductComponentsAttributeValueBean(
						quoteProductComponentsAttributeValue);
				QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(attributeValueBean.getAttributeId()), ATTRIBUTES);
				if (Objects.nonNull(quotePrice)) {
					quotePrice.setMinimumMrc(cpeSubchargesBean.getUnitMrcSp());
					quotePrice.setMinimumNrc(cpeSubchargesBean.getUnitNrcSp());
					quotePrice.setMinimumArc(cpeSubchargesBean.getUnitArcSp());
					quotePrice.setEffectiveMrc(cpeSubchargesBean.getMrc());
					quotePrice.setEffectiveNrc(cpeSubchargesBean.getNrc());
					quotePrice.setEffectiveArc(cpeSubchargesBean.getArc());
					quotePrice.setDiscountInPercent(cpeSubchargesBean.getDiscountToTata());
					quotePrice.setCatalogMrc(cpeSubchargesBean.getCostMrc());
					quotePrice.setCatalogNrc(cpeSubchargesBean.getCostNrc());
					quotePrice.setCatalogArc(cpeSubchargesBean.getCostArc());
					quotePrice.setComputedMrc(cpeSubchargesBean.getAfterDiscountMrc());
					quotePrice.setComputedNrc(cpeSubchargesBean.getAfterDiscountNrc());
					quotePrice.setComputedArc(cpeSubchargesBean.getAfterDiscountArc());
					quotePriceRepository.save(quotePrice);
				} else
					quotePrice = createQuotePrice(quoteId, teamsPF, attributeValueBean,
							cpeSubchargesBean.getUnitMrcSp(), cpeSubchargesBean.getMrc(),
							cpeSubchargesBean.getUnitNrcSp(), cpeSubchargesBean.getNrc(), null,
							cpeSubchargesBean.getDiscountToTata(), cpeSubchargesBean.getCostMrc(),
							cpeSubchargesBean.getCostNrc(), cpeSubchargesBean.getCostArc(),
							cpeSubchargesBean.getAfterDiscountMrc(), cpeSubchargesBean.getAfterDiscountNrc(),
							cpeSubchargesBean.getAfterDiscountArc());
				attributeValueBean.setPrice(new QuotePriceBean(quotePrice));
				attributeValueBeans.add(attributeValueBean);
			});
		else {
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
			quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
			quoteProductComponentsAttributeValue.setDisplayValue(displayName);
			// check for 0 or NA, set the value if false
			if (!CommonConstants.ZERO.equalsIgnoreCase(cpeSubchargesBean.getComponentName())
					|| !TeamsDRConstants.NOT_APPLICABLE.equalsIgnoreCase(cpeSubchargesBean.getComponentName()))
				quoteProductComponentsAttributeValue.setAttributeValues(cpeSubchargesBean.getComponentName());
			QuoteProductComponentsAttributeValueBean attributeValueBean = new QuoteProductComponentsAttributeValueBean(
					quoteProductComponentsAttributeValue);
			attributeValueBean.setName(cpeSubchargesBean.getComponentType());
			quoteProductComponentsAttributeValue.setProductAttributeMaster(teamsDRQuoteService
					.getProductAttributes(attributeValueBean, teamsDRQuoteService.getUserId(Utils.getSource())));
			quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
			attributeValueBean.setAttributeId(quoteProductComponentsAttributeValue.getId());
			QuotePrice quotePrice = createQuotePrice(quoteId, teamsPF, attributeValueBean,
					cpeSubchargesBean.getUnitMrcSp(), cpeSubchargesBean.getMrc(), cpeSubchargesBean.getUnitNrcSp(),
					cpeSubchargesBean.getNrc(), null, cpeSubchargesBean.getDiscountToTata(),
					cpeSubchargesBean.getCostMrc(), cpeSubchargesBean.getCostNrc(), cpeSubchargesBean.getCostArc(),
					cpeSubchargesBean.getAfterDiscountMrc(), cpeSubchargesBean.getAfterDiscountNrc(),
					cpeSubchargesBean.getAfterDiscountArc());
			attributeValueBean.setPrice(new QuotePriceBean(quotePrice));
			attributeValueBeans.add(attributeValueBean);
		}
		return attributeValueBeans;
	}

	/**
	 * Update media gateway prices
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param teamsPF
	 * @param response
	 */
	public void updateMediaGatewayPrices(Integer quoteId, Integer quoteToLeId, MstProductFamily teamsPF,
			TeamsDRPriceResultsDatum response) {
		if (Objects.nonNull(response.getMediaGateway())) {
			Optional<QuoteTeamsDR> quoteTeamsDR = quoteTeamsDRRepository
					.findByQuoteToLeIdAndServiceName(quoteToLeId, TeamsDRConstants.MEDIA_GATEWAY);
			if (quoteTeamsDR.isPresent()) {
				if (Objects.nonNull(response) && Objects.nonNull(response.getMediaGateway()) && Objects
						.nonNull(response.getMediaGateway().getMediaGatewayPriceDetails())) {
					response.getMediaGateway().getMediaGatewayPriceDetails().stream()
							.forEach(mediaGatewayPriceDetailsBean -> {
								Optional<QuoteDirectRouting> quoteDirectRouting = quoteDirectRoutingRepository
										.findById(mediaGatewayPriceDetailsBean.getId());
								if (quoteDirectRouting.isPresent())
									updateQuoteDrPrices(quoteDirectRouting.get(),
											mediaGatewayPriceDetailsBean.getCountryWiseMrc(),
											mediaGatewayPriceDetailsBean.getCountryWiseNrc(),
											mediaGatewayPriceDetailsBean.getCountryWiseArc(),
											mediaGatewayPriceDetailsBean.getCountryWiseTcv());

								updateDrCityAndMgPrices(quoteId, teamsPF, mediaGatewayPriceDetailsBean);
							});
					updateQuoteTeamsDRPrices(quoteTeamsDR.get(), response.getMediaGateway().getTotalMrc(),
							response.getMediaGateway().getTotalNrc(), response.getMediaGateway().getTotalArc(),
							response.getMediaGateway().getTotalTcv());
				}
			}
		}
	}

	/**
	 * Update Quote DR and Mg Prices iterating through price response
	 *
	 * @param quoteId
	 * @param teamsPF
	 * @param mediaGatewayPriceDetailsBean
	 */
	private void updateDrCityAndMgPrices(Integer quoteId, MstProductFamily teamsPF,
			MediaGatewayPriceDetailsBean mediaGatewayPriceDetailsBean) {
		mediaGatewayPriceDetailsBean.getLocationWiseDetails().stream().forEach(locationWiseDetail -> {
			TeamsDRCumulativePricesBean cumulativePricesBean = new TeamsDRCumulativePricesBean();

			locationWiseDetail.getMediaGatewayListPrices().forEach(mediaGatewayListPricesBean -> {
				Optional<QuoteDirectRoutingMediaGateways> quoteDirectRoutingMediaGateway = quoteDirectRoutingMgRepository
						.findById(mediaGatewayListPricesBean.getMgId());
				if (quoteDirectRoutingMediaGateway.isPresent()) {
					try {
						savePriceAttributesOfMg(quoteId, teamsPF, quoteDirectRoutingMediaGateway.get().getId(),
								TeamsDRConstants.TEAMSDR_MEDIAGATEWAY_ATTRIBUTES, mediaGatewayListPricesBean);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
					}
					updateQuoteDrMgPrices(mediaGatewayListPricesBean.getMrc(),
							mediaGatewayListPricesBean.getNrcWithDelivery(), mediaGatewayListPricesBean.getArc(),
							mediaGatewayListPricesBean.getTcv(), quoteDirectRoutingMediaGateway.get());
					findCumulativePrice(cumulativePricesBean, mediaGatewayListPricesBean.getMrc(),
							mediaGatewayListPricesBean.getNrcWithDelivery(), mediaGatewayListPricesBean.getArc(),
							mediaGatewayListPricesBean.getTcv());
				}
			});
			Optional<QuoteDirectRoutingCity> quoteDirectRoutingCity = quoteDirectRoutingCityRepository
					.findById(locationWiseDetail.getMgSiteAddress().getCityId());
			if (quoteDirectRoutingCity.isPresent())
				updateQuoteDrCityPrices(quoteDirectRoutingCity.get(), cumulativePricesBean.getMrc(),
						cumulativePricesBean.getNrc(), cumulativePricesBean.getArc(), cumulativePricesBean.getTcv());
		});
	}

	/**
	 * To find cumulative prices
	 *
	 * @param cumulativePricesBean
	 * @param mrc
	 * @param nrc
	 * @param arc
	 * @param tcv
	 */
	private void findCumulativePrice(TeamsDRCumulativePricesBean cumulativePricesBean, Double mrc, Double nrc,
			Double arc, Double tcv) {
		cumulativePricesBean.setMrc(cumulativePricesBean.getMrc() + mrc);
		cumulativePricesBean.setNrc(cumulativePricesBean.getNrc() + nrc);
		cumulativePricesBean.setArc(cumulativePricesBean.getArc() + arc);
		cumulativePricesBean.setTcv(cumulativePricesBean.getTcv() + tcv);
	}

	/**
	 * Update quote DR prices
	 *
	 * @param quoteDirectRouting
	 * @param mrc
	 * @param nrc
	 * @param arc
	 * @param tcv
	 */
	private void updateQuoteDrPrices(QuoteDirectRouting quoteDirectRouting, Double mrc, Double nrc, Double arc,
			Double tcv) {
		quoteDirectRouting.setMrc(BigDecimal.valueOf(mrc));
		quoteDirectRouting.setNrc(BigDecimal.valueOf(nrc));
		quoteDirectRouting.setArc(BigDecimal.valueOf(arc));
		quoteDirectRouting.setTcv(BigDecimal.valueOf(tcv));
		quoteDirectRoutingRepository.save(quoteDirectRouting);
	}

	/**
	 * Update quote dr media gateway prices
	 *
	 * @param mrc
	 * @param nrcWithDelivery
	 * @param arc
	 * @param tcv
	 * @param quoteDirectRoutingMediaGateway
	 */
	private void updateQuoteDrMgPrices(Double mrc, Double nrcWithDelivery, Double arc, Double tcv,
			QuoteDirectRoutingMediaGateways quoteDirectRoutingMediaGateway) {
		quoteDirectRoutingMediaGateway.setMrc(mrc);
		quoteDirectRoutingMediaGateway.setNrc(nrcWithDelivery);
		quoteDirectRoutingMediaGateway.setArc(arc);
		quoteDirectRoutingMediaGateway.setTcv(tcv);
		quoteDirectRoutingMgRepository.save(quoteDirectRoutingMediaGateway);
	}

	/**
	 * Update microsoft license prices
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param teamsPF
	 * @param response
	 */
	public void updateMicrosoftLicensePrices(Integer quoteId, Integer quoteToLeId, MstProductFamily teamsPF,
			TeamsDRPriceResultsDatum response) {
		if (Objects.nonNull(response.getLicense())) {
			Optional<QuoteTeamsDR> quoteTeamsDR = quoteTeamsDRRepository.findByQuoteToLeIdAndServiceName(quoteToLeId,
					TeamsDRConstants.MICROSOFT_LICENSE);
			if (quoteTeamsDR.isPresent()) {
				List<QuoteTeamsLicense> quoteTeamsLicenses = quoteTeamsLicenseRepository
						.findByQuoteTeamsDR(quoteTeamsDR.get());
				if (Objects.nonNull(quoteTeamsLicenses) && !quoteTeamsLicenses.isEmpty()) {
					quoteTeamsLicenses.forEach(quoteTeamsLicense -> {
						response.getLicense().getLicenseDetail().stream()
								.flatMap(licenseDetail -> licenseDetail.getListOfLicense().stream())
								.anyMatch(license -> {
									if (license.getOfferDisplayName()
											.equalsIgnoreCase(quoteTeamsLicense.getLicenseName())) {
										try {
											savePriceAttributesOfLicense(quoteId, teamsPF, quoteTeamsLicense.getId(),
													TeamsDRConstants.TEAMSDR_LICENSE_CHARGES, license);
										} catch (TclCommonException e) {
											throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
										}
										quoteTeamsLicense.setMrc(license.getMrc());
										quoteTeamsLicense.setNrc(license.getNrc());
										quoteTeamsLicense.setArc(license.getArc());
										quoteTeamsLicense.setTcv(license.getTcv());
										quoteTeamsLicenseRepository.save(quoteTeamsLicense);
										return true;
									}
									return false;
								});
						try {
							savePriceAttributesOfLicense(quoteId, teamsPF, quoteTeamsDR.get().getId(),
									TeamsDRConstants.TEAMSDR_LICENSE_ATTRIBUTES, response.getLicense());
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
						}
						quoteTeamsDR.get().setMrc(response.getLicense().getTotalMrc());
						quoteTeamsDR.get().setNrc(response.getLicense().getTotalNrc());
						quoteTeamsDR.get().setArc(response.getLicense().getTotalArc());
						quoteTeamsDR.get().setTcv(response.getLicense().getTotalTcv());
						quoteTeamsDRRepository.save(quoteTeamsDR.get());
					});
				}
			}
		}
	}

	/**
	 * Update plan and media gateway prices
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param priceResponse
	 * @throws TclCommonException
	 */
	public void updatePlanAndMediaGatewayPrices(Integer quoteId, Integer quoteToLeId, TeamsDRPricingBean priceResponse)
			throws TclCommonException {
		TeamsDRPriceResultsDatum response = priceResponse.getResults().get(0);
		MstProductFamily mstProductFamily = teamsDRQuoteService.getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
		updateManagedServicesPrices(quoteId, quoteToLeId, mstProductFamily, response);
		updateMediaGatewayPrices(quoteId, quoteToLeId, mstProductFamily, response);
	}

	/**
	 * Update license prices
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param priceResponse
	 * @throws TclCommonException
	 */
	public void updateLicensePrices(Integer quoteId, Integer quoteToLeId, TeamsDRPricingBean priceResponse)
			throws TclCommonException {
		TeamsDRPriceResultsDatum response = priceResponse.getResults().get(0);
		MstProductFamily mstProductFamily = teamsDRQuoteService.getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
		updateMicrosoftLicensePrices(quoteId, quoteToLeId, mstProductFamily, response);
	}

	/**
	 * Update all teams service prices
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param priceResponse
	 * @throws TclCommonException
	 */
	public void updateAllTeamsServicesPrices(Integer quoteId, Integer quoteToLeId, TeamsDRPricingBean priceResponse)
			throws TclCommonException {
		TeamsDRPriceResultsDatum response = priceResponse.getResults().get(0);
		MstProductFamily mstProductFamily = teamsDRQuoteService.getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
		updateManagedServicesPrices(quoteId, quoteToLeId, mstProductFamily, response);
		updateMicrosoftLicensePrices(quoteId, quoteToLeId, mstProductFamily, response);
		updateMediaGatewayPrices(quoteId, quoteToLeId, mstProductFamily, response);
	}

	/**
	 * Method to set precision
	 *
	 * @param value
	 * @param precision
	 * @return
	 */
	public Double setPrecision(Double value, Integer precision) {
		Double result = 0.0;
		if (Objects.nonNull(value)) {
			if (precision == 2) {
				result = Math.round(value * 100.0) / 100.0;
				DecimalFormat df1 = new DecimalFormat(".##");
				result = Double.parseDouble(df1.format(result));
			} else if (precision == 4) {
				result = Math.round(value * 10000.0) / 10000.0;
				DecimalFormat df2 = new DecimalFormat(".####");
				result = Double.parseDouble(df2.format(result));
			}
		}
		return result;
	}

	/**
	 * Update price on click of tab
	 *
	 * @param service
	 * @param unitPriceBean
	 * @return
	 */
	public TeamsDRUnitPriceBean getUpdatedPriceOnTab(String service, TeamsDRUnitPriceBean unitPriceBean) {
		if(MEDIA_GATEWAY.equalsIgnoreCase(service)){
			calculateSubTotalForMg(unitPriceBean);
		} else calculatePricesForOtherService(unitPriceBean);
		return unitPriceBean;
	}

	/**
	 * To calculate subtotal and final prices (unit*quantity)
	 * and quote prices for media gateway
	 *
	 * @param unitPriceBean
	 */
	private void calculateSubTotalForMg(TeamsDRUnitPriceBean unitPriceBean) {
		unitPriceBean.setQuoteTeamsMrc(
				checkForNull(unitPriceBean.getQuoteTeamsMrc()) - checkForNull(unitPriceBean.getSubTotalFinalMrc()));
		unitPriceBean.setQuoteTeamsNrc(
				checkForNull(unitPriceBean.getQuoteTeamsNrc()) - checkForNull(unitPriceBean.getSubTotalFinalNrc()));
		unitPriceBean.setSubTotalFinalMrc(
				checkForNull(unitPriceBean.getSubTotalFinalMrc()) - checkForNull(unitPriceBean.getFinalMrc()));
		unitPriceBean.setSubTotalFinalNrc(
				checkForNull(unitPriceBean.getSubTotalFinalNrc()) - checkForNull(unitPriceBean.getFinalNrc()));
		unitPriceBean.setFinalMrc(Utils.setPrecision(
				TeamsDRUtils.checkForNull(unitPriceBean.getUnitMrc()) * (Objects.nonNull(unitPriceBean.getQuantity()) ?
						unitPriceBean.getQuantity() : 0), 2));
		unitPriceBean.setFinalNrc(Utils.setPrecision(
				TeamsDRUtils.checkForNull(unitPriceBean.getUnitNrc()) * (Objects.nonNull(unitPriceBean.getQuantity()) ?
						unitPriceBean.getQuantity() : 0), 2));
		unitPriceBean.setSubTotalFinalMrc(
				checkForNull(unitPriceBean.getSubTotalFinalMrc()) + checkForNull(unitPriceBean.getFinalMrc()));
		unitPriceBean.setSubTotalFinalNrc(
				checkForNull(unitPriceBean.getSubTotalFinalNrc()) + checkForNull(unitPriceBean.getFinalNrc()));
		if (Objects.nonNull(unitPriceBean.getQuantity())) {
			unitPriceBean.setSubTotalUnitMrc(
					checkForNull(unitPriceBean.getSubTotalFinalMrc()) / unitPriceBean.getQuantity());
			unitPriceBean.setSubTotalUnitNrc(
					checkForNull(unitPriceBean.getSubTotalFinalNrc()) / unitPriceBean.getQuantity());
		}
		unitPriceBean.setQuoteTeamsMrc(
				checkForNull(unitPriceBean.getQuoteTeamsMrc()) + checkForNull(unitPriceBean.getSubTotalFinalMrc()));
		unitPriceBean.setQuoteTeamsNrc(
				checkForNull(unitPriceBean.getQuoteTeamsNrc()) + checkForNull(unitPriceBean.getSubTotalFinalNrc()));
	}

	/**
	 * Calculate quoteteams dr price and final price(unit price* quantity)
	 * for all services other than media gateway
	 *
	 * @param unitPriceBean
	 */
	private void calculatePricesForOtherService(TeamsDRUnitPriceBean unitPriceBean) {
		unitPriceBean.setQuoteTeamsMrc(
				checkForNull(unitPriceBean.getQuoteTeamsMrc()) - checkForNull(unitPriceBean.getFinalMrc()));
		unitPriceBean.setQuoteTeamsNrc(
				checkForNull(unitPriceBean.getQuoteTeamsNrc()) - checkForNull(unitPriceBean.getFinalNrc()));
		unitPriceBean.setFinalMrc(setPrecision(
				TeamsDRUtils.checkForNull(unitPriceBean.getUnitMrc()) * (Objects.nonNull(unitPriceBean.getQuantity()) ?
						unitPriceBean.getQuantity() : 0), 2));
		unitPriceBean.setFinalNrc(setPrecision(
				TeamsDRUtils.checkForNull(unitPriceBean.getUnitNrc()) * (Objects.nonNull(unitPriceBean.getQuantity()) ?
						unitPriceBean.getQuantity() : 0), 2));
		unitPriceBean.setQuoteTeamsMrc(
				checkForNull(unitPriceBean.getQuoteTeamsMrc()) + checkForNull(unitPriceBean.getFinalMrc()));
		unitPriceBean.setQuoteTeamsNrc(
				checkForNull(unitPriceBean.getQuoteTeamsNrc()) + checkForNull(unitPriceBean.getFinalNrc()));
	}

	/**
	 * Process Manual prices of TeamsDR
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param manualPriceBean
	 * @param isManualFP
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRManualPriceBean processManualFP(Integer quoteId, Integer quoteLeId,
			TeamsDRManualPriceBean manualPriceBean, Boolean isManualFP) throws TclCommonException {
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
		Integer contractPeriod = TeamsDRUtils.extractContractPeriodFromString(quoteToLe.get().getTermInMonths());
		if (quoteToLe.isPresent()) {
			List<Integer> attributeIds = manualPriceBean.getQuoteTeamsDRManualPrices().stream()
					.filter(quoteTeamsPrice -> !(CUSTOM_PLAN.equalsIgnoreCase(quoteTeamsPrice.getPlan()) && Objects
							.isNull(quoteTeamsPrice.getServiceName())))
					.flatMap(quoteTeamsPrice -> quoteTeamsPrice.getAttributes().stream())
					.map(QuoteProductComponentsAttributeValueBean::getAttributeId).collect(Collectors.toList());
			List<QuoteProductComponentsAttributeValue> quoteProductAttributes = quoteProductComponentsAttributeValueRepository
					.findAllById(attributeIds);
			Map<String, TeamsDRCumulativePricesBean> cumulativePrices = new HashMap<>();
			manualPriceBean.getQuoteTeamsDRManualPrices().forEach(quoteTeamsPrice -> {
				quoteTeamsPrice.getAttributes().forEach(attributes -> {
					quoteProductAttributes.stream().anyMatch(quoteProductAttribute -> {
						if (quoteProductAttribute.getId().equals(attributes.getAttributeId())) {
							QuotePrice quotePrice = quotePriceRepository
									.findByReferenceIdAndReferenceName(String.valueOf(quoteProductAttribute.getId()),
											ATTRIBUTES);
							if (Objects.nonNull(quotePrice)) {
								updateQuotePrice(quoteToLe.get(),quotePrice, attributes.getPrice());
							}
							Integer referenceId = quoteProductAttribute.getQuoteProductComponent().getReferenceId();
							String referenceName = quoteProductAttribute.getQuoteProductComponent().getReferenceName();
							getCumulativePricesForReferenceIdAndName(cumulativePrices, attributes, referenceId,
									referenceName);
							return true;
						} else
							return false;
					});
				});
			});
			TeamsDRCumulativePricesBean quoteCumulativePrices = new TeamsDRCumulativePricesBean();
			Map<Integer, TeamsDRCumulativePricesBean> mgCumulativePrices = new HashMap<>();
			List<Integer> quoteDrMgIds = new ArrayList<>();
			manualPriceBean.getQuoteTeamsDRManualPrices().forEach(quoteTeamsPrice -> {
				if (Objects.nonNull(quoteTeamsPrice.getPlan())) {
					cumulativePrices.forEach((key, value) -> {
						String[] references = key.split(CommonConstants.COMMA);
						updateQuoteToLePrices(quoteToLe.get(), Integer.parseInt(references[0]), manualPriceBean,
								contractPeriod, value.getMrc(), value.getNrc());
					});
				} else if (TeamsDRConstants.MICROSOFT_LICENSE.equalsIgnoreCase(quoteTeamsPrice.getServiceName())) {
					cumulativePrices.forEach((key, value) -> {
						String[] references = key.split(CommonConstants.COMMA);
						if (TeamsDRConstants.TEAMSDR_LICENSE_CHARGES
								.equalsIgnoreCase(references[1]) && !TEAMSDR_LICENSE_ATTRIBUTES
								.equalsIgnoreCase(references[1])) {
							QuoteTeamsLicense quoteTeamsLicense = updatePriceQuoteTeamsLicense(references[0], value,
									contractPeriod);
						}
						quoteCumulativePrices.setTotalMrc(quoteCumulativePrices.getTotalMrc() + value.getMrc());
						quoteCumulativePrices.setTotalNrc(quoteCumulativePrices.getTotalNrc() + value.getNrc());
						quoteCumulativePrices.setTotalArc(quoteCumulativePrices.getTotalArc() + value.getArc());
						quoteCumulativePrices.setTotalTcv(quoteCumulativePrices.getTotalTcv() + value.getTcv());
					});
				} else if (TeamsDRConstants.MEDIA_GATEWAY.equalsIgnoreCase(quoteTeamsPrice.getServiceName())) {
					updateMgManualFP(quoteToLe, cumulativePrices, mgCumulativePrices);
				}
			});
			Optional<String> customPlan = manualPriceBean.getQuoteTeamsDRManualPrices().stream()
					.filter(priceBean -> Objects.nonNull(priceBean.getPlan())).map(priceBean -> priceBean.getPlan())
					.findAny();
			if (customPlan.isPresent() && CUSTOM_PLAN.equalsIgnoreCase(customPlan.get())) {
				updateCustomPlanPrices(quoteLeId);
			}
			Map<Integer, TeamsDRCumulativePricesBean> drCumulativePrices = new HashMap<>();
			manualPriceBean.getQuoteTeamsDRManualPrices().forEach(quoteTeamsPrice -> {
				if (TeamsDRConstants.MICROSOFT_LICENSE.equalsIgnoreCase(quoteTeamsPrice.getServiceName())) {
					updateQuoteToLePrices(quoteToLe.get(), quoteTeamsPrice.getQuoteTeamsDRId(), manualPriceBean,
							contractPeriod, quoteCumulativePrices.getTotalMrc(), quoteCumulativePrices.getTotalNrc());
				} else if (TeamsDRConstants.MEDIA_GATEWAY.equalsIgnoreCase(quoteTeamsPrice.getServiceName())) {
					updateDRCityManualFp(quoteToLe.get(), cumulativePrices, mgCumulativePrices, drCumulativePrices,
							quoteTeamsPrice, manualPriceBean);
				}
			});
			quoteToLeRepository.save(quoteToLe.get());
			manualPriceBean.setQuoteToLeMrc(quoteToLe.get().getFinalMrc());
			manualPriceBean.setQuoteToLeNrc(quoteToLe.get().getFinalNrc());
			manualPriceBean.setQuoteToLeArc(quoteToLe.get().getFinalArc());
			manualPriceBean.setQuoteToLeTcv(quoteToLe.get().getTotalTcv());

			if (Objects.nonNull(isManualFP) && isManualFP) {
				List<QuoteLeAttributeValue> quoteToLeAttributeValueDto = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe.get(),
								LeAttributesConstants.ADMIN_CHANGED_PRICE);
				if (quoteToLeAttributeValueDto.isEmpty()) {
					QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
					quoteLeAttributeValue.setMstOmsAttribute(teamsDRQuoteService
							.createOrGetMstOmsAttribute(LeAttributesConstants.ADMIN_CHANGED_PRICE,
									LeAttributesConstants.ADMIN_CHANGED_PRICE).stream().findAny().get());
					quoteLeAttributeValue.setQuoteToLe(quoteToLe.get());
					quoteLeAttributeValue.setAttributeValue(isManualFP.toString());
					quoteLeAttributeValue.setDisplayValue(LeAttributesConstants.ADMIN_CHANGED_PRICE);
					quoteLeAttributeValueRepository.save(quoteLeAttributeValue);
				}
			}
		}
		return manualPriceBean;
	}

	/**
	 * Update custom plan prices
	 *
	 * @param quoteLeId
	 */
	private void updateCustomPlanPrices(Integer quoteLeId) {
		List<QuoteTeamsDR> quoteTeamsDRs = quoteTeamsDRRepository
				.findByQuoteToLeAndNotInServiceNames(quoteLeId, Arrays.asList(MICROSOFT_LICENSE, MEDIA_GATEWAY));
		if (!quoteTeamsDRs.isEmpty()) {
			TeamsDRCumulativePricesBean prices = new TeamsDRCumulativePricesBean();
			quoteTeamsDRs.stream().filter(quoteTeamsDR -> Objects
					.nonNull(quoteTeamsDR.getServiceName()) && TeamsDRConstants.CUSTOM_PLAN
					.equalsIgnoreCase(quoteTeamsDR.getProfileName())).forEach(quoteTeamsDR -> {
				prices.setMrc(prices.getMrc() + checkForNull(quoteTeamsDR.getMrc()));
				prices.setNrc(prices.getNrc() + checkForNull(quoteTeamsDR.getNrc()));
				prices.setArc(prices.getArc() + checkForNull(quoteTeamsDR.getArc()));
				prices.setTcv(prices.getTcv() + checkForNull(quoteTeamsDR.getTcv()));
			});
			QuoteTeamsDR quoteTeamsDR = quoteTeamsDRRepository.findPlanByQuoteToLeAndProfile(quoteLeId, CUSTOM_PLAN);
			quoteTeamsDRRepository
					.save(updateQuoteTeamsDRPrices(quoteTeamsDR, prices.getMrc(), prices.getNrc(), prices.getArc(),
							prices.getTcv()));
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteTeamsDR.getId(),
							quoteTeamsDR.getProfileName(), MICROSOFT_CLOUD_SOLUTIONS);
			if (Objects.nonNull(quoteProductComponent)) {
				List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());
				if (!attributeValue.isEmpty()) {
					attributeValue.stream().filter(attribute -> RECURRING
							.equalsIgnoreCase(attribute.getProductAttributeMaster().getName()) || NON_RECURRING
							.equalsIgnoreCase(attribute.getProductAttributeMaster().getName())).forEach(attribute -> {
						if (attribute.getProductAttributeMaster().getName().equalsIgnoreCase(RECURRING)) {
							QuotePrice quotePrice = quotePriceRepository
									.findByReferenceIdAndReferenceName(String.valueOf(attribute.getId()), ATTRIBUTES);
							if (Objects.nonNull(quotePrice)) {
								quotePrice.setMinimumMrc(setPrecision(prices.getMrc() / quoteTeamsDR.getNoOfUsers(), 2));
								quotePrice.setEffectiveMrc(setPrecision(prices.getMrc(), 2));
								quotePriceRepository.save(quotePrice);
							}
						} else if (attribute.getProductAttributeMaster().getName().equalsIgnoreCase(NON_RECURRING)) {
							QuotePrice quotePrice = quotePriceRepository
									.findByReferenceIdAndReferenceName(String.valueOf(attribute.getId()), ATTRIBUTES);
							if (Objects.nonNull(quotePrice)) {
								quotePrice.setMinimumNrc(setPrecision(prices.getNrc() / quoteTeamsDR.getNoOfUsers(), 2));
								quotePrice.setEffectiveNrc(setPrecision(prices.getNrc(), 2));
								quotePriceRepository.save(quotePrice);
							}
						}
					});
				}
			}
		}
	}

	/**
	 * Get cumulative prices for reference id and name
	 *
	 * @param cumulativePrices
	 * @param attributes
	 * @param referenceId
	 * @param referenceName
	 */
	private void getCumulativePricesForReferenceIdAndName(Map<String, TeamsDRCumulativePricesBean> cumulativePrices,
			QuoteProductComponentsAttributeValueBean attributes, Integer referenceId, String referenceName) {
		if (cumulativePrices.containsKey(referenceId + CommonConstants.COMMA + referenceName) && COMMERCIAL_COMPONENTS
				.contains(attributes.getName())) {
			TeamsDRCumulativePricesBean cumulative = cumulativePrices
					.get(referenceId + CommonConstants.COMMA + referenceName);
			cumulative.setMrc(cumulative.getMrc() + TeamsDRUtils.checkForNull(attributes.getPrice().getEffectiveMrc()));
			cumulative.setNrc(cumulative.getNrc() + TeamsDRUtils.checkForNull(attributes.getPrice().getEffectiveNrc()));
			cumulativePrices.put(referenceId + CommonConstants.COMMA + referenceName, cumulative);
		} else if (COMMERCIAL_COMPONENTS.contains(attributes.getName())) {
			TeamsDRCumulativePricesBean cumulativePricesBean = new TeamsDRCumulativePricesBean();
			cumulativePricesBean.setMrc(cumulativePricesBean.getMrc() + TeamsDRUtils
					.checkForNull(attributes.getPrice().getEffectiveMrc()));
			cumulativePricesBean.setNrc(cumulativePricesBean.getNrc() + TeamsDRUtils
					.checkForNull(attributes.getPrice().getEffectiveNrc()));
			cumulativePrices.put(referenceId + CommonConstants.COMMA + referenceName, cumulativePricesBean);
		}
	}

	/**
	 * update quote teams license prices
	 *
	 * @param reference
	 * @param value
	 * @param contractPeriod
	 * @return
	 */
	private QuoteTeamsLicense updatePriceQuoteTeamsLicense(String reference, TeamsDRCumulativePricesBean value,
			Integer contractPeriod) {
		QuoteTeamsLicense quoteTeamsLicense = quoteTeamsLicenseRepository.getOne(Integer.parseInt(reference));
		quoteTeamsLicense.setMrc(setPrecision(value.getMrc(), 2));
		quoteTeamsLicense.setNrc(setPrecision(value.getNrc(), 2));
		quoteTeamsLicense.setArc(setPrecision(12 * value.getMrc(), 2));
		quoteTeamsLicense.setTcv(setPrecision((contractPeriod * value.getMrc()) + value.getNrc(), 2));
		return quoteTeamsLicenseRepository.save(quoteTeamsLicense);
	}

	/**
	 * Update quote prices
	 * @param quoteToLe
	 * @param quotePrice
	 * @param quotePriceBean
	 */
	private void updateQuotePrice(QuoteToLe quoteToLe,QuotePrice quotePrice, QuotePriceBean quotePriceBean) {
		if (Objects.nonNull(quotePriceBean)) {
			// to check for price change and audit.
			processQuotePriceAudit(quoteToLe,quotePrice,quotePriceBean);
			quotePrice.setMinimumMrc(quotePriceBean.getMinimumMrc());
			quotePrice.setEffectiveMrc(quotePriceBean.getEffectiveMrc());
			quotePrice.setMinimumNrc(quotePriceBean.getMinimumNrc());
			quotePrice.setEffectiveNrc(quotePriceBean.getEffectiveNrc());
			quotePrice.setEffectiveUsagePrice(quotePriceBean.getEffectiveUsagePrice());
			quotePriceRepository.save(quotePrice);
		}
	}

	/**
	 * Method to process quote price audit
	 * @param quoteToLe
	 * @param quotePrice
	 * @param quotePriceBean
	 */
	private void processQuotePriceAudit(QuoteToLe quoteToLe,QuotePrice quotePrice,QuotePriceBean quotePriceBean){

		// Checking if there is change in any price
		if (checkIfChangeInPriceExists(quotePrice,quotePriceBean)) {

			QuotePriceAudit quotePriceAudit = new QuotePriceAudit();
			quotePriceAudit.setCreatedBy(Utils.getSource());
			quotePriceAudit.setCreatedTime(new Timestamp(new Date().getTime()));

			if(quotePrice.getEffectiveArc() != null && quotePriceBean.getEffectiveArc() != null &&
					!quotePrice.getEffectiveArc().equals(quotePriceBean.getEffectiveArc())){
				quotePriceAudit.setFromArcPrice(quotePrice.getEffectiveArc());
				quotePriceAudit.setToArcPrice(quotePriceBean.getEffectiveArc());
			}

			if(quotePrice.getEffectiveMrc() != null && quotePriceBean.getEffectiveMrc() != null &&
					!quotePrice.getEffectiveMrc().equals(quotePriceBean.getEffectiveMrc())){
				quotePriceAudit.setFromMrcPrice(quotePrice.getEffectiveMrc());
				quotePriceAudit.setToMrcPrice(quotePriceBean.getEffectiveMrc());
			}

			if(quotePrice.getEffectiveNrc() != null && quotePriceBean.getEffectiveNrc() != null &&
					!quotePrice.getEffectiveNrc().equals(quotePriceBean.getEffectiveNrc())){
				quotePriceAudit.setFromNrcPrice(quotePrice.getEffectiveNrc());
				quotePriceAudit.setToNrcPrice(quotePriceBean.getEffectiveNrc());
			}

			if(quotePrice.getEffectiveUsagePrice() != null && quotePriceBean.getEffectiveUsagePrice() != null &&
					!quotePrice.getEffectiveUsagePrice().equals(quotePriceBean.getEffectiveUsagePrice())){
				quotePriceAudit.setFromEffectiveUsagePrice(quotePrice.getEffectiveUsagePrice());
				quotePriceAudit.setToEffectiveUsagePrice(quotePriceBean.getEffectiveUsagePrice());
			}

			quotePriceAudit.setQuotePrice(quotePrice);
			quotePriceAudit.setQuoteRefId(quoteToLe.getQuote().getQuoteCode());
			quotePriceAudit.setCurrencyCode(quoteToLe.getCurrencyCode());
			quotePriceAuditRepository.save(quotePriceAudit);
		}
	}

	/**
	 * Method to check if change in price exists
	 * @param quotePrice
	 * @param quotePriceBean
	 * @return
	 */
	private boolean checkIfChangeInPriceExists(QuotePrice quotePrice,QuotePriceBean quotePriceBean){
		return ((quotePrice.getEffectiveArc() != null && quotePriceBean.getEffectiveArc() != null &&
				!quotePrice.getEffectiveArc().equals(quotePriceBean.getEffectiveArc()))
				|| (quotePrice.getEffectiveMrc() != null && quotePriceBean.getEffectiveMrc() != null &&
				!quotePrice.getEffectiveMrc().equals(quotePriceBean.getEffectiveMrc()))
				|| (quotePrice.getEffectiveNrc() != null && quotePriceBean.getEffectiveNrc() != null &&
				!quotePrice.getEffectiveNrc().equals(quotePriceBean.getEffectiveNrc()))
				|| (quotePrice.getEffectiveUsagePrice() != null && quotePriceBean.getEffectiveUsagePrice() != null &&
				!quotePrice.getEffectiveUsagePrice().equals(quotePriceBean.getEffectiveUsagePrice())));
	}

	/**
	 * Set sub total prices for response
	 *
	 * @param manualPriceBean
	 * @param quoteTeamsDR
	 */
	private void setSubTotal(TeamsDRManualPriceBean manualPriceBean, QuoteTeamsDR quoteTeamsDR) {
		manualPriceBean.setSubtotalMrc(checkForNull(manualPriceBean.getSubtotalMrc()) + quoteTeamsDR.getMrc());
		manualPriceBean.setSubtotalNrc(checkForNull(manualPriceBean.getSubtotalNrc()) + quoteTeamsDR.getNrc());
	}

	/**
	 * Update media gateway with manual fp
	 *
	 * @param quoteToLe
	 * @param cumulativePrices
	 * @param mgCumulativePrices
	 */
	private void updateMgManualFP(Optional<QuoteToLe> quoteToLe,
			Map<String, TeamsDRCumulativePricesBean> cumulativePrices,
			Map<Integer, TeamsDRCumulativePricesBean> mgCumulativePrices) {
		cumulativePrices.forEach((key, value) -> {
			String[] references = key.split(CommonConstants.COMMA);
			QuoteDirectRoutingMediaGateways quoteDirectRoutingMg = quoteDirectRoutingMgRepository
					.getOne(Integer.parseInt(references[0]));
			quoteDirectRoutingMg.setMrc(setPrecision(value.getMrc(), 2));
			quoteDirectRoutingMg.setNrc(setPrecision(value.getNrc(), 2));
			quoteDirectRoutingMg.setArc(setPrecision(12 * value.getMrc(), 2));
			quoteDirectRoutingMg
					.setTcv(setPrecision((TeamsDRUtils.extractContractPeriodFromString(quoteToLe.get().getTermInMonths()) * value
							.getMrc()) + value.getNrc(), 2));
			quoteDirectRoutingMgRepository.save(quoteDirectRoutingMg);
			Integer quoteDrCityId = quoteDirectRoutingMg.getQuoteDirectRoutingCity().getId();
			if (mgCumulativePrices.containsKey(quoteDrCityId)) {
				TeamsDRCumulativePricesBean cumulative = cumulativePrices.get(quoteDrCityId);
				cumulative.setMrc(cumulative.getMrc() + TeamsDRUtils.checkForNull(quoteDirectRoutingMg.getMrc()));
				cumulative.setNrc(cumulative.getNrc() + TeamsDRUtils.checkForNull(quoteDirectRoutingMg.getNrc()));
				cumulative.setArc(cumulative.getArc() + TeamsDRUtils.checkForNull(quoteDirectRoutingMg.getArc()));
				cumulative.setTcv(cumulative.getTcv() + TeamsDRUtils.checkForNull(quoteDirectRoutingMg.getTcv()));
				mgCumulativePrices.put(quoteDrCityId, cumulative);
			} else {
				TeamsDRCumulativePricesBean cumulativePricesBean = new TeamsDRCumulativePricesBean();
				cumulativePricesBean.setMrc(cumulativePricesBean.getMrc() + TeamsDRUtils
						.checkForNull(quoteDirectRoutingMg.getMrc()));
				cumulativePricesBean.setNrc(cumulativePricesBean.getNrc() + TeamsDRUtils
						.checkForNull(quoteDirectRoutingMg.getNrc()));
				cumulativePricesBean.setArc(cumulativePricesBean.getArc() + TeamsDRUtils
						.checkForNull(quoteDirectRoutingMg.getArc()));
				cumulativePricesBean.setTcv(cumulativePricesBean.getTcv() + TeamsDRUtils
						.checkForNull(quoteDirectRoutingMg.getTcv()));
				mgCumulativePrices.put(quoteDrCityId, cumulativePricesBean);
			}
		});
	}

	/**
	 * Update dr city prices for manual fp
	 *
	 * @param quoteToLe
	 * @param cumulativePrices
	 * @param mgCumulativePrices
	 * @param drCumulativePrices
	 * @param quoteTeamsPrice
	 * @param manualPriceBean
	 */
	private void updateDRCityManualFp(QuoteToLe quoteToLe, Map<String, TeamsDRCumulativePricesBean> cumulativePrices,
			Map<Integer, TeamsDRCumulativePricesBean> mgCumulativePrices,
			Map<Integer, TeamsDRCumulativePricesBean> drCumulativePrices, QuoteTeamsDRManualPriceBean quoteTeamsPrice,
			TeamsDRManualPriceBean manualPriceBean) {
		List<QuoteDirectRoutingCity> quoteDirectRoutingCities = quoteDirectRoutingCityRepository
				.findAllById(mgCumulativePrices.keySet());
		List<Integer> quoteDrIds = new ArrayList<>();
		Integer contractPeriod = TeamsDRUtils.extractContractPeriodFromString(quoteToLe.getTermInMonths());
		quoteDirectRoutingCities.forEach(quoteDrCity -> {
			mgCumulativePrices.entrySet().stream().anyMatch(mgCumulative -> {
				if (mgCumulative.getKey().equals(quoteDrCity.getId())) {
					updateQuoteDrCityPrices(contractPeriod, quoteDrCity, mgCumulative.getValue().getMrc(),
							mgCumulative.getValue().getNrc());
					Integer quoteDrId = quoteDrCity.getQuoteDirectRouting().getId();
					if (drCumulativePrices.containsKey(quoteDrId)) {
						TeamsDRCumulativePricesBean cumulative = cumulativePrices.get(quoteDrId);
						cumulative.setMrc(cumulative.getMrc() + TeamsDRUtils.checkForNull(quoteDrCity.getMrc())
								.doubleValue());
						cumulative.setNrc(cumulative.getNrc() + TeamsDRUtils.checkForNull(quoteDrCity.getNrc())
								.doubleValue());
						cumulative.setArc(cumulative.getArc() + TeamsDRUtils.checkForNull(quoteDrCity.getArc())
								.doubleValue());
						cumulative.setTcv(cumulative.getTcv() + TeamsDRUtils.checkForNull(quoteDrCity.getTcv())
								.doubleValue());
						drCumulativePrices.put(quoteDrId, cumulative);
					} else {
						TeamsDRCumulativePricesBean cumulativePricesBean = new TeamsDRCumulativePricesBean();
						cumulativePricesBean
								.setMrc(cumulativePricesBean.getMrc() + TeamsDRUtils.checkForNull(quoteDrCity.getMrc())
										.doubleValue());
						cumulativePricesBean
								.setNrc(cumulativePricesBean.getNrc() + TeamsDRUtils.checkForNull(quoteDrCity.getNrc())
										.doubleValue());
						cumulativePricesBean
								.setArc(cumulativePricesBean.getArc() + TeamsDRUtils.checkForNull(quoteDrCity.getArc())
										.doubleValue());
						cumulativePricesBean
								.setTcv(cumulativePricesBean.getTcv() + TeamsDRUtils.checkForNull(quoteDrCity.getTcv())
										.doubleValue());
						drCumulativePrices.put(quoteDrId, cumulativePricesBean);
					}
					return true;
				} else
					return false;
			});
		});
		TeamsDRCumulativePricesBean cumulativePricesBean = new TeamsDRCumulativePricesBean();
		updateDRManualFP(drCumulativePrices, cumulativePricesBean, contractPeriod);

		updateQuoteToLePrices(quoteToLe, quoteTeamsPrice.getQuoteTeamsDRId(), manualPriceBean, contractPeriod,
				cumulativePricesBean.getMrc(), cumulativePricesBean.getNrc());
	}

	/**
	 * Update quote to le for manual fp
	 *
	 * @param quoteToLe
	 * @param quoteTeamsDrId
	 * @param manualPriceBean
	 * @param contractPeriod
	 * @param mrc
	 * @param nrc
	 */
	private void updateQuoteToLePrices(QuoteToLe quoteToLe, Integer quoteTeamsDrId,
			TeamsDRManualPriceBean manualPriceBean, Integer contractPeriod, Double mrc, Double nrc) {
		Optional<QuoteTeamsDR> quoteTeamsDRop = quoteTeamsDRRepository.findById(quoteTeamsDrId);
		if (quoteTeamsDRop.isPresent()) {
			QuoteTeamsDR quoteTeamsDR = quoteTeamsDRop.get();
			teamsDRQuoteService.subtractQuoteTeamsDRFromQuoteToLe(quoteToLe, quoteTeamsDR);
			quoteTeamsDR = updateQuoteTeamsDRPrices(quoteTeamsDR, mrc, nrc, contractPeriod);
			teamsDRQuoteService.updateQuoteToLeWithQuoteTeamsDR(quoteToLe, quoteTeamsDR);
			setSubTotal(manualPriceBean, quoteTeamsDR);
		}
	}

	/**
	 * Update quote dr city prices
	 *
	 * @param quoteDrCity
	 * @param mrc
	 * @param nrc
	 * @param arc
	 * @param tcv
	 */
	private void updateQuoteDrCityPrices(QuoteDirectRoutingCity quoteDrCity, Double mrc, Double nrc, Double arc,
			Double tcv) {
		quoteDrCity.setMrc(BigDecimal.valueOf(mrc));
		quoteDrCity.setNrc(BigDecimal.valueOf(nrc));
		quoteDrCity.setArc(BigDecimal.valueOf(arc));
		quoteDrCity.setTcv(BigDecimal.valueOf(tcv));
		quoteDirectRoutingCityRepository.save(quoteDrCity);
	}

	/**
	 * Update quote dr city prices
	 *
	 * @param contractPeriod
	 * @param quoteDrCity
	 * @param mrc
	 * @param nrc
	 */
	private void updateQuoteDrCityPrices(Integer contractPeriod, QuoteDirectRoutingCity quoteDrCity, Double mrc,
			Double nrc) {
		quoteDrCity.setMrc(BigDecimal.valueOf(mrc));
		quoteDrCity.setNrc(BigDecimal.valueOf(nrc));
		quoteDrCity.setArc(BigDecimal.valueOf(12 * mrc));
		quoteDrCity.setTcv(BigDecimal.valueOf((contractPeriod * mrc) + nrc));
		quoteDirectRoutingCityRepository.save(quoteDrCity);
	}

	/**
	 * Update quote dr with manual prices
	 *
	 * @param drCumulativePrices
	 * @param cumulativePricesBean
	 * @param contractPeriod
	 */
	private void updateDRManualFP(Map<Integer, TeamsDRCumulativePricesBean> drCumulativePrices,
			TeamsDRCumulativePricesBean cumulativePricesBean, Integer contractPeriod) {
		List<QuoteDirectRouting> quoteDirectRoutings = quoteDirectRoutingRepository
				.findAllById(drCumulativePrices.keySet());
		quoteDirectRoutings.forEach(quoteDr -> {
			drCumulativePrices.entrySet().stream().anyMatch(drCumulative -> {
				if (drCumulative.getKey().equals(quoteDr.getId())) {
					quoteDr.setMrc(BigDecimal.valueOf(drCumulative.getValue().getMrc()));
					quoteDr.setNrc(BigDecimal.valueOf(drCumulative.getValue().getNrc()));
					quoteDr.setArc(BigDecimal.valueOf(12 * quoteDr.getMrc().doubleValue()));
					quoteDr.setTcv(BigDecimal.valueOf(
							(contractPeriod * quoteDr.getMrc().doubleValue()) + quoteDr.getNrc().doubleValue()));
					quoteDirectRoutingRepository.save(quoteDr);
					cumulativePricesBean
							.setMrc(cumulativePricesBean.getMrc() + TeamsDRUtils.checkForNull(quoteDr.getMrc())
									.doubleValue());
					cumulativePricesBean
							.setNrc(cumulativePricesBean.getNrc() + TeamsDRUtils.checkForNull(quoteDr.getNrc())
									.doubleValue());
					cumulativePricesBean
							.setArc(cumulativePricesBean.getArc() + TeamsDRUtils.checkForNull(quoteDr.getArc())
									.doubleValue());
					cumulativePricesBean
							.setTcv(cumulativePricesBean.getTcv() + TeamsDRUtils.checkForNull(quoteDr.getTcv())
									.doubleValue());
					return true;
				} else
					return false;
			});
		});
	}

	/**
	 * Update quote teams dr prices
	 *
	 * @param quoteTeamsDR
	 * @param mrc
	 * @param nrc
	 * @param arc
	 * @param tcv
	 * @return
	 */
	private QuoteTeamsDR updateQuoteTeamsDRPrices(QuoteTeamsDR quoteTeamsDR, Double mrc, Double nrc, Double arc,
			Double tcv) {
		quoteTeamsDR.setMrc(setPrecision(mrc, 2));
		quoteTeamsDR.setNrc(setPrecision(nrc, 2));
		quoteTeamsDR.setArc(setPrecision(arc, 2));
		quoteTeamsDR.setTcv(setPrecision(tcv, 2));
		return quoteTeamsDRRepository.save(quoteTeamsDR);
	}

	/**
	 * Update quote teams dr prices
	 *
	 * @param quoteTeamsDR
	 * @param mrc
	 * @param nrc
	 * @param contractPeriod
	 * @return
	 */
	private QuoteTeamsDR updateQuoteTeamsDRPrices(QuoteTeamsDR quoteTeamsDR, Double mrc, Double nrc,
			Integer contractPeriod) {
		quoteTeamsDR.setMrc(setPrecision(mrc, 2));
		quoteTeamsDR.setNrc(setPrecision(nrc, 2));
		quoteTeamsDR.setArc(setPrecision(12 * mrc, 2));
		quoteTeamsDR.setTcv(setPrecision((contractPeriod * mrc) + nrc, 2));
		return quoteTeamsDRRepository.save(quoteTeamsDR);
	}
}