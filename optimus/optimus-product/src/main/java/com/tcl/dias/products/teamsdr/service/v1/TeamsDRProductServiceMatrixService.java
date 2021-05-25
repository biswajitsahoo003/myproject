package com.tcl.dias.products.teamsdr.service.v1;

import static com.tcl.dias.products.teamsdr.constants.TeamsDRProductConstants.MEDIANT_800B_TEMPLATE;
import static com.tcl.dias.products.teamsdr.constants.TeamsDRProductConstants.MEDIANT_800C_TEMPLATE;
import static com.tcl.dias.products.teamsdr.constants.TeamsDRProductConstants.MEDIA_GATEWAY;
import static com.tcl.dias.products.teamsdr.constants.TeamsDRProductConstants.MS_LICENSE;
import static com.tcl.dias.products.teamsdr.constants.TeamsDRProductConstants.PLAN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.teamsdr.beans.SubComponentHSNCodeBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRHSNCodeBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.productcatelog.entity.entities.TeamsDROfferingHSNCode;
import com.tcl.dias.productcatelog.entity.entities.TeamsDRRateCardHSNCode;
import com.tcl.dias.productcatelog.entity.repository.TeamsDROfferingHSNCodeRepository;
import com.tcl.dias.productcatelog.entity.repository.TeamsDRRateCardHSNCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.teamsdr.beans.ServiceLevelChargesBean;
import com.tcl.dias.common.teamsdr.beans.ServiceLevelChargesWrapper;
import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseAgreementType;
import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseBasedOnProvider;
import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseRequestBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.productcatelog.entity.entities.TeamsDRLicensePrices;
import com.tcl.dias.productcatelog.entity.entities.TeamsDRPRICalculations;
import com.tcl.dias.productcatelog.entity.entities.TeamsDRSIPDetails;
import com.tcl.dias.productcatelog.entity.entities.TeamsDRServiceChargeView;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixTeamsDRCityRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixTeamsDRRepository;
import com.tcl.dias.productcatelog.entity.repository.TeamsDRLicenseCountryRepository;
import com.tcl.dias.productcatelog.entity.repository.TeamsDRLicensePricesRepository;
import com.tcl.dias.productcatelog.entity.repository.TeamsDRPRICalculationsRepository;
import com.tcl.dias.productcatelog.entity.repository.TeamsDRSIPDetailsRepository;
import com.tcl.dias.productcatelog.entity.repository.TeamsDRServiceChargeViewRepository;
import com.tcl.dias.productcatelog.entity.repository.TeamsDrTDMViewRepository;
import com.tcl.dias.products.teamsdr.beans.TeamsDRCityDetailBean;
import com.tcl.dias.products.teamsdr.beans.TeamsDRCountryDetailBean;
import com.tcl.dias.products.teamsdr.beans.TeamsDRLicenseInfoBean;
import com.tcl.dias.products.teamsdr.beans.TeamsDRLicenseInfoProviderBean;
import com.tcl.dias.products.teamsdr.beans.TeamsDRMediaGatewayBean;
import com.tcl.dias.products.teamsdr.constants.TeamsDRProductConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service class for Teams DR product microservice
 *
 * @author Srinivasa Raghavan
 */
@Service
public class TeamsDRProductServiceMatrixService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TeamsDRProductServiceMatrixService.class);

	@Autowired
	ServiceAreaMatrixTeamsDRRepository serviceAreaMatrixTeamsdrRepository;

	@Autowired
	ServiceAreaMatrixTeamsDRCityRepository serviceAreaMatrixTeamsDRCityRepository;

	@Autowired
	TeamsDRLicensePricesRepository teamsDRLicensePricesRepository;

	@Autowired
	TeamsDRLicenseCountryRepository teamsDRLicenseCountryRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customer.sple.details.queue}")
	String serviceProviderQueue;

	@Autowired
	TeamsDRPRICalculationsRepository teamsDRPRICalculationsRepository;

	@Autowired
	TeamsDRSIPDetailsRepository teamsDRSIPDetailsRepository;

	@Autowired
	TeamsDRServiceChargeViewRepository teamsDRServiceChargeViewRepository;

	@Autowired
	TeamsDrTDMViewRepository teamsDrTDMViewRepository;

	@Autowired
	TeamsDROfferingHSNCodeRepository teamsDROfferingHSNCodeRepository;

	@Autowired
	TeamsDRRateCardHSNCodeRepository teamsDRRateCardHSNCodeRepository;

	/**
	 * Method to get all countries
	 *
	 * @return
	 */
	public List<TeamsDRCountryDetailBean> getCountries(Boolean isTataVoiceAvailable) throws TclCommonException {
		Set<Map<String, Object>> countries = serviceAreaMatrixTeamsdrRepository.findByCountry();
		String response = (String) mqUtils.sendAndReceive(serviceProviderQueue, null);
		Map<String, List<String>> countrySpDetails = null;
		if(Objects.nonNull(response) && !response.isEmpty()){
			LOGGER.info("Response From Customer micro service :: {}",response);
			countrySpDetails = Utils.convertJsonStingToJson(response);
			LOGGER.info("Countries with tata le :: {}",countrySpDetails.keySet());
		}
		List<TeamsDRCountryDetailBean> countryDetailBeans = new ArrayList<>();
		Map<String, List<String>> finalCountrySpDetails = countrySpDetails;
		if(Objects.nonNull(isTataVoiceAvailable) && isTataVoiceAvailable){
			countries.forEach(country -> {
				TeamsDRCountryDetailBean teamsDRCountryDetailBean = createTeamsdrCountryDetailBeanForTataVoice(country);
				updateTataLeStatus(finalCountrySpDetails,teamsDRCountryDetailBean);
				countryDetailBeans.add(teamsDRCountryDetailBean);
			});
		}else{
			countries.forEach(country -> {
				TeamsDRCountryDetailBean teamsDRCountryDetailBean = createTeamsdrCountryDetailBean(country);
				updateTataLeStatus(finalCountrySpDetails,teamsDRCountryDetailBean);
				countryDetailBeans.add(teamsDRCountryDetailBean);
			});
		}
		return countryDetailBeans;
	}

	/**
	 * Method to update le status.
	 * @param finalCountrySpDetails
	 * @param teamsDRCountryDetailBean
	 */
	private void updateTataLeStatus(Map<String,List<String>> finalCountrySpDetails,TeamsDRCountryDetailBean teamsDRCountryDetailBean){
		teamsDRCountryDetailBean.setIsTataLeAvailable(false);
		if(Objects.nonNull(finalCountrySpDetails) && finalCountrySpDetails.containsKey(teamsDRCountryDetailBean.getName().toLowerCase())){
			if(!finalCountrySpDetails.get(teamsDRCountryDetailBean.getName().toLowerCase()).isEmpty()){
				teamsDRCountryDetailBean.setIsTataLeAvailable(true);
			}
		}
	}

	/**
	 * create createWebexProductLocationDetailBean
	 *
	 * @param serviceAreaMatrixTeamsDRView
	 * @return {@link TeamsDRCountryDetailBean}
	 */
	private TeamsDRCountryDetailBean createTeamsdrCountryDetailBean(
			Map<String, Object> serviceAreaMatrixTeamsDRView) {
		return new TeamsDRCountryDetailBean(
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.COUNTRY)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.CODE)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.ISD_CODE)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.IS_REGULATED)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.IS_EXCEPTION)));
	}

	/**
	 * create createTeamsdrCountryDetailBeanForTataVoice
	 *
	 * @param serviceAreaMatrixTeamsDRView
	 * @return {@link TeamsDRCountryDetailBean}
	 */
	private TeamsDRCountryDetailBean createTeamsdrCountryDetailBeanForTataVoice(
			Map<String, Object> serviceAreaMatrixTeamsDRView) {
		return new TeamsDRCountryDetailBean(
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.COUNTRY)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.CODE)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.ISD_CODE)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.IS_GSC)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.IS_REGULATED)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.IS_EXCEPTION)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.IS_DV)),
				String.valueOf(serviceAreaMatrixTeamsDRView.get(TeamsDRProductConstants.IS_GOB)));
	}

	/**
	 * To get all cities based on given country
	 *
	 * @param country
	 * @return
	 */
	public List<TeamsDRCityDetailBean> getAllCitiesBasedOncountry(String country) {
		Set<Map<String, Object>> cities = serviceAreaMatrixTeamsDRCityRepository.findByCountry(country);
		List<TeamsDRCityDetailBean> cityDetailBeans = new ArrayList<>();
		cities.forEach(city -> {
			TeamsDRCityDetailBean teamsDRCityDetailBean = new TeamsDRCityDetailBean(
					String.valueOf(city.get(TeamsDRProductConstants.CODE)),
					String.valueOf(city.get(TeamsDRProductConstants.COUNTRY)),
					String.valueOf(city.get(TeamsDRProductConstants.CITY)));
			cityDetailBeans.add(teamsDRCityDetailBean);
		});
		return cityDetailBeans;
	}

	/**
	 * Get license agreement type
	 *
	 * @return
	 */
	public Set<String> getLicenseAgreementTypes() {
		return teamsDRLicensePricesRepository.findDistinctAgreementType();
	}

	/**
	 * Method to fetch licenses based on the agreement type and countries
	 * 
	 * @param requestBean
	 * @return
	 */
	public TeamsDRLicenseAgreementType getLicenseDetailsBasedOnCountries(TeamsDRLicenseRequestBean requestBean) {
		List<Map<String, String>> licensesAndProviders = teamsDRLicenseCountryRepository.findByCountriesAndAgreement(
				requestBean.getCountries(), requestBean.getCountries().size(), requestBean.getAgreementType());
		TeamsDRLicenseAgreementType teamsDRLicenseAgreementType = new TeamsDRLicenseAgreementType();
		teamsDRLicenseAgreementType.setAgreementType(requestBean.getAgreementType());
		teamsDRLicenseAgreementType.setTeamsDRLicenseBasedOnProviders(getLicenses(licensesAndProviders));
		return teamsDRLicenseAgreementType;
	}

	/**
	 * Get licenses organized by providers, codes and agreement type in ascending
	 * order
	 *
	 * @param licenseAndProviders
	 */
	private List<TeamsDRLicenseBasedOnProvider> getLicenses(List<Map<String, String>> licenseAndProviders) {
		Map<String, TeamsDRLicenseBasedOnProvider> teamsDRLicenseMap = new HashMap<>();
		List<TeamsDRLicenseBasedOnProvider> teamsDRLicenseBasedOnProviders = new ArrayList<>();
		licenseAndProviders.forEach(license -> {
			if (teamsDRLicenseMap.containsKey(license.get(TeamsDRProductConstants.PROVIDER))) {
				teamsDRLicenseMap.get(license.get(TeamsDRProductConstants.PROVIDER)).getLicenses()
						.add(license.get(TeamsDRProductConstants.LICENSE));
			} else {
				TeamsDRLicenseBasedOnProvider teamsDRLicenseBasedOnProvider = new TeamsDRLicenseBasedOnProvider();
				teamsDRLicenseBasedOnProvider.setProvider(license.get(TeamsDRProductConstants.PROVIDER));
				teamsDRLicenseBasedOnProvider.setLicenses(new ArrayList<>());
				teamsDRLicenseBasedOnProvider.getLicenses().add(license.get(TeamsDRProductConstants.LICENSE));
				teamsDRLicenseBasedOnProviders.add(teamsDRLicenseBasedOnProvider);
				teamsDRLicenseMap.put(license.get(TeamsDRProductConstants.PROVIDER), teamsDRLicenseBasedOnProvider);
			}
		});
		teamsDRLicenseBasedOnProviders.forEach(
				teamsDRLicenseBasedOnProvider -> Collections.sort(teamsDRLicenseBasedOnProvider.getLicenses()));
		return teamsDRLicenseBasedOnProviders;
	}

	/**
	 * Fetching license information (description, min/max seats) for tooltips,
	 * validation
	 *
	 * @return
	 */
	public List<TeamsDRLicenseInfoProviderBean> getLicenseInfo() {
		List<TeamsDRLicensePrices> licenseDetails = teamsDRLicensePricesRepository.findAll();
		Map<String, TeamsDRLicenseInfoProviderBean> tempMap = new HashMap<>();
		licenseDetails.forEach(licenseDetail -> {
			if (tempMap.containsKey(licenseDetail.getVendor())) {
				TeamsDRLicenseInfoBean teamsDRLicenseInfo = new TeamsDRLicenseInfoBean();
				teamsDRLicenseInfo.setLicenseName(licenseDetail.getDispNm());
				teamsDRLicenseInfo.setDescription(licenseDetail.getLongDesc());
				teamsDRLicenseInfo.setCategory(licenseDetail.getNm());
				teamsDRLicenseInfo.setMinSeats(licenseDetail.getMinSeat());
				teamsDRLicenseInfo.setMaxSeats(licenseDetail.getMaxSeat());
				teamsDRLicenseInfo.setOfferId(licenseDetail.getOfferId());
				teamsDRLicenseInfo.setSfdcProductName(licenseDetail.getSfdcProductName());
				tempMap.get(licenseDetail.getVendor()).getTeamsDRLicenseInfos().add(teamsDRLicenseInfo);

			} else {
				TeamsDRLicenseInfoProviderBean teamsDRLicenseInfoProvider = new TeamsDRLicenseInfoProviderBean();
				teamsDRLicenseInfoProvider.setProvider(licenseDetail.getVendor());
				teamsDRLicenseInfoProvider.setTeamsDRLicenseInfos(new ArrayList<>());
				tempMap.put(licenseDetail.getVendor(), teamsDRLicenseInfoProvider);

				TeamsDRLicenseInfoBean teamsDRLicenseInfo = new TeamsDRLicenseInfoBean();
				teamsDRLicenseInfo.setLicenseName(licenseDetail.getDispNm());
				teamsDRLicenseInfo.setDescription(licenseDetail.getLongDesc());
				teamsDRLicenseInfo.setCategory(licenseDetail.getNm());
				teamsDRLicenseInfo.setMinSeats(licenseDetail.getMinSeat());
				teamsDRLicenseInfo.setMaxSeats(licenseDetail.getMaxSeat());
				teamsDRLicenseInfo.setOfferId(licenseDetail.getOfferId());
				teamsDRLicenseInfo.setSfdcProductName(licenseDetail.getSfdcProductName());
				tempMap.get(licenseDetail.getVendor()).getTeamsDRLicenseInfos().add(teamsDRLicenseInfo);
			}
		});

		List<TeamsDRLicenseInfoProviderBean> teamsDRLicenseInfoProviderBeans = new ArrayList<TeamsDRLicenseInfoProviderBean>(
				tempMap.values());
		teamsDRLicenseInfoProviderBeans.forEach(
				teamsDRLicenseInfoProviderBean -> teamsDRLicenseInfoProviderBean.getTeamsDRLicenseInfos().sort((license,
						license1) -> license.getLicenseName().compareToIgnoreCase(license1.getLicenseName())));
		return teamsDRLicenseInfoProviderBeans;
	}


	/**
	 * Method to create custom TeamsDRMediaGatewayBean
	 * @param value
	 * @param vendorName
	 * @param mgName
	 * @return
	 */
	private TeamsDRMediaGatewayBean createCustomTeamsDRMediaGatewayBean(Integer value, String vendorName, String mgName,
			Integer quantity){
		TeamsDRMediaGatewayBean teamsDRMediaGatewayBean = new TeamsDRMediaGatewayBean();
		String mediagateway = String.format(mgName, value);
		teamsDRMediaGatewayBean.setVendorName(vendorName);
		teamsDRMediaGatewayBean.setMediaGatewayName(mediagateway);
		teamsDRMediaGatewayBean.setQuantity(quantity);
		teamsDRMediaGatewayBean.setMediaGatewayWithQuantity(String.format(mgName, value)
				+ CommonConstants.SPACE + String.format("(%d)",quantity));
		return teamsDRMediaGatewayBean;
	}

	/**
	 * Method to get mediagateways based on type..
	 * @param type
	 * @param value
	 * @param isRedundant
	 */
	public List<TeamsDRMediaGatewayBean> getMediaGateways(String type, Integer value, String isRedundant) {

		List<TeamsDRMediaGatewayBean> teamsDRMediaGatewayBeans = new ArrayList<>();
		if (TeamsDRProductConstants.TDM.equals(type)) {
			// For Tdm
			if (value > 5) {
				Integer quotient = value / 4;
				Integer remainder = value % 4;

				teamsDRMediaGatewayBeans.add(createCustomTeamsDRMediaGatewayBean(4, TeamsDRProductConstants.AUDIO_CODES,
						MEDIANT_800C_TEMPLATE, quotient));
				if (remainder > 0) {
					if (remainder == 1) {
						teamsDRMediaGatewayBeans
								.add(createCustomTeamsDRMediaGatewayBean(remainder, TeamsDRProductConstants.AUDIO_CODES,
										MEDIANT_800B_TEMPLATE, 1));
					} else {
						teamsDRMediaGatewayBeans
								.add(createCustomTeamsDRMediaGatewayBean(remainder, TeamsDRProductConstants.AUDIO_CODES,
										MEDIANT_800C_TEMPLATE, 1));
					}
				}

			} else {
				Optional<TeamsDRPRICalculations> teamsDRPRICalculations = teamsDRPRICalculationsRepository
						.findByPriValue(value);
				if (teamsDRPRICalculations.isPresent()) {
					String mediaGateway;
					if (CommonConstants.YES.equalsIgnoreCase(isRedundant)) {
						mediaGateway = teamsDRPRICalculations.get().getMediaGatewayWithRedundancy();
					} else {
						mediaGateway = teamsDRPRICalculations.get().getMediaGatewayWithoutRedundancy();
					}
					String vendorName = teamsDRPRICalculations.get().getVendorName();
					List<String> mediaGateways = Arrays.asList(mediaGateway.split("\\+"));

					mediaGateways.forEach(mg -> {
						LOGGER.info("Media Gateway :: {}", mg);
						TeamsDRMediaGatewayBean teamsDRMediaGatewayBean = new TeamsDRMediaGatewayBean();
						String substr = mg.substring(mg.indexOf("(") + 1, mg.indexOf(")"));
						Integer quantity = Integer.valueOf(substr);
						teamsDRMediaGatewayBean.setQuantity(quantity);
						teamsDRMediaGatewayBean.setVendorName(vendorName);
						teamsDRMediaGatewayBean.setMediaGatewayName(mg.replaceAll(" *\\(.+?\\)", "").trim());
						teamsDRMediaGatewayBean.setMediaGatewayWithQuantity(mg.trim());
						teamsDRMediaGatewayBeans.add(teamsDRMediaGatewayBean);
					});
				}
			}
		} else {
			// For SIP
			value = value > 250 ? 400 : 250;
			Optional<TeamsDRSIPDetails> teamsDRSIPDetails = teamsDRSIPDetailsRepository.
					findByMaxSessionValueAndIsRedundant(value,
							CommonConstants.YES.equalsIgnoreCase(isRedundant) ? CommonConstants.Y : CommonConstants.N);
			if (teamsDRSIPDetails.isPresent()) {
				TeamsDRMediaGatewayBean teamsDRMediaGatewayBean = new TeamsDRMediaGatewayBean();
				teamsDRMediaGatewayBean.setVendorName(teamsDRSIPDetails.get().getVendorName());
				teamsDRMediaGatewayBean.setMediaGatewayName(teamsDRSIPDetails.get().getMediaGatewayNm().trim());
				teamsDRMediaGatewayBean
						.setMediaGatewayWithQuantity(teamsDRSIPDetails.get().getMediaGatewayNm().trim() + " (1)");
				teamsDRMediaGatewayBean.setQuantity(1);
				teamsDRMediaGatewayBeans.add(teamsDRMediaGatewayBean);
			}
		}

		return teamsDRMediaGatewayBeans;
	}

	/**
	 * To fetch all service level charges from catalog
	 *
	 * @return
	 */
	public ServiceLevelChargesWrapper getServiceLevelCharges() {
		List<TeamsDRServiceChargeView> teamsDRServiceChargeViews = teamsDRServiceChargeViewRepository.findAll();
		List<ServiceLevelChargesBean> serviceLevelCharges = new ArrayList<>();
		teamsDRServiceChargeViews.forEach(serviceLevelCharge -> {
			ServiceLevelChargesBean serviceLevelChargesBean = new ServiceLevelChargesBean();
			serviceLevelChargesBean.setComponentVariant(serviceLevelCharge.getComponentVarient());
			serviceLevelChargesBean.setComponentSubVariant(serviceLevelCharge.getComponentSubVarient());
			serviceLevelChargesBean.setChargeType(serviceLevelCharge.getChargeType());
			serviceLevelChargesBean.setChargeUom(serviceLevelCharge.getChargeCostUom());
			serviceLevelChargesBean.setCurrencyCode(serviceLevelCharge.getChargeCurrency());
			serviceLevelChargesBean.setPrice(serviceLevelCharge.getChargeCost());
			serviceLevelCharges.add(serviceLevelChargesBean);
		});
		ServiceLevelChargesWrapper serviceLevelChargesWrapper = new ServiceLevelChargesWrapper();
		serviceLevelChargesWrapper.setServiceLevelCharges(serviceLevelCharges);
		return serviceLevelChargesWrapper;
	}

	/**
	 * To fetch media gateway device vendor name
	 *
	 * @param req
	 * @return
	 */
	public String getVendorName(Map<String, String> req) {
		String vendorName;
		if (TeamsDRProductConstants.TDM.equalsIgnoreCase(req.get(CommonConstants.TYPE)))
			vendorName = teamsDrTDMViewRepository.findByMediaGatewayNm(req.get(CommonConstants.NAME)).getVendorName();
		else
			vendorName = teamsDRSIPDetailsRepository.findByMediaGatewayNm(req.get(CommonConstants.NAME))
					.getVendorName();

		return vendorName;
	}

	/**
	 * Method to find component name.
	 * 
	 * @param offeringHSNCode
	 * @return
	 */
	private String findComponentName(TeamsDROfferingHSNCode offeringHSNCode) {
		String bundledOfferingName = offeringHSNCode.getBundledOfferningNm();
		LOGGER.info("Bundled Offering Name :: {}", bundledOfferingName);
		String componentName = null;
		if (Objects.nonNull(bundledOfferingName)) {
			if (bundledOfferingName.contains(PLAN)) {
				if (Objects.isNull(offeringHSNCode.getAtomicOfferingNm())
						|| "NULL".equals(offeringHSNCode.getAtomicOfferingNm())) {
					return bundledOfferingName;
				} else
					return offeringHSNCode.getAtomicOfferingNm();
			} else if (MS_LICENSE.equals(bundledOfferingName)) {
				return offeringHSNCode.getChargeLineItem();
			} else if (MEDIA_GATEWAY.equals(bundledOfferingName)) {
				if (Objects.nonNull(offeringHSNCode.getVendorNm())) {
					return offeringHSNCode.getVendorNm();
				} else
					return bundledOfferingName;
			}
		}
		return componentName;
	}

	/**
	 * Method to get offering hsn codes
	 * 
	 * @param offeringHSNCodes
	 * @param components
	 */
	private void getOfferingHSNCodes(List<TeamsDROfferingHSNCode> offeringHSNCodes,
			Map<String, SubComponentHSNCodeBean> components) {
		offeringHSNCodes.stream().filter(Objects::nonNull).forEach(offeringHsn -> {
			LOGGER.info("Hsn code  :: {}", offeringHsn.toString());
			if (components.containsKey(offeringHsn.getBundledOfferningNm())) {
				SubComponentHSNCodeBean subComponentHSNCodeBean = components.get(offeringHsn.getBundledOfferningNm());
				Map<String, Map<String, String>> subComponents = subComponentHSNCodeBean.getSubComponents();
				String atomicOfferingName = findComponentName(offeringHsn);
				if (subComponents.containsKey(atomicOfferingName)) {
					Map<String, String> subComponent = subComponents.get(atomicOfferingName);
					if (!subComponent.containsKey(offeringHsn.getChargeNm())) {
						subComponent.put(offeringHsn.getChargeNm(), offeringHsn.getHsnCode());
					}
				} else {
					Map<String, String> subComponent = new HashMap<>();
					subComponent.put(offeringHsn.getChargeNm(), offeringHsn.getHsnCode());
					subComponents.put(atomicOfferingName, subComponent);
				}
			} else {
				SubComponentHSNCodeBean subComponentHSNCodeBean = new SubComponentHSNCodeBean();
				Map<String, Map<String, String>> subComponents = new HashMap<>();
				subComponentHSNCodeBean.setSubComponents(subComponents);
				components.put(offeringHsn.getBundledOfferningNm(), subComponentHSNCodeBean);
				String atomicOfferingName = findComponentName(offeringHsn);
				LOGGER.info("Atomic Offering Name :: {}", atomicOfferingName);
				Map<String, String> subComponent = new HashMap<>();
				subComponent.put(offeringHsn.getChargeNm(), offeringHsn.getHsnCode());
				subComponents.put(atomicOfferingName, subComponent);
			}
		});
	}

	/**
	 * Method to get rate card hsn codes.
	 * 
	 * @param rateCardHSNCodes
	 * @param components
	 */
	public void getRateCardHSNCodes(List<TeamsDRRateCardHSNCode> rateCardHSNCodes,
			Map<String, SubComponentHSNCodeBean> components) {
		rateCardHSNCodes.stream().filter(Objects::nonNull).forEach(rateCardHsn -> {
			LOGGER.info("Hsn code  :: {}", rateCardHsn.toString());
			if (components.containsKey(rateCardHsn.getComponentVarient())) {
				SubComponentHSNCodeBean subComponentHSNCodeBean = components.get(rateCardHsn.getComponentVarient());
				Map<String, Map<String, String>> subComponents = subComponentHSNCodeBean.getSubComponents();
				String atomicOfferingName = rateCardHsn.getComponentSubVarient();
				if (subComponents.containsKey(atomicOfferingName)) {
					Map<String, String> subComponent = subComponents.get(atomicOfferingName);
					if (!subComponent.containsKey(rateCardHsn.getChargeNm())) {
						subComponent.put(rateCardHsn.getChargeNm(), rateCardHsn.getHsnCode());
					}
				} else {
					Map<String, String> subComponent = new HashMap<>();
					subComponent.put(rateCardHsn.getChargeNm(), rateCardHsn.getHsnCode());
					subComponents.put(atomicOfferingName, subComponent);
				}
			} else {
				SubComponentHSNCodeBean subComponentHSNCodeBean = new SubComponentHSNCodeBean();
				Map<String, Map<String, String>> subComponents = new HashMap<>();
				subComponentHSNCodeBean.setSubComponents(subComponents);
				components.put(rateCardHsn.getComponentVarient(), subComponentHSNCodeBean);
				String atomicOfferingName = rateCardHsn.getComponentSubVarient();
				LOGGER.info("Atomic Offering Name :: {}", atomicOfferingName);
				Map<String, String> subComponent = new HashMap<>();
				subComponent.put(rateCardHsn.getChargeNm(), rateCardHsn.getHsnCode());
				subComponents.put(atomicOfferingName, subComponent);
			}
		});
	}

	/**
	 * Method to fetch all the hsn Codes
	 * 
	 * @return
	 */
	public TeamsDRHSNCodeBean getHSNCodes() throws TclCommonException {
		TeamsDRHSNCodeBean teamsDRHSNCodeBean = new TeamsDRHSNCodeBean();
		Map<String, SubComponentHSNCodeBean> components = new HashMap<>();
		teamsDRHSNCodeBean.setComponents(components);
		Set<Map<String, String>> hsnCodes = teamsDROfferingHSNCodeRepository.findAllHsnCodes();
		String hsnCodesString = Utils.convertObjectToJson(hsnCodes);
		List<TeamsDROfferingHSNCode> offeringHSNCodes = Utils.fromJson(hsnCodesString,
				new TypeReference<List<TeamsDROfferingHSNCode>>() {
				});
		List<TeamsDRRateCardHSNCode> rateCardHSNCodes = teamsDRRateCardHSNCodeRepository.findAll();
		getOfferingHSNCodes(offeringHSNCodes, components);
		getRateCardHSNCodes(rateCardHSNCodes, components);
		return teamsDRHSNCodeBean;
	}
}