package com.tcl.dias.common.constants;

/**
 * Common Constants class to maintain the constants in common package
 * 
 * @author NAVEEN GUNASEKARAN
 *
 */
public class CommonConstants {

	private CommonConstants() {
	}
	
	//public static String EQUAL_STRING="Equal";
	public static final String UPGRADE = "Upgrade";
	public static final String DOWNGRADE = "Downgrade";
	public static final String OTHERS = "Others";
	public static final String NONE = "None";
	public static final String PARALLEL = "Parallel ";
	public static final String HOT = "Hot ";
	public static final String INTRA_CITY = "Intra";
	public static final String INTER_CITY = "Inter";
	public static final String AUTH_TOKEN_HEADER = "x-auth-token";
	public static final String AUTHORIZATION = "authorization";
	public static final String XFORWARDEDFOR_HEADER = "X-Forwarded-For";
	public static final String USERAGENT_HEADER = "user-agent";
	public static final String DUMMY_TOKEN = "********";
	public static final String ANONYMOUS_USER = "anonymousUser";
	public static final String MDC_TOKEN_KEY = "mdc-token";
	public static final String MDC_UID_KEY = "mdc-uid";
	public static final String DOUBLE_COLON = "::";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String SEMI_COMMA = ";";
	public static final String MANUAL = "manual";
	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String NULL = "NULL";
	public static final String MULTI_SPACE = "\\s+";
	public static final String QUESTION_MARK = "?";
	public static final String EQUAL = "=";
	public static final String HYPHEN = "-";
	public static final String AMP = "&";
	public static final String RIGHT_SLASH = "/";
	public static final Integer ACTIVE = 1;
	public static final Integer PASSIVE=0;
	public static final Byte BACTIVE = 1;
	public static final Byte BDEACTIVATE = 0;
	public static final Byte BTEN = 10;
	public static final String OPTIMUS_SALES = "OPTIMUS_SALES";
	public static final String USERMANAGEMENT = "USR_USER_ROLE";
	public static final String ERROR = "Error";
	public static final String SUCCESS = "SUCCESS";
	public static final String YES = "Yes";
	public static final String NO = "No";
	public static final String HASERROR="HASERROR";
	public static final String ISPROFILEVALID="ISPROFILEVALID";
	public static final String GREATER_EQUAL = ">=";
	public static final String LESSTHAN_EQUAL = "<=";
	public static final String PUBLIC_IP_PATTERN = "\\b(?!(10)|192\\.168|172\\.(2[0-9]|1[6-9]|3[0-2]))[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
	public static final String AT = "@";
	public static final String PROD = "PROD";
	public static final String COLON_SEPERATOR = ":::";
	public static final String MASK_TXT = "*****************";
	public static final String ORDERS = "Orders";
	public static final String QUOTES = "Quotes";
	public static final String MS = "ms";
	public static final String UNDERSCORE = "_";
	public static final String QUOTE_GEN = "Quote Generated";
	public static final String ASK_PRICE_COMP = "Ask Price Completed";
	public static final String SENT_COMMERCIAL = "Submitted to Commercial";
	public static final String OPTY_DETAILS_NOT_AVAILABLE = " Commercial Task Not Created , Oppurtunity Details Not Available";

	/* NPL Specifc constants- start */
	public static final String LINK = "Link";
	public static final String SITEA = "Site-A";
	public static final String SITEB = "Site-B";
	public static final String NPL = "NPL";
	public static final String SITE_A_ADDRESS = "Site A Address";
	public static final String SITE_B_ADDRESS = "Site B Address";
	public static final String SERVICE_AVAILABILITY = "Service Availability %";
	public static final String NPL_STD_SLA_VAL = "99.5";
	public static final String NATIONAL_CONNECTIVITY = "National Connectivity";
	public static final String ATTR_SERVICE_AVAILABILITY = "Service Availibility";

	/* NPL Specifc constants- end */
	public static final String Y = "Y";
	public static final String N = "N";
	public static final Integer INACTIVE = 0;
	public static final String GVPN = "GVPN";
	public static final String IAS = "IAS";
	public static final String IWAN = "IWAN";
	public static final String IZO_INTERNET_WAN = "IZO Internet WAN";
	public static final String ALL = "ALL";
	

	// GSC Constants
	public static final String GSC = "GSC";
	public static final String GSIP="GSIP";
	public static final String GSIP_MACD="GSIP_MACD";


	public static final String IPC = "IPC";
	public static final String IZOSDWAN="IZOSDWAN";
	public static final String IZO_SDWAN="IZO SDWAN";
	public static final String IPC_DESC = "IZO Private Cloud";
	public static final String DUAL_CPE="Dual CPE";
	public static final String DISABLE="disable";
	public static final String ENABLE="enable";
	public static final String BASIC="basic";
	public static final String FALSE="false";
	public static final String TRUE="true";
	public static final String VERSA_VENDOR_CODE="IZO_SDWAN_SELECT";
	public static final String CISCO_VENDOR_CODE="IZO_SDWAN_CISCO";


	public static final String MANUAL_FEASIBILITY_NOTIFICATION = "Manual Feasibility Notification";
	public static final String SHARE_QUOTE_NOTIFICATION = "Share Quote Notification";
	public static final String SALES_ORDER_MULTIPLE_NOTIFICATION = "Sales Order Multiple le Notification";
	public static final String SALES_ORDER_SUPPLIER_UNAVAILABLE_NOTIFICATION = "Sales Order Supplier unavailable Notification";
	public static final String COF_CUSTOMER_DELEGATION_NOTIFICATION = "Cof customer delegation Notification";
	public static final String NEW_ORDER_SUBMISSION_NOTIFICATION = "New Order Submit Notification";
	public static final String WELCOME_LETTER_NOTIFICATION = "Welcome Letter Notification";
	public static final String HELP_TICKET_NOTIFICATION = "Help Ticket Notification";
	public static final String ORDER_DELIVERY_COMPLETE_NOTIFICATION = "Order Delivery Complete Notification";
	public static final String QUOTE_UPDATE_ONLINE_NOTIFICATION = "Quote Update Online Notification";
	public static final String FEASIBILITY_PRICING_CHANGE_NOTIFICATION = "Feasibility Pricing Change Notification";
	public static final String USG_ALL = "usergroup_all";
	public static final String MANUAL_FEASIBILITY_PRICING_NOTIFICATION = "Manual Feasibility Pricing Notification";
	public static final String PARTNER_ORDER_NA_LITE_NOTIFICATION="Partner Oder Na Lite Creation Notification";

	//Sales Support Notification
	public static final String OPTIMUS_NOTIFICATION="Optimus Notification";
	public static final String COF_SUBMITTED="COF Submitted";
	public static final String LAUNCH_DELIVERY="Launch Delivery";
	public static final String COF_SUBMITTED_COMPLETE_PERCENTAGE="80";
	public static final String LAUNCH_DELIVERY_COMPLETE_PERCENTAGE="95";

	/* IZOPC related constants - start */
	public static final String IZOPC = "IZOPC";
	public static final String IZOPC_MACD = "IZOPC_MACD";
	public static final String IZOPC_PORT = "IZO Private Connect Port";
	/* IZOPC related constants - start*/
	public static final String MANUAL_PRICING ="Manual Pricing work order";
	public static final String MANUAL_PRICING_DOWN ="Manual Feasibility as Pricing is down";

	/* Audit related constants -start */
	public static final String QUOTEID = "quoteId";
	public static final String QUOTELEID = "quoteLeId";
	public static final String ORDERID = "orderId";
	public static final String ORDERLEID = "orderLeId";
	public static final String STAGE = "stage";
	public static final String STAGES = "stages";
	public static final String ORDERCODE = "orderCode";
	public static final String ORDERCODES = "orderCodes";
	public static final String QUOTECODE = "quoteCode";
	public static final String QUOTECODES = "quoteCodes";
	public static final String SUGGESTEDPROFILENAME="suggestedProfileName";

	public static final String PARTNER = "PARTNER";
	public static final String ENTERPRISE = "Enterprise";
	public static final String SERVICEPROVIDER = "MES";
	public static final String FAILIURE = "FAILURE";
	public static final String BCR_SUCCESS = "Success";

	public static final String RECORD_TYPE_NAME_NEW_COPF_ID = "New COPF ID";
	public static final String RECORD_TYPE_NAME_MACD_COPF_ID = "MACD COPF ID";
	public static final String M6 = "M6";

	public static final Integer TWO = 2;
	public static final String AUTO_GEN_COPF_APPEND = "Auto COPF - ";

	// bcr constants
	public static final String CREATE_OPEN_BCR = "CREATE_OPEN_BCR";
	public static final String CREATE_CLOSED_BCR = "CREATE_CLOSED_BCR";
	public static final String CREATE_INPROGRESS_BCR = "CREATE_INPROGRESS_BCR";
	public static final String CREATE_UPDATE_BCR = "CREATE_UPDATE_BCR";
	public static final String LEVEL_0 = "PB_SS";
	public static final String LEVEL_C1 = "PB_CDA1";
	public static final String LEVEL_C2 = "PB_CDA2";
	public static final String LEVEL_C3 = "PB_CDA3";
	public static final String COMMERCIAL_APPROVER_1 = "COMMERCIAL_APPROVER_1";
	public static final String COMMERCIAL_APPROVER_2 = "COMMERCIAL_APPROVER_2";
	public static final String COMMERCIAL_APPROVER_3 = "COMMERCIAL_APPROVER_3";
	public static final String CREATE = "CREATE";
	
	// Credit check
	public static final String POSITIVE = "Positive";
	public static final String NEGATIVE = "Negative";
	public static final String RESERVED = "Reserved";
	public static final String PENDING_CREDIT_ACTION = "Pending Credit Action";
	public static final String CREDIT_CHECK_STATUS_CHANGE_NOTIFICATION = "Credit Check Status Change Notification";
	public static final String CREDIT_CHECK_ACCOUNT_BLACKLIST_NOTIFICATION = "Credit Check Account Blacklist Notification";
	public static final String NEW = "NEW";
	public static final String MIGRATION = "MIGRATION";
	
	public static final String CRN_ID = "CRN_ID";
	public static final String IPC_CLOUD = "IPC_CLOUD";
	
	public static final String INDIA_SITES = "INDIA";
	public static final String INDIA_INTERNATIONAL_SITES = "INDIA_INTERNATIONAL";
	public static final String INTERNATIONAL_SITES = "INTERNATIONAL";
	
	public static final String QUOTE_SITE_TYPE = "QUOTE_SITE_TYPE";
	public static final String USD="USD";
	public static final String PREDICTED_ACCESS_FEASIBILITY = "Predicted_Access_Feasibility";
	public static final String FEASIBLE = "Feasible";
	public static final String NOT_FEASIBLE = "Not Feasible";
	public static final String RANK = "rank";
	public static final String SELECTED = "Selected";
	public static final String TYPE = "Type";
	public static final String PARTNER_CREATE_ENTITY_NOTIFICATION = "Partner Create Entity Notification";
	
	public static final String THANK_YOU_MSG = "We thank you for choosing Tata Communications as your service provider. We are happy to accept your order.";
	public static final String IPC_THANK_YOU_MSG = "We thank you for choosing Tata Communications as your service provider. Thank you for completing order enrichment, your setup would be ready soon and receive a confirmation mail from our side. ";
	public static final String ORDER_ENRICHMENT_MSG="You must complete the Order Enrichment Form before we can assess your delivery date and start provisioning your service.";
	public static final String IPC_ORDER_ENRICHMENT_MSG="You must complete the Order Enrichment Form before we can assess your delivery date and start provisioning your service. Your order would be provisioned with default setup setting, on non-completion of order enrichment within 3 days of this notification.";
	
	public static final String ASTATUS="ACTIVE";
	public static final String DSTATUS="DEACTIVE";
	public static final String FSTATUS="ID doesn't exists";
	public static final String INVALID="The status is invalid";
	public static final String INR="INR";
	public static final String SP_INDIA="SP India";
	public static final String ENTERPRISE_DIRECT="Enterprise Direct";
	public static final String NIL="NIL";
	public static final String MACD="MACD";
	public static final String PRIMARY="PRIMARY";
	public static final String SECONDARY="SECONDARY";
	public static final String SINGLE="SINGLE";
	public static final String ONE = "1";
	public static final String ZERO = "0";

	public static final String MMR_CROSS_CONNECT="MMR Cross Connect";
	//Byon Constants
	public static final String STATE_CANT_BE_EMPTY ="State can't be Empty";
	public static final String COUNTRY_CANT_BE_EMPTY ="Country can't be Empty";

	public static final String CITY_CANT_BE_EMPTY ="City can't be Empty";
	public static final String PINCODE_CANT_BE_EMPTY ="Pincode can't be Empty";
	public static final String ADDRESS_CANT_BE_EMPTY ="Please enter the address details in respective fields";
	public static final String LOCALITY_CANT_BE_EMPTY ="Locality can't be Empty";
	public static final String INTERNETQUALITY_CANT_BE_EMPTY="Internet Quality can't be Empty";
	public static final String MANAGEMENT_OPTION_CANT_BE_EMPTY="Management Option can't be Empty";
	public static final String TOPLOLOGY_CANT_BE_EMPTY = "Toplogy can't be Empty";
	public static final String SITETYPE__CANT_BE_EMPTY ="Site type can't be empty, Please select a site type to proceed with the primary and secondary details.";
	public static final String PROFILE_CANT_BE_EMPTY ="Profile can't be Empty";
	public static final String SR_CANT_BE_EMPTY ="SR# can't be Empty";
	public static final String PRIMARY_P_B="Primary Port Bandwidth can't be Empty";
	public static final String PRIMARY_L_L_B="Primary Local Loop Bandwidth can't be Empty";
	public static final String PRIMARY_P_M="Primary Port Mode can't be Empty";
	public static final String PRIMARY_A_T="Primary Access Type can't be Empty";
	public static final String PRIMARY_I_T="Primary Interface Type can't be Empty";
	public static final String PRIMARY_S_I="Primary Third Party Service Id can't be Empty";
	public static final String PRIMARY_S_L="Primary Third Party Service Link Uptime Agreement can't be Empty";
	public static final String PRIMARY_I_P="Primary IP Address can't be Empty";
	public static final String PRIMARY_P_R="Primary Provider Name can't be Empty";
	public static final String PRIMARY_L_TE="PrimaryBYON 4G/TLE can't be Empty";
	public static final String SECONDARY_P_B=" Secondary Port Bandwidth can't be Empty";
	public static final String SECONDARY_L_L_B="Secondary Local Loop Bandwidth can't be Empty";
	public static final String SECONDARY_P_M="Secondary Port Mode can't be Empty";
	public static final String SECONDARY_A_T="Secondary Access Type can't be Empty";
	public static final String SECONDARY_I_T="Secondary Interface Type can't be Empty";
	public static final String SECONDARY_S_I="Secondary Third Party Service Id can't be Empty";
	public static final String SECONDARY_S_L="Secondary Third Party Link Uptime Ageement can't be Empty";
	public static final String SECONDARY_I_P="Secondary IP Address can't be Empty";
	public static final String SECONDARY_P_R="Secondary Provider Name can't be Empty";
	public static final String SECONDARY_P_L="Secondary Link Uptime Agreement can't be Empty";
	public static final String SECONDARY_L_TE="Secondary BYON 4G/TLE can't be Empty";
	public static final String EXISTINGADDRESSCHOICE="Use existing Address can't be empty";
	public static final String EXISTIINGADDRESS="please select address from Existing address drop down field";
	public static final String EXISTINGCPECHOICE="Use Existing CPE can't be empty";
	public static final String IASSITENOTEXIST="This site has GVPN Please select the correct site type";
	public static final String GVPNSITENOTEXIST="This site has IAS, Please select the correct site type";
	public static final String DUALCPESECNARIRO="Site type should be single CPE as you choose to use the existing CPE" ;
	public static final String PRIMARYSECONDARY="Please add either primary or secondary information completely";
	public static final String PRIMARYSECONDARYSINGLE="Please add only primary information for the selected site type";
	public static final String INVALIDSITETYPE="Please select valid site type";
	public static final String SECONDARY_P=" Port Bandwidth can't be Empty";
	public static final String SECONDARY_L="Local Loop Bandwidth can't be Empty";
	public static final String SECONDARY_P_MO="Port Mode can't be Empty";
	public static final String SECONDARY_A="Access Type can't be Empty";
	public static final String SECONDARY_I="Interface Type can't be Empty";
	public static final String SECONDARY_SI="Third Party Service Id can't be Empty";
	public static final String SECONDARY_SL="Third Party Link Uptime Agreement can't be Empty";
	public static final String SECONDARY_IP="IP Address can't be Empty";
	public static final String SECONDARY_PR="Provider Name can't be Empty";
	public static final String SECONDARY_LTE="BYON 4G/TLE can't be Empty";
	public static final String BYONSITEADDRESS="Please select tata underlay+Byon site type";
	public static final String IZO = "IZO";
	public static final String NDE = "NDE";
	//Non standard orders
	/*public static final String Burstable_Bandwidth="Burstable Bandwidth";*/
	
	
	//For IZO_SDWAN
	public static final String PRICING = "PRICING";
	public static final String FEASIBILITY = "FEASIBILITY";
	public static final String SDWAN_COST="SDWAN_COST";
	public static final String SDWAN_PRICE="SDWAN_PRICE";
	public static final String DCF="DCF";
	public static final String SDWAN="SDWAN";
	public static final String NEW_STATUS = "NEW";
	public static final String INPROGESS_STATUS ="INPROGRESS";
	public static final String STRUCK_STATUS = "STRUCK";
	public static final String FAILURE_STATUS  = "FAILURE";
	public static final String COMPLETED_STATUS = "COMPLETED";
	public static final String SDWAN_CGW="SDWAN_CGW";
	public static final String SDWAN_VPROXY_COST="SDWAN_VPROXY_COST";
	public static final String SDWAN_VPROXY_PRICE="SDWAN_VPROXY_PRICE";
	public static final String VPROXY_COST="VPROXY_COST";
	public static final String VPROXY_PRICE="VPROXY_PRICE";
	public static final String IZOSDWAN_QUOTES="IZOSDWAN_QUOTES";
	public static final String SDD = "SDD";
	public static final String SDD_ATTACHMENT="sdd_attachment";
	public static final String COF_ATTACHMENT="cof_attachment";
	public static final String STANDARD="STANDARD";
	public static final String CUSTOM="CUSTOM";

	//GDE - BOD
	public static final String GDE = "GDE";
	public static final String BOD_SCHEDULE_START_DATE = "Schedule Start Date & Time";
	public static final String BOD_SCHEDULE_END_DATE = "Schedule End Date & Time";
	public static final String SLOTS = "Slots";
	public static final String BANDWIDTH_ON_DEMAND = "Bandwidth On Demand"; 
	public static final String SCHEDULE_DURATION = "Schedule Duration";
	public static final String UPGRADED_BANDWIDTH = "Upgraded Bandwidth";
    public static final String QUOTE_CATEGORY_BANDWIDTH_ON_DEMAND = "BANDWIDTH_ON_DEMAND";
    public static final String BASE_CIRCUIT_BANDWIDTH = "Base Circuit Bandwidth";

	public static final String CUSTOMER = "CUSTOMER";

	public static final String UAT = "UAT";

	// Ucaas constants
	public static final String CISCO_WEBEX_CCA = "Cisco WebEx CCA";
	public static final String UCAAS = "UCAAS";
	public static final String UCAAS_WEBEX = "UCWB";
	public static final String WEBEX = "WEBEX";

	public static final String SHIFTING = "Shifting";
	public static final String HUB_PARENTED_ID= "Hub Parent ID";

	public static final String SHIFTING_LOCATION = "SHIFTING LOCATION";
	public static final String UPDATE ="UPDATE";
	public static final String CLONE ="CLONE";

	//Added for multi VRF
	public static final String VRF ="VRF";
	public static final String VRF_COMMON ="VRF Common";
	public static final String MULTI_VRF = "multiVrf";
	public static final String VRF_BILLING_TYPE = "vrfBillingType";
	public static final String NO_OF_VRFS = "No of VRFs";
	public static final String VRF_1="VRF 1";
	public static final String VRF_2="VRF 2";
    public static final String VRF_3="VRF 3";
	public static final String VRF_4="VRF 4";
	public static final String VRF_5="VRF 5";
	public static final String VRF_6="VRF 6";
	public static final String VRF_7="VRF 7";
	public static final String PROJECT_NAME="Project Name";
	public static final String VRF_PORT_BANDWIDTH="vrf Port Bandwidth";
	public static final String FLEXIQOS="flexiQos";

	public static final String IPC_PROVISIONING_FLOW = "IPC Provisioning Flow";
	public static final String IPC_SERVICE_DELIVERY = "Service Delivery";
	public static final String IPC_AUTO_PROVISIONING = "Auto Provision";

	public static final String EDIT ="EDIT";

	public static final String IPC_TAX = "Tax";
	public static final String IPC_SOLUTION_DOCUMENT = "SOLUTION";
	public static final String IPC_LICENSE_QUOTE = "LICENSE_QUOTE";
	public static final String IPC_SOLUTION_VALIDATION_EMAIL = "SOLUTION_VALID_EMAIL";
	
	//added for multivrf macd
	public static final String FLEXICOS ="FLEXICOS";
	public static final String MASTER_VRF_FLAG ="MASTER_VRF_FLAG";
	public static final String MULTI_VRF_SOLUTION ="MULTI_VRF_SOLUTION";
	public static final String TOTAL_VRF_BANDWIDTH_MBPS ="TOTAL_VRF_BANDWIDTH_MBPS";
	public static final String NUMBER_OF_VRFS ="NUMBER_OF_VRFS";
	public static final String SLAVE_VRF_SERVICE_ID ="SLAVE_VRF_SERVICE_ID";
	public static final String MASTER_VRF_SERVICE_ID ="MASTER_VRF_SERVICE_ID";
	public static final String CUSTOMER_PROJECT_NAME ="CUSTOMER_PROJECT_NAME";
	public static final String VRF_BASED_BILLING="VRF based billing";

	//o2c Actions
	public static final String ACTIVATE="ACTIVATE";
	public static final String DEACTIVATE="DEACTIVATE";


	public static final String[] SUPPORT_MATRIX_NAMES =  {"PREMIUM","STANDARD","PRIORITY","PREMIUM INTERNATIONAL","PROPRITY INTERNATIONAL","STANDARD INTERNATIONAL",
				"GLOBAL PREMIUM","PARTNER PREMIUM","PARTNER PRIORITY","MSD INDIA","MSD INTERNATIONAL","BFSI"};
	
	public static final String NAME = "Name";
	public static final String DAYS = "Days";
	public static final String ORDER_LE_CODE = "order_le_code";
	
	public static final String DR = "DR";
	public static final String DC = "DC";
	public static final String PROVISION_TYPE_MANUAL = "Manual";
	public static final String PROVISION_TYPE_AUTO = "Auto";
	public static final String ORDER_CATEGORY_FOR_PROVISION_TYPE_MANUAL = "manualOrder";
	public static final String PROVISION_TYPE = "provisionType";
	public static final String IS_ORDER_ALREADY_IMPLEMENTED = "isOrderAlreadyImplemented";
	public static final String IS_IPC_BILLING_INTL = "isIpcBillingInternational";
	public static final String EP_DUBAI = "EP_DUBAI";

	public static final String TATA_CUST_SP_LE_NAME = "Tata Communications Limited";
	public static final String IS_IPC_TAX_CAPTURE_REQUIRED = "isIpcTaxCaptureRequired";
	public static final String FINAL_CPS = "finalCps";

	public static final String EARLY_TERMINATION_CHARGES = "Early Termination Charges";
	public static final String HYBRID_CONNECTION = "Hybrid Connection";
	
	// For teamsdr
	public static final String TEAMSDR = "TEAMSDR";
	public static final String MEDIA_GATEWAY = "Media Gateway";
	public static final String LM = "LM";
	public static final String PHYSICAL_IMPLEMENTATION = "Physical Implementation";
	public static final String PHYSICAL_IMP_PARTCODE = "PS-ONST-INST-ZONE4";
	public static final String CPE_BOM_RESPONSE= "CPE Bom Response";
	public static final String CPE_BOM = "CPE-BOM";
	public static final String CPE_BOMRESPONSE = "cpe-bom-resource";
	public static final String RENTAL_PURCHASE= "Rental Purchase";
	public static final String PHYSICAL_PRESENTATION = "Physical Implementation";
	public static final String OUTRIGHT_PURCHASE ="Outright Purchase";
	public static final String MICROSOFT_CLOUD_SOLUTIONS = "Microsoft Cloud Solutions";
	public static final String UCDR = "UCDR";
	public static final String PLAN = "Plan";
	public static final String VOICE = "VOICE";
	
	//For Econet
	public static final String ECONET="ECO INTERNET";
	public static final String ECONET_SLA=">=95%";

	// For IPC
	public static final String ETC_REASON = "ETC Reason";
	public static final String ETC_PERIOD = "ETC Period";
	public static final String TO_CAMEL_CASE = "To";
	public static final String ATTRIBUTE_REASON = "reason";
	public static final String ATTRIBUTE_FROM_DATE = "fromDate";
	public static final String ATTRIBUTE_TO_DATE = "toDate";
	public static final String CLOUD_CODES = "cloudCodes";
}
