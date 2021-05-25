package com.tcl.dias.performance.service.v1;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.customexception.TCLException;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.performance.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * 
 * @author chetan chaudhary
 * This class deals with Network's Performance 
 * with Interface Device and Service Performance
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class NetworkPerformanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkPerformanceService.class);

	@Value("${network.performance.capm}")
	String capm;

	@Value("${network.performance.baseurl}")
	String baseurl;

	@Value("${performance.username}")
	String username;

	@Value("${performance.password}")
	String password;

	@Autowired
	RestClientService restClientService;

	/**
	 * getHeader() - returns the header for the http request
	 * 
	 * @return header
	 */
	private HashMap<String, String> getHeader() {

		HashMap<String, String> header = new HashMap<>();
		header.put("Accept", "application/json");
		header.put("Content-Type", "application/json");

		return header;
	}

	/**
	 * @author chetan used to get the basic auth with encoded form getBasicAuth
	 * @param userName
	 * @param password
	 * @return
	 */
	private HttpHeaders getBasicAuth(String userName, String password, Map<String, String> contentTypes) {
		
		HttpHeaders headers = new HttpHeaders();
		if (userName != null && password != null) {
			String auth = userName + ":" + password;
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.set("Authorization", authHeader);
		}
		if (contentTypes != null && !contentTypes.isEmpty()) {
			contentTypes.forEach((key, value) -> {
				headers.set(key, value);

			});
		}
		return headers;

	}

	/**
	 * 
	 * @param interfaceId
	 * @param startDate
	 * @param endDate
	 * @param parameter
	 * @return
	 * @throws ParseException
	 */

	public RestResponse getInterfacePerformance(String interfaceId, String startTime, String endTime,
			String paramGroup, List<String> parameter, String top, String skip, String top1, String timezone) throws TclCommonException {
		RestResponse response = null;
		// portmfs, jittermfs - im_Latency,im_Jitter,im_PacketsLost
		
		// im_Bytes, im_Discards
		validateRequest(paramGroup, interfaceId, startTime, endTime, parameter);
		try {
		Map<String, String> paramMap = new HashMap<>();
		long startMillis = parseDateToEpoch(startTime,timezone);
		long endMillis = parseDateToEpoch(endTime,timezone);
		parameter.add(0, "component/Name,Timestamp,ID");
		paramMap.put("$expand", "component");
		paramMap.put("$format", "json");
		paramMap.put("$top", top);
		paramMap.put("$skip", skip);
		paramMap.put("top", top1);
		paramMap.put("$orderby", "ID asc, Timestamp asc");
		paramMap.put("starttime", String.valueOf(startMillis));
		paramMap.put("endtime", String.valueOf(endMillis));
		paramMap.put("resolution", "RATE");
		paramMap.put("$select", String.join(",", parameter));
		paramMap.put("$filter", "((interface/Name eq '" + interfaceId + "'))");
		response = restClientService.getWithQueryParam(capm + baseurl + "/" + paramGroup, paramMap,
				getBasicAuth(username, password, getHeader()));
		LOGGER.debug(response.toString());
		
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_INTERFACE_PERFORMANCE, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * 
	 * @param deviceIp
	 * @param startTime
	 * @param endTime
	 * @param paramGroup
	 * @param parameter
	 * @return
	 * @throws ParseException
	 */
	public RestResponse getDevicePerformance(String deviceIp, String startTime, String endTime,
			String paramGroup, List<String> parameter, String top, String skip, String top1, String timezone,String resolution) throws TclCommonException {
		//parameterGroup - cpumfs, memorymfs
		// parameters - im_Utilization
		//parameterGroup - jittermfs
		// parameters - im_Jitter,im_PacketsLost,im_Latency,im_PathAvailability
		RestResponse response=null;
		validateRequest(paramGroup, deviceIp, startTime, endTime, parameter);
		try {
		Map<String, String> paramMap = new HashMap<>();
		long startMillis = parseDateToEpoch(startTime,timezone);
		long endMillis = parseDateToEpoch(endTime,timezone);

		parameter.add(0, "component/Name,Timestamp,ID");
		paramMap.put("$format", "json");
		paramMap.put("$top", top);
		paramMap.put("$skip", skip);
		paramMap.put("top", top1);
		paramMap.put("$expand", "component");
		paramMap.put("$orderby", "ID asc, Timestamp asc");
		paramMap.put("starttime", String.valueOf(startMillis));
		paramMap.put("endtime", String.valueOf(endMillis));
		paramMap.put("resolution", resolution);
		paramMap.put("$select", String.join(",", parameter));
		paramMap.put("$filter", "((device/PrimaryIPAddress eq '" + deviceIp + "'))");
		LOGGER.info("RestClientServiceUrl {} {}", capm,baseurl);
	    response = restClientService.getWithQueryParam(capm + baseurl + "/" + paramGroup, paramMap,
				getBasicAuth(username, password, getHeader()));
		LOGGER.info("Response {} ",response);
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_PERFORMANCE_DEVICE, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * 
	 * @param paramGroup
	 * @param interfaceId
	 * @param startTime
	 * @param endTime
	 * @param paramGroup
	 * @param parameter
	 * @return
	 * @throws ParseException
	 */

	public RestResponse getServicePerformance(String serviceId, String startTime, String endTime, String paramGroup,
			List<String> parameter, String component, String top, String skip, String top1, String timezone) throws TclCommonException {
		//paramGroup - portmfs
		RestResponse response=null;
		validateRequest(paramGroup, serviceId, startTime, endTime, parameter);
		try {
		Map<String, String> paramMap = new HashMap<>();
		long startMillis = parseDateToEpoch(startTime,timezone);
		long endMillis = parseDateToEpoch(endTime,timezone);

		parameter.add(0, "component/Name,Timestamp,ID");
		paramMap.put("$expand", component);
		paramMap.put("$format", "json");
		paramMap.put("$top", top);
		paramMap.put("$skip", skip);
		paramMap.put("top", top1);
		paramMap.put("$orderby", "ID asc, Timestamp asc");
		paramMap.put("starttime", String.valueOf(startMillis));
		paramMap.put("endtime", String.valueOf(endMillis));
		paramMap.put("resolution", "RATE");
		paramMap.put("$select", String.join(",", parameter));
		paramMap.put("$filter",
				"((substringof(tolower('-" + serviceId + "-'), tolower(interface/AlternateName)) eq true))");
		String url =capm + baseurl + "/"+paramGroup;
		HttpHeaders header=getBasicAuth(username, password, getHeader());
		LOGGER.info("Url : {}  :::  Header {}  ::: paramMap {}",url,header,paramMap);
		 response = restClientService.getWithQueryParam(url, paramMap,
				 header);
		LOGGER.debug(response.toString());
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_INTERFACE_PERFORMANCE, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	/**
	 * 
	 * @param time
	 * @return
	 * @throws TclCommonException
	 */

	private long parseDateToEpoch(String time, String timezone) throws TclCommonException {
		try {
			LOGGER.debug("Time passed: {} ", time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone(timezone));
			Date date = sdf.parse(time);
			return date.getTime() / 1000;
		} catch (ParseException pe) {
			LOGGER.error("Error parsing Date", pe);
			throw new TclCommonException("Wrong Time format " + time);
		}
	}
	private void validateRequest(String paramGroup, String deviceIp, String startTime, String endTime,
			List<String> parameter) throws TclCommonException  {
		if(paramGroup==null ||  deviceIp==null || startTime==null ||  endTime==null ||
				 parameter==null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

}
