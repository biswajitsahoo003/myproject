package com.tcl.dias.serviceinventory.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.tcl.dias.common.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.serviceinventory.beans.CpeUnderlaySitesBean;
import com.tcl.dias.serviceinventory.beans.NetworkSiteDetails;
import com.tcl.dias.serviceinventory.beans.PagedResultWithTimestamp;
import com.tcl.dias.serviceinventory.beans.SdwanCpeDetails;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetails;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetailsBean;
import com.tcl.dias.serviceinventory.beans.SdwanTemplateDetails;
import com.tcl.dias.serviceinventory.beans.SiteAndCpeStatusCount;
import com.tcl.dias.serviceinventory.beans.SiteDetailsSearchRequest;
import com.tcl.dias.serviceinventory.beans.TemplateDetails;
import com.tcl.dias.serviceinventory.beans.ViewSiServiceInfoAllBean;
import com.tcl.dias.serviceinventory.beans.VwServiceAssetAttributeBean;
import com.tcl.dias.serviceinventory.beans.VwServiceAttributesBean;
import com.tcl.dias.serviceinventory.constants.ExceptionConstants;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetAdditionalInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.SdwanEndpoints;
import com.tcl.dias.serviceinventory.entity.entities.ViewSiServiceInfoAll;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAssetAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAssetInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.SdwanEndpointsRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwSiServiceInfoAllRepository;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoApplicationByDeviceIdResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoApplications;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoApplicationsDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoBulkCpeResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoCPEBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoCPEInformationBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.CiscoSiteListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.AssosciatedSiteDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoBulkSiteListResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.SiteListConfigDetails;
import com.tcl.dias.serviceinventory.util.CiscoServiceInventoryUtils;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import com.tcl.dias.serviceinventory.util.ServiceInventoryUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service class to hold all IZOSDWAN related functionalities for Cisco
 * 
 * 
 *
 */
@Service
public class IzoSdwanCiscoInventoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanCiscoInventoryService.class);

	@Autowired
	IzoSdwanInventoryService isoSdwanInventoryService;
	
	@Autowired
	SIServiceAdditionalInfoRepository serviceAdditionalInfoRepository;
	
	@Lazy
	@Autowired
	IzoSdwanAsyncTasks izoSdwanAsyncTasks;

	@Autowired
	VwSiServiceInfoAllRepository vwSiServiceInfoAllRepository;
	
	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;
	
	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	SiServiceAttributeRepository siServiceAttributeRepository;

	@Autowired
	SdwanEndpointsRepository sdwanEndpointsRepository;
	
	@Autowired
	SIServiceAssetInfoRepository serviceAssetInfoRepository;
	
	@Autowired
	SIServiceAssetAdditionalInfoRepository siServiceAssetAdditionalInfoRepository;


	@Autowired
	RestClientService restClientService;
	
	@Autowired
	IzoSdwanCiscoService izoSdwanCiscoService;
	
	@Autowired
	CiscoServiceInventoryUtils ciscoServiceInventoryUtils;
	
	@Autowired
	SIAssetRepository siAssetRepository;
	/**
	 * Get Service details based on login user for product SDWAN and For Cisco
	 *
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanSiteDetailsBean getSdwanAndNetworkSiteDetailsCisco(Integer productId, Integer page, Integer size,
			Integer customerId, Integer partnerId, Integer customerLeId) throws TclCommonException {
		List<Integer> customerLeIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		SdwanSiteDetailsBean SdwanSiteDetailsBean = new SdwanSiteDetailsBean();
		List<ViewSiServiceInfoAllBean> vwSiInfoOverlay = new ArrayList<>();
		try {

			isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);

			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			if (!customerLeIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<String> sdwanServiceIds = new ArrayList<>();
				List<Integer> sdwanSysId = new ArrayList<>();
				List<Map<String, Object>> sdWanSiteDetails = new ArrayList<>();
				if (page == -1) {
					sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetails(customerLeIds, partnerLeIds,
							productId, customerId, partnerId);
					SdwanSiteDetailsBean.setTotalItems(sdWanSiteDetails.size());
				} else {
					page = (page - 1) * size;
					sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetailsWithPageLimit(customerLeIds,
							partnerLeIds, productId, customerId, partnerId, page, size);
					Integer totalItems = vwSiServiceInfoAllRepository.findSdwanSiteCount(customerLeIds, partnerLeIds,
							productId, customerId, partnerId);
					SdwanSiteDetailsBean.setTotalItems(totalItems);
					if (totalItems != null) {
						SdwanSiteDetailsBean.setTotalPages(totalItems / size);
					}
				}

				if (sdWanSiteDetails != null && !sdWanSiteDetails.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					sdWanSiteDetails.stream().forEach(map -> {
						vwSiInfoOverlay.add(mapper.convertValue(map, ViewSiServiceInfoAllBean.class));
					});
				}
				vwSiInfoOverlay.stream().forEach(vwSiInfo -> {
					sdwanServiceIds.add(vwSiInfo.getServiceId());
					sdwanSysId.add(vwSiInfo.getSysId());
				});

				getSdwanSiteData(SdwanSiteDetailsBean, vwSiInfoOverlay, sdwanServiceIds, sdwanSysId, null);
				SdwanSiteDetailsBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return SdwanSiteDetailsBean;
	}
	/**
	 * Get the SDWAN site data for CISCO
	 * @param SdwanSiteDetailsBean
	 * @param vwSiInfoOverlay
	 * @param sdwanServiceIds
	 * @param sdwanSysId
	 * @param status
	 * @throws TclCommonException
	 * @throws IOException
	 */

	private void getSdwanSiteData(SdwanSiteDetailsBean SdwanSiteDetailsBean,
			List<ViewSiServiceInfoAllBean> vwSiInfoOverlay, List<String> sdwanServiceIds, List<Integer> sdwanSysId,
			Boolean status) throws TclCommonException, IOException {
		LOGGER.info("Inside  getSdwanSiteData to fetch underlay product for IZOSDWAN Serviceids {}", sdwanServiceIds);
		List<Map<String, Object>> underlaySiteDetails = vwSiServiceInfoAllRepository
				.findByUndeyLayByIzoSdwanServiceIdsIn(sdwanServiceIds);
		List<VwServiceAssetAttributeBean> vwSiInfoUnderlay = new ArrayList<>();
		List<Integer> underlaySysIds = new ArrayList<>();
		if (underlaySiteDetails != null && !underlaySiteDetails.isEmpty()) {
			final ObjectMapper mapper = new ObjectMapper();
			underlaySiteDetails.stream().forEach(underlaySiteDetail -> {
				vwSiInfoUnderlay.add(mapper.convertValue(underlaySiteDetail, VwServiceAssetAttributeBean.class));
			});
			vwSiInfoUnderlay.stream().forEach(underlay -> {
				underlaySysIds.add(underlay.getSysId());
			});
		}
		Map<String, List<VwServiceAssetAttributeBean>> uderlayGroupedByOverlay = vwSiInfoUnderlay.stream()
				.collect(Collectors.groupingBy(VwServiceAssetAttributeBean::getIzoSdwanSrvcId));
		LOGGER.info("Site Listing fetching service attributes for  IZOSDWAN ServiceDetailids {}", sdwanSysId);
		List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
				.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
						Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL, ServiceInventoryConstants.CISCO_SITE_ID,
								ServiceInventoryConstants.SITE_LIST, ServiceInventoryConstants.PRODUCT_FLAVOUR));

		persistSdwanSiteDetails(SdwanSiteDetailsBean, vwSiInfoOverlay, overLayServiceAttrs, uderlayGroupedByOverlay,
				status);
	}
	/**
	 * Constructing Final Sdwan site details by calling cisco Api's
	 * @param SdwanSiteDetailsBean
	 * @param vwSiInfoOverlay
	 * @param overLayServiceAttrs
	 * @param uderlayGroupedByOverlay
	 * @param status
	 * @throws TclCommonException
	 * @throws IOException
	 */

	private void persistSdwanSiteDetails(SdwanSiteDetailsBean SdwanSiteDetailsBean,
			List<ViewSiServiceInfoAllBean> vwSiInfoOverlay, List<SIServiceAttribute> overLayServiceAttrs,
			Map<String, List<VwServiceAssetAttributeBean>> uderlayGroupedByOverlay, Boolean status)
			throws TclCommonException, IOException {
		try {
			LOGGER.info("Inside persistSdwanSiteDetails to persist data ");
			List<SdwanSiteDetails> sdwanSiteDetails = new ArrayList<>();
			SdwanSiteDetailsBean.setSiteDetails(sdwanSiteDetails);
			Set<String> instanceRegions = new HashSet<>();
			overLayServiceAttrs.stream()
					.filter(attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
					.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
			LOGGER.info("Inside site listing and fetch sdwan endpoints for server code {} ", instanceRegions);
			List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
					.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
			CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
			if (Objects.nonNull(ciscoBulkcpeStatusResponse)
					&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
				vwSiInfoOverlay.stream().forEach(overlay -> {

					LOGGER.info("Entering persistSdwanSiteDetailsBean  serviceIds {} ", overlay.getServiceId());
					SdwanSiteDetails sdwanSiteDetail = new SdwanSiteDetails();
					List<SdwanCpeDetails> sdwanCpeDetails = new ArrayList<>();
					List<SdwanTemplateDetails> templateDetails = new ArrayList<>();
					sdwanSiteDetails.add(sdwanSiteDetail);
					sdwanSiteDetail.setSdwanCpeDetails(sdwanCpeDetails);
					sdwanSiteDetail.setCountry(overlay.getSourceCountry());
					sdwanSiteDetail.setErfCustomerId(overlay.getOrderCustomerId());
					sdwanSiteDetail.setServiceDetailId(overlay.getSysId());
					sdwanSiteDetail.setSdwanServiceId(overlay.getServiceId());
					sdwanSiteDetail.setSdwanSiteAlias(overlay.getSiteAlias());
					sdwanSiteDetail.setCity(overlay.getSourceCity());
					sdwanSiteDetail.setSdwanFamilyName(overlay.getProductFamilyName());
					sdwanSiteDetail.setSdwanFamilyId(overlay.getProductFamilyId());
					sdwanSiteDetail.setSdwanOfferingName(overlay.getProductOfferingName());
					sdwanSiteDetail.setSdwanPrimaryServiceId(overlay.getPrimaryServiceId());
					sdwanSiteDetail.setSdwanPrimaryOrSecondary(overlay.getPrimaryOrSecondary());
					sdwanSiteDetail.setSdwanPriSecServiceId(overlay.getPrimarySecondaryLink());
					sdwanSiteDetail.setSiteName(overlay.getServiceId());
					sdwanSiteDetail.setLatLong(overlay.getLatLong());
					sdwanSiteDetail.setSiteAddress(overlay.getCustomerSiteAddress());
					sdwanSiteDetail.setErfCustomerLeId(overlay.getOrderCustLeId());

					overLayServiceAttrs.stream()
							.filter(attr -> overlay.getSysId().equals(attr.getSiServiceDetail().getId()))
							.forEach(attr -> {
								String attrName = attr.getAttributeName();
								LOGGER.info("Processing service atrribute {} and service id {}", attrName,
										overlay.getServiceId());
								if (attrName.equalsIgnoreCase(ServiceInventoryConstants.PRODUCT_FLAVOUR)) {
									sdwanSiteDetail.setProductFlavour(attr.getAttributeValue());
								} else if (attrName.equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL)) {
									sdwanSiteDetail.setInstanceRegion(attr.getAttributeValue());
								} else if (attrName.equalsIgnoreCase(ServiceInventoryConstants.CISCO_SITE_ID)) {
									sdwanSiteDetail.setSiteId(attr.getAttributeValue());
								}

							});

					if (Objects.isNull(status) || status == false) {
						List<VwServiceAssetAttributeBean> undeyLays = uderlayGroupedByOverlay
								.get(overlay.getServiceId());
						persistUnderlayInformation(SdwanSiteDetailsBean, sdwanSiteDetail, undeyLays,
								ciscoBulkcpeStatusResponse, overlay, instanceByCode);
					}
					List<SdwanTemplateDetails> distinctTemplates = templateDetails.stream()
							.collect(Collectors.collectingAndThen(Collectors.toCollection(
									() -> new TreeSet<>(Comparator.comparing(SdwanTemplateDetails::getTemplateName))),
									ArrayList::new));
					sdwanSiteDetail.setTemplateDetails(distinctTemplates);

				});
				if (Objects.nonNull(status) && status) {
					persistUnderlayInformationThreaded(SdwanSiteDetailsBean, uderlayGroupedByOverlay,
							ciscoBulkcpeStatusResponse, instanceByCode);
					sdwanSiteDetails.forEach(sdwanSiteDetail -> {
						sdwanSiteDetail
								.setSiteStatus(sdwanSiteDetail.getUpLinkCount() == 0 ? ServiceInventoryConstants.OFFLINE
										: (sdwanSiteDetail.getDownLinkCount() == 0 ? ServiceInventoryConstants.ONLINE
												: ServiceInventoryConstants.DEGRADED));
						SdwanSiteDetailsBean.setOnlineCount(ciscoServiceInventoryUtils.incrementCountIfOnline(sdwanSiteDetail.getSiteStatus(),
								SdwanSiteDetailsBean.getOnlineCount()));
						SdwanSiteDetailsBean.setOfflineCount(ciscoServiceInventoryUtils.incrementCountIfOffline(sdwanSiteDetail.getSiteStatus(),
								SdwanSiteDetailsBean.getOfflineCount()));
						SdwanSiteDetailsBean.setDegradedCount(ciscoServiceInventoryUtils.incrementCountIfDegraded(sdwanSiteDetail.getSiteStatus(),
								SdwanSiteDetailsBean.getDegradedCount()));
					});
				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}

	}
	/**
	 * Getting the underlay Info for Cisco
	 * @param sdwanSiteDetailsBean
	 * @param sdwanSiteDetail
	 * @param undeyLays
	 * @param ciscoBulkcpeStatusResponse
	 * @param overlay
	 * @param instanceByCode
	 */
	private void persistUnderlayInformation(SdwanSiteDetailsBean sdwanSiteDetailsBean, SdwanSiteDetails sdwanSiteDetail,
			List<VwServiceAssetAttributeBean> undeyLays, CiscoBulkCpeResponse ciscoBulkcpeStatusResponse,
			ViewSiServiceInfoAllBean overlay, Map<String, List<SdwanEndpoints>> instanceByCode) {

		if (undeyLays != null) {
			Set<String> cpes = new HashSet<>();
			undeyLays.stream().forEach(underlayAsset -> cpes.add(underlayAsset.getAssetName()));
			Map<String, List<VwServiceAssetAttributeBean>> assetGroupedByName = undeyLays.stream()
					.collect(Collectors.groupingBy(VwServiceAssetAttributeBean::getAssetName));
			cpes.stream().forEach(cpe -> {
				LOGGER.info("Inside site listing processing CPE {} ", cpe);
				SdwanCpeDetails sdwanCpeDetail = new SdwanCpeDetails();
				List<NetworkSiteDetails> networkSiteDetails = new ArrayList<>();
				sdwanCpeDetail.setNetworkSiteDetails(networkSiteDetails);
				Map<String, String> cpeStatus = new HashMap<>();
				List<Integer> siAssetIds = new ArrayList<>();
				sdwanCpeDetail.setCpeAtributeid(siAssetIds);
				List<VwServiceAssetAttributeBean> underyAssetDetails = assetGroupedByName.get(cpe);
				LOGGER.info("Processing CPE name {} and service id {} ", cpe, overlay.getServiceId());
				sdwanCpeDetail.setCpeName(underyAssetDetails.get(0).getAssetName());
				sdwanCpeDetail.setControllers(new ArrayList<>());
				Map<String, String> links = new HashMap<>();
				izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetail, cpeStatus,
						sdwanSiteDetail, instanceByCode,links);
				sdwanCpeDetail.setCpeAvailability(cpeStatus.get(ServiceInventoryConstants.CPE_AVAILABILITY));
				sdwanCpeDetail.setCpeStatus(cpeStatus.get(ServiceInventoryConstants.CPE_STATUS));
				sdwanSiteDetail.getSdwanCpeDetails().add(sdwanCpeDetail);
				if (ServiceInventoryConstants.ONLINE.equals(sdwanCpeDetail.getCpeStatus())) {
					sdwanCpeDetail.setLinks(new ArrayList<>());
					if (!links.isEmpty()) {
						links.forEach((linkName, linkStatus) -> {
							Attributes attributes = new Attributes();
							attributes.setAttributeName(linkName);
							attributes.setAttributeValue(linkStatus);
							sdwanCpeDetail.getLinks().add(attributes);
							sdwanSiteDetail.setUpLinkCount(ServiceInventoryConstants.UP.equalsIgnoreCase(linkStatus)
									? sdwanSiteDetail.getUpLinkCount() + 1
									: sdwanSiteDetail.getUpLinkCount());
							sdwanSiteDetail.setDownLinkCount(ServiceInventoryConstants.DOWN.equalsIgnoreCase(linkStatus)
									? sdwanSiteDetail.getDownLinkCount() + 1
									: sdwanSiteDetail.getDownLinkCount());
						});
					} else
						sdwanSiteDetail.setDownLinkCount(sdwanSiteDetail.getDownLinkCount() + 1);
				}
				else
					sdwanSiteDetail.setDownLinkCount(sdwanSiteDetail.getDownLinkCount() + 1);
				underyAssetDetails.stream().forEach(underlaySite -> {
					LOGGER.info("Processing Network serviceId {} ", underlaySite.getServiceId());
					NetworkSiteDetails networkSiteDetail = new NetworkSiteDetails();
					networkSiteDetail.setNetworkSiteServiceId(underlaySite.getServiceId());
					networkSiteDetail.setNetworkSiteAlias(underlaySite.getSiteAlias());
					networkSiteDetail.setNetworkSiteDestCity(underlaySite.getDestinationCity());
					networkSiteDetail.setNetworkSiteDestCountry(underlaySite.getDestinationCountry());
					networkSiteDetail.setNetworkSiteSrcCity(underlaySite.getSourceCity());
					networkSiteDetail.setNetworkSiteSrcCountry(underlaySite.getSourceCountry());
					networkSiteDetail.setSiteStatus(sdwanCpeDetail.getCpeStatus());
					siAssetIds.add(underlaySite.getAssetId());
					networkSiteDetails.add(networkSiteDetail);
					sdwanSiteDetail.setOnlineCpeCount(
							ciscoServiceInventoryUtils.incrementCountIfOnline(sdwanCpeDetail.getCpeStatus(), sdwanSiteDetail.getOnlineCpeCount()));
					sdwanSiteDetail.setOfflineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOffline(sdwanCpeDetail.getCpeStatus(),
							sdwanSiteDetail.getOfflineCpeCount()));

				});

			});
		}
		sdwanSiteDetail.setSiteStatus(sdwanSiteDetail.getUpLinkCount() == 0 ? ServiceInventoryConstants.OFFLINE
				: (sdwanSiteDetail.getDownLinkCount() == 0 ? ServiceInventoryConstants.ONLINE
						: ServiceInventoryConstants.DEGRADED));
		sdwanSiteDetailsBean.setOnlineCount(
				ciscoServiceInventoryUtils.incrementCountIfOnline(sdwanSiteDetail.getSiteStatus(), sdwanSiteDetailsBean.getOnlineCount()));
		sdwanSiteDetailsBean.setOfflineCount(
				ciscoServiceInventoryUtils.incrementCountIfOffline(sdwanSiteDetail.getSiteStatus(), sdwanSiteDetailsBean.getOfflineCount()));
		sdwanSiteDetailsBean.setDegradedCount(
				ciscoServiceInventoryUtils.incrementCountIfDegraded(sdwanSiteDetail.getSiteStatus(), sdwanSiteDetailsBean.getDegradedCount()));
	}
		
	/**
	 * Method to search and sort SDWAN site details for cisco
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<SdwanSiteDetailsBean> getSdwanDetailsBasedOnFilters(SiteDetailsSearchRequest request) throws TclCommonException {
		LOGGER.info("Entering SDWAN search and sort MDC token {}",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		List<SdwanSiteDetailsBean> sdwanDetailsList = new ArrayList<>();
		if(request.getProductId()==null || request.getPage()==null || request.getSize()<=0 ) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
     }
     try {
			LOGGER.info("SDWAN search and sort request {}", request);
			isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, request.getCustomerLeId());
			if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				Specification<SIServiceDetail> spec;

				   if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())){
					  spec = ViewSiServiceInfoAllSpecification.getServiceInfoAllSearchAndSort(request.getSearchText(), request.getSize(), request.getPage(), request.getSortBy(), request.getSortOrder(), customerIds, partnerLeIds, request.getProductId(), request.getCustomerId(), request.getPartnerId());
				   }
				   else{
					   spec = ViewSiServiceInfoAllSpecification.getServiceInfoAllSearchAndSort(request.getSearchText(), request.getSize(), request.getPage(), request.getSortBy(), request.getSortOrder(), customerIds, null, request.getProductId(), request.getCustomerId(), null);
				   }
	           Page<SIServiceDetail> data = siServiceDetailRepository.findAll(spec, PageRequest.of(request.getPage() - 1, request.getSize()));
	           
	           LOGGER.info("Inside getSdwanDetailsBasedOnFilters and data for applied spec {}",data);
	           List<ViewSiServiceInfoAllBean> vwSiInfoOverlay = new ArrayList<>();
	           List<Integer> sdwanSysId = new ArrayList<>();
	           List<String> sdwanServiceIds = new ArrayList<>();
	           SdwanSiteDetailsBean sdwanSiteDetailsBean = new SdwanSiteDetailsBean();
	           List<SdwanSiteDetails> overlaySiteDetails = new ArrayList<>();
	           sdwanSiteDetailsBean.setSiteDetails(overlaySiteDetails);
               if(data!=null && data.getContent()!=null && !data.getContent().isEmpty()) {
            	   data.getContent().stream().forEach(siServiceDetail->{
            		   ciscoServiceInventoryUtils.constructVwSiServiceInfoBean(vwSiInfoOverlay, siServiceDetail, sdwanSysId, sdwanServiceIds);
            	   });
					getSdwanSiteData(sdwanSiteDetailsBean, vwSiInfoOverlay, sdwanServiceIds, sdwanSysId, null);
					sdwanDetailsList.add(sdwanSiteDetailsBean);
					sdwanSiteDetailsBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
					return new PagedResult(sdwanDetailsList, data.getTotalElements(), data.getTotalPages());
	               }
				
				 
			}
     } catch(Exception e) {
    	 throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_ERROR);
     }		
		return null;
		
		}
		

		/**
		 * To fetch and save underlay info in bean using multi-threading
		 *
		 * @param sdwanSiteDetailsBean
		 * @param uderlayGroupedByOverlay
		 * @param ciscoBulkcpeStatusResponse
		 * @param instanceByCode
		 */
	private	void persistUnderlayInformationThreaded(SdwanSiteDetailsBean sdwanSiteDetailsBean,
				Map<String, List<VwServiceAssetAttributeBean>> uderlayGroupedByOverlay,
				CiscoBulkCpeResponse ciscoBulkcpeStatusResponse, Map<String, List<SdwanEndpoints>> instanceByCode) {
			List<CompletableFuture> completableFutures = new ArrayList<>();
			sdwanSiteDetailsBean.getSiteDetails().forEach(sdwanSiteDetail -> {
				List<VwServiceAssetAttributeBean> undeyLays = uderlayGroupedByOverlay
						.get(sdwanSiteDetail.getSdwanServiceId());
				if (undeyLays != null) {
					Set<String> cpes = new HashSet<>();
					undeyLays.stream().forEach(underlayAsset -> cpes.add(underlayAsset.getAssetName()));
					Map<String, List<VwServiceAssetAttributeBean>> assetGroupedByName = undeyLays.stream()
							.collect(Collectors.groupingBy(VwServiceAssetAttributeBean::getAssetName));
					cpes.stream().forEach(cpe -> {
						LOGGER.info("Thread {} Inside site listing processing CPE {} ", Thread.currentThread().getName(),
								cpe);
						SdwanCpeDetails sdwanCpeDetail = new SdwanCpeDetails();
						List<NetworkSiteDetails> networkSiteDetails = new ArrayList<>();
						sdwanCpeDetail.setNetworkSiteDetails(networkSiteDetails);
						Map<String, String> status = new HashMap<>();
						List<Integer> siAssetIds = new ArrayList<>();
						sdwanCpeDetail.setCpeAtributeid(siAssetIds);
						List<VwServiceAssetAttributeBean> underyAssetDetails = assetGroupedByName.get(cpe);
						LOGGER.info("Thread {} Processing CPE name {} and service id {} ", Thread.currentThread().getName(),
								cpe, sdwanSiteDetail.getSdwanServiceId());
						sdwanCpeDetail.setCpeName(underyAssetDetails.get(0).getAssetName());
						sdwanCpeDetail.setControllers(new ArrayList<>());
						Map<String, String> links = new HashMap<>();
						completableFutures.add(izoSdwanAsyncTasks.getSdwanCpeInfoFromCiscoResponseAsync(
								ciscoBulkcpeStatusResponse, sdwanCpeDetail, status,
								sdwanSiteDetail, instanceByCode, links));
						underyAssetDetails.stream().forEach(underlaySite -> {
							LOGGER.info("Thread {} Processing Network serviceId {} ", Thread.currentThread().getName(),
									underlaySite.getServiceId());
							NetworkSiteDetails networkSiteDetail = new NetworkSiteDetails();
							networkSiteDetail.setNetworkSiteServiceId(underlaySite.getServiceId());
							networkSiteDetail.setNetworkSiteAlias(underlaySite.getSiteAlias());
							networkSiteDetail.setNetworkSiteDestCity(underlaySite.getDestinationCity());
							networkSiteDetail.setNetworkSiteDestCountry(underlaySite.getDestinationCountry());
							networkSiteDetail.setNetworkSiteSrcCity(underlaySite.getSourceCity());
							networkSiteDetail.setNetworkSiteSrcCountry(underlaySite.getSourceCountry());
							networkSiteDetail.setSiteStatus(sdwanCpeDetail.getCpeStatus());
							siAssetIds.add(underlaySite.getAssetId());
							networkSiteDetails.add(networkSiteDetail);
							sdwanSiteDetail.setOnlineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOnline(sdwanCpeDetail.getCpeStatus(),
									sdwanSiteDetail.getOnlineCpeCount()));
							sdwanSiteDetail.setOfflineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOffline(sdwanCpeDetail.getCpeStatus(),
									sdwanSiteDetail.getOfflineCpeCount()));

						});
					});
				}
			});
			for (CompletableFuture completableFuture : completableFutures) {
				completableFuture.join();
			}
		}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResult<SdwanSiteDetailsBean> getSdwanDetailsBasedOnFiltersWithStatus(SiteDetailsSearchRequest request,
			Boolean status) throws TclCommonException {
		LOGGER.info("Entering SDWAN search and sort MDC token {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		List<SdwanSiteDetailsBean> sdwanDetailsList = new ArrayList<>();
		if (request.getProductId() == null) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
			}
		try {
				LOGGER.info("SDWAN search and sort request {}", request);
				isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, request.getCustomerLeId());
				LOGGER.info("SDWAN SITE FILTER CustomerID :: {}, CustomerLeIDs :: {}, PartnerId :: {},PartnerLeIds :: {}", request.getCustomerId(),customerIds, request.getPartnerId(), partnerLeIds);
				if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
					Specification<SIServiceDetail> spec;

					if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
						spec = ViewSiServiceInfoAllSpecification.getServiceInfoAllSearchAndSort(request.getSearchText(),
								request.getSize(), request.getPage(), request.getSortBy(), request.getSortOrder(),
								customerIds, partnerLeIds, request.getProductId(), request.getCustomerId(),
								request.getPartnerId());
					} else {
						spec = ViewSiServiceInfoAllSpecification.getServiceInfoAllSearchAndSort(request.getSearchText(),
								request.getSize(), request.getPage(), request.getSortBy(), request.getSortOrder(),
								customerIds, null, request.getProductId(), request.getCustomerId(), null);
					}
					List<SIServiceDetail> data = siServiceDetailRepository.findAll(spec);

					LOGGER.info("Inside getSdwanDetailsBasedOnFilters and data for applied spec {}", data);
					List<ViewSiServiceInfoAllBean> vwSiInfoOverlay = new ArrayList<>();
					List<Integer> sdwanSysId = new ArrayList<>();
					List<String> sdwanServiceIds = new ArrayList<>();
					SdwanSiteDetailsBean sdwanSiteDetailsBean = new SdwanSiteDetailsBean();
					List<SdwanSiteDetails> overlaySiteDetails = new ArrayList<>();
					sdwanSiteDetailsBean.setSiteDetails(overlaySiteDetails);
					if (data != null && !data.isEmpty()) {
						data.stream().forEach(siServiceDetail -> {
							ciscoServiceInventoryUtils.constructVwSiServiceInfoBean(vwSiInfoOverlay, siServiceDetail, sdwanSysId, sdwanServiceIds);
						});
						getSdwanSiteData(sdwanSiteDetailsBean, vwSiInfoOverlay, sdwanServiceIds, sdwanSysId, status);
						//calling switch case for sdwan site details based on group by
						ciscoServiceInventoryUtils.constructSdwanSiteDetailsByGroup(request,sdwanSiteDetailsBean);
						if (request.getPage() != -1) {
							if ((request.getPage() * request.getSize()) <= sdwanSiteDetailsBean.getSiteDetails().size())
								sdwanSiteDetailsBean.setSiteDetails(sdwanSiteDetailsBean.getSiteDetails().subList(
										(request.getPage() - 1) * request.getSize(),
										request.getPage() * request.getSize()));
							else
								sdwanSiteDetailsBean.setSiteDetails(sdwanSiteDetailsBean.getSiteDetails().subList(
										(request.getPage() - 1) * request.getSize(),
										sdwanSiteDetailsBean.getSiteDetails().size()));
						}
						sdwanDetailsList.add(sdwanSiteDetailsBean);
						sdwanSiteDetailsBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
						return new PagedResult(sdwanDetailsList, sdwanSiteDetailsBean.getSiteDetails().size(),
								sdwanSiteDetailsBean.getSiteDetails().size() % request.getSize() == 0
										? sdwanSiteDetailsBean.getSiteDetails().size() / request.getSize()
										: (sdwanSiteDetailsBean.getSiteDetails().size() / request.getSize()) + 1);
					}
				}
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			return null;
		}

		/**
		 * Get status count of all sites and cpe's associated with a customer for cisco
		 *
		 * @param productId
		 * @param customerId
		 * @param customerLeId
		 * @param partnerId
		 * @return
		 * @throws IOException
		 * @throws TclCommonException
		 */
		public SiteAndCpeStatusCount getSiteAndCpeStatusCount(Integer productId, Integer customerId, Integer customerLeId,
				Integer partnerId) throws IOException, TclCommonException {
			LOGGER.info("Entering site and cpe status count MDC token {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			List<Integer> customerIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();
			SiteAndCpeStatusCount siteAndCpeStatusCount = new SiteAndCpeStatusCount();
			isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
			LOGGER.info("Customer ID : "+ customerId + "customer" + customerIds);
			List<Map<String, Object>> allSiteInfo = vwSiServiceInfoAllRepository.findAllSdwanSites(customerIds,
					partnerLeIds, productId, customerId, partnerId);
			List<String> sdwanServiceIds = allSiteInfo.stream().map(siteInfo -> (String) siteInfo.get("srv_service_id"))
					.collect(Collectors.toList());
			List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository.findAttributesBySdwanServiceIds(
					sdwanServiceIds, Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL, ServiceInventoryConstants.CISCO_SITE_ID,
							ServiceInventoryConstants.SITE_LIST, ServiceInventoryConstants.PRODUCT_FLAVOUR),
					productId);
			List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
			if (overLayServiceAttrs != null & !overLayServiceAttrs.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				overLayServiceAttrs.stream().forEach(srvAttribute -> {
					vwServiceAttributes.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
				});
			}
			Set<String> instanceRegions = new HashSet<>();
			vwServiceAttributes.stream().filter(
					attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
					.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
			List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
			CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
			if (Objects.nonNull(ciscoBulkcpeStatusResponse)
					&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
				List<Map<String, Object>> underlaySitesAndCpeInfo = vwSiServiceInfoAllRepository
						.findUnderlaysBySdwanServiceIdGroupedByName(sdwanServiceIds);
				Map<String, String> cpeStatusInfo = new HashMap<>();
				Map<String,String> siteIdStatus= new HashMap<>();
				allSiteInfo.forEach(siteInfo -> {
					SdwanSiteDetails sdwanSiteDetails = new SdwanSiteDetails();
					sdwanSiteDetails.setSdwanCpeDetails(new ArrayList<>());
					sdwanSiteDetails.setSdwanServiceId((String) siteInfo.get("srv_service_id"));
					vwServiceAttributes.stream()
							.filter(attr -> sdwanSiteDetails.getSdwanServiceId().equals(attr.getServiceId()))
							.forEach(attr -> {
								String attrName = attr.getAttributeName();
								LOGGER.info("Processing service atrribute {} and service id {}", attrName,
										sdwanSiteDetails.getSdwanServiceId());
								if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
									sdwanSiteDetails.setOrganisationName(attr.getAttributeValue());
								} else if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
									sdwanSiteDetails.setInstanceRegion(attr.getAttributeValue());
								}
							});
					underlaySitesAndCpeInfo.stream()
							.filter(underlaySite -> underlaySite.get("sdwan_id").equals(siteInfo.get("srv_service_id")))
							.forEach(underlaySite -> {
								SdwanCpeDetails sdwanCpeDetails = new SdwanCpeDetails();
								sdwanCpeDetails.setControllers(new ArrayList<>());
								sdwanCpeDetails.setCpeName((String) underlaySite.get("asset_name"));
								Map<String, String> links = new HashMap<>();
								izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetails, cpeStatusInfo,
										sdwanSiteDetails, instanceByCode,links);
								sdwanCpeDetails.setCpeStatus(cpeStatusInfo.get(ServiceInventoryConstants.CPE_STATUS));
								sdwanCpeDetails.setSdwanServiceId((String) underlaySite.get("sdwan_id"));
								if (ServiceInventoryConstants.ONLINE.equals(sdwanCpeDetails.getCpeStatus())) {
									sdwanCpeDetails.setLinks(new ArrayList<>());
									if (!links.isEmpty()) {
										links.forEach((linkName, linkStatus) -> {
											Attributes attributes = new Attributes();
											attributes.setAttributeName(linkName);
											attributes.setAttributeValue(linkStatus);
											sdwanCpeDetails.getLinks().add(attributes);
											sdwanSiteDetails.setUpLinkCount(
													ServiceInventoryConstants.UP.equalsIgnoreCase(linkStatus)
															? sdwanSiteDetails.getUpLinkCount() + 1
															: sdwanSiteDetails.getUpLinkCount());
											sdwanSiteDetails.setDownLinkCount(
													ServiceInventoryConstants.DOWN.equalsIgnoreCase(linkStatus)
															? sdwanSiteDetails.getDownLinkCount() + 1
															: sdwanSiteDetails.getDownLinkCount());
										});
									} else
										sdwanSiteDetails.setDownLinkCount(sdwanSiteDetails.getDownLinkCount() + 1);
								} else
									sdwanSiteDetails.setDownLinkCount(sdwanSiteDetails.getDownLinkCount() + 1);
								if (ServiceInventoryConstants.ONLINE.equals(sdwanCpeDetails.getCpeStatus())) {
									siteAndCpeStatusCount.setCpeOnlineCount(siteAndCpeStatusCount.getCpeOnlineCount() + 1);
									sdwanSiteDetails.setOnlineCpeCount(sdwanSiteDetails.getOnlineCpeCount() + 1);
								} else {
									siteAndCpeStatusCount
											.setCpeOfflineCount(siteAndCpeStatusCount.getCpeOfflineCount() + 1);
									sdwanSiteDetails.setOfflineCpeCount(sdwanSiteDetails.getOfflineCpeCount() + 1);
								}
								sdwanSiteDetails.getSdwanCpeDetails().add(sdwanCpeDetails);
							});
					sdwanSiteDetails
							.setSiteStatus(sdwanSiteDetails.getUpLinkCount() == 0 ? ServiceInventoryConstants.OFFLINE
									: (sdwanSiteDetails.getDownLinkCount() == 0 ? ServiceInventoryConstants.ONLINE
											: ServiceInventoryConstants.DEGRADED));
					switch (sdwanSiteDetails.getSiteStatus()) {
					case ServiceInventoryConstants.ONLINE:
						siteAndCpeStatusCount.setSiteOnlineCount(siteAndCpeStatusCount.getSiteOnlineCount() + 1);
						break;
					case ServiceInventoryConstants.DEGRADED:
						siteAndCpeStatusCount.setSiteDegradedCount(siteAndCpeStatusCount.getSiteDegradedCount() + 1);
						break;
					default:
						siteAndCpeStatusCount.setSiteOfflineCount(siteAndCpeStatusCount.getSiteOfflineCount() + 1);
					}
					siteIdStatus.put((String) siteInfo.get("srv_service_id"), sdwanSiteDetails.getSiteStatus());
					siteAndCpeStatusCount.setSiteStatusDetails(siteIdStatus);
				});
				siteAndCpeStatusCount.setAllSitesCount(siteAndCpeStatusCount.getSiteDegradedCount()
						+ siteAndCpeStatusCount.getSiteOfflineCount() + siteAndCpeStatusCount.getSiteOnlineCount());
				siteAndCpeStatusCount.setAllCpeCount(siteAndCpeStatusCount.getCpeOfflineCount()+siteAndCpeStatusCount.getCpeOnlineCount());
			}
			return siteAndCpeStatusCount;
		}
		
		/**
		 * To fetch SDWAN Site CPE details tagged to customer/partner
		 * @param productId
		 * @param page
		 * @param size
		 * @param customerId
		 * @param partnerId
		 * @param customerLeId
		 * @return
		 * @throws IOException
		 * @throws TclCommonException
		 */
		public CiscoCPEInformationBean getCPEDetails(Integer productId, Integer page, Integer size, Integer customerId,
			Integer partnerId, Integer customerLeId) throws IOException, TclCommonException {
		CiscoCPEInformationBean sdwanCPEInformationBean = new CiscoCPEInformationBean();

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
		if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
			List<Map<String, Object>> data = null;
			if (page == -1) {
				data = serviceAssetInfoRepository.findSdwanCpeBySiteListAttribute(customerId, customerIds, partnerId, partnerLeIds,
						productId);
			} else {
				page = (page - 1) * size;
				data = serviceAssetInfoRepository.findSdwanCpeBySiteListAttribute(customerId, customerIds, partnerId, partnerLeIds, page,
						size, productId);
			}
			LOGGER.info("Inside getCPEDetails list fetched cpe size {} ", data.size());
			List<String> siteNames = new ArrayList<>();
			Map<String, String> map = new HashMap<>();
			List<Integer> underlaySysIds = new ArrayList<>();
			List<String> overlaySysIds = new ArrayList<>();
			Map<String,String> siteAndAliasMapping=new HashMap<>();
			if (Objects.nonNull(data) && !data.isEmpty()) {
				data.stream().forEach(assetsData->{
					underlaySysIds.add((Integer) assetsData.get("asset_id"));
					overlaySysIds.add((String) assetsData.get("site_name"));
				});
				 List<ViewSiServiceInfoAll> underlays = vwSiServiceInfoAllRepository.findByServiceIdInAndServiceStatusNotIn(overlaySysIds,Arrays.asList(ServiceInventoryConstants.TERMINATED,ServiceInventoryConstants.UNDER__PROVISIONING));
				underlays.stream().forEach(atr->{
					siteAndAliasMapping.put(atr.getServiceId(),atr.getSiteAlias());
					
				});
				LOGGER.info("Inside SDWAN CPE listing fetching CPE alias for underlay sysid {} ", underlaySysIds);
				List<SIServiceAssetAdditionalInfo> cpeAliasList = siServiceAssetAdditionalInfoRepository
						.findByAssetSysIdInAndAttributeNameIn(underlaySysIds,
								Arrays.asList(ServiceInventoryConstants.SDWAN_CPE_ALIAS));
				LOGGER.info("Inside SDWAN CPE listing fetching service attr for IZOSDWAN servicedetail id {} ", overlaySysIds);
				List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository.findAttributesBySdwanServiceIds(
						overlaySysIds, Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL, ServiceInventoryConstants.CISCO_SITE_ID,
								ServiceInventoryConstants.SITE_LIST, ServiceInventoryConstants.PRODUCT_FLAVOUR),
						productId);
				List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
				if (overLayServiceAttrs != null & !overLayServiceAttrs.isEmpty()) {
					ObjectMapper mapper = new ObjectMapper();
					overLayServiceAttrs.stream().forEach(srvAttribute -> {
						vwServiceAttributes.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
					});
				}
				Set<String> instanceRegions = new HashSet<>();
				vwServiceAttributes.stream()
						.filter(attr -> attr.getAttributeName()
								.equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
						.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
				LOGGER.info("Inside SDWAN CPE listing fetching sdwan endpoint for server code {} ", instanceRegions);
				List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
						.findByServerCodeIn(instanceRegions);
				Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
						.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
				CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
				CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
				if (Objects.nonNull(ciscoBulkcpeStatusResponse)
						&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
					List<CiscoCPEBean> sdwanCPEs = data.stream().map(cpeData -> {
						LOGGER.info("Inside SDWAN CPE listing persiting CPE data and cpe name {} ", cpeData.get("cpe_name"));
						CiscoCPEBean sdwanCPEBean = new CiscoCPEBean();
						sdwanCPEBean.setId((Integer) cpeData.get("asset_id"));
						sdwanCPEBean.setCpeName((String) cpeData.get("cpe_name"));
						sdwanCPEBean.setThisUnderlays(new ArrayList<>());
						sdwanCPEBean.setCity((String) cpeData.get("city"));
						sdwanCPEBean.setCountry((String) cpeData.get("country"));
						sdwanCPEBean.setSdwanSiteName((String) cpeData.get("site_name"));
						sdwanCPEBean.setSdwanServiceId((String) cpeData.get("site_name"));
						sdwanCPEBean
								.setUnderlayServiceIds(Arrays.asList(((String) cpeData.get("service_id")).split(",")));
						sdwanCPEBean.setDescription((String) cpeData.get("description"));
                        sdwanCPEBean.setModel((String) cpeData.get("model"));
                        sdwanCPEBean.setSiteAddress((String) cpeData.get("site_address"));
						sdwanCPEBean.setSerialNumber((String) cpeData.get("serial_no"));
						sdwanCPEBean.setSrvSysId((Integer) cpeData.get("srv_sys_id"));
						if(siteAndAliasMapping.get(sdwanCPEBean.getSdwanServiceId()) != null)
							sdwanCPEBean.setSiteAlias(siteAndAliasMapping.get(sdwanCPEBean.getSdwanServiceId()));
						CiscoSiteListBean sdwanTemplateBean = new CiscoSiteListBean();
						sdwanTemplateBean.setAttributeId((Integer) cpeData.get("attributeId"));
						//sdwanTemplateBean.setTemplateName((String) cpeData.get("attributeValue"));
						List<String> name= new ArrayList<>();
						Set<String> ciscoSiteIdList= new HashSet<>();
						ciscoBulkSiteListResponse.getCiscoSiteListDetails().stream().forEach(ciscoSiteList->{
							if(ciscoSiteList.getListId().equalsIgnoreCase((String) cpeData.get("attributeValue"))) {
								sdwanTemplateBean.setListId((String) cpeData.get("attributeValue"));
								name.add(ciscoSiteList.getName());
								ciscoSiteList.getSiteIdentries().stream().forEach(siteId->ciscoSiteIdList.add(String.valueOf(siteId.getSiteId())));
							}
						});
						LOGGER.info("name {}", name);
						sdwanTemplateBean.setListName(name.get(0));
						sdwanTemplateBean.setSiteId(ciscoSiteIdList);
						sdwanCPEBean.setCiscoSiteList(sdwanTemplateBean);
						vwServiceAttributes.stream()
								.filter(attr -> cpeData.get("site_name").equals(attr.getServiceId())).forEach(attr -> {
									String attrName = attr.getAttributeName();
									LOGGER.info("Processing service atrribute {} and service id {}", attrName,
											sdwanCPEBean.getSdwanServiceId());
									if (attrName
											.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
										sdwanCPEBean.setOrganisationName(attr.getAttributeValue());
									} else if (attrName
											.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
										sdwanCPEBean.setInstanceRegion(attr.getAttributeValue());
									}
								});
						sdwanCPEBean.setControllers(new ArrayList<>());
//						getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCPEBean.getCpeName(), map,
//								sdwanCPEBean.getControllers());
						Map<String, String> links = new HashMap<>();
						SdwanCpeDetails sdwanCpeDetails =new SdwanCpeDetails();
						sdwanCpeDetails.setCpeName(sdwanCPEBean.getCpeName());
						izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetails, map,
								new SdwanSiteDetails(), instanceByCode,links);
						
						sdwanCPEBean.setCpeStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
						sdwanCPEBean.setCpeAvailability(map.get(ServiceInventoryConstants.CPE_AVAILABILITY));
						sdwanCPEBean.setOsVersion(map.get(ServiceInventoryConstants.OS_VERSION));
						sdwanCPEBean.setUnderlaySysId((Integer) cpeData.get("srv_sys_id"));
						cpeAliasList.stream().filter(cpeAlias -> sdwanCPEBean.getId().equals(cpeAlias.getAssetSysId()))
								.forEach(cpeAlias -> sdwanCPEBean.setAlias(cpeAlias.getAttributeValue()));
						if (ServiceInventoryConstants.ONLINE.equals(sdwanCPEBean.getCpeStatus())) {
							
							sdwanCPEBean.setLinks(new ArrayList<>());
							links.forEach((linkName, linkStatus) -> {
								Attributes attributes = new Attributes();
								attributes.setAttributeName(linkName);
								attributes.setAttributeValue(linkStatus);
								sdwanCPEBean.getLinks().add(attributes);
							});
						}
						siteNames.add(sdwanCPEBean.getSdwanServiceId());
						return sdwanCPEBean;
					}).collect(Collectors.toList());
					// fetch the underlay service details tagged to izosdwan site
					List<Map<String, Object>> underlayServices = vwSiServiceInfoAllRepository
							.findUnderlayServiceIdbySdwanServiceId(siteNames);
					if (Objects.nonNull(underlayServices) && !underlayServices.isEmpty()) {
						sdwanCPEs.forEach(sdwanCPE -> {
							List<CpeUnderlaySitesBean> sdwanSiteAndStatus = new ArrayList<>();
							underlayServices.stream()
									.filter(underlayService -> sdwanCPE.getSdwanServiceId()
											.equals(((String) underlayService.get("sdwan_id"))))
									.forEach(underlayService -> {
										LOGGER.info("Inside SDWAN CPE listing processing CpeUnderlaySitesBean and underlay service id  {} ", underlayService.get("service_id"));
										CpeUnderlaySitesBean cpeUnderlaySitesBean = new CpeUnderlaySitesBean();
										cpeUnderlaySitesBean.setId((Integer) underlayService.get("sys_id"));
										cpeUnderlaySitesBean.setSiteName((String) underlayService.get("service_id"));
                                        cpeUnderlaySitesBean.setAssetId((Integer) underlayService.get("asset_sys_id"));
										cpeUnderlaySitesBean.setCpeName((String) underlayService.get("asset_name"));
										cpeUnderlaySitesBean.setControllers(new ArrayList<>());
//										getSdwanCpeInfoFromVersaResponse(ciscoBulkcpeStatusResponse,
//												cpeUnderlaySitesBean.getCpeName(), cpeStatus,
//												cpeUnderlaySitesBean.getControllers());
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
										if (sdwanCPE.getUnderlayServiceIds()
												.contains(cpeUnderlaySitesBean.getSiteName())
												&& sdwanCPE.getCpeName().equals(cpeUnderlaySitesBean.getCpeName()))
											sdwanCPE.getThisUnderlays().add(cpeUnderlaySitesBean);
									});
							sdwanCPE.setCpeUnderlaySites(sdwanSiteAndStatus);
							sdwanCPE.setSdwanSiteStatus(
									sdwanCPE.getLinkUpCount() == 0 ? ServiceInventoryConstants.OFFLINE
											: (sdwanCPE.getLinkDownCount() == 0 ? ServiceInventoryConstants.ONLINE
													: ServiceInventoryConstants.DEGRADED));
							// setting the number of online/offline cpe's
							sdwanCPEInformationBean.setOnlineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOnline(sdwanCPE.getCpeStatus(),
									sdwanCPEInformationBean.getOnlineCpeCount()));
							sdwanCPEInformationBean.setOfflineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOffline(sdwanCPE.getCpeStatus(),
									sdwanCPEInformationBean.getOfflineCpeCount()));
						});
					}
					sdwanCPEInformationBean.setCPE(sdwanCPEs);
					sdwanCPEInformationBean.setTotalItems(serviceAssetInfoRepository.findSdwanCpeCount(customerId,
							customerIds, partnerId, partnerLeIds, productId));
					if (sdwanCPEInformationBean.getTotalItems() % size != 0)
						sdwanCPEInformationBean.setTotalPages((sdwanCPEInformationBean.getTotalItems() / size) + 1);
					else
						sdwanCPEInformationBean.setTotalPages(sdwanCPEInformationBean.getTotalItems() / size);
				} else
					throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
			}
		}
		sdwanCPEInformationBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
		return sdwanCPEInformationBean;
	}	
		/**
		 * Cpe details fetch with filter (multithreaded)
		 * @param searchText
		 * @param size
		 * @param page
		 * @param sortBy
		 * @param sortOrder
		 * @param productId
		 * @param customerId
		 * @param customerLeId
		 * @param partnerId
		 * @param groupBy
		 * @return
		 * @throws TclCommonException
		 */
		@SuppressWarnings("unchecked")
		public CiscoCPEInformationBean getCiscoCpeDetailsBasedOnFilters(String searchText, Integer size, Integer page,
				String sortBy, String sortOrder, Integer productId, Integer customerId, Integer customerLeId,
				Integer partnerId, String groupBy) throws TclCommonException {

			List<Integer> customerIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();
			isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
			LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			
			Specification<SIAsset> sdwanServiceDetails;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				sdwanServiceDetails = SIServiceAssetSpecification.getSdwanServiceDetails(searchText, null, partnerLeIds,
						productId, null, partnerId, sortBy, sortOrder, true);
			} else {
				sdwanServiceDetails = SIServiceAssetSpecification.getSdwanServiceDetails(searchText, customerIds, null,
						productId, customerId, null, sortBy, sortOrder, true);
			}

			List<SIAsset> siAssetPaginated = siAssetRepository.findAll(sdwanServiceDetails);
			LOGGER.info("getCpeDetailsBasedOnFiltersPoc Fetched Cpe details : {} items", siAssetPaginated.size());
			LOGGER.info("Cpe details in Poc API  : {} items", siAssetPaginated.toString());
			CiscoCPEInformationBean sdwanCPEInformationBean = new CiscoCPEInformationBean();
			sdwanCPEInformationBean.setCPE(new ArrayList<>());
			if (Objects.nonNull(siAssetPaginated) && !siAssetPaginated.isEmpty()) {
				List<SIAsset> siAssets = siAssetPaginated;
				List<CiscoCPEBean> sdwanCPEBeans = new ArrayList<>();

				Set<String> sdwanServiceIds = new HashSet<>();
				sdwanServiceIds = siAssets.parallelStream().map(siAsset -> siAsset.getSiServiceDetail().getIzoSdwanSrvcId())
						.collect(Collectors.toSet());
				
				List<Integer> underlaySiDetailId = new ArrayList<>();
				underlaySiDetailId = siAssets.parallelStream().map(siAsset -> siAsset.getSiServiceDetail().getId())
						.collect(Collectors.toList());
				List<VwServiceAttributesBean> underlayTemplates = ciscoServiceInventoryUtils.getUnderlaySiteLists(underlaySiDetailId);
				
				List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository
						.findAttributesBySdwanServiceIds(new ArrayList<>(sdwanServiceIds),
								Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL, ServiceInventoryConstants.CISCO_SITE_ID,
										ServiceInventoryConstants.SITE_LIST, ServiceInventoryConstants.PRODUCT_FLAVOUR),
								productId);
				
				List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
				if (overLayServiceAttrs != null & !overLayServiceAttrs.isEmpty()) {
					ObjectMapper mapper = new ObjectMapper();
					overLayServiceAttrs.stream().forEach(srvAttribute -> {
						vwServiceAttributes.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
					});
				}
				LOGGER.info("Overlay attributes : {}", vwServiceAttributes.toString());
				Set<String> instanceRegions = new HashSet<>();
				vwServiceAttributes.stream().filter(
						attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
						.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
				List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
						.findByServerCodeIn(instanceRegions);
				Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
						.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));

				CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
				CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
				if (Objects.nonNull(ciscoBulkcpeStatusResponse)
						&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
					Map<String, String> map = new HashMap<>();
					List<CompletableFuture<String>> completableFutureList = new ArrayList<>();
					siAssets.forEach(siAsset -> {
						CiscoCPEBean sdwanCPEBean = new CiscoCPEBean();
						sdwanCPEBean.setId(siAsset.getId());
						sdwanCPEBean.setCpeName(siAsset.getName());
						sdwanCPEBean.setSdwanSiteName(siAsset.getSiServiceDetail().getIzoSdwanSrvcId());
						sdwanCPEBean.setCity(siAsset.getSiServiceDetail().getSourceCity());
						sdwanCPEBean.setCountry(siAsset.getSiServiceDetail().getSourceCountry());
						sdwanCPEBean.setUnderlaySysId(siAsset.getSiServiceDetail().getId());
						if (Objects.nonNull(siAsset.getSiAssetAttributes()) && !siAsset.getSiAssetAttributes().isEmpty()) {
							siAsset.getSiAssetAttributes().stream().peek(siAssetAttribute -> {
								if (ServiceInventoryConstants.SDWAN_CPE_ALIAS
										.equalsIgnoreCase(siAssetAttribute.getAttributeName()))
									sdwanCPEBean.setAlias(siAssetAttribute.getAttributeValue());
							}).anyMatch(siAssetAttribute -> ServiceInventoryConstants.SDWAN_CPE_ALIAS
									.equalsIgnoreCase(siAssetAttribute.getAttributeName()));
						}
						vwServiceAttributes.stream().filter(
								attr -> siAsset.getSiServiceDetail().getIzoSdwanSrvcId().equals(attr.getServiceId()))
								.forEach(attr -> {
									String attrName = attr.getAttributeName();
									LOGGER.info("Processing service atrribute {} and service id {} thread {}", attrName,
											sdwanCPEBean.getSdwanServiceId(), Thread.currentThread().getName());
									if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
										sdwanCPEBean.setOrganisationName(attr.getAttributeValue());
									} else if (attrName
											.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
										sdwanCPEBean.setInstanceRegion(attr.getAttributeValue());
									}
								});
						ciscoServiceInventoryUtils.constructSdwanSiteListDetails(underlayTemplates, siAsset, sdwanCPEBean,ciscoBulkSiteListResponse);
//						SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
//						sdwanTemplateBean.setTemplateId(
//								siAsset.getSiServiceDetail().getSiServiceAttributes().stream().findAny().get().getId());
//						String listId=siAsset.getSiServiceDetail().getSiServiceAttributes().stream()
//								.findAny().get().getAttributeValue();
//						LOGGER.info("listId {}",siAsset.getSiServiceDetail().getSiServiceAttributes().stream()
//								.findAny().get().getAttributeValue());
						
						//sdwanTemplateBean.setTemplateName(name.get(0));
			//			sdwanCPEBean.setTemplate(sdwanTemplateBean);
						sdwanCPEBean.setSdwanServiceId(siAsset.getSiServiceDetail().getIzoSdwanSrvcId());
						sdwanCPEBean.setUnderlayServiceId(siAsset.getSiServiceDetail().getTpsServiceId());
						sdwanCPEBean.setDescription(siAsset.getDescription());
						sdwanCPEBean.setModel(siAsset.getModel());
						sdwanCPEBean.setSerialNumber(siAsset.getSerialNo());
						sdwanCPEBean.setSiteAddress(siAsset.getSiServiceDetail().getSiteAddress());
						sdwanCPEBean.setControllers(new ArrayList<>());
						sdwanCPEBean.setThisUnderlays(new ArrayList<>());
//						getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCPEBean.getCpeName(), map,
//								sdwanCPEBean.getControllers());
						
						SdwanCpeDetails sdwanCpeDetail= new SdwanCpeDetails();
						sdwanCpeDetail.setCpeName(sdwanCPEBean.getCpeName());
						
						Map<String, String> links = new HashMap<>();
						Map<String, String> status = new HashMap<>();
						completableFutureList.add(izoSdwanAsyncTasks.getSdwanCpeInfoFromCiscoResponseAsync(
								ciscoBulkcpeStatusResponse, sdwanCpeDetail, status,
								new SdwanSiteDetails(), instanceByCode, links));
						for (CompletableFuture<String> completableFuture : completableFutureList) {
							completableFuture.join();
						}
						sdwanCPEBean.setCpeStatus(status.get(ServiceInventoryConstants.CPE_STATUS));
						sdwanCPEBean.setCpeAvailability(status.get(ServiceInventoryConstants.CPE_AVAILABILITY));
						//sdwanCPEBean.setOsVersion(status.get(ServiceInventoryConstants.OS_VERSION));
						sdwanCPEBean.setLinks(sdwanCpeDetail.getLinks());
						sdwanCPEBeans.add(sdwanCPEBean);
					});

					
					sdwanCPEBeans.forEach(sdwanCPE -> {
						List<CpeUnderlaySitesBean> sdwanSiteAndStatus = new ArrayList<>();
						sdwanCPEBeans.stream().filter(
								sdwanCPEBean -> sdwanCPE.getSdwanServiceId().equals((sdwanCPEBean.getSdwanServiceId())))
								.forEach(sdwanCPEBean -> {
									CpeUnderlaySitesBean cpeUnderlaySitesBean = new CpeUnderlaySitesBean();
									cpeUnderlaySitesBean.setId(sdwanCPEBean.getId());
									cpeUnderlaySitesBean.setSiteName(sdwanCPEBean.getUnderlayServiceId());
									cpeUnderlaySitesBean.setCpeName(sdwanCPEBean.getCpeName());
									cpeUnderlaySitesBean.setControllers(new ArrayList<>());
									cpeUnderlaySitesBean.setSiteStatus(sdwanCPEBean.getCpeStatus());
									sdwanCPE.setUnderlaysOnlineCount(ciscoServiceInventoryUtils.incrementCountIfOnline(
											cpeUnderlaySitesBean.getSiteStatus(), sdwanCPE.getUnderlaysOnlineCount()));
									sdwanCPE.setUnderlaysOfflineCount(ciscoServiceInventoryUtils.incrementCountIfOffline(
											cpeUnderlaySitesBean.getSiteStatus(), sdwanCPE.getUnderlaysOfflineCount()));
									sdwanSiteAndStatus.add(cpeUnderlaySitesBean);
									cpeUnderlaySitesBean.setLinks(sdwanCPEBean.getLinks());
									if (Objects.nonNull(sdwanCPEBean.getLinks()) && !sdwanCPEBean.getLinks().isEmpty())
										cpeUnderlaySitesBean.getLinks().forEach(attributes -> {
											sdwanCPE.setLinkUpCount(ServiceInventoryConstants.UP.equalsIgnoreCase(
													attributes.getAttributeValue()) ? sdwanCPE.getLinkUpCount() + 1
															: sdwanCPE.getLinkUpCount());
											sdwanCPE.setLinkDownCount(ServiceInventoryConstants.DOWN.equalsIgnoreCase(
													attributes.getAttributeValue()) ? sdwanCPE.getLinkDownCount() + 1
															: sdwanCPE.getLinkDownCount());
										});
									else
										sdwanCPE.setLinkDownCount(sdwanCPE.getLinkDownCount() + 1);
									if (sdwanCPE.getCpeName().equals(cpeUnderlaySitesBean.getCpeName()))
										sdwanCPE.getThisUnderlays().add(cpeUnderlaySitesBean);
								});
						sdwanCPE.setCpeUnderlaySites(sdwanSiteAndStatus);
						sdwanCPE.setSdwanSiteStatus(sdwanCPE.getLinkUpCount() == 0 ? ServiceInventoryConstants.OFFLINE
								: (sdwanCPE.getLinkDownCount() == 0 ? ServiceInventoryConstants.ONLINE
										: ServiceInventoryConstants.DEGRADED));
						// setting the number of online/offline cpe's
						sdwanCPEInformationBean.setOnlineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOnline(sdwanCPE.getCpeStatus(),
								sdwanCPEInformationBean.getOnlineCpeCount()));
						sdwanCPEInformationBean.setOfflineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOffline(sdwanCPE.getCpeStatus(),
								sdwanCPEInformationBean.getOfflineCpeCount()));
					});
				} else
					throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
				if (Objects.nonNull(groupBy) && !CommonConstants.ALL.equalsIgnoreCase(groupBy)) {
					switch (groupBy) {
					case ServiceInventoryConstants.ONLINE:
						sdwanCPEBeans.retainAll(sdwanCPEBeans.stream().filter(
								sdwanCPE -> ServiceInventoryConstants.ONLINE.equalsIgnoreCase(sdwanCPE.getCpeStatus()))
								.collect(Collectors.toList()));
						break;
					case ServiceInventoryConstants.OFFLINE:
						sdwanCPEBeans.retainAll(sdwanCPEBeans.stream().filter(
								sdwanCPE -> ServiceInventoryConstants.OFFLINE.equalsIgnoreCase(sdwanCPE.getCpeStatus()))
								.collect(Collectors.toList()));
						break;
					}
				}
				else if (ServiceInventoryConstants.STATUS.equalsIgnoreCase(sortBy)) {
					if ("desc".equalsIgnoreCase(sortOrder))
						sdwanCPEBeans
								.sort((CiscoCPEBean s1, CiscoCPEBean s2) -> s1.getCpeStatus().compareTo(s2.getCpeStatus()));
					else
						sdwanCPEBeans
								.sort((CiscoCPEBean s1, CiscoCPEBean s2) -> s2.getCpeStatus().compareTo(s1.getCpeStatus()));
				}
				LOGGER.info("sdwanCPEBeans in POC API  : {}", sdwanCPEBeans.toString());
				//removing duplicates from list
				Set<String> recurringCPEs = new HashSet<>();
				List<CiscoCPEBean> duplicatesRemovedCpes = sdwanCPEBeans.stream().filter(sdwanCPE -> !recurringCPEs.contains(sdwanCPE.getCpeName())).peek(sdwanCPE -> recurringCPEs.add(sdwanCPE.getCpeName())).collect(Collectors.toList());
				//truncating result list to given page-size range after removing duplicates
				if (page != -1) {
					List<CiscoCPEBean> sdwanCPEBeanPaged;
					if ((page * size) <= duplicatesRemovedCpes.size())
						sdwanCPEBeanPaged = duplicatesRemovedCpes.subList((page - 1) * size, page * size);
					else
						sdwanCPEBeanPaged = duplicatesRemovedCpes.subList((page - 1) * size, duplicatesRemovedCpes.size());
					sdwanCPEInformationBean.setCPE(sdwanCPEBeanPaged);
				} else
					sdwanCPEInformationBean.setCPE(sdwanCPEBeans);
				LOGGER.info("sdwanCPEBeans size  : {}", sdwanCPEBeans.size());
				sdwanCPEInformationBean.setTotalItems(sdwanCPEBeans.size());
				sdwanCPEInformationBean.setTotalPages(
						sdwanCPEInformationBean.getTotalItems() % size == 0 ? sdwanCPEInformationBean.getTotalItems() / size
								: (sdwanCPEInformationBean.getTotalItems() / size) + 1);
			}
			sdwanCPEInformationBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
			return sdwanCPEInformationBean;

		}
		/**
		 * Cpe details fetch with filter
		 * @param searchText
		 * @param size
		 * @param page
		 * @param sortBy
		 * @param sortOrder
		 * @param productId
		 * @param customerId
		 * @param customerLeId
		 * @param partnerId
		 * @return
		 * @throws TclCommonException
		 * @throws IOException
		 */
		public CiscoCPEInformationBean getCpeDetailsBasedOnFilters(String searchText, Integer size, Integer page,
				String sortBy, String sortOrder, Integer productId, Integer customerId, Integer customerLeId,
				Integer partnerId) throws TclCommonException, IOException {
			List<Integer> customerIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();

			isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
			LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			
			Specification<SIAsset> sdwanServiceDetails;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				sdwanServiceDetails = SIServiceAssetSpecification.getSdwanServiceDetails(searchText, null, partnerLeIds,
						productId, null, partnerId, sortBy, sortOrder, true);
			} else {
				sdwanServiceDetails = SIServiceAssetSpecification.getSdwanServiceDetails(searchText, customerIds, null,
						productId, customerId, null, sortBy, sortOrder, true);
			}
			Page<SIAsset> siAssetPaginated = siAssetRepository.findAll(sdwanServiceDetails, PageRequest.of(page - 1, size));
			LOGGER.info("Fetched Cpe details in filter API : {} items", siAssetPaginated.getTotalElements());
			LOGGER.info("Cpe details in filter API  : {} items", siAssetPaginated.getContent());
			LOGGER.info("Cpe details in filter API content size : {} items", siAssetPaginated.getContent().size());
			CiscoCPEInformationBean sdwanCPEInformationBean = new CiscoCPEInformationBean();
			sdwanCPEInformationBean.setCPE(new ArrayList<>());if (Objects.nonNull(siAssetPaginated) && Objects.nonNull(siAssetPaginated.getContent())
					&& !siAssetPaginated.getContent().isEmpty()) {
				List<SIAsset> siAssets = siAssetPaginated.getContent();
				List<CiscoCPEBean> sdwanCPEBeans = new ArrayList<>();

				Set<String> sdwanServiceIds = new HashSet<>();
				sdwanServiceIds = siAssets.stream().map(siAsset -> siAsset.getSiServiceDetail().getIzoSdwanSrvcId())
						.collect(Collectors.toSet());
				// Fetching Sdwan templates mapped for underlay
				List<Integer> underlaySysIds = new ArrayList<>();
				underlaySysIds = siAssets.stream().map(siAsset -> siAsset.getSiServiceDetail().getId())
						.collect(Collectors.toList());
				List<VwServiceAttributesBean> underlayTemplates = ciscoServiceInventoryUtils.getUnderlaySiteLists(underlaySysIds);
				List<Map<String, Object>> underlayServices = new ArrayList<>();

				if (!sdwanServiceIds.isEmpty()) {
					// fetch template for the service ids
					// fetch the underlay service details tagged to izosdwan site
					underlayServices = vwSiServiceInfoAllRepository
							.findUnderlayServiceIdbySdwanServiceId(new ArrayList<>(sdwanServiceIds));
				}
				
				List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository.findAttributesBySdwanServiceIds(
						new ArrayList<>(sdwanServiceIds), Arrays.asList(ServiceInventoryConstants.V_MANAGE_URL, ServiceInventoryConstants.CISCO_SITE_ID,
								ServiceInventoryConstants.SITE_LIST, ServiceInventoryConstants.PRODUCT_FLAVOUR),
						productId);
				List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
				if (overLayServiceAttrs != null & !overLayServiceAttrs.isEmpty()) {
					ObjectMapper mapper = new ObjectMapper();
					overLayServiceAttrs.stream().forEach(srvAttribute -> {
						vwServiceAttributes.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
					});
				}
				LOGGER.info("Overlay attributes : {}", vwServiceAttributes.toString());
				Set<String> instanceRegions = new HashSet<>();
				vwServiceAttributes.stream().filter(
						attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
						.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
				List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository	
						.findByServerCodeIn(instanceRegions);
				Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
						.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));

				CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
				CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
				if (Objects.nonNull(ciscoBulkcpeStatusResponse)
						&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
					Map<String, String> map = new HashMap<>();
					siAssets.forEach(siAsset -> {
						CiscoCPEBean sdwanCPEBean = new CiscoCPEBean();
						sdwanCPEBean.setId(siAsset.getId());
						sdwanCPEBean.setCpeName(siAsset.getName());
						sdwanCPEBean.setSdwanSiteName(siAsset.getSiServiceDetail().getIzoSdwanSrvcId());
						sdwanCPEBean.setCity(siAsset.getSiServiceDetail().getSourceCity());
						sdwanCPEBean.setCountry(siAsset.getSiServiceDetail().getSourceCountry());
						sdwanCPEBean.setUnderlaySysId(siAsset.getSiServiceDetail().getId());
						if (Objects.nonNull(siAsset.getSiAssetAttributes()) && !siAsset.getSiAssetAttributes().isEmpty()) {
							siAsset.getSiAssetAttributes().stream().peek(siAssetAttribute -> {
								if (ServiceInventoryConstants.SDWAN_CPE_ALIAS
										.equalsIgnoreCase(siAssetAttribute.getAttributeName()))
									sdwanCPEBean.setAlias(siAssetAttribute.getAttributeValue());
							}).anyMatch(siAssetAttribute -> ServiceInventoryConstants.SDWAN_CPE_ALIAS
									.equalsIgnoreCase(siAssetAttribute.getAttributeName()));
						}
						vwServiceAttributes.stream().filter(
								attr -> siAsset.getSiServiceDetail().getIzoSdwanSrvcId().equals(attr.getServiceId()))
								.forEach(attr -> {
									String attrName = attr.getAttributeName();
									LOGGER.info("Processing service atrribute {} and service id {}", attrName,
											sdwanCPEBean.getSdwanServiceId());
									if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
										sdwanCPEBean.setOrganisationName(attr.getAttributeValue());
									} else if (attrName
											.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
										sdwanCPEBean.setInstanceRegion(attr.getAttributeValue());
									}
								});
						sdwanCPEBean.setSdwanServiceId(siAsset.getSiServiceDetail().getIzoSdwanSrvcId());
						sdwanCPEBean.setUnderlayServiceIds(new ArrayList<>());
						sdwanCPEBean.setDescription(siAsset.getDescription());
						sdwanCPEBean.setModel(siAsset.getModel());
						sdwanCPEBean.setSerialNumber(siAsset.getSerialNo());
						sdwanCPEBean.setSiteAddress(siAsset.getSiServiceDetail().getSiteAddress());
						sdwanCPEBean.setThisUnderlays(new ArrayList<>());
						sdwanCPEBean.setControllers(new ArrayList<>());
						
						Map<String, String> links = new HashMap<>();
						SdwanCpeDetails sdwanCpeDetails =new SdwanCpeDetails();
						sdwanCpeDetails.setCpeName(sdwanCPEBean.getCpeName());
						izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetails, map,
								new SdwanSiteDetails(), instanceByCode,links);
						
						sdwanCPEBean.setCpeStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
						sdwanCPEBean.setCpeAvailability(map.get(ServiceInventoryConstants.CPE_AVAILABILITY));
						sdwanCPEBean.setOsVersion(map.get(ServiceInventoryConstants.OS_VERSION));
						// only SiServiceAttribute with Sdwan_Template_name as attributeName is fetched
						ciscoServiceInventoryUtils.constructSdwanSiteListDetails(underlayTemplates, siAsset, sdwanCPEBean,ciscoBulkSiteListResponse);
						LOGGER.info("siAsset.getSiServiceDetail().getSiServiceAttributes() {}",siAsset.getSiServiceDetail().getSiServiceAttributes());
//						SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
//						sdwanTemplateBean.setTemplateId(
//								siAsset.getSiServiceDetail().getSiServiceAttributes().stream().findAny().get().getId());
//						sdwanTemplateBean.setTemplateName(siAsset.getSiServiceDetail().getSiServiceAttributes().stream()
//								.findAny().get().getAttributeValue());
//						sdwanCPEBean.setTemplate(sdwanTemplateBean);
						if (ServiceInventoryConstants.ONLINE.equals(sdwanCPEBean.getCpeStatus())) {
//							getWanStatusFromVersa(sdwanCPEBean.getCpeName(), sdwanCPEBean.getControllers(),;
//									sdwanCPEBean.getInstanceRegion(), sdwanCPEBean.getOrganisationName(), instanceByCode,
//									links);
							sdwanCPEBean.setLinks(new ArrayList<>());
							links.forEach((linkName, linkStatus) -> {
								Attributes attributes = new Attributes();
								attributes.setAttributeName(linkName);
								attributes.setAttributeValue(linkStatus);
								sdwanCPEBean.getLinks().add(attributes);
							});
						}
						sdwanCPEBeans.add(sdwanCPEBean);
					});
					if (Objects.nonNull(underlayServices) && !underlayServices.isEmpty()) {
						List<Map<String, Object>> finalUnderlayServices = underlayServices;
						Map<String, String> cpeStatus = new HashMap<>();
						sdwanCPEBeans.forEach(sdwanCPE -> {
							List<CpeUnderlaySitesBean> sdwanSiteAndStatus = new ArrayList<>();
							finalUnderlayServices.stream()
									.filter(underlayService -> sdwanCPE.getSdwanServiceId()
											.equals(((String) underlayService.get("sdwan_id"))))
									.forEach(underlayService -> {
										CpeUnderlaySitesBean cpeUnderlaySitesBean = new CpeUnderlaySitesBean();
										cpeUnderlaySitesBean.setId((Integer) underlayService.get("sys_id"));
										cpeUnderlaySitesBean.setSiteName((String) underlayService.get("service_id"));
										cpeUnderlaySitesBean.setCpeName((String) underlayService.get("asset_name"));
										cpeUnderlaySitesBean.setControllers(new ArrayList<>());
//										getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse,
//												cpeUnderlaySitesBean.getCpeName(), cpeStatus,
//												cpeUnderlaySitesBean.getControllers());
										Map<String, String> links = new HashMap<>();
										SdwanCpeDetails sdwanCpeDetails =new SdwanCpeDetails();
										sdwanCpeDetails.setCpeName(cpeUnderlaySitesBean.getCpeName());
										izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetails, cpeStatus,
												new SdwanSiteDetails(), instanceByCode,links);
										cpeUnderlaySitesBean
												.setSiteStatus(cpeStatus.get(ServiceInventoryConstants.CPE_STATUS));
										sdwanCPE.setUnderlaysOnlineCount(ciscoServiceInventoryUtils.incrementCountIfOnline(
												cpeUnderlaySitesBean.getSiteStatus(), sdwanCPE.getUnderlaysOnlineCount()));
										sdwanCPE.setUnderlaysOfflineCount(ciscoServiceInventoryUtils.incrementCountIfOffline(
												cpeUnderlaySitesBean.getSiteStatus(), sdwanCPE.getUnderlaysOfflineCount()));
										sdwanSiteAndStatus.add(cpeUnderlaySitesBean);
										//Map<String, String> links = new HashMap<>();
										if (ServiceInventoryConstants.ONLINE.equals(cpeUnderlaySitesBean.getSiteStatus())) {
//											getWanStatusFromVersa(cpeUnderlaySitesBean.getCpeName(),
//													cpeUnderlaySitesBean.getControllers(), sdwanCPE.getInstanceRegion(),
//													sdwanCPE.getOrganisationName(), instanceByCode, links);
											cpeUnderlaySitesBean.setLinks(new ArrayList<>());
											if (!links.isEmpty()) {
												links.forEach((linkName, linkStatus) -> {
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
										if (sdwanCPE.getCpeName().equals(cpeUnderlaySitesBean.getCpeName()))
											sdwanCPE.getThisUnderlays().add(cpeUnderlaySitesBean);
									});
							sdwanCPE.setCpeUnderlaySites(sdwanSiteAndStatus);
							sdwanCPE.setSdwanSiteStatus(sdwanCPE.getLinkUpCount() == 0 ? ServiceInventoryConstants.OFFLINE
									: (sdwanCPE.getLinkDownCount() == 0 ? ServiceInventoryConstants.ONLINE
											: ServiceInventoryConstants.DEGRADED));
							// setting the number of online/offline cpe's
							sdwanCPEInformationBean.setOnlineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOnline(sdwanCPE.getCpeStatus(),
									sdwanCPEInformationBean.getOnlineCpeCount()));
							sdwanCPEInformationBean.setOfflineCpeCount(ciscoServiceInventoryUtils.incrementCountIfOffline(sdwanCPE.getCpeStatus(),
									sdwanCPEInformationBean.getOfflineCpeCount()));
						});
					}
				} else
					throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
				LOGGER.info("sdwanCPEBeans details in filter API  : {} items", sdwanCPEBeans);
				sdwanCPEInformationBean.setCPE(sdwanCPEBeans);
//				sdwanCPEInformationBean.setTotalPages(siAssetPaginated.getTotalPages());
//				sdwanCPEInformationBean.setTotalItems((int) siAssetPaginated.getTotalElements());
				LOGGER.info("sdwanCPEBeans size  : {}", sdwanCPEBeans.size());
				sdwanCPEInformationBean.setTotalItems(sdwanCPEBeans.size());
				sdwanCPEInformationBean.setTotalPages(
						sdwanCPEInformationBean.getTotalItems() % size == 0 ? sdwanCPEInformationBean.getTotalItems() / size
								: (sdwanCPEInformationBean.getTotalItems() / size) + 1);
			}
			sdwanCPEInformationBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
			return sdwanCPEInformationBean;
		}
	public CiscoApplications getSdwanAppplications(Integer productId, Integer customerId, Integer customerLeId,
		Integer partnerId) throws TclCommonException {
					
		CiscoApplications sdwanApplications = new CiscoApplications();
		try {
		List<Integer> customerLeIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId
				+ "partnerLeIds " + partnerLeIds);
		sdwanApplications.setCustomerId(customerId);
		sdwanApplications.setCustomerLeIds(customerLeIds);
		sdwanApplications.setPartnerId(partnerId);
		sdwanApplications.setPartnerLeId(partnerLeIds);
		sdwanApplications.setProductId(productId);
		sdwanApplications.setLastUpdatedDate(ServiceInventoryUtils.dateConvertor(new Date()));
		List<Map<String, Object>> data = null;
		data = serviceAssetInfoRepository.findSdwanCpeBySiteListAttribute(customerId, customerLeIds, partnerId, partnerLeIds,
				productId);
		Map<String, String> map = new HashMap<>();
		List<CiscoApplicationsDetailBean> versaAppls = new ArrayList<>();
		sdwanApplications.setCiscoApplications(versaAppls);
		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findSdwanServiceAttributes
				(customerLeIds, partnerLeIds, productId, customerId, partnerId,
				Arrays.asList(
						ServiceInventoryConstants.V_MANAGE_URL),
						Arrays.asList(ServiceInventoryConstants.SITE_LIST,ServiceInventoryConstants.CISCO_SITE_ID));
		List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
		List<SdwanEndpoints> sdwanEndPointsForRegions = new ArrayList<>();

		if(Objects.nonNull(data) && !data.isEmpty()) {
			if (serviceAttributes != null && !serviceAttributes.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				serviceAttributes.stream().forEach(attribute -> {
					vwServiceAttributes.add(mapper.convertValue(attribute, VwServiceAttributesBean.class));
				});
				Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped = vwServiceAttributes.stream()
						.collect(Collectors.groupingBy(VwServiceAttributesBean::getSysId));
				Map<String, List<SdwanEndpoints>> instanceByCode = ciscoServiceInventoryUtils.getSdwanEnpointsByCode
						(serviceAttrGrouped, sdwanEndPointsForRegions);
				CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);

				if (Objects.nonNull(ciscoBulkcpeStatusResponse)
						&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {

					List<String> cpeName = new ArrayList<>();

					 data.stream().forEach(cpeData -> {

						 LOGGER.info("Inside SDWAN CPE listing  CPE data and cpe name {} ", cpeData.get("cpe_name"));
						 Map<String, String> links = new HashMap<>();
						 SdwanCpeDetails sdwanCpeDetails = new SdwanCpeDetails();
						 sdwanCpeDetails.setCpeName((String) cpeData.get("cpe_name"));

						 izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(ciscoBulkcpeStatusResponse, sdwanCpeDetails, map,
								 new SdwanSiteDetails(), instanceByCode, links);

						if((map.get(ServiceInventoryConstants.CPE_STATUS)).equalsIgnoreCase("ONLINE")){

							cpeName.add((String) cpeData.get("cpe_name"));
						}

					 });


						 CiscoApplicationByDeviceIdResponse ciscoAppByDeviceIdResp=
						 izoSdwanCiscoService.getApplicationDetailsFromCisco(instanceByCode,ciscoBulkcpeStatusResponse,cpeName.get(0).toString());



						ciscoAppByDeviceIdResp.getCiscoAppDetails().stream().forEach(appDetail -> {
							CiscoApplicationsDetailBean CiscoAppDetailBean = new CiscoApplicationsDetailBean();
							CiscoAppDetailBean.setApplication(appDetail.getApplication());
							CiscoAppDetailBean.setAppLongName(appDetail.getAppLongName());
							CiscoAppDetailBean.setApplicationType("Built-In");
							//CiscoAppDetailBean.setFamily(appDetail.getFamily());
							//CiscoAppDetailBean.setFamilyLongName(appDetail.getFamilyLongName());
							//CiscoAppDetailBean.setLastupdated(appDetail.getLastupdated());
							///CiscoAppDetailBean.setVdeviceDataKey(appDetail.getVdeviceDataKey());
							CiscoAppDetailBean.setVdeviceHostName(appDetail.getVdeviceHostName());
							//CiscoAppDetailBean.setVdeviceName(appDetail.getVdeviceName());
							versaAppls.add(CiscoAppDetailBean);


							});

						 //return sdwanCPEBean;


				}

			}
		}
			} catch(Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_ERROR);
			}
			return sdwanApplications;
			
		}
	public PagedResultWithTimestamp<SiteListConfigDetails> getSdwanTemplateDetails(Integer page, Integer size,
			Integer customerId, Integer customerLeId, Integer partnerId, Integer productId) throws IOException, TclCommonException {
		LOGGER.info("Entering method to fetch template list");

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();

		isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);

		if (Objects.nonNull(customerLeId)) {
			customerIds.clear();
			customerIds.add(customerLeId);
		}
		List<Map<String, Object>> underlaySites = vwSiServiceInfoAllRepository.findUnderlaysByCustomer(customerId,
				partnerId, customerIds, partnerLeIds);
		LOGGER.info("Number of underlaySites : {}", underlaySites.size());
		List<Integer> underlaySysIds = underlaySites.stream().map(underlay -> (Integer) underlay.get("sysId"))
				.collect(Collectors.toList());
		Page<Map<String, Object>> distinctTemplates = serviceAdditionalInfoRepository.findTemplateNameBySysIdPaginated(
				underlaySysIds, ServiceInventoryConstants.SITE_LIST, PageRequest.of(page - 1, size));
		List<String> templateNames = distinctTemplates.stream().map(template -> (String) template.get("name"))
				.collect(Collectors.toList());
		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository
				.findBysiteListIdNames(templateNames, customerId, customerIds, partnerId, partnerLeIds);
		Set<Integer> overlaySysIds = underlaysOverlaysJoined.stream()
				.map(underlaysOverlays -> (Integer) underlaysOverlays.get("overlaySysId")).collect(Collectors.toSet());
		Set<String> underLayServiceIds = underlaysOverlaysJoined.stream()
				.map(underlaysOverlays -> (String) underlaysOverlays.get("underlaySrvcId")).collect(Collectors.toSet());
		List<SIServiceAssetInfo> cpeDetails = serviceAssetInfoRepository
				.findByServiceIdInAndAssetTag(new ArrayList<>(underLayServiceIds), ServiceInventoryConstants.SDWAN_CPE);
		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				new ArrayList<>(overlaySysIds), Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
						ServiceInventoryConstants.V_MANAGE_URL));
		List<VwServiceAttributesBean> vwServiceAttributeBean = new ArrayList<>();
		if (serviceAttributes != null & !serviceAttributes.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			serviceAttributes.stream().forEach(srvAttribute -> {
				vwServiceAttributeBean.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
			});
		}
		LOGGER.info("Overlay attributes for template {}", vwServiceAttributeBean.toString());
		List<SiteListConfigDetails> templateDetails = new ArrayList<>();
		Set<String> instanceRegions = new HashSet<>();
		Map<String, String> map = new HashMap<>();
		vwServiceAttributeBean.stream().filter(
				attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
				.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
		// fetch cpe information (status,technical info) according to instance region
		CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
		if (Objects.nonNull(ciscoBulkcpeStatusResponse)
				&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
			if (Objects.nonNull(underlaysOverlaysJoined) && !underlaysOverlaysJoined.isEmpty()) {
				Set<String> recurringTemplates = new HashSet<>();
				underlaysOverlaysJoined.forEach(template -> {
					
					Set<CiscoCPEBean> sdwanCpe = new HashSet<>();
					Set<AssosciatedSiteDetails> sitedetailsSet= new HashSet<>();
					cpeDetails.stream()
							.filter(cpe -> cpe.getServiceId().equalsIgnoreCase((String) template.get("underlaySrvcId")) && StringUtils.isNotBlank(cpe.getAssetName()))
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
						});
					}
				
					if (!recurringTemplates.contains(template.get("attributeValue"))) {
						//LOGGER.info("sdwanCpeDetails.size() {}", sdwanCpeDetails.size());
						SiteListConfigDetails templateDetail = new SiteListConfigDetails();
						templateDetail.setAttributeId(new ArrayList<>());
						templateDetail.getAttributeId().add((Integer) template.get("templateId"));
						templateDetail.setSiteListId((String) template.get("attributeValue"));
						sdwanCpe.stream().forEach(cpe->{
							AssosciatedSiteDetails sitedetails= new AssosciatedSiteDetails();
							cpe.getCpeUnderlaySites().stream().forEach(cpeunderlay->{
								sitedetails.setSiteName(cpeunderlay.getSiteName());
							});
							sitedetails.setSdwanSiteStaus(cpe.getSdwanSiteStatus());
							sitedetailsSet.add(sitedetails);
						});
						
						templateDetail.setSiteDetails(sitedetailsSet);
						ciscoBulkSiteListResponse.getCiscoSiteListDetails().stream()
						.filter(listdetail->listdetail.getListId().equalsIgnoreCase(templateDetail.getSiteListId()))
						.forEach(siteListRes->{
							templateDetail.setSiteListName(siteListRes.getName());
							
						});
						templateDetail.setSiteListAlias((String) template.get("dispVal"));
						templateDetail.setSdwanServiceId(new HashSet<>());
						if (Objects.nonNull(template.get("overlaySrvcId")))
							templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
						templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
						templateDetails.add(templateDetail);
						recurringTemplates.add(templateDetail.getSiteListId());
					} else {
						//LOGGER.info("sdwanCpeDetails.size() {}", sdwanCpeDetails.size());
						templateDetails.stream().filter(templateDetail -> ((String) template.get("attributeValue"))
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
		} else
			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		PagedResultWithTimestamp<SiteListConfigDetails> templateInfoPagedResult = new PagedResultWithTimestamp<SiteListConfigDetails>(
				templateDetails, distinctTemplates.getTotalElements(), distinctTemplates.getTotalPages(),
				ServiceInventoryUtils.dateConvertor(new Date()));
		return templateInfoPagedResult;
	}
	public PagedResultWithTimestamp<SiteListConfigDetails> searchBasedOnTemplateNames(
			SiteDetailsSearchRequest request) throws TclCommonException {
		LOGGER.info("Entering Template search and sort MDC token {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		List<SiteListConfigDetails> templateDetailsList = new ArrayList<>();
		Long totalItems = null ;
		Integer totalPages = null;
		if(request.getProductId()==null || request.getPage()==null || request.getSize()<=0 ) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
     }
     try {
			LOGGER.info("Template search and sort request {}", request.toString());
			List<Integer> underlaySysId = new ArrayList<>();
			isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerIds, partnerLeIds, request.getCustomerLeId());
			LOGGER.info("SDWAN searchBasedOnTemplateNames Request {} ",request);
			if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
				Page<Map<String, Object>> uniqueTemplateNames;
				String sortDirection = "asc";
				String sortColumn ="";
				if (StringUtils.isBlank(request.getSearchText()) && request.getSearchText() != "NULL") {
					request.setSearchText("");
				}
				if (ServiceInventoryConstants.ALIAS.equalsIgnoreCase(request.getSortBy()) && request.getSortBy() != "NULL") {
					sortColumn = "disp_val";
				}
				else sortColumn = "attribute_value";
				List<Map<String, Object>> underlaySites = vwSiServiceInfoAllRepository.findUnderlaysByCustomer(request.getCustomerId(),
						request.getPartnerId(), customerIds, partnerLeIds);
				LOGGER.info("Number of underlaySites : {}", underlaySites.size());
				List<Integer> underlaySysIds = underlaySites.stream().map(underlay -> (Integer) underlay.get("sysId"))
						.collect(Collectors.toList());
				if ("desc".equalsIgnoreCase(request.getSortOrder()))
					uniqueTemplateNames = serviceAdditionalInfoRepository.findBySysIdAndSearchTextPaginated(
							underlaySysIds, ServiceInventoryConstants.SITE_LIST, request.getSearchText(),
							PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(sortColumn).descending()));
				else
					uniqueTemplateNames = serviceAdditionalInfoRepository.findBySysIdAndSearchTextPaginated(
							underlaySysIds, ServiceInventoryConstants.SITE_LIST, request.getSearchText(),
							PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(sortColumn).ascending()));
				totalItems = uniqueTemplateNames.getTotalElements();
				totalPages = (int)(totalItems % request.getSize()>0? (totalItems/request.getSize())+1 : totalItems/request.getSize());
				if (Objects.nonNull(uniqueTemplateNames) && !uniqueTemplateNames.getContent().isEmpty()) {
					List<String> templateNames = uniqueTemplateNames.stream().map(template -> (String) template.get("name"))
							.collect(Collectors.toList());
					List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository
							.findBysiteListIdNames(templateNames, request.getCustomerId(), customerIds, request.getPartnerId(), partnerLeIds);
					Set<Integer> overlaySysIds = underlaysOverlaysJoined.stream()
							.map(underlaysOverlays -> (Integer) underlaysOverlays.get("overlaySysId")).collect(Collectors.toSet());
					Set<String> underLayServiceIds = underlaysOverlaysJoined.stream()
							.filter(underlaysOverlays -> Objects.nonNull(underlaysOverlays)
									&& Objects.nonNull(underlaysOverlays.get("underlaySrvcId")))
							.map(underlaysOverlays -> (String) underlaysOverlays.get("underlaySrvcId"))
							.collect(Collectors.toSet());
					List<SIServiceAssetInfo> cpeDetails = serviceAssetInfoRepository
							.findByServiceIdInAndAssetTag(new ArrayList<>(underLayServiceIds), ServiceInventoryConstants.SDWAN_CPE);

//							List<Map<String, Object>> underlayAssets = siServiceInfoRepository.getAssetsBySysIdInAndAssetName(underlaySysId, "SDWAN CPE");
					List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository
							.findServiceAttributesByIdsIn(new ArrayList<>(overlaySysIds),
									Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
											ServiceInventoryConstants.V_MANAGE_URL));
					List<VwServiceAttributesBean> vwServiceAttributeBean = new ArrayList<>();
					if (serviceAttributes != null & !serviceAttributes.isEmpty()) {
						ObjectMapper mapper = new ObjectMapper();
						serviceAttributes.stream().forEach(srvAttribute -> {
							vwServiceAttributeBean
									.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
						});
					}
					constructTemplateDetails(templateDetailsList, vwServiceAttributeBean, underlaysOverlaysJoined,
							cpeDetails, underlaySites);
					if (ServiceInventoryConstants.ALIAS.equalsIgnoreCase(request.getSortBy())) {
						if ("desc".equalsIgnoreCase(request.getSortOrder())) {
							templateDetailsList.sort(Comparator.comparing(SiteListConfigDetails::getSiteListAlias,
									Comparator.nullsLast(Comparator.reverseOrder())));
						} else {
							templateDetailsList.sort(Comparator.comparing(SiteListConfigDetails::getSiteListAlias,
									Comparator.nullsLast(Comparator.naturalOrder())));
						}
					} else {
						if ("desc".equalsIgnoreCase(request.getSortOrder())) {
							templateDetailsList.sort(Comparator.comparing(SiteListConfigDetails::getSiteListName,
									Comparator.nullsLast(Comparator.reverseOrder())));
						} else {
							templateDetailsList.sort(Comparator.comparing(SiteListConfigDetails::getSiteListName,
									Comparator.nullsLast(Comparator.naturalOrder())));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		return new PagedResultWithTimestamp<>(templateDetailsList, totalItems, totalPages,
				ServiceInventoryUtils.dateConvertor(new Date()));
	}

	private void constructTemplateDetails(List<SiteListConfigDetails> templateDetails,
			List<VwServiceAttributesBean> vwServiceAttributeBean, List<Map<String, Object>> underlaysOverlaysJoined,
			List<SIServiceAssetInfo> cpeDetails, List<Map<String, Object>> underlaySites)
			throws TclCommonException, IOException {
		Map<String, String> map = new HashMap<>();
		Set<String> instanceRegions = new HashSet<>();
		vwServiceAttributeBean.stream().filter(
				attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.V_MANAGE_URL))
				.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		CiscoBulkSiteListResponse ciscoBulkSiteListResponse = izoSdwanCiscoService.getsiteListDetailsFromCisco(instanceByCode);
		CiscoBulkCpeResponse ciscoBulkcpeStatusResponse = izoSdwanCiscoService.getCpeStatusDetailsFromCisco(instanceByCode);
		if (Objects.nonNull(ciscoBulkcpeStatusResponse)
				&& Objects.nonNull(ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails())) {
				
			if (Objects.nonNull(underlaysOverlaysJoined) && !underlaysOverlaysJoined.isEmpty()) {
				Set<String> recurringTemplates = new HashSet<>();
				underlaysOverlaysJoined.forEach(template -> {
					
					Set<CiscoCPEBean> sdwanCpe = new HashSet<>();
					Set<AssosciatedSiteDetails> sitedetailsSet= new HashSet<>();
					cpeDetails.stream()
							.filter(cpe -> cpe.getServiceId().equalsIgnoreCase((String) template.get("underlaySrvcId")) && StringUtils.isNotBlank(cpe.getAssetName()))
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
						});
					}
				
					if (!recurringTemplates.contains(template.get("attributeValue"))) {
						//LOGGER.info("sdwanCpeDetails.size() {}", sdwanCpeDetails.size());
						SiteListConfigDetails templateDetail = new SiteListConfigDetails();
						templateDetail.setAttributeId(new ArrayList<>());
						templateDetail.getAttributeId().add((Integer) template.get("templateId"));
						templateDetail.setSiteListId((String) template.get("attributeValue"));
						sdwanCpe.stream().forEach(cpe->{
							AssosciatedSiteDetails sitedetails= new AssosciatedSiteDetails();
							cpe.getCpeUnderlaySites().stream().forEach(cpeunderlay->{
								sitedetails.setSiteName(cpeunderlay.getSiteName());
							});
							sitedetails.setSdwanSiteStaus(cpe.getSdwanSiteStatus());
							sitedetailsSet.add(sitedetails);
						});
						
						templateDetail.setSiteDetails(sitedetailsSet);
						ciscoBulkSiteListResponse.getCiscoSiteListDetails().stream()
						.filter(listdetail->listdetail.getListId().equalsIgnoreCase(templateDetail.getSiteListId()))
						.forEach(siteListRes->{
							templateDetail.setSiteListName(siteListRes.getName());
							
						});
						templateDetail.setSiteListAlias((String) template.get("dispVal"));
						templateDetail.setSdwanServiceId(new HashSet<>());
						if (Objects.nonNull(template.get("overlaySrvcId")))
							templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
						templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
						templateDetails.add(templateDetail);
						recurringTemplates.add(templateDetail.getSiteListId());
					} else {
						//LOGGER.info("sdwanCpeDetails.size() {}", sdwanCpeDetails.size());
						templateDetails.stream().filter(templateDetail -> ((String) template.get("attributeValue"))
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
	}
	public Set<String> getproductFlavourDetails(Integer productId,
			Integer customerId,Integer partnerId, Integer customerLeId) throws TclCommonException {
		List<Integer> customerLeIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		isoSdwanInventoryService.getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
		List<Map<String,Object>> sdWanSiteDetails =  new ArrayList<>();
		List<ViewSiServiceInfoAllBean> vwSiInfoOverlay = new ArrayList<>();
		List<Integer> sdwanSysId = new ArrayList<>();
		sdWanSiteDetails = vwSiServiceInfoAllRepository.
				findSdwanSiteDetails(customerLeIds, partnerLeIds, productId, customerId, partnerId);
		if (sdWanSiteDetails != null && !sdWanSiteDetails.isEmpty()) {
			final ObjectMapper mapper = new ObjectMapper();
			sdWanSiteDetails.stream().forEach(map -> {
				vwSiInfoOverlay.add(mapper.convertValue(map, ViewSiServiceInfoAllBean.class));
			});
		}
		vwSiInfoOverlay.stream().forEach(vwSiInfo->{
			sdwanSysId.add(vwSiInfo.getSysId());
		});
		Set<String> productFlavourdetails= new HashSet<>();
		List<SIServiceAttribute> overLayServiceAttrsFlavour = siServiceAttributeRepository
				.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
						Arrays.asList(ServiceInventoryConstants.PRODUCT_FLAVOUR
								));
		overLayServiceAttrsFlavour.stream().forEach(overlayProdFlavor->{
			productFlavourdetails.add(overlayProdFlavor.getAttributeValue().toUpperCase());
		});
		
		return  productFlavourdetails;
	
	}
}
