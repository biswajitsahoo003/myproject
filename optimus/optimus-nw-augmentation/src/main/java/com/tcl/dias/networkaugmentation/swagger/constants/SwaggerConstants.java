package com.tcl.dias.networkaugmentation.swagger.constants;

/**
 * Swagger Constants related information
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface SwaggerConstants {

    interface ApiOperations {
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
			public static final String INITIATE_CUSTOMER_DELAY_TRACKING = "initiate customer delay tracking changes";
			public static final String GET_TASK_SUMMARY = "Get Task Summary";
			public static final String ENDPOINT_MRN_NOTIFICATION = "Expose Endpoint for MRN notification";
			public static final String GET_TASK_LM_PROVIDER_DETAILS = "Get LM Provider Details for Task";
			public static final String ASSIGN_BULK_TASK = "used to assign the bulk task";
			public static final String TERMINATION_REQUEST = "To trigger termination request";
			public static final String ASSIGN_TASK_TRAIL = "API to get assign task trail";

		
		}

		interface DocumentService {
			public static final String CREATE_FOLDER = "Create Folder";
			public static final String LIST_FOLDERS = "List Folders";
			public static final String LIST_FILES = "List Files";
			public static final String UPLOAD_FILE = "Upload File";
			public static final String DELETE_FILE = "Delete File";
			public static final String DELETE_FOLDER = "Delete Folder";
			public static final String DOWNLOAD_FILE = "Download File";
		}
		public static class Attachments {
			private Attachments() {

			}
			public static final String GET_ATTACHMENTS = "This api is used to get all the Attachment details";
			public static final String GET_ATTACHMENTS_DETAILS = "This api is used to get the particular Attachment Details";
			public static final String UPDATE_ATTACHMENTS = "This api is used to update the attachment details";
			public static final String CREATE_ATTACHMENTS = "This Api is used to create Attachments";
			public static final String DELETE_ATTACHMENTS_DETAILS = "This Api is used to delete Attachments";



		}

    }

}
