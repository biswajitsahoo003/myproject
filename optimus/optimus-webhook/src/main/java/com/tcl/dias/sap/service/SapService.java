package com.tcl.dias.sap.service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcl.dias.sap.beans.GrnResponses;
import com.tcl.dias.sap.beans.GrnSapHanaResponse;
import com.tcl.dias.sap.beans.MinResponseBean;
import com.tcl.dias.sap.beans.PoStatusResponseBean;

@Service
public class SapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SapService.class);

    @Value("${mq.sap.minstatus.queue}")
    String sapMinStatusQueue;

    @Value("${mq.sap.postatus.queue}")
    String poStatusQueue;

    @Value("${mq.sap.grnstatus.queue}")
    String grnStatusQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    

    /**
     * sends the Min response details.
     *
     * @param minResponseBean
     */
    public void sendMinStatus(MinResponseBean minResponseBean) {
        LOGGER.info(String.format("MinResponse received : %s", minResponseBean.toString()));
        try {
            Gson gson = new Gson();
            String response = gson.toJson(minResponseBean);
            rabbitTemplate.convertAndSend(sapMinStatusQueue, response);
        } catch (AmqpException e) {
            LOGGER.error("Exception occurred wile pushing minResponse in Sap Service : {}", e);
        }

    }

	public void sendPoStatus(PoStatusResponseBean poStatusBean) {
		LOGGER.info(String.format("PoStatusResponseBean received : %s", poStatusBean.toString()));
		try {
			Gson gson = new Gson();
			String response = gson.toJson(poStatusBean);
			rabbitTemplate.convertAndSend(poStatusQueue, response);
			
		} catch (AmqpException e) {
			LOGGER.error("Exception occurred wile pushing poStatus in Sap Service : {}", e);
		}
	}

    public void sendGrnStatus(GrnSapHanaResponse grnResponses) {
        LOGGER.info(String.format("GrnResponseBean received : %s", grnResponses.toString()));
        try {
            Gson gson = new Gson();
            String response = gson.toJson(grnResponses);
            LOGGER.info("Grn reponse from SAP system  : {}",response);
            rabbitTemplate.convertAndSend(grnStatusQueue, response);
        } catch (AmqpException e) {
            LOGGER.error("Exception occurred wile pushing grnResponse in Sap Service : {}", e);
        }
    }
    
    public void postWithBasicAuthentication(String url, PoStatusResponseBean requestBody, Map<String, String> header,
			String username, String password) {
		LOGGER.trace("RestClientService.postWithBasicAuthentication method invoked with parameter {}.", url);
		try {

			HttpHeaders headers = null;
			if (username != null) {
				headers = getAuthenticationHeader(username, password);
			} else {
				headers = new HttpHeaders();
			}
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}
			
			LOGGER.info("postWithBasicAuthentication call url :{},header:{},request:{}", url, header, convertObjectToJson(requestBody));
			
			HttpEntity<String> request = new HttpEntity<>(convertObjectToJson(requestBody), headers);
			ResponseEntity<String> response = (new RestTemplate()).postForEntity(url, request, String.class);
			LOGGER.info("Output Received : {}", response);
			

		} catch (HttpClientErrorException ex) {
			LOGGER.error("Response for post call  HttpClientErrorException {}", ex.getResponseBodyAsString());
		} catch (Exception e) {
			LOGGER.error("Response for post call  Exception", e);
		}

		LOGGER.trace("RestClientService.postWithBasicAuthentication method ends.");

	}
    
    private HttpHeaders getAuthenticationHeader(String username, String password) {
		LOGGER.info("RestClientService.getAuthenticationHeader method invoked");
		HttpHeaders httpHeaders = new HttpHeaders();
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		httpHeaders.set("Authorization", authHeader);
		LOGGER.info("RestClientService.getAuthenticationHeader method ends.");
		return httpHeaders;
	}
    
    private Map<String, String> createCommonHeader() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		return headers;
	}
    
    public static final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
			.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    public static <T> String convertObjectToJson(T object) {
		String json = null;
		try {
			json = objectMapper.writer().writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}