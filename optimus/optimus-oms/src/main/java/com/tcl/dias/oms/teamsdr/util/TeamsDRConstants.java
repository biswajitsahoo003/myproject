package com.tcl.dias.oms.teamsdr.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Class to hold constants of UCaaS Teams DR
 *
 * @author Srinivasa Raghavan
 *
 */
public class TeamsDRConstants {

	// product level constants
	public static final String UCAAS_TEAMSDR = "UCDR";
	public static final String MICROSOFT_CLOUD_SOLUTIONS = "Microsoft Cloud Solutions";
	public static final String APPROVE = "APPROVE";
	public static String NONE = "NONE";

	// offering level constants
	public static String MANAGED_PLAN = "Managed Plan";
	public static String CONNECTED_PLAN = "Connect Plan";
	public static String CUSTOM_PLAN = "Custom Plan";
	public static String ADDON_SERVICES = "Add-On Services";
	public static String BUNDLED = "Bundled";
	public static String ATOMIC = "Atomic";
	public static final String MEDIA_GATEWAY = "Media Gateway";
	public static final String PLAN = "Plan";
	public static String MS_LICENSE = "MS License";
	public static final String MICROSOFT_LICENSE = "Microsoft License";

	// license attributes
	public static String NO_OF_EXISTING_E1_LICENSE = "Number of Existing E1 License";
	public static String NO_OF_EXISTING_E2_LICENSE = "Number of Existing E3 License";
	public static String NO_OF_EXISTING_E3_LICENSE = "Number of Existing E5 License";
	public static String NO_OF_PS_LICENSE = "Number of Phone System License";
	public static String LICENSE_REQUIREMENT = "License Requirement";
	public static String DOMAIN_AVAILABILITY = "Domain Availability";
	public static String MS_DOMAIN_AVAILABILITY = "MS Domain Availability";
	public static Set<String> EXISTING_LICENSE_ATTRIBUTES = Sets.newHashSet(NO_OF_EXISTING_E1_LICENSE,
			NO_OF_EXISTING_E2_LICENSE, NO_OF_EXISTING_E3_LICENSE, NO_OF_PS_LICENSE, LICENSE_REQUIREMENT,
			DOMAIN_AVAILABILITY, MS_DOMAIN_AVAILABILITY);
	public static Set<String> LICENSE_ATTRIBUTES = Sets.newHashSet(LICENSE_REQUIREMENT, DOMAIN_AVAILABILITY,
			MS_DOMAIN_AVAILABILITY);
	public static final String LICENSE_REQUIRED = "Do you need E1/E3 + Phone System or E5 Licenses ?";

	// generic
	public static final String TWELVE_MONTHS = "12 months";
	public static String CONTRACT_PERIOD = "contractPeriod";
	public static final String CUSTOM_CONFIGURATION_MEDIA_GATEWAY = "Custom-Configuration-Media-Gateway";
	public static final String PRIMARY = "Primary";
	public static final String TEAMSDR_MG_SITE = "TEAMSDR_MG_SITE";
	public static final String SELECT_YOUR_PAYMENT_MODEL = "Select your payment model";
	public static final String FIXED = "Fixed";
	public static final String USAGE = "Usage";
	public static final String NON_RECURRING = "Non-Recurring";
	public static final String RECURRING = "Recurring";
	public static final String OVERAGE = "Overage";
	public static final String CPE_RENTAL_CHARGES = "CPE Rental Charges";
	public static final String CPE_OUTRIGHT_CHARGES = "CPE Outright Charges";
	public static final String CPE_AMC_CHARGES = "CPE Annual Maintenance Charges";
	public static final String MANAGEMENT_AND_MONITORING_CHARGES = "Management & Monitoring Charges";
	public static final String CPE_DELIVERY_CHARGES = "CPE Delivery Charges";
	public static final Set<String> FIXED_CHARGES = Sets.newHashSet(NON_RECURRING,RECURRING,OVERAGE);
	public static final Set<String> USAGE_CHARGES = Sets.newHashSet(NON_RECURRING,RECURRING,USAGE);
	public static final Set<String> ALL_CHARGES = Sets
			.newHashSet(NON_RECURRING, RECURRING, OVERAGE, USAGE, CPE_RENTAL_CHARGES, CPE_AMC_CHARGES,
					MANAGEMENT_AND_MONITORING_CHARGES, CPE_DELIVERY_CHARGES, CPE_OUTRIGHT_CHARGES);
	public static final Set<String> COMMERCIAL_COMPONENTS = Sets
			.newHashSet(NON_RECURRING, RECURRING, CPE_RENTAL_CHARGES, CPE_AMC_CHARGES,
					MANAGEMENT_AND_MONITORING_CHARGES, CPE_OUTRIGHT_CHARGES);
	public static final Set<String> MEDIA_GATEWAY_COMMERCIALS = Sets
			.newHashSet(CPE_RENTAL_CHARGES, CPE_AMC_CHARGES, CPE_OUTRIGHT_CHARGES, MANAGEMENT_AND_MONITORING_CHARGES);
	public static final String ATTRIBUTES = "ATTRIBUTES";
	public static final String SERVICE_ATTRIBUTES = "Service Attributes";
	public static final String TEAMSDR_SERVICE_ATTRIBUTES = "teamsdr.service";
	public static final String TEAMSDR_CONFIG_ATTRIBUTES = "teamsdr.config";
	public static final String TEAMSDR_CHARGES_ATTRIBUTES = "teamsdr.charges";
	public static final String TEAMSDR_SITE_ATTRIBUTES = "teamsdr.sites";
    public static final String TEAMSDR_MEDIAGATEWAY_ATTRIBUTES = "teamsdr.mgcharges";
	public static final String CPE_BOM = "CPE BOM";
	public static final String TEAMSDR_LICENSE_CHARGES = "teamsdr.licensecharges";
	public static final String TEAMSDR_LICENSE_ATTRIBUTES = "teamsdr.license";
	public static final String TATA_VOICE_NEEDED_ATTRIBUTE = "Do you want Tata Voice ?";
	public static final String NOT_APPLICABLE = "NA";

	// Configuration Attributes.
	public static final String IS_GSC = "isGsc";
	public static final String IS_EXCEPTIONAL = "isExceptional";
	public static final String IS_REGULATED = "isRegulated";
	public static final String IS_DOMESTIC_VOICE = "isDomesticVoice";
	public static final String IS_OUTBOUND = "isOutbound";
	public static final String IS_MEDIAGATEWAY = "isMediaGateway";
	public static final Set<String> CONFIGURATION_ATTRIBUTES = Sets.newHashSet(IS_GSC,IS_EXCEPTIONAL,IS_REGULATED,
			IS_DOMESTIC_VOICE,IS_OUTBOUND,IS_MEDIAGATEWAY);
	public static final String SS_REF = "Service Schedule v2.1 20201124";

	public static final String MEDIA_GATEWAY_PURCHASE_TYPE = "Media Gateway Purchase Type";
	public static final String AUDIO_CODES = "Audio Codes";
	public static final String RIBBON = "RIBBON";
	public static final String OUTRIGHT_PURCHASE = "Outright Purchase";
	public static final String RENTAL_PURCHASE = "Rental Purchase";

	// SFDC
	public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static final String MONTHS = "months";
	public static final String OPTIMUS_OPPORTUNITY="Optimus Opportunity";

	public static final String MANAGED_SERVICE = "MANAGED_SERVICE";
	public static final String SIMPLE_SERVICES = "Simple Services";
	public static final String PROFESSIONAL_SERVICES = "Professional Services";
	public static final String USAGE_RATES = "USAGE_RATES";
	public static final String PROFESSIONAL_SERVICE = "PROFESSIONAL_SERVICE";
	public static final String LICENSE = "LICENSE";
	public static final String EQUIPMENT_RENTAL_AC = "EQUIPMENT_RENTAL_AC";
	public static final String EQUIPMENT_OUTRIGHT_AC = "EQUIPMENT_OUTRIGHT_AC";
	public static final String EQUIPMENT_RENTAL_RIBBON = "EQUIPMENT_RENTAL_RIBBON";
	public static final Map<String, String> HSN_CODES = new HashMap<String, String>() {
		{
			put(MANAGED_SERVICE, "998429");
			put(USAGE_RATES, "998429");
			put(PROFESSIONAL_SERVICE, "998429");
			put(LICENSE, "998429");
			put(EQUIPMENT_RENTAL_AC, "998429");
			put(EQUIPMENT_OUTRIGHT_AC, "85176290");
			put(EQUIPMENT_RENTAL_RIBBON, "998429");
		}
	};

	public static final String EQUIPMENT = "Equipment";
	public static final String PLAN_ATTRIBUTES = "Plan Attributes";
	public static final String EQUIPMENT_ATTRIBUTES = "Equipment Attributes";
	public static final String PLAN_REF_NAME = "plan.attributes";
	public static final String EQUIPMENT_REF_NAME = "equipment.attributes";
	public static final String _PRIMARY = "primary";
	public static final String SECONDARY = "secondary";
	public static final String TEAMSDR = "TEAMSDR";
	public static final String GST_ADDRESS = "GST_ADDRESS";
	public static final String APPLICATION_ZIP = "application/zip";
	public static final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";
	public static final String ZIP = ".zip";
	public static final String MEDIA_GATEWAY_ATTRIBUTES = "Media Gateway Attributes";
	public static final String SIP = "SIP";
	public static final String PRI = "PRI";
	public static final String TDM = "TDM";
	public static final String NUMBER_OF_PRI = "Number of PRI";
	public static final String NUMBER_OF_SESSIONS = "Number of Sessions";
	public static final String REDUNDANT_GATEWAY_ATTRIBUTE_NAME = "Do you need Redundant Gateway (HA)";
	public static final String AMC_SUPPORT_PLAN_ATTRIBUTE_NAME = "Select AMC support plan";
	public static final String AHR_ATTRIBUTE_NAME = "Do you need Advance Hardware Replacement ?";
	public static final String SITE_ADDRESS_1 = "Site Address 1";
	public static final String SITE_ADDRESS_2 = "Site Address 2";
	public static final String SITE_ADDRESS_3 = "Site Address 3";
	public static final String PINCODE = "Pincode";
	public static final String STATE = "State";
	public static final String AUDIO_CODE = "Audio Code";
	public static final String _RIBBON = "Ribbon";
	public static final String MANAGEMENT = "Management";
	public static final String SKU_DRIVEN = "SKU Driven";
	public static final String HSN_CODE = "HSN_CODE";
	public static final String CPE_BOM_RESPONSE = "CPE Bom Response";
	public static final String VENDOR_NAME = "Vendor Name";
	public static final String GSC_WITH_TEAMSDR = "Global SIP Connect Service for Tata Communications with Direct Routing";
	public static final String EFFECTIVE_MSA_DATE = "effectiveMSADate";
}
