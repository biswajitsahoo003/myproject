package com.tcl.dias.serviceinventory.service.v1;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.customexception.TCLException;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.beans.CpeUnderlaySitesBean;
import com.tcl.dias.serviceinventory.beans.SdwanCpeDetails;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetails;
import com.tcl.dias.serviceinventory.beans.VwServiceAttributesBean;
import com.tcl.dias.serviceinventory.constants.ExceptionConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAdditionalInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetAdditionalInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SdwanEndpoints;
import com.tcl.dias.serviceinventory.entity.entities.ViewSiServiceInfoAll;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAssetAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAssetInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SdwanEndpointsRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwSiServiceInfoAllRepository;
import com.tcl.dias.serviceinventory.izosdwan.beans.CiscoAliasUpdateRequest;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.ApplicationEntries;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPoliciesApplicationListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPolicyApplicationBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPolicyApplicationPayload;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoBulkCpeResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoCPEBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoSiteListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.SdwanCiscoCpeAllDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.VedgeInventoryDetailsResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.AssosciatedSiteDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoBulkSiteListResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoPolicyListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListConfigBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListInstanceMapping;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteServiceDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.PolicyTypeList;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.SiteListConfigDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.AppRoutePolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.AppRoutePolicyList;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.Assembly;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.AssemblyDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyActivatedDetailsList;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyDetailsRequestBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyStatusResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoSdwanPolicyBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.DataPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.DataPolicyQos;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.DataPolicySequences;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.Entries;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.Match;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.SequenceEntries;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.Sequences;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosPolicyRules;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosRules;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Rule;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Rules_;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.TrafficSteeringPolicy;
import com.tcl.dias.serviceinventory.util.CiscoServiceInventoryUtils;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import com.tcl.dias.serviceinventory.util.ServiceInventoryUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service class to hold all IZOSDWAN related functionalities for Cisco
 * 
 * 
 *
 */
@Service
public class IzoSdwanCiscoDetailedInfoService {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanCiscoDetailedInfoService.class);
	
	@Autowired
	VwSiServiceInfoAllRepository vwSiServiceInfoAllRepository;
	
	@Autowired
	SIServiceAdditionalInfoRepository serviceAdditionalInfoRepository;
	
	@Autowired
	SdwanEndpointsRepository sdwanEndpointsRepository;
	
	@Autowired
	SIServiceAssetInfoRepository serviceAssetInfoRepository;
	
	@Autowired
	IzoSdwanCiscoService izoSdwanCiscoService;
	
	@Autowired
	CiscoServiceInventoryUtils ciscoServiceInventoryUtils;
	
	@Autowired
	IzoSdwanInventoryService izoSdwanInventoryService;
	
	@Autowired
	SIServiceAssetAdditionalInfoRepository siServiceAssetAdditionalInfoRepository;
	
	@Autowired
	SIAssetRepository siAssetRepository;
	
	@Autowired
	SIAssetAttributeRepository siAssetAttributeRepository;
	
	@Autowired
	SiServiceAttributeRepository siServiceAttributeRepository;
		
	/**
	 * Method to get detailed SI info
	 *
	 * @param serviceId
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public CiscoSiteServiceDetailBean getSdwanServiceDetailInfo(String serviceId, Integer productId)
			throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		CiscoSiteServiceDetailBean response = new CiscoSiteServiceDetailBean();
		try {
			List<Map<String, Object>> details = vwSiServiceInfoAllRepository.getSiServiceDetailsAttrByServiceId(
					Arrays.asList(serviceId), productId,
					Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
							ServiceInventoryConstants.V_MANAGE_URL));
			List<Map<String, Object>> underlayDetails = vwSiServiceInfoAllRepository
					.findUnderlayServiceIdbySdwanServiceId(Collections.singletonList(serviceId));
			if (details == null || details.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			List<Integer> underlaySysIds = new ArrayList<>();
			underlayDetails.forEach(underlayDetail -> {
				underlaySysIds.add((Integer) underlayDetail.get("sys_id"));
			});
//			List<Map<String, Object>> templateDetails = serviceAdditionalInfoRepository
//					.findAttributesByUnderlayServiceIds(underlaySysIds,
//							Arrays.asList(ServiceInventoryConstants.SITE_LIST,ServiceInventoryConstants.CISCO_SITE_ID));
			List<VwServiceAttributesBean> templateDetails = ciscoServiceInventoryUtils.getUnderlaySiteLists(underlaySysIds);
			Set<String> instanceRegions = new HashSet<>();
			Set<String> cpeNames = new HashSet<>();
			details.stream().forEach(map -> {
				LOGGER.info("Inside getSdwanServiceDetailInfo for service id {} ", map.get("serviceId"));
				response.setServiceDetailId((Integer) map.get("sysId"));
				response.setServiceId((String) map.get("serviceId"));
				response.setSiteName((String) map.get("serviceId"));
				response.setProductFamilyName((String) map.get("productFamily"));
				response.setOfferingName((String) map.get("offeringName"));
				response.setCity((String) map.get("destCity"));
				response.setCountry((String) map.get("destCountry"));
				response.setPrimaryOrSecondary((String) map.get("primaryOrSecondary"));
				response.setPrimarySecLink((String) map.get("primarySecondaryLink"));
				response.setSiteAddress((String) map.get("siteAddress"));
				response.setSiteAlias((String) map.get("siteAlias"));
				response.setCustomerId((String) map.get("customerId"));
				response.setCustomerLeId((Integer) map.get("customerLeId"));
				response.setIsActive(map.get("isActive").toString());
				response.setIzoSdwanSrvcId((String) map.get("izoSdwanServiceId"));
				String attrName = (String) map.get("attributeName");
				String attrValue = (String) map.get("attributeValue");
				LOGGER.info("Fetching service detail for serviceID {} and attributeName {} and value {} ", serviceId,
						attrName, attrValue);
				if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
					response.setOrganasationName(attrValue);
				} else if (attrName.equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL)) {
					response.setInstanceRegion(attrValue);
					instanceRegions.add(attrValue);
				}
			});

			List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
					.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
			CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
			Set<CiscoSiteListBean> templateNames = new HashSet<>();
			CiscoSiteListBean templateDetail = new CiscoSiteListBean();
			Set<String> siteId= new HashSet<>();
			List<String> name= new ArrayList<>();
			if (templateDetails != null && !templateDetails.isEmpty()) {
				templateDetails.stream().forEach(template->{
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
					templateNames.add(templateDetail);
				});
			}
			CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
			if (Objects.nonNull(ciscoBulkcpeStatusResponse)
					&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
				// check if service id is izosdwan service id or underlay service id
				Map<String, String> cpeStatus = new HashMap<>();
				List<CpeUnderlaySitesBean> cpeUnderlaySites = new ArrayList<>();
				if (Objects.isNull(response.getIzoSdwanSrvcId())) {
					underlayDetails.forEach(underlayDetail -> {
						CpeUnderlaySitesBean cpeUnderlaySitesBean = new CpeUnderlaySitesBean();
						cpeUnderlaySitesBean.setId((Integer) underlayDetail.get("sys_id"));
						cpeUnderlaySitesBean.setSiteName((String) underlayDetail.get("service_id"));
						cpeUnderlaySitesBean.setAssetId((Integer) underlayDetail.get("asset_sys_id"));
						cpeUnderlaySitesBean.setCpeName((String) underlayDetail.get("asset_name"));
						cpeNames.add((String) underlayDetail.get("asset_name"));
						cpeUnderlaySitesBean.setUnderlayProductName((String) underlayDetail.get("underlayProductName"));
						cpeUnderlaySitesBean.setControllers(new ArrayList<>());
						SdwanCpeDetails sdwanCpeDetail = new SdwanCpeDetails();
						sdwanCpeDetail.setCpeName((String) underlayDetail.get("asset_name"));
//						getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, cpeUnderlaySitesBean.getCpeName(),
//								cpeStatus, cpeUnderlaySitesBean.getControllers());
						Map<String, String> links = new HashMap<>();
						izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetail, cpeStatus,
								new SdwanSiteDetails(), instanceByCode,links);
						cpeUnderlaySitesBean.setSiteStatus(cpeStatus.get(ServiceInventoryConstants.CPE_STATUS));
						cpeUnderlaySites.add(cpeUnderlaySitesBean);
						response.setOnlineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOnline(cpeUnderlaySitesBean.getSiteStatus(),
								response.getOnlineCpeCount()));
						response.setOfflineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOffline(cpeUnderlaySitesBean.getSiteStatus(),
								response.getOfflineCpeCount()));
						if (ServiceInventoryConstants.ONLINE.equals(cpeUnderlaySitesBean.getSiteStatus())) {
//							getWanStatusFromVersa(cpeUnderlaySitesBean.getCpeName(),
//									cpeUnderlaySitesBean.getControllers(), response.getInstanceRegion(),
//									response.getOrganasationName(), instanceByCode, links);
							cpeUnderlaySitesBean.setLinks(new ArrayList<>());
							if (!links.isEmpty()) {
								links.forEach((linkName, linkStatus) -> {
									Attributes attributes = new Attributes();
									attributes.setAttributeName(linkName);
									attributes.setAttributeValue(linkStatus);
									cpeUnderlaySitesBean.getLinks().add(attributes);
									response.setLinkUpCount(ServiceInventoryConstants.UP.equalsIgnoreCase(linkStatus)
											? response.getLinkUpCount() + 1
											: response.getLinkUpCount());
									response.setLinkDownCount(
											ServiceInventoryConstants.DOWN.equalsIgnoreCase(linkStatus)
													? response.getLinkDownCount() + 1
													: response.getLinkDownCount());
								});
							} else
								response.setLinkDownCount(response.getLinkDownCount() + 1);
						} else
							response.setLinkDownCount(response.getLinkDownCount() + 1);
					});
					response.setCpeUnderlaySites(cpeUnderlaySites);
					response.setSiteStatus(response.getLinkUpCount() == 0 ? ServiceInventoryConstants.OFFLINE
							: (response.getLinkDownCount() == 0 ? ServiceInventoryConstants.ONLINE
									: ServiceInventoryConstants.DEGRADED));
				} else {
					SIServiceAssetInfo assetInfo = serviceAssetInfoRepository.findByServiceId(serviceId);
					response.setSiteStatus(ciscoServiceInventoryUtils.getSdwanCpeStatus(ciscoBulkcpeStatusResponse, assetInfo.getAssetName()));
				}
				//LOGGER.info("Sdwan Template names {} for serviceid {} ", templateNames, serviceId);
				if (!templateNames.isEmpty())
					response.setCiscoSiteList(templateNames);
				if (!cpeNames.isEmpty())
					response.setCpeNames(cpeNames);
				response.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
				return response;
			} else
				throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}

		}

	public SdwanCiscoCpeAllDetailBean getCpeDetailsBasedOnAssetName(Integer customerId, Integer customerLeId,
			Integer partnerId, String assetName, Integer productId) throws TclCommonException, IOException {

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		izoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);

		SdwanCiscoCpeAllDetailBean sdwanCpeAllDetailBean = new SdwanCiscoCpeAllDetailBean();
		List<Map<String, Object>> siServiceAssetInfo = vwSiServiceInfoAllRepository
				.findByCiscoAssetNameAndAssetTag(customerId, partnerId, customerIds, partnerLeIds, assetName, "SDWAN CPE");
		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesForCustomer(
				customerIds, partnerLeIds, productId, customerId, partnerId,
				Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
						ServiceInventoryConstants.V_MANAGE_URL));
		Set<String> instanceRegions = new HashSet<>();
		List<VwServiceAttributesBean> vwServiceAttributeBean = new ArrayList<>();
		if (serviceAttributes != null & !serviceAttributes.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			serviceAttributes.stream().forEach(srvAttribute -> {
				vwServiceAttributeBean.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
			});
		}
		LOGGER.info("Overlay attributes for CPE {}", vwServiceAttributeBean.toString());
		vwServiceAttributeBean.stream().filter(
				attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
				.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		List<Integer> underlaySysIds = new ArrayList<>();
		List<String> siteName=new ArrayList<>();
		siServiceAssetInfo.stream().forEach(asset -> {
			underlaySysIds.add((Integer) asset.get("assetId"));
			siteName.add((String)asset.get("izoSdwanSrvcId"));
		});
		Map<String, Object> sdWanSiteDetails= new HashMap<String, Object>();
		if(Objects.nonNull(siteName))
			sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSite(siteName.get(0));
		if(sdWanSiteDetails.get("siteAlias") !=null)
			sdwanCpeAllDetailBean.setSiteAlias((String) sdWanSiteDetails.get("siteAlias"));
		else
			sdwanCpeAllDetailBean.setSiteAlias("NA");
		List<SIServiceAssetAdditionalInfo> siServiceAttributes = siServiceAssetAdditionalInfoRepository
				.findByAssetSysIdInAndAttributeNameIn(underlaySysIds,
						Arrays.asList(ServiceInventoryConstants.SDWAN_CPE_ALIAS));
		// REST API call to fetch all the CPE details
		CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
		VedgeInventoryDetailsResponse vedgeInventoryDetailsResponse = izoSdwanCiscoService.getSerialNumberFromCisco(instanceByCode);
		CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
		if (Objects.nonNull(ciscoBulkcpeStatusResponse)
				&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
			siServiceAssetInfo.stream().peek(assets -> {
				sdwanCpeAllDetailBean.setId((Integer) assets.get("assetId"));
				sdwanCpeAllDetailBean.setCpeName((String) assets.get("assetName"));
				sdwanCpeAllDetailBean.setModel((String) assets.get("model"));
				vedgeInventoryDetailsResponse.getVedgeInventoryDetailList().stream()
				.filter(inv->sdwanCpeAllDetailBean.getCpeName().equalsIgnoreCase(inv.getHostName()))
				.forEach(invDetail->{
					sdwanCpeAllDetailBean.setSerialNumber(invDetail.getSerialNumber());
				});
				//sdwanCpeAllDetailBean.setSerialNumber((String) assets.get("serialNo"));
				if(!siServiceAttributes.isEmpty()) {
					sdwanCpeAllDetailBean.setCpeAlias(siServiceAttributes.get(0).getAttributeValue());
				}
				sdwanCpeAllDetailBean.setSiteAddress((String) assets.get("customerSiteAddress"));
				sdwanCpeAllDetailBean.setCity((String) assets.get("sourceCity"));
				sdwanCpeAllDetailBean.setCountry((String) assets.get("sourceCountry"));
				sdwanCpeAllDetailBean.setUnderlayServiceId((String) assets.get("assetServiceId"));
				sdwanCpeAllDetailBean.setSdwanServiceId((String) assets.get("izoSdwanSrvcId"));
				sdwanCpeAllDetailBean.setUnderlaySysId((Integer) assets.get("assetSysId"));
				sdwanCpeAllDetailBean.setSiteName((String) assets.get("izoSdwanSrvcId"));
				// fetch status of CPE from versa
				Map<String, String> map = new HashMap<>();
//				getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCpeAllDetailBean.getCpeName(), map,
//						sdwanCpeAllDetailBean.getControllers());
				SdwanCpeDetails sdwanCpeDetail = new SdwanCpeDetails();
				sdwanCpeDetail.setCpeName((String) assets.get("assetName"));
				Map<String, String> links = new HashMap<>();
				izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetail, map,
						new SdwanSiteDetails(), instanceByCode,links);
				sdwanCpeAllDetailBean.setCpeStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
				sdwanCpeAllDetailBean.setCpeAvailability(map.get(ServiceInventoryConstants.CPE_AVAILABILITY));
				sdwanCpeAllDetailBean.setOsVersion(map.get(ServiceInventoryConstants.OS_VERSION));
				sdwanCpeAllDetailBean.setSku(map.get(ServiceInventoryConstants.SKU));
				sdwanCpeAllDetailBean.setManufacturer("CISCO");
				sdwanCpeAllDetailBean.setLastUpdateDate(map.get(ServiceInventoryConstants.LAST_UPDATED_DATE));
//				Optional<ViewSiServiceInfoAll> sdwanSystemServiceIdOptional = vwSiServiceInfoAllRepository
//						.findByServiceId(sdwanCpeAllDetailBean.getSdwanServiceId());
				Set<String> siteId= new HashSet<>();
				CiscoSiteListBean ciscoSiteListBean= new CiscoSiteListBean();
				ciscoSiteListBean.setAttributeId((Integer) assets.get("templateId"));
				ciscoSiteListBean.setListId((String) assets.get("attributeValue"));
				//ciscoSiteListBean.setListName(listName);
				ciscoBulkSiteListResponse.getCiscoSiteListDetails().stream()
				.filter(listdetail->listdetail.getListId().equalsIgnoreCase(ciscoSiteListBean.getListId()))
				.forEach(siteListRes->{
					ciscoSiteListBean.setListName(siteListRes.getName());
					siteListRes.getSiteIdentries().stream().forEach(siteIdDet->{
						siteId.add(siteIdDet.getSiteId());
					});
				});
				ciscoSiteListBean.setSiteId(siteId);
				sdwanCpeAllDetailBean.setCiscoSiteList(ciscoSiteListBean);
				
				
			}).findAny().get();
		} else
			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		sdwanCpeAllDetailBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
		return sdwanCpeAllDetailBean;
	}



	public String updateAliasName(CiscoAliasUpdateRequest request, String type) throws TclCommonException {
		String response = CommonConstants.FAILIURE;
		try {
			if (type.equalsIgnoreCase(ServiceInventoryConstants.CPE)) {
				if (request.getCpeAssetIds() == null || request.getCpeAssetIds().isEmpty())
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				else {
					List<SIAsset> siAssets = siAssetRepository.findAllById(request.getCpeAssetIds());
					List<SIAssetAttribute> siAssetAttributes = siAssetAttributeRepository
							.findBySiAssetInAndAttributeName(siAssets,
									Arrays.asList(ServiceInventoryConstants.SDWAN_CPE_ALIAS));
					List<SIAssetAttribute> cpeAliasList = new ArrayList<>();
					if (siAssetAttributes.isEmpty()) {
						request.getCpeAssetIds().stream().forEach(assetId -> {
							SIAsset siAsset = siAssetRepository.findById(assetId).get();
							cpeAliasList.add(ciscoServiceInventoryUtils.constructSiAssetAttributes(request.getCpeAlias(),
									ServiceInventoryConstants.SDWAN_CPE_ALIAS, siAsset));

						});
						siAssetAttributeRepository.saveAll(cpeAliasList);

					} else {
						List<Integer> siServDetailId = new ArrayList<>();
						siAssetAttributes.stream().forEach(serAttr -> siServDetailId.add(serAttr.getSiAsset().getId()));

						siAssetAttributes.stream().forEach(siServAttr -> {
							request.getCpeAssetIds().stream().forEach(assetId -> {
								if (siServDetailId.contains(assetId)) {
									siServAttr.setAttributeValue(request.getCpeAlias());
									cpeAliasList.add(siServAttr);
								} else {
									SIAsset siAsset = siAssetRepository.findById(assetId).get();
									cpeAliasList.add(ciscoServiceInventoryUtils.constructSiAssetAttributes(request.getCpeAlias(),
											ServiceInventoryConstants.SDWAN_CPE_ALIAS, siAsset));
								}
							});
						});
						siAssetAttributeRepository.saveAll(cpeAliasList);
					}
				}

			} else if (type.equalsIgnoreCase(ServiceInventoryConstants.SITELIST)) {

				if (request.getSiteListServiceIds() == null || request.getSiteListServiceIds().isEmpty())
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				else {
					List<SIServiceAttribute> siServiceAttributes = siServiceAttributeRepository
							.findAllById(request.getSiteListServiceIds());
					List<SIServiceAttribute> SiteListAliasList = new ArrayList<>();
					siServiceAttributes.stream().forEach(siServAttr -> {
						request.getSiteListServiceIds().stream().forEach(siteListServiceId -> {
							if (siServAttr.getId().equals(siteListServiceId)) {
								siServAttr.setAttributeAltValueLabel(request.getSiteListAlias());
								SiteListAliasList.add(siServAttr);
							}
						});
					});
					siServiceAttributeRepository.saveAll(SiteListAliasList);
				}
			
			} else
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			response = CommonConstants.SUCCESS;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;

	}

	public CiscoSiteListDetailBean getSiteListConfigDetailedInfo(Integer customerId, Integer customerLeId,
			Integer partnerId, String siteListId, Integer productId, HttpServletRequest request) throws IOException, TclCommonException{

		LOGGER.info("Entering method to fetch template detailed info");
		CiscoSiteListDetailBean ciscoSiteListDetail = new CiscoSiteListDetailBean();

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();

		izoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);

		List<SIServiceAdditionalInfo> siteListsinfo = serviceAdditionalInfoRepository
				.findByAttributeNameAndAttributeValue(ServiceInventoryConstants.SITE_LIST, siteListId);
		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository.findBysiteListIdNames(
				Collections.singletonList(siteListId), customerId, customerIds, partnerId, partnerLeIds);
		List<Integer> overlaySysIds = underlaysOverlaysJoined.stream().map(site -> (Integer) site.get("overlaySysId"))
				.collect(Collectors.toList());

		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				overlaySysIds,Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL));

		Set<CiscoSiteListInstanceMapping> ciscoSiteListInstanceMappings = mapOverlayAndUnderlayAttributes(serviceAttributes,
				underlaysOverlaysJoined);
		Set<String> instanceRegions = ciscoSiteListInstanceMappings.stream().map(CiscoSiteListInstanceMapping::getInstanceRegion)
				.collect(Collectors.toSet());
		List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndpoints.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		Set<String> serviceIds = underlaysOverlaysJoined.stream()
				.map(underlay -> (String) underlay.get("overlaySrvcId")).collect(Collectors.toSet());
		List<Map<String, Object>> underlaySites = vwSiServiceInfoAllRepository.findUnderlaysByCustomer(customerId,
				partnerId, customerIds, partnerLeIds);
		LOGGER.info("Number of underlaySites : {}", underlaySites.size());
		LOGGER.info("Number of underlaySites : {}", underlaysOverlaysJoined.size());
		List<ViewSiServiceInfoAll> siteInfos = vwSiServiceInfoAllRepository.findByServiceIdInAndServiceStatusNotIn(new ArrayList<>(serviceIds),Arrays.asList(ServiceInventoryConstants.TERMINATED,ServiceInventoryConstants.UNDER__PROVISIONING));
		List<String> underLayServiceIds = underlaysOverlaysJoined.stream()
				.map(underlaySite -> (String) underlaySite.get("underlaySrvcId")).collect(Collectors.toList());
		List<SIServiceAssetInfo> cpeDetails = serviceAssetInfoRepository.findByServiceIdInAndAssetTag(underLayServiceIds, ServiceInventoryConstants.SDWAN_CPE);
		List<SiteListConfigDetails> siteListConfigDetailsList = new ArrayList<>();
		ciscoSiteListDetail.setAssosciatedSites(new ArrayList<>(serviceIds));
		ciscoSiteListDetail.setAssociatedSitesCount(siteInfos.size());
		ciscoSiteListDetail.setAttributeIds(new ArrayList<>());
		siteListsinfo.forEach(siteList -> ciscoSiteListDetail.getAttributeIds().add(siteList.getAttributeSysId()));
		siteListsinfo.stream().findAny()
				.ifPresent(siteList -> ciscoSiteListDetail.setSiteListAlias(siteList.getDisplayValue()));
		
		// fetch cpe information (status,technical info) according to instance region
		Map<String, String> map = new HashMap<>();
		CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
		CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
		if (Objects.nonNull(ciscoBulkcpeStatusResponse)
				&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
			if (Objects.nonNull(underlaysOverlaysJoined) && !underlaysOverlaysJoined.isEmpty()) {
				Set<String> recurringTemplates = new HashSet<>();
				underlaysOverlaysJoined.forEach(template -> {
					Set<CiscoCPEBean> sdwanCpe = new HashSet<>();
					Set<AssosciatedSiteDetails> sitedetailsSet= new HashSet<>();
					cpeDetails.stream()
							.filter(cpe -> cpe.getServiceId().equalsIgnoreCase((String) template.get("underlaySrvcId")) && 
									StringUtils.isNotBlank(cpe.getAssetName()))
							.peek(cpeDetail -> {
								CiscoCPEBean sdwanCpeDetail = new CiscoCPEBean();
								sdwanCpeDetail.setCpeName(cpeDetail.getAssetName());
								sdwanCpeDetail.setUnderlayServiceId(cpeDetail.getServiceId());
								sdwanCpeDetail.setSdwanServiceId(vwSiServiceInfoAllRepository
										.findById(cpeDetail.getServiceSystemId()).get().getIzoSdwanSrvcId());
								sdwanCpe.add(sdwanCpeDetail);
							}).anyMatch(cpe -> cpe.getServiceId()
									.equalsIgnoreCase((String) template.get("underlaySrvcId")));
					if (Objects.nonNull(underlaySites) && !underlaySites.isEmpty()) {
						sdwanCpe.forEach(sdwanCPE -> {
							mappingUplinkandDownlinkCount(sdwanCPE,underlaySites,ciscoBulkcpeStatusResponse,map,instanceByCode);
							});
					}
				
					if (!recurringTemplates.contains(template.get("attributeValue"))) {
						SiteListConfigDetails siteListDetail = new SiteListConfigDetails();
						mappingSiteListConfigDetails(siteListDetail,sdwanCpe,ciscoBulkSiteListResponse,
								recurringTemplates,template,sitedetailsSet,siteListConfigDetailsList);
					} else {
						siteListConfigDetailsList.stream().filter(templateDetail -> ((String) template.get("attributeValue"))
								.equalsIgnoreCase(templateDetail.getSiteListId())).peek(templateDetail -> {
									templateDetail.getSiteDetails().addAll(sitedetailsSet);
									templateDetail.getAttributeId().add((Integer) template.get("templateId"));
									if (Objects.nonNull(template.get("overlaySrvcId")))
										templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
									templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
								}).anyMatch(templateDetail -> ((String) template.get("attributeValue"))
										.equalsIgnoreCase(templateDetail.getSiteListId()));
					}
				});
			}
		
		}
		else
			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		//ciscoSiteListDetail.setSiteListConfigDetails(siteListConfigDetailsList);
		ciscoSiteListDetail.setAssosciatedSiteDetails(new ArrayList<>());
		
		List<String> siteListIdList= new ArrayList<>();
		siteListConfigDetailsList.stream().forEach(sitelist->{
			siteListIdList.add(sitelist.getSiteListId());
			ciscoSiteListDetail.getAssosciatedSiteDetails().addAll(sitelist.getSiteDetails());
			ciscoSiteListDetail.setSiteListId(sitelist.getSiteListId());
			ciscoSiteListDetail.setSiteListName(sitelist.getSiteListName());
			});
		ciscoSiteListDetail.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
		//fetching policy list
		List<CiscoSdwanPolicyBean> ciscoSdwanPolicyList= new ArrayList<>();
		CiscoPolicyActivatedDetailsList ciscoActivatedDetailList=izoSdwanCiscoService.
				getPolicyDetailsByPolicyActivated(ciscoSiteListInstanceMappings,ciscoSiteListDetail,ciscoSdwanPolicyList,instanceByCode,request,ciscoBulkSiteListResponse);
				return ciscoSiteListDetail;

			
	}
	
	private void mappingSiteListConfigDetails(SiteListConfigDetails siteListDetail, Set<CiscoCPEBean> sdwanCpe,
			CiscoBulkSiteListResponse ciscoBulkSiteListResponse, Set<String> recurringTemplates, 
			Map<String, Object> template, Set<AssosciatedSiteDetails> sitedetailsSet, 
			List<SiteListConfigDetails> templateDetails) {
		siteListDetail.setAttributeId(new ArrayList<>());
		siteListDetail.getAttributeId().add((Integer) template.get("templateId"));
		siteListDetail.setSiteListId((String) template.get("attributeValue"));
		sdwanCpe.stream().forEach(cpe->{
			AssosciatedSiteDetails sitedetails= new AssosciatedSiteDetails();
			cpe.getCpeUnderlaySites().stream().forEach(cpeunderlay->{
				sitedetails.setSiteName(cpeunderlay.getSiteName());
			});
			sitedetails.setSdwanSiteStaus(cpe.getSdwanSiteStatus());
			sitedetailsSet.add(sitedetails);
		});
		
		siteListDetail.setSiteDetails(sitedetailsSet);
		ciscoBulkSiteListResponse.getCiscoSiteListDetails().stream()
		.filter(listdetail->listdetail.getListId().equalsIgnoreCase(siteListDetail.getSiteListId()))
		.forEach(siteListRes->{
			siteListDetail.setSiteListName(siteListRes.getName());
			
		});
		siteListDetail.setSiteListAlias((String) template.get("dispVal"));
		siteListDetail.setSdwanServiceId(new HashSet<>());
		if (Objects.nonNull(template.get("overlaySrvcId")))
			siteListDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
		siteListDetail.setAssociatedSitesCount(siteListDetail.getSdwanServiceId().size());
		templateDetails.add(siteListDetail);
		recurringTemplates.add(siteListDetail.getSiteListId());
		
	}

	private void mappingUplinkandDownlinkCount(CiscoCPEBean sdwanCPE, List<Map<String, Object>> underlaySites,
			CiscoBulkCpeResponse ciscoBulkcpeStatusResponse, Map<String, String> map, Map<String, List<SdwanEndpoints>> instanceByCode) {

		List<CpeUnderlaySitesBean> sdwanSiteAndStatus = new ArrayList<>();
		underlaySites.stream()
				.filter(underlaySite -> sdwanCPE.getSdwanServiceId()
						.equals(((String) underlaySite.get("sdwanServiceId"))))
				.forEach(underlayService -> {
					CpeUnderlaySitesBean cpeUnderlaySitesBean = new CpeUnderlaySitesBean();
					cpeUnderlaySitesBean.setSiteName((String) underlayService.get("sdwanServiceId"));
					cpeUnderlaySitesBean.setCpeName((String) underlayService.get("asset_name"));
					Map<String, String> links = new HashMap<>();
					SdwanCpeDetails sdwanCpeDetails =new SdwanCpeDetails();
					sdwanCpeDetails.setCpeName(cpeUnderlaySitesBean.getCpeName());
					izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetails, map,
							new SdwanSiteDetails(), instanceByCode,links);
					cpeUnderlaySitesBean
					.setSiteStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
					sdwanCPE.setUnderlaysOnlineCount(
					ciscoServiceInventoryUtils.incrementCountIfOnline(cpeUnderlaySitesBean.getSiteStatus(),
							sdwanCPE.getUnderlaysOnlineCount()));
					sdwanCPE.setUnderlaysOfflineCount(
					ciscoServiceInventoryUtils.incrementCountIfOffline(cpeUnderlaySitesBean.getSiteStatus(),
							sdwanCPE.getUnderlaysOfflineCount()));
					sdwanSiteAndStatus.add(cpeUnderlaySitesBean);
			if (ServiceInventoryConstants.ONLINE
					.equals(cpeUnderlaySitesBean.getSiteStatus())) {
				cpeUnderlaySitesBean.setLinks(new ArrayList<>());
				if (!links.isEmpty()) {
					links.forEach((linkName, linkStatus) -> {
						LOGGER.info("Inside SDWAN CPE listing processing link and link name {} ", linkName);
						Attributes attributes = new Attributes();
						attributes.setAttributeName(linkName);
						attributes.setAttributeValue(linkStatus);
						cpeUnderlaySitesBean.getLinks().add(attributes);
						sdwanCPE.setLinkUpCount(
								ServiceInventoryConstants.UP.equalsIgnoreCase(linkStatus)
										? sdwanCPE.getLinkUpCount() + 1
										: sdwanCPE.getLinkUpCount());
						sdwanCPE.setLinkDownCount(
								ServiceInventoryConstants.DOWN.equalsIgnoreCase(linkStatus)
										? sdwanCPE.getLinkDownCount() + 1
										: sdwanCPE.getLinkDownCount());
					});
				} else
					sdwanCPE.setLinkDownCount(sdwanCPE.getLinkDownCount() + 1);
			} else
				sdwanCPE.setLinkDownCount(sdwanCPE.getLinkDownCount() + 1);

					
				});
		sdwanCPE.setCpeUnderlaySites(sdwanSiteAndStatus);
		sdwanCPE.setSdwanSiteStatus(
				sdwanCPE.getLinkUpCount() == 0 ? ServiceInventoryConstants.OFFLINE
						: (sdwanCPE.getLinkDownCount() == 0 ? ServiceInventoryConstants.ONLINE
								: ServiceInventoryConstants.DEGRADED));
			
	}

	private Set<CiscoSiteListInstanceMapping> mapOverlayAndUnderlayAttributes(List<Map<String, Object>> serviceAttributes,
			List<Map<String, Object>> underlaysOverlaysJoined) {
		serviceAttributes
				.sort((a1, a2) -> ((String) a1.get("serviceId")).compareToIgnoreCase((String) a2.get("serviceId")));
		Set<CiscoSiteListInstanceMapping> ciscoSiteListInstanceMappingSet = new HashSet<>();
		String instanceRegion[] = { "" };
		serviceAttributes.forEach(overlayAttribute -> {
			if (ServiceInventoryConstants.V_MANAGE_URL
					.equalsIgnoreCase((String) overlayAttribute.get("attributeName")))
				instanceRegion[0] = (String) overlayAttribute.get("attributeValue");
			if (!StringUtils.isAllBlank(instanceRegion[0])) {
				underlaysOverlaysJoined.forEach(underlayAndOverlay -> {
					if (underlayAndOverlay.get("overlaySrvcId") != null
							&& ((String) underlayAndOverlay.get("overlaySrvcId"))
									.equalsIgnoreCase((String) overlayAttribute.get("serviceId"))) {
						if (ServiceInventoryConstants.SITE_LIST
								.equalsIgnoreCase((String) underlayAndOverlay.get("attributeName"))) {
							CiscoSiteListInstanceMapping ciscoSiteListInstanceMapping = new CiscoSiteListInstanceMapping();
							ciscoSiteListInstanceMapping.setInstanceRegion(instanceRegion[0]);
							ciscoSiteListInstanceMapping.setSiteListId((String) underlayAndOverlay.get("attributeValue"));
							ciscoSiteListInstanceMappingSet.add(ciscoSiteListInstanceMapping);
						}
					}
				});
				instanceRegion[0] = "";
			}
		});
		return ciscoSiteListInstanceMappingSet;
	}

	public String updateSiteListConfig(CiscoSiteListConfigBean ciscoSiteListConfigBean,Integer customerId, Integer customerLeId,
			Integer partnerId, HttpServletRequest request) throws TclCommonException {
		LOGGER.info("In updateSiteListConfig method - request body param : {}", ciscoSiteListConfigBean);
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();

		izoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
		List<String> finalResponse = new ArrayList<>();
		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository.findBysiteListIdNames(
				Collections.singletonList(ciscoSiteListConfigBean.getSiteListId()), customerId, customerIds, partnerId, partnerLeIds);
		List<Integer> overlaySysIds = underlaysOverlaysJoined.stream().map(site -> (Integer) site.get("overlaySysId"))
				.collect(Collectors.toList());

		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				overlaySysIds,Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL));

		Set<CiscoSiteListInstanceMapping> ciscoSiteListInstanceMappings = mapOverlayAndUnderlayAttributes(serviceAttributes,
				underlaysOverlaysJoined);
		
		
		ciscoSiteListConfigBean.getDefintionBasedPolicies().stream().forEach(defntionBean->{
			Set<String> regions = ciscoSiteListInstanceMappings.stream().map(CiscoSiteListInstanceMapping::getInstanceRegion)
					.collect(Collectors.toSet());
				List<Sequences> sequences = new ArrayList<>();
				List<DataPolicySequences> qosSequences = new ArrayList<>();
				List<String> trafficStearingPolicyName = new ArrayList<>();
				List<String> qosPolicyName = new ArrayList<>();
				List<AppRoutePolicy> trafficSteeringList = new ArrayList<>();
				List<DataPolicy> qosPolicyList = new ArrayList<>();
				if (Objects.nonNull(defntionBean.getDefinitionId())) {
					String definitionId = defntionBean.getDefinitionId();
//						if ( Objects.nonNull(defntionBean.getPolicytypeList())) {
//							defntionBean.getPolicytypeList().stream().forEach(policyType->{
//							if (policyType.getPolicyType().equals(ServiceInventoryConstants.TRAFFIC_STEERING)) {
//								trafficStearingPolicyName.addAll(policyType.getPolicyName());
//							}
//							else if (policyType.getPolicyType().equals(ServiceInventoryConstants.QOS)) {
//								
//									qosPolicyName.addAll(policyType.getPolicyName());
//							}
//							
//							});
//						}
					if(Objects.nonNull(defntionBean.getPolicyType()) && Objects.nonNull(defntionBean.getPolicyName())) {
						if(defntionBean.getPolicyType().equalsIgnoreCase(ServiceInventoryConstants.TRAFFIC_STEERING)) {
							trafficStearingPolicyName.addAll(defntionBean.getPolicyName());
						}
						else if(defntionBean.getPolicyType().equalsIgnoreCase(ServiceInventoryConstants.QOS)) {
							qosPolicyName.addAll(defntionBean.getPolicyName());
						}
						
					}
					
					LOGGER.info("In updateSiteListDetails method - siteListName : {}", ciscoSiteListConfigBean.getSiteListName());
					LOGGER.info("In updateSiteListDetails method - regions : {}", regions);
					LOGGER.info("In updateSiteListDetails method - trafficStearingPolicyName rearrangement: {}",
							trafficStearingPolicyName);
					LOGGER.info("In updateSiteListDetails method - qosPolicyName rearrangement: {}",
							qosPolicyName);
					List<SdwanEndpoints> sdwanEndpointsList = new ArrayList<>();
					regions.stream().forEach(atr -> {
						try {
							SdwanEndpoints endpoint = sdwanEndpointsRepository.findByServerCode(atr);
							sdwanEndpointsList.add(endpoint);
							List<AppRoutePolicy>trafficSteering = izoSdwanCiscoService.getTrafficSteeringRuleForSiteListOverAll(definitionId,
									endpoint,request);
							List<DataPolicy> appQosPolicy = izoSdwanCiscoService.getQosPoliciesForSiteListeOverAll(definitionId,
									endpoint,request);
							if (Objects.nonNull(trafficSteering))
								trafficSteeringList.addAll(trafficSteering);
							if (Objects.nonNull(appQosPolicy))
								qosPolicyList.addAll(appQosPolicy);
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
						}
					});
					LOGGER.info("In UpdateSiteDetails method - trafficSteeringList : {}", trafficSteeringList);
					LOGGER.info("In UpdateSiteDetails method - appQosPolicyList : {}",qosPolicyList);
					if (Objects.nonNull(trafficSteeringList) && !trafficSteeringList.isEmpty()) {
							for (String policy : trafficStearingPolicyName) {
								trafficSteeringList.get(0).getSequences().stream().forEach(x->{
									if((x.getSequenceId()+"_"+x.getSequenceName()).equals(policy)) {
										sequences.add(x);
									}
								});
							}
							trafficSteeringList.get(0).getSequences().clear();
							trafficSteeringList.get(0).getSequences().addAll(sequences);
					}
					if (Objects.nonNull(qosPolicyList) && !qosPolicyList.isEmpty()) {
						for (String policy : qosPolicyName) {
							qosPolicyList.get(0).getSequences().stream().forEach(y->{
								if((y.getSequenceId()+"_"+y.getSequenceName()).equals(policy)) {
									qosSequences.add(y);
								}
							});
						}
						qosPolicyList.get(0).getSequences().clear();
						qosPolicyList.get(0).getSequences().addAll(qosSequences);
				}
					String responseTraffic=Constants.SUCCESS;
					String responseQos=Constants.SUCCESS;
					try {
						if(Objects.nonNull(trafficSteeringList) && !trafficSteeringList.isEmpty()) {
							/**
							 * added boolean flag for DSCP-update
							 */
						responseTraffic = izoSdwanCiscoService.saveTrafficSteeringByOrder(definitionId, sdwanEndpointsList,
								trafficSteeringList,request,false, new StringBuilder());
						}
						if(Objects.nonNull(qosPolicyList) && !qosPolicyList.isEmpty()) {
						 responseQos = izoSdwanCiscoService.saveQosByOrder(definitionId,  sdwanEndpointsList, qosPolicyList,request);
						}
					
					if (responseTraffic.equalsIgnoreCase(Constants.SUCCESS)
							 && responseQos.equalsIgnoreCase(Constants.SUCCESS) ) {
						finalResponse.add(Constants.SUCCESS);
					}
					else
						finalResponse.add("FAILURE");
					} catch (TclCommonException  | InterruptedException e ) {
						LOGGER.error("Exception e {}",e);
						throw new TclCommonRuntimeException(e, ResponseResource.R_CODE_ERROR);
					} 
				}
				
			
		});
		return finalResponse.get(0);
	
	}

	public CiscoPolicyDetailBean getPolicyDetailedView(CiscoPolicyDetailsRequestBean request,HttpServletRequest httprequest, 
			Integer customerId, Integer partnerId, Integer customerLeId) throws TclCommonException{
		CiscoPolicyDetailBean ciscoPolicyDetailBean= new CiscoPolicyDetailBean();
		
		LOGGER.info("Entering method to fetch getDirectoryRegionFromRequest");
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		Integer customerLeIds = null ;

		izoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeIds);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + request.getPartnerId());

		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository.findBysiteListIdNames(
				Collections.singletonList(request.getSiteListId()), customerId, customerIds, request.getPartnerId(), partnerLeIds);
		List<Integer> overlaySysIds = underlaysOverlaysJoined.stream().map(site -> (Integer) site.get("overlaySysId"))
				.collect(Collectors.toList());

		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				overlaySysIds,Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL));

		Set<CiscoSiteListInstanceMapping> ciscoSiteListInstanceMappings = mapOverlayAndUnderlayAttributes(serviceAttributes,
				underlaysOverlaysJoined);
		Set<String> instanceRegions = ciscoSiteListInstanceMappings.stream().map(CiscoSiteListInstanceMapping::getInstanceRegion)
				.collect(Collectors.toSet());
		List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		
		ciscoPolicyDetailBean.setDefinitionId(request.getDefinitionId());
		ciscoPolicyDetailBean.setSiteListId(request.getSiteListId());
		ciscoPolicyDetailBean.setSiteListName(request.getSiteListName());
		ciscoPolicyDetailBean.setVpnListName(request.getVpnListName());
		ciscoPolicyDetailBean.setVpnListId(request.getVpnListId());
		ciscoPolicyDetailBean.setPolicyType(request.getPolicyType());
		ciscoPolicyDetailBean.setPolicyName(request.getPolicyName());
		ciscoPolicyDetailBean.setAllApplications(new ArrayList<>());
		ciscoPolicyDetailBean.setPolicyAssoscitedApplicationsIds(new ArrayList<>());
		ciscoPolicyDetailBean.setPolicyAssosciatedAppNames(new ArrayList<>());
		ciscoPolicyDetailBean.setSourceAddress(new ArrayList<>());
		ciscoPolicyDetailBean.setDestinationAddress(new ArrayList<>());
		ciscoPolicyDetailBean.setSlaListId(new ArrayList<>());
		ciscoPolicyDetailBean.setSourceIp(new ArrayList<>());
		ciscoPolicyDetailBean.setDestinationIp(new ArrayList<>());
		ciscoPolicyDetailBean.setAllAddresses(new HashSet<>());
		/** fetch applistroute data */
		if(request.getPolicyType().equalsIgnoreCase("Traffic Steering")|| request.getPolicyType().equalsIgnoreCase("appRoute")) {
			mappingappRoutePolicyDetails(request,ciscoPolicyDetailBean,sdwanEndpoints,httprequest);
		}
		/** fetch qos data */
		if(request.getPolicyType().equalsIgnoreCase("QOS")|| request.getPolicyType().equalsIgnoreCase("data")) {
			mappingqosPolicyDetails(request,ciscoPolicyDetailBean,sdwanEndpoints,httprequest);
			}
		
		izoSdwanCiscoService.getAssosciatedApplications(ciscoPolicyDetailBean, sdwanEndpoints.get(0));
		ciscoPolicyDetailBean.getPolicyAssoscitedApplicationsIds().stream().forEach(app->{
		try {
			izoSdwanCiscoService.getPolicyAssosciatedApplications(app, ciscoPolicyDetailBean, sdwanEndpoints.get(0));
		} catch (TclCommonException e) {
			LOGGER.error("Exception e:: {}", e);
		}
		});
		izoSdwanCiscoService.getPolicyAssosciatedAddresses(ciscoPolicyDetailBean, sdwanEndpoints.get(0));
		ciscoPolicyDetailBean.getSlaListId().stream().forEach(app->{
			try {
				izoSdwanCiscoService.getSlaAssosciatedApplications(app, ciscoPolicyDetailBean, sdwanEndpoints.get(0));
			} catch (TclCommonException e) {
				LOGGER.error("Exception e:: {}", e);
			}
			});
		
		
		return ciscoPolicyDetailBean;
	}

	

	private void mappingqosPolicyDetails(CiscoPolicyDetailsRequestBean request,
			CiscoPolicyDetailBean ciscoPolicyDetailBean, List<SdwanEndpoints> sdwanEndpoints,
			HttpServletRequest httprequest) {
		List<DataPolicy> dataPolicyList= izoSdwanCiscoService.getQosPoliciesForSiteListeOverAll
				(request.getDefinitionId(), sdwanEndpoints.get(0), httprequest);
		dataPolicyList.stream().forEach(appList->appList.getSequences().stream().forEach(seq->{
			if((seq.getSequenceId()+"_"+seq.getSequenceName()).equalsIgnoreCase(request.getPolicyName())) {
				seq.getMatch().getEntries().stream().forEach(entry->{
					if(entry.getField().equalsIgnoreCase("dscp")){
						ciscoPolicyDetailBean.setDscp(entry.getValue());
					}
					if(entry.getField().equalsIgnoreCase("appList")){
						ciscoPolicyDetailBean.getPolicyAssoscitedApplicationsIds().add(entry.getRef());
					}
					if(entry.getField().equalsIgnoreCase("sourceDataPrefixList")){
						ciscoPolicyDetailBean.getSourceAddress().add(entry.getRef());
					}
					if(entry.getField().equalsIgnoreCase("destinationDataPrefixList")){
						ciscoPolicyDetailBean.getDestinationAddress().add(entry.getRef());
					}
				});
				seq.getActions().stream().forEach(act->{
						JSONParser jsonParser = new JSONParser();
						JSONArray responseObj = new JSONArray();
						ObjectMapper mapper = new ObjectMapper();
						try {
							if(jsonParser.parse(Utils.convertObjectToJson(act.getParameter())) instanceof JSONArray) {
							responseObj = (JSONArray) jsonParser.parse(Utils.convertObjectToJson(act.getParameter()));
							List<SequenceEntries> pp3 = mapper.readValue(responseObj.toJSONString(), new TypeReference<List<SequenceEntries>>() {});
							pp3.stream().forEach(s->{
								if(s.getField().equalsIgnoreCase("forwardingClass")){
									ciscoPolicyDetailBean.setQueueName(s.getValue());
								}
								
							});
							}
						} catch (Exception e) {
							LOGGER.error("e:: {}", e);
						}
						
					if(act.getType().equalsIgnoreCase("backupSlaPreferredColor")) {
						ciscoPolicyDetailBean.setPreferredBackUpColor(Arrays.asList(act.getParameter().toString().split("\\s+")));
					}
				});
			}
		}));
		
	}

	private void mappingappRoutePolicyDetails(CiscoPolicyDetailsRequestBean request,
			CiscoPolicyDetailBean ciscoPolicyDetailBean, List<SdwanEndpoints> sdwanEndpoints, HttpServletRequest httprequest) throws TclCommonException {
		List<AppRoutePolicy> appRoutePolicyList= izoSdwanCiscoService.
				getTrafficSteeringRuleForSiteListOverAll(request.getDefinitionId(), sdwanEndpoints.get(0), httprequest);
		appRoutePolicyList.stream().forEach(appList->appList.getSequences().stream().forEach(seq->{
			if((seq.getSequenceId()+"_"+seq.getSequenceName()).equalsIgnoreCase(request.getPolicyName())) {
				seq.getMatch().getEntries().stream().forEach(entry->{
					if(entry.getField().equalsIgnoreCase("dscp")){
						ciscoPolicyDetailBean.setDscp(entry.getValue());
					}
					if(entry.getField().equalsIgnoreCase("appList")){
						ciscoPolicyDetailBean.getPolicyAssoscitedApplicationsIds().add(entry.getRef());
					}
					if(entry.getField().equalsIgnoreCase("sourceDataPrefixList")){
						ciscoPolicyDetailBean.getSourceAddress().add(entry.getRef());
					}
					if(entry.getField().equalsIgnoreCase("destinationDataPrefixList")){
						ciscoPolicyDetailBean.getDestinationAddress().add(entry.getRef());
					}
				});
				seq.getActions().stream().forEach(act->{
					if(act.getType().equalsIgnoreCase("slaclass")) {
						JSONParser jsonParser = new JSONParser();
						JSONArray responseObj;
						ObjectMapper mapper = new ObjectMapper();
						try {
							responseObj = (JSONArray) jsonParser.parse(Utils.convertObjectToJson(act.getParameter()));
							List<SequenceEntries> pp3 = mapper.readValue(responseObj.toJSONString(), new TypeReference<List<SequenceEntries>>() {});
							pp3.stream().forEach(s->{
								if(s.getField().equalsIgnoreCase("name")){
									ciscoPolicyDetailBean.getSlaListId().add(s.getRef());
								}
								if(s.getField().equalsIgnoreCase("preferredColor")){
									ciscoPolicyDetailBean.setPreferredColor(Arrays.asList(s.getValue().split("\\s+")));
								}
							});
						} catch (Exception e) {
							LOGGER.error("e:: {}", e);
						}
						
					}
					if(act.getType().equalsIgnoreCase("backupSlaPreferredColor")) {
						ciscoPolicyDetailBean.setPreferredBackUpColor(Arrays.asList(act.getParameter().toString().split("\\s+")));
					}
				});
			}
		}));
		
		
		
	}

	public List<AssosciatedSiteDetails> getCiscoSiteListConfigStatus(Integer productId, Integer customerId,
			Integer partnerId, Integer customerLeId, String siteListId) throws TclCommonException {
		LOGGER.info("Entering method to fetch template detailed info");
		List<AssosciatedSiteDetails> assosciatedSiteDetails= new ArrayList<>();
		CiscoSiteListDetailBean ciscoSiteListDetail = new CiscoSiteListDetailBean();

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();

		izoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);

		List<SIServiceAdditionalInfo> siteListsinfo = serviceAdditionalInfoRepository
				.findByAttributeNameAndAttributeValue(ServiceInventoryConstants.SITE_LIST, siteListId);
		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository.findBysiteListIdNames(
				Collections.singletonList(siteListId), customerId, customerIds, partnerId, partnerLeIds);
		List<Integer> overlaySysIds = underlaysOverlaysJoined.stream().map(site -> (Integer) site.get("overlaySysId"))
				.collect(Collectors.toList());

		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				overlaySysIds,Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL));

		Set<CiscoSiteListInstanceMapping> ciscoSiteListInstanceMappings = mapOverlayAndUnderlayAttributes(serviceAttributes,
				underlaysOverlaysJoined);
		Set<String> instanceRegions = ciscoSiteListInstanceMappings.stream().map(CiscoSiteListInstanceMapping::getInstanceRegion)
				.collect(Collectors.toSet());
		List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndpoints.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		Set<String> serviceIds = underlaysOverlaysJoined.stream()
				.map(underlay -> (String) underlay.get("overlaySrvcId")).collect(Collectors.toSet());
		List<Map<String, Object>> underlaySites = vwSiServiceInfoAllRepository.findUnderlaysByCustomer(customerId,
				partnerId, customerIds, partnerLeIds);
		LOGGER.info("Number of underlaySites : {}", underlaySites.size());
		LOGGER.info("Number of underlaySites : {}", underlaysOverlaysJoined.size());
		List<ViewSiServiceInfoAll> siteInfos = vwSiServiceInfoAllRepository.findByServiceIdInAndServiceStatusNotIn(new ArrayList<>(serviceIds),Arrays.asList(ServiceInventoryConstants.TERMINATED,ServiceInventoryConstants.UNDER__PROVISIONING));
		List<String> underLayServiceIds = underlaysOverlaysJoined.stream()
				.map(underlaySite -> (String) underlaySite.get("underlaySrvcId")).collect(Collectors.toList());
		List<SIServiceAssetInfo> cpeDetails = serviceAssetInfoRepository.findByServiceIdInAndAssetTag(underLayServiceIds, ServiceInventoryConstants.SDWAN_CPE);
		List<SiteListConfigDetails> siteListConfigDetailsList = new ArrayList<>();
		ciscoSiteListDetail.setAssosciatedSites(new ArrayList<>(serviceIds));
		ciscoSiteListDetail.setAssociatedSitesCount(siteInfos.size());
		ciscoSiteListDetail.setAttributeIds(new ArrayList<>());
		siteListsinfo.forEach(siteList -> ciscoSiteListDetail.getAttributeIds().add(siteList.getAttributeSysId()));
		siteListsinfo.stream().findAny()
				.ifPresent(siteList -> ciscoSiteListDetail.setSiteListAlias(siteList.getDisplayValue()));
		
		// fetch cpe information (status,technical info) according to instance region
		Map<String, String> map = new HashMap<>();
		CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
		CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
		if (Objects.nonNull(ciscoBulkcpeStatusResponse)
				&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
			if (Objects.nonNull(underlaysOverlaysJoined) && !underlaysOverlaysJoined.isEmpty()) {
				Set<String> recurringTemplates = new HashSet<>();
				underlaysOverlaysJoined.forEach(template -> {
					Set<CiscoCPEBean> sdwanCpe = new HashSet<>();
					Set<AssosciatedSiteDetails> sitedetailsSet= new HashSet<>();
					cpeDetails.stream()
							.filter(cpe -> cpe.getServiceId().equalsIgnoreCase((String) template.get("underlaySrvcId")) && 
									StringUtils.isNotBlank(cpe.getAssetName()))
							.peek(cpeDetail -> {
								CiscoCPEBean sdwanCpeDetail = new CiscoCPEBean();
								sdwanCpeDetail.setCpeName(cpeDetail.getAssetName());
								sdwanCpeDetail.setUnderlayServiceId(cpeDetail.getServiceId());
								sdwanCpeDetail.setSdwanServiceId(vwSiServiceInfoAllRepository
										.findById(cpeDetail.getServiceSystemId()).get().getIzoSdwanSrvcId());
								sdwanCpe.add(sdwanCpeDetail);
							}).anyMatch(cpe -> cpe.getServiceId()
									.equalsIgnoreCase((String) template.get("underlaySrvcId")));
					if (Objects.nonNull(underlaySites) && !underlaySites.isEmpty()) {
						sdwanCpe.forEach(sdwanCPE -> {
							mappingUplinkandDownlinkCount(sdwanCPE,underlaySites,ciscoBulkcpeStatusResponse,map,instanceByCode);
							});
					}
				
					if (!recurringTemplates.contains(template.get("attributeValue"))) {
						SiteListConfigDetails siteListDetail = new SiteListConfigDetails();
						mappingSiteListConfigDetails(siteListDetail,sdwanCpe,ciscoBulkSiteListResponse,
								recurringTemplates,template,sitedetailsSet,siteListConfigDetailsList);
					} else {
						siteListConfigDetailsList.stream().filter(templateDetail -> ((String) template.get("attributeValue"))
								.equalsIgnoreCase(templateDetail.getSiteListId())).peek(templateDetail -> {
									templateDetail.getSiteDetails().addAll(sitedetailsSet);
									templateDetail.getAttributeId().add((Integer) template.get("templateId"));
									if (Objects.nonNull(template.get("overlaySrvcId")))
										templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
									templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
								}).anyMatch(templateDetail -> ((String) template.get("attributeValue"))
										.equalsIgnoreCase(templateDetail.getSiteListId()));
					}
				});
			}
		
		}
		else
			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		ciscoSiteListDetail.setAssosciatedSiteDetails(new ArrayList<>());
		
		List<String> siteListIdList= new ArrayList<>();
		siteListConfigDetailsList.stream().forEach(sitelist->{
			siteListIdList.add(sitelist.getSiteListId());
			ciscoSiteListDetail.getAssosciatedSiteDetails().addAll(sitelist.getSiteDetails());
			ciscoSiteListDetail.setSiteListId(sitelist.getSiteListId());
			ciscoSiteListDetail.setSiteListName(sitelist.getSiteListName());
			});
		assosciatedSiteDetails.addAll(ciscoSiteListDetail.getAssosciatedSiteDetails());
		return assosciatedSiteDetails;
	}

	public CiscoPolicyStatusResponse updatePolicyDetails(CiscoPolicyDetailBean request, HttpServletRequest httpServletRequest, Integer customerId, Integer customerLeId, Integer partnerId) throws TclCommonException 
			{
		LOGGER.info("Entering method to updatePolicyDetails request::{}", request);
		CiscoPolicyStatusResponse statusResp= new CiscoPolicyStatusResponse();
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		Integer customerLeIds = null ;

		izoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeIds);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);

		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository.findBysiteListIdNames(
				Collections.singletonList(request.getSiteListId()), customerId, customerIds, partnerId, partnerLeIds);
		List<Integer> overlaySysIds = underlaysOverlaysJoined.stream().map(site -> (Integer) site.get("overlaySysId"))
				.collect(Collectors.toList());

		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				overlaySysIds,Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL));

		Set<CiscoSiteListInstanceMapping> ciscoSiteListInstanceMappings = mapOverlayAndUnderlayAttributes(serviceAttributes,
				underlaysOverlaysJoined);
		Set<String> instanceRegions = ciscoSiteListInstanceMappings.stream().map(CiscoSiteListInstanceMapping::getInstanceRegion)
				.collect(Collectors.toSet());
		List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		
		/**
		 * traffic Steering policies for Applications,dscp,addresses 
		 */
		if(request.getPolicyType().equalsIgnoreCase("Traffic Steering")) {
			statusResp.setPolicyType(request.getPolicyType());
			statusResp.setPolicyName(request.getPolicyName());
			
			/**updating the applications,Dscp,Addresses*/
			//scenario-1
			if (Objects.isNull(request.getPolicyAssoscitedApplicationsIds())
					|| request.getPolicyAssoscitedApplicationsIds().isEmpty()
//					&& Objects.isNull(request.getDscp())
//					&& request.getDscp().isEmpty() && Objects.isNull(request.getSourceAddress())
//					&& request.getSourceAddress().isEmpty() && Objects.isNull(request.getDestinationAddress())
//					&& request.getDestinationAddress().isEmpty()
					) {
				LOGGER.error("policy updated details can't be null or empty:{}", request);
					throw new TclCommonException(ExceptionConstants.CISCO_APPLICATION_EDIT_API_ERROR);
			}
			/** if convention list is matching -Applications */
			else {
				try {
					StringBuilder statusBuilder = new StringBuilder();
					/**
					 * if convention list is matching -Applications
					 */
					//if(request.getPolicyAssoscitedApplicationsIds()!=null && !request.getPolicyAssoscitedApplicationsIds().isEmpty()) {
					String listId = request.getPolicyAssoscitedApplicationsIds().get(0);
					statusResp.setApplicationListId(request.getPolicyAssoscitedApplicationsIds());
					CiscoPolicyApplicationBean ciscoPoliciesAppBean= new CiscoPolicyApplicationBean();
					CiscoPolicyDetailBean ciscoPolicy=	new CiscoPolicyDetailBean();
					ciscoPolicy.setPolicyAssosciatedAppNames(new ArrayList<>());
					ciscoPoliciesAppBean=izoSdwanCiscoService.getPolicyAssosciatedApplications(listId,ciscoPolicy,sdwanEndpoints.get(0));
					CiscoPolicyApplicationPayload appPayload= new CiscoPolicyApplicationPayload();
					appPayload.setEntries(new ArrayList<>());
					constructingRequestPayloadForPolicyApp(appPayload,ciscoPoliciesAppBean,request);
					izoSdwanCiscoService.updatePolicyAssosciatedApplications(listId,appPayload,sdwanEndpoints.get(0),statusBuilder);
					//}
//					else
//					{
//						statusBuilder.append("Updating application failed").append(",");
//					}
					/**
					 * if convention list is matching -Addresses
					 */
//					CiscoPolicyDetailBean ciscoPolicy = new CiscoPolicyDetailBean();
//					// if(Objects.nonNull(request.getSourceAddress()) &&
//					// !request.getSourceAddress().isEmpty()) {
//					ciscoPolicy.setSourceAddress(request.getSourceAddress());
//					// }
//					// if(Objects.nonNull(request.getDestinationAddress()) &&
//					// !request.getDestinationAddress().isEmpty()) {
//					ciscoPolicy.setDestinationAddress(request.getDestinationAddress());
//					// }
//					// if(Objects.nonNull(request.getSourceIp()) &&
//					// !request.getSourceIp().isEmpty()) {
//					ciscoPolicy.setSourceIp(request.getSourceIp());
//					// }
//					// if(Objects.nonNull(request.getDestinationIp()) &&
//					// !request.getDestinationIp().isEmpty()) {
//					ciscoPolicy.setDestinationIp(request.getDestinationIp());
//					// }
//					List<List<Map<String, Object>>> addressList = izoSdwanCiscoService
//							.getPolicyAssociatedAddressesWithId(ciscoPolicy, sdwanEndpoints.get(0));
//					izoSdwanCiscoService.updatePolicyAddress(addressList, sdwanEndpoints.get(0), statusBuilder);
//
//					/**
//					 * if convention list is matching -DSCP
//					 */
//					if (!request.getDefinitionId().isEmpty() && request.getDefinitionId() != null
//							&& !request.getDscp().isEmpty() && request.getDscp() != null) {
//						String definationId = request.getDefinitionId();
//
//						statusResp.setDefinitionId(request.getDefinitionId());
//						AppRoutePolicy appRoutePolicyDscp = new AppRoutePolicy();
//						CiscoPolicyDetailBean ciscoPolicyDscp = new CiscoPolicyDetailBean();
//						ciscoPolicyDscp.setDefinitionId(new String());
//						/* Get the DSCP policy details */
//						appRoutePolicyDscp = izoSdwanCiscoService.getPolicyAssosciatedDscp(definationId,
//								ciscoPolicyDscp, sdwanEndpoints.get(0), httpServletRequest);
//						LOGGER.info("appRoutePolicyDscp:{} ", Utils.convertObjectToJson(appRoutePolicyDscp));
//						AppRoutePolicy dscpPayload = new AppRoutePolicy();
//						dscpPayload.setSequences(new ArrayList<>());
//						/* constructing the request pay load for DSCP */
//						constructingRequestPayloadForDscp(dscpPayload, appRoutePolicyDscp, request);
//						LOGGER.info("dscpPayload:{} ", Utils.convertObjectToJson(dscpPayload));
//						/* updating the DSCP policy details */
//						List<AppRoutePolicy> trafficSteeringList = new ArrayList<>();
//						trafficSteeringList.add(dscpPayload);
//						boolean dscpflag = true;
//						izoSdwanCiscoService.saveTrafficSteeringByOrder(definationId, sdwanEndpoints,
//								trafficSteeringList, httpServletRequest, dscpflag, statusBuilder);
//					}
//					else
//					{
//						statusBuilder.append("Updating dscp failed");
//					}

					statusResp.setUpdateStatus(statusBuilder.toString());
				} catch (TclCommonException e) {
					throw new TclCommonException(e, ResponseResource.R_CODE_ERROR);
				}
			}
		}
		
		/**
		 * QOS policies for Applications,Dscp,Addresses  
		 */
		else if(request.getPolicyType().equalsIgnoreCase("qos") || request.getPolicyType().equalsIgnoreCase("data")) {
			statusResp.setPolicyType(request.getPolicyType());
			statusResp.setPolicyName(request.getPolicyName());
			/** updating the applications,dscp,addresses */
			if (Objects.isNull(request.getPolicyAssoscitedApplicationsIds())
					|| request.getPolicyAssoscitedApplicationsIds().isEmpty() 
//					&& Objects.isNull(request.getDscp())
//					&& request.getDscp().isEmpty() && Objects.isNull(request.getSourceAddress())
//					&& request.getSourceAddress().isEmpty() && Objects.isNull(request.getDestinationAddress())
//					&& request.getDestinationAddress().isEmpty()
					) {
				throw new TclCommonException(ExceptionConstants.CISCO_APPLICATION_EDIT_API_ERROR);
			}
			
			else {
				try {
					StringBuilder statusBuilder = new StringBuilder();
					/**
					 *  if convention list is matching QOS policy-update the Applications 
					 */
//					if (request.getPolicyAssoscitedApplicationsIds() != null
//							&& !request.getPolicyAssoscitedApplicationsIds().isEmpty()) {
						String listId = request.getPolicyAssoscitedApplicationsIds().get(0);
						LOGGER.info("listId :{} ", listId);
						statusResp.setApplicationListId(request.getPolicyAssoscitedApplicationsIds());
						CiscoPolicyApplicationBean ciscoPoliciesAppBean = new CiscoPolicyApplicationBean();
						CiscoPolicyDetailBean ciscoPolicy = new CiscoPolicyDetailBean();
						ciscoPolicy = new CiscoPolicyDetailBean();
						ciscoPolicy.setPolicyAssosciatedAppNames(new ArrayList<>());
						ciscoPoliciesAppBean = izoSdwanCiscoService.getPolicyAssosciatedApplications(listId,
								ciscoPolicy, sdwanEndpoints.get(0));
						LOGGER.info("ciscoPoliciesAppBean for applications:{} ",
								Utils.convertObjectToJson(ciscoPoliciesAppBean));
						CiscoPolicyApplicationPayload appPayload = new CiscoPolicyApplicationPayload();
						appPayload.setEntries(new ArrayList<>());
						constructingRequestPayloadForPolicyApp(appPayload, ciscoPoliciesAppBean, request);
						LOGGER.info("appPayload:{} ", Utils.convertObjectToJson(appPayload));
						izoSdwanCiscoService.updatePolicyAssosciatedApplications(listId, appPayload,
								sdwanEndpoints.get(0), statusBuilder);
//					} else {
//						statusBuilder.append("Updating application failed");
//					}
					/**
					 * if convention list is matching -Addresses
					 */
//					CiscoPolicyDetailBean ciscoPolicy = new CiscoPolicyDetailBean();
//					// if(Objects.nonNull(request.getSourceAddress()) &&
//					// !request.getSourceAddress().isEmpty()) {
//					ciscoPolicy.setSourceAddress(request.getSourceAddress());
//					// }
//					// if(Objects.nonNull(request.getDestinationAddress()) &&
//					// !request.getDestinationAddress().isEmpty()) {
//					ciscoPolicy.setDestinationAddress(request.getDestinationAddress());
//					// }
//					// if(Objects.nonNull(request.getSourceIp()) &&
//					// !request.getSourceIp().isEmpty()) {
//					ciscoPolicy.setSourceIp(request.getSourceIp());
//					// }
//					// if(Objects.nonNull(request.getDestinationIp()) &&
//					// !request.getDestinationIp().isEmpty()) {
//					ciscoPolicy.setDestinationIp(request.getDestinationIp());
//					// }
//					List<List<Map<String, Object>>> addressList = izoSdwanCiscoService
//							.getPolicyAssociatedAddressesWithId(ciscoPolicy, sdwanEndpoints.get(0));
//					izoSdwanCiscoService.updatePolicyAddress(addressList, sdwanEndpoints.get(0), statusBuilder);
//					
//					/**
//					 * if convention list is matching QOS policy-DSCP
//					 */
//					if (!request.getDefinitionId().isEmpty() && request.getDefinitionId() != null
//							&& !request.getDscp().isEmpty() && request.getDscp() != null) {
//					  String definationId = request.getDefinitionId();
//					  statusResp.setDefinitionId(request.getDefinitionId()); 
//					  DataPolicyQos dataPolicyResBean = new DataPolicyQos(); 
//					  /*Get the DSCP policy details */
//					  for (SdwanEndpoints endPoints : sdwanEndpoints) { 
//						  dataPolicyResBean = izoSdwanCiscoService.getPolicyAssociatedDscpWithDefId(definationId,
//								  endPoints,httpServletRequest); 
//						  }
//					  LOGGER.info("dataPolicyGetResBean : {}",Utils.convertObjectToJson(dataPolicyResBean)); 
//					  DataPolicyQos dscpQosPayload= new DataPolicyQos();
//					  dscpQosPayload.setSequences(new ArrayList<>());
//					  dscpQosPayload.setDefaultAction(null); 
//					  dscpQosPayload.setReferences(new ArrayList<>()); 
//					  /*constructing the request pay load for-DSCP */
//					  constructingRequestPayloadQosForDscp(dscpQosPayload,dataPolicyResBean,request);
//					  LOGGER.info("dscpQosPayload: {}",Utils.convertObjectToJson(dscpQosPayload));
//					  /*updating the QOS policy-DSCP details*/ 
//					  List<DataPolicyQos> qosDscpPayloadList = new ArrayList<>(); 
//					  qosDscpPayloadList.add(dscpQosPayload); 
//					  /*updating the DSCP*/
//					  izoSdwanCiscoService.updateQosDscpWithDefid(definationId,sdwanEndpoints,qosDscpPayloadList,httpServletRequest,statusBuilder);
//					} else {
//						statusBuilder.append("Updating dscp failed");
//					}

					statusResp.setUpdateStatus(statusBuilder.toString());
					LOGGER.info("statusResp is: {} ", statusResp);
				} catch (TclCommonException e) {
					throw new TclCommonException(e, ResponseResource.R_CODE_ERROR);
				}
			}
		}
		return statusResp;
	}

	private void constructingRequestPayloadForPolicyApp(CiscoPolicyApplicationPayload appPayload,
			CiscoPolicyApplicationBean ciscoPoliciesAppBean, CiscoPolicyDetailBean request) {
		appPayload.setName(ciscoPoliciesAppBean.getName());
		appPayload.setType(ciscoPoliciesAppBean.getType());
		appPayload.setDescription(ciscoPoliciesAppBean.getDescription());
		Set<ApplicationEntries> appEntrList=new HashSet<>();
		request.getPolicyAssosciatedAppNames().stream().forEach(appName->{
			ApplicationEntries entries= new ApplicationEntries();
			entries.setApp(appName);
			appEntrList.add(entries);
		});
		appPayload.getEntries().addAll(appEntrList);
		
	}

	/**
	 * 
	 * @param appPayload
	 * @param ciscoPoliciesDscpBean
	 * @param request
	 * @author SGanta
	 */
	@SuppressWarnings("deprecation")
	private void constructingRequestPayloadForDscp(AppRoutePolicy dscpPayload,AppRoutePolicy ciscoPoliciesDscpBean, 
			CiscoPolicyDetailBean request) {

		if (Objects.nonNull(ciscoPoliciesDscpBean.getName()) && !ciscoPoliciesDscpBean.getName().isEmpty()
				&& Objects.nonNull(ciscoPoliciesDscpBean.getType()) && !ciscoPoliciesDscpBean.getType().isEmpty()
				&& Objects.nonNull(ciscoPoliciesDscpBean.getDescription())
				&& !ciscoPoliciesDscpBean.getDescription().isEmpty()) {
		dscpPayload.setName(ciscoPoliciesDscpBean.getName());
		dscpPayload.setType(ciscoPoliciesDscpBean.getType());
		dscpPayload.setDescription(ciscoPoliciesDscpBean.getDescription());
		List<Sequences> seqList = new ArrayList<>();
		seqList = ciscoPoliciesDscpBean.getSequences();
		List<SequenceEntries> seqEntriesList = new ArrayList<>();
		
		if(Objects.nonNull(seqList) && !seqList.isEmpty()) {
		ciscoPoliciesDscpBean.getSequences().stream().forEach(e->{
					if ((e.getSequenceId() + "_" + e.getSequenceName()).equalsIgnoreCase(request.getPolicyName())) {
						e.getMatch().getEntries().forEach(h -> {
							if (h.getField().equalsIgnoreCase("dscp")) {
								h.setValue(request.getDscp());
								seqEntriesList.add(h);
							}
						});
					}
				});
			} else {
				throw new TCLException("ciscoPoliciesDscpBean-sequence list empty", seqList.toString());
			}
			dscpPayload.setSequences(seqList);
		}
	}

	/**
	 * @author SGanta
	 * @param dscpPayload
	 * @param ciscoPoliciesDscpBean
	 * @param request
	 * @throws TclCommonException 
	 */
	private void constructingRequestPayloadQosForDscp(DataPolicyQos dscpPayload,DataPolicyQos ciscoPoliciesDscpBean, 
			CiscoPolicyDetailBean request) throws TclCommonException {
		LOGGER.info("Inside constructingRequestPayloadQosForDscp");
		try {
			if (Objects.nonNull(ciscoPoliciesDscpBean.getName()) && !ciscoPoliciesDscpBean.getName().isEmpty()
					&& Objects.nonNull(ciscoPoliciesDscpBean.getType()) && !ciscoPoliciesDscpBean.getType().isEmpty()
					&& Objects.nonNull(ciscoPoliciesDscpBean.getDescription()) && !ciscoPoliciesDscpBean.getDescription().isEmpty()
					&& Objects.nonNull(ciscoPoliciesDscpBean.getDefaultAction())) {
		dscpPayload.setName(ciscoPoliciesDscpBean.getName());
		dscpPayload.setType(ciscoPoliciesDscpBean.getType());
		dscpPayload.setDescription(ciscoPoliciesDscpBean.getDescription());
		dscpPayload.setDefaultAction(ciscoPoliciesDscpBean.getDefaultAction());
		dscpPayload.setReferences(ciscoPoliciesDscpBean.getReferences());
		List<DataPolicySequences> seqList = new ArrayList<>();
		seqList = ciscoPoliciesDscpBean.getSequences();
		List<SequenceEntries> seqEntriesList = new ArrayList<>();
		if(Objects.nonNull(seqList) && !seqList.isEmpty()) {
		ciscoPoliciesDscpBean.getSequences().stream().forEach(e->{
			if(e.getSequenceId()+"_"+e.getSequenceName().equals(request.getPolicyName()) != null) {
			e.getMatch().getEntries().forEach(h->{
				if(h.getField().equalsIgnoreCase("dscp")) {
					h.setValue(request.getDscp());
					seqEntriesList.add(h);
					}
				});
			}
		});
			} else {
				LOGGER.info("ciscoPoliciesDscpBean sequence list:{}", seqList.toString());
				throw new TclCommonException("ciscoPoliciesDscpBean-sequence list empty");
			}
			dscpPayload.setSequences(seqList);
			}
			else {
				LOGGER.error("ciscoPoliciesDscpBean values is empty or null",ciscoPoliciesDscpBean.toString());
			}
		} catch (Exception e) {
			LOGGER.error("Error in constructingRequestPayloadQosForDscp");
			throw new TclCommonException(e, ResponseResource.R_CODE_ERROR);
		}
	}

}
