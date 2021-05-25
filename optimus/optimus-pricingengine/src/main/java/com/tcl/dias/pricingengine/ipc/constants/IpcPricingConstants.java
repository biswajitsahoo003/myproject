package com.tcl.dias.pricingengine.ipc.constants;

/**
 * This file contains the IpcPricingConstants.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class IpcPricingConstants {
	
	public static final String SSD="ssd";
	public static final String SATA="sata";
	public static final String SAS="sas";
	public static final String VCPU="vCPU";
	public static final String VRAM="vRAM";
	public static final String SSDIOPS="ssdIOPS";
	public static final String MGMT_CHARGER_PER_VM="mgmt_charger_per_vm";
	public static final String PER_GB_VRAM_ESXI="per_gb_vram_for_esxi";
	public static final String MS_LICENCE="ms_license";
	public static final String RHEL_LICENCE_SMALL="rhel_license_small";
	public static final String RHEL_LICENCE_LARGE="rhel_license_large";
	public static final String OEL_LICENCE="oel_license";
	public static final String SUSE_LICENSE_SMALL="suse_linux_license_small";
	public static final String SUSE_LICENSE_MEDIUM="suse_linux_license_medium";
	public static final String SUSE_LICENSE_LARGE="suse_linux_license_large";
	public static final String BASE_VDOM="base_vdom";
	public static final String PER_MB="per_mb";
	public static final String MANAGED_UNMANAGED="managed_unmanaged";
	public static final String TOTAL_VM_QTY_VALUE="total_vm_quantity_value";
	public static final String TERM_IN_MONTHS="term_in_months";
	public static final String SHEET="sheet";
	public static final String WINDOWS="windows";
	public static final String RHEL="rhel";
	public static final String OEL="oel";
	public static final String SUSE="suse linux";
	public static final String MANAGED="Managed";
	public static final String UNMANAGED="Unmanaged";
	public static final String ESXI="ESXI";

	public static final String ADDITIONAL_DISCOUNT_PERCENTAGE = "additional_discount";
	public static final String IPC_FINAL_PRICE = "ipc_final_price";
	
	public static final String ADDITIONAL_IP = "additionalIp";
	public static final String BACKUP = "backup";
	public static final String VDOM = "vDom";
	public static final String VPN_CONNECTION = "VPNConnection";
	public static final String DATABASE_LICENSES = "DatabaseLicenses";

	public static final String COMMON_ERROR = "common.exception";
	public static final String CITY_CODE ="CITY_CODE";
	public static final String CITY ="CITY";
	public static final String COUNTRY_CODE ="COUNTRY_CODE";
	public static final String LOCATION_ID ="LOCATION_ID";
	public static final String STATE ="STATE";
	
	public static final String PPU_VM_HOURLY = "Hourly";
	public static final String PPU_VM_DAILY = "Daily";
	public static final String PPU_VM_MONTHLY = "Monthly";
	public static final String RESERVED_VM = "Reserved";
	
	public static final String QUANTITY = "Quantity";
	public static final String MAPPED_VM = "Mapped VMs";

	public static final String INDIA = "INDIA";
	public static final String CURRENCY_USD = "USD";
	
	public static final String L2_THROUGHPUT = "L2 Throughput";
	public static final String L2_THROUGHPUT_10GIG = "10 Gig";
	public static final String L2_THROUGHPUT_1GIG = "1 Gig";
	public static final String CABLE_TYPE = "Cable Type";
	public static final String CABLE_TYPE_FIBER = "Fiber";
	public static final String CABLE_TYPE_COPPER = "Copper";
	public static final String SHARED_SWITCH_PORT = "Shared Switch Port";
	
	public static final String PRICING_ITEM_PER_CPU = "per vCPU";
	public static final String PRICING_ITEM_PER_GB_RAM = "per GB vRAM";
	public static final String PRICING_ITEM_PER_GB_SSD = "per GB SSD";
	public static final String PRICING_ITEM_PER_GB_SSD_IOPS = "per GB SSD Additional IOPS";
	public static final String PRICING_ITEM_PER_GB_HDD_SAS = "per GB HDD SAS";
	public static final String PRICING_ITEM_PER_GB_HDD_SATA = "per GB HDD SATA";
	public static final String PRICING_ITEM_PER_VM_MGMT = "per VM Management";
	public static final String PRICING_ITEM_PER_GB_RAM_ESXI = "per GB vRAM for ESX Hypervisor";
	public static final String PRICING_ITEM_PER_MS_LICENSE = "per MS License";
	public static final String PRICING_ITEM_PER_RHEL_LICENSE_SMALL = "per RHEL License - Small VM (<=8 core)";
	public static final String PRICING_ITEM_PER_RHEL_LICENSE_LARGE = "per RHEL License - Large VM (>8 core)";
	public static final String PRICING_ITEM_PER_SUSE_LICENSE_SMALL = "per SUSE License - Small VM (1-2 core)";
	public static final String PRICING_ITEM_PER_SUSE_LICENSE_MEDIUM = "per SUSE License - Medium VM (3-4 core)";
	public static final String PRICING_ITEM_PER_SUSE_LICENSE_LARGE = "per SUSE License - Large VM (5+ core)";
	public static final String PRICING_ITEM_PER_OEL_LICENSE = "per OEL License";
	public static final String PRICING_ITEM_BASE_VDOM = "baseVDOM (2Mbps)";
	public static final String PRICING_ITEM_PER_MB = "per MB";
	public static final String PRICING_ITEM_DB_PREFIX = "per ";
	public static final String PRICING_ITEM_DB_MANAGED_SUFFIX = " (Managed) License";
	public static final String PRICING_ITEM_DB_UNMANAGED_SUFFIX = " (Unmanaged) License";
	public static final String PRICING_ITEM_DR_PREFIX = "per ";
	public static final String PRICING_ITEM_DR_SUFFIX = " License";
	public static final String PRICING_ITEM_PER_PUBLIC_IPV4 = "per Public IPv4 IP";
	public static final String PRICING_ITEM_PER_GB_COMMVAULT = "per GB Commvault";
	public static final String PRICING_ITEM_PER_GB_ICS_VALUE_BASED_POLICY = "per GB ICS Value Based Policy";
	public static final String PRICING_ITEM_PER_GB_ICS_GEO_RESILIENT_POLICY = "per GB ICS Geo-Resilient Policy";
	public static final String PRICING_ITEM_PER_10GIG_FIBER_CONNECTION_MRC = "per 10 Gig Fiber Connection (MRC)";
	public static final String PRICING_ITEM_PER_10GIG_FIBER_CONNECTION_NRC = "per 10 Gig Fiber Connection (NRC)";
	public static final String PRICING_ITEM_PER_1GIG_FIBER_CONNECTION_MRC = "per 1 Gig Fiber Connection (MRC)";
	public static final String PRICING_ITEM_PER_1GIG_FIBER_CONNECTION_NRC = "per 1 Gig Fiber Connection (NRC)";
	public static final String PRICING_ITEM_PER_1GIG_COPPER_CONNECTION_MRC = "per 1 Gig Copper Connection (MRC)";
	public static final String PRICING_ITEM_PER_1GIG_COPPER_CONNECTION_NRC = "per 1 Gig Copper Connection (NRC)";
	public static final String PRICING_ITEM_PER_SHARED_SWITCH_PORT = "per Shared Switch Port";
	public static final String PRICING_ITEM_PER_CPU_FOR_SAP = "per vCPU for SAP";
	public static final String PRICING_ITEM_PER_GB_RAM_FOR_SAP = "per GB vRAM for SAP";
	public static final String PRICING_ITEM_PER_RHEL_LICENSE_SMALL_FOR_SAP = "per RHEL License for SAP - Small VM (<=8 core)";
	public static final String PRICING_ITEM_PER_RHEL_LICENSE_LARGE_FOR_SAP = "per RHEL License for SAP - Large VM (>8 core)";
	public static final String PRICING_ITEM_PER_SUSE_LICENSE_SMALL_FOR_SAP = "per SUSE License for SAP - Small VM (1-2 core)";
	public static final String PRICING_ITEM_PER_SUSE_LICENSE_MEDIUM_FOR_SAP = "per SUSE License for SAP - Medium VM (3-4 core)";
	public static final String PRICING_ITEM_PER_SUSE_LICENSE_LARGE_FOR_SAP = "per SUSE License for SAP - Large VM (5+ core)";
	
	public static final String PRICING_ATTRIBUTE_PPU_VM_HOURLY_PREMIUM_RATE = "PPU VM - Hourly Premium Rate";
	public static final String PRICING_ATTRIBUTE_PPU_VM_DAILY_PREMIUM_RATE = "PPU VM - Daily Premium Rate";
	public static final String PRICING_ATTRIBUTE_PPU_VM_MONTHLY_PREMIUM_RATE = "PPU VM - Monthly Premium Rate";
}
