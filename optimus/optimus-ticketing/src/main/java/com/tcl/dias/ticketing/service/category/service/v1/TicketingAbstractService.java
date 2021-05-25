package com.tcl.dias.ticketing.service.category.service.v1;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.ticketing.response.ErrorResponseDetails;
import com.tcl.dias.ticketing.service.v1.TicketingService;

/**
 * This file contains the ServiceCategoryAbstract.java class. used to have the
 * restricted details
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public abstract class TicketingAbstractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TicketingAbstractService.class);
	
	@Autowired
	private UserInfoUtils userInfoUtils;



	@Value("${app.id}")
	String appId;

	@Value("${secret.key}")
	String appSecret;

	/**
	 * getCategoriesParam used to get the categories param
	 * 
	 * @param impact
	 * @param serviceId
	 * @param productType
	 * @return
	 */
	protected Map<String, String> getCategoriesParam(String impact, String serviceId, String productType) {
		Map<String, String> params = new HashMap<>();
		params.put("impact", impact);
		params.put("serviceId", serviceId);
		params.put("productType", productType);

		return params;
	}

	/**
	 * getAuthorizatinHeader used to have the header details
	 * 
	 * @return
	 */
	protected HashMap<String, String> getHeader() {

		HashMap<String, String> authorizationHeader = new HashMap<>();
		authorizationHeader.put("Accept", "application/json");
		authorizationHeader.put("Content-Type", "application/json");

		return authorizationHeader;
	}

	/**
	 * @author vivek used to get the basic auth with encoded form getBasicAuth
	 * @param appId
	 * @param appSecret
	 * @return
	 * 
	 * 
	 */

	/* need to pass login user id */
	protected HttpHeaders getBasicAuth(String appId, String appSecret, Map<String, String> contentTypes,
			String YAuthUser) {

		HttpHeaders headers = new HttpHeaders();
		if (appId != null && appSecret != null) {
			String auth = appId + ":" + appSecret;
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.set("Authorization", authHeader);
		}
		if (YAuthUser != null) {
			headers.set("YAuthorization", YAuthUser);

		} else {
			headers.set("YAuthorization", userInfoUtils.getUserInformation().getUserId());
		}
		if (contentTypes != null && !contentTypes.isEmpty()) {

			contentTypes.forEach((key, value) -> {
				headers.set(key, value);

			});
		}
		return headers;

	}

	/**
	 * @author vivek used to handle the error response createErrorResponse
	 * @param response
	 * @return
	 */
	protected ErrorResponseDetails createErrorResponse(RestResponse response) {

		ErrorResponseDetails details = new ErrorResponseDetails();

		if (response.getData() != null) {

			details.setStatus(response.getStatus().toString());
			if (response.getData() != null) {
				JSONParser parser = new JSONParser();
				JSONObject json;
				try {
					json = (JSONObject) parser.parse(response.getData());

					if (json.containsKey("message")) {
						details.setMessage((String) json.get("message"));
					} else if (json.containsKey("errorMessage")) {
						details.setMessage((String) json.get("errorMessage"));
					} else {
						LOGGER.error("error in getting the message from the end poind{}", response.getData());
					}
				} catch (ParseException e) {

				}

			}

		} else if (response.getStatus() == Status.ERROR) {
			details.setStatus(response.getStatus().toString());
			details.setMessage(response.getErrorMessage());
		}

		return details;
	}

	/**
	 * 
	 * used to get the attachement url
	 * 
	 * @param baseUrl
	 * @param ticketId
	 * @return
	 */
	protected String getAttachmentUrl(String baseUrl, String ticketId) {
		return baseUrl + "/" + ticketId;
	}

	/**
	 *  used to the attachment url
	 * 
	 * @param baseUrl
	 * @param ticketId
	 * @param attachmentId
	 * @return
	 */
	protected String getAttachmentUrl(String baseUrl, String ticketId, String attachmentId) {
		return baseUrl + "/" + ticketId + "/" + "attachments";
	}

	/**
	 *  used to the attachment url
	 * 
	 * @param baseUrl
	 * @param ticketId
	 * @param attachmentId
	 * @return
	 */
	protected String getAttachmentDetailsUrl(String baseUrl, String ticketId, String attachmentId) {
		return baseUrl + "/" + ticketId + "/" + "attachments" + "/" + attachmentId;
	}

	/**
	 * used to get the categories param
	 * 
	 * @param impact
	 * @param serviceId
	 * @param productType
	 * @return
	 */
	protected Map<String, String> getServiceRequestParam(List<String> filters, String limit, String offset,
			String sortBy, String sortOrder, String requestType) {
		Map<String, String> params = new HashMap<>();
		if (!Objects.isNull(filters) && !filters.isEmpty()) {
			filters.forEach(value -> {
				
				if(params.containsKey("filters")) {
					params.put("filters", params.get("filters")+","+value);
				}
				else {
				params.put("filters", value);
				}

			});

		}
		if (!Objects.isNull(limit))
			params.put("limit", limit);
		if (!Objects.isNull(offset))
			params.put("offset", offset);
		if (!Objects.isNull(sortBy))
			params.put("sortBy", sortBy);
		if (!Objects.isNull(sortOrder))
			params.put("sortOrder", sortOrder);
		if (!Objects.isNull(requestType))
			params.put("requestType", requestType);

		return params;
	}
	
	/**
	 * used to get the categories param
	 * 
	 * @param impact
	 * @param serviceId
	 * @param productType
	 * @return
	 */
	protected Map<String, String> getServiceRequestParam(String filters, String limit, String offset, String sortBy,
			String sortOrder, String requestType) {
		Map<String, String> params = new HashMap<>();
		if (!Objects.isNull(filters)) {
			params.put("filters", filters);

		}
		if (!Objects.isNull(limit))
			params.put("limit", limit);
		if (!Objects.isNull(offset))
			params.put("offset", offset);
		if (!Objects.isNull(sortBy))
			params.put("sortBy", sortBy);
		if (!Objects.isNull(sortOrder))
			params.put("sortOrder", sortOrder);
		if (!Objects.isNull(requestType))
			params.put("requestType", requestType);

		return params;
	}
}
