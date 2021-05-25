package com.tcl.dias.oms.webex.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constants related to UCAAS.
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexConstants {

	public static final String UCAAS_WEBEX = "UCWB";
	public static final String UCAAS_FAMILY_NAME = "UCAAS";
	public static final String WEBEX = "WEBEX";
	public static final String UCAAS = "ucaas";
	public static final String LNS = "lns";
	public static final String GOB = "gob";
	public static final String PACKAGE = "package";
	public static final String USA = "USA";
	public static final String EMEA = "EMEA";
	public static final String APJ = "APJ";

	// Audio Type Constants
	public static final String SHARED = "Shared";
	public static final String DEDICATED = "Dedicated";
	public static final String PAYPER_SEAT = "PayPer Seat";
	public static final String PAYPER_USE = "PayPer Use";
	public static final String TOLL_DIAL_IN = "Toll Dial-In";
	public static final String TOLL_DIAL_IN_DIAL_OUT = "Toll Dial-In + Toll Dial-Out";
	public static final String TOLL_DIAL_IN_BRIDGE_DIAL_OUT = "Toll Dial-In + Bridge Country Dial-Out";

	// Configuration Constants
	public static final String CONFIGURATION = "Configuration";
	public static final String CONFIGURATION_DETAILS = "Configuration Details";

	// License Constants
	public static final String DEAL_ID = "DealId";
	public static final Integer MAX_ITEMS = 1;
	public static final String CLIENT_SECRET = "client_secret";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_CREDENTIALS = "client_credentials";
	public static final String GRANT_TYPE = "grant_type";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String PARTNUMBER = "PartNumber";
	public static final String UNITNETPRICE = "UnitNetPrice";
	public static final String CISCO_DISCOUNT_TYPE = "CiscoDiscountType";
	public static final String TOTAL_DISCOUNT = "TotalDiscount";
	public static final String CISCO_SUCCESS_CODE = "LSTQCT00";
	public static final String CISCO_EXPIRED_QUOTE_ERROR_CODE = "DAQS015";
	public static final String CISCO_UNAVAILABLE_QUOTE_ERROR_CODE = "LSTQCT01";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	public static final String HSN_CODE = "998315";
	public static final String LICENSE = "License";
	public static final String ENDPOINT = "Endpoint";
	public static final String A_FLEX_CCA_SP_USER = "A-FLEX-CCA-SP-USER";
	public static final String A_FLEX_EACM = "A-FLEX-EACM";
	public static final String A_FLEX_AUCM = "A-FLEX-AUCM";
	public static final String A_FLEX_NUCM = "A-FLEX-NUCM";
	
	public static final String INSTALL_ADDRESS = "InstallAddress";
	public static final String INDIA_COUNTRY_CODE = "IN";
	public static final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";
	public static final String QUOTEOWNER = "QuoteOwner";
	public static final String END_CUSTOMER = "End Customer";

	// GSC Solution Constants
	public static final String LNS_ON_MPLS = "LNS on MPLS";
	public static final String GLOBAL_OUTBOUND_ON_MPLS = "Global Outbound on MPLS";
	public static final String ITFS_ON_MPLS = "ITFS on MPLS";
	public static final String LNS_ON_PSTN = "LNS on PSTN";
	public static final String ITFS_ON_PSTN = "ITFS on PSTN";
	public static final String GLOBAL_OUTBOUND_ON_PSTN = "Global Outbound on PSTN";
	public static final String LNS_COUNTRIES = "LNS Countries";
	public static final String GLOBAL_OUTBOUND_COUNTRIES = "GLOBAL OUTBOUND Countries";
	public static final String ITFS_COUNTRIES = "ITFS Countries";
	public static final String GSC="GSC";
	public static final String GSIP="GSIP";
	public static final String GSIP_MACD="GSIP_MACD";

	// Product Catalog Constants
	public static final String LNS_PACKAGED_CODE = "UCAAS_LNS_PKG_CD_A_0001";
	public static final String OUTBOUND_PACKAGED_CODE = "UCAAS_OB_PKG_CD_A_0001";
	public static final String OUTBOUND_BRIDGE_PACKAGED_CODE = "UCAAS_bridge_PKG_CD_A_0001";

	// GVPN Constants
	public static final String NEW = "New";
	public static final String EXISTING = "Existing";

	// Generic constants
	public static final String MONTHS = "months";
	public static final String SPACE = " ";
	public static final String DDMMYYYY = "dd-MMM-yyyy";
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";

	// SkuID Constants
	public static final String TDI = "TDI";
	public static final String TDI_PLUS_BC = "TDIPlusBC";
	public static final String TDI_PLUS_INT = "TDIPlusInt'l";
	public static final String USA_BRIDGE = "USA Bridge";
	public static final String APJ_BRIDGE = "APJ Bridge";
	public static final String EMEA_BRIDGE = "EMEAR Bridge";
	public static final String AU = "AU";
	public static final String EA = "EA";
	public static final String NU = "NU";
	public static final String TAAP = "TAAP";
	public static final String SUBSCRIPTION = "Subscription";
	public static Map<String, String> audioTypes = new HashMap<String, String>() {
		{
			put(TOLL_DIAL_IN, TDI);
			put(TOLL_DIAL_IN_DIAL_OUT, TDI_PLUS_INT);
			put(TOLL_DIAL_IN_BRIDGE_DIAL_OUT, TDI_PLUS_BC);
		}
	};
	public static Map<String, String> bridgeRegions = new HashMap<String, String>() {
		{
			put(USA, USA_BRIDGE);
			put(EMEA, EMEA_BRIDGE);
			put(APJ, APJ_BRIDGE);
		}
	};

	public static Map<String, String> skuUserTypes = new HashMap<String, String>() {
		{
			put(A_FLEX_AUCM, AU);
			put(A_FLEX_EACM, EA);
			put(A_FLEX_NUCM, NU);
		}
	};

	public static final String ACTIVE_USER = "Active User";
	public static final String ENTERPRISE_AGREEMENT = "Enterprise Agreement";
	public static final String NAMED_USER = "Named User";
	public static Map<String, String> licenseTypes = new HashMap<String, String>() {
		{
			put(A_FLEX_AUCM, ACTIVE_USER);
			put(A_FLEX_EACM, ENTERPRISE_AGREEMENT);
			put(A_FLEX_NUCM, NAMED_USER);
		}
	};

	// Pricing Constants
	public static final String QUOTE_UCAAS = "ucaas.quotes";

	// Service Inventory Exception messages
	public static final String CUSTOMER_ID_NULL_MESSAGE = "Customer ID cannot be NULL";
	public static final String INVENTORY_RECEIVED_MESSAGE = "Inventory receieved";
	public static final String INVENTORY_NOT_AVAILABLE_MESSAGE = "No Inventory available";

	// CCW Exception messages
	public static final String QUOTE_EXPIRED_MESSAGE = "Quote expired for the Deal ID %s";
	public static final String QUOTE_UNAVAILABLE_MESSAGE = "Quote unavailable for the Deal ID %s";
	public static final String CONTRACT_PERIOD_MISMATCH_MESSAGE = "The requested contract period is %s months. Please reach out to support team";
	public static final String CONTRACT_PERIOD_UNSUPPORTED_MESSAGE = "BOM contains unsupported contract period. Please reach out to respective support team";
	public static final String CONTRACT_PERIOD_ABSENT_MESSAGE = "BOM does not contain contract period. Please reach out to respective support team";
	public static final String CLOUD_MEETING_LICENSE_ABSENT_MESSAGE = "The respective deal ID %s does not contain cloud meeting license";
	public static final String TATA_VOICE_VALIDATION_ERROR_MESSAGE = "The following Deal ID %s BOM does not contain Tata voice as service, which is not supported by Optimus. Please reach respective support team";
	public static final String CONTRACT_PERIOD_CISCO_VOICE_ERROR_MESSAGE = "BOM contains Cisco voice and unsupported contract period %s months. Please reach respective support team";
	public static final String BOM_DETAILS_RECIEVED_SUCCESS_MESSAGE = "BOM Details for DealID: %s has been received successfully";
	public static final String TRY_AGAIN_AFTER_SOMETIME_MESSAGE = "Please try again after sometime";
	// SFDC
	public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static final String OPTIMUS_OPPORTUNITY="Optimus Opportunity";
	public static final String WebEx = "WebEx";
	public static final String OPPPORTUNITY_SPECIFICATION = "Webex Calling by Tata Communications";

	// Status codes
	public static final String ERROR_CODE_404 = "404";
	public static final String ERROR_CODE_401 = "401";
	public static final String ERROR_CODE_403 = "403";
	public static final String ERROR_CODE_408 = "408";
	public static final String ERROR_CODE_503 = "503";
	public static final String ERROR_CODE_504 = "504";

	// LR Constants
	public static final String INTL = "INTL";
	public static final String ZERO = "0";

	public static final String SITE_PROPERTIES = "SITE_PROPERTIES";
	public static final String ORDER_UCAAS_SITE_ID = "ORDER_UCAAS_SITE_ID";
	public static final String WEBEX_SITE = "WEBEX_SITE";
	public static final String INDIA = "India";

	public static final String UNIT_PRICE = "unitPrice";

}
