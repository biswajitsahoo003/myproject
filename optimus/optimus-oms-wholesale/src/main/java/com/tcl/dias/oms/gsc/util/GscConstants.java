package com.tcl.dias.oms.gsc.util;

/**
 * Constants related to GSC product
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscConstants {

    public static final byte STATUS_ACTIVE = (byte) 1;
    public static final byte STATUS_INACTIVE = (byte) 0;
    public static final Byte STATUS_ACTIVE_BYTE = Byte.valueOf(STATUS_ACTIVE);
    public static final Byte STATUS_INACTIVE_BYTE = Byte.valueOf(STATUS_INACTIVE);
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_UPDATE = "UPDATE";
    public static final String ACTION_DELETE = "DELETE";
    public static final String ACTION_APPROVE = "APPROVE";
    public static final String GSC_PRODUCT_NAME = "gsc";
    public static final String GSIP_PRODUCT_NAME = "GSIP";
    public static final String TIGER_OPTIMUS_PRODUCTIDENTIFIER = "OPTIMUS";
    public static final String VAS_OPTIMUS_PRODUCT_IDENTIFIER = "VAS";
    public static final String GSC_ORDER_PRODUCT_COMPONENT_TYPE = "gsip";
    public static final String GSC_COMMON_PRODUCT_COMPONENT_TYPE = "gsip.common";
    public static final String GSC_CONFIG_PRODUCT_COMPONENT_TYPE = "gsip.config";
    public static final String PSTN = "PSTN";
    public static final String MPLS = "MPLS";
    public static final String NNI = "NNI";
    public static final String PUBLIC_IP = "Public IP";
    public static final String DEDICATED = "Dedicated";
    public static final String ITFS = "ITFS";
    public static final String LNS = "LNS";
    public static final String UIFN = "UIFN";
    public static final String ACANS = "ACANS";
    public static final String ACLNS = "ACLNS";
    public static final String ACDTFS = "ACDTFS";
    public static final String ACSNS = "ACSNS";
    public static final String DOMESTIC_VOICE = "Domestic Voice";
    public static final String GLOBAL_OUTBOUND = "Global Outbound";
    public static final String RATE_PER_MINUTE_FIXED = "Rate per Minute(fixed)";
    public static final String RATE_PER_MINUTE_SPECIAL = "Rate per Minute(special)";
    public static final String RATE_PER_MINUTE_MOBILE = "Rate per Minute(mobile)";
    public static final String RATE_PER_MINUTE = "Rate per Minute";
    public static final String TERM_NAME = "Termination Name";
    public static final String TERM_RATE = "Termination Rate";
    public static final String PHONE_TYPE = "Phone Type";
    public static final String SURCHARGE_RATE = "Surcharge Rate";

    public static final String ORDER_ID_NULL_MESSAGE = "Order ID cannot be null";
    public static final String ORDER_NULL_MESSAGE = "Order cannot be null";
    public static final String ORDER_GSC_ID_NULL_MESSAGE = "Order GSC ID cannot be null";
    public static final String ORDER_PRODUCT_COMPONENT_NUll_MESSAGE = "OrderProductComponentBean cannot be null";

    public static final String QUOTE_NULL_MESSAGE = "Quote cannot be null";
    public static final String QUOTE_ID_NULL_MESSAGE = "Quote ID cannot be null";
    public static final String QUOTE_LE_ID_NULL_MESSAGE = "Quote Le ID cannot be null";
    public static final String GSC_CONFIGURATION_DATA_NULL_MESSAGE = "GscConfiguration Data cannot be null";
    public static final String QUOTE_GSC_NULL_MESSAGE = "Quote GSC cannot be null";
    public static final String QUOTE_GSC_ID_NULL_MESSAGE = "Quote GSC ID cannot be null";
    public static final String QUOTE_GSC_DETAIL_ID_NULL_MESSAGE = "Quote GSC Detail ID cannot be null";
    public static final String QUOTE_PRODUCT_COMPONENT_NUll_MESSAGE = "QuoteProductComponentBean cannot be null";

    public static final String CONFIGURATION_ID_NULL_MESSAGE = "Configuration ID cannot be null";
    public static final String CONFIGURATIONS_NULL_MESSAGE = "Configuration list cannot be null";
    public static final String SOLUTION_ID_NULL_MESSAGE = "Solution ID cannot be null";
    public static final String ATTRIBUTES_NULL_MESSAGE = "Attributes cannot be null";
    public static final String ATTRIBUTE_VALUE_NULL_MESSAGE = "Attribute values cannot be null";
    public static final String PRODUCT_COMPONENT_NUll_MESSAGE = "Product Component Bean cannot be null";
    public static final String PRODUCT_NAME_NULL_MESSAGE = "Product name cannot be null";
    public static final String FILES_NOT_NULL_MESSAGE = "files cannot be empty";
    public static final String CONTRACT_TERM_NULL_MESSAGE = "Contract terms cannot be null";

    public static final String INPUT_CURRENCY_VALUE = "Input currency cannot be null";
    public static final String QUOTE_TO_LE_STAGE = "stage cannot be null";
    public static final String EMAIL_NULL_MESSAGE = "Email cannot be null";
    public static final String HTTP_SERVLET_RESPONSE_NULL_MESSAGE = "HttpServletResponse cannot be null";
    public static final String NAT_NULL_MESSAGE = "NAT cannot be null";
    public static final String ISAPPROVED_NULL_MESSAGE = "IsApproved cannot be null";
    public static final String COUNT_REQUESTED_NUMBERS = "Quantity Of Numbers";
    public static final String COUNT_PORTED_NUMBERS = "List of numbers to be ported";
    public static final String COUNT_CONCURRENT_CHANNELS = "No Of Concurrent channel";
    public static final String DIRECT = "Direct";
    public static final String CITY_WISE_QUANTITY_OF_NUMBERS = "City wise Quantity Of Numbers";
    public static final String CITY_WISE_PORTING_SERVICE_NEEDED = "City wise Porting service needed";

    public static final String GVPN_TOTAL_MRC = "GVPN MRC";
    public static final String GVPN_TOTAL_NRC = "GVPN NRC";
    public static final String GVPN_TOTAL_ARC = "GVPN ARC";
    public static final String GVPN_TOTAL_TCV = "GVPN TCV";
    public static final String SET = "SET";
    public static final String GET = "GET";

    public static final String INTIAL_ORDER_CONFIGURATION_STATUS = "Order Placed";
    public static final String INTIAL_ORDER_CONFIGURATION_STAGE = "LM Delivery Initiated";
    public static final String ACTIVE_ORDER_CONFIGURATION_STATUS = "Start of Service";
    public static final String ACTIVE_ORDER_CONFIGURATION_STAGE = "LM Implemented";
    public static final String ORDER_ENRICHMENT = "Order Enrichment";

    public static final String DID_MRC = "DID MRC";
    public static final String DID_NRC = "DID NRC";
    public static final String DID_ARC = "DID ARC";
    public static final String CHANNEL_MRC = "Channel MRC";
    public static final String CHANNEL_ARC = "Channel ARC";
    public static final String CHANNEL_NRC = "Channel NRC";
    public static final String ORDER_SETUP_MRC = "Order Setup MRC";
    public static final String ORDER_SETUP_NRC = "Order Setup NRC";
    public static final String ORDER_SETUP_ARC = "Order Setup ARC";

    public static final String OTHERS = "Others";
    public static final String ATTACHMENT_IS_NULL = "Attachment is null";
    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";

    public static final String UPLOAD = "upload";
    public static final String INBOUND_VOLUME = "Inbound Volume";
    public static final String OUTBOUND_VOLUME = "Outbound Volume";
    public static final String INBOUND_COUNTRIES = "Inbound Countries";
    public static final String OUTBOUND_COUNTRIES = "Outbound Countries";
    /* public static final Double DEFAULT_VOLUME=36.0*1000000; */
    public static final Double DEFAULT_VOLUME = 0.4 * 1000000;

    public static final String REF_TYPE_ORDER_GSC_DETAIL = "order_gsc_details";
    public static final String REF_TYPE_ORDER = "order";
    public static final String HEADER_GSC_LOG_REFERENCE_ID = "gsc.log.reference.id";
    public static final String HEADER_GSC_LOG_REFERENCE_TYPE = "gsc.log.reference.type";

    public static final String DOCUMENT_TYPE_BOTH = "BOTH";
    public static final String DOCUMENT_TYPE_DOWNLOAD = "DOWNLOAD";
    public static final String DOCUMENT_TYPE_UPLOAD = "UPLOAD";

    public static final String DOCUMENT_STATUS_PENDING = "PENDING";
    public static final String DOCUMENT_STATUS_UPLOADED = "UPLOADED";
    public static final String DOCUMENT_STATUS_NIL = "NIL";

    public final static String BEST_EFFORT = "Best effort";
    public final static String DEFAULT_RFS_DATE = "45";

    public static final String ORDER_STATE_SUBMIT = "submit";
    public static final String DID = "DID";

    public static final String WHOLESALE_ORDER = "WHOLESALE_ORDER";
    public static final String CHANGE_OUTPULSE_ORDER = "CHANGE_OUTPULSE_ORDER";

    public static final String TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER = "INTERNATIONAL_ORDER";
    public static final String TIGER_SERVICE_TYPE_DOMESTIC_ORDER = "DOMESTIC_ORDER";
    //	public static final String TIGER_SERVICE_TYPE_INTERCONNECT_ORDER = "INTERCONNECT_ORDER";
    public static final String TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES = "INTERCONNECT_ORDER_ACCESS_SERVICES";
    public static final String TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS = "INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS";
    public static final String TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT = "INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT";
    public static final String TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND = "INTERCONNECT_ORDER_GLOBAL_OUTBOUND";
    public static final String ACCESS_TYPE = "Access type";
    public static final String UIFN_REGISTRATION_CHARGE = "UIFN Registration Charge";
    public static final String ACTION_NULL_MESSAGE = "Action value is required. It cannot be null";
    public static final String GLOBAL_OUTBOUND_DYNAMIC_COLUMN = "Global Outbound Rate Column";

    public static final String PRODUCT_COMPONENT_TYPE_ORDER_GSC = "ORDER_GSC";

    public static final String DOWNSTREAM_REQUEST_STATUS_PENDING = "PENDING";
    public static final String DOWNSTREAM_REQUEST_STATUS_SUBMITTED = "SUBMITTED";
    public static final String DOWNSTREAM_REQUEST_STATUS_ERROR = "ERROR";
    public static final String DOWNSTREAM_REQUEST_STATUS_IN_PROGRESS = "INPROGRESS";
    public static final String GVPN_CURRENCY = "USD";

    public static final String SOURCE_SYSTEM_LEGACY = "LEGACY";

    public static final String ORDER_TYPE_MACD = "MACD";
    public static final String TFN_ACTION_CANCEL = "CANCEL";

    public static final String REQUIRED_ON_A_AND_B_NUMBER = "+ required on A & B number (E.164)";
    public static final String DTMF_RELAY_SUPPORT = "DTMF Relay support";
    public static final String SUPPORTED_SIP_PRIVACY_HEADERS = "Supported SIP Privacy headers";
    public static final String SESSION_KEEP_ALIVE_TIMER = "Session Keep Alive Timer";
    public static final String PREFIX_ADDITION = "Prefix addition";
    public static final String CUSTOMER_PUBLIC_IP = "Customer Public IP";
    public static final String TRANSPORT_PROTOCOL = "Transport Protocol";
    public static final String CODEC = "Codec";
    public static final String NO_OF_CONCURRENT_CHANNEL = "No Of Concurrent channel";
    public static final String EQUIPMENT_ADDRESS = "Equipment address";
    public static final String ROUTING_TOPOLOGY = "Routing Topology";
    public static final String DIAL_PLAN_LOGIC = "Dial plan logic (Prefix or CLI)";
    public static final String CALLS_PER_SECOND = "Calls Per Second (CPS)";
    public static final String CERTIFICATE_AUTHORITY_SUPPORT = "Certificate Authority Support";
    public static final String FQDN = "FQDN";
    public static final String IP_ADDRESS_SPACE = "IP Address Space";
    public static final String CUSTOMER_DEVICE_IP = "Customer Device IP";
    public static final String ADDITIONAL_INFORMATION = "Additional Information";

    public static final String CALLING_SERVICE_TYPE = "Calling Service Type";
    public static final String TERMINATION_NUMBER_WORKING_OUTPULSE = "Termination Number (Working Outpulse)";
    public static final String OLD_TERMINATION_NUMBER_WORKING_OUTPULSE = "Old Termination Number (Working Outpulse)";
    public static final String TERMINATION_NUMBER_ISD_CODE = "Termination Number ISD Code";
    public static final String TOLL_FREE_NUMBERS = "Toll Free Numbers(TFN)";
    public static final String CITY_SELECTION = "City Selection";
    public static final String LIST_OF_NUMBERS_TO_BE_PORTED = "List of numbers to be ported";
    public static final String QUANTITY_OF_NUMBERS = "Quantity Of Numbers";
    public static final String EMERGENCY_ADDRESS = "Emergency Address";
    public static final String QUOTE_TYPE_NEW_ORDER = "NEW ORDER";
    public static final String TFN_Reservation_ID = "TFN_Reservation_ID";
    public static final String GSC_CFG_TYPE_REFERENCE = "CHANGE";

    public static final String INDIA = "INDIA";

    public static final String NEW = "NEW";
    public static final String ADD_COUNTRY = "ADD_COUNTRY";
    public static final String DELETE_NUMBER = "REMOVE_NUMBER";
    public static final String CHANGING_OUTPULSE = "CHANGE_OUTPULSE";
    public static final String CUSTOMER_LE_ORG_ID = "ORG_NO";

    public static final String ATTRIBUTE_VALUE_INBOUND_VOLUME = "Inbound Volume";
    public static final String ATTRIBUTE_VALUE_OUTBOUND_VOLUME = "Outbound Volume";
    public static final String UNIT_CONSTANT_VOLUME_COMMITMENT = " Million Minutes/Month traffic";
    public static final String NOT_APPLICABLE = "NA";
    public static final String STRING_ZERO = "0";
    public static final String HIGH_RATE = "high_rate";
    public static final String REQUIRED_PORTING_NUMBERS = "Required porting numbers";
    public static final String GSIP_OUTBOUND_PDF = "GSIP_Outbound_PDF";
    public static final String GSIP_OUTBOUND_EXCEL = "GSIP_Outbound_Excel";
    public static final String COUNTRY = "Country";
    public static final String DEST_ID = "DestID";
    public static final String DESTINATION_NAME = "Destination Name";
    public static final String RATE_MIN = "Rate/Min";
    public static final String COMMENTS = "Comments";
    public static final String GSIP_SURCHARGE_PDF = "GSIP_Surcharge_PDF";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String ACCESS_SERVICES = "Access Services";
    public static final String TRUE_FLAG = "true";
    public static final String SWTCH_UNIT_CD_RERT = "SWTCH_UNIT_CD_RERT";
    public static final String CIRCT_GR_CD_RERT = "CIRCT_GR_CD_RERT";
    public static final String CIRCUIT_ID = "Circuit_ID";
    public static final String NNI_ID = "NNI ID";

    public static final String PAYMENT_CURRENCY = "Payment Currency";
    public static final String BILLING_CURRENCY = "Billing Currency";

    public static final String PRODUCT_NAME = "productName";

    public static final String ADMIN_CHANGED_PRICE = "Admin Changed Price";

    public static final String SECS_CODE = "SECS Code";

    public static final String COMPLETED = "Completed";

    public static final String OPT_WHOLESALE_NGP_CUSTOMER_PORTAL = "OPT_WHOLESALE_NGP_CUSTOMER_PORTAL";

    private GscConstants() {
        /* static usage */
    }

}
