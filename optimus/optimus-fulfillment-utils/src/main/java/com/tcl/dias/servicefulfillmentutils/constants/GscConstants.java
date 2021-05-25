package com.tcl.dias.servicefulfillmentutils.constants;

public class GscConstants {
	
	public static final String KEY_GSC_FLOW_GROUP_ID = "gscFlowGroupId";
	
	public static final String KEY_GSC_FLOW_GROUP_ID_TEST_HANDOVER = "gscFlowGroupIdTestHandover";
	
	public static final String KEY_GSC_FLOW_GROUP_ID_SERVICE_ACCEPTANCE = "gscFlowGroupIdServiceAcceptance";
	
	public static final String KEY_GSC_SUPPLIER_RES_FLOW_GROUP_ID = "gscSupplierResFlowGroupId";
	
	public static final String KEY_GSC_NEW_NUMBER_FLOW_GROUP_ID = "gscNewNumberFlowGroupId";
	
	public static final String KEY_GSC_PORT_NUMBER_FLOW_GROUP_ID = "gscPortNumberFlowGroupId";
	
	public static final String KEY_GSC_FLOW_GROUP_COUNT = "gscFlowGroupCount";
    
    public static final String GET_AVAILABLE_VANITY_NUMBER_URL = "https://camttvdws001.intl.vsnl.co.in:9443/voiceinventory/accessservices/v1.0/vanityNumbers";
    //public static final String RESERVE_OR_RELEASE_VANITY_NUMBER_URL="https://camttvdws001.intl.vsnl.co.in:8443/voiceinventory/accessservices/v1.0/vanityNumbers/25736";
    //public static final String RESERVE_OR_RELEASE_VANITY_NUMBER_URL="https://camttvdws001.intl.vsnl.co.in:8443/voiceinventory/accessservices/v1.0/vanityNumbers/";
    public static final String VANITY_BASIC_AUTHORIZATION="Basic UHJvdmlzaW9uaW5nX0FQSXM6MXEydyFRQFczZTRyI0UkUg==";
    //public static final String GET_AVAILABLE_VANITY_NUMBER_URL = "https://api-uat.tatacommunications.com/VanityNumberAPI_SIP/v2.2/numbers";
    //public static final String RESERVE_OR_RELEASE_VANITY_NUMBER_URL="https://api-uat.tatacommunications.com/VanityNumberAPI_SIP/v2.2/numbers/911431";
    //public static final String VANITY_BASIC_AUTHORIZATION="Basic dGNsYXBpLTNzemhubDJsTjBnNlA5QjgxbDNMQkIzeDoxYjFlYjQ3ODQ2NDMzZGQ5NzViZjUxZTk0YzJjYTg4ZTMzMTg3M2E5";
    
    public static final String GET_SUPPLIER_URL="https://camttvdws001.intl.vsnl.co.in:9443/voiceinventory/accessservices/v1.0/suppliers";
    //public static final String GET_SUPPLIER_USERNAME="Provisioning_APIs";
    //public static final String GET_SUPPLIER_PASSWORD="1q2w!Q@W3e4r#E$R";
    
    public static final String CREATE_ORDER_IN_REPC_URL="https://camttvdws001.intl.vsnl.co.in:9443/voiceinventory/accessservices/v1.0/numbers";
    public static final String CREATE_ORDER_URL_USERNAME="Provisioning_APIs";
    public static final String CREATE_ORDER_URL_PASSWORD="1q2w!Q@W3e4r#E$R";
    public static final String GET_ROUTING_NUMBER_URL="https://camttvdws001.intl.vsnl.co.in:9443/voiceinventory/accessservices/v1.0/routingnumber";
    public static final String BASIC_AUTHORIZATION_REPC="Basic UHJvdmlzaW9uaW5nX0FQSXM6MXEydyFRQFczZTRyI0UkUg==";
    
    //public static final String CONFIG_NMBR_MAPPING_URL_SINGLE = "https://nasuat.tatavideo.net/nas/v1/service";
    public static final String CONFIG_NMBR_MAPPING_URL_BULK = "https://api-uat.tatacommunications.com:443/uat/nas/v3/service/bulk";
	public static final String CONFIG_NMBR_MAPPING_URL_BASIC_AUTH = "Basic dGNsYXBpLTZKdUJSaEYweFZQN0RYUDRlbk5mN2UyUjpmYzRkMTIxM2VjMjZiOTZmYTQ0ZDExNjc0NmE0NjU5OTBlZmI4OGMw";
	public static final String CONFIG_NMBR_MAPPING_URL_Z_AUTH = "Basic YWttYWh0bzpwYXNzd29yZA==";
	
	//Flow related constant values here
	public static final Integer API_MAX_RETRY_COUNT = 5;
	
	public static final String ORIGIN_COUNTRY_CODE = "originCountryCode";
	public static final String ORIGIN_CITY_CODE = "originCityCode";
	public static final String CUSTOMER_ORG_ID = "customerOrgId";
	public static final String SUPPLIER_ORG_ID = "supplierOrgId";
	public static final String SERVICE_TYPE = "serviceType";
	public static final String SERVICE_TYPE_REPC = "serviceTypeRepc";
	public static final String ACCESS_TYPE = "accessType";
	public static final String IS_PARTNER_ORDER = "isPartnerOrder";
	
    public static final String KEY_IS_RESERVATION_EXPIRED = "isReservationExpired";
    public static final String VALUE_RESERVATION_EXPIRED = "expired";
    public static final String VALUE_NO_RESERVATION = "expired";
    public static final String VALUE_RESERVATION_NOT_EXPIRED = "notExpired";
    
    public static final String KEY_IS_RESERVATION_PROVISION_FLOW_REQ = "isReservationProvisionFlowReq";
    public static final String KEY_IS_GET_NUMBER_FLOW_REQ = "isGetNumberFlowReq";
    public static final String KEY_IS_SUPPLIER_FLOW_REQ = "isSupplierFlowReq";
    //Flow status keys
    public static final String KEY_CHECK_RESERVED_NUMBERS_STATUS = "checkRezNoStatus";
    public static final String KEY_GET_AND_RESERVED_NUMBERS_STATUS = "getRezNoStatus";
    public static final String KEY_CREATE_ORDER_IN_REPC_STATUS = "repcOrderCreationStatus";
    public static final String KEY_UPDATE_ORDER_IN_REPC_STATUS = "repcOrderUpdateStatus";
    public static final String KEY_GET_SUPPLIERS_STATUS = "getSuppliersStatus";
    public static final String KEY_GENERATE_ROUTING_NUMBER_STATUS = "generateRoutingNoStatus";
    public static final String KEY_IS_ROUTING_NUMBER_REQ_STATUS = "isRoutingNumRequired";
    public static final String KEY_CONFIGURE_NUMBER_MAPPING_STATUS = "configureNumberStatus";
    public static final String KEY_SEND_PRI_NOTIFICATION_TO_SUPPLIER = "sendPriSupplierNotification";
    public static final String KEY_SEND_SEC_NOTIFICATION_TO_SUPPLIER = "sendSecSupplierNotification";
    
    public static final String KEY_REPC_CREATE_ORDER_TRIGGERFOR = "triggerFor";
    
    public static final String VALUE_SUCCESS = "success";
    public static final String VALUE_FAILED = "failed";
    
    public static final String ACTION = "requiredAction";
    public static final String CALL_TYPE = "callType";
    public static final String IN_MAIN_DIGITS = "inMainDigits";
    public static final String GROUPING_KEY = "groupingKey";
    
    public static final String ASSET_TOLLFREE = "Toll-Free";
    public static final String ASSET_OUTPULSE = "Outpulse";
    public static final String ASSET_ROUTING = "Routing-Number";
    
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String IS_CALLTYPE_FIXED = "isratePerMinutefixed";
    public static final String IS_CALLTYPE_MOBILE = "isratePerMinutemobile";
    public static final String IS_CALLTYPE_PAYPHONE = "isratePerMinutespecial";
	
	public static final String KEY_CASE_INST_ID = "caseInstId";
	public static final String KEY_PLAN_ITEM_INST_ID = "planItemInstId";
	
	public static final String PLACE_ORDER_RESPONSE = "placeOrderRes";
	public static final String TO_ADDR = "toAddr";
	public static final String FROM_ADDR = "fromAddr";	
	public static final String CC_ADDR = "ccAddr";
	
	public static final String KEY_FLOW_BYPASSREPCORDERCREATION = "bypassRepcOrderCreation";
	public static final String KEY_FLOW_UPDATEREPCREQUIRED = "updateRepcRequired";
	public static final String KEY_FLOW_BYPASS_INVENTORY_PROCUREMENT = "byPassInventoryProcurement";
	
	public static final String KEY_ASSET_ATTRIBUTE_STATUS = "status";
	
	public static final String PROCURE_PROV_VAS_NUMBERS = "ProcureAndProvNumbers";
	public static final String PROCURE_DID_NUMBERS = "ProcureDidNumbers";
	public static final String PROVISION_DID_NEW_NUMBERS = "ProvisionNewDidNumbers";
	public static final String PROVISION_DID_PORT_NUMBERS = "ProvisionPortDidNumbers";
	public static final String NEW_SIP_CREATION = "NewSipCreation";
	public static final String PROCURE_UIFN_NUMBERS = "ProcureUIFNNumbers";
	public static final String SERVICE_ACCEPTENCE = "ServiceAcceptence";
	public static final String DELIVER_NUMBERS = "DeliverNumbers";
	
	public static final String UIFN = "UIFN";
	public static final String ITFS = "ITFS";
	public static final String LNS = "LNS";
	public static final String ACDTFS  = "ACDTFS ";
	public static final String ACANS  = "ACANS "; 
	public static final String SIP  = "SIP";
	
	
	public static final String PUBLIC_IP = "Public IP";
	public static final String CONTAINS_DID_SERVICE = "containsDidService";
	public static final String CONTAINS_VAS_SERVICE = "containsVasService";
	
	public static final String INCLUDE_SHAREDIN_CIRCUIT_GRPS = "includeSharedInCircuitGroups";

	public static final String Y = "Y";
	public static final String DOMESTIC_VOICE = "Domestic Voice";
	public static final String DID_SERV_ABBR = "GSCDDID";
	public static final String QUANTITY_OF_NUMBERS="qty";
	public static final String PORTING_QTY="portingQty";
	public static final String KEY_GET_DID_SUPPLIER_STATUS = "getDidSuppStatus";
	public static final String KEY_GET_SITES_STATUS = "getSitesStatus";
	
	public static final String PRODUCT_SIP = "SIP";
	
	public static final String DID_PORT_CHANGE = "DID-PORT-CHANGE";
	
	public static final String DID_NEW_ORDER = "DID-NEW-ORDER";
	
	public static final String KEY_GSC_FLOW_GROUP_ID_PORT = "gscFlowGroupIdPort";
	
	public static final String DID_NEW_NUMBERS_SERVICE_ACCEPTANCE = "DidNewNumServiceAcceptance";
	public static final String DID_PORT_NUMBER_SERVICE_ACCEPTANCE = "DidPortingNumServiceAcceptance";
	
	
	public static final String NEXT_GSC_FLOW_GROUP_ID = "nextgscFlowGroupId";
	public static final String GLOBAL_OUTBOUND = "Global Outbound";

	public static final String MPLS = "MPLS";
	
	public static final String IS_SUPPLIER_INDIA_LE = "isSupplierIndiaLE";
	public static final String SUPPLIER_NOTICE_ADDRESS = "Supplier_Notice_Address";
	

	public static final String NEW_NUM_SUPPLIER_FLOW_REQUIRED = "isNewNumSupplierFlowReq";
	public static final String PORT_NUM_SUPPLIER_FLOW_REQUIRED = "isPortNumSupplierFlowReq";
	
	public static final String PSTN = "PSTN";
	
}
