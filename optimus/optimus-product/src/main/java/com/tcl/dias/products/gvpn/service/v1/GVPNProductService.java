package com.tcl.dias.products.gvpn.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tcl.dias.productcatelog.entity.entities.CpeBomGscDetailView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGscIntlDetailView;
import com.tcl.dias.productcatelog.entity.entities.CpeGscSelectionRule;
import com.tcl.dias.productcatelog.entity.entities.VwCpeBomPowerCableGsc;
import com.tcl.dias.productcatelog.entity.repository.CpeBomGscDetailIntlRepository;
import com.tcl.dias.productcatelog.entity.repository.CpeBomGscDetailRepository;
import com.tcl.dias.productcatelog.entity.repository.CpeGscSelectionRuleRepository;
import com.tcl.dias.productcatelog.entity.repository.VwCpeBomPowerCableGscRepository;
import com.tcl.dias.products.dto.CpeBomGscDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SLaDetailsBean;
import com.tcl.dias.common.constants.SlaConstants;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlView;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnView;
import com.tcl.dias.productcatelog.entity.entities.GvpnSlaCosView;
import com.tcl.dias.productcatelog.entity.entities.GvpnSlaView;
import com.tcl.dias.productcatelog.entity.entities.ProductSlaCosSpec;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGVPN;
import com.tcl.dias.productcatelog.entity.repository.CpeBomGvpnIntlRepository;
import com.tcl.dias.productcatelog.entity.repository.CpeBomGvpnViewRepository;
import com.tcl.dias.productcatelog.entity.repository.GvpnSlaCosViewRepository;
import com.tcl.dias.productcatelog.entity.repository.GvpnSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductSlaCosSpecRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGvpnRepository;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.constants.ProductId;
import com.tcl.dias.products.constants.SLAParameters;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.GvpnSlaRequestDto;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.netty.util.internal.StringUtil;
import org.springframework.util.CollectionUtils;

/**
 * Service class for GVPN product related operations
 * 
 * @author Paulraj
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service

public class GVPNProductService {

    public static final String SFP = "SFP";
    public static final String CPE_PORT = "CPE Port";
    @Autowired
	ServiceAreaMatrixGvpnRepository serviceAreaMatrixGvpnRepository;

	@Autowired
	GvpnSlaViewRepository gvpnSlaViewRepository;

	@Autowired
	GvpnSlaCosViewRepository gvpnSlaCosViewRepository;

	@Autowired
	ProductSlaCosSpecRepository productSlaCosSpecRepository;

	@Autowired
	CpeBomGvpnViewRepository cpeBomGvpnViewRepository;
	
	@Autowired
	CpeBomGvpnIntlRepository cpeBomGvpnIntlRepository;

	@Autowired
	CpeGscSelectionRuleRepository cpeGscSelectionRuleRepository;

	@Autowired
    VwCpeBomPowerCableGscRepository vwCpeBomPowerCableGscRepository;

	@Autowired
    CpeBomGscDetailRepository cpeBomGscDetailRepository;

    @Autowired
    CpeBomGscDetailIntlRepository cpeBomGscDetailIntlRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(GVPNProductService.class);

	/**
	 * values for GVPN product
	 * 
	 * @author Dinahar Vivekanandan
	 * @param gvpnSlaRequest
	 * @return List<SLADto>
	 * @throws TclCommonException
	 */
	public List<SLADto> getSlaValue(GvpnSlaRequestDto gvpnSlaRequest) throws TclCommonException {

		if (gvpnSlaRequest == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		List<SLADto> slaList = new ArrayList<>();
		Integer srcTier = null;

		try {
			String srcPopRegionId = gvpnSlaRequest.getPopRegionSourceId();
			Optional<ServiceAreaMatrixGVPN> optServMatrix = serviceAreaMatrixGvpnRepository
					.findByLocationId(srcPopRegionId);
			if (optServMatrix.isPresent())
				srcTier = Integer.parseInt(optServMatrix.get().getPopTierCd());

			if (srcTier == null)
				throw new TclCommonException(ExceptionConstants.TIER_CATEGORY_UNAVAILABLE,
						ResponseResource.R_CODE_BAD_REQUEST);
			slaList.add(getRoundTripDelay(gvpnSlaRequest));
			slaList.add(getServiceAvailabilityAndNoOfIncidents(gvpnSlaRequest, srcTier,
					SLAParameters.SERVICE_AVAILABILITY.getId()));
			slaList.add(getTtrAndMttrAndJitterLevel(gvpnSlaRequest, srcTier, SLAParameters.TIME_TO_RESTORE.getId()));
			slaList.add(
					getTtrAndMttrAndJitterLevel(gvpnSlaRequest, srcTier, SLAParameters.MEAN_TIME_TO_RESTORE.getId()));
			slaList.add(getServiceAvailabilityAndNoOfIncidents(gvpnSlaRequest, srcTier,
					SLAParameters.MAX_NO_OF_INCIDENTS.getId()));
			slaList.add(getTtrAndMttrAndJitterLevel(gvpnSlaRequest, srcTier,
					SLAParameters.JITTER_SERVICE_LEVEL_TGT.getId()));
			slaList.add(getPktDeliveryRatio(gvpnSlaRequest));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);

		}
		return slaList;
	}

	/**
	 * getRoundTripDelay Method to retrieve round trip delay details
	 * 
	 * @author Dinahar Vivekanandan
	 * @param gvpnSlaRequest
	 * @return
	 * @throws TclCommonException
	 */
	public SLADto getRoundTripDelay(GvpnSlaRequestDto gvpnSlaRequest) throws TclCommonException {
		String rtdValue = null;
		if (gvpnSlaRequest == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		try {
			/*
			 * List<GvpnSlaView> intermediateList = gvpnSlaViewRepository
			 * .findByFactorValueId(new BigInteger(gvpnSlaRequest.getPopRegionSourceId()));
			 * if (intermediateList.isEmpty()) throw new
			 * TclCommonException(ExceptionConstants.COMMON_ERROR,
			 * ResponseResource.R_CODE_BAD_REQUEST); List<Integer> slaIdNoList =
			 * intermediateList.stream().map(GvpnSlaView::getSlaIdNo)
			 * .collect(Collectors.toList());
			 */
			/*
			 * Optional<GvpnSlaView> rtdRecord = gvpnSlaViewRepository
			 * .findByFactorValueIdAndSlaIdNoIn(new
			 * BigInteger(gvpnSlaRequest.getPopRegionDestId()), slaIdNoList);
			 */
			/*
			 * if (rtdRecord.isPresent()) // rtdValue = rtdRecord.get().getDefaultValue();
			 * else throw new TclCommonException(ExceptionConstants.SLA_VALUE_NOT_FOUND,
			 * ResponseResource.R_CODE_NOT_FOUND);
			 */
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return new SLADto(SLAParameters.ROUND_TRIP_DELAY.getName(), rtdValue);
	}

	/**
	 * Method to retrieve service availability and Maximum Number of Incidents
	 * parameters
	 * 
	 * @param gvpnSlaRequest
	 * @return SLADto
	 * @throws TclCommonException
	 */

	public SLADto getServiceAvailabilityAndNoOfIncidents(GvpnSlaRequestDto gvpnSlaRequest, Integer srcTier,
			Integer slaParamId) throws TclCommonException {
		if (gvpnSlaRequest == null || srcTier == null || slaParamId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		List<GvpnSlaView> gvpnSlaViewList = new ArrayList<>();
		try {
			String sltVariant = gvpnSlaRequest.getSltVariant();
			String accessTopology = gvpnSlaRequest.getAccessTopology();

			if (accessTopology == null || sltVariant == null)
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			gvpnSlaViewList = gvpnSlaViewRepository.findDistinctByAccessTopologyAndSlaId(accessTopology, slaParamId);

			if (gvpnSlaViewList == null || gvpnSlaViewList.isEmpty())
				throw new TclCommonException(ExceptionConstants.SLA_VALUE_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}

		return new SLADto(gvpnSlaViewList, srcTier);
	}

	/**
	 * getTimeToRestore Method to retrieve the SLA factors, - Time to Restore, Mean
	 * Time to Restore
	 * 
	 * @param gvpnSlaRequest
	 * @param srcTier
	 * @return
	 * @throws TclCommonException
	 */
	public SLADto getTtrAndMttrAndJitterLevel(GvpnSlaRequestDto gvpnSlaRequest, Integer srcTier, Integer slaParamId)
			throws TclCommonException {
		if (gvpnSlaRequest == null || srcTier == null || slaParamId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		GvpnSlaView gvpnSlaView = null;
		try {
			String sltVariant = gvpnSlaRequest.getSltVariant();
			List<GvpnSlaView> optRecord = gvpnSlaViewRepository.findBySlaId(slaParamId);
			if (!optRecord.isEmpty())
				gvpnSlaView = optRecord.get(0);
			else
				throw new TclCommonException(ExceptionConstants.SLA_VALUE_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return new SLADto(gvpnSlaView, srcTier);
	}

	/**
	 * getPktDeliveryRatio Method to retrieve Packet delivery ratio
	 * 
	 * @author Dinahar Vivekanandan
	 * @param gvpnSlaRequest
	 * @return SLADto
	 * @throws TclCommonException
	 */
	public SLADto getPktDeliveryRatio(GvpnSlaRequestDto gvpnSlaRequest) throws TclCommonException {
		if (gvpnSlaRequest == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		SLADto slaDto = null;
		try {
			Integer cosValue = gvpnSlaRequest.getCosValue();
			String cosScheme = gvpnSlaRequest.getCosScheme();
			if (cosValue != null && cosScheme != null && !cosScheme.isEmpty()) {
				Optional<ProductSlaCosSpec> optRecord = productSlaCosSpecRepository
						.findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(ProductId.GVPN.getId(),
								SLAParameters.PKT_DELIVERY_RATIO_SERV_LEVEL_TGT.getId(), cosScheme);
				if (optRecord.isPresent())
					slaDto = new SLADto(optRecord.get(), cosValue);
				else
					throw new TclCommonException(ExceptionConstants.SLA_VALUE_NOT_FOUND,
							ResponseResource.R_CODE_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return slaDto;
	}

	public ProductSlaBean processProductSla(String request) {

		String[] spliter = request.split(",");
		String accessTopology = spliter[0];
		// factorvalue
		String tier = spliter[1].trim();

		// String accessTopology="Resilient";
		// String tier = "1";
		String tierName = StringUtil.EMPTY_STRING;
		if (tier.equals(String.valueOf(SlaConstants.TIER_ONE_1.getId()))) {
			tierName = SlaConstants.TIER_ONE.getName();
		} else if (tier.equals(String.valueOf(SlaConstants.TIER_TWO_2.getId()))) {
			tierName = SlaConstants.TIER_TWO.getName();
		} else if (tier.equals(String.valueOf(SlaConstants.TIER_THREE_3.getId()))) {
			tierName = SlaConstants.TIER_THREE.getName();
		}
		return getProductSla(accessTopology, tierName, Integer.parseInt(tier));

	}

	/**
	 * getProductSla
	 * 
	 * @param tier
	 * @param factorValue
	 * @return
	 */
	private ProductSlaBean getProductSla(String accessTopology, String tierName, Integer tierCd) {

		ProductSlaBean productSlaBean = new ProductSlaBean();

		// TODO
		// sltVariant - basic/premium/standard
		// accessTopology - Routine/Redundant/Resilient/Resilient/Routine/Unmanaged
		List<GvpnSlaView> upTimeView = gvpnSlaViewRepository.findDistinctByAccessTopologyAndSlaId(accessTopology,
				SLAParameters.SERVICE_AVAILABILITY.getId());
		if (upTimeView != null && !upTimeView.isEmpty()) {
			GvpnSlaView networkUpTime = upTimeView.get(0);
			SLaDetailsBean pBean = new SLaDetailsBean();
			pBean.setName(SLAParameters.SERVICE_AVAILABILITY.getName());
			pBean.setSlaTier(tierName);
			if (tierName.equals(SlaConstants.TIER_ONE.getName())) {
				pBean.setSlaValue(networkUpTime.getTier1Value());
			} else if (tierName.equals(SlaConstants.TIER_TWO.getName())) {
				pBean.setSlaValue(networkUpTime.getTier2Value());
			} else if (tierName.equals(SlaConstants.TIER_THREE.getName())) {
				pBean.setSlaValue(networkUpTime.getTier3Value());
			}
			pBean.setSltVariant(networkUpTime.getSltVariant());

			productSlaBean.getsLaDetails().add(pBean);

		}

		// sltVariant should be Basic/Standard/Premium
		// TODO:sltVariant should be passed here as it is mandatory for gvpn sla.
		List<GvpnSlaView> meanTimeData = gvpnSlaViewRepository.findBySlaId(SLAParameters.MEAN_TIME_TO_RESTORE.getId());
		if (!meanTimeData.isEmpty()) {

			GvpnSlaView networkUpTime = meanTimeData.get(0);
			SLaDetailsBean pBean = new SLaDetailsBean();
			pBean.setName(SLAParameters.MEAN_TIME_TO_RESTORE.getName());
			pBean.setSlaTier(tierName);
			if (tierName.equals(SlaConstants.TIER_ONE.getName())) {
				pBean.setSlaValue(networkUpTime.getTier1Value());
			} else if (tierName.equals(SlaConstants.TIER_TWO.getName())) {
				pBean.setSlaValue(networkUpTime.getTier2Value());
			} else if (tierName.equals(SlaConstants.TIER_THREE.getName())) {
				pBean.setSlaValue(networkUpTime.getTier3Value());
			}
			pBean.setSltVariant(networkUpTime.getSltVariant());
			productSlaBean.getsLaDetails().add(pBean);
		}

		// sltVariant should be Basic/Standard/Premium
		// TODO:sltVariant should be passed here as it is mandatory for gvpn sla.
		List<GvpnSlaView> timeRestoreData = gvpnSlaViewRepository.findBySlaId(SLAParameters.TIME_TO_RESTORE.getId());
		if (!timeRestoreData.isEmpty()) {

			GvpnSlaView ttrAndMttrAndJitterLevelBean = timeRestoreData.get(0);
			SLaDetailsBean pBean = new SLaDetailsBean();
			pBean.setName(SLAParameters.TIME_TO_RESTORE.getName());
			pBean.setSlaTier(tierName);
			if (tierName.equals(SlaConstants.TIER_ONE.getName())) {
				pBean.setSlaValue(ttrAndMttrAndJitterLevelBean.getTier1Value());
			} else if (tierName.equals(SlaConstants.TIER_TWO.getName())) {
				pBean.setSlaValue(ttrAndMttrAndJitterLevelBean.getTier2Value());
			} else if (tierName.equals(SlaConstants.TIER_THREE.getName())) {
				pBean.setSlaValue(ttrAndMttrAndJitterLevelBean.getTier3Value());
			}
			pBean.setSltVariant(ttrAndMttrAndJitterLevelBean.getSltVariant());
			productSlaBean.getsLaDetails().add(pBean);
		}

		// sltVariant should be Basic/Standard/Premium
		// TODO:sltVariant should be passed here as it is mandatory for gvpn sla.
		Optional<GvpnSlaCosView> jittelSrvcData = gvpnSlaCosViewRepository
				.findBySlaIdAndPopTierCd(SLAParameters.JITTER_SERVICE_LEVEL_TGT.getId(), String.valueOf(tierCd));
		if (jittelSrvcData.isPresent()) {
			String cos1Value = "";
			String cos2Value = "";
			String cosValues = "";

			GvpnSlaCosView jittelSrvcDataBean = jittelSrvcData.get();
			SLaDetailsBean pBean = new SLaDetailsBean();
			pBean.setName(SLAParameters.JITTER_SERVICE_LEVEL_TGT.getName());
			pBean.setSlaTier(tierName);
			cos1Value = jittelSrvcDataBean.getCos1Value();
			cos2Value = jittelSrvcDataBean.getCos2Value();
			if (!cos1Value.isEmpty() && !Objects.isNull(cos1Value)) {
				cosValues = cosValues.concat(cos1Value);
			}
			if (!cosValues.isEmpty() && !cos2Value.isEmpty() && !Objects.isNull(cos2Value)) {
				cosValues = cosValues.concat("," + cos2Value);
			} else if (!cos2Value.isEmpty() && !Objects.isNull(cos2Value)) {
				cosValues = cosValues.concat(cos2Value);
			}

			if (!cosValues.isEmpty()) {
				pBean.setSlaValue(cosValues);
				productSlaBean.getsLaDetails().add(pBean);
			}
		}

		Optional<GvpnSlaCosView> cosData = gvpnSlaCosViewRepository.findByCosSchemaName(SLAParameters.COS6.getName());
		if (cosData.isPresent()) {
			GvpnSlaCosView cosDataBean = cosData.get();
			SLaDetailsBean pBean = new SLaDetailsBean();
			pBean.setName(SLAParameters.PKT_DELIVERY_RATIO_SERV_LEVEL_TGT.getName());
			pBean.setSlaValue(cosDataBean.getCos1Value() + "," + cosDataBean.getCos2Value() + ","
					+ cosDataBean.getCos3Value() + "," + cosDataBean.getCos4Value() + "," + cosDataBean.getCos5Value()
					+ "," + cosDataBean.getCos6Value());
			productSlaBean.getsLaDetails().add(pBean);
		}

		return productSlaBean;
	}

	public ProductSlaBean processProductSlaWithCity(String request) {
		String[] spliter = request.split(",");
		String accessTopology = spliter[0];
		String city = spliter[1];
		ServiceAreaMatrixGVPN serviceAreaMatrixGvpn = null;
		List<ServiceAreaMatrixGVPN> serviceAresMatrix = serviceAreaMatrixGvpnRepository
				.findByCityNmContainingIgnoreCase(city);

		if (!serviceAresMatrix.isEmpty()) {
			serviceAreaMatrixGvpn = serviceAresMatrix.get(0);
			String tierName = StringUtil.EMPTY_STRING;
			String popTierId=serviceAreaMatrixGvpn.getPopTierCd()==null?"3":serviceAreaMatrixGvpn.getPopTierCd();
			if (popTierId.equals(String.valueOf(SlaConstants.TIER_ONE_1.getId()))) {
				tierName = SlaConstants.TIER_ONE.getName();
			} else if (popTierId.equals(String.valueOf(SlaConstants.TIER_TWO_2.getId()))) {
				tierName = SlaConstants.TIER_TWO.getName();
			} else if (popTierId.equals(String.valueOf(SlaConstants.TIER_THREE_3.getId()))) {
				tierName = SlaConstants.TIER_THREE.getName();
			}

			return getProductSla(accessTopology, tierName,
					Integer.parseInt(popTierId));
		}
		return getProductSla(accessTopology, SlaConstants.TIER_ONE.getName(),
				2);

	}

	/**
	 * getCpeBom
	 * 
	 * Method to retrieve CPE BOM details
	 * 
	 * @author Paulraj
	 * 
	 * @param productOfferingId
	 * @param bandwidth
	 * @param portInterfaceId
	 * @param routingProtocolId
	 * @return List<CpeBomDto>
	 * @throws TclCommonException
	 */
	public List<CpeBomDto> getCpeBom(Double bandwidth, String portInterface, String routingProtocol,
			String cpeManagementOption,String type) throws TclCommonException {
		List<CpeBomGvpnView> bomDetailsView = null;
		// Set<CpeBomDto> bomDetails = null;
		List<CpeBomDto> bomDetails = null;
		if (bandwidth == null || portInterface == null || routingProtocol == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		try {

			bomDetailsView = cpeBomGvpnViewRepository
					.findByPortInterfaceAndRoutingProtocolAndCpeManagementOptionAndMaxBwInMbpsGreaterThanEqualOrderByMaxBwInMbpsAsc(
							portInterface, routingProtocol, cpeManagementOption, bandwidth);
			if (bomDetailsView == null || bomDetailsView.isEmpty()) {
			    if("macd".equalsIgnoreCase(type))
                {
                    throw new TclCommonException(ExceptionConstants.CPE_DETAILS_EMPTY,
                            ResponseResource.R_CODE_NOT_FOUND);
                }
			    else {
                    throw new TclCommonException(ExceptionConstants.PRODUCT_LOCATIONS_EMPTY,
                            ResponseResource.R_CODE_NOT_FOUND);
                }
            }
			else {

				// bomDetails =
				// bomDetailsView.stream().map(CpeBomDto::new).collect(Collectors.toSet());
				bomDetails = bomDetailsView.stream().map(CpeBomDto::new).collect(Collectors.toList());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return bomDetails;
	}

	public String getTierCd(String cityNm) {
		String tierDefault = "3";
		boolean tierFound = false;
		List<ServiceAreaMatrixGVPN> serviceAreaMatrixGVPNList = serviceAreaMatrixGvpnRepository
				.findByCityNmContainingIgnoreCase(cityNm);

		if (serviceAreaMatrixGVPNList.isEmpty()) {
			return tierDefault;
		} else {
			for (ServiceAreaMatrixGVPN serviceAreaMatrixGVPN : serviceAreaMatrixGVPNList) {
				if (!Objects.isNull(serviceAreaMatrixGVPN.getPopTierCd())
						&& !serviceAreaMatrixGVPN.getPopTierCd().isEmpty()) {
					tierFound = true;
					return serviceAreaMatrixGVPN.getPopTierCd();
				}
			}

			if (!tierFound) {
				return tierDefault;
			}
		}

		return tierDefault;
	}

	/**
	 *
	 * getIntlCpeBom
	 * Method to retrieve CPE BOM details
	 *
	 * @param bandwidth
	 * @param portInterface
	 * @param routingProtocol
	 * @param cpeManagementOption
	 * @param cpeServiceConfig
	 * @param type
	 * @return {@link List}
	 * @throws TclCommonException
	 */
	public List<CpeBomDto> getCpeBomInternational(Double bandwidth, String portInterface, String routingProtocol,
			String cpeManagementOption, String cpeServiceConfig, String type) throws TclCommonException {
		
		List<CpeBomGvpnIntlView> bomDetailsView = null;
		List<CpeBomDto> bomDetails = null;
		if (bandwidth == null || portInterface == null || routingProtocol == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		try {

			bomDetailsView = cpeBomGvpnIntlRepository.findCpeBomIntl(portInterface, cpeManagementOption, routingProtocol, bandwidth, cpeServiceConfig);
			if (bomDetailsView == null || bomDetailsView.isEmpty()) {
			    if("macd".equalsIgnoreCase(type))
                {
                    throw new TclCommonException(ExceptionConstants.CPE_DETAILS_EMPTY,
                            ResponseResource.R_CODE_NOT_FOUND);
                }
			    else {
                    throw new TclCommonException(ExceptionConstants.PRODUCT_LOCATIONS_EMPTY,
                            ResponseResource.R_CODE_NOT_FOUND);
                }
			    
            }
			else {
				bomDetails = bomDetailsView.stream().map(CpeBomDto::new).collect(Collectors.toList());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return bomDetails;
	}

	/**
	 * Method to return list of cpe bom for gsc gvpn based on bandwidth,cubelicenses or no of mft cards
	 *
	 * @param bandwidth
	 * @param typeOfCpe
	 * @return {@link List< CpeBomGscDto >}
	 */
    public List<CpeBomGscDto> getCpeBomForGscGvpn(String bandwidth, String typeOfCpe) throws TclCommonException {
		List<CpeGscSelectionRule> suitableCpeByBandwithAndParameters = new ArrayList<>();
		List<CpeBomGscDto> suitableCpes = new ArrayList<>();
		Objects.requireNonNull(bandwidth,"Bandwidth cannot be null");
		Objects.requireNonNull(typeOfCpe,"Type of Cpe cannot be null");

		String isPassThroughFlagBasedOnTyepOfCpe = getIsPassThroughFlagBasedOnTypeOfCpe(typeOfCpe);
		suitableCpeByBandwithAndParameters = cpeGscSelectionRuleRepository.findSuitableCpeByBandWidthMbpsGreaterThanEqualAndIsPassthroughFlag(Integer.valueOf(bandwidth), isPassThroughFlagBasedOnTyepOfCpe);

		if(!CollectionUtils.isEmpty(suitableCpeByBandwithAndParameters)){
			LOGGER.info("No of suitable cpe for parameters are {} " , suitableCpeByBandwithAndParameters.size());
			suitableCpes = suitableCpeByBandwithAndParameters.stream().map(cpeGscSelectionRule -> constructCpeGscGvpnBom(cpeGscSelectionRule)).collect(Collectors.toList());
		}
		return suitableCpes;
    }

	/**
	 * Get is passthrough cpe based on type
	 * @param typeOfCpe
	 * @return
	 */
	private String getIsPassThroughFlagBasedOnTypeOfCpe(String typeOfCpe) {
		String isPassThroughFlag;
		switch (typeOfCpe){
			case "IP PBX Cube" :
				isPassThroughFlag = "N";
				break;
			case "IP PBX Passthrough Data":
				isPassThroughFlag = "Y";
				break;
			case "Customer SBC PAssthrough Data":
				isPassThroughFlag = "Y";
				break;
			case "TDM-PBX-VG":
				isPassThroughFlag = "N";
				break;
			default:
				isPassThroughFlag = "N";
		}
		return isPassThroughFlag;
	}

	/**
	 * Construct cpe gsc gvpn bom
	 *
	 * @param cpeGscSelectionRule
	 * @return {@link CpeBomGscDto}
	 */
	private CpeBomGscDto constructCpeGscGvpnBom(CpeGscSelectionRule cpeGscSelectionRule) {
		CpeBomGscDto cpeBomGscDto = new CpeBomGscDto();
		cpeBomGscDto.setBomCode(cpeGscSelectionRule.getBomCode());
		cpeBomGscDto.setBandwidthInMbps(cpeGscSelectionRule.getBandWidthMbps().toString());
		if(Objects.nonNull(cpeGscSelectionRule.getCubeSessionsNum())){
			cpeBomGscDto.setNoOfCubeLicenses(cpeGscSelectionRule.getCubeSessionsNum().toString());
		}
		if(Objects.nonNull(cpeGscSelectionRule.getMaxMFTCardNum())){
			cpeBomGscDto.setMaxMFTCards(cpeGscSelectionRule.getMaxMFTCardNum().toString());
		}
		if(Objects.nonNull(cpeGscSelectionRule.getMaxNimCardNum())) {
			cpeBomGscDto.setMaxNimModules(cpeGscSelectionRule.getMaxNimCardNum().toString());
		}
		return cpeBomGscDto;
	}


	/**
	 * Method to return list of power cables applicable against each country
	 *
	 * @param country
	 * @return {@link List< CpeBomGscDto >}
	 */
	public CpeBomGscDto getGscCpePowerCableDetails(String country) throws TclCommonException {
		CpeBomGscDto powerCablesCpe = new CpeBomGscDto();

		List<VwCpeBomPowerCableGsc> powerCablesByCountry = vwCpeBomPowerCableGscRepository.findByCountryName(country);
		if(!CollectionUtils.isEmpty(powerCablesByCountry)){
			Map<String, String> powerCableMap = powerCablesByCountry.stream().collect(Collectors.toMap(powerCable -> powerCable.getProductCode(), powerCable -> powerCable.getProductDescription(), (oldKeyValue, newKeyValue) -> newKeyValue));
			LOGGER.info("distinct power cables {}", powerCableMap.size());
			powerCablesCpe.setPowerCables(powerCableMap);
		}

		return powerCablesCpe;
	}

    /**
     * Get sfp and nim modules for gsc gvpn cpe
     * @param country
     * @param bomName
     * @param portInterface
     * @param routingProtocol
     * @return
     */
    public CpeBomGscDto getGscCpeDetails(String country, String bomName, String portInterface, String routingProtocol) {
        CpeBomGscDto gscCpeDetails = new CpeBomGscDto();
        List<CpeBomGscDetailView> allgscCpeDetails = new ArrayList<>();
        List<CpeBomGscIntlDetailView> allgscCpeIntlDetails = new ArrayList<>();

        if("India".equalsIgnoreCase(country)){
            getGscCpeDetailsOfIndia(country, bomName, portInterface, routingProtocol, gscCpeDetails);
        }
        else{
            getGscCpeDetailsOfIntl(country, bomName, portInterface, routingProtocol, gscCpeDetails);
        }

        return gscCpeDetails;
    }

    /**
     * get gsc cpe details of  intl
     *
     * @param country
     * @param bomName
     * @param portInterface
     * @param routingProtocol
     * @param gscCpeDetails
     */
    private void getGscCpeDetailsOfIntl(String country, String bomName, String portInterface, String routingProtocol, CpeBomGscDto gscCpeDetails) {
        List<CpeBomGscIntlDetailView> allgscCpeIntlDetails;
        LOGGER.info("Parameters for cpe bom details for gsc gvpn are, country : {}, bomname : {}, port interface : {}, routing protocal : {}", country, bomName, portInterface, routingProtocol);
        allgscCpeIntlDetails = cpeBomGscDetailIntlRepository.findByCountryAndBomNameAndPortInterfaceAndRoutingProtocol(country,bomName, portInterface, routingProtocol);
        if(!CollectionUtils.isEmpty(allgscCpeIntlDetails)){
            Map<String, String> sfpModules = allgscCpeIntlDetails.stream().filter(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCategory().equalsIgnoreCase(SFP)).collect(Collectors.toMap(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCode(), cpeBomGscDetailView -> cpeBomGscDetailView.getLongDesc(), (oldKeyValue, newKeyValue) -> newKeyValue));
            Map<String, String> nimModules = allgscCpeIntlDetails.stream().filter(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCategory().equalsIgnoreCase(CPE_PORT)).collect(Collectors.toMap(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCode(), cpeBomGscDetailView -> cpeBomGscDetailView.getLongDesc(), (oldKeyValue, newKeyValue) -> newKeyValue));

            LOGGER.info("List of sfp modules intl {}", sfpModules.size());
            LOGGER.info("List of nim modules intl {}", nimModules.size());
            gscCpeDetails.setNimModules(nimModules);
            gscCpeDetails.setSfpModules(sfpModules);
        }
    }

    /**
     * get gsc cpe details of india
     *
     * @param country
     * @param bomName
     * @param portInterface
     * @param routingProtocol
     * @param gscCpeDetails
     */
    private void getGscCpeDetailsOfIndia(String country, String bomName, String portInterface, String routingProtocol, CpeBomGscDto gscCpeDetails) {
        List<CpeBomGscDetailView> allgscCpeDetails;
        LOGGER.info("Parameters for cpe bom details for gsc gvpn are, country : {}, bomname : {}, port interface : {}, routing protocal : {}", country, bomName, portInterface, routingProtocol);
        allgscCpeDetails = cpeBomGscDetailRepository.findByBomNameAndPortInterfaceAndRoutingProtocol(bomName, portInterface, routingProtocol);
        if(!CollectionUtils.isEmpty(allgscCpeDetails)){
            Map<String, String> sfpModules = allgscCpeDetails.stream().filter(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCategory().equalsIgnoreCase(SFP)).collect(Collectors.toMap(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCode(), cpeBomGscDetailView -> cpeBomGscDetailView.getLongDesc(), (oldKeyValue, newKeyValue) -> newKeyValue));
            Map<String, String> nimModules = allgscCpeDetails.stream().filter(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCategory().equalsIgnoreCase(CPE_PORT)).collect(Collectors.toMap(cpeBomGscDetailView -> cpeBomGscDetailView.getProductCode(), cpeBomGscDetailView -> cpeBomGscDetailView.getLongDesc(), (oldKeyValue, newKeyValue) -> newKeyValue));

            LOGGER.info("List of sfp modules {}", sfpModules.size());
            LOGGER.info("List of nim modules {}", nimModules.size());
            gscCpeDetails.setNimModules(nimModules);
            gscCpeDetails.setSfpModules(sfpModules);
        }
    }
}
