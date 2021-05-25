package com.tcl.dias.servicefulfillment.swagger.constants;

/**
 * Swagger Constants related information
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SwaggerConstants {

	public static class ApiOperations {

		public static final class SAP {
			private SAP() {
			}

			public static final String CPE_INVENTORY_DETAILS = "Used to get CPE inventory details";
			public static final String MST_DATA_CENTER_DETAILS = "Used to get Data centers details";
		}

		public static class Vendor {
			private Vendor() {
			}

			public static final String GET_VENDOR_DETAILS = "Used to get the vendor details";
			public static final String GET_ALL_VENDOR_DETAILS = "Used to get details of all vendors";
			public static final String ADD_VENDOR = "Add a new Vendor";
			public static final String MODIFY_VENDOR_DETAILS = "Modify Vendor Details";
			public static final String DELETE_VENDOR = "Delete Vendor";
			public static final String SEARCH_VENDOR_DETAILS = "Search Vendor";
		}

		public static class LmImplementation {

			private LmImplementation() {

			}

			public static final String SAVE_SITE_READINESS_CONFIRMATION_DETAILS = "save site readiness confirmation details output";
			public static final String CLOSE_TASK = "set task status to closed";
			public static final String SCHEDULE_CUSTOMER_APPOINTMENT = "Schedule Customer Appointment";
			public static final String CREATE_MRN_FOR_MUX = "Create MRN for Mux";
			public static final String INSTALL_MUX = "Create MRN for Mux";
			public static final String RELEASE_MRN_FOR_OSP_IBD_MATERIAL = "Release MRNfor OSP / IBD Material";
			public static final String DEFINE_SCOPE_WORK_PROJECT_PLAN = "Define Scope of Work and Project Plan";
			public static final String PROVIDE_PO_BUILDING_AUTHORITY = "Provide PO to Building Authority";
			public static final String PREPARE_PO_GOVERNMENT_AUTHORITY = "Prepare PO for Government Authority";
			public static final String APPROVE_CAPEX = "Approve Capex";
			public static final String COMPLETE_OSP_WORK = "Complete OSP Work";
			public static final String COMPLETE_IBD_WORK = "Complete IBD Work";
			public static final String PAY_GOVT_AUTHORITY = "Pay to Building Authority";
			public static final String PAY_BUILDING_AUTHORITY = "Pay to Government Authority";
			public static final String CONDUCT_SITE_SURVEY = "Conduct Site Survey Task";
			public static final String PROVIDE_DEMARC_DETAILS = "Provide Demarc Details";
			public static final String COMPLETE_ACCEPTANCE_TESTING = "Complete IBD Acceptance Testing";
			public static final String COMPLETE_OSP_ACCEPTANCE_TESTING = "Complete OSP Acceptance Testing";
			public static final String SAVE_ADDITIONAL_TECHNICAL_DETAILS = "save additional technical details";
			public static final String SAVE_ACE_IPS = "save ace ip's";
			public static final String GET_ACE_IPS = "Get the ace ip's for the given task";
			public static final String SAVE_MUX_DETAILS = "save mux details";
			public static final String SAVE_FIELD_ENGINEER_DETAILS = "Save Field Engineer details";
			public static final String SAVE_VENDOR_DETAILS = "Save Vendor Details";
			public static final String BUILDING_AUTHORITY_CONTRACT = "Building Authority Contct details";
			public static final String CONFIRM_ACCESS_RING = "Confirm access ring";
			public static final String APPLY_PROW = "Used for the Prow implementation";
			public static final String APPLY_ROW = "Used for the Prow implementation";
			public static final String APPLY_FOR_ROW_PERMISSION = "Used for apply of Row permission";
			public static final String DELIVER_MUX = "Deliver Mux";
			public static final String CREATE_WORKORDER_INTERNAL_CABLING = "create-workorder-internal-cabling";
			public static final String COMPLETE_INTERNAL_CABLING = "complete-internal-cabling";
			public static final String INTERNAL_CABLING_COMPLETION_CONFIRMATION = "internal-cabling-completion-confirmation";
			public static final String CONFIRM_LM_ACCEPTANCE = "Used to Confirm Last Mile Acceptance";
			public static final String MAST_INSTALLATION_PLAN = "used for mast installation plan";
			public static final String INTEGRATE_MUX = "Integrate Mux";
			public static final String CONFIGURE_MUX = "Configure Mux";
			public static final String CREATE_INVENTORY_RECORD = "Create Inventory Record";
			public static final String RAISE_PLANNED_EVENT = "Raise Planned Event";
			public static final String PROW_COST_APPROVAL = "used for Prow Cost approval";
			public static final String LM_JEOPARDY = "Used for LM Jeopardy";
			public static final String PROVIDE_RF_DATA_JEOPARDY = "Provide RF Data Jeopardy";
			public static final String UPDATE_TERMINATION_DATE = "Used to update Termination Date";
			public static final String PROVIDE_DEPENDANCY_REMARKS = "Provide dependancy remarks";
			public static final String CREATE_MRN_FOR_KRONE = "Create MRN for Krone";
			public static final String INSTALL_KRONE = "Install Krone";
			public static final String SAVE_BYON_READINESS_DETAILS = "save byon readiness details";
			public static final String SAVE_SDWAN_CGW_DETAILS = "save sdwan cgw advance enrichment details";
			public static final String UPLOAD_SDWAN_LLD_MIGRATION_DOCUMENTS = "Upload sdwan LLD and Migration Documents";
			public static final String SDWAN_LLD_SCHEDULE_CALL = "Sdwan LLD schedule Internal call and kickoff call";
			public static final String SDWAN_ASSIST_CMIP = "Persist Assist CMIP task details";
			public static final String SDWAN_ORDER_DETAILS = "Save Sdwan Order Details";

		}

		public static class Documents {

			private Documents() {

			}

			public static final String GET_MST_DOCUMENT = "Used to get the master document list";
			public static final String GET_MST_SLOTS = "Used to get MSt slot details";

		}

		public static class ExtendNetwork {

			private ExtendNetwork() {

			}

			public static final String GET_VENDOR_DETAILS = "Used to get the vendor details";
			public static final String SCHEDULE_CUSTOMER_APPOINTMENT = "used to create schedule customer appointment";
			public static final String COMPLETE_CABLING = "used for the cabling ";
			public static final String INTERNAL_CABLING = "used for the internal cabling";

		}

		public static class RfLmImplementation {

			public static final String INSTALL_RF = "Install RF";
			public static final String CREATE_MRN = "Create MRN";

			private RfLmImplementation() {

			}

			public static final String PROVIDE_WO_SITE_SURVEY = "Used to provide work order for site survey";
			public static final String INSTALL_MAST = "Build the MAST in customer Site";
			public static final String PREPARE_PO_FOR_AUDIT = "Prepare the PO to be provided to the MAST Audit Vendor";
			public static final String APPROVE_MAST_COMMERCIALS = "used to approve mast commercials";
			public static final String PO_FOR_MAST_PROVIDER = "po for mast provider";
			public static final String AUDIT_INSTALLED_MAST = "Audit the Installed MAST in customer site";
			public static final String DELIVER_RF_EQUIPMENT = "Deliver RF Equipment";

		}

		public static class LMTest {
			private LMTest() {

			}

			public static final String CONDUCT_LM_TEST_ONNET_WIRELINE = "to Conduct LM test for onnet wireline";
			public static final String CONDUCT_LM_TEST_ONNET_WIRELESS = "to Conduct LM test for onnet wireless";
			public static final String CONDUCT_LM_TEST_OFFNET_WIRELESS = "to Conduct LM test for offnet wireless";

		}

		public static class CPEImplementation {
			public static final String PROVIDE_MIN = "Provide min";

			private CPEImplementation() {

			}

			public static final String PROVIDE_PO_FOR_CPE_INSTALLATION = "Provide PO for CPE Installation";
			public static final String PROVIDE_PO_FOR_CPE_SUPPORT = "Provide PO for CPE Support";
			public static final String GENERATE_CPE_INVOICE = "Generate CPE Invoice";
			public static final String GENERATE_MRN_FOR_CPE_TRANSFER = "Generate MRN for CPE Transfer";
			public static final String DISPATCH_CPE = "Dispatch CPE";
			public static final String INSTALL_CPE = "Install CPE";
			public static final String CUSTOMER_CPE_CONFIGURATION_CONFIRMATION = "Customer CPE Configuration Confirmation";
			public static final String CUSTOMER_CPE_INSTALLATION_CONFIRMATION = "Customer CPE Installation Confirmation";
			public static final String PROVIDE_PO_FOR_CPE_ORDER = "Provide PO for CPE Order";

			public static final String NEGOTIATE_COMMERCIALS_WITH_LM_PROVIDERS = "Negotiate Commercials with LM Provider";
			public static final String PROVIDE_PO_TO_LM_PROVIDERS = "Provide PO details to LM Provider";
			public static final String PROVIDE_PO_TO_CROSS_CONNECT_PROVIDERS = "Provide PO Details to cross connect provider";
			public static final String SUPPLIER_ACCEPTANCE = "Get Supplier Acceptance";
			public static final String RAISE_JEOPARDY = "Raise Jeopardy";
			public static final String DEFINE_SOW_PROJECT_PLAN = "Define Scope of Work and Project Plan - Offnet";
			public static final String PROVIDE_KLM_VLAN_DETAILS = "Provide KLM & VLAN Details";
			public static final String TRACK_COMPLETE_LM_DELIVERY = "Track and complete Lm Delivery";
			public static final String OFFNET_SITE_SURVEY = "Offnet site survey Details";
			public static final String CONFIGURE_CPE = "Configure CPE task";
			public static final String CONFIRM_SUPPLIER_CONFIRMATION = "Confirm Supplier Configuration";
			public static final String PROVIDE_PO_TO_LM_PROVIDERS_SITE2 = "Provide PO to LM Providers Site 2";
			public static final String WBS_JEOPARD = "wbs jeopardy";

		}

		public static class ATTACHMENT {
			private ATTACHMENT() {

			}

			public static final String UPDATE_ATTACHMENT_DATA = "Update attachment data";
			public static final String GET_ATTACHMENT_LIST = "Get attachment list";
			public static final String UPLOAD_ATTACHMENT = "Used to upload attachment";
			public static final String UPDATE_ATTACHMENT_STORAGE_URL = "Used to update attachment storage url after Object storage";
			public static final String DOWNLOAD_ATTACHMENT = "Download attachment for given attachment Id";
			public static final String UPLOAD_MANDATORY_ATTACHMENTS = "Upload required attachments";
			public static final String MAST_INSTALLATION_PERMISSION = "for Mast Installation permission";
	     }

		public static class PROCUREMENT {
			private PROCUREMENT() {
				
			}
			 public static final String CREATE_PROCUREMENT = "Create Procurement detail";
			 public static final String GET_PROCUREMENT = "Get Procurement detail";
			 public static final String UPDATE_PROCUREMENT = "Update Procurement detail";
			 public static final String FETCH_PROCUREMENT = "Fetch Procurement detail By ScOrder";
			 public static final String DELETE_PROCUREMENT = "delete Procurement detail";
			 public static final String FETCH_SOLUTION_DETAILS = "fetch Solution detail";
	     }

		public static class WebExImplementation {

			public WebExImplementation() {
			}

			public static final String ADVANCED_ENRICHMENT = "Advanced Enrichment";

			public static final String SOLUTION_DETAILS = "Solution Details";

			public static final String WBSGLCC = "WBS GLCC";

			public static final String PROVIDE_PR_FOR_WEBEX = "PR for Webex Licence";
			public static final String PROVIDE_PO_FOR_WEBEX = "PO for Webex Licence";
			public static final String PROVIDE_PO_REALEASE_FOR_WEBEX = "PO Release for Webex Licence";
			public static final String GENERATE_ORDER_FOR_WEBEX = "Generate Order for Webex Licence";

			public static final String PREPARE_PR_FOR_ENDPOINT = "Prepare PR for Endpoint Installation and Support";
			public static final String CREATE_PO_FOR_ENDPOINT = "Create PO for Endpoint Installation and Support";
			public static final String RELEASE_PO_FOR_ENDPOINT = "Release PO for End Point Installation and Support";
			public static final String CONFIRM_MATERIAL_AVAILABILITY = "Confirm Material Availability";
			public static final String DISPATCH_ENDPOINT = "Dispatch Endpoint";
			public static final String TRACK_ENDPOINT = "Track EndPoint";
			public static final String GENERATE_WEBEX_ENDPOINT_INVOICE = "Generate Webex Endpoint Invoice";
			public static final String GENERATE_MRN_FOR_WEBEX_ENDPOINT_TRANSFER = "Generate MRN for Webex Endpoint Transfer";
			public static final String ACTIVATE_MICROSITE = "Activate Microsite";
			public static final String TD_CREATION_DEDICATED_NUMBERS = "TD Creation for Dedicated Numbers";
			public static final String TD_CREATION_SHARED_NUMBERS = "TD Creation for Shared Numbers";
			public static final String CREATION_CALLBACK_GROUPS = "Creation of Callback Groups";
			public static final String EGRESS_ROUTING_PROFILE = "Egress Routing Profile";
			public static final String INSTALL_ENDPOINT = "Install Endpoint";
			public static final String CONFIGURE_ENDPOINT = "Configure Endpoint";
			public static final String ACTIVATE_VOICE_TO_MICROSITE = "Activat Voice to Microsite";
			public static final String HYBRID_SERVICES_INTEGRATION_WITH_WEBEX = "Hybrid Services Integration with Webex";
			public static final String SRN_GENERATION_ENDPOINT = "SRN Generation Endpoint";
			public static final String ACCESS_CODE_ACTIVATION = "Access Code Activation";
			public static final String CONFIG_ACCESS_CODE = "Config Access Code";
			public static final String COMPONENT_TESTING = "Component Testing";
			public static final String SERVICE_TESTING = "Service Testing";
			public static final String ADFS_SSO = "ADFS, SSO Intergration";
			public static final String PROVIDE_AD = "Provide AD File, SSO";
			public static final String CUSTOMER_ADOPTION = "Customer adoption &Training";
			public static final String CUSTOMER_HANDOVER = "Customer Handover-End Point";

		}

		public static class GscImplementation {

			public GscImplementation() {
			}

			public static final String VOICE_ADVANCED_ENRICHMENT = "Voice Advanced Enrichment";
			public static final String DEDICATED_NUMBER = "Dedicated Number";
			public static final String NUMBER_MAPPING = "Number Mapping";
			public static final String CREATE_TIGER_ORDER = "Create Tiger Order";
			public static final String CUG_CONFIGURATION = "Cug Configuration";
			public static final String TD_CREATION_FOR_CUG = "TD Creation for CUG";
			public static final String CREATE_DP_FORM = "Create Dp Form";
			public static final String E2E_VOICE_TESTING = "E2E Voice Testing";
			public static final String CUSTOMER_ON_BOARDING_FOR_TRANNING = "Customer On Boarding For Tranning";
			
			public static final String REMOVE_NUMBER = "Gsc Remove Number";
			public static final String GET_REMOVED_NUMBERS = "List of TollFree numbers to remove";
		}

		public static class JeopardyFlow {
			private JeopardyFlow() {

			}

			public static final String JEOPARDY = "to trigger jeopardy workflow for tasks";
			public static final String APPROVE_OR_DECLINE_JEOPARDY = "to approve or decline jeopardy workflow for tasks";
			public static final String CLOSE_SALES_NEGOTIATION = "to close sales negotitaion task";

		}

		
		public static class BasicEnrichment {
			private BasicEnrichment() {
				
			}
			
			public static final String BASIC_ENRICHMENT_FOR_VOICE = "Basic Enrichment";
			
			public static final String VALIDATE_DOCUMENT_FOR_VOICE = "Validate Supporting Document";
			public static final String GET_VALIDATE_DOCUMENTS_INFO = "Get Documents Info for Validation";
			public static final String APPROVE_OR_CLARIFY_DOC = "Approve or Clarify Document";
			public static final String RATE_UPLOAD_APPROVAL = "Approve or Clarify Document for Rate Upload task";
			public static final String RATE_UPLOAD = "Rate Upload task";
			public static final String SAVE_SUPPLIER_DETAILS = "Save supplier details";
			public static final String GET_PROVISIONING_VALIDATION_INFO = "Get Provisioning Info for Validation";
			public static final String PROVISIONING_VALIDATION_APPROVAL = "Approve or Clarify Document for Provisioning Validation task";
			public static final String CLOSE_PROVISIONING_INFO_VALIDATION = "Close Provisioning Validation task";
			public static final String UPDATE_BRIDGE = "Update Bridge";
			public static final String GET_SUPPLIER_INFO_FOR_VALIDATION = "Get Supplier Informantion for validation";
			public static final String CLOSE_VALIDATE_SUPPLIER_INTERNAL_DB = "Close Validate Supplier Internal DB task";
			public static final String ENTMM_VALIDATION_APPROVAL = "Approve or Clarify Document for ENTMM task";
			public static final String ENTMM_TASK = "Close ENTMM task";
			public static final String BILLING_PROFILE="Billing Profile / CMS ID";
			public static final String BILLING_PROFILE_APPROVAL="Approve or Clarify Document for Billig Profile Task";
			public static final String VOICE_SALES_ENGR_APPROVAL = "Approve or Clarify Document for Voice Sales Engr task";
			public static final String VOICE_SALES_ENGR = "Voice Sales Engr task";
			public static final String SAVE_SELECTED_SITE = "Save Selected Site";
			public static final String CLOSE_DID_NUMBER_TASK = "Close DID number task";
			public static final String CLOSE_DID_NUMBER_SERVICE_ACCEPTENCE = "Close DID number service acceptence task";
			public static final String CLOSE_DID_NUMBER_TASK_TESTING = "Close DID number task testing";
			public static final String CREATE_NEW_SITE = "Create New Site";
			public static final String  CLOSE_DID_PORTING_RS_TASK="Close DID porting rs task";
			public static final String  CLOSE_DID_REPC_JEOPARDY_TASK="Close DID Jeopardy task";
			public static final String  CLOSE_REPC_UPDATE_JEOPARDY_TASK="Close REPC Update Jeopardy task";
			
			public static final String GET_CHANGE_CONFIG = "Get Change Config";
			public static final String POST_CHANGE_CONFIG = "Post Change Config";
			public static final String CLOSE_BASIC_ENRICHMENT_TASK = "Close Basic enrichment task";

		}
		
		
		public static class SipConfig {
			private SipConfig() {
			}
			public static final String APPROVE_SIPCONFIG = "Approve or Clarify Sip trunk Config";
			public static final String APPROVE_SIP_ROUTELABEL_CREATION = "Approve or Clarify Sip Trunk Routelabel Creation";
			public static final String CLOSE_SIP_ROUTELABEL_CREATION = "Close Sip Trunk Routelabel Creation task";
			public static final String CLOSE_SIP_TRUNK_CONFIG = "Close Sip Trunk Config Task";
			public static final String CLOSE_REPC_DB_CREATION = "Close REPC DB creation Task";
		}
		

		public static class TeamsDRImplementation {

			//media gateway
			public static final String WBSGLCC = "WBS GLCC";

			public static final String PROVIDE_PR_FOR_MEDIA_GATEWAY = "Provide PR for Media Gateway";
			public static final String PROVIDE_PO_FOR_MEDIA_GATEWAY = "Provide PO for Media Gateway";
			public static final String PROVIDE_PO_RELEASE_FOR_MEDIA_GATEWAY = "PO Release for Media Gateway";
			public static final String GENERATE_ORDER_FOR_MEDIA_GATEWAY = "Generate Order for Media Gateway";

			public static final String PREPARE_PR_FOR_ENDPOINT = "Prepare PR for Media Gateway Installation and Support";
			public static final String CREATE_PO_FOR_ENDPOINT = "Create PO for Media Gateway Installation and Support";
			public static final String RELEASE_PO_FOR_ENDPOINT = "Release PO for Media Gateway Installation and Support";
			public static final String CONFIRM_MATERIAL_AVAILABILITY = "Confirm Material Availability";
			public static final String DISPATCH_ENDPOINT = "Dispatch Endpoint";
			public static final String TRACK_ENDPOINT = "Track EndPoint";
			public static final String INSTALL_ENDPOINT = "Install Endpoint";
			public static final String CONFIGURE_ENDPOINT = "Configure Endpoint";
			public static final String ADVANCED_ENRICHMENT = "Advanced Enrichment for Media Gateway";
			//managed services
			public static final String MS_ADVANCE_ENRICHMENT = "Managed Services Advanced Enrichment";
			public static final String SAVE_MANAGEMENT_AND_MONITORING = "Save management and monitoring details";
			public static final String SAVE_TRAINING_DETAILS = "Save Training details";
			public static final String SAVE_SITE_DETAILS = "Save Site details";
			public static final String MS_USER_MAPPING = "Perform User Mapping";
			public static final String MS_UAT_TESTING="Perform UAT Testing for Managed Service";
			public static final String MS_DIRECT_ROUTING = "Perform Direct Routing configuration for Managed Service";
			public static final String MS_END_USER_TRAINING = "Save Managed Services End User Training Details";
			public static final String MS_ADVANCED_LEVEL_TRAINING = "Save Managed Services Advanced Level Training";
		}
}
}
