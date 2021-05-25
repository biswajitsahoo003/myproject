package com.tcl.dias.serviceinventory.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetailsBean;
import com.tcl.dias.serviceinventory.beans.SiteDetailsSearchRequest;
import com.tcl.dias.serviceinventory.beans.ViewSiServiceInfoAllBean;
import com.tcl.dias.serviceinventory.beans.VwServiceAttributesBean;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.SdwanEndpoints;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SdwanEndpointsRepository;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoBulkCpeResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoCPEBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoSiteListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoBulkSiteListResponse;

/**
 * Service Inventory related Utils
 *
 * 
 */
@Service
public class CiscoServiceInventoryUtils {
	
	@Autowired
	SIServiceAdditionalInfoRepository serviceAdditionalInfoRepository;
	
	@Autowired
	SdwanEndpointsRepository sdwanEndpointsRepository;

	private CiscoServiceInventoryUtils() {
		/* static usage */
	}
	
	
	/**
	 * Method to persist ViewSiServiceInfoAllBean
	 * @param vwSiInfoOverlay
	 * @param siServiceDetail
	 * @param sdwanSysId
	 * @param sdwanServiceIds
	 */
	public void constructVwSiServiceInfoBean(List<ViewSiServiceInfoAllBean> vwSiInfoOverlay,
			SIServiceDetail siServiceDetail, List<Integer> sdwanSysId, List<String> sdwanServiceIds) {
		ViewSiServiceInfoAllBean sdwanSiteDetails = new ViewSiServiceInfoAllBean();
		   sdwanSiteDetails.setSysId(siServiceDetail.getId());
		   sdwanSiteDetails.setAccessType(siServiceDetail.getAccessType());
		   sdwanSiteDetails.setBandwidth(siServiceDetail.getBwPortspeed());
		   sdwanSiteDetails.setBandwidthUnit(siServiceDetail.getBwUnit());
		   sdwanSiteDetails.setCustomerSiteAddress(siServiceDetail.getSiteAddress());
		   sdwanSiteDetails.setDestinationCity(siServiceDetail.getDestinationCity());
		   sdwanSiteDetails.setDestinationCountry(siServiceDetail.getDestinationCountry());
		   sdwanSiteDetails.setErfCustPartnerLeId(String.valueOf(siServiceDetail.getSiOrder().getErfCustPartnerLeId()));
		   sdwanSiteDetails.setGvpnSiteTopology(siServiceDetail.getSiteTopology());
		   sdwanSiteDetails.setIsActive(siServiceDetail.getIsActive());
		   sdwanSiteDetails.setIzoSdwanSrvcId(siServiceDetail.getIzoSdwanSrvcId());
		   sdwanSiteDetails.setIpAddressArrangement(siServiceDetail.getIpAddressArrangementType());
		   sdwanSiteDetails.setLastMileBandwidth(siServiceDetail.getLastmileBw());
		   sdwanSiteDetails.setLastMileBandwidthUnit(siServiceDetail.getLastmileBwUnit());
		   sdwanSiteDetails.setLastMileProvider(siServiceDetail.getLastmileProvider());
		   sdwanSiteDetails.setLatLong(siServiceDetail.getLatLong());
		   sdwanSiteDetails.setOrderCustLeId(siServiceDetail.getSiOrder().getErfCustLeId());
		   sdwanSiteDetails.setOrderCustomerId(Integer.parseInt(siServiceDetail.getSiOrder().getErfCustCustomerId()));
		   sdwanSiteDetails.setPartnerCuid(siServiceDetail.getSiOrder().getPartnerCuid());
		   sdwanSiteDetails.setPopAddress(siServiceDetail.getPopSiteAddress());
		   sdwanSiteDetails.setPrimaryOrSecondary(siServiceDetail.getPrimarySecondary());
		   sdwanSiteDetails.setPrimarySecondaryLink(siServiceDetail.getPriSecServiceLink());
		   sdwanSiteDetails.setPrimaryServiceId(siServiceDetail.getPrimaryTpsSrviceId());
		   sdwanSiteDetails.setProductFamilyId(siServiceDetail.getErfPrdCatalogProductId());
		   sdwanSiteDetails.setProductOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
		   sdwanSiteDetails.setProductOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
		   sdwanSiteDetails.setServiceId(siServiceDetail.getTpsServiceId());
		   sdwanSiteDetails.setServiceStatus(siServiceDetail.getServiceStatus());
		   sdwanSiteDetails.setServiceType(siServiceDetail.getServiceType());
		   sdwanSiteDetails.setSiteAlias(siServiceDetail.getSiteAlias());
		   sdwanSiteDetails.setSourceCity(siServiceDetail.getSourceCity());
		   sdwanSiteDetails.setSourceCountry(siServiceDetail.getSourceCountry());
		   sdwanSiteDetails.setCustomerSiteAddress(siServiceDetail.getSiteAddress());
		   sdwanSiteDetails.setVpnName(siServiceDetail.getVpnName());
		   sdwanSiteDetails.setOrderCustLeId(siServiceDetail.getSiOrder().getErfCustLeId());
		   sdwanSiteDetails.setProductFamilyName(siServiceDetail.getErfPrdCatalogProductName());
		   sdwanSysId.add(siServiceDetail.getId());
		   sdwanServiceIds.add(siServiceDetail.getTpsServiceId());
		   vwSiInfoOverlay.add(sdwanSiteDetails);
	}


	/**
	 * Increment count if status is offline
	 *
	 * @param status
	 * @param offlineCount
	 * @return
	 */
    public Integer incrementCountIfOffline(String status, Integer offlineCount)
    {
        if(ServiceInventoryConstants.OFFLINE.equals(status))
            return offlineCount+1;
        else return offlineCount;
    }

    /**
     * Increment count if status is degraded
     *
     * @param status
     * @param degradedCount
     * @return
     */
    public Integer incrementCountIfDegraded(String status, Integer degradedCount)
    {
        if(ServiceInventoryConstants.DEGRADED.equals(status))
            return degradedCount+1;
        else return degradedCount;
    }
    
    /**
	 * Increment count if status is Online
	 * @param status
	 * @param onlineCount
	 * @return
	 */
	
	 public  Integer incrementCountIfOnline(String status, Integer onlineCount)
	    {
	        if(ServiceInventoryConstants.ONLINE.equals(status))
	            return onlineCount+1;
	        else return onlineCount;
	    }
	 
	 public void constructSdwanSiteDetailsByGroup(SiteDetailsSearchRequest request, SdwanSiteDetailsBean sdwanSiteDetailsBean)
	 {
	 if (Objects.nonNull(request.getGroupBy())
				&& !CommonConstants.ALL.equalsIgnoreCase(request.getGroupBy())) {
			switch (request.getGroupBy()) {
			case ServiceInventoryConstants.ONLINE:
				sdwanSiteDetailsBean.setSiteDetails(sdwanSiteDetailsBean.getSiteDetails().stream()
						.filter(sdwanSiteDetails -> ServiceInventoryConstants.ONLINE
								.equalsIgnoreCase(sdwanSiteDetails.getSiteStatus()))
						.collect(Collectors.toList()));
				break;
			case ServiceInventoryConstants.OFFLINE:
				sdwanSiteDetailsBean.setSiteDetails(sdwanSiteDetailsBean.getSiteDetails().stream()
						.filter(sdwanSiteDetails -> ServiceInventoryConstants.OFFLINE
								.equalsIgnoreCase(sdwanSiteDetails.getSiteStatus()))
						.collect(Collectors.toList()));
				break;
			case ServiceInventoryConstants.DEGRADED:
				sdwanSiteDetailsBean.setSiteDetails(sdwanSiteDetailsBean.getSiteDetails().stream()
						.filter(sdwanSiteDetails -> ServiceInventoryConstants.DEGRADED
								.equalsIgnoreCase(sdwanSiteDetails.getSiteStatus()))
						.collect(Collectors.toList()));
				break;
			}
		} else if (ServiceInventoryConstants.SITE_STATUS.equalsIgnoreCase(request.getSortBy())) {
			if ("desc".equalsIgnoreCase(request.getSortOrder()))
				sdwanSiteDetailsBean.getSiteDetails()
						.sort((site1, site2) -> site2.getSiteStatus().compareTo(site1.getSiteStatus()));
			else
				sdwanSiteDetailsBean.getSiteDetails()
						.sort((site1, site2) -> site1.getSiteStatus().compareTo(site2.getSiteStatus()));
		}
	 }
	 

	/**
	 * Method to construct underlay siteLists
	 * Method to get underlay siteLists 
	 * @param underlaySysIds
	 * @return
	 */
	public List<VwServiceAttributesBean> getUnderlaySiteLists(List<Integer> underlaySysIds) {
		List<Map<String, Object>> templateDetails = serviceAdditionalInfoRepository.findAttributesByUnderlayServiceIds(underlaySysIds, 
				Arrays.asList(ServiceInventoryConstants.SITE_LIST,ServiceInventoryConstants.CISCO_SITE_ID));
		List<VwServiceAttributesBean> underlayTemplates = new ArrayList<>();
		if (templateDetails != null && !templateDetails.isEmpty()) {
			final ObjectMapper mapper = new ObjectMapper();
			templateDetails.stream().forEach(underlayTemplate -> {
				underlayTemplates.add(mapper.convertValue(underlayTemplate, VwServiceAttributesBean.class));
			});
		}
		return underlayTemplates;
	}
	
	/**
	 * @param underlaySiteLists
	 * @param siAsset
	 * @param sdwanCPEBean
	 * @param ciscoBulkSiteListResponse 
	 */
	public void constructSdwanSiteListDetails(List<VwServiceAttributesBean> underlayTemplates, SIAsset siAsset,
			CiscoCPEBean sdwanCPEBean, CiscoBulkSiteListResponse ciscoBulkSiteListResponse) {
		Set<String> siteId= new HashSet<>();
		List<String> name= new ArrayList<>();
		underlayTemplates.stream()
		.filter(underlayTemplate -> siAsset.getSiServiceDetail().getId().equals(underlayTemplate.getSysId()))
		.forEach(template->{
			CiscoSiteListBean templateDetail = new CiscoSiteListBean();
			
			//templateDetail.setTemplateName(template.getAttributeValue());
			templateDetail.setAttributeId(template.getAttributeId());
			if(template.getAttributeName().equalsIgnoreCase("CISCO_SITE_ID")) {
				siteId.add(template.getAttributeValue());
			}
			
			if(template.getAttributeName().equalsIgnoreCase("SITE_LIST")) {
			ciscoBulkSiteListResponse.getCiscoSiteListDetails().stream().forEach(ciscoSiteList->{
				if(ciscoSiteList.getListId().equalsIgnoreCase(template.getAttributeValue())) {
					name.add(ciscoSiteList.getName());
					templateDetail.setListId(template.getAttributeValue());
				}
			});
			templateDetail.setListName(name.get(0));
			}
			templateDetail.setSiteId(siteId);
			sdwanCPEBean.setCiscoSiteList(templateDetail);
		});
	}
	
	/**
	 * To construct si asset attribute
	 *
	 * @param attrValue
	 * @param attrName
	 * @param siAsset
	 * @return
	 */
	public SIAssetAttribute constructSiAssetAttributes(String attrValue, String attrName, SIAsset siAsset) {
		SIAssetAttribute siAssetAttribute = new SIAssetAttribute();
		siAssetAttribute.setAttributeName(attrName);
		siAssetAttribute.setAttributeValue(attrValue);
		siAssetAttribute.setSiAsset(siAsset);
		siAssetAttribute.setCreatedBy(Utils.getSource());
		siAssetAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		siAssetAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		siAssetAttribute.setIsActive("Y");
		siAssetAttribute.setAttributeAltValueLabel(attrValue);
		return siAssetAttribute;
	}
	
	
	/**
	 * Get CPE Status
	 *
	 * @param versaCpeStatusResponse
	 * @param cpeName
	 * @return
	 */
	public String getSdwanCpeStatus(CiscoBulkCpeResponse versaCpeStatusResponse, String cpeName) {
		final String[] status = new String[1];
		versaCpeStatusResponse.getCiscoBulkCpeDetails().stream()
				.filter(appliance -> cpeName.equalsIgnoreCase(appliance.getHostName())).peek(appliance -> {
					if (appliance.getReachability().equals(ServiceInventoryConstants.REACHABLE))
						status[0] = ServiceInventoryConstants.ONLINE;
					else
						status[0] = ServiceInventoryConstants.OFFLINE;
				}).anyMatch(appliance -> cpeName.equalsIgnoreCase(appliance.getHostName()));
		return status[0] != null ? status[0] : ServiceInventoryConstants.OFFLINE;
	}
		
	/**
	 * Method to get servce attributes for Cisco
	 * @param serviceAttrGrouped
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @param tempNames
	 * @param directoryCodes
	 */
	public void getSdwanAttributesForCisco(Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped,
			Map<String, Set<String>> orgTemplates, Map<String, Set<String>> orgDirectoryRegions, Set<String> tempNames,
			Set<String> directoryCodes) {
		serviceAttrGrouped.entrySet().forEach(map->{
			Set<String> templateName = new HashSet<>();
			List<String> orgName = new ArrayList<>();
			Set<String> instanceCode = new HashSet<>();
			map.getValue().stream().forEach(value -> {
				if (Objects.nonNull(value.getAttributeName()) && Objects.nonNull(value.getAttributeValue()) && value
						.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
					orgName.add(0, value.getAttributeValue());
				} else if (Objects.nonNull(value.getAttributeName()) && Objects.nonNull(value.getAttributeValue()) && value
						.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
					instanceCode.add(value.getAttributeValue());
					directoryCodes.addAll(instanceCode);
				} 
				if (Objects.nonNull(value.getUnderlayAttributeName()) && Objects.nonNull(value.getUnderlayAttributeValue())
						&& value.getUnderlayAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SITE_LIST)) {
					templateName.add(value.getUnderlayAttributeValue());
					tempNames.addAll(templateName);
				} 
			});
			if(!orgName.isEmpty()) {
				orgTemplates.put(orgName.get(0), tempNames);
				orgDirectoryRegions.put(orgName.get(0), directoryCodes);
			}
		});
	}
	
	/**
	 * Get SDWAN Endpoints by code
	 *
	 * @param serviceAttrGrouped
	 * @param sdwanEndPointsForRegions
	 * @return
	 */
	public Map<String, List<SdwanEndpoints>> getSdwanEnpointsByCode(
			Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped, List<SdwanEndpoints> sdwanEndPointsForRegions) {
		Set<String> instanceRegions = new HashSet<>();
		serviceAttrGrouped.entrySet().forEach(attr->{
			attr.getValue().stream().forEach(attrValue->{
				if(attrValue.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL)) {
					if(attrValue.getAttributeValue() != null)
						instanceRegions.add(attrValue.getAttributeValue());
				}
			});
		});
		sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode =  sdwanEndPointsForRegions.stream().collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		// Instance mapping value should be same in sdwan_endpoints table
		return instanceByCode;
	}
	
	
	/**
	 * Method to get servce attributes for versa
	 * @param serviceAttrGrouped
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @param tempNames
	 * @param directoryCodes
	 */
	public void getSdwanAttributesForcisco(Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped,
			Map<String, Set<String>> orgTemplates, Map<String, Set<String>> orgDirectoryRegions, Set<String> tempNames,
			Set<String> directoryCodes) {
		serviceAttrGrouped.entrySet().forEach(map->{
			Set<String> templateName = new HashSet<>();
			List<String> orgName = new ArrayList<>();
			Set<String> instanceCode = new HashSet<>();
			map.getValue().stream().forEach(value -> {
				if (Objects.nonNull(value.getAttributeName()) && Objects.nonNull(value.getAttributeValue()) && value
						.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
					orgName.add(0, value.getAttributeValue());
				} else if (Objects.nonNull(value.getAttributeName()) && Objects.nonNull(value.getAttributeValue()) && value
						.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL)) {
					instanceCode.add(value.getAttributeValue());
					directoryCodes.addAll(instanceCode);
				} 
				if (Objects.nonNull(value.getUnderlayAttributeName()) && Objects.nonNull(value.getUnderlayAttributeValue())
						&& value.getUnderlayAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SITE_LIST)) {
					templateName.add(value.getUnderlayAttributeValue());
					tempNames.addAll(templateName);
				} 
			});
			if(!orgName.isEmpty()) {
				orgTemplates.put(orgName.get(0), tempNames);
				orgDirectoryRegions.put(orgName.get(0), directoryCodes);
			}
		});
	}

	}
