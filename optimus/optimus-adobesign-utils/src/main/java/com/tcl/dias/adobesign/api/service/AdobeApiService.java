package com.tcl.dias.adobesign.api.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.adobesign.api.beans.AgreementInfo;
import com.tcl.dias.adobesign.authentication.service.AdobeAuthenticationService;
import com.tcl.dias.adobesign.constants.AdobeConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * AdobeApiService- class provides interfaces for all the api for adobe sign
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class AdobeApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdobeApiService.class);

	@Value("${adobesign.api.base.path}")
	String apiBasePath;

	@Value("${adobesign.api.transient.path}")
	String apiTransientPath;

	@Value("${adobesign.api.base.url}")
	String baseUrl;

	@Value("${adobesign.api.agreements.path}")
	String apiAgreementPath;

	@Autowired
	RestClientService restClientService;

	@Autowired
	AdobeAuthenticationService adobeAuthenticationService;

	/**
	 * 
	 * processTransientDocument - This method uploads the document to adobe and
	 * returns the transientDocumentId
	 * 
	 * @return String
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public String processTransientDocument(byte[] file, String fileName) throws TclCommonException {
		try {
			String transientDocumentUrl = (new StringBuilder(baseUrl)).append(apiBasePath).append(apiTransientPath)
					.toString();
			String accessToken = adobeAuthenticationService.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(AdobeConstants.AUTHORIZATION, accessToken);
			RestResponse response = restClientService.postStreamWithProxy(transientDocumentUrl, file, fileName,
					authHeader);
			if (response.getStatus() == Status.SUCCESS) {
				HashMap<String, String> responseMapper = Utils.convertJsonToObject(response.getData(), HashMap.class);
				LOGGER.info("Transient token  response received {}", responseMapper);
				return responseMapper.get(AdobeConstants.TRANSIENT_DOCUMENT_ID);
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing TransientDocument ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * processAgreement - Once the transientDocumentId is fetched , the same need to
	 * be passed along with the sender information
	 * 
	 * @param agreementInfo
	 * @return - unique id
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public String processAgreement(AgreementInfo agreementInfo) throws TclCommonException {
		try {
			String agreementUrl = (new StringBuilder(baseUrl)).append(apiBasePath).append(apiAgreementPath).toString();
			String accessToken = adobeAuthenticationService.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(AdobeConstants.AUTHORIZATION, accessToken);
			ObjectMapper objectMapper = new ObjectMapper();
			String requestBody = objectMapper.writeValueAsString(agreementInfo);
			HttpHeaders headers = new HttpHeaders();
			headers.set(AdobeConstants.AUTHORIZATION, accessToken);
			headers.setContentType(MediaType.APPLICATION_JSON);
			RestResponse response = restClientService.postWithProxy(agreementUrl, requestBody, headers);
			if (response.getStatus() == Status.SUCCESS) {
				HashMap<String, String> responseMapper = Utils.convertJsonToObject(response.getData(), HashMap.class);
				LOGGER.info("agreement  response received {}", responseMapper);
				return responseMapper.get(AdobeConstants.ID);
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing Agreement ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

}
