package com.tcl.dias.performance.service.v1.teradata.queries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tcl.dias.beans.FaultRateBean;
import com.tcl.dias.beans.SLABreachBean;
import com.tcl.dias.beans.ServiceInventoryBean;
import com.tcl.dias.beans.ServiceInventoryDonutBean;
import com.tcl.dias.beans.TicketExcelBean;
import com.tcl.dias.beans.UptimeExcelBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.performance.constants.ExceptionConstants;
import com.tcl.dias.performance.constants.PerformanceConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the queries for Uptime, faultrate, RFO causes, Severity trends, MTTR.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class TeradataReportsQuery {
	private static final Logger LOGGER = LoggerFactory.getLogger(TeradataReportsQuery.class);

	@Qualifier(value = "teradataTemplate")
	@Autowired
	private JdbcTemplate teradataJdbcTemplate;
	
	
	public List<Map<String, Object>> queryForOutageReasons(List<String> cuids, String month) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");
		String query = "Select \r\n" + 
				"	nvl(B.TicketRFOSpecification,'NULL') as TicketRFOSpecification, count(B.TicketNo) as TktCount \r\n" + 
				"from \r\n" + 
				"	GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + 
				"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
				"	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + 
				"where \r\n" + 
				"	extract(month from ticketClosedtime) = " + Integer.valueOf(arr[0]) + "\r\n" + 
				"	and extract(year from ticketClosedtime) = " + Integer.valueOf(arr[1]) +"\r\n" + 
				"	and Ticketstatus like 'Closed%'\r\n" + 
				"	and partycuid in (" + ids + ")\r\n" + 
				"	and PartyType='Legal'\r\n" + 
				"	and Tickettasktype in ('Case','Incident')\r\n" + 
				"	and B.TicketCustomerAcknowledgementFlag='True'\r\n" + 
				"	and B.TicketSourceIdentifier='SNOW'\r\n" + 
				"group by B.TicketRFOSpecification \r\n" + 
				"order by TktCount desc";
		try {
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForOutageCircuitDetails(List<String> cuids, String month, String outageSpecification) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");
		String query = "Select\r\n" + 
				"	GRServiceInventoryServiceId as ServiceId, GRServiceInventoryTargetServiceType as ServiceType,\r\n" + 
				"	B.TicketNo, B.TicketRFOSpecification as RFOSpecification, B.TicketImpact as Impact," +
				"	B.TicketCategoryDetail as CategoryDetail, B.TicketRFOResponsible as RFOResponsible,\r\n" + 
				"	GRServiceInventoryAEndSiteAddress as SiteAddress, GRServiceInventoryCircuitAlias as CktAlias\r\n" + 
				"from \r\n" + 
				"	GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + 
				"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
				"	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + 
				"left outer  join GALAXYDWH_360_security_V.Fact_Circuit A\r\n" + 
				"	on A.CircuitGlobalId = B.Ticket2CircuitGlobalId\r\n" + 
				"left outer join GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist\r\n" + 
				"	on GRServiceInventoryServiceId = A.Circuitid\r\n" + 
				"where \r\n" + 
				"	extract(month from ticketClosedtime) = " + Integer.valueOf(arr[0]) + "\r\n" + 
				"	and extract(year from ticketClosedtime) = "+ Integer.valueOf(arr[1]) + "\r\n" + 
				"	and Ticketstatus like 'closed%'\r\n" + 
				"	and partycuid in (" + ids +")\r\n" + 
				"	and PartyType='Legal'\r\n" + 
				"	and Tickettasktype in ('Case','Incident')\r\n" + 
				"	and B.TicketCustomerAcknowledgementFlag='True'\r\n" + 
				"	and B.TicketSourceIdentifier='SNOW'\r\n" +
				"	and B.TicketRFOSpecification in (" + outageSpecification + ") \r\n" +
				"	and extract(month from GRServiceInventoryAsOnDate) = "+ Integer.valueOf(arr[0]) +" \r\n" + 
				"	and extract(year from GRServiceInventoryAsOnDate) = " + Integer.valueOf(arr[1]) + "\r\n" + 
				"group by \r\n" + 
				"	RFOSpecification,ServiceType,CategoryDetail,RFOResponsible,SiteAddress,CktAlias,B.TicketNo,ServiceId,Impact\r\n" + 
				"order by \r\n" + 
				"	RFOSpecification,ServiceType,B.TicketNo,Impact,CategoryDetail,RFOResponsible,SiteAddress,CktAlias,ServiceId";
		try {
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForMonthwiseTickets(List<String> cuids, String month) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");
		try {
			String query = "Select \r\n" + 
					"	GRServiceInventoryTargetServiceType as ServiceType,\r\n" + 
					"	count(B.TicketNo) as TktCount,B.TicketImpact as Impact\r\n" + 
					"from \r\n" + 
					"	GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
					"	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + 
					"inner join GALAXYDWH_360_security_V.Fact_Circuit A\r\n" + 
					"	on A.CircuitGlobalId = B.Ticket2CircuitGlobalId\r\n" + 
					"inner join GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist\r\n" + 
					"	on GRServiceInventoryServiceId = A.Circuitid\r\n" + 
					"where \r\n" + 
					"		extract(month from ticketClosedtime) = " + Integer.valueOf(arr[0]) + "\r\n" + 
					"	and extract(year from ticketClosedtime) = " + Integer.valueOf(arr[1]) + "\r\n" + 
					"	and Ticketstatus like 'closed%'\r\n" + 
					"	and GRServiceInventoryTargetServiceType in ('" + PerformanceConstants.VAL_GVPN + "'," + 
					" '" + PerformanceConstants.VAL_IAS + "', " + "'" + PerformanceConstants.VAL_NPL + "'" + ")\r\n" + 
					"	and partycuid in (" + ids + ")\r\n" + 
					"	and Tickettasktype in ('Case','Incident')\r\n" + 
					"	and B.TicketCustomerAcknowledgementFlag='True'\r\n" + 
					"	and B.TicketSourceIdentifier='SNOW'\r\n" + 
					"	and CircuitSourceIdentifier='SFDC'\r\n" + 
					"	and extract(month from GRServiceInventoryAsOnDate) = " + Integer.valueOf(arr[0]) +  "\r\n" + 
					"	and extract(year from GRServiceInventoryAsOnDate) = " + Integer.valueOf(arr[1]) + " \r\n" + 
					"group by \r\n" + 
					"	ServiceType, Impact\r\n" + 
					"order by \r\n" + 
					"	ServiceType";
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryTicketDetailsBySeverity(List<String> cuids, String month, String product, String impact) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");
		try {
			String query = "Select \r\n" + 
					"	GRServiceInventoryServiceId as ServiceId, B.TicketNo,\r\n" + 
					"	B.TicketImpact as Impact, GRServiceInventoryTargetServiceType as ServiceType, B.TicketRFOResponsible as RFOResponsible,\r\n" + 
					"	B.TicketCategoryDetail as CategoryDetail, GRServiceInventoryAEndSiteAddress as SiteAddress, GRServiceInventoryCircuitAlias as CktAlias\r\n" + 
					"from \r\n" + 
					"	GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
					"	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + 
					"inner join GALAXYDWH_360_security_V.Fact_Circuit A\r\n" + 
					"	on A.CircuitGlobalId = B.Ticket2CircuitGlobalId\r\n" + 
					"inner join GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist\r\n" + 
					"	on GRServiceInventoryServiceId = A.Circuitid\r\n" + 
					"where \r\n" + 
					"	extract(month from ticketClosedtime) = " + Integer.valueOf(arr[0]) +"\r\n" + 
					"	and extract(year from ticketClosedtime) = " + Integer.valueOf(arr[1]) +"\r\n" + 
					"	and Ticketstatus like 'closed%' \r\n" + 
					"	and GRServiceInventoryTargetServiceType = '"+ product +"'\r\n" + 
					"	and B.TicketImpact = '" + impact +"' \r\n" + 
					"	and partycuid in (" + ids +") \r\n" + 
					"	and Tickettasktype in ('Case','Incident' )\r\n" + 
					"	and B.TicketCustomerAcknowledgementFlag='True' \r\n" + 
					"	and B.TicketSourceIdentifier='SNOW' \r\n" + 
					"	and CircuitSourceIdentifier='SFDC' \r\n" + 
					"	and extract(month from GRServiceInventoryAsOnDate) = " + Integer.valueOf(arr[0]) +" \r\n" + 
					"	and extract(year from GRServiceInventoryAsOnDate) = " + Integer.valueOf(arr[1]) +" \r\n" + 
					"group by  \r\n" + 
					"	CategoryDetail, RFOResponsible, SiteAddress, CktAlias, B.TicketNo, ServiceId, Impact, ServiceType\r\n" + 
					"order by Impact";
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForTrendSeverityWise(List<String> cuids) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
			String query = "Select \r\n" + 
					"	count(B.TicketNo) as TktCount, B.TicketImpact as Impact, " +
					"ticketClosedtime (format 'mm-yyyy') (char(7)) as MonthYear,\r\n" + 
					"cast(extract(month from ticketClosedtime)||trim(extract(year from ticketClosedtime)) as date format 'mmyyyy') as DateYear \r\n" + 
					"from \r\n" + 
					"GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
					"on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + 
					"inner join GALAXYDWH_360_security_V.Fact_Circuit A\r\n" + 
					"on A.CircuitGlobalId = B.Ticket2CircuitGlobalId\r\n" + 
					"inner join GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist\r\n" + 
					"on (GRServiceInventoryServiceId = A.Circuitid\r\n" + 
					"and trim(extract( month from GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GRServiceInventoryAsOnDate))=\r\n" + 
					"trim(extract( month from ticketClosedtime) (format'99'))||'-'||trim(extract( year from ticketClosedtime))\r\n" + 
					")\r\n" + 
					"where  ticketClosedtime between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-6) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day\r\n" + 
					"and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE))\r\n" + 
					"and Ticketstatus like 'closed%'\r\n" + 
					"and partycuid in (" + ids + " )\r\n" +  
					"and PartyType='Legal'\r\n" + 
					"and Tickettasktype in ('Case','Incident')\r\n" + 
					"and B.TicketCustomerAcknowledgementFlag='True'\r\n" + 
					"and B.TicketSourceIdentifier='SNOW'\r\n" + 
					"and GRServiceInventoryTargetServiceType in ('Internet Access Service', 'Global VPN', 'National Private Line')\r\n" + 
					"and  GRServiceInventoryAsOnDate between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-6) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day\r\n" + 
					"and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE))\r\n" + 
					"group by Impact, MonthYear, DateYear \r\n" + 
					"order by DateYear";
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}

	public List<Map<String, Object>> queryTicketDetailsBySeverity(List<String> cuids, String month, String impact) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");
		try {
			String query = "Select \r\n" + 
					"	B.TicketNo, GRServiceInventoryServiceId as ServiceId, GRServiceInventoryTargetServiceType as ServiceType, \r\n" + 
					"	B.TicketImpact as Impact, B.TicketRFOSpecification as RFOSpecification, B.TicketStatus as Status, " +
					"	B.TicketOutageDurationMin as OutageInMin, B.TicketRFOResponsible as RFOResponsible, GRServiceInventoryAEndSiteAddress as SiteAddress \r\n" + 
					"from \r\n" + 
					"	GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
					"	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + 
					"inner join GALAXYDWH_360_security_V.Fact_Circuit A\r\n" + 
					"	on A.CircuitGlobalId = B.Ticket2CircuitGlobalId\r\n" + 
					"inner join GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist\r\n" + 
					"	on GRServiceInventoryServiceId = A.Circuitid\r\n" + 
					"where \r\n" + 
					"	extract(month from ticketClosedtime) = " + Integer.valueOf(arr[0]) +"\r\n" + 
					"	and extract(year from ticketClosedtime) = " + Integer.valueOf(arr[1]) +"\r\n" + 
					"	and Ticketstatus like 'closed%' \r\n" + 
					"	and partycuid in (" + ids +") \r\n" + 
					"	and Tickettasktype in ('Case','Incident') \r\n" + 
					"	and B.TicketCustomerAcknowledgementFlag='True' \r\n" + 
					"	and B.TicketSourceIdentifier='SNOW' \r\n" +
					"	and CircuitSourceIdentifier='SFDC' \r\n" +
					"	and GRServiceInventoryTargetServiceType in ('" +PerformanceConstants.VAL_GVPN+ "'," + 
					"	'" +PerformanceConstants.VAL_IAS +"', " + "'" +PerformanceConstants.VAL_NPL+ "') \r\n" + 
					"	and B.TicketImpact = '" + impact +"' \r\n" + 
					"	and extract(month from GRServiceInventoryAsOnDate) = " + Integer.valueOf(arr[0]) +" \r\n" + 
					"	and extract(year from GRServiceInventoryAsOnDate) = " + Integer.valueOf(arr[1]) +" \r\n" + 
					"group by  \r\n" + 
					"	B.TicketNo, ServiceId, ServiceType, Impact,  RFOSpecification, RFOResponsible, Status, OutageInMin, SiteAddress\r\n" + 
					"order by Impact";
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForUptimeWithFaultrateWithSLA(List<String> cuids, String month) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		LOGGER.info("queryForUptimeWithFaultrateWithSLA with cuids {} ",ids);
		String[] dates = getMonthDates(Integer.valueOf(month.split("-")[0]));
		LOGGER.info("queryForUptimeWithFaultrateWithSLA and date {}",dates);
		try {
			String query = "select\r\n" + 
					"	avg(UptimePercentage) as UptimePercentage , \r\n" + 
					"	sum(TotalCount) as TotalCount , \r\n" + 
					"	sum(SLABreachCount) as SLABreachCount ,\r\n" + 
					"	avg(SLABreachPercent) as SLABreachPercent ,\r\n" + 
					"	avg(FaultRatePercentage) as FaultRatePercentage , \r\n" +
					"	ProductType\r\n" + 
					"from( \r\n" + 
					"  select \r\n" + 
					"  distinct\r\n" + 
					" -- U.CUID,\r\n" + 
					"	   U.AvgSiteUptime as UptimePercentage, \r\n" + 
					"	   U.TotalSiteCount as TotalCount, \r\n" + 
					"	   U.SLABreachYesCount as SLABreachCount, \r\n" + 
					"	   U.SLABreachPercent as SLABreachPercent,\r\n" + 
					"	   avg(F.CustomerServiceFaultMetricsFaultRate) as FaultRatePercentage, \r\n" + 
					"	   U.GRServiceInventoryTargetServiceType as ProductType\r\n" + 
					" from (\r\n" + 
					"	\r\n" + 
					"	select\r\n" + 
					"	T.GRServiceInventoryCUID as CUID,\r\n" + 
					"	Avg(T.SiteUptime) as AvgSiteUptime, Count(T.GRServiceInventoryServiceId) as TotalSiteCount, Sum(T.SLABreachFlagYes) as SLABreachYesCount,\r\n" + 
					"	100*SLABreachYesCount/cast(TotalSiteCount as decimal(8,2)) as SLABreachPercent, T.GRServiceInventoryCUID, T.GRServiceInventoryTargetServiceType,\r\n" + 
					"	T.MonthYear\r\n" + 
					"	from\r\n" + 
					"	(\r\n" + 
					"		Select\r\n" + 
					"			GR.GRServiceInventoryServiceId, GR.GRServiceInventoryCUID, GR.GRServiceInventoryTargetServiceType,\r\n" + 
					"			trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear,\r\n" + 
					"			case when ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 100\r\n" + 
					"			     else CustomerServiceSiteUptimeMetricsSiteUptimePercent end as SiteUptime,\r\n" + 
					"			case when  ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 0\r\n" + 
					"			     when  ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is not null and CustomerServiceSiteUptimeMetricsSLABreachFlag='Y' then 1\r\n" + 
					"			     else 0\r\n" + 
					"			     end as SLABreachFlagYes\r\n" + 
					"		from GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"			inner join GALAXYDWH_360_Security_V.Dim_Party P\r\n" + 
					"				on GR.GRServiceInventoryCUID=P.partyCUID\r\n" + 
					"		left outer join \r\n" + 
					"			GalaxyDWh_360_security_V.RPT_CustomerServiceSiteUptimeMetrics ST\r\n" + 
					"					on (GR.GRServiceInventoryServiceId=ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID\r\n" + 
					"							and GR.GRServiceInventoryCUID=ST.CustomerServiceSiteUptimeMetricsCUID\r\n" + 
					"							and GR.GRServiceInventoryTargetServiceType=ST.CustomerServiceSiteUptimeMetricsServicetype\r\n" + 
					"							and trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate))=ST.CustomerServiceSiteUptimeMetricsMonthYear\r\n" + 
					"						)\r\n" + 
					"		where \r\n" + 
					"		trunc(P.PartyEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"		and P.PartyType='Legal'\r\n" + 
					"		and P.PartySourceIdentifier='Customer GR'\r\n" + 
					"		and GR.GRServiceInventoryCUID in (" + ids +")\r\n" + 
					" 	and \r\n" + 
					"     (\r\n" + 
					"       ( GR.GRServiceInventoryTargetServiceType in ('Global VPN','Internet Access Service','National Private Line')\r\n" + 
					"		and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"		 and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY','SINGLE') \r\n" + 
					"	 and  GR.GRServiceInventoryTargetServiceType not in ('MHS','Managed Security Services','MSS'))\r\n" + 
					"	  or\r\n" + 
					"       (\r\n" + 
					"         GR.GRServiceInventoryTargetServiceType not in ('Global VPN','Internet Access Service','National Private Line','MHS','Managed Security Services','MSS')\r\n" + 
					"        )\r\n" + 
					"      )\r\n" + 
					"		and trunc(GR.GRServiceInventoryAsOnDate)>=to_date('" + dates[0] + "','mm/dd/yyyy')\r\n" + 
					"		and trunc(GR.GRServiceInventoryAsOnDate)<=to_date('"+ dates[1] +"','mm/dd/yyyy')\r\n" + 
					"			) T\r\n" + 
					"	-- where t.monthyear='"+dates[0]+"-"+dates[1]+"'		\r\n" + 
					"		group by \r\n" + 
					"		T.GRServiceInventoryCUID,\r\n" + 
					"		T.GRServiceInventoryTargetServiceType,\r\n" + 
					"		T.MonthYear\r\n" + 
					"	) U\r\n" + 
					"	inner join GALAXYDWH_360_Security_V.RPT_CustomerServiceFaultMetrics F\r\n" + 
					"-- on U.GRServiceInventoryCUID=F.CustomerServiceFaultMetricsCUID\r\n" + 
					"      on U.CUID=F.CustomerServiceFaultMetricsCUID and U.GRServiceInventoryTargetServiceType=F.CustomerServiceFaultMetricsServiceType \r\n" + 
					"         and U.MonthYear= to_char(to_date(case when length(F.CustomerServiceFaultMetricsMonthYear)<5 then '0'||F.CustomerServiceFaultMetricsMonthYear else F.CustomerServiceFaultMetricsMonthYear end, 'mm-yy'),'mm-yyyy') \r\n" + 
					"	group by\r\n" + 
					"	U.AvgSiteUptime, \r\n" + 
					"	U.TotalSiteCount, \r\n" + 
					"	U.SLABreachYesCount, \r\n" + 
					"	U.SLABreachPercent,\r\n" + 
					"	 U.GRServiceInventoryTargetServiceType \r\n" + 
					")X\r\n" + 
					" group by\r\n" + 
					"ProductType";
					LOGGER.info("UptimeFaultSLA Query {} ",query);
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForUptimeTrend(List<String> cuids, List<String> duration) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String months = duration.stream().map(month -> "'" + month + "'").collect(Collectors.joining(","));
		try {
			String query = "select \r\n" + 
					"    M.CustomerServiceUptimeMetricsMonthYear as MonthYear,\r\n" + 
					"	M.CustomerServiceUptimeMetricsServiceType as ProductType,\r\n" + 
					"	AVG(M.CustomerServiceUptimeMetricsCircuitUptimePercent) as UptimePercentage\r\n" + 
					"from \r\n" + 
					"	GALAXYDWH_360_security_V.RPT_CustomerServiceUptimeMetrics M\r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
					"	on C.PartyGlobalid = M.CustomerServiceUptimeMetrics2PartyGlobalId\r\n" + 
					"where \r\n" + 
					"		M.CustomerServiceUptimeMetricsMonthYear in ("+ months + ") \r\n" + 
					"	and C.partycuid in (" + ids + ")\r\n" + 
					"	and M.CustomerServiceUptimeMetricsSourceIdentifier='GALAXY' and ProductType not in ('MHS','MSS','Managed Security Services')\r\n" + 
//					"	and CustomerServiceUptimeMetricsServiceType in ('" + PerformanceConstants.VAL_GVPN + "'," + 
//					" '" + PerformanceConstants.VAL_IAS + "', " + "'" + PerformanceConstants.VAL_NPL + "'" + ")\r\n" + 
					"group by ProductType, MonthYear\r\n" + 
					"order by MonthYear asc";
			
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForSLABreachTrend(List<String> cuids, List<String> duration) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String startDate = getStartDate(duration.get(duration.size()-1));
		String endDate = getEndDate(duration.get(0));

		try {
			String query = "select\r\n" + 
					"	Sum(T.SLABreachFlagYes) as SLABreachCount, GRServiceInventoryTargetServiceType as ProductType, T.MonthYear as MonthYear \r\n" + 
					"from\r\n" + 
					"(\r\n" + 
					"	Select\r\n" + 
					"	GR.GRServiceInventoryServiceId, GR.GRServiceInventoryCUID, GR.GRServiceInventoryTargetServiceType, \r\n" + 
					"	trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear,\r\n" + 
					"	case when ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 100 \r\n" + 
					"	     else CustomerServiceSiteUptimeMetricsSiteUptimePercent end as SiteUptime, \r\n" + 
					"	case when  ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 0 \r\n" + 
					"	     when  ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is not null and CustomerServiceSiteUptimeMetricsSLABreachFlag='Y' then 1 \r\n" + 
					"	     else 0\r\n" + 
					"	     end as SLABreachFlagYes\r\n" + 
					"	from GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"	inner join GALAXYDWH_360_Security_V.Dim_Party P\r\n" + 
					"	on\r\n" + 
					"	GR.GRServiceInventoryCUID=P.partyCUID\r\n" + 
					"	left outer join \r\n" + 
					"	GalaxyDWh_360_security_V.RPT_CustomerServiceSiteUptimeMetrics ST\r\n" + 
					"	on (GR.GRServiceInventoryServiceId=ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID\r\n" + 
					"	and GR.GRServiceInventoryCUID=ST.CustomerServiceSiteUptimeMetricsCUID\r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType=ST.CustomerServiceSiteUptimeMetricsServicetype\r\n" + 
					"	and trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate))=ST.CustomerServiceSiteUptimeMetricsMonthYear\r\n" + 
					"	)\r\n" + 
					"	where \r\n" + 
					"	trunc(P.PartyEffectiveEndDate)=Date '9999-12-31' \r\n" + 
					"	and P.PartyType='Legal'\r\n" + 
					"	and P.PartySourceIdentifier='Customer GR' \r\n" + 
					"	and GR.GRServiceInventoryCUID in (" + ids + ") \r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType in ('" + PerformanceConstants.VAL_GVPN + "', '" + 
										PerformanceConstants.VAL_IAS + "', '" + PerformanceConstants.VAL_NPL + "') \r\n" + 
					"	and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE' \r\n" + 
					"	and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY','SINGLE') \r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)>=to_date('"+ startDate +"','mm/dd/yyyy')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)<=to_date('"+ endDate +"','mm/dd/yyyy')\r\n" + 
					"	and GR.GRServiceInventoryAsOnDate between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-3) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day\r\n" + 
					"	and (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE)) \r\n" + 
					") T \r\n" + 
					"group by MonthYear,ProductType \r\n" + 
					"order by MonthYear";
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForTop5CircuitsWithLeastUptime(List<String> cuids, String month, String product) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String startDate = getStartDate(month);
		String endDate = getEndDate(month);
		try {
			String query = "Select \r\n" + 
					"rank() over (partition by AA.GRServiceInventoryCUID order by AA.SiteUptimePercent) as RNK, \r\n" + 
					"AA.SiteUptimePercent as UptimePercentage, AA.GRServiceInventoryServiceId as CircuitID \r\n" + 
					"\r\n" + 
					"from\r\n" + 
					"(\r\n" + 
					"	Select\r\n" + 
					"	GR.GRServiceInventoryServiceId ,\r\n" + 
					"	GR.GRServiceInventoryServiceLink,\r\n" + 
					"	GR.GRServiceInventoryTargetServiceType,\r\n" + 
					"	GR.GRServiceInventoryAEndSiteAddress,\r\n" + 
					"	GR.GRServiceInventoryCircuitAlias,\r\n" + 
					"	GR.GRServiceInventoryCUID,\r\n" + 
					"	trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear,\r\n" + 
					"	case when ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 100\r\n" + 
					"		 else CustomerServiceSiteUptimeMetricsSiteUptimePercent end as SiteUptimePercent,\r\n" + 
					"	GR.GRServiceInventoryCommittedSLA as CommittedSLA,\r\n" + 
					"	Case when ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 0\r\n" + 
					"		 else ST.CustomerServiceSiteUptimeMetricsDowntimeMinutes end as DowntimeMinutes\r\n" + 
					"\r\n" + 
					"	from GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"	inner join GALAXYDWH_360_Security_V.Dim_Party P\r\n" + 
					"	on\r\n" + 
					"	GR.GRServiceInventoryCUID=P.partyCUID\r\n" + 
					"	left outer join \r\n" + 
					"	GalaxyDWh_360_security_V.RPT_CustomerServiceSiteUptimeMetrics ST\r\n" + 
					"	on (GR.GRServiceInventoryServiceId=ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID\r\n" + 
					"	and GR.GRServiceInventoryCUID=ST.CustomerServiceSiteUptimeMetricsCUID\r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType=ST.CustomerServiceSiteUptimeMetricsServicetype\r\n" + 
					"	and trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate))=ST.CustomerServiceSiteUptimeMetricsMonthYear\r\n" + 
					"	)\r\n" + 
					"	where \r\n" + 
					"	trunc(P.PartyEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"	and P.PartyType='Legal'\r\n" + 
					"	and P.PartySourceIdentifier='Customer GR'\r\n" + 
					"	and GR.GRServiceInventoryCUID in (" + ids + ")\r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType in ('" + product + "')\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY','SINGLE')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)>=to_date('" + startDate + "','mm/dd/yyyy')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)<=to_date('" + endDate +"','mm/dd/yyyy')\r\n" + 
					"	) AA\r\n" + 
					"	left outer join\r\n" + 
					"	GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_HIST  BB\r\n" + 
					"	on  (\r\n" + 
					"	AA.GRServiceInventoryServiceLink=BB.GRServiceInventoryServiceId\r\n" + 
					"	and AA.GRServiceInventoryCUID=BB.GRServiceInventoryCUID\r\n" + 
					"	and AA.GRServiceInventoryTargetServiceType=BB.GRServiceInventoryTargetServiceType\r\n" + 
					"	and AA.MonthYear=trim(extract( month from BB.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from BB.GRServiceInventoryAsOnDate))\r\n" + 
					"	)\r\n" + 
					"	left outer join\r\n" + 
					"	(\r\n" + 
					"	select count(distinct S.TicketNo) as Ticketcount,S.GRServiceInventoryServiceId,S.MonthYear\r\n" + 
					"	from (\r\n" + 
					"	select \r\n" + 
					"	FT.TicketNo,\r\n" + 
					"	GR.GRServiceInventoryServiceId,\r\n" + 
					"	trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear\r\n" + 
					"	from\r\n" + 
					"	GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"	inner join  GALAXYDWH_360_Security_V.Fact_Circuit FC\r\n" + 
					"	on GR.GRServiceInventoryServiceId = FC.Circuitid\r\n" + 
					"	inner join GALAXYDWH_360_Security_V.Fact_Ticket FT\r\n" + 
					"	on FC.CircuitGlobalId = FT.Ticket2CircuitGlobalId\r\n" + 
					"	where  FT.ticketClosedtime between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-1) AS DATE FORMAT 'YYYY-MM-DD')+INTERVAL '1' day\r\n" + 
					"	and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE))\r\n" + 
					"	and FT.Ticketstatus like 'closed%'\r\n" + 
					"	and trunc(FT.TicketEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"	and FT.TicketSourceIdentifier='SNOW'\r\n" + 
					"	and FT.TicketServiceDownEndDate is not null \r\n" + 
					"	and FT.TicketServiceDownStartDate is not null\r\n" + 
					"	and FT.TicketCustomerAcknowledgementFlag='True'\r\n" + 
					"	and trunc(FC.CircuitEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType in ('" + product + "')\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY','SINGLE')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)>=to_date('" + startDate +"','mm/dd/yyyy')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)<=to_date('" + endDate +"','mm/dd/yyyy')\r\n" + 
					"	union\r\n" + 
					"	select \r\n" + 
					"	FT.TicketNo,\r\n" + 
					"	GR.GRServiceInventoryServiceId,\r\n" + 
					"	trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear\r\n" + 
					"	from\r\n" + 
					"	GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"	inner join  GALAXYDWH_360_Security_V.Fact_Circuit FC\r\n" + 
					"	on GR.GRServiceInventoryServiceLink = FC.Circuitid\r\n" + 
					"	inner join GALAXYDWH_360_Security_V.Fact_Ticket FT\r\n" + 
					"	on FC.CircuitGlobalId = FT.Ticket2CircuitGlobalId\r\n" + 
					"	where  FT.ticketClosedtime between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-1) AS DATE FORMAT 'YYYY-MM-DD')+INTERVAL '1' day\r\n" + 
					"	and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE))\r\n" + 
					"	and FT.Ticketstatus like 'closed%'\r\n" + 
					"	and trunc(FT.TicketEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"	and FT.TicketSourceIdentifier='SNOW'\r\n" + 
					"	and FT.TicketServiceDownEndDate is not null \r\n" + 
					"	and FT.TicketServiceDownStartDate is not null\r\n" + 
					"	and FT.TicketCustomerAcknowledgementFlag='True'\r\n" + 
					"	and FT.TicketImpact='Total Loss of Service'\r\n" + 
					"	and trunc(FC.CircuitEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType in ('" + product + "')\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY')\r\n" + 
					"	and GR.GRServiceInventoryServiceLink is NOT NULL\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)>=to_date('" + startDate + "','mm/dd/yyyy')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)<=to_date('" + endDate +"','mm/dd/yyyy')\r\n" + 
					"	) S group by S.GRServiceInventoryServiceId,S.MonthYear\r\n" + 
					") CC\r\n" + 
					"on (CC.GRServiceInventoryServiceId=AA.GRServiceInventoryServiceId\r\n" + 
					"and CC.MonthYear=AA.MonthYear)" +
					"order by RNK asc \r\n" + 
					"qualify row_number() over (order by AA.SiteUptimePercent asc)<6 ";
			LOGGER.debug("TeradataReports: queryForTop5CircuitsWithLeastUptime: {}", query);
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForUptimeCircuitDetails(List<String> cuids, String month, String product) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] dates = getMonthDates(Integer.valueOf(month.split("-")[0]));

		try {
			String query = "Select \r\n" + 
					"	AA.GRServiceInventoryServiceId as CircuitId, AA.GRServiceInventoryServiceLink as ServiceLink, AA.GRServiceInventoryTargetServiceType as ProductType,\r\n" + 
					"	AA.GRServiceInventoryAEndSiteAddress as SiteAddress, AA.GRServiceInventoryCircuitAlias as PrimaryCircuitAlias,\r\n" + 
					"	AA.SiteUptimePercent as UptimePercentage, AA.CommittedSLA as CommittedUptime, BB.GRServiceInventoryCircuitAlias as SecondaryCircuitAlias\r\n" + 
					"from\r\n" + 
					"(\r\n" + 
					"	Select\r\n" + 
					"	GR.GRServiceInventoryServiceId, GR.GRServiceInventoryServiceLink, GR.GRServiceInventoryTargetServiceType,\r\n" + 
					"	GR.GRServiceInventoryAEndSiteAddress, GR.GRServiceInventoryCircuitAlias, GR.GRServiceInventoryCUID,\r\n" + 
					"	trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear,\r\n" + 
					"	case when ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 100\r\n" + 
					"		 else CustomerServiceSiteUptimeMetricsSiteUptimePercent end as SiteUptimePercent,\r\n" + 
					"	GR.GRServiceInventoryCommittedSLA as CommittedSLA\r\n" + 
					"	from GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"	inner join GALAXYDWH_360_Security_V.Dim_Party P\r\n" + 
					"	on\r\n" + 
					"	GR.GRServiceInventoryCUID=P.partyCUID\r\n" + 
					"	left outer join \r\n" + 
					"	GalaxyDWh_360_security_V.RPT_CustomerServiceSiteUptimeMetrics ST\r\n" + 
					"	on (GR.GRServiceInventoryServiceId=ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID\r\n" + 
					"	and GR.GRServiceInventoryCUID=ST.CustomerServiceSiteUptimeMetricsCUID\r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType=ST.CustomerServiceSiteUptimeMetricsServicetype\r\n" + 
					"	and trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate))=ST.CustomerServiceSiteUptimeMetricsMonthYear\r\n" + 
					"	)\r\n" + 
					"	where \r\n" + 
					"	trunc(P.PartyEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"	and P.PartyType='Legal'\r\n" + 
					"	and P.PartySourceIdentifier='Customer GR'\r\n" + 
					"	and GR.GRServiceInventoryCUID in (" + ids + ")\r\n" + 
					"	and GR.GRServiceInventoryTargetServiceType in ('" + product + "')\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"	and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY','SINGLE')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)>=to_date('"+ dates[0] +"','mm/dd/yyyy')\r\n" + 
					"	and trunc(GR.GRServiceInventoryAsOnDate)<=to_date('"+ dates[1] +"','mm/dd/yyyy')\r\n" + 
					"	and GR.GRServiceInventoryAsOnDate between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-5) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day\r\n" + 
					"	and (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE))\r\n" + 
					"	) AA\r\n" + 
					"left outer join\r\n" + 
					"GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_HIST  BB\r\n" + 
					"on (\r\n" + 
					"AA.GRServiceInventoryServiceLink=BB.GRServiceInventoryServiceId\r\n" + 
					"and AA.GRServiceInventoryCUID=BB.GRServiceInventoryCUID\r\n" + 
					"and AA.GRServiceInventoryTargetServiceType=BB.GRServiceInventoryTargetServiceType\r\n" + 
					"and AA.MonthYear=trim(extract( month from BB.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from BB.GRServiceInventoryAsOnDate)))";
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForMTTRTrend(List<String> cuids, String ticketResponsible) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
			return teradataJdbcTemplate.queryForList("select \r\n" + 
					"    to_char(to_date(case when length(RFOTicketImpactMTTRTrendMonthYear)<5 then '0'|| RFOTicketImpactMTTRTrendMonthYear\r\n" + 
					"    else RFOTicketImpactMTTRTrendMonthYear end, 'mm-yy'),'yyyy-mm') as MonthYear, \r\n" + 
					"    AVG(RFOTicketImpactMTTRTrendAVGMTTR) as AvgMTTR \r\n" + 
					"from \r\n" + 
					"    GalaxyDWh_360_Security_V.RPT_RFOTicketImpactMTTRTrend M \r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C \r\n" + 
					"    on C.PartyGlobalid = M.RFOTicketImpactMTTRTrend2PartyGlobalId \r\n" + 
					"where \r\n" + 
					"    to_date(case when length(RFOTicketImpactMTTRTrendMonthYear)<5 then '0'|| RFOTicketImpactMTTRTrendMonthYear \r\n" + 
					"    else RFOTicketImpactMTTRTrendMonthYear end, 'mm-yy')  between \r\n" + 
					"    CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-6) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day \r\n" + 
					"    and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE)) \r\n" + 
					"    and RFOTicketImpactMTTRTrendpartyCUID in (" + ids + ") \r\n" + 
					"	and M.RFOTicketImpactMTTRTrendTicketRFOResponsible='" + ticketResponsible +"' \r\n" + 
					"group by \r\n" + 
					"	MonthYear\r\n" + 
					"order by \r\n" + 
					"	MonthYear asc");
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	/**
	 * Method to get MTTR trend for RFO responsible in customer,tata, and
	 * unidentified.
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param ticketResponsible
	 * @param range
	 * @return list of {@HashMap}
	 * @throws TclCommonException
	 */
 public List<Map<String, Object>> queryForMTTRTrendForPDF(List<String> cuids, String ticketResponsible,String startDate,String endDate) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
			
		/*	String query = "select\r\n" + 
					"distinct to_char(to_date(case when length(TicketMTTRTrendMonthYear)<5 then '0'|| TicketMTTRTrendMonthYear\r\n" + 
					"    else TicketMTTRTrendMonthYear end, 'mm-yy'),'mm-yyyy') as MonthYear,\r\n" + 
					"TicketMTTRTrendTicketRFOResponsible,\r\n" + 
					"TicketMTTRTrendAVGMTTR as AvgMTTR\r\n" + 
					"from \r\n" + 
					"GalaxyDWh_360_Security_V.RPT_TicketMTTRTrend\r\n" + 
					"where TicketMTTRTrendpartyCUID in ("+ ids +")\r\n" + 
					"and TicketMTTRTrendTicketRFOResponsible in ('Customer','TATA','Unidentified')" + 
					"and to_date(case when length(TicketMTTRTrendMonthYear)<5 then '0'|| TicketMTTRTrendMonthYear \r\n" + 
					"else TicketMTTRTrendMonthYear end, 'mm-yy') between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-"+ range+") \r\n" + 
					"AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day and (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE)) order by MonthYear asc"; */
			
			String query = "select\r\n" + 
					"TicketMTTRTrendpartyCUID,\r\n" + 
					"to_char(to_date(case when length(TicketMTTRTrendMonthYear)<5 then '0'|| TicketMTTRTrendMonthYear\r\n" + 
					"    else TicketMTTRTrendMonthYear end, 'mm-yy'),'mm-yyyy') as MonthYear,\r\n" + 
					"to_date(case when length(TicketMTTRTrendMonthYear)<5 then '0'|| TicketMTTRTrendMonthYear\r\n" + 
					"    else TicketMTTRTrendMonthYear end, 'mm-yy') as MMYY,\r\n" + 
					"TicketMTTRTrendTicketRFOResponsible,\r\n" + 
					"TicketMTTRTrendAVGMTTR as AvgMTTR\r\n" + 
					"from \r\n" + 
					"GalaxyDWh_360_Security_V.RPT_TicketMTTRTrend\r\n" + 
					"where TicketMTTRTrendpartyCUID in ("+ ids +") and \r\n" + 
					" TicketMTTRTrendTicketRFOResponsible in ('Customer','TATA','Unidentified')and \r\n" + 
					"\r\n" + 
					"MMYY between to_date("+ startDate +",'mm/dd/yyyy') and to_date("+ endDate+",'mm/dd/yyyy')\r\n" + 
					"order by TicketMTTRTrendpartyCUID,MMYY asc;";
			
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
	
	public List<Map<String, Object>> queryForMTTRSeverityWise(List<String> cuids, String impact) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
			return teradataJdbcTemplate.queryForList("select\r\n" + 
					"	to_char(to_date(case when length(TicketMTTRMonthYear)<5 then '0'|| TicketMTTRMonthYear\r\n" + 
					"   else TicketMTTRMonthYear end, 'mm-yy'),'yyyy-mm') as MonthYear,\r\n" + 
					"	TicketMTTRHourBucket as HourBucket,\r\n" + 
					"	SUM(TicketMTTRTicketCount) as TktCount\r\n" + 
					"from\r\n" + 
					"	GalaxyDWh_360_Security_V.RPT_TicketMTTR M \r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C \r\n" + 
					"    on C.PartyGlobalid = M.TicketMTTR2PartyGlobalId \r\n" + 
					"where \r\n" + 
					"	to_date(case when length(TicketMTTRMonthYear)<5 then '0'|| TicketMTTRMonthYear \r\n" + 
					"    else TicketMTTRMonthYear end, 'mm-yy')  between \r\n" + 
					"    CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-6) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day \r\n" + 
					"    and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE)) \r\n" + 
					"    and C.partycuid in (" + ids + ") \r\n" + 
					"	and M.TicketMTTRTicketImpact = '" + impact + "' \r\n" + 
					"group by MonthYear, HourBucket\r\n" + 
					"order by MonthYear desc, HourBucket asc");
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex, ResponseResource.R_CODE_ERROR);	
		}
	}
 
	/**
	 * Method to get MTTR data severity wise for given dates
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param impact
	 * @param startDate string
	 * @param endDate   string
	 * @return List {@Map}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> queryForMTTRSeverityWisePDF(List<String> cuids, String impact, String startDate,
			String endDate) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {

			String query = "select distinct \r\n" + "TicketMTTRpartyCUID,\r\n"
					+ "to_char(to_date(case when length(TicketMTTRMonthYear)<5 then '0'|| TicketMTTRMonthYear\r\n"
					+ "    else TicketMTTRMonthYear end, 'mm-yy'),'mm-yyyy') as MonthYear,\r\n"
					+ "to_date(case when length(TicketMTTRMonthYear)<5 then '0'|| TicketMTTRMonthYear\r\n"
					+ "    else TicketMTTRMonthYear end, 'mm-yy') as MMYY,\r\n"
					+ "TicketMTTRTicketCount as TktCount,\r\n" + "TicketMTTRTicketImpact,\r\n"
					+ "case when TicketMTTRHourBucket='>8h' then '8-n' else TicketMTTRHourBucket end Hourbucket\r\n"
					+ "from\r\n" + "GalaxyDWh_360_Security_V.RPT_TicketMTTR\r\n" + "where MMYY  between to_date("
					+ startDate + ",'mm/dd/yyyy') and to_date(" + endDate + ",'mm/dd/yyyy')\r\n"
					+ "and TicketMTTRpartyCUID in (" + ids + ")\r\n" + "and TicketMTTRTicketImpact ='" + impact
					+ "'\r\n" + "order by MonthYear asc,HourBucket asc";
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}
	
	private String[] getMonthDates(Integer givenMonth) {
		Calendar now = Calendar.getInstance();
		Integer year = now.get(Calendar.YEAR);
		Integer currentMonth = now.get(Calendar.MONTH) + 1;
		if (givenMonth > currentMonth)
			year = (now.get(Calendar.YEAR)) - 1;

		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.MONTH, givenMonth - 1);
		calender.set(Calendar.DAY_OF_MONTH, calender.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date startDate = calender.getTime();
		calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = calender.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return new String[]{sdf.format(startDate), sdf.format(endDate)};
	}

	private String getEndDate(String monthYear) {
		String[] dates = getMonthDates(Integer.valueOf(monthYear.split("-")[0]));
		return dates[1];
	}

	private String getStartDate(String monthYear) {
		String[] dates = getMonthDates(Integer.valueOf(monthYear.split("-")[0]));
		return dates[0];		
	}

	/**
	 * Method to get MTTR severity TATA/CUSTOMER
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param impact
	 * @param owner
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> queryForMTTRSeverityWithTataOrCustImpact(List<String> cuids, String impact,
			String owner,String startDate,String endDate) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
			
		/*OLD Query --->	
		                   String query = "select\r\n"
					+ "    to_char(to_date(case when length(TicketMTTRMonthYear)<5 then '0'|| TicketMTTRMonthYear\r\n"
					+ "    else TicketMTTRMonthYear end, 'mm-yy'),'yyyy-mm') as MonthYear,\r\n"
					+ "    TicketMTTRHourBucket as HourBucket,\r\n" + "    SUM(TicketMTTRTicketCount) as TktCount,\r\n"
					+ "    TicketMTTRTrendTicketRFOResponsible as resp\r\n" + "from\r\n"
					+ "    GalaxyDWh_360_Security_V.RPT_TicketMTTR M\r\n"
					+ "inner join GALAXYDWH_360_security_V.Dim_Party C\r\n"
					+ "    on C.PartyGlobalid = M.TicketMTTR2PartyGlobalId\r\n"
					+ "inner join GalaxyDWh_360_Security_V.RPT_TicketMTTRTrend MT\r\n"
					+ "    on M.TicketMTTR2PartyGlobalId = MT.TicketMTTRTrend2PartyGlobalId\r\n" + "where \r\n"
					+ "    to_date(case when length(TicketMTTRMonthYear)<5 then '0'|| TicketMTTRMonthYear\r\n"
					+ "    else TicketMTTRMonthYear end, 'mm-yy')  between \r\n"
					+ "    CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-6) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day\r\n"
					+ "    and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE))\r\n" + "    and C.partycuid in (" + ids
					+ ")\r\n" + "	and M.TicketMTTRTicketImpact = '" + impact + "' \r\n"
					+ "    and MT.TicketMTTRTrendTicketRFOResponsible in(" + owner + ")\r\n"
					+ "group by MonthYear, HourBucket, resp\r\n" + "order by MonthYear desc, HourBucket asc\r\n" + ""; */
			
			String query = "select distinct\r\n" + 
					"RFOTicketMTTRpartyCUID,\r\n" + 
					"case when RFOTicketMTTRHourBucket='>8h' then '8-n' else RFOTicketMTTRHourBucket end Hourbucket,\r\n" + 
					"to_char(to_date(case when length(RFOTicketMTTRMonthYear)<5 then '0'|| RFOTicketMTTRMonthYear\r\n" + 
					"    else RFOTicketMTTRMonthYear end, 'mm-yy'),'mm-yyyy') as MonthYear,\r\n" + 
					"to_date(case when length(RFOTicketMTTRMonthYear)<5 then '0'|| RFOTicketMTTRMonthYear\r\n" + 
					"    else RFOTicketMTTRMonthYear end, 'mm-yy') as MMYY,\r\n" + 
					"RFOTicketMTTRTicketRFOResponsible,\r\n" + 
					"RFOTicketMTTRTicketImpact,\r\n" + 
					"RFOTicketMTTRTicketCount as TktCount\r\n" + 
					"from \r\n" + 
					"GalaxyDWh_360_Security_V.RPT_RFOTicketMTTR\r\n" + 
					"where RFOTicketMTTRTicketRFOResponsible=" + owner + "\r\n" + 
					"and RFOTicketMTTRTicketImpact = '"+ impact +"' \r\n" + 
					"and MMYY  between to_date("+ startDate + ",'mm/dd/yyyy') and to_date(" + endDate + ",'mm/dd/yyyy')\r\n" + 
					"and RFOTicketMTTRpartyCUID in ("+ ids+")\r\n" + 
					"order by MonthYear asc,HourBucket asc;";
			
			
			return teradataJdbcTemplate.queryForList(query);
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to get MTTR Trend RFO wise
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param rfoResponsible
	 * @param impact
	 * @return @{Map}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> queryForMTTRTrendForRFOResponsiblility(List<String> cuids, String rfoResponsible,
			String impact,String startDate,String endDate) throws TclCommonException {

		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));

		/*
		 *    OLD QUERY 
		 * String query = "select\r\n" + "RFOTicketImpactMTTRTrendpartyCUID, \r\n"
				+ "RFOTicketImpactMTTRTrendMonthYear,\r\n" + "RFOTicketImpactMTTRTrendTicketRFOResponsible,\r\n"
				+ "RFOTicketImpactMTTRTrendTicketImpact,\r\n" + "RFOTicketImpactMTTRTrendAVGMTTR\r\n" + "from\r\n"
				+ "GalaxyDWh_360_Security_V.RPT_RFOTicketImpactMTTRTrend\r\n"
				+ "where RFOTicketImpactMTTRTrendTicketRFOResponsible=" + rfoResponsible + "\r\n"
				+ "    and RFOTicketImpactMTTRTrendpartyCUID in (" + ids + ") \r\n"
				+ "    and RFOTicketImpactMTTRTrendTicketImpact = " + impact + " \r\n" + "order by \r\n"
				+ "	RFOTicketImpactMTTRTrendMonthYear desc"; */
		
		String query = "select distinct\r\n" + 
				"RFOTicketImpactMTTRTrendpartyCUID, \r\n" + 
				"to_char(to_date(case when length(RFOTicketImpactMTTRTrendMonthYear)<5 then '0'|| RFOTicketImpactMTTRTrendMonthYear\r\n" + 
				"    else RFOTicketImpactMTTRTrendMonthYear end, 'mm-yy'),'mm-yyyy') as RFOTicketImpactMTTRTrendMonthYear,\r\n" + 
				"to_date(case when length(RFOTicketImpactMTTRTrendMonthYear)<5 then '0'|| RFOTicketImpactMTTRTrendMonthYear\r\n" + 
				"    else RFOTicketImpactMTTRTrendMonthYear end, 'mm-yy') as MMYY,\r\n" + 
				"RFOTicketImpactMTTRTrendTicketRFOResponsible,\r\n" + 
				"RFOTicketImpactMTTRTrendTicketImpact,\r\n" + 
				"RFOTicketImpactMTTRTrendAVGMTTR\r\n" + 
				"from\r\n" + 
				"GalaxyDWh_360_Security_V.RPT_RFOTicketImpactMTTRTrend\r\n" + 
				"where RFOTicketImpactMTTRTrendTicketRFOResponsible="+ rfoResponsible +"\r\n" + 
				"and RFOTicketImpactMTTRTrendTicketImpact in ( "+impact+")\r\n" + 
				"----Pass day one( Ex: 01/01/2019 for Jan 2019) for the required month and year\r\n" + 
				"and MMYY between to_date("+startDate+",'mm/dd/yyyy') and to_date("+endDate+",'mm/dd/yyyy')\r\n" + 
				"and RFOTicketImpactMTTRTrendpartyCUID in (" + ids + ")\r\n" + 
				"order by RFOTicketImpactMTTRTrendpartyCUID,MMYY asc";
		
		
		return teradataJdbcTemplate.queryForList(query);
	}

	/**
	 * Method to get Fault Rate
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param month
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public List<FaultRateBean> queryForFaultrate(List<String> cuids, String month) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
			String query = "select distinct \r\n" + "CustomerServiceFaultMetricsCUID,\r\n"
					+ "CustomerServiceFaultMetricsServiceType,\r\n" + "CustomerServiceFaultMetricsMonthYear, \r\n"
					+ "CustomerServiceFaultMetricsTotalCircuitCount,\r\n"
					+ "CustomerServiceFaultMetricsImpactedCircuitCount,\r\n"
					+ "CustomerServiceFaultMetricsFaultRate\r\n" + "from\r\n"
					+ "GalaxyDWh_360_Security_V.RPT_CustomerServiceFaultMetrics\r\n"
					+ "where CustomerServiceFaultMetricsCUID in (" + ids + ") \r\n"
					+ "and CustomerServiceFaultMetricsServiceType in ('" + PerformanceConstants.VAL_GVPN + "'," + " '"
					+ PerformanceConstants.VAL_IAS + "', " + "'" + PerformanceConstants.VAL_NPL + "'," +

					"'" + PerformanceConstants.GVPN + "'," + "'" + PerformanceConstants.IAS + "'," + "'"
					+ PerformanceConstants.NPL + "'" + ")\r\n" + "and CustomerServiceFaultMetricsMonthYear =" + month
					+ " \r\n" + "order by CustomerServiceFaultMetricsCUID,CustomerServiceFaultMetricsServiceType";

			return (teradataJdbcTemplate.query(query, new BeanPropertyRowMapper(FaultRateBean.class)));
		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_UPTIME_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to get ticket trend severity RFO wise
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param rfoResponsible
	 * @return @{Map}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> queryForTrendSeverityWiseWrtRFO(List<String> cuids, String rfoResponsible,String startDate,String endDate)
			throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
		/*	String query = "Select \r\n"
					+ "   extract(month from ticketClosedtime)||'-'||trim(extract(year from ticketClosedtime)) as MonthYear, \r\n"
					+ "	B.TicketImpact as Impact, count(B.TicketNo) as TktCount \r\n" + "from \r\n"
					+ "	GALAXYDWH_360_security_V.Fact_Ticket B \r\n"
					+ "inner join GALAXYDWH_360_security_V.Dim_Party C \r\n"
					+ "	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n"
					+ "where  ticketClosedtime between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-6) AS DATE FORMAT 'YYYY-MM-DD') + INTERVAL '1' day\r\n"
					+ "	and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE)) \r\n"
					+ "	and Ticketstatus like 'closed%' \r\n" + "	and partycuid in (" + ids + " )\r\n"
					+ "	and PartyType='Legal' \r\n" + "	and Tickettasktype in ('Case','Incident') \r\n"
					+ "	and B.TicketCustomerAcknowledgementFlag='True' \r\n"
					+ "	and B.TicketSourceIdentifier='SNOW' \r\n" + "and B.TicketRFOResponsible in(" + rfoResponsible
					+ ") \r\n" +

					"group by Impact, MonthYear\r\n" + "order by MonthYear"; */
			
			
			String query ="Select \r\n" + 
					"PartyCUID, count(B.TicketNo) as TktCount,B.TicketImpact as Impact,ticketClosedtime (format 'mm-yyyy') (char(7)) as MMYYYY,\r\n" + 
					"cast(extract(month from ticketClosedtime)||trim(extract(year from ticketClosedtime)) as date format 'mmyyyy') as MonthYear\r\n" + 
					"\r\n" + 
					"from \r\n" + 
					"GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + 
					"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
					"on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + 
					"where ticketClosedtime between to_date("+ startDate+",'mm/dd/yyyy')\r\n" + 
					"and  to_date("+ endDate+",'mm/dd/yyyy') "+
					"and Ticketstatus like 'closed%'\r\n" + 
					"and partycuid in (" + ids +")\r\n" + 
					"--and  trunc(PartyEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"and PartyType='Legal'\r\n" + 
					"and Tickettasktype in ('Case','Incident')\r\n" + 
					"and B.TicketCustomerAcknowledgementFlag='True'\r\n" + 
					"--and trunc(B.TicketEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"and B.TicketSourceIdentifier='SNOW'\r\n" + 
					"and B.TicketRFOResponsible in ("+ rfoResponsible +") "+  
					"group by  PartyCUID, Impact,MonthYear,MMYYYY\r\n" + 
					"order by MonthYear";
			
			
			return teradataJdbcTemplate.queryForList(query);

		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * A method to get outage reasons for customer End
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param month
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> queryForOutageReasonsCustomerEnd(List<String> cuids, String month)
			throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");
		String query = "Select \r\n" + "	B.TicketRFOSpecification, count(B.TicketNo) as TktCount \r\n" + "from \r\n"
				+ "	GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + "inner join GALAXYDWH_360_security_V.Dim_Party C\r\n"
				+ "	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + "where \r\n"
				+ "	extract(month from ticketClosedtime) = " + Integer.valueOf(arr[0]) + "\r\n"
				+ "	and extract(year from ticketClosedtime) = " + Integer.valueOf(arr[1]) + "\r\n"
				+ "	and Ticketstatus like 'closed%'\r\n" + "	and partycuid in (" + ids + ")\r\n"
				+ "	and PartyType='Legal'\r\n" +

				"and B.TicketRFOResponsible in ('Customer')\r \n " +

				"	and Tickettasktype in ('Case','Incident')\r\n"
				+ "	and B.TicketCustomerAcknowledgementFlag='True'\r\n" + "	and B.TicketSourceIdentifier='SNOW'\r\n"
				+ "group by B.TicketRFOSpecification \r\n" + "order by TktCount desc";
		try {
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * A method to get outage reasons other than customer End.
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param month
	 * @return {@Map}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> queryForOutageReasonsOtherThanCustomerEnd(List<String> cuids, String month)
			throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");
		String query = "Select \r\n" + "	B.TicketRFOSpecification, count(B.TicketNo) as TktCount \r\n" + "from \r\n"
				+ "	GALAXYDWH_360_security_V.Fact_Ticket B\r\n" + "inner join GALAXYDWH_360_security_V.Dim_Party C\r\n"
				+ "	on C.PartyGlobalid = B.Ticket2PartyGlobalId \r\n" + "where \r\n"
				+ "	extract(month from ticketClosedtime) = " + Integer.valueOf(arr[0]) + "\r\n"
				+ "	and extract(year from ticketClosedtime) = " + Integer.valueOf(arr[1]) + "\r\n"
				+ "	and Ticketstatus like 'closed%'\r\n" + "	and partycuid in (" + ids + ")\r\n"
				+ "	and PartyType='Legal'\r\n" +

				"and B.TicketRFOResponsible <>'Customer' \r \n " +

				"	and Tickettasktype in ('Case','Incident')\r\n"
				+ "	and B.TicketCustomerAcknowledgementFlag='True'\r\n" + "	and B.TicketSourceIdentifier='SNOW'\r\n"
				+ "group by B.TicketRFOSpecification \r\n" + "order by TktCount desc";
		try {
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_RFO_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	/**
	 * A method to query trend wise severity for given month range
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param range
	 * @return {@List}
	 * @throws TclCommonException
	 */
	public List<Map<String, Object>> queryForTrendSeverityWiseForGivenMonthRange(List<String> cuids, String startDate,String endDate)
			throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		try {
			
			
			String query = "Select distinct  \r\n" + "                    partyCUID,count(B.TicketNo) as TktCount,\r\n"
					+ "					B.TicketImpact as Impact,\r\n"
					+ "					ticketCreationdate (format 'mm-yyyy') (char(7)) as MMYYYY,\r\n"
					+ "					--Ticketstatus,\r\n"
					+ "                    cast(extract(month from ticketCreationdate)||trim(extract(year from ticketCreationdate)) as date format 'mmyyyy') as Monthyear \r\n"
					+ "                     \r\n" + "from  \r\n"
					+ "                    GALAXYDWH_360_security_V.Fact_Ticket B \r\n"
					+ "                    	inner join GALAXYDWH_360_security_V.Dim_Party C \r\n"
					+ "                    		on C.PartyGlobalid = B.Ticket2PartyGlobalId  \r\n"
					+ "                    	inner join GALAXYDWH_360_security_V.Fact_Circuit A \r\n"
					+ "                    		on A.CircuitGlobalId = B.Ticket2CircuitGlobalId \r\n"
					+ "                    	inner join GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist\r\n"
					+ "                    		on (GRServiceInventoryServiceId = A.Circuitid \r\n"
					+ "                   \r\n"
					+ "				   	and trim(extract( month from GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GRServiceInventoryAsOnDate))= \r\n"
					+ "        		    trim(extract( month from ticketCreationdate) (format'99'))||'-'||trim(extract( year from ticketCreationdate)) \r\n"
					+ "                    ) \r\n" + "                  where  \r\n"
					+ "				  		ticketCreationDate between to_date(" + startDate + ",'mm/dd/yyyy') \r\n"
					+ "                    and to_date(" + endDate + ",'mm/dd/yyyy') \r\n"
					+ "                    and partycuid in (" + ids + ") \r\n"
					+ "                    and  trunc(PartyEffectiveEndDate)=Date '9999-12-31' \r\n"
					+ "                    and PartyType='Legal' \r\n"
					+ "                    and Tickettasktype in ('Case','Incident') \r\n"
					+ "                    and B.TicketCustomerAcknowledgementFlag='True' \r\n"
					+ "                    and B.TicketSourceIdentifier='SNOW' \r\n"
					+ "                    and  GRServiceInventoryAsOnDate between to_date(" + startDate
					+ ",'mm/dd/yyyy') \r\n" + "                    and  to_date(" + endDate + ",'mm/dd/yyyy') \r\n"
					+ "					and Ticketstatus not in('Cancelled','Suspended')\r\n"
					+ "				group by  partyCUID,Impact,MonthYear,MMYYYY \r\n"
					+ "                order by MonthYear;";
			return teradataJdbcTemplate.queryForList(query);
		} catch (Exception ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_SEVERITY_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to query TeraData to get SLA Breach for given month.
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param diff
	 * @return list of SLABreachBean
	 * @throws TclCommonException
	 */
	public List<SLABreachBean> querySLABreachCases(List<String> cuids, String month) throws TclCommonException {
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));

		try {
			String query = "\r\n" + 
					"Select \r\n" + 
					"AA.GRServiceInventoryCUID,\r\n" + 
					"AA.GRServiceInventoryServiceId ,\r\n" + 
					"AA.GRServiceInventoryServiceLink,\r\n" + 
					"AA.GRServiceInventoryTargetServiceType,\r\n" + 
					"AA.GRServiceInventoryAEndSiteAddress,\r\n" + 
					"AA.GRServiceInventoryCircuitAlias as GRServiceInventoryPrimaryCircuitAlias,\r\n" + 
					"AA.SiteUptimePercent,\r\n" + 
					"AA.CommittedSLA,\r\n" + 
					"BB.GRServiceInventoryCircuitAlias as GRServiceInventorySecondaryCircuitAlias,\r\n" + 
					"AA.MonthYear,\r\n" + 
					"AA.Downtimeminutes as UpDowntimeMinutes,\r\n" + 
					"coalesce(CC.Ticketcount,0) as Ticketcount\r\n" + 
					"\r\n" + 
					"from\r\n" + 
					"(\r\n" + 
					"Select\r\n" + 
					"GR.GRServiceInventoryServiceId ,\r\n" + 
					"GR.GRServiceInventoryServiceLink,\r\n" + 
					"GR.GRServiceInventoryTargetServiceType,\r\n" + 
					"GR.GRServiceInventoryAEndSiteAddress,\r\n" + 
					"GR.GRServiceInventoryCircuitAlias,\r\n" + 
					"GR.GRServiceInventoryCUID,\r\n" + 
					"trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear,\r\n" + 
					"case when ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 100\r\n" + 
					"     else CustomerServiceSiteUptimeMetricsSiteUptimePercent end as SiteUptimePercent,\r\n" + 
					"GR.GRServiceInventoryCommittedSLA as CommittedSLA,\r\n" + 
					"Case when ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID is null then 0\r\n" + 
					"     else ST.CustomerServiceSiteUptimeMetricsDowntimeMinutes end as DowntimeMinutes\r\n" + 
					"\r\n" + 
					"from GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"inner join GALAXYDWH_360_Security_V.Dim_Party P\r\n" + 
					"on\r\n" + 
					"GR.GRServiceInventoryCUID=P.partyCUID\r\n" + 
					"left outer join \r\n" + 
					"GalaxyDWh_360_security_V.RPT_CustomerServiceSiteUptimeMetrics ST\r\n" + 
					"on (GR.GRServiceInventoryServiceId=ST.CustomerServiceSiteUptimeMetricsPrimaryCircuitID\r\n" + 
					"and GR.GRServiceInventoryCUID=ST.CustomerServiceSiteUptimeMetricsCUID\r\n" + 
					"and GR.GRServiceInventoryTargetServiceType=ST.CustomerServiceSiteUptimeMetricsServicetype\r\n" + 
					"and trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate))=ST.CustomerServiceSiteUptimeMetricsMonthYear\r\n" + 
					")\r\n" + 
					"where \r\n" + 
					"trunc(P.PartyEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"and P.PartyType='Legal'\r\n" + 
					"and P.PartySourceIdentifier='Customer GR'\r\n" + 
					"and GR.GRServiceInventoryTargetServiceType in ('Global VPN','Internet Access Service','National Private Line')\r\n" + 
					"and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY','SINGLE')\r\n" + 
					"\r\n" + 
					") AA\r\n" + 
					"left outer join\r\n" + 
					"GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_HIST  BB\r\n" + 
					"on  (\r\n" + 
					"AA.GRServiceInventoryServiceLink=BB.GRServiceInventoryServiceId\r\n" + 
					"and AA.GRServiceInventoryCUID=BB.GRServiceInventoryCUID\r\n" + 
					"and AA.GRServiceInventoryTargetServiceType=BB.GRServiceInventoryTargetServiceType\r\n" + 
					"and AA.MonthYear=trim(extract( month from BB.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from BB.GRServiceInventoryAsOnDate))\r\n" + 
					")\r\n" + 
					"left outer join\r\n" + 
					"(\r\n" + 
					"select count(distinct S.TicketNo) as Ticketcount,S.GRServiceInventoryServiceId,S.MonthYear\r\n" + 
					"from (\r\n" + 
					"select \r\n" + 
					"FT.TicketNo,\r\n" + 
					"GR.GRServiceInventoryServiceId,\r\n" + 
					"trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear\r\n" + 
					"\r\n" + 
					"from\r\n" + 
					"GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"inner join  GALAXYDWH_360_Security_V.Fact_Circuit FC\r\n" + 
					"on GR.GRServiceInventoryServiceId = FC.Circuitid\r\n" + 
					"inner join GALAXYDWH_360_Security_V.Fact_Ticket FT\r\n" + 
					"on FC.CircuitGlobalId = FT.Ticket2CircuitGlobalId\r\n" + 
					"\r\n" + 
					"where  \r\n" + 
					"\r\n" + 
					" FT.Ticketstatus like 'closed%'\r\n" + 
					"and trunc(FT.TicketEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"and FT.TicketSourceIdentifier='SNOW'\r\n" + 
					"and FT.TicketServiceDownEndDate is not null \r\n" + 
					"and FT.TicketServiceDownStartDate is not null\r\n" + 
					"and FT.TicketCustomerAcknowledgementFlag='True'\r\n" + 
					"and FT.TicketImpact='Total Loss of Service'\r\n" + 
					"and trunc(FC.CircuitEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"and GR.GRServiceInventoryTargetServiceType in ('Global VPN','Internet Access Service','National Private Line')\r\n" + 
					"and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY','SINGLE')\r\n" + 
					"\r\n" + 
					"union\r\n" + 
					"select \r\n" + 
					"FT.TicketNo,\r\n" + 
					"GR.GRServiceInventoryServiceId,\r\n" + 
					"trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear\r\n" + 
					"\r\n" + 
					"from\r\n" + 
					"GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
					"inner join  GALAXYDWH_360_Security_V.Fact_Circuit FC\r\n" + 
					"on GR.GRServiceInventoryServiceLink = FC.Circuitid\r\n" + 
					"inner join GALAXYDWH_360_Security_V.Fact_Ticket FT\r\n" + 
					"on FC.CircuitGlobalId = FT.Ticket2CircuitGlobalId\r\n" + 
					"\r\n" + 
					"where  FT.ticketClosedtime between CAST( ADD_MONTHS(CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE),-1) AS DATE FORMAT 'YYYY-MM-DD')+INTERVAL '1' day\r\n" + 
					"and  (CURRENT_DATE-EXTRACT(DAY FROM CURRENT_DATE))\r\n" + 
					"and FT.Ticketstatus like 'closed%'\r\n" + 
					"and trunc(FT.TicketEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"and FT.TicketSourceIdentifier='SNOW'\r\n" + 
					"and FT.TicketServiceDownEndDate is not null \r\n" + 
					"and FT.TicketServiceDownStartDate is not null\r\n" + 
					"and FT.TicketCustomerAcknowledgementFlag='True'\r\n" + 
					"and FT.TicketImpact='Total Loss of Service'\r\n" + 
					"and trunc(FC.CircuitEffectiveEndDate)=Date '9999-12-31'\r\n" + 
					"and GR.GRServiceInventoryTargetServiceType in ('Global VPN','Internet Access Service','National Private Line')\r\n" + 
					"and upper(trim(GR.GRServiceInventoryCircuitStatus))='ACTIVE'\r\n" + 
					"and upper(trim(GR.GRServiceInventoryPrisec))  in ('PRIMARY')\r\n" + 
					"and GR.GRServiceInventoryServiceLink is NOT NULL\r\n" + 
					"\r\n" + 
					") S group by S.GRServiceInventoryServiceId,S.MonthYear\r\n" + 
					") CC\r\n" + 
					"on (CC.GRServiceInventoryServiceId=AA.GRServiceInventoryServiceId\r\n" + 
					"and CC.MonthYear=AA.MonthYear)\r\n" + 
					"where AA.MonthYear="+ month +"\r\n" + 
					"and AA.GRServiceInventoryCUID in ("+ ids +")\r\n" + 
					"order by AA.GRServiceInventoryCUID";

			return (teradataJdbcTemplate.query(query, new BeanPropertyRowMapper(SLABreachBean.class)));

		} catch (DataAccessException ex) {
			LOGGER.error(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex);
			throw new TclCommonException(ExceptionConstants.ERROR_IN_GETTING_MTTR_QUERY_RESPONSE, ex,
					ResponseResource.R_CODE_ERROR);
		}
	}
	

	/**
	 * Method to query teraData DB to get All ticket details for supplied CUID
	 * 
	 * @author KRUTSRIN
	 * @param cuids
	 * @param month string in MM-YYYY format
	 * @return list of {@TicketExcelBean}
	 */
public List<TicketExcelBean> getTicketExcel(List<String> cuids,String month) {
		
		String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
		String[] arr = month.split("-");


		String query = 
				"select distinct \r\n" + 
				"C.PartyName as legalEntity,\r\n" + 
				"TicketNo as ticketNumber,\r\n" + 
				"TicketInformationSource as informationSource,\r\n" + 
				"GRServiceInventoryCustomerServiceId as customerServiceId, \r\n" + 
				"TicketImpact as impact,\r\n" + 
				"TicketStatus as status,\r\n" + 
				"TicketDownTimeDDDHHMMSS as serviceDownTime,\r\n" + 
				"TicketRFOResponsible as rfoResponsible,\r\n" + 
				"TicketReasonOutage as rfoCause,\r\n" + 
				"TicketCloseNotes as rfoComments,\r\n" + 
				"TicketRFOSpecification as rfoSpecification,\r\n" + 
				"\r\n" + 
				"TicketCreationDate as created,\r\n" + 
				"TicketLastResolvedDate as resolved,\r\n" + 
				"TicketOutageDurationMin as sumOfTotalOutageDuration,\r\n" + 
				"TicketClosedTime as closed \r\n "+
				"from \r\n" + 
				"GALAXYDWH_360_security_V.Fact_Ticket \r\n" + 
				"inner join GALAXYDWH_360_security_V.Dim_Party C\r\n" + 
				"on C.PartyGlobalid = Ticket2PartyGlobalId \r\n" + 
				"inner join GALAXYDWH_360_security_V.Fact_Circuit A\r\n" + 
				"on A.CircuitGlobalId = Ticket2CircuitGlobalId\r\n" + 
				"inner join GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist \r\n" + 
				"on GRServiceInventoryServiceId = A.Circuitid\r\n" + 
				"\r\n" + 
				"where  Ticketstatus like 'closed%'\r\n" + 
				"and  C.PartyType='Legal'\r\n" + 
				"	and partycuid in (" + ids + " )\r\n" + 
				"	and extract(month from TicketCreationDate) = " + Integer.valueOf(arr[0]) + "\r\n" + 
				"	and extract(year from TicketCreationDate) = " + Integer.valueOf(arr[1]) +"\r\n" + 
				"--Wasn't sure if you need these filters, pls uncomment them if needed\r\n" + 
				"and Tickettasktype in ('Case','Incident')\r\n" + 
				"and TicketCustomerAcknowledgementFlag='True'\r\n" + 
				"and TicketSourceIdentifier='SNOW'\r\n" + 
				"and CircuitSourceIdentifier='SFDC'";
		
		return teradataJdbcTemplate.query(query,new BeanPropertyRowMapper(TicketExcelBean.class) );
	}


/**
 * Method to query uptime report for given month and CUID
 * @author KRUTSRIN
 * @param cuids
 * @param monthYr in MM-yyyy format
 * @return List of {@UptimeExcelBean}
 */
public List<UptimeExcelBean> getUptimeExcel (List<String> cuids, String monthYr){
	String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
	
	String query = "select \r\n" + 
			"accountName,\r\n" + 
			"customerServiceId,\r\n" + 
			"serviceType,\r\n" + 
			"productFlavour,\r\n" + 
			"legalEntity,\r\n" + 
			"case\r\n" + 
			"when circuitUptimePercent<0 then 0 else circuitUptimePercent\r\n" + 
			"end as circuitUptimePercent,\r\n" + 
			"serviceOverallStatus,\r\n" + 
			"\r\n" + 
			"commissioningDate,\r\n" + 
			"bandwidth,\r\n" + 
			"aEndLLProvider,\r\n" + 
			"aEndSiteCity,\r\n" + 
			"aEndSiteAddress,\r\n" + 
			"bEndCity,\r\n" + 
			"bEndSiteAddress,\r\n" + 
			"priSec,\r\n" + 
			" \r\n" + 
			"Monthyear from (\r\n" + 
			"\r\n" + 
			"select \r\n" + 
			"GRServiceInventoryCustomerServiceId as customerServiceId,\r\n" + 
			"GRServiceInventoryServiceType as serviceType,\r\n" + 
			"GRServiceInventoryAccountName as accountName,\r\n" + 
			"GRServiceInventoryCustomerName as legalEntity,\r\n" + 
			"GRServiceInventoryProductFlavour as productFlavour,\r\n" + 
			"case when A.CustomerServiceCircuitUptimeMetricsCircuitID is null then 100 \r\n" + 
			"else A.CustomerServiceCircuitUptimeMetricsCircuitUptimePercent end as circuitUptimePercent,\r\n" + 
			"GRServiceInventoryFinalStatus as serviceOverallStatus,\r\n" + 
			"GRServiceInventoryOrderType as orderType,\r\n" + 
			"GRServiceInventoryCommissioningDate as commissioningDate,\r\n" + 
			"GRServiceInventoryBandwidth as bandwidth,\r\n" + 
			"GRServiceInventoryBSOName as bsoName,\r\n" + 
			"GRServiceInventoryAEndLLProvider as aEndLLProvider,\r\n" + 
			"GRServiceInventoryAEndSiteCity as aEndSiteCity,\r\n" + 
			"GRServiceInventoryAEndSiteAddress as aEndSiteAddress,\r\n" + 
			"GRServiceInventoryBEndSiteCity as bEndCity,\r\n" + 
			"GRServiceInventoryBEndSiteAddress as bEndSiteAddress,\r\n" + 
			"GRServiceInventoryPrisec as priSec,\r\n" + 
			"GRServiceInventoryServiceOptionType as serviceOptionType,\r\n" + 
			"GRServiceInventoryServiceTopology as serviceTopology,\r\n" + 
			"GRServiceInventoryRoutingProtocol as routingProtocol, \r\n" + 
			"GRServiceInventoryCUID as cuid,\r\n" + 
			"trim(extract(month from GRServiceInventoryAsOnDate)(format '99'))||'-'||trim(extract(year from GRServiceInventoryAsOnDate)) as Monthyear \r\n" + 
			"from \r\n" + 
			"GALAXYDWH_360_DirtyRead_V.FACT_GRServiceInventory_Hist \r\n" + 
			"left outer join\r\n" + 
			"GalaxyDWH_360_security_V.RPT_CustomerServiceCircuitUptimeMetrics A\r\n" + 
			"on (\r\n" + 
			"A.CustomerServiceCircuitUptimeMetricsCircuitID=GRServiceInventoryServiceId\r\n" + 
			"and A.CustomerServiceCircuitUptimeMetricsCUID=GRServiceInventoryCUID\r\n" + 
			"and A.CustomerServiceCircuitUptimeMetricsServicetype=GRServiceInventoryTargetServiceType\r\n" + 
			"and A.CustomerServiceCircuitUptimeMetricsMonthYear=trim(extract(month from GRServiceInventoryAsOnDate) (format '99'))||'-'||trim(extract(year from GRServiceInventoryAsOnDate))\r\n" + 
			")\r\n" + 
			"where \r\n" + 
			"Monthyear=("+ monthYr +")\r\n" + 
			"and GRServiceInventoryFinalStatus <> 'Terminated'"
			+ "and GRServiceInventoryServiceType not in ('MHS','MSS','Managed Security Services') \r\n" +
			"and  trim(GRServiceInventoryCUID) in("+ ids +") )x order by cuid";
	
	
	return teradataJdbcTemplate.query(query,new BeanPropertyRowMapper(UptimeExcelBean.class) );

   }
/**
 * Method to query serviceInventory report for given month and CUID
 * @author KRUTSRIN
 * @param cuids
 * @param monthYr in MM-yyyy format
 * @return List of {@ServiceInventoryBean}
 */
public List<ServiceInventoryBean> getServiceInventoryExcel(List<String> cuids,String monthYr){
	String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
	
	String query = "select\r\n" + 
			"GRServiceInventoryAccountName as accountName,\r\n" + 
			"GRServiceInventoryCustomerName as legalEntity,\r\n" + 
			"GRServiceInventoryServiceType as serviceType,\r\n" + 
			"GRServiceInventoryProductFlavour as productFlavour,\r\n" + 
			"GRServiceInventoryOrderId as orderId,\r\n" + 
			"GRServiceInventoryCustomerServiceId as customerServiceId,\r\n" + 
			"GRServiceInventoryCircuitAlias as circuitAlias,\r\n" + 
			"GRServiceInventoryFinalStatus as finalStatus,\r\n" + 
			"GRServiceInventoryCommissioningDate as commissioningDate,\r\n" + 
			"GRServiceInventoryServiceOption as serviceoption,\r\n" + 
			"GRServiceInventoryPrisec as primarySecondary,\r\n" + 
			"GRServiceInventoryBandwidth as circuitBandwidth,\r\n" + 
			"GRServiceInventoryServiceTopology as serviceTopology,\r\n" + 
			"GRServiceInventoryRoutingProtocol as routingProtocol,\r\n" + 
			"GRServiceInventoryAEndInterface as aEndInterface,\r\n" + 
			"GRServiceInventoryBEndInterface as bEndInterface,\r\n" + 
			"GRServiceInventoryAEndLLProvider as aEndLLProvider,\r\n" + 
			"GRServiceInventoryAEndLLBandwidth as aEndLLBandwidth,\r\n" + 
			"GRServiceInventoryBEndLLProvider as bEndLLProvider,\r\n" + 
			"GRServiceInventoryBEndLLBandwidth as bEndLLBandwidth,\r\n" + 
			"GRServiceInventoryAEndSiteCity as aEndSityCity,\r\n" + 
			"GRServiceInventoryAEndSiteAddress as aEndSiteAddress,\r\n" + 
			"GRServiceInventoryBEndSiteCity as bEndSityCity,\r\n" + 
			"GRServiceInventoryBEndSiteAddress as bEndSiteAddress\r\n" + 
			"From\r\n" + 
			"GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist\r\n" + 
			"where trim(extract(month from GRServiceInventoryAsOnDate)(format '99'))||'-'||trim(extract(year from GRServiceInventoryAsOnDate))= ("+ monthYr +")\r\n" + 
			"and GRServiceInventoryCUID in ("+ ids + ")\r\n" + 
			"and lower(trim(GRServiceInventoryFinalStatus)) in ('active','active (change order in progress)');";
	return teradataJdbcTemplate.query(query,new BeanPropertyRowMapper(ServiceInventoryBean.class) );
}

/**
 * Method to query serviceInventory report for given month and CUID
 * @author KRUTSRIN
 * @param cuids
 * @param monthYr in MM-yyyy format
 * @return List of {@ServiceInventoryDonutBean}
 */
public List<ServiceInventoryDonutBean> getServiceInventoryTypeWise(List<String> cuids, String monthYr){
	String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
	monthYr = "'" + monthYr +"'";
	
	String query = "select\r\n" + 
			"	Count( distinct T.GRServiceInventoryServiceId) as serviceIdCount,\r\n" + 
			"	T.GRServiceInventoryCUID as cuid,\r\n" + 
			"	T.GRServiceInventoryTargetServiceType as targetServiceType,\r\n" + 
			"	T.MonthYear as monthYr ,\r\n" + 
			"	T.Status\r\n" + 
			"from\r\n" + 
			"(\r\n" + 
			"	Select\r\n" + 
			"		GR.GRServiceInventoryServiceId ,\r\n" + 
			"		upper(trim(GR.GRServiceInventoryfinalStatus)) as Status,\r\n" + 
			"		GR.GRServiceInventoryCUID,\r\n" + 
			"		GR.GRServiceInventoryTargetServiceType,\r\n" + 
			"		trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear\r\n" + 
			"	from \r\n" + 
			"		GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
			"			inner join GALAXYDWH_360_Security_V.Dim_Party P\r\n" + 
			"				on GR.GRServiceInventoryCUID=P.partyCUID\r\n" + 
			"	where\r\n" + 
			"   P.partyCUID in ("+ ids +")\r\n" + 
			"and MonthYear="+ monthYr +"\r\n" + 
			"		and trunc(P.PartyEffectiveEndDate)=Date '9999-12-31'\r\n" + 
			"		and P.PartyType='Legal'\r\n" + 
			"		and P.PartySourceIdentifier='Customer GR'\r\n" + 
			"	      --and GR.GRServiceInventoryTargetServiceType in ('Global VPN','Internet Access Service','National Private Line')\r\n" + 
			"		and upper(trim(GR.GRServiceInventoryFinalStatus)) ='ACTIVE'\r\n" + 
			") T\r\n" + 
			"group by \r\n" + 
			"	T.GRServiceInventoryCUID,\r\n" + 
			"	T.GRServiceInventoryTargetServiceType,\r\n" + 
			"	T.MonthYear,\r\n" + 
			"	T.Status";
	return teradataJdbcTemplate.query(query,new BeanPropertyRowMapper(ServiceInventoryDonutBean.class) );

   }

/**
 * Method to query serviceInventory report for given month and CUID
 * @author KRUTSRIN
 * @param cuids
 * @param monthYr in MM-yyyy format
 * @return List of {@ServiceInventoryDonutBean}
 */
public List<ServiceInventoryDonutBean> getServiceStatusWiseInventory (List<String> cuids, String monthYr){
	String ids = cuids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
	monthYr = "'" + monthYr +"'";
	String query = "\r\n" + 
			"select\r\n" + 
			"Count( distinct T.GRServiceInventoryServiceId) as ServiceIdCount,\r\n" + 
			"T.GRServiceInventoryCUID as cuid,\r\n" + 
			"T.GRServiceInventorySerivceStatus as serviceStatus,\r\n" + 
			"T.MonthYear\r\n" + 
			"from\r\n" + 
			"(\r\n" + 
			"Select\r\n" + 
			"GR.GRServiceInventoryServiceId ,\r\n" + 
			"GR.GRServiceInventoryCUID,\r\n" + 
			"GR.GRServiceInventorySerivceStatus ,\r\n" + 
			"trim(extract( month from GR.GRServiceInventoryAsOnDate) (format'99'))||'-'||trim(extract( year from GR.GRServiceInventoryAsOnDate)) as MonthYear\r\n" + 
			"from \r\n" + 
			"GalaxyDWH_360_dirtyread_V.FACT_GRServiceInventory_hist GR\r\n" + 
			"inner join GALAXYDWH_360_Security_V.Dim_Party P\r\n" + 
			"on\r\n" + 
			"GR.GRServiceInventoryCUID=P.partyCUID\r\n" + 
			"where\r\n" + 
			"P.partyCUID in ("+ ids +")\r\n" + 
			"and MonthYear="+ monthYr +"\r\n" + 
			"and trunc(P.PartyEffectiveEndDate)=Date '9999-12-31'\r\n" + 
			"and P.PartyType='Legal'\r\n" + 
			"and P.PartySourceIdentifier='Customer GR'\r\n" + 
			"--Added to synch up with other optimus queries\r\n" + 
			"and GR.GRServiceInventoryTargetServiceType in ('Global VPN','Internet Access Service','National Private Line')\r\n" + 
			"and lower(trim(GR.GRServiceInventoryFinalStatus)) in ('active','active (change order in progress)')\r\n" + 
			") T\r\n" + 
			"group by \r\n" + 
			"T.GRServiceInventoryCUID,\r\n" + 
			"T.GRServiceInventorySerivceStatus,\r\n" + 
			"T.MonthYear\r\n" + 
			"";

	
	return teradataJdbcTemplate.query(query,new BeanPropertyRowMapper(ServiceInventoryDonutBean.class) );

   }



}
