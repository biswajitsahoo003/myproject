package com.tcl.dias.servicefulfillmentutils.constants;

/**
 * File contains all Billing related Constants
 * 
 * @author yomagesh
 *
 */
public class BillingConstants {

	/**
	 * Service Types in Optimus
	 */
	public static final String CPE = "CPE";
	public static final String IAS = "IAS";
	public static final String GVPN = "GVPN";
	public static final String NPL = "NPL";
	public static final String NPL_INTRACITY = "NPL Intracity";
	public static final String NPLC = "NPLC";
	public static final String IZOSDWAN = "IZOSDWAN";
	public static final String IZO_SDWAN = "IZO SDWAN";
	public static final String IZOSDWAN_CGW = "IZOSDWAN_CGW";
	public static final String UCAAS = "UCAAS";
	public static final String CISCO_WEBEX = "Cisco WebEx CCA";
	public static final String MIRCOSOFT_CLOUD_SOLNS = "Microsoft Cloud Solutions";
	public static final String DIRECT_ROUTING ="Tata Communications with Direct Routing";
	public static final String IZOPC = "IZOPC";

	/**
	 * IAS & GVPN Products
	 */
	public static final String LOCAL_ACCESS_CHARGE = "Local Access Charges";
	public static final String MAST_CHARGE = "Mast Charges";
	public static final String FIXED_PORT_CHARGE = "Fixed Port Charges";
	public static final String GVPN_PORT_CHARGE = "GVPN Port Charges";
	public static final String ADDITIONAL_IP = "Additional IP Charges";
	public static final String SHIFTING_CHARGE_IAS = "Link/Media Shifting Charges";
	public static final String BURSTABLE_CHARGE = "Burstable Charges";

	/**
	 * CPE Products (Outright & Rental)
	 */
	public static final String CPE_OUTRIGHT_CHARGE = "CPE Outright Charges";
	public static final String CPE_MANAGEMENT_CHARGE = "CPE Management Charges";
	public static final String CPE_INSTALLATION_CHARGE = "CPE Installation Charges";
	public static final String CPE_RENTAL_CHARGE = "CPE Rental Charges";

	/**
	 * NPL Products
	 */
	public static final String LINK_MANGEMENT_CHARGE = "Management Charges";
	public static final String BANDWIDTH_CHARGE = "Bandwidth Charges";
	public static final String SHIFTING_CHARGE_NPL = "Shifting Charges";
	public static final String CROSS_CONNECTION_CHARGE = "Cross Connection Charges";
	public static final String FIBER_ENTRY_CHARGE = "Fiber Entry Charges";

	/**
	 * SDWAN Products
	 */
	public static final String SDWAN_CLOUD_GATEWAY_CHARGE = "IZO SDWAN Cloud Gateway Port Charges";
	public static final String SDWAN_SERVICE_CHARGE = "IZO SDWAN service charges";

	/**
	 * UCAAS Webex Products
	 */
	public static final String WEBEX_ENDPOINT = "UCC End Point";
	public static final String WEBEX_LICENSE = "Cloud Meetings";
	public static final String WEBEX_SUBSCRIPTION = "Fixed price audio plan";

	/**
	 * Teams DR Products
	 */
	public static final String MANAGED_RECURRING = "Managed Plan Recurring Fee";
	public static final String MANAGED_USAGE= "Managed Plan Usage Fee";
	public static final String MANAGED_OVERAGE = "Managed Plan Overage Fee";
	
	public static final String CONNECT_RECURRING = "Connect Plan Recurring Fee";
	public static final String CONNECT_USAGE= "Connect Plan Usage Fee";
	public static final String CONNECT_OVERAGE = "Connect Plan Overage Fee";
	
	public static final String CUSTOM_GSC_RECURRING = "Custom Plan GSC Recurring Fee";
	public static final String CUSTOM_GSC_USAGE = "Custom Plan GSC Usage Fee";
	public static final String CUSTOM_GSC_OVERAGE= "Custom Plan GSC Overage Fee";
	
	public static final String CUSTOM_TENANT_RECURRING = "Custom Plan Tenant Mgmt Recurring Fee";
	public static final String CUSTOM_TENANT_USAGE = "Custom Tenant Mgmt Usage Services Fee";
	public static final String CUSTOM_TENANT_OVERAGE= "Custom Plan Tenant Mgmt Overage Fee";
	
	public static final String CUSTOM_TRAINING_RECURRING = "Custom Plan Training Recurring Fee";
	public static final String CUSTOM_TRAINING_USAGE = "Custom Training Usage Services Fee";
	public static final String CUSTOM_TRAINING_OVERAGE= "Custom Plan Training Overage Fee";
	
	public static final String CUSTOM_ENABLEMENT_RECURRING = "Custom Plan Enablement Recurring Fee";
	public static final String CUSTOM_ENABLEMENT_USAGE = "Custom Plan Enablement Usage Fee";
	public static final String CUSTOM_ENABLEMENT_OVERAGE= "Custom Plan Enablement Overage Fee";
	
	public static final String CUSTOM_MONITORING_RECURRING = "Custom Plan MonitoringMgmt Recurring Fee";
	public static final String CUSTOM_MONITORING_USAGE = "Custom Plan Monitoring Mgmt Usage Fee";
	public static final String CUSTOM_MONITORING_OVERAGE= "Custom Plan Monitoring Mgmt Overage Fee"; 	
	
	public static final String CUSTOM_MANAGED_RECURRING = "Custom Plan ManagedSupport Recurring Fee";
	public static final String CUSTOM_MANAGED_USAGE = "Custom Plan Managed Support Usage Fee";
	public static final String CUSTOM_MANAGED_OVERAGE= "Custom Plan Managed Support Overage Fee"; 
	
	public static final String PROFESSIONAL_T1_TECHNICIAN = "Professional Services T1 Technician";
	public static final String PROFESSIONAL_T2_TECHNICIAN = "Professional Services T2 Technician";
	public static final String PROFESSIONAL_T3_TECHNICIAN = "Professional Services T3 Engineer";
	public static final String PROFESSIONAL_SERVICES_DESIGN = "Professional Services Design Engineer";
	public static final String PROFESSIONAL_SERVICES_PROJECT = "Professional Services Project Management";
	
	public static final String REMOTE_SERVICE = "Remote Simple Ser Request Overage Chgs";
	public static final String EXPEDITED_SERVICE = "Expedited Simple Service Request Charges";
	
	
	/**
	 * Teams DR Product Code for usage and overage
	 */
	
	public static final String MANAGED_USAGE_PRD_CODE = "MSDRU02";
	public static final String MANAGED_OVERAGE_PRD_CODE = "MSDRU10";
	
	public static final String CONNECT_USAGE_PRD_CODE = "MSDRU01";
	public static final String CONNECT_OVERAGE_PRD_CODE = "MSDRU09";
		
	public static final String CUSTOM_GSC_USAGE_PRD_CODE = "MSDRU03";
	public static final String CUSTOM_GSC_OVERAGE_PRD_CODE= "MSDRU11";
	
	public static final String CUSTOM_TENANT_USAGE_PRD_CODE = "MSDRU04";
	public static final String CUSTOM_TENANT_OVERAGE_PRD_CODE= "MSDRU12";
	
	public static final String CUSTOM_TRAINING_USAGE_PRD_CODE = "MSDRU05";
	public static final String CUSTOM_TRAINING_OVERAGE_PRD_CODE= "MSDRU13";
	
	public static final String CUSTOM_ENABLEMENT_USAGE_PRD_CODE = "MSDRU06";
	public static final String CUSTOM_ENABLEMENT_OVERAGE_PRD_CODE= "MSDRU14";
	
	public static final String CUSTOM_MONITORING_USAGE_PRD_CODE = "MSDRU07";
	public static final String CUSTOM_MONITORING_OVERAGE_PRD_CODE= "MSDRU15"; 	
	
	public static final String CUSTOM_MANAGED_USAGE_PRD_CODE = "MSDRU08";
	public static final String CUSTOM_MANAGED_OVERAG_PRD_CODEE= "MSDRU16"; 
	
	public static final String REMOTE_SERVICE_PRD_CODE = "MSDRSR01";
	public static final String EXPEDITED_SERVICE_PRD_CODE = "MSDRSR02";
	public static final String PROFESSIONAL_T1_PRD_CODE = "MSDRSR03";
	public static final String PROFESSIONAL_T2_PRD_CODE = "MSDRSR04";
	public static final String PROFESSIONAL_T3_PRD_CODE = "MSDRSR05";
	public static final String PROFESSIONAL_SERVICES_DESIGN_PRD_CODE = "MSDRSR06";
	public static final String PROFESSIONAL_SERVICES_PROJECT_PRD_CODE = "MSDRSR07";
	
	
	
	
	
	/**
	 * Order Type
	 */
	public static final String NEW = "NEW";
	public static final String MACD = "MACD";
	public static final String MODIFY = "MODIFY";
	public static final String TERMINATE = "TERMINATE";

	/**
	 * Request Type for Auditing
	 */
	public static final String ACCOUNT_CREATION = "Account Creation";
	public static final String PRD_COMM = "Product Commissioning";
	public static final String INVOICE_GEN = "Invoice Generation";
	public static final String SERVICE_TERMINATION_GENEVA = "Service Termination Geneva";
	public static final String SERVICE_TERMINATION_LR = "Service Termination Lr";
	public static final String INVOICE_TERMINATION = "Invoice Termination";
	public static final String PRD_TERM = "Product Termination";
	public static final String COMM_VETTING_ACC_CREATION = "Account Creation in Commercial Vetting";

	/**
	 * Status
	 */
	public static final String IN_PROGRESS = "IN PROGRESS";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	public static final String INVOICE_GEN_SUCCESS = "Invoice Generated";
	public static final String RESPONSE = "Data Received";
	public static final String COMM_FAILED = "COMM FAILED";
	public static final String COMM_SUCCESS = "COMM SUCCESS";
	public static final String UNSUCCESSFUL = "unsuccessful";

	/**
	 * Geneva Billing Constants
	 */
	public static final String ACCOUNT = "ACCOUNT";
	public static final String CREATE = "CREATE";
	public static final String PRODUCT = "PRODUCT";
	public static final String SOURCE_SYSTEM = "OPTIMUS";
	public static final String PROVIDER_SEGMENT = "Enterprise";
	public static final String EXISTING = "Existing";
	public static final String BILLING_ENTITY = "VSIN";
	public static final String INVOICING_CONAME = "VSNL Corporate Invoicing Company";
	public static final String INVOICING_CONAME_INTL = "TCLi Invoicing Company";
	public static final String TATA = "Tata Communications Limited";
	public static final String ONE = "1";
	public static final String A = "A";
	public static final String Z = "Z";
	public static final String B = "B";
	public static final String T = "T";
	public static final String F = "F";
	public static final String HSN_CODE = "998414";
	public static final String NEW_ORDER = "NEW#|#NEW ORDER#|#NEW ORDER#|#";
	public static final String CHANGE_BANDWIDTH = "Upgrade";
	public static final String OPTIMUS_MIGRATED = "Optimus Migrated";
	public static final String SERVICE_TERMINATION_REASON = "On Customer Demand";
	public static final String SELL_TO = "Sell To";
	public static final String DEMO_ORDER = "Demo Order";
	public static final String DEMO_DAYS = "demoDays";
	public static final String SAP_CODE = "sapCode";
	public static final Integer GENEVA_ATTRIBUTE_STRING_LIMIT = 40;

	/**
	 * Invoice Types
	 */
	public static final String PAPER_SLASH_ELECTRONIC = "Paper/Electronic";
	public static final String PAPER_PLUS_ELECTRONIC = "Paper + Electronic";
	public static final String PAPER_SPACE_ELECTONIC = "Paper+ Electronic";
	public static final String ELECTRONIC = "Electronic";
	public static final String EMAIL = "EMAIL";
	public static final String PAPER = "PAPER";
	public static final String PAPER_EMAIL = "PAPEREMAIL";

	/**
	 * Billing Freqeuncies
	 */
	public static final String ONE_M = "1M";
	public static final String TWO_M = "2M";
	public static final String THREE_M = "3M";
	public static final String SIX_M = "6M";
	public static final String TWELVE_M = "12M";

	/**
	 * For IAS & GVPN Burstable, Team DR Usage / Overage
	 */
	public static final String EVENT_SOURCE = "eventSource";
	public static final String PORT_BANDWIDTH = "portBandwidth";
	public static final String BURSTABLE_BANDWIDTH = "burstableBandwidth";
	public static final String BURSTABLE_BANDWIDTH_UOM = "burstableBwUnit";
	public static final String USAGE_MODEL = "95TH PERCENTILE";
	public static final String IS_BURSTABLE = "isBurstable";
	public static final String IS_BURSTABLE_TERMINATED = "isBurstableProductTerminated";
	public static final String BURSTABLE_SCENARIO_TYPE = "Nco_Usage";
	public static final String IAS_BURSTABLE_COST_BAND = "ILL Default CB";
	public static final String IAS_BURSTABLE_TERMINATE_NAME = "ILL Default TR";
	public static final String IAS_BURSTABLE_EVENT_CLASS_NAME = "ILL Default EC";
	public static final String IAS_BURSTABLE_CHARGE_SEG_NAME = "ILL Constant CS";
	public static final String GVPN_BURSTABLE_COST_BAND = "Burstable GVPN Standard CB";
	public static final String GVPN_BURSTABLE_TERMINATE_NAME = "Burstable GVPN standard TR";
	public static final String GVPN_BURSTABLE_EVENT_CLASS_NAME = "Burstable GVPN Standard";
	public static final String GVPN_BURSTABLE_CHARGE_SEG_NAME = "Burstable GVPN Constant CS";
	public static final String GVPN_GENEVA_PROD_NAME = "BURSTABLE GVPN";
	public static final String TEAMS_DR_COST_BAND = "TEAMSDR CB";
	public static final String TEAMS_DR_TERMINATE_NAME = "TEAMSDR TR";
	public static final String TEAMS_DR_EVENT_CLASS_NAME = "TEAMSDR EC";
	public static final String TEAMS_DR_CHARGE_SEG_NAME = "TEAMSDR CS";
	
	public static final String TEAMS_DR_COST_BAND_SIMPLE = "TEAMSDR SR Simple CB";
	public static final String TEAMS_DR_TERMINATE_NAME_SIMPLE = "TEAMSDR SR Simple TR";
	public static final String TEAMS_DR_EVENT_CLASS_NAME_SIMPLE = "TEAMSDR SR Simple EC";
	public static final String TEAMS_DR_CHARGE_SEG_NAME_SIMPLE = "TEAMSDR SR Simple CS";
	
	public static final String TEAMS_DR_COST_BAND_COMPLEX = "TEAMSDR SR Complex CB";
	public static final String TEAMS_DR_TERMINATE_NAME_COMPLEX = "TEAMSDR SR Complex TR";
	public static final String TEAMS_DR_EVENT_CLASS_NAME_COMPLEX = "TEAMSDR SR Complex EC";
	public static final String TEAMS_DR_CHARGE_SEG_NAME_COMPLEX = "TEAMSDR SR Complex CS";

	/**
	 * For Cpe Outright
	 */
	public static final String OUTRIGHT = "OUTRIGHT";
	public static final String CISCO = "Cisco";
	public static final String CPE_SERIAL_NO = "cpeSerialNumber";
	public static final String CPE_DISPACTH_DATE = "cpeDispatchDate";
	public static final String CPE_WAREHOUSE_CITY = "distributionCenterName";
	public static final String CPE_WAREHOUSE_STATE = "distributionCenterState";

	/**
	 * Optimus Constants
	 */
	public static final String PROD_COMM_DATE = "commissioningDate";
	public static final String PROD_BILL_START_DATE = "billStartDate";
	public static final String DEMO_BILL_END_DATE = "demoBillEndDate";
	public static final String DEMO_BILL_START_DATE = "demoBillStartDate";
	public static final String PARENT_DEMO_BILL_START_DATE = "parentDemoBillStartDate";
	public static final String TAX_EXEMPTION = "taxExemption";
	public static final String Y = "Y";
	public static final String DOMESTIC_CUSTOMER = "Domestic";
	public static final String INTL_CUSTOMER = "International";
	public static final String GEO_CODE = "geoCode";
	public static final String IS_INTL_BILLING = "isBillingInternational";
	public static final String ADMIN_ACCESS = "delegateAdminAccess";
	public static final String IZO_PRIVATE = "IZO_Private_";
	
	/**
	 * Prefix for Input Group Id
	 */
	public static final String OPT_ACC = "OP_AC_";
	public static final String OPT_PROD = "OPT_";
	public static final String OPT_TERM = "COMM_OPT";
	public static final String OPT_SERVICE_TERM = "OPT_TERM_";

	/**
	 * Teamsdr
	 */
	public static final String PLAN = "Plan Charges";
	public static final String TEAMSDR_ENDPOINT = "TeamsDREndpoint Charges";
	
	/**
	 * IZOPC
	 */
	public static final String IZOPC_PORT_CHARGE = "IZO Private Connect Port Charges";
	
	/**
	 * DIA
	 */
	public static final String GDIA_PORT_CHARGE = "GDIA Port Charges";
	
	/**
	 * IWAN
	 */
	public static final String IWAN_PORT_CHARGE = "IZO Internet WAN Port Charges";
}