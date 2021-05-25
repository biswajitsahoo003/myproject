package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.AuditLog;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SiteBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SiteFunctionsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SitesBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SitesResBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.delegates.gsc.CircuitGroupsResponseBean;
import com.tcl.dias.servicefulfillmentutils.delegates.gsc.CreateSiteResponseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional(readOnly = false)
public class RepcService {
	
	@Autowired
	RestClientService restClientService;

	@Autowired
	GscService gscService;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Value("${gsc.getsite.url}")
	private String getSiteUrl;	
	
	@Value("${gsc.getsite.authorization}")
	private String getSiterAuthorization;
	
	@Value("${gsc.createcircuitgroup.url}")
	private String createCircuitGroupUrl;	
	
	@Value("${gsc.createcircuitgroup.authorization}")
	private String createCircuitGroupAuthorization;
	
	private static final Logger logger = LoggerFactory.getLogger(RepcService.class);
	
	public boolean requestForGetSites(String serviceCode, Integer serviceId, String customerId,
			String processInstanceId) throws TclCommonException {
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		header.set("Authorization", getSiterAuthorization);
		header.set("Host", "");
		Boolean result = false;

		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("customerId", customerId);
		if (!inputParams.isEmpty()) {
			logger.info("Get Sites - request {}", inputParams);
			AuditLog auditLog = gscService.saveAuditLog(Utils.convertObjectToJson(inputParams), null, serviceCode,
					"GetSites", processInstanceId);
			RestResponse response = restClientService.getWithQueryParam(getSiteUrl, inputParams, header);
			logger.info("Get Sites - response {}", response);
			gscService.updateAuditLog(auditLog, Utils.toJson(response));
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				// @SuppressWarnings("unused")
				SitesBean sitesBean = Utils.fromJson(response.getData(), new TypeReference<SitesBean>() {
				});
				SitesBean filteredSites=new SitesBean();
				List<SiteBean> totalSiteList = new ArrayList<>();
				totalSiteList = sitesBean.getSites();
				for (SiteBean siteBean : totalSiteList) {
					if (siteBean.getSiteFunctions().size() == 2) {
						Boolean isVOG = false;
						Boolean isVOL = false;
						for (SiteFunctionsBean siteFunction : siteBean.getSiteFunctions()) {
							if (siteFunction.getSiteFunctionCd().equalsIgnoreCase("VOG")) {
								isVOG = true;
							} else if (siteFunction.getSiteFunctionCd().equalsIgnoreCase("VOL")) {
								isVOL = true;
							}
						}
						if (isVOG && isVOL) {
							filteredSites.getSites().add(siteBean);
						}
					}
				}
				gscService.persistGetSitesResponse(serviceId, Utils.convertObjectToJson(filteredSites));
				logger.info("List of Valid Sites { } ", filteredSites);
				result = true;
				return result;
			} else {
				try {
					logger.info("Get Sites - error log started");
					componentAndAttributeService.updateAdditionalAttributes(serviceId, "GetSitesCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(),
									response.getStatus().toString()),
							AttributeConstants.ERROR_MESSAGE, "gsc-select-sites");
				} catch (Exception e) {
					logger.error("Get Sites - error message details {}", e);
				} finally {
					return result;
				}
			}
		}

		return result;
	}

	public String requestForCreateNewSite(String custRequest, Integer serviceId, String serviceCode,
			String processInstanceId) throws TclCommonException {
		String status = "failed";
		String siteId=null;
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		header.set("Authorization", getSiterAuthorization);
		header.set("Host", "");
		logger.info("Create New Site in REPC - request {}", custRequest);
		RestResponse response = restClientService.post(getSiteUrl, custRequest, header);
		logger.info("Create New Site in REPC - response {}", response);
		AuditLog auditLog = gscService.saveAuditLog(custRequest, Utils.convertObjectToJson(response.getData()),
				serviceCode, "SiteCreation", processInstanceId);
		gscService.updateAuditLog(auditLog, Utils.toJson(response));
		if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
			CreateSiteResponseBean createSiteResponseBean = Utils.fromJson(response.getData(),
					new TypeReference<CreateSiteResponseBean>() {});
			if ("SUCCESS".equalsIgnoreCase(createSiteResponseBean.getSites().get(0).getStatus())) {
				List<SitesResBean> siteResList = createSiteResponseBean.getSites();
				for (SitesResBean sitesResBean : siteResList) {
					siteId = sitesResBean.getSiteAbbr();
				}
				gscService.persistCreateNewSiteResponse(serviceId, custRequest, response.getData(), siteId);
				return "success";
			}
		} else {
			try {
				logger.info("Create New Site in REPC - error log started");
				componentAndAttributeService.updateAdditionalAttributes(serviceId,
						"CreateNewSiteInRepcCallFailureReason",
						componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(),
								response.getStatus().toString()),
						AttributeConstants.ERROR_MESSAGE, "gsc-site-selection-creation");
			} catch (Exception e) {
				logger.error("Create New Site in REPC - error message details {}", e);
			}
		}
		return status;
	}

	public String requestForCreateCircuitGroup(String custRequest, Integer serviceId, String serviceCode, String processInstanceId) throws TclCommonException {
		String status = "failed";
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		header.set("Authorization", createCircuitGroupAuthorization);
		header.set("Host", "");
		logger.info("Create Circuit Group in REPC - request {}", custRequest);
		AuditLog auditLog = gscService.saveAuditLog(custRequest, custRequest,
				serviceCode, "CreateCircuitGroup", processInstanceId);
		RestResponse response = restClientService.postWithProxy(createCircuitGroupUrl, custRequest, header);
		logger.info("Create Circuit Group in REPC - response {}", response);
		gscService.updateAuditLog(auditLog, Utils.toJson(response));
		if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
			CircuitGroupsResponseBean circuitGroupsResponseBean = Utils.fromJson(response.getData(),
					new TypeReference<CircuitGroupsResponseBean>() {});
			gscService.persistCreateCircuitGroupResponse(serviceId, Utils.convertObjectToJson(circuitGroupsResponseBean.getCircuitGroups()));
			return "success";
		} else {
			try {
				logger.info("Create Circuit Group in REPC - error log started");
				componentAndAttributeService.updateAdditionalAttributes(serviceId,
						"CircuitGroupInRepcCallFailureReason",
						componentAndAttributeService.getErrorMessageDetails(response.getErrorMessage(),
								response.getStatus().toString()),
						AttributeConstants.ERROR_MESSAGE, "gsc-circuit-group-creation");
			} catch (Exception e) {
				logger.error("Create Circuit Group in REPC - error message details {}", e);
			}
		}
		return status;
	}
}
