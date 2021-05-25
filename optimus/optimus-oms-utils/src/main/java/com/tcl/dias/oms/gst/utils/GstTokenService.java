package com.tcl.dias.oms.gst.utils;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.GstAuthTokenRequestBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class GstTokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GstTokenService.class);

	@Autowired
	RestClientService restClientService;

	@Autowired
	ApplicationContext appCtx;

	@Value("${oms.gst.token.url}")
	String gstUrl;

	@Value("${oms.gst.auth.token.clientid}")
	String clientId;

	@Value("${oms.gst.auth.token.username}")
	String username;

	@Value("${oms.gst.auth.token.password}")
	String password;

	@Value("${oms.gst.auth.token.grant.type}")
	String grantType;

	@Value("${oms.gst.auth.token.client.secret}")
	String clientSecret;

	public String getGstToken() throws TclCommonException {
		LOGGER.info("Inside GstTokenService.getGstToken method");
		String token = null;
		try {
			GstAuthTokenRequestBean gstAuthTokenRequestBean = new GstAuthTokenRequestBean();
			gstAuthTokenRequestBean.setClient_id(clientId);
			gstAuthTokenRequestBean.setClient_secret(clientSecret);
			gstAuthTokenRequestBean.setGrant_type(grantType);
			gstAuthTokenRequestBean.setPassword(password);
			gstAuthTokenRequestBean.setUsername(username);
			String requestBody = Utils.convertObjectToJson(gstAuthTokenRequestBean);
			HttpHeaders headers = new HttpHeaders();
			headers.set("content-type", "application/json");
			RestResponse response = restClientService.postWithProxy(gstUrl, requestBody, headers);
			LOGGER.info("Inside GstAuthUtil.getGstToken method response  {} ", response);
			if (response != null && response.getData() != null) {
				LOGGER.info("Inside GstAuthUtil.getGstToken method response data {} ", response.getData());
				JSONObject jsonObj = Utils.convertJsonStingToJson(response.getData());
				if (jsonObj.get("access_token") != null) {
					LOGGER.info("Inside GstAuthUtil.getGstTokengetting access token {} ", jsonObj.get("access_token"));
					token = (String) jsonObj.get("access_token");
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in getting auth token", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return token;
	}


}
