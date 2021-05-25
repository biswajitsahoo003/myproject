package com.tcl.dias.serviceinventory.constants;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class MACDConstants {
	public static final String DUAL_PRIMARY = "DUAL PRIMARY";
	public static final String DUAL_SECONDARY = "DUAL SECONDARY";
	public static final String COS_1 = "cos 1";
	public static final String COS_2 = "cos 2";
	public static final String COS_3 = "cos 3";
	public static final String COS_4 = "cos 4";
	public static final String COS_5 = "cos 5";
	public static final String COS_6 = "cos 6";
	public static final String ZERO_PERCENT = "0%";
	
	public static final String 	VENDOR_ID = "vendor_id";
	public static final String VENDOR_NAME = "vendor_name";
	
	//IPC
	public static final String ADD_CLOUDVM_SERVICE="ADD_CLOUDVM";
    public static final String CONNECTIVITY_UPGRADE_SERVICE="CONNECTIVITY_UPGRADE";
    public static final String ADDITIONAL_SERVICE_UPGRADE="ADDITIONAL_SERVICE_UPGRADE";
    public static final String REQUEST_FOR_TERMINATION_SERVICE="REQUEST_FOR_TERMINATION";
    public static final String UPGRADE_VM_SERVICE="UPGRADE_VM";
    public static final String DELETE_VM_SERVICE="DELETE_VM";
    public static final String NEW_ORDER="NEW";
    public static final String MACD_ORDER="MACD";
    public static final String ACTIVE="Active";
    public static final String TERMINATED="Terminated";
	public static final List<String> IPC_ORDER_ATTRIBUTE_LIST = ImmutableList.of("securityGroup","fireWall");
	
	//GDE MACD
	public static final String SITEA = "siteA";
    public static final String SITEB = "siteB";
    public static final String SI_SITEA = "SiteA";
    public static final String SI_SITEB = "SiteB";
    
    //Termination
    public static final String LOCAL_LOOP_BW = "local_loop_bw";
    public static final String BURSTABLE_BW = "burstable_bw";
    public static final String SITE_TYPE = "Site Type";
    public static final String CPE_BASIC_CHASSIS = "CPE Basic Chassis";
    public static final String  END_MUX_NODE_IP ="endMuxNodeIp";
    public static final String  END_MUX_NODE_NAME ="endMuxNodeName";
    public static final String  END_MUX_NODE_PORT ="endMuxNodePort";
    public static final String  MUX_MAKE ="muxMake";
    public static final String  STRUCTURE_TYPE ="structureType";
    public static final String  MUX_MAKE_MODEL ="muxMakeModel";
    public static final String LAST_MILE_SCNEARIO = "lastMileScenario";
    public static final String LM_CONNECTION_TYPE = "lmConnectionType";
    public static final String MAST_PO_NUMBER = "mastPoNumber";
    public static final String OFFNET_SUPPLIER_BILLSTART_DATE = "offnetSupplierBillStartDate";
    public static final String BTS_DEVICE_TYPE = "bts_device_type";
    public static final String A_END_BACKHAUL_PROVIDER = "A_END_BACKHAUL_PROVIDER";
    public static final String  B_END_BACKHAUL_PROVIDER = "B_END_BACKHAUL_PROVIDER";
    public static final String  BACKHAUL_PROVIDER = "BACKHAUL_PROVIDER";
    public static final String RFMAKE = "rfMake";
    public static final String CPE_SERIAL_NO = "cpe_serial_no";
    public static final String OEM_VENDOR = "oem_vendor";
    public static final String ATTRIBUTE_NAME = "attribute_name";
    public static final String ATTRIBUTE_VALUE = "attribute_value";
    public static final String PRODUCT_FLAVOUR = "product_flavour";
    public static final String LL_ARRANGE_BY = "LL_ARRANGE_BY";
    public static final String SHARED_LM_REQUIRED = "SHARED_LM_REQUIRED";
}
