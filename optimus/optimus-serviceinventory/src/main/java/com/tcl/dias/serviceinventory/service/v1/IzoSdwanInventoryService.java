package com.tcl.dias.serviceinventory.service.v1;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.common.constants.CommonConstants.MDC_TOKEN_KEY;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Sets;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.tcl.dias.serviceinventory.beans.AppBWUtilizationBean;
import com.tcl.dias.serviceinventory.beans.AppLinkUtilizationBean;
import com.tcl.dias.serviceinventory.beans.CpeBWCalculationBean;
import com.tcl.dias.serviceinventory.beans.PagedResultWithTimestamp;
import com.tcl.dias.serviceinventory.beans.SdwanOrgTemplateMapping;
import com.tcl.dias.serviceinventory.beans.SdwanSiteUtilizationDetails;
import com.tcl.dias.serviceinventory.beans.SiteBWUtilizationBean;
import com.tcl.dias.serviceinventory.beans.TemplatePathInterfaceDetail;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface.Bandwidth;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface.ConfiguredInterfaceBw;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface.CpeInterfaceDetails;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface.SdwanWanInterface;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface.Vni;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.CircuitNames;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.CircuitPriorities;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.ForwardingProfile;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.Priority;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.Replication;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_link_utilization.ColumnMetaDatum;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_link_utilization.LinkUtilization;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_link_details.WanPathDetails;
import com.tcl.dias.serviceinventory.beans.PerformanceAttributes;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.beans.ApplicationInfoRequestBean;
import com.tcl.dias.serviceinventory.beans.AuditHistoryCustomer;
import com.tcl.dias.serviceinventory.beans.AuditHistorySalesCSV;
import com.tcl.dias.serviceinventory.beans.CpeSyncStatusBean;
import com.tcl.dias.serviceinventory.beans.CpeUnderlaySitesBean;
import com.tcl.dias.serviceinventory.beans.NetworkSiteDetails;
import com.tcl.dias.serviceinventory.beans.SdwanAddressBean;
import com.tcl.dias.serviceinventory.beans.SdwanAliasUpdateRequest;
import com.tcl.dias.serviceinventory.beans.SdwanApplications;
import com.tcl.dias.serviceinventory.beans.SdwanApplicationsBean;
import com.tcl.dias.serviceinventory.beans.SdwanCPEBean;
import com.tcl.dias.serviceinventory.beans.SdwanCPEInformationBean;
import com.tcl.dias.serviceinventory.beans.SdwanCpeAllDetailBean;
import com.tcl.dias.serviceinventory.beans.SdwanCpeDetails;
import com.tcl.dias.serviceinventory.beans.SdwanCpeInfoBean;
import com.tcl.dias.serviceinventory.beans.SdwanCpePerformanceDetails;
import com.tcl.dias.serviceinventory.beans.SdwanPathPriorityBean;
import com.tcl.dias.serviceinventory.beans.SdwanPolicyDetailBean;
import com.tcl.dias.serviceinventory.beans.SdwanPolicyDetailsRequestBean;
import com.tcl.dias.serviceinventory.beans.SdwanPolicyListBean;
import com.tcl.dias.serviceinventory.beans.SdwanServiceDetailBean;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetails;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetailsBean;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetailsPerformaceBean;
import com.tcl.dias.serviceinventory.beans.SdwanSitePerformanceDetails;
import com.tcl.dias.serviceinventory.beans.SdwanTaskDetailsBean;
import com.tcl.dias.serviceinventory.beans.SdwanTemplateBean;
import com.tcl.dias.serviceinventory.beans.SdwanTemplateDetailBean;
import com.tcl.dias.serviceinventory.beans.SdwanTemplateDetails;
import com.tcl.dias.serviceinventory.beans.SiteAndCpeStatusCount;
import com.tcl.dias.serviceinventory.beans.SiteDetailsSearchRequest;
import com.tcl.dias.serviceinventory.beans.TemplateCpeStatusResponse;
import com.tcl.dias.serviceinventory.beans.TemplateDetails;
import com.tcl.dias.serviceinventory.beans.TemplateSiteDetails;
import com.tcl.dias.serviceinventory.beans.VersaAddressListBean;
import com.tcl.dias.serviceinventory.beans.VersaApplicationNames;
import com.tcl.dias.serviceinventory.beans.VersaApplicationsResponse;
import com.tcl.dias.serviceinventory.beans.VersaUserDefAppRequest;
import com.tcl.dias.serviceinventory.beans.VersaUserDefinedAppsResponse;
import com.tcl.dias.serviceinventory.beans.ViewSiServiceInfoAllBean;
import com.tcl.dias.serviceinventory.beans.VwServiceAssetAttributeBean;
import com.tcl.dias.serviceinventory.beans.VwServiceAttributesBean;
import com.tcl.dias.serviceinventory.constants.ExceptionConstants;
import com.tcl.dias.serviceinventory.csv.columnordering.CustomMappingStrategySales;
import com.tcl.dias.serviceinventory.entity.entities.SIAsset;
import com.tcl.dias.serviceinventory.entity.entities.SIAssetAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAdditionalInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetAdditionalInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAssetInfo;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.entities.SdwanEndpoints;
import com.tcl.dias.serviceinventory.entity.entities.SdwanInventoryAudit;
import com.tcl.dias.serviceinventory.entity.entities.ViewSiServiceInfoAll;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIAssetRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAssetAdditionalInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceAssetInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceInfoRepository;
import com.tcl.dias.serviceinventory.entity.repository.SdwanEndpointsRepository;
import com.tcl.dias.serviceinventory.entity.repository.SdwanInventoryAuditRepository;
import com.tcl.dias.serviceinventory.entity.repository.SiServiceAttributeRepository;
import com.tcl.dias.serviceinventory.entity.repository.VwSiServiceInfoAllRepository;
import com.tcl.dias.serviceinventory.izosdwan.beans.BandwidthUtilizationOfApp;
import com.tcl.dias.serviceinventory.izosdwan.beans.OtherAppBWUtilization;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanAndDiaTraffic;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanBandwidthUtilized;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanBandwidths;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanLinkUsages;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanPolicyBean;
import com.tcl.dias.serviceinventory.izosdwan.beans.SdwanPolicyBeanWithTimeStamp;
import com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters.PerformanceRequest;
import com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters.PerformanceResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters.SiteUtilizationRequest;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_group.AddressGroupView;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_group.Group;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_list.Address;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_list.AddressListView;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_commit_template.CommitTemplateRequest;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_commit_template.VersanmsDevices;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status.Appliance;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status.VersaCpeStatusResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status.VersanmsApplianceStatusResult;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_devicegroup_template.CpeGroupByTemplate;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.AccessPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.FirewallPolicyConfig;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.FirewallPreServices;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.GetFirewallPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.SecurityProfile;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Services;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Zone_;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_custom_service.CustomService;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_policies.AccessFireWallPolicyRules;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_policies.AccessPolicyGroup;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_policies.FireWallRules;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_policies.GetFirewallRuleName;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.ForwardingProfile_;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.ForwardingProfiles;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosPolicyRule;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosPolicyRules;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosRule;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.AppQosRules;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.QosPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.QosPolicyApplication;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Address_;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Application;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Destination;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Match;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Rule;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Rules_;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Source;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.TrafficSteeringPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.TrafficSteeringRuleData;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.UrlCategory;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Zone;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_predefined_url.PredefinedUrl;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_qos_profile.QoSProfiles;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_qos_profile.QosProfile;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_sla_profile.SlaProfile_;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_sla_profile.SlaProfiles;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_sla_profile.SlaProfilesCreatePayload;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_task_id_detail.TaskDetailById;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_task_id_detail.VersaTasksProgressmessage;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.DeviceGroupDatum;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.TemplateCommitResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.TemplateInSyncStatus;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.VersanmsDeviceGroups;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url.UserdefinedUrl;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_wan_status.WanStatus;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_zone_list.ZonesListView;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import com.tcl.dias.serviceinventory.util.ServiceInventoryUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service class to hold all IZOSDWAN 
 * related functionalities
 * @author archchan
 *
 */
@Service
public class IzoSdwanInventoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanInventoryService.class);

	@Autowired
	SIServiceInfoRepository siServiceInfoRepository;
	
	@Autowired
	SIServiceDetailRepository siServiceDetailRepository;
	
	@Autowired
	SIServiceAdditionalInfoRepository serviceAdditionalInfoRepository;
	
	@Autowired
	SIServiceAssetAdditionalInfoRepository siServiceAssetAdditionalInfoRepository;

	@Autowired
	SIAssetRepository siAssetRepository;
	
	@Autowired
	SIAssetAttributeRepository siAssetAttributeRepository;
		
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	RestClientService restClientService;
	
	@Autowired
	VwSiServiceInfoAllRepository vwSiServiceInfoAllRepository;
	
	@Autowired
	SiServiceAttributeRepository siServiceAttributeRepository;
	
	@Autowired
	SIServiceAssetInfoRepository serviceAssetInfoRepository;
	
	@Value("${versa.api.cpe.status.url}")
	private String cpeStatusUrl;
	
	@Value("${olio.request.url}")
	private String olioRequestUrl;
	
	@Value("${olio.node.json.url}")
	private String olionodeJsonUrl;
	
	@Autowired
	SdwanEndpointsRepository sdwanEndpointsRepository;
	
	@Value("${versa.api.create.user.application}")
	private String versaCreateUserApplUrl;

	@Value("${versa.api.edit.user.application}")
	private String versaEditUserApplUrl;

	@Value("${versa.api.create.user.application.common.temp}")
	private String createUserApplCommonTempUrl;

	@Value("${versa.api.edit.user.application.common.temp}")
	private String editUserApplCommonTempUrl;

	@Value("${versa.api.cpe.grouped.by.template}")
	private String cpeGroupedByTemplateUrl;

	@Value("${versa.api.template.sync.status}")
	private String templateSyncStatusUrl;

	@Value("${versa.api.commit.template}")
	private String commitTemplateUrl;

	@Value("${versa.api.get.sdwan.policy.by.name}")
	private String trafficSteeringPolicyNameUrl;

	@Value("${versa.api.get.qos.policy.by.name}")
	private String qosPolicyNameUrl;

	@Autowired
	SdwanInventoryAuditRepository sdwanInventoryAuditRepository;
	
	@Value("${versa.api.get.user.applications}")
	private String versaGetUserApplsUrl;
	
	@Value("${versa.api.get.pre.defined.applications}")
	private String versaGetPreDefinedApplsUrl;
	
	@Value("${versa.api.get.user.app.by.name}")
	private String versaUserAppByNameUrl;

	@Value("${versa.api.get.fp.profile}")
	private String versaGetFpProfileUrl;

	@Value("${versa.api.get.fp.by.name}")
	private String versaGetFpByName;

	@Value("${versa.api.get.qos.profile}")
	private String versaGetQosProfileUrl;

	@Value("${versa.api.get.sla.profile}")
	private String versaSlaProfileUrl;

	@Value("${versa.api.get.address}")
	private String versaAddressUrl;

	@Value("${versa.api.edit.qos.policy}")
	private String versaEditQosPolicyUrl;

	@Value("${versa.api.edit.sdwan.policy}")
	private String versaEditSdwanPolicyUrl;

	@Value("${versa.api.edit.forwarding.profile}")
	private String versaEditFPUrl;

	@Value("${versa.api.user.defined.apps}")
	private String versaUserDefinedUrl;

	@Value("${versa.api.address.group}")
	private String versaAddressGroupUrl;

	@Value("${versa.api.get.zones}")
	private String versaZonesUrl;

	@Value("${versa.api.get.user.urls}")
	private String versaUserUrls;

	@Value("${versa.api.get.qos.policy}")
	private String versaGetQosPolicyUrl;

	@Value("${versa.api.get.sdwan.policy}")
	private String versaGetSdwanPolicyUrl;

	@Value("${versa.api.get.wan.status}")
	private String versaWanStatusUrl;

	@Value("${versa.api.pre.defined.urls}")
	private String versaPredefinedUrlsUrl;

	@Value("${versa.api.task.id.details}")
	private String versaTaskIdDetailesUrl;

	@Value("${versa.api.cpe.interface.details}")
	private String versaCpeInterfaceDetails;

	@Value("${versa.api.configured.interface.bw}")
	private String versaConfiguredInterfaceBw;
	
	@Value("${versa.analytics.logout}")
	private String versaAnalyticsLogoutUrl;
	
	@Value("${versa.analytics.login}")
	private String versaAnalyticsLoginUrl;
	
	@Value("${versa.analytics.app.usage.by.link}")
	private String versaAnalyticsAppUsageByLinkUrl;

	@Lazy
	@Autowired
	IzoSdwanAsyncTasks izoSdwanAsyncTasks;
	
	@Value("${versa.analytics.app.bw.consumption.url}")
	private String versaAnalyticsAppBwUrl;
	
	@Value("${versa.analytics.site.usage.by.app.url}")
	private String versaAnalyticsSiteUsageByAppUrl;
	
	@Value("${versa.analytics.sdwan.traffic.util.url}")
	private String versaAnalyticsSdwanTrafficUrl;
	
	@Value("${versa.analytics.dia.traffic.util.url}")
	private String versaAnalyticsDiaTrafficUrl;
	
	@Value("${versa.api.put.traffic.steering.url}")
	private String versaTrafficSteeringPutUrl;
	
	@Value("${versa.api.put.qos.url}")
	private String versaSaveQosUrl;
	//PUT- fire wall Url
	@Value("${versa.api.put.firewall.url}")
	private String versaSaveFireWallUrl;
	
	@Value("${versa.api.get.traffic.steering.url}")
	private String versaAnalyticsTrafficGetUrl;
	
	@Value("${versa.api.get.qos.url}")
	private String versaQosGetUrl;
	
	@Value("${versa.sla.profile.create.url}")
	private String versaSlaProfileCreateUrl;
	
	@Value("${versa.api.sla.profile.by.name.url}")
	private String versaSlaProfileViewByNameUrl;
	
	@Value("${versa.api.fp.profile.create}")
	private String versaFpProfileCreateUrl;
	
	@Value("${versa.sla.profile.edit.url}")
	private String versaSlaProfileEdit;
	
	@Value("${versa.api.wan.path.url}")
	private String versaApiWanPathDetailsUrl;
	
	@Value("${versa.api.firewall.getPolicy.url}")
	private String versaApiFirewallPolicyGetUrl;
	
	@Value("${versa.api.firewall.getConfiguration.url}")
	private String versaApiFirewallPolicyConfigurationGetUrl;
	
	@Value("${versa.api.firewall.get.config.for.policy}")
	private String versaApiFirewallForPolicy;
	
	//GET-fire wall config url
	@Value("${versa.api.firewall.get.config.policyrule}")
	private String versaApiFirewallForPolicyRule;
	
	
	@Value("${versa.api.firewall.get.predefined.services}")
	private String versaApiFirewallPreDefinedServices;
	
	@Value("${versa.api.firewall.get.custom.services}")
	private String versaApiFirewallCustomServices;
	
	//Get fire wall for access policy names
	@Value("${versa.api.firewall.get.listofnames.policyrule}")
	private String versaApiFirewallForAccessPolicyName;

	/**
	 * Get Service details based on login user for product SDWAN
	 *
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanSiteDetailsBean getSdwanAndNetworkSiteDetails(Integer productId, Integer page, Integer size,Integer customerId,Integer partnerId,Integer customerLeId)
			throws TclCommonException {
		List<Integer> customerLeIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		if (productId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		SdwanSiteDetailsBean SdwanSiteDetailsBean = new SdwanSiteDetailsBean();
		List<ViewSiServiceInfoAllBean> vwSiInfoOverlay = new ArrayList<>();		
		try {
			getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);

			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			if (!customerLeIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<String> sdwanServiceIds = new ArrayList<>();
				List<Integer> sdwanSysId = new ArrayList<>();
				List<Map<String,Object>> sdWanSiteDetails =  new ArrayList<>();
				if(page == -1) {
					sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetails(customerLeIds, partnerLeIds, productId, customerId, partnerId);
					SdwanSiteDetailsBean.setTotalItems(sdWanSiteDetails.size());
				} else {
					page = (page - 1) * size;
					sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetailsWithPageLimit(customerLeIds, partnerLeIds, productId, customerId, partnerId, page, size);
					Integer totalItems = vwSiServiceInfoAllRepository.findSdwanSiteCount(customerLeIds, partnerLeIds, productId, customerId, partnerId);
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
				vwSiInfoOverlay.stream().forEach(vwSiInfo->{
					sdwanServiceIds.add(vwSiInfo.getServiceId());
					sdwanSysId.add(vwSiInfo.getSysId());
				});
				
//				getSiteAttributesAndAssets(SdwanSiteDetailsBean, vwSiInfoOverlay, vwSiInfoUnderlay, sdwanServiceIds,
//						sdwanSysId, underlaySysId);				

				getSdwanSiteData(SdwanSiteDetailsBean, vwSiInfoOverlay, sdwanServiceIds, sdwanSysId, null);
				SdwanSiteDetailsBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return SdwanSiteDetailsBean;
	}

	/**
	 * To get Sdwan site data
	 *
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
			vwSiInfoUnderlay.stream().forEach(underlay->{
				underlaySysIds.add(underlay.getSysId());
			});
		}
		Map<String, List<VwServiceAssetAttributeBean>> uderlayGroupedByOverlay = vwSiInfoUnderlay.stream()
				.collect(Collectors.groupingBy(VwServiceAssetAttributeBean::getIzoSdwanSrvcId));
		LOGGER.info("Site Listing fetching service attributes for  IZOSDWAN ServiceDetailids {}", sdwanSysId);
		List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
				.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
						Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
								ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
		List<Map<String, Object>> templateDetails = serviceAdditionalInfoRepository.findAttributesByUnderlayServiceIds(underlaySysIds, 
				Arrays.asList(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME));
		List<VwServiceAttributesBean> underlayTemplates = new ArrayList<>();
		if (templateDetails != null && !templateDetails.isEmpty()) {
			final ObjectMapper mapper = new ObjectMapper();
			templateDetails.stream().forEach(underlayTemplate -> {
				underlayTemplates.add(mapper.convertValue(underlayTemplate, VwServiceAttributesBean.class));
			});
		}
		persistSdwanSiteDetails(SdwanSiteDetailsBean, vwSiInfoOverlay, overLayServiceAttrs, uderlayGroupedByOverlay,
				status, underlayTemplates);
	}
	
	/**
	 * To construct sdwan site details
	 *
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
			Map<String, List<VwServiceAssetAttributeBean>> uderlayGroupedByOverlay, Boolean status, 
			List<VwServiceAttributesBean> underlayTemplates)
			throws TclCommonException, IOException {
		try {
			LOGGER.info("Inside persistSdwanSiteDetails to persist data ");
			List<SdwanSiteDetails> sdwanSiteDetails = new ArrayList<>();
			SdwanSiteDetailsBean.setSiteDetails(sdwanSiteDetails);
			Set<String> instanceRegions = new HashSet<>();
			overLayServiceAttrs.stream().filter(
					attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
					.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
			LOGGER.info("Inside site listing and fetch sdwan endpoints for server code {} ", instanceRegions);
			List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
					.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
			VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
			if (Objects.nonNull(versaCpeStatusResponse)
					&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
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
								if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
									sdwanSiteDetail.setOrganisationName(attr.getAttributeValue());
								} else if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
									sdwanSiteDetail.setInstanceRegion(attr.getAttributeValue());
								}

							});
					underlayTemplates.stream()
					.filter(underlayTemplate -> overlay.getServiceId().equals(underlayTemplate.getIzoSdwanSrvcId()))
					.forEach(template->{
						SdwanTemplateDetails templateDetail = new SdwanTemplateDetails();
						templateDetail.setTemplateName(template.getAttributeValue());
						templateDetail.setSdwanServiceId(template.getIzoSdwanSrvcId());
						templateDetail.setAttributeId(template.getAttributeId());
						templateDetail.setUnderlayServiceId(template.getServiceId());
						templateDetails.add(templateDetail);
					});
					if (Objects.isNull(status) || status == false) {
						List<VwServiceAssetAttributeBean> undeyLays = uderlayGroupedByOverlay
								.get(overlay.getServiceId());
						persistUnderlayInformation(SdwanSiteDetailsBean, sdwanSiteDetail, undeyLays,
								versaCpeStatusResponse, overlay, instanceByCode);
					}				
					 List<SdwanTemplateDetails> distinctTemplates = templateDetails.stream()
			                    .collect(Collectors.collectingAndThen(
			                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SdwanTemplateDetails:: getTemplateName))),
			                            ArrayList::new));
					 sdwanSiteDetail.setTemplateDetails(distinctTemplates);
				});
				if (Objects.nonNull(status) && status) {
					persistUnderlayInformationThreaded(SdwanSiteDetailsBean, uderlayGroupedByOverlay,
							versaCpeStatusResponse, instanceByCode);
					sdwanSiteDetails.forEach(sdwanSiteDetail -> {
						sdwanSiteDetail
								.setSiteStatus(sdwanSiteDetail.getUpLinkCount() == 0 ? ServiceInventoryConstants.OFFLINE
										: (sdwanSiteDetail.getDownLinkCount() == 0 ? ServiceInventoryConstants.ONLINE
												: ServiceInventoryConstants.DEGRADED));
						SdwanSiteDetailsBean.setOnlineCount(incrementCountIfOnline(sdwanSiteDetail.getSiteStatus(),
								SdwanSiteDetailsBean.getOnlineCount()));
						SdwanSiteDetailsBean.setOfflineCount(incrementCountIfOffline(sdwanSiteDetail.getSiteStatus(),
								SdwanSiteDetailsBean.getOfflineCount()));
						SdwanSiteDetailsBean.setDegradedCount(incrementCountIfDegraded(sdwanSiteDetail.getSiteStatus(),
								SdwanSiteDetailsBean.getDegradedCount()));
					});
				}
			}
			
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
		
	}

	/**
	 * To fetch and save underlay info in bean using multi-threading
	 *
	 * @param sdwanSiteDetailsBean
	 * @param uderlayGroupedByOverlay
	 * @param versaCpeStatusResponse
	 * @param instanceByCode
	 */
	void persistUnderlayInformationThreaded(SdwanSiteDetailsBean sdwanSiteDetailsBean,
			Map<String, List<VwServiceAssetAttributeBean>> uderlayGroupedByOverlay,
			VersaCpeStatusResponse versaCpeStatusResponse, Map<String, List<SdwanEndpoints>> instanceByCode) {
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
					getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCpeDetail.getCpeName(), status,
							sdwanCpeDetail.getControllers());
					sdwanCpeDetail.setCpeAvailability(status.get(ServiceInventoryConstants.CPE_AVAILABILITY));
					sdwanCpeDetail.setCpeStatus(status.get(ServiceInventoryConstants.CPE_STATUS));
					sdwanSiteDetail.getSdwanCpeDetails().add(sdwanCpeDetail);
					completableFutures.add(izoSdwanAsyncTasks.getWanSiteStatusAsync(instanceByCode, sdwanCpeDetail,
							sdwanSiteDetail.getInstanceRegion(), sdwanSiteDetail.getOrganisationName(),
							sdwanSiteDetail));

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
						sdwanSiteDetail.setOnlineCpeCount(incrementCountIfOnline(sdwanCpeDetail.getCpeStatus(),
								sdwanSiteDetail.getOnlineCpeCount()));
						sdwanSiteDetail.setOfflineCpeCount(incrementCountIfOffline(sdwanCpeDetail.getCpeStatus(),
								sdwanSiteDetail.getOfflineCpeCount()));

					});
				});
			}
		});
		for (CompletableFuture completableFuture : completableFutures) {
			completableFuture.join();
		}
	}

	/**
	 * To fetch and save underlay info in bean
	 *
	 * @param sdwanSiteDetailsBean
	 * @param sdwanSiteDetail
	 * @param undeyLays
	 * @param versaCpeStatusResponse
	 * @param overlay
	 * @param instanceByCode
	 */
	void persistUnderlayInformation(SdwanSiteDetailsBean sdwanSiteDetailsBean, SdwanSiteDetails sdwanSiteDetail,
			List<VwServiceAssetAttributeBean> undeyLays, VersaCpeStatusResponse versaCpeStatusResponse,
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
				getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCpeDetail.getCpeName(), cpeStatus,
						sdwanCpeDetail.getControllers());
				sdwanCpeDetail.setCpeAvailability(cpeStatus.get(ServiceInventoryConstants.CPE_AVAILABILITY));
				sdwanCpeDetail.setCpeStatus(cpeStatus.get(ServiceInventoryConstants.CPE_STATUS));
				sdwanSiteDetail.getSdwanCpeDetails().add(sdwanCpeDetail);
				Map<String, String> links = new HashMap<>();
				if (ServiceInventoryConstants.ONLINE.equals(sdwanCpeDetail.getCpeStatus())) {
					getWanStatusFromVersa(sdwanCpeDetail.getCpeName(), sdwanCpeDetail.getControllers(),
							sdwanSiteDetail.getInstanceRegion(), sdwanSiteDetail.getOrganisationName(), instanceByCode,
							links);
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
				} else
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
							incrementCountIfOnline(sdwanCpeDetail.getCpeStatus(), sdwanSiteDetail.getOnlineCpeCount()));
					sdwanSiteDetail.setOfflineCpeCount(incrementCountIfOffline(sdwanCpeDetail.getCpeStatus(),
							sdwanSiteDetail.getOfflineCpeCount()));

				});

			});
		}
		sdwanSiteDetail.setSiteStatus(sdwanSiteDetail.getUpLinkCount() == 0 ? ServiceInventoryConstants.OFFLINE
				: (sdwanSiteDetail.getDownLinkCount() == 0 ? ServiceInventoryConstants.ONLINE
						: ServiceInventoryConstants.DEGRADED));
		sdwanSiteDetailsBean.setOnlineCount(
				incrementCountIfOnline(sdwanSiteDetail.getSiteStatus(), sdwanSiteDetailsBean.getOnlineCount()));
		sdwanSiteDetailsBean.setOfflineCount(
				incrementCountIfOffline(sdwanSiteDetail.getSiteStatus(), sdwanSiteDetailsBean.getOfflineCount()));
		sdwanSiteDetailsBean.setDegradedCount(
				incrementCountIfDegraded(sdwanSiteDetail.getSiteStatus(), sdwanSiteDetailsBean.getDegradedCount()));
	}

	/**
	 * To fetch WAN status from Versa
	 *
	 * @param cpeName
	 * @param controllers
	 * @param instanceRegion
	 * @param orgName
	 * @param instanceByCode
	 * @param linkStatus
	 */
	protected void getWanStatusFromVersa(String cpeName, List<String> controllers, String instanceRegion,
			String orgName, Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, String> linkStatus) {
		LOGGER.info("Inside getWanStatusFromVersa for CPE name {} instance region {} orgName {} thread {}, {}", cpeName,
				instanceRegion, orgName, Thread.currentThread().getId(), Thread.currentThread().getName());
		controllers.forEach(controller -> {
			final String[] wanStatusUrl = { versaWanStatusUrl };
			instanceByCode.get(instanceRegion).forEach(instance -> {
				wanStatusUrl[0] = instance.getServerIp() + ":" + instance.getServerPort() + wanStatusUrl[0];
				wanStatusUrl[0] = wanStatusUrl[0].replace("DYNAMICCPENAME", cpeName);
				wanStatusUrl[0] = wanStatusUrl[0].replace("DYNAMICORGNAME", orgName);
				wanStatusUrl[0] = wanStatusUrl[0].replace("DYNAMICCONTROLLERNAME", controller);
				LOGGER.info("thread {} Wan Status URL : " + wanStatusUrl[0], Thread.currentThread().getName());
				if (linkStatus.isEmpty() || linkStatus.containsValue(ServiceInventoryConstants.DOWN)) {
					RestResponse response = restClientService.getWithBasicAuthentication(wanStatusUrl[0],
							new HashMap<>(), instance.getServerUsername(), instance.getServerPassword());
					if (Objects.nonNull(response) && response.getStatus() == Status.SUCCESS
							&& Objects.nonNull(response.getData())) {
						LOGGER.info("thread {} Response from wan status : {}", Thread.currentThread().getName(),
								response.getData());
						try {
							WanStatus wanStatus = Utils.convertJsonToObject(response.getData(), WanStatus.class);
							if (Objects.nonNull(wanStatus) && Objects.nonNull(wanStatus.getSdwanStatus())
									&& Objects.nonNull(wanStatus.getSdwanStatus().getPathStatus())) {
								wanStatus.getSdwanStatus().getPathStatus().forEach(pathStatus -> {
									if (!linkStatus.containsKey(pathStatus.getLocalWanLink())
											|| linkStatus.get(pathStatus.getLocalWanLink())
													.equalsIgnoreCase(ServiceInventoryConstants.DOWN))
										linkStatus.put(pathStatus.getLocalWanLink(), pathStatus.getConnState());
								});
							}
						} catch (TclCommonException e) {
							LOGGER.info("thread {} Error occurred in wan status : {}", Thread.currentThread().getName(),
									e.getMessage());
						}
					}
				}
			});
		});
	}
	
	/**
	 * Method to search and sort SDWAN site details
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
			getCustomerAndPartnerLeIds(customerIds, partnerLeIds, request.getCustomerLeId());
			// LOGGER.info("CustomerIds"+customerIds+"CustomerId"+customerId+"PartnerId"+partnerId);
			LOGGER.info("SDWAN SITE FILTER CustomerID :: {}", request.getCustomerId());
			LOGGER.info("SDWAN SITE FILTER CustomerLeIDs :: {}", customerIds);
			LOGGER.info("SDWAN SITE FILTER PartnerId :: {}", request.getPartnerId());
			LOGGER.info("SDWAN SITE FILTER PartnerLeIds :: {}", partnerLeIds);

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
            		   constructVwSiServiceInfoBean(vwSiInfoOverlay, siServiceDetail, sdwanSysId, sdwanServiceIds);
            	   });
//            	   getSiteAttributesAndAssets(sdwanSiteDetailsBean, vwSiInfoOverlay, vwSiInfoUnderlay, sdwanServiceIds,
//							sdwanSysId, underlaySysId);
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
	 * Method to search and sort (includes status) SDWAN sites
	 * 
	 * @param request
	 * @param status
	 * @return
	 * @throws TclCommonException
	 */
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
			getCustomerAndPartnerLeIds(customerIds, partnerLeIds, request.getCustomerLeId());
			// LOGGER.info("CustomerIds"+customerIds+"CustomerId"+customerId+"PartnerId"+partnerId);
			LOGGER.info("SDWAN SITE FILTER CustomerID :: {}", request.getCustomerId());
			LOGGER.info("SDWAN SITE FILTER CustomerLeIDs :: {}", customerIds);
			LOGGER.info("SDWAN SITE FILTER PartnerId :: {}", request.getPartnerId());
			LOGGER.info("SDWAN SITE FILTER PartnerLeIds :: {}", partnerLeIds);

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
						constructVwSiServiceInfoBean(vwSiInfoOverlay, siServiceDetail, sdwanSysId, sdwanServiceIds);
					});
					getSdwanSiteData(sdwanSiteDetailsBean, vwSiInfoOverlay, sdwanServiceIds, sdwanSysId, status);
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
	 * Method to persist ViewSiServiceInfoAllBean
	 * @param vwSiInfoOverlay
	 * @param siServiceDetail
	 * @param sdwanSysId
	 * @param sdwanServiceIds
	 */
	private void constructVwSiServiceInfoBean(List<ViewSiServiceInfoAllBean> vwSiInfoOverlay,
			SIServiceDetail siServiceDetail, List<Integer> sdwanSysId, List<String> sdwanServiceIds) {
		LOGGER.info("constructVwSiServiceInfoBean for SDWAN serviceId {} ",siServiceDetail.getTpsServiceId());
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
		   LOGGER.info("Persisted data in bean for SDWAN serviceId {} ",siServiceDetail.getTpsServiceId());
	}

	/**
	 * To fetch SDWAN Site CPE details tagged to customer/partner
	 *
	 * @param productId
	 * @param page
	 * @param size
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 */
	public SdwanCPEInformationBean getCPEDetails(Integer productId, Integer page, Integer size, Integer customerId,
			Integer partnerId, Integer customerLeId) throws IOException, TclCommonException {
		SdwanCPEInformationBean sdwanCPEInformationBean = new SdwanCPEInformationBean();

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
		if (!customerIds.isEmpty() || !partnerLeIds.isEmpty()) {
			List<Map<String, Object>> data = null;
			if (page == -1) {
				data = serviceAssetInfoRepository.findSdwanCpe(customerId, customerIds, partnerId, partnerLeIds,
						productId);
			} else {
				page = (page - 1) * size;
				data = serviceAssetInfoRepository.findSdwanCpe(customerId, customerIds, partnerId, partnerLeIds, page,
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
				List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository
						.findAttributesBySdwanServiceIds(overlaySysIds,
								Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
										ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING), productId);
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
								.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
						.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
				LOGGER.info("Inside SDWAN CPE listing fetching sdwan endpoint for server code {} ", instanceRegions);
				List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
						.findByServerCodeIn(instanceRegions);
				Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
						.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
				VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
				if (Objects.nonNull(versaCpeStatusResponse)
						&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
					List<SdwanCPEBean> sdwanCPEs = data.stream().map(cpeData -> {
						LOGGER.info("Inside SDWAN CPE listing persiting CPE data and cpe name {} ", cpeData.get("cpe_name"));
						SdwanCPEBean sdwanCPEBean = new SdwanCPEBean();
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
						SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
						sdwanTemplateBean.setTemplateId((Integer) cpeData.get("attributeId"));
						sdwanTemplateBean.setTemplateName((String) cpeData.get("attributeValue"));
						sdwanCPEBean.setTemplate(sdwanTemplateBean);
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
						getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCPEBean.getCpeName(), map,
								sdwanCPEBean.getControllers());
						sdwanCPEBean.setCpeStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
						sdwanCPEBean.setCpeAvailability(map.get(ServiceInventoryConstants.CPE_AVAILABILITY));
						sdwanCPEBean.setOsVersion(map.get(ServiceInventoryConstants.OS_VERSION));
						sdwanCPEBean.setUnderlaySysId((Integer) cpeData.get("srv_sys_id"));
						cpeAliasList.stream().filter(cpeAlias -> sdwanCPEBean.getId().equals(cpeAlias.getAssetSysId()))
								.forEach(cpeAlias -> sdwanCPEBean.setAlias(cpeAlias.getAttributeValue()));
						Map<String, String> links = new HashMap<>();
						if (ServiceInventoryConstants.ONLINE.equals(sdwanCPEBean.getCpeStatus())) {
							getWanStatusFromVersa(sdwanCPEBean.getCpeName(), sdwanCPEBean.getControllers(),
									sdwanCPEBean.getInstanceRegion(), sdwanCPEBean.getOrganisationName(),
									instanceByCode, links);
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
					Map<String, String> cpeStatus = new HashMap<>();
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
										getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse,
												cpeUnderlaySitesBean.getCpeName(), cpeStatus,
												cpeUnderlaySitesBean.getControllers());
										cpeUnderlaySitesBean
												.setSiteStatus(cpeStatus.get(ServiceInventoryConstants.CPE_STATUS));
										sdwanCPE.setUnderlaysOnlineCount(
												incrementCountIfOnline(cpeUnderlaySitesBean.getSiteStatus(),
														sdwanCPE.getUnderlaysOnlineCount()));
										sdwanCPE.setUnderlaysOfflineCount(
												incrementCountIfOffline(cpeUnderlaySitesBean.getSiteStatus(),
														sdwanCPE.getUnderlaysOfflineCount()));
										sdwanSiteAndStatus.add(cpeUnderlaySitesBean);
										Map<String, String> links = new HashMap<>();
										if (ServiceInventoryConstants.ONLINE
												.equals(cpeUnderlaySitesBean.getSiteStatus())) {
											getWanStatusFromVersa(cpeUnderlaySitesBean.getCpeName(),
													cpeUnderlaySitesBean.getControllers(), sdwanCPE.getInstanceRegion(),
													sdwanCPE.getOrganisationName(), instanceByCode, links);
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
							sdwanCPEInformationBean.setOnlineCpeCount(incrementCountIfOnline(sdwanCPE.getCpeStatus(),
									sdwanCPEInformationBean.getOnlineCpeCount()));
							sdwanCPEInformationBean.setOfflineCpeCount(incrementCountIfOffline(sdwanCPE.getCpeStatus(),
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
	 * Increment count if status is online
	 *
	 * @param status
	 * @param onlineCount
	 * @return
	 */
    private Integer incrementCountIfOnline(String status, Integer onlineCount)
    {
        if(ServiceInventoryConstants.ONLINE.equals(status))
            return onlineCount+1;
        else return onlineCount;
    }

	/**
	 * Increment count if status is offline
	 *
	 * @param status
	 * @param offlineCount
	 * @return
	 */
    private Integer incrementCountIfOffline(String status, Integer offlineCount)
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
    private Integer incrementCountIfDegraded(String status, Integer degradedCount)
    {
        if(ServiceInventoryConstants.DEGRADED.equals(status))
            return degradedCount+1;
        else return degradedCount;
    }
	
	/**
	 * Method to get detailed SI info
	 *
	 * @param serviceId
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanServiceDetailBean getSdwanServiceDetailInfo(String serviceId, Integer productId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		SdwanServiceDetailBean response = new SdwanServiceDetailBean();
		try {
			List<Map<String, Object>> details = vwSiServiceInfoAllRepository.getSiServiceDetailsAttrByServiceId(Arrays.asList(serviceId), productId,
					Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
			List<Map<String, Object>> underlayDetails = vwSiServiceInfoAllRepository.findUnderlayServiceIdbySdwanServiceId(Collections.singletonList(serviceId));
			if (details == null || details.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			List<Integer> underlaySysIds = new ArrayList<>();
			underlayDetails.forEach(underlayDetail -> {
				underlaySysIds.add((Integer) underlayDetail.get("sys_id"));
			});
			List<Map<String, Object>> templateDetails = serviceAdditionalInfoRepository.findAttributesByUnderlayServiceIds(underlaySysIds, 
					Arrays.asList(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME));
			Set<String> templateNames = new HashSet<>();
			if (templateDetails != null && !templateDetails.isEmpty()) {
				templateDetails.stream().forEach(underlayTemplate -> {
					templateNames.add((String) underlayTemplate.get("attributeValue"));
				});
			}
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
                    LOGGER.info("Fetching service detail for serviceID {} and attributeName {} and value {} ", serviceId, attrName, attrValue);
                    if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
                        response.setOrganasationName(attrValue);
                    } else if (attrName.equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
                        response.setInstanceRegion(attrValue);
						instanceRegions.add(attrValue);
                    }
                });

				List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
						.findByServerCodeIn(instanceRegions);
				Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
						.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
				VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
			if(Objects.nonNull(versaCpeStatusResponse) && Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
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
						getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, cpeUnderlaySitesBean.getCpeName(),
								cpeStatus, cpeUnderlaySitesBean.getControllers());
						cpeUnderlaySitesBean.setSiteStatus(cpeStatus.get(ServiceInventoryConstants.CPE_STATUS));
						cpeUnderlaySites.add(cpeUnderlaySitesBean);
						response.setOnlineCpeCount(incrementCountIfOnline(cpeUnderlaySitesBean.getSiteStatus(),
								response.getOnlineCpeCount()));
						response.setOfflineCpeCount(incrementCountIfOffline(cpeUnderlaySitesBean.getSiteStatus(),
								response.getOfflineCpeCount()));
						Map<String, String> links = new HashMap<>();
						if (ServiceInventoryConstants.ONLINE.equals(cpeUnderlaySitesBean.getSiteStatus())) {
							getWanStatusFromVersa(cpeUnderlaySitesBean.getCpeName(),
									cpeUnderlaySitesBean.getControllers(), response.getInstanceRegion(),
									response.getOrganasationName(), instanceByCode, links);
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
					response.setSiteStatus(getSdwanCpeStatus(versaCpeStatusResponse, assetInfo.getAssetName()));
				}
                LOGGER.info("Sdwan Template names {} for serviceid {} ", templateNames, serviceId);
                if (!templateNames.isEmpty())
                    response.setTemplateNames(templateNames);
                if (!cpeNames.isEmpty())
                    response.setCpeNames(cpeNames);
				response.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
                return response;
            } else throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		} catch (Exception ex) {
			LOGGER.error("Exception fetching detailed ServiceInformation for {}", serviceId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Get CPE details based on given filters
	 * 
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
	 */
	public SdwanCPEInformationBean getCpeDetailsBasedOnFilters(String searchText, Integer size, Integer page,
			String sortBy, String sortOrder, Integer productId, Integer customerId, Integer customerLeId,
			Integer partnerId) throws TclCommonException, IOException {
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();

		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
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
		SdwanCPEInformationBean sdwanCPEInformationBean = new SdwanCPEInformationBean();
		sdwanCPEInformationBean.setCPE(new ArrayList<>());
		if (Objects.nonNull(siAssetPaginated) && Objects.nonNull(siAssetPaginated.getContent())
				&& !siAssetPaginated.getContent().isEmpty()) {
			List<SIAsset> siAssets = siAssetPaginated.getContent();
			List<SdwanCPEBean> sdwanCPEBeans = new ArrayList<>();

			Set<String> sdwanServiceIds = new HashSet<>();
			sdwanServiceIds = siAssets.stream().map(siAsset -> siAsset.getSiServiceDetail().getIzoSdwanSrvcId())
					.collect(Collectors.toSet());
			// Fetching Sdwan templates mapped for underlay
			List<Integer> underlaySysIds = new ArrayList<>();
			underlaySysIds = siAssets.stream().map(siAsset -> siAsset.getSiServiceDetail().getId())
					.collect(Collectors.toList());
			List<VwServiceAttributesBean> underlayTemplates = getUnderlayTemplates(underlaySysIds);
			List<Map<String, Object>> underlayServices = new ArrayList<>();

			if (!sdwanServiceIds.isEmpty()) {
				// fetch template for the service ids
				// fetch the underlay service details tagged to izosdwan site
				underlayServices = vwSiServiceInfoAllRepository
						.findUnderlayServiceIdbySdwanServiceId(new ArrayList<>(sdwanServiceIds));
			}
			List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository
					.findAttributesBySdwanServiceIds(new ArrayList<>(sdwanServiceIds),
							Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
									ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING),
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
					attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
					.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
			List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository	
					.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));

			VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
			if (Objects.nonNull(versaCpeStatusResponse)
					&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
				Map<String, String> map = new HashMap<>();
				siAssets.forEach(siAsset -> {
					SdwanCPEBean sdwanCPEBean = new SdwanCPEBean();
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
					getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCPEBean.getCpeName(), map,
							sdwanCPEBean.getControllers());
					sdwanCPEBean.setCpeStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
					sdwanCPEBean.setCpeAvailability(map.get(ServiceInventoryConstants.CPE_AVAILABILITY));
					sdwanCPEBean.setOsVersion(map.get(ServiceInventoryConstants.OS_VERSION));
					// only SiServiceAttribute with Sdwan_Template_name as attributeName is fetched
					constructSdwanTemplateDetails(underlayTemplates, siAsset, sdwanCPEBean);
//					SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
//					sdwanTemplateBean.setTemplateId(
//							siAsset.getSiServiceDetail().getSiServiceAttributes().stream().findAny().get().getId());
//					sdwanTemplateBean.setTemplateName(siAsset.getSiServiceDetail().getSiServiceAttributes().stream()
//							.findAny().get().getAttributeValue());
//					sdwanCPEBean.setTemplate(sdwanTemplateBean);
					Map<String, String> links = new HashMap<>();
					if (ServiceInventoryConstants.ONLINE.equals(sdwanCPEBean.getCpeStatus())) {
						getWanStatusFromVersa(sdwanCPEBean.getCpeName(), sdwanCPEBean.getControllers(),
								sdwanCPEBean.getInstanceRegion(), sdwanCPEBean.getOrganisationName(), instanceByCode,
								links);
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
									getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse,
											cpeUnderlaySitesBean.getCpeName(), cpeStatus,
											cpeUnderlaySitesBean.getControllers());
									cpeUnderlaySitesBean
											.setSiteStatus(cpeStatus.get(ServiceInventoryConstants.CPE_STATUS));
									sdwanCPE.setUnderlaysOnlineCount(incrementCountIfOnline(
											cpeUnderlaySitesBean.getSiteStatus(), sdwanCPE.getUnderlaysOnlineCount()));
									sdwanCPE.setUnderlaysOfflineCount(incrementCountIfOffline(
											cpeUnderlaySitesBean.getSiteStatus(), sdwanCPE.getUnderlaysOfflineCount()));
									sdwanSiteAndStatus.add(cpeUnderlaySitesBean);
									Map<String, String> links = new HashMap<>();
									if (ServiceInventoryConstants.ONLINE.equals(cpeUnderlaySitesBean.getSiteStatus())) {
										getWanStatusFromVersa(cpeUnderlaySitesBean.getCpeName(),
												cpeUnderlaySitesBean.getControllers(), sdwanCPE.getInstanceRegion(),
												sdwanCPE.getOrganisationName(), instanceByCode, links);
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
						sdwanCPEInformationBean.setOnlineCpeCount(incrementCountIfOnline(sdwanCPE.getCpeStatus(),
								sdwanCPEInformationBean.getOnlineCpeCount()));
						sdwanCPEInformationBean.setOfflineCpeCount(incrementCountIfOffline(sdwanCPE.getCpeStatus(),
								sdwanCPEInformationBean.getOfflineCpeCount()));
					});
				}
			} else
				throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
			LOGGER.info("sdwanCPEBeans details in filter API  : {} items", sdwanCPEBeans);
			sdwanCPEInformationBean.setCPE(sdwanCPEBeans);
//			sdwanCPEInformationBean.setTotalPages(siAssetPaginated.getTotalPages());
//			sdwanCPEInformationBean.setTotalItems((int) siAssetPaginated.getTotalElements());
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
	 * Get CPE details based on given filters
	 *
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param assetName
	 * @param productId
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanCpeAllDetailBean getCpeDetailsBasedOnAssetName(Integer customerId, Integer customerLeId,
			Integer partnerId, String assetName, Integer productId) throws TclCommonException, IOException {

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);

		SdwanCpeAllDetailBean sdwanCpeAllDetailBean = new SdwanCpeAllDetailBean();
		List<Map<String, Object>> siServiceAssetInfo = vwSiServiceInfoAllRepository
				.findByAssetNameAndAssetTag(customerId, partnerId, customerIds, partnerLeIds, assetName, "SDWAN CPE");
		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesForCustomer(
				customerIds, partnerLeIds, productId, customerId, partnerId,
				Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
						ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
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
				attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
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
		VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
		if (Objects.nonNull(versaCpeStatusResponse)
				&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
			siServiceAssetInfo.stream().peek(assets -> {
				sdwanCpeAllDetailBean.setId((Integer) assets.get("assetId"));
				sdwanCpeAllDetailBean.setCpeName((String) assets.get("assetName"));
				sdwanCpeAllDetailBean.setModel((String) assets.get("model"));
				sdwanCpeAllDetailBean.setSerialNumber((String) assets.get("serialNo"));
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
				sdwanCpeAllDetailBean.setControllers(new ArrayList<>());
				// fetch status of CPE from versa
				Map<String, String> map = new HashMap<>();
				getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCpeAllDetailBean.getCpeName(), map,
						sdwanCpeAllDetailBean.getControllers());
				sdwanCpeAllDetailBean.setCpeStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
				sdwanCpeAllDetailBean.setCpeAvailability(map.get(ServiceInventoryConstants.CPE_AVAILABILITY));
				sdwanCpeAllDetailBean.setOsVersion(map.get(ServiceInventoryConstants.OS_VERSION));
				sdwanCpeAllDetailBean.setSku(map.get(ServiceInventoryConstants.SKU));
				sdwanCpeAllDetailBean.setManufacturer(map.get(ServiceInventoryConstants.MANUFACTURER));
				sdwanCpeAllDetailBean.setLastUpdateDate(map.get(ServiceInventoryConstants.LAST_UPDATED_DATE));
				Optional<ViewSiServiceInfoAll> sdwanSystemServiceIdOptional = vwSiServiceInfoAllRepository
						.findByServiceId(sdwanCpeAllDetailBean.getSdwanServiceId());

				SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
				sdwanTemplateBean.setTemplateId((Integer) assets.get("templateId"));
				sdwanTemplateBean.setTemplateName((String) assets.get("attributeValue"));
				sdwanCpeAllDetailBean.setTemplate(sdwanTemplateBean);
			}).findAny().get();
		} else
			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		sdwanCpeAllDetailBean.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
		return sdwanCpeAllDetailBean;
	}

	/**
	 * To fetch full details of a SDWAN Template tagged to a customer
	 *
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param templateName
	 * @return
	 */
	public SdwanTemplateDetailBean getSdwanTemplateDetailedInfo(Integer customerId, Integer customerLeId,
			Integer partnerId, String templateName, Integer productId) throws IOException, TclCommonException {
		LOGGER.info("Entering method to fetch template detailed info");
		SdwanTemplateDetailBean sdwanTemplateDetail = new SdwanTemplateDetailBean();

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();

		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);

		List<SIServiceAdditionalInfo> templates = serviceAdditionalInfoRepository
				.findByAttributeNameAndAttributeValue(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME, templateName);
		List<SdwanPolicyBean> sdwanPolicies = new ArrayList<>();
		List<VwServiceAttributesBean> vwServiceAttributeBean = new ArrayList<>();
		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository.findByTemplateNames(
				Collections.singletonList(templateName), customerId, customerIds, partnerId, partnerLeIds);
		List<Integer> overlaySysIds = underlaysOverlaysJoined.stream().map(site -> (Integer) site.get("overlaySysId"))
				.collect(Collectors.toList());

		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				overlaySysIds,Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
						ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));

		Set<SdwanOrgTemplateMapping> sdwanOrgTemplateMappings = mapOverlayAndUnderlayAttributes(serviceAttributes,
				underlaysOverlaysJoined);
		Set<String> instanceRegions = sdwanOrgTemplateMappings.stream().map(SdwanOrgTemplateMapping::getInstanceRegion)
				.collect(Collectors.toSet());
		List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndpoints.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		Set<String> serviceIds = underlaysOverlaysJoined.stream()
				.map(underlay -> (String) underlay.get("overlaySrvcId")).collect(Collectors.toSet());

		List<ViewSiServiceInfoAll> siteInfos = vwSiServiceInfoAllRepository.findByServiceIdInAndServiceStatusNotIn(new ArrayList<>(serviceIds),Arrays.asList(ServiceInventoryConstants.TERMINATED,ServiceInventoryConstants.UNDER__PROVISIONING));
		LOGGER.info("Number of underlaySites : {}", underlaysOverlaysJoined.size());
		List<String> underLayServiceIds = underlaysOverlaysJoined.stream()
				.map(underlaySite -> (String) underlaySite.get("underlaySrvcId")).collect(Collectors.toList());
		List<SIServiceAssetInfo> cpeDetails = serviceAssetInfoRepository.findByServiceIdInAndAssetTag(underLayServiceIds, ServiceInventoryConstants.SDWAN_CPE);
		List<SdwanCpeInfoBean> sdwanCpeInfos = new ArrayList<>();
		
		List<TemplateSiteDetails> templateSites = new ArrayList<>();
		siteInfos.stream().forEach(site -> {
			TemplateSiteDetails templateSiteDetails = new TemplateSiteDetails();
			templateSiteDetails.setServiceDetailId((Integer) site.getId());
			templateSiteDetails.setSdwanServiceId((String) site.getServiceId());
			templateSiteDetails.setSdwanPrimaryServiceId((String) site.getPrimaryServiceId());
			templateSiteDetails.setSdwanPrimaryOrSecondary((String) site.getPrimaryOrSecondary());
			templateSiteDetails.setSdwanPriSecServiceId((String) site.getPrimarySecondaryLink());
			templateSites.add(templateSiteDetails);
		});
		sdwanTemplateDetail.setTemplateName(templateName);
		sdwanTemplateDetail.setTemplateSiteDetails(templateSites);
		sdwanTemplateDetail.setAssociatedSites(new ArrayList<>(serviceIds));
		sdwanTemplateDetail.setAssociatedCpe(sdwanCpeInfos);
		sdwanTemplateDetail.setAssociatedSitesCount(siteInfos.size());
		sdwanTemplateDetail.setTemplateIds(new ArrayList<>());
		templates.forEach(template -> sdwanTemplateDetail.getTemplateIds().add(template.getAttributeSysId()));
		templates.stream().findAny()
				.ifPresent(template -> sdwanTemplateDetail.setTemplateAlias(template.getDisplayValue()));
		// to fetch list policies tagged to template
		if (serviceAttributes != null & !serviceAttributes.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			serviceAttributes.stream().forEach(srvAttribute -> {
				vwServiceAttributeBean.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
			});
			LOGGER.info("Overlay attributes for template {}", vwServiceAttributeBean.toString());
			VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
			Map<String, Set<String>> orgTemplates = new HashMap<>();
			Map<String, Set<String>> orgDirectoryRegions = new HashMap<>();
			vwServiceAttributeBean.stream()
					.filter(vwServiceAttribute -> ServiceInventoryConstants.SDWAN_ORGANISATION_NAME
							.equalsIgnoreCase(vwServiceAttribute.getAttributeName()))
					.forEach(vwServiceAttribute -> {
						orgTemplates.put(vwServiceAttribute.getAttributeValue(), Collections.singleton(templateName));
						orgDirectoryRegions.put(vwServiceAttribute.getAttributeValue(), instanceRegions);
					});
			Map<String, Set<String>> cpeGroupedByTemplateName = getCpeByTemplateFromVersa(instanceByCode, orgTemplates,
					orgDirectoryRegions);
			Map<String, Set<DeviceGroupDatum>> cpeAvailabilityByTemplate = checkTemplateSyncStatusByCpe(
					cpeGroupedByTemplateName, instanceByCode, orgTemplates, orgDirectoryRegions);

			if (Objects.nonNull(versaCpeStatusResponse)
					&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
				Map<String, String> cpeStatusAndAvail = new HashMap<>();
				Set<String> recurringCpes = new HashSet<>();
				cpeDetails.forEach(cpeDetail -> {
					if (!recurringCpes.contains(cpeDetail.getAssetName())) {
						SdwanCpeInfoBean sdwanCpeInfoBean = new SdwanCpeInfoBean();
						sdwanCpeInfoBean.setCpeId(cpeDetail.getId());
						sdwanCpeInfoBean.setCpeName(cpeDetail.getAssetName());
						getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, cpeDetail.getAssetName(),
								cpeStatusAndAvail, null);
						sdwanCpeInfoBean.setCpeStatus(cpeStatusAndAvail.get(ServiceInventoryConstants.CPE_STATUS));
						sdwanCpeInfoBean
								.setCpeAvailability(cpeStatusAndAvail.get(ServiceInventoryConstants.CPE_AVAILABILITY));
						sdwanCpeInfoBean.setUnderlayServiceId(cpeDetail.getServiceId());
						if (Objects.nonNull(cpeAvailabilityByTemplate.get(templateName))
								&& !cpeAvailabilityByTemplate.get(templateName).isEmpty())
							cpeAvailabilityByTemplate.get(templateName).stream().filter(
									cpeGroup -> Objects.nonNull(cpeGroup) && Objects.nonNull(cpeGroup.getDeviceData()))
									.forEach(cpeGroup -> {
										cpeGroup.getDeviceData().stream().filter(Objects::nonNull).forEach(cpe -> {
											if (sdwanCpeInfoBean.getCpeName().equalsIgnoreCase(cpe.getName()))
												sdwanCpeInfoBean.setInSyncStatus(cpe.getStatus());
										});
									});
						sdwanCpeInfos.add(sdwanCpeInfoBean);
						recurringCpes.add(cpeDetail.getAssetName());
					}
				});
			} else
				throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
			getRulesFromVersa(sdwanOrgTemplateMappings, sdwanPolicies, instanceByCode);
			LOGGER.info("Policy details : " + Utils.convertObjectToJson(sdwanPolicies));
			List<SdwanPolicyListBean> sdwanPolicyList = new ArrayList<>();
			if (Objects.nonNull(sdwanPolicies) && !sdwanPolicies.isEmpty()) {
				sdwanPolicies.forEach(sdwanPolicy -> {
					SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
					sdwanTemplateBean.setTemplateName(sdwanPolicy.getTemplateName());
					// for traffic steering policies
					if (Objects.nonNull(sdwanPolicy.getTrafficSteeringPolicies())
							&& Objects.nonNull(sdwanPolicy.getTrafficSteeringPolicies().getRules())
							&& Objects.nonNull(sdwanPolicy.getTrafficSteeringPolicies().getRules().getRule())) {
						sdwanPolicy.getTrafficSteeringPolicies().getRules().getRule().forEach(rule -> {
							SdwanPolicyListBean sdwanPolicyListBean = new SdwanPolicyListBean();
							sdwanPolicyListBean.setPolicyType(ServiceInventoryConstants.TRAFFIC_STEERING);
							sdwanPolicyListBean.setPolicyName(rule.getName());
							sdwanPolicyListBean.setOrganisationName(sdwanPolicy.getOrganisationName());
							sdwanPolicyListBean.setDirectorRegion(sdwanPolicy.getInstanceRegion());
							sdwanPolicyListBean.setAssociatedTemplate(sdwanTemplateBean);
							// find associated url and app count
							if (Objects.nonNull(rule.getMatch())) {
								if (Objects.nonNull(rule.getMatch().getApplication()))
									sdwanPolicyListBean.setAssociatedAppCount(returnSizeIfNotNull(
											rule.getMatch().getApplication().getPredefinedApplicationList())
											+ returnSizeIfNotNull(
													rule.getMatch().getApplication().getUserDefinedApplicationList()));
								if (Objects.nonNull(rule.getMatch().getUrlCategory()))
									sdwanPolicyListBean.setAssociatedUrlCount(returnSizeIfNotNull(
											rule.getMatch().getUrlCategory().getPredefined())
											+ returnSizeIfNotNull(rule.getMatch().getUrlCategory().getUserDefined()));
							}
							sdwanPolicyList.add(sdwanPolicyListBean);
						});
					}
					// for qos policies
					if (Objects.nonNull(sdwanPolicy.getQosPolicy())
							&& Objects.nonNull(sdwanPolicy.getQosPolicy().getAppQosPolicy())) {
						sdwanPolicy.getQosPolicy().getAppQosPolicy().forEach(qosPolicy -> {
							SdwanPolicyListBean sdwanPolicyListBean = new SdwanPolicyListBean();
							sdwanPolicyListBean.setPolicyType(ServiceInventoryConstants.QOS);
							sdwanPolicyListBean.setPolicyName(qosPolicy.getName());
							sdwanPolicyListBean.setOrganisationName(sdwanPolicy.getOrganisationName());
							sdwanPolicyListBean.setDirectorRegion(sdwanPolicy.getInstanceRegion());
							sdwanPolicyListBean.setAssociatedTemplate(sdwanTemplateBean);
							// find associated url and app count
							if (Objects.nonNull(qosPolicy.getMatch())) {
								if (Objects.nonNull(qosPolicy.getMatch().getApplication()))
									sdwanPolicyListBean.setAssociatedAppCount(returnSizeIfNotNull(
											qosPolicy.getMatch().getApplication().getPredefinedApplicationList())
											+ returnSizeIfNotNull(qosPolicy.getMatch().getApplication()
													.getUserDefinedApplicationList()));
								if (Objects.nonNull(qosPolicy.getMatch().getUrlCategory()))
									sdwanPolicyListBean.setAssociatedUrlCount(
											returnSizeIfNotNull(qosPolicy.getMatch().getUrlCategory().getPredefined())
													+ returnSizeIfNotNull(
															qosPolicy.getMatch().getUrlCategory().getUserDefined()));
							}
							sdwanPolicyList.add(sdwanPolicyListBean);
						});
					}
					if (Objects.nonNull(sdwanPolicy.getFirewallPolicyConfig())
							&& Objects.nonNull(sdwanPolicy.getFirewallPolicyConfig().getAccessPolicy())) {
						sdwanPolicy.getFirewallPolicyConfig().getAccessPolicy().forEach(firewall -> {
							SdwanPolicyListBean sdwanPolicyListBean = new SdwanPolicyListBean();
							sdwanPolicyListBean.setPolicyType(ServiceInventoryConstants.FIREWALL);
							sdwanPolicyListBean.setPolicyName(firewall.getName());
							sdwanPolicyListBean.setOrganisationName(sdwanPolicy.getOrganisationName());
							sdwanPolicyListBean.setDirectorRegion(sdwanPolicy.getInstanceRegion());
							sdwanPolicyListBean.setAssociatedTemplate(sdwanTemplateBean);
							// find associated url and app count
							if (Objects.nonNull(firewall.getMatch())) {
								if (Objects.nonNull(firewall.getMatch().getApplication()))
									sdwanPolicyListBean.setAssociatedAppCount(returnSizeIfNotNull(
											firewall.getMatch().getApplication().getPredefinedApplicationList())
											+ returnSizeIfNotNull(firewall.getMatch().getApplication()
													.getUserDefinedApplicationList()));
								if (Objects.nonNull(firewall.getMatch().getUrlCategory()))
									sdwanPolicyListBean.setAssociatedUrlCount(
											returnSizeIfNotNull(firewall.getMatch().getUrlCategory().getPredefined())
													+ returnSizeIfNotNull(
															firewall.getMatch().getUrlCategory().getUserDefined()));
							}
							sdwanPolicyList.add(sdwanPolicyListBean);
						});
					}
					
				});
				LOGGER.info("sdwanPolicyList: " + sdwanPolicyList);
				sdwanTemplateDetail.setSdwanPolicies(sdwanPolicyList);
			}
		}
		sdwanTemplateDetail.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
		return sdwanTemplateDetail;
	}

	/**
	 * Get SDWAN Template list (Paginated)
	 *
	 * @param page
	 * @param size
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param productId
	 * @return
	 */
	public PagedResultWithTimestamp<TemplateDetails> getSdwanTemplateDetails(Integer page, Integer size,
			Integer customerId, Integer customerLeId, Integer partnerId, Integer productId)
			throws IOException, TclCommonException {
		LOGGER.info("Entering method to fetch template list");

		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();

		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
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
				underlaySysIds, ServiceInventoryConstants.SDWAN_TEMPLATE_NAME, PageRequest.of(page - 1, size));
		List<String> templateNames = distinctTemplates.stream().map(template -> (String) template.get("name"))
				.collect(Collectors.toList());
		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository
				.findByTemplateNames(templateNames, customerId, customerIds, partnerId, partnerLeIds);
		Set<Integer> overlaySysIds = underlaysOverlaysJoined.stream()
				.map(underlaysOverlays -> (Integer) underlaysOverlays.get("overlaySysId")).collect(Collectors.toSet());
		Set<String> underLayServiceIds = underlaysOverlaysJoined.stream()
				.map(underlaysOverlays -> (String) underlaysOverlays.get("underlaySrvcId")).collect(Collectors.toSet());
		List<SIServiceAssetInfo> cpeDetails = serviceAssetInfoRepository
				.findByServiceIdInAndAssetTag(new ArrayList<>(underLayServiceIds), ServiceInventoryConstants.SDWAN_CPE);
		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				new ArrayList<>(overlaySysIds), Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
						ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
		List<VwServiceAttributesBean> vwServiceAttributeBean = new ArrayList<>();
		if (serviceAttributes != null & !serviceAttributes.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			serviceAttributes.stream().forEach(srvAttribute -> {
				vwServiceAttributeBean.add(mapper.convertValue(srvAttribute, VwServiceAttributesBean.class));
			});
		}
		LOGGER.info("Overlay attributes for template {}", vwServiceAttributeBean.toString());
		List<TemplateDetails> templateDetails = new ArrayList<>();
		Map<String, String> cpeData = new HashMap<>();
		Set<String> instanceRegions = new HashSet<>();
		vwServiceAttributeBean.stream().filter(
				attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
				.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		// fetch cpe information (status,technical info) according to instance region
		VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
		if (Objects.nonNull(versaCpeStatusResponse)
				&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
			if (Objects.nonNull(underlaysOverlaysJoined) && !underlaysOverlaysJoined.isEmpty()) {
				Set<String> recurringTemplates = new HashSet<>();
				underlaysOverlaysJoined.forEach(template -> {
					Set<SdwanCpeDetails> sdwanCpeDetails = new HashSet<>();
					cpeDetails.stream()
							.filter(cpe -> cpe.getServiceId().equalsIgnoreCase((String) template.get("underlaySrvcId")) && StringUtils.isNotBlank(cpe.getAssetName()))
							.peek(cpeDetail -> {
								SdwanCpeDetails sdwanCpeDetail = new SdwanCpeDetails();
								sdwanCpeDetail.setCpeAtributeid(Arrays.asList(cpeDetail.getId()));
								sdwanCpeDetail.setCpeName(cpeDetail.getAssetName());
								getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCpeDetail.getCpeName(),
										cpeData, null);
								sdwanCpeDetail.setCpeStatus(cpeData.get(ServiceInventoryConstants.CPE_STATUS));
								sdwanCpeDetail
										.setCpeAvailability(cpeData.get(ServiceInventoryConstants.CPE_AVAILABILITY));
								LOGGER.info("checking sdwanCpeDetail.setSdwanServiceId {} ",vwSiServiceInfoAllRepository
										.findById(cpeDetail.getServiceSystemId()).get().getIzoSdwanSrvcId());
								sdwanCpeDetail.setUnderlayServiceId(cpeDetail.getServiceId());
								sdwanCpeDetail.setSdwanServiceId(vwSiServiceInfoAllRepository
										.findById(cpeDetail.getServiceSystemId()).get().getIzoSdwanSrvcId());
								sdwanCpeDetails.add(sdwanCpeDetail);
							}).anyMatch(cpe -> cpe.getServiceId()
									.equalsIgnoreCase((String) template.get("underlaySrvcId")));
					if (Objects.nonNull(underlaySites) && !underlaySites.isEmpty()) {
						sdwanCpeDetails.forEach(sdwanCPE -> {
							List<NetworkSiteDetails> networkSiteDetails = new ArrayList<>();
							underlaySites.stream()
									.filter(underlayService -> sdwanCPE.getSdwanServiceId()
											.equals(((String) underlayService.get("sdwan_id"))))
									.forEach(underlayService -> {
										NetworkSiteDetails networkSiteDetail = new NetworkSiteDetails();
										networkSiteDetail
												.setNetworkSiteAlias((String) underlayService.get("site_alias"));
										networkSiteDetail
												.setNetworkSiteServiceId((String) underlayService.get("service_id"));
										networkSiteDetail.setSiteName((String) underlayService.get("service_id"));
										networkSiteDetail.setNetworkSiteSrcCountry(
												(String) underlayService.get("source_country"));
										networkSiteDetail
												.setNetworkSiteSrcCity((String) underlayService.get("source_city"));
										networkSiteDetail.setNetworkSiteDestCountry(
												(String) underlayService.get("destination_country"));
										networkSiteDetail.setNetworkSiteDestCity(
												(String) underlayService.get("destination_city"));
										networkSiteDetails.add(networkSiteDetail);
									});
							sdwanCPE.setNetworkSiteDetails(networkSiteDetails);
						});
					}
				
					if (!recurringTemplates.contains(template.get("attributeValue"))) {
						LOGGER.info("sdwanCpeDetails.size() {}", sdwanCpeDetails.size());
						TemplateDetails templateDetail = new TemplateDetails();
						templateDetail.setAttributeId(new ArrayList<>());
						templateDetail.getAttributeId().add((Integer) template.get("templateId"));
						templateDetail.setTemplateName((String) template.get("attributeValue"));
						templateDetail.setSdwanCpeDetails(sdwanCpeDetails);
						templateDetail.setTemplateAlias((String) template.get("dispVal"));
						templateDetail.setSdwanServiceId(new HashSet<>());
						if (Objects.nonNull(template.get("overlaySrvcId")))
							templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
						templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
						templateDetails.add(templateDetail);
						recurringTemplates.add(templateDetail.getTemplateName());
					} else {
						LOGGER.info("sdwanCpeDetails.size() {}", sdwanCpeDetails.size());
						templateDetails.stream().filter(templateDetail -> ((String) template.get("attributeValue"))
								.equalsIgnoreCase(templateDetail.getTemplateName())).peek(templateDetail -> {
									templateDetail.getSdwanCpeDetails().addAll(sdwanCpeDetails);
									templateDetail.getAttributeId().add((Integer) template.get("templateId"));
									if (Objects.nonNull(template.get("overlaySrvcId")))
										templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
									templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
								}).anyMatch(templateDetail -> ((String) template.get("attributeValue"))
										.equalsIgnoreCase(templateDetail.getTemplateName()));
					}
				});
			}
		} else
			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		PagedResultWithTimestamp<TemplateDetails> templateInfoPagedResult = new PagedResultWithTimestamp<TemplateDetails>(
				templateDetails, distinctTemplates.getTotalElements(), distinctTemplates.getTotalPages(),
				ServiceInventoryUtils.dateConvertor(new Date()));
		return templateInfoPagedResult;
	}

	/**
	 * Method to search and sort Template details
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedResultWithTimestamp<TemplateDetails> searchBasedOnTemplateNames(SiteDetailsSearchRequest request)
			throws TclCommonException {
		LOGGER.info("Entering Template search and sort MDC token {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		List<TemplateDetails> templateDetailsList = new ArrayList<>();
		Long totalItems = null ;
		Integer totalPages = null;
		if(request.getProductId()==null || request.getPage()==null || request.getSize()<=0 ) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
     }
     try {
			LOGGER.info("Template search and sort request {}", request.toString());
			List<Integer> underlaySysId = new ArrayList<>();
			getCustomerAndPartnerLeIds(customerIds, partnerLeIds, request.getCustomerLeId());
			// LOGGER.info("CustomerIds"+custome
			// nnnnCustomerIdrIds+""+customerId+"PartnerId"+partnerId);
			LOGGER.info("Template FILTER CustomerID :: {}", request.getCustomerId());
			LOGGER.info("Template FILTER CustomerLeIDs :: {}", customerIds);
			LOGGER.info("Template FILTER PartnerId :: {}", request.getPartnerId());
			LOGGER.info("Template FILTER PartnerLeIds :: {}", partnerLeIds);

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
							underlaySysIds, ServiceInventoryConstants.SDWAN_TEMPLATE_NAME, request.getSearchText(),
							PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(sortColumn).descending()));
				else
					uniqueTemplateNames = serviceAdditionalInfoRepository.findBySysIdAndSearchTextPaginated(
							underlaySysIds, ServiceInventoryConstants.SDWAN_TEMPLATE_NAME, request.getSearchText(),
							PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(sortColumn).ascending()));
				totalItems = uniqueTemplateNames.getTotalElements();
				totalPages = (int)(totalItems % request.getSize()>0? (totalItems/request.getSize())+1 : totalItems/request.getSize());
				if (Objects.nonNull(uniqueTemplateNames) && !uniqueTemplateNames.getContent().isEmpty()) {
					List<String> templateNames = uniqueTemplateNames.stream().map(template -> (String) template.get("name"))
							.collect(Collectors.toList());
					List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository
							.findByTemplateNames(templateNames, request.getCustomerId(), customerIds, request.getPartnerId(), partnerLeIds);
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
											ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
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
							templateDetailsList.sort(Comparator.comparing(TemplateDetails::getTemplateAlias,
									Comparator.nullsLast(Comparator.reverseOrder())));
						} else {
							templateDetailsList.sort(Comparator.comparing(TemplateDetails::getTemplateAlias,
									Comparator.nullsLast(Comparator.naturalOrder())));
						}
					} else {
						if ("desc".equalsIgnoreCase(request.getSortOrder())) {
							templateDetailsList.sort(Comparator.comparing(TemplateDetails::getTemplateName,
									Comparator.nullsLast(Comparator.reverseOrder())));
						} else {
							templateDetailsList.sort(Comparator.comparing(TemplateDetails::getTemplateName,
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

	/**
	 * Construct template details bean
	 *
	 * @param templateDetails
	 * @param vwServiceAttributeBean
	 * @param underlaysOverlaysJoined
	 * @param cpeDetails
	 * @param underlaySites
	 * @throws TclCommonException
	 * @throws IOException
	 */
	private void constructTemplateDetails(List<TemplateDetails> templateDetails,
			List<VwServiceAttributesBean> vwServiceAttributeBean, List<Map<String, Object>> underlaysOverlaysJoined,
			List<SIServiceAssetInfo> cpeDetails, List<Map<String, Object>> underlaySites)
			throws TclCommonException, IOException {
		Map<String, String> cpeData = new HashMap<>();
		Set<String> instanceRegions = new HashSet<>();
		vwServiceAttributeBean.stream().filter(
				attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
				.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		// fetch cpe information (status,technical info) according to instance region
		VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
		if (Objects.nonNull(versaCpeStatusResponse)
				&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
			if (Objects.nonNull(underlaysOverlaysJoined) && !underlaysOverlaysJoined.isEmpty()) {
				Set<String> recurringTemplates = new HashSet<>();
				underlaysOverlaysJoined.forEach(template -> {
					Set<SdwanCpeDetails> sdwanCpeDetails = new HashSet<>();
					cpeDetails.stream()
							.filter(cpe -> cpe.getServiceId().equalsIgnoreCase((String) template.get("underlaySrvcId")) && StringUtils.isNotBlank(cpe.getAssetName()))
							.peek(cpeDetail -> {
								SdwanCpeDetails sdwanCpeDetail = new SdwanCpeDetails();
								sdwanCpeDetail.setCpeAtributeid(Arrays.asList(cpeDetail.getId()));
								sdwanCpeDetail.setCpeName(cpeDetail.getAssetName());
								getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCpeDetail.getCpeName(),
										cpeData, null);
								sdwanCpeDetail.setCpeStatus(cpeData.get(ServiceInventoryConstants.CPE_STATUS));
								sdwanCpeDetail
										.setCpeAvailability(cpeData.get(ServiceInventoryConstants.CPE_AVAILABILITY));
								sdwanCpeDetail.setUnderlayServiceId(cpeDetail.getServiceId());
								sdwanCpeDetails.add(sdwanCpeDetail);
							}).anyMatch(cpe -> cpe.getServiceId()
									.equalsIgnoreCase((String) template.get("underlaySrvcId")));
					if (!recurringTemplates.contains(template.get("attributeValue"))) {
						TemplateDetails templateDetail = new TemplateDetails();
						templateDetail.setAttributeId(new ArrayList<>());
						templateDetail.getAttributeId().add((Integer) template.get("templateId"));
						templateDetail.setTemplateName((String) template.get("attributeValue"));
						templateDetail.setSdwanCpeDetails(sdwanCpeDetails);
						templateDetail.setTemplateAlias((String) template.get("dispVal"));
						templateDetail.setSdwanServiceId(new HashSet<>());
						if (Objects.nonNull(template.get("overlaySrvcId")))
							templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
						templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
						templateDetails.add(templateDetail);
						recurringTemplates.add(templateDetail.getTemplateName());
					} else {
						templateDetails.stream().filter(templateDetail -> ((String) template.get("attributeValue"))
								.equalsIgnoreCase(templateDetail.getTemplateName())).peek(templateDetail -> {
									templateDetail.getSdwanCpeDetails().addAll(sdwanCpeDetails);
									templateDetail.getAttributeId().add((Integer) template.get("templateId"));
									if (Objects.nonNull(template.get("overlaySrvcId")))
										templateDetail.getSdwanServiceId().add((String) template.get("overlaySrvcId"));
									templateDetail.setAssociatedSitesCount(templateDetail.getSdwanServiceId().size());
								}).anyMatch(templateDetail -> ((String) template.get("attributeValue"))
										.equalsIgnoreCase(templateDetail.getTemplateName()));
					}
					
				});
			}
		} else
			throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
	}

	/**
	 * To fetch unique template names based on filters
	 *
	 * @param request
	 * @param sortDirection
	 * @param customerIds
	 * @param partnerLeIds
	 * @return
	 */
	private List<Map<String, Object>> getUniqueTemplateNamesBasedOnFilter(SiteDetailsSearchRequest request,
			String sortDirection, List<Integer> customerIds, List<Integer> partnerLeIds) {
		List<Map<String, Object>> uniqueTemplateNames;
		if (!StringUtils.isAllBlank(request.getSortBy())
				&& ServiceInventoryConstants.ALIAS.equalsIgnoreCase(request.getSortBy())) {
			if (request.getPage() == -1) {
				if (sortDirection.equalsIgnoreCase("desc"))
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueOrderByAliasDesc(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
				else
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueOrderByAliasAsc(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
			} else {
				int page = (request.getPage() - 1) * request.getSize();
				if (sortDirection.equalsIgnoreCase("desc"))
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueLimitAliasDesc(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), page, request.getSize(),
							ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
				else
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueAliasLimit(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), page, request.getSize(),
							ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
			}
		} else {
			if (request.getPage() == -1) {
				if (sortDirection.equalsIgnoreCase("desc"))
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueOrderByDesc(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
				else
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueOrderByAsc(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
			} else {
				int page = (request.getPage() - 1) * request.getSize();
				if (sortDirection.equalsIgnoreCase("desc"))
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueLimitDesc(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), page, request.getSize(),
							ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
				else
					uniqueTemplateNames = vwSiServiceInfoAllRepository.findDistinctTemplateAttrValueLimit(
							request.getSearchText(), request.getCustomerId(), customerIds, request.getPartnerId(),
							partnerLeIds, request.getProductId(), page, request.getSize(),
							ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
			}
		}
		return uniqueTemplateNames;
	}

	/**
	 * Methd to get Qos and SDWAN rules from VERSA
	 * @param vwServiceAttributeBean
	 * @param sdwanPolicies
	 */
	private void getRulesFromVersa(List<VwServiceAttributesBean> vwServiceAttributeBean, List<SdwanPolicyBean> sdwanPolicies) {
		LOGGER.info("Entering getRulesFromVersa to fetch rules and MDC token {} ",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped = vwServiceAttributeBean.stream().collect(Collectors.groupingBy(VwServiceAttributesBean::getSysId));
//						vwServiceAttributeBean.stream().collect(Collectors.groupingBy(VwServiceAttributesBean::getSysId));
		Set<String> instanceRegions = new HashSet<>();
		Set<String> serverIps = new HashSet<>();
		Set<String> templates = new HashSet<>();
		serviceAttrGrouped.entrySet().forEach(attr->{
			attr.getValue().stream().forEach(attrValue->{
				if(attrValue.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
					if(attrValue.getAttributeValue() != null)
						instanceRegions.add(attrValue.getAttributeValue());
				}
			});
		});
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode =  sdwanEndPointsForRegions.stream().collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));// TODO Need to change map values in same case- uppercase.
		LOGGER.info("Sdwan endpoint for server codes in {} ",instanceByCode.keySet());
		serviceAttrGrouped.entrySet().forEach(map->{
			List<String> templateName = new ArrayList<>();
			List<String> orgName = new ArrayList<>();
			List<String> instanceCode = new ArrayList<>();
			map.getValue().stream().forEach(value->{
				if(value.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME)) {
					orgName.add(0,value.getAttributeValue());
				} else if(value.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME)) {
					templateName.add(0,value.getAttributeValue());
				} else if(value.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
					instanceCode.add(0, value.getAttributeValue());
				}
			});
			String qosRulesUrl = versaGetQosPolicyUrl;
			String sdwanRuleUrl = versaGetSdwanPolicyUrl;

			if (!templateName.isEmpty() && !orgName.isEmpty() && instanceByCode.containsKey(instanceCode.get(0))) {
				SdwanEndpoints sdwanEndpoints = instanceByCode.get(instanceCode.get(0)).get(0);
				String url = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort();
				LOGGER.info("Fetching policies for templateName {} and orgName {} and instance region {} ",
						templateName.get(0), orgName.get(0), instanceCode.get(0));
				qosRulesUrl = qosRulesUrl.replace("DYNAMICTEMPLATENAME", templateName.get(0));
				qosRulesUrl = qosRulesUrl.replace("DYNAMICORGNAME", orgName.get(0));
				sdwanRuleUrl = sdwanRuleUrl.replace("DYNAMICTEMPLATENAME", templateName.get(0));
				sdwanRuleUrl = sdwanRuleUrl.replace("DYNAMICORGNAME", orgName.get(0));
				try {
						SdwanPolicyBean sdwanPolicyBean = new SdwanPolicyBean();
					if (!templates.contains(templateName.get(0)) || !serverIps.contains(sdwanEndpoints.getServerIp())) {
						sdwanPolicies.add(sdwanPolicyBean);
						sdwanPolicyBean.setTemplateName(templateName.get(0));
						sdwanPolicyBean.setInstanceRegion(sdwanEndpoints.getServerCode());
						sdwanPolicyBean.setOrganisationName(orgName.get(0));
						LOGGER.info("Consuming Versa URL to fetch QOS policy {} ", url + qosRulesUrl);
						RestResponse qosPolicyResponse = restClientService.getWithBasicAuthentication(url + qosRulesUrl,
								new HashMap<String, String>(), sdwanEndpoints.getServerUsername(),
								sdwanEndpoints.getServerPassword());
						LOGGER.info("qosPolicyResponse {} ", qosPolicyResponse.getData());
						if (qosPolicyResponse.getStatus() == Status.SUCCESS && qosPolicyResponse.getData() != null) {
							QosPolicy qosPolicy = Utils.convertJsonToObject(qosPolicyResponse.getData(),
									QosPolicy.class);
							sdwanPolicyBean.setQosPolicy(qosPolicy);
							if (Objects.isNull(qosPolicy))
								sdwanPolicyBean.setQosPolicyCount(0);
							else
								sdwanPolicyBean.setQosPolicyCount(qosPolicy.getAppQosPolicy().size());
						}

						LOGGER.info("Consuming Versa URL to fetch Traffic Steering policy {} ", url + sdwanRuleUrl);
						RestResponse trafficStreeringResponse = restClientService.getWithBasicAuthentication(
								url + sdwanRuleUrl, new HashMap<String, String>(), sdwanEndpoints.getServerUsername(),
								sdwanEndpoints.getServerPassword());
						LOGGER.info("trafficStreeringResponse {} ", qosPolicyResponse.getData());
						if (trafficStreeringResponse.getStatus() == Status.SUCCESS
								&& trafficStreeringResponse.getData() != null) {
							TrafficSteeringPolicy trafficSteeringPolicy = Utils.convertJsonToObject(
									trafficStreeringResponse.getData(), TrafficSteeringPolicy.class);
							sdwanPolicyBean.setTrafficSteeringPolicies(trafficSteeringPolicy);
							if (Objects.isNull(trafficSteeringPolicy.getRules())
									|| Objects.isNull(trafficSteeringPolicy.getRules().getRule()))
								sdwanPolicyBean.setTrafficSteeringPolicyCount(0);
							else
								sdwanPolicyBean.setTrafficSteeringPolicyCount(
										trafficSteeringPolicy.getRules().getRule().size());
						}
						templates.add(templateName.get(0));
						serverIps.add(sdwanEndpoints.getServerIp());
					}
				} catch (Exception e) {
					LOGGER.error("Exception while parsing user defines applications from versa", e);
				}
			}
		});
	}

	/**
	 * Method to construct templateDetails
	 * 
	 * @param templateDetailsList
	 * @param vwSiInfoUnderlay
	 * @param overlaySeriveDetails
	 * @param templateSiDetail
	 * @param underlayAssets
	 * @param vwServiceAttributeBean
	 * @param tempNameAndAlias
	 * @throws TclCommonException
	 */
	private void constructTemplateDetails(List<TemplateDetails> templateDetailsList,
			List<ViewSiServiceInfoAllBean> vwSiInfoUnderlay, List<ViewSiServiceInfoAll> overlaySeriveDetails,
			Map<String, List<Integer>> templateSiDetail, List<Map<String, Object>> underlayAssets,
			List<SdwanPolicyBean> sdwanPolicies, List<VwServiceAttributesBean> vwServiceAttributeBean,
			Map<String, String> tempNameAndAlias) throws TclCommonException {
		try {
			Set<String> instanceRegions = new HashSet<>();
			vwServiceAttributeBean.stream().filter(
					attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
					.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
			List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
					.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
			Map<String, String> cpeData = new HashMap<>();
			VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
			List<Boolean> versaCpeDataAvailable = new ArrayList<>();
			if (Objects.nonNull(versaCpeStatusResponse) && Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
				versaCpeDataAvailable.add(0, true);
			} else
				versaCpeDataAvailable.add(0, false);
			templateSiDetail.entrySet().stream().forEach(temp->{
				   TemplateDetails templateDetails = new TemplateDetails();
				   List<TemplateSiteDetails> templateSiteDetails = new ArrayList<>();
				   templateDetails.setTemplateSiteDetails(templateSiteDetails);
				Set<SdwanCpeDetails> sdwanCpeDetails = new HashSet<>();
				   templateDetails.setSdwanCpeDetails(sdwanCpeDetails);
				   templateDetailsList.add(templateDetails);
				   templateDetails.setTemplateName(temp.getKey());
				   templateDetails.setAssociatedSitesCount(temp.getValue().size());
				   templateDetails.setTemplateAlias(tempNameAndAlias.get(temp.getKey()));
				   sdwanPolicies.stream().forEach(policy->{
					   if(policy.getTemplateName().equalsIgnoreCase(temp.getKey())) {
						   templateDetails.setAssociatedRulesCount(policy.getQosPolicyCount()+ policy.getTrafficSteeringPolicyCount());
					   }
				   });
				   overlaySeriveDetails.stream().forEach(overlay->{
					   temp.getValue().stream().forEach(sysId->{
						   if(overlay.getId().equals(sysId)) {
							   TemplateSiteDetails overlaySiteDetail = new TemplateSiteDetails();
							   templateSiteDetails.add(overlaySiteDetail);
							   overlaySiteDetail.setSdwanServiceId(overlay.getServiceId());
							   overlaySiteDetail.setCountry(overlay.getSourceCountry());
						       overlaySiteDetail.setErfCustomerId(overlay.getOrderCustomerId());
						       overlaySiteDetail.setServiceDetailId(overlay.getId());
						       overlaySiteDetail.setSdwanServiceId(overlay.getServiceId());
						       overlaySiteDetail.setSdwanSiteAlias(overlay.getSiteAlias());
						       overlaySiteDetail.setCity(overlay.getSourceCity());
						       overlaySiteDetail.setSdwanFamilyName(overlay.getProductFamilyName());
						       overlaySiteDetail.setSdwanFamilyId(overlay.getProductFamilyId());
						       overlaySiteDetail.setSdwanOfferingName(overlay.getProductOfferingName());
							   overlaySiteDetail.setSdwanPrimaryServiceId(overlay.getPrimaryServiceId());
							   overlaySiteDetail.setSdwanPrimaryOrSecondary(overlay.getPrimaryOrSecondary());
							   overlaySiteDetail.setSdwanPriSecServiceId(overlay.getPrimarySecondaryLink());
							   
							   vwSiInfoUnderlay.stream().forEach(underlaySite->{
								   if(overlay.getServiceId().equalsIgnoreCase(underlaySite.getIzoSdwanSrvcId())) {
										   SdwanCpeDetails sdwanCpeDetail = new SdwanCpeDetails();
											List<NetworkSiteDetails> networkSiteDetails = new ArrayList<>();
											NetworkSiteDetails networkSiteDetail = new NetworkSiteDetails();
											networkSiteDetails.add(networkSiteDetail);
											sdwanCpeDetail.setNetworkSiteDetails(networkSiteDetails);
											LOGGER.info("Processing Network serviceId {} ",underlaySite.getServiceId());
											networkSiteDetail.setNetworkSiteServiceId(underlaySite.getServiceId());
											networkSiteDetail.setNetworkSiteAlias(underlaySite.getSiteAlias());
											networkSiteDetail.setNetworkSiteDestCity(underlaySite.getDestinationCity());
											networkSiteDetail.setNetworkSiteDestCountry(underlaySite.getDestinationCountry());
											networkSiteDetail.setNetworkSiteSrcCity(underlaySite.getSourceCity());
											networkSiteDetail.setNetworkSiteSrcCountry(underlaySite.getSourceCountry());
											networkSiteDetail.setIzoSdwanServiceId(underlaySite.getIzoSdwanSrvcId());
											underlayAssets.stream().forEach(map->{
												if(underlaySite.getSysId().equals(map.get("sysId"))){
													LOGGER.info("Processing CPE name {} and service id {} ",map.get("assetType"), overlay.getServiceId());
													sdwanCpeDetail.setCpeName((String) map.get("assetName"));
											getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse,
													sdwanCpeDetail.getCpeName(), cpeData, null);
													 if(versaCpeDataAvailable.get(0)) {
														sdwanCpeDetail.setCpeAvailability(cpeData.get(ServiceInventoryConstants.CPE_AVAILABILITY));
														sdwanCpeDetail.setCpeStatus(cpeData.get(ServiceInventoryConstants.CPE_STATUS));
													 }
													sdwanCpeDetail.setCpeAtributeid(Arrays.asList((Integer) map.get("assestSysId")));
													sdwanCpeDetail.setUnderlayServiceId(underlaySite.getServiceId());
													sdwanCpeDetail.setSdwanServiceId(underlaySite.getIzoSdwanSrvcId());
												}
											});								
											sdwanCpeDetails.add(sdwanCpeDetail);
										
								   }
							   });
						   }
					   }); 
				   });
			   });
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Get status of all CPEs from Versa
	 *
	 * @return
	 * @throws IOException
	 */
	public VersaCpeStatusResponse getCpeStatusDetailsFromVersa(int retryCount,
			Map<String, List<SdwanEndpoints>> instanceByCode) throws IOException, TclCommonException {
		VersaCpeStatusResponse versaCpeStatus = new VersaCpeStatusResponse();
		try {
			List<Appliance> appliances = new ArrayList<>();
			instanceByCode.values().forEach(instances -> {
				instances.forEach(instance -> {
					String cpeDetailsUrl = cpeStatusUrl;
					cpeDetailsUrl = instance.getServerIp() + ":" + instance.getServerPort() + cpeDetailsUrl;
					LOGGER.info("Cpe status versa URL : {}", cpeDetailsUrl);
					RestResponse restResponse = restClientService.getWithBasicAuthentication(cpeDetailsUrl,
							new HashMap<String, String>(), instance.getServerUsername(), instance.getServerPassword());
					ObjectMapper ob = new ObjectMapper();
					ob.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
					if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
						VersaCpeStatusResponse versaCpeStatusResponse = new VersaCpeStatusResponse();
						try {
							versaCpeStatusResponse = Utils.convertJsonToObject(restResponse.getData(), VersaCpeStatusResponse.class);
						} catch (Exception e) {
							LOGGER.info("Error occurred while fetching versa cpe status {}", e.getMessage());
						}
						if (Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
							appliances.addAll(versaCpeStatusResponse.getApplianceStatusResult().getAppliances());
						}
					}
				});
				versaCpeStatus.setApplianceStatusResult(new VersanmsApplianceStatusResult());
				versaCpeStatus.getApplianceStatusResult().setAppliances(appliances);
				List<String> controller=new ArrayList<>();
				versaCpeStatus.getApplianceStatusResult().getAppliances().stream().
				filter(app->app.getType().equalsIgnoreCase("Controller")).forEach(appl->{
					controller.add(appl.getName());
				});
				versaCpeStatus.getApplianceStatusResult().getAppliances().forEach(appl->{
					appl.setControllers(controller);
				});
				versaCpeStatus.getApplianceStatusResult().setTotalCount(appliances.size());
			});
			LOGGER.info("Entire CPE list after fetching from multiple endpoints {}",
					Utils.convertObjectToJson(versaCpeStatus));
		} catch (Exception e) {
			LOGGER.info("Error while receiving cpe status...retrying " + retryCount);
//			if (retryCount <= 1)
//				getCpeStatusDetailsFromVersa(++retryCount, instanceByCode);
//			else
				throw new TclCommonException(ExceptionConstants.VERSA_CPE_STATUS_API_ERROR);
		}
		return versaCpeStatus;
	}

	/**
	 * Get CPE Status and availability
	 *
	 * @param versaCpeStatusResponse
	 * @param cpeName
	 * @return
	 */
	private void getSdwanCpeInfoFromVersaResponse(VersaCpeStatusResponse versaCpeStatusResponse, String cpeName,
			Map<String, String> cpeStatusAndAvailablity, List<String> controllers) {
		LOGGER.info("Inside getSdwanCpeInfoFromVersaResponse CPE {} and controllers {} ", cpeName, controllers);
		cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_STATUS, ServiceInventoryConstants.OFFLINE);
		cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_AVAILABILITY, ServiceInventoryConstants.UNAVAILABLE);
		versaCpeStatusResponse.getApplianceStatusResult().getAppliances().stream()
				.filter(appliance -> cpeName.equalsIgnoreCase(appliance.getName())).peek(appliance -> {
					// checking ping-status for cpe status
					if (appliance.getPingStatus().equals(ServiceInventoryConstants.REACHABLE))
						cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_STATUS,
								ServiceInventoryConstants.ONLINE);
					// checking sync-status for cpe availability
					if (appliance.getSyncStatus().equals(ServiceInventoryConstants.IN_SYNC))
						cpeStatusAndAvailablity.put(ServiceInventoryConstants.CPE_AVAILABILITY,
								ServiceInventoryConstants.AVAILABLE);
					cpeStatusAndAvailablity.put(ServiceInventoryConstants.OS_VERSION, appliance.getSoftwareVersion());
					if (Objects.nonNull(appliance.getHardware())) {
						cpeStatusAndAvailablity.put(ServiceInventoryConstants.SKU, appliance.getHardware().getSku());
						cpeStatusAndAvailablity.put(ServiceInventoryConstants.MANUFACTURER, appliance.getHardware().getManufacturer());
					}	
					if (Objects.nonNull(appliance.getControllers()) && appliance.getControllers()!=null && controllers!=null)
						controllers.addAll(appliance.getControllers());
					cpeStatusAndAvailablity.put(ServiceInventoryConstants.LAST_UPDATED_DATE, appliance.getLastUpdatedTime());
					
				}).anyMatch(appliance -> cpeName.equalsIgnoreCase(appliance.getName()));
	}

	/**
	 * Get CPE Status
	 *
	 * @param versaCpeStatusResponse
	 * @param cpeName
	 * @return
	 */
	private String getSdwanCpeStatus(VersaCpeStatusResponse versaCpeStatusResponse, String cpeName) {
		final String[] status = new String[1];
		versaCpeStatusResponse.getApplianceStatusResult().getAppliances().stream()
				.filter(appliance -> cpeName.equalsIgnoreCase(appliance.getName())).peek(appliance -> {
					// checking ping-status for cpe status
					if (appliance.getPingStatus().equals(ServiceInventoryConstants.REACHABLE))
						status[0] = ServiceInventoryConstants.ONLINE;
					else
						status[0] = ServiceInventoryConstants.OFFLINE;
				}).anyMatch(appliance -> cpeName.equalsIgnoreCase(appliance.getName()));
		return status[0] != null ? status[0] : ServiceInventoryConstants.OFFLINE;
	}
	
	/**
	 * Method to get applications from versa
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanApplications getSdwanAppplications(Integer productId, Integer customerId, Integer customerLeId, Integer partnerId) throws TclCommonException {
		
		SdwanApplications sdwanApplications = new SdwanApplications();
		try {
			List<Integer> customerLeIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();

			getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId
					+ "partnerLeIds " + partnerLeIds);
			sdwanApplications.setCustomerId(customerId);
			sdwanApplications.setCustomerLeIds(customerLeIds);
			sdwanApplications.setPartnerId(partnerId);
			sdwanApplications.setPartnerLeId(partnerLeIds);
			sdwanApplications.setProductId(productId);
			sdwanApplications.setLastUpdatedDate(ServiceInventoryUtils.dateConvertor(new Date()));
			List<SdwanApplicationsBean> versaAppls = new ArrayList<>();
			sdwanApplications.setVersaApplications(versaAppls);
			List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findSdwanServiceAttributes(customerLeIds, partnerLeIds, productId, customerId, partnerId,
					Arrays.asList( ServiceInventoryConstants.SDWAN_ORGANISATION_NAME, ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING ),
							Arrays.asList(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME));
			List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
			List<SdwanEndpoints> sdwanEndPointsForRegions = new ArrayList<>();
			if(serviceAttributes != null && !serviceAttributes.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				serviceAttributes.stream().forEach(attribute->{
					vwServiceAttributes.add(mapper.convertValue(attribute, VwServiceAttributesBean.class));
				});
				Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped = vwServiceAttributes.stream().collect(Collectors.groupingBy(VwServiceAttributesBean::getSysId));
				Map<String, List<SdwanEndpoints>> instanceByCode = getSdwanEnpointsByCode(serviceAttrGrouped, sdwanEndPointsForRegions);
				Map<String, Set<String>> orgTemplates = new HashMap<>();
				Map<String, Set<String>> orgDirectoryRegions = new HashMap<>();
				Set<String> tempNames = new HashSet<>();
				Set<String> directoryCodes = new HashSet<>();
				getSdwanAttributesForVersa(serviceAttrGrouped, orgTemplates, orgDirectoryRegions, tempNames,
						directoryCodes);
				List<String> versaDirectorRegion = new ArrayList<>();
				versaUserAppsByCommonTemplate(versaAppls, instanceByCode, orgDirectoryRegions, versaDirectorRegion);
//				versaUserApplFromMainTemplate(versaAppls, instanceByCode, orgTemplates, orgDirectoryRegions);
				if(!versaDirectorRegion.isEmpty()) {
					SdwanEndpoints sdwanEndpoint = instanceByCode.get(versaDirectorRegion.get(0)).get(0);
					RestResponse preDefinedApps = getVersaBuiltInAppls(sdwanEndpoint);
					if(preDefinedApps.getStatus() == Status.SUCCESS && preDefinedApps.getData() != null) {
						persistPredefinedAppls(versaAppls, preDefinedApps);
					}
				}
			}
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_ERROR);
		}
		return sdwanApplications;
		
	}
	
	/**
	 * Method to get user defined appls from versa common template
	 * @param versaAppls
	 * @param instanceByCode
	 * @param orgDirectoryRegions
	 * @param versaDirectorRegion
	 */
	private void versaUserAppsByCommonTemplate(List<SdwanApplicationsBean> versaAppls,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgDirectoryRegions,
			List<String> versaDirectorRegion) {
		orgDirectoryRegions.entrySet().forEach(orgDirectors->{
			versaDirectorRegion.add(orgDirectors.getValue().stream().findAny().orElse(null));
			if(!versaDirectorRegion.isEmpty()) {
				String userDefinedAppUrl = versaGetUserApplsUrl;
				userDefinedAppUrl = userDefinedAppUrl.replace("DYNAMICTEMPLATENAME", orgDirectors.getKey()+ServiceInventoryConstants.COMMON_TEMPLATE);
				userDefinedAppUrl = userDefinedAppUrl.replace("DYNAMICORGNAME", orgDirectors.getKey());
				SdwanEndpoints sdwanEndpoint = instanceByCode.get(versaDirectorRegion.get(0)).get(0);
				userDefinedAppUrl = sdwanEndpoint.getServerIp()+":"+sdwanEndpoint.getServerPort()+userDefinedAppUrl;
				try {
					LOGGER.info("Invoking VERSA to fetch user def appls for org common template {} ",userDefinedAppUrl);
					RestResponse userDefinedApps = restClientService.getWithBasicAuthentication(userDefinedAppUrl,
							new HashMap<String, String>(), sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
					LOGGER.info("userDefineResponse {} ",userDefinedApps.getData());
					if(userDefinedApps.getStatus() == Status.SUCCESS && userDefinedApps.getData() != null) {
						VersaUserDefinedAppsResponse userDefinedResp = Utils.convertJsonToObject(userDefinedApps.getData(), VersaUserDefinedAppsResponse.class);
						userDefinedResp.getUserDefinedApplications().stream().forEach(userApps->{
							SdwanApplicationsBean sdwanApplicationsBean = new SdwanApplicationsBean(userApps, null, orgDirectors.getKey(), null, sdwanEndpoint.getServerCode());
							versaAppls.add(sdwanApplicationsBean);
						});
					}
					
				} catch (Exception e) {
					LOGGER.error("Exception while parsing user defines applications from versa", e);
				}
				
			} else {
				throw new TclCommonRuntimeException(ExceptionConstants.VERSA_DIRECTOR_MAPPING_NOT_AVAILABLE,ResponseResource.R_CODE_ERROR);
			}
			
		});
	}
	
	/**
	 * Method to fetch user appls from VERSA by Main templates
	 * @param versaAppls
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 */
	private void versaUserApplFromMainTemplate(List<SdwanApplicationsBean> versaAppls,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions) {
		orgTemplates.entrySet().forEach(orgTemplate -> {
			orgTemplate.getValue().stream().forEach(template -> {
				String userDefinedAppUrl = versaGetUserApplsUrl;
				userDefinedAppUrl = userDefinedAppUrl.replace("DYNAMICTEMPLATENAME", template);
				userDefinedAppUrl = userDefinedAppUrl.replace("DYNAMICORGNAME", orgTemplate.getKey());
				Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
				SdwanEndpoints sdwanEndpoint = instanceByCode.get(serverCode.stream().findFirst().get()).get(0);
				userDefinedAppUrl = sdwanEndpoint.getServerIp()+":"+sdwanEndpoint.getServerPort()+userDefinedAppUrl;
				try {
					RestResponse userDefinedApps = restClientService.getWithBasicAuthentication(userDefinedAppUrl,
							new HashMap<String, String>(), sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
					LOGGER.info("userDefineResponse {} ",userDefinedApps.getData());
					if(userDefinedApps.getStatus() == Status.SUCCESS && userDefinedApps.getData() != null) {
						VersaUserDefinedAppsResponse userDefinedResp = Utils.convertJsonToObject(userDefinedApps.getData(), VersaUserDefinedAppsResponse.class);
						userDefinedResp.getUserDefinedApplications().stream().forEach(userApps->{
							SdwanApplicationsBean sdwanApplicationsBean = new SdwanApplicationsBean(userApps, template, orgTemplate.getKey(), null, sdwanEndpoint.getServerCode());
							versaAppls.add(sdwanApplicationsBean);
						});
					}
					
				} catch (Exception e) {
					LOGGER.error("Exception while parsing user defines applications from versa", e);
				}
			});
		});
	}
	
	/**
	 * Method to invoke VERSA predefined API
	 * @param sdwanEndPointsForRegions
	 * @return
	 * @throws TclCommonException
	 */
	private RestResponse getVersaBuiltInAppls(SdwanEndpoints sdwanEndPointsForRegions) throws TclCommonException {
		String preDefinedAppUrl = versaGetPreDefinedApplsUrl;
		preDefinedAppUrl = sdwanEndPointsForRegions.getServerIp()+":"+sdwanEndPointsForRegions.getServerPort()+preDefinedAppUrl;
		RestResponse preDefinedApps = restClientService.getWithBasicAuthentication(preDefinedAppUrl,
				new HashMap<String, String>(), sdwanEndPointsForRegions.getServerUsername(), sdwanEndPointsForRegions.getServerPassword());
		LOGGER.info("VERSA PreDefined apps Response {} ",preDefinedApps.getData());
		return preDefinedApps;
	}

	/**
	 * Method to persist predefined appl response
	 * @param versaAppls
	 * @param preDefinedApps
	 * @throws TclCommonException
	 */
	private void persistPredefinedAppls(List<SdwanApplicationsBean> versaAppls, RestResponse preDefinedApps)
			throws TclCommonException {
		VersaApplicationsResponse preDefinedApplications = Utils.convertJsonToObject(preDefinedApps.getData(), VersaApplicationsResponse.class);
		preDefinedApplications.getApplications().stream().forEach(apps->{
			SdwanApplicationsBean sdwanApplicationsBean = new SdwanApplicationsBean(apps);
			versaAppls.add(sdwanApplicationsBean);
		});
	}

	/**
	 * Method to update SDWAN CPE & Template Alias
	 * @param request
	 * @param type
	 * @return
	 * @throws TclCommonException 
	 */
	public String updateAliasName(SdwanAliasUpdateRequest request, String type) throws TclCommonException {
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
							cpeAliasList.add(constructSiAssetAttributes(request.getCpeAlias(),
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
									cpeAliasList.add(constructSiAssetAttributes(request.getCpeAlias(),
											ServiceInventoryConstants.SDWAN_CPE_ALIAS, siAsset));
								}
							});
						});
						siAssetAttributeRepository.saveAll(cpeAliasList);
					}
				}
			} else if (type.equalsIgnoreCase(ServiceInventoryConstants.TEMPLATE)) {
				if (request.getTemplateServiceIds() == null || request.getTemplateServiceIds().isEmpty())
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				else {
					List<SIServiceAttribute> siServiceAttributes = siServiceAttributeRepository
							.findAllById(request.getTemplateServiceIds());
					List<SIServiceAttribute> templateAliasList = new ArrayList<>();
					siServiceAttributes.stream().forEach(siServAttr -> {
						request.getTemplateServiceIds().stream().forEach(templateServiceId -> {
							if (siServAttr.getId().equals(templateServiceId)) {
								siServAttr.setAttributeAltValueLabel(request.getTemplateAlias());
								templateAliasList.add(siServAttr);
							}
						});
					});
					siServiceAttributeRepository.saveAll(templateAliasList);
				}
			} else
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			response = CommonConstants.SUCCESS;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * To construct si asset attribute
	 *
	 * @param attrValue
	 * @param attrName
	 * @param siAsset
	 * @return
	 */
	private SIAssetAttribute constructSiAssetAttributes(String attrValue, String attrName, SIAsset siAsset) {
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
	 * Get policy list with type from Versa
	 * 
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanPolicyBeanWithTimeStamp getPoliciesList(Integer productId, Integer customerId, Integer customerLeId,
			Integer partnerId) throws TclCommonException {
		LOGGER.info("Entering method to fetch policies list");
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("CustomerIds  " + customerIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
		List<Map<String, Object>> underlaySites = vwSiServiceInfoAllRepository.findUnderlaysByCustomer(customerId,
				partnerId, customerIds, partnerLeIds);
		LOGGER.info("Number of underlaySites : {}", underlaySites.size());
		List<Integer> underlaySysIds = underlaySites.stream().map(underlay -> (Integer) underlay.get("sysId"))
				.collect(Collectors.toList());
		List<Map<String, Object>> distinctTemplates = serviceAdditionalInfoRepository
				.findBySysIdAndAttributeNameGroupByName(underlaySysIds, ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
		List<String> templateNames = distinctTemplates.stream().map(template -> (String) template.get("name"))
				.collect(Collectors.toList());
		List<Map<String, Object>> underlaysOverlaysJoined = vwSiServiceInfoAllRepository
				.findByTemplateNames(templateNames, customerId, customerIds, partnerId, partnerLeIds);
		Set<Integer> overlaySysIds = underlaysOverlaysJoined.stream()
				.map(underlaysOverlays -> (Integer) underlaysOverlays.get("overlaySysId")).collect(Collectors.toSet());
		List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesByIdsIn(
				new ArrayList<>(overlaySysIds), Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
						ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
		Set<SdwanOrgTemplateMapping> sdwanOrgTemplateMappings = mapOverlayAndUnderlayAttributes(serviceAttributes,
				underlaysOverlaysJoined);
		Set<String> instanceRegions = sdwanOrgTemplateMappings.stream().map(mapping -> mapping.getInstanceRegion())
				.collect(Collectors.toSet());
		List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		List<SdwanPolicyBean> policies = new ArrayList<>();
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndpoints.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		getRulesFromVersa(sdwanOrgTemplateMappings, policies, instanceByCode);
		LOGGER.info("Policy details : " + Utils.convertObjectToJson(policies));
		List<SdwanPolicyListBean> sdwanPolicyList = new ArrayList<>();
		SdwanPolicyBeanWithTimeStamp policyBeanLastUpdate = new SdwanPolicyBeanWithTimeStamp();
		policies.forEach(policy -> {
			SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
			sdwanTemplateBean.setTemplateName(policy.getTemplateName());
			// get traffic steering policy list
			if (Objects.nonNull(policy.getTrafficSteeringPolicies())
					&& Objects.nonNull(policy.getTrafficSteeringPolicies().getRules())
					&& Objects.nonNull(policy.getTrafficSteeringPolicies().getRules().getRule()))
				policy.getTrafficSteeringPolicies().getRules().getRule().forEach(rule -> {
					SdwanPolicyListBean sdwanPolicyListBean = new SdwanPolicyListBean();
					sdwanPolicyListBean.setPolicyName(rule.getName());
					sdwanPolicyListBean.setAssociatedTemplate(sdwanTemplateBean);
					sdwanPolicyListBean.setPolicyType(ServiceInventoryConstants.TRAFFIC_STEERING);
					sdwanPolicyListBean.setOrganisationName(policy.getOrganisationName());
					sdwanPolicyListBean.setDirectorRegion(policy.getInstanceRegion());
					sdwanPolicyList.add(sdwanPolicyListBean);
				});
			// get QOS policy list
			if (Objects.nonNull(policy.getQosPolicy()) && Objects.nonNull(policy.getQosPolicy().getAppQosPolicy()))
				policy.getQosPolicy().getAppQosPolicy().forEach(appQosPolicy -> {
					SdwanPolicyListBean sdwanPolicyListBean = new SdwanPolicyListBean();
					sdwanPolicyListBean.setPolicyName(appQosPolicy.getName());
					sdwanPolicyListBean.setAssociatedTemplate(sdwanTemplateBean);
					sdwanPolicyListBean.setPolicyType(ServiceInventoryConstants.QOS);
					sdwanPolicyListBean.setOrganisationName(policy.getOrganisationName());
					sdwanPolicyListBean.setDirectorRegion(policy.getInstanceRegion());
					sdwanPolicyList.add(sdwanPolicyListBean);
				});
			// get FIREWALL policy list
			if (Objects.nonNull(policy.getFirewallPolicyConfig())
					&& Objects.nonNull(policy.getFirewallPolicyConfig().getAccessPolicy()))
				policy.getFirewallPolicyConfig().getAccessPolicy().forEach(firewallPolicy -> {
					SdwanPolicyListBean sdwanPolicyListBean = new SdwanPolicyListBean();
					policy.getAccessPolicyMap().entrySet().forEach(policyName -> {
						boolean getAccessPolicy = policyName.getValue().stream()
								.anyMatch(o -> o.getName().equals(firewallPolicy.getName()));
						if (getAccessPolicy) {
							sdwanPolicyListBean.setAccessPolicyName(policyName.getKey());
						}
					});
					sdwanPolicyListBean.setPolicyName(firewallPolicy.getName());
					sdwanPolicyListBean.setAssociatedTemplate(sdwanTemplateBean);
					sdwanPolicyListBean.setPolicyType(ServiceInventoryConstants.FIREWALL);
					sdwanPolicyListBean.setOrganisationName(policy.getOrganisationName());
					sdwanPolicyListBean.setDirectorRegion(policy.getInstanceRegion());
					sdwanPolicyList.add(sdwanPolicyListBean);
				});
		});
		policyBeanLastUpdate.setLastUpdated(ServiceInventoryUtils.dateConvertor(new Date()));
		policyBeanLastUpdate.setSdwanPolicyBean(sdwanPolicyList);
		return policyBeanLastUpdate;
	}

	/**
	 * Get policies/rules from versa
	 *
	 * @param sdwanOrgTemplateMappings
	 * @param policies
	 * @param instanceByCode
	 */
	private void getRulesFromVersa(Set<SdwanOrgTemplateMapping> sdwanOrgTemplateMappings,
			List<SdwanPolicyBean> policies, Map<String, List<SdwanEndpoints>> instanceByCode) {
		sdwanOrgTemplateMappings.forEach(orgTemplateMapping -> {
			SdwanPolicyBean sdwanPolicyBean = new SdwanPolicyBean();
			sdwanPolicyBean.setTemplateName(orgTemplateMapping.getTemplateName());
			sdwanPolicyBean.setOrganisationName(orgTemplateMapping.getOrganisationName());
			sdwanPolicyBean.setInstanceRegion(orgTemplateMapping.getInstanceRegion());
			instanceByCode.get(orgTemplateMapping.getInstanceRegion()).forEach(endpoint -> {
				try {
					// For traffic steering policies
					String tsUrl = versaGetSdwanPolicyUrl;
					tsUrl = tsUrl.replaceAll("DYNAMICORGNAME", orgTemplateMapping.getOrganisationName());
					tsUrl = tsUrl.replaceAll("DYNAMICTEMPLATENAME", orgTemplateMapping.getTemplateName());
					tsUrl = endpoint.getServerIp() + ":" + endpoint.getServerPort() + tsUrl;
					LOGGER.info("Get traffic steering policy URL : {}", tsUrl);
					RestResponse restResponse = restClientService.getWithBasicAuthentication(tsUrl,
							new HashMap<>(), endpoint.getServerUsername(), endpoint.getServerPassword());
					if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
						TrafficSteeringPolicy trafficSteeringPolicy = Utils.convertJsonToObject(restResponse.getData(),
								TrafficSteeringPolicy.class);
						if (Objects.isNull(trafficSteeringPolicy) || Objects.isNull(trafficSteeringPolicy.getRules())
								|| Objects.isNull(trafficSteeringPolicy.getRules().getRule()))
							sdwanPolicyBean.setTrafficSteeringPolicyCount(0);
						else {
							if (Objects.isNull(sdwanPolicyBean.getTrafficSteeringPolicies()))
								sdwanPolicyBean.setTrafficSteeringPolicies(trafficSteeringPolicy);
							else {
								if (!sdwanPolicyBean.getTrafficSteeringPolicies().getRules().getRule()
										.containsAll(trafficSteeringPolicy.getRules().getRule()))
									sdwanPolicyBean.getTrafficSteeringPolicies().getRules().getRule()
											.addAll(trafficSteeringPolicy.getRules().getRule());
							}
							sdwanPolicyBean.setTrafficSteeringPolicyCount(
									sdwanPolicyBean.getTrafficSteeringPolicies().getRules().getRule().size());
						}
					}

					// for Qos policies
					String qosUrl = versaGetQosPolicyUrl;
					qosUrl = qosUrl.replaceAll("DYNAMICORGNAME", orgTemplateMapping.getOrganisationName());
					qosUrl = qosUrl.replaceAll("DYNAMICTEMPLATENAME", orgTemplateMapping.getTemplateName());
					qosUrl = endpoint.getServerIp() + ":" + endpoint.getServerPort() + qosUrl;
					LOGGER.info("Get QOS policy URL : {}", qosUrl);
					restResponse = restClientService.getWithBasicAuthentication(qosUrl, new HashMap<>(),endpoint.getServerUsername(), endpoint.getServerPassword());
					LOGGER.info("QOS policy response for template {} ", restResponse.getStatus());
					if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
						QosPolicy qosPolicy = Utils.convertJsonToObject(restResponse.getData(), QosPolicy.class);
						if (Objects.isNull(qosPolicy) || Objects.isNull(qosPolicy.getAppQosPolicy()))
							sdwanPolicyBean.setQosPolicyCount(0);
						else {
							if (Objects.isNull(sdwanPolicyBean.getQosPolicy()))
								sdwanPolicyBean.setQosPolicy(qosPolicy);
							else {
								if (!sdwanPolicyBean.getQosPolicy().getAppQosPolicy()
										.containsAll(qosPolicy.getAppQosPolicy()))
									sdwanPolicyBean.getQosPolicy().getAppQosPolicy()
											.addAll(qosPolicy.getAppQosPolicy());
							}
							sdwanPolicyBean.setQosPolicyCount(sdwanPolicyBean.getQosPolicy().getAppQosPolicy().size());
						}
					}
					// for firewall policies
					String firewallUrl = versaApiFirewallPolicyGetUrl;
					firewallUrl = firewallUrl.replaceAll("DYNAMICORGNAME", orgTemplateMapping.getOrganisationName());
					firewallUrl = firewallUrl.replaceAll("DYNAMICTEMPLATENAME", orgTemplateMapping.getTemplateName());
					firewallUrl = endpoint.getServerIp() + ":" + endpoint.getServerPort() + firewallUrl;
					LOGGER.info("Get firewall policy URL : {}", firewallUrl);
					RestResponse firewallUrlResponse = restClientService.getWithBasicAuthentication(firewallUrl,
							new HashMap<>(), endpoint.getServerUsername(), endpoint.getServerPassword());
					LOGGER.info("Get firewall policy response : {}", firewallUrlResponse.getStatus());
					if (firewallUrlResponse.getStatus() == Status.SUCCESS
							&& Objects.nonNull(firewallUrlResponse.getData())) {
						FirewallPolicyConfig firewallPolicy = new FirewallPolicyConfig();
						GetFirewallRuleName getFirewallRuleName = Utils
								.convertJsonToObject(firewallUrlResponse.getData(), GetFirewallRuleName.class);
						if (Objects.isNull(getFirewallRuleName)
								|| Objects.isNull(getFirewallRuleName.getAccessPolicyGroup())) {
							sdwanPolicyBean.setFirewallPolicyCount(0);
							sdwanPolicyBean.setFirewallPolicyConfig(firewallPolicy);
						} else {
							if (Objects.nonNull(getFirewallRuleName.getAccessPolicyGroup())) {
								List<String> accessPolicyName = new ArrayList<>();
								getFirewallRuleName.getAccessPolicyGroup().stream().forEach(rule -> {
									accessPolicyName.add(rule.getName());
								});
								LOGGER.info("Get firewall configuration for below policies: {}", accessPolicyName);
								if (Objects.nonNull(accessPolicyName) && !accessPolicyName.isEmpty()) {
									List<com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.AccessPolicy> accessPolicy = new ArrayList<>();
									Map<String, List<com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.AccessPolicy>> accessPolicyMap = new HashMap<>();
									accessPolicyName.stream().forEach(eachPolicyName -> {
										String firewallConfigUrl = versaApiFirewallPolicyConfigurationGetUrl;
										firewallConfigUrl = firewallConfigUrl.replaceAll("DYNAMICORGNAME",
												orgTemplateMapping.getOrganisationName());
										firewallConfigUrl = firewallConfigUrl.replaceAll("DYNAMICTEMPLATENAME",
												orgTemplateMapping.getTemplateName());
										firewallConfigUrl = firewallConfigUrl.replaceAll("DYNAMICACCESSPOLICYNAME",
												eachPolicyName);
										firewallConfigUrl = endpoint.getServerIp() + ":" + endpoint.getServerPort()
												+ firewallConfigUrl;
										LOGGER.info("Get firewall configuration policy URL : {}", firewallConfigUrl);
										RestResponse firewallConfigUrlResponse = restClientService
												.getWithBasicAuthentication(firewallConfigUrl, new HashMap<>(),
														endpoint.getServerUsername(), endpoint.getServerPassword());
										LOGGER.info("Get firewall policy response for each access policy  : {}",
												firewallConfigUrlResponse.getStatus());
										if (firewallConfigUrlResponse.getStatus() == Status.SUCCESS
												&& Objects.nonNull(firewallConfigUrlResponse.getData())) {
											try {
												FirewallPolicyConfig firewallPolicyConfig = Utils.convertJsonToObject(
														firewallConfigUrlResponse.getData(),
														FirewallPolicyConfig.class);
												if (Objects.nonNull(firewallPolicyConfig)
														&& Objects.nonNull(firewallPolicyConfig.getAccessPolicy())) {
													accessPolicyMap.put(eachPolicyName,
															firewallPolicyConfig.getAccessPolicy());
													accessPolicy.addAll(firewallPolicyConfig.getAccessPolicy());
												}
											} catch (TclCommonException e) {
												throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
											}
										}
									});
									if (Objects.nonNull(accessPolicy) && !accessPolicy.isEmpty()) {
										firewallPolicy.setAccessPolicy(accessPolicy);
										sdwanPolicyBean.setFirewallPolicyConfig(firewallPolicy);
										sdwanPolicyBean.setFirewallPolicyCount(accessPolicy.size());
										sdwanPolicyBean.setAccessPolicyMap(accessPolicyMap);
									}
								}
							}
						}
					}
					policies.add(sdwanPolicyBean);
				} catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
		});
	}

	/**
	 * Map underlay attributes (templates) with overlay attributes
	 *
	 * @param serviceAttributes
	 * @param underlaysOverlaysJoined
	 * @return
	 */
	private Set<SdwanOrgTemplateMapping> mapOverlayAndUnderlayAttributes(List<Map<String, Object>> serviceAttributes,
			List<Map<String, Object>> underlaysOverlaysJoined) {
		serviceAttributes
				.sort((a1, a2) -> ((String) a1.get("serviceId")).compareToIgnoreCase((String) a2.get("serviceId")));
		Set<SdwanOrgTemplateMapping> sdwanOrgTemplateMappings = new HashSet<>();
		String instanceRegion[] = { "" };
		String orgName[] = { "" };
		serviceAttributes.forEach(overlayAttribute -> {
			if (ServiceInventoryConstants.SDWAN_ORGANISATION_NAME
					.equalsIgnoreCase((String) overlayAttribute.get("attributeName")))
				orgName[0] = (String) overlayAttribute.get("attributeValue");
			if (ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING
					.equalsIgnoreCase((String) overlayAttribute.get("attributeName")))
				instanceRegion[0] = (String) overlayAttribute.get("attributeValue");
			if (!StringUtils.isAllBlank(instanceRegion[0]) && !StringUtils.isAllBlank(orgName[0])) {
				underlaysOverlaysJoined.forEach(underlayAndOverlay -> {
					if (underlayAndOverlay.get("overlaySrvcId") != null
							&& ((String) underlayAndOverlay.get("overlaySrvcId"))
									.equalsIgnoreCase((String) overlayAttribute.get("serviceId"))) {
						if (ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
								.equalsIgnoreCase((String) underlayAndOverlay.get("attributeName"))) {
							SdwanOrgTemplateMapping sdwanOrgTemplateMapping = new SdwanOrgTemplateMapping();
							sdwanOrgTemplateMapping.setOrganisationName(orgName[0]);
							sdwanOrgTemplateMapping.setInstanceRegion(instanceRegion[0]);
							sdwanOrgTemplateMapping.setTemplateName((String) underlayAndOverlay.get("attributeValue"));
							sdwanOrgTemplateMappings.add(sdwanOrgTemplateMapping);
						}
					}
				});
				instanceRegion[0] = "";
				orgName[0] = "";
			}
		});
		return sdwanOrgTemplateMappings;
	}

	/**
	 * Get traffic steering rules for a template
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	TrafficSteeringRuleData getTrafficSteeringRuleForTemplate(String templateName, String organisationName,
			String policyName, SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String sdwanRuleUrl = trafficSteeringPolicyNameUrl;
		sdwanRuleUrl = sdwanRuleUrl.replace("DYNAMICTEMPLATENAME", templateName);
		sdwanRuleUrl = sdwanRuleUrl.replace("DYNAMICORGNAME", organisationName);
		sdwanRuleUrl = sdwanRuleUrl.replace("DYNAMICRULENAME", policyName);
		sdwanRuleUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + sdwanRuleUrl;
		RestResponse sdwanPolicyResponse = restClientService.getWithBasicAuthentication(sdwanRuleUrl,
				new HashMap<String, String>(), sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (Objects.nonNull(sdwanPolicyResponse) && Objects.nonNull(sdwanPolicyResponse.getData())) {
			TrafficSteeringRuleData trafficSteeringPolicy = Utils.convertJsonToObject(sdwanPolicyResponse.getData(),
					TrafficSteeringRuleData.class);
			return trafficSteeringPolicy;
		} else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
	}

	/**
	 * Get Qos Policy from versa based on template and organisation
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	AppQosPolicyRule getQosPoliciesForTemplate(String templateName, String organisationName, String policyName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String qosUrl = qosPolicyNameUrl;
		qosUrl = qosUrl.replace("DYNAMICTEMPLATENAME", templateName);
		qosUrl = qosUrl.replace("DYNAMICORGNAME", organisationName);
		qosUrl = qosUrl.replace("DYNAMICRULENAME", policyName);
		qosUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + qosUrl;

		LOGGER.info("Qos policy url : {}", qosUrl);
		RestResponse qosPolicyResponse = restClientService.getWithBasicAuthentication(qosUrl,
				new HashMap<String, String>(), sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (Objects.nonNull(qosPolicyResponse) && Objects.nonNull(qosPolicyResponse.getData())) {
			AppQosPolicyRule qosPolicy = Utils.convertJsonToObject(qosPolicyResponse.getData(), AppQosPolicyRule.class);
			return qosPolicy;
		} else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
	}

	/**
	 * Get policy detail view
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanPolicyDetailBean getPolicyDetailedView(SdwanPolicyDetailsRequestBean request)
			throws TclCommonException {
		SdwanPolicyDetailBean sdwanPolicyDetailBean = new SdwanPolicyDetailBean();
		SdwanPolicyListBean sdwanPolicyListBean = new SdwanPolicyListBean();
		sdwanPolicyListBean.setPolicyName(request.getPolicyName());
		sdwanPolicyListBean.setPolicyType(request.getPolicyType());
		sdwanPolicyListBean.setOrganisationName(request.getOrganisationName());
		sdwanPolicyListBean.setDirectorRegion(request.getDirectorRegion());
		sdwanPolicyListBean.setAccessPolicyName(request.getAccessPolicyName());
		SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
		sdwanTemplateBean.setTemplateName(request.getTemplateName());
		sdwanPolicyListBean.setAssociatedTemplate(sdwanTemplateBean);
		sdwanPolicyDetailBean.setSdwanPolicyListBean(sdwanPolicyListBean);
		SdwanEndpoints sdwanEndpoint = sdwanEndpointsRepository.findByServerCode(request.getDirectorRegion());
		SdwanInventoryAudit sdwanInventoryAudit = sdwanInventoryAuditRepository
				.findFirstByComponentNameAndComponentValueAndOperationOrderByIdDesc(ServiceInventoryConstants.POLICY,
						request.getPolicyName(), ServiceInventoryConstants.COMMIT_TEMPLATE_IN_SYNC);
		sdwanPolicyDetailBean.setTaskId(sdwanInventoryAudit != null ? sdwanInventoryAudit.getTaskId() : null);
		if (request.getPolicyType().equals(ServiceInventoryConstants.TRAFFIC_STEERING)) {
			TrafficSteeringRuleData trafficSteeringPolicy = getTrafficSteeringRuleForTemplate(request.getTemplateName(),
					request.getOrganisationName(), request.getPolicyName(), sdwanEndpoint);
			if (Objects.nonNull(trafficSteeringPolicy) && Objects.nonNull(trafficSteeringPolicy.getRule())) {
				Rule rule = trafficSteeringPolicy.getRule();
					ForwardingProfiles forwardingProfiles = getForwardingProfiles(request.getTemplateName(),
						request.getOrganisationName(), sdwanEndpoint);
					ForwardingProfile_ templateFP = findForwardingProfileFromList(rule.getSet().getForwardingProfile(),
							forwardingProfiles.getForwardingProfile());
					LOGGER.info("Forwarding profile : {}", Utils.convertObjectToJson(templateFP));
					SlaProfiles slaProfiles = getSlaProfilesForTemplate(request.getTemplateName(),
						request.getOrganisationName(), sdwanEndpoint);
					LOGGER.info("Sla Profile : {}", Utils.convertObjectToJson(slaProfiles));
					SlaProfile_ templateSP = findSlaProfileFromList(templateFP.getSlaProfile(),
							slaProfiles.getSlaProfile());
					mapTrafficSteeringPolicyDetails(templateFP, templateSP, rule, sdwanPolicyDetailBean);
			}
		} else if (request.getPolicyType().equals(ServiceInventoryConstants.QOS)) {
			AppQosPolicyRule qosPolicy = getQosPoliciesForTemplate(request.getTemplateName(),
					request.getOrganisationName(), request.getPolicyName(), sdwanEndpoint);
			if (Objects.nonNull(qosPolicy) && Objects.nonNull(qosPolicy.getAppQosPolicy())) {
				AppQosPolicy appQosPolicy = qosPolicy.getAppQosPolicy();
				QoSProfiles qoSProfiles = getQosProfilesForTemplate(request.getTemplateName(),
						request.getOrganisationName(), sdwanEndpoint);
				LOGGER.info("QoS Profile : {}", Utils.convertObjectToJson(qoSProfiles));
				if (Objects.nonNull(appQosPolicy.getSet()) && Objects.nonNull(qoSProfiles)) {
					QosProfile qosProfile = findQosProfileFromList(appQosPolicy.getSet().getQosProfile(),
							qoSProfiles.getQosProfile());
					mapQoSPolicyDetails(qosProfile, appQosPolicy, sdwanPolicyDetailBean);
				}
			}
		}else if (request.getPolicyType().equals(ServiceInventoryConstants.FIREWALL)) {
			GetFirewallPolicy firewall =getFirewallPolicies(request.getTemplateName(),
					request.getOrganisationName(), request.getPolicyName(), request.getAccessPolicyName(), sdwanEndpoint);
			if (Objects.nonNull(firewall) && Objects.nonNull(firewall.getAccessPolicy())) {
				mapFirewallPolicyDetails(firewall, sdwanPolicyDetailBean);
			}
		}
		//fetch ip address from address list
		AddressListView addressListView = getAddressListForTemplate(request.getTemplateName(),
				request.getOrganisationName(), sdwanEndpoint);
		if(Objects.nonNull(addressListView) && Objects.nonNull(addressListView.getAddresses())){
			//to find source address IP
			findIPFromAddressList(sdwanPolicyDetailBean.getSourceAddress(), addressListView.getAddresses().getAddress());
			//to find destination address IP
			findIPFromAddressList(sdwanPolicyDetailBean.getDestinationAddress(), addressListView.getAddresses().getAddress());
		}
		return sdwanPolicyDetailBean;
	}

	/**
	 * Find source IP from address list
	 *
	 * @param sourceAddress
	 * @param addressFromVersa
	 */
	private void findIPFromAddressList(List<SdwanAddressBean> sourceAddress, List<Address> addressFromVersa) {
		if(Objects.nonNull(sourceAddress) && Objects.nonNull(addressFromVersa))
		{
			addressFromVersa.forEach(address -> {
				sourceAddress.stream().filter(source ->
					source.getAddressName().equals(address.getName()))
						.peek(source -> source.setIpAddress(address.getIpv4Prefix()))
						.anyMatch(source -> source.getAddressName().equals(address.getName()));
			});
		}
	}

	/**
	 * Get address list for template
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	private AddressListView getAddressListForTemplate(String templateName, String organisationName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String addressListUrl = versaAddressUrl;
		addressListUrl = addressListUrl.replace("DYNAMICTEMPLATENAME", templateName);
		addressListUrl = addressListUrl.replace("DYNAMICORGNAME", organisationName);
		addressListUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + addressListUrl;
		AddressListView addressListView = new AddressListView();
		RestResponse restResponse = restClientService.getWithBasicAuthentication(addressListUrl, new HashMap<>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
			addressListView = Utils.convertJsonToObject(restResponse.getData(), AddressListView.class);
		}
		return addressListView;
	}

	/**
	 * Set QoS policy details
	 *
	 * @param qosProfile
	 * @param appQosPolicy
	 * @param sdwanPolicyDetailBean
	 * @throws TclCommonException 
	 */
	private void mapQoSPolicyDetails(QosProfile qosProfile, AppQosPolicy appQosPolicy,
			SdwanPolicyDetailBean sdwanPolicyDetailBean) throws TclCommonException {
		LOGGER.info("QosProfile response in mapQoSPolicyDetails: {}", Utils.convertObjectToJson(qosProfile));
		LOGGER.info(" APP QoS Profile response in mapQoSPolicyDetails: {}", Utils.convertObjectToJson(appQosPolicy));
		if (Objects.nonNull(appQosPolicy.getMatch())) {
			sdwanPolicyDetailBean.setDscp(appQosPolicy.getMatch().getDscp());
			if (Objects.nonNull(appQosPolicy.getMatch().getSource())) {
				sdwanPolicyDetailBean.setSourceAddress(new ArrayList<>());
				if (Objects.nonNull(appQosPolicy.getMatch().getSource().getAddress())) {
					sdwanPolicyDetailBean.setSourceAddressGroups(
							appQosPolicy.getMatch().getSource().getAddress().getAddressGroupList());
					if(Objects.nonNull(appQosPolicy.getMatch().getSource().getAddress().getAddressList()))
						appQosPolicy.getMatch().getSource().getAddress().getAddressList().forEach(sourceAddress ->{
							SdwanAddressBean sdwanAddressBean = new SdwanAddressBean();
							sdwanAddressBean.setAddressName(sourceAddress);
							sdwanPolicyDetailBean.getSourceAddress().add(sdwanAddressBean);
						});
				}
				if (Objects.nonNull(appQosPolicy.getMatch().getSource().getZone()))
					sdwanPolicyDetailBean
							.setSourceZones(appQosPolicy.getMatch().getSource().getZone().getZoneList());
			}
			// set destination address and groups
			if (Objects.nonNull(appQosPolicy.getMatch().getDestination())) {
				sdwanPolicyDetailBean.setDestinationAddress(new ArrayList<>());
				if (Objects.nonNull(appQosPolicy.getMatch().getDestination().getAddress())) {
					sdwanPolicyDetailBean.setDestinationAddressGroups(
							appQosPolicy.getMatch().getDestination().getAddress().getAddressGroupList());
					if(Objects.nonNull(appQosPolicy.getMatch().getDestination().getAddress().getAddressList()))
						appQosPolicy.getMatch().getDestination().getAddress().getAddressList().forEach(destinationAddress ->{
							SdwanAddressBean sdwanAddressBean = new SdwanAddressBean();
							sdwanAddressBean.setAddressName(destinationAddress);
							sdwanPolicyDetailBean.getDestinationAddress().add(sdwanAddressBean);
						});
				}
				if (Objects.nonNull(appQosPolicy.getMatch().getDestination().getZone()))
					sdwanPolicyDetailBean
							.setDestinationZones(appQosPolicy.getMatch().getDestination().getZone().getZoneList());
			}
			// set total count
			sdwanPolicyDetailBean.setAddressCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceAddress())
					+ returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationAddress()));
			sdwanPolicyDetailBean
					.setAddressGroupCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceAddressGroups())
							+ returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationAddressGroups()));
			sdwanPolicyDetailBean.setZonesCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceZones())
					+ returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationZones()));
			// set applications and count
			if (Objects.nonNull(appQosPolicy.getMatch().getApplication())) {
				sdwanPolicyDetailBean.setPredefinedApplications(
						appQosPolicy.getMatch().getApplication().getPredefinedApplicationList());
				sdwanPolicyDetailBean.setUserdefinedApplications(
						appQosPolicy.getMatch().getApplication().getUserDefinedApplicationList());
				sdwanPolicyDetailBean.setApplicationsCount((sdwanPolicyDetailBean.getPredefinedApplications() != null
						? sdwanPolicyDetailBean.getPredefinedApplications().size()
						: 0)
						+ (sdwanPolicyDetailBean.getUserdefinedApplications() != null
								? sdwanPolicyDetailBean.getUserdefinedApplications().size()
								: 0));
			}
			// set url's and count
			if (Objects.nonNull(appQosPolicy.getMatch().getUrlCategory())) {
				sdwanPolicyDetailBean.setPredefinedUrls(appQosPolicy.getMatch().getUrlCategory().getPredefined());
				sdwanPolicyDetailBean.setUserDefinedUrls(appQosPolicy.getMatch().getUrlCategory().getUserDefined());
				sdwanPolicyDetailBean.setUrlAssociatedCount((sdwanPolicyDetailBean.getPredefinedUrls() != null
						? sdwanPolicyDetailBean.getPredefinedUrls().size()
						: 0)
						+ (sdwanPolicyDetailBean.getUserDefinedUrls() != null
								? sdwanPolicyDetailBean.getUserDefinedUrls().size()
								: 0));
			}
		}
		// set qos profile data
		if (Objects.nonNull(qosProfile)) {
			sdwanPolicyDetailBean.setQueueName(qosProfile.getForwardingClass());
			sdwanPolicyDetailBean.setPolisherConfig(qosProfile.getPeakKbpsRate());
			sdwanPolicyDetailBean.setProfileName(qosProfile.getName());
		}
		LOGGER.info("QoS Profile sdwanPolicyDetailBean response: {}", Utils.convertObjectToJson(sdwanPolicyDetailBean));
	}

	/**
	 * Search QoS profile from list
	 *
	 * @param qosProfileName
	 * @param qosProfiles
	 * @return
	 */
	private QosProfile findQosProfileFromList(String qosProfileName, List<QosProfile> qosProfiles) {
		QosProfile matchedProfile = new QosProfile();
		if (Objects.nonNull(qosProfileName) && Objects.nonNull(qosProfiles)) {
			for (QosProfile qosProfile : qosProfiles) {
				if (qosProfileName.equals(qosProfile.getName()))
					matchedProfile = qosProfile;
			}
		}
		return matchedProfile;
	}

	/**
	 * Get QoS profile by template and organisation
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	private QoSProfiles getQosProfilesForTemplate(String templateName, String organisationName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String qosProfileUrl = versaGetQosProfileUrl;
		qosProfileUrl = qosProfileUrl.replace("DYNAMICTEMPLATENAME", templateName);
		qosProfileUrl = qosProfileUrl.replace("DYNAMICORGNAME", organisationName);
		qosProfileUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + qosProfileUrl;
		RestResponse restResponse = restClientService.getWithBasicAuthentication(qosProfileUrl, new HashMap<>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		QoSProfiles qoSProfiles = new QoSProfiles();
		if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
			qoSProfiles = Utils.convertJsonToObject(restResponse.getData(), QoSProfiles.class);
			LOGGER.info("Sla Mapping : {}", Utils.convertObjectToJson(qoSProfiles));
		}
		return qoSProfiles;
	}

	/**
	 * Set traffic steering policy details
	 *
	 * @param templateFP
	 * @param templateSP
	 * @param sdwanPolicyDetailBean
	 * @throws TclCommonException 
	 */
	private void mapTrafficSteeringPolicyDetails(ForwardingProfile_ templateFP, SlaProfile_ templateSP, Rule rule,
			SdwanPolicyDetailBean sdwanPolicyDetailBean) throws TclCommonException {
		LOGGER.info("mapTrafficSteeringPolicyDetails templateFP Response : {}", Utils.convertObjectToJson(templateFP));
		LOGGER.info("mapTrafficSteeringPolicyDetails templateSP Response : {}", Utils.convertObjectToJson(templateSP));
		LOGGER.info("mapTrafficSteeringPolicyDetails rule Response : {}", Utils.convertObjectToJson(rule));
		// set data from forwarding profile
		sdwanPolicyDetailBean.setEncryptionMethod(templateFP.getEncryption());
		sdwanPolicyDetailBean.setLoadBalancingMethod(templateFP.getLoadBalance());
		sdwanPolicyDetailBean.setPacketReplicationMethod(
				Objects.nonNull(templateFP.getReplication()) ? templateFP.getReplication().getMode() : null);
		sdwanPolicyDetailBean.setRecomputeTimer(templateFP.getRecomputeTimer());
		sdwanPolicyDetailBean.setThresholdViolationAction(templateFP.getSlaViolationAction());
		// set data from SLA profile
		sdwanPolicyDetailBean.setMaxPacketLoss(templateSP.getLossPercentage());
		sdwanPolicyDetailBean.setMaxFwdPacketLoss(templateSP.getForwardLossPercentage());
		sdwanPolicyDetailBean.setMaxRevPacketLoss(templateSP.getReverseLossPercentage());
		sdwanPolicyDetailBean.setMaxLatency(templateSP.getLatency());
		sdwanPolicyDetailBean.setTransmitUtilization(templateSP.getCircuitTransmitUtilization());
		sdwanPolicyDetailBean.setReceiveUtilization(templateSP.getCircuitReceiveUtilization());
		sdwanPolicyDetailBean.setJitter(templateSP.getDelayVariation());
		sdwanPolicyDetailBean.setProfileName(templateFP.getName());
		sdwanPolicyDetailBean.setSlaProfileName(templateSP.getName());
		sdwanPolicyDetailBean.setMosScore(templateSP.getMosScore());
		//set path priority
		if (Objects.nonNull(templateFP.getCircuitPriorities())
				&& Objects.nonNull(templateFP.getCircuitPriorities().getPriority())) {
			List<SdwanPathPriorityBean> pathPriorities = templateFP.getCircuitPriorities().getPriority().stream()
					.map(SdwanPathPriorityBean::fromPriority).collect(Collectors.toList());
			sdwanPolicyDetailBean.setPathPriority(pathPriorities);
		}
		if (Objects.nonNull(rule.getMatch())) {
			sdwanPolicyDetailBean.setDscp(rule.getMatch().getDscp());
			//set source address and groups
			if(Objects.nonNull(rule.getMatch().getSource())) {
				if(Objects.nonNull(rule.getMatch().getSource().getAddress())) {
					sdwanPolicyDetailBean.setSourceAddress(new ArrayList<>());
					sdwanPolicyDetailBean.setSourceAddressGroups(rule.getMatch().getSource().getAddress().getAddressGroupList());
//					sdwanPolicyDetailBean.setSourceAddress(rule.getMatch().getSource().getAddress().getAddressList());
					if(Objects.nonNull(rule.getMatch().getSource().getAddress().getAddressList()))
					rule.getMatch().getSource().getAddress().getAddressList().forEach(sourceAddress ->{
						SdwanAddressBean sdwanAddressBean = new SdwanAddressBean();
						sdwanAddressBean.setAddressName(sourceAddress);
						sdwanPolicyDetailBean.getSourceAddress().add(sdwanAddressBean);
					});
				}
				if (Objects.nonNull(rule.getMatch().getSource().getZone()))
					sdwanPolicyDetailBean.setSourceZones(rule.getMatch().getSource().getZone().getZoneList());
			}
			//set destination address and groups
			if(Objects.nonNull(rule.getMatch().getDestination())) {
				if(Objects.nonNull(rule.getMatch().getDestination().getAddress())) {
					sdwanPolicyDetailBean.setDestinationAddress(new ArrayList<>());
					sdwanPolicyDetailBean.setDestinationAddressGroups(rule.getMatch().getDestination().getAddress().getAddressGroupList());
//					sdwanPolicyDetailBean.setSourceAddress(rule.getMatch().getDestination().getAddress().getAddressList());
					if(Objects.nonNull(rule.getMatch().getDestination().getAddress().getAddressList()))
					rule.getMatch().getDestination().getAddress().getAddressList().forEach(destinationAddress ->{
						SdwanAddressBean sdwanAddressBean = new SdwanAddressBean();
						sdwanAddressBean.setAddressName(destinationAddress);
						sdwanPolicyDetailBean.getDestinationAddress().add(sdwanAddressBean);
					});
				}
				if (Objects.nonNull(rule.getMatch().getDestination().getZone()))
					sdwanPolicyDetailBean.setDestinationZones(rule.getMatch().getDestination().getZone().getZoneList());
			}
			//set total count
			sdwanPolicyDetailBean.setAddressCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceAddress()) + returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationAddress()));
			sdwanPolicyDetailBean.setAddressGroupCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceAddressGroups()) + returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationAddressGroups()));
			sdwanPolicyDetailBean.setZonesCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceZones()) + returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationZones()));
			// set applications associated and count
			if (Objects.nonNull(rule.getMatch().getApplication())) {
				sdwanPolicyDetailBean
						.setPredefinedApplications(rule.getMatch().getApplication().getPredefinedApplicationList());
				sdwanPolicyDetailBean
						.setUserdefinedApplications(rule.getMatch().getApplication().getUserDefinedApplicationList());
				sdwanPolicyDetailBean.setApplicationsCount((sdwanPolicyDetailBean.getPredefinedApplications() != null
						? sdwanPolicyDetailBean.getPredefinedApplications().size()
						: 0)
						+ (sdwanPolicyDetailBean.getUserdefinedApplications() != null
								? sdwanPolicyDetailBean.getUserdefinedApplications().size()
								: 0));
			}
			// set url's associated
			if (Objects.nonNull(rule.getMatch().getUrlCategory())) {
				sdwanPolicyDetailBean.setPredefinedUrls(rule.getMatch().getUrlCategory().getPredefined());
				sdwanPolicyDetailBean.setUserDefinedUrls(rule.getMatch().getUrlCategory().getUserDefined());
				sdwanPolicyDetailBean.setUrlAssociatedCount((sdwanPolicyDetailBean.getPredefinedUrls() != null
						? sdwanPolicyDetailBean.getPredefinedUrls().size()
						: 0)
						+ (sdwanPolicyDetailBean.getUserDefinedUrls() != null
								? sdwanPolicyDetailBean.getUserDefinedUrls().size()
								: 0));
			}
			LOGGER.info("Traffic Steering sdwanPolicyDetailBean Response : {}", Utils.convertObjectToJson(sdwanPolicyDetailBean));
		}
	}

	/**
	 * Returns true if not null
	 *
	 * @param object
	 * @return
	 */
	private <T> Integer returnSizeIfNotNull(List<T> object){
		if(Objects.nonNull(object))
			return object.size();
		else return 0;
	}

	/**
	 * Search Sla Profile from list
	 *
	 * @param slaProfileName
	 * @param slaProfiles
	 * @return
	 */
	private SlaProfile_ findSlaProfileFromList(String slaProfileName, List<SlaProfile_> slaProfiles) {
		SlaProfile_ templateSla = new SlaProfile_();
		if (Objects.nonNull(slaProfileName) && Objects.nonNull(slaProfiles)) {
			for (SlaProfile_ slaProfile : slaProfiles) {
				if (slaProfileName.equals(slaProfile.getName()))
					templateSla = slaProfile;
			}
		}
		return templateSla;
	}

	/**
	 * Get Sla Profiles with template and organisation
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	private SlaProfiles getSlaProfilesForTemplate(String templateName, String organisationName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String slaProfileUrl = versaSlaProfileUrl;
		slaProfileUrl = slaProfileUrl.replace("DYNAMICTEMPLATENAME", templateName);
		slaProfileUrl = slaProfileUrl.replace("DYNAMICORGNAME", organisationName);
		slaProfileUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + slaProfileUrl;
		RestResponse restResponse = restClientService.getWithBasicAuthentication(slaProfileUrl, new HashMap<>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		SlaProfiles slaProfiles = new SlaProfiles();
		if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
			slaProfiles = Utils.convertJsonToObject(restResponse.getData(), SlaProfiles.class);
			LOGGER.info("Sla Mapping : {}", Utils.convertObjectToJson(slaProfiles));
		}
		return slaProfiles;
	}

	/**
	 * Search forwarding profile from list
	 *
	 * @param forwardingProfile
	 * @param forwardingProfileList
	 * @return
	 */
	private ForwardingProfile_ findForwardingProfileFromList(String forwardingProfile,
			List<ForwardingProfile_> forwardingProfileList) {
		ForwardingProfile_ templateFP = new ForwardingProfile_();
		if (Objects.nonNull(forwardingProfile) && Objects.nonNull(forwardingProfileList)) {
			for (ForwardingProfile_ fp : forwardingProfileList) {
				if (forwardingProfile.equals(fp.getName())) {
					templateFP = fp;
					break;
				}
			}
		}
		return templateFP;
	}

	/**
	 * Get forwarding profiles
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	private ForwardingProfiles getForwardingProfiles(String templateName, String organisationName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String frwdProfileUrl = versaGetFpProfileUrl;
		frwdProfileUrl = frwdProfileUrl.replace("DYNAMICTEMPLATENAME", templateName);
		frwdProfileUrl = frwdProfileUrl.replace("DYNAMICORGNAME", organisationName);
		frwdProfileUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + frwdProfileUrl;
		RestResponse restResponse = restClientService.getWithBasicAuthentication(frwdProfileUrl, new HashMap<>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		ForwardingProfiles forwardingProfiles = new ForwardingProfiles();
		if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
			forwardingProfiles = Utils.convertJsonToObject(restResponse.getData(), ForwardingProfiles.class);
		}
		return forwardingProfiles;
	}

	/**
	 * Get forwarding profiles
	 *
	 *
	 * @param profileName
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	private ForwardingProfile getForwardingProfileByName(String profileName, String templateName,
			String organisationName, SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String frwdProfileUrl = versaGetFpByName;
		frwdProfileUrl = frwdProfileUrl.replace("DYNAMICTEMPLATENAME", templateName);
		frwdProfileUrl = frwdProfileUrl.replace("DYNAMICORGNAME", organisationName);
		frwdProfileUrl = frwdProfileUrl.replace("DYNAMICFPNAME", profileName);
		frwdProfileUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + frwdProfileUrl;
		RestResponse restResponse = restClientService.getWithBasicAuthentication(frwdProfileUrl, new HashMap<>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		ForwardingProfile forwardingProfile = new ForwardingProfile();
		if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
			forwardingProfile = Utils.convertJsonToObject(restResponse.getData(), ForwardingProfile.class);
		}
		return forwardingProfile;
	}
	
	/**
	 * Method to get application details
	 * @param request
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param productId
	 * @return
	 * @throws TclCommonException 
	 */
	public SdwanApplications sdwanApplicationDetailedInfo(ApplicationInfoRequestBean request, 
			Integer customerId, Integer partnerId, Integer customerLeId, Integer productId) throws TclCommonException {
		SdwanApplications sdwanApplications = new SdwanApplications();
		List<SdwanApplicationsBean> sdwanApplBeans = new ArrayList<>();
		try {
			if(StringUtils.isBlank(request.getApplicationName()) || StringUtils.isBlank(request.getOrganisationName()) 
					|| StringUtils.isBlank(request.getDirectoryRegion()))
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			sdwanApplications.setCustomerId(customerId);
			sdwanApplications.setProductId(productId);
			SdwanEndpoints endpts = sdwanEndpointsRepository.findByServerCode(request.getDirectoryRegion());
			String userAppsUrl = versaUserAppByNameUrl;
			if(request.getApplicationType().equalsIgnoreCase(ServiceInventoryConstants.USER_DEFINED)) {
				LOGGER.info("Fetching apps details from VERSA for orgName {} and applName {} ", request.getOrganisationName(), request.getApplicationName());
				userAppsUrl = userAppsUrl.replace("COMMONTEMPLATE", request.getOrganisationName()+ServiceInventoryConstants.COMMON_TEMPLATE);
				userAppsUrl = userAppsUrl.replace("DYNAMICORGNAME", request.getOrganisationName());	
				userAppsUrl = userAppsUrl.replace("DYNAMICAPPNAME", request.getApplicationName());
				userAppsUrl = endpts.getServerIp()+":"+endpts.getServerPort()+userAppsUrl;
					try {
						LOGGER.info("Fetching userdefined app details from VERSA URL {} ",userAppsUrl);
//						String tempTEsting = "https://219.65.51.11:9182/api/config/devices/template/ind_single_1wan/config/orgs/org-services/SDWAN_DEMO_DEV/application-identification/user-defined-applications/user-defined-application/test_for_range?format=json&deep=true";
						RestResponse userDefinedApps = restClientService.getWithBasicAuthentication(userAppsUrl,
								new HashMap<String, String>(), endpts.getServerUsername(), endpts.getServerPassword());
						LOGGER.info("userDefineResponse {} ",userDefinedApps.getData());
						if(userDefinedApps.getStatus() == Status.SUCCESS && userDefinedApps.getData() != null) {
							VersaUserDefinedAppsResponse userDefinedResp = Utils.convertJsonToObject(userDefinedApps.getData(), VersaUserDefinedAppsResponse.class);
							userDefinedResp.getUserDefinedApplications().stream().forEach(userApps->{
								sdwanApplBeans.add(new SdwanApplicationsBean(userApps, request.getTemplateName(), request.getOrganisationName(), null, request.getDirectoryRegion()));
							});
							sdwanApplications.setVersaApplications(sdwanApplBeans);
						}
						
					} catch (Exception e) {
						LOGGER.error("Exception while fetching user defined app details from versa", e);
					}
			}
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_ERROR);
		}
		return sdwanApplications;
		
	}

	/**
	 * Get status count of all sites and cpe's associated with a customer
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
		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
		LOGGER.info("Customer ID : "+ customerId + "customer" + customerIds);
		List<Map<String, Object>> allSiteInfo = vwSiServiceInfoAllRepository.findAllSdwanSites(customerIds,
				partnerLeIds, productId, customerId, partnerId);
		List<String> sdwanServiceIds = allSiteInfo.stream().map(siteInfo -> (String) siteInfo.get("srv_service_id"))
				.collect(Collectors.toList());
		List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository.findAttributesBySdwanServiceIds(
				sdwanServiceIds, Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
						ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING),
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
				attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
				.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));

		VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
		if (Objects.nonNull(versaCpeStatusResponse)
				&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
			List<Map<String, Object>> underlaySitesAndCpeInfo = vwSiServiceInfoAllRepository
					.findUnderlaysBySdwanServiceIdGroupedByName(sdwanServiceIds);
			Map<String, String> cpeStatusInfo = new HashMap<>();
			Map<String,String> siteIdStatus = new HashMap<>();
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
							getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse,
									(String) underlaySite.get("asset_name"), cpeStatusInfo,
									sdwanCpeDetails.getControllers());
							sdwanCpeDetails.setCpeStatus(cpeStatusInfo.get(ServiceInventoryConstants.CPE_STATUS));
							sdwanCpeDetails.setSdwanServiceId((String) underlaySite.get("sdwan_id"));
							Map<String, String> links = new HashMap<>();
							if (ServiceInventoryConstants.ONLINE.equals(sdwanCpeDetails.getCpeStatus())) {
								getWanStatusFromVersa((String) underlaySite.get("asset_name"),
										sdwanCpeDetails.getControllers(), sdwanSiteDetails.getInstanceRegion(),
										sdwanSiteDetails.getOrganisationName(), instanceByCode, links);
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

			siteAndCpeStatusCount.setAllSitesCount(siteAndCpeStatusCount.getSiteDegradedCount()+siteAndCpeStatusCount.getSiteOfflineCount()+siteAndCpeStatusCount.getSiteOnlineCount());
			siteAndCpeStatusCount.setAllCpeCount(siteAndCpeStatusCount.getCpeOfflineCount()+siteAndCpeStatusCount.getCpeOnlineCount());
			
		}
		return siteAndCpeStatusCount;
	}

	/**
	 * Editing policies (traffic steering/App Qos)
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public TemplateCpeStatusResponse editPolicyDetails(SdwanPolicyDetailBean request) throws TclCommonException {
		String status = "";
		TemplateCpeStatusResponse templateCpeStatusResponse = new TemplateCpeStatusResponse();
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
				.findByServerCodeIn(Collections.singleton(request.getSdwanPolicyListBean().getDirectorRegion()));
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		Map<String, Set<String>> orgTemplates = new HashMap<>();
		orgTemplates.put(request.getSdwanPolicyListBean().getOrganisationName(),
				Collections.singleton(request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName()));
		Map<String, Set<String>> orgDirectoryRegions = new HashMap<>();
		orgDirectoryRegions.put(request.getSdwanPolicyListBean().getOrganisationName(),
				Collections.singleton(request.getSdwanPolicyListBean().getDirectorRegion()));
		Set<String> serverCode = orgDirectoryRegions.get(request.getSdwanPolicyListBean().getOrganisationName());
		SdwanEndpoints sdwanEndpoint = instanceByCode.get(serverCode.stream().findFirst().get()).get(0);
		Map<String, Set<String>> cpeGroupedByTemplateName = getCpeByTemplateFromVersa(instanceByCode, orgTemplates,
				orgDirectoryRegions);
		Map<String, Set<DeviceGroupDatum>> cpeAvailabilityByTemplate = checkTemplateSyncStatusByCpe(
				cpeGroupedByTemplateName, instanceByCode, orgTemplates, orgDirectoryRegions);
		if (ServiceInventoryConstants.TRAFFIC_STEERING.equals(request.getSdwanPolicyListBean().getPolicyType())) {
			TrafficSteeringRuleData trafficSteeringPolicy = getTrafficSteeringRuleForTemplate(
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getPolicyName(), sdwanEndpoint);
			String slaProfileName = request.getSlaProfileName();
			String fpProfileName = request.getProfileName();
			if (Objects.nonNull(trafficSteeringPolicy) && Objects.nonNull(trafficSteeringPolicy.getRule())) {
				if(!StringUtils.isAllBlank(request.getSlaProfileName())) {
					SlaProfiles slaProfileRequest = getSlaProfileByName(request.getSlaProfileName(),
							request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
							request.getSdwanPolicyListBean().getOrganisationName(), sdwanEndpoint);
					if(slaProfileRequest != null) {
						if(!slaProfileName.startsWith("SSP_SLA_") && slaProfileRequest != null) {
						    slaProfileName = "SSP_SLA_"+request.getSdwanPolicyListBean().getPolicyName();
							createSlaProfile(request, sdwanEndpoint, slaProfileName, slaProfileRequest);
							LOGGER.info("editPolicyDetails Created SLA profile  {} ",slaProfileName);
						}
						SlaProfilesCreatePayload slaProfileEditRequest = constructSlaProfileEditPayload(slaProfileRequest, request, slaProfileName);
						status = editSlaProfileRequest(request, sdwanEndpoint, slaProfileName, slaProfileEditRequest);
					}
				}
				if (!StringUtils.isAllBlank(request.getProfileName())
						&& !StringUtils.isAllBlank(trafficSteeringPolicy.getRule().getSet().getForwardingProfile())) {
					ForwardingProfile forwardingProfile = getForwardingProfileByName(request.getProfileName(),
							request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
							request.getSdwanPolicyListBean().getOrganisationName(), sdwanEndpoint);				
					if(!fpProfileName.startsWith("SSP_FP_") && forwardingProfile != null) {
						fpProfileName = "SSP_FP_"+request.getSdwanPolicyListBean().getPolicyName();
						createFpProfile(request, sdwanEndpoint, fpProfileName, forwardingProfile, slaProfileName);
						LOGGER.info("editPolicyDetails Created FP profile with Fp profile name  {} and sla profile {} ",fpProfileName, slaProfileName);
					}
					ForwardingProfile editProfileRequest = createRequestForEditFP(forwardingProfile, request, fpProfileName, slaProfileName);
					status = editForwardingProfile(sdwanEndpoint, editProfileRequest, request);
				}
				Rules_ editRequest = createRequestForEditTS(trafficSteeringPolicy.getRule(), request, fpProfileName);
				status = editTrafficSteeringPolicy(sdwanEndpoint, editRequest, request);
				templateCpeStatusResponse.setCreateUpdateStatus(status);
			}
		} else if (ServiceInventoryConstants.QOS.equals(request.getSdwanPolicyListBean().getPolicyType())) {
			AppQosPolicyRule qosPolicy = getQosPoliciesForTemplate(
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getPolicyName(), sdwanEndpoint);
			if (Objects.nonNull(qosPolicy) && Objects.nonNull(qosPolicy.getAppQosPolicy())) {
				AppQosPolicy appQosPolicy = qosPolicy.getAppQosPolicy();
				QosPolicy editRequest = createRequestForEditQos(appQosPolicy, request);
				status = editQosPolicy(sdwanEndpoint, editRequest, request);
				LOGGER.info("editPolicyDetails status value for firewall {} ",status);
				templateCpeStatusResponse.setCreateUpdateStatus(status);
			}
		}else if (ServiceInventoryConstants.FIREWALL.equals(request.getSdwanPolicyListBean().getPolicyType())) {
			GetFirewallPolicy firewall=getFirewallPolicies(request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					request.getSdwanPolicyListBean().getOrganisationName(), 
					request.getSdwanPolicyListBean().getPolicyName(),
					request.getSdwanPolicyListBean().getAccessPolicyName(), sdwanEndpoint);
			if (Objects.nonNull(firewall) && Objects.nonNull(firewall.getAccessPolicy())) {
				AccessPolicy firewallRequestBody=createEditRequestForFirewall(firewall.getAccessPolicy(),request);
				status=editFirewallPolicy(sdwanEndpoint, firewallRequestBody, request);
				templateCpeStatusResponse.setCreateUpdateStatus(status);
			}
		}
		if (status.equals(Constants.SUCCESS)) {
			templateCpeStatusResponse = checkSyncAndCommitTemplate(cpeAvailabilityByTemplate, request.getCustomerId(),
					instanceByCode, orgTemplates, orgDirectoryRegions, ServiceInventoryConstants.POLICY,
					request.getSdwanPolicyListBean().getPolicyName(), null);
		} else
			throw new TclCommonException(ExceptionConstants.VERSA_FAILED_TO_UPDATE_POLICY);
		return templateCpeStatusResponse;
	}

	
	/**
	 * Method to create Forward profile with given name
	 * @param request
	 * @param sdwanEndpoint
	 * @param slaProfileName
	 * @param forwardingProfile
	 * @throws TclCommonException
	 */
	private void createFpProfile(SdwanPolicyDetailBean request, SdwanEndpoints sdwanEndpoint, String fpProfileName,
			ForwardingProfile forwardingProfile, String slaProfileName) throws TclCommonException {
		try {
			LOGGER.info("Inside createFpProfile FP profile namev {} and  slaProfile name {}  " ,forwardingProfile.getForwardingProfile().getName() , slaProfileName);
			forwardingProfile.getForwardingProfile().setName(fpProfileName);
			forwardingProfile.getForwardingProfile().setSlaProfile(slaProfileName); 
			String fpreateUrl = versaFpProfileCreateUrl;
			fpreateUrl = fpreateUrl.replace("<Template name>", request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName());
			fpreateUrl = fpreateUrl.replace("<Organization name>", request.getSdwanPolicyListBean().getOrganisationName());
			fpreateUrl = sdwanEndpoint.getServerIp()+":"+sdwanEndpoint.getServerPort()+fpreateUrl;
			Map<String, String> httpHeaders = new HashMap<>();
			String auth = sdwanEndpoint.getServerUsername() + ":" + sdwanEndpoint.getServerPassword();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			httpHeaders.put("Authorization", authHeader);
			httpHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.put("Accept", MediaType.APPLICATION_JSON_VALUE);
			String requestString = Utils.convertObjectToJson(forwardingProfile);
			LOGGER.info("Inside createFpProfile FP profile create URL {} and  request {}  " ,fpreateUrl , requestString);
			RestResponse slaProfileCreateResponse = restClientService.postWithProxyBasicAuthentication(fpreateUrl, requestString, httpHeaders, 
					sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
			if(slaProfileCreateResponse.getStatus() == Status.SUCCESS) {
				forwardingProfile.getForwardingProfile().setSlaProfile(slaProfileName);
			} else {
				throw new TclCommonException(ExceptionConstants.VERSA_FP_PROFILE_CREATION_FAILED);
			}
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.VERSA_FP_PROFILE_CREATION_FAILED);
		}
		
	}

	/**
	 * Method to create SLA profile
	 * @param request
	 * @param sdwanEndpoint
	 * @param slaProfileName
	 * @param forwardingProfile
	 * @param slaProfileRequest
	 * @throws TclCommonException
	 */
	private String createSlaProfile(SdwanPolicyDetailBean request, SdwanEndpoints sdwanEndpoint, String slaProfileName, 
			SlaProfiles slaProfileRequest) throws TclCommonException {
		String status = CommonConstants.FAILIURE;
		SlaProfilesCreatePayload payload = new SlaProfilesCreatePayload();
		payload.setSlaProfile(slaProfileRequest.getSlaProfile().get(0));
		payload.getSlaProfile().setName(slaProfileName);
		String url = versaSlaProfileCreateUrl; 
		url = url.replace("DYNAMICTEMPLATENAME", request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName());
		url = url.replace("DYNAMICORGNAME", request.getSdwanPolicyListBean().getOrganisationName());
		url = sdwanEndpoint.getServerIp()+":"+sdwanEndpoint.getServerPort()+url;
		Map<String, String> httpHeaders = new HashMap<>();
		String auth = sdwanEndpoint.getServerUsername() + ":" + sdwanEndpoint.getServerPassword();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		httpHeaders.put("Authorization", authHeader);
		httpHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.put("Accept", MediaType.APPLICATION_JSON_VALUE);
		String requestString = Utils.convertObjectToJson(payload);
		Timestamp requestTime = new Timestamp(new Date().getTime());
		LOGGER.info("Create SLA profile URL {} and request {} " , url, request);
		RestResponse slaProfileCreateResponse = restClientService.postWithProxyBasicAuthentication(url, requestString, httpHeaders, 
				sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
		Timestamp responseTime = new Timestamp(new Date().getTime());
		if (slaProfileCreateResponse.getStatus() == Status.SUCCESS) {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndpoint.getServerCode(), url, "POST",
					requestString, HttpStatus.NO_CONTENT.value(), slaProfileCreateResponse.getData(), requestTime, responseTime,
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.SLA_PROFILE, null,
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.SLA_PROFILE, slaProfileName, null, null);
			LOGGER.info("Create SLA profile audit information has been saved");
			status =  Constants.SUCCESS;
		} else {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndpoint.getServerCode(), url, "POST",
					requestString, slaProfileCreateResponse.getStatus().getStatusCode(),
					slaProfileCreateResponse.getErrorMessage(), requestTime, responseTime,
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.SLA_PROFILE, null,
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.SLA_PROFILE, slaProfileName, null, null);
			LOGGER.info("Edit policy audit information has been saved with error");
			throw new TclCommonException(ExceptionConstants.VERSA_SLA_PROFILE_CREATION_FAILED);
		}
		return status;
	}

	/**
	 * To edit forwarding profile of a policy
	 *
	 * @param sdwanEndpoint
	 * @param editProfileRequest
	 * @param request
	 * @return
	 */
	private String editForwardingProfile(SdwanEndpoints sdwanEndpoint, ForwardingProfile editProfileRequest,
			SdwanPolicyDetailBean request) throws TclCommonException {
		LOGGER.info("Entering editForwardingProfile name {} ",editProfileRequest.getForwardingProfile().getName());
		String status = CommonConstants.FAILIURE;
		String url = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort() + versaEditFPUrl;
		url = url.replace("DYNAMICTEMPLATENAME",
				request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName());
		url = url.replace("DYNAMICORGNAME", request.getSdwanPolicyListBean().getOrganisationName());
		url = url.replace("DYNAMICFPNAME", editProfileRequest.getForwardingProfile().getName());
		Map<String, String> httpHeaders = new HashMap<>();
		String auth = sdwanEndpoint.getServerUsername() + ":" + sdwanEndpoint.getServerPassword();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		httpHeaders.put("Authorization", authHeader);
		httpHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.put("Accept", MediaType.APPLICATION_JSON_VALUE);
		String requestString = Utils.convertObjectToJson(editProfileRequest);
		LOGGER.info("Edit forwarding profile URL {} request {} " ,url,requestString);
		Timestamp requestTime = new Timestamp(new Date().getTime());
		RestResponse restResponse = restClientService.putWithBasicAuthentication(url, requestString, httpHeaders,
				sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
		Timestamp responseTime = new Timestamp(new Date().getTime());
		if (restResponse.getStatus() == Status.SUCCESS) {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndpoint.getServerCode(), url, "PUT",
					requestString, HttpStatus.NO_CONTENT.value(), restResponse.getData(), requestTime, responseTime,
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.FORWARDING_PROFILE, null,
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.FORWARDING_PROFILE, request.getProfileName(), null, null);
			LOGGER.info("Edit policy audit information has been saved");
			status =  Constants.SUCCESS;
		} else {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndpoint.getServerCode(), url, "PUT",
					Utils.convertObjectToJson(editProfileRequest), restResponse.getStatus().getStatusCode(),
					restResponse.getErrorMessage(), requestTime, responseTime,
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.FORWARDING_PROFILE, null,
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.FORWARDING_PROFILE, request.getProfileName(), null, null);
			LOGGER.info("Edit policy audit information has been saved with error");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR);
		}
		return status;
	}

	/**
	 * Create request body for saving forwarding profile
	 *
	 * @param forwardingProfile
	 * @param request
	 * @return
	 */
	private ForwardingProfile createRequestForEditFP(ForwardingProfile forwardingProfile, SdwanPolicyDetailBean request,
			String fpProfileName, String slaProfileName) {
		LOGGER.info("Inside createRequestForEditFP for FP profile name {}", fpProfileName);
		ForwardingProfile_ forwardingProfile_;
		if (Objects.nonNull(forwardingProfile) && Objects.nonNull(forwardingProfile.getForwardingProfile()))
			forwardingProfile_ = forwardingProfile.getForwardingProfile();
		else
			forwardingProfile_ = new ForwardingProfile_();
		forwardingProfile_.setName(fpProfileName);
		// resetting path priorities, request from UI is saved as it is
		forwardingProfile_.setCircuitPriorities(new CircuitPriorities());
		forwardingProfile_.getCircuitPriorities().setPriority(new ArrayList<>());
		request.getPathPriority().forEach(pathPriorities -> {
			Priority pathPriority = new Priority();
			pathPriority.setValue(pathPriorities.getPriority());
			CircuitNames circuitNames = new CircuitNames();
			circuitNames.setLocal(pathPriorities.getLocal());
			circuitNames.setRemote(pathPriorities.getRemote());
			pathPriority.setCircuitNames(circuitNames);
			forwardingProfile_.getCircuitPriorities().getPriority().add(pathPriority);
		});
		forwardingProfile_.setEncryption(request.getEncryptionMethod());
		forwardingProfile_.setLoadBalance(request.getLoadBalancingMethod());
		forwardingProfile_.setRecomputeTimer(request.getRecomputeTimer());
		forwardingProfile_.setSlaViolationAction(request.getThresholdViolationAction());
		forwardingProfile_.setSlaProfile(slaProfileName);
		if (Objects.isNull(forwardingProfile_.getReplication()))
			forwardingProfile_.setReplication(new Replication());
		forwardingProfile_.getReplication().setMode(request.getPacketReplicationMethod());
		forwardingProfile.setForwardingProfile(forwardingProfile_);
		return forwardingProfile;
	}

	/**
	 * Creating request payload for editing App Qos policies
	 *
	 * @param appQosPolicy
	 * @param request
	 * @return
	 */
	private QosPolicy createRequestForEditQos(AppQosPolicy appQosPolicy, SdwanPolicyDetailBean request) {
		if (Objects.isNull(appQosPolicy))
			appQosPolicy = new AppQosPolicy();
		if (Objects.isNull(appQosPolicy.getMatch()))
			appQosPolicy.setMatch(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Match());
		appQosPolicy.getMatch().setDscp(request.getDscp());
		if (Objects.isNull(appQosPolicy.getMatch().getApplication()))
			appQosPolicy.getMatch().setApplication(
					new QosPolicyApplication());
		appQosPolicy.getMatch().getApplication().setPredefinedApplicationList(request.getPredefinedApplications());
		appQosPolicy.getMatch().getApplication().setUserDefinedApplicationList(request.getUserdefinedApplications());

		if (Objects.isNull(appQosPolicy.getMatch().getUrlCategory()))
			appQosPolicy.getMatch().setUrlCategory(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.UrlCategory());
		appQosPolicy.getMatch().getUrlCategory().setPredefined(request.getPredefinedUrls());
		appQosPolicy.getMatch().getUrlCategory().setUserDefined(request.getUserDefinedUrls());

		if (Objects.isNull(appQosPolicy.getMatch().getSource()))
			appQosPolicy.getMatch()
					.setSource(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Source());

		if (Objects.isNull(appQosPolicy.getMatch().getSource().getAddress()))
			appQosPolicy.getMatch().getSource()
					.setAddress(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Address());
		AppQosPolicy finalRule = appQosPolicy;
		finalRule.getMatch().getSource().getAddress().setAddressList(new ArrayList<>());
		if (Objects.nonNull(request.getSourceAddress()))
			request.getSourceAddress().forEach(sourceAddress -> finalRule.getMatch().getSource().getAddress()
					.getAddressList().add(sourceAddress.getAddressName()));
		finalRule.getMatch().getSource().getAddress().setAddressGroupList(new ArrayList<>());
		if (Objects.nonNull(request.getSourceAddressGroups()))
			request.getSourceAddressGroups().forEach(sourceAddressGroup -> finalRule.getMatch().getSource().getAddress()
					.getAddressGroupList().add(sourceAddressGroup));
		if (Objects.isNull(finalRule.getMatch().getSource().getZone()))
			finalRule.getMatch().getSource()
					.setZone(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Zone());
		finalRule.getMatch().getSource().getZone().setZoneList(new ArrayList<>());
		if (Objects.nonNull(request.getSourceZones()))
			request.getSourceZones()
					.forEach(sourceZone -> finalRule.getMatch().getSource().getZone().getZoneList().add(sourceZone));

		if (Objects.isNull(finalRule.getMatch().getDestination()))
			finalRule.getMatch().setDestination(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Destination());

		if (Objects.isNull(finalRule.getMatch().getDestination().getAddress()))
			finalRule.getMatch().getDestination()
					.setAddress(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Address_());
		finalRule.getMatch().getDestination().getAddress().setAddressList(new ArrayList<>());
		if (Objects.nonNull(request.getDestinationAddress()))
			request.getDestinationAddress().forEach(destinationAddress -> finalRule.getMatch().getDestination()
					.getAddress().getAddressList().add(destinationAddress.getAddressName()));
		finalRule.getMatch().getDestination().getAddress().setAddressGroupList(new ArrayList<>());
		if (Objects.nonNull(request.getDestinationAddressGroups()))
			request.getDestinationAddressGroups().forEach(destAddressGroup -> finalRule.getMatch().getDestination()
					.getAddress().getAddressGroupList().add(destAddressGroup));
		if (Objects.isNull(finalRule.getMatch().getDestination().getZone()))
			finalRule.getMatch().getDestination()
					.setZone(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Zone());
		finalRule.getMatch().getDestination().getZone().setZoneList(new ArrayList<>());
		if (Objects.nonNull(request.getDestinationZones()))
			request.getDestinationZones()
					.forEach(destZone -> finalRule.getMatch().getDestination().getZone().getZoneList().add(destZone));
		QosPolicy requestPolicy = new QosPolicy();
		requestPolicy.setAppQosPolicy(new ArrayList<>());
		requestPolicy.getAppQosPolicy().add(appQosPolicy);
		return requestPolicy;
	}

	/**
	 * For editing App Qos policy (versa api)
	 *
	 * @param editRequest
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	private String editQosPolicy(SdwanEndpoints sdwanEndPointsForRegion, QosPolicy editRequest,
			SdwanPolicyDetailBean request) throws TclCommonException {
		String url = sdwanEndPointsForRegion.getServerIp() + ":" + sdwanEndPointsForRegion.getServerPort()
				+ versaEditQosPolicyUrl;
		url = url.replace("DYNAMICTEMPLATENAME",
				request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName());
		url = url.replace("DYNAMICORGNAME", request.getSdwanPolicyListBean().getOrganisationName());
		url = url.replace("DYNAMICRULENAME", request.getSdwanPolicyListBean().getPolicyName());
		Map<String, String> httpHeaders = new HashMap<>();
		httpHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.put("Accept", MediaType.APPLICATION_JSON_VALUE);
		String requestString = Utils.convertObjectToJson(editRequest);
		LOGGER.info("Edit App Qos request body : " + requestString);
		LOGGER.info("Edit App Qos Request url : " + url);
		Timestamp requestTime = new Timestamp(new Date().getTime());
		RestResponse restResponse = restClientService.putWithBasicAuthentication(url, requestString, httpHeaders,
				sdwanEndPointsForRegion.getServerUsername(), sdwanEndPointsForRegion.getServerPassword());
		Timestamp responseTime = new Timestamp(new Date().getTime());
		if (restResponse.getStatus() == Status.SUCCESS) {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndPointsForRegion.getServerCode(), url,
					"PUT",requestString, HttpStatus.NO_CONTENT.value(), restResponse.getData(), requestTime, responseTime,
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.POLICY,
					null, request.getSdwanPolicyListBean().getOrganisationName(), request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.POLICY, request.getSdwanPolicyListBean().getPolicyName(), null, null);
			LOGGER.info("Audit information has been saved");
			return Constants.SUCCESS;
		} else {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndPointsForRegion.getServerCode(), url, "PUT",
					requestString, restResponse.getStatus().getStatusCode(), restResponse.getData(), requestTime,
					responseTime, 
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.POLICY,
					null, request.getSdwanPolicyListBean().getOrganisationName(), request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.POLICY, request.getSdwanPolicyListBean().getPolicyName(), null, null);
			LOGGER.info("Audit information has been saved");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR);
		}
	}

	/**
	 * For editing traffic steering policy (Sdwan Policy)
	 *
	 * @param editRequest
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	private String editTrafficSteeringPolicy(SdwanEndpoints sdwanEndPointsForRegion, Rules_ editRequest,
			SdwanPolicyDetailBean request) throws TclCommonException {
		String url = sdwanEndPointsForRegion.getServerIp() + ":" + sdwanEndPointsForRegion.getServerPort()
				+ versaEditSdwanPolicyUrl;
		url = url.replace("DYNAMICTEMPLATENAME",
				request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName());
		url = url.replace("DYNAMICORGNAME", request.getSdwanPolicyListBean().getOrganisationName());
		url = url.replace("DYNAMICRULENAME", request.getSdwanPolicyListBean().getPolicyName());
		Map<String, String> httpHeaders = new HashMap<>();
		String auth = sdwanEndPointsForRegion.getServerUsername() + ":" + sdwanEndPointsForRegion.getServerPassword();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		httpHeaders.put("Authorization", authHeader);
		httpHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.put("Accept", MediaType.APPLICATION_JSON_VALUE);
		String requestString = Utils.convertObjectToJson(editRequest);
		LOGGER.info("Edit traffic steering request : " + requestString);
		LOGGER.info("Edit Traffic steering URL : " + url);
		Timestamp requestTime = new Timestamp(new Date().getTime());
		RestResponse restResponse = restClientService.putWithBasicAuthentication(url, requestString, httpHeaders,
				sdwanEndPointsForRegion.getServerUsername(), sdwanEndPointsForRegion.getServerPassword());
		Timestamp responseTime = new Timestamp(new Date().getTime());
		if (restResponse.getStatus() == Status.SUCCESS) {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndPointsForRegion.getServerCode(), url, "PUT",
					requestString, HttpStatus.NO_CONTENT.value(), restResponse.getData(), requestTime, responseTime,
					CommonConstants.EDIT+CommonConstants.HYPHEN+ServiceInventoryConstants.POLICY,
					null, request.getSdwanPolicyListBean().getOrganisationName(), request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.POLICY, request.getSdwanPolicyListBean().getPolicyName(),null, null);
			LOGGER.info("Edit policy audit information has been saved");
			return Constants.SUCCESS;
		} else {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndPointsForRegion.getServerCode(), url, "PUT",
					Utils.convertObjectToJson(editRequest), restResponse.getStatus().getStatusCode(),
					restResponse.getErrorMessage(), requestTime, responseTime,
					CommonConstants.EDIT+CommonConstants.HYPHEN+ServiceInventoryConstants.POLICY,
					null, request.getSdwanPolicyListBean().getOrganisationName(), request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.POLICY, request.getSdwanPolicyListBean().getPolicyName(), null, null);
			LOGGER.info("Edit policy audit information has been saved with error");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR);
		}
	}

	/**
	 * Create request payload for edit traffic steering policy (SDWAN policy) for
	 * versa
	 *
	 * @param rule
	 * @param request
	 * @return
	 */
	private Rules_ createRequestForEditTS(Rule rule, SdwanPolicyDetailBean request, String fpProfileName) {
		if (Objects.isNull(rule))
			rule = new Rule();
		if (Objects.isNull(rule.getMatch()))
			rule.setMatch(new Match());
		rule.getMatch().setDscp(request.getDscp());
		if (Objects.isNull(rule.getMatch().getApplication()))
			rule.getMatch().setApplication(new Application());
		rule.getMatch().getApplication().setPredefinedApplicationList(request.getPredefinedApplications());
		rule.getMatch().getApplication().setUserDefinedApplicationList(request.getUserdefinedApplications());

		if (Objects.isNull(rule.getMatch().getUrlCategory()))
			rule.getMatch().setUrlCategory(new UrlCategory());
		rule.getMatch().getUrlCategory().setPredefined(request.getPredefinedUrls());
		rule.getMatch().getUrlCategory().setUserDefined(request.getUserDefinedUrls());

		if (Objects.isNull(rule.getMatch().getSource()))
			rule.getMatch().setSource(new Source());

		if (Objects.isNull(rule.getMatch().getSource().getAddress()))
			rule.getMatch().getSource().setAddress(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Address());
		if (Objects.isNull(rule.getSet()))
			rule.setSet(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Set());
		
		Rule finalRule = rule;
		finalRule.getMatch().getSource().getAddress().setAddressList(new ArrayList<>());
		if (Objects.nonNull(request.getSourceAddress()))
			request.getSourceAddress().forEach(sourceAddress -> finalRule.getMatch().getSource().getAddress()
					.getAddressList().add(sourceAddress.getAddressName()));
		finalRule.getMatch().getSource().getAddress().setAddressGroupList(new ArrayList<>());
		if (Objects.nonNull(request.getSourceAddressGroups()))
			request.getSourceAddressGroups().forEach(sourceAddressGroup -> finalRule.getMatch().getSource().getAddress()
					.getAddressGroupList().add(sourceAddressGroup));
		if (Objects.isNull(finalRule.getMatch().getSource().getZone()))
			finalRule.getMatch().getSource().setZone(new Zone());
		finalRule.getMatch().getSource().getZone().setZoneList(new ArrayList<>());
		if (Objects.nonNull(request.getSourceZones()))
			request.getSourceZones()
					.forEach(sourceZone -> finalRule.getMatch().getSource().getZone().getZoneList().add(sourceZone));

		if (Objects.isNull(finalRule.getMatch().getDestination()))
			finalRule.getMatch().setDestination(new Destination());

		if (Objects.isNull(finalRule.getMatch().getDestination().getAddress()))
			finalRule.getMatch().getDestination().setAddress(new Address_());
		finalRule.getMatch().getDestination().getAddress().setAddressList(new ArrayList<>());
		if (Objects.nonNull(request.getDestinationAddress()))
			request.getDestinationAddress().forEach(destinationAddress -> finalRule.getMatch().getDestination()
					.getAddress().getAddressList().add(destinationAddress.getAddressName()));
		finalRule.getMatch().getDestination().getAddress().setAddressGroupList(new ArrayList<>());
		if (Objects.nonNull(request.getDestinationAddressGroups()))
			request.getDestinationAddressGroups().forEach(destAddressGroup -> finalRule.getMatch().getDestination()
					.getAddress().getAddressGroupList().add(destAddressGroup));
		if (Objects.isNull(finalRule.getMatch().getDestination().getZone()))
			finalRule.getMatch().getDestination().setZone(new Zone());
		finalRule.getMatch().getDestination().getZone().setZoneList(new ArrayList<>());
		if (Objects.nonNull(request.getDestinationZones()))
			request.getDestinationZones()
					.forEach(destZone -> finalRule.getMatch().getDestination().getZone().getZoneList().add(destZone));
		if(Objects.nonNull(finalRule.getSet()))
			finalRule.getSet().setForwardingProfile(fpProfileName);
		Rules_ requestRule = new Rules_();
		requestRule.setRule(new ArrayList<>());
		requestRule.getRule().add(rule);
		return requestRule;
	}
	
	/**
	 * Method to create update and clone user defined applications
	 * 
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param versaUserDefAppRequest
	 * @param action
	 * @return
	 * @throws TclCommonException
	 */
	public TemplateCpeStatusResponse createUserDefinedApps(Integer productId, Integer customerId, Integer customerLeId,
			Integer partnerId, VersaUserDefAppRequest versaUserDefAppRequest, String action) throws TclCommonException {
		String response = CommonConstants.FAILIURE;
		TemplateCpeStatusResponse cpeStatusResponse = new TemplateCpeStatusResponse();
		try {
			LOGGER.info("Entering createUserDefinedApps method to {} userdef appls in VERSA ", action);
			List<Integer> customerLeIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();
			getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
			customerLeIds.add(customerLeId);
			partnerLeIds.add(partnerId);
			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId
					+ "partnerLeIds " + partnerLeIds);
//			List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findServiceAttributesForCustomer(
//					customerLeIds, partnerLeIds, productId, customerId, partnerId,
//					Arrays.asList(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME,
//							ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
//							ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
			List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findSdwanServiceAttributes(
					customerLeIds, partnerLeIds, productId, customerId, partnerId,
					Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME, ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING),
					Arrays.asList(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME));
			List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			serviceAttributes.stream().forEach(attribute -> {
				vwServiceAttributes.add(mapper.convertValue(attribute, VwServiceAttributesBean.class));
			});
			Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped = vwServiceAttributes.stream()
					.collect(Collectors.groupingBy(VwServiceAttributesBean::getSysId));
			List<SdwanEndpoints> sdwanEndPointsForRegions = new ArrayList<>();
			Map<String, List<SdwanEndpoints>> instanceByCode = getSdwanEnpointsByCode(serviceAttrGrouped,
					sdwanEndPointsForRegions);
			Map<String, Set<String>> templatesRegionMapping = new HashMap<>();
			String templateName[] = { "" };
			String region[] = { "" };
			serviceAttrGrouped.entrySet().forEach(serviceAttr -> {
				serviceAttr.getValue().forEach(attributes -> {
					if (ServiceInventoryConstants.SDWAN_TEMPLATE_NAME.equalsIgnoreCase(attributes.getUnderlayAttributeName()))
						templateName[0] = attributes.getUnderlayAttributeValue();
					if (ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING
							.equalsIgnoreCase(attributes.getAttributeName()))
						region[0] = attributes.getAttributeValue();
					if (!StringUtils.isAllBlank(templateName[0])
							&& !templatesRegionMapping.containsKey(templateName[0]))
						templatesRegionMapping.put(templateName[0], new HashSet<>());
					if (!StringUtils.isAllBlank(region[0]) && !StringUtils.isAllBlank(templateName[0])) {
						templatesRegionMapping.get(templateName[0]).add(region[0]);
						LOGGER.info("Inside create user appls and processing template name {} and region {} ", templateName[0], region[0]);
						templateName[0] = "";
						region[0] = "";
					}
				});
			});
			Map<String, Set<String>> orgTemplates = new HashMap<>();
			Map<String, Set<String>> orgDirectoryRegions = new HashMap<>();
			Set<String> tempNames = new HashSet<>();
			Set<String> directoryCodes = new HashSet<>();
			getSdwanAttributesForVersa(serviceAttrGrouped, orgTemplates, orgDirectoryRegions, tempNames,
					directoryCodes);
			Map<String, Set<String>> cpeGroupedByTemplateName = getCpeByTemplateFromVersa(instanceByCode, orgTemplates,
					orgDirectoryRegions);
			Map<String, Set<DeviceGroupDatum>> cpeAvailabilityByTemplate = checkTemplateSyncStatusByCpe(
					cpeGroupedByTemplateName, instanceByCode, orgTemplates, orgDirectoryRegions);
			if (serviceAttributes != null && !serviceAttributes.isEmpty()) {
				if (action.equals(CommonConstants.CREATE) || action.equals(CommonConstants.CLONE)) {
					getUserDefApplFromVersa(versaUserDefAppRequest, instanceByCode, orgTemplates, orgDirectoryRegions,
							customerId, action);
					response = CommonConstants.SUCCESS;
				} else if (action.equals(CommonConstants.UPDATE)) {
					response = updateVersaUSerDefAppls(versaUserDefAppRequest, action, customerId, response,
							instanceByCode, orgTemplates, orgDirectoryRegions);
					response = CommonConstants.SUCCESS;
				}
				if (response.equals(CommonConstants.SUCCESS)) {
					cpeStatusResponse = checkSyncAndCommitTemplate(cpeAvailabilityByTemplate, customerId,
							instanceByCode, orgTemplates, orgDirectoryRegions, ServiceInventoryConstants.APPLICATION,
							versaUserDefAppRequest.getUserDefinedApplications().getName(), templatesRegionMapping);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return cpeStatusResponse;
	}

	/**
	 * Check status of devices for each template and commit templates
	 *
	 *
	 * @param cpeAvailabilityByTemplate
	 * @param customerId
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @param templatesRegionMapping
	 * @return
	 */
	private TemplateCpeStatusResponse checkSyncAndCommitTemplate(
			Map<String, Set<DeviceGroupDatum>> cpeAvailabilityByTemplate, Integer customerId,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions, String operation, String operationValue,
			Map<String, Set<String>> templatesRegionMapping) {
		TemplateCpeStatusResponse templateCpeStatusResponse = new TemplateCpeStatusResponse();
		templateCpeStatusResponse.setCreateUpdateStatus(CommonConstants.SUCCESS);
		LOGGER.info("Inside checkSyncAndCommitTemplate and template and insync cpe's {} ", cpeAvailabilityByTemplate);
		if (ServiceInventoryConstants.APPLICATION.equalsIgnoreCase(operation))
			templateCpeStatusResponse = izoSdwanAsyncTasks.commitTemplateOnCpe(customerId, cpeAvailabilityByTemplate,
					instanceByCode, orgTemplates, orgDirectoryRegions, operation, operationValue,
					userInfoUtils.getUserInformation().getUserId(), getCustomerLeIds(), templatesRegionMapping);
		else if (ServiceInventoryConstants.POLICY.equalsIgnoreCase(operation))
			templateCpeStatusResponse = commitTemplateOnCpe(customerId, cpeAvailabilityByTemplate, instanceByCode,
					orgTemplates, orgDirectoryRegions, operation, operationValue);
		return templateCpeStatusResponse;
	}

	/**
	 * Commit templates on In-Sync CPEs
	 *
	 * @param customerId
	 * @param cpeAvailabilityByTemplate
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @return
	 */
	private TemplateCpeStatusResponse commitTemplateOnCpe(Integer customerId,
			Map<String, Set<DeviceGroupDatum>> cpeAvailabilityByTemplate,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions, String operation, String operationValue) {
		TemplateCpeStatusResponse templateCpeStatusResponse = new TemplateCpeStatusResponse();
		templateCpeStatusResponse.setTemplates(new ArrayList<>());
		templateCpeStatusResponse.setInSyncCpes(new ArrayList<>());
		templateCpeStatusResponse.setOutOfSyncCpes(new ArrayList<>());
		templateCpeStatusResponse.setTaskIds(new ArrayList<>());
		orgTemplates.entrySet().forEach(orgTemplate -> {
			orgTemplate.getValue().forEach(template -> {
				templateCpeStatusResponse.getTemplates().add(template);
				String commitTemplatesUrl = commitTemplateUrl;
				commitTemplatesUrl = commitTemplatesUrl.replaceAll("DYNAMICTEMPLATENAME", template);
				Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
				SdwanEndpoints sdwanEndpoint = instanceByCode.get(serverCode.stream().findFirst().get()).get(0);
				commitTemplatesUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
						+ commitTemplatesUrl;
				if (cpeAvailabilityByTemplate.get(template) != null
						&& !cpeAvailabilityByTemplate.get(template).isEmpty()) {
					Map<String, Set<String>> outOfSyncCpeByTemplate = new HashMap<>();
					CommitTemplateRequest commitTemplateRequest = checkCpeStatusConstructCommitRequest(template,
							cpeAvailabilityByTemplate.get(template), outOfSyncCpeByTemplate, templateCpeStatusResponse);
					Timestamp responseTime = new Timestamp(new Date().getTime());
					Timestamp requestTime = new Timestamp(new Date().getTime());
					try {
						LOGGER.info("Commit template VERSA URL {} ", commitTemplatesUrl);
						String requestBody = Utils.convertObjectToJson(commitTemplateRequest);
						LOGGER.info("Commit template VERSA Request {} ", requestBody);
						if (!commitTemplateRequest.getVersanmsDevices().getDeviceList().isEmpty()) {
							requestTime = new Timestamp(new Date().getTime());
							RestResponse response = restClientService.postWithBasicAuthentication(commitTemplatesUrl,
									requestBody, new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
									sdwanEndpoint.getServerPassword());
							responseTime = new Timestamp(new Date().getTime());
							if (response.getStatus() == Status.SUCCESS) {
								TemplateCommitResponse templateInSyncStatus = Utils
										.convertJsonToObject(response.getData(), TemplateCommitResponse.class);
								templateCpeStatusResponse.getTaskIds()
										.add(templateInSyncStatus.getVersanmsTemplateResponse().getTaskId());
								saveAuditInfoSdwan(customerId, null, null, null, serverCode.stream().findFirst().get(),
										commitTemplatesUrl, "POST", requestBody, HttpStatus.OK.value(),
										response.getData(), requestTime, responseTime,
										ServiceInventoryConstants.COMMIT_TEMPLATE_IN_SYNC, null, orgTemplate.getKey(),
										template, operation, operationValue,
										templateInSyncStatus.getVersanmsTemplateResponse().getTaskId(), null);
							}
						}
						if (!outOfSyncCpeByTemplate.isEmpty()
								&& Objects.nonNull(outOfSyncCpeByTemplate.get(template))) {
							saveAuditInfoSdwan(customerId, null, null, null, serverCode.stream().findFirst().get(), null,
									null, Utils.convertObjectToJson(outOfSyncCpeByTemplate.get(template)), null, null,
									requestTime, responseTime, ServiceInventoryConstants.COMMIT_TEMPLATE_OUT_SYNC, null,
									orgTemplate.getKey(), template, operation, operationValue, null, null);
						}
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.VERSA_USER_APPL_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
				}
			});
		});
		return templateCpeStatusResponse;
	}

	/**
	 * Check CPE status, add to list in request if status is IN_SYNC
	 *
	 * @param templateName
	 * @param deviceGroupData
	 * @param outOfSyncCpeByTemplate
	 * @param templateCpeStatusResponse
	 * @return
	 */
	protected CommitTemplateRequest checkCpeStatusConstructCommitRequest(String templateName,
			Set<DeviceGroupDatum> deviceGroupData, Map<String, Set<String>> outOfSyncCpeByTemplate,
			TemplateCpeStatusResponse templateCpeStatusResponse) {
		LOGGER.info("Inside checkCpeStatusConstructCommitRequest for templateName {} and MDC token {} ", templateName, MDC.get(CommonConstants.MDC_TOKEN_KEY));
		CommitTemplateRequest request = new CommitTemplateRequest();
		request.setVersanmsDevices(new VersanmsDevices());
		request.getVersanmsDevices().setDeviceList(new ArrayList<>());
		Set<String> outOfSyncCpes = new HashSet<>();
		if (Objects.nonNull(deviceGroupData)) {
			deviceGroupData.stream().filter(deviceGroup -> Objects.nonNull(deviceGroup.getDeviceData()))
					.forEach(deviceGroup -> {
						deviceGroup.getDeviceData().stream().filter(Objects::nonNull).forEach(cpe -> {
							if (ServiceInventoryConstants.IN_SYNC.equals(cpe.getStatus())) {
								request.getVersanmsDevices().getDeviceList().add(cpe.getName());
								templateCpeStatusResponse.getInSyncCpes().add(cpe.getName());
							} else {
								outOfSyncCpes.add(cpe.getName());
								outOfSyncCpeByTemplate.put(templateName, outOfSyncCpes);
								templateCpeStatusResponse.getOutOfSyncCpes().add(cpe.getName());
							}
						});
					});
		}
		LOGGER.info("checkCpeStatusConstructCommitRequest template name {} Insync cpe {} and out of sync cpe {} ", templateName,
				templateCpeStatusResponse.getInSyncCpes(), 
				templateCpeStatusResponse.getOutOfSyncCpes());
		return request;
	}

	/**
	 * Check CPE sync status for each template in Versa
	 *
	 * @param cpeGroupedByTemplateName
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @return
	 */
	private Map<String, Set<DeviceGroupDatum>> checkTemplateSyncStatusByCpe(
			Map<String, Set<String>> cpeGroupedByTemplateName, Map<String, List<SdwanEndpoints>> instanceByCode,
			Map<String, Set<String>> orgTemplates, Map<String, Set<String>> orgDirectoryRegions) {
		Map<String, Set<DeviceGroupDatum>> cpeAvailabilityByTemplate = new HashMap<>();
		Set<String> recurringTemplates = new HashSet<>();
		orgTemplates.entrySet().forEach(orgTemplate -> {
			orgTemplate.getValue().forEach(template -> {
				orgDirectoryRegions.get(orgTemplate.getKey()).forEach(region -> {
					instanceByCode.get(region).forEach(instance -> {
						LOGGER.info("Entering checkTemplateSyncStatusByCpe to fetch status for template {} ", template);
						String statusUrl = templateSyncStatusUrl;
						SdwanEndpoints sdwanEndpoint = instance;
						statusUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort() + statusUrl;
						try {
							LOGGER.info("Template In-Sync Status VERSA URL {} ", statusUrl);
							String requestBody = null;
							if (Objects.nonNull(cpeGroupedByTemplateName.get(template))
									&& !cpeGroupedByTemplateName.get(template).isEmpty()) {
								try {
									requestBody = constructSyncStatusRequest(cpeGroupedByTemplateName.get(template),template);
								} catch (TclCommonException e) {
									e.printStackTrace();
								}
								LOGGER.info("Template In-Sync Status VERSA URL {} Request {} ", statusUrl, requestBody);
								RestResponse response = restClientService.postWithBasicAuthentication(statusUrl,
										requestBody, new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
										sdwanEndpoint.getServerPassword());
								if (response.getStatus() == Status.SUCCESS) {
									TemplateInSyncStatus templateInSyncStatus = null;
									try {
										templateInSyncStatus = Utils.convertJsonToObject(response.getData(),
												TemplateInSyncStatus.class);
									} catch (TclCommonException e) {
										e.printStackTrace();
									}
									if (Objects.nonNull(templateInSyncStatus.getVersanmsDeviceGroups())
											&& Objects.nonNull(templateInSyncStatus.getVersanmsDeviceGroups()
													.getDeviceGroupData())) {
										if (!recurringTemplates.contains(template)) {
											cpeAvailabilityByTemplate.put(template, new HashSet<>(templateInSyncStatus
													.getVersanmsDeviceGroups().getDeviceGroupData()));
											recurringTemplates.add(template);
										} else
											cpeAvailabilityByTemplate.get(template).addAll(templateInSyncStatus
													.getVersanmsDeviceGroups().getDeviceGroupData());
									}
									LOGGER.info("Template sync status received and template insync {} ",cpeAvailabilityByTemplate);
								} else {
									LOGGER.info("Error occurred while getting template sync status {} ",
											response.getErrorMessage());
								}
							}
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.VERSA_USER_APPL_ERROR,
									ResponseResource.R_CODE_ERROR);
						}
					});
				});
			});
		});
		return cpeAvailabilityByTemplate;
	}

	/**
	 * To construct request body for sync status API (Versa)
	 *
	 * @param cpeNames
	 * @return
	 * @throws TclCommonException
	 */
	private String constructSyncStatusRequest(Set<String> cpeNames, String template) throws TclCommonException {
		TemplateInSyncStatus request = new TemplateInSyncStatus();
		if (Objects.nonNull(cpeNames) && !cpeNames.isEmpty()) {
			request.setVersanmsDeviceGroups(new VersanmsDeviceGroups());
			request.getVersanmsDeviceGroups().setDeviceGroupData(new ArrayList<>());
			request.getVersanmsDeviceGroups().setTemplateName(template);
			cpeNames.forEach(cpe -> {
				DeviceGroupDatum deviceGroupDatum = new DeviceGroupDatum();
				deviceGroupDatum.setName(cpe);
				request.getVersanmsDeviceGroups().getDeviceGroupData().add(deviceGroupDatum);
			});
		}
		return Utils.convertObjectToJson(request);
	}

	/**
	 * Get list of CPEs tagged to templates from Versa
	 *
	 * @param customerId
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @return
	 */
	private Map<String, Set<String>> getCpeByTemplateFromVersa(Map<String, List<SdwanEndpoints>> instanceByCode,
			Map<String, Set<String>> orgTemplates, Map<String, Set<String>> orgDirectoryRegions) {
		Map<String, Set<String>> cpeGroupedByTemplateName = new HashMap<>();
		Set<String> recurringTemplates = new HashSet<>();
		orgTemplates.entrySet().forEach(orgTemplate -> {
			orgTemplate.getValue().forEach(template -> {
				orgDirectoryRegions.get(orgTemplate.getKey()).forEach(region -> {
					instanceByCode.get(region).forEach(instance -> {
						String cpeByTemplateUrl = cpeGroupedByTemplateUrl;
						cpeByTemplateUrl = cpeByTemplateUrl.replaceAll("DYNAMICTEMPLATENAME", template);
						SdwanEndpoints sdwanEndpoint = instance;
						cpeByTemplateUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
								+ cpeByTemplateUrl;
						LOGGER.info("Entering getCpeByTemplateFromVersa to fetch cpe group by template URL {} ",cpeByTemplateUrl);
						try {
							LOGGER.info("Cpe group by template name VERSA URL {} ", cpeByTemplateUrl);
							RestResponse response = restClientService.getWithBasicAuthentication(cpeByTemplateUrl,
									new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
									sdwanEndpoint.getServerPassword());
							if (response.getStatus() == Status.SUCCESS) {
								CpeGroupByTemplate cpeGroupByTemplate = Utils.convertJsonToObject(response.getData(),
										CpeGroupByTemplate.class);
								if (Objects.nonNull(cpeGroupByTemplate)
										&& Objects.nonNull(cpeGroupByTemplate.getDevicegroupTemplateMapping())
										&& Objects.nonNull(
												cpeGroupByTemplate.getDevicegroupTemplateMapping().getDeviceGroup())) {
									if (!recurringTemplates.contains(template)) {
										cpeGroupedByTemplateName.put(template,
												cpeGroupByTemplate.getDevicegroupTemplateMapping().getDeviceGroup());
										recurringTemplates.add(template);
									} else
										cpeGroupedByTemplateName.get(template).addAll(
												cpeGroupByTemplate.getDevicegroupTemplateMapping().getDeviceGroup());
								}
								LOGGER.info("Cpe groups by template name received");
							} else {
								LOGGER.info("Error occurred while getting Cpe groups");
							}
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.VERSA_USER_APPL_ERROR,
									ResponseResource.R_CODE_ERROR);
						}
					});
				});
			});
		});
		return cpeGroupedByTemplateName;
	}

	/**
	 * Get customer Le IDs and Partner Le Ids
	 *
	 * @param customerLeIds
	 * @param partnerLeIds
	 * @param customerId
	 */
	public void getCustomerAndPartnerLeIds(List<Integer> customerLeIds, List<Integer> partnerLeIds,
			Integer customerLeId) {
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			partnerLeIds.addAll(new ArrayList<>(getPartnerLeIds()));
			customerLeIds.addAll(Collections.singletonList((Integer) null));
		} else {
			customerLeIds.addAll(new ArrayList<>(getCustomerLeIds()));
			partnerLeIds.addAll(Collections.singletonList((Integer) null));
		}

		if (Objects.nonNull(customerLeId)) {
			customerLeIds.clear();
			customerLeIds.add(customerLeId);
		}
	}

	/**
	 * Create user defined application from Versa
	 *
	 * @param versaUserDefAppRequest
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @param customerId
	 * @param action
	 */
	private void getUserDefApplFromVersa(VersaUserDefAppRequest versaUserDefAppRequest,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions, Integer customerId, String action) {
		LOGGER.info("Inside getUserDefApplFromVersa  to {} appl {} for customerID {} ", action, 
				versaUserDefAppRequest.getUserDefinedApplications().getName(), customerId);
		orgTemplates.entrySet().forEach(orgTemplate -> {
			orgDirectoryRegions.get(orgTemplate.getKey()).forEach(region -> {
				instanceByCode.get(region).forEach(instance -> {
					String createUserDefAppUrl = createUserApplCommonTempUrl;
					createUserDefAppUrl = createUserDefAppUrl.replaceAll("DYNAMICORGNAME", orgTemplate.getKey());
					Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
					SdwanEndpoints sdwanEndpoint = instance;
					createUserDefAppUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
							+ createUserDefAppUrl;
					try {
						versaUserDefAppRequest.getUserDefinedApplications().setFamily("business-system");
						versaUserDefAppRequest.getUserDefinedApplications().setSubfamily("antivirus");
						versaUserDefAppRequest.getUserDefinedApplications().setProductivity("1");
						LOGGER.info("Create User defined appl in VERSA URL {} ", createUserDefAppUrl);
						Timestamp requestTime = new Timestamp(new Date().getTime());
						RestResponse userDefinedApps = restClientService.postWithBasicAuthentication(
								createUserDefAppUrl, Utils.convertObjectToJson(versaUserDefAppRequest),
								new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
								sdwanEndpoint.getServerPassword());
						Timestamp responseTime = new Timestamp(new Date().getTime());
						LOGGER.info(" Create userDefineAppls Response {} ", userDefinedApps.getData());
						if (userDefinedApps.getStatus() == Status.SUCCESS) {
							saveAuditInfoSdwan(customerId, null, null, null, sdwanEndpoint.getServerCode(),
									createUserDefAppUrl, "POST", Utils.convertObjectToJson(versaUserDefAppRequest),
									HttpStatus.CREATED.value(), userDefinedApps.getData(), requestTime, responseTime,
									action + CommonConstants.HYPHEN + ServiceInventoryConstants.APPLICATION, null,
									orgTemplate.getKey(),
									orgTemplate.getKey() + ServiceInventoryConstants.COMMON_TEMPLATE,
									ServiceInventoryConstants.APPLICATION,
									versaUserDefAppRequest.getUserDefinedApplications().getName(), null, null);
							LOGGER.info("Create/ clone application audit information has been saved");
						} else {
							saveAuditInfoSdwan(customerId, null, null, null, sdwanEndpoint.getServerCode(),
									createUserDefAppUrl, "POST", Utils.convertObjectToJson(versaUserDefAppRequest),
									HttpStatus.INTERNAL_SERVER_ERROR.value(), userDefinedApps.getErrorMessage(),
									requestTime, responseTime,
									action + CommonConstants.HYPHEN + ServiceInventoryConstants.APPLICATION
											+ versaUserDefAppRequest.getUserDefinedApplications().getName(),
									null, orgTemplate.getKey(),
									orgTemplate.getKey() + ServiceInventoryConstants.COMMON_TEMPLATE,
									ServiceInventoryConstants.APPLICATION,
									versaUserDefAppRequest.getUserDefinedApplications().getName(), null, null);
							LOGGER.info("Create application audit information has been saved with error");
//							throw new TclCommonRuntimeException(ExceptionConstants.VERSA_USER_APPL_ERROR, ResponseResource.R_CODE_ERROR); 
						}
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.VERSA_USER_APPL_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
		});
	}

	/**
	 * Method to get servce attributes for versa
	 * @param serviceAttrGrouped
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @param tempNames
	 * @param directoryCodes
	 */
	private void getSdwanAttributesForVersa(Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped,
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
						&& value.getUnderlayAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME)) {
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
	private Map<String, List<SdwanEndpoints>> getSdwanEnpointsByCode(
			Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped, List<SdwanEndpoints> sdwanEndPointsForRegions) {
		Set<String> instanceRegions = new HashSet<>();
		serviceAttrGrouped.entrySet().forEach(attr->{
			attr.getValue().stream().forEach(attrValue->{
				if(attrValue.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING)) {
					if(attrValue.getAttributeValue() != null)
						instanceRegions.add(attrValue.getAttributeValue());
				}
			});
		});
		LOGGER.info("Inside getSdwanEnpointsByCode fetching sdwan endpoints for server codes {} ",instanceRegions);
		sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
		Map<String, List<SdwanEndpoints>> instanceByCode =  sdwanEndPointsForRegions.stream().collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		// Instance mapping value should be same in sdwan_endpoints table
		return instanceByCode;
	}
	
	/**
	 * Method to get application names from versa
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param productId
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public VersaApplicationNames sdwanApplicationsFromVersa(Integer customerId, Integer partnerId,Integer customerLeId, Integer productId,
			SdwanPolicyListBean sdwanPolicyListBean) throws TclCommonException {
		VersaApplicationNames sdwanApplications = new VersaApplicationNames();
		try {
			List<Integer> customerLeIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();

			getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId
					+ "partnerLeIds " + partnerLeIds);
			Set<String> userDefinedAppls = new HashSet<>();
			Set<String> predefinedAppls = new HashSet<>();
			Set<String> associtedUserDefAppls = new HashSet<>();
			Set<String> associtedPredefinedAppls = new HashSet<>();
			sdwanApplications.setUserDefinedApplications(userDefinedAppls);
			sdwanApplications.setPredefinedApplications(predefinedAppls);
			List<Map<String, Object>> serviceAttributes = vwSiServiceInfoAllRepository.findSdwanServiceAttributes(
					customerLeIds, partnerLeIds, productId, customerId, partnerId,
					Arrays.asList(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING,
							ServiceInventoryConstants.SDWAN_ORGANISATION_NAME),
					Arrays.asList(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME));
			List<VwServiceAttributesBean> vwServiceAttributes = new ArrayList<>();
			List<SdwanEndpoints> sdwanEndpoints = new ArrayList<>();
			if(serviceAttributes != null && !serviceAttributes.isEmpty()) {
				ObjectMapper mapper = new ObjectMapper();
				serviceAttributes.stream().forEach(attribute->{
					vwServiceAttributes.add(mapper.convertValue(attribute, VwServiceAttributesBean.class));
				});
				LOGGER.info("Attributes for customer : {}", vwServiceAttributes.toString());
				List<SdwanEndpoints> sdwanEndPointsForRegions = new ArrayList<>();
				Map<Integer, List<VwServiceAttributesBean>> serviceAttrGrouped = vwServiceAttributes.stream()
						.collect(Collectors.groupingBy(VwServiceAttributesBean::getSysId));
				Map<String, List<SdwanEndpoints>> instanceByCode = getSdwanEnpointsByCode(serviceAttrGrouped,
						sdwanEndPointsForRegions);
				Map<String, Set<String>> orgTemplates = new HashMap<>();
				Map<String, Set<String>> orgDirectoryRegions = new HashMap<>();
				Set<String> tempNames = new HashSet<>();
				Set<String> directoryCodes = new HashSet<>();
				getSdwanAttributesForVersa(serviceAttrGrouped, orgTemplates, orgDirectoryRegions, tempNames,
						directoryCodes);
				List<String> versaDirectorRegion = new ArrayList<>();
				persistVersaUserDefinedApplCommonTemplate(userDefinedAppls, instanceByCode, orgTemplates,
						orgDirectoryRegions, versaDirectorRegion);
				sdwanEndpoints.add(0, instanceByCode.get(versaDirectorRegion.get(0)).get(0));
//				persistVersaUserDefinedAppls(userDefinedAppls, instanceByCode, orgTemplates, orgDirectoryRegions);
//				if(!instanceByCode.isEmpty()) {
//					getVersaPredefinedApplNames(predefinedAppls, instanceByCode);
//				}
				if (!versaDirectorRegion.isEmpty()) {
					RestResponse preDefinedApps = getVersaBuiltInAppls(sdwanEndpoints.get(0));
					if (preDefinedApps.getStatus() == Status.SUCCESS && preDefinedApps.getData() != null) {
						VersaApplicationsResponse preDefinedApplications = Utils
								.convertJsonToObject(preDefinedApps.getData(), VersaApplicationsResponse.class);
						if (Objects.nonNull(preDefinedApplications)
								&& Objects.nonNull(preDefinedApplications.getApplications()))
							preDefinedApplications.getApplications().stream().forEach(apps -> {
								predefinedAppls.add(apps.getName());
							});
					}
				}
			}

			getPolicyAssociatedAppls(sdwanPolicyListBean, sdwanApplications, userDefinedAppls, predefinedAppls,
					associtedUserDefAppls, associtedPredefinedAppls, sdwanEndpoints.get(0));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return sdwanApplications;
	}

	/**
	 * Method to persist user defined appls names
	 * 
	 * @param userDefinedAppls
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @throws TclCommonException
	 */
	private void persistVersaUserDefinedApplCommonTemplate(Set<String> userDefinedAppls,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions, List<String> versaDirectorRegion) throws TclCommonException {
		try {
			orgDirectoryRegions.entrySet().forEach(orgDirectors -> {
				versaDirectorRegion.add(0, orgDirectors.getValue().stream().findAny().orElse(null));
				if (!versaDirectorRegion.isEmpty()) {
					String userDefinedAppUrl = versaGetUserApplsUrl;
					userDefinedAppUrl = userDefinedAppUrl.replace("DYNAMICTEMPLATENAME",
							orgDirectors.getKey() + ServiceInventoryConstants.COMMON_TEMPLATE);
					userDefinedAppUrl = userDefinedAppUrl.replace("DYNAMICORGNAME", orgDirectors.getKey());
					SdwanEndpoints sdwanEndpoint = instanceByCode.get(versaDirectorRegion.get(0)).get(0);
					userDefinedAppUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
							+ userDefinedAppUrl;
					try {
						LOGGER.info("Invoking VERSA to fetch user def appls for org common template {} ",
								userDefinedAppUrl);
						RestResponse userDefinedApps = restClientService.getWithBasicAuthentication(userDefinedAppUrl,
								new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
								sdwanEndpoint.getServerPassword());
						LOGGER.info("userDefineResponse {} ", userDefinedApps.getData());
						if (userDefinedApps.getStatus() == Status.SUCCESS && userDefinedApps.getData() != null) {
							VersaUserDefinedAppsResponse userDefinedResp = Utils
									.convertJsonToObject(userDefinedApps.getData(), VersaUserDefinedAppsResponse.class);
							userDefinedResp.getUserDefinedApplications().stream().forEach(userApps -> {
								userDefinedAppls.add(userApps.getName());
							});
						}

					} catch (Exception e) {
						LOGGER.error("Exception while parsing user defines applications from versa", e);
					}

				} else {
					throw new TclCommonRuntimeException(ExceptionConstants.VERSA_DIRECTOR_MAPPING_NOT_AVAILABLE,
							ResponseResource.R_CODE_ERROR);
				}

			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Method to get appl names associated to policy
	 * @param sdwanPolicyListBean
	 * @param sdwanApplications
	 * @param userDefinedAppls
	 * @param predefinedAppls
	 * @param associtedUserDefAppls
	 * @param associtedPredefinedAppls
	 * @param sdwanEndpoint
	 * @throws TclCommonException
	 */
	private void getPolicyAssociatedAppls(SdwanPolicyListBean sdwanPolicyListBean,
			VersaApplicationNames sdwanApplications, Set<String> userDefinedAppls, Set<String> predefinedAppls,
			Set<String> associtedUserDefAppls, Set<String> associtedPredefinedAppls, SdwanEndpoints sdwanEndpoint)
			throws TclCommonException {
		if(sdwanPolicyListBean != null && sdwanPolicyListBean.getPolicyName() != null && sdwanPolicyListBean.getOrganisationName() != null && 
				sdwanPolicyListBean.getDirectorRegion() != null && sdwanPolicyListBean.getAssociatedTemplate().getTemplateName()!= null && 
				sdwanPolicyListBean.getPolicyType() != null && sdwanEndpoint != null) { 
			String qosRuleViewUrl = qosPolicyNameUrl;
			String  trafficSteeringRuleUrl = trafficSteeringPolicyNameUrl;
			sdwanApplications.setAssociatedPreDefinedApplications(associtedPredefinedAppls);
			sdwanApplications.setAssociatedUserDefApplications(associtedUserDefAppls);
			if(sdwanPolicyListBean.getPolicyType().equals(ServiceInventoryConstants.TRAFFIC_STEERING)) {
				getTrafficSteeringPolicyNames(sdwanPolicyListBean, associtedUserDefAppls, associtedPredefinedAppls,
						sdwanEndpoint, trafficSteeringRuleUrl);
			} else if(sdwanPolicyListBean.getPolicyType().equals(ServiceInventoryConstants.QOS)) {
				getQosPolicyNames(sdwanPolicyListBean, associtedUserDefAppls, associtedPredefinedAppls,
						sdwanEndpoint, qosRuleViewUrl, trafficSteeringRuleUrl);							
			} 
			else if(sdwanPolicyListBean.getPolicyType().equals(ServiceInventoryConstants.FIREWALL)) {
				GetFirewallPolicy getFirewallPolicy=
						getFirewallPolicies(sdwanPolicyListBean.getAssociatedTemplate().getTemplateName(), sdwanPolicyListBean.getOrganisationName(), 
						sdwanPolicyListBean.getPolicyName(), sdwanPolicyListBean.getAccessPolicyName(), sdwanEndpoint);
				getFirewallApplicationDetails(getFirewallPolicy, associtedUserDefAppls,
						 associtedPredefinedAppls);
			}
			else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			
		}
	}
	
	
	private void getFirewallApplicationDetails(GetFirewallPolicy getFirewallPolicy, Set<String> associtedUserDefAppls, Set<String> associtedPredefinedAppls) {
		AccessPolicy accessPolicy  = getFirewallPolicy.getAccessPolicy();
		if (Objects.nonNull(accessPolicy.getMatch()) && Objects.nonNull(accessPolicy.getMatch().getApplication())) {
			if (Objects.nonNull(accessPolicy.getMatch().getApplication().getUserDefinedApplicationList()))
				associtedPredefinedAppls
						.addAll(accessPolicy.getMatch().getApplication().getPredefinedApplicationList());
			if (Objects.nonNull(accessPolicy.getMatch().getApplication().getUserDefinedApplicationList()))
				associtedUserDefAppls
						.addAll(accessPolicy.getMatch().getApplication().getUserDefinedApplicationList());
		}
		
	}
	/**
	 * Method to get QOS policy names
	 * @param sdwanPolicyListBean
	 * @param associtedUserDefAppls
	 * @param associtedPredefinedAppls
	 * @param sdwanEndpoint
	 * @param qosRuleViewUrl
	 * @param trafficSteeringRuleUrl
	 * @throws TclCommonException
	 */
	private void getQosPolicyNames(SdwanPolicyListBean sdwanPolicyListBean, Set<String> associtedUserDefAppls,
			Set<String> associtedPredefinedAppls, SdwanEndpoints sdwanEndpoint, String qosRuleViewUrl,
			String trafficSteeringRuleUrl) throws TclCommonException {
		qosRuleViewUrl = setUrlParam(sdwanPolicyListBean, sdwanEndpoint, qosRuleViewUrl);
		RestResponse qosPolicyResponse = restClientService.getWithBasicAuthentication(qosRuleViewUrl,
				new HashMap<String, String>(), sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
		if (Objects.nonNull(qosPolicyResponse) && Objects.nonNull(qosPolicyResponse.getData())) {
			AppQosPolicyRule appQosPolicyData = Utils.convertJsonToObject(qosPolicyResponse.getData(),
					AppQosPolicyRule.class);
			if (Objects.nonNull(appQosPolicyData) && Objects.nonNull(appQosPolicyData.getAppQosPolicy())) {
				AppQosPolicy qosPolicy = appQosPolicyData.getAppQosPolicy();
				if (Objects.nonNull(qosPolicy.getMatch()) && Objects.nonNull(qosPolicy.getMatch().getApplication())) {
					QosPolicyApplication applications = qosPolicy.getMatch().getApplication();
					if (Objects.nonNull(applications.getPredefinedApplicationList()))
						associtedPredefinedAppls
								.addAll(qosPolicy.getMatch().getApplication().getPredefinedApplicationList());
					if (Objects.nonNull(applications.getUserDefinedApplicationList()))
						associtedUserDefAppls
								.addAll(qosPolicy.getMatch().getApplication().getUserDefinedApplicationList());
				}
			}
		}
	}

	/**
	 * Method to get Traffic Steering policy
	 * @param sdwanPolicyListBean
	 * @param associtedUserDefAppls
	 * @param associtedPredefinedAppls
	 * @param sdwanEndpoint
	 * @param trafficSteeringRuleUrl
	 * @throws TclCommonException
	 */
	private void getTrafficSteeringPolicyNames(SdwanPolicyListBean sdwanPolicyListBean,
			Set<String> associtedUserDefAppls, Set<String> associtedPredefinedAppls, SdwanEndpoints sdwanEndpoint,
			String trafficSteeringRuleUrl) throws TclCommonException {
		trafficSteeringRuleUrl = setUrlParam(sdwanPolicyListBean, sdwanEndpoint, trafficSteeringRuleUrl);
		RestResponse sdwanPolicyResponse = restClientService.getWithBasicAuthentication(trafficSteeringRuleUrl,
				new HashMap<String, String>(), sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
		if (Objects.nonNull(sdwanPolicyResponse) && Objects.nonNull(sdwanPolicyResponse.getData())) {
			TrafficSteeringRuleData trafficSteeringPolicy = Utils.convertJsonToObject(sdwanPolicyResponse.getData(),
					TrafficSteeringRuleData.class);
			if(Objects.nonNull(trafficSteeringPolicy) &&  Objects.nonNull(trafficSteeringPolicy.getRule().getMatch()) && Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getApplication())) {
				if(Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getApplication().getPredefinedApplicationList()))
					associtedPredefinedAppls.addAll(trafficSteeringPolicy.getRule().getMatch().getApplication().getPredefinedApplicationList());
				if(Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getApplication().getUserDefinedApplicationList()))
					associtedUserDefAppls.addAll(trafficSteeringPolicy.getRule().getMatch().getApplication().getUserDefinedApplicationList());
				}
			
		}
	}

	/**
	 * Method to get 
	 * @param predefinedAppls
	 * @param instanceByCode
	 * @throws TclCommonException
	 */
	private void getVersaPredefinedApplNames(Set<String> predefinedAppls,
			Map<String, List<SdwanEndpoints>> instanceByCode) throws TclCommonException {
		List<SdwanEndpoints> sdwanEndPoint = new ArrayList<>();
		instanceByCode.entrySet().forEach(map->sdwanEndPoint.addAll(map.getValue()));
		RestResponse preDefinedApps = getVersaBuiltInAppls(sdwanEndPoint.get(0));
		LOGGER.info("VERSA PreDefined apps Response {} ",preDefinedApps.getData());
		if(preDefinedApps.getStatus() == Status.SUCCESS && preDefinedApps.getData() != null) {
			VersaApplicationsResponse preDefinedApplications = Utils.convertJsonToObject(preDefinedApps.getData(), VersaApplicationsResponse.class);
			preDefinedApplications.getApplications().stream().forEach(apps->{
				predefinedAppls.add(apps.getName());
			});
		}
	}

	/**
	 * Method to set URL params
	 * 
	 * @param sdwanPolicyListBean
	 * @param sdwanEndpoint
	 * @param trafficSteeringRuleUrl
	 * @return
	 */
	private String setUrlParam(SdwanPolicyListBean sdwanPolicyListBean, SdwanEndpoints sdwanEndpoint,
			String trafficSteeringRuleUrl) {
		trafficSteeringRuleUrl = trafficSteeringRuleUrl.replace("DYNAMICTEMPLATENAME",
				sdwanPolicyListBean.getAssociatedTemplate().getTemplateName());
		trafficSteeringRuleUrl = trafficSteeringRuleUrl.replace("DYNAMICORGNAME",
				sdwanPolicyListBean.getOrganisationName());
		trafficSteeringRuleUrl = trafficSteeringRuleUrl.replace("DYNAMICRULENAME", sdwanPolicyListBean.getPolicyName());
		trafficSteeringRuleUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
				+ trafficSteeringRuleUrl;
		return trafficSteeringRuleUrl;
	}
	
	/**
	 * Method to update user def appls in VERSA
	 *
	 * @param appRequest
	 * @param action
	 * @param customerId
	 * @param response
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @return
	 * @throws TclCommonException
	 */
	private String updateVersaUSerDefAppls(VersaUserDefAppRequest appRequest, String action, Integer customerId,
			String response, Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions) throws TclCommonException {
		LOGGER.info("Inside updateVersaUSerDefAppls  to update appl {} for customerID {} ", appRequest.getUserDefinedApplications().getName(), customerId);
		orgTemplates.entrySet().forEach(orgTemplate -> {
			orgDirectoryRegions.get(orgTemplate.getKey()).forEach(region -> {
				instanceByCode.get(region).forEach(instance -> {
					try {
						String versaUserDefApplEdit = editUserApplCommonTempUrl;
						versaUserDefApplEdit = versaUserDefApplEdit.replace("DYNAMICORGNAME", orgTemplate.getKey());
						versaUserDefApplEdit = versaUserDefApplEdit.replace("DYNAMICAPPNAME",
								appRequest.getUserDefinedApplications().getName());
						Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
						SdwanEndpoints sdwanEndpoints = instance;
						versaUserDefApplEdit = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort()
								+ versaUserDefApplEdit;
						LOGGER.info("Edit or clone User defined appl in VERSA URL {} ", versaUserDefApplEdit);
						Timestamp requestTime = new Timestamp(new Date().getTime());
						RestResponse userDefinedApps = restClientService.putWithBasicAuthentication(
								versaUserDefApplEdit, Utils.convertObjectToJson(appRequest),
								new HashMap<String, String>(), sdwanEndpoints.getServerUsername(),
								sdwanEndpoints.getServerPassword());
						Timestamp responseTime = new Timestamp(new Date().getTime());
						LOGGER.info("userDefineResponse {} ", userDefinedApps.getData());
						if (userDefinedApps.getStatus() == Status.SUCCESS) {
							saveAuditInfoSdwan(customerId, null, null, null, sdwanEndpoints.getServerCode(),
									versaUserDefApplEdit, "PUT", Utils.convertObjectToJson(appRequest),
									HttpStatus.NO_CONTENT.value(), userDefinedApps.getData(), requestTime, responseTime,
									action.toUpperCase() + CommonConstants.HYPHEN
											+ ServiceInventoryConstants.APPLICATION,
									null, orgTemplate.getKey(),
									orgTemplate.getKey() + ServiceInventoryConstants.COMMON_TEMPLATE,
									ServiceInventoryConstants.APPLICATION,
									appRequest.getUserDefinedApplications().getName(), null, null);
							LOGGER.info(action + "application audit information has been saved");
						} else {
							saveAuditInfoSdwan(customerId, null, null, null, sdwanEndpoints.getServerCode(),
									versaUserDefApplEdit, "PUT", Utils.convertObjectToJson(appRequest),
									HttpStatus.INTERNAL_SERVER_ERROR.value(), userDefinedApps.getErrorMessage(),
									requestTime, responseTime,
									action.toUpperCase() + CommonConstants.HYPHEN
											+ ServiceInventoryConstants.APPLICATION,
									null, orgTemplate.getKey(),
									orgTemplate.getKey() + ServiceInventoryConstants.COMMON_TEMPLATE,
									ServiceInventoryConstants.APPLICATION,
									appRequest.getUserDefinedApplications().getName(), null, null);
							LOGGER.info(action + "application audit information has been saved with error {}",
									userDefinedApps.getErrorMessage());
							throw new TclCommonRuntimeException(ExceptionConstants.VERSA_USER_APPL_ERROR,
									ResponseResource.R_CODE_ERROR);
						}
					} catch (Exception e) {
						LOGGER.info("Error occurred while saving application {}", e.getMessage());
					}
				});
			});
		});
		return response;
	}	

	/**
	 * To save audit information after each post operation SDWAN
	 *
	 * @param customerId
	 * @param customerLeId
	 * @param customerLeIds
	 * @param serviceId
	 * @param instanceRegion
	 * @param url
	 * @param requestMethod
	 * @param requestPayload
	 * @param responseCode
	 * @param response
	 * @param requestTime
	 * @param responseTime
	 * @param operation
	 * @param displayText
	 * @param organizationName
	 * @param templateName
	 * @param compName
	 * @param compVAlue
	 * @param taskId
	 * @param user
	 * @return
	 */
	protected SdwanInventoryAudit saveAuditInfoSdwan(Integer customerId, Integer customerLeId,
			Set<Integer> customerLeIds, String serviceId, String instanceRegion, String url, String requestMethod,
			String requestPayload, Integer responseCode, String response, Timestamp requestTime, Timestamp responseTime,
			String operation, String displayText, String organizationName, String templateName, String compName,
			String compVAlue, Integer taskId, String user) {
		SdwanInventoryAudit sdwanInventoryAudit = new SdwanInventoryAudit();
		sdwanInventoryAudit.setUuid(Utils.generateUid());
		sdwanInventoryAudit.setMdcToken(MDC.get(MDC_TOKEN_KEY));
		if (Objects.isNull(user))
			sdwanInventoryAudit.setUserId(userInfoUtils.getUserInformation().getUserId());
		else
			sdwanInventoryAudit.setUserId(user);
		sdwanInventoryAudit.setCustomerId(customerId);
		if (customerLeId == null && (Objects.isNull(customerLeIds) || customerLeIds.isEmpty()))
			sdwanInventoryAudit.setCustomerLeId(getCustomerLeIds().toString());
		else {
			if (customerLeId != null)
				sdwanInventoryAudit.setCustomerLeId(customerLeId.toString());
			else
				sdwanInventoryAudit.setCustomerLeId(customerLeIds.toString());
		}
		sdwanInventoryAudit.setServiceId(serviceId);
		sdwanInventoryAudit.setInstanceRegion(instanceRegion);
		sdwanInventoryAudit.setUrl(url);
		sdwanInventoryAudit.setRequestMethod(requestMethod);
		sdwanInventoryAudit.setRequestPayload(requestPayload);
		sdwanInventoryAudit.setResponseCode(responseCode);
		sdwanInventoryAudit.setResponse(response);
		sdwanInventoryAudit.setRequestTime(requestTime);
		sdwanInventoryAudit.setResponseTime(responseTime);
		sdwanInventoryAudit.setOperation(operation);
		sdwanInventoryAudit.setDisplayText(displayText);
		sdwanInventoryAudit.setOrganizationName(organizationName);
		sdwanInventoryAudit.setUpdatedTime(new Timestamp(new Date().getTime()));
		sdwanInventoryAudit.setTemplateName(templateName);
		sdwanInventoryAudit.setComponentName(compName);
		sdwanInventoryAudit.setComponentValue(compVAlue);
		sdwanInventoryAudit.setTaskId(taskId);
		return sdwanInventoryAuditRepository.save(sdwanInventoryAudit);
	}
	
	private Set<Integer> getCustomerLeIds() {
		Set<Integer> customerLeIds = new HashSet<>();
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
		for (CustomerDetail customerDetail : customerDetails) {
			customerLeIds.add(customerDetail.getCustomerLeId());
		}
		return customerLeIds;
	}
	
	private Set<Integer> getPartnerLeIds() {
		Set<Integer> partnerLeIds = new HashSet<>();
		List<PartnerDetail> partnerDetails = userInfoUtils.getPartnerDetails();
		for (PartnerDetail partnerDetail : partnerDetails) {
			partnerLeIds.add(partnerDetail.getPartnerLeId());
		}
		return partnerLeIds;
	}
	
	/**
	 * Fetch Sdwan Address/Groups/Zones/Urls from Versa
	 *
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param productId
	 * @param sdwanPolicyListBean
	 * @return
	 * @throws TclCommonException
	 */
	public VersaAddressListBean sdwanAddressFromVersa(Integer customerId, Integer partnerId, Integer customerLeId,
			Integer productId, SdwanPolicyListBean sdwanPolicyListBean) throws TclCommonException {
		VersaAddressListBean addressListBean = new VersaAddressListBean();
		try {
			List<Integer> customerLeIds = new ArrayList<>();
			List<Integer> partnerLeIds = new ArrayList<>();
			getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId
					+ "partnerLeIds " + partnerLeIds);
			List<Address> addresses = new ArrayList<>();
			List<Group> addressGroups = new ArrayList<>();
			List<com.tcl.dias.serviceinventory.izosdwan.beans.versa_zone_list.Zone> zones = new ArrayList<>();
			List<String> predefinedUrls = new ArrayList<>();
			List<com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url.UrlCategory> userdefinedUrls = new ArrayList<>();
			addressListBean.setAddresses(addresses);
			addressListBean.setAddressGroups(addressGroups);
			addressListBean.setZones(zones);
			addressListBean.setPredefinedUrls(predefinedUrls);
			addressListBean.setUserdefinedUrls(userdefinedUrls);
			SdwanEndpoints sdwanEndpoint = new SdwanEndpoints();
			List<SdwanEndpoints> sdwanEndPointsForRegions = new ArrayList<>();
			Set<String> instanceRegions = new HashSet<>();
			if (sdwanPolicyListBean.getDirectorRegion() != null)
				instanceRegions.add(sdwanPolicyListBean.getDirectorRegion());
			sdwanEndPointsForRegions = sdwanEndpointsRepository.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode)); // Instance mapping value should be same in sdwan_endpoints table
			sdwanEndpoint = instanceByCode.get(sdwanPolicyListBean.getDirectorRegion()).get(0);
			Map<String, Set<String>> orgTemplates = new HashMap<>();
			orgTemplates.put(sdwanPolicyListBean.getOrganisationName(),
					Collections.singleton(sdwanPolicyListBean.getAssociatedTemplate().getTemplateName()));
			Map<String, Set<String>> orgDirectoryRegions = new HashMap<>();
			orgDirectoryRegions.put(sdwanPolicyListBean.getOrganisationName(),
					Collections.singleton(sdwanPolicyListBean.getDirectorRegion()));
			persistVersaAddress(addresses, instanceByCode, orgTemplates, orgDirectoryRegions);
			persistVersaAddressGroups(addressGroups, instanceByCode, orgTemplates, orgDirectoryRegions);
			persistVersaZones(zones, instanceByCode, orgTemplates, orgDirectoryRegions);
			persistVersaUserDefinedUrls(userdefinedUrls, instanceByCode, orgTemplates, orgDirectoryRegions);
			if (!instanceByCode.isEmpty()) {
				getVersaPredefinedUrl(predefinedUrls, instanceByCode);
			}
			if(sdwanPolicyListBean.getPolicyType().equalsIgnoreCase(ServiceInventoryConstants.FIREWALL)) {
				FirewallPreServices preServBean=getVersaPredefinedServices(sdwanEndpoint);
				CustomService customService=getCustomServices(sdwanPolicyListBean.getAssociatedTemplate().getTemplateName(), sdwanPolicyListBean.getOrganisationName(), sdwanEndpoint);
				if(Objects.nonNull(preServBean) && Objects.nonNull(preServBean.getPredefinedServices()) && Objects.nonNull(preServBean.getPredefinedServices().getService()) )
					addressListBean.setPredefinedServices(preServBean.getPredefinedServices().getService());
				if(Objects.nonNull(customService) && Objects.nonNull(customService.getService()))
					addressListBean.setUserdefinedServices(customService.getService());
			}
			getPolicyAssociated(sdwanPolicyListBean, addressListBean, sdwanEndpoint);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return addressListBean;
	}
	
	/**
	 * Fetch address groups list from versa
	 *
	 * @param addressGroups
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @throws TclCommonException
	 */
	private void persistVersaAddressGroups(List<Group> addressGroups, Map<String, List<SdwanEndpoints>> instanceByCode,
			Map<String, Set<String>> orgTemplates, Map<String, Set<String>> orgDirectoryRegions)
			throws TclCommonException {
		try {
			orgTemplates.entrySet().forEach(orgTemplate -> {
				orgTemplate.getValue().stream().forEach(template -> {
					String addressGroupUrl = versaAddressGroupUrl;
					addressGroupUrl = addressGroupUrl.replace("DYNAMICTEMPLATENAME", template);
					addressGroupUrl = addressGroupUrl.replace("DYNAMICORGNAME", orgTemplate.getKey());
					Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
					SdwanEndpoints sdwanEndpoint = instanceByCode.get(serverCode.stream().findFirst().get()).get(0);
					addressGroupUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
							+ addressGroupUrl;
					try {
						RestResponse addressResponse = restClientService.getWithBasicAuthentication(addressGroupUrl,
								new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
								sdwanEndpoint.getServerPassword());
						LOGGER.info("Address Groups Response {} ", addressResponse.getData());
						if (addressResponse.getStatus() == Status.SUCCESS && addressResponse.getData() != null) {
							AddressGroupView addressGroupView = Utils.convertJsonToObject(addressResponse.getData(),
									AddressGroupView.class);
							if (Objects.nonNull(addressGroupView.getAddressGroups()))
								addressGroups.addAll(addressGroupView.getAddressGroups().getGroup());
						}

					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Fetch address list from versa
	 *
	 * @param addresses
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @throws TclCommonException
	 */
	private void persistVersaAddress(List<Address> addresses, Map<String, List<SdwanEndpoints>> instanceByCode,
			Map<String, Set<String>> orgTemplates, Map<String, Set<String>> orgDirectoryRegions)
			throws TclCommonException {
		try {
			orgTemplates.entrySet().forEach(orgTemplate -> {
				orgTemplate.getValue().stream().forEach(template -> {
					String addressUrl = versaAddressUrl;
					addressUrl = addressUrl.replace("DYNAMICTEMPLATENAME", template);
					addressUrl = addressUrl.replace("DYNAMICORGNAME", orgTemplate.getKey());
					Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
					SdwanEndpoints sdwanEndpoint = instanceByCode.get(serverCode.stream().findFirst().get()).get(0);
					addressUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort() + addressUrl;
					try {
						RestResponse addressResponse = restClientService.getWithBasicAuthentication(addressUrl,
								new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
								sdwanEndpoint.getServerPassword());
						LOGGER.info("Address list Response {} ", addressResponse.getData());
						if (addressResponse.getStatus() == Status.SUCCESS && addressResponse.getData() != null) {
							AddressListView addressListView = Utils.convertJsonToObject(addressResponse.getData(),
									AddressListView.class);
							if (Objects.nonNull(addressListView.getAddresses())
									&& Objects.nonNull(addressListView.getAddresses().getAddress()))
								addresses.addAll(addressListView.getAddresses().getAddress());
						}

					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Fetch zones list from versa
	 *
	 * @param zones
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @throws TclCommonException
	 */
	private void persistVersaZones(List<com.tcl.dias.serviceinventory.izosdwan.beans.versa_zone_list.Zone> zones,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions) throws TclCommonException {
		try {
			orgTemplates.entrySet().forEach(orgTemplate -> {
				orgTemplate.getValue().stream().forEach(template -> {
					String zonesUrl = versaZonesUrl;
					zonesUrl = zonesUrl.replace("DYNAMICTEMPLATENAME", template);
					zonesUrl = zonesUrl.replace("DYNAMICORGNAME", orgTemplate.getKey());
					Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
					SdwanEndpoints sdwanEndpoint = instanceByCode.get(serverCode.stream().findFirst().get()).get(0);
					zonesUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort() + zonesUrl;
					try {
						RestResponse zonesResponse = restClientService.getWithBasicAuthentication(zonesUrl,
								new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
								sdwanEndpoint.getServerPassword());
						LOGGER.info("Zones list Response {} ", zonesResponse.getData());
						if (zonesResponse.getStatus() == Status.SUCCESS && zonesResponse.getData() != null) {
							ZonesListView zone = Utils.convertJsonToObject(zonesResponse.getData(),
									ZonesListView.class);
							if (Objects.nonNull(zone.getZones()) && Objects.nonNull(zone.getZones().getZone()))
								zones.addAll(zone.getZones().getZone());
						}

					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Fetch User defined URLs list from versa
	 *
	 * @param userdefinedUrls
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @throws TclCommonException
	 */
	private void persistVersaUserDefinedUrls(
			List<com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url.UrlCategory> userdefinedUrls,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions) throws TclCommonException {
		try {
			orgTemplates.entrySet().forEach(orgTemplate -> {
				orgTemplate.getValue().stream().forEach(template -> {
					String userDefinedUrlUrls = versaUserUrls;
					userDefinedUrlUrls = userDefinedUrlUrls.replace("DYNAMICTEMPLATENAME", template);
					userDefinedUrlUrls = userDefinedUrlUrls.replace("DYNAMICORGNAME", orgTemplate.getKey());
					Set<String> serverCode = orgDirectoryRegions.get(orgTemplate.getKey());
					SdwanEndpoints sdwanEndpoint = instanceByCode.get(serverCode.stream().findFirst().get()).get(0);
					userDefinedUrlUrls = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
							+ userDefinedUrlUrls;
					try {
						RestResponse response = restClientService.getWithBasicAuthentication(userDefinedUrlUrls,
								new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
								sdwanEndpoint.getServerPassword());
						LOGGER.info("User defined urls list Response {} ", response.getData());
						if (response.getStatus() == Status.SUCCESS && response.getData() != null) {
							UserdefinedUrl userdefinedUrlResponse = Utils.convertJsonToObject(response.getData(),
									UserdefinedUrl.class);
							if (Objects.nonNull(userdefinedUrlResponse.getUrlCategory()))
								userdefinedUrls.addAll(userdefinedUrlResponse.getUrlCategory());
						}

					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_ERROR);
					}
				});
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Fetch predefined url list from versa
	 *
	 * @param predefinedUrls
	 * @param instanceByCode
	 * @throws TclCommonException
	 * @throws IOException
	 */
	private void getVersaPredefinedUrl(List<String> predefinedUrls, Map<String, List<SdwanEndpoints>> instanceByCode)
			throws TclCommonException, IOException {
		List<SdwanEndpoints> sdwanEndPoint = new ArrayList<>();
		instanceByCode.entrySet().forEach(map -> sdwanEndPoint.addAll(map.getValue()));
		RestResponse response = getVersaBuiltInUrls(sdwanEndPoint);
		LOGGER.info("VERSA PreDefined URLs Response {} ", response.getData());
		if (response.getStatus() == Status.SUCCESS && response.getData() != null) {
			ObjectMapper mapper = new ObjectMapper();
			List<PredefinedUrl> predefinedUrlsResponse = mapper.readValue(response.getData(),
					new TypeReference<List<PredefinedUrl>>() {
					});
			if (Objects.nonNull(predefinedUrlsResponse)) {
				predefinedUrlsResponse.forEach(predefinedUrl -> predefinedUrls.add(predefinedUrl.getCategoryName()));
			}
		}
	}
	
	/**
	 * Get address,group,zones,urls associated to a policy
	 *
	 * @param sdwanPolicyListBean
	 * @param addressListBean
	 * @param sdwanEndpoint
	 * @throws TclCommonException
	 */
	private void getPolicyAssociated(SdwanPolicyListBean sdwanPolicyListBean, VersaAddressListBean addressListBean,
			SdwanEndpoints sdwanEndpoint) throws TclCommonException {
		if (sdwanPolicyListBean != null && sdwanPolicyListBean.getPolicyName() != null
				&& sdwanPolicyListBean.getOrganisationName() != null && sdwanPolicyListBean.getDirectorRegion() != null
				&& sdwanPolicyListBean.getAssociatedTemplate().getTemplateName() != null
				&& sdwanPolicyListBean.getPolicyType() != null && sdwanEndpoint != null) {
			String qosRuleViewUrl = qosPolicyNameUrl;
			String trafficSteeringRuleUrl = trafficSteeringPolicyNameUrl;
			String firewallRuleUrl = versaApiFirewallForPolicy;
			addressListBean.setAssociatedPredefinedUrls(new HashSet<>());
			addressListBean.setAssociatedUserDefinedUrls(new HashSet<>());
			addressListBean.setAsscSrcZones(new HashSet<>());
			addressListBean.setAsscSrcAddresses(new HashSet<>());
			addressListBean.setAsscSrcAddressGroups(new HashSet<>());
			addressListBean.setAsscDestZones(new HashSet<>());
			addressListBean.setAsscDestAddresses(new HashSet<>());
			addressListBean.setAsscDestAddressGroups(new HashSet<>());
			addressListBean.setAsscPredefinedServices(new HashSet<>());
			addressListBean.setAsscUserdefinedServices(new HashSet<>());
			if (sdwanPolicyListBean.getPolicyType().equals(ServiceInventoryConstants.TRAFFIC_STEERING)) {
				getListFromTrafficSteeringPolicy(sdwanPolicyListBean, sdwanEndpoint, trafficSteeringRuleUrl,
						addressListBean.getAssociatedUserDefinedUrls(), addressListBean.getAssociatedPredefinedUrls(),
						addressListBean.getAsscSrcAddresses(), addressListBean.getAsscSrcAddressGroups(),
						addressListBean.getAsscSrcZones(), addressListBean.getAsscDestAddresses(),
						addressListBean.getAsscDestAddressGroups(), addressListBean.getAsscDestZones());
			} else if (sdwanPolicyListBean.getPolicyType().equals(ServiceInventoryConstants.QOS)) {
				getListFromQosPolicy(sdwanPolicyListBean, sdwanEndpoint, qosRuleViewUrl,
						addressListBean.getAssociatedUserDefinedUrls(), addressListBean.getAssociatedPredefinedUrls(),
						addressListBean.getAsscSrcAddresses(), addressListBean.getAsscSrcAddressGroups(),
						addressListBean.getAsscSrcZones(), addressListBean.getAsscDestAddresses(),
						addressListBean.getAsscDestAddressGroups(), addressListBean.getAsscDestZones());
			}else if (sdwanPolicyListBean.getPolicyType().equals(ServiceInventoryConstants.FIREWALL)) {
				getListFromFirewallPolicy(sdwanPolicyListBean, sdwanEndpoint, firewallRuleUrl,
						addressListBean.getAssociatedUserDefinedUrls(), addressListBean.getAssociatedPredefinedUrls(),
						addressListBean.getAsscSrcAddresses(), addressListBean.getAsscSrcAddressGroups(),
						addressListBean.getAsscSrcZones(), addressListBean.getAsscDestAddresses(),
						addressListBean.getAsscDestAddressGroups(), addressListBean.getAsscDestZones(),addressListBean.getAsscPredefinedServices(),addressListBean.getAsscUserdefinedServices());
			}else {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

		}
	}
	
	/**
	 * Get Versa built-in URLs
	 *
	 * @param sdwanEndPointsForRegions
	 * @return
	 * @throws TclCommonException
	 */
	private RestResponse getVersaBuiltInUrls(List<SdwanEndpoints> sdwanEndPointsForRegions) throws TclCommonException {
		String preDefinedUrlListUrl = versaPredefinedUrlsUrl;
		preDefinedUrlListUrl = sdwanEndPointsForRegions.get(0).getServerIp() + ":"
				+ sdwanEndPointsForRegions.get(0).getServerPort() + preDefinedUrlListUrl;
		RestResponse preDefinedUrls = restClientService.getWithBasicAuthentication(preDefinedUrlListUrl,
				new HashMap<String, String>(), sdwanEndPointsForRegions.get(0).getServerUsername(),
				sdwanEndPointsForRegions.get(0).getServerPassword());
		LOGGER.info("VERSA PreDefined URLs Response {} ", preDefinedUrls.getData());
		return preDefinedUrls;
	}
	
	/**
	 * Method to get Traffic Steering policy according to name
	 *
	 * @param sdwanPolicyListBean
	 * @param associatedUserDefUrls
	 * @param associatedPredefinedUrls
	 * @param sdwanEndpoint
	 * @param trafficSteeringRuleUrl
	 * @throws TclCommonException
	 */
	private void getListFromTrafficSteeringPolicy(SdwanPolicyListBean sdwanPolicyListBean, SdwanEndpoints sdwanEndpoint,
			String trafficSteeringRuleUrl, Set<String> associatedUserDefUrls, Set<String> associatedPredefinedUrls,
			Set<String> asscSrcAddress, Set<String> asscSrcAddressGroups, Set<String> asscSrcZones,
			Set<String> asscDestAddress, Set<String> asscDestAddressGroups, Set<String> asscDestZones)
			throws TclCommonException {
		trafficSteeringRuleUrl = setUrlParam(sdwanPolicyListBean, sdwanEndpoint, trafficSteeringRuleUrl);
		RestResponse sdwanPolicyResponse = restClientService.getWithBasicAuthentication(trafficSteeringRuleUrl,
				new HashMap<String, String>(), sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
		if (Objects.nonNull(sdwanPolicyResponse) && Objects.nonNull(sdwanPolicyResponse.getData())) {
			TrafficSteeringRuleData trafficSteeringPolicy = Utils.convertJsonToObject(sdwanPolicyResponse.getData(),
					TrafficSteeringRuleData.class);
			if (Objects.nonNull(trafficSteeringPolicy) && Objects.nonNull(trafficSteeringPolicy.getRule().getMatch())) {
				if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getUrlCategory())) {
					if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getUrlCategory().getPredefined()))
						associatedPredefinedUrls
								.addAll(trafficSteeringPolicy.getRule().getMatch().getUrlCategory().getPredefined());
					if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getUrlCategory().getUserDefined()))
						associatedUserDefUrls
								.addAll(trafficSteeringPolicy.getRule().getMatch().getUrlCategory().getUserDefined());
				}
				// fetch associated source address
				if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getSource())) {
					if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getSource().getAddress())) {
						Optional.ofNullable(
								trafficSteeringPolicy.getRule().getMatch().getSource().getAddress().getAddressList())
								.ifPresent(asscSrcAddress::addAll);
						Optional.ofNullable(trafficSteeringPolicy.getRule().getMatch().getSource().getAddress()
								.getAddressGroupList()).ifPresent(asscSrcAddressGroups::addAll);
					}
					if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getSource().getZone()) && Objects
							.nonNull(trafficSteeringPolicy.getRule().getMatch().getSource().getZone().getZoneList()))
						asscSrcZones
								.addAll(trafficSteeringPolicy.getRule().getMatch().getSource().getZone().getZoneList());
				}
				// fetch associated destination address
				if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getDestination())) {
					if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getDestination().getAddress())) {
						Optional.ofNullable(trafficSteeringPolicy.getRule().getMatch().getDestination().getAddress()
								.getAddressList()).ifPresent(asscDestAddress::addAll);
						Optional.ofNullable(trafficSteeringPolicy.getRule().getMatch().getDestination().getAddress()
								.getAddressGroupList()).ifPresent(asscDestAddressGroups::addAll);
					}
					if (Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getDestination().getZone())
							&& Objects.nonNull(trafficSteeringPolicy.getRule().getMatch().getDestination().getZone()
									.getZoneList()))
						asscDestZones.addAll(
								trafficSteeringPolicy.getRule().getMatch().getDestination().getZone().getZoneList());
				}
			}
		}
	}
	
	/**
	 * Get Qos policy by given name
	 *
	 * @param sdwanPolicyListBean
	 * @param sdwanEndpoint
	 * @param qosRuleViewUrl
	 * @param associatedUserDefinedUrls
	 * @param associatedPredefinedUrls
	 * @param asscSrcAddresses
	 * @param asscSrcAddressGroups
	 * @param asscSrcZones
	 * @param asscDestAddresses
	 * @param asscDestAddressGroups
	 * @param asscDestZones
	 * @throws TclCommonException
	 */
	private void getListFromQosPolicy(SdwanPolicyListBean sdwanPolicyListBean, SdwanEndpoints sdwanEndpoint,
			String qosRuleViewUrl, Set<String> associatedUserDefinedUrls, Set<String> associatedPredefinedUrls,
			Set<String> asscSrcAddresses, Set<String> asscSrcAddressGroups, Set<String> asscSrcZones,
			Set<String> asscDestAddresses, Set<String> asscDestAddressGroups, Set<String> asscDestZones)
			throws TclCommonException {
		qosRuleViewUrl = setUrlParam(sdwanPolicyListBean, sdwanEndpoint, qosRuleViewUrl);
		RestResponse qosPolicyResponse = restClientService.getWithBasicAuthentication(qosRuleViewUrl,
				new HashMap<String, String>(), sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
		if (Objects.nonNull(qosPolicyResponse) && Objects.nonNull(qosPolicyResponse.getData())) {
			AppQosRule appQosPolicy = Utils.convertJsonToObject(qosPolicyResponse.getData(), AppQosRule.class);
			if (Objects.nonNull(appQosPolicy) && Objects.nonNull(appQosPolicy.getAppQosPolicy())) {
				AppQosPolicy qosPolicy = appQosPolicy.getAppQosPolicy();
				if (Objects.nonNull(qosPolicy.getMatch())) {
					if (Objects.nonNull(qosPolicy.getMatch().getUrlCategory())) {
						if (Objects.nonNull(qosPolicy.getMatch().getUrlCategory().getPredefined()))
							associatedPredefinedUrls.addAll(qosPolicy.getMatch().getUrlCategory().getPredefined());
						if (Objects.nonNull(qosPolicy.getMatch().getUrlCategory().getUserDefined()))
							associatedUserDefinedUrls.addAll(qosPolicy.getMatch().getUrlCategory().getUserDefined());
					}
					// fetch associated source address
					if (Objects.nonNull(qosPolicy.getMatch().getSource())) {
						if (Objects.nonNull(qosPolicy.getMatch().getSource().getAddress())) {
							Optional.ofNullable(qosPolicy.getMatch().getSource().getAddress().getAddressList())
									.ifPresent(asscSrcAddresses::addAll);
							Optional.ofNullable(qosPolicy.getMatch().getSource().getAddress().getAddressGroupList())
									.ifPresent(asscSrcAddressGroups::addAll);
						}
						if (Objects.nonNull(qosPolicy.getMatch().getSource().getZone())
								&& Objects.nonNull(qosPolicy.getMatch().getSource().getZone().getZoneList()))
							asscSrcZones.addAll(qosPolicy.getMatch().getSource().getZone().getZoneList());
					}
					// fetch associated destination address
					if (Objects.nonNull(qosPolicy.getMatch().getDestination())) {
						if (Objects.nonNull(qosPolicy.getMatch().getDestination().getAddress())) {
							Optional.ofNullable(qosPolicy.getMatch().getDestination().getAddress().getAddressList())
									.ifPresent(asscDestAddresses::addAll);
							Optional.ofNullable(
									qosPolicy.getMatch().getDestination().getAddress().getAddressGroupList())
									.ifPresent(asscDestAddressGroups::addAll);
						}
						if (Objects.nonNull(qosPolicy.getMatch().getDestination().getZone())
								&& Objects.nonNull(qosPolicy.getMatch().getDestination().getZone().getZoneList()))
							asscDestZones.addAll(qosPolicy.getMatch().getDestination().getZone().getZoneList());
					}
				}
			}
		}
	}

	/**
	 * To fetch CPE status/availability/sync status for a given template
	 *
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public List<CpeSyncStatusBean> getCPESyncStatus(Integer productId, Integer customerId, Integer partnerId,
			Integer customerLeId, SdwanPolicyListBean request) throws IOException, TclCommonException {
		List<Integer> customerLeIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);

		List<Map<String, Object>> underlaysAndOverlaysJoined = vwSiServiceInfoAllRepository.findByTemplateNames(
				Collections.singletonList(request.getAssociatedTemplate().getTemplateName()), customerId, customerLeIds,
				partnerId, partnerLeIds);
		Set<String> underlayServiceIds = underlaysAndOverlaysJoined.stream()
				.map(underlays -> (String) underlays.get("underlaySrvcId")).collect(Collectors.toSet());
		List<SIServiceAssetInfo> cpesAssociatedWithTemplate = serviceAssetInfoRepository
				.findByServiceIdInAndAssetTag(new ArrayList<>(underlayServiceIds), ServiceInventoryConstants.SDWAN_CPE);
		List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
				.findByServerCodeIn(Collections.singleton(request.getDirectorRegion()));
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
		Map<String, Set<String>> orgTemplates = new HashMap<>();
		orgTemplates.put(request.getOrganisationName(),
				Collections.singleton(request.getAssociatedTemplate().getTemplateName()));
		Map<String, Set<String>> orgDirectoryRegions = new HashMap<>();
		orgDirectoryRegions.put(request.getOrganisationName(), Collections.singleton(request.getDirectorRegion()));
		Map<String, Set<String>> cpeGroupedByTemplate = getCpeByTemplateFromVersa(instanceByCode, orgTemplates,
				orgDirectoryRegions);
		Map<String, Set<DeviceGroupDatum>> statusGroupedByTemplate = checkTemplateSyncStatusByCpe(cpeGroupedByTemplate,
				instanceByCode, orgTemplates, orgDirectoryRegions);
		LOGGER.info("CPE Status Grouped By Template {}", Utils.convertObjectToJson(statusGroupedByTemplate));
		VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
		List<CpeSyncStatusBean> cpeSyncStatus = new ArrayList<>();
		Set<String> distinctCpeNames = cpesAssociatedWithTemplate.stream().map(SIServiceAssetInfo::getAssetName)
				.collect(Collectors.toSet());
		distinctCpeNames.forEach(cpeAssociated -> {
			CpeSyncStatusBean cpeSyncStatusBean = new CpeSyncStatusBean();
			cpeSyncStatusBean.setCpeName(cpeAssociated);
			Map<String, String> cpeDetails = new HashMap<>();
			getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, cpeSyncStatusBean.getCpeName(), cpeDetails, null);
			cpeSyncStatusBean.setCpeStatus(cpeDetails.get(ServiceInventoryConstants.CPE_STATUS));
			cpeSyncStatusBean.setCpeAvailability(cpeDetails.get(ServiceInventoryConstants.CPE_AVAILABILITY));
			cpeSyncStatus.add(cpeSyncStatusBean);
		});
		cpeSyncStatus.forEach(cpeAssociated -> {
			statusGroupedByTemplate.get(request.getAssociatedTemplate().getTemplateName()).stream()
					.filter(Objects::nonNull).filter(cpeWithStatus -> Objects.nonNull(cpeWithStatus.getDeviceData()))
					.forEach(cpeWithStatus -> {
						cpeWithStatus.getDeviceData().stream().filter(Objects::nonNull).forEach(deviceData -> {
							if (cpeAssociated.getCpeName().equalsIgnoreCase(deviceData.getName())) {
								cpeAssociated.setTemplateSyncStatus(deviceData.getStatus());
							}
						});
					});
		});
		return cpeSyncStatus;
	}

	/**
	 * Cpe details fetch with filter (multithreaded)
	 *
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
	 * @throws IOException
	 * @throws ExecutionException
	 */
	public SdwanCPEInformationBean getCpeDetailsBasedOnFiltersPoc(String searchText, Integer size, Integer page,
			String sortBy, String sortOrder, Integer productId, Integer customerId, Integer customerLeId,
			Integer partnerId, String groupBy) throws TclCommonException, IOException, ExecutionException {
		List<Integer> customerIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		getCustomerAndPartnerLeIds(customerIds, partnerLeIds, customerLeId);
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
		SdwanCPEInformationBean sdwanCPEInformationBean = new SdwanCPEInformationBean();
		sdwanCPEInformationBean.setCPE(new ArrayList<>());
		if (Objects.nonNull(siAssetPaginated) && !siAssetPaginated.isEmpty()) {
			List<SIAsset> siAssets = siAssetPaginated;
			List<SdwanCPEBean> sdwanCPEBeans = new ArrayList<>();

			Set<String> sdwanServiceIds = new HashSet<>();
			sdwanServiceIds = siAssets.parallelStream().map(siAsset -> siAsset.getSiServiceDetail().getIzoSdwanSrvcId())
					.collect(Collectors.toSet());
			
			List<Integer> underlaySiDetailId = new ArrayList<>();
			underlaySiDetailId = siAssets.parallelStream().map(siAsset -> siAsset.getSiServiceDetail().getId())
					.collect(Collectors.toList());
			List<VwServiceAttributesBean> underlayTemplates = getUnderlayTemplates(underlaySiDetailId);
			
			List<Map<String, Object>> overLayServiceAttrs = serviceAdditionalInfoRepository
					.findAttributesBySdwanServiceIds(new ArrayList<>(sdwanServiceIds),
							Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
									ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING),
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
					attr -> attr.getAttributeName().equalsIgnoreCase(ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING))
					.forEach(attr -> instanceRegions.add(attr.getAttributeValue()));
			List<SdwanEndpoints> sdwanEndPointsForRegions = sdwanEndpointsRepository
					.findByServerCodeIn(instanceRegions);
			Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndPointsForRegions.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));

			VersaCpeStatusResponse versaCpeStatusResponse = getCpeStatusDetailsFromVersa(0, instanceByCode);
			if (Objects.nonNull(versaCpeStatusResponse)
					&& Objects.nonNull(versaCpeStatusResponse.getApplianceStatusResult())) {
				Map<String, String> map = new HashMap<>();
				List<CompletableFuture<String>> completableFutureList = new ArrayList<>();
				siAssets.forEach(siAsset -> {
					SdwanCPEBean sdwanCPEBean = new SdwanCPEBean();
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
					constructSdwanTemplateDetails(underlayTemplates, siAsset, sdwanCPEBean);
//					SdwanTemplateBean sdwanTemplateBean = new SdwanTemplateBean();
//					sdwanTemplateBean.setTemplateId(
//							siAsset.getSiServiceDetail().getSiServiceAttributes().stream().findAny().get().getId());
//					sdwanTemplateBean.setTemplateName(siAsset.getSiServiceDetail().getSiServiceAttributes().stream()
//							.findAny().get().getAttributeValue());
//					sdwanCPEBean.setTemplate(sdwanTemplateBean);
					sdwanCPEBean.setSdwanServiceId(siAsset.getSiServiceDetail().getIzoSdwanSrvcId());
					sdwanCPEBean.setUnderlayServiceId(siAsset.getSiServiceDetail().getTpsServiceId());
					sdwanCPEBean.setDescription(siAsset.getDescription());
					sdwanCPEBean.setModel(siAsset.getModel());
					sdwanCPEBean.setSerialNumber(siAsset.getSerialNo());
					sdwanCPEBean.setSiteAddress(siAsset.getSiServiceDetail().getSiteAddress());
					sdwanCPEBean.setControllers(new ArrayList<>());
					sdwanCPEBean.setThisUnderlays(new ArrayList<>());
					getSdwanCpeInfoFromVersaResponse(versaCpeStatusResponse, sdwanCPEBean.getCpeName(), map,
							sdwanCPEBean.getControllers());
					sdwanCPEBean.setCpeStatus(map.get(ServiceInventoryConstants.CPE_STATUS));
					sdwanCPEBean.setCpeAvailability(map.get(ServiceInventoryConstants.CPE_AVAILABILITY));
					sdwanCPEBean.setOsVersion(map.get(ServiceInventoryConstants.OS_VERSION));
					sdwanCPEBeans.add(sdwanCPEBean);
					completableFutureList.add(izoSdwanAsyncTasks.wanStatusAsync(instanceByCode, sdwanCPEBean));
				});

				for (CompletableFuture<String> completableFuture : completableFutureList) {
					completableFuture.join();
				}
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
								sdwanCPE.setUnderlaysOnlineCount(incrementCountIfOnline(
										cpeUnderlaySitesBean.getSiteStatus(), sdwanCPE.getUnderlaysOnlineCount()));
								sdwanCPE.setUnderlaysOfflineCount(incrementCountIfOffline(
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
					sdwanCPEInformationBean.setOnlineCpeCount(incrementCountIfOnline(sdwanCPE.getCpeStatus(),
							sdwanCPEInformationBean.getOnlineCpeCount()));
					sdwanCPEInformationBean.setOfflineCpeCount(incrementCountIfOffline(sdwanCPE.getCpeStatus(),
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
							.sort((SdwanCPEBean s1, SdwanCPEBean s2) -> s1.getCpeStatus().compareTo(s2.getCpeStatus()));
				else
					sdwanCPEBeans
							.sort((SdwanCPEBean s1, SdwanCPEBean s2) -> s2.getCpeStatus().compareTo(s1.getCpeStatus()));
			}
			LOGGER.info("sdwanCPEBeans in POC API  : {}", sdwanCPEBeans.toString());
			//removing duplicates from list
			Set<String> recurringCPEs = new HashSet<>();
			List<SdwanCPEBean> duplicatesRemovedCpes = sdwanCPEBeans.stream().filter(sdwanCPE -> !recurringCPEs.contains(sdwanCPE.getCpeName())).peek(sdwanCPE -> recurringCPEs.add(sdwanCPE.getCpeName())).collect(Collectors.toList());
			//truncating result list to given page-size range after removing duplicates
			if (page != -1) {
				List<SdwanCPEBean> sdwanCPEBeanPaged;
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
	 * To fetch task details from versa using task ids
	 *
	 * @param taskIds
	 * @return
	 * @throws TclCommonException
	 */
	public List<SdwanTaskDetailsBean> getSdwanTaskIdDetails(List<Integer> taskIds) throws TclCommonException {
		Objects.requireNonNull(taskIds);
		List<SdwanTaskDetailsBean> sdwanTaskDetails = new ArrayList<>();
		List<SdwanInventoryAudit> sdwanInventoryAudits = sdwanInventoryAuditRepository.findByTaskIdIn(taskIds);
		Set<String> directorRegion = sdwanInventoryAudits.stream().map(SdwanInventoryAudit::getInstanceRegion)
				.collect(Collectors.toSet());
		List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository.findByServerCodeIn(directorRegion);
		Map<String, List<SdwanEndpoints>> instanceByCode = sdwanEndpoints.stream()
				.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));

		sdwanInventoryAudits.forEach(sdwanInventoryAudit -> {
			instanceByCode.get(sdwanInventoryAudit.getInstanceRegion()).forEach(sdwanEndpoint -> {
				SdwanTaskDetailsBean sdwanTaskDetailsBean = new SdwanTaskDetailsBean();
				try {
					String taskIdUrl = versaTaskIdDetailesUrl;
					taskIdUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort() + taskIdUrl;
					taskIdUrl = taskIdUrl.replace("DYNAMICTASKID", String.valueOf(sdwanInventoryAudit.getTaskId()));
					LOGGER.info("Task ID URL and credentials : {}, {}, {}", taskIdUrl,
							sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
					RestResponse restResponse = restClientService.getWithBasicAuthentication(taskIdUrl,
							new HashMap<String, String>(), sdwanEndpoint.getServerUsername(),
							sdwanEndpoint.getServerPassword());
					LOGGER.info("Response from task details : {}", restResponse.getData());
					if (restResponse.getStatus() == Status.SUCCESS && Objects.nonNull(restResponse.getData())) {
						TaskDetailById taskDetailById;
						if(restResponse.getData().contains("FAILED".toString())) {
							taskDetailById = Utils.convertJsonToObject(restResponse.getData(),
									TaskDetailById.class);
						}else {
							String response=restResponse.getData().replace("\"versa-tasks.errormessages\":\"\"", "\"versa-tasks.errormessages\":null");
							taskDetailById = Utils.convertJsonToObject(response,
									TaskDetailById.class);
						}
						if (Objects.nonNull(taskDetailById) && Objects.nonNull(taskDetailById.getVersaTasksTask())) {
							sdwanTaskDetailsBean.setTaskId(taskDetailById.getVersaTasksTask().getVersaTasksId());
							sdwanTaskDetailsBean
									.setTaskName(taskDetailById.getVersaTasksTask().getVersaTasksTaskName());
							sdwanTaskDetailsBean.setPercentageCompletion(
									taskDetailById.getVersaTasksTask().getVersaTasksPercentageCompletion());
							sdwanTaskDetailsBean
									.setTaskStatus(taskDetailById.getVersaTasksTask().getVersaTasksTaskStatus());
							sdwanTaskDetailsBean.setTemplateName(sdwanInventoryAudit.getTemplateName());
							if (Objects.nonNull(taskDetailById.getVersaTasksTask().getVersaTasksProgressmessages())
									&& Objects.nonNull(taskDetailById.getVersaTasksTask()
											.getVersaTasksProgressmessages().getVersaTasksProgressmessage())) {
								String applyTemplateMessage = "";
//										taskDetailById.getVersaTasksTask()
//										.getVersaTasksProgressmessages().getVersaTasksProgressmessage().get(1)
//										.getVersaTasksMessage();
								List<VersaTasksProgressmessage> list= taskDetailById.getVersaTasksTask()
								.getVersaTasksProgressmessages().getVersaTasksProgressmessage();
								for(int i=0;i<list.size();i++) {
									if(list.get(i).getVersaTasksMessage().contains("Applying template to")) {
										applyTemplateMessage=list.get(i).getVersaTasksMessage();
									}
								}
								String cpes = applyTemplateMessage.substring(applyTemplateMessage.lastIndexOf('[') + 1,
										applyTemplateMessage.lastIndexOf(']'));
								String[] cpesList = cpes.replaceAll("\\s", "").split(",");
								sdwanTaskDetailsBean.setCpes(Arrays.asList(cpesList));
							}
							sdwanTaskDetailsBean.setTaskStartTime(taskDetailById.getVersaTasksTask().getVersaTasksStartTime());
							sdwanTaskDetailsBean.setTaskEndTime(taskDetailById.getVersaTasksTask().getVersaTasksEndTime());
						}
						sdwanTaskDetails.add(sdwanTaskDetailsBean);
					}
				} catch (Exception e) {
					LOGGER.info("Error occurred while fetching task by id {}", e.getMessage());
				}
			});
		});
		return sdwanTaskDetails;
	}

	/**
	 * Get bandwidth utilization information for all sites of a customer/partner
	 *
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param bwUnit
	 * @param startDate
	 * @param endDate
	 * @param httpServletRequest
	 * @param servletResponse
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanSiteUtilizationDetails getSdwanSiteUtilization(Integer productId, Integer customerId,
			Integer customerLeId, Integer partnerId, String bwUnit, String startDate, String endDate,
			HttpServletRequest httpServletRequest, HttpServletResponse servletResponse) throws TclCommonException {
		if (Objects.isNull(bwUnit))
			bwUnit = "Mbps";
		if (Objects.isNull(startDate))
			startDate = "30daysAgo";
		if (Objects.isNull(endDate))
			endDate = "today";
		List<Integer> customerLeIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
		SdwanSiteUtilizationDetails sdwanSiteUtilizationDetails = new SdwanSiteUtilizationDetails();
		List<SiteBWUtilizationBean> siteBWUtilizations = new ArrayList<>();
		LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
		if (!customerLeIds.isEmpty() || !partnerLeIds.isEmpty()) {
			List<String> sdwanServiceIds = new ArrayList<>();
			List<Integer> sdwanSysId = new ArrayList<>();
			List<Map<String, Object>> sdwanSiteDetails = new ArrayList<>();
			sdwanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetails(customerLeIds, partnerLeIds, productId,
					customerId, partnerId);
			Map<String, String> siteNameToAliasMapping = new HashMap<>();
			sdwanSiteDetails.stream().forEach(sdwanSiteDetail -> {
				sdwanServiceIds.add((String) sdwanSiteDetail.get("serviceId"));
				sdwanSysId.add((Integer) sdwanSiteDetail.get("sysId"));
				siteNameToAliasMapping.put((String) sdwanSiteDetail.get("serviceId"),
						(String) sdwanSiteDetail.get("siteAlias"));
			});
			if (!sdwanServiceIds.isEmpty() && !sdwanSysId.isEmpty()) {
				List<Map<String, Object>> underlays = vwSiServiceInfoAllRepository
						.findUnderlayServiceIdbySdwanServiceId(sdwanServiceIds);
				List<Integer> underlaySysIds = underlays.stream().map(underlay -> (Integer) underlay.get("sys_id"))
						.collect(Collectors.toList());
				Set<String> serviceIds = underlays.stream().map(underlay -> (String) underlay.get("sdwan_id"))
						.collect(Collectors.toSet());
				List<SIServiceAdditionalInfo> templates = serviceAdditionalInfoRepository
						.findBySysIdAndAttributeName(underlaySysIds, ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
				List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
						.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
								Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
										ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
				Set<String> orgNames = new HashSet<>();
				Map<String, String> siteToInstanceRegion = new HashMap<>();
				overLayServiceAttrs.stream().peek(attributes -> {
					if (ServiceInventoryConstants.SDWAN_ORGANISATION_NAME
							.equalsIgnoreCase(attributes.getAttributeName()))
						orgNames.add(attributes.getAttributeValue());
					else if (ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING
							.equalsIgnoreCase(attributes.getAttributeName()))
						siteToInstanceRegion.put(attributes.getSiServiceDetail().getTpsServiceId(),
								attributes.getAttributeValue());
				}).collect(Collectors.toSet());

				List<SdwanEndpoints> analyticsEndpoints = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
				if(analyticsEndpoints == null || analyticsEndpoints.isEmpty()) {
					throw new TclCommonRuntimeException("Analytics endpoint not available");
				}
				SdwanEndpoints versaAnalyticsEndpoint = analyticsEndpoints.get(0);
				HttpHeaders loginHeader = connectVersaAnalytics(httpServletRequest, versaAnalyticsEndpoint);
				Cookie cooki = getCookiesFromHeader(loginHeader);
				LOGGER.info("Cookie : {} ", Utils.convertObjectToJson(cooki));
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
				cooki.setPath("/");
				cooki.setSecure(false);
				sdwanSiteUtilizationDetails.setKey(cooki.getName() + "=" + cooki.getValue());
				servletResponse.addCookie(cooki);

				List<SiteBWUtilizationBean> allUtilizations = new ArrayList<>();
				String finalStartDate = startDate;
				String finalEndDate = endDate;
				String analyticsHost = versaAnalyticsEndpoint.getServerIp()+":"+versaAnalyticsEndpoint.getServerPort();
				orgNames.forEach(orgName -> {
					try {
						List<SiteBWUtilizationBean> siteBWUtilizationsSdwan = new ArrayList<>();
						List<SiteBWUtilizationBean> siteBWUtilizationsDIA = new ArrayList<>();
						// Link Utilization - SDWAN Traffic URL 
						String linkUtilizationSdwan = versaAnalyticsSdwanTrafficUrl+10000;
						linkUtilizationSdwan = linkUtilizationSdwan.replaceAll("DYNAMICORGNAME", orgName);
						// Link Utilization - DIA Traffic URL 
						String linkDIAUrl = versaAnalyticsDiaTrafficUrl;
						linkDIAUrl = linkDIAUrl.replaceAll("DYNAMICORGNAME", orgName);
						linkUtilizationSdwan = linkUtilizationSdwan.replaceAll("<Start_Date>", finalStartDate);
						linkUtilizationSdwan = linkUtilizationSdwan.replaceAll("<End_Date>", finalEndDate);
						linkUtilizationSdwan = analyticsHost+linkUtilizationSdwan;
						linkDIAUrl = linkDIAUrl.replaceAll("<Start_Date>", finalStartDate);
						linkDIAUrl = linkDIAUrl.replaceAll("<End_Date>", finalEndDate);
						linkDIAUrl = analyticsHost+linkDIAUrl;
						getLinkUtilizationFromVersa(siteBWUtilizationsSdwan, linkUtilizationSdwan, headers, underlays,
								orgName, templates);
						getLinkUtilizationFromVersa(siteBWUtilizationsDIA, linkDIAUrl, headers, underlays, orgName,
								templates);
						allUtilizations.addAll(siteBWUtilizationsSdwan);
						allUtilizations.addAll(siteBWUtilizationsDIA);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
					}
				});
				Set<String> recurringSites = new HashSet<>();
				calculateAggregateOfSites(siteBWUtilizations, recurringSites, allUtilizations, siteNameToAliasMapping);
				List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository
						.findByServerCodeIn(Sets.newHashSet(siteToInstanceRegion.values()));
				Map<String, List<SdwanEndpoints>> instanceGroupByRegion = sdwanEndpoints.stream()
						.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
				String finalBwUnit = bwUnit;
				siteBWUtilizations.forEach(siteBWUtilization -> {
					Set<String> uniqueLinks = new HashSet<>();
					siteBWUtilization.setInstanceRegion(siteToInstanceRegion.get(siteBWUtilization.getSiteName()));
					siteBWUtilization.setInterfaces(new HashSet<>());
					siteBWUtilization.getAssociatedCpes().forEach(associatedCpe -> {
						instanceGroupByRegion.get(siteToInstanceRegion.get(siteBWUtilization.getSiteName()))
								.forEach(endpoint -> {
									try {
										CpeInterfaceDetails cpeInterfaceDetails = getCpeInterfaceDetails(endpoint,
												associatedCpe, siteBWUtilization.getOrgName());
										if (Objects.nonNull(cpeInterfaceDetails)
												&& Objects.nonNull(cpeInterfaceDetails.getCollection())
												&& Objects.nonNull(
														cpeInterfaceDetails.getCollection().getSdwanWanInterfaces())) {
											cpeInterfaceDetails.getCollection().getSdwanWanInterfaces().stream()
													.filter(Objects::nonNull)
													.filter(intrface -> siteBWUtilization.getLinkNames()
															.contains(intrface.getCircuitName()))
													.forEach(linkInterface -> {
														siteBWUtilization.getInterfaces()
																.add(linkInterface.getIntfName().split("\\.")[0]);
														uniqueLinks.add(linkInterface.getIntfName().split("\\.")[0]);
													});
										}
										ConfiguredInterfaceBw configuredInterfaceBw = getConfiguredInterfaceBW(endpoint,
												associatedCpe);
										if (Objects.nonNull(configuredInterfaceBw)
												&& Objects.nonNull(configuredInterfaceBw.getInterfaces())
												&& Objects.nonNull(configuredInterfaceBw.getInterfaces().getVni()))
											configuredInterfaceBw.getInterfaces().getVni().stream()
													.filter(vni -> Objects.nonNull(vni)
															&& Objects.nonNull(vni.getBandwidth())
															&& uniqueLinks.contains(vni.getName()))
													.forEach(vni -> {
														if (Objects.isNull(vni.getBandwidth().getUplink()))
															vni.getBandwidth().setUplink(0);
														siteBWUtilization
																.setTotalUplinkBw(siteBWUtilization.getTotalUplinkBw()
																		+ (vni.getBandwidth().getUplink()) / 1000);
														if (Objects.isNull(vni.getBandwidth().getDownlink()))
															vni.getBandwidth().setDownlink(0);
														siteBWUtilization.setTotalDownlinkBw(
																siteBWUtilization.getTotalDownlinkBw()
																		+ (vni.getBandwidth().getDownlink()) / 1000);
													});
									} catch (TclCommonException e) {
										throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
									}
								});
					});
					siteBWUtilization
							.setTotalBw(siteBWUtilization.getTotalUplinkBw() + siteBWUtilization.getTotalDownlinkBw());
					siteBWUtilization.setUplinkBwUtil(siteBWUtilization.getUplinkBwUtil() / 1000000);
					if (siteBWUtilization.getTotalUplinkBw() != 0)
						siteBWUtilization.setUplinkUtilPercent(
								((double) siteBWUtilization.getUplinkBwUtil() / siteBWUtilization.getTotalUplinkBw())
										* 100);
					siteBWUtilization.setDownlinkBwUtil(siteBWUtilization.getDownlinkBwUtil() / 1000000);
					if (siteBWUtilization.getTotalDownlinkBw() != 0)
						siteBWUtilization.setDownlinkUtilPercent(((double) siteBWUtilization.getDownlinkBwUtil()
								/ siteBWUtilization.getTotalDownlinkBw()) * 100);
					siteBWUtilization.setBwUnit(finalBwUnit);
				});
				sdwanSiteDetails.forEach(site -> {
					if (!recurringSites.contains((String) site.get("serviceId"))) {
						if(serviceIds.contains((String) site.get("serviceId"))) {
							SiteBWUtilizationBean siteBWUtilizationBean = new SiteBWUtilizationBean();
							siteBWUtilizationBean.setSiteName((String) site.get("serviceId"));
							siteBWUtilizationBean.setSiteAlias((String) site.get("siteAlias"));
							siteBWUtilizationBean.setBwUnit(finalBwUnit);
							siteBWUtilizations.add(siteBWUtilizationBean);
						}
					}
				});
				HttpHeaders response=analyticsApiLogout(headers);
				LOGGER.info("Logout is status in getSdwanSiteUtilization method {} ", response);
			}
		}
		if(Objects.nonNull(siteBWUtilizations) && !siteBWUtilizations.isEmpty())
			siteBWUtilizations.sort((site1, site2) -> site2.getDownlinkUtilPercent().compareTo(site1.getDownlinkUtilPercent()));
		sdwanSiteUtilizationDetails.setSiteBWUtilization(siteBWUtilizations);
		sdwanSiteUtilizationDetails.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
		return sdwanSiteUtilizationDetails;
	}

	/**
	 * Get configured interface bandwidth
	 *
	 * @param sdwanEndpoints
	 * @param cpeName
	 * @return
	 * @throws TclCommonException
	 */
	public ConfiguredInterfaceBw getConfiguredInterfaceBW(SdwanEndpoints sdwanEndpoints, String cpeName)
			throws TclCommonException {
		ConfiguredInterfaceBw configuredInterfaceBw = new ConfiguredInterfaceBw();
		try {
			String url = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + versaConfiguredInterfaceBw;
			url = url.replaceAll("CPE_Name", cpeName);
			LOGGER.info(" getConfiguredInterfaceBW URL: {}",url);
			RestResponse response = restClientService.getWithBasicAuthentication(url, new HashMap<String, String>(),
					sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			if (response.getStatus() == Status.SUCCESS && Objects.nonNull(response.getData().compareTo("vni"))) {
				LOGGER.info("thread {} Response from getConfiguredInterfaceBW : {}", Thread.currentThread().getName(),
						response.getData());
				if(Objects.nonNull(response.getData()))
				configuredInterfaceBw = Utils.convertJsonToObject(response.getData(), ConfiguredInterfaceBw.class);
			} else {
				LOGGER.info("getConfiguredInterfaceBW cpeName {} Error response {} ",cpeName, response.getErrorMessage());
			}
		} catch (TclCommonException ex) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return configuredInterfaceBw;
	}

	/**
	 * Calculate aggregate of same sites
	 *
	 * @param siteBWUtilizations
	 * @param recurringSites
	 * @param allUtilizations
	 * @param overLayServiceAttrs
	 * @param siteNameToAliasMapping
	 * @return
	 */
	private List<SiteBWUtilizationBean> calculateAggregateOfSites(List<SiteBWUtilizationBean> siteBWUtilizations,
			Set<String> recurringSites, List<SiteBWUtilizationBean> allUtilizations,
			Map<String, String> siteNameToAliasMapping) {
		allUtilizations.forEach(siteBWUtilization -> {
			if (!recurringSites.contains(siteBWUtilization.getSiteName())) {
				LOGGER.info("Inside calculateAggregateOfSites for site name {} ",siteBWUtilization.getSiteName());
				SiteBWUtilizationBean siteBWUtilizationBean = new SiteBWUtilizationBean();
				siteBWUtilizationBean.setOrgName(siteBWUtilization.getOrgName());
				siteBWUtilizationBean.setSiteName(siteBWUtilization.getSiteName());
				siteBWUtilizationBean.setAssociatedCpes(siteBWUtilization.getAssociatedCpes());
				siteBWUtilizationBean.setSiteAlias(siteNameToAliasMapping.get(siteBWUtilizationBean.getSiteName()));
				siteBWUtilizationBean.setUplinkBwUtil(siteBWUtilization.getUplinkBwUtil());
				siteBWUtilizationBean.setDownlinkBwUtil(siteBWUtilization.getDownlinkBwUtil());
				siteBWUtilizationBean.setLinkAvailable(siteBWUtilization.getLinkAvailable());
				siteBWUtilizationBean.setLinkNames(siteBWUtilization.getLinkNames());
				siteBWUtilizationBean.setTemplates(siteBWUtilization.getTemplates());
				siteBWUtilizations.add(siteBWUtilizationBean);
				recurringSites.add(siteBWUtilizationBean.getSiteName());
			} else {
				siteBWUtilizations.stream().filter(siteBWUtilizationBean -> siteBWUtilizationBean.getSiteName()
						.equalsIgnoreCase(siteBWUtilization.getSiteName())).peek(siteBWUtilizationBean -> {
							siteBWUtilizationBean.setUplinkBwUtil(
									siteBWUtilizationBean.getUplinkBwUtil() + siteBWUtilization.getUplinkBwUtil());
							siteBWUtilizationBean.setDownlinkBwUtil(
									siteBWUtilizationBean.getDownlinkBwUtil() + siteBWUtilization.getDownlinkBwUtil());
							siteBWUtilizationBean.getTemplates().addAll(siteBWUtilization.getTemplates());
							siteBWUtilizationBean.getLinkNames().addAll(siteBWUtilization.getLinkNames());
						}).anyMatch(siteBWUtilizationBean -> siteBWUtilizationBean.getSiteName()
								.equalsIgnoreCase(siteBWUtilization.getSiteName()));
			}
		});
		
		return siteBWUtilizations;
	}

	/**
	 * Get cpe's interfaces information
	 *
	 * @param sdwanEndpoints
	 * @param cpeName
	 * @param orgName
	 * @return
	 * @throws TclCommonException
	 */
	public CpeInterfaceDetails getCpeInterfaceDetails(SdwanEndpoints sdwanEndpoints, String cpeName, String orgName)
			throws TclCommonException {
		CpeInterfaceDetails cpeInterfaceDetails = new CpeInterfaceDetails();
		try {
			LOGGER.info("Inside getCpeInterfaceDetails cpe name {} and org name {} ",cpeName, orgName);
			String url = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + versaCpeInterfaceDetails;
			url = url.replaceAll("CPE_Name", cpeName);
			url = url.replaceAll("Org_Name", orgName);
			LOGGER.info("Inside getCpeInterfaceDetails url {} ",url);
			RestResponse response = restClientService.getWithBasicAuthentication(url, new HashMap<String, String>(),
					sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
			LOGGER.info("Inside getCpeInterfaceDetails url {}  ",url);
			if (response.getStatus() == Status.SUCCESS && Objects.nonNull(response.getData())) {
				LOGGER.info("thread {} Response from getCpeInterfaceDetails : {}", Thread.currentThread().getName(),
						response.getData());
				cpeInterfaceDetails = Utils.convertJsonToObject(response.getData(), CpeInterfaceDetails.class);
			} else {
				LOGGER.info("getCpeInterfaceDetails cpeName {} and orgName {} Error  {}", cpeName, orgName, response.getErrorMessage());
			}
		} catch (TclCommonException ex) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return cpeInterfaceDetails;
	}

	/**
	 * Get link utilization information for CPEs in a site (SDWAN/DIA)
	 *
	 * @param siteBWUtilizations
	 * @param linkUtilizationUrl
	 * @param headers
	 * @param underlays
	 * @param templates
	 * @return
	 * @throws TclCommonException
	 */
	List<SiteBWUtilizationBean> getLinkUtilizationFromVersa(List<SiteBWUtilizationBean> siteBWUtilizations,
			String linkUtilizationUrl, HttpHeaders headers, List<Map<String, Object>> underlays, String orgName,
			List<SIServiceAdditionalInfo> templates) throws TclCommonException {
		LOGGER.info("Inside getLinkUtilizationFromVersa Versa URL {} and headers {} ", 
				linkUtilizationUrl, headers.toSingleValueMap());
		RestResponse response = restClientService.getWithProxy(linkUtilizationUrl, headers.toSingleValueMap(), false);
		if (response.getStatus() == Status.SUCCESS) {
			LinkUtilization linkUtilization = Utils.convertJsonToObject(response.getData(), LinkUtilization.class);
			int[] i = new int[1];
			Map<String, Integer> columnId = new HashMap<>();
			linkUtilization.getColumnMetaData().forEach(link -> {
				columnId.put(link.getId(), ++i[0]);
			});
			List<CpeBWCalculationBean> cpeBWCalculationBeans = new ArrayList<>();
			Set<String> checkLinkDuplicates = new HashSet<>();
			linkUtilization.getAaData().forEach(data -> {
				underlays.forEach(sdwanCpe -> {
					CpeBWCalculationBean cpeBWCalculationBean = new CpeBWCalculationBean();
					if ((((String) sdwanCpe.get("asset_name")).equalsIgnoreCase(data.get(columnId.get("siteName") - 1)))
							&& !checkLinkDuplicates.contains(data.get(columnId.get("siteName,accCktName") - 1))) {
						mapCpeCalculationBean(cpeBWCalculationBean, data, columnId, sdwanCpe);
						cpeBWCalculationBeans.add(cpeBWCalculationBean);
						checkLinkDuplicates.add(data.get(columnId.get("siteName,accCktName") - 1));
					}
				});
			});
			LOGGER.info("CPE calculation : {} ", Utils.convertObjectToJson(cpeBWCalculationBeans));
			Set<String> recurringSites = new HashSet<>();
			cpeBWCalculationBeans.forEach(cpeBWCalculationBean -> {
				if (!recurringSites.contains(cpeBWCalculationBean.getSiteName())) {
					SiteBWUtilizationBean siteBWUtilizationBean = new SiteBWUtilizationBean();
					siteBWUtilizationBean.setSiteName(cpeBWCalculationBean.getSiteName());
					siteBWUtilizationBean.setOrgName(orgName);
					siteBWUtilizationBean.setAssociatedCpes(new HashSet<>());
					siteBWUtilizationBean.getAssociatedCpes().add(cpeBWCalculationBean.getCpeName());
					siteBWUtilizationBean.setUplinkBwUtil(cpeBWCalculationBean.getUplinkBw());
					siteBWUtilizationBean.setDownlinkBwUtil(cpeBWCalculationBean.getDownlinkBw());
					siteBWUtilizationBean.setLinkAvailable(true);
					siteBWUtilizationBean.setBwUnit("bps");
					siteBWUtilizationBean.setLinkNames(new HashSet<>());
					siteBWUtilizationBean.getLinkNames().add(cpeBWCalculationBean.getLinkName());
					siteBWUtilizationBean.setTemplates(new HashSet<>());
					templates.stream()
							.filter(template -> cpeBWCalculationBean.getSysId().equals(template.getSysId())
									&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
											.equalsIgnoreCase(template.getAttributeName()))
							.peek(template -> siteBWUtilizationBean.getTemplates().add(template.getAttributeValue()))
							.anyMatch(template -> cpeBWCalculationBean.getSysId().equals(template.getSysId())
									&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
											.equalsIgnoreCase(template.getAttributeName()));
					siteBWUtilizations.add(siteBWUtilizationBean);
					recurringSites.add(cpeBWCalculationBean.getSiteName());
				} else {
					siteBWUtilizations.stream()
							.filter(siteBWUtilization -> siteBWUtilization.getSiteName()
									.equalsIgnoreCase(cpeBWCalculationBean.getSiteName()))
							.peek(siteBWUtilizationBean -> {
								siteBWUtilizationBean.getAssociatedCpes().add(cpeBWCalculationBean.getCpeName());
								siteBWUtilizationBean.setUplinkBwUtil(
										siteBWUtilizationBean.getUplinkBwUtil() + cpeBWCalculationBean.getUplinkBw());
								siteBWUtilizationBean.setDownlinkBwUtil(siteBWUtilizationBean.getDownlinkBwUtil()
										+ cpeBWCalculationBean.getDownlinkBw());
								siteBWUtilizationBean.getLinkNames().add(cpeBWCalculationBean.getLinkName());
								templates.stream()
										.filter(template -> cpeBWCalculationBean.getSysId().equals(template.getSysId())
												&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
														.equalsIgnoreCase(template.getAttributeName()))
										.peek(template -> siteBWUtilizationBean.getTemplates()
												.add(template.getAttributeValue()))
										.anyMatch(
												template -> cpeBWCalculationBean.getSysId().equals(template.getSysId())
														&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
																.equalsIgnoreCase(template.getAttributeName()));
							}).anyMatch(siteBWUtilization -> siteBWUtilization.getSiteName()
									.equalsIgnoreCase(cpeBWCalculationBean.getSiteName()));
				}
			});
		}
		return siteBWUtilizations;
	}

	/**
	 * Get cookies from a header
	 *
	 * @param headers
	 * @return
	 */
	Cookie getCookiesFromHeader(HttpHeaders headers) {
		LOGGER.info("Inside getCookiesFromHeader for the header value {}", headers);
		List<String> setCookies = new ArrayList<>();
		Optional<Map.Entry<String, List<String>>> setCookie = headers.entrySet().stream()
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

	/**
	 * To map Versa data to cpeBWCalculationBean
	 * @param cpeBWCalculationBean
	 * @param data
	 * @param columnId
	 * @param sdwanCpe
	 * @param templates
	 */
	private void mapCpeCalculationBean(CpeBWCalculationBean cpeBWCalculationBean, List<String> data,
			Map<String, Integer> columnId, Map<String, Object> sdwanCpe) {
		cpeBWCalculationBean.setSysId((Integer) sdwanCpe.get("sys_id"));
		cpeBWCalculationBean.setCpeName((String) sdwanCpe.get("asset_name"));
		cpeBWCalculationBean.setSiteName((String) sdwanCpe.get("sdwan_id"));
		cpeBWCalculationBean.setLinkName(data.get(columnId.get("accCktName") - 1));
		cpeBWCalculationBean.setUplinkBw(Objects.nonNull(data.get(columnId.get("bw-tx") - 1))
				? Double.parseDouble(data.get(columnId.get("bw-tx") - 1))
				: 0);
		cpeBWCalculationBean.setDownlinkBw(Objects.nonNull(data.get(columnId.get("bw-rx") - 1))
				? Double.parseDouble(data.get(columnId.get("bw-rx") - 1))
				: 0);
		cpeBWCalculationBean.setTotalBw(Objects.nonNull(data.get(columnId.get("bandwidth") - 1))
				? Double.parseDouble(data.get(columnId.get("bandwidth") - 1))
				: 0);
	}

	/**
	 * Method to login versa analytics
	 *
	 * @return
	 */
	private HttpHeaders connectVersaAnalytics(HttpServletRequest request, SdwanEndpoints endpoint) {
		LOGGER.info("Inside connectVersaAnalytics MDC token {}" ,MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String versaAnalyticsUrl = endpoint.getServerIp()+":"+endpoint.getServerPort()+versaAnalyticsLoginUrl;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		if (Objects.nonNull(request.getCookies()) && Objects.nonNull(request.getCookies()[0]))
			httpHeaders.add(HttpHeaders.COOKIE,
					request.getCookies()[0].getName() + "=" + request.getCookies()[0].getValue());
		//String requestBody = "username="+endpoint.getServerUsername()+"&password="+endpoint.getServerPassword();
		MultiValueMap<String, String> bodyParam= new LinkedMultiValueMap<String, String>();
		bodyParam.add("username", endpoint.getServerUsername());
		bodyParam.add("password", endpoint.getServerPassword());
		LOGGER.info("VERSA login URL {} headers {} and requestpayload {} ",versaAnalyticsUrl, httpHeaders.toSingleValueMap());
		HttpHeaders headers = restClientService.postAndReturnHeadersWithProxy(versaAnalyticsUrl, httpHeaders.toSingleValueMap(),bodyParam);
		LOGGER.info("Versa login response {}",headers);
		return headers;
	}
	
	/**
	 * To map Versa data to getBandwidthForTopLinks associated to site
	 *
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @param siteName
	 * 
	 * 
	 * 
	 * @returnList<SdwanLinkUsages>
	 */

	public List<SdwanLinkUsages> getBandwidthForTopLinks(String siteName, String orgName, String serverCode,
			HttpServletRequest request, String startDate, String endDate) throws TclCommonException {
		if (Objects.isNull(startDate))
			startDate = "30daysAgo";
		if (Objects.isNull(endDate))
			endDate = "today";
		ViewSiServiceInfoAllBean vwSiInfoOverlay = new ViewSiServiceInfoAllBean();
		List<SdwanAndDiaTraffic> sdwanTrafficResponse = new ArrayList<>();
		List<SdwanAndDiaTraffic> diaTrafficResponse = new ArrayList<>();
		Map<String, Object> sdWanSiteDetails = new HashMap<>();
		List<VwServiceAssetAttributeBean> vwSiInfoUnderlay = new ArrayList<>();
		
		Map<String, String> linkAndInterfaceMapping = new HashMap<>();
		Map<String, Bandwidth> interfaceAndBwMapping = new HashMap<>();
		List<SdwanLinkUsages> sdwanLinkUsagesResponse = new ArrayList<>();
		try {
			if (siteName == null || orgName == null || serverCode == null) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
			}
			sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSite(siteName);
			if (sdWanSiteDetails != null && !sdWanSiteDetails.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				vwSiInfoOverlay = mapper.convertValue(sdWanSiteDetails, ViewSiServiceInfoAllBean.class);
			}
			LOGGER.info("Inside  getBandwidthForTopLinks to fetch underlay product for IZOSDWAN Serviceids {}",
					vwSiInfoOverlay.getServiceId());
			List<Map<String, Object>> underlaySiteDetails = vwSiServiceInfoAllRepository
					.findByUndeyLayByIzoSdwanServiceIdsIn(Arrays.asList(vwSiInfoOverlay.getServiceId()));
			if (underlaySiteDetails != null && !underlaySiteDetails.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				underlaySiteDetails.stream().forEach(underlaySiteDetail -> {
					vwSiInfoUnderlay.add(mapper.convertValue(underlaySiteDetail, VwServiceAssetAttributeBean.class));
				});
			}
			LOGGER.info("organisation name for IZOSDWAN Serviceids {}", orgName);
			LOGGER.info("fetching versa url for {}", serverCode);
			SdwanEndpoints sdwanEndpoints = sdwanEndpointsRepository.findByServerCode(serverCode);
			Set<String> cpeLists = new HashSet<>();
			for (VwServiceAssetAttributeBean bean : vwSiInfoUnderlay) {
				cpeLists.add(bean.getAssetName());
			}
			List<SdwanEndpoints> analyticsEndpoints = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
			if(analyticsEndpoints == null || analyticsEndpoints.isEmpty()) {
				throw new TclCommonRuntimeException("Analytics endpoint not available");
			}
			Cookie cooki;
			HttpHeaders loginHeader = connectVersaAnalytics(request, analyticsEndpoints.get(0));
			cooki = getCookiesFromHeader(loginHeader);
			LOGGER.info("Cookie : {} ", Utils.convertObjectToJson(cooki));
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
			sdwanTrafficResponse = versaGetSdwanTraffic(orgName, headers, startDate, endDate,  analyticsEndpoints.get(0));
			diaTrafficResponse = versaGetDiaTraffic(orgName, headers, startDate, endDate,  analyticsEndpoints.get(0));
			LOGGER.info("getBandwidthForTopLinks sdwanTrafficResponse {}", sdwanTrafficResponse);
			LOGGER.info("getBandwidthForTopLinks diaTrafficResponse {}", diaTrafficResponse);
			for (String eachcpe : cpeLists) {
				List<SdwanLinkUsages> sdwanLinkUsagesList = new ArrayList<>();
				List<SdwanAndDiaTraffic> sdwanTrafficDetailsforCpe = new ArrayList<>();
				List<SdwanAndDiaTraffic> diaTrafficDetailsforCpe = new ArrayList<>();
				List<SdwanAndDiaTraffic> sdwanTrafficDetailsforAllCpe = new ArrayList<>();
				List<SdwanAndDiaTraffic> diaTrafficDetailsforAllCpe = new ArrayList<>();
				List<Vni> configuredInterfaceBwAllCpe = new ArrayList<>();
				List<SdwanWanInterface> cpeInterfaceDetailsAllCpe = new ArrayList<>();
				ConfiguredInterfaceBw configuredInterfaceBw = new ConfiguredInterfaceBw();
				CpeInterfaceDetails cpeInterfaceDetails = new CpeInterfaceDetails();
				if (Objects.nonNull(sdwanTrafficResponse))
					sdwanTrafficDetailsforCpe = sdwanTrafficResponse.stream()
							.filter(x -> x.getSiteName().equals(eachcpe)).collect(Collectors.toList());
				if (Objects.nonNull(diaTrafficResponse))
					diaTrafficDetailsforCpe = diaTrafficResponse.stream().filter(x -> x.getSiteName().equals(eachcpe))
							.collect(Collectors.toList());
				cpeInterfaceDetails = getCpeInterfaceDetails(sdwanEndpoints, eachcpe, orgName);
				if (Objects.nonNull(sdwanTrafficDetailsforCpe) && !sdwanTrafficDetailsforCpe.isEmpty())
					sdwanTrafficDetailsforAllCpe.addAll(sdwanTrafficDetailsforCpe);
				if (Objects.nonNull(diaTrafficDetailsforCpe) && !diaTrafficDetailsforCpe.isEmpty())
					diaTrafficDetailsforAllCpe.addAll(diaTrafficDetailsforCpe);
				if (Objects.nonNull(cpeInterfaceDetails) && Objects.nonNull(cpeInterfaceDetails.getCollection())
						&& Objects.nonNull(cpeInterfaceDetails.getCollection().getSdwanWanInterfaces())) {
					configuredInterfaceBw = getConfiguredInterfaceBW(sdwanEndpoints, eachcpe);
					cpeInterfaceDetailsAllCpe.addAll(cpeInterfaceDetails.getCollection().getSdwanWanInterfaces());
				}
				if (Objects.nonNull(configuredInterfaceBw) && Objects.nonNull(configuredInterfaceBw.getInterfaces())
						&& Objects.nonNull(configuredInterfaceBw.getInterfaces().getVni()))
					configuredInterfaceBwAllCpe.addAll(configuredInterfaceBw.getInterfaces().getVni());
				for (SdwanWanInterface cpeInterfaceDetails_new : cpeInterfaceDetailsAllCpe) {
					if (Objects.nonNull(cpeInterfaceDetails_new))
						linkAndInterfaceMapping.put(cpeInterfaceDetails_new.getCircuitName(),
								cpeInterfaceDetails_new.getIntfName());
				}

				LOGGER.info("linkAndInterfaceMapping {}", linkAndInterfaceMapping);
				Set<String> recurringlinks = new HashSet<>();
				List<Vni> totalBwForLinks = new ArrayList<>();
				if (Objects.nonNull(configuredInterfaceBwAllCpe) && !configuredInterfaceBwAllCpe.isEmpty()) {
					configuredInterfaceBwAllCpe.stream().forEach(atr -> {
					    LOGGER.info("linkAndInterfaceMapping configureBW {} and BW {} ", atr.getName(), atr.getBandwidth());
						Vni vni = new Vni();
						Bandwidth bw = new Bandwidth();
						if (!recurringlinks.contains(atr.getName())) {
						    if(atr.getBandwidth()== null) {
								bw.setUplink(0);
								bw.setDownlink(0);
							} else {
								bw.setUplink(atr.getBandwidth().getUplink() == null ? 0 : (atr.getBandwidth().getUplink()/1000));
								bw.setDownlink(atr.getBandwidth().getDownlink() == null ? 0 : (atr.getBandwidth().getDownlink()/1000));
							}
					// 		bw.setUplink(atr.getBandwidth()!= null && atr.getBandwidth().getUplink() == null ? 0 : (atr.getBandwidth().getUplink()/1000));
					// 		bw.setDownlink(atr.getBandwidth()!= null && atr.getBandwidth().getDownlink() == null ? 0 : (atr.getBandwidth().getDownlink()/1000));
							vni.setName(atr.getName());
							vni.setBandwidth(bw);
							totalBwForLinks.add(vni);
							recurringlinks.add(atr.getName());
						} else {
						    LOGGER.info("linkAndInterfaceMapping else condition configureBW name {} and BW {} ", atr.getName(), atr.getBandwidth());
							totalBwForLinks.stream().filter(link -> link.getName().equals(atr.getName())).peek(bean -> {
								Bandwidth bw1 = new Bandwidth();
								Integer uplink = 0;
								Integer downlink = 0;
								if(atr.getBandwidth() != null) {
								    uplink = atr.getBandwidth().getUplink() == null ? 0
										: (atr.getBandwidth().getUplink()/1000);
								    downlink = atr.getBandwidth().getDownlink() == null ? 0
										: (atr.getBandwidth().getDownlink()/1000);
								}
								
								bw1.setUplink(bean.getBandwidth().getUplink() + uplink);
								bw1.setDownlink(bean.getBandwidth().getDownlink() + downlink);
								bean.setBandwidth(bw1);
							}).anyMatch(link -> link.getName().equalsIgnoreCase(atr.getName()));
						}
					});
				}
				if (Objects.nonNull(totalBwForLinks) && !totalBwForLinks.isEmpty()) {
					totalBwForLinks.stream().forEach(atr -> {
						interfaceAndBwMapping.put(atr.getName(), atr.getBandwidth());
					});
					LOGGER.info("interfaceAndBwMapping {}", interfaceAndBwMapping);
				}
				Map<String,Set<String>> cpeAndLinkMapping =new HashMap<String, Set<String>>();
				if (Objects.nonNull(sdwanTrafficDetailsforAllCpe) && !sdwanTrafficDetailsforAllCpe.isEmpty()
						|| Objects.nonNull(diaTrafficDetailsforAllCpe) && !diaTrafficDetailsforAllCpe.isEmpty()) {
					List<SdwanLinkUsages> totalTrafficLinkWise = new ArrayList<>();
					Set<String> cpeName = new HashSet<>();
					for (SdwanAndDiaTraffic sdwanAndDiaTraffic : sdwanTrafficDetailsforAllCpe) {
						SdwanLinkUsages sdwanTrafficForEachLink = new SdwanLinkUsages();
						sdwanTrafficForEachLink.setLinkName(sdwanAndDiaTraffic.getSdwanBandwidths().getLinkName());
						sdwanTrafficForEachLink
								.setUtilizedUplink((double) (sdwanAndDiaTraffic.getSdwanBandwidths().getUplink()));
						sdwanTrafficForEachLink.setUtilizedDownlink(
								(double) (sdwanAndDiaTraffic.getSdwanBandwidths().getDownlink()));
						
						put(cpeAndLinkMapping,sdwanAndDiaTraffic.getSdwanBandwidths().getLinkName(),sdwanAndDiaTraffic.getSiteName());
						totalTrafficLinkWise.add(sdwanTrafficForEachLink);
					}
					for (SdwanAndDiaTraffic sdwanAndDiaTraffic : diaTrafficDetailsforAllCpe) {
						SdwanLinkUsages diaTrafficForEachLink = new SdwanLinkUsages();
						diaTrafficForEachLink.setLinkName(sdwanAndDiaTraffic.getSdwanBandwidths().getLinkName());
						diaTrafficForEachLink
								.setUtilizedUplink((double) (sdwanAndDiaTraffic.getSdwanBandwidths().getUplink()));
						diaTrafficForEachLink.setUtilizedDownlink(
								(double) (sdwanAndDiaTraffic.getSdwanBandwidths().getDownlink()));
						put(cpeAndLinkMapping,sdwanAndDiaTraffic.getSdwanBandwidths().getLinkName(),sdwanAndDiaTraffic.getSiteName());
						totalTrafficLinkWise.add(diaTrafficForEachLink);
					}
					LOGGER.info("total Traffic from both sdwan and dia {}", totalTrafficLinkWise);
					Set<String> recurringSites = new HashSet<>();
					totalTrafficLinkWise.forEach(atr -> {
						SdwanLinkUsages sdwanLinkUsages = new SdwanLinkUsages();
						if (!recurringSites.contains(atr.getLinkName())) {
							String intName = linkAndInterfaceMapping.get(atr.getLinkName());
							LOGGER.info("intName :: {}", intName);
							Set<String> cpe =cpeAndLinkMapping.get(atr.getLinkName());
							LOGGER.info("cpe :: {}", cpe);
							if(intName!=null) {
								String[] interfaceNameSplit = intName.split(Pattern.quote("."));
								sdwanLinkUsages.setInterfaceName(interfaceNameSplit[0]);
								LOGGER.info("sdwanLinkUsages.getInterfaceName :: {}", sdwanLinkUsages.getInterfaceName());
							}
							if(Objects.nonNull(cpe))
								sdwanLinkUsages.setCpe(cpe);
							sdwanLinkUsages.setSiteName(siteName);
							sdwanLinkUsages.setLinkName(atr.getLinkName());
							sdwanLinkUsages.setUtilizedUplink(atr.getUtilizedUplink());
							sdwanLinkUsages.setUtilizedDownlink(atr.getUtilizedDownlink());
							sdwanLinkUsages.setUnit("Mbps");
							sdwanLinkUsages.setOrgName(orgName);
							sdwanLinkUsagesList.add(sdwanLinkUsages);
							recurringSites.add(atr.getLinkName());
							LOGGER.info("recurringSites :: {}", recurringSites);
						} else {
							sdwanLinkUsagesList.stream()
									.filter(utilization -> utilization.getLinkName().equalsIgnoreCase(atr.getLinkName()))
									.peek(bean -> {
										bean.setUtilizedUplink(bean.getUtilizedUplink() + atr.getUtilizedUplink());
										bean.setUtilizedDownlink(bean.getUtilizedDownlink() + atr.getUtilizedDownlink());
									})
									.anyMatch(utilization -> utilization.getLinkName().equalsIgnoreCase(atr.getLinkName()));
						}
					});
					List<String> interfaceNamecombine = new ArrayList<>();
					sdwanLinkUsagesList.stream().forEach(atr -> {
						if (!interfaceNamecombine.contains(atr.getInterfaceName())) {
							Bandwidth bw = interfaceAndBwMapping.get(atr.getInterfaceName());
							if(Objects.nonNull(bw)) {
								atr.setTotalUplink((double) bw.getUplink());
								atr.setTotalDownlink((double) bw.getDownlink());
								if (atr.getTotalUplink() > 0) 
									atr.setUplinkUtilizedPercentage((atr.getUtilizedUplink() / atr.getTotalUplink()) * 100);
								else
									atr.setUplinkUtilizedPercentage((double) 0);
								if (atr.getTotalDownlink() > 0 )
									atr.setDownlinkUtilizedPercentage(
											(atr.getUtilizedDownlink() / atr.getTotalDownlink()) * 100);
								else
									atr.setDownlinkUtilizedPercentage((double) 0);
								sdwanLinkUsagesResponse.add(atr);
								interfaceNamecombine.add(atr.getInterfaceName());
								LOGGER.info("interfaceNamecombine ::{}",interfaceNamecombine);
							}
						
						} else {
							sdwanLinkUsagesResponse.stream().filter(
									utilization -> utilization.getInterfaceName().equalsIgnoreCase(atr.getInterfaceName()))
									.peek(bean -> {
										bean.setTotalUplink(bean.getTotalUplink());
										bean.setTotalDownlink(bean.getTotalDownlink());
										bean.setLinkName(bean.getLinkName() + "," + atr.getLinkName());
										bean.setUtilizedUplink(bean.getUtilizedUplink() + atr.getUtilizedUplink());
										bean.setUtilizedDownlink(bean.getUtilizedDownlink() + atr.getUtilizedDownlink());
										if (bean.getTotalUplink() > 0)
											bean.setUplinkUtilizedPercentage(
													(bean.getUtilizedUplink() / bean.getTotalUplink()) * 100);
										else
											bean.setUplinkUtilizedPercentage((double) 0);
										if (bean.getTotalDownlink() > 0)
											bean.setDownlinkUtilizedPercentage(
													(bean.getUtilizedDownlink() / bean.getTotalDownlink()) * 100);
										else
											bean.setDownlinkUtilizedPercentage((double) 0);
									}).anyMatch(utilization -> utilization.getInterfaceName()
											.equalsIgnoreCase(atr.getInterfaceName()));
						}
					});
				}
			}
				Collections.sort(sdwanLinkUsagesResponse, Collections.reverseOrder());
				HttpHeaders response=analyticsApiLogout(headers);
				LOGGER.info("Logout is status in getBandwidthForTopLinks method {} ", response);
			
		} catch (TclCommonException ex) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		if (sdwanLinkUsagesResponse.size() >= 5)
			return sdwanLinkUsagesResponse.subList(0, 4);
		return sdwanLinkUsagesResponse;
	}
	/**
	 * Get Data from versa on sdwan traffic link wise
	 *
	 * @param orgName
	 * @param loginHeaders
	 * 
	 * 
	 * 
	 * @return List<SdwanAndDiaTraffic>
	 */
	public List<SdwanAndDiaTraffic> versaGetSdwanTraffic(String orgName, HttpHeaders loginHeaders, String startDate,
			String endDate, SdwanEndpoints analyticsEndpoint)			throws TclCommonException {
		List<Map<String, String>> AlltableMappings = new ArrayList<Map<String, String>>();
		List<ColumnMetaDatum> columnMetaData = new ArrayList<>();
		List<String> id = new ArrayList<>();
		List<String> aaData = new ArrayList<>();
		List<SdwanAndDiaTraffic> sdwanAndDiaTrafficList = new ArrayList<>();
		try {
			String url = versaAnalyticsSdwanTrafficUrl+1000;
			url = url.replaceAll("DYNAMICORGNAME", orgName);
			url = url.replaceAll("<Start_Date>", startDate);
			url = url.replaceAll("<End_Date>", endDate);
			url = analyticsEndpoint.getServerIp()+":"+analyticsEndpoint.getServerPort()+url;
			LOGGER.info("Inside versaGetSdwanTraffic Hitting Versa URL {} heasers {} ", url, loginHeaders.toSingleValueMap());
			RestResponse response = restClientService.getWithProxy(url, loginHeaders.toSingleValueMap(), false);
			if (response.getStatus() == Status.SUCCESS) {
				LOGGER.info("thread {} Response from versaGetSdwanTraffic status : {}", Thread.currentThread().getName(),
						response.getData());
				LinkUtilization linkUtilization = Utils.convertJsonToObject(response.getData(), LinkUtilization.class);
				if (Objects.nonNull(linkUtilization) && Objects.nonNull(linkUtilization.getAaData()) && Objects.nonNull(linkUtilization.getColumnMetaData()) ) {
					columnMetaData = linkUtilization.getColumnMetaData();
					List<List<String>> aaDataLists = linkUtilization.getAaData();
					columnMetaData.forEach(ids -> {
						id.add(ids.getId());
					});
					for (int i = 0; i < aaDataLists.size(); i++) {
						aaData.addAll(aaDataLists.get(i));
						Map<String, String> tableMapping = new HashMap<>();
						for (int j = 0; j < aaData.size(); j++) {
							tableMapping.put(id.get(j), aaData.get(id.indexOf((id.get(j)))));
						}
						AlltableMappings.add(tableMapping);
						aaData.clear();
					}
					AlltableMappings.forEach(atr -> {
						SdwanBandwidths sdwanBandwidths = new SdwanBandwidths();
						SdwanAndDiaTraffic sdwanAndDiaTraffic = new SdwanAndDiaTraffic();
						sdwanBandwidths.setUplink(checkForNull((atr.get("bw-tx")))/1000000);
						sdwanBandwidths.setDownlink(checkForNull((atr.get("bw-rx")))/1000000);
						sdwanBandwidths.setBandwidth(checkForNull((atr.get("bandwidth")))/1000000);
						sdwanBandwidths.setLinkName(atr.get("accCktName"));
						sdwanAndDiaTraffic.setSiteName(atr.get("siteName"));
						sdwanAndDiaTraffic.setSdwanBandwidths(sdwanBandwidths);
						sdwanAndDiaTrafficList.add(sdwanAndDiaTraffic);
					});
				}
			}
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return sdwanAndDiaTrafficList;
	}
	
	/**
	 * Get Data from versa on dia traffic link wise
	 *
	 * @param orgName
	 * @param loginHeaders
	 *
	 *
	 *
	 * @param startDate
	 * @param endDate
	 * @return List<SdwanAndDiaTraffic>
	 */
	public List<SdwanAndDiaTraffic> versaGetDiaTraffic(String orgName, HttpHeaders loginHeaders, String startDate, 
			String endDate, SdwanEndpoints analyticsEndpoint)	throws TclCommonException {
		List<SdwanAndDiaTraffic> sdwanAndDiaTrafficList = new ArrayList<SdwanAndDiaTraffic>();
		List<Map<String, String>> AlltableMappings = new ArrayList<Map<String, String>>();
		List<ColumnMetaDatum> columnMetaData = new ArrayList<>();
		List<String> id = new ArrayList<>();
		List<String> aaData = new ArrayList<>();
		try {
			String url = versaAnalyticsDiaTrafficUrl+1000;
			url = url.replaceAll("DYNAMICORGNAME", orgName);
			url = url.replaceAll("<Start_Date>", startDate);
			url = url.replaceAll("<End_Date>", endDate);
			url = analyticsEndpoint.getServerIp()+":"+analyticsEndpoint.getServerPort()+url;
			LOGGER.info("Inside versaGetDiaTraffic Hitting Versa URL {} heasers {} ", url, loginHeaders.toSingleValueMap());
			RestResponse response = restClientService.getWithProxy(url, loginHeaders.toSingleValueMap(), false);
			if (response.getStatus() == Status.SUCCESS) {
				LOGGER.info("thread {} Response from versaGetDiaTraffic status : {}", Thread.currentThread().getName(),
						response.getData());
				LinkUtilization linkUtilization = Utils.convertJsonToObject(response.getData(), LinkUtilization.class);
				if (Objects.nonNull(linkUtilization) && Objects.nonNull(linkUtilization.getAaData()) && Objects.nonNull(linkUtilization.getColumnMetaData()) ) {
					columnMetaData = linkUtilization.getColumnMetaData();
					List<List<String>> aaDataLists = linkUtilization.getAaData();
					columnMetaData.forEach(ids -> {
						id.add(ids.getId());
					});
					for (int i = 0; i < aaDataLists.size(); i++) {
						aaData.addAll(aaDataLists.get(i));
						Map<String, String> tableMapping = new HashMap<>();
						for (int j = 0; j < aaData.size(); j++) {
							tableMapping.put(id.get(j), aaData.get(id.indexOf((id.get(j)))));
						}
						AlltableMappings.add(tableMapping);
						aaData.clear();
					}
					AlltableMappings.forEach(atr -> {
						SdwanBandwidths sdwanBandwidths = new SdwanBandwidths();
						SdwanAndDiaTraffic sdwanAndDiaTraffic = new SdwanAndDiaTraffic();
						sdwanBandwidths.setUplink(checkForNull((atr.get("bw-tx")))/1000000);
						sdwanBandwidths.setDownlink(checkForNull((atr.get("bw-rx")))/1000000);
						sdwanBandwidths.setBandwidth(checkForNull((atr.get("bandwidth")))/1000000);
						sdwanBandwidths.setLinkName(atr.get("accCktName"));
						sdwanAndDiaTraffic.setSiteName(atr.get("siteName"));
						sdwanAndDiaTraffic.setSdwanBandwidths(sdwanBandwidths);
						sdwanAndDiaTrafficList.add(sdwanAndDiaTraffic);
					});
				}
			}
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return sdwanAndDiaTrafficList;
	}

	/**
	 * Check for null, if null return 0 else value(long)
	 *
	 * @param object
	 * @return
	 */
	private double checkForNull(String object) {
		if (object != null)
			return Double.parseDouble(object);
		else
			return 0D;
	}

	/**
	 * Get bandwidth utilization of applications by link
	 *
	 * @param orgName
	 * @param linkName
	 * @param cpes
	 * @return
	 */
	public AppLinkUtilizationBean getApplicationUtilizationByLink(String orgName, String linkName, List<String> cpes,
			HttpServletRequest request, String startDate, String endDate) throws TclCommonException {
		if (Objects.isNull(startDate))
			startDate = "30daysAgo";
		if (Objects.isNull(endDate))
			endDate = "today";
		Cookie cookie;
		List<SdwanEndpoints> analyticsEndpoints = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
		if(analyticsEndpoints == null || analyticsEndpoints.isEmpty()) {
			throw new TclCommonRuntimeException("VERSA Analytics endpoint not available");
		} 
		HttpHeaders loginHeader = connectVersaAnalytics(request, analyticsEndpoints.get(0));
		cookie = getCookiesFromHeader(loginHeader);
		LOGGER.info("Cookie : {} ", Utils.convertObjectToJson(cookie));
		List<AppBWUtilizationBean> appBWUtilizations = new ArrayList<>();
		LinkUtilization appsUtilization = new LinkUtilization();
		Map<String, Integer> columnId = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, cookie.getName() + "=" + cookie.getValue());
		String finalStartDate = startDate;
		String finalEndDate = endDate;
		String analyticsUrl = analyticsEndpoints.get(0).getServerIp()+":"+analyticsEndpoints.get(0).getServerPort();
		cpes.forEach(cpe -> {
			String applicationUtilByLink = versaAnalyticsAppUsageByLinkUrl;
			applicationUtilByLink = applicationUtilByLink.replaceAll("DYNAMICORGNAME", orgName);
			applicationUtilByLink = applicationUtilByLink.replaceAll("DYNAMICCPENAME", cpe);
			applicationUtilByLink = applicationUtilByLink.replaceAll("DYNAMICLINKNAME", linkName);
			applicationUtilByLink = applicationUtilByLink.replaceAll("<Start_Date>", finalStartDate);
			applicationUtilByLink = applicationUtilByLink.replaceAll("<End_Date>", finalEndDate);
			applicationUtilByLink = analyticsUrl+applicationUtilByLink;
			appsUtilization.setAaData(new ArrayList<>());
			RestResponse response = restClientService.getWithProxy(applicationUtilByLink, headers.toSingleValueMap(), false);
			LOGGER.info("getApplicationUtilizationByLink response for URL {} {}",
					applicationUtilByLink, response );
			if (response.getStatus() == Status.SUCCESS) {
				try {
					LinkUtilization appUtilizationPerLink = Utils.convertJsonToObject(response.getData(),
							LinkUtilization.class);
					int[] i = { -1 };
					appUtilizationPerLink.getColumnMetaData().forEach(link -> {
						columnId.put(link.getId(), ++i[0]);
					});
					if (Objects.nonNull(appUtilizationPerLink.getAaData()))
						appsUtilization.getAaData().addAll(appUtilizationPerLink.getAaData());
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e);
				}
			}
		});
		Set<String> recurringAppNames = new HashSet<>();
		AppLinkUtilizationBean appLinkUtilizationBean = new AppLinkUtilizationBean();
		// finding total bandwidth utilized by all the apps in the link
		appsUtilization.getAaData().forEach(apps -> {appLinkUtilizationBean.setTotalBw(
				appLinkUtilizationBean.getTotalBw() + (checkForNull(apps.get(columnId.get("bandwidth"))) / 1000000));
		Double total = checkForNull(apps.get(columnId.get("bandwidth"))) / 1000000;
		LOGGER.info("App Total BW Bps {}  and Mbps {} and appTotalBW {} ",apps.get(columnId.get("bandwidth")), total, appLinkUtilizationBean.getTotalBw());
			});
		// calculating bandwidth utilization percentage by each app
		appsUtilization.getAaData().forEach(apps -> {
			if (!recurringAppNames.contains(apps.get(columnId.get("appId")))) {
				AppBWUtilizationBean appBWUtilizationBean = new AppBWUtilizationBean();
				appBWUtilizationBean.setAppName(apps.get(columnId.get("appId")));
				appBWUtilizationBean.setUplinkBwUtil(checkForNull(apps.get(columnId.get("bw-tx"))) / 1000000);
				appBWUtilizationBean.setDownlinkBwUtil(checkForNull(apps.get(columnId.get("bw-rx"))) / 1000000);
				appBWUtilizationBean.setUtilizedBw(
						appBWUtilizationBean.getUplinkBwUtil() + appBWUtilizationBean.getDownlinkBwUtil());
				appBWUtilizationBean.setUtilizedPercent(
						((double) appBWUtilizationBean.getUtilizedBw() / appLinkUtilizationBean.getTotalBw()) * 100);
				appBWUtilizationBean.setBwUnit("Mbps");
				appBWUtilizations.add(appBWUtilizationBean);
				recurringAppNames.add(apps.get(columnId.get("appId")));
			} else {
				appBWUtilizations.stream().filter(appBWUtilizationBean -> appBWUtilizationBean.getAppName()
						.equalsIgnoreCase(apps.get(columnId.get("appId")))).peek(appBWUtilizationBean -> {
							appBWUtilizationBean.setUplinkBwUtil(appBWUtilizationBean.getUplinkBwUtil()
									+ (checkForNull(apps.get(columnId.get("bw-tx"))) / 1000000));
							appBWUtilizationBean.setDownlinkBwUtil(appBWUtilizationBean.getDownlinkBwUtil()
									+ (checkForNull(apps.get(columnId.get("bw-rx"))) / 1000000));
							appBWUtilizationBean.setUtilizedBw(
									appBWUtilizationBean.getUplinkBwUtil() + appBWUtilizationBean.getDownlinkBwUtil());
							appBWUtilizationBean.setUtilizedPercent(((double) appBWUtilizationBean.getUtilizedBw()
									/ appLinkUtilizationBean.getTotalBw()) * 100);
						}).anyMatch(appBWUtilizationBean -> appBWUtilizationBean.getAppName()
								.equalsIgnoreCase(apps.get(columnId.get("appId"))));
			}
		});
		appBWUtilizations.sort((app1, app2) -> app2.getUtilizedPercent().compareTo(app1.getUtilizedPercent()));
		if (appBWUtilizations.size() > 5) {
			AppBWUtilizationBean appBWUtilizationBean = new AppBWUtilizationBean();
			appBWUtilizationBean.setAppName(CommonConstants.OTHERS);
			appBWUtilizationBean.setBwUnit("Mbps");
			List<AppBWUtilizationBean> appstoRemove = new ArrayList<>();
			IntStream.range(5, appBWUtilizations.size()).forEach(index -> {
				appBWUtilizationBean.setUplinkBwUtil(
						appBWUtilizationBean.getUplinkBwUtil() + appBWUtilizations.get(index).getUplinkBwUtil());
				appBWUtilizationBean.setDownlinkBwUtil(
						appBWUtilizationBean.getDownlinkBwUtil() + appBWUtilizations.get(index).getDownlinkBwUtil());
				appBWUtilizationBean.setUtilizedBw(
						appBWUtilizationBean.getUplinkBwUtil() + appBWUtilizationBean.getDownlinkBwUtil());
				appBWUtilizationBean.setUtilizedPercent(
						((double) appBWUtilizationBean.getUtilizedBw() / appLinkUtilizationBean.getTotalBw()) * 100);
				appstoRemove.add(appBWUtilizations.get(index));
			});
			appBWUtilizations.removeAll(appstoRemove);
			appBWUtilizations.add(appBWUtilizationBean);
		}
		appLinkUtilizationBean.setAppBWUtilizations(appBWUtilizations);
		appLinkUtilizationBean.setBwUnit("Mbps");
		HttpHeaders response=analyticsApiLogout(headers);
		LOGGER.info("Logout is status in getApplicationUtilizationByLink method {} ", response);
		return appLinkUtilizationBean;
	}
	
	/**
	 * Get bandwidth utilization of applications by orgName
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public AppLinkUtilizationBean getApplicationUtilizedPercentageForOrganisation(Integer productId, Integer customerId,
			Integer customerLeId, Integer partnerId, String startDate, String endDate, HttpServletRequest request)
			throws TclCommonException {
		List<AppBWUtilizationBean> appforAllOrgs = new ArrayList<>();
		AppLinkUtilizationBean appLinkUtilizationBean = new AppLinkUtilizationBean();
		List<Integer> partnerLeIds = new ArrayList<>();
		List<Integer> customerLeIds = new ArrayList<>();
		Set<String> orgName = new HashSet<>();
		Set<String> cpes = new HashSet<>();
		try {
			if (Objects.isNull(startDate))
				startDate = "30daysAgo";
			if (Objects.isNull(endDate))
				endDate = "today";
			List<SdwanEndpoints> analyticsEndpoints = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
			if(analyticsEndpoints == null || analyticsEndpoints.isEmpty()) {
				throw new TclCommonRuntimeException("Analytics endpoint not available");
			}
			Cookie cookie;
			HttpHeaders loginHeader = connectVersaAnalytics(request, analyticsEndpoints.get(0));
			cookie = getCookiesFromHeader(loginHeader);
			LOGGER.info("Cookie : {} ", Utils.convertObjectToJson(cookie));
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.COOKIE, cookie.getName() + "=" + cookie.getValue());
			getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			if (!customerLeIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<String> sdwanServiceIds = new ArrayList<>();
				List<Integer> sdwanSysId = new ArrayList<>();
				List<Map<String, Object>> sdwanSiteDetails = new ArrayList<>();
				sdwanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetails(customerLeIds, partnerLeIds, productId,
						customerId, partnerId);
				Map<String, String> siteNameToAliasMapping = new HashMap<>();
				sdwanSiteDetails.stream().forEach(sdwanSiteDetail -> {
					sdwanServiceIds.add((String) sdwanSiteDetail.get("serviceId"));
					sdwanSysId.add((Integer) sdwanSiteDetail.get("sysId"));
					siteNameToAliasMapping.put((String) sdwanSiteDetail.get("serviceId"),
							(String) sdwanSiteDetail.get("siteAlias"));
				});
				if (!sdwanServiceIds.isEmpty() && !sdwanSysId.isEmpty()) {
					List<Map<String, Object>> underlays = vwSiServiceInfoAllRepository
							.findUnderlayServiceIdbySdwanServiceId(sdwanServiceIds);
					underlays.stream().forEach(cpe->{
						cpes.add((String)cpe.get("asset_name"));
					});
					List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
							.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
									Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME));
					overLayServiceAttrs.stream().peek(attributes -> {
						if (ServiceInventoryConstants.SDWAN_ORGANISATION_NAME
								.equalsIgnoreCase(attributes.getAttributeName()))
							orgName.add(attributes.getAttributeValue());
					}).collect(Collectors.toSet());
					String finalStartDate = startDate;
					String finalEndDate = endDate;
					orgName.forEach(org -> {
						List<AppBWUtilizationBean> appBWUtilizationBean = getAppByOrg(org, finalStartDate, finalEndDate, headers, analyticsEndpoints.get(0));
						LOGGER.info("App Utilization by OrgName : {} ", appBWUtilizationBean);
						if (!appBWUtilizationBean.isEmpty() && Objects.nonNull(appBWUtilizationBean)) {
							cpes.stream().forEach(eachCpe->{
								List<AppBWUtilizationBean> bean=appBWUtilizationBean.stream().filter(utilization ->utilization.getCpe().equals(eachCpe)).
								collect(Collectors.toList());
								appforAllOrgs.addAll(bean);
							});
						}
					});
					List<AppBWUtilizationBean> appBw = new ArrayList<AppBWUtilizationBean>();
					appforAllOrgs.stream().forEach(atr -> {
						appLinkUtilizationBean.setTotalBw(appLinkUtilizationBean.getTotalBw() + atr.getUtilizedBw());
					});
					Set<String> recurringAppName = new HashSet<String>();
					appforAllOrgs.stream().forEach(atr -> {
						if (!recurringAppName.contains(atr.getAppName())) {
							appLinkUtilizationBean.setBwUnit("Mbps");
							if (appLinkUtilizationBean.getTotalBw() != null) {
								appLinkUtilizationBean.setTotalBw(appLinkUtilizationBean.getTotalBw());
								atr.setUtilizedPercent(
										(atr.getUtilizedBw() / appLinkUtilizationBean.getTotalBw()) * 100);
								appBw.add(atr);
							}
							recurringAppName.add(atr.getAppName());
						} else {
							appBw.stream()
									.filter(utilization -> utilization.getAppName().equalsIgnoreCase(atr.getAppName()))
									.peek(bean -> {
										bean.setUtilizedBw(bean.getUtilizedBw() + atr.getUtilizedBw());
										bean.setUplinkBwUtil(bean.getUplinkBwUtil() + atr.getUplinkBwUtil());
										bean.setDownlinkBwUtil(bean.getDownlinkBwUtil() + atr.getDownlinkBwUtil());
										bean.setUtilizedPercent(
												(bean.getUtilizedBw() / appLinkUtilizationBean.getTotalBw()) * 100);
									}).anyMatch(
											utilization -> utilization.getAppName().equalsIgnoreCase(atr.getAppName()));
						}
					});
					if (!appBw.isEmpty()) {
						appBw.sort((app1, app2) -> app2.getUtilizedPercent().compareTo(app1.getUtilizedPercent()));
						appLinkUtilizationBean.setAppBWUtilizations(appBw);
					}
				}
			}
			HttpHeaders response=analyticsApiLogout(headers);
			LOGGER.info("Logout is status in getApplicationUtilizedPercentageForOrganisation method {} ", response);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return appLinkUtilizationBean;
	}

	public List<AppBWUtilizationBean> getAppByOrg(String org, String startDate, String endDate, HttpHeaders headers, SdwanEndpoints analyticsEndpoint) {
		List<Map<String, String>> AlltableMappings = new ArrayList<Map<String, String>>();
		List<String> id = new ArrayList<>();
		List<String> aaData = new ArrayList<>();
		List<AppBWUtilizationBean> appLinkUtilizationList = new ArrayList<>();
		try {
			String url = versaAnalyticsAppBwUrl;
			url = url.replaceAll("<Org_Name>", org);
			url = url.replaceAll("<START_DATE>", startDate);
			url = url.replaceAll("<END_DATE>", endDate);
			url = analyticsEndpoint.getServerIp()+":"+analyticsEndpoint.getServerPort()+url;
			LOGGER.info("Inside getAppByOrg Hitting versa with URL {} headers {} ", url, headers.toSingleValueMap());
			RestResponse response = restClientService.getWithProxy(url, headers.toSingleValueMap(), false);
			if (response.getStatus() == Status.SUCCESS) {
				LOGGER.info("thread {} Response from getAppByOrg: {} ", Thread.currentThread().getName(),
						response);
				LinkUtilization appUtilizationByOrg = Utils.convertJsonToObject(response.getData(),
						LinkUtilization.class);
				if (Objects.nonNull(appUtilizationByOrg) && Objects.nonNull(appUtilizationByOrg.getAaData())
						&& Objects.nonNull(appUtilizationByOrg.getColumnMetaData())) {
					List<ColumnMetaDatum> columnMetaData = appUtilizationByOrg.getColumnMetaData();
					List<List<String>> aaDataLists = appUtilizationByOrg.getAaData();
					columnMetaData.forEach(ids -> {
						id.add(ids.getId());
					});
					for (int i = 0; i < aaDataLists.size(); i++) {
						aaData.addAll(aaDataLists.get(i));
						Map<String, String> tableMapping = new HashMap<>();
						for (int j = 0; j < aaData.size(); j++) {
							tableMapping.put(id.get(j), aaData.get(id.indexOf((id.get(j)))));
						}
						AlltableMappings.add(tableMapping);
						aaData.clear();
					}
					AlltableMappings.forEach(atr -> {
						AppBWUtilizationBean appUtilization = new AppBWUtilizationBean();
						appUtilization.setAppName(atr.get("appId"));
						appUtilization.setUtilizedBw(checkForNull(atr.get("bandwidth")) / 1000000);
						appUtilization.setUplinkBwUtil(checkForNull(atr.get("bw-tx")) / 1000000);
						appUtilization.setDownlinkBwUtil(checkForNull(atr.get("bw-rx")) / 1000000);
						appUtilization.setBwUnit("Mbps");
						appUtilization.setLinkName(atr.get("accCktName"));
						appUtilization.setCpe(atr.get("siteName"));
						appLinkUtilizationList.add(appUtilization);
					});
				}
			}
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return appLinkUtilizationList;
	}

	/**
	 * Get utilization of an application in each site of a customer
	 * 
	 * @param appName
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param request
	 * @param bwUnit
	 * @param startDate
	 * @param endDate
	 * @param maxSites
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanSiteUtilizationDetails getSiteUtilizationByApp(String appName, Integer productId, Integer customerId,
			Integer customerLeId, Integer partnerId, HttpServletRequest request, String bwUnit, String startDate,
			String endDate, Integer maxSites) throws TclCommonException {
		if (Objects.isNull(bwUnit))
			bwUnit = "Mbps";
		if (Objects.isNull(startDate))
			startDate = "30daysAgo";
		if (Objects.isNull(endDate))
			endDate = "today";
		if (Objects.isNull(maxSites))
			maxSites = 5;
		List<Integer> customerLeIds = new ArrayList<>();
		List<Integer> partnerLeIds = new ArrayList<>();
		getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
		SdwanSiteUtilizationDetails sdwanSiteUtilizationDetails = new SdwanSiteUtilizationDetails();
		List<SiteBWUtilizationBean> siteBWUtilizations = new ArrayList<>();
		LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
		List<Map<String, Object>> sdwanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetails(customerLeIds,
				partnerLeIds, productId, customerId, partnerId);
		List<String> sdwanServiceIds = new ArrayList<>();
		List<Integer> sdwanSysId = new ArrayList<>();
		Map<String, String> siteNameToAliasMapping = new HashMap<>();
		sdwanSiteDetails.stream().forEach(sdwanSiteDetail -> {
			sdwanServiceIds.add((String) sdwanSiteDetail.get("serviceId"));
			sdwanSysId.add((Integer) sdwanSiteDetail.get("sysId"));
			siteNameToAliasMapping.put((String) sdwanSiteDetail.get("serviceId"),
					(String) sdwanSiteDetail.get("siteAlias"));
		});
		if (!sdwanServiceIds.isEmpty() && !sdwanSysId.isEmpty()) {
			List<Map<String, Object>> underlays = vwSiServiceInfoAllRepository
					.findUnderlayServiceIdbySdwanServiceId(sdwanServiceIds);
			List<Integer> underlaySysIds = underlays.stream().map(underlay -> (Integer) underlay.get("sys_id"))
					.collect(Collectors.toList());
			List<SIServiceAdditionalInfo> templates = serviceAdditionalInfoRepository
					.findBySysIdAndAttributeName(underlaySysIds, ServiceInventoryConstants.SDWAN_TEMPLATE_NAME);
			List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
					.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
							Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
									ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
			Map<String, String> siteToInstanceRegion = new HashMap<>();
			Set<String> orgNames = new HashSet<>();
			overLayServiceAttrs.stream().peek(attributes -> {
				if (ServiceInventoryConstants.SDWAN_ORGANISATION_NAME.equalsIgnoreCase(attributes.getAttributeName()))
					orgNames.add(attributes.getAttributeValue());
				else if (ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING
						.equalsIgnoreCase(attributes.getAttributeName()))
					siteToInstanceRegion.put(attributes.getSiServiceDetail().getTpsServiceId(),
							attributes.getAttributeValue());
			}).collect(Collectors.toSet());
			List<SdwanEndpoints> analyticsEndpoints = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
			if(analyticsEndpoints == null || analyticsEndpoints.isEmpty()) {
				throw new TclCommonRuntimeException("Analytics endpoint not available");
			}
			Cookie cookie;
			HttpHeaders loginHeader = connectVersaAnalytics(request, analyticsEndpoints.get(0));
			cookie = getCookiesFromHeader(loginHeader);
			LOGGER.info("Cookie : {} ", Utils.convertObjectToJson(cookie));
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(HttpHeaders.COOKIE, cookie.getName() + "=" + cookie.getValue());
			sdwanSiteUtilizationDetails.setKey(httpHeaders.get(HttpHeaders.COOKIE).get(0));
			String finalStartDate = startDate;
			String finalEndDate = endDate;
			orgNames.forEach(orgName -> {
				try {
					String url = versaAnalyticsSiteUsageByAppUrl;
					url = url.replaceAll("DYNAMICORGNAME", orgName);
					url = url.replaceAll("<Appln_Name>", appName);
					url = url.replaceAll("<Start_Date>", finalStartDate);
					url = url.replaceAll("<End_Date>", finalEndDate);
					url = analyticsEndpoints.get(0).getServerIp()+":"+analyticsEndpoints.get(0).getServerPort()+url;
					LOGGER.info("getSiteUtilizationByApp Analytics request URL and headers {} {}", url, httpHeaders.toSingleValueMap());
					RestResponse restResponse = restClientService.getWithProxy(url, httpHeaders.toSingleValueMap(), false);
					LOGGER.info("getSiteUtilizationByApp Analytics response {}", restResponse);
					if (restResponse.getStatus() == Status.SUCCESS) {
						LinkUtilization appUtilizationWithCpe = Utils.convertJsonToObject(restResponse.getData(),
								LinkUtilization.class);
						int[] i = { -1 };
						Map<String, Integer> columnId = new HashMap<>();
						appUtilizationWithCpe.getColumnMetaData().forEach(link -> {
							columnId.put(link.getId(), ++i[0]);
						});

						Set<String> recurringSites = new HashSet<>();
						Set<String> recurringCPEs = new HashSet<>();
						underlays.forEach(underlay -> {
							if (!recurringSites.contains((String) underlay.get("sdwan_id"))) {
								appUtilizationWithCpe.getAaData().forEach(apps -> {
									if (((String) underlay.get("asset_name"))
											.equalsIgnoreCase(apps.get(columnId.get("siteName")))) {
										if (!recurringCPEs.contains(apps.get(columnId.get("siteName")))) {
											SiteBWUtilizationBean siteBWUtilizationBean = new SiteBWUtilizationBean();
											siteBWUtilizationBean.setSiteName((String) underlay.get("sdwan_id"));
											siteBWUtilizationBean.setAssociatedCpes(new HashSet<>());
											siteBWUtilizationBean.setLinkNames(new HashSet<>());
											siteBWUtilizationBean.getAssociatedCpes()
													.add(apps.get(columnId.get("siteName")));
											siteBWUtilizationBean.getLinkNames()
													.add(apps.get(columnId.get("accCktName")));
											siteBWUtilizationBean
													.setUplinkBwUtil(checkForNull(apps.get(columnId.get("bw-tx"))));
											siteBWUtilizationBean
													.setDownlinkBwUtil(checkForNull(apps.get(columnId.get("bw-rx"))));
											siteBWUtilizationBean.setOrgName(orgName);
											siteBWUtilizationBean.setSiteAlias(
													siteNameToAliasMapping.get(siteBWUtilizationBean.getSiteName()));
											siteBWUtilizationBean.setLinkAvailable(true);
											siteBWUtilizationBean.setTemplates(new HashSet<>());
											templates.stream().filter(
													template -> underlay.get("sys_id").equals(template.getSysId())
															&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
																	.equalsIgnoreCase(template.getAttributeName()))
													.peek(template -> siteBWUtilizationBean.getTemplates()
															.add(template.getAttributeValue()))
													.anyMatch(template -> underlay.get("sys_id")
															.equals(template.getSysId())
															&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
																	.equalsIgnoreCase(template.getAttributeName()));
											siteBWUtilizations.add(siteBWUtilizationBean);
											recurringSites.add((String) underlay.get("sdwan_id"));
											recurringCPEs.add(apps.get(columnId.get("siteName")));
										} else {
											siteBWUtilizations.stream()
													.filter(siteBWUtilization -> siteBWUtilization.getSiteName()
															.equalsIgnoreCase((String) underlay.get("sdwan_id"))
															&& siteBWUtilization.getAssociatedCpes()
																	.contains((String) underlay.get("asset_name")))
													.peek(siteBWUtilization -> {
														siteBWUtilization.getAssociatedCpes()
																.add(apps.get(columnId.get("siteName")));
														siteBWUtilization.getLinkNames()
																.add(apps.get(columnId.get("accCktName")));
														siteBWUtilization.setUplinkBwUtil(
																siteBWUtilization.getUplinkBwUtil() + checkForNull(
																		apps.get(columnId.get("bw-tx"))));
														siteBWUtilization.setDownlinkBwUtil(
																siteBWUtilization.getDownlinkBwUtil() + checkForNull(
																		apps.get(columnId.get("bw-rx"))));
														templates.stream().filter(template -> underlay.get("sys_id")
																.equals(template.getSysId())
																&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
																		.equalsIgnoreCase(template.getAttributeName()))
																.peek(template -> siteBWUtilization.getTemplates()
																		.add(template.getAttributeValue()))
																.anyMatch(template -> underlay.get("sys_id")
																		.equals(template.getSysId())
																		&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
																				.equalsIgnoreCase(
																						template.getAttributeName()));
													})
													.anyMatch(siteBWUtilization -> siteBWUtilization.getSiteName()
															.equalsIgnoreCase((String) underlay.get("sdwan_id"))
															&& !siteBWUtilization.getAssociatedCpes()
																	.contains((String) underlay.get("asset_name")));
										}
									}
								});
							} else {
								if (!recurringCPEs.contains((String) underlay.get("asset_name")))
									siteBWUtilizations.stream()
											.filter(siteBWUtilization -> siteBWUtilization.getSiteName()
													.equalsIgnoreCase((String) underlay.get("sdwan_id")))
											.peek(siteBWUtilization -> {
												appUtilizationWithCpe.getAaData().forEach(apps -> {
													if (((String) underlay.get("asset_name"))
															.equalsIgnoreCase(apps.get(columnId.get("siteName")))) {
														siteBWUtilization.getAssociatedCpes()
																.add(apps.get(columnId.get("siteName")));
														siteBWUtilization.getLinkNames()
																.add(apps.get(columnId.get("accCktName")));
														siteBWUtilization.setUplinkBwUtil(
																siteBWUtilization.getUplinkBwUtil() + checkForNull(
																		apps.get(columnId.get("bw-tx"))));
														siteBWUtilization.setDownlinkBwUtil(
																siteBWUtilization.getDownlinkBwUtil() + checkForNull(
																		apps.get(columnId.get("bw-rx"))));
														templates.stream().filter(template -> underlay.get("sys_id")
																.equals(template.getSysId())
																&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
																		.equalsIgnoreCase(template.getAttributeName()))
																.peek(template -> siteBWUtilization.getTemplates()
																		.add(template.getAttributeValue()))
																.anyMatch(template -> underlay.get("sys_id")
																		.equals(template.getSysId())
																		&& ServiceInventoryConstants.SDWAN_TEMPLATE_NAME
																				.equalsIgnoreCase(
																						template.getAttributeName()));
													}
												});
											}).anyMatch(siteBWUtilization -> siteBWUtilization.getSiteName()
													.equalsIgnoreCase((String) underlay.get("sdwan_id")));
							}
						});
					}
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
			siteBWUtilizations
					.sort(Comparator.comparing(SiteBWUtilizationBean::getDownlinkBwUtil, Comparator.reverseOrder()));
			List<SiteBWUtilizationBean> trimmedSiteBWUtilizations;
			if (siteBWUtilizations.size() > maxSites)
				trimmedSiteBWUtilizations = siteBWUtilizations.subList(0, maxSites);
			else
				trimmedSiteBWUtilizations = siteBWUtilizations;
			List<SdwanEndpoints> sdwanEndpoints = sdwanEndpointsRepository
					.findByServerCodeIn(Sets.newHashSet(siteToInstanceRegion.values()));
			Map<String, List<SdwanEndpoints>> instanceGroupByRegion = sdwanEndpoints.stream()
					.collect(Collectors.groupingBy(SdwanEndpoints::getServerCode));
			String finalBwUnit = bwUnit;
			trimmedSiteBWUtilizations.forEach(siteBWUtilization -> {
				Set<String> uniqueInterfaces = new HashSet<>();
				siteBWUtilization.setInstanceRegion(siteToInstanceRegion.get(siteBWUtilization.getSiteName()));
				siteBWUtilization.setInterfaces(new HashSet<>());
				siteBWUtilization.getAssociatedCpes().forEach(associatedCpe -> {
					instanceGroupByRegion.get(siteToInstanceRegion.get(siteBWUtilization.getSiteName()))
							.forEach(endpoint -> {
								try {
									CpeInterfaceDetails cpeInterfaceDetails = getCpeInterfaceDetails(endpoint,
											associatedCpe, siteBWUtilization.getOrgName());
									if (Objects.nonNull(cpeInterfaceDetails)
											&& Objects.nonNull(cpeInterfaceDetails.getCollection()) && Objects.nonNull(
													cpeInterfaceDetails.getCollection().getSdwanWanInterfaces())) {
										cpeInterfaceDetails.getCollection().getSdwanWanInterfaces().stream()
												.forEach(linkInterface -> {
													siteBWUtilization.getLinkNames()
															.add(linkInterface.getCircuitName());
													siteBWUtilization.getInterfaces()
															.add(linkInterface.getIntfName().split("\\.")[0]);
													uniqueInterfaces.add(linkInterface.getIntfName().split("\\.")[0]);
												});
									}
									ConfiguredInterfaceBw configuredInterfaceBw = getConfiguredInterfaceBW(endpoint,
											associatedCpe);
									if (Objects.nonNull(configuredInterfaceBw)
											&& Objects.nonNull(configuredInterfaceBw.getInterfaces())
											&& Objects.nonNull(configuredInterfaceBw.getInterfaces().getVni()))
										configuredInterfaceBw.getInterfaces().getVni().stream()
												.filter(vni -> Objects.nonNull(vni)
														&& Objects.nonNull(vni.getBandwidth())
														&& uniqueInterfaces.contains(vni.getName()))
												.forEach(vni -> {
													if (Objects.isNull(vni.getBandwidth().getUplink()))
														vni.getBandwidth().setUplink(0);
													siteBWUtilization
															.setTotalUplinkBw(siteBWUtilization.getTotalUplinkBw()
																	+ (vni.getBandwidth().getUplink()) / 1000);
													if (Objects.isNull(vni.getBandwidth().getDownlink()))
														vni.getBandwidth().setDownlink(0);
													siteBWUtilization
															.setTotalDownlinkBw(siteBWUtilization.getTotalDownlinkBw()
																	+ (vni.getBandwidth().getDownlink()) / 1000);
												});
								} catch (TclCommonException e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
								}
							});
				});
				siteBWUtilization
						.setTotalBw(siteBWUtilization.getTotalUplinkBw() + siteBWUtilization.getTotalDownlinkBw());
				siteBWUtilization.setUplinkBwUtil(siteBWUtilization.getUplinkBwUtil() / 1000000);
				if (siteBWUtilization.getTotalUplinkBw() != 0)
					siteBWUtilization.setUplinkUtilPercent(
							((double) siteBWUtilization.getUplinkBwUtil() / siteBWUtilization.getTotalUplinkBw())
									* 100);
				siteBWUtilization.setDownlinkBwUtil(siteBWUtilization.getDownlinkBwUtil() / 1000000);
				if (siteBWUtilization.getTotalDownlinkBw() != 0)
					siteBWUtilization.setDownlinkUtilPercent(
							((double) siteBWUtilization.getDownlinkBwUtil() / siteBWUtilization.getTotalDownlinkBw())
									* 100);
				siteBWUtilization.setBwUnit(finalBwUnit);
			});
			sdwanSiteUtilizationDetails.setSiteBWUtilization(trimmedSiteBWUtilizations);
			HttpHeaders response=analyticsApiLogout(httpHeaders);
			LOGGER.info("Logout is status in getSiteUtilizationByApp method {} ", response);
		}
		sdwanSiteUtilizationDetails.setTimestamp(ServiceInventoryUtils.dateConvertor(new Date()));
		return sdwanSiteUtilizationDetails;
	}
	/**
	 * For Map<String,List<String>> addition of values to same key
	 *
	 * @param map
	 * @param key
	 * @param value
	 * 
	 */
	public static void put(Map<String, Set<String>> map, String key, String value) {
        if (map.get(key) == null) {
            Set<String> list = new HashSet<>();
            list.add(value);
            map.put(key, list);
        } else {
            map.get(key).add(value);
        }
    }

	/**
	 * Returns Path details for a given template
	 *
	 * @param templateName
	 * @param region
	 * @return
	 */
	public Set<TemplatePathInterfaceDetail> getPathInfoForTemplate(String templateName, String region,
			String profileName, String orgName) throws TclCommonException {
		Objects.requireNonNull(templateName);
		Objects.requireNonNull(region);
		Set<TemplatePathInterfaceDetail> templatePathInterfaceDetails = new HashSet<>();
		SdwanEndpoints sdwanEndpoints = sdwanEndpointsRepository.findByServerCode(region);
		String url = versaApiWanPathDetailsUrl;
		url = url.replaceAll("DYNAMICTEMPLATENAME", templateName);
		url = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + url;
		RestResponse restResponse = restClientService.getWithBasicAuthentication(url, new HashMap<>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (restResponse.getStatus() == Status.SUCCESS) {
			WanPathDetails templatePathDetails = Utils.convertJsonToObject(restResponse.getData(),
					WanPathDetails.class);
			if (Objects.nonNull(templatePathDetails) && Objects.nonNull(templatePathDetails.getWanInterfaces())
					&& Objects.nonNull(templatePathDetails.getWanInterfaces().getVni())) {
				templatePathDetails.getWanInterfaces().getVni().forEach(interfaceDetails -> {
					TemplatePathInterfaceDetail pathInterfaceDetail = new TemplatePathInterfaceDetail();
					pathInterfaceDetail.setInterfaceName(interfaceDetails.getName());
					if (Objects.nonNull(interfaceDetails.getInet()))
						pathInterfaceDetail.setPathName(interfaceDetails.getInet().getCircuitName());
					templatePathInterfaceDetails.add(pathInterfaceDetail);
				});
				Set<String> pathNames = templatePathInterfaceDetails.stream().map(path -> path.getPathName())
						.collect(Collectors.toSet());
				ForwardingProfile forwardingProfile = getForwardingProfileByName(profileName, templateName, orgName,
						sdwanEndpoints);
				if (Objects.nonNull(forwardingProfile) && Objects.nonNull(forwardingProfile.getForwardingProfile())
						&& Objects.nonNull(forwardingProfile.getForwardingProfile().getCircuitPriorities())) {
					forwardingProfile.getForwardingProfile().getCircuitPriorities().getPriority().stream()
							.filter(Objects::nonNull).forEach(priority -> {
								if (Objects.nonNull(priority.getCircuitNames())
										&& Objects.nonNull(priority.getCircuitNames().getLocal())
										&& !pathNames.containsAll(priority.getCircuitNames().getLocal())) {
									priority.getCircuitNames().getLocal().removeAll(pathNames);
									priority.getCircuitNames().getLocal().forEach(inactiveLocal -> {
										TemplatePathInterfaceDetail templatePathInterfaceDetail = new TemplatePathInterfaceDetail();
										templatePathInterfaceDetail.setPathName(inactiveLocal);
										templatePathInterfaceDetail.setStatus(false);
										templatePathInterfaceDetails.add(templatePathInterfaceDetail);
									});
								}
							});
				}
			}
		}
		return templatePathInterfaceDetails;
	}

	/**
	 * To get total utilized and total Bandwidth for org level
	 *
	 * @param productId
	 * @param customerId
	 * @param customerLeId
	 * @param partnerId
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @return
	 */
	
	public SdwanBandwidthUtilized getBandwidthUtilizedAndTotalBw(Integer productId, Integer customerId,
			Integer customerLeId, Integer partnerId, String startDate, String endDate, HttpServletRequest request) {
		List<Integer> partnerLeIds = new ArrayList<>();
		List<Integer> customerLeIds = new ArrayList<>();
		Set<String> cpeList = new HashSet<>();
		List<AppBWUtilizationBean> appBWUtilizationBeanList = new ArrayList<>();
		List<AppBWUtilizationBean> appforAllOrgs = new ArrayList<>();
		SdwanBandwidthUtilized sdwanBandwidthUtilized = new SdwanBandwidthUtilized();
		try {
			if (Objects.isNull(startDate))
				startDate = "30daysAgo";
			if (Objects.isNull(endDate))
				endDate = "today";
			List<SdwanEndpoints> analyticsEndpoints = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
			if(analyticsEndpoints == null || analyticsEndpoints.isEmpty()) {
				throw new TclCommonRuntimeException("Analytics endpoint not available");
			}
			Cookie cookie;
			HttpHeaders loginHeader = connectVersaAnalytics(request, analyticsEndpoints.get(0));
			cookie = getCookiesFromHeader(loginHeader);
			LOGGER.info("Cookie : {} ", Utils.convertObjectToJson(cookie));
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.COOKIE, cookie.getName() + "=" + cookie.getValue());
			getCustomerAndPartnerLeIds(customerLeIds, partnerLeIds, customerLeId);
			LOGGER.info("CustomerIds  " + customerLeIds + " CustomerId  " + customerId + " PartnerId  " + partnerId);
			if (!customerLeIds.isEmpty() || !partnerLeIds.isEmpty()) {
				List<String> sdwanServiceIds = new ArrayList<>();
				List<Integer> sdwanSysId = new ArrayList<>();
				List<Map<String, Object>> sdwanSiteDetails = new ArrayList<>();
				sdwanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSiteDetails(customerLeIds, partnerLeIds,
						productId, customerId, partnerId);
				sdwanSiteDetails.stream().forEach(sdwanSiteDetail -> {
					sdwanServiceIds.add((String) sdwanSiteDetail.get("serviceId"));
					sdwanSysId.add((Integer) sdwanSiteDetail.get("sysId"));
				});
				if (!sdwanServiceIds.isEmpty() && !sdwanSysId.isEmpty()) {
					List<Map<String, Object>> underlays = vwSiServiceInfoAllRepository
							.findUnderlayServiceIdbySdwanServiceId(sdwanServiceIds);
					underlays.stream().forEach(cpe -> cpeList.add((String) cpe.get("asset_name")));
					List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
							.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
									Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
											ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
					Set<String> orgNames = new HashSet<>();
					Set<String> siteToInstanceRegion = new HashSet<>();
					overLayServiceAttrs.stream().peek(attributes -> {
						if (ServiceInventoryConstants.SDWAN_ORGANISATION_NAME
								.equalsIgnoreCase(attributes.getAttributeName()))
							orgNames.add(attributes.getAttributeValue());
						else if (ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING
								.equalsIgnoreCase(attributes.getAttributeName()))
							siteToInstanceRegion.add(attributes.getAttributeValue());
					}).collect(Collectors.toSet());
					List<SdwanEndpoints> endPoints = sdwanEndpointsRepository.findByServerCodeIn(siteToInstanceRegion);
					String finalStartDate = startDate;
					String finalEndDate = endDate;
					List<SdwanWanInterface> cpeInterfaceDetailsList = new ArrayList<SdwanWanInterface>();
					List<Vni> configuredInterfaceBwList = new ArrayList<>();
					orgNames.forEach(org -> {
						LOGGER.info("orgName : {} ", org);
						appBWUtilizationBeanList.addAll(getAppByOrg(org, finalStartDate, finalEndDate, headers, analyticsEndpoints.get(0)));
						endPoints.forEach(endPoint -> {
							LOGGER.info("endPoints : {} ", endPoint);
							cpeList.forEach(eachCpe -> {
								LOGGER.info("Cpe : {} ", eachCpe);
								try {
									CpeInterfaceDetails cpeInterfaceDetails = new CpeInterfaceDetails();
									ConfiguredInterfaceBw configuredInterfaceBw = new ConfiguredInterfaceBw();
									cpeInterfaceDetails = getCpeInterfaceDetails(endPoint, eachCpe, org);
									if (Objects.nonNull(cpeInterfaceDetails)
											&& Objects.nonNull(cpeInterfaceDetails.getCollection()) && Objects.nonNull(
													cpeInterfaceDetails.getCollection().getSdwanWanInterfaces())) {
										cpeInterfaceDetailsList
												.addAll(cpeInterfaceDetails.getCollection().getSdwanWanInterfaces());
										configuredInterfaceBw = getConfiguredInterfaceBW(endPoint, eachCpe);
										LOGGER.info("getLinkUtilizationBandwidth::cpeInterfaceDetails {} ", cpeInterfaceDetails);
									}
									if (Objects.nonNull(configuredInterfaceBw)
											&& Objects.nonNull(configuredInterfaceBw.getInterfaces())
											&& Objects.nonNull(configuredInterfaceBw.getInterfaces().getVni()))
										configuredInterfaceBwList
												.addAll(configuredInterfaceBw.getInterfaces().getVni());

								} catch (TclCommonException e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
								}
							});
						});
					});
					sdwanBandwidthUtilized = getTotalBandwidthAcrossOrg(cpeInterfaceDetailsList,
							configuredInterfaceBwList);
					LOGGER.info("Total Bandwith for Org : {} ", sdwanBandwidthUtilized.getTotalBandwidth());
					AppLinkUtilizationBean appLinkUtilizationBean = new AppLinkUtilizationBean();
					cpeList.stream().forEach(eachCpe -> {
						List<AppBWUtilizationBean> bean = appBWUtilizationBeanList.stream()
								.filter(utilization -> utilization.getCpe().equals(eachCpe))
								.collect(Collectors.toList());
						appforAllOrgs.addAll(bean);
					});
					appforAllOrgs.stream().forEach(atr -> {
						appLinkUtilizationBean.setTotalBw(appLinkUtilizationBean.getTotalBw() + atr.getUtilizedBw());
					});
					LOGGER.info("Bandwith Utilized for Org : {} ", appLinkUtilizationBean.getTotalBw());
					if (Objects.nonNull(sdwanBandwidthUtilized.getTotalBandwidth())
							&& sdwanBandwidthUtilized.getTotalBandwidth() > 0D) {
						if (Objects.nonNull(appLinkUtilizationBean.getTotalBw()))
							sdwanBandwidthUtilized.setBandwidthUtilized(appLinkUtilizationBean.getTotalBw());
						sdwanBandwidthUtilized.setUtilizedPercentage((sdwanBandwidthUtilized.getBandwidthUtilized()
								/ sdwanBandwidthUtilized.getTotalBandwidth()) * 100);
						sdwanBandwidthUtilized.setFreePercentage(100 - sdwanBandwidthUtilized.getUtilizedPercentage());
						sdwanBandwidthUtilized.setUnit("Mbps");
					}
				}
			}
			HttpHeaders response=analyticsApiLogout(headers);
			LOGGER.info("Logout is status in getBandwidthUtilizedAndTotalBw method {} ", response);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return sdwanBandwidthUtilized;
	}

	/**
	 * To get total Bandwidth for org level with associated CPE.
	 *
	 * @param cpeInterfaceDetailsList
	 * @param configuredInterfaceBwList
	 * 
	 * @return
	 */
	/**
	 * @param cpeInterfaceDetailsList
	 * @param configuredInterfaceBwList
	 * @return
	 */
	public SdwanBandwidthUtilized getTotalBandwidthAcrossOrg(List<SdwanWanInterface> cpeInterfaceDetailsList,
			List<Vni> configuredInterfaceBwList) {
		LOGGER.info("Inside getTotalBandwidthAcrossOrg cpeInterfaceDetailsList {} and configureCPEdetails {} ",
				cpeInterfaceDetailsList, configuredInterfaceBwList);
		Map<String, String> linkAndInterfaceMapping = new HashMap<>();
		Map<String, List<Bandwidth>> interfaceAndBwMapping = new HashMap<>();
		SdwanBandwidthUtilized sdwanBandwidthUtilized = new SdwanBandwidthUtilized();
		Map<String, Bandwidth> intfAndTotalBw = new HashMap<>();
//		for (SdwanWanInterface e1 : cpeInterfaceDetailsList) {
//			linkAndInterfaceMapping.put(e1.getCircuitName(), e1.getIntfName());
//		}
//		for (Vni e1 : configuredInterfaceBwList) {
//			putValue(interfaceAndBwMapping, e1.getName(), e1.getBandwidth());
//		}
//		for (String key : linkAndInterfaceMapping.values()) {
//			LOGGER.info("Inside getTotalBandwidthAcrossOrg linkAndInterfaceMapping interface name {} ",key);
//			String[] intf = key.split(Pattern.quote("."));
//			List<Bandwidth> bwList = interfaceAndBwMapping.get(intf[0]);
//			Bandwidth bw = new Bandwidth();
//			bw.setDownlink(0);
//			bw.setUplink(0);
//			LOGGER.info("Inside getTotalBandwidthAcrossOrg bwList {} ",bwList);
//			bwList.stream().forEach(bean -> {
//				if (Objects.nonNull(bean.getUplink()) && Objects.nonNull(bean.getDownlink())) {
//					bw.setUplink(bw.getUplink() + bean.getUplink());
//					bw.setDownlink(bw.getDownlink() + bean.getDownlink());
//				}
//			});
//			intfAndTotalBw.put(intf[0], bw);
//		}
		cpeInterfaceDetailsList.stream().forEach(cpeInterfaceDetail->linkAndInterfaceMapping.put(cpeInterfaceDetail.getCircuitName(), cpeInterfaceDetail.getIntfName()));
		configuredInterfaceBwList.stream().forEach(cpeConfiguredBW->putValue(interfaceAndBwMapping, cpeConfiguredBW.getName(), cpeConfiguredBW.getBandwidth()));
		LOGGER.info("Inside getTotalBandwidthAcrossOrg linkAndInterfaceMapping {} ",linkAndInterfaceMapping);
		linkAndInterfaceMapping.values().forEach(value->{
			LOGGER.info("Inside getTotalBandwidthAcrossOrg linkAndInterfaceMapping interface name {} ",value);
			String[] intf = value.split(Pattern.quote("."));
			List<Bandwidth> bwList = interfaceAndBwMapping.get(intf[0]);
			Bandwidth bw = new Bandwidth();
			bw.setDownlink(0);
			bw.setUplink(0);
			LOGGER.info("Inside getTotalBandwidthAcrossOrg bwList {} ",bwList);
			if(Objects.nonNull(bwList)) {
				bwList.stream().forEach(bean -> {
					if (Objects.nonNull(bean.getUplink()) && Objects.nonNull(bean.getDownlink())) {
						bw.setUplink(bw.getUplink() + bean.getUplink());
						bw.setDownlink(bw.getDownlink() + bean.getDownlink());
					}
				});
			}
			intfAndTotalBw.put(intf[0], bw);
		});
		
		LOGGER.info("intferface And TotalBw {}", intfAndTotalBw);
		Integer totalBandwidth = 0;
		for (Bandwidth bw : intfAndTotalBw.values()) {
			totalBandwidth += (bw.getUplink() + bw.getDownlink());
		}
		sdwanBandwidthUtilized.setTotalBandwidth((double) totalBandwidth / 1000);
		return sdwanBandwidthUtilized;
	}
	/**
	 * To get Bandwidth consumption of link by application wise.
	 *
	 * @param cpeInterfaceDetailsList
	 * @param configuredInterfaceBwList
	 * 
	 * @return
	 */
	public List<BandwidthUtilizationOfApp> getLinkUtilizationBandwidth(String startDate, String endDate,
			String siteName, String appName, HttpServletRequest request) {
		Map<String, Object> sdWanSiteDetails = new HashMap<>();
		ViewSiServiceInfoAllBean vwSiInfoOverlay = new ViewSiServiceInfoAllBean();
		List<VwServiceAssetAttributeBean> vwSiInfoUnderlay = new ArrayList<>();
		List<AppBWUtilizationBean> appBWUtilizationBeanList = new ArrayList<>();
		List<AppBWUtilizationBean> appforAllOrgs = new ArrayList<>();
		List<SdwanWanInterface> cpeInterfaceDetailsList = new ArrayList<>();
		List<Vni> configuredInterfaceBwList = new ArrayList<>();
		Map<String, String> linkAndInterfaceMapping = new HashMap<>();
		Map<String, Bandwidth> interfaceAndBwMapping = new HashMap<>();
		List<OtherAppBWUtilization> bwAppList = new ArrayList<>();
		List<BandwidthUtilizationOfApp> bwAppfinalResponse = new ArrayList<>();
		try {
			if (Objects.isNull(startDate))
				startDate = "30daysAgo";
			if (Objects.isNull(endDate))
				endDate = "today";
			if (siteName == null || appName == null) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
			}
			sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSite(siteName);
			if (sdWanSiteDetails != null && !sdWanSiteDetails.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				vwSiInfoOverlay = mapper.convertValue(sdWanSiteDetails, ViewSiServiceInfoAllBean.class);
			}
			LOGGER.info("Inside  getLinkUtilizationBandwidth to fetch underlay product for IZOSDWAN Serviceids {}",
					vwSiInfoOverlay.getServiceId());
			List<Map<String, Object>> underlaySiteDetails = vwSiServiceInfoAllRepository
					.findByUndeyLayByIzoSdwanServiceIdsIn(Arrays.asList(vwSiInfoOverlay.getServiceId()));
			if (underlaySiteDetails != null && !underlaySiteDetails.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				underlaySiteDetails.stream().forEach(underlaySiteDetail -> {
					vwSiInfoUnderlay.add(mapper.convertValue(underlaySiteDetail, VwServiceAssetAttributeBean.class));
				});
			}
			Set<String> cpeLists = new HashSet<>();
			for (VwServiceAssetAttributeBean bean : vwSiInfoUnderlay) {
				cpeLists.add(bean.getAssetName());
			}
			LOGGER.info("Inside  getLinkUtilizationBandwidth cpe {}", cpeLists);
			List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
					.findBySiServiceDetail_IdInAndAttributeNameIn(Arrays.asList(vwSiInfoOverlay.getSysId()),
							Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
									ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));
			Set<String> orgNames = new HashSet<>();
			Set<String> siteToInstanceRegion = new HashSet<>();
			overLayServiceAttrs.stream().peek(attributes -> {
				if (ServiceInventoryConstants.SDWAN_ORGANISATION_NAME.equalsIgnoreCase(attributes.getAttributeName()))
					orgNames.add(attributes.getAttributeValue());
				else if (ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING
						.equalsIgnoreCase(attributes.getAttributeName()))
					siteToInstanceRegion.add(attributes.getAttributeValue());
			}).collect(Collectors.toSet());
			String finalStartDate = startDate;
			String finalEndDate = endDate;
			List<SdwanEndpoints> analyticsEndpoints = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
			if(analyticsEndpoints == null || analyticsEndpoints.isEmpty()) {
				throw new TclCommonRuntimeException("Analytics endpoint not available");
			}
			Cookie cooki;
			HttpHeaders loginHeader = connectVersaAnalytics(request, analyticsEndpoints.get(0));
			cooki = getCookiesFromHeader(loginHeader);
			LOGGER.info("Cookie : {} ", Utils.convertObjectToJson(cooki));
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.COOKIE, cooki.getName() + "=" + cooki.getValue());
			List<SdwanEndpoints> endPoints = sdwanEndpointsRepository.findByServerCodeIn(siteToInstanceRegion);
			orgNames.forEach(orgName -> {
				LOGGER.info("Inside getLinkUtilizationBandwidth for orgname {} ", orgName);
				appBWUtilizationBeanList.addAll(getAppByOrg(orgName, finalStartDate, finalEndDate, headers, analyticsEndpoints.get(0)));
				endPoints.forEach(endPoint -> {
					LOGGER.info("endPoints : {} ", endPoint);
					cpeLists.forEach(eachCpe -> {
						LOGGER.info("Cpe : {} ", eachCpe);
						try {
							CpeInterfaceDetails cpeInterfaceDetails = new CpeInterfaceDetails();
							ConfiguredInterfaceBw configuredInterfaceBw = new ConfiguredInterfaceBw();
							cpeInterfaceDetails = getCpeInterfaceDetails(endPoint, eachCpe, orgName);
							LOGGER.info("getLinkUtilizationBandwidth::cpeInterfaceDetails {} ", cpeInterfaceDetails);
							if (Objects.nonNull(cpeInterfaceDetails)
									&& Objects.nonNull(cpeInterfaceDetails.getCollection())
									&& Objects.nonNull(cpeInterfaceDetails.getCollection().getSdwanWanInterfaces())) {
								cpeInterfaceDetailsList
										.addAll(cpeInterfaceDetails.getCollection().getSdwanWanInterfaces());
								configuredInterfaceBw = getConfiguredInterfaceBW(endPoint, eachCpe);
								LOGGER.info("getLinkUtilizationBandwidth::configuredInterfaceBw {} ", configuredInterfaceBw);
							}
							if (Objects.nonNull(configuredInterfaceBw)
									&& Objects.nonNull(configuredInterfaceBw.getInterfaces())
									&& Objects.nonNull(configuredInterfaceBw.getInterfaces().getVni()))
								configuredInterfaceBwList.addAll(configuredInterfaceBw.getInterfaces().getVni());

						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
						}
					});
				});
			});
			cpeLists.stream().forEach(eachCpe -> {
				List<AppBWUtilizationBean> bean = appBWUtilizationBeanList.stream()
						.filter(utilization -> utilization.getCpe().equals(eachCpe)).collect(Collectors.toList());
				appforAllOrgs.addAll(bean);
			});
			LOGGER.info("App utilization for associated CPE {} ", appforAllOrgs);
			List<BandwidthUtilizationOfApp> bandwidthUtilizationOfAppList = new ArrayList<>();
			Set<String> appAndLink = new HashSet<String>();
			appforAllOrgs.stream().forEach(bean -> {
				if (!appAndLink.contains(bean.getAppName() + "-" + bean.getLinkName())) {
					BandwidthUtilizationOfApp bandwidthUtilizationOfApp = new BandwidthUtilizationOfApp();
					bandwidthUtilizationOfApp.setAppName(bean.getAppName());
					bandwidthUtilizationOfApp.setLinkName(bean.getLinkName());
					bandwidthUtilizationOfApp.setAppDownlinkUtilizedBw((bean.getDownlinkBwUtil()));
					bandwidthUtilizationOfApp.setAppUplinkUtilizedBw(bean.getUplinkBwUtil());
					bandwidthUtilizationOfApp.setUnit("Mbps");
					bandwidthUtilizationOfAppList.add(bandwidthUtilizationOfApp);
					appAndLink.add(bean.getAppName() + "-" + bean.getLinkName());
				} else {
					bandwidthUtilizationOfAppList.stream().filter(
							x -> x.getAppName().equals(bean.getAppName()) && x.getLinkName().equals(bean.getLinkName()))
							.peek(app -> {
								app.setAppDownlinkUtilizedBw(app.getAppDownlinkUtilizedBw() + bean.getDownlinkBwUtil());
								app.setAppUplinkUtilizedBw(app.getAppUplinkUtilizedBw() + bean.getUplinkBwUtil());
							}).anyMatch(x -> x.getAppName().equals(bean.getAppName())
									&& x.getLinkName().equals(bean.getLinkName()));
				}
			});
			getTotalBandwidthForLink(cpeInterfaceDetailsList, configuredInterfaceBwList, linkAndInterfaceMapping,
					interfaceAndBwMapping);
			Map<String, Bandwidth> linkAndTotalBW = new HashMap<>();
			bandwidthUtilizationOfAppList.stream().forEach(eachLink -> {
				String allInterface = linkAndInterfaceMapping.get(eachLink.getLinkName());
				Bandwidth bw = new Bandwidth();
				if (Objects.nonNull(allInterface)) {
					String[] interfaceNameSplit = allInterface.split(Pattern.quote("."));
					bw = interfaceAndBwMapping.get(interfaceNameSplit[0]);
					if (Objects.nonNull(bw) && Objects.nonNull(bw.getUplink()) && Objects.nonNull(bw.getDownlink())) {
						Double uplinkTotal = (double) bw.getUplink();
						Double downlinkTotal = (double) bw.getDownlink();
						if (uplinkTotal > 0)
							eachLink.setAppUplinkUtilizedPer((eachLink.getAppUplinkUtilizedBw() / uplinkTotal) * 100);
						else
							eachLink.setAppUplinkUtilizedPer(0D);
						if (downlinkTotal > 0)
							eachLink.setAppDownlinkUtilizedPer(
									(eachLink.getAppDownlinkUtilizedBw() / downlinkTotal) * 100);
						else
							eachLink.setAppDownlinkUtilizedPer(0D);
					}
				} else {
					eachLink.setAppUplinkUtilizedPer(0D);
					eachLink.setAppDownlinkUtilizedPer(0D);
				}
				linkAndTotalBW.put(eachLink.getLinkName(), bw);
			});
			List<BandwidthUtilizationOfApp> linkBandwidthUtilizationByApp = bandwidthUtilizationOfAppList.stream()
					.filter(selectedApp -> selectedApp.getAppName().equalsIgnoreCase(appName))
					.collect(Collectors.toList());
			bandwidthUtilizationOfAppList.removeIf(x -> x.getAppName().equals(appName));
			Set<String> recurringlinks = new HashSet<>();
			bandwidthUtilizationOfAppList.stream().forEach(others -> {
				Bandwidth bw = linkAndTotalBW.get(others.getLinkName());
				OtherAppBWUtilization otherAppBWUtilization = new OtherAppBWUtilization();
				if (!recurringlinks.contains(others.getLinkName())) {
				    LOGGER.info("AppBWUtilization inside  not recurringlinks {} ",others);
					otherAppBWUtilization.setLinkName(others.getLinkName());
					otherAppBWUtilization.setAppName(others.getAppName());
					otherAppBWUtilization.setOtherAppUplinkUtilizedBw(others.getAppUplinkUtilizedBw());
					otherAppBWUtilization.setOtherAppDownlinkUtilizedBw(others.getAppDownlinkUtilizedBw());
					if (Objects.nonNull(bw) && Objects.nonNull(bw.getUplink()) && Objects.nonNull(bw.getDownlink())) {
						if (bw.getUplink() > 0)
							otherAppBWUtilization.setOtherAppUplinkUtilizedPer(
									(others.getOtherAppUplinkUtilizedBw() / bw.getUplink()) * 100);
						else
							otherAppBWUtilization.setOtherAppUplinkUtilizedPer(0D);
						if (bw.getDownlink() > 0)
							others.setOtherAppDownlinkUtilizedPer(
									(others.getOtherAppDownlinkUtilizedBw() / bw.getDownlink()) * 100);
						else
							otherAppBWUtilization.setOtherAppDownlinkUtilizedPer(0D);
					}
					bwAppList.add(otherAppBWUtilization);
					recurringlinks.add(others.getLinkName());
				} else {
				    LOGGER.info("AppBWUtilization inside  recurringlinks {} ",others);
					bwAppList.stream().filter(bean -> bean.getLinkName().equals(others.getLinkName())).peek(atr -> {
						atr.setAppName(atr.getAppName() + "," + others.getAppName());
						atr.setOtherAppUplinkUtilizedBw(
								atr.getOtherAppUplinkUtilizedBw() + others.getAppUplinkUtilizedBw());
						atr.setOtherAppDownlinkUtilizedBw(
								atr.getOtherAppDownlinkUtilizedBw() + others.getAppDownlinkUtilizedBw());
						if (Objects.nonNull(bw) && Objects.nonNull(bw.getUplink())
								&& Objects.nonNull(bw.getDownlink())) {
							if (bw.getUplink() > 0)
								atr.setOtherAppUplinkUtilizedPer(
										(atr.getOtherAppUplinkUtilizedBw() / bw.getUplink()) * 100);
							else
								atr.setOtherAppUplinkUtilizedPer(0D);
							if (bw.getDownlink() > 0)
								atr.setOtherAppDownlinkUtilizedPer(
										(atr.getOtherAppDownlinkUtilizedBw() / bw.getDownlink()) * 100);
							else
								atr.setOtherAppDownlinkUtilizedPer(0D);
						}
					}).anyMatch(bean -> bean.getLinkName().equals(others.getLinkName()));
				}
			});
			bwAppList.stream().forEach(atr -> {
				linkBandwidthUtilizationByApp.stream()
						.filter(eachIntf -> eachIntf.getLinkName().equalsIgnoreCase(atr.getLinkName())).peek(bean -> {
							Bandwidth bw = new Bandwidth();
							bw = linkAndTotalBW.get(atr.getLinkName());
							bean.setOtherAppUplinkUtilizedBw(atr.getOtherAppUplinkUtilizedBw());
							bean.setOtherAppDownlinkUtilizedBw(atr.getOtherAppDownlinkUtilizedBw());
							bean.setOtherAppUplinkUtilizedPer(atr.getOtherAppUplinkUtilizedPer());
							bean.setOtherAppDownlinkUtilizedPer(atr.getOtherAppDownlinkUtilizedPer());
							if (Objects.nonNull(bw) && Objects.nonNull(bw.getUplink())
									&& Objects.nonNull(bw.getDownlink())) {
								if (bw.getUplink() > 0)
									bean.setAvailBwForUplink(bw.getUplink()
											- (atr.getOtherAppUplinkUtilizedBw() + bean.getAppUplinkUtilizedBw()));
								else
									bean.setAvailBwForUplink(0D);
								if (bw.getDownlink() > 0)
									bean.setAvailBwForDownlink(bw.getDownlink()
											- (atr.getOtherAppDownlinkUtilizedBw() + bean.getAppDownlinkUtilizedBw()));
								else
									bean.setAvailBwForDownlink(0D);
								if(bean.getAvailBwForUplink()>0 && bw.getUplink() > 0)
									bean.setAvailUplinkPer((bean.getAvailBwForUplink() / bw.getUplink()) * 100);
								else
									bean.setAvailUplinkPer(0D);
								if(bean.getAvailBwForDownlink()>0 && bw.getDownlink() > 0)
									bean.setAvailDownlinkPer((bean.getAvailBwForDownlink() / bw.getDownlink()) * 100);
								else
									bean.setAvailDownlinkPer(0D);
							}
							bwAppfinalResponse.add(bean);
						}).anyMatch(eachIntf -> eachIntf.getLinkName().equals(atr.getLinkName()));
			});
			LOGGER.info("final response for App utilization by link {} ", bwAppfinalResponse);
			HttpHeaders response=analyticsApiLogout(headers);
			LOGGER.info("Logout is status in getLinkUtilizationBandwidth API {} ", response);
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		bwAppfinalResponse
				.sort((app1, app2) -> app2.getAppUplinkUtilizedPer().compareTo(app1.getAppUplinkUtilizedPer()));
		if (bwAppfinalResponse.size() > 5) {
			bwAppfinalResponse.subList(0, 4);
		}
		return bwAppfinalResponse;
	}

	/**
	 * To process total BW data for link
	 *
	 * @param cpeInterfaceDetailsList
	 * @param configuredInterfaceBwList
	 * 
	 * 
	 */
	public void getTotalBandwidthForLink(List<SdwanWanInterface> cpeInterfaceDetailsList,
			List<Vni> configuredInterfaceBwList, Map<String, String> linkAndInterfaceMapping,
			Map<String, Bandwidth> interfaceAndBwMapping) {
		for (SdwanWanInterface cpeInterfaceDetails : cpeInterfaceDetailsList) {
			if (Objects.nonNull(cpeInterfaceDetails))
				linkAndInterfaceMapping.put(cpeInterfaceDetails.getCircuitName(), cpeInterfaceDetails.getIntfName());
		}
		LOGGER.info("IN getTotalBandwidthForLink linkAndInterfaceMapping {}", linkAndInterfaceMapping);
		Set<String> recurringlinks = new HashSet<>();
		List<Vni> totalBwForLinks = new ArrayList<>();
		if (Objects.nonNull(configuredInterfaceBwList) && !configuredInterfaceBwList.isEmpty()) {
			configuredInterfaceBwList.stream().forEach(atr -> {
			    LOGGER.info("IN getTotalBandwidthForLink if configuredInterfaceBwList {} and BW {} ", atr.getName(), atr.getBandwidth());
				Vni vni = new Vni();
				Bandwidth bw = new Bandwidth();
				if (!recurringlinks.contains(atr.getName())) {
				    if(atr.getBandwidth()== null) {
							bw.setUplink(0);
							bw.setDownlink(0);
						} else {
							bw.setUplink(atr.getBandwidth().getUplink() == null ? 0 : (atr.getBandwidth().getUplink()/1000));
							bw.setDownlink(atr.getBandwidth().getDownlink() == null ? 0 : (atr.getBandwidth().getDownlink()/1000));
						}
					vni.setName(atr.getName());
					vni.setBandwidth(bw);
					totalBwForLinks.add(vni);
					recurringlinks.add(atr.getName());
				} else {
				    LOGGER.info("IN getTotalBandwidthForLink else configuredInterfaceBwList {} and BW {}", atr.getName(), atr.getBandwidth());
					totalBwForLinks.stream().filter(link -> link.getName().equals(atr.getName())).peek(bean -> {
						Bandwidth bw1 = new Bandwidth();
						Integer uplink = 0;
						Integer downlink = 0;
						if(atr.getBandwidth() != null) {
						    uplink = atr.getBandwidth().getUplink() == null ? 0
								: (atr.getBandwidth().getUplink() / 1000);
						    downlink = atr.getBandwidth().getDownlink() == null ? 0
								: (atr.getBandwidth().getDownlink() / 1000);
						}
						
						bw1.setUplink(bean.getBandwidth().getUplink() + uplink);
						bw1.setDownlink(bean.getBandwidth().getDownlink() + downlink);
						bean.setBandwidth(bw1);
					}).anyMatch(link -> link.getName().equalsIgnoreCase(atr.getName()));
				}
			});
		}
		if (Objects.nonNull(totalBwForLinks) && !totalBwForLinks.isEmpty()) {
			totalBwForLinks.stream().forEach(atr -> {
				interfaceAndBwMapping.put(atr.getName(), atr.getBandwidth());
			});
			LOGGER.info("IN getTotalBandwidthForLink interfaceAndBwMapping {}", interfaceAndBwMapping);
		}
	}

	/**
	 * For Map<String,List<Bandwidth>> addition of values to same key
	 *
	 * @param map
	 * @param key
	 * @param value
	 * 
	 */
	public static void putValue(Map<String, List<Bandwidth>> map, String key, Bandwidth value) {
		if (map.get(key) == null) {
			List<Bandwidth> list = new ArrayList<>();
			list.add(value);
			map.put(key, list);
		} else {
			map.get(key).add(value);
		}
	}
	/**
	 * Logout method for versa analytics
	 *
	 * @param headers
	 * @return
	 */
	public HttpHeaders analyticsApiLogout(HttpHeaders headers) {
		List<SdwanEndpoints> endpoint = sdwanEndpointsRepository.findByServerCodeAndIsDirector(ServiceInventoryConstants.VERSA_ANALYTICS, (byte) 0);
		HttpHeaders restResponse = null;
		if(Objects.nonNull(endpoint) && !endpoint.isEmpty()) {
			String url=endpoint.get(0).getServerIp() +versaAnalyticsLogoutUrl;
			LOGGER.info("Inside analyticsApiLogout Url and headers {} {}  ", url, headers.toSingleValueMap());
			restResponse = restClientService.postAndReturnHeadersWithProxy(url, headers.toSingleValueMap() ,null);
//			restResponse = restClientService.get(url, headers.toSingleValueMap(), false);
			LOGGER.info("IN analyticsApiLogout restresponse {} ", restResponse);
			if(restResponse == null)
				LOGGER.info("Error occured in versa logout {}", restResponse);
		}
		return restResponse;
	}
	/**
	 * Edit Template Details service
	 *
	 * @param templateDetail
	 * @return
	 * @throws TclCommonException
	 */
	public String editTemplateDetails(SdwanTemplateDetailBean templateDetail) throws TclCommonException {

		LOGGER.info("In editTemplateDetails method - request body param : {}", templateDetail);
		Set<String> regions = new HashSet<>();
		String finalResponse = "";
		List<Integer> templateId = new ArrayList<>();
		List<Rule> trafficSteeringRuleArrangedList = new ArrayList<>();
		List<AppQosPolicy> appQosPolicyRuleArrangedList = new ArrayList<>();
		List<AccessPolicy> appFireWallPolicyArrangedList = new ArrayList<>();
		
		List<String> orgName = new ArrayList<>();
		List<String> trafficStearingPolicyName = new ArrayList<>();
		List<String> qosPolicyName = new ArrayList<>();
		List<String> firewallPolicyName = new ArrayList<>();
				
		List<Rules_> trafficSteeringList = new ArrayList<>();
		List<AppQosPolicyRules> appQosPolicyList = new ArrayList<>();
		List<AccessFireWallPolicyRules> fireWallPolicyList = new ArrayList<>(); 
		
		if (Objects.nonNull(templateDetail) && Objects.nonNull(templateDetail.getSdwanPolicies())) {
			String templateName = templateDetail.getTemplateName();
			templateId = templateDetail.getTemplateIds();
			templateDetail.getSdwanPolicies().stream().forEach(bean -> {
				if (Objects.nonNull(bean.getDirectorRegion()) && Objects.nonNull(bean.getOrganisationName())
						&& Objects.nonNull(bean.getPolicyType())) {
					regions.add(bean.getDirectorRegion());
					orgName.add(bean.getOrganisationName());
					if (bean.getPolicyType().equals(ServiceInventoryConstants.TRAFFIC_STEERING))
						trafficStearingPolicyName.add(bean.getPolicyName());
					else if (bean.getPolicyType().equals(ServiceInventoryConstants.QOS))
						qosPolicyName.add(bean.getPolicyName());
					//get the firewall policy name from templateDetail
					else if (bean.getPolicyType().equals(ServiceInventoryConstants.FIREWALL))
						firewallPolicyName.add(bean.getPolicyName());
				}
			});
			List<SIServiceAttribute> siServiceAttribute = siServiceAttributeRepository
					.findByIdInAndAttributeNameInQuery(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME, templateId);
			siServiceAttribute.stream().forEach(si -> {
				if (templateDetail.getTemplateAlias() != null)
					si.setAttributeAltValueLabel(templateDetail.getTemplateAlias());
			});
			LOGGER.info("In editTemplateDetails method - templateName : {}", templateName);
			LOGGER.info("In editTemplateDetails method - regions : {}", regions);
			LOGGER.info("In editTemplateDetails method - orgName : {}", orgName);
			LOGGER.info("In editTemplateDetails method - trafficStearingPolicyName rearrangement: {}",
					trafficStearingPolicyName);
			LOGGER.info("In editTemplateDetails method - qosPolicyName rearrangement : {}", 
					qosPolicyName);
			LOGGER.info("In editTemplateDetails method - firewallPolicyName rearrangement : {}", firewallPolicyName);
			
			List<SdwanEndpoints> sdwanEndpointsList = new ArrayList<>();
			regions.stream().forEach(atr -> {
				try {
					SdwanEndpoints endpoint = sdwanEndpointsRepository.findByServerCode(atr);
					sdwanEndpointsList.add(endpoint);
					Rules_ trafficSteering = getTrafficSteeringRuleForTemplateOverAll(templateName, orgName.get(0),
							endpoint);
					AppQosPolicyRules appQosPolicy = getQosPoliciesForTemplateOverAll(templateName, orgName.get(0)
							,endpoint);
					/* get the fire wall access policy names*/
					String accPolyName = getFireWallPolicyName(templateName,orgName.get(0),sdwanEndpointsList);
					AccessFireWallPolicyRules accessfireWallPolicy = getFireWallPoliciesForTemplateOverAll(templateName, orgName.get(0),accPolyName
							,endpoint);
					
					if (Objects.nonNull(trafficSteering))
						trafficSteeringList.add(trafficSteering);
					if (Objects.nonNull(appQosPolicy))
						appQosPolicyList.add(appQosPolicy);
					if(Objects.nonNull(accessfireWallPolicy))
						fireWallPolicyList.add(accessfireWallPolicy);
					
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
			LOGGER.info("In editTemplateDetails method - trafficSteeringList : {}", trafficSteeringList);
			LOGGER.info("In editTemplateDetails method - appQosPolicyList : {}", appQosPolicyList);
			LOGGER.info("In editTemplateDetails method - fireWallPolicyList : {}", fireWallPolicyList);
			
			if (Objects.nonNull(trafficSteeringList) && !trafficSteeringList.isEmpty()) {
				trafficSteeringList.stream().forEach(atr -> {
					for (String policy : trafficStearingPolicyName) {
						trafficSteeringRuleArrangedList
								.add((atr.getRule().stream().filter(x -> x.getName().equals(policy)).findAny().get()));
					}
				});
			}
			if (Objects.nonNull(appQosPolicyList) && !appQosPolicyList.isEmpty()) {
				appQosPolicyList.stream().forEach(atr -> {
					for (String policy : qosPolicyName) {
						appQosPolicyRuleArrangedList.add((atr.getAppQosPolicy().stream()
								.filter(x -> x.getName().equals(policy)).findAny().get()));
					}
				});
			}
			
			/*  for fireWall policy rearrangement (Versa) */ 
			if (Objects.nonNull(fireWallPolicyList) && !fireWallPolicyList.isEmpty()) {
				fireWallPolicyList.stream().forEach(atr -> {
					for (String policy : firewallPolicyName) {
						appFireWallPolicyArrangedList.add((atr.getAccessPolicy().stream()
								.filter(x -> x.getName().equals(policy)).findAny().get()));
					}
				});
			}
			
			Rules_ rules = new Rules_();
			TrafficSteeringPolicy trafficSteeringPolicy = new TrafficSteeringPolicy();
			AppQosPolicyRules appQosPolicy = new AppQosPolicyRules();  
			AccessFireWallPolicyRules accessfireWallPolicy = new AccessFireWallPolicyRules();
			
			AppQosRules appQosRules = new AppQosRules();
			FireWallRules fireWallRules = new FireWallRules();
			
			if (Objects.nonNull(trafficSteeringRuleArrangedList) && !trafficSteeringRuleArrangedList.isEmpty())
				rules.setRule(trafficSteeringRuleArrangedList);
			if (Objects.nonNull(rules))
				trafficSteeringPolicy.setRules(rules);
			if (Objects.nonNull(appQosPolicyRuleArrangedList) && !appQosPolicyRuleArrangedList.isEmpty())
				appQosPolicy.setAppQosPolicy(appQosPolicyRuleArrangedList);
			if (Objects.nonNull(rules))
				appQosRules.setAppQosPolicy(appQosPolicy);
			/* set the fire wall rules*/
			if (Objects.nonNull(appFireWallPolicyArrangedList) && !appFireWallPolicyArrangedList.isEmpty())
				accessfireWallPolicy.setAccessPolicy(appFireWallPolicyArrangedList);
			if (Objects.nonNull(rules))
				fireWallRules.setAccessPolicy(accessfireWallPolicy);
			
			
			LOGGER.info("In editTemplateDetails method - trafficSteeringPolicy : {}",
					Utils.convertObjectToJson(trafficSteeringPolicy));
			LOGGER.info("In editTemplateDetails method - appQosRules : {}", 
					Utils.convertObjectToJson(appQosRules));
			LOGGER.info("In editTemplateDetails method - fireWallRules : {}", 
					Utils.convertObjectToJson(fireWallRules));
			try {
				String responseTraffic = saveTrafficSteeringByOrder(templateName, orgName.get(0), sdwanEndpointsList,
						trafficSteeringPolicy);
				String responseQos = saveQosByOrder(templateName, orgName.get(0), sdwanEndpointsList, appQosRules);
				/* To save the fire wall policies by order */
				/* get the fire wall access policy name*/
				String accPolyName = getFireWallPolicyName(templateName,orgName.get(0),sdwanEndpointsList);
				String responseFireWall = saveFireWallByOrder(templateName, orgName.get(0),accPolyName, sdwanEndpointsList, fireWallRules);
				if (responseTraffic.equalsIgnoreCase(Constants.SUCCESS)
						&& responseQos.equalsIgnoreCase(Constants.SUCCESS) 
						&& responseFireWall.equalsIgnoreCase(Constants.SUCCESS)) { 
					siServiceAttributeRepository.saveAll(siServiceAttribute);
					finalResponse = Constants.SUCCESS;
				}
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
			}
		}
		return finalResponse;
	}

	/**
	 * Get traffic steering rules for a template
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	public Rules_ getTrafficSteeringRuleForTemplateOverAll(String templateName, String organisationName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String tsUrl = versaAnalyticsTrafficGetUrl + "?format=json&deep=true&offset=0&limit=10000";
		tsUrl = tsUrl.replaceAll("DYNAMICORGNAME", organisationName);
		tsUrl = tsUrl.replaceAll("DYNAMICTEMPLATENAME", templateName);
		tsUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + tsUrl;
		LOGGER.info("Get traffic steering policy URL : {}", tsUrl);
		RestResponse sdwanPolicyResponse = restClientService.getWithBasicAuthentication(tsUrl,
				new HashMap<String, String>(), sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (Objects.nonNull(sdwanPolicyResponse) && Objects.nonNull(sdwanPolicyResponse.getData())) {
			Rules_ trafficSteeringPl = Utils.convertJsonToObject(sdwanPolicyResponse.getData(), Rules_.class);
			return trafficSteeringPl;
		} else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
	}

	/**
	 * Get Qos Policy from versa based on template and organisation
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */
	public AppQosPolicyRules getQosPoliciesForTemplateOverAll(String templateName, String organisationName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String qosUrl = versaQosGetUrl + "?format=json&deep=true&offset=0&limit=1000";
		qosUrl = qosUrl.replace("DYNAMICTEMPLATENAME", templateName);
		qosUrl = qosUrl.replace("DYNAMICORGNAME", organisationName);
		qosUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + qosUrl;
		LOGGER.info("getQosPoliciesForTemplateOverAll Qos policy url : {}", qosUrl);
		RestResponse qosPolicyResponse = restClientService.getWithBasicAuthentication(qosUrl,
				new HashMap<String, String>(), sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (Objects.nonNull(qosPolicyResponse) && Objects.nonNull(qosPolicyResponse.getData())) {
			AppQosPolicyRules qosPolicy = Utils.convertJsonToObject(qosPolicyResponse.getData(),
					AppQosPolicyRules.class);
			return qosPolicy;
		} else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
	}
	
	/**
	 * get Access policy name for fire wall 
	 * @author Sobhan Ganta 
	 *  
	 */ 
	
	public String getFireWallPolicyName(String templateName,String organisationName,List<SdwanEndpoints> sdwanEndpoints) throws TclCommonException {
		/* get the  fire wall access policy name*/
		String firewallGetAccessPolicyNameUrl = versaApiFirewallForAccessPolicyName;
		firewallGetAccessPolicyNameUrl = firewallGetAccessPolicyNameUrl.replace("DYNAMICTEMPLATENAME", templateName);
		firewallGetAccessPolicyNameUrl = firewallGetAccessPolicyNameUrl.replace("DYNAMICORGNAME", organisationName);
		String accPlyName=null;
		for (SdwanEndpoints endPoints : sdwanEndpoints) {
			firewallGetAccessPolicyNameUrl = endPoints.getServerIp() + ":" + endPoints.getServerPort() + firewallGetAccessPolicyNameUrl;
			LOGGER.info("getFireWallPoliciesForTemplateOverAll firewall policy url : {}", firewallGetAccessPolicyNameUrl );
			GetFirewallRuleName ListfirewallPoliName = new GetFirewallRuleName(); 
			RestResponse fireWallPolicyResponseforAccessPolicyName = restClientService.getWithBasicAuthentication(firewallGetAccessPolicyNameUrl,
					new HashMap<String, String>(), endPoints.getServerUsername(), endPoints.getServerPassword());
			
			if (Objects.nonNull(fireWallPolicyResponseforAccessPolicyName) && Objects.nonNull(fireWallPolicyResponseforAccessPolicyName.getData())) {
				 ListfirewallPoliName = Utils.convertJsonToObject(fireWallPolicyResponseforAccessPolicyName.getData(),GetFirewallRuleName.class);
				 LOGGER.info("GetFirewallRuleName respone :{} ",ListfirewallPoliName);
				 List<AccessPolicyGroup> listAcceGroupname = new ArrayList<>();
				 listAcceGroupname =  ListfirewallPoliName.getAccessPolicyGroup();
				 accPlyName = listAcceGroupname.get(0).getName();
				 LOGGER.info("Access Policy Name : " + accPlyName);
			}
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
		return accPlyName;
	}
	
	/**
	 * Get fire wall Policy from versa based on template and organization
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 * @author SGanta
	 */
	public AccessFireWallPolicyRules getFireWallPoliciesForTemplateOverAll(String templateName, String organisationName,String accPolyName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String firewallUrl = versaApiFirewallForPolicyRule; 
		firewallUrl = firewallUrl.replace("DYNAMICTEMPLATENAME", templateName);
		firewallUrl = firewallUrl.replace("DYNAMICORGNAME", organisationName);
		firewallUrl = firewallUrl.replace("POLICYNAME", accPolyName);
		firewallUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + firewallUrl;
		LOGGER.info("getFireWallPoliciesForTemplateOverAll firewall policy url : {}", firewallUrl );
		RestResponse fireWallPolicyResponse = restClientService.getWithBasicAuthentication(firewallUrl,
				new HashMap<String, String>(), sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (Objects.nonNull(fireWallPolicyResponse) && Objects.nonNull(fireWallPolicyResponse.getData())) {
			AccessFireWallPolicyRules firewallPolicy = Utils.convertJsonToObject(fireWallPolicyResponse.getData(),AccessFireWallPolicyRules.class);
			return firewallPolicy;
		} else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
	}
	
	/**
	 * To save Traffic sterring policy rearrangement
	 *
	 * @param templateName
	 * @param organisationName
	 * @param sdwanEndpoints
	 * @param trafficSteeringPolicy
	 * @return
	 * @throws TclCommonException
	 */
	public String saveTrafficSteeringByOrder(String templateName, String organisationName,
			List<SdwanEndpoints> sdwanEndpoints, TrafficSteeringPolicy trafficSteeringPolicy)
			throws TclCommonException {
		String trafficResponse = "";
		String trafficUrl = versaTrafficSteeringPutUrl;
		trafficUrl = trafficUrl.replace("DYNAMICTEMPLATENAME", templateName);
		trafficUrl = trafficUrl.replace("DYNAMICORGNAME", organisationName);
		for (SdwanEndpoints endPoints : sdwanEndpoints) {
			trafficUrl = endPoints.getServerIp() + ":" + endPoints.getServerPort() + trafficUrl;
			LOGGER.info("saveTrafficSteeringByOrder url : {}", trafficUrl);
			RestResponse response = restClientService.putWithBasicAuthentication(trafficUrl,
					Utils.convertObjectToJson(trafficSteeringPolicy), new HashMap<String, String>(),
					endPoints.getServerUsername(), endPoints.getServerPassword());
			LOGGER.info("saveTrafficSteeringByOrder response : {}", response);
			if (Objects.nonNull(response) && response.getStatus() == Status.SUCCESS)
				trafficResponse = Constants.SUCCESS;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
		return trafficResponse;
	}
	/**
	 * To save Qos policy rearrangement
	 *
	 * @param templateName
	 * @param organisationName
	 * @param sdwanEndpoints
	 * @param trafficSteeringPolicy
	 * @return
	 * @throws TclCommonException
	 */
	public String saveQosByOrder(String templateName, String organisationName, List<SdwanEndpoints> sdwanEndpoints,
			AppQosRules appQosPolicyRules) throws TclCommonException {
		String qosResponse = "";
		String qosUrl = versaSaveQosUrl;
		qosUrl = qosUrl.replace("DYNAMICTEMPLATENAME", templateName);
		qosUrl = qosUrl.replace("DYNAMICORGNAME", organisationName);
		for (SdwanEndpoints endPoints : sdwanEndpoints) {
			qosUrl = endPoints.getServerIp() + ":" + endPoints.getServerPort() + qosUrl;
			LOGGER.info("saveQosByOrder url : {}", qosUrl);
			RestResponse response = restClientService.putWithBasicAuthentication(qosUrl,
					Utils.convertObjectToJson(appQosPolicyRules), new HashMap<String, String>(),
					endPoints.getServerUsername(), endPoints.getServerPassword());
			LOGGER.info("saveQosByOrder response : {}", response);
			if (Objects.nonNull(response) && response.getStatus() == Status.SUCCESS)
				qosResponse = Constants.SUCCESS;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
		return qosResponse;
	}
	
	/**
	 * To save fireWall policy rearrangement
	 *
	 * @param templateName
	 * @param organisationName
	 * @param sdwanEndpoints
	 * @param FireWallRules
	 * @return
	 * @throws TclCommonException
	 */
	public String saveFireWallByOrder(String templateName, String organisationName,String accPolyName, List<SdwanEndpoints> sdwanEndpoints,
			FireWallRules fireWallPolicyRules) throws TclCommonException {
		String firewallResponse = "";
		String fireWallUrl = versaSaveFireWallUrl;
		fireWallUrl = fireWallUrl.replace("DYNAMICTEMPLATENAME", templateName);
		fireWallUrl = fireWallUrl.replace("DYNAMICORGNAME", organisationName);
		fireWallUrl = fireWallUrl.replace("POLICYNAME", accPolyName);
		for (SdwanEndpoints endPoints : sdwanEndpoints) {
			fireWallUrl = endPoints.getServerIp() + ":" + endPoints.getServerPort() + fireWallUrl;
			LOGGER.info("saveFireWallByOrder Url: "+ fireWallUrl);
			RestResponse response = restClientService.putWithBasicAuthentication(fireWallUrl,
					Utils.convertObjectToJson(fireWallPolicyRules), new HashMap<String, String>(),
					endPoints.getServerUsername(), endPoints.getServerPassword());
			LOGGER.info("save firewallByOrder response : {}", response);
			if (Objects.nonNull(response) && response.getStatus() == Status.SUCCESS)
				firewallResponse = Constants.SUCCESS;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
		return firewallResponse;
	}
	
	/**
	 * Method to get SLA profile details by name from VERSA
	 * @param profileName
	 * @param templateName
	 * @param organisationName
	 * @param sdwanEndpoints
	 * @return
	 * @throws TclCommonException
	 */
	private SlaProfiles getSlaProfileByName(String profileName, String templateName,
			String organisationName, SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		String slaProfileView = versaSlaProfileViewByNameUrl;
		slaProfileView = slaProfileView.replace("DYNAMICTEMPLATENAME", templateName);
		slaProfileView = slaProfileView.replace("DYNAMICORGNAME", organisationName);
		slaProfileView = slaProfileView.replace("DYNAMICSLAPROFILENAME", profileName);
		slaProfileView = sdwanEndpoints.getServerIp()+":"+sdwanEndpoints.getServerPort()+slaProfileView;
		LOGGER.info("getSlaProfileByName URL {} ",slaProfileView);
		RestResponse restResponse = restClientService.getWithBasicAuthentication(slaProfileView, new HashMap<String, String>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		SlaProfiles slaProfiles = new SlaProfiles();
		if (Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())) {
			slaProfiles = Utils.convertJsonToObject(restResponse.getData(), SlaProfiles.class);
		}
		return slaProfiles;
	}
	
	/**
	 * Method to construct SLA profile edit payload
	 * @param slaProfiles
	 * @param request
	 * @param slaProfileName
	 * @return
	 * @throws TclCommonException
	 */
	public SlaProfilesCreatePayload constructSlaProfileEditPayload(SlaProfiles slaProfiles, SdwanPolicyDetailBean request, String slaProfileName) throws TclCommonException {
		SlaProfilesCreatePayload slaPayload = new SlaProfilesCreatePayload();
		if(Objects.nonNull(slaProfiles) && request.getSlaProfileName() != null)
		try {
			LOGGER.info("Inside constructSlaProfileEditPayload to construct payload for SLA profile {} ",slaProfileName);
			SlaProfile_ slaProfile = new SlaProfile_();
			slaProfile.setName(slaProfileName);
			slaProfile.setLossPercentage(request.getMaxPacketLoss());
			slaProfile.setForwardLossPercentage(request.getMaxFwdPacketLoss());
			slaProfile.setReverseLossPercentage(request.getMaxRevPacketLoss());
			slaProfile.setLatency(request.getMaxLatency());
			slaProfile.setCircuitTransmitUtilization(request.getTransmitUtilization());
			slaProfile.setCircuitReceiveUtilization(request.getReceiveUtilization());
			slaProfile.setDelayVariation(request.getJitter());
			slaProfile.setMosScore(request.getMosScore());
			slaPayload.setSlaProfile(slaProfile);
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
		
		return slaPayload;
		
	}
	
	/**
	 * Method to edit SLA profile request
	 * @param request
	 * @param sdwanEndpoint
	 * @param slaProfileName
	 * @param slaProfileRequest
	 * @return
	 * @throws TclCommonException
	 */
	private String editSlaProfileRequest(SdwanPolicyDetailBean request, SdwanEndpoints sdwanEndpoint,
			String slaProfileName, SlaProfilesCreatePayload slaProfileRequest) throws TclCommonException {
		try {
			Map<String, String> httpHeaders = new HashMap<>();
			String auth = sdwanEndpoint.getServerUsername() + ":" + sdwanEndpoint.getServerPassword();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			httpHeaders.put("Authorization", authHeader);
			httpHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.put("Accept", MediaType.APPLICATION_JSON_VALUE);
			String versaSlaProfileEditUrl = versaSlaProfileEdit;
			versaSlaProfileEditUrl = versaSlaProfileEditUrl.replace("DYNAMICTEMPLATENAME",
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName());
			versaSlaProfileEditUrl = versaSlaProfileEditUrl.replace("DYNAMICORGNAME", request.getSdwanPolicyListBean().getOrganisationName());
			versaSlaProfileEditUrl = versaSlaProfileEditUrl.replace("DYNAMICSLAPROFILENAME", slaProfileName);
			versaSlaProfileEditUrl = sdwanEndpoint.getServerIp()+":"+sdwanEndpoint.getServerPort()+versaSlaProfileEditUrl;
			String slaEditPayload = Utils.convertObjectToJson(slaProfileRequest);
			Timestamp slaEditRequestTime = new Timestamp(new Date().getTime());
			LOGGER.info("Edit SLA profile edit url {} and request {}  " ,versaSlaProfileEditUrl,slaEditPayload);
			RestResponse slaProfileEditResponse = restClientService.putWithProxyBasicAuthentication(versaSlaProfileEditUrl, slaEditPayload, httpHeaders, 
						sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
			Timestamp slaEditResponseTime = new Timestamp(new Date().getTime());
			if (slaProfileEditResponse.getStatus() == Status.SUCCESS) {
				saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndpoint.getServerCode(), versaSlaProfileEditUrl, "PUT",
						slaEditPayload, HttpStatus.NO_CONTENT.value(), slaProfileEditResponse.getData(), slaEditRequestTime, slaEditResponseTime,
						CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.SLA_PROFILE, null,
						request.getSdwanPolicyListBean().getOrganisationName(),
						request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
						ServiceInventoryConstants.SLA_PROFILE, request.getProfileName(), null, null);
				LOGGER.info("Edit SLA policy audit information has been saved {} ", slaProfileName);
				return Constants.SUCCESS;
			} else {
				saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndpoint.getServerCode(), versaSlaProfileEditUrl, "PUT",
						slaEditPayload, slaProfileEditResponse.getStatus().getStatusCode(),
						slaProfileEditResponse.getErrorMessage(), slaEditRequestTime, slaEditResponseTime,
						CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.SLA_PROFILE, null,
						request.getSdwanPolicyListBean().getOrganisationName(),
						request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
						ServiceInventoryConstants.SLA_PROFILE, request.getProfileName(), null, null);
				LOGGER.info("Edit SLA policy audit information has been saved {} with error {} ", slaProfileName,
						slaProfileEditResponse.getErrorMessage());
				throw new TclCommonException(ExceptionConstants.VERSA_SLA_PROFILE_EDIT_FAILED);
			}
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.VERSA_SLA_PROFILE_EDIT_FAILED);
		}
		
	}
	/**
	 * To Download CSV file for Audit and Transaction history for customer
	 *
	 * @param userType
	 * @param organisationName
	 * @return
	 */
	public List<AuditHistoryCustomer> createCsvForAuditCustomer(Integer customerId) {
		List<SdwanInventoryAudit> enterpriseUser=new ArrayList<>();
		List<AuditHistoryCustomer> filteredCustCol=new ArrayList<>();
		enterpriseUser=sdwanInventoryAuditRepository.findTop2000ByCustomerIdOrderByCustomerIdDesc(customerId);
		if(Objects.nonNull(enterpriseUser) && !enterpriseUser.isEmpty()) {
			enterpriseUser.stream().forEach(eachDetail->{
				AuditHistoryCustomer sdwanInventoryAudit = new AuditHistoryCustomer();
				sdwanInventoryAudit.setUuid(nullCheckForCsv(eachDetail.getUuid()));
				sdwanInventoryAudit.setUserId(nullCheckForCsv(eachDetail.getUserId()));
				sdwanInventoryAudit.setOperation(nullCheckForCsv(eachDetail.getOperation()));
				sdwanInventoryAudit.setUpdatedTime(eachDetail.getUpdatedTime());
				sdwanInventoryAudit.setTemplateName(nullCheckForCsv(eachDetail.getTemplateName()));
				filteredCustCol.add(sdwanInventoryAudit);
			});
		}
			return filteredCustCol;
	}
	
	/**
	 * Method to write and download SDWAN audit history in CSV file
	 * @param customerId
	 * @param response
	 * @throws TclCommonException
	 */
	public void downloadSdwanAudit( HttpServletResponse response) throws TclCommonException {
		try {
			UserInformation userInfo = userInfoUtils.getUserInformation();
			String userType = userInfoUtils.getUserType();
			LOGGER.info("Inside downloadSdwanAudit to download SDWAN  inventory audit for the user type {} ", userType);
			String filename = ServiceInventoryConstants.AUDIT_HISTORY;
			if(userInfo == null || userInfo.getUserType() == null || 
					!Arrays.asList(UserType.INTERNAL_USERS.toString(), UserType.CUSTOMER.toString()).contains(userInfo.getUserType())) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, R_CODE_ERROR);
			}
			response.reset();
			filename = filename+ServiceInventoryConstants.CSV_FILE_FORMAT;
			response.setContentType("text/csv");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
			if(userInfo.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())){
				List<AuditHistorySalesCSV> report = createCsvForAuditSales();
				
				CustomMappingStrategySales<AuditHistorySalesCSV> mappingStrategy = new CustomMappingStrategySales<>();
	            mappingStrategy.setType(AuditHistorySalesCSV.class);
				StatefulBeanToCsv<AuditHistorySalesCSV> writer = new StatefulBeanToCsvBuilder<AuditHistorySalesCSV>(
						response.getWriter()).withMappingStrategy(mappingStrategy).build();
				// write all users to csv file
				writer.write(report);
				
			} else if(userInfo.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
				CustomerDetail custdetail = userInfoUtils.getCustomerDetails().stream().findFirst().orElse(null);
				if(custdetail.getErfCustomerId() == null)
					throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				LOGGER.info("Inside downloadSdwanAudit to download SDWAN  inventory audit for the customer id {} ", custdetail.getErfCustomerId());
				List<AuditHistoryCustomer> report = createCsvForAuditCustomer(custdetail.getErfCustomerId());
				CustomMappingStrategySales<AuditHistoryCustomer> mappingStrategy = new CustomMappingStrategySales<>();
	            mappingStrategy.setType(AuditHistoryCustomer.class);
				StatefulBeanToCsv<AuditHistoryCustomer> writer = new StatefulBeanToCsvBuilder<AuditHistoryCustomer>(
						response.getWriter()).withMappingStrategy(mappingStrategy).build();
				// write all users to csv file
				writer.write(report);
			}
			
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		}
	}
	
	/**
	 * To Download CSV file for Audit and Transaction history for sales
	 *
	 * @param userType
	 * @param organisationName
	 * @return
	 */
	public List<AuditHistorySalesCSV> createCsvForAuditSales() {
		LOGGER.info("Inside createCsvForAuditSales to construct sdwan audit data for admin ");
		List<SdwanInventoryAudit> adminDetails=new ArrayList<>();
		List<AuditHistorySalesCSV> filteredSalCol=new ArrayList<>();
		adminDetails=sdwanInventoryAuditRepository.findTop2000ByOrderByIdDesc();
		if(Objects.nonNull(adminDetails) && !adminDetails.isEmpty()) {
			adminDetails.stream().forEach(eachDetail->{
				AuditHistorySalesCSV sdwanInventoryAudit = new AuditHistorySalesCSV();
				sdwanInventoryAudit.setUuid(nullCheckForCsv(eachDetail.getUuid()));
				sdwanInventoryAudit.setUserId(nullCheckForCsv(eachDetail.getUserId()));
				sdwanInventoryAudit.setCustomerId(eachDetail.getCustomerId());
				sdwanInventoryAudit.setCustomerLeId(nullCheckForCsv(eachDetail.getCustomerLeId()));
				sdwanInventoryAudit.setRequestPayload(nullCheckForCsv(eachDetail.getRequestPayload()));
				sdwanInventoryAudit.setResponse(nullCheckForCsv(eachDetail.getResponse()));
				sdwanInventoryAudit.setServiceId(nullCheckForCsv(eachDetail.getServiceId()));
				sdwanInventoryAudit.setUrl(nullCheckForCsv(eachDetail.getUrl()));
				sdwanInventoryAudit.setOperation(nullCheckForCsv(eachDetail.getOperation()));
				sdwanInventoryAudit.setUpdatedTime(eachDetail.getUpdatedTime());
				sdwanInventoryAudit.setInstanceRegion(nullCheckForCsv(eachDetail.getInstanceRegion()));
				sdwanInventoryAudit.setRequestTime(eachDetail.getRequestTime());
				sdwanInventoryAudit.setResponseTime(eachDetail.getResponseTime());
				sdwanInventoryAudit.setOrganizationName(nullCheckForCsv(eachDetail.getOrganizationName()));
				sdwanInventoryAudit.setTemplateName(nullCheckForCsv(eachDetail.getTemplateName()));
				sdwanInventoryAudit.setComponentValue(nullCheckForCsv(eachDetail.getComponentValue()));
				sdwanInventoryAudit.setTaskId(eachDetail.getTaskId());
				filteredSalCol.add(sdwanInventoryAudit);
			});
		}
			return filteredSalCol;
	}
	
	public String nullCheckForCsv(String values) {
		return values==null ? "" : values ;
	}
	
	/**
	 * Get Firewall Policy from versa based on template , organisation and policy
	 * name
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */

	GetFirewallPolicy getFirewallPolicies(String templateName, String organisationName, String policyName,
			String accessPolicy, SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		List<String> accessPolicyName=getAccessPolicyGroupName(templateName,organisationName,policyName,sdwanEndpoints);
		if(!accessPolicyName.isEmpty() && Objects.nonNull(accessPolicyName))
			accessPolicy=accessPolicyName.get(0);

		String firewallUrl = versaApiFirewallForPolicy;
		firewallUrl = firewallUrl.replace("DYNAMICTEMPLATENAME", templateName);
		firewallUrl = firewallUrl.replace("DYNAMICORGNAME", organisationName);
		firewallUrl = firewallUrl.replace("DYNAMICACCESSPOLICYNAME", accessPolicy);
		firewallUrl = firewallUrl.replace("DYNAMICRULENAME", policyName);
		firewallUrl = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + firewallUrl;
		LOGGER.info("getFirewallPolicies for each rule url : {}", firewallUrl);
		RestResponse response = restClientService.getWithBasicAuthentication(firewallUrl, new HashMap<String, String>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		if (Objects.nonNull(response) && Objects.nonNull(response.getData())) {
			GetFirewallPolicy firewallPolicy = Utils.convertJsonToObject(response.getData(), GetFirewallPolicy.class);
			return firewallPolicy;
		} else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
	}
	private List<String> getAccessPolicyGroupName(String templateName, String organisationName, String policyName,
			SdwanEndpoints sdwanEndpoints) throws TclCommonException {
		List<String> accessPolicyNames= new ArrayList<>();
		String firewallAccessPolicyResponse=versaApiFirewallPolicyGetUrl;
		firewallAccessPolicyResponse = firewallAccessPolicyResponse.replace("DYNAMICTEMPLATENAME", templateName);
		firewallAccessPolicyResponse = firewallAccessPolicyResponse.replace("DYNAMICORGNAME", organisationName);
		firewallAccessPolicyResponse = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort() + firewallAccessPolicyResponse;
		RestResponse responsenew = restClientService.getWithBasicAuthentication(firewallAccessPolicyResponse, new HashMap<String, String>(),
				sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		GetFirewallRuleName getFirewallRuleName= new GetFirewallRuleName();
		if (Objects.nonNull(responsenew) && Objects.nonNull(responsenew.getData())) {
		 getFirewallRuleName = Utils
				.convertJsonToObject(responsenew.getData(), GetFirewallRuleName.class);
		}
		if(Objects.nonNull(getFirewallRuleName.getAccessPolicyGroup())) {
		getFirewallRuleName.getAccessPolicyGroup().stream().forEach(accesspolicy -> {
			accessPolicyNames.add(accesspolicy.getName());
		});
		}
		return accessPolicyNames;
	}
	
	/**
	 * Set Firewall policy details
	 *
	 * @param firewallProfile
	 * @param sdwanPolicyDetailBean
	 * @throws TclCommonException
	 */
	private void mapFirewallPolicyDetails(GetFirewallPolicy firewallProfile,
			SdwanPolicyDetailBean sdwanPolicyDetailBean) throws TclCommonException {
		LOGGER.info("mapFirewallPolicyDetails response : {}", Utils.convertObjectToJson(firewallProfile));
		if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getSource())) {
			sdwanPolicyDetailBean.setSourceAddress(new ArrayList<>());
			if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getSource().getAddress())) {
				sdwanPolicyDetailBean.setSourceAddressGroups(
						firewallProfile.getAccessPolicy().getMatch().getSource().getAddress().getAddressGroupList());
				if (Objects.nonNull(
						firewallProfile.getAccessPolicy().getMatch().getSource().getAddress().getAddressList()))
					firewallProfile.getAccessPolicy().getMatch().getSource().getAddress().getAddressList()
							.forEach(sourceAddress -> {
								SdwanAddressBean sdwanAddressBean = new SdwanAddressBean();
								sdwanAddressBean.setAddressName(sourceAddress);
								sdwanPolicyDetailBean.getSourceAddress().add(sdwanAddressBean);
							});
			}
			if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getSource().getZone()))
				sdwanPolicyDetailBean.setSourceZones(
						firewallProfile.getAccessPolicy().getMatch().getSource().getZone().getZoneList());
		}
		// set destination address and groups
		if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getDestination())) {
			sdwanPolicyDetailBean.setDestinationAddress(new ArrayList<>());
			if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getDestination().getAddress())) {
				sdwanPolicyDetailBean.setDestinationAddressGroups(firewallProfile.getAccessPolicy().getMatch()
						.getDestination().getAddress().getAddressGroupList());
				if (Objects.nonNull(
						firewallProfile.getAccessPolicy().getMatch().getDestination().getAddress().getAddressList()))
					firewallProfile.getAccessPolicy().getMatch().getDestination().getAddress().getAddressList()
							.forEach(destinationAddress -> {
								SdwanAddressBean sdwanAddressBean = new SdwanAddressBean();
								sdwanAddressBean.setAddressName(destinationAddress);
								sdwanPolicyDetailBean.getDestinationAddress().add(sdwanAddressBean);
							});
			}
			if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getDestination().getZone()))
				sdwanPolicyDetailBean.setDestinationZones(
						firewallProfile.getAccessPolicy().getMatch().getDestination().getZone().getZoneList());
		}
		// set total count
		sdwanPolicyDetailBean.setAddressCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceAddress())
				+ returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationAddress()));
		sdwanPolicyDetailBean.setAddressGroupCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceAddressGroups())
				+ returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationAddressGroups()));
		sdwanPolicyDetailBean.setZonesCount(returnSizeIfNotNull(sdwanPolicyDetailBean.getSourceZones())
				+ returnSizeIfNotNull(sdwanPolicyDetailBean.getDestinationZones()));

		// set applications and count
		if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getApplication())) {
			sdwanPolicyDetailBean.setPredefinedApplications(
					firewallProfile.getAccessPolicy().getMatch().getApplication().getPredefinedApplicationList());
			sdwanPolicyDetailBean.setUserdefinedApplications(
					firewallProfile.getAccessPolicy().getMatch().getApplication().getUserDefinedApplicationList());
			sdwanPolicyDetailBean.setApplicationsCount((sdwanPolicyDetailBean.getPredefinedApplications() != null
					? sdwanPolicyDetailBean.getPredefinedApplications().size()
					: 0)
					+ (sdwanPolicyDetailBean.getUserdefinedApplications() != null
							? sdwanPolicyDetailBean.getUserdefinedApplications().size()
							: 0));
		}
		// set url's and count
		if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getUrlCategory())) {
			sdwanPolicyDetailBean
					.setPredefinedUrls(firewallProfile.getAccessPolicy().getMatch().getUrlCategory().getPredefined());
			sdwanPolicyDetailBean
					.setUserDefinedUrls(firewallProfile.getAccessPolicy().getMatch().getUrlCategory().getUserDefined());
			sdwanPolicyDetailBean.setUrlAssociatedCount((sdwanPolicyDetailBean.getPredefinedUrls() != null
					? sdwanPolicyDetailBean.getPredefinedUrls().size()
					: 0)
					+ (sdwanPolicyDetailBean.getUserDefinedUrls() != null
							? sdwanPolicyDetailBean.getUserDefinedUrls().size()
							: 0));
		}

		if (Objects.nonNull(firewallProfile.getAccessPolicy().getMatch().getServices())) {
			sdwanPolicyDetailBean.setPredefinedServices(
					firewallProfile.getAccessPolicy().getMatch().getServices().getPredefinedServicesList());
			sdwanPolicyDetailBean.setUserDefinedServices(
					firewallProfile.getAccessPolicy().getMatch().getServices().getServicesList());
			sdwanPolicyDetailBean.setServiceCount((sdwanPolicyDetailBean.getPredefinedServices() != null
					? sdwanPolicyDetailBean.getPredefinedServices().size()
					: 0)
					+ (sdwanPolicyDetailBean.getUserDefinedServices() != null
							? sdwanPolicyDetailBean.getUserDefinedServices().size()
							: 0));
		}
		if (Objects.nonNull(firewallProfile.getAccessPolicy().getSet().getSecurityProfile())) {
			sdwanPolicyDetailBean.setSecurityProfile(firewallProfile.getAccessPolicy().getSet().getSecurityProfile());
			sdwanPolicyDetailBean.setSetValue(firewallProfile.getAccessPolicy().getSet());
		}
		else {
			sdwanPolicyDetailBean.setSecurityProfile(new SecurityProfile());
			sdwanPolicyDetailBean.setSetValue(firewallProfile.getAccessPolicy().getSet());
		}
		
		if(Objects.nonNull(firewallProfile.getAccessPolicy().getDescription()))
			sdwanPolicyDetailBean.setDescription(firewallProfile.getAccessPolicy().getDescription());
		LOGGER.info("firewall Profile response: {}", Utils.convertObjectToJson(sdwanPolicyDetailBean));

	}

	/**
	 * Method to invoke VERSA predefined services
	 * 
	 * @param sdwanEndPointsForRegions
	 * @return
	 * @throws TclCommonException
	 */
	FirewallPreServices getVersaPredefinedServices(SdwanEndpoints sdwanEndPointsForRegions) throws TclCommonException {
		String preDefinedServiceUrl = versaApiFirewallPreDefinedServices;
		FirewallPreServices firewallPolicyServices;
		preDefinedServiceUrl = sdwanEndPointsForRegions.getServerIp() + ":" + sdwanEndPointsForRegions.getServerPort()
				+ preDefinedServiceUrl;
		RestResponse preDefinedServices = restClientService.getWithBasicAuthentication(preDefinedServiceUrl,
				new HashMap<String, String>(), sdwanEndPointsForRegions.getServerUsername(),
				sdwanEndPointsForRegions.getServerPassword());
		LOGGER.info("VERSA PreDefined apps Response {} ", preDefinedServices.getStatus());
		if (Objects.nonNull(preDefinedServices) && Objects.nonNull(preDefinedServices.getData())) {
			firewallPolicyServices = Utils.convertJsonToObject(preDefinedServices.getData(), FirewallPreServices.class);
		} else
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, R_CODE_ERROR);
		return firewallPolicyServices;
	}

	/**
	 * Get Firewall Policy Custom Services from versa based on template and
	 * organisation
	 *
	 * @param templateName
	 * @param organisationName
	 * @return
	 * @throws TclCommonException
	 */

	CustomService getCustomServices(String templateName, String organisationName, SdwanEndpoints sdwanEndpoints)
			throws TclCommonException {
		String firewallCustomService = versaApiFirewallCustomServices;
		firewallCustomService = firewallCustomService.replace("DYNAMICTEMPLATENAME", templateName);
		firewallCustomService = firewallCustomService.replace("DYNAMICORGNAME", organisationName);
		firewallCustomService = sdwanEndpoints.getServerIp() + ":" + sdwanEndpoints.getServerPort()
				+ firewallCustomService;
		LOGGER.info("getFirewallPolicies for each rule url : {}", firewallCustomService);
		RestResponse response = restClientService.getWithBasicAuthentication(firewallCustomService,
				new HashMap<String, String>(), sdwanEndpoints.getServerUsername(), sdwanEndpoints.getServerPassword());
		CustomService customService = new CustomService();
		if (Objects.nonNull(response) && Objects.nonNull(response.getData())) {
			 customService = Utils.convertJsonToObject(response.getData(), CustomService.class);
			
		} else {
			LOGGER.error("Error in getting the getCustomServices response {}, status {}",response.getData(),response.getStatus());
		}
		return customService;
	}

	/**
	 * Get firewall policy by rule name
	 *
	 * @param sdwanPolicyListBean
	 * @param sdwanEndpoint
	 * @param qosRuleViewUrl
	 * @param associatedUserDefinedUrls
	 * @param associatedPredefinedUrls
	 * @param asscSrcAddresses
	 * @param asscSrcAddressGroups
	 * @param asscSrcZones
	 * @param asscDestAddresses
	 * @param asscDestAddressGroups
	 * @param asscDestZones
	 * @param asscUserdefinedServices
	 * @param asscPredefinedServices
	 * @throws TclCommonException
	 */
	private void getListFromFirewallPolicy(SdwanPolicyListBean sdwanPolicyListBean, SdwanEndpoints sdwanEndpoint,
			String firewallRuleViewUrl, Set<String> associatedUserDefinedUrls, Set<String> associatedPredefinedUrls,
			Set<String> asscSrcAddresses, Set<String> asscSrcAddressGroups, Set<String> asscSrcZones,
			Set<String> asscDestAddresses, Set<String> asscDestAddressGroups, Set<String> asscDestZones,
			Set<String> asscPredefinedServices, Set<String> asscUserdefinedServices) throws TclCommonException {
		firewallRuleViewUrl = firewallRuleViewUrl.replaceAll("DYNAMICORGNAME",
				sdwanPolicyListBean.getOrganisationName());
		firewallRuleViewUrl = firewallRuleViewUrl.replaceAll("DYNAMICTEMPLATENAME",
				sdwanPolicyListBean.getAssociatedTemplate().getTemplateName());
		firewallRuleViewUrl = firewallRuleViewUrl.replaceAll("DYNAMICACCESSPOLICYNAME",
				sdwanPolicyListBean.getAccessPolicyName());
		firewallRuleViewUrl = firewallRuleViewUrl.replace("DYNAMICRULENAME", sdwanPolicyListBean.getPolicyName());
		firewallRuleViewUrl = setUrlParam(sdwanPolicyListBean, sdwanEndpoint, firewallRuleViewUrl);
		RestResponse firewallPolicyResponse = restClientService.getWithBasicAuthentication(firewallRuleViewUrl,
				new HashMap<String, String>(), sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
		if (Objects.nonNull(firewallPolicyResponse) && Objects.nonNull(firewallPolicyResponse.getData())) {
			GetFirewallPolicy firewallPolicy = Utils.convertJsonToObject(firewallPolicyResponse.getData(),
					GetFirewallPolicy.class);
			if (Objects.nonNull(firewallPolicy) && Objects.nonNull(firewallPolicy.getAccessPolicy())) {
				if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch())) {
					if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getUrlCategory())) {
						if (Objects
								.nonNull(firewallPolicy.getAccessPolicy().getMatch().getUrlCategory().getPredefined()))
							associatedPredefinedUrls.addAll(
									firewallPolicy.getAccessPolicy().getMatch().getUrlCategory().getPredefined());
						if (Objects
								.nonNull(firewallPolicy.getAccessPolicy().getMatch().getUrlCategory().getUserDefined()))
							associatedUserDefinedUrls.addAll(
									firewallPolicy.getAccessPolicy().getMatch().getUrlCategory().getUserDefined());
					}
					// fetch associated source address
					if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getSource())) {
						if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getSource().getAddress())) {
							Optional.ofNullable(firewallPolicy.getAccessPolicy().getMatch().getSource().getAddress()
									.getAddressList()).ifPresent(asscSrcAddresses::addAll);
							Optional.ofNullable(firewallPolicy.getAccessPolicy().getMatch().getSource().getAddress()
									.getAddressGroupList()).ifPresent(asscSrcAddressGroups::addAll);
						}
						if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getSource().getZone())
								&& Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getSource().getZone()
										.getZoneList()))
							asscSrcZones.addAll(
									firewallPolicy.getAccessPolicy().getMatch().getSource().getZone().getZoneList());
					}
					// fetch associated destination address
					if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getDestination())) {
						if (Objects
								.nonNull(firewallPolicy.getAccessPolicy().getMatch().getDestination().getAddress())) {
							Optional.ofNullable(firewallPolicy.getAccessPolicy().getMatch().getDestination()
									.getAddress().getAddressList()).ifPresent(asscDestAddresses::addAll);
							Optional.ofNullable(firewallPolicy.getAccessPolicy().getMatch().getDestination()
									.getAddress().getAddressGroupList()).ifPresent(asscDestAddressGroups::addAll);
						}
						if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getDestination().getZone())
								&& Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getDestination()
										.getZone().getZoneList()))
							asscDestZones.addAll(firewallPolicy.getAccessPolicy().getMatch().getDestination().getZone()
									.getZoneList());
					}

					// fetch associated services
					if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getServices())) {
						if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getServices()
								.getPredefinedServicesList())) {
							Optional.ofNullable(firewallPolicy.getAccessPolicy().getMatch().getServices()
									.getPredefinedServicesList()).ifPresent(asscPredefinedServices::addAll);
						}
					}
					
					if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getServices())) {
						if (Objects.nonNull(firewallPolicy.getAccessPolicy().getMatch().getServices()
								.getServicesList())) {
							Optional.ofNullable(
									firewallPolicy.getAccessPolicy().getMatch().getServices().getServicesList())
									.ifPresent(asscUserdefinedServices::addAll);
						}
					}
				}
			}
		}
	}

	/**
	 * create request body for firewall
	 *
	 * @param AccessPolicy
	 * @param request
	 * @throws TclCommonException
	 */
	private AccessPolicy createEditRequestForFirewall(AccessPolicy accessPolicy, SdwanPolicyDetailBean request)
			throws TclCommonException {
		LOGGER.info("createEditRequestForFirewall accessPolicy value : {}", accessPolicy);
		LOGGER.info("createEditRequestForFirewall request value : {}", request);
		if (Objects.isNull(accessPolicy))
			accessPolicy = new AccessPolicy();
		if (Objects.isNull(accessPolicy.getMatch()))
			accessPolicy.setMatch(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Match());
		if (Objects.isNull(accessPolicy.getMatch().getApplication()))
			accessPolicy.getMatch().setApplication(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Application());
		if (Objects.isNull(accessPolicy.getMatch().getUrlCategory()))
			accessPolicy.getMatch().setUrlCategory(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.UrlCategory());
		if (Objects.isNull(accessPolicy.getMatch().getServices()))
			accessPolicy.getMatch().setServices(new Services());
		if (Objects.isNull(accessPolicy.getMatch().getSource().getZone()))
			accessPolicy.getMatch().getSource()
					.setZone(new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Zone());
		if (Objects.isNull(accessPolicy.getMatch().getSource().getAddress()))
			accessPolicy.getMatch().getSource().setAddress(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Address());
		if (Objects.isNull(accessPolicy.getMatch().getDestination()))
			accessPolicy.getMatch().setDestination(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Destination());
		if (Objects.isNull(accessPolicy.getMatch().getDestination())){
		if (Objects.isNull(accessPolicy.getMatch().getDestination().getZone()))
			accessPolicy.getMatch().getDestination().setZone(new Zone_());
		
		if (Objects.isNull(accessPolicy.getMatch().getDestination().getAddress()))
			accessPolicy.getMatch().getDestination().setAddress(
					new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Address_());
		}
		if (!request.getSourceZones().isEmpty() && Objects.nonNull(request.getSourceZones())) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getSource())&& Objects.nonNull(accessPolicy.getMatch().getSource().getZone()))
				accessPolicy.getMatch().getSource().getZone().setZoneList(request.getSourceZones());
		} 
		else
			accessPolicy.getMatch().getSource().getZone().setZoneList(new ArrayList<>());
		
		if (Objects.nonNull(request.getSourceAddress()) && !request.getSourceAddress().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getSource())
					&& Objects.nonNull(accessPolicy.getMatch().getSource().getAddress())) {
				List<String> souceAddress = new ArrayList<>();
				request.getSourceAddress().forEach(sourceAddress -> {
					souceAddress.add(sourceAddress.getAddressName());
				});
				accessPolicy.getMatch().getSource().getAddress().setAddressList(souceAddress);
			}
		} 
		else
			accessPolicy.getMatch().getSource().getAddress().setAddressList(new ArrayList<>());
		
		if (Objects.nonNull(request.getSourceAddressGroups()) && !request.getSourceAddressGroups().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getSource())
					&& Objects.nonNull(accessPolicy.getMatch().getSource().getAddress())) {
				accessPolicy.getMatch().getSource().getAddress().setAddressGroupList(request.getSourceAddressGroups());
			}
		} else
			accessPolicy.getMatch().getSource().getAddress().setAddressGroupList(new ArrayList<>());

		if (!request.getDestinationZones().isEmpty() && Objects.nonNull(request.getDestinationZones())) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					) {
				com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Destination destination= 
						new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Destination();
				accessPolicy.getMatch().setDestination(destination);
				Zone_ zone = new Zone_();
				zone.setZoneList(new ArrayList<>());
				accessPolicy.getMatch().getDestination().setZone(zone);
				accessPolicy.getMatch().getDestination().getZone().setZoneList(request.getDestinationZones());
			}
		} else {
			if(accessPolicy.getMatch().getDestination()!=null && accessPolicy.getMatch().getDestination().getZone()!=null)
			accessPolicy.getMatch().getDestination().getZone().setZoneList(new ArrayList<>());
		}
		
		if (Objects.nonNull(request.getDestinationAddress()) && !request.getDestinationAddress().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getDestination())) {
				List<String> destinationAddress = new ArrayList<>();
				request.getDestinationAddress().forEach(desAdress -> {
					destinationAddress.add(desAdress.getAddressName());
				});
				if(accessPolicy.getMatch().getDestination()!=null) {
				accessPolicy.getMatch().getDestination().setAddress(
						new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Address_());
				accessPolicy.getMatch().getDestination().getAddress().setAddressList(destinationAddress);
				}
			}
		} 
//		else {
//			if(accessPolicy.getMatch().getDestination()!=null && accessPolicy.getMatch().getDestination().getAddress()!=null) {
//			accessPolicy.getMatch().getDestination().getAddress().setAddressList(new ArrayList<>());
//			}
//		}
		if (Objects.nonNull(request.getDestinationAddressGroups())
				&& !request.getDestinationAddressGroups().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getDestination())) {
				//if(accessPolicy.getMatch().getDestination()!=null) {
//					accessPolicy.getMatch().getDestination().setAddress(
//							new com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Address_());
				accessPolicy.getMatch().getDestination().getAddress()
						.setAddressGroupList(request.getDestinationAddressGroups());
				//}
			}
		} 
//		else {
//			if(accessPolicy.getMatch().getDestination()!=null  && accessPolicy.getMatch().getDestination().getAddress()!=null)
//			accessPolicy.getMatch().getDestination().getAddress().setAddressGroupList(request.getDestinationAddressGroups());
//		}

		if (Objects.nonNull(request.getPredefinedServices()) && !request.getPredefinedServices().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getServices()))
				accessPolicy.getMatch().getServices().setPredefinedServicesList(request.getPredefinedServices());

		} else
			accessPolicy.getMatch().getServices().setPredefinedServicesList(request.getPredefinedServices());
		
		if (Objects.nonNull(request.getUserDefinedServices()) && !request.getUserDefinedServices().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getServices()))
				accessPolicy.getMatch().getServices().setServicesList(request.getUserDefinedServices());
		} else
			accessPolicy.getMatch().getServices().setServicesList(request.getUserDefinedServices());

		if (Objects.nonNull(request.getPredefinedApplications()) && !request.getPredefinedApplications().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getApplication()))
				accessPolicy.getMatch().getApplication()
						.setPredefinedApplicationList(request.getPredefinedApplications());
		} else
			accessPolicy.getMatch().getApplication().setPredefinedApplicationList(request.getPredefinedApplications());

		if (Objects.nonNull(request.getUserdefinedApplications()) && !request.getUserdefinedApplications().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch())
					&& Objects.nonNull(accessPolicy.getMatch().getApplication()))
				accessPolicy.getMatch().getApplication()
						.setUserDefinedApplicationList(request.getUserdefinedApplications());
		} else
			accessPolicy.getMatch().getApplication().setUserDefinedApplicationList(request.getUserdefinedApplications());

		if (Objects.nonNull(request.getSetValue())) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getSet()))
				accessPolicy.setSet(request.getSetValue());
		}
		if (Objects.nonNull(request.getDescription())) {
			if (Objects.nonNull(accessPolicy))
				accessPolicy.setDescription(request.getDescription());
		}
		else {
			if (Objects.isNull(request.getDescription()))
				accessPolicy.setDescription(" ");
			}
		if (Objects.nonNull(request.getPredefinedUrls()) && !request.getPredefinedUrls().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch().getUrlCategory()))
				accessPolicy.getMatch().getUrlCategory().setPredefined(request.getPredefinedUrls());
		} else
			accessPolicy.getMatch().getUrlCategory().setPredefined(request.getPredefinedUrls());
		
		if (Objects.nonNull(request.getUserDefinedUrls()) && !request.getUserDefinedUrls().isEmpty()) {
			if (Objects.nonNull(accessPolicy) && Objects.nonNull(accessPolicy.getMatch().getUrlCategory()))
				accessPolicy.getMatch().getUrlCategory().setUserDefined(request.getUserDefinedUrls());
		} else
			accessPolicy.getMatch().getUrlCategory().setUserDefined(request.getUserDefinedUrls());

		LOGGER.info("createEditRequestForFirewall Request body : {}", Utils.convertObjectToJson(accessPolicy));
		return accessPolicy;
	}

	/**
	 * For editing Firewall policy (versa api)
	 *
	 * @param editRequest
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	private String editFirewallPolicy(SdwanEndpoints sdwanEndPointsForRegion, AccessPolicy editRequest,
			SdwanPolicyDetailBean request) throws TclCommonException {
		List<String> accessPolicyName=getAccessPolicyGroupName(request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
				request.getSdwanPolicyListBean().getOrganisationName(),request.getSdwanPolicyListBean().getPolicyName(),sdwanEndPointsForRegion);
		if(!accessPolicyName.isEmpty() && Objects.nonNull(accessPolicyName))
			request.getSdwanPolicyListBean().setAccessPolicyName(accessPolicyName.get(0));
		String firewallUrl = sdwanEndPointsForRegion.getServerIp() + ":" + sdwanEndPointsForRegion.getServerPort()
				+ versaApiFirewallForPolicy;
		firewallUrl = firewallUrl.replace("DYNAMICTEMPLATENAME",
				request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName());
		firewallUrl = firewallUrl.replace("DYNAMICORGNAME", request.getSdwanPolicyListBean().getOrganisationName());
		firewallUrl = firewallUrl.replace("DYNAMICACCESSPOLICYNAME",
				request.getSdwanPolicyListBean().getAccessPolicyName());
		firewallUrl = firewallUrl.replace("DYNAMICRULENAME", request.getSdwanPolicyListBean().getPolicyName());
		Map<String, String> httpHeaders = new HashMap<>();
		httpHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.put("Accept", MediaType.APPLICATION_JSON_VALUE);
		GetFirewallPolicy firewallReqBody = new GetFirewallPolicy();
		firewallReqBody.setAccessPolicy(editRequest);
		String requestString = Utils.convertObjectToJson(firewallReqBody);
		LOGGER.info("editFirewallPolicy request body : " + requestString);
		LOGGER.info("editFirewallPolicy Request url : " + firewallUrl);
		Timestamp requestTime = new Timestamp(new Date().getTime());
		RestResponse restResponse = restClientService.putWithBasicAuthentication(firewallUrl, requestString,
				httpHeaders, sdwanEndPointsForRegion.getServerUsername(), sdwanEndPointsForRegion.getServerPassword());
		Timestamp responseTime = new Timestamp(new Date().getTime());
		if (restResponse.getStatus() == Status.SUCCESS) {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndPointsForRegion.getServerCode(),
					firewallUrl, "PUT", requestString, HttpStatus.NO_CONTENT.value(), restResponse.getData(),
					requestTime, responseTime,
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.POLICY, null,
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.POLICY, request.getSdwanPolicyListBean().getPolicyName(), null, null);
			LOGGER.info("Audit information has been saved");
			return Constants.SUCCESS;
		} else {
			saveAuditInfoSdwan(request.getCustomerId(), null, null, null, sdwanEndPointsForRegion.getServerCode(),
					firewallUrl, "PUT", requestString, restResponse.getStatus().getStatusCode(), restResponse.getData(),
					requestTime, responseTime,
					CommonConstants.EDIT + CommonConstants.HYPHEN + ServiceInventoryConstants.POLICY, null,
					request.getSdwanPolicyListBean().getOrganisationName(),
					request.getSdwanPolicyListBean().getAssociatedTemplate().getTemplateName(),
					ServiceInventoryConstants.POLICY, request.getSdwanPolicyListBean().getPolicyName(), null, null);
			LOGGER.info("Audit information has been saved");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR);
		}
	}
	
	public List<PerformanceResponse> getPerformanceDetails(PerformanceRequest request) throws TclCommonException {
		LOGGER.info("getPerformanceDetails {}", request);
		List<PerformanceResponse> performanceRespList = new ArrayList<>();
		if (Objects.isNull(request))
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		inputRequestValidation(request);
		String requestBody = constructingRequestBody(request);
		try {
			RestResponse responseBody = restClientService.post(olioRequestUrl, requestBody);

			if (Objects.nonNull(responseBody.getData())) {
				LOGGER.info("responseBody of getPerformanceDetails():::: {}", responseBody.getData());
				JSONParser jsonParser = new JSONParser();
				JSONObject responseObj = (JSONObject) jsonParser.parse(responseBody.getData());
				JSONObject aggregationObj = (JSONObject) responseObj.get("aggregations");
				JSONObject dataObj2 = (JSONObject) aggregationObj.get("2");
				JSONArray bucketsArray = (JSONArray) dataObj2.get("buckets");
				for (Object bucketdata : bucketsArray) {
					PerformanceResponse perfresp = new PerformanceResponse();
					JSONObject app = (JSONObject) bucketdata;
					String keyasstring = app.get("key_as_string") != null ? (String) app.get("key_as_string") : null;
					perfresp.setDate(keyasstring.split("\\+")[0]);
					JSONObject appObject = (JSONObject) app.get("3");
					JSONArray bucketsList = (JSONArray) appObject.get("buckets");

					for (Object bucket : bucketsList) {
						JSONObject zero = (JSONObject) bucket;
						JSONObject zeroObject = (JSONObject) zero.get("1");
						Double value = zeroObject.get("value") != null ? (Double) zeroObject.get("value") : null;
						DecimalFormat df = new DecimalFormat("#.###");
						perfresp.setValue(df.format(value));
						performanceRespList.sort((PerformanceResponse p1,
								PerformanceResponse p2) -> (p1.getDate().compareTo(p2.getDate())));
						performanceRespList.add(perfresp);
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in getPerformance Details :::: {}",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		LOGGER.info("performanceRespList:: {}", performanceRespList);

		return performanceRespList;
	}

	/**
	 * 
	 * constructing request body
	 *
	 * @param request
	 */
	private String constructingRequestBody(PerformanceRequest request) {
		
		String requestBody=ServiceInventoryConstants.REQUESTBODY;
		requestBody=requestBody.replace("<REMOTESITENAME>",request.getCpeRemote());
		requestBody=requestBody.replace("<LOCALSITENAME>",request.getCpeLocal());
		requestBody= requestBody.replace("<STARTDATE>",request.getStartDate());
		requestBody=requestBody.replace("<ENDDATE>",request.getEndDate());
		requestBody=requestBody.replace("<PERFORMANCEPARAM>",request.getPerformanceMatrix());
		
		LOGGER.info("constructingRequestBody() ::: {}", requestBody);
		
		return requestBody;
	}

	/**
	 * Request Parameters Validation
	 * @param request
	 * @throws TclCommonException 
	 */

	private void inputRequestValidation(PerformanceRequest request) throws TclCommonException {
		LOGGER.info("Validating the request params:: {} ",request);
		if (!StringUtils.isBlank(request.getPerformanceMatrix())
				&& request.getPerformanceMatrix().equalsIgnoreCase("jitter")) {
			request.setPerformanceMatrix("Jitter");

		} else if (!StringUtils.isBlank(request.getPerformanceMatrix())
				&& (request.getPerformanceMatrix().equalsIgnoreCase("Latency")
						|| request.getPerformanceMatrix().equalsIgnoreCase("delay"))) {

			request.setPerformanceMatrix("delay");
		} else if (!StringUtils.isBlank(request.getPerformanceMatrix())
				&& request.getPerformanceMatrix().equalsIgnoreCase("Packet loss")) {
			request.setPerformanceMatrix("pduLossRatio");

		} else {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		if (!StringUtils.isBlank(request.getStartDate()) && !StringUtils.isBlank(request.getEndDate())
			&& !StringUtils.isBlank(request.getCpeLocal()) && !StringUtils.isBlank(request.getCpeRemote())) {
			request.setStartDate(request.getStartDate());
			request.setEndDate(request.getEndDate());
		} else {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

	}
	/**
	 * method to fetch only cpe,site and link details
	 * @param productId
	 * @param customerId
	 * @param partnerId
	 * @param customerLeId
	 * @return
	 * @throws TclCommonException
	 */
	public SdwanSiteDetailsPerformaceBean getSdwanPerformaceSiteDetails(Integer productId, Integer customerId, Integer partnerId, Integer customerLeId) throws TclCommonException {
			Integer page=-1;
			Integer size=1000;
			SdwanSiteDetailsBean response = getSdwanAndNetworkSiteDetails(productId, page, size,customerId, partnerId, customerLeId);
			LOGGER.info(" response in  getSdwanAndNetworkSiteDetails {}", response);
			SdwanSiteDetailsPerformaceBean sdwanSiteDetailsPerformaceBean= new SdwanSiteDetailsPerformaceBean();
			List<SdwanSitePerformanceDetails> sdwanSitePerformanceDetailsList= new ArrayList<>();
			if(Objects.nonNull(response.getSiteDetails())) {
			response.getSiteDetails().forEach(sitedetails -> {
				SdwanSitePerformanceDetails SdwanSitePerformanceDetails= new SdwanSitePerformanceDetails();
				SdwanSitePerformanceDetails.setSiteName(sitedetails.getSdwanSiteAlias()+"("+sitedetails.getSiteName()+")");
				SdwanSitePerformanceDetails.setServiceId(sitedetails.getSiteName());
				SdwanEndpoints endPoints=sdwanEndpointsRepository.findByServerCode(sitedetails.getInstanceRegion());
				LOGGER.info("sitedetails.getSiteName()::{},sitedetails.getInstanceRegion():: {}, endPoints::{}::",sitedetails.getSiteName(),sitedetails.getInstanceRegion(),endPoints);
				List<SdwanCpePerformanceDetails> sdwanCpePerformanceDetailsList= new ArrayList<>();
				if(Objects.nonNull(sitedetails.getSdwanCpeDetails())) {
				sitedetails.getSdwanCpeDetails().forEach(cpeDtails->{
					if(Objects.nonNull(cpeDtails)) {
					SdwanCpePerformanceDetails SdwanCpePerformanceDetails= new SdwanCpePerformanceDetails();
					SdwanCpePerformanceDetails.setCpeName(cpeDtails.getCpeName());
					try {
						List<String> interfacenames= new ArrayList<>();
						if(Objects.nonNull(getConfiguredInterfaceBW(endPoints,  cpeDtails.getCpeName()).getInterfaces()))
						    if(Objects.nonNull(getConfiguredInterfaceBW(endPoints,  cpeDtails.getCpeName()).getInterfaces().getVni()))
						        getConfiguredInterfaceBW(endPoints,  cpeDtails.getCpeName()).getInterfaces().getVni().stream()
								.forEach(inter->interfacenames.add(inter.getName()));
						SdwanCpePerformanceDetails.setInterfaceNames(interfacenames);
						
						} catch (TclCommonException e) {
							LOGGER.error("Exception fetching interfacenames for {}", cpeDtails.getCpeName());
						}
					List<PerformanceAttributes> linksList = new ArrayList<>();
					if(Objects.nonNull(cpeDtails.getLinks())) {
					cpeDtails.getLinks().forEach(link->{
						PerformanceAttributes PerformanceAttributes= new PerformanceAttributes();
						PerformanceAttributes.setAttributeName(link.getAttributeName());
						linksList.add(PerformanceAttributes);
					});
					}
					SdwanCpePerformanceDetails.setLinks(linksList);
					sdwanCpePerformanceDetailsList.add(SdwanCpePerformanceDetails);
					}	
				});
				}
				if(Objects.nonNull(sdwanCpePerformanceDetailsList) && sdwanCpePerformanceDetailsList.size()>0) {
				SdwanSitePerformanceDetails.setSdwanCpeDetails(sdwanCpePerformanceDetailsList);
				sdwanSitePerformanceDetailsList.add(SdwanSitePerformanceDetails);
				}
			});
			}
			if(Objects.nonNull(sdwanSitePerformanceDetailsList))
			sdwanSiteDetailsPerformaceBean.setSiteDetails(sdwanSitePerformanceDetailsList);
				
		return sdwanSiteDetailsPerformaceBean;
	}
	
	/**
	 * @param underlayTemplates
	 * @param siAsset
	 * @param sdwanCPEBean
	 */
	private void constructSdwanTemplateDetails(List<VwServiceAttributesBean> underlayTemplates, SIAsset siAsset,
			SdwanCPEBean sdwanCPEBean) {
		underlayTemplates.stream()
		.filter(underlayTemplate -> siAsset.getSiServiceDetail().getId().equals(underlayTemplate.getSysId()))
		.forEach(template->{
			SdwanTemplateBean templateDetail = new SdwanTemplateBean();
			templateDetail.setTemplateName(template.getAttributeValue());
			templateDetail.setTemplateId(template.getAttributeId());
			sdwanCPEBean.setTemplate(templateDetail);
		});
	}
	
	/**
	 * Method to construct underlay templates
	 * Method to get underlay templates 
	 * @param underlaySysIds
	 * @return
	 */
	private List<VwServiceAttributesBean> getUnderlayTemplates(List<Integer> underlaySysIds) {
		List<Map<String, Object>> templateDetails = serviceAdditionalInfoRepository.findAttributesByUnderlayServiceIds(underlaySysIds, 
				Arrays.asList(ServiceInventoryConstants.SDWAN_TEMPLATE_NAME));
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
	 * method to get the siteByUsageLiveDataDetails
	 * @param siteName
	 * @param cpeName
	 * @param apiType
	 * @return
	 * @throws TclCommonException
	 */
	public Object getLiveDatadetails(String siteName, String cpeName, String apiType) throws TclCommonException {
		LOGGER.info("Entered in to getLiveDatadetails()  siteName:: ,{}, cpeName:: {},apiType :: {}", siteName, cpeName,
				apiType);
		if (StringUtils.isBlank(siteName) && StringUtils.isBlank(cpeName) && StringUtils.isBlank(apiType)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		if(apiType.equalsIgnoreCase("all_traffic")) {
			apiType="All_Traffic";
		}
		JSONObject interfaces_statistics = new JSONObject();
		Map<String, Object> sdWanSiteDetails = new HashMap<String, Object>();
		List<Integer> sdwanSysId = new ArrayList<>();
		ViewSiServiceInfoAllBean vwSiInfoOverlay = new ViewSiServiceInfoAllBean();
		try {
			if (Objects.nonNull(siteName))
				LOGGER.info("fetching sdWanSiteDetails based on siteName");
			sdWanSiteDetails = vwSiServiceInfoAllRepository.findSdwanSite(siteName);
			LOGGER.info("sdWanSiteDetails ::{}", sdWanSiteDetails);
			if (sdWanSiteDetails != null && !sdWanSiteDetails.isEmpty()) {
				final ObjectMapper mapper = new ObjectMapper();
				vwSiInfoOverlay = mapper.convertValue(sdWanSiteDetails, ViewSiServiceInfoAllBean.class);
			}
			LOGGER.info("Inside getLiveDatadetails for siteByUsage sysId's {}", vwSiInfoOverlay.getSysId());
			sdwanSysId.add(vwSiInfoOverlay.getSysId());
			List<SIServiceAttribute> overLayServiceAttrs = siServiceAttributeRepository
					.findBySiServiceDetail_IdInAndAttributeNameIn(sdwanSysId,
							Arrays.asList(ServiceInventoryConstants.SDWAN_ORGANISATION_NAME,
									ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING));

			List<String> orgNames = new ArrayList<>();
			List<String> siteToInstanceRegion = new ArrayList<>();
			overLayServiceAttrs.stream().forEach(attributes -> {
				if (ServiceInventoryConstants.SDWAN_ORGANISATION_NAME.equalsIgnoreCase(attributes.getAttributeName()))
					orgNames.add(attributes.getAttributeValue());
				else if (ServiceInventoryConstants.SDWAN_INSTANCE_MAPPING
						.equalsIgnoreCase(attributes.getAttributeName()))
					siteToInstanceRegion.add(attributes.getAttributeValue());
			});
			LOGGER.info("OrganizationName List {},siteToInstanceRegion List{} ", orgNames, siteToInstanceRegion);
			LOGGER.info("OrganizationName {},siteToInstanceRegion{} ", orgNames.get(0), siteToInstanceRegion.get(0));
			SdwanEndpoints endPoints = sdwanEndpointsRepository.findByServerCode(siteToInstanceRegion.get(0));
			SiteUtilizationRequest req = constructingSiteUtilizationRequest(apiType, cpeName, orgNames.get(0),
					endPoints);
			LOGGER.info("SiteUtilizationRequest {}, olionodeJsonUrl{}",req,olionodeJsonUrl);
			RestResponse responseBody = restClientService.post(olionodeJsonUrl, Utils.convertObjectToJson(req));
			LOGGER.info("responseBody :: olionodeJsonUrl:: {} Data{}, Status:: {}", olionodeJsonUrl,responseBody.getData(),responseBody.getStatus());
			if (Objects.nonNull(responseBody.getData())) {
				JSONParser jsonParser = new JSONParser();
				JSONObject responseObj = (JSONObject) jsonParser.parse(responseBody.getData());
				JSONObject aggregationObj = (JSONObject) responseObj.get("api_output");
				JSONObject data = (JSONObject) aggregationObj.get("data");
				JSONObject apiOutPut = (JSONObject) data.get("api_output");
				if (req.getApi_type().equalsIgnoreCase("All_Traffic")) {
					interfaces_statistics = (JSONObject) apiOutPut.get("interfaces:statistics");
				} else if (req.getApi_type().equalsIgnoreCase("SDWAN_Traffic")) {
					interfaces_statistics = (JSONObject) apiOutPut.get("collection");
				} else {
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				}
			}
			LOGGER.error("Error in getting the response {}",responseBody.getData());
		}

		catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return interfaces_statistics;
	}

	private SiteUtilizationRequest constructingSiteUtilizationRequest(String apiType, String cpeName,
			String organizationName, SdwanEndpoints endPoints) {

		SiteUtilizationRequest req = new SiteUtilizationRequest();
//		req.setApi_type(apiType);
//		req.setCpename("ind-uat-matrix-00086-1");
//		req.setCustomername("OLIO-UAT");
//		req.setRequestid(DateUtil.convertDateToStringWithTime(new Date()));
//		req.setVd_ip("10.133.228.8");
//		req.setVd_port(endPoints.getServerPort());

		// need to uncomment when data is coming from olio
		 req.setApi_type(apiType);
		 req.setCpename(cpeName);
		 req.setCustomername(organizationName);
		 req.setRequestid(DateUtil.convertDateToStringWithTime(new Date()));
	   	req.setVd_ip(endPoints.getServerIp().replaceAll("https://",""));
	 	req.setVd_port(endPoints.getServerPort());
	
		return req;

	}

}
