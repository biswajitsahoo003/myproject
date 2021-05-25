package com.tcl.dias.products.swagger.constants;

/**
 * constants related to Swagger documentation
 * 
 * @author Manojkumar R
 *
 */
public class SwaggerConstants {
	private SwaggerConstants() {
	}

	public static class ApiOperations {
		private ApiOperations() {
		}

		public static class Products {
			private Products() {
			}

			public static final String GET_ALL_CPE_VALUES_IAS = "Get a list of all the CPE Values present in CpeBomView";
			public static final String GET_ALL_CPE_VALUES_GVPN = "Get a list of all the CPE Values present in CpeBomGvpnView";
			public static final String GET_BY_PRODUCT_ID = "Get the products by the given product family id";
			public static final String GET_ALL_PRODUCT = "Get all product families";
			public static final String GET_PRICING_DETAILS = "Get Pricing details";
			public static final String GET_SLA_VALUE = "Get SLA values by the given product offering id and service varient id";
			public static final String GET_PRODUCT_LOCATIONS = "Get Product Locations API";
			public static final String GET_BY_PRODUCT_OFFERING_ID = "Get product offering details based on product offering id";
			public static final String GET_SERVICE_DETAILS_BY_PRODUCT_ID = "Get Service Details for given product family id";
			public static final String GET_CPE_BOM_DETAILS = "Get CPE BOM details for the profile";
			public static final String GET_BOM_RESOURCE_DETAILS = "Get Resource details for the provided list of CPE-BOM";
			public static final String GET_SLA_VALUE_NPL = "Get SLA values by the given service variant and access topology";

			// NPL related entries - start
			public static final String GET_NPL_ASSURED_UPTIME = "Get minimum network uptime value for NPL";
			public static final String GET_ATTRIBUTE_VALUES = "Get attribute values by the given attribute type";
			public static final String GET_ALL_ATTRIBUTES = "Get the master set of attributes for a product profile";
			public static final String GET_PROFILE_LIST = "Get profile list for product";
			public static final String GET_COMPONENT_BY_PRODUCT_OFFERING_ID = "Get component details based on product offering id";
			public static final String GET_NPL_POP_DETAILS = "Get NPL pop location details";
			public static final String GET_NPL_DATA_CENTER_DETAILS = "Get NPL Data Center details";
			public static final String GET_CITY_LIST_DATA_CENTER_DETAILS = "Get city list for Data Center";
			// NPL related entries - end

			// GSC related entries - start
			public static final String GET_GSC_COUNTRY_DETAILS = "used to get gsc specific country details";
			public static final String GET_GSC_CITY_DETAILS = "used to get gsc specific city details";
			public static final String GET_GSC_COUNTRY_SERIVE_DETAILS = "used to get gsc specific country service details";
			public static final String GET_GSC_COUNTRY_SERVICE_ALL_DETAILS = "used to get gsc specific country service all details";
			// GSC related entries - end

			// Webex related entries - start
			public static final String GET_WEBEX_COUNTRY_DETAILS = "used to get gsc specific country details";
			public static final String DOWNLOAD_WEBEX_PRICING = "download price list for webex products";
			// Webex related entries - end

			public static final String DOWNLOAD_OUTBOUND_PRICING = "download outbound pricing for gsip";

			//Teamsdr Related entries
			public static final String GET_TEAMSDR_COUNTRIES = "Getting all the teamsdr countries";
			public static final String GET_TEAMSDR_CITIES = "Getting all the teamsdr cites based on the country";
			public static final String GET_TEAMSDR_MEDIAGATEWAYS = "Getting all the teamsdr mediagateways based on the type";

		}

		public static class CloudProvider {
			private CloudProvider() {
			}

			public static final String GET_CLOUD_PROVIDER_DETAILS = "get all the cloud provider details";
			public static final String GET_DATA_CENTER_DETAILS = "get all the data center details";
			public static final String GET_CLOUD_PROVIDER_ATTRIBUTES = "get the required attributes for the cloud provider";
			public static final String GET_DATA_CENTER_DETAIL_FOR_DCCODE = "Retrieves data center detail for the given data center code";

		}
		
		public static class Izosdwan{
			private Izosdwan() {
				
			}
			
			public static final String GET_ALL_PROFILES = "Get all profiles for SDWAN";
			public static final String GET_ALL_VPROXY_PROFILES="Get all Vproxy profiles for sdwan";
			public static final String GET_ALL_VUTM_PROFILES="Get all vUTM profiles for sdwan";
			public static final String GET_ALL_BW_INTERFACE_MAPPING_BY_VENDOR="Get all BW and Interface type mapping by vendor";
		}
	}

}
