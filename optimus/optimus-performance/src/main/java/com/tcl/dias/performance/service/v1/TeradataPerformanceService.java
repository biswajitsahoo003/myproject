package com.tcl.dias.performance.service.v1;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tcl.dias.beans.CircuitDetails;
import com.tcl.dias.beans.CircuitInfoBean;
import com.tcl.dias.beans.CircuitUptimeResponseBean;
import com.tcl.dias.beans.FaultRate;
import com.tcl.dias.beans.FaultRateBean;
import com.tcl.dias.beans.ImpactWithCount;
import com.tcl.dias.beans.MTTRSeverityResponseBean;
import com.tcl.dias.beans.MTTRTrendInfo;
import com.tcl.dias.beans.MTTRTrendRFOWise;
import com.tcl.dias.beans.MTTRTrendResponseBean;
import com.tcl.dias.beans.RFOServiceDetailsResponseBean;
import com.tcl.dias.beans.SLABreachBean;
import com.tcl.dias.beans.ServerityTicketResponse;
import com.tcl.dias.beans.ServiceInventoryBean;
import com.tcl.dias.beans.ServiceInventoryDonutBean;
import com.tcl.dias.beans.SeverityTicketDetailsResponseBean;
import com.tcl.dias.beans.TicketByMTTRDuration;
import com.tcl.dias.beans.TicketExcelBean;
import com.tcl.dias.beans.TicketTrend;
import com.tcl.dias.beans.TrendInfoBean;
import com.tcl.dias.beans.TrendResponseBean;
import com.tcl.dias.beans.UptimeCircuitDetailsResponseBean;
import com.tcl.dias.beans.UptimeExcelBean;
import com.tcl.dias.beans.UptimeInfoBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.performance.constants.ExceptionConstants;
import com.tcl.dias.performance.constants.PerformanceConstants;
import com.tcl.dias.performance.service.v1.teradata.queries.TeradataReportsQuery;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the Component details for PerformanceService.
 * 
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class TeradataPerformanceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TeradataPerformanceService.class);

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeIdsQueue;

	@Qualifier(value = "teradataTemplate")
	@Autowired
	private JdbcTemplate teradataJdbcTemplate;

	@Qualifier(value = "optimusTemplate")
	@Autowired
	private JdbcTemplate optimusTemplate;

	@Autowired
	TeradataReportsQuery reports;
	
	@Value("${rabbitmq.user.products.queue}")
	String inventoryConsumerQueue;
	
	private DecimalFormat df = new DecimalFormat(PerformanceConstants.PERCENTAGE_FORMAT);

	
	/**
	 * To get the reason for outage specification with the ticket count for a given month.
	 * 
	 * @param month - The month as 12-2018 format required.
	 * @return {@Map<String, Object>}, Reason with Count.
	 * @throws TclCommonException 
	 */
	public Map<String, Object> getMonthlyOutageReport(String monthYear) throws TclCommonException {
		Map<String, Object> outageReasonWithCount = new LinkedHashMap<>();
		if(StringUtils.isEmpty(monthYear)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		try {
			LOGGER.info("getttingLegalEntityIds :::");
			List<String> customerLeIds = getLegalEntityIds();
			LOGGER.info(" Before populateSfdcCustomerIds {}",customerLeIds);
			populateSfdcCustomerIds(customerLeIds);
			if(customerLeIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
			}

			outageReasonWithCount.put(PerformanceConstants.KEY_TOTAL, 0);
			outageReasonWithCount.put(PerformanceConstants.KEY_TOP, 0);
			outageReasonWithCount.put(PerformanceConstants.KEY_OTHER_REASONS, "");
			validateSqlInjectionInput(monthYear); //sql injection
			LOGGER.info("querying ForOutageReasons ::");
			List<Map<String, Object>> reasonWithCount = reports.queryForOutageReasons(customerLeIds, monthYear);
			reasonWithCount.stream().forEach(map -> {
				String outageReason = (String) map.get(PerformanceConstants.KEY_RFO_SPEC);
				Integer ticketCount = (Integer) map.get(PerformanceConstants.KEY_TICKET_COUNT);

				// calculate total count of tickets
				int count = (int) outageReasonWithCount.get(PerformanceConstants.KEY_TOTAL);
				count = count+ticketCount;
				if(outageReasonWithCount.size() <=10) {
					outageReasonWithCount.put(outageReason, ticketCount);
					outageReasonWithCount.put(PerformanceConstants.KEY_TOP, count);		
				}
				else {
					// For RFO other than Top 8, put the keys appended in a string and store it for searching purposes.
					String otherReasons = (String) outageReasonWithCount.get(PerformanceConstants.KEY_OTHER_REASONS);
					otherReasons = (otherReasons.isEmpty()) ? otherReasons.concat(outageReason) : otherReasons.concat(","+outageReason);
					outageReasonWithCount.put(PerformanceConstants.KEY_OTHER_REASONS, otherReasons);
				}
				outageReasonWithCount.put(PerformanceConstants.KEY_TOTAL, count);
			});

			int othersCount = (int) outageReasonWithCount.get(PerformanceConstants.KEY_TOTAL) 
					- (int) outageReasonWithCount.get(PerformanceConstants.KEY_TOP);
			outageReasonWithCount.put(PerformanceConstants.KEY_OTHERS, othersCount);
			
			outageReasonWithCount.remove(PerformanceConstants.KEY_TOP);
			LOGGER.info("outageReasonWithCount:: {}",outageReasonWithCount);
			return outageReasonWithCount;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_RFO_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_RFO_REPORTS, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	/**
	 * To get the circuit details for the given outage reason, for a given month.
	 * 
	 * @param monthYear - The month as 12-2018 format required.
	 * @param outageSpecification, The outage reason.
	 * @return {@Map<String, Object>}, Reason with Count.
	 * @throws TclCommonException 
	 */
	public List<RFOServiceDetailsResponseBean> getOutageCircuitDetails(String monthYear, String outageSpecification) throws TclCommonException {
		if(StringUtils.isEmpty(monthYear) || StringUtils.isEmpty(outageSpecification)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		try {
			List<String> customerLeIds = getLegalEntityIds();
			populateSfdcCustomerIds(customerLeIds);
			if(customerLeIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
			}
			
			outageSpecification = Stream.of(outageSpecification.split(","))
					.map(s -> "'".concat(s).concat("'")).collect(Collectors.joining(","));
			List<Map<String, Object>> queryResponse = reports.queryForOutageCircuitDetails(customerLeIds, monthYear, outageSpecification);
			return queryResponse.stream().map(row -> {
				RFOServiceDetailsResponseBean bean = new RFOServiceDetailsResponseBean();
				bean.setServiceId((String) row.get(PerformanceConstants.KEY_SERVICEID));
				bean.setServiceType((String) row.get(PerformanceConstants.KEY_SERVICE_TYPE));
				bean.setTicketNo((String) row.get(PerformanceConstants.KEY_TICKET_NUM));
				bean.setRfoSpecification((String) row.get(PerformanceConstants.KEY_RFO_SPECIFICATION));
				bean.setImpact((String) row.get(PerformanceConstants.KEY_IMPACT));
				bean.setCategoryDetail((String) row.get(PerformanceConstants.KEY_CATEGORY_DETAIL));
				bean.setRfoResponsible((String) row.get(PerformanceConstants.KEY_RFO_RESPONSIBLE));
				bean.setSiteAddress((String) row.get(PerformanceConstants.KEY_SITE_ADDRESS));
				bean.setCktAlias((row.get(PerformanceConstants.KEY_CIRCUIT_ALIAS) != null) ? (String) row.get(PerformanceConstants.KEY_CIRCUIT_ALIAS): "");
				return bean;
			}).collect(Collectors.toList());
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_RFO_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_RFO_REPORTS, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	/**
	 * To get the severity tickets for a given month product wise, categorized based on Impact.
	 * 
	 * @param month - The month as 12-2018 format required.
	 * @return {@ServerityTicketResponse}
	 * @throws TclCommonException
	 */
	public ServerityTicketResponse getMonthlySeverityTickets(String monthWithYear,List<String> productsForUser) throws TclCommonException {
		LOGGER.info("Inside getMonthlySeverityTickets");
		if(StringUtils.isEmpty(monthWithYear)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		ServerityTicketResponse response;
		try {
			LOGGER.info("Inside getMonthlySeverityTickets, getting LegalEntityId's");
			List<String> leIds = getLegalEntityIds();
			LOGGER.info("Inside getMonthlySeverityTickets,populateSfdcCustomerIds");
			populateSfdcCustomerIds(leIds);
			if(leIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
			}
			
			List<TicketTrend> trends = new ArrayList<>();
			LOGGER.info("queryForMonthwiseTickets" );
			List<Map<String, Object>> result = reports.queryForMonthwiseTickets(leIds, monthWithYear);
			LOGGER.info("result in queryForMonthwiseTickets :: {}",result );
			result.stream().forEach(row -> populateTrendsForMonth(row, trends, PerformanceConstants.KEY_SERVICETYPE));
			List<String> availableInTeradata = trends.stream().map(TicketTrend :: getProductOrMonth).collect(Collectors.toList());
			
			if(Objects.nonNull(productsForUser)) {
			trends.addAll(productsForUser.stream().filter(pdt -> !availableInTeradata.contains(convertPdtName(pdt)))
					.map(pdt -> {
						TicketTrend ticketTrend = new TicketTrend();
						ticketTrend.setProductOrMonth(pdt);
						return ticketTrend;
					}).collect(Collectors.toList()));
			}
			
			response = new ServerityTicketResponse();
			response.setTrends(trends);
			LOGGER.info("severity response:: {}",response);
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the severity tickets for a given month product wise, categorized based on Impact.
	 * 
	 * @param monthWithYear - The month as 12-2018 format required.
	 * @param product - The service type
	 * @param impact - The impact type
	 * @return {@List<SeverityTicketDetailsResponseBean>}, the details for each ticket.
	 * @throws TclCommonException
	 */
	public List<SeverityTicketDetailsResponseBean> getTicketDetailsBySeverity(String monthWithYear, String product, String impact) throws TclCommonException {
		if(StringUtils.isEmpty(monthWithYear) || StringUtils.isEmpty(product) || StringUtils.isEmpty(impact)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		try {
			List<String> leIds = getLegalEntityIds();
			populateSfdcCustomerIds(leIds);
			if(leIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
			}
			
			List<Map<String, Object>> result = reports.queryTicketDetailsBySeverity(leIds, monthWithYear, product, impact);
			return result.stream().map(row -> {
				SeverityTicketDetailsResponseBean bean = new SeverityTicketDetailsResponseBean();
				bean.setServiceId((String) row.get(PerformanceConstants.KEY_SERVICEID));
				bean.setTicketNo((String) row.get(PerformanceConstants.KEY_TICKET_NUM));
				bean.setServiceType((String) row.get(PerformanceConstants.KEY_SERVICETYPE));
				bean.setImpact((String) row.get(PerformanceConstants.KEY_IMPACT));
				bean.setCategoryDetail((String) row.get(PerformanceConstants.KEY_CATEGORY_DETAIL));
				bean.setRfoResponsible((String) row.get(PerformanceConstants.KEY_RFO_RESPONSIBLE));
				bean.setSiteAddress((String) row.get(PerformanceConstants.KEY_SITE_ADDRESS));
				bean.setCktAlias((row.get(PerformanceConstants.KEY_CIRCUIT_ALIAS) != null) ? (String) row.get(PerformanceConstants.KEY_CIRCUIT_ALIAS): "");
				return bean;
			}).collect(Collectors.toList());
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the severity tickets for 6 months duration, categorized based on Impact.
	 * 
	 * @return {@ServerityTicketResponse}
	 * @throws TclCommonException
	 */
	public ServerityTicketResponse getTicketTrendSeverityWise() throws TclCommonException {
		ServerityTicketResponse response = new ServerityTicketResponse();
		try {
			List<String> leIds = getLegalEntityIds();
			populateSfdcCustomerIds(leIds);
			if(leIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
			}

			List<TicketTrend> trends = new ArrayList<>();
			List<Map<String, Object>> result = reports.queryForTrendSeverityWise(leIds);
			result.stream().forEach(row -> populateTrends(row, trends, PerformanceConstants.KEY_MONTH_WITH_YEAR));
			response.setTrends(trends);
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * To get the severity tickets for a given month product wise, categorized based on Impact.
	 * 
	 * @param monthWithYear - The month as 12-2018 format required.
	 * @param impact - The impact type
	 * @return {@List<SeverityTicketDetailsResponseBean>}, the details for each ticket.
	 * @throws TclCommonException
	 */
	public List<SeverityTicketDetailsResponseBean> getTicketDetailsBySeverity(String monthWithYear, String impact) throws TclCommonException {
		if(StringUtils.isEmpty(monthWithYear) || StringUtils.isEmpty(impact)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		try {
			List<String> leIds = getLegalEntityIds();
			populateSfdcCustomerIds(leIds);
			if(leIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
			}
			
			List<Map<String, Object>> result = reports.queryTicketDetailsBySeverity(leIds, monthWithYear, impact);
			return result.stream().map(row -> {
				SeverityTicketDetailsResponseBean bean = new SeverityTicketDetailsResponseBean();
				bean.setTicketNo((String) row.get(PerformanceConstants.KEY_TICKET_NUM));
				bean.setServiceId((String) row.get(PerformanceConstants.KEY_SERVICEID));
				bean.setServiceType((String) row.get(PerformanceConstants.KEY_SERVICETYPE));
				bean.setImpact((String) row.get(PerformanceConstants.KEY_IMPACT));
				bean.setRfoSpecification((String) row.get(PerformanceConstants.KEY_RFO_SPECIFICATION));
				bean.setRfoResponsible((String) row.get(PerformanceConstants.KEY_RFO_RESPONSIBLE));
				bean.setTicketStatus((String) row.get(PerformanceConstants.KEY_STATUS));
				bean.setTicketOutageInMts((Integer) row.get(PerformanceConstants.KEY_OUTAGE_DURATION_MTS));
				bean.setSiteAddress((String) row.get(PerformanceConstants.KEY_SITE_ADDRESS));
				return bean;
			}).collect(Collectors.toList());
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	/**
	 *  To fetch the Uptime, Faultrate for circuits for the given month - product wise segregated.
	 * 
	 * @param month, Month to be sent in (MM-YY) format 
	 * @return {CircuitUptimeResponseBean}
	 * @throws TclCommonException 
	 */
	@SuppressWarnings("unchecked")
	public CircuitUptimeResponseBean getUptimeReportPerMonth(String month) throws TclCommonException {
		if(StringUtils.isEmpty(month)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		CircuitUptimeResponseBean beanResponse = new CircuitUptimeResponseBean();
		List<UptimeInfoBean> uptimeBean = new ArrayList<>();
		try {
			List<Map<String, Object>> repsonse = reports.queryForUptimeWithFaultrateWithSLA(customerLeIds, month);
			LOGGER.info("Uptimereport per month query result {} ", repsonse);
			List<String> availableInTeradata = new ArrayList<>();
			repsonse.stream().forEach(row -> {
				LOGGER.info("Uptimereport per month key {}  value {} ",row.keySet(), row.values());
				CircuitInfoBean circuitInfo = new CircuitInfoBean();
				String pdt = (String) row.get(PerformanceConstants.KEY_PRODUCT_TYPE);
				
				Double uptime = (Double) row.get(PerformanceConstants.KEY_UPTIME_IN_PERCENTAGE);
				circuitInfo.setUptimePercentage(df.format(uptime));
				circuitInfo.setTotalCircuitCount((Integer) row.get(PerformanceConstants.KEY_TOTAL_COUNT));
				circuitInfo.setSlaBreachCount((row.get(PerformanceConstants.KEY_SLA_BREACH_COUNT) != null) ? (Integer) row.get(PerformanceConstants.KEY_SLA_BREACH_COUNT) : 0);
				circuitInfo.setSlaBreachPercentage((row.get(PerformanceConstants.KEY_SLA_BREACH_PERCENTAGE) != null) ? BigDecimal.valueOf((Double) row.get(PerformanceConstants.KEY_SLA_BREACH_PERCENTAGE)) : BigDecimal.ZERO);
				circuitInfo.setFaultRatePercentage((row.get(PerformanceConstants.KEY_FAULT_RATE) != null)  ? BigDecimal.valueOf((Double) row.get(PerformanceConstants.KEY_FAULT_RATE)) : BigDecimal.ZERO);
				circuitInfo.setFaultRateTotalCount((row.get(PerformanceConstants.KEY_FAULT_RATE_TOTAL_COUNT) != null)  ? (Integer) row.get(PerformanceConstants.KEY_FAULT_RATE_TOTAL_COUNT) : 0);
				circuitInfo.setFaultRateImpactedCount((row.get(PerformanceConstants.KEY_FAULT_RATE_IMPACTED_CIRUIT_COUNT) != null)  ? (Integer) row.get(PerformanceConstants.KEY_FAULT_RATE_IMPACTED_CIRUIT_COUNT) : 0);
			
				UptimeInfoBean info = new UptimeInfoBean();
				info.setProduct(pdt);
				info.setCircuitInfo(circuitInfo);
				uptimeBean.add(info);
				
				availableInTeradata.add(pdt);
			});

			// Compare the products for this user with response from Galaxy. If any pdts is not available in galaxy response, 
			// populate with empty data as per requirement.
			List<String> productsForUser = (List<String>) mqUtils.sendAndReceive(inventoryConsumerQueue, null);
			if(Objects.nonNull(productsForUser)) {
				List<String> removedList = new ArrayList<>();
				productsForUser.stream().forEach(pdt -> {
					if(availableInTeradata.contains(convertPdtName(pdt))) {
						removedList.add(pdt);
					}
				});
				productsForUser.removeAll(removedList);
				productsForUser.stream().forEach(pdt -> {
					CircuitInfoBean circuitInfo = new CircuitInfoBean();
					UptimeInfoBean info = new UptimeInfoBean();
					info.setProduct(pdt);
					info.setCircuitInfo(circuitInfo);
					uptimeBean.add(info);
					
					circuitInfo.setUptimePercentage(df.format(0));
					circuitInfo.setTotalCircuitCount(0);
					circuitInfo.setSlaBreachCount(0);
					circuitInfo.setSlaBreachPercentage(BigDecimal.ZERO);
					circuitInfo.setFaultRatePercentage(BigDecimal.ZERO);
					circuitInfo.setFaultRateTotalCount(0);
					circuitInfo.setFaultRateImpactedCount(0);
				});	
			}
			beanResponse.setMonth(month);
			beanResponse.setUptimeBeans(uptimeBean);
			return beanResponse;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the top most 5 circuits with the least Uptime percentage.
	 * 
	 * @param month, Month for which the uptime data is required
	 * @param product, The product type for which the uptime data is required
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> getCiruitsWithLeastUptime(String month, String product) throws TclCommonException {
		if(StringUtils.isEmpty(month) || StringUtils.isEmpty(product)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		
		try {
			return reports.queryForTop5CircuitsWithLeastUptime(customerLeIds, month, product);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the details of the circuits
	 * @param impact 
	 * @param product 
	 * @param month 
	 * 
	 * @param month, Month for which the uptime data is required
	 * @param product, The product type for which the uptime data is required
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public List<UptimeCircuitDetailsResponseBean> getCiruitsUptimeDetailedInfo(String month, String product) throws TclCommonException {
		if(StringUtils.isEmpty(month) || StringUtils.isEmpty(product)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		
		try {
			List<Map<String, Object>> result = reports.queryForUptimeCircuitDetails(customerLeIds, month, product);
			return result.stream().map(row -> {
				UptimeCircuitDetailsResponseBean bean = new UptimeCircuitDetailsResponseBean();
				bean.setCircuitId((String) row.get(PerformanceConstants.KEY_CIRCUITID));
				bean.setServiceLink((String) row.get(PerformanceConstants.KEY_SERVICE_LINK));
				bean.setSiteAddress((String) row.get(PerformanceConstants.KEY_SITE_ADDRESS));
				bean.setPrimaryCktAlias((row.get(PerformanceConstants.KEY_PRI_CIRCUIT_ALIAS) != null) ? (String) row.get(PerformanceConstants.KEY_PRI_CIRCUIT_ALIAS): "");
				bean.setSecCircuitAlias((row.get(PerformanceConstants.KEY_SEC_CIRCUIT_ALIAS) != null) ? (String) row.get(PerformanceConstants.KEY_SEC_CIRCUIT_ALIAS): "");
				bean.setCommittedSLA((String) row.get(PerformanceConstants.KEY_COMMITTED_UPTIME));
				bean.setUptimePercentage((BigDecimal) row.get(PerformanceConstants.KEY_UPTIME_IN_PERCENTAGE));
				return bean;
			}).collect(Collectors.toList());
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the uptime trend for 6 months - product wise.
	 * 
	 * @param month, Month for which the uptime data is required
	 * @param product, The product type for which the uptime data is required
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public List<TrendResponseBean> getUptimeTrendInfo(List<String> duration) throws TclCommonException {
		LOGGER.info("Inside getUptimeTrendInfo");
		if(Objects.isNull(duration)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		
		List<TrendResponseBean> response = new ArrayList<>();
		try {
			if (duration != null) {
				for (String month : duration) {
					validateSqlInjectionInput(month); // sql injection
				}
			}
			List<Map<String, Object>> queryResponse = reports.queryForUptimeTrend(customerLeIds, duration);
			queryResponse.stream().forEach(row -> {				
				TrendResponseBean trendBean = null;
				List<TrendInfoBean> monthWithUptime = null;
				TrendInfoBean trendInfo = new TrendInfoBean();

				String product = (String) row.get(PerformanceConstants.KEY_PRODUCT_TYPE);
				
				trendInfo.setMonth(convertDateFormat((String) row.get(PerformanceConstants.KEY_MONTH_WITH_YEAR)));
				
				trendInfo.setUptimePercentage(df.format((Double) row.get(PerformanceConstants.KEY_UPTIME_IN_PERCENTAGE)));
				
				Optional<TrendResponseBean> pdtWiseBean = getComponent(product, response);
				if(pdtWiseBean.isPresent()) {
					trendBean = pdtWiseBean.get();
					monthWithUptime = trendBean.getTrendInfo();
				}
				else {
					monthWithUptime = new ArrayList<>();
					trendBean = new TrendResponseBean();
					trendBean.setProduct(product);
					response.add(trendBean);
				}
				monthWithUptime.add(trendInfo);
				trendBean.setTrendInfo(monthWithUptime);
			});
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the SLA breach trend for 3 months - product wise.
	 * 
	 * @return a list of {@TrendResponseBean}
	 * @throws TclCommonException
	 */
	public List<TrendResponseBean> geSLATrendInfo(List<String> duration) throws TclCommonException {
		LOGGER.info("Inside geSLATrendInfo");
		if(Objects.isNull(duration)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);	
		}
		
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		
		List<TrendResponseBean> response = new ArrayList<>();
		try {
			List<Map<String, Object>> queryResponse = reports.queryForSLABreachTrend(customerLeIds, duration);
			queryResponse.stream().forEach(row -> {				
				TrendResponseBean trendBean = null;
				List<TrendInfoBean> monthWithTrendData = null;
				TrendInfoBean trendInfo = new TrendInfoBean();

				String product = (String) row.get(PerformanceConstants.KEY_PRODUCT_TYPE);
				trendInfo.setMonth(convertDateFormat((String) row.get(PerformanceConstants.KEY_MONTH_WITH_YEAR)));
				trendInfo.setSlaBreachCount((Integer) row.get(PerformanceConstants.KEY_SLA_BREACH_COUNT));
				
				Optional<TrendResponseBean> pdtWiseBean = getComponent(product, response);
				if(pdtWiseBean.isPresent()) {
					trendBean = pdtWiseBean.get();
					monthWithTrendData = trendBean.getTrendInfo();
				}
				else {
					monthWithTrendData = new ArrayList<>();
					trendBean = new TrendResponseBean();
					trendBean.setProduct(product);
					response.add(trendBean);
				}
				monthWithTrendData.add(trendInfo);
				trendBean.setTrendInfo(monthWithTrendData);
			});
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the MTTR report data - month wise, for Customer, TATA, Unidentified end.
	 * 
	 * @return {@MTTRResponseBean}
	 * @throws TclCommonException
	 */
	public MTTRTrendResponseBean getMttrTrendReport() throws TclCommonException {
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		
		try {
			List<Map<String, Object>> customerEnd = reports.queryForMTTRTrend(customerLeIds, PerformanceConstants.KEY_CUSTOMER);
			List<Map<String, Object>> tclEnd = reports.queryForMTTRTrend(customerLeIds, PerformanceConstants.KEY_TATA);
			List<Map<String, Object>> unidentifiedEnd = reports.queryForMTTRTrend(customerLeIds, PerformanceConstants.KEY_UNIDENTIFIED);
			MTTRTrendResponseBean responseBean = new MTTRTrendResponseBean();
			List<MTTRTrendInfo> trendInfo = new ArrayList<>();

			customerEnd.stream().forEach( row -> {
				MTTRTrendInfo info = new MTTRTrendInfo();
				String m1 = (String) row.get(PerformanceConstants.KEY_MONTH_WITH_YEAR);
				tclEnd.stream().forEach( row1 -> {
					String m2 = (String) row1.get(PerformanceConstants.KEY_MONTH_WITH_YEAR);
					if(m1.equals(m2)) {
						unidentifiedEnd.stream().forEach( row2 -> {
							String m3 = (String) row2.get(PerformanceConstants.KEY_MONTH_WITH_YEAR);
							if(m1.equals(m3))
								info.setAverageUnidentifiedEnd(df.format((Double) row1.get(PerformanceConstants.KEY_AVERAGE_MTTR)));
						});
						info.setMonthYear(m1);
						info.setAverageMttrCustomerEnd(df.format((Double) row.get(PerformanceConstants.KEY_AVERAGE_MTTR)));
						info.setAverageMttrTataEnd(df.format((Double) row1.get(PerformanceConstants.KEY_AVERAGE_MTTR)));
					}
				});
				trendInfo.add(info);
			});
			responseBean.setTrendInfo(trendInfo);
			return responseBean;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	
	/**
	 * Method to get the MTTR report data for PDF - month wise for Customer, TATA,
	 * Unidentified end.
	 * 
	 * @author KRUTSRIN
	 * @param startDate
	 * @param endDate
	 * @return {@LinkedHashMap}
	 * @throws TclCommonException
	 */
	public LinkedHashMap<String, HashMap<String, String>> getMttrTrendReportForPDF(String startDate,String endDate) throws TclCommonException {
		LOGGER.info(" Inside getMttrTrendReportForPDF");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		
		try {
			List<Map<String, Object>> trendList = reports.queryForMTTRTrendForPDF(customerLeIds, PerformanceConstants.KEY_CUSTOMER,startDate,endDate);
			LinkedHashMap<String, HashMap<String,String>> tempMap = new LinkedHashMap<String, HashMap<String,String>>();
			
			trendList.stream().forEach(x -> {
				HashMap<String,String> innerMap = new HashMap<String,String>();
				String m1 = (String) x.get(PerformanceConstants.KEY_MONTH_WITH_YEAR);
				String rforesp = (String) x.get("TicketMTTRTrendTicketRFOResponsible");
				BigDecimal avgMttrBig =  (BigDecimal) x.get(PerformanceConstants.KEY_AVERAGE_MTTR);
				String avgMttr = null;
				
				// convert bigDecimal to String
				if(avgMttrBig !=null) {
					avgMttr = avgMttrBig.toString();
				}
				
				if(rforesp.equals("Customer")) {
					innerMap.put("Customer",avgMttr);
				}else if(rforesp.equals("TATA")) {
					innerMap.put("TATA",avgMttr);
				}else if(rforesp.equals("Unidentified")) {
					innerMap.put("Unidentified",avgMttr);
				}
				String [] arr = {"Customer", "TATA", "Unidentified"};
				
				if(ArrayUtils.contains(arr,rforesp )){
				innerMap.put(rforesp, avgMttr);
				}
				
				if(tempMap.keySet().contains(m1)) {
					HashMap<String, String> existingInnerMap = tempMap.get(m1);
					existingInnerMap.putAll(innerMap);
				}else {
				tempMap.put(m1, innerMap);
				}
				
			});

			tempMap.forEach( (k,v) ->{
				LOGGER.info( "keys (monthYear )   : "+ k);
					
				  v.putIfAbsent("Customer", "0.000");
				
					v.putIfAbsent("TATA", "0.000");
				
					v.putIfAbsent("Unidentified", "0.000");
			});
			
			return tempMap;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the MTTR severity wise ticket count, for a given Impact type.
	 * 
	 * @param impact
	 * @return
	 * @throws TclCommonException
	 */
	public List<MTTRSeverityResponseBean> getMttrSeverityReport(String impact) throws TclCommonException {
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		
		try {
			validateSqlInjectionInput(impact); //sql injection
			List<Map<String, Object>> result = reports.queryForMTTRSeverityWise(customerLeIds, impact);
			List<MTTRSeverityResponseBean> response = new ArrayList<>();
			result.stream().forEach(row -> {
				MTTRSeverityResponseBean bean = null;
				List<TicketByMTTRDuration> list = null;
				
				String monthYear = (String) row.get(PerformanceConstants.KEY_MONTH_WITH_YEAR);
				Optional<MTTRSeverityResponseBean> existingBean = response.stream().filter(t -> t.getMonthYear().equals(monthYear)).findFirst();
				if(existingBean.isPresent() && existingBean.get() !=null) {
					bean = existingBean.get();
					list = bean.getDurationWise();
				}
				else {
					bean = new MTTRSeverityResponseBean();
					list = new ArrayList<>();
					bean.setMonthYear(monthYear);
					bean.setDurationWise(list);
					response.add(bean);
				}
				TicketByMTTRDuration durationWise = new TicketByMTTRDuration();
				durationWise.setHrsBucket((String) row.get(PerformanceConstants.KEY_HOUR_BUCKET));
				durationWise.setTicketCount((Integer) row.get(PerformanceConstants.KEY_TICKET_COUNT));
				list.add(durationWise);
			});
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	/**
	 * To get the MTTR severity wise ticket count, for a given Impact type.
	 * 
	 * @param impact
	 * @param startDate
	 * @param endDate
	 * @return list{@MTTRSeverityResponseBean}
	 * @throws TclCommonException
	 */
	public List<MTTRSeverityResponseBean> getMttrSeverityReportForPDF(String impact,String startDate,String endDate) throws TclCommonException {
		LOGGER.info("Inside getMttrSeverityReportForPDF");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if(customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);	
		}
		try {
			List<Map<String, Object>> result = reports.queryForMTTRSeverityWisePDF(customerLeIds, impact,startDate,endDate);
			List<MTTRSeverityResponseBean> response = new ArrayList<>();
			result.stream().forEach(row -> {
				MTTRSeverityResponseBean bean = null;
				List<TicketByMTTRDuration> list = null;
				
				String monthYear = (String) row.get(PerformanceConstants.KEY_MONTH_WITH_YEAR);
				Optional<MTTRSeverityResponseBean> existingBean = response.stream().filter(t -> t.getMonthYear().equals(monthYear)).findFirst();
				if(existingBean.isPresent()) {
					bean = existingBean.get();
					list = bean.getDurationWise();
				}
				else {
					bean = new MTTRSeverityResponseBean();
					list = new ArrayList<>();
					bean.setMonthYear(monthYear);
					bean.setDurationWise(list);
					response.add(bean);
				}
				TicketByMTTRDuration durationWise = new TicketByMTTRDuration();
				durationWise.setHrsBucket((String) row.get(PerformanceConstants.KEY_HOUR_BUCKET));
				durationWise.setTicketCount((Integer) row.get(PerformanceConstants.KEY_TICKET_COUNT));
				list.add(durationWise);
			});
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the severity tickets trend for 12 months based on RFO
	 * @author KRUTSRIN
	 * @param monthListforStackChart 
	 * @return {@ServerityTicketResponse}
	 * @throws TclCommonException
	 */
	public ServerityTicketResponse getTicketTrendSeverityWiseRFO(String rfoResponsible, String startDate,
			String endDate, List<String> monthYearList) throws TclCommonException {
		ServerityTicketResponse response = new ServerityTicketResponse();
		List<String> monthListforStackChart = new ArrayList<String>();
		monthListforStackChart.addAll(monthYearList);
		try {
			List<String> leIds = getLegalEntityIds();
			populateSfdcCustomerIds(leIds);
			if (leIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			}

			List<TicketTrend> trends = new ArrayList<>();
			List<Map<String, Object>> result = reports.queryForTrendSeverityWiseWrtRFO(leIds, rfoResponsible, startDate,
					endDate);
			result.stream().forEach(row -> populateTrends(row, trends, PerformanceConstants.KEY_MMYYYY));
			List<String> availableInTeradata = trends.stream().map(TicketTrend::getProductOrMonth)
					.collect(Collectors.toList());
			trends.addAll(monthListforStackChart.stream()
					.filter(monthOrProduct -> !availableInTeradata.contains(monthOrProduct)).map(pdt -> {
						TicketTrend ticketTrend = new TicketTrend();
						ticketTrend.setProductOrMonth(pdt);
						return ticketTrend;
					}).collect(Collectors.toList()));
			response.setTrends(trends);
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * To get the reason for outage specification with the ticket count for a given
	 * month for customer or non customer
	 * @author KRUTSRIN
	 * @param month - The month as 12-2018 format required.
	 * @return {@Map<String, Object>}, Reason with Count.
	 * @throws TclCommonException
	 */
	public Object[] getMonthlyOutageReportForCustAndNonCust(String monthYear) throws TclCommonException {
		LOGGER.info("Inside getMonthlyOutageReportForCustAndNonCust");
		Map<String, Object> outageReasonWithCountForCust = new LinkedHashMap<>();
		Map<String, Object> outageReasonWithCountForNonCust = new LinkedHashMap<>();

		if (StringUtils.isEmpty(monthYear)) {
			LOGGER.error(ExceptionConstants.REQUEST_VALIDATION_ERROR);
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION_ERROR);
		}
		try {
			List<String> customerLeIds = getLegalEntityIds();
			populateSfdcCustomerIds(customerLeIds);
			if (customerLeIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			}
			List<Map<String, Object>> reasonWithCountCust = reports.queryForOutageReasonsCustomerEnd(customerLeIds,
					monthYear);
			List<Map<String, Object>> reasonWithCountNonCust = reports
					.queryForOutageReasonsOtherThanCustomerEnd(customerLeIds, monthYear);

			getOutageWithCount(outageReasonWithCountForCust, reasonWithCountCust);
			getOutageWithCount(outageReasonWithCountForNonCust, reasonWithCountNonCust);

			return new Object[] { outageReasonWithCountForCust, outageReasonWithCountForNonCust };

		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_RFO_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_RFO_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * To get the MTTR severity wise ticket count, for a given Impact type for given
	 * RFO. RFO will be either customer or TATA
	 * 
	 * @author KRUTSRIN
	 * @param impact
	 * @param startDate
	 * @param startDate
	 * @return list {@MTTMTTRSeverityResponseBean}
	 * @throws TclCommonException
	 */
	public List<MTTRSeverityResponseBean> getMttrSeverityReportTataOrCustomer(String impact, String owner,String startDate,String endDate)
			throws TclCommonException {
		LOGGER.info("Inside getMttrSeverityReportTataOrCustomer");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if (customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
		}
		try {
			List<Map<String, Object>> result = reports.queryForMTTRSeverityWithTataOrCustImpact(customerLeIds, impact,
					owner,startDate,endDate);
			List<MTTRSeverityResponseBean> responseList = buildMTTRObjFOResponsibilityWise(result);
			return responseList;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * To get the MTTR report data - month wise, based on rfoRespondibility.
	 * @param rforesponsiblity,impact
	 * @author KRUTSRIN
	 * @return {@MTTRResponseBean}
	 * @throws TclCommonException
	 */
	public List<MTTRTrendRFOWise> getMttrTrendReportRfoResponsibleWise(String rfoResponsible, String impact,String startDate,String endDate)
			throws TclCommonException {
		LOGGER.info("Inside getMttrTrendReportRfoResponsibleWise");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if (customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
		}

		try {
			List<Map<String, Object>> trendList = reports.queryForMTTRTrendForRFOResponsiblility(customerLeIds,
					rfoResponsible, impact,startDate,endDate);
			List<MTTRTrendRFOWise> trendInfo = new ArrayList<>();
			trendList.stream().forEach(row -> {
				MTTRTrendRFOWise info = new MTTRTrendRFOWise();
				String m1 = (String) row.get(PerformanceConstants.RFOTicketImpactMTTRTrendMonthYear);
				info.setMonthYear(m1);
				String m2 = (String) row.get(PerformanceConstants.RFOTicketImpactMTTRTrendTicketImpact);
				info.setImpact(m2);
				String m3 = df.format((BigDecimal) row.get(PerformanceConstants.RFOTicketImpactMTTRTrendAVGMTTR));
				info.setAvgMttr(m3);
				trendInfo.add(info);
			});
			return trendInfo;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_MTTR_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Get Fault rate for a given month
	 * @param month
	 * @author KRUTSRIN
	 * @param productsForUser 
	 * @return list of fault rate
	 * @throws TclCommonException
	 */
	public List<FaultRate> getFaultrate(String month, List<String> productsForUser) throws TclCommonException {
		LOGGER.info("Inside getFaultrate");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if (customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
		}
		try {
			List<FaultRate> trends = new ArrayList<>();
			validateSqlInjectionInput(month); //sql injection
			List<FaultRateBean> faultList = reports.queryForFaultrate(customerLeIds, month);
			faultList.stream().forEach(row -> populateFaultTrends(row, trends));
			List<String> availableInTeradata = trends.stream().map(FaultRate :: getProductOrMonthWise).collect(Collectors.toList());

			if (Objects.nonNull(productsForUser)) {
				trends.addAll(productsForUser.stream().filter(pdt -> !availableInTeradata.contains(convertPdtName(pdt)))
						.map(pdt -> {
							FaultRate faultRate = new FaultRate();
							faultRate.setProductOrMonthWise(pdt);
							return faultRate;
						}).collect(Collectors.toList()));
			}
			return trends;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_UPTIME_REPORTS, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * To get the severity tickets for given months duration, categorized based on
	 * Impact.
	 * 
	 * @author KRUTSRIN
	 * @param monthYearListForRFO 
	 * @return {@ServerityTicketResponse}
	 * @throws TclCommonException
	 */
	public ServerityTicketResponse getTicketTrendSeverityWiseForGivenMonthRange(String startDate, String endDate,
			List<String> monthYearList) throws TclCommonException {
		LOGGER.info("Inside getTicketTrendSeverityWiseForGivenMonthRange ::startDate ::{},endDate :: {}, monthYearList::{}",
				startDate,endDate,monthYearList);
	List<String> monthYearListForRFO = new ArrayList<String>();
	monthYearListForRFO.addAll(monthYearList);
		ServerityTicketResponse response = new ServerityTicketResponse();
		try {
			List<String> leIds = getLegalEntityIds();
			populateSfdcCustomerIds(leIds);
			if (leIds.isEmpty()) {
				LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
				throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			}

			List<TicketTrend> trends = new ArrayList<>();
			List<Map<String, Object>> result = reports.queryForTrendSeverityWiseForGivenMonthRange(leIds, startDate,
					endDate);
			result.stream().forEach(row -> populateTrends(row, trends, PerformanceConstants.KEY_MMYYYY));
			List<String> availableInTeradata = trends.stream().map(TicketTrend::getProductOrMonth)
					.collect(Collectors.toList());
				monthYearListForRFO.removeAll(availableInTeradata);
				monthYearListForRFO.stream().forEach(monthOrProduct -> {
					TicketTrend tTrend = new TicketTrend();
					tTrend.setProductOrMonth(monthOrProduct);
					// logic in pdfReportService already handles impacts and its counts.
					// initializing missing month is enough here
					trends.add(tTrend);
				});
			
			SimpleDateFormat f = new SimpleDateFormat("MM-yyyy");
			trends.stream().sorted((a, b) -> {
				try {
					return f.parse(a.getProductOrMonth()).compareTo(f.parse(b.getProductOrMonth()));
				} catch (ParseException e) {
				 LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));

				}
				return 0;
			});
			response.setTrends(trends);
			return response;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SEVERITY_REPORTS, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to get SLA Breach cases as a list of SLABreachBean
	 * 
	 * @author KRUTSRIN
	 * @param diff
	 * @return list of SLABreachBean
	 * @throws TclCommonException
	 */
	public List<SLABreachBean> getSLABreachedCases(String diff) throws TclCommonException {
		LOGGER.info("Inside getSLABreachedCases");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		if (customerLeIds.isEmpty()) {
			LOGGER.error(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMER_IDS);
		}
		List<SLABreachBean> sortedList = new ArrayList<SLABreachBean>();
		try {
			validateSqlInjectionInput(diff); //sql injection
			List<SLABreachBean> breachList = reports.querySLABreachCases(customerLeIds, diff);
			breachList.forEach(x -> {
				if ((x.getCommittedUptime() != null && x.getAchievedUptime() != null)
						&& Double.valueOf(x.getCommittedUptime()) > Double.valueOf(x.getAchievedUptime())) {
					if (x.getServiceLink() != null) {
						x.setServiceID(x.getServiceID() + "/" + x.getServiceLink());
					}
					sortedList.add(x);
				}
			});
			return sortedList;
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_SLA_BREACH_CASES_REPORTS, ExceptionUtils.getStackTrace(ex));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_SLA_BREACH_CASES_REPORTS, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	/**
	 * Method to get Ticket details for specific CUID to generate excel sheet
	 * @param month string in MM-YYYY format
	 * @return list of {@TicketExcelBean}
	 * @throws TclCommonException
	 */
	public List<TicketExcelBean> getTicketDetailsForExcel(String month) throws TclCommonException {
		LOGGER.info("getTicketDetailsForExcel for month {}",month);
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		return reports.getTicketExcel(customerLeIds,month);
	}
	
	

	/**
	 * Method to get uptime details for specific CUID to generate excel sheet
	 * @author KRUTSRIN
   * @param month string in MM-YYYY format
	 * @return list of {@UptimeExcelBean}
	 * @throws TclCommonException
	 */
	public List<UptimeExcelBean> getUptimeForExcel(String month) throws TclCommonException {
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		validateSqlInjectionInput(month); //sql injection
		return reports.getUptimeExcel(customerLeIds,month);
	}
	
	
	/**
	 * Method to get serviceInventory details for specific CUID to generate excel sheet
	 * @author KRUTSRIN
   * @param month string in MM-YYYY format
	 * @return list of {@ServiceInventoryBean}
	 * @throws TclCommonException
	 */
	public List<ServiceInventoryBean> getserviceInventoryDetailsForExcel(String month) throws TclCommonException {
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		return reports.getServiceInventoryExcel(customerLeIds,month);
	}

	/**
	 * Method to get serviceInventory details for specific CUID.
	 * 
	 * @author KRUTSRIN
	 * @param month string in MM-YYYY format
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public Map<String, String> getServiceInventoryDetailTypeWiseForPDF(String month) throws TclCommonException {
		LOGGER.info("Inside getServiceInventoryDetailTypeWiseForPDF");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		validateSqlInjectionInput(month); //sql injection
		List<ServiceInventoryDonutBean> siBeanListFromDB = reports.getServiceInventoryTypeWise(customerLeIds, month);

		return siBeanListFromDB.stream().peek(
				pk -> LOGGER.info("SERVICE_TYPE:" + pk.getTargetServiceType() + " COUNT:: " + pk.getServiceIdCount()))
				.collect(Collectors.toMap(ServiceInventoryDonutBean::getTargetServiceType,
						ServiceInventoryDonutBean::getServiceIdCount, (a1, a2) -> a2));
	}
	
	/**
	 * Method to get serviceInventory status wise for specific CUID.
	 * 
	 * @author KRUTSRIN
	 * @param month string in MM-YYYY format
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public Map<String, String> getServiceInventoryDetailStatusWiseForPDF(String month) throws TclCommonException {
		LOGGER.info("Inside getServiceInventoryDetailStatusWiseForPDF");
		List<String> customerLeIds = getLegalEntityIds();
		populateSfdcCustomerIds(customerLeIds);
		validateSqlInjectionInput(month); //sql injection
		List<ServiceInventoryDonutBean> siBeanListFromDB = reports.getServiceStatusWiseInventory(customerLeIds, month);
		return siBeanListFromDB.stream().peek(
				pk -> LOGGER.info("SERVICE_STATUS:" + pk.getTargetServiceType() + " COUNT:: " + pk.getServiceIdCount()))
				.collect(Collectors.toMap(ServiceInventoryDonutBean::getServiceStatus,
						ServiceInventoryDonutBean::getServiceIdCount, (a1, a2) -> a2));
	}


	/**
	 * Method to populate faultTrends from query result
	 * @author KRUTSRIN
	 * @param FaultRateBean
	 * @param trends
	 */
	private void populateFaultTrends(FaultRateBean row, List<FaultRate> trends) {

		if (row.getCustomerServiceFaultMetricsServiceType().equals("Global VPN")
				|| row.getCustomerServiceFaultMetricsServiceType().equals("GVPN")) {
			row.setCustomerServiceFaultMetricsServiceType("GVPN");
		} else if (row.getCustomerServiceFaultMetricsServiceType().equals("Internet Access Service")
				|| row.getCustomerServiceFaultMetricsServiceType().equals("IAS")) {
			row.setCustomerServiceFaultMetricsServiceType("ILL");
		} else if (row.getCustomerServiceFaultMetricsServiceType().equals("National Private Line")
				|| row.getCustomerServiceFaultMetricsServiceType().equals("NPL")) {
			row.setCustomerServiceFaultMetricsServiceType("NPL");
		}

		String totCrcuitcount = row.getCustomerServiceFaultMetricsTotalCircuitCount();
		String impactCrcuitcount = row.getCustomerServiceFaultMetricsImpactedCircuitCount();
		String faultRate = row.getCustomerServiceFaultMetricsFaultRate();

		CircuitDetails cd = new CircuitDetails();
		FaultRate fl = new FaultRate();
		fl.setProductOrMonthWise(row.getCustomerServiceFaultMetricsServiceType());

		cd.setFaultMetricsTotalCircuit("Total No of Circuits");

		if (totCrcuitcount != null) {
			cd.setFaultMetricsTotalCircuitCount(df.format(Double.parseDouble(totCrcuitcount)));
		} else {
			cd.setFaultMetricsTotalCircuitCount("00.00");
		}
		cd.setFaultMetricsImpactedCircuit("Impacted Circuits");
		if (impactCrcuitcount != null) {
			cd.setFaultMetricsImpactedCircuitCount(df.format(Double.parseDouble(impactCrcuitcount)));
		} else {
			cd.setFaultMetricsImpactedCircuitCount("00.00");
		}
		cd.setFaultMetricsFaultRate("Fault Rate");
		if (faultRate != null) {
			cd.setFaultMetricsFaultRateCount(df.format(Double.parseDouble(faultRate)));
		} else {
			cd.setFaultMetricsFaultRate(df.format("00.00"));
		}
		fl.setCiruitDetail(cd);
		trends.add(fl);
	}

	/**
	 * Method to build RFO object responsibility wise
	 * @author KRUTSRIN
	 * @param result
	 * @return List of MTTRSeverityResponseBean
	 */
	private List<MTTRSeverityResponseBean> buildMTTRObjFOResponsibilityWise(List<Map<String, Object>> result) {
		List<MTTRSeverityResponseBean> response = new ArrayList<>();
		result.stream().forEach(row -> {
			MTTRSeverityResponseBean bean = null;
			List<TicketByMTTRDuration> list = null;

			String monthYear = (String) row.get(PerformanceConstants.KEY_MONTH_WITH_YEAR);
			Optional<MTTRSeverityResponseBean> existingBean = response.stream()
					.filter(t -> t.getMonthYear().equals(monthYear)).findFirst();
			if (existingBean.isPresent() && existingBean.get() != null) {
				bean = existingBean.get();
				list = bean.getDurationWise();
			} else {
				bean = new MTTRSeverityResponseBean();
				list = new ArrayList<>();
				bean.setMonthYear(monthYear);
				bean.setDurationWise(list);
				response.add(bean);
			}
			TicketByMTTRDuration durationWise = new TicketByMTTRDuration();
			durationWise.setHrsBucket((String) row.get(PerformanceConstants.KEY_HOUR_BUCKET));
			durationWise.setTicketCount((Integer) row.get(PerformanceConstants.KEY_TICKET_COUNT));
			list.add(durationWise);
		});
		return response;
	}

	/**
	 * A method to get Outrage count.
	 * @author KRUTSRIN
	 * @param outageReasonWithCount
	 * @param reasonWithCount
	 */
	private void getOutageWithCount(Map<String, Object> outageReasonWithCount,
			List<Map<String, Object>> reasonWithCount) {
		outageReasonWithCount.put(PerformanceConstants.KEY_TOTAL, 0);
		outageReasonWithCount.put(PerformanceConstants.KEY_TOP, 0);
		reasonWithCount.stream().forEach(map -> {
			String outageReason = (String) map.get(PerformanceConstants.KEY_RFO_SPEC);
			Integer ticketCount = (Integer) map.get(PerformanceConstants.KEY_TICKET_COUNT);

			// calculate total count of tickets
			int count = (int) outageReasonWithCount.get(PerformanceConstants.KEY_TOTAL);
			count = count + ticketCount;
			if (outageReasonWithCount.size() <= 10) {
				outageReasonWithCount.put(outageReason, ticketCount);
				outageReasonWithCount.put(PerformanceConstants.KEY_TOP, count);
			}
			outageReasonWithCount.put(PerformanceConstants.KEY_TOTAL, count);
		});

		int othersCount = (int) outageReasonWithCount.get(PerformanceConstants.KEY_TOTAL)
				- (int) outageReasonWithCount.get(PerformanceConstants.KEY_TOP);
		outageReasonWithCount.put(PerformanceConstants.KEY_OTHERS, othersCount);

		outageReasonWithCount.remove(PerformanceConstants.KEY_TOP);
	}

	private void populateTrendsForMonth(Map<String, Object> row, List<TicketTrend> trends, String serviceType) {
		String product = (String) row.get(serviceType);
		String impact = (String) row.get(PerformanceConstants.KEY_IMPACT);
		Integer count = (Integer) row.get(PerformanceConstants.KEY_TICKET_COUNT);
		
		Optional<TicketTrend> tt = trends.stream().filter(t -> t.getProductOrMonth().equals(product)).findFirst();
		if(!tt.isPresent()) {
			ImpactWithCount ic = new ImpactWithCount();
			ic.setImpact(impact);
			ic.setCount(count);
			List<ImpactWithCount> impacts = new ArrayList<>();
			impacts.add(ic);

			TicketTrend trend = new TicketTrend();
			trend.setProductOrMonth(product);
			trend.setImpactTickets(impacts);
			trends.add(trend);
		}
		else {
			TicketTrend addedPdtTrend = tt.get();
			List<ImpactWithCount> impacts = addedPdtTrend.getImpactTickets();
			ImpactWithCount ic = new ImpactWithCount();
			ic.setImpact(impact);
			ic.setCount(count);
			impacts.add(ic);
		}
	}

	
	private void populateTrends(Map<String, Object> row, List<TicketTrend> trends, String inputMonth) {
		String month = (String) row.get(inputMonth);
		String impact = (String) row.get(PerformanceConstants.KEY_IMPACT);
		Integer count = (Integer) row.get(PerformanceConstants.KEY_TICKET_COUNT);
		
		String monthFormatted = convertDate(month);
		Optional<TicketTrend> tt = trends.stream().filter(t -> t.getProductOrMonth().equals(monthFormatted)).findFirst();
		if(!tt.isPresent()) {
			ImpactWithCount ic = new ImpactWithCount();
			ic.setImpact(impact);
			ic.setCount(count);
			List<ImpactWithCount> impacts = new ArrayList<>();
			impacts.add(ic);

			TicketTrend trend = new TicketTrend();
			trend.setProductOrMonth(monthFormatted);
			trend.setImpactTickets(impacts);
			trends.add(trend);
		}
		else {
			TicketTrend addedPdtTrend = tt.get();
			List<ImpactWithCount> impacts = addedPdtTrend.getImpactTickets();
			ImpactWithCount ic = new ImpactWithCount();
			ic.setImpact(impact);
			ic.setCount(count);
			impacts.add(ic);
		}
	}

	private Optional<TrendResponseBean> getComponent(String product, List<TrendResponseBean> repsonse) {
		return repsonse.stream().filter(info -> info.getProduct().equals(product)).findFirst();
	}
		
	private List<String> getLegalEntityIds() throws TclCommonException {
		LOGGER.info("Inside getLegalEntityIds()");
		UserInformation userInfo = userInfoUtils.getUserInformation(Utils.getSource());
		LOGGER.info("userInfo in  getLegalEntityIds():: {}", userInfo);
		if (Objects.isNull(userInfo))
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_USERINFO);
		LOGGER.info("getCustomer Details");
		List<CustomerDetail> customers = userInfo.getCustomers();
		LOGGER.info("getCustomer Details :: {}",customers);
		if (Objects.isNull(customers))
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);

		List<String> customerLeIds = customers.stream().map(cust -> cust.getCustomerLeId().toString()).collect(Collectors.toList());
		LOGGER.info("customerLeIds  :: {}",customerLeIds);
		return customerLeIds;
	}
	
	private List<String> populateSfdcCustomerIds(List<String> leIds) throws TclCommonException {
		if(leIds==null || leIds.isEmpty())
			throw new TclCommonException(ExceptionConstants.CUSTOMER_LE_ERROR, ResponseResource.R_CODE_NOT_FOUND); 
		LOGGER.info("Inside populateSfdcCustomerIds ");
		String customerLeIdsCommaSeparated = String.join(",", leIds);
		LOGGER.info("performing Queue call for rabbitmq_customerle_details_queue ");
		String response = (String) mqUtils.sendAndReceive(customerLeIdsQueue, customerLeIdsCommaSeparated);
		LOGGER.info("response from Queue call for rabbitmq_customerle_details_queue :: {}",response);
		CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
				.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
		LOGGER.info("cLeBean :: {}",cLeBean);
		leIds.clear();
		if (null != cLeBean) {
			cLeBean.getCustomerLeDetails().stream().forEach(customerLeBean ->
				leIds.add(customerLeBean.getSfdcId())
		);} 
		return leIds;
	}
	
	private String convertPdtName(String pdt) {
		switch(pdt.toUpperCase()) {
			case PerformanceConstants.GVPN:
				return PerformanceConstants.VAL_GVPN;
			case PerformanceConstants.IAS:
				return PerformanceConstants.VAL_IAS;
			case PerformanceConstants.ILL:
				return PerformanceConstants.VAL_IAS;
			case PerformanceConstants.NPL:
				return PerformanceConstants.VAL_NPL;
			case PerformanceConstants.GSIP:
				return PerformanceConstants.VAL_GSIP;
			default:
				return pdt;
		}
		
	}	
	/**
	 * Method to convert date format
	 * 
	 * @param dateStr
	 * @return MMM-yy format date String
	 */
	private String convertDateFormat(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-yy");
		Date dNow = null;
		try {
			dNow = sdf.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}

		SimpleDateFormat ft1 = new SimpleDateFormat("MM-yyyy");
		LOGGER.info("Product wis uptime trend X axis dates :: " + ft1.format(dNow));
		return ft1.format(dNow);
	}
	
	/**
	 * Method to convert input date in 01-2019 to format Jan-2019
	 * 
	 * @param inputDate
	 * @return Date in MMM-YYYY format
	 */
	private String convertDate(String inputDate) {
		SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
		Date current = null;
		try {
			current = format.parse(inputDate);
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}
		SimpleDateFormat format1 = new SimpleDateFormat("MMM-yyyy");
		return format1.format(current);
	}
/**
	 * Converting a string date in one format to date string of another format.
	 * 
	 * @author KRUTSRIN
	 * @param sourceForm
	 * @param destinationForm
	 * @param strDate
	 * @return dateString
	 */
	public static String convertDateStrFormat(String sourceForm, String destinationForm, String strDate) {
		SimpleDateFormat sdfSource = new SimpleDateFormat(sourceForm);
		Date date = null;
		try {
			date = sdfSource.parse(strDate);
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}
		SimpleDateFormat sdfDestination = new SimpleDateFormat(destinationForm);
		strDate = sdfDestination.format(date);
		return strDate;
	}
	
	public static void validateSqlInjectionInput(String inputParam) throws TclCommonException {
		if (StringUtils.isNotBlank(inputParam)) {
			if (inputParam.toLowerCase().contains(" or ") || inputParam.toLowerCase().contains(" and ") || inputParam.contains("=") || inputParam.contains("*")
					|| inputParam.toLowerCase().contains("delete") || inputParam.toLowerCase().contains("update")
					|| inputParam.toLowerCase().contains("select") || inputParam.toLowerCase().contains("!")) {
				LOGGER.info("vulnarable input detected {}", inputParam);
				throw new TclCommonException("billing.validation.sql");
			} else {
				LOGGER.info("Input is safe {}", inputParam);
			}
		}
	}
}