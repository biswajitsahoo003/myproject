package com.tcl.dias.serviceactivation.constants;

import com.google.common.collect.ImmutableMap;

public class CramerConstants {

	// create service constants
	public static final String SITE_CODE = "siteCode";
	public static final String ORDER_ID = "orderId";
	public static final String CUSTOMER_NAME = "customerName";
	public static final String SERVICE_TYPE = "serviceType";
	public static final String SERVICE_BANDWIDTH_VALUE = "serviceBandwidthValue";
	public static final String SERVICE_BANDWIDTH_UNIT = "serviceBandwidthUnit";
	public static final String LM_BANDWIDTH_VALUE = "lmBandwidthValue";
	public static final String LM_BANDWIDTH_UNIT = "lmBandwidthUnit";
	public static final String SERVICE_OPTION = "serviceOption";
	public static final String REQUEST_ID = "requestId";
	public static final String REQUESTING_SYSTEM = "RequestingSystem";
	public static final String CROSSCONNECTTYPE = "CROSSCONNECTTYPE";
	public static final String OPTIMUS = "OPTIMUS";


	// create CLR constants
	public static final String COPF_ID = "orderId";
	public static final String SCENARIO_TYPE = "scenarioType";
	public static final String FEASIBILITY_ID = "feasibilityId";
	public static final String BANDWIDTH_VALUE = "bandwidthValue";
	public static final String BANDWIDTH_UNIT = "bandwidthUnit";
	public static final String COVERAGE = "coverage";
	public static final String ORDER_TYPE = "orderType";

	public static final String NPL = "NPL";
	public static final String NPLC = "NPLC";
	public static final String NPL_INTRACITY = "NPL Intracity";
	public static final String NDE = "NDE";
	public static final String PRE_RESERVED = "preReserved";
	public static final String IS_MODIFIED = "isModified";
	public static final String ON_NET = "onnet";
	public static final String REQUEST_TYPE = "requestType";
	public static final String INTERFACE = "interface";
	public static final String NODEW_MUX_NAME = "nodewMuxName";
	public static final String MAX_HOP = "maxHop";
	public static final String PRIMARY_SECONDARY = "primarySecondary";
	public static final String HANGING_NETWORKING_CIRCUITS = "hangingNetworkCircuits";
	
	public static final String ALLOWED_TIER1_HOPS = "allowedTier1Hops";
	public static final String ALLOW_TTSL_NODES = "allowTTSLNodes";
	public static final String OTHER_SEGMENT = "otherSegment";
	
	public static final String CUID = "cuid";
	public static final String IS_EXTENDED_LAN = "isExtendedLan";
	public static final String IS_EXTENDED_LAN_CHANGED = "isExtendedLanChanged";
	public static final String BASE_RATE = "baseRate";
	public static final String BASE_RATE_UNIT = "baseRateUnit";
	public static final String BURST_RATE = "burstRate";
	public static final String BURST_RATE_UNIT = "burstRateUnit";
	public static final String IP_ADDRESS_ARRANGEMENT = "ipAddressArrangement";
	public static final String IS_IP_PATH_TYPE_CHANGED = "isIpPathTypeChanged";
	public static final String LAST_MILE_INTERFACE = "lastMileInterface";
	public static final String MULTI_VRF_FLAG = "multiVRFFlag";
	public static final String MULTI_VRF_SOLUTION = "multiVRFSolution";
	public static final String MULTI_LINK = "mulitLink";
	public static final String NETWORK_COMP_TYPE = "networkCompType";
	public static final String NO_OF_VRFS = "noOfVRFS";
	public static final String NO_OF_IP_ADDRESSES = "noOfIpAddresses";
	public static final String PATH_TYPE = "pathType";
	public static final String PORT_BANDWIDTH = "portBandwidth";
	public static final String PORT_BANDWIDTH_UNIT = "portBandwidthUnit";
	public static final String BW_UNIT = "bwUnit";
	public static final String PRISEC_MAPPING = "priSecMapping";
	
	public static final String PROVIDER = "provider";
	public static final String ROUTING_PROTOCOL = "routingProtocol";
	public static final String SERVICE_CATEGORY = "serviceCategory";
	public static final String SERVICE_OPTION_CHANGED = "serviceOptionChanged";
	public static final String SHARED_CPE_REQUIRED = "sharedCpeRequired";
	public static final String SHARED_LM_REQUIRED = "sharedLmRequired";
	public static final String TCL_POP_CITY = "tclPopCity";
	public static final String UNIQUE_POP_ID = "uniquePopId";
	
	public static final String WAN_IP_TO_BE_RESERVED_KEY = "wanIpToBeReservedKey";
	public static final String WAN_IP_TO_BE_RESERVED_VALUE = "wanIpToBeReservedValue";
	public static final String IP_ADDRESS_TO_BE_RESERVED_KEY = "ipAddressToBeReservedKey";
	public static final String IP_ADDRESS_TO_BE_RESERVED_VALUE = "ipAddressToBeReservedValue";

	public static final String CREATE_SERVICE = "CREATE_SERVICE";
	public static final String CREATE_SERVICE_GVPN = "CREATE_SERVICE_GVPN";
	public static final String GET_MUX_INFO_SYNC = "GET_MUX_INFO_SYNC";
	public static final String GET_MUX_INFO_ASYNC = "GET_MUX_INFO_ASYNC";
	public static final String GET_DOWNTIME_ASYNC = "GET_DOWNTIME_ASYNC";

	public static final String GET_DOWNTIME = "GET_DOWNTIME";
	public static final String CREATE_CLR = "CREATE_CLR";
	public static final String SKIP_CLR = "SKIP_CLR";
	public static final String IPSERVICE_SYNC = "IPSERVICE_SYNC";
	public static final String IPSERVICE_ASYNC = "IPSERVICE_ASYNC";
	public static final String GET_HD_CONFIG_DETAILS = "HD_CONFIG_DETAILS";
	
	public static final String IS_VALID_BTS = "IS_VALID_BTS";
	public static final String CHECK_CLR_INFO = "CHECK_CLR_INFO";
	public static final String CHECK_IP_CLR = "CHECK_IP_CLR";
    public static final String CREATE_CLR_ASYNC = "CREATE_CLR_ASYNC";
    public static final String GET_CLR_ASYNC = "GET_CLR_ASYNC";
    public static final String SET_CLR_ASYNC = "SET_CLR_ASYNC";
    public static final String GET_CLR_ASYNC_FAILURE = "GET_CLR_ASYNC_FAILURE";
    public static final String GET_IP_ASYNC = "GET_IP_ASYNC";
    public static final String GET_ASSIGN_DUMMY_IP_ASYNC = "GET_ASSIGN_DUMMY_IP_ASYNC";
    public static final String GET_RELEASE_DUMMY_IP_ASYNC = "GET_RELEASE_DUMMY_IP_ASYNC";
    
    public static final String SET_ELECTRICAL_HANDOFF = "SET_ELECTRICAL_HANDOFF_DETAILS";
    
    /* create clr constants moved from service fulfillment utils */
	public static final String REQUESTING_SYSTEM_OPTIMUS = "OPTIMUS";
	public static final String TYPE_NEW = "NEW";
	public static final String SERVICE = "SERVICE";
	public static final String ISSUED = "ISSUED";
	public static final String MUXIP = "muxIP";
	public static final String MUXNAME = "muxName";
	public static final String VPNIN = "VPNIN_";
	public static final String ILL_STANDARD_PORT = "ILL STANDARD PORT";
	public static final String SINGLE_INTERNET_ACCESS = "Single Internet Access";
	public static final String STDILL = "STDILL";
	public static final String PILL = "PILL";
	public static final String ECOINTERNET = "ECO INTERNET";
	public static final String Routing_Protocol = "Routing Protocol";
	public static final String resp_city = "resp_city";
	public static final String pop_network_loc_id = "pop_network_loc_id";
	public static final String IP_Address_Provided_By = "IP Address Provided By";
	public static final String _IP_ADDRESS_ARRANGEMENT = "IP Address Arrangement";
	public static final String Burstable_Bandwidth = "burstableBandwidth";
	public static final String Burstable_Bandwidth_Unit = "burstableBwUnit";
	public static final String mbps = "mbps";
	public static final String Mbps = "Mbps";
	public static final String ONNET_WIRELINE = "ONNET WIRELINE";
	public static final String MBPS = "MBPS";
	public static final String Interface_Type_A_end = "Interface Type - A end";
	public static final String EXTENDED_LAN_REQUIRED = "Extended LAN Required?";
	public static final String SHARED_CPE = "Shared CPE";
	public static final String SITE_ID = "siteId";
	public static final String SERVICE_CODE = "serviceCode";
	public static final String CLR_CREATION = "CLR CREATION";
	public static final String CUSTOMER_ACCESS = "Customer Access";
	public static final String ILL = "ILL";
	public static final String IAS = "IAS";
	public static final String GVPN = "GVPN";
	public static final String PRODUCT_OFFERING_NAME = "productOfferingName";
	public static final String SHARED_LASTMILE = "sharedLastMile";
	public static final String SHAREDCPE = "sharedCpe";
	public static final String SOURCE_CITY = "sourceCity";
	public static final String POP_SITE_CODE = "popSiteCode";
	public static final String YES = "Yes";
	public static final String NO = "No";
	public static final String SC_ORDER_ID = "scOrderId";
	public static final String IS_EXTENDED_LAN_REQUIRED ="isExtendedLanRequired";
	public static final String IP_ADDR_MGMT = "ipAddressManagement";
	public static final String IP_ADDR_ATTR_NAME = "IP Address Arrangement";
	public static final String ADDITIONAL_IP_ADDR_MGMT = "additionalIPsArrangementType";
	public static final String ADDITIONAL_IP_ATTR_NAME = "IP Address Arrangement for Additional IPs";
	public static final String LOCAL_LOOP_INTERFACE = "local_loop_interface";
	public static final String FAST_ETHERNET = "Fast Ethernet";
	public static final String GIGABIT_ETHERNET = "Gigabit Ethernet";
	public static final String FE = "FE";
	public static final String GE = "GE";
	public static final String EMPTY = "";
	public static final String IP_ADDRESS_PROVIDED_BY = "IP Address Provided By";
	public static final String TCL = "TCL";
	public static final String IDC_BANDWIDTH = "IDC Bandwidth";
	public static final String IP_ADDRESS_TO_BE_PROVIDED = "IPADDRESSTOBEPROVIDED";
	public static final String NO_OF_IP_ADDRESS = "NOOFIPADDRESS";
	public static final String CONVERTER_IP = "Converter IP";
	public static final String FALVOR_CHANGE = "FlavorChange";
	public static final String WAN_IP_TO_BE_REVERSED = "WANIP_TO_BE_REVERSED";
	public static final String TCL_IPV4_ADDRESSES = "TCL IPv4 Addresses";
	public static final String TCL_IPV6_ADDRESSES = "TCL IPv6 Addresses";
	public static final String IPV4 = "IPV4";
	public static final String LASTMILE_PROVIDER = "lastMileProvider";
	public static final String TATA_COMMUNICATIONS = "TATA COMMUNICATIONS";
	public static final String PRIMARY = "Primary";
	public static final String SECONDARY = "Secondary";
	public static final String FALSE_STRING = "false";
	public static final String TRUE_STRING = "true";
	public static final String MAN = "MAN";
	public static final String SINGLE = "single";
	public static final String LOCAL_LOOP_BANDWIDTH = "localLoopBandwidth";
	public static final String AGGREGATION_BANDWIDTH = "aggregationBandwidth";
	public static final String CPE_MANAGEMENT_TYPE = "cpeManagementType";
	public static final String UNMANAGED = "Unmanaged";
	public static final String MANAGED = "Managed";
	public static final String FULLY_MANAGED = "Fully Managed";
	public static final String PHYSICAL_MANAGED = "Physical Managed";
	public static final String PROACTIVE_MONITORING = "Proactive Monitoring";
	public static final String PROACTIVE_SERVICE = "Proactive services";
	public static final String PROACTIVE_SERVICES = "Proactive Services";
	public static final String SDWAN = "SDWAN";
	public static final String CONFIG_MANAGED = "Config Managed";
	public static final String PHYSICALLY_MANAGED = "Physically Managed";
	public static final String PROACTIVE_MONITORED = "Proactive Monitored";
	public static final String SERVICE_VARIANT = "serviceVariant";
	public static final String PRODUCT_NAME = "productName";
    public static final String GET_IP_SYNC = "GET_IP_SYNC";
	public static final String GET_CLR_SYNC = "GET_CLR_SYNC";
	public static final String SET_CLR_SYNC = "SET_CLR_SYNC";
	public static final String SET_MFD_CLR_SYNC = "SET_MFD_CLR_SYNC";
	public static final String GET_ASSIGN_DUMMY_IP_SYNC = "GET_ASSIGN_DUMMY_IP_SYNC";
	public static final String GET_RELEASE_DUMMY_IP_SYNC = "GET_RELEASE_DUMMY_IP_SYNC";
	public static final String EOR_IOR_DEPENDENCY_CHECK = "EOR_IOR_DEPENDENCY_CHECK";
	
	/**CGW CONSTANTS**/
	public static final String CURRENT_CG_USECASE="Current_CG_Usecase";
	public static final String USECASE="useCase";

	/**MACD CONSTANTS**/
	public static final String TYPE_MACD = "MACD";
	public static final String CHANGE = "CHANGE";
	public static final String SHIFT_SITE_SERVICE="SHIFT_SITE";
	public static final String ADD_SITE_SERVICE="ADD_SITE";
	public static final String CHANGE_BANDWIDTH_SERVICE="CHANGE_BANDWIDTH";
	public static final String ADD_IP_SERVICE="ADD_IP";
	public static final String REQUEST_TERMINATION_SERVICE="REQUEST_TERMINATION";
	public static final String ORDER_CATEGORY = "orderCategory";
	public static final String ORDER_SUB_CATEGORY = "orderSubCategory";
	public static final ImmutableMap<String, String> ORDER_CATEGORY_MAP = ImmutableMap.of( 
			"ADD_IP", "HOT UPGRADE", 
			"ADD_SITE", ""); 
	
    private CramerConstants() {
		/* static usage */
	}
}
