package com.tcl.dias.oms.constants;

import com.tcl.dias.oms.beans.FPRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * SFDC Constants
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum FPConstants {

	// NPL specific constants -start

	SERVICE_AVAILABILITY("Service Availability"), NATIONAL_PRIVATE_LINES("NPL"), NATIONAL_DEDICATED_ETHERNET("National Dedicated Ethernet"),HUB_PARENTED("Hub Parented"), LINK("Link"), LOCAL_LOOP(
			"Local Loop"), SITEA("Site-A"), SITEB("Site-B"), UP_TIME("Up time"), INTERFACE_TYPE(
					"Interface Type - "), POP("POP"), LINK_MANAGEMENT_CHARGES("Link Management Charges"),PROVIDER_CHARGE("Provider Charge"),

	// NPL specific constants -end

	ENTERPRISE("Enterprise"), SERVICE_PROVIDER("Service Provider"), STANDARD("Standard"), PREMIUM("Premium"), NEW_ORDER(
			"New Order"), INTERNET_ACCESS_SERVICE("Internet Access Service"), INTERFACE("Interface"), PORT_BANDWIDTH(
					"Port Bandwidth"), PORT_BANDWIDTH_UNIT("Port Bandwidth Unit"), BURSTABLE_BANDWIDTH("Burstable Bandwidth"), IPV4_POOL_SIZE(
							"IPv4 Address Pool Size"), IPV6_POOL_SIZE("IPv6 Address Pool Size"), CPE_MANAGEMENT_TYPE(
									"CPE Management Type"), CPE("CPE"), SERVICE_VARIANT("Service Variant"), USAGE_MODEL(
											"Usage Model"), RENTAL("rental"), OUTRIGHT_SALE("sale"), FAST_ETHERNET(
													"Fast Ethernet"), FE("FE"), GE("GE"), IS_FP_DONE(
															"is_feasiblity_check_done"), FEASIBILITY(
																	"FEASIBILITY"), TRUE("true"), FALSE(
																			"false"), IS_PRICING_DONE(
																					"is_pricing_check_done"), PRICING(
																							"PRICING"), SYSTEM(
																									"system"), LAST_MILE(
																											"Last mile"), ACCESS(
																													"Access"), INTERNET_PORT(
																															"Internet Port"), ADD_ON(
																																	"Addon"), ADDITIONAL_IP(
																																			"Additional IPs"), TOTAL_MRC(
																																					"TOTAL_MRC"), TOTAL_NRC(
																																							"TOTAL_NRC"), TOTAL_ARC(
																																									"TOTAL_ARC"), TOTAL_TCV(
																																											"TOTAL_TCV"), PRIMARY(
																																													"primary"), SECONDARY(
																																															"secondary"), TYPE(
																																																	"TYPE"), ONNET(
																																																			"onnet"), OFFNET(
																																																					"offnet"), ERROR_PATTERN(
																																																							"Error_in_feasibility"), CPE_BASIC_CHASSIS(
																																																									"CPE Basic Chassis"), ADDITIONAL_IP_FLAG(
																																																											"Additional IP"), IP_ADDRESS_MANAGEMENT(
																																																													"IP Address Arrangement for Additional IPs"), PROVIDER(
																																																															"TATA COMMUNICATIONS"), LOCAL_LOOP_BW(
																																																																	"Local Loop Bandwidth"), LOCAL_LOOP_BW_UNIT("Local Loop Bandwidth Unit"), MANUAL(
																																																																			"manual"), PACKET_DROP(
																																																																					"Packet Drop %"),PACKET_DROP_WITHOUT_PERCENTAGE(
																																																																							"Packet Drop"), NETWORK_UPTIME(
																																																																							"Network Uptime"), ADDITIONAL_IP_IPV4(
																																																																									"IPv4 Address Pool Size for Additional IPs"), ADDITIONAL_IP_IPV6(
																																																																											"IPv6 Address Pool Size for Additional IPs"), PORT_TYPE(
																																																																													"Port Type"), NATIONAL_CONNECTIVITY(
																																																																															"National Connectivity"), PRIVATE_LINES(

																																																																																	"Private Lines"), IAS_ILL(
																																																																																			"Internet Access Service"), GVPN(
																																																																																					"GVPN"), NPL(
																																																																																							"NPL"), VPN_PORT(
																																																																																									"VPN Port"), GLOBAL_VPN(
																																																																																											"Global VPN"), RESILIENCY(
																																																																																													"Resiliency"), PORT_MODE(
																																																																																															"Port Mode"), ACCESS_TOPOLOGY(
																																																																																																	"Access Topology"), GSC_DETAIL_LEVEL(
																																																																																																			"gsip"), ITFS(
																																																																																																					"ITFS"), PSTN(
																																																																																																							"PSTN"), LNS(
																																																																																																									"LNS"), RATE_PER_MINUTE_FIXED(
																																																																																																											"Rate per Minute(fixed)"), RATE_PER_MINUTE_SPECIAL(
																																																																																																													"Rate per Minute(special)"), RATE_PER_MINUTE_MOBILE(
																																																																																																															"Rate per Minute(mobile)"), LATENCY_ROUND_TRIP_DELAY(
																																																																																																																	"Round Trip Delay (RTD)"), MAST_COST(
																																																																																																																			"Mast Cost"), MAST_COST_DESC(
																																																																																																																					"Mast Charges Under Access"), WIRELESS(
																																																																																																																							"Wireless"), CUSTOM(
																																																																																																																									"CUSTOM"), FEASIBLE(
																																																																																																																											"FEASIBLE"), NOT_FEASIBLE(

																																																																																																																													"NOT_FEASIBLE"),MANUAL_FEASIBLE("Manual Feasible"),IZO_PC("IZO PC"),CLOUD_PROVIDER("Cloud Provider"),SERVICE_ID("Service Id"),SLT_VARIENT("SLT Varient"),IZO_PORT("IZO Private Connect Port"),BANDWIDTH("Bandwidth"),VPN_TOPOLOGY("VPN Topology"),IS_DOMESTIC_VPN("Domestic VPN"),SHIFTING_CHARGES("Shifting Charges"),BASE_TX_100("100-Base-TX"),BASE_TX_1000("1000-Base-TX"),GIGABIT_ETHERNET("Gigabit Ethernet"),COMPRESSED_INTERNET_RATIO("Compressed Internet Ratio"),COMPRESSED_INTERNET("Compressed Internet"), LOCAL_LOOP_BANDWIDTH_A("Local Loop Bandwidth A"),LOCAL_LOOP_BANDWIDTH_B("Local Loop Bandwidth B"),LOCAL_LOOP_BANDWIDTH ("Local Loop Bandwidth"),COMPRESSED("Compressed"), MAST_CHARGE_ONNET("Mast Charge onnet"),
	 RADWIN("Radwin"),
	 MAST_CHARGE_OFFNRT("Mast Charge offnrt"),
	 PROVIDER_CHANRGE("Provider Chanrge"),
	 MAN_RENTALS("MAN Rentals"),
	 MAN_OCP("MAN OCP"),
	 LM_MAN_INBUILDING("LM MAN inbuilding"),
	 LM_MAN_MUX("LM MAN MUX"),
	 LM_MAN_BW("LM MAN bw"),
	 CPE_DISCOUNT_MANAGEMENT("CPE Discount Management"),
	 CPE_DISCOUNT_RENTAL("CPE Discount Rental"),
	 CPE_DISCOUNT_OUTRIGHT_SALE("CPE Discount Outright Sale"),
	 CPE_DISCOUNT_INSTALL("CPE Discount Install"),
	 COPPER("Copper"),
	 FIBER("Fiber"),
	 UTB("utp"),
	 FIXED_PORT_MRC("Fixed Port(MRC)"),
	 PORT_NRC("Port NRC"),
	 CPE_INSTALL("CPE Install"),
	 SUPPORT("Support"),
	 RECOVERY("Recovery"),
	 CPE_MANAGEMENT("CPE Management"),
	 SFP_CHARGE("SFP Charge"),
	 CUSTOM_LOCAL_TAXES("Custom and Local taxes"),
	 LOGISTIC_CHARGES("Logistics charges"),
	 LM_NRC("LM NRC"),
	 LM_MRC("LM MRC"),
	 X_CONNECT_MRC("XConnect MRC"),
	 X_CONNECT_NRC("XConnect NRC"),

	 
	 PROW_VALUE("PROW Value"),
	 ARC_CONVERTER_CHARGES("ARC Converter Charges"), 
	 ARC_BW_OFFNET("ARC - BW Offnet"), 
	 ARC_COLOCATION("ARC-Colocation"), 
	 OTC_MODEM_CHARGES("OTC Modem Charges"), 
	 OTC_NRC_INSTALLATION("OTC/NRC - Installation"), 
	 ARC_MODEM_CHARGES("ARC Modem Charges"),
	 ARC_BW_ONNET("ARC - BW Onnet"),
	 CPE_INTL_CHASSIS_FLAG("CPE Intl Chassis flag"),
	 DUAL_CIRCUIT("Dual Circuit"),
	 LICENSE_COST("IZO SDWAN service charges"),
	 WIRELINE("Wireline"),
	 GDE_BOD("GDE_BOD"),
	 ONNETRF("onnetrf"),
	 UBRPMP("TCL UBR PMP"),
	 UBRP2P("Radwin from TCL POP"),
	 UBRP2PMP("Radwin from TCL POP"),
	 CPE_SUPPORT_TYPE("CPE Support Type"),
	 SERVICE_FLAVOUR("Service Flavour"),
	 LOCAL_LOOP_TYPE("Local Loop Type");

	 
	 



	String constantCode;

	private FPConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, FPConstants> CODE_MAP = new HashMap<>();

	static {
		for (FPConstants type : FPConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final FPConstants getByCode(String value) {
		return CODE_MAP.get(value);
	}

	public String getConstantCode() {
		return constantCode;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return this.getConstantCode();
	}
}
