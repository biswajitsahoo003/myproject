package com.tcl.dias.performance.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.tcl.dias.beans.ConferenceRoomUsageBean;
import com.tcl.dias.beans.ConferenceUsageReponse;
import com.tcl.dias.beans.TelchemyQOSSummaryBean;
import com.tcl.dias.beans.conferenceRoomUsageReport.ConferenceRoomUsage;
import com.tcl.dias.beans.conferenceRoomUsageReport.UsageRoomData;
import com.tcl.dias.beans.conferenceUsageReport.ConferenceUsage;
import com.tcl.dias.beans.conferenceUsageReport.TelchemyQOSSummaryResponse;
import com.tcl.dias.beans.conferenceUsageReport.UsageData;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.performance.constants.ExceptionConstants;
import com.tcl.dias.performance.constants.PerformanceConstants;
import com.tcl.dias.performance.constants.SQLQueryConstants;
import com.tcl.dias.performance.request.ReportGeneratorRequest;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the PerformanceReportService.java class. This class
 * contains all the service methods related to Performance report
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class PerformanceReportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceReportService.class);
	
	@Qualifier(value = "reportTemplate")
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeIdsQueue;
	
	@Value("${conference.usage.summary.api}")
	String conferencUsageApiUrl;
	
	@Value("${conference.api.username}")
	String conferencUsageApiUsername;
	
	@Value("${conference.api.password}")
	String conferencUsageApiPwd;
	@Value("${conference.room.usage.summary.api}")
	String conferenceRoomUsageApi;
	
	@Value("${telchemy.qos.status.summary.api}")
	String telchemyQosStatusSummaryApi;
	
	@Autowired
	RestClientService restClientService;
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getCustomerSfdcLeIds this method
	 *            is used to get sfdcLeIds for mapped customerLeIds 
	 * @param List<String>
	 * @return List<String>
	 * @throws TclCommonException
	 */
	private List<String> getCustomerSfdcLeIds(List<String> customerLeIds) throws TclCommonException, IllegalArgumentException{
		if(customerLeIds==null || customerLeIds.isEmpty())
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LE_ERROR, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR); 
		String customerLeIdsCommaSeparated = String.join(",", customerLeIds);
		String response = (String) mqUtils.sendAndReceive(customerLeIdsQueue, customerLeIdsCommaSeparated);
		CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
				.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
		customerLeIds.clear();
		if (null != cLeBean) {
			cLeBean.getCustomerLeDetails().stream().forEach(customerLeBean -> {
				customerLeIds.add(customerLeBean.getSfdcId());
			});
		} 
		return customerLeIds;
	}
	private void validateReportGeneratorRequest(ReportGeneratorRequest reportGeneratorRequest) throws TclCommonException {
		if ((reportGeneratorRequest == null) || reportGeneratorRequest.getStartDate() == null || reportGeneratorRequest.getEndDate() == null
				|| reportGeneratorRequest.getGranularity() == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);

		}
	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getPerformanceReportDetails this method
	 *            is used to get Performance report details 
	 * @param ReportGeneratorRequest
	 * @return List<Map<String, Object>>
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getPerformanceReportDetails(ReportGeneratorRequest request) throws TclCommonException {

		StringBuffer sqlQuery = null;
		List<Map<String, Object>> result = null;
		Map<String, Object> queryParam = new HashMap<>();
		List<String> customerLeId = new ArrayList<>();
        validateReportGeneratorRequest(request);
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("agg_start_time", request.getStartDate());
			queryParam.put("agg_end_time", request.getEndDate());
			queryParam.put("orig_cuid", customerLeId);
			queryParam.put("dest_cuid", customerLeId);
			switch (request.getGranularity()) {
			/**Value 5 is for 5 minutes report*/
			case "Minute Slicing":
				sqlQuery = new StringBuffer(SQLQueryConstants.PERFORMANCE_RPT_QUERY_5MIN);
				break;
			/**for Hourly report*/	
			case "Hour Slicing":
				sqlQuery = new StringBuffer(SQLQueryConstants.PERFORMANCE_RPT_QUERY_HOUR);
				break;
			/**for one day report*/	
			default:
				sqlQuery = new StringBuffer(SQLQueryConstants.PERFORMANCE_RPT_QUERY_DAY);
			}

			if (request.getSourceCountry() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getSourceCountry())) {
				queryParam.put("orig_country", request.getSourceCountry());
				sqlQuery = sqlQuery.append(" and orig_country=:orig_country");
			}
			if (request.getDestinationCountry() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getDestinationCountry())) {
				queryParam.put("dst_country", request.getDestinationCountry());
				sqlQuery = sqlQuery.append(" and dst_country=:dst_country");
			}
			if (request.getSourceLocation() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getSourceLocation())) {
				queryParam.put("orig_location", request.getSourceCountry());
				sqlQuery = sqlQuery.append(" and orig_location=:orig_location");
			}
			if (request.getDestinationLocation() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getDestinationLocation())) {
				queryParam.put("dst_location", request.getDestinationLocation());
				sqlQuery = sqlQuery.append(" and dst_location=:dst_location");
			}
			result=namedParameterJdbcTemplate.queryForList(sqlQuery.toString(), queryParam);
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_PERFORMANCE_REPORT, ex, ResponseResource.R_CODE_ERROR);
		}

		return result;

	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getUsageReportDetails this method
	 *            is used to get usage report details 
	 * @param ReportGeneratorRequest
	 * @return List<Map<String, Object>>
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getUsageReportDetails (ReportGeneratorRequest request) throws TclCommonException {

		StringBuffer sqlQuery = null;
		List<Map<String, Object>> result = null;
		Map<String, Object> queryParam = new HashMap<>();
		List<String> customerLeId = new ArrayList<>();
		validateReportGeneratorRequest(request);
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("agg_start_time", request.getStartDate());
			queryParam.put("agg_end_time", request.getEndDate());
			queryParam.put("orig_cuid", customerLeId);
			queryParam.put("dest_cuid", customerLeId);
			switch (request.getGranularity()) {
			/**Value 5 is for 5 minutes report*/
			case "Minute Slicing":
				sqlQuery = new StringBuffer(SQLQueryConstants.USAGE_RPT_QUERY_5MIN);
				break;
			/**for Hourly report*/	
			case "Hour Slicing":
				sqlQuery = new StringBuffer(SQLQueryConstants.USAGE_RPT_QUERY_HOUR);
				break;
			/**for one day report*/	
			default:
				sqlQuery = new StringBuffer(SQLQueryConstants.USAGE_RPT_QUERY_DAY);
			}

			if (request.getSourceCountry() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getSourceCountry())) {
				queryParam.put("orig_country", request.getSourceCountry());
				sqlQuery = sqlQuery.append(" and orig_country=:orig_country");
			}
			if (request.getDestinationCountry() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getDestinationCountry())) {
				queryParam.put("dst_country", request.getDestinationCountry());
				sqlQuery = sqlQuery.append(" and dst_country=:dst_country");
			}
			if (request.getSourceLocation() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getSourceLocation())) {
				queryParam.put("orig_location", request.getSourceCountry());
				sqlQuery = sqlQuery.append(" and orig_location=:orig_location");
			}
			if (request.getDestinationLocation() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getDestinationLocation())) {
				queryParam.put("dst_location", request.getDestinationLocation());
				sqlQuery = sqlQuery.append(" and dst_location=:dst_location");
			}
			result=namedParameterJdbcTemplate.queryForList(sqlQuery.toString(), queryParam);
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_USAGE_REPORT, ex, ResponseResource.R_CODE_ERROR);
		}

		return result;

	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getConcurentAndBandwidthReportDetails this method
	 *            is used to get Concurrent and Bandwidth report details 
	 * @param ReportGeneratorRequest
	 * @return List<Map<String, Object>>
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getConcurentAndBandwidthReportDetails(ReportGeneratorRequest request) throws TclCommonException {

		StringBuffer sqlQuery = null;
		List<Map<String, Object>> result = null;
		Map<String, Object> queryParam = new HashMap<>();
		List<String> customerLeId = new ArrayList<>();
		validateReportGeneratorRequest(request);
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("call_start_time", request.getStartDate());
			queryParam.put("call_end_time", request.getEndDate());
			queryParam.put("cuid", customerLeId);
			switch (request.getGranularity()) {
			/**Value 5 is for 5 minutes report*/
			case "Minute Slicing":
				sqlQuery = new StringBuffer(SQLQueryConstants.CONCURRENT_BANDWIDTH_RPT_QUERY_5MIN);
				break;
			/**for Hourly report*/	
			case "Hour Slicing":
				sqlQuery = new StringBuffer(SQLQueryConstants.CONCURRENT_BANDWIDTH_RPT_QUERY_HOUR);
				break;
			/**for one day report*/	
			default:
				sqlQuery = new StringBuffer(SQLQueryConstants.CONCURRENT_BANDWIDTH_RPT_QUERY_DAY);
			}

			if (request.getSourceLocation() != null && !CommonConstants.ALL.equalsIgnoreCase(request.getSourceLocation())) {
				queryParam.put("location", request.getSourceCountry());
				sqlQuery = sqlQuery.append(" and location=:location");
			}
			
			result=namedParameterJdbcTemplate.queryForList(sqlQuery.toString(), queryParam);
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_CONCURRENT_REPORT, ex, ResponseResource.R_CODE_ERROR);
		}

		return result;

	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getCountryListForPerformanceAndUsageReport this method
	 *            is used to get country list 
	 * @param ReportGeneratorRequest
	 * @return List<Map<String, Object>>
	 * @throws TclCommonException
	 */
	public List<String> getSourceCountryList(String granularity) throws TclCommonException{
		List<String> customerLeId = new ArrayList<>();
		Map<String, Object> queryParam = new HashMap<>();
		List<Map<String, Object>> result = null;
		List<String> sourceCountryList=new ArrayList<>();
		String sqlQuery=null;
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("orig_cuid", customerLeId);			
			switch (granularity) {
			case "Minute Slicing":
				sqlQuery = SQLQueryConstants.SOURCE_COUNTRY_LIST_QUERY_5MIN;
				break;
			case "Hour Slicing":
				sqlQuery = SQLQueryConstants.SOURCE_COUNTRY_LIST_QUERY_HOUR;
				break;
			default:
				sqlQuery = SQLQueryConstants.SOURCE_COUNTRY_LIST_QUERY_DAY;
			}
			result=namedParameterJdbcTemplate.queryForList(sqlQuery, queryParam);
			result.stream().forEach(location->{
				if(location.get("origCounrtry")!=null && !location.get("origCounrtry").toString().isEmpty())
				sourceCountryList.add((String) location.get("origCounrtry"));
			});
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_COUNTRY_LIST, ex, ResponseResource.R_CODE_ERROR);
		}
		return sourceCountryList;
		
	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getSourceLocationList this method
	 *            is used to get source location list 
	 * @param ReportGeneratorRequest
	 * @return List<String>
	 * @throws TclCommonException
	 */
	public List<String> getSourceLocationList(String granularity,String countryName) throws TclCommonException{
		List<String> customerLeId = new ArrayList<>();
		List<String> sourceLocationList = new ArrayList<>();
		Map<String, Object> queryParam = new HashMap<>();
		List<Map<String, Object>> result = null;
		String sqlQuery=null;
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("orig_country", countryName);
			queryParam.put("orig_cuid", customerLeId);
			
			switch (granularity) {
			case "Minute Slicing":
				sqlQuery = SQLQueryConstants.ORIG_LOCATION_LIST_QUERY_5MIN;
				break;
			case "Hour Slicing":
				sqlQuery = SQLQueryConstants.ORIG_LOCATION_LIST_QUERY_HOUR;
				break;
			default:
				sqlQuery = SQLQueryConstants.ORIG_LOCATION_LIST_QUERY_DAY;
			}
			result=namedParameterJdbcTemplate.queryForList(sqlQuery, queryParam);
			result.stream().forEach(location->{
				if(location.get("orig_location")!=null && !location.get("orig_location").toString().isEmpty())
				sourceLocationList.add((String) location.get("orig_location"));
			});
			
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SOURCE_LOCATION_LIST, ex, ResponseResource.R_CODE_ERROR);
		}
		return sourceLocationList;
		
	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getDestinationLocationList this method
	 *            is used to get destination location list 
	 * @param ReportGeneratorRequest
	 * @return List<String>
	 * @throws TclCommonException
	 */
	public List<String> getDestinationLocationList(String granularity,String countryName) throws TclCommonException{
		List<String> customerLeId = new ArrayList<>();
		List<String> dstLocationList = new ArrayList<>();
		Map<String, Object> queryParam = new HashMap<>();
		List<Map<String, Object>> result = null;
		String sqlQuery=null;
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("dst_country", countryName);
			queryParam.put("dest_cuid", customerLeId);
			
			switch (granularity) {
			case "Minute Slicing":
				sqlQuery = SQLQueryConstants.DST_LOCATION_LIST_QUERY_5MIN;
				break;
			case "Hour Slicing":
				sqlQuery = SQLQueryConstants.DST_LOCATION_LIST_QUERY_HOUR;
				break;
			default:
				sqlQuery = SQLQueryConstants.DST_LOCATION_LIST_QUERY_DAY;
			}
			result=namedParameterJdbcTemplate.queryForList(sqlQuery, queryParam);
			result.stream().forEach(location->{
				if(location.get("dst_location")!=null && !location.get("dst_location").toString().isEmpty())
					dstLocationList.add((String) location.get("dst_location"));
			});
			
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_DST_LOCATION_LIST, ex, ResponseResource.R_CODE_ERROR);
		}
		return dstLocationList;
		
	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getLocationListForConcurrentReport this method
	 *            is used to get concurrent report location 
	 * @param ReportGeneratorRequest
	 * @return List<String>
	 * @throws TclCommonException
	 */
	public List<String> getLocationListForConcurrentReport(String granularity) throws TclCommonException{
		List<String> customerLeId = new ArrayList<>();
		Map<String, Object> queryParam = new HashMap<>();
		List<Map<String, Object>> result = null;
		List<String> locationList = new ArrayList<>();
		String sqlQuery=null;
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("cuid", customerLeId);
			
			switch (granularity) {
			case "Minute Slicing":
				sqlQuery = SQLQueryConstants.CONCRRNT_LOCATION_LIST_QUERY_5MIN;
				break;
			case "Hour Slicing":
				sqlQuery = SQLQueryConstants.CONCRRNT_LOCATION_LIST_QUERY_HOUR;
				break;
			default:
				sqlQuery = SQLQueryConstants.CONCRRNT_LOCATION_LIST_QUERY_DAY;
			}
			result=namedParameterJdbcTemplate.queryForList(sqlQuery, queryParam);
			result.stream().forEach(location->{
				if(location.get("location")!=null && !location.get("location").toString().isEmpty())
					locationList.add((String) location.get("location"));
			});
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_CONCURRENT_LOCATION_LIST, ex, ResponseResource.R_CODE_ERROR);
		}
		return locationList;
		
	}
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getCountryListForPerformanceAndUsageReport this method
	 *            is used to get country list 
	 * @param ReportGeneratorRequest
	 * @return List<Map<String, Object>>
	 * @throws TclCommonException
	 */
	public List<String> getDestinationCountryList(String granularity) throws TclCommonException{
		List<String> customerLeId = new ArrayList<>();
		Map<String, Object> queryParam = new HashMap<>();
		List<Map<String, Object>> result = null;
		List<String> dstCountryList=new ArrayList<>();
		String sqlQuery=null;
		try {
			UserInformation userInformation = userInfoUtils.getUserInformation(Utils.getSource());
			userInformation.getCustomers().stream().forEach(customer -> {
				customerLeId.add(customer.getCustomerLeId().toString());
			});
			getCustomerSfdcLeIds(customerLeId);
			queryParam.put("dest_cuid", customerLeId);
			
			switch (granularity) {
			case "Minute Slicing":
				sqlQuery = SQLQueryConstants.DST_COUNTRY_LIST_QUERY_5MIN;
				break;
			case "Hour Slicing":
				sqlQuery = SQLQueryConstants.DST_COUNTRY_LIST_QUERY_HOUR;
				break;
			default:
				sqlQuery = SQLQueryConstants.DST_COUNTRY_LIST_QUERY_DAY;
			}
			result=namedParameterJdbcTemplate.queryForList(sqlQuery, queryParam);
			result.stream().forEach(location->{
				if(location.get("dstCountry")!=null && !location.get("dstCountry").toString().isEmpty())
					dstCountryList.add((String) location.get("dstCountry"));
			});
		}catch(Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_IN_COUNTRY_LIST, ex, ResponseResource.R_CODE_ERROR);
		}
		return dstCountryList;
		
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ To Get Conference Usage Report
	 * @param month
	 * @param year
	 * @param customerName
	 * @return ConferenceUsageReponse
	 * @throws TclCommonException
	 */
	public ConferenceUsageReponse toGetConferenceUsageReport(String month, String year, String customerName)
			throws TclCommonException {
		ConferenceUsageReponse conferenceUsageReponse = new ConferenceUsageReponse();
		String url;
		/**
		 * Since the data is not available and 
		 * as part of testing hardcoding the below customer name..
		 */
//		if(Objects.nonNull(customerName) && customerName.equalsIgnoreCase("Symrise AG")) {
//			customerName="Life Insurance Corporation";
//		}
		if (Objects.nonNull(month) && Objects.nonNull(year) && Objects.nonNull(customerName))
			url = conferencUsageApiUrl.concat("?month=" + month + "&customerName=" + customerName + "&year=" + year);
		else
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		LOGGER.info("In toGetConferenceUsageReport url : {}", url);
			RestResponse response = restClientService.getWithBasicAuthentication(url, new HashMap<String, String>(),
					conferencUsageApiUsername, conferencUsageApiPwd);
			if (Objects.nonNull(response) && response.getStatus() == Status.SUCCESS
					&& Objects.nonNull(response.getData())) {
				ConferenceUsage conferenceUsage = Utils.convertJsonToObject(response.getData(), ConferenceUsage.class);
				if (Objects.nonNull(conferenceUsage) && !conferenceUsage.getData().isEmpty())
					conferenceUsageReponse = createRespStrtFrReport(conferenceUsage.getData().get(0));
			}
		return conferenceUsageReponse;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ To frame response structure
	 * @param usageData
	 * @return ConferenceUsageReponse
	 */
	public ConferenceUsageReponse createRespStrtFrReport(UsageData usageData) {
		ConferenceUsageReponse conferenceUsageReponse = new ConferenceUsageReponse();
		conferenceUsageReponse.setTotalParticipants(checkNullAndConvertToString(usageData.getParticipants()));
		conferenceUsageReponse.setMonthlyAverage(checkNullAndConvertToString(usageData.getAvgParticipants()));
		conferenceUsageReponse
				.setTotalParticipantsMinutes(checkNullAndConvertToString(usageData.getParticipantMinutes()));
		conferenceUsageReponse
				.setMonthlyAverageMinutes(checkNullAndConvertToString(usageData.getAvgParticpantMinutes()));
		conferenceUsageReponse.setTotalConferences(checkNullAndConvertToString(usageData.getConferenceCount()));
		conferenceUsageReponse.setTotalConferenceMinutes(checkNullAndConvertToString(usageData.getConferenceMinutes()));
		conferenceUsageReponse
				.setMonthlyAverageConfMinutes(checkNullAndConvertToString(usageData.getConferenceDurationAvg()));
		conferenceUsageReponse.setUsageData(usageData);
		return conferenceUsageReponse;
	}

	public String checkNullAndConvertToString(Object value) {
		return value == null ? " " : value.toString();
	}
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/ To Get Conference Room Usage Report
	 * @param month
	 * @param year
	 * @param customerName
	 * @return ConferenceRoomUsageBean
	 * @throws TclCommonException
	 */
	public List<ConferenceRoomUsageBean> toGetConferenceRoomUsageReport(String month, String year, String customerName)
			throws TclCommonException {
		List<ConferenceRoomUsageBean> conferenceRoomUsageBean = new ArrayList<>();
		String url;
//		/**
//		 * Since the data is not available and 
//		 * as part of testing hardcoding the below customer name..
//		 */
//		if(Objects.nonNull(customerName) && customerName.equalsIgnoreCase("Symrise AG")) {
//			customerName="Life Insurance Corporation";
//		}
		if (Objects.nonNull(month) && Objects.nonNull(year) && Objects.nonNull(customerName))
			url = conferenceRoomUsageApi.concat("?month=" + month + "&customerName=" + customerName + "&year=" + year);
		else
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		LOGGER.info("In toGetConferenceRoomUsageReport url : {}", url);
			RestResponse response = restClientService.getWithBasicAuthentication(url, new HashMap<String, String>(),
					conferencUsageApiUsername, conferencUsageApiPwd);
			if (Objects.nonNull(response) && response.getStatus() == Status.SUCCESS
					&& Objects.nonNull(response.getData())) {
				ConferenceRoomUsage conferenceRmUsage = Utils.convertJsonToObject(response.getData(),
						ConferenceRoomUsage.class);
				if (Objects.nonNull(conferenceRmUsage) && !conferenceRmUsage.getData().isEmpty())
					conferenceRoomUsageBean = createRespStrtFrUsageReport(conferenceRmUsage.getData());
			}
		return conferenceRoomUsageBean;
	}
	/**
	 * to get Telchemy QOS Status summary Response
	 * @param month
	 * @param year
	 * @param cuid
	 * @return
	 * @throws TclCommonException
	 */
	public TelchemyQOSSummaryResponse getTelchemyQosStatusSummary(String month, String year, String cuid)
			throws TclCommonException {
		TelchemyQOSSummaryResponse telchemyQosSummary = new TelchemyQOSSummaryResponse();
		String url;

		if (Objects.nonNull(month) && Objects.nonNull(year) && Objects.nonNull(cuid))
			url = telchemyQosStatusSummaryApi.concat("?month=" + month + "&cuid=" + cuid + "&year=" + year);
		else
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		LOGGER.info("In getTelchemyQosStatusSummary url : {}", url);
		RestResponse response = restClientService.getWithBasicAuthentication(url, new HashMap<String, String>(),
				conferencUsageApiUsername, conferencUsageApiPwd);
		LOGGER.info("In getTelchemyQosStatusSummary response{}",response);
		if (Objects.nonNull(response) && response.getStatus() == Status.SUCCESS
				&& Objects.nonNull(response.getData())) {
			telchemyQosSummary = Utils.convertJsonToObject(response.getData(), TelchemyQOSSummaryResponse.class);
		}
		LOGGER.info("telchemyQosSummary reponse {}",telchemyQosSummary);
		return telchemyQosSummary;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/ To frame response structure
	 * @param usageData
	 * @return ConferenceUsageReponse
	 */
	public List<ConferenceRoomUsageBean> createRespStrtFrUsageReport(List<UsageRoomData> conferenceRoomUsageList) {
		List<ConferenceRoomUsageBean> usageList = new ArrayList<>();
		conferenceRoomUsageList.stream().forEach(bean -> {
			ConferenceRoomUsageBean conferenceRoomUsageBean = new ConferenceRoomUsageBean();
			conferenceRoomUsageBean.setCustomerName(checkNullAndConvertToString(bean.getCustomerName()));
			conferenceRoomUsageBean.setMonth(checkNullAndConvertToString(bean.getMonth()));
			conferenceRoomUsageBean.setYear(checkNullAndConvertToString(bean.getYear()));
			conferenceRoomUsageBean.setConferenceCount(checkNullAndConvertToString(bean.getConferenceCount()));
			conferenceRoomUsageBean
					.setConferenceDurationAvg(checkNullAndConvertToString(bean.getConferenceDurationAvg()));
			conferenceRoomUsageBean.setConferenceMinutes(checkNullAndConvertToString(bean.getConferenceMinutes()));
			conferenceRoomUsageBean.setRoomName(checkNullAndConvertToString(bean.getRoomName()));
			conferenceRoomUsageBean.setMonthShortName(checkNullAndConvertToString(bean.getMonthShortName()));
			conferenceRoomUsageBean.setCreatedAt(checkNullAndConvertToString(bean.getCreatedAt()));
			conferenceRoomUsageBean.setUpdatedAt(checkNullAndConvertToString(bean.getUpdatedAt()));
			usageList.add(conferenceRoomUsageBean);
		});
		return usageList;
	}
}
