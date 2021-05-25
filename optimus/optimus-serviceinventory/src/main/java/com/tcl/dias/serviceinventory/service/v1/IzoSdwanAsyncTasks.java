package com.tcl.dias.serviceinventory.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.beans.SdwanCPEBean;
import com.tcl.dias.serviceinventory.beans.SdwanCpeDetails;
import com.tcl.dias.serviceinventory.beans.SdwanSiteDetails;
import com.tcl.dias.serviceinventory.beans.TemplateCpeStatusResponse;
import com.tcl.dias.serviceinventory.constants.ExceptionConstants;
import com.tcl.dias.serviceinventory.entity.entities.SdwanEndpoints;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status.CiscoBulkCpeResponse;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_commit_template.CommitTemplateRequest;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.DeviceGroupDatum;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.TemplateCommitResponse;
import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Class for executing Async tasks for Izo Sdwan
 * 
 * @author Srinivasa Raghavan
 */
@Service
@EnableAsync
public class IzoSdwanAsyncTasks {

	@Autowired
	RestClientService restClientService;

	@Autowired
	IzoSdwanInventoryService izoSdwanInventoryService;
	
	@Autowired
	IzoSdwanCiscoInventoryService izoSdwanInventoryServiceCisco;
	
	@Autowired
	IzoSdwanCiscoService izoSdwanCiscoService;
	

	@Value("${versa.api.commit.template}")
	private String commitTemplateUrl;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanAsyncTasks.class);

	/**
	 * To fetch WAN status for different CPEs asynchronously/concurrently
	 *
	 * @param instanceByCode
	 * @param sdwanCPEBean
	 * @return
	 */
	@Async("asyncExecutor")
	public CompletableFuture<String> wanStatusAsync(Map<String, List<SdwanEndpoints>> instanceByCode,
			SdwanCPEBean sdwanCPEBean) {
		LOGGER.info("Entering wanStatusAsync by thread id {}, name {}", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		Map<String, String> links = new HashMap<>();
		if (ServiceInventoryConstants.ONLINE.equals(sdwanCPEBean.getCpeStatus())) {
			izoSdwanInventoryService.getWanStatusFromVersa(sdwanCPEBean.getCpeName(), sdwanCPEBean.getControllers(),
					sdwanCPEBean.getInstanceRegion(), sdwanCPEBean.getOrganisationName(), instanceByCode, links);
			sdwanCPEBean.setLinks(new ArrayList<>());
			links.forEach((linkName, linkStatus) -> {
				Attributes attributes = new Attributes();
				attributes.setAttributeName(linkName);
				attributes.setAttributeValue(linkStatus);
				sdwanCPEBean.getLinks().add(attributes);
			});
		}
		LOGGER.info("Exiting wanStatusAsync by thread id {}, name {}", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		return CompletableFuture.completedFuture("SUCCESS");
	}

	/**
	 * To fetch WAN status of CPEs under each site asynchronously/concurrently for
	 * site status
	 *
	 * @param instanceByCode
	 * @param sdwanCpeDetails
	 * @param instanceRegion
	 * @param organisationName
	 * @return
	 */
	@Async("asyncExecutor")
	public CompletableFuture<String> getWanSiteStatusAsync(Map<String, List<SdwanEndpoints>> instanceByCode,
			SdwanCpeDetails sdwanCpeDetails, String instanceRegion, String organisationName,
			SdwanSiteDetails sdwanSiteDetail) {
		LOGGER.info("Entering getWanSiteStatusAsync by thread id {}, name {}", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		Map<String, String> links = new HashMap<>();
		if (ServiceInventoryConstants.ONLINE.equals(sdwanCpeDetails.getCpeStatus())) {
			izoSdwanInventoryService.getWanStatusFromVersa(sdwanCpeDetails.getCpeName(),
					sdwanCpeDetails.getControllers(), instanceRegion, organisationName, instanceByCode, links);
			sdwanCpeDetails.setLinks(new ArrayList<>());
			if (!links.isEmpty()) {
				links.forEach((linkName, linkStatus) -> {
					Attributes attributes = new Attributes();
					attributes.setAttributeName(linkName);
					attributes.setAttributeValue(linkStatus);
					sdwanCpeDetails.getLinks().add(attributes);
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
		LOGGER.info("Exiting getWanSiteStatusAsync by thread id {}, name {}", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		return CompletableFuture.completedFuture("SUCCESS");
	}

	/**
	 * Commit templates on In-Sync CPEs
	 *
	 * @param customerId
	 * @param cpeAvailabilityByTemplate
	 * @param instanceByCode
	 * @param orgTemplates
	 * @param orgDirectoryRegions
	 * @param templatesRegionMapping
	 * @return
	 */
	@Async("asyncExecutor")
	protected TemplateCpeStatusResponse commitTemplateOnCpe(Integer customerId,
			Map<String, Set<DeviceGroupDatum>> cpeAvailabilityByTemplate,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, Set<String>> orgTemplates,
			Map<String, Set<String>> orgDirectoryRegions, String operation, String operationValue, String user,
			Set<Integer> customerLeIds, Map<String, Set<String>> templatesRegionMapping) {
		LOGGER.info("Thread id {} name {} entering commitTemplateOnCpe ", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		TemplateCpeStatusResponse templateCpeStatusResponse = new TemplateCpeStatusResponse();
		templateCpeStatusResponse.setTemplates(new ArrayList<>());
		templateCpeStatusResponse.setInSyncCpes(new ArrayList<>());
		templateCpeStatusResponse.setOutOfSyncCpes(new ArrayList<>());
		templateCpeStatusResponse.setTaskIds(new ArrayList<>());
		orgTemplates.entrySet().forEach(orgTemplate -> {
			orgTemplate.getValue().forEach(template -> {
				templatesRegionMapping.get(template).forEach(templateRegion -> {
					instanceByCode.get(templateRegion).forEach(instance -> {
						templateCpeStatusResponse.getTemplates().add(template);
						String commitTemplatesUrl = commitTemplateUrl;
						commitTemplatesUrl = commitTemplatesUrl.replaceAll("DYNAMICTEMPLATENAME", template);
						SdwanEndpoints sdwanEndpoint = instance;
						commitTemplatesUrl = sdwanEndpoint.getServerIp() + ":" + sdwanEndpoint.getServerPort()
								+ commitTemplatesUrl;
						if (cpeAvailabilityByTemplate.get(template) != null
								&& !cpeAvailabilityByTemplate.get(template).isEmpty()) {
							Map<String, Set<String>> outOfSyncCpeByTemplate = new HashMap<>();
							CommitTemplateRequest commitTemplateRequest = izoSdwanInventoryService
									.checkCpeStatusConstructCommitRequest(template,
											cpeAvailabilityByTemplate.get(template), outOfSyncCpeByTemplate,
											templateCpeStatusResponse);
							try {
								if (Objects.nonNull(commitTemplateRequest.getVersanmsDevices().getDeviceList())
										&& !commitTemplateRequest.getVersanmsDevices().getDeviceList().isEmpty()) {
									LOGGER.info("Thread {} Commit template VERSA URL {} ",
											Thread.currentThread().getName(), commitTemplatesUrl);
									String requestBody = Utils.convertObjectToJson(commitTemplateRequest);
									LOGGER.info("Thread {} Commit template VERSA Request {} ",
											Thread.currentThread().getName(), requestBody);
									Timestamp requestTime = new Timestamp(new Date().getTime());
									RestResponse response = restClientService.postWithBasicAuthentication(
											commitTemplatesUrl, requestBody, new HashMap<String, String>(),
											sdwanEndpoint.getServerUsername(), sdwanEndpoint.getServerPassword());
									Timestamp responseTime = new Timestamp(new Date().getTime());
									if (response.getStatus() == Status.SUCCESS) {
										templateCpeStatusResponse.setCreateUpdateStatus(CommonConstants.SUCCESS);
										TemplateCommitResponse templateInSyncStatus = Utils
												.convertJsonToObject(response.getData(), TemplateCommitResponse.class);
										templateCpeStatusResponse.getTaskIds()
												.add(templateInSyncStatus.getVersanmsTemplateResponse().getTaskId());
										izoSdwanInventoryService.saveAuditInfoSdwan(customerId, null, customerLeIds,
												null, templateRegion, commitTemplatesUrl, "POST", requestBody,
												HttpStatus.OK.value(), response.getData(), requestTime, responseTime,
												ServiceInventoryConstants.COMMIT_TEMPLATE_IN_SYNC, null,
												orgTemplate.getKey(), template, operation, operationValue,
												templateInSyncStatus.getVersanmsTemplateResponse().getTaskId(), user);
									}
								}
								if (!outOfSyncCpeByTemplate.isEmpty()
										&& Objects.nonNull(outOfSyncCpeByTemplate.get(template))) {
									LOGGER.info("Saving audit of out of sync cpe {} and template {} ", outOfSyncCpeByTemplate.get(template), template);
									izoSdwanInventoryService.saveAuditInfoSdwan(customerId, null, customerLeIds, null,
											templateRegion, null, null,
											Utils.convertObjectToJson(outOfSyncCpeByTemplate.get(template)), null, null,
											null, null, ServiceInventoryConstants.COMMIT_TEMPLATE_OUT_SYNC, null,
											orgTemplate.getKey(), template, operation, operationValue, null, user);
								}
							} catch (Exception e) {
								templateCpeStatusResponse.setCreateUpdateStatus(CommonConstants.FAILIURE);
								throw new TclCommonRuntimeException(ExceptionConstants.VERSA_USER_APPL_ERROR,
										ResponseResource.R_CODE_ERROR);
							}
						}
					});
				});
			});
		});
		LOGGER.info("Thread id {} name {} exiting commitTemplateOnCpe ", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		return templateCpeStatusResponse;
	}
	@Async("asyncExecutor")
	public CompletableFuture getSdwanCpeInfoFromCiscoResponseAsync(CiscoBulkCpeResponse versaCpeStatusResponse,
			SdwanCpeDetails sdwanCpeDetail, Map<String, String> status, SdwanSiteDetails sdwanSiteDetail,
			Map<String, List<SdwanEndpoints>> instanceByCode, Map<String, String> links) {
		LOGGER.info("Entering wanStatusAsync by thread id {}, name {}", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		izoSdwanCiscoService.getSdwanCpeInfoFromCiscoResponse(versaCpeStatusResponse, sdwanCpeDetail, status,
				sdwanSiteDetail, instanceByCode,links);
			sdwanCpeDetail.setCpeAvailability(status.get(ServiceInventoryConstants.CPE_AVAILABILITY));
			sdwanCpeDetail.setCpeStatus(status.get(ServiceInventoryConstants.CPE_STATUS));
			if(Objects.nonNull(sdwanSiteDetail.getSdwanCpeDetails()))
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
		LOGGER.info("Exiting wanStatusAsync by thread id {}, name {}", Thread.currentThread().getId(),
				Thread.currentThread().getName());
		return CompletableFuture.completedFuture("SUCCESS");
	}
}
