package com.tcl.dias.sfdc.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.sfdc.response.bean.*;
import com.tcl.dias.common.beans.EtcRecordListBean;
import com.tcl.dias.common.beans.TerminationWaiverBean;
import com.tcl.dias.common.beans.WaiverResponseBean;
import com.tcl.dias.sfdc.bean.SfdcUpdateWaiverRequest;
import com.tcl.dias.sfdc.bean.SfdcWaiverRequest;
import com.tcl.dias.sfdc.component.SfdcUpdateWaiverMapper;
import com.tcl.dias.sfdc.response.bean.EtcRecordList;
import com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean;
import com.tcl.dias.common.sfdc.bean.PartnerEntityContactRequestBean;
import com.tcl.dias.common.sfdc.response.bean.*;
import com.tcl.dias.sfdc.bean.SfdcPartnerEntityContactRequest;
import com.tcl.dias.sfdc.response.bean.SfdcPartnerEntityContactResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import com.google.common.collect.ImmutableMap;
import com.tcl.dias.common.beans.COPFIDOmsList;
import com.tcl.dias.common.beans.COPFIdRecord;
import com.tcl.dias.common.beans.COPFOmsAttribute;
import com.tcl.dias.common.beans.COPFOmsRequest;
import com.tcl.dias.common.beans.COPFOmsResponse;
import com.tcl.dias.common.beans.COPFRequest;
import com.tcl.dias.common.beans.COPFResponse;
import com.tcl.dias.common.beans.MDMAddress;
import com.tcl.dias.common.beans.MDMContact;
import com.tcl.dias.common.beans.MDMContactMethod;
import com.tcl.dias.common.beans.MDMOmsRequestBean;
import com.tcl.dias.common.beans.MDMOmsResponseBean;
import com.tcl.dias.common.beans.MDMOmsSrcKeys;
import com.tcl.dias.common.beans.MDMRequestBean;
import com.tcl.dias.common.beans.MDMResponseBean;
import com.tcl.dias.common.beans.MDMSrcKeys;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SfdcOpportunityInfo;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.sfdc.bean.BCROmsRecord;
import com.tcl.dias.common.sfdc.bean.BCROmsRequest;
import com.tcl.dias.common.sfdc.bean.BCROmsResponse;
import com.tcl.dias.common.sfdc.bean.BCROptyProperitiesBean;
import com.tcl.dias.common.sfdc.bean.BCRRequest;
import com.tcl.dias.common.sfdc.bean.BCRResponse;
import com.tcl.dias.common.sfdc.bean.BcrRecord;
import com.tcl.dias.common.sfdc.bean.CreateRequestV1;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponse;
import com.tcl.dias.common.sfdc.bean.CreditCheckQueryResponseBean;
import com.tcl.dias.common.sfdc.bean.CustomerContractingEntityBean;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OwnerRegionQueryResponseBean;
import com.tcl.dias.common.sfdc.bean.ProductServicesQueryBean;
import com.tcl.dias.common.sfdc.bean.ProductServicesRecord;
import com.tcl.dias.common.sfdc.bean.ServiceRequestBean;
import com.tcl.dias.common.sfdc.bean.SfdcCreditCheckQueryRequest;
import com.tcl.dias.common.sfdc.bean.UpdateRequstV1;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.service.impl.NotificationService;
import com.tcl.dias.sfdc.bean.DealRegistrationRequestBean;
import com.tcl.dias.sfdc.bean.DealRegistrationResponse;
import com.tcl.dias.sfdc.bean.SfdcOwnerRegionQueryResponseBean;
import com.tcl.dias.sfdc.bean.SfdStagingBean;
import com.tcl.dias.sfdc.bean.SfdcCampaignReponseBean;
import com.tcl.dias.sfdc.bean.SfdcCreditCheckQueryRequestBean;
import com.tcl.dias.sfdc.bean.SfdcCreditCheckQueryResponse;
import com.tcl.dias.sfdc.bean.SfdcCreditCheckQueryResponseBean;
import com.tcl.dias.sfdc.bean.SfdcDeleteProductBean;
import com.tcl.dias.sfdc.bean.SfdcFeasibilityRequestBean;
import com.tcl.dias.sfdc.bean.SfdcFeasibilityUpdateRequestBean;
import com.tcl.dias.sfdc.bean.SfdcOpportunityBundleRequest;
import com.tcl.dias.sfdc.bean.SfdcOpportunityRequest;
import com.tcl.dias.sfdc.bean.SfdcOwnerRegionQueryResponse;
import com.tcl.dias.sfdc.bean.SfdcPartnerEntityRequest;
import com.tcl.dias.sfdc.bean.SfdcPartnerOpportunityRequest;
import com.tcl.dias.sfdc.bean.SfdcProductServiceBean;
import com.tcl.dias.sfdc.bean.SfdcSalesFunnelRequestBean;
import com.tcl.dias.sfdc.bean.SfdcSalesFunnelResponse;
import com.tcl.dias.sfdc.bean.SfdcSiteSolutionBean;
import com.tcl.dias.sfdc.bean.SfdcUpdateProductServiceBean;
import com.tcl.dias.sfdc.constants.MapperConstants;
import com.tcl.dias.sfdc.constants.SfdcConstants;
import com.tcl.dias.sfdc.factory.SfdcMapperFactory;
import com.tcl.dias.sfdc.mapper.ISfdcMapper;
import com.tcl.dias.sfdc.response.bean.SfdcAccessToken;
import com.tcl.dias.sfdc.response.bean.SfdcAttributes;
import com.tcl.dias.sfdc.response.bean.SfdcBundleOpportunity;
import com.tcl.dias.sfdc.response.bean.SfdcFeasibilityResponseBean;
import com.tcl.dias.sfdc.response.bean.SfdcOpportunity;
import com.tcl.dias.sfdc.response.bean.SfdcOpportunityBundleResponseBean;
import com.tcl.dias.sfdc.response.bean.SfdcOpportunityResponseBean;
import com.tcl.dias.sfdc.response.bean.SfdcPartnerEntityResponse;
import com.tcl.dias.sfdc.response.bean.SfdcPartnerOppertunityResponse;
import com.tcl.dias.sfdc.response.bean.SfdcProductServicesResponseBean;
import com.tcl.dias.sfdc.response.bean.SfdcStagingResponseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import static com.tcl.dias.common.utils.Utils.objectMapper;

/**
 * SfdcService Class is used for all the functionalities related to sfdc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class SfdcService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SfdcService.class);

	@Value("${sfdc.end.point}")
	private String endPointUrl;

	@Value("${sfdc.create.opportunity}")
	private String createOpportunityUrl;

	@Value("${sfdc.get.opportunity}")
	private String getOpportunityUrl;

	@Value("${sfdc.product.request}")
	private String productUrl;

	@Value("${sfdc.delete.request}")
	private String deleteProductUrl;

	@Value("${sfdc.update.stage}")
	private String stageUrl;

	@Value("${sfdc.site.update}")
	private String siteUrl;

	@Value("${sfdc.create.feasibility.url}")
	private String createFeasUrl;

	@Value("${rabbitmq.opportunity.response.create}")
	String opportunityResponseQueue;

	@Value("${rabbitmq.opportunity.response.site}")
	String siteResponseQueue;

	@Value("${rabbitmq.opportunity.response.productservices}")
	String productResponseQueue;

	@Value("${rabbitmq.sfdc.feasibility.response}")
	String feasibilityResponseQueue;

	@Value("${rabbitmq.opportunity.productservices.update}")
	String updateProductResponseQueue;

	@Value("${rabbitmq.opportunity.response.update}")
	String stagingResponseQueue;

	@Value("${sfdc.auth.token}")
	String sfdcAuthTokenEndPoint;

	@Value("${sfdc.client}")
	String clientId;

	@Value("${sfdc.secret}")
	String clientSecret;

	@Value("${sfdc.grant.type}")
	String grantType;

	@Value("${sfdc.username}")
	String userName;

	@Value("${sfdc.password}")
	String password;

	@Value("${sfdc.auth}")
	String auth;

	@Value("${mdm.auth.key}")
	String mdmAuthKey;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MQUtils mQUtils;

	@Autowired
	RestClientService restClientService;

	@Autowired
	private SfdcMapperFactory factory;

	@Value("${sfdc.process.bcr}")
	String processBcrResponseQueue;

	@Value("${sfc.bcr.url}")
	String bcrUrl;

	@Value("${sfdc.create.ptr.entity.url}")
	private String createPartnerEntityUrl;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Value("${rabbitmq.create.partner.entity.response.update}")
	String partnerEntityCreateResponseUpdate;

	@Value("${mdm.process.response}")
	String processMDMResponseQueue;

	@Value("${mdm.end.point}")
	private String mdmUrl;

	@Value("${sfdc.copf.url}")
	private String copfIdUrl;

	@Value("${sfdc.process.copf.response}")
	String processCopfIdResponseQueue;

	@Value("${sfdc.get.sales.funnel.url}")
	private String getSalesFunnelUrl;
	
	@Value("${sfdc.get.credit.check.query.url}")
	String getCreditCheckQueryUrl;
	
	@Value("${sfdc.credit.check.response}")
	String sfdcCreditCheckResponseQueue;

	@Value("${sfdc.create.opportunity.dealregistration}")
	private String partnerDealregistrationUrl;
	
	@Value("${sfdc.end.point}")
	String getOwnerRegionUrl;

	@Value("${sfdc.create.partner.entity.contact.url}")
	private String createPartnerEntityContactUrl;

	@Value("${rabbitmq.create.partner.entity.contact.response.update}")
	String partnerEntityContactCreateResponseUpdate;

	@Value("${sfdc.account.create.request.url}")
	String sfdcAccountCreationRequestUrl;

	@Value("${sfdc.account.update.request.url}")
	String sfdcAccountUpdateRequestUrl;
	
	@Value("${sfdc.waiver.create}")
	private String waiverUrl;

	@Value("${rabbitmq.sfdc.waiver.response.create}")
	String waiverResponseQueue;

	@Value("${rabbitmq.sfdc.waiver.response.update}")
	String waiverResponseUpdateQueue;

	/**
	 * 
	 * getSfdcOpportunityInfo is used to get SFDC opty info
	 * 
	 * @param opportunityId
	 * @return
	 * @throws TclCommonException
	 */
	public SfdcOpportunityInfo getSfdcOpportunityInfo(String opportunityId) throws TclCommonException {
		LOGGER.info("opportunity get info id {}", opportunityId);
		String url = String.format("%s%s?opportunityId=%s", endPointUrl, getOpportunityUrl, opportunityId);
		SfdcAccessToken accessToken = getAuthToken();
		String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE + accessToken.getAccessToken();
		RestResponse sfdcResponse = restClientService.get(url,
				ImmutableMap.of(SfdcConstants.AUTHORIZATION.toString(), authAccessToken), true);
		if (sfdcResponse.getStatus() == Status.SUCCESS) {
			SfdcOpportunityResponseBean opportunityBean = (SfdcOpportunityResponseBean) Utils
					.convertJsonToObject(sfdcResponse.getData(), SfdcOpportunityResponseBean.class);
			SfdcOpportunityInfo opportunityInfo = new SfdcOpportunityInfo();
			opportunityInfo.setOpportunityId(opportunityId);
			opportunityInfo.setStageName(opportunityBean.getOpportunity().getStageName());
			return opportunityInfo;
		} else {
			throw new RuntimeException(String.format("Error occurred while getting opportunity from SFDC: %s",
					sfdcResponse.getErrorMessage()));
		}
	}

	/**
	 * 
	 *
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public void processSdfcOpportunity(String input) throws TclCommonException {
		try {
			if(input.contains(CommonConstants.SDWAN)) {
				ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.BUNDLE_OPPORTUNITY.toString());
				SfdcOpportunityBundleRequest sfdcRequest=(SfdcOpportunityBundleRequest) mapper.transfortToSfdcRequest(input);
				processBundleSdfcOpportunity(sfdcRequest);
				
			}else {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.OPPORTUNITY_MAPPER.toString());
			SfdcOpportunityRequest sfdcRequest = (SfdcOpportunityRequest) mapper.transfortToSfdcRequest(input);
			String request = Utils.convertObjectToJson(sfdcRequest);
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			LOGGER.info("opportunity create request {}", request);
			String url = endPointUrl + createOpportunityUrl;
			RestResponse sfdcReponse = restClientService.post(url, request, null, null, authHeader);
			LOGGER.info("opportunity create response {}", sfdcReponse);

			if (sfdcReponse.getStatus() == Status.SUCCESS) {
				SfdcOpportunityResponseBean sfdcOpportunity = (SfdcOpportunityResponseBean) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcOpportunityResponseBean.class);
				if (sfdcOpportunity.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					OpportunityResponseBean opBean = constructOpportunityRespone(sfdcOpportunity);
					if(Objects.nonNull(sfdcRequest)){
						opBean.setQuoteToLeId(sfdcRequest.getQuoteToLeId());
					}
					LOGGER.info("opportunity response to queue {}", opBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcOpportunity {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));

				} else {
					LOGGER.info("Retrying with 4 secs {}", request);
					sfdcRetryCheck(request, authHeader, sfdcRequest);

				}
			} else {
				LOGGER.info("Retrying with 4 secs {}", request);
				sfdcRetryCheck(request, authHeader, sfdcRequest);
			}
		}
		} catch (Exception e) {
			LOGGER.error("Error in process Opportunity engine ", e);
		}

	}

	/**
	 * sfdcRetryCheck
	 *
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcRetryCheck(String request, Map<String, String> authHeader, SfdcOpportunityRequest sfdcRequest)
			throws InterruptedException, TclCommonException {
		RestResponse sfdcReponse;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + createOpportunityUrl;
		sfdcReponse = restClientService.post(url, request, null, null, authHeader);
		LOGGER.info("opportunity sfdc {}", sfdcReponse);

		if (sfdcReponse.getStatus() == Status.SUCCESS) {
			SfdcOpportunityResponseBean sfdcOpportunity = (SfdcOpportunityResponseBean) Utils
					.convertJsonToObject(sfdcReponse.getData(), SfdcOpportunityResponseBean.class);
			if (sfdcOpportunity.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				OpportunityResponseBean opBean = constructOpportunityRespone(sfdcOpportunity);
				// for teamsdr
				if(Objects.nonNull(sfdcRequest.getQuoteToLeId())){
					opBean.setQuoteToLeId(sfdcRequest.getQuoteToLeId());
				}
				LOGGER.info("opportunity response to queue {}", opBean);
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
			} else {
				LOGGER.info("opportunity update error in updating stage {} ", sfdcReponse);
				notificationService.notifySfdcError(sfdcReponse.toString(), request, url, authHeader.toString());
				OpportunityResponseBean opBean = new OpportunityResponseBean();
				opBean.setError(true);
				opBean.setErrorMessage(sfdcReponse.getData());
				opBean.setRefId(sfdcRequest.getCreateRequestV1().getOpportunity().getPortalTransactionIdC());
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
			}

		} else {
			notificationService.notifySfdcError(sfdcReponse.toString(), request, url, authHeader.toString());
			OpportunityResponseBean opBean = new OpportunityResponseBean();
			opBean.setError(true);
			opBean.setErrorMessage(sfdcReponse.getData());
			opBean.setRefId(sfdcRequest.getCreateRequestV1().getOpportunity().getPortalTransactionIdC());
			LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
		}
	}

	/**
	 * 
	 *
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */

	public SfdcAccessToken getAuthToken() throws TclCommonException {
		SfdcAccessToken authToken = new SfdcAccessToken();

		try {
			LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
			formBody.add(SfdcConstants.CLIENT_ID.toString(), clientId);
			formBody.add(SfdcConstants.CLIENT_SECRET.toString(), clientSecret);
			formBody.add(SfdcConstants.GRANT_TYPE.toString(), grantType);
			formBody.add(SfdcConstants.PASSWORD.toString(), password);
			formBody.add(SfdcConstants.USERNAME.toString(), userName);
			LOGGER.info("token request for sfdc {}", formBody);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), auth);
			RestResponse response = restClientService.post(sfdcAuthTokenEndPoint, formBody, authHeader);
			if (response.getStatus() == Status.SUCCESS) {
				LOGGER.info("token respone from sfdc {}", response);
				authToken = (SfdcAccessToken) Utils.convertJsonToObject(response.getData(), SfdcAccessToken.class);
			} else {
				retryAuthToken(authToken);
			}

		} catch (Exception e) {
			LOGGER.error("Error in getting access token", e);
		}
		return authToken;

	}

	/**
	 * retryAuthToken
	 */
	private SfdcAccessToken retryAuthToken(SfdcAccessToken authToken) {
		try {
			TimeUnit.SECONDS.sleep(4);
			LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
			formBody.add(SfdcConstants.CLIENT_ID.toString(), clientId);
			formBody.add(SfdcConstants.CLIENT_SECRET.toString(), clientSecret);
			formBody.add(SfdcConstants.GRANT_TYPE.toString(), grantType);
			formBody.add(SfdcConstants.PASSWORD.toString(), password);
			formBody.add(SfdcConstants.USERNAME.toString(), userName);
			LOGGER.info("token request for sfdc {}", formBody);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), auth);
			RestResponse response = restClientService.post(sfdcAuthTokenEndPoint, formBody, authHeader);
			if (response.getStatus() == Status.SUCCESS) {
				LOGGER.info("token respone from sfdc {}", response);
				authToken = (SfdcAccessToken) Utils.convertJsonToObject(response.getData(), SfdcAccessToken.class);
			} else {
				LOGGER.info("error token respone from sfdc {}", response);
				notificationService.notifySfdcError(response.toString(), formBody.toString(), sfdcAuthTokenEndPoint,
						authHeader.toString());
			}

		} catch (Exception e) {
			LOGGER.error("Error in getting access token", e);
		}
		return authToken;
	}

	/**
	 * 
	 *
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */

	public void processSdfcSite(String input) throws TclCommonException {

		try {

			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.SITE_MAPPER.toString());
			SfdcSiteSolutionBean respone = (SfdcSiteSolutionBean) mapper.transfortToSfdcRequest(input);
			String request = Utils.convertObjectToJson(respone);
			SfdcAccessToken accessToken = getAuthToken();
			LOGGER.info("createSite request" + request);
			String authenticationAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authenticationAccessToken);
			RestResponse response = restClientService.post(endPointUrl + siteUrl, request, null, null, authHeader);
			LOGGER.info("site sfdc respone {}", response);

			if (response.getStatus() == Status.SUCCESS) {
				com.tcl.dias.sfdc.response.bean.SiteResponseBean siteResponse = (com.tcl.dias.sfdc.response.bean.SiteResponseBean) Utils
						.convertJsonToObject(response.getData(),
								com.tcl.dias.sfdc.response.bean.SiteResponseBean.class);
				if (siteResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					SiteResponseBean siteResponseBean = constructSiteResponeAndSendToQueue(siteResponse);
					siteResponseBean.setIsCancel(respone.getIsCancel());
					siteResponseBean.setProdSolutionCode(respone.getProductSolutionCode());
					LOGGER.info("site  respone to queue {}", siteResponseBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcSite {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(siteResponseQueue, Utils.convertObjectToJson(siteResponseBean));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcSiteRetryCheck(request, authHeader, respone.getOpportunityID(),	respone.getIsCancel(),respone.getProductSolutionCode());
				}
			} else {
				notificationService.notifySfdcError(response.toString(), request, endPointUrl + siteUrl,
						authHeader.toString());
				SiteResponseBean siteResponseBean = new SiteResponseBean();
				siteResponseBean.setError(true);
				siteResponseBean.setErrorMessage(response.getData());
				siteResponseBean.setIsCancel(respone.getIsCancel());
				siteResponseBean.setProdSolutionCode(respone.getProductSolutionCode());
				siteResponseBean.setOpportunityId(respone.getOpportunityID());
				LOGGER.info("MDC Filter token value in before Queue call sfdcSiteRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(siteResponseQueue, Utils.convertObjectToJson(siteResponseBean));
			}

		} catch (Exception e) {
			LOGGER.error("Error in process Sdfc site", e);
		}

	}

	/**
	 * sfdcSiteRetryCheck
	 * 
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcSiteRetryCheck(String request, Map<String, String> authHeader, String sfdcId,Boolean isCancel,String prodSolCode)
			throws InterruptedException, TclCommonException {
		RestResponse response;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + siteUrl;
		response = restClientService.post(url, request, null, null, authHeader);
		LOGGER.info("site sfdc respone {}", response);

		if (response.getStatus() == Status.SUCCESS) {
			com.tcl.dias.sfdc.response.bean.SiteResponseBean siteResponse = (com.tcl.dias.sfdc.response.bean.SiteResponseBean) Utils
					.convertJsonToObject(response.getData(), com.tcl.dias.sfdc.response.bean.SiteResponseBean.class);
			if (siteResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				SiteResponseBean siteResponseBean = constructSiteResponeAndSendToQueue(siteResponse);
				siteResponseBean.setIsCancel(isCancel);
				siteResponseBean.setProdSolutionCode(prodSolCode);
				LOGGER.info("site  respone to queue {}", siteResponseBean);
				LOGGER.info("MDC Filter token value in before Queue call sfdcSiteRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(siteResponseQueue, Utils.convertObjectToJson(siteResponseBean));
			} else {
				notificationService.notifySfdcError(response.toString(), request, url, authHeader.toString());
				SiteResponseBean siteResponseBean = new SiteResponseBean();
				siteResponseBean.setError(true);
				siteResponseBean.setErrorMessage(response.getData());
				siteResponseBean.setOpportunityId(sfdcId);
				siteResponseBean.setIsCancel(isCancel);
				siteResponseBean.setProdSolutionCode(prodSolCode);
				LOGGER.info("MDC Filter token value in before Queue call sfdcSiteRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(siteResponseQueue, Utils.convertObjectToJson(siteResponseBean));
			}
		} else {
			notificationService.notifySfdcError(response.toString(), request, url, authHeader.toString());
			SiteResponseBean siteResponseBean = new SiteResponseBean();
			siteResponseBean.setError(true);
			siteResponseBean.setErrorMessage(response.getData());
			siteResponseBean.setIsCancel(isCancel);
			siteResponseBean.setProdSolutionCode(prodSolCode);
			siteResponseBean.setOpportunityId(sfdcId);
			LOGGER.info("MDC Filter token value in before Queue call sfdcSiteRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(siteResponseQueue, Utils.convertObjectToJson(siteResponseBean));
		}
	}

	/**
	 * 
	 *
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public void processSdfcProduct(String input) throws TclCommonException {

		try {
			LOGGER.info("Input service request to sfdc {}", input);
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PROCESS_MAPPER.toString());
			SfdcProductServiceBean response = (SfdcProductServiceBean) mapper.transfortToSfdcRequest(input);
			SfdcAccessToken accessToken = getAuthToken();
			String authAccessToken="";
			 authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
				if(input.contains("IPVPN") || input.contains("Managed Services")) {
					authAccessToken = SfdcConstants.AUTH.toString() + CommonConstants.SPACE
							+ accessToken.getAccessToken();
				}
			String request = Utils.convertObjectToJson(response);

			LOGGER.info("Create product service request to sfdc :: {}", request);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse sfdcResponse = restClientService.post(endPointUrl + productUrl, request, null, null,
					authHeader);
			LOGGER.info("Create product service response from sfdc :: {}", sfdcResponse);

			if (sfdcResponse.getStatus() == Status.SUCCESS) {
				SfdcProductServicesResponseBean productServiceResponse = (SfdcProductServicesResponseBean) Utils
						.convertJsonToObject(sfdcResponse.getData(), SfdcProductServicesResponseBean.class);
				if (productServiceResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					ProductServicesResponseBean siteResponseBean = constructProductResponeAndSendToQueue(
							productServiceResponse);
					siteResponseBean.setIsCancel(response.getCreateRequestV1().getIsCancel());
					siteResponseBean.setSfdcid(response.getCreateRequestV1().getOpportunityId());
					siteResponseBean.setProductSolutionCode(response.getProductSolutionCode());
					siteResponseBean.setType("create");
					siteResponseBean.setQuoteToLeId(response.getQuoteLeId());
					siteResponseBean.setProductSolutionCode(response.getProductSolutionCode());
					if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
						siteResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
					}
					LOGGER.info("MDC Filter token value in before Queue call processSdfcProduct {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(productResponseQueue, Utils.convertObjectToJson(siteResponseBean));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcProductRetryCheck(response, request, authHeader, response.getProductSolutionCode(),
							response.getCreateRequestV1().getOpportunityId());
				}
			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, endPointUrl + productUrl,
						authHeader.toString());
				ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
				prodResponseBean.setError(true);
				prodResponseBean.setIsCancel(response.getCreateRequestV1().getIsCancel());
				prodResponseBean.setProductSolutionCode(response.getProductSolutionCode());
				prodResponseBean.setErrorMessage(sfdcResponse.getData());
				prodResponseBean.setSfdcid(response.getCreateRequestV1().getOpportunityId());
				prodResponseBean.setType("create");
				LOGGER.info("MDC Filter token value in before Queue call sfdcProductRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
			}

		} catch (Exception e) {
			LOGGER.error("error in processing product service sfdc", e);
		}

	}

	/**
	 * used to process create feasibility request for sfdc and call the sfdc api
	 * 
	 * @param request
	 */
	public void createFeasibility(String request) {

		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.FEASIBILITY_MAPPER.toString());
			FeasibilityRequestBean requestBean = (FeasibilityRequestBean) Utils.convertJsonToObject(request,
					FeasibilityRequestBean.class);
			SfdcFeasibilityRequestBean sfdcFeasibilityRequestBean;
			SfdcFeasibilityUpdateRequestBean sfdcFeasibilityUpdateBean;
			String feasibilityRequest = "";
			SfdcAccessToken accessToken = getAuthToken();
			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse sfdcResponse = null;
			if (requestBean.getCreateOrUpdate().equalsIgnoreCase("create")) {
				sfdcFeasibilityRequestBean = (SfdcFeasibilityRequestBean) mapper.transfortToSfdcRequest(request);
				feasibilityRequest = Utils.convertObjectToJson(sfdcFeasibilityRequestBean);
				LOGGER.info("create/update feasibility request to sfdc {}", feasibilityRequest);
				sfdcResponse = restClientService.post(endPointUrl + createFeasUrl, feasibilityRequest, null, null,
						authHeader);
			} else {
				sfdcFeasibilityUpdateBean = (SfdcFeasibilityUpdateRequestBean) mapper.transfortToSfdcRequest(request);
				if (sfdcFeasibilityUpdateBean != null && !sfdcFeasibilityUpdateBean.getUpdateRequestV1().isEmpty()
						&& sfdcFeasibilityUpdateBean.getUpdateRequestV1().get(0) != null
						&& sfdcFeasibilityUpdateBean.getUpdateRequestV1().get(0).getFReq() != null && StringUtils
								.isNotEmpty(sfdcFeasibilityUpdateBean.getUpdateRequestV1().get(0).getFReq().getId())) {
					feasibilityRequest = Utils.convertObjectToJson(sfdcFeasibilityUpdateBean);
					LOGGER.info("create/update feasibility request to sfdc {}", feasibilityRequest);
					sfdcResponse = restClientService.put(endPointUrl + createFeasUrl, feasibilityRequest, authHeader);
				} else {
					FeasibilityResponseBean feasResponseBean = new FeasibilityResponseBean();
					feasResponseBean.setSfdcServiceJobId(requestBean.getSfdcServiceJobId());
					feasResponseBean.setError(true);
					feasResponseBean.setMessage("Create feasibility Id is empty");
					LOGGER.info("MDC Filter token value in before Queue call createFeasibility {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(feasibilityResponseQueue, Utils.convertObjectToJson(feasResponseBean));

				}
			}
			LOGGER.info("create/update feasibility response to sfdc {}", sfdcResponse);

			if (sfdcResponse != null) {
				if (sfdcResponse.getStatus() == Status.SUCCESS) {

					SfdcFeasibilityResponseBean feasibilityResponse = (SfdcFeasibilityResponseBean) Utils
							.convertJsonToObject(sfdcResponse.getData(), SfdcFeasibilityResponseBean.class);
					if (feasibilityResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
						FeasibilityRequestBean feasibilityRequestBean = (FeasibilityRequestBean) Utils
								.convertJsonToObject(request, FeasibilityRequestBean.class);
						FeasibilityResponseBean feasibilityResponseBean = constructFeasibilityResponse(
								feasibilityRequestBean.getSfdcServiceJobId(), feasibilityRequestBean.getSiteId(),
								feasibilityResponse);
						LOGGER.info("MDC Filter token value in before Queue call createFeasibility {} :",
								MDC.get(CommonConstants.MDC_TOKEN_KEY));
						mQUtils.send(feasibilityResponseQueue, Utils.convertObjectToJson(feasibilityResponseBean));

					} else {
						LOGGER.info("Retrying with 3 secs {}", request);
						sfdcRetryCreateFeasibility(request, feasibilityRequest, authHeader,
								requestBean.getCreateOrUpdate());
					}
				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcRetryCreateFeasibility(request, feasibilityRequest, authHeader,
							requestBean.getCreateOrUpdate());
				}
			}

		} catch (Exception e) {
			LOGGER.error("error in processing create feasibility for sfdc", e);
		}

	}

	private FeasibilityResponseBean constructFeasibilityResponse(Integer sfdcServiceJobId, Integer siteId,
			SfdcFeasibilityResponseBean sfdcResponseBean) {
		FeasibilityResponseBean responseBean = new FeasibilityResponseBean();
		responseBean.setfReqResponseList(new ArrayList<FResponse>());
		responseBean.setCustomReqId(sfdcResponseBean.getCustomReqId());
		responseBean.setErrorcode(sfdcResponseBean.getErrorcode());
		responseBean.setMessage(sfdcResponseBean.getMessage());
		responseBean.setStatus(sfdcResponseBean.getStatus());
		responseBean.setSfdcServiceJobId(sfdcServiceJobId);
		responseBean.setSiteId(siteId);

		if (sfdcResponseBean.getFReqResponseList() != null && !sfdcResponseBean.getFReqResponseList().isEmpty()) {
			sfdcResponseBean.getFReqResponseList().stream().forEach(sfdcFres -> {
				FResponse fResponse = new FResponse();
				Attributes attributes = new Attributes();
				attributes.setType(sfdcFres.getAttributes().getType());
				attributes.setUrl(sfdcFres.getAttributes().getUrl());
				fResponse.setAttributes(attributes);
				fResponse.setAddressAEndC(sfdcFres.getAddressAEndC());
				fResponse.setADDRESSLINE1AEndC(sfdcFres.getADDRESSLINE1AEndC());
				fResponse.setAvailableTelecomPRIProviderAEndC(sfdcFres.getAvailableTelecomPRIProviderAEndC());
				fResponse.setCityAEndC(sfdcFres.getCityAEndC());
				fResponse.setContinentAEndC(sfdcFres.getContinentAEndC());
				fResponse.setCountryAEndC(sfdcFres.getCountryAEndC());
				fResponse.setId(sfdcFres.getId());
				fResponse.setOnlineILLAutoC(sfdcFres.getOnlineILLAutoC());
				fResponse.setOtherPOPAEndC(sfdcFres.getOtherPOPAEndC());
				fResponse.setPinZipAEndC(sfdcFres.getPinZipAEndC());
				fResponse.setPortCircuitCapacityC(sfdcFres.getPortCircuitCapacityC());
				fResponse.setProductsServicesC(sfdcFres.getProductsServicesC());
				fResponse.setRecordTypeId(sfdcFres.getRecordTypeId());
				fResponse.setRequestTypeC(sfdcFres.getRequestTypeC());
				fResponse.setStateAEndC(sfdcFres.getStateAEndC());
				fResponse.setStatus(sfdcFres.getStatus());
				fResponse.setCloudEnablementC(sfdcFres.getCloudEnablementC());
				fResponse.setInterfaceC(sfdcFres.getInterfaceC());
				fResponse.setSiteContactNameAEndC(sfdcFres.getSiteContactNameAEndC());
				fResponse.setSiteLocalContactNumberAEndC(sfdcFres.getSiteLocalContactNumberAEndC());
				responseBean.getfReqResponseList().add(fResponse);
			});
		}

		return responseBean;
	}

	private void sfdcRetryCreateFeasibility(String omsFeasibilityRequest, String request,
			Map<String, String> authHeader, String action) throws TclCommonException, InterruptedException {

		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + createFeasUrl;
		if (action.equalsIgnoreCase("create"))
			sfdcResponse = restClientService.post(url, request, null, null, authHeader);
		else
			sfdcResponse = restClientService.put(url, request, authHeader);
		LOGGER.info("create feasibility response to sfdc {}", sfdcResponse);
		FeasibilityRequestBean feasibilityRequestBean = (FeasibilityRequestBean) Utils
				.convertJsonToObject(omsFeasibilityRequest, FeasibilityRequestBean.class);
		if (sfdcResponse.getStatus() == Status.SUCCESS) {
			SfdcFeasibilityResponseBean feasibilityResponse = (SfdcFeasibilityResponseBean) Utils
					.convertJsonToObject(sfdcResponse.getData(), SfdcFeasibilityResponseBean.class);
			if (feasibilityResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				FeasibilityResponseBean feasResponseBean = constructFeasibilityResponse(
						feasibilityRequestBean.getSfdcServiceJobId(), feasibilityRequestBean.getSiteId(),
						feasibilityResponse);
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCreateFeasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(feasibilityResponseQueue, Utils.convertObjectToJson(feasResponseBean));
			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
				FeasibilityResponseBean feasResponseBean = new FeasibilityResponseBean();
				feasResponseBean.setSfdcServiceJobId(feasibilityRequestBean.getSfdcServiceJobId());
				feasResponseBean.setError(true);
				feasResponseBean.setMessage(feasibilityResponse.getMessage());
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryFeasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(feasibilityResponseQueue, Utils.convertObjectToJson(feasResponseBean));
			}

		} else {
			notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
			FeasibilityResponseBean feasResponseBean = new FeasibilityResponseBean();
			feasResponseBean.setSfdcServiceJobId(feasibilityRequestBean.getSfdcServiceJobId());
			feasResponseBean.setError(true);
			feasResponseBean.setMessage(sfdcResponse.getData());
			LOGGER.info("MDC Filter token value in before Queue call sfdcRetryFeasibility {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(feasibilityResponseQueue, Utils.convertObjectToJson(feasResponseBean));
		}

	}

	/**
	 * sfdcProductRetryCheck
	 * 
	 * @param response
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcProductRetryCheck(SfdcProductServiceBean response, String request, Map<String, String> authHeader,
			String productSolutionId, String sfdcId) throws InterruptedException, TclCommonException {
		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + productUrl;
		sfdcResponse = restClientService.post(url, request, null, null, authHeader);
		LOGGER.info("product service response to sfdc {}", sfdcResponse);

		if (sfdcResponse.getStatus() == Status.SUCCESS) {
			SfdcProductServicesResponseBean productServiceResponse = (SfdcProductServicesResponseBean) Utils
					.convertJsonToObject(sfdcResponse.getData(), SfdcProductServicesResponseBean.class);
			if (productServiceResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				ProductServicesResponseBean siteResponseBean = constructProductResponeAndSendToQueue(
						productServiceResponse);
				siteResponseBean.setSfdcid(response.getCreateRequestV1().getOpportunityId());
				siteResponseBean.setProductSolutionCode(productSolutionId);
				siteResponseBean.setType("create");
				if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
					siteResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
				}
				LOGGER.info("MDC Filter token value in before Queue call sfdcProductRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(productResponseQueue, Utils.convertObjectToJson(siteResponseBean));
			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
				ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
				prodResponseBean.setError(true);
				prodResponseBean.setErrorMessage(sfdcResponse.getData());
				prodResponseBean.setSfdcid(sfdcId);
				prodResponseBean.setType("create");
				prodResponseBean.setProductSolutionCode(response.getProductSolutionCode());
				if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
					prodResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
				}
				LOGGER.info("MDC Filter token value in before Queue call sfdcProductRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
			}

		} else {
			notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
			ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
			prodResponseBean.setError(true);
			prodResponseBean.setErrorMessage(sfdcResponse.getData());
			prodResponseBean.setSfdcid(sfdcId);
			prodResponseBean.setProductSolutionCode(response.getProductSolutionCode());
			prodResponseBean.setType("create");
			if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
				prodResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
			}
			LOGGER.info("MDC Filter token value in before Queue call sfdcProductRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
		}
	}

	public void processSdfcUpdateProduct(String input) throws TclCommonException {

		try {
			LOGGER.info("input details:{}",input);
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PRODUCT_UPDATE.toString());
			SfdcUpdateProductServiceBean response = (SfdcUpdateProductServiceBean) mapper.transfortToSfdcRequest(input);
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			if(input.contains("IZOSDWAN")) {
				authAccessToken = SfdcConstants.AUTH.toString() + CommonConstants.SPACE
						+ accessToken.getAccessToken();
			}
			String request = Utils.convertObjectToJson(response);

			LOGGER.info("Update product service request to sfdc :: {}", request);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse sfdcResponse = restClientService.put(endPointUrl + productUrl, request, authHeader);
			LOGGER.info("Update product service response from sfdc :: {}", sfdcResponse);

			if (sfdcResponse.getStatus().equals(Status.SUCCESS)) {
				SfdcProductServicesResponseBean productServiceResponse = (SfdcProductServicesResponseBean) Utils
						.convertJsonToObject(sfdcResponse.getData(), SfdcProductServicesResponseBean.class);
				if (productServiceResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					ProductServicesResponseBean siteResponseBean = constructProductResponeAndSendToQueue(
							productServiceResponse);
					siteResponseBean.setSfdcid(response.getUpdateRequestV1().getOpportunityId());
					siteResponseBean.setType("update");
					siteResponseBean.setIsCancel(response.getIsCancel());
					siteResponseBean.setProductSolutionCode(response.getProductSolutionCode());
					// for teamsdr
					if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
						siteResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
					}
					LOGGER.info("MDC Filter token value in before Queue call processSdfcUpdateProduct {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(productResponseQueue, Utils.convertObjectToJson(siteResponseBean));
				} else {
					LOGGER.info("Retrying with 10 secs {}", request);
					sfdcUpdateProductRetryCheck(response, request, authHeader,
							response.getUpdateRequestV1().getOpportunityId());
				}
			} else {
				sfdcUpdateProductRetryCheck(response, request, authHeader,
						response.getUpdateRequestV1().getOpportunityId());
			}

		} catch (Exception e) {
			LOGGER.error("error in processing product service sfdc", e);
		}

	}

	public void processSdfcDeleteProduct(String input) throws TclCommonException {

		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PRODUCT_DELETE.toString());
			SfdcDeleteProductBean response = (SfdcDeleteProductBean) mapper.transfortToSfdcRequest(input);
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();

			String request = Utils.convertObjectToJson(response);

			LOGGER.info("Delete product service request to sfdc ::  {}", request);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse sfdcResponse = restClientService.post(endPointUrl + deleteProductUrl, request, null, null,
					authHeader);
			LOGGER.info("Delete product service response from sfdc ::  {}", sfdcResponse);
			if (sfdcResponse.getStatus() == Status.SUCCESS && sfdcResponse.getData() != null) {
				SfdcProductServicesResponseBean productServiceResponse = (SfdcProductServicesResponseBean) Utils
						.convertJsonToObject(sfdcResponse.getData(), SfdcProductServicesResponseBean.class);
				if (productServiceResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
					prodResponseBean.setError(false);
					prodResponseBean.setSfdcid(response.getOpportunityId());
					prodResponseBean.setType("delete");
					// for teamsdr
					if(Objects.nonNull(response.getParentTpsSfdcOptyId())){
						prodResponseBean.setParentTpsSfdcOptyId(response.getParentTpsSfdcOptyId());
					}
					LOGGER.info("MDC Filter token value in before Queue call processSdfcDeleteProduct {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String requestToQueue =  Utils.convertObjectToJson(prodResponseBean);
					LOGGER.info("Input to the queue from delete product service :: {}",requestToQueue);
					mQUtils.send(productResponseQueue,requestToQueue);
					LOGGER.info("Response received success {} ", request);
				} else {
					LOGGER.info("Retrying with 10 secs {}", request);
					sfdcDeleteProductRetryCheck(response, request, authHeader);
				}
			} else {
				LOGGER.info("Response received failed {} ", sfdcResponse.getData());
				LOGGER.info("Retrying with 10 secs {}", request);
				sfdcDeleteProductRetryCheck(response, request, authHeader);
			}

		} catch (Exception e) {
			LOGGER.error("error in processing product service sfdc", e);
		}

	}

	private void sfdcDeleteProductRetryCheck(SfdcDeleteProductBean response, String request,
			Map<String, String> authHeader) throws InterruptedException, TclCommonException {
		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(10);
		sfdcResponse = restClientService.post(endPointUrl + deleteProductUrl, request, null, null,
				authHeader);
		LOGGER.info("product service response to sfdc {}", sfdcResponse);

		if (sfdcResponse.getStatus() == Status.SUCCESS && sfdcResponse.getData() != null) {
			SfdcProductServicesResponseBean productServiceResponse = (SfdcProductServicesResponseBean) Utils
					.convertJsonToObject(sfdcResponse.getData(), SfdcProductServicesResponseBean.class);
			if (productServiceResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
				prodResponseBean.setError(false);
				prodResponseBean.setSfdcid(response.getOpportunityId());
				prodResponseBean.setType("delete");
				// for teamsdr
				if(Objects.nonNull(response.getParentTpsSfdcOptyId())){
					prodResponseBean.setParentTpsSfdcOptyId(response.getParentTpsSfdcOptyId());
				}
				LOGGER.info("MDC Filter token value in before Queue call sfdcDeleteProductRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
				LOGGER.info("Response received success {} ", request);
			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, endPointUrl + deleteProductUrl,
						authHeader.toString());
				ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
				prodResponseBean.setError(true);
				prodResponseBean.setErrorMessage(sfdcResponse.getData());
				prodResponseBean.setSfdcid(response.getOpportunityId());
				prodResponseBean.setType("delete");
				if(Objects.nonNull(response.getParentTpsSfdcOptyId())){
					prodResponseBean.setParentTpsSfdcOptyId(response.getParentTpsSfdcOptyId());
				}
				LOGGER.info("MDC Filter token value in before Queue call sfdcDeleteProductRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
			}
		} else {
			LOGGER.info("Response received failed {} ", sfdcResponse.getData());
			notificationService.notifySfdcError(sfdcResponse.toString(), request, endPointUrl + deleteProductUrl,
					authHeader.toString());
			ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
			prodResponseBean.setError(true);
			prodResponseBean.setErrorMessage(sfdcResponse.getData());
			prodResponseBean.setSfdcid(response.getOpportunityId());
			prodResponseBean.setType("delete");
			if(Objects.nonNull(response.getParentTpsSfdcOptyId())){
				prodResponseBean.setParentTpsSfdcOptyId(response.getParentTpsSfdcOptyId());
			}
			LOGGER.info("MDC Filter token value in before Queue call sfdcDeleteProductRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
		}
	}

	/**
	 * sfdcUpdateProductRetryCheck
	 * 
	 * @param response
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcUpdateProductRetryCheck(SfdcUpdateProductServiceBean response, String request,
			Map<String, String> authHeader, String sfdcId) throws InterruptedException, TclCommonException {
		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(10);
		String url = endPointUrl + productUrl;
		sfdcResponse = restClientService.put(url, request, authHeader);
		LOGGER.info("product service response to sfdc {}", sfdcResponse);

		if (sfdcResponse.getStatus().equals(Status.SUCCESS)) {
			SfdcProductServicesResponseBean productServiceResponse = (SfdcProductServicesResponseBean) Utils
					.convertJsonToObject(sfdcResponse.getData(), SfdcProductServicesResponseBean.class);
			if (productServiceResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				ProductServicesResponseBean siteResponseBean = constructProductResponeAndSendToQueue(
						productServiceResponse);
				siteResponseBean.setSfdcid(response.getUpdateRequestV1().getOpportunityId());
				siteResponseBean.setType("update");
				siteResponseBean.setIsCancel(response.getIsCancel());
				siteResponseBean.setProductSolutionCode(response.getProductSolutionCode());
				if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
					siteResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
				}
				LOGGER.info("MDC Filter token value in before Queue call sfdcUpdateProductRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(productResponseQueue, Utils.convertObjectToJson(siteResponseBean));
			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
				ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
				prodResponseBean.setError(true);
				prodResponseBean.setErrorMessage(sfdcResponse.getData());
				prodResponseBean.setSfdcid(sfdcId);
				prodResponseBean.setIsCancel(response.getIsCancel());
				prodResponseBean.setType("update");
				prodResponseBean.setProductSolutionCode(response.getProductSolutionCode());
				// for teamsdr
				if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
					prodResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
				}
				LOGGER.info("MDC Filter token value in before Queue call sfdcUpdateProductRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
			}
		} else {
			notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
			ProductServicesResponseBean prodResponseBean = new ProductServicesResponseBean();
			prodResponseBean.setError(true);
			prodResponseBean.setErrorMessage(sfdcResponse.getData());
			prodResponseBean.setSfdcid(sfdcId);
			prodResponseBean.setIsCancel(response.getIsCancel());
			prodResponseBean.setType("update");
			prodResponseBean.setProductSolutionCode(response.getProductSolutionCode());
			if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
				prodResponseBean.setParentQuoteToLeId(response.getParentQuoteToLeId());
			}
			LOGGER.info("MDC Filter token value in before Queue call sfdcUpdateProductRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(productResponseQueue, Utils.convertObjectToJson(prodResponseBean));
		}
	}

	/**
	 * 
	 *
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */

	public void processSdfcStage(String input) throws TclCommonException {

		try {

			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.STAGING_MAPPER.toString());
			SfdStagingBean response = (SfdStagingBean) mapper.transfortToSfdcRequest(input);
			if (response.getUpdateRequestV1().getOpportunity().getStageName().contains("COF Received")) {
				LOGGER.info("Waiting for COF received for 10 seconds ");
				TimeUnit.SECONDS.sleep(10);
			}
			String request = Utils.convertObjectToJson(response);
			LOGGER.info("sfdc request for updating stage {}", request);

			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse sfdcResponse = restClientService.put(endPointUrl + stageUrl, request, authHeader);
			LOGGER.info("sfdc respone for updating stage {}", sfdcResponse);
			if (sfdcResponse.getStatus() == Status.SUCCESS) {
				SfdcStagingResponseBean stagingResponse = (SfdcStagingResponseBean) Utils
						.convertJsonToObject(sfdcResponse.getData(), SfdcStagingResponseBean.class);
				if (stagingResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					StagingResponseBean stage = constructProductResponeAndSendToQueue(stagingResponse);
					// for teamsdr
					if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
						stage.setParentQuoteToLeId(response.getParentQuoteToLeId());
					}
					LOGGER.info("MDC Filter token value in before Queue call processSdfcStage {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(stagingResponseQueue, Utils.convertObjectToJson(stage));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcStageUpdateRetryCheck(request, authHeader,
							response.getUpdateRequestV1().getOpportunity().getOpportunityIDC(),response);
				}
			} else {
				sfdcStageUpdateRetryCheck(request, authHeader,
						response.getUpdateRequestV1().getOpportunity().getOpportunityIDC(),response);
			}

		} catch (Exception e) {

			LOGGER.error("error in processing stage request ", e);
		}

	}

	/**
	 * sfdcStageUpdateRetryCheck
	 * 
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcStageUpdateRetryCheck(String request, Map<String, String> authHeader, String sfdcId,SfdStagingBean response)
			throws InterruptedException, TclCommonException {
		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(4);
		sfdcResponse = restClientService.put(endPointUrl + stageUrl, request, authHeader);
		LOGGER.info("sfdc respone for updating stage {}", sfdcResponse);
		if (sfdcResponse.getStatus() == Status.SUCCESS) {
			SfdcStagingResponseBean stagingResponse = (SfdcStagingResponseBean) Utils
					.convertJsonToObject(sfdcResponse.getData(), SfdcStagingResponseBean.class);
			if (stagingResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				StagingResponseBean stage = constructProductResponeAndSendToQueue(stagingResponse);
				// for teamsdr
				if(Objects.nonNull(response) && Objects.nonNull(response.getParentQuoteToLeId())){
					stage.setParentQuoteToLeId(response.getParentQuoteToLeId());
				}
				LOGGER.info("MDC Filter token value in before Queue call sfdcStageUpdateRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(stagingResponseQueue, Utils.convertObjectToJson(stage));
			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, endPointUrl + stageUrl,
						authHeader.toString());
				StagingResponseBean stage = new StagingResponseBean();
				stage.setError(true);
				stage.setErrorMessage(sfdcResponse.getData());
				stage.setCustomOptyId(sfdcId);
				LOGGER.info("MDC Filter token value in before Queue call sfdcStageUpdateRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(stagingResponseQueue, Utils.convertObjectToJson(stage));
			}
		} else {
			notificationService.notifySfdcError(sfdcResponse.toString(), request, endPointUrl + stageUrl,
					authHeader.toString());
			StagingResponseBean stage = new StagingResponseBean();
			stage.setError(true);
			stage.setErrorMessage(sfdcResponse.getData());
			stage.setCustomOptyId(sfdcId);
			LOGGER.info("MDC Filter token value in before Queue call sfdcStageUpdateRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(stagingResponseQueue, Utils.convertObjectToJson(stage));
		}
	}

	/**
	 * constructAndSendToQueue
	 * 
	 * @param siteResponse
	 */
	private SiteResponseBean constructSiteResponeAndSendToQueue(
			com.tcl.dias.sfdc.response.bean.SiteResponseBean siteResponse) {

		SiteResponseBean responseBean = new SiteResponseBean();
		responseBean.setOpportunityId(siteResponse.getOpportunityID());
		responseBean.setProductService(siteResponse.getProductService());
		responseBean.setStatus(siteResponse.getStatus());
		responseBean.setMessage(siteResponse.getMessage());
		if (siteResponse.getSiteLocations() != null) {
			siteResponse.getSiteLocations().forEach(siteLoc -> {
				SiteLocationResponse responseLoc = new SiteLocationResponse();
				responseLoc.setLocation(siteLoc.getLocation());
				responseLoc.setSfdcSiteName(siteLoc.getSfdcSiteName());
				responseLoc.setSiteId(siteLoc.getSiteId());
				responseLoc.setSiteName(siteLoc.getSiteName());
				responseBean.getSiteLocations().add(responseLoc);
			});

		}
		return responseBean;

	}

	/**
	 * constructProductResponeAndSendToQueue
	 * 
	 * @param productServiceResponse
	 * @return
	 */
	private ProductServicesResponseBean constructProductResponeAndSendToQueue(
			SfdcProductServicesResponseBean productServiceResponse) {
		ProductServicesResponseBean responseBean = new ProductServicesResponseBean();

		responseBean.setMessage(productServiceResponse.getMessage());
		responseBean.setProductId(productServiceResponse.getProductId());
		responseBean.setStatus(productServiceResponse.getStatus());

		if (productServiceResponse.getProductsservices() != null) {
			productServiceResponse.getProductsservices().forEach(prod -> {

				ProductsserviceResponse productsservice = new ProductsserviceResponse();
				AttributesResponseBean attributesResponseBean = new AttributesResponseBean();
				SfdcAttributes attributes = prod.getAttributes();
				attributesResponseBean.setType(attributes.getType());
				attributesResponseBean.setUrl(attributes.getUrl());
				productsservice.setAttributes(attributesResponseBean);
				productsservice.setBigMachinesARC(prod.getBigMachinesARCC());
				productsservice.setBigMachinesMRC(prod.getBigMachinesMRCC());
				productsservice.setBigMachinesNRC(prod.getBigMachinesNRCC());
				productsservice.setBigMachinesTCV(prod.getBigMachinesTCVC());
				productsservice.setCurrencyIsoCode(prod.getCurrencyIsoCode());
				productsservice.setType(prod.getTypeC());
				productsservice.setRecordTypeId(prod.getRecordTypeId());
				productsservice.setProductType(prod.getProductTypeC());
				productsservice.setProductNRC(prod.getProductNRCC());
				productsservice.setProductMRC(prod.getProductMRCC());
				productsservice.setProductLineOfBusiness(prod.getProductLineOfBusinessC());
				productsservice.setOrderType(prod.getOrderTypeC());
				productsservice.setOpportunityName(prod.getOpportunityNameC());
				productsservice.setMultiVRFSolution(prod.getMultiVRFSolutionC());
				productsservice.setL2FeasibilityCommercialManager(prod.getL2FeasibilityCommercialManagerC());
				productsservice.setIsTrainingNeeded(prod.getDoYouNeedTrainingForThisProductC());
				productsservice.setIdcLocation(prod.getIDCLocationC());
				productsservice.setIdcFloor(prod.getIDCFloorC());
				productsservice.setIdcBandwidth(prod.getIDCBandwidthC());
				productsservice.setId(prod.getId());

				productsservice.setaEndCountryC(prod.getaEndCountryC());
				productsservice.setbEndCountryC(prod.getbEndCountryC());
				productsservice.setaEndCityC(prod.getaEndCityC());
				productsservice.setbEndCityC(prod.getbEndCityC());
				productsservice.setQuantityC(prod.getQuantityC());
				productsservice.setInterfaceC(prod.getInterfaceC());
				productsservice.setBandwidthCircuitSpeed(prod.getBandwidthCircuitSpeedC());
				// npl BM transition specific
				productsservice.setSubType(prod.getSubTypeC());
				productsservice.setBandwidthCircuitSpeed(prod.getBandwidthCircuitSpeedC());
				productsservice.setMediaType(prod.getMediaTypeC());
				// gsc specific
				productsservice.setOpportunityNameC(prod.getOpportunityNameC());
				productsservice.setDataOrMobileC(prod.getDataOrMobileC());
				productsservice.setpOCAttachedC(prod.getpOCAttachedC());
				productsservice.setInterconnectTypeC(prod.getInterconnectTypeC());
				productsservice.setEnabledForUnifiedAccessC(prod.getEnabledForUnifiedAccessC());
				productsservice.setProductCategoryC(prod.getProductCategoryC());
				productsservice.setCallTypeC(prod.getCallTypeC());
				productsservice.setPrimaryFeaturesC(prod.getPrimaryFeaturesC());
				productsservice.setLnsC(prod.getLnsC());
				productsservice.setItfsC(prod.getItfsC());
				productsservice.setUifnC(prod.getUifnC());
				productsservice.setAudioConferencingAccessNoServiceC(prod.getAudioConferencingAccessNoServiceC());
				productsservice.setAudioConferencingDTFServiceC(prod.getAudioConferencingDTFServiceC());
				//IZO SDWAN SPECIFIC
				productsservice.setMssTypec(prod.getMssTypeC());
				productsservice.setProductFlavourC(prod.getProductFlavourC());
				responseBean.getProductsservices().add(productsservice);

			});
		}

		return responseBean;
	}

	/**
	 * constructOpportunityResponeAndSendToQueue
	 * 
	 * @param opportunityResponseBean
	 * @return
	 */
	private OpportunityResponseBean constructOpportunityRespone(SfdcOpportunityResponseBean sfOpportunityResponseBean) {
		OpportunityResponseBean responseBean = new OpportunityResponseBean();
		if (sfOpportunityResponseBean != null) {
			responseBean.setCustomOptyId(sfOpportunityResponseBean.getCustomOptyId());
			responseBean.setMessage(sfOpportunityResponseBean.getMessage());
			responseBean.setStatus(sfOpportunityResponseBean.getStatus());
			SfdcOpportunity opportunity = sfOpportunityResponseBean.getOpportunity();
			OpportunityResponse opResponse = constructOpportunity(opportunity);
			responseBean.setOpportunity(opResponse);

		}

		return responseBean;
	}

	/**
	 * constructOpportunity
	 * 
	 * @param opportunity
	 */
	private OpportunityResponse constructOpportunity(SfdcOpportunity opportunity) {
		OpportunityResponse opResponse = new OpportunityResponse();
		if (opportunity != null) {
			opResponse.setAccountId(opportunity.getAccountId());
			AttributesResponseBean attributesResponseBean = new AttributesResponseBean();

			attributesResponseBean.setType(opportunity.getAttributes().getType());
			attributesResponseBean.setUrl(opportunity.getAttributes().getUrl());
			opResponse.setAttributes(attributesResponseBean);
			opResponse.setBillingFrequency(opportunity.getBillingFrequencyC());
			opResponse.setBillingMethod(opportunity.getBillingMethodC());
			opResponse.setCloseDate(opportunity.getCloseDate());
			opResponse.setCofType(opportunity.getCOFTypeC());
			opResponse.setCurrencyIsoCode(opportunity.getCurrencyIsoCode());
			opResponse.setCustomerChurned(opportunity.getCustomerChurnedC());
			opResponse.setCustomerContractingEntity(opportunity.getCustomerContractingEntityC());
			opResponse.setDummyParentTerminationOpportunity(opportunity.getDummyParentTerminationOpportunityC());
			opResponse.setDescription(opportunity.getDescription());
			opResponse.setWinLossRemarks(opportunity.getWinLossRemarksC());
			opResponse.setType(opportunity.getType());
			opResponse.setTermOfMonths(opportunity.getTermOfMonthsC());
			opResponse.setTataBillingEntity(opportunity.getTATABillingEntityC());
			opResponse.setSubType(opportunity.getSubTypeC());
			opResponse.setStageName(opportunity.getStageName());
			opResponse.setSelectProductType(opportunity.getSelectProductTypeC());
			opResponse.setReferralToPartner(opportunity.getReferralToPartnerC());
			opResponse.setRecordTypeId(opportunity.getRecordTypeId());
			opResponse.setProgramManagerUserLookup(opportunity.getProgramManagerUserLookupC());
			opResponse.setPortalTransactionId(opportunity.getPortalTransactionIdC());
			opResponse.setPaymentTerm(opportunity.getPaymentTermC());
			opResponse.setOwnerId(opportunity.getOwnerId());
			opResponse.setOrderCategory(opportunity.getOrderCategoryC());
			opResponse.setOpportunityClassification(opportunity.getOpportunityClassificationC());
			opResponse.setName(opportunity.getName());
			opResponse.setMigrationSourceSystem(opportunity.getMigrationSourceSystemC());
			opResponse.setLeadTimeToRFS(opportunity.getLeadTimeToRFSC());
			opResponse.setIsPartnerOrder(opportunity.getIsPartnerOrderC());
			opResponse.setiLLAutoCreation(opportunity.getILLAutoCreationC());
			opResponse.setId(opportunity.getId());
			opResponse.setCustomerMailReceivedDate(opportunity.getCustomerMailReceivedDateC());
			opResponse.setCurrentCircuitServiceID(opportunity.getCurrentCircuitServiceIDC());
			opResponse.setParentOpportunity(opportunity.getParentOpportunityC());
			opResponse.setPreviousMRC(opportunity.getPreviousMRCc());
			opResponse.setPreviousNRC(opportunity.getPreviousNRCc());
			opResponse.setEffectiveDateOfChange(opportunity.getEffectiveDateOfChangeC());
			opResponse.setReasonForTermination(opportunity.getReasonForTerminationC());
			//Credit Check
			opResponse.setIccEnterpiceVoiceProductFlag(opportunity.getIccEnterpriceVoiceProductFlagC());
			opResponse.setLastApprovedMrcNrc(opportunity.getMrcNrcC());
			//Termination
			opResponse.setTerminationSubReason(opportunity.getTerminationSubReasonC());
			opResponse.setTerminationSendToTDDate(opportunity.getCustomerRequestedDateC());
			opResponse.setInternalOrCustomer(opportunity.getInternalCustomerC());
			opResponse.setCsmNonCsm(opportunity.getCsmNoncsmC());
			opResponse.setCommunicationRecipient(opportunity.getCommunicationRecipientC());
			opResponse.setHandoverTo(opportunity.getHandoverToC());
            opResponse.setRetentionReason(opportunity.getRetentionReasonC());
            opResponse.setHandoverOn(opportunity.getHandoverOnC());
            opResponse.setSalesAdministratorRegion(opportunity.getSalesAdministratorRegionC());
            opResponse.setSalesAdministrator(opportunity.getSalesAdministratorC());
            opResponse.setEarlyTerminationCharges(opportunity.getEtcSystemGeneratedC());
            opResponse.setActualEtcToBeCharged(opportunity.getEarlyTerminationChargesAmountC());
            opResponse.setEtcWaiverType(opportunity.getEtcWaiverTypeC());
            opResponse.setEtcWaived(opportunity.getEtcWaiverC());
            opResponse.setEtcRemarks(opportunity.getEtcRemarksC());
            opResponse.setEarlyTerminationChargesApplicable(opportunity.getEarlyTerminationChargesC());



		}
		return opResponse;

	}

	/**
	 * constructProductResponeAndSendToQueue
	 * 
	 * @param stagingResponse
	 * @return
	 */
	private StagingResponseBean constructProductResponeAndSendToQueue(SfdcStagingResponseBean stagingResponse) {
		StagingResponseBean responseBean = new StagingResponseBean();
		responseBean.setCustomOptyId(stagingResponse.getCustomOptyId());
		responseBean.setMessage(stagingResponse.getMessage());
		responseBean.setStatus(stagingResponse.getStatus());
		OpportunityResponse opportunityResponse = constructOpportunity(stagingResponse.getOpportunity());
		responseBean.setOpportunity(opportunityResponse);
		return responseBean;
	}

	/**
	 * 
	 * Process SDFDC BCR Request
	 * 
	 * @param input
	 * @throws TclCommonException
	 */
	public void processSdfcBCR(String input) throws TclCommonException {

		try {
			String request = null;
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			BCROmsRequest bcrOmsRequest = (BCROmsRequest) Utils.convertJsonToObject(input, BCROmsRequest.class);
			if (bcrOmsRequest.getBcrOmsRecords().get(0).getBcrOptyProperitiesBeans().getUpdate()) {
				request = Utils.convertObjectToJson(convertUpdateOmsBCRRequestToBCRRequest(bcrOmsRequest));
			} else {
				request = Utils.convertObjectToJson(convertOmsBCRRequestToBCRRequest(bcrOmsRequest));
			}

			LOGGER.info("bcr request to sfdc {}", request);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse sfdcResponse = null;
			if (bcrOmsRequest.getBcrOmsRecords().get(0).getBcrOptyProperitiesBeans().getUpdate()) {
				sfdcResponse = restClientService.put(endPointUrl + bcrUrl, request, authHeader);
			} else {
				sfdcResponse = restClientService.post(endPointUrl + bcrUrl, request, null, null, authHeader);

			}
			LOGGER.info("bcr service response to sfdc {}", sfdcResponse);

			if (sfdcResponse.getStatus() == Status.SUCCESS) {
				BCRResponse bcrResponse = (BCRResponse) Utils.convertJsonToObject(sfdcResponse.getData(),
						BCRResponse.class);
				if (bcrResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					BCROmsResponse bcrOmsResponse = constructBcrOmsResponse(bcrResponse, bcrOmsRequest);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcProduct {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(processBcrResponseQueue, Utils.convertObjectToJson(bcrOmsResponse));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcBcrRecheck(request, authHeader, bcrOmsRequest);
				}
			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, (endPointUrl + productUrl),
						authHeader.toString());
				BCRResponse bcrResponse = new BCRResponse();
				bcrResponse.setErrorcode("400");
				bcrResponse.setMessage("system Error");
				bcrResponse.setStatus(CommonConstants.FAILIURE);
				BCROmsResponse bcrOmsResponse = constructBcrOmsResponse(bcrResponse, bcrOmsRequest);
				LOGGER.info("MDC Filter token value in before Queue call sfdcBcrRecheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(processBcrResponseQueue, Utils.convertObjectToJson(bcrOmsResponse));

			}

		} catch (Exception e) {
			LOGGER.error("error in processing product service sfdc", e);
		}

	}

	private BCRRequest convertOmsBCRRequestToBCRRequest(BCROmsRequest bcrOmsRequest) {
		BCRRequest bcrRequest = new BCRRequest();
		List<CreateRequestV1> createRequestV1s = new ArrayList<>();
		CreateRequestV1 createRequestV1 = new CreateRequestV1();
		if (bcrOmsRequest.getBcrOmsRecords() != null && !bcrOmsRequest.getBcrOmsRecords().isEmpty()) {
			BCROmsRecord bcrOmsRecord = bcrOmsRequest.getBcrOmsRecords().get(0);
			if (bcrOmsRecord.getBcrOptyProperitiesBeans() != null) {
				BCROptyProperitiesBean bcrOptyProperitiesBean = bcrOmsRecord.getBcrOptyProperitiesBeans();
				BcrRecord bcrRecord = new BcrRecord();
				bcrRecord.setCompletionStatusC(bcrOptyProperitiesBean.getCompletionStatus());
				bcrRecord.setCurrencyIsoCode(bcrOptyProperitiesBean.getQuoteCurrency());
				bcrRecord.setCustomerSubmissionDateC(bcrOptyProperitiesBean.getCustomerSubmissionDate());
				bcrRecord.setDateResourceRequiredC(bcrOptyProperitiesBean.getDateResourceRequiredDate());
				bcrRecord.setPricingCategoryC(bcrOptyProperitiesBean.getPricingCategory());
				bcrRecord.setRegionOfSaleC(bcrOptyProperitiesBean.getRegion());
				bcrRecord.setServiceRequiredC("Commercial Management Support");
				bcrRecord.setStatusC(bcrOptyProperitiesBean.getStatus());
				// bcrRecord.setAssignedToC(bcrOmsRecord.getCustAttribute());
				bcrRecord.setSalesApprovalDate(bcrOptyProperitiesBean.getApproverDate());
				bcrRecord.setCdaCmLevel1ApprovalDate(bcrOptyProperitiesBean.getCdaCmLevel1ApprovalDate());
				bcrRecord.setCdaCmLevel2ApprovalDate(bcrOptyProperitiesBean.getCdaCmLevel2ApprovalDate());
				bcrRecord.setCdaCmLevel3ApprovalDate(bcrOptyProperitiesBean.getCdaCmLevel3ApprovalDate());
				bcrRecord.setId(bcrOptyProperitiesBean.getId());
				createRequestV1.setBcrRecord(bcrRecord);
				createRequestV1.setOpportunityId(bcrOmsRecord.getOpportunityId());
				createRequestV1.setSalesApproverEmailId(bcrOptyProperitiesBean.getApproverId());
				createRequestV1.setRecordTypeName(bcrOmsRecord.getRecordTypeName());
				createRequestV1.setCdaCmLevel1ApproverEmailId(bcrOptyProperitiesBean.getCdaCmLevel1ApproverName());
				createRequestV1.setCdaCmLevel2ApproverEmailId(bcrOptyProperitiesBean.getCdaCmLevel2ApproverName());
				createRequestV1.setCdaCmLevel3ApproverEmailId(bcrOptyProperitiesBean.getCdaCmLevel3ApproverName());
				createRequestV1.setAssignedToEmailId(bcrOptyProperitiesBean.getAssignedToEmailId());
			}

		}
		createRequestV1s.add(createRequestV1);
		bcrRequest.setCreateRequestV1(createRequestV1s);
		return bcrRequest;
	}

	private BCROmsResponse constructBcrOmsResponse(BCRResponse bcrResponse, BCROmsRequest bcrOmsRequest) {
		BCROmsResponse bcrOmsResponse = new BCROmsResponse();
		bcrOmsResponse.setbCRList(bcrResponse.getBCRList());
		bcrOmsResponse.setCustomBCRId(bcrResponse.getCustomBCRId());
		bcrOmsResponse.setErrorcode(bcrResponse.getErrorcode());
		bcrOmsResponse.setMessage(bcrResponse.getMessage());
		bcrOmsResponse.setStatus(bcrResponse.getStatus());
		bcrOmsResponse.setTpsId(bcrOmsRequest.getTpdId());
		return bcrOmsResponse;
	}

	private void sfdcBcrRecheck(String request, Map<String, String> authHeader, BCROmsRequest bcrOmsRequest)
			throws InterruptedException, TclCommonException {
		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + productUrl;
		sfdcResponse = restClientService.post(endPointUrl + bcrUrl, request, null, null, authHeader);
		LOGGER.info("bcr request to sfdc {}", sfdcResponse);
		BCRResponse bcrResponse = (BCRResponse) Utils.convertJsonToObject(sfdcResponse.getData(), BCRResponse.class);
		if (sfdcResponse.getStatus() == Status.SUCCESS) {

			if (bcrResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				BCROmsResponse bcrOmsResponse = constructBcrOmsResponse(bcrResponse, bcrOmsRequest);
				LOGGER.info("MDC Filter token value in before Queue call sfdcBcrRecheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(processBcrResponseQueue, Utils.convertObjectToJson(bcrOmsResponse));

			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
				BCROmsResponse bcrOmsResponse = constructBcrOmsResponse(bcrResponse, bcrOmsRequest);
				LOGGER.info("MDC Filter token value in before Queue call sfdcBcrRecheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(processBcrResponseQueue, Utils.convertObjectToJson(bcrOmsResponse));
			}
		} else {
			notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
			BCROmsResponse bcrOmsResponse = constructBcrOmsResponse(bcrResponse, bcrOmsRequest);
			LOGGER.info("MDC Filter token value in before Queue call sfdcBcrRecheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(processBcrResponseQueue, Utils.convertObjectToJson(bcrOmsResponse));
		}

	}

	/**
	 * Process Partner Entity creation in SFDC
	 *
	 * @param input
	 * @throws TclCommonException
	 */
	public void processSdfcPartnerEntity(String input) throws TclCommonException {
		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PARTNER_ENTITY.toString());
			SfdcPartnerEntityRequest sfdcRequest = (SfdcPartnerEntityRequest) mapper.transfortToSfdcRequest(input);
			String request = Utils.convertObjectToJson(sfdcRequest);
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);

			String sfdcUrlPartnerCreateEntity = endPointUrl + createPartnerEntityUrl;

			LOGGER.info("partner SFDC endPointUrl {}", sfdcUrlPartnerCreateEntity);
			LOGGER.info("partner SFDC createPartnerEntityUrl path {}", createPartnerEntityUrl);
			LOGGER.info("partner SFDC entity create request URL {}", sfdcUrlPartnerCreateEntity);
			LOGGER.info("partner entity create request {}", request);

			RestResponse sfdcReponse = restClientService.post(sfdcUrlPartnerCreateEntity, request, null, null, authHeader);

			LOGGER.info("partner entity create response {}", sfdcReponse);

			if (sfdcReponse.getStatus() == Status.SUCCESS) {
				SfdcPartnerEntityResponse partnerEntityResponse = (SfdcPartnerEntityResponse) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcPartnerEntityResponse.class);
				if (partnerEntityResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					PartnerEntityResponseBean partnerEntityBean = constructPartnerEntityResponse(partnerEntityResponse);
					partnerEntityBean.setReferenceId(sfdcRequest.getReferenceId());
					LOGGER.info("partner entity response to queue {}", partnerEntityBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcPartnerEntity {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(partnerEntityCreateResponseUpdate, Utils.convertObjectToJson(partnerEntityBean));
				} else {
					LOGGER.info("Retrying with 4 secs for create entity {}", request);
					createEnityRetryCheck(request, authHeader, sfdcRequest);

				}
			} else {
				LOGGER.info("Retrying with 4 secs for create entity {}", request);
				createEnityRetryCheck(request, authHeader, sfdcRequest);
			}

		} catch (Exception e) {
			LOGGER.warn("Error in process Partner Entity engine ", e);
		}

	}

	private PartnerEntityResponseBean constructPartnerEntityResponse(
			SfdcPartnerEntityResponse sfdcPartnerEntityResponse) {
		PartnerEntityResponseBean partnerEntityResponseBean = new PartnerEntityResponseBean();
		partnerEntityResponseBean.setMessage(sfdcPartnerEntityResponse.getMessage());
		partnerEntityResponseBean.setStatus(sfdcPartnerEntityResponse.getStatus());
		partnerEntityResponseBean.setCustomerLegalEntityCode(sfdcPartnerEntityResponse.getCustomerLegalEntityCode());
		return partnerEntityResponseBean;
	}

	/**
	 * Process Partner Sales Funnel in SFDC
	 *
	 * @param request
	 * @throws TclCommonException
	 */
	public List<SfdcSalesFunnelResponseBean> getSfdcSalesFunnelData(String request) {
		List<SfdcSalesFunnelResponseBean> response = new ArrayList<>();
		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.SALES_FUNNEL.toString());
			SfdcSalesFunnelRequestBean sfdcSalesFunnelRequest = (SfdcSalesFunnelRequestBean) mapper
					.transfortToSfdcRequest(request);
			String sfdcRequest = Utils.convertObjectToJson(sfdcSalesFunnelRequest);
			RestResponse sfdcReponse = getSfdcSalesFunnelResponse(request, sfdcRequest);

			if (sfdcReponse.getStatus().equals(Status.SUCCESS)) {
				SfdcSalesFunnelResponse salesFunnelResponse = (SfdcSalesFunnelResponse) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcSalesFunnelResponse.class);
				if (salesFunnelResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					response = constructSalesFunnelResponse(salesFunnelResponse);
					LOGGER.debug("get sales funnel response to queue : {}", response);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in process sales data of partner ", e);
		}
		return response;
	}

	/**
	 * Process Partner Deal Registration
	 *
	 * @param request
	 * @return
	 */
	public List<DealRegistrationResponseBean> getDealRegistrationForPartner(String request) {
		List<DealRegistrationResponseBean> response = new ArrayList<>();
		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.DEAL_REGISTRATION.toString());
			DealRegistrationRequestBean dealRegistrationRequestBean = (DealRegistrationRequestBean) mapper.transfortToSfdcRequest(request);
			String sfdcRequest = Utils.convertObjectToJson(dealRegistrationRequestBean);
			RestResponse dealRegistrationReponse = getSfdcSalesFunnelResponse(request, sfdcRequest);

			if (dealRegistrationReponse.getStatus().equals(Status.SUCCESS)) {
				DealRegistrationResponse dealRegistrationResponse = (DealRegistrationResponse) Utils
						.convertJsonToObject(dealRegistrationReponse.getData(), DealRegistrationResponse.class);
				if (dealRegistrationResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					response = constructDealRegistrationResponse(dealRegistrationResponse);
					LOGGER.debug("get deal registration response to queue : {}", response);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in process deal registration of partner ", e);
		}
		return response;
	}

	/**
	 * Rest call to SFDC for sales Funnel data
	 *
	 * @param request
	 * @param sfdcRequest
	 * @return {@link RestResponse}
	 * @throws TclCommonException
	 */
	private RestResponse getSfdcSalesFunnelResponse(String request, String sfdcRequest) throws TclCommonException {
		SfdcAccessToken accessToken = getAuthToken();

		String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE + accessToken.getAccessToken();
		Map<String, String> authHeader = new HashMap<>();
		authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
		LOGGER.info("partner sales funnel request : {}", request);

		RestResponse sfdcReponse = restClientService.post(getSalesFunnelUrl, sfdcRequest, null, null, authHeader);

		LOGGER.info("partner sales funnel response : {}", sfdcReponse);
		return sfdcReponse;
	}

	/**
	 * Method to construct Sales Funnel Response Bean
	 *
	 * @param salesFunnelResponse
	 * @return {@link SfdcSalesFunnelResponseBean}
	 */
	private List<SfdcSalesFunnelResponseBean> constructSalesFunnelResponse(
			SfdcSalesFunnelResponse salesFunnelResponse) {
		List<SfdcSalesFunnelResponseBean> salesFunnelResponseBeans = new ArrayList<>();
		salesFunnelResponseBeans = salesFunnelResponse.getSfdcSalesFunnelResponseBean();
		return salesFunnelResponseBeans;
	}

	private List<SfdcActiveCampaignResponseBean> constructCampaignResponse(
			SfdcCampaignReponseBean salesFunnelResponse) {
		List<SfdcActiveCampaignResponseBean> sfdcActiveCampaignResponseBeanList = new ArrayList<>();
		sfdcActiveCampaignResponseBeanList = salesFunnelResponse.getSfdcSalesFunnelResponseBean();
		return sfdcActiveCampaignResponseBeanList;
	}
	/**
	 * Method to construct Sales Funnel Response Bean
	 *
	 * @param dealRegistrationResponse
	 * @return {@link DealRegistrationResponseBean}
	 */
	private List<DealRegistrationResponseBean> constructDealRegistrationResponse(DealRegistrationResponse dealRegistrationResponse) {
		List<DealRegistrationResponseBean> dealRegistrationResponseBeans = new ArrayList<>();
		dealRegistrationResponseBeans = dealRegistrationResponse.getDealRegistrationResponseBean();
		return dealRegistrationResponseBeans;
	}

	protected HashMap<String, String> getHeader() {
		HashMap<String, String> authorizationHeader = new HashMap<>();
		authorizationHeader.put("Accept", "application/json");
		authorizationHeader.put("Content-Type", "application/json");

		return authorizationHeader;
	}

	/**
	 * 
	 * process MDM Details
	 * 
	 * @param input
	 * @throws TclCommonException
	 */
	public void processMDMDetails(String input) throws TclCommonException {

		try {
			MDMOmsRequestBean mDMOmsRequestBean = (MDMOmsRequestBean) Utils.convertJsonToObject(input,
					MDMOmsRequestBean.class);
			String request = Utils.convertObjectToJson(convertOmsCMDRequestToMDMRequest(mDMOmsRequestBean));

			LOGGER.info("Mdm request to sales {}", request);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put("Authorization", "Basic " + mdmAuthKey);
			RestResponse mdmResponse = new RestResponse();

			// Update le contact
			if (!mDMOmsRequestBean.getIsAdd()) {
				mdmResponse = restClientService.put(mdmUrl + mDMOmsRequestBean.getContactId(), request, authHeader);
			}
			// Add billing address
			else {
				mdmResponse = restClientService.post(mdmUrl, request, null, null, authHeader);
			}

			LOGGER.info("Mdm  response to sfdc {}", mdmResponse);
			String responseMdmRest = mdmResponse.getStatus().toString();
			if (responseMdmRest.equals("SUCCESS")) {
				MDMResponseBean response = (MDMResponseBean) Utils.convertJsonToObject(mdmResponse.getData(),
						MDMResponseBean.class);
				if (response.getStatus().equalsIgnoreCase("201")) {
					MDMOmsResponseBean mDMResponseBean = constructMDMOmsResponse(response, mDMOmsRequestBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcProduct {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(processMDMResponseQueue, Utils.convertObjectToJson(mDMResponseBean));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);

				}
			} else {
				MDMOmsResponseBean mDMResponseBean = constructMDMErrorOmsResponse(mdmResponse.getStatus().toString(),
						mdmResponse.getErrorMessage(), mDMOmsRequestBean);
				LOGGER.info("MDC Filter token value in before Queue call processSdfcProduct {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(processMDMResponseQueue, Utils.convertObjectToJson(mDMResponseBean));
			}

		} catch (Exception e) {
			LOGGER.error("error in processing product service sfdc", e);
		}

	}

	/**
	 * convert OmsMDMRequest to MdmRequst
	 * 
	 * @param MDMOmsRequestBean
	 * @throws TclCommonException
	 */
	private MDMRequestBean convertOmsCMDRequestToMDMRequest(MDMOmsRequestBean mDMRequestBean) {
		MDMRequestBean mdmRequestBean = new MDMRequestBean();
		String corrId = RandomStringUtils.randomNumeric(16);
		mdmRequestBean.setCorrId(corrId);
		MDMContact contact = new MDMContact();

		MDMSrcKeys srcKey = new MDMSrcKeys();
		srcKey.setSrcKeyID(mDMRequestBean.getSrcKeyId());
		srcKey.setSrcKeyIDType("CUID");

		ArrayList<MDMAddress> addresses = new ArrayList<MDMAddress>();
		MDMAddress address = new MDMAddress();
		address.setAddressLineOne(mDMRequestBean.getAddressLineOne());
		address.setAddressUsageValue("HQ");
		address.setCity(mDMRequestBean.getCity());
		address.setCountryName(mDMRequestBean.getCountry());
		address.setCreatedDate("2017-11-29 05:57:40");
		address.setProvinceStateValue(mDMRequestBean.getState());
		address.setStandardizedAddressExternally("True");
		address.setZipPostalCode(mDMRequestBean.getPinCode());
		addresses.add(address);

		ArrayList<MDMContactMethod> contactMethods = new ArrayList<MDMContactMethod>();
		MDMContactMethod contactEmail = new MDMContactMethod();
		contactEmail.setContactMethod("Email");
		contactEmail.setContactMethodStartDate("2017-11-29 05:57:40");
		contactEmail.setContactMethodUsage("Business-Email");
		contactEmail.setContactValue(mDMRequestBean.getEmailId());
		contactMethods.add(contactEmail);
		MDMContactMethod contactMobile = new MDMContactMethod();
		contactMobile.setContactMethod("Mobile");
		contactMobile.setContactMethodStartDate("2017-11-29 05:57:40");
		contactMobile.setContactMethodUsage("Business-Mobile");
		contactMobile.setContactValue(mDMRequestBean.getPhoneNumber());
		contactMethods.add(contactMobile);

		ArrayList<String> role = new ArrayList<String>();
		if (mDMRequestBean.getIsAdd()) {
			role.add("Bill to Contact");
		} else {
			role.add("HQ Contact");
		}

		contact.setAddresses(addresses);
		contact.setContactMethods(contactMethods);
		contact.setRole(role);
		contact.setSrcKeys(srcKey);
		contact.setBillingCollectionsContact("false");
		contact.setContactRelationshipType("HQCON");
		contact.setCreatedById("Optimus");
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = formatter.format(date);
		contact.setCreatedDate(strDate);
		contact.setCustomer24X7Noc("false");
		contact.setDecisionMakerInfluencer("false");
		contact.setDoNotCall("false");
		contact.setExecutive("false");
		contact.setFailedEmail("No");
		contact.setFirstName(mDMRequestBean.getFname());
		contact.setHasOptedOutOfEmail("false");
		contact.setHasOptedOutOfFax("false");
		contact.setLastName(mDMRequestBean.getLname());
		contact.setOwnerId("Optimus");
		contact.setPrimaryRoamingServiceContact("false");
		contact.setPrimaryServiceAssuranceContact("false");
		contact.setServiceAssuranceContact("false");
		contact.setSourceIdentifierValue("OPTIMUS");
		contact.setTechnicalContact("false");
		mdmRequestBean.setContact(contact);
		return mdmRequestBean;

	}

	/**
	 * convert OmsMDMResponse to MdmResponse
	 * 
	 * @param MDMOmsRequestBean
	 * @throws TclCommonException
	 */

	private MDMOmsResponseBean constructMDMOmsResponse(MDMResponseBean mdmResponse, MDMOmsRequestBean mDMRequestBean) {
		MDMOmsResponseBean mDMResponseBean = new MDMOmsResponseBean();
		mDMResponseBean.setTpsId(mDMRequestBean.getTpsId());
		mDMResponseBean.setCorrId(mdmResponse.getCorrId());
		mDMResponseBean.setFirstName(mdmResponse.getFirstName());
		mDMResponseBean.setLastName(mdmResponse.getLastName());
		mDMResponseBean.setMessage(mdmResponse.getMessage());
		mDMResponseBean.setPartyId(mdmResponse.getPartyId());
		if (mdmResponse.getStatus().equalsIgnoreCase("201")) {
			mDMResponseBean.setStatus(CommonConstants.SUCCESS);
		} else {
			mDMResponseBean.setStatus("FAILURE");
		}

		MDMOmsSrcKeys src = new MDMOmsSrcKeys();
		src.setSrcKeyID(mdmResponse.getSrcKeys().getSrcKeyID());
		src.setSrcKeyIDType(mdmResponse.getSrcKeys().getSrcKeyIDType());
		mDMResponseBean.setSrcKey(src);

		if (mDMRequestBean.getIsAdd()) {
			mDMResponseBean.setAddAddress(true);
			mDMResponseBean.setCustomerLeBillinginfoId(mDMRequestBean.getCustomerLeBillingId());
		}
		mDMResponseBean.setContactId(mdmResponse.getContactId());
		return mDMResponseBean;
	}

	private MDMOmsResponseBean constructMDMErrorOmsResponse(String status, String message,
			MDMOmsRequestBean mDMOmsRequestBean) {
		MDMOmsResponseBean mdmOmsResponseBean = new MDMOmsResponseBean();
		mdmOmsResponseBean.setTpsId(mDMOmsRequestBean.getTpsId());
		mdmOmsResponseBean.setStatus("Failure");
		mdmOmsResponseBean.setMessage(message);
		return mdmOmsResponseBean;
	}

	/**
	 * 
	 * process copfid Details
	 * 
	 * @param input
	 * @throws TclCommonException
	 */
	public void processCopfIdDetails(String input) {

		try {
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			COPFOmsRequest copfOmsRequest = (COPFOmsRequest) Utils.convertJsonToObject(input, COPFOmsRequest.class);
			String request = Utils.convertObjectToJson(convertOmsCofidRequestToCopfIdRequest(copfOmsRequest));

			LOGGER.info("copf Id request to sfdc {}", request);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse sfdcResponse = restClientService.post(copfIdUrl, request, null, null, authHeader);
			LOGGER.info("copf id service response to sfdc {}", sfdcResponse);

			if (sfdcResponse.getStatus() == Status.SUCCESS) {
				COPFResponse copfResponse = (COPFResponse) Utils.convertJsonToObject(sfdcResponse.getData(),
						COPFResponse.class);
				if (copfResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					COPFOmsResponse cOPFOmsResponse = constructCopfIdOmsResponse(copfResponse,
							copfOmsRequest.getTpsId());
					LOGGER.info("MDC Filter token value in before Queue call processCopfIdProduct {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(processCopfIdResponseQueue, Utils.convertObjectToJson(cOPFOmsResponse));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcCopfIdRecheck(request, authHeader, copfOmsRequest);
				}
			}

		} catch (Exception e) {
			LOGGER.error("error in processing product service sfdc", e);
		}

	}

	/**
	 * 
	 * create COPFOmsResponse
	 * 
	 * @param COPFResponse,copfOmsResponse
	 * @throws TclCommonException
	 */

	private COPFOmsResponse constructCopfIdOmsResponse(COPFResponse copfResponse, Integer tpsId) {
		COPFOmsResponse copfOmsResponse = new COPFOmsResponse();
		List<COPFIDOmsList> cOPFIDOmsList = new ArrayList<>();
		if (copfResponse.getCOPFIDList() != null) {
			copfResponse.getCOPFIDList().stream().forEach(copfIdList -> {
				COPFIDOmsList copfidOmsList = new COPFIDOmsList();
				COPFOmsAttribute copfOmsAttribute = new COPFOmsAttribute();
				if (copfIdList.getAttributes() != null) {
					copfOmsAttribute.setType(copfIdList.getAttributes().getType());
					copfOmsAttribute.setUrl(copfIdList.getAttributes().getType());
				}
				copfidOmsList.setAttributes(copfOmsAttribute);
				copfidOmsList.setId(copfIdList.getId());
				copfidOmsList.setName(copfIdList.getName());
				cOPFIDOmsList.add(copfidOmsList);
			});
		}
		copfOmsResponse.setCOPFIDOmsList(cOPFIDOmsList);
		copfOmsResponse.setCustomCOPFIDId(copfResponse.getCustomCOPFIDId());
		copfOmsResponse.setErrorCode(copfResponse.getErrorcode());
		copfOmsResponse.setMessage(copfResponse.getMessage());
		copfOmsResponse.setStatus(copfResponse.getStatus());
		copfOmsResponse.setTpsId(tpsId);
		return copfOmsResponse;
	}

	/**
	 * 
	 * create COPFRequest
	 * 
	 * @param COPFOmsRequest
	 * @throws TclCommonException
	 */

	private COPFRequest convertOmsCofidRequestToCopfIdRequest(COPFOmsRequest cOPFOmsRequest) {
		COPFRequest copfRequest = new COPFRequest();
		List<com.tcl.dias.common.beans.CreateRequestV1> req = new ArrayList<>();
		if (cOPFOmsRequest.getLinkCOPFDetails() != null) {
			cOPFOmsRequest.getLinkCOPFDetails().stream().forEach(linkCopdDetails -> {
				com.tcl.dias.common.beans.CreateRequestV1 request = new com.tcl.dias.common.beans.CreateRequestV1();
				request.setOpportunityId(cOPFOmsRequest.getOpportunityId());
				request.setProductServiceId(cOPFOmsRequest.getProductServiceId());
				request.setRecordTypeName(cOPFOmsRequest.getRecordTypeName());
				COPFIdRecord copfIdRecord = new COPFIdRecord();
				copfIdRecord.setCOPFIDC(linkCopdDetails.getCopfIdC());
				copfIdRecord.setCurrencyIsoCode(cOPFOmsRequest.getCurrencyIsoCode());
				copfIdRecord.setMRCC(linkCopdDetails.getMrcC());
				copfIdRecord.setnRCC(linkCopdDetails.getNrc());
				copfIdRecord.setSiteLocationIdC(linkCopdDetails.getCity());
				copfIdRecord.setLocationC(linkCopdDetails.getCity());
                  copfIdRecord.setEffectiveDateOfTerminationC(linkCopdDetails.getEffectiveDateOfTermination());
		  copfIdRecord.setPreCopfId(linkCopdDetails.getPreCopfId());
				request.setCOPFIdRecord(copfIdRecord);
				req.add(request);
			});
		}
		copfRequest.setCreateRequestV1(req);
		return copfRequest;
	}

	/**
	 * 
	 * process copfid Details
	 * 
	 * @param input
	 * @throws TclCommonException
	 */

	private void sfdcCopfIdRecheck(String request, Map<String, String> authHeader, COPFOmsRequest cOPFOmsRequest)
			throws InterruptedException, TclCommonException {
		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(4);
		String url = copfIdUrl;
		sfdcResponse = restClientService.post(copfIdUrl, request, null, null, authHeader);
		LOGGER.info("cofid request to sfdc {}", sfdcResponse);
		COPFResponse copfResponse = (COPFResponse) Utils.convertJsonToObject(sfdcResponse.getData(),
				COPFResponse.class);
		if (sfdcResponse.getStatus() == Status.SUCCESS) {
			if (copfResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				COPFOmsResponse copfOmsResponse = constructCopfIdOmsResponse(copfResponse, cOPFOmsRequest.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcCOFIDRecheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(processBcrResponseQueue, Utils.convertObjectToJson(copfOmsResponse));

			} else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
				COPFOmsResponse copfOmsResponse = constructCopfIdOmsResponse(copfResponse, cOPFOmsRequest.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcCOFIDRecheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(processCopfIdResponseQueue, Utils.convertObjectToJson(copfOmsResponse));
			}
		} else {
			notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
			COPFOmsResponse copfOmsResponse = constructCopfIdOmsResponse(copfResponse, cOPFOmsRequest.getTpsId());
			LOGGER.info("MDC Filter token value in before Queue call sfdcCOFIDRecheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(processCopfIdResponseQueue, Utils.convertObjectToJson(copfOmsResponse));

		}

	}

	private BCRRequest convertUpdateOmsBCRRequestToBCRRequest(BCROmsRequest bcrOmsRequest) {
		BCRRequest bcrRequest = new BCRRequest();
		List<UpdateRequstV1> updateRequestV1s = new ArrayList<>();
		UpdateRequstV1 updateRequestV1 = new UpdateRequstV1();
		if (bcrOmsRequest.getBcrOmsRecords() != null && !bcrOmsRequest.getBcrOmsRecords().isEmpty()) {
			BCROmsRecord bcrOmsRecord = bcrOmsRequest.getBcrOmsRecords().get(0);
			if (bcrOmsRecord.getBcrOptyProperitiesBeans() != null) {
				BCROptyProperitiesBean bcrOptyProperitiesBean = bcrOmsRecord.getBcrOptyProperitiesBeans();
				BcrRecord bcrRecord = new BcrRecord();
				bcrRecord.setCompletionStatusC(bcrOptyProperitiesBean.getCompletionStatus());
				bcrRecord.setCurrencyIsoCode(bcrOptyProperitiesBean.getQuoteCurrency());
				bcrRecord.setPricingCategoryC(bcrOptyProperitiesBean.getPricingCategory());
				bcrRecord.setPricingDocumentsC(bcrOptyProperitiesBean.getPricingCategory());
				bcrRecord.setRegionOfSaleC(bcrOptyProperitiesBean.getRegion());
				bcrRecord.setStatusC(bcrOptyProperitiesBean.getStatus());
				bcrRecord.setSalesApprovalDate(bcrOptyProperitiesBean.getApproverDate());
				bcrRecord.setCdaCmLevel1ApprovalDate(bcrOptyProperitiesBean.getCdaCmLevel1ApprovalDate());
				bcrRecord.setCdaCmLevel2ApprovalDate(bcrOptyProperitiesBean.getCdaCmLevel2ApprovalDate());
				bcrRecord.setCdaCmLevel3ApprovalDate(bcrOptyProperitiesBean.getCdaCmLevel3ApprovalDate());
				bcrRecord.setId(bcrOptyProperitiesBean.getId());
				updateRequestV1.setBcrRecord(bcrRecord);
				updateRequestV1.setSalesApproverEmailId(bcrOptyProperitiesBean.getApproverId());
				updateRequestV1.setCdaCmLevel1ApproverEmailId(bcrOptyProperitiesBean.getCdaCmLevel1ApproverName());
				updateRequestV1.setCdaCmLevel2ApproverEmailId(bcrOptyProperitiesBean.getCdaCmLevel2ApproverName());
				updateRequestV1.setCdaCmLevel3ApproverEmailId(bcrOptyProperitiesBean.getCdaCmLevel3ApproverName());
				updateRequestV1.setAssignedToEmailId(bcrOptyProperitiesBean.getAssignedToEmailId());
			}

		}
		updateRequestV1s.add(updateRequestV1);
		bcrRequest.setUpdateRequstV1(updateRequestV1s);
		return bcrRequest;
	}

	/**
	 * createEnityRetryCheck
	 *
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void createEnityRetryCheck(String request, Map<String, String> authHeader,
			SfdcPartnerEntityRequest sfdcRequest) throws InterruptedException, TclCommonException {
		RestResponse sfdcReponse;
		TimeUnit.SECONDS.sleep(4);

		String sfdcUrlPartnerCreateEntity = endPointUrl + createPartnerEntityUrl;

		LOGGER.info("partner SFDC endPointUrl {}", endPointUrl);
		LOGGER.info("partner SFDC createPartnerEntityUrl path {}", createPartnerEntityUrl);
		LOGGER.info("partner SFDC entity create retry request URL {}", sfdcUrlPartnerCreateEntity);

		sfdcReponse = restClientService.post(sfdcUrlPartnerCreateEntity, request, null, null, authHeader);
		LOGGER.info("create entity sfdc {}", sfdcReponse);

		if (sfdcReponse.getStatus() == Status.SUCCESS) {
			SfdcPartnerEntityResponse createEntityResponse = (SfdcPartnerEntityResponse) Utils
					.convertJsonToObject(sfdcReponse.getData(), SfdcPartnerEntityResponse.class);
			if (createEntityResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				PartnerEntityResponseBean opBean = constructPartnerEntityResponse(createEntityResponse);
				opBean.setReferenceId(sfdcRequest.getReferenceId());
				LOGGER.info("create entity reponse to queue {}", opBean);
				LOGGER.info("MDC Filter token value in before Queue call createEnityRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(partnerEntityCreateResponseUpdate, Utils.convertObjectToJson(opBean));
			} else {
				LOGGER.info("create entity error {} ", sfdcReponse);
				notificationService.notifySfdcError(sfdcReponse.toString(), request, sfdcUrlPartnerCreateEntity,
						authHeader.toString());
				PartnerEntityResponseBean opBean = new PartnerEntityResponseBean();
				opBean.setStatus(CommonConstants.FAILIURE);
				opBean.setMessage(sfdcReponse.getData());
				opBean.setReferenceId(sfdcRequest.getReferenceId());
				LOGGER.info("MDC Filter token value in before Queue call createEnityRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(partnerEntityCreateResponseUpdate, Utils.convertObjectToJson(opBean));
			}

		} else {
			notificationService.notifySfdcError(sfdcReponse.toString(), request, sfdcUrlPartnerCreateEntity,
					authHeader.toString());
			PartnerEntityResponseBean opBean = new PartnerEntityResponseBean();
			opBean.setStatus(CommonConstants.FAILIURE);
			opBean.setMessage(sfdcReponse.getData());
			opBean.setReferenceId(sfdcRequest.getReferenceId());
			LOGGER.info("MDC Filter token value in before Queue call createEnityRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(partnerEntityCreateResponseUpdate, Utils.convertObjectToJson(opBean));
		}
	}

	/**
	 * processPartnerSdfcOpportunity
	 *
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	public void processPartnerSdfcOpportunity(String input) throws TclCommonException {
		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PARTNER_OPPOERTUNITY_MAPPER.toString());
			SfdcPartnerOpportunityRequest sfdcRequest = (SfdcPartnerOpportunityRequest) mapper
					.transfortToSfdcRequest(input);
			String request = Utils.convertObjectToJson(sfdcRequest);
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			LOGGER.info("opportunity create request {}", request);
			String url = endPointUrl + partnerDealregistrationUrl;
			RestResponse sfdcReponse = restClientService.post(url, request, null, null, authHeader);
			LOGGER.info("opportunity create response {}", sfdcReponse);

			if (sfdcReponse.getStatus() == Status.SUCCESS) {
				SfdcPartnerOppertunityResponse sfdcOpportunity = (SfdcPartnerOppertunityResponse) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcPartnerOppertunityResponse.class);
				if (sfdcOpportunity.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					OpportunityResponseBean opBean = constructPartnerOpportunityRespone(sfdcOpportunity, sfdcRequest
							.getSfdcPartnerOpportunityWrap().stream().findFirst().get().getPortalTransactionIdNGP());
					LOGGER.info("opportunity response to queue {}", opBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcOpportunity {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));

				} else {
					LOGGER.info("Retrying with 4 secs {}", request);
					sfdcPartnerOptyRetryCheck(request, authHeader, sfdcRequest);

				}
			} else {
				LOGGER.info("Retrying with 4 secs {}", request);
				sfdcPartnerOptyRetryCheck(request, authHeader, sfdcRequest);
			}

		} catch (Exception e) {
			LOGGER.error("Error in process Opportunity engine ", e);
		}

	}

	/**
	 * sfdcRetryCheck
	 *
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcPartnerOptyRetryCheck(String request, Map<String, String> authHeader,
			SfdcPartnerOpportunityRequest sfdcRequest) throws InterruptedException, TclCommonException {
		RestResponse sfdcReponse;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + partnerDealregistrationUrl;
		sfdcReponse = restClientService.post(url, request, null, null, authHeader);
		LOGGER.info("opportunity sfdc {}", sfdcReponse);

		if (sfdcReponse.getStatus() == Status.SUCCESS) {
			SfdcPartnerOppertunityResponse sfdcOpportunity = (SfdcPartnerOppertunityResponse) Utils
					.convertJsonToObject(sfdcReponse.getData(), SfdcPartnerOppertunityResponse.class);
			if (sfdcOpportunity.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				OpportunityResponseBean opBean = constructPartnerOpportunityRespone(sfdcOpportunity, sfdcRequest
						.getSfdcPartnerOpportunityWrap().stream().findFirst().get().getPortalTransactionIdNGP());
				LOGGER.info("opportunity response to queue {}", opBean);
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
			} else {
				LOGGER.info("opportunity update error in updating stage {} ", sfdcReponse);
				notificationService.notifySfdcError(sfdcReponse.toString(), request, url, authHeader.toString());
				OpportunityResponseBean opBean = new OpportunityResponseBean();
				opBean.setError(true);
				opBean.setErrorMessage(sfdcReponse.getData());
				opBean.setRefId(sfdcRequest.getSfdcPartnerOpportunityWrap().stream().findFirst().get()
						.getPortalTransactionIdNGP());
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
			}

		} else {
			notificationService.notifySfdcError(sfdcReponse.toString(), request, url, authHeader.toString());
			OpportunityResponseBean opBean = new OpportunityResponseBean();
			opBean.setError(true);
			opBean.setErrorMessage(sfdcReponse.getData());
			opBean.setRefId(
					sfdcRequest.getSfdcPartnerOpportunityWrap().stream().findFirst().get().getPortalTransactionIdNGP());
			LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
		}
	}

	private OpportunityResponseBean constructPartnerOpportunityRespone(
			SfdcPartnerOppertunityResponse sfOpportunityResponseBean, String portalTransactionId) {
		OpportunityResponseBean responseBean = new OpportunityResponseBean();
		if (sfOpportunityResponseBean != null) {
			responseBean.setCustomOptyId(sfOpportunityResponseBean.getOpportunityResponse().stream().findFirst().get()
					.getOpportunityExternalId());
			responseBean.setMessage(sfOpportunityResponseBean.getMessage());
			responseBean.setStatus(sfOpportunityResponseBean.getStatus());
			responseBean.setError(false);
			OpportunityResponse opportunityResponse = new OpportunityResponse();
			opportunityResponse.setPortalTransactionId(portalTransactionId);
			opportunityResponse.setType("New Order");
			opportunityResponse.setStageName(sfOpportunityResponseBean.getOpportunityResponse().stream().findFirst()
					.get().getOpportunityStageName());
			responseBean.setOpportunity(opportunityResponse);
		}
		return responseBean;
	}
	
	public void processCreditCheckQueryAPI(String request) {
		try {
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			SfdcCreditCheckQueryRequest creditCheckRequest = (SfdcCreditCheckQueryRequest) Utils.convertJsonToObject(request, SfdcCreditCheckQueryRequest.class);
			String inputRequest  = Utils.convertObjectToJson(convertOmsRequestToCreditCheckQueryRequest(creditCheckRequest));
			LOGGER.info("processCreditCheckQueryAPI request to sfdc {}", inputRequest);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			String url = endPointUrl + getCreditCheckQueryUrl ;
			RestResponse sfdcResponse = restClientService.post(url, inputRequest, null, null,
					authHeader);
			if(sfdcResponse != null)
			LOGGER.info("processCreditCheckQueryAPI response from sfdc {}", sfdcResponse.toString());

			if (sfdcResponse.getStatus() == Status.SUCCESS) {
				SfdcCreditCheckQueryResponseBean queryResponse = (SfdcCreditCheckQueryResponseBean) Utils
						.convertJsonToObject(sfdcResponse.getData(), SfdcCreditCheckQueryResponseBean.class);
				if (queryResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					LOGGER.info("response for tps id {},  opty Id {}", creditCheckRequest.getTpsId(), queryResponse.getSfdcCreditCheckQueryBean().get(0).getOpportunityIDC() );
					CreditCheckQueryResponseBean creditCheckResponse = constructCreditCheckResponse(queryResponse, creditCheckRequest.getTpsId());
					LOGGER.info("MDC Filter token value in before Queue call processCreditCheckQueryAPI {} :");
					mQUtils.send(sfdcCreditCheckResponseQueue, Utils.convertObjectToJson(creditCheckResponse));

				} else {
					LOGGER.info("Retrying with 3 secs {}", inputRequest);
					sfdcCreditCheckQueryRecheck(inputRequest, authHeader, creditCheckRequest);
				}
			}

		} catch (Exception e) {
			LOGGER.error("error in processing product service sfdc", e);
		}
		
	}

	private CreditCheckQueryResponseBean constructCreditCheckResponse(SfdcCreditCheckQueryResponseBean queryResponse, Integer tpsId) {
		CreditCheckQueryResponseBean response = new CreditCheckQueryResponseBean();
		if (Objects.nonNull(queryResponse.getSfdcCreditCheckQueryBean())
				&& !queryResponse.getSfdcCreditCheckQueryBean().isEmpty()) {
			response.setMessage(queryResponse.getMessage());
			response.setSfdcCreditCheckQueryResponse(constructResponse(queryResponse.getSfdcCreditCheckQueryBean()));
			response.setStatus(queryResponse.getStatus());
			response.setTpsId(tpsId);
		} 
		return response;
	}

	private List<CreditCheckQueryResponse> constructResponse(
			List<SfdcCreditCheckQueryResponse> sfdcCreditCheckQueryBean) {
		List<CreditCheckQueryResponse> responseList = new ArrayList<>();
		sfdcCreditCheckQueryBean.forEach(queryEntry -> {
			CreditCheckQueryResponse response = new CreditCheckQueryResponse();
			response.setOpportunityId(queryEntry.getOpportunityIDC());
			response.setAnnualizedContractValue(queryEntry.getAnnualizedContractValueC());
			response.setApprovedBy(queryEntry.getApprovedByC());
			response.setBillingFrequency(queryEntry.getBillingFrequencyC());
			response.setBillingMethod(queryEntry.getBillingMethodC());
			response.setBundledMRC(queryEntry.getBundledMRCC());
			response.setConditionalApprovalRemarks(queryEntry.getConditionalApprovalRemarksC());
			response.setConditionalApprovalType(queryEntry.getConditionalApprovalTypeC());
			response.setCopfId(queryEntry.getCopfIdC());
			response.setCreditControlProcessComments(queryEntry.getCreditControlProcessCommentsC());
			response.setCreditLimit(queryEntry.getCreditLimitC());
			response.setCreditRating(queryEntry.getCreditRatingC());
			response.setCreditRemarks(queryEntry.getCreditRemarksC());
			response.setCustomerContractingEntity(queryEntry.getCustomerContractingEntityC());
			response.setDateOfCreditApproval(queryEntry.getDateOfCreditApprovalC());
			response.setDifferentialMRC(queryEntry.getDifferentialMRCC());
			response.setIccEnterpiceVoiceProductFlag(queryEntry.getIccEnterpiceVoiceProductFlagC());
			response.setId(queryEntry.getId());
			response.setIsPreApprovedOpportunity(queryEntry.getIsPreApprovedOpportunityC());
			if(Objects.nonNull(queryEntry.getMrcNrcC()) && StringUtils.isNotBlank(queryEntry.getMrcNrcC()))
				response.setMrcNrc(queryEntry.getMrcNrcC());
			else
				response.setMrcNrc(CommonConstants.EMPTY);
			response.setNotifyCreditControlTeam(queryEntry.getNotifyCreditControlTeamC());
			response.setOpportunityMRC(queryEntry.getOpportunityMRCC());
			response.setOpportunityNRC(queryEntry.getOpportunityNRCC());
			response.setPaymentTerm(queryEntry.getPaymentTermC());
			response.setPreviousMRC(queryEntry.getPreviousMRCC());
			response.setRedoCreditVerification(queryEntry.getRedoCreditVerificationC());
			response.setReservedBy(queryEntry.getReservedByC());
			response.setSecurityDepositAmount(queryEntry.getSecurityDepositAmountC());
			response.setSecurityDepositRequired(queryEntry.getSecurityDepositRequiredC());
			response.setStatusOfCreditControl(queryEntry.getStatusOfCreditControlC());
			response.setTataBillingEntity(queryEntry.getTataBillingEntityC());
			response.setPortalTransactionId(queryEntry.getPortalTransactionIdC());
			response.setCustomerName(queryEntry.getCustomerNameC());
			response.setAccountId(queryEntry.getAccountId());
			ProductServicesQueryBean productServices = new ProductServicesQueryBean();
			List<ProductServicesRecord> productServicesList = new ArrayList<>();
			if(Objects.nonNull(queryEntry.getProductsServices()) && Objects.nonNull(queryEntry.getProductsServices().getRecord()) && !queryEntry.getProductsServices().getRecord().isEmpty()) {
				queryEntry.getProductsServices().getRecord().forEach(record -> {
					ProductServicesRecord productRecord = new ProductServicesRecord();
					productRecord.setOpportunityNameC(record.getOpportunityNameC());
					productRecord.setProductMRCc(record.getProductMRCc());
					productRecord.setProductNRCc(record.getProductNRCc());
					productServicesList.add(productRecord);
				});
			}
			productServices.setRecord(productServicesList);
			response.setProductServices(productServices);
			if(queryEntry.getCustomerContractingEntity() != null && queryEntry.getCustomerContractingEntity().getCustomerCodeC() != null) {
			CustomerContractingEntityBean customerContractingEntityBean = new CustomerContractingEntityBean();
			customerContractingEntityBean.setCustomerCode(queryEntry.getCustomerContractingEntity().getCustomerCodeC());
			response.setCustomerContractingEntityBean(customerContractingEntityBean);
			}
			responseList.add(response);
			
		});
		return responseList;
	}

	private SfdcCreditCheckQueryRequestBean convertOmsRequestToCreditCheckQueryRequest(SfdcCreditCheckQueryRequest creditCheckRequest) {
		SfdcCreditCheckQueryRequestBean request = new SfdcCreditCheckQueryRequestBean();
		request.setFields(creditCheckRequest.getFields());
		request.setObjectName(creditCheckRequest.getObjectName());
		request.setSourceSystem(creditCheckRequest.getSourceSystem());
		request.setTransactionId(creditCheckRequest.getTransactionId());
		request.setWhereClause(creditCheckRequest.getWhereClause());
		return request;
	}
	
	
	private void sfdcCreditCheckQueryRecheck(String request, Map<String, String> authHeader, SfdcCreditCheckQueryRequest cRequest) throws InterruptedException, TclCommonException {
		RestResponse sfdcResponse;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + getCreditCheckQueryUrl ;
		sfdcResponse = restClientService.post(url, request, null, null,
				authHeader);
		LOGGER.info("credit check request to sfdc {}, request {}", sfdcResponse, cRequest);
		SfdcCreditCheckQueryResponseBean sfdcCreditCheckResponse = (SfdcCreditCheckQueryResponseBean) Utils
				.convertJsonToObject(sfdcResponse.getData(), SfdcCreditCheckQueryResponseBean.class);
	     if (sfdcResponse.getStatus() == Status.SUCCESS) {
			if (sfdcCreditCheckResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				CreditCheckQueryResponseBean creditCheckResponse  = constructCreditCheckResponse(sfdcCreditCheckResponse, cRequest.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcCOFIDRecheck {} :");
				mQUtils.send(sfdcCreditCheckResponseQueue, Utils.convertObjectToJson(creditCheckResponse));
				LOGGER.info("Response from sfdc service response json {}", creditCheckResponse);

			}else {
				notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
				CreditCheckQueryResponseBean creditCheckResponse  = constructCreditCheckResponse(sfdcCreditCheckResponse, cRequest.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcCreditCheckQueryRecheck {} :");
				mQUtils.send(sfdcCreditCheckResponseQueue, Utils.convertObjectToJson(creditCheckResponse));
			}
		}else {
			notificationService.notifySfdcError(sfdcResponse.toString(), request, url, authHeader.toString());
			CreditCheckQueryResponseBean creditCheckResponse  = constructCreditCheckResponse(sfdcCreditCheckResponse, cRequest.getTpsId());
			LOGGER.info("MDC Filter token value in before Queue call sfdcCreditCheckQueryRecheck {} :");
			mQUtils.send(sfdcCreditCheckResponseQueue, Utils.convertObjectToJson(creditCheckResponse));

		}
		
	}
	
	public CreditCheckQueryResponseBean processRetriggerCreditCheckQueryAPI(String request) {
		CreditCheckQueryResponseBean creditCheckResponse = null;
		try {
			
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			SfdcCreditCheckQueryRequest creditCheckRequest = (SfdcCreditCheckQueryRequest) Utils.convertJsonToObject(request, SfdcCreditCheckQueryRequest.class);
			String inputRequest  = Utils.convertObjectToJson(convertOmsRequestToCreditCheckQueryRequest(creditCheckRequest));
			LOGGER.info("processRetriggerCreditCheckQueryAPI request to sfdc {}", inputRequest);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			String url = endPointUrl + getCreditCheckQueryUrl ;
			RestResponse sfdcResponse = restClientService.post(url, inputRequest, null, null,
					authHeader);
			LOGGER.info("processRetriggerCreditCheckQueryAPI response from sfdc {}", sfdcResponse);

			if (sfdcResponse.getStatus() == Status.SUCCESS) {
				SfdcCreditCheckQueryResponseBean queryResponse = (SfdcCreditCheckQueryResponseBean) Utils
						.convertJsonToObject(sfdcResponse.getData(), SfdcCreditCheckQueryResponseBean.class);
				if (queryResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					creditCheckResponse = constructCreditCheckResponse(queryResponse, creditCheckRequest.getTpsId());

				} else {					
						notificationService.notifySfdcError(queryResponse.toString(), request, url, authHeader.toString());
						creditCheckResponse  = constructCreditCheckResponse(queryResponse, creditCheckRequest.getTpsId());
					
				}
			}

		} catch (Exception e) {
			LOGGER.error("error in processing retrigger credit check sfdc", e);
		}
		return creditCheckResponse;
		
	}

	public String getServiceRequest(String request) throws TclCommonException {
		LOGGER.info("Entering getSErvice Request , input {}", request.toString());
		String response = null;
	ServiceRequestBean serviceRequestBean = (ServiceRequestBean) Utils.convertJsonToObject(request, ServiceRequestBean.class);
	if(SfdcConstants.CREATE_OPPORTUNITY.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())){
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.OPPORTUNITY_MAPPER.toString());
		SfdcOpportunityRequest sfdcRequest = (SfdcOpportunityRequest) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(sfdcRequest);
		
	} else if(SfdcConstants.CREATE_PRODUCT.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PROCESS_MAPPER.toString());
		SfdcProductServiceBean res = (SfdcProductServiceBean) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(res);
	} else if(SfdcConstants.UPDATE_PRODUCT.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PRODUCT_UPDATE.toString());
		SfdcUpdateProductServiceBean res = (SfdcUpdateProductServiceBean) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(res);
	} else if(SfdcConstants.DELETE_PRODUCT.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PRODUCT_DELETE.toString());
		SfdcDeleteProductBean res = (SfdcDeleteProductBean) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(res);
	} else if (SfdcConstants.UPDATE_OPPORTUNITY.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.STAGING_MAPPER.toString());
		SfdStagingBean res = (SfdStagingBean) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(res);
	}  else if (SfdcConstants.UPDATE_SITE.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.SITE_MAPPER.toString());
		SfdcSiteSolutionBean respone = (SfdcSiteSolutionBean) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(respone);
	} else if(SfdcConstants.CREATE_COPF_ID.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		COPFOmsRequest copfOmsRequest = (COPFOmsRequest) Utils.convertJsonToObject(serviceRequestBean.getRequest(), COPFOmsRequest.class);
		response = Utils.convertObjectToJson(convertOmsCofidRequestToCopfIdRequest(copfOmsRequest));
	} else if(SfdcConstants.CREATE_FEASIBILITY.toString().equalsIgnoreCase(serviceRequestBean.getServiceType()) || 
			SfdcConstants.UPDATE_FEASIBILITY.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.FEASIBILITY_MAPPER.toString());
		FeasibilityRequestBean requestBean = (FeasibilityRequestBean) Utils.convertJsonToObject(serviceRequestBean.getRequest(),
				FeasibilityRequestBean.class);
		if (requestBean.getCreateOrUpdate().equalsIgnoreCase("create")) {
			SfdcFeasibilityRequestBean	sfdcFeasibilityRequestBean = (SfdcFeasibilityRequestBean) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
			String feasibilityRequest = Utils.convertObjectToJson(sfdcFeasibilityRequestBean);
		response = Utils.convertObjectToJson(feasibilityRequest);
		} else {
			SfdcFeasibilityUpdateRequestBean	sfdcFeasibilityUpdateBean = (SfdcFeasibilityUpdateRequestBean) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
			String feasibilityRequest = Utils.convertObjectToJson(sfdcFeasibilityUpdateBean);
			response = Utils.convertObjectToJson(feasibilityRequest);
		}

	} else if(SfdcConstants.CREATE_WAIVER.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.WAIVER_MAPPER.toString());
		SfdcWaiverRequest res = (SfdcWaiverRequest) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(res);
	} else if(SfdcConstants.UPDATE_WAIVER.toString().equalsIgnoreCase(serviceRequestBean.getServiceType())) {
		ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.WAIVER_UPDATE_MAPPER.toString());
		SfdcUpdateWaiverRequest res = (SfdcUpdateWaiverRequest) mapper.transfortToSfdcRequest(serviceRequestBean.getRequest());
		response = Utils.convertObjectToJson(res);
	}
	return response;
	}

	/**
	 * Process Partner Sales Funnel in SFDC
	 *
	 * @param request
	 * @throws TclCommonException
	 */
	public List<SfdcActiveCampaignResponseBean> getSfdcCampaignData(String request) {
		List<SfdcActiveCampaignResponseBean> response = new ArrayList<>();
		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.SALES_FUNNEL.toString());
			SfdcSalesFunnelRequestBean sfdcSalesFunnelRequest = (SfdcSalesFunnelRequestBean) mapper
					.transfortToSfdcRequest(request);
			String sfdcRequest = Utils.convertObjectToJson(sfdcSalesFunnelRequest);
			RestResponse sfdcReponse = getSfdcSalesFunnelResponse(request, sfdcRequest);

			if (sfdcReponse.getStatus().equals(Status.SUCCESS)) {
				SfdcCampaignReponseBean salesFunnelResponse = (SfdcCampaignReponseBean) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcCampaignReponseBean.class);
				if (salesFunnelResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					response = constructCampaignResponse(salesFunnelResponse);
					LOGGER.debug("get sales funnel response to queue : {}", response);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in process sales data of partner ", e);
		}
		return response;
	}

	private void processBundleSdfcOpportunity(SfdcOpportunityBundleRequest sfdcRequest) {
		try {
			
			String request = Utils.convertObjectToJson(sfdcRequest);
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.AUTH.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			LOGGER.info("opportunity create request {}", request);
			String url = endPointUrl + createOpportunityUrl;
			RestResponse sfdcReponse = restClientService.post(url, request, null, null, authHeader);
			LOGGER.info("opportunity create response {}", sfdcReponse);

			if (sfdcReponse.getStatus() == Status.SUCCESS) {
				SfdcOpportunityBundleResponseBean sfdcOpportunity = (SfdcOpportunityBundleResponseBean) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcOpportunityBundleResponseBean.class);
				if (sfdcOpportunity.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					BundledOpportunityResponseBean bundleOpBean = constructBundleOpportunityRespone(sfdcOpportunity);
					LOGGER.info("opportunity response to queue {}", bundleOpBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcOpportunity {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(bundleOpBean));

				} else {
					LOGGER.info("Retrying with 4 secs {}", request);
					sfdcRetryBundleCheck(request, authHeader, sfdcRequest);

				}
			} else {
				LOGGER.info("Retrying with 4 secs {}", request);
				sfdcRetryBundleCheck(request, authHeader, sfdcRequest);
			}
		} catch (Exception e) {
			LOGGER.error("Error in process Opportunity engine ", e);
		}
	}

	/**
	 * 
	 * @param opportunityResponseBean
	 * @return
	 */
	private BundledOpportunityResponseBean constructBundleOpportunityRespone(
			SfdcOpportunityBundleResponseBean opportunityResponseBean) {
		BundledOpportunityResponseBean responseBean = new BundledOpportunityResponseBean();
		if (opportunityResponseBean != null) {
			responseBean.setCustomOptyId(opportunityResponseBean.getCustomOptyId());
			responseBean.setMessage(opportunityResponseBean.getMessage());
			responseBean.setStatus(opportunityResponseBean.getStatus());
			SfdcBundleOpportunity opportunity = opportunityResponseBean.getOpportunity();
			BundledOpportunityResponse bundleResponse = constructBundleOpportunity(opportunity);
			responseBean.setOpportunity(bundleResponse);
		}
		return responseBean;
	}

	/**
	 * 
	 * @param opportunity
	 * @return
	 */
	private BundledOpportunityResponse constructBundleOpportunity(SfdcBundleOpportunity opportunity) {
		BundledOpportunityResponse bundleResponse = new BundledOpportunityResponse();
		if (opportunity != null) {
			AttributesResponseBean attributesResponseBean = new AttributesResponseBean();
			attributesResponseBean.setType(opportunity.getAttributes().getType());
			attributesResponseBean.setUrl(opportunity.getAttributes().getUrl());
			bundleResponse.setAttributes(attributesResponseBean);
			bundleResponse.setBundledSubOrderTypeTwoC(opportunity.getBundledSubOrderTypeTwoC());
			bundleResponse.setBundledProductFourC(opportunity.getBundledProductFourC());
			bundleResponse.setTermsOfMonthsC(opportunity.getTermsOfMonthsC());
			bundleResponse.setDescription(opportunity.getDescription());
			bundleResponse.setAccountId(opportunity.getAccountId());
			bundleResponse.setOpportunityClassificationC(opportunity.getOpportunityClassificationC());
			bundleResponse.setCustomerChurnedC(opportunity.getCustomerChurnedC());
			bundleResponse.setTataBillingEntityC(opportunity.getTataBillingEntityC());
			bundleResponse.setSelectProductTypeC(opportunity.getSelectProductTypeC());
			bundleResponse.setCurrentCircuitServiceIDc(opportunity.getCurrentCircuitServiceIDc());
			bundleResponse.setCloseDate(opportunity.getCloseDate());
			bundleResponse.setName(opportunity.getName());
			bundleResponse.setSubTypeC(opportunity.getSubTypeC());
			bundleResponse.setPortalTransactionIdC(opportunity.getPortalTransactionIdC());
			bundleResponse.setOwnerId(opportunity.getOwnerId());
			bundleResponse.setRecordTypeId(opportunity.getRecordTypeId());
			bundleResponse.setPreviousMRCC(opportunity.getPreviousMRCC());
			bundleResponse.setBundledOrderTypeOneC(opportunity.getBundledOrderTypeOneC());
			bundleResponse.setBundledProductThreeC(opportunity.getBundledProductThreeC());
			bundleResponse.setStageName(opportunity.getStageName());
			bundleResponse.setPreviousNRCc(opportunity.getPreviousNRCc());
			bundleResponse.setBundledProductTwoC(opportunity.getBundledProductTwoC());
			bundleResponse.setBillingFrequencyC(opportunity.getBillingFrequencyC());
			bundleResponse.setCurrencyIsoCode(opportunity.getCurrencyIsoCode());
			bundleResponse.setBundledOrderTypeFourC(opportunity.getBundledOrderTypeFourC());
			bundleResponse.setPaymentCurrencyC(opportunity.getPaymentCurrencyC());
			bundleResponse.setCofTypeC(opportunity.getCofTypeC());
			bundleResponse.setEffectiveDateOfChangeC(opportunity.getEffectiveDateOfChangeC());
			bundleResponse.setBundledSubOrderTypeOneC(opportunity.getBundledSubOrderTypeOneC());
			bundleResponse.setType(opportunity.getType());
			bundleResponse.setCustomerContractingEntityC(opportunity.getCustomerContractingEntityC());
			bundleResponse.setMigrationSourceSystemC(opportunity.getMigrationSourceSystemC());
			bundleResponse.setBillingmethodc(opportunity.getBillingmethodc());
			bundleResponse.setBundledSubOrderTypeThreeC(opportunity.getBundledSubOrderTypeThreeC());
			bundleResponse.setOrderCategoryC(opportunity.getOrderCategoryC());
			bundleResponse.setLeadTimeToRfsC(opportunity.getLeadTimeToRfsC());
			bundleResponse.setReferralToPartnerC(opportunity.getReferralToPartnerC());
			bundleResponse.setIllAutoCreationC(opportunity.getIllAutoCreationC());
			bundleResponse.setBundleNameC(opportunity.getBundleNameC());
			bundleResponse.setPaymentTermC(opportunity.getPaymentTermC());
			bundleResponse.setBundledOrderTypeTwoC(opportunity.getBundledOrderTypeTwoC());
			bundleResponse.setOpportunitySpecificationC(opportunity.getOpportunitySpecificationC());
			bundleResponse.setId(opportunity.getId());
		}
		return bundleResponse;
	}

	/**
	 * sfdcRetryCheck
	 * 
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcRetryBundleCheck(String request, Map<String, String> authHeader,
			SfdcOpportunityBundleRequest sfdcRequest) throws InterruptedException, TclCommonException {
		RestResponse sfdcReponse;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + createOpportunityUrl;
		sfdcReponse = restClientService.post(url, request, null, null, authHeader);
		LOGGER.info("opportunity sfdc {}", sfdcReponse);

		if (sfdcReponse.getStatus() == Status.SUCCESS) {
			SfdcOpportunityBundleResponseBean sfdcOpportunity = Utils.convertJsonToObject(sfdcReponse.getData(),
					SfdcOpportunityBundleResponseBean.class);
			if (sfdcOpportunity.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				BundledOpportunityResponseBean bundleOpBean = constructBundleOpportunityRespone(sfdcOpportunity);
				LOGGER.info("opportunity response to queue {}", bundleOpBean);
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(bundleOpBean));
			} else {
				LOGGER.info("opportunity update error in updating stage {} ", sfdcReponse);
				notificationService.notifySfdcError(sfdcReponse.toString(), request, url, authHeader.toString());
				OpportunityResponseBean opBean = new OpportunityResponseBean();
				opBean.setError(true);
				opBean.setErrorMessage(sfdcReponse.getData());
				opBean.setRefId(sfdcRequest.getCreateRequestV1().getOpportunity().getPortalTransactionIdC());
				LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
			}

		} else {
			notificationService.notifySfdcError(sfdcReponse.toString(), request, url, authHeader.toString());
			OpportunityResponseBean opBean = new OpportunityResponseBean();
			opBean.setError(true);
			opBean.setErrorMessage(sfdcReponse.getData());
			opBean.setRefId(sfdcRequest.getCreateRequestV1().getOpportunity().getPortalTransactionIdC());
			LOGGER.info("MDC Filter token value in before Queue call sfdcRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(opportunityResponseQueue, Utils.convertObjectToJson(opBean));
		}
	}
	
	/**
	 * @author Madhumiethaa Palanisamy
	 * @param request
	 * @return
	 */
	public OwnerRegionQueryResponseBean processOwnerRegionQueryAPI(String request) {
		OwnerRegionQueryResponseBean response = new OwnerRegionQueryResponseBean();
		try {
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			SfdcCreditCheckQueryRequest creditCheckRequest = (SfdcCreditCheckQueryRequest) Utils
					.convertJsonToObject(request, SfdcCreditCheckQueryRequest.class);
			String inputRequest = Utils
					.convertObjectToJson(convertOmsRequestToCreditCheckQueryRequest(creditCheckRequest));
			LOGGER.info("processOwnerRegionQueryAPI request to sfdc {}", inputRequest);
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			String url = getOwnerRegionUrl + getCreditCheckQueryUrl;
			RestResponse sfdcResponse = restClientService.post(url, inputRequest, null, null, authHeader);
			if (sfdcResponse != null)
				LOGGER.info("processOwnerRegionQueryAPI response from sfdc {}", sfdcResponse.toString());

			if (sfdcResponse.getStatus() == Status.SUCCESS) {
				SfdcOwnerRegionQueryResponseBean queryResponse = (SfdcOwnerRegionQueryResponseBean) Utils
						.convertJsonToObject(sfdcResponse.getData(), SfdcOwnerRegionQueryResponseBean.class);
				if (queryResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					if (Objects.nonNull(queryResponse.getSfdcOwnerRegionQueryBean())
							&& !queryResponse.getSfdcOwnerRegionQueryBean().isEmpty()) {
						response.setMessage(queryResponse.getMessage());
						response.setId(queryResponse.getSfdcOwnerRegionQueryBean().get(0).getId());
						response.setOpportunityOwnersRegionc(
								queryResponse.getSfdcOwnerRegionQueryBean().get(0).getOpportunityOwnersRegionc());
						response.setStatus(queryResponse.getStatus());
					}

				} else {
					notificationService.notifySfdcError(queryResponse.toString(), request, url, authHeader.toString());
					if (Objects.nonNull(queryResponse.getSfdcOwnerRegionQueryBean())
							&& !queryResponse.getSfdcOwnerRegionQueryBean().isEmpty()) {
						response.setMessage(queryResponse.getMessage());
						response.setId(queryResponse.getSfdcOwnerRegionQueryBean().get(0).getId());
						response.setOpportunityOwnersRegionc(
								queryResponse.getSfdcOwnerRegionQueryBean().get(0).getOpportunityOwnersRegionc());
						response.setStatus(queryResponse.getStatus());
					}

				}
			}

		} catch (Exception e) {
			LOGGER.error("error in processing retrigger credit check sfdc", e);
		}
		return response;

	}

	/**
	 * Process Partner Sales Funnel in SFDC
	 *
	 * @param request
	 * @throws TclCommonException
	 */
	public SfdcAccountEntityCreationResponse createAccountEntityRequest(String request) {
		SfdcAccountEntityCreationResponse sfdcAccountEntityCreationResponse = null;
		try {
//			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.SALES_FUNNEL.toString());
//			SfdcSalesFunnelRequestBean sfdcSalesFunnelRequest = (SfdcSalesFunnelRequestBean) mapper
//					.transfortToSfdcRequest(request);
//			String sfdcRequest = Utils.convertObjectToJson(sfdcSalesFunnelRequest);
			RestResponse sfdcReponse = getSfdcAccountCreateResponse(request, request);
			if (sfdcReponse.getStatus().equals(Status.SUCCESS)) {
				sfdcAccountEntityCreationResponse = (SfdcAccountEntityCreationResponse) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcAccountEntityCreationResponse.class);
				if (sfdcAccountEntityCreationResponse.getMessage().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					LOGGER.debug("get account response to queue : {}", sfdcAccountEntityCreationResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in process sales data of partner ", e);
		}
		return sfdcAccountEntityCreationResponse;
	}


	private RestResponse getSfdcAccountCreateResponse(String request, String sfdcRequest) throws TclCommonException {
		SfdcAccessToken accessToken = getAuthToken();

		String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE + accessToken.getAccessToken();
		Map<String, String> authHeader = new HashMap<>();
		authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
		LOGGER.debug("partner sales funnel request : {}", request);

		RestResponse sfdcReponse = restClientService.post(endPointUrl+sfdcAccountCreationRequestUrl, sfdcRequest, null, null, authHeader);

		LOGGER.debug("partner sales funnel response : {}", sfdcReponse);
		return sfdcReponse;
	}

	/**
	 * Process Partner Sales Funnel in SFDC
	 *
	 * @param request
	 * @throws TclCommonException
	 */
	public AccountUpdationResponse updateAccountRequest(String request) {
		AccountUpdationResponse accountUpdationResponse = null;
		try {
//			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.SALES_FUNNEL.toString());
//			SfdcSalesFunnelRequestBean sfdcSalesFunnelRequest = (SfdcSalesFunnelRequestBean) mapper
//					.transfortToSfdcRequest(request);
//			String sfdcRequest = Utils.convertObjectToJson(sfdcSalesFunnelRequest);
			RestResponse sfdcReponse = getSfdcAccountUpdateResponse(request, request);
			if (sfdcReponse.getStatus().equals(Status.SUCCESS)) {
				accountUpdationResponse = (AccountUpdationResponse) Utils
						.convertJsonToObject(sfdcReponse.getData(), AccountUpdationResponse.class);
				if (accountUpdationResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					LOGGER.debug("get account response to queue : {}", accountUpdationResponse);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in process sales data of partner ", e);
		}
		return accountUpdationResponse;
	}


	private RestResponse getSfdcAccountUpdateResponse(String request, String sfdcRequest) throws TclCommonException {
		SfdcAccessToken accessToken = getAuthToken();

		String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE + accessToken.getAccessToken();
		Map<String, String> authHeader = new HashMap<>();
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.set(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
		httpHeaders.set(SfdcConstants.CONTENT_TYPE.toString(),"application/json");
		authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
		LOGGER.debug("partner sales funnel request : {}", request);

		RestResponse sfdcReponse = restClientService.put(endPointUrl+sfdcAccountUpdateRequestUrl, sfdcRequest, httpHeaders, null, null);

		LOGGER.debug("partner sales funnel response : {}", sfdcReponse);
		return sfdcReponse;
	}
	
	public void processSdfcWaiver(String input) throws TclCommonException {

		try {

			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.WAIVER_MAPPER.toString());
			SfdcWaiverRequest respone = (SfdcWaiverRequest) mapper.transfortToSfdcRequest(input);
			String request = Utils.convertObjectToJson(respone);
			SfdcAccessToken accessToken = getAuthToken();
			LOGGER.info("createWaiver request" + request);
			String authenticationAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authenticationAccessToken);
			RestResponse response = restClientService.post(endPointUrl + waiverUrl, request, null, null, authHeader);
			LOGGER.info("site sfdc respone {}", response);

			TerminationWaiverBean terminationWaiverBean = (TerminationWaiverBean) Utils.convertJsonToObject(input,
					TerminationWaiverBean.class);
			if (response.getStatus() == Status.SUCCESS) {
//				List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = (List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean>) Utils
//						.convertJsonToObject(response.getData(),
//								List.class);
				ObjectMapper objectMapper = new ObjectMapper();
				List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = objectMapper.readValue(response.getData(),
									new TypeReference<List<SfdcWaiverResponseBean>>() {});
				if (waiverResponse.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					WaiverResponseBean waiverResponseBean = constructWaiverResponseAndSendToQueue(waiverResponse.get(0), terminationWaiverBean.getTpsId());
					LOGGER.info("waiver response to queue {}", waiverResponseBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcWaiver {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(waiverResponseQueue, Utils.convertObjectToJson(waiverResponseBean));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcWaiverRetryCheck(request, authHeader, input);
				}
			} else {
				notificationService.notifySfdcError(response.toString(), request, endPointUrl + waiverUrl,
						authHeader.toString());
				WaiverResponseBean waiverResponseBean = new WaiverResponseBean();
				waiverResponseBean.setError(true);
				waiverResponseBean.setErrorMessage(response.getData());
				waiverResponseBean.setTpsId(terminationWaiverBean.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(waiverResponseQueue, Utils.convertObjectToJson(waiverResponseBean));
			}

		} catch (Exception e) {
			LOGGER.error("Error in process Sdfc site", e);
		}
		}
	
	public void processSdfcPartnerEntityContact(String input) throws TclCommonException {
		try {
			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.PARTNER_ENTITY_CONTACT.toString());
			SfdcPartnerEntityContactRequest sfdcRequest = (SfdcPartnerEntityContactRequest) mapper.transfortToSfdcRequest(input);
			// Setting Default Value
			sfdcRequest.getSfdcPartnerEntityContactBean().getSfdcPartnerEntityContact().setActive("Yes");
			String request = Utils.convertObjectToJson(sfdcRequest);
			SfdcAccessToken accessToken = getAuthToken();

			String authAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authAccessToken);
			LOGGER.info("partner entity contact create request {}", request);

			RestResponse sfdcReponse = restClientService.post(endPointUrl + createPartnerEntityContactUrl, request, null, null, authHeader);

			LOGGER.info("partner entity contact create response {}", sfdcReponse);

			if (sfdcReponse.getStatus() == Status.SUCCESS) {
				SfdcPartnerEntityContactResponse partnerEntityContactResponse = (SfdcPartnerEntityContactResponse) Utils
						.convertJsonToObject(sfdcReponse.getData(), SfdcPartnerEntityContactResponse.class);
				if (partnerEntityContactResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					PartnerEntityContactResponseBean partnerEntityContactResponseBean = constructPartnerEntityContactResponse(partnerEntityContactResponse);
					partnerEntityContactResponseBean.setReferenceId(sfdcRequest.getReferenceId());
					LOGGER.info("partner entity contact response to queue {}", partnerEntityContactResponseBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcPartnerEntityContact {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(partnerEntityContactCreateResponseUpdate, Utils.convertObjectToJson(partnerEntityContactResponseBean));
				} else {
					LOGGER.info("Retrying with 4 secs for create entity {}", request);
					createEnityContactRetryCheck(request, authHeader, sfdcRequest);

				}
			} else {
				LOGGER.info("Retrying with 4 secs for create entity {}", request);
				createEnityContactRetryCheck(request, authHeader, sfdcRequest);
			}

		} catch (Exception e) {
			LOGGER.warn("Error in process Partner Entity engine ", e);
		}

	}


	private WaiverResponseBean constructWaiverResponseAndSendToQueue(SfdcWaiverResponseBean sfdcWaiverResponseBean, Integer tpsId) {
		WaiverResponseBean responseBean = new WaiverResponseBean();
		if (sfdcWaiverResponseBean != null) {
			responseBean.setTpsId(tpsId);
			responseBean.setMessage(sfdcWaiverResponseBean.getMessage());
			responseBean.setStatus(sfdcWaiverResponseBean.getStatus());
			responseBean.setErrorCode(sfdcWaiverResponseBean.getErrorcode());
			List<EtcRecordListBean> etcList = constructEtcForWaiver(sfdcWaiverResponseBean.getEtcRecord());
			responseBean.setEtcRecordList(etcList);

		}

		return responseBean;
	}


	private List<EtcRecordListBean> constructEtcForWaiver(EtcRecordList etcRecord) {
		List<EtcRecordListBean> etcRecordListBeanList = new ArrayList<>();
		if (etcRecord != null) {
				EtcRecordListBean etcRecordBean = new EtcRecordListBean();
				AttributesResponseBean attributesResponseBean = new AttributesResponseBean();
				attributesResponseBean.setType(etcRecord.getAttributes().getType());
				attributesResponseBean.setUrl(etcRecord.getAttributes().getUrl());
				etcRecordBean.setAttributes(attributesResponseBean);
				etcRecordBean.setId(etcRecord.getId());
				etcRecordBean.setName(etcRecord.getName());
				etcRecordBean.setChildOptyId(etcRecord.getOpportunityNameC());
				etcRecordListBeanList.add(etcRecordBean);

		}
		return etcRecordListBeanList;

	}
	
	private PartnerEntityContactResponseBean constructPartnerEntityContactResponse(
			SfdcPartnerEntityContactResponse sfdcPartnerEntityResponse) {
		PartnerEntityContactResponseBean partnerEntityContactResponseBean = new PartnerEntityContactResponseBean();
		partnerEntityContactResponseBean.setMessage(sfdcPartnerEntityResponse.getMessage());
		partnerEntityContactResponseBean.setStatus(sfdcPartnerEntityResponse.getStatus());
		partnerEntityContactResponseBean.setCustomerContactId(sfdcPartnerEntityResponse.getCustomerContactId());
		return partnerEntityContactResponseBean;
	}

	/**
	 * sfdcSiteRetryCheck


	/**
	 * createEnityRetryCheck
	 *
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcWaiverRetryCheck(String request, Map<String, String> authHeader, String input)
			throws InterruptedException, TclCommonException, IOException {
		RestResponse response;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + waiverUrl;
		response = restClientService.post(url, request, null, null, authHeader);
		LOGGER.info("waiver sfdc response {}", response);
		TerminationWaiverBean terminationWaiverBean = (TerminationWaiverBean) Utils.convertJsonToObject(input,
				TerminationWaiverBean.class);

		if (response.getStatus() == Status.SUCCESS) {
			/*List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = (List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean>) Utils
					.convertJsonToObject(response.getData(), List.class);*/
			ObjectMapper objectMapper = new ObjectMapper();
			List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = objectMapper.readValue(response.getData(),
					new TypeReference<List<SfdcWaiverResponseBean>>() {});
			if (waiverResponse.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				WaiverResponseBean waiverResponseBean = constructWaiverResponseAndSendToQueue(waiverResponse.get(0), terminationWaiverBean.getTpsId());
				LOGGER.info("waiver  response to queue {}", waiverResponseBean);
				LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(waiverResponseQueue, Utils.convertObjectToJson(waiverResponseBean));
			} else {
				notificationService.notifySfdcError(response.toString(), request, url, authHeader.toString());
				WaiverResponseBean waiverResponseBean = new WaiverResponseBean();
				waiverResponseBean.setError(true);
				waiverResponseBean.setErrorMessage(response.getData());
				waiverResponseBean.setTpsId(terminationWaiverBean.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(waiverResponseQueue, Utils.convertObjectToJson(waiverResponseBean));
			}
		} else {
			notificationService.notifySfdcError(response.toString(), request, url, authHeader.toString());
			WaiverResponseBean waiverResponseBean = new WaiverResponseBean();
			waiverResponseBean.setError(true);
			waiverResponseBean.setErrorMessage(response.getData());
			waiverResponseBean.setTpsId(terminationWaiverBean.getTpsId());
			LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(waiverResponseQueue, Utils.convertObjectToJson(waiverResponseBean));
		}
	}


	/**
	 *
	 * @param input
	 * @throws TclCommonException
	 */
	public void processSdfcUpdateWaiver(String input) throws TclCommonException {

		try {

			ISfdcMapper mapper = factory.getInstanceMapper(MapperConstants.WAIVER_UPDATE_MAPPER.toString());
			SfdcUpdateWaiverRequest respone = (SfdcUpdateWaiverRequest) mapper.transfortToSfdcRequest(input);
			String request = Utils.convertObjectToJson(respone);
			SfdcAccessToken accessToken = getAuthToken();
			LOGGER.info("createWaiver request" + request);
			String authenticationAccessToken = SfdcConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(SfdcConstants.AUTHORIZATION.toString(), authenticationAccessToken);
			RestResponse response = restClientService.put(endPointUrl + waiverUrl, request,authHeader);
			LOGGER.info("site sfdc respone {}", response);

			TerminationWaiverBean terminationWaiverBean = (TerminationWaiverBean) Utils.convertJsonToObject(input,
					TerminationWaiverBean.class);
			if (response.getStatus() == Status.SUCCESS) {
			/*	List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = (List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean>) Utils
						.convertJsonToObject(response.getData(),
								List.class);*/
				ObjectMapper objectMapper = new ObjectMapper();
				List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = objectMapper.readValue(response.getData(),
						new TypeReference<List<SfdcWaiverResponseBean>>() {});
				if (waiverResponse.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					WaiverResponseBean waiverResponseBean = constructWaiverResponseAndSendToQueue(waiverResponse.get(0), terminationWaiverBean.getTpsId());
					LOGGER.info("waiver response to queue {}", waiverResponseBean);
					LOGGER.info("MDC Filter token value in before Queue call processSdfcWaiver {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mQUtils.send(waiverResponseUpdateQueue, Utils.convertObjectToJson(waiverResponseBean));

				} else {
					LOGGER.info("Retrying with 3 secs {}", request);
					sfdcWaiverUpdateRetryCheck(request, authHeader, input);
				}
			} else {
				notificationService.notifySfdcError(response.toString(), request, endPointUrl + waiverUrl,
						authHeader.toString());
				WaiverResponseBean waiverResponseBean = new WaiverResponseBean();
				waiverResponseBean.setError(true);
				waiverResponseBean.setErrorMessage(response.getData());
				waiverResponseBean.setTpsId(terminationWaiverBean.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(waiverResponseUpdateQueue, Utils.convertObjectToJson(waiverResponseBean));
			}

		} catch (Exception e) {
			LOGGER.error("Error in process Sdfc site", e);
		}

	}

	/**
	 * sfdcWaiverUpdateRetryCheck
	 *
	 * @param request
	 * @param authHeader
	 * @throws InterruptedException
	 * @throws TclCommonException
	 */
	private void sfdcWaiverUpdateRetryCheck(String request, Map<String, String> authHeader, String input)
			throws InterruptedException, TclCommonException, IOException {
		RestResponse response;
		TimeUnit.SECONDS.sleep(4);
		String url = endPointUrl + waiverUrl;
		response = restClientService.put(url, request, authHeader);
		LOGGER.info("waiver sfdc response {}", response);
		TerminationWaiverBean terminationWaiverBean= (TerminationWaiverBean) Utils.convertJsonToObject(input,
				TerminationWaiverBean.class);

		if (response.getStatus() == Status.SUCCESS) {
		/*	List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = (List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean>) Utils
					.convertJsonToObject(response.getData(), List.class);*/
			ObjectMapper objectMapper = new ObjectMapper();
			List<com.tcl.dias.sfdc.response.bean.SfdcWaiverResponseBean> waiverResponse = objectMapper.readValue(response.getData(),
					new TypeReference<List<SfdcWaiverResponseBean>>() {});
			if (waiverResponse.get(0).getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				WaiverResponseBean waiverResponseBean = constructWaiverResponseAndSendToQueue(waiverResponse.get(0), terminationWaiverBean.getTpsId());
				LOGGER.info("waiver  response to queue {}", waiverResponseBean);
				LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverUpdateRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(waiverResponseUpdateQueue, Utils.convertObjectToJson(waiverResponseBean));
			} else {
				notificationService.notifySfdcError(response.toString(), request, url, authHeader.toString());
				WaiverResponseBean waiverResponseBean = new WaiverResponseBean();
				waiverResponseBean.setError(true);
				waiverResponseBean.setErrorMessage(response.getData());
				waiverResponseBean.setTpsId(terminationWaiverBean.getTpsId());
				LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverUpdateRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(waiverResponseUpdateQueue, Utils.convertObjectToJson(waiverResponseBean));
			}
		} else {
			notificationService.notifySfdcError(response.toString(), request, url, authHeader.toString());
			WaiverResponseBean waiverResponseBean = new WaiverResponseBean();
			waiverResponseBean.setError(true);
			waiverResponseBean.setErrorMessage(response.getData());
			waiverResponseBean.setTpsId(terminationWaiverBean.getTpsId());
			LOGGER.info("MDC Filter token value in before Queue call sfdcWaiverUpdateRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(waiverResponseUpdateQueue, Utils.convertObjectToJson(waiverResponseBean));
		}
	}
	
	private void createEnityContactRetryCheck(String request, Map<String, String> authHeader,
									   SfdcPartnerEntityContactRequest sfdcRequest) throws InterruptedException, TclCommonException {
		RestResponse sfdcReponse;
		TimeUnit.SECONDS.sleep(4);
		sfdcReponse = restClientService.post(endPointUrl + createPartnerEntityContactUrl, request, null, null, authHeader);
		LOGGER.info("create entity sfdc {}", sfdcReponse);

		if (sfdcReponse.getStatus() == Status.SUCCESS) {
			SfdcPartnerEntityContactResponse createEntityResponse = (SfdcPartnerEntityContactResponse) Utils
					.convertJsonToObject(sfdcReponse.getData(), SfdcPartnerEntityContactResponse.class);
			if (createEntityResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
				PartnerEntityContactResponseBean opBean = constructPartnerEntityContactResponse(createEntityResponse);
				opBean.setReferenceId(sfdcRequest.getReferenceId());
				LOGGER.info("create entity response to queue {}", opBean);
				LOGGER.info("MDC Filter token value in before Queue call createEntityContactRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(partnerEntityContactCreateResponseUpdate, Utils.convertObjectToJson(opBean));
			} else {
				LOGGER.info("create entity error {} ", sfdcReponse);
				notificationService.notifySfdcError(sfdcReponse.toString(), request, endPointUrl + createPartnerEntityContactUrl,
						authHeader.toString());
				PartnerEntityResponseBean opBean = new PartnerEntityResponseBean();
				opBean.setStatus(CommonConstants.FAILIURE);
				opBean.setMessage(sfdcReponse.getData());
				opBean.setReferenceId(sfdcRequest.getReferenceId());
				LOGGER.info("MDC Filter token value in before Queue call createEntityContactRetryCheck {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mQUtils.send(partnerEntityContactCreateResponseUpdate, Utils.convertObjectToJson(opBean));
			}

		} else {
			notificationService.notifySfdcError(sfdcReponse.toString(), request, endPointUrl + createPartnerEntityContactUrl,
					authHeader.toString());
			PartnerEntityContactResponseBean opBean = new PartnerEntityContactResponseBean();
			opBean.setStatus(CommonConstants.FAILIURE);
			opBean.setMessage(sfdcReponse.getData());
			opBean.setReferenceId(sfdcRequest.getReferenceId());
			LOGGER.info("MDC Filter token value in before Queue call createEntityContactRetryCheck {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mQUtils.send(partnerEntityContactCreateResponseUpdate, Utils.convertObjectToJson(opBean));
		}
	}

}
