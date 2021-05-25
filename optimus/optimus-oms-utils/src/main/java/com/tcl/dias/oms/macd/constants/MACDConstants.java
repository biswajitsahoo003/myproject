package com.tcl.dias.oms.macd.constants;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * MACDConstants class is to define specification for MACD related constants.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MACDConstants {
	
	
	public static final String MACD_QUOTE_TYPE="MACD";
	public static final String SHIFT_SITE_SERVICE="SHIFT_SITE";
	public static final String ADD_SITE_SERVICE="ADD_SITE";
	public static final String SOURCE_SYSTEM="LEGACY";
	public static final String CHANGE_BANDWIDTH_SERVICE="CHANGE_BANDWIDTH";
	public static final String ADD_IP_SERVICE="ADD_IP";
	public static final String REQUEST_TERMINATION_SERVICE="REQUEST_TERMINATION";
	public static final String ARC="Annual ARC";
	public static final String SOLUTION_QUOTE="Total Solution Quote";
	public static final String COMPONENTS="Components";
	public static final String REFERENCE_TYPE_PRIMARY="primary";
	public static final String CURRENCY_TYPE="INR";
	public static final String NRC="One Time NRC";
    public static final String LOCATION_ID = "Site Address";
    public static final String LAT_LONG = "LAT LONG";
    public static final String PORT_BANDWIDTH_PRIMARY = "PORT BANDWIDTH PRIMARY";
    public static final String LOCAL_LOOP_BANDWIDTH_PRIMARY = "LOCAL LOOP BANDWIDTH PRIMARY";
    public static final String CPE_MODEL_PRIMARY = "CPE MODEL PRIMARY";
    public static final String INTERFACE_PRIMARY = "INTERFACE PRIMARY";
    public static final String IP_ADDRESS_ARRANGEMENT_FOR_ADDITIONAL_IPS = "IP Address Arrangement for Additional IPs";
    public static final String IPV4_ADDRESS_POOL_SIZE_FOR_ADDITIONAL_IPS = "IPv4 Address Pool Size for Additional IPs";
    public static final String IPV6_ADDRESS_POOL_SIZE_FOR_ADDITIONAL_IPS = "IPv6 Address Pool Size for Additional IPs";
    public static final String PORT_BANDWIDTH_SECONDARY = "PORT BANDWIDTH SECONDARY";
    public static final String LOCAL_LOOP_BANDWIDTH_SECONDARY = "LOCAL LOOP BANDWIDTH SECONDARY";
    public static final String CPE_MODEL_SECONDARY = "CPE MODEL SECONDARY";
    public static final String INTERFACE_SECONDARY = "INTERFACE SECONDARY";
    public static final String SINGLE = "Single";
    public static final String DUAL_PRIMARY = "PRIMARY";
    public static final String DUAL_SECONDARY = "SECONDARY";
    public static final String CHANGE_BANDWIDTH = "Change Bandwidth";
    public static final String SHIFT_SITE = "Shift Site";
    public static final String ADD_IP="Add IP";
    public static final String ADD_SITE="Add Site";
    public static final String PARALLEL_BUILD = "downtime_needed_ind";
    public static final String PARALLEL_RUN_DAYS = "downtime_duration";
    public static final String PRIMARY_STRING = "Primary";
    public static final String SECONDARY_STRING = "Secondary";
    public static final String BOTH_STRING = "Both";
    public static final String REFERENCE_TYPE_SECONDAARY="secondary";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String TERMINATION_REQUEST_RECEIVED = "Termination Request Received";    
    public static final String ADD_PUBLIC_IP = "Add Public IP";
    public static final String HOT_UPGRADE="Hot Upgrade";
    public static final String PARALLEL_UPGRADE="Parallel Upgrade";
    public static final String IPv4 = "IPv4";
    public static final String DUAL = "Dual";
    public static final String IPv6 = "IPv6";
    public static final String MACD_ORDER_IN_PROGRESS="MACD_ORDER_IN_PROGRESS";
    public static final String MACD_ORDER_INITIATED="MACD_ORDER_INITIATED";
    public static final String MACD_ORDER_COMMISSIONED="MACD_ORDER_COMMISSIONED";
    public static final String MBPS = "Mbps";
    public static final String POINT_FIVE_MBPS = "0.5 Mbps";
    public static final String POINT_TWOFIVE_MBPS = "0.25 Mbps";
    public static final String FIVE_TWELVE_KBPS = "512 Kbps";
    public static final String TWO_FIFTY_SIX_KBPS = "256 Kbps";
    public static final String TERMINATION_REQUEST_CREATED = "Request Created";
    public static final String KBPS_lOWER_CASE = "kbps";
    public static final String MBPS_LOWER_CASE = "mbps";
    public static final String GBPS_lOWER_CASE = "gbps";
    public static final String GBPS = "Gbps";
    public static final String KBPS = "Kbps";
    public static final String ADDITION_OF_SITE="Addition of Sites";
    public static final String PARALLEL_SHIFTING="Parallel Shifting";
    public static final String SHIFTING="Shifting";

    public static final String ADD_CLOUDVM_SERVICE="ADD_CLOUDVM";
    public static final String CONNECTIVITY_UPGRADE_SERVICE="CONNECTIVITY_UPGRADE";
    public static final String ADDITIONAL_SERVICE_UPGRADE="ADDITIONAL_SERVICE_UPGRADE";
    public static final String REQUEST_FOR_TERMINATION_SERVICE="REQUEST_FOR_TERMINATION";
    public static final String UPGRADE_VM_SERVICE="UPGRADE_VM";
    public static final String DELETE_VM_SERVICE="DELETE_VM";

    public static final String ADD_VM="ADD VM";
    public static final String CONNECTIVITY_UPGRADE="CONNECTIVITY UPGRADE";
    public static final String ADDITIONAL_UPGRADE="ADDITIONAL SERVICE UPGRADE";
    public static final String REQUEST_FOR_TERMINATION="REQUEST FOR TERMINATION";
    public static final String UPGRADE_VM="UPGRADE VM";
    public static final String DELETE_VM="DELETE VM";
    public static final String LM_BSO_SHIFTING = "LM Shifting / BSO Change";
    public static final String PARALLEL_RUNDAYS = "Parallel Rundays";
    public static final String BULK_UPGRADE = "Bulk Upgrade";
    public static final String HEADER_SERIAL_NO = "SR#";
    public static final String SERVICE_ID = "Service ID";
    public static final String ADDRESS= "Address";
    public static final String PORT_BANDWIDTH = " Port Bandwidth";
    public static final String LOCAL_LOOP_BANDWIDTH= "Local Loop Bandwidth";
    public static final String CPE_MODEL_CHASSIS= "CPE Model Chassis";
    public static final String INTERFACE= "Interface";

    public static final String CHANGE_ORDER = "Change Order";
    public static final String MACD = "MACD";
    public static final String NEW = "NEW";
    public static final String SINGLE_ALL_CAPS = "SINGLE";
    
    public static final List<String> IPC_MACD_SERVICE_LIST = ImmutableList.of(ADD_CLOUDVM_SERVICE,CONNECTIVITY_UPGRADE_SERVICE,ADDITIONAL_SERVICE_UPGRADE,REQUEST_FOR_TERMINATION_SERVICE,UPGRADE_VM_SERVICE,DELETE_VM_SERVICE);

    public static final String OTHERS = "OTHERS";
    public static final String OTHERS_SFDC="Change Order \u2013 Others";
    public static final String SITE_SHIFTED = "siteShifted";
    public static final String DIFFERENTIAL_MRC="DifferentialMRC";
    public static final String OTHERS_COF="– Others";
    public static final String OFFNET_SMALL_CASE = "offnet";
    public static final String ONNET_SMALL_CASE = "onnet";
    public static final String VENDOR_ID = "vendor_id";
    public static final String VENDOR_NAME = "vendor_name";
    public static final String ONNET_WIRELINE = "OnnetWL";
	public static final String OFFNET_WIRELINE = "OffnetWL";
	public static final String ONNET_RF = "OnnetRF";
	public static final String OFFNET_RF = "OffnetRF";
	
	public static final String BSO_CHANGE = "-BSO Change";
	public static final String LM_SHIFTING = "LM Shifting";
	public static final String LM_SHIFTING_BSO_CHANGE = "LM Shifting-BSO Change";
	public static final String HYPHEN_LM_SHIFTING = "-LM Shifting";
	public static final String OFFERING_NAME = "offeringName";
	public static final String DUAL_SMALL_CASE = "dual";
	

    // NPL MACD
    
    public static final String SITEA = "siteA";
    public static final String SITEB = "siteB";
    public static final String NPL_COMMON = "NPL Common";
    public static final String SI_SITEA = "SiteA";
    public static final String SI_SITEB = "SiteB";
    public static final String INTERFACE_TYPE_A_END = "Interface Type - A end";
    public static final String INTERFACE_TYPE_B_END = "Interface Type - B end";
    public static final String LINK = "Link";
    public static final String MRC="Annual MRC";
    public static final String INTL_CURRENCY_TYPE="USD";
    public static final String COMPLETED="COMPLETED";
    
    public static final String TERMINATION  = "TERMINATION";
    public static final String MACD_ORDER_CANCELED="MACD_ORDER_CANCELED";
    
    
}
