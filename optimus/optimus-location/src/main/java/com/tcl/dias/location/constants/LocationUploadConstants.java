package com.tcl.dias.location.constants;


/**
 * location upload Constants class to maintain the constants related to excel location uploads
 * 
 * @author NAVEEN GUNASEKARAN
 *
 */
public class LocationUploadConstants {
	private LocationUploadConstants() {
	}

	public static final String CITY = "city";
	public static final String LOCALITY = "locality";
	public static final String COLUMN = "Column ";
	public static final String LOCALITY_CITY = "Locality & City";
	public static final String INVALID_INPUT = "Invalid Input";
	public static final String INVALID_PINCODE_COUNTRY = "Invalid Country/Pincode";
	public static final String COLUMN_PINCODE_COUNTRY = "Column Pincode/Country";
	public static final String ROW ="Row ";
	public static final String LAT ="lat";
	public static final String LNG ="lng";
	public static final String COMMA = ",";
	public static final String LONG_NAME = "long_name";
	public static final String ADMIN_AREA_LEVEL_1 = "administrative_area_level_1";
	public static final String ADMIN_AREA_LEVEL_2 = "administrative_area_level_2";
	public static final String POSTAL_TOWN = "postal_town";
	public static final String ZERO_RESULTS = "ZERO_RESULTS";
	public static final String STATUS = "status";
	public static final String TYPES = "types";
	public static final String LOCATION = "location";
	public static final String ADDRESS_COMPONENTS = "address_components";
	public static final String FORMATTED_ADDRESS = "formatted_address";
	public static final String GEOMETRY = "geometry";
	public static final String RESULTS = "results";
	public static final String HTTP_METHOD_GET = "GET";
	public static final String ACCEPT = "Accept";
	public static final String ADDRESS = "&address";
	public static final String SPACE = " ";
	public static final String SENSOR_TRUE = "&sensor=true";
	public static final String GOOGLE_API_KEY = "?key=";
	public static final String SITE_LOCATIONS = "Site Locations";
	public static final String BYON_UPLOAD="Byon Upload";
	public static final String HEADER_SERIAL_NO = "SR#";
	public static final String HEADER_COUNTRY = "Country";
	public static final String HEADER_STATE = "State";
	public static final String HEADER_CITY = "City";
	public static final String HEADER_PINCODE ="Pin/Zip Code";
	public static final String HEADER_LOCALITY ="Locality";
	public static final String HEADER_ADDRESS = "Address";
	public static final String HEADER_INTERNETQUALITY = "Internet Quality";
	public static final String HEADER_SITETYPE="Site Type";
	public static final String HEADER_PRIMARY ="Primary";
	public static final String HEADER_SECONDARY ="Secondary";
	public static final String HEADER_PORTBANDWIDTH="Port Bandwidth";
	public static final String HEADER_LOCALLOOPBANDWDTH="Local Loop Bandwidth";
	public static final String HEADER_PORTMODE="Port Mode";
	public static final String HEADER_PORTMODEACTIVE="Active";
	public static final String HEADER_PORTMODEPASSIVE ="Passive";
	public static final String HEADER_ACESSTYPEWIRELINE = "Wireline";
	public static final String HEADER_ACCESSTYPEWIRELESS ="Wireless";
	public static final String HEADER_ACCESSTYPE="Access Type";
	public static final String HEADER_INTERFACETYPE="Interface Type";
	public static final String HEADER_PROFILES = "Profiles";
	public static final String LOCATION_TEMPLATE_XLSX ="location_template.xlsx";
	public static final String PATTERN ="yyyyMMddHHmmss";
	public static final String ERROR_MSG ="error_message";
	public static final String UTF8 ="UTF-8";
	public static final String INVALID_ADDRESS ="Address is not identifiable through system, Please capture detailed address in excel template";
	public static final String INVALID_POSTAL_CODE ="Postal code is not identifiable through system, Please capture detailed address in excel template";
	public static final String INVALID_LOCALITY ="Locality is not identifiable through system, Please capture detailed address in excel template";
	public static final String STATE_CANT_BE_EMPTY ="State Can't be Empty";
	public static final String COUNTRY_CANT_BE_EMPTY ="Country Can't be Empty";

	public static final String CITY_CANT_BE_EMPTY ="City Can't be Empty";
	public static final String PINCODE_CANT_BE_EMPTY ="Pincode Can't be Empty";
	public static final String ADDRESS_CANT_BE_EMPTY ="Address Can't be Empty";
	public static final String LOCALITY_CANT_BE_EMPTY ="Locality Can't be Empty";
	public static final String INTERNETQUALITY_CANT_BE_EMPTY="Internet Quality Can't be Empty";
	public static final String SITETYPE__CANT_BE_EMPTY ="Site Type Can't be Empty";
	public static final String PROFILE_CANT_BE_EMPTY ="Profile Can't be Empty";
	public static final String SR_CANT_BE_EMPTY ="SR# Can't be Empty";
	
	public static final String TRY_AFTER_SOMETIME ="Please try after sometime";
	public static final String EQUALTO ="=";
	public static final String POSTAL_CODE = "postal_code";
	public static final String SINGAPORE ="Singapore";
	
	public static final String LAT_LONG_ERROR ="Invalid Input:Address not identified by google API";
	public static final String LAT_ERROR ="Invalid Data";
	
	
	// Npl Specific
	public static final String COUNTRY_A_CANT_BE_EMPTY ="Country A Can't be Empty";
	public static final String COUNTRY_B_CANT_BE_EMPTY ="Country B Can't be Empty";
	public static final String STATE_A_CANT_BE_EMPTY ="State A Can't be Empty";
	public static final String STATE_B_CANT_BE_EMPTY ="State B Can't be Empty";
	public static final String CITY_A_CANT_BE_EMPTY ="City A Can't be Empty";
	public static final String CITY_B_CANT_BE_EMPTY ="City B Can't be Empty";
	public static final String PINCODE_A_CANT_BE_EMPTY ="Pincode A Can't be Empty";
	public static final String PINCODE_B_CANT_BE_EMPTY ="Pincode B Can't be Empty";
	public static final String ADDRESS_A_CANT_BE_EMPTY ="Address A Can't be Empty";
	public static final String ADDRESS_B_CANT_BE_EMPTY ="Address B Can't be Empty";
	public static final String LOCALITY_A_CANT_BE_EMPTY ="Locality A Can't be Empty";
	public static final String LOCALITY_B_CANT_BE_EMPTY ="Locality B Can't be Empty";
	public static final String HEADER_COUNTRY_A = "Country A";
	public static final String HEADER_STATE_A = "State A";
	public static final String HEADER_CITY_A = "City A";
	public static final String HEADER_PINCODE_A ="Pin/Zip Code A";
	public static final String HEADER_LOCALITY_A ="Locality A";
	public static final String HEADER_ADDRESS_A = "Address A";
	public static final String HEADER_COUNTRY_B = "Country B";
	public static final String TYPE_A = "Type A";
	public static final String TYPE_B = "Type B";
	public static final String PINCODE_A ="Pin/Zip Code A";
	public static final String LOCALITY_A ="Locality A";
	public static final String LOCALITY_B ="Locality B";
	public static final String HEADER_STATE_B = "State B";
	public static final String HEADER_CITY_B = "City B";
	public static final String HEADER_PINCODE_B ="Pin/Zip Code B";
	public static final String HEADER_LOCALITY_B ="Locality B";
	public static final String HEADER_ADDRESS_B = "Address B";
	public static final String HEADER_TYPE_A = "Type A";
	public static final String HEADER_TYPE_B = "Type B";
	public static final String TYPE_A_CANT_BE_EMPTY ="Type A Can't be Empty";
	public static final String TYPE_B_CANT_BE_EMPTY ="Type B Can't be Empty";
	public static final String SITE = "SITE";
	public static final String DC = "DC";
	public static final String POP = "POP";
	
	
}
