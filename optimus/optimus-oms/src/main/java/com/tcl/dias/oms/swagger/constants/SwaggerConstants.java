package com.tcl.dias.oms.swagger.constants;

/**
 * 
 * This file contains the SwaggerConstants.java class. This class conatins
 * swagger constants
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

		public static class Quotes {
			private Quotes() {
				// DO NOTHING
			}

			public static final String GET_EXCEL_TEMPLATE = "download the template for macd ill";
			public static final String GET_BY_PINCODE = "Get the Details by the given pincode";
			public static final String ADD_LOCATION = "Add location details";
			public static final String UPDATE_LOCATION = "Update location details by id";
			public static final String GET_LOCATION = "Get location details by given id";
			public static final String GET_LOCATIONS = "Get location details by given list of id's";
			public static final String UPDATE_ILLSITES = "Update the illsites based on the quote id";
			public static final String GET_SITE_INFORMATION = "Get site information based on the quote id";
			public static final String GET_BOM_DETAILS_BY_QUOTE_ID = "Create quote based on user input";
			public static final String GET_TOTAL_CHARGES_QUOTE_ID = "Get total charges by quote Id";
			public static final String CREATE_DOCUMENT = "Create Document";
			public static final String GET_USER_DETAILS = "Get user details";
			public static final String GET_USER_SITE_DETAILS = "Get user site details";
			public static final String GET_TAX_EXCEMPTED_SITES = "Get only taxExcempted site";
			public static final String GET_SITE_INFO_BY_ID = "Get site info by site id";
			public static final String DELETE_SITE_INFO = "Delete site info";
			public static final String CREATE_QUOTE = "create the quote";
			public static final String CREATE_TNC = "update special tnc for the quote";
			public static final String DOCUSIGN_RETRY = "docusign retry";
			public static final String GET_TNC = "get special tnc for the quote";
			public static final String UPDATE_SITE = "update the site information to the given quote";
			public static final String SHARE_QUOTE = "Share quote to the input email";
			public static final String UPDATE_LINK = "update the link information to the given quote";
			public static final String GET_QUOTE = "get the quote configuration";
			public static final String DELETE_OR_DISABLE_SITE = "delete or disable the site information for the given quote";
			public static final String GET_TERMS_AND_CONDITIONS = "Get the terms and conditions for the given product name";
			public static final String GET_ALL_USER_DETAILS = "Get all user details based on the customer id";
			public static final String TRIGGER_EMAIL = "Trigger Email to the delegated user";
			public static final String GET_DELEGATED_USERS = "Get the user from quote delegation based on the status";
			public static final String GET_QUOTE_SUMMARY = "Get all the quote summay details";
			public static final String GET_PRODUCT_SOLUTION = "Get Product solution details";
			public static final String EDIT_SITES = "Edit Sites";
			public static final String APPROVE_QUOTES = "Approve Sites";
			public static final String QUOTES_DOCUSIGN = "Docusign for quotes";
			public static final String SFDC_GET = "Get Sfdc Audit";
			public static final String SFDC_TRIGGER = "Trigger Sfdc";
			public static final String FEASIBILITY_CHECK = "Feasibility check";
			public static final String DASH_BOARD_DETAILS = "Get dashboard details";
			public static final String GET_QUOTES_BASED_ON_CUSTOMER = "Get quotes based on customer";
			public static final String SAVE_SITE_PROPERTIES = "Save site properties";
			public static final String GET_SITE_PROPERTIES_ONLY = "Get site properties only";
			public static final String SAVE_LEGALENTITY_ATTRIBUTE = "Save legal entity attribute";
			public static final String SAVE_VPROXY_SOLUTIONS = "Save Vproxy solutions against the IZOSDWAN Quote";
			public static final String GET_VPROXY_SOLUTION_DETAILS="Get Vproxy Solution Details";
			public static final String GET_VPROXY_PRICING_DETAILS="Get Vproxy Pricing details";
			public static final String PERSIST_VPROXY_PRICING_INFO="vproxy pricing information ";
			public static final String GET_ALL_DETAILS_BY_QUOTETOLEID = "Get all attribute details by quote to le id";
			public static final String UPDATE_CURRENCY_IN_QUOTE_TO_LE = "Update the currency code in quote to le";
			public static final String GET_GST_ADDRESS_DTL = "Get GSTIN Address Details";
			public static final String GET_EHS_DETAILS_ONLY = "Get ehs detials";

			public static final String TRIGGER_FEASIBILITY = "Trigger components for feasibility engine";
			public static final String SFDC_UPDATE_STAGE = "Update the sfdc stage";
			public static final String SAVE_FEASIBILITY = "Save the feasible sites";
			public static final String UPDATE_BILLING_INFO_FOR_SFDC = "used to update billing info into oms for sfdc process";
			public static final String TRIGGER_PRICING = "Trigger components for pricing engine";

			public static final String DELETE_OR_DISABLE_LINK = "API to delete or disable link";
			public static final String GET_LINK_INFO_BY_ID = "Get link info by link id";

			public static final String GET_CONTACT_ATTRIBUTE = "used to get contact attribute";
			public static final String UPDATE_SITE_PROPERTIES = "used to update site list of properties ";
			public static final String CURRENCY_CONVERSION = "Convert payment currency into required currency format";
			public static final String EDIT_LINKS = "Edit Links";
			public static final String GET_USER_LINK_DETAILS = "Get user link details";
			public static final String DELETE_NPL_QUOTE = "Delete NPL quote and order details";
			public static final String UPLOAD_IZOSDWAN_BYON_DETAILS="Upload byon details from excel sheet and persist details in data base";
			public static final String UPLOAD_IZOSDWAN_BYON_STATUS="This Api is used to get the status of upload file";
			public static final String DOWNLOAD_LOCATION_TEMPLATE="This Api is used to download byon template and aslo details";
			public static final String CREATE_UPDATE_DELETE_ATTRIBUTES = "used to create/update/delete product component attributes";

			public static final String GET_QUOTE_PRICE_AUDIT = "Get Quote Price audit component wise";
			public static final String DELETE_OR_DISABLE_SOLUTION_AND_SITE = "API to delete or disable solution and sites";
			public static final String GET_SLA_DETAILS = "Get sla details";
			public static final String GET_OUTBOUND_DETAILS = "Get outbound details";
			public static final String GET_OUTBOUNG_SURCHARGE_PRICES = "Get outbound surcharge prices";
			public static final String GET_OUTBOUNG_SURCHARGE_PRICES_IN_EXCEL = "Get outbound surcharge prices in Excel format";
			public static final String TRIGGER_MAIL_GSC = "Trigger Mail Notification";
			public static final String CHECK_MSA_SSA_DOCUMENTS = "Check MSA and SSA Documents";
			public static final String UPDATE_COF_UPLOADED_DETAILS = "Update the details related to the cof uploaded to the storage container";
			public static final String DOWNLOAD_COF_STORAGE = "Download the cof document from storage container";

			public static final String GET_QUOTE_PAGE_CURRENCY = "Get quote page currency";
			public static final String GET_FEASIBLITY_PRICING_DETAILS = "Get feasibility and pricing details based on the given site id input";
			public static final String GET_MACD_ORDER_SUMMARY = "Get order summary of the macd quote based on the quote id and service id input";
			public static final String GET_COMPARED_QUOTES = "Get the NRC and ARC and their components' prices";

			public static final String MOVE_FILES_OBJECT_STORAGE = "Move files from file system to Object storage";

			public static final String UPDATE_USER_DETAILS = "Update the User details";
			public static final String SITE_NOT_FEASIBLE = "Site is not Feasible";
			public static final String REMOVE_UNSELECTED_SOLUTION = "Remove Unselected Solution from Quote";
			public static final String UPDATE_UPLOAD_LINK = "update the upload links information to the given quote";
			public static final String UPADTE_PARTNER_ENTITY_LOCATION = "update the partner legal entity location";

			public static final String SAVE_ASK_PRICE = "Save Ask price into site properties";
			public static final String GET_DISCOUNT_DETAILS = "Get discount details for edited price.";
			public static final String DELETE_OR_DISABLE_QUOTE_CLOUD = "delete or disable the cloud product information for the given quote";
			public static final String ADD_QUOTE_CLOUD = "add new cloud product for the given quote";
			public static final String ADD_QUOTE_COMPONENT = "Add new Quote Component";
			public static final String DELETE_QUOTE_COMPONENT = "Delete new Quote Component";
			public static final String ADD_QUOTE_COMPONENT_ATTR = "Add or Update new Quote Component Attribute";
			public static final String DELETE_QUOTE_COMPONENT_ATTR = "Delete new Quote Component Attibute";
			public static final String UPDATE_SOLUTION_NAME = "Update Solution Name";
			public static final String VALIDATE_ORDERFORM = "Validate Order Form";
			public static final String TRIGGER_WORKFLOW = "Trigger Workflow";
			public static final String UPLOAD_SOLUTION_DOCUMENT = "Upload solution document";
			public static final String DOWNLOAD_SOLUTION_DOCUMENT = "Download solution document";
			public static final String GET_SITE_DOCUMENTS_QUOTE_LE = "Get Site document namse for the quoteToLe";
			public static final String UPDATE_QUOTE_LE_FOR_DEMO = "Update demo related information for the quoteToLe";

			public static final String GET_OPPORTUNITY_DETAILS = "Get Opportunity Details";
			public static final String CREDITCHECK_RETRIGGER = "Credit Check Retrigger";
			public static final String CREATE_CONFGIGURATIONS = "used to create quote gsc details";
			public static final String DELETE_CONFIGURATIONS = "used to delete quote gsc configurations";
			public static final String GET_CONFGIGURATIONS = "used to get quote gsc details";
			public static final String UPDATE_CURRENCY = "convert or update currency based on code";
			public static final String TERMS_IN_MONTHS = "Update Terms in Months";
			public static final String GET_SERVICE_REQUEST = "Get Service Request";
			public static final String GET_SDWAN_CPE_DETAILS="used to get the cpe details by the given vendor name addons and profile";
			public static final String GET_SDWAN_PORT_DETAILS="Used to get cpe port details";
			public static final String GET_NETWORK_SUMMARY_DETAILS="Used to get network summary details";
			public static final String GET_CONFIGURATION_DETAILS="Get Configuration details using site type";
			public static final String UPLOAD_UPGRADE_EXCEL = "Upload Bulk Upgrade Details";
			public static final String SET_BILLING_PARAMETERS = "Set Billing Parameters";

			public static final String GET_CONTRACT_TERM_INFO="Get Contract Term List for the quote in multicircuit";
			public static final String TRIGGER_WORKFLOW_FOR_MF = "Trigger Manual Feasibility Workflow";

			public static final String UPDATE_CROSS_CONNECT_SITE = "update the cross connect site information to the given quote";
			public static final String DELETE_OR_DISABLE_CROSS_CONNECT_SITE = "delete or disable the cross connect site information for the given quote";
			public static final String EDIT_CROSS_CONNECT_SITES = "Edit cross connect Sites";
			public static final String CGW_BANDWIDTH = " Suggest default bandwidth for cloudGateway and to save the user modified bandwidth";
			

            public static final String BYON_PERSIST = "Persists BYON details in IZOSDWAN sites";
            public static final String GET_IZOSDWAN_QUOTE_ATTRIBUTES = "Gets all attributes for the quote";
            public static final String LOCATION_VALIDATION_STATUS="Get BYON location validation update status";
            
            public static final String BYONUPLOADVALIDATIONDONE="This API is used the check whether the uploaded file for BYON is validated or not";
            public static final String BYON100PUPLOADCHECK = "This API is used to return whether BYON details are already updated or not";
            public static final String IZOSDWANSOLUTIONLEVELCHARGES="This API is used to get solution level charges for IZO-SDWAN";
            public static final String ADDORMODIFYQUOTEATTRIBUTES="This API is used to add or modify the quote attributes";
            public static final String UPDATE_QUOTE_STAGE="This API is used to update the quote stage";

            public static final String DELETE_QUOTE_IZOSDWAN = "Delete Quote for IzoSdwan";

			public static final String CROSS_CONNECT_FEASIBILITY_DATA = "cross connect feasibility data";
			public static final String UPDATE_QUOTE_LE_WITH_SERVICEIDS = "Update Quote to le with service ids";
			public static final String UPDATE_QUOTE_LE_ATTRIBUTES = "Update Quote to le attributes";
			public static final String INTRACITY_EXCEPTION_RULES = "Get intracity exception rules";
			public static final String GET_SUB_CATEGORY_FOR_NS_QUOTES = "Validate sub category for NS quotes";
			public static final String GET_VRF_DATA = "Get Vrf data based on siteId";
			public static final String GET_MULTI_VRF_ANNEXURE_DATA = "Get Multi Vrf Annexure Data";


			public static final String GET_LM_PROVIDER_DETAILS = "get LM provider details for a service ID";
			public static final String GET_LE_OWNER_DETAILS = "get LE Owner details for a customer ID";


			public static final String MULTISITE_ILL_GVPN_FILE_UPLOAD = "multisite ill gvpn file upload";
			public static final String MULTISITE_NPL_FILE_UPLOAD = "multisite npl file upload";
			public static final String MULTISITE_ILL_GVPN_STATUS = "get ill gvpn multisite status";
			public static final String MULTISITE_NPL_STATUS = "get npl multisite status";

			public static final String GET_O2C_CALL_STATUS = "Get O2C Call Status at ten percent";
			
			public static final String UPDATE_MULTISITE_BILLING_INFO = "Update multisite billing info";
			public static final String GET_MULTISITE_BILLING_INFO = "Get multisite billing info";
			public static final String UPLOAD_EMAIL_CONFIRMATION_DOCUMENT = "Upload email confirmation document";
			public static final String UPDATE_NSQUOTE_STATUS="Update NsQuote Status in Quote";

			public static final String UPDATE_PRODUCT_IN_SFDC="Update Product in SFDC";
			public static final String DOWNLOAD_SERVICE_SCHEDULE="Download Service Schedule";
			public static final String RESET_MF_TASK_TRIGGERED = "used to reset mf_task_triggered attribute to 0";
			public static final String DELETE_ATTRIBUTE ="Delete Attribute";
			public static final String UPDATE_BILLING_DETAILS ="Update Billing Details";
			public static final String UPDATE_CCA_DETAILS ="Update Billing Details";
			public static final String UPDATE_SITE_FEASIBILITY_SELECTION ="Update Site feasibility response selection";
			public static final String UPDATE_LINK_FEASIBILITY_SELECTION ="Update Link feasibility response selection";
			public static final String UPDATE_COMPONENT_ATTRIBUTES = "Update CPE Attribute value for given quote code";
			public static final String UPDATE_ORDER_TYPE = "Update Sfdc Order type in Order Ill Site to Service";
			public static final String FETCH_FEASIBILITY_PRICING_PAYLOAD="Fetch feasibility pricing request response for quote code";

		}

		public static class Customer {
			private Customer() {
			}

			public static final String GET_CUSTOMER_ID_TRIGGER_EMAIL = "Get the customer email id to trigger email";
			public static final String CREATE_USER_GROUP = "Creates User Group";
			public static final String USER_GROUP = "Get all User groups";
			public static final String UPDATE_USER_GROUP = "used to update user group";
			public static final String DELETE_USER_GROUP = "used to delete userlegal entity groups";
			public static final String SUPPLIER_UNAVAILABLE = "Suppleier Unavailable";
			
			
		}

		public static class UserManagement {
			private UserManagement() {
			}

			public static final String PAGE_USER_GROUP = "Get all User groups";

			public static final String UPDATE_USER_DETAILS = "Used to update user details";

			public static final String GET_USER_DETAILS = "User to get the user details";

			public static final String PUSH_USER_DETAILS = "User to push the user details";

			public static final String GENERAL_TERMS_AND_CONDITIONS = "General Terms and Conditions";
			
			public static final String UPDATE_SHOW_COS_MESSAGE = "Update the show cos message";

		}

		public static class INTERNALSTAGEVIEW {

			private INTERNALSTAGEVIEW() {

			}

			public static final String GET_ALL_ORDERS = "Get all the orders";
			public static final String GET_ORDER_SUMMARY = "Get all order summary";
			public static final String DELETE_QUOTE = "Delete quote";
			public static final String GET_ORDER_DETAILS = "Get order details based on Id";
			public static final String GET_FEASIBLITY_PRICING_DETAILS = "Get feasiblity and pricing details based on site id";
			public static final String EDIT_FEASIBLITY_DETAILS = "Edit feasibility details based on site Id and feasibility Id";
			public static final String NOTIFY_FEASIBLITY_DETAILS = "Notify feasibility details based on site Id and feasibility Id";
			public static final String GET_QUOTE_SUMMARY = "used to get quote summary";
			public static final String DOWNLOADEXCEL = "used to download excel for order summary";
			public static final String GET_DISTINCT_DETAILS_FOR_FILTER = "Get distinct details from orders";
			public static final String GET_ALL_ORDERS_BY_SEARCH = "Get all the orders by search";
			public static final String PROCESS_CUSTOM_FP = "Process Custom FP";
			public static final String GENERATE_COF_PDF = "Generate CoF pdf";
			public static final String SAVE_DISCOUNT_DETAILS = "Save the discounted details in site properties";
			public static final String APPROVE_DISCOUNT = "Approve the discount percent based on the approval level";
			public static final String DELETE_OPPORTUNITY = "Delete orderLite Opportunity";
			public static final String QUOTE_CODE_AUDIT = "Get audit for quote Code";
			public static final String SAVE_WAIVER_DETAILS = "Save the waiver details in site properties";

			// NPL related constants - start
			public static final String GET_NPL_FEASIBLITY_PRICING_DETAILS = "Get feasiblity and pricing details based on link id";
			public static final String GET_NPL_ORDER_EXCEL_DOWNLOAD = "Download the Npl order excel sheet";
			// NPL related constants - end
			
			//IZO-SDWAN
			public static final String POST_TO_UPDATE_PRICE_MANUALLY = "Post API to update pricing manually";
			public static final String UPLOAD_FILE_DOC = "used to upload Document";
			
			
			public static final String GET_SERVICE_DETAILS_FOR_CANCELLATION = "Get service details from MDM Inventory for Cancellation orders";
			public static final String MF_TASK_TRIGGERED = "mf task triggered value set to 0";


		}

		public static class LR {

			private LR() {

			}

			public static final String GET_ALL_ORDERS = "Get all unprocessed Lr";
			public static final String GET_NOTIFICATION = "Push from LR to notification Status";

		}

		public static class Orders {
			private Orders() {
				// DO NOTHING
			}

			public static final String GET_ORDERS = "Get orders based on order id";
			public static final String GET_SITE_INFO = "Get orders based on site id";
			public static final String UPDATE_SITES = "Update orders ill site info";
			public static final String EDIT_ORDER_SITES = "Edit orders sites ";
			public static final String GET_ORDER_ATTACHEMENTS = "Get attachments based on order id";
			public static final String DASH_BOARD_DETAILS = "Get order dashboard details";
			public static final String DASH_BOARD_ORDER_DETAILS = "Get order details";
			public static final String GET_TAX_EXEMPTION_DETAILS = "Get site-wise tax exemption details based on orderToLe id";
			public static final String SAVE_LEGALENTITY_ATTRIBUTE = "Save legal entity attribute";
			public static final String UPDATE_QUOTE_LE_STATUS = "Update the status based on quote to le id";
			public static final String UPDATE_ORDER_LE_STATUS = "Update the status based on order to le id";
			public static final String UPDATE_ORDER_SITE_STATUS = "Update the status based on order site id";
			public static final String UPDATE_QUOTE_SITE_STATUS = "Update the status based on quote site id";
			public static final String GET_ALL_ORDERS = "Get all orders based on product";
			public static final String CUSTOMER_REQ = "Customer Request based on subject and message";
			public static final String SAVE_SITE_PROPERTIES = "used to have update local it contact";
			public static final String UPDATE_OPTY_STAGE = "update opty stage";
			public static final String NEW_ORDER_EMAIL = "Trigger email when a new order is created";
			public static final String GET_PILOT_TEAM_CONTACT_DETAILS = "Get the contact details for the pilot team";
			public static final String GET_SITE_PROPERTIES_ONLY = "Used to get site properties details only";
			public static final String GET_ORDER_ILLSITE_AUDIT_TRAIL = "Get the audit trail details for order Ill sites";
			public static final String GET_CUSTOMER_DETAILS = "Get the customer details based on the user type";
			public static final String EDIT_ORDER_LINKS = "Edit order links ";
			public static final String UPDATE_REQUESTOR_DATE_FOR_LINK = "Update requestor date for a link";
			public static final String UPDATE_ATTRIBUTES = "used to update product component attributes";
			public static final String GET_CONFGIGURATIONS = "used to get order gsc details";
			public static final String UPDATE_CONFIGURATIONS = "Update one or more order configurations";
			public static final String GET_ORDER_ATTRIBUTES = "used to get order related attributes";
			public static final String GET_CONFIGURATION_NUMBERS = "used to get numbers from number inventory API";
			public static final String RESERVE_CONFIGURATION_NUMBERS = "used to reserve numbers from number inventory API";
			public static final String UPLOAD_FILE_DOC = "Upload file document";
			public static final String GET_CONFIGURATION_DOCUMENTS = "Used to get documents applicable for this configuration";
			public static final String UPLOAD_CONFIGURATION_DOCUMENT = "Used to upload document specific to configuration";
			public static final String DOWNLOAD_CONFIGURATION_DOCUMENT = "Used to download document specific to configuration";
			public static final String DOWNLOAD_OUTBOUND_PRICES = "download outbound prices";
			public static final String RETRY_DOWNSTREAM_ORDER = "Retry order processing with downstream system for GSC";
			public static final String GET_LNS_CONFIGURATION_NUMBERS = "used to get Lns city tfn numbers based on city code";
			public static final String UPDATE_TIGER_REQUEST = "update tiger request";
			public static final String UPDATE_ORDER_LE_ATTRIBUTES = "Update Order to le attributes";
			public static final String UPLOAD_EMAIL_CONFIRMATION_DOCUMENT = "Upload email confirmation document";

			public static final String ADD_ORDER_COMPONENT_ATTR = "Add new order cloud attributes";
			public static final String LAUNCH_VM = "To Launch a VM and send a requests to Project & IPC";
			public static final String DOWNLOAD_ORDER_ENRICHMENT_DETAILS = "download order enrichment details as pdf";
			public static final String GET_SECURITY_GRP = "Get Environment,Zone and Department details from Security Group";

			public static final String UPDATE_ATTRIBUTES_IZOSDWAN_CGW ="Update attributes for Izosdwan cloud gateway";
			
			public static final String UPDATE_ORDER_LE_IN_ATTACHMENTS = "Update the order le id in attachments";

			public static final String DOWNLOAD_ZIPPED_COF = "Downloaded zipped Customer Order Forms";
			public static final String UPDATE_ORDER_TO_CASH_ACTION="Update order to cash enabled Status in orders";

		}

		public static class MACD {
			private MACD() {
			}

			public static final String PLACE_MACD_REQUEST = "place a MACD request";
			public static final String GET_MACD_ORDER_SUMMARY = "Get order summary of the macd quote based on the quote id and service id input";
			public static final String REQUEST_FOR_CANCELLATION = "Request for Cancellation based on the given tps service id input";
			public static final String UPDATE_QUOTE = "Update quote related components and attributes";
			public static final String BANDWIDTH_UPDATED_FLAG = "Get the bandwidth Updated flag for service ids";
			public static final String GET_ACTIVE_SLOTS_FOR_SERVICE_ID = "Get active slots for the given service";

		}

		public static class TERMINATION {
			private TERMINATION() {
			}

			public static final String TERMINATION_EMAIL_ACKNOWLEDGEMENT = "Send Email Acknowledgment for Termination Quote";
			public static final String TERMINATION_EMAIL_INITIATION = "Send Email for Termination Initiation";
			public static final String TERMINATION_UPDATE_WAIVER = "Update Waiver details for Termination";
			public static final String TERMINATION_ETC_CALCULATION = "Request ETC calculation for Termination";
			public static final String GENERATE_UPLOAD_TRF = "Generate & Upload Termination Request Form";
			public static final String TERMINATION_VA_STAGE_MOVEMENT = "Trigger Verbal Aggreement Stage Movement for Termination Quote";
			public static final String PROCESS_VA_STAGE_MOVEMENT = "Auto Process Verbal Aggreement Stage Movement for Termination Quotes";
			public static final String FIND_TERMINATION_QUOTES_BY_SERVICE_LIST = "Find list of Termination Quotes by Service Id(s)";
			public static final String GET_UPLOADED_DOCIUMENTS_ATTACHMENTID = "Find all the attachment ids for the given circuit";
			public static final String RETRIEVE_CURRENT_QUOTE_STAGE = "Retrieve Current Quote Stage Information";

		}

		public static class Sla {
			private Sla() {
				// DO NOTHING
			}

			public static final String SAVE_SLA = "Save sla to ill sites";

		}

		public static class Cof {
			private Cof() {
				// DO NOTHING
			}

			public static final String GET_COF_DETAILS = "Get cof details from uuid";

			public static final String SAVE_COF_DETAILS = "save cof details from uuid";

		}

		public static class GSC {

			private GSC() {

			}

			public static final String GET_GSC_QUOTE_SUMMARY = "used to get gsc specific quote summary";
			public static final String TRIGGER_PRICING = "used to get the price details for quote";
			public static final String GET_GSC_QUOTE_DETAILS = "used to get gsc specific quote details by id";
			public static final String GET_GSC_QUOTE_PDF = "used to get gsc quote pdf";
			public static final String GET_GSC_ORDER_DETAILS = "used to get gsc specific order details by id";
			public static final String GET_GSC_COF_PDF = "used to get gsc cof pdf";
			public static final String DELETE_GSC_QUOTE = "used to delete gsc quote";
			public static final String GET_GSC_ORDER_EXCEL_DOWNLOAD = "Download the GSC order excel sheet";
			public static final String GET_EMERGENCY_ADDRESS_DETAILS = "Getting emergency address details";
			public static final String GET_A_TO_Z_EXCEL_DOWNLOAD = "Download the A to Z GSC excel sheet";
			public static final String UPLOAD_OUTBOUND_PRICES = "Upload outbound prices";
			public static final String UPLOAD_EXCEL_STATUS = "Excel upload status";
			public static final String GLOBAL_OUTBOUND_TEMPLATE= "Global Outbound Negotiated Price Template";
			public static final String ORDER_ENRICHMENT_BULK_UPLOAD_EXCEL = "Order Enrichment Bulk Upload Excel";
			public static final String ORDER_ENRICHMENT_TEMPLATE_EXCEL = "Order Enrichment Template Excel";

			public static class MACD {
				private MACD() {
				}

				public static final String PLACE_MACD_REQUEST = "place a MACD request";
			}
		}

		public static class Partner {

			private Partner() {

			}

			public static final String CREATE_PARTNER_OPPORTUNITY = "Create Partner Opportunity";
			public static final String GET_PARTNER_OPPORTUNITY_DETAILS = "Get Opportunity Details";
			public static final String GET_CAMPAIGN_NAMES = "Get Campaign Names";
			public static final String GET_APPLICABLE_PRODUCTS = "Get Applicable Products";
			public static final String GET_PARTNER_CUSTOMER_DETAILS = "Get Partner's Customer Details";
			public static final String GET_RELAY_WARE_TRAINING_DETAILS = "Get Relay ware training Details";
			public static final String UPLOAD_FILE_DOC = "used to upload Document";
			public static final String DOWNLOAD_FILE_DOC = "used to download document";
			public static final String CREATE_PARTNER_ENTITY = "used to create partner entity";
			public static final String GET_CALLIDUS_INFO = "To get callidus information";
			public static final String GET_INDUSTRY_INFO = "To get industry and sub industry information";
			public static final String GET_SALES_FUNNEL_DETAILS = "Get Sales Funnel Details";
			public static final String GET_CUSTOMER_INFO = "To get customer information";
			public static final String GET_RELAY_WARE_SESSION_ID = "Get Relay ware authentication session id";
			public static final String GET_DNB_DATA = "Get DNB Data";
			public static final String CONFIRM_PARTNER_USER_TERMS = "Confirm partner user general terms";
			public static final String UPADTE_PARTNER_ENTITY_LOCATION = "update the partner legal entity location";
			public static final String PARTNER_NEW_TEMP_CUSTOMER = "new temp customer for partner";
            public static final String CREATE_ORG_SECS_ID = "create ORG and SECS ID for new entity";
			public static final String GET_PSAM_PARTNER_LE = "To fetch psam email based on";
			public static final String GET_CAMPAIGN_DETAILS = "Get Campaign Details from SFDC";
			public static final String GET_ORDER_NA_LITE_PRODUCT = "Get order na lite details";
			public static final String CREATE_PARTNER_ORDER_NA_LITE = "Create Partner order na lite";
			public static final String GET_PARTNER_QUOTE_CUSTOMER = "Get customer details for partner quotes";
			public static final String GET_USER_BY_EMAIL = "To fetch psam user based on user emailID";
			public static final String UNARCHIVE_ACCOUNT = "To unarchive account in SFDC";
			public static final String CREATE_ENTITY_BY_PARTNER = "Create entity by partner";
			//public static final String GET_USER_BY_EMAIL = "To fetch psam email based on";

        }
		
		
		public static class CreditCheck{
			private CreditCheck() {}
			
			public static final String QUOTE_CREDIT_CHECK = "Quote Credit Check";
			public static final String GET_CREDITCHECK_STATUS = "Get the status of credit check for quote";
			public static final String CREDITCHECK = "Place or update the credit check related calls to SFDC";
		}

		public static class ThirdPartyService{
			private ThirdPartyService() {}

			public static final String CREATE_OPTY_RETRIGGER = "Create Opportunity Retrigger";
			public static final String CREATE_PRODUCT_RETRIGGER = "Create Product Retrigger";
		}
		
		public static class UCAAS {

			private UCAAS() {
			}

			public static final String UPDATE_QUOTE = "Update Quote";
			public static final String GET_ORDER = "Get Order By Id";
			public static final String GET_UCAAS_QUOTE_PDF = "To get Quote Pdf";
			public static final String GET_UCAAS_COF_PDF = "Get Ucaas COF Pdf";
		}

		public static class GstAddress{
			private GstAddress() {}

			public static final String GET_GST_ADDRESS = "This Api fetch Gst address for the given gst number";
		}

		public static class OrderAmendment{
			private OrderAmendment() {}

			public static final String GET_SERVICE_DETAILS = "Gets the list of service and its status for a given order id";
			public static final String GET_IF_QUOTE_EXISTS = "Checks if quote is already created for given Order and returns the quote id if present";
			public static final String CREATE_QUOTE = "Creates quote or updates the existing quote if changes are made by user";
			public static final String UPDATE_LOCATIONS = "Updates the existing location to the new one";
			public static final String GET_LM_PROVIDER_DETAILS = "Gets the Last mile vendor name , the selected vendor and the type of site : primary or secondary";
			public static final String UPDATE_FEAS_SITE_AS_NON_FEASIBLE = "Updates the  feasible site as NF so Manual Feasibility Workbench can be triggered";
			public static final String UPDATE_STAGES = "Updates the various stages like get quote, check out , order form etc";
			public static final String GET_SITE_TO_SERVICE = "Gets the o2c service Ids for various sites";
			public static final String UPDATE_IF_SHIFT_SITE = "Updates quote category if site is shifted for parent order MACD , Change BW";
			public static final String UPDATE_LM_PROVIDER_DETAILS = "Updates the Last mile vendor name which is selected";
			public static final String UPDATE_AMENDMENT_STATUS = "Updates the amendment status for audit for model for cancellation";


		}
		
		public static class ManualFeasibility{
			private ManualFeasibility(){ } 
				public static final String GET_MF_ATTACHMENT="Get AttachmentIds";
				public static final String GET_MST_MF_PRODUCT_LIST="Get Mst manual feasibility product list";
			}

		public static class GDE{
			private GDE() {}

			public static final String GET_FEASIBILITY_STATUS = "Gets the feasibiity response from MDSO";
			public static final String GET_ORDER_STAGES = "Gets Order Stages";
			public static final String UPDATE_PRICE = "Updates the price manually";
			public static final String GET_GDE_FEASIBLITY_PRICING_DETAILS = "Get feasibility and pricing details of given link id";
			public static final String DOWNLOAD_APPROVED_COF = "Download Approved COF";
			
		}
		
		
		public static class OrderCancellation{
			private OrderCancellation() {}

			public static final String CREATE_QUOTE = "Creates cancellation quote for the order selected by the user that is to be cancelled";
			public static final String GET_IF_QUOTE_EXISTS = "Checks if cancellation quote is already created for given Order and returns the quote id if present";


		}

		
		public static class BULK_FEASIBILITY{
			private BULK_FEASIBILITY() {}

			public static final String BULK_FILE_UPLOAD = "upload file to check for feasibility";
			public static final String GET_ALL_BULK_FEASIBILITY_INFO = "get all bulk feasibility data";
			public static final String BULK_OUTPUT_FILE_DOWNLOAD = "to download bulk output file";

		}


		}


}
