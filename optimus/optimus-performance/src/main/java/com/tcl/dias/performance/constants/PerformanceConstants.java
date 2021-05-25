package com.tcl.dias.performance.constants;

/**
 * This file contains the PerformanceConstants.java class.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PerformanceConstants {

	// Ticket trends record keys
	public static final String KEY_CIRCUIT_ID = "Circuitid";
	public static final String KEY_TICKET_IMPACT = "TicketImpact";

	// Uptime record keys
	public static final String KEY_CUSTOMER_ID = "cuid";
	public static final String KEY_CALENDER_MONTH = "calendarmonth";
	public static final String KEY_SERVICE_ID = "serviceid";
	public static final String KEY_UPTIME_PERCENTAGE = "uptime_pct";
	public static final String KEY_SERVICE_TYPE = "servicetype";

	
	// MTTR record keys
	public static final String KEY_TICKET_NO = "ticketno";
	public static final String KEY_MTTR_HRS = "MTTRhrs";
	public static final String KEY_CREATION_DATE = "CreationDate";
	
	public static final String SEVERITY1 = "Severity1";
	public static final String SEVERITY2 = "Severity2";
	public static final String SEVERITY3 = "Severity3";

	// RFO record keys
	public static final String KEY_COUNT = "cnt";
	
	public static final String KEY_TOTAL = "Total";
	public static final String KEY_RFO_SPEC = "TicketRFOSpecification";
	public static final String KEY_OTHERS = "Others";
	public static final String KEY_TOP = "Top8";
	public static final String KEY_OTHER_REASONS = "Other-Keys";

	// Severity report keys
	public static final String KEY_INV_CUID = "GRServiceInventoryCUID";
	public static final String KEY_INV_SERVICEID = "GRServiceInventoryServiceId";
	public static final String KEY_INV_SERVICE_TYPE = "GRServiceInventoryTargetServiceType";
	public static final String KEY_TICKET_NUMBER = "TicketNo";
	public static final String KEY_TICKET_STATUS = "TicketStatus";
	public static final String KEY_TICKET_DOWNTIME_MIN = "TicketDownTimeMin";
	public static final String KEY_TICKET_CREATION_DATE = "TicketCreationDate";
	public static final String KEY_TICKET_RFO_SPEC = "TicketRFOSpecification";
	public static final String KEY_TICKET_RFO_COMMENTS = "TicketRFOComments";
	public static final String KEY_CIRCUIT_SPEED = "CircuitSpeed";
	
	public static final String KEY_SI_SERVICE_TYPE = "GRServiceInventoryServiceType";
	public static final String MONTH = "MonthYear";
	public static final String KEY_SERVICETYPE = "ServiceType";

	public static final String VAL_GVPN = "Global VPN";
	public static final String VAL_IAS = "Internet Access Service";
	public static final String VAL_NPL = "National Private Line";
	public static final String VAL_IZOPC = "IZO Private Connect";
	public static final String VAL_GSIP = "Global SIP"; // dummy until galaxy gives proper name.

	public static final String GVPN = "GVPN";
	public static final String IAS = "IAS";
	public static final String ILL = "ILL";
	public static final String IZOPC = "IZOPC";
	public static final String GSIP = "GSIP";
	public static final String NPL = "NPL";
	
	// Others
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String KEY_CIRCUIT_ALIAS = "CktAlias";
	public static final String KEY_UPTIME_IN_PERCENTAGE = "UptimePercentage";
	public static final String KEY_TOTAL_COUNT = "TotalCount";
	public static final String KEY_IMPACT_CIRCUITS = "ImpactedCircuitCount";
	public static final String KEY_FAULT_RATE = "FaultRatePercentage";
	public static final String KEY_SLA_BREACH_COUNT = "SLABreachCount";
	public static final String KEY_SLA_BREACH_PERCENTAGE = "SLABreachPercent";
	public static final String KEY_FAULT_RATE_TOTAL_COUNT = "FaultRateTotalCircuitCount";
	public static final String KEY_FAULT_RATE_IMPACTED_CIRUIT_COUNT = "FaultRateImpactedCount";

	public static final String KEY_SERVICE_LINK = "ServiceLink";
	public static final String KEY_PRI_CIRCUIT_ALIAS = "PrimaryCircuitAlias";
	public static final String KEY_SEC_CIRCUIT_ALIAS = "SecondaryCircuitAlias";
	public static final String KEY_COMMITTED_UPTIME = "CommittedUptime";

	public static final String KEY_CIRCUITID = "CircuitId";
	public static final String KEY_SITE_ADDRESS = "SiteAddress";
	public static final String KEY_SERVICEID= "ServiceId";
	public static final String KEY_TICKET_NUM = "TicketNo";
	public static final String KEY_IMPACT = "Impact";
	public static final String KEY_RFO_SPECIFICATION = "RFOSpecification";
	public static final String KEY_CATEGORY_DETAIL = "CategoryDetail";
	public static final String KEY_RFO_RESPONSIBLE = "RFOResponsible";
	public static final String KEY_STATUS = "Status";
	public static final String KEY_OUTAGE_DURATION_MTS = "OutageInMin";

	public static final String KEY_PRODUCT_TYPE = "ProductType";
	public static final String KEY_MONTH_WITH_YEAR = "MonthYear";
	public static final String KEY_AVERAGE_MTTR = "AvgMTTR";
	public static final String KEY_HOUR_BUCKET = "HourBucket";
	public static final String KEY_TICKET_COUNT = "TktCount";
	
	public static final String KEY_MMYYYY = "MMYYYY";

	public static final String PERCENTAGE_FORMAT = "00.00";
	
	
	// Constants for MTTR - trend with rfo responsible
	
	public static final String RFOTicketImpactMTTRTrendpartyCUID = "RFOTicketImpactMTTRTrendpartyCUID";
	public static final String RFOTicketImpactMTTRTrendMonthYear = "RFOTicketImpactMTTRTrendMonthYear";
	public static final String RFOTicketImpactMTTRTrendTicketRFOResponsible = "RFOTicketImpactMTTRTrendTicketRFOResponsible";
	public static final String RFOTicketImpactMTTRTrendTicketImpact = "RFOTicketImpactMTTRTrendTicketImpact";
	public static final String RFOTicketImpactMTTRTrendAVGMTTR = "RFOTicketImpactMTTRTrendAVGMTTR";

	public static final String KEY_CUSTOMER = "Customer";
	public static final String KEY_TATA = "TATA";
	public static final String KEY_UNIDENTIFIED = "Unidentified";

}
