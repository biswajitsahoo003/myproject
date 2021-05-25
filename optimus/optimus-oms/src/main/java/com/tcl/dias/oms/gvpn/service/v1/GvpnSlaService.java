package com.tcl.dias.oms.gvpn.service.v1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SLaDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.GvpnSlaConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.LeProductSla;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.repository.LeProductSlaRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.dias.oms.gvpn.beans.GvpnCosBean;
import com.tcl.dias.oms.gvpn.beans.GvpnOfferingName;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.springframework.util.CollectionUtils;

/**
 * This file contains the GvpnSlaService related Service
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class GvpnSlaService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnSlaService.class);

	@Autowired
	private LeProductSlaRepository leProductSlaRepository;

	@Autowired
	private QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	private SlaMasterRepository slaMasterRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.gvpn.sla.queue}")
	String producSlaQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String poplocationQueue;

	@Value("${rabbitmq.gvpn.sla.city.queue}")
	String productSlaCityQueue;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Value("${standard.value}")
	String standardValue;

	@Value("${premium.value}")
	String premiumValue;

	/**
	 * @author VIVEK KUMAR K saveSla
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void saveSla(QuoteToLe quoteToLe) throws TclCommonException {


		try {
			List<QuoteToLeProductFamily> mstProductFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			mstProductFamily.stream().forEach(quoteToLeProductFamily -> processQuoteLe(quoteToLe,
					quoteToLeProductFamily.getMstProductFamily().getName()));

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.SAVE_SLA_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void processQuoteLe(QuoteToLe quoteLE, String familName) {

		quoteLE.getQuoteToLeProductFamilies().stream().forEach(quoProd -> processProductSolutions(quoProd, familName));
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ processProductSolutions used to get
	 *       the product solution details
	 * @param illSiteDtos
	 * @param quoProd
	 * @param leProductSla
	 * @param familName
	 * @param productBean
	 */
	private void processProductSolutions(QuoteToLeProductFamily quoProd, String familName) {
		if (quoProd.getMstProductFamily().getName().equals(familName)) {
			quoProd.getProductSolutions().stream().forEach(prodSol -> processIllSites(prodSol));
		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ processIllSites used to process the
	 *       ill site with sla details
	 * @param illSiteDtos
	 * @param prodSol
	 * @param leProductSla
	 * @param productBean
	 */
	private void processIllSites(ProductSolution prodSol) {
		prodSol.getQuoteIllSites().stream().forEach(ill -> processSLa(ill));

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ processSiteTaxExempted this is used
	 *       to process the ill site with Product sla
	 * @param illSiteDtos
	 * @param ill
	 * @param leProductSla
	 * @param productBean
	 */
	public void processSLa(QuoteIllSite ill) {
		if (ill.getStatus() == 1) {
			try {
				constructIllSiteSla(ill);
			} catch (TclCommonException e) {
				LOGGER.error("error in process Sla", e);

			}

		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getLeProductSla used to get the
	 *       LeProductSla
	 * @param customerDetails
	 * @param quoteToLe
	 * @param mstProductFamily
	 * @return
	 */
	@Transactional
	public List<LeProductSla> getLeProductSla(List<CustomerDetail> customerDetails, QuoteToLe quoteToLe,
			MstProductFamily mstProductFamily)  {
		List<LeProductSla> leProductSla = null;
		if (quoteToLe.getErfCusCustomerLegalEntityId() != null) {
			leProductSla = leProductSlaRepository.findByErfCustomerLeIdAndMstProductFamily(
					quoteToLe.getErfCusCustomerLegalEntityId(), mstProductFamily);
		}

		if (leProductSla == null || leProductSla.isEmpty()) {
			leProductSla = getLeProductSlaFromCustomer(customerDetails, mstProductFamily);
		}

		return leProductSla;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getLeProductSlaFromCustomer used to
	 *       get Sla details based on customer
	 * @param customerDetails
	 * @param quoteToLe
	 * @return
	 */
	private List<LeProductSla> getLeProductSlaFromCustomer(List<CustomerDetail> customerDetails,
			MstProductFamily mstProductFamily) {
		List<LeProductSla> leProductSlas = null;
		if (customerDetails != null && !customerDetails.isEmpty()) {
			CustomerDetail detail = customerDetails.get(0);
			if (detail != null) {
				leProductSlas = leProductSlaRepository.findByErfCustomerIdAndMstProductFamily(detail.getErfCustomerId(),
						mstProductFamily);
			}

		}
		return leProductSlas;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructIllSiteSla used to
	 *       construct Site sla detials
	 * @param leProductSla
	 * @param quoteIllSite
	 * @param productBean
	 */
	public void constructIllSiteSla(QuoteIllSite quoteIllSite) throws TclCommonException {
		if (inPopAvailable(quoteIllSite)) {
			constructDataFromProductSla(quoteIllSite);
		} else {
			processSlaFromCity(quoteIllSite);
		}
	}

	/**
	 * @author VIVEK KUMAR K processSlaFromCity
	 * @param quoteIllSite
	 * @throws TclCommonException
	 */

	private void processSlaFromCity(QuoteIllSite quoteIllSite) throws TclCommonException {

		ProductSlaBean productBean = null;
		LOGGER.info("MDC Filter token value in before Queue call processSlaFromCity {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteIllSite.getErfLocSitebLocationId()));
		AddressDetail addressDetail = null;
		if(StringUtils.isNotBlank(locationResponse)) {
			addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		}
		String cityName = "";
		if (addressDetail != null) {
			cityName = addressDetail.getCity();
		}
		if(quoteIllSite.getProductSolution() != null && quoteIllSite.getProductSolution().getProductProfileData() != null) {
		GvpnOfferingName gvpnOffBean = (GvpnOfferingName) Utils
				.convertJsonToObject(quoteIllSite.getProductSolution().getProductProfileData(), GvpnOfferingName.class);
		
		String accessTopology = getAccessTopology(gvpnOffBean.getOfferingName(), null, null);

        //For gvpn along with gsc, accessTopology will always be REDUNDANT - changes
        //for gsc gvpn only if cpe of TDM type, then access type cannot be changed in UI, so making it as redundant.
        if(quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode().contains("GSC")){
            accessTopology = updateAccessTopologyForGscGvpn(quoteIllSite, accessTopology);
        }
        else{
            updateAccessTopology(quoteIllSite, accessTopology);
        }

		String slaRequest = accessTopology + CommonConstants.COMMA + cityName + CommonConstants.COMMA
				+ GvpnConstants.GVPN;
		LOGGER.info("MDC Filter token value in before Queue call processSlaFromCity {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String response = (String) mqUtils.sendAndReceive(productSlaCityQueue, slaRequest);
		LOGGER.info("Output Payload for for sla with city {}", response);
		if (response != null) {
			productBean = (ProductSlaBean) Utils.convertJsonToObject(response, ProductSlaBean.class);
		}
		if (productBean != null) {
			saveSlaDetails(productBean,quoteIllSite);
		}
		}
	}

    /**
     * update access topology for gsc gvpn
     *
     * @param quoteIllSite
     * @param accessTopology
     * @return
     */
    private String updateAccessTopologyForGscGvpn(QuoteIllSite quoteIllSite, String accessTopology) {
        List<QuoteProductComponent> cpeComponents = quoteProductComponentRepository
                .findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(),
                        "CPE", QuoteConstants.GVPN_SITES.toString());
        if (!CollectionUtils.isEmpty(cpeComponents)) {
            List<QuoteProductComponentsAttributeValue> cpeAttributes = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(cpeComponents.get(0));
            if (!CollectionUtils.isEmpty(cpeAttributes)) {
                Optional<QuoteProductComponentsAttributeValue> typeOfCpeAttribute = cpeAttributes.stream().filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase("Type Of Cpe"))
                        .findAny();
                if (typeOfCpeAttribute.isPresent() && typeOfCpeAttribute.get().getAttributeValues().equalsIgnoreCase("TDM_PBX_VG")) {
                    LOGGER.info("For TDM, access topology should be TDM");
                    accessTopology = GvpnConstants.REDUNDANT;
                    updateAccessTopology(quoteIllSite, accessTopology);
                }
            }
        }
        return accessTopology;
    }

    /**
	 * saveSlaDetails
	 * 
	 * @param productBean
	 * @param quoteIllSite
	 */
	private void saveSlaDetails(ProductSlaBean productBean, QuoteIllSite quoteIllSite) throws TclCommonException {

		List<SLaDetailsBean> slaDetailBeans = productBean.getsLaDetails();
		slaDetailBeans.forEach(sLaDetailsBean -> {
			try {

				if (sLaDetailsBean.getName().equalsIgnoreCase(GvpnSlaConstants.SERVICE_AVAILABILITY.getName())) {
					persistSlaDetail(quoteIllSite, sLaDetailsBean.getSlaValue(),
							GvpnSlaConstants.SERVICE_AVAILABILITY.getName());
				}

				if (sLaDetailsBean.getName().equalsIgnoreCase(GvpnSlaConstants.MEAN_TIME_TO_RESTORE.getName())) {
					persistSlaDetail(quoteIllSite, sLaDetailsBean.getSlaValue(),
							GvpnSlaConstants.MEAN_TIME_TO_RESTORE.getName());
				}

				if (sLaDetailsBean.getName().equalsIgnoreCase(GvpnSlaConstants.TIME_TO_RESTORE.getName())) {
					persistSlaDetail(quoteIllSite, sLaDetailsBean.getSlaValue(),
							GvpnSlaConstants.TIME_TO_RESTORE.getName());
				}

				if (sLaDetailsBean.getName().equalsIgnoreCase(GvpnSlaConstants.JITTER_SERVICE_LEVEL_TGT.getName())) {
					String valuesToPersist = getOnlyQuoteSelectedCosSLAJitter(quoteIllSite,
							sLaDetailsBean.getSlaValue());
					persistSlaDetail(quoteIllSite, valuesToPersist,
							GvpnSlaConstants.JITTER_SERVICE_LEVEL_TGT.getName());
				}

				if (sLaDetailsBean.getName()
						.equalsIgnoreCase(GvpnSlaConstants.PKT_DELIVERY_RATIO_SERV_LEVEL_TGT.getName())) {
					String valuesToPersist = getOnlyQuoteSelectedCosSLA(quoteIllSite, sLaDetailsBean.getSlaValue());
					persistSlaDetail(quoteIllSite, valuesToPersist,
							GvpnSlaConstants.PKT_DELIVERY_RATIO_SERV_LEVEL_TGT.getName());
				}
				
				if (sLaDetailsBean.getSltVariant() != null) {
					persistOrUpdateSltVariant(quoteIllSite, sLaDetailsBean.getSltVariant());
				}
				
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}

		});

	}

	/**
	 * @author VIVEK KUMAR K inPopAvailable
	 * @param quoteIllSite
	 * @return
	 */

	private boolean inPopAvailable(QuoteIllSite quoteIllSite) {
		return (quoteIllSite.getErfLocSiteaSiteCode() != null);
	}


	/**
	 * @author VIVEK KUMAR K persistSlaDetail
	 * 
	 * @param quoteIllSite
	 * @param slaValue
	 */
	private void persistSlaDetail(QuoteIllSite quoteIllSite, String slaValue, String slaAttri) {
		SlaMaster masterOpt = slaMasterRepository.findBySlaName(slaAttri);
		if (masterOpt == null)
			masterOpt = createSLaMaster(slaAttri);
		// Optional<SlaMaster> masterOpt =
		// Optional.of(slaMasterRepository.findBySlaName(slaAttri));
		final SlaMaster master = masterOpt;
		List<QuoteIllSiteSla> quoteIllSiteSlas = quoteIllSiteSlaRepository.findByQuoteIllSiteAndSlaMaster(quoteIllSite,
				master);
		if (quoteIllSiteSlas != null && !quoteIllSiteSlas.isEmpty()) {
			quoteIllSiteSlas.stream().forEach(quoteIllSiteSla -> {
				quoteIllSiteSla.setQuoteIllSite(quoteIllSite);
				quoteIllSiteSla.setSlaMaster(master);
				quoteIllSiteSla.setSlaValue(slaValue);
				quoteIllSiteSlaRepository.save(quoteIllSiteSla);
			});
		} else {
			QuoteIllSiteSla illSiteSla = new QuoteIllSiteSla();
			illSiteSla.setQuoteIllSite(quoteIllSite);
			illSiteSla.setSlaMaster(master);
			illSiteSla.setSlaValue(slaValue);
			quoteIllSiteSlaRepository.save(illSiteSla);
		}
	}

	/**
	 * createSLaMaster
	 * 
	 * @param slaAttri
	 */
	private SlaMaster createSLaMaster(String slaAttri) {
		SlaMaster master = new SlaMaster();
		master.setSlaName(slaAttri);
		return slaMasterRepository.save(master);
	}

	/**
	 * @author VIVEK KUMAR K constructDataFromProductSla
	 * @param quoteIllSite
	 */

	private void constructDataFromProductSla(QuoteIllSite quoteIllSite) {
		try {
			String offeringName = quoteIllSite.getProductSolution().getMstProductOffering().getProductName();
			ProductSlaBean productBean = null;
			String primaryCityTier = "";
			String secondaryCityTier = "";

			List<QuoteProductComponent> components = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(),
							IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.GVPN_SITES.toString());

			for (QuoteProductComponent quoteProductComponent : components) {
				List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());

				for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
					Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
							.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
					if (prodAttrMaster.isPresent()) {
						if (prodAttrMaster.get().getName().equals(GvpnConstants.GVPN_CITY_TIER_PRIMARY)) {
							if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
								primaryCityTier = quoteProductComponentsAttributeValue.getAttributeValues().trim();
						}  else if (prodAttrMaster.get().getName().equals(GvpnConstants.GVPN_CITY_TIER_SECONDARY)) {

							if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
								secondaryCityTier = quoteProductComponentsAttributeValue.getAttributeValues().trim();
						}
					}
				}
			}

			primaryCityTier = getTierValues(primaryCityTier);
			if (!secondaryCityTier.isEmpty()) {
				secondaryCityTier = getTierValues(secondaryCityTier);
			}

			String accessTopology = "";
			String highestTierValue = "";
			if (!secondaryCityTier.isEmpty()) {
				accessTopology = getAccessTopology(offeringName, primaryCityTier, secondaryCityTier);
				highestTierValue = getHighestTierValue(primaryCityTier, secondaryCityTier);
			} else {
				accessTopology = getAccessTopology(offeringName, primaryCityTier, null);
				highestTierValue = getHighestTierValue(primaryCityTier, null);
			}

			//For gvpn along with gsc, accessTopology will always be REDUNDANT - changes
			//for gsc gvpn only if cpe of TDM type, then access type cannot be changed in UI, so making it as redundant.
			if(quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getQuoteCode().contains("GSC")){
                accessTopology = updateAccessTopologyForGscGvpn(quoteIllSite, accessTopology);
            }
			else{
				updateAccessTopology(quoteIllSite, accessTopology);
			}

			String slaRequest = accessTopology + CommonConstants.COMMA + highestTierValue;
			LOGGER.info("MDC Filter token value in before Queue call constructDataFromProductSla {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(producSlaQueue, slaRequest);
			LOGGER.info("Output Payload for for sla with tier {}", response);
			if (StringUtils.isNotBlank(response)) {
				productBean = (ProductSlaBean) Utils.convertJsonToObject(response, ProductSlaBean.class);
			}
			if (productBean != null) {

				saveSlaDetails(productBean, quoteIllSite);

			}
		} catch (Exception e) {
			LOGGER.error("error in getting sla detailsa details ", e);
		}

	}

	/**
	 * updateAccessTopology
	 * 
	 * @param quoteIllSite
	 * @param accessTopology
	 */
	private void updateAccessTopology(QuoteIllSite quoteIllSite, String accessTopology) {
		LOGGER.info("Updating the accesstopology as {} for quote ill site id{}", accessTopology, quoteIllSite.getId());

		try {
			if (!StringUtils.isBlank(accessTopology)) {
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), "GVPN Common",QuoteConstants.GVPN_SITES.toString());

				if (!quoteProductComponents.isEmpty()) {
					quoteProductComponents.stream().forEach(quoteProductComponent -> {

						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
										quoteProductComponent.getId(),
										"Access Topology ");
						if (!quoteProductComponentsAttributeValues.isEmpty()) {
							quoteProductComponentsAttributeValues.stream()
									.forEach(quoteProductComponentsAttributeValue -> {
										LOGGER.info("Existing accesstopology is {}", quoteProductComponentsAttributeValue.getAttributeValues());
										quoteProductComponentsAttributeValue.setAttributeValues(accessTopology);
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									});
						}

					});

				}
			}
		} catch (Exception e) {

			LOGGER.error("erro with access topology", e);
		}
	}

	private String getTierValues(String tierResponse) {
		switch (tierResponse) {
		case "Non_Tier1":
			return "1";
		case "Non_Tier2":
			return "2";
		case "Non_Tier3":
			return "3";
		case "Tier1":
			return "1";
		case "Tier2":
			return "2";
		case "Tier3":
			return "3";
		case "1":
			return "1";
		case "2":
			return "2";
		case "3":
			return "3";
		default:
			return "3";
		}
	}

	/**
	 * getAccessTopology - Method to get the access toplogy for Gvpn service
	 * availablity SLA based on the primary & secondary tiers along with profiles
	 * selected for the particular site.
	 * 
	 * @param offeringName
	 * @param primaryTier
	 * @param secondaryTier
	 * @return
	 * @author NAVEEN GUNASEKARAN
	 */
	private String getAccessTopology(String offeringName, String primaryTier, String secondaryTier) {
		String accessTopology = "";
		switch (offeringName.toLowerCase()) {
		case GvpnConstants.SINGLE_UNMANGED_GVPN:
			accessTopology = GvpnConstants.UNMANAGED;
			break;
		case GvpnConstants.DUAL_UNMANGED_GVPN:
			if (null != primaryTier && null != secondaryTier) {
				if (primaryTier.equalsIgnoreCase(GvpnConstants.TIER3)
						&& secondaryTier.equalsIgnoreCase(GvpnConstants.TIER3)) {
					accessTopology = GvpnConstants.RESILIENT;
				} else {
					accessTopology = GvpnConstants.REDUNDANT;
				}
			} else {
				accessTopology = GvpnConstants.RESILIENT;
			}
			break;
		case GvpnConstants.SINGLE_MANGED_GVPN:
			accessTopology = GvpnConstants.ROUTINE;
			break;

		case GvpnConstants.DUAL_MANGED_GVPN:
			if (null != primaryTier && null != secondaryTier) {
				if (primaryTier.equalsIgnoreCase(GvpnConstants.TIER3)
						&& secondaryTier.equalsIgnoreCase(GvpnConstants.TIER3)) {
					accessTopology = GvpnConstants.RESILIENT;
				} else {
					accessTopology = GvpnConstants.REDUNDANT;
				}
			} else {
				accessTopology = GvpnConstants.RESILIENT;
			}
			break;
		}
		return accessTopology;
	}

	private String getHighestTierValue(String tier1Value, String tier2Value) {
		String higestetTierValue = "";
		if (!Objects.isNull(tier1Value) && !Objects.isNull(tier2Value)) {
			Integer tier1 = Integer.valueOf(tier1Value);
			Integer tier2 = Integer.valueOf(tier2Value);
			// if tier 1 & 3 comes then hightest tier value is tier 1 here.
			if (tier1 > tier2) {
				higestetTierValue = tier2.toString();
			} else {
				higestetTierValue = tier1.toString();
			}
		} else if (!Objects.isNull(tier1Value)) {
			higestetTierValue = tier1Value;
		} else {
			higestetTierValue = tier2Value;
		}

		if (StringUtils.isEmpty(higestetTierValue)) {
			higestetTierValue = "3";
		}
		return higestetTierValue;
	}

	private String getOnlyQuoteSelectedCosSLA(QuoteIllSite quoteIllSite, String pdrValue) throws TclCommonException {
		List<QuoteProductComponent> quoteProdCompList = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), GvpnConstants.GVPN_COMMON,QuoteConstants.GVPN_SITES.toString());

		List<Integer> quoteProdCompIdList = quoteProdCompList.stream().map(QuoteProductComponent::getId)
				.collect(Collectors.toList());

		List<String> cosNamesList = new ArrayList<>();
		// for cos 6 list creation
		for (int i = 1; i <= 6; i++) {
			cosNamesList.add(GvpnConstants.COS + i);
		}
		Map<String, String> cosProfileValueMap = new LinkedHashMap<>();

		for (String cosKey : cosNamesList) {
			List<QuoteProductComponentsAttributeValue> quoteProdCompAttrValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdInAndProductAttributeMaster_Name(quoteProdCompIdList, cosKey);
			if (!quoteProdCompAttrValues.isEmpty()) {
				if (null != quoteProdCompAttrValues.get(0).getAttributeValues()
						&& !quoteProdCompAttrValues.get(0).getAttributeValues().isEmpty()) {
					int posValue = Integer.parseInt(
							quoteProdCompAttrValues.get(0).getAttributeValues().replaceAll(GvpnConstants.PERCENT, ""));
					if (posValue > 0) {
						cosProfileValueMap.put(cosKey, posValue + GvpnConstants.PERCENT);
					}
				}
			}
		}

		Map<String, String> cosSlaValuesMap = new LinkedHashMap<>();
		if (pdrValue.contains(CommonConstants.COMMA)) {
			String[] splitter = pdrValue.split(CommonConstants.COMMA);
			for (int i = 1; i <= splitter.length; i++) {
				cosSlaValuesMap.put(GvpnConstants.COS + i, splitter[i - 1]);
			}
		}

		List<GvpnCosBean> gvpnCosBeanLists = new ArrayList<>();
		StringBuilder valuesToPersist = new StringBuilder();
		for (Entry<String, String> eachProfileMap : cosProfileValueMap.entrySet()) {
			for (Entry<String, String> eachSlaMap : cosSlaValuesMap.entrySet()) {
				// getting only the selected cos sla from product catalog values.
				if (eachProfileMap.getKey().equalsIgnoreCase(eachSlaMap.getKey())) {
					GvpnCosBean gvpnCosBean = new GvpnCosBean();
					gvpnCosBean.setCosKey(eachSlaMap.getKey());
					gvpnCosBean.setCosValue(eachSlaMap.getValue());
					gvpnCosBeanLists.add(gvpnCosBean);
				}
			}
		}

		valuesToPersist.append(Utils.convertObjectToJson(gvpnCosBeanLists));
		return valuesToPersist.toString();
	}

	private String getOnlyQuoteSelectedCosSLAJitter(QuoteIllSite quoteIllSite, String jitterValue)
			throws TclCommonException {

		String cos1Value = "";
		String cos2Value = "";
		// jitter cos 1 & cos 2 profile logic changes
		Map<String, String> cosSlaValuesMap = new LinkedHashMap<>();
		if (jitterValue.contains(CommonConstants.COMMA)) {
			String[] slaCosSplit = jitterValue.split(CommonConstants.COMMA);
			cos1Value = slaCosSplit[0];
			cos2Value = slaCosSplit[1];
			cosSlaValuesMap.put(GvpnConstants.COS1, cos1Value);
			cosSlaValuesMap.put(GvpnConstants.COS2, cos2Value);
		} else {
			cos1Value = jitterValue;
			cosSlaValuesMap.put(GvpnConstants.COS1, cos1Value);
		}

		List<QuoteProductComponent> quoteProdCompList = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), GvpnConstants.GVPN_COMMON,QuoteConstants.GVPN_SITES.toString());

		List<Integer> quoteProdCompIdList = quoteProdCompList.stream().map(eachProdComp -> eachProdComp.getId())
				.collect(Collectors.toList());

		List<String> cosNamesList = new ArrayList<>();
		// for cos 6 list creation
		for (int i = 1; i <= 2; i++) {
			cosNamesList.add(GvpnConstants.COS + i);
		}
		Map<String, String> cosProfileValueMap = new LinkedHashMap<>();

		for (String cosKey : cosNamesList) {
			List<QuoteProductComponentsAttributeValue> quoteProdCompAttrValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdInAndProductAttributeMaster_Name(quoteProdCompIdList, cosKey);
			if (!quoteProdCompAttrValues.isEmpty()) {
				if (null != quoteProdCompAttrValues.get(0).getAttributeValues()
						&& !quoteProdCompAttrValues.get(0).getAttributeValues().isEmpty()) {
					int posValue = Integer.parseInt(
							quoteProdCompAttrValues.get(0).getAttributeValues().replaceAll(GvpnConstants.PERCENT, ""));
					if (posValue > 0) {
						cosProfileValueMap.put(cosKey, posValue + GvpnConstants.PERCENT);
					}
				}
			}
		}

		List<GvpnCosBean> gvpnCosBeanLists = new ArrayList<>();
		StringBuilder valuesToPersist = new StringBuilder();
		for (Entry<String, String> eachProfileMap : cosProfileValueMap.entrySet()) {
			for (Entry<String, String> eachSlaMap : cosSlaValuesMap.entrySet()) {
				// getting only the selected cos sla from product catalog values.
				if (eachProfileMap.getKey().equalsIgnoreCase(eachSlaMap.getKey())) {
					GvpnCosBean gvpnCosBean = new GvpnCosBean();
					gvpnCosBean.setCosKey(eachSlaMap.getKey());
					gvpnCosBean.setCosValue(eachSlaMap.getValue());
					gvpnCosBeanLists.add(gvpnCosBean);
				}
			}
		}

		valuesToPersist.append(Utils.convertObjectToJson(gvpnCosBeanLists));
		return valuesToPersist.toString();
	}
	
	private void persistOrUpdateSltVariant(QuoteIllSite quoteIllSite, String sltVariant) {

		try {
			if (!StringUtils.isBlank(sltVariant)) {
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), "GVPN Common",QuoteConstants.GVPN_SITES.toString());

				if (!quoteProductComponents.isEmpty()) {
					quoteProductComponents.stream().forEach(quoteProductComponent -> {

						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
										quoteProductComponent.getId(),
										GvpnConstants.SLT_VARIANT);
						if (!quoteProductComponentsAttributeValues.isEmpty()) {
							quoteProductComponentsAttributeValues.stream()
									.forEach(quoteProductComponentsAttributeValue -> {
										quoteProductComponentsAttributeValue.setAttributeValues(sltVariant);
										quoteProductComponentsAttributeValueRepository
												.save(quoteProductComponentsAttributeValue);
									});
						} else {
							List<ProductAttributeMaster> productAttributeMaster = productAttributeMasterRepository
									.findByNameAndStatus(GvpnConstants.SLT_VARIANT, CommonConstants.BACTIVE);
							if (productAttributeMaster != null && !productAttributeMaster.isEmpty()) {
								QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
								quoteProductComponentsAttributeValue.setAttributeValues(sltVariant);
								quoteProductComponentsAttributeValue.setDisplayValue(sltVariant);
								quoteProductComponentsAttributeValue
										.setProductAttributeMaster(productAttributeMaster.get(0));
								quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
								quoteProductComponentsAttributeValueRepository
										.save(quoteProductComponentsAttributeValue);
							}
						}

					});

				}
			}
		} catch (Exception e) {

			LOGGER.error("erro with access topology", e);
		}
	}
}
