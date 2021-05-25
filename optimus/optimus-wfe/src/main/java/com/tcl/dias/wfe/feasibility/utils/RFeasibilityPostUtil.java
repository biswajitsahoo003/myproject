package com.tcl.dias.wfe.feasibility.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
/**
 * RestClientService Class is used for all the rest call internally
 * 
 *
 * @author Paulraj Sundar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class RFeasibilityPostUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(RFeasibilityPostUtil.class);

	@Autowired
	RestClientService restClientService;
/**
 * post * used for http post call
 * @param request
 * @param requestUrl
 * @return
 * @throws Exception
 */
	public List<Map<String, Object>> postURL(String request, String requestUrl) throws Exception {

		List<Map<String, Object>> rArray = null;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		RestResponse feasibilityResponse = restClientService.post(requestUrl, request, headers);
		LOGGER.info("Output response from RFeasibilityPostUtil::::{} ", feasibilityResponse);
		if (feasibilityResponse.getStatus() == Status.SUCCESS) {

			String result = feasibilityResponse.getData();
			if (result.contains("NaN")) {
				result = result.replaceAll("NaN", "null");
			}
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(result);
			JSONArray arr = (JSONArray) jsonObject.get("results");
			rArray = (List<Map<String, Object>>) arr;
			for (Map<String, Object> map : rArray) {
				Double errorMessage = 0.0;
				if (map.get("error_flag") instanceof Double) {
					errorMessage = (Double) map.get("error_flag");
					LOGGER.info("Double instance:::: ");
				} else if (map.get("error_flag") instanceof Integer) {
					errorMessage = new Double((Integer) map.get("error_flag"));
					LOGGER.info("Integer instance:::: ");
				}
				if (errorMessage.equals(1)) { // If any error from R model response then will send the error message to
					LOGGER.info("R Model Exception flag 1::::"); // response queue
					throw new Exception();
				} 
			}
		}else if(feasibilityResponse.getStatus() == Status.ERROR){
			LOGGER.info("ERROR message::::"); // response queue
			throw new Exception();
		}
		return rArray;
	}
	/**
	 * Get * used for http post call
	 * @param url
	 * @return
	 */
	public RestResponse get(String url) {
		// LOGGER.trace("RestClientService.get method invoked with parameter {}.", url);
		RestTemplate restTemplate = new RestTemplate();
		RestResponse restResponse = new RestResponse();
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.get method ends.");
		return restResponse;

	}
	/**
	 * post * used for http post call to the CQ links
	 * @param url
	 * @param requestBody
	 * @param tkt_serviceTkt
	 * @return
	 */
	public RestResponse postCqUrl(String url, String requestBody, String tkt_serviceTkt) {
		// LOGGER.trace("RestClientService.post method invoked with parameter {}.",
		// url);
		RestResponse restResponse = new RestResponse();
		RestTemplate restTemplate = new RestTemplate();
		try {
			LOGGER.debug("Calling url : {} , with response body {}", url, requestBody);
			HttpHeaders headers = null;
			headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.TEXT_PLAIN_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			LOGGER.debug("Output Received : {}", response);
			if (tkt_serviceTkt.equals("Ticket")) {
				List<String> strValue = response.getHeaders().get("Location");
				restResponse.setData(strValue.toString());
			} else {
				restResponse.setData(response.getBody());
			}
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}
}
