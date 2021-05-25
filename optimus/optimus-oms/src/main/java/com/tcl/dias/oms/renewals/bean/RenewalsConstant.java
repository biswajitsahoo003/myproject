package com.tcl.dias.oms.renewals.bean;

public class RenewalsConstant {

	public final static String SERVICE_ID_NA = "Service Id not Found";
	public final static String INVALID_NUMBER = "invalid.number.error";
	public final static String RENEWALS = "RENEWALS";
	public final static String QUOTE = "QUOTE";
	public final static String INR = "INR";
	public final static String MONTH = " months";
	public final static Character Y = 'Y';
	public final static String IS_COOMERCIAL = "IS_COMMERCIAL";
	public final static Character N = 'N';
	public final static String COMPONENTS = "COMPONENTS";
	public final static String ATTRIBUTES = "ATTRIBUTES";
	public final static String GVPN = "GVPN";
	public final static String IAS = "IAS";
	public final static String NPL = "NPL";
	public final static String CPE = "CPE";
	public final static String ADDITIONAL_IPS = "Additional IPs";
	public final static String NDE = "NDE";
	public final static String ADDON_PC ="Addon pc";
	// ILL
	public final static String INTERNET_PORT = "Internet Port";
	public final static String LAST_MILE = "Last mile";
	public final static String CPE_MANAGEMENT = "CPE Management";
	public final static String IAS_COMMON = "IAS Common";
	public final static String ADDON = "Addon";
	public final static String MAST_COST = "Mast Cost";
	public final static String INTERFACE = "Interface";
	public final static String PORT_BANDWIDTH = "Port Bandwidth";
	public final static String SERVICE_TYPE = "Service type";
	public final static String BURSTABLE_BANDWIDTH = "Burstable Bandwidth";
	public final static String USAGE_MODEL = "Usage Model";
	public final static String IP_ADDRESS_ARRANGEMENT = "IP Address Arrangement";
	public final static String IPV4_ADDRESS_POOL_SIZE = "IPv4 Address Pool Size";
	public final static String IPV6_ADDRESS_POOL_SIZE = "IPv6 Address Pool Size";
	public final static String EXTENDED_LAN_REQUIRED = "Extended LAN Required?";
	public final static String ROUTING_PROTOCOL = "Routing Protocol";
	public final static String BFD_REQUIRED = "BFD Required";
	public final static String BGP_PEERING_ON = "BGP Peering on";
	public final static String BGP_AS_NUMBER = "BGP AS Number";
	public final static String CUSTOMER_PREFIXES = "Customer prefixes";
	public final static String SHIFTING_CHARGES = "Shifting Charges";
	public final static String NO_DATA_IN_EXCEL = "no.data.in.excel";
	public static final String SI_NO_DATA_ERROR = "no.data.in.service.inventory";
	public static final String SERVICEID_IS_NOT_DUAL = "serviceid.not.dual";
	public static final String VPN_PORT  = "VPN Port";
	public static final String CPE_RECOVERY_CHARGES = "CPE Recovery Charges";
	
	public final static String CROSS_CONNECT = "Cross Connect";
	public final static String SECONDARY = "secondary";
	public final static String PRIMARY = "primary";
	public final static String RESILIENCY = "Resiliency";
	public final static String YES = "YES";
	public final static String CUSTOMER_PROVIDED = "customer provided";
	public final static String MANAGED_INTERNET_ACCESS_WITH_BACKUP = "Managed Internet Access with Backup";
	public final static String MANAGED_SINGLE_INTERNET_ACCESS = "Managed Single Internet Access";
	public final static String SINGLE_INTERNET_ACCESS = "Single Internet Access";
	public final static String CUSTOM_CONFIGURATION = "Custom Configuration";

	public final static String PRIMARY_COMP_MRC = "pc mrc";
	public final static String PRIMARY_COMP_NRC = "pc nrc";
	public final static String PRIMARY_COMP_ARC = "pc arc";
	public final static String PRIMARY_COMP_EUC = "pc euc";

	public final static String SECONDARY_COMP_MRC = "sc mrc";
	public final static String SECONDARY_COMP_NRC = "sc nrc";
	public final static String SECONDARY_COMP_ARC = "sc arc";
	public final static String SECONDARY_COMP_EUC = "sc euc";

	public final static String PRIMARY_COMP_MRC_SPACE = " pc mrc";
	public final static String PRIMARY_COMP_NRC_SPACE = " pc nrc";
	public final static String PRIMARY_COMP_ARC_SPACE = " pc arc";
	public final static String PRIMARY_COMP_EUC_SPACE = " pc euc";

	public final static String SECONDARY_COMP_MRC_SPACE = " sc mrc";
	public final static String SECONDARY_COMP_NRC_SPACE = " sc nrc";
	public final static String SECONDARY_COMP_ARC_SPACE = " sc arc";
	public final static String SECONDARY_COMP_EUC_SPACE = " sc euc";

	public final static String PRIMARY_ATTR_MRC_SPACE = " p mrc";
	public final static String PRIMARY_ATTR_NRC_SPACE = " p nrc";
	public final static String PRIMARY_ATTR_ARC_SPACE = " p arc";
	public final static String PRIMARY_ATTR_EUC_SPACE = " p euc";

	public final static String SECONDARY_ATTR_MRC_SPACE = " s mrc";
	public final static String SECONDARY_ATTR_NRC_SPACE = " s nrc";
	public final static String SECONDARY_ATTR_ARC_SPACE = " s arc";
	public final static String SECONDARY_ATTR_EUC_SPACE = " s euc";

	public final static String PO_NUMBER = "PO Number";
	public final static String PO_DATE = "PO Date";
	
	public final static String NATIONAL_CONNECTIVITY = "National Connectivity";
	public final static String LINK_MANAGEMENT_CHARGES = "Link Management Charges";
	public final static String LINK = "Link";

	public final static String ILL_INTERNET_PORT = "{\"name\":\"Internet Port\",\"componentMasterId\":1,\"attributes\":[{\"name\":\"Interface\",\"value\":\"Fast Ethernet\",\"attributeMasterId\":1},{\"name\":\"Port Bandwidth\",\"value\":\"20\",\"attributeMasterId\":29},{\"name\":\"Service type\",\"value\":\"Fixed\",\"attributeMasterId\":3},{\"name\":\"Burstable Bandwidth\",\"value\":\"\",\"attributeMasterId\":4},{\"name\":\"Usage Model\",\"value\":\"\",\"attributeMasterId\":5},{\"name\":\"IP Address Arrangement\",\"value\":\"IPv4\",\"attributeMasterId\":12},{\"name\":\"IPv4 Address Pool Size\",\"value\":\"TCL IPv4/29\",\"attributeMasterId\":13},{\"name\":\"IPv6 Address Pool Size\",\"value\":\"\",\"attributeMasterId\":14},{\"name\":\"Extended LAN Required?\",\"value\":\"No\",\"attributeMasterId\":16},{\"name\":\"Routing Protocol\",\"value\":\"Static\",\"attributeMasterId\":18},{\"name\":\"BFD Required\",\"value\":\"No\",\"attributeMasterId\":17},{\"name\":\"BGP Peering on\",\"value\":\"\",\"attributeMasterId\":21},{\"name\":\"BGP AS Number\",\"value\":\"\",\"attributeMasterId\":38},{\"name\":\"Customer prefixes\",\"value\":\"\",\"attributeMasterId\":39}],\"type\":\"primary\"}";
	public final static String ILL_LAST_MILE = "{\"name\":\"Last mile\",\"componentMasterId\":2,\"attributes\":[{\"name\":\"Shared Last Mile\",\"value\":\"No\",\"attributeMasterId\":22},{\"name\":\"Shared Last Mile Service ID\",\"value\":\"\",\"attributeMasterId\":23},{\"name\":\"LM MAN bw\",\"value\":\"\",\"attributeMasterId\":49},{\"name\":\"LM MAN MUX\",\"value\":\"\",\"attributeMasterId\":51},{\"name\":\"LM MAN inbuilding\",\"value\":\"\",\"attributeMasterId\":52},{\"name\":\"MAN OCP\",\"value\":\"\",\"attributeMasterId\":53},{\"name\":\"MAN Rentals\",\"value\":\"\",\"attributeMasterId\":54},{\"name\":\"Provider Chanrge\",\"value\":\"\",\"attributeMasterId\":55},{\"name\":\"Mast Charge offnrt\",\"value\":\"\",\"attributeMasterId\":57},{\"name\":\"Radwin\",\"value\":\"\",\"attributeMasterId\":58},{\"name\":\"Mast Charge onnet\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"PROW Value\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC Converter Charges\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC - BW Onnet\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC-Colocation\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"OTC Modem Charges\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"OTC/NRC - Installation\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC Modem Charges\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC - BW Offnet\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"Local Loop Bandwidth\",\"value\":\"20\",\"attributeMasterId\":31}],\"type\":\"primary\"}";
	public final static String ILL_CPE = "{\"name\":\"CPE\",\"componentMasterId\":3,\"attributes\":[{\"name\":\"CPE Basic Chassis\",\"value\":\"ISR4321/K9\",\"attributeMasterId\":6},{\"name\":\"CPE Discount Install\",\"value\":\"\",\"attributeMasterId\":45},{\"name\":\"CPE Discount Outright Sale\",\"value\":\"\",\"attributeMasterId\":46},{\"name\":\"CPE Discount Rental\",\"value\":\"\",\"attributeMasterId\":47},{\"name\":\"CPE Discount Management\",\"value\":\"\",\"attributeMasterId\":48},{\"name\":\"Shared CPE\",\"value\":\"No\",\"attributeMasterId\":24},{\"name\":\"Shared CPE Service ID\",\"value\":\"\",\"attributeMasterId\":25}],\"type\":\"primary\"}";
	public final static String ILL_ADDITIONAL_IPS = "{\"name\":\"Additional IPs\",\"componentMasterId\":6,\"attributes\":[],\"type\":\"primary\"}";
	public final static String ILL_SHIFTING_CHARGES = "{\"name\":\"Shifting Charges\",\"componentMasterId\":6,\"attributes\":[],\"type\":\"primary\"}";

	public final static String ILL_INTERNET_PORT_SEC = "{\"name\":\"Internet Port\",\"componentMasterId\":1,\"attributes\":[{\"name\":\"Interface\",\"value\":\"Fast Ethernet\",\"attributeMasterId\":1},{\"name\":\"Port Bandwidth\",\"value\":\"20\",\"attributeMasterId\":29},{\"name\":\"Service type\",\"value\":\"Fixed\",\"attributeMasterId\":3},{\"name\":\"Burstable Bandwidth\",\"value\":\"\",\"attributeMasterId\":4},{\"name\":\"Usage Model\",\"value\":\"\",\"attributeMasterId\":5},{\"name\":\"IP Address Arrangement\",\"value\":\"IPv4\",\"attributeMasterId\":12},{\"name\":\"IPv4 Address Pool Size\",\"value\":\"TCL IPv4/29\",\"attributeMasterId\":13},{\"name\":\"IPv6 Address Pool Size\",\"value\":\"\",\"attributeMasterId\":14},{\"name\":\"Extended LAN Required?\",\"value\":\"No\",\"attributeMasterId\":16},{\"name\":\"Routing Protocol\",\"value\":\"Static\",\"attributeMasterId\":18},{\"name\":\"BFD Required\",\"value\":\"No\",\"attributeMasterId\":17},{\"name\":\"BGP Peering on\",\"value\":\"\",\"attributeMasterId\":21},{\"name\":\"BGP AS Number\",\"value\":\"\",\"attributeMasterId\":38},{\"name\":\"Customer prefixes\",\"value\":\"\",\"attributeMasterId\":39}],\"type\":\"secondary\"}";
	public final static String ILL_LAST_MILE_SEC = "{\"name\":\"Last mile\",\"componentMasterId\":2,\"attributes\":[{\"name\":\"Shared Last Mile\",\"value\":\"No\",\"attributeMasterId\":22},{\"name\":\"Shared Last Mile Service ID\",\"value\":\"\",\"attributeMasterId\":23},{\"name\":\"LM MAN bw\",\"value\":\"\",\"attributeMasterId\":49},{\"name\":\"LM MAN MUX\",\"value\":\"\",\"attributeMasterId\":51},{\"name\":\"LM MAN inbuilding\",\"value\":\"\",\"attributeMasterId\":52},{\"name\":\"MAN OCP\",\"value\":\"\",\"attributeMasterId\":53},{\"name\":\"MAN Rentals\",\"value\":\"\",\"attributeMasterId\":54},{\"name\":\"Provider Chanrge\",\"value\":\"\",\"attributeMasterId\":55},{\"name\":\"Mast Charge offnrt\",\"value\":\"\",\"attributeMasterId\":57},{\"name\":\"Radwin\",\"value\":\"\",\"attributeMasterId\":58},{\"name\":\"Mast Charge onnet\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"PROW Value\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC Converter Charges\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC - BW Onnet\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC-Colocation\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"OTC Modem Charges\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"OTC/NRC - Installation\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC Modem Charges\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"ARC - BW Offnet\",\"value\":\"\",\"attributeMasterId\":60},{\"name\":\"Local Loop Bandwidth\",\"value\":\"20\",\"attributeMasterId\":31}],\"type\":\"secondary\"}";
	public final static String ILL_CPE_SEC = "{\"name\":\"CPE\",\"componentMasterId\":3,\"attributes\":[{\"name\":\"CPE Basic Chassis\",\"value\":\"ISR4321/K9\",\"attributeMasterId\":6},{\"name\":\"CPE Discount Install\",\"value\":\"\",\"attributeMasterId\":45},{\"name\":\"CPE Discount Outright Sale\",\"value\":\"\",\"attributeMasterId\":46},{\"name\":\"CPE Discount Rental\",\"value\":\"\",\"attributeMasterId\":47},{\"name\":\"CPE Discount Management\",\"value\":\"\",\"attributeMasterId\":48},{\"name\":\"Shared CPE\",\"value\":\"No\",\"attributeMasterId\":24},{\"name\":\"Shared CPE Service ID\",\"value\":\"\",\"attributeMasterId\":25}],\"type\":\"secondary\"}";
	public final static String ILL_ADDITIONAL_IPS_SEC = "{\"name\":\"Additional IPs\",\"componentMasterId\":6,\"attributes\":[],\"type\":\"secondary\"}";
	public final static String ILL_SHIFTING_CHARGES_SEC = "{\"name\":\"Shifting Charges\",\"componentMasterId\":6,\"attributes\":[],\"type\":\"secondary\"}";

	public final static String ACTIVE = "Active";

	public static final String SERVICEID_IS_NOT_PRIMARY = "serviceid.not.primary";
	public static final String SERVICEID_IS_NOT_SECONDAYY = "serviceid.not.secondary";
	public static final String EFFECTIVE_DATE = "Effective Date";
	public static final String DATE_FORMAT_ERROR = "date.format.error";
	public static final String STRING_INPUT="string.input";
	public static final String TAX_EXCEMPTION = "taxExemption";
	public final static String GST_NUMBER = "GST_NUMBER";
	public final static String GST_NUMBER_NOT_AVAILABLE = "gst.number.not.available";
	public final static String GST_FORMAT_EXCEPTION = "gst.format.notvalid";
}
