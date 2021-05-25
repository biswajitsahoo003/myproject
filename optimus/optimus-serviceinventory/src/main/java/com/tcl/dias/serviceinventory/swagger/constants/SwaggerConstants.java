package com.tcl.dias.serviceinventory.swagger.constants;

/**
 * Swagger Constants related information
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface SwaggerConstants {

	interface ApiOperations {
		interface SIOrder {
			String SI_ORDER_DETAILS = "Get SI Order details";
			String GET_ACCESS_TYPE_DETAILS = "Get SI Order details";
		}

		interface InventoryDetails {
			String PRODUCT_FAMILY_STATS = "get product family stats";
			String SERVICE_DATA = "get service data by access type and GSC product";
			String GET_IP_ATTRIBUTE_DETAILS = "get IP attribute for perticular service id";
			String GET_SI_DETAILS = "get detailed information for particular service id";
			String GET_SI_FILTERS_DETAILS="get service filter details";
			String GET_SI_DIDSERVICE_DETAILS="get service filter details";
			String GET_SI_TOLLFREE_DETAILS="get service filter details";
			String GET_CLOUD_SERVICE_DETAILS ="Get Cloud Service details based on product id";
			String GET_PRODUCT_SI_DETAILS = "Get service detailed information for particular service id and product id";
			String GET_SI_HANDOVER_DATA="Get Service Inventory Handover Data";
			String UPDATE_COS_MESSAGE_PREFERENCES = "Update the cos message preference for the particular service id";
			String GET_ATTRIBUTE_DETAILS = "get attribute for perticular service id";
			String GET_SI_ASSET_DETAILS_BY_CLOUDCODE = "get asset details with price for the cloud code";
		}

		interface ConfigurationDetails {
			String ACCESS_TYPE_CONFIGURATIONS = "get access type based configurations";
			String CONFIGURATION_NUMBERS = "get numbers from configurations";
			String CONFIGURATION_SITES = "get sites from configurations";
		}
		
		interface ServiceDetail {
			String GET_SOURCE_COUNTRY = "get source country for vpn id";
		}
		
		interface ATTACHMENT {
			public static final String DOWNLOAD_ATTACHMENT = "Download attachment for given attachment Id";
			public static final String NUMBER_LIST_BY_SITE_AS_PDF = "Get list of numbers by site as pdf";
		}

		interface PartnerCustomerLeDetail{
			String GET_PARTNER_CUSTOMER_LE_DETAIL= "get customer and partner le details based on logged in user partner detail";
		}

		interface SDWANSITES {
			public static final String GET_SDWAN_SITE_DETAILS = "Get SDWAN site details for the given customerId";
			public static final String GET_SDWAN_SITE_DETAILS_BASED_ON_FILTER = "Search and sort SDWAN site detais based on filter";
			public static final String GET_SDWAN_SERVICE_DETAILS = "Search and sort SDWAN site detais based on filter";
			public static final String GET_SDWAN_SITE_PERFORMANCE_DETAILS = "Get SDWAN site details to get the performance details for the given customerId";
			public static final String GET_PRODUCT_FLAVOUR_DETAILS = "Get Product Flavour Details";
		}

		interface SDWANCPE {
			public static final String GET_ASSET_DETAILS = "Get SDWAN CPE details for given customer id";
			public static final String GET_SDWAN_CPE_DETAILS_BASED_ON_FILTER = "Search and sort SDWAN CPE detais based on filter";
			public static final String GET_SDWAN_CPE_FULL_DETAILS = "Get CPE details";
			public static final String UPDATE_SDWAN_CPE_TEMPLATE_ALIAS = "Update SDWAN Alias for cpe and template ";
		}

		interface SDWANTEMPLATE {
			public static final String GET_TEMPLATE_DETAILS = "Get SDWAN TEMPLATE details for given customer ID";
			public static final String GET_TEMPLATE_FULL_DETAILS = "Get SDWAN TEMPLATE full details";
			public static final String GET_TEMPLATE_DETAILS_BASED_ON_FILTER = "Search and sort Templates based on filter";
			public static final String GET_SITELIST_CONFIG_VIEW = "Get SiteList Configuration Detailed View";
		}
		
		interface IzoSdwanMacd {
			public static final String GET_APPLICATIONS = "API to get both predefined and user defined applications from VERSA";
			public static final String GET_POLICY = "API to get traffic steering and QOS policies from VERSA";
			public static final String GET_POLICY_DETAILS = "API to get detail information of traffic steering and QoS polices from VERSA";
			public static final String GET_APPLICATION_DETAILS = "API to get Application details for selected application";
			public static final String CREATE_USER_DEFINED_APPLICATION = "API to create user defined applications";
			public static final String EDIT_POLICY_DETAILS = "Edit policy information and save to VERSA";
			public static final String GET_APPLICATIONS_DETAILS = "Get predefined and userdefined application details from VERSA";
			public static final String UPDATE_OR_CLONE_USER_DEFINED_APPLICATION = "API to update and clone user defined applications";
			public static final String GET_APPLS_BW_CONSUMPTION = "API to get bandwidth consumed by application";
			public static final String GET_SDWAN_TASK_DETAILS = "API to get sdwan task details";
			public static final String GET_SITE_UTILIZATION = "API to get site utilization";
			public static final String GET_LINK_UTILIZATION_FOR_TOP_LINKS = "API to get link utilization for top 5 links";
			public static final String GET_APP_UTILIZATION_BY_LINKS = "API to get app utilization by links";
			public static final String GET_APP_UTILIZATION_BY_ORAGANIZATION = "API to get Application utilization for given organization";
			public static final String GET_SITE_UTILIZATION_BY_APPS = "API to get site utilization by applications";
			public static final String GET_SDWAN_VERSA_PATH = "API to Path priotity from versa";
			public static final String GET_APP_BW_OVERALL_PERCENTAGE = "API to Application bandwidth utilization overall percentage";

		}
		
		interface PerformanceDetails {
			String PERFORMANCE_DETAILS = "Get Performance Details";
			String GET_SITE_BY_USAGE_LIVE_DATA = "Get Site By Usage Live Data Details";
		}
	}

}
