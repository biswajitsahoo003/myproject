package com.tcl.dias.preparefulfillment.swagger.constants;

/**
 * Swagger Constants related information
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface SwaggerConstants {

    interface ApiOperations {
    	
    	  interface NEGOTIATION{
    	    	public static final String PROCESS_SALES_NEGOTIATION_CSM_TASK="Process Sales Negotiation CSM";
    	    	public static final String PROCESS_SALES_NEGOTIATION_CIM_TASK="Process Sales Negotiation CIM";

  		}
    	interface TERMINATION{
  	    	public static final String INTIATE_TERMINATION_ON_TEN_PERCENT="Intiate Termination on 10 Percent Chance";
  	    	public static final String DROP_TERMINATION_QUOTE="Drop Termination Quote";
  	    	public static final String CLOSE_SALES_NEGOTIATION_FOR_TERMINATION = "Close Sales Negotiation For Termination";
  	    	public static final String CLOSE_CONFIRM_ZERO_NODE_MUX_FOR_TERMINATION = "Close Confirm Zero Node Mux For Termination";
  	    	public static final String CUSTOMER_APPOINTMENT_FOR_MUX_RECVOERY = "Customer Appoinment for MUX Recovery";
  	    	public static final String CUSTOMER_APPOINTMENT_FOR_RF_RECVOERY = "Customer Appoinment for RF Recovery";
  	    	public static final String CLOSE_ARRANGE_FIELD_ENGINEER_FOR_MUX_RECOVERY = "Close Arrange Field Engineer for MUX Recovery";
  	    	public static final String CLOSE_ARRANGE_VENODR_FOR_RF_RECOVERY = "Close Arrange Vendor for RF Recovery";
  	    	public static final String CLOSE_CONFIRM_MUX_RECOVERY = "Close Confirm MUX Recovery";
  	    	public static final String CLOSE_CONFIRM_RF_RECOVERY = "Close Confirm RF Recovery";
  	    	
  	    	public static final String CUSTOMER_APPOINTMENT_FOR_CPE_RECVOERY = "Customer Appoinment for CPE Recovery";
  	    	public static final String CLOSE_ARRANGE_VENODR_FOR_CPE_RECOVERY = "Close Arrange Vendor for CPE Recovery";
  	    	public static final String CLOSE_CONFIRM_CPE_RECOVERY = "Close Confirm CPE Recovery";
  	    	
  	    	public static final String CUSTOMER_APPOINTMENT_FOR_MAST_DISMANTLING = "Customer Appoinment for MAST Dismantling";
  	    	public static final String CLOSE_ARRANGE_VENODR_FOR_MAST_DISMANTLING = "Close Arrange Vendor for MAST Dismantling";
  	    	public static final String CLOSE_CONFIRM_MAST_DISMANTLING = "Close Confirm MAST Dismantling";
  	    	public static final String TERMINATION_NPL = "Npl Termination flow";
  	    	public static final String TERMINATION_IAS = "Npl Termination flow IAS/GVPN";
  	    	
  	    	public static final String BLOCK_RESOURCES = "Confirm Block Resources";

  	    	public static final String CLOSE_TERMINATE_OFFNET_BACKHAUL_PO_TRF_DATE_EXTENSION = "Close Terminate Offnet Backhaul PO TRF Date Extension";
  	    	public static final String CLOSE_TERMINATE_OFFNET_PO_TRF_DATE_EXTENSION = "Close Terminate Offnet PO TRF Date Extension";
  	    	
  	    	public static final String CLOSE_TERMINATE_OFFNET_BACKHAUL_PO_CUSTOMER_RETAINED = "Close Terminate Offnet Backhaul PO Customer Retained";
  	    	public static final String CLOSE_TERMINATE_OFFNET_PO_CUSTOMER_REATINED = "Close Terminate Offnet PO Customer Retained";
  	    	
  	    	public static final String TRIGGER_TERMINATION_FULL_FLEDGED = "Trigger Termination Full Fleged Workflow";

  	    	public static final String AUTOMIGRATION = "AUTO-MIGRATION";
  	    	
  	    	public static final String TERMINATION_HOLD = "TERMINATION HOLD";

			public static final String CANCEL_OFFNET_PO = "Cancel Offnet PO";

			public static final String CLOSE_TERMINATE_VALIDATE_SUPPORTING_DOCUMENT="Close the validate supporting document for termination";

		}
    	
        interface Filter{
        	String GET_CUSTOM_FILTER_DETAILS="Lists all the custom filter save by user";
			String SAVE_CUSTOM_FILTER_DETAILS = "Saves custom filter details";
			String DELETE_CUSTOM_FILTER_DETAILS = "Delete custom filter details";
			String FILTER_TASK_BY_ASSIGNED_GROUP = "List all the tasks for given assigned groups";
		}

        interface DASHBOARD {
            public static String GET_SERVICE_INFO = "Get Service Info for given sc Order Id";
            public static String GET_SERVICEDETAIL_INFO = "Get Service Info for given Service Id";
            public static String GET_ORDER_DASHBOARD_COUNT = "Get the Stage count by given scOrderId";
            public static String GET_ORDER_LM_SCENARIO_COUNT = "Get the count of LM ScenarioTypes";
			public static String GET__SERVICE_RECORDS_BY_ORDER_TYPE = "Get the service record informations from given OrderType and ProductName";
			public static String GET_ORDER_TYPE_COUNT = "Get the In-Progress Order count for given Order Type";
            public static String GET_PROCESS_TASK_LOGS = "Get the task log informations from given scOrderId";
            public static String GET_SERVICE_RECORDS = "Get the service record informations from given stageDefKey";
        }
        
        interface Task {
			public static final String GET_TASK_COUNT = "Used to get the task count";
			public static final String GET_USER_DETAILS = "User to get all the user";
			public static final String UPLOAD_VMI = "upload vmi template";

			public static final String GET_LATEST_ACTIVITY = "Used to get the latest activity";
			public static final String GET_TASK_DETAILS = "Used to get the task Details";
			public static final String GET_TASK_DETAILS_BASED_ON_FILTER = "Used to get the task Details based on filter";

			public static final String ASSIGN_TASK = "used to assign the task";
			public static final String UPDATE_COMPONENT_ATTRIBUTES = "update attribute values else inser if not present";
			public static final String UPDATE_SERVICE_ATTRIBUTES = "update service attribute values else inser if not present";
			public static final String GET_TASK_DETAILS_BASED_ON_STATUS = "Used to get the task details fetched for a groupname and group them by status";
			public static final String GET_TASK_BASED_ON_ID = "Used to get the task based on a provided id";
			public static final String DELAY_TASK_DETAILS = "used to get delay task";
			public static final String INITIATE_DAILY_TRACKING = "initiate daily tracking changes";
			public static final String TRIGGER_SDWAN_SOLUTION = "Trigger SDWAN Solution";
			public static final String TRIGGER_SDWAN_IAS_GVPN_UNDERLAY = "Trigger SDWAN IAS/GVPN Underlay";
			public static final String TRIGGER_SDWAN_CGW = "Trigger SDWAN CGW";
			public static final String TRIGGER_SDWAN_BYON_INTERNET_UNDERLAY = "Trigger SDWAN BYON Internet Underlay";
			public static final String TRIGGER_SDWAN_DIA_IWAN_GVPN_INTL_UNDERLAY = "Trigger SDWAN DIA/IWAN/GVPN INTL Underlay";
			public static final String TRIGGER_SDWAN = "Trigger SDWAN";
			public static final String TRIGGER_SDWAN_OVERLAY = "Trigger SDWAN Overlay";
			public static final String INITIATE_CUSTOMER_DELAY_TRACKING = "initiate customer delay tracking changes";
			public static final String GET_TASK_SUMMARY = "Get Task Summary";
			public static final String ENDPOINT_MRN_NOTIFICATION = "Expose Endpoint for MRN notification";
			public static final String GET_TASK_LM_PROVIDER_DETAILS = "Get LM Provider Details for Task";
			public static final String ASSIGN_BULK_TASK = "used to assign the bulk task";
			public static final String TERMINATION_REQUEST = "To trigger termination request";
			public static final String CANCEL_CLR_REQUEST= "Update the service to cancel state";
			public static final String GET_SALES_SUPPORT_EMAIL_IDS = "Get Sales Support Email Ids";
			public static final String PROCESS_SALES_NEGOTIATION_TASK = "Process Sales Negotiation task";
			public static final String CONFIRM_SALES_NEGOTIATION_TASK = "Confirm sales negotiation task";
			public static final String GET_PNR_DETAILS = "Get PNR(Ponit of No Return) Details";
			public static final String TERM_REMAINDER = "Termination Remainder notification";
			public static final String RESOURCE_LIST_TRIGGER = "Get resource released staus service details and trigger flow";
			public static final String CANCEL_IPC_COMMVALID_TASK = "Cancel IPC Comm Valid Task";
			public static final String SEND_EMAIL_TO_CUSTOMER = "send email to customer inside pm tasks";
			public static final String O2C_CANCELLATION_TRIGGER_API = "To trigger cancellation from O2C";	
			public static final String COMPLETE_TASKS = "Complete Tasks";
			public static final String COMPLETE_TRIGGER_TASKS = "Complete Trigger Tasks";
		}
    }

}
