package com.tcl.dias.products.iwan.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tcl.dias.productcatelog.entity.entities.*;
import com.tcl.dias.productcatelog.entity.repository.*;
import com.tcl.dias.products.constants.DataCenterType;
import com.tcl.dias.products.dto.DataCenterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SLaDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.SlaConstants;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.constants.SLAParameters;
import com.tcl.dias.products.constants.ServiceVariants;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.netty.util.internal.StringUtil;

/**
 * 
 *
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class IWANProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IWANProductService.class);

	@Autowired
	IasPriceBookRepository iasPriceBookRepository;

	@Autowired
	ServiceAreaMatrixIASRepository serviceAreaMatrixIASRepository;

	@Autowired
	ProductServiceAreaMatrixIASRepository productServiceAreaMatrixIASRepository;

	@Autowired
	IasSLAViewRepository slaViewRepository;
	
	@Autowired
	VwProductSlaSpecIASRepository vwProductSlaSpecIASRepository;

	@Autowired
	IasDataCenterRepository iasDataCenterRepository;

	/**
	 * Method to fetch pricing details
	 * 
	 * @author Dinahar Vivekanandan
	 * @return List<IasPriceBook>
	 * @throws TclCommonException
	 */
	public List<IasPriceBook> getPrice() throws TclCommonException {
		List<IasPriceBook> priceList = new ArrayList<>();
		try {
			priceList = iasPriceBookRepository.findAll();
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return priceList;
	}

	/**
	 * 
	 * getSlaValue - Method to fetch SLA Values
	 *
	 * TO DO: The City id will be repaced by POP region id based on which Tier
	 * categorisation is done. Based on the user inputs (lat/long) feasibility
	 * engine will pick the nearest POP, based on that POP id Tier categorisation
	 * will be done
	 *
	 * @author Dinahar Vivekanandan
	 * 
	 * @param productOfferingId
	 * @param serviceVariantId
	 * @param cityId
	 * @param accessTypeId
	 * @param destinationId
	 * @return List<SLADto>
	 * @throws TclCommonException
	 */
	public List<SLADto> getSlaValue(Integer productOfferingId, Integer serviceVariantId, Integer popRegionId,
			Integer accessTypeId, Integer destinationId) throws TclCommonException {

		List<SLADto> slaList = new ArrayList<>();
		Integer tierId = null;

		try {
			if (Objects.isNull(productOfferingId) || Objects.isNull(serviceVariantId) || Objects.isNull(popRegionId)
					|| Objects.isNull(accessTypeId) || Objects.isNull(destinationId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

			/*
			 * Get tier id based on POP region id
			 */
			Optional<ProductServiceAreaMatrixIAS> productServiceAreaMatrixIAS = productServiceAreaMatrixIASRepository
					.findByLocationId(popRegionId);
			if (productServiceAreaMatrixIAS.isPresent())
				tierId = productServiceAreaMatrixIAS.get().getTierId();
			else
				throw new TclCommonException(ExceptionConstants.TIER_ID_NULL, ResponseResource.R_CODE_NOT_FOUND);
			LOGGER.info("***** Tier id -> {} ", tierId);

			/*
			 * Get SLA for Packet drop SLAId for Packet drop is 2 Packet drop depends on
			 * service variant only
			 */
			// List<IasSLAView> pktDropRecords =
			// slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientId(
			// productOfferingId, SLAParameters.PACKET_DROP.getId(), serviceVariantId);

			// Will retrieve a single record for packet drop
			// if (pktDropRecords == null || pktDropRecords.isEmpty())
			// throw new TclCommonException(ExceptionConstants.PRODUCT_SLA_EMPTY,
			// ResponseResource.R_CODE_NOT_FOUND);
			// else
			// slaList.add(new SLADto(pktDropRecords.get(0),));

			/*
			 * Get SLA for Network up-time SLAId for Network up-time is 3 Network up-time
			 * depends on access type and tier categorization
			 */

			getSLABasedOnTwoFactors(productOfferingId, serviceVariantId, SLAParameters.NETWORK_UP_TIME.getId(),
					accessTypeId, tierId, slaList);

			/*
			 * Get SLA for Round trip delay. SLAId for Round trip delay is 1. Round trip
			 * delay depends on tier categorization, if variant is standard (1) or
			 * compressed (3) Round trip delay depends on tier categorization and POP
			 * destination as well, if variant is premium (2) or enhanced (4)
			 */

			if (serviceVariantId == ServiceVariants.STANDARD.getId()
					|| serviceVariantId == ServiceVariants.COMPRESSED.getId())
				getSLABasedOnSingleFactor(productOfferingId, serviceVariantId, SLAParameters.ROUND_TRIP_DELAY.getId(),
						tierId, slaList);

			if (serviceVariantId == ServiceVariants.PREMIUM.getId()
					|| serviceVariantId == ServiceVariants.ENHANCED.getId())
				getSLABasedOnTwoFactors(productOfferingId, serviceVariantId, SLAParameters.ROUND_TRIP_DELAY.getId(),
						destinationId, tierId, slaList);

			if (slaList.isEmpty() || slaList.size() < 3)
				throw new TclCommonException(ExceptionConstants.SLA_PARAM_MISSING, ResponseResource.R_CODE_NOT_FOUND);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return slaList;
	}

	/**
	 * @author Dinahar Vivekanandan. getSLABasedOnTwoFactors - Method used to
	 *         retrieve the SLA value based on 2 factors
	 * @param productOfferingId
	 * @param serviceVariantId
	 * @param slaId
	 * @param factorValOne
	 * @param factorValTwo
	 * @param slaList
	 * @throws TclCommonException
	 */
	public void getSLABasedOnTwoFactors(Integer productOfferingId, Integer serviceVariantId, Integer slaId,
			Integer factorValOne, Integer factorValTwo, List<SLADto> slaList) throws TclCommonException {

		try {
			// Retrieve the list of factor groups based on Factor one and other inputs
			// List<IasSLAView> recordsForFactorOne = slaViewRepository
			// .findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(productOfferingId,
			// slaId,
			// serviceVariantId, factorValOne);
			//
			// if (recordsForFactorOne == null || recordsForFactorOne.isEmpty())
			// throw new TclCommonException(ExceptionConstants.PRODUCT_SLA_EMPTY,
			// ResponseResource.R_CODE_NOT_FOUND);
			//
			// else {
			// LOGGER.info("***** recordsForFactorOne.size() -> {} ",
			// recordsForFactorOne.size());
			//
			// // Get the factor groups in recordsForFactorOne list and get the records for
			// // those factor groups
			//// List<IasSLAView> recordsForFactorGroups =
			// slaViewRepository.findByFactorGroupIdIn(
			//// recordsForFactorOne.stream().map(IasSLAView::getFactorValueId).collect(Collectors.toList()));
			//// if (recordsForFactorGroups == null || recordsForFactorGroups.isEmpty())
			//// throw new TclCommonException(ExceptionConstants.PRODUCT_SLA_EMPTY,
			//// ResponseResource.R_CODE_NOT_FOUND);
			////
			//// else {
			////
			//// LOGGER.info("***** recordsForFactorGroups.size() -> {} ",
			// recordsForFactorGroups.size());
			////
			//// // Apply filters on recordsForFactorGroups using Factor two and other
			// inputs. It
			//// // should return single value
			////
			////// Optional<IasSLAView> finalRecord = recordsForFactorGroups.stream()
			////// .filter(record -> record.getProductOfferingId().equals(productOfferingId)
			////// && record.getSlaId().equals(slaId)
			////// && record.getServiceVarientId().equals(serviceVariantId)
			////// && record.getFactorValueId().equals(factorValTwo))
			////// .findAny();
			//////
			////// if (finalRecord.isPresent())
			////// slaList.add(new SLADto(finalRecord.get()));
			////// else
			////// throw new TclCommonException(ExceptionConstants.PRODUCT_SLA_EMPTY,
			////// ResponseResource.R_CODE_NOT_FOUND);
			////
			//// }
			// }
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * getSLABasedOnSingleFactor - Method to retrieve SLA value based on single SLA
	 * factor
	 * 
	 * @author Dinahar Vivekanandan
	 * 
	 * @param productOfferingId
	 * @param serviceVariantId
	 * @param slaId
	 * @param factorVal
	 * @param slaList
	 * @throws TclCommonException
	 */
	public void getSLABasedOnSingleFactor(Integer productOfferingId, Integer serviceVariantId, Integer slaId,
			Integer factorVal, List<SLADto> slaList) throws TclCommonException {
		List<IasSLAView> records = null;
		try {
			//// records =
			//// slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(
			//// productOfferingId, slaId, serviceVariantId, factorVal);
			//// LOGGER.info("***** records.size() -> {} ", records.size());
			// // Will retrieve a single record
			// if (records.isEmpty())
			// throw new TclCommonException(ExceptionConstants.PRODUCT_SLA_EMPTY,
			//// ResponseResource.R_CODE_NOT_FOUND);
			// else
			// slaList.add(new SLADto(records.get(0),factorVal));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	public ProductSlaBean processProductSla(String request) {

		String[] spliter = request.split(",");
		String tier = spliter[0];
		String factorValue = spliter[1];
		String serviceVariant = spliter[2];
		String tierName = StringUtil.EMPTY_STRING;
		if (tier.equals(String.valueOf(SlaConstants.TIER_ONE_1.getId()))) {
			tierName = SlaConstants.TIER_ONE.getName();
		} else if (tier.equals(String.valueOf(SlaConstants.TIER_TWO_2.getId()))) {
			tierName = SlaConstants.TIER_TWO.getName();
		} else if (tier.equals(String.valueOf(SlaConstants.TIER_THREE_3.getId()))) {
			tierName = SlaConstants.TIER_THREE.getName();
		}
		return getProductSla(tierName, factorValue, serviceVariant);

	}

	/**
	 * getProductSla
	 * 
	 * @param tier
	 * @param factorValue
	 * @return
	 */

	private ProductSlaBean getProductSla(String tierName, String factorValue, String serviceVariant) {
		ProductSlaBean productSlaBean = new ProductSlaBean();

		List<IasSLAView> upTimeView = slaViewRepository.findBySlaIdAndFactorValue(SLAParameters.NETWORK_UP_TIME.getId(),
				factorValue);
		if (upTimeView != null && !upTimeView.isEmpty()) {
			IasSLAView networkUpTime = upTimeView.get(0);
			SLaDetailsBean pBean = new SLaDetailsBean();
			pBean.setName(SlaConstants.NETWORK_UP_TIME.getName());
			pBean.setSlaTier(tierName);
			if (tierName.equals(SlaConstants.TIER_ONE.getName())) {
				pBean.setSlaValue(networkUpTime.getTier1Value());
			} else if (tierName.equals(SlaConstants.TIER_TWO.getName())) {
				pBean.setSlaValue(networkUpTime.getTier2Value());
			} else if (tierName.equals(SlaConstants.TIER_THREE.getName())) {
				pBean.setSlaValue(networkUpTime.getTier3Value());
			}

			productSlaBean.getsLaDetails().add(pBean);

		}

		List<IasSLAView> roundTripDelayView = slaViewRepository
				.findBySlaIdAndSltVariant(SLAParameters.ROUND_TRIP_DELAY.getId(), serviceVariant);

		if (roundTripDelayView != null && !roundTripDelayView.isEmpty()) {
			IasSLAView roundTripDelay = roundTripDelayView.get(0);
			SLaDetailsBean pBean = new SLaDetailsBean();
			pBean.setName(SlaConstants.ROUND_TRIP_DELAY.getName());
			pBean.setSlaTier(tierName);
			if (tierName.equals(SlaConstants.TIER_ONE.getName())) {
				pBean.setSlaValue(roundTripDelay.getTier1Value() + CommonConstants.SPACE + CommonConstants.MS);
			} else if (tierName.equals(SlaConstants.TIER_TWO.getName())) {
				pBean.setSlaValue(roundTripDelay.getTier2Value()+ CommonConstants.SPACE + CommonConstants.MS);
			} else if (tierName.equals(SlaConstants.TIER_THREE.getName())) {
				pBean.setSlaValue(roundTripDelay.getTier3Value()+ CommonConstants.SPACE + CommonConstants.MS);
			}

			productSlaBean.getsLaDetails().add(pBean);

		}

		return productSlaBean;

	}

	public ProductSlaBean processProductSlaWithCity(String request) {
		String[] spliter = request.split(",");
		String city = spliter[0];
		String factorValue = spliter[1];
		String serviceVariant = spliter[2];
		ServiceAreaMatrixIAS serviceAreaMatrixIAS = null;
		List<ServiceAreaMatrixIAS> serviceAresMatrix = serviceAreaMatrixIASRepository.findByCityDtl(city);
		if (!serviceAresMatrix.isEmpty()) {
			serviceAreaMatrixIAS = serviceAresMatrix.get(0);
			String tierName = StringUtil.EMPTY_STRING;
			if (serviceAreaMatrixIAS.getTierCdNrb().equals(SlaConstants.TIER_ONE_1.getId())) {
				tierName = SlaConstants.TIER_ONE.getName();
			} else if (serviceAreaMatrixIAS.getTierCdNrb().equals(SlaConstants.TIER_TWO_2.getId())) {
				tierName = SlaConstants.TIER_TWO.getName();
			} else if (serviceAreaMatrixIAS.getTierCdNrb().equals(SlaConstants.TIER_THREE_3.getId())) {
				tierName = SlaConstants.TIER_THREE.getName();
			}

			return getProductSla(tierName, factorValue, serviceVariant);
		}
		return getProductSla(SlaConstants.TIER_THREE.getName(), factorValue, serviceVariant);

	}
	/**
	 * 
	 * Get PacketDro pDetails For IAS BasedOn SltVariant
	 * @param sltVariant
	 * @return
	 */
	public String getPacketDropDetailsForIASBasedOnSltVariant(String sltVariant) {
		String response =null;
		if(sltVariant!=null) {
			List<VwProductSlaSpecIAS> vwProductSlaSpecIASs = vwProductSlaSpecIASRepository.findBySltVarientAndSlaMetricId(sltVariant, CommonConstants.TWO);
			if(vwProductSlaSpecIASs!=null && !vwProductSlaSpecIASs.isEmpty() && vwProductSlaSpecIASs.get(0)!=null) {
				response =  vwProductSlaSpecIASs.get(0).getDefaultValue();
			}
		}
		return response;
	}



	/**
	 * getServiceAreaMatrixDc - Method to fetch data center details
	 * @return List of DataCenterBeans
	 * @throws TclCommonException
	 */
	public List<DataCenterBean> getDcDetailsForIas(String cityName) throws TclCommonException {
		List<DataCenterBean> dataCenterBeans;
		List<ViewIasDataCenterSam> iasDataCenterSams;

		if(Objects.nonNull(cityName) && !cityName.isEmpty()){
			iasDataCenterSams=iasDataCenterRepository.findByDcTypeAndTownsDtlAndIsActive(DataCenterType.INDIA_DATA_CENTER.getDcType(), cityName,"Y");
		}else {
			iasDataCenterSams = iasDataCenterRepository.findByDcTypeAndIsActive(DataCenterType.INDIA_DATA_CENTER.getDcType(), "Y");
		}
		LOGGER.info("Request query for DC: {} "+cityName);
		if (iasDataCenterSams.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.NPL_PRODUCT_DC_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}

		dataCenterBeans = iasDataCenterSams.stream()
				.map(DataCenterBean::new)
				.collect(Collectors.toList());

		return dataCenterBeans;
	}

}
