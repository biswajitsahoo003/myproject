package com.tcl.dias.common.constants;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * @author Syed Ali.
 * This file contains all the constants required for teamsdr odr.
 */
public class TeamsDROdrConstants {
	public static final String MICROSOFT_CLOUD_SOLUTIONS = "Microsoft Cloud Solutions";
	public static final String MEDIA_GATEWAY = "Media Gateway";
	public static final String FIXED = "Fixed";
	public static final String USAGE = "Usage";
	public static final String NON_RECURRING = "Non-Recurring";
	public static final String RECURRING = "Recurring";
	public static final String OVERAGE = "Overage";
	public static final String CPE_RENTAL_CHARGES = "CPE Rental Charges";
	public static final String CPE_OUTRIGHT_CHARGES = "CPE Outright Charges";
	public static final String CPE_AMC_CHARGES = "CPE Annual Maintenance Charges";
	public static final String PLAN_COMMERCIAL = "Plan Commercial";
	public static final String LICENSE_COMMERCIAL = "License Commercial";
	public static final String LICENSE_MANAGEMENT = "License Management Charge";
	public static final String SITE_COMMERCIAL = "Site Commercial";
	public static final String MANAGEMENT_AND_MONITORING_CHARGES = "Management & Monitoring Charges";
	public static final String SITE = "Site";
	public static final String TEAMSDR_SITE_ATTRIBUTES = "teamsdr.sites";
	public static final String TEAMSDR_MEDIAGATEWAY_ATTRIBUTES = "teamsdr.mgcharges";
	public static final String TEAMSDR_LICENSE_CHARGES = "teamsdr.licensecharges";
	public static final String TEAMSDR_LICENSE_ATTRIBUTES = "teamsdr.license";
	public static final String CHARGE_ITEM = "Charge Item";
	public static final String ATTRIBUTES = "ATTRIBUTES";
	public static final String COMMON = "COMMON";
	public static final String SITE_CONFIGURATION = "Site Configuration";
	public static final String SITE_ADDRESS_1 = "Site Address 1";
	public static final String SITE_ADDRESS_2 = "Site Address 2";
	public static final String SITE_ADDRESS_3 = "Site Address 3";
	public static final String STATE = "State";
	public static final String PINCODE = "Pincode";
	public static final String TEAMSDR_SOLUTION = "TEAMSDR_SOLUTION";
	public static final String PUBLIC_IP = "Public Ip";
	public static final String PRODUCT_NAME = "productName";
	public static final String PRODUCT_CODE = "productCode";
	public static final String PLAN_REF_NAME = "plan.attributes";
	public static final String EQUIPMENT_REF_NAME = "equipment.attributes";
	public static final String AGREEMENT_TYPE = "agreement_type";
	public static final String PROVIDER = "provider";
	public static final String CONTRACT_PERIOD = "contract_period";
	public static final String NO_OF_LICENSES = "no_of_licenses";
	public static final String SFDC_PRODUCT_NAME = "sfdc_product_name";
	public static final String NO_OF_NAMED_USERS = "no_of_named_users";
	public static final String NO_OF_COMMON_AREA_DEVICES = "no_of_common_area_devices";
	public static final String TOTAL_USERS = "total_users";
	public static final String TOTAL_COMMITTED_USERS = "Total Committed Users";
	public static final String TEAMSDR_SERVICE_ATTRIBUTES = "teamsdr.service";
	public static final String TEAMSDR_CONFIG_ATTRIBUTES = "teamsdr.config";
	public static final String PLAN = "Plan";
	public static final String PLAN_CHARGE_ITEM = "planChargeItem";
	public static final String SERVICE_CHARGE_ITEM = "serviceChargeItem";
	public static final String TOTAL_NO_OF_LICENSE = "total_licenses";
	public static final String DO_YOU_NEED_LICENSE = "Do you need license?";
	public static final String PLAN_ATTRIBUTES = "Plan Attributes";
	public static final String DO_YOU_NEED_E1_E3_LICENSE = "Do you need E1/E3 + Phone System or E5 Licenses ?";
	public static final String DID_ATTRIBUTE = "Do customer wants to do allocation of DIDs to User?";
	public static final String DO_CUSTOMER_NEEDS_DID = "Do customer needs DID ?";
	public static final String DEMARCATION_ROOM = "Demarcation - Room";
	public static final String RACK = "Rack";
	public static final String ROOM = "Room";
	public static final String BUILDING_NAME = "Building Name";
	public static final String DEMARCATION_WING = "Demarcation - Wing";
	public static final String DEMARCATION_FLOOR = "Demarcation - Floor";
	public static final String GST_ADDRESS = "GST Address";
	public static final String DEMARCATION_BUILDING_NAME = "Demarcation - Building Name";
	public static final String FLOOR = "Floor";
	public static final String LOCAL_IT_CONTACT_NAME = "Local IT Contact Name";
	public static final String LOCAL_IT_CONTACT_NUMBER = "Local IT Contact Number";
	public static final String LOCAL_IT_EMAIL_ID = "Local IT Contact E-mail ID";
	public static final String CUSTOM_PLAN = "Custom Plan";
	public static final String MG_QUANTITY = "mediagatewayQuantity";
	public static final String T1_TECHNICIAN = "T1 Technician";
	public static final String T2_TECHNICIAN = "T2 Technician";
	public static final String T3_TECHNICIAN = "T3 Technician";
	public static final String DESIGN_ENGINEER = "Design Engineer";
	public static final String PROJECT_MANAGEMENT = "Project Management";
	public static final String REMOTE = "Remote";
	public static final String EXPEDITED = "Expedited";
	public static final String CITY = "City";
	public static final String VENDOR_NAME = "Vendor Name";
	public static final Set<String> ALL_CHARGES = Sets.newHashSet(NON_RECURRING, RECURRING, OVERAGE, USAGE,
			CPE_RENTAL_CHARGES,MANAGEMENT_AND_MONITORING_CHARGES, CPE_OUTRIGHT_CHARGES);
	public static final Set<String> SERVICE_LEVEL_CHARGES = Sets.newHashSet(T1_TECHNICIAN, T2_TECHNICIAN, T3_TECHNICIAN,
			DESIGN_ENGINEER, PROJECT_MANAGEMENT, REMOTE, EXPEDITED);
	public static final String ENDPOINT = "Endpoint";
	public static final String SP_LE_COUNTRY = "spLeCountry";
	public static final String NEW_ORDER = "New Order";
	public static final String COF_REF_ID = "cofReferenceId";
	public static final String COF_CREATED_TIME = "cofCreatedTime";
}
