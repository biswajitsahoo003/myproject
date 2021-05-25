package com.tcl.dias.filters.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * All the filters related Services will be implemented in this class
 * 
 * @author chetan chaudhary
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class FilterService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterService.class);

	@Value("${filters.request.snow}")
	String snow;

	@Value("${filters.request.baseurl}")
	String baseurl;

	@Value("${filters.username}")
	String username;

	@Value("${filters.password}")
	String password;

	@Value("${filters.change_request}")
	String change_request;

	@Value("${filters.sn_customerservice_case}")
	String incident_request;

	@Value("${filters.sc_req_item}")
	String service_request;

	@Autowired
	MQUtils mqUtils;
	@Value("${rabbitmq.customerle.queue}")
	String customerLeIdsQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	RestClientService service;

	@Value("${rabbitmq.get.partner.legal.entity.by.partner}")
	String getPartnerLegalEntityDetails;

	@Value("${rabbitmq.get.cutomer.cuid.by.partner}")
	String getCustomerCuidByPartnerId;

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
	 * 
	 * @param userName
	 * @param password
	 * @param contentTypes
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
	 * @author chetan chaudhary
	 *  it is used to get the filters for the change request.
	 * @param searchBy
	 * @param serviceType
	 * @param requestType
	 * @param status
	 * @param valueFor
	 * @return
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public RestResponse filterValuesForChangeRequest(
			String searchBy,
			String serviceType,
			String requestType,
			String status,
			String valueFor) throws TclCommonException {
		RestResponse response = null;
		try {
			Map<String, String> paramMap = new HashMap<>();
			//"2019-02-15 00:00:00"
			Calendar instance = Calendar.getInstance();
			SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			instance.setTime(date);
			String currentYearDate = isoFormat.format(instance.getTime());
			instance.add(Calendar.YEAR, -1);
			String previousYearDate = isoFormat.format(instance.getTime());
			String wClause = "";
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				wClause = "contact_typeINPhone,Email,Portal,Customer,Optimus"
						+"^sys_created_on>=" + previousYearDate + "^sys_created_on<=" + currentYearDate
						+"^company.ref_customer_account.u_cuidIN" + this.getCustomerCUIDForPartnerLe();
			} else {
				wClause = "contact_typeINPhone,Email,Portal,Customer,Optimus"
						+"^sys_created_on>=" + previousYearDate + "^sys_created_on<=" + currentYearDate
						+"^company.ref_customer_account.u_cuidIN" + this.getCuid();
			}
			if(searchBy!=null) {
				wClause= wClause+"^(number="+searchBy+"^ORu_service_id="+searchBy+")";
			}
			if(serviceType!=null) {
				wClause= wClause+"^u_product_name.name="+serviceType;
			}
			if(requestType!=null) {
				wClause= wClause+"^u_subcategory="+requestType;
			}
			if(status!=null) {
				wClause= wClause+"^state="+status;
			}
			paramMap.put("sysparm_query", wClause);
			paramMap.put("sysparm_count", "true");
			paramMap.put("sysparm_group_by",valueFor);
			paramMap.put("sysparm_display_value", "true");
			LOGGER.info("FilterService: filterValuesForChangeRequest(): paramMap = " + paramMap);

			response = service.getWithQueryParam(snow + baseurl + "/" + change_request, paramMap,
					getBasicAuth(username, password, getHeader()));
			 LOGGER.debug(response.toString());
		}catch(Exception ex) {
			LOGGER.error(ExceptionConstants.COMMON_ERROR, ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	/**
	 * @author chetan chaudhary
	 * @param searchBy
	 * @param serviceType
	 * @param requestType
	 * @param status
	 * @param valueFor
	 * @return
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public RestResponse filterValuesForServiceRequest(
			String searchBy,
			String serviceType,
			String requestType,
			String status,
			String valueFor) throws TclCommonException {
		
		RestResponse response = null;
		try {
			Map<String, String> paramMap = new HashMap<>();
			Calendar instance = Calendar.getInstance();
			SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			instance.setTime(date);
			String currentYearDate = isoFormat.format(instance.getTime());
			instance.add(Calendar.YEAR, -1);
			String previousYearDate = isoFormat.format(instance.getTime());
			String wClause="";
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				wClause="request.company.ref_customer_account.u_cuidIN"+this.getCustomerCUIDForPartnerLe()
						+"^sys_created_on>="+previousYearDate+"^sys_created_on<="+currentYearDate;
			} else {
				wClause="request.company.ref_customer_account.u_cuidIN"+ this.getCuid()
						+"^sys_created_on>="+previousYearDate+"^sys_created_on<="+currentYearDate;
			}
			
			if(searchBy!=null) {
				wClause= wClause+"^(request.number="+searchBy+"^ORu_service_id="+searchBy+")";
			}
			if(serviceType!=null) {
				wClause= wClause+"^u_product_name.name="+serviceType;
			}
			if(requestType!=null) {
				wClause= wClause+"^cat_item.name="+requestType;
			}
			if(status!=null) {
				wClause= wClause+"^state="+status;
			}

			paramMap.put("sysparm_query", wClause);
			paramMap.put("sysparm_count", "true");
			paramMap.put("sysparm_group_by",valueFor);
			paramMap.put("sysparm_display_value", "true");
			LOGGER.info("FilterService: filterValuesForServiceRequest(): paramMap = " + paramMap);
			HttpHeaders header=getBasicAuth(username, password, getHeader());
			String url=snow + baseurl + "/" + service_request;
			LOGGER.info("Url {} :: headers {} ::: paramMap {}" ,url,header, paramMap);
			response = service.getWithQueryParam(url, paramMap,
					header);
			LOGGER.debug(response.toString());
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * @author chetan chaudhary
	 * @param searchBy
	 * @param serviceType
	 * @param issueType
	 * @param impact
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param valueFor
	 * @return
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public RestResponse filterValuesForIncidentRequest(
			String searchBy,
			String serviceType,
			String issueType,
			String impact,
			String status,
			String startDate,
			String endDate,
			String valueFor)throws TclCommonException {
		RestResponse response = null;
		try {
			Map<String, String> paramMap = new HashMap<>();
			String wClause="";
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				wClause="account.u_cuidIN"+ this.getCustomerCUIDForPartnerLe();
			} else {
				wClause="account.u_cuidIN"+ this.getCuid();
			}
			if(searchBy!=null) {
				wClause= wClause+"^(u_product_name="+searchBy+"^ORstate="+searchBy+")";
			}
			if(serviceType!=null) {
				wClause= wClause+"^product.name="+serviceType;
			}
			if(issueType!=null) {
				wClause= wClause+"^category="+issueType;
			}
			if(impact!=null) {
				wClause= wClause+"^impact="+impact;
			}
			if(status!=null) {
				wClause= wClause+"^state="+status;
			}
			if (startDate != null && endDate != null) {
				// if both dates are passed, check its in one yr range.
				validateFromToDates(startDate, endDate);
				wClause = wClause + "^sys_created_on>=" + startDate;
				wClause = wClause + "^sys_created_on<=" + endDate;
			} else if (startDate == null && endDate != null || startDate != null && endDate == null) {
				LOGGER.error(ExceptionConstants.DATE_ERROR);
				throw new TclCommonException(ExceptionConstants.DATE_ERROR, null, ResponseResource.R_CODE_ERROR);
			}
		    wClause = wClause + "^u_acknowledgement=true";
			wClause = wClause + "^ORu_notify=false";
			paramMap.put("sysparm_query", wClause);
			paramMap.put("sysparm_count", "true");
			paramMap.put("sysparm_group_by", valueFor);
			paramMap.put("sysparm_display_value", "true");
			LOGGER.info("FilterService: filterValuesForIncidentRequest(): paramMap = " + paramMap);
			String url=snow + baseurl + "/" + incident_request;
			HttpHeaders header=getBasicAuth(username, password, getHeader());
			
			LOGGER.info("Url {} ::: Header {} : paramMap {} :: " ,url,header, paramMap);
			response = service.getWithQueryParam(url, paramMap,
					header);
			LOGGER.debug(response.toString());
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	/**
	 * @author chetan chaudhary
	 * @param searchBy
	 * @param serviceType
	 * @param valueFor
	 * @return
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public RestResponse filterValuesForPlannedEvents(
			String searchBy,
			String serviceType,
			String valueFor)throws TclCommonException {
		RestResponse response = null;
		try {
			Calendar instance = Calendar.getInstance();
			SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			instance.setTime(date);
			instance.add(Calendar.DAY_OF_MONTH, -14);
			String startDate = isoFormat.format(instance.getTime());
			Map<String, String> paramMap = new HashMap<>();
			String wClause = "";
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				wClause = "company.ref_customer_account.u_cuidIN" + this.getCustomerCUIDForPartnerLe()
						+ "^sys_created_on>=" + startDate
						+ "^contact_typeIN3rd Party Local Loop,Tata Backbone Network,Tata Backbone 3rd Party,u_international_cables,voice_pe,mobility_pe";
			}
			else{
				wClause = "company.ref_customer_account.u_cuidIN" + this.getCuid()
						+ "^sys_created_on>=" + startDate
						+ "^contact_typeIN3rd Party Local Loop,Tata Backbone Network,Tata Backbone 3rd Party,u_international_cables,voice_pe,mobility_pe";
			}
			if(searchBy!=null) {
				wClause= wClause+"^(number="+searchBy+"^ORu_service_id="+searchBy+")";
			}
			if(serviceType!=null) {
				wClause= wClause+"^u_product_name="+serviceType;
			}

			paramMap.put("sysparm_query", wClause);
			paramMap.put("sysparm_count", "true");
			paramMap.put("sysparm_group_by", valueFor);
			paramMap.put("sysparm_display_value", "true");
			LOGGER.info("FilterService: filterValuesForPlannedEvents(): paramMap = " + paramMap);

			response = service.getWithQueryParam(snow + baseurl + "/" + change_request, paramMap,
					getBasicAuth(username, password, getHeader()));
			LOGGER.debug(response.toString());

		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	
	/**
	 * 
	 * Get Customer ID 
	 * @return comma separated string of CUIDs
	 * @throws TclCommonException
	 */
	private String getCuid() throws TclCommonException {
		String leIDS = null;

		try {
			UserInformation userInfo = userInfoUtils.getUserInformation(Utils.getSource());
			if (Objects.isNull(userInfo))
				throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_USERINFO);

			List<CustomerDetail> customers = userInfo.getCustomers();
			if (Objects.isNull(customers))
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);

			List<String> customerLeIds = customers.stream().map(cust -> cust.getCustomerLeId().toString())
					.collect(Collectors.toList());

			if (customerLeIds != null && !customerLeIds.isEmpty()) {
				List<String> sfdcList = populateSfdcCustomerIds(customerLeIds);
				if (sfdcList != null && !sfdcList.isEmpty()) {
					leIDS = sfdcList.stream().map(x -> String.valueOf(x)).collect(Collectors.joining(","));
				}
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.ERROR_FETCHING_CUSTOMER_LEIDS, ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return leIDS;
	}

	/**
	 * @param leIds - List of LEIDs
	 * @return list of LeIDs from sfdc
	 * @throws TclCommonException
	 */
	private List<String> populateSfdcCustomerIds(List<String> leIds) throws TclCommonException {
		if (leIds == null || leIds.isEmpty())
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LE_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		String customerLeIdsCommaSeparated = String.join(",", leIds);
		String response = (String) mqUtils.sendAndReceive(customerLeIdsQueue, customerLeIdsCommaSeparated);
		CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils.convertJsonToObject(response,
				CustomerLegalEntityDetailsBean.class);
		leIds.clear();
		if (null != cLeBean) {
			cLeBean.getCustomerLeDetails().stream().forEach(customerLeBean -> leIds.add(customerLeBean.getSfdcId()));
		}
		return leIds;
	}
	
	
	/**
	 * @author KRUTHIKA S
	 * @param valueFor
	 * @return restResponse Object
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public RestResponse getNumberForIssueType(
			String valueFor)throws TclCommonException {
		RestResponse response = null;
		try {
			Map<String, String> paramMap = new HashMap<>();

			String wClause="account.u_cuidIN";
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				wClause = wClause + this.getCustomerCUIDForPartnerLe();
			} else {
				wClause = wClause + this.getCuid();
			}
			paramMap.put("sysparm_query", wClause);
			paramMap.put("sysparm_count", "true");
			paramMap.put("sysparm_group_by", valueFor);
			paramMap.put("sysparm_display_value", "all");
			response = service.getWithQueryParam(snow + baseurl + "/" + incident_request, paramMap,
					getBasicAuth(username, password, getHeader()));
			LOGGER.debug(response.toString());

		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	

	/**
	 * @author KRUTHIKA S
	 * @param valueFor
	 * @return restResponse Object
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public RestResponse getMapValueForRequestType(
			String valueFor)throws TclCommonException {
		RestResponse response = null;
		try {
			Map<String, String> paramMap = new HashMap<>();
			String wClause = "";
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				wClause = "company.ref_customer_account.u_cuidIN" + this.getCustomerCUIDForPartnerLe();
			} else{
				wClause = "company.ref_customer_account.u_cuidIN" + this.getCuid();
			}
			paramMap.put("sysparm_query", wClause);
			paramMap.put("sysparm_count", "true");
			paramMap.put("sysparm_group_by", valueFor);
			paramMap.put("sysparm_display_value", "all");
			response = service.getWithQueryParam(snow + baseurl + "/" + change_request, paramMap,
					getBasicAuth(username, password, getHeader()));
			LOGGER.debug(response.toString());

		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	public static void validateFromToDates(String fromDate,String toDate) throws java.text.ParseException, TclCommonException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDate fromDateLocal = LocalDate.parse(fromDate, formatter);
		LocalDate toDateLocal = LocalDate.parse(toDate, formatter);
		Period diff = Period.between(fromDateLocal, toDateLocal);
		if (diff.getYears() > 1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * Get Partner Cuid
	 *
	 * @return
	 */
	private String getPartnerCuid() {
		Integer partnerId = userInfoUtils.getUserInformation().getPartners().stream().findFirst().get().getPartnerId();
		List<PartnerLegalEntityBean> partnerLegalEntityBeans = new ArrayList<>();
		try {
			String partnerResponse = (String) mqUtils.sendAndReceive(getPartnerLegalEntityDetails, String.valueOf(partnerId));
			partnerLegalEntityBeans = Utils.fromJson(partnerResponse, new TypeReference<List<PartnerLegalEntityBean>>() {
			});
		} catch (Exception e) {
			LOGGER.warn("Exception Occured :: {}", e);
		}
		return partnerLegalEntityBeans.stream().map(PartnerLegalEntityBean::getTpsSfdcCuid).collect(Collectors.joining(","));
	}
	private String getCustomerCUIDForPartnerLe(){
		LOGGER.info("Before Partenr Id for SNOW----------");
		Integer partnerId = userInfoUtils.getUserInformation().getPartners().stream().findFirst().get().getErfPartnerId();
        LOGGER.info("Partenr Id for SNOW---------- {}", partnerId);
        List<String> cUIDs = null;
		try {
			String partnerResponse = (String) mqUtils.sendAndReceive(getCustomerCuidByPartnerId, String.valueOf(partnerId));
			cUIDs = Utils.fromJson(partnerResponse, new TypeReference<List<String>>() {
			});
		} catch (Exception e) {
			LOGGER.warn("Exception Occured :: {}", e);
		}
		return cUIDs.stream().collect(Collectors.joining(","));
	}

}
