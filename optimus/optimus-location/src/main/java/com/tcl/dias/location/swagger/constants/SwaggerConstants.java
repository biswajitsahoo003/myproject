package com.tcl.dias.location.swagger.constants;

/**
 * This file contains the SwaggerConstants.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SwaggerConstants {
	private SwaggerConstants() {
	}

	public static class ApiOperations {
		private ApiOperations() {
		}

		public static class Location {
			private Location() {
			}

			public static final String GET_BY_PINCODE = "Get the Details by the given pincode";
			public static final String ADD_LOCATION = "Add location details";
			public static final String ADD_LOCATION_WITH_CUSTOMER_LE = "Add location details with customer le mapping";
			public static final String UPDATE_CUSTOMER_AND_LE_LOCATION_FOR_CGW = "Update customer and customer le mapping for CGW locations";
			public static final String UPDATE_LOCATION = "Update location details by id";
			public static final String GET_LOCATION = "Get location details by given id";
			public static final String GET_LOCATIONS = "Get location details by given list of id's";
			public static final String GET_DC_LOCATION_ID = "Get data centre's location id using data centre code";
			public static final String GET_DC_CODE = "Get data centre's code for location id";
			public static final String POST_OR_UPDATE_IT_CONTACT = "Post or Update customer site It location contact details";
			public static final String GET_IT_CONTACT = "Get customer site It location contact details";
			public static final String DELETE_IT_CONTACT = "Delete customer site It location contact details";
			public static final String UPDATE_DEMARCATION_AND_LOCAL_IT_CONTACT = "Update Demarcation details and local it contact";
			public static final String UPLOAD_LOCATION = "Update location details from XLS";
			public static final String LOAD_LOCATION_CUSTOMER = "This API returns the list of legal entities and it's location address details with customer Id.";
			public static final String LOAD_LOCATION_LEGAL = "This API returns the list of legal entities and it's location address details with legal entity ids";
			public static final String LAT_LONG_UPDATE = "This API updates the lat long details of the given location id";
			public static final String GET_ADDRESS_BASED_ON_CUSTOMER = "Get Address details based on customer details";
			public static final String GET_LOCAL_IT_CONTACT = "Get Local It contact details based on the local it contact input";
			public static final String UPLOAD_LOCATION_EXCEL = "Location excel upload for more than one sites";
			public static final String DOWNLOAD_LOCATION_TEMPLATE = "Download location template, list of profiles needs to be passed";
			public static final String GET_STATE_DETAILS = "Used to get the state details based on country";
			public static final String GET_COUNTRY_DETAILS = "Used to get all distinct countries";
			public static final String GET_CITY_DETAILS = "Used to get city details based on state";
			public static final String UPLOAD_LOCATION_EXCEL_NPL = "Location excel upload for more than one sites for NPL";
			public static final String LOCATION_USING_ADDRESSID= "used to return the bean of responses using address id";
			public static final String ADD_LOCATION_FOR_NPL="Add location for NPL product";
			public static final String ADD_POP_LOCATIONS = "Add POP location details";
			public static final String GET_LAT_LONG="Get Latitude and Longitude details";
			public static final String PUNCH_LAT_LONG="Punch Latitude and Longitude details";
			public static final String ADD_CUSTOM_ADDRESS = "Add custom address into MST table";
			/** Gsc Product Constants Start */

			public static final String GET_CITIES = "Get all city for given country";
			public static final String GET_COUNTRY = "Get all countries";
			public static final String GET_STATES_CITIES_BY_COUNTRY = "Get the states and cities by given country";
			/** Gsc Product Constants End */
			public static final String GET_LOCALITY_BY_PINCODE_AND_CITY = "Get locality details by pincode city and country";

		}
	}

}
