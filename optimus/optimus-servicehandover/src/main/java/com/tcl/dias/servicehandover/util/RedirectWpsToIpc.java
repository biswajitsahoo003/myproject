package com.tcl.dias.servicehandover.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import com.tcl.common.keycloack.bean.keycloakAuthBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.keycloack.constants.KeycloakConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicehandover.wps.beans.InvoiceOperationInput;
import com.tcl.dias.servicehandover.wps.beans.OptimusAccoutInputBO; 
import com.tcl.dias.servicehandover.wps.beans.OptimusProductInputBO;

@Service
public class RedirectWpsToIpc {
	
	@Autowired
	RestClientService restClientService;
	
	private static final Logger logger = LoggerFactory.getLogger(RedirectWpsToIpc.class);
 	
	public String getKeyCloakAuth(String server) throws Exception {
		logger.info("inside getKeyCloakAuth :{}");
		LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
		formBody.add(KeycloakConstants.CLIENT_ID.toString(), "optimus");
		formBody.add(KeycloakConstants.GRANT_TYPE.toString(), "password");
		formBody.add(KeycloakConstants.PASSWORD.toString(), "welcome123");
		formBody.add(KeycloakConstants.USERNAME.toString(), "optimus_shriram");
		RestResponse response = restClientService
				.postWithoutHeader("http://"+server+"/auth/realms/master/protocol/openid-connect/token", formBody);
		keycloakAuthBean keycloakAuthBean =(keycloakAuthBean) Utils.convertJsonToObject(response.getData(), keycloakAuthBean.class);
		logger.info("getKeyCloakAuth token {} ", keycloakAuthBean.getAccessToken());
		return keycloakAuthBean.getAccessToken();
	}
	
	public void asyncAccount( String hostAddress, OptimusAccoutInputBO accountInfo) throws Exception {
		logger.info("inside asyncAccount for IPC");
		String request = Utils.convertObjectToJson(accountInfo);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Bearer "+getKeyCloakAuth(hostAddress));
		RestResponse response = restClientService
				.post("http://"+hostAddress+"/optimus-servicehandover/api/v1/wps-optimus/sync/account", request,headers);
		logger.info("inside asyncAccount for Completed {} {}" , response , response.getStatus());
		
		
	}
	
	public void asyncOrder( String hostAddress, OptimusProductInputBO orderInfo) throws Exception {
		logger.info("inside asyncAccount for IPC");
		String request = Utils.convertObjectToJson(orderInfo);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Bearer "+getKeyCloakAuth(hostAddress));
		RestResponse response = restClientService
				.post("http://"+hostAddress+"/optimus-servicehandover/api/v1/wps-optimus/sync/order", request,headers);
		logger.info("inside asyncOrder for Completed {} {}" , response , response.getStatus());
	}
	
	private void asyncInvoice(InvoiceOperationInput invoiceInfo) throws Exception {
		String request = Utils.convertObjectToJson(invoiceInfo);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Bearer "+getKeyCloakAuth(""));
		RestResponse response = restClientService
				.post("http://10.133.208.188/optimus-servicehandover/api/v1/wps-optimus/sync/invoice", request,headers);
		logger.info("inside asyncInvoice for Completed {} {}" , response , response.getStatus());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String isOrderAvailable(String hostAddress, String orderCode, String sourceProdSeq) throws Exception {
		try {
			logger.info("{} - {}, Availability check for IPC in: {}", orderCode, sourceProdSeq, hostAddress);
			OptimusProductInputBO orderInfo = new OptimusProductInputBO();
			String request = Utils.convertObjectToJson(orderInfo);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.add("Authorization", "Bearer "+getKeyCloakAuth(hostAddress));
			RestResponse response = restClientService
					.post("http://"+hostAddress+"/optimus-servicehandover/api/v1/ipc-commercial-vetting/isOrderPresent/orders/"+orderCode+"/sourceProdSeq/"+sourceProdSeq, request, headers);
			ResponseResource parsedResp = (ResponseResource) Utils.convertJsonToObject(response.getData(), ResponseResource.class);
			logger.info("parsedResp {}" , parsedResp);
			String result = ((Map<String,Object>) parsedResp.getData()).get("orderSyncResponse").toString();
			logger.info("Availability check for IPC Completed {} {}" , response , result);
			return result;
		} catch (Exception e) {
			logger.error("Error Occured {}" , e.getMessage());
			return "FAILURE";
		}
	}

}
