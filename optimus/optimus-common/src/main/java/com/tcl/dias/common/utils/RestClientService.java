package com.tcl.dias.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.ThirdPartyServiceAuditBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

//import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * RestClientService Class is used for all the rest call internally
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class RestClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientService.class);

	@Autowired
	ApplicationContext appCtx;

	@Value("${system.proxy.host}")
	String proxyHost;

	@Value("${system.proxy.port}")
	Integer proxyPort;

	@Autowired
	MQUtils mqUtils;

	public void setProxy() {
		System.setProperty("http.proxyHost", proxyHost);
		System.setProperty("http.proxyPort", proxyPort + "");
	}

	/**
	 * get used for http get call
	 * 
	 * @param url
	 * @return
	 */
	public RestResponse get(String url) {
		LOGGER.trace("RestClientService.get method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			ResponseEntity<String> response = getAutoProxyRestTemplate(url).getForEntity(url, String.class);
			processAudit(url, "GET", null, null, response.getBody(), String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.get method ends.");
		return restResponse;

	}

	/**
	 * 
	 * getStream
	 * 
	 * @param url
	 * @return
	 */
	public ByteArrayResource getStream(String url) {
		LOGGER.trace("RestClientService.getStream method invoked with parameter {}.", url);
		ByteArrayResource streamResponse = null;
		try {
			Utils.disableSslVerification();
			ResponseExtractor<ByteArrayResource> responseExtractor = response -> {
				return new ByteArrayResource(IOUtils.toByteArray(response.getBody()));
			};
			// Optional Accept header
			RequestCallback requestCallback = request -> request.getHeaders()
					.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
			streamResponse = getAutoProxyRestTemplate(url).execute(url, HttpMethod.GET, requestCallback,
					responseExtractor);
		} catch (Exception e) {
			LOGGER.error("Error in Stream", e);
		}

		LOGGER.trace("RestClientService.get method ends.");
		return streamResponse;

	}

	/**
	 * 
	 * putStream
	 * 
	 * @param url
	 * @param requestBody
	 * @param data
	 * @return
	 */
	public RestResponse putStream(String url, byte[] data, String fileName) {
		LOGGER.info("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();
			MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
			parts.add("file", new ByteArrayResource(data));
			parts.add("filename", fileName);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
					parts, headers);
			RestTemplate restTemplate = getAutoProxyRestTemplate(url);
			LOGGER.info("restTemplate after getAutoProxyRestTemplate method");
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
			LOGGER.info("restTemplate after restTemplate.exchange");
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			LOGGER.info("Error in put Stream {}", ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.putStream method ends.");
		return restResponse;

	}
	
	public RestResponse postStreamWithProxy(String url, byte[] data,String fileName, Map<String, String> header) {
		LOGGER.info("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();
			MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		    ByteArrayResource contentsAsResource = new ByteArrayResource(data) {
		        @Override
		        public String getFilename() {
		            return fileName; // Filename has to be returned in order to be able to post.
		        }
		    };
			parts.add("File", contentsAsResource);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
					parts, headers);
			RestTemplate restTemplate = getProxyRestTemplate(url);
			LOGGER.info("restTemplate after getAutoProxyRestTemplate method");
			ResponseEntity<String> response = restTemplate.postForEntity(url,requestEntity, String.class);
			LOGGER.info("restTemplate after restTemplate.exchange");
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			LOGGER.info("Error in put Stream {}", ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.putStream method ends.");
		return restResponse;

	}

	public RestResponse get(String url, Map<String, String> headers, Boolean isAudit) {
		LOGGER.trace("RestClientService.get method invoked with parameter {} and headers {}.", url, headers);
		RestResponse restResponse = new RestResponse();
		RestTemplate restTemplate = new RestTemplate();
		try {
			Utils.disableSslVerification();
			HttpHeaders httpHeaders = new HttpHeaders();
			headers.forEach((key, value) -> httpHeaders.set(key, value));
			HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
			if (isAudit == null || isAudit)
				processAudit(url, "GET", null, null, response.getBody(), String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}
		LOGGER.trace("RestClientService.get with headers method ends.");
		return restResponse;
	}

	public RestResponse getWithProxy(String url, Map<String, String> headers, Boolean isAudit) {
		LOGGER.trace("RestClientService.getWithProxy method invoked with parameter {} and headers {}.", url, headers);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();
			HttpHeaders httpHeaders = new HttpHeaders();
			headers.forEach((key, value) -> httpHeaders.set(key, value));
			HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
			LOGGER.info("getWithProxy executed for url and headers {}  {}",url,httpEntity);
			ResponseEntity<String> response = getAutoProxyRestTemplate(url).exchange(url, HttpMethod.GET,
					httpEntity, String.class);
			LOGGER.info("getWithProxy executed for url and response {}  {}",url,response);
			if (isAudit == null || isAudit)
				processAudit(url, "GET", null, null, response.getBody(), String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Exception occured in getWithProxy  response {} and error  {} ",  restResponse, e);
		}
        LOGGER.trace("RestClientService.getWithProxy with headers method ends.");		
		return restResponse;
	}

	public RestResponse getImage(String url) {
		LOGGER.trace("RestClientService.getImage method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			List<HttpMessageConverter<?>> a = new ArrayList<>();
			a.add(new ResourceHttpMessageConverter());
			RestTemplate restTemplate = getProxyRestTemplate(url);
			restTemplate.setMessageConverters(a);
			ResponseEntity<Resource> response = restTemplate.getForEntity(url, Resource.class);
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
				restResponse.setInputStream(response.getBody().getInputStream());
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.getImage method ends.");
		return restResponse;

	}

	/**
	 * post * used for http post call
	 * 
	 * @param url
	 * @return
	 */

	public RestResponse post(String url, String requestBody, String username, String password,
			Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			HttpHeaders headers = null;
			if (username != null) {
				headers = getAuthenticationHeader(username, password);
			} else {
				headers = new HttpHeaders();
			}
			headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			RestTemplate template=getAutoProxyRestTemplate(url);
			ResponseEntity<String> response = template.postForEntity(url, request,
					String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode() || HttpStatus.MULTI_STATUS == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpClientErrorException e) {
			LOGGER.error("Error in put",e);
			LOGGER.error("StatusCode : {} getRawStatusCode :{} ",e.getStatusCode(),e.getRawStatusCode());
			LOGGER.error("StatusText : {}",e.getStatusText());
			LOGGER.error("ResponseBodyAsString : {}",e.getResponseBodyAsString());
			Map<String, String> res = null;
			try {
				res = (HashMap) Utils.convertJsonToObject(e.getResponseBodyAsString(), HashMap.class);
			} catch (TclCommonException e1) {
				// do nothing - just to suppress the exception
			}
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(res != null ? res.get("message") : e.getResponseBodyAsString());
		} catch (HttpServerErrorException e) {
			LOGGER.error("Error in put",e);
			LOGGER.error("StatusCode : {} getRawStatusCode :{} ",e.getStatusCode(),e.getRawStatusCode());
			LOGGER.error("StatusText : {}",e.getStatusText());
			LOGGER.error("ResponseBodyAsString : {}",e.getResponseBodyAsString());
			Map<String, String> res = null;
			try {
				res = (HashMap) Utils.convertJsonToObject(e.getResponseBodyAsString(), HashMap.class);
			} catch (TclCommonException e1) {
				// do nothing - just to suppress the exception
			}
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(res != null ? res.get("message") : e.getResponseBodyAsString());
		} catch (Exception e) {
			LOGGER.error("Error in put",e);
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestResponse put(String url, String requestBody, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			RestTemplate restTemplate = getAutoProxyRestTemplate(url);
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
			processAudit(url, "PUT", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpClientErrorException e) {
			LOGGER.error("Error in put",e);
			LOGGER.error("StatusCode : {} getRawStatusCode :{} ",e.getStatusCode(),e.getRawStatusCode());
			LOGGER.error("StatusText : {}",e.getStatusText());
			LOGGER.error("ResponseBodyAsString : {}",e.getResponseBodyAsString());
			Map<String, String> res = null;
			try {
				res = (HashMap) Utils.convertJsonToObject(e.getResponseBodyAsString(), HashMap.class);
			} catch (TclCommonException e1) {
				// do nothing - just to suppress the exception
			}
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(res != null ? res.get("message") : e.getResponseBodyAsString());
		} catch (HttpServerErrorException e) {
			LOGGER.error("Error in put",e);
			LOGGER.error("StatusCode : {} getRawStatusCode :{} ",e.getStatusCode(),e.getRawStatusCode());
			LOGGER.error("StatusText : {}",e.getStatusText());
			LOGGER.error("ResponseBodyAsString : {}",e.getResponseBodyAsString());
			Map<String, String> res = null;
			try {
				res = (HashMap) Utils.convertJsonToObject(e.getResponseBodyAsString(), HashMap.class);
			} catch (TclCommonException e1) {
				// do nothing - just to suppress the exception
			}
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(res != null ? res.get("message") : e.getResponseBodyAsString());
		} catch (Exception e) {
			LOGGER.error("Error in put",e);					
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * post used for http post call without custom header
	 * 
	 * @param url
	 * @return
	 */
	public RestResponse post(String url, String requestBody) {
		LOGGER.info("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		RestTemplate restTemplate = new RestTemplate();
		try {
			LOGGER.info("Calling url : {} , with request body {}", url, requestBody);
			HttpHeaders headers = null;
			headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			LOGGER.info("With Headers {} , with request body {}",request.getHeaders().toString(),request.getBody());
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * Post the request with json charset UTF-8 unicode support
	 *
	 * @param url
	 * @param requestBody
	 * @return
	 */
	public RestResponse postWithUtf8Support(String url, String requestBody) {
		LOGGER.info("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		RestTemplate restTemplate = new RestTemplate();
		try {
			LOGGER.info("Calling url : {} , with request body {}", url, requestBody);
			HttpHeaders headers = null;
			headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			LOGGER.info("With Headers {} , with request body {}", request.getHeaders().toString(), request.getBody());
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;
	}

	/**
	 * post used for http post call without custom header and to send request
	 * through proxy.
	 * 
	 * @param url
	 * @return
	 */
	public RestResponse postWithProxy(String url, String requestBody) {
		LOGGER.info("RestClientService.postWithProxy method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			LOGGER.info("Calling url : {} , with response body {}", url, requestBody);
			HttpHeaders headers = null;
			headers = new HttpHeaders();
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("postWithProxy Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			LOGGER.error("Error in postWithProxy {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}
		LOGGER.info("RestClientService.postWithProxy method ends.");
		return restResponse;
	}

	public RestResponse post(String url, String requestBody, HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		RestTemplate restTemplate = new RestTemplate();
		try {
			LOGGER.info("Calling url : {} , with response body {}", url, requestBody);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			/** Needs to remove this line before moving it to PROD **/
			Utils.disableSslVerification();
			
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode() || HttpStatus.MULTI_STATUS == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				LOGGER.error("Response Failed with status");
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Error in Rest", e);
			String errorpayload = e.getResponseBodyAsString();
			LOGGER.info("HttpStatusCodeException {}", url, requestBody);
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					LOGGER.error("Error in getting message ", e);
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				LOGGER.error("Error in parsing={}, errorpayload={}", jsex.getMessage(), errorpayload);
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestResponse postWithProxy(String url, String requestBody, HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			LOGGER.info("Calling url : {} , with response body {}", url, requestBody);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getAutoProxyRestTemplate(url).postForEntity(url, request,
					String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("postWithProxy Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode() || HttpStatus.MULTI_STATUS == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				LOGGER.error("Response Failed with status");
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Error in Rest", e);
			LOGGER.error("StatusCode : {} getRawStatusCode :{} ",e.getStatusCode(),e.getRawStatusCode());
			LOGGER.error("StatusText : {}",e.getStatusText());
			LOGGER.error("ResponseBodyAsString : {}",e.getResponseBodyAsString());
			String errorpayload = e.getResponseBodyAsString();
			LOGGER.info("HttpStatusCodeException {}", url, requestBody);
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));
				} else if (jsonObj.has("detail")) {
					restResponse.setErrorMessage(jsonObj.getString("detail"));
				} else if (Objects.nonNull(jsonObj)) {
					restResponse.setErrorMessage(errorpayload);
				} else {
					LOGGER.error("Error in getting message ", e);
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
				}
			} catch (JSONException jsex) {
				LOGGER.error("Error in parsing={}, errorpayload={}", jsex.getMessage(), errorpayload);
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * post used for http post call with custome header
	 * 
	 * @param url
	 * @return
	 */

	public RestResponse post(String url, LinkedMultiValueMap<String, Object> formBody, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(formBody, headers);
			ResponseEntity<String> response = getAutoProxyRestTemplate(url).postForEntity(url, request,
					String.class);
			processAudit(url, "POST", headers.toString(), formBody.toString(), response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		}

		catch (HttpStatusCodeException exception) {
			LOGGER.warn("Error In response ", exception);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(exception));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends. ");
		return restResponse;

	}
	
	public RestResponse postWithProxy(String url, LinkedMultiValueMap<String, Object> formBody, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(formBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).postForEntity(url, request,
					String.class);
			processAudit(url, "POST", headers.toString(), formBody.toString(), response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		}

		catch (HttpStatusCodeException exception) {
			LOGGER.warn("Error In response ", exception);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(exception));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends. ");
		return restResponse;

	}

	/**
	 * getAuthenticationHeader used for Authenticaltion Header
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
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

	/**
	 * post used for http post call with custome header
	 * 
	 * @param url
	 * @return
	 */

	public RestResponse postWithoutHeader(String url, LinkedMultiValueMap<String, Object> formBody) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

			HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(formBody);
			ResponseEntity<String> response = getProxyRestTemplate(url).postForEntity(url, request, String.class);
			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}

		} catch (HttpStatusCodeException exception) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(exception));
		}

		LOGGER.trace("RestClientService.post method ends. ");
		return restResponse;

	}

	public RestResponse postKeyCloak(String url, String requestBody, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).exchange(url, HttpMethod.POST, request,
					String.class);

			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestResponse deleteKeyCloak(String url, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).exchange(url, HttpMethod.DELETE, request,
					String.class);

			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestResponse deleteKeyCloakWithRequest(String url, String requestBody, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).exchange(url, HttpMethod.DELETE, request,
					String.class);

			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestResponse getCallForKeycloak(String url, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<String> request = new HttpEntity<>(headers);
			Utils.disableSslVerification();

			ResponseEntity<String> response = getProxyRestTemplate(url).exchange(url, HttpMethod.GET, request,
					String.class);

			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;
	}

	public HttpHeaders getHeaders(String url, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		HttpHeaders httpHeaders = new HttpHeaders();
		try {
			Utils.disableSslVerification();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<String> request = new HttpEntity<>(headers);
			Utils.disableSslVerification();

			RestTemplate restTemplate = new RestTemplate();
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
				protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
					super.prepareConnection(connection, httpMethod);
					connection.setInstanceFollowRedirects(false);
				}
			};
			if (StringUtils.isNotBlank(proxyHost) && proxyPort != null && !(StringUtils.isNotBlank(url) && (url.contains("customer.tatacommunications.com") || url.contains("customersandbox.tatacommunications.com")))) {
				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				requestFactory.setProxy(proxy);
			}
			restTemplate.setRequestFactory(requestFactory);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			httpHeaders = response.getHeaders();
		} catch (Exception e) {
			LOGGER.error("Error in header ", e);
		}
		LOGGER.trace("RestClientService.post method ends.");
		return httpHeaders;
	}

	public RestResponse putKeyCloak(String url, String requestBody, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).exchange(url, HttpMethod.PUT, request,
					String.class);

			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()
					|| HttpStatus.NO_CONTENT == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestTemplate getRestTemplate(String url) {
		LOGGER.info("Inside getRestTemplate WitoutProxy with URL {} ", url);
		if(StringUtils.isNotBlank(url) && (url.contains("customer.tatacommunications.com") || url.contains("customersandbox.tatacommunications.com"))) {//Doing to exclude the proxy for customer.tatacommunications.com
			return new RestTemplate();
		}
		if (StringUtils.isNotBlank(proxyHost) && proxyPort != null) {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			return new RestTemplate(requestFactory);
		} else {
			return new RestTemplate();
		}
	}
	
	public RestTemplate getProxyRestTemplate(String url) {
		LOGGER.info("Inside getProxyRestTemplate with URL {} ", url);
		if(StringUtils.isNotBlank(url) && (url.contains("customer.tatacommunications.com") || url.contains("customersandbox.tatacommunications.com"))) {//Doing to exclude the proxy for customer.tatacommunications.com
			return new RestTemplate();
		}
		if (StringUtils.isNotBlank(proxyHost) && proxyPort != null) {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
			requestFactory.setProxy(proxy);
			return new RestTemplate(requestFactory);
		} else {
			return new RestTemplate();
		}
	}
	
	public RestTemplate getAutoProxyRestTemplate(String url) {
		if (StringUtils.isNotBlank(url) && (url.contains("customer.tatacommunications.com") || url.contains("customersandbox.tatacommunications.com"))) {// Doing to exclude the
																								// proxy for

			LOGGER.info("Inside if condition getAutoProxyRestTemplate url {}", url);																// customer.tatacommunications.com
			return new RestTemplate();
		} else {
			LOGGER.info("Inside else condition getAutoProxyRestTemplate url {}", url);
			return appCtx.getBean(RestTemplate.class);
		}

	}

	public HttpHeaders postKeyCloakReturnResponseHeaders(String url, String requestBody, Map<String, String> header) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		try {
			Utils.disableSslVerification();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).exchange(url, HttpMethod.POST, request,
					String.class);
			return response.getHeaders();

		} catch (Exception e) {
			LOGGER.warn("Error In response ", e);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return null;
	}
	
	public RestResponse getWithBasicAuthentication(String url, Map<String, String> params, String username, String password) {
		LOGGER.trace("RestClientService.get method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();
			HttpHeaders headers = null;
			if (username != null) {
				headers = getAuthenticationHeader(username, password);
			} else {
				headers = new HttpHeaders();
			}
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> entity = new HttpEntity<>(headers);
			
			LOGGER.info("Inside getWithBasicAuthentication URL {} and Params {} ",url, params);
			ResponseEntity<String> response = (new RestTemplate()).exchange(constructUrlWithQueryParam(params, url),
					HttpMethod.GET, entity, String.class);
			LOGGER.info("getWithBasicAuthentication Output Received : {}", response);
			processAudit(constructUrlWithQueryParam(params, url), "GET", headers.toString(), null, response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.get method ends.");
		return restResponse;

	}

	public RestResponse getWithProxyBasicAuthentication(String url, Map<String, String> params, String username,
			String password) {
		LOGGER.trace("RestClientService.get method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();
			HttpHeaders headers = null;
			if (username != null) {
				headers = getAuthenticationHeader(username, password);
			} else {
				headers = new HttpHeaders();
			}
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			LOGGER.info("getWithProxyBasicAuthentication URL {} params {}", url, params );
			ResponseEntity<String> response = getProxyRestTemplate(url)
					.exchange(constructUrlWithQueryParam(params, url), HttpMethod.GET, entity, String.class);
			LOGGER.info("getWithProxyBasicAuthentication Output Received : {}", response);
			processAudit(constructUrlWithQueryParam(params, url), "GET", headers.toString(), null, response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.get method ends.");
		return restResponse;

	}

	public RestResponse putWithProxyBasicAuthentication(String url, String requestBody, Map<String, String> header,
			String username, String password) {
		LOGGER.trace("RestClientService.putWithProxyBasicAuthentication method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

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

			LOGGER.info("putWithProxyBasicAuthentication call url :{},header:{},request:{}", url, header, requestBody);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).exchange(url, HttpMethod.PUT, request,
					String.class);
			processAudit(url, "PUT", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}

		} catch (HttpClientErrorException ex) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ex.getResponseBodyAsString());
			LOGGER.error("Response for put call  HttpClientErrorException {}", ex.getResponseBodyAsString());
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Response for put call  Exception", e);
		}

		LOGGER.trace("RestClientService.putWithProxyBasicAuthentication method ends.");
		return restResponse;

	}

	/**
	 * @author vivek getWithQueryParamAndBasicAuth
	 * @param url
	 * @param params
	 * @param appID
	 * @param secretCode
	 * @return
	 */
	public RestResponse getWithQueryParam(String url, Map<String, String> params, HttpHeaders headers) {
		LOGGER.trace("RestClientService.get method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();
			HttpEntity<?> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response = (new RestTemplate()).exchange(constructUrlWithQueryParam(params, url),
					HttpMethod.GET, entity, String.class);
			processAudit(constructUrlWithQueryParam(params, url), "GET", headers.toString(), null, response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Error in Rest", e);
			LOGGER.error("StatusCode : {} getRawStatusCode :{} ",e.getStatusCode(),e.getRawStatusCode());
			LOGGER.error("StatusText : {}",e.getStatusText());
			LOGGER.error("ResponseBodyAsString : {}",e.getResponseBodyAsString());
			
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.error("Request Failed So trying with Proxy",e);
			restResponse=getWithQueryParamWithProxy(url, params, headers);
			//restResponse.setStatus(Status.ERROR);
			//restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.get method ends.");
		return restResponse;

	}

	public RestResponse getWithQueryParamWithProxy(String url, Map<String, String> params, HttpHeaders headers) {
		LOGGER.trace("RestClientService.get method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();
			HttpEntity<?> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response = getAutoProxyRestTemplate(url)
					.exchange(constructUrlWithQueryParam(params, url), HttpMethod.GET, entity, String.class);
			processAudit(constructUrlWithQueryParam(params, url), "GET", headers.toString(), null, response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Error in restTemplate",e);
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.error("Error in restTemplate",e);
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.get method ends.");
		return restResponse;

	}

	public RestTemplate getinsecureHttpsRestTemplate()
			throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
		/*
		 * TrustStrategy acceptingTrustStrategy = (java.security.cert.X509Certificate[]
		 * chain, String authType) -> true;
		 * 
		 * SSLContext sslContext =
		 * org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null,
		 * acceptingTrustStrategy) .build();
		 * 
		 * SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		 * // HostnameVerifier nullHostnameVerifier = new HostnameVerifier() { // public
		 * boolean verify(String hostname, SSLSession session) { // return true; // } //
		 * }; CloseableHttpClient httpClient =
		 * HttpClients.custom().setSSLSocketFactory(csf)
		 * .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		 * 
		 * HttpComponentsClientHttpRequestFactory requestFactory = new
		 * HttpComponentsClientHttpRequestFactory();
		 * 
		 * requestFactory.setHttpClient(httpClient); return new
		 * RestTemplate(requestFactory);
		 */

		SSLContext sslContext = SSLContext.getInstance("SSL");

		// set up a TrustManager that trusts everything
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} }, new SecureRandom());

		// SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		// HttpComponentsClientHttpRequestFactory requestFactory =
		// sslContext.getServerSocketFactory();
		requestFactory.setHttpClient(httpClient);
		return new RestTemplate(requestFactory);

		// SSLSocketFactory sf = new SSLSocketFactory(sslContext);
		// Scheme httpsScheme = new Scheme("https", 443, sf);
		// SchemeRegistry schemeRegistry = new SchemeRegistry();
		// schemeRegistry.register(httpsScheme);
		//
		// // apache HttpClient version >4.2 should use BasicClientConnectionManager
		// ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
		// HttpClient httpClient = new DefaultHttpClient(cm);

	}

	/**
	 * @author vivek put used for update param
	 * @param url
	 * @param requestBody
	 * @param header
	 * @param data
	 * @param ticketNo
	 * @return
	 */
	public RestResponse put(String url, String requestBody, HttpHeaders headers, MultipartFile data, String ticketNo) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("data", data);
			body.add("ticketNo", ticketNo);
			body.add("form", requestBody);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			RestTemplate restTemplate = getAutoProxyRestTemplate(url);
			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * @author vivek patch for update
	 * @param url
	 * @param requestBody
	 * @param headers
	 * @return
	 */
	public RestResponse patch(String url, String requestBody, HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		// RestTemplate restTemplate = new RestTemplate();
		try {
			/*
			 * HttpComponentsClientHttpRequestFactory requestFactory = new
			 * HttpComponentsClientHttpRequestFactory();
			 * restTemplate.setRequestFactory(requestFactory);
			 */
			RestTemplate restTemplate = getinsecureHttpsRestTemplate();
			LOGGER.debug("Calling url : {} , with response body {}", url, requestBody);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
			processAudit(url, "PATCH", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.debug("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode() || HttpStatus.MULTI_STATUS == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}
	
	public RestResponse patchWithProxy(String url, String requestBody, HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		// RestTemplate restTemplate = new RestTemplate();
		try {
			/*
			 * HttpComponentsClientHttpRequestFactory requestFactory = new
			 * HttpComponentsClientHttpRequestFactory();
			 * restTemplate.setRequestFactory(requestFactory);
			 */
			RestTemplate restTemplate = getAutoProxyRestTemplate(url);
			LOGGER.debug("Calling url : {} , with response body {}", url, requestBody);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
			processAudit(url, "PATCH", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.debug("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode() || HttpStatus.MULTI_STATUS == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * constructUrlWithQueryParam
	 * 
	 * @param queryParam
	 * @param url
	 * @return
	 */
	private String constructUrlWithQueryParam(Map<String, String> queryParam, String url) {

		if (queryParam != null && !queryParam.isEmpty()) {
			return url + "" + "?" + queryParam.entrySet().stream().map(p -> p.getKey() + "=" + p.getValue())
					.reduce((p1, p2) -> p1 + "&" + p2).orElse("");
		}
		return url;

	}

	/**
	 * postWithClientRepo used with client repo
	 * 
	 * @param url
	 * @param requestBody
	 * @param headers
	 * @return
	 */
	public RestResponse postWithClientRepo(String url, String requestBody, HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		// RestTemplate restTemplate = new RestTemplate();

		try {
			// Utils.disableSslVerification();
			RestTemplate restTemplate = getinsecureHttpsRestTemplate();
			// HttpComponentsClientHttpRequestFactory requestFactory = new
			// HttpComponentsClientHttpRequestFactory();
			// LOGGER.debug("Calling url : {} , with response body {}", url, requestBody);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			// restTemplate.setRequestFactory(requestFactory);
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			// ResponseEntity<String> response = (new RestTemplate()).postForEntity(url,
			// request, String.class);
			LOGGER.debug("Output Received : {}", response);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * postWithUrlEncodedRequest method to call rest apis with url encoded request
	 * 
	 * @param url
	 * @param requestBody
	 * @param headers
	 * @return
	 */
	public RestResponse postWithUrlEncodedRequest(String url, MultiValueMap<String, String> requestBody,
			HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with url {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			RestTemplate restTemplate = getinsecureHttpsRestTemplate();
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody.toString(), response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.debug("Response Received from the post api call : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.error("Error in post so trying with proxy", e);
			restResponse=postWithUrlEncodedRequestWithProxy(url, requestBody, headers);
			//restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			//restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}
	
	public RestResponse postWithUrlEncodedRequestWithProxy(String url, MultiValueMap<String, String> requestBody,
			HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with url {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = (getProxyRestTemplate(url)).postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody.toString(), response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.debug("Response Received from the post api call : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			String errorpayload = e.getResponseBodyAsString();
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jsex) {
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jsex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.error("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestResponse postWithBasicAuthentication(String url, String requestBody, Map<String, String> header,
			String username, String password) {
		LOGGER.trace("RestClientService.postWithBasicAuthentication method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

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

			LOGGER.info("postWithBasicAuthentication call url :{},header:{},request:{}", url, header, requestBody);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = (new RestTemplate()).postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("postWithBasicAuthentication Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}

		} catch (HttpClientErrorException ex) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ex.getResponseBodyAsString());
			LOGGER.error("Response for post call  HttpClientErrorException {}", ex.getResponseBodyAsString());
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Response for post call  Exception", e);
		}

		LOGGER.trace("RestClientService.postWithBasicAuthentication method ends.");
		return restResponse;

	}

	public RestResponse postWithProxyBasicAuthentication(String url, String requestBody, Map<String, String> header,
			String username, String password) {
		LOGGER.info("RestClientService.postWithProxyBasicAuthentication method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

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

			LOGGER.info("postWithProxyBasicAuthentication call url :{},header:{},request:{}", url, header, requestBody);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getAutoProxyRestTemplate(url).postForEntity(url, request,
					String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpClientErrorException ex) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ex.getResponseBodyAsString());
			LOGGER.error("Response for post call  HttpClientErrorException {}", ex.getResponseBodyAsString());
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Response for post call  Exception", e);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	public RestResponse postWithBasicAuthenticationProxy(String url, String requestBody, Map<String, String> header,
			String username, String password) {
		LOGGER.info("RestClientService.postWithProxyBasicAuthentication method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

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

			LOGGER.info("postWithProxyBasicAuthentication call url :{},header:{},request:{}", url, header, requestBody);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = getProxyRestTemplate(url).postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpClientErrorException ex) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ex.getResponseBodyAsString());
			LOGGER.error("Response for post call  HttpClientErrorException {}", ex.getResponseBodyAsString());
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Response for post call  Exception", e);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * 
	 * processAudit
	 * 
	 * @param requestUrl
	 * @param httpMethod
	 * @param requestHeader
	 * @param requestPayload
	 * @param responsePayload
	 */
	private void processAudit(String requestUrl, String httpMethod, String requestHeader, String requestPayload,
			String responsePayload, String statusCode) {
		try {
			ThirdPartyServiceAuditBean thirdPartyServiceAuditBean = new ThirdPartyServiceAuditBean();
			thirdPartyServiceAuditBean.setCreatedBy(Utils.getSource());
			thirdPartyServiceAuditBean.setCreatedTime(new Date());
			thirdPartyServiceAuditBean.setHttpMethod(httpMethod);
			thirdPartyServiceAuditBean.setMdcToken(MDC.get(CommonConstants.MDC_TOKEN_KEY));
			thirdPartyServiceAuditBean.setRequestHeader(requestHeader);
			thirdPartyServiceAuditBean.setRequestPayload(requestPayload);
			thirdPartyServiceAuditBean.setResponsePayload(responsePayload);
			thirdPartyServiceAuditBean.setSessionStateId(null);
			thirdPartyServiceAuditBean.setRequestUrl(requestUrl);
			thirdPartyServiceAuditBean.setStatusCode(statusCode);
			LOGGER.info("MDC Filter token value in before Queue call processAudit {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send("optimus_audit_thirdparty_queue", Utils.convertObjectToJson(thirdPartyServiceAuditBean));
		} catch (Exception e) {
			LOGGER.error("Error in sending the audit", e);
		}
	}
	
	public RestResponse postSkipSslVerification(String url, String requestBody, HttpHeaders headers) {
		LOGGER.trace("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		RestTemplate restTemplate = new RestTemplate();
		try {
			Utils.disableSslVerification();
			LOGGER.info("Calling url : {} , with response body {}", url, requestBody);
			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			processAudit(url, "POST", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				LOGGER.error("Response Failed with status");
				restResponse.setStatus(Status.FAILURE);
			}
		} catch (HttpStatusCodeException e) {
			LOGGER.error("Error in Rest", e);
			String errorpayload = e.getResponseBodyAsString();
			LOGGER.info("HttpStatusCodeException {}", url, requestBody);
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(errorpayload);
				if (jsonObj.has("message")) {
					restResponse.setErrorMessage(jsonObj.getString("message"));

				} else {
					LOGGER.error("Error in getting message ", e);
					restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));

				}
			} catch (JSONException jex) {
				LOGGER.error("Error in parsing={}, errorpayload={}", jex.getMessage(), errorpayload);
				restResponse.setErrorMessage(ExceptionUtils.getStackTrace(jex));

			}

			restResponse.setStatus(Status.ERROR);

		} catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}

	/**
	 * Post and return headers of response
	 *
	 * @param url
	 * @param header
	 * @param requestBody
	 * @return
	 */
	public HttpHeaders postAndReturnHeadersWithProxy(String url, Map<String, String> header, MultiValueMap<String, String> bodyParam) {
		LOGGER.info("RestClientService.postAndReturnHeadersWithProxy method invoked with parameter {}.", url);
		HttpHeaders httpHeaders = new HttpHeaders();
		ResponseEntity<String> response = null;
		try {
			Utils.disableSslVerification();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/x-www-form-urlencoded");
			for (Entry<String, String> headerMap : header.entrySet()) {
				headers.set(headerMap.getKey(), headerMap.getValue());
			}

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyParam, headers);
			Utils.disableSslVerification();

			RestTemplate restTemplate = new RestTemplate();
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
				protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
					super.prepareConnection(connection, httpMethod);
					connection.setInstanceFollowRedirects(false);
				}
			};
			if (StringUtils.isNotBlank(proxyHost) && proxyPort != null) {
				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				requestFactory.setProxy(proxy);
			}
			restTemplate.setRequestFactory(requestFactory);
			LOGGER.info("Inside postAndReturnHeadersWithProxy hitting url {} with headers {} ",url, request);
			response = getProxyRestTemplate(url).exchange(url, HttpMethod.POST, request,
					String.class);
			LOGGER.info("postAndReturnHeadersWithProxy Response status : {}", response);
			if(response != null && response.getHeaders() != null) {
				httpHeaders = response.getHeaders();
			}
			LOGGER.info("postAndReturnHeadersWithProxy Response Headers : {}", httpHeaders);

		} catch (Exception e) {
			LOGGER.error("Error in header inside postAndReturnHeadersWithProxy response {} and error {}",response, e.toString());
		}
		LOGGER.trace("RestClientService.post method ends.");
		return httpHeaders;
	}
	
	/**
	 * Method to trigger Http method PUT with Basic authentication
	 * @param url
	 * @param requestBody
	 * @param header
	 * @param username
	 * @param password
	 * @return
	 */
	public RestResponse putWithBasicAuthentication(String url, String requestBody, Map<String, String> header,
			String username, String password) {
		LOGGER.trace("RestClientService.putWithBasicAuthentication method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			Utils.disableSslVerification();

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

			LOGGER.info("putWithBasicAuthentication call url :{},header:{},request:{}", url, header, requestBody);

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = (new RestTemplate()).exchange(url, HttpMethod.PUT, request, String.class);
			processAudit(url, "PUT", headers.toString(), requestBody, response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("putWithBasicAuthentication Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.NO_CONTENT == response.getStatusCode() || HttpStatus.OK == response.getStatusCode()
					|| HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}

		} catch (HttpClientErrorException ex) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ex.getResponseBodyAsString());
			LOGGER.error("Response for put call  HttpClientErrorException {}", ex.getResponseBodyAsString());
		} catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			LOGGER.error("Response for put call  Exception", e);
		}

		LOGGER.trace("RestClientService.putWithBasicAuthentication method ends.");
		return restResponse;

	}
	
	/**
	 * Method to post multipart file to downstream.
	 * @param url
	 * @param multipartFile
	 * @return RestResponse
	 */
	public RestResponse postMultipartFile(String url,  MultipartFile multipartFile) {
		LOGGER.info("RestClientService.post method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = null;
			headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			
			MultiValueMap<String, Object> body
			  = new LinkedMultiValueMap<>();
			body.add("file", new FileSystemResource(convert(multipartFile)));
			
			HttpEntity<MultiValueMap<String, Object>> requestEntity
			 = new HttpEntity<>(body, headers);
			
				ResponseEntity<String> response = restTemplate
				  .postForEntity(url, requestEntity, String.class);
				
			processAudit(url, "POST", headers.toString(), "", response.getBody(),
					String.valueOf(response.getStatusCode()));
			LOGGER.info("Output Received : {}", response);
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode() || HttpStatus.CREATED == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		}catch (HttpClientErrorException exception) {
		    LOGGER.error("Exception status code {}",exception.getStatusCode());
		    LOGGER.error("Exception response body {}",exception.getResponseBodyAsString());
		    restResponse.setData(exception.getResponseBodyAsString());
		    restResponse.setErrorMessage(ExceptionUtils.getStackTrace(exception));
			restResponse.setStatus(Status.ERROR);
		    }
		
		catch (Exception e) {
			LOGGER.warn("Error in post {}", e);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
			restResponse.setStatus(Status.ERROR);
		    restResponse.setData(e.getMessage());
		}

		LOGGER.trace("RestClientService.post method ends.");
		return restResponse;

	}
	
	
	public RestResponse getWithoutAuth(String url) {
		LOGGER.trace("RestClientService.get method invoked with parameter {}.", url);
		RestResponse restResponse = new RestResponse();
		try {
			ResponseEntity<String> response =new RestTemplate().getForEntity(url, String.class);
			processAudit(url, "GET", null, null, response.getBody(), String.valueOf(response.getStatusCode()));
			restResponse.setData(response.getBody());
			if (HttpStatus.OK == response.getStatusCode()) {
				restResponse.setStatus(Status.SUCCESS);
			} else {
				restResponse.setStatus(Status.FAILURE);
			}
		} 
		// Added to handle 404 with response json for bulk feasibility
		catch (HttpClientErrorException exception) {
			LOGGER.error("Exception status code {}", exception.getStatusCode());
			LOGGER.error("Exception response Body {}", exception.getResponseBodyAsString());
			restResponse.setData(exception.getResponseBodyAsString());
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(exception));
			restResponse.setStatus(Status.ERROR);
		}
		catch (Exception e) {
			restResponse.setStatus(Status.ERROR);
			restResponse.setErrorMessage(ExceptionUtils.getStackTrace(e));
		}

		LOGGER.trace("RestClientService.get method ends.");
		return restResponse;

	}

	
	 public static File convert(MultipartFile file)
	  {    
	    File convFile = new File(file.getOriginalFilename());
	    try {
	        convFile.createNewFile();
	          FileOutputStream fos = new FileOutputStream(convFile); 
	            fos.write(file.getBytes());
	            fos.close(); 
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } 

	    return convFile;
	 }
	 
	 /**
	  * Method to download multipart file.
	  * @param url
	  * @param multipartFile
	  * @return RestResponse
	  * @throws TclCommonException 
	  */
	 public ResponseEntity<byte[]> getFile(String url) throws TclCommonException {
		 LOGGER.info("RestClientService.get method invoked with url {}", url);
		 RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<byte[]> responseEntity = null;
		 try {
			 HttpHeaders headers = null;
			 headers = new HttpHeaders();
			 headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			 HttpEntity<String> entity = new HttpEntity<>(headers); 
			 responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
			 LOGGER.info("Output Received : {}", responseEntity);
		 }
		 catch (HttpClientErrorException exception) {
			 LOGGER.error("Exception status code {}", exception.getStatusCode());
			 LOGGER.error("Exception response Body {}", exception.getResponseBodyAsString());
			 throw new TclCommonException(exception.getMessage(), exception, ResponseResource.R_CODE_NOT_FOUND);

		 } 
		 return responseEntity;
	 }
	 public ResponseEntity<String> getAndReturnHeadersWithBasicAuthentication(String url, Map<String, String> header, String username, String password) {
			LOGGER.trace("RestClientService.getAndReturnHeadersWithBasicAuthentication {}.", url);
			
			ResponseEntity<String> response = null ;
			try {
				Utils.disableSslVerification();
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
				HttpEntity<?> entity = new HttpEntity<>(headers);
				
				Utils.disableSslVerification();

				RestTemplate restTemplate = new RestTemplate();
				SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
					protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
						super.prepareConnection(connection, httpMethod);
						connection.setInstanceFollowRedirects(false);
					}
				};
				
				restTemplate.setRequestFactory(requestFactory);
				LOGGER.info("Inside getAndReturnHeadersWithOutProxy hitting url {} with headers {} ",url, entity);
				response = getRestTemplate(url).exchange(url, HttpMethod.GET, entity,
						String.class);
				LOGGER.info("getAndReturnHeadersWithOutProxy Response status : {}", response);


			} catch (Exception e) {
				LOGGER.error("Error in header inside getAndReturnHeadersWithOutProxy response {} and error {}",response, e.toString());
			}
			LOGGER.trace("RestClientService.get method ends.");
			return response;
		}
		public ResponseEntity<String> getAndReturnHeadersWithProxyBasicAuthentication(String url, Map<String, String> header, String username, String password) {
			LOGGER.trace("RestClientService.getAndReturnHeaders With Proxy BasicAuthentication {}.", url);
			
			ResponseEntity<String> response = null ;
			try {
				Utils.disableSslVerification();
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
				HttpEntity<?> entity = new HttpEntity<>(headers);
				
				Utils.disableSslVerification();

				RestTemplate restTemplate = new RestTemplate();
				SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
					protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
						super.prepareConnection(connection, httpMethod);
						connection.setInstanceFollowRedirects(false);
					}
				};
				if (StringUtils.isNotBlank(proxyHost) && proxyPort != null) {
					Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
					requestFactory.setProxy(proxy);
				}
				restTemplate.setRequestFactory(requestFactory);
				LOGGER.info("Inside getAndReturnHeadersWithProxy hitting url {} with headers {} ",url, entity);
				response = getProxyRestTemplate(url).exchange(url, HttpMethod.GET, entity,
						String.class);
				LOGGER.info("getAndReturnHeadersWithOutProxy Response status : {}", response);


			} catch (Exception e) {
				LOGGER.error("Error in header inside getAndReturnHeadersWithOutProxy response {} and error {}",response, e.toString());
			}
			LOGGER.trace("RestClientService.get method ends.");
			return response;
		}


}
