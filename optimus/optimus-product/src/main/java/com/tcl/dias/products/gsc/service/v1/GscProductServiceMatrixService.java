package com.tcl.dias.products.gsc.service.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.gsc.beans.GscSlaBean;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.productcatelog.entity.entities.GscSlaView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCACANSView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCACDTFSView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCDVView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCGlobalOutBndView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCITFSView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCLNSView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGSCUIFNView;
import com.tcl.dias.productcatelog.entity.repository.GscSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCACANSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCACDTFSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCDVViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCGlobalOutBndViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCITFSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCLNSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCPSTNViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCUIFNViewRepository;
import com.tcl.dias.products.gsc.beans.GscProductLocationBean;
import com.tcl.dias.products.gsc.beans.GscProductLocationDetailBean;
import com.tcl.dias.products.gsc.beans.GscServiceMatrixAttributeBean;
import com.tcl.dias.products.gsc.beans.GscServiceMatrixBean;
import com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.products.constants.ExceptionConstants.COMMON_ERROR;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.ACANS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.ACANS_PSTN_SLT;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.ACDTFS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.ACLNS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.BEST_EFFORT;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.BETWEEN_INTERNATIONAL_CARREIER_YES;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.COMMA;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.COMMENTS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.COUNTRY_NAME_NULL_MESSAGE;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.DEDICATED;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.DESTINATION_NAME;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.DIALING_FORMAT_TEXT;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.DOMESTIC_VOICE;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.ESTIMATED_STANDARD_LEAD_TIME_DAYS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.FIXED_NETWORK_LIMITATION;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.FROM_PAYPHONE;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.GLOBAL_OUTBOUND;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.GLOBAL_OUTBOUND_PSTN_SLT;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.HYPHEN;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.INDIA_COUNTRY_CODE;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.INTERNAL_COMMENTS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.ITFS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.LNS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.MOBILE_NETWORK_LIMITATION;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.MPLS;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.MPLS_SLT;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.NEW_NUMBER_AVAILABLE_INDICATOR;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.NNI;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.NUMBER_SIMULTANEOUS_CALLS_PER_NUMBER;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.ONHOLD_INDICATOR;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.PHONE_TYPE;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.PORTABILITY;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.PRODUCT_NAME_NULL_MESSAGE;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.PROV_TESTING_SLT;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.PSTN;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.PUBLIC_IP;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.REGION;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.SERVICE_LEVEL;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.THIRD_COUNTRY_CALLING;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.UIFN;
import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.YES;

/**
 * Service matrix for GSC products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscProductServiceMatrixService {

	private static final String NAME = "name";
	private static final String CODE = "code";
	private static final String ISD_CODE = "isdCode";
	private static final Logger LOGGER = LoggerFactory.getLogger(GscProductServiceMatrixService.class);

	@Autowired
	ServiceAreaMatrixGSCACANSViewRepository serviceAreaMatrixGSCACANSViewRepository;
	@Autowired
	ServiceAreaMatrixGSCACDTFSViewRepository serviceAreaMatrixGSCACDTFSViewRepository;
	@Autowired
	ServiceAreaMatrixGSCDVViewRepository serviceAreaMatrixGSCDVViewRepository;
	@Autowired
	ServiceAreaMatrixGSCGlobalOutBndViewRepository serviceAreaMatrixGSCGlobalOutBndViewRepository;
	@Autowired
	ServiceAreaMatrixGSCITFSViewRepository serviceAreaMatrixGSCITFSViewRepository;
	@Autowired
	ServiceAreaMatrixGSCLNSViewRepository serviceAreaMatrixGSCLNSViewRepository;
	@Autowired
	ServiceAreaMatrixGSCUIFNViewRepository serviceAreaMatrixGSCUIFNViewRepository;
	@Autowired
	ServiceAreaMatrixGSCPSTNViewRepository serviceAreaMatrixGSCPSTNViewRepository;
	@Autowired
	GscSlaViewRepository gscSlaViewRepository;
	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.service.gsc.countries}")
	String gscPulseCountriesQueue;

	@Value("${rabbitmq.service.gsc.product.names}")
	String gscProductNamesQueue;

	@Value("${rabbitmq.partner.le.secs.queue}")
	String partnerLeSECSQueue;

	/**
	 * Get Countries for Partner, Enterprise and Wholesale Customer
	 *
	 * @param productName
	 * @param accessType
	 * @param secsId
	 * @return
	 * @throws TclCommonException
	 */
	public GscProductLocationBean getCountries(final String productName, final String accessType,
											   String secsId, final String partnerLeId) throws TclCommonException {
		if (!StringUtils.isAllEmpty(secsId)) {
			return getCountriesForWholesaleAndPartner(productName, accessType, secsId);
		}
		if (!StringUtils.isAllEmpty(partnerLeId)) {
			String response = (String) mqUtils.sendAndReceive(partnerLeSECSQueue, partnerLeId);
			LeSapCodeResponse secsCode = Utils.convertJsonToObject(response, LeSapCodeResponse.class);
			LOGGER.info("SECS Code response :: {}", secsCode);
			secsId = secsCode.getLeSapCodes().stream().map(LeSapCodeBean::getCodeValue).collect(Collectors.joining(COMMA));
			return getCountriesForWholesaleAndPartner(productName, accessType, secsId);
		}
		return getCountriesForCustomer(productName, accessType);
	}

	private GscProductLocationBean getCountriesForWholesaleAndPartner(final String productName, final String accessType, final String secsId) {
		final GscProductLocationBean gscProductLocationBean = new GscProductLocationBean();
		Set<GscProductLocationDetailBean> gscProductLocationDetailBeans = new HashSet<>();
		try {
			String response = (String) mqUtils.sendAndReceive(gscPulseCountriesQueue, productName.concat(HYPHEN).concat(secsId));
			LOGGER.info("Inventory Response :: {}", response);
			Set<Map<String, Object>> serviceAreaMatrixGSC = Utils.fromJson(response, new TypeReference<Set<Map<String, Object>>>() {
			});
//			Set<Map<String, Object>> serviceAreaMatrixGSC = Utils.convertJsonToObject(response, Set.class);
			LOGGER.info("Response :: {}", serviceAreaMatrixGSC);

			gscProductLocationDetailBeans = createGscCountryBean(serviceAreaMatrixGSC);
			gscProductLocationBean.setSources(gscProductLocationDetailBeans);
			gscProductLocationBean.setDestinations(gscProductLocationDetailBeans);
			if(PSTN.equalsIgnoreCase(accessType)) {
				gscProductLocationBean.setDestinations(getAllCountriesForPSTN());
			}

			LOGGER.info("gscProductLocationBean :: {}", gscProductLocationBean);
		} catch (Exception e) {
			LOGGER.error("Error while getting gsc pulse related countries :: {}", e.getStackTrace());
		}
		return gscProductLocationBean;
	}

	/**
	 * Get Countries by product name
	 *
	 * @param productName
	 * @return {@link GscProductLocationBean}
	 */
	private GscProductLocationBean getCountriesForCustomer(final String productName, final String accessType)
			throws TclCommonException {
		Objects.requireNonNull(productName, "Product name cannot be null");
		final GscProductLocationBean gscProductLocationBean = new GscProductLocationBean();
		Set<GscProductLocationDetailBean> gscProductLocationDetailBeans = new HashSet<>();
		Set<Map<String, Object>> serviceAreaMatrixGSC = new HashSet<>();
		try {
			switch (productName) {
			case ITFS:
				serviceAreaMatrixGSC = serviceAreaMatrixGSCITFSViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryNameWhereIndicatorPresent();
				break;
			case LNS:
				serviceAreaMatrixGSC = serviceAreaMatrixGSCLNSViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName();
				break;
			case UIFN:
				serviceAreaMatrixGSC = serviceAreaMatrixGSCUIFNViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName();
				break;
			case ACANS:
				serviceAreaMatrixGSC = serviceAreaMatrixGSCACANSViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName();
				break;
			case ACDTFS:
				serviceAreaMatrixGSC = serviceAreaMatrixGSCACDTFSViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName();
				break;
			case GLOBAL_OUTBOUND:
				serviceAreaMatrixGSC = serviceAreaMatrixGSCGlobalOutBndViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName();
				break;
			case DOMESTIC_VOICE:
				if(MPLS.equalsIgnoreCase(accessType)) {
					serviceAreaMatrixGSC = serviceAreaMatrixGSCDVViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryNameAndMPLS();
				} else {
					serviceAreaMatrixGSC = serviceAreaMatrixGSCDVViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName();
				}
				break;
			default:
				break;
			}
			gscProductLocationDetailBeans = createGscCountryBean(serviceAreaMatrixGSC);
		} catch (Exception e) {
			throw new TclCommonException(COMMON_ERROR, e, R_CODE_ERROR);
		}
		filterCountriesBasedOnProduct(productName, accessType, gscProductLocationBean, gscProductLocationDetailBeans);
		return gscProductLocationBean;
	}

	private void filterCountriesBasedOnProduct(String productName, String accessType, GscProductLocationBean gscProductLocationBean, Set<GscProductLocationDetailBean> gscProductLocationDetailBeans) {
		if (ACANS.equalsIgnoreCase(productName)) {
			gscProductLocationBean.setSources(gscProductLocationDetailBeans);
			gscProductLocationBean.setDestinations(getAllCountriesForACANS());
		} else if (ACDTFS.equalsIgnoreCase(productName)) {
			gscProductLocationBean.setDestinations(getAllCountriesForACDTFS());
			gscProductLocationBean.setSources(gscProductLocationDetailBeans);
		} else if (isAccessTypePublicIP(productName, accessType)) {
			gscProductLocationDetailBeans = excludeIndia(gscProductLocationDetailBeans);
			gscProductLocationBean.setSources(gscProductLocationDetailBeans);
			gscProductLocationBean.setDestinations(gscProductLocationDetailBeans);
		} else if(UIFN.equalsIgnoreCase(productName)){
			gscProductLocationBean.setSources(gscProductLocationDetailBeans);
			gscProductLocationBean.setDestinations(getAllCountriesForUIFN());
		} else if(PSTN.equalsIgnoreCase(accessType)) {
			gscProductLocationBean.setSources(gscProductLocationDetailBeans);
			gscProductLocationBean.setDestinations(getAllCountriesForPSTN());
		} else {
			gscProductLocationBean.setSources(gscProductLocationDetailBeans);
			gscProductLocationBean.setDestinations(gscProductLocationDetailBeans);
		}
	}

	private Set<GscProductLocationDetailBean> excludeIndia(Set<GscProductLocationDetailBean> gscProductLocationDetailBeans) {
		return gscProductLocationDetailBeans.stream().filter(gscProductLocationDetailBean ->
				!gscProductLocationDetailBean.getCode().equalsIgnoreCase(INDIA_COUNTRY_CODE)).collect(Collectors.toSet());
	}

	private boolean isAccessTypePublicIP(String productName, String accessType) {
		return (UIFN.equalsIgnoreCase(productName) || LNS.equalsIgnoreCase(productName))
				&& PUBLIC_IP.equalsIgnoreCase(accessType);
	}

	private Set<GscProductLocationDetailBean> createGscCountryBean(final Set<Map<String, Object>> serviceAreaMatrixGSC) {
		return serviceAreaMatrixGSC.stream().map(this::createGscProductLocationDetailBean)
				.collect(Collectors.toSet());
	}

	private GscProductLocationDetailBean createGscProductLocationDetailBean(
			final Map<String, Object> serviceAreaMatrixGSCView) {
		return new GscProductLocationDetailBean(serviceAreaMatrixGSCView.get(NAME).toString(),
				serviceAreaMatrixGSCView.get(CODE).toString(), serviceAreaMatrixGSCView.get(ISD_CODE).toString());
	}

	/**
	 * Get all cities by country name
	 *
	 * @param countryName
	 * @return {@link GscProductLocationBean}
	 */
	public GscProductLocationBean getCities(final String productName, final String countryName)
			throws TclCommonException {
		Objects.requireNonNull(productName, "Product name cannot be null");
		Objects.requireNonNull(countryName, "Country name cannot be null");
		GscProductLocationBean gscProductLocationBean = new GscProductLocationBean();
		Set<GscProductLocationDetailBean> gscProductLocationDetailBeans = new HashSet<>();
		if (LNS.equalsIgnoreCase(productName) || DOMESTIC_VOICE.equalsIgnoreCase(productName)) {
			List<ServiceAreaMatrixGSCLNSView> serviceAreaMatrixGSCLNSViews = serviceAreaMatrixGSCLNSViewRepository
					.findByIsoCountryName(countryName);
			gscProductLocationDetailBeans = serviceAreaMatrixGSCLNSViews.stream()
					.map(this::createGscProductLocationDetailBeanLNSForCity).collect(Collectors.toSet());
		} else if (ACANS.equalsIgnoreCase(productName)) {
			List<ServiceAreaMatrixGSCACANSView> serviceAreaMatrixGSCACANSViews = serviceAreaMatrixGSCACANSViewRepository
					.findByIsoCountryName(countryName);
			gscProductLocationDetailBeans = serviceAreaMatrixGSCACANSViews.stream()
					.map(this::createGscProductLocationDetailBeanACANSForCity).collect(Collectors.toSet());

		}
		gscProductLocationBean.setSources(gscProductLocationDetailBeans);
		gscProductLocationBean.setDestinations(gscProductLocationDetailBeans);
		return gscProductLocationBean;
	}

	private GscProductLocationDetailBean createGscProductLocationDetailBeanLNSForCity(ServiceAreaMatrixGSCLNSView serviceAreaMatrixGSCLNSView) {
		GscProductLocationDetailBean gscProductLocationDetailBean = new GscProductLocationDetailBean();
		gscProductLocationDetailBean.setName(serviceAreaMatrixGSCLNSView.getCityName());
		gscProductLocationDetailBean.setCode(serviceAreaMatrixGSCLNSView.getCityCode());
		if(Objects.nonNull(serviceAreaMatrixGSCLNSView.getAreaCode())) {
			gscProductLocationDetailBean.setAreaCode(Arrays.asList(serviceAreaMatrixGSCLNSView.getAreaCode()));
		}
//		return new GscProductLocationDetailBean(serviceAreaMatrixGSCLNSView.getCityName(),
//				serviceAreaMatrixGSCLNSView.getCityCode(), null, Arrays.asList(serviceAreaMatrixGSCLNSView.getAreaCode()));
		return gscProductLocationDetailBean;
	}

	private GscProductLocationDetailBean createGscProductLocationDetailBeanACANSForCity(
			ServiceAreaMatrixGSCACANSView serviceAreaMatrixGSCACANSView) {
		return new GscProductLocationDetailBean(serviceAreaMatrixGSCACANSView.getCityName(),
				serviceAreaMatrixGSCACANSView.getCityCode());
	}

	/**
	 * Get GSC Service matrix for given product and country name
	 *
	 * @param productName
	 * @param countryName
	 * @return {@link GscServiceMatrixBean}
	 */
	public GscServiceMatrixBean getServiceMatrix(final String productName, final String countryName)
			throws TclCommonException {
		Objects.requireNonNull(productName, PRODUCT_NAME_NULL_MESSAGE);
		Objects.requireNonNull(countryName, COUNTRY_NAME_NULL_MESSAGE);
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		try {
			switch (productName) {
			case ITFS:
				List<ServiceAreaMatrixGSCITFSView> serviceAreaMatrixGSCITFSViews = serviceAreaMatrixGSCITFSViewRepository
						.findByIsoCountryName(countryName);
				gscServiceMatrixBean = getGscServiceMatrixBeanForITFS(serviceAreaMatrixGSCITFSViews);
				break;
			case LNS:
				List<ServiceAreaMatrixGSCLNSView> serviceAreaMatrixGSCLNSViews = serviceAreaMatrixGSCLNSViewRepository
						.findByIsoCountryName(countryName);
				gscServiceMatrixBean = getGscServiceMatrixBeanForLNS(serviceAreaMatrixGSCLNSViews);
				break;
			case UIFN:
				List<ServiceAreaMatrixGSCUIFNView> serviceAreaMatrixGSCUIFNViews = serviceAreaMatrixGSCUIFNViewRepository
						.findByIsoCountryName(countryName);
				gscServiceMatrixBean = getGscServiceMatrixBeanForUIFN(serviceAreaMatrixGSCUIFNViews);
				break;
			case ACANS:
				List<ServiceAreaMatrixGSCACANSView> serviceAreaMatrixGSCACANSViews = serviceAreaMatrixGSCACANSViewRepository
						.findByIsoCountryName(countryName);
				gscServiceMatrixBean = getGscServiceMatrixBeanForACANS(serviceAreaMatrixGSCACANSViews);
				break;
			case ACDTFS:
				List<ServiceAreaMatrixGSCACDTFSView> serviceAreaMatrixGSCAudioCnfView = serviceAreaMatrixGSCACDTFSViewRepository
						.findByIsoCountryName(countryName);
				gscServiceMatrixBean = getGscServiceMatrixBeanForACDTFS(serviceAreaMatrixGSCAudioCnfView);
				break;
			case GLOBAL_OUTBOUND:
				List<ServiceAreaMatrixGSCGlobalOutBndView> serviceAreaMatrixGSCGlobalOutBndViews = serviceAreaMatrixGSCGlobalOutBndViewRepository
						.findByIsoCountryName(countryName);
				gscServiceMatrixBean = getGscServiceMatrixBeanForGlobalOutbound(serviceAreaMatrixGSCGlobalOutBndViews);
				break;
			case DOMESTIC_VOICE:
				List<ServiceAreaMatrixGSCDVView> serviceAreaMatrixGSCDVViews = serviceAreaMatrixGSCDVViewRepository
						.findByIsoCountryName(countryName);
				gscServiceMatrixBean = getGscServiceMatrixBeanForDV(serviceAreaMatrixGSCDVViews);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw new TclCommonException(COMMON_ERROR, e, R_CODE_ERROR);
		}

		return gscServiceMatrixBean;
	}

	private static GscServiceMatrixBean getGscServiceMatrixBeanForITFS(
			List<ServiceAreaMatrixGSCITFSView> serviceAreaMatrixGSCViews) {
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		serviceAreaMatrixGSCViews.forEach(serviceAreaMatrixGSCView -> {
			gscServiceMatrixBean.setProductName(serviceAreaMatrixGSCView.getProductName());
			gscServiceMatrixBean.setCountryName(serviceAreaMatrixGSCView.getIsoCountryName());
			gscServiceMatrixBean.setRestrictions(createRestrictionsForITFS(serviceAreaMatrixGSCView));
			gscServiceMatrixBean.setAccessibility(createAccessibilityForITFS(serviceAreaMatrixGSCView));
			gscServiceMatrixBean.setComments(createCommentsForITFS(serviceAreaMatrixGSCView));
			gscServiceMatrixBean.setOthers(createOthersForITFS(serviceAreaMatrixGSCView));
		});
		return gscServiceMatrixBean;
	}

	private static List<GscServiceMatrixAttributeBean> createRestrictionsForITFS(
			final ServiceAreaMatrixGSCITFSView serviceAreaMatrixGSCITFSView) {
		List<GscServiceMatrixAttributeBean> restrictions = new ArrayList<>();
		GscServiceMatrixAttributeBean porting = GscServiceMatrixAttributeBean.toAttributeBean(
				PORTABILITY,
				changePortingTextFormat(serviceAreaMatrixGSCITFSView.getPortabilityText()));
		GscServiceMatrixAttributeBean thirdCountryCalling = GscServiceMatrixAttributeBean.toAttributeBean(
				THIRD_COUNTRY_CALLING,
				serviceAreaMatrixGSCITFSView.getThirdCountryCallingRestrictionText());
		restrictions.add(porting);
		restrictions.add(thirdCountryCalling);
		return restrictions;
	}

	/**
	 * For ITFS - Porting option available for all International country
	 * <p>
	 * Between domestic carriers: no Between int'l carriers: yes
	 *
	 * @param portingText
	 * @return
	 */
	private static String changePortingTextFormat(String portingText) {
		if (Objects.nonNull(portingText)) {
			if (portingText.contains(BETWEEN_INTERNATIONAL_CARREIER_YES)
					|| YES.equalsIgnoreCase(portingText)) {
				return YES;
			}
		}
		return GscProductServiceMatrixConstant.NO;
	}

	private static List<GscServiceMatrixAttributeBean> createAccessibilityForITFS(
			final ServiceAreaMatrixGSCITFSView serviceAreaMatrixGSCITFSView) {
		List<GscServiceMatrixAttributeBean> accessibility = new ArrayList<>();
		GscServiceMatrixAttributeBean fixedLimitation = GscServiceMatrixAttributeBean.toAttributeBean(
				FIXED_NETWORK_LIMITATION,
				serviceAreaMatrixGSCITFSView.getFixedNetworkLimitationText());
		GscServiceMatrixAttributeBean mobileLimitation = GscServiceMatrixAttributeBean.toAttributeBean(
				MOBILE_NETWORK_LIMITATION,
				serviceAreaMatrixGSCITFSView.getMobileNetworkApplicableText());
		GscServiceMatrixAttributeBean fromPayPhone = GscServiceMatrixAttributeBean.toAttributeBean(
				FROM_PAYPHONE,
				serviceAreaMatrixGSCITFSView.getPayphoneApplicableText());
		accessibility.add(fixedLimitation);
		accessibility.add(mobileLimitation);
		accessibility.add(fromPayPhone);
		return accessibility;
	}

	private static List<GscServiceMatrixAttributeBean> createCommentsForITFS(
			final ServiceAreaMatrixGSCITFSView serviceAreaMatrixGSCITFSView) {
		List<GscServiceMatrixAttributeBean> comments = new ArrayList<>();
		GscServiceMatrixAttributeBean comment = GscServiceMatrixAttributeBean.toAttributeBean(
				COMMENTS, serviceAreaMatrixGSCITFSView.getCommentsText());
		comments.add(comment);
		return comments;
	}

	private static List<GscServiceMatrixAttributeBean> createOthersForITFS(
			final ServiceAreaMatrixGSCITFSView serviceAreaMatrixGSCITFSView) {
		List<GscServiceMatrixAttributeBean> others = new ArrayList<>();
		GscServiceMatrixAttributeBean newNumberAvailableIndicator = GscServiceMatrixAttributeBean.toAttributeBean(
				NEW_NUMBER_AVAILABLE_INDICATOR,
				serviceAreaMatrixGSCITFSView.getNewNumberAvailableIndicator());
		GscServiceMatrixAttributeBean dialingFormatText = GscServiceMatrixAttributeBean.toAttributeBean(
				DIALING_FORMAT_TEXT,
				serviceAreaMatrixGSCITFSView.getDialingFormatText());
		GscServiceMatrixAttributeBean numberSimultaneousCallsPerNumber = GscServiceMatrixAttributeBean.toAttributeBean(
				NUMBER_SIMULTANEOUS_CALLS_PER_NUMBER,
				getNumberSimultaneousCallsPerNumber(serviceAreaMatrixGSCITFSView.getNumberSimultaneousCallsPerNumber()));
		GscServiceMatrixAttributeBean estimatedStandardLeadTimeDays = GscServiceMatrixAttributeBean.toAttributeBean(
				ESTIMATED_STANDARD_LEAD_TIME_DAYS,
				serviceAreaMatrixGSCITFSView.getEstimatedStandardLeadTimeDays());
		others.add(newNumberAvailableIndicator);
		others.add(dialingFormatText);
		others.add(numberSimultaneousCallsPerNumber);
		others.add(estimatedStandardLeadTimeDays);
		return others;
	}

	/**
	 * Get Number format for number calls per number
	 *
	 * @param numberSimultaneousCallsPerNumber
	 * @return {@link String}
	 */
	private static String getNumberSimultaneousCallsPerNumber(String numberSimultaneousCallsPerNumber) {
		try {
			return String.valueOf(new DecimalFormat("#").format(Double.parseDouble(numberSimultaneousCallsPerNumber)));
		} catch (NumberFormatException | NullPointerException nfe) {
			return numberSimultaneousCallsPerNumber;
		}
	}

	private static GscServiceMatrixBean getGscServiceMatrixBeanForUIFN(
			List<ServiceAreaMatrixGSCUIFNView> serviceAreaMatrixGSCUIFNViews) {
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		serviceAreaMatrixGSCUIFNViews.forEach(serviceAreaMatrixGSCUIFNView -> {
			gscServiceMatrixBean.setProductName(serviceAreaMatrixGSCUIFNView.getProductName());
			gscServiceMatrixBean.setCountryName(serviceAreaMatrixGSCUIFNView.getIsoCountryName());
			gscServiceMatrixBean.setRestrictions(createRestrictionsForUIFN(serviceAreaMatrixGSCUIFNView));
			gscServiceMatrixBean.setAccessibility(createAccessibilityForUIFN(serviceAreaMatrixGSCUIFNView));
			gscServiceMatrixBean.setComments(createCommentsForUIFN(serviceAreaMatrixGSCUIFNView));
			gscServiceMatrixBean.setOthers(createOthersForUIFN(serviceAreaMatrixGSCUIFNView));
		});
		return gscServiceMatrixBean;
	}

	private static List<GscServiceMatrixAttributeBean> createRestrictionsForUIFN(
			final ServiceAreaMatrixGSCUIFNView serviceAreaMatrixGSCUIFNView) {
		List<GscServiceMatrixAttributeBean> restrictions = new ArrayList<>();
		GscServiceMatrixAttributeBean thirdCountryCalling = GscServiceMatrixAttributeBean.toAttributeBean(
				THIRD_COUNTRY_CALLING,
				serviceAreaMatrixGSCUIFNView.getThirdCountryCallingRestrictionText());
		restrictions.add(thirdCountryCalling);
		return restrictions;
	}

	private static List<GscServiceMatrixAttributeBean> createAccessibilityForUIFN(
			final ServiceAreaMatrixGSCUIFNView serviceAreaMatrixGSCUIFNView) {
		List<GscServiceMatrixAttributeBean> accessibility = new ArrayList<>();
		GscServiceMatrixAttributeBean fixedLimitation = GscServiceMatrixAttributeBean.toAttributeBean(
				FIXED_NETWORK_LIMITATION,
				serviceAreaMatrixGSCUIFNView.getFixedNetworkLimitationText());
		GscServiceMatrixAttributeBean mobileLimitation = GscServiceMatrixAttributeBean.toAttributeBean(
				MOBILE_NETWORK_LIMITATION,
				serviceAreaMatrixGSCUIFNView.getMobileNetworkApplicableText());
		GscServiceMatrixAttributeBean fromPayPhone = GscServiceMatrixAttributeBean.toAttributeBean(
				FROM_PAYPHONE,
				serviceAreaMatrixGSCUIFNView.getPayphoneApplicableText());
		accessibility.add(fixedLimitation);
		accessibility.add(mobileLimitation);
		accessibility.add(fromPayPhone);
		return accessibility;
	}

	private static List<GscServiceMatrixAttributeBean> createCommentsForUIFN(
			final ServiceAreaMatrixGSCUIFNView serviceAreaMatrixGSCUIFNView) {
		List<GscServiceMatrixAttributeBean> comments = new ArrayList<>();
		GscServiceMatrixAttributeBean comment = GscServiceMatrixAttributeBean.toAttributeBean(
				COMMENTS, serviceAreaMatrixGSCUIFNView.getCommentsText());
		comments.add(comment);
		return comments;
	}

	private static List<GscServiceMatrixAttributeBean> createOthersForUIFN(
			final ServiceAreaMatrixGSCUIFNView serviceAreaMatrixGSCUIFNView) {
		List<GscServiceMatrixAttributeBean> others = new ArrayList<>();
		GscServiceMatrixAttributeBean newNumberAvailableIndicator = GscServiceMatrixAttributeBean.toAttributeBean(
				NEW_NUMBER_AVAILABLE_INDICATOR,
				serviceAreaMatrixGSCUIFNView.getNewNumberAvailableInicator());
		GscServiceMatrixAttributeBean internationalAccessCode = GscServiceMatrixAttributeBean.toAttributeBean(
				GscProductServiceMatrixConstant.INTERNATIONA_ACCESS_CODE,
				serviceAreaMatrixGSCUIFNView.getInternationalAccessCode());
		GscServiceMatrixAttributeBean numberSimultaneousCallsPerNumber = GscServiceMatrixAttributeBean.toAttributeBean(
				NUMBER_SIMULTANEOUS_CALLS_PER_NUMBER,
				getNumberSimultaneousCallsPerNumber(serviceAreaMatrixGSCUIFNView.getNumberSimultaneousCallsPerNumber()));
		GscServiceMatrixAttributeBean estimatedStandardLeadTimeDays = GscServiceMatrixAttributeBean.toAttributeBean(
				ESTIMATED_STANDARD_LEAD_TIME_DAYS,
				serviceAreaMatrixGSCUIFNView.getEstimatedStandardLeadTimeDays());
		others.add(newNumberAvailableIndicator);
		others.add(internationalAccessCode);
		others.add(numberSimultaneousCallsPerNumber);
		others.add(estimatedStandardLeadTimeDays);
		return others;
	}

	private static GscServiceMatrixBean getGscServiceMatrixBeanForLNS(
			List<ServiceAreaMatrixGSCLNSView> serviceAreaMatrixGSCLNSViews) {
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		serviceAreaMatrixGSCLNSViews.forEach(serviceAreaMatrixGSCLNSView -> {
			gscServiceMatrixBean.setProductName(serviceAreaMatrixGSCLNSView.getProductName());
			gscServiceMatrixBean.setCountryName(serviceAreaMatrixGSCLNSView.getIsoCountryName());
			gscServiceMatrixBean.setRestrictions(createRestrictionsForLNS(serviceAreaMatrixGSCLNSView));
			gscServiceMatrixBean.setComments(createCommentsForLNS(serviceAreaMatrixGSCLNSView));
			gscServiceMatrixBean.setOthers(createOthersForLNS(serviceAreaMatrixGSCLNSView));
		});
		return gscServiceMatrixBean;
	}

	private static List<GscServiceMatrixAttributeBean> createRestrictionsForLNS(
			final ServiceAreaMatrixGSCLNSView serviceAreaMatrixGSCLNSView) {
		List<GscServiceMatrixAttributeBean> restrictions = new ArrayList<>();
		GscServiceMatrixAttributeBean porting = GscServiceMatrixAttributeBean.toAttributeBean(
				PORTABILITY, serviceAreaMatrixGSCLNSView.getPortabilityText());
		restrictions.add(porting);
		return restrictions;
	}

	private static List<GscServiceMatrixAttributeBean> createCommentsForLNS(
			final ServiceAreaMatrixGSCLNSView serviceAreaMatrixGSCLNSView) {
		List<GscServiceMatrixAttributeBean> comments = new ArrayList<>();
		GscServiceMatrixAttributeBean comment = GscServiceMatrixAttributeBean.toAttributeBean(
				COMMENTS, serviceAreaMatrixGSCLNSView.getCommentsText());
		comments.add(comment);
		return comments;
	}

	private static List<GscServiceMatrixAttributeBean> createOthersForLNS(
			final ServiceAreaMatrixGSCLNSView serviceAreaMatrixGSCLNSView) {
		List<GscServiceMatrixAttributeBean> others = new ArrayList<>();
		GscServiceMatrixAttributeBean estimatedStandardLeadTimeDays = GscServiceMatrixAttributeBean.toAttributeBean(
				ESTIMATED_STANDARD_LEAD_TIME_DAYS,
				serviceAreaMatrixGSCLNSView.getEstimatedStandardLeadTimeDays());
		GscServiceMatrixAttributeBean onHoldIndicator = GscServiceMatrixAttributeBean.toAttributeBean(
				ONHOLD_INDICATOR, serviceAreaMatrixGSCLNSView.getOnHoldIndicator());
		GscServiceMatrixAttributeBean numberSimultaneousCallsPerNumber = GscServiceMatrixAttributeBean.toAttributeBean(
				NUMBER_SIMULTANEOUS_CALLS_PER_NUMBER,
				getNumberSimultaneousCallsPerNumber(serviceAreaMatrixGSCLNSView.getNumberSimultaneousCallsPerNumber()));
		others.add(estimatedStandardLeadTimeDays);
		others.add(onHoldIndicator);
		others.add(numberSimultaneousCallsPerNumber);
		return others;
	}

	private static GscServiceMatrixBean getGscServiceMatrixBeanForDV(
			List<ServiceAreaMatrixGSCDVView> serviceAreaMatrixGSCViews) {
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		serviceAreaMatrixGSCViews.forEach(serviceAreaMatrixGSCView -> {
			gscServiceMatrixBean.setProductName(serviceAreaMatrixGSCView.getProductName());
			gscServiceMatrixBean.setCountryName(serviceAreaMatrixGSCView.getIsoCountryName());
			gscServiceMatrixBean.setRestrictions(createRestrictionsForDV(serviceAreaMatrixGSCView));
			gscServiceMatrixBean.setOthers(createOthersForDV(serviceAreaMatrixGSCView));
		});
		return gscServiceMatrixBean;
	}

	private static List<GscServiceMatrixAttributeBean> createRestrictionsForDV(
			final ServiceAreaMatrixGSCDVView serviceAreaMatrixGSCDVView) {
		List<GscServiceMatrixAttributeBean> restrictions = new ArrayList<>();
		GscServiceMatrixAttributeBean porting = GscServiceMatrixAttributeBean.toAttributeBean(
				PORTABILITY, serviceAreaMatrixGSCDVView.getPortabilityText());
		restrictions.add(porting);
		return restrictions;
	}

	private static List<GscServiceMatrixAttributeBean> createOthersForDV(
			final ServiceAreaMatrixGSCDVView serviceAreaMatrixGSCDVView) {
		List<GscServiceMatrixAttributeBean> others = new ArrayList<>();
		GscServiceMatrixAttributeBean estimatedStandardLeadTimeDays = GscServiceMatrixAttributeBean.toAttributeBean(
				ESTIMATED_STANDARD_LEAD_TIME_DAYS,
				serviceAreaMatrixGSCDVView.getEstimatedStandardLeadTimeDays());
		others.add(estimatedStandardLeadTimeDays);
		return others;
	}

	private static GscServiceMatrixBean getGscServiceMatrixBeanForACANS(
			List<ServiceAreaMatrixGSCACANSView> serviceAreaMatrixGSCACANSViews) {
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		serviceAreaMatrixGSCACANSViews.forEach(serviceAreaMatrixGSCView -> {
			gscServiceMatrixBean.setProductName(serviceAreaMatrixGSCView.getProductName());
			gscServiceMatrixBean.setCountryName(serviceAreaMatrixGSCView.getIsoCountryName());
		});
		return gscServiceMatrixBean;
	}

	private static GscServiceMatrixBean getGscServiceMatrixBeanForACDTFS(
			List<ServiceAreaMatrixGSCACDTFSView> serviceAreaMatrixGSCACDTFSViews) {
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		serviceAreaMatrixGSCACDTFSViews.forEach(serviceAreaMatrixGSCView -> {
			gscServiceMatrixBean.setProductName(serviceAreaMatrixGSCView.getProductName());
			gscServiceMatrixBean.setCountryName(serviceAreaMatrixGSCView.getIsoCountryName());
			gscServiceMatrixBean.setRestrictions(createRestrictionsForACDTFS(serviceAreaMatrixGSCView));
			gscServiceMatrixBean.setAccessibility(createAccessibilityForACDTFS(serviceAreaMatrixGSCView));
			gscServiceMatrixBean.setComments(createCommentsForACDTFS(serviceAreaMatrixGSCView));
			gscServiceMatrixBean.setOthers(createOthersForACDTFS(serviceAreaMatrixGSCView));
		});
		return gscServiceMatrixBean;
	}
	
	private static List<GscServiceMatrixAttributeBean> createRestrictionsForACDTFS(
			final ServiceAreaMatrixGSCACDTFSView serviceAreaMatrixGSCACDTFSView) {
		List<GscServiceMatrixAttributeBean> restrictions = new ArrayList<>();
		GscServiceMatrixAttributeBean porting = GscServiceMatrixAttributeBean.toAttributeBean(
				PORTABILITY,
				changePortingTextFormat(serviceAreaMatrixGSCACDTFSView.getPortabilityText()));
		GscServiceMatrixAttributeBean thirdCountryCalling = GscServiceMatrixAttributeBean.toAttributeBean(
				THIRD_COUNTRY_CALLING,
				serviceAreaMatrixGSCACDTFSView.getThirdCountryCallingRestrictionText());
		restrictions.add(porting);
		restrictions.add(thirdCountryCalling);
		return restrictions;
	}
	
	
		private static List<GscServiceMatrixAttributeBean> createAccessibilityForACDTFS(
			final ServiceAreaMatrixGSCACDTFSView serviceAreaMatrixGSCACDTFSView) {
		List<GscServiceMatrixAttributeBean> accessibility = new ArrayList<>();
		GscServiceMatrixAttributeBean fixedLimitation = GscServiceMatrixAttributeBean.toAttributeBean(
				FIXED_NETWORK_LIMITATION,
				serviceAreaMatrixGSCACDTFSView.getFixedNetworkLimitationText());
		GscServiceMatrixAttributeBean mobileLimitation = GscServiceMatrixAttributeBean.toAttributeBean(
				MOBILE_NETWORK_LIMITATION,
				serviceAreaMatrixGSCACDTFSView.getMobileNetworkApplicableText());
		GscServiceMatrixAttributeBean fromPayPhone = GscServiceMatrixAttributeBean.toAttributeBean(
				FROM_PAYPHONE,
				serviceAreaMatrixGSCACDTFSView.getPayphoneApplicableText());
		accessibility.add(fixedLimitation);
		accessibility.add(mobileLimitation);
		accessibility.add(fromPayPhone);
		return accessibility;
	}

	private static List<GscServiceMatrixAttributeBean> createCommentsForACDTFS(
			final ServiceAreaMatrixGSCACDTFSView serviceAreaMatrixGSCACDTFSView) {
		List<GscServiceMatrixAttributeBean> comments = new ArrayList<>();
		GscServiceMatrixAttributeBean comment = GscServiceMatrixAttributeBean.toAttributeBean(
				COMMENTS, serviceAreaMatrixGSCACDTFSView.getCommentsText());
		comments.add(comment);
		return comments;
	}

	private static List<GscServiceMatrixAttributeBean> createOthersForACDTFS(
			final ServiceAreaMatrixGSCACDTFSView serviceAreaMatrixGSCACDTFSView) {
		List<GscServiceMatrixAttributeBean> others = new ArrayList<>();
		GscServiceMatrixAttributeBean newNumberAvailableIndicator = GscServiceMatrixAttributeBean.toAttributeBean(
				NEW_NUMBER_AVAILABLE_INDICATOR,
				serviceAreaMatrixGSCACDTFSView.getNewNumberAvailableIndicator());
		GscServiceMatrixAttributeBean dialingFormatText = GscServiceMatrixAttributeBean.toAttributeBean(
				DIALING_FORMAT_TEXT,
				serviceAreaMatrixGSCACDTFSView.getDialingFormatText());
		GscServiceMatrixAttributeBean numberSimultaneousCallsPerNumber = GscServiceMatrixAttributeBean.toAttributeBean(
				NUMBER_SIMULTANEOUS_CALLS_PER_NUMBER,
				getNumberSimultaneousCallsPerNumber(serviceAreaMatrixGSCACDTFSView.getNumberSimultaneousCallsPerNumber()));
		GscServiceMatrixAttributeBean estimatedStandardLeadTimeDays = GscServiceMatrixAttributeBean.toAttributeBean(
				ESTIMATED_STANDARD_LEAD_TIME_DAYS,
				serviceAreaMatrixGSCACDTFSView.getEstimatedStandardLeadTimeDays());
		others.add(newNumberAvailableIndicator);
		others.add(dialingFormatText);
		others.add(numberSimultaneousCallsPerNumber);
		others.add(estimatedStandardLeadTimeDays);
		return others;
	}

	private static GscServiceMatrixBean getGscServiceMatrixBeanForGlobalOutbound(
			List<ServiceAreaMatrixGSCGlobalOutBndView> serviceAreaMatrixGSCViews) {
		GscServiceMatrixBean gscServiceMatrixBean = new GscServiceMatrixBean();
		List<List<GscServiceMatrixAttributeBean>> regions = new ArrayList<>();
		List<GscServiceMatrixAttributeBean> region = new ArrayList<>();
		serviceAreaMatrixGSCViews.forEach(serviceAreaMatrixGSCView -> {
			gscServiceMatrixBean.setProductName(GLOBAL_OUTBOUND);
			gscServiceMatrixBean.setCountryName(serviceAreaMatrixGSCView.getIsoCountryName());
			regions.add(createOthersForGlobalOutbound(serviceAreaMatrixGSCView));
		});
		gscServiceMatrixBean.setRegions(regions);
		return gscServiceMatrixBean;
	}

	private static List<GscServiceMatrixAttributeBean> createCommentsForGlobalOutbound(
			final ServiceAreaMatrixGSCGlobalOutBndView serviceAreaMatrixGSCITFSView) {
		List<GscServiceMatrixAttributeBean> comments = new ArrayList<>();
		GscServiceMatrixAttributeBean comment = GscServiceMatrixAttributeBean
				.toAttributeBean(COMMENTS, serviceAreaMatrixGSCITFSView.getComments());
		comments.add(comment);
		return comments;
	}

	private static List<GscServiceMatrixAttributeBean> createOthersForGlobalOutbound(
			final ServiceAreaMatrixGSCGlobalOutBndView serviceAreaMatrixGSCGlobalOutBndView) {
		List<GscServiceMatrixAttributeBean> others = new ArrayList<>();
		GscServiceMatrixAttributeBean phoneType = GscServiceMatrixAttributeBean.toAttributeBean(
				PHONE_TYPE, serviceAreaMatrixGSCGlobalOutBndView.getPhoneType());
		GscServiceMatrixAttributeBean destinationName = GscServiceMatrixAttributeBean.toAttributeBean(
				DESTINATION_NAME,
				serviceAreaMatrixGSCGlobalOutBndView.getDestinationName());
		GscServiceMatrixAttributeBean serviceLevel = GscServiceMatrixAttributeBean.toAttributeBean(
				SERVICE_LEVEL, serviceAreaMatrixGSCGlobalOutBndView.getServiceLevel());
		GscServiceMatrixAttributeBean region = GscServiceMatrixAttributeBean.toAttributeBean(
				REGION, serviceAreaMatrixGSCGlobalOutBndView.getRegion());
		GscServiceMatrixAttributeBean comments = GscServiceMatrixAttributeBean.toAttributeBean(
				COMMENTS, serviceAreaMatrixGSCGlobalOutBndView.getComments());
		GscServiceMatrixAttributeBean internalComments = GscServiceMatrixAttributeBean.toAttributeBean(
				INTERNAL_COMMENTS,
				serviceAreaMatrixGSCGlobalOutBndView.getInternalComments());
		others.add(phoneType);
		others.add(destinationName);
		others.add(serviceLevel);
		others.add(region);
		others.add(comments);
		others.add(internalComments);
		return others;
	}

	/**
	 * TODO : Need to work on this once sla is ready Process product sla bean
	 *
	 * @param request
	 * @return {@link ProductSlaBean}
	 */
	public ProductSlaBean processProductSla(final String request) {
		return new ProductSlaBean();
	}

	private Set<GscProductLocationDetailBean> getAllCountriesForACANS() {
		return createGscCountryBean(
				serviceAreaMatrixGSCLNSViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName());
	}

	private Set<GscProductLocationDetailBean> getAllCountriesForACDTFS() {
		return createGscCountryBean(
				serviceAreaMatrixGSCITFSViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryNameForACDTFS());
	}

	private Set<GscProductLocationDetailBean> getAllCountriesForUIFN(){
		return createGscCountryBean(
				serviceAreaMatrixGSCUIFNViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryNameAndCountryTypeSrcDest());
	}

	private Set<GscProductLocationDetailBean> getAllCountriesForPSTN(){
		return createGscCountryBean(serviceAreaMatrixGSCPSTNViewRepository.findAllCountry());
	}

	/**
	 * Calculates the rfs date based on service, country and accesstype
	 *
	 * @param service
	 * @param accessType
	 * @param country
	 * @return rfsDate
	 * @author VISHESH AWASTHI
	 */
	public String getGscExpectedDateForDelivery(final String service, final String accessType, final String country) {
		Objects.requireNonNull(service);
		Objects.requireNonNull(accessType);
		Objects.requireNonNull(country);
		String expectedDeliveryDate = null;
		switch (service) {
		case ITFS:
			switch (accessType) {
			case PSTN:
				expectedDeliveryDate = getGscSLTForITFSOnPSTN(country);
				break;
			case MPLS:
				expectedDeliveryDate = getGscSLTOnMPLS(getGscSLTForITFSOnPSTN(country));
				break;
			case PUBLIC_IP:
			case DEDICATED:
				expectedDeliveryDate = getGscSLTOnPublicIp(getGscSLTForITFSOnPSTN(country));
				break;
			case NNI:
				expectedDeliveryDate = BEST_EFFORT;
				break;
			default:
				break;
			}
			break;
		case UIFN:
			switch (accessType) {
			case PSTN:
				expectedDeliveryDate = getGscSLTForUIFNOnPSTN(country);
				break;
			case MPLS:
				expectedDeliveryDate = getGscSLTOnMPLS(getGscSLTForUIFNOnPSTN(country));
				break;
			case PUBLIC_IP:
			case DEDICATED:
				expectedDeliveryDate = getGscSLTOnPublicIp(getGscSLTForUIFNOnPSTN(country));
				break;
			case NNI:
				expectedDeliveryDate = BEST_EFFORT;
				break;
			default:
				break;
			}
			break;
		case LNS:
			switch (accessType) {
			case PSTN:
				expectedDeliveryDate = getGscSLTForLNSOnPSTN(country);
				break;
			case MPLS:
				expectedDeliveryDate = getGscSLTOnMPLS(getGscSLTForLNSOnPSTN(country));
				break;
			case PUBLIC_IP:
			case DEDICATED:
				expectedDeliveryDate = getGscSLTOnPublicIp(getGscSLTForLNSOnPSTN(country));
				break;
			case NNI:
				expectedDeliveryDate = BEST_EFFORT;
				break;
			default:
				break;
			}
			break;
		case ACANS:
		case ACDTFS:
		case ACLNS:
			switch (accessType) {
			case PSTN:
				expectedDeliveryDate = ACANS_PSTN_SLT;
				break;
			case MPLS:
				expectedDeliveryDate = getGscSLTOnMPLS(ACANS_PSTN_SLT);
				break;
			case PUBLIC_IP:
			case DEDICATED:
				expectedDeliveryDate = getGscSLTOnPublicIp(ACANS_PSTN_SLT);
				break;
			case NNI:
				expectedDeliveryDate = BEST_EFFORT;
				break;
			default:
				break;
			}
			break;
		case GLOBAL_OUTBOUND:
			switch (accessType) {
			case PSTN:
				expectedDeliveryDate = GLOBAL_OUTBOUND_PSTN_SLT;
				break;
			case MPLS:
				expectedDeliveryDate = getGscSLTOnMPLS(GLOBAL_OUTBOUND_PSTN_SLT);
				break;
			case PUBLIC_IP:
				expectedDeliveryDate = getGscSLTOnPublicIp(GLOBAL_OUTBOUND_PSTN_SLT);
				break;
			case NNI:
				expectedDeliveryDate = BEST_EFFORT;
				break;
			default:
				break;
			}
			break;
		case DOMESTIC_VOICE:
			switch (accessType) {
			case PSTN:
				expectedDeliveryDate = getGscSLTForDomestivVoiceOnPSTN(country);
				break;
			case MPLS:
				expectedDeliveryDate = getGscSLTOnMPLS(getGscSLTForDomestivVoiceOnPSTN(country));
				break;
			case PUBLIC_IP:
				expectedDeliveryDate = getGscSLTOnPublicIp(getGscSLTForDomestivVoiceOnPSTN(country));
				break;
			case NNI:
				expectedDeliveryDate = BEST_EFFORT;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		return expectedDeliveryDate;
	}

	/**
	 * @param country
	 * @return
	 */
	private String getGscSLTForITFSOnPSTN(final String country) {
		List<ServiceAreaMatrixGSCITFSView> gscITFSViews = serviceAreaMatrixGSCITFSViewRepository
				.findByIsoCountryName(country);
		return gscITFSViews.stream().findFirst().map(ServiceAreaMatrixGSCITFSView::getEstimatedStandardLeadTimeDays)
				.orElse(BEST_EFFORT);
	}

	/**
	 * @param country
	 * @return
	 */
	private String getGscSLTForUIFNOnPSTN(final String country) {
		List<ServiceAreaMatrixGSCUIFNView> gscUIFNViews = serviceAreaMatrixGSCUIFNViewRepository
				.findByIsoCountryName(country);
		return gscUIFNViews.stream().findFirst().map(ServiceAreaMatrixGSCUIFNView::getEstimatedStandardLeadTimeDays)
				.orElse(BEST_EFFORT);
	}

	/**
	 * @param country
	 * @return
	 */
	private String getGscSLTForLNSOnPSTN(final String country) {
		List<ServiceAreaMatrixGSCLNSView> gscLNSViews = serviceAreaMatrixGSCLNSViewRepository
				.findByIsoCountryName(country);
		return gscLNSViews.stream().findFirst().map(ServiceAreaMatrixGSCLNSView::getEstimatedStandardLeadTimeDays)
				.orElse(BEST_EFFORT);
	}

	/**
	 * @param country
	 * @return
	 */
	private String getGscSLTForDomestivVoiceOnPSTN(final String country) {
		List<ServiceAreaMatrixGSCDVView> gscDomesticVoiceViews = serviceAreaMatrixGSCDVViewRepository
				.findByIsoCountryName(country);
		return gscDomesticVoiceViews.stream().findFirst()
				.map(ServiceAreaMatrixGSCDVView::getEstimatedStandardLeadTimeDays)
				.orElse(BEST_EFFORT);
	}

	/**
	 * @param pstnSLT
	 * @return
	 */
	private String getGscSLTOnMPLS(String pstnSLT) {
		return pstnSLT.equalsIgnoreCase(BEST_EFFORT) ? pstnSLT : getSLTForMpls(pstnSLT);
	}

	/**
	 * @param pstnSLT
	 * @return
	 */
	private String getSLTForMpls(String pstnSLT) {
		return String
				.valueOf(Math.max(Integer.valueOf(pstnSLT), Integer.valueOf(MPLS_SLT))
						+ Integer.valueOf(PROV_TESTING_SLT));
	}

	/**
	 * @param pstnSLT
	 * @return
	 */
	private String getGscSLTOnPublicIp(String pstnSLT) {
		return pstnSLT.equalsIgnoreCase(BEST_EFFORT) ? pstnSLT
				: getSLTForPublicIp(pstnSLT);
	}

	/**
	 * @param pstnSLT
	 * @return
	 */
	private String getSLTForPublicIp(String pstnSLT) {
		return String.valueOf(
				Math.max(Integer.valueOf(pstnSLT), Integer.valueOf(PROV_TESTING_SLT))
						+ Integer.valueOf(GscProductServiceMatrixConstant.TESTING_SLT));
	}

	/**
	 * Get Gsc sla details based on accessType
	 * 
	 * @param listener
	 * @return {@link List<GscSlaBean>}
	 */
	public List<GscSlaBean> processProductSla(GscSlaBeanListener listener) {
		Objects.requireNonNull(listener, "Listener should not be null");
		List<GscSlaBean> gscSlaBeanList = new ArrayList<>();
		List<GscSlaView> gscSlaViewList = gscSlaViewRepository.findByAccessTopology(listener.getAccessType());
		Optional.ofNullable(gscSlaViewList).ifPresent(gscSlaView -> {
			gscSlaView.forEach(slaView -> {
				GscSlaBean gscSlaBean = new GscSlaBean();
				gscSlaBean.setSlaName(slaView.getSlaName());
				gscSlaBean.setDefaultValue(slaView.getDefaultValue());
				gscSlaBean.setAccessTopology(slaView.getAccessTopology());
				gscSlaBean.setPdtName(slaView.getPdtName());
				gscSlaBeanList.add(gscSlaBean);
			});
		});
		return gscSlaBeanList;
	}

	/**
	 * Get LNS City Code and City Name
	 *
	 * @return
	 */
	public Map<String, String> getLNSCitiesByCode() {
		Map<String, String> cityCodeAndName = new HashMap<>();
		Set<Map<String, Object>> serviceAreaMatrixGSCLNSViews = serviceAreaMatrixGSCLNSViewRepository.findLnsCityCodesAndNames();
		for(Map<String, Object> map : serviceAreaMatrixGSCLNSViews) {
			cityCodeAndName.put(map.get(CODE).toString(), map.get(NAME).toString());
		}
		return cityCodeAndName;
	}

	/**
	 * Get ACANS City Code and City Name
	 *
	 * @return
	 */
	public Map<String, String> getACANSCitiesByCode() {
		Map<String, String> cityCodeAndName = new HashMap<>();
		Set<Map<String, Object>> serviceAreaMatrixGSCLNSViews = serviceAreaMatrixGSCACANSViewRepository.findAcansCityCodesAndNames();
		for(Map<String, Object> map : serviceAreaMatrixGSCLNSViews) {
			cityCodeAndName.put(map.get(CODE).toString(), map.get(NAME).toString());
		}
		return cityCodeAndName;
	}

	/**
	 * Get distinct outboundCountries
	 *
	 * @param productName
	 * @return
	 */
	public List<String> getDistinctCountriesByProduct(String productName) throws TclCommonException {
		List<String> outboundCountries = new ArrayList<>();
		try {
			switch (productName) {
				case GLOBAL_OUTBOUND:
					outboundCountries = serviceAreaMatrixGSCGlobalOutBndViewRepository.findAll().stream().map(ServiceAreaMatrixGSCGlobalOutBndView::getIsoCountryName).distinct().collect(Collectors.toList());
					LOGGER.info("No of distinct outbound countries by GB{} ", outboundCountries.size());
					break;
			}
		} catch (Exception e) {
			throw new TclCommonException(COMMON_ERROR, e, R_CODE_ERROR);
		}
		return outboundCountries;
	}

	/**
	 * Get Applicable Product Names by Secs ID
	 *
	 * @param secsId
	 * @return
	 */
	public List<String> getProductNames(String secsId, String partnerLeId) {
		List<String> gscProductNames = new ArrayList<>();
		try {
			if (!StringUtils.isAllEmpty(partnerLeId)) {
				String response = (String) mqUtils.sendAndReceive(partnerLeSECSQueue, partnerLeId);
				LeSapCodeResponse secsCode = Utils.convertJsonToObject(response, LeSapCodeResponse.class);
				LOGGER.info("SECS Code response :: {}", secsCode);
				secsId = secsCode.getLeSapCodes().stream().map(LeSapCodeBean::getCodeValue).collect(Collectors.joining(COMMA));
			}
			String response = (String) mqUtils.sendAndReceive(gscProductNamesQueue, secsId);
			LOGGER.info("Response {}", response);
			gscProductNames = Utils.fromJson(response, new TypeReference<List<String>>() {
			});
		} catch(Exception e) {
			LOGGER.error("Error while getting GSC Product Names :: {}", e.getStackTrace());
		}
		return gscProductNames;
	}

}
