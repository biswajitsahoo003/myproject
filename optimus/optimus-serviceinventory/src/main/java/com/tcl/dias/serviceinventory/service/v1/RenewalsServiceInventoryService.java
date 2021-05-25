package com.tcl.dias.serviceinventory.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.serviceinventory.beans.SIConfigurationCountryBean;
import com.tcl.dias.serviceinventory.constants.RenewalsServiceAttributeConstants;
import com.tcl.dias.serviceinventory.constants.SiServiceAttributeConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIComponentAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIContractInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIOrderAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.repository.SIComponentAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceAttributeRepository;
import com.tcl.dias.serviceinventory.renewals.RenevalsValidateBean;
import com.tcl.dias.serviceinventory.renewals.RenewalsAttributeDetail;
import com.tcl.dias.serviceinventory.renewals.RenewalsComponentDetail;
import com.tcl.dias.serviceinventory.renewals.RenewalsPriceBean;
import com.tcl.dias.serviceinventory.renewals.RenewalsQuoteDetail;
import com.tcl.dias.serviceinventory.renewals.RenewalsSite;
import com.tcl.dias.serviceinventory.renewals.RenewalsSiteDetail;
import com.tcl.dias.serviceinventory.renewals.RenewalsSolutionDetail;

/**
 * Service class for service inventory related API
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class RenewalsServiceInventoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RenewalsServiceInventoryService.class);

	@Autowired
	SIOrderRepository siOrderRepository;

	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;

	@Autowired
	SiServiceAttributeRepository siServiceAttributeRepository;

	@Autowired
	SIOrderAttributeRepository siOrderAttributeRepository;
	
	@Autowired
	SIComponentAttributeRepository sIComponentAttributeRepository;

	// private RenewalsComponentDetail componentDetail;

	/**
	 * Get Configurations by order id and product name
	 *
	 * @param orderId
	 * @param productName
	 * @return {@link SIConfigurationCountryBean}
	 */

	/*
	 * public RenewalsQuoteDetail constructQuoteDetailsForList(String[] serviceIds)
	 * { RenewalsQuoteDetail quoteDetail = new RenewalsQuoteDetail(); // int
	 * mappingId = 0; SIOrder siOrder = null; SIContractInfo siContractInfo = null;
	 * Map<String, RenewalsPriceBean> priceMapping = new HashMap<String,
	 * RenewalsPriceBean>(); for (String serviceId : serviceIds) { // mappingId++;
	 * Optional<SIServiceDetail> existingSiServiceDetails =
	 * siServiceDetailRepository .findByTpsServiceIdAndIsActive(serviceId.trim(),
	 * "Y"); RenewalsPriceBean renewalsPrice = new RenewalsPriceBean(); if
	 * (existingSiServiceDetails.isPresent()) {
	 * 
	 * SIServiceDetail siServiceDetail = existingSiServiceDetails.get();
	 * RenewalsSolutionDetail solutionDetail = constructSolution(siServiceDetail);
	 * 
	 * if (solutionDetail != null) {
	 * solutionDetail.setAccessType(siServiceDetail.getAccessType());
	 * 
	 * // solutionDetail.setIsmultiVrf(siServiceDetail.getIsmultivrf().toString());
	 * quoteDetail.getSolutions().add(solutionDetail);
	 * 
	 * siOrder = siServiceDetail.getSiOrder();
	 * quoteDetail.setCustomerId(Integer.valueOf(siOrder.getErfCustCustomerId()));
	 * 
	 * siContractInfo = siServiceDetail.getSiContractInfo();
	 * 
	 * List<RenewalsSiteDetail> siteDetailsList = new
	 * ArrayList<RenewalsSiteDetail>(); RenewalsSiteDetail siteDetails = new
	 * RenewalsSiteDetail();
	 * siteDetails.setLocationId(Integer.valueOf(siServiceDetail.
	 * getErfLocSiteAddressId())); siteDetailsList.add(siteDetails);
	 * 
	 * RenewalsSite site = new RenewalsSite();
	 * site.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
	 * site.setSite(siteDetailsList); site.setServiceId(serviceId.trim());
	 * quoteDetail.setProductName(siServiceDetail.getErfPrdCatalogProductName());
	 * quoteDetail.getSite().add(site);
	 * 
	 * if (siServiceDetail.getMrc() != null) {
	 * renewalsPrice.setMrc(siServiceDetail.getMrc()); } else {
	 * renewalsPrice.setMrc(0D); } if (siServiceDetail.getNrc() != null) {
	 * renewalsPrice.setNrc(siServiceDetail.getNrc()); } else {
	 * renewalsPrice.setNrc(0D); } if (siServiceDetail.getArc() != null) {
	 * renewalsPrice.setArc(siServiceDetail.getArc()); } else {
	 * renewalsPrice.setArc(0D); }
	 * priceMapping.put(siServiceDetail.getTpsServiceId(), renewalsPrice);
	 * 
	 * quoteDetail.setRenewalsPriceBean(priceMapping); } }
	 * 
	 * } List<RenewalsAttributeDetail> quoteLeAttributeValueList =
	 * getQuoteToLeAttr(siOrder, siContractInfo); if
	 * (!quoteLeAttributeValueList.isEmpty())
	 * quoteDetail.setQuoteAttributeList(quoteLeAttributeValueList); return
	 * quoteDetail; }
	 */
	
	public RenewalsSolutionDetail constructSolutionV1(String productName,Map<String, Object> existingSiServiceDetail,Integer serviceId, String type) {
		List<String> listCategory;
		if (productName.equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)) {
			listCategory = getListOfCategory();
		} else if (productName
				.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {
			listCategory = getListOfCategoryGvpn();
		} else {
			listCategory = getListOfCategoryNpl();
		}

		List<SIComponentAttribute> sIComponentAttributes;
		RenewalsSolutionDetail solutionDetails = new RenewalsSolutionDetail();

		Map<String, RenewalsComponentDetail> componetMap = new HashMap<String, RenewalsComponentDetail>();
		List<SIServiceAttribute> sIServiceAttributeList = siServiceAttributeRepository
				.findBySiServiceDetailIdAndCategoryIn(serviceId, listCategory);
		if (productName.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL) || productName.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
	       sIComponentAttributes = sIComponentAttributeRepository.findFirstBySiServiceDetailId(serviceId);
	       for(SIComponentAttribute sIComponentAttribute: sIComponentAttributes) {
	    	   SIServiceAttribute obj = new SIServiceAttribute();
	    	   obj.setAttributeName(sIComponentAttribute.getAttributeName());
	    	   obj.setAttributeValue(sIComponentAttribute.getAttributeValue());
	    	   obj.setCategory(SiServiceAttributeConstants.LAST_MILE);
	    	   sIServiceAttributeList.add(obj);
	       }
		}
		if (!sIServiceAttributeList.isEmpty()) {
			componetMap = new HashMap<>();

			for (SIServiceAttribute sIServiceAttribute : sIServiceAttributeList) {
				RenewalsComponentDetail componentDetail = new RenewalsComponentDetail();
				if (componetMap.get(sIServiceAttribute.getCategory()) == null) {

					componetMap.put(sIServiceAttribute.getCategory(), componentDetail);
				} else {
					componentDetail = componetMap.get(sIServiceAttribute.getCategory());
				}
				componentDetail = constructAttributeValues(componentDetail, sIServiceAttribute, type);

				if (componentDetail != null) {
					componetMap.put(sIServiceAttribute.getCategory(), componentDetail);
				}

			}
		}
		if (!componetMap.isEmpty()) {
			if (productName
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)
					) {
				RenewalsComponentDetail componentDetail = componetMap
						.get(SiServiceAttributeConstants.INTERNET_PORT) != null
						? componetMap.get(SiServiceAttributeConstants.INTERNET_PORT)
						: new RenewalsComponentDetail();
				componentDetail = updatePortSpeedV1(componentDetail, existingSiServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH,
						SiServiceAttributeConstants.INTERNET_PORT);
				componentDetail.setType(type);
				componetMap.put(SiServiceAttributeConstants.INTERNET_PORT, componentDetail);
				RenewalsComponentDetail componentDetail2 = componetMap
						.get(SiServiceAttributeConstants.LAST_MILE) != null
						? componetMap.get(SiServiceAttributeConstants.LAST_MILE)
						: new RenewalsComponentDetail();
						
						componentDetail2 = updatePortSpeedV1(componentDetail2, existingSiServiceDetail,
						SiServiceAttributeConstants.LOCAL_LOOP_BANDWIDTH,
						SiServiceAttributeConstants.LAST_MILE);
						componentDetail2.setType(type);
				componetMap.put(SiServiceAttributeConstants.LAST_MILE, componentDetail2);
			//	List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
				
			//	componentList.add(componentDetail);
			//	componentList.add(componentDetail2);
			//	solutionDetails.setComponents(componentList);
			}
			if (productName
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {
				RenewalsComponentDetail componentDetail1 = componetMap
						.get(RenewalsServiceAttributeConstants.VPN_PORT) != null
						? componetMap.get(RenewalsServiceAttributeConstants.VPN_PORT)
						: new RenewalsComponentDetail();
						componentDetail1 = updatePortSpeedV1(componentDetail1, existingSiServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH,
						RenewalsServiceAttributeConstants.VPN_PORT);
				
						componentDetail1 = updatePortSpeedV1(componentDetail1, existingSiServiceDetail,
						SiServiceAttributeConstants.BURSTABLE_BANDWIDTH,
						RenewalsServiceAttributeConstants.VPN_PORT);
						componentDetail1.setType(type);
						componetMap.put(RenewalsServiceAttributeConstants.VPN_PORT, componentDetail1);
						RenewalsComponentDetail componentDetail2 = componetMap
								.get(RenewalsServiceAttributeConstants.LAST_MILE) != null
								? componetMap.get(RenewalsServiceAttributeConstants.LAST_MILE)
								: new RenewalsComponentDetail();				
						componentDetail2 = updatePortSpeedV1(componentDetail2, existingSiServiceDetail,
						SiServiceAttributeConstants.LOCAL_LOOP_BANDWIDTH,
						SiServiceAttributeConstants.LAST_MILE);
						componentDetail2.setType(type);
						componetMap.put(SiServiceAttributeConstants.LAST_MILE, componentDetail2);
				//List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();				
				//componentList.add(componentDetail2);
				//componentList.add(componentDetail1);
		
				//solutionDetails.setComponents(componentList);
			} else if (productName
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)
					|| productName
							.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
				
				RenewalsComponentDetail componentDetail1 = componetMap
						.get(RenewalsServiceAttributeConstants.NATIONAL_CONNECTIVITY) != null
						? componetMap.get(RenewalsServiceAttributeConstants.NATIONAL_CONNECTIVITY)
						: new RenewalsComponentDetail();
				componentDetail1 = updatePortSpeedV1(componentDetail1, existingSiServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH,
						RenewalsServiceAttributeConstants.NATIONAL_CONNECTIVITY);
				componentDetail1.setType(type);
				componetMap.put(RenewalsServiceAttributeConstants.NATIONAL_CONNECTIVITY, componentDetail1);
				
				RenewalsComponentDetail componentDetail2 = componetMap
						.get(RenewalsServiceAttributeConstants.LAST_MILE) != null
						? componetMap.get(RenewalsServiceAttributeConstants.LAST_MILE)
						: new RenewalsComponentDetail();
				componentDetail2 = updatePortSpeedV1(componentDetail2, existingSiServiceDetail,
						SiServiceAttributeConstants.LOCAL_LOOP_BANDWIDTH,
						SiServiceAttributeConstants.LAST_MILE);
				componentDetail2.setType(type);
				componetMap.put(SiServiceAttributeConstants.LAST_MILE, componentDetail2);
			//	List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
			//	componentList.add(componentDetail1);
			//	componentList.add(componentDetail2);
			//	solutionDetails.setComponents(componentList);
			}
			if (!componetMap.isEmpty()) {
				for (Map.Entry<String, RenewalsComponentDetail> entry : componetMap.entrySet()) {
					solutionDetails.getComponents().add(entry.getValue());
				}
			}
			if (existingSiServiceDetail.get("erfPrdCatalogOfferingName") != null) {
				solutionDetails.setOfferingName((String)existingSiServiceDetail.get("erfPrdCatalogOfferingName"));
			}
			solutionDetails.setServiceId(existingSiServiceDetail.get("tpsServiceId") != null?(String)existingSiServiceDetail.get("tpsServiceId"):null);

		} else {
			return null;
		}

		return solutionDetails;
	}
	
	
	public RenewalsSolutionDetail constructSolution(SIServiceDetail siServiceDetail) {
		List<String> listCategory;
		if (siServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)) {
			listCategory = getListOfCategory();
		} else if (siServiceDetail.getErfPrdCatalogProductName()
				.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {
			listCategory = getListOfCategoryGvpn();
		} else {
			listCategory = getListOfCategoryNpl();
		}

		RenewalsSolutionDetail solutionDetails = new RenewalsSolutionDetail();

		Map<String, RenewalsComponentDetail> componetMap = new HashMap<String, RenewalsComponentDetail>();
		List<SIServiceAttribute> sIServiceAttributeList = siServiceAttributeRepository
				.findBySiServiceDetailAndCategoryIn(siServiceDetail, listCategory);
		if (!sIServiceAttributeList.isEmpty()) {
			componetMap = new HashMap<String, RenewalsComponentDetail>();

			for (SIServiceAttribute sIServiceAttribute : sIServiceAttributeList) {
				RenewalsComponentDetail componentDetail = new RenewalsComponentDetail();
				if (componetMap.get(sIServiceAttribute.getCategory()) == null) {

					componetMap.put(sIServiceAttribute.getCategory(), componentDetail);
				} else {
					componentDetail = componetMap.get(sIServiceAttribute.getCategory());
				}
		//		componentDetail = constructAttributeValues(componentDetail, sIServiceAttribute);

				if (componentDetail != null) {
					componetMap.put(sIServiceAttribute.getCategory(), componentDetail);
				}

			}
		}
		/*
		 * componentDetail = updatePortSpeed(componentDetail, siServiceDetail,
		 * SiServiceAttributeConstants.BURSTABLE_BANDWIDTH,
		 * SiServiceAttributeConstants.INTERNET_PORT); if (componentDetail != null) {
		 * componetMap.put(SiServiceAttributeConstants.INTERNET_PORT, componentDetail);
		 * }
		 */
		if (!componetMap.isEmpty()) {
			if (siServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)
					|| siServiceDetail.getErfPrdCatalogProductName()
							.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)) {
				RenewalsComponentDetail componentDetail = componetMap
						.get(SiServiceAttributeConstants.INTERNET_PORT) != null
								? componetMap.get(SiServiceAttributeConstants.INTERNET_PORT)
								: new RenewalsComponentDetail();
				componentDetail = updatePortSpeed(componentDetail, siServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH, SiServiceAttributeConstants.INTERNET_PORT);
				if (componentDetail != null) {
					componetMap.put(SiServiceAttributeConstants.INTERNET_PORT, componentDetail);
				}
			} else if (siServiceDetail.getErfPrdCatalogProductName()
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {

				RenewalsComponentDetail componentDetail = componetMap
						.get(RenewalsServiceAttributeConstants.VPN_PORT) != null
								? componetMap.get(RenewalsServiceAttributeConstants.VPN_PORT)
								: new RenewalsComponentDetail();
				componentDetail = updatePortSpeed(componentDetail, siServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH, RenewalsServiceAttributeConstants.VPN_PORT);
				if (componentDetail != null) {
					componetMap.put(RenewalsServiceAttributeConstants.VPN_PORT, componentDetail);
				}

			}
			if (!componetMap.isEmpty()) {
				for (Map.Entry<String, RenewalsComponentDetail> entry : componetMap.entrySet()) {
					solutionDetails.getComponents().add(entry.getValue());
				}
			}
			if (siServiceDetail.getErfPrdCatalogOfferingName() != null) {
				solutionDetails.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
			}
			solutionDetails.setServiceId(siServiceDetail.getTpsServiceId());

		} else {
			return null;
		}

		return solutionDetails;
	}

	public RenewalsSolutionDetail constructSolutionList(List<SIServiceDetail> siServiceDetailList) {
		LOGGER.info("Constructing Solution service ids--> {}", siServiceDetailList);
		RenewalsSolutionDetail solutionDetails = new RenewalsSolutionDetail();
		for (SIServiceDetail siServiceDetail : siServiceDetailList) {
			List<String> listCategory;
			if (siServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)) {
				listCategory = getListOfCategory();
			} else if (siServiceDetail.getErfPrdCatalogProductName()
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {
				listCategory = getListOfCategoryGvpn();
			} else {
				listCategory = getListOfCategoryNpl();
			}
			Map<String, RenewalsComponentDetail> componetMap = new HashMap<String, RenewalsComponentDetail>();
			List<SIServiceAttribute> sIServiceAttributeList = siServiceAttributeRepository
					.findBySiServiceDetailAndCategoryIn(siServiceDetail, listCategory);
			if (!sIServiceAttributeList.isEmpty()) {
				componetMap = new HashMap<String, RenewalsComponentDetail>();

				for (SIServiceAttribute sIServiceAttribute : sIServiceAttributeList) {
					RenewalsComponentDetail componentDetail = new RenewalsComponentDetail();
					if (componetMap.get(sIServiceAttribute.getCategory()) == null) {

						componetMap.put(sIServiceAttribute.getCategory(), componentDetail);
					} else {
						componentDetail = componetMap.get(sIServiceAttribute.getCategory());
					}
			//		componentDetail = constructAttributeValues(componentDetail, sIServiceAttribute);

					if (componentDetail != null) {
						componetMap.put(sIServiceAttribute.getCategory(), componentDetail);
					}

				}
			}

			// if (!componetMap.isEmpty()) {
			if (siServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)) {
				RenewalsComponentDetail componentDetail = componetMap
						.get(SiServiceAttributeConstants.INTERNET_PORT) != null
								? componetMap.get(SiServiceAttributeConstants.INTERNET_PORT)
								: new RenewalsComponentDetail();
				componentDetail = updatePortSpeed(componentDetail, siServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH, SiServiceAttributeConstants.INTERNET_PORT);
				if (componentDetail != null) {
					componetMap.put(SiServiceAttributeConstants.INTERNET_PORT, componentDetail);
				}
			} else if (siServiceDetail.getErfPrdCatalogProductName()
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {

				RenewalsComponentDetail componentDetail = componetMap
						.get(RenewalsServiceAttributeConstants.VPN_PORT) != null
								? componetMap.get(RenewalsServiceAttributeConstants.VPN_PORT)
								: new RenewalsComponentDetail();
				componentDetail = updatePortSpeed(componentDetail, siServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH, RenewalsServiceAttributeConstants.VPN_PORT);
				if (componentDetail != null) {
					componetMap.put(RenewalsServiceAttributeConstants.VPN_PORT, componentDetail);
				}

			} else if (siServiceDetail.getErfPrdCatalogProductName()
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)
					|| siServiceDetail.getErfPrdCatalogProductName()
							.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
				RenewalsComponentDetail componentDetail = componetMap
						.get(SiServiceAttributeConstants.NATIONAL_CONNECTIVITY) != null
								? componetMap.get(SiServiceAttributeConstants.NATIONAL_CONNECTIVITY)
								: new RenewalsComponentDetail();
				componentDetail = updatePortSpeed(componentDetail, siServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH, SiServiceAttributeConstants.NATIONAL_CONNECTIVITY);
				if (componentDetail != null) {
					componetMap.put(SiServiceAttributeConstants.INTERNET_PORT, componentDetail);
				}
			}
			if (!componetMap.isEmpty()) {
				for (Map.Entry<String, RenewalsComponentDetail> entry : componetMap.entrySet()) {
					solutionDetails.getComponents().add(entry.getValue());
				}
			}
			if (siServiceDetail.getErfPrdCatalogOfferingName() != null) {
				solutionDetails.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
			}
			solutionDetails.setServiceId(siServiceDetail.getTpsServiceId());

			// }
			/*
			 * else { return null; }
			 */
		}
		return solutionDetails;
	}

	public RenewalsComponentDetail updatePortSpeed(RenewalsComponentDetail componentDetail,
			SIServiceDetail siServiceDetail, String attributeName, String componentName) {
		RenewalsAttributeDetail attributeDetail = new RenewalsAttributeDetail();
		attributeDetail.setName(attributeName);
		if (attributeName.equalsIgnoreCase(SiServiceAttributeConstants.PORT_BANDWIDTH)) {
			if (siServiceDetail.getBwPortspeed() != null) {
				attributeDetail.setValue(siServiceDetail.getBwPortspeed());
			} else {
				attributeDetail.setValue(SiServiceAttributeConstants.DEFAULT_PORT_BANDWIDTH);
			}
		}
		if (attributeName.equalsIgnoreCase(SiServiceAttributeConstants.BURSTABLE_BANDWIDTH)) {
			attributeDetail.setValue(siServiceDetail.getBurstableBwPortspeed());
		}
		componentDetail.setName(componentName);
		componentDetail.getAttributes().add(attributeDetail);

		if (siServiceDetail.getServiceType() != null) {
			RenewalsAttributeDetail attributeDetail1 = new RenewalsAttributeDetail();
			attributeDetail1.setName(LeAttributesConstants.SERVICE_TYPE);
			attributeDetail1.setValue(siServiceDetail.getServiceType());
			componentDetail.getAttributes().add(attributeDetail1);
		}

		return componentDetail;
	}
	
	public RenewalsComponentDetail updatePortSpeedV1(RenewalsComponentDetail componentDetail,
			Map<String,Object> serviceDetail, String attributeName, String componentName) {
		RenewalsAttributeDetail attributeDetail = new RenewalsAttributeDetail();
		attributeDetail.setName(attributeName);
		if (attributeName.equalsIgnoreCase(SiServiceAttributeConstants.PORT_BANDWIDTH)) {
			if (serviceDetail.get("bwPortSpeed") != null) {
				attributeDetail.setValue((String)serviceDetail.get("bwPortSpeed")+" "+(String)serviceDetail.get("bwPortSpeedUnit"));
			} else {
				attributeDetail.setValue(SiServiceAttributeConstants.DEFAULT_PORT_BANDWIDTH);
			}
		}
		if (attributeName.equalsIgnoreCase(SiServiceAttributeConstants.BURSTABLE_BANDWIDTH)) {
			attributeDetail.setValue(serviceDetail.get("burstableBwPortSpeed")!=null?(String)serviceDetail.get("burstableBwPortSpeed")+" "+(String)serviceDetail.get("burstableBwPortSpeedUnit"):null);
		}
		if (attributeName.equalsIgnoreCase(SiServiceAttributeConstants.LOCAL_LOOP_BANDWIDTH)) {
			attributeDetail.setValue(serviceDetail.get("lastmileBw")!=null?(String)serviceDetail.get("lastmileBw")+" "+(String)serviceDetail.get("lastmileUnit"):null);
		}
		componentDetail.setName(componentName);
		componentDetail.getAttributes().add(attributeDetail);

		if (serviceDetail.get("serviceType") != null) {
			RenewalsAttributeDetail attributeDetail1 = new RenewalsAttributeDetail();
			attributeDetail1.setName(LeAttributesConstants.SERVICE_TYPE);
			attributeDetail1.setValue((String)serviceDetail.get("serviceType"));
			componentDetail.getAttributes().add(attributeDetail1);
		}

		return componentDetail;
	}

	public RenewalsComponentDetail constructAttributeValues(RenewalsComponentDetail componentDetail,
			SIServiceAttribute sIServiceAttribute,String type) {
		RenewalsAttributeDetail attributeDetail = new RenewalsAttributeDetail();
		attributeDetail.setName(sIServiceAttribute.getAttributeName());
		attributeDetail.setValue(sIServiceAttribute.getAttributeValue());
		componentDetail.setName(sIServiceAttribute.getCategory());
		componentDetail.setType(type);
		componentDetail.getAttributes().add(attributeDetail);
		return componentDetail;
	}
	
    public List<RenevalsValidateBean> constructBasicInfoV2(String[] serviceIds) {
        List<RenevalsValidateBean> renevalsValidateBeanList = new ArrayList<>();
        List<String> serviceIdList = new ArrayList<String>();
        for (String serviceId : serviceIds) {
               serviceIdList.add(serviceId.trim());
        }
        LOGGER.info("Inside Construct Basic Info of service Ids --> {}", serviceIdList);
        List<Map<String, Object>> existingSiServiceDetails = siServiceDetailRepository
               .findSIByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressId(serviceIdList);
        List<String> serviceIdTracker = new ArrayList<>();
        for (Map<String, Object> existingSiServiceDetail : existingSiServiceDetails) {

               String serviceId = (String) existingSiServiceDetail.get("tpsServiceId");
               if (serviceIdTracker.contains(serviceId)) {
                     continue;
               } else {
                     serviceIdTracker.add(serviceId);
               }
               RenevalsValidateBean renevalsValidateBean = new RenevalsValidateBean();
               renevalsValidateBean.setServiceId(serviceId);
               renevalsValidateBean.setCustId((String) existingSiServiceDetail.get("erfCustCustomerId"));
               renevalsValidateBean.setProduct((String) existingSiServiceDetail.get("erfPrdCatalogProductName"));
               renevalsValidateBean.setLeId(String.valueOf(existingSiServiceDetail.get("erfCustLeId")));
               renevalsValidateBean.setOfferingName((String) existingSiServiceDetail.get("erfPrdCatalogOfferingName"));
               renevalsValidateBean.setCircuitStatus((String) existingSiServiceDetail.get("circuitStatus"));
               renevalsValidateBean.setIsActive(existingSiServiceDetail.get("isActive") != null
                            ? String.valueOf(existingSiServiceDetail.get("isActive"))
                            : null);
               renevalsValidateBean.setLocationId((String) existingSiServiceDetail.get("erfLocSiteAddressId"));
               String remarks = existingSiServiceDetail.get("remarks") != null
                            ? (String) existingSiServiceDetail.get("remarks")
                            : null;
               if (StringUtils.isNotBlank(remarks)) {
                     renevalsValidateBean.setRemarks(remarks);
               } else {
                     renevalsValidateBean.setRemarks("");
               }
               renevalsValidateBean.setServiceStatus((String) existingSiServiceDetail.get("serviceStatus"));
               renevalsValidateBeanList.add(renevalsValidateBean);
               LOGGER.info("Adding Service Id to bean --> {}", serviceId);

        }

        if (serviceIdTracker != null && !serviceIdTracker.isEmpty()) {
               serviceIdList.removeAll(serviceIdTracker);
        }
        if(!serviceIdList.isEmpty()) {
        List<Map<String, Object>> existingSiServiceDetailsAll = siServiceDetailRepository
                      .findSIByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(
                                   serviceIdList);

        for (Map<String, Object> existingSiServiceDetail : existingSiServiceDetailsAll) {
               LOGGER.info("info  --> {}", existingSiServiceDetail.get("rownumber"));
               String serviceId = (String) existingSiServiceDetail.get("tpsServiceId");
               RenevalsValidateBean renevalsValidateBean = new RenevalsValidateBean();
               renevalsValidateBean.setServiceId(serviceId);
               renevalsValidateBean.setCustId((String) existingSiServiceDetail.get("erfCustCustomerId"));
               renevalsValidateBean.setProduct((String) existingSiServiceDetail.get("erfPrdCatalogProductName"));
               renevalsValidateBean.setLeId(String.valueOf(existingSiServiceDetail.get("erfCustLeId")));
               renevalsValidateBean.setOfferingName((String) existingSiServiceDetail.get("erfPrdCatalogOfferingName"));
               renevalsValidateBean.setCircuitStatus((String) existingSiServiceDetail.get("circuitStatus"));
               renevalsValidateBean.setIsActive(existingSiServiceDetail.get("isActive") != null
                            ? String.valueOf(existingSiServiceDetail.get("isActive"))
                            : null);
               renevalsValidateBean.setLocationId((String) existingSiServiceDetail.get("erfLocSiteAddressId"));
               String remarks = existingSiServiceDetail.get("remarks") != null
                            ? (String) existingSiServiceDetail.get("remarks")
                            : null;
               if (StringUtils.isNotBlank(remarks)) {
                     renevalsValidateBean.setRemarks(remarks);
               } else {
                     renevalsValidateBean.setRemarks("");
               }
               renevalsValidateBean.setServiceStatus((String) existingSiServiceDetail.get("serviceStatus"));
               renevalsValidateBeanList.add(renevalsValidateBean);
               LOGGER.info("Adding Service Id to bean --> {}", serviceId);

        }
        }
        return renevalsValidateBeanList;
 }

	
	
	/**
	 * 
	 * constructBasicInfo
	 * @param serviceIds
	 * @return
	 */
	public List<RenevalsValidateBean> constructBasicInfoV1(String[] serviceIds) {
		List<RenevalsValidateBean> renevalsValidateBeanList = new ArrayList<>();
		List<String> serviceIdList =new ArrayList<>();
		for (String serviceId : serviceIds) {
			serviceIdList.add(serviceId.trim());
		}
		LOGGER.info("Inside Construct Basic Info of service Ids --> {}", serviceIdList);
		List<Map<String, Object>> existingSiServiceDetails = siServiceDetailRepository
				.findByTpsServiceId(serviceIdList);
		LOGGER.info("DB output {}",existingSiServiceDetails.size());
		Set<String> serviceIdTracker = new HashSet<>();
		for (Map<String, Object> existingSiServiceDetail : existingSiServiceDetails) {
			String serviceId = (String) existingSiServiceDetail.get("tpsServiceId");
			LOGGER.info("Processing  Service Id to bean --> {}", serviceId);
			if (serviceIdTracker.contains(serviceId)) {
				continue;
			} else {
				serviceIdTracker.add(serviceId);
			}
			RenevalsValidateBean renevalsValidateBean = new RenevalsValidateBean();
			renevalsValidateBean.setServiceId(serviceId);
			renevalsValidateBean.setServiceStatus(existingSiServiceDetail.get("serviceStatus")!=null?(String) existingSiServiceDetail.get("serviceStatus"):null);
			renevalsValidateBean.setCustId((String) existingSiServiceDetail.get("erfCustCustomerId"));
			renevalsValidateBean.setProduct((String) existingSiServiceDetail.get("erfPrdCatalogProductName"));
			renevalsValidateBean.setLeId(String.valueOf(existingSiServiceDetail.get("erfCustLeId")));
			renevalsValidateBean.setOfferingName((String) existingSiServiceDetail.get("erfPrdCatalogOfferingName"));
			renevalsValidateBean.setCircuitStatus((String) existingSiServiceDetail.get("circuitStatus"));
			renevalsValidateBean.setIsActive(existingSiServiceDetail.get("isActive")!=null?String.valueOf(existingSiServiceDetail.get("isActive")):null);
			renevalsValidateBean.setLocationId((String) existingSiServiceDetail.get("erfLocSiteAddressId"));
			String remarks = existingSiServiceDetail.get("remarks") != null
					? (String) existingSiServiceDetail.get("remarks")
					: null;
			if (StringUtils.isNotBlank(remarks)) {
				renevalsValidateBean.setRemarks(remarks);
			} else {
				renevalsValidateBean.setRemarks("");
			}
			renevalsValidateBeanList.add(renevalsValidateBean);
			LOGGER.info("Adding Service Id to bean --> {}", renevalsValidateBean);
		}
		LOGGER.info("Total Si --> {}", renevalsValidateBeanList);
		return renevalsValidateBeanList;
	}

	public List<RenevalsValidateBean> constructBasicInfo(String[] serviceIds) {
		LOGGER.info("Inside Construct Basic Info of service Ids --> {}", serviceIds);
		List<RenevalsValidateBean> renevalsValidateBeanList = new ArrayList<RenevalsValidateBean>();
		for (String serviceId : serviceIds) {
			List<SIServiceDetail> existingSiServiceDetails;
			 existingSiServiceDetails = siServiceDetailRepository
					.findByPrimaryTpsSrviceId(
							serviceId.trim());
			if(existingSiServiceDetails.isEmpty()) {
				
				 existingSiServiceDetails = siServiceDetailRepository
							.findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(
									serviceId.trim(), RenewalsServiceAttributeConstants.ACTIVE,
									RenewalsServiceAttributeConstants.IS_ACTIVE);
					if (!existingSiServiceDetails.isEmpty()) {
						RenevalsValidateBean renevalsValidateBean = new RenevalsValidateBean();
						SIServiceDetail siServiceDetail = existingSiServiceDetails.get(0);
						SIOrder siOrder = siServiceDetail.getSiOrder();
						renevalsValidateBean.setServiceId(serviceId.trim());
						renevalsValidateBean.setCustId(siOrder.getErfCustCustomerId());
						renevalsValidateBean.setProduct(siServiceDetail.getErfPrdCatalogProductName());
						renevalsValidateBean.setLeId(String.valueOf(siOrder.getErfCustLeId()));
						renevalsValidateBean.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
						renevalsValidateBean.setCircuitStatus(siServiceDetail.getCircuitStatus());
						renevalsValidateBean.setIsActive(siServiceDetail.getIsActive());
						renevalsValidateBean.setLocationId(siServiceDetail.getErfLocSiteAddressId());
						if(siServiceDetail.getRemarks()!=null && siServiceDetail.getRemarks()!="") {
							renevalsValidateBean.setRemarks(siServiceDetail.getRemarks());
						}else {
							renevalsValidateBean.setRemarks("");
						}
						renevalsValidateBeanList.add(renevalsValidateBean);
						LOGGER.info("Adding Service Id to bean  non dual--> {}", serviceIds);
					} else {
						List<SIServiceDetail> datas = siServiceDetailRepository
								.findByTpsServiceIdForValidation(serviceId.trim());
						if (!datas.isEmpty()) {
						RenevalsValidateBean renevalsValidateBean = new RenevalsValidateBean();
						for(SIServiceDetail data:  datas) {
						SIServiceDetail siServiceDetail = data;
						SIOrder siOrder = siServiceDetail.getSiOrder();
						renevalsValidateBean.setServiceId(serviceId.trim());
						renevalsValidateBean.setCustId(siOrder.getErfCustCustomerId());
						renevalsValidateBean.setProduct(siServiceDetail.getErfPrdCatalogProductName());
						renevalsValidateBean.setLeId(String.valueOf(siOrder.getErfCustLeId()));
						renevalsValidateBean.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
						renevalsValidateBean.setCircuitStatus(siServiceDetail.getCircuitStatus());
						renevalsValidateBean.setIsActive(siServiceDetail.getIsActive());
						renevalsValidateBean.setLocationId(siServiceDetail.getErfLocSiteAddressId());
						if(siServiceDetail.getRemarks()!=null && siServiceDetail.getRemarks()!="") {
							renevalsValidateBean.setRemarks(siServiceDetail.getRemarks());
						}else {
							renevalsValidateBean.setRemarks("");
						}
						renevalsValidateBeanList.add(renevalsValidateBean);
						LOGGER.info("Adding Service Id to bean non dual with error--> {}", serviceIds);
						}
						}
						else {
						LOGGER.info("Adding Service Id Not Available in Db --> {}", serviceIds);
						}
					}
				
			}else {
				
				
				existingSiServiceDetails = siServiceDetailRepository
						.findByPrimaryTpsSrviceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(
								serviceId.trim(), RenewalsServiceAttributeConstants.ACTIVE,
								RenewalsServiceAttributeConstants.IS_ACTIVE);
				if (!existingSiServiceDetails.isEmpty()) {
					RenevalsValidateBean renevalsValidateBean = new RenevalsValidateBean();
					SIServiceDetail siServiceDetail = existingSiServiceDetails.get(0);
					SIOrder siOrder = siServiceDetail.getSiOrder();
					renevalsValidateBean.setServiceId(serviceId.trim());
					renevalsValidateBean.setCustId(siOrder.getErfCustCustomerId());
					renevalsValidateBean.setProduct(siServiceDetail.getErfPrdCatalogProductName());
					renevalsValidateBean.setLeId(String.valueOf(siOrder.getErfCustLeId()));
					renevalsValidateBean.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
					renevalsValidateBean.setCircuitStatus(siServiceDetail.getCircuitStatus());
					renevalsValidateBean.setIsActive(siServiceDetail.getIsActive());
					renevalsValidateBean.setLocationId(siServiceDetail.getErfLocSiteAddressId());
					if(siServiceDetail.getRemarks()!=null && siServiceDetail.getRemarks()!="") {
						renevalsValidateBean.setRemarks(siServiceDetail.getRemarks());
					}else {
						renevalsValidateBean.setRemarks("");
					}
					renevalsValidateBeanList.add(renevalsValidateBean);
					LOGGER.info("Adding Service Id to bean dual --> {}", serviceIds);
				} else {
					List<SIServiceDetail> datas = siServiceDetailRepository
							.findByPrimaryTpsSrviceId(serviceId.trim());
					if (!datas.isEmpty()) {
					RenevalsValidateBean renevalsValidateBean = new RenevalsValidateBean();
					for(SIServiceDetail data:  datas) {
					SIServiceDetail siServiceDetail = data;
					SIOrder siOrder = siServiceDetail.getSiOrder();
					renevalsValidateBean.setServiceId(serviceId.trim());
					renevalsValidateBean.setCustId(siOrder.getErfCustCustomerId());
					renevalsValidateBean.setProduct(siServiceDetail.getErfPrdCatalogProductName());
					renevalsValidateBean.setLeId(String.valueOf(siOrder.getErfCustLeId()));
					renevalsValidateBean.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
					renevalsValidateBean.setCircuitStatus(siServiceDetail.getCircuitStatus());
					renevalsValidateBean.setIsActive(siServiceDetail.getIsActive());
					renevalsValidateBean.setLocationId(siServiceDetail.getErfLocSiteAddressId());
					if(siServiceDetail.getRemarks()!=null && siServiceDetail.getRemarks()!="") {
						renevalsValidateBean.setRemarks(siServiceDetail.getRemarks());
					}else {
						renevalsValidateBean.setRemarks("");
					}
					renevalsValidateBeanList.add(renevalsValidateBean);
					LOGGER.info("Adding Service Id to bean dual with error--> {}", serviceIds);
					}
					}
					else {
					LOGGER.info("Adding Service Id Not Available in Db --> {}", serviceIds);
					}
				}
				
				
			}	
		}
		LOGGER.info("Sending data to OMS  --> {}", renevalsValidateBeanList.toString());
		return renevalsValidateBeanList;
	}
	
	public List<RenewalsAttributeDetail> getQuoteToLeAttrV1(Map<String, Object> existingSiServiceDetail) {
		List<RenewalsAttributeDetail> quoteLeAttributeValueList = new ArrayList<RenewalsAttributeDetail>();

			if (existingSiServiceDetail.get("erfCustSpLeName") != null) {
				LOGGER.info("Adding SUPPLIER_CONTRACTING_ENTITY");
				quoteLeAttributeValueList.add(constructAttributeDetail(
						LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY, (String)existingSiServiceDetail.get("erfCustSpLeName")));
			}
			if (existingSiServiceDetail.get("erfCustLeName") != null) {
				LOGGER.info("Adding LEGAL_ENTITY_NAME");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.LEGAL_ENTITY_NAME,
						 (String)existingSiServiceDetail.get("erfCustLeName")));
			}
			if (existingSiServiceDetail.get("tpsSfdcCuid") != null) {
				LOGGER.info("Adding ACCOUNT_CUID");
				quoteLeAttributeValueList.add(
						constructAttributeDetail(LeAttributesConstants.ACCOUNT_CUID,  (String)existingSiServiceDetail.get("tpsSfdcCuid")));
			}
			if (existingSiServiceDetail.get("accountManager") != null) {
				LOGGER.info("Adding LE_NAME");
				quoteLeAttributeValueList.add(
						constructAttributeDetail(LeAttributesConstants.LE_NAME, (String)existingSiServiceDetail.get("accountManager")));
			}
			if (existingSiServiceDetail.get("accountManagerEmail") != null) {
				LOGGER.info("Adding LE_EMAIL");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.LE_EMAIL,
						(String)existingSiServiceDetail.get("accountManagerEmail")));
			}

			if (existingSiServiceDetail.get("customerContact") != null) {
				LOGGER.info("Adding CONTACT_NAME");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.CONTACT_NAME,
						(String)existingSiServiceDetail.get("customerContact")));
			}

			if (existingSiServiceDetail.get("customerContactEmail") != null) {
				LOGGER.info("Adding CONTACT_EMAIL");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.CONTACT_EMAIL,
						(String)existingSiServiceDetail.get("customerContactEmail")));
			}

			if (existingSiServiceDetail.get("billingFrequency") != null) {
				LOGGER.info("Adding BILLING_FREQUENCY");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_FREQUENCY,
						(String)existingSiServiceDetail.get("billingFrequency")));
			}
			if (existingSiServiceDetail.get("billingMethod") != null) {
				LOGGER.info("Adding BILLING_METHOD");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_METHOD,
						(String)existingSiServiceDetail.get("billingMethod")));
			}
			if (existingSiServiceDetail.get("paymentTerm") != null) {
				LOGGER.info("Adding PAYMENT_TERM");
				quoteLeAttributeValueList.add(
						constructAttributeDetail(LeAttributesConstants.PAYMENT_TERM, (String)existingSiServiceDetail.get("paymentTerm")));
			}
			if (existingSiServiceDetail.get("billingContactId") != null) {
				LOGGER.info("Adding BILLING_CONTACT_ID");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_ID,
						String.valueOf(existingSiServiceDetail.get("billingContactId"))));
			}
			if (existingSiServiceDetail.get("billingCurrency") != null) {
				LOGGER.info("Adding BILLING_CURRENCY");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CURRENCY,
						String.valueOf(existingSiServiceDetail.get("billingCurrency"))));
			}
			if (existingSiServiceDetail.get("paymentCurrency") != null) {
				LOGGER.info("Adding PAYMENT_CURRENCY");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.PAYMENT_CURRENCY,
						String.valueOf(existingSiServiceDetail.get("paymentCurrency"))));
			}
			
	

		if (existingSiServiceDetail.get("sfdcAccountId") != null) {
			LOGGER.info("Adding ACCOUNT_NO18");
			quoteLeAttributeValueList
					.add(constructAttributeDetail(LeAttributesConstants.ACCOUNT_NO18, String.valueOf(existingSiServiceDetail.get("sfdcAccountId"))));
		}
		Integer siOrderId=(Integer)existingSiServiceDetail.get("siOrderid");

		SIOrderAttribute siOrderAttribute = getValueOfSiOrderAndAttributV1((Integer)existingSiServiceDetail.get("siOrderid"),
				LeAttributesConstants.LE_STATE_GST_NO);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding LE_STATE_GST_NO");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.LE_STATE_GST_NO,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttributV1(siOrderId, LeAttributesConstants.BILLING_CONTACT_NAME);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_NAME");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_NAME,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttributV1(siOrderId, LeAttributesConstants.BILLING_CONTACT_MOBILE);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_MOBILE");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_MOBILE,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttributV1(siOrderId, LeAttributesConstants.BILLING_CONTACT_EMAIL);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_EMAIL");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_EMAIL,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttributV1(siOrderId, LeAttributesConstants.BILLING_CONTACT_TITLE);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_TITLE");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_TITLE,
					siOrderAttribute.getAttributeValue()));
		}
		return quoteLeAttributeValueList;
	}

	public List<RenewalsAttributeDetail> getQuoteToLeAttr(SIOrder siOrder, SIContractInfo siContractInfo,
			SIServiceDetail siServiceDetail) {
		LOGGER.info("Adding Attributes in getQuoteToLeAttr --> {}", siServiceDetail.getTpsServiceId());
		List<RenewalsAttributeDetail> quoteLeAttributeValueList = new ArrayList<RenewalsAttributeDetail>();

		if (siContractInfo != null) {
			if (siContractInfo.getErfCustSpLeName() != null) {
				LOGGER.info("Adding SUPPLIER_CONTRACTING_ENTITY");
				quoteLeAttributeValueList.add(constructAttributeDetail(
						LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY, siContractInfo.getErfCustSpLeName()));
			}
			if (siContractInfo.getErfCustLeName() != null) {
				LOGGER.info("Adding LEGAL_ENTITY_NAME");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.LEGAL_ENTITY_NAME,
						siContractInfo.getErfCustLeName()));
			}
			if (siContractInfo.getTpsSfdcCuid() != null) {
				LOGGER.info("Adding ACCOUNT_CUID");
				quoteLeAttributeValueList.add(
						constructAttributeDetail(LeAttributesConstants.ACCOUNT_CUID, siContractInfo.getTpsSfdcCuid()));
			}
			if (siContractInfo.getAccountManager() != null) {
				LOGGER.info("Adding LE_NAME");
				quoteLeAttributeValueList.add(
						constructAttributeDetail(LeAttributesConstants.LE_NAME, siContractInfo.getAccountManager()));
			}
			if (siContractInfo.getAccountManagerEmail() != null) {
				LOGGER.info("Adding LE_EMAIL");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.LE_EMAIL,
						siContractInfo.getAccountManagerEmail()));
			}

			if (siContractInfo.getCustomerContact() != null) {
				LOGGER.info("Adding CONTACT_NAME");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.CONTACT_NAME,
						siContractInfo.getCustomerContact()));
			}

			if (siContractInfo.getCustomerContactEmail() != null) {
				LOGGER.info("Adding CONTACT_EMAIL");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.CONTACT_EMAIL,
						siContractInfo.getCustomerContactEmail()));
			}

			if (siContractInfo.getBillingFrequency() != null) {
				LOGGER.info("Adding BILLING_FREQUENCY");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_FREQUENCY,
						siContractInfo.getBillingFrequency()));
			}
			if (siContractInfo.getBillingMethod() != null) {
				LOGGER.info("Adding BILLING_METHOD");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_METHOD,
						siContractInfo.getBillingMethod()));
			}
			if (siContractInfo.getPaymentTerm() != null) {
				LOGGER.info("Adding PAYMENT_TERM");
				quoteLeAttributeValueList.add(
						constructAttributeDetail(LeAttributesConstants.PAYMENT_TERM, siContractInfo.getPaymentTerm()));
			}
			if (siContractInfo.getBillingContactId() != null) {
				LOGGER.info("Adding BILLING_CONTACT_ID");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_ID,
						siContractInfo.getBillingContactId().toString()));
			}
			if (siContractInfo.getBillingCurrency() != null) {
				LOGGER.info("Adding BILLING_CURRENCY");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CURRENCY,
						siContractInfo.getBillingCurrency().toString()));
			}
			if (siContractInfo.getPaymentCurrency() != null) {
				LOGGER.info("Adding PAYMENT_CURRENCY");
				quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.PAYMENT_CURRENCY,
						siContractInfo.getPaymentCurrency().toString()));
			}
			

		}

		if (siOrder != null && siOrder.getSfdcAccountId() != null) {
			LOGGER.info("Adding ACCOUNT_NO18");
			quoteLeAttributeValueList
					.add(constructAttributeDetail(LeAttributesConstants.ACCOUNT_NO18, siOrder.getSfdcAccountId()));
		}

		SIOrderAttribute siOrderAttribute = getValueOfSiOrderAndAttribut(siOrder,
				LeAttributesConstants.LE_STATE_GST_NO);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding LE_STATE_GST_NO");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.LE_STATE_GST_NO,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttribut(siOrder, LeAttributesConstants.BILLING_CONTACT_NAME);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_NAME");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_NAME,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttribut(siOrder, LeAttributesConstants.BILLING_CONTACT_MOBILE);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_MOBILE");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_MOBILE,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttribut(siOrder, LeAttributesConstants.BILLING_CONTACT_EMAIL);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_EMAIL");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_EMAIL,
					siOrderAttribute.getAttributeValue()));
		}

		siOrderAttribute = getValueOfSiOrderAndAttribut(siOrder, LeAttributesConstants.BILLING_CONTACT_TITLE);
		if (siOrderAttribute != null && !siOrderAttribute.getAttributeName().isEmpty()) {
			LOGGER.info("Adding BILLING_CONTACT_TITLE");
			quoteLeAttributeValueList.add(constructAttributeDetail(LeAttributesConstants.BILLING_CONTACT_TITLE,
					siOrderAttribute.getAttributeValue()));
		}
		LOGGER.info("Completed Adding Attributes in getQuoteToLeAttr --> {}", siServiceDetail.getTpsServiceId());
		return quoteLeAttributeValueList;
	}

	public RenewalsAttributeDetail constructAttributeDetail(String name, String value) {
		RenewalsAttributeDetail attributeDetail = new RenewalsAttributeDetail();
		attributeDetail.setName(name);
		attributeDetail.setValue(value);
		return attributeDetail;
	}

	public SIOrderAttribute getValueOfSiOrderAndAttribut(SIOrder siOrder, String name) {
		return siOrderAttributeRepository.findFirstBySiOrderAndAttributeNameOrderByIdDesc(siOrder, name);
	}
	
	public SIOrderAttribute getValueOfSiOrderAndAttributV1(Integer siOrderId, String name) {
		return siOrderAttributeRepository.findFirstBySiOrderIdAndAttributeNameOrderByIdDesc(siOrderId, name);
	}

	public List<String> getListOfCategory() {
		List<String> listCategory = new ArrayList<String>();
		listCategory.add(RenewalsServiceAttributeConstants.INTERNET_PORT);
		listCategory.add(RenewalsServiceAttributeConstants.LAST_MILEMILE);
		listCategory.add(RenewalsServiceAttributeConstants.CPE);
		listCategory.add(RenewalsServiceAttributeConstants.CPE_MANAGEMENT);
		listCategory.add(RenewalsServiceAttributeConstants.IAS_COMMON);
		listCategory.add(RenewalsServiceAttributeConstants.ADDON);
		listCategory.add(RenewalsServiceAttributeConstants.ADDITIONAL_IPS);
		listCategory.add(RenewalsServiceAttributeConstants.DNS);
		listCategory.add(RenewalsServiceAttributeConstants.SHIFTING_CHARGES);
		return listCategory;
	}

	public List<String> getListOfCategoryGvpn() {
		List<String> listCategory = new ArrayList<String>();
		listCategory.add(RenewalsServiceAttributeConstants.VPN_PORT);
		listCategory.add(RenewalsServiceAttributeConstants.LAST_MILE);
		listCategory.add(RenewalsServiceAttributeConstants.CPE_MANAGEMENT);
		listCategory.add(RenewalsServiceAttributeConstants.GVPN_COMMON);
		listCategory.add(RenewalsServiceAttributeConstants.VRF_COMMON);
		return listCategory;
	}

	public List<String> getListOfCategoryNpl() {
		List<String> listCategory = new ArrayList<String>();
		listCategory.add(RenewalsServiceAttributeConstants.PRIVATE_LINES);
		listCategory.add(RenewalsServiceAttributeConstants.LAST_MILE);
		listCategory.add(RenewalsServiceAttributeConstants.NATIONAL_CONNECTIVITY);
		listCategory.add(RenewalsServiceAttributeConstants.LINK_MANAGEMENT_CHARGES);
		return listCategory;
	}
	
	
	public RenewalsQuoteDetail constructQuoteDetailsNplForListV1(String[] serviceIds) {
		RenewalsQuoteDetail renewalQuoteDetail = new RenewalsQuoteDetail();
		List<RenewalsAttributeDetail> renewalAttributeDetails = new ArrayList<RenewalsAttributeDetail>();
		renewalQuoteDetail.setQuoteAttributeList(renewalAttributeDetails);
		List<String> serviceIdList =new ArrayList<>();
		for (String serviceId : serviceIds) {
			serviceIdList.add(serviceId.trim());
		}
		LOGGER.info("Inside constructQuoteDetailsNplForListV1 of service Ids --> {}", serviceIdList);
		List<Map<String, Object>> existingSiServiceDetails = siServiceDetailRepository
				.findByPrimaryTpsSrviceIdOrTpsServiceId(serviceIdList,
						RenewalsServiceAttributeConstants.ACTIVE, RenewalsServiceAttributeConstants.IS_ACTIVE);
		Set<Integer> productMapper=new HashSet<>();
		Map<String,List<RenewalsSiteDetail>> siteMapper=new HashMap<>();
		Map<String,RenewalsSite> siteRMapper=new HashMap<>();
		Map<String,RenewalsPriceBean> priceMapper=new HashMap<>();
		Set<String> serviceIdMapper=new HashSet<>();
		for (Map<String, Object> existingSiServiceDetail : existingSiServiceDetails) {
			
			RenewalsSolutionDetail renewalsSolutionDetail  =null;
			String serviceId = (String) existingSiServiceDetail.get("tpsServiceId");
			String primaryTpsServiceId = existingSiServiceDetail.get("primaryTpsServiceId") != null
					? (String) existingSiServiceDetail.get("primaryTpsServiceId")
					: null;
			String erfCustCustomerId = existingSiServiceDetail.get("erfCustCustomerId") != null
					?(String) existingSiServiceDetail.get("erfCustCustomerId"): null;
			String erfPrdCatalogProductName = existingSiServiceDetail.get("erfPrdCatalogProductName") != null
					?(String) existingSiServiceDetail.get("erfPrdCatalogProductName"): null;
			String erfPrdCatalogOfferingName = existingSiServiceDetail.get("erfPrdCatalogOfferingName") != null
					?(String) existingSiServiceDetail.get("erfPrdCatalogOfferingName"): null;
			Integer servId = existingSiServiceDetail.get("serviceId") != null
					?(Integer) existingSiServiceDetail.get("serviceId"): null;
			String accessType = existingSiServiceDetail.get("accessType") != null
					?(String) existingSiServiceDetail.get("accessType"): null;
			String erfLocSiteAddressId = existingSiServiceDetail.get("erfLocSiteAddressId") != null
					?(String) existingSiServiceDetail.get("erfLocSiteAddressId"): null;
			
			Double mrc = existingSiServiceDetail.get("mrc") != null
					?(Double) existingSiServiceDetail.get("mrc"): 0D;
			Double nrc = existingSiServiceDetail.get("nrc") != null
					?(Double) existingSiServiceDetail.get("nrc"): 0D;
			Double arc = existingSiServiceDetail.get("arc") != null
					?(Double) existingSiServiceDetail.get("arc"): 0D;
			String type = existingSiServiceDetail.get("primaryOrSecondary") != null 
					&& !(existingSiServiceDetail.get("primaryOrSecondary").toString().equalsIgnoreCase("single"))
					? (String) existingSiServiceDetail.get("primaryOrSecondary").toString().toLowerCase()
					: "primary"; 	
		    String copfId = existingSiServiceDetail.get("tps_copf_id") != null
							?(String) existingSiServiceDetail.get("tps_copf_id"): null;
							
			Character taxException = existingSiServiceDetail.get("tax_exemption_flag") != null
					?(Character) existingSiServiceDetail.get("tax_exemption_flag"): null; 				
						
			renewalQuoteDetail.setProductName(erfPrdCatalogProductName);
			if (erfPrdCatalogProductName
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)
					|| erfPrdCatalogProductName
							.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
			///	if(!productMapper.contains(servId)) {
					renewalsSolutionDetail  = constructSolutionV1(erfPrdCatalogProductName,existingSiServiceDetail,servId, type);
					productMapper.add(servId);
			//	}else {
					LOGGER.info("NDE product , so picking first site");
			//	}
				
			}else {
				renewalsSolutionDetail  = constructSolutionV1(erfPrdCatalogProductName,existingSiServiceDetail,servId, type);
			}
			if (renewalsSolutionDetail == null) {
				renewalsSolutionDetail = constructBasicComponents(existingSiServiceDetail, serviceId,
						erfPrdCatalogProductName, erfPrdCatalogOfferingName, type);
			}
			
			if (primaryTpsServiceId != null && primaryTpsServiceId.equals(serviceId)) {
				renewalsSolutionDetail.setDual(true);
			} else {
				renewalsSolutionDetail.setDual(false);
			}
			renewalsSolutionDetail.setAccessType(accessType);
			renewalsSolutionDetail.setCopfId(copfId);
			renewalsSolutionDetail.setTaxException(taxException);
			renewalQuoteDetail.setCustomerId(Integer.valueOf(erfCustCustomerId));
			if(siteMapper.get(serviceId)==null) {
				List<RenewalsSiteDetail> renewalSites=new ArrayList<>();
				RenewalsSiteDetail siteDetails = new RenewalsSiteDetail();
				siteDetails.setLocationId(Integer.valueOf(erfLocSiteAddressId));
				renewalSites.add(siteDetails);
				siteMapper.put(serviceId, renewalSites);
			}else {
				RenewalsSiteDetail siteDetails = new RenewalsSiteDetail();
				siteDetails.setLocationId(Integer.valueOf(erfLocSiteAddressId));
				siteMapper.get(serviceId).add(siteDetails);
			}
			
			if(siteRMapper.get(serviceId)==null) {
				RenewalsSite site = new RenewalsSite();
				site.setOfferingName(erfPrdCatalogOfferingName);
				site.setServiceId(serviceId);
				site.setSite(siteMapper.get(serviceId));
				siteRMapper.put(serviceId, site);
			}else {
				RenewalsSite site = siteRMapper.get(serviceId);
				site.setSite(siteMapper.get(serviceId));
			}
			
				if(priceMapper.get(serviceId)==null) {
					RenewalsPriceBean renewalsPrice = new RenewalsPriceBean();
					renewalsPrice.setMrc(mrc);
					renewalsPrice.setNrc(nrc);
					renewalsPrice.setArc(arc);
					priceMapper.put(serviceId, renewalsPrice);
				}else {
					if (renewalsSolutionDetail.isDual()) {
						priceMapper.get(serviceId).setMrc(priceMapper.get(serviceId).getMrc() + mrc);
						priceMapper.get(serviceId).setNrc(priceMapper.get(serviceId).getNrc() + nrc);
						priceMapper.get(serviceId).setArc(priceMapper.get(serviceId).getArc() + arc);
					}
				}
				if (!serviceIdMapper.contains(serviceId)) {
	//				List<RenewalsAttributeDetail> quoteLeAttributeValue = getQuoteToLeAttrV1(existingSiServiceDetail);
	//				if (quoteLeAttributeValue != null && !quoteLeAttributeValue.isEmpty()) {
	//					renewalAttributeDetails.addAll(quoteLeAttributeValue);
	//				}
					renewalQuoteDetail.getSolutions().add(renewalsSolutionDetail);
					serviceIdMapper.add(serviceId);
				}else {
			        //tttt  
					int j=-1;
					for(int i =0; i<renewalQuoteDetail.getSolutions().size(); i++) {
						if(renewalQuoteDetail.getSolutions().get(i).getServiceId().equalsIgnoreCase(serviceId)) {
							if(!renewalsSolutionDetail.getComponents().isEmpty()) {
							renewalQuoteDetail.getSolutions().get(i).getComponents().addAll(renewalsSolutionDetail.getComponents());
							}							
						}
					}
				}			
		}
		renewalQuoteDetail.setRenewalsPriceBean(priceMapper);
		
		List<RenewalsAttributeDetail> quoteLeAttributeValue = getQuoteToLeAttrV1(existingSiServiceDetails.get(0));
		if (quoteLeAttributeValue != null && !quoteLeAttributeValue.isEmpty()) {
			renewalAttributeDetails.addAll(quoteLeAttributeValue);
		}
		
		for (Entry<String, RenewalsSite> renewalSite : siteRMapper.entrySet()) {
			renewalQuoteDetail.getSite().add(renewalSite.getValue());
		}
		return renewalQuoteDetail;
	}

	private RenewalsSolutionDetail constructBasicComponents(Map<String, Object> existingSiServiceDetail,
			String serviceId, String erfPrdCatalogProductName, String erfPrdCatalogOfferingName, String type) {
		RenewalsSolutionDetail renewalsSolutionDetail;
		renewalsSolutionDetail = new RenewalsSolutionDetail();
		if (erfPrdCatalogOfferingName != null) {
			renewalsSolutionDetail.setOfferingName(erfPrdCatalogOfferingName);

			if (erfPrdCatalogProductName
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)
					) {
				RenewalsComponentDetail componentDetail = new RenewalsComponentDetail();
				componentDetail = updatePortSpeedV1(componentDetail, existingSiServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH,
						SiServiceAttributeConstants.INTERNET_PORT);
				componentDetail.setType(type);
				
				RenewalsComponentDetail componentDetail2 = new RenewalsComponentDetail();
				componentDetail2 = updatePortSpeedV1(componentDetail2, existingSiServiceDetail,
						SiServiceAttributeConstants.LOCAL_LOOP_BANDWIDTH,
						SiServiceAttributeConstants.LAST_MILE);
				componentDetail2.setType(type);
				
				List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
				componentList.add(componentDetail);
				componentList.add(componentDetail2);
								renewalsSolutionDetail.setComponents(componentList);
			}
			if (erfPrdCatalogProductName
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {
				RenewalsComponentDetail componentDetail1 = new RenewalsComponentDetail();
				componentDetail1 = updatePortSpeedV1(componentDetail1, existingSiServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH,
						RenewalsServiceAttributeConstants.VPN_PORT);
				componentDetail1.setType(type);
				
				componentDetail1 = updatePortSpeedV1(componentDetail1, existingSiServiceDetail,
						SiServiceAttributeConstants.BURSTABLE_BANDWIDTH,
						RenewalsServiceAttributeConstants.VPN_PORT);
				
				RenewalsComponentDetail componentDetail2 = new RenewalsComponentDetail();
				componentDetail2 = updatePortSpeedV1(componentDetail2, existingSiServiceDetail,
						SiServiceAttributeConstants.LOCAL_LOOP_BANDWIDTH,
						SiServiceAttributeConstants.LAST_MILE);
				componentDetail2.setType(type);
				
				List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
				componentList.add(componentDetail1);
				componentList.add(componentDetail2);
					renewalsSolutionDetail.setComponents(componentList);
			} else if (erfPrdCatalogProductName
					.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)
					|| erfPrdCatalogProductName
							.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
				
				RenewalsComponentDetail componentDetail1 = new RenewalsComponentDetail();
				componentDetail1 = updatePortSpeedV1(componentDetail1, existingSiServiceDetail,
						SiServiceAttributeConstants.PORT_BANDWIDTH,
						RenewalsServiceAttributeConstants.NATIONAL_CONNECTIVITY);
				componentDetail1.setType(type);
				
				RenewalsComponentDetail componentDetail2 = new RenewalsComponentDetail();
				componentDetail2 = updatePortSpeedV1(componentDetail2, existingSiServiceDetail,
						SiServiceAttributeConstants.LOCAL_LOOP_BANDWIDTH,
						SiServiceAttributeConstants.LAST_MILE);
				componentDetail2.setType(type);
				
				List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
				componentList.add(componentDetail1);
				componentList.add(componentDetail2);
				
				renewalsSolutionDetail.setComponents(componentList);
			}
		}

		renewalsSolutionDetail.setServiceId(serviceId);
		LOGGER.info("Solution of serviceid-->{} , is -->{}", serviceId, renewalsSolutionDetail);
		return renewalsSolutionDetail;
	}

	public RenewalsQuoteDetail constructQuoteDetailsNplForList(String[] serviceIds) {//METHOD
		LOGGER.info("Constructing Service Data service ids--> {}", serviceIds);
		RenewalsQuoteDetail quoteDetail = new RenewalsQuoteDetail();
		// int mappingId = 0;
		SIOrder siOrder = null;
		SIContractInfo siContractInfo = null;
		List<RenewalsAttributeDetail> quoteLeAttributeValueList = new ArrayList<RenewalsAttributeDetail>();
		Map<String, RenewalsPriceBean> priceMapping = new HashMap<String, RenewalsPriceBean>();
		for (String serviceId : serviceIds) {
			// mappingId++;
			boolean isDual = true;
			List<SIServiceDetail> existingSiServiceDetails = null;
			existingSiServiceDetails = siServiceDetailRepository
					.findByPrimaryTpsSrviceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNullOrderByIdAsc(
							serviceId.trim(), RenewalsServiceAttributeConstants.ACTIVE,
							RenewalsServiceAttributeConstants.IS_ACTIVE);
			if (existingSiServiceDetails.isEmpty()) {

				isDual = false;
				existingSiServiceDetails = siServiceDetailRepository
						.findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(
								serviceId.trim(), RenewalsServiceAttributeConstants.ACTIVE,
								RenewalsServiceAttributeConstants.IS_ACTIVE);
			} else {
				LOGGER.info("Dual Service Id -->{}", serviceId);
			}

			RenewalsPriceBean renewalsPrice = new RenewalsPriceBean();
			RenewalsSolutionDetail solutionDetail = null;
			if (!existingSiServiceDetails.isEmpty()) {

				SIServiceDetail siServiceDetail = existingSiServiceDetails.get(0);
				if (siServiceDetail.getErfPrdCatalogProductName()
						.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)
						|| siServiceDetail.getErfPrdCatalogProductName()
								.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
					solutionDetail = constructSolution(existingSiServiceDetails.get(0));
				} else {
					solutionDetail = constructSolutionList(existingSiServiceDetails);
				}

				if (solutionDetail == null) {
					solutionDetail = new RenewalsSolutionDetail();
					if (siServiceDetail.getErfPrdCatalogOfferingName() != null) {
						solutionDetail.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());

						if (siServiceDetail.getErfPrdCatalogProductName()
								.equalsIgnoreCase(RenewalsServiceAttributeConstants.IAS)
								) {
							RenewalsComponentDetail componentDetail = new RenewalsComponentDetail();
							componentDetail = updatePortSpeed(componentDetail, siServiceDetail,
									SiServiceAttributeConstants.PORT_BANDWIDTH,
									SiServiceAttributeConstants.INTERNET_PORT);
							List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
							componentList.add(componentDetail);
							solutionDetail.setComponents(componentList);
						}
						if (siServiceDetail.getErfPrdCatalogProductName()
								.equalsIgnoreCase(RenewalsServiceAttributeConstants.GVPN)) {
							RenewalsComponentDetail componentDetail1 = new RenewalsComponentDetail();
							componentDetail1 = updatePortSpeed(componentDetail1, siServiceDetail,
									SiServiceAttributeConstants.PORT_BANDWIDTH,
									RenewalsServiceAttributeConstants.VPN_PORT);
							List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
							componentList.add(componentDetail1);
							solutionDetail.setComponents(componentList);
						} else if (siServiceDetail.getErfPrdCatalogProductName()
								.equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)
								|| siServiceDetail.getErfPrdCatalogProductName()
										.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
							
							RenewalsComponentDetail componentDetail1 = new RenewalsComponentDetail();
							componentDetail1 = updatePortSpeed(componentDetail1, siServiceDetail,
									SiServiceAttributeConstants.PORT_BANDWIDTH,
									RenewalsServiceAttributeConstants.NATIONAL_CONNECTIVITY);
							List<RenewalsComponentDetail> componentList = new ArrayList<RenewalsComponentDetail>();
							componentList.add(componentDetail1);
							solutionDetail.setComponents(componentList);
						}
					}

					solutionDetail.setServiceId(siServiceDetail.getTpsServiceId());
					LOGGER.info("Solution of serviceid-->{} , is -->{}", serviceId, solutionDetail);
				}
				solutionDetail.setDual(isDual);
				solutionDetail.setServiceId(serviceId.trim());
				solutionDetail.setAccessType(siServiceDetail.getAccessType());

				// solutionDetail.setIsmultiVrf(siServiceDetail.getIsmultivrf().toString());
				quoteDetail.getSolutions().add(solutionDetail);

				siOrder = siServiceDetail.getSiOrder();
				quoteDetail.setCustomerId(Integer.valueOf(siOrder.getErfCustCustomerId()));

				siContractInfo = siServiceDetail.getSiContractInfo();

				List<RenewalsSiteDetail> siteDetailsList = getSiteDetails(serviceId);

				/*
				 * RenewalsSiteDetail siteDetails = new RenewalsSiteDetail();
				 * siteDetails.setLocationId(Integer.valueOf(siServiceDetail.
				 * getErfLocSiteAddressId())); siteDetailsList.add(siteDetails);
				 */

				RenewalsSite site = new RenewalsSite();
				site.setOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
				site.setSite(siteDetailsList);
				site.setServiceId(serviceId.trim());
				quoteDetail.setProductName(siServiceDetail.getErfPrdCatalogProductName());
				quoteDetail.getSite().add(site);

				if (isDual) {
					if (existingSiServiceDetails.get(0).getMrc() != null
							|| existingSiServiceDetails.get(1).getMrc() != null) {
						renewalsPrice.setMrc((existingSiServiceDetails.get(0).getMrc() != null
								? existingSiServiceDetails.get(0).getMrc()
								: 0D)
								+ (existingSiServiceDetails.get(1).getMrc() != null
										? existingSiServiceDetails.get(0).getMrc()
										: 0D));
					} else {
						renewalsPrice.setMrc(0D);
					}
					if (existingSiServiceDetails.get(0).getNrc() != null
							|| existingSiServiceDetails.get(1).getNrc() != null) {
						renewalsPrice.setNrc((existingSiServiceDetails.get(0).getNrc() != null
								? existingSiServiceDetails.get(0).getNrc()
								: 0D)
								+ (existingSiServiceDetails.get(1).getNrc() != null
										? existingSiServiceDetails.get(0).getNrc()
										: 0D));
					} else {
						renewalsPrice.setNrc(0D);
					}
					if (existingSiServiceDetails.get(0).getArc() != null
							|| existingSiServiceDetails.get(1).getArc() != null) {
						renewalsPrice.setArc((existingSiServiceDetails.get(0).getArc() != null
								? existingSiServiceDetails.get(0).getArc()
								: 0D)
								+ (existingSiServiceDetails.get(1).getArc() != null
										? existingSiServiceDetails.get(0).getArc()
										: 0D));
					} else {
						renewalsPrice.setArc(0D);
					}

				} else {

					if (siServiceDetail.getMrc() != null) {
						renewalsPrice.setMrc(siServiceDetail.getMrc());
					} else {
						renewalsPrice.setMrc(0D);
					}
					if (siServiceDetail.getNrc() != null) {
						renewalsPrice.setNrc(siServiceDetail.getNrc());
					} else {
						renewalsPrice.setNrc(0D);
					}
					if (siServiceDetail.getArc() != null) {
						renewalsPrice.setArc(siServiceDetail.getArc());
					} else {
						renewalsPrice.setArc(0D);
					}
				}
				priceMapping.put(siServiceDetail.getTpsServiceId(), renewalsPrice);

				quoteDetail.setRenewalsPriceBean(priceMapping);
				LOGGER.info("Price of serviceid-->{} , is -->{}", serviceId, priceMapping.toString());
			}
			if (siOrder != null && siContractInfo != null) {
				LOGGER.info("Adding Attributes for -->{}", serviceId);
				List<RenewalsAttributeDetail> quoteLeAttributeValue = getQuoteToLeAttr(siOrder, siContractInfo,
						existingSiServiceDetails.get(0));
				if (quoteLeAttributeValue != null && !quoteLeAttributeValue.isEmpty()) {
					quoteLeAttributeValueList.addAll(quoteLeAttributeValue);
					LOGGER.info("Lost of Attributes of -->{} -->{}", serviceId, quoteLeAttributeValue.toString());
				}
				LOGGER.info("Added Attributes for -->{}", serviceId);
			}
		}

		if (!quoteLeAttributeValueList.isEmpty())
			quoteDetail.setQuoteAttributeList(quoteLeAttributeValueList);
		LOGGER.info("Send Data to OMS -->{}", quoteDetail.toString());
		return quoteDetail;
	}

	public List<RenewalsSiteDetail> getSiteDetails(String serviceId) {
		LOGGER.info("Fetching Site Details for service Id -->{}", serviceId);
		List<RenewalsSiteDetail> siteDetailsList = new ArrayList<RenewalsSiteDetail>();
		List<SIServiceDetail> existingSiServiceDetails = null;
		existingSiServiceDetails = siServiceDetailRepository
				.findByPrimaryTpsSrviceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNullOrderByIdAsc(
						serviceId.trim(), RenewalsServiceAttributeConstants.ACTIVE,
						RenewalsServiceAttributeConstants.IS_ACTIVE);
		if (existingSiServiceDetails != null && existingSiServiceDetails.isEmpty()) {
			existingSiServiceDetails = siServiceDetailRepository
					.findByTpsServiceIdAndServiceStatusIgnoreCaseAndIsActiveAndErfLocSiteAddressIdIsNotNull(
							serviceId.trim(), RenewalsServiceAttributeConstants.ACTIVE,
							RenewalsServiceAttributeConstants.IS_ACTIVE);
		}
		for (SIServiceDetail siServiceDetail : existingSiServiceDetails) {

			if (siServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(RenewalsServiceAttributeConstants.NPL)
					|| siServiceDetail.getErfPrdCatalogProductName()
							.equalsIgnoreCase(RenewalsServiceAttributeConstants.NDE)) {
				RenewalsSiteDetail siteDetails = new RenewalsSiteDetail();
				siteDetails.setLocationId(Integer.valueOf(siServiceDetail.getErfLocSiteAddressId()));
				siteDetailsList.add(siteDetails);
			} else {

				if (siteDetailsList.isEmpty()) {
					RenewalsSiteDetail siteDetails = new RenewalsSiteDetail();
					siteDetails.setLocationId(Integer.valueOf(siServiceDetail.getErfLocSiteAddressId()));
					siteDetailsList.add(siteDetails);
				} else {
					if (siteDetailsList.get(0).getLocationId()
							.intValue() != (Integer.valueOf(siServiceDetail.getErfLocSiteAddressId())).intValue()) {
						RenewalsSiteDetail siteDetails = new RenewalsSiteDetail();
						siteDetails.setLocationId(Integer.valueOf(siServiceDetail.getErfLocSiteAddressId()));
						siteDetailsList.add(siteDetails);
					}
				}

			}
		}
		return siteDetailsList;
	}

}
