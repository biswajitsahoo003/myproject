package com.tcl.dias.serviceinventory.service.v1;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.beans.SdwanCpeDetails;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetails;
import com.tcl.dias.serviceinventory.constants.ExceptionConstants;
import com.tcl.dias.serviceinventory.entity.entities.SdwanEndpoints;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoApplicationByDeviceIdResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoApplicationDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPoliciesAddressListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPoliciesApplicationListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPoliciesSlaListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPolicyApplicationBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans.CiscoPolicyApplicationPayload;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoBulkCpeDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoBulkCpeResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoCpeByDeviceIdResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoCpeDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.VedgeInventoryDetail;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation.VedgeInventoryDetailsResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.AppRoutePolicyListInfo;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoAssosciatedDefintionBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoBulkSiteListResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoPolicyListBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.CiscoSiteListInstanceMapping;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.DataPolicyListInfo;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.PolicyListInfo;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.SiteListInfo;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.VpnListInfo;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.VpnListInfoNew;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.AppRoutePolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.Assembly;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.AssemblyDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyActivatedDetail;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyActivatedDetailsList;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoPolicyDetailBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.CiscoSdwanPolicyBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.DataPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.DataPolicyQos;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.SequenceEntries;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.VpnListDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans.VpnListReponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_list.AddressListView;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import javassist.runtime.Desc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service class to hold all IZOSDWAN related functionalities for Cisco
 * 
 * 
 *
 */
@Service
public class IzoSdwanCiscoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanCiscoService.class);

	
	@Value("${cisco.api.bulk.cpe.url}")
	private String cpeBulkUrl;
	@Value("${cisco.api.cpe.url}")
	private String cpeUrl;
	@Value("${cisco.api.siteList.url}")
	private String siteListUrl;
	@Value("${cisco.api.vedgeInventory.url}")
	private String vedgeInventoryUrl;
	@Value("${cisco.api.supportapplication.url}")
	private String applicationListUrl;
	
	@Value("${cisco.api.policyactivated.vsmart.url}")
	private String vsmarturl;
	@Value("${cisco.api.clienttoken.url}")
	private String clientTokenUrl;
	@Value("${cisco.api.policyactivated.vpnList.url}")
	private String vpnListUrl;
	@Value("${cisco.api.policyactivated.appPolicy.url}")
	private String appPolicyUrl;
	@Value("${cisco.api.policyactivated.dataPolicy.url}")
	private String dataPolicyUrl;
	
	@Value("${cisco.api.applicationPolicy.url}")
	private String appListPolicyUrl;
	@Value("${cisco.api.addressPolicy.url}")
	private String addressListPolicyUrl;
	@Value("${cisco.api.slaPolicyList.url}")
	private String slaListPolicyUrl;

	@Value("${cisco.api.addressPolicyPut.url}")
	private String addressListPolicyPutUrl;

	@Autowired
	RestClientService restClientService;

		/**
	 * Get CPE Status and availability for Cisco
	 * @param ciscoBulkcpeStatusResponse
	 * @param sdwanCpeDetail
	 * @param cpeStatusAndAvailablity
	 * @param sdwanSiteDetail
	 * @param instanceByCode
	 * @param links
	 */

	public void getSdwanCpeInfoFromCiscoResponse(CiscoBulkCpeResponse ciscoBulkcpeStatusResponse, SdwanCpeDetails sdwanCpeDetail,
			Map<String, String> cpeStatusAndAvailablity, SdwanSiteDetails sdwanSiteDetail,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, String> links) {
		//CiscoCpeByDeviceIdResponse CiscoCpeByDeviceIdResponse = new CiscoCpeByDeviceIdResponse();
		List<CiscoCpeDetails> ciscoCpeDetails = new ArrayList<>();
		LOGGER.info("Inside getSdwanCpeInfoFromCiscoResponse CPE {} and controllers {} ", sdwanCpeDetail.getCpeName());
		cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_STATUS, ServiceInventoryConstants.OFFLINE);
		cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_AVAILABILITY, ServiceInventoryConstants.UNAVAILABLE);
		ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails().stream().forEach(ciscocpeDetails -> {
			if (ciscocpeDetails.getHostName().equalsIgnoreCase(sdwanCpeDetail.getCpeName())) {
				if(ciscocpeDetails.getReachability().equalsIgnoreCase("reachable")) {
					cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_STATUS, ServiceInventoryConstants.ONLINE);
					cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_AVAILABILITY, ServiceInventoryConstants.AVAILABLE);
					cpeStatusAndAvailablity.put(ServiceInventoryConstants.OS_VERSION, ciscocpeDetails.getVersion());
					cpeStatusAndAvailablity.put(ServiceInventoryConstants.SKU, ciscocpeDetails.getModelSku());
					cpeStatusAndAvailablity.put("DEVICEMODEL",ciscocpeDetails.getDeviceModel());
					cpeStatusAndAvailablity.put(ServiceInventoryConstants.LAST_UPDATED_DATE,ciscocpeDetails.getLastupdated());
				}
				LOGGER.info(ciscocpeDetails.getDeviceId());
				instanceByCode.values().forEach(instances -> {
					instances.forEach(instance -> {
						String cpeDetailsBulkUrl = cpeUrl + ciscocpeDetails.getDeviceId();
						cpeDetailsBulkUrl = instance.getServerIp() + ":" + instance.getServerPort() + cpeDetailsBulkUrl;
						LOGGER.info("Cpe status Cisco URL : {}", cpeDetailsBulkUrl);
						RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(cpeDetailsBulkUrl,
								new HashMap<String, String>(), instance.getServerUsername(),
								instance.getServerPassword());
						LOGGER.info("restResponse status {} message {}", restResponse.getStatus(), restResponse.getErrorMessage());
						if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
							CiscoCpeByDeviceIdResponse ciscoCpeStatusresp = new CiscoCpeByDeviceIdResponse();
							try {
								ciscoCpeStatusresp = Utils.convertJsonToObject(restResponse.getData(),
										CiscoCpeByDeviceIdResponse.class);
								LOGGER.info("ciscoCpeStatusresp {}", ciscoCpeStatusresp);
							} catch (Exception e) {
								LOGGER.info("Error occurred while fetching cisco cpe status {}", e.getMessage());
							}
							if (Objects.nonNull(ciscoCpeStatusresp.getCiscoCpeDetails())) {
								ciscoCpeDetails.addAll(ciscoCpeStatusresp.getCiscoCpeDetails().stream()
										.filter(sdwanSiteDetails -> "0"
												.equalsIgnoreCase(sdwanSiteDetails.getVpnid()))
										.collect(Collectors.toList()));
							}
						}
					});
				});
			}
		});
		LOGGER.info("ciscoCpeDetails::: {}", ciscoCpeDetails.size());
		ciscoCpeDetails.stream().forEach(linkedetails->{
			links.put(linkedetails.getIfname(),linkedetails.getIfoperstatus());
		});

	}
	/**
	 * Get the Bulk CPE Details and it's availability by calling CISCO
	 * @param instanceByCode
	 * @return
	 * @throws TclCommonException
	 */

	public CiscoBulkCpeResponse getCpeStatusDetailsFromCisco(Map<String, List<SdwanEndpoints>> instanceByCode)
			throws TclCommonException {

		CiscoBulkCpeResponse ciscoCpeStatus = new CiscoBulkCpeResponse();
		try {
			List<CiscoBulkCpeDetails> appliances = new ArrayList<>();
			instanceByCode.values().forEach(instances -> {
				instances.forEach(instance -> {
					String cpeDetailsBulkUrl = cpeBulkUrl;
					cpeDetailsBulkUrl = instance.getServerIp() + ":" + instance.getServerPort() + cpeBulkUrl;
					LOGGER.info("Cpe status cisco URL : {}", cpeDetailsBulkUrl);
					RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(cpeDetailsBulkUrl,
							new HashMap<String, String>(), instance.getServerUsername(), instance.getServerPassword());
					LOGGER.info("restResponse status {} message {}", restResponse.getStatus(), restResponse.getErrorMessage());
				
					if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
						CiscoBulkCpeResponse ciscoCpeStatusresp = new CiscoBulkCpeResponse();
						try {
							ciscoCpeStatusresp = Utils.convertJsonToObject(restResponse.getData(),
									CiscoBulkCpeResponse.class);
							LOGGER.info("ciscoCpeStatusresp {}", ciscoCpeStatusresp);
						} catch (Exception e) {
							LOGGER.info("Error occurred while fetching cisco cpe status {}", e.getMessage());
						}
						if (Objects.nonNull(ciscoCpeStatusresp.getCiscoBulkCpeDetails())) {
							appliances.addAll(ciscoCpeStatusresp.getCiscoBulkCpeDetails());
						}

					}
				});
				ciscoCpeStatus.setCiscoBulkCpeDetails(new ArrayList<>());
				ciscoCpeStatus.setCiscoBulkCpeDetails(appliances);
			});
			LOGGER.info("Entire CPE list after fetching from multiple endpoints {}",
					Utils.convertObjectToJson(ciscoCpeStatus));
		} catch (Exception e) {

			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		}
		return ciscoCpeStatus;
	}
	public CiscoBulkSiteListResponse getsiteListDetailsFromCisco(Map<String, List<SdwanEndpoints>> instanceByCode)
			throws TclCommonException {


		CiscoBulkSiteListResponse ciscoCpeStatus = new CiscoBulkSiteListResponse();
		try {
			List<CiscoSiteListDetails> appliances = new ArrayList<>();
			instanceByCode.values().forEach(instances -> {
				instances.forEach(instance -> {
					String cpeDetailsBulkUrl = siteListUrl;
					cpeDetailsBulkUrl = instance.getServerIp() + ":" + instance.getServerPort() + siteListUrl;
					LOGGER.info("Cpe status cisco URL : {}", cpeDetailsBulkUrl);
					RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(cpeDetailsBulkUrl,
							new HashMap<String, String>(), instance.getServerUsername(), instance.getServerPassword());
					LOGGER.info("restResponse status {} message {}", restResponse.getStatus(), restResponse.getErrorMessage());
				
					if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
						CiscoBulkSiteListResponse ciscoCpeStatusresp = new CiscoBulkSiteListResponse();
						try {
							ciscoCpeStatusresp = Utils.convertJsonToObject(restResponse.getData(),
									CiscoBulkSiteListResponse.class);
							LOGGER.info("ciscoCpeStatusresp {}", ciscoCpeStatusresp);
						} catch (Exception e) {
							LOGGER.info("Error occurred while fetching cisco cpe status {}", e.getMessage());
						}
						if (Objects.nonNull(ciscoCpeStatusresp.getCiscoSiteListDetails())) {
							appliances.addAll(ciscoCpeStatusresp.getCiscoSiteListDetails());
						}

					}
				});
				ciscoCpeStatus.setCiscoSiteListDetails(new ArrayList<>());
				ciscoCpeStatus.setCiscoSiteListDetails(appliances);
			});
			LOGGER.info("Entire CPE list after fetching from multiple endpoints {}",
					Utils.convertObjectToJson(ciscoCpeStatus));
		} catch (Exception e) {

			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		}
		return ciscoCpeStatus;
	
	}
	public VedgeInventoryDetailsResponse getSerialNumberFromCisco(Map<String, List<SdwanEndpoints>> instanceByCode) 
		throws TclCommonException {

			VedgeInventoryDetailsResponse vedgeInventoryDetailsResponse = new VedgeInventoryDetailsResponse();
			try {
				List<VedgeInventoryDetail> vedgeInventoryDetail = new ArrayList<>();
				instanceByCode.values().forEach(instances -> {
					instances.forEach(instance -> {
						String cpeDetailsBulkUrl = vedgeInventoryUrl;
						cpeDetailsBulkUrl = instance.getServerIp() + ":" + instance.getServerPort() + vedgeInventoryUrl;
						LOGGER.info("Cpe status cisco URL : {}", cpeDetailsBulkUrl);
						RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(cpeDetailsBulkUrl,
								new HashMap<String, String>(), instance.getServerUsername(), instance.getServerPassword());
						LOGGER.info("restResponse status {} message {}", restResponse.getStatus(), restResponse.getErrorMessage());
					
						if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
							VedgeInventoryDetailsResponse vedgeInventoryDetailsResp = new VedgeInventoryDetailsResponse();
							try {
								vedgeInventoryDetailsResp = Utils.convertJsonToObject(restResponse.getData(),
										VedgeInventoryDetailsResponse.class);
								LOGGER.info("ciscoCpeStatusresp {}", vedgeInventoryDetailsResp);
							} catch (Exception e) {
								LOGGER.info("Error occurred while fetching cisco cpe status {}", e.getMessage());
							}
							if (Objects.nonNull(vedgeInventoryDetailsResp.getVedgeInventoryDetailList())) {
								vedgeInventoryDetail.addAll(vedgeInventoryDetailsResp.getVedgeInventoryDetailList());
							}

						}
					});
					vedgeInventoryDetailsResponse.setVedgeInventoryDetailList(new ArrayList<>());
					vedgeInventoryDetailsResponse.setVedgeInventoryDetailList(vedgeInventoryDetail);
				});
				
			} catch (Exception e) {

				throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
			}
			return vedgeInventoryDetailsResponse;

	}
	public CiscoApplicationByDeviceIdResponse getApplicationDetailsFromCisco(
			Map<String, List<SdwanEndpoints>> instanceByCode, CiscoBulkCpeResponse ciscoBulkcpeStatusResponse, String cpeName) {
		LOGGER.info("cpeName{}",cpeName);
		List<String> deviceId=new ArrayList<>();
//		ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails().stream().forEach(ciscocpeDetails -> {
//			if(ciscocpeDetails.getHostName().equalsIgnoreCase("vEdge-100b-1")) {
//				deviceId.add(ciscocpeDetails.getDeviceId());
//			}
//		});
		
		ciscoBulkcpeStatusResponse.getCiscoBulkCpeDetails().stream().filter(
				ciscocpeDetails -> ciscocpeDetails.getHostName().equalsIgnoreCase(cpeName))
				.forEach(cpeDet -> deviceId.add(cpeDet.getDeviceId()));
		CiscoApplicationByDeviceIdResponse CiscoAppDeviceIdResp= new CiscoApplicationByDeviceIdResponse();
		List<CiscoApplicationDetails> ciscoAppList= new ArrayList<>();
		instanceByCode.values().forEach(instances -> {
			instances.forEach(instance -> {
				String cpeDetailsBulkUrl = applicationListUrl +deviceId.get(0);
				cpeDetailsBulkUrl = instance.getServerIp() + ":" + instance.getServerPort() + cpeDetailsBulkUrl;
				LOGGER.info("Cpe status Cisco URL : {}", cpeDetailsBulkUrl);
				RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(cpeDetailsBulkUrl,
						new HashMap<String, String>(), instance.getServerUsername(),
						instance.getServerPassword());
				LOGGER.info("restResponse status {} message {}", restResponse.getStatus(), restResponse.getErrorMessage());
				if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
					CiscoApplicationByDeviceIdResponse ciscoCpeStatusresp = new CiscoApplicationByDeviceIdResponse();
					try {
						ciscoCpeStatusresp = Utils.convertJsonToObject(restResponse.getData(),
								CiscoApplicationByDeviceIdResponse.class);
						LOGGER.info("ciscoCpeStatusresp {}", ciscoCpeStatusresp);
					} catch (Exception e) {
						LOGGER.info("Error occurred while fetching cisco cpe status {}", e.getMessage());
					}
					if (Objects.nonNull(ciscoCpeStatusresp.getCiscoAppDetails())) {
						ciscoAppList.addAll(ciscoCpeStatusresp.getCiscoAppDetails());
					}
					
				}
			});
			CiscoAppDeviceIdResp.setCiscoAppDetails(new ArrayList<>());
			CiscoAppDeviceIdResp.setCiscoAppDetails(ciscoAppList);
		});
		return CiscoAppDeviceIdResp;
	}
	@SuppressWarnings("unchecked")
	public CiscoPolicyActivatedDetailsList getPolicyDetailsByPolicyActivated(Set<CiscoSiteListInstanceMapping> ciscoSiteListInstanceMappings, CiscoSiteListDetailBean ciscoSiteListDetail,
			List<CiscoSdwanPolicyBean> ciscoSdwanPolicyList, Map<String, List<SdwanEndpoints>> instanceByCode, 
			HttpServletRequest request, CiscoBulkSiteListResponse ciscoBulkSiteListResponse) throws TclCommonException {
		LOGGER.info(" Inside getPolicyDetailsByPolicyActivated Details");
		CiscoPolicyActivatedDetailsList ciscoPolicyActivatedBean = new CiscoPolicyActivatedDetailsList();
		try {
			List<CiscoPolicyActivatedDetail> ciscoActivatedPolicyList = new ArrayList<>();
			List<AssemblyDetails> assemblydetails= new ArrayList<>();	
			List<CiscoPolicyListBean> ciscoPolicyListBean = new ArrayList<>();
			List<VpnListInfo> vpnListInfoList=new ArrayList<>();
			List<VpnListInfoNew> vpnListInfoListNewApp=new ArrayList<>();
			List<VpnListInfoNew> vpnListInfoListNewData=new ArrayList<>();
			AppRoutePolicyListInfo appPolicyListInfo= new AppRoutePolicyListInfo();
			DataPolicyListInfo dataPolicyListInfo= new DataPolicyListInfo();
			ciscoSiteListInstanceMappings.stream().forEach(ciscoSiteListInstanceMapping->{
			instanceByCode.values().forEach(instances -> {
				instances.forEach(instance -> {
					String vsmartPespUrl = vsmarturl;
					String vpnRespUrl = vpnListUrl;
					String appPolicyRespUrl = appPolicyUrl;
					String dataPolicyRespUrl =dataPolicyUrl;
					vsmartPespUrl = instance.getServerIp() + ":" + instance.getServerPort() + vsmarturl;
					vpnRespUrl=instance.getServerIp() + ":" + instance.getServerPort() + vpnListUrl;
					appPolicyRespUrl=instance.getServerIp() + ":" + instance.getServerPort() + appPolicyUrl;
					dataPolicyRespUrl=instance.getServerIp() + ":" + instance.getServerPort() + dataPolicyUrl;
					ResponseEntity<String> loginHeader = connectCiscoClient(request, instance);
					Cookie cooki = getCookiesFromHeader(loginHeader);
					HttpHeaders headers = new HttpHeaders();
					headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
					headers.add("x-xsrf-token",loginHeader.getBody());
					
					LOGGER.info("vsmartPespUrl cisco URL : {}", vsmartPespUrl);
					ResponseEntity<String> ciscoActivatedPolicyResp = restClientService.getAndReturnHeadersWithProxyBasicAuthentication(vsmartPespUrl,
							headers.toSingleValueMap(), instance.getServerUsername(), instance.getServerPassword());
					CiscoPolicyActivatedDetailsList activatedPolicy = new CiscoPolicyActivatedDetailsList();
						try {
							activatedPolicy = Utils.convertJsonToObject(ciscoActivatedPolicyResp.getBody(),
									CiscoPolicyActivatedDetailsList.class);
							LOGGER.info("activatedPolicy {}", activatedPolicy);
						} catch (Exception e) {
							LOGGER.info("Error occurred while fetching cisco cpe status {}", e.getMessage());
						}	
						
						if (Objects.nonNull(activatedPolicy.getCiscoPolicyActivatedDetails())) {
							activatedPolicy.getCiscoPolicyActivatedDetails().parallelStream().
							filter(policy->policy.isPolicyActivated()).
							forEach(ciscoPolicyDet->{
								ciscoActivatedPolicyList.add(ciscoPolicyDet);
								try {
									ciscoPolicyDet.setAssembly(Utils.convertJsonToObject(ciscoPolicyDet.getPolicyDefinition(),Assembly.class));
									assemblydetails.addAll(ciscoPolicyDet.getAssembly().getAssemblyDetails());
								} catch (TclCommonException e) {
									LOGGER.info("Error occurred while fetching cisco activatedPolicy details {}", e.getMessage());
								}
								});
						}
						

				String vpnUrl=	vpnRespUrl;
				String appRouteUrl=appPolicyRespUrl;
				String dataUrl=dataPolicyRespUrl;
				ciscoPolicyActivatedBean.setCiscoPolicyActivatedDetails(new ArrayList<>());
				ciscoPolicyActivatedBean.setCiscoPolicyActivatedDetails(ciscoActivatedPolicyList);
				CiscoAssosciatedDefintionBean associatedDefinitionDetails=new CiscoAssosciatedDefintionBean();
				CiscoPolicyListBean policyBean = new CiscoPolicyListBean();
				
				VpnListInfo vpnListInfo = new VpnListInfo();
				
				
				assemblydetails.stream().forEach(assembly->{
					assembly.getEntries().stream().forEach(entries->{
						//if(entries.getSiteLists().contains("c4c6e75a-b5c9-4566-be83-8252996d3cf6")) {
						if(entries.getSiteLists().contains(ciscoSiteListDetail.getSiteListId())) {
							associatedDefinitionDetails.setDefinitionId(assembly.getDefinitionId());
							policyBean.setPolicyType(assembly.getType());
							if(Objects.nonNull(entries.getVpnLists())){
						List<SiteListInfo> siteListInfoList=new ArrayList<>();
							entries.getVpnLists().stream().forEach(vpnList->{
								
								entries.getSiteLists().stream().forEach(siteList->{
									SiteListInfo siteListInfo= new SiteListInfo();
									siteListInfo.setSiteListId(siteList);
									ciscoBulkSiteListResponse.getCiscoSiteListDetails().parallelStream()
									.filter(listdetail->listdetail.getListId().equalsIgnoreCase(siteListInfo.getSiteListId()))
									.forEach(siteListRes->{
										siteListInfo.setSiteListName(siteListRes.getName());
										
									});
									siteListInfoList.add(siteListInfo);
									associatedDefinitionDetails.setSiteListInfo(siteListInfoList);
								});
								VpnListInfoNew vpnListInfoNew = new VpnListInfoNew();
								vpnListInfo.setVpnListId(vpnList);
								vpnListInfoNew.setVpnListId(vpnList);
								RestResponse restResponse = restClientService
										.getWithProxyBasicAuthentication(vpnUrl.replaceAll("DYNAMICVPNID", vpnList),
										new HashMap<String, String>(), instance.getServerUsername(),
										instance.getServerPassword());
								List<Map<Object,Object>> vpnListDetails= new ArrayList<>();
								if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
									
									try {
										
										 vpnListDetails = Utils.convertJsonToObject(restResponse.getData(),
												List.class);
										LOGGER.info("vpnListRsponse{}",vpnListDetails);
										vpnListDetails.stream().forEach(
												vpn-> vpnListInfoNew.setVpnName(vpn.get("name").toString())
												);
									} catch (Exception e) {
										LOGGER.info("Error occurred while fetching cisco cpe status {}", e.getMessage());
									}
								}
								
								vpnListInfoNew.setSiteListName(siteListInfoList);
//								ciscoSiteListDetail.setCiscoPoliciesList(new ArrayList<>());
								policyBean.setAssociatedDefinitionDetails(associatedDefinitionDetails);
								//vpnListInfoNew.setPolicyListInfo(new ArrayList<>());
								if(policyBean.getPolicyType().equalsIgnoreCase("appRoute")) {
									ResponseEntity<String> appRouteResp = restClientService
											.getAndReturnHeadersWithProxyBasicAuthentication(appRouteUrl.replaceAll("DYNAMICAPPROUTEDEFID", assembly.getDefinitionId()),
											headers.toSingleValueMap(), instance.getServerUsername(), instance.getServerPassword());
									AppRoutePolicy appPolicy=new AppRoutePolicy();
									try {
										 appPolicy=Utils.convertJsonToObject(appRouteResp.getBody(), AppRoutePolicy.class);
										LOGGER.info("appPolicy{}",appPolicy);
									} catch (TclCommonException e) {
										LOGGER.info("Error in getting app policy details");
									}
									
									LOGGER.info("appResponse{}",appRouteResp);
									
									List<CiscoPolicyListBean> policyBeanList = new ArrayList<>();
									List<PolicyListInfo> policyListInfoList= new ArrayList<>();
									vpnListInfoNew.setPolicyListInfo(new ArrayList<>());
									appPolicy.getSequences().stream().forEach(seq->{
										CiscoPolicyListBean policyNew= new CiscoPolicyListBean();
										PolicyListInfo policyListInfo= new PolicyListInfo();
											policyNew.setPolicyName(seq.getSequenceId()+"_"+seq.getSequenceName());
											policyNew.setPolicyType(policyBean.getPolicyType());
										
											policyListInfo.setPolicyName(seq.getSequenceId()+"_"+seq.getSequenceName());
											policyListInfo.setPolicyType(policyBean.getPolicyType());
											policyListInfo.setDirectorRegion(ciscoSiteListInstanceMapping.getInstanceRegion());
											policyListInfoList.add(policyListInfo);
											
											policyNew.setAssociatedDefinitionDetails(policyBean.getAssociatedDefinitionDetails());
											policyBeanList.add(policyNew);
									
									});
									vpnListInfoNew.getPolicyListInfo().addAll(policyListInfoList);
									vpnListInfoNew.setDefinitionId(assembly.getDefinitionId());
									ciscoPolicyListBean.addAll(policyBeanList);
									vpnListInfoListNewApp.add(vpnListInfoNew);
									appPolicyListInfo.setVpnListInfoListNew(vpnListInfoListNewApp);
									appPolicyListInfo.setDirectorRegion(ciscoSiteListInstanceMapping.getInstanceRegion());
									
									
									
								}
								else  if(policyBean.getPolicyType().equalsIgnoreCase("data")) {
									ResponseEntity<String> dataResp = restClientService
											.getAndReturnHeadersWithProxyBasicAuthentication(dataUrl.replaceAll("DYNAMICDATADEFID", assembly.getDefinitionId()),
											headers.toSingleValueMap(), instance.getServerUsername(), instance.getServerPassword());
									DataPolicy dataPolicy=new DataPolicy();
									try {
										dataPolicy=Utils.convertJsonToObject(dataResp.getBody(), DataPolicy.class);
										LOGGER.info("dataPolicy{}",dataPolicy);
									} catch (TclCommonException e) {
										LOGGER.info("Error in getting data Policy Details");
									}
									
									LOGGER.info("dataResp{}",dataResp);
									List<CiscoPolicyListBean> policyBeanList = new ArrayList<>();
									List<PolicyListInfo> policyListInfoList= new ArrayList<>();
									vpnListInfoNew.setPolicyListInfo(new ArrayList<>());
									dataPolicy.getSequences().stream().forEach(seq->{
										CiscoPolicyListBean policyNew= new CiscoPolicyListBean();
										PolicyListInfo policyListInfo= new PolicyListInfo();
											policyNew.setPolicyName(seq.getSequenceId()+"_"+seq.getSequenceName());
											policyNew.setPolicyType(policyBean.getPolicyType());
											policyListInfo.setPolicyName(seq.getSequenceId()+"_"+seq.getSequenceName());
											policyListInfo.setPolicyType(policyBean.getPolicyType());
											policyListInfo.setDirectorRegion(ciscoSiteListInstanceMapping.getInstanceRegion());
											policyListInfoList.add(policyListInfo);
											policyNew.setAssociatedDefinitionDetails(policyBean.getAssociatedDefinitionDetails());
											policyBeanList.add(policyNew);
									
									});
									ciscoPolicyListBean.addAll(policyBeanList);
									vpnListInfoNew.getPolicyListInfo().addAll(policyListInfoList);
									vpnListInfoNew.setDefinitionId(assembly.getDefinitionId());
									vpnListInfoListNewData.add(vpnListInfoNew);
									dataPolicyListInfo.setVpnListInfoListNew(vpnListInfoListNewData);
									
								}
						
										
								
								vpnListInfoList.add(vpnListInfo);
								associatedDefinitionDetails.setVpnListInfo(vpnListInfoList);
							});
						}
							
						}
						
					});
				});
				
				
				
				//ciscoSiteListDetail.setCiscoPoliciesList(ciscoPolicyListBean);
				ciscoSiteListDetail.setAppPolicies(appPolicyListInfo);
				ciscoSiteListDetail.setDataPolicies(dataPolicyListInfo);
				
				});
				
				
			});
			});
			
		} catch (Exception e) {

			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		}
		return ciscoPolicyActivatedBean;

		
		
	}
	private Cookie getCookiesFromHeader(ResponseEntity<String> loginHeader) {
		LOGGER.info("Inside getCookiesFromHeader for the header value {}", loginHeader);
		List<String> setCookies = new ArrayList<>();
		Optional<Map.Entry<String, List<String>>> setCookie = loginHeader.getHeaders().entrySet().stream()
				.filter(entry -> entry.getKey().equalsIgnoreCase("Set-Cookie")).findFirst();
		if (setCookie.isPresent()) {
			setCookies = setCookie.get().getValue();
		}
		Cookie cooki = null;
		for (String cookies : setCookies) {
			String[] cookieSpliter = cookies.split(CommonConstants.SEMI_COMMA);
			int i = 0;
			for (String cookie : cookieSpliter) {
				String[] splitter = cookie.split(CommonConstants.EQUAL);
				String key = splitter[0].trim();
				String value = null;

				if (splitter.length == 2) {
					value = splitter[1].trim();
				} else {
					break;
				}
				if (i == 0) {
					cooki = new Cookie(key, value);
					cooki.setMaxAge(60 * 60 * 24);
					cooki.setHttpOnly(true);
					cooki.setSecure(true);
					cooki.setVersion(1);
					i++;
				}
				if (key.equals("Path")) {
					cooki.setPath(value);
				}

			}
		}
		return cooki;

	}
	private ResponseEntity<String> connectCiscoClient(HttpServletRequest request, SdwanEndpoints instance) {
			String CiscoClientTokenUrl = instance.getServerIp()+":"+instance.getServerPort()+clientTokenUrl;
			HttpHeaders httpHeaders = new HttpHeaders();
			if (Objects.nonNull(request.getCookies()) && Objects.nonNull(request.getCookies()[0]))
				httpHeaders.add(HttpHeaders.COOKIE,
						request.getCookies()[0].getName() + "=" + request.getCookies()[0].getValue());
			
		ResponseEntity<String> response= restClientService.getAndReturnHeadersWithProxyBasicAuthentication(CiscoClientTokenUrl, httpHeaders.toSingleValueMap(),instance.getServerUsername(),instance.getServerPassword());
			return response ;
		}
	
	public List<AppRoutePolicy> getTrafficSteeringRuleForSiteListOverAll(String definitionId,
			SdwanEndpoints endpoint, HttpServletRequest request) throws TclCommonException{
		List<AppRoutePolicy> appRoutePolicyList= new ArrayList<>();
		String appPolicyRespUrl = appPolicyUrl;
		appPolicyRespUrl=endpoint.getServerIp() + ":" + endpoint.getServerPort() + appPolicyUrl;
		ResponseEntity<String> loginHeader = connectCiscoClient(request, endpoint);
		Cookie cooki = getCookiesFromHeader(loginHeader);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
		headers.add("x-xsrf-token",loginHeader.getBody());
		ResponseEntity<String> appRouteResp = restClientService
				.getAndReturnHeadersWithProxyBasicAuthentication(appPolicyRespUrl.replaceAll("DYNAMICAPPROUTEDEFID", definitionId),
				headers.toSingleValueMap(), endpoint.getServerUsername(), endpoint.getServerPassword());
		AppRoutePolicy appPolicy=new AppRoutePolicy();
		try {if(Objects.nonNull(appRouteResp)) {
			 appPolicy=Utils.convertJsonToObject(appRouteResp.getBody(), AppRoutePolicy.class);
			 
			 appRoutePolicyList.add(appPolicy);
		}
			LOGGER.info("appPolicy{}",appPolicy);
		} catch (TclCommonException e) {
			LOGGER.info("Error in getting app policy details");
		}
		return appRoutePolicyList;
	}
	public List<DataPolicy> getQosPoliciesForSiteListeOverAll(String definitionId, SdwanEndpoints endpoint,
			HttpServletRequest request) {
		List<DataPolicy> dataPolicyList= new ArrayList<>();
		String dataPolicyRespUrl = dataPolicyUrl;
		dataPolicyRespUrl=endpoint.getServerIp() + ":" + endpoint.getServerPort() + dataPolicyUrl;
		ResponseEntity<String> loginHeader = connectCiscoClient(request, endpoint);
		Cookie cooki = getCookiesFromHeader(loginHeader);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
		headers.add("x-xsrf-token",loginHeader.getBody());
		ResponseEntity<String> appRouteResp = restClientService
				.getAndReturnHeadersWithProxyBasicAuthentication(dataPolicyRespUrl.replaceAll("DYNAMICDATADEFID", definitionId),
						headers.toSingleValueMap(), endpoint.getServerUsername(), endpoint.getServerPassword());
		DataPolicy dataPolicy=new DataPolicy();
		try {
			if( Objects.nonNull(appRouteResp)) {
			dataPolicy=Utils.convertJsonToObject(appRouteResp.getBody(), DataPolicy.class);
			 
			 dataPolicyList.add(dataPolicy);
			}
			LOGGER.info("appPolicy{}",dataPolicy);
		} catch (TclCommonException e) {
			LOGGER.info("Error in getting app policy details");
		}
		return dataPolicyList;
	}
	public String saveTrafficSteeringByOrder(String definitionId, List<SdwanEndpoints> sdwanEndpoints,
			List<AppRoutePolicy> trafficSteeringList, HttpServletRequest request,boolean dscp,StringBuilder trafficResponse) throws TclCommonException {
		LOGGER.info("Inside saveTrafficSteeringByOrder");
		for (SdwanEndpoints endPoints : sdwanEndpoints) {
			String appPolicyRespUrl = appPolicyUrl;
			appPolicyRespUrl=endPoints.getServerIp() + ":" + endPoints.getServerPort() + appPolicyUrl;
			ResponseEntity<String> loginHeader = connectCiscoClient(request, endPoints);
			Cookie cooki = getCookiesFromHeader(loginHeader);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
			headers.add("x-xsrf-token",loginHeader.getBody());
			if(Objects.nonNull(trafficSteeringList) && !trafficSteeringList.isEmpty()) {
			LOGGER.info(Utils.convertObjectToJson(trafficSteeringList.get(0)));
			String requestBody= Utils.convertObjectToJson(trafficSteeringList.get(0));
			appPolicyRespUrl=appPolicyRespUrl.replaceAll("DYNAMICAPPROUTEDEFID", definitionId);
			try {
			RestResponse appRouteResp = restClientService
					.putWithProxyBasicAuthentication(appPolicyRespUrl, requestBody, 
							headers.toSingleValueMap(), endPoints.getServerUsername(), endPoints.getServerPassword());
			
			if (Objects.nonNull(appRouteResp) && appRouteResp.getStatus() == Status.SUCCESS && dscp) {
				trafficResponse.append("Updating ").append("dscp").append(" succeeded");
			}
			else if (Objects.nonNull(appRouteResp) && appRouteResp.getStatus() == Status.SUCCESS && Boolean.FALSE.equals(dscp) ) {
				trafficResponse.append(Constants.SUCCESS);
			}
			
			else if(appRouteResp.getStatus() == Status.ERROR && dscp) {
			trafficSteeringList.get(0).getSequences().forEach(e->{
				e.getMatch().getEntries().forEach(h->{
					if(h.getField().equalsIgnoreCase("dscp")) {
						trafficResponse.append("Updating Dscp failed");
						}
					});
				});
			}
			else if(appRouteResp.getStatus() == Status.ERROR && appRouteResp.getErrorMessage().contains("Failed to acquire lock, template or policy locked in edit mode.")
					&& Boolean.FALSE.equals(dscp)) {
				throw new TclCommonException(ExceptionConstants.CISCO_CONCURRENT_UPDATE_ERROR, ResponseResource.R_CODE_ERROR);
			}
			else {
				LOGGER.error("appRouteResp response {}",appRouteResp.getErrorMessage());
				throw new TclCommonException(appRouteResp.getErrorMessage(), R_CODE_ERROR);
			}
			}
			catch(TclCommonException e) {
				throw new TclCommonException(e, R_CODE_ERROR);
				}
			}
		}
		return trafficResponse.toString();
		
	}
	public String saveQosByOrder(String definitionId, List<SdwanEndpoints> sdwanEndpointsList,
			List<DataPolicy> qosPolicyList, HttpServletRequest request) throws TclCommonException, InterruptedException {
		String dataResponse = "";
		
		for (SdwanEndpoints endPoints : sdwanEndpointsList) {
			String dataPolicyRespUrl = dataPolicyUrl;
			dataPolicyRespUrl=endPoints.getServerIp() + ":" + endPoints.getServerPort() + dataPolicyUrl;
			ResponseEntity<String> loginHeader = connectCiscoClient(request, endPoints);
			Cookie cooki = getCookiesFromHeader(loginHeader);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
			headers.add("x-xsrf-token",loginHeader.getBody());
			if(Objects.nonNull(qosPolicyList) && !qosPolicyList.isEmpty() ) {
			LOGGER.info(Utils.convertObjectToJson(qosPolicyList.get(0)));
			String requestBody= Utils.convertObjectToJson(qosPolicyList.get(0));
			dataPolicyRespUrl=dataPolicyRespUrl.replaceAll("DYNAMICDATADEFID", definitionId);
			try {
			RestResponse dataResp = restClientService
					.putWithProxyBasicAuthentication(dataPolicyRespUrl, requestBody, 
							headers.toSingleValueMap(), endPoints.getServerUsername(), endPoints.getServerPassword());
			if (Objects.nonNull(dataResp) && dataResp.getStatus() == Status.SUCCESS) {
				dataResponse = Constants.SUCCESS;
				LOGGER.info("appRouteResp response {}",dataResp.getData());
			}
			else if(dataResp.getStatus() == Status.ERROR && dataResp.getErrorMessage().contains("Failed to acquire lock, template or policy locked in edit mode.")) {
				throw new TclCommonException(ExceptionConstants.CISCO_CONCURRENT_UPDATE_ERROR, ResponseResource.R_CODE_ERROR);
			}
			else {
				LOGGER.error("data response {}",dataResp.getErrorMessage());
				throw new TclCommonException(dataResp.getErrorMessage(), R_CODE_ERROR);
			}
			}
			catch(TclCommonException e) {
				throw new TclCommonException(e, R_CODE_ERROR);
			}
			
			}
		}
		return dataResponse;
	}
		/**
		 * Get address list for Cisco
		 *
		 * @param templateName
		 * @param organisationName
		 * @return
		 * @throws TclCommonException
		 */
		public CiscoPoliciesAddressListBean getPolicyAssosciatedAddresses(CiscoPolicyDetailBean ciscoPolicyDetailBean,
				SdwanEndpoints sdwanEndpoints) throws TclCommonException {
			String addressListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + addressListPolicyUrl;
			CiscoPoliciesAddressListBean addressListView = new CiscoPoliciesAddressListBean();
			RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(addressListUrl, new HashMap<>(),
					sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
				addressListView = Utils.convertJsonToObject(restResponse.getData(), CiscoPoliciesAddressListBean.class);
			}
			addressListView.getCiscoAdressApps().stream().forEach(adress->{
				ciscoPolicyDetailBean.getSourceAddress().stream().forEach(sourceAdress->{
					if(adress.getListId().equalsIgnoreCase(sourceAdress)) {
						adress.getEntries().stream().forEach(entry->{
							ciscoPolicyDetailBean.getSourceIp().add(entry.getIpPrefix());
						});
					}
				});
				ciscoPolicyDetailBean.getDestinationAddress().stream().forEach(destAdress->{
					if(adress.getListId().equalsIgnoreCase(destAdress)) {
						adress.getEntries().stream().forEach(entry->{
							ciscoPolicyDetailBean.getDestinationIp().add(entry.getIpPrefix());
						});
					}
				});
				adress.getEntries().stream().forEach(entry->{
					ciscoPolicyDetailBean.getAllAddresses().add(entry.getIpPrefix());
				});
				
			});
			
			
			return addressListView;
		}
		
		/**
		 * Get address list for Cisco
		 *
		 * @param templateName
		 * @param organisationName
		 * @return
		 * @throws TclCommonException
		 */
		public CiscoPoliciesApplicationListBean getAssosciatedApplications(CiscoPolicyDetailBean ciscoPolicyDetailBean,
				SdwanEndpoints sdwanEndpoints) throws TclCommonException {

			String appListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + appListPolicyUrl;
			CiscoPoliciesApplicationListBean appListView = new CiscoPoliciesApplicationListBean();
			RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(appListUrl, new HashMap<>(),
					sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
				appListView = Utils.convertJsonToObject(restResponse.getData(), CiscoPoliciesApplicationListBean.class);
			}
			appListView.getCiscoPolicyApps().stream().forEach(apps->{
				apps.getEntries().stream().forEach(entry->{
					if(entry.getApp()!=null)
					ciscoPolicyDetailBean.getAllApplications().add(entry.getApp());
				});
			});
			return appListView;
		}
		
		public CiscoPolicyApplicationBean getPolicyAssosciatedApplications(String listId, CiscoPolicyDetailBean ciscoPolicyDetailBean,
				SdwanEndpoints sdwanEndpoints) throws TclCommonException {
	
			String appListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort()+appListPolicyUrl+"/DYNAMICAPPREFID";
			appListUrl = appListUrl.replace("DYNAMICAPPREFID", listId);
			CiscoPolicyApplicationBean appView = new CiscoPolicyApplicationBean();
			RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(appListUrl, new HashMap<>(),
					sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
				appView = Utils.convertJsonToObject(restResponse.getData(), CiscoPolicyApplicationBean.class);
			}
			
			appView.getEntries().stream().forEach(entry->{
						ciscoPolicyDetailBean.getPolicyAssosciatedAppNames().add(entry.getApp());
					});
			return appView;
		}
		
		public String updatePolicyAssosciatedApplications(String listId, CiscoPolicyApplicationPayload appPayload,
				SdwanEndpoints sdwanEndpoints,StringBuilder statusBuilder) throws TclCommonException {
			String appListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort()+appListPolicyUrl+"/DYNAMICAPPREFID";
			appListUrl = appListUrl.replace("DYNAMICAPPREFID", listId);
			String requestBody= Utils.convertObjectToJson(appPayload);
			RestResponse restResponse = restClientService.putWithProxyBasicAuthentication(appListUrl,requestBody, new HashMap<>(),
					sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData()) && restResponse.getStatus() == Status.SUCCESS) {
				statusBuilder.append("Updating application succeeded");
				//.append(",");
			}
			else if(restResponse.getStatus() == Status.ERROR && restResponse.getErrorMessage().contains("Failed to acquire lock, template or policy locked in edit mode.")) {
				statusBuilder.append("Updating application failed").append(",");
				/*
				 * removing the below commented line for demo purpose, need to comment the below line once dscp and addresses are uncommneted 
				 */
				throw new TclCommonException(ExceptionConstants.CISCO_CONCURRENT_UPDATE_ERROR, ResponseResource.R_CODE_ERROR);
			}
			else {
				throw new TclCommonException(ExceptionConstants.CISCO_FAILED_TO_UPDATE_POLICY, ResponseResource.R_CODE_ERROR);
			}
			return statusBuilder.toString();
		}
		
		public CiscoPoliciesSlaListBean getSlaAssosciatedApplications(String listId, CiscoPolicyDetailBean ciscoPolicyDetailBean,
				SdwanEndpoints sdwanEndpoints) throws TclCommonException {
	
			String appListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + slaListPolicyUrl;
			appListUrl = appListUrl.replace("DYNAMICAPPREFID", listId);
			CiscoPoliciesSlaListBean appListView = new CiscoPoliciesSlaListBean();
			RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(appListUrl, new HashMap<>(),
					sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
				appListView = Utils.convertJsonToObject(restResponse.getData(), CiscoPoliciesSlaListBean.class);
			}
			appListView.getCiscoPolicySlaBean().stream().forEach(app->{
				if(app.getListId().equalsIgnoreCase(listId)) {
					app.getEntries().stream().forEach(entry->{
						ciscoPolicyDetailBean.setLatency(entry.getLatency());
						ciscoPolicyDetailBean.setJitter(entry.getJitter());
						ciscoPolicyDetailBean.setLoss(entry.getLoss());
					});
					
				}
				
			});
			return appListView;
		}
		
		/**
		 * 
		 * @param definationId
		 * @param ciscoPolicyDetailBean
		 * @param sdwanEndpoints
		 * @return AppRoutePolicyDscp
		 * @throws TclCommonException
		 * description - Get the DSCP policy details
		 * @author SGanta
		 */
		public AppRoutePolicy getPolicyAssosciatedDscp(String definationId, CiscoPolicyDetailBean ciscoPolicyDetailBean,
				SdwanEndpoints sdwanEndpoints,HttpServletRequest request) throws TclCommonException {
			AppRoutePolicy appView = new AppRoutePolicy();
			String appPolicyRespUrl = appPolicyUrl;
			appPolicyRespUrl=sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + appPolicyUrl;
			ResponseEntity<String> loginHeader = connectCiscoClient(request, sdwanEndpoints);
			Cookie cooki = getCookiesFromHeader(loginHeader);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
			headers.add("x-xsrf-token",loginHeader.getBody());
			
			ResponseEntity<String> appRouteResp = restClientService
					.getAndReturnHeadersWithProxyBasicAuthentication(appPolicyRespUrl.replaceAll("DYNAMICAPPROUTEDEFID", definationId),
					headers.toSingleValueMap(), sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			
			if(Objects.nonNull(appRouteResp)) {
				appView=Utils.convertJsonToObject(appRouteResp.getBody(), AppRoutePolicy.class);
			}
			return appView;
		}
		
		/**
		 * @ harshil-changes
		 * @param ciscoPolicyDetailBean
		 * @param sdwanEndpoints
		 * @return
		 * @throws TclCommonException
		 */
		public List<List<Map<String, Object>>> getPolicyAssociatedAddressesWithId(CiscoPolicyDetailBean ciscoPolicyDetailBean,
					SdwanEndpoints sdwanEndpoints) throws TclCommonException {
			String addressListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + addressListPolicyUrl;
			Map<String, Object> addressMap = new HashMap<>();
			List<List<Map<String, Object>>> finalList = new ArrayList<>();
			RestResponse restResponse = restClientService.getWithProxyBasicAuthentication(addressListUrl, new HashMap<>(), sdwanEndpoints.getServerUsername(),
					sdwanEndpoints.getServerPassword());
			if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
				addressMap = Utils.convertJsonToObject(restResponse.getData(), Map.class);
			}

			List<Map<String, Object>> addressMapList = (List<Map<String, Object>>)addressMap.get("data");
			List<Map<String, Object>> sourceAddressList = new ArrayList<>();
			Map<String, Object> sourceMap =new HashMap<>();
			List<Map<String,Object>> destinationAddressList = new ArrayList<>();
			Map<String, Object> destinationMap =new HashMap<>();
			if(!ciscoPolicyDetailBean.getSourceAddress().isEmpty() && ciscoPolicyDetailBean.getSourceAddress()!=null) {
				sourceAddressList = addressMapList.stream().filter(o ->
						o.get("listId").toString().equalsIgnoreCase(ciscoPolicyDetailBean.getSourceAddress().get(0))).collect(Collectors.toList());
				sourceAddressList.get(0).remove("entries");
				List<Map<String, Object>> sourceEntries = new ArrayList<>();
				ciscoPolicyDetailBean.getSourceIp().forEach(obj -> {
					Map<String, Object> ipMap = new LinkedHashMap<>();
					ipMap.put("ipPrefix", obj);
					sourceEntries.add(ipMap);
				});
				sourceAddressList.get(0).put("entries", sourceEntries);
				sourceAddressList.get(0).put("addressType", "Source Address");
				}
				else {
				sourceMap.put("addressType", "Source Address");
				sourceAddressList.add(sourceMap);
				}
			if(!ciscoPolicyDetailBean.getDestinationAddress().isEmpty() && ciscoPolicyDetailBean.getDestinationAddress()!=null) {
				destinationAddressList = addressMapList.stream().filter(o ->
						o.get("listId").toString().equalsIgnoreCase(ciscoPolicyDetailBean.
								getDestinationAddress().get(0))).collect(Collectors.toList());
				destinationAddressList.get(0).remove("entries");
				List<Map<String, Object>> descEntries = new ArrayList<>();
				ciscoPolicyDetailBean.getDestinationIp().forEach(obj -> {
					Map<String, Object> ipMap = new LinkedHashMap<>();
					ipMap.put("ipPrefix", obj);
					descEntries.add(ipMap);
				});
				
				destinationAddressList.get(0).put("entries", descEntries);
				destinationAddressList.get(0).put("addressType", "Destination Address");
				}
				else {
				destinationMap.put("addressType", "Destination Address");
				destinationAddressList.add(destinationMap);
				}
			finalList.add(sourceAddressList);
			finalList.add(destinationAddressList);
			return finalList;
		}
		 
		  /**
		   * harshil changes
		   * @param addressList
		   * @param sdwanEndpoints
		   * @return
		   * @throws TclCommonException
		   */
		  public void updatePolicyAddress(List<List<Map<String, Object>>> addressList, SdwanEndpoints sdwanEndpoints, StringBuilder stringBuilder) throws TclCommonException {
			  for (List<Map<String, Object>> addressMapList : addressList) {
				  for (Map<String, Object> addressMap : addressMapList) {
					  String addressType = addressMap.get("addressType").toString();
					  String listId = null;
					  if(addressMap.containsKey("listId")) {
					   listId = addressMap.get("listId").toString();
					  
					  Map<String, Object> appPayload = new LinkedHashMap<>();
					  appPayload.put("name", addressMap.get("name").toString());
					  appPayload.put("description", addressMap.get("description").toString());
					  appPayload.put("type", addressMap.get("type").toString());
					  appPayload.put("listId", listId);
					  appPayload.put("entries", addressMap.get("entries"));
					  String appListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + addressListPolicyPutUrl + "/DYNAMICAPPREFID";
					  appListUrl = appListUrl.replace("DYNAMICAPPREFID", listId);
					  String requestBody = Utils.convertObjectToJson(appPayload);
					  RestResponse restResponse = restClientService.putWithProxyBasicAuthentication(appListUrl, requestBody, new HashMap<>(), sdwanEndpoints.getServerUsername(),
							  sdwanEndpoints.getServerPassword());
					  if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData()) && restResponse.getStatus() == Status.SUCCESS) {
						  stringBuilder.append("Updating ").append(addressType).append(" succeeded").append(",");
					  } else if (restResponse.getStatus() == Status.ERROR && restResponse.getErrorMessage().contains("Failed to acquire lock, template or policy locked in edit mode.")) {
						  if ("SOURCE ADDRESS".equalsIgnoreCase(addressType)) {
							  stringBuilder.append("Updating source address failed").append(",");
						  }
						  if ("DESTINATION ADDRESS".equalsIgnoreCase(addressType)) {
							  stringBuilder.append("Updating destination address failed").append(",");
						  }
					  } else {
						  throw new TclCommonException(ExceptionConstants.CISCO_FAILED_TO_UPDATE_POLICY,
								  ResponseResource.R_CODE_ERROR);
					  }
					  }
					  else {
						  if ("SOURCE ADDRESS".equalsIgnoreCase(addressType)) {
							  stringBuilder.append("Updating source address failed").append(",");
						  }
						  if ("DESTINATION ADDRESS".equalsIgnoreCase(addressType)) {
							  stringBuilder.append("Updating destination address failed").append(",");
						  }
					  }
				  }
			  }
		  }

		  /**
			 * 
			 * @param definitionId
			 * @param endpoint
			 * @param request
			 * @return
			 * @author SGanta
			 */
			public DataPolicyQos getPolicyAssociatedDscpWithDefId(String definitionId, SdwanEndpoints endpoint,
					HttpServletRequest request) {
				LOGGER.info("Inside getPolicyAssociatedDscpWithDefId ");
				String dataPolicyRespUrl = dataPolicyUrl;
				dataPolicyRespUrl=endpoint.getServerIp() + ":" + endpoint.getServerPort() + dataPolicyUrl;
				ResponseEntity<String> loginHeader = connectCiscoClient(request, endpoint);
				Cookie cooki = getCookiesFromHeader(loginHeader);
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
				headers.add("x-xsrf-token",loginHeader.getBody());
				ResponseEntity<String> appRouteResp = restClientService
						.getAndReturnHeadersWithProxyBasicAuthentication(dataPolicyRespUrl.replaceAll("DYNAMICDATADEFID", definitionId),
								headers.toSingleValueMap(), endpoint.getServerUsername(), endpoint.getServerPassword());
				DataPolicyQos dataPolicy=new DataPolicyQos();
				try {
					if( Objects.nonNull(appRouteResp)) {
					dataPolicy=Utils.convertJsonToObject(appRouteResp.getBody(), DataPolicyQos.class);
					}
				} catch (TclCommonException e) {
					LOGGER.info("Error in getting getPolicyAssociatedDscpWithDefId");
				}
				return dataPolicy;
			}
			 /**
			   * @author SGanta
			   * @param definitionId
			   * @param sdwanEndpoints
			   * @param qosDscpPayloadList
			   * @param httpServletRequest
			   * @return
			   * @throws TclCommonException
			   */
			public String updateQosDscpWithDefid(String definitionId, List<SdwanEndpoints> sdwanEndpoints,
					List<DataPolicyQos> qosDscpPayloadList, HttpServletRequest httpServletRequest,StringBuilder dataUpdateResponse) throws TclCommonException {
				LOGGER.info("Inside updateQosDscpWithDefid");
				for (SdwanEndpoints endPoints : sdwanEndpoints) {
					String dataPolicyRespUrl = dataPolicyUrl;
					dataPolicyRespUrl=endPoints.getServerIp() + ":" + endPoints.getServerPort() + dataPolicyUrl;
					ResponseEntity<String> loginHeader = connectCiscoClient(httpServletRequest, endPoints);
					Cookie cooki = getCookiesFromHeader(loginHeader);
					HttpHeaders headers = new HttpHeaders();
					headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
					headers.add("x-xsrf-token",loginHeader.getBody());
					if(Objects.nonNull(qosDscpPayloadList) && !qosDscpPayloadList.isEmpty() ) {
					LOGGER.info(Utils.convertObjectToJson(qosDscpPayloadList.get(0)));
					String requestBody= Utils.convertObjectToJson(qosDscpPayloadList.get(0));
					LOGGER.info("Put url requestBody response : {}",requestBody);
					dataPolicyRespUrl=dataPolicyRespUrl.replaceAll("DYNAMICDATADEFID", definitionId);
					try {
					RestResponse dataResp = restClientService
							.putWithProxyBasicAuthentication(dataPolicyRespUrl, requestBody, 
									headers.toSingleValueMap(), endPoints.getServerUsername(), endPoints.getServerPassword());
					if (Objects.nonNull(dataResp) && dataResp.getStatus() == Status.SUCCESS) {
						dataUpdateResponse.append("Updated dscp ").append(Constants.SUCCESS);
						LOGGER.info("dataResp response {}",dataResp.getData());
						LOGGER.info("dataUpdateResponse is: "+ dataUpdateResponse);
					}
					else if(dataResp.getStatus() == Status.ERROR && dataResp.getErrorMessage().contains("Failed to acquire lock, template or policy locked in edit mode.")) {
						LOGGER.info("Updating dscp failed");
						dataUpdateResponse.append("Updating dscp failed");
						//throw new TclCommonException(ExceptionConstants.CISCO_CONCURRENT_UPDATE_ERROR, ResponseResource.R_CODE_ERROR);
					}
					else {
						LOGGER.error("data response {}",dataResp.getErrorMessage());
						throw new TclCommonException(dataResp.getErrorMessage(), R_CODE_ERROR);
					}
					}
					catch(TclCommonException e) {
						throw new TclCommonException(e, R_CODE_ERROR);
					}
					
					}
				}
				return dataUpdateResponse.toString();
			}
}
