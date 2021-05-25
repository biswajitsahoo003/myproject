package com.tcl.dias.products.izosdwan.service.v1;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.AddonsBean;
import com.tcl.dias.common.beans.CgwServiceAreaMatricBean;
import com.tcl.dias.common.beans.CpeRequestBean;
import com.tcl.dias.common.beans.IzoSdwanCpeBomInterface;
import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.beans.IzosdwanBandwidthInterface;
import com.tcl.dias.common.beans.ProductLocationBean;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.VProxyAddonsBean;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.beans.VproxyAttributeDetails;
import com.tcl.dias.common.beans.VproxyProductOfferingBean;
import com.tcl.dias.common.beans.VproxyQuestionnaireDet;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.common.beans.VutmProfileDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.productcatelog.entity.entities.CpeBomInterfaceSdwanView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomSdwanRulesView;
import com.tcl.dias.productcatelog.entity.entities.Location;
import com.tcl.dias.productcatelog.entity.entities.VwSdwanBwIntfMapping;
import com.tcl.dias.productcatelog.entity.entities.VwSdwanProductOffering;
import com.tcl.dias.productcatelog.entity.entities.VwVproxyProductComponent;
import com.tcl.dias.productcatelog.entity.entities.VwVproxyProductOffering;
import com.tcl.dias.productcatelog.entity.repository.CpeBomInterfaceSdwanViewRepository;
import com.tcl.dias.productcatelog.entity.repository.CpeBomSdwanDetailRepository;
import com.tcl.dias.productcatelog.entity.repository.LocationRepository;
import com.tcl.dias.productcatelog.entity.repository.VwCtryCitySlaSdwanRepository;
import com.tcl.dias.productcatelog.entity.repository.VwSdwanBwIntfMappingRepository;
import com.tcl.dias.productcatelog.entity.repository.VwSdwanIndiaSlaRepository;
import com.tcl.dias.productcatelog.entity.repository.VwSdwanProductOfferingRepository;
import com.tcl.dias.productcatelog.entity.repository.VwSlaProductSdwanRepository;
import com.tcl.dias.productcatelog.entity.repository.VwVproxyProductComponentRepository;
import com.tcl.dias.productcatelog.entity.repository.VwVproxyProductOfferingRepository;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.constants.IzosdwanConstants;
import com.tcl.dias.products.izosdwan.beans.CpeBomDetails;
import com.tcl.dias.products.izosdwan.beans.CpeDetails;
import com.tcl.dias.products.izosdwan.beans.CpeSpecifications;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This is the service class for IZOSDWAN product service
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class IzosdwanProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductsService.class);

	@Autowired
	VwSdwanProductOfferingRepository vwSdwanProductOfferingRepository;

	@Autowired
	CpeBomSdwanDetailRepository cpeBomDetails;

	@Autowired
	CpeBomInterfaceSdwanViewRepository bomInterfaceSdwanViewRepository;

	@Autowired

	VwSdwanIndiaSlaRepository vwSdwanIndiaSlaRepository;
	
	@Autowired

	VwCtryCitySlaSdwanRepository vwCtryCitySlaSdwanRepository;

	@Autowired
	VwSlaProductSdwanRepository vwSlaProductSdwanRepository;

	@Autowired
	VwVproxyProductOfferingRepository vwVproxyProductOfferingRepository;

	@Autowired
	VwVproxyProductComponentRepository vwVproxyProductComponentRepository;

	@Autowired
	VwSdwanBwIntfMappingRepository vwSdwanBwIntfMappingRepository;
	protected MQUtils mqUtils;

	@Value("${rabbitmq.product.cpe.detail}")
	String cpeNameQueue;
	
	@Autowired
	LocationRepository locationRepository;

	/**
	 * 
	 * Get Product Offering details for SDWAN
	 * 
	 * @author AnandhiV
	 * @param vendor
	 * @return
	 * @throws TclCommonException
	 */
	public List<VendorProfileDetailsBean> getProductOfferingsForSdwanBasedOnVendor(String vendor)
			throws TclCommonException {
		if (!StringUtils.isNotBlank(vendor)) {
			LOGGER.info("Vendor name is empty or null");
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		List<VendorProfileDetailsBean> vendorProfileDetailsBeans = new ArrayList<>();
		try {
			switch (vendor) {
			case "Cisco":
				constructProfiles(vendorProfileDetailsBeans, IzosdwanConstants.CISCO_VENDOR_CODE);
				LOGGER.info("Cisco Vendor profiles");
				break;
			case "Select":
				constructProfiles(vendorProfileDetailsBeans, IzosdwanConstants.VERSA_VENDOR_CODE);
				LOGGER.info("Versa Vendor profiles");
				break;
			case "All":
				constructProfiles(vendorProfileDetailsBeans, IzosdwanConstants.CISCO_VENDOR_CODE);
				constructProfiles(vendorProfileDetailsBeans, IzosdwanConstants.VERSA_VENDOR_CODE);
				LOGGER.info("View all profiles");
				break;
			default:
				LOGGER.info("Invalid Vendor");
				break;
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while fetching the profile!!", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return vendorProfileDetailsBeans;
	}

	private void constructProfiles(List<VendorProfileDetailsBean> vendorProfileDetailsBeans, String vendor) {
		VendorProfileDetailsBean vendorProfileDetailsBean = new VendorProfileDetailsBean();
		List<ProductOfferingsBean> productOfferingsBeans = new ArrayList<>();
		List<String> profiles = vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(vendor);
		if (profiles != null && !profiles.isEmpty()) {
			profiles.stream().forEach(profile -> {
				ProductOfferingsBean productOfferingsBean = new ProductOfferingsBean();
				List<VwSdwanProductOffering> vwSdwanProductOfferings = vwSdwanProductOfferingRepository
						.findByProfileCdAndVendorCd(profile, vendor);
				if (vwSdwanProductOfferings != null && !vwSdwanProductOfferings.isEmpty()) {
					List<AddonsBean> addOns = new ArrayList<>();
					vwSdwanProductOfferings.stream().forEach(offerings -> {
						productOfferingsBean.setMrc(offerings.getMrc());
						productOfferingsBean.setNrc(offerings.getNrc());
						if (offerings.getProfileDescription() != null) {
							productOfferingsBean.setProductOfferingsDescription(
									Arrays.asList(offerings.getProfileDescription().split("\\r\\n")));
						}
						productOfferingsBean.setProductOfferingsName(offerings.getProfileName());
						productOfferingsBean.setProductOfferingsCode(offerings.getProfileCd());
						AddonsBean addonsBean = new AddonsBean();
						addonsBean.setCode(offerings.getAddonCd());
						addonsBean.setName(offerings.getAddonName());
						if (offerings.getAddonDescription() != null) {
							addonsBean.setDescription(Arrays.asList(offerings.getAddonDescription().split("\\r\\n")));
						}
						if (addonsBean != null && addonsBean.getName() != null) {
							addOns.add(addonsBean);
						}
					});
					productOfferingsBean.setAddons(addOns);
				}
				productOfferingsBeans.add(productOfferingsBean);
			});
			vendorProfileDetailsBean.setProductOfferingsBeans(productOfferingsBeans);
			vendorProfileDetailsBean.setVendor(vendor);
			vendorProfileDetailsBeans.add(vendorProfileDetailsBean);
		}
	}

	/**
	 * this method will fecth the cpe models based on the addons,profilename and
	 * vendor
	 * 
	 * @param bean
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unused")
	public List<IzoSdwanCpeDetails> getCpeDetails(CpeRequestBean bean) throws TclCommonException {
		List<IzoSdwanCpeDetails> cpeDetails = new ArrayList<>();

		try {
			if (bean == null || bean.getVendorName() == null || bean.getProfileName() == null) {
				List<CpeBomSdwanRulesView> sdwanCpeDet = cpeBomDetails.findAll();
				cpeDetails = getSdwanCpeDetails(sdwanCpeDet);
			} else {
				if (bean.getProfileName().equals("Select Secure Premium")) {
					bean.setProfileName("SECURE_PREMIUM");
				}
				if (bean.getAddons() == null || bean.getAddons().length() == 0) {
					List<CpeBomSdwanRulesView> sdwanCpeDet = cpeBomDetails
							.selectByVendorNameAndLicCd(bean.getVendorName(), bean.getProfileName());

					cpeDetails = getSdwanCpeDetails(sdwanCpeDet);
				} else {
					List<CpeBomSdwanRulesView> sdwanCpeDet = cpeBomDetails.findByVendorNameAndAddonCdAndLicCd(
							bean.getVendorName(), bean.getAddons(), bean.getProfileName());

					cpeDetails = getSdwanCpeDetails(sdwanCpeDet);
					cpeDetails = cpeDetails.stream().distinct().collect(Collectors.toList());

				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return cpeDetails;

	}

	private List<IzoSdwanCpeDetails> getSdwanCpeDetails(List<CpeBomSdwanRulesView> cpeRules) {
		List<IzoSdwanCpeDetails> sdwanCpeDet = new ArrayList<>();
		// TreeSet<IzoSdwanCpeDetails> cpeRules1 = new
		// TreeSet<>(Comparator.comparing(IzoSdwanCpeDetails::getCpeName));
		if (cpeRules != null && !cpeRules.isEmpty()) {
			cpeRules.stream().forEach(cpe -> {
				if (cpe != null) {
					sdwanCpeDet.add(extractCpeDetails(cpe));
				}
			});
		}
		return sdwanCpeDet;

	}

	private IzoSdwanCpeDetails extractCpeDetails(CpeBomSdwanRulesView cpe) {
		IzoSdwanCpeDetails cpeDet = new IzoSdwanCpeDetails();
		if ("Kbps".equals(cpe.getMaxbwUom())) {
			Float bandwidth = (cpe.getMaxbw() / 1024);
			cpeDet.setBandwidthRate("Mbps");
			cpeDet.setBandwidth(Math.round(bandwidth));
			LOGGER.info("AFTER CHANGING KBPS TO MBPS BANDWIDTH {}", cpeDet.getBandwidth());
		} else if ("Gbps".equals(cpe.getMaxbwUom())) {
			Float bandwidth = (cpe.getMaxbw() * 1000);
			LOGGER.info("BEFORE  CHANGING GBPS TO MBPS BANDWIDTH {}", cpe.getMaxbw());
			cpeDet.setBandwidthRate("Mbps");
			cpeDet.setBandwidth(Math.round(bandwidth));
			LOGGER.info("AFTER  CHANGING GBPS TO MBPS BANDWIDTH {}", cpeDet.getBandwidth());
		} else {
			cpeDet.setBandwidth(Math.round(cpe.getMaxbw()));
			cpeDet.setBandwidthRate(cpe.getMaxbwUom());
		}

		cpeDet.setCpeName(cpe.getBomCd());
		cpeDet.setL2Ports(cpe.getMaxL2Ports());
		cpeDet.setL3Ports(cpe.getMaxL3Ports());
		cpeDet.setCpePriority(cpe.getCpePriority());
		cpeDet.setMaxL3Cu(cpe.getMaxL3Cu());
		cpeDet.setMaxL3Fi(cpe.getMaxL3Fi());
		cpeDet.setAddon(cpe.getAddonCd());
		cpeDet.setProfile(cpe.getLicCd());
		cpeDet.setVendor(cpe.getVendorName());
		return cpeDet;

	}

	public List<IzoSdwanCpeBomInterface> getCpeBomInterface() {
		List<IzoSdwanCpeBomInterface> cpeBomInterface = new ArrayList<>();
		try {
			List<CpeBomInterfaceSdwanView> bomInterface = bomInterfaceSdwanViewRepository.findAll();
			cpeBomInterface = getInterfaceDetail(bomInterface);
		} catch (Exception e) {
		}
		return cpeBomInterface;
	}

	List<IzoSdwanCpeBomInterface> getInterfaceDetail(List<CpeBomInterfaceSdwanView> bomInterface) {
		List<IzoSdwanCpeBomInterface> interfaceDetails = new ArrayList<>();
		LOGGER.info("interface details:{}", bomInterface);
		if (bomInterface != null && !bomInterface.isEmpty()) {
			interfaceDetails = bomInterface.stream().map(this::getDet).collect(Collectors.toList());
		}
		return interfaceDetails;
	}

	private IzoSdwanCpeBomInterface getDet(CpeBomInterfaceSdwanView bomInterface) {
		IzoSdwanCpeBomInterface interfaceDetails = new IzoSdwanCpeBomInterface();
		interfaceDetails.setBomNameCd(bomInterface.getBomNameCd());
		LOGGER.info("name:{}", bomInterface.getBomNameCd());
		interfaceDetails.setPhysicalResourceCd(bomInterface.getPhysicalResourceCd());
		interfaceDetails.setProductCategory(bomInterface.getProductCategory());
		interfaceDetails.setInterfaceType(bomInterface.getInterfaceType());
		interfaceDetails.setDescription(bomInterface.getDescription());
		interfaceDetails.setProvider(bomInterface.getProvider());
		interfaceDetails.setCpeModelEndOfSale(bomInterface.getCpeModelEndOfSale());
		interfaceDetails.setCpeModelEndOfLife(bomInterface.getCpeModelEndOfLife());
		return interfaceDetails;
	}

	public CpeDetails getCofCpeDetails(List<String> cpeName) throws TclCommonException {
		CpeDetails cpeDetails = new CpeDetails();
		// String cpeDet=(String) mqUtils.sendAndReceive(cpeNameQueue,
		// Utils.convertObjectToJson(quoteId));
		// List<String> cpeName=Utils.fromJson(cpeDet,new TypeReference<List<String>>()
		// {});
		try {
			if (cpeName != null && !cpeName.isEmpty()) {
				List<CpeBomDetails> cpeBomInfo = new ArrayList<>();
				cpeName.stream().forEach(cpe -> {
					CpeBomDetails cpeBomDetails = new CpeBomDetails();
					List<CpeSpecifications> cpeSpecs = new ArrayList<>();
					cpeBomDetails.setCpeName(cpe);
					List<IzoSdwanCpeBomInterface> uniquecpeDetails = getUniqueCpeDetails(cpe);
					for (IzoSdwanCpeBomInterface cpeBomInterface : uniquecpeDetails) {
						CpeSpecifications cpeSpecifications = new CpeSpecifications();
						cpeSpecifications.setTechnicalSpecification(cpeBomInterface.getProductCategory());
						cpeSpecifications.setPartCode(cpeBomInterface.getPhysicalResourceCd());
						cpeSpecifications.setDescription(cpeBomInterface.getDescription());
						cpeSpecs.add(cpeSpecifications);
					}
					cpeBomDetails.setCpeSpecifications(cpeSpecs);
					cpeBomInfo.add(cpeBomDetails);
				});
				cpeDetails.setCpeBomDetails(cpeBomInfo);
			}
		} catch (Exception e) {
			LOGGER.error("Error occured on getting cpe bom name details!!", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return cpeDetails;
	}

	public List<IzoSdwanCpeBomInterface> getUniqueCpeDetails(String cpe) {
		List<IzoSdwanCpeBomInterface> cpeBomInterfaces = new ArrayList<>();
		try {
			cpeBomInterfaces = getCpeBomInterface();
			if (cpe != null) {
				if (cpeBomInterfaces != null && !cpeBomInterfaces.isEmpty()) {
					cpeBomInterfaces = cpeBomInterfaces.stream()
							.sorted((IzoSdwanCpeBomInterface s1, IzoSdwanCpeBomInterface s2) -> s1.getProductCategory()
									.compareTo(s2.getProductCategory()))
							.filter(cpeName -> cpe.equalsIgnoreCase(cpeName.getBomNameCd()))
							.collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
		}
		return cpeBomInterfaces;
	}

	public String getSlaTierDetails(String siteTypeName, String cityName, String productName, String countryName, String vendorName)
			throws TclCommonException {
		String tierValue = "";
		if (siteTypeName == null && productName == null && vendorName==null) {
			LOGGER.info("site type name  vendor name prodct name is empty or null");
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String tierType = "";
			if(countryName!=null && !countryName.isEmpty() && !countryName.equalsIgnoreCase("India")) {
				tierType = vwCtryCitySlaSdwanRepository.getTierValueByCountry(countryName,vendorName);
			}
			else {
				if(cityName!=null && !cityName.isEmpty()) {
					tierType = vwCtryCitySlaSdwanRepository.getTierValue(cityName,vendorName);}
			}
			if (tierType == null) {
				return tierType;
			} else {
				switch (tierType) {
				case "T1":
					tierValue = vwSlaProductSdwanRepository.getTier1AndSiteTypeName(siteTypeName);
					break;
				case "T2":
					tierValue = vwSlaProductSdwanRepository.getTier2AndSiteTypeName(siteTypeName);
					break;
				case "T3":
					tierValue = vwSlaProductSdwanRepository.getTier3AndSiteTypeName(siteTypeName);
					break;
				case "T4":
					tierValue = vwSlaProductSdwanRepository.getTier4AndSiteTypeName(siteTypeName);
					break;
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occured on getting sdwan sla details!!", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return tierValue;
	}

	public List<String> getByonInterface() throws TclCommonException {
		List<String> interfaceType = null;
		try {
			interfaceType = vwSdwanBwIntfMappingRepository.getInterface();

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return interfaceType;
	}

	public List<VproxySolutionsBean> getVproxyProductDetails(List<VproxyAttributeDetails> vproxyAtrributeDetails) {
		List<VproxySolutionsBean> solutions = new ArrayList<>();
		for (VproxyAttributeDetails vproxySolution : vproxyAtrributeDetails) {
			if (vproxySolution.getAttributeValue().equals("Yes")) {
				getVproxySolutionDetails(vproxySolution.getAttributeName(), solutions);
			}
		}
		return solutions;
	}

	private void getVproxySolutionDetails(String solutionName, List<VproxySolutionsBean> solutions) {
		List<VproxyProductOfferingBean> vproxyProductBeans = new ArrayList<>();
		switch (solutionName) {
		case "isSPAVproxy":
			LOGGER.info("Secure Private Access Profiles");
			solutionName = "Secure Private Access";
			VproxySolutionsBean vproxySolution = new VproxySolutionsBean();
			vproxySolution.setSolutionName(solutionName);
			getProfileDetails(solutionName, vproxyProductBeans);
			vproxySolution.setVproxyProductOfferingBeans(vproxyProductBeans);
			List<VproxyQuestionnaireDet> productQuestionnarie = getProfileQuestionnarireDetails(solutionName);
			vproxySolution.setVproxyQuestionnaireDets(productQuestionnarie);
			solutions.add(vproxySolution);
			break;

		case "isSWGVproxy":
			LOGGER.info("Secure Web Gateway Profiles");
			solutionName = "Secure Web Gateway";
			VproxySolutionsBean vproxySolution2 = new VproxySolutionsBean();
			vproxySolution2.setSolutionName(solutionName);
			getProfileDetails(solutionName, vproxyProductBeans);
			vproxySolution2.setVproxyProductOfferingBeans(vproxyProductBeans);
			List<VproxyQuestionnaireDet> productQuestionnarie1 = getProfileQuestionnarireDetails(solutionName);
			vproxySolution2.setVproxyQuestionnaireDets(productQuestionnarie1);
			solutions.add(vproxySolution2);
			break;

		default:
			LOGGER.info("solution name for vproxy");
			break;
		}
	}

	// This Method will gives list of profiles
	private void getProfileDetails(String productName, List<VproxyProductOfferingBean> vproxyProductBeans) {
		List<String> storeProfiles = new ArrayList<>();
		List<VwVproxyProductOffering> vproxyProductDetails = vwVproxyProductOfferingRepository
				.findByProductOfferingName(productName);
		if (!vproxyProductDetails.isEmpty() && vproxyProductDetails != null) {
			for (VwVproxyProductOffering vproxyProductDet : vproxyProductDetails) {
				VproxyProductOfferingBean vproxyProductOfferingBean = new VproxyProductOfferingBean();
				if (storeProfiles.isEmpty()) {
					vproxyProductOfferingBean.setSolutionName(productName);
					vproxyProductOfferingBean.setProductOfferingName(vproxyProductDet.getProfileName());
					vproxyProductOfferingBean.setProductOfferingDescription(
							Arrays.asList(vproxyProductDet.getProfileDescription().split("\\r\\n")));
					// To Get List Of Addons
					LOGGER.info("Profile Name Is {}", vproxyProductDet.getProfileName());
					List<VProxyAddonsBean> vProxyAddonsBeans = getProfileAddons(vproxyProductDetails,
							vproxyProductDet.getProfileName());
					vproxyProductOfferingBean.setvProxyAddonsBeans(vProxyAddonsBeans);
					vproxyProductBeans.add(vproxyProductOfferingBean);
					storeProfiles.add(vproxyProductDet.getProfileName());
				} else {
					if ((!storeProfiles.contains(vproxyProductDet.getProfileName()))) {
						vproxyProductOfferingBean.setSolutionName(productName);
						vproxyProductOfferingBean.setProductOfferingName(vproxyProductDet.getProfileName());
						vproxyProductOfferingBean.setProductOfferingDescription(
								Arrays.asList(vproxyProductDet.getProfileDescription().split("\\r\\n")));
						List<VProxyAddonsBean> vProxyAddonsBeans = getProfileAddons(vproxyProductDetails,
								vproxyProductDet.getProfileName());
						vproxyProductOfferingBean.setvProxyAddonsBeans(vProxyAddonsBeans);
						vproxyProductBeans.add(vproxyProductOfferingBean);
						storeProfiles.add(vproxyProductDet.getProfileName());
					}
				}
			}

		}

	}

	// This Method will return List Of Addons For Profile
	private List<VProxyAddonsBean> getProfileAddons(List<VwVproxyProductOffering> vproxyProductDetails,
			String profileName) {
		List<VProxyAddonsBean> vProxyAddonsBeans = new ArrayList<>();
		for (VwVproxyProductOffering vproxyProduct : vproxyProductDetails) {
			if (vproxyProduct.getProfileName().equals(profileName)) {
				VProxyAddonsBean addon = new VProxyAddonsBean();
				LOGGER.info("Add On Name is {}", vproxyProduct.getAddonName());
				addon.setName(vproxyProduct.getAddonName());
				List<VwVproxyProductComponent> addonQues = vwVproxyProductComponentRepository
						.findByLicenseNameAndAddonName(profileName, vproxyProduct.getAddonName());
				List<String> attributeValues = new ArrayList<>();
				if (!addonQues.isEmpty() && addonQues != null && addonQues.size() != 0) {
					VproxyQuestionnaireDet vproxyQues = new VproxyQuestionnaireDet();
					if (addonQues.get(0) != null) {
						vproxyQues.setDescription(addonQues.get(0).getAttributeDesc());
						LOGGER.info("Addon Questionnarie Name is {}", addonQues.get(0).getAttributeName());
						vproxyQues.setName(addonQues.get(0).getAttributeName());
						LOGGER.info("Metric value is  {}",
								IzosdwanUtils.getAddonMetricsBasedOnQuestion(addonQues.get(0).getAttributeName()));
						vproxyQues.setMetricValue(
								IzosdwanUtils.getAddonMetricsBasedOnQuestion(addonQues.get(0).getAttributeName()));
						for (VwVproxyProductComponent questionnarieDet : addonQues) {
							if (questionnarieDet.getAttributeValue() != null
									&& !questionnarieDet.getAttributeValue().equalsIgnoreCase("NULL")) {
								attributeValues.add(questionnarieDet.getAttributeValue());
							}

						}
					}
					vproxyQues.setValues(attributeValues);
					addon.setVproxyAddonQuestionnaireDet(vproxyQues);
				}
				vProxyAddonsBeans.add(addon);
			}
		}
		return vProxyAddonsBeans;
	}

	// This is method is used to get questionnarie for profiles
	private List<VproxyQuestionnaireDet> getProfileQuestionnarireDetails(String productName) {
		List<VproxyQuestionnaireDet> profileQuestionnarie = new ArrayList<>();
		List<VwVproxyProductComponent> profileQuestionnaries = new ArrayList<>();
		List<VwVproxyProductComponent> profileQuestionnariesList = vwVproxyProductComponentRepository
				.selectProfileQuestionnarie(productName);
		for (VwVproxyProductComponent productComponent : profileQuestionnariesList) {
			if (productComponent != null) {
				if (productComponent.getAddonName() == null && productComponent.getLicenseName() == null) {
					profileQuestionnaries.add(productComponent);
				}
			}
		}
		if (!profileQuestionnaries.isEmpty() && profileQuestionnaries != null) {
			List<String> attributeValues = new ArrayList<>();
			for (VwVproxyProductComponent vproxyProfileQues : profileQuestionnaries) {
				VproxyQuestionnaireDet vproxyQues = new VproxyQuestionnaireDet();
				vproxyQues.setName(vproxyProfileQues.getAttributeName());
				vproxyQues.setDescription(vproxyProfileQues.getAttributeDesc());
				for (VwVproxyProductComponent questionnarieDet : profileQuestionnaries) {
					if (questionnarieDet.getAttributeValue() != null
							&& !questionnarieDet.getAttributeValue().equalsIgnoreCase("NULL")) {
						attributeValues.add(questionnarieDet.getAttributeValue());
					}
				}
				profileQuestionnarie.add(vproxyQues);
			}
		}
		return profileQuestionnarie;
	}

	/**
	 * 
	 * This method is used to get all vutm profiles information
	 * 
	 * @return
	 */
	public List<VutmProfileDetailsBean> getVutmProfiles() {
		List<VutmProfileDetailsBean> vutmProfileDetailsBeans = new ArrayList<>();
		List<Map<String, Object>> vwProductProfileVutms = vwVproxyProductOfferingRepository.findVutmProfiles();
		if (vwProductProfileVutms != null && !vwProductProfileVutms.isEmpty()) {
			vwProductProfileVutms.stream().forEach(vutm -> {
				VutmProfileDetailsBean vutmProfileDetailsBean = new VutmProfileDetailsBean();
				vutm.forEach((k, v) -> LOGGER.info("Key is {} and value is {}", k, v.toString()));
				String[] desc = vutm.get("profile_desc").toString().split("\r\n");
				List<String> descList = new ArrayList<>();
				for(String description:desc) {
					description = description.replace("\n", "");
					descList.add(description);
				}
				vutmProfileDetailsBean.setOfferingDescription(descList);
				vutmProfileDetailsBean.setOfferingName(vutm.get("profile_name").toString());
				vutmProfileDetailsBeans.add(vutmProfileDetailsBean);
			});
		}
		return vutmProfileDetailsBeans;
	}
	
	/**
	 * 
	 * This method is used to get all vutm breakout location information
	 * 
	 * @return
	 */
	public List<String> getVutmBreakOutLocations(){
		List<String> locations = new ArrayList<>();
		List<Map<String, Object>> details = vwVproxyProductOfferingRepository.findAttributeForVutm(IzosdwanCommonConstants.BREAK_OUT_ATTRIBUTE_NAME_CATALOG);
		if(details!=null && !details.isEmpty()) {
			details.stream().forEach(map->{
				locations.add(map.get("attribute_value").toString());
			});
		}
		return locations;
	}
	
	/**
	 * 
	 * Get Service Area Matrix for CGW by City
	 * @param location
	 * @return
	 */
	public CgwServiceAreaMatricBean getServiceAreaMatrixByLocationForCgw(String location) {
		try {
		Map<String, Object> map = vwVproxyProductOfferingRepository.findCGWServiceAreaMatrixByCity(location);
		if(map!=null && !map.isEmpty()) {
			return convertMapToBean(map);
		}
		}catch(Exception e) {
			LOGGER.error("Error on getting the service area matrix ",e);
		}
		return null;
	}
	
	/**
	 * 
	 * Get Service Area Matrix for CGW
	 * @return
	 */
	public List<CgwServiceAreaMatricBean> getServiceAreaMatrixForCgw() {
		List<CgwServiceAreaMatricBean> cgwServiceAreaMatricBeans = new ArrayList<>();
		try {
			List<Map<String, Object>> list = vwVproxyProductOfferingRepository.findCGWServiceAreaMatrix();
			if (list != null && !list.isEmpty()) {
				list.stream().forEach(map -> {
					cgwServiceAreaMatricBeans.add(convertMapToBean(map));
				});

			}
		} catch (Exception e) {
			LOGGER.error("Error on getting the service area matrix ", e);
		}
		return cgwServiceAreaMatricBeans;
	}
		 

	private CgwServiceAreaMatricBean convertMapToBean(Map<String, Object> map) {
		CgwServiceAreaMatricBean cgwServiceAreaMatricBean = new CgwServiceAreaMatricBean();
		cgwServiceAreaMatricBean.setAsno(map.get("asno").toString());
		cgwServiceAreaMatricBean.setCityName(map.get("city_nm").toString());
		cgwServiceAreaMatricBean.setHostName(map.get("host_name").toString());
		cgwServiceAreaMatricBean.setLocationId(Integer.parseInt(map.get("erf_location_id").toString()));
		cgwServiceAreaMatricBean.setPopAddress(map.get("pop_address").toString());
		cgwServiceAreaMatricBean.setPrimaryIor(map.get("primary_ior").toString());
		cgwServiceAreaMatricBean.setSecondaryIor(map.get("secondary_ior").toString());
		cgwServiceAreaMatricBean.setSerialNumber(map.get("serial_number").toString());
		cgwServiceAreaMatricBean.setSiteId(map.get("site_id").toString());
		return cgwServiceAreaMatricBean;
	}
	
	
	public List<IzosdwanBandwidthInterface> getbwinterfaceTypes(String vendor) throws TclCommonException {
		List<IzosdwanBandwidthInterface> bwInterface = new ArrayList<>();
		try {
			if(vendor.equalsIgnoreCase(CommonConstants.VERSA_VENDOR_CODE)) {
				vendor=IzosdwanCommonConstants.VERSA;	
			}
			else if(vendor.equalsIgnoreCase(CommonConstants.CISCO_VENDOR_CODE)) {
				vendor=IzosdwanCommonConstants.CISCO;
			}
			LOGGER.info("vendor:{}",vendor);
			List<VwSdwanBwIntfMapping> bwinterfaceTypes = vwSdwanBwIntfMappingRepository.findByVendor(vendor);
			if(bwinterfaceTypes!=null && !bwinterfaceTypes.isEmpty()) {
				bwinterfaceTypes.stream().forEach(interfaceType->{
					IzosdwanBandwidthInterface bandwidthInterface = new IzosdwanBandwidthInterface();
					bandwidthInterface.setInterfaceCableType(interfaceType.getInterfaceCableType());
					bandwidthInterface.setInterfaceType(interfaceType.getInterfaceType());
					if(!interfaceType.getMinBwUnit().equalsIgnoreCase("mbps")) {
						String bw = setBandwidthConversion(interfaceType.getMinBw().toString(), interfaceType.getMinBwUnit());	
						bandwidthInterface.setMinBw(Integer.valueOf(bw));
						bandwidthInterface.setMinBwUnit("Mbps");
					}
					else {
						bandwidthInterface.setMinBw(interfaceType.getMinBw());
						bandwidthInterface.setMinBwUnit(interfaceType.getMinBwUnit());
					}
					bandwidthInterface.setVendor(interfaceType.getVendor());
					bwInterface.add(bandwidthInterface);
				});
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Interfaces : {}",bwInterface);
		return bwInterface;

	}
	
	public String setBandwidthConversion(String bandwidth, String bandwidthUnit)
	{
		Double bandwidthValue=0D;
		Double bwidth = 0D;
		LOGGER.info("Bandwidth Value in setBandwidthConversion {}",bandwidth);
		LOGGER.info("Bandwidth Unit in setBandwidthConversion {}",bandwidthUnit);

		if(Objects.nonNull(bandwidth)&&Objects.nonNull(bandwidthUnit))
		{
			switch (bandwidthUnit.trim().toLowerCase())
			{
				case "kbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue / 1024;
					bandwidth = bandwidthValue.toString();
					break;
				}
				case "gbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue * 1000;
					bandwidth = bandwidthValue.toString();
					break;
				}
				default:
					break;
			}

			int index=bandwidth.indexOf(".");
			if(index>0) {
				LOGGER.info("bandwidth value" + bandwidth);
				String precisions = bandwidth.substring(index + 1);
				LOGGER.info("precision value" + precisions);
				if (precisions.length() > 3) {
					DecimalFormat df = new DecimalFormat("#.###");
					df.setRoundingMode(RoundingMode.CEILING);
					String value = df.format(bandwidthValue);
					LOGGER.info("Formatted value" + value);
					bandwidth = value;
				}
			}
			if (Character.toString(bandwidth.charAt(index+1)).equalsIgnoreCase(CommonConstants.ZERO))
			{
				bwidth = Double.parseDouble(bandwidth.trim());
				Integer bw = bwidth.intValue();
				bandwidth = bw.toString();
			}
		}
		LOGGER.info("Resultant Bandwidth in setBandwidthConversion",bandwidth);
		return bandwidth;
	}
	
	public ProductLocationBean getLocationByCountryName(String country) {
		LOGGER.info("Inside getLocationByCountryName {}", country);
		ProductLocationBean productLocationBean = new ProductLocationBean();
		try {
			List<Location> locations = locationRepository.getLocationInfoByCountryName(country);
			if (locations != null && !locations.isEmpty()) {

				Location location = locations.stream().filter(loc -> loc.getName().equalsIgnoreCase(country))
						.findFirst().orElse(null);
				if (location != null) {
					LOGGER.info("Got Location details!! name --> {} Intl Dial code --> {}", location.getName(),
							location.getIntlDialCode());
					productLocationBean.setId(location.getId());
					productLocationBean.setIntlDialCode(location.getIntlDialCode());
					productLocationBean.setName(location.getName());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on fetching location information by country name");
		}
		return productLocationBean;
	}
}
