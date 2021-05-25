package com.tcl.dias.serviceactivation.activation.constants;

public class AceConstants {

	public class ROUTER {

		public static final String ROUTER_MAKE = "ROUTER_MAKE";
		public static final String ROUTER_MPLS = "MPLS";
		public static final String ROUTER_PE = "PE";
		public static final String ROUTER_ALU = "ALCATEL IP";
		public static final String JUNIPER = "JUNIPER";
		public static final String CISCO_IP = "CISCO IP";

		public static final String ROUTING_PROTOCAL_BGP = "BGP";

		public static final String ROUTING_PROTOCAL_STATIC = "STATIC";
		public static final String EXTERNAL = "EXTERNAL";
		public static final String ROUTER_INTERFACE_TYPE = "routerInterfaceType";
		public static final String ROUTER_HOST_NAME = "ROUTER_HOST_NAME";
		public static final String ROUTER_PHYSICAL_PORT = "ROUTER_PHYSICAL_PORT";
		public static final String ROUTER_CURRENT_PHYSICAL_PORT = "ROUTER_CURRENT_PHYSICAL_PORT";
		public static final String ROUTER_INTERFACE_NAME = "ROUTER_INTERFACE_NAME";
		public static final String ROUTER_CURRENT_INTERFACE_NAME = "ROUTER_CURRENT_INTERFACE_NAME";
		public static final String ROUTER_V4_ADDRESS="ROUTER_V4_ADDRESS";
		public static final String ROUTER_CURRENT_V4_ADDRESS="ROUTER_CURRENT_V4_ADDRESS";
		public static final String ROUTER_V6_ADDRESS="ROUTER_V6_ADDRESS";
		public static final String ROUTER_CURRENT_V6_ADDRESS="ROUTER_CURRENT_V6_ADDRESS";
		public static final String ROUTER_MGMT_V4_ADDRESS="ROUTER_MGMT_V4_ADDRESS";
		public static final String ROUTER_ETHERNET_VLAN="ROUTER_ETHERNET_VLAN";
		public static final String ROUTER_CURRENT_ETHERNET_VLAN="ROUTER_CURRENT_ETHERNET_VLAN";
		public static final String ROUTER_CHANNEL_GROUP_NUMBER="ROUTER_CHANNEL_GROUP_NUMBER";
		public static final String ROUTER_CURRENT_CHANNEL_GROUP_NUMBER="ROUTER_CURRENT_CHANNEL_GROUP_NUMBER";
		public static final String ROUTER_FIRST_TIME_SLOT="ROUTER_FIRST_TIME_SLOT";
		public static final String ROUTER_CURRENT_FIRST_TIME_SLOT="ROUTER_CURRENT_FIRST_TIME_SLOT";
		public static final String ROUTER_LAST_TIME_SLOT="ROUTER_LAST_TIME_SLOT";
		public static final String ROUTER_CURRENT_LAST_TIME_SLOT="ROUTER_CURRENT_LAST_TIME_SLOT";
		public static final String ROUTER_INBOUND_V4="ROUTER_INBOUND_V4";
		public static final String ROUTER_INBOUND_V6="ROUTER_INBOUND_V6";
		public static final String ROUTER_OUTBOUND_V4="ROUTER_OUTBOUND_V4";
		public static final String ROUTER_OUTBOUND_V6="ROUTER_OUTBOUND_V6";
		public static final String ROUTER_CURRENT_INBOUND_V4="ROUTER_CURRENT_INBOUND_V4";
		public static final String ROUTER_CURRENT_INBOUND_V6="ROUTER_CURRENT_INBOUND_V6";
		public static final String ROUTER_CURRENT_OUTBOUND_V4="ROUTER_CURRENT_OUTBOUND_V4";
		public static final String ROUTER_CURRENT_OUTBOUND_V6="ROUTER_CURRENT_OUTBOUND_V6";

	}
	
	public class ORDER {
		public static final String CUSTOMER = "CUSTOMER_ORDER";
		public static final String NEW = "NEW";
		public static final String CMIP = "CMIP";
		public static final String OPTIMUS = "OPTIMUS";
		public static final String IN_PROGRESS = "IN PROGRESS";
		public static final String SERVICE_NEW_ = "SERVICE_NEW_";
		public static final String SERVICE_MACD_ = "SERVICE_MACD_";
		public static final String INTERNET_VPN="INTERNET-VPN";
		public static final String PRIMUS_INTERNET = "PRIMUS_INTERNET";
		public static final String ALU4755 = "4755";
		public static final String ALU584 = "584";
	}
	
	public class LMCOMPONENT {
		public static final String LMCOMPONENTTYPE = "LMCOMPONENTTYPE";
		
	}

	public class SERVICE {
		public static final String SERVICE_SUBTYPE_DIL = "DIL";
		public static final String REDUNDACY_ROLE = "REDUNDACYROLE";
		public static final String PRIMARY = "PRIMARY";
		public static final String SECONDARY = "SECONDARY";

		public static final String SERVICEID = "SERVICEID";
		public static final String SAMMANAGERID = "SAMMANAGERID";
		public static final String SERVICE_TYPE = "SERVICE_TYPE";
		public static final String SERVICE_SUB_TYPE = "SERVICE_SUB_TYPE";
		public static final String MANAGEMENTTYPE = "MANAGEMENTTYPE";
		public static final String MANAGEMENTSCOPE = "MANAGEMENTSCOPE";
		public static final String CUSTOMERNAME = "CUSTOMERNAME";
		public static final String IPPATH = "IP_PATHS";
		public static final String ORIGINATOR_NAME = "ORIGINATOR_NAME";
		public static final String ASDFLAG = " ASDFLAG";
		public static final String ALUSVCNAME_INTERNET_VPN_INTERNET_VPN_S = "INTERNET-VPN:INTERNET-VPN:S";
		public static final String ALUSVCNAME_ILL_PRIMUS_INTERNET_S = "ILL:PRIMUS_INTERNET:S";
		public static final String ISSUED = "ISSUED";
		public static final String FIXED = "FIXED";

	}

	public class OSPF {
		public static final String POINT_TO_POINT = "Point-to-Point";

	}

	public class IPADDRESS {
		public static final String WANV4ADDRESS = "WANV4ADDRESS";
		public static final String WANV4ADDRESS_SECONDARY = "WANV4ADDRESS_SECONDARY";
		public static final String WANV6ADDRESS = "WANV6ADDRESS";
		public static final String WANV6ADDRESS_SECONDARY = "WANV6ADDRESS_SECONDARY";

		public static final String LANV4ADDRESS = "LANV4ADDRESS";
		public static final String LANV4ADDRESS_SECONDARY = "LANV4ADDRESS_SECONDARY";

		public static final String LANV6ADDRESS = "LANV6ADDRESS";
		public static final String LANV6ADDRESS_SECONDARY = "LANV6ADDRESS_SECONDARY";
		public static final String EXTENDEDLANENABLED = "EXTENDEDLANENABLED";
		public static final String IPPATH = "IPPATH";
		public static final String IPV6 = "V6";
		public static final String IPV4 = "V4";
		public static final String DUAL = "DUALSTACK";
		public static final String WAN = "WAN";
		public static final String PRIMARY = "PRIMARY";
		public static final String MANAGEMENTTYPE = "MANAGEMENTTYPE";
		public static final String MANAGED = "MANAGED";
		public static final String NMSSERVICEIPV4ADDRESS = "115.114.9.68";
		public static final String PINGADDRESS1 = "4.2.2.2";

		

	}

	public class BGPPOLICY {
		public static final String SETOUTBOUNDIPV6POLICYNAME = "EXPORT_EBGP_DEFAULT_IPv6_ACE";

		public static final String DEFAULTROUTE = "DEFAULTROUTE";
		public static final String DEFAULTROUTES_IPV6_ACE = "DEFAULTROUTES_IPv6_ACE";
		public static final String PARTIALROUTE = "PARTIALROUTE";
		public static final String PARTIALROUTES_IPV6_ACE = "PARTIALROUTES_IPv6_ACE";
		public static final String FULLROUTE = "FULLROUTE";
		public static final String FULLROUTES_IPV6_ACE = "FULLROUTES_IPv6_ACE";
		public static final String EXPORT_EBGP_DEFAULT_IPV6_ACE = "EXPORT_EBGP_DEFAULT_IPv6_ACE";
		public static final String EXPORT_EBGP_PARTIAL_IPV6_ACE = "EXPORT_EBGP_PARTIAL_IPv6_ACE";
		public static final String EXPORT_EBGP_DENY_ALL_IPV6_ACE = "EXPORT_EBGP_DENY-ALL_IPv6_ACE";
		public static final String DEFAULTROUTES_IPV4_ACE = "DEFAULTROUTES_IPv4_ACE";
		public static final String PARTIALROUTES_IPV4_ACE = "PARTIALROUTES_IPv4_ACE";
		public static final String FULLROUTES_IPV4_ACE = "FULLROUTES_IPv4_ACE";
		public static final String SETOUTBOUNDIPV4POLICYNAME = "EXPORT_EBGP_DEFAULT_IPv4_ACE";
		public static final String DEFAULTROUTES_IPv4_ACE = "DEFAULTROUTES_IPv4_ACE";
		public static final String PARTIALROUTES_IPv4_ACE = "PARTIALROUTES_IPv4_ACE";
		public static final String FULLROUTES_IPv4_ACE = "FULLROUTES_IPv4_ACE";

	}

	public class INTERFACE {

		public static final String ETHRNER_INNERV_LAN = "ETHRNER_INNERV_LAN";

		public static final String LAN_PHY = "LAN-PHY";
		public static final String XGIGABIT_ETHERNET = "XE";
		public static final String HDLC = "HDLC";
		public static final String PPP = "PPP";
		public static final String TRUNK = "Trunk";
		public static final String DOT1Q = "DOT1Q";
		public static final String MODE = "ACCESS";

		public static final String Q_IN_Q = "Q-in-Q";
		public static final String QNQ = "QnQ";

		public static final String ETHRNER_OUTERV_LAN = "ETHRNER_OUTERV_LAN";

	}

	public class CPE {
		public static final String CPE_INTERFACE_IPV4 = "cpeInterfaceIPV4";
		public static final String CPE_INTERFACE_IPV6 = "cpeInterfaceIPV6";
		public static final String CPE_INTERFACE_TYPE = "cpeInterfaceType";
		public static final String CPE_HOST_NAME = "CPE_HOST_NAME";
		public static final String CPE_SNP_SERVER_COMMUNITY = "CPE_SNP_SERVER_COMMUNITY";
		public static final String CPE_PHYSICAL_PORT = "CPE_PHYSICAL_PORT";
		public static final String CPE_CURRENT_PHYSICAL_PORT = "CPE_CURRENT_PHYSICAL_PORT";
		public static final String CPE_INTERFACE_NAME = "CPE_INTERFACE_NAME";
		public static final String CPE_CURRENT_INTERFACE_NAME = "CPE_CURRENT_INTERFACE_NAME";
		public static final String CPE_V4_ADDRESS="CPE_V4_ADDRESS";
		public static final String CPE_CURRENT_V4_ADDRESS="CPE_CURRENT_V4_ADDRESS";
		public static final String CPE_V6_ADDRESS="CPE_V6_ADDRESS";
		public static final String CPE_CURRENT_V6_ADDRESS="CPE_CURRENT_V6_ADDRESS";
		public static final String CPE_ETHERNET_VLAN="CPE_ETHERNET_VLAN";
		public static final String CPE_CURRENT_ETHERNET_VLAN="CPE_CURRENT_ETHERNET_VLAN";

	}

	public class VRF {

		public static final String VRF_NAME = "VRF_NAME";
		public static final String PRIMUS_INTERNET = "PRIMUS_INTERNET";
		public static final String INTERNET_VPN = "INTERNET_VPN";

	}

	public class ACE {
		public static final String ACE_ = "ACE_";
		public static final String ACE_F_ = "ACE_F_";
		public static final String ACE_F_IN = "ACE_F_IN:";
		public static final String ACE_F_OUT = "ACE_F_OUT:";
		public static final String ACE_M_ = "ACE_M_";
		public static final String ACE_IN_COS6_ = "ACE_IN:COS6_";
		public static final String ACE_OUT_COS6_ = "ACE_OUT:COS6_";

	}

	public class BANDWIDTHUNIT {

		public static final String KBPS = "KBPS";
		public static final String MBPS = "MBPS";
		public static final String GBPS = "GBPS";
		public static final String BPS = "BPS";


		public static final String KB = "Kb";
		public static final String MB = "Mb";
		public static final String GB = "Gb";

	}

	public class PROTOCOL {
		public static final String LOOPBACK = "LOOPBACK";
		public static final String ROUTINGPROTOCAL = "ROUTINGPROTOCAL";

		public static final String RTBH = "RTBH";
		public static final String BGP = "BGP";
		public static final String STATIC = "STATIC";

		public static final String SETV6LOCALPREFERENCE = "200";

		public static final String SETLOCALPREFERENCE = "200";

		public static final String ISMULTIHOPTTL = "10";
		public static final String ROUTESEXCHANGED_DEFAULT = "DEFAULTROUTE";
		public static final String SETMAXPREFIX2000 = "2000";

		public static final String NOT_APPLICABLE = "NOT_APPLICABLE";
		public static final String DEFAULTROUTE = "DEFAULTROUTE";
		public static final String LOCALASNUMBER = "4755";

		public static final String SETMAXPREFIXTHRESHOLD = "80";

		public static final String ROUTESEXCHANGED_PARTIAL = "PARTIALROUTE";
		public static final String ROUTESEXCHANGED_FULL = "FULLROUTE";

		public static final String DEFAULT = "DEFAULT";
		public static final String PARTIAL = "PARTIAL";
		public static final String FULL = "FULL";

	}

	public class COSPROFILE {

		public static final String COS6 = "6COS";
		public static final String STANDARD = "STANDARD";

		public static final String PREMIUM = "PREMIUM";

		public static final String UNICAST = "UNICAST";
		public static final String BOTH = "BOTH";

	}

	public class DEFAULT {

		public static final String NOT_APPLICABLE = "NOT_APPLICABLE";

		public static final String NA = "NA";

		public static final String INTERNET = "INTERNET";
		public static final String PRIMUS = "PRIMUS";
		
		public static final String ETHERNET_OUTERVLAN = "ETHERNET_OUTERVLAN";
		public static final String ETHERNET = "ETHERNET";
		public static final String SDH = "SDH";
		public static final String SERIAL = "SERIAL";


	}

	public class RF {

		public static final String BTS_NUM_BTS = "bts_num_BTS";

	}

}
